/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author amarquezgr
 */
public class Plotter extends JPanel{
    private static final int PREF_W = 800;
    private static final int PREF_H = 650;
    private PlotWindow window;
    private boolean withMarks, withLegends;
    private Marker.MarkerShape markershape;
    private String title, xlabel, ylabel;

    public PlotWindow getWindow() {
        return window;
    }

    public Plotter(boolean withMarks, Marker.MarkerShape markershape, boolean withLegend, String title, String xlabel, String ylabel) {
        window = new PlotWindow();
        this.withMarks = withMarks;
        this.withLegends = withLegend;
        this.markershape = markershape;
        this.title = title;
        this.xlabel = xlabel;
        this.ylabel = ylabel;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    @Override
    public Dimension getPreferredSize() {
       return new Dimension(PREF_W, PREF_H);
    }
    
    public void plot(String WindowTitle) {
       JFrame frame = new JFrame(WindowTitle);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.getContentPane().add(this);
       frame.pack();
       frame.setLocationByPlatform(true);
       frame.setVisible(true);
    }
    
    public void save(String fileName, String type) throws IOException {
//        clear();
        BufferedImage bi = draw();
        File outputFile = new File(fileName + "." + type);
        ImageIO.write(bi, type, outputFile);
    }
    
//    private void clear() {
//        window.setData(new SeriesCollection());
//        window.draw(withMarks, markershape, withLegends);
//    }
    private void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        window.setHeigth(getHeight());
        window.setWidth(getWidth());
        window.setTitle(title);
        window.setXlabel(xlabel);
        window.setYlabel(ylabel);
        window.setGraphics(g2);
        window.setMarginDefaultValues();
        window.draw(this.withMarks, this.markershape, this.withLegends);
    }
    private BufferedImage draw() {
        window.setHeigth(PREF_H);
        window.setWidth(PREF_W);
        BufferedImage image = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        try {
                window.setTitle(title);
                window.setXlabel(xlabel);
                window.setYlabel(ylabel);
                window.setGraphics(g);
                window.setMarginDefaultValues();
                window.draw(this.withMarks, this.markershape, this.withLegends);
                return image;
        } finally {
                g.dispose();
        }
    }

    public void addData(double[] data, Color color){
        this.window.getData().addSerie(new Serie(data, color));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Plotter plotter = new Plotter(false, Marker.MarkerShape.CIRCLE, true,"Random Time Series", "Time", "Values");
                double[] scores = new double[1000];
                for(int i=0;i < scores.length;i++){
                    scores[i] = mimath.MiMath.random(-100,100);
                }
                Legend legend = new Legend("Serie Prueba",Color.RED);
                plotter.window.getLegends().addLegend(legend);
                plotter.window.getData().addSerie(new Serie(scores,legend));
                plotter.plot("Prueba");                
          }
       });
    }
}
