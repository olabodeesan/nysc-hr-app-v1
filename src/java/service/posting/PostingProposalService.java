/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.posting;

import app.exceptions.AppException;
import dto.posting.PostingDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;

/**
 *
 * @author Gainsolutions
 */
public class PostingProposalService implements Serializable {

    private String employeePostingSelectQuery;

    private ServiceUtil serviceUtil;
    private DBUtil dBUtil;

    public PostingProposalService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
        initSelectQuery();
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
                .append(",e.current_cadre,scadre.description as _current_cadre, e.current_location,sloc.description as _current_location")
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
                .append(",e.present_status_remarks,e.mode_of_entry,smoe.description as _mode_of_entry ")
                .append("from employee e ")
                .append("join setup_marital_status sms on e.marital_status = sms.record_id ")
                .append("join setup_states ss on e.state_of_origin=ss.record_id ")
                .append("join setup_lgas slga on e.lga = slga.record_id ")
                .append("join setup_cadre scadre on e.current_cadre = scadre.record_id ")
                .append("join setup_states sloc on e.current_location = sloc.record_id ")
                .append("join setup_departments sdept on e.current_dept = sdept.record_id ")
                .append("join setup_personnel_status sps on e.present_status = sps.record_id ")
                .append("left outer join setup_personnel_status_reasons spsr on e.present_status_reason = spsr.record_id ")
                .append("join setup_mode_of_entry smoe on e.mode_of_entry = smoe.record_id ");

        employeePostingSelectQuery = strBuilder.toString();
    }

    public String getEmployeePostingSelectQuery() {
        return employeePostingSelectQuery;
    }

    public Map<String, Object> searchRecords(String countQuery, String selectQuery, int startRow, int pagesize)
            throws AppException {

        Integer countInt = 0;
        Map<String, Object> result = new HashMap<>();
        List<PostingDTO> dto_list = new ArrayList<>();
        PostingDTO dto = null;
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        String todayDate = DateTimeUtils.getActivityDate();
        ViewHelper viewHelper = new ViewHelper();

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

                dto = new PostingDTO();
                dto.setRecordId(result_set.getString("employee_rec_id"));
                dto.setFileNo(result_set.getString("file_no"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));

                dto.setGender(result_set.getString("gender"));

                dto.setMaritalStatus(result_set.getString("marital_status"));
                dto.setMaritalStatusDesc(result_set.getString("_marital_status"));
                dto.setGradeLevel(result_set.getString("current_grade_level"));

                dto.setStateOfOrigin(result_set.getString("state_of_origin"));
                dto.setStateOfOriginDesc(result_set.getString("_state_of_origin"));

                dto.setPresentCadre(result_set.getString("current_cadre"));
                dto.setPresentCadreDesc(result_set.getString("_current_cadre"));

                dto.setPresentLocation(result_set.getString("current_location"));
                dto.setPresentLocationDesc(result_set.getString("_current_location"));

                dto.setDateOfPostingToPresentLocation(result_set.getString("current_date_of_posting_to_location"));
                dto.setDateOfPostingToPresentLocationDesc(result_set.getString("_current_date_of_posting_to_location"));

                dto.setLengthOfStay(viewHelper.calculatLenghtOfStay(todayDate,
                        result_set.getString("current_date_of_posting_to_location"), "ymd"));

                dto.setIsStateAcct(result_set.getString("is_state_coord"));
                dto.setIsStateAcct(result_set.getString("is_state_acct"));
                dto.setIsSecondment(result_set.getString("is_secondment"));

                dto_list.add(dto);

            }

        } catch (Exception exc) {
            exc.printStackTrace();
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
}
