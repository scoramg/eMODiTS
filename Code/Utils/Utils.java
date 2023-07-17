/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import parameters.*;
import DataMining.Classification.Classification;
import DataSets.DataSet;
import DataSets.DiscretizedDataSet;
import Diversity.fitnessfunction.FitnessDiversity;
import Exceptions.MyException;
import Interfaces.IPopulation;
import Interfaces.IScheme;
import Populations.Population;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
import com.github.jabbalaci.graphviz.GraphViz;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 *
 * @author amarquezgr
 */
public class Utils {
    public static double IndividualSimilarityMeasure(IScheme individual1, IScheme individual2){
        double sm = 0.0;
        
        for (int i=0; i<individual1.getNumberWordCuts();i++){
            sm += mimath.MiMath.getEuclideanDist(individual1.toDoubleArray(), individual2.toDoubleArray());
        }
       
        return  sm / (double) individual1.getNumberWordCuts();
    }
    
    public static <K, V extends Comparable<? super V>> Map<K, V> SortMapByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                return (e2.getValue()).compareTo(e1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
    
    public static void CalculateMeasure(int g, IPopulation population, Locals local_params){
        FitnessDiversity xi = new FitnessDiversity(0);
        FitnessDiversity psi= new FitnessDiversity(1);
        FitnessDiversity nu = new FitnessDiversity(2);
        FitnessDiversity chi= new FitnessDiversity(3);
        xi.Calculate(population.getAllFitnessValue());
        psi.Calculate(population.getAllFitnessValue());
        nu.Calculate(population.getAllFitnessValue());
        chi.Calculate(population.getAllFitnessValue());
        
        
        double std = mimath.MiMath.getDesviacionStandar(population.getAllFitnessValue());
        double mean = mimath.MiMath.getMedia(population.getAllFitnessValue());
        
        local_params.MeasuresData[g][0] = (float) xi.getFitnessmeasure();
        local_params.MeasuresData[g][1] = (float) psi.getFitnessmeasure();
        local_params.MeasuresData[g][2] = (float) nu.getFitnessmeasure();
        local_params.MeasuresData[g][3] = (float) chi.getFitnessmeasure();
        local_params.MeasuresData[g][7] = (float) std;
        local_params.MeasuresData[g][8] = (float) mean;
    }
    
    public static String findDirectory(String parentDirectory, String Folder) {
        Folder = Folder.replace("/", "");
        
//        System.out.println(Folder);
        
        if(parentDirectory == null){
            return "No existe la carpeta";
        }
        
        File dir = new File(parentDirectory);

        File[] files = dir.listFiles();
        for (File file : files) {
//            System.out.println(file.getName());
            if (file.getName().equals(Folder)) {
                return file.getAbsolutePath();
            }
        }
        return findDirectory(dir.getParent(),Folder);
    }
    
    public static List<Character> getListSymbols(){
        List<Character> symbols = new ArrayList<>();
        symbols.add((char) 0);
        for (int i=1; i<2001;i++){
            int t = Character.getType((char) i);
            if (t==1 || t==2){
                symbols.add((char) i);
            }
        }
        return symbols;
    }
    
    public static double[] Normalize(double[] data){
        double[] norm = new double[data.length];
        double min = mimath.MiMath.min(data);
        double max = mimath.MiMath.max(data);
//        double scaleFactor = ((max-min) == 0) ? max : (max-min);
        for (int c = 0; c < data.length; c++) {
            if((max-min) == 0){
                norm[c] = (double) data[c] / max;
            } else {
                norm[c] = (double)((data[c] - min) / (max-min));
            }
        }
        return norm;
    }
    
    public static double[][] Normalize(double[][] data){
        double[][] norm = new double[data.length][data[0].length];
        for(int f=0;f<data.length;f++){
            double min = mimath.MiMath.min(data[f]);
            double max = mimath.MiMath.max(data[f]);
            double scaleFactor = ((max-min) == 0) ? max : (max-min);
            for (int c = 0; c < data[f].length; c++) {
                norm[f][c] = ((data[f][c] - min) / scaleFactor);
            }
        }
        return norm;
    }
    
    public static Double[] doubleArray2DoubleArray(double[] array){
        Double[] res = new Double[array.length];
        for(int j=0;j<array.length;j++){
            res[j] = Double.valueOf(array[j]);
        }
        return res;
    }
    
    public static void main(String[] args) {
        List<Character> simbols = getListSymbols();
        System.out.println(simbols.get(1));
    }
    
}