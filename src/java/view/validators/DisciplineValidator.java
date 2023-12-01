/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.validators;

import dto.discipline.DisciplineDTO;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class DisciplineValidator implements Serializable {

    public DisciplineValidator() {
    }

    public boolean validateNewDiscipline(FacesContext context, DisciplineDTO dto) {

        boolean result = true;

        boolean check_empid = StringUtils.isBlank(dto.getEmployeeRecordId());
        if (check_empid) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Employee ID is required", null);
        }
        
        boolean check_case = StringUtils.isBlank(dto.getCaseLeveled());
        if (check_case) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Case Leveled is required", null);
        }
        
        boolean check_mgtdecision = StringUtils.isBlank(dto.getManagementDecision());
        if (check_mgtdecision) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Management decision is required", null);
        }

        boolean check_sanction_date = GenericValidator.isDate(dto.getDateOfSanction(), "dd/MM/yyyy", true);
        if (check_sanction_date == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid effective date. Use dd/mm/yyyy", null);
        }

        return result;

    }
    
    //this is not for closure
    public boolean validateUpdateDiscipline(FacesContext context, DisciplineDTO dto) {

        boolean result = true;

        boolean check_empid = StringUtils.isBlank(dto.getEmployeeRecordId());
        if (check_empid) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Employee ID is required", null);
        }
        
        boolean check_case = StringUtils.isBlank(dto.getCaseLeveled());
        if (check_case) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Case Leveled is required", null);
        }
        
        boolean check_mgtdecision = StringUtils.isBlank(dto.getManagementDecision());
        if (check_mgtdecision) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Management decision is required", null);
        }

        boolean check_sanction_date = GenericValidator.isDate(dto.getDateOfSanction(), "dd/MM/yyyy", true);
        if (check_sanction_date == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid effective date. Use dd/mm/yyyy", null);
        }

        return result;

    }
    
    public boolean validateCloseDiscipline(FacesContext context, DisciplineDTO dto) {

        boolean result = true;

        boolean check_empid = StringUtils.isBlank(dto.getEmployeeRecordId());
        if (check_empid) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Employee ID is required", null);
        }
        
        boolean check_case = StringUtils.isBlank(dto.getCaseLeveled());
        if (check_case) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Case Leveled is required", null);
        }
        
        boolean check_mgtdecision = StringUtils.isBlank(dto.getManagementDecision());
        if (check_mgtdecision) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Management decision is required", null);
        }

        boolean check_sanction_date = GenericValidator.isDate(dto.getDateOfSanction(), "dd/MM/yyyy", true);
        if (check_sanction_date == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid effective date. Use dd/mm/yyyy", null);
        }
        
        boolean check_close_date = GenericValidator.isDate(dto.getDateOfClosure(), "dd/MM/yyyy", true);
        if (check_close_date == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid date of closure. Use dd/mm/yyyy", null);
        }

        return result;

    }
}
