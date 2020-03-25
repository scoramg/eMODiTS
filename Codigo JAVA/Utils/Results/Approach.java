/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Results;

import Exceptions.MyException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;
import weka.classifiers.evaluation.Prediction;

/**
 *
 * @author amarquezgr
 */
public class Approach implements Cloneable{
    private double ErrorRate, pvalue, StandardDeviation;
    private double[] ErrorRates;
    private int rank;
    private String name, h0;
    private boolean IsApproachBase;
    private List<Integer> correctPredictions;

    public double getErrorRate() {
        return ErrorRate;
    }

    public double getPvalue() {
        return pvalue;
    }

    public double getStandardDeviation() {
        return StandardDeviation;
    }

    public double[] getErrorRates() {
        return ErrorRates;
    }

    public String getH0() {
        return h0;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getCorrectPredictions() {
        return correctPredictions;
    }
   
    public boolean isIsApproachBase() {
        return IsApproachBase;
    }

    public void setErrorRate(double ErrorRate) {
        this.ErrorRate = ErrorRate;
    }

    private void setPvalue(double pvalue) {
        this.pvalue = pvalue;
    }

    public void setStandardDeviation(double StandardDeviation) {
        this.StandardDeviation = StandardDeviation;
    }

    public void setErrorRates(double[] ErrorRates) {
        this.ErrorRates = ErrorRates;
    }

    private void setH0(String h0) {
        this.h0 = h0;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsApproachBase(boolean IsApproachBase) {
        this.IsApproachBase = IsApproachBase;
    }

    public void setCorrectPredictions(List<Integer> correctPredictions) {
        this.correctPredictions = correctPredictions;
    }

    public Approach() {
    }
    
    public Approach(double ErrorRate, double[] ErrorRates, String name, boolean IsApproachBase, List<Integer> correctPredictions) {
        this.ErrorRate = ErrorRate;
        this.ErrorRates = ErrorRates.clone();
        this.name = name;
        this.IsApproachBase = IsApproachBase;
        this.StandardDeviation = mimath.MiMath.getDesviacionStandar(ErrorRates);
        this.h0 = "";
        this.pvalue = Double.NaN;
        this.rank = -1;
        this.correctPredictions = correctPredictions;
    }
    
    public void CalculateStats(Approach base, String test) throws MyException{
        
        switch(test){
            default: case "wilcoxon":
                WilcoxonSignedRankTest wrs = new WilcoxonSignedRankTest();
                setPvalue(wrs.wilcoxonSignedRankTest(base.getErrorRates(), this.getErrorRates(), true));
                break;
            case "mcnemar":
                McNemarTest.mcnemar nemar = new McNemarTest.mcnemar(base.getCorrectPredictions(), this.getCorrectPredictions());
                setPvalue(nemar.test());
        }
        
        if (getPvalue() < 0.05){
            if(base.getErrorRate() < this.getErrorRate()){
                setH0("+");
            } else {
                setH0("-");
            }
        } else {
            setH0("=");
        }
    }
    
    @Override
    public Approach clone(){
        try {
            super.clone();
            Approach clon = new Approach(ErrorRate, ErrorRates, name, IsApproachBase, correctPredictions);
            clon.setH0(h0);
            clon.setPvalue(pvalue);
            clon.setRank(rank);
            clon.setStandardDeviation(StandardDeviation);
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Approach.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
