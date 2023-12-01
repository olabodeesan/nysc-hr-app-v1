/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.employees;

import java.io.Serializable;

/**
 *
 * @author IronHide
 */
public class FetchOptionsDTO implements Serializable{
    
    private boolean fetchAll;
    private boolean fetchCadre;
    private boolean fetchAcadData;
    private boolean fetchAppointments;
    private boolean fetchLocations;
    private boolean fetchNextOfKin;
    private boolean fetchStatus;
    private boolean fetchDocs;
    private boolean fetchDiscipline;
    
    private boolean fetchGeneralTraining;
    private boolean fetchFurtherTraining;
    private boolean fetchAnnualLeave;
    private boolean fetchLeaveApplications;
    
    //from Esan
    private boolean fetchConversion;

    public FetchOptionsDTO() {
    }

    public boolean isFetchAll() {
        return fetchAll;
    }

    public void setFetchAll(boolean fetchAll) {
        this.fetchAll = fetchAll;
    }

    public boolean isFetchCadre() {
        return fetchCadre;
    }

    public void setFetchCadre(boolean fetchCadre) {
        this.fetchCadre = fetchCadre;
    }

    public boolean isFetchAcadData() {
        return fetchAcadData;
    }

    public void setFetchAcadData(boolean fetchAcadData) {
        this.fetchAcadData = fetchAcadData;
    }

    public boolean isFetchAppointments() {
        return fetchAppointments;
    }

    public void setFetchAppointments(boolean fetchAppointments) {
        this.fetchAppointments = fetchAppointments;
    }

    public boolean isFetchLocations() {
        return fetchLocations;
    }

    public void setFetchLocations(boolean fetchLocations) {
        this.fetchLocations = fetchLocations;
    }

    public boolean isFetchNextOfKin() {
        return fetchNextOfKin;
    }

    public void setFetchNextOfKin(boolean fetchNextOfKin) {
        this.fetchNextOfKin = fetchNextOfKin;
    }

    public boolean isFetchStatus() {
        return fetchStatus;
    }

    public void setFetchStatus(boolean fetchStatus) {
        this.fetchStatus = fetchStatus;
    }

    public boolean isFetchDocs() {
        return fetchDocs;
    }

    public void setFetchDocs(boolean fetchDocs) {
        this.fetchDocs = fetchDocs;
    }

    public boolean isFetchDiscipline() {
        return fetchDiscipline;
    }

    public void setFetchDiscipline(boolean fetchDiscipline) {
        this.fetchDiscipline = fetchDiscipline;
    }    

    public boolean isFetchGeneralTraining() {
        return fetchGeneralTraining;
    }

    public void setFetchGeneralTraining(boolean fetchGeneralTraining) {
        this.fetchGeneralTraining = fetchGeneralTraining;
    }

    public boolean isFetchFurtherTraining() {
        return fetchFurtherTraining;
    }

    public void setFetchFurtherTraining(boolean fetchFurtherTraining) {
        this.fetchFurtherTraining = fetchFurtherTraining;
    }

    public boolean isFetchAnnualLeave() {
        return fetchAnnualLeave;
    }

    public void setFetchAnnualLeave(boolean fetchAnnualLeave) {
        this.fetchAnnualLeave = fetchAnnualLeave;
    }

    public boolean isFetchLeaveApplications() {
        return fetchLeaveApplications;
    }

    public void setFetchLeaveApplications(boolean fetchLeaveApplications) {
        this.fetchLeaveApplications = fetchLeaveApplications;
    } 

    public boolean isFetchConversion() {
        return fetchConversion;
    }

    public void setFetchConversion(boolean fetchConversion) {
        this.fetchConversion = fetchConversion;
    }
    
    
}
