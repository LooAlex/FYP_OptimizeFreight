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
import java.text.DecimalFormat;
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
    public int shipCategory_ID; 
    //Code 
    //Description in Super
    
    public int capacityTEU;     //Wg ::TEU
    public int capacityFEU;     //Wg ::FEU
    public double timeLoadUnLoadPerFullContainerTEU;    //ρvig :: Hour | 
    public double timeLoadUnLoadPerFullContainerFEU;
    public double avgWeightUtilizeContainer;            //Wg :: TON | should be 7.7f ->Wang2019
    
    public double DesignSpeed;     
    public double MinSpeed;
    public double MaxSpeed;
    public ArrayList<Double> SpeedOptions;          
    
    public double Coeff_FuelIdle;                   //fIdle Ton per Day
    public double Coeff_Alpha;                      //α
    public double Coeff_Beta;                       //β
    public double Coeff_FuelTravel;                 //Fe 
    
    public double Coeff_c_speed;                    //Coeff k1 from Yao
    public double Coeff_c_weight;                   //Coef k2 from Yao  F = Dij/24*k1V^2+k2
    
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
    public boolean isLate;
    
    public double TimeStartOper;                    //TS :: Hour | tA+0.5
    public double TotalOperationTime;               //ti ::HOUR
    public double TimeEndOper;                      //TE ::Hour | TS+ti
    
    public double BunkerLevelAfterOper;             //Avi and Avi-1 :: TON depends on when we use it
    public double BunkerLevelAtArrival;             //µvi :: TON
    public double CurrentBunkerAmount;              //bvi ::TON
    public double CurrentTotalBunkerHoldingCost;    //Hvi ::TON
    public double AvgCurrentBunkerHoldingCost;      //hvi ::TON
    
    public double Dept_Bunker;                      //dvi ::TON
    public boolean hasNoFuel;
   
    public double minAmountToBunkerUp;                //Yv ::TON
    public double minBunkerAmt;                      //yvi ::TON
    
    public double AmountBunkered;                   //Bvi ::TON, total fuel bunkered , random value between 20% and bunkerCapacity
    public boolean hasBunkered;                     // true false
    
    public double FuelConsumedIdle;                 //FIdle ::TON
    public double FuelConsumedTraveled;              //FTravel ::TON
    
    public int TotalAmountContainerCarried;         //Lvig = (Lvi +TotalDemands) ::number
    public double AmountLoaded;
    public double AmountUnloaded;
    public double CurrentShipPayload;               //Wij ::Ton  NoContainers*weight_Coeff + CurrentBunkerAmount(cuz in ton)
    public double totalLoadUnload;
    
  
    
    
    public String previousPortName;
    public ShipCategoryDTO(){
        
    }
    
    public ShipCategoryDTO(ResultSet rs)throws SQLException{
        super(rs);
        shipCategory_ID = rs.getInt("ShipCategory_ID");
        capacityTEU = rs.getInt("CapacityTEU");
        capacityFEU = rs.getInt("CapacityFEU");
        MinSpeed = rs.getDouble("MinSpeed");
        MaxSpeed = rs.getDouble("MaxSpeed");
        DesignSpeed = rs.getDouble("DesignSpeed");
        Coeff_FuelIdle = rs.getDouble("Coeff_FuelIdle");	
        Coeff_Alpha = rs.getDouble("Coeff_Alpha");
        Coeff_Beta = rs.getDouble("Coeff_Beta");
        Coeff_FuelTravel= rs.getDouble("Coeff_FuelTravel");
        
        Coeff_c_speed= rs.getDouble("Coeff_c_speed");
        Coeff_c_weight= rs.getDouble("Coeff_c_weight");
        
        BunkerCapacity = rs.getDouble("BunkerCapacity");	
        CriticalBunkerLevel = rs.getDouble("CriticalBunkerLevel");
        timeLoadUnLoadPerFullContainerTEU = rs.getDouble("TimeLoadUnLoadPerFullContainerTEU");
        timeLoadUnLoadPerFullContainerFEU = rs.getDouble("TimeLoadUnLoadPerFullContainerFEU");
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
        shipCategory_ID = ship.shipCategory_ID;
        capacityTEU = ship.capacityTEU;
        capacityFEU = ship.capacityFEU;
        MinSpeed = ship.MinSpeed;
        MaxSpeed =ship.MaxSpeed;
        DesignSpeed = ship.DesignSpeed;
        Coeff_FuelIdle = ship.Coeff_FuelIdle;	
        Coeff_Alpha = ship.Coeff_Alpha;
        Coeff_Beta = ship.Coeff_Beta;
                
        Coeff_c_speed= ship.Coeff_c_speed;
        Coeff_c_weight= ship.Coeff_c_weight;
        
        Coeff_FuelTravel= ship.Coeff_FuelTravel;
        BunkerCapacity = ship.BunkerCapacity;	
        CriticalBunkerLevel = ship.CriticalBunkerLevel;
        timeLoadUnLoadPerFullContainerTEU = ship.timeLoadUnLoadPerFullContainerTEU;
        timeLoadUnLoadPerFullContainerFEU = ship.timeLoadUnLoadPerFullContainerFEU;
        Penalty_ZeroBunker = ship.Penalty_ZeroBunker;
        weeklyFrequencyHour = ship.weeklyFrequencyHour;
        avgWeightUtilizeContainer = ship.avgWeightUtilizeContainer;
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
        this.isLate = ship.isLate;

        this.TimeStartOper = ship.TimeStartOper;                    //TS :: Hour | tA+0.5
        this.TotalOperationTime = ship.TotalOperationTime;               //ti ::HOUR
        this.TimeEndOper = ship.TimeEndOper;                      //TE ::Hour | TS+ti

        this.BunkerLevelAfterOper = ship.BunkerLevelAfterOper;             //Avi and Avi-1 :: TON depends on when we use it
        this.BunkerLevelAtArrival = ship.BunkerLevelAtArrival;             //µvi :: TON
        this.CurrentBunkerAmount = ship.CurrentBunkerAmount;              //bvi ::TON
        this.CurrentTotalBunkerHoldingCost = ship.CurrentTotalBunkerHoldingCost;    //Hvi ::TON
        this.AvgCurrentBunkerHoldingCost = ship.AvgCurrentBunkerHoldingCost;      //hvi ::TON
        this.Dept_Bunker = ship.Dept_Bunker;                      //dvi ::TON
        this.hasNoFuel = ship.hasNoFuel;
        this.AmountBunkered = ship.AmountBunkered;                   //Bvi ::TON, total fuel bunkered , random value between 20% and bunkerCapacity
        this.hasBunkered = ship.hasBunkered;
        this.FuelConsumedIdle = ship.FuelConsumedIdle;                 //FIdle ::TON
        this.FuelConsumedTraveled = ship.FuelConsumedTraveled;              //FTravel ::TON

        this.TotalAmountContainerCarried = ship.TotalAmountContainerCarried;         //Lvig = (Lvi +TotalDemands) ::number
        this.CurrentShipPayload = ship.CurrentShipPayload;               //Wij ::Ton  NoContainers*weight_Coeff + CurrentBunkerAmount(cuz in ton)
        this.totalLoadUnload = ship.totalLoadUnload;
        this.AmountLoaded = ship.AmountLoaded;
        this.AmountUnloaded = ship.AmountUnloaded;
        this.previousPortName = ship.previousPortName;
        
    }
    @Override
    public String toString() {
        return this.Code;
    
    
    }
    
        public Object[] convertListShipToObjectgArray(){
        //use this.ship
            
            DecimalFormat df = CoreFunctions.getDecimalFormat(2);

            String ShipCategoryID       = this.shipCategory_ID+"";
            String Code                 = this.Code ;
            String Description          =this.Description ;
            String CapacityTEU          = df.format(this.capacityTEU );
            String CapacityFFE          = df.format(this.capacityFEU  );

            String MinSpeed             = df.format(this.MinSpeed );
            String MaxSpeed             = df.format(this.MaxSpeed );

            String DesignSpeed          = df.format(this.DesignSpeed  );
            String Coeff_FuelIdle       = Double.toString(this.Coeff_FuelIdle  );

            String Coeff_Alpha          = Double.toString(this.Coeff_Alpha );
            String Coeff_Beta           = Double.toString(this.Coeff_Beta   );
            String Coeff_FuelTravel     = Double.toString(this.Coeff_FuelTravel);
            String Coeff_c_speed        = Double.toString(this.Coeff_c_speed );
            String Coeff_c_weight       = Double.toString(this.Coeff_c_weight );
            String BunkerCapacity       = Double.toString(this.BunkerCapacity );

            String CriticalBunkerLevel       = df.format(this.CriticalBunkerLevel  );
            String TimeLoadUnLoadPerFullContainerTEU   = Double.toString(this.timeLoadUnLoadPerFullContainerTEU  );
            String Penalty_ZeroBunker   =  df.format(this.Penalty_ZeroBunker); 
            String Remarks              =   this.Remarks ;

            boolean IsActive            = this.IsActive  ;
            String CreatedBy            = this.CreatedBy+"" ;
              


        return new Object[]{ShipCategoryID,Code,Description,
            CapacityTEU,CapacityFFE,
            MinSpeed,MaxSpeed,DesignSpeed,
            Coeff_FuelIdle,
            Coeff_Alpha,Coeff_Beta,Coeff_FuelTravel,
            Coeff_c_speed,Coeff_c_weight,
            BunkerCapacity,
            CriticalBunkerLevel,
            TimeLoadUnLoadPerFullContainerTEU,
            Penalty_ZeroBunker,
            Remarks,IsActive,CreatedBy
        };
    }
}
