/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.welfare;

import dto.welfare.RetireesDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.context.RequestContext;
import service.welfare.RetireeService;
import view.utils.ViewHelper;

/**
 *
 * @author Gainsolutions
 */
public class RetiringOfficerMBean implements Serializable{

    private List<RetireesDTO> retireeRecordList;
    private ViewHelper viewHelper;
    private RetireeService service;

    public RetiringOfficerMBean() {
        service = new RetireeService();
        viewHelper = new ViewHelper();
        displayRetireeInFewMonthTime();
    }

    private void displayRetireeInFewMonthTime() {
        RequestContext reqContext=RequestContext.getCurrentInstance();
        
        try {
            retireeRecordList = service.getRetireesIn3MonthTime();
//            if(retireeRecordList!=null){
//                reqContext.update("printPanel");
//            }
        } catch (Exception ex) {

        }
    }

    public List<RetireesDTO> getRetireeRecordList() {
        if (retireeRecordList == null) {
            retireeRecordList = new ArrayList<>();
        }
        return retireeRecordList;
    }

    public void setRetireeRecordList(List<RetireesDTO> retireeRecordList) {
        this.retireeRecordList = retireeRecordList;
    }

}
