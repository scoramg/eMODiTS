/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import DataSets.Data;
import DataSets.DataSet;
import ca.nengo.io.MatlabExporter;
import java.util.List;
import parameters.*;

/**
 *
 * @author amarquezgr
 */
public interface IPopulation {
    public IScheme[] getIndividuals();
    public int getLength();
    public boolean isIsSelfAdaptation();
    public double getTotalFitness();
    public double getCrossoverSuccessRate();
    public List<Double> getAllFitnessValue();
    public IScheme getBest();
    public IScheme getBestByErrorRate();
    public double getChangeRate();
    public double getMutationSuccessRate();
    public String getName();
    public void addIndividual(IScheme individual);
    
    public void setLength(int length);
    public void setAt(IScheme ind, int j);
    
    
    public void Generate(int iApproach, Data ds, double[][] limits, int iFitnessFunctionConf);
    public IPopulation Join(IPopulation p);
    public IPopulation Extract(IPopulation pop, int newSize);
    public IPopulation CreateOffsprings(Generals general_params, Data ds);
    public IPopulation Mutate(double MutationRate, Data ds, double[][] limits);
    public void Evaluate(Data ds, double[] weights);
    public void Replace(IPopulation pop);
    public void sort();
    
    public void CalculateMutationPerformance(IPopulation offsprings);
    public void reset();
    public IPopulation clone();
    public float[][] getArrayFitnessValuesWithRank();
    
    public void Classify(DataSet ds, boolean UsingTest);
}
