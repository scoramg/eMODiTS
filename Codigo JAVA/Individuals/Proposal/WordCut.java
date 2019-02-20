/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Individuals.Proposal;

import Individuals.PEVOMO.Alphabet;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.ArrayList;
//import java.util.List;
//import mimath.MiMath;

/**
 *
 * @author amarquezgr
 */
public class WordCut implements Comparable<WordCut>, Cloneable {
    private int cut;
    private Alphabet alphabet;
    
    public int getCut() {
        return cut;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    
    public void setCut(int cut) {
        this.cut = cut;
    }

    public void setAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet.clone();
    }
    
    public WordCut() {
        cut = 0;
        alphabet = new Alphabet();
    }

    public WordCut(int cut) {
        this.cut = cut;
        alphabet = new Alphabet();
    }

    public WordCut(int cut, double[][] limits) {
        this.cut = cut;
        alphabet = new Alphabet(limits);
    }

    public WordCut(int cut, Alphabet alphabet) {
        this.cut = cut;
        this.alphabet = alphabet;
    }
    
    
    
//    public void updateAlphabetCut(int index, double[][] limits){
//        alphabet.updateCut(index, limits);
//    }
//    
//    public void deleteAlphabetCut(int index){
//        alphabet.deleteCut(index);
//    }
//    
//    public void addAlphabetCut(double[][] limits){
//        this.alphabet.addCut(limits);
//    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WordCut = ").append(cut).append(" (").append(this.alphabet.toString()).append(")").append("\n");
//        sb.append("WordCut = ").append(cut).append(",");
//        sb.append(cut);
        return sb.toString();
    }

    @Override
    public int compareTo(WordCut o) {
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
        final WordCut other = (WordCut) obj;
        if (this.cut != other.cut) {
            return false;
        }
        return true;
    }
    
    @Override
    protected WordCut clone(){
        try {
            super.clone();
            WordCut clon = new WordCut();
            clon.setCut(this.cut);
            clon.setAlphabet(this.alphabet);
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(WordCut.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    public double[] toDoubleArray(){
        
        double[] array = new double[this.alphabet.getAlphabet().size() + 1];
        array[0] = this.cut;
        for(int i=0;i<this.alphabet.getAlphabet().size();i++){
            array[i+1] = this.alphabet.getElementAt(i);
        }
        return array;
    }
}
