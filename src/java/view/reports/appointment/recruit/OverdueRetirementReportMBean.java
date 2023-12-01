/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.reports.appointment.recruit;

import app.exceptions.AppException;
import dto.employees.EmployeeDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import service.reports.PersonnelRecordsReportService;
import view.utils.AppConstants;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class OverdueRetirementReportMBean implements Serializable{

    private List<EmployeeDTO> recordList;
    private String currentSelectQuery;
    private StringBuilder selectQueryBuilder, generalStringBuilder;
    private final ViewHelper viewHelper;
    private final PersonnelRecordsReportService service;

    public OverdueRetirementReportMBean() {
        service = new PersonnelRecordsReportService();
        viewHelper = new ViewHelper();

        initSearchQuery(true, false, true);
    }

    private void initSearchQuery(boolean isForInit, boolean isForSearch, boolean shouldFetch) {

        selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append(service.getEmployeeSelectQuery())
                .append("where ");

        if (isForInit) {
            selectQueryBuilder.append(" e.present_status in ")
                    .append("(")
                    .append(AppConstants.PERSONNEL_ACTIVE)
                    .append(",").append(AppConstants.PERSONNEL_SUSPENDED)
                    .append(") and e.date_due_for_retirement < curdate() ")
                    .append(" order by e._temp_sort  ");
            currentSelectQuery = selectQueryBuilder.toString();

            if (shouldFetch) {
                fillResults();
            }
        }
    }

    private void fillResults() {

        try {
            recordList = service.searchRecords(currentSelectQuery);
        } catch (AppException exc) {
            System.out.println(exc.getMessage());
            recordList = null;

        }
    }

    public List<EmployeeDTO> getRecordList() {
        if (recordList == null) {
            recordList = new ArrayList<>();
        }
        return recordList;
    }

    public void setRecordList(List<EmployeeDTO> recordList) {
        this.recordList = recordList;
    }

    public String getCurrentSelectQuery() {
        return currentSelectQuery;
    }

    public void setCurrentSelectQuery(String currentSelectQuery) {
        this.currentSelectQuery = currentSelectQuery;
    }

}
