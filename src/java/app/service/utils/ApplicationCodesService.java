/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.service.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author GAINSolutions
 */
public class ApplicationCodesService {

    private ServiceUtil serviceUtil;
    private DBUtil dbUtil;

    public ApplicationCodesService() {
        serviceUtil = new ServiceUtil();
        dbUtil = new DBUtil();
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
    
     public Map<String, String> getStates() {
        Map<String, String> result = new LinkedHashMap<>();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String query = "select record_id,description from setup_states where record_status='ACTIVE'";

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

}
