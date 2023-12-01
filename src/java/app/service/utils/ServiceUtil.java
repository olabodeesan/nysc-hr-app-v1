/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.service.utils;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author GAINSolutions
 */
public class ServiceUtil implements Serializable {   

    public ServiceUtil() {
    }
    
    public String getValueOrEmpty(String value) {
        return (StringUtils.isBlank(StringUtils.trim(value))) ? "" : value.trim();
    }  
    
    public String getValueOrZero(String value) {
        return (StringUtils.isBlank(StringUtils.trim(value))) ? "0" : value.trim();
    }
    
    public String getValueOrNull(String value) {
        return (StringUtils.isBlank(StringUtils.trim(value))) ? null : value.trim();
    }
    
    public String resetDateYMD(String dateDMY) {
        String result = "";
        if (!StringUtils.isBlank(StringUtils.trim(dateDMY))) {
            String[] strArray = dateDMY.split("-");
            if (strArray != null && strArray.length == 3) {
                result = strArray[2] + "-" + strArray[1] + "-" + strArray[0];
            }
        }
        if (StringUtils.isBlank(result)) {
            result = dateDMY;
        }
        return result;
    }
    
    public void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            //ignore
        }
    }

    public void close(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception e) {
            //ignore
        }
    }

    public void close(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            //ignore
        }
    }
    
    public void close(CallableStatement cs) {
        try {
            if (cs != null) {
                cs.close();
            }
        } catch (Exception e) {
            //ignore
        }
    }

    public void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            //ignore
        }
    }
    
}
