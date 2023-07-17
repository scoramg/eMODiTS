/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class DiscreteHistogram {
    private List<DiscreteHistogramRow> data;
    private int klass;

    public DiscreteHistogram() {
        data = new ArrayList<>();
    }

    public List<DiscreteHistogramRow> getData() {
        return data;
    }

    public int getKlass() {
        return klass;
    }

    public void setData(List<DiscreteHistogramRow> data) {
        this.data = new ArrayList(data);
    }

    public void setKlass(int klass) {
        this.klass = klass;
    }
    
    public void addValue(int i, double valor){
        this.data.get(i).addValue(valor);
    }
    
    public void addRow(){
        DiscreteHistogramRow dhw = new DiscreteHistogramRow();
        this.data.add(dhw);
    }
    
    public void addRow(DiscreteHistogramRow dhw){
        this.data.add(dhw);
    }
    
    public OneDimensionHistogram toOneDimensionalArray(){
        OneDimensionHistogram onedimension = new OneDimensionHistogram();
        onedimension.setKlass(this.klass);
        for(DiscreteHistogramRow dhr: this.data){
            onedimension.setData(dhr.getRow());
        }
        return onedimension;
    }
    
    @Override
    protected DiscreteHistogram clone() throws CloneNotSupportedException {
        try {
            super.clone();
            DiscreteHistogram hist = new DiscreteHistogram();
//            hist.setDimensions(this.dimensions.clone());
            hist.setKlass(this.klass);
            hist.setData(this.data);
            return hist;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Histogram.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(this.klass).append("\n");
//        sb.append("Dimensions[0]: ").append(this.dimensions[0]).append(", Dimensions[1]: ").append(this.dimensions[1]).append("\n");
        for(DiscreteHistogramRow h: this.data){
            sb.append(h.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public void destroy(){
//        this.dimensions = null;
        this.data = null;
        this.klass = 0;
    }
    
}
