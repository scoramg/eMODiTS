/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SAX.aSAX;

import DataSets.Data;
import DataSets.DataSet;
import DataSets.DiscretizedData;
import DataSets.DiscretizedDataSet;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class aSAX extends SAX {
    
    public aSAX() {
        super();
    }

    public aSAX(int wordSize, int alphabetSize) {
        super(wordSize, alphabetSize);
    }
    
    public aSAX(DataSet ds, int wordSize, int alphabetSize) {
        super(ds, wordSize, alphabetSize);
    }
    
    public void CalculateAdaptiveBreakpoints(double[][] data, double threshold){
        double[] r = new double[getAlphabetSize()];
//        double[] cuts = this.getAlphabetCuts();
        double[] cuts = new double[this.getAlphabetSize()+1];
        for(int i=0;i<this.getAlphabetSize();i++) cuts[i] = this.getAlphabetCuts()[i];
        cuts[this.getAlphabetSize()] = Double.POSITIVE_INFINITY;
        
        List<List<Double>> ta = new ArrayList<>();
        double error = Double.POSITIVE_INFINITY;
        double delta = Double.POSITIVE_INFINITY;
        
        double[][] PAACoeficients = new double[data.length][this.getWordSize()];
        for(int i=0; i<data.length;i++){
            try {
                PAACoeficients[i] = this.paa(this.znorm(data[i], 0.01));
            } catch (SAXException ex) {
                Logger.getLogger(aSAX.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        while(error>=threshold){
            for(int i=0; i<this.getAlphabetSize();i++){
                List<Double> tn = new ArrayList<>();
                for (int f=0;f<PAACoeficients.length;f++){
                    for (int c=0;c<PAACoeficients[f].length;c++){
                        if (PAACoeficients[f][c] >= cuts[i] && PAACoeficients[f][c] < cuts[i+1]){
                            tn.add(PAACoeficients[f][c]);
                        }
                    }
                }
                r[i] = mimath.MiMath.getMedia(tn);
                ta.add(tn);
            }
            for (int i=1;i<this.getAlphabetSize();i++){
                cuts[i] = (double)(r[i-1]+r[i])/2;
            }
            
            double new_delta=0;
            for(int i=1; i<this.getAlphabetSize(); i++){
                for(double d: ta.get(i))
                    new_delta += Math.pow((d-r[i]), 2);
            } 
            error = (delta-new_delta)/delta;    
            delta=new_delta;
        }
        double[] newcuts = Arrays.copyOf(cuts, cuts.length-1);
        Arrays.sort(newcuts);
        this.setAlphabetCuts(newcuts);
    }
    
    @Override
    public DiscretizedData Discretize(Data ds) throws SAXException{
        int[] dimensions = ds.getDimensions();
        int instances = dimensions[0];
        
        CalculateAdaptiveBreakpoints(DataSet.getSubDataSet(ds.getData(), 0.1), 0.001);
        getWordCuts(getWordBreaks(ds.getDimensions()[1]-1));
        adjust(ds);
        
        DiscretizedData ds_dis = new DiscretizedData(instances, this.getWordSize() + 1);
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
//        ds_dis.convert2CharArray();
        ds_dis.convert2StringArray();
//        ds_dis.convert2FloatArray();
        return ds_dis;
    }
    
    @Override
    public String getName(){
        return "aSAX";
    }
    
    
    public static void main(String[] args) throws MyException, SAXException {
        DataSet ds = new DataSet(10, false);
        aSAX asax = new aSAX(10, 8);
//        MultiDataSet trans = oned_sax.Transform(data);
        TimeSeriesDiscretize_source.symbols = Utils.Utils.getListSymbols();
        DiscretizedData ds_dis = asax.Discretize(ds.getTrain());
        System.out.println(ds_dis.toString());
        
        ReconstructedData rd = asax.Reconstruct(ds_dis);

        Plotter plot = new Plotter(false, Marker.MarkerShape.CIRCLE, false, "Reconstruction", "Time", "Value");
        plot.addData(ds.getOriginal().getValuesNorm(0, 1, ds.getOriginal().getDimensions()[1]), Color.BLUE);
        plot.addData(rd.getValues(0, 1, rd.getDimensions()[1]), Color.RED);
        plot.plot("Reconstruction");

//        try {
//            aSAX asax = new aSAX(8, 3);
//            
//            for(double d: asax.getAlphabetCuts()){
//                System.out.println(d);
//            }
//            
//            double[][] randwalk = DataSet.RandomWalk(55, 256, 1);
////            double[][] subrandwalk = DataSet.getSubDataSet(randwalk, 0.3);
////            System.out.println(subrandwalk.length);
//            
//            double[][] PAACoeficients = new double[randwalk.length][asax.getWordSize()];
//            for(int i=0; i<randwalk.length;i++){
//                try {
//                    PAACoeficients[i] = asax.paa(asax.znorm(randwalk[i], 0.01));
//                } catch (SAXException ex) {
//                    Logger.getLogger(aSAX.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
////            
//////            Classification cls = new Classification(PAACoeficients);
//////            SimpleKMeans kmeans = new SimpleKMeans();
//////            kmeans.setNumClusters(asax.getAlphabetSize());
//////            String ArchivoSalida = "Prueba.arff";
//////            cls.Clustering(kmeans, ArchivoSalida);
////            
//            asax.CalculateAdaptiveBreakpoints(PAACoeficients, 0.01);
//            for(double d: asax.getAlphabetCuts()){
//                System.out.println(d);
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(aSAX.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
    
    
}
