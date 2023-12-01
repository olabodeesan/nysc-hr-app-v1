/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import app.dto.AppUserDTO;
import dto.employees.EmployeeDTO;
import dto.employees.RetirementDatesDTO;
import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 *
 * @author Ironhide
 */
public class ViewHelper implements Serializable {

    public static void addMessage(FacesContext context, String clientId, String summary, String detail, FacesMessage.Severity severity) {
        context.addMessage(clientId, new FacesMessage(severity, summary, detail));
    }

    public static void addErrorMessage(FacesContext context, String clientId, String summary, String detail) {
        addMessage(context, clientId, summary, detail, FacesMessage.SEVERITY_ERROR);
    }

    public static void addWarningMessage(FacesContext context, String clientId, String summary, String detail) {
        addMessage(context, clientId, summary, detail, FacesMessage.SEVERITY_WARN);
    }

    public static void addInfoMessage(FacesContext context, String clientId, String summary, String detail) {
        addMessage(context, clientId, summary, detail, FacesMessage.SEVERITY_INFO);
    }

    public static void clearMessages(FacesContext context) {
        Iterator<FacesMessage> msgIterator = context.getMessages();
        while (msgIterator.hasNext()) {
            msgIterator.next();
            msgIterator.remove();
        }
    }

    public AppUserDTO getAppUser() {
        return (AppUserDTO) getSessionAttribute(AppConstants.SESSION_SCOPE_CURRENT_USER);
    }

    public String getStaffEmail() {
        AppUserDTO staff = getAppUser();
        String email = staff.getEmail();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<").append(email).append(">");

        String result = strBuilder.toString();
        return result;
    }

    public void redirectTo(String url) {
        FacesContext context = FacesContext.getCurrentInstance();
        NavigationHandler nav = context.getApplication().getNavigationHandler();
        nav.handleNavigation(context, null, url);
        context.renderResponse();
    }

    public Calendar convertStringToCalendar(String dateStr, String pattern) {
        Calendar calendar = null;
        try {
            SimpleDateFormat curFormater = new SimpleDateFormat(pattern);
            Date dateObj = curFormater.parse(dateStr);
            calendar = Calendar.getInstance();
            calendar.setTime(dateObj);
        } catch (Exception e) {
            calendar = null;
        }
        return calendar;
    }

    public String convertCalendarToString(Calendar calendar, String pattern) {
        String result = null;
        try {
            SimpleDateFormat curFormater = new SimpleDateFormat(pattern);
            Date dateObj = calendar.getTime();
            result = curFormater.format(dateObj);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public RetirementDatesDTO calculateRetirementDates(EmployeeDTO employeeDto) {
        RetirementDatesDTO result = new RetirementDatesDTO();
        result.setOutcome(false);

        if (employeeDto != null) {

            Calendar dateOfFirstApp = convertStringToCalendar(employeeDto.getDateOfFirstAppointment(), "dd/MM/yyyy");
            Calendar dateOfBirth = convertStringToCalendar(employeeDto.getDateOfBirth(), "dd/MM/yyyy");

            if (dateOfFirstApp != null && dateOfBirth != null) {

                dateOfFirstApp.add(Calendar.YEAR, 35);
                dateOfBirth.add(Calendar.YEAR, 60);

                Calendar dateToRetireByLengthOfStay = Calendar.getInstance();
                dateToRetireByLengthOfStay.setTime(dateOfFirstApp.getTime());

                Calendar dateToRetireByAge = Calendar.getInstance();
                dateToRetireByAge.setTime(dateOfBirth.getTime());

                Calendar dateDueForRetirement = null;
                if (dateToRetireByLengthOfStay.before(dateToRetireByAge)) {
                    dateDueForRetirement = dateToRetireByLengthOfStay;
                } else {
                    dateDueForRetirement = dateToRetireByAge;
                }

                String str_dateRetireByLenthOfStay = convertCalendarToString(dateToRetireByLengthOfStay, "dd/MM/yyyy");
                String str_dateRetireByAge = convertCalendarToString(dateToRetireByAge, "dd/MM/yyyy");
                String str_dateDueForRetirement = convertCalendarToString(dateDueForRetirement, "dd/MM/yyyy");

                if (!StringUtils.isBlank(str_dateRetireByLenthOfStay) && !StringUtils.isBlank(str_dateRetireByAge)
                        && !StringUtils.isBlank(str_dateDueForRetirement)) {

                    result.setDateToRetireBasedOnLengthOfStay(str_dateRetireByLenthOfStay);
                    result.setDateToRetireBasedOnAge(str_dateRetireByAge);
                    result.setDateDueForRetirement(str_dateDueForRetirement);

                    result.setOutcome(true);

                } else {
                    result.setOutcome(false);
                }

            } else {
                result.setOutcome(false);
            }

        } else {

        }

        return result;
    }

    public Date addBusinessDays(Date startDate, int duration) {
        Date result = null;

        if (startDate != null) {
            Calendar now = Calendar.getInstance();
            now.setTime(startDate);
            
            now.add(Calendar.DAY_OF_MONTH, duration);
            
            result = now.getTime();
        }

        return result;
    }

    public String getDateAsYMDWithDashString(Date dateObj) {
        String result = null;
        if (dateObj != null) {
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                result = formatter.format(dateObj);
            } catch (Exception exc) {
            }
        }
        return result;
    }

    public String getDateAsDMYWithSlashString(Date dateObj) {
        String result = null;
        if (dateObj != null) {
            try {
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                result = formatter.format(dateObj);
            } catch (Exception exc) {
            }
        }
        return result;
    }

    public static String fixSqlFieldValue(String value) {
        if (value == null) {
            return null;
        }
        int length = value.length();
        StringBuilder fixedValue = new StringBuilder((int) (length * 1.1));
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c == '\'') {
                fixedValue.append("''");
            } else {
                fixedValue.append(c);
            }
        }
        return fixedValue.toString();
    }

    public String getContentDir() {
        String content_dir = "\blah";
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (SystemUtils.IS_OS_UNIX) {
                content_dir = ctx.getExternalContext().getInitParameter("UNIX_CONTENT_DIR");
            } else if (SystemUtils.IS_OS_WINDOWS) {
                content_dir = ctx.getExternalContext().getInitParameter("WIN_CONTENT_DIR");
            }
        } catch (Exception exc) {
        }
        return content_dir;
    }

    public String getPersonnelDocsDir() {
        String content_dir = "\blah";
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (SystemUtils.IS_OS_UNIX) {
                content_dir = ctx.getExternalContext().getInitParameter("UNIX_PERSONNEL_DOCS_DIR");
            } else if (SystemUtils.IS_OS_WINDOWS) {
                content_dir = ctx.getExternalContext().getInitParameter("WIN_PERSONNEL_DOCS_DIR");
            }
        } catch (Exception exc) {
        }
        return content_dir;
    }

    public String getRelativeDocsDir() {
        String content_dir = "\blah";
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (SystemUtils.IS_OS_UNIX) {
                content_dir = ctx.getExternalContext().getInitParameter("UNIX_RELATIVE_DOCS_DIR");
            } else if (SystemUtils.IS_OS_WINDOWS) {
                content_dir = ctx.getExternalContext().getInitParameter("WIN_RELATIVE_DOCS_DIR");
            }
        } catch (Exception exc) {
        }
        return content_dir;
    }

    public String getRequestParamValue(String param_name) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getRequestParameterMap().get(param_name);
    }

    public void putSessionAttribute(String attribute, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put(attribute, value);
    }

    public Object getSessionAttribute(String attribute) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get(attribute);
    }

    public void removeSessionAttribute(String attribute) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove(attribute);
    }

    public void putApplicationAttribute(String attribute, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getApplicationMap().put(attribute, value);
    }

    public Object getApplicationAttribute(String attribute) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getApplicationMap().get(attribute);
    }

    public void removeApplicationAttribute(String attribute) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getApplicationMap().remove(attribute);
    }

    public void logoffUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove(AppConstants.SESSION_SCOPE_CURRENT_USER);
        redirectTo("/login.xhtml?faces-redirect=true");
    }
    
    public long computeLengthOfStay(String currentDate, String previousDate) {
        long result = 0;
        try {
            //dd-MM-yyyy
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDatePosted = sdf.parse(currentDate);
            Date previousLocationDate = sdf.parse(previousDate);

            DateTime dt1 = new DateTime(previousLocationDate);
            DateTime dt2 = new DateTime(currentDatePosted);

//  result = Math.abs(Days.daysBetween(dt2, dt1).getDays());
            result = Days.daysBetween(dt1, dt2).getDays();

        } catch (Exception exc) {

        }

        return result;
    }

    public String calculatLenghtOfStay(String todayDate, String datePosted, String format) {

        String result = "";

        if (!StringUtils.isBlank(todayDate) && !StringUtils.isBlank(datePosted)) {
            String[] todayDateArr = todayDate.split("-");
            String[] datePostedArr = datePosted.split("-");

            if (todayDateArr != null && todayDateArr.length == 3 && datePostedArr != null && datePostedArr.length == 3) {

                int todayyear = 0;
                int todaymonth = 0;
                int todayday = 0;
                int postedyear = 0;
                int postedmonth = 0;
                int postedday = 0;

                if (StringUtils.equalsIgnoreCase(format, "ymd")) {
                    todayyear = Integer.parseInt(todayDateArr[0]);
                    todaymonth = Integer.parseInt(todayDateArr[1]);
                    todayday = Integer.parseInt(todayDateArr[2]);
                    //
                    postedyear = Integer.parseInt(datePostedArr[0]);
                    postedmonth = Integer.parseInt(datePostedArr[1]);
                    postedday = Integer.parseInt(datePostedArr[2]);
                } else if (StringUtils.equalsIgnoreCase(format, "dmy")) {
                    todayyear = Integer.parseInt(todayDateArr[2]);
                    todaymonth = Integer.parseInt(todayDateArr[1]);
                    todayday = Integer.parseInt(todayDateArr[0]);
                    //
                    postedyear = Integer.parseInt(datePostedArr[2]);
                    postedmonth = Integer.parseInt(datePostedArr[1]);
                    postedday = Integer.parseInt(datePostedArr[0]);
                }

                LocalDate postedLocalDate = new LocalDate(postedyear, postedmonth, postedday);
                LocalDate todayLocalDate = new LocalDate(todayyear, todaymonth, todayday);

                Period period = new Period(postedLocalDate, todayLocalDate, PeriodType.yearMonthDay());

                int ageYears = period.getYears();

                //get age string               
                StringBuilder strBuilder = new StringBuilder();
//                strBuilder.append(Math.abs(period.getYears())).append("yrs, ")
//                        .append(Math.abs(period.getMonths())).append("mth(s), ")
//                        .append(Math.abs(period.getDays())).append("day(s)");
                
                strBuilder.append(period.getYears()).append("yrs, ")
                        .append(period.getMonths()).append("mth(s), ")
                        .append(period.getDays()).append("day(s)");

                String ageStr = strBuilder.toString();

                result = ageStr;

            }
        }

        return result;
    }
}
