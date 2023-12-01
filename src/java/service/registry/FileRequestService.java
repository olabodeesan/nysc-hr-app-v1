/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.registry;

import app.exceptions.AppException;
import dto.setup.dto.registry.FileRequestDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;

/**
 *
 * @author IronHide
 */
public class FileRequestService implements Serializable {

    private final ServiceUtil serviceUtil;
    private final DBUtil dBUtil;

    public FileRequestService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
    }

    public Map<String, Object> searchRecords(String countQuery, String selectQuery, int startRow, int pagesize)
            throws AppException {

        Integer countInt = 0;
        Map<String, Object> result = new LinkedHashMap<>();
        List<FileRequestDTO> dto_list = new ArrayList<>();
        FileRequestDTO dto = null;
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

                dto = new FileRequestDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setRegistryFileNo(result_set.getString("registry_file_no"));
                dto.setRegistryFileName(result_set.getString("registry_file_name"));
                dto.setRequestingOfficerInfo(result_set.getString("requesting_officer_info"));
                dto.setPurpose(result_set.getString("purpose"));
                dto.setRequestDate(result_set.getString("_request_date"));
                dto.setRequestTime(result_set.getString("request_time"));
                dto.setReturnDate(result_set.getString("_return_date"));
                dto.setReturnTime(result_set.getString("return_time"));
                dto.setInOutStatus(result_set.getString("in_out_status"));
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

    public FileRequestDTO getFileRequest(String recordId)
            throws AppException {

        FileRequestDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("SELECT d.record_id,d.registry_file_no,d.registry_file_name,d.requesting_officer_info,d.purpose ")
                    .append(",date_format(d.request_date,'%d/%m/%Y') as _request_date,d.request_time ")
                    .append(",date_format(d.return_date,'%d/%m/%Y') as _return_date,d.return_time,d.in_out_status,d.remarks,d.record_status")
                    .append(",d.last_mod,d.last_mod_by ")
                    .append("FROM registry_file_requests d ")
                    .append(" where d.record_id=? ");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, recordId);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = new FileRequestDTO();
                result.setRecordId(result_set.getString("record_id"));
                result.setRegistryFileNo(result_set.getString("registry_file_no"));
                result.setRegistryFileName(result_set.getString("registry_file_name"));
                result.setRequestingOfficerInfo(result_set.getString("requesting_officer_info"));
                result.setPurpose(result_set.getString("purpose"));
                result.setRequestDate(result_set.getString("_request_date"));
                result.setRequestTime(result_set.getString("request_time"));
                result.setReturnDate(result_set.getString("_return_date"));
                result.setReturnTime(result_set.getString("return_time"));
                result.setInOutStatus(result_set.getString("in_out_status"));
                result.setRemarks(result_set.getString("remarks"));
                result.setRecordStatus(result_set.getString("record_status"));
                result.setLastMod(result_set.getString("last_mod"));
                result.setLastModBy(result_set.getString("last_mod_by"));

            } else {
                throw new AppException(AppConstants.NOT_FOUND);
            }

        } catch (Exception exc) {
            System.out.print(exc.getMessage());
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }
    
    public boolean checkIfFileIsOut(String registry_file_no)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("SELECT d.record_id ")
                    .append("FROM registry_file_requests d ")
                    .append(" where d.registry_file_no=? and in_out_status=? ");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, registry_file_no);
            prep_stmt.setString(2, AppConstants.REGISTRY_FILE_OUT);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = true; 
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

    public boolean createFileRequest(FileRequestDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            
            //check that the file is not out first
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("SELECT d.record_id ")
                    .append("FROM registry_file_requests d ")
                    .append(" where d.registry_file_no=? and in_out_status=? ");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getRegistryFileNo());
            prep_stmt.setString(2, AppConstants.REGISTRY_FILE_OUT);

            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            
            if(exists){
                throw new AppException(AppConstants.REGISTRY_FILE_OUT);
            }

            strBuilder = new StringBuilder();
            strBuilder.append("insert into registry_file_requests ")
                    .append("(registry_file_no,registry_file_name,requesting_officer_info,purpose,request_date,request_time")
                    .append(",in_out_status")
                    .append(",remarks,record_status,created,created_by,last_mod,last_mod_by )")
                    .append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?)");

            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getRegistryFileNo());
            prep_stmt.setString(2, dto.getRegistryFileName());
            prep_stmt.setString(3, dto.getRequestingOfficerInfo());
            prep_stmt.setString(4, dto.getPurpose());
            prep_stmt.setString(5, serviceUtil.resetToDbDateFormat(dto.getRequestDate()));
            prep_stmt.setString(6, dto.getRequestTime());
            prep_stmt.setString(7, dto.getInOutStatus());
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getRemarks()));
            prep_stmt.setString(9, dto.getRecordStatus());
            prep_stmt.setString(10, dto.getCreated());
            prep_stmt.setString(11, dto.getCreatedBy());
            prep_stmt.setString(12, dto.getLastMod());
            prep_stmt.setString(13, dto.getLastModBy());

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

    public boolean updateFileRequest(FileRequestDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //log old record
            logFileRequest(db_conn, dto);

            //continue update
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update registry_file_requests ")
                    .append("set ")
                    .append("registry_file_no=? ")
                    .append(",registry_file_name=? ")
                    .append(",requesting_officer_info=? ")
                    .append(",purpose=? ")
                    .append(",request_date=? ")
                    .append(",request_time=? ")
                    .append(",remarks=? ")
                    .append(",last_mod=? ")
                    .append(",last_mod_by=? ")
                    .append(" where record_id=?");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getRegistryFileNo());
            prep_stmt.setString(2, dto.getRegistryFileName());
            prep_stmt.setString(3, dto.getRequestingOfficerInfo());
            prep_stmt.setString(4, dto.getPurpose());
            prep_stmt.setString(5, serviceUtil.resetToDbDateFormat(dto.getRequestDate()));
            prep_stmt.setString(6, dto.getRequestTime());
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
    
    public boolean returnFileRequest(FileRequestDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //log old record
            logFileRequest(db_conn, dto);

            //continue
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update registry_file_requests ")
                    .append("set ")
                    .append("return_date=?")
                    .append(",return_time=?")
                    .append(",in_out_status=?")
                    .append(",remarks=? ")
                    .append(",last_mod=? ")
                    .append(",last_mod_by=? ")
                    .append(" where record_id=?");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, serviceUtil.resetToDbDateFormat(dto.getReturnDate()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getReturnTime()));
            prep_stmt.setString(3, dto.getInOutStatus());
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getRemarks()));
            prep_stmt.setString(5, dto.getLastMod());
            prep_stmt.setString(6, dto.getLastModBy());
            prep_stmt.setString(7, dto.getRecordId());

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

    public boolean deleteFileRequest(FileRequestDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //log old record
            logFileRequest(db_conn, dto);

            //continue de-activate
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update registry_file_requests ")
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
    public void logFileRequest(Connection log_db_conn, FileRequestDTO dto)
            throws AppException {

        //NO NEED TO RE-INIT CONNECTION
        //ITS PASSED
        PreparedStatement prep_stmt = null;

        try {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("insert into registry_file_requests_history ")
                    .append("(file_request_id,registry_file_no,registry_file_name,requesting_officer_info,purpose,request_date,request_time")
                    .append(",return_date,return_time,in_out_status")
                    .append(",remarks,record_status,last_mod,last_mod_by )")
                    .append(" (")
                    .append("select record_id,registry_file_no,registry_file_name,requesting_officer_info,purpose,request_date,request_time")
                    .append(",return_date,return_time,in_out_status")
                    .append(",remarks,record_status,last_mod,last_mod_by ")
                    .append(" from registry_file_requests ")
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
