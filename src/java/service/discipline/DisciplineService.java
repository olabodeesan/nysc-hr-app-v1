/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.discipline;

import app.exceptions.AppException;
import dto.discipline.DisciplineDTO;
import dto.employees.EmployeeDTO;
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

/**
 *
 * @author IronHide
 */
public class DisciplineService implements Serializable {

    private String employeeSelectQuery;
    private String suspendedListClause, disengagedListClause, disciplinedClause;
    private ServiceUtil serviceUtil;
    private DBUtil dBUtil;

    public DisciplineService() {
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
                .append("join setup_mode_of_entry smoe on e.mode_of_entry = smoe.record_id ");

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

                dto_list.add(dto);

            }

            //now fetch the discipline details and add
            //REWORK THESE LATER
            if (!dto_list.isEmpty()) {
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("SELECT d.record_id,d.employee_rec_id,d.case_leveled_against,d.management_decision ")
                        .append(",date_format(d.date_of_sanction,'%d/%m/%Y') as _date_of_sanction ")
                        .append("FROM employee_discipline d ")
                        .append("where d.record_id  ")
                        .append("in (SELECT MAX(record_id) FROM employee_discipline where date_of_closure is null GROUP BY employee_rec_id) ");
                prep_stmt = db_conn.prepareStatement(strBuilder.toString());
                result_set = prep_stmt.executeQuery();

                while (result_set.next()) {

                    for (EmployeeDTO e : dto_list) {
                        if (StringUtils.equalsIgnoreCase(e.getRecordId(), result_set.getString("employee_rec_id"))) {
                            e.setDisciplineCase(result_set.getString("case_leveled_against"));
                            e.setDisciplineMgtDecision(result_set.getString("management_decision"));
                            e.setDisciplineDate(result_set.getString("_date_of_sanction"));
                        }
                    }

                }
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

    public boolean disciplineEmployee(DisciplineDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //update the employee table
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update employee ")
                    .append(" set ")
                    .append("presently_disciplined=?")
                    .append(",last_mod=?,last_mod_by=? ")
                    .append(" where employee_rec_id=?");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, "Y");
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));

            prep_stmt.executeUpdate();

            //then insert the history
            strBuilder = new StringBuilder();
            strBuilder.append("insert into employee_discipline ")
                    .append(" (employee_rec_id,case_leveled_against,management_decision,date_of_sanction ")
                    .append(",created,created_by,last_mod,last_mod_by)")
                    .append(" values (?,?,?,?,?,?,?,?) ");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getCaseLeveled()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getManagementDecision()));
            prep_stmt.setString(4, serviceUtil.resetToDbDateFormat(dto.getDateOfSanction()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getCreated()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getCreatedBy()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(8, serviceUtil.getValueOrNull(dto.getLastModBy()));

            prep_stmt.executeUpdate();

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
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public boolean closeEmployeeDiscipline(DisciplineDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //update the employee table
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update employee ")
                    .append(" set ")
                    .append("presently_disciplined=?")
                    .append(",last_mod=?,last_mod_by=? ")
                    .append(" where employee_rec_id=?");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, "N");
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getEmployeeRecordId()));

            prep_stmt.executeUpdate();

            //then update the history
            strBuilder = new StringBuilder();
            strBuilder.append("update employee_discipline ")
                    .append(" set ")
                    .append(" date_of_closure=?,last_mod=?,last_mod_by=? ")
                    .append(" where record_id=? ");
            query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, serviceUtil.resetToDbDateFormat(dto.getDateOfClosure()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(3, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getRecordId()));

            prep_stmt.executeUpdate();

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
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }

    public DisciplineDTO getEmployeeDiscipline(String recordId)
            throws AppException {

        DisciplineDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            //select the details
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("select record_id,employee_rec_id,case_leveled_against,management_decision ")
                    .append(",date_format(date_of_sanction,'%d/%m/%Y') as _date_of_sanction  ")
                    .append(",date_format(date_of_closure,'%d/%m/%Y') as _date_of_closure ")
                    .append(" from employee_discipline ")
                    .append(" where record_id=?");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, recordId);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = new DisciplineDTO();
                result.setRecordId(result_set.getString("record_id"));
                result.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                result.setCaseLeveled(result_set.getString("case_leveled_against"));
                result.setManagementDecision(result_set.getString("management_decision"));
                result.setDateOfSanction(result_set.getString("_date_of_sanction"));
                result.setDateOfClosure(result_set.getString("_date_of_closure"));
            } else {
                throw new AppException(AppConstants.NOT_FOUND);
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

    public boolean updateEmployeeDiscipline(DisciplineDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update employee_discipline ")
                    .append(" set ")
                    .append(" case_leveled_against=?,management_decision=? ")
                    .append(",date_of_sanction=?,date_of_closure=? ")
                    .append(" ,last_mod=?,last_mod_by=? ")
                    .append(" where record_id=? ");
            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, serviceUtil.getValueOrNull(dto.getCaseLeveled()));
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getManagementDecision()));
            prep_stmt.setString(3, serviceUtil.resetToDbDateFormat(dto.getDateOfSanction()));
            prep_stmt.setString(4, serviceUtil.resetToDbDateFormat(dto.getDateOfClosure()));
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getLastMod()));
            prep_stmt.setString(6, serviceUtil.getValueOrNull(dto.getLastModBy()));
            prep_stmt.setString(7, serviceUtil.getValueOrNull(dto.getRecordId()));

            prep_stmt.executeUpdate();

            result = true;

        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }
}
