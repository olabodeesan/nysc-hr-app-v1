/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.posting;

import dto.employees.EmployeeDTO;
import dto.employees.EmployeeLocationDTO;
import dto.employees.FetchOptionsDTO;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.FacesLocal;
import org.primefaces.context.RequestContext;
import service.employees.EmployeeService;
import service.posting.PostingService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.EmployeeValidator;
import view.validators.PostingValidator;

/**
 *
 * @author Gainsolutions
 */
public class UpdatePostingMBean implements Serializable {

    private EmployeeDTO recordDto;
    private EmployeeLocationDTO editLocation;
    private String recordId;

    private String employeeRecordId;
    private String gradeLvl;
    private String locationFrom;

    private boolean inEditMode;
    private boolean isSpecialAppt;

    private PostingService service;
    private EmployeeService employeeService;
    
    private EmployeeValidator validator;
    private ViewHelper viewHelper;
    
    //Esan
      private final FetchOptionsDTO fetchOptions;

    public UpdatePostingMBean() {
        employeeService=new EmployeeService();        
        service = new PostingService();
        validator = new EmployeeValidator();
        
        viewHelper = new ViewHelper();

        inEditMode = false;
        
        fetchOptions=new FetchOptionsDTO();
        fetchOptions.setFetchLocations(true);
        
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

    public void handleSpecialAppointment(String isSpecialAppointment) {
        if (StringUtils.equals(isSpecialAppointment, "Y")) {
            isSpecialAppt = true;
        } else {
            isSpecialAppt = false;
        }
    }

    public void preparePostingUpdateByLink(String row_record_id) {
        if (!StringUtils.isBlank(row_record_id)) {
            try {
                editLocation = service.getEmployeePosting(row_record_id);
                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("editPostingDialogPanel");
            } catch (Exception exc) {
               
            }
        } else {
            editLocation = null;
        }
    }

    private void fetchRecord(String employee_id) {
        try {
            
            recordDto = employeeService.getEmployeee(employee_id, AppConstants.FETCH_MODE_RECORD_ID, AppConstants.FETCH_VIEW_UPDATE,fetchOptions ); 
            if (recordDto != null) {
                gradeLvl = recordDto.getPresentGradeLevel();
            }
        } catch (Exception exc) {
            inEditMode = false;
        }
    }

    public void enableEditMode() {
        inEditMode = true;
    }

    public void cancelEditMode() {
        inEditMode = false;
    }

    public void updateAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance();
        PostingValidator validate = new PostingValidator();

        editLocation.setGradeLevel(gradeLvl);

        boolean validation_outcome = validate.validatePosting(context, editLocation);

        if (validation_outcome) {

            String activityDate = DateTimeUtils.getActivityDate();
            String currentUser = viewHelper.getAppUser().getUsername();

            editLocation.setLastMod(activityDate);
            editLocation.setLastModBy(currentUser);

            try {
                boolean result = service.updateEmployeePosting(editLocation);
                if (result) {
                    ViewHelper.addInfoMessage(context, null, "Posting Update successful.", null);
                    editLocation = null;
                    reqContext.addCallbackParam("editposting", true);
                    loadRecord();
                } else {
                    reqContext.addCallbackParam("editposting", false);
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            } catch (Exception exc) {

                String msg = exc.getMessage();
                if (StringUtils.equals(msg, AppConstants.LOCATION_ERROR)) {
                    ViewHelper.addErrorMessage(context, null, "Error: You cannot Post to same location.", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            }
        }
    }

    public EmployeeDTO getRecordDto() {
        if(recordDto==null){
            recordDto=new EmployeeDTO();
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

    public EmployeeLocationDTO getEditLocation() {
        if (editLocation == null) {
            editLocation = new EmployeeLocationDTO();
        }
        return editLocation;
    }

    public void setEditLocation(EmployeeLocationDTO editLocation) {
        this.editLocation = editLocation;
    }

    public boolean isIsSpecialAppt() {
        return isSpecialAppt;
    }

    public void setIsSpecialAppt(boolean isSpecialAppt) {
        this.isSpecialAppt = isSpecialAppt;
    }

}
