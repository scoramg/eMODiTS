/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FitnessFunctions;

import BeansCL.DiscreteHistogramCollection;
import BeansCL.HistogramCollection;
import BeansCL.HistogramScheme;
import BeansCL.OneDimensionHistogram;
import DataSets.Data;
import DataSets.DiscretizedData;
import DataSets.ReconstructedData;
import Interfaces.IScheme;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mimath.MiMath;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;

/**
 *
 * @author amarquezgr
 */
public class FitnessFunction  implements Cloneable {
    
    private int nofunctions;
    private int iConfiguration;
    private int[] idFunctions;
    private double[] weights;
    private double EvaluatedValue;
    private double[] EvaluatedValues;
    private Classifier classifier;

    public void setNofunctions(int nofunctions) {
        this.nofunctions = nofunctions;
    }

    public void setIdFunctions(int[] idFunctions) {
        this.idFunctions = idFunctions.clone() ;
    }

    public void setEvaluatedValue(double EvaluatedValue) {
        this.EvaluatedValue = EvaluatedValue;
    }

    public int getNofunctions() {
        return nofunctions;
    }

    public int[] getIdFunctions() {
        return idFunctions;
    }

    public final void setiConfiguration(int iConfiguration) {
        this.iConfiguration = iConfiguration;
    }
    
    public FitnessFunction() {
        classifier = null;
    }
    
    public FitnessFunction(int iConfiguration) {
        this.iConfiguration = iConfiguration;
        this.setConfiguration();
//        this.nofunctions = no_functions;
//        this.idFunctions = new int[no_functions];
        this.weights = new double[this.nofunctions];
//        this.EvaluatedValues = new double[no_functions];
    }
    
    public FitnessFunction(int iConfiguration, double[] weights) {
//        this.nofunctions = no_functions;
//        this.idFunctions = new int[no_functions];
        this.iConfiguration = iConfiguration;
        this.setConfiguration();
        this.weights = weights;
    }
    
    public void addFunction(int idfunction, int position){
        idFunctions[position] = idfunction;
    }
    
    public void addWeights(double weight, int position){
        this.weights[position] = weight;
    }
    
//    public void Evaluate(Data ds, DiscretizedData ds_dis, List<WordCut> scheme) {
//        ConfusionMatrix MC = new ConfusionMatrix(ds, ds_dis);
//        
//        for(int f=0; f< this.nofunctions;f++){
//            switch(this.idFunctions[f]){
//                case 0:
//                    EvaluatedValues[f] = Functions.EPAccuracy(MC);
//                    break;
//                case 1:
//                    EvaluatedValues[f] = Functions.EPComplexity(MC, ds);
//                    break;
//                case 2:
//                    EvaluatedValues[f] = Functions.EPCompression(ds_dis.getDimensions()[1]-1,ds.getDimensions()[1]-1);
//                    break;
//                case 3:
//                    EvaluatedValues[f] = Functions.getMissclassificationRate(ds_dis, this.classifier);
//                    break;
//                case 4:
//                    EvaluatedValues[f] = Functions.aComplexity(scheme);
//                    break;
//                case 5:
//                    EvaluatedValues[f] = Functions.getMissclassificationRate(ds_dis, classifier);
//                    break;    
//            }
//            this.EvaluatedValue += this.weights[f] * EvaluatedValues[f];
//        }
//    }
    
    public void Evaluate(HistogramCollection ds, HistogramScheme scheme) throws Exception {
//        DiscreteHistogramCollection dhc = ds.Discretize(scheme);
        DiscreteHistogramCollection dhc = scheme.Discretize(ds);
        List<OneDimensionHistogram> dd = dhc.toOneDimensionalDataSet();
        for(int f=0; f< this.nofunctions;f++){
            switch(this.idFunctions[f]){
                case 9:
                    this.EvaluatedValues[f] = (double) dd.get(0).getData().size()/Functions.KnnAccuracy(dd);
                    break;
            }
            this.EvaluatedValue += this.weights[f] * EvaluatedValues[f];
        }
        
    }
    
    public void Evaluate(Data ds, IScheme scheme) throws Exception {
        DiscretizedData ds_dis = scheme.DiscretizeByPAA(ds);
        ConfusionMatrix MC = new ConfusionMatrix(ds, ds_dis);
        
        for(int f=0; f< this.nofunctions;f++){
            switch(this.idFunctions[f]){
                case 0:
                    EvaluatedValues[f] = Functions.EPAccuracy(MC);
                    break;
                case 1:
                    EvaluatedValues[f] = Functions.EPComplexity(MC, ds);
                    break;
                case 2:
                    EvaluatedValues[f] = Functions.EPCompression(ds_dis.getDimensions()[1]-1,ds.getDimensions()[1]-1);
                    break;
                case 3:
                    EvaluatedValues[f] = Functions.getMissclassificationRate(ds_dis, this.classifier);
                    break;
                case 4:
                    EvaluatedValues[f] = Functions.aComplexity(scheme);
                    break;
                case 5:
                    EvaluatedValues[f] = Functions.SegmentInfo(scheme,ds);
                    break; 
                case 6:
                    EvaluatedValues[f] = Functions.PermutationEntropyComplexity(MC, ds);
                    break;   
                case 7:
                    EvaluatedValues[f] = Functions.STDPenalty(scheme, ds);
                    break;  
                case 8:
                    ReconstructedData ds_recons = scheme.Reconstruct(ds_dis);
                    EvaluatedValues[f] = Functions.InfoLoss(ds, ds_recons);
                    break;      
            }
            this.EvaluatedValue += this.weights[f] * EvaluatedValues[f];
        }
    }

    public void updateWeights(int indice) {
        this.weights[indice] = MiMath.random(0.0, 1.0);
    }

    public double[] getWeights() {
        return this.weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights.clone();
    }
    
    public final void setConfiguration(){
        switch(this.iConfiguration){
            case 1: // EPEntropy, EPComplexity and EPCompression
                this.nofunctions=3;
                this.idFunctions = new int[this.nofunctions];
                idFunctions[0] = 0;
                idFunctions[1] = 1;
                idFunctions[2] = 2;
                this.EvaluatedValues = new double[this.nofunctions];
                break;
            case 2: // Tree Classification and EPComplexity
                this.nofunctions = 2;
                this.idFunctions = new int[this.nofunctions];
                this.idFunctions[0] = 3;
                this.idFunctions[1] = 1;
                this.classifier = new J48();
                this.EvaluatedValues = new double[this.nofunctions];
                break;
            case 3: //EPEntropy and EPComplexity
                this.nofunctions = 2;
                this.idFunctions = new int[this.nofunctions];
                idFunctions[0] = 0;
                idFunctions[1] = 1;
                this.EvaluatedValues = new double[this.nofunctions];
                break;
            case 4: //EPEntropy and EPCompression
                this.nofunctions = 2;
                this.idFunctions = new int[this.nofunctions];
                idFunctions[0] = 0;
                idFunctions[1] = 2;
                this.EvaluatedValues = new double[this.nofunctions];
                break;
            case 5: //EPCOmplexity and EPCompression
                this.nofunctions = 2;
                this.idFunctions = new int[this.nofunctions];
                idFunctions[0] = 1;
                idFunctions[1] = 2;
                this.EvaluatedValues = new double[this.nofunctions];
                break;
            case 6: // Tree Classification and EPCompression
                this.nofunctions = 2;
                this.idFunctions = new int[this.nofunctions];
                this.idFunctions[0] = 3;
                this.idFunctions[1] = 2;
                this.classifier = new J48();
                this.EvaluatedValues = new double[this.nofunctions];
                break;  
            case 7: // EPEntropy and aComplexity
                this.nofunctions = 2;
                this.idFunctions = new int[this.nofunctions];
                this.idFunctions[0] = 3;
                this.idFunctions[1] = 4;
                this.classifier = new J48();
                this.EvaluatedValues = new double[this.nofunctions];
                break;    
            case 8: // 
                this.nofunctions = 3;
                this.idFunctions = new int[this.nofunctions];
                this.idFunctions[0] = 0;
                this.idFunctions[1] = 1;
                this.idFunctions[2] = 8;
                this.classifier = new J48();
                this.EvaluatedValues = new double[this.nofunctions];
                break; 
            case 9: // 
                this.nofunctions = 1;
                this.idFunctions = new int[this.nofunctions];
                this.idFunctions[0] = 9;
                this.classifier = new J48();
                this.EvaluatedValues = new double[this.nofunctions];
                break; 
        }
    }

    public FitnessFunction MutateWeights(double MutationRate) {    
        double[] w = this.weights;
        int i = MiMath.randomInt(0, this.getNoFunctions());
        
        w[i] = mimath.MiMath.random(0, 1);
        
        double max = -1;
        int imax = -1;
        
        for(int j=0;j<this.getNoFunctions();j++){
            if(j!=i){
                if(w[j]>max){
                    max = w[j];
                    imax = j;
                }
            }
        }
        
        double sum = 0.0;
        for (int s=0;s<this.getNoFunctions();s++){
            if(s!=imax){
                sum += w[s];
            }
        }
        
        w[imax] = Math.abs(1-sum);
        
        return new FitnessFunction(this.iConfiguration, w);
    }

    public void ImportFromMatFile(String filename) {
        try {
            MatFileReader mfr = new MatFileReader(filename);
            Map<String, MLArray> mlArrayRetrived = mfr.getContent();
            
            MLArray w = mlArrayRetrived.get("fitness_weights");
            this.weights = ((MLDouble) w).getArray()[0];
        } catch (IOException ex) {
            Logger.getLogger(FitnessFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public FitnessFunction clone() {
        try {
            super.clone();
            FitnessFunction clon = new FitnessFunction();
            clon.setWeights(this.weights);
            clon.setEvaluatedValues(this.getEvaluatedValues());
            clon.setEvaluatedValue(this.EvaluatedValue);
            clon.setIdFunctions(this.idFunctions);
            clon.setNofunctions(this.nofunctions);
            clon.classifier = this.classifier;
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(FitnessFunction.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
    }

    public double getEvaluatedValue() {
        return this.EvaluatedValue;
    }

    public double[] getEvaluatedValues() {
        return this.EvaluatedValues;
    }

    public void setEvaluatedValues(double[] EvaluatedValues) {
        this.EvaluatedValues = EvaluatedValues.clone();
    }

    public int getNoFunctions() {
        return this.nofunctions;
    }
    
}
