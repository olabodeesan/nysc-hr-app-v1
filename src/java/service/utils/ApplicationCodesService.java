/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.utils;

import dto.setup.RankDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import view.utils.AppConstants;

/**
 *
 * @author IronHide
 */
public class ApplicationCodesService {

    private final ServiceUtil serviceUtil;
    private final DBUtil dbUtil;

    public ApplicationCodesService() {
        serviceUtil = new ServiceUtil();
        dbUtil = new DBUtil();
    }

    public Map<String, String> getStaffAccounts() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select concat_ws('-',primary_role,username) as account_info")
                .append(" ,concat_ws(' ',surname,othernames) as fullname from system_users")
                .append(" where username<>'sysadmin' and record_status='ACTIVE'");

        String query = queryBuilder.toString();

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("account_info"), result_set.getString("fullname"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getPrimarySystemRoles() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select role_code,description from system_roles where role_type='PRIMARY'";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("role_code"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getSystemConfigs() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select config_code,config_value from system_config";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("config_code"), result_set.getString("config_value"));
            }

        } catch (Exception exc) {
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getCadre() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_cadre where record_status='ACTIVE'";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getContinents() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_continents where record_status='ACTIVE'";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getCountries() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_countries where record_status='ACTIVE'";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getDepartments() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_departments where record_status='ACTIVE'";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getPositionAppointments() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_position_appointment where record_status='ACTIVE'";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getQualifications() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id, concat('(',abbreviation,') ',description) as description  from setup_qualifications where record_status='ACTIVE'";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getQualificationsByCategory(String institutionType) {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select record_id, concat('(',abbreviation,') ',description) as description ")
                .append("from setup_qualifications where institution_type=? and record_status='ACTIVE'");
        String query = strBuilder.toString();

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, institutionType);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getMaritalStatus() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id, description  from setup_marital_status where record_status='ACTIVE'";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getStates() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_states where location_type=? and record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, AppConstants.LOCATION_STATE);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getStateLGAs(String state_id) {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_lgas where state_id=? and record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, state_id);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getGradeLevel() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select grade_level from setup_grade_levels where record_status='ACTIVE' order by grade_level";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("grade_level"), result_set.getString("grade_level"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getGradeStep(String grade_level) {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dbUtil.getDatasource().getConnection();

            //first get the grade level min/max step
            String query = "select min_step,max_step from setup_grade_level_step_config where grade_level=?";
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, grade_level);
            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {

                String min_step = result_set.getString("min_step");
                String max_step = result_set.getString("max_step");

                query = "select grade_step from setup_grade_steps where (grade_step between ? and ?) and record_status='ACTIVE' order by grade_step";
                prep_stmt = db_conn.prepareStatement(query);
                prep_stmt.setString(1, min_step);
                prep_stmt.setString(2, max_step);
                result_set = prep_stmt.executeQuery();

                while (result_set.next()) {
                    result.put(result_set.getString("grade_step"), result_set.getString("grade_step"));
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

    public Map<String, String> getLocations() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_states where record_status='ACTIVE' order by location_type";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getInstitutionTypes() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_institution_types where record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getCourses() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_courses where record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getRelationships() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_relationships where record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public RankDTO getRank(String cadre, String grade_level) {

        RankDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select * from setup_rank where cadre_id=? and grade_level=? and record_status='ACTIVE' order by description limit 1";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, cadre);
            prep_stmt.setString(2, grade_level);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = new RankDTO();
                result.setRecordId(result_set.getString("record_id"));
                result.setDescription(result_set.getString("description"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getActivePersonnelStatus() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_personnel_status where status_type=? and record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, AppConstants.STATUS_ACTIVE);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getSuspendedStatus() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_personnel_status where status_type=? and record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, AppConstants.STATUS_SUSPENDED);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getDisengagementStatus() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_personnel_status where status_type=? and record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, AppConstants.STATUS_DISENGAGED);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getEmployeeStatusReasons(String status_id) {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_personnel_status_reasons where status_id=? and record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, status_id);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getTrainingCerts() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_training_certificates where record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getTrainingApprovalCodes(boolean fetchPending) {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select code_key,description from setup_training_approval where ");
        if (fetchPending) {
            strBuilder.append(" code_key<>? and ");
        }
        strBuilder.append(" record_status='ACTIVE' order by description ");
        String query = strBuilder.toString();

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            if (fetchPending) {
                prep_stmt.setString(1, AppConstants.TRAINING_PENDING_APPROVAL);
            }

            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("code_key"));
            }

        } catch (Exception exc) {
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getTrainingSpecializations() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_training_specializations where record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getTrainingTypes() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_training_type where record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getTrainingConsultant() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_training_consultant where record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getLeaveApprovalCodes() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select code_key,description from setup_registry_leave_approval where description<>? and record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, AppConstants.LEAVE_PENDING_APPROVAL);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("code_key"));
            }

        } catch (Exception exc) {
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getGradeLevelAnnualLeaveDurations() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select grade_level,duration from setup_gradelevel_annualleave_duration";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("grade_level"), result_set.getString("duration"));
            }

        } catch (Exception exc) {
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getPostingReason() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_posting_reason where record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("record_id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getPensionFundAdminstrator() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select id,description from setup_pfas where record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("id"));
            }

        } catch (Exception exc) {

        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public Map<String, String> getBanker() {

        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select id,description from setup_bank where record_status='ACTIVE' order by description";

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {
                result.put(result_set.getString("description"), result_set.getString("id"));
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
