/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FitnessFunctions;

import DataSets.Data;
import DataSets.DiscretizedData;
import DataSets.DiscretizedDataSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class ConfusionMatrix implements Cloneable {
    private Map<String,Integer[]> confusionmatrix;
//    private Integer[] subsum;
    private Map<String,Integer> subsum;
    private Integer[] dimensions;
    private int sum;
    private Map<Integer, Integer> NoInstancesPerClass;
    private Map<Integer, Integer> SubKlass;

    public Map<String, Integer[]> getConfusionmatrix() {
        return confusionmatrix;
    }

    public Integer[] getDimensions() {
        return dimensions;
    }

    public Map<String,Integer> getSubsum() {
        return subsum;
    }

    public int getSum() {
        return sum;
    }

    public Map<Integer, Integer> getNoInstancesPerClass() {
        return NoInstancesPerClass;
    }

    public Map<Integer, Integer> getSubKlass() {
        return SubKlass;
    }
    
    public void setConfusionmatrix(Map<String, Integer[]> confusionmatrix) {
        this.confusionmatrix = confusionmatrix;
    }

    public void setDimensions(Integer[] dimensions) {
        this.dimensions = dimensions;
    }

    public void setSubsum(Map<String,Integer> subsum) {
        this.subsum = subsum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
    
    public void add(int indexClass, int noClasses, String value){
        Integer[] countclass = new Integer[noClasses];
        for (int i = 0; i<noClasses; i++){
            if(indexClass == i){
                countclass[i] = 1;
            } else {
                countclass[i] = 0;
            }
        }
        this.confusionmatrix.put(value, countclass);
    }
    
    public void update(int indexClass, String value){
        Integer[] countclass = this.confusionmatrix.get(value);
        countclass[indexClass]++;
        this.confusionmatrix.put(value, countclass);
    }
    
    public void build(Data ds, DiscretizedData ds_dis){
        int size = 0;
        for(int i=0; i<ds.getDimensions()[0]; i++){
            int indexClass = ds.getIndexOfClass(Integer.valueOf(ds_dis.getSds_discretized()[i][0]));
            if (!this.confusionmatrix.containsKey(ds_dis.getSds_discretized()[i][1])){
                this.add(indexClass, ds.getNoClasses(), ds_dis.getSds_discretized()[i][1]);
                size++;
            } else {
                this.update(indexClass, ds_dis.getSds_discretized()[i][1]);
            }
        }
        
        this.dimensions[0] = size;
        
        this.subsum = new HashMap<>(); //Integer[size];
        for(String pal: this.confusionmatrix.keySet()){
            Integer[] countclass = this.confusionmatrix.get(pal);
            int suma = 0;
//            for (Integer countclas : countclass) {
            for (int i=0; i<countclass.length; i++){
//                suma += countclas;
                suma += countclass[i];
                if (countclass[i] > 0){
                    int klass = ds.getClasses().get(i);
                    if(!this.NoInstancesPerClass.containsKey(klass)){
                        this.NoInstancesPerClass.put(klass, 1);
                    } else {
                        int count = this.NoInstancesPerClass.get(klass);
                        count++;
                        this.NoInstancesPerClass.put(klass, count);
                    }
                    
                    if(!this.SubKlass.containsKey(klass)){
                        this.SubKlass.put(klass, countclass[i]);
                    } else {
                        int count = this.SubKlass.get(klass);
                        count+=countclass[i];
                        this.SubKlass.put(klass, count);
                    }
                }
            }
            this.subsum.put(pal, suma);
        }
    }

    public ConfusionMatrix() {
        this.confusionmatrix = new HashMap<>();
        this.NoInstancesPerClass = new HashMap<>();
        this.SubKlass = new HashMap<>();
        this.dimensions = new Integer[2];
        this.dimensions[0] = 0;
        this.dimensions[1] = 0;
    }
    
    public ConfusionMatrix(Data ds, DiscretizedData ds_dis) {
        this.confusionmatrix = new HashMap<>();
        this.NoInstancesPerClass = new HashMap<>();
        this.SubKlass = new HashMap<>();
        this.dimensions = new Integer[2];
        this.dimensions[0] = 0;
        this.dimensions[1] = ds.getNoClasses();
        this.build(ds, ds_dis);
        this.setSum();
    }
    
    public void setSum(){
        int sum = 0;
        for(String pal: this.confusionmatrix.keySet()){
            Integer[] countclass = this.confusionmatrix.get(pal);
            for(int i=0;i<countclass.length;i++){
                sum+=countclass[i];
            }
        }
        this.setSum(sum);
    }
    
    public void removeIndexs(List<Integer> indexs){
        int i = 0;
        ConfusionMatrix aux = new ConfusionMatrix();
        for(String pal: this.confusionmatrix.keySet()){
            if(!indexs.contains(i)){
                Integer[] countclass = this.confusionmatrix.get(pal);
                aux.confusionmatrix.put(pal, countclass);
            }
            i++;
        }
        this.setConfusionmatrix(aux.getConfusionmatrix());
        this.setSum();
        this.setDimensions();
    }
    
    public void setDimensions(){
        Integer[] dimensions = new Integer[2];
        dimensions[0] = this.confusionmatrix.size();
        dimensions[1] = this.dimensions[1];
        this.setDimensions(dimensions);
    }
    
    public double[][] getProbabilities(){
        
        double[][] probabilities = new double[getConfusionmatrix().size()][getDimensions()[1]];
        int i=0;
        Map<String,Integer> sum = getSubsum();
        
        for(String pal: getConfusionmatrix().keySet()){
            Integer[] count = getConfusionmatrix().get(pal);
            for(int j=0; j<count.length; j++){
                probabilities[i][j] = (double) count[j] / sum.get(pal);
            }
            i++;
        }
        
        return probabilities;
    }
    
    public double[][] getEntropyMatrix(double[][] probabilities){
        double[][] entropy = new double[getConfusionmatrix().size()][getDimensions()[1]];
        for(int i=0;i<getConfusionmatrix().size();i++){
            for(int j=0;j<getDimensions()[1];j++){
                double prob = probabilities[i][j] == 0 ? 1: probabilities[i][j];
                entropy[i][j] = prob * Math.log(1/prob);
            }
        }
        return entropy;
    }
    
    public double getSumEntropy(double[][] entropy){
        double sum = 0.0; 
        for(int i=0;i<getConfusionmatrix().size();i++){
            for(int j=0;j<getDimensions()[1];j++){
                sum+=entropy[i][j];
            }
        }
        return sum;
    }
    
    
    public List<Integer> getRowsEmpty(double[][] entropy){
        List<Integer> rows = new ArrayList<>();
        for(int i=0;i<getConfusionmatrix().size();i++){
            int zeros = 0;
            for(int j=0;j<getDimensions()[1];j++){
                if(entropy[i][j] == 0) zeros++;
            }
            if (zeros == getDimensions()[1]) rows.add(i);
        }
        return rows;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
//        int j = 0;
        for (Integer i: this.dimensions){
            System.out.println(i);
        }
        for(String pal: this.confusionmatrix.keySet()){
            sb.append(pal).append(" ");
            Integer[] countclass = this.confusionmatrix.get(pal);
            for(int i=0;i<countclass.length;i++){
                sb.append(countclass[i]).append(" ");
            }
            sb.append(" = ").append(this.subsum.get(pal));
            sb.append("\n");
//            j++;
        }
        
        for(Integer i: this.NoInstancesPerClass.keySet()){
           sb.append("Klass:").append(i).append(" = ").append(this.NoInstancesPerClass.get(i)).append("\n"); 
        }
        
        return sb.toString();
    }
    
    @Override
    protected ConfusionMatrix clone() {
        try {
            super.clone();
            ConfusionMatrix clon = new ConfusionMatrix();
            for(String pal: this.confusionmatrix.keySet()){
                clon.getConfusionmatrix().put(pal, this.confusionmatrix.get(pal));
            }
            
            for(Integer klass: this.NoInstancesPerClass.keySet()){
                clon.getNoInstancesPerClass().put(klass, this.NoInstancesPerClass.get(klass));
            }
            
            clon.setSum(this.getSum());
            clon.setDimensions(this.getDimensions());
            clon.setSubsum(this.getSubsum());
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ConfusionMatrix.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
