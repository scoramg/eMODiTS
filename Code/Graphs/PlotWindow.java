/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

import Graphs.LegendsCollection.Orientation;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author amarquezgr
 */
public class PlotWindow {
    public static final int PADDING=20;
    
    private int Width;
    private int Height;
    private int[] margins; //0 - top, 1-left, 2-bottom, 3-rigth
    private PlotArea area;
    private String title;
    private String xlabel;
    private String ylabel;
    private SeriesCollection data;
    private LegendsCollection legends;
    private Font titleFont = new Font("Arial", Font.BOLD, 16);
    private Font labelFont = new Font("Arial", 0, 14);
    private Graphics2D graphics;
    
    
    public PlotWindow(SeriesCollection data) {
        this.data = data;
        this.margins = new int[4];
        setMarginDefaultValues();
        this.title = "";
        this.xlabel = "";
        this.ylabel = "";
        this.legends = new LegendsCollection();
    }
    
    public PlotWindow() {
        this.data = new SeriesCollection();
        this.legends = new LegendsCollection();
        this.margins = new int[4];
        setMarginDefaultValues();
        this.title = "";
        this.xlabel = "";
        this.ylabel = "";
    }

    public int getWidth() {
        return Width;
    }

    public int getHeight() {
        return Height;
    }

    public int[] getMargins() {
        return margins;
    }

    public PlotArea getArea() {
        return area;
    }

    public SeriesCollection getData() {
        return data;
    }

    public String getTitle() {
        return title;
    }

    public String getXlabel() {
        return xlabel;
    }

    public String getYlabel() {
        return ylabel;
    }

    public LegendsCollection getLegends() {
        return legends;
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public void setWidth(int Width) {
        this.Width = Width;
    }

    public void setHeigth(int Height) {
        this.Height = Height;
    }

    public void setMargins(int[] margins) {
        this.margins = margins;
    } 

    public void setTitle(String title) {
        this.title = title;
    }

    public void setXlabel(String xlabel) {
        this.xlabel = xlabel;
    }

    public void setYlabel(String ylabel) {
        this.ylabel = ylabel;
    }

    public void setMarginDefaultValues(){
        setBottomMargin(PADDING);
        setTopMargin(PADDING);
        setLeftMargin(PADDING);
        setRightMargin(PADDING);
    }
 
    public void setBottomMargin(int margin){
        this.margins[2] = margin;
    }
    
    public void setTopMargin(int margin){
        this.margins[0] = margin;
    }
    
    public void setLeftMargin(int margin){
        this.margins[1] = margin;
    }
    
    public void setRightMargin(int margin){
        this.margins[3] = margin;
    }

    public void setGraphics(Graphics2D graphics) {
        this.graphics = graphics;
    }

    public void addBottomMargin(int increase){
        this.margins[2] += increase;
    }
    
    public void addTopMargin(int increase){
        this.margins[0] += increase;
    }
    
    public void addLeftMargin(int increase){
        this.margins[1] += increase;
    }
    
    public void addRightMargin(int increase){
        this.margins[3] += increase;
    }
    
    public int getBottomMargin(){
        return this.margins[2];
    }
    
    public int getTopMargin(){
        return this.margins[0];
    }
    
    public int getLeftMargin(){
        return this.margins[1];
    }
    
    public int getRightMargin(){
        return this.margins[3];
    }

    public void setData(SeriesCollection data) {
        this.data = data;
    }

    public static void drawString(Graphics2D g, String s, int x, int y, String hAlign, String vAlign) {
            FontMetrics fm = g.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds(s, g);
            
            // by default align by left
            if (hAlign == "RIGHT")
                    x -= rect.getWidth();
            else if (hAlign == "CENTER")
                    x -= rect.getWidth() / 2;

            // by default align by bottom
            if (vAlign == "TOP")
                    y += rect.getHeight();
            else if (vAlign == "CENTER")
                    y += rect.getHeight() / 2;

            g.drawString(s, x, y);
    }
    
    private void drawTitle(){
        graphics.setFont(this.titleFont);
        FontMetrics fm = graphics.getFontMetrics();
//        System.out.println(fm.getStringBounds(this.title, this.graphics).getWidth());
//        int halfstringsize = mimath.MiMath.toInt(fm.stringWidth(title) / 2);
        int halfwindowwidth = mimath.MiMath.toInt(this.Width / 2);
        drawString(graphics, title,halfwindowwidth, PADDING, "CENTER", "CENTER");
        this.addTopMargin(this.getTopMargin()+fm.getHeight());
    }
    
    private void drawXLabel(){
        graphics.setFont(this.labelFont);
        FontMetrics fm = graphics.getFontMetrics();
//        int halfstringsize = mimath.MiMath.toInt(fm.stringWidth(xlabel) / 2);
        int halfwindowwidth = mimath.MiMath.toInt(this.Width / 2);
        drawString(graphics, xlabel,halfwindowwidth, this.Height+AxisOptions.MARGIN_XTICKSLABEL-this.getBottomMargin(), "CENTER", "TOP");
        this.addBottomMargin(fm.getHeight());
    }
    
    private void drawYLabel(){
        graphics.setFont(this.labelFont);
        FontMetrics fm = graphics.getFontMetrics();
//        int halfstringsize = mimath.MiMath.toInt(fm.stringWidth(ylabel) / 2);
        int halfwindowheight = mimath.MiMath.toInt(this.Height / 2);
        int x = this.getLeftMargin()-area.axis.getYticklabelwidth()-AxisOptions.MARGIN_XTICKSLABEL;
        int y = halfwindowheight;
        int angle = -90;
        graphics.translate((float)x,(float)y);
        graphics.rotate(Math.toRadians(angle));
        drawString(graphics, ylabel, 0, 0, "CENTER", "CENTER");
        graphics.rotate(-Math.toRadians(angle));
        graphics.translate(-(float)x,-(float)y);
        this.addLeftMargin(fm.getHeight());
    }
    
    private void drawBackground() {
        graphics.setColor(Color.WHITE); 
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }
    
    public void draw(boolean withMarks, Marker.MarkerShape markershape, boolean withLegends){
        this.drawBackground();
        this.area = new PlotArea(this);
        this.addBottomMargin(area.axis.getXticklabelheight());
        this.addLeftMargin(area.axis.getYticklabelwidth());
        
        if(withLegends){
            legends.draw(graphics, this, Orientation.TOP);
        }
        
        if (!this.title.isEmpty()){
            drawTitle();
        }
        if (!this.xlabel.isEmpty()){
            drawXLabel();
        }
        if (!this.ylabel.isEmpty()){
            drawYLabel();
        }
        this.area.drawArea(graphics, this);
        this.data.draw(graphics, this, withMarks, markershape);
    }
    
}
