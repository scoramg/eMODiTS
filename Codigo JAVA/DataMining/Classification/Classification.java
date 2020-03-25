package DataMining.Classification;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;
import weka.associations.Apriori;
import weka.associations.AssociatorEvaluation;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.core.AbstractInstance;
import weka.core.Attribute;
import weka.core.Debug;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;
import weka.filters.unsupervised.attribute.NumericCleaner;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Reorder;

/**
 *
 * @author root
 */
public final class Classification {
//    Classifier classifier;
    Instances DataSource;
    Instances DataTrain;
    Instances DataTest;
    double errorRate;
    List<Prediction> predictions;
//    MiClasificador mCl;

    public Instances getDataSource() {
        return DataSource;
    }

    public Instances getDataTrain() {
        return DataTrain;
    }

    public Instances getDataTest() {
        return DataTest;
    }

    public double getErrorRate() {
        return errorRate;
    }


    public List<Prediction> getPredictions() {
        return predictions;
    }
    
    public void setDataSource(Instances DataSource) {
        this.DataSource = DataSource;
    }

    public void setDataTrain(Instances DataTrain) {
        this.DataTrain = DataTrain;
    }

    public void setDataTest(Instances DataTest) {
        this.DataTest = DataTest;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }

    public Classification() {
        this.errorRate = Double.NaN;
    }
    
    public Classification(DataSets.DiscretizedData source){
        try {
            this.DataSource = ImportFrom("DataSource",source);
            this.errorRate = Double.NaN;
        } catch (Exception ex) {
            Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Classification(double[][] DataSource) {
        try {
            this.DataSource = ImportFrom("DataSource",DataSource);
            this.errorRate = Double.NaN;
        } catch (Exception ex) {
            Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Classification(double[][] DataTrain, double[][] DataTest) {
        try {
            this.DataTrain = ImportFrom("DataSource", DataTrain);
            this.DataTest = ImportFrom("DataSource", DataTest);
            this.errorRate = Double.NaN;
        } catch (Exception ex) {
            Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Classification(Instances DataSource) {    
        this.DataSource = DataSource;
        this.errorRate = Double.NaN;
    } 

    public Classification(Instances DataTrain, Instances DataTest) {
        this.DataTrain = DataTrain;
        this.DataTest = DataTest;
        this.errorRate = Double.NaN;
    }
    
    public Classification(DataSets.DiscretizedData DataTrain, DataSets.DiscretizedData DataTest) throws Exception {    
        TreeSet[] attrValues = AttributeValues(DataTrain, DataTest);
        this.DataTrain = ImportFrom("DataTrain", attrValues, DataTrain);
        this.DataTest = ImportFrom("DataTest", attrValues, DataTest);
        this.errorRate = Double.NaN;
    }
    
    public Classification(DataSets.DiscretizedData Data, DataSets.DiscretizedData DataTrain, DataSets.DiscretizedData DataTest) throws Exception {    
        this.DataSource = ImportFrom("Data", Data);

        Instances structure = new DataSource(this.DataSource).getStructure();
        this.DataTrain = ImportFrom("DataTrain", structure, DataTrain);
        this.DataTest = ImportFrom("DataTest", structure, DataTest);
        
        this.errorRate = Double.NaN;
    }
    
    public Classification(Instances DataSource, Instances DataTrain, Instances DataTest) {    
        this.DataSource = DataSource;
        this.DataTrain = DataTrain;
        this.DataTest = DataTest;
        this.errorRate = Double.NaN;
    }
    
    public double[] ClassifyByCrossValidation(Classifier classifier){
        try {
            System.out.println(DataSource.numInstances());
            classifier.buildClassifier(DataSource);
            Evaluation evalTrainCV = new Evaluation(DataSource);
            double[] res = evalTrainCV.crossValidateModel2(classifier, DataSource, 10, new Random(1));                
            this.setErrorRate(mimath.MiMath.getMedia(res));
            this.predictions = new ArrayList<>(evalTrainCV.predictions());    
            return res;
        } catch (Exception ex) {
            Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public double[] ClassifyByCVInTest(Classifier classifier, int numFolds){
        double[] res = new double[numFolds];
        this.predictions = new ArrayList<>();
        try {
            classifier.buildClassifier(DataTrain);
            Evaluation evalTest = new Evaluation(DataTest);
            Instances test = new Instances(DataTest);
//            System.out.println("Test:");
//            System.out.println(test.toString());
            test.randomize(new Random(1));
            test.stratify(numFolds);
            for(int i=0; i<numFolds;i++){
                Instances testf = test.testCV(numFolds, i);
                evalTest.evaluateModel(classifier, testf); 
                res[i] = (double) evalTest.pctIncorrect()/(double) 100;
                for(Prediction e: evalTest.predictions()){
                    this.predictions.add(e);
                }
            }
            
            this.setErrorRate(mimath.MiMath.getMedia(res));

        } catch (Exception ex) {
            Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    public void ClassifyWithTraining(Classifier classifier){
        try {
            classifier.buildClassifier(DataTrain);
            Evaluation evalTest = new Evaluation(DataTrain);  
            evalTest.evaluateModel(classifier, DataTest); 
            this.setErrorRate((double) evalTest.pctIncorrect()/(double) 100);
            this.predictions = new ArrayList<>(evalTest.predictions());
        } catch (Exception ex) {
            Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ClassifyWithTraining(int IndexClassTrain, int IndexClassTest, Classifier classifier){
        try {
            DataTrain.setClassIndex(IndexClassTrain);                
            classifier.buildClassifier(DataTrain);
            DataTest.setClassIndex(IndexClassTest);
            Evaluation evalTest = new Evaluation(DataTrain);  
            evalTest.evaluateModel(classifier, DataTest); 
            this.setErrorRate((double) evalTest.pctIncorrect()/100);
            this.predictions = new ArrayList<>(evalTest.predictions());
        } catch (Exception ex) {
            Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TreeSet[] AttributeValues(DataSets.DiscretizedData data_train, DataSets.DiscretizedData data_test){
        TreeSet[] attr = new TreeSet[data_train.getIds_discretized()[0].length];
        for(int i=0; i<data_train.getIds_discretized()[0].length;i++){
            attr[i] = new TreeSet();
        }
        for(int j=0; j<data_train.getIds_discretized().length;j++){
            for(int i=0; i<data_train.getIds_discretized()[j].length;i++){
                attr[i].add(data_train.getIds_discretized()[j][i]);
            }
        }
        for(int j=0; j<data_test.getIds_discretized().length;j++){
            for(int i=0; i<data_test.getIds_discretized()[j].length;i++){
                attr[i].add(data_test.getIds_discretized()[j][i]);
            }
        }
        return attr;
    }
    
    public TreeSet[] AttributeValues(double[][] data_train, double[][] data_test){
        TreeSet[] attr = new TreeSet[data_train[0].length];
        for(int i=0; i<data_train[0].length;i++){
            attr[i] = new TreeSet();
        }
        for(int j=0; j<data_train.length;j++){
            for(int i=0; i<data_train[j].length;i++){
                attr[i].add(data_train[j][i]);
            }
        }
        for(int j=0; j<data_test.length;j++){
            for(int i=0; i<data_test[j].length;i++){
                attr[i].add(data_test[j][i]);
            }
        }
        return attr;
    }
    
    public TreeSet[] AttributeValues(DataSets.DiscretizedData data_train){
        TreeSet[] attr = new TreeSet[data_train.getIds_discretized()[0].length];
        for(int i=0; i<data_train.getIds_discretized()[0].length;i++){
            attr[i] = new TreeSet();
        }
        for(int j=0; j<data_train.getIds_discretized().length;j++){
            for(int i=0; i<data_train.getIds_discretized()[j].length;i++){
                attr[i].add(data_train.getIds_discretized()[j][i]);
            }
        }
        return attr;
    }
    
    public static Instances ImportFrom(String name, TreeSet[] attrValues, DataSets.DiscretizedData data){
        ArrayList<Attribute> attributes = new ArrayList<>(data.getDimensions()[1]);
        for(int i=0; i<data.getDimensions()[1];i++){
            List<Integer> values = new ArrayList<>(attrValues[i]);
            List<String> attval = new ArrayList<>();
            for(Integer ivalue: values){
                attval.add(ivalue.toString());
            }
            attributes.add(new Attribute("x"+i, attval, i));
            
        }
        
        Instances res = new Instances(name, attributes,0);
        
        for(int f=0; f<data.getDimensions()[0]; f++){
            
            double[] values = new double[data.getFds_discretized()[f].length];
            for(int j=0;j<data.getFds_discretized()[f].length;j++){
                values[j] = attributes.get(j).indexOfValue(String.valueOf(data.getIds_discretized()[f][j]));
            }
            
            res.add(new DenseInstance(1.0, values));
        }
        res = ConvertNumeric2Nominal(res,"first-last");
        res.setClassIndex(0);
        return res;
    }
    
    public static Instances ImportFrom(String name, TreeSet[] attrValues, double[][] data){
        ArrayList<Attribute> attributes = new ArrayList<>(data[0].length);
        for(int i=0; i<data[0].length;i++){
            List<Double> values = new ArrayList<>(attrValues[i]);
            List<String> attval = new ArrayList<>();
            for(Double ivalue: values){
                attval.add(ivalue.toString());
            }
            attributes.add(new Attribute("x"+i, attval, i));
            
        }
        
        Instances res = new Instances(name, attributes,0);
        
        for(int f=0; f<data.length; f++){
            
            double[] values = new double[data[f].length];
            for(int j=0;j<data[f].length;j++){
                values[j] = attributes.get(j).indexOfValue(String.valueOf(data[f][j]));
            }
            
            res.add(new DenseInstance(1.0, values));
        }
        res = ConvertNumeric2Nominal(res,"first-last");
        res.setClassIndex(0);
        return res;
    }
    
    public static Instances ImportFrom(String name, DataSets.DiscretizedData data){
        ArrayList<Attribute> attributes = new ArrayList<>(data.getDimensions()[1]);
        attributes.add(new Attribute("x0",0));
        for(int i=1; i<data.getDimensions()[1];i++){
            attributes.add(new Attribute("x"+i, i));
        }
        
        Instances res = new Instances(name, attributes,0);
        
        for(int f=0; f<data.getDimensions()[0]; f++){
            
            double[] values = new double[data.getFds_discretized()[f].length];
            for(int j=0;j<data.getFds_discretized()[f].length;j++){
                values[j] = data.getFds_discretized()[f][j];
            }
            
            res.add(new DenseInstance(1.0, values));
        }
//        System.out.println(res);
//        try{
//            res = ConvertNumeric2Nominal(res,"first-last");
//        } catch (java.lang.IllegalArgumentException ex) {
            int decimals = 3;
            res = ApplyNumericCleaner(res, decimals);
            res = ConvertNumeric2Nominal(res,"first-last");
//            cleaner = Filter(classname="weka.filters.unsupervised.attribute.NumericCleaner", options=["-R", "first-last", "-decimals", str(decimals)])
//            cleaner.inputformat(data)
//            data = cleaner.filter(data)
//        }
        res.setClassIndex(0);
        
        return res;
    }
    
    public static Instances ImportFrom(String name, double[][] data){
        ArrayList<Attribute> attributes = new ArrayList<>(data[0].length);
        attributes.add(new Attribute("x0",0));
        for(int i=1; i<data[0].length;i++){
            attributes.add(new Attribute("x"+i, i));
        }
        
        Instances res = new Instances(name, attributes,0);
        
        for(int f=0; f<data.length; f++){
            double[] values = new double[data[f].length];
            for(int j=0;j<data[f].length;j++){
                values[j] = data[f][j];
            }
            res.add(new DenseInstance(1.0, values));
        }
        res = ConvertNumeric2Nominal(res,"first");
        res.setClassIndex(0);
//        System.out.println(res.attribute(1).isNominal());
        
        return res;
    }
    
    public static Instances ImportFrom(String name, Instances Structure, DataSets.DiscretizedData data){
        ArrayList<Attribute> attributes = new ArrayList<>(data.getDimensions()[1]);
        
        for(int i=0; i<Structure.numAttributes();i++){
            attributes.add(Structure.attribute(i));
            
        }
        
        Instances res = new Instances(name, attributes,0);
        
        for(int f=0; f<data.getDimensions()[0]; f++){
            
            double[] values = new double[data.getFds_discretized()[f].length];
            for(int j=0;j<data.getFds_discretized()[f].length;j++){
                values[j] = attributes.get(j).indexOfValue(String.valueOf(data.getIds_discretized()[f][j]));
            }
            
            res.add(new DenseInstance(1.0, values));
        }
        res = ConvertNumeric2Nominal(res,"first-last");
        res.setClassIndex(0);
        
        return res;
    }
    
    public static Instances ImportFrom(String name, Instances Structure, double[][] data){;
        ArrayList<Attribute> attributes = new ArrayList<>(data[0].length);
        
        for(int i=0; i<Structure.numAttributes();i++){
            attributes.add(Structure.attribute(i));
            
        }
        
        Instances res = new Instances(name, attributes,0);
        
        for(int f=0; f<data.length; f++){
            
            double[] values = new double[data[f].length];
            for(int j=0;j<data[f].length;j++){
                values[j] = attributes.get(j).indexOfValue(String.valueOf(data[f][j]));
            }
            
            res.add(new DenseInstance(1.0, values));
        }
        res = ConvertNumeric2Nominal(res,"first-last");
        res.setClassIndex(0);
        
        return res;
    }
    
    public static Instances ConvertNumeric2Nominal(Instances data, String option){
        Instances newData = null;
        try {
            NumericToNominal convert= new NumericToNominal();
            String[] options= new String[2];
            options[0]="-R";
            options[1]= option;
            
            convert.setOptions(options);
            convert.setInputFormat(data);
            
            newData=Filter.useFilter(data, convert);
        } catch (Exception ex) {
            Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newData;
    }
    
    public static Instances ApplyNumericCleaner(Instances data, int decimal){
        Instances newData = null;
        try {
            Filter cleaner = new NumericCleaner();
            
            String[] options = {"-R", "first-last", "-decimals", String.valueOf(decimal)};
            
            cleaner.setOptions(options);
            cleaner.setInputFormat(data);
            
            newData = Filter.useFilter(data, cleaner);
            
        } catch (Exception ex) {
            Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newData;
    }
    

    public Instances MoveAtts(Instances m_Data, int indexfrom, int indexto) throws Exception {
        
        Reorder     reorder;
        String      order;
        int         i;

        order = "";
        if ( (indexfrom > 0) && (indexto <= m_Data.numAttributes()) ) {
            for (i = 1; i < m_Data.numAttributes()+1; i++) {
                // skip new class
                if (i == indexfrom){
                    continue;
                }

                if (i == indexto){
                    order += ",";
                    order += Integer.toString(indexto);
                    order += ",";
                    order += Integer.toString(indexfrom);
                    continue;
                }

                if (!order.equals(""))
                    order += ",";
                order += Integer.toString(i);
            }
//            System.out.println("order:"+order);
            // process data
            reorder = new Reorder();
            reorder.setAttributeIndices(order);
            reorder.setInputFormat(m_Data);
            m_Data = Filter.useFilter(m_Data, reorder);
        }
        return m_Data;
    }
    
    public void Clustering(Clusterer cluster,String ArchivoSalida){
        try {            
            Debug.Clock clockModel = new Debug.Clock();
            clockModel.setUseCpuTime(true);
            clockModel.start();
            cluster.buildClusterer(DataSource);
            
            clockModel.stop();
            
            ClusterEvaluation clusterEval = new ClusterEvaluation();		
            clusterEval.setClusterer(cluster);  
            
            Debug.Clock clock = new Debug.Clock();
            clock.setUseCpuTime(true);

            clock.start();
            clusterEval.evaluateClusterer(DataSource);	
            
            System.out.println(clusterEval.clusterResultsToString());
//            for( double d: clusterEval.getClusterAssignments())
//                System.out.println(d);
                    
            clock.stop();

            //Filter to add the cluster results to the arff file
            AddCluster addClusterOut = new AddCluster();
            addClusterOut.setClusterer(cluster);
            addClusterOut.setInputFormat(DataSource);

            Instances data2 = Filter.useFilter(DataSource, addClusterOut);

            ArffSaver saver = new ArffSaver();
            saver.setInstances(data2);

            File outputFile = new File (ArchivoSalida);
            //Save the file with the new attribute in the data folder
            saver.setFile(outputFile);
            saver.writeBatch();

        } catch (Exception ex) {
            Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        if(this.DataSource != null){
            sb.append("DataSource:").append("\n");
            for(int i=0;i<this.DataSource.numInstances();i++){
                for(int j=0;j<this.DataSource.instance(i).numValues();j++){
                    sb.append(this.DataSource.instance(i).value(j)).append(",");
                }
                sb.deleteCharAt(sb.length()-1).append("\n");
            }
            sb.append("\n");
        }
        if(this.DataTrain != null){
            sb.append("DataTrain:").append("\n");
            for(int i=0;i<this.DataTrain.numInstances();i++){
                for(int j=0;j<this.DataTrain.instance(i).numValues();j++){
                    sb.append(this.DataTrain.instance(i).value(j)).append(",");
                }
                sb.deleteCharAt(sb.length()-1).append("\n");
            }
            sb.append("\n");
        }
        
        if(this.DataTest != null){
            sb.append("DataTest:").append("\n");
            for(int i=0;i<this.DataTest.numInstances();i++){
                for(int j=0;j<this.DataTest.instance(i).numValues();j++){
                    sb.append(this.DataTest.instance(i).value(j)).append(",");
                }
                sb.deleteCharAt(sb.length()-1).append("\n");
            }
        }
        sb.append("\n").append("errorRate = ").append(this.errorRate);
        return sb.toString();
    }
    
}
