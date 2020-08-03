package mat.client.shared;

import mat.shared.ConstantMessages;
import mat.shared.StringUtility;

public class MessageDelegate {
    public static final String DEFAULT_SECURITY_QUESTION_VALUE = "********";
    public static final String EMPTY_VALUE = "";
    public static final String CQL_FUNCTION_ARGUMENT_NAME_ERROR = "Invalid argument name. Must start with an alpha-character or underscore followed by an alpha-numeric character(s) or underscore(s), and must not contain spaces.";
    public static final String CONTINOUS_VARIABLE_IS_NOT_PATIENT_BASED_ERROR = "Continous Variable measures must not be patient based.";
    public static final String s_ERR_RETRIEVE_SCORING_CHOICES = "Problem while retrieving measure scoring choices.";
    public static final String NQF_NUMBER_REQUIRED_ERROR = "NQF Number is required when a measure is Endorsed by NQF.";
    public static final String SECURITY_QUESTION_ANSWERS_MUST_CONTAIN_AT_LEAST_THREE_CHARACTERS = "Security question answers must contain at least three characters.";
    public static final String ALL_SECURITY_QUESTIONS_MUST_CONTAIN_A_VALID_SECURITY_ANSWER = "All security questions must contain a valid security answer";
    public static final String GENERIC_ERROR_MESSAGE = "The Measure Authoring Tool was unable to process the request. Please try again. If the problem persists please contact the Help Desk.";
    public static final String DUPLICATE_LIBRARY_NAME = "The CQL library name is used by one or more CQL libraries within the MAT. Please edit the CQL library name.";
    public static final String DUPLICATE_LIBRARY_NAME_SAVE = "The MAT was unable to save the change to the CQL library name. All CQL library names must be unique within the MAT.";
    public static final String VERSION_LIBRARY_NAME_ERROR_MESSAGE = "The MAT is unable to version the measure. The CQL Library name associated with this measure is not unique. Please edit the CQL Library name before versioning.";
    public static final String VERSION_STANDALONE_LIBRARY_NAME_ERROR_MESSAGE = "The MAT is unable to version the Library. The CQL Library name is not unique. Please edit the CQL Library name before versioning.";
    public static final String VSAC_UNAUTHORIZED_ERROR = "Unable to retrieve information from VSAC. Please log in to UMLS again to re-establish a connection.";

    private static final String WELCOME_MESSAGE = "You have successfully logged into the MAT.";
    private static final String ALERT_LOADING_MESSAGE = "Please wait until loading is complete.";
    private static final String CHANGES_SAVED = "Changes are successfully saved.";
    private static final String NO_MARKUP_ALLOWED = "No markup text allowed in any text fields.";
    private static final String FIRST_NAME_MIN = "First Name must be at least two characters.";
    private static final String FIRST_NAME_REQUIRED = "First Name is required.";
    private static final String GROUPING_SAVED = "Grouping has been saved.";
    private static final String LAST_NAME_REQUIRED = "Last Name is required.";
    private static final String LOGIN_ID_REQUIRED = "User ID is required.";
    private static final String LIBRARY_NAME_REQUIRED = "Library Name is required.";
    public static final String LIBRARY_NAME_IS_CQL_KEYWORD_ERROR = "The CQL Library Name can not be the same as a CQL Keyword.";
    public static final String QDM_CQL_STAND_ALONE_LIBRARY_NAME_ERROR = "Invalid Library Name. Library names must start with an alpha-character or underscore, followed by an alpha-numeric character(s) or underscore(s), and must not contain spaces.";
    public static final String FHIR_CQL_STAND_ALONE_LIBRARY_NAME_ERROR = "Invalid Library Name. Library names must start with an upper case letter, followed by an alpha-numeric character(s) or underscore(s), and must not contain spaces.";
    private static final String EMAIL_ID_INCORRECT_FORMAT = "Email Address has an incorrect format.";
    private static final String EMAIL_ID_REQUIRED = "Email Address is required.";
    private static final String HARP_ID_IS_REQUIRED = "HARP ID is required.";
    private static final String LOGIN_USER_REQUIRED = "User Name is required.";
    private static final String NO_MEASURES = "No measures returned. Please change your search criteria and search again.";
    private static final String NO_LIBRARIES_RETURNED = "No libraries returned. Please change your search criteria and search again.";
    private static final String OID_REQUIRED = "OID is required.";
    private static final String OID_TOO_LONG = "OID cannot exceed 50 characters.";
    private static final String ORG_REQUIRED = "Organization is required.";
    private static final String PASSWORD_CHANGED = "Your password has been changed.";
    private static final String PERSONAL_INFO_UPDATED = "Your personal information has been updated.";
    private static final String PHONE_REQUIRED = "Phone Number is required.";
    private static final String SEC_QUESTIONS_UPDATED = "Your security questions have been updated.";
    private static final String SUPP_DATA_SAVED = "Supplemental Data Elements have been saved.";
    private static final String RISK_ADJ_SAVED = "Risk Adjustment Variables have been saved.";
    private static final String TEMP_EMAIL_SENT = " Account re-activated E-mail has been sent.";
    private static final String VSAC_VERSION_OR_EFFECTIVE_DATE_REQUIRED = "Value set version or effective date has been selected." + " Please enter a date to continue.";
    private static final String RATIO_NUM_DENO_ASSOCIATION_REQUIRED = "For Ratio measures, in the case of more than one Initial Population, " + " Numerator and Denominator must contain one association.";
    private static final String RATIO_MEASURE_OBS_ASSOCIATION_REQUIRED = "For Ratio Measures, each Measure Observation requires an association be made to the Numerator or the Denominator.";
    private static final String ERROR_IN_SAVING_QDM_ELEMENTS = "Valuesets with different OIDs can not have the same valueset name.";
    private static final String CQL_LIBRARY_LIMIT_WARNING = "You have reached the maximum number of selected CQL Libraries (10).";
    private static final String VSAC_EXPANSION_PROFILE_SELECTION = "Please Select VSAC Expansion Profile to Apply to value sets.";
    private static final String DEFAULT_EXPANSION_PROFILE_REMOVED = "Successfully removed VSAC Expansion Profile from value sets.";
    private static final String VSAC_PROFILE_APPLIED_TO_QDM_ELEMENTS = "Successfully applied VSAC Expansion Profile to value sets.";
    private static final String WARNING_MEASURE_PACKAGE_CREATION_GENERIC = "Unable to create measure package. " + "Please validate your measure logic in both Population Workspace and Clause Workspace.";
    private static final String WARNING_MEASURE_PACKAGE_CREATION_RISK_ADJUSTMENT = "Unable to create measure package. " + "Please validate that Risk Adjustment Vairable clauses do not contain a datetimediff function or a QDM Variable.";
    private static final String WARNING_MEASURE_PACKAGE_CREATION_STRATA = "Unable to create measure package. Stratum must contain at least one clause.";
    private static final String SUCCESSFULLY_MODIFIED_ALL_OIDS = "All QDM elements and/or attributes using the same value set OID have been modified " + "to the selected Version and/or Expansion Identifier.";
    private static final String DELETE_WARNING_MESSAGE = "You have selected to delete this expression. Do you want to permanently delete";
    private static final String NO_USERS_RETURNED = "No users returned. Please search again.";
    private static final String NO_VERSION_CREATED = "Unable to version. There are validation errors in CQL. Please correct and try again.";
    private static final String MSG_TYPE_CHECK_VALIDATION_MEASURE_PACKAGE = "Unable to create measure package. There is an incorrect return type in the following grouping(s): ";
    private static final String ERROR_VALIDATION_COMMENT_AREA = "Comment cannot exceed 250 characters. ";
    private static final String INVALID_COMMENT_CHARACTERS = "Comments can not contain /* or */.";
    private static final String PACKAGER_CQL_ERROR = "Your CQL file contains validation errors. Errors must be corrected before proceeding to measure packaging. Please return to the CQL Workspace to make corrections.";
    private static final String SUCCESSFULLY_SHARED = " sharing status has been successfully updated.";
    private static final String UMLS_CODE_IDENTIFIER_REQUIRED = "Please enter a Code URL.";
    private static final String ACCOUNT_REVOKED = "Your account has been revoked. Please contact Support.";
    private static final String CLAUSE_WORK_SPACE_VALIDATION_SUCCESS = "Measure logic validation successful.";
    private static final String ALL_PASSWORD_FIELDS_REQUIRED = "All password fields are required.";
    private static final String MEASURE_LOGIC_IS_INCOMPLETE = " Measure logic is incomplete.";
    private static final String LHS_RHS_REQUIRED = " LHS and RHS are required for Timings, Relationships and Satisfies functions.";
    private static final String ATLEAST_ONE_CHILD_REQUIRED = " Functions must contain at least one child node.";
    private static final String AT_LEAST_TWO_CHILDREN_REQUIRED = "Union, Intersection, and Datetimediff must contain at least two or more child nodes.";
    private static final String AT_LEAST_THREE_CHILDREN_REQUIRED = "Satisfies All and Satisfies Any must contain at least three or more child nodes.";
    private static final String INVALID_CHARACTER_VALIDATION_ERROR = " Value set name cannot contain any of the following characters + * ? : - | ! \" %";
    private static final String ORGANIZATION_SUCCESS_MESSAGE = "Organization successfully added.";
    private static final String ORGANIZATION_MODIFIED_SUCCESS_MESSAGE = "Organization successfully modified.";
    private static final String USER_SUCCESS_MESSAGE = "User information saved successfully.";
    private static final String CLAUSE_EMPTY = "Clause must contain logic.";
    private static final String PATIENT_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE = "For Patient-based Measures, all definitions directly added to populations must return a Boolean.";
    private static final String EPISODE_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE = "For Episode-based Measures, all definitions directly added to populations must return a list of the same type.";
    private static final String EPISODE_BASED_RATIO_MEASURE_SAVE_GROUPING_VALIDATION_MESSAGE = "Measure Observations can only be added to a measure grouping in a Ratio measure, if the measure is Episode-based.";
    private static final String MEASURE_OBSERVATION_USER_DEFINED_FUNC_VALIDATION_MESSAGE = "Measure Observations added to a measure grouping may only contain a user-defined function that has exactly 1 argument in the argument list.";
    private static final String MEASURE_OBSERVATION_RETURN_SAME_TYPE_VALIDATION_MESSAGE = "A Measure Observation must have an argument that returns the same type as the items in the list that was returned for the definition applied to the associated measure population.";
    private static final String MEASURE_OBSERVATION_USER_DEFINED_FUNC_REURN_TYPE_VALIDATION_MESSAGE = "Measure Observations added to a measure grouping must contain a user-defined function that returns an integer, a decimal, or a quantity.";
    private static final String CONTINUOUS_VARIABLE_MAY_NOT_CONTAIN = "For a Continuous Variable measure, a grouping may not contain any Numerator, Numerator Exclusions, Denominator, Denominator Exclusions, or Denominator Exceptions.";
    private static final String CONTINUOUS_VARIABLE_WRONG_NUM = "For a Continuous Variable measure, a grouping must contain exactly one of each of the following: " + "Initial Population, Measure Population,and at least one Measure Observation.";
    private static final String DELETE_MEASURE_WARNING_MESSAGE = "Deleting a draft or version of a measure will permanently remove the designated measure draft or version from  the Measure Authoring Tool. Deleted measures cannot be recovered.";
    private static final String DOESNT_FOLLOW_RULES = "The new password you entered does not match the following rules:";
    private static final String DUPLICATE_ERROR = "Import failed. One or more duplicate codes exist in file. Please remove then try again.";
    private static final String EMAIL_ALREADY_EXISTS = "E-mail Address already exists.";
    private static final String HARP_ID_ALREADY_EXISTS = "HARP ID already exists.";
    private static final String EMAIL_MISMATCH = "Email did not match with the User ID.  Please Contact the Administrator.";
    private static final String EMAIL_NOT_FOUND_MSG = "Email Address is required.";
    private static final String EMPTY_FILE_ERROR = "Import failed. File is empty.";
    private static final String GROUPING_REQUIRED = "A Grouping is required.";
    private static final String LOGIN_FAILED = "Invalid username and/or password and/or One Time Security code. MAT accounts are locked after three invalid login attempts.";
    private static final String LOGIN_FAILED_TEMP_PASSWORD_EXPIRED = "Unable to login. Your temporary password has expired. Please contact HelpDesk to renew your password.";
    private static final String LOGIN_FAILED_USER_ALREADY_LOGGED_IN = "Unable to login. Another session has been established with this account. Please verify you do not already have an open session.";
    private static final String MEASURE_DELETION_INVALID_PWD = "The entered password is invalid. Please try again.";
    private static final String MEASURE_DELETION_SUCCESS_MSG = "Measure successfully deleted.";
    private static final String MEASURE_NAME_REQUIRED = "Measure Name is required.";
    private static final String MEASURE_PACKAGE_UMLS_NOT_LOGGED_IN = "Measure packaged successfully. " + "Value set data is not included in the measure package as you are not logged into UMLS.";
    private static final String MEASURE_PACKAGE_FAILED_VSAC_TIMEOUT = "Measure Package Failed. VSAC request timed out.Please contact Help Desk.";
    private static final String MEASURE_PERIOD_DATES_ERROR = "Please enter valid Measurement Period dates.";
    private static final String MEASURE_SELECTION_ERROR = "Please select at least one measure";
    private static final String MODIFY_QDM_ATTRIBUTE_VALIDATION = "A value set with an Attribute category must be used for this data element.";
    private static final String MODIFY_QDM_NON_ATTRIBUTE_VALIDATION = "A value set with a non-Attribute category must be used for this data element.";
    private static final String MODIFY_VALUE_SET_SELECT_ATLEAST_ONE = "Please select atleast one applied value set to modify.";
    private static final String MUST_CONTAIN_LOWER = "Must contain a lowercase letter.";
    private static final String MUST_CONTAIN_NUMBER = "Must contain a number.";
    private static final String MUST_CONTAIN_SPECIAL = "Must contain a special character.";
    private static final String MUST_CONTAIN_UPPER = "Must contain an uppercase letter.";
    private static final String NOT_UNIQUE_QUESTIONS = "You cannot use the same security question more than once.";
    private static final String OID_EXISTS = "OID already exists.";
    private static final String PACKAGE_SUCCESS = "Measure packaged successfully. Please access the Measure Library to export the measure.";
    private static final String PACKAGE_SUCCESS_VSAC_OID_MISSING = "Measure packaged successfully. One or more OIDs could not be updated from VSAC.";
    private static final String PASSWORD_MISMATCH = "Your new password and confirm password do not match.";
    private static final String INVALID_USER = "Login Failed : Try Again";
    private static final String IS_NOT_CURRENT_PASSWORD = "New password cannot be the same as the previous 6 passwords.";
    private static final String IS_NOT_PREVIOUS_PASSWORD = "Previous 6 passwords cannot be reused. Try again.";
    private static final String CHANGE_OLD_PASSWORD = "Password needs to be at least one day old before you can change it. Try again.";
    private static final String PASSWORD_MISMATCH_ERROR_MESSAGE = "Incorrect password supplied.Try again";
    private static final String PASSWORD_REQUIRED = "Password is required.";
    private static final String PASSWORD_REQUIRED_ERROR_MESSAGE = "Existing password is required to confirm changes.";
    private static final String PASSWORD_WRONG_LENGTH = "Must be between 15 and 128 characters long.";
    private static final String SECURITYCODE_REQUIRED = "Security Code is required.";
    private static final String PHONE_10_DIGIT = "Phone Number is required to be 10 digits.";
    private static final String PROPORTION_MAY_NOT_CONTAIN = "For a Proportion measure,a grouping may not contain a Numerator Exclusion,Measure Population,or Measure Observation.";
    private static final String PROPORTION_TOO_MANY = "For a Proportion measure, a grouping may not contain more than one of each of the following: " + "Denominator Exclusions, Denominator Exceptions, and Numerator Exclusions.";
    private static final String PROPORTION_WRONG_NUM = "For a Proportion measure, a grouping must contain exactly one of each of the following: " + "Initial Population, Denominator and Numerator.";
    private static final String RATIO_MAY_NOT_CONTAIN = "For a Ratio measure,a grouping may not contain a Denominator Exception,or Measure Population.";
    private static final String RATIO_TOO_MANY = "For a Ratio measure, a grouping may not contain more than one of each of the following: Denominator Exclusion and Numerator Exclusion.";
    private static final String RATIO_TOO_MANY_POPULATIONS = "For a Ratio measure, a grouping may not contain more than two of the following: Initial Populations.";
    private static final String RATIO_WRONG_NUM = "For a Ratio measure, a grouping must contain exactly one of each of the following: " + "Denominator and Numerator.";
    private static final String RATIO_TOO_FEW_POPULATIONS = "For a Ratio measure, a grouping must contain at least one Initial Population.";
    private static final String INVALID_LOGIC_CQL_WORK_SPACE = "Measure Logic is incomplete.Please validate your measure logic in CQL Workspace.";
    private static final String INVALIDLOGIC_CLAUSE_WORK_SPACE = "Clause logic is incomplete.Please validate your clause logic.";
    private static final String COHORT_WRONG_NUM = "For a Cohort measure, a grouping must contain exactly one Initial Population.";
    private static final String STRATIFICATION_VALIDATION_FOR_GROUPING = " Measure Grouping cannot contain more than one Stratification.";
    private static final String MEASURE_OBS_VALIDATION_FOR_GROUPING = " A Ratio Measure may not contain more than two Measure Observations in a Measure grouping.";
    private static final String SAVE_ERROR_MSG = "You have unsaved changes that will be discarded if you continue. Do you want to continue without saving?";
    private static final String SECURITY_NOT_ANSWERED = "Your security questions have not been answered.  You cannot continue. Please contact the Helpdesk";
    private static final String SECURITY_Q_MISMATCH = "The answer for the security question did not match.";
    private static final String SECURITY_QUESTION_HEADER = "The answer(s) provided to the security question(s) do not match the following rules:";
    private static final String SERVER_CALL_NULL = "Server call for login returned null.";
    private static final String SUCCESSFUL_MODIFY_APPLIED_VALUESET = "Selected value set has been modified successfully.";
    private static final String SUCCESSFUL_MODIFY_APPLIED_CODE = "Selected code has been modified successfully.";
    private static final String SYSTEM_ERROR = "Import failed due to system error. Please try again.";
    private static final String TRANSFER_CHECKBOX_ERROR_MEASURE = "Please select at least one Measure to transfer ownership.";
    private static final String TRANSFER_CHECKBOX_ERROR_CQL_LIBRARY = "Please select at least one CQL Library to transfer ownership.";
    private static final String TRANSFER_OWNERSHIP_SUCCESS = "Ownership successfully transferred to ";
    private static final String UML_LOGIN_FAILED = "Login failed. Please sign in again.";
    private static final String UML_LOGIN_UNAVAILABLE = "Unable to verify your UMLS credentials. Please contact the MAT Help Desk or try again.";
    private static final String UMLS_NOT_LOGGEDIN = "You are not logged in to UMLS. Please sign in to UMLS.";
    private static final String UMLS_OID_REQUIRED = "Please enter an OID.";
    private static final String UMLS_SUCCESSFULL_LOGIN = "Successfully logged in to UMLS";
    private static final String UNABLE_TO_PROCESS = "System error.  Unable to process information.";
    private static final String UNKNOWN_FAIL = "Unknown failure reason";
    private static final String USER_NOT_FOUND_MSG = "User ID is required.";
    private static final String USER_REQUIRED_ERROR_MSG = "Please select a user to transfer ownership.";
    private static final String VALIDATION_MSG_DATA_TYPE_VSAC = "Please select datatype from drop down list.";
    private static final String VALIDATION_MSG_ELEMENT_WITHOUT_VSAC = "Please enter value set name.";
    private static final String VALUE_SET_DATE_INVALID = "Value Set Package Date is not a valid date.";
    private static final String VSAC_RETRIEVE_FAILED = "Unable to retrieve from VSAC. Please check the data and try again.";
    private static final String VSAC_RETRIEVE_TIMEOUT = "Your request to VSAC has timed-out. Please attempt your retrieve again. If the problem persists, please contact the MAT Support Desk.";
    private static final String COMMENT_ADDED_SUCCESSFULLY = "Comment Changes Added.";
    private static final String COMPARISON_DILOAG_BOX_ERROR_DISPLAY = "Please enter Quantity field.";
    private static final String COMPARISON_DILOAG_BOX_UNIT_ERROR_DISPLAY = "Please select Unit.";
    private static final String CLAUSE_WORK_SPACE_INVALID_NESTED_CLAUSE = "Invalid clause(s) used in logic.";
    private static final String CLAUSE_WORK_SPACE_INVALID_ATTRIBUTE_IN_SAT_ALL_ANY = "Satisfies All and Satisfies Any LHS QDM element may not contain attributes.";
    private static final String CLAUSE_WORK_SPACE_INVALID_NESTED_DEPTH_CLAUSE = "Clause Exceeds Maximum Number of Nested Logic Levels (10).";
    private static final String CLAUSE_WORK_SPACE_INVALID_LOGICAL_OPERATOR = "Any logical operator under a top-level logical operator must contain at least one logical operator or clause. Any terminal logical operator under a top-level logical operator must contain at least one clause.";
    private static final String ONLY_ONE_CHILD_REQUIRED = " Functions must contain only one child node.";
    private static final String INVALID_LOGIC_MEASURE_PACKAGER = "Populations or Measure Observations within a Measure Grouping must contain a valid Definition or Function.";
    private static final String ERROR_LIBRARY_VERSION = "Please select version type Major or Minor.";
    private static final String ERROR_SELECT_ATTRIBUTE_NAME = "Please select Attribute Name.";
    private static final String ERROR_SELECT_ITEM_NAME = "Please select Item Name.";
    private static final String ERROR_SELECT_ITEM_TYPE = "Please select Item Type.";
    private static final String ERROR_SELECT_ITEM_NAME_OR_DATA_TYPE = "Please select Item Name or Datatype.";
    private static final String ERROR_INVALID_QUANTITY = "Please Enter valid Quantity.";
    private static final String ERROR_INVALID_DATE_TIME_QUANTITY = "You can not enter both DateTime and Quantity.";
    private static final String ERROR_INVALID_MODE_DETAILS = "Please Select valid Mode Details.";
    private static final String ERROR_SELECT_ATTRIBUTE_TO_INSERT = "Please Select Attribute to insert into Editor";
    private static final String ERROR_ENTER_DATE_TIME_AND_QUANTITY = "Please enter DateTime or Quantity/units.";
    private static final String ERROR_INVALID_DATE_TIME = "Please Enter a valid Date/Time.";
    private static final String WARNING_DELETION_CQL_LIBRARY = "Deleting a draft of a library will permanently remove the designated " + "library draft from the Measure Authoring Tool. Deleted libraries cannot be recovered.";
    private static final String CQL_LIBRARY_DELETION_SUCCESS_MSG = "CQL Library successfully deleted.";
    private static final String UNABLE_TO_VERIFY_HARP_USER = "Unable to verify user, Please contact the MAT Help Desk or try again.";
    private static final String DESCRIPTION_REQUIRED = "Description is required for Standalone Libraries. Please populate it in General Information.";
    private static final String PUBLISHER_REQUIRED = "Publisher is required for Standalone Libraries. Please populate it in General Information.";


    private static final String CQL_LIBRARY_DESCRIPTION_REQUIRED = "CQL Library Description is required.";
    private static final String CQL_LIBRARY_PUBLISHER_REQUIRED = "CQL Library Publisher is required.";

    public static String getUnableToVerifyHarpUser() {
        return UNABLE_TO_VERIFY_HARP_USER;
    }

    public String getONLY_ONE_CHILD_REQUIRED() {
        return ONLY_ONE_CHILD_REQUIRED;
    }

    public String getRATIO_TOO_FEW_POPULATIONS() {
        return RATIO_TOO_FEW_POPULATIONS;
    }

    public String getAccountRevokedMessage() {
        return ACCOUNT_REVOKED;
    }

    public String getAlertLoadingMessage() {
        return ALERT_LOADING_MESSAGE;
    }

    public String getNoMarkupAllowedMessage() {
        return NO_MARKUP_ALLOWED;
    }

    public String getAllPasswordFieldsRequired() {
        return ALL_PASSWORD_FIELDS_REQUIRED;
    }

    public String getChangesSavedMessage() {
        return CHANGES_SAVED;
    }

    public String getCLAUSE_WORK_SPACE_VALIDATION_SUCCESS() {
        return CLAUSE_WORK_SPACE_VALIDATION_SUCCESS;
    }

    public String getContinuousVariableMayNotContainMessage() {
        return CONTINUOUS_VARIABLE_MAY_NOT_CONTAIN;
    }

    public String getContinuousVariableWrongNumMessage() {
        return CONTINUOUS_VARIABLE_WRONG_NUM;
    }

    public String getDELETE_MEASURE_WARNING_MESSAGE() {
        return DELETE_MEASURE_WARNING_MESSAGE;
    }

    public String getDoesntFollowRulesMessage() {
        return DOESNT_FOLLOW_RULES;
    }

    public String getDuplicateAppliedValueSetMsg(String name) {
        return name + " already exists. Please add a unique suffix.";
    }

    public String getDuplicateErrorMessage() {
        return DUPLICATE_ERROR;
    }

    public String getEmailAlreadyExistsMessage() {
        return EMAIL_ALREADY_EXISTS;
    }

    public String getHarpIdAlreadyExistsMessage() {
        return HARP_ID_ALREADY_EXISTS;
    }

    public String getEmailMismatchMessage() {
        return EMAIL_MISMATCH;
    }

    public String getEmailNotFoundMessage() {
        return EMAIL_NOT_FOUND_MSG;
    }

    public String getEmptyFileError() {
        return EMPTY_FILE_ERROR;
    }

    public String getFirstMinMessage() {
        return FIRST_NAME_MIN;
    }

    public String getFirstNameRequiredMessage() {
        return FIRST_NAME_REQUIRED;
    }

    public String getGenericErrorMessage() {
        return GENERIC_ERROR_MESSAGE;
    }

    public String getGroupingRequiredMessage() {
        return GROUPING_REQUIRED;
    }

    public String getGroupingSavedMessage() {
        return GROUPING_SAVED;
    }

    public String getLastNameRequiredMessage() {
        return LAST_NAME_REQUIRED;
    }

    public String getLoginFailedAlreadyLoggedInMessage() {
        return LOGIN_FAILED_USER_ALREADY_LOGGED_IN;
    }

    public String getLoginFailedMessage() {
        return LOGIN_FAILED;
    }

    public String getLoginFailedTempPasswordExpiredMessage() {
        return LOGIN_FAILED_TEMP_PASSWORD_EXPIRED;
    }

    public String getLoginIDRequiredMessage() {
        return LOGIN_ID_REQUIRED;
    }

    public String getEmailIdFormatIncorrectMessage() {
        return EMAIL_ID_INCORRECT_FORMAT;
    }

    public String getLoginUserRequiredMessage() {
        return LOGIN_USER_REQUIRED;
    }

    public String getMEASURE_PACKAGE_UMLS_NOT_LOGGED_IN() {
        return MEASURE_PACKAGE_UMLS_NOT_LOGGED_IN;
    }

    public String getMEASURE_PACKAGE_VSAC_TIMEOUT() {
        return MEASURE_PACKAGE_FAILED_VSAC_TIMEOUT;
    }

    public String getMEASURE_PERIOD_DATES_ERROR() {
        return MEASURE_PERIOD_DATES_ERROR;
    }

    public String getMeasureDeletionInvalidPwd() {
        return MEASURE_DELETION_INVALID_PWD;
    }

    public String getMeasureDeletionSuccessMgs() {
        return MEASURE_DELETION_SUCCESS_MSG;
    }

    public String getMeasureNameRequiredMessage() {
        return MEASURE_NAME_REQUIRED;
    }

    public String getMeasureSelectionError() {
        return MEASURE_SELECTION_ERROR;
    }

    public String getMODIFY_QDM_ATTRIBUTE_VALIDATION() {
        return MODIFY_QDM_ATTRIBUTE_VALIDATION;
    }

    public String getMODIFY_QDM_NON_ATTRIBUTE_VALIDATION() {
        return MODIFY_QDM_NON_ATTRIBUTE_VALIDATION;
    }

    public String getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE() {
        return MODIFY_VALUE_SET_SELECT_ATLEAST_ONE;
    }

    public String getMustContainLowerMessage() {
        return MUST_CONTAIN_LOWER;
    }

    public String getMustContainNumberMessage() {
        return MUST_CONTAIN_NUMBER;
    }

    public String getMustContainSpecialMessage() {
        return MUST_CONTAIN_SPECIAL;
    }

    public String getMustContainUpperMessage() {
        return MUST_CONTAIN_UPPER;
    }

    public String getNoMeasuresMessage() {
        return NO_MEASURES;
    }

    public String getNotUniqueQuestions() {
        return NOT_UNIQUE_QUESTIONS;
    }

    public String getOIDExistsMessage() {
        return OID_EXISTS;
    }

    public String getOIDRequiredMessage() {
        return OID_REQUIRED;
    }

    public String getOIDTooLongMessage() {
        return OID_TOO_LONG;
    }

    public String getOrgRequiredMessage() {
        return ORG_REQUIRED;
    }

    public final String getPackageSuccessMessage() {
        return PACKAGE_SUCCESS;
    }

    public final String getPackageSuccessAmberMessage() {
        return PACKAGE_SUCCESS_VSAC_OID_MISSING;
    }

    public String getPasswordMismatchErrorMessage() {
        return PASSWORD_MISMATCH_ERROR_MESSAGE;
    }

    public String getPasswordMismatchMessage() {
        return PASSWORD_MISMATCH;
    }

    public static String getInvalidUser() {
        return INVALID_USER;
    }

    public String getPasswordRequiredErrorMessage() {
        return PASSWORD_REQUIRED_ERROR_MESSAGE;
    }

    public String getPasswordRequiredMessage() {
        return PASSWORD_REQUIRED;
    }

    public String getSecurityCodeRequiredMessage() {
        return SECURITYCODE_REQUIRED;
    }

    public String getPasswordSavedMessage() {
        return PASSWORD_CHANGED;
    }

    public String getPasswordWrongLengthMessage() {
        return PASSWORD_WRONG_LENGTH;
    }

    public String getPersonalInfoUpdatedMessage() {
        return PERSONAL_INFO_UPDATED;
    }

    public String getPhoneRequiredMessage() {
        return PHONE_REQUIRED;
    }

    public String getPhoneTenDigitMessage() {
        return PHONE_10_DIGIT;
    }

    public String getProportionMayNotContainMessage() {
        return PROPORTION_MAY_NOT_CONTAIN;
    }

    public String getProportionTooManyMessage() {
        return PROPORTION_TOO_MANY;
    }

    public String getProportionWrongNumMessage() {
        return PROPORTION_WRONG_NUM;
    }

    public String getQDMOcurrenceSuccessMessage(String codeListName, String dataType, String occurrenceMessage) {
        return " The QDM Element " + occurrenceMessage + " of " + codeListName + ": " + dataType + " has been added successfully.";
    }

    public String getQDMSuccessMessage(String codeListName, String dataType) {
        return " The QDM Element " + codeListName + ": " + dataType + " has been added successfully.";
    }

    public String getRatioMayNotContainMessage() {
        return RATIO_MAY_NOT_CONTAIN;
    }

    public String getRatioTooManyMessage() {
        return RATIO_TOO_MANY;
    }

    public String getRatioWrongNumMessage() {
        return RATIO_WRONG_NUM;
    }

    public String getSaveErrorMsg() {
        return SAVE_ERROR_MSG;
    }

    public String getSecurityNotAnsweredMessage() {
        return SECURITY_NOT_ANSWERED;
    }

    public String getSecurityQMismatchMessage() {
        return SECURITY_Q_MISMATCH;
    }

    public String getSecurityQuestionHeeaderMessage() {
        return SECURITY_QUESTION_HEADER;
    }

    public String getSecurityQuestionsUpdatedMessage() {
        return SEC_QUESTIONS_UPDATED;
    }

    public String getServerCallNullMessage() {
        return SERVER_CALL_NULL;
    }

    public String getSUCCESSFUL_MODIFY_APPLIED_VALUESET() {
        return SUCCESSFUL_MODIFY_APPLIED_VALUESET;

    }

    public String getSuppDataSavedMessage() {
        return SUPP_DATA_SAVED;
    }

    public String getRiskAdjSavedMessage() {
        return RISK_ADJ_SAVED;
    }

    public String getSystemErrorMessage() {
        return SYSTEM_ERROR;
    }

    public String getTempEmailSentMessage() {
        return TEMP_EMAIL_SENT;
    }

    public String getTransferCheckBoxErrorMeasure() {
        return TRANSFER_CHECKBOX_ERROR_MEASURE;
    }

    public String getTransferOwnershipSuccess() {
        return TRANSFER_OWNERSHIP_SUCCESS;
    }

    public String getUML_LOGIN_FAILED() {
        return UML_LOGIN_FAILED;
    }

    public String getUML_LOGIN_UNAVAILABLE() {
        return UML_LOGIN_UNAVAILABLE;
    }

    public String getUMLS_NOT_LOGGEDIN() {
        return UMLS_NOT_LOGGEDIN;
    }

    public String getUMLS_OID_REQUIRED() {
        return UMLS_OID_REQUIRED;
    }

    public String getUMLS_SUCCESSFULL_LOGIN() {
        return UMLS_SUCCESSFULL_LOGIN;
    }

    public String getUnableToProcessMessage() {
        return UNABLE_TO_PROCESS;
    }

    public String getUnknownErrorMessage(int code) {
        return "Unknown Code " + code;
    }

    public String getUnknownFailMessage() {
        return UNKNOWN_FAIL;
    }

    public String getUserNotFoundMessage() {
        return USER_NOT_FOUND_MSG;
    }

    public String getUserRequiredErrorMessage() {
        return USER_REQUIRED_ERROR_MSG;
    }

    public String getVALIDATION_MSG_DATA_TYPE_VSAC() {
        return VALIDATION_MSG_DATA_TYPE_VSAC;
    }

    public String getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC() {
        return VALIDATION_MSG_ELEMENT_WITHOUT_VSAC;
    }

    public String getValueSetDateInvalidMessage() {
        return VALUE_SET_DATE_INVALID;
    }

    public String getVSAC_RETRIEVE_FAILED() {
        return VSAC_RETRIEVE_FAILED;
    }

    public String getVSAC_VERSION_OR_EFFECTIVE_DATE_REQUIRED() {
        return VSAC_VERSION_OR_EFFECTIVE_DATE_REQUIRED;
    }

    public String getIS_NOT_PREVIOUS_PASSWORD() {
        return IS_NOT_PREVIOUS_PASSWORD;
    }

    public String getCHANGE_OLD_PASSWORD() {
        return CHANGE_OLD_PASSWORD;
    }

    public String getIS_NOT_CURRENT_PASSWORD() {
        return IS_NOT_CURRENT_PASSWORD;
    }

    public String getCOMMENT_ADDED_SUCCESSFULLY() {
        return COMMENT_ADDED_SUCCESSFULLY;
    }

    public String getCOHORT_WRONG_NUM() {
        return COHORT_WRONG_NUM;
    }

    public String getComparisonDiloagBoxErrorDisplay() {
        return COMPARISON_DILOAG_BOX_ERROR_DISPLAY;
    }

    public String getRATIO_TOO_MANY_POPULATIONS() {
        return RATIO_TOO_MANY_POPULATIONS;
    }

    public String getRatioNumDenoAssociationRequired() {
        return RATIO_NUM_DENO_ASSOCIATION_REQUIRED;
    }

    public String getRatioMeasureObsAssociationRequired() {
        return RATIO_MEASURE_OBS_ASSOCIATION_REQUIRED;
    }

    public String getMEASURE_LOGIC_IS_INCOMPLETE() {
        return MEASURE_LOGIC_IS_INCOMPLETE;
    }

    public String getLHS_RHS_REQUIRED() {
        return LHS_RHS_REQUIRED;
    }

    public String getATLEAST_ONE_CHILD_REQUIRED() {
        return ATLEAST_ONE_CHILD_REQUIRED;
    }

    public String getAT_LEAST_TWO_CHILDREN_REQUIRED() {
        return AT_LEAST_TWO_CHILDREN_REQUIRED;
    }

    public String getAT_LEAST_THREE_CHILDREN_REQUIRED() {
        return AT_LEAST_THREE_CHILDREN_REQUIRED;
    }

    public String getINVALID_CHARACTER_VALIDATION_ERROR() {
        return INVALID_CHARACTER_VALIDATION_ERROR;
    }

    public String getSTRATIFICATION_VALIDATION_FOR_GROUPING() {
        return STRATIFICATION_VALIDATION_FOR_GROUPING;
    }

    public String getMEASURE_OBS_VALIDATION_FOR_GROUPING() {
        return MEASURE_OBS_VALIDATION_FOR_GROUPING;
    }

    public String getORGANIZATION_SUCCESS_MESSAGE() {
        return ORGANIZATION_SUCCESS_MESSAGE;
    }

    public String getORGANIZATION_MODIFIED_SUCCESS_MESSAGE() {
        return ORGANIZATION_MODIFIED_SUCCESS_MESSAGE;
    }

    public String getUSER_SUCCESS_MESSAGE() {
        return USER_SUCCESS_MESSAGE;
    }

    public String getEmailIdRequired() {
        return EMAIL_ID_REQUIRED;
    }

    public String getINVALID_LOGIC_CQL_WORK_SPACE() {
        return INVALID_LOGIC_CQL_WORK_SPACE;
    }

    public String getINVALIDLOGIC_CLAUSE_WORK_SPACE() {
        return INVALIDLOGIC_CLAUSE_WORK_SPACE;
    }

    public String getCLAUSE_WORK_SPACE_INVALID_NESTED_CLAUSE() {
        return CLAUSE_WORK_SPACE_INVALID_NESTED_CLAUSE;
    }

    public String getCLAUSE_WORK_SPACE_INVALID_NESTED_DEPTH_CLAUSE() {
        return CLAUSE_WORK_SPACE_INVALID_NESTED_DEPTH_CLAUSE;
    }

    public String getWelcomeMessage(String userFristName) {
        return "Welcome " + userFristName + "! " + WELCOME_MESSAGE;
    }

    public String getVsacExpansionProfileSelection() {
        return VSAC_EXPANSION_PROFILE_SELECTION;
    }

    public String getVsacProfileAppliedToQdmElements() {
        return VSAC_PROFILE_APPLIED_TO_QDM_ELEMENTS;
    }

    public String getDefaultExpansionIdRemovedMessage() {
        return DEFAULT_EXPANSION_PROFILE_REMOVED;
    }

    public String getERROR_IN_SAVING_QDM_ELEMENTS() {
        return ERROR_IN_SAVING_QDM_ELEMENTS;
    }

    public String getWARNING_MEASURE_PACKAGE_CREATION_GENERIC() {
        return WARNING_MEASURE_PACKAGE_CREATION_GENERIC;
    }

    public String getWARNING_MEASURE_PACKAGE_CREATION_RISK_ADJUSTMENT() {
        return WARNING_MEASURE_PACKAGE_CREATION_RISK_ADJUSTMENT;
    }

    public String getWARNING_MEASURE_PACKAGE_CREATION_STRATA() {
        return WARNING_MEASURE_PACKAGE_CREATION_STRATA;
    }

    public String getCLAUSE_WORK_SPACE_INVALID_ATTRIBUTE_IN_SAT_ALL_ANY() {
        return CLAUSE_WORK_SPACE_INVALID_ATTRIBUTE_IN_SAT_ALL_ANY;
    }

    public String getCLAUSE_WORK_SPACE_INVALID_LOGICAL_OPERATOR() {
        return CLAUSE_WORK_SPACE_INVALID_LOGICAL_OPERATOR;
    }

    public String getCLAUSE_EMPTY() {
        return CLAUSE_EMPTY;
    }

    public String getSUCCESSFULLY_MODIFIED_ALL_OIDS() {
        return SUCCESSFULLY_MODIFIED_ALL_OIDS;
    }

    public String getCOMPARISON_DILOAG_BOX_UNIT_ERROR_DISPLAY() {
        return COMPARISON_DILOAG_BOX_UNIT_ERROR_DISPLAY;
    }

    public String getINVALID_LOGIC_MEASURE_PACKAGER() {
        return INVALID_LOGIC_MEASURE_PACKAGER;
    }

    public String getDELETE_WARNING_MESSAGE() {
        return DELETE_WARNING_MESSAGE;
    }

    public String getLibraryNameRequired() {
        return LIBRARY_NAME_REQUIRED;
    }

    public String getQDMCqlLibyNameError() {
        return QDM_CQL_STAND_ALONE_LIBRARY_NAME_ERROR;
    }

    public String getFhirCqlLibyNameError() {
        return FHIR_CQL_STAND_ALONE_LIBRARY_NAME_ERROR;
    }

    public String getNoLibrarues() {
        return NO_LIBRARIES_RETURNED;
    }

    public String getERROR_LIBRARY_VERSION() {
        return ERROR_LIBRARY_VERSION;
    }

    public String getCqlLimitWarningMessage() {
        return CQL_LIBRARY_LIMIT_WARNING;
    }

    public static String getNoUsersReturned() {
        return NO_USERS_RETURNED;
    }

    public String getNoVersionCreated() {
        return NO_VERSION_CREATED;
    }

    public String getERROR_SELECT_ATTRIBUTE_NAME() {
        return ERROR_SELECT_ATTRIBUTE_NAME;
    }

    public String getERROR_SELECT_ITEM_NAME() {
        return ERROR_SELECT_ITEM_NAME;
    }

    public String getERROR_SELECT_ITEM_TYPE() {
        return ERROR_SELECT_ITEM_TYPE;
    }

    public String getERROR_SELECT_ITEM_NAME_OR_DATA_TYPE() {
        return ERROR_SELECT_ITEM_NAME_OR_DATA_TYPE;
    }

    public String getERROR_INVALID_QUANTITY() {
        return ERROR_INVALID_QUANTITY;
    }

    public String getERROR_INVALID_DATE_TIME_QUANTITY() {
        return ERROR_INVALID_DATE_TIME_QUANTITY;
    }

    public String getERROR_INVALID_MODE_DETAILS() {
        return ERROR_INVALID_MODE_DETAILS;
    }

    public String getERROR_SELECT_ATTRIBUTE_TO_INSERT() {
        return ERROR_SELECT_ATTRIBUTE_TO_INSERT;
    }

    public String getERROR_ENTER_DATE_TIME_AND_QUANTITY() {
        return ERROR_ENTER_DATE_TIME_AND_QUANTITY;
    }

    public String getERROR_INVALID_DATE_TIME() {
        return ERROR_INVALID_DATE_TIME;
    }

    public String getTRANSFER_CHECKBOX_ERROR_CQL_LIBRARY() {
        return TRANSFER_CHECKBOX_ERROR_CQL_LIBRARY;
    }

    public String getUMLS_CODE_IDENTIFIER_REQUIRED() {
        return UMLS_CODE_IDENTIFIER_REQUIRED;
    }

    public String getCreatePackageTypeCheckError() {
        return MSG_TYPE_CHECK_VALIDATION_MEASURE_PACKAGE;
    }

    public String getWARNING_DELETION_CQL_LIBRARY() {
        return WARNING_DELETION_CQL_LIBRARY;
    }

    public String getCQL_LIBRARY_DELETION_SUCCESS_MSG() {
        return CQL_LIBRARY_DELETION_SUCCESS_MSG;
    }

    public String getERROR_VALIDATION_COMMENT_AREA() {
        return ERROR_VALIDATION_COMMENT_AREA;
    }

    public String getPATIENT_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE() {
        return PATIENT_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE;
    }

    public String getEPISODE_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE() {
        return EPISODE_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE;
    }

    public String getEPISODE_BASED_RATIO_MEASURE_SAVE_GROUPING_VALIDATION_MESSAGE() {
        return EPISODE_BASED_RATIO_MEASURE_SAVE_GROUPING_VALIDATION_MESSAGE;
    }

    public String getMEASURE_OBSERVATION_USER_DEFINED_FUNC_VALIDATION_MESSAGE() {
        return MEASURE_OBSERVATION_USER_DEFINED_FUNC_VALIDATION_MESSAGE;
    }

    public String getMEASURE_OBSERVATION_RETURN_SAME_TYPE_VALIDATION_MESSAGE() {
        return MEASURE_OBSERVATION_RETURN_SAME_TYPE_VALIDATION_MESSAGE;
    }

    public String getMEASURE_OBSERVATION_USER_DEFINED_FUNC_REURN_TYPE_VALIDATION_MESSAGE() {
        return MEASURE_OBSERVATION_USER_DEFINED_FUNC_REURN_TYPE_VALIDATION_MESSAGE;
    }

    public static String getPACKAGER_CQL_ERROR() {
        return PACKAGER_CQL_ERROR;
    }

    public String getSUCESS_FUNCTION_ARG_MODIFY(String ArgName) {
        return "Function Argument " + ArgName + " successfully modified.";
    }

    public String getSUCESS_FUNCTION_ARG_ADD(String ArgName) {
        return "Function Argument " + ArgName + " successfully saved.";
    }

    public String getMeasureDraftSuccessfulMessage(String measureName) {
        return "You have created a draft of " + StringUtility.trimTextToSixtyChars(measureName) + ". Please click continue to navigate to the Measure Details page.";
    }

    public String getLibraryDraftSuccessfulMessage(String cqlLibName) {
        return "You have created a draft of " + StringUtility.trimTextToSixtyChars(cqlLibName) + ". Please click continue to navigate to the CQL Composer.";
    }

    public String getCreateNewMeasureSuccessfulMessage(String measureName) {
        return "You have created a new measure " + StringUtility.trimTextToSixtyChars(measureName) + ". Please click continue to navigate to the Measure Details page.";
    }

    public String getEditMeasureSuccessfulMessage(String measureName) {
        return "You have successfully edited measure " + StringUtility.trimTextToSixtyChars(measureName) + ". Please click continue to navigate to the Measure Library page.";
    }

    public String getCloneMeasureSuccessfulMessage(String measureName) {
        return "You have successfully cloned measure " + StringUtility.trimTextToSixtyChars(measureName) + ". Please click continue to navigate to the Measure Details page.";
    }

    public String getConvertMeasureFailureMessage() {
        return "An error occurred while converting the measure. Please try again later. If this continues please contact the mat help desk.";
    }

    public String getConvertCqlLibraryFailureMessage() {
        return "An error occurred while converting the Cql Library. Please try again later. If this continues please contact the mat help desk.";
    }

    public String getCreateNewLibrarySuccessfulMessage(String libraryName) {
        return "You have created a new library " + StringUtility.trimTextToSixtyChars(libraryName) + ". Please click continue to navigate to the CQL Composer.";
    }

    public String getVersionSuccessfulMessage(String name, String version) {
        return name + " v" + StringUtility.trimTextToSixtyChars(version) + " has been successfully created.";
    }

    public String getVersionAndPackageUnsuccessfulMessage() {
        return "Your measure could not be packaged. Select Cancel and return to the measure composer to correct any issues or select Continue to create a version without a measure package.";
    }

    public String getVersionAndPackageSuccessfulMessage(String name, String version) {
        return name + " has been successfully packaged and v" + StringUtility.trimTextToSixtyChars(version) + " has been successfully created.";
    }

    public String getUnusedIncludedLibraryWarning(String name) {
        return "You have included libraries that are unused. In order to version " + name + ", these must be removed. Select Continue to have the MAT remove these included libraries or Cancel to stop the version process.";
    }

    public static String getMeasureSuccessfullyShared(String measureName) {
        return measureName + SUCCESSFULLY_SHARED;
    }

    public static String getLibrarySuccessfullyShared(String cqlLibraryName) {
        return cqlLibraryName + SUCCESSFULLY_SHARED;
    }

    public String getSUCCESSFUL_MODIFY_APPLIED_CODE() {
        return SUCCESSFUL_MODIFY_APPLIED_CODE;
    }

    public String getVSAC_RETRIEVE_TIMEOUT() {
        return VSAC_RETRIEVE_TIMEOUT;
    }

    public String getINVALID_COMMENT_CHARACTERS() {
        return INVALID_COMMENT_CHARACTERS;
    }

    public static String getMeasureSaveServerErrorMessage(int code) {
        String pre = "Error saving: ";
        String s = "SPECIAL CODE " + code + ".";

        if (code == ConstantMessages.ID_NOT_UNIQUE) {
            s = "ID not unique.";
        }
        if (code == ConstantMessages.REACHED_MAXIMUM_VERSION) {
            s = "maximum version reached.";
        }
        if (code == ConstantMessages.REACHED_MAXIMUM_MAJOR_VERSION) {
            s = "maximum major version reached.";
        }
        if (code == ConstantMessages.REACHED_MAXIMUM_MINOR_VERSION) {
            s = "maximum minor version reached.";
        }
        if (code == ConstantMessages.INVALID_VALUE_SET_DATE) {
            s = "invalid value set date.";
        }
        return pre + s;
    }

    public String getLibraryNameIsCqlKeywordError() {
        return LIBRARY_NAME_IS_CQL_KEYWORD_ERROR;
    }

    public String getConversionBlockedWithDraftsErrorMessage() {
        return "Only one draft per measure family should be allowed.";
    }

    public String getConversionBlockedWithDraftsErrorMessageForCqlLibrary() {
        return "Only one draft per Library family should be allowed.";
    }

    public String getHarpIdRequiredMessage() {
        return HARP_ID_IS_REQUIRED;
    }

    public String getCqlLibraryDescriptionRequired() {
        return CQL_LIBRARY_DESCRIPTION_REQUIRED;
    }

    public String getCqlLibraryPublisherRequired() {
        return CQL_LIBRARY_PUBLISHER_REQUIRED;
    }

    public String getDescriptionRequired() {
        return DESCRIPTION_REQUIRED;
    }

    public String getPublisherRequired() {
        return PUBLISHER_REQUIRED;
    }
}