/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amarquezgr
 */
public class OneDimensionHistogram {
    private List<Double> data;
    private int klass;

    public List<Double> getData() {
        return data;
    }

    public int getKlass() {
        return klass;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }

    public void setKlass(int klass) {
        this.klass = klass;
    }

    public OneDimensionHistogram() {
        this.data = new ArrayList<>();
        this.klass = Integer.MIN_VALUE;
    }
    
    public void addData(Double value){
        this.data.add(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(this.klass).append(", Data: ");
        for(Double value: this.data){
            sb.append(value).append(" ");
        }
        return sb.toString();
    }
    
    
}
