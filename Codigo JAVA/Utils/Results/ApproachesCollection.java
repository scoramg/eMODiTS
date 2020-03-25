/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Results;

import DataSets.DataSet;
import Exceptions.MyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.python.google.common.primitives.Doubles;

/**
 *
 * @author amarquezgr
 */
public class ApproachesCollection {
    private int iDS;
    private List<Approach> approaches;

    public int getiDS() {
        return iDS;
    }

    public List<Approach> getApproaches() {
        return approaches;
    }

    public void setiDS(int iDS) {
        this.iDS = iDS;
    }

    public void setApproaches(List<Approach> approaches) {
        this.approaches = approaches;
    }

    public ApproachesCollection(int iDS) {
        this.iDS = iDS;
        approaches = new ArrayList<>();
    }
    
    public void addApproach(Approach approach){
        this.approaches.add(approach);
    }
    
    public Approach getApproachBase(){
        Approach appbase = new Approach();
        for(Approach app: this.approaches){
            if(app.isIsApproachBase()) {
                appbase = app.clone();
            }
        } 
        return appbase;
    }
    
    public String[] getApproachesName(){
        String[] names = new String[this.approaches.size()];
        for(int i=0; i< this.approaches.size();i++){
            names[i] = this.approaches.get(i).getName();
        }
        return names;
    }
    
    public void setRanks(){
//        Double[] MediasArray = {modits.getErrorRate(), ep.getErrorRate(), sax.getErrorRate(), asax.getErrorRate()};
        Double[] MediasArray = new Double[this.approaches.size()];
        for(int i=0; i< this.approaches.size();i++) 
            MediasArray[i] = this.approaches.get(i).getErrorRate();
        
        Set<Double> mediasSet = new HashSet<>(Arrays.asList(MediasArray));
        Double[] aux = new Double[mediasSet.size()];
        mediasSet.toArray(aux);
        double[] medias = Stream.of(aux).mapToDouble(Double::doubleValue).toArray();
        Arrays.sort(medias);

        for(int i=0; i< this.approaches.size();i++) {
            int rank = (Doubles.indexOf(medias, this.approaches.get(i).getErrorRate())+1);
            this.approaches.get(i).setRank(rank);
        }
    }
    
    public void CalculateStats(String test) throws MyException{
        Approach appbase = getApproachBase();
        for(int i=0; i< this.approaches.size();i++) {
            this.approaches.get(i).CalculateStats(appbase, test);
        }
    }

    @Override
    public String toString() {
        StringBuilder csvtable = new StringBuilder();
        csvtable.append(DataSet.getUCRRepository(this.iDS)).append(",");
        for(Approach app: this.approaches){
            csvtable.append(app.getErrorRate()).append(",").append(app.getStandardDeviation()).append(",").append(app.getRank()).append(",");
            if (!app.isIsApproachBase()){
                csvtable.append(app.getPvalue()).append(",").append(app.getH0()).append(",");
            }
            
        }
        csvtable.deleteCharAt(csvtable.length()-1).append("\n");
        return csvtable.toString();
    }
    
    
}
