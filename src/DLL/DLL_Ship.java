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
public class DLL_Ship {
     private DBHelper _DBHelper;
     
     
    public DLL_Ship() {
        try {
            _DBHelper = new DBHelper();
            
        } catch (SQLException ex) {
            Logger.getLogger(DLL_InitAll.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DLL_InitAll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    //Functions
    
}//end
