/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Populations;

import Algorithms.operators.crossovers;
import DataSets.Data;
//import Algorithms.selection.TournamentSelection;
import Interfaces.*;
import DataSets.DataSet;
import Exceptions.MyException;
import ParetoFront.ParetoFront;
import ca.nengo.io.MatlabExporter;
import java.io.File;
import java.io.IOException;
import parameters.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import methods.multiobjective.NSGA2;

/**
 *
 * @author amarquezgr
 */
public class MOPopulation implements IPopulation, Cloneable{
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

    /**
     *
     * @return
     */
    @Override
    public int getLength() {
        return length;
    }

    @Override
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
    
    public MOPopulation() {
        this.MutationSuccessRate = 0;
        this.ChangeRate = 0;
        this.CrossoverSuccessRate = 0;
    }
    
    public MOPopulation(int PopulationSize, boolean isSelfAdaptation) {
        this.setLength(PopulationSize);
        this.setIsSelfAdaptation(isSelfAdaptation);
        this.individuals = new IScheme[PopulationSize];
        this.MutationSuccessRate = 0;
        this.ChangeRate = 0;
        this.CrossoverSuccessRate = 0;
    }
    
//    public MOPopulation(int PopulationSize, int iApproach, boolean isSelfAdaptation, int iFitnessFunctionConf, DataSet ds) {
//        this.MutationSuccessRate = 0;
//        this.ChangeRate = 0;
//        this.CrossoverSuccessRate=0;
//        this.setLength(PopulationSize);
//        this.setIsSelfAdaptation(isSelfAdaptation);
//        this.individuals = new IScheme[PopulationSize];
//        for(int i=0;i<PopulationSize;i++){
//            switch(iApproach){
//                case 0: //One Alphabet
//                    this.individuals[i] = new Individuals.PEVOMO.MOScheme(ds, iFitnessFunctionConf);
//                    this.individuals[i].adjust(ds);
//                    break;
//                case 1: //Several Alphabet
//                    this.individuals[i] = new Individuals.Proposal.MOScheme(ds, iFitnessFunctionConf);
//                    this.individuals[i].adjust(ds);
//                    break;   
//                
//            }
//            
//        }
//    }
    
    @Override 
    public void Generate(int iApproach, Data ds, double[][] limits, int iFitnessFunctionConf){
        for(int i=0;i<this.length;i++){
            switch(iApproach){
                case 0: //One Alphabet
                    this.individuals[i] = new Individuals.PEVOMO.MOScheme(ds, limits, iFitnessFunctionConf);
                    this.individuals[i].adjust(ds);
                    break;
                case 1: //Several Alphabet
                    this.individuals[i] = new Individuals.Proposal.MOScheme(ds, limits, iFitnessFunctionConf);
                    this.individuals[i].adjust(ds);
                    break;   
                
            }
        }
        System.out.println(this.getName() + " - " + this.individuals[0].getName());
    }
    
    @Override
    public void Evaluate(Data ds, double[] weights){
        for(int i=0;i<this.length;i++){
            this.individuals[i].evaluate(ds, weights);
        }
    }
    
    
//    ******************Fata ver si para multiobjetivo se necesita
    @Override
    public IScheme getBest(){ 
        double min = Double.POSITIVE_INFINITY;
        int ind = -1;
//        System.out.println("length:"+this.length);
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
    public MOPopulation Mutate(double MutationRate,  Data ds, double[][] limits){
        MOPopulation offsprings = new MOPopulation(this.length, this.isSelfAdaptation);
        for(int i=0;i<this.length;i++){
            offsprings.individuals[i] = this.individuals[i].Mutate(MutationRate, this.isSelfAdaptation, ds, limits).clone();
            offsprings.individuals[i].adjust(ds);
        }
        return offsprings;
    }
    
//    ****Falta ver si en Multiobjetivo hace falta
    
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
    
    public IPopulation Join(IPopulation p){
        MOPopulation joined = new MOPopulation(this.length + p.getLength(), this.isSelfAdaptation);
        for(int i=0;i<this.individuals.length;i++) joined.individuals[i] = this.individuals[i];
        
        for(int j=0;j<p.getIndividuals().length;j++) joined.individuals[this.length+j] = p.getIndividuals()[j];
        
        return joined;
    }

    
    @Override
    public void Replace(IPopulation pop){
        pop.sort();
        for(int i=0; i<this.length; i++){
            this.individuals[i] = pop.getIndividuals()[i].clone();
//            this.individuals[i].setCrowdingDistance(Double.NaN);
//            this.individuals[i].setRank(0);
            
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
    public MOPopulation clone() {
        try {
            super.clone();
            MOPopulation clon = new MOPopulation();
            clon.setLength(this.length);
            clon.setIsSelfAdaptation(this.isSelfAdaptation);
            clon.setIndividuals(this.individuals.clone());
            clon.ChangeRate = this.ChangeRate;
            clon.CrossoverSuccessRate = this.CrossoverSuccessRate;
            clon.MutationSuccessRate = this.MutationSuccessRate;
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MOPopulation.class.getName()).log(Level.SEVERE, null, ex);
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
        try{
            Arrays.sort(this.getIndividuals());
        } catch(NullPointerException ex) {
            Logger.getLogger(MOPopulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public IPopulation Extract(IPopulation pop, int newSize){
        MOPopulation population = new MOPopulation(newSize, pop.isIsSelfAdaptation());
        
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
            double sum=0;
            try{
                for(int j=0;j<this.individuals[i].getNoFunctions();j++){
                    sum += this.individuals[i].getEvaluatedValues()[j];
                }
            } catch(NullPointerException ex) {
                Logger.getLogger(MOPopulation.class.getName()).log(Level.SEVERE, null, ex);
            }
           fitness.add(sum);
        }
        return fitness;
    }
//    
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
    
    @Override
    public IPopulation CreateOffsprings(Generals general_params, Data ds){
        int k = this.length-1;
        MOPopulation offsprings = new MOPopulation(this.length, this.isSelfAdaptation);
        
        int o=0, count_cr=0;
        
        for (int i = 0; i < this.length; i++){
            if (i < k){
                Random rnd = new Random();
                double num = rnd.nextDouble();
                if (num <= general_params.getCrossOverRate()){
                    IScheme[] offspring = new IScheme[2];
//                    switch(general_params.getiApproach()){
//                        case 1:
                            offspring = crossovers.OnePoint(this.individuals[i], this.individuals[k],general_params.getiApproach());
                            offspring[0].adjust(ds);
                            offspring[1].adjust(ds);
//                            offspring[0].evaluate(local_params.getDs(), general_params.getiFitnessFunctionConf(), general_params.getWeights());
//                            offspring[1].evaluate(local_params.getDs(), general_params.getiFitnessFunctionConf(), general_params.getWeights());
//                            offspring[0].evaluate(local_params.getDs(), general_params.getWeights());
//                            offspring[1].evaluate(local_params.getDs(), general_params.getWeights());
//                            if (this.individuals[i].compareTo(offspring[0]) == 1){ //El hijo mejoro al padre
//                                count_cr++;
//                            }
//                            if (this.individuals[k].compareTo(offspring[1]) == 1){ //El hijo mejoro al padre
//                                count_cr++;
//                            }
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
        return MOPopulation.class.getName();
    }
    
    @Override
    public float[][] getArrayFitnessValuesWithRank(){
        float[][] array = new float[this.length][this.individuals[0].getFitnessFunction().getNoFunctions()+1];
        for(int i=0;i<this.length;i++){
            array[i][0] = this.individuals[i].getRank();
            for(int j=0;j<this.individuals[i].getFitnessFunction().getNoFunctions();j++){
                array[i][j+1] = (float) this.individuals[i].getEvaluatedValues()[j];
            }
        }
        return array;
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
    public IScheme getBestByErrorRate(){
        double min = Double.POSITIVE_INFINITY;
        int ind = -1;
//        System.out.println("length:"+this.length);
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
    public void Classify(DataSet ds, boolean UsingTest){
        for(int i=0;i<this.length;i++){
            this.individuals[i].Classify(ds, UsingTest, "train");
//            if (UsingTest){
////                try {
////                    DataSet ds_test = new DataSet(ds.getIndex(), "_TEST");
//                    this.individuals[i].Classify(ds);
////                } catch (MyException ex) {
////                    Logger.getLogger(MOPopulation.class.getName()).log(Level.SEVERE, null, ex);
////                }
//            }
//            else {
//                this.individuals[i].Classify(ds.getTrain());
//            }
        }
    }
    
    public static void ExportInitialPopulation(int PS, int iApproach, int iFitnessFunctionConf, int iDS){
        try {
            DataSet ds = new DataSet(iDS,false);
            MOPopulation Pop = new MOPopulation(PS,  false);
            Pop.Generate(iApproach, ds.getTrain(), ds.getLimits(), iFitnessFunctionConf);
            Pop.Evaluate(ds.getTrain(), new double[3]);
            List<ParetoFront> Fronts = NSGA2.FastNonDominatedSort(Pop);
            
            MatlabExporter exporter = new MatlabExporter();
            Fronts.get(0).Export(exporter);
            exporter.add("AccumulatedFrontFitness", Fronts.get(0).toFloatArray());
            
            String directory = Pop.getName()+"/"+ds.getName();
            String FileName =  ds.getName()+"_"+Pop.getName()+"_Frente_FF"+iFitnessFunctionConf;
            
            File FileDir = new File(directory);
            if(!FileDir.exists()) FileDir.mkdirs();
            
            File FileTabla = new File(directory+"/"+FileName+".mat");
            
            exporter.write(FileTabla);
            
        } catch (MyException ex) {
            Logger.getLogger(MOPopulation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MOPopulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {

//        int PS = Integer.valueOf(args[0]);
//        int iApproach = Integer.valueOf(args[1]); // 0 - PEVOMO, 1 - Proposal
//        int iFitnessFunctionConf = Integer.valueOf(args[2]); //1 Rechy (3 funciones), 2 TDFunction, 3 Rechy(Entropia y Complejidad), 4 rfEntropyCompression, 5 rfComplexityCompression
//        int iDS = Integer.valueOf(args[3]); 
        int PS = 5000;
        int iApproach = 1; // 0 - PEVOMO, 1 - Proposal
        int iFitnessFunctionConf = 7; //1 Rechy (3 funciones), 2 Tree Classification y Complejidad, 3 Rechy(Entropia y Complejidad), 4 rechy(Entropy y Compression), 5 rechy(Complexity y Compression) 6. Tree Classification y compresion. 7-EPEntropy y aComplexity
        int iDS = 2;
        MOPopulation.ExportInitialPopulation(PS, iApproach, iFitnessFunctionConf, iDS);
        
        try {
//            int MaxIter = 50;    
            DataSet ds = new DataSet(iDS, false);
//            MOPopulation BestClassifications = new MOPopulation(PS, true);
//            for(int i=0;i<MaxIter;i++){
                MOPopulation pop = new MOPopulation(PS, true);
                pop.Generate(iApproach, ds.getTrain(), ds.getLimits(), iFitnessFunctionConf);
                pop.Evaluate(ds.getTrain(), new double[3]);
                List<ParetoFront> Fronts = NSGA2.FastNonDominatedSort(pop);
                pop.Classify(ds,true);
//                BestClassifications.addIndividual(pop.getBestByErrorRate());
//            }
            
            
            
            System.out.println("Best Classification scheme:");
            System.out.println(pop.getBestByErrorRate().toString());
            System.out.println("size:"+pop.getBestByErrorRate().getNumberWordCuts());
            StringBuilder sb = new StringBuilder();
            for(Integer i:pop.getBestByErrorRate().getTotalCuts()){
                sb.append(i).append(" ");
            }
            System.out.println("Noalph::"+sb.toString());
            double[] r = {0,0,0};
            System.out.println(Fronts.get(0).getKnee(r).toString());
//            
//            DataSets.DiscretizedDataSet ds_dis = pop.getBestByErrorRate().DiscretizeByPAA(ds);
////            
//            FitnessFunctions.ConfusionMatrix MC = new FitnessFunctions.ConfusionMatrix(ds, ds_dis);
//            
//            System.out.println("Rank: "+pop.getBestByErrorRate().getRank()+" --- ER: "+pop.getBestByErrorRate().getErrorRate()+" --- Fitness Functions: [0]:"+FitnessFunctions.Functions.EPAccuracy(MC) + " [1]:" + FitnessFunctions.Functions.EPComplexity(MC, ds) + " [2]:" + FitnessFunctions.Functions.EPCompression(ds_dis.getDimensions()[1]-1,ds.getDimensions()[1]-1));
//            System.out.println("MC.dimensions[0]:" + MC.getDimensions()[0]);
//            
//            System.out.println("DS Dimensions: ["+ds.getDimensions()[0]+","+ds.getDimensions()[1]+"], MC Dimensions: ["+MC.getDimensions()[0]+","+MC.getDimensions()[1]+"], ds_dis dimensions: ["+ds_dis.getDimensions()[0]+","+ds_dis.getDimensions()[1]+"]");
//            System.out.println("PE: " + FitnessFunctions.Functions.PermutationEntropy(MC, ds));
//            
//            System.out.println("PEntropy: " + FitnessFunctions.Functions.calculatePermutationEntropy(ds.getValuesFrom(1, 1, ds.getDimensions()[1]-1), ds_dis.getDimensions()[1]-1));
//            System.out.println("Shannon Entropy:"+FitnessFunctions.Functions.calculateShannonEntropy(ds_dis.toStringList()));
//            
//            System.out.println("First Front:");
//            System.out.println(Fronts.get(0).toString());
            
        } catch (MyException ex) {
            Logger.getLogger(MOPopulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
