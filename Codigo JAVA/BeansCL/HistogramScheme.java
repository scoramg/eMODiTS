/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL;

import DataMining.Classification.Classification;
import DataTypes.ArraySortedUniqueList;
import Exceptions.MyException;
import FitnessFunctions.FitnessFunction;
import Interfaces.SortedUniqueList;
import com.github.jabbalaci.graphviz.GraphViz;
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
import BeansCL.parameters.*;
import ca.nengo.io.MatlabExporter;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.trees.J48;

/**
 *
 * @author amarquezgr
 */
public class HistogramScheme implements Cloneable, Comparable<HistogramScheme> {
    private List<HistogramWidthCut> elements;
    private FitnessFunction fitness_function;
    private double ErrorRate;
//    private double[] ErrorRatesByFolds;
    private String DecisionTreeGraph;
    private List<Prediction> predictions;
    private List<Integer> correctPredictions;
    
    private int rank;
    private List<HistogramScheme> DominationSet;
    private int DominatedCount;
    private double CrowdingDistance;
    
    private Classification csf;

    public FitnessFunction getFitness_function() {
        return fitness_function;
    }

    public List<HistogramScheme> getDominationSet() {
        return DominationSet;
    }

    public int getDominatedCount() {
        return DominatedCount;
    }

    public double getCrowdingDistance() {
        return CrowdingDistance;
    }

    public Classification getClassificationModel() {
        return csf;
    }

    public void setFitness_function(FitnessFunction fitness_function) {
        this.fitness_function = fitness_function;
    }

    public void setErrorRate(double ErrorRate) {
        this.ErrorRate = ErrorRate;
    }

//    public void setErrorRatesByFolds(double[] ErrorRatesByFolds) {
//        this.ErrorRatesByFolds = ErrorRatesByFolds;
//    }

    public void setDecisionTreeGraph(String DecisionTreeGraph) {
        this.DecisionTreeGraph = DecisionTreeGraph;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public void setCorrectPredictions(List<Integer> correctPredictions) {
        this.correctPredictions = correctPredictions;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setDominationSet(List<HistogramScheme> DominationSet) {
        this.DominationSet = DominationSet;
    }

    public void setDominatedCount(int DominatedCount) {
        this.DominatedCount = DominatedCount;
    }

    public void setCrowdingDistance(double CrowdingDistance) {
        this.CrowdingDistance = CrowdingDistance;
    }
    
    public void setElements(List<HistogramWidthCut> elements) {
        this.elements = new ArrayList<>();
        for (HistogramWidthCut item : elements) this.elements.add(item.clone());
        
    }
    
    public List<HistogramWidthCut> getElements() {
        return elements;
    }
    
    public HistogramWidthCut getElementAt(int index) {
        List<HistogramWidthCut> auxiliar = new ArrayList<>(this.elements);
        return auxiliar.get(index);
    }
    
    public double getErrorRate() {
        return ErrorRate;
    }
    
//    public double[] getErrorRatesByFolds() {
//        return ErrorRatesByFolds;
//    }
    
    public List<Prediction> getPredictions() {
        return predictions;
    }
    
    public String getDecisionTreeGraph() {
        return DecisionTreeGraph;
    }

    public void sort() {
        Collections.sort(this.elements);
    }
    
    public void IncreaseDominatedCount(){
        this.DominatedCount++;
    }
    
    public void DecreaseDominatedCount(){
        this.DominatedCount--;
    }
    
    public int getMaxAlphabetsize(){
        int max = 0;
        for(HistogramWidthCut wc: this.elements){
            if (wc.getHeight_cuts().getCuts().size() > max)
                max = wc.getHeight_cuts().getCuts().size();
        }
        return max;
    }
  
    public int getNumberWordCuts() {
        return this.elements.size();
    }
    
    public void evaluate(HistogramCollection ds, double[] weights) {
        try {
            this.fitness_function.setWeights(weights);
            this.fitness_function.Evaluate(ds, this);
        } catch (Exception ex) {
            Logger.getLogger(HistogramScheme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public FitnessFunction getFitnessFunction() {
        return fitness_function;
    }
    
    public void setFitnessFunction(FitnessFunction fitnes_function) {
        this.fitness_function = fitnes_function.clone();
    }
    
    public double getFitnessValue() {
        return this.fitness_function.getEvaluatedValue();
    }
    
    public List<Integer> getCuts(){
        List<Integer> cuts = new ArrayList<>();
        for(HistogramWidthCut w: this.elements){
            cuts.add(w.getCut());
        }
        return cuts;
    }
    
    public DiscreteHistogramCollection Discretize(HistogramCollection data){
        DiscreteHistogramCollection dhc = new DiscreteHistogramCollection();
        int[] intervals = new int[2];
        int[] heightinterval = new int[2];
        for (int i=0; i<data.getInstances(); i++){
            intervals[0] = 0;
            DiscreteHistogram dh = new DiscreteHistogram();
            for (HistogramWidthCut wc: this.elements){
                intervals[1] = wc.getCut();
                heightinterval[0] = 0;
                DiscreteHistogramRow dhr = new DiscreteHistogramRow();
                for(Integer val: wc.getHeight_cuts().getCuts()){
                    heightinterval[1] = val;
                    double media = MiMath.getMedia(data.getValuesFrom(i, intervals[0], intervals[1], heightinterval[0], heightinterval[1]));
                    dhr.addValue(media);
                    heightinterval[0] = val;
                }
                dh.addRow(dhr);
                intervals[0] = wc.getCut();
            }
            dh.setKlass(data.getData().get(i).getKlass());
            dhc.addDiscreteHistogram(dh);
            dhc.addClass(data.getData().get(i).getKlass());
        }
        return dhc;
    }
    
//    Revisar
    public void Generate(Histogram hist, int limit) {
        double coef = (Math.floor(Math.random()*5)+1)/10;
        int numcuts = (int) Math.floor(coef*hist.getDimensions()[1]);
        for(int i=0; i<numcuts;i++){
            int cut = MiMath.randomInt(2, hist.getDimensions()[1]-1, this.getCuts());
            HistogramWidthCut wordcut = new HistogramWidthCut(cut, limit);
            elements.add(wordcut);
            sort();
        }
    }
    
    //Revisar
    public HistogramScheme Mutate(double MutationRate, boolean isSelfAdaptation, int[] dimensions) {
        HistogramScheme offspring = (HistogramScheme) this.clone();
        TreeSet<Integer> excluir ;
        for(int i=0; i< this.elements.size(); i++){
            excluir = new TreeSet<>(offspring.getCuts());
            
            if(Math.random() <= MutationRate){
                int cut = MiMath.randomInt(2, dimensions[1]-1, excluir);
                if (cut > 0){
                    offspring.getElementAt(i).setCut(cut);
                }
            } 
            offspring.getElementAt(i).setHeight_cuts(offspring.getElementAt(i).getHeight_cuts().Mutate(MutationRate, dimensions[1]));
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
        for (HistogramWidthCut c: this.elements){
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
    
    public double[] toIntegerArray() {
        List<Integer> a = new ArrayList<>();
        
        for(HistogramWidthCut wc:this.elements){
            a.add(wc.getCut());
            for(Integer alph: wc.getHeight_cuts().getCuts()){
                a.add(alph);
            }
        }
        
        double[] array = new double[a.size()];
        
        for(int i=0;i<a.size();i++){
            array[i] = a.get(i);
        }
        
        return array;
    }
    
    public float[][] Elements2FloatArray() {
        float[][] DataCuts = new float[this.elements.size()][this.getMaxAlphabetsize()+1];
        for(int i =0; i < this.elements.size();i++){
            for(int j=0 ; j < this.getMaxAlphabetsize()+1; j++){
                DataCuts[i][j] = Float.NaN;
            }
        }

        for(int i =0; i< this.elements.size();i++){
            DataCuts[i][0] = (float) this.getElementAt(i).getCut();
            for(int j=0;j<this.getElementAt(i).getHeight_cuts().getCuts().size(); j++){
                DataCuts[i][j+1] = this.getElementAt(i).getHeight_cuts().getElementAt(j);
            }
        }
        return DataCuts;
    }
    
//    //Revisar
    public void Export(Generals general_params, Locals local_params) {
        try {
            String FileName = local_params.getDs().getName()+"_"+general_params.population.getBest().getName()+"_e"+(local_params.getExecution()+1);
            String directory = general_params.population.getBest().getName()+"/"+local_params.getDs().getName();
            
            float[][] DataCuts = Elements2FloatArray();
            general_params.exporter.add("cuts", DataCuts);
            
            float[][] er = new float[1][1];
            er[0][0] = (float) getErrorRate();
            general_params.exporter.add("ErrorRate", er);
            
            DiscreteHistogramCollection ds_dis = this.Discretize(local_params.getDs().getHistComplete());
            DiscreteHistogramCollection ds_dis_train = this.Discretize(local_params.getDs().getHistTrain());
            DiscreteHistogramCollection ds_dis_test = this.Discretize(local_params.getDs().getHistTest());
//            DiscreteHistogramCollection ds_dis = local_params.getDs().getHistComplete().Discretize(this);
//            DiscreteHistogramCollection ds_dis_train = local_params.getDs().getHistTrain().Discretize(this);
//            DiscreteHistogramCollection ds_dis_test = local_params.getDs().getHistTest().Discretize(this);
            
            general_params.exporter.add("ds_dis", ds_dis.toFloatArray());
            general_params.exporter.add("ds_dis_train", ds_dis_train.toFloatArray());
            general_params.exporter.add("ds_dis_test", ds_dis_test.toFloatArray());
            
            File FileDir = new File(directory);
            if(!FileDir.exists()) FileDir.mkdirs();
            
            File FileTabla = new File(directory+"/"+FileName+".mat");
            
            float[][] DataTime = new float[1][2];
            DataTime[0][0] = (float) local_params.getTimeAlgorithm();
            DataTime[0][1] = (float) general_params.getTotalTime();
            general_params.exporter.add("Time", DataTime);
            
            general_params.exporter.add("ConvergenceData", local_params.ConvergenceData);
            
            general_params.exporter.add("MeasuresData", local_params.MeasuresData);
            
            general_params.exporter.write(FileTabla);
        } catch (IOException ex) {
            Logger.getLogger(HistogramScheme.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public String PrintCuts() {
        StringBuilder sb = new StringBuilder();
        for(HistogramWidthCut w:this.elements){
            sb.append(w.getCut()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        
        return sb.toString();
    }
    
    public void ImportFromMatFile(String filename, int ds_length) {
        try {
            MatFileReader mfr = new MatFileReader(filename);
            Map<String, MLArray> mlArrayRetrived = mfr.getContent();
            
            MLArray w = mlArrayRetrived.get("cuts");
            double[][] arr = ((MLDouble) w).getArray();
//            StringBuilder sb = new StringBuilder();
            for(double[] fila: arr){
                HistogramWidthCut wc = new HistogramWidthCut();
                SortedUniqueList<Integer> alph = new ArraySortedUniqueList<>();
                wc.setCut((int) fila[0]);
                for(int i=1;i<fila.length;i++){
                    if(!Double.isNaN(fila[i])){
                        alph.add((int) fila[i]);
                    }
                }
                HistogramHeightCuts a = new HistogramHeightCuts();
                a.setCuts(alph);
                wc.setHeight_cuts(a);
                this.elements.add(wc);
            }
            MLArray er = mlArrayRetrived.get("ErrorRate");
            double[][] ER = ((MLDouble) er).getArray();
            this.ErrorRate = ER[0][0];
            
            MLArray fit = mlArrayRetrived.get("fitness_values");
            double[][] arrfit = ((MLDouble) fit).getArray();
            this.fitness_function = new FitnessFunction();
            this.fitness_function.setEvaluatedValues(arrfit[0]);
            
        } catch (IOException ex) {
            Logger.getLogger(HistogramScheme.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public String getName() {
        return "Histogram";
    }

    public HistogramScheme clone() {
        try {
            super.clone();
            HistogramScheme clon = new HistogramScheme();
            clon.setElements(this.getElements());
            clon.setFitnessFunction(this.getFitnessFunction());
            clon.DecisionTreeGraph = this.DecisionTreeGraph;
            clon.ErrorRate = this.ErrorRate;
//            clon.ds_dis = this.ds_dis.clone();
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(HistogramScheme.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public int compareTo(HistogramScheme o) {
        HistogramScheme ind = (HistogramScheme) o;
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
    
    public void add(double[][] front) {
        for(double[] o: front){
            HistogramWidthCut wc = new HistogramWidthCut();
            SortedUniqueList<Integer> alph = new ArraySortedUniqueList<>();
            wc.setCut((int) o[0]);
            for(int k=1;k<o.length;k++){
                if(!Double.isNaN(o[k])){
                    alph.add((int) o[k]);
                }
            }
            HistogramHeightCuts a = new HistogramHeightCuts();
            a.setCuts(alph);
            wc.setHeight_cuts(a);

            if(!this.getCuts().contains(wc.getCut())){
                this.elements.add(wc);
            } else {
                int ind = this.find(wc.getCut());
                this.elements.get(ind).getHeight_cuts().CreateOffspring(wc.getHeight_cuts().getCuts());
            }
        }
    }

    public void add(Object o) {
        HistogramWidthCut c = (HistogramWidthCut) o;
        if(!this.getCuts().contains(c.getCut())){
            this.elements.add(c);
        } else {
            int ind = this.find(c.getCut());
            this.elements.get(ind).getHeight_cuts().CreateOffspring(c.getHeight_cuts().getCuts());
        }
    }
    
    public List<Integer> getTotalCuts() {
        List<Integer> list = new ArrayList<>();
        list.add(this.elements.size());
        for(HistogramWidthCut wc:this.elements){
            list.add(wc.getHeight_cuts().getCuts().size());
        }
        return list;
    }
    
    public double[] getEvaluatedValues() {
        return this.fitness_function.getEvaluatedValues();
    }
       
////    Revisar
    public void Classify(HistogramDataSet dataset, boolean UsingTest, String set_type) {
        try {
            
            J48 j48 = new J48();
            
            csf = new Classification();
            if(UsingTest){
                DiscreteHistogramCollection ds_dis_train = this.Discretize(dataset.getHistTrain());
                DiscreteHistogramCollection ds_dis_test = this.Discretize(dataset.getHistTest());
//                DiscreteHistogramCollection ds_dis_train = dataset.getHistTrain().Discretize(this);
//                DiscreteHistogramCollection ds_dis_test = dataset.getHistTest().Discretize(this);
                csf = new Classification(ds_dis_train.toDiscreteDataSet(), ds_dis_test.toDiscreteDataSet());
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
                DiscreteHistogramCollection data = new DiscreteHistogramCollection();
                
                switch (set_type){
                    case "original":
                        DiscreteHistogramCollection ds_dis= this.Discretize(dataset.getHistComplete());
//                        DiscreteHistogramCollection ds_dis= dataset.getHistComplete().Discretize(this);
                        data = ds_dis.clone();
                        ds_dis.destroy();
                        break;
                    case "train":
                        DiscreteHistogramCollection ds_dis_train= this.Discretize(dataset.getHistTrain());
//                        DiscreteHistogramCollection ds_dis_train = dataset.getHistTrain().Discretize(this);
                        data=ds_dis_train.clone();
                        ds_dis_train.destroy();
                        break;
                    case "test":
                        DiscreteHistogramCollection ds_dis_test= this.Discretize(dataset.getHistTest());
//                        DiscreteHistogramCollection ds_dis_test = dataset.getHistTest().Discretize(this);
                        data=ds_dis_test.clone();
                        ds_dis_test.destroy();
                        break;
                } 
                csf = new Classification(data.toDiscreteDataSet());
                csf.ClassifyByCrossValidation(j48);
//                this.ErrorRatesByFolds = errors.clone();
                this.ErrorRate = mimath.MiMath.getMedia(csf.eval.getErrorRatesByFolds());
//                this.predictions = csf.getPredictions();
                data.destroy();
            }
            this.predictions = csf.getPredictions();
            this.getCorrectPredictions(csf.getPredictions());
            
            this.DecisionTreeGraph = j48.graph();
//            this.DiscretizedString = ds_dis.getOriginal().PrintStrings();
            
        } catch (MyException ex) {
            Logger.getLogger(HistogramScheme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HistogramScheme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void empty() {
        elements = new ArrayList<>();
    }
    
    public int getNoFunctions() {
        return this.fitness_function.getNoFunctions();
    }
    
    public void ExportGraph(Locals local_params) {
        String directory = getName()+"/"+local_params.getDs().getName()+"/Trees";
        String FileName = "Arbol_e"+(local_params.getExecution()+1);

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        GraphViz gv = new GraphViz();
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".txt" )  ){
               out.println( this.DecisionTreeGraph );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HistogramScheme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ExportGraph(HistogramDataSet ds, String folder, String Location, String Selector) {
        String directory = folder+"/"+Location+"/"+ds.getName()+"/Trees";
        String FileName = "Arbol_best";

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".txt" )  ){
               out.println( this.DecisionTreeGraph );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HistogramScheme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    Revisar
    public void Export(HistogramDataSet dataset, MatlabExporter exporter) {
        float[][] DataCuts = new float[this.elements.size()][this.getMaxAlphabetsize()+1];
            for(int i =0; i < this.elements.size();i++){
                for(int j=0 ; j < this.getMaxAlphabetsize()+1; j++){
                    DataCuts[i][j] = Float.NaN;
                }
            }
            
            for(int i =0; i< this.elements.size();i++){
                DataCuts[i][0] = (float) this.getElementAt(i).getCut();
                for(int j=0;j<this.getElementAt(i).getHeight_cuts().getCuts().size(); j++){
                    DataCuts[i][j+1] = this.getElementAt(i).getHeight_cuts().getElementAt(j);
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
            
            DiscreteHistogramCollection ds_dis = this.Discretize(dataset.getHistComplete());
            DiscreteHistogramCollection ds_dis_train = this.Discretize(dataset.getHistTrain());
            DiscreteHistogramCollection ds_dis_test = this.Discretize(dataset.getHistTest());

//            DiscreteHistogramCollection ds_dis = dataset.getHistComplete().Discretize(this);
//            DiscreteHistogramCollection ds_dis_train = dataset.getHistTrain().Discretize(this);
//            DiscreteHistogramCollection ds_dis_test = dataset.getHistTest().Discretize(this);
            
            exporter.add("ds_dis", ds_dis.toDiscreteDataSet().getFds_discretized());
            exporter.add("ds_dis_train", ds_dis_train.toDiscreteDataSet().getFds_discretized());
            exporter.add("ds_dis_test", ds_dis_test.toDiscreteDataSet().getFds_discretized());
    }
    
    public List<Integer> Cuts2Intervals(){
        List<Integer> intervals = new ArrayList<>();
        Integer[] CutIntervals = new Integer[2];
        CutIntervals[0] = 0;
        
        for(HistogramWidthCut wc: this.elements){
            CutIntervals[1] = wc.getCut();
            intervals.add(CutIntervals[1]-CutIntervals[0]);
            CutIntervals[0] = wc.getCut();
        }
        
        return intervals;
    }
        
    public String Predictions2CSV() {
        StringBuilder sb = new StringBuilder();
        for(Prediction p : predictions){
            sb.append(p.actual()).append(",").append(p.predicted()).append("\n");
        }
        return sb.toString();
    }
    
//    Revisar
    public void ExportErrorRates(HistogramDataSet ds, String folder, String Location, String type_selection) {
        String FileName = "ErrorRates_"+type_selection;
        
        String directory = Location+'/'+folder+'/'+ds.getName();
        
        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".csv" )  ){
            for(double d: this.csf.eval.getErrorRatesByFolds()){
               out.println(d);
            }
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(HistogramScheme.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public List<Integer> getCorrectPredictions() {
        return this.correctPredictions;
    }
    
    public void getCorrectPredictions(List<Prediction> predictions) {
        this.correctPredictions = new ArrayList<>();
        for(int i=0; i<predictions.size(); i++){
            int pred = predictions.get(i).predicted() == predictions.get(i).actual() ? 1 : 0;
            this.correctPredictions.add(pred);
        }
    }
    
    public HistogramScheme() {
        elements = new ArrayList<>();
        this.fitness_function = new FitnessFunction();
//        this.ErrorRatesByFolds = new double[10];
//        for (int i=0;i<10;i++){
//            this.ErrorRatesByFolds[i] = Double.NaN;
//        }
        csf = new Classification();
    }
    
    public HistogramScheme(int[] dimensions, int iFitnessFunctionConf) {
        elements = new ArrayList<>();
        double coef = (Math.floor(Math.random()*5)+1)/10;
        int numcuts = (int) Math.floor(coef*dimensions[1]);
        for(int i=0; i<numcuts;i++){
            int cut = MiMath.randomInt(2, dimensions[1]-1, this.getCuts());
            HistogramWidthCut wordcut = new HistogramWidthCut(cut, dimensions[0]);
            elements.add(wordcut);
            sort();
        }
        this.fitness_function = new FitnessFunction(iFitnessFunctionConf);
        csf = new Classification();
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
        sb.append("\n").append("Width Cut numbers:").append(this.elements.size()).append("\n");
        sb.append("\n").append("Cuts:").append("\n");
        for(HistogramWidthCut wc:this.elements){
            sb.append(wc.toString()).append(" ");
        }
        
        return sb.toString();
    }
    
    public int getRank() {
        return rank;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        HistogramDataSet hds = new HistogramDataSet(90);
        
        HistogramScheme hs = new HistogramScheme(hds.getHistTrain().getData().get(0).getDimensions(), 0);
        
//        DiscreteHistogramCollection dhc = hds.getHistTrain().Discretize(hs);
//        DiscreteHistogram dh = hds.getHistTrain().getData().get(0).discretize(hs);
        DiscreteHistogramCollection dh = hs.Discretize(hds.getHistTrain());
        
        System.out.println(hs.toString());
//        System.out.println(hds.getHistTrain().getData().get(0).toString());
        System.out.println(dh.getData().get(0).toString());
////        System.out.println("--------Original Data--------");
////        System.out.println(hds.getHistTrain().getData().get(0).toString());
////        System.out.println("--------Discrete Data--------");
////        System.out.println(dhc.getData().get(0).toString());
////        System.out.println("--------One Dimension Discrete Data--------");
////        System.out.println(dhc.getData().get(0).toOneDimensionalArray().toString());
////        System.out.println(dhc.getData().get(1).toOneDimensionalArray().size());
////        System.out.println("--------One Dimension Discrete Data Set--------");
//        
////        DiscretizedData dd = dhc.toDiscreteDataSet();
//        List<OneDimensionHistogram> dd = dhc.toOneDimensionalDataSet();
////        FileOutputStream outputStream = new FileOutputStream("/Users/amarquezgr/Desktop/prueba2.csv");
////        byte[] strToBytes = dd.PrintFloats2csv().getBytes();
////        outputStream.write(strToBytes);
////        outputStream.close();
//
//        for(OneDimensionHistogram od: dd){
////            System.out.println(od.getData().size());
//            System.out.println(od.toString());
//        }
//        
////        Functions.KnnAccuracy(dd);
//        
////        Classification cls = new Classification(dd);
//////        DTW_kNN knn = new DTW_kNN(1);
////        J48 knn = new J48();
//////        IBk knn = new IBk(20);
////        cls.ClassifyByCrossValidation(knn);
//        
//        System.out.println(Functions.KnnAccuracy(dd));
        
    }
    
}
