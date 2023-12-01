/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.registry;

import app.exceptions.AppException;
import dto.setup.dto.registry.FileRequestDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import service.registry.FileRequestService;
import view.utils.AppConstants;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class FileRequestMBean implements Serializable {

    private List<FileRequestDTO> recordList;
    private FileRequestDTO selectedRecord;
    private String srchRegistryFileNo, srchRegistryFileName;
    private String srchRequesterInfo;
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
    private final FileRequestService service;

    public FileRequestMBean() {
        service = new FileRequestService();
        viewHelper = new ViewHelper();

        initSearchQuery(true, false, true);
    }

    private void initSearchQuery(boolean isForInit, boolean isForSearch, boolean shouldFetch) {

        countQueryBuilder = new StringBuilder();
        countQueryBuilder.append("select count(d.record_id) from registry_file_requests d ")
                .append("where ");

        selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append("SELECT d.record_id,d.registry_file_no,d.registry_file_name,d.requesting_officer_info,d.purpose ")
                .append(",date_format(d.request_date,'%d/%m/%Y') as _request_date,d.request_time ")
                .append(",date_format(d.return_date,'%d/%m/%Y') as _return_date,d.return_time,d.in_out_status,d.remarks,d.record_status")
                .append(",d.last_mod,d.last_mod_by ")
                .append("FROM registry_file_requests d ")
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
        if (!StringUtils.isBlank(srchRegistryFileNo)) {
            criteria.add("d.registry_file_no = " + "'" + ViewHelper.fixSqlFieldValue(srchRegistryFileNo) + "' ");
        }

        if (!StringUtils.isBlank(srchRegistryFileName)) {
            criteria.add("d.registry_file_name like " + "'%" + ViewHelper.fixSqlFieldValue(srchRegistryFileName) + "%' ");
        }
        
        if (!StringUtils.isBlank(srchRequesterInfo)) {
            criteria.add("d.requesting_officer_info like " + "'%" + ViewHelper.fixSqlFieldValue(srchRequesterInfo) + "%' ");
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
            recordList = (List<FileRequestDTO>) result.get(AppConstants.RECORD_LIST);

        } catch (AppException exc) {
            System.out.println(exc.getMessage());
            currentPage = 0;
            totalRows = 0;
            recordList = null;

        }
    }

    public List<FileRequestDTO> getRecordList() {
        if (recordList == null) {
            recordList = new ArrayList<>();
        }
        return recordList;
    }

    public void setRecordList(List<FileRequestDTO> recordList) {
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

    public FileRequestDTO getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(FileRequestDTO selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public String getSrchRegistryFileNo() {
        return srchRegistryFileNo;
    }

    public void setSrchRegistryFileNo(String srchRegistryFileNo) {
        this.srchRegistryFileNo = srchRegistryFileNo;
    }

    public String getSrchRegistryFileName() {
        return srchRegistryFileName;
    }

    public void setSrchRegistryFileName(String srchRegistryFileName) {
        this.srchRegistryFileName = srchRegistryFileName;
    }

    public String getSrchRequesterInfo() {
        return srchRequesterInfo;
    }

    public void setSrchRequesterInfo(String srchRequesterInfo) {
        this.srchRequesterInfo = srchRequesterInfo;
    }

}
