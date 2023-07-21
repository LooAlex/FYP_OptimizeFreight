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
public class SalesmanGenome implements Comparable<SalesmanGenome>{

    public List<Integer> getGenome() {
        return genome;
    }

    public void setGenome(List<Integer> genome) {
        this.genome = genome;
    }

    public int[][] getTravelPrices() {
        return travelPrices;
    }

    public void setTravelPrices(int[][] travelPrices) {
        this.travelPrices = travelPrices;
    }

    public int getStartingCity() {
        return startingCity;
    }

    public void setStartingCity(int startingCity) {
        this.startingCity = startingCity;
    }

    public int getNumberOfCities() {
        return numberOfCities;
    }

    public void setNumberOfCities(int numberOfCities) {
        this.numberOfCities = numberOfCities;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
    List<Integer> genome;
    int [][] travelPrices;
    int startingCity;
    int numberOfCities;
    int fitness;
        
    //random genome, used in initialPopulationFunction.
    public SalesmanGenome(int numberOfCities,int[][] travelPrices, int startingCity){
        this.travelPrices = travelPrices;
        this.numberOfCities = numberOfCities;
        this.startingCity = startingCity;
        
        this.genome = randomSaleman();
        this.fitness = this.calculateFitness();
        
    }
    
    //userDefined genome
    public SalesmanGenome (List<Integer> permutationOfCities,int numberOfCities, int [][]travelPrices,int StartingCity){
        
        this.genome = permutationOfCities;
        this.numberOfCities = numberOfCities;
        this.travelPrices = travelPrices;
        this.startingCity = StartingCity;
        
        this.fitness = this.calculateFitness();
        
    }
    
    //randomGenome, except for starting city which will be same, all the rest will be added and shuffle in a list
    private List<Integer> randomSaleman(){
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i<numberOfCities; i++){
            if(i != startingCity)
                result.add(i);
        }
        Collections.shuffle(result);
        return result;
    }
    
    //fitness calculation, it is a minimization problem, and we will be using the travelingCost matrix to help us
    //this will simply search for next city, and add the cost of this to next city.
    public int calculateFitness(){
        int fitness = 0;
        int currentCity  = this.startingCity;
        
        //calculate entire path cost.
        for(int gene : genome){
            fitness+=travelPrices[currentCity][gene];//currentCity = rowIndex, gene = colIndex, cell at that coord = cost to travel there
            currentCity = gene;
        }
        
        fitness += travelPrices[genome.get(numberOfCities-2)][startingCity];
        
        return fitness;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Path");
        sb.append(startingCity);
        for(int gene:genome){
            sb.append(" ");
            sb.append(gene);

        }
        sb.append(" ");
        sb.append(startingCity);
        sb.append("\nLength");
        sb.append(getFitness());
        return sb.toString();
    }
    
    //Comparable function

    /**
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(SalesmanGenome genome ) {
       
        if(this.fitness > genome.getFitness())
            return 1;
        else if(this.fitness < genome.getFitness())
            return -1;
        else
            return 0;
    }
    
}
