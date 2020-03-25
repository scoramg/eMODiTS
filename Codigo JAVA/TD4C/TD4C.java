/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TD4C;

import DataMining.Classification.Classification;
import DataSets.Data;
import DataSets.DataSet;
import DataSets.DiscretizedData;
import DataSets.DiscretizedDataSet;
import DataSets.ReconstructedData;
import Exceptions.MyException;
import SAX.SAX;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.trees.J48;


/**
 *
 * @author amarquezgr
 */
public class TD4C {
    private double[] ErrorRatesByFolds;
    private double ErrorRate, threshold, correlationRatio;
    private List<Prediction> predictions;
    private List<Integer> correctPredictions;
    private String DecisionTreeGraph;
    private int bins;
    
    private Map<String, Set<Instance>> classedInstances;
    private IDistanceMeasure distanceMeasure;
//    private Map<Integer, Set<Instancia>> classedInstances;

    public TD4C() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double[] getErrorRatesByFolds() {
        return ErrorRatesByFolds;
    }

    public double getErrorRate() {
        return ErrorRate;
    }

    public List<Integer> getCorrectPredictions() {
        return correctPredictions;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public String getDecisionTreeGraph() {
        return DecisionTreeGraph;
    }

    public void setErrorRatesByFolds(double[] ErrorRatesByFolds) {
        this.ErrorRatesByFolds = ErrorRatesByFolds;
    }

    public void setErrorRate(double ErrorRate) {
        this.ErrorRate = ErrorRate;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public void setDecisionTreeGraph(String DecisionTreeGraph) {
        this.DecisionTreeGraph = DecisionTreeGraph;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
//
//    public void setCorrelationRatio(double correlationRatio) {
//        this.correlationRatio = correlationRatio;
//    }

    public void setBins(int bins) {
        this.bins = bins;
    }

    public double getThreshold() {
        return threshold;
    }
    
    public void getCorrectPredictions(List<Prediction> predictions){
        this.correctPredictions = new ArrayList<>();
        for(int i=0; i<predictions.size(); i++){
            int pred = predictions.get(i).predicted() == predictions.get(i).actual() ? 1 : 0;
            this.correctPredictions.add(pred);
        }
    }

//    public double getCorrelationRatio() {
//        return correlationRatio;
//    }

    public int getBins() {
        return bins;
    }
    
    public TD4C(IDistanceMeasure distanceMeasure, double threshold, double correlationRatio) {
        this.ErrorRatesByFolds = new double[10];
        this.ErrorRate = Double.NaN;
        this.predictions = new ArrayList<>();
        this.DecisionTreeGraph = "";
        this.bins = 0;
        this.correlationRatio = correlationRatio;
        this.threshold = threshold;
        this.distanceMeasure = distanceMeasure;
        this.classedInstances = new LinkedHashMap<>();
    }
    
    public DiscretizedDataSet getDiscretization(DataSet ds){
        TimeSeriesDiscretize_source.symbols = Utils.Utils.getListSymbols();
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        ds_dis.setOriginal(this.Discretize(ds.getOriginal()));
        ds_dis.setTrain(this.Discretize(ds.getTrain()));
        ds_dis.setTest(this.Discretize(ds.getTest()));
            
        return ds_dis;
    }
    
    public DiscretizedData Discretize(Data ds){
        
        DataTable originalTable = new DataTable(ds);
        
        this.classedInstances = new LinkedHashMap<>();
        for (Instance instance : originalTable.getInstances()) {
            Set<Instance> classSet = classedInstances.get(instance.getClassification());
            if (classSet == null) {
                classSet = new LinkedHashSet<>();
                classedInstances.put(instance.getClassification(), classSet);
            }
            classSet.add(instance);
        }
        
        DataTable discreteTable =  discrete(originalTable);
        
        DiscretizedData discretizedData = new DiscretizedData(discreteTable.getInstances().size(), discreteTable.getFeatures().size());
        
//        Set<Feature> keys = discreteTable.getFeaturesSorted();
        int k=0;
        for(Feature<Double> feature: discreteTable.getFeaturesSorted()){
            if (feature.getKey().equals("Class")){
                for (Instance<Integer> instancia : feature.getAllConcritInstances()) {
                    discretizedData.addValue(instancia.getInstance(),0, feature.getValue(instancia).intValue(),true );
                }
            } else {
                for (Instance<Integer> instancia : feature.getAllConcritInstances()) {
                    int valor = (int) feature.getValue(instancia, CsvNumberRepresentation.Integer_Representation);
                    discretizedData.addValue(instancia.getInstance(),k, valor, false );
                }
            }
            k++;
        }
        
        return discretizedData;
    }
    
    
    public DataTable discrete(DataTable table) {
        DataTable discreteTable = new DataTable();
        List<DiscreteFeature> discreteFeatures = new ArrayList<>();
        Feature classFeature = null;
//        int discreteFeatureCount = 0;
        for (Feature feature : table.getFeatures()) {
            if (feature.getKey().equals("Class")) {
                classFeature = feature;
            } else {
                DiscreteFeature discreteFeature = discrete(feature, bins);
                if (discreteFeature.getRank() >= threshold) {
//                    discreteFeature.setDataTable(discreteTable);
                    discreteFeatures.add(discreteFeature);
//                    discreteFeatureCount++;
                }
            }
        }
//        log.info(discreteFeatureCount + " features out of " + table.getFeatures().size() + " were discrete and ranked above threshold (" + threshold + ")");
        discreteFeatures.sort(DiscreteFeature::compareTo);
        Collections.reverse(discreteFeatures);
        int numberOfInstances = table.getInstances().size();
        double correlationThreshold = ((numberOfInstances * correlationRatio - 1) / (double) numberOfInstances);
//        int addedFeatureCount = 0;
        for (DiscreteFeature discreteFeature : discreteFeatures) {
            boolean toAdd = true;
            for (Feature existingFeature : discreteTable.getFeatures()) {
                double correlationRatio = existingFeature.correlationRatio(discreteFeature, CsvNumberRepresentation.Integer_Representation);
                if ( correlationRatio > correlationThreshold) {
                    toAdd = false;
                    break;
                }
            }
            if (toAdd) {
                discreteTable.put(discreteFeature);
//                addedFeatureCount++;
            }
        }
//        log.info(addedFeatureCount + " Non correlated features out of " + discreteFeatureCount + " discrete and ranked features were added to discrete data table");
        discreteTable.put(classFeature);
        return discreteTable;
    }

    public DiscreteFeature discrete(Feature<Double> feature, int bins) {
        List<Double> optionalCutoffs = getOptionalCutoffs(feature);
        List<Double> currentCutoffs = new ArrayList<>();
        for (int i = 1; i < bins && optionalCutoffs.size() > 0; i++) {
            Double bestCutoff = null;
            double bestCutoffDistance = 0;
            for (Double possibleCutoff : optionalCutoffs) {
                List<Double> tempCutoffs = new ArrayList<>(currentCutoffs);
                tempCutoffs.add(possibleCutoff);
                double cutoffDistance = evaluateCutoffs(feature, tempCutoffs);
                if (cutoffDistance >= bestCutoffDistance) {
                    bestCutoffDistance = cutoffDistance;
                    bestCutoff = possibleCutoff;
                }
            }
            currentCutoffs.add(bestCutoff);
            optionalCutoffs.remove(bestCutoff);
            currentCutoffs.sort(Double::compareTo);
        }
        return createDiscreteFeature(feature, currentCutoffs);
    }

    private DiscreteFeature createDiscreteFeature(Feature<Double> feature, List<Double> cutoffs) {
        DiscreteFeature discreteFeature = new DiscreteFeature(feature.getKey(), 0, null);
        discreteFeature.setCutoffs(cutoffs);
        discreteFeature.setRank(evaluateCutoffs(feature, cutoffs));
        for (Instance instance : feature.getAllConcritInstances()) {
            Double value = feature.getValue(instance);
            int discreteIndex = 0;
            for (Double cutoff : cutoffs) {
                if (value < cutoff) {
                    break;
                }
                discreteIndex++;
            }
            discreteFeature.setValue(instance, discreteIndex);
        }
        return discreteFeature;
    }

    private double evaluateCutoffs(Feature<? extends Double> feature, List<Double> cutoffs) {
        cutoffs.sort(Double::compareTo);
        int[][] bins = getBins(feature, cutoffs);
        return distanceMeasure.cutoffScore(classedInstances.size(), bins);
    }

    private int[][] getBins(Feature<? extends Double> feature, List<Double> sortedCutoffs) {
        int[][] bins = new int[classedInstances.size()][sortedCutoffs.size() + 1];
        int classIndex = 0;
        for (String className : classedInstances.keySet()) {
            for (Instance instance : classedInstances.get(className)) {
                if (instance.getSetType().equals(InstanceSetType.TRAIN_SET)) {
                    int cutoffIndex = 0;
                    for (Double cutoff : sortedCutoffs) {
                        if (feature.getValue(instance) < cutoff) {
                            break;
                        }
                        cutoffIndex++;
                    }
                    bins[classIndex][cutoffIndex]++;
                }
            }
            classIndex++;
        }
        return bins;
    }

    private List<Double> getOptionalCutoffs(Feature<Double> feature) {
        List<Double> optionalCutoffs = new ArrayList<>();
        for (Double featureValue : feature.getAllValues()) {
            optionalCutoffs.add(featureValue);
        }
        optionalCutoffs.sort(Double::compareTo);
        return optionalCutoffs;
    }

    public String getType() {
        return distanceMeasure.getName();
    }
    
////    public void loadClassedInstances(Data ds){
////        this.classedInstances = new LinkedHashMap<>();
////        for (Instance instance : instances) {
////            Set<Instance> classSet = classedInstances.get(instance.getClassification());
////            if (classSet == null) {
////                classSet = new LinkedHashSet<>();
////                classedInstances.put(instance.getClassification(), classSet);
////            }
////            classSet.add(instance);
////        }
////    }
//    
//    public DiscretizedData Discretize(Data ds){
//        
//        DiscretizedData discreteTable = new DiscretizedData();
//        
//        List<DiscreteInstancia> discreteInstancias = new ArrayList<>();
//
//        for(int i=0;i<ds.getDimensions()[0];i++){
//            _Feature<Double> instancia = new _Feature(ds.getDataDouble()[i],0);
//            DiscreteInstancia discreteInstancia = discrete(instancia, bins, ds);
//            Double rank = discreteInstancia.getRank();
//            if (rank >= threshold) {
//                  discreteInstancias.add(discreteInstancia);
//            }
//        }
//        
//        discreteInstancias.sort(DiscreteInstancia::compareTo);
//        Collections.reverse(discreteInstancias);
////        int numberOfInstances = ds.getDimensions()[0];
////        double correlationThreshold = ((numberOfInstances * correlationRatio - 1) / (double) numberOfInstances);
//        int ins = 0;
//        for (DiscreteInstancia discreteInstancia : discreteInstancias) {
////            boolean toAdd = true;
////            for (double[] instancia : discreteTable.getDds_discretized()) { // Falta eliminar el valor de la clase. instancia debe ir sin ese valor
////                instancia = Arrays.copyOfRange(instancia, 1, instancia.length);
////                if(instancia.length>0){
////                    _Feature existingInstancia = new _Feature(Utils.Utils.doubleArray2DoubleArray(instancia), 0);
////                    Double corrRatio = existingInstancia.correlationRatio(discreteInstancia);
////                    if (corrRatio > correlationThreshold) {
////                        toAdd = false;
////                        break;
////                    }
////                }
////            }
////            if (toAdd) {
//                int NoAtr=discreteInstancia.getValues().length+1;
//                discreteTable.addEmptyRow(NoAtr);
//                discreteTable.addValue(ins,0, (int) Math.round((double) discreteInstancia.getClase()),true );
//                for(int j=0; j<discreteInstancia.getValues().length; j++){
//                    Double value =  (Double) discreteInstancia.getValue(j);
//                    int valor = (int) Math.round(value.doubleValue());
//                    discreteTable.addValue(ins,j+1, valor, false );
//                }
//                ins++;
////            }
//        }
//        return discreteTable;
//    }
//    
//    public DiscreteInstancia discrete(_Feature<Double> instancia, int bins, Data ds) {
//        List<Double> optionalCutoffs = getOptionalCutoffs(instancia);
//        List<Double> currentCutoffs = new ArrayList<>();
//        for (int i = 1; i < bins && optionalCutoffs.size() > 0; i++) {
//            Double bestCutoff = null;
//            double bestCutoffDistance = 0;
//            for (Double possibleCutoff : optionalCutoffs) {
//                List<Double> tempCutoffs = new ArrayList<>(currentCutoffs);
//                tempCutoffs.add(possibleCutoff);
//                double cutoffDistance = evaluateCutoffs(instancia, tempCutoffs, ds);
//                if (cutoffDistance >= bestCutoffDistance) {
//                    bestCutoffDistance = cutoffDistance;
//                    bestCutoff = possibleCutoff;
//                }
//            }
//            currentCutoffs.add(bestCutoff);
//            optionalCutoffs.remove(bestCutoff);
//            currentCutoffs.sort(Double::compareTo);
//        }
//        return createDiscreteFeature(instancia, currentCutoffs, ds);
//    }
//    
//    private DiscreteInstancia createDiscreteFeature(_Feature<Double> instancia, List<Double> cutoffs, Data ds) {
//        Object[] size = instancia.getValues();
//        DiscreteInstancia discreteFeature = new DiscreteInstancia(size.length);
//        discreteFeature.setCutoffs(cutoffs);
//        discreteFeature.setClase(instancia.getClase());
//        discreteFeature.setRank(evaluateCutoffs(instancia, cutoffs, ds));
////        for (Double value : _Feature.getValues()) {
//        for(int i=0; i<size.length; i++){
//            Double value = instancia.getValue(i);
//            int discreteIndex = 0;
//            for (Double cutoff : cutoffs) {
//                if (value < cutoff) {
//                    break;
//                }
//                discreteIndex++;
//            }
//            discreteFeature.setValue(i, (double) discreteIndex);
//        }
//        return discreteFeature;
//    }
//    
//    private int[][] getBins(_Feature<? extends Double> instancia, List<Double> sortedCutoffs, Data ds) {
//        int[][] bins = new int[ds.getNoClasses()][sortedCutoffs.size() + 1];
//        
//        double[][] datos = ds.getData();
//        for(int i=0; i<datos.length; i++){
//            int clase = ds.getIndexOfClass((int) Math.round(datos[i][0]));
//            for(int j=1; j<datos[i].length;j++){
//                int cutoffIndex = 0;
//                for (Double cutoff : sortedCutoffs) {
//                        if(instancia.getValue(j-1) < cutoff){
//                            break;
//                        }
//                        cutoffIndex++;
//                    }
//                    bins[clase][cutoffIndex]++;
//            }
//        }
//        
//        
//////        ***********
//////        int classIndex = 0;
//////        for (Integer clase : ds.getClasses()) {
////        for(int clase = 0; clase < ds.getNoClasses(); clase++){
////            for (Double[] instance : ds.getInstancesFromKlass(clase,0)) {
////                for(Double d: instance){
////                    int cutoffIndex = 0;
////                    for (Double cutoff : sortedCutoffs) {
////                        if (d < cutoff) {
////                            break;
////                        }
////                        cutoffIndex++;
////                    }
////                    bins[clase][cutoffIndex]++;
////                }
////            }
//////            classIndex++;
////        }
//        return bins;
//    }
//
//    private List<Double> getOptionalCutoffs(_Feature<Double> instancia) {
//        List<Double> optionalCutoffs = new ArrayList<>();
//        Set<Double> values = instancia.getAllValues();
////        int i =1;
//        for (double featureValue : values) {
//            optionalCutoffs.add(featureValue);
//        }
//        optionalCutoffs.sort(Double::compareTo);
//        return optionalCutoffs;
//    }
//
//    private double evaluateCutoffs(_Feature<? extends Double> instancia, List<Double> cutoffs, Data ds) {
//        cutoffs.sort(Double::compareTo);
//        int[][] bins = getBins(instancia, cutoffs, ds);
//        return distanceMeasure.cutoffScore(ds.getNoClasses(), bins);
//    }
    
    public void Classify(DiscretizedDataSet ds_dis, boolean UsingTest, String set_type){
        try {
            J48 j48 = new J48();
            Classification csf = new Classification();
            if(UsingTest){
                DiscretizedData ds_dis_train = ds_dis.getTrain();
                DiscretizedData ds_dis_test = ds_dis.getTest();
                csf = new Classification(ds_dis_train, ds_dis_test);
                switch (set_type){
                    case "WithoutCV":
                        csf.ClassifyWithTraining(j48);
                        break;
                    case "WithCV":
                        double[] errors = csf.ClassifyByCVInTest(j48, 10);
                        this.ErrorRatesByFolds = errors.clone();
                        break;
                    default:
                        csf.ClassifyWithTraining(j48);
                        break;
                }
                this.ErrorRate = csf.getErrorRate();
//                DiscretizedData ds_dis_train = ds_dis.getTrain();
//                DiscretizedData ds_dis_test = ds_dis.getTest();
//                csf = new Classification(ds_dis_train, ds_dis_test);
//                csf.ClassifyWithTraining(j48);
//                this.ErrorRate = csf.getErrorRate();
//                this.predictions = csf.getPredictions();
////                csf = new Classification(ds_dis_train, ds_dis_test);
////                double[] errors = csf.ClassifyByCVInTest(j48, 10);
////                this.ErrorRatesByFolds = errors.clone();
////                this.ErrorRate = csf.getErrorRate();
//                this.predictions = csf.getPredictions();
            } else{
                DiscretizedData data = new DiscretizedData();
                
                switch (set_type){
                    case "original":
                        data = ds_dis.getOriginal();
                        break;
                    case "train":
//                        DiscretizedData ds_dis_train = this.Discretize(dataset.getTrain());
                        data=ds_dis.getTrain();
                        break;
                    case "test":
//                        DiscretizedData ds_dis_test = this.Discretize(dataset.getTest());
                        data=ds_dis.getOriginal();
                        break;
                } 
                csf = new Classification(data);
                double[] errors = csf.ClassifyByCrossValidation(j48);
                this.ErrorRatesByFolds = errors.clone();
                this.ErrorRate = mimath.MiMath.getMedia(errors);
//                this.predictions = csf.getPredictions();
            }
            this.predictions = csf.getPredictions();
            this.getCorrectPredictions(csf.getPredictions());
            this.DecisionTreeGraph = j48.graph();
            
        } catch (Exception ex) { 
          java.util.logging.Logger.getLogger(TD4C.class.getName()).log(Level.SEVERE, null, ex);
      } 
    }
    
    public ReconstructedData Reconstruct(DiscretizedData ds_dis) {
        int attributes = ds_dis.getDimensions()[1];
        int instances = ds_dis.getDimensions()[0];
        ReconstructedData reconstructed = new ReconstructedData(instances,attributes);
        
        for(int f=0; f<ds_dis.getDds_discretized().length;f++){
            double[] norm = Utils.Utils.Normalize(Arrays.copyOfRange(ds_dis.getDds_discretized()[f], 1, ds_dis.getDds_discretized()[f].length));
            double[] row = new double[1+norm.length];
            row[0] = ds_dis.getDds_discretized()[f][0];
            System.arraycopy(norm, 0, row, 1, norm.length);
            reconstructed.addRow(f, row);
        }
        
        return reconstructed;
    }
    
    public String getName(){
        return "TD4C"+"_"+distanceMeasure.getName();
    }
    
    public void ExportGraph(String BDName, int Execution, String Location) throws FileNotFoundException{
        String FileName = "Arbol_e"+(Execution+1);
        
        if(Execution<0){
            FileName = "Arbol_final";
        }
        String directory = Location+"/"+getName()+"/"+BDName+"/Trees";
        

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".txt" )  ){
               out.println( this.DecisionTreeGraph );
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(TD4C.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void ExportErrorRates(DataSet ds, String Location) {
        String FileName = "ErrorRates";
        
        String directory = Location+'/'+this.getName()+'/'+ds.getName();
        
        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".csv" )  ){
            for(double d: this.ErrorRatesByFolds){
               out.println(d);
            }
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(SAX.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    public static void main(String[] args) throws MyException {
        DataSet ds = new DataSet(10,false);
        TD4C td4c = new TD4C(new CosineDistance(), 0, 1.5);
        td4c.setBins(10);
        DiscretizedDataSet ds_dis = td4c.getDiscretization(ds);
        System.out.println(ds_dis.getOriginal().toString());
        System.out.println("Dim Orig:" + Arrays.toString(ds.getTrain().getDimensions()));
        System.out.println("Dim Disc:" + Arrays.toString(ds_dis.getTrain().getDimensions()));
        td4c.Classify(ds_dis, false,"original");
        System.out.println("ER:"+td4c.getErrorRate());
    }
    
    
}
