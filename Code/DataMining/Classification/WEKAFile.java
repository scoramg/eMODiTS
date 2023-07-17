package DataMining.Classification;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

/**
 *
 * @author Aldo
 */
public final class WEKAFile {
    
    ArffLoader archivo;
    String url;
    
    public WEKAFile(String pArchivo, String salida){
        archivo = new ArffLoader();
        try {
            if(getExtension(pArchivo).compareTo("csv") == 0){
                CSVToARFF(pArchivo, salida);
                url = salida;
            } else {
                archivo.setFile(new File(pArchivo)); 
                url = salida;
            }
        } catch (IOException ex) {
            Logger.getLogger(WEKAFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public WEKAFile(){
        archivo = new ArffLoader();
    }
    
    public void setArchivo(String pArchivo, String salida){
        try {
            if(getExtension(pArchivo).compareTo("csv") == 0){
                CSVToARFF(pArchivo, salida);
                url = salida;
            } else {
                archivo.setFile(new File(pArchivo));   
                url = salida;
            }
        } catch (IOException ex) {
            Logger.getLogger(WEKAFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void CSVToARFF(String ArchivoCSV, String salida) throws IOException{
        //Copia de WekaGUIDemo
        File file = new File(ArchivoCSV);
        CSVLoader loader = new CSVLoader();
        loader.setSource(file);
        Instances data = loader.getDataSet();
        Instances structure = loader.getStructure();
//        System.out.println(file.getParent());

        ArffSaver saver = new ArffSaver();
        saver.setStructure(structure);
        saver.setInstances(data);     
        saver.setFile(new File(salida));
        saver.writeBatch();
//        BufferedWriter writer = new BufferedWriter(new FileWriter(salida));
//        writer.write(data.toString());
//        writer.flush();
//        writer.close();
        archivo.setFile(new File(salida));        
    }
    
    public void CSVToARFF(String ArchivoCSV, String salida, Instances estructura) throws IOException{
        //Copia de WekaGUIDemo
        File file = new File(ArchivoCSV);
        CSVLoader loader = new CSVLoader();
        loader.setSource(file);
        Instances data = loader.getDataSet();
        
        ArffSaver saver = new ArffSaver();
        saver.setStructure(estructura);        
        saver.setInstances(data);
        saver.setFile(new File(salida));
        saver.writeBatch();
//        BufferedWriter writer = new BufferedWriter(new FileWriter(salida));
//        writer.write(data.toString());
//        writer.flush();
//        writer.close();
        
        archivo.setFile(new File(salida));        
    }
    
    public void InstancesToARFF(Instances data, String salida) throws IOException{
        
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(salida));
        saver.writeBatch();
//        BufferedWriter writer = new BufferedWriter(new FileWriter(salida));
//        writer.write(data.toString());
//        writer.flush();
//        writer.close();
        archivo.setFile(new File(salida));        
    }
    
    public File ARFFtoCSV(){
        try {
            CSVSaver csv = new CSVSaver();
            csv.setStructure(archivo.getStructure());
            csv.setInstances(archivo.getDataSet());
            csv.setFile(new File("ArchivoPadre.csv"));
            csv.writeBatch();            
            archivo.reset();
        } catch (Exception ex) {
            Logger.getLogger(WEKAFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new File("ArchivoPadre.csv");
    }
    
    public static String getExtension(String archivo){
        String extension = archivo;
        while(extension.contains(".")){
            extension = extension.substring(extension.indexOf(".")+1);
        }        
        return extension;
    }

    
}
