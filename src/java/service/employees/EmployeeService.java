/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.employees;

import app.exceptions.AppException;
import dto.employees.AcademicDataDTO;
import dto.employees.EmployeeAppointmentsDTO;
import dto.employees.EmployeeCadreDTO;
import dto.employees.EmployeeDTO;
import dto.employees.EmployeeLocationDTO;
import dto.appointment.recruit.EmployeeStatusDTO;
import dto.discipline.DisciplineDTO;
import dto.employees.FetchOptionsDTO;
import dto.employees.NextOfKinDTO;
import dto.setup.dto.registry.AnnualLeaveRosterDTO;
import dto.setup.dto.registry.RegistryDocumentDTO;
import dto.setup.dto.registry.StaffLeaveApplicationDTO;
import dto.setup.dto.training.StaffFurtherStudyApplicationDTO;
import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;

/**
 *
 * @author IronHide
 */
public class EmployeeService implements Serializable {

    private String employeeSelectQuery;
    private String suspendedListClause, disengagedListClause, disciplinedClause, orderClause;
    private ServiceUtil serviceUtil;
    private DBUtil dBUtil;

    public EmployeeService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();

        initSelectQuery();
        initWhereClauses();
    }

    private void initWhereClauses() {
        //build discipline clause
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(" e.presently_disciplined='Y'");
        disciplinedClause = strBuilder.toString();

        //build suspended clause
        strBuilder = new StringBuilder();
        strBuilder.append(" e.present_status= ").append(AppConstants.PERSONNEL_SUSPENDED).append(" ");
        suspendedListClause = strBuilder.toString();

        //build disengaged clause
        strBuilder = new StringBuilder();
        strBuilder.append(" e.present_status IN ")
                .append("(")
                .append(AppConstants.PERSONNEL_RETIRED).append(",")
                .append(AppConstants.PERSONNEL_DECEASED).append(",")
                .append(AppConstants.PERSONNEL_DISMISSED).append(",")
                .append(AppConstants.PERSONNEL_TERMINATED)
                .append(") ");
        disengagedListClause = strBuilder.toString();

        //order clause
        strBuilder = new StringBuilder();
        strBuilder.append(" order by e._temp_sort asc ");
        orderClause = strBuilder.toString();
    }

    public String getSuspendedListClause() {
        return suspendedListClause;
    }

    public String getDisengagedListClause() {
        return disengagedListClause;
    }

    public String getDisciplinedClause() {
        return disciplinedClause;
    }

    public String getOrderClause() {
        return orderClause;
    }

    private void initSelectQuery() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select e.employee_rec_id,e.file_no, e.surname, e.other_names, e.gender, e.marital_status,sms.description as _marital_status")
                .append(", e.state_of_origin,ss.description as _state_of_origin, e.lga, slga.description as _lga")
                .append(",e.date_of_birth,date_format(date_of_birth,'%d/%m/%Y') as _date_of_birth")
                .append(", e.date_of_1st_appt,date_format(date_of_1st_appt,'%d/%m/%Y') as _date_of_1st_appt")
                .append(",e.current_grade_level, e.current_grade_step")
                .append(", e.current_date_of_present_appt,date_format(current_date_of_present_appt,'%d/%m/%Y') as _current_date_of_present_appt")
                .append(",e.confirmation_date, date_format(confirmation_date,'%d/%m/%Y') as _confirmation_date")
                .append(",e.current_cadre,scadre.description as _current_cadre")
                .append(",e.current_rank,srank.description as _current_rank")
                .append(", e.current_location,sloc.description as _current_location")
                .append(",e.current_date_of_posting_to_location,date_format(current_date_of_posting_to_location,'%d/%m/%Y') as _current_date_of_posting_to_location")
                .append(", e.current_dept, sdept.description as _current_dept, e.is_state_coord, e.is_state_acct, e.is_secondment")
                .append(",e.date_retire_based_on_length_of_stay,date_format(date_retire_based_on_length_of_stay,'%d/%m/%Y') as _date_retire_based_on_length_of_stay")
                .append(",e.date_retire_based_on_age,date_format(date_retire_based_on_age,'%d/%m/%Y') as _date_retire_based_on_age")
                .append(",e.date_due_for_retirement,date_format(date_due_for_retirement,'%d/%m/%Y') as _date_due_for_retirement")
                .append(",date_format(date_due_for_retirement,'%Y') as _year_due_for_retirement ")
                .append(",e.primary_phone,e.secondary_phone,e.primary_email,e.secondary_email,e.present_address,e.permanent_home_address ")
                .append(",e.present_status,sps.description as _present_status")
                .append(",e.present_status_reason,spsr.description as _present_status_reason")
                .append(",e.date_of_present_status,date_format(date_of_present_status,'%d/%m/%Y') as _date_of_present_status")
                .append(",e.present_status_remarks,e.mode_of_entry,smoe.description as _mode_of_entry,e.transfered_entry_organization ")
                .append(",e.presently_disciplined ")
                .append(" ,e.pfa_id, pfas.description as _pfa_description ,e.pfa_number  ")
                .append(" ,e.ippis_number ,e.nhf_number,e.nhis_number,e.tin_number   ")
                .append(" ,e.salary_bank_id, salaryBank.description as _salary_bank_name ,e.salary_bank_account_no ")
                .append(" ,e.operation_bank_id, operationBank.description as _operation_bank_name ,e.operation_bank_account_no ")
                .append("from employee e ")
                .append("join setup_marital_status sms on e.marital_status = sms.record_id ")
                .append("join setup_states ss on e.state_of_origin=ss.record_id ")
                .append("join setup_lgas slga on e.lga = slga.record_id ")
                .append("join setup_cadre scadre on e.current_cadre = scadre.record_id ")
                .append("join setup_rank srank on e.current_rank = srank.record_id ")
                .append("join setup_states sloc on e.current_location = sloc.record_id ")
                .append("join setup_departments sdept on e.current_dept = sdept.record_id ")
                .append("join setup_personnel_status sps on e.present_status = sps.record_id ")
                .append("left outer join setup_personnel_status_reasons spsr on e.present_status_reason = spsr.record_id ")
                .append("join setup_mode_of_entry smoe on e.mode_of_entry = smoe.record_id ")
                .append("left join setup_pfas pfas on e.pfa_id = pfas.id ")
                .append("left join setup_bank salaryBank on e.salary_bank_id = salaryBank.id ")
                .append("left join setup_bank operationBank on e.operation_bank_id = operationBank.id ");

        employeeSelectQuery = strBuilder.toString();
    }

    public String getEmployeeSelectQuery() {
        return employeeSelectQuery;
    }

    public Map<String, Object> searchRecords(String countQuery, String selectQuery, int startRow, int pagesize)
            throws AppException {

        Integer countInt = 0;
        Map<String, Object> result = new HashMap<>();
        List<EmployeeDTO> dto_list = new ArrayList<>();
        EmployeeDTO dto = null;
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String pagedQuery = selectQuery + " limit " + startRow + ", " + pagesize;
        try {

            db_conn = dBUtil.getDatasource().getConnection();

            prep_stmt = db_conn.prepareStatement(countQuery);
            result_set = prep_stmt.executeQuery();
            while (result_set.next()) {
                countInt = Integer.valueOf(result_set.getObject(1).toString());
            }

            prep_stmt = db_conn.prepareStatement(pagedQuery);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {

                dto = new EmployeeDTO();
                dto.setRecordId(result_set.getString("employee_rec_id"));
                dto.setFileNo(result_set.getString("file_no"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));
                dto.setGender(result_set.getString("gender"));

                dto.setMaritalStatus(result_set.getString("marital_status"));
                dto.setMaritalStatusDesc(result_set.getString("_marital_status"));

                dto.setStateOfOrigin(result_set.getString("state_of_origin"));
                dto.setStateOfOriginDesc(result_set.getString("_state_of_origin"));

                dto.setLga(result_set.getString("lga"));
                dto.setLgaDesc(result_set.getString("_lga"));

                dto.setDateOfBirth(result_set.getString("date_of_birth"));
                dto.setDateOfBirthDesc(result_set.getString("_date_of_birth"));

                dto.setDateOfFirstAppointment(result_set.getString("date_of_1st_appt"));
                dto.setDateOfFirstAppointmentDesc(result_set.getString("_date_of_1st_appt"));

                dto.setPresentGradeLevel(result_set.getString("current_grade_level"));
                dto.setPresentGradeStep(result_set.getString("current_grade_step"));

                dto.setDateOfPresentAppointment(result_set.getString("current_date_of_present_appt"));
                dto.setDateOfPresentAppointmentDesc(result_set.getString("_current_date_of_present_appt"));

                dto.setConfirmationDate(result_set.getString("confirmation_date"));
                dto.setConfirmationDateDesc(result_set.getString("_confirmation_date"));

                dto.setPresentCadre(result_set.getString("current_cadre"));
                dto.setPresentCadreDesc(result_set.getString("_current_cadre"));

                dto.setPresentRank(result_set.getString("current_rank"));
                dto.setPresentRankDesc(result_set.getString("_current_rank"));

                dto.setPresentLocation(result_set.getString("current_location"));
                dto.setPresentLocationDesc(result_set.getString("_current_location"));

                dto.setDateOfPostingToPresentLocation(result_set.getString("current_date_of_posting_to_location"));
                dto.setDateOfPostingToPresentLocationDesc(result_set.getString("_current_date_of_posting_to_location"));

                dto.setPresentDepartment(result_set.getString("current_dept"));
                dto.setPresentDepartmentDesc(result_set.getString("_current_dept"));

                dto.setIsStateAcct(result_set.getString("is_state_coord"));
                dto.setIsStateAcct(result_set.getString("is_state_acct"));
                dto.setIsSecondment(result_set.getString("is_secondment"));

                dto.setDateToRetireBasedOnLengthOfStay(result_set.getString("date_retire_based_on_length_of_stay"));
                dto.setDateToRetireBasedOnLengthOfStayDesc(result_set.getString("_date_retire_based_on_length_of_stay"));

                dto.setDateToRetireBasedOnAge(result_set.getString("date_retire_based_on_age"));
                dto.setDateToRetireBasedOnAgeDesc(result_set.getString("_date_retire_based_on_age"));

                dto.setDateDueForRetirement(result_set.getString("date_due_for_retirement"));
                dto.setDateDueForRetirementDesc(result_set.getString("_date_due_for_retirement"));

                dto.setYearDueForRetirement(result_set.getString("_year_due_for_retirement"));

                dto.setPresentStatus(result_set.getString("present_status"));
                dto.setPresentStatusDesc(result_set.getString("_present_status"));
                dto.setPresentStatusReason(result_set.getString("present_status_reason"));
                dto.setPresentStatusReasonDesc(result_set.getString("_present_status_reason"));
                dto.setPresentStatusRemarks(result_set.getString("present_status_remarks"));
                dto.setDateOfPresentStatus(result_set.getString("date_of_present_status"));
                dto.setDateOfPresentStatusDesc(result_set.getString("_date_of_present_status"));
                dto.setModeOfEntry(result_set.getString("mode_of_entry"));
                dto.setModeOfEntryDesc(result_set.getString("_mode_of_entry"));
                dto.setTransferedEntryOrganization(result_set.getString("transfered_entry_organization"));
                dto.setPresentlyDisciplined(result_set.getString("presently_disciplined"));

                dto.setPfaId(result_set.getString("pfa_id"));
                dto.setPfaDescription(result_set.getString("_pfa_description"));
                dto.setPfaNumber(result_set.getString("pfa_number"));
                dto.setNhfNumber(result_set.getString("nhf_number"));
                dto.setNhisNumber(result_set.getString("nhis_number"));
                dto.setTinNumber(result_set.getString("tin_number"));
                dto.setIppisNumber(result_set.getString("ippis_number"));
                dto.setSalaryBankId(result_set.getString("salary_bank_id"));
                dto.setSalaryBankDescription(result_set.getString("_salary_bank_name"));
                dto.setSalaryBankAccountNo(result_set.getString("salary_bank_account_no"));
                dto.setOperationBankId(result_set.getString("operation_bank_id"));
                dto.setOperationBankDescription(result_set.getString("_operation_bank_name"));
                dto.setOperationBankAccountNo(result_set.getString("operation_bank_account_no"));

                dto_list.add(dto);

            }

        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }

        result.put(AppConstants.TOTAL_RECORDS, countInt);
        result.put(AppConstants.RECORD_LIST, dto_list);

        return result;
    }

    public EmployeeDTO getEmployeee(String fetchValue, String fetchBy, String fetchPurpose, FetchOptionsDTO histOptions)
            throws AppException {

        EmployeeDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getEmployeeSelectQuery());

            if (StringUtils.equals(fetchBy, AppConstants.FETCH_MODE_RECORD_ID)) {
                strBuilder.append(" where e.employee_rec_id=? ");
            } else if (StringUtils.equals(fetchBy, AppConstants.FETCH_MODE_FILE_NO)) {
                strBuilder.append(" where e.file_no=? ");
            }

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);

            if (StringUtils.equals(fetchBy, AppConstants.FETCH_MODE_RECORD_ID)) {
                prep_stmt.setString(1, fetchValue);
            } else if (StringUtils.equals(fetchBy, AppConstants.FETCH_MODE_FILE_NO)) {
                prep_stmt.setString(1, fetchValue);
            }

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = new EmployeeDTO();
                result.setRecordId(result_set.getString("employee_rec_id"));
                result.setFileNo(result_set.getString("file_no"));
                result.setSurname(result_set.getString("surname"));
                result.setOtherNames(result_set.getString("other_names"));
                result.setGender(result_set.getString("gender"));

                result.setMaritalStatus(result_set.getString("marital_status"));
                result.setMaritalStatusDesc(result_set.getString("_marital_status"));

                result.setStateOfOrigin(result_set.getString("state_of_origin"));
                result.setStateOfOriginDesc(result_set.getString("_state_of_origin"));

                result.setLga(result_set.getString("lga"));
                result.setLgaDesc(result_set.getString("_lga"));

                result.setDateOfBirth(result_set.getString("date_of_birth"));
                result.setDateOfBirthDesc(result_set.getString("_date_of_birth"));

                result.setDateOfFirstAppointment(result_set.getString("date_of_1st_appt"));
                result.setDateOfFirstAppointmentDesc(result_set.getString("_date_of_1st_appt"));

                result.setPresentGradeLevel(result_set.getString("current_grade_level"));
                result.setPresentGradeStep(result_set.getString("current_grade_step"));

                result.setDateOfPresentAppointment(result_set.getString("current_date_of_present_appt"));
                result.setDateOfPresentAppointmentDesc(result_set.getString("_current_date_of_present_appt"));

                result.setConfirmationDate(result_set.getString("confirmation_date"));
                result.setConfirmationDateDesc(result_set.getString("_confirmation_date"));

                result.setPresentCadre(result_set.getString("current_cadre"));
                result.setPresentCadreDesc(result_set.getString("_current_cadre"));

                result.setPresentRank(result_set.getString("current_rank"));
                result.setPresentRankDesc(result_set.getString("_current_rank"));

                result.setPresentLocation(result_set.getString("current_location"));
                result.setPresentLocationDesc(result_set.getString("_current_location"));

                result.setDateOfPostingToPresentLocation(result_set.getString("current_date_of_posting_to_location"));
                result.setDateOfPostingToPresentLocationDesc(result_set.getString("_current_date_of_posting_to_location"));

                result.setPresentDepartment(result_set.getString("current_dept"));
                result.setPresentDepartmentDesc(result_set.getString("_current_dept"));

                result.setIsStateAcct(result_set.getString("is_state_coord"));
                result.setIsStateAcct(result_set.getString("is_state_acct"));
                result.setIsSecondment(result_set.getString("is_secondment"));

                result.setDateToRetireBasedOnLengthOfStay(result_set.getString("date_retire_based_on_length_of_stay"));
                result.setDateToRetireBasedOnLengthOfStayDesc(result_set.getString("_date_retire_based_on_length_of_stay"));

                result.setDateToRetireBasedOnAge(result_set.getString("date_retire_based_on_age"));
                result.setDateToRetireBasedOnAgeDesc(result_set.getString("_date_retire_based_on_age"));

                result.setDateDueForRetirement(result_set.getString("date_due_for_retirement"));
                result.setDateDueForRetirementDesc(result_set.getString("_date_due_for_retirement"));

                result.setYearDueForRetirement(result_set.getString("_year_due_for_retirement"));

                result.setPrimaryPhone(result_set.getString("primary_phone"));
                result.setSecondaryPhone(result_set.getString("secondary_phone"));
                result.setPrimaryEmail(result_set.getString("primary_email"));
                result.setSecondaryEmail(result_set.getString("secondary_email"));
                result.setPresentAddress(result_set.getString("present_address"));
                result.setPermanentHomeAddress(result_set.getString("permanent_home_address"));

                result.setPresentStatus(result_set.getString("present_status"));
                result.setPresentStatusDesc(result_set.getString("_present_status"));
                result.setPresentStatusReason(result_set.getString("present_status_reason"));
                result.setPresentStatusReasonDesc(result_set.getString("_present_status_reason"));
                result.setPresentStatusRemarks(result_set.getString("present_status_remarks"));
                result.setDateOfPresentStatus(result_set.getString("date_of_present_status"));
                result.setDateOfPresentStatusDesc(result_set.getString("_date_of_present_status"));
                result.setModeOfEntry(result_set.getString("mode_of_entry"));
                result.setModeOfEntryDesc(result_set.getString("_mode_of_entry"));

                result.setTransferedEntryOrganization(result_set.getString("transfered_entry_organization"));
                result.setPresentlyDisciplined(result_set.getString("presently_disciplined"));

                result.setPfaId(result_set.getString("pfa_id"));
                result.setPfaDescription(result_set.getString("_pfa_description"));
                result.setPfaNumber(result_set.getString("pfa_number"));
                result.setNhfNumber(result_set.getString("nhf_number"));
                result.setNhisNumber(result_set.getString("nhis_number"));
                result.setTinNumber(result_set.getString("tin_number"));
                result.setIppisNumber(result_set.getString("ippis_number"));
                result.setSalaryBankId(result_set.getString("salary_bank_id"));
                result.setSalaryBankDescription(result_set.getString("_salary_bank_name"));
                result.setSalaryBankAccountNo(result_set.getString("salary_bank_account_no"));
                result.setOperationBankId(result_set.getString("operation_bank_id"));
                result.setOperationBankDescription(result_set.getString("_operation_bank_name"));
                result.setOperationBankAccountNo(result_set.getString("operation_bank_account_no"));

                //if the fetch is for update set dates to their corresponding desc formats
                if (StringUtils.equals(fetchPurpose, AppConstants.FETCH_VIEW_UPDATE)) {
                    result.setDateOfBirth(result.getDateOfBirthDesc());
                    result.setDateOfFirstAppointment(result.getDateOfFirstAppointmentDesc());
                    result.setConfirmationDate(result.getConfirmationDateDesc());
                }

                //fetch other personel data
                //FETCH CADRE
                if (histOptions.isFetchCadre() || histOptions.isFetchAll()) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("select d.record_id,d.employee_rec_id,d.cadre,s.description as _cadre,d.date_appointed ")
                            .append(" ,date_format(d.date_appointed,'%d/%m/%Y') as _date_appointed ")
                            .append(" from employee_cadre d ")
                            .append(" join setup_cadre s on d.cadre=s.record_id ")
                            .append(" where d.employee_rec_id=? order by  d.date_appointed desc");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, result.getRecordId());
                    result_set = prep_stmt.executeQuery();

                    EmployeeCadreDTO empCadre = null;
                    while (result_set.next()) {
                        empCadre = new EmployeeCadreDTO();
                        empCadre.setRecordId(result_set.getString("record_id"));
                        empCadre.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                        empCadre.setCadre(result_set.getString("cadre"));
                        empCadre.setCadreDesc(result_set.getString("_cadre"));
                        empCadre.setDateAppointed(result_set.getString("date_appointed"));
                        empCadre.setDateAppointedDesc(result_set.getString("_date_appointed"));

                        result.addCadre(empCadre);
                    }
                }

                //FATCH ACADEMIC LIST
                if (histOptions.isFetchAcadData() || histOptions.isFetchAll()) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("SELECT d.record_id,d.employee_rec_id,d.institution_type,s1.description as _institution_type,d.institution_name ")
                            .append(",d.course,s2.description as _course,d.country,s4.description as _country ")
                            .append(",d.start_date,date_format(d.start_date,'%d/%m/%Y') as _start_date ")
                            .append(",d.end_date,date_format(d.end_date,'%d/%m/%Y') as _end_date,d.qualification")
                            .append(", concat('(',s3.abbreviation,') ',s3.description) as _qualification ")
                            .append("FROM employee_education d ")
                            .append("join setup_institution_types s1 on (d.institution_type = s1.record_id) ")
                            .append("left outer join setup_courses s2 on (d.course = s2.record_id) ")
                            .append("join setup_qualifications s3 on (d.qualification = s3.record_id) ")
                            .append("join setup_countries s4 on (d.country = s4.record_id) ")
                            .append("where d.employee_rec_id=?  order by  d.end_date desc ");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, result.getRecordId());
                    result_set = prep_stmt.executeQuery();

                    AcademicDataDTO empAcad = null;
                    while (result_set.next()) {
                        empAcad = new AcademicDataDTO();
                        empAcad.setRecordId(result_set.getString("record_id"));
                        empAcad.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                        empAcad.setInstType(result_set.getString("institution_type"));
                        empAcad.setInstTypeDesc(result_set.getString("_institution_type"));
                        empAcad.setInstName(result_set.getString("institution_name"));
                        empAcad.setCourse(result_set.getString("course"));
                        empAcad.setCourseDesc(result_set.getString("_course"));
                        empAcad.setCountry(result_set.getString("country"));
                        empAcad.setCountryDesc(result_set.getString("_country"));
                        empAcad.setStartDate(result_set.getString("start_date"));
                        empAcad.setStartDateDesc(result_set.getString("_start_date"));
                        empAcad.setEndDate(result_set.getString("end_date"));
                        empAcad.setEndDateDesc(result_set.getString("_end_date"));
                        empAcad.setQualification(result_set.getString("qualification"));
                        empAcad.setQualificationDesc(result_set.getString("_qualification"));

                        result.addAcademicData(empAcad);
                    }
                }

                //FETCH APPOINTMENTS
                if (histOptions.isFetchAppointments() || histOptions.isFetchAll()) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("SELECT  d.record_id, d.employee_rec_id, d.grade_level, d.grade_step ")
                            .append(",d.date_appointed,date_format(d.date_appointed,'%d/%m/%Y') as _date_appointed ")
                            .append(",scadre.description as _present_cadre,srank.description as _present_rank ")
                            .append("FROM employee_grade_level d ")
                            .append("join setup_cadre scadre on d.present_cadre = scadre.record_id ")
                            .append("join setup_rank srank on d.present_rank = srank.record_id ")
                            .append("where d.employee_rec_id=? order by d.date_appointed desc ");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, result.getRecordId());
                    result_set = prep_stmt.executeQuery();

                    EmployeeAppointmentsDTO empAppt = null;
                    while (result_set.next()) {
                        empAppt = new EmployeeAppointmentsDTO();
                        empAppt.setRecordId(result_set.getString("record_id"));
                        empAppt.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                        empAppt.setGradeLevel(result_set.getString("grade_level"));
                        empAppt.setGradeStep(result_set.getString("grade_step"));
                        empAppt.setDateAppointed(result_set.getString("date_appointed"));
                        empAppt.setDateAppointedDesc(result_set.getString("_date_appointed"));

                        empAppt.setAppointmentCadreDesc(result_set.getString("_present_cadre"));
                        empAppt.setAppointmentRankDesc(result_set.getString("_present_rank"));

                        result.addAppointment(empAppt);
                    }
                }

                //FETCH LOCATIONS
                if (histOptions.isFetchLocations() || histOptions.isFetchAll()) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("SELECT d.record_id, d.employee_rec_id,d.location_from,d.location,s1.description as _location ")
                            .append(",d.department, s2.description as _department ")
                            .append(",d.date_of_posting,date_format(d.date_of_posting,'%d/%m/%Y') as _date_of_posting ")
                            .append(",d.is_state_coord, d.is_state_acct,d.is_recent_posting ")
                            .append("FROM employee_location d  ")
                            .append("join setup_states s1 on (d.location=s1.record_id) ")
                            .append("left outer join setup_departments s2 on (d.department=s2.record_id) ")
                            .append("where d.employee_rec_id=? order by d.date_of_posting desc ");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, result.getRecordId());
                    result_set = prep_stmt.executeQuery();

                    EmployeeLocationDTO empLocation = null;

                    String todayDate = DateTimeUtils.getActivityDate();
                    ViewHelper viewHelper = new ViewHelper();
                    while (result_set.next()) {
                        empLocation = new EmployeeLocationDTO();
                        empLocation.setRecordId(result_set.getString("record_id"));
                        empLocation.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                        empLocation.setLocationFrom(result_set.getString("location_from"));
                        empLocation.setLocation(result_set.getString("location"));
                        empLocation.setLocationDesc(result_set.getString("_location"));
                        empLocation.setDepartment(result_set.getString("department"));
                        empLocation.setDepartmentDesc(result_set.getString("_department"));
                        empLocation.setDatePosted(result_set.getString("date_of_posting"));
                        empLocation.setDatePostedDesc(result_set.getString("_date_of_posting"));

                        empLocation.setLengthOfStay(viewHelper.calculatLenghtOfStay(todayDate,
                                result_set.getString("date_of_posting"), "ymd"));

                        empLocation.setIsStateCoord(result_set.getString("is_state_coord"));
                        empLocation.setIsStateAcct(result_set.getString("is_state_acct"));
                        empLocation.setIsRecentPosting(result_set.getString("is_recent_posting"));
                        result.addLocation(empLocation);
                    }
                }

                //FETCH NEXT OF KIN
                if (histOptions.isFetchNextOfKin() || histOptions.isFetchAll()) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("SELECT d.record_id,d.employee_rec_id,d.surname,d.other_names ")
                            .append(",d.relationship, s1.description as _relationship ")
                            .append(",d.phone_no, d.email,d.address ")
                            .append("FROM employee_next_of_kin d ")
                            .append("join setup_relationships s1 on (d.relationship = s1.record_id)  ")
                            .append("where d.employee_rec_id=? ");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, result.getRecordId());
                    result_set = prep_stmt.executeQuery();

                    NextOfKinDTO empNofkin = null;
                    while (result_set.next()) {
                        empNofkin = new NextOfKinDTO();
                        empNofkin.setRecordId(result_set.getString("record_id"));
                        empNofkin.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                        empNofkin.setSurname(result_set.getString("surname"));
                        empNofkin.setOtherNames(result_set.getString("other_names"));
                        empNofkin.setRelationship(result_set.getString("relationship"));
                        empNofkin.setRelationshipDesc(result_set.getString("_relationship"));
                        empNofkin.setPhone(result_set.getString("phone_no"));
                        empNofkin.setEmail(result_set.getString("email"));
                        empNofkin.setAddress(result_set.getString("address"));

                        result.addNofkin(empNofkin);
                    }
                }

                //FETCH PERSONNEL STATUS
                if (histOptions.isFetchStatus() || histOptions.isFetchAll()) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("SELECT d.record_id,d.employee_rec_id,d.status_id,s1.description as _status_id ")
                            .append(",d.status_reason, s2.description as _status_reason,d.status_remarks")
                            .append(",d.date_attained,date_format(d.date_attained,'%d/%m/%Y') as _date_attained ")
                            .append("FROM employee_status d ")
                            .append("left outer join setup_personnel_status s1 on (d.status_id = s1.record_id)  ")
                            .append("left outer join setup_personnel_status_reasons s2 ")
                            .append("   on (d.status_reason = s2.record_id and d.status_id=s2.status_id) ")
                            .append("where d.employee_rec_id=? order by d.date_attained desc");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, result.getRecordId());
                    result_set = prep_stmt.executeQuery();

                    EmployeeStatusDTO empStatus = null;
                    while (result_set.next()) {
                        empStatus = new EmployeeStatusDTO();
                        empStatus.setRecordId(result_set.getString("record_id"));
                        empStatus.setEmployeeRecordId(result_set.getString("employee_rec_id"));

                        empStatus.setStatusId(result_set.getString("status_id"));
                        empStatus.setStatusDesc(result_set.getString("_status_id"));

                        empStatus.setStatusReason(result_set.getString("status_reason"));
                        empStatus.setStatusReasonDesc(result_set.getString("_status_reason"));

                        empStatus.setRemarks(result_set.getString("status_remarks"));

                        empStatus.setEffectiveDate(result_set.getString("date_attained"));
                        empStatus.setEffectiveDateDesc(result_set.getString("_date_attained"));

                        result.addStatus(empStatus);
                    }
                }

                //FETCH DISCIPLINE
                if (histOptions.isFetchDiscipline() || histOptions.isFetchAll()) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("select d.record_id,d.employee_rec_id,d.case_leveled_against,d.management_decision ")
                            .append(",date_format(d.date_of_sanction,'%d/%m/%Y') as _date_of_sanction  ")
                            .append(",date_format(d.date_of_closure,'%d/%m/%Y') as _date_of_closure ")
                            .append(" from employee_discipline d ")
                            .append(" where d.employee_rec_id=? order by d.date_of_sanction desc ");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, result.getRecordId());
                    result_set = prep_stmt.executeQuery();

                    DisciplineDTO discipline = null;
                    while (result_set.next()) {
                        discipline = new DisciplineDTO();
                        discipline.setRecordId(result_set.getString("record_id"));
                        discipline.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                        discipline.setCaseLeveled(result_set.getString("case_leveled_against"));
                        discipline.setManagementDecision(result_set.getString("management_decision"));
                        discipline.setDateOfSanction(result_set.getString("_date_of_sanction"));
                        discipline.setDateOfClosure(result_set.getString("_date_of_closure"));

                        result.addDiscpline(discipline);
                    }
                }

                //FETCH ANNUAL LEAVE ROSTER
                if (histOptions.isFetchAnnualLeave() || histOptions.isFetchAll()) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("select d.record_id,d.roster_year,d.employee_rec_id,d.duration ")
                            .append(",date_format(d.expected_start_date,'%d/%m/%Y') as _expected_start_date  ")
                            .append(",date_format(d.expected_end_date,'%d/%m/%Y') as _expected_end_date ")
                            .append(",d.remaining_days ")
                            .append(" from registry_annual_leave_roster d ")
                            .append(" where d.employee_rec_id=? order by d.last_mod desc ");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, result.getRecordId());
                    result_set = prep_stmt.executeQuery();

                    AnnualLeaveRosterDTO annualRoster = null;
                    while (result_set.next()) {
                        annualRoster = new AnnualLeaveRosterDTO();
                        annualRoster.setRecordId(result_set.getString("record_id"));
                        annualRoster.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                        annualRoster.setRosterYear(result_set.getString("roster_year"));
                        annualRoster.setDuration(result_set.getString("duration"));
                        annualRoster.setExpectedStartDate(result_set.getString("_expected_start_date"));
                        annualRoster.setExpectedEndDate(result_set.getString("_expected_end_date"));
                        annualRoster.setRemainingDays(result_set.getString("remaining_days"));

                        result.addAnnualLeaveRoster(annualRoster);
                    }
                }

                //FETCH LEAVE APPLICATIONS
                if (histOptions.isFetchLeaveApplications() || histOptions.isFetchAll()) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("SELECT d.record_id,d.employee_rec_id,d.duration,d.leave_application_year ")
                            .append(",date_format(d.start_date,'%d/%m/%Y') as _start_date")
                            .append(",date_format(d.expected_end_date,'%d/%m/%Y') as _expected_end_date")
                            .append(",d.before_approval_remaining_days,d.after_approval_remaining_days ")
                            .append(",d.approval_status,d.remarks,d.record_status ")
                            .append(" FROM registry_staff_leave_application d ")
                            .append(" where d.employee_rec_id=? order by d.last_mod desc ");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, result.getRecordId());
                    result_set = prep_stmt.executeQuery();

                    StaffLeaveApplicationDTO leaveApplication = null;
                    while (result_set.next()) {
                        leaveApplication = new StaffLeaveApplicationDTO();
                        leaveApplication.setRecordId(result_set.getString("record_id"));
                        leaveApplication.setLeaveApplicationYear(result_set.getString("leave_application_year"));
                        leaveApplication.setDuration(result_set.getString("duration"));

                        leaveApplication.setExpectedStartDate(result_set.getString("_start_date"));
                        leaveApplication.setExpectedEndDate(result_set.getString("_expected_end_date"));

                        leaveApplication.setBeforeApprovalRemainingDays(result_set.getString("before_approval_remaining_days"));
                        leaveApplication.setAfterApprovalRemainingDays(result_set.getString("after_approval_remaining_days"));
                        leaveApplication.setApprovalStatus(result_set.getString("approval_status"));
                        leaveApplication.setRemarks(result_set.getString("remarks"));
                        leaveApplication.setRecordStatus(result_set.getString("record_status"));

                        result.addLeaveApplication(leaveApplication);
                    }
                }

                //FETCH PERSONNEL TRAINING APPLICATIONS
                if (histOptions.isFetchFurtherTraining() || histOptions.isFetchAll()) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("SELECT d.record_id,d.employee_rec_id,d.institution,d.course,d.country,d.state ")
                            .append(",date_format(d.start_date,'%d/%m/%Y') as _start_date")
                            .append(",date_format(d.end_date,'%d/%m/%Y') as _end_date")
                            .append(",d.approval_status,d.approval_status_reason")
                            .append(",d.remarks,d.record_status,d.last_mod,d.last_mod_by ")
                            .append(",t2.description as _country ")
                            .append(",ifnull(t3.description,d.state) as _state ")
                            .append(",t4.description as _course ")
                            .append("FROM staff_further_study_application d ")
                            .append("join setup_countries t2 on d.country=t2.record_id ")
                            .append("left outer join setup_states t3 on d.state=t3.record_id ")
                            .append("join setup_courses t4 on d.course=t4.record_id ")
                            .append(" where d.employee_rec_id=? order by d.last_mod desc ");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, result.getRecordId());
                    result_set = prep_stmt.executeQuery();

                    StaffFurtherStudyApplicationDTO trainingAppl = null;
                    while (result_set.next()) {
                        trainingAppl = new StaffFurtherStudyApplicationDTO();
                        trainingAppl.setRecordId(result_set.getString("record_id"));
                        trainingAppl.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                        trainingAppl.setInstitution(result_set.getString("institution"));
                        trainingAppl.setCourseName(result_set.getString("_course"));
                        trainingAppl.setCountry(result_set.getString("country"));
                        trainingAppl.setCountryName(result_set.getString("_country"));
                        trainingAppl.setState(result_set.getString("state"));
                        trainingAppl.setStateName(result_set.getString("_state"));
                        trainingAppl.setExpectedStartDate(result_set.getString("_start_date"));
                        trainingAppl.setExpectedEndDate(result_set.getString("_end_date"));
                        trainingAppl.setApprovalStatus(result_set.getString("approval_status"));
                        trainingAppl.setApprovalStatusReason(result_set.getString("approval_status_reason"));
                        trainingAppl.setRemarks(result_set.getString("remarks"));
                        trainingAppl.setRecordStatus(result_set.getString("record_status"));

                        result.addTrainingApplication(trainingAppl);
                    }
                }

                //FETCH DOCS
                if (histOptions.isFetchDocs() || histOptions.isFetchAll()) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("SELECT d.record_id,d.employee_rec_id,d.file_name,d.remarks")
                            .append(",date_format(d.last_mod,'%d/%m/%Y') as _last_mod ")
                            .append(" FROM registry_employee_doc_upload d ")
                            .append(" where d.employee_rec_id=? order by d.last_mod desc ");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, result.getRecordId());
                    result_set = prep_stmt.executeQuery();

                    RegistryDocumentDTO document = null;
                    while (result_set.next()) {
                        document = new RegistryDocumentDTO();
                        document.setRecordId(result_set.getString("record_id"));
                        document.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                        document.setFileNameWithExt(result_set.getString("file_name"));

                        document.setFileNameNoExt(StringUtils.substringBeforeLast(document.getFileNameWithExt(), "."));

                        document.setRemarks(result_set.getString("remarks"));
                        document.setLastMod(result_set.getString("_last_mod"));

                        result.addDocument(document);
                    }
                }

                //GET PHOTO FILE PATH
                try {
                    ViewHelper viewHelper = new ViewHelper();
                    strBuilder = new StringBuilder();
                    strBuilder.append(viewHelper.getPersonnelDocsDir()).append("/").append(result.getRecordId())
                            .append("/").append(result.getRecordId()).append(".jpg");
                    String photoFileName = strBuilder.toString();

                    File photoFile = new File(photoFileName);
                    if (photoFile.exists()) {
                        strBuilder = new StringBuilder();
                        strBuilder.append(viewHelper.getRelativeDocsDir()).append("/").append(result.getRecordId())
                                .append("/").append(result.getRecordId()).append(".jpg");
                        result.setPhotoUrl(strBuilder.toString());
                    } else {
                        result.setPhotoUrl(AppConstants.DEFAULT_PHOTO_URL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            } else {

                throw new AppException(AppConstants.NOT_FOUND);
            }

        } catch (Exception exc) {
            exc.printStackTrace();

            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean createEmployee(EmployeeDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //check duplicate file number
            String check_query = "select employee_rec_id from employee where file_no=?";
            prep_stmt = db_conn.prepareStatement(check_query);
            prep_stmt.setString(1, dto.getFileNo());
            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE);
            }

            //check duplicate prim phone
            check_query = "select employee_rec_id from employee where primary_phone=?";
            prep_stmt = db_conn.prepareStatement(check_query);
            prep_stmt.setString(1, dto.getPrimaryPhone());
            result_set = prep_stmt.executeQuery();
            exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE_PHONE);
            }

            //check duplicate sec phone
            if (!StringUtils.isBlank(dto.getSecondaryPhone())) {
                check_query = "select employee_rec_id from employee where secondary_phone=?";
                prep_stmt = db_conn.prepareStatement(check_query);
                prep_stmt.setString(1, dto.getSecondaryPhone());
                result_set = prep_stmt.executeQuery();
                exists = result_set.next();
                if (exists) {
                    throw new AppException(AppConstants.DUPLICATE_SEC_PHONE);
                }
            }

            //check duplicate prim email
            check_query = "select employee_rec_id from employee where primary_email=?";
            prep_stmt = db_conn.prepareStatement(check_query);
            prep_stmt.setString(1, dto.getPrimaryEmail());
            result_set = prep_stmt.executeQuery();
            exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE_EMAIL);
            }

            //check duplicate sec email
            if (!StringUtils.isBlank(dto.getSecondaryEmail())) {
                check_query = "select employee_rec_id from employee where secondary_email=?";
                prep_stmt = db_conn.prepareStatement(check_query);
                prep_stmt.setString(1, dto.getSecondaryEmail());
                result_set = prep_stmt.executeQuery();
                exists = result_set.next();
                if (exists) {
                    throw new AppException(AppConstants.DUPLICATE_SEC_EMAIL);
                }
            }

            //fetch annual emolument and set it, else throw exception
            String query = "select emulument from setup_staff_emulument where grade_level=? and grade_level_step=?";
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getPresentGradeLevel());
            prep_stmt.setString(2, dto.getPresentGradeStep());
            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                dto.setPresentAnnualEmulument(result_set.getString("emulument"));
            } else {
                throw new AppException(AppConstants.NO_EMOLUMENT);
            }

            //now do the insert
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("insert into employee ")
                    .append("(")
                    .append("employee_rec_id,file_no,surname,other_names,gender,marital_status,state_of_origin,lga,date_of_birth")
                    .append(",date_of_1st_appt,current_grade_level,current_grade_step,current_date_of_present_appt")
                    .append(",current_cadre,current_date_of_present_cadre,current_rank")
                    .append(",current_location,current_date_of_posting_to_location,current_dept")
                    .append(",is_state_coord,is_state_acct,is_secondment")
                    .append(",date_retire_based_on_length_of_stay,date_retire_based_on_age,date_due_for_retirement")
                    .append(",primary_phone,secondary_phone,primary_email,secondary_email")
                    .append(",present_address,permanent_home_address,created,created_by,last_mod,last_mod_by")
                    .append(",present_status,date_of_present_status,mode_of_entry,transfered_entry_organization,presently_disciplined")
                    .append(",current_annual_emulument")
                    .append(")")
                    .append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getRecordId()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(StringUtils.upperCase(dto.getFileNo())));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getSurname()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getOtherNames()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getGender()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getMaritalStatus()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getStateOfOrigin()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getLga()));
            prep_stmt.setString(9, serviceUtil.resetToDbDateFormat(dto.getDateOfBirth()));
            prep_stmt.setString(10, serviceUtil.resetToDbDateFormat(dto.getDateOfFirstAppointment()));
            prep_stmt.setString(11, serviceUtil.getValueOrNull(dto.getPresentGradeLevel()));
            prep_stmt.setString(12, serviceUtil.getValueOrNull(dto.getPresentGradeStep()));
            prep_stmt.setString(13, serviceUtil.resetToDbDateFormat(dto.getDateOfPresentAppointment()));
            prep_stmt.setString(14, serviceUtil.getValueOrNull(dto.getPresentCadre()));
            prep_stmt.setString(15, serviceUtil.resetToDbDateFormat(dto.getDateOfPresentCadre()));

            prep_stmt.setString(16, serviceUtil.getValueOrNull(dto.getPresentRank()));

            prep_stmt.setString(17, serviceUtil.getValueOrNull(dto.getPresentLocation()));
            prep_stmt.setString(18, serviceUtil.resetToDbDateFormat(dto.getDateOfPostingToPresentLocation()));
            prep_stmt.setString(19, serviceUtil.getValueOrNull(dto.getPresentDepartment()));

            prep_stmt.setString(20, "N");
            prep_stmt.setString(21, "N");
            prep_stmt.setString(22, "N");

            prep_stmt.setString(23, serviceUtil.resetToDbDateFormat(dto.getDateToRetireBasedOnLengthOfStay()));
            prep_stmt.setString(24, serviceUtil.resetToDbDateFormat(dto.getDateToRetireBasedOnAge()));
            prep_stmt.setString(25, serviceUtil.resetToDbDateFormat(dto.getDateDueForRetirement()));
            prep_stmt.setString(26, serviceUtil.getValueOrNull(dto.getPrimaryPhone()));
            prep_stmt.setString(27, serviceUtil.getValueOrNull(dto.getSecondaryPhone()));
            prep_stmt.setString(28, serviceUtil.getValueOrNull(dto.getPrimaryEmail()));
            prep_stmt.setString(29, serviceUtil.getValueOrNull(dto.getSecondaryEmail()));
            prep_stmt.setString(30, serviceUtil.getValueOrNull(dto.getPresentAddress()));
            prep_stmt.setString(31, serviceUtil.getValueOrNull(dto.getPermanentHomeAddress()));
            prep_stmt.setString(32, serviceUtil.getValueOrNull(dto.getCreated()));
            prep_stmt.setString(33, serviceUtil.getValueOrNull(dto.getCreatedBy()));
            prep_stmt.setString(34, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(35, serviceUtil.getValueOrNull(dto.getLastModBy()));

            prep_stmt.setString(36, serviceUtil.getValueOrNull(dto.getPresentStatus()));
            prep_stmt.setString(37, serviceUtil.resetToDbDateFormat(dto.getDateOfPresentStatus()));

            prep_stmt.setString(38, serviceUtil.getValueOrNull(dto.getModeOfEntry()));

            prep_stmt.setString(39, serviceUtil.getValueOrNull(dto.getTransferedEntryOrganization()));

            prep_stmt.setString(40, "N");

            prep_stmt.setString(41, serviceUtil.getValueOrNull(dto.getPresentAnnualEmulument()));

            prep_stmt.executeUpdate();

            //insert all the history stuff
            //insert grade level dates
            strBuilder = new StringBuilder();
            strBuilder.append("insert into employee_grade_level_dates ")
                    .append(" (employee_rec_id,").append("grade_").append(dto.getPresentGradeLevel()).append(") ")
                    .append(" values (?,?) ");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getRecordId()));
            prep_stmt.setString(2, serviceUtil.resetToDbDateFormat(dto.getDateOfPresentAppointment()));
            prep_stmt.executeUpdate();

            //insert grade level and step history
            strBuilder = new StringBuilder();
            strBuilder.append("insert into employee_grade_level ")
                    .append(" (employee_rec_id,grade_level,grade_step,date_appointed,record_status ")
                    .append(",created,created_by,last_mod,last_mod_by,present_cadre,present_rank)")
                    .append(" values (?,?,?,?,?,?,?,?,?,?,?) ");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getRecordId()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getPresentGradeLevel()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getPresentGradeStep()));
            prep_stmt.setString(4, serviceUtil.resetToDbDateFormat(dto.getDateOfPresentAppointment()));
            prep_stmt.setString(5, AppConstants.ACTIVE);
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getCreated()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getCreatedBy()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(9, serviceUtil.getValueOrNull(dto.getLastModBy()));

            prep_stmt.setString(10, serviceUtil.getValueOrNull(dto.getPresentCadre()));
            prep_stmt.setString(11, serviceUtil.getValueOrNull(dto.getPresentRank()));

            prep_stmt.executeUpdate();

            //insert the cadre history
            strBuilder = new StringBuilder();
            strBuilder.append("insert into employee_cadre ")
                    .append(" (employee_rec_id,cadre,date_appointed,record_status,created,created_by,last_mod,last_mod_by) ")
                    .append(" values (?,?,?,?,?,?,?,?) ");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getRecordId()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getPresentCadre()));
            prep_stmt.setString(3, serviceUtil.resetToDbDateFormat(dto.getDateOfPresentAppointment()));
            prep_stmt.setString(4, AppConstants.ACTIVE);
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getCreated()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getCreatedBy()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getLastModBy()));

            prep_stmt.executeUpdate();

            //insert the location history
            strBuilder = new StringBuilder();
            strBuilder.append("insert into employee_location ")
                    .append(" (employee_rec_id,location,department,date_of_posting,is_state_coord,is_state_acct ")
                    .append(" ,created,created_by,last_mod,last_mod_by)")
                    .append(" values (?,?,?,?,?,?,?,?,?,?) ");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getRecordId()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getPresentLocation()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getPresentDepartment()));
            prep_stmt.setString(4, serviceUtil.resetToDbDateFormat(dto.getDateOfPostingToPresentLocation()));
            prep_stmt.setString(5, "N");
            prep_stmt.setString(6, "N");
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getCreated()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getCreatedBy()));
            prep_stmt.setString(9, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(10, serviceUtil.getValueOrNull(dto.getLastModBy()));

            prep_stmt.executeUpdate();

            //insert the next of kin history
            strBuilder = new StringBuilder();
            strBuilder.append("insert into employee_next_of_kin ")
                    .append(" (employee_rec_id,surname,other_names,relationship,phone_no,email,address ")
                    .append(" ,created,created_by,last_mod,last_mod_by)")
                    .append(" values (?,?,?,?,?,?,?,?,?,?,?) ");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            for (NextOfKinDTO nofkin : dto.getNextOfKinList()) {
                prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getRecordId()));
                prep_stmt.setString(2, serviceUtil.getValueOrNull(nofkin.getSurname()));
                prep_stmt.setString(3, serviceUtil.getValueOrNull(nofkin.getOtherNames()));
                prep_stmt.setString(4, serviceUtil.getValueOrNull(nofkin.getRelationship()));
                prep_stmt.setString(5, serviceUtil.getValueOrNull(nofkin.getPhone()));
                prep_stmt.setString(6, serviceUtil.getValueOrNull(nofkin.getEmail()));
                prep_stmt.setString(7, serviceUtil.getValueOrNull(nofkin.getAddress()));
                prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getCreated()));
                prep_stmt.setString(9, serviceUtil.getValueOrNull(dto.getCreatedBy()));
                prep_stmt.setString(10, serviceUtil.getValueOrNull(dto.getLastMod()));
                prep_stmt.setString(11, serviceUtil.getValueOrNull(dto.getLastModBy()));

                prep_stmt.addBatch();
            }

            prep_stmt.executeBatch();

            //insert the academic data history
            strBuilder = new StringBuilder();
            strBuilder.append("insert into employee_education ")
                    .append(" (employee_rec_id,institution_type,institution_name,course,country,start_date,end_date,qualification ")
                    .append(" ,created,created_by,last_mod,last_mod_by)")
                    .append(" values (?,?,?,?,?,?,?,?,?,?,?,?) ");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            for (AcademicDataDTO acad : dto.getAcademicDataList()) {
                prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getRecordId()));
                prep_stmt.setString(2, serviceUtil.getValueOrNull(acad.getInstType()));
                prep_stmt.setString(3, serviceUtil.getValueOrNull(acad.getInstName()));
                prep_stmt.setString(4, serviceUtil.getValueOrNull(acad.getCourse()));
                prep_stmt.setString(5, serviceUtil.getValueOrNull(acad.getCountry()));
                prep_stmt.setString(6, serviceUtil.resetToDbDateFormat(acad.getStartDate()));
                prep_stmt.setString(7, serviceUtil.resetToDbDateFormat(acad.getEndDate()));
                prep_stmt.setString(8, serviceUtil.getValueOrNull(acad.getQualification()));
                prep_stmt.setString(9, serviceUtil.getValueOrNull(dto.getCreated()));
                prep_stmt.setString(10, serviceUtil.getValueOrNull(dto.getCreatedBy()));
                prep_stmt.setString(11, serviceUtil.getValueOrNull(dto.getLastMod()));
                prep_stmt.setString(12, serviceUtil.getValueOrNull(dto.getLastModBy()));

                prep_stmt.addBatch();
            }

            prep_stmt.executeBatch();

            //update the current qualifications employee column
            //fetch them
            strBuilder = new StringBuilder();
            strBuilder.append("select concat('#',sq.abbreviation,'#') as qualification_abbrev  ")
                    .append(" from employee_education ee ")
                    .append(" join setup_qualifications sq on ee.qualification=sq.record_id  ")
                    .append(" where ee.employee_rec_id=?");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getRecordId());
            result_set = prep_stmt.executeQuery();

            strBuilder = new StringBuilder();
            while (result_set.next()) {
                strBuilder.append(result_set.getString("qualification_abbrev")).append(",");
            }

            //now update
            String current_qualifications = strBuilder.toString();
            if (!StringUtils.isBlank(current_qualifications)) {
                current_qualifications = StringUtils.substringBeforeLast(current_qualifications, ",");
                query = "update employee set current_qualifications=? where employee_rec_id=?";
                prep_stmt = db_conn.prepareStatement(query);
                prep_stmt.setString(1, current_qualifications);
                prep_stmt.setString(2, dto.getRecordId());

                prep_stmt.executeUpdate();
            }

            //
            //insert the status history
            //NO NEED FOR REASON AND STATUS SINCE ITS A NEW EMPLOYEE
            strBuilder = new StringBuilder();
            strBuilder.append("insert into employee_status ")
                    .append(" (employee_rec_id,status_id,date_attained,record_status ")
                    .append(",created,created_by,last_mod,last_mod_by)")
                    .append(" values (?,?,?,?,?,?,?,?) ");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getRecordId()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getPresentStatus()));
            prep_stmt.setString(3, serviceUtil.resetToDbDateFormat(dto.getDateOfPresentStatus()));
            prep_stmt.setString(4, AppConstants.ACTIVE);
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getCreated()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getCreatedBy()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getLastModBy()));

            prep_stmt.executeUpdate();

            //commit the transaction
            db_conn.commit();
            result = true;

        } catch (Exception exc) {
            if (db_conn != null) {
                try {
                    db_conn.rollback();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            exc.printStackTrace();
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean updateEmployeeBiodata(EmployeeDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update employee ")
                    .append(" set ")
                    .append("surname=?")
                    .append(",other_names=?")
                    .append(",date_of_birth=?")
                    .append(",gender=?")
                    .append(",marital_status=?")
                    .append(",state_of_origin=?")
                    .append(",lga=?")
                    .append(",date_retire_based_on_length_of_stay=?")
                    .append(",date_retire_based_on_age=?")
                    .append(",date_due_for_retirement=?")
                    .append(",last_mod=?,last_mod_by=? ")
                    .append(" where employee_rec_id=?");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getSurname()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getOtherNames()));
            prep_stmt.setString(3, serviceUtil.resetToDbDateFormat(dto.getDateOfBirth()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getGender()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getMaritalStatus()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getStateOfOrigin()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getLga()));
            prep_stmt.setString(8, serviceUtil.resetToDbDateFormat(dto.getDateToRetireBasedOnLengthOfStay()));
            prep_stmt.setString(9, serviceUtil.resetToDbDateFormat(dto.getDateToRetireBasedOnAge()));
            prep_stmt.setString(10, serviceUtil.resetToDbDateFormat(dto.getDateDueForRetirement()));
            prep_stmt.setString(11, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(12, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(13, serviceUtil.getValueOrNull(dto.getRecordId()));

            prep_stmt.executeUpdate();

            result = true;

        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean updateEmplyeeEmploymentData(EmployeeDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update employee ")
                    .append(" set ")
                    .append("date_of_1st_appt=?")
                    .append(",confirmation_date=?")
                    .append(",date_retire_based_on_length_of_stay=?")
                    .append(",date_retire_based_on_age=?")
                    .append(",date_due_for_retirement=?")
                    .append(",last_mod=?,last_mod_by=? ")
                    .append(" where employee_rec_id=?");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.resetToDbDateFormat(dto.getDateOfFirstAppointment()));
            prep_stmt.setString(2, serviceUtil.resetToDbDateFormat(dto.getConfirmationDate()));
            prep_stmt.setString(3, serviceUtil.resetToDbDateFormat(dto.getDateToRetireBasedOnLengthOfStay()));
            prep_stmt.setString(4, serviceUtil.resetToDbDateFormat(dto.getDateToRetireBasedOnAge()));
            prep_stmt.setString(5, serviceUtil.resetToDbDateFormat(dto.getDateDueForRetirement()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getRecordId()));

            prep_stmt.executeUpdate();

            result = true;

        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean updateEmployeeContact(EmployeeDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update employee ")
                    .append(" set ")
                    .append("primary_phone=?")
                    .append(",secondary_phone=?")
                    .append(",primary_email=?")
                    .append(",secondary_email=?")
                    .append(",present_address=?")
                    .append(",permanent_home_address=?")
                    .append(",last_mod=?,last_mod_by=? ")
                    .append(" where employee_rec_id=?");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getPrimaryPhone()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getSecondaryPhone()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getPrimaryEmail()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getSecondaryEmail()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getPresentAddress()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getPermanentHomeAddress()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(9, serviceUtil.getValueOrNull(dto.getRecordId()));

            prep_stmt.executeUpdate();

            result = true;

        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean addEmployeeAcadData(AcademicDataDTO acadDto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //insert the academic data history
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("insert into employee_education ")
                    .append(" (employee_rec_id,institution_type,institution_name,course,country,start_date,end_date,qualification ")
                    .append(" ,created,created_by,last_mod,last_mod_by)")
                    .append(" values (?,?,?,?,?,?,?,?,?,?,?,?) ");
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, serviceUtil.getValueOrNull(acadDto.getEmployeeRecordId()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(acadDto.getInstType()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(acadDto.getInstName()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(acadDto.getCourse()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(acadDto.getCountry()));
            prep_stmt.setString(6, serviceUtil.resetToDbDateFormat(acadDto.getStartDate()));
            prep_stmt.setString(7, serviceUtil.resetToDbDateFormat(acadDto.getEndDate()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(acadDto.getQualification()));
            prep_stmt.setString(9, serviceUtil.getValueOrNull(acadDto.getCreated()));
            prep_stmt.setString(10, serviceUtil.getValueOrNull(acadDto.getCreatedBy()));
            prep_stmt.setString(11, serviceUtil.getValueOrNull(acadDto.getLastMod()));
            prep_stmt.setString(12, serviceUtil.getValueOrNull(acadDto.getLastModBy()));

            prep_stmt.executeUpdate();

            //update the current qualifications employee column
            //fetch them
            strBuilder = new StringBuilder();
            strBuilder.append("select concat('#',sq.abbreviation,'#') as qualification_abbrev  ")
                    .append(" from employee_education ee ")
                    .append(" join setup_qualifications sq on ee.qualification=sq.record_id  ")
                    .append(" where ee.employee_rec_id=?");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, acadDto.getEmployeeRecordId());
            result_set = prep_stmt.executeQuery();

            strBuilder = new StringBuilder();
            while (result_set.next()) {
                strBuilder.append(result_set.getString("qualification_abbrev")).append(",");
            }

            //now update
            String current_qualifications = strBuilder.toString();
            if (!StringUtils.isBlank(current_qualifications)) {
                current_qualifications = StringUtils.substringBeforeLast(current_qualifications, ",");
                query = "update employee set current_qualifications=?,last_mod=?,last_mod_by=? where employee_rec_id=?";
                prep_stmt = db_conn.prepareStatement(query);
                prep_stmt.setString(1, current_qualifications);
                prep_stmt.setString(2, serviceUtil.getValueOrNull(acadDto.getLastMod()));
                prep_stmt.setString(3, serviceUtil.getValueOrNull(acadDto.getLastModBy()));
                prep_stmt.setString(4, serviceUtil.getValueOrNull(acadDto.getEmployeeRecordId()));

                prep_stmt.executeUpdate();
            }

            db_conn.commit();
            result = true;

        } catch (Exception exc) {
            if (db_conn != null) {
                try {
                    db_conn.rollback();
                } catch (Exception e) {

                }
            }
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public AcademicDataDTO getEmployeeAcadData(String recordId, String fetchView)
            throws AppException {

        AcademicDataDTO result = new AcademicDataDTO();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("SELECT d.record_id,d.employee_rec_id,d.institution_type,s1.description as _institution_type,d.institution_name ")
                    .append(",d.course,s2.description as _course,d.country,s4.description as _country ")
                    .append(",d.start_date,date_format(d.start_date,'%d/%m/%Y') as _start_date ")
                    .append(",d.end_date,date_format(d.end_date,'%d/%m/%Y') as _end_date,d.qualification")
                    .append(", concat('(',s3.abbreviation,') ',s3.description) as _qualification ")
                    .append("FROM employee_education d ")
                    .append("join setup_institution_types s1 on (d.institution_type = s1.record_id) ")
                    .append("left outer join setup_courses s2 on (d.course = s2.record_id) ")
                    .append("join setup_qualifications s3 on (d.qualification = s3.record_id) ")
                    .append("join setup_countries s4 on (d.country = s4.record_id) ")
                    .append("where d.record_id=?  order by  d.institution_type desc ");
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, recordId);
            result_set = prep_stmt.executeQuery();

            boolean found = result_set.next();

            if (found) {
                result.setRecordId(result_set.getString("record_id"));
                result.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                result.setInstType(result_set.getString("institution_type"));
                result.setInstTypeDesc(result_set.getString("_institution_type"));
                result.setInstName(result_set.getString("institution_name"));
                result.setCourse(result_set.getString("course"));
                result.setCourseDesc(result_set.getString("_course"));
                result.setCountry(result_set.getString("country"));
                result.setCountryDesc(result_set.getString("_country"));
                result.setStartDate(result_set.getString("start_date"));
                result.setStartDateDesc(result_set.getString("_start_date"));
                result.setEndDate(result_set.getString("end_date"));
                result.setEndDateDesc(result_set.getString("_end_date"));
                result.setQualification(result_set.getString("qualification"));
                result.setQualificationDesc(result_set.getString("_qualification"));

                if (StringUtils.equals(fetchView, AppConstants.FETCH_VIEW_UPDATE)) {
                    result.setStartDate(result.getStartDateDesc());
                    result.setEndDate(result.getEndDateDesc());
                }
            }

        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean updateEmployeeAcadData(AcademicDataDTO acadDto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //update the academic data history
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update employee_education set")
                    .append(" institution_type=?,institution_name=?,course=?,country=?,start_date=?,end_date=?,qualification=? ")
                    .append(" ,last_mod=?,last_mod_by=? ")
                    .append(" where record_id=? ");
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, serviceUtil.getValueOrNull(acadDto.getInstType()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(acadDto.getInstName()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(acadDto.getCourse()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(acadDto.getCountry()));
            prep_stmt.setString(5, serviceUtil.resetToDbDateFormat(acadDto.getStartDate()));
            prep_stmt.setString(6, serviceUtil.resetToDbDateFormat(acadDto.getEndDate()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(acadDto.getQualification()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(acadDto.getLastMod()));
            prep_stmt.setString(9, serviceUtil.getValueOrNull(acadDto.getLastModBy()));
            prep_stmt.setString(10, serviceUtil.getValueOrNull(acadDto.getRecordId()));

            prep_stmt.executeUpdate();

            //update the current qualifications employee column
            //fetch them
            strBuilder = new StringBuilder();
            strBuilder.append("select concat('#',sq.abbreviation,'#') as qualification_abbrev  ")
                    .append(" from employee_education ee ")
                    .append(" join setup_qualifications sq on ee.qualification=sq.record_id  ")
                    .append(" where ee.employee_rec_id=?");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, acadDto.getEmployeeRecordId());
            result_set = prep_stmt.executeQuery();

            strBuilder = new StringBuilder();
            while (result_set.next()) {
                strBuilder.append(result_set.getString("qualification_abbrev")).append(",");
            }

            //now update
            String current_qualifications = strBuilder.toString();
            if (!StringUtils.isBlank(current_qualifications)) {
                current_qualifications = StringUtils.substringBeforeLast(current_qualifications, ",");
                query = "update employee set current_qualifications=?,last_mod=?,last_mod_by=? where employee_rec_id=?";
                prep_stmt = db_conn.prepareStatement(query);
                prep_stmt.setString(1, current_qualifications);
                prep_stmt.setString(2, serviceUtil.getValueOrNull(acadDto.getLastMod()));
                prep_stmt.setString(3, serviceUtil.getValueOrNull(acadDto.getLastModBy()));
                prep_stmt.setString(4, serviceUtil.getValueOrNull(acadDto.getEmployeeRecordId()));

                prep_stmt.executeUpdate();
            }

            db_conn.commit();
            result = true;

        } catch (Exception exc) {
            if (db_conn != null) {
                try {
                    db_conn.rollback();
                } catch (Exception e) {

                }
            }
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public NextOfKinDTO getEmployeeNofkin(String recordId)
            throws AppException {

        NextOfKinDTO result = new NextOfKinDTO();

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("SELECT d.record_id,d.employee_rec_id,d.surname,d.other_names ")
                    .append(",d.relationship, s1.description as _relationship ")
                    .append(",d.phone_no, d.email,d.address ")
                    .append("FROM employee_next_of_kin d ")
                    .append("join setup_relationships s1 on (d.relationship = s1.record_id)  ")
                    .append("where d.record_id=? ");
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, recordId);
            result_set = prep_stmt.executeQuery();

            boolean found = result_set.next();

            if (found) {
                result.setRecordId(result_set.getString("record_id"));
                result.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                result.setSurname(result_set.getString("surname"));
                result.setOtherNames(result_set.getString("other_names"));
                result.setRelationship(result_set.getString("relationship"));
                result.setRelationshipDesc(result_set.getString("_relationship"));
                result.setPhone(result_set.getString("phone_no"));
                result.setEmail(result_set.getString("email"));
                result.setAddress(result_set.getString("address"));
            }

        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean updateEmployeeNofkin(NextOfKinDTO nofkinDto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            //insert the next of kin history
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update employee_next_of_kin set ")
                    .append(" surname=?,other_names=?,relationship=?,phone_no=?,email=?,address=? ")
                    .append(" ,last_mod=?,last_mod_by=? ")
                    .append(" where record_id=? ");
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, serviceUtil.getValueOrNull(nofkinDto.getSurname()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(nofkinDto.getOtherNames()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(nofkinDto.getRelationship()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(nofkinDto.getPhone()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(nofkinDto.getEmail()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(nofkinDto.getAddress()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(nofkinDto.getLastMod()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(nofkinDto.getLastModBy()));
            prep_stmt.setString(9, serviceUtil.getValueOrNull(nofkinDto.getRecordId()));

            prep_stmt.executeUpdate();

            result = true;

        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean updateEmployeeAccountDetails(EmployeeDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();

            strBuilder.append("update employee ")
                    .append(" set ")
                    .append("pfa_id=?")
                    .append(",pfa_number=?")
                    .append(",nhf_number=?")
                    .append(",nhis_number=?")
                    .append(",tin_number=?")
                    .append(",ippis_number=?")
                    .append(",salary_bank_id=?")
                    .append(",salary_bank_account_no=?")
                    .append(",operation_bank_id=?")
                    .append(",operation_bank_account_no=?")
                    .append(",last_mod=?,last_mod_by=? ")
                    .append(" where file_no=?");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getPfaId()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getPfaNumber()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getNhfNumber()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getNhisNumber()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getTinNumber()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getIppisNumber()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getSalaryBankId()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getSalaryBankAccountNo()));
            prep_stmt.setString(9, serviceUtil.getValueOrNull(dto.getOperationBankId()));
            prep_stmt.setString(10, serviceUtil.getValueOrNull(dto.getOperationBankAccountNo()));
            prep_stmt.setString(11, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(12, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(13, serviceUtil.getValueOrNull(dto.getFileNo()));

            prep_stmt.executeUpdate();

            result = true;

        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

}
