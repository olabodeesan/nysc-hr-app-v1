/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.registry;

import dto.setup.dto.registry.FileRequestDTO;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.FacesLocal;
import service.registry.FileRequestService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.RegistryValidator;

/**
 *
 * @author IronHide
 */
public class NewFileRequestMBean implements Serializable {

    private FileRequestDTO recordDto;
    private boolean fileIsOut;

    private final FileRequestService fileRequestService;
    private final RegistryValidator validator;
    private final ViewHelper viewHelper;

    public NewFileRequestMBean() {
        fileRequestService = new FileRequestService();
        validator = new RegistryValidator();
        viewHelper = new ViewHelper();
    }

    @PostConstruct
    public void afterConstruct() {

        reInitRecord();

    }

    private void reInitRecord() {
        Calendar now = Calendar.getInstance();
        Date nowDate = now.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:m a");
        recordDto = new FileRequestDTO();

        recordDto.setRequestDate(dateFormat.format(nowDate));
        recordDto.setRequestTime(timeFormat.format(nowDate));

        fileIsOut = true;
    }

    public void checkAvailablity() {
        if (recordDto != null && !StringUtils.isBlank(recordDto.getRegistryFileNo())) {
            try {
                fileIsOut = fileRequestService.checkIfFileIsOut(recordDto.getRegistryFileNo());   
                if(fileIsOut){
                    FacesContext context = FacesContext.getCurrentInstance();
                    ViewHelper.addErrorMessage(context, null, "Sorry: This file is not available at the moment.", null);
                }
            } catch (Exception e) {
                fileIsOut = false;
            }
        } else {
            reInitRecord();
        }
    }

    public void createAction(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateFileRequest(context, recordDto, false);

        if (validation_outcome) {
            try {
                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();

                recordDto.setCreated(activityDate);
                recordDto.setCreatedBy(currentUser);
                recordDto.setLastMod(activityDate);
                recordDto.setLastModBy(currentUser);

                recordDto.setInOutStatus(AppConstants.REGISTRY_FILE_OUT);
                recordDto.setRecordStatus(AppConstants.ACTIVE);

                boolean result = fileRequestService.createFileRequest(recordDto);

                if (result) {
                    try {
                        FacesLocal.redirect(context, "registry/file-request-success.jsf?nav=registry&vyTasGsK=%s&vhsuYagsh62fshkfd=%s&8uss23kj=%s",
                                "sDhsf6dfasugf7374ugsydgfaksgfysgf", "", "ashdjahsd75678sdk");
                    } catch (Exception exc) {
                        //think of what to do here
                    }
                } else {
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }

            } catch (Exception e) {
                String msg = e.getMessage();
                if (StringUtils.equals(msg, AppConstants.REGISTRY_FILE_OUT)) {
                    ViewHelper.addErrorMessage(context, null, "Error: File is not available.", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }

            }
        }
    }

    public FileRequestDTO getRecordDto() {
        if (recordDto == null) {
            recordDto = new FileRequestDTO();
        }
        return recordDto;
    }

    public void setRecordDto(FileRequestDTO recordDto) {
        this.recordDto = recordDto;
    }

    public boolean isFileIsOut() {
        return fileIsOut;
    }

    public void setFileIsOut(boolean fileIsOut) {
        this.fileIsOut = fileIsOut;
    }

}
