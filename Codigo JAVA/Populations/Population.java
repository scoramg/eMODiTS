/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Populations;

import Algorithms.operators.crossovers;
import DataSets.Data;
//import Algorithms.selection.TournamentSelection;
//import FitnessFunctions.ConfusionMatrix;
import Interfaces.IScheme;
import DataSets.DataSet;
import Exceptions.MyException;
import Interfaces.IPopulation;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
//import ca.nengo.io.MatlabExporter;
import parameters.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class Population implements IPopulation, Cloneable{
    private IScheme[] individuals;
    private boolean isSelfAdaptation;
    private int length;
    private double MutationSuccessRate;
    private double ChangeRate;
    private double CrossoverSuccessRate;
    
    @Override
    public IScheme[] getIndividuals() {
        return individuals;
    }

    @Override
    public int getLength() {
        return length;
    }

    public boolean isIsSelfAdaptation() {
        return isSelfAdaptation;
    }
    
    public void setIndividuals(IScheme[] individuals) {
        this.individuals = individuals;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
        this.individuals = new IScheme[length];
    }

    public void setIsSelfAdaptation(boolean isSelfAdaptation) {
        this.isSelfAdaptation = isSelfAdaptation;
    }

    @Override
    public double getMutationSuccessRate() {
        return this.MutationSuccessRate;
    }

    @Override
    public double getCrossoverSuccessRate() {
        return CrossoverSuccessRate;
    }
    
    @Override
    public double getChangeRate() {
        return ChangeRate;
    }
    
    public Population() {
        this.MutationSuccessRate = 0;
        this.ChangeRate = 0;
        this.CrossoverSuccessRate = 0;
    }
    
    public Population(int PopulationSize, boolean isSelfAdaptation) {
        this.setLength(PopulationSize);
        this.setIsSelfAdaptation(isSelfAdaptation);
        this.individuals = new IScheme[PopulationSize];
        this.MutationSuccessRate = 0;
        this.ChangeRate = 0;
        this.CrossoverSuccessRate = 0;
    }
    
//    public Population(int PopulationSize, int iApproach, boolean isSelfAdaptation, DataSet ds, int iFitnessFunctionConf) {
//        this.MutationSuccessRate = 0;
//        this.ChangeRate = 0;
//        this.CrossoverSuccessRate=0;
//        this.setLength(PopulationSize);
//        this.setIsSelfAdaptation(isSelfAdaptation);
//        this.individuals = new IScheme[PopulationSize];
//        for(int i=0;i<PopulationSize;i++){
//            switch(iApproach){
//                case 0: //One Alphabet
//                    this.individuals[i] = new Individuals.PEVOMO.Scheme(new Individuals.PEVOMO.Word(ds.getDimensions()[1]), new Individuals.PEVOMO.Alphabet(ds.getLimits()), this.isSelfAdaptation,iFitnessFunctionConf);
//                    this.individuals[i].adjust(ds);
//                    break;
//                case 1: //Several Alphabet
//                    this.individuals[i] = new Individuals.Proposal.Scheme(ds, TimeSeriesDiscretize_source.MIN_NUMBER_OF_ALPHABET_CUTS, TimeSeriesDiscretize_source.MAX_NUMBER_OF_ALPHABET_CUTS, this.isSelfAdaptation,iFitnessFunctionConf);
//                    this.individuals[i].adjust(ds);
//                    break;    
//                
//            }
//        }
//    }
    
    @Override
    public void Generate(int iApproach, Data ds, double[][] limits, int iFitnessFunctionConf){
        System.out.println(this.getName());
        for(int i=0;i<this.length;i++){
            switch(iApproach){
                case 0: //One Alphabet
                    this.individuals[i] = new Individuals.PEVOMO.Scheme(new Individuals.PEVOMO.Word(ds.getDimensions()[1]), new Individuals.PEVOMO.Alphabet(limits),this.isSelfAdaptation,iFitnessFunctionConf);
                    this.individuals[i].adjust(ds);
                    break;
                case 1: //Several Alphabet
                    this.individuals[i] = new Individuals.Proposal.Scheme(ds, limits, TimeSeriesDiscretize_source.MIN_NUMBER_OF_ALPHABET_CUTS, TimeSeriesDiscretize_source.MAX_NUMBER_OF_ALPHABET_CUTS, this.isSelfAdaptation,iFitnessFunctionConf);
                    this.individuals[i].adjust(ds);
                    break;    
                
            }
        }
    }
    
    @Override
    public void Evaluate(Data ds, double[] weights){
        for(int i=0;i<this.length;i++){
            this.individuals[i].evaluate(ds, weights);
        }
    }
    
    @Override
    public IScheme getBest(){
        double min = Double.POSITIVE_INFINITY;
        int ind = -1;
        for(int i=0;i<this.length;i++){
            if(this.individuals[i].getFitnessValue() < min){
                min = this.individuals[i].getFitnessValue();
                ind = i;
            }
        }
        if(ind != -1)
            return this.individuals[ind];
        else
            return null;
    }
    
    @Override
    public IScheme getBestByErrorRate(){
        double min = Double.POSITIVE_INFINITY;
        int ind = -1;
        for(int i=0;i<this.length;i++){
            if(this.individuals[i].getErrorRate()< min){
                min = this.individuals[i].getErrorRate();
                ind = i;
            }
        }
        if(ind != -1)
            return this.individuals[ind];
        else
            return null;
    }
    
    @Override
    public Population Mutate(double MutationRate, Data ds, double[][] limits){
        Population offsprings = new Population(this.length, this.isSelfAdaptation);
        for(int i=0;i<this.length;i++){
            offsprings.individuals[i] = this.individuals[i].Mutate(MutationRate, this.isSelfAdaptation, ds, limits).clone();
            offsprings.individuals[i].adjust(ds);
        }
        return offsprings;
    }
    
    @Override
    public void CalculateMutationPerformance(IPopulation offsprings){
        
        int count_msr=0;
        double suma_cr = 0;
        for(int i=0;i<this.length;i++){
            if (this.individuals[i].compareTo(offsprings.getIndividuals()[i]) == 1){ //El hijo mejoro al padre
                count_msr++;
            }
            
            suma_cr += Utils.Utils.IndividualSimilarityMeasure(this.individuals[i], offsprings.getIndividuals()[i]);
        }
        
        this.MutationSuccessRate = (double) count_msr / (double) this.length;
        this.ChangeRate = (double) suma_cr/ (double) this.length;
        
    }
    
    @Override
    public IPopulation Join(IPopulation p){
        Population joined = new Population(this.length + p.getLength(), this.isSelfAdaptation);
        for(int i=0;i<this.individuals.length;i++) joined.individuals[i] = this.individuals[i];
        
        for(int j=0;j<p.getIndividuals().length;j++) joined.individuals[this.length+j] = p.getIndividuals()[j];
        
        return joined;
    }
    
//    @Override
//    public void Replace(TournamentSelection ts){
//        int w=0;
////        System.out.println("ts.getVictories().size():"+ts.getVictories().size());
//        for(IScheme individual: ts.getVictories().keySet()){
//            if(w < this.length) this.individuals[w] = individual;
//            w++;
//        }
//    }
//    
    @Override
    public void Replace(IPopulation pop){
//        int w=0;
//        for(IScheme individual: pop.getIndividuals()){
        pop.sort();
        for(int i=0; i<this.length; i++){
            this.individuals[i] = pop.getIndividuals()[i].clone();
//            if(w < this.length) this.individuals[w] = individual;
//            w++;
        }
    }
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Population length: ").append(this.length).append("\n");
        for(int i=0;i<this.length;i++){
            sb.append(this.individuals[i].toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Population clone() {
        try {
            super.clone();
            Population clon = new Population();
            clon.setLength(this.length);
            clon.setIsSelfAdaptation(this.isSelfAdaptation);
            clon.setIndividuals(this.individuals.clone());
//            clon.individuals = new IScheme[this.length];
//            for(int i=0;i<this.individuals.length;i++){
//                clon.individuals[i] = this.individuals[i].clone();
//            }
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public String IndividualsSize2String(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.length;i++){
            sb.append(this.individuals[i].getNumberWordCuts()).append(",");
        }
        sb.deleteCharAt(sb.length()-1).append("\n");
        return sb.toString();
    }
    
    public String IndividualsSizeFitness2String(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.length;i++){
            sb.append(this.individuals[i].getNumberWordCuts()).append("(").append(this.individuals[i].getFitnessValue()).append(")\n");
        }
//        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
    public String IndividualsFitness2String(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.length;i++){
            sb.append(this.individuals[i].getFitnessValue()).append(",");
        }
//        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
    
    public String totalCuts2String(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.length;i++){
            for(Integer value:this.individuals[i].getTotalCuts()){
                sb.append(value).append(",");
            }
            sb.deleteCharAt(sb.length()-1).append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public void sort(){
        Arrays.sort(this.getIndividuals());
    }
    
    @Override
    public IPopulation Extract(IPopulation pop, int newSize){
        Population population = new Population(newSize, pop.isIsSelfAdaptation());
        
        for (int i=0;i<newSize;i++){
            population.individuals[i] = pop.getIndividuals()[i].clone();
        }
        
        return population;
    }
    
    @Override
    public void setAt(IScheme ind, int j){
        this.individuals[j] = ind.clone();
    }
    
    @Override
    public List<Double> getAllFitnessValue(){
        List<Double> fitness = new ArrayList<>();
        for (int i=0; i<this.length;i++){
           fitness.add(this.individuals[i].getFitnessValue());
        }
        return fitness;
//        double[] fit_arr = new double[this.length];
//        for (int i=0; i<this.length;i++){
//            fit_arr[i] = this.individuals[i].getFitnessValue();
//        }
//        return fit_arr;
    }
    
    @Override
    public double getTotalFitness() {
        double total = 0.0;
        for (int i=0;i<this.length;i++){
            total += this.individuals[i].getFitnessValue();
        }
        return total;
    }
    
    public void replaceAt(int i, IScheme new_individual) {
        this.individuals[i] = new_individual.clone();
    }
    
    public Population CreateOffsprings(Generals general_params, Data ds){
        int k = this.length-1;
        Population offsprings = new Population(this.length, this.isSelfAdaptation);
        
        int o=0, count_cr=0;
        
        for (int i = 0; i < this.length; i++){
            if (i < k){
                Random rnd = new Random();
                double num = rnd.nextDouble();
//                System.out.println("num:"+num+" ,PC="+CrossOverRate);
                if (num <= general_params.getCrossOverRate()){
//                    System.out.println("1");
                    IScheme[] offspring = new IScheme[2];
//                    switch(general_params.getiApproach()){
//                        case 1:
                            offspring = crossovers.OnePoint(this.individuals[i], this.individuals[k],general_params.getiApproach());
//                            System.out.println("offspring[0].cuts:"+offspring[0].PrintCuts());
//                            System.out.println("offspring[1].cuts:"+offspring[1].PrintCuts());
                            offspring[0].adjust(ds);
                            offspring[1].adjust(ds);
//                            offspring[0].evaluate(local_params.getDs(), general_params.getWeights());
//                            offspring[1].evaluate(local_params.getDs(), general_params.getWeights());
                            if (this.individuals[i].compareTo(offspring[0]) == 1){ //El hijo mejoro al padre
                                count_cr++;
                            }
                            if (this.individuals[k].compareTo(offspring[1]) == 1){ //El hijo mejoro al padre
                                count_cr++;
                            }
//                            break;
//                    }
                    
                    for (int j=0;j<2;j++){
                        offsprings.replaceAt(o, offspring[j]);
                        o++;
                    }
                } else {
                    offsprings.replaceAt(o, this.individuals[i]);
                    o++;
                    offsprings.replaceAt(o, this.individuals[k]);
                    o++;
                }
            }
            k--;
        }
        
        this.CrossoverSuccessRate = (double) count_cr / (double) this.length;
        return offsprings;
    }
    
    @Override
    public void reset() {
        this.MutationSuccessRate = 0;
        this.ChangeRate = 0;
        this.CrossoverSuccessRate = 0;
        this.setLength(0);
        this.individuals = new IScheme[0];
    }
    
    @Override
    public String getName() {
        return Population.class.getName();
    }

    @Override
    public float[][] getArrayFitnessValuesWithRank() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void addIndividual(IScheme individual){
        IScheme[] newIndividuals = new IScheme[this.length+1];
        for(int i=0;i<this.length;i++) newIndividuals[i] = this.individuals[i].clone();
        newIndividuals[this.length] = individual.clone();
        this.setLength(this.length+1);
        this.individuals = newIndividuals.clone();
    }
    
    @Override
    public void Classify(DataSet ds, boolean UsingTest){
        for(int i=0;i<this.length;i++){
            this.individuals[i].Classify(ds, UsingTest, "train");
        }
    }
    
    public static void main(String[] args) {
        int PS = 50;
        int iApproach = 1; // 0 - PEVOMO, 1 - Proposal
        int iFitnessFunctionConf = 3; //1 Rechy (3 funciones), 2 TDFunction, 3 Rechy(Entropia y Complejidad), 4 rfEntropyCompression, 5 rfComplexityCompression
        int iDS = 1;
//        MOPopulation.ExportInitialPopulation(PS, iApproach, iFitnessFunctionConf, iDS);
        
        try {

            DataSet ds = new DataSet(iDS);
            Population pop = new Population(PS, false);
            pop.Generate(iApproach, ds.getTrain(), ds.getLimits(), iFitnessFunctionConf);
            double[] weights = new double[3];
            weights[0] = 0.9;
            weights[1] = 0.09;
            weights[2] = 0.009;
            pop.Evaluate(ds.getTrain(), weights);
//            List<ParetoFront> Fronts = NSGA2.FastNonDominatedSort(pop);
            pop.Classify(ds, true);
            
            System.out.println("Best Classification scheme:");
            System.out.println(pop.getBestByErrorRate().toString());
            
            Population childs = pop.Mutate(0.8, ds.getTrain(), ds.getLimits());
            childs.Evaluate(ds.getTrain(), weights);
            childs.Classify(ds, true);
            
            System.out.println("Best Classification child scheme:");
            System.out.println(pop.getBestByErrorRate().toString());
            
        } catch (MyException ex) {
            Logger.getLogger(MOPopulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
