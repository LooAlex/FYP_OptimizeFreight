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
        
        if(FuelFunctionType == null){
            System.out.println("Heloooo?");
        }
        this.MaxIterations = 1000;
        this.genomeSize = numberOfPorts-1;
        
    }
    
     public List<GAGenome> initialPopulation(){
        List<GAGenome> population  = new ArrayList<>();
        for(int i = 0 ;i<generationSize ; i++){
            //random GAGenome constructor
        if(FuelFunctionType == null){
            System.out.println("Heloooo?");
        }
            population.add(new GAGenome(numberOfPorts,IndexSelectedStartPort,IndexToPortMatrix,SelectedShipCategory,TravelDistances,FuelFunctionType));
            System.out.println("First Pop: Genene "+i);
        }
        
        return population;
    }
    public List<GAGenome> selection (List<GAGenome> population){
        List<GAGenome> selected =  new ArrayList<>();
        for(int i = 0 ; i <reproductionSize; i++){
            if(selectionType == CoreEnum.SelectionType.ROULETTE){
                selected.add(rouletteSelection(population));    //return only 1 salesGenome
            }else if (selectionType == CoreEnum.SelectionType.TOURNAMENT){
                selected.add(selectionTournament(population)); //return only 1 salesGenome
            }
            
            //each loop has 1 purpose select 1 salesGenome to pass onto the next generation until we hit the reproductionSize
            //so, the selectedSize will of size 200 only
        }
        return selected;
    }
    
    public GAGenome rouletteSelection(List<GAGenome>population){
        double totalFitness = population.stream().map(GAGenome::getFitness).mapToDouble(Double::doubleValue).sum();
        //vatList.stream().map(T :: functioninT).mapToInt(
        
        //pick random value
        Random random = new Random();
        int selectedValue = random.nextInt((int)totalFitness); //totalFitness = popFitness, using this to see which people are feed using random value 
        float recValue = (float)1/selectedValue; //abitrary randomValue threshold
        
        //We add up values until we reach our recValue and we pick the genome that cross the threshold, for below
        float currentSum=0;
        for(GAGenome genome : population){
            currentSum +=(float)1/genome.getFitness();
            if(currentSum >= recValue){
                return genome;//the moment someone pass >=recValue, rouletterSelectionStop, so it can stop at index 0 even!
            }
        }
        
        //in case the return above did not happen, because no one reach threshold, we choose at random
        int selectedRandom = random.nextInt(generationSize);
        return population.get(selectedRandom); //we generated a random index from generationSize which will be number of individual in pop, so index will always be someone.    
    }
    
    public static <E> List<E> pickRandomElements(List<E> list, int n){
        Random r = new Random();
        int length = list.size();//list = size of population, generationSize here
        
        if(length < n) return null;
        
        for(int i = length-1; i >= length - n; --i){ 
            Collections.swap(list, i, r.nextInt(i+1));//r.nextInt(num) is 0 inclusive and num exclusive so value from this is 0-9 
        }
        return list.subList(length-n,length); //taking only the indexes we applied Collections.swap on.
    }
    
    public GAGenome selectionTournament(List<GAGenome>population){
        List<GAGenome> selected = pickRandomElements(population, this.tournamentSize);
        return Collections.min(selected);//using the comparor implement we made in SalesGenome to find Genome with min.  
    }
    
    public List<GAGenome> crossover (List<GAGenome> parents){
        Random random = new Random();
        int breakpoint = random.nextInt(genomeSize); //genomeSize or a genome is excluding starting and final destination since the are same. notes and should not change.
        List<GAGenome> children = new ArrayList<>();
        
        //copy parental genomes - copy to preserve the previous genes incase they are needed in other generation
        List<Integer> parent1Genome = parents.get(0).getGenome();
        List<Integer> parent2Genome = parents.get(1).getGenome();
        
        //Creating Child 1 
        for(int i =0 ; i < breakpoint; i++){
            int newVal;
            newVal  = parent2Genome.get(i);
            Collections.swap(parent1Genome, parent1Genome.indexOf(newVal), i);
            //essenstial, saying: get value, newVal from index i = 0 from parent2, get *index of that newVal from parent1Genome and swap those 2
            //if P2 index:0 has val: 1 and in P1 val:1 is at index 3, then we swap in P1 val at #3, j with val at #0, i 
        }
        if(FuelFunctionType == null){
            System.out.println("Heloooo?");
        }
        children.add(new GAGenome(parent1Genome,numberOfPorts,IndexSelectedStartPort,IndexToPortMatrix,SelectedShipCategory,TravelDistances,FuelFunctionType));
        
        //reset to modified parent back 
        parent1Genome = parents.get(0).getGenome();
        
        for(int i = 0; i < breakpoint; i++){
            int newVal = parent1Genome.get(i);
            Collections.swap(parent2Genome, parent2Genome.indexOf(newVal), i);
        }
        children.add(new GAGenome(parent2Genome,numberOfPorts,IndexSelectedStartPort,IndexToPortMatrix,SelectedShipCategory,TravelDistances,FuelFunctionType));
        
        return children;
    }
    
    public GAGenome mutate (GAGenome salesman){
        Random random = new Random();
        float mutate =  random.nextFloat();
        if(mutate < mutationRate){
            //if it mutate, return a new GAGenome
            List<Integer> genome = salesman.getGenome();
            Collections.swap(genome, random.nextInt(genomeSize), random.nextInt(genomeSize));
            return new GAGenome (genome,numberOfPorts,IndexSelectedStartPort,IndexToPortMatrix,SelectedShipCategory,TravelDistances,FuelFunctionType);
        
        }
        
        return salesman;
    }
    
    public List<GAGenome> createGeneration(List<GAGenome> population){
        List<GAGenome> newGeneration  = new ArrayList<>();
        int currentGenerationSize = 0;
        
        while(currentGenerationSize < generationSize){
            List<GAGenome> parents = pickRandomElements(population, 2);
            List<GAGenome> children = crossover(parents);
            //we could use a for loop to loop through all children but that expensive, so we use static since we ever gonna use 2 parent
            children.set(0,mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));//before we add children to new pop, we see if they will mutate
            
            newGeneration.addAll(children);
            
            currentGenerationSize += 2; //because we got 2 childrens, from mixing 2 parent
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


