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
      //From GUI
    public CoreEnum.SelectionType type;
    public ShipCategoryDTO SelectedShipCategory;
    public ArrayList<PortDTO> lstSelectedPort;
    public int numberOfPorts;
    public PortDTO SelectedStartPort;
    public int IndexSelectedStartPort;
    public HashMap<Integer,PortDTO> IndexToPortMatrix;
    public double [][]TravelDistances;
    public double TargetOperationCostFitness;
    public int generationSize;
    public int reproductionSize;
    public float mutationRate;
    public int tournamentSize;
    
    //Here
    public int MaxIteration;
    
    
    public GA_PortAlgorithm
    (
        ShipCategoryDTO SelectedShipCategory,int numberOfPorts,PortDTO SelectedStartPort,int IndexSelectedStartPort,
        HashMap<Integer,PortDTO> IndexToPortMatrix, double [][]TravelDistances,
        double TargetOperationCostFitness,
        int generationSize,int reproductionSize,float mutationRate,
        CoreEnum.SelectionType type,int tournamentSize
    ){
        this.SelectedShipCategory = SelectedShipCategory;
        this.lstSelectedPort = lstSelectedPort;
        this.numberOfPorts = numberOfPorts;
        this.SelectedStartPort = SelectedStartPort;
        this.IndexSelectedStartPort = IndexSelectedStartPort;
        this.IndexToPortMatrix = IndexToPortMatrix;
        this.TravelDistances  = TravelDistances;
        this.TargetOperationCostFitness = TargetOperationCostFitness;
        this.generationSize = generationSize;
        this.reproductionSize = reproductionSize;
        this.mutationRate = mutationRate;
        this.type= type;
        this.tournamentSize= this.tournamentSize;
        
        this.MaxIteration = 10000;
        
    }
}


