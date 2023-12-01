/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.training;

import app.exceptions.AppException;
import dto.setup.TrainingSpecializationDTO;
import dto.setup.dto.training.TrainingParticipantDTO;
import dto.setup.dto.training.TrainingProposalDTO;
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
public class TrainingProposalService implements Serializable {

    private final ServiceUtil serviceUtil;
    private final DBUtil dBUtil;

    public TrainingProposalService() {
        serviceUtil = new ServiceUtil();
        dBUtil = new DBUtil();
    }

    public Map<String, Object> searchRecords(String countQuery, String selectQuery, int startRow, int pagesize)
            throws AppException {

        Integer countInt = 0;
        Map<String, Object> result = new LinkedHashMap<>();
        List<TrainingProposalDTO> dto_list = new ArrayList<>();
        TrainingProposalDTO dto = null;
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

                dto = new TrainingProposalDTO();
                dto.setRecordId(result_set.getString("record_id"));
                dto.setTitle(result_set.getString("title"));
                dto.setTrainingType(result_set.getString("training_type"));
                dto.setTrainingTypeName(result_set.getString("_training_type"));
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
                dto.setObjective(result_set.getString("objective"));
                dto.setConsultant(result_set.getString("consultant"));
                dto.setConsultantName(result_set.getString("_consultant"));
                dto.setExpectedCertificate(result_set.getString("expected_certificate"));
                dto.setExpectedCertificateName(result_set.getString("_expected_certificate"));
                dto.setApprovalStatus(result_set.getString("approval_status"));
                dto.setApprovalStatusReason(result_set.getString("approval_status_reason"));
                dto.setRemarks(result_set.getString("remarks"));
                dto.setRecordStatus(result_set.getString("record_status"));
                dto.setLastMod(result_set.getString("last_mod"));
                dto.setLastModBy(result_set.getString("last_mod_by"));

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

    public TrainingProposalDTO getTrainingProposal(String recordId, boolean fetchSpecializations, boolean fetchEnrollees)
            throws AppException {

        TrainingProposalDTO result = null;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("select d.record_id,d.title,d.training_type,d.country,d.state ")
                    .append(",date_format(d.start_date,'%d/%m/%Y') as _start_date")
                    .append(",date_format(d.end_date,'%d/%m/%Y') as _end_date")
                    .append(",d.objective,d.consultant,d.expected_certificate,d.approval_status,d.approval_status_reason ")
                    .append(",d.remarks,d.record_status,d.last_mod,d.last_mod_by ")
                    .append(",t1.description as _training_type ")
                    .append(",t2.description as _country ")
                    .append(",t3.description as _state ")
                    .append(",t4.description as _consultant ")
                    .append(",t5.description as _expected_certificate ")
                    .append(" from training_proposal d ")
                    .append(" join setup_training_type t1 on d.training_type=t1.record_id ")
                    .append(" join setup_countries t2 on d.country=t2.record_id ")
                    .append(" left outer join setup_states t3 on d.state=t3.record_id ")
                    .append(" join setup_training_consultant t4 on d.consultant=t4.record_id ")
                    .append(" join setup_training_certificates t5 on d.expected_certificate=t5.record_id  ")
                    .append(" where d.record_id=? ");

            String query = strBuilder.toString();

            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, recordId);

            result_set = prep_stmt.executeQuery();
            boolean found = result_set.next();

            if (found) {
                result = new TrainingProposalDTO();
                result.setRecordId(result_set.getString("record_id"));
                result.setTitle(result_set.getString("title"));
                result.setTrainingType(result_set.getString("training_type"));
                result.setTrainingTypeName(result_set.getString("_training_type"));
                result.setCountry(result_set.getString("country"));
                result.setCountryName(result_set.getString("_country"));
                result.setState(result_set.getString("state"));
                result.setStateName(result_set.getString("_state"));
                result.setExpectedStartDate(result_set.getString("_start_date"));
                result.setExpectedEndDate(result_set.getString("_end_date"));
                result.setObjective(result_set.getString("objective"));
                result.setConsultant(result_set.getString("consultant"));
                result.setConsultantName(result_set.getString("_consultant"));
                result.setExpectedCertificate(result_set.getString("expected_certificate"));
                result.setExpectedCertificateName(result_set.getString("_expected_certificate"));
                result.setApprovalStatus(result_set.getString("approval_status"));
                result.setApprovalStatusReason(result_set.getString("approval_status_reason"));
                result.setRemarks(result_set.getString("remarks"));
                result.setRecordStatus(result_set.getString("record_status"));
                result.setLastMod(result_set.getString("last_mod"));
                result.setLastModBy(result_set.getString("last_mod_by"));

                //fetch specializations if value is true
                if (fetchSpecializations) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("select d.training_proposal_id,d.specialization_id ")
                            .append(",t2.description as _specialization")
                            .append(" from training_proposal_specializations d")
                            .append(" join setup_training_specializations t2 on d.specialization_id=t2.record_id ")
                            .append(" where d.training_proposal_id=? ");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, recordId);

                    result_set = prep_stmt.executeQuery();
                    TrainingSpecializationDTO specialization = null;
                    while (result_set.next()) {
                        specialization = new TrainingSpecializationDTO();
                        specialization.setRecordId(result_set.getString("specialization_id"));
                        specialization.setDescription(result_set.getString("_specialization"));

                        result.addSpecialization(specialization);
                    }
                }

                //fetch enrollees if value is true
                if (fetchEnrollees) {
                    strBuilder = new StringBuilder();
                    strBuilder.append("select d.training_proposal_id,d.employee_rec_id ")
                            .append(",t2.file_no,t2.surname,t2.other_names,t2.current_grade_level ")
                            .append(",t2.primary_phone,t2.primary_email ")
                            .append(",t3.description as _department ")
                            .append(" from training_participants d")
                            .append(" join employee t2 on d.employee_rec_id=t2.employee_rec_id ")
                            .append(" join setup_departments t3 on t2.current_dept=t3.record_id ")
                            .append(" where d.training_proposal_id=? ");
                    query = strBuilder.toString();
                    prep_stmt = db_conn.prepareStatement(query);
                    prep_stmt.setString(1, recordId);

                    result_set = prep_stmt.executeQuery();
                    TrainingParticipantDTO participant = null;
                    while (result_set.next()) {
                        participant = new TrainingParticipantDTO();
                        participant.setEmployeeRecordId(result_set.getString("employee_rec_id"));
                        participant.setFileNo(result_set.getString("file_no"));
                        participant.setGradeLevel(result_set.getString("current_grade_level"));
                        participant.setSurname(result_set.getString("surname"));
                        participant.setOtherNames(result_set.getString("other_names"));
                        participant.setPhone(result_set.getString("primary_phone"));
                        participant.setEmail(result_set.getString("primary_email"));
                        participant.setDepartment(result_set.getString("_department"));

                        result.addParticipant(participant);
                    }
                }

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

    public boolean createTrainingProposal(TrainingProposalDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);

            //check duplicate title
            String check_query = "select record_id from training_proposal where title=?";

            prep_stmt = db_conn.prepareStatement(check_query);
            prep_stmt.setString(1, dto.getTitle());
            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE);
            }

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("insert into training_proposal ")
                    .append("(record_id,title,training_type,country,state,start_date,end_date,objective,consultant,expected_certificate")
                    .append(",approval_status,approval_status_reason,remarks,record_status,created,created_by,last_mod,last_mod_by )")
                    .append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getRecordId());
            prep_stmt.setString(2, dto.getTitle());
            prep_stmt.setString(3, dto.getTrainingType());
            prep_stmt.setString(4, dto.getCountry());
            prep_stmt.setString(5, serviceUtil.getValueOrNull(dto.getState()));
            prep_stmt.setString(6, serviceUtil.resetToDbDateFormat(dto.getExpectedStartDate()));
            prep_stmt.setString(7, serviceUtil.resetToDbDateFormat(dto.getExpectedEndDate()));
            prep_stmt.setString(8, dto.getObjective());
            prep_stmt.setString(9, dto.getConsultant());
            prep_stmt.setString(10, dto.getExpectedCertificate());
            prep_stmt.setString(11, dto.getApprovalStatus());
            prep_stmt.setString(12, serviceUtil.getValueOrNull(dto.getApprovalStatusReason()));
            prep_stmt.setString(13, serviceUtil.getValueOrNull(dto.getRemarks()));
            prep_stmt.setString(14, dto.getRecordStatus());
            prep_stmt.setString(15, dto.getCreated());
            prep_stmt.setString(16, dto.getCreatedBy());
            prep_stmt.setString(17, dto.getLastMod());
            prep_stmt.setString(18, dto.getLastModBy());

            prep_stmt.executeUpdate();

            //insert the areas of specialization
            if (!dto.getSpecializations().isEmpty()) {
                query = "insert into training_proposal_specializations values (?,?)";
                prep_stmt = db_conn.prepareStatement(query);

                for (TrainingSpecializationDTO s : dto.getSpecializations()) {
                    prep_stmt.setString(1, dto.getRecordId());
                    prep_stmt.setString(2, s.getRecordId());

                    prep_stmt.addBatch();
                }

                prep_stmt.executeBatch();
            }

            //insert the participants
            if (!dto.getParticipants().isEmpty()) {
                query = "insert into training_participants values (?,?)";
                prep_stmt = db_conn.prepareStatement(query);

                for (TrainingParticipantDTO p : dto.getParticipants()) {
                    prep_stmt.setString(1, dto.getRecordId());
                    prep_stmt.setString(2, p.getEmployeeRecordId());

                    prep_stmt.addBatch();
                }

                prep_stmt.executeBatch();
            }

            //commit transaction and return
            db_conn.commit();
            result = true;

        } catch (Exception exc) {
            System.out.println(exc.getMessage());
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

    public boolean updateTrainingProposal(TrainingProposalDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);
            
            //log old record
            logTrainingProposal(db_conn, dto);

            //continue
            //check duplicate title
            String check_query = "select record_id from training_proposal where title=? and record_id<>?";

            prep_stmt = db_conn.prepareStatement(check_query);
            prep_stmt.setString(1, dto.getTitle());
            prep_stmt.setString(2, dto.getRecordId());
            result_set = prep_stmt.executeQuery();
            boolean exists = result_set.next();
            if (exists) {
                throw new AppException(AppConstants.DUPLICATE);
            }

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update training_proposal ")
                    .append(" set ")
                    .append("title=?")
                    .append(",training_type=?")
                    .append(",country=?")
                    .append(",state=?")
                    .append(",start_date=?")
                    .append(",end_date=?")
                    .append(",objective=?")
                    .append(",consultant=?")
                    .append(",expected_certificate=?")
                    .append(",remarks=?")
                    .append(",last_mod=?")
                    .append(",last_mod_by=?")
                    .append(" where record_id=? ");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getTitle());
            prep_stmt.setString(2, dto.getTrainingType());
            prep_stmt.setString(3, dto.getCountry());
            prep_stmt.setString(4, serviceUtil.getValueOrNull(dto.getState()));
            prep_stmt.setString(5, serviceUtil.resetToDbDateFormat(dto.getExpectedStartDate()));
            prep_stmt.setString(6, serviceUtil.resetToDbDateFormat(dto.getExpectedEndDate()));
            prep_stmt.setString(7, dto.getObjective());
            prep_stmt.setString(8, dto.getConsultant());
            prep_stmt.setString(9, dto.getExpectedCertificate());
            prep_stmt.setString(10, serviceUtil.getValueOrNull(dto.getRemarks()));
            prep_stmt.setString(11, dto.getLastMod());
            prep_stmt.setString(12, dto.getLastModBy());
            prep_stmt.setString(13, dto.getRecordId());

            prep_stmt.executeUpdate();

            //delete trainng specializations
            query = "delete from training_proposal_specializations where training_proposal_id=?";
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getRecordId());
            
            prep_stmt.executeUpdate();
            
            //now re-insert the areas of specialization
            if (!dto.getSpecializations().isEmpty()) {
                query = "insert into training_proposal_specializations values (?,?)";
                prep_stmt = db_conn.prepareStatement(query);

                for (TrainingSpecializationDTO s : dto.getSpecializations()) {
                    prep_stmt.setString(1, dto.getRecordId());
                    prep_stmt.setString(2, s.getRecordId());

                    prep_stmt.addBatch();
                }

                prep_stmt.executeBatch();
            }

            //delete trainng participants
            query = "delete from training_participants where training_proposal_id=?";
            prep_stmt = db_conn.prepareStatement(query);
            prep_stmt.setString(1, dto.getRecordId());
            
            prep_stmt.executeUpdate();
            //now re-insert the participants
            if (!dto.getParticipants().isEmpty()) {
                query = "insert into training_participants values (?,?)";
                prep_stmt = db_conn.prepareStatement(query);

                for (TrainingParticipantDTO p : dto.getParticipants()) {
                    prep_stmt.setString(1, dto.getRecordId());
                    prep_stmt.setString(2, p.getEmployeeRecordId());

                    prep_stmt.addBatch();
                }

                prep_stmt.executeBatch();
            }

            //commit transaction and return
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
    
    public boolean approveTrainingProposal(TrainingProposalDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);
            
            //log old record
            logTrainingProposal(db_conn, dto);

            //continue
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update training_proposal ")
                    .append("set ")
                    .append("approval_status=?")
                    .append(",approval_status_reason=?")
                    .append(",last_mod=? ")
                    .append(",last_mod_by=? ")
                    .append(" where record_id=?");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getApprovalStatus());
            prep_stmt.setString(2, serviceUtil.getValueOrNull(dto.getApprovalStatusReason()));
            prep_stmt.setString(3, dto.getLastMod());
            prep_stmt.setString(4, dto.getLastModBy());
            prep_stmt.setString(5, dto.getRecordId());

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
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }
    
    public boolean deleteTrainingProposal(TrainingProposalDTO dto)
            throws AppException {

        boolean result = false;

        Connection db_conn = null;
        PreparedStatement prep_stmt = null;
        ResultSet result_set = null;

        try {
            db_conn = dBUtil.getDatasource().getConnection();
            db_conn.setAutoCommit(false);
            
            //log old record
            logTrainingProposal(db_conn, dto);

            //continue
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("update training_proposal ")
                    .append("set ")
                    .append("record_status=? ")
                    .append(",last_mod=? ")
                    .append(",last_mod_by=? ")
                    .append(" where record_id=?");

            String query = strBuilder.toString();
            prep_stmt = db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getRecordStatus());
            prep_stmt.setString(2, dto.getLastMod());
            prep_stmt.setString(3, dto.getLastModBy());
            prep_stmt.setString(4, dto.getRecordId());

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
            serviceUtil.close(result_set);
            serviceUtil.close(prep_stmt);
            serviceUtil.close(db_conn);
        }
        return result;
    }
    
    //history log
    public void logTrainingProposal(Connection log_db_conn,TrainingProposalDTO dto)
            throws AppException {
        
        //NO NEED TO RE-INIT CONNECTION
        //ITS PASSED
        PreparedStatement prep_stmt = null;

        try {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("insert into training_proposal_history ")
                    .append("(training_proposal_id,title,training_type,country,state,start_date,end_date,objective,consultant,expected_certificate")
                    .append(",approval_status,approval_status_reason,remarks,record_status,last_mod,last_mod_by )")
                    .append(" (")
                    .append("select record_id,title,training_type,country,state,start_date,end_date,objective,consultant,expected_certificate")
                    .append(",approval_status,approval_status_reason,remarks,record_status,last_mod,last_mod_by ")
                    .append(" from training_proposal ")
                    .append(" where record_id=? ")
                    .append(") ");

            String query = strBuilder.toString();
            prep_stmt = log_db_conn.prepareStatement(query);

            prep_stmt.setString(1, dto.getRecordId());

            prep_stmt.executeUpdate();

        } catch (Exception exc) {
            throw new AppException(exc.getMessage());
        } finally {
            serviceUtil.close(prep_stmt);
        }
    }
    
}
