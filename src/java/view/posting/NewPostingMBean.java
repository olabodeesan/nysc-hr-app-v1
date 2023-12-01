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
import org.primefaces.context.RequestContext;
import service.employees.EmployeeService;
import service.posting.PostingService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.PostingValidator;

/**
 *
 * @author Gainsolutions
 */
public class NewPostingMBean implements Serializable {

    private EmployeeDTO recordDto;
    private EmployeeLocationDTO newLocation;
    private EmployeeLocationDTO editLocation;
    private String employeeRecordId;
    private final EmployeeService employeeService;
    private boolean isSpecialAppt;
    private final PostingService service;
    private final ViewHelper viewHelper;
    private String searchFileNo;
    private boolean searchStarted;
    PostingValidator validator;
    //Esan
    private final FetchOptionsDTO fetchOptions;

    public NewPostingMBean() {
        service = new PostingService();
        employeeService = new EmployeeService();
        viewHelper = new ViewHelper();
        validator = new PostingValidator();

        fetchOptions = new FetchOptionsDTO();
        fetchOptions.setFetchLocations(true);
    }

    private void loadRecord(String file_no) {
        if (!StringUtils.isBlank(file_no)) {
            try {
                recordDto = employeeService.getEmployeee(file_no, AppConstants.FETCH_MODE_FILE_NO, AppConstants.FETCH_VIEW_ONLY, fetchOptions);
            } catch (Exception exc) {
                recordDto = null;
                System.out.println(exc.getMessage());
            }
        } else {
            recordDto = null;
        }
    }

    public void handleSpecialAppointment(String isSpecialAppointment) {
        isSpecialAppt = StringUtils.equals(isSpecialAppointment, "Y");
    }

    public void searchAction(ActionEvent event) {
        searchStarted = true;
        loadRecord(searchFileNo);
    }

    public void prepareNewPostingDialog() {
        newLocation = new EmployeeLocationDTO();
        newLocation.setEmployeeRecordId(recordDto.getRecordId());
        newLocation.setEmployeeFileNo(recordDto.getFileNo());
        newLocation.setGradeLevel(recordDto.getPresentGradeLevel());
        newLocation.setLocationFrom(recordDto.getPresentLocation());

        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("newDialogPanel");
    }

    public void closeNewConfirmDialog() {
        newLocation = new EmployeeLocationDTO();
        newLocation.setEmployeeRecordId(recordDto.getRecordId());
        //reasonsMap = new LinkedHashMap<>();

        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("newDialogPanel");
    }

    public void preSubmitNewAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        boolean validation_outcome = validator.validatePosting(context, newLocation);

        RequestContext reqContext = RequestContext.getCurrentInstance();

        if (validation_outcome) {
            reqContext.update("submitNewDialogPanel");
            reqContext.execute("PF('submitNewDialog').show()");
        } else {
            reqContext.update("newDialogPanel");
        }
    }

    public void createAction(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance();

        boolean validation_outcome = validator.validatePosting(context, newLocation);

        if (validation_outcome) {
            String activityDate = DateTimeUtils.getActivityDate();
            String currentUser = viewHelper.getAppUser().getUsername();
            newLocation.setIsSecondment("N");
            newLocation.setCreated(activityDate);
            newLocation.setCreatedBy(currentUser);
            newLocation.setLastMod(activityDate);
            newLocation.setLastModBy(currentUser);
            try {
                boolean result = service.createEmployeePosting(newLocation);
                if (result) {
                    ViewHelper.addInfoMessage(context, null, "Posting to New Location successful.", null);
                    newLocation = null;
                    loadRecord(searchFileNo);
                } else {
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

    public void preSubmitEditAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        boolean validation_outcome = validator.validatePosting(context, editLocation);

        RequestContext reqContext = RequestContext.getCurrentInstance();

        if (validation_outcome) {
            reqContext.update("submitEditDialogPanel");
            reqContext.execute("PF('submitEditDialog').show()");
        } else {
            reqContext.update("editDialogPanel");
        }
    }

    public void closeEditConfirmDialog() {
        if (editLocation != null) {
            editLocation = null;
        }
        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("editDialogPanel");
    }

    public void updateAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance();
        PostingValidator validate = new PostingValidator();

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
                    loadRecord(searchFileNo);
                } else {
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

    public void preparePostingUpdateByLink(String row_record_id) {
        if (!StringUtils.isBlank(row_record_id)) {
            try {
                editLocation = service.getEmployeePosting(row_record_id);
                
               editLocation.setEmployeeFileNo(recordDto.getFileNo());
                editLocation.setGradeLevel(recordDto.getPresentGradeLevel());
                editLocation.setLocationFrom(recordDto.getPresentLocation()); 

                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("editDialogPanel");
            } catch (Exception exc) {
         
            }
        } else {
            editLocation = null;
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

    public EmployeeLocationDTO getNewLocation() {
        if (newLocation == null) {
            newLocation = new EmployeeLocationDTO();
        }
        return newLocation;
    }

    public void setNewLocation(EmployeeLocationDTO newLocation) {
        this.newLocation = newLocation;
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

    public String getSearchFileNo() {
        return searchFileNo;
    }

    public void setSearchFileNo(String searchFileNo) {
        this.searchFileNo = searchFileNo;
    }

    public String getEmployeeRecordId() {
        return employeeRecordId;
    }

    public void setEmployeeRecordId(String employeeRecordId) {
        this.employeeRecordId = employeeRecordId;
    }

    public boolean isSearchStarted() {
        return searchStarted;
    }

    public void setSearchStarted(boolean searchStarted) {
        this.searchStarted = searchStarted;
    }

}
