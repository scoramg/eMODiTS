/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparators;

import Interfaces.IScheme;
import java.util.Comparator;

/**
 *
 * @author amarquezgr
 */
public class ComparatorByRankingAndCrowdingDistance implements Comparator<IScheme> {

    @Override
    public int compare(IScheme o1, IScheme o2) {
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
