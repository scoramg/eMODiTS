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
public class DiscreteHistogramRow {
        private List<Double> row;

        public DiscreteHistogramRow() {
            row = new ArrayList<>();
        }

        public List<Double> getRow() {
            return row;
        }

        public void setRow(List<Double> row) {
            this.row = row;
        }
        
        public void addValue(double valor){
            this.row.add(valor);
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for(Double d: this.row){
                sb.append(d).append(" ");
            }
            return sb.toString();
        }
    }
