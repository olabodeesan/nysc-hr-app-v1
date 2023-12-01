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
public class NewlyPostedWelfareDTO implements Serializable {

    private String recordId;
    private String fileNo;
    private String employeeRecordId;
    private String surname;
    private String otherNames;
    private String grdadeLevel;
    private String prevLocation, prevLocationDesc;
    private String newLocation, newLocationDesc;
    private String effectiveDate, effectiveDateDesc;
    private String postingReason, postingReasonDesc;
    private String kilometres;

    private String dgApprovalClaimStatus;
    private String dgApprovalDate, dgApprovalDateDesc;

    private String claimPaymentStatus;
    private String claimPaymentDate, claimPaymentDateDesc;
    private String totalClaimAmount, totalClaimAmountDesc;

    private String isForwardedForClaims;
    private String isRecentPosting;
    private String hasDgApprovedClaims;
    private String dateForwardedForClaims;
    private String dateApprovedByDg;

    private String cummulativeTotalClaim, cummulativeTotalClaimDesc;

    private String created, createdBy;
    private String lastMod, lastModBy;

    private boolean claimsApproved;
    private boolean claimsPaid;

    public NewlyPostedWelfareDTO() {
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

    public String getPrevLocation() {
        return prevLocation;
    }

    public void setPrevLocation(String prevLocation) {
        this.prevLocation = prevLocation;
    }

    public String getPrevLocationDesc() {
        return prevLocationDesc;
    }

    public void setPrevLocationDesc(String prevLocationDesc) {
        this.prevLocationDesc = prevLocationDesc;
    }

    public String getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(String newLocation) {
        this.newLocation = newLocation;
    }

    public String getNewLocationDesc() {
        return newLocationDesc;
    }

    public void setNewLocationDesc(String newLocationDesc) {
        this.newLocationDesc = newLocationDesc;
    }

    public String getPostingReason() {
        return postingReason;
    }

    public void setPostingReason(String postingReason) {
        this.postingReason = postingReason;
    }

    public String getPostingReasonDesc() {
        return postingReasonDesc;
    }

    public void setPostingReasonDesc(String postingReasonDesc) {
        this.postingReasonDesc = postingReasonDesc;
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

    public String getKilometres() {
        return kilometres;
    }

    public void setKilometres(String kilometres) {
        this.kilometres = kilometres;
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

    public String getDgApprovalDateDesc() {
        return dgApprovalDateDesc;
    }

    public void setDgApprovalDateDesc(String dgApprovalDateDesc) {
        this.dgApprovalDateDesc = dgApprovalDateDesc;
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
