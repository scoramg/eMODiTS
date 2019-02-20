/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SAX.OneD_SAX;

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
import java.util.stream.IntStream;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 *
 * @author amarquezgr
 */
public class OneD_SAX extends SAX {
    private int NoSlopes;
    private double[] SlopeCuts;

    public OneD_SAX() {
        super();
        this.NoSlopes = 0;
//        SlopeCuts = OneD_SAX.CalculateCuts(this.NoSlopes, 1);
    }
    
    public OneD_SAX(int NoSlopes) {
        this.NoSlopes = NoSlopes;
         SlopeCuts = OneD_SAX.CalculateCuts(this.NoSlopes, 1);
    }

    public OneD_SAX(int NoSlopes, int wordSize, int alphabetSize) {
        super(wordSize, alphabetSize);
        this.NoSlopes = NoSlopes;
        SlopeCuts = OneD_SAX.CalculateCuts(this.NoSlopes, (double) 0.03/wordSize);
    }

    public OneD_SAX(int NoSlopes, DataSet ds, int wordSize, int alphabetSize) {
        super(ds, wordSize, alphabetSize);
        this.NoSlopes = NoSlopes;
         SlopeCuts = OneD_SAX.CalculateCuts(this.NoSlopes, (double) 0.03/wordSize);
    }
    
    public static double[] CalculateCuts(int alphabetSize, double standardDev) {
//        double riskProbabilityLevel = 0.02D;
        double mean = 0;
//        double standardDev = 1;
//        double expectedRisk = 0.051286461995869864D;

        NormalDistribution distribution = new NormalDistribution(mean, standardDev);
        double[] cuts = new double[alphabetSize];
        cuts[0] = Double.NEGATIVE_INFINITY;
        for(int i=1;i<=alphabetSize-1;i++){
            cuts[i] = mimath.MiMath.Redondear(distribution.inverseCumulativeProbability((double) (i)/alphabetSize),2);
        }
        Arrays.sort(cuts);
        return cuts;
//        System.out.println(distribution.toString());
//        double outcomeRisk = distribution.inverseCumulativeProbability(riskProbabilityLevel);
    }
    
    public double getSlope(double[] V, int t){
        int L = V.length;
        int[] T = IntStream.range(t, L+t).toArray();
//        double meanT = mimath.MiMath.getMedia(T);
//        double meanV = mimath.MiMath.getMedia(V);
//        double s1=0.0, s2=0.0;
//        
        double[][] data = new double[L][2];
        for(int i=0;i<L;i++){
            data[i][0] = V[i];
            data[i][1] = T[i];
//            try{
//                s1 += ((T[i]-meanT)*(V[i]-meanV));
//                s2 += (Math.pow(T[i]-meanT,2));
//            } catch(ArrayIndexOutOfBoundsException exception) {
//                System.out.println("1");
//            }
        }
//        return (double) s1/s2;
        SimpleRegression simpleRegression = new SimpleRegression(true);
        simpleRegression.addData(data);
        return simpleRegression.getSlope();
    }
    
    
    public double[] slopes(double[] ts) throws SAXException {
    // fix the length
    int len = ts.length;
    if (len < this.getWordSize()) {
      throw new SAXException("PAA size can't be greater than the timeseries size.");
    }
    // check for the trivial case
    if (len == this.getWordSize()) {
      return Arrays.copyOf(ts, ts.length);
    }
    else {
      double[] slopes = new double[this.getWordSize()];
      int segStart = 0; //***
      int segEnd = 0; //****
      for (int i = 0; i < this.getWordSize(); i++) {
        segEnd = this.getWordSegments()[i];
//          System.out.println(segStart+","+segEnd);
        double[] segment = Arrays.copyOfRange(ts, segStart, segEnd);
        
        slopes[i] = getSlope(segment, segStart);
        segStart = this.getWordSegments()[i];
      }
      return slopes;
    }
  }
    
    public int[] ts2slopes(double[] ts, double nThreshold) throws SAXException {
    double[] series = slopes(znorm(ts, nThreshold));
//    double[] series = slopes(ts);
    int[] res = new int[series.length];
    for (int i = 0; i < series.length; i++) {
      res[i] = num2index(series[i], this.SlopeCuts);
    }
    return res;
  }
    
    public MultiDataSet Transform(Data ds) throws SAXException{
        int[] dimensions = ds.getDimensions();
        int instances = dimensions[0];
        getWordCuts(getWordBreaks(ds.getDimensions()[1]-1));
        adjust(ds);
        MultiDataSet mds_dis = new MultiDataSet(instances, this.getWordSize(), 2);
        
        for(int i=0;i<instances;i++){
            mds_dis.addClass(i, (int) ds.getValue(i, 0));
            double[] ts = znorm(ds.getValues(i, 1, ds.getDimensions()[1]),0.01);
            int[] ints = this.ts2Index(ts);
            int[] slopes = this.ts2slopes(ts, 0.01);
            int a=1;
            for(int j=0; j<ints.length;j++){
                mds_dis.add(i, j, 0, ints[j]);
                mds_dis.add(i, j, 1, slopes[j]);
                a++;
            }
        }
        
        return mds_dis;
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
            int[] ints = this.ts2Index(ts);
            int[] slopes = this.ts2slopes(ts, 0.01);
            int a=1;
            for(int j=0; j<ints.length;j++){
                ds_dis.addValue(i, a, OneDSAX2Symbols(ints[j], this.getAlphabetSize(), slopes[j], this.NoSlopes), false);
                a++;
            }
        }
        ds_dis.convert2StringArray();
        return ds_dis;
    }
    
    public List<Integer> getTotalCuts(DiscretizedData ds_dis) {
        List<Integer> list = new ArrayList<>();
        list.add(this.getWordSize());
        for(int i = 0; i<this.getWordSize();i++){
            list.add(ds_dis.getMaxIntValue());
        }
        return list;
    }
    
    @Override
    public ReconstructedData Reconstruct(DiscretizedData ds_dis) {
        int attributes = this.getWordSegments()[this.getWordSize()-1]+1;
        int instances = ds_dis.getDimensions()[0];
        ReconstructedData reconstructed = new ReconstructedData(instances,attributes);
        
        List<Integer> diffs = Cuts2Intervals();
        List<Integer> maximums = this.getTotalCuts(ds_dis);
        
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
    
    public static String toBinary(int cuts, int value){
        int NoBits = (int) (Math.floor(Math.log(cuts)));
        if(cuts%2 != 0) NoBits++;
        
        StringBuilder binary = new StringBuilder();
        int mask = (int) Math.pow(2,NoBits);
        while (mask > 0){
            if ((mask & value) == 0){
                binary.append("0");
            } else {
                binary.append("1");
            }
            mask = mask >> 1;
        }
        return binary.toString();
    }
    
    public int OneDSAX2Symbols(int alph, int NoCutsAlphabet, int slope, int NoSlopes){
        alph = alph-1;
        slope = slope-1;
//        System.out.println(alph+"-"+slope);
        String BinaryAlphabet = toBinary(NoCutsAlphabet, alph);
        String BinarySlope = toBinary(NoSlopes, slope);
//        System.out.println(BinaryAlphabet+"."+BinarySlope);
        return Integer.parseInt(BinaryAlphabet+BinarySlope, 2)+1;
    }
    
    @Override
    public String getName(){
        return "OneD-SAX";
    }
    
    public static void main(String[] args) throws SAXException, MyException {
        DataSet ds = new DataSet(10);
//        DataSets.Data data = new Data();
//        int[] dimensions = new int[2];
//        dimensions[0] = 2;
//        dimensions[1] = 6;
//        data.setDimensions(dimensions);
//        data.setData(new double[dimensions[0]][dimensions[1]]);
////        data = [[-1., 2., 0.1, -1., 1., -1.], [1., 3.2, -1., -3., 1., -1.]];
//        data.add(0, 0, -1);
//        data.add(0, 1, 2);
//        data.add(0, 2, 0.1);
//        data.add(0, 3, -1);
//        data.add(0, 4, 1);
//        data.add(0, 5, -1);
//        data.add(1, 0, 1);
//        data.add(1, 1, 3.2);
//        data.add(1, 2, -1);
//        data.add(1, 3, -3);
//        data.add(1, 4, 1);
//        data.add(1, 5, -1);
//        System.out.println(data.toString());
        
        OneD_SAX oned_sax = new OneD_SAX(8, 10, 8);
//        MultiDataSet trans = oned_sax.Transform(data);
        TimeSeriesDiscretize_source.symbols = Utils.Utils.getListSymbols();
        DiscretizedData ds_dis = oned_sax.Discretize(ds.getTrain());
        System.out.println(ds_dis.toString());

        ReconstructedData rd = oned_sax.Reconstruct(ds_dis);

        Plotter plot = new Plotter(false, Marker.MarkerShape.CIRCLE, false, "Reconstruction", "Time", "Value");
        plot.addData(ds.getOriginal().getValuesNorm(0, 1, ds.getOriginal().getDimensions()[1]), Color.BLUE);
        plot.addData(rd.getValues(0, 1, rd.getDimensions()[1]), Color.RED);
        plot.plot("Reconstruction");
        
        
    }
}
