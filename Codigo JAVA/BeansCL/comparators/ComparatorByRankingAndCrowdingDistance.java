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
public class ComparatorByRankingAndCrowdingDistance implements Comparator<HistogramScheme> {

    @Override
    public int compare(HistogramScheme o1, HistogramScheme o2) {
        if (o1==null)
            return 1;
        else if (o2 == null)
                return -1;
        
        if (o1.getRank()<o2.getRank()) {
            return -1;
        } else  {
            if (o1.getRank()>o2.getRank()) {
                return 1;
            } else {
                if(o1.getCrowdingDistance() > o2.getCrowdingDistance()){
                    return -1;
                } else {
                    if(o1.getCrowdingDistance() < o2.getCrowdingDistance()){
                        return 1;
                    }
                }
            }        
        }

        return 0;
    }
    
}
