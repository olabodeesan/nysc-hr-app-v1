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
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import service.reports.PersonnelRecordsReportService;
import view.utils.AppConstants;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class RetirementProposalReportMBean implements Serializable {

    private List<EmployeeDTO> recordList;
    private EmployeeDTO selectedRecord;
    private Date searchStartDate, searchEndDate;
    private String searchStartDateStr, searchEndDateStr;
    private String currentSelectQuery;
    private StringBuilder selectQueryBuilder, generalStringBuilder;
    private final ViewHelper viewHelper;
    private final PersonnelRecordsReportService service;

    public RetirementProposalReportMBean() {
        service = new PersonnelRecordsReportService();
        viewHelper = new ViewHelper();

        initSearchQuery(true, false, true);
    }

    private void initSearchQuery(boolean isForInit, boolean isForSearch, boolean shouldFetch) {

        selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append(service.getEmployeeSelectQuery())
                .append("where ");

        if (isForInit) {
            selectQueryBuilder.append("e.employee_rec_id='0' and e.present_status in ")
                    .append("(")
                    .append(AppConstants.PERSONNEL_ACTIVE)
                    .append(",").append(AppConstants.PERSONNEL_SUSPENDED)
                    .append(") ")
                    .append(" order by e._temp_sort  ");
            currentSelectQuery = selectQueryBuilder.toString();

            if (shouldFetch) {
                fillResults();
            }
        }
    }

    public void searchAction(ActionEvent event) {

        initSearchQuery(false, true, false);

        List<String> criteria = new ArrayList<>();

        if (searchStartDate != null) {

            //compulsory criteria
            generalStringBuilder = new StringBuilder();
            generalStringBuilder.append(" e.present_status in ")
                    .append("(")
                    .append(AppConstants.PERSONNEL_ACTIVE)
                    .append(",").append(AppConstants.PERSONNEL_SUSPENDED)
                    .append(") ");
            criteria.add(generalStringBuilder.toString());

            if (searchEndDate == null) {
                searchEndDate = searchStartDate;
            }
            searchStartDateStr = viewHelper.getDateAsYMDWithDashString(searchStartDate);
            searchEndDateStr = viewHelper.getDateAsYMDWithDashString(searchEndDate);

            generalStringBuilder = new StringBuilder();
            generalStringBuilder.append("e.date_due_for_retirement between ")
                    .append(" '").append(searchStartDateStr).append("' ")
                    .append(" and ")
                    .append(" '").append(searchEndDateStr).append("' ");
            criteria.add(generalStringBuilder.toString());
        }else{
            FacesContext context = FacesContext.getCurrentInstance();
            ViewHelper.addWarningMessage(context, null, "From date is required", null);
        }

        if (criteria.isEmpty()) {
            initSearchQuery(true, false, true);
            //recordList = null;
        } else {

            for (int i = 0; i < criteria.size(); i++) {
                if (i > 0) {
                    selectQueryBuilder.append(" AND ");
                }
                selectQueryBuilder.append(criteria.get(i));
            }
            selectQueryBuilder.append(" order by e._temp_sort ");

            currentSelectQuery = selectQueryBuilder.toString();

            fillResults();

        }

        selectedRecord = null;

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

    public EmployeeDTO getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(EmployeeDTO selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public Date getSearchStartDate() {
        return searchStartDate;
    }

    public void setSearchStartDate(Date searchStartDate) {
        this.searchStartDate = searchStartDate;
    }

    public Date getSearchEndDate() {
        return searchEndDate;
    }

    public void setSearchEndDate(Date searchEndDate) {
        this.searchEndDate = searchEndDate;
    }

    public String getSearchStartDateStr() {
        return searchStartDateStr;
    }

    public void setSearchStartDateStr(String searchStartDateStr) {
        this.searchStartDateStr = searchStartDateStr;
    }

    public String getSearchEndDateStr() {
        return searchEndDateStr;
    }

    public void setSearchEndDateStr(String searchEndDateStr) {
        this.searchEndDateStr = searchEndDateStr;
    }

}
