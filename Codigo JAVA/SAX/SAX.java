package SAX;

import DataMining.Classification.Classification;
import DataSets.Data;
import DataSets.DataSet;
import DataSets.DiscretizedData;
import DataSets.DiscretizedDataSet;
import DataSets.ReconstructedData;
import Exceptions.MyException;
import Graphs.Marker;
import Graphs.Plotter;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.math3.distribution.NormalDistribution;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.trees.J48;

/**
 * Implements algorithms for low-level data manipulation.
 * 
 * @author Pavel Senin
 * 
 */
public class SAX {

  private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  /** The latin alphabet, lower case letters a-z. */
  public static final char[] ALPHABET = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
      'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

  // static block - we instantiate the logger
  //
  private static final Logger LOGGER = LoggerFactory.getLogger(SAX.class);
  
  private int wordSize, alphabetSize;
  private int[] wordSegments;
  private double ErrorRate, pointsPerSegment;
  private String DecisionTreeGraph;
  private double[] ErrorRatesByFolds;
  private List<Prediction> predictions;
  private double[] AlphabetCuts;
//  private DiscretizedDataSet ds_dis;
  
  
  /**
   * Constructor.
   */
  public SAX() {
    super();
    ErrorRate = 0.0;
    DecisionTreeGraph="";
//    AlphabetCuts = SAX.CalculateCuts(0);
  }
  
  public SAX(int wordSize, int alphabetSize) {
        this.wordSize = wordSize;
        this.alphabetSize = alphabetSize;
        ErrorRate = 0.0;
        DecisionTreeGraph="";
        wordSegments = new int[wordSize];
        AlphabetCuts = SAX.CalculateCuts(this.alphabetSize);
    }

  /**
   * Constructor.
     * @param wordSize: size of word
     * @param alphabetSize: number of alphabets
   */
    public SAX(DataSet ds, int wordSize, int alphabetSize) {
        this.wordSize = wordSize;
        this.alphabetSize = alphabetSize;
        ErrorRate = 0.0;
        DecisionTreeGraph="";
        wordSegments = new int[wordSize];
        AlphabetCuts = SAX.CalculateCuts(this.alphabetSize);
//        setDiscretization(ds);
    }
    
    public DiscretizedDataSet getDiscretization(DataSet ds){
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        try {
            ds_dis = new DiscretizedDataSet();
            ds_dis.setOriginal(this.Discretize(ds.getOriginal()));
            ds_dis.setTrain(this.Discretize(ds.getTrain()));
            ds_dis.setTest(this.Discretize(ds.getTest()));
        } catch (SAXException ex) {
            java.util.logging.Logger.getLogger(SAX.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ds_dis;
    }

    public double[] getAlphabetCuts() {
        return AlphabetCuts;
    }

//    public DiscretizedDataSet getDs_dis() {
//        return ds_dis;
//    }

    public void setAlphabetCuts(double[] AlphabetCuts) {
        this.AlphabetCuts = AlphabetCuts;
    }

    public double getPointsPerSegment() {
        return pointsPerSegment;
    }
    
  /**
   * Get Word Size.
     * @return The word size
   */
    public int getWordSize() {
        return wordSize;
    }

    public double[] getErrorRatesByFolds() {
        return ErrorRatesByFolds;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }
    
    /**
   * Get Word Cuts.
     * @return The word Segments 
   */
    public int[] getWordSegments() {
        return wordSegments;
    }

    /**
   * Get Alphabet Size.
     * @return The alphabet size
   */
    public int getAlphabetSize() {
        return alphabetSize;
    }

    /**
   * Set Word Size.
   * @param wordSize: size of word
   */
    public void setWordSize(int wordSize) {
        this.wordSize = wordSize;
        this.wordSegments = new int[wordSize];
    }

    /**
   * Set Alphabet Size.
   * @param alphabetSize: number of alphabets
   */
    public void setAlphabetSize(int alphabetSize) {
        this.alphabetSize = alphabetSize;
    }

    /**
   * Get Misclassification rate by j48 algorithm.
     * @return Misclassification rate
   */
    public double getErrorRate() {
        return ErrorRate;
    }
    
     /**
   * Computes the mean value of timeseries.
   * 
   * @param series The timeseries.
   * @return The mean value.
   */
  public double mean(double[] series) {
    double res = 0D;
    int count = 0;
    for (double tp : series) {
      res += tp;
      count += 1;

    }
    if (count > 0) {
      return res / ((Integer) count).doubleValue();
    }
    return Double.NaN;
  }
  
  /**
   * Speed-optimized implementation.
   * 
   * @param series The timeseries.
   * @return the standard deviation.
   */
  public double stDev(double[] series) {
    double num0 = 0D;
    double sum = 0D;
    int count = 0;
    for (double tp : series) {
      num0 = num0 + tp * tp;
      sum = sum + tp;
      count += 1;
    }
    double len = ((Integer) count).doubleValue();
    return Math.sqrt((len * num0 - sum * sum) / (len * (len - 1)));
  }

  /**
   * Z-Normalize routine.
   * 
   * @param series the input timeseries.
   * @param normalizationThreshold the zNormalization threshold value.
   * @return Z-normalized time-series.
   */
  public double[] znorm(double[] series, double normalizationThreshold) {
    double[] res = new double[series.length];
    double mean = mean(series);
    double sd = stDev(series);
    if (sd < normalizationThreshold) {
      // return series.clone();
      // return array of zeros
      return res;
    }
    for (int i = 0; i < res.length; i++) {
      res[i] = (series[i] - mean) / sd;
    }
    return res;
  }
  
    public double[] getWordBreaks(int len){
        double[] breaks = new double[this.wordSize + 1];
        this.pointsPerSegment = (double) len / (double) this.wordSize;
        for (int i = 0; i < this.wordSize + 1; i++) {
            breaks[i] = i * this.pointsPerSegment;
        }
        return breaks;
    }
  
  public void getWordCuts(double[] breaks){
//      double[] breaks = getWordBreaks(len);
      for(int i=1;i<breaks.length;i++){
//          System.out.println("breaks["+i+"]:"+breaks[i]);
          this.wordSegments[i-1] = Double.valueOf(Math.ceil(breaks[i])).intValue();
//          System.out.println("this.wordSegments["+(i-1)+"]:"+wordSegments[i-1]);
      }
  }
  
  public void adjust(Data ds){
      this.wordSegments[this.wordSize-1] = ds.getDimensions()[1]-1;
  }

  /**
   * Approximate the timeseries using PAA. If the timeseries has some NaN's they are handled as
   * follows: 1) if all values of the piece are NaNs - the piece is approximated as NaN, 2) if there
   * are some (more or equal one) values happened to be in the piece - algorithm will handle it as
   * usual - getting the mean.
   * 
   * @param ts The timeseries to approximate.
   * @return PAA-approximated timeseries.
   * @throws SAXException if error occurs.
   * 
   */
  public double[] paa(double[] ts) throws SAXException {
    // fix the length
    int len = ts.length;
    if (len < this.wordSize) {
      throw new SAXException("PAA size can't be greater than the timeseries size.");
    }
    // check for the trivial case
    if (len == this.wordSize) {
      return Arrays.copyOf(ts, ts.length);
    }
    else {
      double[] paa = new double[this.wordSize];
//      double[] breaks = getWordBreaks(len);
//      getWordCuts(getWordBreaks(len));
      double segStart = 0; //***
      double segEnd = 0; //****
      for (int i = 0; i < this.wordSize; i++) {
//        double segStart = breaks[i];
//        double segEnd = breaks[i + 1];
//        double segStart = this.wordSegments[i];
        segEnd = this.wordSegments[i];

        double fractionStart = Math.ceil(segStart) - segStart;
        double fractionEnd = segEnd - Math.floor(segEnd);

        int fullStart = Double.valueOf(Math.floor(segStart)).intValue();
        int fullEnd = Double.valueOf(Math.ceil(segEnd)).intValue();
//          System.out.println(fullStart+","+fullEnd);
        double[] segment = Arrays.copyOfRange(ts, fullStart, fullEnd);

        if (fractionStart > 0) {
          segment[0] = segment[0] * fractionStart;
        }

        if (fractionEnd > 0) {
          segment[segment.length - 1] = segment[segment.length - 1] * fractionEnd;
        }

        double elementsSum = 0.0;
        for (double e : segment) {
          elementsSum = elementsSum + e;
        }

        paa[i] = elementsSum / pointsPerSegment;
        segStart = this.wordSegments[i];
      }
      return paa;
    }
  }
 
  
  public int[] ts2Index(double[] ts) throws SAXException {
//    double[] series = paa(znorm(ts, nThreshold));
    double[] series = paa(ts);
    int[] res = new int[series.length];
    for (int i = 0; i < series.length; i++) {
      res[i] = num2index(series[i], AlphabetCuts);
    }
    return res;
  }
  
  /**
   * Get mapping of number to cut index.
   * 
   * @param value the value to map.
   * @param cuts the array of intervals.
   * @return character corresponding to numeric value.
   */
    public int num2index(double value, double[] cuts) {
        int count = 1;
        while ((count < cuts.length) && (cuts[count] <= value)) {
          count++;
        }
        return count;
    }
  
  public static double[] CalculateCuts(int alphabetSize) {
//        double riskProbabilityLevel = 0.02D;
        double mean = 0;
        double standardDev = 1;
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
    
    public DiscretizedData Discretize(Data ds) throws SAXException{
        int[] dimensions = ds.getDimensions();
        int instances = dimensions[0];
        getWordCuts(getWordBreaks(ds.getDimensions()[1]-1));
        adjust(ds);
        DiscretizedData ds_dis = new DiscretizedData(instances, this.wordSize + 1);
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
    
    public String getName(){
        return "SAX";
    }
    
    public void Classify(DiscretizedDataSet ds_dis, boolean UsingTest, String set_type){
        try {
            J48 j48 = new J48();
            Classification csf = new Classification();
            if(UsingTest){
                DiscretizedData ds_dis_train = ds_dis.getTrain();
                DiscretizedData ds_dis_test = ds_dis.getTest();
                csf = new Classification(ds_dis_train, ds_dis_test);
                csf.ClassifyWithTraining(j48);
                this.ErrorRate = csf.getErrorRate();
                this.predictions = csf.getPredictions();
            } else{
                DiscretizedData data = new DiscretizedData();
                
                switch (set_type){
                    case "original":
                        data = ds_dis.getOriginal();
                        break;
                    case "train":
//                        DiscretizedData ds_dis_train = this.Discretize(dataset.getTrain());
                        data=ds_dis.getTrain();
                        break;
                    case "test":
//                        DiscretizedData ds_dis_test = this.Discretize(dataset.getTest());
                        data=ds_dis.getOriginal();
                        break;
                } 
                csf = new Classification(data);
                double[] errors = csf.ClassifyByCrossValidation(j48);
                this.ErrorRatesByFolds = errors.clone();
                this.ErrorRate = mimath.MiMath.getMedia(errors);
                this.predictions = csf.getPredictions();
            }
            
            this.DecisionTreeGraph = j48.graph();
            
        } catch (Exception ex) { 
          java.util.logging.Logger.getLogger(SAX.class.getName()).log(Level.SEVERE, null, ex);
      } 
        
    }
  
    
    public void ExportGraph(String BDName, int Execution, String Location) throws FileNotFoundException{
        String FileName = "Arbol_e"+(Execution+1);
        
        if(Execution<0){
            FileName = "Arbol_final";
        }
        String directory = Location+"/"+getName()+"/"+BDName+"/Trees";
        

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".txt" )  ){
               out.println( this.DecisionTreeGraph );
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(SAX.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void ExportStrings(DataSet ds, String Location) throws FileNotFoundException, SAXException{
        String FileName = "DiscretizedString";
        String directory = Location+"/"+getName()+"/"+ds.getName()+"/Strings";
        

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        DiscretizedData ds_dis = this.Discretize(ds.getOriginal());
        
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".txt" )  ){
               out.println( ds_dis.PrintStrings() );
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(SAX.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void ExportStrings2JSON(DataSet ds, String Location) throws FileNotFoundException, SAXException{
        String FileName = getName()+"_"+ds.getName();
        String FileNameTrain = getName()+"_"+ds.getName()+"_TRAIN";
        String FileNameTest = getName()+"_"+ds.getName()+"_TEST";
        
        String directory = Location+"/"+getName()+"/JSONMODiTS";
        

        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();
        
        DiscretizedData ds_dis = this.Discretize(ds.getOriginal());
//        DiscretizedData ds_dis_train = this.Discretize(ds.getTrain());
//        DiscretizedData ds_dis_test = this.Discretize(ds.getTest());

        
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".json" )  ){
               out.println( ds_dis.PrintIntContinuos2JSON());
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(SAX.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileNameTrain+".json" )  ){
               out.println( ds_dis.PrintIntContinuos2JSON());
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(SAX.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(  PrintWriter out = new PrintWriter( directory+"/"+FileNameTest+".json" )  ){
               out.println( ds_dis.PrintIntContinuos2JSON());
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(SAX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    public String Predictions2CSV(){
        StringBuilder sb = new StringBuilder();
        for(Prediction p : predictions){
            sb.append(p.actual()).append(",").append(p.predicted()).append("\n");
        }
        return sb.toString();
    }
    
    public void ExportErrorRates(DataSet ds, String Location) {
        String FileName = "ErrorRates";
        
        String directory = Location+'/'+this.getName()+'/'+ds.getName();
        
        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".csv" )  ){
            for(double d: this.ErrorRatesByFolds){
               out.println(d);
            }
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(SAX.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    public void ImportFromMatFile(String filename, int ds_length){
        try {
            MatFileReader mfr = new MatFileReader(filename);
            Map<String, MLArray> mlArrayRetrived = mfr.getContent();

            MLArray w = mlArrayRetrived.get("word");
            double[][] arr = ((MLDouble) w).getArray();

            this.wordSize = arr[0].length;
            this.wordSegments = new int[this.wordSize];
            for(int i=0;i<arr[0].length;i++){
                this.wordSegments[i] =  (int) arr[0][i];
            }

            MLArray a = mlArrayRetrived.get("alphabet");
            double[][] arrA = ((MLDouble) a).getArray();
            this.alphabetSize = arrA[0].length;
            this.AlphabetCuts = new double[this.alphabetSize];
            for(int i=0;i<arrA[0].length;i++){
                this.AlphabetCuts[i] = arrA[0][i];
            }
            
            

            MLArray er = mlArrayRetrived.get("ErrorRate");
            double[][] ER = ((MLDouble) er).getArray();
            this.ErrorRate = ER[0][0];
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SAX.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public List<Integer> Cuts2Intervals(){
        List<Integer> intervals = new ArrayList<>();
        Integer[] CutIntervals = new Integer[2];
        CutIntervals[0] = 0;
        
        for(Integer wc: this.wordSegments){
            CutIntervals[1] = wc;
            intervals.add(CutIntervals[1]-CutIntervals[0]);
            CutIntervals[0] = wc;
        }
        
        return intervals;
    }
    
    public List<Integer> getTotalCuts() {
        List<Integer> list = new ArrayList<>();
        list.add(this.wordSize);
        for(int i = 0; i<this.wordSize;i++){
            list.add(this.alphabetSize);
        }
        return list;
    }
    
    public ReconstructedData Reconstruct(DiscretizedData ds_dis) {
        int attributes = this.wordSegments[this.wordSize-1]+1;
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
    
//    public static void main(String[] args) throws MyException, SAXException {
//        DataSet ds = new DataSet(10);
//        SAX sax = new SAX(10, 8);
////        MultiDataSet trans = oned_sax.Transform(data);
//        TimeSeriesDiscretize_source.symbols = Utils.Utils.getListSymbols();
//        DiscretizedData ds_dis = sax.Discretize(ds.getTrain());
//        System.out.println(ds_dis.toString());
//        
//        ReconstructedData rd = sax.Reconstruct(ds_dis);
//
//        Plotter plot = new Plotter(false, Marker.MarkerShape.CIRCLE, false, "Reconstruction", "Time", "Value");
//        plot.addData(ds.getOriginal().getValuesNorm(0, 1, ds.getOriginal().getDimensions()[1]), Color.BLUE);
//        plot.addData(rd.getValues(0, 1, rd.getDimensions()[1]), Color.RED);
//        plot.plot("Reconstruction");
//    }

  /**
   * Reads timeseries from a file. Assumes that file has a single double value on every line.
   * Assigned timestamps are the line numbers.
   * 
   * @param filename The file to read from.
   * @param columnIdx The column index.
   * @param sizeLimit The number of lines to read, 0 == all.
   * @return data.
   * @throws IOException if error occurs.
   * @throws SAXException if error occurs.
   */
//  public static double[] readFileColumn(String filename, int columnIdx, int sizeLimit)
//      throws IOException, SAXException {
//
//    // make sure the path exists
//    Path path = Paths.get(filename);
//    if (!(Files.exists(path))) {
//      throw new SAXException("unable to load data - data source not found.");
//    }
//
//    BufferedReader br = new BufferedReader(
//        new InputStreamReader(new FileInputStream(filename), "UTF-8"));
//
//    return readTS(br, columnIdx, sizeLimit);
//  }

  /**
   * Reads timeseries from a file. Assumes that file has a single double value on every line.
   * Assigned timestamps are the line numbers.
   * 
   * @param br The reader to use.
   * @param columnIdx The column index.
   * @param sizeLimit The number of lines to read, 0 == all.
   * @return data.
   * @throws IOException if error occurs.
   * @throws SAXException if error occurs.
   */
//  public static double[] readTS(BufferedReader br, int columnIdx, int sizeLimit)
//      throws IOException, SAXException {
//    ArrayList<Double> preRes = new ArrayList<>();
//    int lineCounter = 0;
//
//    String line = null;
//    while ((line = br.readLine()) != null) {
//      String[] split = line.trim().split("\\s+");
//      if (split.length < columnIdx) {
//        String message = "Unable to read data from column " + columnIdx;
//        br.close();
//        throw new SAXException(message);
//      }
//      String str = split[columnIdx];
//      double num = Double.NaN;
//      try {
//        num = Double.valueOf(str);
//      }
//      catch (NumberFormatException e) {
//        LOGGER.info("Skipping the row " + lineCounter + " with value \"" + str + "\"");
//        continue;
//      }
//      preRes.add(num);
//      lineCounter++;
//      if ((0 != sizeLimit) && (lineCounter >= sizeLimit)) {
//        break;
//      }
//    }
//    br.close();
//    double[] res = new double[preRes.size()];
//    for (int i = 0; i < preRes.size(); i++) {
//      res[i] = preRes.get(i);
//    }
//    return res;
//
//  }

  /**
   * Read at least N elements from the one-column file.
   * 
   * @param dataFileName the file name.
   * @param loadLimit the load limit.
   * @return the read data or empty array if nothing to load.
   * @throws SAXException if error occurs.
   * @throws IOException if error occurs.
   */
//  public double[] readTS(String dataFileName, int loadLimit) throws SAXException, IOException {
//
//    Path path = Paths.get(dataFileName);
//    if (!(Files.exists(path))) {
//      throw new SAXException("unable to load data - data source not found.");
//    }
//
//    BufferedReader reader = Files.newBufferedReader(path, DEFAULT_CHARSET);
//
//    return readTS(reader, 0, loadLimit);
//
//  }

  /**
   * Finds the maximal value in timeseries.
   * 
   * @param series The timeseries.
   * @return The max value.
   */
//  public double max(double[] series) {
//    double max = Double.MIN_VALUE;
//    for (int i = 0; i < series.length; i++) {
//      if (max < series[i]) {
//        max = series[i];
//      }
//    }
//    return max;
//  }

  /**
   * Finds the minimal value in timeseries.
   * 
   * @param series The timeseries.
   * @return The min value.
   */
//  public double min(double[] series) {
//    double min = Double.MAX_VALUE;
//    for (int i = 0; i < series.length; i++) {
//      if (min > series[i]) {
//        min = series[i];
//      }
//    }
//    return min;
//  }

  /**
   * Computes the mean value of timeseries.
   * 
   * @param series The timeseries.
   * @return The mean value.
   */
//  public double mean(int[] series) {
//    double res = 0D;
//    int count = 0;
//    for (int tp : series) {
//      res += (double) tp;
//      count += 1;
//
//    }
//    if (count > 0) {
//      return res / ((Integer) count).doubleValue();
//    }
//    return Double.NaN;
//  }

  /**
   * Computes the median value of timeseries.
   * 
   * @param series The timeseries.
   * @return The median value.
   */
//  public double median(double[] series) {
//    double[] clonedSeries = series.clone();
//    Arrays.sort(clonedSeries);
//
//    double median;
//    if (clonedSeries.length % 2 == 0) {
//      median = (clonedSeries[clonedSeries.length / 2]
//          + (double) clonedSeries[clonedSeries.length / 2 - 1]) / 2;
//    }
//    else {
//      median = clonedSeries[clonedSeries.length / 2];
//    }
//    return median;
//  }

  /**
   * Compute the variance of timeseries.
   * 
   * @param series The timeseries.
   * @return The variance.
   */
//  public double var(double[] series) {
//    double res = 0D;
//    double mean = mean(series);
//    int count = 0;
//    for (double tp : series) {
//      res += (tp - mean) * (tp - mean);
//      count += 1;
//    }
//    if (count > 0) {
//      return res / ((Integer) (count - 1)).doubleValue();
//    }
//    return Double.NaN;
//  }

  /**
   * Get mapping of a number to char.
   * 
   * @param value the value to map.
   * @param cuts the array of intervals.
   * @return character corresponding to numeric value.
   */
//  public char num2char(double value, double[] cuts) {
//    int count = 0;
//    while ((count < cuts.length) && (cuts[count] <= value)) {
//      count++;
//    }
//    return ALPHABET[count];
//  }
  
  /**
   * Converts index into char.
   * 
   * @param idx The index value.
   * @return The char by index.
   */
//  public char num2char(int idx) {
//    return ALPHABET[idx];
//  }

  

  /**
   * Extract subseries out of series.
   * 
   * @param series The series array.
   * @param start the fragment start.
   * @param end the fragment end.
   * @return The subseries.
   * @throws IndexOutOfBoundsException If error occurs.
   */
//  public double[] subseriesByCopy(double[] series, int start, int end)
//      throws IndexOutOfBoundsException {
//    if ((start > end) || (start < 0) || (end > series.length)) {
//      throw new IndexOutOfBoundsException("Unable to extract subseries, series length: "
//          + series.length + ", start: " + start + ", end: " + String.valueOf(end - start));
//    }
//    return Arrays.copyOfRange(series, start, end);
//  }

  /**
   * Prettyfies the timeseries for screen output.
   * 
   * @param series the data.
   * @param df the number format to use.
   * 
   * @return The timeseries formatted for screen output.
   */
//  public String seriesToString(double[] series, NumberFormat df) {
//    StringBuilder sb = new StringBuilder();
//    sb.append('[');
//    for (double d : series) {
//      sb.append(df.format(d)).append(',');
//    }
//    sb.delete(sb.length() - 2, sb.length() - 1).append("]");
//    return sb.toString();
//  }

  /**
   * Normalizes data in interval 0-1.
   * 
   * @param data the dataset.
   * @return normalized dataset.
   */
//  public double[] normOne(double[] data) {
//    double[] res = new double[data.length];
//    double max = max(data);
//    for (int i = 0; i < data.length; i++) {
//      res[i] = data[i] / max;
//    }
//    return res;
//  }
  
  /**
   * Calculate the alphabet AlphabetCuts based on Normal distribution
   * @return The alphabet AlphabetCuts
   */
    

}
