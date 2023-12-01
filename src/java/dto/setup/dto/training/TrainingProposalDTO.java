/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.setup.dto.training;

import dto.setup.TrainingSpecializationDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author IronHide
 */
public class TrainingProposalDTO implements Serializable {

    private String recordId;
    private String title;
    private String trainingType,trainingTypeName;
    private String country,countryName,location;
    private String state,stateName;
    private Date startDate, endDate;
    private String expectedStartDate;
    private String expectedEndDate;
    private String objective;
    private String consultant,consultantName;
    private String expectedCertificate,expectedCertificateName;
    private String approvalStatus;
    private String approvalStatusReason;
    private String remarks;
    private String recordStatus;
    private String created;
    private String createdBy;
    private String lastMod;
    private String lastModBy;
    
    private List<TrainingSpecializationDTO> specializations;
    private List<TrainingParticipantDTO> participants;

    public TrainingProposalDTO() {
        specializations = new ArrayList<>();
        participants = new ArrayList<>();
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExpectedStartDate() {
        return expectedStartDate;
    }

    public void setExpectedStartDate(String expectedStartDate) {
        this.expectedStartDate = expectedStartDate;
    }

    public String getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(String expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getConsultant() {
        return consultant;
    }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }

    public String getExpectedCertificate() {
        return expectedCertificate;
    }

    public void setExpectedCertificate(String expectedCertificate) {
        this.expectedCertificate = expectedCertificate;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalStatusReason() {
        return approvalStatusReason;
    }

    public void setApprovalStatusReason(String approvalStatusReason) {
        this.approvalStatusReason = approvalStatusReason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
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

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public void setTrainingTypeName(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getExpectedCertificateName() {
        return expectedCertificateName;
    }

    public void setExpectedCertificateName(String expectedCertificateName) {
        this.expectedCertificateName = expectedCertificateName;
    }

    public List<TrainingSpecializationDTO> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<TrainingSpecializationDTO> specializations) {
        this.specializations = specializations;
    }

    public List<TrainingParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<TrainingParticipantDTO> participants) {
        this.participants = participants;
    }
    
    public void addParticipant(TrainingParticipantDTO entry){
        participants.add(entry);
    }
    
    public void addSpecialization(TrainingSpecializationDTO entry){
        specializations.add(entry);
    }

}
