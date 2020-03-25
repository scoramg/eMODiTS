/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Results;

import DataSets.DataSet;
import Exceptions.MyException;
//import Individuals.PEVOMO.Scheme;
import Interfaces.IScheme;
import RAW.RAW;
import SAX.ESAX.ESAX;
import SAX.ESAXKMeans.ESAXKMeans;
import SAX.OneD_SAX.OneD_SAX;
import SAX.RKmeans.RKMeans;
import SAX.SAX;
import SAX.SAXKMeans.SAXKMeans;
import SAX.aSAX.aSAX;
import SAX.rSAX.rSAX;
import TD4C.CosineDistance;
import TD4C.EntropyDistance;
import TD4C.KullbackLeiblerDistance;
import TD4C.TD4C;
import TimeSeriesDiscretize.TimeSeriesDiscretize_source;
import Utils.Utils;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class Export {
    private final List<ApproachesCollection> table;
    private double[][] summaryRanks, summaryTest;
    private  String[] approachesName, Location, TypeSelection;
    private final int NoExecutions, ClassificationType;
    private int iAppBase;
    private IScheme EP;

    public Export(String[] approachesName, int iAppBase, int NoExecutions, String[] Location, String[] TypeSelection, int ClassificationType) {
        this.approachesName = approachesName.clone();
        this.NoExecutions = NoExecutions;
        this.Location = Location.clone();
        this.TypeSelection = TypeSelection.clone();
        table = new ArrayList<>();
        this.iAppBase = iAppBase;
        this.ClassificationType = ClassificationType;
    }
    
    public Export( int iAppBase, int NoExecutions, int ClassificationType) {
        this.NoExecutions = NoExecutions;
        table = new ArrayList<>();
        this.iAppBase = iAppBase;
        this.ClassificationType = ClassificationType;
    }
    
    public void initializeSummary(){
        for(int f=0; f<summaryRanks.length;f++){
            for(int c=0;c<summaryRanks[f].length;c++){ 
                summaryRanks[f][c] = 0;
            }
        }
        
        for(int f=0; f<summaryTest.length-1;f++){
            for(int c=0;c<3;c++){ //0-wins, 1-lose, 2-no diff
                this.summaryTest[f][c] = 0;
            }
        }
    }
    
    public void addRow(DataSet ds, String test) throws IOException, Exception{
        ApproachesCollection ac = new ApproachesCollection(ds.getIndex());
        Methods method = new Methods(this.ClassificationType);
        for(int l=0; l<this.Location.length; l++){
            method.setLocation(this.Location[l]);
//            System.out.println(this.Location[l]);
            EP = method.GetBestEP(this.NoExecutions, ds);
            System.out.println(ds.getName()+","+EP.getNumberWordCuts() + "," + EP.getNumberAlphabetCuts());
            for(int t=0; t<this.TypeSelection.length;t++){
                method.setType_selection(this.TypeSelection[t]);
                for(int i=0; i<this.approachesName.length;i++){
                    boolean isBase = false;
                    String ApproachName = this.approachesName[i]+"_"+TypeSelection[t]+"_"+this.Location[l];
                    if((i+l+t)==iAppBase) isBase = true;
                    switch(this.approachesName[i]){
                        case "MODiTS":
                            IScheme modits = method.getBestMODiTS(ds, 0);
                            ac.addApproach(new Approach(modits.getErrorRate(), modits.getErrorRatesByFolds(), ApproachName, isBase, modits.getCorrectPredictions()));
                            break;
                        case "Proposal Multiobjective":
                            IScheme proposal = method.getBestMODiTS(ds, 1);
                            ac.addApproach(new Approach(proposal.getErrorRate(), proposal.getErrorRatesByFolds(), ApproachName, isBase, proposal.getCorrectPredictions()));
                            break;
                        case "EP":
                            ac.addApproach(new Approach(EP.getErrorRate(), EP.getErrorRatesByFolds(), ApproachName, isBase,EP.getCorrectPredictions()));
                            break;
                        case "SAX":
                            SAX sax = method.GetBestSAX(ds, EP);
                            ac.addApproach(new Approach(sax.getErrorRate(), sax.getErrorRatesByFolds(), ApproachName, isBase, sax.getCorrectPredictions()));
                            break;
                        case "aSAX":
                            aSAX asax = method.GetBestAlphaSAX(ds, EP);
                            ac.addApproach(new Approach(asax.getErrorRate(), asax.getErrorRatesByFolds(), ApproachName, isBase, asax.getCorrectPredictions()));
                            break;
                        case "EPMO":
                            IScheme epmo = method.getBestEPMO(ds);
                            ac.addApproach(new Approach(epmo.getErrorRate(), epmo.getErrorRatesByFolds(), ApproachName, isBase, epmo.getCorrectPredictions()));
                            break;
                        case "ESAX":
                            ESAX esax = method.GetBestESAX(ds, EP);
                            ac.addApproach(new Approach(esax.getErrorRate(), esax.getErrorRatesByFolds(), ApproachName, isBase, esax.getCorrectPredictions()));
                            break; 
                        case "ESAXKMeans":
                            ESAXKMeans esaxkmeans = method.GetBestESAXKMeans(ds, EP);
                            ac.addApproach(new Approach(esaxkmeans.getErrorRate(), esaxkmeans.getErrorRatesByFolds(), ApproachName, isBase,esaxkmeans.getCorrectPredictions()));
                            break;    
                        case "1D-SAX":
                            OneD_SAX onedsax = method.GetBest1dSAX(ds, EP);
                            ac.addApproach(new Approach(onedsax.getErrorRate(), onedsax.getErrorRatesByFolds(), ApproachName, isBase, onedsax.getCorrectPredictions()));
                            break;  
                        case "RKMeans":
                            RKMeans rkmeans = method.GetBestRKMeans(ds, EP);
                            ac.addApproach(new Approach(rkmeans.getErrorRate(), rkmeans.getErrorRatesByFolds(), ApproachName, isBase,rkmeans.getCorrectPredictions()));
                            break; 
                        case "SAXKMeans":
                            SAXKMeans saxkmeans = method.GetBestSAXKMeans(ds, EP);
                            ac.addApproach(new Approach(saxkmeans.getErrorRate(), saxkmeans.getErrorRatesByFolds(), ApproachName, isBase,saxkmeans.getCorrectPredictions()));
                            break;    
                        case "rSAX":
                            rSAX rsax = method.GetBestRSAX(ds, EP);
                            ac.addApproach(new Approach(rsax.getErrorRate(), rsax.getErrorRatesByFolds(), ApproachName, isBase, rsax.getCorrectPredictions()));
                            break;
                        case "TD4C_Ent":
                            TD4C td4c_ent = method.GetBestTD4C(ds, new EntropyDistance());
                            ac.addApproach(new Approach(td4c_ent.getErrorRate(), td4c_ent.getErrorRatesByFolds(), ApproachName, isBase, td4c_ent.getCorrectPredictions()));
                            break;
                        case "TD4C_Cos":
                            TD4C td4c_cos = method.GetBestTD4C(ds, new CosineDistance());
                            ac.addApproach(new Approach(td4c_cos.getErrorRate(), td4c_cos.getErrorRatesByFolds(), ApproachName, isBase,td4c_cos.getCorrectPredictions()));
                            break;
                        case "TD4C_KB":
                            TD4C td4c_kb = method.GetBestTD4C(ds, new KullbackLeiblerDistance());
                            ac.addApproach(new Approach(td4c_kb.getErrorRate(), td4c_kb.getErrorRatesByFolds(), ApproachName, isBase, td4c_kb.getCorrectPredictions()));
                            break;    
                        case "RAW":
                            RAW raw = method.GetRAWClassification(ds);
                            ac.addApproach(new Approach(raw.getErrorRate(), raw.getErrorRatesByFolds(), ApproachName, isBase, raw.getCorrectPredictions()));
                            break;    
                            
                    }
                }
            }
        }
        ac.setRanks();
        ac.CalculateStats(test);
        this.table.add(ac);
        
    }
    
    public void ExecuteSummaries(){
        summaryRanks = new double[this.table.get(0).getApproachesName().length][this.table.get(0).getApproachesName().length];
        summaryTest = new double[this.table.get(0).getApproachesName().length][3];
        
        initializeSummary();
        
        for (ApproachesCollection ac: this.table){
            for(int i=0; i<ac.getApproaches().size(); i++){
                summaryRanks[i][ac.getApproaches().get(i).getRank()-1]++;
                if(ac.getApproaches().get(i).getH0().equals("+")){
                    summaryTest[i][0]++;
                }
                if(ac.getApproaches().get(i).getH0().equals("-")){
                    summaryTest[i][1]++;
                }
                if(ac.getApproaches().get(i).getH0().equals("=")){
                    summaryTest[i][2]++;
                }
            }
        }
    }
    
    public static String ordinal(int i) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
        case 11:
        case 12:
        case 13:
            return i + "th";
        default:
            return i + sufixes[i % 10];

        }
    }
    
    public void ExportSummaries(String SummaryRankFileName, String SummaryStatsFileName) throws IOException{
        ExecuteSummaries();
        StringBuilder csvranks = new StringBuilder();
        csvranks.append("Approach").append(",");
        for(int i=0; i<this.table.get(0).getApproachesName().length;i++){
            csvranks.append(ordinal(i+1)).append(",");
        }
        csvranks.deleteCharAt(csvranks.length()-1).append("\n");
        
        for(int c=0;c<summaryRanks.length;c++){
            csvranks.append(this.table.get(0).getApproachesName()[c]).append(",");
            for(int f=0;f<summaryRanks[c].length;f++){
                csvranks.append(summaryRanks[c][f]).append(",");
            }
            csvranks.deleteCharAt(csvranks.length()-1).append("\n");
        }
        
        System.out.println(csvranks.toString());
        try (BufferedWriter br = new BufferedWriter(new FileWriter(SummaryRankFileName))) {
            br.write(csvranks.toString());
        }
        
        StringBuilder csvtests = new StringBuilder();
        csvtests.append(this.table.get(0).getApproachesName()[iAppBase]).append(" vs ").append(",").append("Wins").append(",").append("Loses").append(",").append("No Diff").append("\n");

        for(int f=0; f<this.table.get(0).getApproachesName().length;f++){
            if(f!=iAppBase){
                csvtests.append(this.table.get(0).getApproachesName()[f]).append(",");
                for(int c=0;c<3;c++){ //0-wins, 1-lose, 2-ties
                    csvtests.append(summaryTest[f][c]).append(",");
                }
            }
            csvtests.deleteCharAt(csvtests.length()-1).append("\n");
        }
        System.out.println(csvtests.toString());
        try (BufferedWriter br = new BufferedWriter(new FileWriter(SummaryStatsFileName))) {
            br.write(csvtests.toString());
        }
    }
    
    public void ExportCSVTable(String FileName) throws IOException{
        StringBuilder csvtable = new StringBuilder();
        csvtable.append("Dataset").append(",");
        for(int i=0; i<this.table.get(0).getApproachesName().length;i++){
            csvtable.append(this.table.get(0).getApproachesName()[i]).append(",").append("SD").append(",").append("Rank").append(",");
            if (i != iAppBase){
                csvtable.append("pValue").append(",").append("h0").append(",");
            }
        }
        csvtable.deleteCharAt(csvtable.length()-1).append("\n");

        this.table.forEach((ac) -> {
            System.out.println(ac.toString());
            csvtable.append(ac.toString());
        });
        System.out.println(csvtable.toString());
        try (BufferedWriter br = new BufferedWriter(new FileWriter(FileName))) {
            br.write(csvtable.toString());
        }
    }
    
    public void execute(String FileNameTable, String SummaryRankFileName, String SummaryStatsFileName, String test) throws MyException, IOException, Exception{
//        int i=10;
        for (int i = 1; i < DataSet.NUMBER_OF_DATASETS; i++) {
            if (!DataSet.DATASETS_IGNORED.contains(i)) {
                DataSet ds = new DataSet(i, false);
                addRow(ds, test);
                ds.destroy();
            }
        }
        
        ExportCSVTable(FileNameTable);
        ExportSummaries(SummaryRankFileName, SummaryStatsFileName);
    }
    
    /*
    ConfigurationType: 1 => Selection Methods, 2=> Approaches
    ClassificationType: 1 => CV on original, 2 => CV on test, 3 => Train-Test without test subdivision, 4 => Train-Test with test subdivision
    test="wilcoxon" or "mcnemar"
    */
    
    public void ExecuteConfigurations(int configurationType, String test){
//        String[] Location = {"e15p100g300"};
        this.Location = new String[]{"/e15p100g300/"};
//        String[] type_selection={}, approaches={};
        switch(configurationType){
            case 1: // Selection methods
                this.TypeSelection = new String[]{"Train-CV","Knee","Mean","KMeans"};
                this.approachesName = new String[]{"MODiTS"};
                break;
            case 2:
                this.TypeSelection = new String[]{"Train-CV"};
                this.approachesName = new String[]{"MODiTS", "EP", "SAX", "aSAX", "ESAX", "ESAXKMeans", "1D-SAX","RKMeans","SAXKMeans","rSAX","TD4C_Cos","RAW"};
                break;
            case 3:
                this.TypeSelection = new String[]{"KMeans"};
                this.approachesName = new String[]{"MODiTS", "EP", "SAX", "aSAX", "ESAX", "ESAXKMeans", "1D-SAX","RKMeans","SAXKMeans","rSAX","TD4C_Cos","RAW"};
                break;    
        }
        
//        Export exp = new Export(approaches, 0, 15, Location, type_selection, ClassificationType);
        try {
            String base = "ResultsMODiTSConf"+configurationType+"Class"+ClassificationType+test;
            execute(base+"_App.csv", base+"_Ranks.csv", base+"_Tests.csv", test);
        } catch (IOException ex) {
            Logger.getLogger(Export.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Export.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) throws IOException, Exception {

        TimeSeriesDiscretize_source.symbols = Utils.getListSymbols();
        int ConfigurationType = 1; // 1 => Selection Methods, 2=> Approaches
        int ClassificationType = 1; //1 CV on original, 2 CV on test, 3 Train-Test without test subdivision, 4 Train-Test with test subdivision
        String test = "wilcoxon"; //test="wilcoxon" or "mcnemar"
        Export exp = new Export(0, 15, ClassificationType);
        exp.ExecuteConfigurations(ConfigurationType, test);
    }
    // TT Train y test con los datos raw normales
    // TT2 Es Train y test con los datos raw suavizado
    // TT3 significa Train and test con test subdividido para la comparación a pares. datos raw no suavizados
}
