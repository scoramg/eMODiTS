/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Individuals.PEVOMO;

import Algorithms.operators.crossovers;
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
import org.python.google.common.collect.Iterables;

/**
 *
 * @author amarquezgr
 */
public class Alphabet implements Cloneable {
//    private TreeSet<Double> alphabet;
    private SortedUniqueList<Double> alphabet;
//    private int mincuts;
//    private int maxcuts;

//    public TreeSet<Double> getAlphabet() {
    public SortedUniqueList<Double> getAlphabet() {
        return alphabet;
    }

//    public void setAlphabet(TreeSet<Double> alphabet) {
    public void setAlphabet(SortedUniqueList<Double> alphabet) {
        this.alphabet = alphabet;
    }
    
    public void setAlphabet(List<Double> alphabet) {
        this.alphabet.addAll(alphabet);
    }

    public Alphabet() {
        this.alphabet = new ArraySortedUniqueList<>();
//        this.alphabet = new TreeSet<>();
    }

    public Alphabet(double[][] limits) {
        this.alphabet = new ArraySortedUniqueList<>();
//        this.alphabet = new TreeSet<>();
        int num_cuts = MiMath.randomInt(TimeSeriesDiscretize_source.MIN_NUMBER_OF_ALPHABET_CUTS, TimeSeriesDiscretize_source.MAX_NUMBER_OF_ALPHABET_CUTS);
        for(int i =0;i<num_cuts;i++){
            double new_valor = MiMath.random(limits[0][0], limits[0][1]);
            this.alphabet.add(new_valor);
        }
//        sort();
    }
    
    public void Generate(double[][] limits) {
        int num_cuts = MiMath.randomInt(TimeSeriesDiscretize_source.MIN_NUMBER_OF_ALPHABET_CUTS, TimeSeriesDiscretize_source.MAX_NUMBER_OF_ALPHABET_CUTS);
        for(int i =0;i<num_cuts;i++){
            double new_valor = MiMath.random(limits[0][0], limits[0][1]);
            this.alphabet.add(new_valor);
        }
//        sort();
    }
    
//    public void sort(){
//        Collections.sort(this.alphabet);
//    }
    
    public Double getElementAt(int index){
//        List<Double> auxiliar = new ArrayList<>(this.alphabet);
//        return auxiliar.get(index);
        return this.alphabet.get(index);
//        Iterator<Double> iterator = this.alphabet.iterator();
//        int i=0;
//        while(iterator.hasNext()) {
//            Double val = iterator.next();
//            if(i==index){
//                return val;
//            }
//            i++;
//        }
//
//        return null;
        
    }
    
    public void addCut(double[][] limits){
        if(this.alphabet.size()<TimeSeriesDiscretize_source.MAX_NUMBER_OF_ALPHABET_CUTS){
            double new_valor = MiMath.random(limits[0][0], limits[0][1]);
            this.alphabet.add(new_valor);
//            sort();
        }
    }
    
    public void InsertCut(double cut){
        if(this.alphabet.size()<TimeSeriesDiscretize_source.MAX_NUMBER_OF_ALPHABET_CUTS){
            this.alphabet.add(cut);
//            sort();
        }
    }
    
//    public void deleteCut(int index){
//        if(this.alphabet.size()>TimeSeriesDiscretize_source.MIN_NUMBER_OF_ALPHABET_CUTS){
//            this.alphabet.remove(index);
//            sort();
//        }
//    }
//    
//    public void updateCut(int index, double[][] limits){
//        double new_valor = MiMath.random(limits[0][0], limits[0][1]);
//        this.alphabet.set(index, new_valor);
//        sort();
//    }
    
    public void adjust(double[][] limits){
        this.alphabet.add(limits[0][0]);
        this.alphabet.add(limits[0][1]);
//        this.getAlphabet().set(0, limits[0][0]);
//        this.getAlphabet().set(this.getAlphabet().size()-1, limits[0][1]);
    }
    
    public int getAlphabetByGini(List<Double> data, double[][] limits){
        double[] interval = new double[2];
        int number = 0;
        double minginivalue = Double.POSITIVE_INFINITY;
//        interval[0] = this.getElementAt(0);
        interval[0] = limits[0][0];
        
        for(int i=0; i<this.alphabet.size();i++){
            interval[1] = this.getElementAt(i);
            int cont=0;
            for(Double d:data){
//                System.out.println("d="+d);
                if (d >= interval[0] && d <= interval[1]){
                    cont++;
                }
            }
            double ginivalue = Double.POSITIVE_INFINITY;
            if (cont > 0){
                ginivalue = 1 - Math.pow(((double) cont/data.size()), 2);
            }
//            System.out.println("alfabeto="+i+1+", ginivalue="+ginivalue + ", cont="+cont+", data.size()="+data.size());
            if (ginivalue < minginivalue){
                minginivalue = ginivalue;
                number = i+1;
            }
            interval[0] = this.getElementAt(i);
        }
        interval[1] = limits[0][1];
        
        int cont=0;
        for(Double d:data){
//                System.out.println("d="+d);
            if (d >= interval[0] && d <= interval[1]){
                cont++;
            }
        }
        double ginivalue = Double.POSITIVE_INFINITY;
        if (cont > 0){
            ginivalue = 1 - Math.pow(((double) cont/data.size()), 2);
        }
//        System.out.println("alfabeto="+this.alphabet.size()+", ginivalue="+ginivalue + ", cont="+cont+", data.size()="+data.size());
        if (ginivalue < minginivalue){
            minginivalue = ginivalue;
            number = this.alphabet.size();
        }
        
        return number;
    }
    
    public int getAlphabetByAvg(double media){ //debe estar ajustado
        double[] interval = new double[2];
        int number = 0;
        
        interval[0] = this.getElementAt(0);
        interval[1] = this.getElementAt(0);
        
        if (media < interval[0]) number = 1;
        else {
            for(int i=1; i<this.alphabet.size();i++){
//                interval[1] = this.alphabet.get(i);
                interval[1] = this.getElementAt(i);
                if (media >= interval[0] && media <= interval[1]){
                    number = i+1;
                }
//                interval[0] = this.alphabet.get(i);
                interval[0] = this.getElementAt(i);
            }
            if (media > interval[1]) number = this.alphabet.size()+1;
        }
        if(number==0){
            System.out.println("Alphabet="+this.toString());
            System.out.println("number="+number + ", media=" + media + ", interval[1]="+interval[1]);
        }
        return number;
    }
    
    public Alphabet Mutate(double MutationRate, double[][] limits){
        //        Mutate alphabet cuts
        
        Alphabet mutation = new Alphabet();
        
        for(Double ac: this.alphabet){
            if(Math.random() <= MutationRate){
                mutation.addCut(limits);
            }
            else {
                mutation.InsertCut(ac);
            }
        }
        
        return mutation;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Alphabet: <");
        for(Double a: this.alphabet){
            sb.append(a).append(",");
        }
        sb.deleteCharAt(sb.length()-1).append(">");
        return sb.toString();
    }

    @Override
    public Alphabet clone(){
        try {
            super.clone();
            Alphabet clon = new Alphabet();
//            System.out.println(this.alphabet.size());
            for(Double v:this.alphabet){
                clon.getAlphabet().add(v);
            }
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void ImportFromMatFile(String filename){
        try {
            MatFileReader mfr = new MatFileReader(filename);
            Map<String, MLArray> mlArrayRetrived = mfr.getContent();
            
            MLArray w = mlArrayRetrived.get("alphabet");
            double[] arr = ((MLDouble) w).getArray()[0];
            for(double d: arr){
                this.alphabet.add(d);
            }
        } catch (IOException ex) {
            Logger.getLogger(Alphabet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    public void join(TreeSet a){
//        this.alphabet.addAll(a);
//    }
//    public void CreateOffspring(TreeSet a){
    public void CreateOffspring(SortedUniqueList a){
        List<Double> parent1 = new ArrayList<>(this.alphabet);
        List<Double> parent2 = new ArrayList<>(a);
        List<Double> offspring = crossovers.DiscreteCrossover(parent1, parent2);
//        this.alphabet = new TreeSet<>(offspring);
        this.alphabet = new ArraySortedUniqueList<>(offspring);
    }

}
