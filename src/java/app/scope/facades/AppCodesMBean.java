/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.scope.facades;

import java.util.Map;
import view.utils.ApplicationCodesLoader;

/**
 *
 * @author IronHide
 */
public class AppCodesMBean {

    ApplicationCodesLoader loader = new ApplicationCodesLoader();

    public AppCodesMBean() {
    }

    public Map<String, String> getGender() {
        return loader.getGender();
    }

    public Map<String, String> getRecordStatusCodes() {
        return loader.getRecordStatusCodes();
    }

    public Map<String, String> getDepartments() {
        return loader.getDepartments();
    }

    public Map<String, String> getPrimarySystemRoles() {
        return loader.getPrimarySystemRoles();
    }

    public Map<String, String> getCadre() {
        return loader.getCadre();
    }

    public Map<String, String> getContinents() {
        return loader.getContinents();
    }

    public Map<String, String> getCountries() {
        return loader.getCountries();
    }

    public Map<String, String> getPositionAppointments() {
        return loader.getPositionAppointments();
    }

    public Map<String, String> getQualifications() {
        return loader.getQualifications();
    }

    public Map<String, String> getMaritalStatus() {
        return loader.getMaritalStatus();
    }

    public Map<String, String> getStates() {
        return loader.getStates();
    }

    public Map<String, String> getGradeLevel() {
        return loader.getGradeLevel();
    }

//    public Map<String, String> getGradeStep() {
//        return loader.getGradeStep();
//    }
    public Map<String, String> getLocations() {
        return loader.getLocations();
    }

    public Map<String, String> getInstitutionTypes() {
        return loader.getInstitutionTypes();
    }

    public Map<String, String> getCourses() {
        return loader.getCourses();
    }

    public Map<String, String> getRelationships() {
        return loader.getRelationships();
    }

    public Map<String, String> getActivePersonnelStatus() {
        return loader.getActivePersonnelStatus();
    }

    public Map<String, String> getSuspendedStatus() {
        return loader.getSuspendedStatus();
    }

    public Map<String, String> getDisengagementStatus() {
        return loader.getDisengagementStatus();
    }

    public Map<String, String> getLeaveApprovalCodes() {
        return loader.getLeaveApprovalCodes();
    }

    public Map<String, String> getTrainingCerts() {
        return loader.getTrainingCerts();
    }

    public Map<String, String> getTrainingApprovalCodes() {
        return loader.getTrainingApprovalCodes();
    }

    public Map<String, String> getTrainingApprovalCodesComplete() {
        return loader.getTrainingApprovalCodesComplete();
    }

    public Map<String, String> getTrainingSpecializations() {
        return loader.getTrainingSpecializations();
    }

    public Map<String, String> getTrainingTypes() {
        return loader.getTrainingTypes();
    }

    public Map<String, String> getTrainingConsultant() {
        return loader.getTrainingConsultant();
    }

    //From Esan
    public Map<String, String> getPostingReason() {
        return loader.getPostingReason();
    }

    public Map<String, String> getSpecialAppointment() {
        return loader.getSpecialAppointment();
    }

    public Map<String, String> getClaimStatus() {
        return loader.getClaimStatus();
    }

    public Map<String, String> getDgApprovalStatus() {
        return loader.getDgApprovalStatus();
    }

    public Map<String, String> getPensionFundAdministrator() {
        return loader.getPensionFundAdministrator();
    }

    public Map<String, String> getBankers() {
        return loader.getBankers();
    }

   
}
