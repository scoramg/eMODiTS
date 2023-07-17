/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataMining.Classification;

import DataSets.DataSet;
import Exceptions.MyException;
import RAW.RAW;
//import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import DataMining.Classification.Evaluation;
import weka.classifiers.lazy.IBk;

/**
 *
 * @author amarquezgr
 */
public class StatisticRatesCollection implements Cloneable{
    private List<StatisticRates> RatesByClass;
    public Evaluation eval;
//    private double Accuracy;
//    private double Sensitivity;
//    private double Specificity;

//    public double getAccuracy() {
//        return Accuracy;
//    }
//
//    public double getSensitivity() {
//        return Sensitivity;
//    }
//
//    public double getSpecificity() {
//        return Specificity;
//    }

    public StatisticRatesCollection() {
        RatesByClass = new ArrayList<>();
    }
    
    public StatisticRatesCollection(Classification csf) throws Exception {
        RatesByClass = new ArrayList<>();
//        System.out.println(csf.eval.toMatrixString());
        this.eval = csf.eval;
        for (int i = 0; i < this.eval.getM_NumClasses(); i++) {
            add(i);
        }
    }
    
    public double getNumInstances(int classindex){
        double instances = 0;
        double[][] confusion_matrix = this.eval.confusionMatrix();
        for (int i=0; i<this.eval.getM_NumClasses();i++){
            instances += confusion_matrix[classindex][i];
        }
        return instances;
    }
    
    public double getPctCorrect(int classindex, double InstancesPerClass){
        double correct = 0;
        double[][] confusion_matrix = this.eval.confusionMatrix();
        correct = confusion_matrix[classindex][classindex];
        return (double) (correct / InstancesPerClass);
    }
    
    public double getPctIncorrect(int classindex, double InstancesPerClass){
        double correct = 0;
        double[][] confusion_matrix = this.eval.confusionMatrix();
        correct = confusion_matrix[classindex][classindex];
        return (double) ((InstancesPerClass - correct) / InstancesPerClass);
    }
    
    public double getWeightedAvgPctCorrect(){
        double pctCorrectTotal = 0, total_instances = 0;
        for (int i = 0; i < this.eval.getM_NumClasses(); i++) {
            double intancesPerClass = getNumInstances(i);
            total_instances += intancesPerClass;
            double temp = getPctCorrect(i, intancesPerClass);
            pctCorrectTotal += (temp * intancesPerClass);
        }

        return pctCorrectTotal / total_instances;
    }
    
    public double getWeightedAvgPctIncorrect(){
        double pctIncorrectTotal = 0, total_instances = 0;
        for (int i = 0; i < this.eval.getM_NumClasses(); i++) {
            double intancesPerClass = getNumInstances(i);
            total_instances += intancesPerClass;
            double temp = getPctIncorrect(i, intancesPerClass);
            pctIncorrectTotal += (temp * intancesPerClass);
        }

        return pctIncorrectTotal / total_instances;
    }
    
    public double getAccuracy(int classindex){
        double TP = this.eval.numTruePositives(classindex);
        double TN = this.eval.numTrueNegatives(classindex);
        double FP = this.eval.numFalsePositives(classindex);
        double FN = this.eval.numFalseNegatives(classindex);
//        System.out.println("Class:"+classindex+", TP:"+TP+", TN:"+TN+", FP:"+FP+", FN:"+FN+", Acc:"+(TP + TN)/(TP + TN + FN + FP));
        return (TP + TN)/(TP + TN + FN + FP);
    }
    
    public double getWeightedAvgAccuracy(){
        double accuracyTotal = 0, total_instances = 0;
        for (int i = 0; i < this.eval.getM_NumClasses(); i++) {
            double intancesPerClass = getNumInstances(i);
            total_instances += intancesPerClass;
            double temp = getAccuracy(i);
            accuracyTotal += (temp * intancesPerClass);
        }

        return accuracyTotal / total_instances;
    }
    
    public double getSensitivity(int classindex){
        double TP = this.eval.numTruePositives(classindex);
        double FN = this.eval.numFalseNegatives(classindex);
        return TP/(TP + FN);
    }
    
    public double getWeightedAvgSensitivity(int classindex){
        double sensitivityTotal = 0, total_instances = 0;
        for (int i = 0; i < this.eval.getM_NumClasses(); i++) {
            double intancesPerClass = getNumInstances(i);
            total_instances += intancesPerClass;
            double temp = getSensitivity(i);
            sensitivityTotal += (temp * intancesPerClass);
        }

        return sensitivityTotal / total_instances;
    }
    
    public double getSpecificity(int classindex){
        double TN = this.eval.numTrueNegatives(classindex);
        double FP = this.eval.numFalsePositives(classindex);
        return TN/(TN + FP);
    }
    
    public double getWeightedAvgSpecificity(int classindex){
        double specificityTotal = 0, total_instances = 0;
        for (int i = 0; i < this.eval.getM_NumClasses(); i++) {
            double intancesPerClass = getNumInstances(i);
            total_instances += intancesPerClass;
            double temp = getSpecificity(i);
            specificityTotal += (temp * intancesPerClass);
        }

        return specificityTotal / total_instances;
    }
    
    public void add(int klass){ //Se sacan de la mejor solución de las 15 ejecuciones.
        String[] classes = this.eval.getM_ClassNames();
        double total_instances = getNumInstances(klass);
        double pctCorrect = getPctCorrect(klass, total_instances);
        double pctIncorrect = getPctIncorrect(klass, total_instances);
        StatisticRates sr = new StatisticRates(classes[klass], pctCorrect, pctIncorrect, this.eval.truePositiveRate(klass),
                this.eval.falsePositiveRate(klass), this.eval.precision(klass), this.eval.recall(klass), this.eval.fMeasure(klass), 
                this.eval.matthewsCorrelationCoefficient(klass), this.eval.areaUnderROC(klass), this.eval.areaUnderROC(klass), getAccuracy(klass), 
                getSensitivity(klass), getSpecificity(klass));
//        System.out.println(sr.toString());
        this.RatesByClass.add(sr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i=0;
        for(StatisticRates sr: RatesByClass){
            if (i>0){
//                sb.append(",").append(",").append(",").append(",").append(",");
                sb.append(",").append(",");
            }
            sb.append(sr.toString()).append("\n");
            i++;
        }
        sb.append(",").append(",").append("Weighted Avg.,").append(getWeightedAvgPctCorrect()).append(",").append(getWeightedAvgPctIncorrect()).append(",");
        sb.append(this.eval.weightedTruePositiveRate()).append(",").append(this.eval.weightedFalsePositiveRate()).append(",");
        sb.append(this.eval.weightedPrecision()).append(",").append(this.eval.weightedRecall()).append(",").append(this.eval.weightedFMeasure()).append(",");
        sb.append(this.eval.weightedMatthewsCorrelation()).append(",").append(this.eval.weightedAreaUnderROC()).append(",");
        sb.append(this.eval.weightedAreaUnderPRC()).append(",").append(this.getWeightedAvgAccuracy()).append(",").append(this.getWeightedAvgSensitivity(i)).append(",");
        sb.append(this.getWeightedAvgSpecificity(i)).append("\n");
        return sb.toString();
    }

    @Override
    public StatisticRatesCollection clone() throws CloneNotSupportedException {
        super.clone();
        StatisticRatesCollection src = new StatisticRatesCollection();
        src.eval = this.eval;
//        src.Accuracy = this.Accuracy;
//        src.Sensitivity = this.Sensitivity;
//        src.Specificity = this.Specificity;
        src.RatesByClass = new ArrayList<>();
        for(StatisticRates sr: this.RatesByClass){
            src.RatesByClass.add(sr.clone());
        }
        return src; //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void main(String[] args) throws MyException {
        DataSet ds = new DataSet(93, false);
        RAW raw = new RAW();
        raw.Classify(ds, new IBk(7), false,"original");
    }
    
    
}
