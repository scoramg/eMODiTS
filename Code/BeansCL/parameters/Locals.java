/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL.parameters;

//import parameters.*;
import BeansCL.HistogramDataSet;
import DataSets.DataSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class Locals implements Cloneable{
    private HistogramDataSet ds; 
    private int nGenerations;
    private int execution;
    private long timeAlgorithm;
    public float[][] ConvergenceData;
    public float[][] MeasuresData;
    public float[][] ParetoFronts;
    public float[][] FinalParetoFront;
    public float[][] populations;

    public HistogramDataSet getDs() {
        return ds;
    }

    public int getExecution() {
        return execution;
    }

    public long getTimeAlgorithm() {
        return timeAlgorithm;
    }

    public float[][] getConvergenceData() {
        return ConvergenceData;
    }

    public float[][] getMeasuresData() {
        return MeasuresData;
    }

    public int getnGenerations() {
        return nGenerations;
    }

    public void setDs(HistogramDataSet ds) {
        this.ds = ds;
    }

    public void setExecution(int execution) {
        this.execution = execution;
    }

    public void setTimeAlgorithm(long timeAlgorithm) {
        this.timeAlgorithm = timeAlgorithm;
    }

    public void setnGenerations(int nGenerations) {
        this.nGenerations = nGenerations;
    }

    public Locals() {
        
    }
    
    public Locals(HistogramDataSet ds, int execution, long timeAlgorithm, int nGenerations) {
        this.ds = ds;
        this.execution = execution;
        this.timeAlgorithm = timeAlgorithm;
        this.nGenerations = nGenerations;
        this.ConvergenceData = new float[nGenerations][1];
        this.MeasuresData = new float[nGenerations+1][9];
    }

    public Locals(HistogramDataSet ds, int execution, int nGenerations) {
        this.ds = ds;
        this.execution = execution;
        this.nGenerations = nGenerations;
        this.ConvergenceData = new float[nGenerations][1];
        this.MeasuresData = new float[nGenerations+1][9];
        this.ParetoFronts = new float[1][1];
    }
    
    
    @Override
    public Locals clone() {
        try {
            super.clone();
            Locals clon = new Locals();
            clon.setnGenerations(nGenerations);
            clon.setDs(this.ds.clone());
            clon.setExecution(this.execution);
            clon.setTimeAlgorithm(this.timeAlgorithm);
            clon.ConvergenceData = this.ConvergenceData.clone();
            clon.MeasuresData = this.MeasuresData.clone();
            clon.ParetoFronts = this.ParetoFronts.clone();
            clon.FinalParetoFront = this.FinalParetoFront.clone();
            clon.populations=this.populations.clone();
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Generals.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
}
