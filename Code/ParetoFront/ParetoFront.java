/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParetoFront;

import DataSets.DataSet;
import Interfaces.IScheme;
import ca.nengo.io.MatlabExporter;
import comparators.ComparatorByErrorRates;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//import parameters.Locals;

/**
 *
 * @author amarquezgr
 */
public final class ParetoFront {
    private List<IScheme> Front;

    public ParetoFront() {
        this.Front = new ArrayList<>();
    }

    public void setFront(List<IScheme> front) {
        this.Front = front;
    }

    public List<IScheme> getFront() {
        return Front;
    }

    public void addData(IScheme data){
        this.Front.add(data.clone());
    }
    
    //f1 dominates f2
    public static int dominates(double[] f1, double[] f2, int NoFunctions){
        int dominate1 ; // dominate1 indicates if some objective of solution1 
                    // dominates the same objective in solution2. dominate2
        int dominate2 ; // is the complementary of dominate1.

        dominate1 = 0 ; 
        dominate2 = 0 ;
        
        int flag; //stores the result of the comparison
        
        for (int i = 0; i < NoFunctions; i++) {
            if (f1[i] < f2[i]) {
                flag = -1;
            } else if (f1[i] > f2[i]) {
                flag = 1;
            } else {
                flag = 0;
            }

            if (flag == -1) {
                dominate1 = 1;
            }

            if (flag == 1) {
                dominate2 = 1;           
            }
        }

        if (dominate1 == dominate2) {            
            return 0; //No one dominate the other
        }
        if (dominate1 == 1) {
            return -1; // solution1 dominate
        }
        return 1;    // solution2 dominate  
        
        
//        ******* Este era el mio
//        int cont_menores = 0, cont_iguales = 0;
//        for(int i=0;i<NoFunctions;i++){
//            if (f1[i] < f2[i]){
//                cont_menores++;
//            } 
//            if (f1[i] == f2[i]){
//                cont_iguales++;
//            }
//        }
//        if (cont_menores == NoFunctions){
//            return true;
//        }
//        else {
//            return cont_menores>0 && cont_iguales>0;
//        }
        
    }
    
    public static double[] EvaluateWithEps(double[] funs, double[] epsilon, int NoFunctions){
        double[] res = new double[funs.length];
        for(int i=0;i<NoFunctions;i++){
            res[i] = Math.floor((double) funs[i]/epsilon[i]);
        }
        return res;
    }
    
    public static boolean IsNotComparableWith(double[] f1, double[] f2, int NoFunctions){
//        boolean b1= dominates(f1, f2, NoFunctions);
//        boolean b2= dominates(f2, f1, NoFunctions);
//        return (b1==b2);
        int dom = dominates(f1, f2, NoFunctions);
        return dom==0;
    }
    
    public static double getFunctionsDistance(double[] f1, double[] f2){
        double sum = 0.0;
        for(int i=0;i<f1.length;i++){
            sum+=Math.pow(f1[i]-f2[i], 2);
        }
        return Math.sqrt(sum);
    }
    
    public static int edominates(double[] f1, double[] f2, double[] epsilon, int NoFunctions){
        double value1, value2;
        int dominate1 ; // dominate1 indicates if some objective of solution1 
                    // dominates the same objective in solution2. dominate2
        int dominate2 ; // is the complementary of dominate1.

        dominate1 = 0 ; 
        dominate2 = 0 ; 
        int flag;
        // Idem number of violated constraint. Apply a dominance Test
        for (int i = 0; i < NoFunctions; i++) {
          value1 = f1[i];
          value2 = f2[i];

          //Objetive implements comparable!!! 
          if (value1/(1 + epsilon[i]) < value2) {
            flag = -1;
          } else if (value1/(1 + epsilon[i]) > value2) {
            flag = 1;
          } else {
            flag =  0;
          }

          if (flag == -1) {
            dominate1 = 1;
          }

          if (flag == 1) {
            dominate2 = 1;
          }
        }

        if (dominate1 == dominate2) {
          return 0; // No one dominates the other
        }

        if (dominate1 == 1) {
          return -1; // solution1 dominates
        }

        return 1;    // solution2 dominates
        
//        Este era el mio
        
//        double[] BoxX = EvaluateWithEps(f1, epsilon, NoFunctions);
//        double[] BoxY = EvaluateWithEps(f2, epsilon, NoFunctions);
//        
//        
//        if(Arrays.equals(BoxX, BoxY)){
//            if(IsNotComparableWith(f1, f2, NoFunctions)){
//                if(getFunctionsDistance(f1,BoxX) < getFunctionsDistance(f2,BoxX)){
//                    return true;
//                }
//                else {
//                    return false;
//                }
//            } else {
//                if(dominates(f1,f2,NoFunctions)==-1){
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        } else {
//            if(dominates(BoxX,BoxY,NoFunctions)==-1) {
//                return true;
//            }
//            else{
//                return false;
//            }
//        }
    }
    
    public boolean isEmpty(){
        return this.Front.isEmpty();
    }
    
    public int size(){
        return this.Front.size();
    }
    
    public void classify(DataSet ds, boolean UsingTest){
        for(int i=0;i<this.Front.size();i++){
            Front.get(i).Classify(ds, UsingTest, "train");
        }
    }
    
    public IScheme getBest(DataSet ds, boolean UsingTest){
        classify(ds,UsingTest);
        Collections.sort(Front,new ComparatorByErrorRates());
//        System.out.println(PrintErrorRates());
        return Front.get(0);
    }
    
    public IScheme getKnee(double[] reference){
        double min = Double.POSITIVE_INFINITY;
        int ind = 0;
        for (int i=0;i<Front.size();i++){
            double d = mimath.MiMath.getEuclideanDist(Front.get(i).getEvaluatedValues(), reference);
            if (d<min){
                min = d;
                ind = i;
            }
        }
        return Front.get(ind);
    }
    
    public float[][] toFloatArray(){
        float[][] array = new float[Front.size()][Front.get(0).getFitnessFunction().getNoFunctions()];
        for (int i=0;i<Front.size();i++){
            for(int f=0;f<Front.get(i).getFitnessFunction().getNoFunctions();f++){
                array[i][f] = (float) Front.get(i).getEvaluatedValues()[f];
            }
        }
        return array;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Front Size:").append(this.Front.size()).append("\n");
        sb.append("Data:").append("\n");
        for(IScheme a:this.Front){
            sb.append(a.toString()).append("\n");
        }
        return sb.toString();
    }
    
    public String PrintErrorRates(){
        StringBuilder sb = new StringBuilder();
        sb.append("ErrorRates:");
        for(int i=0;i<this.Front.size();i++){
            sb.append(Front.get(i).getErrorRate()).append(" ");
        }
        return sb.toString();
    }
    
    public void Export(MatlabExporter exporter) {
        for(int i=0;i<this.Front.size();i++){
            float[][] data = this.Front.get(i).Elements2FloatArray();
            exporter.add("FrontIndividual"+i, data);
            float[][] DataFunctionValues = new float[1][this.Front.get(i).getEvaluatedValues().length];
            for(int j=0;j<this.Front.get(i).getEvaluatedValues().length;j++){
                DataFunctionValues[0][j] = (float) this.Front.get(i).getFitnessFunction().getEvaluatedValues()[j];
            }
            exporter.add("EvaluatedValues"+i,DataFunctionValues);
        }
    }
    
}
