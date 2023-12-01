/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.common;

import app.exceptions.AppException;
import dto.common.DashboardDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import service.utils.DBUtil;
import service.utils.ServiceUtil;

/**
 *
 * @author IronHide
 */
public class DashBoardService implements Serializable {
    
    private final ServiceUtil serviceUtil;
    private final DBUtil dBUtil;

    public DashBoardService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
    }

    public DashboardDTO getDashBoard()
            throws AppException {

        DashboardDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("select 'abc'")
                    .append(",(")
                    .append("select count(employee_rec_id) from employee where present_status in (1,3)")
                    .append(") as active_personnel")
                    .append(",(")
                    .append("select count(employee_rec_id) from employee where present_status in (1,3)")
                    .append(" and date_due_for_retirement between curdate() and date_Add(curdate(), interval 2 month)")
                    .append(") as retiring_soon")
                    .append(",(")
                    .append("select count(employee_rec_id) from employee where present_status in (1,3) and date_due_for_retirement < curdate()")
                    .append(") as overdue_retire");
            
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            result_set = prep_stmt.executeQuery();
            
            result_set.next();
            
            result = new DashboardDTO();
            result.setActivePersonnel(result_set.getInt("active_personnel"));
            result.setRetiringSoon(result_set.getInt("retiring_soon"));
            result.setOverdueRetirement(result_set.getInt("overdue_retire"));            

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
