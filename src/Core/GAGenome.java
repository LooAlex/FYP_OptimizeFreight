/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import java.util.*;
/**
 *
 * @author Loo Alex
 */
public class GAGenome implements Comparable<GAGenome>{

    List<Integer> genome;
    int startingPort;
    int numberOfPorts;
    int[][] travelMatrix;
    double fitness;

    public List<Integer> getGenome() {
        return genome;
    }

    public void setGenome(List<Integer> genome) {
        this.genome = genome;
    }

    public int getStatingPort() {
        return startingPort;
    }

    public void setStatingPort(int statingPort) {
        this.startingPort = statingPort;
    }

    public int getNumberOfPorts() {
        return numberOfPorts;
    }

    public void setNumberOfPorts(int numberOfPorts) {
        this.numberOfPorts = numberOfPorts;
    }

    public int[][] getTravelMatrix() {
        return travelMatrix;
    }

    public void setTravelMatrix(int[][] travelMatrix) {
        this.travelMatrix = travelMatrix;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public GAGenome(int numberOfPorts, int [][]travelMatrix,int startingPort) {
        this.travelMatrix  = travelMatrix;
        this.numberOfPorts  = numberOfPorts;
        this.startingPort = startingPort;
        
        this.genome = randomPorts();
        this.fitness = this.calculateFitness();
        
    }
    
    private List<Integer> randomPorts(){
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i<numberOfPorts; i++){
            if(i != startingPort)
                result.add(i);
        }
        Collections.shuffle(result);
        return result;
    }
    
    //how to calculate this fitness path?
    
    public double calculateFitness(){
        int fitness = 0;
        int currentPort  = this.startingPort;
        
        //calculate entire path cost.
        for(int gene : genome){
            fitness += travelMatrix[currentPort][gene];//currentCity = rowIndex, gene = colIndex, cell at that coord = cost to travel there
            currentPort = gene;
        }
        
        fitness += travelMatrix[genome.get(numberOfPorts-2)][startingPort];
        
        return fitness;
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
    
}
