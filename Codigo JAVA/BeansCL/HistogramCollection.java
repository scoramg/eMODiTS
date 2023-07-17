/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL;

import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author amarquezgr
 */
public class HistogramCollection {
    private List<Histogram> data;
    private TreeSet<Integer> classes;
    private int instances;

    public List<Histogram> getData() {
        return data;
    }

    public TreeSet<Integer> getClasses() {
        return classes;
    }

    public int getInstances() {
        return instances;
    }

    public int getNoClasses() {
        return classes.size();
    }

    public void setData(List<Histogram> data) {
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

    public void addHistogram(Histogram histogram){
        data.add(histogram);
        this.addClass(histogram.getKlass());
//        this.instances++;
    }

    public HistogramCollection() {
        this.data = new ArrayList<>();
        this.classes = new TreeSet<>();
        this.instances=0;
    }
    
    public void load(MLArray filecontent){
        this.instances = ((MLStructure) filecontent).getDimensions()[1];
        for (int i=0; i<this.instances;i++){
            Histogram hist = new Histogram();
//            print(((MLDouble) ((MLStructure) filecontent).getField("hist",i)).getArray().length)
            hist.setHistogram(((MLDouble) ((MLStructure) filecontent).getField("hist",i)).getArray());
            double[][] arr = ((MLDouble) ((MLStructure) filecontent).getField("class",i)).getArray();
            hist.setKlass((int) arr[0][0]);
            this.addHistogram(hist);
        }
    }    
    
    public List<Double> getValuesFrom(int instance, int col_begin, int col_end, int row_begin, int row_end){
        List<Double> values = new ArrayList<>();
        Histogram hist = this.data.get(instance);
        for(int i=row_begin; i<row_end; i++){
            for(int j=col_begin; j<col_end; j++){
                values.add(hist.getHistogram()[i][j]);
            }
        }
        
        return values;
    }
    
//    public DiscreteHistogramCollection Discretize(HistogramScheme hs){
//        DiscreteHistogramCollection dhc = new DiscreteHistogramCollection();
////        int[] intervals = new int[2];
////        int[] heightinterval = new int[2];
//        for (int i=0; i<this.getInstances(); i++){
//            dhc.addDiscreteHistogram(this.getData().get(i).discretize(hs));
//            dhc.addClass(this.getData().get(i).getKlass());
//        }
//        return dhc;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Histogram hist: this.data){
            sb.append(hist.toString());
        }
        return sb.toString();
    }
    
    public void destroy(){
        this.classes = null;
        this.data = null;
        this.instances = 0;
    }
}
