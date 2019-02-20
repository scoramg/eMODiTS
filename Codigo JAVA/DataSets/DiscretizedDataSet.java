/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataSets;

import Individuals.Proposal.WordCut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class DiscretizedDataSet implements Cloneable{
    private DiscretizedData Original, Train, Test;

    public DiscretizedData getOriginal() {
        return Original;
    }

    public DiscretizedData getTrain() {
        return Train;
    }

    public DiscretizedData getTest() {
        return Test;
    }

    public void setOriginal(DiscretizedData Original) {
        this.Original = Original;
    }

    public void setTrain(DiscretizedData Train) {
        this.Train = Train;
    }

    public void setTest(DiscretizedData Test) {
        this.Test = Test;
    }

    public DiscretizedDataSet() {
    }
    
    @Override
    public DiscretizedDataSet clone() {
        try {
            super.clone();
//            DiscretizedDataSet clon = new DiscretizedDataSet(this.getDimensions()[0], this.getDimensions()[1]);
            DiscretizedDataSet clon = new DiscretizedDataSet();
            clon.setOriginal(Original.clone());
            clon.setTest(Test.clone());
            clon.setTrain(Train.clone());
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DiscretizedDataSet.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
