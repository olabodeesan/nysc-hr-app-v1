/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.registry;

import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import dto.setup.dto.registry.RegistryDocumentDTO;
import java.io.Serializable;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.FacesLocal;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import service.employees.EmployeeService;
import service.registry.RegistryDocumentService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.RegistryValidator;

/**
 *
 * @author IronHide
 */
public class PersonnelDocumentsMBean implements Serializable {

    private EmployeeDTO recordDto;
    private RegistryDocumentDTO newRecordDto, editRecordDto;
    private String searchFileNo;

    private final EmployeeService employeeService;
    private final RegistryDocumentService service;
    private final ViewHelper viewHelper;

    private UploadedFile file;

    private final FetchOptionsDTO fetchOptions;

    private boolean searchStarted;

    public PersonnelDocumentsMBean() {
        service = new RegistryDocumentService();
        employeeService = new EmployeeService();
        viewHelper = new ViewHelper();

        fetchOptions = new FetchOptionsDTO();
        fetchOptions.setFetchDocs(true);

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
    public void handleFileUpload() {
        if (file != null && newRecordDto != null && !StringUtils.isBlank(newRecordDto.getFileNameNoExt())) {
            try {
                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();

                newRecordDto.setCreated(activityDate);
                newRecordDto.setCreatedBy(currentUser);
                newRecordDto.setLastMod(activityDate);
                newRecordDto.setLastModBy(currentUser);

                newRecordDto.setEmployeeRecordId(recordDto.getRecordId());
                newRecordDto.setFileNameWithExt(newRecordDto.getFileNameNoExt() + ".jpg");

                newRecordDto.setRecordStatus(AppConstants.ACTIVE);

                boolean result = service.uploadRegistryDocFile(file, newRecordDto);
                if (result) {
                    loadRecord(recordDto.getFileNo());
                    Messages.addInfo("upload_msg", newRecordDto.getFileNameNoExt() + " Uploaded Successfully");
                    newRecordDto = null;
                } else {
                    Messages.addError("upload_msg", "An error occurred while uploading document. Try again.");
                }
            } catch (Exception exc) {
                String msg = exc.getMessage();
                if (StringUtils.equals(msg, AppConstants.DUPLICATE)) {
                    Messages.addError("upload_msg", "Duplicate file name found. Try again.");
                } else {
                    Messages.addError("upload_msg", "An error occurred while uploading document. Try again.");
                }
            }
        } else {
            Messages.addError("upload_msg", "Both File and Desc Required");
        }

    }

    /*public void createAction(ActionEvent event) {
     FacesContext context = FacesContext.getCurrentInstance();
     boolean validation_outcome = validator.validateStaffLeaveApplication(context, newRecordDto, false);//since u set status them bellow

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

     newRecordDto.setApprovalStatus(AppConstants.LEAVE_PENDING_APPROVAL);
     newRecordDto.setRecordStatus(AppConstants.ACTIVE);

     boolean result = service.createStaffLeaveApplication(newRecordDto);

     if (result) {
     loadRecord(recordDto.getFileNo());
     newRecordDto = null;
     ViewHelper.addInfoMessage(context, null, "Leave application saved successfully. PENDING APPROVAL.", null);
     } else {
     loadRecord(recordDto.getFileNo());
     newRecordDto = null;
     ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
     }

     } catch (Exception e) {
     String msg = e.getMessage();
     if (StringUtils.equals(msg, AppConstants.LEAVE_DAYS_TOO_MUCH)) {
     ViewHelper.addErrorMessage(context, null, "Error: Cannot allocate these number of days", null);
     } else if (StringUtils.equals(msg, AppConstants.NO_ANNUAL_LEAVE_ROSTER)) {
     ViewHelper.addErrorMessage(context, null, "Error: No leave roster of current year found for personnel", null);
     } else if (StringUtils.equals(msg, AppConstants.ACTIVE_PENDING_EXISTS)) {
     ViewHelper.addErrorMessage(context, null, "Error: Cannot apply, a pending application exists.", null);
     } else {
     ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
     }

     }
     }
     }*/
    //************END NEW****************
    //************START EDIT****************
    /*public void prepareEditDialog(String edit_leave_appl_id) {
     if (!StringUtils.isBlank(edit_leave_appl_id)) {
     try {
     //System.out.println("appl id" + edit_leave_appl_id);
     editRecordDto = service.getStaffLeaveApplication(edit_leave_appl_id);

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

     public void handleEditStartDateChange() {
     Date startAnnualLeave = editRecordDto.getStartDate();
     if (startAnnualLeave != null) {
     int duration = NumberUtils.toInt(editRecordDto.getDuration());
     editRecordDto.setEndDate(viewHelper.addBusinessDays(startAnnualLeave, duration));
     }
     }

     public void closeEditConfirmDialog() {
     editRecordDto = new StaffLeaveApplicationDTO();
     editRecordDto.setEmployeeRecordId(recordDto.getRecordId());
     editRecordDto.setEmployeeFileNo(recordDto.getFileNo());
     //reasonsMap = new LinkedHashMap<>();

     RequestContext reqContext = RequestContext.getCurrentInstance();
     reqContext.update("editDialogPanel");
     }

     public void preSubmitEditAction(ActionEvent event) {
     FacesContext context = FacesContext.getCurrentInstance();
     boolean validation_outcome = validator.validateStaffLeaveApplication(context, editRecordDto, true);

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
     boolean validation_outcome = validator.validateStaffLeaveApplication(context, editRecordDto, true);

     if (validation_outcome) {
     try {

     String activityDate = DateTimeUtils.getActivityDate();
     String currentUser = viewHelper.getAppUser().getUsername();

     editRecordDto.setLastMod(activityDate);
     editRecordDto.setLastModBy(currentUser);

     editRecordDto.setExpectedStartDate(viewHelper.getDateAsDMYWithSlashString(editRecordDto.getStartDate()));
     editRecordDto.setExpectedEndDate(viewHelper.getDateAsDMYWithSlashString(editRecordDto.getEndDate()));

     boolean result = service.updateStaffLeaveApplication(editRecordDto);

     if (result) {
     loadRecord(recordDto.getFileNo());
     editRecordDto = null;
     ViewHelper.addInfoMessage(context, null, "Leave application saved successfully.", null);
     } else {
     loadRecord(recordDto.getFileNo());
     editRecordDto = null;
     ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
     }

     } catch (Exception e) {
     String msg = e.getMessage();
     if (StringUtils.equals(msg, AppConstants.LEAVE_DAYS_TOO_MUCH)) {
     ViewHelper.addErrorMessage(context, null, "Error: Cannot allocate these number of days", null);
     } else if (StringUtils.equals(msg, AppConstants.NO_ANNUAL_LEAVE_ROSTER)) {
     ViewHelper.addErrorMessage(context, null, "Error: No leave roster of current year found for personnel", null);
     } else {
     ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
     }

     }
     }
     }*/
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

    public RegistryDocumentDTO getNewRecordDto() {
        if (newRecordDto == null) {
            newRecordDto = new RegistryDocumentDTO();
        }
        return newRecordDto;
    }

    public void setNewRecordDto(RegistryDocumentDTO newRecordDto) {
        this.newRecordDto = newRecordDto;
    }

    public RegistryDocumentDTO getEditRecordDto() {
        if (editRecordDto == null) {
            editRecordDto = new RegistryDocumentDTO();
        }
        return editRecordDto;
    }

    public void setEditRecordDto(RegistryDocumentDTO editRecordDto) {
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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
