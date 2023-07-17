/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Individuals.Proposal;

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
import Individuals.PEVOMO.Alphabet;
import Individuals.PEVOMO.Word;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
import Utils.Utils;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import mimath.MiMath;
import parameters.Generals;
import parameters.Locals;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.trees.J48;
import java.util.Arrays;

/**
 *
 * @author amarquezgr
 */
public class Scheme implements IScheme, Cloneable, Comparable<IScheme> {
    private List<WordCut> elements;
//    private TreeSet<WordCut> elements;
//    private IEvaluator fitness_function;
    private FitnessFunction fitness_function;
    private boolean isSelfAdaptation;
    
    private double ErrorRate;
//    private double[] ErrorRatesByFolds;
    private String DecisionTreeGraph;
    private List<Prediction> predictions;
    private List<Integer> correctPredictions;
//    public double[][] MatrixConfusion;
    StatisticRatesCollection statistic_rates;
//    private String DiscretizedString;
//    
//    private DiscretizedDataSet ds_dis;
    public Classification csf;

    
    public List<WordCut> getElements() {
        return elements;
    }

    @Override
    public boolean isIsSelfAdaptation() {
        return isSelfAdaptation;
    }
    
    public void setElements(List<WordCut> elements) {
        this.elements = new ArrayList<>();
        for (WordCut item : elements) this.elements.add(item.clone());
        
    }

//    @Override
//    public DiscretizedDataSet getDs_dis() {
//        return ds_dis;
//    }

    @Override
    public WordCut getElementAt(int index){
        List<WordCut> auxiliar = new ArrayList<>(this.elements);
        return auxiliar.get(index);
    }

    @Override
    public double getErrorRate() {
        return ErrorRate;
    }

    @Override
    public Classification getClassificationModel() {
        return this.csf;
    }

    @Override
    public List<Prediction> getPredictions() {
        return predictions;
    }
    
    @Override
    public String getDecisionTreeGraph() {
        return DecisionTreeGraph;
    }

//    @Override
//    public Generals getGeneral_params() {
//        return general_params;
//    }
//
//    @Override
//    public Locals getLocal_params() {
//        return local_params;
//    }
//
//    @Override
//    public void setGeneral_params(Generals general_params) {
//        this.general_params = general_params;
//    }
//
//    @Override
//    public void setLocal_params(Locals local_params) {
//        this.local_params = local_params;
//    }

    public Scheme() {
        elements = new ArrayList<>();
//        this.ErrorRatesByFolds = new double[10];
//        for (int i=0;i<10;i++){
//            this.ErrorRatesByFolds[i] = Double.NaN;
//        }
        csf = new Classification();
    }

    
    public Scheme(Data ds, double[][] limits, int minalphabet, int maxalphabet, boolean isSelfAdaptation, int iFitnessFunctionConf) {
        elements = new ArrayList<>();
        this.isSelfAdaptation = isSelfAdaptation;
        double coef = (Math.floor(Math.random()*5)+1)/10;
        int numcuts = (int) Math.floor(coef*ds.getDimensions()[1]);
        for(int i=0; i<numcuts;i++){
            int cut = MiMath.randomInt(TimeSeriesDiscretize_source.MIN_NUMBER_OF_WORD_CUTS, ds.getDimensions()[1]-1, this.getCuts());
            WordCut wordcut = new WordCut(cut, limits);
            elements.add(wordcut);
            sort();
        }
//        this.general_params = general_params;
//        this.local_params = local_params;
        this.fitness_function = new FitnessFunction(iFitnessFunctionConf);
        csf = new Classification();
    }

//    public Scheme(DataSet ds, int minalphabet, int maxalphabet, FitnessFunction fitness_function, boolean isSelfAdaptation) {
////        elements = new TreeSet<>();
//        elements = new ArrayList<>();
//        double coef = (Math.floor(Math.random()*5)+1)/10;
//        int numcuts = (int) Math.floor(coef*ds.getDimensions()[1]);
//        this.isSelfAdaptation = isSelfAdaptation;
//        for(int i=0; i<numcuts;i++){
//            int cut = MiMath.randomInt(TimeSeriesDiscretize_source.MIN_NUMBER_OF_WORD_CUTS, ds.getDimensions()[1]-1, this.getCuts());
//            WordCut wordcut = new WordCut(cut, ds.getLimits());
//            elements.add(wordcut);
//            sort();
//        }
//        this.fitness_function = fitness_function.clone();
////        this.general_params = general_params;
////        this.local_params = local_params;
//    }
    
    @Override
    public void sort(){
        Collections.sort(this.elements);
    }
    
    public void addCut(WordCut c, int ds_length){
        if(!this.getCuts().contains(c.getCut())){
            if(this.elements.size() < (TimeSeriesDiscretize_source.MAX_NUMBER_OF_WORD_CUTS * ds_length)){
                this.elements.add(c);
                this.sort();
            }
        }
    }
    
    @Override
    public Word getWord(){
        Word word = new Word();
        for(WordCut w: this.elements){
            word.addWordCut(w.getCut());
        }
        return word;
    }
    
    public List<Integer> getCuts(){
        List<Integer> cuts = new ArrayList<>();
        for(WordCut w: this.elements){
            cuts.add(w.getCut());
        }
        return cuts;
    }
    
    public int getMaxAlphabetsize(){
        int max = 0;
        for(WordCut wc: this.elements){
            if (wc.getAlphabet().getAlphabet().size() > max)
                max = wc.getAlphabet().getAlphabet().size();
        }
        return max;
    }
    
    @Override
    public void adjust(Data ds){
        this.elements.get(this.elements.size()-1).setCut(ds.getDimensions()[1]-1);
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
        sb.append("\n").append("Cuts:").append("\n");
        for(WordCut wc:this.elements){
            sb.append(wc.toString()).append(" ");
        }
        
        return sb.toString();
    }

    @Override
    public int getNumberWordCuts() {
        return this.elements.size();
    }

    @Override
    public DiscretizedData DiscretizeByPAA(Data ds) {
        int[] dimensions = ds.getDimensions();
        int instances = dimensions[0];
        
        DiscretizedData ds_dis = new DiscretizedData(instances, this.elements.size()+1);
        
        int[] intervals = new int[2];
        
//        boolean LastIsLength=true;
        
        for(int i=0;i<instances;i++){
            intervals[0] = 1;
            ds_dis.addValue(i,0, (int) ds.getValue(i, 0), true); //agrego la clase
            int na = 1;
            for(int a=0;a<this.elements.size();a++){
                intervals[1] = this.getElementAt(a).getCut();
                double media = MiMath.getMedia(ds.getValuesFrom(i, intervals[0], intervals[1]));
                ds_dis.addValue(i,na,this.getElementAt(a).getAlphabet().getAlphabetByAvg(media), false);
                na++;
                intervals[0] = this.getElementAt(a).getCut();
            }
            intervals[1] = ds.getDimensions()[1]-1;
//            System.out.println("intervals[0]:"+intervals[0]+", intervals[1]:"+intervals[1]);
            if(intervals[0] != intervals[1]){
                double mean = MiMath.getMedia(ds.getValuesFrom(i, intervals[0], intervals[1]));
                ds_dis.addValue(i,this.elements.size()+1,this.getElementAt(this.elements.size()-1).getAlphabet().getAlphabetByAvg(mean), false);
//                LastIsLength = false;
            }
        }
        
//        if(LastIsLength){
//            ds_dis.deleteLastAtt();
//        }
        
//        ds_dis.convert2CharArray();
        ds_dis.convert2StringArray();
//        ds_dis.convert2FloatArray();
        ds_dis.convert2ContinuosIntArray(this.elements);
        return ds_dis;
    }
    
//    public DiscretizedDataSet DiscretizeByGini(DataSet ds) {
//        int[] dimensions = ds.getDimensions();
//        int instances = dimensions[0];
//        
//        DiscretizedDataSet ds_dis = new DiscretizedDataSet(instances, this.elements.size()+1);
//        
//        int[] intervals = new int[2];
////        System.out.println("Giny");
//        for(int i=0;i<instances;i++){
//            intervals[0] = 1;
//            ds_dis.addValue(i,0, (int) ds.getValue(i, 0)); //agrego la clase
//            for(int a=0;a<this.elements.size();a++){
////                System.out.println(this.elements.get(a).toString());
//                intervals[1] = this.getElementAt(a).getCut();
////                double media = MiMath.getMedia(ds.getValuesFrom(i, intervals[0], intervals[1])); //cambiar***********
////                ds_dis.addValue(i,a+1,this.elements.get(a).getAlphabet().getAlphabetByAvg(media));//cambiar***********
////                System.out.println("Palabra:"+(a+1)+", intervals[0]:"+intervals[0]+", intervals[1]:"+intervals[1]);
//                ds_dis.addValue(i,a+1,this.getElementAt(a).getAlphabet().getAlphabetByGini(ds.getValuesFrom(i, intervals[0], intervals[1]), ds.getLimits()));
//                intervals[0] = this.getElementAt(a).getCut();
//            }
//        }
//        ds_dis.convert2CharArray();
//        ds_dis.convert2StringArray();
//        ds_dis.convert2FloatArray();
////        ds_dis.convert2IntArray();
//        return ds_dis;
//    }
    
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

    public double[] evaluate(Data ds, double[] weights, int[] functions) {
        try {
            FitnessFunction ff = new FitnessFunction();
            ff.setNofunctions(functions.length);
            ff.initEvaluatedValues();
            ff.setIdFunctions(functions);
            ff.setWeights(weights);
            ff.Evaluate(ds, this);
            return ff.getEvaluatedValues();
        } catch (Exception ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
            return null;
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
//        return fitness_function.getFitness_value();
        return this.fitness_function.getEvaluatedValue();
    }

    @Override
    public void Generate(Data ds, double[][] limits) {
        double coef = (Math.floor(Math.random()*5)+1)/10;
        int numcuts = (int) Math.floor(coef*ds.getDimensions()[1]);
        for(int i=0; i<numcuts;i++){
            int cut = MiMath.randomInt(TimeSeriesDiscretize_source.MIN_NUMBER_OF_WORD_CUTS, ds.getDimensions()[1]-1, this.getCuts());
            WordCut wordcut = new WordCut(cut, limits);
            elements.add(wordcut);
            sort();
        }
        
    }

    @Override
    public IScheme Mutate(double MutationRate, boolean isSelfAdaptation, Data ds, double[][] limits) {
        Scheme offspring = (Scheme) this.clone();
        TreeSet<Integer> excluir ;
        for(int i=0; i< this.elements.size(); i++){
            excluir = new TreeSet<>(offspring.getCuts());
            
            if(Math.random() <= MutationRate){
                int cut = MiMath.randomInt(TimeSeriesDiscretize_source.MIN_NUMBER_OF_WORD_CUTS, ds.getDimensions()[1]-1, excluir);
                if (cut > 0){
                    offspring.getElementAt(i).setCut(cut);
                }
            } 
            offspring.getElementAt(i).setAlphabet(offspring.getElementAt(i).getAlphabet().Mutate(MutationRate, limits));
            offspring.sort();
        }
        
        if(isSelfAdaptation){
//            Mutate weights
            offspring.setFitnessFunction(this.fitness_function.MutateWeights(MutationRate).clone());
        } else {
            offspring.setFitnessFunction(this.fitness_function.clone());
        }
        
        return offspring;
    }
    
    public boolean ExistDuplicates(){
        Map<Integer,Integer> duplicates = new HashMap<>();
        boolean exists = false;
        for (WordCut c: this.elements){
            if (duplicates.containsKey(c.getCut())){
                int conteo = duplicates.get(c.getCut());
                duplicates.put(c.getCut(), conteo + 1);
            } else {
                duplicates.put(c.getCut(), 1);
            }
        }
        
        for(Integer cont: duplicates.keySet()){
            if(duplicates.get(cont) > 1) exists = true;
        }
        return exists;
    }

//    @Override
//    public void Export2Matlab(String FileName) {
//        MatlabExporter mexp = new MatlabExporter();
//        File FileTabla = new File(FileName);
//        
//        mexp = this.add2Export(mexp);
//        
//        try {
//            mexp.write(FileTabla);
//        } catch (IOException ex) {
//            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    @Override
    public String getName() {
        //return "ProposalEP";
        return "MODiTS";
    }
    
    @Override
    public double[] toDoubleArray(){
        
        List<Double> a = new ArrayList<>();
        
        for(WordCut wc:this.elements){
            a.add((double) wc.getCut());
            for(Double alph: wc.getAlphabet().getAlphabet()){
                a.add(alph);
            }
        }
        
        double[] array = new double[a.size()];
        
        for(int i=0;i<a.size();i++){
            array[i] = a.get(i);
        }
        
        return array;
        
    }
    
    @Override
    public float[][] Elements2FloatArray(){
        float[][] DataCuts = new float[this.elements.size()][this.getMaxAlphabetsize()+1];
        for(int i =0; i < this.elements.size();i++){
            for(int j=0 ; j < this.getMaxAlphabetsize()+1; j++){
                DataCuts[i][j] = Float.NaN;
            }
        }

        for(int i =0; i< this.elements.size();i++){
            DataCuts[i][0] = (float) this.getElementAt(i).getCut();
            for(int j=0;j<this.getElementAt(i).getAlphabet().getAlphabet().size(); j++){
                DataCuts[i][j+1] = this.getElementAt(i).getAlphabet().getElementAt(j).floatValue();
            }
        }
        return DataCuts;
    }

    @Override
    public void Export(Generals general_params, Locals local_params) {
        try {
            String FileName = local_params.getDs().getName()+"_"+general_params.population.getBest().getName()+"_e"+(local_params.getExecution()+1);
            String directory = general_params.population.getBest().getName()+"/"+local_params.getDs().getName();
            
            float[][] DataCuts = Elements2FloatArray();
            general_params.exporter.add("cuts", DataCuts);
            
            
//            float[][] DataFunctionValues = new float[1][this.fitness_function.getNoFunctions()];
//            for(int i=0;i<this.fitness_function.getNoFunctions();i++){
//                DataFunctionValues[0][i] = (float) this.fitness_function.getEvaluatedValues()[i];
//            }
//            general_params.exporter.add("fitness_values", DataFunctionValues);
            
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

            /* Adicional para el análisis de frentes de pareto para infoloss*/
            int[] functions = {0, 1, 8};
            double[] values = this.evaluate(local_params.getDs().getTrain(), general_params.getWeights(), functions);
            float[][] fitness_values = new float[1][3];
            for(int i=0;i<values.length; i++){
                fitness_values[0][i] = (float) values[i];
            }
            general_params.exporter.add("fitness_paretos", fitness_values);
            /* -------- */
            
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
            
            MLArray w = mlArrayRetrived.get("cuts");
            double[][] arr = ((MLDouble) w).getArray();
//            StringBuilder sb = new StringBuilder();
            for(double[] fila: arr){
                WordCut wc = new WordCut();
                List<Double> alph = new ArrayList<>();
                wc.setCut((int) fila[0]);
                for(int i=1;i<fila.length;i++){
                    if(!Double.isNaN(fila[i])){
                        alph.add(fila[i]);
                    }
                }
                Alphabet a = new Alphabet();
                a.setAlphabet(alph);
                wc.setAlphabet(a);
                this.elements.add(wc);
            }
            MLArray er = mlArrayRetrived.get("ErrorRate");
            double[][] ER = ((MLDouble) er).getArray();
            this.ErrorRate = ER[0][0];
            
            MLArray fit = mlArrayRetrived.get("fitness_values");
            double[][] arrfit = ((MLDouble) fit).getArray();
            this.fitness_function = new FitnessFunction();
            this.fitness_function.setEvaluatedValues(arrfit[0]);
//            System.out.println(sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public String PrintCuts(){
        StringBuilder sb = new StringBuilder();
        for(WordCut w:this.elements){
            sb.append(w.getCut()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        
        return sb.toString();
    }

    @Override
    public IScheme clone(){
        try {
            super.clone();
            Scheme clon = new Scheme();
            clon.setElements(this.getElements());
            clon.setFitnessFunction(this.getFitnessFunction());
            clon.isSelfAdaptation = this.isSelfAdaptation;
            clon.DecisionTreeGraph = this.DecisionTreeGraph;
            clon.ErrorRate = this.ErrorRate;
//            clon.ds_dis = this.ds_dis.clone();
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }

    @Override
    public int compareTo(IScheme o) {
        Scheme ind = (Scheme) o;
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
    
    public int find(int cut){
        int res = -1;
        for(int i=0; i<this.elements.size();i++){
            if (cut==this.elements.get(i).getCut()){
                res = i;
                break;
            }
        }
        return res;
    }
    
    @Override
    public void add(double[][] front){
        for(double[] o: front){
            WordCut wc = new WordCut();
            List<Double> alph = new ArrayList<>();
            wc.setCut((int) o[0]);
            for(int k=1;k<o.length;k++){
                if(!Double.isNaN(o[k])){
                    alph.add(o[k]);
                }
            }
            Alphabet a = new Alphabet();
            a.setAlphabet(alph);
            wc.setAlphabet(a);

            if(!this.getCuts().contains(wc.getCut())){
                this.elements.add(wc);
            } else {
                int ind = this.find(wc.getCut());
                this.elements.get(ind).getAlphabet().CreateOffspring(wc.getAlphabet().getAlphabet());
            }
        }
    }
    
    @Override
    public void add(Object o){
        WordCut c = (WordCut) o;
        if(!this.getCuts().contains(c.getCut())){
            this.elements.add(c);
        } else {
            int ind = this.find(c.getCut());
            this.elements.get(ind).getAlphabet().CreateOffspring(c.getAlphabet().getAlphabet());
        }
    }
    
    @Override
    public List<Integer> getTotalCuts(){
        List<Integer> list = new ArrayList<>();
        list.add(this.elements.size());
        for(WordCut wc:this.elements){
            list.add(wc.getAlphabet().getAlphabet().size());
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
    public void empty() {
        elements = new ArrayList<>();
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
//                DiscretizedData ds_dis_train = this.DiscretizeByPAA(dataset.getTrain());
//                DiscretizedData ds_dis_test= this.DiscretizeByPAA(dataset.getTest());
//                csf = new Classification(ds_dis_train, ds_dis_test);
//                csf.ClassifyWithTraining(j48);
//                this.ErrorRate = csf.getErrorRate();
//                this.predictions = csf.getPredictions();
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
//                this.predictions = csf.getPredictions();
                ds_dis_train.destroy();
                ds_dis_test.destroy();
            } else{
                DiscretizedData data = new DiscretizedData();
                
                switch (set_type){
                    case "original":
                        DiscretizedData ds_dis= this.DiscretizeByPAA(dataset.getOriginal());
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
                        data=ds_dis_test.clone();
                        ds_dis_test.destroy();
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

        String directory = getName()+"/"+local_params.getDs().getName()+"/Trees";
        String FileName = "Arbol_e"+(local_params.getExecution()+1);

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        //GraphViz gv = new GraphViz();
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".txt" )  ){
               out.println( this.DecisionTreeGraph );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public void ExportGraph(DataSet ds, String folder,String Location, String Selector) {
        String directory = folder+"/"+Location+"/"+ds.getName()+"/Trees";
        String FileName = "Arbol_best";

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".txt" )  ){
               out.println( this.DecisionTreeGraph );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void Export(DataSet dataset, MatlabExporter exporter) {
        
            float[][] DataCuts = new float[this.elements.size()][this.getMaxAlphabetsize()+1];
            for(int i =0; i < this.elements.size();i++){
                for(int j=0 ; j < this.getMaxAlphabetsize()+1; j++){
                    DataCuts[i][j] = Float.NaN;
                }
            }
            
            for(int i =0; i< this.elements.size();i++){
                DataCuts[i][0] = (float) this.getElementAt(i).getCut();
                for(int j=0;j<this.getElementAt(i).getAlphabet().getAlphabet().size(); j++){
                    DataCuts[i][j+1] = this.getElementAt(i).getAlphabet().getElementAt(j).floatValue();
                }
            }
            exporter.add("cuts", DataCuts);
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
            
            /* Adicional para el análisis de frentes de pareto para infoloss*/
            int[] functions = {0, 1, 8};
            double[] values = this.evaluate(dataset.getTrain(), this.fitness_function.getWeights(), functions);
            float[][] fitness_values = new float[1][3];
            for(int i=0;i<values.length; i++){
                fitness_values[0][i] = (float) values[i];
            }
            exporter.add("fitness_paretos", fitness_values);
            /* -------- */
    }

    @Override
    public int getNumberAlphabetCuts() {
        throw new UnsupportedOperationException("Does not apply."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ExportStrings(DataSet ds, String Location) {
        String FileName = "DiscretizedString";
        String directory = getName()+"/"+Location+ds.getName()+"/Strings";
        

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".txt" )  ){
            DiscretizedData ds_dis = this.DiscretizeByPAA(ds.getOriginal());
               out.println( ds_dis.PrintStrings());
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Alphabet getAlphabet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
//    public int getNumberAlphabetCuts(int word_cut) {
//        return this.elements.get(word_cut).getAlphabet().getAlphabet().size();
//    }

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
        
        for(WordCut wc: this.elements){
            CutIntervals[1] = wc.getCut();
            intervals.add(CutIntervals[1]-CutIntervals[0]);
            CutIntervals[0] = wc.getCut();
        }
        
        return intervals;
    }

    @Override
    public ReconstructedData Reconstruct(DiscretizedData ds_dis) {
        int attributes = this.getElementAt(this.elements.size()-1).getCut()+1;
        int instances = ds_dis.getDimensions()[0];
        ReconstructedData reconstructed = new ReconstructedData(instances,attributes);
        
        List<Integer> diffs = Cuts2Intervals();
         List<Integer> maximums = this.getTotalCuts();
        
        //System.out.println("ds_dis.dimensions: ["+ds_dis.getDimensions()[0]+","+ds_dis.getDimensions()[1]+"]");
        //System.out.println("diffs:"+diffs.toString());
        //System.out.println("maximums:"+maximums.toString());
        
        for(int f=0; f<ds_dis.getIds_discretized().length;f++){
            double[] row = new double[1];
            row[0] = ds_dis.getIds_discretized()[f][0];
            for(int c=1; c<ds_dis.getIds_discretized()[f].length;c++){
                // double value = (double) ds_dis.getIds_discretized()[f][c] / maximums.get(c);
                double value = (double) ds_dis.getIds_discretized()[f][c];
                double[] normvalues = new double[diffs.get(c-1)]; 
                java.util.Arrays.fill(normvalues, value);
                double[] aux = new double[row.length + normvalues.length];
                
                System.arraycopy(row, 0, aux, 0, row.length);
                System.arraycopy(normvalues, 0, aux, row.length, normvalues.length);
                
                row = aux.clone();
                // System.out.println(aux.toString());
            }
            //reconstructed.addRow(f, row);
            double[] norm = Utils.Normalize(Arrays.copyOfRange(row, 1, row.length-1));
            reconstructed.getData()[f][0] = row[0];
            System.arraycopy(norm, 0, reconstructed.getData()[f], 1, norm.length);
        }
        
        return reconstructed;
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
        String FileName = "ErrorRates_"+type_selection;
        
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
    public static void main(String[] args) throws MyException, FileNotFoundException, IOException{
        TimeSeriesDiscretize_source.symbols = Utils.getListSymbols();
        DataSet ds = new DataSet(93, false); 
        MatFileReader mfr = new MatFileReader("/Users/scoramg/Dropbox/Doctorado IA/Tesis/Programa JAVA/Tesis/e15p100g300_C/ProposalEP/ColposcopiaHML/ColposcopiaHML_ProposalEP_e1.mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        Scheme esquema = new Scheme();
        String filename = "cuts";
        MLArray w = mlArrayRetrived.get(filename);
        //MLArray w = mlArrayRetrived.get("IndividualFront3");
        //MLArray w = mlArrayRetrived.get("cuts");
        double[][] arr = ((MLDouble) w).getArray();
        for(double[] fila: arr){
            WordCut wc = new WordCut();
            List<Double> alph = new ArrayList<>();
            wc.setCut((int) fila[0]);
            for(int i=1;i<fila.length;i++){
                if(!Double.isNaN(fila[i])){
                    alph.add(fila[i]);
                }
            }
            Alphabet a = new Alphabet();
            a.setAlphabet(alph);
            wc.setAlphabet(a);
            esquema.elements.add(wc);
        }
        int[] functions = {0,1,8};
        FitnessFunction ff = new FitnessFunction(12);
        double[] values = esquema.evaluate(ds.getTrain(), new double[3], functions);
        System.out.println("2");
    }
}


