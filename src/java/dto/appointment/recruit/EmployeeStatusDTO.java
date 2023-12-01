/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.appointment.recruit;

import java.io.Serializable;

/**
 *
 * @author IronHide
 */
public class EmployeeStatusDTO implements Serializable{
    
    private String recordId;
    private String employeeRecordId, employeeFileNo;
    private String statusId, statusDesc;
    private String statusReason,statusReasonDesc;
    private String remarks;
    private String effectiveDate, effectiveDateDesc;
    
    private String created, createdBy;
    private String lastMod;
    private String lastModBy;

    public EmployeeStatusDTO() {
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

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public String getStatusReasonDesc() {
        return statusReasonDesc;
    }

    public void setStatusReasonDesc(String statusReasonDesc) {
        this.statusReasonDesc = statusReasonDesc;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getEffectiveDateDesc() {
        return effectiveDateDesc;
    }

    public void setEffectiveDateDesc(String effectiveDateDesc) {
        this.effectiveDateDesc = effectiveDateDesc;
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
    
    
    
}
