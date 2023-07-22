/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testArena.tuto_GA;

import java.util.*;

/**
 *
 * @author Loo Alex
 */
public class TravelingSaleman {
    private int generationSize;     //population size and is fix, represen the number of genome/final solutions
    private int genomeSize;         //length of genome Arraylist, which will be  numberOfCities-1 because of starting city
    private int numberOfCities;     //why genomeSize and numberOfCities seperated?
    private int reproductionSize;   //number of genome selected to reproduce to make the next generation, 2 pair of parent = 2 chilren
    private int maxIterations;      //the max number of generation the system will evolved before stopping
    private float mutationRate;     //the frequency of mutation when creating new generation(its after mating)
    private int [][] travelPrices;  //price to travel between each city, 0 diagonal symetrical in lower and upper triangle
    private int startingCity;       //index of starting city we choose
    private int targetFitness;      //use like a benchmark to terminate program when we know a targe minimum, if we dont know, well set  it lowest.
    private int tournamentSize;     //size of tournament for tournament selection
    private SelectionType selectionType;    //will determine the type of selection, will it be tournament or roulette.,
    

    
    public TravelingSaleman (int numberofCities,SelectionType type, int [][] travelPrices, int startingCity, int targetFitness){
        this.numberOfCities = numberofCities;
        this.genomeSize = numberofCities-1; //numCities = 12, so 12 genes, - 1 to remove starting gene that wont change
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.targetFitness = targetFitness;
        this.selectionType = type;
        generationSize = 5000;          //number of salesmanGenome in a pop
        reproductionSize = 200;         //number of peep to breed, used in selection() to choose next
        maxIterations =1000;    //max loop 
        mutationRate= 0.1f; //10% mutate
        tournamentSize = 40;//40 people to fight
        
        
    }
    public List<SalesmanGenome> initialPopulation(){
        List<SalesmanGenome> population  = new ArrayList<>();
        for(int i = 0 ;i<generationSize ; i++){
            population.add(new SalesmanGenome(numberOfCities,travelPrices,startingCity)); //first pop we use randomConstructor
        }
        
        return population;
    }
    //base on the selectionType, it determine the reproductionSize
    public List<SalesmanGenome> selection (List<SalesmanGenome> population){
        List<SalesmanGenome> selected =  new ArrayList<>();
        SalesmanGenome winner; //this has a genome-> list of cities, number of ciities, and fitness.
        for(int i = 0 ; i <reproductionSize; i++){
            if(selectionType == SelectionType.ROULETTE){
                selected.add(rouletteSelection(population));    //return only 1 salesGenome
            }else if (selectionType == SelectionType.TOURNAMENT){
                selected.add(selectionTournament(population)); //return only 1 salesGenome
            }
            
            //each loop has 1 purpose select 1 salesGenome to pass onto the next generation until we hit the reproductionSize
            //so, the selectedSize will of size 200 only
        }
        return selected;
    }

    public SalesmanGenome rouletteSelection(List<SalesmanGenome>population){
        int totalFitness = population.stream().map(SalesmanGenome::getFitness).mapToInt(Integer::intValue).sum();
        //vatList.stream().map(T :: functioninT).mapToInt(
        
        //pick random value
        Random random = new Random();
        int selectedValue = random.nextInt(totalFitness); //totalFitness = popFitness which if popFitness is 1000, the random value is 0-999, if 1 comes out, a lot
            
    // Because we're doing minimization, we need to use reciprocal
    // value so the probability of selecting a genome would be
    // inversely proportional to its fitness - the smaller the fitness
    // the higher the probability of genomes in this population to pass
        float recValue = (float)1/selectedValue; 
        
        //We add up values until we reach our recValue and we pick the genome that cross the threshold, for below
        float currentSum=0;
        for(SalesmanGenome genome : population){
            currentSum +=(float)1/genome.getFitness();
            if(currentSum >= recValue){
                return genome;//the moment someone pass >=recValue, rouletterSelectionStop, so it can stop at index 0 even!
            }
        }
        
        //in case the return above did not happen, because no one reach threshold, we choose at random
        int selectedRandom = random.nextInt(generationSize);
        return population.get(selectedRandom); //we generated a random index from generationSize which will be number of individual in pop, so index will always be someone.    
    }
    
    //TOURNAMENT
    //a helper function to pick n random elements from the  population
    //so we could enter them in a tournament
    public static <E> List<E> pickRandomElements(List<E> list, int n){
        Random r = new Random();
        int length = list.size();//list = size of population, generationSize here
        
        if(length < n) return null;//exmaple pass in  a pop of 10 and i want 6 to enter tournament
        //  if length = 10, n = 6
        for(int i = length-1; i >= length - n; --i){ //i = 9, i> 4, i--, so it will do 6 iteration, and starting index is last in list
            Collections.swap(list, i, r.nextInt(i+1));//r.nextInt(num) is 0 inclusive and num exclusive so value from this is 0-9 
            //iter max = 6 when n = 6 and length  = 10, and condition stop < 10-6 = <4
            //i = 9, swap 9 with any random position less than itself
            //i= 8 swap index 8 with any random position less than itself
            //at the end we will have a randomized right side that we will take away and we are sure are the random number we swapped with.
            //as each time we swap, we dont return on same index there
            
        }
        return list.subList(length-n,length); //taking only the indexes we applied Collections.swap on.
    }
    
    public SalesmanGenome selectionTournament(List<SalesmanGenome>population){
        List<SalesmanGenome> selected = pickRandomElements(population, this.tournamentSize);
        return Collections.min(selected);//using the comparor implement we made in SalesGenome to find Genome with min.  
    }
    
    
    public List<SalesmanGenome> crossover (List<SalesmanGenome> parents){
        Random random = new Random();
        int breakpoint = random.nextInt(genomeSize); //genomeSize or a genome is excluding starting and final destination since the are same. notes and should not change.
        List<SalesmanGenome> children = new ArrayList<>();
        
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
        
        children.add(new SalesmanGenome(parent1Genome,numberOfCities,travelPrices,startingCity));
        
        //reset to modified parent back 
        parent1Genome = parents.get(0).getGenome();
        
        for(int i = 0; i < breakpoint; i++){
            int newVal = parent1Genome.get(i);
            Collections.swap(parent2Genome, parent2Genome.indexOf(newVal), i);
        }
        children.add(new SalesmanGenome(parent2Genome,numberOfCities,travelPrices,startingCity));
        
        return children;
        
        
    }
    
    
    //mutation, if we reach beyond a certian prob, we swap 2 random genome in a child
    
    public SalesmanGenome mutate (SalesmanGenome salesman){
        Random random = new Random();
        float mutate =  random.nextFloat();
        if(mutate < mutationRate){
            List<Integer> genome = salesman.getGenome();
            Collections.swap(genome, random.nextInt(genomeSize), random.nextInt(genomeSize));
            return new SalesmanGenome (genome,numberOfCities,travelPrices,startingCity);
        }//if it mutate, return a new saleman
        
        return salesman;
    }
    
    //Make new Gen
    //usign generation algo, so we make entirely new population of children
    public List<SalesmanGenome> createGeneration(List<SalesmanGenome> population){
        List<SalesmanGenome> newGeneration  = new ArrayList<>();
        int currentGenerationSize = 0;
        
        while(currentGenerationSize < generationSize){
            List<SalesmanGenome> parents = pickRandomElements(population, 2);
            List<SalesmanGenome> children = crossover(parents);
            //we could use a for loop to loop through all children but that expensive, so we use static since we ever gonna use 2 parent
            children.set(0,mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));//before we add children to new pop, we see if they will mutate
            
            newGeneration.addAll(children);
            
            currentGenerationSize += 2; //because we got 2 childrens, from mixing 2 parent
            
            
        }
        
        return newGeneration;
    }
    
    public SalesmanGenome optimize(){
        List<SalesmanGenome> population  = initialPopulation();
        SalesmanGenome globalBestGenome = population.get(0);
        
        for(int i = 0; i< maxIterations; i++){//mainly it only select and mate
            List<SalesmanGenome> selected = selection(population);
            population = createGeneration(selected);
            globalBestGenome = Collections.min(population); //does not compare globalBestGenome, only replace it and use it to see if we are close to heuristic or not
            if(globalBestGenome.getFitness()< targetFitness)
                break;
        }
        
        return globalBestGenome;
    }
}//end

