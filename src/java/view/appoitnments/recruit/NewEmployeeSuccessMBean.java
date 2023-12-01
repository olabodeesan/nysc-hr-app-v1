/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.appoitnments.recruit;

import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.FacesLocal;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import service.employees.EmployeeService;
import service.fileupload.FileUploadService;
import view.utils.AppConstants;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class NewEmployeeSuccessMBean implements Serializable {

    private EmployeeDTO recordDto;
    private String recordId;

    private EmployeeService service;
    private FileUploadService fileUploadService;
    private ViewHelper viewHelper;

    public NewEmployeeSuccessMBean() {
        service = new EmployeeService();
        fileUploadService = new FileUploadService();
        viewHelper = new ViewHelper();

        loadRecord();
    }

    private void loadRecord() {
        FacesContext context = FacesContext.getCurrentInstance();
        recordId = FacesLocal.getRequestParameter(context, "vhsuYagsh62fshkfd");
        System.out.println("Employee Record Id is" + recordId);

        if (!StringUtils.isBlank(recordId)) {
            FetchOptionsDTO fetchOptionsDTO=new FetchOptionsDTO();
            fetchOptionsDTO.setFetchAll(true);
            try {
              //  recordDto = service.getEmployeee(recordId, null, AppConstants.FETCH_MODE_RECORD_ID, null);
           //     recordDto = service.getEmployeee(recordId, null, AppConstants.FETCH_MODE_RECORD_ID, fetchOptionsDTO);
           recordDto = service.getEmployeee(recordId, AppConstants.FETCH_MODE_RECORD_ID, null, fetchOptionsDTO);
            } catch (Exception exc) {
                recordDto = null;
                System.out.println(exc.getMessage());
            }
        }else{
            recordDto = null;
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance();
        try {
            UploadedFile uploadedfile = event.getFile();

            boolean result = fileUploadService.uploadPersonnelPhoto(uploadedfile, recordDto.getRecordId());
            if (result) {
                FacesLocal.redirect(context, "appointment-recruitment/new-personnel-success.jsf?vyTasGsK=%s&vhsuYagsh62fshkfd=%s&8uss23kj=%s",
                        "sDhsf6dfasugf7374ugsydgfaksgfysgf", recordDto.getRecordId(), "ashdjahsd75678sdk");
                //reqContext.addCallbackParam("photouploadresult", true);
                //ViewHelper.addInfoMessage(context, null, "Photograph uploaded successfully. You may need to click the reresh button to see update", null);
            } else {
                reqContext.addCallbackParam("photouploadresult", false);
                ViewHelper.addErrorMessage(context, null, "An error occurred while updating personnel photo. Try again.", null);
            }
        } catch (Exception exc) {
            reqContext.addCallbackParam("photouploadresult", false);
            ViewHelper.addErrorMessage(context, null, "An error occurred while updating personnel photo. Try again.", null);
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

}
