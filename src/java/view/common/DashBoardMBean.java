/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.common;

import dto.common.DashboardDTO;
import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import service.common.DashBoardService;
import service.employees.EmployeeService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class DashBoardMBean implements Serializable {

    private DashboardDTO recordDto;
    private final DashBoardService service;
    private final EmployeeService employeeService;
    private EmployeeDTO employeeDTO;
    private FetchOptionsDTO fetchOptionsDTO;
    private final ViewHelper viewHelper;

    public DashBoardMBean() {
        service = new DashBoardService();
        employeeService = new EmployeeService();
        viewHelper = new ViewHelper();
        fetchOptionsDTO = new FetchOptionsDTO();

    }

    @PostConstruct
    public void afterConstruct() {
        try {
            recordDto = service.getDashBoard();
            employeeDTO = employeeService.getEmployeee(viewHelper.getAppUser().getPersonnelFileNo(), AppConstants.FETCH_MODE_FILE_NO, AppConstants.FETCH_VIEW_ONLY, fetchOptionsDTO);
        } catch (Exception e) {
            // System.out.println("Error" + e.getMessage());
        }
    }

    public void updateEmployeeAccountDetails(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome =true;// validator.validateContactInfo(context, recordDto);

        if (validation_outcome) {

            String activityDate = DateTimeUtils.getActivityDate();
            String currentUser = viewHelper.getAppUser().getUsername();

            employeeDTO.setLastMod(activityDate);
            employeeDTO.setLastModBy(currentUser);

            try {
                boolean result = employeeService.updateEmployeeAccountDetails(employeeDTO);
                if (result) {
                   
                    ViewHelper.addInfoMessage(context, null, "Account details updated successfully", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
            }

        }
    }

    public DashboardDTO getRecordDto() {
        if (recordDto == null) {
            recordDto = new DashboardDTO();
        }
        return recordDto;
    }

    public void setRecordDto(DashboardDTO recordDto) {
        this.recordDto = recordDto;
    }

    public EmployeeDTO getEmployeeDTO() {
        if (employeeDTO == null) {
            employeeDTO = new EmployeeDTO();
        }
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

}
