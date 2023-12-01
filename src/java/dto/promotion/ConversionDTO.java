/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.promotion;

import java.io.Serializable;

public class ConversionDTO implements Serializable {

    private String recordId;
    private String employeeRecordId, employeeFileNo;
    private String instType, instTypeDesc;
    private String instName;
    private String country, countryDesc;
    private String course, courseDesc;
    private String startDate, startDateDesc;
    private String endDate, endDateDesc;
    private String qualification, qualificationDesc;

    private String gradeLevel;
    private String gradeLevelStep;
    private String effectiveDate, effectiveDateDesc;

    private String conversionCadre, conversionCadreDesc;
    private String conversionRank, conversionRankDesc;

    private String previousCadre, previousCadreDesc;
    private String previousRank, previousRankDesc;

    private String created;
    private String createdBy;
    private String lastMod;
    private String lastModBy;

    public ConversionDTO() {
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getEmployeeRecordId() {
        return employeeRecordId;
    }

    public void setEmployeeRecordId(String employeeRecordId) {
        this.employeeRecordId = employeeRecordId;
    }

    public String getEmployeeFileNo() {
        return employeeFileNo;
    }

    public void setEmployeeFileNo(String employeeFileNo) {
        this.employeeFileNo = employeeFileNo;
    }

    public String getInstType() {
        return instType;
    }

    public void setInstType(String instType) {
        this.instType = instType;
    }

    public String getInstTypeDesc() {
        return instTypeDesc;
    }

    public void setInstTypeDesc(String instTypeDesc) {
        this.instTypeDesc = instTypeDesc;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryDesc() {
        return countryDesc;
    }

    public void setCountryDesc(String countryDesc) {
        this.countryDesc = countryDesc;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDateDesc() {
        return startDateDesc;
    }

    public void setStartDateDesc(String startDateDesc) {
        this.startDateDesc = startDateDesc;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDateDesc() {
        return endDateDesc;
    }

    public void setEndDateDesc(String endDateDesc) {
        this.endDateDesc = endDateDesc;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getQualificationDesc() {
        return qualificationDesc;
    }

    public void setQualificationDesc(String qualificationDesc) {
        this.qualificationDesc = qualificationDesc;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getGradeLevelStep() {
        return gradeLevelStep;
    }

    public void setGradeLevelStep(String gradeLevelStep) {
        this.gradeLevelStep = gradeLevelStep;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getEffectiveDateDesc() {
        return effectiveDateDesc;
    }

    public void setEffectiveDateDesc(String effectiveDateDesc) {
        this.effectiveDateDesc = effectiveDateDesc;
    }

    public String getConversionCadre() {
        return conversionCadre;
    }

    public void setConversionCadre(String conversionCadre) {
        this.conversionCadre = conversionCadre;
    }

    public String getConversionCadreDesc() {
        return conversionCadreDesc;
    }

    public void setConversionCadreDesc(String conversionCadreDesc) {
        this.conversionCadreDesc = conversionCadreDesc;
    }

    public String getConversionRank() {
        return conversionRank;
    }

    public void setConversionRank(String conversionRank) {
        this.conversionRank = conversionRank;
    }

    public String getConversionRankDesc() {
        return conversionRankDesc;
    }

    public void setConversionRankDesc(String conversionRankDesc) {
        this.conversionRankDesc = conversionRankDesc;
    }

    public String getPreviousCadre() {
        return previousCadre;
    }

    public void setPreviousCadre(String previousCadre) {
        this.previousCadre = previousCadre;
    }

    public String getPreviousCadreDesc() {
        return previousCadreDesc;
    }

    public void setPreviousCadreDesc(String previousCadreDesc) {
        this.previousCadreDesc = previousCadreDesc;
    }

    public String getPreviousRank() {
        return previousRank;
    }

    public void setPreviousRank(String previousRank) {
        this.previousRank = previousRank;
    }

    public String getPreviousRankDesc() {
        return previousRankDesc;
    }

    public void setPreviousRankDesc(String previousRankDesc) {
        this.previousRankDesc = previousRankDesc;
    }
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastMod() {
        return lastMod;
    }

    public void setLastMod(String lastMod) {
        this.lastMod = lastMod;
    }

    public String getLastModBy() {
        return lastModBy;
    }

    public void setLastModBy(String lastModBy) {
        this.lastModBy = lastModBy;
    }

}
