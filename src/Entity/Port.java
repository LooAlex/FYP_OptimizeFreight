/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import Core.*;
import java.time.OffsetDateTime;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.swing.JButton;
import org.jxmapviewer.viewer.DefaultWaypoint;

/**
 *
 * @author Loo Alex
 * //Port extends DefaultWaypoint
 * //Code,Description,IsActive,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate inherited from BaseDTO
 */
public class Port extends DefaultWaypoint {
    
    public int PortID;
    public int RegionID;
    
    public String Code = "";        //ex: P001
    public String Description = ""; //Name -> Port-Louis

    public double PortLat;
    public double PortLon;
    public double Port_DockingCost;
    //Fuel
    public double Port_FuelPrice ;
    public double Port_BunkerRefuelFixedCost ;
    public boolean CanBunker;
    //Containters
    public double Port_FixedCostSetUpRig;
    public double Port_TimeOperation;
    //Penalty
    public double Port_PenaltyCostWindowTime;
    
    public  String Remarks = "";
    public boolean IsActive;
    
    public int CreatedBy;
    public OffsetDateTime CreatedDate;
    public int ModifiedBy;
    public OffsetDateTime ModifiedDate;
    public int RecordID;//general ID incase we need a general ID to use across tables.
    
    
    //Waypoint
    private JButton button;
    private PointType pointType;
    public class PortSearchDTO {
        
    }
    
    
    private static enum PointType{
        START,END;
    }

    
}

