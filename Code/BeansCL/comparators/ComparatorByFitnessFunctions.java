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
public class ComparatorByFitnessFunctions implements Comparator<HistogramScheme> {

    private final int iFunction;

    public ComparatorByFitnessFunctions (int iFunction) {
        this.iFunction = iFunction;
    }

    @Override
    public int compare(HistogramScheme e1, HistogramScheme e2) {
//        if (type.equals("name")) {
//             return e1.getName().compareTo(e2.getName());
//        }
//        return e1.getId().compareTo(e2.getId());
        return new Double(e1.getEvaluatedValues()[iFunction]).compareTo(e2.getEvaluatedValues()[iFunction]);
    }

}