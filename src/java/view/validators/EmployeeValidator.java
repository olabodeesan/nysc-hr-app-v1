/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.validators;

import dto.employees.AcademicDataDTO;
import dto.employees.EmployeeDTO;
import dto.employees.NextOfKinDTO;
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
public class EmployeeValidator implements Serializable {

    String[] phone_prefix = AppConstants.phone_prefix;

    public EmployeeValidator() {
    }

    public boolean validateNewEmployee(FacesContext context, EmployeeDTO dto) {

        boolean result = true;

        //validate transfer details
        if (StringUtils.equals(dto.getModeOfEntry(), AppConstants.TRANSFER_ENTRY)) {
            //date of first appointment
            boolean check_date_first_appointment = GenericValidator.isDate(dto.getDateOfFirstAppointment(), "dd/MM/yyyy", true);
            if (check_date_first_appointment == false) {
                result = false;
                ViewHelper.addErrorMessage(context, "txt_d_of_first_appt", "Invalid date of first appointment. Use dd/mm/yyyy", null);
            }

            //trasnfering organization
            boolean check_transfer_organization = StringUtils.isBlank(dto.getTransferedEntryOrganization());
            if (check_transfer_organization) {
                result = false;
                ViewHelper.addErrorMessage(context, "txt_transfer_organization", "Transfer Organization is required", null);
            }

        }

        //file no
        boolean check_file_no = StringUtils.isBlank(dto.getFileNo());
        if (check_file_no) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_file_no", "File number is required", null);
        }

        //surname and other names
        boolean check_surname = StringUtils.isBlank(dto.getSurname());
        if (check_surname) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_surname", "Surname is required", null);
        }

        boolean check_othername = StringUtils.isBlank(dto.getOtherNames());
        if (check_othername) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_othernames", "Other name is required", null);
        }

        //dob
        boolean check_dob = GenericValidator.isDate(dto.getDateOfBirth(), "dd/MM/yyyy", true);
        if (check_dob == false) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_dob", "Invalid date of birth. Use dd/mm/yyyy", null);
        }

        //gender
        boolean check_gender = StringUtils.isBlank(dto.getGender());
        if (check_gender) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_gender", "Gender is required", null);
        }

        //marital status
        boolean check_marital_status = StringUtils.isBlank(dto.getMaritalStatus());
        if (check_marital_status) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_marital_status", "Marital status is required", null);
        }

        //state of origin
        boolean check_state_of_origin = StringUtils.isBlank(dto.getStateOfOrigin());
        if (check_state_of_origin) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_state_of_origin", "State of origin is required", null);
        }

        //lga
        boolean check_lga = StringUtils.isBlank(dto.getLga());
        if (check_lga) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_lga", "LGA is required", null);
        }

        //grade level
        boolean check_gradelevel = StringUtils.isBlank(dto.getPresentGradeLevel());
        if (check_gradelevel) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_gradelevel", "Grade level is required", null);
        }

        //grade step
        boolean check_gradestep = StringUtils.isBlank(dto.getPresentGradeStep());
        if (check_gradestep) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_gradestep", "Grade step is required", null);
        }

        //date of appointment
        boolean check_date_appointment = GenericValidator.isDate(dto.getDateOfPresentAppointment(), "dd/MM/yyyy", true);
        if (check_date_appointment == false) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_d_of_appointment", "Invalid date of appointment. Use dd/mm/yyyy", null);
        }

        //cadre
        boolean check_cadre = StringUtils.isBlank(dto.getPresentCadre());
        if (check_cadre) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_cadre", "Cadre is required", null);
        }

        //rank
        boolean check_rank = StringUtils.isBlank(dto.getPresentRank());
        if (check_rank) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_rank", "Rank is required", null);
        }
        
        
        
        //present emulument
        /*boolean check_present_annual_emulument = StringUtils.isBlank(dto.getPresentAnnualEmulument());
        if (check_present_annual_emulument) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_annual_emulument", "Annual emulument is required", null);
        }*/

        //location
        boolean check_location = StringUtils.isBlank(dto.getPresentLocation());
        if (check_location) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_location", "Location is required", null);
        }

        //department
        boolean check_department = StringUtils.isBlank(dto.getPresentDepartment());
        if (check_department) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_department", "Department is required", null);
        }

        //primary phone
        String prim_phone = dto.getPrimaryPhone();
        boolean check_phone = StringUtils.isBlank(prim_phone) || !(StringUtils.startsWithAny(prim_phone, phone_prefix))
                || !(StringUtils.length(prim_phone) == 11);
        if (check_phone) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_prim_phone", "Phone number must be 11 digit Nigerian GSM number", null);
        }

        //sec phone
        String sec_phone = dto.getSecondaryPhone();
        if (!StringUtils.isBlank(sec_phone)) {
            check_phone = StringUtils.isBlank(sec_phone) || !(StringUtils.startsWithAny(sec_phone, phone_prefix))
                    || !(StringUtils.length(sec_phone) == 11);
            if (check_phone) {
                result = false;
                ViewHelper.addErrorMessage(context, "txt_sec_phone", "Phone number must be 11 digit Nigerian GSM number", null);
            }
        }

        //primary email
        String prim_email = dto.getPrimaryEmail();
        boolean check_email = GenericValidator.isEmail(prim_email);
        if (check_email == false) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_prim_email", "Invalid email address.", null);
        }

        //sec email
        String sec_email = dto.getSecondaryEmail();
        if (!StringUtils.isBlank(sec_email)) {
            check_email = GenericValidator.isEmail(sec_email);
            if (check_email == false) {
                result = false;
                ViewHelper.addErrorMessage(context, "txt_sec_email", "Invalid email address.", null);
            }
        }

        //present address
        boolean check_pres_address = StringUtils.isBlank(dto.getPresentAddress());
        if (check_pres_address) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_pres_address", "Present address is required", null);
        }

        //perm address
        boolean check_perm_address = StringUtils.isBlank(dto.getPermanentHomeAddress());
        if (check_perm_address) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_perm_address", "Permanent address is required", null);
        }

        //check academic data
        if (dto.getAcademicDataList().isEmpty()) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_acad_data", "Academic data is required", null);
        } else {
            // not completed yet
//            boolean has_tertiary = false;
//            for(AcademicDataDTO a : dto.getAcademicDataList()){
//                if(a.getInstType().equals(AppConstants.ter)){
//                    has_tertiary = true;
//                }
//            }
        }

        //check next of kin
        boolean check_next_of_kin = dto.getNextOfKinList().isEmpty() || dto.getNextOfKinList().size() != 2;
        if (check_next_of_kin) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_next_of_kin", "Two next of kin is required", null);
        }

        if (!result) {
            ViewHelper.addErrorMessage(context, "global_msg", "Validation errors occured. Review form and try again.", null);
        }

        return result;

    }

    public boolean validateAcademicData(FacesContext context, AcademicDataDTO dto) {
        boolean result = true;

        //inst type
        boolean check_insttype = StringUtils.isBlank(dto.getInstType());
        if (check_insttype) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Institution type is required", null);
        }

        //inst name
        boolean check_instname = StringUtils.isBlank(dto.getInstName());
        if (check_instname) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Institution name is required", null);
        }

        //course
        if (StringUtils.equals(dto.getInstType(), "3")) {
            boolean check_course = StringUtils.isBlank(dto.getCourse());
            if (check_course) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Course is required", null);
            }
        }

        //country
        boolean check_country = StringUtils.isBlank(dto.getCountry());
        if (check_country) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Country of study is required", null);
        }

        //start date
        boolean check_startdate = GenericValidator.isDate(dto.getStartDate(), "dd/MM/yyyy", true);
        if (check_startdate == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid start date. Use dd/mm/yyyy", null);
        }

        //end date
        boolean check_enddate = GenericValidator.isDate(dto.getEndDate(), "dd/MM/yyyy", true);
        if (check_enddate == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid end date. Use dd/mm/yyyy", null);
        }

        //qualification
        boolean check_qualifications = StringUtils.isBlank(dto.getQualification());
        if (check_qualifications) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Qualification is required", null);
        }

        return result;
    }

    public boolean validateNextOfKin(FacesContext context, NextOfKinDTO dto) {
        boolean result = true;

        //surname
        boolean check_surname = StringUtils.isBlank(dto.getSurname());
        if (check_surname) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Surname of next of kin is required", null);
        }

        //othername
        boolean check_other_names = StringUtils.isBlank(dto.getOtherNames());
        if (check_other_names) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Other name of next of kin is required", null);
        }

        //relationship
        boolean check_relationship = StringUtils.isBlank(dto.getRelationship());
        if (check_relationship) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Relationship with next of kin is required", null);
        }

        //phone
        String phone = dto.getPhone();
        boolean check_phone = StringUtils.isBlank(phone) || !(StringUtils.startsWithAny(phone, phone_prefix))
                || !(StringUtils.length(phone) == 11);
        if (check_phone) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Phone number must be 11 digit Nigerian GSM number", null);
        }

        //email
        String email = dto.getEmail();
        if (!StringUtils.isBlank(email)) {
            boolean check_email = GenericValidator.isEmail(email);
            if (check_email == false) {
                result = false;
                ViewHelper.addErrorMessage(context, null, "Invalid email address.", null);
            }
        }

        //address
        boolean check_address = StringUtils.isBlank(dto.getAddress());
        if (check_address) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Address of next of kin is required", null);
        }

        return result;
    }

    public boolean validateUpdateBiodata(FacesContext context, EmployeeDTO dto) {
        boolean result = true;

        //surname and other names
        boolean check_surname = StringUtils.isBlank(dto.getSurname());
        if (check_surname) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_surname", "Surname is required", null);
        }

        boolean check_othername = StringUtils.isBlank(dto.getOtherNames());
        if (check_othername) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_othernames", "Other name is required", null);
        }

        //dob
        boolean check_dob = GenericValidator.isDate(dto.getDateOfBirth(), "dd/MM/yyyy", true);
        if (check_dob == false) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_dob", "Invalid date of birth. Use dd/mm/yyyy", null);
        }

        //gender
        boolean check_gender = StringUtils.isBlank(dto.getGender());
        if (check_gender) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_gender", "Gender is required", null);
        }

        //marital status
        boolean check_marital_status = StringUtils.isBlank(dto.getMaritalStatus());
        if (check_marital_status) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_marital_status", "Marital status is required", null);
        }

        //state of origin
        boolean check_state_of_origin = StringUtils.isBlank(dto.getStateOfOrigin());
        if (check_state_of_origin) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_state_of_origin", "State of origin is required", null);
        }

        //lga
        boolean check_lga = StringUtils.isBlank(dto.getLga());
        if (check_lga) {
            result = false;
            ViewHelper.addErrorMessage(context, "dd_lga", "LGA is required", null);
        }

        return result;
    }

    public boolean validateEmploymentData(FacesContext context, EmployeeDTO dto) {
        boolean result = true;

        //date first of appointment
        boolean check_date_first_appointment = GenericValidator.isDate(dto.getDateOfFirstAppointment(), "dd/MM/yyyy", true);
        if (check_date_first_appointment == false) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_d_of_first_appointment", "Invalid date of first appointment. Use dd/mm/yyyy", null);
        }

        //confirmation date
        boolean check_confirmation_date = GenericValidator.isDate(dto.getConfirmationDate(), "dd/MM/yyyy", true);
        if (check_confirmation_date == false) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_confirmation_date", "Invalid confirmation date. Use dd/mm/yyyy", null);
        }

        return result;
    }

    public boolean validateContactInfo(FacesContext context, EmployeeDTO dto) {
        boolean result = true;

        //primary phone
        String prim_phone = dto.getPrimaryPhone();
        boolean check_phone = StringUtils.isBlank(prim_phone) || !(StringUtils.startsWithAny(prim_phone, phone_prefix))
                || !(StringUtils.length(prim_phone) == 11);
        if (check_phone) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_prim_phone", "Phone number must be 11 digit Nigerian GSM number", null);
        }

        //sec phone
        String sec_phone = dto.getSecondaryPhone();
        if (!StringUtils.isBlank(sec_phone)) {
            check_phone = StringUtils.isBlank(sec_phone) || !(StringUtils.startsWithAny(sec_phone, phone_prefix))
                    || !(StringUtils.length(sec_phone) == 11);
            if (check_phone) {
                result = false;
                ViewHelper.addErrorMessage(context, "txt_sec_phone", "Phone number must be 11 digit Nigerian GSM number", null);
            }
        }

        //primary email
        String prim_email = dto.getPrimaryEmail();
        boolean check_email = GenericValidator.isEmail(prim_email);
        if (check_email == false) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_prim_email", "Invalid email address.", null);
        }

        //sec email
        String sec_email = dto.getSecondaryEmail();
        if (!StringUtils.isBlank(sec_email)) {
            check_email = GenericValidator.isEmail(sec_email);
            if (check_email == false) {
                result = false;
                ViewHelper.addErrorMessage(context, "txt_sec_email", "Invalid email address.", null);
            }
        }

        //present address
        boolean check_pres_address = StringUtils.isBlank(dto.getPresentAddress());
        if (check_pres_address) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_pres_address", "Present address is required", null);
        }

        //perm address
        boolean check_perm_address = StringUtils.isBlank(dto.getPermanentHomeAddress());
        if (check_perm_address) {
            result = false;
            ViewHelper.addErrorMessage(context, "txt_perm_address", "Permanent address is required", null);
        }

        return result;
    }

}
