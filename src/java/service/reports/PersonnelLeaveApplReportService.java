/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.reports;

import app.exceptions.AppException;
import dto.setup.dto.registry.StaffLeaveApplicationDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import service.utils.DBUtil;
import service.utils.ServiceUtil;

/**
 *
 * @author IronHide
 */
public class PersonnelLeaveApplReportService implements Serializable{
    
    private final ServiceUtil serviceUtil;
    private final DBUtil dBUtil;

    public PersonnelLeaveApplReportService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
    }

    public List<StaffLeaveApplicationDTO> searchRecords(String selectQuery)
            throws AppException {
        
        List<StaffLeaveApplicationDTO> result = new ArrayList<>();
        StaffLeaveApplicationDTO dto = null;
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;
        
        try {

            db_conn = dBUtil.getDatasource().getConnection();

            prep_stmt = db_conn.prepareStatement(selectQuery);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {

                dto = new StaffLeaveApplicationDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                dto.setEmployeeFileNo(result_set.getString("file_no"));
                dto.setGradeLevel(result_set.getString("current_grade_level"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));
                dto.setPhone(result_set.getString("primary_phone"));
                dto.setEmail(result_set.getString("primary_email"));
                dto.setDepartment(result_set.getString("_department"));

                dto.setLeaveApplicationYear(result_set.getString("leave_application_year"));
                dto.setDuration(result_set.getString("duration"));

                dto.setExpectedStartDate(result_set.getString("_start_date"));
                dto.setOldExpectedStartDate(dto.getExpectedStartDate());

                dto.setExpectedEndDate(result_set.getString("_expected_end_date"));

                dto.setBeforeApprovalRemainingDays(result_set.getString("before_approval_remaining_days"));
                dto.setAfterApprovalRemainingDays(result_set.getString("after_approval_remaining_days"));
                dto.setApprovalStatus(result_set.getString("approval_status"));
                dto.setRemarks(result_set.getString("remarks"));
                dto.setRecordStatus(result_set.getString("record_status"));

                dto.setLastMod(result_set.getString("last_mod"));
                dto.setLastModBy(result_set.getString("last_mod_by"));

                result.add(dto);

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
    
}
