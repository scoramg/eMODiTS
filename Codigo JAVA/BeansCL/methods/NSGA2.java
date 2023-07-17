/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL.methods;

//import Algorithms.selection.TournamentSelection;
//import DataSets.Data;
//import Interfaces.*;
//import ParetoFront.ParetoFront;
import BeansCL.HistogramCollection;
import BeansCL.HistogramScheme;
import BeansCL.ParetoFront;
import BeansCL.Population;
import BeansCL.parameters.Generals;
import BeansCL.parameters.Locals;
import BeansCL.selection.TournamentSelection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import parameters.Generals;
//import parameters.Locals;

/**
 *
 * @author amarquezgr
 */
public class NSGA2 {
    
    List<ParetoFront> fronts;

    public NSGA2() {
       fronts = new ArrayList<>();
    }

    public void Execute(Generals general_params, Locals local_params) {
        System.out.println("NSGA II");
        fronts = new ArrayList<>();
        List<ParetoFront> first_fronts = new ArrayList<>();
        List<float[][]> populations = new ArrayList<>();
        double[] stds = new double[general_params.getnGenerations()]; //****
        local_params.setTimeAlgorithm(System.currentTimeMillis());
        
        HistogramCollection ds = new HistogramCollection();
        
        switch(general_params.getTypeDataSet()){
            case 0:
                ds = local_params.getDs().getHistComplete();
                break;
            case 1:
                ds = local_params.getDs().getHistTrain();
                break;
            case 2:
                ds = local_params.getDs().getHistTest();
                break;
        }
        
        general_params.population.Generate(ds, general_params.getiFitnessFunctionConf());
        general_params.population.Evaluate(ds, general_params.getWeights());
        
        for(int g=0; g<general_params.getnGenerations();g++){
            System.out.println("Running execution: "+(local_params.getExecution()+1)+", Generation:"+(g+1));
            fronts = FastNonDominatedSort(general_params.population);
            CalcCrowdingDistance();
            
            first_fronts.add(fronts.get(0));
            populations.add(general_params.population.getArrayFitnessValuesWithRank());
            
            TournamentSelection parents = new TournamentSelection(general_params.population, general_params.getOpponents());
            Population offsprings = parents.selected.CreateOffsprings(general_params);
            offsprings = offsprings.Mutate(general_params.getMutationRate(), local_params.getDs().getHistTrain().getData().get(0).getDimensions());
            offsprings.Evaluate(ds, general_params.getWeights());
            
            Population joined = general_params.population.Join(offsprings);
            
//            System.out.println("Joined: "+joined.toString());
            
            fronts = FastNonDominatedSort(joined);
            CalcCrowdingDistance();
            
//            System.out.println("Joined Ranked: "+joined.toString());
            
            Population newpop = NewPopulationFromFronts(general_params.population);
            
//            System.out.println("New Population: "+newpop.toString());
            stds[g] = mimath.MiMath.getDesviacionStandar(newpop.getAllFitnessValue());
//            if(g>0 && (stds[g]-stds[g-1]==0)){
//                System.out.println("Me detengo aquí");
//            }
            
            general_params.population.Replace(newpop);
            
//            System.out.println("Population Replaced: "+newpop.toString());
//            int r=0;
//            System.out.println(general_params.getPopulation().getBestByErrorRate());
            
        }
        fronts = FastNonDominatedSort(general_params.population);
        CalcCrowdingDistance();
//        IScheme best = fronts.get(0).getBest(local_params.getDs());
//        local_params.ParetoFronts = Fronts.get(0).toFloatArray();
        first_fronts.add(fronts.get(0));
        for(int i=0;i<fronts.get(0).getFront().size();i++)
            general_params.accumulatedFront.addIndividual(fronts.get(0).getFront().get(i));
        local_params.ParetoFronts = ParetoList2FloatArray(first_fronts);
        local_params.FinalParetoFront = fronts.get(0).toFloatArray();
        local_params.populations = PopulationList2FloatArray(populations);
        
//        general_params.misclassification_rates[local_params.getExecution()][0] = local_params.getExecution()+1;
//        general_params.misclassification_rates[local_params.getExecution()][1] = (float) best.getErrorRate();
//        
//        best.ExportGraph(local_params);
//        best.Export(general_params, local_params);
        
        //Exportar el frente de pareto
    }
    
    public static List<ParetoFront> FastNonDominatedSort(Population mopopulation){
        List<ParetoFront> Fronts = new ArrayList<>();
        ParetoFront F1 = new ParetoFront();
        int ranked=0;
        for(int i=0;i<mopopulation.getLength();i++){
            mopopulation.getIndividuals()[i].setDominatedCount(0);
            mopopulation.getIndividuals()[i].setDominationSet(new ArrayList<>());
            mopopulation.getIndividuals()[i].setRank(0);
            mopopulation.getIndividuals()[i].setCrowdingDistance(0);
        }
        for(int p=0;p<(mopopulation.getLength()-1);p++){
            for(int q=p+1;q<mopopulation.getLength();q++){
                if(p!=q){
                    //Si p dominates q
                    int p_dom_q=ParetoFront.dominates(mopopulation.getIndividuals()[p].getEvaluatedValues(), mopopulation.getIndividuals()[q].getEvaluatedValues(), mopopulation.getIndividuals()[p].getNoFunctions());
                    if(p_dom_q==-1){
                        mopopulation.getIndividuals()[p].getDominationSet().add(mopopulation.getIndividuals()[q]);
                        mopopulation.getIndividuals()[q].IncreaseDominatedCount();
                    } else if(p_dom_q==1){
                        //Si q dominates p
//                        if(ParetoFront.dominates(mopopulation.getIndividuals()[q].getFitnessFunction().getFitness_values(), mopopulation.getIndividuals()[p].getFitnessFunction().getFitness_values(), MORechyFunction.NO_FUNCTIONS)){
                            mopopulation.getIndividuals()[q].getDominationSet().add(mopopulation.getIndividuals()[p]);
                            mopopulation.getIndividuals()[p].IncreaseDominatedCount();
                        }
//                    }
                }
            }
        }
        
        for(int p=0;p<mopopulation.getLength();p++){
            if (mopopulation.getIndividuals()[p].getDominatedCount()==0){
                mopopulation.getIndividuals()[p].setRank(0);
                F1.addData(mopopulation.getIndividuals()[p]);
                ranked++;
            }
        }
        Fronts.add(F1);
        
        int i=0;
        
        while(Fronts.get(i).size()!= 0){
            ParetoFront Q = new ParetoFront();
            for(int j=0; j<Fronts.get(i).size();j++){
                for(int k=0;k<Fronts.get(i).getFront().get(j).getDominationSet().size();k++){
                    Fronts.get(i).getFront().get(j).getDominationSet().get(k).DecreaseDominatedCount();
                    if(Fronts.get(i).getFront().get(j).getDominationSet().get(k).getDominatedCount()==0){
                        Fronts.get(i).getFront().get(j).getDominationSet().get(k).setRank(i+1);
                        Q.addData(Fronts.get(i).getFront().get(j).getDominationSet().get(k));
                        ranked++;
                    }
                }
            }
            if (!Q.isEmpty()){
                Fronts.add(Q);
                i++;
            }
            if(ranked == mopopulation.getLength()){
                break;
            }
        }
        
        return Fronts;
    }
    
    
    public void CalcCrowdingDistance(){
        for(int f=0; f<fronts.size();f++){
            int size = fronts.get(f).size();

            if (size > 0){
              
                if (size == 1) {
                  fronts.get(f).getFront().get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);

                } else {

                    if (size == 2) {
                      fronts.get(f).getFront().get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
                      fronts.get(f).getFront().get(1).setCrowdingDistance(Double.POSITIVE_INFINITY);

                    } else {      
                        
                        for(int m=0;m<fronts.get(f).getFront().get(0).getFitnessFunction().getNoFunctions();m++){
                            Collections.sort(fronts.get(f).getFront(), new BeansCL.comparators.ComparatorByFitnessFunctions(m));
                            fronts.get(f).getFront().get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
                            fronts.get(f).getFront().get(fronts.get(f).getFront().size()-1).setCrowdingDistance(Double.POSITIVE_INFINITY);
                            double fmax = fronts.get(f).getFront().get(fronts.get(f).getFront().size()-1).getEvaluatedValues()[m];
                            double fmin = fronts.get(f).getFront().get(0).getEvaluatedValues()[m];
                            double FmaxFminDiff = fmax-fmin;
                            if(FmaxFminDiff==0) FmaxFminDiff = 1;
                            for(int i=1;i<fronts.get(f).getFront().size()-1;i++){
                                double dis = fronts.get(f).getFront().get(i).getCrowdingDistance() + ((fronts.get(f).getFront().get(i+1).getEvaluatedValues()[m] - fronts.get(f).getFront().get(i-1).getEvaluatedValues()[m])/FmaxFminDiff);
                                fronts.get(f).getFront().get(i).setCrowdingDistance(dis);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public Population NewPopulationFromFronts(Population pop){
        Population newpopulation = pop.clone();
        newpopulation.reset();
        newpopulation.setLength(pop.getLength());
        
        int t = 0;
        
        for(int f=0; f<fronts.size();f++){
            if((t+fronts.get(f).size()) < pop.getLength()){
                for(HistogramScheme sch: fronts.get(f).getFront()){
                    newpopulation.getIndividuals()[t] = sch.clone();
                    t++;
                }
            } else {
                int j=0;
                List<HistogramScheme> last = fronts.get(f).getFront();
                Collections.sort(last, new BeansCL.comparators.ComparatorByRankingAndCrowdingDistance());
                while(t<pop.getLength()){
                    newpopulation.getIndividuals()[t] = last.get(j).clone();
                    t++;
                    j++;
                }
            }
        }
        
        return newpopulation;
        
    }
    
    public float[][] ParetoList2FloatArray(List<ParetoFront> list){
        int size = 0;
        
        for(ParetoFront pf:list){
            float[][] aux = pf.toFloatArray();
            size+=aux.length;
        }
        
        float[][] array = new float[size][list.get(0).getFront().get(0).getFitnessFunction().getNoFunctions()+1];
        int i=0, g=0;
//        for(int p=0;p<list.size();p++){
        for(ParetoFront pf:list){
            float[][] aux = pf.toFloatArray();
            for(int f=0;f<aux.length;f++){
                array[i][0] = g;
                for(int c=0;c<aux[f].length;c++){
                    array[i][c+1] = aux[f][c];
                }
                i++;
            }
            g++;
        }
        return array;
    }
    
    public float[][] PopulationList2FloatArray(List<float[][]> list){
        float[][] array = new float[list.size()*list.get(0).length][list.get(0)[0].length+1];
        int j=0;
        for(int i=0;i<list.size();i++){
            float[][] pop = list.get(i);
            for(int k=0;k<pop.length;k++){
                array[j][0] = i;
                for(int c=0;c<pop[k].length;c++){
                    array[j][c+1] = pop[k][c];
                }
                j++;
            }
            
        }
        return array;
    }
    
    public String ParetoListToString(List<ParetoFront> list){
        StringBuilder sb = new StringBuilder();
        
        for(int i=0;i<list.size();i++){
            sb.append("Front").append((i+1)).append(":").append(list.get(i).toString()).append("\n");
        }
        return sb.toString();
    }
}
