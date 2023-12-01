/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.utils;

/**
 *
 * @author IronHide
 */
public class AppConstants {

    /**
     * ******** integers ***************
     */
    public static final int QUERY_PAGE_SIZE = 50;
    /**
     * ******** strings ***************
     */
    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";
    public static final String NOT_ACTIVATED = "NOT_ACTIVATED";
    public static final String IS_ACTIVATED = "IS_ACTIVATED";
    public static final String INVALID_ACTIVATION_PARAMS = "INVALID_ACTIVATION_PARAMS";
    public static final String OPEN = "OPEN";
    public static final String CLOSED = "CLOSED";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String ANY = "ANY";
    public static final String NOT_AUTHORIZED = "NOT_AUTHORIZED";
    public static final String TOTAL_RECORDS = "TOTAL_RECORDS";
    public static final String RECORD_LIST = "RECORD_LIST";

    //duplicate values
    public static final String DUPLICATE = "DUPLICATE";
    public static final String DUPLICATE_CODE = "DUPLICATE_CODE";
    public static final String DUPLICATE_NAME = "DUPLICATE_NAME";
    public static final String DUPLICATE_PHONE = "DUPLICATE_PHONE";
    public static final String DUPLICATE_EMAIL = "DUPLICATE_EMAIL";
    public static final String DUPLICATE_SEC_PHONE = "DUPLICATE_SEC_PHONE";
    public static final String DUPLICATE_SEC_EMAIL = "DUPLICATE_SEC_EMAIL";
    public static final String DUPLICATE_CAC_REG_NO = "DUPLICATE_CAC_REG_NO";
    public static final String DUPLICATE_USERNAME = "DUPLICATE_USERNAME";
    public static final String DUPLICATE_RECORD = "DUPLICATE_RECORD";
    public static final String DUPLICATE_ROLE = "DUPLICATE_ROLE";
    public static final String NO_EMOLUMENT = "NO_EMOLUMENT";

    public static final String NOT_LOGGED_IN = "NOT_LOGGED_IN";
    public static final String DIRECTORY_NOT_FOUND = "DIRECTORY_NOT_FOUND";
    public static final String APPROVED = "APPROVED";
    public static final String DECLINED = "DECLINED";
    public static final String SYSADMIN = "SYSADMIN";
    public static final String ERROR = "ERROR";

    //authentication stuffs
    public static final String AUTH_TYPE = "AUTH_TYPE";
    public static final String ACTIVE_DIRECTORY = "ACTIVE_DIRECTORY";
    public static final String DATABASE = "DATABASE";
    public static final String ACTIVE_DIR_DC = "ACTIVE_DIR_DC";
    public static final String ACTIVE_DIR_DC_IP = "ACTIVE_DIR_DC_IP";
    public static final String ACTIVE_DIR_DC_PORT = "ACTIVE_DIR_DC_PORT";

    public static final String[] phone_prefix = new String[]{"080", "081", "070", "071", "090", "091"};

    //session scoped variables
    public static final String SESSION_SCOPE_CURRENT_USER = "CURRENT_USER";

    //application scoped variables
    public static final String APP_SCOPE_CADRE = "APP_SCOPE_CADRE";
    public static final String APP_SCOPE_CONTINENTS = "APP_SCOPE_CONTINENTS";
    public static final String APP_SCOPE_COUNTRIES = "APP_SCOPE_COUNTRIES";
    public static final String APP_SCOPE_DEPARTMENTS = "APP_SCOPE_DEPARTMENTS";
    public static final String APP_SCOPE_POSITION_APPOINTMENTS = "APP_SCOPE_POSITION_APPOINTMENTS";
    public static final String APP_SCOPE_QUALIFICATIONS = "APP_SCOPE_QUALIFICATIONS";

    public static final String APP_SCOPE_MARITAL_STATUS = "APP_SCOPE_MARITAL_STATUS";
    public static final String APP_SCOPE_STATES = "APP_SCOPE_STATES";
    public static final String APP_SCOPE_GRADE_LEVEL = "APP_SCOPE_GRADE_LEVEL";
    public static final String APP_SCOPE_GRADE_STEP = "APP_SCOPE_GRADE_STEP";
    public static final String APP_SCOPE_LOCATIONS = "APP_SCOPE_LOCATIONS";
    public static final String APP_SCOPE_INSTITUTION_TYPES = "APP_SCOPE_INSTITUTION_TYPES";

    public static final String APP_SCOPE_COURSES = "APP_SCOPE_COURSES";
    public static final String APP_SCOPE_RELATIONSHIPS = "APP_SCOPE_RELATIONSHIPS";

    public static final String APP_SCOPE_DISENGAGED_STATUS = "APP_SCOPE_DISENGAGED_STATUS";
    public static final String APP_SCOPE_ACTIVE_STATUS = "APP_SCOPE_ACTIVE_STATUS";
    public static final String APP_SCOPE_SUSPENDED_STATUS = "APP_SCOPE_SUSPENDED_STATUS";

    public static final String APP_SCOPE_TRAINING_CERTIFICATES = "APP_SCOPE_TRAINING_CERTIFICATES";
    public static final String APP_SCOPE_TRAINING_PROPOSAL_APPROVAL = "APP_SCOPE_TRAINING_PROPOSAL_APPROVAL";
    public static final String APP_SCOPE_TRAINING_PROPOSAL_APPROVAL_COMPLETE = "APP_SCOPE_TRAINING_PROPOSAL_APPROVAL_COMPLETE";
    public static final String APP_SCOPE_TRAINING_SPECIALIZATIONS = "APP_SCOPE_TRAINING_SPECIALIZATIONS";
    public static final String APP_SCOPE_TRAINING_TYPES = "APP_SCOPE_TRAINING_TYPES";
    public static final String APP_SCOPE_TRAINING_CONSULTANTS = "APP_SCOPE_TRAINING_CONSULTANTS";

    public static final String APP_SCOPE_LEAVE_APPROVAL = "APP_SCOPE_LEAVE_APPROVAL";

    public static final String APP_SCOPE_GL_ANNUAL_LEAVE_DURATIONS = "APP_SCOPE_GL_ANNUAL_LEAVE_DURATIONS";

    //LOCATION TYPES
    public static final String LOCATION_STATE = "STATE";
    public static final String LOCATION_NDHQ = "NDHQ";

    //
    public static final String FETCH_MODE_RECORD_ID = "RECORD_ID";
    public static final String FETCH_MODE_FILE_NO = "FILE_NO";

    public static final String FETCH_VIEW_UPDATE = "FETCH_VIEW_UPDATE";
    public static final String FETCH_VIEW_ONLY = "FETCH_VIEW_ONLY";

    public static final String DEFAULT_PHOTO_URL = "/images/bluemanmxxl.png";

    //
    public static final String FRESH_ENTRY = "1";
    public static final String TRANSFER_ENTRY = "2";

    //
    public static final String STATUS_DISENGAGED = "DISENGAGED";
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_SUSPENDED = "SUSPENDED";

    //
    public static final String PERSONNEL_ACTIVE = "1";
    public static final String PERSONNEL_RETIRED = "2";

    public static final String PERSONNEL_SUSPENDED = "3";
    public static final String PERSONNEL_DECEASED = "4";
    public static final String PERSONNEL_DISMISSED = "5";
    public static final String PERSONNEL_TERMINATED = "6";

    //exception codes
    public static final String ANNUAL_ROSTER_IN_USE = "ANNUAL_ROSTER_IN_USE";
    public static final String NO_ANNUAL_LEAVE_ROSTER = "NO_ANNUAL_LEAVE_ROSTER";
    public static final String LEAVE_DAYS_TOO_MUCH = "LEAVE_DAYS_TOO_MUCH";
    public static final String ACTIVE_PENDING_EXISTS = "ACTIVE_PENDING_EXISTS";

    //country
    public static final String NIGERIA = "1";
    public static final String NIGERIA_DESC = "Nigeria";
    public static final String LOCAL_TRAINING = "1";
    public static final String FOREIGN_TRAINING = "2";

    //
    public static final String TRAINING_APPROVED = "APPROVED";
    public static final String TRAINING_NOT_APPROVED = "NOT_APPROVED";
    public static final String TRAINING_PENDING_APPROVAL = "PENDING_APPROVAL";

    //
    public static final String REGISTRY_FILE_OUT = "OUT";
    public static final String REGISTRY_FILE_IN = "IN";    //
    public static final String LEAVE_APPROVED = "APPROVED";
    public static final String LEAVE_NOT_APPROVED = "NOT_APPROVED";
    public static final String LEAVE_PENDING_APPROVAL = "PENDING_APPROVAL";

    // Additional Constant Esan
    public static final String LOCATION_ERROR = "LOCATION_ERROR";

    public static final String APP_SCOPE_POSTING_TYPE = "APP_SCOPE_POSTING_TYPE";
    public static final String APP_SCOPE_SPECIAL_APPOINTMENT = "APP_SCOPE_SPECIAL_APPOINTMENT";

    public static final String APP_SCOPE_PENSION_FUND_ADMINISTRATOR = "APP_SCOPE_PENSION_FUND_ADMINISTRATOR";

    public static final String APP_SCOPE_BANKERS = "APP_SCOPE_BANKERS";

    public static final String PAID = "PAID";
    public static final String UNPAID = "UNPAID";
    public static final String PENDING = "PENDING";

    //public Profile Type
    public static final String STATE_STAFF = "STATE_STAFF";
    public static final String SYS_ADMIN = "SYS_ADMIN";
    public static final String DIRECTOR = "DIRECTOR";
    public static final String DD_PLANNING = "DD_PLANNING";
    public static final String DD_CAREER_MGT = "DD_CAREER_MGT";
    public static final String DD_TRAINING = "DD_TRAINING";
    public static final String DD_STAFF_WELFARE = "DD_STAFF_WELFARE";

}
