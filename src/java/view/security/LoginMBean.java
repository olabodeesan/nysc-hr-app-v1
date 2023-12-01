/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.security;

import app.dto.AppUserDTO;
import view.utils.AppConstants;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Messages;
import service.security.SecurityService;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class LoginMBean implements Serializable {

    private String username;
    private String password;
    private ViewHelper viewHelper;
    private SecurityService service;

    public LoginMBean() {
        service = new SecurityService();
        viewHelper = new ViewHelper();
    }

    public void loginAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        if (!StringUtils.isBlank(username) && !StringUtils.isBlank(password)) {

            AppUserDTO currentUser = null;

            try {
                currentUser = service.authenticateDBUser(username, password);

                if (currentUser != null) {
                    viewHelper.putSessionAttribute(AppConstants.SESSION_SCOPE_CURRENT_USER, currentUser);

                    if (StringUtils.equalsIgnoreCase(currentUser.getProfileType(), AppConstants.SYS_ADMIN) || StringUtils.equalsIgnoreCase(currentUser.getProfileType(), AppConstants.DIRECTOR)) {
                        viewHelper.redirectTo("/common/dashboard.xhtml?faces-redirect=true&nav=dash");
                    }else if (StringUtils.equalsIgnoreCase(currentUser.getProfileType(), AppConstants.STATE_STAFF)) {
                        viewHelper.redirectTo("/common/staff_dashboard.xhtml?faces-redirect=true&nav=dash");
                    }

                } else {
                    ViewHelper.addErrorMessage(context, null, "Invalid account information", null);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                String msg = e.getMessage();
                if (StringUtils.equals(msg, AppConstants.NOT_FOUND)) {
                    ViewHelper.addErrorMessage(context, null, "Invalid account information", null);
                } else if (StringUtils.equals(msg, AppConstants.INACTIVE)) {
                    ViewHelper.addErrorMessage(context, null, "Inactive account. Contact system admin.", null);
                } else {
                    ViewHelper.addErrorMessage(context, null, "Authentication error. Try again.", null);
                }
            }
        } else {
            Messages.addError(null, "All fields are required");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
