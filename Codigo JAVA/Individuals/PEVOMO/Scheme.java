
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Individuals.PEVOMO;

import DataMining.Classification.Classification;
import DataMining.Classification.StatisticRatesCollection;
import DataSets.Data;
import Interfaces.IScheme;
import DataSets.DataSet;
import DataSets.DiscretizedData;
import DataSets.DiscretizedDataSet;
import DataSets.ReconstructedData;
import Exceptions.MyException;
import FitnessFunctions.FitnessFunction;
import Individuals.Proposal.WordCut;
import ca.nengo.io.MatlabExporter;
//import com.github.jabbalaci.graphviz.GraphViz;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mimath.MiMath;
import parameters.Generals;
import parameters.Locals;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.trees.J48;

/**
 *
 * @author amarquezgr
 */
public class Scheme implements IScheme, Cloneable, Comparable<IScheme> {
//    public String scheme_name = "PAA";
    private Word word;
    private Alphabet alphabet;
    private FitnessFunction fitness_function;
    private boolean isSelfAdaptation;
    
    
    private double ErrorRate;
//    private double[] ErrorRatesByFolds;
    private String DecisionTreeGraph;
    private List<Prediction> predictions;
    private List<Integer> correctPredictions;
//    public double[][] MatrixConfusion;
    public StatisticRatesCollection statistic_rates;
    public Classification csf;
//    private String DiscretizedString;

//    private DiscretizedDataSet ds_dis;
    
    @Override
    public Word getWord() {
        return word;
    }

    @Override
    public Alphabet getAlphabet() {
        return alphabet;
    }

    public FitnessFunction getFitness_function() {
        return fitness_function;
    }

    @Override
    public double getErrorRate() {
        return ErrorRate;
    }

    @Override
    public String getDecisionTreeGraph() {
        return DecisionTreeGraph;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

//    @Override
//    public DiscretizedDataSet getDs_dis() {
//        return ds_dis;
//    }
    
    public void setWord(Word word) {
        this.word = word.clone();
    }

    public void setAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet.clone();
    }


    public Scheme(){
        this.word = new Word();
        this.alphabet = new Alphabet();
//        this.ErrorRatesByFolds = new double[10];
//        for (int i=0;i<10;i++){
//            this.ErrorRatesByFolds[i] = Double.NaN;
//        }
    }
    
    public Scheme(Word word, Alphabet alphabet, boolean isSelfAdaptation, int iFitnessFunctionConf) {
        this.word = word;
        this.alphabet = alphabet;
        this.isSelfAdaptation = isSelfAdaptation;
        this.fitness_function = new FitnessFunction(iFitnessFunctionConf);
//        this.ErrorRatesByFolds = new double[10];
//        for (int i=0;i<10;i++){
//            this.ErrorRatesByFolds[i] = Double.NaN;
//        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        double[] w = this.fitness_function.getWeights();
        sb.append("weights = [");
        for(int i=0;i<w.length;i++){
            sb.append(w[i]).append(",");
        }
        sb.deleteCharAt(sb.length()-1).append("]").append("\n");
        sb.append("Fitness Values: ");
        for(int i=0;i<this.fitness_function.getNoFunctions();i++){
            sb.append("[").append(i).append("]:").append(this.fitness_function.getEvaluatedValues()[i]).append(" ");
        }
        sb.append("\n").append("Monoobjective Fitness Value: ").append(this.fitness_function.getEvaluatedValue());
        sb.append(" --- ErrorRate:"+this.ErrorRate).append("\n");
        sb.append("\n").append(word.toString()).append("\n").append(alphabet.toString()).append("\n").append("Fitness Value= ").append(this.getFitnessValue()).append("\n");
        return sb.toString();
    }

    @Override
    public int getNumberWordCuts() {
        return this.word.getWord().size();
    }

    @Override
    public void adjust(Data ds) {
        this.getWord().adjust(ds.getDimensions()[1]);
    }

    @Override
    public DiscretizedData DiscretizeByPAA(Data ds) {
        int[] dimensions = ds.getDimensions();
        int instances = dimensions[0];
        
        DiscretizedData ds_dis = new DiscretizedData(instances, this.word.getWord().size() + 1);
        
        int[] intervals = new int[2];
        
//        boolean LastIsLength=true;
        
        for(int i=0;i<instances;i++){
            intervals[0] = 1;
            ds_dis.addValue(i,0, (int) ds.getValue(i, 0), true); //agrego la clase
            for(int a=0;a<this.word.getWord().size();a++){
                intervals[1] = this.word.getElementAt(a);
                double mean = MiMath.getMedia(ds.getValuesFrom(i, intervals[0], intervals[1]));
                ds_dis.addValue(i,a+1,this.alphabet.getAlphabetByAvg(mean), false);
                intervals[0] = this.word.getElementAt(a);
            }
            
            intervals[1] = ds.getDimensions()[1]-1;
            if(intervals[0] != intervals[1]){
                double mean = MiMath.getMedia(ds.getValuesFrom(i, intervals[0], intervals[1]));
                ds_dis.addValue(i,this.word.getWord().size()+1,this.alphabet.getAlphabetByAvg(mean), false);
//                LastIsLength=false;
            }
        }
                
//        ds_dis.convert2CharArray();
        ds_dis.convert2StringArray();
//        ds_dis.convert2FloatArray();
        return ds_dis;
    }
    
    private List<Individuals.Proposal.WordCut> toWordCut(){
        List<Individuals.Proposal.WordCut> scheme = new ArrayList<>();
        for(Integer cut: this.word.getWord()){
            Individuals.Proposal.WordCut wc = new Individuals.Proposal.WordCut(cut.intValue(),this.alphabet);
            scheme.add(wc);
        }
        return scheme;
    }
    
//    @Override
//    public void setDiscretization(DataSet ds){
//        ds_dis = new DiscretizedDataSet();
//        ds_dis.setOriginal(this.DiscretizeByPAA(ds.getOriginal()));
//        ds_dis.setTrain(this.DiscretizeByPAA(ds.getTrain()));
//        ds_dis.setTest(this.DiscretizeByPAA(ds.getTest()));
//    }
    
    @Override
    public void evaluate(Data ds, double[] weights) {
        try {
            this.fitness_function.setWeights(weights);
            this.fitness_function.Evaluate(ds, this);

        } catch (Exception ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    @Override
    public FitnessFunction getFitnessFunction() {
        return fitness_function;
    }

    @Override
    public void setFitnessFunction(FitnessFunction fitnes_function) {
        this.fitness_function = fitnes_function.clone();
    }

    @Override
    public double getFitnessValue() {
        return fitness_function.getEvaluatedValue();
    }

    @Override
    public void Generate(Data ds, double[][] limits) {
        this.word.Generate(ds.getDimensions()[1]);
        this.alphabet.Generate(limits);
    }

    @Override
    public Scheme Mutate(double MutationRate, boolean isSelfAdaptation, Data ds, double[][] limits) {
        Scheme offspring = new Scheme();
//        Mutate word cuts
        offspring.setWord(this.word.Mutate(MutationRate, ds.getDimensions()[1]));
        
//        Mutate alphabet cuts
        offspring.setAlphabet(this.alphabet.Mutate(MutationRate, limits));
        
        if(isSelfAdaptation){
//            Mutate weights
            offspring.setFitnessFunction(this.fitness_function.MutateWeights(MutationRate).clone());
        } else {
             offspring.setFitnessFunction(this.fitness_function.clone());
        }
        return offspring;
    }

    @Override
    public String getName() {
        return "PEVOMO";
    }
    
    @Override
    public void Export(Generals general_params, Locals local_params) {
        try {
            String FileName = local_params.getDs().getName()+"_"+general_params.population.getBest().getName()+"_e"+(local_params.getExecution()+1);
            String directory = general_params.population.getBest().getName()+"/"+local_params.getDs().getName();
            
            float[][] DataWord = new float[1][this.word.getWord().size()];
            for(int i = 0; i<this.word.getWord().size();i++){
                DataWord[0][i] = this.word.getElementAt(i).floatValue();
            }
            general_params.exporter.add("word", DataWord);

            float[][] DataAlphabet = new float[1][this.alphabet.getAlphabet().size()];
            for(int i = 0; i<this.alphabet.getAlphabet().size();i++){
                DataAlphabet[0][i] = this.alphabet.getElementAt(i).floatValue();
            }
            general_params.exporter.add("alphabet", DataAlphabet);
            
            
            float[][] DataFunctionValues = new float[1][this.fitness_function.getNoFunctions()];
            for(int i=0;i<this.fitness_function.getNoFunctions();i++){
                DataFunctionValues[0][i] = (float) this.fitness_function.getEvaluatedValues()[i];
            }
            general_params.exporter.add("fitness_values", DataFunctionValues);
            
            float[][] er = new float[1][1];
            er[0][0] = (float) getErrorRate();
            general_params.exporter.add("ErrorRate", er);
            
//            int iDS = local_params.getDs().getIndex();
//            DataSet ds_train = local_params.getDs();
//            DataSet ds = new DataSet(iDS, "");
//            DataSet ds_test = new DataSet(iDS, "_TEST");
            
            DiscretizedData ds_dis = this.DiscretizeByPAA(local_params.getDs().getOriginal());
            DiscretizedData ds_dis_train = this.DiscretizeByPAA(local_params.getDs().getTrain());
            DiscretizedData ds_dis_test = this.DiscretizeByPAA(local_params.getDs().getTest());
            
            general_params.exporter.add("ds_dis", ds_dis.getFds_discretized());
            general_params.exporter.add("ds_dis_train", ds_dis_train.getFds_discretized());
            general_params.exporter.add("ds_dis_test", ds_dis_test.getFds_discretized());
            
            File FileDir = new File(directory);
            if(!FileDir.exists()) FileDir.mkdirs();
            
            File FileTabla = new File(directory+"/"+FileName+".mat");
            
//        individual.add2Export(general_params);
            
            float[][] DataTime = new float[1][2];
            DataTime[0][0] = (float) local_params.getTimeAlgorithm();
            DataTime[0][1] = (float) general_params.getTotalTime();
            general_params.exporter.add("Time", DataTime);
            
            general_params.exporter.add("ConvergenceData", local_params.ConvergenceData);
            
            general_params.exporter.add("MeasuresData", local_params.MeasuresData);
            
            general_params.exporter.write(FileTabla);
        } catch (IOException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @Override
    public void ImportFromMatFile(String filename, int ds_length){
        try {
            MatFileReader mfr = new MatFileReader(filename);
            Map<String, MLArray> mlArrayRetrived = mfr.getContent();
            
            MLArray w = mlArrayRetrived.get("word");
            double[][] arr = ((MLDouble) w).getArray();
//            StringBuilder sb = new StringBuilder();
            for(double[] fila: arr){
                for(int i=0;i<fila.length;i++){
                    this.word.InsertCut((int) fila[i],ds_length);
                }
            }
            
            MLArray a = mlArrayRetrived.get("alphabet");
            double[][] arrA = ((MLDouble) a).getArray();
            List<Double> alph = new ArrayList<>();
            for(double[] filaA: arrA){
                for(int i=0;i<filaA.length;i++){
                    if(!Double.isNaN(filaA[i])){
                        alph.add(filaA[i]);
                    }
                }
            }
            
            this.alphabet.setAlphabet(alph);
            
            MLArray er = mlArrayRetrived.get("ErrorRate");
            double[][] ER = ((MLDouble) er).getArray();
            this.ErrorRate = ER[0][0];
            
            MLArray fit = mlArrayRetrived.get("fitness_values");
            double[][] arrfit = ((MLDouble) fit).getArray();
            this.fitness_function = new FitnessFunction();
            this.fitness_function.setEvaluatedValues(arrfit[0]);
           
        } catch (IOException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    @Override
    public IScheme clone() {
        try {
            super.clone();
            Scheme clon = new Scheme();
            clon.setWord(this.word);
            clon.setAlphabet(this.alphabet);
            clon.setFitnessFunction(this.fitness_function);
            clon.ErrorRate = this.ErrorRate;
            clon.DecisionTreeGraph = this.DecisionTreeGraph;
            clon.isSelfAdaptation = this.isSelfAdaptation;
//            clon.ds_dis = this.ds_dis.clone();
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Override
    public int compareTo(IScheme o) {
        Individuals.PEVOMO.Scheme ind = (Individuals.PEVOMO.Scheme) o;
        if (this.getFitnessValue() < ind.getFitnessValue()) {
            return -1;
        }
        else {
            if (this.getFitnessValue() > ind.getFitnessValue()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    
    public double[] toDoubleArray(){
        
        List<Double> a = new ArrayList<>();
        
        for(Integer w:this.word.getWord()){
            a.add((double) w);
        }
        
        for(Double alph: this.getAlphabet().getAlphabet()){
            a.add(alph);
        }
        
        double[] array = new double[a.size()];
        
        for(int i=0;i<a.size();i++){
            array[i] = a.get(i);
        }
        
        return array;
        
    }

    @Override
    public boolean isIsSelfAdaptation() {
        return isSelfAdaptation;
    }

    @Override
    public String PrintCuts() {
        return this.word.toString();
    }

    @Override
    public List<Integer> getTotalCuts() {
        List<Integer> list = new ArrayList<>();
        list.add(this.getNumberWordCuts());
        for(int i = 0; i<this.getNumberWordCuts();i++){
            list.add(getNumberAlphabetCuts());
        }
        return list;
    }

    @Override
    public double[] getEvaluatedValues() {
        return this.fitness_function.getEvaluatedValues();
    }

    @Override
    public void setRank(int rank) {
        throw new UnsupportedOperationException("No aplica."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<IScheme> getDominationSet() {
        throw new UnsupportedOperationException("No aplica."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getDominatedCount() {
        throw new UnsupportedOperationException("No aplica."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void IncreaseDominatedCount() {
        throw new UnsupportedOperationException("No aplica."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void DecreaseDominatedCount() {
        throw new UnsupportedOperationException("No aplica."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCrowdingDistance(double CrowdingDistance) {
        throw new UnsupportedOperationException("No aplica."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getCrowdingDistance() {
        throw new UnsupportedOperationException("No aplica."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getElementAt(int index) {
        throw new UnsupportedOperationException("No aplica."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void empty() {
        this.word = new Word();
        this.alphabet = new Alphabet();
    }

    @Override
    public void add(double[][] front) {
        Word w = new Word();
        for(Double d : front[0]){
            if(!d.isNaN()) w.addWordCut(d.intValue());
        }
        
//        List<Double> a = new ArrayList<>();
        Alphabet a = new Alphabet();
        for(Double d : front[1]){
            if(!d.isNaN()) a.InsertCut(d);
        }
        
        this.word.setWord(w.getWord());
        this.alphabet.setAlphabet(a.getAlphabet());
    }
    
    @Override
    public void add(Object o) {
        throw new UnsupportedOperationException("No aplica."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sort() {
        throw new UnsupportedOperationException("No aplica."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getRank() {
        throw new UnsupportedOperationException("No aplica."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void Classify(DiscretizedDataSet dataset, boolean UsingTest, String set_type){
        try {
            
            J48 j48 = new J48();
            
            csf = new Classification();
            if(UsingTest){
                csf = new Classification(dataset.getTrain(), dataset.getTest());
                switch (set_type){
                    case "WithoutCV":
                        csf.ClassifyWithTraining(j48);
                        break;
                    case "WithCV":
                        csf.ClassifyByCVInTest(j48, 10);
//                        this.ErrorRatesByFolds = errors.clone();
                        break;
                    default:
                        csf.ClassifyWithTraining(j48);
                        break;
                }
                this.ErrorRate = csf.getErrorRate();
//                this.predictions = csf.getPredictions();
            } else{
                DiscretizedData data = new DiscretizedData();
                
                switch (set_type){
                    case "original":
                        DiscretizedData ds_dis = dataset.getOriginal();
                        data = ds_dis;
                        break;
                    case "train":
                        DiscretizedData ds_dis_train = dataset.getTrain();
                        data=ds_dis_train;
                        break;
                    case "test":
                        DiscretizedData ds_dis_test = dataset.getTest();
                        data=ds_dis_test;
                        break;
                } 
                csf = new Classification(data);
                csf.ClassifyByCrossValidation(j48);
//                this.ErrorRatesByFolds = errors.clone();
                this.ErrorRate = mimath.MiMath.getMedia(csf.eval.getErrorRatesByFolds());
//                this.predictions = csf.getPredictions();
            }
            this.predictions = csf.getPredictions();
            this.getCorrectPredictions(csf.getPredictions());
//            this.MatrixConfusion = csf.getMatrixConfusion();
            statistic_rates = new StatisticRatesCollection(csf);
            this.DecisionTreeGraph = j48.graph();
            
            
        } catch (MyException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void Classify(DataSet dataset, boolean UsingTest, String set_type){
        try {
            
            J48 j48 = new J48();
            
            csf = new Classification();
            if(UsingTest){
                DiscretizedData ds_dis_train = this.DiscretizeByPAA(dataset.getTrain());
                DiscretizedData ds_dis_test = this.DiscretizeByPAA(dataset.getTest());
                csf = new Classification(ds_dis_train, ds_dis_test);
                switch (set_type){
                    case "WithoutCV":
                        csf.ClassifyWithTraining(j48);
                        break;
                    case "WithCV":
                        csf.ClassifyByCVInTest(j48, 10);
//                        this.ErrorRatesByFolds = errors.clone();
                        break;
                    default:
                        csf.ClassifyWithTraining(j48);
                        break;
                }
                this.ErrorRate = csf.getErrorRate();
                ds_dis_train.destroy();
                ds_dis_test.destroy();
//                this.predictions = csf.getPredictions();
//                DiscretizedData ds_dis_train = this.DiscretizeByPAA(dataset.getTrain());
//                DiscretizedData ds_dis_test = this.DiscretizeByPAA(dataset.getTest());
//                csf = new Classification(ds_dis_train, ds_dis_test);
//                csf.ClassifyWithTraining(j48);
//                this.ErrorRate = csf.getErrorRate();
//                this.predictions = csf.getPredictions();
////                csf = new Classification(ds_dis_train, ds_dis_test);
////                double[] errors = csf.ClassifyByCVInTest(j48, 10);
////                this.ErrorRatesByFolds = errors.clone();
////                this.ErrorRate = csf.getErrorRate();
////                this.predictions = csf.getPredictions();
            } else{
                DiscretizedData data = new DiscretizedData();
                
                switch (set_type){
                    case "original":
                        DiscretizedData ds_dis = this.DiscretizeByPAA(dataset.getOriginal());
                        data = ds_dis.clone();
                        ds_dis.destroy();
                        break;
                    case "train":
                        DiscretizedData ds_dis_train = this.DiscretizeByPAA(dataset.getTrain());
                        data=ds_dis_train.clone();
                        ds_dis_train.destroy();
                        break;
                    case "test":
                        DiscretizedData ds_dis_test = this.DiscretizeByPAA(dataset.getTest());
                        data=ds_dis_test;
                        ds_dis_test.clone();
                        break;
                } 
                csf = new Classification(data);
                csf.ClassifyByCrossValidation(j48);
//                this.ErrorRatesByFolds = errors.clone();
                this.ErrorRate = mimath.MiMath.getMedia(csf.eval.getErrorRatesByFolds());
//                this.predictions = csf.getPredictions();
                data.destroy();
            }
            this.predictions = csf.getPredictions();
            this.getCorrectPredictions(csf.getPredictions());
//            this.MatrixConfusion = csf.getMatrixConfusion();
            statistic_rates = new StatisticRatesCollection(csf);
            this.DecisionTreeGraph = j48.graph();
//            this.DiscretizedString = ds_dis.getOriginal().PrintStrings();
            
        } catch (MyException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    @Override
//    public void Classify(DataSet ds){
//        try {
//            J48 j48 = new J48();
//            
//            DiscretizedDataSet ds_dis_train = this.DiscretizeByPAA(ds.getTrain());
//            DiscretizedDataSet ds_dis_test = this.DiscretizeByPAA(ds.getTest());
//            
//            Classification csf = new Classification(ds_dis_train, ds_dis_test);
//            csf.ClassifyWithTraining(j48);
//            this.ErrorRate = csf.getErrorRate();
//            this.DecisionTreeGraph = j48.graph();
//        } catch (Exception ex) {
//            Logger.getLogger(MOScheme.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    @Override
    public void ExportGraph(Locals local_params){
        String FileName = "Arbol_e"+(local_params.getExecution()+1);
       
        String directory = getName()+"/"+local_params.getDs().getName()+"/Trees";
        

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".txt" )  ){
               out.println( this.DecisionTreeGraph );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        GraphViz gv = new GraphViz();
//        gv.readSource(directory+"/"+FileName+".txt");
//    //         System.out.println(gv.getDotSource());
//        String type = "png";
//        String repesentationType= "dot";
//        File out = new File(directory+"/"+FileName+"."+ type);  
//        gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
    }

    @Override
    public void setDominatedCount(int DominatedCount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDominationSet(List<IScheme> DominationSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNoFunctions() {
        return this.fitness_function.getNoFunctions();
    }

    @Override
    public void ExportGraph(DataSet ds, String folder, String Location, String Selector) {
        String directory = Location+"/"+folder+"/"+ds.getName()+"/Trees";
        String FileName = "Arbol_final";

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();
//        System.out.println(directory+"/"+FileName+".txt");
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".txt" )  ){
            out.println( this.DecisionTreeGraph );
        } catch (FileNotFoundException ex) {
//            System.out.println(ex);
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getMaxSizeWordAlphabet(){
        if (this.word.getWord().size() > this.alphabet.getAlphabet().size()){
            return this.word.getWord().size();
        } else {
            return this.alphabet.getAlphabet().size();
        }
        
    }
    
    @Override
    public float[][] Elements2FloatArray() {
        float[][] Data = new float[2][this.getMaxSizeWordAlphabet()+1];
        for(int i =0; i < 2;i++){
            for(int j=0 ; j < this.getMaxSizeWordAlphabet()+1; j++){
                Data[i][j] = Float.NaN;
            }
        }
        for(int i = 0; i<this.word.getWord().size();i++){
            Data[0][i] = this.word.getElementAt(i).floatValue();
        }
        for(int i = 0; i<this.alphabet.getAlphabet().size();i++){
            Data[1][i] = this.alphabet.getElementAt(i).floatValue();
        }
        return Data;
    }

    @Override
    public void Export(DataSet dataset, MatlabExporter exporter) {
//        try {
//            float[][] Data = Elements2FloatArray();
            float[][] DataWord = new float[1][this.word.getWord().size()];
            for(int i = 0; i<this.word.getWord().size();i++){
                DataWord[0][i] = this.word.getElementAt(i).floatValue();
            }
            exporter.add("word", DataWord);
            
            float[][] DataAlphabet = new float[1][this.alphabet.getAlphabet().size()];
            for(int i = 0; i<this.alphabet.getAlphabet().size();i++){
                DataAlphabet[0][i] = this.alphabet.getElementAt(i).floatValue();
            }
            exporter.add("alphabet", DataAlphabet);
            
            
            float[][] DataFunctionValues = new float[1][this.getEvaluatedValues().length];
            for(int i=0;i<this.getEvaluatedValues().length;i++){
                DataFunctionValues[0][i] = (float) this.fitness_function.getEvaluatedValues()[i];
            }
            exporter.add("fitness_values", DataFunctionValues);
            
            float[][] er = new float[1][1];
            er[0][0] = (float) getErrorRate();
            exporter.add("ErrorRate", er);
//            int iDS = dataset.getIndex();
//            DataSet ds_train = dataset.clone();
//            DataSet ds = new DataSet(iDS, "");
//            DataSet ds_test = new DataSet(iDS, "_TEST");
            
            DiscretizedData ds_dis = this.DiscretizeByPAA(dataset.getOriginal());
            DiscretizedData ds_dis_train = this.DiscretizeByPAA(dataset.getTrain());
            DiscretizedData ds_dis_test = this.DiscretizeByPAA(dataset.getTest());
            
            exporter.add("ds_dis", ds_dis.getFds_discretized());
            exporter.add("ds_dis_train", ds_dis_train.getFds_discretized());
            exporter.add("ds_dis_test", ds_dis_test.getFds_discretized());
//        } catch (MyException ex) {
//            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public int getNumberAlphabetCuts() {
        return this.alphabet.getAlphabet().size();
    }

    @Override
    public Classification getClassificationModel() {
        return this.csf;
    }

    @Override
    public void ExportStrings(DataSet ds, String Location) {
        String FileName = "DiscretizedString";
        String directory = Location+getName()+"/"+ds.getName()+"/Strings";
        

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".txt" )  ){
                DiscretizedData ds_dis = this.DiscretizeByPAA(ds.getOriginal());
               out.println( ds_dis.PrintStrings() );
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void Export2JSON(DataSet dataset, String Location) {
        String FileName = getName()+"_"+dataset.getName();
        String FileNameTrain = getName()+"_"+dataset.getName()+"_TRAIN";
        String FileNameTest = getName()+"_"+dataset.getName()+"_TEST";
        
        String directory = Location+getName()+"/JSONMODiTS";
        

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();
        
        DiscretizedData ds_dis = this.DiscretizeByPAA(dataset.getOriginal());
        DiscretizedData ds_dis_train = this.DiscretizeByPAA(dataset.getTrain());
        DiscretizedData ds_dis_test = this.DiscretizeByPAA(dataset.getTest());

        
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".json" )  ){
               out.println( ds_dis.PrintIntContinuos2JSON());
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileNameTrain+".json" )  ){
               out.println( ds_dis_train.PrintIntContinuos2JSON());
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileNameTest+".json" )  ){
               out.println( ds_dis_test.PrintIntContinuos2JSON());
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Integer> Cuts2Intervals(){
        List<Integer> intervals = new ArrayList<>();
        Integer[] CutIntervals = new Integer[2];
        CutIntervals[0] = 0;
        
        for(Integer wc: this.word.getWord()){
            CutIntervals[1] = wc;
            intervals.add(CutIntervals[1]-CutIntervals[0]);
            CutIntervals[0] = wc;
        }
        
        return intervals;
    }

    @Override
    public ReconstructedData Reconstruct(DiscretizedData ds_dis) {
        int attributes = this.word.getElementAt(this.getNumberWordCuts()-1)+1;
        int instances = ds_dis.getDimensions()[0];
        ReconstructedData reconstructed = new ReconstructedData(instances,attributes);
        
        List<Integer> diffs = Cuts2Intervals();
        List<Integer> maximums = this.getTotalCuts();
        
//        System.out.println("ds_dis.dimensions: ["+ds_dis.getDimensions()[0]+","+ds_dis.getDimensions()[1]+"]");
//        System.out.println("diffs:"+diffs.toString());
//        System.out.println("maximums:"+maximums.toString());
        
        for(int f=0; f<ds_dis.getIds_discretized().length;f++){
            double[] row = new double[1];
            row[0] = ds_dis.getIds_discretized()[f][0];
            for(int c=1; c<ds_dis.getIds_discretized()[f].length;c++){
                double value = (double) ds_dis.getIds_discretized()[f][c] / maximums.get(c);
                double[] normvalues = new double[diffs.get(c-1)]; 
                java.util.Arrays.fill(normvalues, value);
                double[] aux = new double[row.length + normvalues.length];
                
                System.arraycopy(row, 0, aux, 0, row.length);
                System.arraycopy(normvalues, 0, aux, row.length, normvalues.length);
                
                row = aux.clone();
            }
            reconstructed.addRow(f, row);
        }
        
        return reconstructed;
    }

    @Override
    public List<WordCut> getElements() {
        List<WordCut> res = new ArrayList<>();
        for(Integer w : this.word.getWord()){
            WordCut wc = new WordCut(w, alphabet);
            res.add(wc);
        }
        return res;
    }
    
    @Override
    public String Predictions2CSV(){
        StringBuilder sb = new StringBuilder();
        for(Prediction p : predictions){
            sb.append(p.actual()).append(",").append(p.predicted()).append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public void ExportErrorRates(DataSet ds, String folder, String Location, String type_selection) {
        String FileName = "ErrorRates";
        
        String directory = Location+'/'+folder+'/'+ds.getName();
        
        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".csv" )  ){
            for(double d: this.csf.eval.getErrorRatesByFolds()){
               out.println(d);
            }
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }

    @Override
    public void getCorrectPredictions(List<Prediction> predictions) {
        this.correctPredictions = new ArrayList<>();
        for(int i=0; i<predictions.size(); i++){
            int pred = predictions.get(i).predicted() == predictions.get(i).actual() ? 1 : 0;
            this.correctPredictions.add(pred);
        }
    }

    @Override
    public List<Integer> getCorrectPredictions() {
        return this.correctPredictions;
    }

    @Override
    public StatisticRatesCollection getStatisticRateCollection() {
        return this.statistic_rates;
    }

    @Override
    public double[] getStatisticalData(int type) {
        double[] res = new double[this.csf.getNumFolds()];
        switch(type){
            case 1: //ErrorRates
                res = this.csf.eval.getErrorRatesByFolds().clone();
                break;
            case 2:
                res = this.csf.eval.getFMeasureByfolds().clone();
                break;
        }
        return res;
    }

    @Override
    public double getMeasureData(int type) {
        double res = Double.NEGATIVE_INFINITY;
        switch(type){
            case 1: //ErrorRates
                res = this.getErrorRate();
                break;
            case 2:
                res = this.getStatisticRateCollection().eval.weightedFMeasure();
                break;
        }
        return res;
    }

}
