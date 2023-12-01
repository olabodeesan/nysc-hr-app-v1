/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.setup.dto.registry;

import java.io.Serializable;

/**
 *
 * @author IronHide
 */
public class RegistryDocumentDTO implements Serializable{
    
    private String recordId;
    private String employeeRecordId;
    private String fileNameNoExt, fileNameWithExt, oldFileNameWithExt;
    private String recordStatus;
    private String remarks;
    private String created, createdBy;
    private String lastMod, lastModBy;

    public RegistryDocumentDTO() {
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getEmployeeRecordId() {
        return employeeRecordId;
    }

    public void setEmployeeRecordId(String employeeRecordId) {
        this.employeeRecordId = employeeRecordId;
    }

    public String getFileNameNoExt() {
        return fileNameNoExt;
    }

    public void setFileNameNoExt(String fileNameNoExt) {
        this.fileNameNoExt = fileNameNoExt;
    }

    public String getOldFileNameWithExt() {
        return oldFileNameWithExt;
    }

    public void setOldFileNameWithExt(String oldFileNameWithExt) {
        this.oldFileNameWithExt = oldFileNameWithExt;
    }

    public String getFileNameWithExt() {
        return fileNameWithExt;
    }

    public void setFileNameWithExt(String fileNameWithExt) {
        this.fileNameWithExt = fileNameWithExt;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
    
    
}
