/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL;

import BeansCL.methods.*;
import BeansCL.parameters.Generals;
import BeansCL.parameters.Locals;
import Exceptions.MyException;
import ca.nengo.io.MatlabExporter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class ExecuteDiscretizationHistogram {
    
    Generals general_params;
    Locals local_params;
    
    public static void main(String[] args) throws MyException {
        int PS = 10;
        int NE = 1;
        int NG = 1;
        double MR = 0.2;
        double CR = 0.8;
        int iFitnessFunction = 9;
        int iDS = 90;
        int opponents = (int) Math.floor(PS * 0.1);
        
        int iAlgorithm = 1;
//        int iApproach = 1;
//        int TypeDataSet = 1; //0-toda, 1-train, 2-test
        
        double[] weights = new double[3];
        for (int i=0; i<3; i++){
            weights[i] = 1;        
        }
        
        Generals general_params = new Generals();
        general_params.setiFitnessFunctionConf(iFitnessFunction);
        
        if (general_params.getiFitnessFunctionConf()>0){
            
            general_params.setWeights(weights);
            general_params.setOpponents(opponents);
            general_params.setiAlgorithm(iAlgorithm);
            general_params.setTotalTime(System.currentTimeMillis());
            general_params.setnExecutions(NE);
//            general_params.setiApproach(iApproach);
            general_params.setnGenerations(NG);
            general_params.setIsSelfAdaption(false);
            general_params.setMutationRate(MR);
            general_params.setCrossOverRate(CR);
//            general_params.setTypeDataSet(TypeDataSet);
        
            Population population = new Population(PS, false);
            general_params.setPopulation(population);
        }
        
        HistogramDataSet hds = new HistogramDataSet(iDS);
        System.out.println("Dataset: "+hds.getName());

        general_params.setAccumulatedFront(new Population(0, general_params.isIsSelfAdaption()));
        for (int e=0; e<NE;e++){
            Locals local_params = new Locals(hds, e, NG);
            
            switch(general_params.getiAlgorithm()){
                case 1:
                    EP ep = new EP();
                    ep.Execute(general_params, local_params);
                    break;
//                case 2:
//                    runGA();
//                    break;
                case 3:
                    NSGA2 nsga2 = new NSGA2();
                    nsga2.Execute(general_params, local_params);
            }
                
        }

        String scheme_type = general_params.population.getIndividuals()[0].getName();
        String directory = scheme_type+"/"+hds.getName();
        String FileName =  hds.getName()+"_"+scheme_type;
        Export(directory, FileName, hds, general_params);
        
    }
    
    public static void Export(String directory, String FileName, HistogramDataSet ds, Generals general_params){
        try {
            MatlabExporter exporter = new MatlabExporter();
            general_params.CalculateStatistics();
            File FileDir = new File(directory);
            if(!FileDir.exists()) FileDir.mkdirs();

            if (general_params.getiAlgorithm() == 3){
                List<ParetoFront> fronts = NSGA2.FastNonDominatedSort(general_params.accumulatedFront);
//                double[] reference = {0,0,0};
                HistogramScheme best = fronts.get(0).getBest(ds, false);
                exporter.add("AccumulatedFrontFitness", fronts.get(0).toFloatArray());
                fronts.get(0).Export(exporter);
                best.Export(ds,exporter);
            } else {
                HistogramScheme best = general_params.accumulatedFront.getBestByErrorRate();
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
            Logger.getLogger(ExecuteDiscretizationHistogram.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
