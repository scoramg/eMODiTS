/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

/**
 *
 * @author amarquezgr
 */
public class Range {
    private double min;
    private double max;
    private double diff;


    public Range(double min, double max) {
            this.min = min;
            this.max = max;
            this.diff = max - min;
    }

    public Range(Range range) {
            this.min = range.min;
            this.max = range.max;
            this.diff = max - min;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getDiff() {
        return diff;
    }

    public void setMin(double min) {
            this.min = min;
            this.diff = max - min;
    }

    public void setMax(double max) {
            this.max = max;
            this.diff = max - min;
    }

    @Override
    public String toString() {
            return "Range [min=" + min + ", max=" + max + ", diff="+diff+"]";
    }
}
