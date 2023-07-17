/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeSeriesDiscretize;

import java.util.List;

//import ParetoFront.ParetoFront;
import parameters.*;

/**
 *
 * @author amarquezgr
 */
public class TimeSeriesDiscretize_source implements Runnable {
    
    public static final int MAX_NUMBER_OF_ALPHABET_CUTS = 26;
    public static final int MIN_NUMBER_OF_ALPHABET_CUTS = 1;
    public static final int MIN_NUMBER_OF_WORD_CUTS = 1;
    public static final double MAX_NUMBER_OF_WORD_CUTS = 0.9;
    public static List<Character> symbols;
    
    Generals general_params;
    Locals local_params;

    public TimeSeriesDiscretize_source(Generals general_params, Locals local_params) {
        this.general_params = general_params;
        this.local_params = local_params;
        symbols = Utils.Utils.getListSymbols();
    }
    
    
    
//    public TimeSeriesDiscretize_source(DataSet ds, int iFitnessFunction, int PopulationSize, int iApproach, int nGenerations, boolean isSelfAdaption,double[] weights, double MutationRate, int opponents, boolean isUpdate, int execution, boolean isGiny, double CrossOverRate)  {
//        general_params = new Generals(ds, iFitnessFunction, PopulationSize, iApproach, execution, nGenerations, opponents, isSelfAdaption, weights, MutationRate, iApproach, execution, isUpdate, isGiny,CrossOverRate);
//    }
//    
//    public TimeSeriesDiscretize_source(DataSet ds, int iFitnessFunction, int PopulationSize, int iApproach, int nGenerations, boolean isSelfAdaption,double[] weights, double MutationRate, int opponents, boolean isUpdate, int execution, boolean isGiny)  {
//        general_params = new Generals(ds, iFitnessFunction, PopulationSize, iApproach, execution, nGenerations, opponents, isSelfAdaption, weights, MutationRate, iApproach, execution, isUpdate, isGiny);
//    }
//    
//    public TimeSeriesDiscretize_source(DataSet ds, int iFitnessFunction, int PopulationSize, int iApproach, int nGenerations, boolean isSelfAdaption,double[] weights, double MutationRate, int opponents, boolean isUpdate, int execution, Population population, boolean isGiny)  {
//        general_params = new Generals(population, ds, iFitnessFunction, PopulationSize, iApproach, execution, nGenerations, opponents, isSelfAdaption, weights, MutationRate, iApproach, execution, isUpdate, isGiny);
//    }

    public void setExecution(int execution) {
        local_params.setExecution(execution);
    }
    
    public void Execute(){
        switch(general_params.getiAlgorithm()){
            case 1:
                runEP();
                break;
            case 2:
                runGA();
                break;
            case 3:
                runNSGAII();
                break;    
        }
        
    }
    
    @Override
    public void run() {
        this.Execute();
    }

    public void runGA(){
        methods.monoobjective.GA ga = new methods.monoobjective.GA();
        ga.Execute(general_params, local_params);
    }
    
    public void runEP(){
        methods.monoobjective.EP ep = new methods.monoobjective.EP();
        ep.Execute(general_params, local_params);
    }
    
    public void runNSGAII(){
        methods.multiobjective.NSGA2 nsga2 = new methods.multiobjective.NSGA2();
        nsga2.Execute(general_params, local_params);
    }
}