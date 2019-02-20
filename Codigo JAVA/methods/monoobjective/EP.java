/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package methods.monoobjective;

import Algorithms.selection.StochasticRanking;
import Algorithms.selection.TournamentSelection;
import DataSets.Data;
import Interfaces.IPopulation;
import Interfaces.iMethod;
import Populations.Population;
import parameters.*;
import Utils.Utils;

/**
 *
 * @author amarquezgr
 */
public class EP implements iMethod{

    public EP() {
    }
    
    /**
     *
     * @param general_params
     * @param local_params
     */
    @Override
    public void Execute(Generals general_params, Locals local_params){
        System.out.println("EP");
        local_params.setTimeAlgorithm(System.currentTimeMillis());
        
        Data ds = new Data();
        
        switch(general_params.getTypeDataSet()){
            case 0:
                ds = local_params.getDs().getOriginal();
                break;
            case 1:
                ds = local_params.getDs().getTrain();
                break;
            case 2:
                ds = local_params.getDs().getTest();
                break;
        }
        
        general_params.population.Generate(general_params.getiApproach(), ds, local_params.getDs().getLimits(), general_params.getiFitnessFunctionConf());
        general_params.population.Evaluate(ds, general_params.getWeights());
        
        Utils.CalculateMeasure(0,general_params.population, local_params);
        
        for(int g=0; g<general_params.getnGenerations();g++){
            System.out.println("Running execution:"+(local_params.getExecution()+1)+", Generation:"+(g+1));
            ReplaceHalf(general_params, ds, local_params.getDs().getLimits(), g+1);
            local_params.ConvergenceData[g][0] = (float) general_params.population.getBest().getFitnessValue();
            Utils.CalculateMeasure(g+1,general_params.population, local_params);
        }
        
        local_params.setTimeAlgorithm(System.currentTimeMillis() - local_params.getTimeAlgorithm());
        general_params.population.getBest().Classify(local_params.getDs(),false, "train");
        general_params.population.getBest().ExportGraph(local_params);
        general_params.accumulatedFront.addIndividual(general_params.population.getBest().clone());
        general_params.misclassification_rates[local_params.getExecution()][0] = local_params.getExecution()+1;
        general_params.misclassification_rates[local_params.getExecution()][1] = (float) general_params.population.getBest().getFitnessValue();
        general_params.misclassification_rates[local_params.getExecution()][2] = (float) general_params.population.getBest().getErrorRate();
        general_params.population.getBest().Export(general_params, local_params);
    }
    public static void ReplaceHalf(Generals general_params, Data ds, double[][] limits, int g){
        general_params.population.sort();
        int newSize = Math.floorDiv(general_params.population.getLength(),2);
        IPopulation bests = general_params.population.Extract(general_params.population, newSize);
        IPopulation offsprings = bests.Mutate(general_params.getMutationRate(), ds, limits);
        offsprings.Evaluate(ds, general_params.getWeights());
        bests.CalculateMutationPerformance(offsprings);
//        local_params.MeasuresData[g][4] = (float) bests.getChangeRate();
//        local_params.MeasuresData[g][5] = (float) bests.getMutationSuccessRate();
        general_params.population = bests.Join(offsprings).clone();
    }
    
    public static void ReplaceStochastic(Generals general_params, Data ds, double[][] limits, int g, double pf){
        IPopulation offsprings = general_params.population.Mutate(general_params.getMutationRate(), ds, limits);
        offsprings.Evaluate(ds, general_params.getWeights());
        general_params.population.CalculateMutationPerformance(offsprings);
//        local_params.MeasuresData[g][4] = (float) general_params.population.getChangeRate();
//        local_params.MeasuresData[g][5] = (float) general_params.population.getMutationSuccessRate();
        IPopulation joined = general_params.population.Join(offsprings);
        StochasticRanking sr = new StochasticRanking(joined, pf);
        general_params.population.Replace(sr.selected);
    }
    
    public static void ReplaceTournament(Generals general_params, Data ds, double[][] limits, int g){
        IPopulation offsprings = general_params.population.Mutate(general_params.getMutationRate(), ds, limits);
        offsprings.Evaluate(ds, general_params.getWeights());
        general_params.population.CalculateMutationPerformance(offsprings);
//        local_params.MeasuresData[g][4] = (float) general_params.population.getChangeRate();
//        local_params.MeasuresData[g][5] = (float) general_params.population.getMutationSuccessRate();
        IPopulation joined = general_params.population.Join(offsprings);
        TournamentSelection tournament = new TournamentSelection(joined, general_params.getOpponents());
        general_params.population.Replace(tournament.getSelected());
    }
}
