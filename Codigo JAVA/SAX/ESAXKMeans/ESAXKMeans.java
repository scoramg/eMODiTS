/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SAX.ESAXKMeans;

import DataSets.Data;
import DataSets.DataSet;
import DataSets.DiscretizedData;
import DataSets.ReconstructedData;
import Exceptions.MyException;
import Graphs.Marker;
import Graphs.Plotter;
import SAX.ESAX.ESAX;
import SAX.RKmeans.RKMeans;
import SAX.SAXException;
import java.awt.Color;
import java.util.Arrays;

/**
 *
 * @author amarquezgr
 */
public class ESAXKMeans extends ESAX {

    public ESAXKMeans() {
    }

    public ESAXKMeans(int wordSize, int alphabetSize) {
        super(wordSize, alphabetSize);
    }

    public ESAXKMeans(DataSet ds, int wordSize, int alphabetSize) {
        super(ds, wordSize, alphabetSize);
    }
    
    public  Coeficients[] PAA(double[] ts, double[] cuts) throws SAXException {
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

                paa[i] = new Coeficients(segment, cuts);
                segStart = this.getWordSegments()[i];
            }
            return paa;
        }
    }
    
    public int[] ts2Index(double[] ts, double[] cuts) throws SAXException {
        ESAX.Coeficients[] series = PAA(ts, cuts);
        int[] res = new int[series.length * 3];
        int r=0;
        for (int i = 0; i < series.length; i++) {
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
            double[] cuts = RKMeans.CalculateCutsByKMeans(ts, this.getAlphabetSize());
            Arrays.sort(cuts);
            
            int[] ints = this.ts2Index(ts, cuts);
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
    public String getName(){
        return "ESAXKMeans";
    }
    
    public static void main(String[] args) throws MyException, SAXException {
        DataSet ds = new DataSet(10, false);
        ESAXKMeans esaxkmeans = new ESAXKMeans(4, 7);
        TimeSeriesDiscretize.TimeSeriesDiscretize_source.symbols = Utils.Utils.getListSymbols();
        DiscretizedData ds_dis = esaxkmeans.Discretize(ds.getTrain());
        System.out.println(ds_dis.toString());
        
        ReconstructedData rd = esaxkmeans.Reconstruct(ds_dis);

        Plotter plot = new Plotter(false, Marker.MarkerShape.CIRCLE, false, "Reconstruction", "Time", "Value");
        plot.addData(ds.getOriginal().getValuesNorm(0, 1, ds.getOriginal().getDimensions()[1]), Color.BLUE);
        plot.addData(rd.getValues(0, 1, rd.getDimensions()[1]), Color.RED);
        plot.plot("Reconstruction");
    }
}
