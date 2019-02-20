/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

import java.awt.Font;
import java.awt.FontMetrics;
import sun.font.FontDesignMetrics;

/**
 *
 * @author amarquezgr
 */
public class AxisOptions {
    public static int MARGIN_XTICKSLABEL = 5;
    public static int MARGIN_YTICKSLABEL = 10;
    private int xlength, ylength;
    private String[] xLabelTicks;
    private String[] yLabelTicks;
    private double xTicksDist;
    private double yTicksDist;
    private int NoCutsX, NoCutsY;
    private int xticklabelheight, yticklabelwidth;

    public int getXlength() {
        return xlength;
    }

    public int getYlength() {
        return ylength;
    }

    public String[] getxLabelTicks() {
        return xLabelTicks;
    }

    public String[] getyLabelTicks() {
        return yLabelTicks;
    }

    public double getxTicksDist() {
        return xTicksDist;
    }

    public double getyTicksDist() {
        return yTicksDist;
    }

    public int getNoCutsX() {
        return NoCutsX;
    }

    public int getNoCutsY() {
        return NoCutsY;
    }
    
    public int getXticklabelheight() {
        return xticklabelheight;
    }

    public int getYticklabelwidth() {
        return yticklabelwidth;
    }

    public void setNoCutsX(int NoCutsX) {
        this.NoCutsX = NoCutsX;
    }

    public void setNoCutsY(int NoCutsY) {
        this.NoCutsY = NoCutsY;
    }
    
    public void setXticklabelheight(int xticklabelheight) {
        this.xticklabelheight = xticklabelheight;
    }

    public void setYticklabelwidth(int yticklabelheight) {
        this.yticklabelwidth = yticklabelheight;
    }
    
    public AxisOptions(PlotWindow window) {
        getTicksOptions(window);
    }
    
    public final void getTicksOptions(PlotWindow window){
        this.NoCutsX = (int) (window.getData().getxLimits().getMax() + 1);
        this.setxLabelTicks(this.NoCutsX, window.getData().getxLimits());
        this.NoCutsY = (int) (window.getData().getyLimits().getDiff())+1;
        this.setyLabelTicks(this.NoCutsY, window.getData().getyLimits());
        this.xticklabelheight = window.getGraphics().getFontMetrics().getHeight();
        this.yticklabelwidth = window.getGraphics().getFontMetrics().stringWidth(getLargerString(yLabelTicks));
        
    }
    
    public void getAxisOptions(PlotWindow window){
        this.xlength = window.getWidth()-window.getLeftMargin()-window.getRightMargin();
        this.ylength = window.getHeight()-window.getBottomMargin()-window.getTopMargin();
        this.xTicksDist = (double) this.xlength / (window.getData().getxLimits().getMax() + 1);
        this.yTicksDist = (double) this.ylength / window.getData().getyLimits().getDiff();
    }
    
    
    public void setxLabelTicks(int cuts, Range range) {
        this.xLabelTicks = getLabelTicks(cuts, range);
    } 
    
    public void setyLabelTicks(int cuts, Range range) {
        this.yLabelTicks = getLabelTicks(cuts, range);

    }
    
    public String[] getLabelTicks(int cuts, Range range){
        String[] LabelTicks = new String[cuts+1];
        double sig = range.getMin();
        for(int i=0; i<=cuts; i++){
            LabelTicks[i] = String.valueOf(mimath.MiMath.toInt(sig));
            if((sig+1) > range.getMax())
                sig=range.getMax();
            else
                sig++;
        }
        return LabelTicks;
    }
    
    public static String getLargerString(String[] data){
        int max = 0;
        String str = "";
        for(String s:data){
            if (s.length()>max) str = s;
        }
        return str;
    }
}
