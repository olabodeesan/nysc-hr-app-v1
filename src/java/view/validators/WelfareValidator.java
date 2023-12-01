/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.validators;

import dto.welfare.NewlyPostedWelfareDTO;
import dto.welfare.RetireesDTO;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import view.utils.ViewHelper;

public class WelfareValidator implements Serializable {

    public WelfareValidator() {
    }

    public boolean validateDgApprovalStatus(FacesContext context, NewlyPostedWelfareDTO dto) {
        boolean result = true;

        //
        boolean check_dgApprovalDate = StringUtils.isBlank(dto.getDgApprovalDate());
        if (check_dgApprovalDate) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "DG's Approval Date is required", null);
        }

        boolean check_dgApprovalStatus = StringUtils.isBlank(dto.getDgApprovalClaimStatus());
        if (check_dgApprovalStatus) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "DG's Approval Status is required", null);
        }

        return result;
    }
    
    public boolean validateTransferClaimPaymentStatus(FacesContext context, NewlyPostedWelfareDTO dto) {
        boolean result = true;

        //
        boolean check_paymentDate = StringUtils.isBlank(dto.getClaimPaymentDate());
        if (check_paymentDate) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Claims Payment Date is required", null);
        }

        boolean check_paymentStatus = StringUtils.isBlank(dto.getClaimPaymentStatus());
        if (check_paymentStatus) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Claims Payment Status is required", null);
        }

        return result;
    }
    
    
     public boolean validateDgRetireeApprovalStatus(FacesContext context, RetireesDTO dto) {
        boolean result = true;

        //
        boolean check_dgApprovalDate = StringUtils.isBlank(dto.getDgApprovalDate());
        if (check_dgApprovalDate) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "DG's Approval Date is required", null);
        }

        boolean check_dgApprovalStatus = StringUtils.isBlank(dto.getDgApprovalClaimStatus());
        if (check_dgApprovalStatus) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "DG's Approval Status is required", null);
        }

        return result;
    }
    
    public boolean validateRetireeClaimPaymentStatus(FacesContext context, RetireesDTO dto) {
        boolean result = true;

        //
        boolean check_paymentDate = StringUtils.isBlank(dto.getClaimPaymentDate());
        if (check_paymentDate) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Claims Payment Date is required", null);
        }

        boolean check_paymentStatus = StringUtils.isBlank(dto.getClaimPaymentStatus());
        if (check_paymentStatus) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Claims Payment Status is required", null);
        }

        return result;
    }
    

}
