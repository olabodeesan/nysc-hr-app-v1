/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service.setup;

import dto.setup.CountryDTO;
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
public class CountryService implements Serializable{
    
    private ServiceUtil serviceUtil;
    private DBUtil dBUtil;

    public CountryService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
    }

    public Map<String, Object> searchRecords(String countQuery, String selectQuery, int startRow, int pagesize)
            throws AppException {

        Integer countInt = 0;
        Map<String, Object> result = new HashMap<>();
        List<CountryDTO> dto_list = new ArrayList<>();
        CountryDTO dto = null;
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

                dto = new CountryDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setDescription(result_set.getString("description"));
                dto.setContinentId(result_set.getString("continent"));
                dto.setContinentName(result_set.getString("continent_name"));
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

    public CountryDTO getCountry(String recordId)
            throws AppException {

        CountryDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("select d.record_id,d.description,d.continent,d.record_status,e.description as continent_name ")
                    .append("from setup_countries d ")
                    .append(" join setup_continents e on d.continent = e.record_id ")
                    .append(" where d.record_id=?");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, recordId);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = new CountryDTO();
                result.setRecordId(result_set.getString("record_id"));
                result.setDescription(result_set.getString("description"));
                result.setContinentId(result_set.getString("continent"));
                result.setContinentName(result_set.getString("continent_name"));
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

    public boolean createCountry(CountryDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            //check duplicate name
            String check_query = "select record_id from setup_countries where description=?";

            prep_stmt = db_conn.prepareStatement(check_query);
            prep_stmt.setString(1, dto.getDescription());
            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE);
            }

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("insert into setup_countries ")
                    .append("(description,continent,record_status,created,created_by,last_mod,last_mod_by)")
                    .append(" values (?,?,?,?,?,?,?)");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, serviceUtil.getValueOrEmpty(dto.getDescription()));
            prep_stmt.setString(2, serviceUtil.getValueOrEmpty(dto.getContinentId()));
            prep_stmt.setString(3, serviceUtil.getValueOrEmpty(dto.getRecordStatus()));
            prep_stmt.setString(4, serviceUtil.getValueOrEmpty(dto.getCreated()));
            prep_stmt.setString(5, serviceUtil.getValueOrEmpty(dto.getCreatedBy()));
            prep_stmt.setString(6, serviceUtil.getValueOrEmpty(dto.getLastMod()));
            prep_stmt.setString(7, serviceUtil.getValueOrEmpty(dto.getLastModBy()));

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

    public boolean updateCountry(CountryDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            //check duplicate name
            String check_query = "select record_id from setup_countries where description=? and record_id<>?";

            prep_stmt = db_conn.prepareStatement(check_query);
            prep_stmt.setString(1, dto.getDescription());
            prep_stmt.setString(2, dto.getRecordId());
            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE);
            }

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update setup_countries ")
                    .append(" set ")
                    .append("description=?")
                    .append(",continent=?")
                    .append(",record_status=?")
                    .append(",last_mod=?")
                    .append(",last_mod_by=?")
                    .append(" where record_id=?");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, serviceUtil.getValueOrEmpty(dto.getDescription()));
            prep_stmt.setString(2, serviceUtil.getValueOrEmpty(dto.getContinentId()));   
            prep_stmt.setString(3, serviceUtil.getValueOrEmpty(dto.getRecordStatus()));            
            prep_stmt.setString(4, serviceUtil.getValueOrEmpty(dto.getLastMod()));
            prep_stmt.setString(5, serviceUtil.getValueOrEmpty(dto.getLastModBy()));
            prep_stmt.setString(6, serviceUtil.getValueOrEmpty(dto.getRecordId()));

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
