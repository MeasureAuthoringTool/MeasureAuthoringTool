package mat.shared;


// TODO: Auto-generated Javadoc
/**
 * The Class ConstantMessages.
 */
public  class ConstantMessages {
	
	//Logging constants
	/** The Constant SERVER_LOG. */
	public static final int SERVER_LOG =0;
	
	/** The Constant DB_LOG. */
	public static final int DB_LOG =1;
	
	//Server side errors for saving measure metadata
	/** The Constant ID_NOT_UNIQUE. */
	public static final int ID_NOT_UNIQUE = 1;
	
	/** The Constant REACHED_MAXIMUM_VERSION. */
	public static final int REACHED_MAXIMUM_VERSION = 2;
	
	/** The Constant REACHED_MAXIMUM_MAJOR_VERSION. */
	public static final int REACHED_MAXIMUM_MAJOR_VERSION = 3;
	
	/** The Constant REACHED_MAXIMUM_MINOR_VERSION. */
	public static final int REACHED_MAXIMUM_MINOR_VERSION = 4;
	
	/** The Constant INVALID_VALUE_SET_DATE. */
	public static final int INVALID_VALUE_SET_DATE = 5;
	
	/** The Constant INVALID_VALUE_SET_DATE. */
	public static final int INVALID_DATA = 6;
	
	/** The Constant FILE_NOT_SELECTED. */
	public static final String FILE_NOT_SELECTED ="Please Select a File.";
	
	/** The Constant EXCEL_FILE_TYPE. */
	public static final String EXCEL_FILE_TYPE = "Please select a file with an Excel file type (.xls or .xlsx)";
	
	/** The Constant EMPTY_FILE_ERROR. */
	public static final String EMPTY_FILE_ERROR = "Import failed. File is empty.";
	
	/** The Constant INCOMPLETE_ROW_ERROR. */
	public static final String INCOMPLETE_ROW_ERROR = "Import failed. One or more rows is missing a Code or a Descriptor.";
	
	/** The Constant SYSTEM_ERROR. */
	public static final String SYSTEM_ERROR = "Import failed due to system error. Please try again.";
	
	/** The Constant DUPLICATE_ERROR. */
	public static final String DUPLICATE_ERROR = "Import failed. One or more duplicate codes exist in file. Please remove then try again.";
	
	/** The Constant INVALID_ROW_INPUT_ERROR. */
	public static final String INVALID_ROW_INPUT_ERROR = "Import failed. Invalid input was found at row ";
	
	/** The Constant INVALID_TEMPLATE. */
	public static final String INVALID_TEMPLATE = "Import failed. Invalid Template.";
	
	/** The Constant DUPLICATE_CODES_MSG. */
	public static final String DUPLICATE_CODES_MSG = "code(s) were identified as duplicates to code(s) already in the code list and were ignored upon import.";
	
	/** The Constant TWO_WHITE_SPACES. */
	public static final String TWO_WHITE_SPACES = "&nbsp;&nbsp;";
	
	/** The Constant CONTINUOUS_VARIABLE_SCORING. */
	public static final String CONTINUOUS_VARIABLE_SCORING ="Continuous Variable";
	
	/** The Constant PROPORTION_SCORING. */
	public static final String PROPORTION_SCORING = "Proportion";
	
	/** The Constant RATIO_SCORING. */
	public static final String RATIO_SCORING = "Ratio";
	
	/** The Constant COHORT_SCORING. */
	public static final String COHORT_SCORING = "Cohort";
	
	//From MAT_APP.CONTEXT
	/** The Constant POPULATION_CONTEXT_ID. */
	public static final String POPULATION_CONTEXT_ID= "initialPopulation";
	
	/** The Constant NUMERATOR_CONTEXT_ID. */
	public static final String NUMERATOR_CONTEXT_ID = "numerator";
	
	/** The Constant NUMERATOR_EXCLUSIONS_CONTEXT_ID. */
	public static final String NUMERATOR_EXCLUSIONS_CONTEXT_ID = "numeratorExclusions";
	
	/** The Constant DENOMINATOR_CONTEXT_ID. */
	public static final String DENOMINATOR_CONTEXT_ID = "denominator";
	
	/** The Constant DENOMINATOR_EXCLUSIONS_CONTEXT_ID. */
	public static final String DENOMINATOR_EXCLUSIONS_CONTEXT_ID = "denominatorExclusions";
	
	/** The Constant DENOMINATOR_EXCEPTIONS_CONTEXT_ID. */
	public static final String DENOMINATOR_EXCEPTIONS_CONTEXT_ID = "denominatorExceptions";
	
	/** The Constant MEASURE_POPULATION_CONTEXT_ID. */
	public static final String MEASURE_POPULATION_CONTEXT_ID = "measurePopulation";
	
	/** The Constant MEASURE_OBSERVATION_CONTEXT_ID. */
	public static final String MEASURE_OBSERVATION_CONTEXT_ID = "measureObservation";
	//US 597
	/** The Constant STRATIFICATION_CONTEXT_ID. */
	public static final String STRATIFICATION_CONTEXT_ID = "stratification";
	
	/** The Constant STRATIFIER_CONTEXT_ID. */
	public static final String STRATIFIER_CONTEXT_ID = "stratum";
	
	/** The Constant USER_DEFINED_CONTEXT_ID. */
	public static final String USER_DEFINED_CONTEXT_ID = "10";
	
	/** The Constant MEASURE_PHRASE_CONTEXT_ID. */
	public static final String MEASURE_PHRASE_CONTEXT_ID ="11";
	
	/** The Constant CONTEXT_ID_COUNT. */
	public static final int CONTEXT_ID_COUNT = 11;
	
	/** The Constant POPULATION_CONTEXT_DESC. */
	public static final String POPULATION_CONTEXT_DESC= "Population";
	
	/** The Constant NUMERATOR_CONTEXT_DESC. */
	public static final String NUMERATOR_CONTEXT_DESC = "Numerator";
	
	/** The Constant NUMERATOR_EXCLUSIONS_CONTEXT_DESC. */
	public static final String NUMERATOR_EXCLUSIONS_CONTEXT_DESC = "Numerator Exclusions";
	
	/** The Constant DENOMINATOR_CONTEXT_DESC. */
	public static final String DENOMINATOR_CONTEXT_DESC = "Denominator";
	
	/** The Constant DENOMINATOR_EXCLUSIONS_CONTEXT_DESC. */
	public static final String DENOMINATOR_EXCLUSIONS_CONTEXT_DESC = "Denominator Exclusions";
	
	/** The Constant DENOMINATOR_EXCEPTIONS_CONTEXT_DESC. */
	public static final String DENOMINATOR_EXCEPTIONS_CONTEXT_DESC = "Denominator Exceptions";
	
	/** The Constant MEASURE_POPULATION_CONTEXT_DESC. */
	public static final String MEASURE_POPULATION_CONTEXT_DESC = "Measure Population";
	
	/** The Constant MEASURE_OBSERVATION_CONTEXT_DESC. */
	public static final String MEASURE_OBSERVATION_CONTEXT_DESC = "Measure Observation";
	//US 597
	/** The Constant STRAT_CONTEXT_DESC. */
	public static final String STRAT_CONTEXT_DESC = "Stratification";
	
	/** The Constant USER_DEFINED_CONTEXT_DESC. */
	public static final String USER_DEFINED_CONTEXT_DESC = "User-defined";
	
	/** The Constant MEASURE_PHRASE_CONTEXT_DESC. */
	public static final String MEASURE_PHRASE_CONTEXT_DESC ="Measure Phrase";
	
	/** The Constant POP_TAB. */
	public static final String POP_TAB = "Pop";
	
	/** The Constant POP_TAB_EXPANDED. */
	public static final String POP_TAB_EXPANDED = "Population";
	
	/** The Constant NUM_TAB. */
	public static final String NUM_TAB = "Num";
	
	/** The Constant NUM_TAB_EXPANDED. */
	public static final String NUM_TAB_EXPANDED = "Numerator";
	
	/** The Constant NUM_EX_TAB. */
	public static final String NUM_EX_TAB = "N Excl";
	
	/** The Constant NUM_EX_TAB_EXPANDED. */
	public static final String NUM_EX_TAB_EXPANDED = "Numerator Exclusions";
	
	/** The Constant DEN_TAB. */
	public static final String DEN_TAB = "Den";
	
	/** The Constant DEN_TAB_EXPANDED. */
	public static final String DEN_TAB_EXPANDED = "Denominator";
	
	/** The Constant EXCL_TAB. */
	public static final String EXCL_TAB = "D Excl";
	
	/** The Constant EXCL_TAB_EXPANDED. */
	public static final String EXCL_TAB_EXPANDED = "Denominator Exclusions";
	
	/** The Constant EXCEP_TAB. */
	public static final String EXCEP_TAB = "D Excep";
	
	/** The Constant EXCEP_TAB_EXPANDED. */
	public static final String EXCEP_TAB_EXPANDED = "Denominator Exceptions";
	
	/** The Constant MEASURE_POP_TAB. */
	public static final String MEASURE_POP_TAB = "Meas Pop";
	
	/** The Constant MEASURE_POP_TAB_EXPANDED. */
	public static final String MEASURE_POP_TAB_EXPANDED = "Measure Population";
	
	/** The Constant MEASURE_OBS_TAB. */
	public static final String MEASURE_OBS_TAB = "Meas Obs";
	
	/** The Constant MEASURE_OBS_TAB_EXPANDED. */
	public static final String MEASURE_OBS_TAB_EXPANDED = "Measure Observation";
	
	/** The Constant STRAT_TAB. */
	public static final String STRAT_TAB = "Strat";
	
	/** The Constant STRAT_TAB_EXPANDED. */
	public static final String STRAT_TAB_EXPANDED = "Stratification";
	
	/** The Constant USER_DEFINED_TAB. */
	public static final String USER_DEFINED_TAB = "User-Defined";
	
	/** The Constant USER_DEFINED_TAB_EXPANDED. */
	public static final String USER_DEFINED_TAB_EXPANDED ="User-Defined";
	
	/** The Constant MEASURE_PHRASE_TAB. */
	public static final String MEASURE_PHRASE_TAB =  "Measure Phrase";
	
	/** The Constant MEASURE_PHRASE_TAB_EXPANDED. */
	public static final String MEASURE_PHRASE_TAB_EXPANDED = "Measure Phrase";
	
	/** The Constant ATTRIBUTE. */
	public static final String ATTRIBUTE = "Attribute";
	
	/** The Constant MEASUREMENT_PERIOD. */
	public static final String MEASUREMENT_PERIOD = "Measurement Period";
	
	/** The Constant MEASUREMENT_START_DATE. */
	public static final String MEASUREMENT_START_DATE = "Measurement Start Date";
	
	/** The Constant MEASUREMENT_END_DATE. */
	public static final String MEASUREMENT_END_DATE = "Measurement End Date";
	
	/** The Constant MEASUREMENT_TIMING. */
	public static final String MEASUREMENT_TIMING = "Measure Timing";
	
	//data type
	/** The Constant TIMING_ELEMENT. */
	public static final String TIMING_ELEMENT = "Timing Element";
	
	/** The Constant PATIENT_CHARACTERISTIC. */
	public static final String PATIENT_CHARACTERISTIC = "Patient Characteristic";
	
	/** The Constant PATIENT_CHARACTERISTIC_RACE. */
	public static final String PATIENT_CHARACTERISTIC_RACE = "Patient Characteristic Race";
	
	/** The Constant PATIENT_CHARACTERISTIC_ETHNICITY. */
	public static final String PATIENT_CHARACTERISTIC_ETHNICITY = "Patient Characteristic Ethnicity";
	
	/** The Constant PATIENT_CHARACTERISTIC_PAYER. */
	public static final String PATIENT_CHARACTERISTIC_PAYER = "Patient Characteristic Payer";
	
	/** The Constant PATIENT_CHARACTERISTIC_GENDER. */
	public static final String PATIENT_CHARACTERISTIC_GENDER = "Patient Characteristic Sex";
	
	/** The Constant ADMINISTRATIVE_GENDER_MALE. */
	public static final String ADMINISTRATIVE_GENDER_MALE = "Administrative Gender Male";
	
	/** The Constant ADMINISTRATIVE_GENDER_FEMALE. */
	public static final String ADMINISTRATIVE_GENDER_FEMALE = "Administrative Gender Female";
	
	/** The Constant ADMINISTRATIVE_GENDER_UNDIFFERENTIATED. */
	public static final String ADMINISTRATIVE_GENDER_UNDIFFERENTIATED = "Administrative Gender Undifferentiated";
	
	/** The Constant BIRTH_DATE. */
	public static final String BIRTH_DATE = "birth date";
	
	/** The Constant GENDER_MALE_OID. */
	public static final String GENDER_MALE_OID = "2.16.840.1.113883.3.560.100.1";
	
	/** The Constant GENDER_FEMALE_OID. */
	public static final String GENDER_FEMALE_OID = "2.16.840.1.113883.3.560.100.2";
	
	/** The Constant GENDER_UNDIFFERENTIATED_OID. */
	public static final String GENDER_UNDIFFERENTIATED_OID = "2.16.840.1.113883.3.560.100.3";
	
	/** The Constant BIRTH_DATE_OID. */
	public static final String BIRTH_DATE_OID = "2.16.840.1.113883.3.560.100.4";
	
	/** The Constant GROUPING_CODE_SYSTEM. */
	public static final String GROUPING_CODE_SYSTEM =  "Grouping";
	
	/** The Constant HL7_ADMINGENDER_CODE_SYSTEM. */
	public static final String HL7_ADMINGENDER_CODE_SYSTEM = "Administrative Sex";
	
	/** The Constant LOINC_CODE_SYSTEM. */
	public static final String LOINC_CODE_SYSTEM = "LOINC";
	//New Suppliemental CodeSystems
	/** The Constant CDC_CODE_SYSTEM. */
	public static final String CDC_CODE_SYSTEM = "CDC";
	
	/** The Constant SOURCE_OF_PAYMENT. */
	public static final String SOURCE_OF_PAYMENT = "Source of Payment Typology";
	
	//MODULES
	/** The Constant MAT_MODULE. */
	public static final String MAT_MODULE = "MAT";
	
	/** The Constant LOGIN_MODULE. */
	public static final String LOGIN_MODULE = "LOGIN";
	
	//UNITS
	/** The Constant UNIT_FUNCTION. */
	public static final String UNIT_FUNCTION = "Function";
	
	/** The Constant UNIT_COMPARISON. */
	public static final String UNIT_COMPARISON = "Comparison";
	
	/** The Constant UNIT_TMP_COMPARISON. */
	public static final String UNIT_TMP_COMPARISON = "TemporalComparison";
	
	/** The Constant UNIT_ATTRIBUTE. */
	public static final String UNIT_ATTRIBUTE = "Attribute";
	
	/** The Constant COMPARISON_UNIT_DEFAULT. */
	public static final String COMPARISON_UNIT_DEFAULT = "seconds";
	
	
	//PASSWORD
	/** The Constant PASSWORD. */
	public static final String PASSWORD = "password";
	
	/** The Constant PASSWORD_EXPIRE_DATE. */
	public static final String PASSWORD_EXPIRE_DATE = "passwordExpireDate";
	
	/** The Constant WEB_LINK. */
	public static final String WEB_LINK = "link";
	
	/** The Constant LOGINID. */
	public static final String LOGINID = "loginId";
	
	/** The Constant ROOT_PATH. */
	public static final String ROOT_PATH = "/";
	
	//TEMPLATES
	/** The Constant TEMPLATE_WELCOME. */
	public static final String TEMPLATE_WELCOME = "welcomeTemplate.vm";
	
	/** The Constant TEMPLATE_TEMP_PASSWORD. */
	public static final String TEMPLATE_TEMP_PASSWORD = "tempPasswordTemplate.vm";
	
	/** The Constant TEMPLATE_RESET_PASSWORD. */
	public static final String TEMPLATE_RESET_PASSWORD = "resetPasswordTemplate.vm";
	
	/** The Constant TEMPLATE_FORGOT_LOGINID. */
	public static final String TEMPLATE_FORGOT_LOGINID = "forgotLoginIDTemplate.vm";
	
	
	//events
	/** The Constant INSERT. */
	public static final String INSERT = "Insert";
	
	/** The Constant UPDATE. */
	public static final String UPDATE = "Update";
	
	/** The Constant DELETE. */
	public static final String DELETE = "Delete";
	
	/** The Constant EXPORT. */
	public static final String EXPORT = "Export";
	
	/** The Constant USER_COMMENT. */
	public static final String USER_COMMENT = "User Comment";
	
	/** The Constant USER_NOT_FOUND. */
	public static final String USER_NOT_FOUND = "User Not Found";
	
	/** The Constant EMAIL_NOT_FOUND. */
	public static final String EMAIL_NOT_FOUND = "Email Not Found";
	
	/** The Constant TOOLTIP_FOR_OCCURRENCE. */
	public static final String TOOLTIP_FOR_OCCURRENCE = "Select Specific Occurrence if you need to reference a specific occurrence of your element.";
	
	
	/** The Constant CODE_LIST_WS_HISTORY_STATE. */
	public static final int CODE_LIST_WS_HISTORY_STATE = 1;
	
	/** The Constant MAX_PAGE_DISPLAY. */
	public static final int MAX_PAGE_DISPLAY = 10;
	
	/** The Constant PAGE_DOTS_DISPLAY. */
	public static final int PAGE_DOTS_DISPLAY = 3;
	
	/** The Constant GROUPED_CODE_LIST_CS. */
	public static final String GROUPED_CODE_LIST_CS = "Grouping";
	
	//MeasureLibrary ListBox Options
	/** The Constant DEFAULT_SELECT. */
	public static final String DEFAULT_SELECT = "--Select--";
	
	/** The Constant CREATE_NEW_MEASURE. */
	public static final String CREATE_NEW_MEASURE = "New Measure";
	
	/** The Constant CREATE_NEW_DRAFT. */
	public static final String CREATE_NEW_DRAFT = "Draft of Existing Measure";
	
	/** The Constant CREATE_NEW_VERSION. */
	public static final String CREATE_NEW_VERSION = "Measure Version of Draft";
	
	/** The Constant MAXIMUM_ALLOWED_VERSION. */
	public static final String MAXIMUM_ALLOWED_VERSION = "999.999";
	
	/** The Constant MAXIMUM_ALLOWED_MAJOR_VERSION. */
	public static final String MAXIMUM_ALLOWED_MAJOR_VERSION = "999";
	
	/** The Constant MAXIMUM_ALLOWED_MINOR_VERSION. */
	public static final String MAXIMUM_ALLOWED_MINOR_VERSION = "999";
	
	//Value Set Library ListBox Options
	/** The Constant CREATE_NEW_GROUPED_VALUE_SET. */
	public static final String CREATE_NEW_GROUPED_VALUE_SET = "New Grouped Value Set";
	
	/** The Constant CREATE_NEW_VALUE_SET. */
	public static final String CREATE_NEW_VALUE_SET = "New Value Set";
	
	/** The Constant CREATE_VALUE_SET_DRAFT. */
	public static final String CREATE_VALUE_SET_DRAFT = "Draft of Value Set";
	
	//Value Set Edit Screen
	/** The Constant OID_CAUTION. */
	public static final String OID_CAUTION = "CAUTION: Changing the OID should be avoided unless absolutely necessary. Under most circumstances the OID should only be changed when a correction is needed.";
	
	/** The Constant CATEGORY_MEASUREEL. */
	public static final String CATEGORY_MEASUREEL = "22";
	
	/** The Constant CATEGORY_PROPEL. */
	public static final String CATEGORY_PROPEL = "23";
	
	/** The Constant GENDER. */
	public static final String GENDER = "ONC Administrative Sex";
	
	/** The Constant RACE. */
	public static final String RACE = "Race";
	
	/** The Constant ETHNICITY. */
	public static final String ETHNICITY = "Ethnicity";
	
	/** The Constant PAYER. */
	public static final String PAYER = "Payer";
	
	/** The Constant GENDER_OID. */
	public static final String GENDER_OID = "2.16.840.1.113762.1.4.1";
	
	/** The Constant RACE_OID. */
	public static final String RACE_OID = "2.16.840.1.114222.4.11.836";
	
	/** The Constant ETHNICITY_OID. */
	public static final String ETHNICITY_OID = "2.16.840.1.114222.4.11.837";
	
	/** The Constant PAYER_OID. */
	public static final String PAYER_OID = "2.16.840.1.114222.4.11.3591";
	
	/** The Constant SUPPLEMENTAL_DATA_ELEMENT_OID_ARR. */
	public static final String[] SUPPLEMENTAL_DATA_ELEMENT_OID_ARR = {GENDER_OID,RACE_OID, ETHNICITY_OID, PAYER_OID};
	
	/** The Constant MAIN_TAB_LAYOUT_ID. */
	public static final String MAIN_TAB_LAYOUT_ID = "mainTab";
	
	/** The Constant MEASURE_COMPOSER_TAB. */
	public static final String MEASURE_COMPOSER_TAB = "measureTab";
	
	/** The Constant USER_DEFINED_QDM_OID. */
	public static final String USER_DEFINED_QDM_OID="1.1.1.1";
	
	/** The Constant USER_DEFINED_QDM_NAME. */
	public static final String USER_DEFINED_QDM_NAME="User Defined QDM";
	
	/** The Constant PATIENT_CHARACTERISTIC_BIRTHDATE. */
	public static final String PATIENT_CHARACTERISTIC_BIRTHDATE = "Patient Characteristic Birthdate";
	
	/** The Constant PATIENT_CHARACTERISTIC_EXPIRED. */
	public static final String PATIENT_CHARACTERISTIC_EXPIRED = "Patient Characteristic Expired";
	
	/** The Constant EXPIRED. */
	public static final String EXPIRED = "Expired";
	
	/** The Constant BIRTHDATE. */
	public static final String BIRTHDATE = "Birthdate";
	
	/** The Constant EXPIRED_OID. */
	public static final String EXPIRED_OID = "419099009";
	
	/** The Constant BIRTHDATE. */
	public static final String BIRTHDATE_OID = "21112-8";
	
	
}
