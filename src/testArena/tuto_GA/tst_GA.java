/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testArena.tuto_GA;
import java.nio.file.FileSystemNotFoundException;
import java.util.*;
/**
 *
 * @author Loo Alex
 */
public class tst_GA {
    public tst_GA(){
        
        int numberOfCities = 12;
        int[][] travelPrices = new int[numberOfCities][numberOfCities];
        for(int i = 0; i<numberOfCities;i++){
            for(int j = 0; j <numberOfCities; j++){
                Random random =new Random();
                if(i==j){
                    travelPrices[i][j] = 0;
                }else{
                    travelPrices[i][j] = random.nextInt(100);
                    travelPrices[j][i] =travelPrices[i][j]; //make the upper and lower triangle same
                    
                }       
            }
        }
        
        printTravelPrices(travelPrices, numberOfCities);
        //numberOfCities = 3 see at start of main
        //targetFitness =0, so least maximum
        //startingCity = 0, so its id is 0
        TravelingSaleman geneticAlgorithm =  new TravelingSaleman(numberOfCities,SelectionType.ROULETTE,travelPrices,5,0);
        SalesmanGenome result = geneticAlgorithm.optimize();
        System.out.println(result);
    }
    
    
    public static   void printTravelPrices(int [][]TravelPrices, int numberOfCities){
        for(int i = 0; i< numberOfCities; i++){
            for(int j = 0; j < numberOfCities; j++){
                System.out.println(TravelPrices[i][j]);
                if(TravelPrices[i][j]/10==0)
                    System.out.println("  ");//cuz ij = 0, so to make retain space and have like a matrix, we add 2extra space
                else
                    System.out.println(' ');
            }
            System.out.println("");
                
        }
    }
    

}
