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
public class EmployeeLocationDTO implements Serializable {

    private String recordId;
    private String employeeRecordId;
    private String employeeFileNo;
    private String location, locationDesc;
    private String locationFrom, locationFromDesc;
    private String gradeLevel;
    private String department, departmentDesc;
    private String datePosted, datePostedDesc;
    private String isStateCoord, isStateAcct, isSecondment;
    private String isSpecialAppointment;
    private String specialAppointment, specialAppointmentDesc;
    private String lengthOfStay;
    private String postingReason;
    
    
    private String dgApprovalClaimStatus;
    private String dgApprovalDate;
    
    private String paymentStatus;
    private String dateOfPayment;
    
    private String totalClaimAmount;

    private String isForwardedForClaims;
    private String isRecentPosting;
    private String hasDgApprovedClaims;
    private String dateForwardedForClaims;
    private String dateApprovedByDg;

    private String created, createdBy;
    private String lastMod, lastModBy;
    

    public EmployeeLocationDTO() {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentDesc() {
        return departmentDesc;
    }

    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getDatePostedDesc() {
        return datePostedDesc;
    }

    public void setDatePostedDesc(String datePostedDesc) {
        this.datePostedDesc = datePostedDesc;
    }

    public String getIsStateCoord() {
        return isStateCoord;
    }

    public void setIsStateCoord(String isStateCoord) {
        this.isStateCoord = isStateCoord;
    }

    public String getIsStateAcct() {
        return isStateAcct;
    }

    public void setIsStateAcct(String isStateAcct) {
        this.isStateAcct = isStateAcct;
    }

    public String getPostingReason() {
        return postingReason;
    }

    public void setPostingReason(String postingReason) {
        this.postingReason = postingReason;
    }

    public String getIsSpecialAppointment() {
        return isSpecialAppointment;
    }

    public void setIsSpecialAppointment(String isSpecialAppointment) {
        this.isSpecialAppointment = isSpecialAppointment;
    }

    public String getSpecialAppointment() {
        return specialAppointment;
    }

    public void setSpecialAppointment(String specialAppointment) {
        this.specialAppointment = specialAppointment;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
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

    public String getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(String locationFrom) {
        this.locationFrom = locationFrom;
    }

    public String getLocationFromDesc() {
        return locationFromDesc;
    }

    public void setLocationFromDesc(String locationFromDesc) {
        this.locationFromDesc = locationFromDesc;
    }

    public String getIsSecondment() {
        return isSecondment;
    }

    public void setIsSecondment(String isSecondment) {
        this.isSecondment = isSecondment;
    }

    public String getSpecialAppointmentDesc() {
        return specialAppointmentDesc;
    }

    public void setSpecialAppointmentDesc(String specialAppointmentDesc) {
        this.specialAppointmentDesc = specialAppointmentDesc;
    }

    public String getLengthOfStay() {
        return lengthOfStay;
    }

    public void setLengthOfStay(String lengthOfStay) {
        this.lengthOfStay = lengthOfStay;
    }

    public String getDgApprovalClaimStatus() {
        return dgApprovalClaimStatus;
    }

    public void setDgApprovalClaimStatus(String dgApprovalClaimStatus) {
        this.dgApprovalClaimStatus = dgApprovalClaimStatus;
    }

    public String getDgApprovalDate() {
        return dgApprovalDate;
    }

    public void setDgApprovalDate(String dgApprovalDate) {
        this.dgApprovalDate = dgApprovalDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(String dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public String getTotalClaimAmount() {
        return totalClaimAmount;
    }

    public void setTotalClaimAmount(String totalClaimAmount) {
        this.totalClaimAmount = totalClaimAmount;
    }

    public String getIsForwardedForClaims() {
        return isForwardedForClaims;
    }

    public void setIsForwardedForClaims(String isForwardedForClaims) {
        this.isForwardedForClaims = isForwardedForClaims;
    }

    public String getIsRecentPosting() {
        return isRecentPosting;
    }

    public void setIsRecentPosting(String isRecentPosting) {
        this.isRecentPosting = isRecentPosting;
    }

    public String getHasDgApprovedClaims() {
        return hasDgApprovedClaims;
    }

    public void setHasDgApprovedClaims(String hasDgApprovedClaims) {
        this.hasDgApprovedClaims = hasDgApprovedClaims;
    }

    public String getDateForwardedForClaims() {
        return dateForwardedForClaims;
    }

    public void setDateForwardedForClaims(String dateForwardedForClaims) {
        this.dateForwardedForClaims = dateForwardedForClaims;
    }

    public String getDateApprovedByDg() {
        return dateApprovedByDg;
    }

    public void setDateApprovedByDg(String dateApprovedByDg) {
        this.dateApprovedByDg = dateApprovedByDg;
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
