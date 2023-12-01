/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.welfare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gainsolutions
 */
public class WelfareClaimReportDTO implements Serializable {

    private String cummulativeTotalClaim, cummulativeTotalClaimDesc;
    List<NewlyPostedWelfareDTO> newtransferClaim, approvedTransferClaim;

    List<NewlyPostedWelfareDTO> unpaidTransferClaim;
    List<NewlyPostedWelfareDTO> paidTransferClaim;

    List<RetireesDTO> retireeClaim, approvedRetireeClaim;
    List<RetireesDTO> unpaidRetireeClaim, paidRetireeClaim;

    private boolean transferclaimApproved, pendingDgApprovalClaim, paidTransfer, unpaidTransfer;

    private boolean retireeclaimApproved, retireeClaimpendingDgApproval, paidRetiree, unpaidRetiree;

    public WelfareClaimReportDTO() {
        newtransferClaim = new ArrayList<>();
        unpaidTransferClaim = new ArrayList<>();
        paidTransferClaim = new ArrayList<>();
        approvedTransferClaim = new ArrayList<>();

        // Retiree Section
        retireeClaim = new ArrayList<>();
        approvedRetireeClaim = new ArrayList<>();
        unpaidRetireeClaim = new ArrayList<>();
        paidRetireeClaim = new ArrayList<>();
        transferclaimApproved = false;
        pendingDgApprovalClaim = false;
        paidTransfer = unpaidTransfer = false;

        retireeclaimApproved = retireeClaimpendingDgApproval = paidRetiree = unpaidRetiree = false;

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

    public List<NewlyPostedWelfareDTO> getNewtransferClaim() {
        return newtransferClaim;
    }

    public void setNewtransferClaim(List<NewlyPostedWelfareDTO> newtransferClaim) {
        this.newtransferClaim = newtransferClaim;
    }

    public List<NewlyPostedWelfareDTO> getUnpaidTransferClaim() {
        return unpaidTransferClaim;
    }

    public void setUnpaidTransferClaim(List<NewlyPostedWelfareDTO> unpaidTransferClaim) {
        this.unpaidTransferClaim = unpaidTransferClaim;
    }

    public List<NewlyPostedWelfareDTO> getPaidTransferClaim() {
        return paidTransferClaim;
    }

    public void setPaidTransferClaim(List<NewlyPostedWelfareDTO> paidTransferClaim) {
        this.paidTransferClaim = paidTransferClaim;
    }

    public List<NewlyPostedWelfareDTO> getApprovedTransferClaim() {
        return approvedTransferClaim;
    }

    public void setApprovedTransferClaim(List<NewlyPostedWelfareDTO> approvedTransferClaim) {
        this.approvedTransferClaim = approvedTransferClaim;
    }

    public List<RetireesDTO> getRetireeClaim() {
        return retireeClaim;
    }

    public void setRetireeClaim(List<RetireesDTO> retireeClaim) {
        this.retireeClaim = retireeClaim;
    }

    public List<RetireesDTO> getApprovedRetireeClaim() {
        return approvedRetireeClaim;
    }

    public void setApprovedRetireeClaim(List<RetireesDTO> approvedRetireeClaim) {
        this.approvedRetireeClaim = approvedRetireeClaim;
    }

    public List<RetireesDTO> getUnpaidRetireeClaim() {
        return unpaidRetireeClaim;
    }

    public void setUnpaidRetireeClaim(List<RetireesDTO> unpaidRetireeClaim) {
        this.unpaidRetireeClaim = unpaidRetireeClaim;
    }

    public List<RetireesDTO> getPaidRetireeClaim() {
        return paidRetireeClaim;
    }

    public void setPaidRetireeClaim(List<RetireesDTO> paidRetireeClaim) {
        this.paidRetireeClaim = paidRetireeClaim;
    }

    public void addNewTransferClaim(NewlyPostedWelfareDTO dto) {
        newtransferClaim.add(dto);
    }

    public void addUnpaidTransferClaim(NewlyPostedWelfareDTO dto) {
        unpaidTransferClaim.add(dto);
    }

    public void addPaidTransferClaim(NewlyPostedWelfareDTO dto) {
        paidTransferClaim.add(dto);
    }

    public void addApprovedTransferClaim(NewlyPostedWelfareDTO dto) {
        approvedTransferClaim.add(dto);
    }

    //Append retiree
    public void addRetireeClaim(RetireesDTO dto) {
        retireeClaim.add(dto);
    }

    public void addApprovedRetireeClaim(RetireesDTO dto) {
        approvedRetireeClaim.add(dto);
    }

    public void addUnpaidRetireeClaim(RetireesDTO dto) {
        unpaidRetireeClaim.add(dto);
    }

    public void addPaidRetireeClaim(RetireesDTO dto) {
        paidRetireeClaim.add(dto);
    }

    public boolean isTransferclaimApproved() {
        return transferclaimApproved;
    }

    public void setTransferclaimApproved(boolean transferclaimApproved) {
        this.transferclaimApproved = transferclaimApproved;
    }

    public boolean isPendingDgApprovalClaim() {
        return pendingDgApprovalClaim;
    }

    public void setPendingDgApprovalClaim(boolean pendingDgApprovalClaim) {
        this.pendingDgApprovalClaim = pendingDgApprovalClaim;
    }

    public boolean isPaidTransfer() {
        return paidTransfer;
    }

    public void setPaidTransfer(boolean paidTransfer) {
        this.paidTransfer = paidTransfer;
    }

    public boolean isUnpaidTransfer() {
        return unpaidTransfer;
    }

    public void setUnpaidTransfer(boolean unpaidTransfer) {
        this.unpaidTransfer = unpaidTransfer;
    }

    public boolean isRetireeclaimApproved() {
        return retireeclaimApproved;
    }

    public void setRetireeclaimApproved(boolean retireeclaimApproved) {
        this.retireeclaimApproved = retireeclaimApproved;
    }

    public boolean isRetireeClaimpendingDgApproval() {
        return retireeClaimpendingDgApproval;
    }

    public void setRetireeClaimpendingDgApproval(boolean retireeClaimpendingDgApproval) {
        this.retireeClaimpendingDgApproval = retireeClaimpendingDgApproval;
    }

    public boolean isPaidRetiree() {
        return paidRetiree;
    }

    public void setPaidRetiree(boolean paidRetiree) {
        this.paidRetiree = paidRetiree;
    }

    public boolean isUnpaidRetiree() {
        return unpaidRetiree;
    }

    public void setUnpaidRetiree(boolean unpaidRetiree) {
        this.unpaidRetiree = unpaidRetiree;
    }

}
