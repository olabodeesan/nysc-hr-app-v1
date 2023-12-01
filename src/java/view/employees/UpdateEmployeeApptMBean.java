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
import view.utils.ViewHelper;
import view.validators.EmployeeValidator;

/**
 *
 * @author IronHide
 */
public class UpdateEmployeeApptMBean implements Serializable{

    private EmployeeDTO recordDto;
    private String recordId;
    private boolean inEditMode;

    private EmployeeService service;
    private EmployeeValidator validator;
    private ViewHelper viewHelper;
    
    private FetchOptionsDTO fetchOptions;

    public UpdateEmployeeApptMBean() {
        service = new EmployeeService();
        validator = new EmployeeValidator();
        viewHelper = new ViewHelper();
        
        fetchOptions = new FetchOptionsDTO();
        fetchOptions.setFetchAppointments(true);

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

    public void updateEmploymentDataAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        /*boolean validation_outcome = validator.validateEmploymentData(context, recordDto);

        if (validation_outcome) {

            String activityDate = DateTimeUtils.getActivityDate();
            String currentUser = viewHelper.getAppUser().getUsername();

            recordDto.setLastMod(activityDate);
            recordDto.setLastModBy(currentUser);

            //calculate the retirement dates
            RetirementDatesDTO retirementDates = viewHelper.calculateRetirementDates(recordDto.getDateOfFirstAppointment(), recordDto.getDateOfBirth());

            if (retirementDates.isOutcome() == true) {

                recordDto.setDateToRetireBasedOnLengthOfStay(retirementDates.getDateToRetireBasedOnLengthOfStay());
                recordDto.setDateToRetireBasedOnAge(retirementDates.getDateToRetireBasedOnAge());
                recordDto.setDateDueForRetirement(retirementDates.getDateDueForRetirement());

                try {
                    boolean result = service.updateEmploymentData(recordDto);
                    if (result) {
                        inEditMode = false;
                        loadRecord();
                        ViewHelper.addInfoMessage(context, null, "Employment data updated successfully", null);                        
                    } else {
                        ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            } else {

            }

        }*/
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

