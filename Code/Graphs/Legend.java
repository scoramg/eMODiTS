/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author amarquezgr
 */
public class Legend {
    
    public static int COLOR_WIDTH = 20, PADDING_LEGEND=5;
    private String legend;
    private Color color;
    public static Font font = new Font("Arial", 0, 12);
    
    public String getLegend() {
        return legend;
    }

    public Color getColor() {
        return color;
    }
    
    public Legend(String legend, Color color) {
        this.legend = legend;
        this.color = color;
    }
    
    public int getWidth(Graphics2D g2){
        FontMetrics fm = g2.getFontMetrics();
        return PADDING_LEGEND+COLOR_WIDTH+fm.stringWidth(this.legend);
    }
    
    public void draw(Graphics2D g2, int x, int y){
//        Font prev = g2.getFont();
//        g2.setFont(font);
        Rectangle2D colorarea = new Rectangle(x, y+g2.getFontMetrics().getHeight()/2, COLOR_WIDTH, g2.getFontMetrics().getHeight()/2);
        g2.setColor(this.color);
        g2.fill(colorarea);
        g2.setColor(Color.BLACK);
//        System.out.println("y:"+y+", g2.getFontMetrics().getHeight():"+g2.getFontMetrics().getHeight());
        g2.drawString(legend, x+COLOR_WIDTH+PADDING_LEGEND, y+g2.getFontMetrics().getHeight());
//        g2.setFont(prev);
    }
    
}
