/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL;

import DataSets.DiscretizedData;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aldo.Marquez
 */
public class DiscreteHistogramCollection implements Cloneable {
    private List<DiscreteHistogram> data;
    private TreeSet<Integer> classes;
    private int instances;

    public List<DiscreteHistogram> getData() {
        return data;
    }

    public TreeSet<Integer> getClasses() {
        return classes;
    }

    public int getInstances() {
        return instances;
    }

    public void setData(List<DiscreteHistogram> data) {
        this.data = data;
    }

    public void setClasses(TreeSet<Integer> classes) {
        this.classes = classes;
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }
    
    public void addClass(int klass){
        this.classes.add(klass);
    }

    public void addDiscreteHistogram(DiscreteHistogram histogram){
        data.add(histogram);
        this.addClass(histogram.getKlass());
        this.instances++;
    }

    public DiscreteHistogramCollection() {
        this.data = new ArrayList<>();
        this.classes = new TreeSet<>();
        this.instances=0;
    }
    
    public List<OneDimensionHistogram> toOneDimensionalDataSet(){
        List<OneDimensionHistogram> res = new ArrayList<>();
        for (DiscreteHistogram dh: this.data){
            res.add(dh.toOneDimensionalArray());
        }
        return res;
    }
    
    public float[][] toFloatArray(){
        int attributes=0;
        int inst = this.data.size();
        List<OneDimensionHistogram> ds = this.toOneDimensionalDataSet();
        
        for (OneDimensionHistogram dh: ds){
            if(dh.getData().size()>attributes){
                attributes = dh.getData().size();
            }
        }
        float[][] fds = new float[inst][attributes+1];
        
        for(int i=0; i<inst; i++){
            for(int j=0;j<attributes;j++){
                fds[i][j] = Float.NaN;
            }
        }
        
//        DiscretizedData dd = new DiscretizedData(inst, attributes+1);
        for(int i=0; i<ds.size(); i++){
            fds[i][0] = ds.get(i).getKlass();
            int b=1;
            for(int a=0;a<ds.get(i).getData().size();a++){
                fds[i][b] = ds.get(i).getData().get(a).floatValue();
                b++;
            }
        }
        
        return fds;
    }
    
    public DiscretizedData toDiscreteDataSet(){
        int atributes=0;
        int inst = this.data.size();
        List<OneDimensionHistogram> ds = this.toOneDimensionalDataSet();
        
        for (OneDimensionHistogram dh: ds){
            if(dh.getData().size()>atributes){
                atributes = dh.getData().size();
            }
        }
        DiscretizedData dd = new DiscretizedData(inst, atributes+1);
        for(int i=0; i<ds.size(); i++){
            dd.addValue(i, 0, ds.get(i).getKlass(), true);
            for(int a=0;a<ds.get(i).getData().size();a++){
                dd.addValue(i, a+1, ds.get(i).getData().get(a));
            }
        }
        
        return dd;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (DiscreteHistogram hist: this.data){
            sb.append(hist.toString());
        }
        return sb.toString();
    }
    
    public void destroy(){
        this.classes = null;
        this.data = null;
        this.instances = 0;
    }
    
    @Override
    public DiscreteHistogramCollection clone(){
         
        try {
            super.clone();
            DiscreteHistogramCollection clon = new DiscreteHistogramCollection();
            clon.setClasses(classes);
            clon.setData(data);
            clon.setInstances(instances);
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DiscreteHistogramCollection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
