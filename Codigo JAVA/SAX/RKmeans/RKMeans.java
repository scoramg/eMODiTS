/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SAX.RKmeans;

import DataMining.Clustering.KMeans;
import DataMining.Clustering.KMeansResultado;
import DataMining.Clustering.Punto;
import DataSets.Data;
import DataSets.DataSet;
import DataSets.DiscretizedData;
import DataSets.ReconstructedData;
import Exceptions.MyException;
import Graphs.Marker;
import Graphs.Plotter;
import SAX.SAX;
import SAX.SAXException;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author amarquezgr
 */
public class RKMeans extends SAX {

    public RKMeans() {
    }

    public RKMeans(int wordSize, int alphabetSize) {
        super(wordSize, alphabetSize);
    }

    public RKMeans(DataSet ds, int wordSize, int alphabetSize) {
        super(ds, wordSize, alphabetSize);
    }
    
    public static double[] CalculateCutsByKMeans(double[] data, int k){
        List<Punto> puntos = new ArrayList<>();
        for(int i=0;i<data.length;i++){
            double[] row = new double[1];
            row[0] = data[i];
            Punto p = new Punto(row);
            puntos.add(p);
        }
        KMeans kmeans = new KMeans();
        KMeansResultado resultado = kmeans.calcular(puntos, k);
        double[] cuts = new double[k];
        for (int j=0; j < resultado.getClusters().size(); j++) {
            cuts[j] = resultado.getClusters().get(j).getCentroide().getData()[0];
        }
        Arrays.sort(cuts);
        return cuts;
    }
    
    public int[] ts2Index(double[] ts, double[] cuts) throws SAXException {
        int[] res = new int[ts.length];
        for (int i = 0; i < ts.length; i++) {
            res[i] = num2index(ts[i], cuts);
        }
        return res;
    }
    
    @Override
    public DiscretizedData Discretize(Data ds) throws SAXException{
        int[] dimensions = ds.getDimensions();
        int instances = dimensions[0];
        int attributes = dimensions[1];
        getWordCuts(getWordBreaks(ds.getDimensions()[1]-1));
        adjust(ds);
        DiscretizedData ds_dis = new DiscretizedData(instances, attributes);
        for(int i=0;i<instances;i++){
            ds_dis.addValue(i,0, (int) ds.getValue(i, 0), true);
            double[] ts = ds.getValues(i, 1, ds.getDimensions()[1]);
            double[] cuts = CalculateCutsByKMeans(ts, this.getAlphabetSize());
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
        return "RKMeans";
    }
    
    @Override
    public ReconstructedData Reconstruct(DiscretizedData ds_dis) {
        int attributes = ds_dis.getDimensions()[1];
        int instances = ds_dis.getDimensions()[0];
        ReconstructedData reconstructed = new ReconstructedData(instances,attributes);
        
        for(int f=0; f<ds_dis.getIds_discretized().length;f++){
            double[] norm = Utils.Utils.Normalize(Arrays.copyOfRange(ds_dis.getDds_discretized()[f], 1, ds_dis.getIds_discretized()[f].length));
            double[] row = new double[1+norm.length];
            row[0] = ds_dis.getDds_discretized()[f][0];
            System.arraycopy(norm, 0, row, 1, norm.length);
//            row[0] = ds_dis.getIds_discretized()[f][0];
//            for(int c=1; c<ds_dis.getIds_discretized()[f].length;c++){
//                double value = (double) ds_dis.getIds_discretized()[f][c] / maximums.get(c);
//                double[] normvalues = new double[diffs.get(c-1)]; 
//                java.util.Arrays.fill(normvalues, value);
//                double[] aux = new double[row.length + normvalues.length];
//                
//                System.arraycopy(row, 0, aux, 0, row.length);
//                System.arraycopy(normvalues, 0, aux, row.length, normvalues.length);
//                
//                row = aux.clone();
//            }
            reconstructed.addRow(f, row);
        }
        
        return reconstructed;
    }
    
    public static void main(String[] args) throws MyException, SAXException {
        DataSet ds = new DataSet(10);
        RKMeans rkmeans = new RKMeans(10, 10);
        TimeSeriesDiscretize.TimeSeriesDiscretize_source.symbols = Utils.Utils.getListSymbols();
        DiscretizedData ds_dis = rkmeans.Discretize(ds.getTrain());
        
        ReconstructedData rd = rkmeans.Reconstruct(ds_dis);
        
//        System.out.println(rd.getDimensions()[0]+","+rd.getDimensions()[1]);

        Plotter plot = new Plotter(false, Marker.MarkerShape.CIRCLE, false, "Reconstruction", "Time", "Value");
        plot.addData(ds.getOriginal().getValuesNorm(0, 1, ds.getOriginal().getDimensions()[1]), Color.BLUE);
        plot.addData(rd.getValues(0, 1, rd.getDimensions()[1]), Color.RED);
        plot.plot("Reconstruction");
    }
}
