/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.posting;

import app.exceptions.AppException;
import dto.employees.EmployeeLocationDTO;
import dto.posting.PostingDTO;
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
public class PostingService implements Serializable {

    private String employeePostingSelectQuery;

    private ServiceUtil serviceUtil;
    private DBUtil dBUtil;

    public PostingService() {
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

        employeePostingSelectQuery = strBuilder.toString();
    }

    public String getEmployeePostingSelectQuery() {
        return employeePostingSelectQuery;
    }

    public Map<String, Object> searchRecords(String countQuery, String selectQuery, int startRow, int pagesize)
            throws AppException {

        Integer countInt = 0;
        Map<String, Object> result = new HashMap<>();
        List<PostingDTO> dto_list = new ArrayList<>();
        PostingDTO dto = null;
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

                dto = new PostingDTO();
                dto.setRecordId(result_set.getString("employee_rec_id"));
                dto.setFileNo(result_set.getString("file_no"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));

                dto.setGender(result_set.getString("gender"));

                dto.setMaritalStatus(result_set.getString("marital_status"));
                dto.setMaritalStatusDesc(result_set.getString("_marital_status"));
                dto.setGradeLevel(result_set.getString("current_grade_level"));

                dto.setStateOfOrigin(result_set.getString("state_of_origin"));
                dto.setStateOfOriginDesc(result_set.getString("_state_of_origin"));

                dto.setPresentCadre(result_set.getString("current_cadre"));
                dto.setPresentCadreDesc(result_set.getString("_current_cadre"));

                dto.setPresentLocation(result_set.getString("current_location"));
                dto.setPresentLocationDesc(result_set.getString("_current_location"));

                dto.setDateOfPostingToPresentLocation(result_set.getString("current_date_of_posting_to_location"));
                dto.setDateOfPostingToPresentLocationDesc(result_set.getString("_current_date_of_posting_to_location"));

                dto.setLengthOfStay(viewHelper.calculatLenghtOfStay(todayDate,
                        result_set.getString("current_date_of_posting_to_location"), "ymd"));

                dto.setIsStateAcct(result_set.getString("is_state_coord"));
                dto.setIsStateAcct(result_set.getString("is_state_acct"));
                dto.setIsSecondment(result_set.getString("is_secondment"));

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

    public PostingDTO getEmployeePosting(String recordId, String fileNo, String fetchBy, String fetchView)
            throws AppException {
        PostingDTO result = null;
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        String todayDate = DateTimeUtils.getActivityDate();
        ViewHelper viewHelper = new ViewHelper();

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getEmployeePostingSelectQuery());

            if (StringUtils.equals(fetchBy, AppConstants.FETCH_MODE_RECORD_ID)) {
                strBuilder.append(" where e.employee_rec_id=? ");
            } else if (StringUtils.equals(fetchBy, AppConstants.FETCH_MODE_FILE_NO)) {
                strBuilder.append(" where e.file_no=? ");
            }

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            if (StringUtils.equals(fetchBy, AppConstants.FETCH_MODE_RECORD_ID)) {
                prep_stmt.setString(1, recordId);
            } else if (StringUtils.equals(fetchBy, AppConstants.FETCH_MODE_FILE_NO)) {
                prep_stmt.setString(1, fileNo);
            }

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();
            if (found) {
                result = new PostingDTO();
                result.setRecordId(result_set.getString("employee_rec_id"));
                result.setFileNo(result_set.getString("file_no"));
                result.setSurname(result_set.getString("surname"));
                result.setOtherNames(result_set.getString("other_names"));
                result.setGradeLevel(result_set.getString("current_grade_level"));
                result.setPresentLocation(result_set.getString("current_location"));
                result.setPresentLocationDesc(result_set.getString("_current_location"));
                result.setDateOfPostingToPresentLocation(result_set.getString("current_date_of_posting_to_location"));
                result.setDateOfPostingToPresentLocationDesc(result_set.getString("_current_date_of_posting_to_location"));
                result.setLengthOfStay(viewHelper.calculatLenghtOfStay(todayDate,
                        result_set.getString("current_date_of_posting_to_location"), "ymd"));
                //fetch locations
                strBuilder = new StringBuilder();
                strBuilder.append("SELECT d.record_id, d.employee_rec_id,d.location_from,d.location,s1.description as _location ")
                        .append(",d.department, s2.description as _department ")
                        .append(",d.date_of_posting,date_format(d.date_of_posting,'%d/%m/%Y') as _date_of_posting ")
                        .append(",d.is_state_coord, d.is_state_acct,d.is_recent_posting ")
                        .append("FROM employee_location d  ")
                        .append("join setup_states s1 on (d.location=s1.record_id) ")
                        .append("left outer join setup_departments s2 on (d.department=s2.record_id) ")
                        .append("where d.employee_rec_id=? order by d.date_of_posting desc ");
                query = strBuilder.toString();
                prep_stmt = db_conn.prepareStatement(query);
                prep_stmt.setString(1, result.getRecordId());
                result_set = prep_stmt.executeQuery();

                EmployeeLocationDTO empLocation = null;
                while (result_set.next()) {
                    empLocation = new EmployeeLocationDTO();
                    empLocation.setRecordId(result_set.getString("record_id"));
                    empLocation.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                    empLocation.setLocationFrom(result_set.getString("location_from"));
                    empLocation.setLocation(result_set.getString("location"));
                    empLocation.setLocationDesc(result_set.getString("_location"));
                    empLocation.setDepartment(result_set.getString("department"));
                    empLocation.setDepartmentDesc(result_set.getString("_department"));
                    empLocation.setDatePosted(result_set.getString("date_of_posting"));
                    empLocation.setDatePostedDesc(result_set.getString("_date_of_posting"));
                    empLocation.setIsStateCoord(result_set.getString("is_state_coord"));
                    empLocation.setIsStateAcct(result_set.getString("is_state_acct"));
                    empLocation.setIsRecentPosting(result_set.getString("is_recent_posting"));
                    result.addLocation(empLocation);
                }
            }

            //Get Cummulative Lenght of Stay As Special Appointee
            String firstApptDate = null;
            String otherApptDate = null;

            //Get the First Date of Posting on Special Appointment
            strBuilder = new StringBuilder();
            strBuilder.append(" select date_of_posting from employee_location ")
                    .append(" where employee_rec_id=? ")
                    .append(" and (is_state_coord='Y' or is_state_acct='Y') ")
                    .append(" order by record_id asc ")
                    .append(" limit 1 ");

            query = strBuilder.toString();
            prep_stmt.setString(1, result.getRecordId());
            result_set = prep_stmt.executeQuery();
            found = result_set.next();
            if (found) {
                firstApptDate = result_set.getString("date_of_posting");
            }

            //Get the latest Date of Posting on Special Appointment
            strBuilder = new StringBuilder();
            strBuilder.append(" select date_of_posting from employee_location ")
                    .append(" where employee_rec_id=? ")
                    .append(" and (is_state_coord='Y' or is_state_acct='Y') ")
                    .append(" order by record_id desc ")
                    .append(" limit 1 ");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, result.getRecordId());
            result_set = prep_stmt.executeQuery();
            found = result_set.next();
            if (found) {
                otherApptDate = result_set.getString("date_of_posting");
            }

            //Calculate the Cummlative Length of Stay As Special Appointees
            if (firstApptDate != null && otherApptDate != null) {
                if (StringUtils.equalsIgnoreCase(firstApptDate, otherApptDate)) {
                    result.setLengthOfSpecialAppoint(viewHelper.calculatLenghtOfStay(todayDate, otherApptDate, "ymd"));
                } else {
                  //  result.setLengthOfSpecialAppoint(viewHelper.calculatLenghtOfStay(otherApptDate, firstApptDate, "ymd"));

                    result.setLengthOfSpecialAppoint(viewHelper.calculatLenghtOfStay(firstApptDate, otherApptDate, "ymd"));
                }
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }

        return result;
    }

    public boolean createEmployeePosting(EmployeeLocationDTO dto)
            throws AppException {
        boolean result = false;
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            StringBuilder strBuilder = new StringBuilder();

            //Check if Posting is to Present Location of the Employee
            String checkPresentLocation;
            strBuilder.append(" select current_location from employee ")
                    .append(" where employee_rec_id=? and current_location=?");
            checkPresentLocation = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(checkPresentLocation);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getLocation()));
            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.LOCATION_ERROR);
            }

            // If Posting is not to Current Location of the Employee
            // Action 1:- Update Recent Posting Status of the Employee
            strBuilder = new StringBuilder();
            strBuilder.append(" update employee_location ")
                    .append(" set is_recent_posting=? ")
                    .append(" where employee_rec_id=? ");
            prep_stmt = db_conn.prepareStatement(strBuilder.toString());
            prep_stmt.setString(1, "N");
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));
            prep_stmt.executeUpdate();

            //Action 2:- Update Employee Current Location and Date of Posting
            strBuilder = new StringBuilder();
            strBuilder.append(" update employee ")
                    .append(" set ")
                    .append(" current_location=?, current_date_of_posting_to_location=? ")
                    .append(" ,is_state_coord=?,is_state_acct=?,is_secondment=? ")
                    .append(" ,last_mod=?,last_mod_by=? ")
                    .append(" where employee_rec_id=? ");
            prep_stmt = db_conn.prepareStatement(strBuilder.toString());

            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getLocation()));
            prep_stmt.setString(2, serviceUtil.resetToDbDateFormat(dto.getDatePosted()));

            //Check the Selected Special Appointment
            if (StringUtils.equals(dto.getIsSpecialAppointment(), "Y")) {
                if (StringUtils.equalsIgnoreCase(dto.getSpecialAppointment(), "1")) {
                    dto.setIsStateCoord("Y");
                    dto.setIsStateAcct("N");
                } else if (StringUtils.equalsIgnoreCase(dto.getSpecialAppointment(), "2")) {
                    dto.setIsStateCoord("N");
                    dto.setIsStateAcct("Y");
                }
            } else {
                dto.setIsStateCoord("N");
                dto.setIsStateAcct("N");
            }

            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getIsStateCoord()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getIsStateAcct()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getIsSecondment()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));
            prep_stmt.executeUpdate();

            //Action 3: Process Transfer Claim
            String gradeLevelAmount = "0";
            double totalClaim = 0;
            strBuilder = new StringBuilder();
            strBuilder.append(" select amount from setup_night_allowances ")
                    .append(" where grade_level=? ");
            prep_stmt = db_conn.prepareStatement(strBuilder.toString());
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getGradeLevel()));
            result_set = prep_stmt.executeQuery();
            exists = result_set.next();
            if (exists) {
                gradeLevelAmount = result_set.getString("amount");
            }

            //Get Kilometer
            String kilometer = "0";
            strBuilder = new StringBuilder();
            strBuilder.append(" select kilometers FROM setup_location_distance ")
                    .append(" where (location_from='").append(dto.getLocationFrom()).append("'")
                    .append(" and location_to='").append(dto.getLocation()).append("')")
                    .append(" or (location_from='").append(dto.getLocation()).append("'")
                    .append(" and location_to='").append(dto.getLocationFrom()).append("')");

            prep_stmt = db_conn.prepareStatement(strBuilder.toString());
            result_set = prep_stmt.executeQuery();
            exists = result_set.next();
            if (exists) {
                kilometer = result_set.getString("kilometers");
            }

            //Check if Posting is Concession=(1) and General=(2) Posting
            if (StringUtils.equalsIgnoreCase(dto.getPostingReason(), "1")) {
                totalClaim = 75 * Double.parseDouble(kilometer) * 6;
                dto.setTotalClaimAmount(String.valueOf(totalClaim));

            } else if (StringUtils.equalsIgnoreCase(dto.getPostingReason(), "2")) {
                totalClaim = (28 * Double.parseDouble(gradeLevelAmount)) + (75 * Double.parseDouble(kilometer) * 6);
                dto.setTotalClaimAmount(String.valueOf(totalClaim));
            }

            //Action 4:- Insert New Posting to Employee Location History
            strBuilder = new StringBuilder();
            strBuilder.append(" insert into employee_location(")
                    .append("employee_rec_id,location_from,location,department,date_of_posting")
                    .append(",is_state_coord,is_state_acct,appointment_id,posting_reason,length_of_stay")
                    .append(",is_recent_posting,total_claim_amount,dg_approval_claim_status")
                    .append(",dg_approval_date,claim_payment_status")
                    .append(",date_of_payment")
                    .append(",created,created_by,last_mod,last_mod_by)")
                    .append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            prep_stmt = db_conn.prepareStatement(strBuilder.toString());

            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getLocationFrom()));

            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getLocation()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getDepartment()));
            prep_stmt.setString(5, serviceUtil.resetToDbDateFormat(dto.getDatePosted()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getIsStateCoord()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getIsStateAcct()));
            prep_stmt.setString(8, dto.getSpecialAppointment());
            prep_stmt.setString(9, dto.getPostingReason());
            prep_stmt.setString(10, serviceUtil.getValueOrNull(dto.getLengthOfStay()));
            prep_stmt.setString(11, "Y");
            prep_stmt.setString(12, serviceUtil.getValueOrZero(dto.getTotalClaimAmount()));
            prep_stmt.setString(13, "PENDING");

            prep_stmt.setString(14, serviceUtil.getValueOrNull(dto.getDgApprovalDate()));
            prep_stmt.setString(15, "UNPAID");
            prep_stmt.setString(16, serviceUtil.getValueOrNull(dto.getDateOfPayment()));
            prep_stmt.setString(17, serviceUtil.getValueOrNull(dto.getCreated()));
            prep_stmt.setString(18, serviceUtil.getValueOrNull(dto.getCreatedBy()));
            prep_stmt.setString(19, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(20, serviceUtil.getValueOrNull(dto.getLastModBy()));
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

    public boolean updateEmployeePosting(EmployeeLocationDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        StringBuilder strBuilder = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //Action 1: Process Transfer Claim
            String gradeLevelAmount = "0";
            double totalClaim = 0;

            strBuilder = new StringBuilder();
            strBuilder.append(" select amount from setup_night_allowances ")
                    .append(" where grade_level=? ");
            prep_stmt = db_conn.prepareStatement(strBuilder.toString());
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getGradeLevel()));
            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            if (exists) {
                gradeLevelAmount = result_set.getString("amount");
            }

            //Get Kilometer
            String kilometer = "0";
            strBuilder = new StringBuilder();
            strBuilder.append(" select kilometers FROM setup_location_distance ")
                    .append(" where (location_from='").append(dto.getLocationFrom()).append("'")
                    .append(" and location_to='").append(dto.getLocation()).append("')")
                    .append(" or (location_from='").append(dto.getLocation()).append("'")
                    .append(" and location_to='").append(dto.getLocationFrom()).append("')");

            prep_stmt = db_conn.prepareStatement(strBuilder.toString());

            result_set = prep_stmt.executeQuery();
            exists = result_set.next();
            if (exists) {
                kilometer = result_set.getString("kilometers");
            }

            //Check if Posting is Concession=(1) and General=(2) Posting
            if (StringUtils.equalsIgnoreCase(dto.getPostingReason(), "1")) {
                totalClaim = 75 * Double.parseDouble(kilometer) * 6;
                dto.setTotalClaimAmount(String.valueOf(totalClaim));

            } else if (StringUtils.equalsIgnoreCase(dto.getPostingReason(), "2")) {
                totalClaim = (28 * Double.parseDouble(gradeLevelAmount)) + (75 * Double.parseDouble(kilometer) * 6);
                dto.setTotalClaimAmount(String.valueOf(totalClaim));
            }

            //Action 2:- Update Recent Posting
            strBuilder = new StringBuilder();
            strBuilder.append(" update employee_location ")
                    .append(" set ")
                    .append(" location=?,date_of_posting=?")
                    .append(",is_state_coord=?,is_state_acct=?,appointment_id=?,posting_reason=?")
                    .append(",total_claim_amount=?")
                    .append(",last_mod=?,last_mod_by=?")
                    .append(" where employee_rec_id=? and is_recent_posting=? ");
            prep_stmt = db_conn.prepareStatement(strBuilder.toString());
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getLocation()));
            prep_stmt.setString(2, serviceUtil.resetToDbDateFormat(dto.getDatePosted()));

            //Check the Selected Special Appointment
            if (StringUtils.equals(dto.getIsSpecialAppointment(), "Y")) {
                if (StringUtils.equalsIgnoreCase(dto.getSpecialAppointment(), "1")) {
                    dto.setIsStateCoord("Y");
                    dto.setIsStateAcct("N");
                } else if (StringUtils.equalsIgnoreCase(dto.getSpecialAppointment(), "2")) {
                    dto.setIsStateCoord("N");
                    dto.setIsStateAcct("Y");
                }

            } else {
                dto.setIsStateCoord("N");
                dto.setIsStateAcct("N");
            }

            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getIsStateCoord()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getIsStateAcct()));
            prep_stmt.setString(5, dto.getSpecialAppointment());
            prep_stmt.setString(6, dto.getPostingReason());
            prep_stmt.setString(7, dto.getTotalClaimAmount());
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(9, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(10, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));
            prep_stmt.setString(11, "Y");
            prep_stmt.executeUpdate();

            //Action 3:- Update Employee Current Location
            strBuilder = new StringBuilder();
            strBuilder.append(" update employee ")
                    .append(" set ")
                    .append(" current_location=?,current_date_of_posting_to_location=? ")
                    .append(",is_state_coord=?,is_state_acct=?")
                    .append(" ,last_mod=?,last_mod_by=? ")
                    .append(" where employee_rec_id=? ");
            prep_stmt = db_conn.prepareStatement(strBuilder.toString());
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getLocation()));
            prep_stmt.setString(2, serviceUtil.resetToDbDateFormat(dto.getDatePosted()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getIsStateCoord()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getIsStateAcct()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));
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

    public EmployeeLocationDTO getEmployeePosting(String recordId)
            throws AppException {

        EmployeeLocationDTO result = new EmployeeLocationDTO();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            //fetch locations
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("SELECT d.record_id, d.employee_rec_id,d.location_from,d.location,s1.description as _location ")
                    .append(",emp.current_grade_level")
                    .append(",d.department, s2.description as _department ")
                    .append(",d.date_of_posting,date_format(d.date_of_posting,'%d/%m/%Y') as _date_of_posting ")
                    .append(",d.is_state_coord, d.is_state_acct,d.appointment_id,d.posting_reason,d.is_recent_posting ")
                    .append("FROM employee_location d  ")
                    .append("join employee emp on emp.employee_rec_id=d.employee_rec_id ")
                    .append("join setup_states s1 on (d.location=s1.record_id) ")
                    .append("left outer join setup_departments s2 on (d.department=s2.record_id) ")
                    .append("where d.record_id=? order by d.date_of_posting desc ");

            prep_stmt = db_conn.prepareStatement(strBuilder.toString());
            prep_stmt.setString(1, recordId);
            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {

                result.setRecordId(result_set.getString("record_id"));
                result.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                result.setLocationFrom(result_set.getString("location_from"));
                result.setGradeLevel(result_set.getString("current_grade_level"));
                result.setLocation(result_set.getString("location"));
                result.setLocationDesc(result_set.getString("_location"));
                result.setDepartment(result_set.getString("department"));
                result.setDepartmentDesc(result_set.getString("_department"));
                result.setDatePosted(result_set.getString("_date_of_posting"));
                //  result.setDatePostedDesc(result_set.getString("_date_of_posting"));

                if (StringUtils.equalsIgnoreCase(result_set.getString("is_state_coord"), "Y")
                        || StringUtils.equalsIgnoreCase(result_set.getString("is_state_acct"), "Y")) {
                    result.setIsSpecialAppointment("Y");
                } else {
                    result.setIsSpecialAppointment("N");
                }
                result.setSpecialAppointment(result_set.getString("appointment_id"));
                result.setPostingReason(result_set.getString("posting_reason"));
            }
        } catch (Exception exc) {

            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public PostingDTO getProposedPosting(String currentDate, String datePostedToLocation)
            throws AppException {
        PostingDTO result = null;
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        ViewHelper viewHelper = new ViewHelper();

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getEmployeePostingSelectQuery());

            strBuilder.append(" where (year(?)-year(e.current_date_of_posting_to_location))>=? ");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, currentDate);
            prep_stmt.setString(2, datePostedToLocation);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();
            if (found) {
                result = new PostingDTO();
                result.setRecordId(result_set.getString("employee_rec_id"));
                result.setFileNo(result_set.getString("file_no"));
                result.setSurname(result_set.getString("surname"));
                result.setOtherNames(result_set.getString("other_names"));
                result.setPresentLocation(result_set.getString("current_location"));
                result.setPresentLocationDesc(result_set.getString("_current_location"));
                result.setDateOfPostingToPresentLocation(result_set.getString("current_date_of_posting_to_location"));
                result.setDateOfPostingToPresentLocationDesc(result_set.getString("_current_date_of_posting_to_location"));
                result.setLengthOfStay(viewHelper.calculatLenghtOfStay(currentDate,
                        result_set.getString("current_date_of_posting_to_location"), "ymd"));
            }
        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }

        return result;
    }

}
