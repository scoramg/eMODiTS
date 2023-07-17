/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

import Graphs.Marker.MarkerShape;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amarquezgr
 */
public class LegendsCollection {
    private static final int INTERLINE_SPACE = 15, MARGIN = 5; 
    public enum Orientation{TOP, BOTTOM, LEFT, RIGHT};
    public enum Align{HORIZONTAL, VERTICAL}
    private final List<Legend> legends = new ArrayList<>();
    private Rectangle2D legendbox;

    public LegendsCollection() {
    }

    
    public void addLegend(Legend legend){
        legends.add(legend);
    }
    
    public int getLegendBoxHeight(Graphics2D g2){
        return (g2.getFontMetrics().getHeight() * legends.size()) + (MARGIN);
    }
    
    public int getLegendBoxWidth(Graphics2D g2){
        int maxwidth = 0;
//        FontMetrics fm = g2.getFontMetrics();
        for(Legend leg:legends){
//            if(fm.stringWidth(leg.getLegend()) > maxwidth){
//                maxwidth = fm.stringWidth(leg.getLegend());
//            }
            if(leg.getWidth(g2) > maxwidth){
                maxwidth = leg.getWidth(g2);
            }
        }
        
        return maxwidth + (2*MARGIN);
    }
    
    public void draw(Graphics2D g2, PlotWindow window, Orientation orientation){
        int x,y;
        switch(orientation){
            case BOTTOM:
                x =  (window.getWidth()/2) - (getLegendBoxWidth(g2)/2);
                y = window.getHeight() - getLegendBoxHeight(g2)-PlotWindow.PADDING;
                legendbox = new Rectangle(x, y, getLegendBoxWidth(g2), getLegendBoxHeight(g2));
                g2.draw(legendbox);
                window.addBottomMargin(getLegendBoxHeight(g2));
                window.addBottomMargin(PlotWindow.PADDING);
                break;
            case LEFT:
                x = window.getRightMargin();
                y = (window.getHeight()/2) - (getLegendBoxHeight(g2)/2);
                legendbox = new Rectangle(x, y, getLegendBoxWidth(g2), getLegendBoxHeight(g2));
                g2.draw(legendbox);
                window.addLeftMargin(getLegendBoxWidth(g2));
                window.addLeftMargin(PlotWindow.PADDING);
                break;
            case TOP:
                x =  (window.getWidth()/2) - (getLegendBoxWidth(g2)/2);
                y = window.getTopMargin() + PlotWindow.PADDING;
                legendbox = new Rectangle(x, y, getLegendBoxWidth(g2), getLegendBoxHeight(g2));
                g2.draw(legendbox);
                window.addTopMargin(getLegendBoxHeight(g2)/2);
//                window.addTopMargin(PlotWindow.PADDING);
                break;
            default:
            case RIGHT:
                x = window.getWidth()-window.getRightMargin()-getLegendBoxWidth(g2);
                y = (window.getHeight()/2) - (getLegendBoxHeight(g2)/2);
                legendbox = new Rectangle(x, y, getLegendBoxWidth(g2), getLegendBoxHeight(g2));
                g2.draw(legendbox);
                window.addRightMargin(getLegendBoxWidth(g2));
                window.addRightMargin(PlotWindow.PADDING);
                break;
        }
        int ylegend = y;
        for(Legend l: legends){
            l.draw(g2, MARGIN+x, ylegend);
            ylegend+=INTERLINE_SPACE;
        }
    }
}
