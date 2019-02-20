/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FitnessFunctions;

import DataMining.Classification.Classification;
import DataSets.Data;
import DataSets.DiscretizedData;
import DataSets.DiscretizedDataSet;
import DataSets.ReconstructedData;
import Interfaces.IScheme;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;
import mimath.MiMath;
import weka.classifiers.Classifier;

/**
 *
 * @author amarquezgr
 */
public class Functions {
    
    public static double EPComplexity(ConfusionMatrix MC, Data ds){
        double complexity = 0;
        if((MC.getDimensions()[0] - ds.getNoClasses()) < 0){
            complexity = ds.getDimensions()[0] - (MC.getDimensions()[0] - ds.getNoClasses());
        } else {
            complexity = MC.getDimensions()[0] - ds.getNoClasses();
        }
        double res = (double) complexity / (ds.getDimensions()[0] + ds.getNoClasses() - 1);
        return res;
    }
    
    //Fata corregir
    public static double PermutationEntropyComplexity(ConfusionMatrix MC, Data ds){
        double complexity = 0.0;
        for (int c=0;c<ds.getNoClasses();c++){
            int klass = ds.getClasses().get(c);
            int countMC = MC.getNoInstancesPerClass().get(klass);
            int countDS = ds.getNoInstancesPerClass().get(klass);
            double p = (double) countMC/countDS;
            complexity += p * Math.log(p);
        }
        
        return complexity * (-1);
    }
    
    public static double EPCompression(int discretizedsize, int originalsize){
        double res = (double) (discretizedsize)/(2*(originalsize));  
//        return res * (-1);
        return res;
    }
    
    public static double EPAccuracy(ConfusionMatrix MC){
        double[][] probabilities = MC.getProbabilities();
        double[][] entropy = MC.getEntropyMatrix(probabilities);
        double sum = MC.getSumEntropy(entropy);
        List<Integer> rowsZeros = MC.getRowsEmpty(entropy);
        ConfusionMatrix MC_aux = MC.clone();
        MC_aux.removeIndexs(rowsZeros);
        MC_aux.setSum();
        
        double accuracy = 0;
        if(MC_aux.getDimensions()[0]>0){
            double pr = (sum * MC_aux.getSum())/(MC_aux.getDimensions()[0]);
            accuracy = 1 - MiMath.inverseNumber(pr+1);
        }
        return accuracy;
    }
    
    public static double getMissclassificationRate(DiscretizedData ds_dis, Classifier classifier){
        Classification cls = new Classification(ds_dis);
        cls.ClassifyByCrossValidation(classifier);
        return cls.getErrorRate();
    }
    
    public static double getMissclassificationRate(DiscretizedDataSet ds_dis, Classifier classifier) throws Exception{
        Classification cls = new Classification(ds_dis.getTrain(), ds_dis.getTest());
        cls.ClassifyWithTraining(classifier);
        return cls.getErrorRate();
    }
    
    public static double aComplexity(IScheme scheme){
        double complexity = 1.0;
        for(int i=0; i< scheme.getElements().size(); i++){
            complexity *= scheme.getElements().get(i).getAlphabet().getAlphabet().size();
        }
        complexity = (double) complexity / Math.pow(TimeSeriesDiscretize_source.MAX_NUMBER_OF_ALPHABET_CUTS,scheme.getElements().size());
        return complexity;
    }
    
    public static double SegmentInfo(IScheme scheme, Data ds){
        double[] entropy = new double[scheme.getElements().size()];
        
        int[] intervalW = new int[2];
        intervalW[0] = 1;
        for (int w=0; w<scheme.getElements().size(); w++){
            intervalW[1] = scheme.getElements().get(w).getCut();
            double[] intervalA = new double[2];
            intervalA[0] = DataSets.DataSet.limits[0][0];
            entropy[w] = 0;
            for(int a=0; a<scheme.getElements().get(w).getAlphabet().getAlphabet().size();a++ ){
                intervalA[1] = scheme.getElements().get(w).getAlphabet().getAlphabet().get(a);
                Map<Integer, Integer> class_frecuency = ds.getCountClassFrom(intervalW, intervalA);
                int NoTS = 0;
                for (Integer i : class_frecuency.values()) {
                    NoTS += i;
                }
                double class_entropy = 0;
                for(Integer klass: ds.getClasses()){
                    if(class_frecuency.containsKey(klass)){
                        class_entropy += (((double) class_frecuency.get(klass)/NoTS) * Math.log((double) class_frecuency.get(klass)/NoTS));
                    }
                    
                }
                class_entropy *= -1;
                entropy[w] += class_entropy;
                intervalA[0] = scheme.getElements().get(w).getAlphabet().getAlphabet().get(a);
            }
            intervalW[1] = scheme.getElements().get(w).getCut();
        }
        
        return DoubleStream.of(entropy).average().getAsDouble();
    }
    
    public static double STDPenalty(IScheme scheme, Data ds){
        int[] intervalW = new int[2];
        intervalW[0] = 1;
        double sumStd = 0;
        for (int w=0; w<scheme.getElements().size(); w++){
            intervalW[1] = scheme.getElements().get(w).getCut();
            List<Double> values = ds.getValuesFrom(intervalW[0],intervalW[1]);
            sumStd = mimath.MiMath.getDesviacionStandar(values);
            intervalW[1] = scheme.getElements().get(w).getCut();
        }
        return (double) sumStd/scheme.getElements().size();
    }
    
    public static double InfoLoss(Data ds, ReconstructedData ds_res){
        double sum=0;
        for(int i=0;i<ds.getNormalized().length;i++){
            double[] normalized = Arrays.copyOfRange(ds.getNormalized()[i], 1, ds.getNormalized()[i].length-1);
            double[] reconstructed = Arrays.copyOfRange(ds_res.getData()[i], 1, ds_res.getData()[i].length-1);
            double mse = mimath.MiMath.MSE(normalized,reconstructed);
//            System.out.println(mse);
            sum+=mse;
        }
        
        return (double) sum/ds.getNormalized().length;
    }
    
    public static double KnnAccuracy(DataSets.DiscretizedData ds_dis){
        int[] acerto = new int[ds_dis.getIds_discretized().length];
//        int[] predicted = new int[ds_dis.getIds_discretized().length];
        for(int f=0;f<ds_dis.getIds_discretized().length;f++){
            double min_dis = Double.POSITIVE_INFINITY;
            int klass = Integer.MAX_VALUE;
            int[] serie = Arrays.copyOfRange(ds_dis.getIds_discretized()[f], 1, ds_dis.getDimensions()[1]);
            for(int j=f+1; j<ds_dis.getIds_discretized().length; j++){
                int[] compared = Arrays.copyOfRange(ds_dis.getIds_discretized()[j], 1, ds_dis.getDimensions()[1]);
                double dist = mimath.MiMath.getEuclideanDist(serie, compared);
                if(dist < min_dis){
                    min_dis = dist;
                    klass = ds_dis.getIds_discretized()[j][0];
//                    predicted[f] = ds_dis.getIds_discretized()[j][0];
                }
            }
            acerto[f] = (klass == ds_dis.getIds_discretized()[f][0]) ? 1 : 0;
        }
//        for(int i = 0; i< acerto.length; i++) System.out.println(ds_dis.getIds_discretized()[i][0]+","+predicted[i]);
        return mimath.MiMath.getMedia(acerto);
    }
}
