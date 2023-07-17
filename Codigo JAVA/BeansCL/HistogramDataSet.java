/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeansCL;

import Utils.Utils;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class HistogramDataSet extends DataSets.DataSet implements Cloneable{
    private HistogramCollection HistComplete;
    private HistogramCollection HistTrain;
    private HistogramCollection HistTest;
    
    @Override
    public double[][] getLimits() {
        return limits;
    }

    public HistogramCollection getHistComplete() {
        return HistComplete;
    }

    public HistogramCollection getHistTrain() {
        return HistTrain;
    }

    public HistogramCollection getHistTest() {
        return HistTest;
    }

    public void setOriginal(HistogramCollection complete) {
        this.HistComplete = complete;
    }

    public void setTrain(HistogramCollection HistTrain) {
        this.HistTrain = HistTrain;
    }

    public void setTest(HistogramCollection HistTest) {
        this.HistTest = HistTest;
    }

    public HistogramDataSet() {
    }
    
    public HistogramDataSet(int iDS) {
        this.setRuta_ucr(Utils.findDirectory(System.getProperty("user.dir"), "Datasets")+"/");
        String bd = this.getUCRRepository(iDS);
        this.setName(bd);
        this.setIndex(iDS);
        this.HistComplete = new HistogramCollection();
        this.HistTrain = new HistogramCollection();
        this.HistTest = new HistogramCollection();
        if(iDS>0){
            String file =  this.getRuta_ucr() + bd +"/"+bd+".mat";  
            
            try {
                MatFileReader matfilereader = new MatFileReader(file);
                this.limits = ((MLDouble) matfilereader.getMLArray("limites")).getArray(); 
                this.HistComplete.load(matfilereader.getMLArray(bd));
                this.HistTrain.load(matfilereader.getMLArray(bd+"_TRAIN"));
                this.HistTest.load(matfilereader.getMLArray(bd+"_TEST"));
            } catch (IOException ex) {
                Logger.getLogger(HistogramDataSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.limits = new double[1][2];
        }
    }
    
    @Override
    public HistogramDataSet clone() {
        super.clone();
        HistogramDataSet clon = new HistogramDataSet();
        clon.setOriginal(this.HistComplete);
        clon.setTrain(this.HistTrain);
        clon.setTest(this.HistTest);
        return clon;
        
    }
    
    public void destroy(){
        this.HistComplete = null;
        this.HistTest = null;
        this.HistTrain = null;
    }
    
    public static void main(String[] args) {
        HistogramDataSet hds = new HistogramDataSet(90);
        System.out.println(hds.getHistTrain().getData().get(1).toString());
        System.out.println(hds.getHistTrain().getData().get(1).getColumn(0).toString());
        System.out.println(hds.getHistTrain().getValuesFrom(1, 0, 2, 0, 2).toString());
        
    }
}
