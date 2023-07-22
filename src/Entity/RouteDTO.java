/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;
import Core.*;
import java.sql.*;
/**
 *
 * @author Loo Alex
 * 
 * there will be RouteHeaderDTO, and RouteContentDTO
 */
public class RouteDTO extends BaseDTO{
    public int RouteID;
    
    public int OriginPortID;
    public int DestinationPortID;
    public double Distance;

    public RouteDTO() {
    }
    
    public RouteDTO(ResultSet r) throws SQLException{
        super(r);
       
    }
    
}
