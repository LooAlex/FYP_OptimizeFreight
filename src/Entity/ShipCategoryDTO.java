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
    public double CriticalBunkerLevel;              //DB preset CriticalBunkerLevel
    public double CriticalBunkerLevelNew;            //siv ::Ton //bunkerCapacity*0.05
    public double Penalty_ZeroBunker;               //PBkn :: $/TON
    
    //Simulation
    //public List <DemandDTO> Demands;//contains [RouteID which contains PortFromTo][DemandAmount][TotalWeightBaseOnContainerType]
    public double DistanceTravel;                      //Dij :: NauticalMile
    
    public Date StartingDate;
    public Date EndingDate;
    
    public double TimeHorizon;                      //T ::Hour
    public double weeklyFrequencyHour;                  //fw ::HOUR
    public double ETA;                              //T Estimate :: Hour = timeEnd+weeklyFrequency
    public double SelectedSpeed;                    //v nauticalMiles/hour
    public double timeTravel;                       //Tij :: Hour
    
    public double timeArrival;                      //tA :: Hour
    public double timeLeave;                        //tE :: Hour 
    public double timeLate;                         //TLate :: Hour
    
    public double TimeStartOper;                    //TS :: Hour | tA+0.5
    public double TotalOperationTime;               //ti ::HOUR
    public double TimeEndOper;                      //TE ::Hour | TS+ti
    
    public double BunkerLevelAfterOper;             //Avi and Avi-1 :: TON depends on when we use it
    public double BunkerLevelAtArrival;             //µvi :: TON
    public double CurrentBunkerAmount;              //bvi ::TON
    public double CurrentTotalBunkerHoldingCost;    //Hvi ::TON
    public double AvgCurrentBunkerHoldingCost;      //hvi ::TON
    public double Dept_Bunker;                      //dvi ::TON
   
    public double minAmountToBunkerUp;                //Yv ::TON
    public double minBunkerAmt;                      //yvi ::TON
    public double AmountBunkered;                   //Bvi ::TON, total fuel bunkered , random value between 20% and bunkerCapacity
    public boolean hasBunkered;                     // true false
    public int binaryBunkered;                      //Nvi {0,1} only.
    
    public double FuelConsumedIdle;                 //FIdle ::TON
    public double FuelConsumedTraveled;              //FTravel ::TON
    
    public int TotalAmountContainerCarried;         //Lvig = (Lvi +TotalDemands) ::number
    public double CurrentShipPayload;               //Wij ::Ton  NoContainers*weight_Coeff + CurrentBunkerAmount(cuz in ton)
    public double totalLoadUnLoad;
    
    public String previousPortName;
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
        CriticalBunkerLevelNew = this.BunkerCapacity * 0.05;
        minBunkerAmt =this.BunkerCapacity * 0.15;
        minAmountToBunkerUp = this.BunkerCapacity *0.20;
        
    }
    
    //deep copy
    public ShipCategoryDTO(ShipCategoryDTO ship){
        super(ship.Code,ship.Description,ship.Remarks,ship.IsActive,ship.CreatedBy);
        ShipCategory_ID = ship.ShipCategory_ID;
        CapacityTEU = ship.CapacityTEU;
        CapacityFEU = ship.CapacityFEU;
        MinSpeed = ship.MinSpeed;
        MaxSpeed =ship.MaxSpeed;
        DesignSpeed = ship.DesignSpeed;
        Coeff_FuelIdle = ship.Coeff_FuelIdle;	
        Coeff_Alpha = ship.Coeff_Alpha;
        Coeff_Beta = ship.Coeff_Beta;
        Coeff_FuelTravel= ship.Coeff_FuelTravel;
        BunkerCapacity = ship.BunkerCapacity;	
        CriticalBunkerLevel = ship.CriticalBunkerLevel;
        TimeLoadUnLoadPerFullContainerTEU = ship.TimeLoadUnLoadPerFullContainerTEU;
        TimeLoadUnLoadPerFullContainerFEU = ship.TimeLoadUnLoadPerFullContainerFEU;
        Penalty_ZeroBunker = ship.Penalty_ZeroBunker;
        weeklyFrequencyHour = ship.weeklyFrequencyHour;
        AvgWeightUtilizeContainer = ship.AvgWeightUtilizeContainer;
        SpeedOptions = new ArrayList<>(Arrays.asList(new Double[]{DesignSpeed,MinSpeed,MaxSpeed}));
        TimeHorizon = ship.TimeHorizon;
        CriticalBunkerLevelNew = this.BunkerCapacity * 0.05;
        minBunkerAmt =this.BunkerCapacity * 0.15;
        minAmountToBunkerUp = this.BunkerCapacity *0.20;
        
        this.DistanceTravel= ship.DistanceTravel;                      //Dij :: NauticalMile
   
    
        this.ETA = ship.ETA;                              //T Estimate :: Hour = timeEnd+weeklyFrequency
        this.SelectedSpeed = ship.SelectedSpeed;                    //v nauticalMiles/hour
        this.timeTravel = ship.timeTravel;                       //Tij :: Hour

        this.timeArrival = ship.timeArrival;                      //tA :: Hour
        this.timeLeave = ship.timeLeave;                        //tE :: Hour 
        this.timeLate = ship.timeLate;                         //TLate :: Hour

        this.TimeStartOper = ship.TimeStartOper;                    //TS :: Hour | tA+0.5
        this.TotalOperationTime = ship.TotalOperationTime;               //ti ::HOUR
        this.TimeEndOper = ship.TimeEndOper;                      //TE ::Hour | TS+ti

        this.BunkerLevelAfterOper = ship.BunkerLevelAfterOper;             //Avi and Avi-1 :: TON depends on when we use it
        this.BunkerLevelAtArrival = ship.BunkerLevelAtArrival;             //µvi :: TON
        this.CurrentBunkerAmount = ship.CurrentBunkerAmount;              //bvi ::TON
        this.CurrentTotalBunkerHoldingCost = ship.CurrentTotalBunkerHoldingCost;    //Hvi ::TON
        this.AvgCurrentBunkerHoldingCost = ship.AvgCurrentBunkerHoldingCost;      //hvi ::TON
        this.Dept_Bunker = ship.Dept_Bunker;                      //dvi ::TON

        this.AmountBunkered = ship.AmountBunkered;                   //Bvi ::TON, total fuel bunkered , random value between 20% and bunkerCapacity

        this.FuelConsumedIdle = ship.FuelConsumedIdle;                 //FIdle ::TON
        this.FuelConsumedTraveled = ship.FuelConsumedTraveled;              //FTravel ::TON

        this.TotalAmountContainerCarried = ship.TotalAmountContainerCarried;         //Lvig = (Lvi +TotalDemands) ::number
        this.CurrentShipPayload = ship.CurrentShipPayload;               //Wij ::Ton  NoContainers*weight_Coeff + CurrentBunkerAmount(cuz in ton)
        this.totalLoadUnLoad = ship.totalLoadUnLoad;
        this.previousPortName = ship.previousPortName;
        
    }
    @Override
    public String toString() {
        return this.Code;
    
    
    }
    
    
}
