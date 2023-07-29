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
    private int containerTypeID;
    private String code;
    private String description;
    private Double avgUtilizeWeight; //TON
            

    public int getContainerTypeID() {
        return containerTypeID;
    }

    public void setContainerTypeID(int containerTypeID) {
        this.containerTypeID = containerTypeID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAvgUtilizeWeight() {
        return avgUtilizeWeight;
    }

    public void setAvgUtilizeWeight(Double avgUtilizeWeight) {
        this.avgUtilizeWeight = avgUtilizeWeight;
    }

    public ContainerTypeDTO(){
        
    }
    
    /**
     *
     * @param rs
     */
    public ContainerTypeDTO(ResultSet rs) throws SQLException{
        super(rs.getString("Remarks"), CoreFunctions.convertStringToBoolean(rs.getString("IsActive")),rs.getInt("CreatedBy"));
        containerTypeID = rs.getInt("ContainerType_ID");
        code = rs.getString("Code");
        description = rs.getString("Description");
        avgUtilizeWeight = rs.getDouble("AvgUtilizeWeight");
        
    }
    
    
}
