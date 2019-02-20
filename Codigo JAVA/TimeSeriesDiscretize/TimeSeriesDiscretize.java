/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeSeriesDiscretize;

import Exceptions.MyException;
import DataSets.DataSet;
import Interfaces.IScheme;
import Populations.MOPopulation;
import Populations.Population;
import ParetoFront.ParetoFront;
import ca.nengo.io.MatlabExporter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import methods.multiobjective.NSGA2;
import parameters.Generals;
import parameters.Locals;

/**
 *
 * @author amarquezgr
 */
public class TimeSeriesDiscretize extends javax.swing.JFrame implements Runnable {

    /**
     * Creates new form TimeSeriesDiscretize
     */
    Generals general_params;
    
    public TimeSeriesDiscretize() {
        initComponents();
        general_params = new Generals();
        jPFF.setVisible(false);
        for(int i=0;i<=DataSet.NUMBER_OF_DATASETS;i++){
            jCBDataset.addItem(DataSet.getUCRRepository(i));
        }
        jTFAlpha.setEnabled(true);
        jTFBeta.setEnabled(true);
        jTFGamma.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bGMutatortype = new javax.swing.ButtonGroup();
        bGDiscretizationType = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLblP = new javax.swing.JLabel();
        jTFPopulationSize = new javax.swing.JTextField();
        jLblE = new javax.swing.JLabel();
        jTFExecutions = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTFGenerations = new javax.swing.JTextField();
        jLblMR = new javax.swing.JLabel();
        jTFMutationRate = new javax.swing.JTextField();
        jLblOpponent = new javax.swing.JLabel();
        jTFOpponents = new javax.swing.JTextField();
        jLblMR1 = new javax.swing.JLabel();
        jTFCrossoverRate = new javax.swing.JTextField();
        jLblAlgorithm = new javax.swing.JLabel();
        jCBAlgorithm = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLblFF = new javax.swing.JLabel();
        jCBFitnessFunction = new javax.swing.JComboBox();
        jPFF = new javax.swing.JPanel();
        jChBSelfAdaptation = new javax.swing.JCheckBox();
        jLblAlpha = new javax.swing.JLabel();
        jTFAlpha = new javax.swing.JTextField();
        jLblBeta = new javax.swing.JLabel();
        jTFBeta = new javax.swing.JTextField();
        jTFGamma = new javax.swing.JTextField();
        jLblGamma = new javax.swing.JLabel();
        btnIniciar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        pbBarraEstado = new javax.swing.JProgressBar();
        jPanel4 = new javax.swing.JPanel();
        jCBApproach = new javax.swing.JComboBox();
        lblNotas = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jCBDataset = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Evolutionary Algorithm"));

        jLblP.setText("Population Size:");

        jTFPopulationSize.setText("50");

        jLblE.setText("Number of Executions:");

        jTFExecutions.setText("5");
        jTFExecutions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFExecutionsActionPerformed(evt);
            }
        });

        jLabel1.setText("Number of Generations:");

        jTFGenerations.setText("250");

        jLblMR.setText("Mutation rate:");

        jTFMutationRate.setText("0.2");

        jLblOpponent.setText("Opponents:");

        jTFOpponents.setText("0.1");

        jLblMR1.setText("Crossover Rate:");

        jTFCrossoverRate.setText("0.8");
        jTFCrossoverRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCrossoverRateActionPerformed(evt);
            }
        });

        jLblAlgorithm.setText("Algorithm:");

        jCBAlgorithm.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select one...", "Evolutionary Programming", "Genetic Algorithm", "NSGA II" }));
        jCBAlgorithm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBAlgorithmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLblE)
                            .addComponent(jLblP)
                            .addComponent(jLabel1)
                            .addComponent(jLblMR)
                            .addComponent(jLblOpponent)
                            .addComponent(jLblMR1))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTFCrossoverRate)
                            .addComponent(jTFMutationRate)
                            .addComponent(jTFPopulationSize, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(jTFExecutions)
                            .addComponent(jTFGenerations)
                            .addComponent(jTFOpponents)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLblAlgorithm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBAlgorithm, 0, 1, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblP)
                    .addComponent(jTFPopulationSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblE)
                    .addComponent(jTFExecutions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTFGenerations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFMutationRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblMR))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFCrossoverRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblMR1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFOpponents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLblOpponent))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblAlgorithm)
                    .addComponent(jCBAlgorithm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Evaluator"));

        jLblFF.setText("Fitness Function:");

        jCBFitnessFunction.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select one...", "RechyFunction", "TreeDecision", "rfEntropyComplexity", "rfEntropyCompression", "rfComplexityCompression" }));
        jCBFitnessFunction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBFitnessFunctionActionPerformed(evt);
            }
        });

        jChBSelfAdaptation.setText("Self-adaptation");
        jChBSelfAdaptation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChBSelfAdaptationActionPerformed(evt);
            }
        });

        jLblAlpha.setText("Alpha:");

        jTFAlpha.setText("0");

        jLblBeta.setText("Beta:");

        jTFBeta.setText("0");

        jTFGamma.setText("0");

        jLblGamma.setText("Gamma:");

        javax.swing.GroupLayout jPFFLayout = new javax.swing.GroupLayout(jPFF);
        jPFF.setLayout(jPFFLayout);
        jPFFLayout.setHorizontalGroup(
            jPFFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPFFLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPFFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPFFLayout.createSequentialGroup()
                        .addComponent(jLblGamma)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFGamma))
                    .addGroup(jPFFLayout.createSequentialGroup()
                        .addComponent(jChBSelfAdaptation)
                        .addGap(0, 180, Short.MAX_VALUE))
                    .addGroup(jPFFLayout.createSequentialGroup()
                        .addGroup(jPFFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLblAlpha)
                            .addComponent(jLblBeta))
                        .addGap(18, 18, 18)
                        .addGroup(jPFFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFAlpha)
                            .addComponent(jTFBeta))))
                .addContainerGap())
        );
        jPFFLayout.setVerticalGroup(
            jPFFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPFFLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jChBSelfAdaptation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPFFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblAlpha)
                    .addComponent(jTFAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPFFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblBeta)
                    .addComponent(jTFBeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPFFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblGamma)
                    .addComponent(jTFGamma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jCBFitnessFunction, 0, 334, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPFF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLblFF)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLblFF)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBFitnessFunction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPFF, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnIniciar.setText("Iniciar");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pbBarraEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pbBarraEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Approach"));

        jCBApproach.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PEVOMO", "Proposal" }));
        jCBApproach.setSelectedIndex(1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jCBApproach, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCBApproach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("DataSet"));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCBDataset, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCBDataset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnIniciar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNotas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(btnIniciar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNotas, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        // TODO add your handling code here:
        pbBarraEstado.setValue(0);
        Thread Ejecutar = new Thread(this);
        Ejecutar.start();

    }//GEN-LAST:event_btnIniciarActionPerformed

    private void jCBFitnessFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBFitnessFunctionActionPerformed
        // TODO add your handling code here:
//        jPFF.setVisible(false);
        if (jCBFitnessFunction.getSelectedIndex() == 1 && jCBAlgorithm.getSelectedIndex() != 3){
            jPFF.setVisible(true);
        } else {
            jPFF.setVisible(false);
        }
        
    }//GEN-LAST:event_jCBFitnessFunctionActionPerformed

    private void jChBSelfAdaptationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBSelfAdaptationActionPerformed
        jTFAlpha.setEnabled(true);
        jTFBeta.setEnabled(true);
        jTFGamma.setEnabled(true);
        
        if(jChBSelfAdaptation.isSelected()){
            jTFAlpha.setEnabled(false);
            jTFBeta.setEnabled(false);
            jTFGamma.setEnabled(false);
        }
    }//GEN-LAST:event_jChBSelfAdaptationActionPerformed

    private void jTFCrossoverRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCrossoverRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCrossoverRateActionPerformed

    private void jCBAlgorithmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBAlgorithmActionPerformed
        // TODO add your handling code here:
        if(jCBAlgorithm.getSelectedIndex() == 3){
//            jCBFitnessFunction.setSelectedIndex(1);
//            jCBFitnessFunction.setEnabled(false);
            jPFF.setVisible(false);
//            jCBApproach.setSelectedIndex(1);
//            jCBApproach.setEnabled(false);
        } else {
//            jCBFitnessFunction.setSelectedIndex(0);
//            jCBFitnessFunction.setEnabled(true);
            jPFF.setVisible(true);
//            jCBApproach.setSelectedIndex(1);
//            jCBApproach.setEnabled(true);
        }
    }//GEN-LAST:event_jCBAlgorithmActionPerformed

    private void jTFExecutionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFExecutionsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFExecutionsActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TimeSeriesDiscretize.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TimeSeriesDiscretize.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TimeSeriesDiscretize.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TimeSeriesDiscretize.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TimeSeriesDiscretize().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bGDiscretizationType;
    private javax.swing.ButtonGroup bGMutatortype;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JComboBox jCBAlgorithm;
    private javax.swing.JComboBox jCBApproach;
    private javax.swing.JComboBox jCBDataset;
    private javax.swing.JComboBox jCBFitnessFunction;
    private javax.swing.JCheckBox jChBSelfAdaptation;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLblAlgorithm;
    private javax.swing.JLabel jLblAlpha;
    private javax.swing.JLabel jLblBeta;
    private javax.swing.JLabel jLblE;
    private javax.swing.JLabel jLblFF;
    private javax.swing.JLabel jLblGamma;
    private javax.swing.JLabel jLblMR;
    private javax.swing.JLabel jLblMR1;
    private javax.swing.JLabel jLblOpponent;
    private javax.swing.JLabel jLblP;
    private javax.swing.JPanel jPFF;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jTFAlpha;
    private javax.swing.JTextField jTFBeta;
    private javax.swing.JTextField jTFCrossoverRate;
    private javax.swing.JTextField jTFExecutions;
    private javax.swing.JTextField jTFGamma;
    private javax.swing.JTextField jTFGenerations;
    private javax.swing.JTextField jTFMutationRate;
    private javax.swing.JTextField jTFOpponents;
    private javax.swing.JTextField jTFPopulationSize;
    private javax.swing.JLabel lblNotas;
    private javax.swing.JProgressBar pbBarraEstado;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
//        int iFitnessFunction = jCBFF.getSelectedIndex();
        general_params.setiFitnessFunctionConf(jCBFitnessFunction.getSelectedIndex());
        
        general_params.setOpponents((int) Math.floor(Integer.parseInt(jTFPopulationSize.getText()) * Double.parseDouble(jTFOpponents.getText())));
        
        if (general_params.getiFitnessFunctionConf()>0){
        
            int iDS = jCBDataset.getSelectedIndex();

            int d=iDS, fultimo=iDS;
            if(iDS==0){
                d=1;
                fultimo=DataSet.NUMBER_OF_DATASETS;
            }
            
            for(int m=d;m<=fultimo;m++){
                try {
//                    DataSet ds = new DataSet(m, jTFDatasetDirectory.getText(), "_TRAIN");
                    DataSet ds = new DataSet(m);
                    double[] weights = new double[3];
                    if(!jChBSelfAdaptation.isSelected()){
                        weights[0] = Double.parseDouble(jTFAlpha.getText());
                        weights[1] = Double.parseDouble(jTFBeta.getText());
                        weights[2] = Double.parseDouble(jTFGamma.getText());
                        general_params.setWeights(weights);
                    }
                    
                    pbBarraEstado.setValue(0);
                    lblNotas.setText("Running database: "+DataSet.getUCRRepository(m));
                    
                    general_params.setiAlgorithm(jCBAlgorithm.getSelectedIndex());
                    general_params.setTotalTime(System.currentTimeMillis());
                    general_params.setnExecutions(Integer.parseInt(jTFExecutions.getText()));
                    general_params.setiApproach(jCBApproach.getSelectedIndex());
                    general_params.setnGenerations(Integer.parseInt(jTFGenerations.getText()));
                    general_params.setIsSelfAdaption(jChBSelfAdaptation.isSelected());
                    general_params.setMutationRate(Double.parseDouble(jTFMutationRate.getText()));
                    general_params.setCrossOverRate( Double.parseDouble(jTFCrossoverRate.getText()));
                    
                    switch(general_params.getiAlgorithm()){
                        case 1:
                        case 2:
                            Population population = new Population(Integer.parseInt(jTFPopulationSize.getText()), general_params.isIsSelfAdaption());
                            general_params.setPopulation(population);
                            general_params.setAccumulatedFront(new Population(0, general_params.isIsSelfAdaption()));
                            break;
                        case 3:
                            MOPopulation mopopulation = new MOPopulation(Integer.parseInt(jTFPopulationSize.getText()), general_params.isIsSelfAdaption());
                            general_params.setPopulation(mopopulation);
                            general_params.setAccumulatedFront(new MOPopulation(0, general_params.isIsSelfAdaption()));
                            break;    
                            
                    }
                    
                    for(int e=0; e<Integer.parseInt(jTFExecutions.getText());e++){
                        Locals local_params = new Locals(ds, e,general_params.getnGenerations());
                        TimeSeriesDiscretize_source tsd = new TimeSeriesDiscretize_source(general_params, local_params);
//                        Future<double[]> future = pool.submit(tsd);
//                        set.add(future);
                        Thread th = new Thread(tsd);
                        th.start();
                        th.join();
//                        tsd.setExecution(e);
//                        double[] datos = tsd.Execute();
//                        errorRates[e][0] = e;
//                        errorRates[e][1] = (float) datos[0];
//                        errorRates[e][2] = (float) datos[1];
                        pbBarraEstado.setValue(((e+1) * 100)/Integer.parseInt(jTFExecutions.getText()));
                    }
//                    for (Future<double[]> future : set) {
//                        double[] datos = future.get();
//                        errorRates[(int) datos[0]][0] = (float) datos[0];
//                        errorRates[(int) datos[0]][1] = (float) datos[1];
//                        errorRates[(int) datos[0]][2] = (float) datos[2];
//                    }
//                    String scheme_type = (String) jCBApproach.getSelectedItem();
                    String scheme_type = general_params.population.getIndividuals()[0].getName();
                    String directory = scheme_type+"/"+ds.getName();
                    String FileName =  ds.getName()+"_"+scheme_type;
                    this.Export(directory, FileName, ds);
//                    String FileNameAcc =  ds.getName()+"_"+scheme_type+"_AF";
//                    this.ExportAccumulatedFront(directory, FileNameAcc);
                    
                } catch (MyException | InterruptedException ex) {
                    Logger.getLogger(TimeSeriesDiscretize.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            File file1 = new File("temporal.csv");
            file1.delete();
            File file2 = new File("temporal.arff");
            file2.delete();
            lblNotas.setText("Finished");
        }
    }
    
    private void Export(String directory, String FileName, DataSet ds){
        try {
            MatlabExporter exporter = new MatlabExporter();
            general_params.CalculateStatistics();
            File FileDir = new File(directory);
            if(!FileDir.exists()) FileDir.mkdirs();
            
            if (general_params.getiAlgorithm() == 3){
                List<ParetoFront> fronts = NSGA2.FastNonDominatedSort(general_params.accumulatedFront);
                double[] reference = {0,0,0};
                IScheme best = fronts.get(0).getKnee(reference);
                exporter.add("AccumulatedFrontFitness", fronts.get(0).toFloatArray());
                fronts.get(0).Export(exporter);
                best.Export(ds,exporter);
            } else {
                IScheme best = general_params.accumulatedFront.getBestByErrorRate();
                best.Export(ds, exporter);
            }
            
            File FileTabla = new File(directory+"/"+FileName+".mat");
//            System.out.println(directory+"/"+FileName+".mat");
            general_params.setTotalTime(System.currentTimeMillis() - general_params.getTotalTime());
            exporter.add("errorRates", general_params.getMisclassification_rates());
//            exporter.add("statistics", general_params.getStatistics());
            float[][] time = new float[1][1];
            time[0][0] = general_params.getTotalTime();
            exporter.add("time", time);
            exporter.write(FileTabla);
        } catch (IOException ex) {
            Logger.getLogger(TimeSeriesDiscretize.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    private void ExportAccumulatedFront(String directory, String FileName){
//        try {
//            MatlabExporter exporter = new MatlabExporter();
//            List<ParetoFront> fronts = NSGA2.FastNonDominatedSort(general_params.accumulatedFront);
//            fronts.get(0).Export(exporter);
//            File FileTabla = new File(directory+"/"+FileName+".mat");
//            exporter.write(FileTabla);
//        } catch (IOException ex) {
//            Logger.getLogger(TimeSeriesDiscretize.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//    }
}
