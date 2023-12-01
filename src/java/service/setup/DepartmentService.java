/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service.setup;

import dto.setup.DepartmentDTO;
import app.exceptions.AppException;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author IronHide
 */
public class DepartmentService implements Serializable{
    
    private ServiceUtil serviceUtil;
    private DBUtil dBUtil;

    public DepartmentService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
    }

    public Map<String, Object> searchRecords(String countQuery, String selectQuery, int startRow, int pagesize)
            throws AppException {

        Integer countInt = 0;
        Map<String, Object> result = new HashMap<>();
        List<DepartmentDTO> dto_list = new ArrayList<>();
        DepartmentDTO dto = null;
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

                dto = new DepartmentDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setDescription(result_set.getString("description"));
                dto.setRecordStatus(result_set.getString("record_status"));

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

    public DepartmentDTO getDepartment(String recordId)
            throws AppException {

        DepartmentDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("select record_id,description,record_status from setup_departments where record_id=?");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, recordId);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = new DepartmentDTO();
                result.setRecordId(result_set.getString("record_id"));
                result.setDescription(result_set.getString("description"));
                result.setRecordStatus(result_set.getString("record_status"));

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

    public boolean createDepartment(DepartmentDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            //check duplicate name
            String check_query = "select record_id from setup_departments where description=?";

            prep_stmt = db_conn.prepareStatement(check_query);
            prep_stmt.setString(1, dto.getDescription());
            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE);
            }

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("insert into setup_departments ")
                    .append("(description,record_status,created,created_by,last_mod,last_mod_by)")
                    .append(" values (?,?,?,?,?,?)");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, serviceUtil.getValueOrEmpty(dto.getDescription()));
            prep_stmt.setString(2, serviceUtil.getValueOrEmpty(dto.getRecordStatus()));
            prep_stmt.setString(3, serviceUtil.getValueOrEmpty(dto.getCreated()));
            prep_stmt.setString(4, serviceUtil.getValueOrEmpty(dto.getCreatedBy()));
            prep_stmt.setString(5, serviceUtil.getValueOrEmpty(dto.getLastMod()));
            prep_stmt.setString(6, serviceUtil.getValueOrEmpty(dto.getLastModBy()));

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

    public boolean updateDepartment(DepartmentDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            //check duplicate name
            String check_query = "select record_id from setup_departments where description=? and record_id<>?";

            prep_stmt = db_conn.prepareStatement(check_query);
            prep_stmt.setString(1, dto.getDescription());
            prep_stmt.setString(2, dto.getRecordId());
            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE);
            }

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update setup_departments ")
                    .append(" set ")
                    .append("description=?")
                    .append(",record_status=?")
                    .append(",last_mod=?")
                    .append(",last_mod_by=?")
                    .append(" where record_id=?");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, serviceUtil.getValueOrEmpty(dto.getDescription()));
            prep_stmt.setString(2, serviceUtil.getValueOrEmpty(dto.getRecordStatus()));            
            prep_stmt.setString(3, serviceUtil.getValueOrEmpty(dto.getLastMod()));
            prep_stmt.setString(4, serviceUtil.getValueOrEmpty(dto.getLastModBy()));
            prep_stmt.setString(5, serviceUtil.getValueOrEmpty(dto.getRecordId()));

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
    
}
