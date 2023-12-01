/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.validators;

import dto.setup.dto.training.StaffFurtherStudyApplicationDTO;
import dto.setup.dto.training.TrainingProposalDTO;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import view.utils.AppConstants;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class TrainingValidator implements Serializable {

    private final ViewHelper viewHelper;

    public TrainingValidator() {
        viewHelper = new ViewHelper();
    }

    public boolean validateTrainingProposal(FacesContext context, TrainingProposalDTO dto,
            boolean check_specialization, boolean check_status, boolean is_new_or_approval) {

        boolean result = true;

        boolean check_title = StringUtils.isBlank(dto.getTitle());
        if (check_title) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Title is required", null);
        }

        boolean check_consultant = StringUtils.isBlank(dto.getConsultant());
        if (check_consultant) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Consultant is required", null);
        }

        boolean check_trainingtype = StringUtils.isBlank(dto.getTrainingType());
        if (check_trainingtype) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Training type is required", null);
        }

        boolean check_country = StringUtils.isBlank(dto.getCountry());
        if (check_country) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Country is required", null);
        }

        //check state only if its nigeria
        if (StringUtils.equals(dto.getCountry(), AppConstants.NIGERIA)) {
            boolean check_state = StringUtils.isBlank(dto.getState());
            if (check_state) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "State is required", null);
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

        boolean check_objective = StringUtils.isBlank(dto.getObjective());
        if (check_objective) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Objective is required", null);
        }

        boolean check_expectedcert = StringUtils.isBlank(dto.getExpectedCertificate());
        if (check_expectedcert) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Expected certificate is required", null);
        }

        if (check_status) {
            boolean check_approvalstatus = StringUtils.isBlank(dto.getApprovalStatus());
            if (check_approvalstatus) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Approval status is required", null);
            }
        }

        if (check_specialization) {
            if (dto.getSpecializations().isEmpty()) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Empty training specializations", null);
            }
        }

        if (!is_new_or_approval && StringUtils.equals(dto.getApprovalStatus(), AppConstants.TRAINING_APPROVED)) {
            if (dto.getParticipants().isEmpty()) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Empty training participants", null);
            }
        }

        return result;

    }

    public boolean validateStaffFurtherStudy(FacesContext context, StaffFurtherStudyApplicationDTO dto, boolean shouldCheckStatus) {

        boolean result = true;

        boolean check_employee_nos = StringUtils.isBlank(dto.getEmployeeFileNo())
                || StringUtils.isBlank(dto.getEmployeeRecordId());
        if (check_employee_nos) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid personnel record", null);
        }

        boolean check_inst = StringUtils.isBlank(dto.getInstitution());
        if (check_inst) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Institution is required", null);
        }

        boolean check_course = StringUtils.isBlank(dto.getCourse());
        if (check_course) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Course is required", null);
        }

        boolean check_country = StringUtils.isBlank(dto.getCountry());
        if (check_country) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Country is required", null);
        }

        //check state only if its nigeria
        String country = dto.getCountry();
        if (!StringUtils.isBlank(country) && StringUtils.equals(country, AppConstants.NIGERIA)) {
            boolean check_state = StringUtils.isBlank(dto.getState());
            if (check_state) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "State is required", null);
            }
        } else if (!StringUtils.isBlank(country) && !StringUtils.equals(country, AppConstants.NIGERIA)) {
            boolean check_location = StringUtils.isBlank(dto.getLocation());
            if (check_location) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Location is required", null);
            }
        } else {
            //result = false;
            //ViewHelper.addErrorMessage(context, null, "State/Location is required", null);
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

        if (shouldCheckStatus) {
            boolean check_approvalstatus = StringUtils.isBlank(dto.getApprovalStatus());
            if (check_approvalstatus) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Approval status is required", null);
            }
        }

        return result;

    }

}
