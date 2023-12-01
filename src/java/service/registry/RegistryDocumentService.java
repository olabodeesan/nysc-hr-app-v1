/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.registry;

import app.exceptions.AppException;
import dto.setup.dto.registry.RegistryDocumentDTO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.UploadedFile;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class RegistryDocumentService implements Serializable {

    private final ServiceUtil serviceUtil;
    private final DBUtil dBUtil;

    public RegistryDocumentService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
    }
    
    public RegistryDocumentDTO getRegistryFile(String recordId)
            throws AppException {

        RegistryDocumentDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("SELECT d.record_id,d.employee_rec_id,d.file_name,d.remarks")
                    .append(",date_format(d.last_mod,'%d/%m/%Y') as _last_mod ")
                    .append("FROM registry_employee_doc_upload d ")
                    .append(" where d.record_id=? ");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, recordId);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = new RegistryDocumentDTO();
                result.setRecordId(result_set.getString("record_id"));
                result.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                result.setFileNameWithExt(result_set.getString("file_name"));
                
                result.setFileNameNoExt(StringUtils.substringBeforeLast(result.getFileNameWithExt(), "."));
                
                result.setRemarks(result_set.getString("remarks"));
                result.setLastMod(result_set.getString("_last_mod"));

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

    public boolean uploadRegistryDocFile(UploadedFile uploaded_file, RegistryDocumentDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        StringBuilder strBuilder = null;

        File content_file = null;
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        boolean _file_uploaded = false;
        boolean _db_updated = false;

        try {

            //UPDATE DB
            //insert into database table first
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //System.out.println(dto.getEmployeeRecordId() + " , " + dto.getFileNameWithExt());
            
            String query = "select * from registry_employee_doc_upload where employee_rec_id=? and file_name=?";
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getEmployeeRecordId());
            prep_stmt.setString(2, dto.getFileNameWithExt());
            result_set = prep_stmt.executeQuery();

            boolean exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE);
            }

            strBuilder = new StringBuilder();
            strBuilder.append("insert into registry_employee_doc_upload ")
                    .append(" (employee_rec_id,file_name,record_status,remarks,created,created_by,last_mod,last_mod_by)")
                    .append(" values (?,?,?,?,?,?,?,?)");
            query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));
            prep_stmt.setString(2, dto.getFileNameWithExt());
            prep_stmt.setString(3, dto.getRecordStatus());
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getRemarks()));
            prep_stmt.setString(5, dto.getCreated());
            prep_stmt.setString(6, dto.getCreatedBy());
            prep_stmt.setString(7, dto.getLastMod());
            prep_stmt.setString(8, dto.getLastModBy());

            prep_stmt.executeUpdate();

            _db_updated = true;

            //UPLOAD FILE
            //check if staff folder exists else create it
            ViewHelper viewHelper = new ViewHelper();
            strBuilder = new StringBuilder();

            strBuilder.append(viewHelper.getPersonnelDocsDir())
                    .append("/")
                    .append(dto.getEmployeeRecordId())
                    .append("/DOCS");
            String employeeDocDirName = strBuilder.toString();

            File employeeDocDir = new File(employeeDocDirName);
            boolean dir_exists = employeeDocDir.exists();

            //System.out.println("got here: " + employeeDocDirName);
            
            if (!dir_exists) {
                //System.out.println("got here 2 ");
                employeeDocDir.mkdirs();
            }

            //test again if it exists
            dir_exists = employeeDocDir.exists();

            if (dir_exists) {
                //get full file name
                strBuilder = new StringBuilder();
                strBuilder.append(employeeDocDirName).append("/").append(dto.getFileNameWithExt());
                String filePath = strBuilder.toString();

                //upload the file to disk
                content_file = new File(filePath);
                fileOutputStream = new FileOutputStream(content_file);
                byte[] buffer = new byte[6124];
                int bulk;
                inputStream = uploaded_file.getInputstream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }

                _file_uploaded = true;

            }

            if (_file_uploaded && _db_updated) {
                db_conn.commit();
                result = true;
            }

        } catch (Exception exc) {
            //rollback any db changes
            if (db_conn != null) {
                try {
                    db_conn.rollback();
                } catch (Exception e) {

                }
            }
            //delete file if already uploaded
            try {
                if (_file_uploaded) {
                    FileUtils.forceDelete(content_file);
                }
            } catch (Exception e2) {

            }
            System.out.println(exc.getMessage());
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
            
            serviceUtil.close(fileOutputStream);
            serviceUtil.close(inputStream);
        }

        return result;
    }

    public boolean renameRegistryDocFile(RegistryDocumentDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        File oldFile = null;
        File newFile = null;

        boolean _file_renamed = false;
        boolean _db_updated = false;

        try {

            //UPDATE DB
            //update the into database table first
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            String query = "select * from registry_employee_doc_upload where record_id<>? and employee_rec_id=? and file_name=?";
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getRecordId());
            prep_stmt.setString(2, dto.getEmployeeRecordId());
            prep_stmt.setString(3, dto.getFileNameWithExt());
            result_set = prep_stmt.executeQuery();

            boolean exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE);
            }

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update registry_employee_doc_upload ")
                    .append(" set file_name=?,remarks=?,last_mod=?,last_mod_by=? ")
                    .append(" where record_id=? ");
            query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getFileNameWithExt());
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getRemarks()));
            prep_stmt.setString(3, dto.getLastMod());
            prep_stmt.setString(4, dto.getLastModBy());
            prep_stmt.setString(5, dto.getRecordId());

            prep_stmt.executeUpdate();

            _db_updated = true;

            //RENAME THE FILE
            //check if staff folder exists else create it
            ViewHelper viewHelper = new ViewHelper();
            strBuilder = new StringBuilder();

            strBuilder.append(viewHelper.getPersonnelDocsDir())
                    .append("/")
                    .append(dto.getEmployeeRecordId())
                    .append("/DOCS");
            String employeeDocDirName = strBuilder.toString();

            File employeeDocDir = new File(employeeDocDirName);
            boolean dir_exists = employeeDocDir.exists();

            if (dir_exists) {
                strBuilder = new StringBuilder();
                strBuilder.append(employeeDocDirName).append("/").append(dto.getOldFileNameWithExt());
                String oldFilePath = strBuilder.toString();

                strBuilder = new StringBuilder();
                strBuilder.append(employeeDocDirName).append("/").append(dto.getFileNameWithExt());
                String newFilePath = strBuilder.toString();

                oldFile = new File(oldFilePath);
                newFile = new File(newFilePath);

                if (oldFile.exists()) {
                    FileUtils.moveFile(oldFile, newFile);
                    _file_renamed = true;
                }
            }

            if (_file_renamed && _db_updated) {
                db_conn.commit();
                result = true;
            }

        } catch (Exception exc) {
            //rollback any db changes
            if (db_conn != null) {
                try {
                    db_conn.rollback();
                } catch (Exception e) {

                }
            }
            //delete file if already uploaded
            try {
                if (_file_renamed) {
                    FileUtils.moveFile(newFile, oldFile);
                }
            } catch (Exception e2) {

            }
            System.out.println(exc.getMessage());
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);            
        }

        return result;
    }

}
