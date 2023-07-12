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
public class CoreData implements Serializable{
    public static String  classDirectory =  System.getProperty("user.dir") ; //return path of project folder
    public static String Data_ResourceFilePath = "/src/Data_Resource";
}
