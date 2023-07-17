/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amarquezgr
 */
public class TreeInterpeter {
    private List<classNode> TreeTable;

    public TreeInterpeter() {
        TreeTable = new ArrayList<>();
    }

    public List<classNode> getStructure() {
        return TreeTable;
    }

    public void setStructure(List<classNode> structure) {
        this.TreeTable = structure;
    }
    
    public void InsertNode(String node, String WordCut, String AlphabetCut, String klass){
        boolean exist = false;
        for (int i=0;i<this.TreeTable.size();i++){
            if (TreeTable.get(i).getWordcut().equals(WordCut) && TreeTable.get(i).getAlphabetcut().equals(AlphabetCut)){
                exist = true;
                if (!TreeTable.get(i).getKlass().equals(klass)){
                    TreeTable.get(i).addKlass(klass);
                }
            }
        }
        if (!exist){
            TreeTable.add(new classNode(node, WordCut, AlphabetCut, klass));
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (classNode cn:TreeTable){
            sb.append(cn.toString()).append("\n");
        }
        return sb.toString();
    }
    
    public void build(String ArbolFile){
        String[] arbolreader = ArbolFile.split("\n");
        
        HashMap<String,String> CutVar = new HashMap<>();
        HashMap<String,String[]> CutAlph = new HashMap<>();
        
        String[] str_formatted;
        
        for (String s: arbolreader){
//            String str_formatted=null;
            if (s.contains("\"x")){
                str_formatted = s.replace(" [label=\"", ",").replace("\" ]","").split(",");
//                System.out.println(str_formatted[1]);
                CutVar.put(str_formatted[0], str_formatted[1]);
            } else {
                if(s.contains("->")){
                    str_formatted = s.replace("->", ",").replace(" [label=\"= ", ",").replace("\"]","").split(",");
//                    String str = s.replace("->", ",").replace(" [label=\"= ", ",").replace("\"]","");
                    String[] vals = new String[2];
                    vals[0] = str_formatted[0]; //nodo padre que esta almacenado en cutvar.
                    vals[1] = str_formatted[2]; //Alphabet cut
                    CutAlph.put(str_formatted[1], vals);
                } else {
                    if(s.contains("shape")){
                        String str = s.replace(" [label=\"", ",").replace("shape=box style=filled ]","");
                        String to_replace = str.substring(str.indexOf("("));
                        str_formatted=str.replace(to_replace," ").split(",");
                        String NodoClass = str_formatted[0];
                        String Klass = str_formatted[1].trim();
                        String WordCut = "", AlphabetCut = "";
                        
                        if (CutAlph.containsKey(str_formatted[0])){
                            String[] vals = CutAlph.get(str_formatted[0]);
                            AlphabetCut = vals[1];
                            if(CutVar.containsKey(vals[0])){
                                WordCut = CutVar.get(vals[0]);
                            }
                        }
                        this.InsertNode(NodoClass, WordCut, AlphabetCut, Klass);
                    }
                }
            }
        }
    }
    
    public class classNode{
        private String nodeName;
        private String wordcut;
        private String alphabetcut;
        private SortedSet<String> klass;

        public classNode(String nodeName, String wordcut, String alphabetcut, String klass) {
            this.nodeName = nodeName;
            this.wordcut = wordcut;
            this.alphabetcut = alphabetcut;
            this.klass = new TreeSet<>();
            this.klass.add(klass);
        }

        public classNode(String wordcut, String alphabetcut, String klass) {
            this.wordcut = wordcut;
            this.alphabetcut = alphabetcut;
            this.klass.add(klass);
        }

        public classNode(String nodeName, String klass) {
            this.nodeName = nodeName;
            this.klass.add(klass);
        }
        
        public classNode() {
            this.nodeName = "";
            this.alphabetcut = "";
            this.wordcut = "";
            this.klass = new TreeSet<>();
        }

        public String getNodeName() {
            return nodeName;
        }

        public String getWordcut() {
            return wordcut;
        }

        public String getAlphabetcut() {
            return alphabetcut;
        }

        public SortedSet<String> getKlass() {
            return klass;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

        public void setWordcut(String wordcut) {
            this.wordcut = wordcut;
        }

        public void setAlphabetcut(String alphabetcut) {
            this.alphabetcut = alphabetcut;
        }

        public void addKlass(String newKlass){
            this.klass.add(newKlass);
        }

        private void empty() {
            this.alphabetcut = "";
            this.wordcut = "";
            this.klass = new TreeSet<>();
            this.nodeName = "";
        }

        @Override
        public String toString() {
//            return nodeName + "," + wordcut + "," + alphabetcut + "," + klass;
            StringBuilder sb = new StringBuilder();
            sb.append(nodeName).append(";").append(wordcut).append(";").append(alphabetcut).append(";").append(this.klass.toString());
//            for (String s: this.klass){
//                sb.append(s).append(",");
//            }
//            sb.deleteCharAt(sb.length()-1);
            return sb.toString();
        }
        
    }

    
    public static void main(String[] args) throws IOException {
        TreeInterpeter ti = new TreeInterpeter();
        File file = new File("e15p100g300/MODiTS/Beef/Trees/Arbol_best_CTV.txt");
        byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        String tree = new String(encoded, StandardCharsets.UTF_8);
        ti.build(tree);
        System.out.println(ti.toString());
    }
    
}
