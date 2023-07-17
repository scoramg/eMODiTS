/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataSets;

import Individuals.PEVOMO.Word;
import Interfaces.IScheme;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class ReconstructedData implements Cloneable{
    private double[][] data;
    private int[] dimensions; 

    public double[][] getData() {
        return data;
    }

    
    public int[] getDimensions() {
        return dimensions;
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    
    public void setDimensions(int[] dimensions) {
        this.dimensions = dimensions;
    }
    
    public void addRow(int instance, double[] d){
        this.data[instance] = d.clone();
    }

    public ReconstructedData() {
    }
    
    public ReconstructedData(int instances, int attributes) {
        this.data = new double[instances][attributes];
        dimensions = new int[2];
        dimensions[0] = instances;
        dimensions[1] = attributes;
    }
    
    public double[] getValues(int instance, int begin, int end){
        double[] values = new double[end-begin];
        int j=0;
        for(int i=begin;i<end;i++){
            values[j] = this.data[instance][i];
            j++;
        }

        return values;
    }
    
    public double[] getMedia(){
        double[] media = new double[getDimensions()[1]-1];

//        System.out.println("getDimensions()[1]:"+getDimensions()[1]);

        for(int i=0; i<getDimensions()[1]-1;i++){
            List<Double> data = new ArrayList<>();
            for(int j=0;j<getDimensions()[0];j++){
                data.add(this.data[j][i+1]);
                
            }
            media[i] = mimath.MiMath.getMedia(data);
        }
        return media;
    }

    @Override
    protected Object clone() {
        try {
            super.clone();
            ReconstructedData clon = new ReconstructedData();
            clon.setData(this.data.clone());
            clon.setDimensions(this.dimensions.clone());
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DiscretizedDataSet.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.dimensions[0];i++){
            for(int j=0;j<this.dimensions[1];j++){
                sb.append(this.data[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
