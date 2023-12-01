/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.training;

import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import dto.setup.TrainingSpecializationDTO;
import dto.setup.dto.training.TrainingParticipantDTO;
import dto.setup.dto.training.TrainingProposalDTO;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.FacesLocal;
import service.employees.EmployeeService;
import service.training.TrainingProposalService;
import view.utils.AppConstants;
import view.utils.ApplicationCodesLoader;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.TrainingValidator;

/**
 *
 * @author IronHide
 */
public class UpdateTrainingProposalMBean implements Serializable {

    private TrainingProposalDTO recordDto;
    private String proposalRecordId;
    private TrainingSpecializationDTO newSpecializationDto;
    private TrainingParticipantDTO newParticipantDto;

    private String srchParticipantFileNo;

    private final TrainingProposalService trainingService;
    private final EmployeeService employeeService;
    private final TrainingValidator validator;
    private final ViewHelper viewHelper;

    private final FetchOptionsDTO fetchOptions;

    private Map<String, String> trainingSpecializationMapCopy;
    private Map<String, String> countryMapCopy;
    private Map<String, String> countryMap;

    public UpdateTrainingProposalMBean() {
        trainingService = new TrainingProposalService();
        employeeService = new EmployeeService();
        validator = new TrainingValidator();
        viewHelper = new ViewHelper();

        fetchOptions = new FetchOptionsDTO();
    }

    private void loadTrainingProposal() {
        FacesContext context = FacesContext.getCurrentInstance();
        proposalRecordId = FacesLocal.getRequestParameter(context, "vhsuYagsh62fshkfd");

        if (!StringUtils.isBlank(proposalRecordId)) {
            try {
                recordDto = trainingService.getTrainingProposal(proposalRecordId, true, true);

                if (recordDto != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    recordDto.setStartDate(dateFormat.parse(recordDto.getExpectedStartDate()));
                    recordDto.setEndDate(dateFormat.parse(recordDto.getExpectedEndDate()));

                    processTrainingTypeChange();
                }
            } catch (Exception e) {

            }
        }
    }

    @PostConstruct
    public void afterConstruct() {
        ApplicationCodesLoader codesLoader = new ApplicationCodesLoader();
        trainingSpecializationMapCopy = new LinkedHashMap<>(codesLoader.getTrainingSpecializations());
        countryMapCopy = new LinkedHashMap<>(codesLoader.getCountries());

        loadTrainingProposal();
    }

    public void handleTrainingTypeChange() {
        processTrainingTypeChange();
    }

    private void processTrainingTypeChange() {
        String training_type = recordDto.getTrainingType();
        if (!StringUtils.isBlank(training_type)) {
            if (StringUtils.equals(training_type, AppConstants.LOCAL_TRAINING)) {
                countryMap = new LinkedHashMap<>();
                countryMap.put(AppConstants.NIGERIA_DESC, AppConstants.NIGERIA);
            } else {
                countryMap = new LinkedHashMap<>(countryMapCopy);
                countryMap.remove(AppConstants.NIGERIA_DESC);
            }
        }
    }

    public void addSpecialization() {
        if (newSpecializationDto != null) {
            if (!StringUtils.isBlank(newSpecializationDto.getRecordId())) {

                //first check for duplicates
                boolean exists = false;

                for (TrainingSpecializationDTO entry : recordDto.getSpecializations()) {
                    if (StringUtils.equals(entry.getRecordId(), newSpecializationDto.getRecordId())) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    for (Map.Entry<String, String> entry : trainingSpecializationMapCopy.entrySet()) {
                        if (StringUtils.equals(entry.getValue(), newSpecializationDto.getRecordId())) {
                            newSpecializationDto.setDescription(entry.getKey());
                            break;
                        }
                    }

                    recordDto.getSpecializations().add(newSpecializationDto);
                    newSpecializationDto = null;
                } else {
                    FacesContext context = FacesContext.getCurrentInstance();
                    ViewHelper.addWarningMessage(context, "spec_txt", "Warning: Duplicate", null);
                }

            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                ViewHelper.addWarningMessage(context, "spec_txt", "Warning: No selection to add", null);
            }
        }
    }

    public void removeSpecialization(String specialization_id) {
        if (!StringUtils.isBlank(specialization_id)) {

            int remove_index = -1;
            int list_size = recordDto.getSpecializations().size();
            TrainingSpecializationDTO entry = null;

            for (int i = 0; i < list_size; i++) {
                entry = recordDto.getSpecializations().get(i);
                if (StringUtils.equals(entry.getRecordId(), specialization_id)) {
                    remove_index = i;
                    break;
                }
            }

            if (remove_index != -1) {
                recordDto.getSpecializations().remove(remove_index);
            }
        }
    }

    public void loadEmployeeRecord() {
        newParticipantDto = null;
        if (!StringUtils.isBlank(srchParticipantFileNo)) {

            //check for duplicate
            boolean exists = false;
            for (TrainingParticipantDTO p : recordDto.getParticipants()) {
                if (StringUtils.equalsIgnoreCase(p.getFileNo(), srchParticipantFileNo)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                try {
                    EmployeeDTO employee = employeeService.getEmployeee(srchParticipantFileNo,
                            AppConstants.FETCH_MODE_FILE_NO, AppConstants.FETCH_VIEW_ONLY, fetchOptions);

                    newParticipantDto = new TrainingParticipantDTO();
                    newParticipantDto.setEmployeeRecordId(employee.getRecordId());
                    newParticipantDto.setFileNo(employee.getFileNo());
                    newParticipantDto.setSurname(employee.getSurname());
                    newParticipantDto.setOtherNames(employee.getOtherNames());
                    newParticipantDto.setGradeLevel(employee.getPresentGradeLevel());
                    newParticipantDto.setPhone(employee.getPrimaryPhone());
                    newParticipantDto.setEmail(employee.getPrimaryEmail());
                    newParticipantDto.setDepartment(employee.getPresentDepartmentDesc());

                } catch (Exception exc) {
                    newParticipantDto = null;
                    String msg = exc.getMessage();
                    FacesContext context = FacesContext.getCurrentInstance();
                    if(StringUtils.equals(msg, AppConstants.NOT_FOUND)){
                        ViewHelper.addWarningMessage(context, "participant_txt", "Participant not found", null);
                    }else{
                        ViewHelper.addWarningMessage(context, "participant_txt", "Error fetching personnel info", null);
                    }
                    //System.out.println(exc.getMessage());
                }
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                ViewHelper.addWarningMessage(context, "participant_txt", "Duplicate participant", null);
            }

        } else {
            newParticipantDto = null;
            FacesContext context = FacesContext.getCurrentInstance();
            ViewHelper.addWarningMessage(context, "participant_txt", "File number is required", null);
        }
    }

    public void addParticipant() {
        if (newParticipantDto != null) {
            if (!StringUtils.isBlank(newParticipantDto.getEmployeeRecordId())) {
                recordDto.getParticipants().add(newParticipantDto);
                newParticipantDto = null;
                srchParticipantFileNo = null;
            }
        }
    }

    public void removeParticipant(String participant_file_no) {
        if (!StringUtils.isBlank(participant_file_no)) {

            int remove_index = -1;
            int list_size = recordDto.getParticipants().size();
            TrainingParticipantDTO entry = null;

            for (int i = 0; i < list_size; i++) {
                entry = recordDto.getParticipants().get(i);
                if (StringUtils.equals(entry.getFileNo(), participant_file_no)) {
                    remove_index = i;
                    break;
                }
            }

            if (remove_index != -1) {
                recordDto.getParticipants().remove(remove_index);
            }
        }
    }

    public void updateAction(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateTrainingProposal(context, recordDto, true, false, false);

        if (validation_outcome) {
            try {
                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();

                recordDto.setLastMod(activityDate);
                recordDto.setLastModBy(currentUser);

                recordDto.setExpectedStartDate(viewHelper.getDateAsDMYWithSlashString(recordDto.getStartDate()));
                recordDto.setExpectedEndDate(viewHelper.getDateAsDMYWithSlashString(recordDto.getEndDate()));

                boolean result = trainingService.updateTrainingProposal(recordDto);

                if (result) {
                    try {
                        FacesLocal.redirect(context, "training/training-proposal-success.jsf?nav=training&vyTasGsK=%s&vhsuYagsh62fshkfd=%s&8uss23kj=%s",
                                "sDhsf6dfasugf7374ugsydgfaksgfysgf", recordDto.getRecordId(), "ashdjahsd75678sdk");
                    } catch (Exception exc) {
                        //think of what to do here
                    }
                } else {
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }

            } catch (Exception e) {
                String msg = e.getMessage();
                if (StringUtils.equals(msg, AppConstants.DUPLICATE)) {
                    ViewHelper.addErrorMessage(context, null, "Error: Duplicate training title.", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }

            }
        }
    }

    public TrainingProposalDTO getRecordDto() {
        if (recordDto == null) {
            recordDto = new TrainingProposalDTO();
        }
        return recordDto;
    }

    public void setRecordDto(TrainingProposalDTO recordDto) {
        this.recordDto = recordDto;
    }

    public TrainingSpecializationDTO getNewSpecializationDto() {
        if (newSpecializationDto == null) {
            newSpecializationDto = new TrainingSpecializationDTO();
        }
        return newSpecializationDto;
    }

    public void setNewSpecializationDto(TrainingSpecializationDTO newSpecializationDto) {
        this.newSpecializationDto = newSpecializationDto;
    }

    public TrainingParticipantDTO getNewParticipantDto() {
        if (newParticipantDto == null) {
            newParticipantDto = new TrainingParticipantDTO();
        }
        return newParticipantDto;
    }

    public void setNewParticipantDto(TrainingParticipantDTO newParticipantDto) {
        this.newParticipantDto = newParticipantDto;
    }

    public Map<String, String> getCountryMap() {
        if (countryMap == null) {
            countryMap = new LinkedHashMap<>();
        }
        return countryMap;
    }

    public void setCountryMap(Map<String, String> countryMap) {
        this.countryMap = countryMap;
    }

    public String getSrchParticipantFileNo() {
        return srchParticipantFileNo;
    }

    public void setSrchParticipantFileNo(String srchParticipantFileNo) {
        this.srchParticipantFileNo = srchParticipantFileNo;
    }

}
