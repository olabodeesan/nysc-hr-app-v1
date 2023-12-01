/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.appoitnments.recruit;

import dto.employees.AcademicDataDTO;
import dto.employees.EmployeeDTO;
import dto.employees.NextOfKinDTO;
import dto.employees.RetirementDatesDTO;
import dto.setup.RankDTO;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.FacesLocal;
import org.primefaces.context.RequestContext;
import service.employees.EmployeeService;
import service.utils.ApplicationCodesService;
import service.utils.DBUtil;
import view.utils.AppConstants;
import view.utils.ApplicationCodesLoader;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.EmployeeValidator;

/**
 *
 * @author IronHide
 */
public class NewEmployeeMBean implements Serializable {

    private EmployeeDTO recordDto;
    private AcademicDataDTO newAcademicData, editAcademicData;
    private NextOfKinDTO newNextOfKin, editNextOfKin;
    private int tempEntryId = 0;

    private ViewHelper viewHelper;
    private EmployeeValidator validator;
    private EmployeeService service;
    private ApplicationCodesService codeService;
    private ApplicationCodesLoader codeLoader;

    private Map<String, String> stepMap;
    private Map<String, String> lgaMap;
    private Map<String, String> qualificationsMap;

    public NewEmployeeMBean() {
        viewHelper = new ViewHelper();
        validator = new EmployeeValidator();
        service = new EmployeeService();
        codeService = new ApplicationCodesService();
        codeLoader = new ApplicationCodesLoader();
        
        initRecord();
    }
    
    private void initRecord(){
        FacesContext context = FacesContext.getCurrentInstance();
        String mode_of_entry = FacesLocal.getRequestParameter(context, "m23asdfg");
        
        recordDto = new EmployeeDTO();
        if(StringUtils.equals(mode_of_entry, AppConstants.FRESH_ENTRY) || StringUtils.equals(mode_of_entry, AppConstants.TRANSFER_ENTRY)){            
            recordDto.setModeOfEntry(mode_of_entry);
        }else{
            recordDto.setModeOfEntry(null);
        }
    }

    public void handleNewGradeLevelSelect() {
        fetchGradeSteps();
        fetchRank();
    }

    private void fetchGradeSteps() {
        try {
            stepMap = codeService.getGradeStep(recordDto.getPresentGradeLevel());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleNewStateOriginSelect() {
        fetchLgas(recordDto.getStateOfOrigin());
    }

    private void fetchLgas(String state_id) {
        try {
            lgaMap = codeService.getStateLGAs(state_id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleNewCadreSelect() {
        fetchRank();
    }

    private void fetchRank() {
        try {
            RankDTO rank = codeService.getRank(recordDto.getPresentCadre(), recordDto.getPresentGradeLevel());
            if (rank != null) {
                recordDto.setPresentRank(rank.getRecordId());
                recordDto.setPresentRankDesc(rank.getDescription());
            } else {
                recordDto.setPresentRank(null);
                recordDto.setPresentRankDesc("?");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

            recordDto.setPresentRank(null);
            recordDto.setPresentRankDesc("?");
        }
    }

    public void createAction(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();

        boolean validation_outcome = validator.validateNewEmployee(context, recordDto);

        if (validation_outcome) {

          //  System.out.println("Am here Outome is" + validation_outcome);
            //gen an id
            DBUtil dbUtil = new DBUtil();

            Integer employeeRecordId = dbUtil.generateEmployeeId();
          //  System.out.println("Am here Outome is new employee id is " + employeeRecordId);
            
            if (employeeRecordId != null) {

                recordDto.setRecordId(employeeRecordId.toString());

                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();

                recordDto.setCreated(activityDate);
                recordDto.setCreatedBy(currentUser);
                recordDto.setLastMod(activityDate);
                recordDto.setLastModBy(currentUser);

                //set the status to 1 = active
                recordDto.setPresentStatus(AppConstants.PERSONNEL_ACTIVE);
                
                //fix dates to correspond to date of present appointment
                String _dateOfPresentAppointment = recordDto.getDateOfPresentAppointment();
                
                //if fresh employee reset date of first appt to date of present appt
                if(StringUtils.equals(recordDto.getModeOfEntry(), AppConstants.FRESH_ENTRY)){
                    recordDto.setDateOfFirstAppointment(_dateOfPresentAppointment);
                    
                    //empty the transfer org
                    recordDto.setTransferedEntryOrganization(null);
                }               
                
                recordDto.setDateOfPresentCadre(_dateOfPresentAppointment);
                recordDto.setDateOfPostingToPresentLocation(_dateOfPresentAppointment);
                recordDto.setDateOfPresentStatus(_dateOfPresentAppointment);

                //calculate the retirement dates
                RetirementDatesDTO retirementDates = viewHelper.calculateRetirementDates(recordDto);

                if (retirementDates.isOutcome() == true) {

                    recordDto.setDateToRetireBasedOnLengthOfStay(retirementDates.getDateToRetireBasedOnLengthOfStay());
                    recordDto.setDateToRetireBasedOnAge(retirementDates.getDateToRetireBasedOnAge());
                    recordDto.setDateDueForRetirement(retirementDates.getDateDueForRetirement());

                    try {
                        boolean result = service.createEmployee(recordDto);
                        if (result) {
                            try {
                                FacesLocal.redirect(context, "appointment-recruitment/new-personnel-success.jsf?vyTasGsK=%s&vhsuYagsh62fshkfd=%s&8uss23kj=%s",
                                        "sDhsf6dfasugf7374ugsydgfaksgfysgf", recordDto.getRecordId(), "ashdjahsd75678sdk");
                            } catch (Exception exc) {
                                //think of what to do here
                            }
                        } else {
                            ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                        }
                    } catch (Exception exc) {
                        exc.printStackTrace();
                        String msg = exc.getMessage();
                        if (StringUtils.equals(msg, AppConstants.DUPLICATE)) {
                            ViewHelper.addErrorMessage(context, null, "Error: File number already exists.", null);
                        } else if (StringUtils.equals(msg, AppConstants.DUPLICATE_PHONE)) {
                            ViewHelper.addErrorMessage(context, null, "Error: Primary phone number already exists.", null);
                        } else if (StringUtils.equals(msg, AppConstants.DUPLICATE_SEC_PHONE)) {
                            ViewHelper.addErrorMessage(context, null, "Error: Secondary phone number already exists.", null);
                        } else if (StringUtils.equals(msg, AppConstants.DUPLICATE_EMAIL)) {
                            ViewHelper.addErrorMessage(context, null, "Error: Primary email already exists.", null);
                        } else if (StringUtils.equals(msg, AppConstants.DUPLICATE_SEC_EMAIL)) {
                            ViewHelper.addErrorMessage(context, null, "Error: Secondary email already exists.", null);
                        }else if (StringUtils.equals(msg, AppConstants.NO_EMOLUMENT)) {
                            ViewHelper.addErrorMessage(context, null, "Error: No annual emolument found for grade level/step.", null);
                        } else {
                            ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                        }
                    }
                } else {
                    ViewHelper.addErrorMessage(context, null, "System Error: Date recalculation failed. Try Again.", null);
                }

            } else {
                ViewHelper.addErrorMessage(context, null, "System Error: Record ID could not be generated. Try Again.", null);
            }

        }
    }

    //ACADEMIC DATA
    public void handleNewInstTypeSelect() {
        fetchQualifications(newAcademicData.getInstType());
    }

    public void handleEditInstTypeSelect() {
        fetchQualifications(editAcademicData.getInstType());
    }

    private void fetchQualifications(String inst_type) {
        try {
            qualificationsMap = codeService.getQualificationsByCategory(inst_type);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAcademicData(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        boolean validation_outcome = validator.validateAcademicData(context, newAcademicData);
        if (validation_outcome) {

            ++tempEntryId;

            newAcademicData.setTempId(Integer.toString(tempEntryId));

            //clear course if not tertiary
            if (!StringUtils.equals(newAcademicData.getInstType(), "3")) {
                newAcademicData.setCourse(null);
                newAcademicData.setCourseDesc(null);
            }

            //get the corresponding attribute names
            //inst type
            for (Entry<String, String> entry : codeLoader.getInstitutionTypes().entrySet()) {
                if (entry.getValue().equals(newAcademicData.getInstType())) {
                    newAcademicData.setInstTypeDesc(entry.getKey());
                    break;
                }
            }

            //course
            if (!StringUtils.isBlank(newAcademicData.getCourse())) {
                for (Entry<String, String> entry : codeLoader.getCourses().entrySet()) {
                    if (entry.getValue().equals(newAcademicData.getCourse())) {
                        newAcademicData.setCourseDesc(entry.getKey());
                        break;
                    }
                }
            }

            //country
            for (Entry<String, String> entry : codeLoader.getCountries().entrySet()) {
                if (entry.getValue().equals(newAcademicData.getCountry())) {
                    newAcademicData.setCountryDesc(entry.getKey());
                    break;
                }
            }

            //qualification
            for (Entry<String, String> entry : codeLoader.getQualifications().entrySet()) {
                if (entry.getValue().equals(newAcademicData.getQualification())) {
                    newAcademicData.setQualificationDesc(entry.getKey());
                    break;
                }
            }

            recordDto.getAcademicDataList().add(newAcademicData);
            newAcademicData = null;

            ViewHelper.addInfoMessage(context, null, "Academic data added successfully. You may continue or close this dialog.", null);

        }
    }

    public void prepareAcadDataUpdate(String entry_temp_id) {
        if (!StringUtils.isBlank(entry_temp_id)) {
            try {
                for (int i = 0; i < recordDto.getAcademicDataList().size(); i++) {
                    if (StringUtils.equals(entry_temp_id, recordDto.getAcademicDataList().get(i).getTempId())) {
                        editAcademicData = recordDto.getAcademicDataList().get(i);
                        break;
                    }
                }

                fetchQualifications(editAcademicData.getInstType());

                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("editAcadDialogPanel");
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
            }
        } else {
            editAcademicData = null;
        }
    }

    public void updateAcademicData(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance();

        boolean validation_outcome = validator.validateAcademicData(context, editAcademicData);

        if (validation_outcome) {
            int entry_index = -1;
            for (int i = 0; i < recordDto.getAcademicDataList().size(); i++) {
                if (StringUtils.equals(editAcademicData.getTempId(), recordDto.getAcademicDataList().get(i).getTempId())) {
                    entry_index = i;
                    break;
                }
            }

            //clear course if not tertiary
            if (!StringUtils.equals(editAcademicData.getInstType(), "3")) {
                editAcademicData.setCourse(null);
                editAcademicData.setCourseDesc(null);
            }

            //get the corresponding attribute names
            //inst type
            for (Entry<String, String> entry : codeLoader.getInstitutionTypes().entrySet()) {
                if (entry.getValue().equals(editAcademicData.getInstType())) {
                    editAcademicData.setInstTypeDesc(entry.getKey());
                    break;
                }
            }

            //course
            if (!StringUtils.isBlank(editAcademicData.getCourse())) {
                for (Entry<String, String> entry : codeLoader.getCourses().entrySet()) {
                    if (entry.getValue().equals(editAcademicData.getCourse())) {
                        editAcademicData.setCourseDesc(entry.getKey());
                        break;
                    }
                }
            }

            //country
            for (Entry<String, String> entry : codeLoader.getCountries().entrySet()) {
                if (entry.getValue().equals(editAcademicData.getCountry())) {
                    editAcademicData.setCountryDesc(entry.getKey());
                    break;
                }
            }

            //qualification
            for (Entry<String, String> entry : codeLoader.getQualifications().entrySet()) {
                if (entry.getValue().equals(editAcademicData.getQualification())) {
                    editAcademicData.setQualificationDesc(entry.getKey());
                    break;
                }
            }

            recordDto.getAcademicDataList().remove(entry_index);
            recordDto.getAcademicDataList().add(entry_index, editAcademicData);

            reqContext.addCallbackParam("editacadresult", true);
        } else {
            reqContext.addCallbackParam("editacadresult", false);
        }
    }

    public void removeAcademicData(String entry_temp_id) {

        if (!StringUtils.isBlank(entry_temp_id)) {
            int entry_index = -1;
            for (int i = 0; i < recordDto.getAcademicDataList().size(); i++) {
                if (StringUtils.equals(entry_temp_id, recordDto.getAcademicDataList().get(i).getTempId())) {
                    entry_index = i;
                    break;
                }
            }

            recordDto.getAcademicDataList().remove(entry_index);
        }
    }

    //NEXT OF KIN DATA
//    public void prepareNewNofkinData(){
//        
//    }
    public void addNofkinData(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        boolean validation_outcome = validator.validateNextOfKin(context, newNextOfKin);

        if (validation_outcome) {

            ++tempEntryId;

            newNextOfKin.setTempId(Integer.toString(tempEntryId));

            //set the relationship name
            for (Entry<String, String> entry : codeLoader.getRelationships().entrySet()) {
                if (entry.getValue().equals(newNextOfKin.getRelationship())) {
                    newNextOfKin.setRelationshipDesc(entry.getKey());
                    break;
                }
            }

            recordDto.getNextOfKinList().add(newNextOfKin);
            newNextOfKin = null;

            ViewHelper.addInfoMessage(context, null, "Next of kin info added successfully. You may continue or close this dialog.", null);

        }
    }

    public void prepareNofkinDataUpdate(String entry_temp_id) {
        if (!StringUtils.isBlank(entry_temp_id)) {
            try {
                for (int i = 0; i < recordDto.getNextOfKinList().size(); i++) {
                    if (StringUtils.equals(entry_temp_id, recordDto.getNextOfKinList().get(i).getTempId())) {
                        editNextOfKin = recordDto.getNextOfKinList().get(i);
                        break;
                    }
                }

                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("editNokinDialogPanel");
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
            }
        } else {
            editNextOfKin = null;
        }
    }

    public void updateNofkinData(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance();

        boolean validation_outcome = validator.validateNextOfKin(context, editNextOfKin);

        if (validation_outcome) {
            int entry_index = -1;
            for (int i = 0; i < recordDto.getNextOfKinList().size(); i++) {
                if (StringUtils.equals(editNextOfKin.getTempId(), recordDto.getNextOfKinList().get(i).getTempId())) {
                    entry_index = i;
                    break;
                }
            }

            //set the relationship name
            for (Entry<String, String> entry : codeLoader.getRelationships().entrySet()) {
                if (entry.getValue().equals(editNextOfKin.getRelationship())) {
                    editNextOfKin.setRelationshipDesc(entry.getKey());
                    break;
                }
            }

            recordDto.getNextOfKinList().remove(entry_index);
            recordDto.getNextOfKinList().add(entry_index, editNextOfKin);

            reqContext.addCallbackParam("editnofkinresult", true);
        } else {
            reqContext.addCallbackParam("editnofkinresult", true);
        }
    }

    public void removeNofkinData(String entry_temp_id) {

        if (!StringUtils.isBlank(entry_temp_id)) {
            int entry_index = -1;
            for (int i = 0; i < recordDto.getNextOfKinList().size(); i++) {
                if (StringUtils.equals(entry_temp_id, recordDto.getNextOfKinList().get(i).getTempId())) {
                    entry_index = i;
                    break;
                }
            }

            recordDto.getNextOfKinList().remove(entry_index);
        }
    }

    public EmployeeDTO getRecordDto() {
        if (recordDto == null) {
            recordDto = new EmployeeDTO();
        }
        return recordDto;
    }

    public void setRecordDto(EmployeeDTO recordDto) {
        this.recordDto = recordDto;
    }

    public AcademicDataDTO getNewAcademicData() {
        if (newAcademicData == null) {
            newAcademicData = new AcademicDataDTO();
        }
        return newAcademicData;
    }

    public void setNewAcademicData(AcademicDataDTO newAcademicData) {
        this.newAcademicData = newAcademicData;
    }

    public NextOfKinDTO getNewNextOfKin() {
        if (newNextOfKin == null) {
            newNextOfKin = new NextOfKinDTO();
        }
        return newNextOfKin;
    }

    public void setNewNextOfKin(NextOfKinDTO newNextOfKin) {
        this.newNextOfKin = newNextOfKin;
    }

    public AcademicDataDTO getEditAcademicData() {
        if (editAcademicData == null) {
            editAcademicData = new AcademicDataDTO();
        }
        return editAcademicData;
    }

    public void setEditAcademicData(AcademicDataDTO editAcademicData) {
        this.editAcademicData = editAcademicData;
    }

    public NextOfKinDTO getEditNextOfKin() {
        if (editNextOfKin == null) {
            editNextOfKin = new NextOfKinDTO();
        }
        return editNextOfKin;
    }

    public void setEditNextOfKin(NextOfKinDTO editNextOfKin) {
        this.editNextOfKin = editNextOfKin;
    }

    public Map<String, String> getLgaMap() {
        if (lgaMap == null) {
            lgaMap = new LinkedHashMap<>();
        }
        return lgaMap;
    }

    public void setLgaMap(Map<String, String> lgaMap) {
        this.lgaMap = lgaMap;
    }

    public Map<String, String> getQualificationsMap() {
        if (qualificationsMap == null) {
            qualificationsMap = new LinkedHashMap<>();
        }
        return qualificationsMap;
    }

    public void setQualificationsMap(Map<String, String> qualificationsMap) {
        this.qualificationsMap = qualificationsMap;
    }

    public Map<String, String> getStepMap() {
        if (stepMap == null) {
            stepMap = new LinkedHashMap<>();
        }
        return stepMap;
    }

    public void setStepMap(Map<String, String> stepMap) {
        this.stepMap = stepMap;
    }

}
