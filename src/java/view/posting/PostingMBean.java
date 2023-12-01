/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.posting;

import app.exceptions.AppException;
import dto.posting.PostingDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import service.posting.PostingService;
import view.utils.AppConstants;
import view.utils.ViewHelper;

/**
 *
 * @author Gainsolutions
 */
public class PostingMBean implements Serializable {

    private List<PostingDTO> recordList;
  //  private EmployeeDTO selectedRecord;

    private PostingDTO selectedRecord;
    private String srchFileNo;
    private String srchSurname, srchOtherNames, srchGender, srchMaritalStatus, srchLocation, srchDept, srchStateOfOrigin;
    private String srchLga, srchCadre, srchRank, srchGradeLevel, srchGradeStep, srchPhone;
    private String currentCountQuery;
    private String currentSelectQuery;
    private StringBuilder countQueryBuilder, selectQueryBuilder, generalStringBuilder;
    private int totalRows = 0;
    private int currentPage = 0;
    private int startRow;
    private int pageSize = 35;
    private String paginationDescription;
    private ViewHelper viewHelper;
    private PostingService service;

    public PostingMBean() {
        service = new PostingService();
        viewHelper = new ViewHelper();

        initSearchQuery(true, false, true);
    }

    private void initSearchQuery(boolean isForInit, boolean isForSearch, boolean shouldFetch) {
        countQueryBuilder = new StringBuilder();
        countQueryBuilder.append("select count(e.employee_rec_id) from employee e ")
                .append("where ");

        selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append(service.getEmployeePostingSelectQuery())
                .append("where ");

        if (isForInit) {
            countQueryBuilder.append(" e.employee_rec_id<>'0' and present_status='1' ");
            currentCountQuery = countQueryBuilder.toString();

            selectQueryBuilder.append(" e.employee_rec_id<>'0' and present_status='1' order by e.current_grade_level desc ");
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

        //criteria.add("d.host_staff_record_id = " + "'" + ViewHelper.fixSqlFieldValue(viewHelper.getCurrentUser().getUsername()) + "' ");
        if (!StringUtils.isBlank(srchFileNo)) {
            criteria.add("e.file_no like " + "'%" + ViewHelper.fixSqlFieldValue(srchFileNo) + "%' ");
        }

        if (!StringUtils.isBlank(srchSurname)) {
            criteria.add("e.surname like " + "'%" + ViewHelper.fixSqlFieldValue(srchSurname) + "%' ");
        }

        if (!StringUtils.isBlank(srchOtherNames)) {
            criteria.add("e.other_names like " + "'%" + ViewHelper.fixSqlFieldValue(srchOtherNames) + "%' ");
        }

        if (!StringUtils.isBlank(srchLocation)) {
            criteria.add("e.current_location = " + "'" + ViewHelper.fixSqlFieldValue(srchLocation) + "' ");
        }

        if (!StringUtils.isBlank(srchDept)) {
            criteria.add("e.current_dept = " + "'" + ViewHelper.fixSqlFieldValue(srchDept) + "' ");
        }

        if (!StringUtils.isBlank(srchCadre)) {
            criteria.add("e.current_cadre = " + "'" + ViewHelper.fixSqlFieldValue(srchCadre) + "' ");
        }

        if (!StringUtils.isBlank(srchRank)) {
            criteria.add("e.current_rank = " + "'" + ViewHelper.fixSqlFieldValue(srchRank) + "' ");
        }

        if (!StringUtils.isBlank(srchGradeLevel)) {
            criteria.add("e.current_grade_level = " + "'" + ViewHelper.fixSqlFieldValue(srchGradeLevel) + "' ");
        }

        if (!StringUtils.isBlank(srchStateOfOrigin)) {
            criteria.add("e.state_of_origin = " + "'" + ViewHelper.fixSqlFieldValue(srchStateOfOrigin) + "' ");
        }

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
            selectQueryBuilder.append(" order by e.current_grade_level desc");

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
            recordList = (List<PostingDTO>) result.get(AppConstants.RECORD_LIST);

        } catch (AppException exc) {
            System.out.println(exc.getMessage());
            currentPage = 0;
            totalRows = 0;
            recordList = null;

        }
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

    public PostingDTO getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(PostingDTO selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public String getSrchFileNo() {
        return srchFileNo;
    }

    public void setSrchFileNo(String srchFileNo) {
        this.srchFileNo = srchFileNo;
    }

    public String getSrchSurname() {
        return srchSurname;
    }

    public void setSrchSurname(String srchSurname) {
        this.srchSurname = srchSurname;
    }

    public String getSrchOtherNames() {
        return srchOtherNames;
    }

    public void setSrchOtherNames(String srchOtherNames) {
        this.srchOtherNames = srchOtherNames;
    }

    public String getSrchLocation() {
        return srchLocation;
    }

    public void setSrchLocation(String srchLocation) {
        this.srchLocation = srchLocation;
    }

    public String getSrchDept() {
        return srchDept;
    }

    public void setSrchDept(String srchDept) {
        this.srchDept = srchDept;
    }

    public String getSrchStateOfOrigin() {
        return srchStateOfOrigin;
    }

    public void setSrchStateOfOrigin(String srchStateOfOrigin) {
        this.srchStateOfOrigin = srchStateOfOrigin;
    }

    public String getSrchCadre() {
        return srchCadre;
    }

    public void setSrchCadre(String srchCadre) {
        this.srchCadre = srchCadre;
    }

    public String getSrchRank() {
        return srchRank;
    }

    public void setSrchRank(String srchRank) {
        this.srchRank = srchRank;
    }

    public String getSrchGradeLevel() {
        return srchGradeLevel;
    }

    public void setSrchGradeLevel(String srchGradeLevel) {
        this.srchGradeLevel = srchGradeLevel;
    }

    public List<PostingDTO> getRecordList() {
        if (recordList == null) {
            recordList = new ArrayList<>();
        }
        return recordList;
    }

    public void setRecordList(List<PostingDTO> recordList) {
        this.recordList = recordList;
    }

}
