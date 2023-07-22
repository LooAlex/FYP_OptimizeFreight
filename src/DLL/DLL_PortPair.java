/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DLL;
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
public class DLL_PortPair {
     private DBHelper _DBHelper;
     
     
    public DLL_PortPair() {
        try {
            _DBHelper = new DBHelper();
            
        } catch (SQLException ex) {
            Logger.getLogger(DLL_InitAll.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DLL_InitAll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    //Functions
      public Result<ArrayList<PortPairDistanceDTO>> getPairPortDistances(){
        
        Errors errors = new Errors();
        ArrayList<PortPairDistanceDTO> lst = new ArrayList<>();
        
        try
        {
            Connection conn = _DBHelper.createConnection();
        
            CallableStatement statement = conn.prepareCall("{call PairPortDistance_GetAll()}");

            ResultSet rs = statement.executeQuery();//use this to execute the current SP or query inside that stm
           
            if(rs != null){
                while(rs.next()){
                    lst.add(new PortPairDistanceDTO(rs));
                }
            }else{
                errors.errorMessages.add("A Problem was encounter while retreiving Pair Port Distances");
            }
           
            
            statement.close();
            conn.close();
 
            System.out.println("Stored procedure called successfully!  PairPortDistance_GetAll");
            
            
        }catch(Exception ex){
           ex.printStackTrace();
        }
                
        return new Result(lst,errors);
    }
    
    public Result<ArrayList<PortPairDistanceDTO>> getPairPortDistance_ListByRow(int OriginPortID,int DestinationPortID){
        
        Errors errors = new Errors();
        ArrayList<PortPairDistanceDTO> lst = new ArrayList<>();
        
        try
        {
            Connection conn = _DBHelper.createConnection();
        
            CallableStatement statement = conn.prepareCall("{call PortPairDistance_ListByRow_OD(?,?)}");
            
            statement.setInt("_OriginPortID", OriginPortID);
            statement.setInt("_DestinationPortID", DestinationPortID);
            ResultSet rs = statement.executeQuery();//use this to execute the current SP or query inside that stm

            if(rs != null){
                while(rs.next()){
                    lst.add(new PortPairDistanceDTO(rs));
                    break;
                }
            }else{
                errors.errorMessages.add("A Problem was encounter while retreiving Pair Port Distances");
            }
            
            
            statement.close();
            conn.close();
 
            System.out.println("Stored procedure called successfully! PortPairDistance_ListByRow");
            
            
        }catch(Exception ex){
           ex.printStackTrace();
        }
                
        return new Result(lst,errors);
    }
    
    public Result<ArrayList<PortPairDistanceDTO>> getPort_GetData(String Code,String Description,int OriginPortID,int DestinationPortID){
        
        Errors errors = new Errors();
        ArrayList<PortPairDistanceDTO> lst = new ArrayList<>();
        
        try
        {
            Connection conn = _DBHelper.createConnection();
        
            CallableStatement statement = conn.prepareCall("{call PairPortDistance_GetData(?,?)}");
            
            statement.setString("_Code", Code);
            statement.setString("_Description", Description);
            statement.setInt("_OriginPortID", OriginPortID);
            statement.setInt("_DestinationPortID", DestinationPortID);
            
            ResultSet rs = statement.executeQuery();//use this to execute the current SP or query inside that stm

            if(rs != null){
                while(rs.next()){
                    lst.add(new PortPairDistanceDTO(rs));
                }
            }else{
                errors.errorMessages.add("A Problem was encounter while retreiving Pair Port Distances");
            }
            
            statement.close();
            conn.close();
 
            System.out.println("Stored procedure called successfully!");
            
            
        }catch(Exception ex){
           ex.printStackTrace();
        }
                
        return new Result(lst,errors);
    }
}//end
