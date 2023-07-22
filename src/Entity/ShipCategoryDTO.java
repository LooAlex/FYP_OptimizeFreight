/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import Core.*;
import java.time.*;
import java.lang.constant.DirectMethodHandleDesc;
import java.math.BigDecimal;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.DoubleStream;
/**
 *
 * @author Loo Alex
 * //Code,Description,IsActive,CreatedBy,CreatedDate,ModifiedBy,ModifiedDate inherited from BaseDTO
 */
public class ShipCategoryDTO extends BaseDTO{
    //DB
    public int ShipCategory_ID; 
    //Code 
    //Description in Super
    
    public int CapacityTEU;     //Wg ::TEU
    public int CapacityFEU;     //Wg ::FEU
    public double TimeLoadUnLoadPerFullContainerTEU;    //ρvig :: Hour | 
    public double TimeLoadUnLoadPerFullContainerFEU;
    public double AvgWeightUtilizeContainer;            //Wg :: TON | should be 7.7f ->Wang2019
    
    public double DesignSpeed;     
    public double MinSpeed;
    public double MaxSpeed;
    public ArrayList<Double> SpeedOptions;          
    
    public double Coeff_FuelIdle;                   //fIdle Ton per Day
    public double Coeff_Alpha;                      //α
    public double Coeff_Beta;                       //β
    public double Coeff_FuelTravel;                 //Fe 
    public double BunkerCapacity;                   //Wv ::TON
    public double CriticalBunkerLevel;              //siv ::Ton //bunkerCapacity*0.05, or use CriticalBunkerLevel
        
    public double Penalty_ZeroBunker;               //PBkn :: $/TON
    
    //Simulation
    //public List <DemandDTO> Demands;//contains [RouteID which contains PortFromTo][DemandAmount][TotalWeightBaseOnContainerType]
    public int CurrentPortID;                       //i
    public int PreviousPortID;                      //i-1
    public int DistanceTravel;                      //Dij :: NauticalMile
    
    public Date StartingDate;
    public Date EndingDate;
    
    public double TimeHorizon;                      //T ::Hour
    public double weeklyFrequency;                  //fw ::HOUR
    public double ETA;                              //T Estimate :: Hour = timeEnd+weeklyFrequency
    public double SelectedSpeed;                    //v nauticalMiles/hour
    public double TravelTime;                       //Tij :: Hour
    
    public double timeArrival;                      //tA :: Hour
    public double timeEnd;                          //tE :: Hour 
    public double timeLate;                         //TLate :: Hour
    
    public double TimeStartOper;                    //Ts :: Hour | tA+0.5
    public double TotalOperationTime;               //ti ::HOUR
    public double TimeEndOper;                      //TE ::Hour | Ts+ti
    
    public double BunkerLevelAfterOper;             //Avi and Avi-1 :: TON depends on when we use it
    public double BunkerLevelAtArrival;             //µvi
    public double CurrentBunkerAmount;              //bvi ::TON
    public double CurrentTotalBunkerHoldingCost;    //Hvi ::TON
    public double AvgCurrentBunkerHoldingCost;      //hvi ::TON
    public double Dept_Bunker;                      //dvi ::TON
    public double AmountBunkered;                   //Bvi ::TON, total fuel bunkered , random value between 20% and bunkerCapacity
    public boolean hasBunkered;                     // true false
    public int binaryBunkered;                      //Nvi {0,1} only.
    
    public int TotalAmountContainerCarried;         //Lvig = (Lvi +TotalDemands) ::number
    public double CurrentShipPayload;               //Wij ::Ton  NoContainers*weight_Coeff + CurrentBunkerAmount(cuz in ton)
    
    
    public ShipCategoryDTO(){
        
    }
    
    public ShipCategoryDTO(ResultSet rs)throws SQLException{
        super(rs);
        ShipCategory_ID = rs.getInt("ShipCategory_ID");
        CapacityTEU = rs.getInt("CapacityTEU");
        CapacityFEU = rs.getInt("CapacityFEU");
        MinSpeed = rs.getDouble("MinSpeed");
        MaxSpeed = rs.getDouble("MaxSpeed");
        DesignSpeed = rs.getDouble("DesignSpeed");
        Coeff_FuelIdle = rs.getDouble("Coeff_FuelIdle");	
        Coeff_Alpha = rs.getDouble("Coeff_Alpha");
        Coeff_Beta = rs.getDouble("Coeff_Beta");
        Coeff_FuelTravel= rs.getDouble("Coeff_FuelTravel");
        BunkerCapacity = rs.getDouble("BunkerCapacity");	
        CriticalBunkerLevel = rs.getDouble("CriticalBunkerLevel");
        TimeLoadUnLoadPerFullContainerTEU = rs.getDouble("TimeLoadUnLoadPerFullContainerTEU");
        TimeLoadUnLoadPerFullContainerFEU = rs.getDouble("TimeLoadUnLoadPerFullContainerFEU");
        Penalty_ZeroBunker = rs.getDouble("Penalty_ZeroBunker");
       
        SpeedOptions = new ArrayList<>(Arrays.asList(new Double[]{DesignSpeed,MinSpeed,MaxSpeed}));
        TimeHorizon = 0;
        
    }
    
    
    
    
    
}
