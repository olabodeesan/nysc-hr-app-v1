/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import service.utils.ApplicationCodesService;

/**
 *
 * @author IronHide
 */
public class ApplicationCodesLoader {

    private final ApplicationCodesService service;

    public ApplicationCodesLoader() {
        service = new ApplicationCodesService();
    }

    public Map<String, String> getGender() {
        Map<String, String> codes = null;
        if (getAppScopeObject("app_scope_gender") == null) {

            try {
                codes = new LinkedHashMap<>();
                codes.put("Male", "M");
                codes.put("Female", "F");
            } catch (Exception exc) {
            }
            putAppScopeObject("app_scope_gender", codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject("app_scope_gender");
        }
        return codes;
    }

    public Map<String, String> getOpenStatus() {
        Map<String, String> codes = null;
        if (getAppScopeObject("app_scope_openstatuscodes") == null) {

            try {
                codes = new LinkedHashMap<>();
                codes.put(AppConstants.OPEN, AppConstants.OPEN);
                codes.put(AppConstants.CLOSED, AppConstants.CLOSED);
            } catch (Exception exc) {
            }
            putAppScopeObject("app_scope_openstatuscodes", codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject("app_scope_openstatuscodes");
        }
        return codes;
    }

    public Map<String, String> getPrimarySystemRoles() {
        Map<String, String> codes = null;
        if (getAppScopeObject("app_scope_primary_roles") == null) {
            try {
                codes = service.getPrimarySystemRoles();
            } catch (Exception exc) {
            }
            putAppScopeObject("app_scope_primary_roles", codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject("app_scope_primary_roles");
        }
        return codes;
    }

    public Map<String, String> getStaffAccounts() {
        Map<String, String> codes = null;
        if (getAppScopeObject("app_scope_staff_accts") == null) {
            try {
                codes = service.getStaffAccounts();
            } catch (Exception exc) {
            }
            putAppScopeObject("app_scope_staff_accts", codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject("app_scope_staff_accts");
        }
        return codes;
    }

    public Map<String, String> getRecordStatusCodes() {
        Map<String, String> codes = null;
        if (getAppScopeObject("app_scope_statuscodes") == null) {

            try {
                codes = new LinkedHashMap<>();
                codes.put("ACTIVE", "ACTIVE");
                codes.put("INACTIVE", "INACTIVE");
            } catch (Exception exc) {
            }
            putAppScopeObject("app_scope_statuscodes", codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject("app_scope_statuscodes");
        }
        return codes;
    }

    public Map<String, String> getSystemConfigs() {
        Map<String, String> codes = null;
        if (getAppScopeObject("app_scope_system_configs") == null) {
            try {
                codes = service.getSystemConfigs();
            } catch (Exception exc) {
            }
            putAppScopeObject("app_scope_system_configs", codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject("app_scope_system_configs");
        }
        return codes;
    }

    public Map<String, String> getDepartments() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_DEPARTMENTS) == null) {
            try {
                codes = service.getDepartments();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_DEPARTMENTS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_DEPARTMENTS);
        }
        return codes;
    }

    public Map<String, String> getCadre() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_CADRE) == null) {
            try {
                codes = service.getCadre();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_CADRE, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_CADRE);
        }
        return codes;
    }

    public Map<String, String> getContinents() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_CONTINENTS) == null) {
            try {
                codes = service.getContinents();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_CONTINENTS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_CONTINENTS);
        }
        return codes;
    }

    public Map<String, String> getCountries() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_COUNTRIES) == null) {
            try {
                codes = service.getCountries();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_COUNTRIES, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_COUNTRIES);
        }
        return codes;
    }

    public Map<String, String> getPositionAppointments() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_POSITION_APPOINTMENTS) == null) {
            try {
                codes = service.getPositionAppointments();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_POSITION_APPOINTMENTS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_POSITION_APPOINTMENTS);
        }
        return codes;
    }

    public Map<String, String> getQualifications() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_QUALIFICATIONS) == null) {
            try {
                codes = service.getQualifications();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_QUALIFICATIONS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_QUALIFICATIONS);
        }
        return codes;
    }

    public Map<String, String> getMaritalStatus() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_MARITAL_STATUS) == null) {
            try {
                codes = service.getMaritalStatus();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_MARITAL_STATUS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_MARITAL_STATUS);
        }
        return codes;
    }

    public Map<String, String> getStates() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_STATES) == null) {
            try {
                codes = service.getStates();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_STATES, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_STATES);
        }
        return codes;
    }

    public Map<String, String> getGradeLevel() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_GRADE_LEVEL) == null) {
            try {
                codes = service.getGradeLevel();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_GRADE_LEVEL, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_GRADE_LEVEL);
        }
        return codes;
    }

    public Map<String, String> getActivePersonnelStatus() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_ACTIVE_STATUS) == null) {
            try {
                codes = service.getActivePersonnelStatus();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_ACTIVE_STATUS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_ACTIVE_STATUS);
        }
        return codes;
    }

    public Map<String, String> getSuspendedStatus() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_SUSPENDED_STATUS) == null) {
            try {
                codes = service.getSuspendedStatus();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_SUSPENDED_STATUS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_SUSPENDED_STATUS);
        }
        return codes;
    }

    public Map<String, String> getDisengagementStatus() {

        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_DISENGAGED_STATUS) == null) {
            try {
                codes = service.getDisengagementStatus();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_DISENGAGED_STATUS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_DISENGAGED_STATUS);
        }
        return codes;
    }

//    public Map<String, String> getGradeStep() {
//        Map<String, String> codes = null;
//        if (getAppScopeObject(AppConstants.APP_SCOPE_GRADE_STEP) == null) {
//            try {
//                codes = service.getGradeStep();
//            } catch (Exception exc) {
//            }
//            putAppScopeObject(AppConstants.APP_SCOPE_GRADE_STEP, codes);
//        } else {
//            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_GRADE_STEP);
//        }
//        return codes;
//    }
    public Map<String, String> getLocations() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_LOCATIONS) == null) {
            try {
                codes = service.getLocations();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_LOCATIONS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_LOCATIONS);
        }
        return codes;
    }

    public Map<String, String> getInstitutionTypes() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_INSTITUTION_TYPES) == null) {
            try {
                codes = service.getInstitutionTypes();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_INSTITUTION_TYPES, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_INSTITUTION_TYPES);
        }
        return codes;
    }

    public Map<String, String> getCourses() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_COURSES) == null) {
            try {
                codes = service.getCourses();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_COURSES, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_COURSES);
        }
        return codes;
    }

    public Map<String, String> getRelationships() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_RELATIONSHIPS) == null) {
            try {
                codes = service.getRelationships();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_RELATIONSHIPS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_RELATIONSHIPS);
        }
        return codes;
    }

    public Map<String, String> getTrainingCerts() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_TRAINING_CERTIFICATES) == null) {
            try {
                codes = service.getTrainingCerts();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_TRAINING_CERTIFICATES, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_TRAINING_CERTIFICATES);
        }
        return codes;
    }

    public Map<String, String> getTrainingApprovalCodes() {

        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_TRAINING_PROPOSAL_APPROVAL) == null) {
            try {
                codes = service.getTrainingApprovalCodes(true);
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_TRAINING_PROPOSAL_APPROVAL, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_TRAINING_PROPOSAL_APPROVAL);
        }
        return codes;
    }

    public Map<String, String> getTrainingApprovalCodesComplete() {

        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_TRAINING_PROPOSAL_APPROVAL_COMPLETE) == null) {
            try {
                codes = service.getTrainingApprovalCodes(false);
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_TRAINING_PROPOSAL_APPROVAL_COMPLETE, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_TRAINING_PROPOSAL_APPROVAL_COMPLETE);
        }
        return codes;
    }

    public Map<String, String> getTrainingSpecializations() {

        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_TRAINING_SPECIALIZATIONS) == null) {
            try {
                codes = service.getTrainingSpecializations();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_TRAINING_SPECIALIZATIONS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_TRAINING_SPECIALIZATIONS);
        }
        return codes;
    }

    public Map<String, String> getTrainingTypes() {

        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_TRAINING_TYPES) == null) {
            try {
                codes = service.getTrainingTypes();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_TRAINING_TYPES, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_TRAINING_TYPES);
        }
        return codes;
    }

    public Map<String, String> getTrainingConsultant() {

        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_TRAINING_CONSULTANTS) == null) {
            try {
                codes = service.getTrainingConsultant();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_TRAINING_CONSULTANTS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_TRAINING_CONSULTANTS);
        }
        return codes;
    }

    public Map<String, String> getLeaveApprovalCodes() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_LEAVE_APPROVAL) == null) {
            try {
                codes = service.getLeaveApprovalCodes();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_LEAVE_APPROVAL, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_LEAVE_APPROVAL);
        }
        return codes;
    }

    public Map<String, String> getGradeLevelAnnualLeaveDurations() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_GL_ANNUAL_LEAVE_DURATIONS) == null) {
            try {
                codes = service.getGradeLevelAnnualLeaveDurations();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_GL_ANNUAL_LEAVE_DURATIONS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_GL_ANNUAL_LEAVE_DURATIONS);
        }
        return codes;
    }

    //From ESAN
    public Map<String, String> getPostingReason() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_POSTING_TYPE) == null) {

            try {
                codes = new LinkedHashMap<>();
                codes = service.getPostingReason();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_POSTING_TYPE, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_POSTING_TYPE);
        }
        return codes;
    }

    public Map<String, String> getSpecialAppointment() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_SPECIAL_APPOINTMENT) == null) {

            try {
                codes = new LinkedHashMap<String, String>();
                codes.put("NO", "N");
                codes.put("YES", "Y");
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_SPECIAL_APPOINTMENT, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_SPECIAL_APPOINTMENT);
        }
        return codes;
    }

    public Map<String, String> getDgApprovalStatus() {
        Map<String, String> codes = null;
        if (getAppScopeObject("app_scope_dg_approval_status") == null) {

            try {
                codes = new LinkedHashMap<String, String>();
                codes.put("PENDING", "PENDING");
                codes.put("APPROVED", "APPROVED");
            } catch (Exception exc) {
            }
            putAppScopeObject("app_scope_dg_approval_status", codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject("app_scope_dg_approval_status");
        }
        return codes;
    }

    public Map<String, String> getClaimStatus() {
        Map<String, String> codes = null;
        if (getAppScopeObject("app_scope_claim_status") == null) {

            try {
                codes = new LinkedHashMap<String, String>();
                codes.put("PAID", "PAID");
                codes.put("UNPAID", "UNPAID");
            } catch (Exception exc) {
            }
            putAppScopeObject("app_scope_claim_status", codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject("app_scope_claim_status");
        }
        return codes;
    }

    public Map<String, String> getPensionFundAdministrator() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_PENSION_FUND_ADMINISTRATOR) == null) {

            try {
                codes = new LinkedHashMap<>();
                codes = service.getPensionFundAdminstrator();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_PENSION_FUND_ADMINISTRATOR, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_PENSION_FUND_ADMINISTRATOR);
        }
        return codes;
    }

    
      public Map<String, String> getBankers() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_BANKERS) == null) {

            try {
                codes = new LinkedHashMap<>();
                codes = service.getBanker();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_BANKERS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_BANKERS);
        }
        return codes;
    }

      
    public void putAppScopeObject(String key, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getApplicationMap().put(key, value);
    }

    public Object getAppScopeObject(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getApplicationMap().get(key);
    }
}
