/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.employees;

import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.FacesLocal;
import service.employees.EmployeeService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.EmployeeValidator;

/**
 *
 * @author IronHide
 */
public class UpdateEmployeeContactMBean implements Serializable {

    private EmployeeDTO recordDto;
    private String recordId;
    private boolean inEditMode;

    private EmployeeService service;
    private EmployeeValidator validator;
    private ViewHelper viewHelper;
    private FetchOptionsDTO fetchOptions;

    public UpdateEmployeeContactMBean() {
        service = new EmployeeService();
        validator = new EmployeeValidator();
        viewHelper = new ViewHelper();
        
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

    public void updateEmployeeContact(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateContactInfo(context, recordDto);

        if (validation_outcome) {

            String activityDate = DateTimeUtils.getActivityDate();
            String currentUser = viewHelper.getAppUser().getUsername();

            recordDto.setLastMod(activityDate);
            recordDto.setLastModBy(currentUser);

            try {
                boolean result = service.updateEmployeeContact(recordDto);
                if (result) {
                    inEditMode = false;
                    loadRecord();
                    ViewHelper.addInfoMessage(context, null, "Contact data updated successfully", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
            }

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

}
