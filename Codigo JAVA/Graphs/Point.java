/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class Point implements Cloneable{
    private double x;
    private double y;
    
    private double xi;
    private double yi;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getXi() {
        return xi;
    }

    public double getYi() {
        return yi;
    }

    public void setXi(double xi) {
        this.xi = xi;
    }

    public void setYi(double yi) {
        this.yi = yi;
    }

    public Point() {
    }
    
    public Point(double xi, double yi, PlotWindow window) {
        this.xi = xi;
        this.yi = yi;
        getCoordinates(window);
    }
    
    public final void getCoordinates(PlotWindow window){
        int x0 = window.getMargins()[1]; // x0=0
        int y0 = window.getHeight() - window.getMargins()[2]; // y0=0
        
        this.x = x0 + (window.getArea().axis.getxTicksDist() * (this.xi+Math.abs(window.getData().getxLimits().getMin())));
        this.y = y0 - (window.getArea().axis.getyTicksDist() * (this.yi+Math.abs(window.getData().getyLimits().getMin())));
        
    }
    
    @Override
    public Point clone() {
        try {
            super.clone();
            Point clon = new Point();
            clon.setXi(this.xi);
            clon.setYi(this.yi);
            clon.x = this.x;
            clon.y = this.y;
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Point.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
