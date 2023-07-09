/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import testArena.tuto_csv.CSV_Reader;

/**
 *
 * @author Loo Alex
 * Contains all general and useful functions
 */
public final class  CoreFunctions {
    private CoreFunctions(){
        throw new IllegalAccessError("Instanciation not allow");
    }
    
    //notused
    public static void readCSV(String FilePath, String seperator){
        
        if(seperator.toLowerCase().equals("ws")){
            seperator = "\\s+";
        }
        
        //FilePath = "C:/Alex/Docs1/1_Codings/FYP_OptimizationInFreightTransportation/src/testArena/tuto_csv/[testCSV]DataSource/SacramentocrimeJanuary2006.csv";
        //"C:/Alex/Docs1/1_Codings/FYP_OptimizationInFreightTransportation/src/Data_Resource/portsLinerlib.csv"
        String line =""; //used to read line by line in csv

        try {
            BufferedReader br = new BufferedReader(new FileReader(FilePath.toString()));
            while((line = br.readLine()) != null){
                //if not null, read current line in csv []
                String[] values = line.split(seperator); // a 1D array, only rows
                System.out.println("PortName:" + values[1] + ", Country: " + values[2] +", Lat: " + values[5]+ ", Lon: " +values[6]); 
               
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
