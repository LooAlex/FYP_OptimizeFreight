/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;
import Core.*;

/**
 *
 * @author Loo Alex
 */
public class DemandDTO extends BaseDTO{
    public int DemandAmt;
    public int SupplyAmt;
    public int ContainerTypeID;
    public float AvgWeight_Ton;
    
    public DemandDTO(){
        DemandAmt = CoreFunctions.randomInt(0, 5000);
        SupplyAmt = CoreFunctions.randomInt(0, 5000);
    }
    
    //Demand base on Ship Size
    public DemandDTO(int shp_Size){
        DemandAmt = CoreFunctions.randomInt(0, shp_Size);
        SupplyAmt = CoreFunctions.randomInt(0, shp_Size);
    }
    
    //specific Demand amount
    public DemandDTO(int DemandAmt,int SupplyAmt){
        this.DemandAmt = DemandAmt;
        this.SupplyAmt = SupplyAmt;
    }
}
