/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeSeriesDiscretize;

import DataSets.DataSet;
import Exceptions.MyException;
import Interfaces.IPopulation;
import Populations.MOPopulation;
import Populations.Population;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import parameters.*;

/**
 *
 * @author amarquezgr
 */
public class iRaceTunning {
    public static void main(String[] args) {
        try {
            double[] weights = new double[3];
            
//            double conf = Integer.valueOf(args[0]);
//            double inst = Integer.valueOf(args[1]);
            double CR = Double.valueOf(args[2]);
            double MR = Double.valueOf(args[3]);
            int PS = Integer.valueOf(args[4]);
            int NG = Integer.valueOf(args[5]);
            int iDS = Integer.valueOf(args[6]);
//            weights[0] =  Double.valueOf(args[7]);
            
//            int conf =1, inst=3;
//            double CR = 0.8;
//            double MR = 0.2;
//            int PS = 10;
//            int NG = 2;
//            int iDS = 2;
//            weights[0] =  0.9;
//            weights[1] =  0.09;//mimath.MiMath.Redondear(mimath.MiMath.random(0, (1-weights[0])), 4);
//            weights[2] =  0.009;//mimath.MiMath.Redondear(1 - (weights[0]+weights[1]),4);
//            
//            String configuration = "-conf "+conf+" -i "+inst+" -cr "+CR+ " -mr "+MR+" -iDS "+iDS+" -PS "+PS+" -NG "+NG+" -alpha "+weights[0]+" -beta "+weights[1]+" -gamma "+weights[2];
//            
//            String filename = "configurations/Conf_"+conf+"_inst" +inst+"_"+mimath.MiMath.random(0, 1)+".txt";
//            
////            File FileDir = new File(filename);
////            if(FileDir.exists()) {
////                double subfix = mimath.MiMath.random(0, 1);
////                filename = filename +"_"+subfix;
////            }
//            
//            try(  PrintWriter out = new PrintWriter( filename )  ){
//                out.println( configuration );
//            } catch (FileNotFoundException ex) {
//                Logger.getLogger(iRaceTunning.class.getName()).log(Level.SEVERE, null, ex);
//            }
            
            int opponents = (int) Math.floor(PS * 0.1);
            int iAlgorithm = 3;
            int TypeDataSet = 1;
            
            IPopulation population;
            
            if (iAlgorithm == 3) population = new MOPopulation(PS, false);
            else population = new Population(PS, false);;
            
            DataSet ds = new DataSet(iDS, false);
            Generals general_params = new Generals(1, 1, PS, 1, NG, opponents, false, weights, MR, 0, CR, iAlgorithm, population,TypeDataSet);
            
            for (int e=0; e<1;e++){
                Locals local_params = new Locals(ds, e,general_params.getnGenerations());
                TimeSeriesDiscretize_source tsd = new TimeSeriesDiscretize_source(general_params, local_params);
                tsd.Execute();
            }
//            System.out.println("population:"+general_params.population.IndividualsFitness2String());
//            double resultado = general_params.getStatistics()[0][0];
            double resultado = general_params.population.getBest().getFitnessValue();
//            double mejorConocido = ds.getMinERFound();
            System.out.println(String.valueOf(resultado));
        } catch (MyException ex) {
            Logger.getLogger(iRaceTunning.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
}
