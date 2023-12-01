/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.reports;

import app.exceptions.AppException;
import dto.welfare.RetireesDTO;
import dto.welfare.WelfareClaimReportDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import org.apache.commons.lang3.StringUtils;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.DateTimeUtils;

/**
 *
 * @author nysc-ict-11
 */
public class RetireeWelfareReportService implements Serializable {
    
    private String employeeSelectQuery;
    private ServiceUtil serviceUtil;
    private DBUtil dBUtil;
    
    public RetireeWelfareReportService() {
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
    
    public WelfareClaimReportDTO getRetireEmployeeClaimReport(String startDate, String endDate)
            throws AppException {
        
        WelfareClaimReportDTO result = new WelfareClaimReportDTO();
        RetireesDTO dto = null;
        
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        
        String todayDate = DateTimeUtils.getActivityDate();
        
        try {
            db_conn = dBUtil.getDatasource().getConnection();
            
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getEmployeeSelectQuery());
            
            strBuilder.append(" where date_due_for_retirement <='")
                    .append(todayDate).append("'")
                    .append(" and dg_approve_retiree_claim_status='PENDING'")
                    //   .append(" or retiree_payment_claim_status='UNPAID') ")
                    .append("order by e._temp_sort ");
            
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, startDate);
            prep_stmt.setString(2, endDate);
            result_set = prep_stmt.executeQuery();
            
            double cummulativeClaim = 0;
            
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
                dto.setCurrentAnnualEmulument(result_set.getString("current_annual_emulument"));
                dto.setCurrentAnnualEmulumentDesc(result_set.getString("_annual_emulument_desc"));
                dto.setDgApprovalClaimStatus(result_set.getString("dg_approve_retiree_claim_status"));
                dto.setClaimPaymentStatus(result_set.getString("retiree_payment_claim_status"));
                dto.setRetirementClaim(result_set.getString("retirement_claim"));
                dto.setRetirementClaimDesc(result_set.getString("_ret_claim"));
                //Process Individual Retiree 
                kilos = result_set.getString("_kilometers");
                if (!StringUtils.isBlank(kilos)) {
                    dto.setKilometres(kilos);
                    totalClaims = (0.05 * Double.parseDouble(dto.getCurrentAnnualEmulument()))
                            + (75 * Double.parseDouble(dto.getKilometres()) * 6);
                    
                    DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
                    formatter.applyPattern("###,###,###,###,###,##0.00");
                    dto.setTotalClaimAmountDesc(formatter.format(totalClaims));
                    
                    dto.setTotalClaimAmount(String.valueOf(totalClaims));
                } else {
                    dto.setKilometres("0");
                    totalClaims = (0.05 * Double.parseDouble(dto.getCurrentAnnualEmulument()));
                    
                    DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
                    formatter.applyPattern("###,###,###,###,###,##0.00");
                    dto.setTotalClaimAmountDesc(formatter.format(totalClaims));
                    
                    dto.setTotalClaimAmount(String.valueOf(totalClaims));
                }
                result.addRetireeClaim(dto);

                //Cummulative Total Claim
                cummulativeClaim += totalClaims;
            }
              if (result.getRetireeClaim().size() > 0) {
                result.setRetireeClaimpendingDgApproval(true);
            }
            
            DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
            formatter.applyPattern("###,###,###,###,###,##0.00");
            result.setCummulativeTotalClaimDesc(formatter.format(cummulativeClaim));
            
        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }
    
    public WelfareClaimReportDTO getApprovedRetireeClaimReport(String startDate, String endDate)
            throws AppException {
        
        WelfareClaimReportDTO result = new WelfareClaimReportDTO();
        RetireesDTO dto = null;
        
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        
        String todayDate = DateTimeUtils.getActivityDate();
        
        try {
            db_conn = dBUtil.getDatasource().getConnection();
            
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getEmployeeSelectQuery());
            
            strBuilder.append(" where date_due_for_retirement <='")
                    .append(todayDate).append("'")
                    .append(" and (dg_approve_retiree_claim_status='APPROVED'")
                    .append(" or retiree_payment_claim_status='UNPAID') ")
                    .append(" and dg_approval_date between ? and ? ")
                    .append("order by e._temp_sort ");
            
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, startDate);
            prep_stmt.setString(2, endDate);
            result_set = prep_stmt.executeQuery();
            
            double cummulativeClaim = 0;
            
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
                dto.setCurrentAnnualEmulument(result_set.getString("current_annual_emulument"));
                dto.setCurrentAnnualEmulumentDesc(result_set.getString("_annual_emulument_desc"));
                dto.setDgApprovalClaimStatus(result_set.getString("dg_approve_retiree_claim_status"));
                dto.setClaimPaymentStatus(result_set.getString("retiree_payment_claim_status"));
                dto.setRetirementClaim(result_set.getString("retirement_claim"));
                dto.setRetirementClaimDesc(result_set.getString("_ret_claim"));
                //Process Individual Retiree 
                kilos = result_set.getString("_kilometers");
                if (!StringUtils.isBlank(kilos)) {
                    dto.setKilometres(kilos);
                    totalClaims = (0.05 * Double.parseDouble(dto.getCurrentAnnualEmulument()))
                            + (75 * Double.parseDouble(dto.getKilometres()) * 6);
                    
                    DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
                    formatter.applyPattern("###,###,###,###,###,##0.00");
                    dto.setTotalClaimAmountDesc(formatter.format(totalClaims));
                    
                    dto.setTotalClaimAmount(String.valueOf(totalClaims));
                } else {
                    dto.setKilometres("0");
                    totalClaims = (0.05 * Double.parseDouble(dto.getCurrentAnnualEmulument()));
                    
                    DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
                    formatter.applyPattern("###,###,###,###,###,##0.00");
                    dto.setTotalClaimAmountDesc(formatter.format(totalClaims));
                    
                    dto.setTotalClaimAmount(String.valueOf(totalClaims));
                }
                result.addApprovedRetireeClaim(dto);

                //Cummulative Total Claim
                cummulativeClaim += totalClaims;
            }
            
            if (result.getApprovedRetireeClaim().size() > 0) {
                result.setRetireeclaimApproved(true);
            }
            
            DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
            formatter.applyPattern("###,###,###,###,###,##0.00");
            result.setCummulativeTotalClaimDesc(formatter.format(cummulativeClaim));
            
        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }
    
    public WelfareClaimReportDTO getUnpaidRetireeClaimReport(String startDate, String endDate)
            throws AppException {
        
        WelfareClaimReportDTO result = new WelfareClaimReportDTO();
        RetireesDTO dto = null;
        
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        
        String todayDate = DateTimeUtils.getActivityDate();
        
        try {
            db_conn = dBUtil.getDatasource().getConnection();
            
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getEmployeeSelectQuery());
            
            strBuilder.append(" where date_due_for_retirement <='")
                    .append(todayDate).append("'")
                    .append(" and (dg_approve_retiree_claim_status='APPROVED'")
                    .append(" or retiree_payment_claim_status='UNPAID') ")
                    .append(" and dg_approval_date between ? and ? ")
                    .append("order by e._temp_sort ");
            
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, startDate);
            prep_stmt.setString(2, endDate);
            result_set = prep_stmt.executeQuery();
            
            double cummulativeClaim = 0;
            
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
                dto.setCurrentAnnualEmulument(result_set.getString("current_annual_emulument"));
                dto.setCurrentAnnualEmulumentDesc(result_set.getString("_annual_emulument_desc"));
                dto.setDgApprovalClaimStatus(result_set.getString("dg_approve_retiree_claim_status"));
                dto.setClaimPaymentStatus(result_set.getString("retiree_payment_claim_status"));
                dto.setRetirementClaim(result_set.getString("retirement_claim"));
                dto.setRetirementClaimDesc(result_set.getString("_ret_claim"));
                //Process Individual Retiree 
                kilos = result_set.getString("_kilometers");
                if (!StringUtils.isBlank(kilos)) {
                    dto.setKilometres(kilos);
                    totalClaims = (0.05 * Double.parseDouble(dto.getCurrentAnnualEmulument()))
                            + (75 * Double.parseDouble(dto.getKilometres()) * 6);
                    
                    DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
                    formatter.applyPattern("###,###,###,###,###,##0.00");
                    dto.setTotalClaimAmountDesc(formatter.format(totalClaims));
                    
                    dto.setTotalClaimAmount(String.valueOf(totalClaims));
                } else {
                    dto.setKilometres("0");
                    totalClaims = (0.05 * Double.parseDouble(dto.getCurrentAnnualEmulument()));
                    
                    DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
                    formatter.applyPattern("###,###,###,###,###,##0.00");
                    dto.setTotalClaimAmountDesc(formatter.format(totalClaims));
                    
                    dto.setTotalClaimAmount(String.valueOf(totalClaims));
                }
                result.addUnpaidRetireeClaim(dto);

                //Cummulative Total Claim
                cummulativeClaim += totalClaims;
            }
            
            if (result.getUnpaidRetireeClaim().size() > 0) {
                result.setUnpaidRetiree(true);
            }
            
            DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
            formatter.applyPattern("###,###,###,###,###,##0.00");
            result.setCummulativeTotalClaimDesc(formatter.format(cummulativeClaim));
            
        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }
    
    public WelfareClaimReportDTO getPaidRetireeClaimReport(String startDate, String endDate)
            throws AppException {
        
        WelfareClaimReportDTO result = new WelfareClaimReportDTO();
        RetireesDTO dto = null;
        
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        
        String todayDate = DateTimeUtils.getActivityDate();
        
        try {
            db_conn = dBUtil.getDatasource().getConnection();
            
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getEmployeeSelectQuery());
            
            strBuilder.append(" where date_due_for_retirement <='")
                    .append(todayDate).append("'")
                    .append(" and (dg_approve_retiree_claim_status='APPROVED'")
                    .append(" or retiree_payment_claim_status='PAID') ")
                    .append(" and retiree_payment_date between ? and ?")
                    .append("order by e._temp_sort ");
            
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, startDate);
            prep_stmt.setString(2, endDate);
            result_set = prep_stmt.executeQuery();
            
            double cummulativeClaim = 0;
            
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
                dto.setCurrentAnnualEmulument(result_set.getString("current_annual_emulument"));
                dto.setCurrentAnnualEmulumentDesc(result_set.getString("_annual_emulument_desc"));
                dto.setDgApprovalClaimStatus(result_set.getString("dg_approve_retiree_claim_status"));
                dto.setClaimPaymentStatus(result_set.getString("retiree_payment_claim_status"));
                dto.setRetirementClaim(result_set.getString("retirement_claim"));
                dto.setRetirementClaimDesc(result_set.getString("_ret_claim"));
                //Process Individual Retiree 
                kilos = result_set.getString("_kilometers");
                if (!StringUtils.isBlank(kilos)) {
                    dto.setKilometres(kilos);
                    totalClaims = (0.05 * Double.parseDouble(dto.getCurrentAnnualEmulument()))
                            + (75 * Double.parseDouble(dto.getKilometres()) * 6);
                    
                    DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
                    formatter.applyPattern("###,###,###,###,###,##0.00");
                    dto.setTotalClaimAmountDesc(formatter.format(totalClaims));
                    
                    dto.setTotalClaimAmount(String.valueOf(totalClaims));
                } else {
                    dto.setKilometres("0");
                    totalClaims = (0.05 * Double.parseDouble(dto.getCurrentAnnualEmulument()));
                    
                    DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
                    formatter.applyPattern("###,###,###,###,###,##0.00");
                    dto.setTotalClaimAmountDesc(formatter.format(totalClaims));
                    
                    dto.setTotalClaimAmount(String.valueOf(totalClaims));
                }
                result.addPaidRetireeClaim(dto);

                //Cummulative Total Claim
                cummulativeClaim += totalClaims;
            }
            if (result.getPaidRetireeClaim().size() > 0) {
                result.setPaidRetiree(true);
            }
            
            DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
            formatter.applyPattern("###,###,###,###,###,##0.00");
            result.setCummulativeTotalClaimDesc(formatter.format(cummulativeClaim));
            
        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }
    
}
