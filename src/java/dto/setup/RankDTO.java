/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.setup;

import java.io.Serializable;

/**
 *
 * @author IronHide
 */
public class RankDTO implements Serializable{
    
    private String recordId;
    private String description;
    private String cadreId,cadreDesc;
    private String gradeLevel;
    private String recordStatus;
    private String created;
    private String createdBy;
    private String lastMod;
    private String lastModBy;

    public RankDTO() {
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getLastMod() {
        return lastMod;
    }

    public void setLastMod(String lastMod) {
        this.lastMod = lastMod;
    }

    public String getLastModBy() {
        return lastModBy;
    }

    public void setLastModBy(String lastModBy) {
        this.lastModBy = lastModBy;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getCadreId() {
        return cadreId;
    }

    public void setCadreId(String cadreId) {
        this.cadreId = cadreId;
    }

    public String getCadreDesc() {
        return cadreDesc;
    }

    public void setCadreDesc(String cadreDesc) {
        this.cadreDesc = cadreDesc;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }
    
}
