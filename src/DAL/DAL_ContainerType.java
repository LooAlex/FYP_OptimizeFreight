/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;
import java.sql.*;
import Core.*;
import Entity.*;
import Entity.GUI_Entity.GUI_Port.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Loo Alex
 */
public class DAL_ContainerType {
     private DBHelper _DBHelper;
     
     
    public DAL_ContainerType() {
        try {
            _DBHelper = new DBHelper();
            
        } catch (SQLException ex) {
            Logger.getLogger(DAL_ContainerType.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAL_ContainerType.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
        
    //Functions
    
    public Result<ArrayList<ContainerTypeDTO>> getContainerTypes(){
        Errors errors  = new Errors();
        ArrayList<ContainerTypeDTO> lst = new ArrayList<>();
        
        try
        {
            Connection conn = _DBHelper.createConnection();
            CallableStatement cstmt = conn.prepareCall("{call ContainerType_GetAll()}");
            
            ResultSet rs = cstmt.executeQuery();
            
            if(rs != null){
                while(rs.next()){
                lst.add(new ContainerTypeDTO(rs));
                }
            }else{
                errors.errorMessages.add("A Problem was encountered while retrieving Container Type.");
            }
            
            
            cstmt.close();
            conn.close();
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new Result(lst,errors);
    }
    public Result<ArrayList<ContainerTypeDTO>> getContainerType_ListByRow(int ContainerTypeID){
        Errors errors = new Errors();
        ArrayList<ContainerTypeDTO> lst  = new ArrayList<>();
        try
        {
            Connection conn = _DBHelper.createConnection();
            
            CallableStatement cstmt = conn.prepareCall("{call ContainerType_ListByRow(?)}");
            cstmt.setInt("_ContainerTypeID",ContainerTypeID);
           
            
            ResultSet rs = cstmt.executeQuery();
            
            if(rs != null){
                while(rs.next()){
                    lst.add(new ContainerTypeDTO(rs));
                   
                }
            }else{
                errors.errorMessages.add("A Problem was encounter on retreiving Container Types.");
            }
            cstmt.close();
            conn.close();
            
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return new Result(lst,errors);
    }
    public Result<ArrayList<ContainerTypeDTO>> getContainerType_GetData(String Code,String Description){
        Errors errors = new Errors();
        ArrayList<ContainerTypeDTO> lst  = new ArrayList<>();
        try
        {
            Connection conn = _DBHelper.createConnection();
            
            CallableStatement cstmt = conn.prepareCall("{call ContainerType_GetData(?,?)}");
            cstmt.setString("_Code",Code);
            cstmt.setString("_Description",Description);
            
            ResultSet rs = cstmt.executeQuery();
            
            if(rs != null){
                while(rs.next()){
                    lst.add(new ContainerTypeDTO(rs));
                   
                }
            }else{
                errors.errorMessages.add("A Problem was encounter on retreiving Container Types.");
            }
            cstmt.close();
            conn.close();
            
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return new Result(lst,errors);
    }
    
    
    
}//end
