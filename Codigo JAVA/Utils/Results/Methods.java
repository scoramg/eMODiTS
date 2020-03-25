/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Results;

import DataMining.Classification.Classification;
import DataSets.DataSet;
import DataSets.DiscretizedData;
import DataSets.DiscretizedDataSet;
import Exceptions.MyException;
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
import SAX.SAXVSM.SAXVSM;
import SAX.aSAX.aSAX;
import SAX.rSAX.rSAX;
import TD4C.EntropyDistance;
import TD4C.IDistanceMeasure;
import TD4C.TD4C;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
import Utils.Utils;
import ca.nengo.io.MatlabExporter;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Level;
import weka.core.Instances;

/**
 *
 * @author amarquezgr
 */
public class Methods {

    public int ClassificationType;
    public String Location, type_selection;
    
    public Methods(){
        
    }
    
    public Methods(int ClassificationType){
        this.ClassificationType = ClassificationType;
    }
    
    public Methods(int ClassificationType, String Location) {
        this.ClassificationType = ClassificationType;
        this.Location = Location;
    }
    
    public Methods(int ClassificationType, String Location, String type_selection) {
        this.ClassificationType = ClassificationType;
        this.Location = Location;
        this.type_selection = type_selection;
    }

    public void setClassificationType(int ClassificationType) {
        this.ClassificationType = ClassificationType;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public void setType_selection(String type_selection) {
        this.type_selection = type_selection;
    }
    
    

    public IScheme getBestMODiTS(DataSet ds, int type) throws IOException {
//        IScheme individual = new Individuals.Proposal.MOScheme();
//        String folder = "MODiTS";
//        if (type == 1) {
//            //individual = new Individuals.Proposal.MOScheme();
//            folder = "Proposal Multiobjective";
//        } 
//        MatlabExporter exporter = new MatlabExporter();
//        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + Location, folder) + "/" + ds.getName() + "/";
//        String file = ds.getName() + "_" + folder;
////        System.out.println(dir + file);
//        MatFileReader mfr = new MatFileReader(dir + file + ".mat");
//        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
//        MLArray w = mlArrayRetrived.get("AccumulatedFrontFitness");
//        double[][] arr = ((MLDouble) w).getArray();
//        iSelectors selector;
//        switch (type_selection) {
//            case "Knee": //Reference Point [0 0 0]
//                selector = new ReferencePointSelector(arr,0);
//                break;
//            case "Mean": //Reference Point Mean
//                selector = new ClusteringSelector(arr, type, mfr, ds, 1); //ReferencePointSelector(arr, 1);
//                break;
//            case "Train-Test": //Trampa
//                selector = new ClassificationSelector(arr, 0, mfr, true, ds);
//                break;
//            case "Train-CV"://Train - CV
//                selector = new ClassificationSelector(arr, 0, mfr, false, ds);
//                break;
//            case "KMeans"://K-Means (k=20% del tamaño del cluster), and classification with TD
//                int k = (int) Math.floor(arr.length*0.20);
//                selector = new ClusteringSelector(arr, 0, mfr, ds, k); 
//                break;    
//            default://Reference Point [0 0 0]
//                selector = new ReferencePointSelector(arr, 0);
//                break;
//        }
//        int ind = selector.getSelection();
////        int ind = 63;
//       
//        
//        String FileName = "IndSel_"+ds.getName()+"_"+type_selection;
//        
//        String directory = Location+'/'+folder+'/'+ds.getName();
//        
//        File FileDir = new File(directory);
//        if(!FileDir.exists()) FileDir.mkdirs();
//
//        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".csv" )  ){
//            out.println("FrontIndividual" + ind);
//        } catch (FileNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Methods.class.getName()).log(Level.SEVERE, null, ex);
//        }   
//        
//         MLArray f = mlArrayRetrived.get("FrontIndividual" + ind);
//        
//        double[][] front = ((MLDouble) f).getArray();
//        individual.add(front);
//
//        switch(this.ClassificationType){
//            case 1: // CV on original
//                individual.Classify(ds, false,"original");
//                break;
//            case 2: //CV on test
//                individual.Classify(ds, false,"test");
//                break;
//            case 3: //Train-Test without test subdivision   
//                individual.Classify(ds, true, "WithoutCV");
//                break;
//            case 4: //Train-Test with test subdivision   
//                individual.Classify(ds, true, "WithCV");  
//                break;
//            case 5: //CV on train  
//                individual.Classify(ds, false,"train");
//                break;
//            default:
//                individual.Classify(ds, false,"original");
//                break;
//        }
//        
////        individual.Export(ds, exporter);
//                
//        String fileBest = ds.getName()+"_"+folder+"_"+selector.getName()+"_B";
//
//        File FileDirBest = new File(dir);
//        if(!FileDirBest.exists()) FileDirBest.mkdirs();
//
//        File FileTabla = new File(dir+"/"+fileBest+".mat");
//        exporter.write(FileTabla);
//        individual.ExportGraph(ds, folder, Location, selector.getName());
//        individual.ExportErrorRates(ds, folder, Location, type_selection);
//        
//        return individual;
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
        
        switch(this.ClassificationType){
            case 1: // CV on original
                individual.Classify(ds_dis, false,"original");
                break;
            case 2: //CV on test
                individual.Classify(ds_dis, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                individual.Classify(ds_dis, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                individual.Classify(ds_dis, true, "WithCV");  
                break;
            case 5: //CV on train  
                individual.Classify(ds_dis, false,"train");
                break;
            default:
                individual.Classify(ds_dis, false,"original");
                break;
        }
        
        individual.ExportGraph(ds, folder, System.getProperty("user.dir")+Location, selector.getName());
        individual.ExportErrorRates(ds, folder, System.getProperty("user.dir")+Location, type_selection);
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
        return individual;
    }
    
    public IScheme getBestEPMO(DataSet ds) throws IOException {
        IScheme individual = new Individuals.PEVOMO.MOScheme();
        String folder = "EP Multiobjective";
        MatlabExporter exporter = new MatlabExporter();
        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + Location, individual.getName()) + "/" + ds.getName() + "/";
        String file = ds.getName() + "_" + individual.getName();
        MatFileReader mfr = new MatFileReader(dir + file + ".mat");
        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
        MLArray w = mlArrayRetrived.get("AccumulatedFrontFitness");
        double[][] arr = ((MLDouble) w).getArray();
        iSelectors selector;
        switch (type_selection) {
            case "Knee": //Reference Point [0 0 0]
                selector = new ReferencePointSelector(arr, 0);
                break;
            case "Mean": //Reference Point Mean
                selector = new ClusteringSelector(arr, 1, mfr, ds, 1); //selector = new ReferencePointSelector(arr, 1);
                break;
            case "Train-Test": //Trampa
                selector = new ClassificationSelector(arr, 1, mfr, true, ds);
                break;
            case "Train-CV"://Train - CV
                selector = new ClassificationSelector(arr, 1, mfr, false, ds);
                break;
            case "KMeans"://K-Means (k=20% del tamaño del cluster), and classification with TD
                int k = (int) Math.floor(arr.length*0.20);
                selector = new ClusteringSelector(arr, 1, mfr, ds, k); 
                break;       
            default://Reference Point [0 0 0]
                selector = new ReferencePointSelector(arr, 0);
                break;
        }
        int ind = selector.getSelection();
        
        String FileName = "IndSel_"+ds.getName()+"_"+type_selection;
        
        String directory = Location+'/'+folder+'/'+ds.getName();
        
        File FileDir = new File(directory);
        if(!FileDir.exists()) FileDir.mkdirs();

        try(  PrintWriter out = new PrintWriter( directory+"/"+FileName+".csv" )  ){
            out.println("FrontIndividual" + ind);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Methods.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
//        int ind = 63;
        MLArray f = mlArrayRetrived.get("FrontIndividual" + ind);
        double[][] front = ((MLDouble) f).getArray();
        individual.add(front);

        switch(this.ClassificationType){
            case 1: // CV on original
                individual.Classify(ds, false,"original");
                break;
            case 2: //CV on test
                individual.Classify(ds, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                individual.Classify(ds, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                individual.Classify(ds, true, "WithoutCV");  
                break;
            case 5: //CV on train  
                individual.Classify(ds, false,"train");
                break;    
            default:
                individual.Classify(ds, false,"original");
                break;
        }
        
        
//        individual.Export(ds, exporter);
                
        String fileBest = ds.getName()+"_"+individual.getName()+"_"+selector.getName()+"_B";

        File FileDirBest = new File(dir);
        if(!FileDirBest.exists()) FileDirBest.mkdirs();

        File FileTabla = new File(dir+"/"+fileBest+".mat");
        exporter.write(FileTabla);
        individual.ExportGraph(ds, folder, Location, selector.getName());
        individual.ExportErrorRates(ds, folder, Location, type_selection);
        return individual;
    }

    public IScheme GetBestEP(int NoExec, DataSet ds) throws IOException {
//        IScheme individual = new Individuals.PEVOMO.Scheme();
//        String folder = "PEVOMO";
//        MatlabExporter exporter = new MatlabExporter();
//        double min = 10000000;
//        int nexec = 0;
////        System.out.println(System.getProperty("user.dir") + "/" + Location);
//        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + Location, individual.getName()) + "/" + ds.getName() + "/";
//
//        for (int e = 0; e < NoExec; e++) {
//            individual.empty();
//
//            String file = ds.getName() + "_" + individual.getName() + "_e" + (e + 1);
//
//            individual.ImportFromMatFile(dir + file + ".mat", ds.getTrain().getDimensions()[1]);
//
//            if ((0.9 * individual.getEvaluatedValues()[0] + 0.09 * individual.getEvaluatedValues()[1] + 0.009 * individual.getEvaluatedValues()[2]) < min) {
//                min = 0.9 * individual.getEvaluatedValues()[0] + 0.09 * individual.getEvaluatedValues()[1] + 0.009 * individual.getEvaluatedValues()[2];
//                nexec = e;
//            }
//
//        }
//        individual.empty();
//        String file = ds.getName() + "_" + individual.getName() + "_e" + (nexec + 1);
//        individual.ImportFromMatFile(dir + file + ".mat", ds.getTrain().getDimensions()[1]);
//
//        switch(this.ClassificationType){
//            case 1: // CV on original
//                individual.Classify(ds, false,"original");
//                break;
//            case 2: //CV on test
//                individual.Classify(ds, false,"test");
//                break;
//            case 3: //Train-Test without test subdivision   
//                individual.Classify(ds, true, "WithoutCV");
//                break;
//            case 4: //Train-Test with test subdivision   
//                individual.Classify(ds, true, "WithoutCV");  
//                break;
//            case 5: //CV on train  
//                individual.Classify(ds, false,"train");
//                break;    
//            default:
//                individual.Classify(ds, false,"original");
//                break;
//        }
//        
//
////        individual.Export(ds, exporter);
//                
//        String fileBest = ds.getName()+"_"+individual.getName()+"_B";
//
//        File FileDirBest = new File(dir);
//        if(!FileDirBest.exists()) FileDirBest.mkdirs();
//
//        File FileTabla = new File(dir+"/"+fileBest+".mat");
//        exporter.write(FileTabla);
//        individual.ExportGraph(ds,folder,Location,"");
//        individual.ExportErrorRates(ds, folder, Location, "");
//        return individual;
        String folder = "PEVOMO";
        
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
        
        switch(this.ClassificationType){
            case 1: // CV on original
                individual.Classify(ds_dis, false,"original");
                break;
            case 2: //CV on test
                individual.Classify(ds_dis, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                individual.Classify(ds_dis, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                individual.Classify(ds_dis, true, "WithoutCV");  
                break;
            case 5: //CV on train  
                individual.Classify(ds_dis, false,"train");
                break;    
            default:
                individual.Classify(ds_dis, false,"original");
                break;
        }
//        dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + Location, individual.getName()) + "/" + ds.getName() + "/";
//        System.out.println(System.getProperty("user.dir"));
        individual.ExportGraph(ds,folder,System.getProperty("user.dir")+Location,"");
        individual.ExportErrorRates(ds, folder, System.getProperty("user.dir")+Location, "");
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
        return individual;
    }

    public SAX GetBestSAX(DataSet ds, IScheme BestEP) throws IOException, SAXException, Exception {
//        SAX sax = new SAX(ds, BestEP.getNumberWordCuts(), BestEP.getNumberAlphabetCuts());
//        DiscretizedDataSet ds_dis = sax.getDiscretization(ds);
//
//        switch(this.ClassificationType){
//            case 1: // CV on original
//                sax.Classify(ds_dis, false,"original");
//                break;
//            case 2: //CV on test
//                sax.Classify(ds_dis, false,"test");
//                break;
//            case 3: //Train-Test without test subdivision   
//                sax.Classify(ds_dis, true, "WithoutCV");
//                break;
//            case 4: //Train-Test with test subdivision   
//                sax.Classify(ds_dis, true, "WithoutCV");  
//                break;
//            case 5: //CV on train  
//                sax.Classify(ds_dis, false,"train");
//                break;
//            default:
//                sax.Classify(ds_dis, false,"original");
//                break;
//        }
//
////        MatlabExporter exporter = new MatlabExporter();
////        
////        exporter.add("ds_dis", ds_dis.getOriginal().getFds_discretized());
////        exporter.add("ds_dis_train",  ds_dis.getOriginal().getFds_discretized());
////        exporter.add("ds_dis_test",  ds_dis.getOriginal().getFds_discretized());
////        float[][] er = new float[1][1];
////        er[0][0] = (float) sax.getErrorRate();
////        exporter.add("ErrorRate", er);
////
////        float[][] DataWord = new float[1][sax.getWordSegments().length];
////        for(int w = 0; w<sax.getWordSegments().length;w++){
////            DataWord[0][w] = (float) sax.getWordSegments()[w];
////        }
////        exporter.add("word", DataWord);
////        
////        float[][] DataAlphabet = new float[1][sax.getAlphabetCuts().length];
////        for(int i = 0; i<sax.getAlphabetCuts().length;i++){
////            DataAlphabet[0][i] = (float) sax.getAlphabetCuts()[i];
////        }
////        exporter.add("alphabet", DataAlphabet);
////        
////        String dirSAX = Utils.findDirectory(System.getProperty("user.dir")+"/"+Location, sax.getName())+"/"+ds.getName()+"/";
////
////        String fileSAX = ds.getName()+"_"+sax.getName()+"_B";
////
////        File FileDirSAX = new File(dirSAX);
////        if(!FileDirSAX.exists()) FileDirSAX.mkdirs();
////
////        File FileTabla = new File(dirSAX+"/"+fileSAX+".mat");
////        exporter.write(FileTabla);
//        sax.ExportGraph(ds.getName(), -1, Location);
//        sax.ExportErrorRates(ds, Location);
//        ds_dis.destroy();
//        return sax;
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
        dd_or.importData(ds_dis_or.clone());
        
        DiscretizedData dd_train = new DiscretizedData(ds_dis_train.length, ds_dis_train[0].length);
        dd_train.importData(ds_dis_train.clone());
        
        DiscretizedData dd_test = new DiscretizedData(ds_dis_test.length, ds_dis_test[0].length);
        dd_test.importData(ds_dis_test.clone());
        
        ds_dis.setOriginal(dd_or);
        ds_dis.setTrain(dd_train);
        ds_dis.setTest(dd_test);
        
        switch(this.ClassificationType){
            case 1: // CV on original
                sax.Classify(ds_dis, false,"original");
                break;
            case 2: //CV on test
                sax.Classify(ds_dis, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                sax.Classify(ds_dis, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                sax.Classify(ds_dis, true, "WithoutCV");  
                break;
            case 5: //CV on train  
                sax.Classify(ds_dis, false,"train");
                break;
            default:
                sax.Classify(ds_dis, false,"original");
                break;
        }
        
        sax.ExportGraph(ds.getName(), -1, System.getProperty("user.dir")+Location);
        sax.ExportErrorRates(ds, System.getProperty("user.dir")+Location);
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
        return sax;
    }

    public aSAX GetBestAlphaSAX(DataSet ds, IScheme BestEP) throws IOException, SAXException, Exception {
//        aSAX asax = new aSAX(ds, BestEP.getNumberWordCuts(), BestEP.getNumberAlphabetCuts());
//        DiscretizedDataSet ds_dis = asax.getDiscretization(ds);
//       
//        switch(this.ClassificationType){
//            case 1: // CV on original
//                asax.Classify(ds_dis, false,"original");
//                break;
//            case 2: //CV on test
//                asax.Classify(ds_dis, false,"test");
//                break;
//            case 3: //Train-Test without test subdivision   
//                asax.Classify(ds_dis, true, "WithoutCV");
//                break;
//            case 4: //Train-Test with test subdivision   
//                asax.Classify(ds_dis, true, "WithoutCV");  
//                break;
//            case 5: //CV on train  
//                asax.Classify(ds_dis, false,"train");
//                break;    
//            default:
//                asax.Classify(ds_dis, false,"original");
//                break;
//        }
//
////        MatlabExporter exporter = new MatlabExporter();
////        exporter.add("ds_dis", ds_dis.getOriginal().getFds_discretized());
////        exporter.add("ds_dis_train",  ds_dis.getTrain().getFds_discretized());
////        exporter.add("ds_dis_test",  ds_dis.getTest().getFds_discretized());
////        float[][] er = new float[1][1];
////        er[0][0] = (float) asax.getErrorRate();
////        exporter.add("ErrorRate", er);
////
////        float[][] DataWord = new float[1][asax.getWordSegments().length];
////        for(int w = 0; w<asax.getWordSegments().length;w++){
////            DataWord[0][w] = (float) asax.getWordSegments()[w];
////        }
////        exporter.add("word", DataWord);
////        
////        float[][] DataAlphabet = new float[1][asax.getAlphabetCuts().length];
////        for(int i = 0; i<asax.getAlphabetCuts().length;i++){
////            DataAlphabet[0][i] = (float) asax.getAlphabetCuts()[i];
////        }
////        exporter.add("alphabet", DataAlphabet);
////        
////        String dirSAX = Utils.findDirectory(System.getProperty("user.dir")+"/"+Location, asax.getName())+"/"+ds.getName()+"/";
////
////        String fileSAX = ds.getName()+"_"+asax.getName()+"_B";
////
////        File FileDirSAX = new File(dirSAX);
////        if(!FileDirSAX.exists()) FileDirSAX.mkdirs();
////
////        File FileTabla = new File(dirSAX+"/"+fileSAX+".mat");
////        exporter.write(FileTabla);
//        asax.ExportGraph(ds.getName(), -1, Location);
//        asax.ExportErrorRates(ds, Location);
//        ds_dis.destroy();

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
       
        switch(this.ClassificationType){
            case 1: // CV on original
                asax.Classify(ds_dis, false,"original");
                break;
            case 2: //CV on test
                asax.Classify(ds_dis, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                asax.Classify(ds_dis, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                asax.Classify(ds_dis, true, "WithoutCV");  
                break;
            case 5: //CV on train  
                asax.Classify(ds_dis, false,"train");
                break;    
            default:
                asax.Classify(ds_dis, false,"original");
                break;
        } 
        
        asax.ExportGraph(ds.getName(), -1, System.getProperty("user.dir")+Location);
        asax.ExportErrorRates(ds, System.getProperty("user.dir")+Location);
        
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
        return asax;
    }
    
    public ESAX GetBestESAX(DataSet ds, IScheme BestEP) throws IOException, SAXException, Exception {
//        ESAX esax = new ESAX(ds, BestEP.getNumberWordCuts(), BestEP.getNumberAlphabetCuts());
//        DiscretizedDataSet ds_dis = esax.getDiscretization(ds);
//        
//        switch(this.ClassificationType){
//            case 1: // CV on original
//                esax.Classify(ds_dis, false,"original");
//                break;
//            case 2: //CV on test
//                esax.Classify(ds_dis, false,"test");
//                break;
//            case 3: //Train-Test without test subdivision   
//                esax.Classify(ds_dis, true, "WithoutCV");
//                break;
//            case 4: //Train-Test with test subdivision   
//                esax.Classify(ds_dis, true, "WithoutCV");  
//                break;
//            case 5: //CV on train  
//                esax.Classify(ds_dis, false,"train");
//                break;    
//            default:
//                esax.Classify(ds_dis, false,"original");
//                break;
//        }
//        
//
////        MatlabExporter exporter = new MatlabExporter();
////        exporter.add("ds_dis", ds_dis.getOriginal().getFds_discretized());
////        exporter.add("ds_dis_train",  ds_dis.getTrain().getFds_discretized());
////        exporter.add("ds_dis_test",  ds_dis.getTest().getFds_discretized());
////        float[][] er = new float[1][1];
////        er[0][0] = (float) esax.getErrorRate();
////        exporter.add("ErrorRate", er);
////
////        float[][] DataWord = new float[1][esax.getWordSegments().length];
////        for(int w = 0; w<esax.getWordSegments().length;w++){
////            DataWord[0][w] = (float) esax.getWordSegments()[w];
////        }
////        exporter.add("word", DataWord);
////        
////        float[][] DataAlphabet = new float[1][esax.getAlphabetCuts().length];
////        for(int i = 0; i<esax.getAlphabetCuts().length;i++){
////            DataAlphabet[0][i] = (float) esax.getAlphabetCuts()[i];
////        }
////        exporter.add("alphabet", DataAlphabet);
////        
////        String dirSAX = Utils.findDirectory(System.getProperty("user.dir")+"/"+Location, esax.getName())+"/"+ds.getName()+"/";
////
////        String fileSAX = ds.getName()+"_"+esax.getName()+"_B";
////
////        File FileDirSAX = new File(dirSAX);
////        if(!FileDirSAX.exists()) FileDirSAX.mkdirs();
////
////        File FileTabla = new File(dirSAX+"/"+fileSAX+".mat");
////        exporter.write(FileTabla);
//        esax.ExportGraph(ds.getName(), -1, Location);
//        esax.ExportErrorRates(ds, Location);
//        ds_dis.destroy();

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
        
        switch(this.ClassificationType){
            case 1: // CV on original
                esax.Classify(ds_dis, false,"original");
                break;
            case 2: //CV on test
                esax.Classify(ds_dis, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                esax.Classify(ds_dis, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                esax.Classify(ds_dis, true, "WithoutCV");  
                break;
            case 5: //CV on train  
                esax.Classify(ds_dis, false,"train");
                break;    
            default:
                esax.Classify(ds_dis, false,"original");
                break;
        }
        
        esax.ExportGraph(ds.getName(), -1, System.getProperty("user.dir")+Location);
        esax.ExportErrorRates(ds, System.getProperty("user.dir")+Location);
            
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();
        return esax;
    }
    
    public ESAXKMeans GetBestESAXKMeans(DataSet ds, IScheme BestEP) throws IOException, SAXException, Exception {
//        ESAXKMeans esaxkmeans = new ESAXKMeans(ds, BestEP.getNumberWordCuts(), BestEP.getNumberAlphabetCuts());
//        DiscretizedDataSet ds_dis = esaxkmeans.getDiscretization(ds);
//        
//        switch(this.ClassificationType){
//            case 1: // CV on original
//                esaxkmeans.Classify(ds_dis, false,"original");
//                break;
//            case 2: //CV on test
//                esaxkmeans.Classify(ds_dis, false,"test");
//                break;
//            case 3: //Train-Test without test subdivision   
//                esaxkmeans.Classify(ds_dis, true, "WithoutCV");
//                break;
//            case 4: //Train-Test with test subdivision   
//                esaxkmeans.Classify(ds_dis, true, "WithoutCV");  
//                break;
//            case 5: //CV on train  
//                esaxkmeans.Classify(ds_dis, false,"train");
//                break;    
//            default:
//                esaxkmeans.Classify(ds_dis, false,"original");
//                break;
//        }
//        
//        
////        MatlabExporter exporter = new MatlabExporter();
////        exporter.add("ds_dis", ds_dis.getOriginal().getFds_discretized());
////        exporter.add("ds_dis_train",  ds_dis.getTrain().getFds_discretized());
////        exporter.add("ds_dis_test",  ds_dis.getTest().getFds_discretized());
////        float[][] er = new float[1][1];
////        er[0][0] = (float) esaxkmeans.getErrorRate();
////        exporter.add("ErrorRate", er);
////
////        float[][] DataWord = new float[1][esaxkmeans.getWordSegments().length];
////        for(int w = 0; w<esaxkmeans.getWordSegments().length;w++){
////            DataWord[0][w] = (float) esaxkmeans.getWordSegments()[w];
////        }
////        exporter.add("word", DataWord);
////        
////        float[][] DataAlphabet = new float[1][esaxkmeans.getAlphabetCuts().length];
////        for(int i = 0; i<esaxkmeans.getAlphabetCuts().length;i++){
////            DataAlphabet[0][i] = (float) esaxkmeans.getAlphabetCuts()[i];
////        }
////        exporter.add("alphabet", DataAlphabet);
////        
////        String dirSAX = Utils.findDirectory(System.getProperty("user.dir")+"/"+Location, esaxkmeans.getName())+"/"+ds.getName()+"/";
////
////        String fileSAX = ds.getName()+"_"+esaxkmeans.getName()+"_B";
////
////        File FileDirSAX = new File(dirSAX);
////        if(!FileDirSAX.exists()) FileDirSAX.mkdirs();
////
////        File FileTabla = new File(dirSAX+"/"+fileSAX+".mat");
////        exporter.write(FileTabla);
//        esaxkmeans.ExportGraph(ds.getName(), -1, Location);
//        esaxkmeans.ExportErrorRates(ds, Location);
//        ds_dis.destroy();

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
        
        switch(this.ClassificationType){
            case 1: // CV on original
                esaxkmeans.Classify(ds_dis, false,"original");
                break;
            case 2: //CV on test
                esaxkmeans.Classify(ds_dis, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                esaxkmeans.Classify(ds_dis, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                esaxkmeans.Classify(ds_dis, true, "WithoutCV");  
                break;
            case 5: //CV on train  
                esaxkmeans.Classify(ds_dis, false,"train");
                break;    
            default:
                esaxkmeans.Classify(ds_dis, false,"original");
                break;
        }
        
        esaxkmeans.ExportGraph(ds.getName(), -1, System.getProperty("user.dir")+Location);
        esaxkmeans.ExportErrorRates(ds, System.getProperty("user.dir")+Location);
            
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();

        return esaxkmeans;
    }
    
    public OneD_SAX GetBest1dSAX(DataSet ds, IScheme BestEP) throws IOException, SAXException, Exception {
//        OneD_SAX onedsax = new OneD_SAX(8,ds, BestEP.getNumberWordCuts(), BestEP.getNumberAlphabetCuts());
//        DiscretizedDataSet ds_dis = onedsax.getDiscretization(ds);
//        
//        switch(this.ClassificationType){
//            case 1: // CV on original
//                onedsax.Classify(ds_dis, false,"original");
//                break;
//            case 2: //CV on test
//                onedsax.Classify(ds_dis, false,"test");
//                break;
//            case 3: //Train-Test without test subdivision   
//                onedsax.Classify(ds_dis, true, "WithoutCV");
//                break;
//            case 4: //Train-Test with test subdivision   
//                onedsax.Classify(ds_dis, true, "WithoutCV");  
//                break;
//            case 6: //CV on train  
//                onedsax.Classify(ds_dis, false,"train");
//                break;    
//            default:
//                onedsax.Classify(ds_dis, false,"original");
//                break;
//        }
//        
////        MatlabExporter exporter = new MatlabExporter();
////        exporter.add("ds_dis", ds_dis.getOriginal().getFds_discretized());
////        exporter.add("ds_dis_train",  ds_dis.getTrain().getFds_discretized());
////        exporter.add("ds_dis_test",  ds_dis.getTest().getFds_discretized());
////        float[][] er = new float[1][1];
////        er[0][0] = (float) onedsax.getErrorRate();
////        exporter.add("ErrorRate", er);
////
////        float[][] DataWord = new float[1][onedsax.getWordSegments().length];
////        for(int w = 0; w<onedsax.getWordSegments().length;w++){
////            DataWord[0][w] = (float) onedsax.getWordSegments()[w];
////        }
////        exporter.add("word", DataWord);
////        
////        float[][] DataAlphabet = new float[1][onedsax.getAlphabetCuts().length];
////        for(int i = 0; i<onedsax.getAlphabetCuts().length;i++){
////            DataAlphabet[0][i] = (float) onedsax.getAlphabetCuts()[i];
////        }
////        exporter.add("alphabet", DataAlphabet);
////        
////        String dirSAX = Utils.findDirectory(System.getProperty("user.dir")+"/"+Location, onedsax.getName())+"/"+ds.getName()+"/";
////
////        String fileSAX = ds.getName()+"_"+onedsax.getName()+"_B";
////
////        File FileDirSAX = new File(dirSAX);
////        if(!FileDirSAX.exists()) FileDirSAX.mkdirs();
////
////        File FileTabla = new File(dirSAX+"/"+fileSAX+".mat");
////        exporter.write(FileTabla);
//        onedsax.ExportGraph(ds.getName(), -1, Location);
//        onedsax.ExportErrorRates(ds, Location);
//        ds_dis.destroy();

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
        
        switch(this.ClassificationType){
            case 1: // CV on original
                onedsax.Classify(ds_dis, false,"original");
                break;
            case 2: //CV on test
                onedsax.Classify(ds_dis, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                onedsax.Classify(ds_dis, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                onedsax.Classify(ds_dis, true, "WithoutCV");  
                break;
            case 6: //CV on train  
                onedsax.Classify(ds_dis, false,"train");
                break;    
            default:
                onedsax.Classify(ds_dis, false,"original");
                break;
        }
        
        onedsax.ExportGraph(ds.getName(), -1, System.getProperty("user.dir")+Location);
        onedsax.ExportErrorRates(ds, System.getProperty("user.dir")+Location);
        
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();

        return onedsax;
    }
    
    public RKMeans GetBestRKMeans(DataSet ds, IScheme BestEP) throws IOException, SAXException, Exception {
//        RKMeans rkmeans = new RKMeans(ds, BestEP.getNumberWordCuts(), BestEP.getNumberAlphabetCuts());
//        DiscretizedDataSet ds_dis = rkmeans.getDiscretization(ds);
//        
//        switch(this.ClassificationType){
//            case 1: // CV on original
//                rkmeans.Classify(ds_dis, false,"original");
//                break;
//            case 2: //CV on test
//                rkmeans.Classify(ds_dis, false,"test");
//                break;
//            case 3: //Train-Test without test subdivision   
//                rkmeans.Classify(ds_dis, true, "WithoutCV");
//                break;
//            case 4: //Train-Test with test subdivision   
//                rkmeans.Classify(ds_dis, true, "WithoutCV");  
//                break;
//            case 5: //CV on train  
//                rkmeans.Classify(ds_dis, false,"train");
//                break;    
//            default:
//                rkmeans.Classify(ds_dis, false,"original");
//                break;
//        }
//        
////        MatlabExporter exporter = new MatlabExporter();
////        exporter.add("ds_dis", ds_dis.getOriginal().getFds_discretized());
////        exporter.add("ds_dis_train",  ds_dis.getTrain().getFds_discretized());
////        exporter.add("ds_dis_test",  ds_dis.getTest().getFds_discretized());
////        float[][] er = new float[1][1];
////        er[0][0] = (float) rkmeans.getErrorRate();
////        exporter.add("ErrorRate", er);
////
////        float[][] DataWord = new float[1][rkmeans.getWordSegments().length];
////        for(int w = 0; w<rkmeans.getWordSegments().length;w++){
////            DataWord[0][w] = (float) rkmeans.getWordSegments()[w];
////        }
////        exporter.add("word", DataWord);
////        
////        float[][] DataAlphabet = new float[1][rkmeans.getAlphabetCuts().length];
////        for(int i = 0; i<rkmeans.getAlphabetCuts().length;i++){
////            DataAlphabet[0][i] = (float) rkmeans.getAlphabetCuts()[i];
////        }
////        exporter.add("alphabet", DataAlphabet);
////        
////        String dirSAX = Utils.findDirectory(System.getProperty("user.dir")+"/"+Location, rkmeans.getName())+"/"+ds.getName()+"/";
////
////        String fileSAX = ds.getName()+"_"+rkmeans.getName()+"_B";
////
////        File FileDirSAX = new File(dirSAX);
////        if(!FileDirSAX.exists()) FileDirSAX.mkdirs();
////
////        File FileTabla = new File(dirSAX+"/"+fileSAX+".mat");
////        exporter.write(FileTabla);
//        rkmeans.ExportGraph(ds.getName(), -1, Location);
//        rkmeans.ExportErrorRates(ds, Location);
//        ds_dis.destroy();

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
        
        switch(this.ClassificationType){
            case 1: // CV on original
                rkmeans.Classify(ds_dis, false,"original");
                break;
            case 2: //CV on test
                rkmeans.Classify(ds_dis, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                rkmeans.Classify(ds_dis, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                rkmeans.Classify(ds_dis, true, "WithoutCV");  
                break;
            case 5: //CV on train  
                rkmeans.Classify(ds_dis, false,"train");
                break;    
            default:
                rkmeans.Classify(ds_dis, false,"original");
                break;
        }
        
        rkmeans.ExportGraph(ds.getName(), -1, System.getProperty("user.dir")+Location);
        rkmeans.ExportErrorRates(ds, System.getProperty("user.dir")+Location);
        
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();

        return rkmeans;
    }
    
    public SAXKMeans GetBestSAXKMeans(DataSet ds, IScheme BestEP) throws IOException, SAXException, Exception {
//        SAXKMeans saxkmeans = new SAXKMeans(ds, BestEP.getNumberWordCuts(), BestEP.getNumberAlphabetCuts());
//        DiscretizedDataSet ds_dis = saxkmeans.getDiscretization(ds);
//        
//        switch(this.ClassificationType){
//            case 1: // CV on original
//                saxkmeans.Classify(ds_dis, false,"original");
//                break;
//            case 2: //CV on test
//                saxkmeans.Classify(ds_dis, false,"test");
//                break;
//            case 3: //Train-Test without test subdivision   
//                saxkmeans.Classify(ds_dis, true, "WithoutCV");
//                break;
//            case 4: //Train-Test with test subdivision   
//                saxkmeans.Classify(ds_dis, true, "WithoutCV");  
//                break;
//            case 5: //CV on train  
//                saxkmeans.Classify(ds_dis, false,"train");
//                break;    
//            default:
//                saxkmeans.Classify(ds_dis, false,"original");
//                break;
//        }
//        
////        MatlabExporter exporter = new MatlabExporter();
////        exporter.add("ds_dis", ds_dis.getOriginal().getFds_discretized());
////        exporter.add("ds_dis_train",  ds_dis.getTrain().getFds_discretized());
////        exporter.add("ds_dis_test",  ds_dis.getTest().getFds_discretized());
////        float[][] er = new float[1][1];
////        er[0][0] = (float) saxkmeans.getErrorRate();
////        exporter.add("ErrorRate", er);
////
////        float[][] DataWord = new float[1][saxkmeans.getWordSegments().length];
////        for(int w = 0; w<saxkmeans.getWordSegments().length;w++){
////            DataWord[0][w] = (float) saxkmeans.getWordSegments()[w];
////        }
////        exporter.add("word", DataWord);
////        
////        float[][] DataAlphabet = new float[1][saxkmeans.getAlphabetCuts().length];
////        for(int i = 0; i<saxkmeans.getAlphabetCuts().length;i++){
////            DataAlphabet[0][i] = (float) saxkmeans.getAlphabetCuts()[i];
////        }
////        exporter.add("alphabet", DataAlphabet);
////        
////        String dirSAX = Utils.findDirectory(System.getProperty("user.dir")+"/"+Location, saxkmeans.getName())+"/"+ds.getName()+"/";
////
////        String fileSAX = ds.getName()+"_"+saxkmeans.getName()+"_B";
////
////        File FileDirSAX = new File(dirSAX);
////        if(!FileDirSAX.exists()) FileDirSAX.mkdirs();
////
////        File FileTabla = new File(dirSAX+"/"+fileSAX+".mat");
////        exporter.write(FileTabla);
//        saxkmeans.ExportGraph(ds.getName(), -1, Location);
//        saxkmeans.ExportErrorRates(ds, Location);
//        ds_dis.destroy();

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
        
        switch(this.ClassificationType){
            case 1: // CV on original
                saxkmeans.Classify(ds_dis, false,"original");
                break;
            case 2: //CV on test
                saxkmeans.Classify(ds_dis, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                saxkmeans.Classify(ds_dis, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                saxkmeans.Classify(ds_dis, true, "WithoutCV");  
                break;
            case 5: //CV on train  
                saxkmeans.Classify(ds_dis, false,"train");
                break;    
            default:
                saxkmeans.Classify(ds_dis, false,"original");
                break;
        }
        
        saxkmeans.ExportGraph(ds.getName(), -1, System.getProperty("user.dir")+Location);
        saxkmeans.ExportErrorRates(ds, System.getProperty("user.dir")+Location);
         
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();

        return saxkmeans;
    }
    
    public rSAX GetBestRSAX(DataSet ds, IScheme BestEP) throws IOException, SAXException, Exception {
//        rSAX rsax = new rSAX(ds, BestEP.getNumberWordCuts(), BestEP.getNumberAlphabetCuts(), 10);
//        int ksel = rsax.SelectK(ds);
//        DiscretizedDataSet ds_dis = rsax.getDiscretization(ds, ksel);
//        
//        switch(this.ClassificationType){
//            case 1: // CV on original
//                rsax.Classify(ds_dis, false,"original");
//                break;
//            case 2: //CV on test
//                rsax.Classify(ds_dis, false,"test");
//                break;
//            case 3: //Train-Test without test subdivision   
//                rsax.Classify(ds_dis, true, "WithoutCV");
//                break;
//            case 4: //Train-Test with test subdivision   
//                rsax.Classify(ds_dis, true, "WithoutCV");  
//                break;
//            case 5: //CV on train  
//                rsax.Classify(ds_dis, false,"train");
//                break;    
//            default:
//                rsax.Classify(ds_dis, false,"original");
//                break;
//        }
//        
////        MatlabExporter exporter = new MatlabExporter();
////        exporter.add("ds_dis", ds_dis.getOriginal().getFds_discretized());
////        exporter.add("ds_dis_train",  ds_dis.getTrain().getFds_discretized());
////        exporter.add("ds_dis_test",  ds_dis.getTest().getFds_discretized());
////        float[][] er = new float[1][1];
////        er[0][0] = (float) rsax.getErrorRate();
////        exporter.add("ErrorRate", er);
////
////        float[][] DataWord = new float[1][rsax.getWordSegments().length];
////        for(int w = 0; w<rsax.getWordSegments().length;w++){
////            DataWord[0][w] = (float) rsax.getWordSegments()[w];
////        }
////        exporter.add("word", DataWord);
////        
////        float[][] DataAlphabet = new float[1][rsax.getAlphabetCuts().length];
////        for(int i = 0; i<rsax.getAlphabetCuts().length;i++){
////            DataAlphabet[0][i] = (float) rsax.getAlphabetCuts()[i];
////        }
////        exporter.add("alphabet", DataAlphabet);
////        
////        String dirSAX = Utils.findDirectory(System.getProperty("user.dir")+"/"+Location, rsax.getName())+"/"+ds.getName()+"/";
////
////        String fileSAX = ds.getName()+"_"+rsax.getName()+"_B";
////
////        File FileDirSAX = new File(dirSAX);
////        if(!FileDirSAX.exists()) FileDirSAX.mkdirs();
////
////        File FileTabla = new File(dirSAX+"/"+fileSAX+".mat");
////        exporter.write(FileTabla);
//        rsax.ExportGraph(ds.getName(), -1, Location);
//        rsax.ExportErrorRates(ds, Location);
//        ds_dis.destroy();

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
        
        switch(this.ClassificationType){
            case 1: // CV on original
                rsax.Classify(ds_dis, false,"original");
                break;
            case 2: //CV on test
                rsax.Classify(ds_dis, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                rsax.Classify(ds_dis, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                rsax.Classify(ds_dis, true, "WithoutCV");  
                break;
            case 5: //CV on train  
                rsax.Classify(ds_dis, false,"train");
                break;    
            default:
                rsax.Classify(ds_dis, false,"original");
                break;
        }
        
        rsax.ExportGraph(ds.getName(), -1, System.getProperty("user.dir")+Location);
        rsax.ExportErrorRates(ds, System.getProperty("user.dir")+Location);
        
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();

        return rsax;
    }

    public SAXVSM GetBestSAXVSM(DataSet ds) throws IOException, SAXException, Exception {
        SAXVSM saxvsm = new SAXVSM();
//        saxvsm.Classify(ds, false, "original");
        return saxvsm;
    }
    
    public TD4C GetBestTD4C(DataSet ds, IDistanceMeasure distanceMeasure) throws IOException, SAXException, Exception {
//        TD4C td4c = new TD4C(distanceMeasure, 0, 1.5);
//        td4c.setBins(4);
//        DiscretizedDataSet ds_dis = td4c.getDiscretization(ds);
//        
//        switch(this.ClassificationType){
//            case 1: // CV on original
//                td4c.Classify(ds_dis, false,"original");
//                break;
//            case 2: //CV on test
//                td4c.Classify(ds_dis, false,"test");
//                break;
//            case 3: //Train-Test without test subdivision   
//                td4c.Classify(ds_dis, true, "WithoutCV");
//                break;
//            case 4: //Train-Test with test subdivision   
//                td4c.Classify(ds_dis, true, "WithoutCV");  
//                break;
//            case 5: //CV on train  
//                td4c.Classify(ds_dis, false,"train");
//                break;    
//            default:
//                td4c.Classify(ds_dis, false,"original");
//                break;
//        }
////        td4c.Classify(ds_dis, true, "");   
//        
////        MatlabExporter exporter = new MatlabExporter();
////        exporter.add("ds_dis", ds_dis.getOriginal().getFds_discretized());
////        exporter.add("ds_dis_train",  ds_dis.getTrain().getFds_discretized());
////        exporter.add("ds_dis_test",  ds_dis.getTest().getFds_discretized());
////        float[][] er = new float[1][1];
////        er[0][0] = (float) td4c.getErrorRate();
////        exporter.add("ErrorRate", er);
////        
////        String dirSAX = Utils.findDirectory(System.getProperty("user.dir")+"/"+Location, td4c.getName())+"/"+ds.getName()+"/";
////
////        String fileSAX = ds.getName()+"_"+td4c.getName()+"_B";
////
////        File FileDirSAX = new File(dirSAX);
////        if(!FileDirSAX.exists()) FileDirSAX.mkdirs();
////
////        File FileTabla = new File(dirSAX+"/"+fileSAX+".mat");
////        exporter.write(FileTabla);
//        td4c.ExportGraph(ds.getName(), -1, Location);
//        td4c.ExportErrorRates(ds, Location);
//        ds_dis.destroy();

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
        
        switch(this.ClassificationType){
            case 1: // CV on original
                td4c.Classify(ds_dis, false,"original");
                break;
            case 2: //CV on test
                td4c.Classify(ds_dis, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                td4c.Classify(ds_dis, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                td4c.Classify(ds_dis, true, "WithoutCV");  
                break;
            case 5: //CV on train  
                td4c.Classify(ds_dis, false,"train");
                break;    
            default:
                td4c.Classify(ds_dis, false,"original");
                break;
        }
        
        td4c.ExportGraph(ds.getName(), -1, System.getProperty("user.dir")+Location);
        td4c.ExportErrorRates(ds, System.getProperty("user.dir")+Location);
            
        dd_or.destroy();
        dd_train.destroy();
        dd_test.destroy();
        ds_dis.destroy();

        return td4c;
    }
    
    public RAW GetRAWClassification(DataSet ds) throws IOException, SAXException, Exception {
        RAW raw = new RAW();
//        raw.Classify(ds, false, "original");    
//        raw.Classify(ds, true, "");    

        switch(this.ClassificationType){
            case 1: // CV on original
                raw.Classify(ds, false,"original");
                break;
            case 2: //CV on test
                raw.Classify(ds, false,"test");
                break;
            case 3: //Train-Test without test subdivision   
                raw.Classify(ds, true, "WithoutCV");
                break;
            case 4: //Train-Test with test subdivision   
                raw.Classify(ds, true, "WithoutCV");  
                break;
            case 5: //CV on train  
                raw.Classify(ds, false,"train");
                break;    
            default:
                raw.Classify(ds, false,"original");
                break;
        }

        return raw;
    }
    
    
    public void ExportErrorRates_SAXVSM(DataSet ds) throws IOException, SAXException, Exception {
        long startTime = System.currentTimeMillis();
        Classification csf = new Classification(ds.getTrain().getData());
        
        if (csf.getDataSource().classIndex() != csf.getDataSource().numAttributes()-1){
            Instances PreData = csf.MoveAtts(csf.getDataSource(), 1, csf.getDataSource().numAttributes());
            PreData = Classification.ConvertNumeric2Nominal(PreData, "last");
            PreData.setClassIndex(PreData.numAttributes()-1);
            csf.setDataSource(PreData);
        }
        
        int[] params = SAXVSM.parameterSearch(csf.getDataSource());
        
        int PAA_intervalsPerWindow = params[0];
        int SAX_alphabetSize = params[1];
        int windowSize = params[2];
        
        System.out.println("Training Finished");
        
        SAXVSM saxvsm = new SAXVSM(PAA_intervalsPerWindow, SAX_alphabetSize, windowSize);
        saxvsm.Classify(ds, false, "original");
        String dir = Location+"/"+saxvsm.getName()+"/"+ds.getName()+"/";
        String filename = saxvsm.getName()+"_"+ds.getName()+"_B.mat";

        File FileDir = new File(dir);
        if(!FileDir.exists()) FileDir.mkdirs();

        MatlabExporter exporter = new MatlabExporter();
        
        float[][] parameters = new float[params.length][1];
        for(int i=0; i<params.length;i++){
            parameters[i][0] = (float) params[i];
        }
        exporter.add("Parameters", parameters);
        
        float[][] error_rates = new float[saxvsm.getErrorRatesByFolds().length][1];
        for(int i=0; i<saxvsm.getErrorRatesByFolds().length;i++){
            error_rates[i][0] = (float) saxvsm.getErrorRatesByFolds()[i];
        }
        exporter.add("ErrorRates", error_rates);
        File FileTabla = new File(dir+filename);
        exporter.write(FileTabla);
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time : " + (endTime - startTime) / 1000.0
                        + " seconds");
    }
    
    public static void main(String[] args) throws MyException, IOException {
        String Location = "e15p100g500";
        DataSet ds = new DataSet(10,false);
        
        TimeSeriesDiscretize_source.symbols = Utils.getListSymbols();
//        IScheme sch = getBestMODiTS(ds, Location, 0, "Train-CV");

        Methods method = new Methods(0, Location, "KMeans");

        IScheme sch = method.getBestMODiTS(ds, 0);
        System.out.println(sch.getErrorRate());

//          SAX sax = new SAX(ds, 53, 23);  
    }
}
