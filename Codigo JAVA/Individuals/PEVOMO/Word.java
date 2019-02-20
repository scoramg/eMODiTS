/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Individuals.PEVOMO;

import Interfaces.SortedUniqueList;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
import DataTypes.ArraySortedUniqueList;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import mimath.MiMath;

/**
 *
 * @author amarquezgr
 */
public class Word implements Cloneable{
//    private TreeSet<Integer> word;
    private SortedUniqueList<Integer> word;
    
//    public TreeSet<Integer> getWord() {
    public SortedUniqueList<Integer> getWord() {
        return this.word;
    }

//    public void setWord(TreeSet<Integer> word) {
    public void setWord(SortedUniqueList<Integer> word) {
        this.word = word;
    }
    
//    public void setWord(List<Integer> word) {
//        this.word.addAll(word);
//    }

    public Word() {
//        this.word = new TreeSet<>();
        this.word = new ArraySortedUniqueList<>();
    }
    
    
    public Word(int ds_length) {
        this.word = new ArraySortedUniqueList<>();
//        this.word = new TreeSet<>();
        double coef = (Math.floor(Math.random()*5)+1)/10;
        int numcuts = (int) Math.floor(coef*ds_length);
        for(int i=0; i<numcuts;i++){
//            int new_valor = MiMath.randomInt(TimeSeriesDiscretize_source.MIN_NUMBER_OF_WORD_CUTS, ds_length-1, this.word);
            int new_valor = MiMath.randomInt(TimeSeriesDiscretize_source.MIN_NUMBER_OF_WORD_CUTS, ds_length-1, this.word.toList());
            this.word.add(new_valor);
        }
//        sort();
    }
    
    public void Generate(int ds_length) {
        double coef = (Math.floor(Math.random()*5)+1)/10;
        int numcuts = (int) Math.floor(coef*ds_length);
        for(int i=0; i<numcuts;i++){
//            int new_valor = MiMath.randomInt(TimeSeriesDiscretize_source.MIN_NUMBER_OF_WORD_CUTS, ds_length-1, this.word);
            int new_valor = MiMath.randomInt(TimeSeriesDiscretize_source.MIN_NUMBER_OF_WORD_CUTS, ds_length-1, this.word.toList());
            this.word.add(new_valor);
        }
//        sort();
    }
    
//    public void sort(){
//        Collections.sort(this.word);
//    }
    
    public Integer getElementAt(int index){
//        List<Integer> auxiliar = new ArrayList<>(this.word);
        return this.word.get(index);
//        Iterator<Integer> iterator = this.word.iterator();
//        int i=0;
//        while(iterator.hasNext()) {
//            Integer val = iterator.next();
//            if(i==index){
//                return val;
//            }
//            i++;
//        }
//
//        return null; 
    }
    
    public void replaceAt(int index, int value){
//        SortedUniqueList<Integer> newword = new ArraySortedUniqueList<>(this.word);
////        TreeSet<Integer> newword = new TreeSet<>();
//        for(int i=0;i<this.word.size();i++){
//            if (i == index){
//                newword.add(value);
//            } else {
//                newword.add(this.getElementAt(i));
//            }
//        }
//        this.setWord(newword);
//        return auxiliar.get(index);
        this.word.remove(index);
        this.addWordCut(value);
    }
    
    public void addCut(int ds_length){
        if(this.word.size() < (TimeSeriesDiscretize_source.MAX_NUMBER_OF_WORD_CUTS * ds_length)){
            int new_valor = MiMath.randomInt(2, ds_length-1, this.word.toList());
//            int new_valor = MiMath.randomInt(2, ds_length-1, this.word);
            this.word.add(new_valor);
        }
    }
    
//    public int addCut(int ds_length, TreeSet<Integer> excluir){
//        int res = -1;
//        if(this.word.size() < (TimeSeriesDiscretize_source.MAX_NUMBER_OF_WORD_CUTS * ds_length)){
//            int new_valor = MiMath.randomInt(2, ds_length-1, excluir);
//            if(new_valor > 0){
//                this.word.add(new_valor);
//                res = new_valor;
//            }
//        } 
//        return res;
//    }
    
    public void addCut(int ds_length, SortedUniqueList<Integer> excluir){
        if(this.word.size() < (TimeSeriesDiscretize_source.MAX_NUMBER_OF_WORD_CUTS * ds_length)){
            int new_valor = MiMath.randomInt(2, ds_length-1, excluir.toList());
            if(new_valor > 0){
                this.word.add(new_valor);
            }
        } 
    }
    
    public void addWordCut(int cut){
        this.word.add(cut);
    }
    
    public void InsertCut(int cut, int ds_length){
        if(!this.word.contains(cut)){
            if(this.word.size() < (TimeSeriesDiscretize_source.MAX_NUMBER_OF_WORD_CUTS * ds_length)){
                this.word.add(cut);
            }
        }
    }
    
    public void adjust(int ds_length) {
//        this.getWord().set(this.getWord().size()-1, ds_length-1);
        this.getWord().add(ds_length-1);
    }
    
    public Word Mutate(double MutationRate, int length){
//        Word mutation = new Word();
        Word mutation = this.clone();
//        List<int[]> mutated = new ArrayList<>();
//        TreeSet<Integer> excluir;
        SortedUniqueList<Integer> excluir;
//        for(Integer wc: this.getWord()){
        for(int i=0;i<this.word.size();i++){
            
//            excluir = new TreeSet<>(mutation.word);
            excluir = new ArraySortedUniqueList<>(mutation.word);
            
            if(Math.random() <= MutationRate){
//                int cut = MiMath.randomInt(2, length-1, excluir);
                int cut = MiMath.randomInt(2, length-1, excluir.toList());
                if (cut > 0){
                    mutation.replaceAt(i, cut);
                }
            }
        }
        
        if(this.word.size()!=mutation.getWord().size()){
            System.out.println("this.word:"+this.toString()+", offspring.cuts="+mutation.toString());
            System.out.println("this.word.size="+this.word.size()+", mutation.getWord().size="+mutation.getWord().size());
        }
        
        return mutation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Palabra: <");
        for(Integer p: this.word){
            sb.append(p).append(",");
        }
        sb.deleteCharAt(sb.length()-1).append(">");
        return sb.toString();
    }

    @Override
    public Word clone() {
        try {
            super.clone();
            Word clon = new Word();
            for(Integer v:this.word){
                clon.getWord().add(v);
            }
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public int size(){
        return this.word.size();
    }
    
    public void ImportFromMatFile(String filename){
        try {
            MatFileReader mfr = new MatFileReader(filename);
            Map<String, MLArray> mlArrayRetrived = mfr.getContent();
            
            MLArray w = mlArrayRetrived.get("word");
            double[] arr = ((MLDouble) w).getArray()[0];
            for(double d: arr){
                this.word.add((int) d);
            }
        } catch (IOException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
