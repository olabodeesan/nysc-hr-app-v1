/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.validators;

import dto.promotion.ConversionDTO;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import view.utils.ViewHelper;

/**
 *
 * @author Gainsolutions
 */
public class ConversionValidator implements Serializable {

    public ConversionValidator() {
    }

    public boolean validateNewConversion(FacesContext context, ConversionDTO dto) {
        boolean result = true;
        //inst name
        boolean check_instname = StringUtils.isBlank(dto.getInstName());
        if (check_instname) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Institution name is required", null);
        }

        //course
        boolean check_course = StringUtils.isBlank(dto.getCourse());
        if (check_course) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Course is required", null);
        }

        //country
        boolean check_country = StringUtils.isBlank(dto.getCountry());
        if (check_country) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Country of study is required", null);
        }

        //start date
        boolean check_startdate = GenericValidator.isDate(dto.getStartDate(), "dd/MM/yyyy", true);
        if (check_startdate == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid start date. Use dd/mm/yyyy", null);
        }

        //end date
        boolean check_enddate = GenericValidator.isDate(dto.getEndDate(), "dd/MM/yyyy", true);
        if (check_enddate == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid end date. Use dd/mm/yyyy", null);
        }

        //qualification
        boolean check_qualifications = StringUtils.isBlank(dto.getQualification());
        if (check_qualifications) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Qualification is required", null);
        }

        //gradeLevel
        boolean check_gradeLevel = StringUtils.isBlank(dto.getGradeLevel());
        if (check_gradeLevel) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Grade Level is required", null);
        }

        //gradeLevelStep
        boolean check_gradeLevelStep = StringUtils.isBlank(dto.getGradeLevelStep());
        if (check_gradeLevelStep) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Grade Level Step is required", null);
        }

        //effectiveDate
        boolean check_effectiveDate = GenericValidator.isDate(dto.getEffectiveDate(), "dd/MM/yyyy", true);
        if (check_effectiveDate == false) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Invalid Effective Date. Use dd/mm/yyyy", null);
        }

        //conversionCadre
        boolean check_conversionCadre = StringUtils.isBlank(dto.getConversionCadre());
        if (check_conversionCadre) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Conversion Cadre is required", null);
        }

        //conversionRankDesc
        boolean check_conversionRankDesc = StringUtils.isBlank(dto.getConversionRankDesc());
        if (check_conversionRankDesc) {
            result = false;
            ViewHelper.addErrorMessage(context, null, "Conversion Rank is required", null);
        }

        return result;
    }
}
