/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.reports.leave.training;

import app.exceptions.AppException;
import dto.setup.dto.training.StaffFurtherStudyApplicationDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import service.reports.TrainingApplReportService;
import view.utils.AppConstants;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class TrainingApplReportMBean implements Serializable{

    private List<StaffFurtherStudyApplicationDTO> recordList;
    private String srchFileNo;
    private String srchInstitutionName, srchDepartment, srchCountry, srchCourse, srchApprovalStatus, srchRecordStatus;
    private Date srchFromDate, srchToDate;
    private String srchFromDateStr, srchToDateStr;
    private String currentSelectQuery;
    private StringBuilder selectQueryBuilder, generalStringBuilder;
    private final ViewHelper viewHelper;
    private final TrainingApplReportService service;

    public TrainingApplReportMBean() {
        service = new TrainingApplReportService();
        viewHelper = new ViewHelper();

        initSearchQuery(true, false, true);
    }

    private void initSearchQuery(boolean isForInit, boolean isForSearch, boolean shouldFetch) {

        selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append("SELECT d.record_id,d.employee_rec_id,d.institution,d.course,d.country,d.state ")
                .append(",date_format(d.start_date,'%d/%m/%Y') as _start_date")
                .append(",date_format(d.end_date,'%d/%m/%Y') as _end_date")
                .append(",d.approval_status,d.approval_status_reason")
                .append(",d.remarks,d.record_status,d.last_mod,d.last_mod_by ")
                .append(",t1.file_no,t1.surname,t1.other_names,t1.current_grade_level ")
                .append(",t1.primary_phone,t1.primary_email ")
                .append(",t2.description as _country ")
                .append(",ifnull(t3.description,d.state) as _state ")
                .append(",t4.description as _course ")
                .append(",t5.description as _department ")
                .append("FROM staff_further_study_application d ")
                .append("join employee t1 on d.employee_rec_id=t1.employee_rec_id ")
                .append("join setup_countries t2 on d.country=t2.record_id ")
                .append("left outer join setup_states t3 on d.state=t3.record_id ")
                .append("join setup_courses t4 on d.course=t4.record_id ")
                .append("join setup_departments t5 on t1.current_dept=t5.record_id ")
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
        
        if (!StringUtils.isBlank(srchDepartment)) {
            criteria.add("t5.record_id = " + "'" + ViewHelper.fixSqlFieldValue(srchDepartment) + "' ");
        }

        if (!StringUtils.isBlank(srchInstitutionName)) {
            criteria.add("d.institution like " + "'%" + ViewHelper.fixSqlFieldValue(srchInstitutionName) + "%' ");
        }

        if (!StringUtils.isBlank(srchCourse)) {
            criteria.add("d.course = " + "'" + ViewHelper.fixSqlFieldValue(srchCourse) + "' ");
        }

        if (!StringUtils.isBlank(srchCountry)) {
            criteria.add("d.country = " + "'" + ViewHelper.fixSqlFieldValue(srchCountry) + "' ");
        }

        if (!StringUtils.isBlank(srchApprovalStatus)) {
            criteria.add("d.approval_status = " + "'" + ViewHelper.fixSqlFieldValue(srchApprovalStatus) + "' ");
        }

        if (StringUtils.isBlank(srchRecordStatus)) {
            srchRecordStatus = AppConstants.ACTIVE;
            criteria.add("d.record_status = " + "'" + ViewHelper.fixSqlFieldValue(srchRecordStatus) + "' ");
        } else {
            criteria.add("d.record_status = " + "'" + ViewHelper.fixSqlFieldValue(srchRecordStatus) + "' ");
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
                    .append("'").append(srchToDateStr).append("' ");
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

    public List<StaffFurtherStudyApplicationDTO> getRecordList() {
        if (recordList == null) {
            recordList = new ArrayList<>();
        }
        return recordList;
    }

    public void setRecordList(List<StaffFurtherStudyApplicationDTO> recordList) {
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

    public String getSrchCountry() {
        return srchCountry;
    }

    public void setSrchCountry(String srchCountry) {
        this.srchCountry = srchCountry;
    }

    public String getSrchCourse() {
        return srchCourse;
    }

    public void setSrchCourse(String srchCourse) {
        this.srchCourse = srchCourse;
    }

    public String getSrchApprovalStatus() {
        return srchApprovalStatus;
    }

    public void setSrchApprovalStatus(String srchApprovalStatus) {
        this.srchApprovalStatus = srchApprovalStatus;
    }

    public String getSrchRecordStatus() {
        return srchRecordStatus;
    }

    public void setSrchRecordStatus(String srchRecordStatus) {
        this.srchRecordStatus = srchRecordStatus;
    }

    public String getSrchDepartment() {
        return srchDepartment;
    }

    public void setSrchDepartment(String srchDepartment) {
        this.srchDepartment = srchDepartment;
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

    public String getSrchInstitutionName() {
        return srchInstitutionName;
    }

    public void setSrchInstitutionName(String srchInstitutionName) {
        this.srchInstitutionName = srchInstitutionName;
    }

}

