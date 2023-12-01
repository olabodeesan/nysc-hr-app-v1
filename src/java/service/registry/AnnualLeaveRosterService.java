/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.registry;

import app.exceptions.AppException;
import dto.setup.dto.registry.AnnualLeaveRosterDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;

/**
 *
 * @author IronHide
 */
public class AnnualLeaveRosterService implements Serializable {

    private final ServiceUtil serviceUtil;
    private final DBUtil dBUtil;

    public AnnualLeaveRosterService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
    }

    public Map<String, Object> searchRecords(String countQuery, String selectQuery, int startRow, int pagesize)
            throws AppException {

        Integer countInt = 0;
        Map<String, Object> result = new LinkedHashMap<>();
        List<AnnualLeaveRosterDTO> dto_list = new ArrayList<>();
        AnnualLeaveRosterDTO dto = null;
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

                dto = new AnnualLeaveRosterDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                dto.setEmployeeFileNo(result_set.getString("file_no"));
                dto.setGradeLevel(result_set.getString("current_grade_level"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));
                dto.setPhone(result_set.getString("primary_phone"));
                dto.setEmail(result_set.getString("primary_email"));
                dto.setDepartment(result_set.getString("_department"));

                dto.setRosterYear(result_set.getString("roster_year"));
                dto.setDuration(result_set.getString("duration"));
                dto.setExpectedStartDate(result_set.getString("_expected_start_date"));
                dto.setExpectedEndDate(result_set.getString("_expected_end_date"));
                dto.setRemainingDays(result_set.getString("remaining_days"));
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

    public AnnualLeaveRosterDTO getAnnualLeaveRoster(String recordId)
            throws AppException {

        AnnualLeaveRosterDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("SELECT d.record_id,d.roster_year,d.employee_rec_id,d.duration ")
                    .append(",date_format(d.expected_start_date,'%d/%m/%Y') as _expected_start_date  ")
                    .append(",date_format(d.expected_end_date,'%d/%m/%Y') as _expected_end_date ")
                    .append(",d.remaining_days,d.last_mod,d.last_mod_by ")
                    .append(",t1.file_no,t1.surname,t1.other_names,t1.current_grade_level ")
                    .append(",t1.primary_phone,t1.primary_email ")
                    .append(",t2.description as _department ")
                    .append("FROM registry_annual_leave_roster d ")
                    .append("join employee t1 on d.employee_rec_id=t1.employee_rec_id ")
                    .append("join setup_departments t2 on t1.current_dept=t2.record_id ")
                    .append(" where d.record_id=? ");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, recordId);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = new AnnualLeaveRosterDTO();
                result.setRecordId(result_set.getString("record_id"));
                result.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                result.setEmployeeFileNo(result_set.getString("file_no"));
                result.setGradeLevel(result_set.getString("current_grade_level"));
                result.setSurname(result_set.getString("surname"));
                result.setOtherNames(result_set.getString("other_names"));
                result.setPhone(result_set.getString("primary_phone"));
                result.setEmail(result_set.getString("primary_email"));
                result.setDepartment(result_set.getString("_department"));

                result.setRosterYear(result_set.getString("roster_year"));
                result.setDuration(result_set.getString("duration"));

                result.setExpectedStartDate(result_set.getString("_expected_start_date"));
                result.setOldExpectedStartDate(result.getExpectedStartDate());

                result.setExpectedEndDate(result_set.getString("_expected_end_date"));
                result.setRemainingDays(result_set.getString("remaining_days"));
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

    public boolean createAnnualLeaveRoster(AnnualLeaveRosterDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            //check for duplicate
            String query = "select record_id from registry_annual_leave_roster where employee_rec_id=? and roster_year=?";
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getEmployeeRecordId());
            prep_stmt.setString(2, dto.getRosterYear());

            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE);
            }

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("insert into registry_annual_leave_roster ")
                    .append("(roster_year,employee_rec_id,duration,expected_start_date,expected_end_date,remaining_days")
                    .append(",created,created_by,last_mod,last_mod_by )")
                    .append(" values (?,?,?,?,?,?,?,?,?,?)");

            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getRosterYear());
            prep_stmt.setString(2, dto.getEmployeeRecordId());
            prep_stmt.setString(3, dto.getDuration());
            prep_stmt.setString(4, serviceUtil.resetToDbDateFormat(dto.getExpectedStartDate()));
            prep_stmt.setString(5, serviceUtil.resetToDbDateFormat(dto.getExpectedEndDate()));
            prep_stmt.setString(6, dto.getRemainingDays());
            prep_stmt.setString(7, dto.getCreated());
            prep_stmt.setString(8, dto.getCreatedBy());
            prep_stmt.setString(9, dto.getLastMod());
            prep_stmt.setString(10, dto.getLastModBy());

            prep_stmt.executeUpdate();

            result = true;

        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean updateAnnualLeaveRoster(AnnualLeaveRosterDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //check for duplicate
            String query = "select record_id from registry_annual_leave_roster where record_id<>? and employee_rec_id=? and roster_year=?";
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getRecordId());
            prep_stmt.setString(2, dto.getEmployeeRecordId());
            prep_stmt.setString(3, dto.getRosterYear());

            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE);
            }

            //continue only if personel has taken no leave yet
            query = "select duration,remaining_days from registry_annual_leave_roster where record_id=? and employee_rec_id=?";
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getRecordId());
            prep_stmt.setString(2, dto.getEmployeeRecordId());

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                String _duration = result_set.getString("duration");
                String _remaining_days = result_set.getString("remaining_days");
                boolean can_update = StringUtils.equals(_remaining_days, _duration);

                if (can_update) {
                    //log old record
                    logAnnualLeaveRoster(db_conn, dto);

                    //continue update
                    StringBuilder strBuilder = new StringBuilder();
                    strBuilder.append("update registry_annual_leave_roster ")
                            .append("set ")
                            .append("duration=? ")
                            .append(",expected_start_date=? ")
                            .append(",expected_end_date=? ")
                            .append(",remaining_days=? ")
                            .append(",last_mod=? ")
                            .append(",last_mod_by=? ")
                            .append(" where record_id=?");

                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);

                    prep_stmt.setString(1, dto.getDuration());
                    prep_stmt.setString(2, serviceUtil.resetToDbDateFormat(dto.getExpectedStartDate()));
                    prep_stmt.setString(3, serviceUtil.resetToDbDateFormat(dto.getExpectedEndDate()));
                    prep_stmt.setString(4, dto.getRemainingDays());
                    prep_stmt.setString(5, dto.getLastMod());
                    prep_stmt.setString(6, dto.getLastModBy());
                    prep_stmt.setString(7, dto.getRecordId());

                    prep_stmt.executeUpdate();

                    db_conn.commit();
                    result = true;
                } else {
                    throw new AppException(AppConstants.ANNUAL_ROSTER_IN_USE);
                }
            } else {
                throw new AppException(AppConstants.NOT_FOUND);
            }

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

    //history log
    public void logAnnualLeaveRoster(Connection log_db_conn, AnnualLeaveRosterDTO dto)
            throws AppException {

        //NO NEED TO RE-INIT CONNECTION
        //ITS PASSED
        PreparedStatement prep_stmt = null;

        try {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("insert into registry_annual_leave_roster_history ")
                    .append("(leave_roster_id,roster_year,employee_rec_id,duration,expected_start_date,expected_end_date,remaining_days")
                    .append(",last_mod,last_mod_by )")
                    .append(" (")
                    .append("select record_id ,roster_year,employee_rec_id,duration,expected_start_date,expected_end_date,remaining_days")
                    .append(",last_mod,last_mod_by ")
                    .append(" from registry_annual_leave_roster ")
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
