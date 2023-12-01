/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author IronHide
 */
public class DemoMBean implements Serializable{

    /**
     * Creates a new instance of DemoMBean
     */
    public DemoMBean() {
        
    }
    
    public void buttonAction(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Hello there! Your account is active"));
    }
    
}
