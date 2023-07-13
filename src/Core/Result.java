/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Loo Alex
 * Result is a container that can hold any value in its var Data and if there are errors in any process, 
 * obj Error will be used and bring the message.
 */
public class Result<T extends List<? extends Object>> implements Serializable{
    
    public Error errors;
    public T Data; //to get direct access of the Result. //a Collection englobe all set, and list
        //we will changing to list for now
    
    public Error getErrors() {
        return errors;
    }

    public void setErrors(Error errors) {
        this.errors = errors;
    }  
    
    public Result(){
        errors = new Error();
    }
    
    public Result(List<? extends Object> lstContainer){
        Data = (T) lstContainer;
        errors = new Error();
    }
    //obj+ Errors
    public Result(List<? extends Object> lstContainer, Error errors){
        Data = (T)lstContainer;
        setErrors(errors);
    }
    

}
    
    
    

