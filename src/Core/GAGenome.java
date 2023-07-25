/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;


import java.util.*;
import Core.*;
import DLL.*;
import Entity.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    double fitnessSpeed;
    CoreEnum.FuelFunctionType FType;
    
    float TotalTimeTaken;
    public ShipCategoryDTO ship;
    public HashMap<Integer, PortDTO> IndexToPortMatrix;  //
    public String Path ;
    public double TotalDistanceTravel;
    
    //Port Variable
    public double TotalHandlingCost;        //CHdl :: $ |(DemandAmt+SupplyAmnt)*Port_CostPerFullContainer
    public double TotalFuelTravelCost;      //Ctravel :: $ |previous port i to this port j
    public double TotalFuelIdleCost;        //CFIdle :: $ |for this port i, for duration of operation
    public double TotalPenaltyCost;         //CPnt :: $ |
    
    
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
    public GAGenome(int numberOfPorts, int IndexSelectedStartingPort, HashMap<Integer,PortDTO> IndexToPortMatrix,ShipCategoryDTO SelectedShipCategory, double [][]travelDistances,CoreEnum.FuelFunctionType selectedFtype) {
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
    private List<Integer> randomPorts(){//we use this functions since i encoded the pord ID with their respective Index
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i<numberOfPorts; i++){
            if(i != IndexStartingPort)
                result.add(i);
        }
        Collections.shuffle(result);
        return result;
    }
    
    //how to calculate this fitness path?
    
    public double calculateFitness(CoreEnum.FuelFunctionType cal_FType){
        
        if(cal_FType == null){
            System.out.println("YOO");
        }
        
        
        double fitnessOper = 0;
        int IndexPreviousPort  = this.IndexStartingPort; //use this for matrix
        
        int i_LastPort = genome_FullCyclePath.size()-1;
        int IndexCurrentPort = 0;   //the id
        
        double final_Hvi;
        this.DataPortHistory = new ArrayList<>();
        String previousPortName  ="";
 //-----//calculate entire path cost except origin and destination.
       // for(int gene : genenomeWithStarting){
       for(int i = 0; i < genome_FullCyclePath.size() ; i++){
            IndexCurrentPort = genome_FullCyclePath.get(i);  //next port Index for IndexToPortMatrix and travelDistanceMatrix
            PortDTO currentPort = new PortDTO(IndexToPortMatrix.get(IndexCurrentPort));
            currentPort.shp_choseSpeed = ship.SelectedSpeed;
            
            if(i == i_LastPort){
                //just deposit demand at last port, will not re supply again
                currentPort.demands.DemandAmt = 0;
            }
 //current gene  = already traveled to port j
            resetShip_Port_DependentVariable();
            //getDistance From_PreviousPort To_CurrentPort
            ship.DistanceTravel = travelDistances[IndexPreviousPort][IndexCurrentPort];
            
            //getTimeTravel
            ship.timeTravel = ((ship.SelectedSpeed < 1.00d) ? 0.00d : ship.DistanceTravel /ship.SelectedSpeed);
            
            //cal µvi
            ship.BunkerLevelAtArrival = cal_uvi(ship.BunkerLevelAfterOper,cal_FType,this.ship);
            double travel = ship.FuelConsumedTraveled;
            double hvi = ship.AvgCurrentBunkerHoldingCost;
            if( i!=0 && (travel == 0 || hvi == 0) ){
                System.out.println("Something fishy First");
            }
//-------//cal CTrv
            
            currentPort.TotalFuelTravelCost = cal_CTrv(travel,hvi);
            
            //set tA :: Time arrival at currentPort
            setShipTimeArrivalAtPort();
  //-------//CPnt
            //check penaltyLateArrival
            if(ship.timeArrival > ship.ETA && i != 0){
                ship.timeLate = ship.timeArrival - ship.ETA;
                currentPort.TotalPenaltyCost = currentPort.Penalty_LateArrival * ship.timeLate ;   
            }
            //check penaltyZeroBunker if <0.05*BunkerCap
            if(ship.BunkerLevelAtArrival < ship.CriticalBunkerLevelNew && i != 0){
                if(ship.BunkerLevelAtArrival <= 0){
                    //normalize and discourage negative or zero bunker
                    ship.Dept_Bunker = (ship.BunkerLevelAtArrival *-1) + ship.CriticalBunkerLevelNew*1000.00;
                }else{
                    ship.Dept_Bunker = (ship.BunkerLevelAtArrival) + ship.CriticalBunkerLevelNew*10;
                }
                
                currentPort.TotalPenaltyCost += ship.Dept_Bunker * ship.Penalty_ZeroBunker;
            }
            //check if need Bunkering
            //cal Bvi //rework when i add starting port again
            if(ship.BunkerLevelAtArrival <= ship.minBunkerAmt && i != i_LastPort){
                //must not be origin and IndexLastPort = other port that need bunkering
                randomBunkerAmount(this.ship);
                
            }else{
                //i not origin and CurrentBunkerLevel is high or indexLastPort
                ship.AmountBunkered  = 0;
            }  
            //set bvi
            set_bvi_CurrentBunkerAmount(this.ship);
            
 //--------//cal Hvi
            ship.CurrentTotalBunkerHoldingCost = cal_Hvi(ship.AvgCurrentBunkerHoldingCost, ship.BunkerLevelAtArrival, currentPort.Port_FuelPrice,ship.AmountBunkered);
            //cal hvi
            cal_hvi_AvgCurrentBunkerHoldingCost(this.ship);
            //StartLoadingFirstSupply
            if(currentPort.demands.DemandAmt > 0 || ship.TotalAmountContainerCarried > 0){
                //set TS
                ship.TimeStartOper = ship.timeArrival+0.5;
                //cal total time to load
                ship.TotalOperationTime = cal_ti(ship.TotalAmountContainerCarried,currentPort.demands.DemandAmt,ship.TimeLoadUnLoadPerFullContainerTEU);
            }
            //set TE
            ship.TimeEndOper = ship.TimeStartOper + ship.TotalOperationTime;
 //--------//cal_CHdl
            currentPort.TotalHandlingCost = cal_CHdl(currentPort.demands.DemandAmt,ship.TotalAmountContainerCarried,currentPort.Port_CostPerFullContainer);
            //cal_Wij
            ship.CurrentShipPayload = cal_Wij(currentPort.demands.DemandAmt);
            //cal_FIdle
            ship.FuelConsumedIdle = cal_FIdle(ship.Coeff_FuelIdle,ship.TotalOperationTime);
 //--------//cal_CFIdle
            currentPort.TotalFuelIdleCost = cal_CFIdle();
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
                setShipETA();
            }else{
                ship.timeLeave = ship.TimeEndOper;
                //finalHvi after all operations
                final_Hvi = cal_FinalHvi(ship.AvgCurrentBunkerHoldingCost, ship.BunkerLevelAfterOper);
            }
            
            currentPort.shp_timeTaken = ship.timeLeave - ship.timeArrival ;
 //--CalOper + PortCall
            currentPort.TotalOperationalCost = currentPort.TotalFuelTravelCost + currentPort.PortCall_Cost +  currentPort.TotalPenaltyCost + currentPort.TotalHandlingCost + currentPort.TotalFuelIdleCost + final_Hvi;
            fitnessOper += currentPort.TotalOperationalCost;
            TotalDistanceTravel += ship.DistanceTravel;
            
            ship.previousPortName = previousPortName;
            IndexPreviousPort = IndexCurrentPort;//need previous only to get distance to current port
             
            previousPortName = currentPort.PortName;
            
            //copy
            ShipCategoryDTO currentShip = new ShipCategoryDTO(ship);
            currentPort.currentShip = currentShip;
            this.DataPortHistory.add(currentPort);
        }
        TotalTimeTaken = (float)ship.timeLeave/24;
        toPath();
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
            }
            
            shipLeavingCurrentPort.BunkerLevelAtArrival = cal_uvi(shipLeavingCurrentPort.BunkerLevelAfterOper,cal_FType,shipLeavingCurrentPort);
            double travel = shipLeavingCurrentPort.FuelConsumedTraveled;
            double hvi = shipLeavingCurrentPort.AvgCurrentBunkerHoldingCost;
//-------//cal CTrv
            currentPort.TotalFuelTravelCost = cal_CTrv(travel,hvi);
            
            //dont bunker, asumed it was same
            //set bvi
            set_bvi_CurrentBunkerAmount(shipLeavingCurrentPort);
            
 //--------//cal Hvi
            shipLeavingCurrentPort.CurrentTotalBunkerHoldingCost = cal_Hvi(shipLeavingCurrentPort.AvgCurrentBunkerHoldingCost, shipLeavingCurrentPort.BunkerLevelAtArrival, currentPort.Port_FuelPrice,shipLeavingCurrentPort.AmountBunkered);
            //cal hvi
            cal_hvi_AvgCurrentBunkerHoldingCost(shipLeavingCurrentPort);
            //cal Avi
            shipLeavingCurrentPort.BunkerLevelAfterOper = cal_Avi(shipLeavingCurrentPort);
             
            if(i != i_LastPort){
                final_Hvi = 0;
            }else{
                final_Hvi = cal_FinalHvi(shipLeavingCurrentPort.AvgCurrentBunkerHoldingCost, shipLeavingCurrentPort.BunkerLevelAfterOper);
            }
            
 //--CalOper + PortCall
            currentPort.TotalOperationalCost = currentPort.TotalFuelTravelCost + currentPort.PortCall_Cost +  currentPort.TotalPenaltyCost + currentPort.TotalHandlingCost + currentPort.TotalFuelIdleCost + final_Hvi;
            fitnessOperNew += currentPort.TotalOperationalCost;
            SpeedToThisPort = SpeedToNextPort;
        }
        
        toPath();
        
        return fitnessOperNew;
    }
    
    public void deepCopyPorts(){
        DataPortsAlter = new ArrayList<>();
        for(int i = 0; i<DataPortHistory.size() ; i++){
            DataPortsAlter.add(new PortDTO(DataPortHistory.get(i)));
        }
    }
    public void setShipETA(){
        ship.ETA = ship.timeLeave+ship.weeklyFrequencyHour;
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
        
        return (ship.timeTravel/24.00) * (ship.Coeff_k_speed * Math.pow(ship.SelectedSpeed, 3) + ship.Coeff_k_weight); 
    }
    public double FuelFunctionWeightVariable(double FuelAmountAtLeave_PreviousPost,  ShipCategoryDTO ship){
        
        return (ship.DistanceTravel/24.00) * ship.Coeff_FuelTravel * Math.pow(ship.SelectedSpeed, 2) * (ship.Coeff_Alpha + ship.Coeff_Beta*ship.CurrentShipPayload);
    }
    public double cal_CTrv( double traveled, double AvgCurrentBunkerHoldingCost){
        return (traveled* AvgCurrentBunkerHoldingCost);
    }
    public void set_bvi_CurrentBunkerAmount(ShipCategoryDTO ship){
        double excessBunker = 0;
        if(ship.AmountBunkered > ship.BunkerCapacity){
            //remove excess, now BunkerLevel  is max
            excessBunker = ship.AmountBunkered - ship.BunkerCapacity; 
            ship.AmountBunkered -= excessBunker;
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
        ship.totalLoadUnLoad = (AmtDemand+AmtSupply);
        return ship.totalLoadUnLoad*timeShipToLoadUnloadContainer;
    }
    public double cal_CHdl(int AmtDemand,int AmtSupply,double CostToLoadUnloadContainer){
        return(AmtDemand+AmtSupply)*CostToLoadUnloadContainer;
    }
    public double cal_Wij(int AmtDemand){
        // total to be carried = only demand amt as we assume next port we will supply the entire demand to that port
        //so its load, complete unload, to reload
        ship.TotalAmountContainerCarried = AmtDemand;
        return ship.TotalAmountContainerCarried*ship.AvgWeightUtilizeContainer;
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
        
        
        ship.TimeStartOper = 0;
        ship.TimeEndOper = 0;
        ship.TotalOperationTime = 0;
        ship.timeLate = 0;
        ship.DistanceTravel = 0;
        ship.AmountBunkered = 0;
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
