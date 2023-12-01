/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.discipline;

import dto.discipline.DisciplineDTO;
import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import service.discipline.DisciplineService;
import service.employees.EmployeeService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.DisciplineValidator;

/**
 *
 * @author IronHide
 */
public class CloseDisciplineMBean implements Serializable {

    private EmployeeDTO recordDto;
    private DisciplineDTO closeDisciplineDto;
    private String searchFileNo;

    private EmployeeService employeeService;
    private DisciplineService disciplineService;
    private DisciplineValidator validator;
    private ViewHelper viewHelper;

    private FetchOptionsDTO fetchOptions;

    private boolean searchStarted;

    public CloseDisciplineMBean() {
        disciplineService = new DisciplineService();
        employeeService = new EmployeeService();
        validator = new DisciplineValidator();
        viewHelper = new ViewHelper();

        fetchOptions = new FetchOptionsDTO();
        fetchOptions.setFetchDiscipline(true);

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

    public void prepareCloseDiscplineDialog(String row_record_id) {
        if (!StringUtils.isBlank(row_record_id)) {
            try {
                RequestContext reqContext = RequestContext.getCurrentInstance();
                closeDisciplineDto = disciplineService.getEmployeeDiscipline(row_record_id);
                if (closeDisciplineDto != null) {
                    closeDisciplineDto.setEmployeeFileNo(recordDto.getFileNo());
                    reqContext.update("disciplineDialogPanel");
                }
            } catch (Exception exc) {

            }
        }       

    }

    public void closeDisciplineConfirmDialog() {
        closeDisciplineDto = new DisciplineDTO();

        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("disciplineDialogPanel");
    }

    public void preSubmitCloseDiscplineAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateCloseDiscipline(context, closeDisciplineDto);

        RequestContext reqContext = RequestContext.getCurrentInstance();

        if (validation_outcome) {
            //reqContext.execute("PF('disciplineDialog').hide()");

            reqContext.update("submitDisciplineDialogPanel");
            reqContext.execute("PF('submitDisciplineDialog').show()");
        } else {
            reqContext.update("disciplineDialogPanel");
        }
    }

    public void closeDisciplineAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateCloseDiscipline(context, closeDisciplineDto);

        if (validation_outcome) {
            try {

                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();
                
                closeDisciplineDto.setLastMod(activityDate);
                closeDisciplineDto.setLastModBy(currentUser);

                boolean result = disciplineService.closeEmployeeDiscipline(closeDisciplineDto);

                if (result) {
                    loadRecord(recordDto.getFileNo());
                    closeDisciplineDto = null;
                    ViewHelper.addInfoMessage(context, null, "Personnel discipline closed successfully.", null);
                } else {
                    loadRecord(recordDto.getFileNo());
                    closeDisciplineDto = null;
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }

            } catch (Exception e) {
                ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
            }
        } else {
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

    public DisciplineDTO getCloseDisciplineDto() {
        if (closeDisciplineDto == null) {
            closeDisciplineDto = new DisciplineDTO();
        }
        return closeDisciplineDto;
    }

    public void setCloseDisciplineDto(DisciplineDTO closeDisciplineDto) {
        this.closeDisciplineDto = closeDisciplineDto;
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
