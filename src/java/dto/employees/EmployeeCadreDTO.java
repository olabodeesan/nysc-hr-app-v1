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
public class EmployeeCadreDTO implements Serializable{
    
    private String recordId;
    private String employeeRecordId;
    private String cadre, cadreDesc;
    private String dateAppointed,dateAppointedDesc;

    public EmployeeCadreDTO() {
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getEmployeeRecordId() {
        return employeeRecordId;
    }

    public void setEmployeeRecordId(String employeeRecordId) {
        this.employeeRecordId = employeeRecordId;
    }

    public String getCadre() {
        return cadre;
    }

    public void setCadre(String cadre) {
        this.cadre = cadre;
    }

    public String getCadreDesc() {
        return cadreDesc;
    }

    public void setCadreDesc(String cadreDesc) {
        this.cadreDesc = cadreDesc;
    }

    public String getDateAppointed() {
        return dateAppointed;
    }

    public void setDateAppointed(String dateAppointed) {
        this.dateAppointed = dateAppointed;
    }

    public String getDateAppointedDesc() {
        return dateAppointedDesc;
    }

    public void setDateAppointedDesc(String dateAppointedDesc) {
        this.dateAppointedDesc = dateAppointedDesc;
    }
    
    
    
}
