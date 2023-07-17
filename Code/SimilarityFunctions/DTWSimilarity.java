/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimilarityFunctions;

/**
 *
 * @author amarquezgr
 */
public class DTWSimilarity { 
 
    private static final long serialVersionUID = -8898553450277603746L; 
 
    private double pointDistance(int i, int j, double[] ts1, double[] ts2) { 
        double diff = ts1[i] - ts2[j]; 
        return (diff * diff); 
    } 
    
    private double pointDistance(int i, int j, Double[] ts1, Double[] ts2) { 
        double diff = ts1[i] - ts2[j]; 
        return (diff * diff); 
    } 
 
    private double distance2Similarity(double x) { 
        return (1.0 - (x / (1 + x))); 
    } 
 
    public double measure(double[] ts1, double[] ts2) { 
 
        /** Build a point-to-point distance matrix */ 
        double[][] dP2P = new double[ts1.length][ts2.length]; 
        for (int i = 0; i < ts1.length; i++) { 
            for (int j = 0; j < ts2.length; j++) { 
                dP2P[i][j] = pointDistance(i, j, ts1, ts2); 
            } 
        } 
 
        /** Check for some special cases due to ultra short time series */ 
        if (ts1.length == 0 || ts2.length == 0) { 
            return Double.NaN; 
        } 
 
        if (ts1.length == 1 && ts2.length == 1) { 
            return distance2Similarity(Math.sqrt(dP2P[0][0])); 
        } 
 
        /**
         * Build the optimal distance matrix using a dynamic programming 
         * approach 
         */ 
        double[][] D = new double[ts1.length][ts2.length]; 
 
        D[0][0] = dP2P[0][0]; // Starting point 
 
        for (int i = 1; i < ts1.length; i++) { // Fill the first column of our 
            // distance matrix with optimal 
            // values 
            D[i][0] = dP2P[i][0] + D[i - 1][0]; 
        } 
 
        if (ts2.length == 1) { // TS2 is a point 
            double sum = 0; 
            for (int i = 0; i < ts1.length; i++) { 
                sum += D[i][0]; 
            } 
            return distance2Similarity(Math.sqrt(sum) / ts1.length); 
        } 
 
        for (int j = 1; j < ts2.length; j++) { // Fill the first row of our 
            // distance matrix with optimal 
            // values 
            D[0][j] = dP2P[0][j] + D[0][j - 1]; 
        } 
 
        if (ts1.length == 1) { // TS1 is a point 
            double sum = 0; 
            for (int j = 0; j < ts2.length; j++) { 
                sum += D[0][j]; 
            } 
            return distance2Similarity(Math.sqrt(sum) / ts2.length); 
        } 
 
        for (int i = 1; i < ts1.length; i++) { // Fill the rest 
            for (int j = 1; j < ts2.length; j++) { 
                double[] steps = { D[i - 1][j - 1], D[i - 1][j], D[i][j - 1] }; 
                double min = Math.min(steps[0], Math.min(steps[1], steps[2])); 
                D[i][j] = dP2P[i][j] + min; 
            } 
        } 
 
        /**
         * Calculate the distance between the two time series through optimal 
         * alignment. 
         */ 
        int i = ts1.length - 1; 
        int j = ts2.length - 1; 
        int k = 1; 
        double dist = D[i][j]; 
 
        while (i + j > 2) { 
            if (i == 0) { 
                j--; 
            } else if (j == 0) { 
                i--; 
            } else { 
                double[] steps = { D[i - 1][j - 1], D[i - 1][j], D[i][j - 1] }; 
                double min = Math.min(steps[0], Math.min(steps[1], steps[2])); 
 
                if (min == steps[0]) { 
                    i--; 
                    j--; 
                } else if (min == steps[1]) { 
                    i--; 
                } else if (min == steps[2]) { 
                    j--; 
                } 
            } 
            k++; 
            dist += D[i][j]; 
        } 
 
        return distance2Similarity(Math.sqrt(dist) / k); 
    } 
    
    public double measure(Double[] ts1, Double[] ts2) { 
 
        /** Build a point-to-point distance matrix */ 
        double[][] dP2P = new double[ts1.length][ts2.length]; 
        for (int i = 0; i < ts1.length; i++) { 
            for (int j = 0; j < ts2.length; j++) { 
                dP2P[i][j] = pointDistance(i, j, ts1, ts2); 
            } 
        } 
 
        /** Check for some special cases due to ultra short time series */ 
        if (ts1.length == 0 || ts2.length == 0) { 
            return Double.NaN; 
        } 
 
        if (ts1.length == 1 && ts2.length == 1) { 
            return distance2Similarity(Math.sqrt(dP2P[0][0])); 
        } 
 
        /**
         * Build the optimal distance matrix using a dynamic programming 
         * approach 
         */ 
        double[][] D = new double[ts1.length][ts2.length]; 
 
        D[0][0] = dP2P[0][0]; // Starting point 
 
        for (int i = 1; i < ts1.length; i++) { // Fill the first column of our 
            // distance matrix with optimal 
            // values 
            D[i][0] = dP2P[i][0] + D[i - 1][0]; 
        } 
 
        if (ts2.length == 1) { // TS2 is a point 
            double sum = 0; 
            for (int i = 0; i < ts1.length; i++) { 
                sum += D[i][0]; 
            } 
            return distance2Similarity(Math.sqrt(sum) / ts1.length); 
        } 
 
        for (int j = 1; j < ts2.length; j++) { // Fill the first row of our 
            // distance matrix with optimal 
            // values 
            D[0][j] = dP2P[0][j] + D[0][j - 1]; 
        } 
 
        if (ts1.length == 1) { // TS1 is a point 
            double sum = 0; 
            for (int j = 0; j < ts2.length; j++) { 
                sum += D[0][j]; 
            } 
            return distance2Similarity(Math.sqrt(sum) / ts2.length); 
        } 
 
        for (int i = 1; i < ts1.length; i++) { // Fill the rest 
            for (int j = 1; j < ts2.length; j++) { 
                double[] steps = { D[i - 1][j - 1], D[i - 1][j], D[i][j - 1] }; 
                double min = Math.min(steps[0], Math.min(steps[1], steps[2])); 
                D[i][j] = dP2P[i][j] + min; 
            } 
        } 
 
        /**
         * Calculate the distance between the two time series through optimal 
         * alignment. 
         */ 
        int i = ts1.length - 1; 
        int j = ts2.length - 1; 
        int k = 1; 
        double dist = D[i][j]; 
 
        while (i + j > 2) { 
            if (i == 0) { 
                j--; 
            } else if (j == 0) { 
                i--; 
            } else { 
                double[] steps = { D[i - 1][j - 1], D[i - 1][j], D[i][j - 1] }; 
                double min = Math.min(steps[0], Math.min(steps[1], steps[2])); 
 
                if (min == steps[0]) { 
                    i--; 
                    j--; 
                } else if (min == steps[1]) { 
                    i--; 
                } else if (min == steps[2]) { 
                    j--; 
                } 
            } 
            k++; 
            dist += D[i][j]; 
        } 
 
        return distance2Similarity(Math.sqrt(dist) / k); 
    } 
 
}