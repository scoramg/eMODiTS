/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL.comparators;

import BeansCL.HistogramScheme;
import java.util.Comparator;

/**
 *
 * @author amarquezgr
 */
public class ComparatorByErrorRates implements Comparator<HistogramScheme> {

    
    public ComparatorByErrorRates () {
    }

    @Override
    public int compare(HistogramScheme e1, HistogramScheme e2) {

        return new Double(e1.getErrorRate()).compareTo(e2.getErrorRate());
    }

}