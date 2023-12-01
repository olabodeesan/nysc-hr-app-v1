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
public class FileRequestDTO implements Serializable{
    private String recordId;
    private String registryFileNo;
    private String registryFileName;
    private String requestingOfficerInfo;
    private String purpose;
    private String requestDate;
    private String requestTime;
    private String returnDate;
    private String returnTime;
    private String inOutStatus;
    private String remarks;
    private String recordStatus;
    private String created;
    private String createdBy;
    private String lastMod;
    private String lastModBy;

    public FileRequestDTO() {
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRegistryFileNo() {
        return registryFileNo;
    }

    public void setRegistryFileNo(String registryFileNo) {
        this.registryFileNo = registryFileNo;
    }

    public String getRegistryFileName() {
        return registryFileName;
    }

    public void setRegistryFileName(String registryFileName) {
        this.registryFileName = registryFileName;
    }

    public String getRequestingOfficerInfo() {
        return requestingOfficerInfo;
    }

    public void setRequestingOfficerInfo(String requestingOfficerInfo) {
        this.requestingOfficerInfo = requestingOfficerInfo;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getInOutStatus() {
        return inOutStatus;
    }

    public void setInOutStatus(String inOutStatus) {
        this.inOutStatus = inOutStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
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
