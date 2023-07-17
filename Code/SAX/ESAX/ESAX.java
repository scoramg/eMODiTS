/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SAX.ESAX;

import DataSets.Data;
import DataSets.DataSet;
import DataSets.DiscretizedData;
import DataSets.ReconstructedData;
import Exceptions.MyException;
import Graphs.Marker;
import Graphs.Plotter;
import SAX.SAX;
import SAX.SAXException;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author amarquezgr
 */
public class ESAX extends SAX {

    public ESAX() {
        super();
    }

    public ESAX(int wordSize, int alphabetSize) {
        super(wordSize, alphabetSize);
    }

    public ESAX(DataSet ds, int wordSize, int alphabetSize) {
        super(ds, wordSize, alphabetSize);
    }
    
    public  Coeficients[] PAA(double[] ts) throws SAXException {
        // fix the length
        int len = ts.length;

        if (len < this.getWordSize()) {
            throw new SAXException("PAA size can't be greater than the timeseries size.");
        }
        // check for the trivial case
//        if (len == this.getWordSize()) {
//            Coeficients[] paa = new Coeficients[ts.length];
//            for (int i=0; i<ts.length; i++){
//                paa[i] = new Coeficients(ts[i]);
//            }
//            return paa;
//        }
        else {
            Coeficients[] paa = new Coeficients[this.getWordSize()];
            int segStart = 0; //***
            int segEnd = 0; //****
            for (int i = 0; i < this.getWordSize(); i++) {
                segEnd = this.getWordSegments()[i];

                double[] segment = Arrays.copyOfRange(ts, segStart, segEnd);

                paa[i] = new Coeficients(segment, this.getAlphabetCuts());
                segStart = this.getWordSegments()[i];
            }
            return paa;
        }
    }
    
    @Override
    public int[] ts2Index(double[] ts) throws SAXException {
//        Coeficients[] series = PAA(znorm(ts, nThreshold));
        Coeficients[] series = PAA(ts);
        int[] res = new int[series.length * 3];
        int r=0;
        for (int i = 0; i < series.length; i++) {
//            res[i] = num2index(series[i], this.getAlphabetCuts());
            int[] index = series[i].toIndexOrdered();
            for(int j=0; j<index.length;j++) {
                res[r] = index[j];
                r++;
            }
        }
        return res;
    }
    
    @Override
    public DiscretizedData Discretize(Data ds) throws SAXException{
        int[] dimensions = ds.getDimensions();
        int instances = dimensions[0];
        getWordCuts(getWordBreaks(ds.getDimensions()[1]-1));
        adjust(ds);
        DiscretizedData ds_dis = new DiscretizedData(instances, (this.getWordSize()*3) + 1);
        for(int i=0;i<instances;i++){
            ds_dis.addValue(i,0, (int) ds.getValue(i, 0), true);
            double[] ts = znorm(ds.getValues(i, 1, ds.getDimensions()[1]),0.01);
            int[] ints = this.ts2Index(ts);
            int a=1;
            for(int j=0; j<ints.length;j++){
                ds_dis.addValue(i, a, ints[j], false);
                a++;
            }
        }
        ds_dis.convert2StringArray();
        return ds_dis;
    }
    
    @Override
    public List<Integer> getTotalCuts() {
        List<Integer> list = new ArrayList<>();
        list.add(this.getWordSize());
        for(int i = 0; i<this.getWordSize();i++){
            for(int c=0;c<3;c++)
                list.add(this.getAlphabetSize());
        }
        return list;
    }
    
    @Override
    public List<Integer> Cuts2Intervals(){
        List<Integer> intervals = new ArrayList<>();
        Integer[] CutIntervals = new Integer[2];
        CutIntervals[0] = 0;
        
        for(Integer wc: this.getWordSegments()){
            CutIntervals[1] = wc;
            Integer diff = CutIntervals[1]-CutIntervals[0];
            int diffxinter = (int) Math.ceil((double) diff/3);
            int suma = 0;
            for(int i=0;i<3;i++){
                if((suma+diffxinter) < diff){
                    intervals.add(diffxinter);
                    suma+=diffxinter;
                } else {
                    intervals.add(diff-suma);
                }
            }
            CutIntervals[0] = wc;
        }
        
        return intervals;
    }
    
    @Override
    public ReconstructedData Reconstruct(DiscretizedData ds_dis) {
        int attributes = this.getWordSegments()[this.getWordSize()-1]+1;
        int instances = ds_dis.getDimensions()[0];
        ReconstructedData reconstructed = new ReconstructedData(instances,attributes);
        
        List<Integer> diffs = Cuts2Intervals();
        List<Integer> maximums = this.getTotalCuts();
        
//        System.out.println("ds_dis.dimensions: ["+ds_dis.getDimensions()[0]+","+ds_dis.getDimensions()[1]+"]");
//        System.out.println("diffs:"+diffs.toString());
//        System.out.println("maximums:"+maximums.toString());
        
        for(int f=0; f<ds_dis.getIds_discretized().length;f++){
            double[] row = new double[1];
            row[0] = ds_dis.getIds_discretized()[f][0];
            for(int c=1; c<ds_dis.getIds_discretized()[f].length;c++){
                double value = (double) ds_dis.getIds_discretized()[f][c] / maximums.get(c);
                double[] normvalues = new double[diffs.get(c-1)]; 
                java.util.Arrays.fill(normvalues, value);
                double[] aux = new double[row.length + normvalues.length];
                
                System.arraycopy(row, 0, aux, 0, row.length);
                System.arraycopy(normvalues, 0, aux, row.length, normvalues.length);
                
                row = aux.clone();
            }
            reconstructed.addRow(f, row);
        }
        
        return reconstructed;
    }
    
    @Override
    public String getName(){
        return "ESAX";
    }
    
    public class Coeficients{
        private double min, mean, max;
        private int pmax, pmid, pmin, smax, smid, smin;

        public Coeficients(double[] segment, double[] cuts) {
            double[] min = mimath.MiMath.getMin(segment);
            this.pmin = (int) min[0];
            this.min = min[1];
            this.mean = mimath.MiMath.getMedia(segment);
            double[] max = mimath.MiMath.getMax(segment);
            this.pmax = (int) max[0];
            this.max = max[1];
            this.pmid = Math.floorDiv(this.pmax+this.pmin,2);
            
            smax = num2index(this.max, cuts);
            smid = num2index(this.mean, cuts);
            smin = num2index(this.min, cuts);
        }

        public double getMin() {
            return min;
        }

        public double getMean() {
            return mean;
        }

        public double getMax() {
            return max;
        }

        public int getPmax() {
            return pmax;
        }

        public int getPmid() {
            return pmid;
        }

        public int getPmin() {
            return pmin;
        }
        
        public int[] toIndexOrdered(){
            int[] index = new int[3];
            
            if(pmax < pmid && pmid < pmin){
                index[0] = smax;
                index[1] = smid;
                index[2] = smin;
            } else if(pmin < pmid && pmid < pmax) {
                    index[0] = smin;
                    index[1] = smid;
                    index[2] = smax;
                } else if(pmin < pmax && pmax < pmid){
                        index[0] = smin;
                        index[1] = smax;
                        index[2] = smid;
                    } else if(pmax < pmin && pmin < pmid){
                            index[0] = smax;
                            index[1] = smin;
                            index[2] = smid;
                        } else if(pmid < pmax && pmax < pmin){
                                index[0] = smid;
                                index[1] = smax;
                                index[2] = smin;
                            } else {
                                index[0] = smid;
                                index[1] = smin;
                                index[2] = smax;
                            }
            
            return index;
        }
    }

    
    public static void main(String[] args) throws MyException, SAXException {
        DataSet ds = new DataSet(10, false);
        ESAX esax = new ESAX(4, 7);
        TimeSeriesDiscretize_source.symbols = Utils.Utils.getListSymbols();
        DiscretizedData ds_dis = esax.Discretize(ds.getTrain());
        System.out.println(ds_dis.toString());
//        for(int i=1; i<13;i++){
//            int pos = Math.floorDiv(i-1, 3);
//            
////            int pos = Math.floorDiv(i, 3);
//            System.out.println("x="+i+", pos="+pos);
//        }
        ReconstructedData rd = esax.Reconstruct(ds_dis);
        System.out.println(rd.toString());
        
        Plotter plot = new Plotter(false, Marker.MarkerShape.CIRCLE, false, "Reconstruction", "Time", "Value");
        plot.addData(ds.getOriginal().getValuesNorm(0, 1, ds.getOriginal().getDimensions()[1]), Color.BLUE);
        plot.addData(rd.getValues(0, 1, rd.getDimensions()[1]), Color.RED);
        plot.plot("Reconstruction");
    }
}