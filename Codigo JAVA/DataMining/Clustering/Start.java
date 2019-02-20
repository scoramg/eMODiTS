package DataMining.Clustering;

import DataSets.DataSet;
import Exceptions.MyException;
import Interfaces.IScheme;
import Utils.Utils;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import au.com.bytecode.opencsv.CSVReader;

public class Start {
    public static void main(String[] args) throws IOException, MyException {
        List<Punto> puntos = new ArrayList<>();
        double[] data = {-2.7379, -2.3043, -2.8384, -2.6345, -2.5591, -2.8684, -2.9709, -2.8899, -2.0437, -1.3889, -1.7081, -0.9905, -0.8189, -0.8338, -0.6648, -0.7133, -0.7427, -0.3904, -0.0572, 0.2779, 0.4366, 0.1511, 0.3207, 0.7062, 0.8218, 1.0665, 1.2383, 1.1666, 1.2361, 1.0499, 1.2600, 0.9888, 0.7360, 0.5446, -0.1516, 0.1885, 0.2654, 0.0869, 0.4109, 0.0062, -0.0179, -0.0750, 0.0004, 0.0744, -0.1301, -0.1372, -0.1762, -0.0278, 0.2308, 0.4930, 0.2888, 0.3071, 0.0200, -0.2433, -0.2449, 0.1175, -0.0645, 0.0233, -0.0300, 0.2342, -0.0233, -0.0156, 0.1150, 0.3753, 0.7404, 0.7607, 0.4081, 0.2325, -0.0185, 0.5373, 0.3917, 0.5686, 0.5231, 0.7332, 0.5524, 0.2208, -0.1155, -0.0001, -0.0420, -0.0884, 0.2472, 0.3162, 0.3629, 0.7384, 0.5481, 0.7129, 0.9103, 0.8527, 0.9037, 0.6280, 0.3566, 0.3814, 0.5522, 1.1635, 1.0058, 1.0501, 1.0306, 0.5735, 0.4697, 0.0454};
        for(int i=0;i<data.length;i++){
            double[] row = new double[1];
            row[0] = data[i];
            Punto p = new Punto(row);
            puntos.add(p);
        }
        KMeans kmeans = new KMeans();
        int k = 10;
        KMeansResultado resultado = kmeans.calcular(puntos, k);
        for (Cluster cluster : resultado.getClusters()) {
            System.out.println(cluster.getCentroide().toString());
        }
        
//        DataSet ds = new DataSet(1);
//        String Location = "e15p100g300";
//        IScheme individual = new Individuals.Proposal.MOScheme();
//        String dir = Utils.findDirectory(System.getProperty("user.dir") + "/" + Location, individual.getName()) + "/" + ds.getName() + "/";
//        String file = ds.getName() + "_" + individual.getName();
//        MatFileReader mfr = new MatFileReader(dir + file + ".mat");
//        Map<String, MLArray> mlArrayRetrived = mfr.getContent();
//        MLArray w = mlArrayRetrived.get("AccumulatedFrontFitness");
//        double[][] arr = ((MLDouble) w).getArray();
//
//	List<Punto> puntos = new ArrayList<Punto>();
//
//        for (int i=0; i<arr.length;i++){
//            double[] row = new double[arr[i].length+1];
//            row[0] = i;
//            for(int j=0; j< arr[i].length;j++){
//                row[j+1] = arr[i][j];
//            }
//	    Punto p = new Punto(row);
//	    puntos.add(p);
//	}
//
//	KMeans kmeans = new KMeans();
//        List<Punto> candidates = new ArrayList<>();
//        int k = 5;
//        KMeansResultado resultado = kmeans.calcular(puntos, k);
//        int i = 0;
//        for (Cluster cluster : resultado.getClusters()) {
//            i++;
//            Punto CentroidNearest = new Punto();
//            double dist = Double.POSITIVE_INFINITY;
//            for (Punto punto : cluster.getPuntos()) {
//                if(mimath.MiMath.getEuclideanDist(punto.getData(1), cluster.getCentroide().getData(1)) < dist){
//                    CentroidNearest = punto;
//                    dist = mimath.MiMath.getEuclideanDist(punto.getData(1), cluster.getCentroide().getData(1));
//                }
//            }
//            candidates.add(CentroidNearest);
//            System.out.println(" -- Centroid Cluster "+i+" --");
//            System.out.println(cluster.getCentroide().toString());
//            System.out.println(" ");
//        }
//        for(Punto p: candidates){
//            System.out.println(p.toString());
//        }
    }
}