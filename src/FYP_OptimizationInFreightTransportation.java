/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import testArena.tuto_map.frm_tst_Map;
import testArena.tuto_csv.*;
import Core.*;
import FRM.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UIManager.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Loo Alex
 */
public class FYP_OptimizationInFreightTransportation {
    public static String  classDirectory ;
    public FYP_OptimizationInFreightTransportation() {
    }
    
    
    public static void main(String[] args) {    
        init();
        
        Scanner sc = new Scanner(System.in);
        new frm_SimulationPanel_Main().setVisible(true);//main work             
        //Functions.readCSV(args,this.getClass().getResource("/Data_Resource/portsLinerlib.csv"), ws);
        String currentFilePath = classDirectory + "/portsLinerlib.csv";
        System.out.println(currentFilePath);
        CoreFunctions.readCSV("C:/Alex/Docs1/1_Codings/FYP_OptimizationInFreightTransportation/src/Data_Resource/portsLinerlib.csv", "ws");
        
        // <editor-fold defaultstate="collapsed" desc="TestArena">
        
        String codeActi = "";
        //System.out.println("Enter activationCode, it work once");
        //codeActi = sc.nextLine();
        codeActi  = "csv";
        
        callingTestArena(codeActi.toLowerCase());
        // </editor-fold> 
        

        
        
    }
    public static void init (){
        FYP_OptimizationInFreightTransportation FYP_Main = new FYP_OptimizationInFreightTransportation();
        FYP_Main.initClassDirectory();
    }
    
    public String initClassDirectory(){
        return this.getClass().getResource("/Data_Resource/").toString();
    }
    
    public static void readCSV(String FilePath, String seperator){
        
        if(seperator.toLowerCase().equals("ws")){
            seperator = "\t";
        }
        
        //FilePath = "C:/Alex/Docs1/1_Codings/FYP_OptimizationInFreightTransportation/src/testArena/tuto_csv/[testCSV]DataSource/SacramentocrimeJanuary2006.csv";
        String[] line ; //used to read line by line in csv

        try 
        {
            BufferedReader br = new BufferedReader(new FileReader(FilePath));
            String  nextLine;
            while ((nextLine = br.readLine()) != null) {
                //if not null, read current line in csv []
                String[] values = nextLine.split("\t"); // a 1D array, only rows
                System.out.println("PortName:" + values[1] + ", Country: " + values[2] +", Lat: " + values[5]+ ", Lon: " +values[6]); 
            }
            
            //use ui path to take all data from csv and replace delimiter with comma
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            Logger.getLogger(CSV_Reader.class.getName()).log(Level.SEVERE, null, ex);
            
        }  catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(CSV_Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void callingTestArena(String codeActivation){
       switch(codeActivation){
           case "map":        
               new frm_tst_Map().setVisible(true);
               break;
               
           case "csv":
               new CSV_Reader().readCSV();
               break;   
               
           case "abc":
               break;
               
           default:
               break;   
       }
    }

    
}
