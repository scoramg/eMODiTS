/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParetoFront;

/**
 *
 * @author amarquezgr
 */
public class ReferencePointSelector implements iSelectors{
    private ReferencePoint reference_point;
    private double[][] front;
    private int type_point;

    public ReferencePointSelector(double[][] data, int type_point) {
        this.front = data.clone();
        this.type_point = type_point;
        if (this.front.length>0){
            reference_point = new ReferencePoint(data[0].length);
            switch(this.type_point){
                case 0: //Knee
                    reference_point.getKneePoint();
                    break;
                case 1:
                    reference_point.getMeanPoint(data);
                    break;
            }
            
        }
//        System.out.println(reference_point.toString());
    }
    
    @Override
    public int getSelection(){
        int ind=-1;
        double min = 10000000;
        for(int j=0;j<front.length;j++){
            double[] fila = front[j];
            if (mimath.MiMath.getEuclideanDist(fila, reference_point.getPoint()) < min){
                min = mimath.MiMath.getEuclideanDist(fila, reference_point.getPoint());
                ind = j;
            }
        }
        return ind;
    }
    
    @Override
    public String getName(){
//        return "Knee";
        String name = "";
        switch(this.type_point){
            case 0: //Knee
                name = "Knee";
                break;
            case 1:
                name = "Centroid";
                break;
        }
        return name;
    }
}
