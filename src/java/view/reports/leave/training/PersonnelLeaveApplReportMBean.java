/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.reports.leave.training;

import app.exceptions.AppException;
import dto.setup.dto.registry.StaffLeaveApplicationDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import service.registry.StaffLeaveApplicationService;
import service.reports.PersonnelLeaveApplReportService;
import view.utils.AppConstants;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class PersonnelLeaveApplReportMBean implements Serializable{

    private List<StaffLeaveApplicationDTO> recordList;
    private String srchFileNo, srchDepartment, srchApprovalStatus;
    private Date srchFromDate, srchToDate;
    private String srchFromDateStr, srchToDateStr;
    private String currentSelectQuery;
    private StringBuilder selectQueryBuilder, generalStringBuilder;
    private final ViewHelper viewHelper;
    private final PersonnelLeaveApplReportService service;

    public PersonnelLeaveApplReportMBean() {
        service = new PersonnelLeaveApplReportService();
        viewHelper = new ViewHelper();

        initSearchQuery(true, false, true);
    }

    private void initSearchQuery(boolean isForInit, boolean isForSearch, boolean shouldFetch) {

        selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append("SELECT d.record_id,d.employee_rec_id,d.duration,d.leave_application_year")
                .append(",date_format(d.start_date,'%d/%m/%Y') as _start_date")
                .append(",date_format(d.expected_end_date,'%d/%m/%Y') as _expected_end_date")
                .append(",d.before_approval_remaining_days,d.after_approval_remaining_days ")
                .append(",d.approval_status,d.remarks,d.record_status,d.last_mod,d.last_mod_by")
                .append(",t1.file_no,t1.surname,t1.other_names,t1.current_grade_level ")
                .append(",t1.primary_phone,t1.primary_email ")
                .append(",t2.description as _department ")
                .append("FROM registry_staff_leave_application d ")
                .append("join employee t1 on d.employee_rec_id=t1.employee_rec_id ")
                .append("join setup_departments t2 on t1.current_dept=t2.record_id ")
                .append("where ");

        if (isForInit) {

            selectQueryBuilder.append(" d.record_id<>'0' ").append(" order by d.last_mod desc ");
            currentSelectQuery = selectQueryBuilder.toString();

            if (shouldFetch) {
                fillResults();
            }
        }
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

        if (!StringUtils.isBlank(srchApprovalStatus)) {
            criteria.add("d.approval_status = " + "'" + ViewHelper.fixSqlFieldValue(srchApprovalStatus) + "' ");
        }

        if (!StringUtils.isBlank(srchDepartment)) {
            criteria.add("t2.record_id = " + "'" + ViewHelper.fixSqlFieldValue(srchDepartment) + "' ");
        }

        if (srchFromDate != null) {
            if (srchToDate == null) {
                srchToDate = srchFromDate;
            }
            srchFromDateStr = viewHelper.getDateAsYMDWithDashString(srchFromDate);
            srchToDateStr = viewHelper.getDateAsYMDWithDashString(srchToDate);

            generalStringBuilder = new StringBuilder();
            generalStringBuilder.append("d.start_date between ")
                    .append("'").append(srchFromDateStr).append("' ")
                    .append(" and ")
                    .append("'").append(srchToDateStr).append("'");
            criteria.add(generalStringBuilder.toString());
        }

        if (criteria.isEmpty()) {
            initSearchQuery(true, false, true);
        } else {

            for (int i = 0; i < criteria.size(); i++) {
                if (i > 0) {
                    selectQueryBuilder.append(" AND ");
                }
                selectQueryBuilder.append(criteria.get(i));
            }
            selectQueryBuilder.append(" order by d.last_mod desc");

            currentSelectQuery = selectQueryBuilder.toString();

            fillResults();

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

    public List<StaffLeaveApplicationDTO> getRecordList() {
        if (recordList == null) {
            recordList = new ArrayList<>();
        }
        return recordList;
    }

    public void setRecordList(List<StaffLeaveApplicationDTO> recordList) {
        this.recordList = recordList;
    }

    public String getCurrentSelectQuery() {
        return currentSelectQuery;
    }

    public void setCurrentSelectQuery(String currentSelectQuery) {
        this.currentSelectQuery = currentSelectQuery;
    }

    public String getSrchFileNo() {
        return srchFileNo;
    }

    public void setSrchFileNo(String srchFileNo) {
        this.srchFileNo = srchFileNo;
    }

    public String getSrchDepartment() {
        return srchDepartment;
    }

    public void setSrchDepartment(String srchDepartment) {
        this.srchDepartment = srchDepartment;
    }

    public String getSrchApprovalStatus() {
        return srchApprovalStatus;
    }

    public void setSrchApprovalStatus(String srchApprovalStatus) {
        this.srchApprovalStatus = srchApprovalStatus;
    }

    public Date getSrchFromDate() {
        return srchFromDate;
    }

    public void setSrchFromDate(Date srchFromDate) {
        this.srchFromDate = srchFromDate;
    }

    public Date getSrchToDate() {
        return srchToDate;
    }

    public void setSrchToDate(Date srchToDate) {
        this.srchToDate = srchToDate;
    }

}

