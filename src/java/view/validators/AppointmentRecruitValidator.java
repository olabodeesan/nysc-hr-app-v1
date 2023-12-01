/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.validators;

import dto.appointment.recruit.EmployeeStatusDTO;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class AppointmentRecruitValidator implements Serializable {

    public AppointmentRecruitValidator() {
    }

    public boolean validateDisengagePersonnel(FacesContext context, EmployeeStatusDTO dto, boolean emptyReasonMap) {
        boolean result = true;

        boolean check_status = StringUtils.isBlank(dto.getStatusId());
        if (check_status) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Mode of exit is required", null);
        }

        if (!emptyReasonMap) {
            boolean check_reason = StringUtils.isBlank(dto.getStatusReason());
            if (check_reason) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Reason for exit is required", null);
            }
        }

        boolean check_effective_date = GenericValidator.isDate(dto.getEffectiveDate(), "dd/MM/yyyy", true);
        if (check_effective_date == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid effective date. Use dd/mm/yyyy", null);
        }

        return result;
    }
    
    public boolean validateSuspendPersonnel(FacesContext context, EmployeeStatusDTO dto, boolean emptyReasonMap) {
        boolean result = true;

        boolean check_status = StringUtils.isBlank(dto.getStatusId());
        if (check_status) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Mode of suspension is required", null);
        }

        if (!emptyReasonMap) {
            boolean check_reason = StringUtils.isBlank(dto.getStatusReason());
            if (check_reason) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Reason for suspension is required", null);
            }
        }

        boolean check_effective_date = GenericValidator.isDate(dto.getEffectiveDate(), "dd/MM/yyyy", true);
        if (check_effective_date == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid effective date. Use dd/mm/yyyy", null);
        }

        return result;
    }
    
    public boolean validateLiftSuspension(FacesContext context, EmployeeStatusDTO dto) {
        boolean result = true;

        boolean check_status = StringUtils.isBlank(dto.getStatusId());
        if (check_status) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Mode of suspension is required", null);
        }

        boolean check_effective_date = GenericValidator.isDate(dto.getEffectiveDate(), "dd/MM/yyyy", true);
        if (check_effective_date == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid effective date. Use dd/mm/yyyy", null);
        }

        return result;
    }

}
