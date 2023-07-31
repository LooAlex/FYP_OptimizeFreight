/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Algorithms;
import java.nio.file.FileSystemNotFoundException;
import java.util.*;
import Core.*;
import DAL.*;
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
    public CoreEnum.SelectionType selectionType;     //GUI
    public CoreEnum.FuelFunctionType FuelFunctionType;  //GUI
    
    public ShipCategoryDTO SelectedShipCategory;    //GUI
    public HashMap<Integer,PortDTO> IndexToPortMatrix;//GUI
    

    public int genomeSize;                  //HERE
    public int MaxIterations;                //HERE
    
    public GA_PortAlgorithm
    (
        ShipCategoryDTO SelectedShipCategory,int numberOfPorts,int IndexSelectedStartPort,
        HashMap<Integer,PortDTO> IndexToPortMatrix, double [][]TravelDistances,
        double TargetOperationCostFitness,
        int generationSize,int reproductionSize,float mutationRate,
        CoreEnum.SelectionType type,int tournamentSize,
        CoreEnum.FuelFunctionType Ftype
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
        this.selectionType= type;
        this.tournamentSize= tournamentSize;
        this.FuelFunctionType = Ftype;
        
        this.MaxIterations = 1000;
        this.genomeSize = numberOfPorts-1;
        
    }
    
     public List<GAGenome> initialPopulation(){
        List<GAGenome> population  = new ArrayList<>();
        for(int i = 0 ;i<generationSize ; i++){
            //random GAGenome constructor
            population.add(new GAGenome(numberOfPorts,
                    IndexSelectedStartPort,
                    IndexToPortMatrix,SelectedShipCategory,
                    TravelDistances,
                    FuelFunctionType));
            System.out.println("First Pop: Genene "+i);
        }
        
        return population;
    }
    public List<GAGenome> selection (List<GAGenome> population){
        List<GAGenome> selected =  new ArrayList<>();
        for(int i = 0 ; i <reproductionSize; i++){
            if(selectionType == CoreEnum.SelectionType.ROULETTE){
                selected.add(rouletteSelection(population));    
            }else if (selectionType == CoreEnum.SelectionType.TOURNAMENT){
                selected.add(selectionTournament(population)); 
            }
        }
        return selected; //return only 1 gaGenome
    }
    
    public GAGenome rouletteSelection(List<GAGenome>population){
        double totalFitness = population.stream().map(GAGenome::getFitness).mapToDouble(Double::doubleValue).sum();

        //pick random value
        Random random = new Random();
        int selectedValue = random.nextInt((int)totalFitness); 
        float recValue = (float)1/selectedValue; //abitrary randomValue threshold
        
        //We add up values until we reach our recValue and we pick the genome that cross the threshold
        float currentSum=0;
        for(GAGenome genome : population){
            currentSum +=(float)1/genome.getFitness();
            if(currentSum >= recValue){
                return genome;
            }
        }
        
        //in case the return above did not happen, because no one reach threshold, we choose at random
        int selectedRandom = random.nextInt(generationSize);
        return population.get(selectedRandom); 
    }
    
    public GAGenome selectionTournament(List<GAGenome>population){
        List<GAGenome> selected = pickRandomElements(population, this.tournamentSize);
        return Collections.min(selected);
    }
    
    public static <E> List<E> pickRandomElements(List<E> list, int n){
        Random r = new Random();
        int length = list.size();// generationSize here
        
        if(length < n) return null;
        
        for(int i = length-1; i >= length - n; --i){ 
            Collections.swap(list, i, r.nextInt(i+1));
        }
        return list.subList(length-n,length);
    }
    
    
    
    public List<GAGenome> crossover (List<GAGenome> parents){
        Random random = new Random();
        int breakpoint = random.nextInt(genomeSize); 
        List<GAGenome> children = new ArrayList<>();
        
        //copy parental genomes 
        List<Integer> parent1Genome = parents.get(0).getGenome();
        List<Integer> parent2Genome = parents.get(1).getGenome();
        
        //Creating Child 1 
        for(int i =0 ; i < breakpoint; i++){
            int newVal;
            newVal  = parent2Genome.get(i);
            Collections.swap(parent1Genome, parent1Genome.indexOf(newVal), i); 
        }
        children.add(new GAGenome(parent1Genome,numberOfPorts,IndexSelectedStartPort,
                IndexToPortMatrix,SelectedShipCategory,TravelDistances,FuelFunctionType));
        
        //reset to modified parent back 
        parent1Genome = parents.get(0).getGenome();
        
        //creating child 2
        for(int i = 0; i < breakpoint; i++){
            int newVal = parent1Genome.get(i);
            Collections.swap(parent2Genome, parent2Genome.indexOf(newVal), i);
        }
        children.add(new GAGenome(parent2Genome,numberOfPorts,IndexSelectedStartPort,
                IndexToPortMatrix,SelectedShipCategory,TravelDistances,FuelFunctionType));
        
        return children;
    }
    
    public GAGenome mutate (GAGenome child){
        Random random = new Random();
        float mutate =  random.nextFloat();
        if(mutate < mutationRate){
            //mutate return a new GAGenome
            List<Integer> genome = child.getGenome();
            Collections.swap(genome, random.nextInt(genomeSize), random.nextInt(genomeSize));
            return new GAGenome (genome,numberOfPorts,IndexSelectedStartPort,
                    IndexToPortMatrix,SelectedShipCategory,TravelDistances,FuelFunctionType);
        }
        //else return same
        return child;
    }
    
    public List<GAGenome> createGeneration(List<GAGenome> population){
        List<GAGenome> newGeneration  = new ArrayList<>();
        int currentGenerationSize = 0;
        
        while(currentGenerationSize < generationSize){
            List<GAGenome> parents = pickRandomElements(population, 2);
            List<GAGenome> children = crossover(parents);
            
            children.set(0,mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));
            
            newGeneration.addAll(children);
            
            currentGenerationSize += 2;
        }
        return newGeneration;
    }
    
    public GAGenome optimize(){
        List<GAGenome> population  = initialPopulation();
        GAGenome globalBestGenome = population.get(0);
        
        for(int i = 0; i< this.MaxIterations; i++){//mainly it only select and mate
            List<GAGenome> selected = selection(population);     //bestfit
            population = createGeneration(selected);    //mating
            System.out.println("Population No: "+i);
            globalBestGenome = Collections.min(population); //does not compare globalBestGenome, only replace it and use it to see if we are close to heuristic or not
            if(globalBestGenome.getFitness()< this.TargetOperationCostFitness)
                break;
        }
        
        return globalBestGenome;
    }
}


