/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.view.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import app.dto.AppUserDTO;
import org.apache.commons.lang3.SystemUtils;

/**
 *
 * @author GAINSolutions
 */
public class ViewHelper implements Serializable {

    public static void addMessage(FacesContext context, String clientId, String summary, String detail, FacesMessage.Severity severity) {
        context.addMessage(clientId, new FacesMessage(severity, summary, detail));
    }

    public static void addErrorMessage(FacesContext context, String clientId, String summary, String detail) {
        addMessage(context, clientId, summary, detail, FacesMessage.SEVERITY_ERROR);
    }

    public static void addWarningMessage(FacesContext context, String clientId, String summary, String detail) {
        addMessage(context, clientId, summary, detail, FacesMessage.SEVERITY_WARN);
    }

    public static void addInfoMessage(FacesContext context, String clientId, String summary, String detail) {
        addMessage(context, clientId, summary, detail, FacesMessage.SEVERITY_INFO);
    }

    public static void clearMessages(FacesContext context) {
        Iterator<FacesMessage> msgIterator = context.getMessages();
        while (msgIterator.hasNext()) {
            msgIterator.next();
            msgIterator.remove();
        }
    }

    public AppUserDTO getAppUser() {
        return (AppUserDTO) getSessionAttribute(AppConstants.SESSION_SCOPE_CURRENT_USER);
    }
    
    public String getStaffEmail(){
        AppUserDTO staff = getAppUser();
        String email = staff.getEmail();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<").append(email).append(">");
        
        String result = strBuilder.toString();
        return result;
    }    

    public void redirectTo(String url) {
        FacesContext context = FacesContext.getCurrentInstance();
        NavigationHandler nav = context.getApplication().getNavigationHandler();
        nav.handleNavigation(context, null, url);
        context.renderResponse();
    }
    
    public String getDateString(Date dateObj) {
        String result = "1920-04-04";
        if (dateObj != null) {
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                result = formatter.format(dateObj);
            } catch (Exception exc) {
            }
        }
        return result;
    }

    public static String fixSqlFieldValue(String value) {
        if (value == null) {
            return null;
        }
        int length = value.length();
        StringBuilder fixedValue = new StringBuilder((int) (length * 1.1));
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c == '\'') {
                fixedValue.append("''");
            } else {
                fixedValue.append(c);
            }
        }
        return fixedValue.toString();
    }
    
    public String getContentDir() {
        String content_dir = "\blah";
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (SystemUtils.IS_OS_UNIX) {
                content_dir = ctx.getExternalContext().getInitParameter("UNIX_CONTENT_DIR");
            }else if(SystemUtils.IS_OS_MAC_OSX){
                 content_dir = ctx.getExternalContext().getInitParameter("MAC_CONTENT_DIR");
            }else if(SystemUtils.IS_OS_MAC){
                 content_dir = ctx.getExternalContext().getInitParameter("MAC_CONTENT_DIR");
            }else if (SystemUtils.IS_OS_WINDOWS) {
                content_dir = ctx.getExternalContext().getInitParameter("WIN_CONTENT_DIR");
            }
        } catch (Exception exc) {
        }
        return content_dir;
    }

    public String getRequestParamValue(String param_name) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getRequestParameterMap().get(param_name);
    }

    public void putSessionAttribute(String attribute, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put(attribute, value);
    }

    public Object getSessionAttribute(String attribute) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get(attribute);
    }

    public void removeSessionAttribute(String attribute) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove(attribute);
    }

    public void putApplicationAttribute(String attribute, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getApplicationMap().put(attribute, value);
    }

    public Object getApplicationAttribute(String attribute) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getApplicationMap().get(attribute);
    }

    public void removeApplicationAttribute(String attribute) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getApplicationMap().remove(attribute);
    }

    public void logoffUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove(AppConstants.SESSION_SCOPE_CURRENT_USER);
        redirectTo("/login.jsf?faces-redirect=true");
    }
}
