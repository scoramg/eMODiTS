/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataSets;

import Exceptions.MyException;
import Graphs.Marker;
import Graphs.Plotter;
import Utils.Utils;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import java.awt.Color;
import java.io.IOException; 
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 *
 * @author amarquezgr
 */
public class DataSet implements Cloneable {
//    public static final int NUMBER_OF_DATASETS = 89;
    public static final int NUMBER_OF_DATASETS = 91;
//    public static final List<Integer> DATASETS_IGNORED = new ArrayList<>();
//    public static final List<Integer> DATASETS_IGNORED = Arrays.asList(23,66);
//    public static final List<Integer> DATASETS_IGNORED = Arrays.asList(86,87,88,89,90);
//    public static final List<Integer> DATASETS_IGNORED = Arrays.asList(1,2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90);
    public static final List<Integer> DATASETS_IGNORED = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89);
    public static double[][] limits;
    private Data original;
    private Data train;
    private Data test;
//    private int noClasses;
//    private List<Integer> classes; 
    private String ruta_ucr;
    private String name;
    private int index;
//    private String subfix;
    private double minERFound;

    public double[][] getLimits() {
        return limits;
    }

    public String getRuta_ucr() {
        return ruta_ucr;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public Data getOriginal() {
        return original;
    }

    public Data getTrain() {
        return train;
    }

    public Data getTest() {
        return test;
    }

    public double getMinERFound() {
        return minERFound;
    }

    public void setMinERFound(double minERFound) {
        this.minERFound = minERFound;
    }
    
    public void setLimits(double[][] límites) {
        this.limits = límites.clone();
    }

    public void setRuta_ucr(String ruta_ucr) {
        this.ruta_ucr = ruta_ucr;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setOriginal(Data original) {
        this.original = original.clone();
    }

    public void setTrain(Data train) {
        this.train = train.clone();
    }

    public void setTest(Data test) {
        this.test = test.clone();
    }
    
//    public DataSet(int iDS) throws MyException {
//        this.ruta_ucr = Utils.findDirectory(System.getProperty("user.dir"), "Datasets")+"/";
//        String bd = DataSet.getUCRRepository(iDS);
//        this.setName(bd);
//        this.setIndex(iDS);
//        this.original = new Data();
//        this.train = new Data();
//        this.test = new Data();
//        if(iDS>0){
//
//            String file =  this.ruta_ucr + bd +"/"+bd+".mat";  
//            
//            try {
//                MatFileReader matfilereader = new MatFileReader(file);
//                this.limits = ((MLDouble) matfilereader.getMLArray("limites")).getArray(); 
//                this.original.load(matfilereader.getMLArray(bd));
//                this.train.load(matfilereader.getMLArray(bd+"_TRAIN"));
//                this.test.load(matfilereader.getMLArray(bd+"_TEST"));
//            } catch (IOException ex) {
//                Logger.getLogger(DataSet.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            this.limits = new double[1][2];
//        }
//    }
    
    public DataSet(int iDS, boolean isSmoothed) throws MyException {
        this.ruta_ucr = Utils.findDirectory(System.getProperty("user.dir"), "Datasets")+"/";
        String bd = DataSet.getUCRRepository(iDS);
        this.setName(bd);
        this.setIndex(iDS);
        this.original = new Data();
        this.train = new Data();
        this.test = new Data();
        if(iDS>0){
            String subf = "";
            if(isSmoothed) {
                subf = "S";
            }
            String file =  this.ruta_ucr + bd +"/"+bd+subf+".mat";  
            
            try {
                MatFileReader matfilereader = new MatFileReader(file);
                this.limits = ((MLDouble) matfilereader.getMLArray("limites"+subf)).getArray(); 
                this.original.load(matfilereader.getMLArray(bd+subf));
                this.train.load(matfilereader.getMLArray(bd+subf+"_TRAIN"));
                this.test.load(matfilereader.getMLArray(bd+subf+"_TEST"));
            } catch (IOException ex) {
                Logger.getLogger(DataSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.limits = new double[1][2];
        }
    }

    public DataSet() {
    }
    
    public static String getUCRRepository(int iDS){
        String nombre = "";
        switch(iDS){
            case 0: nombre = "All Databases"; break;  
            case 1: nombre = "Adiac"; break; 
            case 2: nombre = "ArrowHead"; break; 
            case 3: nombre = "Beef"; break;
            case 4: nombre = "BeetleFly"; break;
            case 5: nombre = "BirdChicken"; break;
            case 6: nombre = "Car"; break;
            case 7: nombre = "CBF"; break;
            case 8: nombre = "ChlorineConcentration"; break;
            case 9: nombre = "CinCECGtorso"; break;
            case 10: nombre = "Coffee"; break;
            case 11: nombre = "Computers"; break;
            case 12: nombre = "CricketX"; break;
            case 13: nombre = "CricketY"; break;
            case 14: nombre = "CricketZ"; break;
            case 15: nombre = "DiatomSizeReduction"; break;
            case 16: nombre = "DistalPhalanxOutlineAgeGroup"; break;
            case 17: nombre = "DistalPhalanxOutlineCorrect"; break;
            case 18: nombre = "DistalPhalanxTW"; break;
            case 19: nombre = "Earthquakes"; break;
            case 20: nombre = "ECG200"; break;
            case 21: nombre = "ECG5000"; break;
            case 22: nombre = "ECGFiveDays"; break;
            case 23: nombre = "ElectricDevices"; break;
            case 24: nombre = "FaceAll"; break;
            case 25: nombre = "FaceFour"; break;
            case 26: nombre = "FacesUCR"; break;
            case 27: nombre = "FiftyWords"; break;
            case 28: nombre = "Fish"; break;
            case 29: nombre = "FordA"; break;
            case 30: nombre = "FordB"; break;
            case 31: nombre = "GunPoint"; break; 
            case 32: nombre = "Ham"; break; 
            case 33: nombre = "HandOutlines"; break; 
            case 34: nombre = "Haptics"; break; 
            case 35: nombre = "Herring"; break;
            case 36: nombre = "InlineSkate"; break;
            case 37: nombre = "InsectWingbeatSound"; break;
            case 38: nombre = "ItalyPowerDemand"; break;
            case 39: nombre = "LargeKitchenAppliances"; break;
            case 40: nombre = "Lighting2"; break;
            case 41: nombre = "Lighting7"; break;
            case 42: nombre = "Mallat"; break;
            case 43: nombre = "Meat"; break;
            case 44: nombre = "MedicalImages"; break;
            case 45: nombre = "MiddlePhalanxOutlineAgeGroup"; break;
            case 46: nombre = "MiddlePhalanxOutlineCorrect"; break;
            case 47: nombre = "MiddlePhalanxTW"; break;
            case 48: nombre = "MoteStrain"; break;
            case 49: nombre = "NonInvasiveFetalECGThorax1"; break;
            case 50: nombre = "NonInvasiveFetalECGThorax2"; break;
            case 51: nombre = "OliveOil"; break;
            case 52: nombre = "OSULeaf"; break;
            case 53: nombre = "PhalangesOutlinesCorrect"; break;
            case 54: nombre = "Phoneme"; break;
            case 55: nombre = "Plane"; break;
            case 56: nombre = "ProximalPhalanxOutlineAgeGroup"; break;
            case 57: nombre = "ProximalPhalanxOutlineCorrect"; break;
            case 58: nombre = "ProximalPhalanxTW"; break;
            case 59: nombre = "RefrigerationDevices"; break;
            case 60: nombre = "ScreenType"; break;
            case 61: nombre = "ShapeletSim"; break;
            case 62: nombre = "ShapesAll"; break;
            case 63: nombre = "SmallKitchenAppliances"; break;
            case 64: nombre = "SonyAIBORobotSurface1"; break;
            case 65: nombre = "SonyAIBORobotSurface2"; break;
            case 66: nombre = "StarLightCurves"; break;
            case 67: nombre = "Strawberry"; break;
            case 68: nombre = "SwedishLeaf"; break;
            case 69: nombre = "Symbols"; break;
            case 70: nombre = "SyntheticControl"; break;
            case 71: nombre = "ToeSegmentation1"; break;
            case 72: nombre = "ToeSegmentation2"; break;
            case 73: nombre = "Trace"; break;
            case 74: nombre = "TwoLeadECG"; break;
            case 75: nombre = "TwoPatterns"; break; 
            case 76: nombre = "UWaveGestureLibraryAll"; break; 
            case 77: nombre = "UWaveGestureLibraryX"; break; 
            case 78: nombre = "UWaveGestureLibraryY"; break; 
            case 79: nombre = "UWaveGestureLibraryZ"; break; 
            case 80: nombre = "Wafer"; break;
            case 81: nombre = "Wine"; break;
            case 82: nombre = "WordSynonyms"; break;
            case 83: nombre = "Worms"; break;
            case 84: nombre = "WormsTwoClass"; break;
            case 85: nombre = "Yoga"; break; 
            case 86: nombre = "ColposcopiaH"; break;
            case 87: nombre = "BreastCancer"; break;
            case 88: nombre = "BreastCancerBin"; break;
            case 89: nombre = "Precipitacion"; break;
            case 90: nombre = "BeansCL"; break;
                
        }
        return nombre;
    }
    
    
    public void getMinMisclassificationRateFound(int iDS){
        double best_accuracy = 0;
        switch(iDS){
            case 1: best_accuracy = 0.8098; break; //Adiac
            case 2: best_accuracy = 0.8187; break; //Beef
            case 3: best_accuracy = 0.9981; break; //CBF
            case 4: best_accuracy = 0.9996; break; //Coffee 
            case 5: best_accuracy = 0.8905; break; //ECG200
            case 6: best_accuracy = 0.9956; break; //FaceFour
            case 7: best_accuracy = 0.8207; break; //FiftyWords
            case 8: best_accuracy = 0.9742; break; //Fish
            case 9: best_accuracy = 0.9987; break; //GunPoint   
            case 10: best_accuracy = 0.8370; break; //Lighting2
            case 11: best_accuracy = 0.7995; break; //Lighting7
            case 12: best_accuracy = 0.9013; break; //OliveOil
            case 13: best_accuracy = 0.9674; break;// OSULeaf
            case 14: best_accuracy = 0.9667; break; //SwedishLeaf
            case 15: best_accuracy = 0.9992; break; //SyntheticControl
            case 16: best_accuracy = 0.9999; break; //Trace
            case 17: best_accuracy = 1; break; //TwoPatterns
            case 18: best_accuracy = 0.9998; break; //Wafer
            case 19: best_accuracy = 0.9099; break; //Yoga
            default: best_accuracy = Double.NaN;
        }
        if (Double.isNaN(best_accuracy)){
            this.minERFound = best_accuracy;
        } else {
            this.minERFound = 1-best_accuracy;
        }
    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        for(int i=0; i<this.dimensions[0];i++){
//            for(int j=0;j<this.dimensions[1];j++){
//                sb.append(data[i][j]).append(" ");
//            }
//            sb.append("\n");
//        }
//        return sb.toString();
//    }
    
    public static String PrintAllDataSetNames(){
        StringBuilder sb = new StringBuilder();
        for(int i=1;i<NUMBER_OF_DATASETS;i++){
            if(!DataSet.DATASETS_IGNORED.contains(i)){
                sb.append("'").append(getUCRRepository(i)).append("'").append(",");
            }
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
    
    @Override
    public DataSet clone() {
        try {
            super.clone();
            DataSet clon = new DataSet();
            
            clon.setLimits(this.limits);
            clon.setOriginal(this.original);
            clon.setTrain(this.train);
            clon.setTest(this.test);
//            clon.setDimensions(this.dimensions);
            
            
            clon.setName(this.name);
            clon.setRuta_ucr(this.ruta_ucr);
            clon.setIndex(this.index);
//            clon.setSubfix(this.subfix);
            clon.setMinERFound(this.minERFound);
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DataSet.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /** This is the continuous analog of symmetric random walk, each increment y(s+t)-y(s) is Gaussian 
     * with distribution N(0,t^2) and increments over disjoint intervals are independent. It is typically 
     * simulated as an approximating random walk in discrete time. 
     * 
     * @param instances Number of instances of the dataset
     * @param ts_length size of the time series
     * @param sigma
     * @return 
     */
    public static double[][] RandomWalk(int instances, int ts_length, double sigma){

        double[][] data = new double[instances][ts_length];
        NormalDistribution distribution = new NormalDistribution(0, sigma);
        for(int i=0;i<instances;i++){
            data[i][0] = 0;
            for (int j=1; j<ts_length; j++){
                double number = distribution.sample();
                data[i][j] = data[i][j-1] + number; 
            }
        }
        return data;
    }
    
    public static double[][] getSubDataSet(double[][] data, double porcentage){  
        int new_instances = (int)(porcentage * data.length);
        
        double[][] subset = new double[new_instances][data[0].length];
        
        Object[] indexs = mimath.MiMath.randomIntArray(new_instances, data.length-1);
        
        for(int i=0;i<indexs.length;i++){
            subset[i] = data[(int) indexs[i]];
        }
        
        return subset;
    }
    
    public void destroy(){
        limits = null;
        original.destroy();
        train.destroy();
        test.destroy();
        ruta_ucr = null;
        name = null;
    }
    
    public static void main(String[] args) {
        try {
            DataSet ds = new DataSet(10,false);
            Plotter plotter = new Plotter(false, Marker.MarkerShape.CIRCLE, false, "Normalize", "", "");
            for(int i=0; i<ds.getTrain().getDimensions()[0]; i++){
                double[] data = Arrays.copyOfRange(ds.getTrain().getNormalized()[i], 1, ds.getTrain().getNormalized()[i].length-1);
                
                plotter.addData(data, Color.BLUE);
            }
            plotter.plot("Normalized");
//            System.out.println("Train:");
//            System.out.println(ds.getTrain().toString());
//            System.out.println("Test:");
//            System.out.println(ds.getTest().toString());
//            System.out.println("Original:");
//            System.out.println(ds.getOriginal().toString());
//            for(Integer klass: ds.getTrain().getNoInstancesPerClass().keySet()){
//                System.out.println(klass+":"+ds.getTrain().getNoInstancesPerClass().get(klass));
//            }
//            
//            for(Integer klass: ds.getTrain().getClasses()){
//                System.out.println("klass:"+klass);
//            }
        } catch (MyException ex) {
            Logger.getLogger(DataSet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


