/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Results;

import DataSets.DataSet;

/**
 *
 * @author amarquezgr
 */
public class ApendicesLatex {
    
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
//        int i = 10;
        
        for(int i=1; i<DataSet.NUMBER_OF_DATASETS;i++){
            if(!DataSet.DATASETS_IGNORED.contains(i)){
                String bd = DataSet.getUCRRepository(i);
//                sb.append("\\section{").append(bd).append("} \\label{sec:﻿apend_tree:").append(bd).append("}").append("\n");
//                sb.append("\\begin{figure}[H]").append("\n").append("\\includegraphics[width=\\textwidth]{Figuras/Apendices/Arboles/imgs/").append(bd).append("_ArbolBest_CTV}").append("\n");
//                sb.append("\\caption{Árbol de decisión obtenido por eMODiTS para la base de datos ").append(bd).append(".}").append("\n");
//                sb.append("\\label{fig_").append(bd).append("_arbol}").append("\n");
//                sb.append("\\end{figure}").append("\n\n");
                sb.append("\\section{").append(bd).append("} \\label{sec:apen_intr:").append(bd).append("}").append("\n");
                sb.append("\\begin{figure}[H]").append("\n").append("\\includegraphics[width=\\textwidth]{Figuras/Apendices/Interpretacion/imgs/").append(bd).append("_MODiTS_GraphicScheme_Distr}").append("\n");
                sb.append("\\caption{Distribución de las clases para la base de datos ").append(bd).append(" extraída del árbol obtenido por eMODiTS. Cada segmento en el eje del tiempo representa un nodo interno ($x_i$) del árbol y cada rectángulo de color representa un nodo hoja.}").append("\n");
                sb.append("\\label{fig_").append(bd).append("_distr}").append("\n");
                sb.append("\\end{figure}").append("\n\n");
            }
        }
        System.out.println(sb.toString());
//        \section{Adiac} \label{sec:adiac}
//\begin{figure}[!h]
//	\includegraphics[width=\textwidth]{Figuras/Apendices/Arboles/imgs/Adiac_MODiTS_GraphicScheme_Distr}
//	\caption{Árbol de decisión obtenido por eMODiTS para la base de datos Adiac.}
//	\label{fig_adiac_arbol}
//\end{figure} 
   }
}
