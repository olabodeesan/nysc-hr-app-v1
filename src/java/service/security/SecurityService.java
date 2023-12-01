/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.security;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import app.dto.AppUserDTO;
import app.exceptions.AppException;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author IronHide
 */
public class SecurityService implements Serializable {

    private ServiceUtil serviceUtil;
    private DBUtil dbUtil;

    public SecurityService() {
        serviceUtil = new ServiceUtil();
        dbUtil = new DBUtil();
    }

    public AppUserDTO authenticateDBUser(String username, String password) throws AppException {

        AppUserDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        StringBuilder strBuilder;

        try {
            db_conn = dbUtil.getDatasource().getConnection();
            strBuilder = new StringBuilder();
            strBuilder.append("select emp.surname, emp.other_names, emp.primary_phone,emp.primary_email, concat_ws(' ',emp.surname, emp.other_names) as _full_name ")
                    .append(" ,emp.current_location, loc.description as _location_description ")
                    .append(" ,sysuser.user_id,sysuser.profile_type,sysuser.username ")
                    .append(" ,sysuser.pass_word,sysuser.record_status,sysuser.primary_role_id,sysuser.profile_selector,sysuser.first_login ")
                    .append(" ,sysuser.personnel_file_no ")
                    .append(" from system_users sysuser ")
                    .append(" left join employee emp on sysuser.personnel_file_no=emp.file_no ")
                    .append(" left join setup_states loc on emp.current_location=loc.record_id ")
                    .append(" where sysuser.username=? and sysuser.pass_word=sha1(?) ");

            String user_check = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(user_check);
            prep_stmt.setString(1, username);
            prep_stmt.setString(2, password);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {

                if (StringUtils.equalsIgnoreCase(result_set.getString("record_status"), AppConstants.ACTIVE)) {

                    result = new AppUserDTO();
                    result.setUsername(result_set.getString("username"));
                    result.setFullName(result_set.getString("_full_name"));
                    result.setProfileType(result_set.getString("profile_type"));
                    result.setPersonnelFileNo(result_set.getString("personnel_file_no"));
                    result.setLocationId(result_set.getString("current_location"));
                    result.setLocationDescription(result_set.getString("_location_description"));

                    //result.setStaffNo(result_set.getString("staff_no"));
                    //result.setStaffRefNo(result_set.getString("staff_ref_no"));
                    //result.setSurname(result_set.getString("surname"));
                    //result.setOtherNames(result_set.getString("other_names"));
                    //result.setDepartment(result_set.getString("department"));
                    //result.setPhone(result_set.getString("phone"));
                    //result.setEmail(result_set.getString("email"));
                    //result.setPrimaryRole(result_set.getString("primary_role"));
                    //fetch his roles
//                    String role_query = "select * from system_user_roles where username=?";
//                    prep_stmt = db_conn.prepareStatement(role_query);
//                    prep_stmt.setString(1, username);
//                    result_set = prep_stmt.executeQuery();
//                    while (result_set.next()) {
//                        result.getRoleList().add(result_set.getString("system_role"));
//                    }
                } else {
                    throw new AppException(AppConstants.INACTIVE);
                }
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

    public AppUserDTO authenticateADUser(String username, String password) throws AppException {

        AppUserDTO result = null;

        return result;
    }

    public boolean changeDBProfilePassword(String username, String oldPassword, String newPassword)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dbUtil.getDatasource().getConnection();

            String query = "select username from system_users where username=? and sha1(?)=password and record_status=?";

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, username);
            prep_stmt.setString(2, oldPassword);
            prep_stmt.setString(3, AppConstants.ACTIVE);

            result_set = prep_stmt.executeQuery();

            boolean found = result_set.next();

            if (found == false) {
                throw new AppException(AppConstants.NOT_FOUND);
            }

            if (found) {

                String updateQuery = "update system_users set pass_word=sha1(?) where username=?";

                prep_stmt = db_conn.prepareStatement(updateQuery);
                prep_stmt.setString(1, newPassword);
                prep_stmt.setString(2, username);

                prep_stmt.executeUpdate();

                result = true;

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

    public boolean changeStaffDBPassword(String username, String newPassword)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dbUtil.getDatasource().getConnection();

            String updateQuery = "update system_users set pass_word=sha1(?) where username=?";

            prep_stmt = db_conn.prepareStatement(updateQuery);
            prep_stmt.setString(1, newPassword);
            prep_stmt.setString(2, username);

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

    public boolean resetDBPassword(String email, String newPassword)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dbUtil.getDatasource().getConnection();

            String query = "select username from system_users where email=?";

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, email);

            result_set = prep_stmt.executeQuery();

            boolean found = result_set.next();

            if (found == false) {
                throw new AppException(AppConstants.NOT_FOUND);
            }

            if (found) {

                String updateQuery = "update system_users set pass_word=sha1(?) where email=?";

                prep_stmt = db_conn.prepareStatement(updateQuery);
                prep_stmt.setString(1, newPassword);
                prep_stmt.setString(2, email);

                prep_stmt.executeUpdate();

                result = true;

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

}
