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
public class SuspendPersonnelMBean implements Serializable{
    
    private EmployeeDTO recordDto;
    private EmployeeStatusDTO newStatusDto;
    private String searchFileNo;

    private EmployeeService employeeService;
    private AppointmentRecruitService apptService;
    private ApplicationCodesService codeService;
    private AppointmentRecruitValidator validator;
    private ViewHelper viewHelper;

    private FetchOptionsDTO fetchOptions;

    private Map<String, String> reasonsMap;
    
    private boolean searchStarted;

    public SuspendPersonnelMBean() {
        apptService = new AppointmentRecruitService();
        employeeService = new EmployeeService();
        codeService = new ApplicationCodesService();
        validator = new AppointmentRecruitValidator();
        viewHelper = new ViewHelper();

        fetchOptions = new FetchOptionsDTO();
        fetchOptions.setFetchStatus(true);
        
        //pre-load the options since the mode is already known
        fetchReasons(AppConstants.PERSONNEL_SUSPENDED);
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

    /*public void handleStatusChange() {
        fetchReasons(newStatusDto.getStatusId());
    }*/

    private void fetchReasons(String status_id) {
        try {
            reasonsMap = codeService.getEmployeeStatusReasons(status_id);
            //System.out.println("got here: " + reasonsMap);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchAction(ActionEvent event) {
        searchStarted = true;
        loadRecord(searchFileNo);
    }
    
    public void prepareNewSuspendDialog() {
        newStatusDto = new EmployeeStatusDTO();
        newStatusDto.setEmployeeRecordId(recordDto.getRecordId());
        newStatusDto.setEmployeeFileNo(recordDto.getFileNo());
        newStatusDto.setStatusId(AppConstants.PERSONNEL_SUSPENDED);
        //reasonsMap = new LinkedHashMap<>();
        
        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("suspendDialogPanel");
    }
    
    public void closeSuspendConfirmDialog() {
        newStatusDto = new EmployeeStatusDTO();
        newStatusDto.setEmployeeRecordId(recordDto.getRecordId());
        //reasonsMap = new LinkedHashMap<>();
        
        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("suspendDialogPanel");
    }
    
    public void submitSuspendAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateSuspendPersonnel(context, newStatusDto, getReasonsMap().isEmpty());

        RequestContext reqContext = RequestContext.getCurrentInstance();
        
        if (validation_outcome) {
            reqContext.execute("PF('suspendDialog').hide()");
            
            reqContext.update("submitSuspendDialogPanel");
            reqContext.execute("PF('submitSuspendDialog').show()");
        }else{
            reqContext.update("suspendDialogPanel");
        }
    }

    public void suspendAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateSuspendPersonnel(context, newStatusDto, getReasonsMap().isEmpty());

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
                    ViewHelper.addInfoMessage(context, null, "Personnel Appointment Suspended Successfully.", null);                    
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

    public Map<String, String> getReasonsMap() {
        if (reasonsMap == null) {
            reasonsMap = new LinkedHashMap<>();
        }
        return reasonsMap;
    }

    public void setReasonsMap(Map<String, String> reasonsMap) {
        this.reasonsMap = reasonsMap;
    }

    public boolean isSearchStarted() {
        return searchStarted;
    }

    public void setSearchStarted(boolean searchStarted) {
        this.searchStarted = searchStarted;
    }

}
