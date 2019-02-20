/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author amarquezgr
 */
public class Serie {
    private double[] data;
    private Marker marker;
    private Legend legend;

    public double[] getData() {
        return data;
    }

    public Legend getLegend() {
        return legend;
    }

    public void setData(double[] data) {
        this.data = data.clone();
    }

    public void setLegend(Legend legend) {
        this.legend = legend;
    }

    public int size(){
        return this.data.length;
    }
    
    public Serie(double[] data, Color color) {
        this.data = data;
        this.legend = new Legend("Serie", color);
        this.marker = new Marker();
    }

    public Serie(double[] data, Legend legend) {
        this.data = data;
        this.legend = legend;
        this.marker = new Marker();
    }

    public void draw(Graphics2D g2, PlotWindow window, boolean withMarks, Marker.MarkerShape markershape){
//        g2.drawLine(0, 0, 100, 100);
        if(withMarks){
            this.marker.setMarkerColor(legend.getColor());
            this.marker.setMarkerSize(10);
            this.marker.setShape(markershape);
        }
        g2.setColor(this.legend.getColor());
        Point p_prev = new Point((1), this.getData()[0], window);
        if(withMarks){
            marker.drawMarker(g2, p_prev.getX(), p_prev.getY());
        }
        
        for(int i=1;i<this.size();i++){
            Point p = new Point((i+1), this.getData()[i], window);
            if(withMarks){
                marker.drawMarker(g2, p.getX(), p.getY());
            }
            g2.drawLine(mimath.MiMath.toInt(p_prev.getX()), mimath.MiMath.toInt(p_prev.getY()), mimath.MiMath.toInt(p.getX()), mimath.MiMath.toInt(p.getY()));
            g2.setColor(this.legend.getColor());
            p_prev = p.clone();
        }
    }
    
    
}
