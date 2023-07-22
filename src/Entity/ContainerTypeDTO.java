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
public class ContainerTypeDTO extends BaseDTO {

    public int getContainerTypeID() {
        return ContainerTypeID;
    }

    public void setContainerTypeID(int ContainerTypeID) {
        this.ContainerTypeID = ContainerTypeID;
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

    public Double getAvgUtilizeWeight() {
        return AvgUtilizeWeight;
    }

    public void setAvgUtilizeWeight(Double AvgUtilizeWeight) {
        this.AvgUtilizeWeight = AvgUtilizeWeight;
    }
    
    public int ContainerTypeID;
    public String Code;
    public String Description;
    public Double AvgUtilizeWeight; //TON
            
    
    public ContainerTypeDTO(){
        
    }
    
    /**
     *
     * @param rs
     */
    public ContainerTypeDTO(ResultSet rs) throws SQLException{
        super(rs.getString("Remarks"), CoreFunctions.convertStringToBoolean(rs.getString("IsActive")),rs.getInt("CreatedBy"));
        ContainerTypeID = rs.getInt("ContainerType_ID");
        Code = rs.getString("Code");
        Description = rs.getString("Description");
        AvgUtilizeWeight = rs.getDouble("AvgUtilizeWeight");
        
    }
    
    
}
