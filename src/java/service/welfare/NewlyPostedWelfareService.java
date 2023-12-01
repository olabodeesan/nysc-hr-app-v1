/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.welfare;

import app.exceptions.AppException;
import dto.welfare.NewlyPostedWelfareDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;

/**
 *
 * @author Gainsolutions
 */
public class NewlyPostedWelfareService implements Serializable {

    private String welfareSelectQuery;

    private ServiceUtil serviceUtil;
    private DBUtil dBUtil;

    public NewlyPostedWelfareService() {
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

    public Map<String, Object> searchRecords(String countQuery, String selectQuery, int startRow, int pagesize)
            throws AppException {

        Integer countInt = 0;
        Map<String, Object> result = new HashMap<>();
        List<NewlyPostedWelfareDTO> dto_list = new ArrayList<>();
        NewlyPostedWelfareDTO dto = null;
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String todayDate = DateTimeUtils.getActivityDate();
        ViewHelper viewHelper = new ViewHelper();

        String pagedQuery = selectQuery + " limit " + startRow + ", " + pagesize;
        try {

            db_conn = dBUtil.getDatasource().getConnection();

            prep_stmt = db_conn.prepareStatement(countQuery);
            result_set = prep_stmt.executeQuery();
            while (result_set.next()) {
                countInt = Integer.valueOf(result_set.getObject(1).toString());
            }

            prep_stmt = db_conn.prepareStatement(pagedQuery);
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

                //Check if the DG Retirement Approval Status              
                if (StringUtils.equalsIgnoreCase(dto.getDgApprovalClaimStatus(), AppConstants.APPROVED) || StringUtils.isBlank(dto.getDgApprovalClaimStatus())) {
                    dto.setClaimsApproved(true);
                } else if (StringUtils.equalsIgnoreCase(dto.getDgApprovalClaimStatus(), AppConstants.PENDING)) {
                    dto.setClaimsApproved(false);
                }//End Check DG Approve Claim

                dto.setDgApprovalDate(result_set.getString("dg_approval_date"));
                dto.setDgApprovalDateDesc(result_set.getString("_dg_approval_date"));

                //Check if the Retirement Status has been paid  
                dto.setClaimPaymentStatus(result_set.getString("claim_payment_status"));

                if (StringUtils.equalsIgnoreCase(dto.getClaimPaymentStatus(), AppConstants.PAID) || StringUtils.isBlank(dto.getClaimPaymentStatus())
                        || StringUtils.equalsIgnoreCase(dto.getDgApprovalClaimStatus(), AppConstants.PENDING)) {
                    dto.setClaimsPaid(true);
                } else if (StringUtils.equalsIgnoreCase(dto.getClaimPaymentStatus(), AppConstants.UNPAID)) {
                    dto.setClaimsPaid(false);
                }

                dto.setClaimPaymentDate(result_set.getString("date_of_payment"));
                dto.setClaimPaymentDateDesc(result_set.getString("_payment_date_desc"));

                dto.setTotalClaimAmount(result_set.getString("total_claim_amount"));
                dto.setTotalClaimAmountDesc(result_set.getString("_claim_desc"));

                dto_list.add(dto);

            }

        } catch (Exception exc) {
            exc.printStackTrace();
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }

        result.put(AppConstants.TOTAL_RECORDS, countInt);
        result.put(AppConstants.RECORD_LIST, dto_list);

        return result;
    }

    public NewlyPostedWelfareDTO getPostingRecord(String recordId)
            throws AppException {
        NewlyPostedWelfareDTO result = null;
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        String todayDate = DateTimeUtils.getActivityDate();
        ViewHelper viewHelper = new ViewHelper();

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            StringBuilder strBuilder = new StringBuilder();

            strBuilder.append(getWelfareSelectQuery())
                    .append(" where empLoc.record_id=? ")
                    .append("and (empLoc.is_recent_posting='Y'")
                    .append(" or empLoc.is_recent_posting='N') ");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, recordId);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = new NewlyPostedWelfareDTO();
                result.setRecordId(result_set.getString("record_id"));
                //     System.out.println(result.getRecordId());
                result.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                result.setFileNo(result_set.getString("file_no"));
                result.setSurname(result_set.getString("surname"));
                result.setOtherNames(result_set.getString("other_names"));
                result.setGrdadeLevel(result_set.getString("current_grade_level"));
                result.setPostingReason(result_set.getString("posting_reason"));
                result.setPostingReasonDesc(result_set.getString("_posting_reason_desc"));
                result.setNewLocation(result_set.getString("location"));
                result.setNewLocationDesc(result_set.getString("_to_loc_desc"));
                result.setPrevLocation(result_set.getString("location_from"));
                result.setPrevLocationDesc(result_set.getString("_prev_loc_desc"));
                result.setEffectiveDate(result_set.getString("date_of_posting"));
                result.setEffectiveDateDesc(result_set.getString("_effective_Date"));
                result.setKilometres(result_set.getString("_kilometers"));

                result.setDgApprovalClaimStatus(result_set.getString("dg_approval_claim_status"));
                //Check if the DG Retirement Approval Status              
                if (StringUtils.equalsIgnoreCase(result.getDgApprovalClaimStatus(), AppConstants.APPROVED) || StringUtils.isBlank(result.getDgApprovalClaimStatus())) {
                    result.setClaimsApproved(true);
                } else if (StringUtils.equalsIgnoreCase(result.getDgApprovalClaimStatus(), AppConstants.PENDING)) {
                    result.setClaimsApproved(false);
                }//End Check DG Approve Claim

                result.setDgApprovalDate(result_set.getString("dg_approval_date"));
                result.setDgApprovalDateDesc(result_set.getString("_dg_approval_date"));

                result.setClaimPaymentStatus(result_set.getString("claim_payment_status"));
                //Check if the Retirement Status has been paid  
                if (StringUtils.equalsIgnoreCase(result.getClaimPaymentStatus(), AppConstants.PAID) || StringUtils.isBlank(result.getClaimPaymentStatus())
                        || StringUtils.equalsIgnoreCase(result.getDgApprovalClaimStatus(), AppConstants.PENDING)) {
                    result.setClaimsPaid(true);
                } else if (StringUtils.equalsIgnoreCase(result.getClaimPaymentStatus(), AppConstants.UNPAID)) {
                    result.setClaimsPaid(false);
                }

                result.setClaimPaymentDate(result_set.getString("date_of_payment"));
                result.setClaimPaymentDateDesc(result_set.getString("_payment_date_desc"));

                result.setTotalClaimAmount(result_set.getString("total_claim_amount"));
                result.setTotalClaimAmountDesc(result_set.getString("_claim_desc"));
            }
        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean dgApproveClaim(NewlyPostedWelfareDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        StringBuilder strBuilder = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);            

            strBuilder = new StringBuilder();
            strBuilder.append(" update employee_location ")
                    .append(" set ")
                    .append(" dg_approval_claim_status=? ")
                    .append(" ,dg_approval_date=? ")
                    .append(" ,last_mod=?,last_mod_by=? ")
                    .append(" where record_id=? ");
            prep_stmt = db_conn.prepareStatement(strBuilder.toString());
            prep_stmt.setString(1, dto.getDgApprovalClaimStatus());
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getDgApprovalDate()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(5, dto.getRecordId());
            prep_stmt.executeUpdate();

            db_conn.commit();
            result = true;

        } catch (Exception exc) {
            exc.printStackTrace();
            if (db_conn != null) {
                try {
                    db_conn.rollback();
                } catch (Exception e) {

                }
            }
            throw new AppException(exc.getMessage());

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }

        return result;
    }

    public boolean claimsPayment(NewlyPostedWelfareDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        StringBuilder strBuilder = null;
        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            strBuilder = new StringBuilder();
            strBuilder.append(" update employee_location ")
                    .append(" set ")
                    .append(" claim_payment_status=? ")
                    .append(" ,date_of_payment=? ")
                    .append(" ,last_mod=?,last_mod_by=? ")
                    .append(" where record_id=? ");
            prep_stmt = db_conn.prepareStatement(strBuilder.toString());
            prep_stmt.setString(1, dto.getClaimPaymentStatus());
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getClaimPaymentDate()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getRecordId()));
            prep_stmt.executeUpdate();

            db_conn.commit();
            result = true;

        } catch (Exception exc) {
            exc.printStackTrace();
            if (db_conn != null) {
                try {
                    db_conn.rollback();
                } catch (Exception e) {

                }
            }
            throw new AppException(exc.getMessage());

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

}
