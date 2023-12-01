/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.view.security;

import java.io.Serializable;
import app.dto.AppUserDTO;
import app.view.utils.AppConstants;
import app.view.utils.ViewHelper;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author GAINSolutions
 */
public class LoginMBean implements Serializable {

    String userName;
    String password;
    ViewHelper viewHelper;

    /**
     * Creates a new instance of LoginMBean
     */
    public LoginMBean() {
        viewHelper = new ViewHelper();
    }

    public String getUserName() {
        return userName;
    }

    public void loginAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        if (!StringUtils.isBlank(userName) && !StringUtils.isBlank(password)) {
            if (StringUtils.equalsIgnoreCase(userName, "sysadmin")
                    && StringUtils.equalsIgnoreCase(password, "sysadmin")) {
                AppUserDTO currentUser = new AppUserDTO();
                currentUser.setUsername("sysadmin");

                viewHelper.putSessionAttribute(AppConstants.SESSION_SCOPE_CURRENT_USER, currentUser);
                viewHelper.redirectTo("/common/dashboard.jsf?faces-redirect=true");

            }
        } else {
            ViewHelper.addErrorMessage(context, null, "Enter Your Username and Password", null);
        }

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
