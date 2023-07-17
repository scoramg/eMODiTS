/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParetoFront;

/**
 *
 * @author amarquezgr
 */
public class ReferencePoint {
    private double[] point;
    private int size;

    public double[] getPoint() {
        return point;
    }

    public int getSize() {
        return size;
    }

    public void setPoint(double[] point) {
        this.point = point;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ReferencePoint() {
        this.size = 0;
        this.point = new double[this.size];
    }

    public ReferencePoint(int pointSize) {
        this.size = pointSize;
        this.point = new double[pointSize];
    }

    public ReferencePoint(double[] point, int size) {
        this.point = point;
        this.size = size;
    }

    public ReferencePoint(double[] point) {
        this.point = point;
    }
     
    public void getKneePoint(){
        for (int i=0;i<this.point.length;i++){
            this.point[i] = 0;
        }
//        this.point[0] = 1;
//        this.point[1] = 1;
//        this.point[2] = 0.1;
    }
    
    public void getMeanPoint(double[][] data){
        double[] sums = new double[this.size];
        
        for(int s=0;s<this.size;s++) sums[s]=0;
            
        for (int i=0;i<data.length;i++){
            for(int j=0;j<data[i].length;j++){
                sums[j] += data[i][j];
            }
        }
        
        for(int s=0;s<this.size;s++){
            this.point[s] = (double) sums[s] / (double) data.length;
        }
    }
    
    public void getCentroidPoint(double[][] data){
        double[][] means = new double[data.length][this.size];
        
        for(int f=0;f<this.size;f++) for(int s=0;s<this.size;s++) means[f][s]=0;
        
        double[] minimums = new double[this.size];
        int[] indminimums = new int[this.size];
        
        for(int s=0;s<this.size;s++) minimums[s]=Double.POSITIVE_INFINITY;
        for(int s=0;s<this.size;s++) indminimums[s]=-1;
            
        for (int i=0;i<data.length;i++){
            for(int j=0;j<data[i].length;j++){
                if (data[i][j] < minimums[j]){
                    minimums[j] = data[i][j];
                    indminimums[j] = i;
                }
            }
        }
        
//        for(int s=0;s<this.size;s++){
//            this.point[s] = (double) sums[s] / (double) data.length;
//        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reference Point: [ ");
        for (int i=0;i<this.size;i++){
            sb.append(this.point[i]).append(" ");
        }
        return sb.toString();
    }
    
    
}
