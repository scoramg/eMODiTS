/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms.selection;

import Interfaces.*;
import mimath.MiMath;

/**
 *
 * @author amarquezgr
 */
public class RouletteSelection implements ISelector {
    
    public IPopulation selected;

    public RouletteSelection() {
//        selected = new Population();
    }

    public RouletteSelection(IPopulation population) {
        Execute(population);
    }
    
    @Override
    public void Execute(IPopulation population){
        
        double SumaApt = 0, PromedioApt = 0;
        
        int length = population.getLength();
        if(population.getLength()%2 != 0){
            length = population.getLength()-1;
        }
        
        int[] iParents = new int[length];
        
        //Conversion de Minimizacion a Maximizacion
        IPopulation populationAux = population.clone();
        SumaApt = populationAux.getTotalFitness();
        
        PromedioApt = MiMath.Redondear(SumaApt / population.getLength(), 2);
        double[] VEs = new double[population.getLength()];
        //calculo del valor esperado
        for(int i=0; i<population.getLength(); i++){
            VEs[i] = MiMath.Redondear(populationAux.getIndividuals()[i].getFitnessValue()/PromedioApt,2);             
        }
       
        int j=0;
//        for(int i=0; i<population.getLength(); i++){
        for(int i=0; i<length; i++){
            double r = MiMath.Redondear(Math.random() * population.getLength(),2);
            
            double SumRuleta = 0;
            boolean SigueGirando = true;
            while(SigueGirando){
                SumRuleta += VEs[j];
                if(SumRuleta>=r){
                    iParents[i] = j;
                    SigueGirando = false;
                }
                j++;
                if (j == length) j=0;
            }
        }
//        selected = new Population(iParents.length, population.isIsSelfAdaptation());
        selected = population.clone();
        selected.reset();
        selected.setLength(iParents.length);
        
        for (int k=0; k < iParents.length;k ++){
//            System.out.println("iParents["+k+"]:"+iParents[k]);
//            System.out.println("population.getIndividuals()[iParents["+k+"]]:"+population.getIndividuals()[iParents[k]].toString());
            selected.setAt(population.getIndividuals()[iParents[k]], k);
        }
            
    }
    
}
