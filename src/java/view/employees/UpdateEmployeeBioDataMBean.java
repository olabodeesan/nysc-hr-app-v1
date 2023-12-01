/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.employees;

import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import dto.employees.RetirementDatesDTO;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.FacesLocal;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import service.employees.EmployeeService;
import service.fileupload.FileUploadService;
import service.utils.ApplicationCodesService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.EmployeeValidator;

/**
 *
 * @author IronHide
 */
public class UpdateEmployeeBioDataMBean implements Serializable {

    private EmployeeDTO recordDto;
    private String recordId;
    private boolean inEditMode;

    private ApplicationCodesService codeService;
    private Map<String, String> lgaMap;

    private EmployeeService service;
    private FileUploadService fileUploadService;
    private EmployeeValidator validator;
    private ViewHelper viewHelper;
    
    private FetchOptionsDTO fetchOptions;

    public UpdateEmployeeBioDataMBean() {
        service = new EmployeeService();
        validator = new EmployeeValidator();
        viewHelper = new ViewHelper();
        codeService = new ApplicationCodesService();
        fileUploadService = new FileUploadService();
                
        fetchOptions = new FetchOptionsDTO();

        inEditMode = false;
        loadRecord();
    }

    private void loadRecord() {

        if (!StringUtils.isBlank(recordId)) {
            fetchRecord(recordId);
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            recordId = FacesLocal.getRequestParameter(context, "vhsuYagsh62fshkfd");
            fetchRecord(recordId);
        }
    }

    private void fetchRecord(String employee_id) {
        try {
            recordDto = service.getEmployeee(employee_id, AppConstants.FETCH_MODE_RECORD_ID, AppConstants.FETCH_VIEW_UPDATE, fetchOptions);
            fetchLgas(recordDto.getStateOfOrigin());
        } catch (Exception exc) {
            recordDto = null;
            inEditMode = false;
            exc.printStackTrace();
        }
    }

    public void handleStateOriginSelect() {
        fetchLgas(recordDto.getStateOfOrigin());
    }

    private void fetchLgas(String state_id) {
        try {
            lgaMap = codeService.getStateLGAs(state_id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void enableEditMode() {
        inEditMode = true;
    }
    
    public void cancelEditMode() {
        inEditMode = false;
    }

    public void updateBiodataAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateUpdateBiodata(context, recordDto);

        if (validation_outcome) {

            String activityDate = DateTimeUtils.getActivityDate();
            String currentUser = viewHelper.getAppUser().getUsername();

            recordDto.setLastMod(activityDate);
            recordDto.setLastModBy(currentUser);

            //calculate the retirement dates
            RetirementDatesDTO retirementDates = viewHelper.calculateRetirementDates(recordDto);

            if (retirementDates.isOutcome() == true) {

                recordDto.setDateToRetireBasedOnLengthOfStay(retirementDates.getDateToRetireBasedOnLengthOfStay());
                recordDto.setDateToRetireBasedOnAge(retirementDates.getDateToRetireBasedOnAge());
                recordDto.setDateDueForRetirement(retirementDates.getDateDueForRetirement());

                try {
                    boolean result = service.updateEmployeeBiodata(recordDto);
                    if (result) {
                        inEditMode = false;
                        loadRecord();
                        ViewHelper.addInfoMessage(context, null, "Bio-data updated successfully", null);                        
                    } else {
                        ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            } else {
                ViewHelper.addErrorMessage(context, null, "System Error: Date recalculation failed. Try Again.", null);
            }

        }
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance();
        try {
            UploadedFile uploadedfile = event.getFile();

            boolean result = fileUploadService.uploadPersonnelPhoto(uploadedfile, recordDto.getRecordId());
            System.out.println("view.employees.UpdateEmployeeBioDataMBean.handleFileUpload()" + result);
            if (result) {
                FacesLocal.redirect(context, "employees/update-bio-data.jsf?vyTasGsK=%s&vhsuYagsh62fshkfd=%s&8uss23kj=%s",
                        "sDhsf6dfasugf7374ugsydgfaksgfysgf", recordDto.getRecordId(), "ashdjahsd75678sdk");
                //reqContext.addCallbackParam("photouploadresult", true);
                //ViewHelper.addInfoMessage(context, null, "Photograph uploaded successfully. You may need to click the reresh button to see update", null);
            } else {
                reqContext.addCallbackParam("photouploadresult", false);
                ViewHelper.addErrorMessage(context, null, "An error occurred while updating personnel photo. Try again.", null);
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            reqContext.addCallbackParam("photouploadresult", false);
            ViewHelper.addErrorMessage(context, null, "An error occurred while updating personnel photo. Try again.", null);
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

    public boolean isInEditMode() {
        return inEditMode;
    }

    public void setInEditMode(boolean inEditMode) {
        this.inEditMode = inEditMode;
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

}
