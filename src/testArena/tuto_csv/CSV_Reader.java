/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testArena.tuto_csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.FilerException;

/**
 *
 * @author Loo Alex
 */
public class CSV_Reader {

   public static void main (String[] args){
        String path = "C:/Alex/Docs1/1_Codings/FYP_OptimizationInFreightTransportation/src/testArena/tuto_csv/[testCSV]DataSource/SacramentocrimeJanuary2006.csv";

        //now we gonna use a BufferReader which is faster and more efficient than a Scanner;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            Logger.getLogger(CSV_Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    
   }
    
}
    