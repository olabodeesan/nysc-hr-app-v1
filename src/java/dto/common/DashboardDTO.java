/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.common;

import java.io.Serializable;

/**
 *
 * @author IronHide
 */
public class DashboardDTO implements Serializable{
    
    private int activePersonnel;
    private int retiringSoon;
    private int overdueRetirement;

    public DashboardDTO() {
    }

    public int getActivePersonnel() {
        return activePersonnel;
    }

    public void setActivePersonnel(int activePersonnel) {
        this.activePersonnel = activePersonnel;
    }

    public int getRetiringSoon() {
        return retiringSoon;
    }

    public void setRetiringSoon(int retiringSoon) {
        this.retiringSoon = retiringSoon;
    }

    public int getOverdueRetirement() {
        return overdueRetirement;
    }

    public void setOverdueRetirement(int overdueRetirement) {
        this.overdueRetirement = overdueRetirement;
    }    
    
}
