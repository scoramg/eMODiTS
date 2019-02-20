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
public class ComparatorByErrorRates implements Comparator<IScheme> {

    
    public ComparatorByErrorRates () {
    }

    @Override
    public int compare(IScheme e1, IScheme e2) {
//        if (type.equals("name")) {
//             return e1.getName().compareTo(e2.getName());
//        }
//        return e1.getId().compareTo(e2.getId());
        return new Double(e1.getErrorRate()).compareTo(e2.getErrorRate());
    }

}