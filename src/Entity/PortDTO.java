/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;
import java.sql.*;
import Entity.GUI_Entity.GUI_Port.IEventPortWaypoint;
import Core.*;
import Entity.GUI_Entity.GUI_Port.ButtonPort;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.swing.JButton;
import jdk.jfr.Description;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author Loo Alex
 //PortDTO extends DefaultWaypoint
 //UnLoCode,PortName,IsActive,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate inherited from BaseDTO
 */
public class PortDTO extends DefaultWaypoint {
    
    public int PortID;

    public int CountryID;                   //Country
    public String CountryName; 
    
    public int CabotageRegionID;         //CabotageRegion
    public String CabotageRegionName;
    
    public int RegionID;                    //D_region
    public String RegionName;               

    public String UnLoCode = "";            //ex: P001 //UnLocode
    public String PortName = "";         //PortName -> PortDTO-Louis

    public double PortLat;                  //Latitude
    public double PortLon;                  //Longitude
    
    //--PortCall
    public double PortCall_Cost ;        //Both BunkerPrice and DockingCost
    
    //--Fuel
    public double Port_FuelPrice ;
    public boolean CanBunker;
    
    //--Containters
    public double Port_CostPerFullContainer;   
    
    //--Penalty
    public double Penalty_LateArrival;
    
    
    // <editor-fold defaultstate="collapsed" desc="Simulation variable">
    
    //--Time
    public float ETA; //Estimate Time Arrival.

    //--Ship
    public ShipCategoryDTO currentShip;
    
    public double shp_timeTaken;
    public double shp_choseSpeed;
 
    public DemandDTO demands;
 
    
    //ObjetiveFunction var
    public double TotalHandlingCost;        //CHdl :: $ |(DemandAmt+SupplyAmnt)*Port_CostPerFullContainer
    public double TotalFuelTravelCost;      //Ctravel :: $ |previous port i to this port j
    public double TotalFuelIdleCost;        //CFIdle :: $ |for this port i, for duration of operation
    public double TotalPenaltyCost;         //CPnt :: $ |
    public double TotalOperationalCost;
    // </editor-fold>
    
    //--
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
    
    // <editor-fold defaultstate="collapsed" desc="Get Set PortDTO">
    public DemandDTO getDemands() {
        return demands;
    }

    //container
    public void setDemands(int ShipCapacity) {
        this.demands = new DemandDTO(100,ShipCapacity);
    }
    
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

    public int getCabotageRegionID() {
        return CabotageRegionID;
    }

    public void setCabotageRegionID(int CabotageRegionID) {
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

    public String getPortName() {
        return PortName;
    }

    public void setPortName(String PortName) {
        this.PortName = PortName;
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

    public double getPortCall_Cost() {
        return PortCall_Cost;
    }

    public void setPortCall_Cost(double PortCall_Cost) {
        this.PortCall_Cost = PortCall_Cost;
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

    public double getPenalty_LateArrival() {
        return Penalty_LateArrival;
    }

    public void setPenalty_LateArrival(double Penalty_LateArrival) {
        this.Penalty_LateArrival = Penalty_LateArrival;
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
    // </editor-fold>

        
    public PortDTO(){};
    
    public PortDTO (String UnLoCode, String Description,PointType pointType,IEventPortWaypoint IPortevent, GeoPosition coord){
        super(coord);
        this.UnLoCode = UnLoCode;
        this.PortName = Description;   
        this.pointType = pointType;
        initButton(IPortevent);  //did not have this code before<-- 29/06/23
        demands = new DemandDTO();
    }
    public PortDTO(ResultSet rs,IEventPortWaypoint IPortEvent) throws SQLException{
        super(new GeoPosition(
                rs.getDouble("Latitude"), 
                rs.getDouble("Longitude")));
        
        initButton(IPortEvent); //mantatody in all PortDTO events
        PortID = rs.getInt("Port_ID");
        CountryID = rs.getInt("CountryID");
        CabotageRegionID = rs.getInt("CabotageRegionID");
        RegionID = rs.getInt("RegionID");
        
        UnLoCode = rs.getString("UNLocode");
        PortName = rs.getString("PortName");
        PortLat = rs.getDouble("Latitude");
        PortLon = rs.getDouble("Longitude");
        
        PortCall_Cost = rs.getDouble("PortCallCost");
        
        Port_FuelPrice = rs.getDouble("PortFuelPrice");
        CanBunker = CoreFunctions.convertStringToBoolean(rs.getString("CanBunker"));
        
        Port_CostPerFullContainer = rs.getDouble("CostPerFullContainerLoadUnLoad");
        Penalty_LateArrival = rs.getDouble("PenaltyLateArrival");
        
        Remarks = rs.getString("Remarks");
        IsActive = CoreFunctions.convertStringToBoolean(rs.getString("IsActive"));
        CreatedBy = rs.getInt("CreatedBy");
       
          
    }
    
    public PortDTO(PortDTO port) {

        PortID = port.PortID;
        CountryID = port.CountryID;
        CabotageRegionID = port.CabotageRegionID;
        RegionID = port.RegionID;
        
        UnLoCode = port.UnLoCode;
        PortName = port.PortName;
        PortLat = port.PortLat;
        PortLon = port.PortLon;
        
        PortCall_Cost = port.PortCall_Cost;
        
        Port_FuelPrice = port.Port_FuelPrice;
        CanBunker = port.CanBunker;
        
        Port_CostPerFullContainer = port.Port_CostPerFullContainer;
        Penalty_LateArrival = port.Penalty_LateArrival;
        
        Remarks = port.Remarks;
        IsActive = port.IsActive;
        CreatedBy = port.CreatedBy;
       
        demands = port.demands;

    }
    public PortDTO (String[]arrCSVLine,IEventPortWaypoint IPortEvent){
        //super(coords)
        super(new GeoPosition( 
                ((arrCSVLine[ColumnIndexCSV.PORT_LAT]== null || arrCSVLine.length < ColumnIndexCSV.PORT_LAT+1 || arrCSVLine[ColumnIndexCSV.PORT_LAT].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndexCSV.PORT_LAT])) ,
                ((arrCSVLine[ColumnIndexCSV.PORT_LON]== null || arrCSVLine.length < ColumnIndexCSV.PORT_LON+1 || arrCSVLine[ColumnIndexCSV.PORT_LON].isBlank() ) ? 0.00d : Double.parseDouble( arrCSVLine[ColumnIndexCSV.PORT_LON])) 
        ));
        
        initButton(IPortEvent); //mantatody in all PortDTO events
        PortID = ((arrCSVLine.length < ColumnIndexCSV.PORTID+1 || arrCSVLine[ColumnIndexCSV.PORTID]== null || arrCSVLine[ColumnIndexCSV.PORTID].isBlank() ) ? 0 : Integer.parseInt(arrCSVLine[ColumnIndexCSV.PORTID]));
         //0 UnLocode
        UnLoCode = ((arrCSVLine.length < ColumnIndexCSV.UNLOCODE+1 || arrCSVLine[ColumnIndexCSV.UNLOCODE]== null || arrCSVLine[ColumnIndexCSV.UNLOCODE].isBlank() ) ? "" : arrCSVLine[ColumnIndexCSV.UNLOCODE]);
        //1 name
        PortName = ((arrCSVLine.length < ColumnIndexCSV.PORTNAME+1 || arrCSVLine[ColumnIndexCSV.PORTNAME]== null || arrCSVLine[ColumnIndexCSV.PORTNAME].isBlank() ) ? "" : arrCSVLine[ColumnIndexCSV.PORTNAME]);
        //2 CountryID     
        CountryID = ((arrCSVLine.length < ColumnIndexCSV.COUNTRY_ID+1 || arrCSVLine[ColumnIndexCSV.COUNTRY_ID]== null || arrCSVLine[ColumnIndexCSV.COUNTRY_ID].isBlank() ) ? 0 : Integer.parseInt(arrCSVLine[ColumnIndexCSV.COUNTRY_ID]));
        //3 CabotageRegionID
        CabotageRegionID = ((arrCSVLine.length < ColumnIndexCSV.CABOTAGE_REGION_ID+1 || arrCSVLine[ColumnIndexCSV.CABOTAGE_REGION_ID]== null || arrCSVLine[ColumnIndexCSV.CABOTAGE_REGION_ID].isBlank() ) ? 0 : Integer.parseInt(arrCSVLine[ColumnIndexCSV.CABOTAGE_REGION_ID]));        
        //4 D_RegionID
        RegionID = ((arrCSVLine.length < ColumnIndexCSV.REGION_ID+1 || arrCSVLine[ColumnIndexCSV.REGION_ID]== null || arrCSVLine[ColumnIndexCSV.REGION_ID].isBlank() ) ? 0 : Integer.parseInt(arrCSVLine[ColumnIndexCSV.REGION_ID]));
        //5 Latitude
        PortLat  = ((arrCSVLine.length < ColumnIndexCSV.PORT_LAT+1 || arrCSVLine[ColumnIndexCSV.PORT_LAT]== null || arrCSVLine[ColumnIndexCSV.PORT_LAT].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndexCSV.PORT_LAT])); 
        //6 Longitude
        PortLon  = ((arrCSVLine.length < ColumnIndexCSV.PORT_LON+1|| arrCSVLine[ColumnIndexCSV.PORT_LON]== null || arrCSVLine[ColumnIndexCSV.PORT_LON].isBlank() ) ? 0.00d :Double.parseDouble( arrCSVLine[ColumnIndexCSV.PORT_LON]));        
        //10 CostPerFULL   Container Moved
        Port_CostPerFullContainer = ((arrCSVLine.length < ColumnIndexCSV.COST_PER_FULL_CONTAINER_LOAD_UNLOAD+1 || arrCSVLine[ColumnIndexCSV.COST_PER_FULL_CONTAINER_LOAD_UNLOAD]== null || arrCSVLine[ColumnIndexCSV.COST_PER_FULL_CONTAINER_LOAD_UNLOAD].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndexCSV.COST_PER_FULL_CONTAINER_LOAD_UNLOAD]));
        //7 PortCallCost <-- BunnkerCost and Docking Charges
        PortCall_Cost = ((arrCSVLine.length < ColumnIndexCSV.PORTCALL_COSTFIX+1 || arrCSVLine[ColumnIndexCSV.PORTCALL_COSTFIX]== null || arrCSVLine[ColumnIndexCSV.PORTCALL_COSTFIX].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndexCSV.PORTCALL_COSTFIX]));
        
        //8 Port_FuelPrice;
        Port_FuelPrice = ((arrCSVLine.length < ColumnIndexCSV.PORT_FUEL_PRICE+1 || arrCSVLine[ColumnIndexCSV.PORT_FUEL_PRICE]== null || arrCSVLine[ColumnIndexCSV.PORT_FUEL_PRICE].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndexCSV.PORT_FUEL_PRICE]));
        //9 CanBunker  
        CanBunker = ((arrCSVLine.length < ColumnIndexCSV.CAN_BUNKER+1 || arrCSVLine[ColumnIndexCSV.CAN_BUNKER]== null || arrCSVLine[ColumnIndexCSV.CAN_BUNKER].isBlank() ) ? false : CoreFunctions.convertStringToBoolean(arrCSVLine[ColumnIndexCSV.CAN_BUNKER]));
        
        Penalty_LateArrival = ((arrCSVLine.length < ColumnIndexCSV.PENALTY_LATE_ARRIVAL_PER_HOUR+1 || arrCSVLine[ColumnIndexCSV.PENALTY_LATE_ARRIVAL_PER_HOUR]== null || arrCSVLine[ColumnIndexCSV.PENALTY_LATE_ARRIVAL_PER_HOUR].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndexCSV.PENALTY_LATE_ARRIVAL_PER_HOUR]));;
        
        Remarks = ((arrCSVLine.length < ColumnIndexCSV.REMARKS+1 || arrCSVLine[ColumnIndexCSV.REMARKS]== null || arrCSVLine[ColumnIndexCSV.REMARKS].isBlank() ) ? "" : arrCSVLine[ColumnIndexCSV.REMARKS]);;
        
        IsActive =  ((arrCSVLine.length < ColumnIndexCSV.ISACTIVE+1 || arrCSVLine[ColumnIndexCSV.ISACTIVE]== null || arrCSVLine[ColumnIndexCSV.ISACTIVE].isBlank() ) ? false : CoreFunctions.convertStringToBoolean(arrCSVLine[ColumnIndexCSV.ISACTIVE]));
        
        CreatedBy = ((arrCSVLine.length < ColumnIndexCSV.CREATEDBY+1 || arrCSVLine[ColumnIndexCSV.CREATEDBY]== null || arrCSVLine[ColumnIndexCSV.CREATEDBY].isBlank() ) ? 0 : Integer.parseInt(arrCSVLine[ColumnIndexCSV.CREATEDBY]));
             
        }
    

 
    private void initButton( IEventPortWaypoint IPortevent){
            button = new ButtonPort(); //takes care UI aspect, icon etc of the button waypoint
            button.addActionListener(new ActionListener() {
                //add an eventlistener to to the button of the waypoint being created, so that when we click on waypoint, something happens
                @Override
                public void actionPerformed(ActionEvent e) {
                 //inate functions of any JButton for an addActionListener is the function actionPerformed, inside this function
                 //is all the things that the system will do once this button for this waypoint is clicked.

                    // if this button is clicked :
                    IPortevent.selected(PortDTO.this);

                }
            });
     
    }
    
    public static enum PointType{
        START,END;
    }
    
    public class ColumnIndexCSV{
        static int PORTID               = 0;
        static int COUNTRY_ID           = 1;
        static int CABOTAGE_REGION_ID   = 2;
        static int REGION_ID            = 3;
        static int UNLOCODE             = 4;  //cvs
        static int PORTNAME             = 5;  //name  
        static int  PORT_LAT            = 6;
        static int  PORT_LON            = 7;
        static int PORTCALL_COSTFIX     = 8;
        static int PORT_FUEL_PRICE      = 9;
        static int CAN_BUNKER           = 10;
        static int COST_PER_FULL_CONTAINER_LOAD_UNLOAD = 11;
        static int PENALTY_LATE_ARRIVAL_PER_HOUR = 12;
        static int REMARKS = 13;
        static int ISACTIVE = 14;
        static int CREATEDBY = 15;
        
        static int LOAD_UNLOAD_TIME_PER_CONTAINER = 999;
        
    }

    @Override
    public String toString() {
        return this.getPortName();
    }
    
    
    
}

