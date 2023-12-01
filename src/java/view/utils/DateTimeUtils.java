/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package view.utils;

import java.util.Calendar;

/**
 *
 * @author Ironhide
 */
public class DateTimeUtils {

    public static Calendar getCalendar(){
        return Calendar.getInstance();
    }

    public static String getActivityDate() {
        Calendar c = Calendar.getInstance();
        
        String now = "";

        String day = c.get(Calendar.DAY_OF_MONTH) + "";
        String month = (c.get(Calendar.MONTH) + 1) + "";
        String year = c.get(Calendar.YEAR) + "";        

        if(day.length()==1){
            day = "0" + day;
        }
        if(month.length()==1){
            month = "0" + month;
        }

        now = year + "-" + month + "-" + day;
        
        return now;
    }
    
    public static String getTimeStamp() {
        Calendar c = Calendar.getInstance();
        String now = "";

        String day = c.get(Calendar.DAY_OF_MONTH) + "";
        String month = (c.get(Calendar.MONTH) + 1) + "";
        String year = c.get(Calendar.YEAR) + "";        

        if(day.length()==1){
            day = "0" + day;
        }
        if(month.length()==1){
            month = "0" + month;
        }

        now = year + "-" + month + "-" + day + "@" + c.get(Calendar.HOUR) + "-" + c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND);
        
        return now;
    }

    public static int getDay(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(){
        Calendar c = Calendar.getInstance();
        return (c.get(Calendar.MONTH) + 1);
    }

    public static int getYear(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

}
