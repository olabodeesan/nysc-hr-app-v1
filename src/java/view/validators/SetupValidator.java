/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.validators;


import dto.setup.CadreDTO;
import dto.setup.CountryDTO;
import dto.setup.DepartmentDTO;
import dto.setup.PositionAppointmentDTO;
import dto.setup.QualificationDTO;
import view.utils.ViewHelper;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author IronHide
 */
public class SetupValidator implements Serializable {

    public SetupValidator() {
    }

    //cadre
    public boolean validateCadre(FacesContext context, CadreDTO dto) {
        boolean result = true;

        boolean check_description = StringUtils.isBlank(dto.getDescription());
        boolean check_status = StringUtils.isBlank(dto.getRecordStatus());

        if (check_description) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Description is required", null);
        }

        if (check_status) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Status is required", null);
        }

        return result;
    }
    
    //country
    public boolean validateCountry(FacesContext context, CountryDTO dto) {
        boolean result = true;

        boolean check_description = StringUtils.isBlank(dto.getDescription());
        boolean check_continent = StringUtils.isBlank(dto.getContinentId());
        boolean check_status = StringUtils.isBlank(dto.getRecordStatus());

        if (check_description) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Description is required", null);
        }
        
        if (check_continent) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Continent is required", null);
        }

        if (check_status) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Status is required", null);
        }

        return result;
    }
    
    //department
    public boolean validateDepartment(FacesContext context, DepartmentDTO dto) {
        boolean result = true;

        boolean check_description = StringUtils.isBlank(dto.getDescription());
        boolean check_status = StringUtils.isBlank(dto.getRecordStatus());

        if (check_description) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Description is required", null);
        }

        if (check_status) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Status is required", null);
        }

        return result;
    }
    
    //position apt
    public boolean validatePositionAppointment(FacesContext context, PositionAppointmentDTO dto) {
        boolean result = true;

        boolean check_description = StringUtils.isBlank(dto.getDescription());
        boolean check_status = StringUtils.isBlank(dto.getRecordStatus());

        if (check_description) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Description is required", null);
        }

        if (check_status) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Status is required", null);
        }

        return result;
    }
    
    //qualification
    public boolean validateQualification(FacesContext context, QualificationDTO dto) {
        boolean result = true;

        boolean check_insttype = StringUtils.isBlank(dto.getInstitutionType());
        String abbrev = dto.getAbbreviation();
        boolean check_abbrev = StringUtils.isBlank(abbrev) || StringUtils.length(abbrev) > 10;
        boolean check_description = StringUtils.isBlank(dto.getDescription());
        boolean check_status = StringUtils.isBlank(dto.getRecordStatus());

        if (check_insttype) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Issuing Institution Category is required", null);
        }
        
        if (check_abbrev) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Abbreviation is required. Must be less than 10 characters", null);
        }
        
        if (check_description) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Description is required", null);
        }

        if (check_status) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Status is required", null);
        }

        return result;
    }

}
