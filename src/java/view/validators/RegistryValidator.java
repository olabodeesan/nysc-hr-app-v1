/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.validators;

import dto.setup.dto.registry.AnnualLeaveRosterDTO;
import dto.setup.dto.registry.FileRequestDTO;
import dto.setup.dto.registry.StaffLeaveApplicationDTO;
import java.io.Serializable;
import java.util.Calendar;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class RegistryValidator implements Serializable {

    private final ViewHelper viewHelper;

    public RegistryValidator() {
        viewHelper = new ViewHelper();
    }

    public boolean validateFileRequest(FacesContext context, FileRequestDTO dto, boolean isReturn) {

        boolean result = true;

        boolean check_reg_fileno = StringUtils.isBlank(dto.getRegistryFileNo());
        if (check_reg_fileno) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "File number is required", null);
        }

        boolean check_reg_filename = StringUtils.isBlank(dto.getRegistryFileName());
        if (check_reg_filename) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "File name is required", null);
        }

        boolean check_officer_info = StringUtils.isBlank(dto.getRequestingOfficerInfo());
        if (check_officer_info) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Requesting Officer information is required", null);
        }

        boolean check_purpose = StringUtils.isBlank(dto.getPurpose());
        if (check_purpose) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Purpose of request is required", null);
        }

        boolean check_request_date = GenericValidator.isDate(dto.getRequestDate(), "dd/MM/yyyy", true);
        if (!check_request_date) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid request date", null);
        }

        boolean check_request_time = StringUtils.isBlank(dto.getRequestTime());
        if (check_request_time) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Request time is required", null);
        }

        //boolean check_inout_status = StringUtils.isBlank(dto.getInOutStatus());
        //if (check_inout_status) {
        //    result = false;
        //    ViewHelper.addErrorMessage(context, null, "IN/OUT status is required", null);
        //}
        if (isReturn) {
            boolean check_return_date = GenericValidator.isDate(dto.getReturnDate(), "dd/MM/yyyy", true);
            if (!check_return_date) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Invalid return date", null);
            }

            boolean check_return_time = StringUtils.isBlank(dto.getReturnTime());
            if (check_return_time) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Return time is required", null);
            }
        }

        return result;

    }

    public boolean validateAnnualLeaveRoster(FacesContext context, AnnualLeaveRosterDTO dto) {

        boolean result = true;

        String roster_year = dto.getRosterYear();
        boolean check_roster_year = GenericValidator.isDate(roster_year, "yyyy", true);
        if (!check_roster_year) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid roster year", null);
        }

        //check that it same year
        if (check_roster_year) {
            Calendar now = Calendar.getInstance();
            int now_year = now.get(Calendar.YEAR);
            boolean is_same_year = now_year == NumberUtils.toInt(roster_year, 0000);
            if (!is_same_year) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Invalid roster year", null);
            }
        }

        boolean check_employee_nos = StringUtils.isBlank(dto.getEmployeeFileNo())
                || StringUtils.isBlank(dto.getEmployeeRecordId());
        if (check_employee_nos) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid personnel record", null);
        }

        String duration = dto.getDuration();
        boolean check_duration = GenericValidator.isInt(duration);
        if (!check_duration) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid duration", null);
        }

        if (check_duration) {
            if (!(NumberUtils.toInt(duration) > 0)) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Duration must be greater than zero", null);
            }
        }

        String startDate = viewHelper.getDateAsDMYWithSlashString(dto.getStartDate());
        boolean check_start_date = GenericValidator.isDate(startDate, "dd/MM/yyyy", true);
        if (!check_start_date) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid start date", null);
        }

        String endDate = viewHelper.getDateAsDMYWithSlashString(dto.getEndDate());
        boolean check_end_date = GenericValidator.isDate(endDate, "dd/MM/yyyy", true);
        if (!check_end_date) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid end date", null);
        }

        return result;

    }

    public boolean validateStaffLeaveApplication(FacesContext context, StaffLeaveApplicationDTO dto, boolean should_check_status) {

        boolean result = true;

        String roster_year = dto.getLeaveApplicationYear();
        boolean check_roster_year = GenericValidator.isDate(roster_year, "yyyy", true);
        if (!check_roster_year) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid roster year", null);
        }

        //check that it same year
        if (check_roster_year) {
            Calendar now = Calendar.getInstance();
            int now_year = now.get(Calendar.YEAR);
            boolean is_same_year = now_year == NumberUtils.toInt(roster_year, 0000);
            if (!is_same_year) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Invalid roster year", null);
            }
        }

        boolean check_employee_nos = StringUtils.isBlank(dto.getEmployeeFileNo())
                || StringUtils.isBlank(dto.getEmployeeRecordId());
        if (check_employee_nos) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid personnel record", null);
        }

        String duration = dto.getDuration();
        boolean check_duration = GenericValidator.isInt(duration);
        if (!check_duration) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid duration", null);
        }

        if (check_duration) {
            if (!(NumberUtils.toInt(duration) > 0)) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Duration must be greater than zero", null);
            }
        }

        String startDate = viewHelper.getDateAsDMYWithSlashString(dto.getStartDate());
        boolean check_start_date = GenericValidator.isDate(startDate, "dd/MM/yyyy", true);
        if (!check_start_date) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid start date", null);
        }

        String endDate = viewHelper.getDateAsDMYWithSlashString(dto.getEndDate());
        boolean check_end_date = GenericValidator.isDate(endDate, "dd/MM/yyyy", true);
        if (!check_end_date) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid end date", null);
        }

        if (should_check_status) {
            boolean check_approval_status = StringUtils.isBlank(dto.getApprovalStatus());
            if (check_approval_status) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Approval status is required", null);
            }

            boolean check_record_status = StringUtils.isBlank(dto.getRecordStatus());
            if (check_record_status) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Record status is required", null);
            }
        }

        return result;

    }

}
