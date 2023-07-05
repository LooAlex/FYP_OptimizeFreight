/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import Core.*;
import Entity.GUI_Entity.Waypoint.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.OffsetDateTime;

import javax.swing.JButton;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

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
    
    private static enum PointType{
        START,END;
    }
        
    public Port(){};
    
    public Port (String Code, String Description,PointType pointType,IEventPortWaypoint IPortevent, GeoPosition coord){
        super(coord);
        this.Code = Code;
        this.Description = Description;   
        this.pointType = pointType;
        initButton(IPortevent);  //did not have this code before<-- 29/06/23
    }
    // <editor-fold defaultstate="collapsed" desc="PortSearchDTO">
    public class PortSearchDTO {

    }
    // </editor-fold>
    

     private void initButton( IEventPortWaypoint IPortevent){
        button = new ButtonWaypoint(); //takes care UI aspect, icon etc of the button waypoint
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
    
    


    
}

