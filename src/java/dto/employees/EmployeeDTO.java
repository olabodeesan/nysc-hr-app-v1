/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.employees;

import dto.appointment.recruit.EmployeeStatusDTO;
import dto.discipline.DisciplineDTO;
import dto.promotion.ConversionDTO;
import dto.setup.dto.registry.AnnualLeaveRosterDTO;
import dto.setup.dto.registry.RegistryDocumentDTO;
import dto.setup.dto.registry.StaffLeaveApplicationDTO;
import dto.setup.dto.training.StaffFurtherStudyApplicationDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import view.utils.AppConstants;

/**
 *
 * @author IronHide
 */
public class EmployeeDTO implements Serializable {

    private String recordId;
    private String fileNo;
    private String surname;
    private String otherNames;
    private String dateOfBirth, dateOfBirthDesc;
    private String gender;
    private String maritalStatus, maritalStatusDesc;
    private String stateOfOrigin, stateOfOriginDesc;
    private String lga, lgaDesc;

    private String dateOfFirstAppointment, dateOfFirstAppointmentDesc;
    private String confirmationDate, confirmationDateDesc;

    private String presentGradeLevel;
    private String presentGradeStep;
    private String dateOfPresentAppointment, dateOfPresentAppointmentDesc;

    private String presentCadre, presentCadreDesc;
    private String presentRank, presentRankDesc;
    private String dateOfPresentCadre, dateOfPresentCadreDesc;

    private String presentLocation, presentLocationDesc;
    private String presentDepartment, presentDepartmentDesc;
    private String dateOfPostingToPresentLocation, dateOfPostingToPresentLocationDesc;

    private String primaryPhone;
    private String primaryEmail;
    private String secondaryPhone;
    private String secondaryEmail;

    private String presentAddress;
    private String permanentHomeAddress;

    private String isStateCoord, isStateAcct, isSecondment;

    private String dateToRetireBasedOnLengthOfStay, dateToRetireBasedOnLengthOfStayDesc;
    private String dateToRetireBasedOnAge, dateToRetireBasedOnAgeDesc;
    private String dateDueForRetirement, dateDueForRetirementDesc;
    private String yearDueForRetirement;

    private List<AcademicDataDTO> academicDataList;
    private List<NextOfKinDTO> nextOfKinList;

    private List<EmployeeCadreDTO> cadreList;
    private List<EmployeeAppointmentsDTO> appointmentsList;
    private List<EmployeeLocationDTO> locationList;

    private List<EmployeeStatusDTO> statusList;
    private List<DisciplineDTO> disciplineList;

    private List<AnnualLeaveRosterDTO> annualLeaveList;
    private List<StaffLeaveApplicationDTO> leaveApplicationList;

    private List<StaffFurtherStudyApplicationDTO> trainingApplicationList;

    private List<RegistryDocumentDTO> documents;

    private String created;
    private String createdBy;
    private String lastMod;
    private String lastModBy;

    private String presentStatus, presentStatusDesc;
    private String presentStatusReason, presentStatusReasonDesc;
    private String presentStatusRemarks;
    private String dateOfPresentStatus, dateOfPresentStatusDesc;

    private String modeOfEntry, modeOfEntryDesc;
    private String transferedEntryOrganization;
    private String presentlyDisciplined, presentlySuspended;

    private String presentAnnualEmulument;

    private String photoUrl;

    //remove later
    private String _suspensionStatus;
    private String _suspensionReason;
    private String _suspensionDate;

    private String _disciplineCase;
    private String _disciplineMgtDecision;
    private String _disciplineDate;

    //from esan
    private String lengthOfStay, lengthOfSpecialAppoint;

    private List<ConversionDTO> conversionList;

    // addtional fields
    private String pfaId, pfaDescription;
    private String pfaNumber;
    private String nhfNumber;
    private String nhisNumber;
    private String tinNumber;
    private String ippisNumber;
    private String salaryBankId, salaryBankDescription;
    private String salaryBankAccountNo;
    private String operationBankId, operationBankDescription;
    private String operationBankAccountNo;

    public EmployeeDTO() {
        academicDataList = new ArrayList<>();
        nextOfKinList = new ArrayList<>();
        cadreList = new ArrayList<>();
        appointmentsList = new ArrayList<>();
        locationList = new ArrayList<>();
        statusList = new ArrayList<>();

        disciplineList = new ArrayList<>();

        annualLeaveList = new ArrayList<>();
        leaveApplicationList = new ArrayList<>();

        trainingApplicationList = new ArrayList<>();

        documents = new ArrayList<>();

        conversionList = new ArrayList<>();
    }

    public String getSuspensionStatus() {
        return _suspensionStatus;
    }

    public void setSuspensionStatus(String _suspensionStatus) {
        this._suspensionStatus = _suspensionStatus;
    }

    public String getSuspensionReason() {
        return _suspensionReason;
    }

    public void setSuspensionReason(String _suspensionReason) {
        this._suspensionReason = _suspensionReason;
    }

    public String getSuspensionDate() {
        return _suspensionDate;
    }

    public void setSuspensionDate(String _suspensionDate) {
        this._suspensionDate = _suspensionDate;
    }

    public String getDisciplineCase() {
        return _disciplineCase;
    }

    public void setDisciplineCase(String _disciplineCase) {
        this._disciplineCase = _disciplineCase;
    }

    public String getDisciplineMgtDecision() {
        return _disciplineMgtDecision;
    }

    public void setDisciplineMgtDecision(String _disciplineMgtDecision) {
        this._disciplineMgtDecision = _disciplineMgtDecision;
    }

    public String getDisciplineDate() {
        return _disciplineDate;
    }

    public void setDisciplineDate(String _disciplineDate) {
        this._disciplineDate = _disciplineDate;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getPresentGradeLevel() {
        return presentGradeLevel;
    }

    public void setPresentGradeLevel(String presentGradeLevel) {
        this.presentGradeLevel = presentGradeLevel;
    }

    public String getPresentGradeStep() {
        return presentGradeStep;
    }

    public void setPresentGradeStep(String presentGradeStep) {
        this.presentGradeStep = presentGradeStep;
    }

    public String getDateOfPresentAppointment() {
        return dateOfPresentAppointment;
    }

    public void setDateOfPresentAppointment(String dateOfPresentAppointment) {
        this.dateOfPresentAppointment = dateOfPresentAppointment;
    }

    public String getPresentCadre() {
        return presentCadre;
    }

    public String getDateOfPresentCadre() {
        return dateOfPresentCadre;
    }

    public void setDateOfPresentCadre(String dateOfPresentCadre) {
        this.dateOfPresentCadre = dateOfPresentCadre;
    }

    public void setPresentCadre(String presentCadre) {
        this.presentCadre = presentCadre;
    }

    public String getPresentRank() {
        return presentRank;
    }

    public void setPresentRank(String presentRank) {
        this.presentRank = presentRank;
    }

    public String getPresentRankDesc() {
        return presentRankDesc;
    }

    public void setPresentRankDesc(String presentRankDesc) {
        this.presentRankDesc = presentRankDesc;
    }

    public String getPresentLocation() {
        return presentLocation;
    }

    public void setPresentLocation(String presentLocation) {
        this.presentLocation = presentLocation;
    }

    public String getPresentDepartment() {
        return presentDepartment;
    }

    public void setPresentDepartment(String presentDepartment) {
        this.presentDepartment = presentDepartment;
    }

    public String getDateOfPostingToPresentLocation() {
        return dateOfPostingToPresentLocation;
    }

    public void setDateOfPostingToPresentLocation(String dateOfPostingToPresentLocation) {
        this.dateOfPostingToPresentLocation = dateOfPostingToPresentLocation;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPermanentHomeAddress() {
        return permanentHomeAddress;
    }

    public void setPermanentHomeAddress(String permanentHomeAddress) {
        this.permanentHomeAddress = permanentHomeAddress;
    }

    public List<AcademicDataDTO> getAcademicDataList() {
        return academicDataList;
    }

    public void setAcademicDataList(List<AcademicDataDTO> academicDataList) {
        this.academicDataList = academicDataList;
    }

    public List<NextOfKinDTO> getNextOfKinList() {
        return nextOfKinList;
    }

    public void setNextOfKinList(List<NextOfKinDTO> nextOfKinList) {
        this.nextOfKinList = nextOfKinList;
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

    public String getDateOfFirstAppointment() {
        return dateOfFirstAppointment;
    }

    public void setDateOfFirstAppointment(String dateOfFirstAppointment) {
        this.dateOfFirstAppointment = dateOfFirstAppointment;
    }

    public String getDateToRetireBasedOnLengthOfStay() {
        return dateToRetireBasedOnLengthOfStay;
    }

    public void setDateToRetireBasedOnLengthOfStay(String dateToRetireBasedOnLengthOfStay) {
        this.dateToRetireBasedOnLengthOfStay = dateToRetireBasedOnLengthOfStay;
    }

    public String getDateToRetireBasedOnAge() {
        return dateToRetireBasedOnAge;
    }

    public void setDateToRetireBasedOnAge(String dateToRetireBasedOnAge) {
        this.dateToRetireBasedOnAge = dateToRetireBasedOnAge;
    }

    public String getDateDueForRetirement() {
        return dateDueForRetirement;
    }

    public void setDateDueForRetirement(String dateDueForRetirement) {
        this.dateDueForRetirement = dateDueForRetirement;
    }

    public String getMaritalStatusDesc() {
        return maritalStatusDesc;
    }

    public void setMaritalStatusDesc(String maritalStatusDesc) {
        this.maritalStatusDesc = maritalStatusDesc;
    }

    public String getStateOfOriginDesc() {
        return stateOfOriginDesc;
    }

    public void setStateOfOriginDesc(String stateOfOriginDesc) {
        this.stateOfOriginDesc = stateOfOriginDesc;
    }

    public String getLgaDesc() {
        return lgaDesc;
    }

    public void setLgaDesc(String lgaDesc) {
        this.lgaDesc = lgaDesc;
    }

    public String getDateOfFirstAppointmentDesc() {
        return dateOfFirstAppointmentDesc;
    }

    public void setDateOfFirstAppointmentDesc(String dateOfFirstAppointmentDesc) {
        this.dateOfFirstAppointmentDesc = dateOfFirstAppointmentDesc;
    }

    public String getDateOfPresentAppointmentDesc() {
        return dateOfPresentAppointmentDesc;
    }

    public void setDateOfPresentAppointmentDesc(String dateOfPresentAppointmentDesc) {
        this.dateOfPresentAppointmentDesc = dateOfPresentAppointmentDesc;
    }

    public String getPresentCadreDesc() {
        return presentCadreDesc;
    }

    public void setPresentCadreDesc(String presentCadreDesc) {
        this.presentCadreDesc = presentCadreDesc;
    }

    public String getDateOfPresentCadreDesc() {
        return dateOfPresentCadreDesc;
    }

    public void setDateOfPresentCadreDesc(String dateOfPresentCadreDesc) {
        this.dateOfPresentCadreDesc = dateOfPresentCadreDesc;
    }

    public String getPresentLocationDesc() {
        return presentLocationDesc;
    }

    public void setPresentLocationDesc(String presentLocationDesc) {
        this.presentLocationDesc = presentLocationDesc;
    }

    public String getPresentDepartmentDesc() {
        return presentDepartmentDesc;
    }

    public void setPresentDepartmentDesc(String presentDepartmentDesc) {
        this.presentDepartmentDesc = presentDepartmentDesc;
    }

    public String getDateOfPostingToPresentLocationDesc() {
        return dateOfPostingToPresentLocationDesc;
    }

    public void setDateOfPostingToPresentLocationDesc(String dateOfPostingToPresentLocationDesc) {
        this.dateOfPostingToPresentLocationDesc = dateOfPostingToPresentLocationDesc;
    }

    public String getDateToRetireBasedOnLengthOfStayDesc() {
        return dateToRetireBasedOnLengthOfStayDesc;
    }

    public void setDateToRetireBasedOnLengthOfStayDesc(String dateToRetireBasedOnLengthOfStayDesc) {
        this.dateToRetireBasedOnLengthOfStayDesc = dateToRetireBasedOnLengthOfStayDesc;
    }

    public String getDateToRetireBasedOnAgeDesc() {
        return dateToRetireBasedOnAgeDesc;
    }

    public void setDateToRetireBasedOnAgeDesc(String dateToRetireBasedOnAgeDesc) {
        this.dateToRetireBasedOnAgeDesc = dateToRetireBasedOnAgeDesc;
    }

    public String getDateDueForRetirementDesc() {
        return dateDueForRetirementDesc;
    }

    public void setDateDueForRetirementDesc(String dateDueForRetirementDesc) {
        this.dateDueForRetirementDesc = dateDueForRetirementDesc;
    }

    public String getYearDueForRetirement() {
        return yearDueForRetirement;
    }

    public void setYearDueForRetirement(String yearDueForRetirement) {
        this.yearDueForRetirement = yearDueForRetirement;
    }

    public String getDateOfBirthDesc() {
        return dateOfBirthDesc;
    }

    public void setDateOfBirthDesc(String dateOfBirthDesc) {
        this.dateOfBirthDesc = dateOfBirthDesc;
    }

    public String getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(String confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getConfirmationDateDesc() {
        return confirmationDateDesc;
    }

    public void setConfirmationDateDesc(String confirmationDateDesc) {
        this.confirmationDateDesc = confirmationDateDesc;
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

    public String getPresentStatus() {
        return presentStatus;
    }

    public void setPresentStatus(String presentStatus) {
        this.presentStatus = presentStatus;
    }

    public String getPresentStatusReason() {
        return presentStatusReason;
    }

    public void setPresentStatusReason(String presentStatusReason) {
        this.presentStatusReason = presentStatusReason;
    }

    public String getPresentStatusReasonDesc() {
        return presentStatusReasonDesc;
    }

    public void setPresentStatusReasonDesc(String presentStatusReasonDesc) {
        this.presentStatusReasonDesc = presentStatusReasonDesc;
    }

    public String getPresentStatusRemarks() {
        return presentStatusRemarks;
    }

    public void setPresentStatusRemarks(String presentStatusRemarks) {
        this.presentStatusRemarks = presentStatusRemarks;
    }

    public String getDateOfPresentStatus() {
        return dateOfPresentStatus;
    }

    public void setDateOfPresentStatus(String dateOfPresentStatus) {
        this.dateOfPresentStatus = dateOfPresentStatus;
    }

    public String getDateOfPresentStatusDesc() {
        return dateOfPresentStatusDesc;
    }

    public void setDateOfPresentStatusDesc(String dateOfPresentStatusDesc) {
        this.dateOfPresentStatusDesc = dateOfPresentStatusDesc;
    }

    public String getPresentStatusDesc() {
        return presentStatusDesc;
    }

    public void setPresentStatusDesc(String presentStatusDesc) {
        this.presentStatusDesc = presentStatusDesc;
    }

    public String getModeOfEntry() {
        return modeOfEntry;
    }

    public void setModeOfEntry(String modeOfEntry) {
        this.modeOfEntry = modeOfEntry;
    }

    public String getModeOfEntryDesc() {
        return modeOfEntryDesc;
    }

    public void setModeOfEntryDesc(String modeOfEntryDesc) {
        this.modeOfEntryDesc = modeOfEntryDesc;
    }

    public String getPresentAnnualEmulument() {
        return presentAnnualEmulument;
    }

    public void setPresentAnnualEmulument(String presentAnnualEmulument) {
        this.presentAnnualEmulument = presentAnnualEmulument;
    }

    public List<EmployeeCadreDTO> getCadreList() {
        return cadreList;
    }

    public void setCadreList(List<EmployeeCadreDTO> cadreList) {
        this.cadreList = cadreList;
    }

    public List<EmployeeAppointmentsDTO> getAppointmentsList() {
        return appointmentsList;
    }

    public void setAppointmentsList(List<EmployeeAppointmentsDTO> appointmentsList) {
        this.appointmentsList = appointmentsList;
    }

    public List<EmployeeLocationDTO> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<EmployeeLocationDTO> locationList) {
        this.locationList = locationList;
    }

    public List<EmployeeStatusDTO> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<EmployeeStatusDTO> statusList) {
        this.statusList = statusList;
    }

    public List<DisciplineDTO> getDisciplineList() {
        return disciplineList;
    }

    public void setDisciplineList(List<DisciplineDTO> disciplineList) {
        this.disciplineList = disciplineList;
    }

    public List<AnnualLeaveRosterDTO> getAnnualLeaveList() {
        return annualLeaveList;
    }

    public void setAnnualLeaveList(List<AnnualLeaveRosterDTO> annualLeaveList) {
        this.annualLeaveList = annualLeaveList;
    }

    public List<StaffLeaveApplicationDTO> getLeaveApplicationList() {
        return leaveApplicationList;
    }

    public void setLeaveApplicationList(List<StaffLeaveApplicationDTO> leaveApplicationList) {
        this.leaveApplicationList = leaveApplicationList;
    }

    public List<StaffFurtherStudyApplicationDTO> getTrainingApplicationList() {
        return trainingApplicationList;
    }

    public void setTrainingApplicationList(List<StaffFurtherStudyApplicationDTO> trainingApplicationList) {
        this.trainingApplicationList = trainingApplicationList;
    }

    public List<RegistryDocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<RegistryDocumentDTO> documents) {
        this.documents = documents;
    }

    public void addDocument(RegistryDocumentDTO entry) {
        documents.add(entry);
    }

    public void addAcademicData(AcademicDataDTO entry) {
        academicDataList.add(entry);
    }

    public void addNofkin(NextOfKinDTO entry) {
        nextOfKinList.add(entry);
    }

    public void addCadre(EmployeeCadreDTO entry) {
        cadreList.add(entry);
    }

    public void addAppointment(EmployeeAppointmentsDTO entry) {
        appointmentsList.add(entry);
    }

    public void addLocation(EmployeeLocationDTO entry) {
        locationList.add(entry);
    }

    public void addStatus(EmployeeStatusDTO entry) {
        statusList.add(entry);
    }

    public void addDiscpline(DisciplineDTO entry) {
        disciplineList.add(entry);
    }

    public void addAnnualLeaveRoster(AnnualLeaveRosterDTO entry) {
        annualLeaveList.add(entry);
    }

    public void addLeaveApplication(StaffLeaveApplicationDTO entry) {
        leaveApplicationList.add(entry);
    }

    public void addTrainingApplication(StaffFurtherStudyApplicationDTO entry) {
        trainingApplicationList.add(entry);
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTransferedEntryOrganization() {
        return transferedEntryOrganization;
    }

    public void setTransferedEntryOrganization(String transferedEntryOrganization) {
        this.transferedEntryOrganization = transferedEntryOrganization;
    }

    public String getPresentlyDisciplined() {
        return presentlyDisciplined;
    }

    public void setPresentlyDisciplined(String presentlyDisciplined) {
        this.presentlyDisciplined = presentlyDisciplined;
    }

    public String getPresentlySuspended() {
        return presentlySuspended;
    }

    public void setPresentlySuspended(String presentlySuspended) {
        this.presentlySuspended = presentlySuspended;
    }

    public boolean isTransferEntry() {
        return StringUtils.equals(getModeOfEntry(), AppConstants.TRANSFER_ENTRY);
    }

    public boolean isActive() {
        return StringUtils.equals(getPresentStatus(), AppConstants.PERSONNEL_ACTIVE);
    }

    public boolean isSuspended() {
        return StringUtils.equals(getPresentStatus(), AppConstants.PERSONNEL_SUSPENDED);
    }

    public boolean isDisciplined() {
        return StringUtils.equals(getPresentlyDisciplined(), "Y");
    }

    public boolean isOutOfService() {
        return isDisengaged();
    }

    public boolean isDisengaged() {
        return StringUtils.equals(getPresentStatus(), AppConstants.PERSONNEL_RETIRED)
                || StringUtils.equals(getPresentStatus(), AppConstants.PERSONNEL_DECEASED)
                || StringUtils.equals(getPresentStatus(), AppConstants.PERSONNEL_DISMISSED)
                || StringUtils.equals(getPresentStatus(), AppConstants.PERSONNEL_TERMINATED);
    }

    public String getLengthOfStay() {
        return lengthOfStay;
    }

    public void setLengthOfStay(String lengthOfStay) {
        this.lengthOfStay = lengthOfStay;
    }

    public String getLengthOfSpecialAppoint() {
        return lengthOfSpecialAppoint;
    }

    public void setLengthOfSpecialAppoint(String lengthOfSpecialAppoint) {
        this.lengthOfSpecialAppoint = lengthOfSpecialAppoint;
    }

    public List<ConversionDTO> getConversionList() {
        return conversionList;
    }

    public void setConversionList(List<ConversionDTO> conversionList) {
        this.conversionList = conversionList;
    }

    public void addConversion(ConversionDTO dto) {
        conversionList.add(dto);
    }

    public String getPfaId() {
        return pfaId;
    }

    public void setPfaId(String pfaId) {
        this.pfaId = pfaId;
    }

    public String getPfaDescription() {
        return pfaDescription;
    }

    public void setPfaDescription(String pfaDescription) {
        this.pfaDescription = pfaDescription;
    }

    public String getPfaNumber() {
        return pfaNumber;
    }

    public void setPfaNumber(String pfaNumber) {
        this.pfaNumber = pfaNumber;
    }

    public String getNhfNumber() {
        return nhfNumber;
    }

    public void setNhfNumber(String nhfNumber) {
        this.nhfNumber = nhfNumber;
    }

    public String getNhisNumber() {
        return nhisNumber;
    }

    public void setNhisNumber(String nhisNumber) {
        this.nhisNumber = nhisNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getIppisNumber() {
        return ippisNumber;
    }

    public void setIppisNumber(String ippisNumber) {
        this.ippisNumber = ippisNumber;
    }

    public String getSalaryBankId() {
        return salaryBankId;
    }

    public void setSalaryBankId(String salaryBankId) {
        this.salaryBankId = salaryBankId;
    }

    public String getSalaryBankDescription() {
        return salaryBankDescription;
    }

    public void setSalaryBankDescription(String salaryBankDescription) {
        this.salaryBankDescription = salaryBankDescription;
    }
   
    public String getSalaryBankAccountNo() {
        return salaryBankAccountNo;
    }

    public void setSalaryBankAccountNo(String salaryBankAccountNo) {
        this.salaryBankAccountNo = salaryBankAccountNo;
    }

    public String getOperationBankId() {
        return operationBankId;
    }

    public void setOperationBankId(String operationBankId) {
        this.operationBankId = operationBankId;
    }

    public String getOperationBankDescription() {
        return operationBankDescription;
    }

    public void setOperationBankDescription(String operationBankDescription) {
        this.operationBankDescription = operationBankDescription;
    }

    public String getOperationBankAccountNo() {
        return operationBankAccountNo;
    }

    public void setOperationBankAccountNo(String operationBankAccountNo) {
        this.operationBankAccountNo = operationBankAccountNo;
    }

}
