/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.registry;

import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import dto.setup.dto.registry.AnnualLeaveRosterDTO;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.primefaces.context.RequestContext;
import service.employees.EmployeeService;
import service.registry.AnnualLeaveRosterService;
import view.utils.AppConstants;
import view.utils.ApplicationCodesLoader;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.RegistryValidator;

/**
 *
 * @author IronHide
 */
public class PersonnelAnnualLeaveRosterMBean implements Serializable {

    private EmployeeDTO recordDto;
    private AnnualLeaveRosterDTO newRecordDto, editRecordDto;
    private String searchFileNo;

    private final EmployeeService employeeService;
    private final AnnualLeaveRosterService service;
    private final RegistryValidator validator;
    private final ViewHelper viewHelper;

    private final FetchOptionsDTO fetchOptions;

    private boolean searchStarted;

    private Map<String, String> gradeLevelLeaveDurations;

    public PersonnelAnnualLeaveRosterMBean() {
        service = new AnnualLeaveRosterService();
        employeeService = new EmployeeService();
        validator = new RegistryValidator();
        viewHelper = new ViewHelper();

        fetchOptions = new FetchOptionsDTO();
        fetchOptions.setFetchAnnualLeave(true);

    }

    @PostConstruct
    public void afterConstruct() {
        ApplicationCodesLoader codeLoader = new ApplicationCodesLoader();
        gradeLevelLeaveDurations = new LinkedHashMap<>(codeLoader.getGradeLevelAnnualLeaveDurations());

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

    public void searchAction(ActionEvent event) {
        searchStarted = true;
        loadRecord(searchFileNo);
    }

    //************START NEW****************
    public void prepareNewDialog() {
        newRecordDto = new AnnualLeaveRosterDTO();
        newRecordDto.setEmployeeRecordId(recordDto.getRecordId());
        newRecordDto.setEmployeeFileNo(recordDto.getFileNo());

        newRecordDto.setDuration(gradeLevelLeaveDurations.get(recordDto.getPresentGradeLevel()));

        Calendar now = Calendar.getInstance();
        newRecordDto.setRosterYear(Integer.toString(now.get(Calendar.YEAR)));

        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("newDialogPanel");
    }

    public void handleNewStartDateChange() {
        Date startAnnualLeave = newRecordDto.getStartDate();
        if (startAnnualLeave != null) {
            int duration = NumberUtils.toInt(newRecordDto.getDuration());
            newRecordDto.setEndDate(viewHelper.addBusinessDays(startAnnualLeave, duration));
        }
    }

    public void closeNewConfirmDialog() {
        newRecordDto = new AnnualLeaveRosterDTO();
        newRecordDto.setEmployeeRecordId(recordDto.getRecordId());
        newRecordDto.setEmployeeFileNo(recordDto.getFileNo());
        //reasonsMap = new LinkedHashMap<>();

        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("newDialogPanel");
    }

    public void preSubmitNewAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateAnnualLeaveRoster(context, newRecordDto);

        RequestContext reqContext = RequestContext.getCurrentInstance();

        if (validation_outcome) {
            //reqContext.execute("PF('newDialog').hide()");

            reqContext.update("submitNewDialogPanel");
            reqContext.execute("PF('submitNewDialog').show()");
        } else {
            reqContext.update("newDialogPanel");
        }
    }

    public void createAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateAnnualLeaveRoster(context, newRecordDto);

        if (validation_outcome) {
            try {

                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();

                newRecordDto.setCreated(activityDate);
                newRecordDto.setCreatedBy(currentUser);
                newRecordDto.setLastMod(activityDate);
                newRecordDto.setLastModBy(currentUser);

                newRecordDto.setExpectedStartDate(viewHelper.getDateAsDMYWithSlashString(newRecordDto.getStartDate()));
                newRecordDto.setExpectedEndDate(viewHelper.getDateAsDMYWithSlashString(newRecordDto.getEndDate()));

                newRecordDto.setRemainingDays(newRecordDto.getDuration());

                boolean result = service.createAnnualLeaveRoster(newRecordDto);

                if (result) {
                    loadRecord(recordDto.getFileNo());
                    newRecordDto = null;
                    ViewHelper.addInfoMessage(context, null, "Annual leave roster saved successfully.", null);
                } else {
                    loadRecord(recordDto.getFileNo());
                    newRecordDto = null;
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }

            } catch (Exception e) {
                String msg = e.getMessage();
                if (StringUtils.equals(msg, AppConstants.DUPLICATE)) {
                    ViewHelper.addErrorMessage(context, null, "Error: Duplicate entry for roster year.", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }
            }
        }
    }
    
    //************END NEW****************

    //************START EDIT****************
    public void prepareEditDialog(String edit_annual_leave_id) {
        if (!StringUtils.isBlank(edit_annual_leave_id)) {
            try {
                editRecordDto = service.getAnnualLeaveRoster(edit_annual_leave_id);
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                editRecordDto.setStartDate(dateFormat.parse(editRecordDto.getExpectedStartDate()));
                editRecordDto.setEndDate(dateFormat.parse(editRecordDto.getExpectedEndDate()));

                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("editDialogPanel");
            } catch (Exception exc) {
            }
        }
    }
    
    public void handleEditStartDateChange() {
        Date startAnnualLeave = editRecordDto.getStartDate();
        if (startAnnualLeave != null) {
            int duration = NumberUtils.toInt(editRecordDto.getDuration());
            editRecordDto.setEndDate(viewHelper.addBusinessDays(startAnnualLeave, duration));
        }
    }

    public void closeEditConfirmDialog() {
        editRecordDto = new AnnualLeaveRosterDTO();
        editRecordDto.setEmployeeRecordId(recordDto.getRecordId());
        editRecordDto.setEmployeeFileNo(recordDto.getFileNo());
        //reasonsMap = new LinkedHashMap<>();

        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("editDialogPanel");
    }

    public void preSubmitEditAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateAnnualLeaveRoster(context, editRecordDto);

        RequestContext reqContext = RequestContext.getCurrentInstance();

        if (validation_outcome) {
            //reqContext.execute("PF('newDialog').hide()");

            reqContext.update("submitEditDialogPanel");
            reqContext.execute("PF('submitEditDialog').show()");
        } else {
            reqContext.update("editDialogPanel");
        }
    }
    
    public void updateAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateAnnualLeaveRoster(context, editRecordDto);

        if (validation_outcome) {
            try {

                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();

                editRecordDto.setLastMod(activityDate);
                editRecordDto.setLastModBy(currentUser);

                editRecordDto.setExpectedStartDate(viewHelper.getDateAsDMYWithSlashString(editRecordDto.getStartDate()));
                editRecordDto.setExpectedEndDate(viewHelper.getDateAsDMYWithSlashString(editRecordDto.getEndDate()));

                editRecordDto.setRemainingDays(editRecordDto.getDuration());

                boolean result = service.updateAnnualLeaveRoster(editRecordDto);

                if (result) {
                    loadRecord(recordDto.getFileNo());
                    editRecordDto = null;
                    ViewHelper.addInfoMessage(context, null, "Leave roster saved successfully.", null);
                } else {
                    loadRecord(recordDto.getFileNo());
                    editRecordDto = null;
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }

            } catch (Exception e) {
                String msg = e.getMessage();
                if (StringUtils.equals(msg, AppConstants.DUPLICATE)) {
                    ViewHelper.addErrorMessage(context, null, "Error: Duplicate entry for roster year.", null);
                }else if (StringUtils.equals(msg, AppConstants.ANNUAL_ROSTER_IN_USE)) {
                    ViewHelper.addErrorMessage(context, null, "Error: Roster already in use, Cannot Update.", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }
            }
        }
    }
    
    //************END EDIT****************

    public EmployeeDTO getRecordDto() {
        if (recordDto == null) {
            recordDto = new EmployeeDTO();
        }
        return recordDto;
    }

    public void setRecordDto(EmployeeDTO recordDto) {
        this.recordDto = recordDto;
    }

    public AnnualLeaveRosterDTO getNewRecordDto() {
        if (newRecordDto == null) {
            newRecordDto = new AnnualLeaveRosterDTO();
        }
        return newRecordDto;
    }

    public void setNewRecordDto(AnnualLeaveRosterDTO newRecordDto) {
        this.newRecordDto = newRecordDto;
    }

    public AnnualLeaveRosterDTO getEditRecordDto() {
        if (editRecordDto == null) {
            editRecordDto = new AnnualLeaveRosterDTO();
        }
        return editRecordDto;
    }

    public void setEditRecordDto(AnnualLeaveRosterDTO editRecordDto) {
        this.editRecordDto = editRecordDto;
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
