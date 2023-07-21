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
public class DLL_InitAll {
    private DBHelper _DBHelper;
    public DLL_InitAll() {
        try {
            _DBHelper = new DBHelper();
            
        } catch (SQLException ex) {
            Logger.getLogger(DLL_InitAll.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DLL_InitAll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
            
            statement.execute();//this will only execute the statement, and indicate to return something even if it did
            statement.close();
            conn.close();
 
            System.out.println("Stored procedure called successfully!");
            
            
        }catch(Exception ex){
           ex.printStackTrace();
        }
                
        return new Result(lst,errors);
    }
}
