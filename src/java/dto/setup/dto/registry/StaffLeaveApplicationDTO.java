/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.setup.dto.registry;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author IronHide
 */
public class StaffLeaveApplicationDTO implements Serializable{
    
    private String recordId;
    private String employeeRecordId, employeeFileNo, surname, otherNames, department, phone, email, gradeLevel;
    private String leaveApplicationYear;
    private String duration;
    private Date startDate, endDate;
    private String expectedStartDate,oldExpectedStartDate;
    private String expectedEndDate;
    private String beforeApprovalRemainingDays;
    private String afterApprovalRemainingDays;
    private String approvalStatus;
    private String remarks;
    private String recordStatus;
    private String created;
    private String createdBy;
    private String lastMod;
    private String lastModBy;

    public StaffLeaveApplicationDTO() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getLeaveApplicationYear() {
        return leaveApplicationYear;
    }

    public void setLeaveApplicationYear(String leaveApplicationYear) {
        this.leaveApplicationYear = leaveApplicationYear;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getExpectedStartDate() {
        return expectedStartDate;
    }

    public void setExpectedStartDate(String expectedStartDate) {
        this.expectedStartDate = expectedStartDate;
    }

    public String getOldExpectedStartDate() {
        return oldExpectedStartDate;
    }

    public void setOldExpectedStartDate(String oldExpectedStartDate) {
        this.oldExpectedStartDate = oldExpectedStartDate;
    }

    public String getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(String expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getBeforeApprovalRemainingDays() {
        return beforeApprovalRemainingDays;
    }

    public void setBeforeApprovalRemainingDays(String beforeApprovalRemainingDays) {
        this.beforeApprovalRemainingDays = beforeApprovalRemainingDays;
    }

    public String getAfterApprovalRemainingDays() {
        return afterApprovalRemainingDays;
    }

    public void setAfterApprovalRemainingDays(String afterApprovalRemainingDays) {
        this.afterApprovalRemainingDays = afterApprovalRemainingDays;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
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
