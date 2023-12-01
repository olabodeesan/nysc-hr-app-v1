/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.registry;

import app.exceptions.AppException;
import dto.setup.dto.registry.AnnualLeaveRosterDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import service.registry.AnnualLeaveRosterService;
import view.utils.AppConstants;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class PersonnelAnnualLeaveRosterListingMBean implements Serializable {

    private List<AnnualLeaveRosterDTO> recordList;
    private AnnualLeaveRosterDTO selectedRecord;
    private String srchFileNo;
    private Date srchFromDate, srchToDate;
    private String srchFromDateStr, srchToDateStr;
    private String currentCountQuery;
    private String currentSelectQuery;
    private StringBuilder countQueryBuilder, selectQueryBuilder, generalStringBuilder;
    private int totalRows = 0;
    private int currentPage = 0;
    private int startRow;
    private int pageSize = 35;
    private String paginationDescription;
    private final ViewHelper viewHelper;
    private final AnnualLeaveRosterService service;

    public PersonnelAnnualLeaveRosterListingMBean() {
        service = new AnnualLeaveRosterService();
        viewHelper = new ViewHelper();

        initSearchQuery(true, false, true);
    }

    private void initSearchQuery(boolean isForInit, boolean isForSearch, boolean shouldFetch) {

        countQueryBuilder = new StringBuilder();
        countQueryBuilder.append("select count(d.record_id) from registry_annual_leave_roster d ")
                .append("join employee t1 on d.employee_rec_id=t1.employee_rec_id ")
                .append("join setup_departments t2 on t1.current_dept=t2.record_id ")
                .append("where ");

        selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append("SELECT d.record_id,d.roster_year,d.employee_rec_id,d.duration ")
                .append(",date_format(d.expected_start_date,'%d/%m/%Y') as _expected_start_date  ")
                .append(",date_format(d.expected_end_date,'%d/%m/%Y') as _expected_end_date ")
                .append(",d.remaining_days,d.last_mod,d.last_mod_by ")
                .append(",t1.file_no,t1.surname,t1.other_names,t1.current_grade_level ")
                .append(",t1.primary_phone,t1.primary_email ")
                .append(",t2.description as _department ")
                .append("FROM registry_annual_leave_roster d ")
                .append("join employee t1 on d.employee_rec_id=t1.employee_rec_id ")
                .append("join setup_departments t2 on t1.current_dept=t2.record_id ")
                .append("where ");

        if (isForInit) {
            countQueryBuilder.append(" d.record_id<>'0' ");
            currentCountQuery = countQueryBuilder.toString();

            selectQueryBuilder.append(" d.record_id<>'0' ").append(" order by d.roster_year desc, t1._temp_sort asc ");
            currentSelectQuery = selectQueryBuilder.toString();

            if (shouldFetch) {
                fillResults();
            }
        }
    }

    public void nextPage(ActionEvent ae) {

        if ((currentPage * pageSize) < totalRows) {
            currentPage++;
        }

        if ((currentPage * pageSize) > totalRows) {
            currentPage--;
        }

        fillResults();
    }

    public void previousPage(ActionEvent ae) {

        if (currentPage <= 0) {
            currentPage = 0;
        } else {
            currentPage--;
        }
        fillResults();
    }

    public void searchAction(ActionEvent event) {

        initSearchQuery(false, true, false);

        List<String> criteria = new ArrayList<>();

        //if (!StringUtils.isBlank(srchFileNo)) {
        //    criteria.add("e.file_no like " + "'%" + ViewHelper.fixSqlFieldValue(srchFileNo) + "%' ");
        //}
        if (!StringUtils.isBlank(srchFileNo)) {
            criteria.add("t1.file_no = " + "'" + ViewHelper.fixSqlFieldValue(srchFileNo) + "' ");
        }

        /*if (srchFromDate != null) {
         if (srchToDate == null) {
         srchToDate = srchFromDate;
         }
         srchFromDateStr = viewHelper.getDateAsYMDWithDashString(srchFromDate);
         srchToDateStr = viewHelper.getDateAsYMDWithDashString(srchToDate);

         generalStringBuilder = new StringBuilder();
         generalStringBuilder.append("d.start_date between ")
         .append("cast('").append(srchFromDateStr).append("' as date)")
         .append(" and ")
         .append("cast('").append(srchToDateStr).append("' as date)");
         criteria.add(generalStringBuilder.toString());
         }*/
        if (criteria.isEmpty()) {
            initSearchQuery(true, false, true);
        } else {

            for (int i = 0; i < criteria.size(); i++) {
                if (i > 0) {
                    selectQueryBuilder.append(" AND ");
                    countQueryBuilder.append(" AND ");
                }
                selectQueryBuilder.append(criteria.get(i));
                countQueryBuilder.append(criteria.get(i));
            }
            selectQueryBuilder.append(" order by d.roster_year desc, t1._temp_sort asc");

            currentPage = 0;
            currentCountQuery = countQueryBuilder.toString();
            currentSelectQuery = selectQueryBuilder.toString();

            fillResults();

        }

        selectedRecord = null;

    }

    private void fillResults() {
        startRow = currentPage * pageSize;

        try {
            Map<String, Object> result = service.searchRecords(currentCountQuery, currentSelectQuery, startRow, pageSize);
            totalRows = Integer.parseInt(result.get(AppConstants.TOTAL_RECORDS).toString());
            recordList = (List<AnnualLeaveRosterDTO>) result.get(AppConstants.RECORD_LIST);

        } catch (AppException exc) {
            System.out.println(exc.getMessage());
            currentPage = 0;
            totalRows = 0;
            recordList = null;

        }
    }

    public List<AnnualLeaveRosterDTO> getRecordList() {
        if (recordList == null) {
            recordList = new ArrayList<>();
        }
        return recordList;
    }

    public void setRecordList(List<AnnualLeaveRosterDTO> recordList) {
        this.recordList = recordList;
    }

    public String getPaginationDescription() {
        if (!getRecordList().isEmpty()) {
            paginationDescription = "Displaying " + (startRow + 1);
            if ((startRow + 1) < totalRows) {
                paginationDescription += "-";

                if ((startRow + pageSize) < totalRows) {
                    paginationDescription += (startRow + pageSize);
                } else {
                    paginationDescription += (totalRows);
                }
            }
            paginationDescription += " of " + (totalRows);
        } else {
            paginationDescription = "No Records Found";
        }

        return paginationDescription;
    }

    public void setPaginationDescription(String paginationDescription) {
        this.paginationDescription = paginationDescription;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentCountQuery() {
        return currentCountQuery;
    }

    public void setCurrentCountQuery(String currentCountQuery) {
        this.currentCountQuery = currentCountQuery;
    }

    public String getCurrentSelectQuery() {
        return currentSelectQuery;
    }

    public void setCurrentSelectQuery(String currentSelectQuery) {
        this.currentSelectQuery = currentSelectQuery;
    }

    public AnnualLeaveRosterDTO getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(AnnualLeaveRosterDTO selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public String getSrchFileNo() {
        return srchFileNo;
    }

    public void setSrchFileNo(String srchFileNo) {
        this.srchFileNo = srchFileNo;
    }

}
