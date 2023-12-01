/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.training;

import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import dto.setup.dto.training.StaffFurtherStudyApplicationDTO;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import service.employees.EmployeeService;
import service.training.StaffFurtherStudyApplicationService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.TrainingValidator;

/**
 *
 * @author IronHide
 */
public class PersonnelTrainingApplicationMBean implements Serializable {
    
    private EmployeeDTO recordDto;
    private StaffFurtherStudyApplicationDTO newRecordDto, editRecordDto, approveRecordDto;
    private String searchFileNo;
    
    private final EmployeeService employeeService;
    private final StaffFurtherStudyApplicationService service;
    private final TrainingValidator validator;
    private final ViewHelper viewHelper;
    
    private final FetchOptionsDTO fetchOptions;
    
    private boolean searchStarted;
    
    public PersonnelTrainingApplicationMBean() {
        service = new StaffFurtherStudyApplicationService();
        employeeService = new EmployeeService();
        validator = new TrainingValidator();
        viewHelper = new ViewHelper();
        
        fetchOptions = new FetchOptionsDTO();
        fetchOptions.setFetchFurtherTraining(true);
        
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
    
    public void handleAnyCountryChange(){
        
    }

    //************START NEW****************
    public void prepareNewDialog() {
        newRecordDto = new StaffFurtherStudyApplicationDTO();
        newRecordDto.setEmployeeRecordId(recordDto.getRecordId());
        newRecordDto.setEmployeeFileNo(recordDto.getFileNo());
        
        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("newDialogPanel");
    }
    
    public void closeNewConfirmDialog() {
        newRecordDto = new StaffFurtherStudyApplicationDTO();
        newRecordDto.setEmployeeRecordId(recordDto.getRecordId());
        newRecordDto.setEmployeeFileNo(recordDto.getFileNo());
        
        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("newDialogPanel");
    }
    
    public void preSubmitNewAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateStaffFurtherStudy(context, newRecordDto, false);
        
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
        boolean validation_outcome = validator.validateStaffFurtherStudy(context, newRecordDto, false);
        
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
                
                if (!StringUtils.equals(newRecordDto.getCountry(), AppConstants.NIGERIA)) {
                    newRecordDto.setState(newRecordDto.getLocation());
                }                
                
                newRecordDto.setApprovalStatus(AppConstants.TRAINING_PENDING_APPROVAL);
                newRecordDto.setRecordStatus(AppConstants.ACTIVE);
                
                boolean result = service.createStaffFurtherStudyApplication(newRecordDto);
                
                if (result) {
                    loadRecord(recordDto.getFileNo());
                    newRecordDto = null;
                    ViewHelper.addInfoMessage(context, null, "Training application saved successfully.", null);
                } else {
                    loadRecord(recordDto.getFileNo());
                    newRecordDto = null;
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }
                
            } catch (Exception e) {
                String msg = e.getMessage();
                if (StringUtils.equals(msg, AppConstants.ACTIVE_PENDING_EXISTS)) {
                    ViewHelper.addErrorMessage(context, null, "Error: Personnel has an active training application pending approval.", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }
                
            }
        } else {
            ViewHelper.addErrorMessage(context, null, "Validation Error: entries were found invalid.", null);
        }
    }
    //************END NEW****************

    //************START EDIT****************
    public void prepareEditDialog(String appl_id) {
        if (!StringUtils.isBlank(appl_id)) {
            try {
                //System.out.println("appl id" + edit_leave_appl_id);
                editRecordDto = service.getStaffFurtherStudyApplication(appl_id);
                
                if (editRecordDto != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    editRecordDto.setStartDate(dateFormat.parse(editRecordDto.getExpectedStartDate()));
                    editRecordDto.setEndDate(dateFormat.parse(editRecordDto.getExpectedEndDate()));
                }
                
                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("editDialogPanel");
            } catch (Exception exc) {
                
            }
        }
    }
    
    public void closeEditConfirmDialog() {
        editRecordDto = new StaffFurtherStudyApplicationDTO();
        editRecordDto.setEmployeeRecordId(recordDto.getRecordId());
        editRecordDto.setEmployeeFileNo(recordDto.getFileNo());
        //reasonsMap = new LinkedHashMap<>();

        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("editDialogPanel");
    }
    
    public void preSubmitEditAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateStaffFurtherStudy(context, editRecordDto, false);
        
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
        boolean validation_outcome = validator.validateStaffFurtherStudy(context, editRecordDto, false);
        
        if (validation_outcome) {
            try {
                
                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();
                
                editRecordDto.setLastMod(activityDate);
                editRecordDto.setLastModBy(currentUser);
                
                if (!StringUtils.equals(editRecordDto.getCountry(), AppConstants.NIGERIA)) {
                    editRecordDto.setState(editRecordDto.getLocation());
                }
                
                editRecordDto.setExpectedStartDate(viewHelper.getDateAsDMYWithSlashString(editRecordDto.getStartDate()));
                editRecordDto.setExpectedEndDate(viewHelper.getDateAsDMYWithSlashString(editRecordDto.getEndDate()));
                
                boolean result = service.updateStaffFurtherStudyApplication(editRecordDto);
                
                if (result) {
                    loadRecord(recordDto.getFileNo());
                    editRecordDto = null;
                    ViewHelper.addInfoMessage(context, null, "Training application saved successfully.", null);
                } else {
                    loadRecord(recordDto.getFileNo());
                    editRecordDto = null;
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }
                
            } catch (Exception e) {
                String msg = e.getMessage();
                if (StringUtils.equals(msg, AppConstants.ACTIVE_PENDING_EXISTS)) {
                    ViewHelper.addErrorMessage(context, null, "Error: Personnel has an active training application pending approval.", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }
                
            }
        } else {
            ViewHelper.addErrorMessage(context, null, "Validation Error: entries were found invalid.", null);
        }
    }
    //************END EDIT****************

    //************START APPROVAL****************
    public void prepareApproveDialog(String appl_id) {
        if (!StringUtils.isBlank(appl_id)) {
            try {
                approveRecordDto = service.getStaffFurtherStudyApplication(appl_id);
                
                if (approveRecordDto != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    approveRecordDto.setStartDate(dateFormat.parse(approveRecordDto.getExpectedStartDate()));
                    approveRecordDto.setEndDate(dateFormat.parse(approveRecordDto.getExpectedEndDate()));
                }
                
                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("approveDialogPanel");
            } catch (Exception exc) {
            }
        }
    }
    
    public void closeApproveConfirmDialog() {
        approveRecordDto = new StaffFurtherStudyApplicationDTO();
        approveRecordDto.setEmployeeRecordId(recordDto.getRecordId());
        approveRecordDto.setEmployeeFileNo(recordDto.getFileNo());
        //reasonsMap = new LinkedHashMap<>();

        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("approveDialogPanel");
    }
    
    public void preSubmitApproveAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateStaffFurtherStudy(context, approveRecordDto, true);
        
        RequestContext reqContext = RequestContext.getCurrentInstance();
        
        if (validation_outcome) {
            //reqContext.execute("PF('newDialog').hide()");

            reqContext.update("submitApproveDialogPanel");
            reqContext.execute("PF('submitApproveDialog').show()");
        } else {
            reqContext.update("approveDialogPanel");
        }
    }
    
    public void approveAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateStaffFurtherStudy(context, approveRecordDto, true);
        
        if (validation_outcome) {
            try {
                
                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();
                
                approveRecordDto.setLastMod(activityDate);
                approveRecordDto.setLastModBy(currentUser);
                
                boolean result = service.approveStaffFurtherStudyApplication(approveRecordDto);
                
                if (result) {
                    loadRecord(recordDto.getFileNo());
                    approveRecordDto = null;
                    ViewHelper.addInfoMessage(context, null, "Training application updated successfully.", null);
                } else {
                    loadRecord(recordDto.getFileNo());
                    approveRecordDto = null;
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }
                
            } catch (Exception e) {
                ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
            }
        }
    }

    //************END APPROVAL****************
    public EmployeeDTO getRecordDto() {
        if (recordDto == null) {
            recordDto = new EmployeeDTO();
        }
        return recordDto;
    }
    
    public void setRecordDto(EmployeeDTO recordDto) {
        this.recordDto = recordDto;
    }
    
    public StaffFurtherStudyApplicationDTO getNewRecordDto() {
        if (newRecordDto == null) {
            newRecordDto = new StaffFurtherStudyApplicationDTO();
        }
        return newRecordDto;
    }
    
    public void setNewRecordDto(StaffFurtherStudyApplicationDTO newRecordDto) {
        this.newRecordDto = newRecordDto;
    }
    
    public StaffFurtherStudyApplicationDTO getEditRecordDto() {
        if (editRecordDto == null) {
            editRecordDto = new StaffFurtherStudyApplicationDTO();
        }
        return editRecordDto;
    }
    
    public void setEditRecordDto(StaffFurtherStudyApplicationDTO editRecordDto) {
        this.editRecordDto = editRecordDto;
    }
    
    public StaffFurtherStudyApplicationDTO getApproveRecordDto() {
        if (approveRecordDto == null) {
            approveRecordDto = new StaffFurtherStudyApplicationDTO();
        }
        return approveRecordDto;
    }
    
    public void setApproveRecordDto(StaffFurtherStudyApplicationDTO approveRecordDto) {
        this.approveRecordDto = approveRecordDto;
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
