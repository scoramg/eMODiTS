/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visualizer;

import DataSets.DataSet;
import Tree.TreeInterpeter;
import com.github.jabbalaci.graphviz.GraphViz;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class ExportTrees {
    public static void export(String scheme, String Location, int NoExec){
        for(int i=1; i<=DataSet.NUMBER_OF_DATASETS;i++){
            String DBName = DataSet.getUCRRepository(i);
            GraphViz gv = new GraphViz();
            for(int e=0;e<NoExec;e++){
                String FileName = "Arbol_e"+(e+1);
                String directory = Location+"/"+scheme+"/"+DBName+"/Trees";
                gv.readSource(directory+"/"+FileName+".txt");
        //    //         System.out.println(gv.getDotSource());
                String type = "png";
                String repesentationType= "dot";
                File out = new File(directory+"/"+FileName+"."+ type);  
                gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
            }
        }
    }
    
    public static void export(String scheme, String Location, String DBName, String Selector){
            try {
                GraphViz gv = new GraphViz();
                String FileName = "Arbol_best_"+Selector;
                String directory = Location+"/"+scheme+"/"+DBName+"/Trees";
                gv.readSource(directory+"/"+FileName+".txt");
                String type = "eps";
                String repesentationType= "dot";
                String FileNameIMG = DBName+"_Arbolbest_"+Selector;
                File out = new File(directory+"/"+FileNameIMG+"."+ type);
                gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
                
                String FileNameCSV = directory+"/"+FileName+".csv";
                
                TreeInterpeter ti = new TreeInterpeter();
                File file = new File(directory+"/"+FileName+".txt");
                byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                String tree = new String(encoded, StandardCharsets.UTF_8);
                ti.build(tree);
                
                try (BufferedWriter br = new BufferedWriter(new FileWriter(FileNameCSV))) {
                    br.write(ti.toString());
                } catch (IOException ex) {
                    Logger.getLogger(ExportTrees.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
            Logger.getLogger(ExportTrees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        int i=23;
//        for(int i=1; i<=DataSet.NUMBER_OF_DATASETS;i++){
            if(!DataSet.DATASETS_IGNORED.contains(i)){
                export("MODiTS","e15p100g300", DataSet.getUCRRepository(i), "CTV");
            }
//        }
    }
}
