/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataMining.Classification;

/**
 *
 * @author amarquezgr
 */
public class StatisticRates implements Cloneable{
    private String LabelClass;
    private double ClassificationRate;
    private double MisclassificationRate;
    private double TruePositiveRate;
    private double TrueNegativeRate;
    private double Precision;
    private double Recall;
    private double f_measure;
    private double MatthewsCorrelationCoefficient;
    private double AreaUnderROC;
    private double AreaUnderPRC;
    private double Accuracy;
    private double Sensitivity;
    private double Specificity;

    public String getLabelClass() {
        return LabelClass;
    }

    public double getMisclassificationRate() {
        return MisclassificationRate;
    }

    public double getTruePositiveRate() {
        return TruePositiveRate;
    }

    public double getTrueNegativeRate() {
        return TrueNegativeRate;
    }

    public double getPrecision() {
        return Precision;
    }

    public double getRecall() {
        return Recall;
    }

    public double getClassificationRate() {
        return ClassificationRate;
    }

    public double getF_measure() {
        return f_measure;
    }

    public double getMatthewsCorrelationCoefficient() {
        return MatthewsCorrelationCoefficient;
    }

    public double getAreaUnderROC() {
        return AreaUnderROC;
    }

    public double getAreaUnderPRC() {
        return AreaUnderPRC;
    }
    
    public double getAccuracy() {
        return Accuracy;
    }

    public double getSensitivity() {
        return Sensitivity;
    }

    public double getSpecificity() {
        return Specificity;
    }

    public void setLabelClass(String LabelClass) {
        this.LabelClass = LabelClass;
    }

    public void setClassificationRate(double ClassificationRate) {
        this.ClassificationRate = ClassificationRate;
    }

    public void setF_measure(double f_measure) {
        this.f_measure = f_measure;
    }

    public void setMisclassificationRate(double MisclassificationRate) {
        this.MisclassificationRate = MisclassificationRate;
    }

    public void setTruePositiveRate(double TruePositiveRate) {
        this.TruePositiveRate = TruePositiveRate;
    }

    public void setTrueNegativeRate(double TrueNegativeRate) {
        this.TrueNegativeRate = TrueNegativeRate;
    }

    public void setPrecision(double Precision) {
        this.Precision = Precision;
    }

    public void setRecall(double Recall) {
        this.Recall = Recall;
    }

    public void setMatthewsCorrelationCoefficient(double MatthewsCorrelationCoefficient) {
        this.MatthewsCorrelationCoefficient = MatthewsCorrelationCoefficient;
    }

    public void setAreaUnderROC(double AreaUnderROC) {
        this.AreaUnderROC = AreaUnderROC;
    }

    public void setAreaUnderPRC(double AreaUnderPRC) {
        this.AreaUnderPRC = AreaUnderPRC;
    }

    public StatisticRates() {
    }

    public StatisticRates(String LabelClass, double ClassificationRate, double MisclassificationRate, double TruePositiveRate, 
            double TrueNegativeRate, double Precision, double Recall, double F_measure, double MatthewsCorrelationCoefficient, 
            double AreaUnderROC, double AreaUnderPRC, double Accuracy, double Sensitivity, double Specificity) {
        this.LabelClass = LabelClass;
        this.ClassificationRate = ClassificationRate;
        this.MisclassificationRate = MisclassificationRate;
        this.TruePositiveRate = TruePositiveRate;
        this.TrueNegativeRate = TrueNegativeRate;
        this.Precision = Precision;
        this.Recall = Recall;
        this.f_measure = F_measure;
        this.MatthewsCorrelationCoefficient = MatthewsCorrelationCoefficient;
        this.AreaUnderROC = AreaUnderROC;
        this.AreaUnderPRC = AreaUnderPRC;
        this.Accuracy = Accuracy;
        this.Sensitivity = Sensitivity;
        this.Specificity = Specificity;
    }
    
    @Override
    public String toString(){
        return this.LabelClass+","+this.ClassificationRate+","+this.MisclassificationRate+","+this.TruePositiveRate+","+
                this.TrueNegativeRate+","+this.Precision+","+this.Recall+","+this.f_measure+","+this.MatthewsCorrelationCoefficient+","+
                this.AreaUnderROC+","+this.AreaUnderPRC+","+this.Accuracy+","+this.Sensitivity+","+this.Specificity;
    }

    @Override
    protected StatisticRates clone() throws CloneNotSupportedException {
        super.clone();
        StatisticRates sr = new StatisticRates();
        sr.ClassificationRate = this.ClassificationRate;
        sr.AreaUnderPRC = this.AreaUnderPRC;
        sr.AreaUnderROC = this.AreaUnderROC;
        sr.LabelClass = this.LabelClass;
        sr.MatthewsCorrelationCoefficient = this.MatthewsCorrelationCoefficient;
        sr.MisclassificationRate = this.MisclassificationRate;
        sr.Precision = this.Precision;
        sr.Recall = this.Recall;
        sr.f_measure = this.f_measure;
        sr.TrueNegativeRate = this.TrueNegativeRate;
        sr.TruePositiveRate = this.TruePositiveRate;
        sr.Accuracy = this.Accuracy;
        sr.Sensitivity = this.Sensitivity;
        sr.Specificity = this.Specificity;
        return sr; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
