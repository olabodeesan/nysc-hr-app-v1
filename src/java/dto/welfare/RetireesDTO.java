/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.welfare;

import java.io.Serializable;

/**
 *
 * @author Gainsolutions
 */
public class RetireesDTO implements Serializable {

    private String recordId;
    private String fileNo;
    private String employeeRecordId;
    private String surname;
    private String otherNames;
    private String grdadeLevel;
    private String presentLocation, presentLocationDesc;
    private String stateOfOrigin, stateOfOriginDesc;
    private String currentAnnualEmulument, currentAnnualEmulumentDesc;
    private String dateDueForRetirement, dateDueForRetirementDesc;

    private String kilometres;
    private String dgApprovalClaimStatus;
    private String dgApprovalDate, dgApprovalDateDesc;
    private String claimPaymentStatus;
    private String claimPaymentDate, claimPaymentDateDesc;
    private String totalClaimAmount, totalClaimAmountDesc;

    private String retirementClaim, retirementClaimDesc;

    private String cummulativeTotalClaim, cummulativeTotalClaimDesc;

    private String created, createdBy;
    private String lastMod, lastModBy;

    private boolean claimProcessed;
    private boolean claimsApproved;
    private boolean claimsPaid;

    public RetireesDTO() {
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getEmployeeRecordId() {
        return employeeRecordId;
    }

    public void setEmployeeRecordId(String employeeRecordId) {
        this.employeeRecordId = employeeRecordId;
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

    public String getGrdadeLevel() {
        return grdadeLevel;
    }

    public void setGrdadeLevel(String grdadeLevel) {
        this.grdadeLevel = grdadeLevel;
    }

    public String getPresentLocation() {
        return presentLocation;
    }

    public void setPresentLocation(String presentLocation) {
        this.presentLocation = presentLocation;
    }

    public String getPresentLocationDesc() {
        return presentLocationDesc;
    }

    public void setPresentLocationDesc(String presentLocationDesc) {
        this.presentLocationDesc = presentLocationDesc;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getStateOfOriginDesc() {
        return stateOfOriginDesc;
    }

    public void setStateOfOriginDesc(String stateOfOriginDesc) {
        this.stateOfOriginDesc = stateOfOriginDesc;
    }

    public String getCurrentAnnualEmulument() {
        return currentAnnualEmulument;
    }

    public void setCurrentAnnualEmulument(String currentAnnualEmulument) {
        this.currentAnnualEmulument = currentAnnualEmulument;
    }

    public String getCurrentAnnualEmulumentDesc() {
        return currentAnnualEmulumentDesc;
    }

    public void setCurrentAnnualEmulumentDesc(String currentAnnualEmulumentDesc) {
        this.currentAnnualEmulumentDesc = currentAnnualEmulumentDesc;
    }

    public String getKilometres() {
        return kilometres;
    }

    public void setKilometres(String kilometres) {
        this.kilometres = kilometres;
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

    public String getDgApprovalDateDesc() {
        return dgApprovalDateDesc;
    }

    public void setDgApprovalDateDesc(String dgApprovalDateDesc) {
        this.dgApprovalDateDesc = dgApprovalDateDesc;
    }

    public String getClaimPaymentStatus() {
        return claimPaymentStatus;
    }

    public void setClaimPaymentStatus(String claimPaymentStatus) {
        this.claimPaymentStatus = claimPaymentStatus;
    }

    public String getClaimPaymentDate() {
        return claimPaymentDate;
    }

    public void setClaimPaymentDate(String claimPaymentDate) {
        this.claimPaymentDate = claimPaymentDate;
    }

    public String getClaimPaymentDateDesc() {
        return claimPaymentDateDesc;
    }

    public void setClaimPaymentDateDesc(String claimPaymentDateDesc) {
        this.claimPaymentDateDesc = claimPaymentDateDesc;
    }

    public String getTotalClaimAmount() {
        return totalClaimAmount;
    }

    public void setTotalClaimAmount(String totalClaimAmount) {
        this.totalClaimAmount = totalClaimAmount;
    }

    public String getTotalClaimAmountDesc() {
        return totalClaimAmountDesc;
    }

    public void setTotalClaimAmountDesc(String totalClaimAmountDesc) {
        this.totalClaimAmountDesc = totalClaimAmountDesc;
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

    public String getDateDueForRetirement() {
        return dateDueForRetirement;
    }

    public void setDateDueForRetirement(String dateDueForRetirement) {
        this.dateDueForRetirement = dateDueForRetirement;
    }

    public String getDateDueForRetirementDesc() {
        return dateDueForRetirementDesc;
    }

    public void setDateDueForRetirementDesc(String dateDueForRetirementDesc) {
        this.dateDueForRetirementDesc = dateDueForRetirementDesc;
    }

    public String getRetirementClaim() {
        return retirementClaim;
    }

    public void setRetirementClaim(String retirementClaim) {
        this.retirementClaim = retirementClaim;
    }

    public String getRetirementClaimDesc() {
        return retirementClaimDesc;
    }

    public void setRetirementClaimDesc(String retirementClaimDesc) {
        this.retirementClaimDesc = retirementClaimDesc;
    }

    public String getCummulativeTotalClaim() {
        return cummulativeTotalClaim;
    }

    public void setCummulativeTotalClaim(String cummulativeTotalClaim) {
        this.cummulativeTotalClaim = cummulativeTotalClaim;
    }

    public String getCummulativeTotalClaimDesc() {
        return cummulativeTotalClaimDesc;
    }

    public void setCummulativeTotalClaimDesc(String cummulativeTotalClaimDesc) {
        this.cummulativeTotalClaimDesc = cummulativeTotalClaimDesc;
    }

    public boolean isClaimProcessed() {
        return claimProcessed;
    }

    public void setClaimProcessed(boolean claimProcessed) {
        this.claimProcessed = claimProcessed;
    }

    public boolean isClaimsApproved() {
        return claimsApproved;
    }

    public void setClaimsApproved(boolean claimsApproved) {
        this.claimsApproved = claimsApproved;
    }

    public boolean isClaimsPaid() {
        return claimsPaid;
    }

    public void setClaimsPaid(boolean claimsPaid) {
        this.claimsPaid = claimsPaid;
    }

}
