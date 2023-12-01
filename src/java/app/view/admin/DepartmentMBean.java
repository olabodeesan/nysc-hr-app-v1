/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.view.admin;

import app.dto.DepartmentDTO;
import app.exceptions.AppException;
import app.service.admin.DepartmentService;
import app.view.utils.AppConstants;
import app.view.utils.DateTimeUtils;
import app.view.utils.ViewHelper;
import app.view.validation.DepartmentValidator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author GAINSolutions
 */
public class DepartmentMBean implements Serializable {

    private DepartmentDTO newRecord;
    private List<DepartmentDTO> recordList;
    private DepartmentDTO selectedRecord;
    private DepartmentDTO editRecord;
    private String srchDescription;
    private String srchRecordStatus;
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

    /**
     * Creates a new instance of DepartmentMBean
     */
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
        selectQueryBuilder.append("select d.record_id,d.description")
                .append(" ,d.record_status from setup_departments d ")
                .append("where ");

        if (isForInit) {
            countQueryBuilder.append(" d.record_id<>'0' ");
            currentCountQuery = countQueryBuilder.toString();

            selectQueryBuilder.append(" d.record_id<>'0' ");
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

    public void createAction(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();

        DepartmentValidator validator = new DepartmentValidator();
        boolean validation_outcome = validator.validateNew(context, newRecord);

        if (validation_outcome) {
            String activityDate = DateTimeUtils.getActivityDate();
            String currentUser = viewHelper.getAppUser().getUsername();

            newRecord.setCreated(activityDate);
            newRecord.setCreatedBy(currentUser);
            newRecord.setLastMod(activityDate);
            newRecord.setLastModBy(currentUser);

            try {
                boolean result = service.createRecord(newRecord);
                if (result) {
                    ViewHelper.addInfoMessage(context, null, "Record Saved Successfully. You may enter another record, or close the dialog.", null);
                    newRecord = null;

                    viewHelper.removeApplicationAttribute(AppConstants.APP_SCOPE_DEPT);

                    fillResults();
                } else {
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            } catch (Exception exc) {
                String msg = exc.getMessage();
                if (StringUtils.equals(msg, AppConstants.DUPLICATE)) {
                    ViewHelper.addErrorMessage(context, null, "Error: Description already exists.", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            }
        }
    }

    public void updateAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        if (!StringUtils.isBlank(editRecord.getRecordId())) {

            DepartmentValidator validator = new DepartmentValidator();
            boolean validation_outcome = validator.validateNew(context, editRecord);
            if (validation_outcome) {
                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();

                editRecord.setLastMod(activityDate);
                editRecord.setLastModBy(currentUser);

                RequestContext request_context = RequestContext.getCurrentInstance();

                try {
                    boolean result = service.updateRecord(editRecord);
                    if (result) {
                        ViewHelper.addInfoMessage(context, null, "Record Updated Successfully. Close this dialog", null);
                        editRecord = null;

                        viewHelper.removeApplicationAttribute(AppConstants.APP_SCOPE_DEPT);

                        fillResults();

                        request_context.addCallbackParam("updateresult", true);    //basic parameter

                    } else {
                        ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                        request_context.addCallbackParam("updateresult", false);
                    }
                } catch (Exception exc) {
                    String msg = exc.getMessage();
                    if (StringUtils.equals(msg, AppConstants.DUPLICATE_NAME)) {
                        ViewHelper.addErrorMessage(context, null, "Error: Name already exists.", null);
                    } else {
                        ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                    }
                }
            }
        } else {
            ViewHelper.addErrorMessage(context, null, "No record selected", null);
        }
    }

    public void preparenewAction() {
        newRecord = new DepartmentDTO();
    }

    public void prepareUpdate(ActionEvent event) {
        if (selectedRecord != null) {
            try {
                editRecord = service.getRecord(selectedRecord.getRecordId());
            } catch (Exception exc) {
            }
        } else {
            editRecord = null;
        }
    }

    public void prepareUpdateByLink(String recordId) {
        if (recordId != null) {
            try {
                editRecord = service.getRecord(recordId);
                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("editDialogPanel");
            } catch (Exception exc) {
            }
        } else {
            editRecord = null;
        }
    }

    public void searchAction(ActionEvent event) {

        initSearchQuery(false, true, false);

        List<String> criteria = new ArrayList<>();

        if (!StringUtils.isBlank(srchDescription)) {
            criteria.add("d.description like " + "'" + ViewHelper.fixSqlFieldValue(srchDescription) + "' ");
        }

        if (!StringUtils.isBlank(srchRecordStatus)) {
            criteria.add("d.record_status like " + "'" + ViewHelper.fixSqlFieldValue(srchRecordStatus) + "' ");
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
            selectQueryBuilder.append(" order by d.record_id");

            currentPage = 0;
            currentCountQuery = countQueryBuilder.toString();
            currentSelectQuery = selectQueryBuilder.toString();

            fillResults();
        }

    }

    private void fillResults() {
        startRow = currentPage * pageSize;
        //System.out.println(currentSelectQuery);
        try {
            Map<String, Object> result = service.searchRecords(currentCountQuery, currentSelectQuery, startRow, pageSize);
            totalRows = Integer.parseInt(result.get(AppConstants.TOTAL_RECORDS).toString());
            recordList = (List<DepartmentDTO>) result.get(AppConstants.RECORD_LIST);

        } catch (AppException exc) {
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

    public String getSrchDescription() {
        return srchDescription;
    }

    public void setSrchDescription(String srchDescription) {
        this.srchDescription = srchDescription;
    }

    public String getSrchRecordStatus() {
        return srchRecordStatus;
    }

    public void setSrchRecordStatus(String srchRecordStatus) {
        this.srchRecordStatus = srchRecordStatus;
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
}
