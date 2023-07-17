/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SAX.rSAX;

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
import java.awt.Color;
import java.util.logging.Level;

/**
 *
 * @author amarquezgr
 */
public class rSAX extends SAX {
    
    private int tau;
    private double d;
    private double[][] ShiftingCuts;

    public int getTau() {
        return tau;
    }

    public double getD() {
        return d;
    }

    public double[][] getShiftingCuts() {
        return ShiftingCuts;
    }

    public void setTau(int tau) {
        this.tau = tau;
    }
    
    public rSAX() {
        super();
    }

    public rSAX(int tau) {
        this.tau = tau;
    }
    
    public rSAX(int wordSize, int alphabetSize, int tau) {
        super(wordSize, alphabetSize);
        this.tau = tau;
        this.ShiftingCuts = new double[tau+1][this.getAlphabetCuts().length];
        this.setD();
        this.RandomShifting();
    }

    public rSAX(DataSet ds, int wordSize, int alphabetSize, int tau) {
        super(ds, wordSize, alphabetSize);
        this.tau = tau;
        this.ShiftingCuts = new double[tau+1][this.getAlphabetCuts().length];
        this.setD();
        this.RandomShifting();
    }
    
    public void setD(){
        this.d = Double.POSITIVE_INFINITY;
        for(int i=1; i<this.getAlphabetCuts().length-1; i++){
            int j=i+1;
            this.d = Math.min(this.d, Math.abs((this.getAlphabetCuts()[i] - this.getAlphabetCuts()[j])));
        }
    }
    
    public void RandomShifting(){
        double lb = ((double) (this.d/2))*(-1);
        double ub = (double) (this.d/2);
        for(int k=0; k<this.tau; k++){
            double l = mimath.MiMath.randInterval(lb, ub);
            for(int j=0; j<this.getAlphabetCuts().length; j++){
                this.ShiftingCuts[k][j] = this.getAlphabetCuts()[j] + l;
            }
        }
    }
    
    public int SelectK(DataSet ds){
        int ksel = Integer.MAX_VALUE;
        double minER = Double.POSITIVE_INFINITY;
        for(int k=0;k<this.tau;k++){
            DiscretizedDataSet ds_dis = this.getDiscretization(ds, k);
            this.Classify(ds_dis, false, "train");
//            System.out.println(this.getErrorRate());
            if(this.getErrorRate() < minER){
                minER = this.getErrorRate();
                ksel = k;
            }
        }
        return ksel;
    }
    
    public DiscretizedDataSet getDiscretization(DataSet ds, int k){
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        try {
            ds_dis = new DiscretizedDataSet();
            ds_dis.setOriginal(this.Discretize(ds.getOriginal(), k));
            ds_dis.setTrain(this.Discretize(ds.getTrain(), k));
            ds_dis.setTest(this.Discretize(ds.getTest(), k));
        } catch (SAXException ex) {
            java.util.logging.Logger.getLogger(SAX.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ds_dis;
    }
    
    public int[] ts2Index(double[] ts, double[] cuts) throws SAXException {
        double[] series = paa(ts);
        //    double[] series = paa(ts);
        int[] res = new int[series.length];
        for (int i = 0; i < series.length; i++) {
            res[i] = num2index(series[i], cuts);
        }
        return res;
    }
    
    public DiscretizedData Discretize(Data ds, int k) throws SAXException{
        int[] dimensions = ds.getDimensions();
        int instances = dimensions[0];
        getWordCuts(getWordBreaks(ds.getDimensions()[1]-1));
        adjust(ds);
        DiscretizedData ds_dis = new DiscretizedData(instances, this.getWordSize() + 1);
        for(int i=0;i<instances;i++){
            ds_dis.addValue(i,0, (int) ds.getValue(i, 0), true);
            double[] ts = znorm(ds.getValues(i, 1, ds.getDimensions()[1]),0.01);
            int[] ints = this.ts2Index(ts, this.ShiftingCuts[k]);
            int a=1;
            for(int j=0; j<ints.length;j++){
                ds_dis.addValue(i, a, ints[j], false);
                a++;
            }
        }
        return ds_dis;    
    }
    
    public String PrintShiftingCut(int k){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<this.getAlphabetSize();i++){
            sb.append(this.ShiftingCuts[k][i]).append(" ");
        }
        return sb.toString();
    }
    
    @Override
    public String getName(){
        return "rSAX";
    }
    
    public static void main(String[] args) throws MyException, SAXException {
        DataSet ds = new DataSet(10, false);
        rSAX rsax = new rSAX(10, 10, 10); //tau = 10 es lo recomendado por los autores
        TimeSeriesDiscretize.TimeSeriesDiscretize_source.symbols = Utils.Utils.getListSymbols();
        int ksel = rsax.SelectK(ds);
        
        DiscretizedData ds_dis = rsax.Discretize(ds.getOriginal(), ksel);
        System.out.println(ds_dis);
        
        ReconstructedData rd = rsax.Reconstruct(ds_dis);

        Plotter plot = new Plotter(false, Marker.MarkerShape.CIRCLE, false, "Reconstruction", "Time", "Value");
        plot.addData(ds.getOriginal().getValuesNorm(0, 1, ds.getOriginal().getDimensions()[1]), Color.BLUE);
        plot.addData(rd.getValues(0, 1, rd.getDimensions()[1]), Color.RED);
        plot.plot("Reconstruction");

    }
}
