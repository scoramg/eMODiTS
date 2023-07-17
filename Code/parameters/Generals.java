/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parameters;

import Interfaces.IPopulation;
import ParetoFront.ParetoFrontCollection;
//import Populations.*;
import ca.nengo.io.MatlabExporter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mimath.MiMath;

/**
 *
 * @author amarquezgr
 */
public class Generals implements Cloneable{
    public IPopulation population, accumulatedFront;
//    public MOPopulation MOPopulation;
    private int iFitnessFunctionConf, iAlgorithm;
    private int iApproach, nExecutions, nGenerations, opponents;
    private boolean isSelfAdaption;
    private double[] weights;
    private double MutationRate, CrossOverRate;
    private long totalTime;
    public MatlabExporter exporter;
    public float[][] misclassification_rates;
    private float[][] Statistics;
    private int TypeDataSet; //0-Todo, 1-Train, 2-Test
    public ParetoFrontCollection execution_front;

    public IPopulation getPopulation() {
        return population;
    }

    public IPopulation getAccumulatedFront() {
        return accumulatedFront;
    }

    public int getiFitnessFunctionConf() {
        return iFitnessFunctionConf;
    }

    public int getiApproach() {
        return iApproach;
    }

    public int getnGenerations() {
        return nGenerations;
    }

    public int getOpponents() {
        return opponents;
    }

    public boolean isIsSelfAdaption() {
        return isSelfAdaption;
    }

    public double[] getWeights() {
        return weights;
    }

    public double getMutationRate() {
        return MutationRate;
    }

    public double getCrossOverRate() {
        return CrossOverRate;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public MatlabExporter getExporter() {
        return exporter;
    }

    public float[][] getMisclassification_rates() {
        return misclassification_rates;
    }

    public float[][] getStatistics() {
        return Statistics;
    }

    public int getnExecutions() {
        return nExecutions;
    }

    public int getiAlgorithm() {
        return iAlgorithm;
    }

    public int getTypeDataSet() {
        return TypeDataSet;
    }
    
    public void setnExecutions(int nExecutions) {
        this.nExecutions = nExecutions;
        this.misclassification_rates = new float[nExecutions][3];
    }
    
    public void setPopulation(IPopulation population) {
        this.population = population;
    }

    public void setAccumulatedFront(IPopulation accumulatedFront) {
        this.accumulatedFront = accumulatedFront;
    }

    public void setiFitnessFunctionConf(int iFitnessFunctionConf) {
        this.iFitnessFunctionConf = iFitnessFunctionConf;
    }

    public void setiApproach(int iApproach) {
        this.iApproach = iApproach;
    }

    public void setnGenerations(int nGenerations) {
        this.nGenerations = nGenerations;
    }

    public void setOpponents(int opponents) {
        this.opponents = opponents;
    }

    public void setIsSelfAdaption(boolean isSelfAdaption) {
        this.isSelfAdaption = isSelfAdaption;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public void setMutationRate(double MutationRate) {
        this.MutationRate = MutationRate;
    }

    public void setCrossOverRate(double CrossOverRate) {
        this.CrossOverRate = CrossOverRate;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public void setExporter(MatlabExporter exporter) {
        this.exporter = exporter;
    }

    public void setiAlgorithm(int iAlgorithm) {
        this.iAlgorithm = iAlgorithm;
    }

    public void setTypeDataSet(int TypeDataSet) {
        this.TypeDataSet = TypeDataSet;
    }
    
    public Generals() {
        this.exporter = new MatlabExporter();
//        this.population = new Population();
        this.Statistics = new float[1][5];
        this.execution_front = new ParetoFrontCollection();
    }
    
    public Generals(int nExecutions, int iFitnessFunctionConf, int PopulationSize, int iApproach, int nGenerations, int opponents, boolean isSelfAdaption, double[] weights, double MutationRate, long totalTime, double CrossOverRate, int iAlgorithm, IPopulation population, int TypeDataSet) {
        this.nExecutions = nExecutions;
        this.iFitnessFunctionConf = iFitnessFunctionConf;
        this.iApproach = iApproach;
        this.nGenerations = nGenerations;
        this.opponents = opponents;
        this.isSelfAdaption = isSelfAdaption;
        this.weights = weights;
        this.MutationRate = MutationRate;
        this.CrossOverRate = CrossOverRate;
        this.totalTime = totalTime;
        this.exporter = new MatlabExporter();
        this.population = population;
        this.misclassification_rates = new float[nExecutions][3];
        this.Statistics = new float[1][5];
        this.iAlgorithm = iAlgorithm;
        this.TypeDataSet = TypeDataSet;
        this.execution_front = new ParetoFrontCollection();
    }
    
    public void CalculateStatistics(){
        List<Double> data = new ArrayList<>();
        
        for(int i=0;i<this.nExecutions;i++){
            data.add(Double.valueOf(this.misclassification_rates[i][2]));
        }
        Statistics[0][0] = (float) MiMath.getMejor(data);
        Statistics[0][1] = (float) MiMath.getPeor(data);
        Statistics[0][2] = (float) MiMath.getMediana(data);
        Statistics[0][3] = (float) MiMath.getMedia(data);
        Statistics[0][4] = (float) MiMath.getDesviacionStandar(data);
    }
    
    @Override
    public Generals clone() {
        try {
            super.clone();
            Generals clon = new Generals();
            clon.setCrossOverRate(this.CrossOverRate);
            clon.setExporter(this.exporter);
            clon.setIsSelfAdaption(this.isSelfAdaption);
            clon.setMutationRate(this.MutationRate);
            clon.setOpponents(this.opponents);
            clon.setPopulation(this.population.clone());
            clon.setTotalTime(this.totalTime);
            clon.setWeights(this.weights.clone());
            clon.setiApproach(this.iApproach);
            clon.setiFitnessFunctionConf(this.iFitnessFunctionConf);
            clon.setnGenerations(this.nGenerations);
            clon.misclassification_rates = this.misclassification_rates.clone();
            clon.Statistics = this.Statistics.clone();
            clon.setiAlgorithm(this.iAlgorithm);
            clon.setTypeDataSet(this.TypeDataSet);
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Generals.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
