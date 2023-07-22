/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DLL;
import java.sql.*;
import Core.*;
import Entity.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Loo Alex
 */
public class DLL_ShipCategory {
     private DBHelper _DBHelper;
     
     
    public DLL_ShipCategory() {
        try {
            _DBHelper = new DBHelper();
            
        } catch (SQLException ex) {
            Logger.getLogger(DLL_InitAll.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DLL_InitAll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    //Functions
     public Result<ArrayList<ShipCategoryDTO>> getShipCategorys(){
        
        Errors errors = new Errors();
        ArrayList<ShipCategoryDTO> lst = new ArrayList<>();
        
        try
        {
            Connection conn = _DBHelper.createConnection();
        
            CallableStatement statement = conn.prepareCall("{call ShipCategory_GetAll()}");

            ResultSet rs = statement.executeQuery();//use this to execute the current SP or query inside that stm
           
            
            while(rs.next()){
                lst.add(new ShipCategoryDTO(rs));
            }
            
            statement.close();
            conn.close();
 
            System.out.println("Stored procedure called successfully!  ShipCategory_GetAll");
            
            
        }catch(Exception ex){
           ex.printStackTrace();
        }
                
        return new Result(lst,errors);
    }
    
    public Result<ArrayList<ShipCategoryDTO>> getShipCategory_ListByRow(int ShipCategoryID){
        
        Errors errors = new Errors();
        ArrayList<ShipCategoryDTO> lst = new ArrayList<>();
        
        try
        {
            Connection conn = _DBHelper.createConnection();
        
            CallableStatement statement = conn.prepareCall("{call ShipCategory_ListByRow(?)}");
            
            statement.setInt("_ShipCategoryID", ShipCategoryID);
            ResultSet rs = statement.executeQuery();//use this to execute the current SP or query inside that stm

            while(rs.next()){
                lst.add(new ShipCategoryDTO(rs));
                break; //because we expect only 1 item.
            }
            
            statement.close();
            conn.close();
 
            System.out.println("Stored procedure called successfully!  ShipCategory_ListByRow");
            
            
        }catch(Exception ex){
           ex.printStackTrace();
        }
                
        return new Result(lst,errors);
    }
    
    public Result<ArrayList<ShipCategoryDTO>> getShipCategory_GetData(String Code,String Description){
        
        Errors errors = new Errors();
        ArrayList<ShipCategoryDTO> lst = new ArrayList<>();
        
        try
        {
            Connection conn = _DBHelper.createConnection();
        
            CallableStatement statement = conn.prepareCall("{call ShipCategory_GetData(?,?)}");
            
            statement.setString("_Code", Code);
            statement.setString("_Description", Description);
            
            ResultSet rs = statement.executeQuery();//use this to execute the current SP or query inside that stm

            while(rs.next()){
                lst.add(new ShipCategoryDTO(rs));
            }
            
            statement.close();
            conn.close();
 
            System.out.println("Stored procedure called successfully! ShipCategory_GetData");
            
            
        }catch(Exception ex){
           ex.printStackTrace();
        }
                
        return new Result(lst,errors);
    }
}//end
