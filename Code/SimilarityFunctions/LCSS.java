/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimilarityFunctions;

/**
 *
 * @author amarquezgr
 */
public class LCSS {
    private double epsilon, delta;

    public LCSS(double epsilon, double delta) {
        this.epsilon = epsilon;
        this.delta = delta;
    }
    
    public double[] Head(double[] ts){
        return java.util.Arrays.copyOfRange(ts, 0, ts.length-1);
    }
    
    public double measure(double[] ts1, double[] ts2){
        if (ts1.length == 0 || ts2.length == 0){
//            System.out.println("1");
            return 0.0;
        }
        
        if((Math.abs(ts1[ts1.length-1] - ts2[ts2.length-1]) < this.epsilon) && (Math.abs(ts1.length - ts2.length) <= this.delta)){
//            System.out.println("2");
            return 1 + this.measure(Head(ts1), Head(ts2));
        }
//        System.out.println("3");
        return Math.max(this.measure(Head(ts1), ts2), this.measure(ts1, Head(ts2)));
    }
    
    public double similarity(double[] ts1, double[] ts2){
        return (double) this.measure(ts1, ts2) / Math.min(ts1.length, ts2.length);
    }
    
    public static void main(String[] args) {
        double[] A = {0.24, 1,267, 9.434, 0.6345};
        double[] B = {3.24, 1.34, 7.345, 1.645};
        
        double epsilon = Math.min(mimath.MiMath.getDesviacionStandar(A), mimath.MiMath.getDesviacionStandar(B));
        double delta = Math.ceil(Math.max(A.length, B.length)*0.2);
                
        LCSS lcss = new LCSS(epsilon, delta);
        System.out.println(lcss.similarity(A, B));
    }
}
