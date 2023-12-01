/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.view.validation;

import app.dto.GenderDTO;
import app.view.utils.ViewHelper;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author GAINSolutions
 */
public class GenderValidator implements Serializable {

    public GenderValidator() {
    }
    
     public boolean validateNew(FacesContext context, GenderDTO dto) {
        boolean result = true;

        boolean check_Description = StringUtils.isBlank(dto.getDescription());

        boolean check_status = StringUtils.isBlank(dto.getRecordStatus());

        if (check_Description) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Description is Required", null);
        }

        if (check_status) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Record Status is Required", null);
        }
        return result;

    }
}
