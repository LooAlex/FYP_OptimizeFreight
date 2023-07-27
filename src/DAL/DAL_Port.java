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
public class DAL_Port {
     private DBHelper _DBHelper;
     
     
    public DAL_Port() {
        try {
            _DBHelper = new DBHelper();
            
        } catch (SQLException ex) {
            Logger.getLogger(DAL_Port.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAL_Port.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Functions
    
    //always call this when IEventPort IsReady
    public Result<ArrayList<PortDTO>> getPorts(IEventPortWaypoint IPortEvent){
        
        Errors errors = new Errors();
        ArrayList<PortDTO> lst = new ArrayList<>();
        
        try
        {
            Connection conn = _DBHelper.createConnection();
        
            CallableStatement statement = conn.prepareCall("{call Port_GetAll()}");

            ResultSet rs = statement.executeQuery();//use this to execute the current SP or query inside that stm
           
            
            while(rs.next()){
                lst.add(new PortDTO(rs,IPortEvent));
            }
            
            statement.close();
            conn.close();
 
            System.out.println("Stored procedure called successfully! Port_GetAll");
            
            
        }catch(Exception ex){
           ex.printStackTrace();
        }
                
        return new Result(lst,errors);
    }
    
    public Result<ArrayList<PortDTO>> getPort_ListByRow(int PortID,IEventPortWaypoint IPortEvent){
        
        Errors errors = new Errors();
        ArrayList<PortDTO> lst = new ArrayList<>();
        
        try
        {
            Connection conn = _DBHelper.createConnection();
        
            CallableStatement statement = conn.prepareCall("{call Port_ListByRow(?)}");
            
            statement.setInt("_PortID", PortID);
            ResultSet rs = statement.executeQuery();//use this to execute the current SP or query inside that stm

            while(rs.next()){
                lst.add(new PortDTO(rs,IPortEvent));
                break; //because we expect only 1 item.
            }
            
            statement.close();
            conn.close();
 
            System.out.println("Stored procedure called successfully! Port_ListByRow");
            
            
        }catch(Exception ex){
           ex.printStackTrace();
        }
                
        return new Result(lst,errors);
    }
    
    public Result<ArrayList<PortDTO>> getPort_GetData(String UnLocode,String PortName,IEventPortWaypoint IPortEvent){
        
        Errors errors = new Errors();
        ArrayList<PortDTO> lst = new ArrayList<>();
        
        try
        {
            Connection conn = _DBHelper.createConnection();
        
            CallableStatement statement = conn.prepareCall("{call Port_GetData(?,?)}");
            
            statement.setString("_UnLocode", UnLocode);
            statement.setString("_PortName", PortName);
            
            ResultSet rs = statement.executeQuery();//use this to execute the current SP or query inside that stm

            while(rs.next()){
                lst.add(new PortDTO(rs,IPortEvent));
            }
            
            statement.close();
            conn.close();
 
            System.out.println("Stored procedure called successfully! Port_GetData");
            
            
        }catch(Exception ex){
           ex.printStackTrace();
        }
                
        return new Result(lst,errors);
    }
    
}//end
