/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FitnessFunctions.ComplexityMeasures;

import DataSets.Data;
import FitnessFunctions.ConfusionMatrix;

/**
 *
 * @author amarquezgr
 */
public class _ComplexityMeasures {
    public static double RechyComplexity(ConfusionMatrix MC, Data ds){
        double complexity = 0;
//        System.out.println("MC.getDimensions()[0]:"+MC.getDimensions()[0]+", ds.getNoClasses():"+ds.getNoClasses()+", MC.getDimensions()[0] - ds.getNoClasses()="+(MC.getDimensions()[0] - ds.getNoClasses()));
        if((MC.getDimensions()[0] - ds.getNoClasses()) < 0){
            complexity = ds.getDimensions()[0] - (MC.getDimensions()[0] - ds.getNoClasses());
        } else {
//            System.out.println("this.MC.getDimensions()[0] - ds.getNoClasses()="+(this.MC.getDimensions()[0] - ds.getNoClasses()));
            complexity = MC.getDimensions()[0] - ds.getNoClasses();
        }
//        System.out.println("ds.getDimensions()[0]:"+ds.getDimensions()[0]+", (ds.getDimensions()[0] + ds.getNoClasses() - 1):"+(ds.getDimensions()[0] + ds.getNoClasses() - 1));
        double res = (double) complexity / (ds.getDimensions()[0] + ds.getNoClasses() - 1);
//        System.out.println("Complexity="+res);
        return res;
    }
    
    public static double PermutationEntropy(ConfusionMatrix MC, Data ds){
        double complexity = 0.0;
        for (int c=0;c<ds.getNoClasses();c++){
            int count = 0;
            int suma = 0;
            int n = 0;
            for(String pal: MC.getConfusionmatrix().keySet()){
                n = pal.length();
                Integer[] countclass = MC.getConfusionmatrix().get(pal);
                if(countclass[c]>0) {
                    count++;
                    suma+=countclass[c];
                }
            }
            System.out.println("n:"+n);
            double p = (double) count/(suma+1);//(ds.getDimensions()[1] - n + 1);
            complexity += - p * Math.log(p);
        }
        
        return complexity;
    }
}
