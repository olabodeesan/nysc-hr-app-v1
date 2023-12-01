/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.employees;

import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import dto.employees.NextOfKinDTO;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.FacesLocal;
import org.primefaces.context.RequestContext;
import service.employees.EmployeeService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.EmployeeValidator;

/**
 *
 * @author IronHide
 */
public class UpdateEmployeeNofkinMBean implements Serializable {

    private EmployeeDTO recordDto;
    private NextOfKinDTO editNextOfKin;
    private String recordId;
    private boolean inEditMode;

    private EmployeeService service;
    private EmployeeValidator validator;
    private ViewHelper viewHelper;
    
    private FetchOptionsDTO fetchOptions;

    public UpdateEmployeeNofkinMBean() {
        service = new EmployeeService();
        validator = new EmployeeValidator();
        viewHelper = new ViewHelper();
        
        fetchOptions = new FetchOptionsDTO();
        fetchOptions.setFetchNextOfKin(true);

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
            recordDto = service.getEmployeee(employee_id, AppConstants.FETCH_MODE_RECORD_ID, AppConstants.FETCH_VIEW_UPDATE,fetchOptions);
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

    public void prepareNofkinUpdateByLink(String row_record_id) {
        if (!StringUtils.isBlank(row_record_id)) {
            try {
                editNextOfKin = service.getEmployeeNofkin(row_record_id);

                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("editNokinDialogPanel");
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
            }
        } else {
            editNextOfKin = null;
        }
    }

    public void updateNofkin(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        RequestContext reqContext = RequestContext.getCurrentInstance();
        boolean validation_outcome = validator.validateNextOfKin(context, editNextOfKin);
        if (validation_outcome) {
            String activityDate = DateTimeUtils.getActivityDate();
            String currentUser = viewHelper.getAppUser().getUsername();

            editNextOfKin.setLastMod(activityDate);
            editNextOfKin.setLastModBy(currentUser);

            try {
                boolean result = service.updateEmployeeNofkin(editNextOfKin);
                if (result) {
                    ViewHelper.addInfoMessage(context, null, "Record Updated Successfully. Close this dialog", null);
                    editNextOfKin = null;

                    reqContext.addCallbackParam("editnofkinresult", true);

                    loadRecord();
                } else {
                    reqContext.addCallbackParam("editnofkinresult", false);
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
                reqContext.addCallbackParam("editnofkinresult", false);
                ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
            }
        } else {
            reqContext.addCallbackParam("editnofkinresult", false);
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

    public NextOfKinDTO getEditNextOfKin() {
        if (editNextOfKin == null) {
            editNextOfKin = new NextOfKinDTO();
        }
        return editNextOfKin;
    }

    public void setEditNextOfKin(NextOfKinDTO editNextOfKin) {
        this.editNextOfKin = editNextOfKin;
    }
}
