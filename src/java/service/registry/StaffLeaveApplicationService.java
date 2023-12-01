/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.registry;

import app.exceptions.AppException;
import dto.setup.dto.registry.StaffLeaveApplicationDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;

/**
 *
 * @author IronHide
 */
public class StaffLeaveApplicationService implements Serializable {

    private final ServiceUtil serviceUtil;
    private final DBUtil dBUtil;

    public StaffLeaveApplicationService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
    }

    public Map<String, Object> searchRecords(String countQuery, String selectQuery, int startRow, int pagesize)
            throws AppException {

        Integer countInt = 0;
        Map<String, Object> result = new LinkedHashMap<>();
        List<StaffLeaveApplicationDTO> dto_list = new ArrayList<>();
        StaffLeaveApplicationDTO dto = null;
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

                dto = new StaffLeaveApplicationDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                dto.setEmployeeFileNo(result_set.getString("file_no"));
                dto.setGradeLevel(result_set.getString("current_grade_level"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));
                dto.setPhone(result_set.getString("primary_phone"));
                dto.setEmail(result_set.getString("primary_email"));
                dto.setDepartment(result_set.getString("_department"));

                dto.setLeaveApplicationYear(result_set.getString("leave_application_year"));
                dto.setDuration(result_set.getString("duration"));

                dto.setExpectedStartDate(result_set.getString("_start_date"));
                dto.setOldExpectedStartDate(dto.getExpectedStartDate());

                dto.setExpectedEndDate(result_set.getString("_expected_end_date"));

                dto.setBeforeApprovalRemainingDays(result_set.getString("before_approval_remaining_days"));
                dto.setAfterApprovalRemainingDays(result_set.getString("after_approval_remaining_days"));
                dto.setApprovalStatus(result_set.getString("approval_status"));
                dto.setRemarks(result_set.getString("remarks"));
                dto.setRecordStatus(result_set.getString("record_status"));

                dto.setLastMod(result_set.getString("last_mod"));
                dto.setLastModBy(result_set.getString("last_mod_by"));

                dto_list.add(dto);

            }

        } catch (Exception exc) {
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

    public StaffLeaveApplicationDTO getStaffLeaveApplication(String recordId)
            throws AppException {

        StaffLeaveApplicationDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("SELECT d.record_id,d.employee_rec_id,d.duration,d.leave_application_year")
                    .append(",date_format(d.start_date,'%d/%m/%Y') as _start_date")
                    .append(",date_format(d.expected_end_date,'%d/%m/%Y') as _expected_end_date")
                    .append(",d.before_approval_remaining_days,d.after_approval_remaining_days ")
                    .append(",d.approval_status,d.remarks,d.record_status,d.last_mod,d.last_mod_by")
                    .append(",t1.file_no,t1.surname,t1.other_names,t1.current_grade_level ")
                    .append(",t1.primary_phone,t1.primary_email ")
                    .append(",t2.description as _department ")
                    .append("FROM registry_staff_leave_application d ")
                    .append("join employee t1 on d.employee_rec_id=t1.employee_rec_id ")
                    .append("join setup_departments t2 on t1.current_dept=t2.record_id ")
                    .append(" where d.record_id=? ");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, recordId);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = new StaffLeaveApplicationDTO();
                result.setRecordId(result_set.getString("record_id"));
                result.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                result.setEmployeeFileNo(result_set.getString("file_no"));
                result.setGradeLevel(result_set.getString("current_grade_level"));
                result.setSurname(result_set.getString("surname"));
                result.setOtherNames(result_set.getString("other_names"));
                result.setPhone(result_set.getString("primary_phone"));
                result.setEmail(result_set.getString("primary_email"));
                result.setDepartment(result_set.getString("_department"));

                result.setLeaveApplicationYear(result_set.getString("leave_application_year"));
                result.setDuration(result_set.getString("duration"));

                result.setExpectedStartDate(result_set.getString("_start_date"));
                result.setOldExpectedStartDate(result.getExpectedStartDate());

                result.setExpectedEndDate(result_set.getString("_expected_end_date"));

                result.setBeforeApprovalRemainingDays(result_set.getString("before_approval_remaining_days"));
                result.setAfterApprovalRemainingDays(result_set.getString("after_approval_remaining_days"));
                result.setApprovalStatus(result_set.getString("approval_status"));
                result.setRemarks(result_set.getString("remarks"));
                result.setRecordStatus(result_set.getString("record_status"));

                result.setLastMod(result_set.getString("last_mod"));
                result.setLastModBy(result_set.getString("last_mod_by"));

            } else {
                throw new AppException(AppConstants.NOT_FOUND);
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

    public boolean createStaffLeaveApplication(StaffLeaveApplicationDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //first check if there is any active pending approvals, dont allow
            String query = "select record_id from registry_staff_leave_application where employee_rec_id=? and approval_status=? and record_status=?";
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getEmployeeRecordId());
            prep_stmt.setString(2, AppConstants.LEAVE_PENDING_APPROVAL);
            prep_stmt.setString(3, AppConstants.ACTIVE);

            result_set = prep_stmt.executeQuery();

            boolean old_pending_exists = result_set.next();

            if (old_pending_exists) {
                throw new AppException(AppConstants.ACTIVE_PENDING_EXISTS);
            }

            //get the remaining days
            query = "select remaining_days from registry_annual_leave_roster where employee_rec_id=? and roster_year=? FOR UPDATE";
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getEmployeeRecordId());
            prep_stmt.setString(2, dto.getLeaveApplicationYear());

            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            if (exists) {

                //check that it can acomodate application days
                int _old_annual_remaining_days = result_set.getInt("remaining_days");
                int application_duration = NumberUtils.toInt(dto.getDuration());

                boolean _can_take_days = _old_annual_remaining_days >= application_duration;

                if (_can_take_days) {

                    //insert the application
                    StringBuilder strBuilder = new StringBuilder();
                    strBuilder.append("insert into registry_staff_leave_application ")
                            .append("(employee_rec_id,duration,leave_application_year,start_date,expected_end_date")
                            .append(",before_approval_remaining_days,after_approval_remaining_days,approval_status,remarks,record_status")
                            .append(",created,created_by,last_mod,last_mod_by )")
                            .append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);

                    prep_stmt.setString(1, dto.getEmployeeRecordId());
                    prep_stmt.setString(2, dto.getDuration());
                    prep_stmt.setString(3, dto.getLeaveApplicationYear());
                    prep_stmt.setString(4, serviceUtil.resetToDbDateFormat(dto.getExpectedStartDate()));
                    prep_stmt.setString(5, serviceUtil.resetToDbDateFormat(dto.getExpectedEndDate()));
                    prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getBeforeApprovalRemainingDays()));
                    prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getAfterApprovalRemainingDays()));
                    prep_stmt.setString(8, dto.getApprovalStatus());
                    prep_stmt.setString(9, serviceUtil.getValueOrNull(dto.getRemarks()));
                    prep_stmt.setString(10, dto.getRecordStatus());
                    prep_stmt.setString(11, dto.getCreated());
                    prep_stmt.setString(12, dto.getCreatedBy());
                    prep_stmt.setString(13, dto.getLastMod());
                    prep_stmt.setString(14, dto.getLastModBy());

                    prep_stmt.executeUpdate();

                    db_conn.commit();
                    result = true;

                } else {
                    throw new AppException(AppConstants.LEAVE_DAYS_TOO_MUCH);
                }

            } else {
                throw new AppException(AppConstants.NO_ANNUAL_LEAVE_ROSTER);
            }

        } catch (Exception exc) {
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

    public boolean updateStaffLeaveApplication(StaffLeaveApplicationDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //log old record
            logStaffLeaveApplication(db_conn, dto);

            //continue update
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update registry_staff_leave_application ")
                    .append("set ")
                    .append("start_date=? ")
                    .append(",expected_end_date=? ")
                    .append(",remarks=? ")
                    .append(",last_mod=? ")
                    .append(",last_mod_by=? ")
                    .append(" where record_id=?");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, serviceUtil.resetToDbDateFormat(dto.getExpectedStartDate()));
            prep_stmt.setString(2, serviceUtil.resetToDbDateFormat(dto.getExpectedEndDate()));
            prep_stmt.setString(3, dto.getRemarks());
            prep_stmt.setString(4, dto.getLastMod());
            prep_stmt.setString(5, dto.getLastModBy());
            prep_stmt.setString(6, dto.getRecordId());

            prep_stmt.executeUpdate();

            db_conn.commit();
            result = true;

        } catch (Exception exc) {
            System.out.println(exc.getMessage());
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

    public boolean approveStaffLeaveApplication(StaffLeaveApplicationDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //log old record
            logStaffLeaveApplication(db_conn, dto);

            String approvalStatus = dto.getApprovalStatus();

            if (!StringUtils.equals(approvalStatus, AppConstants.APPROVED)) {
                //NOT APPROVED
                //continue update
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("update registry_staff_leave_application ")
                        .append("set ")
                        .append("approval_status=? ")
                        .append(",last_mod=? ")
                        .append(",last_mod_by=? ")
                        .append(" where record_id=?");

                String query = strBuilder.toString();
                prep_stmt = db_conn.prepareStatement(query);

                prep_stmt.setString(1, dto.getApprovalStatus());
                prep_stmt.setString(2, dto.getLastMod());
                prep_stmt.setString(3, dto.getLastModBy());
                prep_stmt.setString(4, dto.getRecordId());

                prep_stmt.executeUpdate();

            } else { //IT WAS APPROVED

                //continue
                //get the remaining days
                String query = "select remaining_days from registry_annual_leave_roster where employee_rec_id=? and roster_year=? FOR UPDATE";
                prep_stmt = db_conn.prepareStatement(query);
                prep_stmt.setString(1, dto.getEmployeeRecordId());
                prep_stmt.setString(2, dto.getLeaveApplicationYear());

                result_set = prep_stmt.executeQuery();
                boolean exists = result_set.next();
                if (exists) {

                    //check that it can acomodate application days
                    int _old_annual_remaining_days = result_set.getInt("remaining_days");
                    int application_duration = NumberUtils.toInt(dto.getDuration());

                    boolean _can_take_days = _old_annual_remaining_days >= application_duration;

                    if (_can_take_days) {

                        int _new_annual_remaining_days = _old_annual_remaining_days - application_duration;

                        //continue update
                        StringBuilder strBuilder = new StringBuilder();
                        strBuilder.append("update registry_staff_leave_application ")
                                .append("set ")
                                .append("approval_status=? ")
                                .append(",before_approval_remaining_days=? ")
                                .append(",after_approval_remaining_days=? ")
                                .append(",last_mod=? ")
                                .append(",last_mod_by=? ")
                                .append(" where record_id=?");

                        query = strBuilder.toString();
                        prep_stmt = db_conn.prepareStatement(query);

                        prep_stmt.setString(1, dto.getApprovalStatus());
                        prep_stmt.setInt(2, _old_annual_remaining_days);
                        prep_stmt.setInt(3, _new_annual_remaining_days);
                        prep_stmt.setString(4, dto.getLastMod());
                        prep_stmt.setString(5, dto.getLastModBy());
                        prep_stmt.setString(6, dto.getRecordId());

                        prep_stmt.executeUpdate();

                        //update the annual remaining days
                        strBuilder = new StringBuilder();
                        strBuilder.append("update registry_annual_leave_roster ")
                                .append(" set ")
                                .append(" remaining_days=? ")
                                .append(" ,last_mod=? ")
                                .append(" ,last_mod_by=? ")
                                .append(" where employee_rec_id=? and roster_year=? ");
                        query = strBuilder.toString();

                        prep_stmt = db_conn.prepareStatement(query);
                        prep_stmt.setInt(1, _new_annual_remaining_days);
                        prep_stmt.setString(2, dto.getLastMod());
                        prep_stmt.setString(3, dto.getLastModBy());
                        prep_stmt.setString(4, dto.getEmployeeRecordId());
                        prep_stmt.setString(5, dto.getLeaveApplicationYear());

                        prep_stmt.executeUpdate();

                    } else {
                        throw new AppException(AppConstants.LEAVE_DAYS_TOO_MUCH);
                    }

                } else {
                    throw new AppException(AppConstants.NO_ANNUAL_LEAVE_ROSTER);
                }

            }

            db_conn.commit();
            result = true;

        } catch (Exception exc) {
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

    public boolean deleteStaffLeaveApplication(StaffLeaveApplicationDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //log old record
            logStaffLeaveApplication(db_conn, dto);

            //continue
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update registry_staff_leave_application ")
                    .append("set ")
                    .append("record_status=? ")
                    .append(",last_mod=? ")
                    .append(",last_mod_by=? ")
                    .append(" where record_id=?");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getRecordStatus());
            prep_stmt.setString(2, dto.getLastMod());
            prep_stmt.setString(3, dto.getLastModBy());
            prep_stmt.setString(4, dto.getRecordId());

            prep_stmt.executeUpdate();

            db_conn.commit();
            result = true;

        } catch (Exception exc) {
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

    //history log
    public void logStaffLeaveApplication(Connection log_db_conn, StaffLeaveApplicationDTO dto)
            throws AppException {

        //NO NEED TO RE-INIT CONNECTION
        //ITS PASSED
        PreparedStatement prep_stmt = null;

        try {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("insert into registry_staff_leave_application_history ")
                    .append("(leave_applciation_id,employee_rec_id,duration,leave_application_year,start_date,expected_end_date")
                    .append(",before_approval_remaining_days,after_approval_remaining_days,approval_status,remarks,record_status")
                    .append(",last_mod,last_mod_by )")
                    .append(" (")
                    .append("select record_id ,employee_rec_id,duration,leave_application_year,start_date,expected_end_date")
                    .append(",before_approval_remaining_days,after_approval_remaining_days,approval_status,remarks,record_status")
                    .append(",last_mod,last_mod_by ")
                    .append(" from registry_staff_leave_application ")
                    .append(" where record_id=? ")
                    .append(") ");

            String query = strBuilder.toString();
            prep_stmt = log_db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getRecordId());

            prep_stmt.executeUpdate();

        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(prep_stmt);
        }
    }

}
