/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.employees;

import dto.employees.AcademicDataDTO;
import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.FacesLocal;
import org.primefaces.context.RequestContext;
import service.employees.EmployeeService;
import service.utils.ApplicationCodesService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.EmployeeValidator;

/**
 *
 * @author IronHide
 */
public class UpdateEmployeeAcadMBean implements Serializable {

    private EmployeeDTO recordDto;
    private AcademicDataDTO newAcademicData, editAcademicData;
    private String recordId;
    private boolean inEditMode;

    private Map<String, String> qualificationsMap;

    private ApplicationCodesService codeService;
    private EmployeeService service;
    private EmployeeValidator validator;
    private ViewHelper viewHelper;
    
    private FetchOptionsDTO fetchOptions;

    public UpdateEmployeeAcadMBean() {
        service = new EmployeeService();
        codeService = new ApplicationCodesService();
        validator = new EmployeeValidator();
        viewHelper = new ViewHelper();
        
        fetchOptions = new FetchOptionsDTO();
        fetchOptions.setFetchAcadData(true);

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
            
            recordDto = service.getEmployeee(employee_id, AppConstants.FETCH_MODE_RECORD_ID, AppConstants.FETCH_VIEW_UPDATE,fetchOptions );
        } catch (Exception exc) {
            recordDto = null;
            inEditMode = false;
            exc.printStackTrace();
        }
    }

    public void enableEditMode() {
        inEditMode = true;
    }

    public void cancelEditMode() {
        inEditMode = false;
    }
    
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

    public void addAcadData(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateAcademicData(context, newAcademicData);

        if (validation_outcome) {
            String activityDate = DateTimeUtils.getActivityDate();
            String currentUser = viewHelper.getAppUser().getUsername();

            newAcademicData.setCreated(activityDate);
            newAcademicData.setCreatedBy(currentUser);
            newAcademicData.setLastMod(activityDate);
            newAcademicData.setLastModBy(currentUser);
            
            newAcademicData.setEmployeeRecordId(recordDto.getRecordId());

            try {
                boolean result = service.addEmployeeAcadData(newAcademicData);
                if (result) {
                    ViewHelper.addInfoMessage(context, null, "Record Saved Successfully. You may enter another record, or close the dialog.", null);
                    newAcademicData = null;

                    loadRecord();
                } else {
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
                ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
            }
        }
    }

    public void prepareAcadUpdateByLink(String row_record_id) {
        if (!StringUtils.isBlank(row_record_id)) {
            try {
                editAcademicData = service.getEmployeeAcadData(row_record_id, AppConstants.FETCH_VIEW_UPDATE);
                
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

    public void updateAcadData(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        RequestContext reqContext = RequestContext.getCurrentInstance();
        boolean validation_outcome = validator.validateAcademicData(context, editAcademicData);
        if (validation_outcome) {
            String activityDate = DateTimeUtils.getActivityDate();
            String currentUser = viewHelper.getAppUser().getUsername();

            editAcademicData.setLastMod(activityDate);
            editAcademicData.setLastModBy(currentUser);

            try {
                boolean result = service.updateEmployeeAcadData(editAcademicData);
                if (result) {
                    ViewHelper.addInfoMessage(context, null, "Record Updated Successfully. Close this dialog", null);
                    editAcademicData = null;

                    reqContext.addCallbackParam("editacadresult", true);

                    loadRecord();
                } else {
                    reqContext.addCallbackParam("editacadresult", false);
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
                reqContext.addCallbackParam("editacadresult", false);
                ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
            }
        } else {
            reqContext.addCallbackParam("editacadresult", false);
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

    public AcademicDataDTO getNewAcademicData() {
        if (newAcademicData == null) {
            newAcademicData = new AcademicDataDTO();
        }
        return newAcademicData;
    }

    public void setNewAcademicData(AcademicDataDTO newAcademicData) {
        this.newAcademicData = newAcademicData;
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

    public Map<String, String> getQualificationsMap() {
        if (qualificationsMap == null) {
            qualificationsMap = new LinkedHashMap<>();
        }
        return qualificationsMap;
    }

    public void setQualificationsMap(Map<String, String> qualificationsMap) {
        this.qualificationsMap = qualificationsMap;
    }

}
