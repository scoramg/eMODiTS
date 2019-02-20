/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParetoFront;

import DataSets.DataSet;
import DataSets.DiscretizedDataSet;
import Exceptions.MyException;
import Interfaces.IScheme;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class ClassificationSelector implements iSelectors {
    
    private double[][] front;
    private int individual_type;
    private MatFileReader mfr;
    private boolean usingTEST;
    private DataSet ds;

    public ClassificationSelector(double[][] front) {
        this.front = front.clone();
        this.individual_type = 0;
        this.usingTEST = false;
    }

    public ClassificationSelector(double[][] front, int individual_type) {
        this.front = front.clone();
        this.individual_type = individual_type;
        this.usingTEST = false;
    }

    public ClassificationSelector(double[][] front, int individual_type, MatFileReader mfr) {
        this.front = front.clone();
        this.individual_type = individual_type;
        this.mfr = mfr;
        this.usingTEST = false;
    }

    public ClassificationSelector(double[][] front, int individual_type, MatFileReader mfr, boolean usingTEST) {
        this.front = front.clone();
        this.individual_type = individual_type;
        this.mfr = mfr;
        this.usingTEST = usingTEST;
    }

    public ClassificationSelector(double[][] front, int individual_type, MatFileReader mfr, boolean usingTEST, DataSet ds) {
        this.front = front.clone();
        this.individual_type = individual_type;
        this.mfr = mfr;
        this.usingTEST = usingTEST;
        this.ds = ds;
    }
    
    @Override
    public int getSelection() {
        int ind = -1;
//            DataSet ds = new DataSet(this.iDS);
            
        double minErr = Double.POSITIVE_INFINITY;

        IScheme indAux;
        if (this.individual_type == 0) indAux = new Individuals.Proposal.MOScheme();
        else indAux = new Individuals.PEVOMO.MOScheme();

        Map<String, MLArray> mlArrayRetrived = mfr.getContent();

        for(int j=0;j<this.front.length;j++){
            indAux.empty();
            MLArray f = mlArrayRetrived.get("FrontIndividual"+j);
            double[][] individual = ((MLDouble) f).getArray();
            indAux.add(individual);
//                indAux.setDiscretization(ds);
            indAux.Classify(ds, usingTEST, "train");
//            StringBuilder sb = new StringBuilder();
//            sb.append("j:").append(j).append(", indAux.getErrorRate():").append(indAux.getErrorRate()).append(", minErr:").append(minErr);
            if (indAux.getErrorRate() < minErr){
                ind = j;
                minErr = indAux.getErrorRate();
//                sb.append(", ind:").append(ind);
            }
//            System.out.println(sb.toString());
        }
        return ind;
    }

    @Override
    public String getName() {
        if (this.usingTEST){
            return "CTT";
        } else {
            return "CTV";
        }
    }
    
}
