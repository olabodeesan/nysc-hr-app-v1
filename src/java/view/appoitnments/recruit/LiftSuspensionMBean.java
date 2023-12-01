/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.appoitnments.recruit;

import dto.appointment.recruit.EmployeeStatusDTO;
import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import service.appointment.recruit.AppointmentRecruitService;
import service.employees.EmployeeService;
import service.utils.ApplicationCodesService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.AppointmentRecruitValidator;

/**
 *
 * @author IronHide
 */
public class LiftSuspensionMBean implements Serializable{

    private EmployeeDTO recordDto;
    private EmployeeStatusDTO newStatusDto;
    private String searchFileNo;

    private EmployeeService employeeService;
    private AppointmentRecruitService apptService;
    private ApplicationCodesService codeService;
    private AppointmentRecruitValidator validator;
    private ViewHelper viewHelper;

    private FetchOptionsDTO fetchOptions;
    
    private boolean searchStarted;

    public LiftSuspensionMBean() {
        apptService = new AppointmentRecruitService();
        employeeService = new EmployeeService();
        codeService = new ApplicationCodesService();
        validator = new AppointmentRecruitValidator();
        viewHelper = new ViewHelper();

        fetchOptions = new FetchOptionsDTO();
        fetchOptions.setFetchStatus(true);
        
    }

    private void loadRecord(String file_no) {
        if (!StringUtils.isBlank(file_no)) {
            try {
                recordDto = employeeService.getEmployeee(file_no, AppConstants.FETCH_MODE_FILE_NO, AppConstants.FETCH_VIEW_ONLY, fetchOptions);
            } catch (Exception exc) {
                recordDto = null;
                System.out.println(exc.getMessage());
            }
        }else{
            recordDto = null;
        }
    }    

    public void searchAction(ActionEvent event) {
        searchStarted = true;
        loadRecord(searchFileNo);
    }
    
    public void prepareNewLiftSuspensionDialog() {
        newStatusDto = new EmployeeStatusDTO();
        newStatusDto.setEmployeeRecordId(recordDto.getRecordId());
        newStatusDto.setEmployeeFileNo(recordDto.getFileNo());
        newStatusDto.setStatusId(AppConstants.PERSONNEL_ACTIVE);
        
        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("liftSuspensionDialogPanel");
    }
    
    public void closeLiftSuspensionConfirmDialog() {
        newStatusDto = new EmployeeStatusDTO();
        newStatusDto.setEmployeeRecordId(recordDto.getRecordId());
        
        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("liftSuspensionDialogPanel");
    }
    
    public void submitLiftSuspensionAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateLiftSuspension(context, newStatusDto);

        RequestContext reqContext = RequestContext.getCurrentInstance();
        
        if (validation_outcome) {
            reqContext.execute("PF('liftSuspensionDialog').hide()");
            
            reqContext.update("submitLiftSuspensionDialogPanel");
            reqContext.execute("PF('submitLiftSuspensionDialog').show()");
        }else{
            reqContext.update("liftSuspensionDialogPanel");
        }
    }

    public void liftSuspensionAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateLiftSuspension(context, newStatusDto);

        if (validation_outcome) {            
            try {

                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();

                newStatusDto.setCreated(activityDate);
                newStatusDto.setCreatedBy(currentUser);
                newStatusDto.setLastMod(activityDate);
                newStatusDto.setLastModBy(currentUser);

                boolean result = apptService.updateEmployeePresentStatus(newStatusDto);

                if (result) {
                    loadRecord(recordDto.getFileNo());
                    newStatusDto = null;
                    ViewHelper.addInfoMessage(context, null, "Personnel Suspension Lifted Successfully.", null);                    
                } else {
                    loadRecord(recordDto.getFileNo());
                    newStatusDto = null;
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }

            } catch (Exception e) {
                ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
            }
        }else{
            ViewHelper.addErrorMessage(context, null, "Validation Error: entries were found invalid.", null);
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

    public EmployeeStatusDTO getNewStatusDto() {
        if (newStatusDto == null) {
            newStatusDto = new EmployeeStatusDTO();
        }
        return newStatusDto;
    }

    public void setNewStatusDto(EmployeeStatusDTO newStatusDto) {
        this.newStatusDto = newStatusDto;
    }

    public String getSearchFileNo() {
        return searchFileNo;
    }

    public void setSearchFileNo(String searchFileNo) {
        this.searchFileNo = searchFileNo;
    }

    public boolean isSearchStarted() {
        return searchStarted;
    }

    public void setSearchStarted(boolean searchStarted) {
        this.searchStarted = searchStarted;
    }

}
