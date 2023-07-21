/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import Core.*;
import java.lang.constant.DirectMethodHandleDesc;
import java.util.List;

/**
 *
 * @author Loo Alex
 * //Code,Description,IsActive,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate inherited from BaseDTO
 */
public class ShipDTO extends BaseDTO{
    public int ShipID;
    public String Code;
    public String Description;
    public int ShipCategoryID;
    
    //Data from ShipID
    public List <DemandDTO> Demands;//contains [RouteID which contains PortFromTo][DemandAmount][TotalWeightBaseOnContainerType]
    public float CurrentShipWeight;//Ton:  NoContainers*weight_Coeff + CurrentBunkerAmount(cuz in ton)
    public float CurrentBunkerAmount;//Ton
    public float SelectedSpeed;
    
    //Data from ShipCategoryID
    public int shipMaxCapacity; //TEU
    public float timeLoadUnloadPerFullContainer;
    public float weight_container_coeff;// should be 7.7f ->Wang2019
    
    public float designSpeed;
    public float minSpeed;
    public float maxSpeed;
    
    public float bunkerCapacity;
    public float idleBunkerConsumption;     //Ton per Day
    public float a_coeff_BunkerConsumption;
    public float g_coeff_BunkerConsumption;
    public float minBunkerAmount;//Ton
    
    //Canal fee?
    public float Cost_PanamaFee;
    public float Cost_SuezFee;
    
    
    //constant
    
    
    
    
}
