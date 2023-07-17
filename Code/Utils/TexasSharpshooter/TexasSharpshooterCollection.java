/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.TexasSharpshooter;

import DataSets.DataSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amarquezgr
 */
public class TexasSharpshooterCollection {
    private List<TexasSharpshooterMethods> row;
    private DataSet ds;
    TexasSharpshooterMethods base;

    public TexasSharpshooterCollection(DataSet ds, String[] methods, String[] Selectors, TexasSharpshooterMethods base) throws Exception {
        row = new ArrayList<>();
        this.ds = ds;
        this.base =  base;
        addRow(methods,Selectors);
    }
    
    public void addRow(String[] methods, String[] selectors) throws Exception{
        for(String method: methods){
            for(String selector: selectors){
                if(!method.equals(base.getMethod()) || !selector.equals(base.getType_selection())){
                    TexasSharpshooterMethods met = new TexasSharpshooterMethods(selector, base.getLocation(), method, ds);
                    met.CalculateRatios(this.base);
                    this.row.add(met);
                }
            }
        }
    }
    
    public int size(){
        return this.row.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(TexasSharpshooterMethods tsm: this.row){
            sb.append(tsm.getExpected()).append("   ").append(tsm.getActual()).append("   ").append(tsm.getMethod()).append(tsm.getType_selection()).append("\n");
        }
        return sb.toString();
    }
    
    
    
//    public static void main(String[] args) throws MyException, Exception {
//        DataSet ds = new DataSet(10, false);
//        
//        String[] TypeSelection = new String[]{"Train-CV","Knee","Mean","KMeans"};
//        String[] approachesName = new String[]{"MODiTS"};
//        String Location = "/e15p100g300/";
//        TimeSeriesDiscretize_source.symbols = Utils.getListSymbols(); 
////                this.TypeSelection = new String[]{"Train-CV"};
////                this.approachesName = new String[]{"MODiTS", "EP", "SAX", "aSAX", "ESAX", "ESAXKMeans", "1D-SAX","RKMeans","SAXKMeans","rSAX","TD4C-Ent","TD4C-Cos","TD4C-KB","RAW"};
//        TexasSharpshooterMethods base = new TexasSharpshooterMethods("Train-CV", Location, "MODiTS", ds);
//        TexasSharpshooterCollection tsc = new TexasSharpshooterCollection(ds, approachesName, TypeSelection, base);
////        tsc.addRow(approachesName, TypeSelection);
//        System.out.println(tsc.toString());
//    }
    
}