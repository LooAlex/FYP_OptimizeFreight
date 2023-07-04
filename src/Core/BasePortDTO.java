/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import java.time.OffsetDateTime;
import org.jxmapviewer.viewer.DefaultWaypoint;

/**
 *
 * @author Loo Alex
 */
public class BasePortDTO extends DefaultWaypoint{
    
    public int RecordID;
    public String Code = "";        //ex: P001
    public String Description = ""; //Name -> Port-Louis

    public double PortLat;
    public double PortLon;
    public double Port_FuelPrice ;
    
    public  String Remarks = "";
    public boolean IsActive;
    
    public int CreatedBy;
    public OffsetDateTime CreatedDate;
    public int ModifiedBy;
    public OffsetDateTime ModifiedDate;

    public int getRecordID() {
        return RecordID;
    }

    public void setRecordID(int RecordID) {
        this.RecordID = RecordID;
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

    public double getPort_FuelPrice() {
        return Port_FuelPrice;
    }

    public void setPort_FuelPrice(double Port_FuelPrice) {
        this.Port_FuelPrice = Port_FuelPrice;
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
    
      public static enum PortPointType {
        START, END; //Origin, Destination
    }
}
