/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.posting;

import dto.employees.EmployeeDTO;
import dto.employees.EmployeeLocationDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gainsolutions
 */
public class PostingDTO implements Serializable {

    private String recordId;
    private String employeeRecordId;
    private String fileNo;
    private String surname;
    private String otherNames;
    private String gender;
    private String gradeLevel;
    private String maritalStatus, maritalStatusDesc;
    private String presentLocation, presentLocationDesc;
    private String dateOfPostingToPresentLocation, dateOfPostingToPresentLocationDesc;
    private String stateOfOrigin, stateOfOriginDesc;
    private String presentCadre, presentCadreDesc;
    private String isStateCoord, isStateAcct, isSecondment;

    private String lengthOfStay;
    private String lengthOfSpecialAppoint;
    private String photoUrl;
    private String created, createdBy;
    private String lastMod, lastModBy;

    private List<EmployeeLocationDTO> locationList;
    private List<EmployeeDTO> employeeList;

    public PostingDTO() {
        locationList = new ArrayList<>();
        employeeList = new ArrayList<>();
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getEmployeeRecordId() {
        return employeeRecordId;
    }

    public void setEmployeeRecordId(String employeeRecordId) {
        this.employeeRecordId = employeeRecordId;
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

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
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

    public String getPresentLocation() {
        return presentLocation;
    }

    public void setPresentLocation(String presentLocation) {
        this.presentLocation = presentLocation;
    }

    public String getPresentLocationDesc() {
        return presentLocationDesc;
    }

    public void setPresentLocationDesc(String presentLocationDesc) {
        this.presentLocationDesc = presentLocationDesc;
    }

    public String getDateOfPostingToPresentLocation() {
        return dateOfPostingToPresentLocation;
    }

    public void setDateOfPostingToPresentLocation(String dateOfPostingToPresentLocation) {
        this.dateOfPostingToPresentLocation = dateOfPostingToPresentLocation;
    }

    public String getDateOfPostingToPresentLocationDesc() {
        return dateOfPostingToPresentLocationDesc;
    }

    public void setDateOfPostingToPresentLocationDesc(String dateOfPostingToPresentLocationDesc) {
        this.dateOfPostingToPresentLocationDesc = dateOfPostingToPresentLocationDesc;
    }

    public String getLengthOfStay() {
        return lengthOfStay;
    }

    public void setLengthOfStay(String lengthOfStay) {
        this.lengthOfStay = lengthOfStay;
    }

    public List<EmployeeLocationDTO> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<EmployeeLocationDTO> locationList) {
        this.locationList = locationList;
    }

    public List<EmployeeDTO> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<EmployeeDTO> employeeList) {
        this.employeeList = employeeList;
    }

    public void addLocation(EmployeeLocationDTO entry) {
        locationList.add(entry);
    }

    public void addEmployee(EmployeeDTO entry) {
        employeeList.add(entry);
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMaritalStatusDesc() {
        return maritalStatusDesc;
    }

    public void setMaritalStatusDesc(String maritalStatusDesc) {
        this.maritalStatusDesc = maritalStatusDesc;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getStateOfOriginDesc() {
        return stateOfOriginDesc;
    }

    public void setStateOfOriginDesc(String stateOfOriginDesc) {
        this.stateOfOriginDesc = stateOfOriginDesc;
    }

    public String getPresentCadre() {
        return presentCadre;
    }

    public void setPresentCadre(String presentCadre) {
        this.presentCadre = presentCadre;
    }

    public String getPresentCadreDesc() {
        return presentCadreDesc;
    }

    public void setPresentCadreDesc(String presentCadreDesc) {
        this.presentCadreDesc = presentCadreDesc;
    }

    public String getIsStateCoord() {
        return isStateCoord;
    }

    public void setIsStateCoord(String isStateCoord) {
        this.isStateCoord = isStateCoord;
    }

    public String getIsStateAcct() {
        return isStateAcct;
    }

    public void setIsStateAcct(String isStateAcct) {
        this.isStateAcct = isStateAcct;
    }

    public String getIsSecondment() {
        return isSecondment;
    }

    public void setIsSecondment(String isSecondment) {
        this.isSecondment = isSecondment;
    }

    public String getLengthOfSpecialAppoint() {
        return lengthOfSpecialAppoint;
    }

    public void setLengthOfSpecialAppoint(String lengthOfSpecialAppoint) {
        this.lengthOfSpecialAppoint = lengthOfSpecialAppoint;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }
    
    

}
