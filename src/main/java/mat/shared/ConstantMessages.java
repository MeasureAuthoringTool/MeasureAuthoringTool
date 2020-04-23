package mat.shared;

/**
 * The Class ConstantMessages.
 */
public  class ConstantMessages {
	public static final int SERVER_LOG =0;

	public static final int DB_LOG =1;

	public static final int ID_NOT_UNIQUE = 1;

	public static final int REACHED_MAXIMUM_VERSION = 2;

	public static final int REACHED_MAXIMUM_MAJOR_VERSION = 3;

	public static final int REACHED_MAXIMUM_MINOR_VERSION = 4;

	public static final int INVALID_VALUE_SET_DATE = 5;

	public static final int INVALID_DATA = 6;

	public static final int INVALID_CQL_DATA = 7;

	public static final int INVALID_CQL_LIBRARIES = 8;

	public static final String FILE_NOT_SELECTED ="Please Select a File.";

	public static final String EXCEL_FILE_TYPE = "Please select a file with an Excel file type (.xls or .xlsx)";

	public static final String EMPTY_FILE_ERROR = "Import failed. File is empty.";

	public static final String INCOMPLETE_ROW_ERROR = "Import failed. One or more rows is missing a Code or a Descriptor.";

	public static final String SYSTEM_ERROR = "Import failed due to system error. Please try again.";

	public static final String DUPLICATE_ERROR = "Import failed. One or more duplicate codes exist in file. Please remove then try again.";

	public static final String INVALID_ROW_INPUT_ERROR = "Import failed. Invalid input was found at row ";

	public static final String INVALID_TEMPLATE = "Import failed. Invalid Template.";

	public static final String DUPLICATE_CODES_MSG = "code(s) were identified as duplicates to code(s) already in the code list and were ignored upon import.";

	public static final String TWO_WHITE_SPACES = "&nbsp;&nbsp;";

	public static final String CONTINUOUS_VARIABLE_SCORING ="Continuous Variable";

	public static final String PROPORTION_SCORING = "Proportion";

	public static final String RATIO_SCORING = "Ratio";

	public static final String COHORT_SCORING = "Cohort";

	public static final String POPULATION_CONTEXT_ID= "initialPopulation";

	public static final String NUMERATOR_CONTEXT_ID = "numerator";

	public static final String NUMERATOR_EXCLUSIONS_CONTEXT_ID = "numeratorExclusions";

	public static final String DENOMINATOR_CONTEXT_ID = "denominator";

	public static final String DENOMINATOR_EXCLUSIONS_CONTEXT_ID = "denominatorExclusions";

	public static final String DENOMINATOR_EXCEPTIONS_CONTEXT_ID = "denominatorExceptions";

	public static final String MEASURE_POPULATION_CONTEXT_ID = "measurePopulation";

	public static final String MEASURE_OBSERVATION_CONTEXT_ID = "measureObservation";

	public static final String STRATIFICATION_CONTEXT_ID = "stratification";

	public static final String STRATIFIER_CONTEXT_ID = "stratum";

	public static final String USER_DEFINED_CONTEXT_ID = "10";

	public static final String MEASURE_PHRASE_CONTEXT_ID ="11";

	public static final int CONTEXT_ID_COUNT = 11;

	public static final String POPULATION_CONTEXT_DESC= "Population";

	public static final String NUMERATOR_CONTEXT_DESC = "Numerator";

	public static final String NUMERATOR_EXCLUSIONS_CONTEXT_DESC = "Numerator Exclusions";

	public static final String DENOMINATOR_CONTEXT_DESC = "Denominator";

	public static final String DENOMINATOR_EXCLUSIONS_CONTEXT_DESC = "Denominator Exclusions";

	public static final String DENOMINATOR_EXCEPTIONS_CONTEXT_DESC = "Denominator Exceptions";

	public static final String MEASURE_POPULATION_CONTEXT_DESC = "Measure Population";

	public static final String MEASURE_OBSERVATION_CONTEXT_DESC = "Measure Observation";

	public static final String STRAT_CONTEXT_DESC = "Stratification";

	public static final String USER_DEFINED_CONTEXT_DESC = "User-defined";

	public static final String MEASURE_PHRASE_CONTEXT_DESC ="Measure Phrase";

	public static final String POP_TAB = "Pop";

	public static final String POP_TAB_EXPANDED = "Population";

	public static final String NUM_TAB = "Num";

	public static final String NUM_TAB_EXPANDED = "Numerator";

	public static final String NUM_EX_TAB = "N Excl";

	public static final String NUM_EX_TAB_EXPANDED = "Numerator Exclusions";

	public static final String DEN_TAB = "Den";

	public static final String DEN_TAB_EXPANDED = "Denominator";

	public static final String EXCL_TAB = "D Excl";

	public static final String EXCL_TAB_EXPANDED = "Denominator Exclusions";

	public static final String EXCEP_TAB = "D Excep";

	public static final String EXCEP_TAB_EXPANDED = "Denominator Exceptions";

	public static final String MEASURE_POP_TAB = "Meas Pop";

	public static final String MEASURE_POP_TAB_EXPANDED = "Measure Population";

	public static final String MEASURE_OBS_TAB = "Meas Obs";

	public static final String MEASURE_OBS_TAB_EXPANDED = "Measure Observation";

	public static final String STRAT_TAB = "Strat";

	public static final String STRAT_TAB_EXPANDED = "Stratification";

	public static final String USER_DEFINED_TAB = "User-Defined";

	public static final String USER_DEFINED_TAB_EXPANDED ="User-Defined";

	public static final String MEASURE_PHRASE_TAB =  "Measure Phrase";

	public static final String MEASURE_PHRASE_TAB_EXPANDED = "Measure Phrase";

	public static final String ATTRIBUTE = "Attribute";

	public static final String MEASUREMENT_PERIOD = "Measurement Period";

	public static final String MEASUREMENT_START_DATE = "Measurement Start Date";

	public static final String MEASUREMENT_END_DATE = "Measurement End Date";

	public static final String MEASUREMENT_TIMING = "Measure Timing";

	public static final String TIMING_ELEMENT = "Timing Element";

	public static final String PATIENT_CHARACTERISTIC = "Patient Characteristic";

	public static final String PATIENT_CHARACTERISTIC_RACE = "Patient Characteristic Race";

	public static final String PATIENT_CHARACTERISTIC_ETHNICITY = "Patient Characteristic Ethnicity";

	public static final String PATIENT_CHARACTERISTIC_PAYER = "Patient Characteristic Payer";

	public static final String PATIENT_CHARACTERISTIC_GENDER = "Patient Characteristic Sex";

	public static final String ADMINISTRATIVE_GENDER_MALE = "Administrative Gender Male";

	public static final String ADMINISTRATIVE_GENDER_FEMALE = "Administrative Gender Female";

	public static final String ADMINISTRATIVE_GENDER_UNDIFFERENTIATED = "Administrative Gender Undifferentiated";

	public static final String GENDER_MALE_OID = "2.16.840.1.113883.3.560.100.1";

	public static final String GENDER_FEMALE_OID = "2.16.840.1.113883.3.560.100.2";

	public static final String GENDER_UNDIFFERENTIATED_OID = "2.16.840.1.113883.3.560.100.3";

	public static final String BIRTH_DATE_OID = "2.16.840.1.113883.3.560.100.4";

	public static final String GROUPING_CODE_SYSTEM =  "Grouping";

	public static final String HL7_ADMINGENDER_CODE_SYSTEM = "Administrative Sex";

	public static final String LOINC_CODE_SYSTEM = "LOINC";

	public static final String CDC_CODE_SYSTEM = "CDC";

	public static final String SOURCE_OF_PAYMENT = "Source of Payment Typology";

	public static final String MAT_MODULE = "MAT";

	public static final String LOGIN_MODULE = "LOGIN";

	public static final String BONNIE_MODULE = "BONNIE";

	public static final String UNIT_FUNCTION = "Function";

	public static final String UNIT_COMPARISON = "Comparison";

	public static final String UNIT_TMP_COMPARISON = "TemporalComparison";

	public static final String UNIT_ATTRIBUTE = "Attribute";

	public static final String COMPARISON_UNIT_DEFAULT = "seconds";

    public static final String HARP_SUPPORT_MODULE = "HARP SUPPORT";

    public static final String PASSWORD = "password";

    public static final String PASSWORD_EXPIRE_DATE = "passwordExpireDate";

    public static final String WEB_LINK = "link";

    public static final String LOGINID = "loginId";

    public static final String HARPID = "harpId";

    public static final String USER_EMAIL = "userEmail";

    public static final String URL = "url";

    public static final String ROOT_PATH = "/";

	public static final String INSERT = "Insert";

	public static final String UPDATE = "Update";

	public static final String DELETE = "Delete";

	public static final String EXPORT = "Export";

	public static final String USER_COMMENT = "User Comment";

	public static final String USER_NOT_FOUND = "User Not Found";

	public static final String EMAIL_NOT_FOUND = "Email Not Found";

	public static final String TOOLTIP_FOR_OCCURRENCE = "Select Specific Occurrence if you need to reference a specific occurrence of your element.";

	public static final int CODE_LIST_WS_HISTORY_STATE = 1;

	public static final int MAX_PAGE_DISPLAY = 10;

	public static final int PAGE_DOTS_DISPLAY = 3;

	public static final String GROUPED_CODE_LIST_CS = "Grouping";

	public static final String DEFAULT_SELECT = "--Select--";

	public static final String CREATE_NEW_MEASURE = "New Measure";

	public static final String CREATE_NEW_DRAFT = "Draft of Existing Measure";

	public static final String CREATE_NEW_VERSION = "Measure Version of Draft";

	public static final String CREATE_NEW_CQL = "New Library";

	public static final String CREATE_NEW_CQL_DRAFT = "Draft of Existing Library";

	public static final String CREATE_NEW_CQL_VERSION = "Library Version of Draft";

	public static final String MAXIMUM_ALLOWED_VERSION = "999.999";

	public static final String MAXIMUM_ALLOWED_MAJOR_VERSION = "999";

	public static final String MAXIMUM_ALLOWED_MINOR_VERSION = "999";

	public static final String CREATE_NEW_GROUPED_VALUE_SET = "New Grouped Value Set";

	public static final String CREATE_NEW_VALUE_SET = "New Value Set";

	public static final String CREATE_VALUE_SET_DRAFT = "Draft of Value Set";

	public static final String OID_CAUTION = "CAUTION: Changing the OID should be avoided unless absolutely necessary. Under most circumstances the OID should only be changed when a correction is needed.";

	public static final String CATEGORY_MEASUREEL = "22";

	public static final String CATEGORY_PROPEL = "23";

	public static final String GENDER = "ONC Administrative Sex";

	public static final String RACE = "Race";

	public static final String ETHNICITY = "Ethnicity";

	public static final String PAYER = "Payer";

	public static final String GENDER_OID = "2.16.840.1.113762.1.4.1";

	public static final String RACE_OID = "2.16.840.1.114222.4.11.836";

	public static final String ETHNICITY_OID = "2.16.840.1.114222.4.11.837";

	public static final String PAYER_OID = "2.16.840.1.114222.4.11.3591";

	public static final String[] SUPPLEMENTAL_DATA_ELEMENT_OID_ARR = {GENDER_OID,RACE_OID, ETHNICITY_OID, PAYER_OID};

	public static final String MAIN_TAB_LAYOUT_ID = "mainTab";

	public static final String MEASURE_COMPOSER_TAB = "measureTab";

	public static final String CQL_COMPOSER_TAB = "cqlTab";

	public static final String USER_DEFINED_QDM_OID="1.1.1.1";

	public static final String USER_DEFINED_QDM_NAME="User Defined QDM";

	public static final String PATIENT_CHARACTERISTIC_BIRTHDATE = "Patient Characteristic Birthdate";

	public static final String PATIENT_CHARACTERISTIC_EXPIRED = "Patient Characteristic Expired";

	public static final String DEAD = "Dead";

	public static final String EXPIRED = "Expired";

	public static final String BIRTHDATE = "Birthdate";

	public static final String DEAD_OID = "419099009";

	public static final String BIRTHDATE_OID = "21112-8";

	public static final String BIRTHDATE_CODE_SYSTEM_OID = "2.16.840.1.113883.6.1";

	public static final String DEAD_CODE_SYSTEM_OID = "2.16.840.1.113883.6.96";

	public static int PACKAGE_VALIDATION_FAIL = 30;
}
