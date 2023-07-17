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
public class Marker{
    public enum MarkerShape { NONE, CIRCLE, SQUARE, DIAMOND };
    private Color markerColor;
    private int markerSize;
    private MarkerShape shape;

    public Marker() {
        markerColor = Color.BLACK;
        markerSize = 10;
    }

    public Marker(Color markerColor, MarkerShape shape, int markerSize) {
        this.markerColor = markerColor;
        this.markerSize = markerSize;
        this.shape = shape;
    }

    public Color getMarkerColor() {
        return markerColor;
    }

    public int getMarkerSize() {
        return markerSize;
    }

    public MarkerShape getShape() {
        return shape;
    }

    public void setMarkerColor(Color markerColor) {
        this.markerColor = markerColor;
    }

    public void setMarkerSize(int markerSize) {
        this.markerSize = markerSize;
    }

    public void setShape(MarkerShape shape) {
        this.shape = shape;
    }
    
    public void drawMarker(Graphics2D g, double x, double y) {
//        g.drawLine(0, 0, 100, 100);
        switch (this.shape) {
        case CIRCLE:
                g.setColor(this.markerColor);
                g.fillOval(mimath.MiMath.toInt(x - (this.markerSize/2)), mimath.MiMath.toInt(y - (this.markerSize/2)), this.markerSize, this.markerSize);
                g.setColor(this.markerColor);
                g.drawOval(mimath.MiMath.toInt(x - (this.markerSize/2)), mimath.MiMath.toInt(y - (this.markerSize/2)), this.markerSize, this.markerSize);
                break;
        case SQUARE:
                g.setColor(this.markerColor);
                g.fillRect(mimath.MiMath.toInt(x - (this.markerSize/2)), mimath.MiMath.toInt(y - (this.markerSize/2)), this.markerSize, this.markerSize);
                g.setColor(this.markerColor);
                g.drawRect(mimath.MiMath.toInt(x - (this.markerSize/2)), mimath.MiMath.toInt(y - (this.markerSize/2)), this.markerSize, this.markerSize);							
                break;
        case DIAMOND:
                int[] xpts = { mimath.MiMath.toInt(x), mimath.MiMath.toInt(x + (this.markerSize/2)), mimath.MiMath.toInt(x), mimath.MiMath.toInt(x - (this.markerSize/2)) };
                int[] ypts = { mimath.MiMath.toInt(y - (this.markerSize/2)), mimath.MiMath.toInt(y), mimath.MiMath.toInt(y + (this.markerSize/2)), mimath.MiMath.toInt(y) };
                g.setColor(this.markerColor);
                g.fillPolygon(xpts, ypts, 4);
                g.setColor(this.markerColor);
                g.drawPolygon(xpts, ypts, 4);
                break;
//        case COLUMN:
//                g.setColor(this.markerColor);
//                g.fillRect(mimath.MiMath.toInt(x), mimath.MiMath.toInt(y), this.markerSize, mimath.MiMath.toInt(y3 - y));
//                g.setColor(seriesColor);
//                g.drawRect(mimath.MiMath.toInt(x), mimath.MiMath.toInt(y), this.markerSize, mimath.MiMath.toInt(y3 - y));
//                break;
//        case BAR:
//                g.setColor(this.markerColor);
//                g.fillRect(mimath.MiMath.toInt(x3), mimath.MiMath.toInt(y), mimath.MiMath.toInt(x - x3), this.markerSize);
//                g.setColor(seriesColor);
//                g.drawRect(mimath.MiMath.toInt(x3), mimath.MiMath.toInt(y), mimath.MiMath.toInt(x - x3), this.markerSize);				
//                break;
        default:
        } 
    }
}
