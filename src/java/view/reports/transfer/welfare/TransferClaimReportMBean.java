/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.reports.transfer.welfare;

import dto.welfare.WelfareClaimReportDTO;
import java.io.Serializable;
import java.util.Date;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import service.reports.TransferWelfareReportService;
import view.utils.ViewHelper;

/**
 *
 * @author Gainsolutions
 */
public class TransferClaimReportMBean implements Serializable {

    WelfareClaimReportDTO recordDto;
    private TransferWelfareReportService service;
    private Date searchStartDate, searchEndDate;
    private String searchStartDateStr, searchEndDateStr;
    private final ViewHelper viewHelper;

    public TransferClaimReportMBean() {
        service = new TransferWelfareReportService();
viewHelper=new ViewHelper();
        // fillResults();
    }

    private void fillResults() {
        try {
         //   recordDto = service.getTransferClaimPendingDGApprovalReport();
        } catch (Exception ex) {

        }
    }

    public void searchAction(ActionEvent ev) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (searchStartDate != null) {
            searchStartDateStr = viewHelper.getDateAsYMDWithDashString(searchStartDate);
        }
        if (searchEndDate == null) {
            searchEndDate = searchStartDate;
        }
        searchStartDateStr = viewHelper.getDateAsYMDWithDashString(searchStartDate);
        searchEndDateStr = viewHelper.getDateAsYMDWithDashString(searchEndDate);

        try {
            recordDto = service.getTransferClaimPendingDGApprovalReport(searchStartDateStr, searchEndDateStr);
        } catch (Exception ex) {

        }

    }

    public WelfareClaimReportDTO getRecordDto() {
        if (recordDto == null) {
            recordDto = new WelfareClaimReportDTO();
        }
        return recordDto;
    }

    public void setRecordDto(WelfareClaimReportDTO recordDto) {
        this.recordDto = recordDto;
    }

    public Date getSearchStartDate() {
        return searchStartDate;
    }

    public void setSearchStartDate(Date searchStartDate) {
        this.searchStartDate = searchStartDate;
    }

    public Date getSearchEndDate() {
        return searchEndDate;
    }

    public void setSearchEndDate(Date searchEndDate) {
        this.searchEndDate = searchEndDate;
    }

    public String getSearchStartDateStr() {
        return searchStartDateStr;
    }

    public void setSearchStartDateStr(String searchStartDateStr) {
        this.searchStartDateStr = searchStartDateStr;
    }

    public String getSearchEndDateStr() {
        return searchEndDateStr;
    }

    public void setSearchEndDateStr(String searchEndDateStr) {
        this.searchEndDateStr = searchEndDateStr;
    }

}
