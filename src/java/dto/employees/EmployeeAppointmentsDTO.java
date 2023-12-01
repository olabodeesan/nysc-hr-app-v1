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
public class EmployeeAppointmentsDTO implements Serializable{
    
    private String recordId;
    private String employeeRecordId;
    private String gradeLevel;
    private String gradeStep;
    private String dateAppointed,dateAppointedDesc;
    private String appointmentCadre,appointmentCadreDesc;
    private String appointmentRank,appointmentRankDesc;

    public EmployeeAppointmentsDTO() {
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

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getGradeStep() {
        return gradeStep;
    }

    public void setGradeStep(String gradeStep) {
        this.gradeStep = gradeStep;
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

    public String getAppointmentCadre() {
        return appointmentCadre;
    }

    public void setAppointmentCadre(String appointmentCadre) {
        this.appointmentCadre = appointmentCadre;
    }

    public String getAppointmentCadreDesc() {
        return appointmentCadreDesc;
    }

    public void setAppointmentCadreDesc(String appointmentCadreDesc) {
        this.appointmentCadreDesc = appointmentCadreDesc;
    }

    public String getAppointmentRank() {
        return appointmentRank;
    }

    public void setAppointmentRank(String appointmentRank) {
        this.appointmentRank = appointmentRank;
    }

    public String getAppointmentRankDesc() {
        return appointmentRankDesc;
    }

    public void setAppointmentRankDesc(String appointmentRankDesc) {
        this.appointmentRankDesc = appointmentRankDesc;
    }
    
}
