/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.promotion;

import java.io.Serializable;

/**
 *
 * @author Gainsolutions
 */
public class PromotionExerciseDTO implements Serializable{
    private String promotionYear;
    private String noDueForPromotion;
    private String noPromoted;
    
    private String created,createdBy;
    private String lastMode,lastModBy;

    public PromotionExerciseDTO() {
    }

    
    public String getPromotionYear() {
        return promotionYear;
    }

    public void setPromotionYear(String promotionYear) {
        this.promotionYear = promotionYear;
    }

    public String getNoDueForPromotion() {
        return noDueForPromotion;
    }

    public void setNoDueForPromotion(String noDueForPromotion) {
        this.noDueForPromotion = noDueForPromotion;
    }

    public String getNoPromoted() {
        return noPromoted;
    }

    public void setNoPromoted(String noPromoted) {
        this.noPromoted = noPromoted;
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

    public String getLastMode() {
        return lastMode;
    }

    public void setLastMode(String lastMode) {
        this.lastMode = lastMode;
    }

    public String getLastModBy() {
        return lastModBy;
    }

    public void setLastModBy(String lastModBy) {
        this.lastModBy = lastModBy;
    }
    
    
}
