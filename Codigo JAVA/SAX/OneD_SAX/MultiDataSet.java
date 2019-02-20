/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SAX.OneD_SAX;

/**
 *
 * @author amarquezgr
 */
public class MultiDataSet {
    private int[] klass;
    private Cell[][] data;
    private int[] dimensions;

    public MultiDataSet() {
        this.dimensions = new int[2];
    }

    public MultiDataSet(int instances, int attributes, int nelements) {
        this.dimensions = new int[2];
        this.dimensions[0] = instances;
        this.dimensions[1] = attributes;
        data = new Cell[instances][attributes];
        klass = new int[instances];
        for(int f=0;f<instances;f++){
            klass[f] = 0;
            for(int c=0; c<attributes;c++){
                data[f][c] = new Cell(nelements);
            }
        }
    }
    
    public void add(int instance, int attribute, int position, int value){
        data[instance][attribute].add(value, position);
    }
    
    public void addClass(int position, int klass){
        this.klass[position] = klass;
    }
    
    public int[] getCell(int instance, int attribute){
        return data[instance][attribute].getCell();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int f=0; f<this.dimensions[0];f++){
            for(int c=0; c<this.dimensions[1];c++){
                sb.append(data[f][c].toString()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    
    
}
