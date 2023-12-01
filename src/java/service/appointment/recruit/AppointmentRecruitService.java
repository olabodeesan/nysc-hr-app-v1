/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.appointment.recruit;

import app.exceptions.AppException;
import dto.appointment.recruit.EmployeeStatusDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;

/**
 *
 * @author IronHide
 */
public class AppointmentRecruitService implements Serializable{    
    
    private ServiceUtil serviceUtil;
    private DBUtil dBUtil;

    public AppointmentRecruitService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();        
    }
    
    public boolean updateEmployeePresentStatus(EmployeeStatusDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //update the employee table
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update employee ")
                    .append(" set ")
                    .append("present_status=?")
                    .append(",present_status_reason=?")
                    .append(",present_status_remarks=?")
                    .append(",date_of_present_status=?")
                    .append(",last_mod=?,last_mod_by=? ")
                    .append(" where employee_rec_id=?");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getStatusId()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getStatusReason()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getRemarks()));
            prep_stmt.setString(4, serviceUtil.resetToDbDateFormat(dto.getEffectiveDate()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));

            prep_stmt.executeUpdate();
            
            //then insert the history
            strBuilder = new StringBuilder();
            strBuilder.append("insert into employee_status ")
                    .append(" (employee_rec_id,status_id,status_reason,status_remarks,date_attained,record_status ")
                    .append(",created,created_by,last_mod,last_mod_by)")
                    .append(" values (?,?,?,?,?,?,?,?,?,?) ");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getStatusId()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getStatusReason()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getRemarks()));
            prep_stmt.setString(5, serviceUtil.resetToDbDateFormat(dto.getEffectiveDate()));
            prep_stmt.setString(6, AppConstants.ACTIVE);
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getCreated()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getCreatedBy()));
            prep_stmt.setString(9, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(10, serviceUtil.getValueOrNull(dto.getLastModBy()));

            prep_stmt.executeUpdate();

            db_conn.commit();
            result = true;

        } catch (Exception exc) {
            if(db_conn != null){
                try{
                    db_conn.rollback();
                }catch(Exception e){
                    
                }
            }
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }
    
}
