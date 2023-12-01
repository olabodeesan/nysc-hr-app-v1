/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.setup;

import app.exceptions.AppException;
import dto.setup.DepartmentDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import service.setup.DepartmentService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.SetupValidator;

/**
 *
 * @author IronHide
 */
public class DepartmentMBean implements Serializable{

    private DepartmentDTO newRecord;
    private List<DepartmentDTO> recordList;
    private DepartmentDTO selectedRecord;
    private DepartmentDTO editRecord;
    private String srchDescription;
    private String srchStatus;
    private String currentCountQuery;
    private String currentSelectQuery;
    private StringBuilder countQueryBuilder, selectQueryBuilder, generalStringBuilder;
    private int totalRows = 0;
    private int currentPage = 0;
    private int startRow;
    private int pageSize = 35;
    private String paginationDescription;
    private ViewHelper viewHelper;
    private DepartmentService service;

    public DepartmentMBean() {
        service = new DepartmentService();
        viewHelper = new ViewHelper();

        initSearchQuery(true, false, true);
    }

    private void initSearchQuery(boolean isForInit, boolean isForSearch, boolean shouldFetch) {

        countQueryBuilder = new StringBuilder();
        countQueryBuilder.append("select count(d.record_id) from setup_departments d ")
                .append("where ");

        selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append("select d.record_id,d.description,d.record_status ")
                .append(" from setup_departments d ")
                .append("where ");

        if (isForInit) {
            countQueryBuilder.append(" d.record_id<>'0' ");
            currentCountQuery = countQueryBuilder.toString();

            selectQueryBuilder.append(" d.record_id<>'0' order by d.description ");
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

    public void prepareNewAction() {
        newRecord = null;
    }

    public void createAction(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        SetupValidator validator = new SetupValidator();

        boolean validation_outcome = validator.validateDepartment(context, newRecord);

        if (validation_outcome) {
            String activityDate = DateTimeUtils.getActivityDate();
            String currentUser = viewHelper.getAppUser().getUsername();

            newRecord.setCreated(activityDate);
            newRecord.setCreatedBy(currentUser);
            newRecord.setLastMod(activityDate);
            newRecord.setLastModBy(currentUser);

            try {
                boolean result = service.createDepartment(newRecord);
                if (result) {
                    ViewHelper.addInfoMessage(context, null, "Record Saved Successfully. You may enter another record, or close the dialog.", null);
                    newRecord = null;
                    viewHelper.removeApplicationAttribute(AppConstants.APP_SCOPE_DEPARTMENTS);

                    fillResults();
                } else {
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
                String msg = exc.getMessage();
                if (StringUtils.equals(msg, AppConstants.DUPLICATE)) {
                    ViewHelper.addErrorMessage(context, null, "Error: Description already exists.", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            }
        }
    }

    public void prepareUpdateByLink(String row_record_id) {
        if (!StringUtils.isBlank(row_record_id)) {
            try {
                editRecord = service.getDepartment(row_record_id);
                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("editDialogPanel");
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
            }
        } else {
            editRecord = null;
        }
    }

    public void updateAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        SetupValidator validator = new SetupValidator();

        RequestContext reqContext = RequestContext.getCurrentInstance();
        boolean validation_outcome = validator.validateDepartment(context, editRecord);
        if (validation_outcome) {
            String activityDate = DateTimeUtils.getActivityDate();
            String currentUser = viewHelper.getAppUser().getUsername();

            editRecord.setLastMod(activityDate);
            editRecord.setLastModBy(currentUser);

            try {
                boolean result = service.updateDepartment(editRecord);
                if (result) {
                    ViewHelper.addInfoMessage(context, null, "Record Updated Successfully. Close this dialog", null);
                    editRecord = null;
                    viewHelper.removeApplicationAttribute(AppConstants.APP_SCOPE_DEPARTMENTS);

                    reqContext.addCallbackParam("updateresult", true);

                    fillResults();
                } else {
                    reqContext.addCallbackParam("updateresult", false);
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
                reqContext.addCallbackParam("updateresult", false);
                String msg = exc.getMessage();
                if (StringUtils.equals(msg, AppConstants.DUPLICATE)) {
                    ViewHelper.addErrorMessage(context, null, "Error: Description already exists.", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            }
        } else {
            reqContext.addCallbackParam("updateresult", false);
        }
    }

    public void searchAction(ActionEvent event) {

        initSearchQuery(false, true, false);

        List<String> criteria = new ArrayList<>();

        //criteria.add("d.host_staff_record_id = " + "'" + ViewHelper.fixSqlFieldValue(viewHelper.getCurrentUser().getUsername()) + "' ");
        if (!StringUtils.isBlank(srchDescription)) {
            criteria.add("d.description like " + "'%" + ViewHelper.fixSqlFieldValue(srchDescription) + "%' ");
        }

        if (!StringUtils.isBlank(srchStatus)) {
            criteria.add("d.record_status = " + "'" + ViewHelper.fixSqlFieldValue(srchStatus) + "' ");
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
            selectQueryBuilder.append(" order by d.description desc");

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
            recordList = (List<DepartmentDTO>) result.get(AppConstants.RECORD_LIST);

        } catch (AppException exc) {
            System.out.println(exc.getMessage());
            currentPage = 0;
            totalRows = 0;
            recordList = null;

        }
    }

    public List<DepartmentDTO> getRecordList() {
        if (recordList == null) {
            recordList = new ArrayList<>();
        }
        return recordList;
    }

    public void setRecordList(List<DepartmentDTO> recordList) {
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

    public DepartmentDTO getNewRecord() {
        if (newRecord == null) {
            newRecord = new DepartmentDTO();
        }
        return newRecord;
    }

    public void setNewRecord(DepartmentDTO newRecord) {
        this.newRecord = newRecord;
    }

    public DepartmentDTO getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(DepartmentDTO selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public DepartmentDTO getEditRecord() {
        if (editRecord == null) {
            editRecord = new DepartmentDTO();
        }
        return editRecord;
    }

    public void setEditRecord(DepartmentDTO editRecord) {
        this.editRecord = editRecord;
    }

    public String getSrchDescription() {
        return srchDescription;
    }

    public void setSrchDescription(String srchDescription) {
        this.srchDescription = srchDescription;
    }

    public String getSrchStatus() {
        return srchStatus;
    }

    public void setSrchStatus(String srchStatus) {
        this.srchStatus = srchStatus;
    }

}

