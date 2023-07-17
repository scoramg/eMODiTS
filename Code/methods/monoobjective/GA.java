/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package methods.monoobjective;

import Algorithms.selection.RouletteSelection;
import DataSets.Data;
import Interfaces.iMethod;
import Populations.Population;
import parameters.*;
import Utils.Utils;

/**
 *
 * @author amarquezgr
 */
public class GA implements iMethod{

    public GA() {
    }

    
    /**
     *
     * @param general_params
     * @param local_params
     */
    @Override
    public void Execute(Generals general_params, Locals local_params){
        System.out.println("GA");
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

            RouletteSelection parents = new RouletteSelection(general_params.population);
            Population offsprings = (Population) parents.selected.CreateOffsprings(general_params, ds);
            local_params.MeasuresData[g][6] = (float) parents.selected.getCrossoverSuccessRate();
            offsprings = offsprings.Mutate(general_params.getMutationRate(), ds, local_params.getDs().getLimits());
            offsprings.Evaluate(ds, general_params.getWeights());
            general_params.population.Replace(general_params.population.Join(offsprings));
            
            local_params.ConvergenceData[g][0] = (float) general_params.population.getBest().getFitnessValue();
            Utils.CalculateMeasure(g+1,general_params.population, local_params);
            
        }

        local_params.setTimeAlgorithm(System.currentTimeMillis() - local_params.getTimeAlgorithm());
        general_params.population.getBest().Classify(local_params.getDs(),false, "train");
        general_params.population.getBest().ExportGraph(local_params);
        general_params.accumulatedFront.addIndividual(general_params.population.getBest());
        general_params.misclassification_rates[local_params.getExecution()][0] = local_params.getExecution()+1;
        general_params.misclassification_rates[local_params.getExecution()][1] = (float) general_params.population.getBest().getFitnessValue();
        general_params.misclassification_rates[local_params.getExecution()][2] = (float) general_params.population.getBest().getErrorRate();
        general_params.population.getBest().Export(general_params, local_params);
    }
}
