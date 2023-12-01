/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.view.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import app.service.utils.ApplicationCodesService;

/**
 *
 * @author GAINSolutions
 */
public class ApplicationCodesLoader {

    private ApplicationCodesService service;

    public ApplicationCodesLoader() {
        service = new ApplicationCodesService();
    }

    public Map<String, String> getGender() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_GENDER) == null) {

            try {
                codes = new LinkedHashMap<String, String>();
                codes.put("Male", "M");
                codes.put("Female", "F");
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_GENDER, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_GENDER);
        }
        return codes;
    }

    public Map<String, String> getRecordStatusCodes() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_STATUS) == null) {
            try {
                codes = new LinkedHashMap<String, String>();
                codes.put("ACTIVE", "ACTIVE");
                codes.put("INACTIVE", "INACTIVE");
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_STATUS, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_STATUS);
        }
        return codes;
    }

    public Map<String, String> getDepartments() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_DEPT) == null) {
            try {
                codes = service.getDepartments();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_DEPT, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_DEPT);
        }
        return codes;
    }

    public Map<String, String> getStates() {
        Map<String, String> codes = null;
        if (getAppScopeObject(AppConstants.APP_SCOPE_STATE) == null) {
            try {
                codes = service.getStates();
            } catch (Exception exc) {
            }
            putAppScopeObject(AppConstants.APP_SCOPE_STATE, codes);
        } else {
            codes = (LinkedHashMap<String, String>) getAppScopeObject(AppConstants.APP_SCOPE_STATE);
        }
        return codes;
    }

    public void putAppScopeObject(String key, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getApplicationMap().put(key, value);
    }

    public Object getAppScopeObject(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getApplicationMap().get(key);
    }
}
