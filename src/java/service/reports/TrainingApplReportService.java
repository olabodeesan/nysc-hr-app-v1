/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.reports;

import app.exceptions.AppException;
import dto.setup.dto.training.StaffFurtherStudyApplicationDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.math.NumberUtils;
import service.utils.DBUtil;
import service.utils.ServiceUtil;
import view.utils.AppConstants;

/**
 *
 * @author IronHide
 */
public class TrainingApplReportService implements Serializable {

    private final ServiceUtil serviceUtil;
    private final DBUtil dBUtil;

    public TrainingApplReportService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
    }

    public List<StaffFurtherStudyApplicationDTO> searchRecords(String selectQuery)
            throws AppException {

        List<StaffFurtherStudyApplicationDTO> result = new ArrayList<>();
        StaffFurtherStudyApplicationDTO dto = null;
        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {

            db_conn = dBUtil.getDatasource().getConnection();

            prep_stmt = db_conn.prepareStatement(selectQuery);
            result_set = prep_stmt.executeQuery();

            while (result_set.next()) {

                dto = new StaffFurtherStudyApplicationDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                dto.setEmployeeFileNo(result_set.getString("file_no"));
                dto.setGradeLevel(result_set.getString("current_grade_level"));
                dto.setSurname(result_set.getString("surname"));
                dto.setOtherNames(result_set.getString("other_names"));
                dto.setPhone(result_set.getString("primary_phone"));
                dto.setEmail(result_set.getString("primary_email"));
                dto.setDepartment(result_set.getString("_department"));
                
                dto.setInstitution(result_set.getString("institution"));
                dto.setCourse(result_set.getString("course"));
                dto.setCourseName(result_set.getString("_course"));
                
                dto.setCountry(result_set.getString("country"));
                dto.setCountryName(result_set.getString("_country"));
                
                int state_id = NumberUtils.toInt(result_set.getString("state"));
                if(state_id!=0){
                    dto.setState(result_set.getString("state"));
                }else{
                    dto.setLocation(result_set.getString("_state"));
                }
                
                dto.setStateName(result_set.getString("_state"));
                dto.setExpectedStartDate(result_set.getString("_start_date"));
                dto.setExpectedEndDate(result_set.getString("_end_date"));
                dto.setApprovalStatus(result_set.getString("approval_status"));
                dto.setApprovalStatusReason(result_set.getString("approval_status_reason"));
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
