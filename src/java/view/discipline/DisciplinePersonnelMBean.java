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
public class DisciplinePersonnelMBean implements Serializable {

    private EmployeeDTO recordDto;
    private DisciplineDTO newDisciplineDto;
    private String searchFileNo;

    private final EmployeeService employeeService;
    private final DisciplineService disciplineService;
    private final DisciplineValidator validator;
    private final ViewHelper viewHelper;

    private final FetchOptionsDTO fetchOptions;

    private boolean searchStarted;

    public DisciplinePersonnelMBean() {
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

    public void prepareNewDiscplineDialog() {
        newDisciplineDto = new DisciplineDTO();
        newDisciplineDto.setEmployeeRecordId(recordDto.getRecordId());
        newDisciplineDto.setEmployeeFileNo(recordDto.getFileNo());

        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("disciplineDialogPanel");
    }

    public void closeDisciplineConfirmDialog() {
        newDisciplineDto = new DisciplineDTO();
        newDisciplineDto.setEmployeeRecordId(recordDto.getRecordId());
        //reasonsMap = new LinkedHashMap<>();

        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("item-detail-panel");
    }

    public void preSubmitDiscplineAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateNewDiscipline(context, newDisciplineDto);

        RequestContext reqContext = RequestContext.getCurrentInstance();

        if (validation_outcome) {
            //reqContext.execute("PF('disciplineDialog').hide()");

            reqContext.update("submitDisciplineDialogPanel");
            reqContext.execute("PF('submitDisciplineDialog').show()");
        } else {
            reqContext.update("disciplineDialogPanel");
        }
    }

    public void disciplineAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateNewDiscipline(context, newDisciplineDto);

        if (validation_outcome) {
            try {

                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();

                newDisciplineDto.setCreated(activityDate);
                newDisciplineDto.setCreatedBy(currentUser);
                newDisciplineDto.setLastMod(activityDate);
                newDisciplineDto.setLastModBy(currentUser);

                boolean result = disciplineService.disciplineEmployee(newDisciplineDto);

                if (result) {
                    loadRecord(recordDto.getFileNo());
                    newDisciplineDto = null;
                    ViewHelper.addInfoMessage(context, null, "Personnel is disciplined successfully.", null);
                } else {
                    loadRecord(recordDto.getFileNo());
                    newDisciplineDto = null;
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

    public DisciplineDTO getNewDisciplineDto() {
        if(newDisciplineDto == null){
            newDisciplineDto = new DisciplineDTO();
        }
        return newDisciplineDto;
    }

    public void setNewDisciplineDto(DisciplineDTO newDisciplineDto) {
        this.newDisciplineDto = newDisciplineDto;
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
