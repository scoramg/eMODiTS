/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Results;

import DataSets.Data;
import DataSets.DataSet;
import DataSets.DiscretizedData;
//import DataSets.DiscretizedDataSet;
import DataSets.ReconstructedData;
import Exceptions.MyException;
import Individuals.PEVOMO.Scheme;
import SAX.ESAX.ESAX;
import SAX.ESAXKMeans.ESAXKMeans;
import SAX.OneD_SAX.OneD_SAX;
import SAX.RKmeans.RKMeans;
import SAX.SAX;
import SAX.SAXKMeans.SAXKMeans;
import SAX.aSAX.aSAX;
import SAX.rSAX.rSAX;
import SimilarityFunctions.DTWSimilarity;
import SimilarityFunctions.LCSSDistance;
import TD4C.CosineDistance;
import Utils.Utils;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import TD4C.TD4C;


/**
 *
 * @author amarquezgr
 */
public class InfoLossResults {
    private String[][] infoloss;

    public InfoLossResults() {
        TimeSeriesDiscretize.TimeSeriesDiscretize_source.symbols = Utils.getListSymbols();
    }
    
    public static double[][] convert2matrix(double[] ts1, double[] ts2) throws MyException{
        
        if(ts1.length != ts2.length){
            throw new MyException("Lengths are differents");
        } else {
            double[][] matrix = new double[ts1.length][2];
            for(int i =0;i<ts1.length; i++){
                matrix[i][0] = ts1[i];
                matrix[i][1] = ts2[i];
            }
            return matrix;
        }
    }
    
    public double CalculateMetric(int metric, String method, DataSet ds, String Location, String Selector) throws IOException, MyException{
//        System.out.println(System.getProperty("user.dir"));
//        System.out.println("/"+Location+"/"+ method +"/"+ds.getName());

        String approach = method;
        
        if(method.equals("EP")) approach = "PEVOMO";
        if(method.equals("1D-SAX")) approach = "OneD-SAX";
        if(method.equals("TD4C_Cos")) approach = "TD4C_Cosine";

//        String ruta = Utils.findDirectory(System.getProperty("user.dir"), Location)+"/"+ approach +"/"+ds.getName()+"/";
//        String rutaDis = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", approach) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + method;
        String SelectorName = getSelectorName(approach, Utils.findDirectory(System.getProperty("user.dir") + Location, approach) + "/" + ds.getName() + "/"+file, Selector);
        String FileBest = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", approach) + "/" + ds.getName() + "/" + ds.getName()+"_"+approach+"_"+SelectorName+"B.mat"; 
        DiscretizedData ds_dis = getDiscretizedTimeSeries(approach, ds.getName(), SelectorName);
        ReconstructedData rd = new ReconstructedData();
        switch(method){
            case "MODiTS":
                Individuals.Proposal.MOScheme modits = new Individuals.Proposal.MOScheme();
                modits.ImportFromMatFile(FileBest, ds.getOriginal().getDimensions()[1]);
                rd = modits.Reconstruct(ds_dis);
                break;
            case "Proposal Multiobjective":
                Individuals.Proposal.MOScheme proposal = new Individuals.Proposal.MOScheme();
                proposal.ImportFromMatFile(FileBest, ds.getOriginal().getDimensions()[1]);
                rd = proposal.Reconstruct(ds_dis);
                break;
            case "EP":
                Scheme EP = new Scheme();
                EP.ImportFromMatFile(FileBest, ds.getOriginal().getDimensions()[1]);
                rd = EP.Reconstruct(ds_dis);
                break;
            case "SAX":
                SAX sax = new SAX();
                sax.ImportFromMatFile(FileBest, ds.getOriginal().getDimensions()[1]);
                rd = sax.Reconstruct(ds_dis);
                break;
            case "aSAX":
                aSAX asax = new aSAX();
                asax.ImportFromMatFile(FileBest, ds.getOriginal().getDimensions()[1]);
                rd = asax.Reconstruct(ds_dis);
                break;
            case "EPMO":
                Individuals.PEVOMO.MOScheme epmo = new Individuals.PEVOMO.MOScheme();
                epmo.ImportFromMatFile(FileBest, ds.getOriginal().getDimensions()[1]);
                rd = epmo.Reconstruct(ds_dis);
                break;
            case "ESAX":
                ESAX esax = new ESAX();
                esax.ImportFromMatFile(FileBest, ds.getOriginal().getDimensions()[1]);
                rd = esax.Reconstruct(ds_dis);        
                break; 
            case "ESAXKMeans": 
                ESAXKMeans esaxkmeans = new ESAXKMeans();
                esaxkmeans.ImportFromMatFile(FileBest, ds.getOriginal().getDimensions()[1]);
                rd = esaxkmeans.Reconstruct(ds_dis);
                break;    
            case "1D-SAX": 
                OneD_SAX onedsax = new OneD_SAX();
                onedsax.ImportFromMatFile(FileBest, ds.getOriginal().getDimensions()[1]);
                rd = onedsax.Reconstruct(ds_dis);
                break;  
            case "RKMeans": 
                RKMeans rkmeans = new RKMeans();
//                rkmeans.ImportFromMatFile(FileBest, ds.getOriginal().getDimensions()[1]);
                rd = rkmeans.Reconstruct(ds_dis);
                break; 
            case "SAXKMeans": 
                SAXKMeans saxkmeans = new SAXKMeans();
                saxkmeans.ImportFromMatFile(FileBest, ds.getOriginal().getDimensions()[1]);
                rd = saxkmeans.Reconstruct(ds_dis);
                break;    
            case "rSAX": 
                rSAX rsax = new rSAX();
                rsax.ImportFromMatFile(FileBest, ds.getOriginal().getDimensions()[1]);
                rd = rsax.Reconstruct(ds_dis);
                break;
            case "TD4C_Cos": 
                TD4C td4c = new TD4C(new CosineDistance(),0, 1.5);
                rd = td4c.Reconstruct(ds_dis);
                break;  
        }
//        System.out.println("ds.name:"+ds.getName()+", method:"+method);
        double value = 0;
        if (metric == 0) value = CalculatePearsonMetric(ds.getOriginal(), rd);
        if (metric == 1) value = CalculateLCSS(ds.getOriginal(), rd);
        return value;
    }
    
    public String getSelectorName(String method, String ruta, String Selector) throws IOException{
        
        
//        
//        MatFileReader mfr = new MatFileReader(dir + file + ".mat");
//        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
//        MLArray w = mlArrayRetrived.get("AccumulatedFrontFitness");
//        double[][] arr = ((MLDouble) w).getArray();
        
//        String file = BDName + "_" + method;
        String SelectorAbb = "";
        
        if(method.equals("MODiTS") || method.contains("Multiobjective")){
//            MatFileReader mfr = new MatFileReader(ruta + file + ".mat");
//            Map<String, MLArray> mlArrayRetrived = mfr.getContent();
//            MLArray w = mlArrayRetrived.get("AccumulatedFrontFitness");
//            double[][] arr = ((MLDouble) w).getArray();
            MatFileReader mfr = new MatFileReader(ruta + ".mat");
            Map<String, MLArray> mlArrayRetrived = mfr.getContent();
            MLArray w = mlArrayRetrived.get("AccumulatedFrontFitness");
            double[][] arr = ((MLDouble) w).getArray();

            switch(Selector){
                case "Knee": //Reference Point [0 0 0]
                    SelectorAbb += "Knee_";
                    break;
                case "Mean": //Reference Point Mean
                    SelectorAbb += "KMeans-1_";
                    break;
                case "Train-Test": //Trampa
                    SelectorAbb += "Knee_";
                    break;
                case "Train-CV"://Train - CV
                    SelectorAbb += "CTV_";
                    break;
                case "KMeans"://K-Means (k=20% del tamaño del cluster), and classification with TD
                    int k = (int) Math.floor(arr.length*0.20);
                    SelectorAbb += "KMeans-"+k+"_";
                    break;    
                default://Knee
                    SelectorAbb += "Knee_";
                    break;
            }
        }
//        return dir + BDName+"_"+method+"_"+SelectorAbb+"B.mat"; 
        return SelectorAbb;
    }
    
    public DiscretizedData getDiscretizedTimeSeries(String method, String BDName, String Selector) throws IOException{
        
        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", method) + "/" + BDName + "/";
        String file = BDName + "_" + method;
        
        MatFileReader mfr = new MatFileReader(dir + file + "_" + Selector + "B.mat");
//        mlArrayRetrived = mfr.getContent();
//        MLArray dis = mlArrayRetrived.get("ds_dis");
//        double[][] ds_dis_or = ((MLDouble) dis).getArray();
//        MLArray dis_train = mlArrayRetrived.get("ds_dis_train");
//        double[][] ds_dis_train = ((MLDouble) dis_train).getArray();
//        MLArray dis_test = mlArrayRetrived.get("ds_dis_test");
//        double[][] ds_dis_test = ((MLDouble) dis_test).getArray();
//        
////        String fileBest = getSelectorName(method, ruta, BDName, Selector);
//        MatFileReader mfr = new MatFileReader(fileBest);
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray w = mlArrayRetrived.get("ds_dis");
        double[][] ds_dis = ((MLDouble) w).getArray();
        DiscretizedData dd = new DiscretizedData(ds_dis.length, ds_dis[0].length);
        for(int i=0; i<ds_dis.length; i++){
            for(int a=0; a<ds_dis[0].length;a++){
                dd.addValue(i, a, (int) ds_dis[i][a], a==0);
            }
        }
        dd.convert2StringArray();
        return dd;
    }
    
    public double CalculatePearsonMetric(Data original, ReconstructedData reconstructed) throws MyException{
//        double measure = 0;
//        for(int i=0; i< original.getDimensions()[0];i++){
//            double[][] data = convert2matrix(original.getValues(i, 1, original.getDimensions()[1]), reconstructed.getValues(i, 1, reconstructed.getDimensions()[1]));
//            PearsonsCorrelation cp = new PearsonsCorrelation(data);
//            RealMatrix cm = cp.getCorrelationMatrix();
//            measure += cm.getEntry(0, 1);
//        }
//        return (double) measure/original.getDimensions()[0];
        double[][] data = convert2matrix(original.getMedia(true), reconstructed.getMedia());
        PearsonsCorrelation cp = new PearsonsCorrelation(data);
        RealMatrix cm = cp.getCorrelationMatrix();
        return 1-cm.getEntry(0, 1);
    }
    
    public double CalculateLCSS(Data original, ReconstructedData reconstructed){
        double epsilon = Math.min(mimath.MiMath.getDesviacionStandar(original.getMedia(true)), mimath.MiMath.getDesviacionStandar(reconstructed.getMedia()));
        int delta = (int) Math.ceil(Math.max(original.getMedia(true).length, reconstructed.getMedia().length)*0.2);
                
//        LCSS lcss = new LCSS(epsilon, delta);
//        return lcss.similarity(original.getMedia(true), reconstructed.getMedia());
        LCSSDistance lcss = new LCSSDistance(delta, epsilon);
        return lcss.distance(original.getMedia(true), reconstructed.getMedia());
    }
    
    public double CalculateDTW(Data original, ReconstructedData reconstructed){
        DTWSimilarity dtw = new DTWSimilarity();
        return dtw.measure(original.getMedia(true), reconstructed.getMedia());
    }
    
    public void Execute(int metric, String[] method, String Location, String Selector, String FileName) throws MyException, IOException{
        infoloss = new String[DataSet.NUMBER_OF_DATASETS-DataSet.DATASETS_IGNORED.size()][method.length+1];
        infoloss[0][0] = "DataSet";
        for(int j=0; j<method.length;j++){
            infoloss[0][j+1] = method[j];
        }
        
        for (int i = 1; i < DataSet.NUMBER_OF_DATASETS; i++) {
            if (!DataSet.DATASETS_IGNORED.contains(i)) {
                DataSet ds = new DataSet(i, false);
                System.out.println(ds.getName());
                infoloss[i][0] = ds.getName();
                for(int j=0; j<method.length;j++){
                    infoloss[i][j+1] = String.valueOf(this.CalculateMetric(metric, method[j], ds, Location, Selector));
                }
            }
        }
        
        StringBuilder csvtable = new StringBuilder();
        for(int i=0; i<this.infoloss.length;i++){
            for(int j=0; j<this.infoloss[0].length;j++){
                csvtable.append(this.infoloss[i][j]).append(",");
            }
            csvtable.deleteCharAt(csvtable.length()-1).append("\n");
        }
        System.out.println(csvtable.toString());
        try (BufferedWriter br = new BufferedWriter(new FileWriter(FileName))) {
            br.write(csvtable.toString());
        }
        
    }
    
    public static void main(String[] args) throws MyException, IOException{
//        DataSet ds = new DataSet(10);
////        ReconstructedData ds_recons = scheme.Reconstruct(ds_dis);
//        double[][] data = convert2matrix(ds.getOriginal().getData()[0], ds.getOriginal().getData()[49]);
//        PearsonsCorrelation cp = new PearsonsCorrelation(data);
//        RealMatrix cm = cp.getCorrelationMatrix();
//        System.out.println(cm.getEntry(0, 1));
//        System.out.println(cm.toString());
//        System.out.println(System.getProperty("user.dir"));
        InfoLossResults ilr = new InfoLossResults();
        String[] methods = {"MODiTS", "EP", "SAX", "aSAX", "ESAX", "ESAXKMeans", "1D-SAX","RKMeans","SAXKMeans","rSAX","TD4C_Cos"};
        String Selector = "Train-CV";
        String Location = "/e15p100g300/";
        ilr.Execute(1,methods, Location, Selector, "InfoLossLCSS.csv");
    }
}
