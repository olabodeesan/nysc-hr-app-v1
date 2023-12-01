/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.promotion;

import dto.promotion.PromotionExerciseDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Gainsolutions
 */
public class PromotionExerciseMBean implements Serializable {

    List<PromotionExerciseDTO> promotionList;
    PromotionExerciseDTO promotionDto, selectedRecord;

    private String srchYear;
    private String currentCountQuery;
    private String currentSelectQuery;
    private StringBuilder countQueryBuilder, selectQueryBuilder, generalStringBuilder;
    private int totalRows = 0;
    private int currentPage = 0;
    private int startRow;
    private int pageSize = 35;
    private String paginationDescription;

    public PromotionExerciseMBean() {
        promotionList = new ArrayList<>();
        promotionDto = new PromotionExerciseDTO();
        promotionDto.setPromotionYear("2014");
        promotionDto.setNoDueForPromotion("3000");
        promotionDto.setNoPromoted("2000");
        promotionList.add(promotionDto);

        promotionDto = new PromotionExerciseDTO();
        promotionDto.setPromotionYear("2013");
        promotionDto.setNoDueForPromotion("3500");
        promotionDto.setNoPromoted("3000");
        promotionList.add(promotionDto);

        promotionDto = new PromotionExerciseDTO();
        promotionDto.setPromotionYear("2012");
        promotionDto.setNoDueForPromotion("4000");
        promotionDto.setNoPromoted("3500");
        promotionList.add(promotionDto);

    }

    public void nextPage(ActionEvent ae) {

        if ((currentPage * pageSize) < totalRows) {
            currentPage++;
        }

        if ((currentPage * pageSize) > totalRows) {
            currentPage--;
        }

        //  fillResults();
    }

    public void previousPage(ActionEvent ae) {

        if (currentPage <= 0) {
            currentPage = 0;
        } else {
            currentPage--;
        }
        //  fillResults();
    }

    public void searchAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
    }

    public String getPaginationDescription() {
//        if (!getRecordList().isEmpty()) {
//            paginationDescription = "Displaying " + (startRow + 1);
//            if ((startRow + 1) < totalRows) {
//                paginationDescription += "-";
//
//                if ((startRow + pageSize) < totalRows) {
//                    paginationDescription += (startRow + pageSize);
//                } else {
//                    paginationDescription += (totalRows);
//                }
//            }
//            paginationDescription += " of " + (totalRows);
//        } else {
//            paginationDescription = "No Records Found";
//        }
//
        return paginationDescription;
    }

    public void setPaginationDescription(String paginationDescription) {
        this.paginationDescription = paginationDescription;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentCountQuery() {
        return currentCountQuery;
    }

    public void setCurrentCountQuery(String currentCountQuery) {
        this.currentCountQuery = currentCountQuery;
    }

    public String getCurrentSelectQuery() {
        return currentSelectQuery;
    }

    public void setCurrentSelectQuery(String currentSelectQuery) {
        this.currentSelectQuery = currentSelectQuery;
    }

    public List<PromotionExerciseDTO> getPromotionList() {
        if (promotionList == null) {
            promotionList = new ArrayList<>();
        }
        return promotionList;
    }

    public void setPromotionList(List<PromotionExerciseDTO> promotionList) {

        this.promotionList = promotionList;
    }

    public PromotionExerciseDTO getPromotionDto() {
        if (promotionDto == null) {
            promotionDto = new PromotionExerciseDTO();
        }
        return promotionDto;
    }

    public void setPromotionDto(PromotionExerciseDTO promotionDto) {
        this.promotionDto = promotionDto;
    }

    public String getSrchYear() {
        return srchYear;
    }

    public void setSrchYear(String srchYear) {
        this.srchYear = srchYear;
    }

    public PromotionExerciseDTO getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(PromotionExerciseDTO selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

}
