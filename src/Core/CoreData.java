/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;
import java.io.Serializable;

/**
 *
 * @author Loo Alex
 * contains all general data that are needed anywhere
 */
public final class CoreData implements Serializable{
    private CoreData(){
        throw new IllegalAccessError("Instanciation not allow");
    }
    //Variables
    public static String classDirectory =  System.getProperty("user.dir") ; //return path of project folder
    public static String Data_ResourceFilePath = "/src/Data_Resource";
    
    public static float TimeHorizon;
}
