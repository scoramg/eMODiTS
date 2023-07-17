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
import mimath.MiMath;

/**
 *
 * @author amarquezgr
 */
public class Histogram implements Cloneable{
    private double[][] histogram;
    private int[] dimensions; //Revisar si va aqui y que funcion tiene
    private int klass;

    public double[][] getHistogram() {
        return histogram;
    }

    public int getKlass() {
        return klass;
    }

    public int[] getDimensions() {
        return dimensions;
    }
    
    public void setHistogram(double[][] histogram) {
        this.histogram = histogram.clone();
//        System.out.println("histogram.length:"+histogram.length+", histogram[0].length:"+histogram[0].length);
        this.dimensions[0] = histogram.length;
        this.dimensions[1] = histogram[0].length;
    }

    public void setKlass(int klass) {
        this.klass = klass;
    }

    public void setDimensions(int[] dimensions) {
        this.dimensions = dimensions;
    }

    public Histogram() {
        this.dimensions = new int[2];
    }
    
    @Override
    protected Histogram clone() throws CloneNotSupportedException {
        try {
            super.clone();
            Histogram hist = new Histogram();
            hist.setDimensions(this.dimensions.clone());
            hist.setKlass(this.klass);
            hist.setHistogram(this.histogram.clone());
            return hist;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Histogram.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public List<Double> getColumn(int index){
        List<Double> column = new ArrayList<>(); // Here I assume a rectangular 2D array! 
        
//        RealMatrix matrix = MatrixUtils.createRealMatrix(this.histogram);
//        column = Arrays.stream(matrix.getColumn(index)).boxed().collect(Collectors.toList());
        for(int i=0; i<this.histogram.length; i++){
           column.add(this.histogram[i][index]);
        }
        return column;
    }
    
//    public List<Double> getValuesFrom(int col_begin, int col_end, int row_begin, int row_end){
//        List<Double> values = new ArrayList<>();
////        Histogram hist = this.data.get(instance);
//        for(int i=row_begin; i<row_end; i++){
//            for(int j=col_begin; j<col_end; j++){
//                values.add(this.getHistogram()[i][j]);
//            }
//        }
//        
//        return values;
//    }
//    
//    public DiscreteHistogram discretize(HistogramScheme hs){
//        DiscreteHistogram dh = new DiscreteHistogram();
//        int[] intervals = new int[2];
//        int[] heightinterval = new int[2];
//        intervals[0] = 0;
//        for (HistogramWidthCut wc: hs.getElements()){
//            intervals[1] = wc.getCut();
//            heightinterval[0] = 0;
//            DiscreteHistogramRow dhr = new DiscreteHistogramRow();
//            for(Integer val: wc.getHeight_cuts().getCuts()){
//                heightinterval[1] = val;
//                double media = MiMath.getMedia(this.getValuesFrom(intervals[0], intervals[1], heightinterval[0], heightinterval[1]));
//                dhr.addValue(media);
//                heightinterval[0] = val;
//            }
//            dh.addRow(dhr);
//            intervals[0] = wc.getCut();
//        }
//        dh.setKlass(this.getKlass());
//        return dh;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(this.klass).append("\n");
        sb.append("Dimensions[0]: ").append(this.dimensions[0]).append(", Dimensions[1]: ").append(this.dimensions[1]).append("\n");
        for(int i=0; i<this.dimensions[0];i++){
            for(int j=0;j<this.dimensions[1];j++){
                sb.append(this.histogram[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public void destroy(){
        this.dimensions = null;
        this.histogram = null;
        this.klass = 0;
    }
    
}
