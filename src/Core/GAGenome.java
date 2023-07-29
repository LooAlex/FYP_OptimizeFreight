/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;


import java.util.*;
import Core.*;
import DAL.*;
import Entity.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
/**
 *
* @author Loo Alex
 */
public class GAGenome implements Comparable<GAGenome>{

    public ArrayList<Integer> getGenome_FullCyclePath() {
        return genome_FullCyclePath;
    }

    public void setGenome_FullCyclePath(ArrayList<Integer> genome_FullCyclePath) {
        this.genome_FullCyclePath = genome_FullCyclePath;
    }

    public int getIndexStartingPort() {
        return IndexStartingPort;
    }

    public void setIndexStartingPort(int IndexStartingPort) {
        this.IndexStartingPort = IndexStartingPort;
    }

    public double getTotalDistanceTravel() {
        return TotalDistanceTravel;
    }

    public void setTotalDistanceTravel(double TotalDistanceTravel) {
        this.TotalDistanceTravel = TotalDistanceTravel;
    }

    public float getTotalTimeTaken() {
        return TotalTimeTaken;
    }

    public void setTotalTimeTaken(float TotalTimeTaken) {
        this.TotalTimeTaken = TotalTimeTaken;
    }

    public ShipCategoryDTO getShip() {
        return ship;
    }

    public void setShip(ShipCategoryDTO ship) {
        this.ship = ship;
    }

    public HashMap<Integer, PortDTO> getIndexToPortMatrix() {
        return IndexToPortMatrix;
    }

    public void setIndexToPortMatrix(HashMap<Integer, PortDTO> IndexToPortMatrix) {
        this.IndexToPortMatrix = IndexToPortMatrix;
    }

    List<Integer> genome;   //all ports except origin and destination for shuffle
    ArrayList<Integer> genome_FullCyclePath ;
    public ArrayList<PortDTO> DataPortHistory;        //neutral first data
    public ArrayList<PortDTO>DataPortsAlter;    //store altered data.
    double[][] travelDistances;
    int IndexStartingPort;  
    int numberOfPorts;
    double fitness;         //totalOperatingCost for that portSequence including StartingPort
    CoreEnum.FuelFunctionType FType;
    
    float TotalTimeTaken;
    public ShipCategoryDTO ship;
    public HashMap<Integer, PortDTO> IndexToPortMatrix;  //
    public String Path ;
    public double TotalDistanceTravel;
   
    
    public List<Integer> getGenome() {
        return genome;
    }

    public void setGenome(List<Integer> genome) {
        this.genome = genome;
    }

    public int getStatingPort() {
        return IndexStartingPort;
    }

    public void setStatingPort(int statingPort) {
        this.IndexStartingPort = statingPort;
    }

    public int getNumberOfPorts() {
        return numberOfPorts;
    }

    public void setNumberOfPorts(int numberOfPorts) {
        this.numberOfPorts = numberOfPorts;
    }

    public double[][] getTravelDistances() {
        return travelDistances;
    }

    public void setTravelDistances(double[][] travelDistances) {
        this.travelDistances = travelDistances;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    //GAGenome Random
    public GAGenome
        (int numberOfPorts,
            int IndexSelectedStartingPort, 
            HashMap<Integer,PortDTO> IndexToPortMatrix,
            ShipCategoryDTO SelectedShipCategory, 
            double [][]travelDistances,
            CoreEnum.FuelFunctionType selectedFtype
        ) {
        this.numberOfPorts  = numberOfPorts;//to make random int shuffle
        this.IndexStartingPort = IndexSelectedStartingPort;//access indexMatrix and create genome
        
        createIndexToPortMatrix(IndexToPortMatrix);
        
        this.travelDistances  = travelDistances;
        this.ship = new ShipCategoryDTO(SelectedShipCategory);
        
        //shuffle new genome
        this.genome = randomPorts(); //all ports except orgin and destination
        //addFullPath
        createFullPath();
        this.FType = selectedFtype;
        this.fitness = this.calculateFitness(FType);
   
    }
    
     //userDefined genome //from mutation or permutation
    public GAGenome (List<Integer> permutationOfPorts, int numberOfPorts, int IndexSelectedStartingPort, HashMap<Integer,PortDTO> IndexToPortMatrix,ShipCategoryDTO SelectedShipCategory, double [][]travelDistances,CoreEnum.FuelFunctionType selectedFtype){
        this.numberOfPorts  = numberOfPorts;//to make random int shuffle
        this.IndexStartingPort = IndexSelectedStartingPort;//access indexMatrix and create genome
        
        createIndexToPortMatrix(IndexToPortMatrix);
        
        this.travelDistances  = travelDistances;
        this.ship = new ShipCategoryDTO(SelectedShipCategory);
        
        this.genome = permutationOfPorts;
        //addFullPath
        createFullPath();
        
        this.FType = selectedFtype;
        this.fitness = this.calculateFitness(FType);
        
    }
    private void createIndexToPortMatrix(HashMap<Integer,PortDTO> IndexToPortMatrix){
        this.IndexToPortMatrix = new HashMap<>();
        this.IndexToPortMatrix.putAll(IndexToPortMatrix);
    }
    private void createFullPath(){
        this.genome_FullCyclePath = new ArrayList<>();
        this.genome_FullCyclePath.add(IndexStartingPort);//Start Origin Port
        for(int i = 0 ; i< genome.size() ; i++){
            this.genome_FullCyclePath.add(genome.get(i));
        }
        this.genome_FullCyclePath.add(IndexStartingPort);//End Origin Port
    }
    //we can use the randomPorts as it will always be 0-NPorts and IndexStartingPoint will be same
    private List<Integer> randomPorts(){
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i<numberOfPorts; i++){
            if(i != IndexStartingPort)
                result.add(i);
        }
        Collections.shuffle(result);
        return result;
    }
   
    
    public double calculateFitness(CoreEnum.FuelFunctionType cal_FType){
        double fitnessOper = 0;
        int IndexPreviousPort  = this.IndexStartingPort; 
        
        int i_LastPort = genome_FullCyclePath.size()-1;
        int IndexCurrentPort = 0;   
        
        double final_Hvi;
        this.DataPortHistory = new ArrayList<>();
        String previousPortName  ="";
       
       for(int i = 0; i < genome_FullCyclePath.size() ; i++){
            IndexCurrentPort = genome_FullCyclePath.get(i); 
            PortDTO currentPort = new PortDTO(IndexToPortMatrix.get(IndexCurrentPort));
            currentPort.shp_chosenSpeed = ship.SelectedSpeed;
            
            if(i == i_LastPort){
                currentPort.demands.DemandAmt = 0;
            }
            resetShip_Port_DependentVariable();
            //getDistance 
            ship.DistanceTravel = (travelDistances[IndexPreviousPort][IndexCurrentPort] < 0 ? Integer.MAX_VALUE :travelDistances[IndexPreviousPort][IndexCurrentPort] );
            
            //getTimeTravel
            ship.timeTravel = ((ship.SelectedSpeed < 1.00d) ? 0.00d : ship.DistanceTravel /ship.SelectedSpeed);
            
            //cal µvi
            ship.BunkerLevelAtArrival = cal_uvi(ship.BunkerLevelAfterOper,cal_FType,this.ship);
            double travel = ship.FuelConsumedTraveled;
            double hvi = ship.AvgCurrentBunkerHoldingCost;
//-------//cal CTrv
            
            currentPort.totalFuelTravelCost = cal_CTrv(travel,hvi);
            
            //set tA 
            setShipTimeArrivalAtPort();
  //-------//CPnt
            //check penaltyLateArrival
            if(ship.timeArrival > ship.ETA && i != 0){
                ship.isLate = true;
                ship.timeLate = ship.timeArrival - ship.ETA;
                currentPort.totalPenaltyCost = currentPort.penalty_LateArrival * ship.timeLate *9999999;   
            }
            //check penaltyZeroBunker 
            if(ship.BunkerLevelAtArrival < ship.CriticalBunkerLevelNew && i != 0){
                if(ship.BunkerLevelAtArrival <= 0){
                    ship.hasNoFuel = true;
                    ship.Dept_Bunker = (ship.BunkerLevelAtArrival *-1) + ship.CriticalBunkerLevelNew*9999999;
                    
                }else{
                    ship.Dept_Bunker = (ship.BunkerLevelAtArrival) + ship.CriticalBunkerLevelNew*10000;
                }
                
                currentPort.totalPenaltyCost += ship.Dept_Bunker * ship.Penalty_ZeroBunker;
            }
            //cal Bvi
            if(ship.BunkerLevelAtArrival <= ship.minBunkerAmt ){
                if(i !=i_LastPort){
                    randomBunkerAmount(ship);
                }
                else{
                    ship.AmountBunkered = 1;
                }
                ship.hasBunkered = true;
            }else{
                ship.AmountBunkered  = 0;
            }  
            //set bvi
            set_bvi_CurrentBunkerAmount(this.ship);
            
 //--------//cal Hvi
            ship.CurrentTotalBunkerHoldingCost = cal_Hvi(ship.AvgCurrentBunkerHoldingCost, 
                    ship.BunkerLevelAtArrival, 
                    currentPort.port_FuelPrice,
                    ship.AmountBunkered);
            //cal hvi
            cal_hvi_AvgCurrentBunkerHoldingCost(this.ship);
            //StartLoadingFirstSupply
            if(currentPort.demands.DemandAmt > 0 || ship.TotalAmountContainerCarried > 0){
                //set TS
                ship.TimeStartOper = ship.timeArrival+0.5;
                //cal total time to load
                ship.TotalOperationTime = cal_ti(ship.TotalAmountContainerCarried,
                        currentPort.demands.DemandAmt,
                        ship.timeLoadUnLoadPerFullContainerTEU);
            }
            //set TE
            ship.TimeEndOper = ship.TimeStartOper + ship.TotalOperationTime;
 //--------//cal_CHdl
            currentPort.totalHandlingCost = cal_CHdl(currentPort.demands.DemandAmt,
                    ship.TotalAmountContainerCarried,
                    currentPort.port_CostPerFullContainer);
            //cal_Wij
            ship.CurrentShipPayload = cal_Wij(currentPort.demands.DemandAmt);
            //cal_FIdle
            ship.FuelConsumedIdle = cal_FIdle(ship.Coeff_FuelIdle,ship.TotalOperationTime);
 //--------//cal_CFIdle
            currentPort.totalFuelIdleCost = cal_CFIdle();
            //cal Avi
            ship.BunkerLevelAfterOper = cal_Avi(this.ship);
            
            if(i != i_LastPort){
                final_Hvi = 0;
            //all ports except last port     
                //set tE
                ship.timeLeave = ship.TimeEndOper+0.25;  
                //Choose a speed
                randomSpeed();
                //set TEst, Estimate Time of Arival
                setPortETA(i);
            }else{
                ship.timeLeave = ship.TimeEndOper;
                //finalHvi after all operations
                final_Hvi = cal_FinalHvi(
                        ship.AvgCurrentBunkerHoldingCost, 
                        ship.BunkerLevelAfterOper);
                currentPort.totalValueFuelLeft_FinalHvi = ( final_Hvi <0 ? final_Hvi*-1 : final_Hvi );
                if(final_Hvi<0){
                    final_Hvi=9999999;
                    currentPort.totalPenaltyCost += final_Hvi;
                }
            }
            currentPort.shp_PortTimeTaken = ship.timeLeave - ship.timeArrival ;
 //--CalOper + PortCall
            currentPort.totalOperationalCost = ( currentPort.totalFuelTravelCost + currentPort.portCall_Cost +  
                    currentPort.totalPenaltyCost + currentPort.totalHandlingCost + currentPort.totalFuelIdleCost + final_Hvi);
            
            fitnessOper += currentPort.totalOperationalCost;
            TotalDistanceTravel += ship.DistanceTravel;
            
            ship.previousPortName = previousPortName;
            IndexPreviousPort = IndexCurrentPort;
            previousPortName = currentPort.portName; 
            //copy
            ShipCategoryDTO currentShip = new ShipCategoryDTO(ship);
            currentPort.currentShip = currentShip;
            this.DataPortHistory.add(currentPort);
        }
        TotalTimeTaken = (float)ship.timeLeave/24;
        toPath();
        
        Double check = fitnessOper;   
        if(check.isNaN()|| check.doubleValue() < 0){
            fitnessOper = Double.MAX_VALUE;
        }
        return fitnessOper;
    }
    
    public double CalculateNewFitness(CoreEnum.FuelFunctionType cal_FType){
        //deep copy
        deepCopyPorts();
        double fitnessOperNew = 0;
        
        int i_LastPort =DataPortsAlter.size()-1;
        
        double final_Hvi;
        
        double SpeedToThisPort = 0.00d;
        double SpeedToNextPort = 0.00d;
        //DataPort Should be like the reusable history
        double previousBunkerAfterOper_Avi = 0.00d;
        for(int i = 0 ; i <DataPortsAlter.size(); i++){
            PortDTO currentPort  = DataPortsAlter.get(i);
            ShipCategoryDTO shipLeavingCurrentPort = currentPort.currentShip;
            
            //all ship store are stored after they have selected a speed for next port
            SpeedToNextPort = shipLeavingCurrentPort.SelectedSpeed;
            //reset speed to previous, at origin = 0, at port i = selected speed at origin
            shipLeavingCurrentPort.SelectedSpeed = SpeedToThisPort;
            
            //cal uvi
            if(i==0){
                shipLeavingCurrentPort.BunkerLevelAtArrival = 0;
                shipLeavingCurrentPort.CurrentBunkerAmount  = 0;
                shipLeavingCurrentPort.CurrentTotalBunkerHoldingCost = 0;
                shipLeavingCurrentPort.BunkerLevelAfterOper   = 0;    
                shipLeavingCurrentPort.AvgCurrentBunkerHoldingCost = 0;
            }
            
            
            shipLeavingCurrentPort.BunkerLevelAtArrival = cal_uvi(previousBunkerAfterOper_Avi,cal_FType,shipLeavingCurrentPort);
            double travel = shipLeavingCurrentPort.FuelConsumedTraveled;
            double hvi = shipLeavingCurrentPort.AvgCurrentBunkerHoldingCost;
//-------//cal CTrv
            currentPort.totalFuelTravelCost = cal_CTrv(travel,hvi);
            
            //check if need Bunkering
            //cal Bvi //rework when i add starting port again
            if(shipLeavingCurrentPort.BunkerLevelAtArrival <= shipLeavingCurrentPort.minBunkerAmt ){
                if(shipLeavingCurrentPort.BunkerLevelAtArrival < 0){
                    shipLeavingCurrentPort.hasNoFuel = true;
                    
                    currentPort.totalPenaltyCost = Double.MAX_VALUE;
                }
                
//                if( shipLeavingCurrentPort.AmountBunkered <=0){
//                     //must not be origin and IndexLastPort = other port that need bunkering
//                    //the data have is considered the past, we are only reevaluating and if in the past it did not bunkr, but new formula recommend it , then it does
//                    //in case the new algo need to bunker and it did not bunker previously
//                    if(i !=i_LastPort){
//                        randomBunkerAmount(shipLeavingCurrentPort);
//                    }
//                    else{
//                        //handle non negative at last port
//                        shipLeavingCurrentPort.AmountBunkered = 1;
//                    }
//                    shipLeavingCurrentPort.hasBunkered = true;
//                }
               
                
                
            }//else keep amount bunkered same 
            //set bvi
            set_bvi_CurrentBunkerAmount(shipLeavingCurrentPort);
            
 //--------//cal Hvi
            shipLeavingCurrentPort.CurrentTotalBunkerHoldingCost = cal_Hvi(shipLeavingCurrentPort.AvgCurrentBunkerHoldingCost, shipLeavingCurrentPort.BunkerLevelAtArrival, currentPort.port_FuelPrice,shipLeavingCurrentPort.AmountBunkered);
            //cal hvi
            cal_hvi_AvgCurrentBunkerHoldingCost(shipLeavingCurrentPort);
            //cal Avi
            shipLeavingCurrentPort.BunkerLevelAfterOper = cal_Avi(shipLeavingCurrentPort);
             
            if(i != i_LastPort){
                final_Hvi = 0;
            }else{
                
                final_Hvi = cal_FinalHvi(shipLeavingCurrentPort.AvgCurrentBunkerHoldingCost, shipLeavingCurrentPort.BunkerLevelAfterOper);
                currentPort.totalValueFuelLeft_FinalHvi  = final_Hvi;
                
                if(final_Hvi<0){
                    final_Hvi=9999999;
                    currentPort.totalPenaltyCost += final_Hvi;
                }
               
            }
            
 //--CalOper + PortCall
            currentPort.totalOperationalCost = currentPort.totalFuelTravelCost + currentPort.portCall_Cost +  currentPort.totalPenaltyCost + currentPort.totalHandlingCost + currentPort.totalFuelIdleCost + final_Hvi;
            fitnessOperNew += currentPort.totalOperationalCost;
            SpeedToThisPort = SpeedToNextPort;
            previousBunkerAfterOper_Avi = shipLeavingCurrentPort.BunkerLevelAfterOper;
        }
        
        toPath();
        
        Double check = fitnessOperNew;   
        if(check.isNaN() || check.doubleValue() < 0){
            fitnessOperNew = Double.MAX_VALUE;
        }
        return fitnessOperNew;
    }
    
    public void deepCopyPorts(){
        DataPortsAlter = new ArrayList<>();
        for(int i = 0; i<DataPortHistory.size() ; i++){
            DataPortsAlter.add(new PortDTO(DataPortHistory.get(i)));
        }
    }
    public void setPortETA(int i){
        //visiting a port each 2 weeks
        ship.ETA =ship.weeklyFrequencyHour*(i+1);
    }
    public void setShipTimeArrivalAtPort(){
        ship.timeArrival = ship.timeLeave+ship.timeTravel;
    }
    public double cal_uvi(double FuelAmountAtLeave_PreviousPort, CoreEnum.FuelFunctionType Ftype, ShipCategoryDTO ship){
        
        double FuelConsumedTraveled = 0.00d;
        if(Ftype == CoreEnum.FuelFunctionType.SPEEDONLY){
            FuelConsumedTraveled = FuelFunctionSpeedOnly(FuelAmountAtLeave_PreviousPort, ship);
        }else if (Ftype == CoreEnum.FuelFunctionType.WEIGHTVARIABLE){
            FuelConsumedTraveled = FuelFunctionWeightVariable(FuelAmountAtLeave_PreviousPort, ship);
        }
        
        ship.FuelConsumedTraveled = FuelConsumedTraveled;
        
        return (FuelAmountAtLeave_PreviousPort - ship.FuelConsumedTraveled);
    }
    public double FuelFunctionSpeedOnly (double FuelAmountAtLeave_PreviousPost,  ShipCategoryDTO ship){
        
        return (ship.timeTravel/24.00) * (ship.Coeff_c_speed * Math.pow(ship.SelectedSpeed, 3) + ship.Coeff_c_weight); 
    }
    public double FuelFunctionWeightVariable(double FuelAmountAtLeave_PreviousPost,  ShipCategoryDTO ship){
        
        return (ship.DistanceTravel/24.00) * ship.Coeff_FuelTravel * Math.pow(ship.SelectedSpeed, 2) * (ship.Coeff_Alpha + ship.Coeff_Beta*ship.CurrentShipPayload);
    }
    public double cal_CTrv( double traveled, double AvgCurrentBunkerHoldingCost){
        return (traveled* AvgCurrentBunkerHoldingCost);
    }
    public void set_bvi_CurrentBunkerAmount(ShipCategoryDTO ship){
        double excessBunker = 0;
        
        //if negative, we already settle the dept, reset it to zero here.
        if(ship.BunkerLevelAtArrival<0){
            ship.BunkerLevelAtArrival = 0;   
        }
        
        if((ship.AmountBunkered+ship.BunkerLevelAtArrival) >= ship.BunkerCapacity){
            //remove excess, now BunkerLevel  is max
            excessBunker = ship.AmountBunkered+ship.BunkerLevelAtArrival - ship.BunkerCapacity; 
            ship.AmountBunkered  = ship.AmountBunkered- excessBunker;
        } 
        ship.CurrentBunkerAmount = ship.BunkerLevelAtArrival + ship.AmountBunkered;   
        
    }
    public double cal_Hvi(double previous_hvi,double FuelAtArrival,double PortFuelPrice, double AmountBunkered){
        return (previous_hvi*FuelAtArrival)+(PortFuelPrice*AmountBunkered);
    }
    public void cal_hvi_AvgCurrentBunkerHoldingCost(ShipCategoryDTO ship){
        ship.AvgCurrentBunkerHoldingCost= ship.CurrentTotalBunkerHoldingCost/ship.CurrentBunkerAmount;   
    }
    public double cal_ti(int AmtSupply,int AmtDemand,double timeShipToLoadUnloadContainer){
        ship.AmountLoaded = AmtDemand;
        ship.AmountUnloaded = AmtSupply;
        ship.totalLoadUnload = (AmtDemand+AmtSupply);
        return ship.totalLoadUnload*timeShipToLoadUnloadContainer;
    }
    public double cal_CHdl(int AmtDemand,int AmtSupply,double CostToLoadUnloadContainer){
        return(AmtDemand+AmtSupply)*CostToLoadUnloadContainer;
    }
    public double cal_Wij(int AmtDemand){
        // total to be carried = only demand amt as we assume next port we will supply the entire demand to that port
        //so its load, complete unload, to reload
        ship.TotalAmountContainerCarried = AmtDemand;
        return ship.TotalAmountContainerCarried*ship.avgWeightUtilizeContainer;
    }
    public double cal_FIdle(double coeff_FuelIdle,double TotalOperationTime){
        return coeff_FuelIdle*(TotalOperationTime/24.00);
    }
    public double cal_CFIdle(){
        return (ship.FuelConsumedIdle*ship.AvgCurrentBunkerHoldingCost);
    }
     public double cal_Avi(ShipCategoryDTO ship){
         //bvi - FIdle
        return (ship.CurrentBunkerAmount-ship.FuelConsumedIdle);
    }
    
    public void resetShip_Port_DependentVariable(){
        
        ship.isLate = false;
        ship.hasBunkered = false;
        ship.hasNoFuel = false;
        ship.TimeStartOper = 0;
        ship.TimeEndOper = 0;
        ship.TotalOperationTime = 0;
        ship.timeLate = 0;
        ship.DistanceTravel = 0;
        ship.AmountBunkered = 0;
        ship.Dept_Bunker = 0;
        //1.00 or 1.00d java see as double
        //1.00f java see as float
    }
   
    
    public void randomSpeed(){
        Random rand = new Random();
        ship.SelectedSpeed  = ship.SpeedOptions.get(rand.nextInt(0,3));
    }
    public void randomBunkerAmount(ShipCategoryDTO ship){
        Random rand = new Random();
        ship.AmountBunkered = rand.nextInt((int)ship.minAmountToBunkerUp, (int)ship.BunkerCapacity);
        //we could restrict the ceiling with maxBunker-currentLevel but this will reduce the chance of system to bunker full once in a while.
    }
    public double cal_FinalHvi(double previous_hvi,double BunkerLevelAfterOper_Avi){
        return (previous_hvi*BunkerLevelAfterOper_Avi);
    }
    public void toPath(){
        StringBuilder sb = new StringBuilder();
        sb.append("PathID: ");
        
        for(int i = 0 ; i < genome_FullCyclePath.size(); i++){
            sb.append(IndexToPortMatrix.get(genome_FullCyclePath.get(i)).getPortID());
            sb.append(" ");
        }
        sb.append("\nPath: ");
        
        for(int i = 0 ; i < genome_FullCyclePath.size(); i++){
            sb.append(IndexToPortMatrix.get(genome_FullCyclePath.get(i)).getPortName());
            if(i != genome_FullCyclePath.size()-1){
                sb.append(" -> ");
            }       
        }
        Path = sb.toString();
    }
    
    public LinkedList<Object[]> convertDataPortToStringArray(ArrayList<PortDTO> DataPort){
        LinkedList<Object[]> converted = new LinkedList<>();
        
        if(DataPort!= null && DataPort.size()>2){
            for(int i =0; i<DataPort.size(); i++){
                DecimalFormat df = CoreFunctions.getDecimalFormat(2);
                
                String sequenceNo       = i+1+"";
                String PortFromName     = (i == 0 ? "-" : DataPort.get(i).currentShip.previousPortName);
                String PortToName       = DataPort.get(i).portName;
                String Distance         = df.format(DataPort.get(i).currentShip.DistanceTravel);
                String Speed            = df.format(DataPort.get(i).shp_chosenSpeed  );
                String Time             = df.format(DataPort.get(i).currentShip.timeTravel  );
                String FuelConsumedTravel   = df.format(DataPort.get(i).currentShip.FuelConsumedTraveled  );
                
                String TimeArrival      = df.format(DataPort.get(i).currentShip.timeArrival  );
                String TimeLeft         = df.format(DataPort.get(i).currentShip.timeLeave  );
                String PortTimeTaken        = df.format(DataPort.get(i).shp_PortTimeTaken);
                String FuelArrival      = df.format(DataPort.get(i).currentShip.BunkerLevelAtArrival  );
                String FuelAtLeave      = df.format(DataPort.get(i).currentShip.BunkerLevelAfterOper  );
                
                String PortSupply       = df.format(DataPort.get(i).currentShip.AmountUnloaded  );
                String PortDemand       = df.format(DataPort.get(i).currentShip.AmountLoaded   );
                String CostPerContainer = df.format(DataPort.get(i).port_CostPerFullContainer  );
                String OperTime         = df.format(DataPort.get(i).shp_PortTimeTaken  );
                String OperFuelConsumed = df.format(DataPort.get(i).currentShip.FuelConsumedIdle  );
                
                boolean HasBunker       = DataPort.get(i).currentShip.hasBunkered  ;
                String AmountBunkered   = df.format(DataPort.get(i).currentShip.AmountBunkered  );
                String PortFuelPrice    =  df.format(DataPort.get(i).port_FuelPrice ); 
                boolean IsLate          = DataPort.get(i).currentShip.isLate  ;
                String TimeLate         = df.format(DataPort.get(i).currentShip.timeLate  );
                
                boolean HasNoFuel       = DataPort.get(i).currentShip.hasNoFuel  ;
                String DeptBunker       = df.format(DataPort.get(i).currentShip.Dept_Bunker  );
               
                String PortCall         = df.format(DataPort.get(i).portCall_Cost  );
                String TotalTravelCost  = df.format(DataPort.get(i).totalFuelTravelCost  );
                String TotalIdleFuelCost   = df.format(DataPort.get(i).totalFuelIdleCost  );
                String TotalHandlingCost    = df.format(DataPort.get(i).totalHandlingCost  );
                String TotalPenaltyCost     = df.format(DataPort.get(i).totalPenaltyCost  );
                String TotalValueFuelLeft   = df.format(DataPort.get(i).totalValueFuelLeft_FinalHvi  );
                String TotalOperatingCost   = df.format(DataPort.get(i).totalOperationalCost  );

                converted.add(new Object[]{
                    sequenceNo,PortFromName,PortToName,Distance,Speed,Time,FuelConsumedTravel,
                    TimeArrival,TimeLeft,
                    PortTimeTaken,
                    FuelArrival,FuelAtLeave,
                    PortSupply,PortDemand,CostPerContainer,OperTime,OperFuelConsumed,
                    HasBunker,AmountBunkered,PortFuelPrice,
                    IsLate,TimeLate,
                    HasNoFuel,DeptBunker,
                    PortCall,TotalTravelCost,TotalIdleFuelCost,TotalHandlingCost,TotalPenaltyCost,TotalValueFuelLeft,
                    TotalOperatingCost});
            }
        }
        return converted;
    }
    @Override
    public int compareTo(GAGenome genome) {
        if(this.fitness > genome.getFitness())
            return 1;
        else if(this.fitness < genome.getFitness())
            return -1;
        else
            return 0;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        toPath();
        sb.append(Path); 
        sb.append("\nTotal Operation Cost: ");
        sb.append(new BigDecimal(getFitness()).setScale(2,RoundingMode.HALF_UP));
        
        return sb.toString();
    }
    
    
}
