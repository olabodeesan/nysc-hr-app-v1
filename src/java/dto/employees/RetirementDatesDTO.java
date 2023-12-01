/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.employees;

import java.io.Serializable;

/**
 *
 * @author IronHide
 */
public class RetirementDatesDTO implements Serializable{
    
    private boolean outcome;
    private String dateToRetireBasedOnLengthOfStay;
    private String dateToRetireBasedOnAge;
    private String dateDueForRetirement;    

    public RetirementDatesDTO() {
        
    }

    public boolean isOutcome() {
        return outcome;
    }

    public void setOutcome(boolean outcome) {
        this.outcome = outcome;
    }

    public String getDateToRetireBasedOnLengthOfStay() {
        return dateToRetireBasedOnLengthOfStay;
    }

    public void setDateToRetireBasedOnLengthOfStay(String dateToRetireBasedOnLengthOfStay) {
        this.dateToRetireBasedOnLengthOfStay = dateToRetireBasedOnLengthOfStay;
    }

    public String getDateToRetireBasedOnAge() {
        return dateToRetireBasedOnAge;
    }

    public void setDateToRetireBasedOnAge(String dateToRetireBasedOnAge) {
        this.dateToRetireBasedOnAge = dateToRetireBasedOnAge;
    }

    public String getDateDueForRetirement() {
        return dateDueForRetirement;
    }

    public void setDateDueForRetirement(String dateDueForRetirement) {
        this.dateDueForRetirement = dateDueForRetirement;
    }
    
    
}
