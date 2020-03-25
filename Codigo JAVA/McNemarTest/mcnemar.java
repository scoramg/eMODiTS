/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package McNemarTest;

import Exceptions.MyException;
import java.util.List;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.tests.countData.mcNemarTest.McNemarTest;

/**
 *
 * @author amarquezgr
 */
public class mcnemar {
    private DataTable contingencyTable;

    public DataTable getContingenceTable() {
        return contingencyTable;
    }

    public mcnemar(List<Integer> predictions1, List<Integer> predictions2) throws MyException {
        if(predictions1.size() != predictions2.size()){
            throw new MyException("The length of predictions are different");
        } else {
            ConstructContingencyTable(predictions1, predictions2);
        }
    }
    
    public void ConstructContingencyTable(List<Integer> predictions1, List<Integer> predictions2){

//			    Classifier2 Correct,	Classifier2 Incorrect
//Classifier1 Correct 	        Yes/Yes					Yes/No
//Classifier1 Incorrect 	No/Yes 					No/No

        double[][] contingence = new double[2][2];
        for(int i=0; i<contingence.length;i++){
            for(int j=0;j<contingence[0].length;j++){
                contingence[i][j] = 0;
            }
        }
        for(int i=0; i<predictions1.size();i++){
            if(predictions1.get(i) == 1 && predictions2.get(i) == 1) contingence[0][0]++;
            if(predictions1.get(i) == 1 && predictions2.get(i) == 0) contingence[0][1]++;
            if(predictions1.get(i) == 0 && predictions2.get(i) == 1) contingence[1][0]++;
            if(predictions1.get(i) == 0 && predictions2.get(i) == 0) contingence[1][1]++;
        }
        
        this.contingencyTable = new DataTable(contingence);
    }
    
    public double test(){
        McNemarTest test = new McNemarTest(this.contingencyTable);
        test.doTest();
        return test.getExactPValue();
    }
}
