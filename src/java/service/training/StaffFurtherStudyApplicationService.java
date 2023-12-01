/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.training;

import app.exceptions.AppException;
import dto.setup.dto.training.StaffFurtherStudyApplicationDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.math.NumberUtils;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;

/**
 *
 * @author IronHide
 */
public class StaffFurtherStudyApplicationService implements Serializable {

    private final ServiceUtil serviceUtil;
    private final DBUtil dBUtil;

    public StaffFurtherStudyApplicationService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
    }

    public Map<String, Object> searchRecords(String countQuery, String selectQuery, int startRow, int pagesize)
            throws AppException {

        Integer countInt = 0;
        Map<String, Object> result = new LinkedHashMap<>();
        List<StaffFurtherStudyApplicationDTO> dto_list = new ArrayList<>();
        StaffFurtherStudyApplicationDTO dto = null;
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

                dto = new StaffFurtherStudyApplicationDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                dto.setEmployeeFileNo(result_set.getString("file_no"));
                dto.setGradeLevel(result_set.getString("current_grade_level"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));
                dto.setPhone(result_set.getString("primary_phone"));
                dto.setEmail(result_set.getString("primary_email"));
                dto.setDepartment(result_set.getString("_department"));
                
                dto.setInstitution(result_set.getString("institution"));
                dto.setCourse(result_set.getString("course"));
                dto.setCourseName(result_set.getString("_course"));
                
                dto.setCountry(result_set.getString("country"));
                dto.setCountryName(result_set.getString("_country"));
                
                int state_id = NumberUtils.toInt(result_set.getString("state"));
                if(state_id!=0){
                    dto.setState(result_set.getString("state"));
                }else{
                    dto.setLocation(result_set.getString("_state"));
                }
                
                dto.setStateName(result_set.getString("_state"));
                dto.setExpectedStartDate(result_set.getString("_start_date"));
                dto.setExpectedEndDate(result_set.getString("_end_date"));
                dto.setApprovalStatus(result_set.getString("approval_status"));
                dto.setApprovalStatusReason(result_set.getString("approval_status_reason"));
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

    public StaffFurtherStudyApplicationDTO getStaffFurtherStudyApplication(String recordId)
            throws AppException {

        StaffFurtherStudyApplicationDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("SELECT d.record_id,d.employee_rec_id,d.institution,d.course,d.country,d.state ")
                    .append(",date_format(d.start_date,'%d/%m/%Y') as _start_date")
                    .append(",date_format(d.end_date,'%d/%m/%Y') as _end_date")
                    .append(",d.approval_status,d.approval_status_reason")
                    .append(",d.remarks,d.record_status,d.last_mod,d.last_mod_by ")
                    .append(",t1.file_no,t1.surname,t1.other_names,t1.current_grade_level ")
                    .append(",t1.primary_phone,t1.primary_email ")
                    .append(",t2.description as _country ")
                    .append(",ifnull(t3.description,d.state) as _state ")
                    .append(",t4.description as _course ")
                    .append(",t5.description as _department ")
                    .append("FROM staff_further_study_application d ")
                    .append("join employee t1 on d.employee_rec_id=t1.employee_rec_id ")
                    .append("join setup_countries t2 on d.country=t2.record_id ")
                    .append("left outer join setup_states t3 on d.state=t3.record_id ")
                    .append("join setup_courses t4 on d.course=t4.record_id ")
                    .append("join setup_departments t5 on t1.current_dept=t5.record_id ")
                    .append(" where d.record_id=? ");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, recordId);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = new StaffFurtherStudyApplicationDTO();
                result.setRecordId(result_set.getString("record_id"));
                result.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                result.setEmployeeFileNo(result_set.getString("file_no"));
                result.setGradeLevel(result_set.getString("current_grade_level"));
                result.setSurname(result_set.getString("surname"));
                result.setOtherNames(result_set.getString("other_names"));
                result.setPhone(result_set.getString("primary_phone"));
                result.setEmail(result_set.getString("primary_email"));
                result.setDepartment(result_set.getString("_department"));
                
                result.setInstitution(result_set.getString("institution"));
                
                result.setCourse(result_set.getString("course"));
                result.setCourseName(result_set.getString("_course"));
                
                result.setCountry(result_set.getString("country"));
                result.setCountryName(result_set.getString("_country"));
                
                int state_id = NumberUtils.toInt(result_set.getString("state"));
                if(state_id!=0){
                    result.setState(result_set.getString("state"));
                }else{
                    result.setLocation(result_set.getString("_state"));
                }
                
                result.setStateName(result_set.getString("_state"));
                result.setExpectedStartDate(result_set.getString("_start_date"));
                result.setExpectedEndDate(result_set.getString("_end_date"));
                result.setApprovalStatus(result_set.getString("approval_status"));
                result.setApprovalStatusReason(result_set.getString("approval_status_reason"));
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

    public boolean createStaffFurtherStudyApplication(StaffFurtherStudyApplicationDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            //first check if there is any active pending approvals, dont allow
            String query = "select record_id from staff_further_study_application where employee_rec_id=? and approval_status=? and record_status=?";
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getEmployeeRecordId());
            prep_stmt.setString(2, AppConstants.TRAINING_PENDING_APPROVAL);
            prep_stmt.setString(3, AppConstants.ACTIVE);

            result_set = prep_stmt.executeQuery();

            boolean old_pending_exists = result_set.next();

            if (old_pending_exists) {
                throw new AppException(AppConstants.ACTIVE_PENDING_EXISTS);
            }

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("insert into staff_further_study_application ")
                    .append("(employee_rec_id,institution,course,country,state,start_date,end_date")
                    .append(",approval_status,approval_status_reason,remarks,record_status,created,created_by,last_mod,last_mod_by )")
                    .append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getEmployeeRecordId());
            prep_stmt.setString(2, dto.getInstitution());
            prep_stmt.setString(3, dto.getCourse());
            prep_stmt.setString(4, dto.getCountry());
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getState()));
            prep_stmt.setString(6, serviceUtil.resetToDbDateFormat(dto.getExpectedStartDate()));
            prep_stmt.setString(7, serviceUtil.resetToDbDateFormat(dto.getExpectedEndDate()));
            prep_stmt.setString(8, dto.getApprovalStatus());
            prep_stmt.setString(9, serviceUtil.getValueOrNull(dto.getApprovalStatusReason()));
            prep_stmt.setString(10, serviceUtil.getValueOrNull(dto.getRemarks()));
            prep_stmt.setString(11, dto.getRecordStatus());
            prep_stmt.setString(12, dto.getCreated());
            prep_stmt.setString(13, dto.getCreatedBy());
            prep_stmt.setString(14, dto.getLastMod());
            prep_stmt.setString(15, dto.getLastModBy());

            prep_stmt.executeUpdate();

            result = true;

        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean updateStaffFurtherStudyApplication(StaffFurtherStudyApplicationDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //log old record
            logFurtherStudy(db_conn, dto);

            //continue update
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update staff_further_study_application ")
                    .append("set ")
                    .append("institution=? ")
                    .append(",course=? ")
                    .append(",country=? ")
                    .append(",state=? ")
                    .append(",start_date=? ")
                    .append(",end_date=? ")
                    .append(",remarks=? ")
                    .append(",last_mod=? ")
                    .append(",last_mod_by=? ")
                    .append(" where record_id=?");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getInstitution());
            prep_stmt.setString(2, dto.getCourse());
            prep_stmt.setString(3, dto.getCountry());
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getState()));
            prep_stmt.setString(5, serviceUtil.resetToDbDateFormat(dto.getExpectedStartDate()));
            prep_stmt.setString(6, serviceUtil.resetToDbDateFormat(dto.getExpectedEndDate()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getRemarks()));
            prep_stmt.setString(8, dto.getLastMod());
            prep_stmt.setString(9, dto.getLastModBy());
            prep_stmt.setString(10, dto.getRecordId());

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

    public boolean approveStaffFurtherStudyApplication(StaffFurtherStudyApplicationDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //log old record
            logFurtherStudy(db_conn, dto);

            //continue
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update staff_further_study_application ")
                    .append("set ")
                    .append("approval_status=?")
                    .append(",approval_status_reason=?")
                    .append(",last_mod=? ")
                    .append(",last_mod_by=? ")
                    .append(" where record_id=?");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getApprovalStatus());
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getApprovalStatusReason()));
            prep_stmt.setString(3, dto.getLastMod());
            prep_stmt.setString(4, dto.getLastModBy());
            prep_stmt.setString(5, dto.getRecordId());

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

    public boolean deleteStaffFurtherStudyApplication(StaffFurtherStudyApplicationDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //log old record
            logFurtherStudy(db_conn, dto);

            //continue de-activate
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update staff_further_study_application ")
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
    public void logFurtherStudy(Connection log_db_conn, StaffFurtherStudyApplicationDTO dto)
            throws AppException {

        //NO NEED TO RE-INIT CONNECTION
        //ITS PASSED
        PreparedStatement prep_stmt = null;

        try {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("insert into staff_further_study_application_history ")
                    .append("(further_application_id,employee_rec_id,institution,course,country,state,start_date,end_date")
                    .append(",approval_status,approval_status_reason,remarks,record_status,last_mod,last_mod_by )")
                    .append(" (")
                    .append("select record_id ,employee_rec_id,institution,course,country,state,start_date,end_date")
                    .append(",approval_status,approval_status_reason,remarks,record_status,last_mod,last_mod_by ")
                    .append(" from staff_further_study_application ")
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
