/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amarquezgr
 */
public class SeriesCollection {
    private List<Serie> series;
    Range xLimits;
    Range yLimits;

    public SeriesCollection() {
        this.series = new ArrayList<>();
        this.xLimits = new Range(0, 1);
        this.yLimits = new Range(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    public void addSerie(Serie serie){
        this.series.add(serie);
        if (serie.size() > xLimits.getMax()) xLimits.setMax(serie.size());
        if (mimath.MiMath.max(serie.getData()) > yLimits.getMax()) yLimits.setMax(mimath.MiMath.max(serie.getData()));
        if (mimath.MiMath.min(serie.getData()) < yLimits.getMin()) yLimits.setMin(mimath.MiMath.min(serie.getData()));
    }

    public double[] getSerie(int idx){
        return this.series.get(idx).getData();
    }

    public int size(){
        return this.series.size();
    }

    public Range getxLimits() {
        return xLimits;
    }

    public Range getyLimits() {
        return yLimits;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<this.series.size();i++){
            sb.append("[ ");
            for(int j=0; j<this.series.get(i).size();j++){
                sb.append(this.series.get(i).getData()[j]).append(" ");
            }
            sb.append(" ]").append("\n");
        }
        sb.append("X Limits: ").append(this.xLimits.toString()).append("\n");
        sb.append("Y Limits: ").append(this.yLimits.toString()).append("\n");
        return sb.toString();
    }
    
    public void draw(Graphics2D g2, PlotWindow window, boolean withMarks, Marker.MarkerShape markershape){
        for(Serie s: this.series){
            s.draw(g2, window, withMarks, markershape); 
        }
    }
}
