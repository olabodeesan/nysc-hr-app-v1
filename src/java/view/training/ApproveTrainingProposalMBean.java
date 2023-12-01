/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.training;

import dto.setup.dto.training.TrainingProposalDTO;
import java.text.SimpleDateFormat;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.FacesLocal;
import service.training.TrainingProposalService;
import view.utils.DateTimeUtils;
import view.utils.ViewHelper;
import view.validators.TrainingValidator;

/**
 *
 * @author IronHide
 */
public class ApproveTrainingProposalMBean {

    private TrainingProposalDTO recordDto;
    private String proposalRecordId;

    private final TrainingProposalService trainingService;
    private final TrainingValidator validator;
    private final ViewHelper viewHelper;

    public ApproveTrainingProposalMBean() {
        trainingService = new TrainingProposalService();        
        validator = new TrainingValidator();
        viewHelper = new ViewHelper();

        
        loadTrainingProposal();
    }
    
    private void loadTrainingProposal(){
        FacesContext context = FacesContext.getCurrentInstance();
        proposalRecordId = FacesLocal.getRequestParameter(context, "vhsuYagsh62fshkfd");
        
        if(!StringUtils.isBlank(proposalRecordId)){
            try{
                recordDto = trainingService.getTrainingProposal(proposalRecordId, true, true);
                
                if (recordDto != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    recordDto.setStartDate(dateFormat.parse(recordDto.getExpectedStartDate()));
                    recordDto.setEndDate(dateFormat.parse(recordDto.getExpectedEndDate()));
                }
            }catch(Exception e){
                
            }
        }
    }

    public void approveAction(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateTrainingProposal(context, recordDto, true, true, true);

        if (validation_outcome) {
            try {
                String activityDate = DateTimeUtils.getActivityDate();
                String currentUser = viewHelper.getAppUser().getUsername();

                recordDto.setLastMod(activityDate);
                recordDto.setLastModBy(currentUser);                

                boolean result = trainingService.approveTrainingProposal(recordDto);

                if (result) {
                    try {
                        FacesLocal.redirect(context, "training/training-proposal-success.jsf?nav=training&vyTasGsK=%s&vhsuYagsh62fshkfd=%s&8uss23kj=%s",
                                "sDhsf6dfasugf7374ugsydgfaksgfysgf", recordDto.getRecordId(), "ashdjahsd75678sdk");
                    } catch (Exception exc) {
                        //think of what to do here
                    }
                } else {
                    ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
                }

            } catch (Exception e) {
               ViewHelper.addErrorMessage(context, null, "System Error: An error occured, please try again.", null);
            }
        }
    }

    public TrainingProposalDTO getRecordDto() {
        if (recordDto == null) {
            recordDto = new TrainingProposalDTO();
        }
        return recordDto;
    }

    public void setRecordDto(TrainingProposalDTO recordDto) {
        this.recordDto = recordDto;
    }
    
}
