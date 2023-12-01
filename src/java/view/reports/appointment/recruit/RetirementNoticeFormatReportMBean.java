/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.reports.appointment.recruit;

import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import service.employees.EmployeeService;
import view.utils.AppConstants;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class RetirementNoticeFormatReportMBean implements Serializable{

    private EmployeeDTO recordDto;
    private String searchFileNo;
    private String todaysDate;

    private final EmployeeService employeeService;
    private final ViewHelper viewHelper;

    private final FetchOptionsDTO fetchOptions;

    private boolean searchStarted;

    public RetirementNoticeFormatReportMBean() {
        employeeService = new EmployeeService();
        viewHelper = new ViewHelper();

        fetchOptions = new FetchOptionsDTO();

    }
    
    @PostConstruct
    public void afterConstruct(){
        Calendar now = Calendar.getInstance();
        Date nowDate = now.getTime();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        todaysDate = dateFormat.format(nowDate);
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
    
    public EmployeeDTO getRecordDto() {
        if (recordDto == null) {
            recordDto = new EmployeeDTO();
        }
        return recordDto;
    }

    public void setRecordDto(EmployeeDTO recordDto) {
        this.recordDto = recordDto;
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

    public String getTodaysDate() {
        return todaysDate;
    }

    public void setTodaysDate(String todaysDate) {
        this.todaysDate = todaysDate;
    }

}

