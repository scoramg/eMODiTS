/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Diversity.fitnessfunction;

import java.util.List;

/**
 *
 * @author amarquezgr
 */
public class FitnessDiversity {
    private double maxDiffFbestFavg, fitnessmeasure;
    private final int type;

    public double getMaxDiffFbestFavg() {
        return maxDiffFbestFavg;
    }

    public int getType() {
        return type;
    }

    public double getFitnessmeasure() {
        return fitnessmeasure;
    }
    
    public FitnessDiversity() {
        maxDiffFbestFavg = 0;
        fitnessmeasure=0;
        type = 0;
    }

    public FitnessDiversity(int type) {
        maxDiffFbestFavg = 0;
        fitnessmeasure=0;
        this.type = type;
        
    }
    
    public void Calculate(List<Double> fitness){
        double fbest = mimath.MiMath.getMejor(fitness);
        double fworst = mimath.MiMath.getPeor(fitness);
        double favg = mimath.MiMath.getMedia(fitness);
        double sigmaf = mimath.MiMath.getDesviacionStandar(fitness);
        
        this.maxDiffFbestFavg = Math.max(this.maxDiffFbestFavg, Math.abs(fbest-favg));
        
        switch(type){ //tomados de Differential evolution with fitness diversity self-adaptation
            case 0: // xi value
                this.fitnessmeasure = Math.min(Math.abs((fbest-favg)/fbest),1);
                break;
            case 1: // psi value
                this.fitnessmeasure = 1 - Math.abs((favg-fbest)/(fworst-fbest));
                break;    
            case 2: // nu value
                this.fitnessmeasure = Math.min(sigmaf/Math.abs(favg),1);
                break;    
            case 3: // chi value
                this.fitnessmeasure = Math.abs(fbest-favg)/this.maxDiffFbestFavg;
                break;    
        }
    }
}
