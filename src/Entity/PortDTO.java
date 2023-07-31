/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;
import java.sql.*;
import Entity.GUI_Entity.GUI_Port.IEventPortWaypoint;
import Core.*;
import Entity.GUI_Entity.GUI_Port.ButtonPort;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JButton;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author Loo Alex
 //PortDTO extends DefaultWaypoint
 //unLoCode,portName,isActive,createdBy,createdDate,modifiedBy,modifiedDate inherited from BaseDTO
 */
public class PortDTO extends DefaultWaypoint {

    public int portID;

    public int countryID;                   //Country
    public String countryName; 
    
    public int cabotageRegionID;         //CabotageRegion
    public String cabotageRegionName;
    
    public int regionID;                    //D_region
    public String regionName;               

    public String unLoCode = "";            //ex: P001 //UnLocode
    public String portName = "";         //PortName -> PortDTO-Louis

    public double portLat;                  //Latitude
    public double portLon;                  //Longitude
    
    //--PortCall
    public double portCall_Cost ;        //Both BunkerPrice and DockingCost
    
    //--Fuel
    public double port_FuelPrice ;
    public boolean canBunker;
    
    //--Containters
    public double port_CostPerFullContainer;   
    
    //--Penalty
    public double penalty_LateArrival;
    

    //--Ship
    public ShipCategoryDTO currentShip;
    
    public double shp_PortTimeTaken;
    public double shp_chosenSpeed;
 
    public DemandDTO demands;
    
    //ObjetiveFunction var
    public double totalHandlingCost;        //CHdl :: $ |(DemandAmt+SupplyAmnt)*port_CostPerFullContainer
    public double totalFuelTravelCost;      //Ctravel :: $ |previous port i to this port j
    public double totalFuelIdleCost;        //CFIdle :: $ |for this port i, for duration of operation
    public double totalPenaltyCost;         //CPnt :: $ |
    public double totalValueFuelLeft_FinalHvi;
    public double totalOperationalCost;
    
    //--
    public  String remarks = "";
    public boolean isActive;

    public int createdBy;
    public OffsetDateTime createdDate;
    public int modifiedBy;
    public OffsetDateTime modifiedDate;
    public int recordID;//general ID incase we need a general ID to use across tables.
    
    //Waypoint
    private JButton button;
    private PointType pointType;

    public int getPortID() {
        return portID;
    }

    public void setPortID(int portID) {
        this.portID = portID;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getCabotageRegionID() {
        return cabotageRegionID;
    }

    public void setCabotageRegionID(int cabotageRegionID) {
        this.cabotageRegionID = cabotageRegionID;
    }

    public String getCabotageRegionName() {
        return cabotageRegionName;
    }

    public void setCabotageRegionName(String cabotageRegionName) {
        this.cabotageRegionName = cabotageRegionName;
    }

    public int getRegionID() {
        return regionID;
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getUnLoCode() {
        return unLoCode;
    }

    public void setUnLoCode(String unLoCode) {
        this.unLoCode = unLoCode;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public double getPortLat() {
        return portLat;
    }

    public void setPortLat(double portLat) {
        this.portLat = portLat;
    }

    public double getPortLon() {
        return portLon;
    }

    public void setPortLon(double portLon) {
        this.portLon = portLon;
    }

    public double getPortCall_Cost() {
        return portCall_Cost;
    }

    public void setPortCall_Cost(double portCall_Cost) {
        this.portCall_Cost = portCall_Cost;
    }

    public double getPort_FuelPrice() {
        return port_FuelPrice;
    }

    public void setPort_FuelPrice(double port_FuelPrice) {
        this.port_FuelPrice = port_FuelPrice;
    }

    public boolean isCanBunker() {
        return canBunker;
    }

    public void setCanBunker(boolean canBunker) {
        this.canBunker = canBunker;
    }

    public double getPort_CostPerFullContainer() {
        return port_CostPerFullContainer;
    }

    public void setPort_CostPerFullContainer(double port_CostPerFullContainer) {
        this.port_CostPerFullContainer = port_CostPerFullContainer;
    }

    public double getPenalty_LateArrival() {
        return penalty_LateArrival;
    }

    public void setPenalty_LateArrival(double penalty_LateArrival) {
        this.penalty_LateArrival = penalty_LateArrival;
    }

    public ShipCategoryDTO getCurrentShip() {
        return currentShip;
    }

    public void setCurrentShip(ShipCategoryDTO currentShip) {
        this.currentShip = currentShip;
    }

    public double getShp_PortTimeTaken() {
        return shp_PortTimeTaken;
    }

    public void setShp_PortTimeTaken(double shp_PortTimeTaken) {
        this.shp_PortTimeTaken = shp_PortTimeTaken;
    }

    public double getShp_chosenSpeed() {
        return shp_chosenSpeed;
    }

    public void setShp_chosenSpeed(double shp_chosenSpeed) {
        this.shp_chosenSpeed = shp_chosenSpeed;
    }

    public DemandDTO getDemands() {
        return demands;
    }

    public void setDemands(DemandDTO demands) {
        this.demands = demands;
    }

    public double getTotalHandlingCost() {
        return totalHandlingCost;
    }

    public void setTotalHandlingCost(double totalHandlingCost) {
        this.totalHandlingCost = totalHandlingCost;
    }

    public double getTotalFuelTravelCost() {
        return totalFuelTravelCost;
    }

    public void setTotalFuelTravelCost(double totalFuelTravelCost) {
        this.totalFuelTravelCost = totalFuelTravelCost;
    }

    public double getTotalFuelIdleCost() {
        return totalFuelIdleCost;
    }

    public void setTotalFuelIdleCost(double totalFuelIdleCost) {
        this.totalFuelIdleCost = totalFuelIdleCost;
    }

    public double getTotalPenaltyCost() {
        return totalPenaltyCost;
    }

    public void setTotalPenaltyCost(double totalPenaltyCost) {
        this.totalPenaltyCost = totalPenaltyCost;
    }

    public double getTotalValueFuelLeft_FinalHvi() {
        return totalValueFuelLeft_FinalHvi;
    }

    public void setTotalValueFuelLeft_FinalHvi(double totalValueFuelLeft_FinalHvi) {
        this.totalValueFuelLeft_FinalHvi = totalValueFuelLeft_FinalHvi;
    }

    public double getTotalOperationalCost() {
        return totalOperationalCost;
    }

    public void setTotalOperationalCost(double totalOperationalCost) {
        this.totalOperationalCost = totalOperationalCost;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public OffsetDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(OffsetDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public PointType getPointType() {
        return pointType;
    }

    public void setPointType(PointType pointType) {
        this.pointType = pointType;
    }
    
    
        
    public PortDTO(){};
    
    public PortDTO (String UnLoCode, String Description,PointType pointType,IEventPortWaypoint IPortevent, GeoPosition coord){
        super(coord);
        this.unLoCode = UnLoCode;
        this.portName = Description;   
        this.pointType = pointType;
        initButton(IPortevent);  //did not have this code before<-- 29/06/23
        demands = new DemandDTO();
    }
    public PortDTO(ResultSet rs,IEventPortWaypoint IPortEvent) throws SQLException{
        super(new GeoPosition(
                rs.getDouble("Latitude"), 
                rs.getDouble("Longitude")));
        
        initButton(IPortEvent); //mantatody in all PortDTO events
        portID = rs.getInt("Port_ID");
        countryID = rs.getInt("CountryID");
        cabotageRegionID = rs.getInt("CabotageRegionID");
        regionID = rs.getInt("RegionID");
        
        unLoCode = rs.getString("UNLocode");
        portName = rs.getString("PortName");
        portLat = rs.getDouble("Latitude");
        portLon = rs.getDouble("Longitude");
        
        portCall_Cost = rs.getDouble("PortCallCost");
        
        port_FuelPrice = rs.getDouble("PortFuelPrice");
        canBunker = CoreFunctions.convertStringToBoolean(rs.getString("CanBunker"));
        
        port_CostPerFullContainer = rs.getDouble("CostPerFullContainerLoadUnLoad");
        penalty_LateArrival = rs.getDouble("PenaltyLateArrival");
        
        remarks = rs.getString("Remarks");
        isActive = CoreFunctions.convertStringToBoolean(rs.getString("IsActive"));
        createdBy = rs.getInt("CreatedBy");
       
        currentShip = new ShipCategoryDTO();
          
    }
    
    public PortDTO(PortDTO port) {

        portID = port.portID;
        countryID = port.countryID;
        cabotageRegionID = port.cabotageRegionID;
        regionID = port.regionID;
        
        unLoCode = port.unLoCode;
        portName = port.portName;
        portLat = port.portLat;
        portLon = port.portLon;
        
        portCall_Cost = port.portCall_Cost;
        
        port_FuelPrice = port.port_FuelPrice;
        canBunker = port.canBunker;
        
        port_CostPerFullContainer = port.port_CostPerFullContainer;
        penalty_LateArrival = port.penalty_LateArrival;
       
        demands = port.demands;
        
        //--Ship
        currentShip = new ShipCategoryDTO(port.currentShip);
    
        shp_PortTimeTaken = port.shp_PortTimeTaken;
        shp_chosenSpeed = port.shp_chosenSpeed;

        //ObjetiveFunction var
        totalHandlingCost  = port.totalHandlingCost;        //CHdl :: $ |(DemandAmt+SupplyAmnt)*port_CostPerFullContainer
        totalFuelTravelCost = port.totalFuelTravelCost;      //Ctravel :: $ |previous port i to this port j
        totalFuelIdleCost  = port.totalFuelIdleCost;        //CFIdle :: $ |for this port i, for duration of operation
        totalPenaltyCost = port.totalPenaltyCost;         //CPnt :: $ |
        totalOperationalCost = port.totalOperationalCost;
    
        remarks = port.remarks;
        isActive = port.isActive;
        createdBy = port.createdBy;

    }
    
    public PortDTO (String[]arrCSVLine,IEventPortWaypoint IPortEvent){
        //super(coords)
        super(new GeoPosition( 
                ((arrCSVLine[ColumnIndexCSV.PORT_LAT]== null || arrCSVLine.length < ColumnIndexCSV.PORT_LAT+1 || arrCSVLine[ColumnIndexCSV.PORT_LAT].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndexCSV.PORT_LAT])) ,
                ((arrCSVLine[ColumnIndexCSV.PORT_LON]== null || arrCSVLine.length < ColumnIndexCSV.PORT_LON+1 || arrCSVLine[ColumnIndexCSV.PORT_LON].isBlank() ) ? 0.00d : Double.parseDouble( arrCSVLine[ColumnIndexCSV.PORT_LON])) 
        ));
        
        initButton(IPortEvent); //mantatody in all PortDTO events
        portID = ((arrCSVLine.length < ColumnIndexCSV.PORTID+1 || arrCSVLine[ColumnIndexCSV.PORTID]== null || arrCSVLine[ColumnIndexCSV.PORTID].isBlank() ) ? 0 : Integer.parseInt(arrCSVLine[ColumnIndexCSV.PORTID]));
         //0 UnLocode
        unLoCode = ((arrCSVLine.length < ColumnIndexCSV.UNLOCODE+1 || arrCSVLine[ColumnIndexCSV.UNLOCODE]== null || arrCSVLine[ColumnIndexCSV.UNLOCODE].isBlank() ) ? "" : arrCSVLine[ColumnIndexCSV.UNLOCODE]);
        //1 name
        portName = ((arrCSVLine.length < ColumnIndexCSV.PORTNAME+1 || arrCSVLine[ColumnIndexCSV.PORTNAME]== null || arrCSVLine[ColumnIndexCSV.PORTNAME].isBlank() ) ? "" : arrCSVLine[ColumnIndexCSV.PORTNAME]);
        //2 countryID     
        countryID = ((arrCSVLine.length < ColumnIndexCSV.COUNTRY_ID+1 || arrCSVLine[ColumnIndexCSV.COUNTRY_ID]== null || arrCSVLine[ColumnIndexCSV.COUNTRY_ID].isBlank() ) ? 0 : Integer.parseInt(arrCSVLine[ColumnIndexCSV.COUNTRY_ID]));
        //3 cabotageRegionID
        cabotageRegionID = ((arrCSVLine.length < ColumnIndexCSV.CABOTAGE_REGION_ID+1 || arrCSVLine[ColumnIndexCSV.CABOTAGE_REGION_ID]== null || arrCSVLine[ColumnIndexCSV.CABOTAGE_REGION_ID].isBlank() ) ? 0 : Integer.parseInt(arrCSVLine[ColumnIndexCSV.CABOTAGE_REGION_ID]));        
        //4 D_RegionID
        regionID = ((arrCSVLine.length < ColumnIndexCSV.REGION_ID+1 || arrCSVLine[ColumnIndexCSV.REGION_ID]== null || arrCSVLine[ColumnIndexCSV.REGION_ID].isBlank() ) ? 0 : Integer.parseInt(arrCSVLine[ColumnIndexCSV.REGION_ID]));
        //5 Latitude
        portLat  = ((arrCSVLine.length < ColumnIndexCSV.PORT_LAT+1 || arrCSVLine[ColumnIndexCSV.PORT_LAT]== null || arrCSVLine[ColumnIndexCSV.PORT_LAT].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndexCSV.PORT_LAT])); 
        //6 Longitude
        portLon  = ((arrCSVLine.length < ColumnIndexCSV.PORT_LON+1|| arrCSVLine[ColumnIndexCSV.PORT_LON]== null || arrCSVLine[ColumnIndexCSV.PORT_LON].isBlank() ) ? 0.00d :Double.parseDouble( arrCSVLine[ColumnIndexCSV.PORT_LON]));        
        //10 CostPerFULL   Container Moved
        port_CostPerFullContainer = ((arrCSVLine.length < ColumnIndexCSV.COST_PER_FULL_CONTAINER_LOAD_UNLOAD+1 || arrCSVLine[ColumnIndexCSV.COST_PER_FULL_CONTAINER_LOAD_UNLOAD]== null || arrCSVLine[ColumnIndexCSV.COST_PER_FULL_CONTAINER_LOAD_UNLOAD].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndexCSV.COST_PER_FULL_CONTAINER_LOAD_UNLOAD]));
        //7 PortCallCost <-- BunnkerCost and Docking Charges
        portCall_Cost = ((arrCSVLine.length < ColumnIndexCSV.PORTCALL_COSTFIX+1 || arrCSVLine[ColumnIndexCSV.PORTCALL_COSTFIX]== null || arrCSVLine[ColumnIndexCSV.PORTCALL_COSTFIX].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndexCSV.PORTCALL_COSTFIX]));
        
        //8 port_FuelPrice;
        port_FuelPrice = ((arrCSVLine.length < ColumnIndexCSV.PORT_FUEL_PRICE+1 || arrCSVLine[ColumnIndexCSV.PORT_FUEL_PRICE]== null || arrCSVLine[ColumnIndexCSV.PORT_FUEL_PRICE].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndexCSV.PORT_FUEL_PRICE]));
        //9 canBunker  
        canBunker = ((arrCSVLine.length < ColumnIndexCSV.CAN_BUNKER+1 || arrCSVLine[ColumnIndexCSV.CAN_BUNKER]== null || arrCSVLine[ColumnIndexCSV.CAN_BUNKER].isBlank() ) ? false : CoreFunctions.convertStringToBoolean(arrCSVLine[ColumnIndexCSV.CAN_BUNKER]));
        
        penalty_LateArrival = ((arrCSVLine.length < ColumnIndexCSV.PENALTY_LATE_ARRIVAL_PER_HOUR+1 || arrCSVLine[ColumnIndexCSV.PENALTY_LATE_ARRIVAL_PER_HOUR]== null || arrCSVLine[ColumnIndexCSV.PENALTY_LATE_ARRIVAL_PER_HOUR].isBlank() ) ? 0.00d : Double.parseDouble(arrCSVLine[ColumnIndexCSV.PENALTY_LATE_ARRIVAL_PER_HOUR]));
        
        remarks = ((arrCSVLine.length < ColumnIndexCSV.REMARKS+1 || arrCSVLine[ColumnIndexCSV.REMARKS]== null || arrCSVLine[ColumnIndexCSV.REMARKS].isBlank() ) ? "" : arrCSVLine[ColumnIndexCSV.REMARKS]);
        
        isActive =  ((arrCSVLine.length < ColumnIndexCSV.ISACTIVE+1 || arrCSVLine[ColumnIndexCSV.ISACTIVE]== null || arrCSVLine[ColumnIndexCSV.ISACTIVE].isBlank() ) ? false : CoreFunctions.convertStringToBoolean(arrCSVLine[ColumnIndexCSV.ISACTIVE]));
        
        createdBy = ((arrCSVLine.length < ColumnIndexCSV.CREATEDBY+1 || arrCSVLine[ColumnIndexCSV.CREATEDBY]== null || arrCSVLine[ColumnIndexCSV.CREATEDBY].isBlank() ) ? 0 : Integer.parseInt(arrCSVLine[ColumnIndexCSV.CREATEDBY]));
             
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
    
    public Object[] convertListPortToObjectArray(){
        
        
        

        DecimalFormat df = CoreFunctions.getDecimalFormat(2);
                
        String PortID               = this.portID+"";
        String countryID            = this.countryID+"";
        String cabotageRegionID     = this.cabotageRegionID+"";
        String regionID             = this.regionID+"";
        String unLocode             = this.unLoCode ;
        String portName             = this.portName ;

        String portLat              = Double.toString(portLon);
        String portLon              = Double.toString(this.portLon );
        
        String portCallCost         = Double.toString(this.portCall_Cost);
       
        String port_FuelPrice          = Double.toString(this.port_FuelPrice  );
        boolean canBunker           = this.canBunker;
        String port_CostPerFullContainer  = Double.toString(this.port_CostPerFullContainer );
        String penalty_LateArrival  = Double.toString(this.penalty_LateArrival );

        String Remarks              = this.remarks ;
        boolean IsActive            = this.isActive  ;
        String CreatedBy            = this.createdBy+"";
                    
        
        return new Object[]{
            PortID,
            countryID,cabotageRegionID,regionID,
            unLocode,portName,portLat,portLon,
            portCallCost,
            port_FuelPrice,canBunker,
            port_CostPerFullContainer,
            penalty_LateArrival,
            Remarks,IsActive,CreatedBy
        };
    }
}

