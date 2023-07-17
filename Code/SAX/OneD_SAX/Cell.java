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
public class Cell {
    private int[] cell;

    public int[] getCell() {
        return cell;
    }
    
    public Cell(int nelements) {
        cell = new int[nelements];
        for(int i=0; i<nelements;i++) cell[i] = 0;
    }

    public Cell(int[] cell) {
        this.cell = cell;
    }
    
    public void add(int element, int position){
        cell[position] = element;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i=0; i< cell.length;i++){
            sb.append(cell[i]).append(",");
        }
        sb.deleteCharAt(sb.length()-1).append("]");
        return sb.toString();
    }
    
    
}
