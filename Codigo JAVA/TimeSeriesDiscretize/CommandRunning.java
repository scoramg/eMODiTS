/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeSeriesDiscretize;

import DataSets.DataSet;
import Exceptions.MyException;
//import Interfaces.IPopulation;
import Interfaces.IScheme;
import Populations.MOPopulation;
import Populations.Population;
import ParetoFront.ParetoFront;
//import Utils.Results.Results;
import ca.nengo.io.MatlabExporter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import methods.multiobjective.NSGA2;
import parameters.Generals;
import parameters.Locals;

/**
 *
 * @author amarquezgr
 */
public class CommandRunning {
    public static void main(String[] args) {
        int PS = Integer.valueOf(args[0]); // Tamaño de la población (Population Size)
        int NE = Integer.valueOf(args[1]); // Número de ejecuciones (Number of Execution)
        int NG = Integer.valueOf(args[2]); // Número de Generaciones (Number of Generations)
        double MR = Double.valueOf(args[3]); //Porcentaje de mutación (Mutation percentage)
        double CR = Double.valueOf(args[4]); // Porcentaje de cruza (Crossover percentahe)
        int iFitnessFunction = Integer.valueOf(args[5]); //Type of fitness functions - 1 Rechy (3 funciones), 8-EPAccuracy,EPComplexity,Infoloss. CHOOSE option 8
        int iAlgorithm = Integer.valueOf(args[6]); //Type of optimizer 1- Evolutionary Programming, 2 - Genetic Algorithm, 3 - NSGAII. CHOOSE option 3
        int iApproach = Integer.valueOf(args[7]); // Type of approach 0 - PEVOMO, 1 - Proposal. CHOOSE option 1.
        int iDS = Integer.valueOf(args[8]); //Este parámetro esta relacionado el número de base de datos que está definido en DataSets.java, puedes agregar un nuevo número, agregarlo ahí y pasarlo como parámetro en esta parte. (This option is related to the database ID to be executed, defined in DataSets.java. Here, it is possible to add a new dataset by modifying the DataSet.java and after putting the new number as in this parameter.)

        /*int PS = 100;
        int NE = 15;
        int NG = 300;
        double MR = 0.2;
        double CR = 0.8;
        int iFitnessFunction = 1; //3 (EC), 10 (EI), 11 (CI), 8 (ECI)
        int iAlgorithm = 1;
        int iApproach = 0;
        int iDS = 95;*/
        
        int TypeDataSet = 1; //0-toda, 1-train, 2-test
//        
        
        int opponents = (int) Math.floor(PS * 0.1);
        
        double[] weights = new double[4];
        weights[0] = 0.9;
        weights[1] = 0.09;
        weights[2] = 0.009;
        weights[3] = 0.0009;
        
//        IPopulation population = new MOPopulation(PS, false);
//        Generals general_params = new Generals(NE, iFitnessFunction, PS, iApproach, NG, opponents, false, weights, MR, 0, CR, iAlgorithm);
        Generals general_params = new Generals();
        general_params.setiFitnessFunctionConf(iFitnessFunction);
        
        if (general_params.getiFitnessFunctionConf()>0){
            
            general_params.setWeights(weights);
            general_params.setOpponents(opponents);
            general_params.setiAlgorithm(iAlgorithm);
            general_params.setTotalTime(System.currentTimeMillis());
            general_params.setnExecutions(NE);
            general_params.setiApproach(iApproach);
            general_params.setnGenerations(NG);
            general_params.setIsSelfAdaption(false);
            general_params.setMutationRate(MR);
            general_params.setCrossOverRate(CR);
            general_params.setTypeDataSet(TypeDataSet);
            
            switch(iAlgorithm){
                case 1:
                case 2:
                    Population population = new Population(PS, false);
                    general_params.setPopulation(population);
                    general_params.setAccumulatedFront(new Population(0, general_params.isIsSelfAdaption()));
                    break;
                case 3:
                    MOPopulation mopopulation = new MOPopulation(PS, false);
                    general_params.setPopulation(mopopulation);
                    general_params.setAccumulatedFront(new MOPopulation(0, general_params.isIsSelfAdaption()));
                    break;    

            }

            int d=iDS, fultimo=iDS;
            if(iDS==0){
                d=1;
                fultimo=DataSet.NUMBER_OF_DATASETS;
            }

            for(int m=d;m<=fultimo;m++){
                try {
                    DataSet ds = new DataSet(m, false);
                    System.out.println("Dataset: "+ds.getName() + ", Dimensions: ["+ds.getOriginal().getDimensions()[0]+","+ds.getOriginal().getDimensions()[1]+"]");

                    general_params.setAccumulatedFront(new MOPopulation(0, general_params.isIsSelfAdaption()));
                    for (int e=0; e<NE;e++){
                        Locals local_params = new Locals(ds, e, NG);
                        TimeSeriesDiscretize_source tsd = new TimeSeriesDiscretize_source(general_params, local_params);
                        tsd.Execute();
                    }

                    String scheme_type = general_params.population.getIndividuals()[0].getName();
                    String directory = scheme_type+"/"+ds.getName();
                    String FileName =  ds.getName()+"_"+scheme_type;
                    Export(directory, FileName, ds, general_params);
                    general_params.execution_front.Export(directory);
//                    if (general_params.getiAlgorithm() == 1)
//                        Results.GetDataEP(NE, m, "");

                } catch (MyException ex) {
                    Logger.getLogger(CommandRunning.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(CommandRunning.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static void Export(String directory, String FileName, DataSet ds, Generals general_params){
        try {
            MatlabExporter exporter = new MatlabExporter();
            general_params.CalculateStatistics();
            File FileDir = new File(directory);
            if(!FileDir.exists()) FileDir.mkdirs();

            if (general_params.getiAlgorithm() == 3){
                List<ParetoFront> fronts = NSGA2.FastNonDominatedSort(general_params.accumulatedFront);
//                double[] reference = {0,0,0};
                IScheme best = fronts.get(0).getBest(ds, false);
                exporter.add("AccumulatedFrontFitness", fronts.get(0).toFloatArray());
                fronts.get(0).Export(exporter);
                best.Export(ds,exporter);
            } else {
                IScheme best = general_params.accumulatedFront.getBestByErrorRate();
                best.Export(ds, exporter);
            }

            File FileTabla = new File(directory+"/"+FileName+".mat");
//            System.out.println(directory+"/"+FileName+".mat");
            general_params.setTotalTime(System.currentTimeMillis() - general_params.getTotalTime());
            exporter.add("errorRates", general_params.getMisclassification_rates());
//            exporter.add("statistics", general_params.getStatistics());
            float[][] time = new float[1][1];
            time[0][0] = general_params.getTotalTime();
            exporter.add("time", time);
            exporter.write(FileTabla);
        } catch (IOException ex) {
            Logger.getLogger(TimeSeriesDiscretize.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
