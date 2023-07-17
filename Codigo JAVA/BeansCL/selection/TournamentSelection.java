/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL.selection;

//import Algorithms.selection.*;
//import Interfaces.*;
import BeansCL.HistogramScheme;
import BeansCL.Population;
import Utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author amarquezgr
 */
public class TournamentSelection{
    private Map<HistogramScheme,Integer> victories;
    int number_opponents;
    public Population selected;

    public Map<HistogramScheme, Integer> getVictories() {
        return victories;
    }

    public void setVictories(Map<HistogramScheme, Integer> victories) {
        this.victories = victories;
    }
    
    public TournamentSelection() {
        victories = new HashMap<>();
        number_opponents = 0;
    }
    
    public TournamentSelection(Population population, int number_opponents) {
        victories = new HashMap<>();
        this.number_opponents = number_opponents;
        Execute(population);
    }

    public Population getSelected() {
        return selected;
    }
    
    public void Execute(Population population) {
        this.selected = population.clone();
        this.selected.reset();
        this.selected.setLength(population.getLength());
        for(int i =0; i < population.getLength(); i++){
            List<Integer> opponents = new ArrayList<>();
            opponents.add(i);
            victories.put(population.getIndividuals()[i], 0);
            for(int j=0; j<number_opponents; j++){
                int opponent = mimath.MiMath.randomInt(0, population.getLength()-1, opponents);
//                if (population.getIndividuals()[i].getFitnessValue() < population.getIndividuals()[opponent].getFitnessValue()){
                if (population.getIndividuals()[i].compareTo(population.getIndividuals()[opponent]) < 0){
                    int victory = victories.get(population.getIndividuals()[i]);
                    victories.put(population.getIndividuals()[i], victory+1);
                }
                opponents.add(opponent);
            }
        }
        victories = Utils.SortMapByValue(victories);
        
        int w=0;
        for(HistogramScheme individual: this.getVictories().keySet()){
            if(w < population.getLength()) this.selected.getIndividuals()[w] = individual;
            w++;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Victories List:\n");
        for(HistogramScheme v: this.victories.keySet()){
            sb.append(v.toString()).append("Victories: ").append(victories.get(v)).append("\n\n");
        }
        return sb.toString();
    }
    
    public String IndividualsSizeFitness2String(){
//        StringBuilder sb = new StringBuilder();
//        for(int i=0;i<this.length;i++){
//            sb.append(this.individuals[i].getNumberWordCuts()).append("(").append(this.individuals[i].getFitnessValue()).append("),");
//        }
//        sb.deleteCharAt(sb.length()-1).append("\n");
//        return sb.toString();
//        
        StringBuilder sb = new StringBuilder();
        sb.append("Victories List:\n");
        for(HistogramScheme v: this.victories.keySet()){
            sb.append(v.getNumberWordCuts()).append("(").append(v.getFitnessValue()).append(" - Victories: ").append(victories.get(v)).append("\n");
        }
//        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

}
