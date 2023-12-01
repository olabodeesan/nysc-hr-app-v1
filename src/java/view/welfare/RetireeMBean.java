/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.welfare;

import app.exceptions.AppException;
import dto.employees.FetchOptionsDTO;
import dto.welfare.RetireesDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import service.welfare.RetireeService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.WelfareValidator;

public class RetireeMBean implements Serializable {

    private List<RetireesDTO> recordList;

    private RetireesDTO selectedRecord;
    private RetireesDTO recordDto;
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
    private RetireeService service;

    Date dgApprovalDate, paymentDate;
    private String dgApprovalDateStr, paymentDateStr;

    private final FetchOptionsDTO fetchOptions;

    public RetireeMBean() {
        service = new RetireeService();
        viewHelper = new ViewHelper();

        fetchOptions = new FetchOptionsDTO();
        fetchOptions.setFetchAll(true);

        initSearchQuery(true, false, true);
    }

    private void initSearchQuery(boolean isForInit, boolean isForSearch, boolean shouldFetch) {
        String todayDate = DateTimeUtils.getActivityDate();
        countQueryBuilder = new StringBuilder();
        countQueryBuilder.append("select count(e.employee_rec_id) from employee e ")
                .append("where ");

        selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append(service.getEmployeeSelectQuery())
                .append("where ");

        if (isForInit) {
            countQueryBuilder.append(" e.employee_rec_id<>'0' and date_due_for_retirement <='")
                    .append(todayDate).append("'")
                    .append(" or (dg_approve_retiree_claim_status='PENDING'")
                    .append(" or retiree_payment_claim_status='UNPAID') ");
            currentCountQuery = countQueryBuilder.toString();

            selectQueryBuilder.append(" e.employee_rec_id<>'0' and date_due_for_retirement <='")
                    .append(todayDate).append("'")
                    .append(" or (dg_approve_retiree_claim_status='PENDING'")
                    .append(" or retiree_payment_claim_status='UNPAID') ")
                    .append("order by e.current_grade_level desc ");
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

    public void preSubmitPaymentDialog(ActionEvent ev) {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance();
        WelfareValidator validator = new WelfareValidator();
        if (paymentDate != null) {
            paymentDateStr = viewHelper.getDateAsYMDWithDashString(paymentDate);
            recordDto.setClaimPaymentDate(paymentDateStr);
        }
        boolean validation_outcome = validator.validateRetireeClaimPaymentStatus(context, recordDto);

        if (validation_outcome) {
            reqContext.update("submitPaymentDialogPanel");
            reqContext.execute("PF('submitPaymentDialog').show()");
        } else {
            reqContext.update("paymentDialogPanel");
        }
    }

    public void prepareApprovalByLink(String employeeRecordId) {
        if (!StringUtils.isBlank(employeeRecordId)) {
            try {
                recordDto = service.getEmployee(employeeRecordId);
                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("dgApprovalDialogPanel");
            } catch (Exception ex) {

            }
        }
    }

    public void preSubmitApproval(ActionEvent ev) {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance();

        WelfareValidator validator = new WelfareValidator();

        if (dgApprovalDate != null) {
            dgApprovalDateStr = viewHelper.getDateAsYMDWithDashString(dgApprovalDate);
            recordDto.setDgApprovalDate(dgApprovalDateStr);
        }

        boolean validation_outcome = validator.validateDgRetireeApprovalStatus(context, recordDto);

        if (validation_outcome) {
            reqContext.execute("PF('submitDgApprovalDialog').show()");
            reqContext.update("submitDgApprovalDialogPanel");
        } else {
            reqContext.update("dgApprovalDialogPanel");
        }

    }

    public void submitDgApprovalAction(ActionEvent ae) {
        FacesContext context = FacesContext.getCurrentInstance();
        String activityDate = DateTimeUtils.getActivityDate();
        String currentUser = viewHelper.getAppUser().getUsername();

        recordDto.setLastMod(activityDate);
        recordDto.setLastModBy(currentUser);
        WelfareValidator validator = new WelfareValidator();

        if (dgApprovalDate != null) {
            dgApprovalDateStr = viewHelper.getDateAsYMDWithDashString(dgApprovalDate);
            recordDto.setDgApprovalDate(dgApprovalDateStr);
        }

        boolean validation_outcome = validator.validateDgRetireeApprovalStatus(context, recordDto);

        if (validation_outcome) {

            try {
                boolean result = service.dgApproveClaim(recordDto);
                if (result) {
                    ViewHelper.addInfoMessage(context, null, "Claims Approval Successful.", null);
                    recordDto = null;
                    fillResults();
                } else {
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }
            } catch (Exception ex) {

            }

        }
    }

    public void preSubmitClaims(ActionEvent ae) {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance();

        boolean validation_outcome = true;//validator.validateRefundLoan(context, refundRecord);
        if (validation_outcome) {
            reqContext.update("submitDialogPanel");
            reqContext.execute("PF('submitDialog').show()");
        } else {
            reqContext.update("retireeClaimDialogPanel");
        }
    }

    public void submitAction(ActionEvent ae) {
        FacesContext context = FacesContext.getCurrentInstance();
        String activityDate = DateTimeUtils.getActivityDate();
        String currentUser = viewHelper.getAppUser().getUsername();

        recordDto.setLastMod(activityDate);
        recordDto.setLastModBy(currentUser);
        boolean validate = true;
        if (validate) {

            try {
                boolean result = service.processClaims(recordDto);
                if (result) {
                    ViewHelper.addInfoMessage(context, null, "Claims forwarded for DG Approval.", null);
                    recordDto = null;
                    fillResults();
                } else {
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }

            } catch (Exception ex) {

            }
        }
    }

    public void preparePaymentByLink(String employeeRecordId) {
        if (!StringUtils.isBlank(employeeRecordId)) {
            try {
                recordDto = service.getEmployee(employeeRecordId);
                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("paymentDialogPanel");

            } catch (Exception ex) {

            }
        }
    }

    public void submitPaymentAction(ActionEvent ae) {
        FacesContext context = FacesContext.getCurrentInstance();
        String activityDate = DateTimeUtils.getActivityDate();
        String currentUser = viewHelper.getAppUser().getUsername();

        recordDto.setLastMod(activityDate);
        recordDto.setLastModBy(currentUser);
        WelfareValidator validator = new WelfareValidator();

        if (paymentDate != null) {
            paymentDateStr = viewHelper.getDateAsYMDWithDashString(paymentDate);
            recordDto.setClaimPaymentDate(paymentDateStr);
        }
        boolean validation_outcome = validator.validateRetireeClaimPaymentStatus(context, recordDto);
        if (validation_outcome) {
            try {
                boolean result = service.claimsPayment(recordDto);
                if (result) {
                    ViewHelper.addInfoMessage(context, null, "Claims Payment Successful.", null);
                    recordDto = null;
                    fillResults();
                } else {
                    ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                }

            } catch (Exception ex) {

            }

        }
    }

    public void prepareRecordByLink(String employeeRecordId) {
        if (!StringUtils.isBlank(employeeRecordId)) {
            try {
                recordDto = service.getEmployee(employeeRecordId);

                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("retireeClaimDialogPanel");

            } catch (Exception ex) {

            }
        }
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
            recordList = (List<RetireesDTO>) result.get(AppConstants.RECORD_LIST);

        } catch (AppException exc) {
            System.out.println(exc.getMessage());
            currentPage = 0;
            totalRows = 0;
            recordList = null;

        }
    }

    public List<RetireesDTO> getRecordList() {
        if (recordList == null) {
            recordList = new ArrayList<>();
        }
        return recordList;
    }

    public void setRecordList(List<RetireesDTO> recordList) {
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

    public RetireesDTO getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(RetireesDTO selectedRecord) {
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

    public RetireesDTO getRecordDto() {
        if (recordDto == null) {
            recordDto = new RetireesDTO();
        }
        return recordDto;
    }

    public void setRecordDto(RetireesDTO recordDto) {
        this.recordDto = recordDto;
    }

    public Date getDgApprovalDate() {
        return dgApprovalDate;
    }

    public void setDgApprovalDate(Date dgApprovalDate) {
        this.dgApprovalDate = dgApprovalDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getDgApprovalDateStr() {
        return dgApprovalDateStr;
    }

    public void setDgApprovalDateStr(String dgApprovalDateStr) {
        this.dgApprovalDateStr = dgApprovalDateStr;
    }

    public String getPaymentDateStr() {
        return paymentDateStr;
    }

    public void setPaymentDateStr(String paymentDateStr) {
        this.paymentDateStr = paymentDateStr;
    }

}
