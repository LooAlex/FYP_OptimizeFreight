/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import Entity.GUI_Entity.GUI_Port.IEventPortWaypoint;
import Core.*;
import Entity.GUI_Entity.GUI_Port.ButtonPort;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.OffsetDateTime;

import javax.swing.JButton;
import jdk.jfr.Description;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author Loo Alex
 //Port extends DefaultWaypoint
 //UnLoCode,Description,IsActive,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate inherited from BaseDTO
 */
public class Port extends DefaultWaypoint {
    
    public int PortID;
    public int RegionID;
    
    public String UnLoCode = "";        // UNLoCode
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

    // <editor-fold defaultstate="collapsed" desc="Get-Set PortOnly">
    public int getPortID() {
        return PortID;
    }

    public void setPortID(int PortID) {
        this.PortID = PortID;
    }

    public int getRegionID() {
        return RegionID;
    }

    public void setRegionID(int RegionID) {
        this.RegionID = RegionID;
    }

    public String getUnLoCode() {
        return UnLoCode;
    }

    public void setUnLoCode(String UnLoCode) {
        this.UnLoCode = UnLoCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public double getPortLat() {
        return PortLat;
    }

    public void setPortLat(double PortLat) {
        this.PortLat = PortLat;
    }

    public double getPortLon() {
        return PortLon;
    }

    public void setPortLon(double PortLon) {
        this.PortLon = PortLon;
    }

    public double getPort_DockingCost() {
        return Port_DockingCost;
    }

    public void setPort_DockingCost(double Port_DockingCost) {
        this.Port_DockingCost = Port_DockingCost;
    }

    public double getPort_FuelPrice() {
        return Port_FuelPrice;
    }

    public void setPort_FuelPrice(double Port_FuelPrice) {
        this.Port_FuelPrice = Port_FuelPrice;
    }

    public double getPort_BunkerRefuelFixedCost() {
        return Port_BunkerRefuelFixedCost;
    }

    public void setPort_BunkerRefuelFixedCost(double Port_BunkerRefuelFixedCost) {
        this.Port_BunkerRefuelFixedCost = Port_BunkerRefuelFixedCost;
    }

    public boolean isCanBunker() {
        return CanBunker;
    }

    public void setCanBunker(boolean CanBunker) {
        this.CanBunker = CanBunker;
    }

    public double getPort_FixedCostSetUpRig() {
        return Port_FixedCostSetUpRig;
    }

    public void setPort_FixedCostSetUpRig(double Port_FixedCostSetUpRig) {
        this.Port_FixedCostSetUpRig = Port_FixedCostSetUpRig;
    }

    public double getPort_TimeOperation() {
        return Port_TimeOperation;
    }

    public void setPort_TimeOperation(double Port_TimeOperation) {
        this.Port_TimeOperation = Port_TimeOperation;
    }

    public double getPort_PenaltyCostWindowTime() {
        return Port_PenaltyCostWindowTime;
    }

    public void setPort_PenaltyCostWindowTime(double Port_PenaltyCostWindowTime) {
        this.Port_PenaltyCostWindowTime = Port_PenaltyCostWindowTime;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public boolean isIsActive() {
        return IsActive;
    }

    public void setIsActive(boolean IsActive) {
        this.IsActive = IsActive;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public OffsetDateTime getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(OffsetDateTime CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public int getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(int ModifiedBy) {
        this.ModifiedBy = ModifiedBy;
    }

    public OffsetDateTime getModifiedDate() {
        return ModifiedDate;
    }

    public void setModifiedDate(OffsetDateTime ModifiedDate) {
        this.ModifiedDate = ModifiedDate;
    }

    public int getRecordID() {
        return RecordID;
    }

    public void setRecordID(int RecordID) {
        this.RecordID = RecordID;
    }
    
    // </editor-fold>
    
    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) { //
        this.button = button;
    }

    public PointType getPointType() {
        return pointType;
    }

    public void setPointType(PointType pointType) {
        this.pointType = pointType;
    }
    

        
    public Port(){};
    
    public Port (String UnLoCode, String Description,PointType pointType,IEventPortWaypoint IPortevent, GeoPosition coord){
        super(coord);
        this.UnLoCode = UnLoCode;
        this.Description = Description;   
        this.pointType = pointType;
        initButton(IPortevent);  //did not have this code before<-- 29/06/23
    }
    

   
    // <editor-fold defaultstate="collapsed" desc="PortSearchDTO">
    public class PortSearchDTO {

    }
    // </editor-fold>s
    
    // <editor-fold defaultstate="collapsed" desc="PortCreateUpdate">
    public class PortCreateUpdate{
        public int RegionID;                    //D_region
        public String RegionName;               
        
        public int CountryID;                   //Country
        public String CountryName;              

        public String CabotageRegionID;         //CabotageRegion
        public String CabotageRegionName;    
        
        public String UnLoCode = "";            //ex: P001 //UnLocode
        public String Description = "";         //PortName -> Port-Louis

        public double PortLat;                  //Latitude
        public double PortLon;                  //Longitude
        //Fuel
        public double Port_FuelPrice ;          
        public double PortCall_CostFix ;        //Both BunkerPrice and DockingCost
        public boolean CanBunker;
        //Containters
        public double Port_CostPerFullContainer;   
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

        public int getRegionID() {
            return RegionID;
        }

        public void setRegionID(int RegionID) {
            this.RegionID = RegionID;
        }

        public String getRegionName() {
            return RegionName;
        }

        public void setRegionName(String RegionName) {
            this.RegionName = RegionName;
        }

        public int getCountryID() {
            return CountryID;
        }

        public void setCountryID(int CountryID) {
            this.CountryID = CountryID;
        }

        public String getCountryName() {
            return CountryName;
        }

        public void setCountryName(String CountryName) {
            this.CountryName = CountryName;
        }

        public String getCabotageRegionID() {
            return CabotageRegionID;
        }

        public void setCabotageRegionID(String CabotageRegionID) {
            this.CabotageRegionID = CabotageRegionID;
        }

        public String getCabotageRegionName() {
            return CabotageRegionName;
        }

        public void setCabotageRegionName(String CabotageRegionName) {
            this.CabotageRegionName = CabotageRegionName;
        }

        public String getUnLoCode() {
            return UnLoCode;
        }

        public void setUnLoCode(String UnLoCode) {
            this.UnLoCode = UnLoCode;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public double getPortLat() {
            return PortLat;
        }

        public void setPortLat(double PortLat) {
            this.PortLat = PortLat;
        }

        public double getPortLon() {
            return PortLon;
        }

        public void setPortLon(double PortLon) {
            this.PortLon = PortLon;
        }

        public double getPort_FuelPrice() {
            return Port_FuelPrice;
        }

        public void setPort_FuelPrice(double Port_FuelPrice) {
            this.Port_FuelPrice = Port_FuelPrice;
        }

        public double getPortCall_CostFix() {
            return PortCall_CostFix;
        }

        public void setPortCall_CostFix(double PortCall_CostFix) {
            this.PortCall_CostFix = PortCall_CostFix;
        }

        public boolean isCanBunker() {
            return CanBunker;
        }

        public void setCanBunker(boolean CanBunker) {
            this.CanBunker = CanBunker;
        }

        public double getPort_CostPerFullContainer() {
            return Port_CostPerFullContainer;
        }

        public void setPort_CostPerFullContainer(double Port_CostPerFullContainer) {
            this.Port_CostPerFullContainer = Port_CostPerFullContainer;
        }

        public double getPort_TimeOperation() {
            return Port_TimeOperation;
        }

        public void setPort_TimeOperation(double Port_TimeOperation) {
            this.Port_TimeOperation = Port_TimeOperation;
        }

        public double getPort_PenaltyCostWindowTime() {
            return Port_PenaltyCostWindowTime;
        }

        public void setPort_PenaltyCostWindowTime(double Port_PenaltyCostWindowTime) {
            this.Port_PenaltyCostWindowTime = Port_PenaltyCostWindowTime;
        }

        public String getRemarks() {
            return Remarks;
        }

        public void setRemarks(String Remarks) {
            this.Remarks = Remarks;
        }

        public boolean isIsActive() {
            return IsActive;
        }

        public void setIsActive(boolean IsActive) {
            this.IsActive = IsActive;
        }

        public int getCreatedBy() {
            return CreatedBy;
        }

        public void setCreatedBy(int CreatedBy) {
            this.CreatedBy = CreatedBy;
        }

        public OffsetDateTime getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(OffsetDateTime CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public int getModifiedBy() {
            return ModifiedBy;
        }

        public void setModifiedBy(int ModifiedBy) {
            this.ModifiedBy = ModifiedBy;
        }

        public OffsetDateTime getModifiedDate() {
            return ModifiedDate;
        }

        public void setModifiedDate(OffsetDateTime ModifiedDate) {
            this.ModifiedDate = ModifiedDate;
        }

        public int getRecordID() {
            return RecordID;
        }

        public void setRecordID(int RecordID) {
            this.RecordID = RecordID;
        }

        //Waypoint
        private JButton button;
        private PointType pointType;
        
        public PortCreateUpdate(String[]arrCSVLine){
        
        
         //0 UnLocode
        UnLoCode = ((arrCSVLine.length < ColumnIndex.UNLOCODE+1 || arrCSVLine[ColumnIndex.UNLOCODE]== null || arrCSVLine[ColumnIndex.UNLOCODE].isBlank() ) ? "" : arrCSVLine[ColumnIndex.UNLOCODE]);
        //1 name
        Description = ((arrCSVLine.length < ColumnIndex.DESCRIPTION+1 || arrCSVLine[ColumnIndex.DESCRIPTION]== null || arrCSVLine[ColumnIndex.DESCRIPTION].isBlank() ) ? "" : arrCSVLine[ColumnIndex.DESCRIPTION]);
        //2 Country      
        CountryName = ((arrCSVLine.length < ColumnIndex.COUNTRY_ID+1 || arrCSVLine[ColumnIndex.COUNTRY_ID]== null || arrCSVLine[ColumnIndex.COUNTRY_ID].isBlank() ) ? "" : arrCSVLine[ColumnIndex.COUNTRY_ID]);
        //4 D_Region
        RegionName = ((arrCSVLine.length < ColumnIndex.REGION_ID+1 || arrCSVLine[ColumnIndex.REGION_ID]== null || arrCSVLine[ColumnIndex.REGION_ID].isBlank() ) ? "" : arrCSVLine[ColumnIndex.REGION_ID]);
        //3 CabotageRegion
        CabotageRegionName = ((arrCSVLine.length < ColumnIndex.CABOTAGE_REGION_ID+1 || arrCSVLine[ColumnIndex.CABOTAGE_REGION_ID]== null || arrCSVLine[ColumnIndex.CABOTAGE_REGION_ID].isBlank() ) ? "" : arrCSVLine[ColumnIndex.CABOTAGE_REGION_ID]);
        //5 Longitude
        PortLon  = ((arrCSVLine.length < ColumnIndex.PORT_LON+1|| arrCSVLine[ColumnIndex.PORT_LON]== null || arrCSVLine[ColumnIndex.PORT_LON].isBlank() ) ? 0.00d :Double.parseDouble( arrCSVLine[ColumnIndex.PORT_LON]));
        //6 Latitude
        PortLat  = ((arrCSVLine.length < ColumnIndex.PORT_LAT+1 || arrCSVLine[ColumnIndex.PORT_LAT]== null || arrCSVLine[ColumnIndex.PORT_LAT].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndex.PORT_LAT])); 
        //8 CostPerFULL   Container Moved
        Port_CostPerFullContainer = ((arrCSVLine.length < ColumnIndex.COST_PER_FULL_CONTAINER+1 || arrCSVLine[ColumnIndex.COST_PER_FULL_CONTAINER]== null || arrCSVLine[ColumnIndex.COST_PER_FULL_CONTAINER].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndex.COST_PER_FULL_CONTAINER]));
        //10 PortCallCostFixed <-- BunnkerCost and Docking Charges
        PortCall_CostFix = ((arrCSVLine.length < ColumnIndex.PORTCALL_COSTFIX+1 || arrCSVLine[ColumnIndex.PORTCALL_COSTFIX]== null || arrCSVLine[ColumnIndex.PORTCALL_COSTFIX].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndex.PORTCALL_COSTFIX]));
        
        
        

        }
    }
    // </editor-fold>
    private void initButton( IEventPortWaypoint IPortevent){
            button = new ButtonPort(); //takes care UI aspect, icon etc of the button waypoint
            button.addActionListener(new ActionListener() {
                //add an eventlistener to to the button of the waypoint being created, so that when we click on waypoint, something happens
                @Override
                public void actionPerformed(ActionEvent e) {
                 //inate functions of any JButton for an addActionListener is the function actionPerformed, inside this function
                 //is all the things that the system will do once this button for this waypoint is clicked.

                    // if this button is clicked :
                    IPortevent.selected(Port.this);

                }
            });
     
    }
    
    public static enum PointType{
        START,END;
    }
    
//    public static enum ColumnIndex{
//        PORTID              (999),
//        UNLOCODE            (0), //cvs
//        DESCRIPTION         (1),
//        COUNTRY_ID          (2),
//        REGION_ID           (4),
//        CABOTAGE_REGION_ID  (3),
//        PORT_LAT            (6),
//        PORT_LON            (5),
//        PORTCALL_COSTFIX    (10),
//        COST_PER_FULL_CONTAINER(8),
//        PORT_FUEL_PRICE     (999),
//        PENALTY_TIME_CONSTRAINT_PER_HOUR(999),
//        LOAD_UNLOAD_TIME_PER_CONTAINER(999),
//        CAN_BUNKER          (999);
//        
//        private int value;
//        ColumnIndex(int value){
//            this.value = value;
//        }
//        public int getValue(){return value;}
//
//    }
    public class ColumnIndex{
        static int PORTID              =999;
        static int UNLOCODE            = 0;  //cvs
        static int DESCRIPTION         = 1;
        static int COUNTRY_ID          = 2;
        static int REGION_ID           = 4;
        static int CABOTAGE_REGION_ID  = 3;
        static int PORT_LAT            = 6;
        static int PORT_LON            = 5;
        static int PORTCALL_COSTFIX    = 10;
        static int COST_PER_FULL_CONTAINER = 8;
        static int PORT_FUEL_PRICE     = 999;
        static int PENALTY_TIME_CONSTRAINT_PER_HOUR = 999;
        static int LOAD_UNLOAD_TIME_PER_CONTAINER = 999;
        static int CAN_BUNKER          = 999;
    }

    
}

