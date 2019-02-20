/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import DataSets.Data;
import DataSets.DataSet;
import DataSets.DiscretizedData;
import DataSets.DiscretizedDataSet;
import DataSets.ReconstructedData;
import FitnessFunctions.FitnessFunction;
import Individuals.PEVOMO.Alphabet;
import Individuals.PEVOMO.Word;
import Individuals.Proposal.WordCut;
import ca.nengo.io.MatlabExporter;
import java.util.List;
import parameters.Generals;
import parameters.Locals;
import weka.classifiers.evaluation.Prediction;

/**
 *
 * @author amarquezgr
 */
public interface IScheme {
    
    public Word getWord();
    public Alphabet getAlphabet();
    public List<WordCut> getElements();
    public int getNumberWordCuts();
    public int getNumberAlphabetCuts();
    public void adjust(Data ds);
//    public void setDiscretization(DataSet ds);
    public DiscretizedData DiscretizeByPAA(Data ds);
//    public DiscretizedDataSet getDs_dis();
    public void evaluate(Data ds, double[] weights);
    public FitnessFunction getFitnessFunction();
    public void setFitnessFunction(FitnessFunction fitnes_function);
    public double getFitnessValue();
    public void Generate(Data ds, double[][] limits);
    public IScheme Mutate(double MutationRate, boolean isSelfAdaptation, Data ds, double[][] limits);
    public String getName();
    public IScheme clone();
    public int compareTo(IScheme o);
    public double[] toDoubleArray();
    public boolean isIsSelfAdaptation();
    public String PrintCuts();
    public List<Integer> getTotalCuts();
    public void ImportFromMatFile(String filename, int ds_length);
    public double[] getEvaluatedValues();
    public int getNoFunctions();
    public void setRank(int rank);
    public List<IScheme> getDominationSet();
    public int getDominatedCount();
    void setDominatedCount(int DominatedCount);
    public void setDominationSet(List<IScheme> DominationSet);
    public void IncreaseDominatedCount();
    public void DecreaseDominatedCount();
    public void setCrowdingDistance(double CrowdingDistance);
    public double getCrowdingDistance();
    public int getRank();
    public Object getElementAt(int index);
    public void empty();
    public void add(double[][] front);
    public void add(Object o);
    public void sort();
    public double[] getErrorRatesByFolds();
    public List<Prediction> getPredictions();
    public String Predictions2CSV();
    
    public double getErrorRate();
    public String getDecisionTreeGraph();
    public void Classify(DataSet dataset, boolean UsingTest, String set_type);
    
    public ReconstructedData Reconstruct(DiscretizedData ds_dis);
    
    public void ExportGraph(Locals local_params);
    public void ExportGraph(DataSet ds, String folder, String Location, String Selector);
    public void ExportStrings(DataSet ds, String Location);
    public void Export(Generals general_params, Locals local_params);
    public void Export(DataSet dataset, MatlabExporter exporter);
    public float[][] Elements2FloatArray();
    public void Export2JSON(DataSet ds, String Location);
    public void ExportErrorRates(DataSet ds, String folder, String Location, String type_selection);
    
}
