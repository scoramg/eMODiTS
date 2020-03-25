/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.TexasSharpshooter;

import DataSets.DataSet;
import Exceptions.MyException;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
import Utils.TexasSharpshooter.TexasSharpshooterMethods;
import Utils.Utils;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amarquezgr
 */
public class TexasSharpshooterPlot {
    private List<TexasSharpshooterCollection> points;
    private String Location, SelectorBase, MethodBase;
    private String[] methods, Selectors;

    public TexasSharpshooterPlot() {
        points = new ArrayList<>();
    }
    
    public void setConfigurations(int configurationType, int ConfBase){
        this.Location = "/e15p100g300/";
        this.MethodBase = "MODiTS";
                
        switch(configurationType){
            case 1: // Selection methods
                this.Selectors = new String[]{"Train-CV","Knee","Mean","KMeans"};
                this.methods = new String[]{"MODiTS"};
                break;
            case 2:
                this.Selectors = new String[]{"Train-CV"};
                this.methods = new String[]{"MODiTS", "EP", "SAX", "aSAX", "ESAX", "ESAXKMeans", "1D-SAX","RKMeans","SAXKMeans","rSAX","TD4C-Cos"};
                break;
            case 3:
                this.Selectors = new String[]{"KMeans"};
                this.methods = new String[]{"MODiTS", "EP", "SAX", "aSAX", "ESAX", "ESAXKMeans", "1D-SAX","RKMeans","SAXKMeans","rSAX","TD4C-Cos"};
                break;    
        }
        
        switch(ConfBase){
            case 1:
                this.SelectorBase = "Train-CV";
                break;
            case 2:
                this.SelectorBase = "Knee";
                break;
            case 3:
                this.SelectorBase = "Mean";
                break;    
            case 4:
                this.SelectorBase = "KMeans";
                break;
            default:
                this.SelectorBase = "Train-CV";
                break;
        }
    }
    
    public void execute(int configurationType, int ConfBase) throws MyException, Exception{
        this.setConfigurations(configurationType, ConfBase);
        for (int i = 1; i < DataSet.NUMBER_OF_DATASETS; i++) {
            if (!DataSet.DATASETS_IGNORED.contains(i)) {
                DataSet ds = new DataSet(i, false);
                System.out.println(ds.getName());
                TexasSharpshooterMethods base = new TexasSharpshooterMethods(this.SelectorBase, this.Location, this.MethodBase, ds);
                points.add(new TexasSharpshooterCollection(ds, this.methods, this.Selectors, base));
                ds.destroy();
            }
        }
        this.ExportPoints("TexasSConf"+configurationType+"ConfBase"+ConfBase+".dat");
    }
    
    public void ExportPoints(String filename) throws IOException{
        try (BufferedWriter br = new BufferedWriter(new FileWriter(filename))) {
            br.write(this.toString());
            br.close();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("x").append("   ").append("y").append("   ").append("label").append("\n");
        for(TexasSharpshooterCollection tsc: this.points){
            sb.append(tsc.toString());
        }
        return sb.toString();
    }
    
    
    
    public static void main(String[] args) throws Exception {
        TimeSeriesDiscretize_source.symbols = Utils.getListSymbols(); 
        TexasSharpshooterPlot tsp = new TexasSharpshooterPlot();
        tsp.execute(2, 1);
        System.out.println(tsp.toString());
    }
}
