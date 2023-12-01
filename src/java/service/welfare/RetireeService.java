/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.welfare;

import app.exceptions.AppException;
import dto.welfare.RetireesDTO;
import dto.welfare.WelfareClaimReportDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;

/**
 *
 * @author Gainsolutions
 */
public class RetireeService implements Serializable {

    private String employeeSelectQuery;
    private ServiceUtil serviceUtil;
    private DBUtil dBUtil;

    public RetireeService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();

        initSelectQuery();
    }

    private void initSelectQuery() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select e.employee_rec_id,e.file_no, e.surname, e.other_names, e.gender, e.marital_status,sms.description as _marital_status")
                .append(", e.state_of_origin,ss.description as _state_of_origin, e.lga, slga.description as _lga")
                .append(",e.date_of_birth,date_format(date_of_birth,'%d/%m/%Y') as _date_of_birth")
                .append(", e.date_of_1st_appt,date_format(date_of_1st_appt,'%d/%m/%Y') as _date_of_1st_appt")
                .append(",e.current_grade_level, e.current_grade_step")
                .append(", e.current_date_of_present_appt,date_format(current_date_of_present_appt,'%d/%m/%Y') as _current_date_of_present_appt")
                .append(",e.confirmation_date, date_format(confirmation_date,'%d/%m/%Y') as _confirmation_date")
                .append(",e.current_cadre,scadre.description as _current_cadre, e.current_location,sloc.description as _current_location")
                .append(",e.current_date_of_posting_to_location,date_format(current_date_of_posting_to_location,'%d/%m/%Y') as _current_date_of_posting_to_location")
                .append(", e.current_dept, sdept.description as _current_dept, e.is_state_coord, e.is_state_acct, e.is_secondment")
                .append(",e.date_retire_based_on_length_of_stay,date_format(date_retire_based_on_length_of_stay,'%d/%m/%Y') as _date_retire_based_on_length_of_stay")
                .append(",e.date_retire_based_on_age,date_format(date_retire_based_on_age,'%d/%m/%Y') as _date_retire_based_on_age")
                .append(",e.date_due_for_retirement,date_format(date_due_for_retirement,'%d/%m/%Y') as _date_due_for_retirement")
                .append(",date_format(date_due_for_retirement,'%Y') as _year_due_for_retirement ")
                .append(",e.primary_phone,e.secondary_phone,e.primary_email,e.secondary_email,e.present_address,e.permanent_home_address ")
                .append(",e.present_status,sps.description as _present_status")
                .append(",e.present_status_reason,spsr.description as _present_status_reason")
                .append(",e.date_of_present_status,date_format(date_of_present_status,'%d/%m/%Y') as _date_of_present_status")
                .append(",e.present_status_remarks,e.mode_of_entry,smoe.description as _mode_of_entry ")
                .append(",e.current_annual_emulument,format(e.current_annual_emulument,2) as _annual_emulument_desc ")
                .append(",e.retirement_claim,format(e.retirement_claim,2) as _ret_claim ")
                .append(",e.dg_approve_retiree_claim_status,e.retiree_payment_claim_status ")
                .append(" ,(SELECT kilometers FROM setup_location_distance ")
                .append(" where (location_from=e.state_of_origin and location_to=e.current_location) ")
                .append(" or (location_from=e.current_location and location_to=e.state_of_origin )) as _kilometers ")
                .append("from employee e ")
                .append("join setup_marital_status sms on e.marital_status = sms.record_id ")
                .append("join setup_states ss on e.state_of_origin=ss.record_id ")
                .append("join setup_lgas slga on e.lga = slga.record_id ")
                .append("join setup_cadre scadre on e.current_cadre = scadre.record_id ")
                .append("join setup_states sloc on e.current_location = sloc.record_id ")
                .append("join setup_departments sdept on e.current_dept = sdept.record_id ")
                .append("join setup_personnel_status sps on e.present_status = sps.record_id ")
                .append("left outer join setup_personnel_status_reasons spsr on e.present_status_reason = spsr.record_id ")
                .append("join setup_mode_of_entry smoe on e.mode_of_entry = smoe.record_id ");

        employeeSelectQuery = strBuilder.toString();
    }

    public String getEmployeeSelectQuery() {
        return employeeSelectQuery;
    }

    public Map<String, Object> searchRecords(String countQuery, String selectQuery, int startRow, int pagesize)
            throws AppException {

        Integer countInt = 0;
        Map<String, Object> result = new HashMap<>();
        List<RetireesDTO> dto_list = new ArrayList<>();
        RetireesDTO dto = null;
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

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
                double totalClaims = 0;
                String kilos = "";
                dto = new RetireesDTO();
                dto.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                dto.setFileNo(result_set.getString("file_no"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));
                dto.setStateOfOrigin(result_set.getString("state_of_origin"));
                dto.setStateOfOriginDesc(result_set.getString("_state_of_origin"));
                dto.setPresentLocation(result_set.getString("current_location"));
                dto.setPresentLocationDesc(result_set.getString("_current_location"));
                dto.setDateDueForRetirement(result_set.getString("date_due_for_retirement"));
                dto.setDateDueForRetirementDesc(result_set.getString("_date_due_for_retirement"));
                dto.setCurrentAnnualEmulument(serviceUtil.getValueOrZero(result_set.getString("current_annual_emulument")));
                dto.setCurrentAnnualEmulumentDesc(result_set.getString("_annual_emulument_desc"));

                dto.setRetirementClaim(serviceUtil.getValueOrZero(result_set.getString("retirement_claim")));
                dto.setRetirementClaimDesc(result_set.getString("_ret_claim"));

                //Check if the Retirement Claims has been Processed
                if (Double.parseDouble(dto.getRetirementClaim()) > 0) {
                    dto.setClaimProcessed(true);
                } else {
                    dto.setClaimProcessed(false);
                }

                dto.setDgApprovalClaimStatus(result_set.getString("dg_approve_retiree_claim_status"));

                //Check if the DG Retirement Approval Status              
                if (StringUtils.equalsIgnoreCase(dto.getDgApprovalClaimStatus(), AppConstants.APPROVED) || StringUtils.isBlank(dto.getDgApprovalClaimStatus())) {
                    dto.setClaimsApproved(true);
                } else if (StringUtils.equalsIgnoreCase(dto.getDgApprovalClaimStatus(), AppConstants.PENDING)) {
                    dto.setClaimsApproved(false);
                }

                //Check if the Retirement Status has been paid       
                dto.setClaimPaymentStatus(result_set.getString("retiree_payment_claim_status"));

                if (StringUtils.equalsIgnoreCase(dto.getClaimPaymentStatus(), AppConstants.PAID) || StringUtils.isBlank(dto.getClaimPaymentStatus())
                        || StringUtils.equalsIgnoreCase(dto.getDgApprovalClaimStatus(), AppConstants.PENDING)) {
                    dto.setClaimsPaid(true);
                } else if (StringUtils.equalsIgnoreCase(dto.getClaimPaymentStatus(), AppConstants.UNPAID)) {
                    dto.setClaimsPaid(false);
                }

                //Process Individual Retiree 
                kilos = result_set.getString("_kilometers");
                if (!StringUtils.isBlank(kilos)) {
                    dto.setKilometres(kilos);
                    totalClaims = (0.05 * Double.parseDouble(dto.getCurrentAnnualEmulument()))
                            + (75 * Double.parseDouble(dto.getKilometres()) * 6);
                    dto.setTotalClaimAmount(String.valueOf(totalClaims));
                } else {
                    dto.setKilometres("0");
                    totalClaims = (0.05 * Double.parseDouble(dto.getCurrentAnnualEmulument()));
                    dto.setTotalClaimAmount(String.valueOf(totalClaims));
                }
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

    public RetireesDTO getEmployee(String recordId)
            throws AppException {

        RetireesDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String todayDate = DateTimeUtils.getActivityDate();

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getEmployeeSelectQuery())
                    .append(" where e.employee_rec_id=? ");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            
            prep_stmt.setString(1, recordId);
            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();
            if (found) {
                double totalClaims = 0;
                String kilos = "";
                result = new RetireesDTO();
                result.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                result.setFileNo(result_set.getString("file_no"));
                result.setSurname(result_set.getString("surname"));
                result.setOtherNames(result_set.getString("other_names"));
                result.setStateOfOrigin(result_set.getString("state_of_origin"));
                result.setStateOfOriginDesc(result_set.getString("_state_of_origin"));
                result.setPresentLocation(result_set.getString("current_location"));
                result.setPresentLocationDesc(result_set.getString("_current_location"));
                result.setDateDueForRetirement(result_set.getString("date_due_for_retirement"));
                result.setDateDueForRetirementDesc(result_set.getString("_date_due_for_retirement"));
                result.setCurrentAnnualEmulument(result_set.getString("current_annual_emulument"));
                result.setCurrentAnnualEmulumentDesc(result_set.getString("_annual_emulument_desc"));

                result.setRetirementClaim(serviceUtil.getValueOrZero(result_set.getString("retirement_claim")));
                result.setRetirementClaimDesc(result_set.getString("_ret_claim"));

                //Check if the Retirement Claims has been Processed
                if (Double.parseDouble(result.getRetirementClaim()) > 0) {
                    result.setClaimProcessed(true);
                } else {
                    result.setClaimProcessed(false);
                }

                result.setDgApprovalClaimStatus(result_set.getString("dg_approve_retiree_claim_status"));

                //Check if the DG Retirement Approval Status              
                if (StringUtils.equalsIgnoreCase(result.getDgApprovalClaimStatus(), AppConstants.APPROVED) || StringUtils.isBlank(result.getDgApprovalClaimStatus())
                        || StringUtils.equalsIgnoreCase(result.getDgApprovalClaimStatus(), AppConstants.PENDING)) {
                    result.setClaimsApproved(true);
                } else if (StringUtils.equalsIgnoreCase(result.getDgApprovalClaimStatus(), AppConstants.PENDING)) {
                    result.setClaimsApproved(false);
                }

                result.setClaimPaymentStatus(result_set.getString("retiree_payment_claim_status"));
                //Check if the Retirement Status has been paid               
                if (StringUtils.equalsIgnoreCase(result.getClaimPaymentStatus(), AppConstants.PAID) || StringUtils.isBlank(result.getClaimPaymentStatus())
                        || StringUtils.equalsIgnoreCase(result.getDgApprovalClaimStatus(), AppConstants.PENDING)) {
                    result.setClaimsPaid(true);
                } else if (StringUtils.equalsIgnoreCase(result.getClaimPaymentStatus(), AppConstants.UNPAID)
                        && StringUtils.equalsIgnoreCase(result.getDgApprovalClaimStatus(), AppConstants.APPROVED)) {
                    result.setClaimsPaid(false);
                }

                //Process Individual Retiree 
                kilos = result_set.getString("_kilometers");
                if (!StringUtils.isBlank(kilos)) {
                    result.setKilometres(kilos);
                    totalClaims = (0.05 * Double.parseDouble(result.getCurrentAnnualEmulument()))
                            + (75 * Double.parseDouble(result.getKilometres()) * 6);
                    result.setTotalClaimAmount(String.valueOf(totalClaims));
                } else {
                    result.setKilometres("0");
                    totalClaims = (0.05 * Double.parseDouble(result.getCurrentAnnualEmulument()));
                    result.setTotalClaimAmount(String.valueOf(totalClaims));
                }
            } else {
                throw new AppException(AppConstants.NOT_FOUND);
            }

        } catch (Exception exc) {
            exc.printStackTrace();
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean processClaims(RetireesDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            StringBuilder strBuilder = new StringBuilder();

            strBuilder = new StringBuilder();
            strBuilder.append(" update employee ")
                    .append(" set ")
                    .append(" dg_approve_retiree_claim_status=? ")
                    .append(" ,retiree_payment_claim_status=? ")
                    .append(" ,retirement_claim=? ")
                    .append(" ,last_mod=?,last_mod_by=? ")
                    .append(" where employee_rec_id=? ");
            prep_stmt = db_conn.prepareStatement(strBuilder.toString());
            prep_stmt.setString(1, "PENDING");
            prep_stmt.setString(2, "UNPAID");
            prep_stmt.setString(3, serviceUtil.getValueOrZero(dto.getTotalClaimAmount()));

            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));
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

    public boolean dgApproveClaim(RetireesDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            StringBuilder strBuilder = new StringBuilder();

            strBuilder = new StringBuilder();
            strBuilder.append(" update employee ")
                    .append(" set ")
                    .append(" dg_approve_retiree_claim_status=? ")
                    .append(" ,dg_approval_date=? ")
                    .append(" ,last_mod=?,last_mod_by=? ")
                    .append(" where employee_rec_id=? ");
            prep_stmt = db_conn.prepareStatement(strBuilder.toString());
            prep_stmt.setString(1, dto.getDgApprovalClaimStatus());
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));
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

    public boolean claimsPayment(RetireesDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            StringBuilder strBuilder = new StringBuilder();

            strBuilder = new StringBuilder();
            strBuilder.append(" update employee ")
                    .append(" set ")
                    .append(" retiree_payment_claim_status=? ")
                    .append(" ,retiree_payment_date=? ")
                    .append(" ,last_mod=?,last_mod_by=? ")
                    .append(" where employee_rec_id=? ");
            prep_stmt = db_conn.prepareStatement(strBuilder.toString());
            prep_stmt.setString(1, dto.getClaimPaymentStatus());
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));
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

    public List<RetireesDTO> getRetireesIn3MonthTime()
            throws AppException {

        List<RetireesDTO> result = new ArrayList<>();
        RetireesDTO dto = null;
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            StringBuilder strBuilder = new StringBuilder();
            String activityDate = DateTimeUtils.getActivityDate();
            strBuilder.append(getEmployeeSelectQuery());

            strBuilder.append(" where (datediff(e.date_due_for_retirement,")
                    .append("'")
                    .append(activityDate)
                    .append("')) <='90' ")
                    .append(" and e.present_status='1' ");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                dto = new RetireesDTO();
                dto.setRecordId(result_set.getString("employee_rec_id"));
                dto.setFileNo(result_set.getString("file_no"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));
                dto.setStateOfOriginDesc(result_set.getString("_state_of_origin"));
                dto.setPresentLocation(result_set.getString("current_location"));
                dto.setPresentLocationDesc(result_set.getString("_current_location"));
                dto.setDateDueForRetirementDesc(result_set.getString("_date_due_for_retirement"));
                //    System.out.println("File No " + dto.getFileNo());
                result.add(dto);
            }
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
