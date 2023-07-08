/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import testArena.tuto_map.frm_tst_Map;
import testArena.tuto_csv.*;

import FRM.*;
import javax.swing.UIManager;
import javax.swing.UIManager.*;
import java.util.Scanner;



/**
 *
 * @author Loo Alex
 */
public class FYP_OptimizationInFreightTransportation {
    static String  className ;
    /** Dark
     * @param args the command line arguments
     */
    public static void main(String[] args) {    
        Scanner sc = new Scanner(System.in);
        new frm_SimulationPanelMain2().setVisible(true);//main work             
               
        System.out.println("Enter activationCode, it work once");
        String codeActi = sc.nextLine();
        callingTestArena(codeActi.toLowerCase());

        

        
        
    }
    
    public static void callingTestArena(String codeActivation){
       switch(codeActivation){
           case "map":        
               new frm_tst_Map().setVisible(true);
               break;
               
           case "csv":
               new CSV_Reader();
               break;   
               
           case "abc":
               break;
               
           default:
               break;
          
              
                  
       }
    }

    
}
