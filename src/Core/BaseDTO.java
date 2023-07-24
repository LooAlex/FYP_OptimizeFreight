/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.*;
import java.sql.*;
import java.text.DecimalFormat;

/**
 *
 * @author Loo Alex
 */
public class BaseDTO implements Serializable{
    public int RecordID;
    public String Code = "";        //ex: P001
    public String Description = ""; //Name -> Port-Louis

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
    
     

    public BaseDTO() {
    }

    public BaseDTO (String Remarks, boolean isActive, int CreatedBy){
        this.IsActive = isActive; 
        this.Remarks = Remarks;
        this.CreatedBy = CreatedBy;
    }
    
    public BaseDTO (String Code, String Description, String Remarks, boolean isActive, int CreatedBy){
        this.Code = Code;
        this.Description = Description;
        this.IsActive = isActive; 
        this.Remarks = Remarks;
        this.CreatedBy = CreatedBy;
    }
    
    public BaseDTO (int RecordID,String Code, String Description, String Remarks, boolean isActive, int CreatedBy){
        this.RecordID = RecordID;
        this.Code = Code;
        this.Description = Description;
        this.IsActive = isActive; 
        this.Remarks = Remarks;
        this.CreatedBy = CreatedBy;
    }
    
    public BaseDTO(ResultSet r) throws SQLException
    {
        this.Code = r.getString("Code");
        this.Description = r.getString("Description");
        this.IsActive = CoreFunctions.convertStringToBoolean(r.getString("IsActive"));
        this.Remarks = r.getString("Remarks");
        this.CreatedBy = r.getInt("CreatedBy");
    }

}
