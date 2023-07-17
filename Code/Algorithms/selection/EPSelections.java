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
public class EPSelections implements ISelector {
    public IPopulation selected;
    public double percentage;

    public EPSelections() {
//        selected = new Population();
        percentage = 1;
    }

    public EPSelections(IPopulation selected, double percentage) {
        this.selected = selected;
        this.percentage = percentage;
        Execute(selected);
    }
    
    @Override
    public void Execute(IPopulation population) {
        int newSize = (int) Math.floor(population.getLength() * this.percentage);
        selected = population.Extract(population, newSize);
    }
    
}
