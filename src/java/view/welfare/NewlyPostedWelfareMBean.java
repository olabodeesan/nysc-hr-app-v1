/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.welfare;

import app.exceptions.AppException;
import dto.welfare.NewlyPostedWelfareDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import service.welfare.NewlyPostedWelfareService;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.WelfareValidator;

/**
 *
 * @author Gainsolutions
 */
public class NewlyPostedWelfareMBean implements Serializable {

    private List<NewlyPostedWelfareDTO> recordList;
    //  private EmployeeDTO selectedRecord;

    private NewlyPostedWelfareDTO selectedRecord;
    private NewlyPostedWelfareDTO recordDto;
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
    private NewlyPostedWelfareService service;

    Date dgApprovalDate, paymentDate;
    private String dgApprovalDateStr, paymentDateStr;

    public NewlyPostedWelfareMBean() {
        service = new NewlyPostedWelfareService();
        viewHelper = new ViewHelper();

        initSearchQuery(true, false, true);
    }

    private void initSearchQuery(boolean isForInit, boolean isForSearch, boolean shouldFetch) {
        countQueryBuilder = new StringBuilder();
        countQueryBuilder.append("select count(empLoc.record_id) from employee_location empLoc ")
                .append("join employee e on e.employee_rec_id = empLoc.employee_rec_id ")
                .append("join setup_states curloc on e.current_location = curloc.record_id ")
                .append("join setup_states sloc on empLoc.location_from = sloc.record_id ")
                .append("join setup_states slo on empLoc.location = slo.record_id ")
                .append("join setup_posting_reason spr on empLoc.posting_reason=spr.record_id ")
                .append("where ");

        selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append(service.getWelfareSelectQuery())
                .append("where ");

        if (isForInit) {
            countQueryBuilder.append(" empLoc.record_id<>'0' ")
                    .append(" and (empLoc.dg_approval_claim_status='PENDING' ")
                    .append(" or empLoc.claim_payment_status='UNPAID') ");

            currentCountQuery = countQueryBuilder.toString();

            selectQueryBuilder.append(" empLoc.record_id<>'0' ")
                    .append(" and (empLoc.dg_approval_claim_status='PENDING' ")
                    .append(" or empLoc.claim_payment_status='UNPAID') ");
            currentSelectQuery = selectQueryBuilder.toString();

            if (shouldFetch) {
                fillResults();
            }

        }

    }

    public void searchAction(ActionEvent event) {
        initSearchQuery(false, true, false);

        List<String> criteria = new ArrayList<>();

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
            criteria.add("e.current_location like " + "'%" + ViewHelper.fixSqlFieldValue(srchLocation) + "%' ");
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
            selectQueryBuilder.append(" order by empLoc.record_id ");

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
            recordList = (List<NewlyPostedWelfareDTO>) result.get(AppConstants.RECORD_LIST);

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

    public void prepareApprovalByLink(String employeeRecordId) {
        if (!StringUtils.isBlank(employeeRecordId)) {
            try {
                recordDto = service.getPostingRecord(employeeRecordId);
                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("dgApprovalDialogPanel");
            } catch (Exception ex) {

            }
        }
    }

    public void preSubmitApproval(ActionEvent ev) {
        FacesContext context = FacesContext.getCurrentInstance();

        WelfareValidator validator = new WelfareValidator();

        dgApprovalDateStr = viewHelper.getDateAsYMDWithDashString(dgApprovalDate);
        recordDto.setDgApprovalDate(dgApprovalDateStr);

        RequestContext reqContext = RequestContext.getCurrentInstance();

        boolean validation_outcome = validator.validateDgApprovalStatus(context, recordDto);

        if (validation_outcome) {
            reqContext.execute("PF('submitApprovalDialog').show()");
            reqContext.update("submitApprovalDialogPanel");

        } else {
            reqContext.update("dgApprovalDialogPanel");
        }
    }

    public void dgApprovalAction(ActionEvent ae) {
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

        boolean validate = validator.validateDgApprovalStatus(context, recordDto);
        if (validate) {
            try {
                boolean result = service.dgApproveClaim(recordDto);
                if (result) {
                    ViewHelper.addInfoMessage(context, null, "Claims Approval Successful.", null);

                    fillResults();
                    recordDto = null;
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
                recordDto = service.getPostingRecord(employeeRecordId);
                RequestContext reqContext = RequestContext.getCurrentInstance();
                reqContext.update("paymentDialogPanel");

            } catch (Exception ex) {

            }
        }
    }

    public void preSubmitPayment(ActionEvent ev) {
        FacesContext context = FacesContext.getCurrentInstance();

        WelfareValidator validator = new WelfareValidator();

        paymentDateStr = viewHelper.getDateAsYMDWithDashString(paymentDate);
        recordDto.setClaimPaymentDate(paymentDateStr);

        RequestContext reqContext = RequestContext.getCurrentInstance();

        boolean validation_outcome = validator.validateTransferClaimPaymentStatus(context, recordDto);

        if (validation_outcome) {
            reqContext.execute("PF('submitPaymentDialog').show()");
            reqContext.update("submitPaymentDialogPanel");

        } else {
            reqContext.update("paymentDialogPanel");
        }
    }

    public void paymentAction(ActionEvent ae) {
        FacesContext context = FacesContext.getCurrentInstance();

        String activityDate = DateTimeUtils.getActivityDate();
        String currentUser = viewHelper.getAppUser().getUsername();
        recordDto.setLastMod(activityDate);
        recordDto.setLastModBy(currentUser);

        WelfareValidator validator = new WelfareValidator();
        if (paymentDate != null) {
            paymentDateStr = viewHelper.getDateAsYMDWithDashString(paymentDate);
            recordDto.setClaimPaymentDate(paymentDateStr);

            boolean validate = validator.validateTransferClaimPaymentStatus(context, recordDto);
            if (validate) {
                try {
                    boolean result = service.claimsPayment(recordDto);
                    if (result) {
                        ViewHelper.addInfoMessage(context, null, "Claims Payment Successful.", null);
                         fillResults(); 
                        recordDto = null;
                      
                    } else {
                        ViewHelper.addErrorMessage(context, null, "An error occured. Try Again.", null);
                    }
                } catch (Exception ex) {

                }
            }
        }
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

    public NewlyPostedWelfareDTO getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(NewlyPostedWelfareDTO selectedRecord) {
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

    public List<NewlyPostedWelfareDTO> getRecordList() {
        if (recordList == null) {
            recordList = new ArrayList<>();
        }
        return recordList;
    }

    public void setRecordList(List<NewlyPostedWelfareDTO> recordList) {
        this.recordList = recordList;
    }

    public NewlyPostedWelfareDTO getRecordDto() {
        if (recordDto == null) {
            recordDto = new NewlyPostedWelfareDTO();
        }
        return recordDto;
    }

    public void setRecordDto(NewlyPostedWelfareDTO recordDto) {
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
