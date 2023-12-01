/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.discipline;

import java.io.Serializable;

/**
 *
 * @author IronHide
 */
public class DisciplineDTO implements Serializable{
    
    private String recordId;
    private String employeeRecordId,employeeFileNo;
    private String caseLeveled;
    private String managementDecision;
    private String dateOfSanction,dateOfSanctionDesc;
    private String dateOfClosure,dateOfClosureDesc;
    
    private String created,createdBy;
    private String lastMod,lastModBy;

    public DisciplineDTO() {
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

    public String getEmployeeFileNo() {
        return employeeFileNo;
    }

    public void setEmployeeFileNo(String employeeFileNo) {
        this.employeeFileNo = employeeFileNo;
    }

    public String getCaseLeveled() {
        return caseLeveled;
    }

    public void setCaseLeveled(String caseLeveled) {
        this.caseLeveled = caseLeveled;
    }

    public String getManagementDecision() {
        return managementDecision;
    }

    public void setManagementDecision(String managementDecision) {
        this.managementDecision = managementDecision;
    }

    public String getDateOfSanction() {
        return dateOfSanction;
    }

    public void setDateOfSanction(String dateOfSanction) {
        this.dateOfSanction = dateOfSanction;
    }

    public String getDateOfSanctionDesc() {
        return dateOfSanctionDesc;
    }

    public void setDateOfSanctionDesc(String dateOfSanctionDesc) {
        this.dateOfSanctionDesc = dateOfSanctionDesc;
    }

    public String getDateOfClosure() {
        return dateOfClosure;
    }

    public void setDateOfClosure(String dateOfClosure) {
        this.dateOfClosure = dateOfClosure;
    }

    public String getDateOfClosureDesc() {
        return dateOfClosureDesc;
    }

    public void setDateOfClosureDesc(String dateOfClosureDesc) {
        this.dateOfClosureDesc = dateOfClosureDesc;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastMod() {
        return lastMod;
    }

    public void setLastMod(String lastMod) {
        this.lastMod = lastMod;
    }

    public String getLastModBy() {
        return lastModBy;
    }

    public void setLastModBy(String lastModBy) {
        this.lastModBy = lastModBy;
    }
    
     
}
