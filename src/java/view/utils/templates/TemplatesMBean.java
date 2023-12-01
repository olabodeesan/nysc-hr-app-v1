/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view.utils.templates;

import view.utils.AppConstants;
import view.utils.ViewHelper;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author IronHide
 */
public class TemplatesMBean implements Serializable{
    
    public TemplatesMBean() {
    }
    
    public void preRenderView(ComponentSystemEvent cse) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        if (session.getAttribute(AppConstants.SESSION_SCOPE_CURRENT_USER) == null) {
            ViewHelper viewHelper = new ViewHelper();
            viewHelper.logoffUser();
        }
    }

    public void logoutAction(ActionEvent event) {
        ViewHelper viewHelper = new ViewHelper();
        viewHelper.logoffUser();
    }
    
}
