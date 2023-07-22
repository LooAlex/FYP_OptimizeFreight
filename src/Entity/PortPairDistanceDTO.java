/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;
import java.sql.*;
import Core.*;
/**
 *
 * @author Loo Alex
 */
public class PortPairDistanceDTO extends BaseDTO{

    public int getPortPairDistanceID() {
        return PortPairDistanceID;
    }

    public void setPortPairDistanceID(int PortPairDistanceID) {
        this.PortPairDistanceID = PortPairDistanceID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public int getOriginPortID() {
        return OriginPortID;
    }

    public void setOriginPortID(int OriginPortID) {
        this.OriginPortID = OriginPortID;
    }

    public int getDestinationPortID() {
        return DestinationPortID;
    }

    public void setDestinationPortID(int DestinationPortID) {
        this.DestinationPortID = DestinationPortID;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(Double Distance) {
        this.Distance = Distance;
    }
    public int PortPairDistanceID;
    public int OriginPortID;
    public int DestinationPortID;
    public double Distance;
    
    public PortPairDistanceDTO(){
        
    }
    
    public PortPairDistanceDTO(ResultSet rs) throws SQLException {
        super(rs);
        PortPairDistanceID = rs.getInt("PortPairDistance_ID");
        OriginPortID  = rs.getInt("OriginPortID");
        DestinationPortID = rs.getInt("DestinationPortID");
        Distance = rs.getDouble("Distance");
    }
    
}
