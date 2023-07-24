/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import testArena.tuto_csv.CSV_Reader;

/**
 *
 * @author Loo Alex
 * Contains all general and useful functions
 */
public final class  CoreFunctions  {
    
    
    private CoreFunctions(){
        throw new IllegalAccessError("Instanciation not allow");
    }
    
    //
    public static Result<List<String[]>> readCSV(String FilePath, String regexDelimiter,boolean hasTitle){
        Errors errors = new Errors();
        
        List<String[]> lstArrValues = new ArrayList<>();
        
        if(regexDelimiter.toLowerCase().equals("tab")){
            regexDelimiter = "\\t";
        }
        
        //FilePath = "C:/Alex/Docs1/1_Codings/FYP_OptimizationInFreightTransportation/src/testArena/tuto_csv/[testCSV]DataSource/SacramentocrimeJanuary2006.csv";
        //"C:/Alex/Docs1/1_Codings/FYP_OptimizationInFreightTransportation/src/Data_Resource/portsLinerlib.csv"
        
        String line =""; //used to read line by line in csv
        try {
            BufferedReader br = new BufferedReader(new FileReader(FilePath.toString()));
            while((line = br.readLine()) != null){
                if(hasTitle == true){
                    hasTitle = false;
                    continue;
                    
                }
                    
                //if not null, read current line in csv []
                String[] values = line.split(regexDelimiter); // a 1D array, only rows
                
                lstArrValues.add(values);
            }      
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            Logger.getLogger(CSV_Reader.class.getName()).log(Level.SEVERE, null, ex);
            
            errors.errorMessages.add("File not found.");
            
        }  catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(CSV_Reader.class.getName()).log(Level.SEVERE, null, ex);
           
            errors.errorMessages.add("Cannot read or write to file");
        }
        
        
        return new Result(lstArrValues,errors);
    }
    
    //Takes an object[] and convert it to string, the delimiter is what seperate each values in the array on display.
    //ex: int[] arr = new int[]{1,2,3,4} Delimiter "-" gives 1-2-3-4 as String output
    public static String convertObjectArrayToString(Object[] arr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : arr)
                sb.append(obj.toString()).append(delimiter);

        return sb.substring(0, sb.length() - 1);

    }
    
    //Convert any object into an array of object
    //used mainly when we hold an array of something that we want to convert to string.
    public static Object[] ConvertToObjectArray( Object obj){   
        return (Object[])obj;
    }
    
    public static boolean convertStringToBoolean(String s){
        boolean bool = false;
        if(Integer.parseInt(s) == 1){
            bool = true;
        }
        
        return bool;
    }
    
    public static int randomInt(int floor,int ceiling){
        return new Random().nextInt(floor,ceiling);
    }
            
    public static DecimalFormat getDecimalFormat(int decimalPoint){
        DecimalFormat df  = new DecimalFormat();
        df.setMaximumFractionDigits(decimalPoint);
            
        return df;
    }
}   
