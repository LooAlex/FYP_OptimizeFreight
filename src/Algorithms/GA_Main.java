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
public class GA_Main {

    private LinkedList<PortPairDistanceDTO> PairDistances;
    private List<PortDTO> DataPorts;
    private CoreEnum.SelectionType type;
    
    public GA_Main(){
        
    }
    
    public GA_Main(ArrayList<PortDTO> DataPorts,LinkedList<PortPairDistanceDTO> PairDistances, CoreEnum.SelectionType type){
        this.PairDistances = PairDistances;
        
    }
}
