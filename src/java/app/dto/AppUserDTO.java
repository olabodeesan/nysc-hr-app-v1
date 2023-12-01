/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author
 */
public class AppUserDTO implements Serializable {

    private String username;
    private String staffRefNo, staffNo;
    private String password, newPassword, confirmPassword;
    private String surname;
    private String otherNames;
    private String locationId, locationDescription;
    private String departmentId, departmentDescription;
    private String fullName;
    private String phone;
    private String email;
    private String primaryRoleId;
    private String recordStatus;
    private String created,createdBy;
    private String lastMod,lastModBy;
    private String profileType;
    private String personnelFileNo;
    private String personnelProfileSelector;
    private String isSuspended;
    private String isDisciplined;

    private List<String> roleList;

    public AppUserDTO() {
        roleList = new ArrayList<String>();
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getStaffRefNo() {
        return staffRefNo;
    }

    public void setStaffRefNo(String staffRefNo) {
        this.staffRefNo = staffRefNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentDescription() {
        return departmentDescription;
    }

    public void setDepartmentDescription(String departmentDescription) {
        this.departmentDescription = departmentDescription;
    }

    public String getPrimaryRoleId() {
        return primaryRoleId;
    }

    public void setPrimaryRoleId(String primaryRoleId) {
        this.primaryRoleId = primaryRoleId;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getPersonnelFileNo() {
        return personnelFileNo;
    }

    public void setPersonnelFileNo(String personnelFileNo) {
        this.personnelFileNo = personnelFileNo;
    }

    public String getPersonnelProfileSelector() {
        return personnelProfileSelector;
    }

    public void setPersonnelProfileSelector(String personnelProfileSelector) {
        this.personnelProfileSelector = personnelProfileSelector;
    }

    public String getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(String isSuspended) {
        this.isSuspended = isSuspended;
    }

    public String getIsDisciplined() {
        return isDisciplined;
    }

    public void setIsDisciplined(String isDisciplined) {
        this.isDisciplined = isDisciplined;
    }
    
    

}
