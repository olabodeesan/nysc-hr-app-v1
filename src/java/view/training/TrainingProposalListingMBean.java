/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.training;

import app.exceptions.AppException;
import dto.setup.dto.training.TrainingProposalDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import service.training.TrainingProposalService;
import view.utils.AppConstants;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class TrainingProposalListingMBean implements Serializable {

    private List<TrainingProposalDTO> recordList;
    private TrainingProposalDTO selectedRecord;
    //private String srchFileNo;
    private String srchTitle, srchConsultant, srchType, srchSpecialization,
            srchStartDate, srchApprovalStatus, srchRecordStatus;
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
    private final TrainingProposalService service;

    public TrainingProposalListingMBean() {
        service = new TrainingProposalService();
        viewHelper = new ViewHelper();

        initSearchQuery(true, false, true);
    }

    private void initSearchQuery(boolean isForInit, boolean isForSearch, boolean shouldFetch) {

        countQueryBuilder = new StringBuilder();
        countQueryBuilder.append("select count(d.record_id) from training_proposal d ")
                .append(" join setup_training_type t1 on d.training_type=t1.record_id ")
                .append(" join setup_countries t2 on d.country=t2.record_id ")
                .append(" left outer join setup_states t3 on d.state=t3.record_id ")
                .append(" join setup_training_consultant t4 on d.consultant=t4.record_id ")
                .append(" join setup_training_certificates t5 on d.expected_certificate=t5.record_id  ")
                .append("where ");

        selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append("select d.record_id,d.title,d.training_type,d.country,d.state ")
                .append(",date_format(d.start_date,'%d/%m/%Y') as _start_date")
                .append(",date_format(d.end_date,'%d/%m/%Y') as _end_date")
                .append(",d.objective,d.consultant,d.expected_certificate,d.approval_status,d.approval_status_reason ")
                .append(",d.remarks,d.record_status,d.last_mod,d.last_mod_by ")
                .append(",t1.description as _training_type ")
                .append(",t2.description as _country ")
                .append(",ifnull(t3.description,d.state) as _state ")
                .append(",t4.description as _consultant ")
                .append(",t5.description as _expected_certificate ")
                .append(" from training_proposal d ")
                .append(" join setup_training_type t1 on d.training_type=t1.record_id ")
                .append(" join setup_countries t2 on d.country=t2.record_id ")
                .append(" left outer join setup_states t3 on d.state=t3.record_id ")
                .append(" join setup_training_consultant t4 on d.consultant=t4.record_id ")
                .append(" join setup_training_certificates t5 on d.expected_certificate=t5.record_id  ")
                .append("where ");

        if (isForInit) {
            countQueryBuilder.append(" d.record_id<>'0' ");
            currentCountQuery = countQueryBuilder.toString();

            selectQueryBuilder.append(" d.record_id<>'0' ").append(" order by d.last_mod desc ");
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
        if (!StringUtils.isBlank(srchTitle)) {
            criteria.add("d.title like " + "'%" + ViewHelper.fixSqlFieldValue(srchTitle) + "%' ");
        }

        if (!StringUtils.isBlank(srchConsultant)) {
            criteria.add("d.consultant = " + "'" + ViewHelper.fixSqlFieldValue(srchConsultant) + "' ");
        }

        if (!StringUtils.isBlank(srchType)) {
            criteria.add("d.training_type = " + "'" + ViewHelper.fixSqlFieldValue(srchType) + "' ");
        }

        if (StringUtils.isBlank(srchApprovalStatus)) {
            srchApprovalStatus = AppConstants.TRAINING_APPROVED;
            criteria.add("d.approval_status = " + "'" + ViewHelper.fixSqlFieldValue(srchApprovalStatus) + "' ");
        } else {
            criteria.add("d.approval_status = " + "'" + ViewHelper.fixSqlFieldValue(srchApprovalStatus) + "' ");
        }

        if (StringUtils.isBlank(srchRecordStatus)) {
            srchRecordStatus = AppConstants.ACTIVE;
            criteria.add("d.record_status = " + "'" + ViewHelper.fixSqlFieldValue(srchRecordStatus) + "' ");
        } else {
            criteria.add("d.record_status = " + "'" + ViewHelper.fixSqlFieldValue(srchRecordStatus) + "' ");
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
            selectQueryBuilder.append(" order by d.last_mod desc");

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
            recordList = (List<TrainingProposalDTO>) result.get(AppConstants.RECORD_LIST);

        } catch (AppException exc) {
            System.out.println(exc.getMessage());
            currentPage = 0;
            totalRows = 0;
            recordList = null;

        }
    }

    public List<TrainingProposalDTO> getRecordList() {
        if (recordList == null) {
            recordList = new ArrayList<>();
        }
        return recordList;
    }

    public void setRecordList(List<TrainingProposalDTO> recordList) {
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

    public TrainingProposalDTO getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(TrainingProposalDTO selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public String getSrchTitle() {
        return srchTitle;
    }

    public void setSrchTitle(String srchTitle) {
        this.srchTitle = srchTitle;
    }

    public String getSrchConsultant() {
        return srchConsultant;
    }

    public void setSrchConsultant(String srchConsultant) {
        this.srchConsultant = srchConsultant;
    }

    public String getSrchType() {
        return srchType;
    }

    public void setSrchType(String srchType) {
        this.srchType = srchType;
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

}
