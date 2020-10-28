package mat.shared;

/**
 * The Class ConstantMessages.
 */
public class ConstantMessages {

    public static final int DB_LOG = 1;

    public static final int ID_NOT_UNIQUE = 1;

    public static final int REACHED_MAXIMUM_VERSION = 2;

    public static final int REACHED_MAXIMUM_MAJOR_VERSION = 3;

    public static final int REACHED_MAXIMUM_MINOR_VERSION = 4;

    public static final int INVALID_VALUE_SET_DATE = 5;

    public static final int INVALID_DATA = 6;

    public static final int INVALID_CQL_DATA = 7;

    public static final int INVALID_CQL_LIBRARIES = 8;

    public static final int DESCRIPTION_REQUIRED = 14;

    public static final int PUBLISHER_REQUIRED = 15;

    public static final int MEASURE_NAME_INVALID = 16;

    public static final String INCOMPLETE_ROW_ERROR = "Import failed. One or more rows is missing a Code or a Descriptor.";

    public static final String CONTINUOUS_VARIABLE_SCORING = "Continuous Variable";

    public static final String PROPORTION_SCORING = "Proportion";

    public static final String RATIO_SCORING = "Ratio";

    public static final String COHORT_SCORING = "Cohort";

    public static final String POPULATION_CONTEXT_ID = "initialPopulation";

    public static final String NUMERATOR_CONTEXT_ID = "numerator";

    public static final String NUMERATOR_EXCLUSIONS_CONTEXT_ID = "numeratorExclusions";

    public static final String DENOMINATOR_CONTEXT_ID = "denominator";

    public static final String DENOMINATOR_EXCLUSIONS_CONTEXT_ID = "denominatorExclusions";

    public static final String DENOMINATOR_EXCEPTIONS_CONTEXT_ID = "denominatorExceptions";

    public static final String MEASURE_POPULATION_CONTEXT_ID = "measurePopulation";

    public static final String MEASURE_OBSERVATION_CONTEXT_ID = "measureObservation";

    public static final String USER_DEFINED_CONTEXT_DESC = "User-defined";

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

    public static final String USER_DEFINED_TAB_EXPANDED = "User-Defined";

    public static final String MEASURE_PHRASE_TAB = "Measure Phrase";

    public static final String MEASURE_PHRASE_TAB_EXPANDED = "Measure Phrase";

    public static final String ATTRIBUTE = "Attribute";

    public static final String TIMING_ELEMENT = "Timing Element";

    public static final String PATIENT_CHARACTERISTIC_RACE = "Patient Characteristic Race";

    public static final String PATIENT_CHARACTERISTIC_ETHNICITY = "Patient Characteristic Ethnicity";

    public static final String PATIENT_CHARACTERISTIC_PAYER = "Patient Characteristic Payer";

    public static final String PATIENT_CHARACTERISTIC_GENDER = "Patient Characteristic Sex";

    public static final String GROUPING_CODE_SYSTEM = "Grouping";

    public static final String HL7_ADMINGENDER_CODE_SYSTEM = "Administrative Sex";

    public static final String CDC_CODE_SYSTEM = "CDC";

    public static final String SOURCE_OF_PAYMENT = "Source of Payment Typology";

    public static final String MAT_MODULE = "MAT";

    public static final String LOGIN_MODULE = "LOGIN";

    public static final String HARP_SUPPORT_MODULE = "HARP SUPPORT";

    public static final String PASSWORD = "password";

    public static final String PASSWORD_EXPIRE_DATE = "passwordExpireDate";

    public static final String LOGINID = "loginId";

    public static final String HARPID = "harpId";

    public static final String USER_EMAIL = "userEmail";

    public static final String URL = "url";

    public static final String INSERT = "Insert";

    public static final String UPDATE = "Update";

    public static final String DELETE = "Delete";

    public static final String USER_NOT_FOUND = "User Not Found";

    public static final String EMAIL_NOT_FOUND = "Email Not Found";

    public static final String TOOLTIP_FOR_OCCURRENCE = "Select Specific Occurrence if you need to reference a specific occurrence of your element.";

    public static final String DEFAULT_SELECT = "--Select--";

    public static final String CREATE_NEW_MEASURE = "New Measure";

    public static final String CREATE_NEW_CQL = "New Library";

    public static final String MAXIMUM_ALLOWED_VERSION = "999.999";

    public static final String MAXIMUM_ALLOWED_MAJOR_VERSION = "999";

    public static final String MAXIMUM_ALLOWED_MINOR_VERSION = "999";

    public static final String GENDER_OID = "2.16.840.1.113762.1.4.1";

    public static final String RACE_OID = "2.16.840.1.114222.4.11.836";

    public static final String ETHNICITY_OID = "2.16.840.1.114222.4.11.837";

    public static final String PAYER_OID = "2.16.840.1.114222.4.11.3591";

    public static final String MAIN_TAB_LAYOUT_ID = "mainTab";

    public static final String MEASURE_COMPOSER_TAB = "measureTab";

    public static final String CQL_COMPOSER_TAB = "cqlTab";

    public static final String USER_DEFINED_QDM_OID = "1.1.1.1";

    public static final String USER_DEFINED_QDM_NAME = "User Defined QDM";

    public static final String PATIENT_CHARACTERISTIC_BIRTHDATE = "Patient Characteristic Birthdate";

    public static final String PATIENT_CHARACTERISTIC_EXPIRED = "Patient Characteristic Expired";

    public static final String DEAD = "Dead";

    public static final String BIRTHDATE = "Birthdate";

    public static final String DEAD_OID = "419099009";

    public static final String BIRTHDATE_OID = "21112-8";

    public static final String BIRTHDATE_CODE_SYSTEM_OID = "2.16.840.1.113883.6.1";

    public static final String DEAD_CODE_SYSTEM_OID = "2.16.840.1.113883.6.96";

    public static final String SUPPORT_EMAIL = "supportEmailAddress";

    private ConstantMessages() {
        // Utility class
    }
}
