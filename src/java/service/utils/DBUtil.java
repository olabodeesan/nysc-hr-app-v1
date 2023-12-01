/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.utils;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author IronHide
 */
public class DBUtil implements Serializable {
    
    private ServiceUtil serviceUtil;

    public DBUtil() {
        serviceUtil = new ServiceUtil();
    }   
    

    public DataSource getDatasource() throws NamingException {
        Context c = new InitialContext();
        DataSource dataSource = (DataSource) c.lookup("java:comp/env/nyschr_ds");
        return dataSource;
    }

    public Integer generateEmployeeId() {
        Integer retVal = null;
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = getDatasource().getConnection();
            cs = conn.prepareCall("{call gen_employee_id(?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.execute();
            retVal = cs.getInt(1);
        } catch (Exception exc) {

        } finally {
            serviceUtil.close(cs);
            serviceUtil.close(conn);
        }
        return retVal;
    }
    
    public Integer generateOtherId() {
        Integer retVal = null;
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = getDatasource().getConnection();
            cs = conn.prepareCall("{call gen_other_id(?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.execute();
            retVal = cs.getInt(1);
        } catch (Exception exc) {

        } finally {
            serviceUtil.close(cs);
            serviceUtil.close(conn);
        }
        return retVal;
    }

}
