/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms.operators;

//import Individuals.Proposal.WordCut;
import BeansCL.HistogramScheme;
import DataTypes.ArraySortedUniqueList;
import Interfaces.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author amarquezgr
 */
public class crossovers {
    
    public static HistogramScheme[] OnePoint(HistogramScheme parent1, HistogramScheme parent2){
        HistogramScheme[] offsprings = new HistogramScheme[2];
        
        Random rdn = new Random();
//        int cut1 = mimath.MiMath.randomInt(1, parent1.getNumberWordCuts());
//        int cut2 = mimath.MiMath.randomInt(1, parent1.getNumberWordCuts());
        int cut1 = rdn.nextInt(parent1.getNumberWordCuts());
        int cut2 = rdn.nextInt(parent2.getNumberWordCuts());

        offsprings[0] = parent1.clone();
        offsprings[1] = parent2.clone();

        offsprings[0].empty();
        offsprings[1].empty();

        for (int i=0;i<cut1;i++){
            offsprings[0].add(parent1.getElementAt(i));
        }

        for (int i=0;i<cut2;i++){
            offsprings[1].add(parent2.getElementAt(i));
        }

        for (int i=cut1;i<parent1.getNumberWordCuts();i++){
            offsprings[1].add(parent1.getElementAt(i));
        }

        for (int i=cut2;i<parent2.getNumberWordCuts();i++){
            offsprings[0].add(parent2.getElementAt(i));
        }
        offsprings[0].sort();
        offsprings[1].sort();
        
        if ((offsprings[0].getNumberWordCuts() >= TimeSeriesDiscretize.TimeSeriesDiscretize_source.MIN_NUMBER_OF_WORD_CUTS) && (offsprings[1].getNumberWordCuts() >= TimeSeriesDiscretize.TimeSeriesDiscretize_source.MIN_NUMBER_OF_WORD_CUTS)){
            return offsprings;
        } else {
            return OnePoint(parent1, parent2);
        }
    }
    
    public static IScheme[] OnePoint(IScheme parent1, IScheme parent2, int iApproach){
        IScheme[] offsprings = new IScheme[2];
        switch(iApproach){
            case 0:
                offsprings = OneAlphabetCrossover(parent1,parent2);
                break;
            case 1:
                offsprings = SeveralAlphabetsCrossover(parent1,parent2);
                break;
        }
        return offsprings;
    }
    
    private static IScheme[] SeveralAlphabetsCrossover(IScheme parent1, IScheme parent2){
        IScheme[] offsprings = new IScheme[2];
        
        Random rdn = new Random();
//        int cut1 = mimath.MiMath.randomInt(1, parent1.getNumberWordCuts());
//        int cut2 = mimath.MiMath.randomInt(1, parent1.getNumberWordCuts());
        int cut1 = rdn.nextInt(parent1.getNumberWordCuts());
        int cut2 = rdn.nextInt(parent2.getNumberWordCuts());

        offsprings[0] = parent1.clone();
        offsprings[1] = parent2.clone();

        offsprings[0].empty();
        offsprings[1].empty();

        for (int i=0;i<cut1;i++){
            offsprings[0].add(parent1.getElementAt(i));
        }

        for (int i=0;i<cut2;i++){
            offsprings[1].add(parent2.getElementAt(i));
        }

        for (int i=cut1;i<parent1.getNumberWordCuts();i++){
            offsprings[1].add(parent1.getElementAt(i));
        }

        for (int i=cut2;i<parent2.getNumberWordCuts();i++){
            offsprings[0].add(parent2.getElementAt(i));
        }
        offsprings[0].sort();
        offsprings[1].sort();
        
        if ((offsprings[0].getNumberWordCuts() >= TimeSeriesDiscretize.TimeSeriesDiscretize_source.MIN_NUMBER_OF_WORD_CUTS) && (offsprings[1].getNumberWordCuts() >= TimeSeriesDiscretize.TimeSeriesDiscretize_source.MIN_NUMBER_OF_WORD_CUTS)){
            return offsprings;
        } else {
//            System.out.println("offsprings[0].getNumberWordCuts():"+offsprings[0].getNumberWordCuts()+", offsprings[1].getNumberWordCuts():"+offsprings[1].getNumberWordCuts());
            return SeveralAlphabetsCrossover(parent1, parent2);
        }
    }
    
    private static IScheme[] OneAlphabetCrossover(IScheme parent1, IScheme parent2) {
        IScheme[] offsprings = new IScheme[2];
        offsprings[0] = parent1.clone();
        offsprings[1] = parent2.clone();

        offsprings[0].empty();
        offsprings[1].empty();
        
        Random rdn = new Random();
        int wordcut1 = rdn.nextInt(parent1.getNumberWordCuts());
        int wordcut2 = rdn.nextInt(parent2.getNumberWordCuts());
        
//        System.out.println("wordcut1: "+wordcut1+" - wordcut2: "+wordcut2);
        
        for(int i=0;i<wordcut1;i++){
            offsprings[0].getWord().addWordCut(parent1.getWord().getElementAt(i));
        }
        
        for(int i=wordcut1;i<parent1.getNumberWordCuts();i++){
            offsprings[1].getWord().addWordCut(parent1.getWord().getElementAt(i));
        }
        
        for(int i=0;i<wordcut2;i++){
            offsprings[1].getWord().addWordCut(parent2.getWord().getElementAt(i));
        }
        
        for(int i=wordcut2;i<parent2.getNumberWordCuts();i++){
            offsprings[0].getWord().addWordCut(parent2.getWord().getElementAt(i));
        }
        
        int alphcut1 = rdn.nextInt(parent1.getNumberAlphabetCuts());
        int alphcut2 = rdn.nextInt(parent2.getNumberAlphabetCuts());
        
//        System.out.println("alphcut1: "+alphcut1+" - alphcut2: "+alphcut2);
        
        for(int i=0;i<alphcut1;i++){
            offsprings[0].getAlphabet().InsertCut(parent1.getAlphabet().getElementAt(i));
        }
        
        for(int i=alphcut1;i<parent1.getNumberAlphabetCuts();i++){
            offsprings[1].getAlphabet().InsertCut(parent1.getAlphabet().getElementAt(i));
        }
        
        for(int i=0;i<alphcut2;i++){
            offsprings[1].getAlphabet().InsertCut(parent2.getAlphabet().getElementAt(i));
        }
        
        for(int i=alphcut2;i<parent2.getNumberAlphabetCuts();i++){
            offsprings[0].getAlphabet().InsertCut(parent2.getAlphabet().getElementAt(i));
        }
        
        return offsprings;
    }
    
    
    public static Double[] AverageCrossover(Double[] parent1, Double[] parent2){
        int max = (parent1.length > parent2.length) ? parent1.length  : parent2.length;
        
        Double[] offspring = new Double[max];
        
        for(int i=0; i<max;i++){
            double v1 = (i > parent1.length) ? 0 : parent1[i];
            double v2 = (i > parent2.length) ? 0 : parent2[i];
            offspring[i] = (v1 + v2) / (double) 2;
        }
        return offspring;
    }
    
    public static List<Double> DiscreteCrossover(List<Double> parent1, List<Double> parent2){
        int max = (parent1.size() > parent2.size()) ? parent1.size()  : parent2.size();
        
        List<Double> offspring = new ArrayList<>();
        
        for(int i=0; i<max;i++){
            Random rdn = new Random();
            if (rdn.nextDouble() < 0.5){
                if (i<parent1.size()){
                    offspring.add(parent1.get(i));
                }
            } else {
                if (i<parent2.size()){
                    offspring.add(parent2.get(i));
                }
            }
        }
        return offspring;
    }
    
    public static SortedUniqueList<Integer> DiscreteCrossover(SortedUniqueList<Integer> parent1, SortedUniqueList<Integer> parent2){
        int max = (parent1.size() > parent2.size()) ? parent1.size()  : parent2.size();
        
        SortedUniqueList<Integer> offspring = new ArraySortedUniqueList<>();
        
        for(int i=0; i<max;i++){
            Random rdn = new Random();
            if (rdn.nextDouble() < 0.5){
                if (i<parent1.size()){
                    offspring.add(parent1.get(i));
                }
            } else {
                if (i<parent2.size()){
                    offspring.add(parent2.get(i));
                }
            }
        }
        return offspring;
    }
}
