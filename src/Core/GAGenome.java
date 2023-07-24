/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;


import java.util.*;
import Core.*;
import DLL.*;
import Entity.*;
import java.sql.*;
/**
 *
* @author Loo Alex
 */
public class GAGenome implements Comparable<GAGenome>{

    List<Integer> genome;   //all ports except origin and destination for shuffle
    ArrayList<Integer> genome_FullCyclePath ;
    double[][] travelDistances;
    int IndexStartingPort;  
    int numberOfPorts;
    double fitness;         //totalOperatingCost for that portSequence including StartingPort
    
    public ShipCategoryDTO ship;
    public HashMap<Integer, PortDTO> IndexToPortMatrix;  //

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
    public GAGenome(int numberOfPorts, int IndexSelectedStartingPort, HashMap<Integer,PortDTO> IndexToPortMatrix,ShipCategoryDTO SelectedShipCategory, double [][]travelDistances) {
        this.numberOfPorts  = numberOfPorts;//to make random int shuffle
        this.IndexStartingPort = IndexSelectedStartingPort;//access indexMatrix and create genome
        this.IndexToPortMatrix = IndexToPortMatrix;
        this.travelDistances  = travelDistances;
        this.ship = SelectedShipCategory;

        
        //shuffle new genome
        this.genome = randomPorts(); //all ports except orgin and destination
        //addFullPath
        createFullPath();
        
        this.fitness = this.calculateFitness();
        
    }
    
     //userDefined genome //from mutation or permutation
    public GAGenome (List<Integer> permutationOfPorts, int numberOfPorts, int IndexSelectedStartingPort, HashMap<Integer,PortDTO> IndexToPortMatrix,ShipCategoryDTO SelectedShipCategory, double [][]travelDistances){
        this.numberOfPorts  = numberOfPorts;//to make random int shuffle
        this.IndexStartingPort = IndexSelectedStartingPort;//access indexMatrix and create genome
        this.IndexToPortMatrix = IndexToPortMatrix;
        this.travelDistances  = travelDistances;
        this.ship = SelectedShipCategory;
        
        this.genome = permutationOfPorts;
        //addFullPath
        createFullPath();
        
        this.fitness = this.calculateFitness();
        
    }
    
    private void createFullPath(){
        this.genome_FullCyclePath = new ArrayList<>();
        this.genome_FullCyclePath.add(IndexStartingPort);//Start Origin Port
        this.genome_FullCyclePath.addAll(genome);        //all other ports in middle 
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
    
    public double calculateFitness(){
        double fitnessOper = 0;
        int IndexPreviousPort  = this.IndexStartingPort; //use this for matrix
        
        int i_LastPort = genome_FullCyclePath.size()-1;
        int IndexCurrentPort = 0;   //the id
        
        double final_Hvi;
 //-----//calculate entire path cost except origin and destination.
       // for(int gene : genenomeWithStarting){
       for(int i = 0; i < genome_FullCyclePath.size() ; i++){
            IndexCurrentPort = genome_FullCyclePath.get(i);  //next port Index for IndexToPortMatrix and travelDistanceMatrix
            PortDTO currentPort = IndexToPortMatrix.get(IndexCurrentPort);
 //current gene  = already traveled to port j
            resetShipPortDependentVariable();
            //getDistance From_PreviousPort To_CurrentPort
            ship.DistanceTravel = travelDistances[IndexPreviousPort][IndexCurrentPort];
            //getTimeTravel
            ship.timeTravel = ((ship.SelectedSpeed < 1.00d) ? 0.00d : ship.DistanceTravel /ship.SelectedSpeed);
            //cal µvi
            ship.BunkerLevelAtArrival = cal_uvi(ship.BunkerLevelAfterOper);
  //-------//cal CTrv
            currentPort.TotalFuelTravelCost = cal_CTrv();
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
            if(i ==0 ){
                //at Origin Max bunker
                ship.AmountBunkered = ship.BunkerCapacity;
            }
            else if(ship.BunkerLevelAtArrival <= ship.minBunkerAmt && i != i_LastPort){
                //must not be origin and IndexLastPort = other port that need bunkering
                randomBunkerAmount();
            }else{
                //i not origin and CurrentBunkerLevel is high or indexLastPort
                ship.AmountBunkered  = 0;
            }
            //set bvi
            set_bvi_CurrentBunkerAmount();
 //--------//cal Hvi
            ship.CurrentTotalBunkerHoldingCost = cal_Hvi(ship.AvgCurrentBunkerHoldingCost, ship.BunkerLevelAtArrival, currentPort.Port_FuelPrice);
            //cal hvi
            cal_hvi_AvgCurrentBunkerHoldingCost();
            //StartLoadingFirstSupply
            if(currentPort.demands.DemandAmt > 0){
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
            ship.BunkerLevelAfterOper = cal_Avi();
            
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
                ship.TimeHorizon = ship.TimeEndOper;
                //finalHvi after all operations
                final_Hvi = cal_FinalHvi(ship.AvgCurrentBunkerHoldingCost, ship.BunkerLevelAfterOper);
            }

 //--CalOper + PortCall
            fitnessOper += currentPort.TotalFuelTravelCost + currentPort.PortCall_Cost +  currentPort.TotalPenaltyCost + currentPort.TotalHandlingCost + currentPort.TotalFuelIdleCost + final_Hvi;

            IndexPreviousPort = IndexCurrentPort;//need previous only to get distance to current port
            
        }

        return fitnessOper;
    }
    
    public void setShipETA(){
        ship.ETA = ship.timeLeave+ship.weeklyFrequencyHour;
    }
    public void setShipTimeArrivalAtPort(){
        ship.timeArrival = ship.timeLeave+ship.timeTravel;
    }
    public double cal_uvi(double FuelAmountAtLeave_PreviousPort){
        ship.FuelConsumedTraveled = (ship.DistanceTravel/24.00) * ship.Coeff_FuelTravel * Math.pow(ship.SelectedSpeed, 2) * (ship.Coeff_Alpha + ship.Coeff_Beta*ship.CurrentShipPayload);
        
        return (FuelAmountAtLeave_PreviousPort - ship.FuelConsumedTraveled);
    }
    public double cal_CTrv(){
        return (ship.FuelConsumedTraveled * ship.AvgCurrentBunkerHoldingCost);
    }
    public void set_bvi_CurrentBunkerAmount(){
        double excessBunker = 0;
        if(ship.AmountBunkered > ship.BunkerCapacity){
            //remove excess, now BunkerLevel  is max
            excessBunker = ship.AmountBunkered - ship.BunkerCapacity; 
            ship.AmountBunkered -= excessBunker;
        }
        ship.CurrentBunkerAmount = ship.BunkerLevelAtArrival + ship.AmountBunkered;   
    }
    public double cal_Hvi(double previous_hvi,double FuelAtArrival,double PortFuelPrice){
        return (previous_hvi*FuelAtArrival)+(PortFuelPrice*ship.AmountBunkered);
    }
    public void cal_hvi_AvgCurrentBunkerHoldingCost(){
        ship.CurrentTotalBunkerHoldingCost= ship.CurrentTotalBunkerHoldingCost*ship.CurrentBunkerAmount;   
    }
    public double cal_ti(int AmtSupply,int AmtDemand,double timeShipToLoadUnloadContainer){
        return(AmtDemand+AmtSupply)*timeShipToLoadUnloadContainer;
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
     public double cal_Avi(){
         //bvi - FIdle
        return (ship.CurrentBunkerAmount-ship.FuelConsumedIdle);
    }
    
    public void resetShipPortDependentVariable(){
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
    public void randomBunkerAmount(){
        Random rand = new Random();
        ship.AmountBunkered = rand.nextInt((int)ship.minAmountToBunkerUp, (int)ship.BunkerCapacity);
        //we could restrict the ceiling with maxBunker-currentLevel but this will reduce the chance of system to bunker full once in a while.
    }
    public double cal_FinalHvi(double previous_hvi,double BunkerLevelAfterOper_Avi){
        return (previous_hvi*BunkerLevelAfterOper_Avi);
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
        sb.append("Path: ");
        
        for(int i = 0 ; i < genome_FullCyclePath.size(); i++){
            sb.append(IndexToPortMatrix.get(genome_FullCyclePath.get(i)).getPortID());
            sb.append(" ");
        }
  
        sb.append(" ");
        sb.append("\nTotal Operation Cost: ");
        sb.append(getFitness());
        
        return sb.toString();
    }
    
}
