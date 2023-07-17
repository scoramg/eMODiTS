/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class HistogramWidthCut implements Comparable<HistogramWidthCut>, Cloneable  {
    private int cut;
    private HistogramHeightCuts height_cuts;

    public int getCut() {
        return cut;
    }

    public HistogramHeightCuts getHeight_cuts() {
        return height_cuts;
    }

    public void setCut(int cut) {
        this.cut = cut;
    }

    public void setHeight_cuts(HistogramHeightCuts height_cuts) {
        this.height_cuts = height_cuts;
    }
    
    public HistogramWidthCut() {
        cut = 0;
        height_cuts = new HistogramHeightCuts();
    }

    public HistogramWidthCut(int cut) {
        this.cut = cut;
        height_cuts = new HistogramHeightCuts();
    }

    public HistogramWidthCut(int cut, int limit) {
        this.cut = cut;
        height_cuts = new HistogramHeightCuts(limit);
    }

    public HistogramWidthCut(int cut, HistogramHeightCuts height_cuts) {
        this.cut = cut;
        this.height_cuts = height_cuts;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WidthCut = ").append(this.cut).append(" (").append(this.height_cuts.toString()).append(")").append("\n");
        return sb.toString();
    }

    @Override
    public int compareTo(HistogramWidthCut o) {
        int value = 0;
        if (this.cut < o.cut){
            value  = -1;
        } else {
            if(this.cut > 0){
                value = 1;
            }
        }
        
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.cut;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HistogramWidthCut other = (HistogramWidthCut) obj;
        if (this.cut != other.cut) {
            return false;
        }
        return true;
    }
    
    @Override
    public HistogramWidthCut clone(){
        try {
            super.clone();
            HistogramWidthCut clon = new HistogramWidthCut();
            clon.setCut(this.cut);
            clon.setHeight_cuts(this.height_cuts);
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(HistogramWidthCut.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    public int[] toArray(){
        
        int[] array = new int[this.height_cuts.getCuts().size() + 1];
        array[0] = this.cut;
        for(int i=0;i<this.height_cuts.getCuts().size();i++){
            array[i+1] = this.height_cuts.getElementAt(i);
        }
        return array;
    }
}
