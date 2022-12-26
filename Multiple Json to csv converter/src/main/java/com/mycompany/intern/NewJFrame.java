/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.intern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;

import com.mycompany.intern.CsvParser;
import com.mycompany.intern.CsvVo;


/**
 *
 * @author snkgs
 */
public class NewJFrame extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
    }
    
    //Function to extract names of all the files from a given path
    public ArrayList<String> listFiles(String dir) throws IOException {
    ArrayList<String> fileSet = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
        for (Path path : stream) {
            if (!Files.isDirectory(path)) {
                fileSet.add(path.getFileName()
                    .toString());
            }
        }
    }
    return fileSet;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        location = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToggleButton1.setText("Change to CSV");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Enter Folder Location");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(jToggleButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(location, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jLabel1)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(41, 41, 41)
                .addComponent(location, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:

        try {
            String loc = location.getText();
            
            //text file to created to write errors
            File file = new File(loc + "\\" + "error.txt");

            ArrayList<String> filelocs = listFiles(loc);
            ArrayList<String> csvlocs = new ArrayList<>();
            
            //iterate through each file extracting its data into a CSV file
            for(int i=0; i<filelocs.size(); i++){
                try{
                    
                    if(!file.exists()){
                        file.createNewFile();
                    }

                    if(!filelocs.get(i).contains(".json")){
                        continue;
                    }
                    
                    JsonNode jsonTree = new ObjectMapper().readTree(new File(loc + "\\" + filelocs.get(i)));
                    Builder csvSchemaBuilder = CsvSchema.builder();
                    JsonNode firstObject = jsonTree.elements().next();
                    firstObject.fieldNames().forEachRemaining(fieldName ->
                            {csvSchemaBuilder.addColumn(fieldName);});

                
                    CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
                    CsvMapper csvMapper = new CsvMapper();
                    String a = filelocs.get(i).replace(".json", ".csv");
                    csvMapper.writerFor(JsonNode.class)
                            .with(csvSchema)
                            .writeValue(new File(loc + "\\" + a), jsonTree); 
                    
                }catch(Exception e){
                    
                    //code that catches the exceptions and write them into the error.txt file
                    FileWriter pw = new FileWriter(file, true);
                    pw.write(filelocs.get(i));
                    pw.write("\n");
                    pw.write(e.toString());
                    pw.write("\n");pw.write("\n");
                    pw.close();
                }
            }
            
            //extracting the names of all the csv files
            filelocs = listFiles(loc);
            for(int j=0; j<filelocs.size(); j++){
                if(filelocs.get(j).contains(".csv")){
                    csvlocs.add(filelocs.get(j));
                }
            }
            
            //extracting headers of the csv file
            File csv1 = new File(loc + "\\" + csvlocs.get(0));
            List<String> allCsvHeaders = CsvParser.getHeadersFromACsv(csv1);
            List<CsvVo> allCsvRecords = new ArrayList<>();
            Set<String> uniqueHeaders = new HashSet<>(allCsvHeaders);
            File a;
            
            //list to store all records from all the CSV files
            List<CsvVo> records;
            
            //adding all the data in each CSV file into a List
            for(int i=0; i<csvlocs.size(); i++){
                a = new File(loc + "\\" + csvlocs.get(i));
                records = CsvParser.getRecodrsFromACsv(a, allCsvHeaders);
                allCsvRecords.addAll(records);   
            }
            
            //adding the data extracted into the list into result.csv file
            CsvParser.writeToCsv(new File(loc + "\\" + "result.csv"), uniqueHeaders, allCsvRecords);
 
        } catch (Exception ex) {
            
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextField location;
    // End of variables declaration//GEN-END:variables
}