/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.promotion;

import dto.employees.EmployeeDTO;
import dto.employees.FetchOptionsDTO;
import dto.promotion.ConversionDTO;
import dto.setup.RankDTO;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import service.employees.EmployeeService;
import service.utils.ApplicationCodesService;
import view.utils.AppConstants;
import view.utils.ApplicationCodesLoader;
import view.utils.ViewHelper;
import view.validators.ConversionValidator;

public class ConversionMBean implements Serializable {

    private EmployeeDTO recordDto;
    private ConversionDTO conversionDto;
    private String searchFileNo;
    private EmployeeService employeeService;
    private ViewHelper viewHelper;
    private FetchOptionsDTO fetchOptions;
    private boolean searchStarted;

    private ApplicationCodesService codeService;
    private ApplicationCodesLoader codeLoader;
    
    ConversionValidator validator;

    private Map<String, String> qualificationsMap;
    private Map<String, String> stepMap;

    public ConversionMBean() {
        employeeService = new EmployeeService();
        viewHelper = new ViewHelper();
        fetchOptions = new FetchOptionsDTO();
        fetchOptions.setFetchConversion(true);

        codeService = new ApplicationCodesService();
        codeLoader = new ApplicationCodesLoader();
        validator=new ConversionValidator();
    }

    private void loadRecord(String file_no) {
        if (!StringUtils.isBlank(file_no)) {
            try {
                recordDto = employeeService.getEmployeee(file_no, AppConstants.FETCH_MODE_FILE_NO, AppConstants.FETCH_VIEW_ONLY, fetchOptions);
            } catch (Exception exc) {
                recordDto = null;
                System.out.println(exc.getMessage());
            }
        } else {
            recordDto = null;
        }
    }

    public void handleNewGradeLevelSelect() {
        fetchGradeSteps();
        fetchRank();
    }

    private void fetchGradeSteps() {
        try {
            stepMap = codeService.getGradeStep(conversionDto.getGradeLevel());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleNewCadreSelect() {
        fetchRank();
    }

    private void fetchRank() {
        try {
            RankDTO rank = codeService.getRank(conversionDto.getConversionCadre(), conversionDto.getGradeLevel());
            if (rank != null) {
                conversionDto.setConversionRank(rank.getRecordId());
                conversionDto.setConversionRankDesc(rank.getDescription());
            } else {
                 conversionDto.setConversionRank(null);
                 conversionDto.setConversionRankDesc("?");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

              conversionDto.setConversionRank(null);
                 conversionDto.setConversionRankDesc("?");
        }
    }

    public void prepareNewConversionDialog() {
        conversionDto = new ConversionDTO();
        conversionDto.setEmployeeRecordId(recordDto.getRecordId());
        conversionDto.setEmployeeFileNo(recordDto.getFileNo());

        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("conversionDialogPanel");
    }

    public void closeConversionConfirmDialog() {
        conversionDto = new ConversionDTO();

        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.update("conversionDialogPanel");
    }

    public void searchAction(ActionEvent event) {
        searchStarted = true;
        loadRecord(searchFileNo);
    }

    public EmployeeDTO getRecordDto() {
        if (recordDto == null) {
            recordDto = new EmployeeDTO();
        }
        return recordDto;
    }

    public void setRecordDto(EmployeeDTO recordDto) {
        this.recordDto = recordDto;
    }

    public ConversionDTO getConversionDto() {
        if (conversionDto == null) {
            conversionDto = new ConversionDTO();
        }
        return conversionDto;
    }

    public void setConversionDto(ConversionDTO conversionDto) {
        this.conversionDto = conversionDto;
    }

    public String getSearchFileNo() {
        return searchFileNo;
    }

    public void setSearchFileNo(String searchFileNo) {
        this.searchFileNo = searchFileNo;
    }

    public boolean isSearchStarted() {
        return searchStarted;
    }

    public void setSearchStarted(boolean searchStarted) {
        this.searchStarted = searchStarted;
    }

    private void fetchQualifications(String inst_type) {
        try {
            qualificationsMap = codeService.getQualificationsByCategory(inst_type);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<String, String> getQualificationsMap() {
        if (qualificationsMap == null) {
            qualificationsMap = new LinkedHashMap<>();
        }
        return qualificationsMap;
    }

    public void setQualificationsMap(Map<String, String> qualificationsMap) {
        this.qualificationsMap = qualificationsMap;
    }

    public Map<String, String> getStepMap() {
        if (stepMap == null) {
            stepMap = new LinkedHashMap<>();
        }
        return stepMap;
    }

    public void setStepMap(Map<String, String> stepMap) {
        this.stepMap = stepMap;
    }

    public void preSubmitConversionAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean validation_outcome = validator.validateNewConversion(context, conversionDto);

        RequestContext reqContext = RequestContext.getCurrentInstance();

        if (validation_outcome) {
            //reqContext.execute("PF('disciplineDialog').hide()");

            reqContext.update("submitConversionDialogPanel");
            reqContext.execute("PF('submitConversionDialog').show()");
        } else {
            reqContext.update("conversionDialogPanel");
        }
    }

}
