/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataSets;

import Individuals.Proposal.WordCut;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class DiscretizedData implements Cloneable{
    private int[][] ids_discretized;
    private char[][] cds_discretized;
    private String[][] sds_discretized;
    private float[][] fds_discretized;
    private double[][] dds_discretized;
    private int[][] icds_discretized; //arreglo discretizado utilizando los integers unicos en cada segmento de palabra. Se utiliza unicamente para la propuesta.
    
    private int[] dimensions; 
   
    public int[][] getIds_discretized() {
        return ids_discretized;
    }

    public String[][] getSds_discretized() {
        return sds_discretized;
    }

    public float[][] getFds_discretized() {
        return fds_discretized;
    }

    public double[][] getDds_discretized() {
        return dds_discretized;
    }

    public int[][] getIcds_discretized() {
        return icds_discretized;
    }

    public int[] getDimensions() {
        return dimensions;
    }

    public void setIds_discretized(int[][] ids_discretized) {
        this.ids_discretized = ids_discretized.clone();
    }

    public void setCds_discretized(char[][] cds_discretized) {
        this.cds_discretized = cds_discretized.clone();
    }

    public void setSds_discretized(String[][] sds_discretized) {
        this.sds_discretized = sds_discretized.clone();
    }

    public void setFds_discretized(float[][] fds_discretized) {
        this.fds_discretized = fds_discretized.clone();
    }

    public void setDds_discretized(double[][] dds_discretized) {
        this.dds_discretized = dds_discretized;
    }

    public void setIcds_discretized(int[][] icds_discretized) {
        this.icds_discretized = icds_discretized;
    }
    
    public void setDimensions(int[] dimensions) {
        this.dimensions = dimensions.clone();
    }

    public DiscretizedData() {
        dimensions = new int[2];
        dimensions[0] = 0;
        dimensions[1] = 0;
        this.ids_discretized = new int[dimensions[0]][0];
        this.cds_discretized = new char[dimensions[0]][0];
        this.sds_discretized = new String[dimensions[0]][2];
        this.fds_discretized = new float[dimensions[0]][0];
        this.dds_discretized = new double[dimensions[0]][0];
        this.icds_discretized = new int[dimensions[0]][0];
    }
    
    public DiscretizedData(int attributes){
        dimensions = new int[2];
        dimensions[0] = 0;
        dimensions[1] = attributes;
        this.ids_discretized = new int[dimensions[0]][attributes];
        this.cds_discretized = new char[dimensions[0]][attributes];
        this.sds_discretized = new String[dimensions[0]][2];
        this.fds_discretized = new float[dimensions[0]][attributes];
        this.dds_discretized = new double[dimensions[0]][attributes];
        this.icds_discretized = new int[dimensions[0]][attributes];
    }
    
    public DiscretizedData(int instances, int attributes) {
        this.ids_discretized = new int[instances][attributes];
        this.cds_discretized = new char[instances][attributes];
        this.sds_discretized = new String[instances][2];
        this.fds_discretized = new float[instances][attributes];
        this.dds_discretized = new double[instances][attributes];
        this.icds_discretized = new int[instances][attributes];
        dimensions = new int[2];
        dimensions[0] = instances;
        dimensions[1] = attributes;
    }
    
    public void addEmptyRow(int attributesNumber){
        dimensions[0]++;
        dimensions[1] = attributesNumber;
        double[][] new_dds_discretized = new double[dimensions[0]][dimensions[1]];
        for (int j=0;j< this.dds_discretized.length; j++)
            System.arraycopy(this.dds_discretized[j], 0, new_dds_discretized[j], 0, dimensions[1]);
        for (int i=0; i<this.dimensions[1];i++)
            new_dds_discretized[dimensions[0]-1][i] = Double.NaN;
        this.dds_discretized = new_dds_discretized.clone();
        
        int[][] new_ids_discretized = new int[dimensions[0]][dimensions[1]];
        for (int j=0;j< this.ids_discretized.length; j++)
            System.arraycopy( this.ids_discretized[j], 0, new_ids_discretized[j], 0, dimensions[1]);
        for (int i=0; i<this.dimensions[1];i++)
            new_ids_discretized[dimensions[0]-1][i] = Integer.MIN_VALUE;
        this.ids_discretized = new_ids_discretized.clone();
        
        char[][] new_cds_discretized = new char[dimensions[0]][dimensions[1]];
        for (int j=0;j< this.cds_discretized.length; j++)
            System.arraycopy( this.cds_discretized[j], 0, new_cds_discretized[j], 0, dimensions[1]);
        for (int i=0; i<this.dimensions[1];i++)
            new_cds_discretized[dimensions[0]-1][i] = ' ';
        this.cds_discretized = new_cds_discretized.clone();
        
        float[][] new_fds_discretized = new float[dimensions[0]][dimensions[1]];
        for (int j=0;j< this.fds_discretized.length; j++)
            System.arraycopy( this.fds_discretized[j], 0, new_fds_discretized[j], 0, dimensions[1]);
        for (int i=0; i<this.dimensions[1];i++)
            new_fds_discretized[dimensions[0]-1][i] = Float.NaN;
        this.fds_discretized = new_fds_discretized.clone();
        
        int[][] new_icds_discretized = new int[dimensions[0]][dimensions[1]];
        for (int j=0;j< this.icds_discretized.length; j++)
            System.arraycopy( this.icds_discretized[j], 0, new_icds_discretized[j], 0, dimensions[1]);
        for (int i=0; i<this.dimensions[1];i++)
            new_icds_discretized[dimensions[0]-1][i] = Integer.MIN_VALUE;
        this.icds_discretized = new_icds_discretized.clone();
    }
    
    public void deleteLastAtt(){
        dimensions[1]--;
        int[][] new_ids_discretized = new int[dimensions[0]][dimensions[1]];
        char[][] new_cds_discretized = new char[dimensions[0]][dimensions[1]];
        float[][] new_fds_discretized = new float[dimensions[0]][dimensions[1]];
        double[][] new_dds_discretized = new double[dimensions[0]][dimensions[1]];
        int[][] new_icds_discretized = new int[dimensions[0]][dimensions[1]];
       
        for (int j=0;j< this.ids_discretized.length; j++)
            System.arraycopy( this.ids_discretized[j], 0, new_ids_discretized[j], 0, dimensions[1]);
        
        for (int j=0;j< this.cds_discretized.length; j++)
            System.arraycopy( this.cds_discretized[j], 0, new_cds_discretized[j], 0, dimensions[1]);
        
        for (int j=0;j< this.fds_discretized.length; j++)
            System.arraycopy( this.fds_discretized[j], 0, new_fds_discretized[j], 0, dimensions[1]);
        
        for (int j=0;j< this.dds_discretized.length; j++)
            System.arraycopy( this.dds_discretized[j], 0, new_dds_discretized[j], 0, dimensions[1]);
        
        for (int j=0;j< this.icds_discretized.length; j++)
            System.arraycopy( this.icds_discretized[j], 0, new_icds_discretized[j], 0, dimensions[1]);
        
    }
    
    public void addValue(int instance, int attribute, int value, boolean isClass){
        this.ids_discretized[instance][attribute] = value;
        this.fds_discretized[instance][attribute] = (float) value;
        this.dds_discretized[instance][attribute] = (double) value;
        if(!isClass){
            this.cds_discretized[instance][attribute] = getLetterForNumber(value);
        } else {
            this.cds_discretized[instance][attribute] = (char) value;
        }
    }
    
    public void addValue(int instance, int attribute, double value){
//        System.out.println(this.dds_discretized.length+", "+this.dds_discretized[0].length);
        this.ids_discretized[instance][attribute] = (int) value;
        this.fds_discretized[instance][attribute] = (float) value;
        this.dds_discretized[instance][attribute] = (double) value;
        this.cds_discretized[instance][attribute] = (char) value;
    }
    
    public void addValue(int instance, int attribute, double value, boolean isClass){
        this.ids_discretized[instance][attribute] = (int) value;
        this.fds_discretized[instance][attribute] = (float) value;
        this.dds_discretized[instance][attribute] = (double) value;
        if(!isClass){
            this.cds_discretized[instance][attribute] = getLetterForNumber((int) value);
        } else {
            this.cds_discretized[instance][attribute] = (char) value;
        }
    }
    
    public void convert2ContinuosIntArray(List<WordCut> elements){
        for(int i=0;i<this.dimensions[0];i++){
            int num_alph = 0;
            icds_discretized[i][0] = num_alph+this.ids_discretized[i][0];
            icds_discretized[i][1] = num_alph+this.ids_discretized[i][1];
            for(int j=2;j<this.dimensions[1];j++){
                num_alph += elements.get(j-2).getAlphabet().getAlphabet().size();
                icds_discretized[i][j] = num_alph+this.ids_discretized[i][j];
            }
        }
    }
    
//    public void convert2CharArray(){
//        for(int i=0;i<this.dimensions[0];i++){
//            this.cds_discretized[i][0] = (char) this.ids_discretized[i][0]; //this.getDigitForNumber(this.ids_discretized[i][0]);
//            for(int j=1;j<this.dimensions[1];j++){
////                System.out.println(this.ids_discretized[i][j]);
//                cds_discretized[i][j] = this.getLetterForNumber(this.ids_discretized[i][j]);
//            }
//        }
//    }
    
    public void convert2StringArray(){
        for(int i=0;i<this.dimensions[0];i++){
            this.sds_discretized[i][0] = String.valueOf(this.ids_discretized[i][0]);
            sds_discretized[i][1] = new String(Arrays.copyOfRange(this.cds_discretized[i], 1, this.cds_discretized[i].length));
        }
    }
    
//    public void convert2StringArray(){
//        for(int i=0;i<this.dimensions[0];i++){
//            this.sds_discretized[i][0] = String.valueOf(this.ids_discretized[i][0]);
//            sds_discretized[i][1] = "";
//            for(int j=1;j<this.dimensions[1];j++){
//                sds_discretized[i][1] = sds_discretized[i][1]+getLetterForNumber(this.ids_discretized[i][j])+String.valueOf(j);
//            }
//        }
//    }
    
    public char getLetterForNumber(int i) {
//        System.out.println(i);
        char letter = 0;
//        List<Character> symbols = Utils.Utils.getListSymbols();
        try{
//            letter = i > 0 && i < 27 ? (char)(i + 96) : i > 26 && i < 53 ? (char)(i+38) : null;
            letter = TimeSeriesDiscretize_source.symbols.get(i);
        } catch(NullPointerException ex) {
            Logger.getLogger(DiscretizedDataSet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return letter;
    }
    
    public List<String> toStringList(){
        List<String> values = new ArrayList<>();
        for(int i=0;i<this.dimensions[0];i++){
            values.add(this.sds_discretized[i][1]);
        }
        return values;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.dimensions[0];i++){
            for(int j=0;j<this.dimensions[1];j++){
                sb.append(this.ids_discretized[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
//    public String PrintChars() {
//        StringBuilder sb = new StringBuilder();
//        for(int i=0;i<this.dimensions[0];i++){
//            for(int j=0;j<this.dimensions[1];j++){
//                sb.append(this.cds_discretized[i][j]).append(" ");
//            }
//            sb.append("\n");
//        }
//        return sb.toString();
//    }
    
    public String PrintIntContinuos() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.dimensions[0];i++){
            for(int j=0;j<this.dimensions[1];j++){
                sb.append(this.icds_discretized[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public String PrintFloats2csv() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.dimensions[0];i++){
            for(int j=0;j<this.dimensions[1];j++){
                sb.append(this.fds_discretized[i][j]).append(",");
            }
            sb.deleteCharAt(this.dimensions[1]-1);
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public String PrintStrings() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.dimensions[0];i++){
            for(int j=0;j<2;j++){
                sb.append(this.sds_discretized[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public String PrintStrings2JSON() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.dimensions[0];i++){
            sb.append("{").append("\"klass\"").append(":").append("\"").append(this.sds_discretized[i][0]).append("\"").append(",");
            sb.append("\"text\"").append(":").append("\"").append(this.sds_discretized[i][1]).append("\"").append("}");
            
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public String PrintIntContinuos2JSON() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.dimensions[0];i++){
            sb.append("{").append("\"klass\"").append(":").append("\"").append(this.icds_discretized[i][0]).append("\"").append(",");
            sb.append("\"text\"").append(":").append("\"");
            for(int j=1;j<this.dimensions[1];j++){
                sb.append(this.icds_discretized[i][j]).append(" ");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("\"").append("}").append("\n");
        }
        return sb.toString();
    }
    
    public int getMaxIntValue(){
        int max = Integer.MIN_VALUE;
        for(int i=0;i<this.dimensions[0];i++){
            for(int j=1;j<this.dimensions[1];j++){
                if(this.ids_discretized[i][j] > max){
                    max = this.ids_discretized[i][j];
                }
            }
        }
        return max;
    }
    
    public double[] getValues(int column){
        double[] values = new double[this.dimensions[0]];
        int j=0;
        for(int i=0;i<this.dimensions[0];i++){
            values[j] = this.dds_discretized[i][column];
            j++;
        }

        return values;
    }
    
    public double[] getValues(int instance, int begin, int end){
        double[] values = new double[end-begin];
        int j=0;
        for(int i=begin;i<end;i++){
            values[j] = this.dds_discretized[instance][i];
            j++;
        }

        return values;
    }
    
    @Override
    public DiscretizedData clone() {
        try {
            super.clone();
//            DiscretizedDataSet clon = new DiscretizedDataSet(this.getDimensions()[0], this.getDimensions()[1]);
            DiscretizedData clon = new DiscretizedData();
            clon.setCds_discretized(this.cds_discretized);
            clon.setDimensions(this.dimensions.clone());
            clon.setIds_discretized(this.ids_discretized.clone());
            clon.setSds_discretized(this.sds_discretized.clone());
            clon.setFds_discretized(this.fds_discretized.clone());
            clon.setDds_discretized(this.dds_discretized.clone());
            clon.setIcds_discretized(this.icds_discretized.clone());
            return clon;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DiscretizedDataSet.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void importData(double[][] data){
        for(int i=0; i<data.length; i++){
            for(int j=0;j<data[i].length;j++){
                boolean isclass = false;
                if(j==0) isclass = true;
                this.addValue(i, j, (int) Math.round(data[i][j]), isclass);
            }
        }
    }
    
    public void destroy(){
        this.cds_discretized = null;
        this.ids_discretized = null;
        this.sds_discretized = null;
        this.fds_discretized = null;
        this.dds_discretized = null;
        this.icds_discretized = null;
        this.dimensions = null;
    }
}
