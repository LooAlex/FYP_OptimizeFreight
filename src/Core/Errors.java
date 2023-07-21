/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Loo Alex
 */
public class Errors implements Serializable {
    public List<String> errorMessages;
    public boolean hasErrors = false;

    public boolean isHasErrors() {
        if(errorMessages != null && errorMessages.size() > 0){
            this.hasErrors = true;
        }
        else{
            this.hasErrors = false;
        }
        
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        if(errorMessages != null && errorMessages.size() > 0 && hasErrors == true){
            this.hasErrors = hasErrors;
        }
        else{
            this.hasErrors = false;
        }
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    
    public Errors(){
        isHasErrors();
        errorMessages = new ArrayList<String>(){
            @Override
            public boolean add(String e) {
                if(e != null && !e.isBlank());
                    hasErrors = true;
                    
                return super.add(e); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
            }
        
    };
    }
}
