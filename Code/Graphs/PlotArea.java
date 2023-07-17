/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author amarquezgr
 */
public class PlotArea {
    private static final int MAXCUT = 20;
    private static final int LENGTH_TICKS = 10;
    
    public AxisOptions axis;

    public PlotArea(PlotWindow window) {
        axis = new AxisOptions(window);
    }
    
    public void drawArea(Graphics2D g2, PlotWindow window){
//        CalculateAxisOptions(window);
//        getOptions(windowWidth, windowHeight, data.getxLimits().getMax(),data.getyLimits().getDiff());
//        axis = new AxisOptions(window);
//        System.out.println("axis.getXlength():"+axis.getXlength()+", axis.getYlength():"+axis.getYlength());
        axis.getAxisOptions(window);
        Rectangle2D area = new Rectangle(window.getLeftMargin(), window.getTopMargin(), axis.getXlength() , axis.getYlength());
//        g2.draw(area);
        g2.setColor(Color.WHITE);
        g2.fill(area);
        g2.setColor(Color.BLACK);
        g2.draw(area);
        
        //Draw x ticks lines
//        int no_xcuts = (int) (window.getData().getxLimits().getMax() + 1);
        double xgap = axis.getxTicksDist();
        int xjump = 1, xjumpj=0;
        if (xgap < 11){
            xjump = mimath.MiMath.toInt(Math.round(window.getData().getxLimits().getMax()*0.2));
        }
//        axis.setxLabelTicks(no_xcuts, window.getData().getxLimits());
        
        for (int i = 0; i < axis.getNoCutsX(); i++) {
            if (i == (xjump*xjumpj)){
                int x0 = (int) (window.getLeftMargin() + (xgap * i));
                int x1 = x0;
                int y0_b = window.getHeight() - window.getBottomMargin();
                int y1_b=y0_b-LENGTH_TICKS;
                int y0_s = window.getTopMargin();
                int y1_s=y0_s+LENGTH_TICKS;
                PlotWindow.drawString(g2, axis.getxLabelTicks()[i], x0, y0_b + AxisOptions.MARGIN_XTICKSLABEL, "CENTER", "TOP");
                g2.drawLine(x0, y0_b, x1, y1_b);
                g2.drawLine(x0, y0_s, x1, y1_s);
                xjumpj++;
            }
        }
        
        //Draw y ticks lines
//        int no_ycuts = (int) (window.getData().getyLimits().getDiff())+1;
        double ygap = axis.getyTicksDist();
        int yjump = 1, yjumpj=0;
        if (ygap < 11){
            yjump = MAXCUT;
        }
        
//        axis.setyLabelTicks(no_ycuts, window.getData().getyLimits());
        for (int i = 0; i <= axis.getNoCutsY(); i++) {
            if (i == (yjump*yjumpj)){
                int x0_left = window.getLeftMargin();
                int x1_left = x0_left+LENGTH_TICKS;
                int x0_right = window.getWidth()-window.getRightMargin();
                int x1_right = x0_right-LENGTH_TICKS;
                int y0 = (int) (window.getHeight() - window.getBottomMargin() - (ygap * i));
                if(y0 < window.getTopMargin()) y0 = window.getTopMargin(); //para que no muestre divisores fuera del rango
                int y1 = y0 ;
                PlotWindow.drawString(g2, axis.getyLabelTicks()[i], x0_left-AxisOptions.MARGIN_XTICKSLABEL, y0, "RIGHT", "CENTER");
                g2.drawLine(x0_left, y0, x1_left, y1);
                g2.drawLine(x0_right, y0, x1_right, y1);
                yjumpj++;
            }
        }
    }
    
}
