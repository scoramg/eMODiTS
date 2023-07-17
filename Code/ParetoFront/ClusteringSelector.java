/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParetoFront;

import DataMining.Clustering.Cluster;
import DataSets.DataSet;
import com.jmatio.io.MatFileReader;
import DataMining.Clustering.KMeans;
import DataMining.Clustering.KMeansResultado;
import DataMining.Clustering.Punto;
import Interfaces.IScheme;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author amarquezgr
 */
public class ClusteringSelector implements iSelectors {
    
    private int k;
    private List<Punto> front;
    private int individual_type;
    private MatFileReader mfr;
    private DataSet ds;
    
    public void addPoints(double[][] front){
        this.front = new ArrayList<>();
        for (int i=0; i<front.length;i++){
            double[] row = new double[front[i].length+1];
            row[0] = i;
            for(int j=0; j< front[i].length;j++){
                row[j+1] = front[i][j];
            }
	    Punto p = new Punto(row);
	    this.front.add(p);
	}
    }
    
    public ClusteringSelector(int k, int individual_type) {
        this.k = k;
        this.individual_type = individual_type;
    }
    
    public ClusteringSelector(double[][] front, int k) {
        addPoints(front);
        this.individual_type = 0;
        this.k = k;
     }

    public ClusteringSelector(double[][] front, int individual_type, int k) {
        addPoints(front);
        this.individual_type = individual_type;
        this.k = k;
     }

    public ClusteringSelector(double[][] front, int individual_type, MatFileReader mfr, int k) {
        addPoints(front);
        this.individual_type = individual_type;
        this.mfr = mfr;
        this.k = k;
     }

    
    public ClusteringSelector(double[][] front, int individual_type, MatFileReader mfr, DataSet ds, int k) {
        addPoints(front);
        this.individual_type = individual_type;
        this.mfr = mfr;
        this.ds = ds;
        this.k = k;
    }
    

    @Override
    public int getSelection() {
        KMeans kmeans = new KMeans();
        List<Integer> candidates = new ArrayList<>();
        KMeansResultado resultado = kmeans.calcular(this.front, this.k);
        for (Cluster cluster : resultado.getClusters()) {
            int nearest = 0;
            double dist = Double.POSITIVE_INFINITY;
            for (int i= 0; i<this.front.size(); i++) {
                Punto punto = this.front.get(i);
                if(cluster.getCentroide().distanciaEuclideana(punto) < dist){
                    nearest = i;
                    dist = cluster.getCentroide().distanciaEuclideana(punto);
                }
            }
            candidates.add(nearest);
        }
        if(candidates.size() == 1){
            return candidates.get(0);
        } else {
            int ind = -1;
            Map<String, MLArray> mlArrayRetrived = mfr.getContent();
            double minErr = Double.POSITIVE_INFINITY;
            IScheme indAux;
            if (this.individual_type == 0) indAux = new Individuals.Proposal.MOScheme();
            else indAux = new Individuals.PEVOMO.MOScheme();
            
            for(int j=0;j<candidates.size();j++){
                indAux.empty();
                MLArray f = mlArrayRetrived.get("FrontIndividual"+candidates.get(j));
                double[][] individual = ((MLDouble) f).getArray();
                indAux.add(individual);
                indAux.Classify(this.ds, false, "train");

                if (indAux.getErrorRate() < minErr){
                    ind = candidates.get(j);
                    minErr = indAux.getErrorRate();
                }
            }
            return ind;
        }
    }

    @Override
    public String getName() {
        return "KMeans-" + this.k;
    }
    
}
