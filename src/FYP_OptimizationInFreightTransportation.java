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
import java.sql.SQLException;
import javax.swing.UIManager;
import javax.swing.UIManager.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Loo Alex
 * This is the main, that start everything, logging  and necessary initialization start here,
 * and then OpenPanel_Main control everything
 */
public class FYP_OptimizationInFreightTransportation {

    public FYP_OptimizationInFreightTransportation() {
        
    }
    
    
    public static void main(String[] args) {    
        init();
        
        Scanner sc = new Scanner(System.in);
        new frm_OpenPanel_Main().setVisible(true);//main work             
        
        // <editor-fold defaultstate="collapsed" desc="TestArena">
       
        String codeActi = "";
        //System.out.println("Enter activationCode, it work once");
        //codeActi = sc.nextLine();
        codeActi  = "map";
        
        callingTestArena(codeActi.toLowerCase());
        // </editor-fold> 
        

        
        
    }
    
    //init anything needed here
    public static void init (){
        try {
            DBHelper DBHelper = new DBHelper(); //RegisterDriver
            
        } catch (SQLException ex) {
            Logger.getLogger(FYP_OptimizationInFreightTransportation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FYP_OptimizationInFreightTransportation.class.getName()).log(Level.SEVERE, null, ex);
        }
        FYP_OptimizationInFreightTransportation FYP_Main = new FYP_OptimizationInFreightTransportation();
        
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
