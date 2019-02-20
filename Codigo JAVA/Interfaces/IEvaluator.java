/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import DataSets.*;

/**
 *
 * @author amarquezgr
 */
public interface IEvaluator {
    public void Evaluate(Data ds, DiscretizedDataSet ds_dis);
//    public double getFitness_value();
    
    public void updateWeights(int indice);
    public double[] getWeights();
    public void setWeights(double[] weights);
    
    public IEvaluator MutateWeights(double MutationRate);
    public void ImportFromMatFile(String filename);
    public IEvaluator clone();
    
    public double getEvaluatedValue();
    public double[] getEvaluatedValues();
    public void setEvaluatedValues(double[] EvaluatedValues);
    
//    public double[] getFitness_values();
    public int getNoFunctions();
}
