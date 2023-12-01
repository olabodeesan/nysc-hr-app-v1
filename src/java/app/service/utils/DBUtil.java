/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.service.utils;

import java.io.Serializable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author GAINSolutions
 */
public class DBUtil implements Serializable {

    public DataSource getDatasource() throws NamingException {
        Context c = new InitialContext();
        return (DataSource) c.lookup("java:comp/env/nyscHr_ds");
    }

}
