/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.TexasSharpshooter;

import DataSets.DataSet;
import DataSets.DiscretizedData;
import DataSets.DiscretizedDataSet;
import Interfaces.IScheme;
import ParetoFront.ClassificationSelector;
import ParetoFront.ClusteringSelector;
import ParetoFront.ReferencePointSelector;
import ParetoFront.iSelectors;
import RAW.RAW;
import SAX.ESAX.ESAX;
import SAX.ESAXKMeans.ESAXKMeans;
import SAX.OneD_SAX.OneD_SAX;
import SAX.RKmeans.RKMeans;
import SAX.SAX;
import SAX.SAXException;
import SAX.SAXKMeans.SAXKMeans;
import SAX.aSAX.aSAX;
import SAX.rSAX.rSAX;
import TD4C.CosineDistance;
import TD4C.EntropyDistance;
import TD4C.IDistanceMeasure;
import TD4C.TD4C;
import Utils.Utils;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author amarquezgr
 */
public class TexasSharpshooterMethods {
    private Map<String, Double> misclassification;
    private String type_selection, Location, method;
    private double expected, actual;
    
    public TexasSharpshooterMethods(String type_selection, String location, String Method, DataSet ds) throws IOException, Exception {
        this.misclassification = new HashMap<>();
        this.type_selection=type_selection;
        this.Location = location;
        this.method = Method;
        
        expected = 0.0;
        actual = 0.0;
        
        switch(Method){
            case "MODiTS":
                addMODiTS(ds,0);
                break;
            case "Proposal Multiobjective":
                addMODiTS(ds,1);
                break;    
            case "EP":
                addEP(ds);
                break;
            case "SAX":
                addSAX(ds);
                break;
            case "aSAX":
                addAlphaSAX(ds);
                break;    
            case "ESAX":
                addESAX(ds);
                break;   
            case "ESAXKMeans":
                addESAXKMeans(ds);
                break;  
            case "1D-SAX":
                add1dSAX(ds);
                break;   
            case "RKMeans":
                addRKMeans(ds);
                break;     
            case "SAXKMeans":
                addSAXKMeans(ds);
                break;    
            case "rSAX":
                addRSAX(ds);
                break;  
            case "TD4C-Ent":
                addTD4C(ds, new EntropyDistance());
                break;    
            case "TD4C-Cos":
                addTD4C(ds, new CosineDistance());
                break;
            case "TD4C-KB":
                addTD4C(ds, new EntropyDistance());
                break; 
            case "RAW":
                addRAW(ds);
                break;      
        }
        
    }
    
    public Double getValue(String key){
        return misclassification.get(key);
    }

    public String getMethod() {
        return method;
    }

    public String getLocation() {
        return Location;
    }

    public String getType_selection() {
        return type_selection;
    }

    public double getExpected() {
        return expected;
    }

    public double getActual() {
        return actual;
    }

    public void CalculateRatios(TexasSharpshooterMethods base){
        if(this.getValue("train") == 0) {
            this.expected = 0;
        } else {
            this.expected = (double) base.getValue("train")/this.getValue("train"); 
        }
        if(this.getValue("test") == 0){
            this.actual = 0;
        } else {
            this.actual = (double) base.getValue("test")/this.getValue("test"); 
        }
    }
    
    public void addMODiTS(DataSet ds, int type) throws IOException {
        IScheme individual = new Individuals.Proposal.MOScheme();
        String folder = "MODiTS";
        if (type == 1) {
            //individual = new Individuals.Proposal.MOScheme();
            folder = "Proposal Multiobjective";
        } 
        
        String dir = Utils.findDirectory(System.getProperty("user.dir") + Location, folder) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + folder;
        
        MatFileReader mfr = new MatFileReader(dir + file + ".mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray w = mlArrayRetrived.get("AccumulatedFrontFitness");
        double[][] arr = ((MLDouble) w).getArray();
        
        iSelectors selector;
        switch (type_selection) {
            case "Knee": //Reference Point [0 0 0]
                selector = new ReferencePointSelector(0);
                break;
            case "Mean": //Reference Point Mean
                selector = new ClusteringSelector(1, 0); //ReferencePointSelector(arr, 1);
                break;
            case "Train-Test": //Trampa
                selector = new ClassificationSelector(true);
                break;
            case "Train-CV"://Train - CV
                selector = new ClassificationSelector(false);
                break;
            case "KMeans"://K-Means (k=20% del tamaño del cluster), and classification with TD
                int k = (int) Math.floor(arr.length*0.20);
                selector = new ClusteringSelector(k,0); 
                break;    
            default://Reference Point [0 0 0]
                selector = new ReferencePointSelector(0);
                break;
        }
        
        dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", folder) + "/" + ds.getName() + "/";
        file = ds.getName() + "_" + folder;
        
        mfr = new MatFileReader(dir + file + "_" + selector.getName() + "_B.mat");
        mlArrayRetrived = mfr.getContent();
        MLArray dis = mlArrayRetrived.get("ds_dis");
        double[][] ds_dis_or = ((MLDouble) dis).getArray();
        MLArray dis_train = mlArrayRetrived.get("ds_dis_train");
        double[][] ds_dis_train = ((MLDouble) dis_train).getArray();
        MLArray dis_test = mlArrayRetrived.get("ds_dis_test");
        double[][] ds_dis_test = ((MLDouble) dis_test).getArray();
        
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        DiscretizedData dd_or = new DiscretizedData(ds_dis_or.length, ds_dis_or[0].length);
        dd_or.importData(ds_dis_or);
        DiscretizedData dd_train = new DiscretizedData(ds_dis_train.length, ds_dis_train[0].length);
        dd_train.importData(ds_dis_train);
        DiscretizedData dd_test = new DiscretizedData(ds_dis_test.length, ds_dis_test[0].length);
        dd_test.importData(ds_dis_test);
        
        ds_dis.setOriginal(dd_or);
        ds_dis.setTrain(dd_train);
        ds_dis.setTest(dd_test);
        
        individual.Classify(ds_dis, false,"train");
        misclassification.put("train", individual.getErrorRate());
        
        individual.Classify(ds_dis, false,"test");        
        misclassification.put("test", individual.getErrorRate()); 
        
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
    }
    
    public void addEP(DataSet ds) throws IOException {
        IScheme individual = new Individuals.PEVOMO.Scheme();
        individual.empty();
        
        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", individual.getName()) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + individual.getName();
        
        MatFileReader mfr = new MatFileReader(dir + file + "_B.mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray dis = mlArrayRetrived.get("ds_dis");
        double[][] ds_dis_or = ((MLDouble) dis).getArray();
        MLArray dis_train = mlArrayRetrived.get("ds_dis_train");
        double[][] ds_dis_train = ((MLDouble) dis_train).getArray();
        MLArray dis_test = mlArrayRetrived.get("ds_dis_test");
        double[][] ds_dis_test = ((MLDouble) dis_test).getArray();
        
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        DiscretizedData dd_or = new DiscretizedData(ds_dis_or.length, ds_dis_or[0].length);
        dd_or.importData(ds_dis_or);
        DiscretizedData dd_train = new DiscretizedData(ds_dis_train.length, ds_dis_train[0].length);
        dd_train.importData(ds_dis_train);
        DiscretizedData dd_test = new DiscretizedData(ds_dis_test.length, ds_dis_test[0].length);
        dd_test.importData(ds_dis_test);
        
        ds_dis.setOriginal(dd_or);
        ds_dis.setTrain(dd_train);
        ds_dis.setTest(dd_test);
        
        individual.Classify(ds_dis, false,"train");
        misclassification.put("train", individual.getErrorRate());
        
        individual.Classify(ds_dis, false,"test");        
        misclassification.put("test", individual.getErrorRate()); 
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
    }

    public void addSAX(DataSet ds) throws IOException, SAXException, Exception {
        SAX sax = new SAX();
        
        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", sax.getName()) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + sax.getName();
        
        MatFileReader mfr = new MatFileReader(dir + file + "_B.mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray dis = mlArrayRetrived.get("ds_dis");
        double[][] ds_dis_or = ((MLDouble) dis).getArray();
        MLArray dis_train = mlArrayRetrived.get("ds_dis_train");
        double[][] ds_dis_train = ((MLDouble) dis_train).getArray();
        MLArray dis_test = mlArrayRetrived.get("ds_dis_test");
        double[][] ds_dis_test = ((MLDouble) dis_test).getArray();
        
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        DiscretizedData dd_or = new DiscretizedData(ds_dis_or.length, ds_dis_or[0].length);
        dd_or.importData(ds_dis_or);
        DiscretizedData dd_train = new DiscretizedData(ds_dis_train.length, ds_dis_train[0].length);
        dd_train.importData(ds_dis_train);
        DiscretizedData dd_test = new DiscretizedData(ds_dis_test.length, ds_dis_test[0].length);
        dd_test.importData(ds_dis_test);
        
        ds_dis.setOriginal(dd_or);
        ds_dis.setTrain(dd_train);
        ds_dis.setTest(dd_test);

        sax.Classify(ds_dis, false,"train");
        misclassification.put("train", sax.getErrorRate());
        
        sax.Classify(ds_dis, false,"test");        
        misclassification.put("test", sax.getErrorRate());   
        
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
        ds_dis.destroy();
    }

    public void addAlphaSAX(DataSet ds) throws IOException, SAXException, Exception {
        aSAX asax = new aSAX();
        
        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", asax.getName()) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + asax.getName();
        
        MatFileReader mfr = new MatFileReader(dir + file + "_B.mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray dis = mlArrayRetrived.get("ds_dis");
        double[][] ds_dis_or = ((MLDouble) dis).getArray();
        MLArray dis_train = mlArrayRetrived.get("ds_dis_train");
        double[][] ds_dis_train = ((MLDouble) dis_train).getArray();
        MLArray dis_test = mlArrayRetrived.get("ds_dis_test");
        double[][] ds_dis_test = ((MLDouble) dis_test).getArray();
        
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        DiscretizedData dd_or = new DiscretizedData(ds_dis_or.length, ds_dis_or[0].length);
        dd_or.importData(ds_dis_or);
        DiscretizedData dd_train = new DiscretizedData(ds_dis_train.length, ds_dis_train[0].length);
        dd_train.importData(ds_dis_train);
        DiscretizedData dd_test = new DiscretizedData(ds_dis_test.length, ds_dis_test[0].length);
        dd_test.importData(ds_dis_test);
        
        ds_dis.setOriginal(dd_or);
        ds_dis.setTrain(dd_train);
        ds_dis.setTest(dd_test);
       
        asax.Classify(ds_dis, false,"train");
        misclassification.put("train", asax.getErrorRate());
        
        asax.Classify(ds_dis, false,"test");        
        misclassification.put("test", asax.getErrorRate()); 
        
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
    }
    
    public void addESAX(DataSet ds) throws IOException, SAXException, Exception {
        ESAX esax = new ESAX();
        
        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", esax.getName()) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + esax.getName();
        
        MatFileReader mfr = new MatFileReader(dir + file + "_B.mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray dis = mlArrayRetrived.get("ds_dis");
        double[][] ds_dis_or = ((MLDouble) dis).getArray();
        MLArray dis_train = mlArrayRetrived.get("ds_dis_train");
        double[][] ds_dis_train = ((MLDouble) dis_train).getArray();
        MLArray dis_test = mlArrayRetrived.get("ds_dis_test");
        double[][] ds_dis_test = ((MLDouble) dis_test).getArray();
        
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        DiscretizedData dd_or = new DiscretizedData(ds_dis_or.length, ds_dis_or[0].length);
        dd_or.importData(ds_dis_or);
        DiscretizedData dd_train = new DiscretizedData(ds_dis_train.length, ds_dis_train[0].length);
        dd_train.importData(ds_dis_train);
        DiscretizedData dd_test = new DiscretizedData(ds_dis_test.length, ds_dis_test[0].length);
        dd_test.importData(ds_dis_test);
        
        ds_dis.setOriginal(dd_or);
        ds_dis.setTrain(dd_train);
        ds_dis.setTest(dd_test);
        
        esax.Classify(ds_dis, false,"train");
        misclassification.put("train", esax.getErrorRate());
        
        esax.Classify(ds_dis, false,"test");        
        misclassification.put("test", esax.getErrorRate()); 
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
    }
    
    public void addESAXKMeans(DataSet ds) throws IOException, SAXException, Exception {
        ESAXKMeans esaxkmeans = new ESAXKMeans();
        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", esaxkmeans.getName()) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + esaxkmeans.getName();
        
        MatFileReader mfr = new MatFileReader(dir + file + "_B.mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray dis = mlArrayRetrived.get("ds_dis");
        double[][] ds_dis_or = ((MLDouble) dis).getArray();
        MLArray dis_train = mlArrayRetrived.get("ds_dis_train");
        double[][] ds_dis_train = ((MLDouble) dis_train).getArray();
        MLArray dis_test = mlArrayRetrived.get("ds_dis_test");
        double[][] ds_dis_test = ((MLDouble) dis_test).getArray();
        
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        DiscretizedData dd_or = new DiscretizedData(ds_dis_or.length, ds_dis_or[0].length);
        dd_or.importData(ds_dis_or);
        DiscretizedData dd_train = new DiscretizedData(ds_dis_train.length, ds_dis_train[0].length);
        dd_train.importData(ds_dis_train);
        DiscretizedData dd_test = new DiscretizedData(ds_dis_test.length, ds_dis_test[0].length);
        dd_test.importData(ds_dis_test);
        
        ds_dis.setOriginal(dd_or);
        ds_dis.setTrain(dd_train);
        ds_dis.setTest(dd_test);
        
        esaxkmeans.Classify(ds_dis, false,"train");
        misclassification.put("train", esaxkmeans.getErrorRate());
        
        esaxkmeans.Classify(ds_dis, false,"test");        
        misclassification.put("test", esaxkmeans.getErrorRate()); 
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
    }
    
    public void add1dSAX(DataSet ds) throws IOException, SAXException, Exception {
        OneD_SAX onedsax = new OneD_SAX();
        
        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", onedsax.getName()) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + onedsax.getName();
        
        MatFileReader mfr = new MatFileReader(dir + file + "_B.mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray dis = mlArrayRetrived.get("ds_dis");
        double[][] ds_dis_or = ((MLDouble) dis).getArray();
        MLArray dis_train = mlArrayRetrived.get("ds_dis_train");
        double[][] ds_dis_train = ((MLDouble) dis_train).getArray();
        MLArray dis_test = mlArrayRetrived.get("ds_dis_test");
        double[][] ds_dis_test = ((MLDouble) dis_test).getArray();
        
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        DiscretizedData dd_or = new DiscretizedData(ds_dis_or.length, ds_dis_or[0].length);
        dd_or.importData(ds_dis_or);
        DiscretizedData dd_train = new DiscretizedData(ds_dis_train.length, ds_dis_train[0].length);
        dd_train.importData(ds_dis_train);
        DiscretizedData dd_test = new DiscretizedData(ds_dis_test.length, ds_dis_test[0].length);
        dd_test.importData(ds_dis_test);
        
        ds_dis.setOriginal(dd_or);
        ds_dis.setTrain(dd_train);
        ds_dis.setTest(dd_test);
        
        onedsax.Classify(ds_dis, false,"train");
        misclassification.put("train", onedsax.getErrorRate());
        
        onedsax.Classify(ds_dis, false,"test");        
        misclassification.put("test", onedsax.getErrorRate()); 
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
    }
    
    public void addRKMeans(DataSet ds) throws IOException, SAXException, Exception {
        RKMeans rkmeans = new RKMeans();
        
        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", rkmeans.getName()) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + rkmeans.getName();
        
        MatFileReader mfr = new MatFileReader(dir + file + "_B.mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray dis = mlArrayRetrived.get("ds_dis");
        double[][] ds_dis_or = ((MLDouble) dis).getArray();
        MLArray dis_train = mlArrayRetrived.get("ds_dis_train");
        double[][] ds_dis_train = ((MLDouble) dis_train).getArray();
        MLArray dis_test = mlArrayRetrived.get("ds_dis_test");
        double[][] ds_dis_test = ((MLDouble) dis_test).getArray();
        
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        DiscretizedData dd_or = new DiscretizedData(ds_dis_or.length, ds_dis_or[0].length);
        dd_or.importData(ds_dis_or);
        DiscretizedData dd_train = new DiscretizedData(ds_dis_train.length, ds_dis_train[0].length);
        dd_train.importData(ds_dis_train);
        DiscretizedData dd_test = new DiscretizedData(ds_dis_test.length, ds_dis_test[0].length);
        dd_test.importData(ds_dis_test);
        
        ds_dis.setOriginal(dd_or);
        ds_dis.setTrain(dd_train);
        ds_dis.setTest(dd_test);
        
        rkmeans.Classify(ds_dis, false,"train");
        misclassification.put("train", rkmeans.getErrorRate());
        
        rkmeans.Classify(ds_dis, false,"test");        
        misclassification.put("test", rkmeans.getErrorRate()); 
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
    }
    
    public void addSAXKMeans(DataSet ds) throws IOException, SAXException, Exception {
        SAXKMeans saxkmeans = new SAXKMeans();
        
        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", saxkmeans.getName()) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + saxkmeans.getName();
        
        MatFileReader mfr = new MatFileReader(dir + file + "_B.mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray dis = mlArrayRetrived.get("ds_dis");
        double[][] ds_dis_or = ((MLDouble) dis).getArray();
        MLArray dis_train = mlArrayRetrived.get("ds_dis_train");
        double[][] ds_dis_train = ((MLDouble) dis_train).getArray();
        MLArray dis_test = mlArrayRetrived.get("ds_dis_test");
        double[][] ds_dis_test = ((MLDouble) dis_test).getArray();
        
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        DiscretizedData dd_or = new DiscretizedData(ds_dis_or.length, ds_dis_or[0].length);
        dd_or.importData(ds_dis_or);
        DiscretizedData dd_train = new DiscretizedData(ds_dis_train.length, ds_dis_train[0].length);
        dd_train.importData(ds_dis_train);
        DiscretizedData dd_test = new DiscretizedData(ds_dis_test.length, ds_dis_test[0].length);
        dd_test.importData(ds_dis_test);
        
        ds_dis.setOriginal(dd_or);
        ds_dis.setTrain(dd_train);
        ds_dis.setTest(dd_test);
        
        saxkmeans.Classify(ds_dis, false,"train");
        misclassification.put("train", saxkmeans.getErrorRate());
        
        saxkmeans.Classify(ds_dis, false,"test");        
        misclassification.put("test", saxkmeans.getErrorRate()); 
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
    }
    
    public void addRSAX(DataSet ds) throws IOException, SAXException, Exception {
        rSAX rsax = new rSAX(10);
//        int ksel = rsax.SelectK(ds);
        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", rsax.getName()) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + rsax.getName();
        
        MatFileReader mfr = new MatFileReader(dir + file + "_B.mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray dis = mlArrayRetrived.get("ds_dis");
        double[][] ds_dis_or = ((MLDouble) dis).getArray();
        MLArray dis_train = mlArrayRetrived.get("ds_dis_train");
        double[][] ds_dis_train = ((MLDouble) dis_train).getArray();
        MLArray dis_test = mlArrayRetrived.get("ds_dis_test");
        double[][] ds_dis_test = ((MLDouble) dis_test).getArray();
        
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        DiscretizedData dd_or = new DiscretizedData(ds_dis_or.length, ds_dis_or[0].length);
        dd_or.importData(ds_dis_or);
        DiscretizedData dd_train = new DiscretizedData(ds_dis_train.length, ds_dis_train[0].length);
        dd_train.importData(ds_dis_train);
        DiscretizedData dd_test = new DiscretizedData(ds_dis_test.length, ds_dis_test[0].length);
        dd_test.importData(ds_dis_test);
        
        ds_dis.setOriginal(dd_or);
        ds_dis.setTrain(dd_train);
        ds_dis.setTest(dd_test);
        
        rsax.Classify(ds_dis, false,"train");
        misclassification.put("train", rsax.getErrorRate());
        
        rsax.Classify(ds_dis, false,"test");        
        misclassification.put("test", rsax.getErrorRate()); 
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
    }

    public void addTD4C(DataSet ds, IDistanceMeasure distanceMeasure) throws IOException, SAXException, Exception {
        TD4C td4c = new TD4C(distanceMeasure, 0, 1.5);
        td4c.setBins(4);
//        DiscretizedDataSet ds_dis = td4c.getDiscretization(ds);

        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + "Discretizados", td4c.getName()) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + td4c.getName();
        
        MatFileReader mfr = new MatFileReader(dir + file + "_B.mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray dis = mlArrayRetrived.get("ds_dis");
        double[][] ds_dis_or = ((MLDouble) dis).getArray();
        MLArray dis_train = mlArrayRetrived.get("ds_dis_train");
        double[][] ds_dis_train = ((MLDouble) dis_train).getArray();
        MLArray dis_test = mlArrayRetrived.get("ds_dis_test");
        double[][] ds_dis_test = ((MLDouble) dis_test).getArray();
        
        DiscretizedDataSet ds_dis = new DiscretizedDataSet();
        DiscretizedData dd_or = new DiscretizedData(ds_dis_or.length, ds_dis_or[0].length);
        dd_or.importData(ds_dis_or);
        DiscretizedData dd_train = new DiscretizedData(ds_dis_train.length, ds_dis_train[0].length);
        dd_train.importData(ds_dis_train);
        DiscretizedData dd_test = new DiscretizedData(ds_dis_test.length, ds_dis_test[0].length);
        dd_test.importData(ds_dis_test);
        
        ds_dis.setOriginal(dd_or);
        ds_dis.setTrain(dd_train);
        ds_dis.setTest(dd_test);
        
        td4c.Classify(ds_dis, false,"train");
        misclassification.put("train", td4c.getErrorRate());
        
        td4c.Classify(ds_dis, false,"test");        
        misclassification.put("test", td4c.getErrorRate()); 
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
    }
    
    public void addRAW(DataSet ds) throws IOException, SAXException, Exception {
        RAW raw = new RAW();

        raw.Classify(ds, false,"train");
        misclassification.put("train", raw.getErrorRate());
        
        raw.Classify(ds, false,"test");        
        misclassification.put("test", raw.getErrorRate()); 
        
    }
    
//    public static void main(String[] args) throws MyException, IOException, Exception {
//        String Location = "/e15p100g300/";
//        String folder = "MODiTS";
//        
//        String type_selection = "Train-CV";
//        
//        DataSet ds = new DataSet(10, false);
//        
//        TimeSeriesDiscretize_source.symbols = Utils.getListSymbols();
//        
//        TexasSharpshooterMethods tsm = new TexasSharpshooterMethods(type_selection, Location, folder, ds);
//        
//         System.out.println(tsm.misclassification.get("train")+"-"+tsm.misclassification.get("test"));
//        
//    }
//    
}
