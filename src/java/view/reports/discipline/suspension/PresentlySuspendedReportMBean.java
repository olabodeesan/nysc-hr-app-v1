/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.reports.discipline.suspension;

import app.exceptions.AppException;
import dto.employees.EmployeeDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import service.reports.SuspensionReportService;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class PresentlySuspendedReportMBean implements Serializable{

    private List<EmployeeDTO> recordList;
    private EmployeeDTO selectedRecord;
    private String srchFileNo;
    private String srchSurname, srchOtherNames, srchGender, srchMaritalStatus, srchLocation, srchDept, srchStateOfOrigin;
    private String srchLga, srchCadre, srchRank, srchGradeLevel, srchGradeStep, srchPhone;
    private String currentSelectQuery;
    private StringBuilder selectQueryBuilder, generalStringBuilder;
    private final ViewHelper viewHelper;
    private final SuspensionReportService service;

    public PresentlySuspendedReportMBean() {
        service = new SuspensionReportService();
        viewHelper = new ViewHelper();

        initSearchQuery(true, false, true);
    }

    private void initSearchQuery(boolean isForInit, boolean isForSearch, boolean shouldFetch) {

        selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append(service.getEmployeeSelectQuery())
                .append("where ");

        if (isForInit) {
            selectQueryBuilder.append(service.getSuspendedListClause()).append(" order by e._temp_sort ");
            currentSelectQuery = selectQueryBuilder.toString();

            if (shouldFetch) {
                fillResults();
            }
        }
    }

    public void searchAction(ActionEvent event) {

        initSearchQuery(false, true, false);

        List<String> criteria = new ArrayList<>();

        //compulsory criteria
        criteria.add(service.getSuspendedListClause());

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

}

