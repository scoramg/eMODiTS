/*
DTW with early abandon
 */
package SimilarityFunctions;

/**
 *
 * @author ajb
 */
public final class DTW extends DTW_DistanceBasic {
    
//    /**
//     *
//     * @param a
//     * @param b
//     * @param cutoff
//     * @return
//     */
//    @Override
// public final double distance(double[] a,double[] b, double cutoff){
//        double minDist;
//        boolean tooBig;
//// Set the longest series to a. is this necessary?
//        double[] temp;
//        if(a.length<b.length){
//                temp=a;
//                a=b;
//                b=temp;
//        }
//        int n=a.length;
//        int m=b.length;
///*  Parameter 0<=r<=1. 0 == no warp, 1 == full warp 
//generalised for variable window size
//* */
//        windowSize = getWindowSize(n);
////Extra memory than required, could limit to windowsize,
////        but avoids having to recreate during CV 
////for varying window sizes        
//        if(matrixD==null)
//            matrixD=new double[n][m];
//        
///*
////Set boundary elements to max. 
//*/
//        int start,end;
//        for(int i=0;i<n;i++){
//            start=windowSize<i?i-windowSize:0;
//            end=i+windowSize+1<m?i+windowSize+1:m;
//            for(int j=start;j<end;j++)
//                matrixD[i][j]=Double.MAX_VALUE;
//        }
//        matrixD[0][0]=(a[0]-b[0])*(a[0]-b[0]);
////a is the longer series. 
////Base cases for warping 0 to all with max interval	r	
////Warp a[0] onto all b[1]...b[r+1]
//        for(int j=1;j<windowSize && j<m;j++)
//                matrixD[0][j]=matrixD[0][j-1]+(a[0]-b[j])*(a[0]-b[j]);
//
////	Warp b[0] onto all a[1]...a[r+1]
//        for(int i=1;i<windowSize && i<n;i++)
//                matrixD[i][0]=matrixD[i-1][0]+(a[i]-b[0])*(a[i]-b[0]);
////Warp the rest,
//        for (int i=1;i<n;i++){
//            tooBig=true; 
//            start=windowSize<i?i-windowSize+1:1;
//            end=i+windowSize<m?i+windowSize:m;
//            for (int j = start;j<end;j++){
//                    minDist=matrixD[i][j-1];
//                    if(matrixD[i-1][j]<minDist)
//                            minDist=matrixD[i-1][j];
//                    if(matrixD[i-1][j-1]<minDist)
//                            minDist=matrixD[i-1][j-1];
//                    matrixD[i][j]=minDist+(a[i]-b[j])*(a[i]-b[j]);
//                    if(tooBig&&matrixD[i][j]<cutoff)
//                            tooBig=false;               
//            }
//            //Early abandon
//            if(tooBig){
//                return Double.MAX_VALUE;
//            }
//        }			
////Find the minimum distance at the end points, within the warping window. 
//        return matrixD[n-1][m-1];
//    }
    private static final long serialVersionUID = -8898553450277603746L; 
 
    private double pointDistance(int i, int j, double[] ts1, double[] ts2) { 
        double diff = ts1[i] - ts2[j]; 
        return (diff * diff); 
    } 
 
    private double distance2Similarity(double x) { 
        return (1.0 - (x / (1 + x))); 
    } 
 
    public double distance(double[] ts1, double[] ts2, double cutoff) { 
 
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
        System.out.println("ts1.length:"+ts1.length+", ts2.length:"+ts2.length);
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
