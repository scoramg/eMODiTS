/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms.selection;

import Interfaces.*;

/**
 *
 * @author amarquezgr
 */
public class StochasticRanking implements ISelector {
    public IPopulation selected;
    public double pf;

    public StochasticRanking(double pf) {
        this.pf = pf;
    }
    
    public StochasticRanking(IPopulation selected, double pf) {
        this.pf = pf;
        Execute(selected);
    }

    public int compare(IScheme o1, IScheme o2) {
        double u = mimath.MiMath.randInterval(0, 1);
        
        if ((u < this.pf)) {
            if (o1.getFitnessValue() > o2.getFitnessValue()) {
                return 1;
            } else if (o1.getFitnessValue() < o2.getFitnessValue()) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public void Execute(IPopulation population) {
        this.selected = population.clone();
        
        int popsize = selected.getLength();

        for (int i = 1; i < popsize; i++) {
            for (int j = 0; j < popsize - i; j++) {
                IScheme x1 = selected.getIndividuals()[j];
                IScheme x2 = selected.getIndividuals()[j + 1];
                if (this.compare(x1,x2) == 1) {
                    IScheme aux = x1.clone();
                    this.selected.setAt(x2,j);
                    this.selected.setAt(aux,j+1);
                }
            }
        }
    }
    
    
}
