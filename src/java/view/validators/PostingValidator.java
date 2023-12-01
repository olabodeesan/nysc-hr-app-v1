/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.validators;

import java.io.Serializable;
import dto.employees.EmployeeLocationDTO;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import view.utils.ViewHelper;

/**
 *
 * @author Gainsolutions
 */
public class PostingValidator implements Serializable {

    public PostingValidator() {
    }

    public boolean validatePosting(FacesContext context, EmployeeLocationDTO dto) {

        boolean result = true;

        //
           boolean check_employeerecordID = StringUtils.isBlank(dto.getEmployeeRecordId());
        if (check_employeerecordID) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Employee Record Identity is required", null);
        }
        
        boolean check_postingReason = StringUtils.isBlank(dto.getPostingReason());
        if (check_postingReason) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Posting Reason is required", null);
        }

        boolean check_location = StringUtils.isBlank(dto.getLocation());
        if (check_location) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "New Posting Location is required", null);
        }

        //dob
        boolean check_posted = GenericValidator.isDate(dto.getDatePosted(), "dd/MM/yyyy", true);
        if (check_posted == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid date of Posting. Use dd/mm/yyyy", null);
        }

        boolean check_dept = StringUtils.isBlank(dto.getDepartment());
        if (check_dept) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "New Posting Department is required", null);
        }

        boolean check_isspecialAppointment = StringUtils.isBlank(dto.getIsSpecialAppointment());
        if (check_isspecialAppointment) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Special Appointment Check is required", null);
        }

        if (StringUtils.equals(dto.getIsSpecialAppointment(), "Y")) {
            boolean check_specialAppointment = StringUtils.isBlank(dto.getSpecialAppointment());
            if (check_specialAppointment) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Special Appointment is required", null);
            }
        }

        return result;
    }
}
