/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.reports;

import app.exceptions.AppException;
import dto.welfare.NewlyPostedWelfareDTO;
import dto.welfare.WelfareClaimReportDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import service.utils.DBUtil;
import service.utils.ServiceUtil;

/**
 *
 * @author nysc-ict-11
 */
public class TransferWelfareReportService implements Serializable {

    private String welfareSelectQuery;

    private ServiceUtil serviceUtil;
    private DBUtil dBUtil;

    public TransferWelfareReportService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
        initSelectQuery();
    }

    private void initSelectQuery() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select e.employee_rec_id,e.file_no, e.surname, e.other_names,e.current_grade_level ")
                .append(" ,empLoc.employee_rec_id,empLoc.record_id ")
                .append(" ,empLoc.posting_reason, spr.description as _posting_reason_desc ")
                .append(" ,empLoc.dg_approval_claim_status ")
                .append(" ,empLoc.dg_approval_date, date_format(empLoc.dg_approval_date,'%d/%m/%Y') as _dg_approval_date")
                .append(" ,empLoc.claim_payment_status ")
                .append(" ,empLoc.date_of_payment,date_format(empLoc.date_of_payment,'%d/%m/%Y') as _payment_date_desc")
                .append(" ,empLoc.total_claim_amount,format(empLoc.total_claim_amount,2) as _claim_desc")
                .append(" ,e.current_location, curloc.description as _curr_loc_desc ")
                .append(" ,empLoc.location_from, sloc.description as _prev_loc_desc ")
                .append(" ,empLoc.location, slo.description as _to_loc_desc ")
                .append(" ,e.current_location, curloc.description as _curr_loc_desc ")
                .append(" ,empLoc.date_of_posting, date_format(empLoc.date_of_posting,'%d/%m/%Y') as _effective_Date ")
                .append(" ,(SELECT kilometers FROM setup_location_distance ")
                .append(" where (location_from=empLoc.location and location_to=empLoc.location_from) ")
                .append(" or (location_from=empLoc.location_from and location_to=empLoc.location )) as _kilometers ")
                .append(" from employee_location empLoc ")
                .append("join employee e on e.employee_rec_id = empLoc.employee_rec_id ")
                .append("join setup_states curloc on e.current_location = curloc.record_id ")
                .append("join setup_states sloc on empLoc.location_from = sloc.record_id ")
                .append("join setup_states slo on empLoc.location = slo.record_id ")
                .append("join setup_posting_reason spr on empLoc.posting_reason=spr.record_id ");

        welfareSelectQuery = strBuilder.toString();
    }

    public String getWelfareSelectQuery() {
        return welfareSelectQuery;
    }

    public WelfareClaimReportDTO getTransferClaimPendingDGApprovalReport(String startDate, String endDate)
            throws AppException {

        WelfareClaimReportDTO result = new WelfareClaimReportDTO();
        NewlyPostedWelfareDTO dto = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        double cummulativeClaim = 0;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getWelfareSelectQuery());
            strBuilder.append(" where empLoc.dg_approval_claim_status='PENDING' ")
                    .append(" and empLoc.claim_payment_status='UNPAID'")
                    .append(" and empLoc.date_of_posting between ? and ? ");
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, startDate);
            prep_stmt.setString(2, endDate);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                dto = new NewlyPostedWelfareDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                dto.setFileNo(result_set.getString("file_no"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));
                dto.setGrdadeLevel(result_set.getString("current_grade_level"));
                dto.setPostingReason(result_set.getString("posting_reason"));
                dto.setPostingReasonDesc(result_set.getString("_posting_reason_desc"));
                dto.setNewLocation(result_set.getString("location"));
                dto.setNewLocationDesc(result_set.getString("_to_loc_desc"));
                dto.setPrevLocation(result_set.getString("location_from"));
                dto.setPrevLocationDesc(result_set.getString("_prev_loc_desc"));
                dto.setEffectiveDate(result_set.getString("date_of_posting"));
                dto.setEffectiveDateDesc(result_set.getString("_effective_Date"));
                dto.setKilometres(result_set.getString("_kilometers"));

                dto.setDgApprovalClaimStatus(result_set.getString("dg_approval_claim_status"));
                dto.setDgApprovalDate(result_set.getString("dg_approval_date"));
                dto.setDgApprovalDateDesc(result_set.getString("_dg_approval_date"));

                dto.setClaimPaymentStatus(result_set.getString("claim_payment_status"));
                dto.setClaimPaymentDate(result_set.getString("date_of_payment"));
                dto.setClaimPaymentDateDesc(result_set.getString("_payment_date_desc"));

                dto.setTotalClaimAmount(result_set.getString("total_claim_amount"));
                dto.setTotalClaimAmountDesc(result_set.getString("_claim_desc"));

                result.addNewTransferClaim(dto);

                cummulativeClaim += Double.parseDouble(result_set.getString("total_claim_amount"));
            }
            if(result.getNewtransferClaim().size()>0){
                result.setPendingDgApprovalClaim(true);
            }

            DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
            formatter.applyPattern("###,###,###,###,###,##0.00");
            result.setCummulativeTotalClaimDesc(formatter.format(cummulativeClaim));

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public WelfareClaimReportDTO getTransferClaimApprovedByDGReport(String startDate, String endDate)
            throws AppException {

        WelfareClaimReportDTO result = new WelfareClaimReportDTO();
        NewlyPostedWelfareDTO dto = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        double cummulativeClaim = 0;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getWelfareSelectQuery());
            strBuilder.append(" where empLoc.dg_approval_claim_status='APPROVED' ")
                    .append(" and empLoc.claim_payment_status='UNPAID'")
                    .append(" and empLoc.dg_approval_date between ? and ? ");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, startDate);
            prep_stmt.setString(2, endDate);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                dto = new NewlyPostedWelfareDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                dto.setFileNo(result_set.getString("file_no"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));
                dto.setGrdadeLevel(result_set.getString("current_grade_level"));
                dto.setPostingReason(result_set.getString("posting_reason"));
                dto.setPostingReasonDesc(result_set.getString("_posting_reason_desc"));
                dto.setNewLocation(result_set.getString("location"));
                dto.setNewLocationDesc(result_set.getString("_to_loc_desc"));
                dto.setPrevLocation(result_set.getString("location_from"));
                dto.setPrevLocationDesc(result_set.getString("_prev_loc_desc"));
                dto.setEffectiveDate(result_set.getString("date_of_posting"));
                dto.setEffectiveDateDesc(result_set.getString("_effective_Date"));
                dto.setKilometres(result_set.getString("_kilometers"));

                dto.setDgApprovalClaimStatus(result_set.getString("dg_approval_claim_status"));
                dto.setDgApprovalDate(result_set.getString("dg_approval_date"));
                dto.setDgApprovalDateDesc(result_set.getString("_dg_approval_date"));

                dto.setClaimPaymentStatus(result_set.getString("claim_payment_status"));
                dto.setClaimPaymentDate(result_set.getString("date_of_payment"));
                dto.setClaimPaymentDateDesc(result_set.getString("_payment_date_desc"));

                dto.setTotalClaimAmount(result_set.getString("total_claim_amount"));
                dto.setTotalClaimAmountDesc(result_set.getString("_claim_desc"));

                result.addApprovedTransferClaim(dto);

                cummulativeClaim += Double.parseDouble(result_set.getString("total_claim_amount"));
            }

            if (result.getApprovedTransferClaim().size() > 0) {
                result.setTransferclaimApproved(true);
            }

            System.out.println(result.isTransferclaimApproved());

            DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
            formatter.applyPattern("###,###,###,###,###,##0.00");
            result.setCummulativeTotalClaimDesc(formatter.format(cummulativeClaim));

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public WelfareClaimReportDTO getUnpaidTransferClaimReport(String startDate, String endDate)
            throws AppException {

        WelfareClaimReportDTO result = new WelfareClaimReportDTO();
        NewlyPostedWelfareDTO dto = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        double cummulativeClaim = 0;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getWelfareSelectQuery());
            strBuilder.append(" where empLoc.dg_approval_claim_status='APPROVED' ")
                    .append(" or empLoc.claim_payment_status='UNPAID'")
                    .append(" and empLoc.dg_approval_date between ? and ? ");
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, startDate);
            prep_stmt.setString(2, endDate);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                dto = new NewlyPostedWelfareDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                dto.setFileNo(result_set.getString("file_no"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));
                dto.setGrdadeLevel(result_set.getString("current_grade_level"));
                dto.setPostingReason(result_set.getString("posting_reason"));
                dto.setPostingReasonDesc(result_set.getString("_posting_reason_desc"));
                dto.setNewLocation(result_set.getString("location"));
                dto.setNewLocationDesc(result_set.getString("_to_loc_desc"));
                dto.setPrevLocation(result_set.getString("location_from"));
                dto.setPrevLocationDesc(result_set.getString("_prev_loc_desc"));
                dto.setEffectiveDate(result_set.getString("date_of_posting"));
                dto.setEffectiveDateDesc(result_set.getString("_effective_Date"));
                dto.setKilometres(result_set.getString("_kilometers"));

                dto.setDgApprovalClaimStatus(result_set.getString("dg_approval_claim_status"));
                dto.setDgApprovalDate(result_set.getString("dg_approval_date"));
                dto.setDgApprovalDateDesc(result_set.getString("_dg_approval_date"));

                dto.setClaimPaymentStatus(result_set.getString("claim_payment_status"));
                dto.setClaimPaymentDate(result_set.getString("date_of_payment"));
                dto.setClaimPaymentDateDesc(result_set.getString("_payment_date_desc"));

                dto.setTotalClaimAmount(result_set.getString("total_claim_amount"));
                dto.setTotalClaimAmountDesc(result_set.getString("_claim_desc"));

                result.addUnpaidTransferClaim(dto);

                cummulativeClaim += Double.parseDouble(result_set.getString("total_claim_amount"));
            }
            if(result.getUnpaidTransferClaim().size()>0){
                result.setUnpaidTransfer(true);
            }

            DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
            formatter.applyPattern("###,###,###,###,###,##0.00");
            result.setCummulativeTotalClaimDesc(formatter.format(cummulativeClaim));

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public WelfareClaimReportDTO getPaidTransferClaimReport(String startDate, String endDate)
            throws AppException {

        WelfareClaimReportDTO result = new WelfareClaimReportDTO();
        NewlyPostedWelfareDTO dto = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        double cummulativeClaim = 0;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getWelfareSelectQuery());
            strBuilder.append(" where empLoc.dg_approval_claim_status='APPROVED' ")
                    .append(" and empLoc.claim_payment_status='PAID'")
                    .append(" and empLoc.date_of_payment between ? and ? ");
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, startDate);
            prep_stmt.setString(2, endDate);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                dto = new NewlyPostedWelfareDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                dto.setFileNo(result_set.getString("file_no"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));
                dto.setGrdadeLevel(result_set.getString("current_grade_level"));
                dto.setPostingReason(result_set.getString("posting_reason"));
                dto.setPostingReasonDesc(result_set.getString("_posting_reason_desc"));
                dto.setNewLocation(result_set.getString("location"));
                dto.setNewLocationDesc(result_set.getString("_to_loc_desc"));
                dto.setPrevLocation(result_set.getString("location_from"));
                dto.setPrevLocationDesc(result_set.getString("_prev_loc_desc"));
                dto.setEffectiveDate(result_set.getString("date_of_posting"));
                dto.setEffectiveDateDesc(result_set.getString("_effective_Date"));
                dto.setKilometres(result_set.getString("_kilometers"));

                dto.setDgApprovalClaimStatus(result_set.getString("dg_approval_claim_status"));
                dto.setDgApprovalDate(result_set.getString("dg_approval_date"));
                dto.setDgApprovalDateDesc(result_set.getString("_dg_approval_date"));

                dto.setClaimPaymentStatus(result_set.getString("claim_payment_status"));
                dto.setClaimPaymentDate(result_set.getString("date_of_payment"));
                dto.setClaimPaymentDateDesc(result_set.getString("_payment_date_desc"));

                dto.setTotalClaimAmount(result_set.getString("total_claim_amount"));
                dto.setTotalClaimAmountDesc(result_set.getString("_claim_desc"));

                result.addPaidTransferClaim(dto);

                cummulativeClaim += Double.parseDouble(result_set.getString("total_claim_amount"));
            }
            
             if(result.getPaidTransferClaim().size()>0){
                result.setPaidTransfer(true);
            }


            DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
            formatter.applyPattern("###,###,###,###,###,##0.00");
            result.setCummulativeTotalClaimDesc(formatter.format(cummulativeClaim));

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

}
