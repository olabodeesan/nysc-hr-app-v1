/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.template.views;

import app.view.utils.AppConstants;
import app.view.utils.ViewHelper;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author GAINSolutions
 */
public class TemplateMBean implements Serializable {

    /**
     * Creates a new instance of TemplateMBean
     */
    public TemplateMBean() {
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
