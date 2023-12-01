/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.training;

import dto.employees.FetchOptionsDTO;
import dto.setup.TrainingSpecializationDTO;
import dto.setup.dto.training.TrainingProposalDTO;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.FacesLocal;
import service.training.TrainingProposalService;
import service.utils.DBUtil;
import view.utils.AppConstants;
import view.utils.ApplicationCodesLoader;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.TrainingValidator;

/**
 *
 * @author IronHide
 */
public class NewTrainingProposalMBean implements Serializable {

    private TrainingProposalDTO recordDto;
    private TrainingSpecializationDTO newSpecializationDto;

    private final TrainingProposalService trainingService;
    private final TrainingValidator validator;
    private final ViewHelper viewHelper;

    private Map<String, String> trainingSpecializationMapCopy;
    private Map<String, String> countryMapCopy;

    private Map<String, String> countryMap;

    public NewTrainingProposalMBean() {
        trainingService = new TrainingProposalService();
        validator = new TrainingValidator();
        viewHelper = new ViewHelper();
    }

    @PostConstruct
    public void afterConstruct() {
        ApplicationCodesLoader codesLoader = new ApplicationCodesLoader();
        trainingSpecializationMapCopy = new LinkedHashMap<>(codesLoader.getTrainingSpecializations());
        countryMapCopy = new LinkedHashMap<>(codesLoader.getCountries());
    }

    public void handleTrainingTypeChange() {
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

    public void createAction(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateTrainingProposal(context, recordDto, true, false, true);

        if (validation_outcome) {
            try {

                DBUtil dBUtil = new DBUtil();
                Integer recordId = dBUtil.generateOtherId();

                if (recordId != null) {
                    recordDto.setRecordId(Integer.toString(recordId));

                    String activityDate = DateTimeUtils.getActivityDate();
                    String currentUser = viewHelper.getAppUser().getUsername();

                    recordDto.setCreated(activityDate);
                    recordDto.setCreatedBy(currentUser);
                    recordDto.setLastMod(activityDate);
                    recordDto.setLastModBy(currentUser);

                    recordDto.setExpectedStartDate(viewHelper.getDateAsDMYWithSlashString(recordDto.getStartDate()));
                    recordDto.setExpectedEndDate(viewHelper.getDateAsDMYWithSlashString(recordDto.getEndDate()));

                    recordDto.setApprovalStatus(AppConstants.TRAINING_PENDING_APPROVAL);
                    recordDto.setRecordStatus(AppConstants.ACTIVE);

                    boolean result = trainingService.createTrainingProposal(recordDto);

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
                } else {
                    ViewHelper.addErrorMessage(context, null, "System Error: Could not generate ID", null);
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

    public Map<String, String> getCountryMap() {
        if (countryMap == null) {
            countryMap = new LinkedHashMap<>();
        }
        return countryMap;
    }

    public void setCountryMap(Map<String, String> countryMap) {
        this.countryMap = countryMap;
    }

}
