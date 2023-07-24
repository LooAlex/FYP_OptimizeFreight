/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Algorithms;
import java.nio.file.FileSystemNotFoundException;
import java.util.*;
import Core.*;
import DLL.*;
import Entity.*;
import java.sql.*;
/**
 *
 * @author Loo Alex
 */
public class GA_PortAlgorithm {
      //From 
    public int generationSize;              //GUI
    public int numberOfPorts;               //GUI
    public int reproductionSize;            //GUI
    public float mutationRate;              //GUI    
    public double [][]TravelDistances;      //GUI
    public int IndexSelectedStartPort;      //GUI
    public double TargetOperationCostFitness;   //GUI
    public int tournamentSize;              //GUI
    public CoreEnum.SelectionType type;     //GUI
    
    public ShipCategoryDTO SelectedShipCategory;    //GUI
    public HashMap<Integer,PortDTO> IndexToPortMatrix;//GUI
    
    public int genomeSize;                  //Here
    public int MaxIteration;                //HERE
    
    
    
    
    //Here
    
    
    
    public GA_PortAlgorithm
    (
        ShipCategoryDTO SelectedShipCategory,int numberOfPorts,int IndexSelectedStartPort,
        HashMap<Integer,PortDTO> IndexToPortMatrix, double [][]TravelDistances,
        double TargetOperationCostFitness,
        int generationSize,int reproductionSize,float mutationRate,
        CoreEnum.SelectionType type,int tournamentSize
    ){
        this.SelectedShipCategory = SelectedShipCategory;
        this.numberOfPorts = numberOfPorts;
        this.IndexSelectedStartPort = IndexSelectedStartPort;
        this.IndexToPortMatrix = IndexToPortMatrix;
        this.TravelDistances  = TravelDistances;
        this.TargetOperationCostFitness = TargetOperationCostFitness;
        this.generationSize = generationSize;
        this.reproductionSize = reproductionSize;
        this.mutationRate = mutationRate;
        this.type= type;
        this.tournamentSize= this.tournamentSize;
        
        this.MaxIteration = 1000;
        this.genomeSize = numberOfPorts-1;
        
    }
    
     public List<GAGenome> initialPopulation(){
        List<GAGenome> population  = new ArrayList<>();
        for(int i = 0 ;i<generationSize ; i++){
            population.add(new GAGenome(numberOfCities,travelPrices,startingCity)); //first pop we use randomConstructor
        }
        
        return population;
    }
    
    
    
}


