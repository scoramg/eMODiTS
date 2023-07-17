/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL;

import Algorithms.operators.crossovers;
import DataTypes.ArraySortedUniqueList;
import Interfaces.SortedUniqueList;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mimath.MiMath;

/**
 *
 * @author amarquezgr
 */
public class HistogramHeightCuts {
    private SortedUniqueList<Integer> cuts;
    private int limit;

    public SortedUniqueList<Integer> getCuts() {
        return cuts;
    }

    public int getLimit() {
        return limit;
    }

    public void setCuts(SortedUniqueList<Integer> cuts) {
        this.cuts = cuts;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    private void Generate(){
        int num_cuts = MiMath.randomInt(2, this.limit-1);
        for(int i =0;i<num_cuts;i++){
            int new_valor = MiMath.randomInt(1, this.limit);
            this.cuts.add(new_valor);
        }
    }

    public HistogramHeightCuts() {
        cuts = new ArraySortedUniqueList<>();
        limit = 0;
    }

    public HistogramHeightCuts(int limit) {
        this.cuts = new ArraySortedUniqueList<>();
        this.limit = limit;
        this.Generate();
    }
    
    public int getElementAt(int index){
        return this.cuts.get(index);
    }
    
    public void addCut(int limit){
        if(this.cuts.size()<limit-1){
            int new_valor = MiMath.randomInt(0, limit);
            this.cuts.add(new_valor);
//            sort();
        }
    }
    
    public void InsertCut(int cut, int limit){
        if(this.cuts.size()<limit-1){
            this.cuts.add(cut);
//            sort();
        }
    }
    
    public HistogramHeightCuts Mutate(double MutationRate, int limit){
        //        Mutate alphabet cuts
        
        HistogramHeightCuts mutation = new HistogramHeightCuts(limit);
        
        for(int ac: this.cuts){
            if(Math.random() <= MutationRate){
                mutation.addCut(limit);
            }
            else {
                mutation.InsertCut(ac,limit);
            }
        }
        
        return mutation;
    }
    
    public void CreateOffspring(SortedUniqueList<Integer> a){
        SortedUniqueList<Integer> parent1 = new ArraySortedUniqueList<>(this.cuts);
        SortedUniqueList<Integer> parent2 = new ArraySortedUniqueList<>(a);
        SortedUniqueList<Integer> offspring = crossovers.DiscreteCrossover(parent1, parent2);
//        this.alphabet = new TreeSet<>(offspring);
        this.cuts = new ArraySortedUniqueList<>(offspring);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Alphabet: <");
        for(int a: this.cuts){
            sb.append(a).append(",");
        }
        sb.deleteCharAt(sb.length()-1).append(">");
        return sb.toString();
    }

    @Override
    public HistogramHeightCuts clone(){
        try {
            super.clone();
            HistogramHeightCuts clon = new HistogramHeightCuts(this.limit);
//            System.out.println(this.alphabet.size());
            for(int v:this.cuts){
                clon.getCuts().add(v);
            }
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(HistogramHeightCuts.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
