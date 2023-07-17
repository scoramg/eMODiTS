/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataSets;

import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.python.google.common.primitives.Doubles;

/**
 *
 * @author amarquezgr
 */
public class Data implements Cloneable{
    private double[][] data;
    private double[][] normalized;
    private int[] dimensions;
    private int noClasses;
    private List<Integer> classes; 
    private Map<Integer, Integer> NoInstancesPerClass;

    public double[][] getData() {
        return data;
    }
    
    public Double[][] getDataDouble() {
        Double[][] data = new Double[dimensions[0]][dimensions[1]];
        for(int i=0; i<dimensions[0];i++){
            for(int j=0; j<dimensions[1];j++){
                data[i][j] = Double.valueOf(this.data[i][j]);
            }
        }
        return data;
    }

    public double[][] getNormalized() {
        return normalized;
    }

    public Data getNormalizedData() {
        Data d = new Data();
        d.load(this.normalized);
        return d;
    }

    public int[] getDimensions() {
        return dimensions;
    }

    public void setData(double[][] datos) {
        this.data = datos.clone();
    }

    public void setDimensions(int[] dimensions) {
        this.dimensions = dimensions.clone();
    }
    
    public int getNoClasses() {
        return noClasses;
    }

    public List<Integer> getClasses() {
        return classes;
    }

    public Map<Integer, Integer> getNoInstancesPerClass() {
        return NoInstancesPerClass;
    }
    
    public void setNoClasses(int noClasses) {
        this.noClasses = noClasses;
    }

    public void setClasses(List<Integer> classes) {
        this.classes = new ArrayList<>();
        for (Integer item : classes) this.classes.add(item);
    }
    
    public void setClassesInfo() {
        classes = new ArrayList<>();
        NoInstancesPerClass = new HashMap<>();
        for(int i=0;i<this.getDimensions()[0];i++){
            Integer klass = (int) this.getData()[i][0];
            if (!this.classes.contains(klass)){
                this.classes.add(klass);
            }
            if (!this.NoInstancesPerClass.containsKey(klass)){
                this.NoInstancesPerClass.put(klass, 1);
            } else {
                int count = this.NoInstancesPerClass.get(klass);
                count++;
                this.NoInstancesPerClass.put(klass, count);
            }
        }
        Collections.sort(classes);
        this.noClasses = this.classes.size();
        
    }

    public Data() {
        this.dimensions = new int[2];
        this.data = new double[1][1];
    }

    public void load(double[][] datos){
        this.dimensions[0] = datos.length;
        this.dimensions[1] = datos[0].length;
        this.data = datos.clone();
        this.normalized = new double[this.dimensions[0]][this.dimensions[1]];
//        this.normalized = Utils.Utils.Normalize(data);
        for (int i=0; i<data.length;i++){
            double[] norm = Utils.Utils.Normalize(Arrays.copyOfRange(data[i], 1, data[i].length-1));
            normalized[i][0] = data[i][0];
            System.arraycopy(norm, 0, normalized[i], 1, norm.length);
        }
        this.setClassesInfo();
    }
    
    public void load(MLArray filecontent){
        this.dimensions = filecontent.getDimensions();
        this.data = ((MLDouble) filecontent).getArray();
        this.normalized = new double[this.dimensions[0]][this.dimensions[1]];
//        this.normalized = Utils.Utils.Normalize(data);
        for (int i=0; i<data.length;i++){
            double[] norm = Utils.Utils.Normalize(Arrays.copyOfRange(data[i], 1, data[i].length-1));
            normalized[i][0] = data[i][0];
            System.arraycopy(norm, 0, normalized[i], 1, norm.length);
        }
        this.setClassesInfo();
    }
    
    public int getIndexOfClass(int Class){
        return this.classes.indexOf(Class);
    }

    public List<Double> getValuesFrom(int instance, int begin, int end){
        return Doubles.asList(Arrays.copyOfRange(this.data[instance], begin, end));

        
    }
    public List<Double> getValuesFrom(int begin, int end){
        List<Double> values = new ArrayList<>();
        for(int i=0; i<this.dimensions[0];i++){
            values.addAll(getValuesFrom(i, begin, end));
        }
        return values;
    }
    
    public Map<Integer, Integer> getCountClassFrom(int[] WordIntervals, double[] AlphabetIntervals){
        Map<Integer, Integer> countKlass = new HashMap<>();
        for(int instance = 1; instance < getDimensions()[0]; instance++){
            for(int i=WordIntervals[0];i<=WordIntervals[1];i++){
                if(this.data[instance][i] >= AlphabetIntervals[0] && this.data[instance][i] <= AlphabetIntervals[1]){
                    int klass = (int) this.data[instance][0];
                    if (!countKlass.containsKey(klass)){
                        countKlass.put(klass, 1);
                    } else {
                        int count = countKlass.get(klass);
                        count++;
                        countKlass.put(klass, count);
                    }
                }
            }
        }
        return countKlass;
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
    
    public double[] getValuesNorm(int instance, int begin, int end){
        double[] values = new double[end-begin];
        int j=0;
        for(int i=begin;i<end;i++){
            values[j] = this.normalized[instance][i];
            j++;
        }

        return values;
    }

    public double getValue(int instance, int attribute){
        return this.data[instance][attribute];
    }

    public double[] getMedia(boolean isNormalized){
        double[] media = new double[getDimensions()[1]-1];

//        System.out.println("getDimensions()[1]:"+getDimensions()[1]);

        for(int i=0; i<getDimensions()[1]-1;i++){
            List<Double> data = new ArrayList<>();
            for(int j=0;j<getDimensions()[0];j++){
                if(isNormalized){
                    data.add(this.normalized[j][i+1]);
                } else {
                    data.add(this.data[j][i+1]);
                }
            }
            media[i] = mimath.MiMath.getMedia(data);
        }
        return media;
    }

    public double[][] getMediaPerClass(List<Integer> classes){
        double[][] media = new double[classes.size()][getDimensions()[1]-1];
        for (int c=0; c<classes.size(); c++){
            int con = 0; 
            for(int j=0;j<getDimensions()[0];j++){
                if(data[j][0] == classes.get(c)){
                    for(int i=1; i<getDimensions()[1];i++){
                        media[c][i-1] += this.data[j][i];
                        con++;
                    }
                }
            }
            for(int k=0;k<media[c].length;k++){
                media[c][k] /= (double) con;
            }
        }
        return media;
    }

    public double[] getMediana(){
        double[] mediana = new double[getDimensions()[1]-1];

        for(int i=0; i<getDimensions()[1]-1;i++){
            List<Double> data = new ArrayList<>();
            for(int j=0;j<getDimensions()[0];j++){
                data.add(this.data[j][i+1]);
            }
            mediana[i] = mimath.MiMath.getMediana(data);
        }
        return mediana;
    }
    
    public void add(int instance, int attribute, double value){
        data[instance][attribute] = value;
    }
    
    public List<Double[]> getInstancesFromKlass(int klass, int klassindex){
        List<Double[]> instances = new ArrayList<>();
        for(int i=0; i<this.data.length;i++){
            if(data[i][klassindex]==klass){
//                Double[] datos = new Double[data[i].length];
//                for(int j=0;j<data[i].length;j++){
//                    datos[j] = Double.valueOf(data[i][j]);
//                }
                instances.add(Utils.Utils.doubleArray2DoubleArray(data[i]));
            }
        }
        return instances;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<this.dimensions[0];i++){
            for(int j=0;j<this.dimensions[1];j++){
                sb.append(data[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    @Override
    public Data clone() {
        try {
            super.clone();
            Data clon = new Data();
            clon.setData(data);
            clon.setDimensions(dimensions);
            clon.setClasses(this.classes);
            clon.setNoClasses(this.noClasses);
            for(Integer klass: this.NoInstancesPerClass.keySet()){
                clon.getNoInstancesPerClass().put(klass, this.NoInstancesPerClass.get(klass));
            }
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void destroy(){
        data = null;
        normalized = null;
        dimensions = null;
        noClasses = 0;
        classes = null;  
        NoInstancesPerClass = null;
    }
    
}
