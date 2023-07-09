/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testArena.tuto_csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.FilerException;

/**
 *
 * @author Loo Alex
 */
public class CSV_Reader {

   public void readCSV (){
       //using [ SacramentocrimeJanuary2006.csv ] which uses comma as seperator, not all csv use same "regex seperator"
        String path = "C:/Alex/Docs1/1_Codings/FYP_OptimizationInFreightTransportation/src/testArena/tuto_csv/[testCSV]DataSource/SacramentocrimeJanuary2006.csv";
        String line =""; //used to read each lines in csv
         int count =0;
        //now we gonna use a BufferReader which is faster and more efficient than a Scanner;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while((line = br.readLine()) != null){
                //reading line by line, if the current we are reading is not null
                count = count+1;
                String[] values = line.split(","); // a 1D array, only rows
                System.out.println("Date:" + values[0] +", Crime Description: " + values[5]); //values[0] means current data in column 1 and values[5] means display current data in 5th column
                //using this system we can only access data per row, at a time in the csv.
                if(count == 2)
                    break;
                
                //the split function seperate all data by the comma and store them in their respective rows, if there are 4 commas
                //expect 5 rows 0,1,2,3,4 in the string array, 5th row is for the last string after last comma.
                //data found at each line are store in their seperate 
            }
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            Logger.getLogger(CSV_Reader.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(CSV_Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    
   }
    
}
    