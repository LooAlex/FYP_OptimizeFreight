/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;
import java.sql.*;
/**
 *
 * @author Loo Alex
 */
public class DBHelper {
    public String mySqlDriver ="com.mysql.cj.jdbc.Driver" ;
    public String DBName = "fyp";
    public static String urlHost = "jdbc:mysql://localhost:3306/";
    public static String uname = "root";
    public static String password = "";
    public String url;
    
    //use this most of the time
    public DBHelper() throws SQLException, ClassNotFoundException{
        //1.make driver understand
        Class.forName(mySqlDriver);
        //construct url
        url = urlHost+DBName;
    }
    
    public DBHelper(String _DBName) throws SQLException, ClassNotFoundException{
        //1.make driver understand
        Class.forName(mySqlDriver);
        //construct url
        url = urlHost+_DBName;
        //2.make connection
    }
    
    public DBHelper(String _DBName, String _uname, String _password) throws SQLException, ClassNotFoundException{
        //1.make driver understand
        Class.forName(mySqlDriver);
        //construct url
        url = urlHost+_DBName;
    }   
    
    public Connection createConnection() throws SQLException{
        return DriverManager.getConnection(url,uname,password);
    }
    
}
