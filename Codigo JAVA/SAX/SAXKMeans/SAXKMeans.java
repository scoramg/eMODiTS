/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SAX.SAXKMeans;

import DataSets.Data;
import DataSets.DataSet;
import DataSets.DiscretizedData;
import DataSets.ReconstructedData;
import Exceptions.MyException;
import Graphs.Marker;
import Graphs.Plotter;
import SAX.RKmeans.RKMeans;
import SAX.SAX;
import SAX.SAXException;
import java.awt.Color;
//import SAX.SAX;

/**
 *
 * @author amarquezgr
 */
public class SAXKMeans extends SAX {

    public SAXKMeans() {
    }

    public SAXKMeans(int wordSize, int alphabetSize) {
        super(wordSize, alphabetSize);
    }

    public SAXKMeans(DataSet ds, int wordSize, int alphabetSize) {
        super(ds, wordSize, alphabetSize);
    }
    
    public int[] ts2Index(double[] ts, double[] cuts) throws SAXException {
        double[] series = paa(ts);
        int[] res = new int[series.length];
        for (int i = 0; i < series.length; i++) {
            res[i] = num2index(series[i], cuts);
        }
        return res;
    }
    
    @Override
    public DiscretizedData Discretize(Data ds) throws SAXException{
        int[] dimensions = ds.getDimensions();
        int instances = dimensions[0];
        getWordCuts(getWordBreaks(ds.getDimensions()[1]-1));
        adjust(ds);
        DiscretizedData ds_dis = new DiscretizedData(instances, this.getWordSize() + 1);
        for(int i=0;i<instances;i++){
            ds_dis.addValue(i,0, (int) ds.getValue(i, 0), true);
            double[] ts = znorm(ds.getValues(i, 1, ds.getDimensions()[1]),0.01);
            double[] cuts = RKMeans.CalculateCutsByKMeans(ts, this.getAlphabetSize());
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
        return "SAXKMeans";
    }
    
    public static void main(String[] args) throws MyException, SAXException {
        DataSet ds = new DataSet(10);
        SAXKMeans saxkmeans = new SAXKMeans(5, 10);
        TimeSeriesDiscretize.TimeSeriesDiscretize_source.symbols = Utils.Utils.getListSymbols();
        DiscretizedData ds_dis = saxkmeans.Discretize(ds.getTrain());
        System.out.println(ds_dis.toString());
        
        ReconstructedData rd = saxkmeans.Reconstruct(ds_dis);

        Plotter plot = new Plotter(false, Marker.MarkerShape.CIRCLE, false, "Reconstruction", "Time", "Value");
        plot.addData(ds.getOriginal().getValuesNorm(0, 1, ds.getOriginal().getDimensions()[1]), Color.BLUE);
        plot.addData(rd.getValues(0, 1, rd.getDimensions()[1]), Color.RED);
        plot.plot("Reconstruction");
    }
}
