package mat.client.shared;

import mat.shared.ConstantMessages;

/**
 * Message store to prevent duplicated messages final String fields and their
 * getters
 * 
 * @author aschmidt
 * 
 */
public class MessageDelegate {

	/**
	 * @param code
	 * @return String
	 */
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

	/**
	 *
	 */
	private final String PACKAGE_SUCCESS = "Measure packaged successfully. Please access the Measure Library to export the measure.";

	/**
	 * @return String {@link String}
	 */
	public final String getPackageSuccessMessage() {
		return PACKAGE_SUCCESS;
	}

	/**
	 * 
	 */
	private final String UNABLE_TO_PROCESS = "System error.  Unable to process information.";

	/**
	 * @return String
	 */
	public String getUnableToProcessMessage() {
		return UNABLE_TO_PROCESS;
	}

	/*
	 * RATIO
	 */
	/**
	 * 
	 */
	private final String RATIO_WRONG_NUM = "For a Ratio measure, a grouping must contain exactly one of each of the following: "
			+ "Population, Denominator, and Numerator.";

	/**
	 * @return String
	 */
	public String getRatioWrongNumMessage() {
		return RATIO_WRONG_NUM;
	}

	/**
	 * 
	 */
	private final String RATIO_MAY_NOT_CONTAIN = "For a Ratio measure, a grouping may not contain a Denominator Exception, Measure Observation, or Measure Population.";

	/**
	 * @return String
	 */
	public String getRatioMayNotContainMessage() {
		return RATIO_MAY_NOT_CONTAIN;
	}

	/**
	 * 
	 */
	private final String RATIO_TOO_MANY = "For a Ratio measure, a grouping may not contain more than one of each of the following: Denominator Exclusion and Numerator Exclusion.";

	/**
	 * @return String
	 */
	public String getRatioTooManyMessage() {
		return RATIO_TOO_MANY;
	}

	/*
	 * PROPORTION
	 */
	/**
	 * 
	 */
	private final String PROPORTION_WRONG_NUM = "For a Proportion measure, a grouping must contain exactly one of each of the following: "
			+ "Population and Denominator.";

	/**
	 * @return String
	 */
	public String getProportionWrongNumMessage() {
		return PROPORTION_WRONG_NUM;
	}

	/**
	 * 
	 */
	private final String PROPORTION_MAY_NOT_CONTAIN = "For a Proportion measure, a grouping may not contain a Numerator Exclusion, Measure Population, or Measure Observation.";

	/**
	 * @return String
	 */
	public String getProportionMayNotContainMessage() {
		return PROPORTION_MAY_NOT_CONTAIN;
	}

	/**
	 * 
	 */
	private final String PROPORTION_TOO_FEW = "For a Proportion measure, a grouping may not contain less than one Numerator.";

	/**
	 * @return String
	 */
	public String getProportionTooFewMessage() {
		return PROPORTION_TOO_FEW;
	}

	/**
	 * 
	 */
	private final String PROPORTION_TOO_MANY = "For a Proportion measure, a grouping may not contain more than one of each of the following: "
			+ "Denominator Exclusions and Denominator Exceptions.";

	/**
	 * @return String
	 */
	public String getProportionTooManyMessage() {
		return PROPORTION_TOO_MANY;
	}

	/*
	 * CONTINUOUS VARIABLE
	 */
	/**
	 * 
	 */
	private final String CONTINUOUS_VARIABLE_WRONG_NUM = "For a Continuous Variable measure, a grouping must contain exactly one of each of the following: "
			+ "Population, Measure Population, and Measure Observation.";

	/**
	 * @return String
	 */
	public String getContinuousVariableWrongNumMessage() {
		return CONTINUOUS_VARIABLE_WRONG_NUM;
	}

	/**
	 * 
	 */
	private final String CONTINUOUS_VARIABLE_MAY_NOT_CONTAIN = "For a Continuous Variable measure, a grouping may not contain any Numerator, Numerator Exclusions, Denominator, Denominator Exclusions, or Denominator Exceptions.";

	/**
	 * @return String
	 */
	public String getContinuousVariableMayNotContainMessage() {
		return CONTINUOUS_VARIABLE_MAY_NOT_CONTAIN;
	}

	/**
	 * 
	 */
	private final String PASSWORD_REQUIRED = "Password is required.";

	/**
	 * @return String
	 */
	public String getPasswordRequiredMessage() {
		return PASSWORD_REQUIRED;
	}

	/**
	 * 
	 */
	private final String SERVER_CALL_NULL = "Server call for login returned null.";

	/**
	 * @return String
	 */
	/**
	 * @return String
	 */
	public String getServerCallNullMessage() {
		return SERVER_CALL_NULL;
	}

	/**
	 * @param str
	 * @return String
	 */
	public String getGroupedCodeListExistsMessage(String str) {
		return "Grouped Value Set " + str + " already exists.";
	}

	/**
	 * 
	 */
	private final String CODE_LIST_REQUIRED = "Value Set is required.";

	/**
	 * @return String
	 */
	public String getCodeListRequiredMessage() {
		return CODE_LIST_REQUIRED;
	}

	/**
	 * @param str
	 * @return String
	 */
	public String getCodeListAlreadyExistsForGroupedMessage(String str) {
		return "Value Set already exists for the " + str
				+ " grouped value set.";
	}

	/**
	 * @param str
	 * @return String
	 */
	public String getCodeAlreadyExistsForMessage(String str) {
		return "Code already exists for " + str + " value set.";
	}

	/**
	 * @param msg
	 * @return String
	 */
	public String getIdentityMessage(String msg) {
		return msg;
	}

	/**
	 * 
	 */
	private final String LOGIN_FAILED = "Login failed. Please sign in again.";

	/**
	 * @return String
	 */
	public String getLoginFailedMessage() {
		return LOGIN_FAILED;
	}

	/**
	 * 
	 */
	private final String LOGIN_FAILED_USER_ALREADY_LOGGED_IN = "Unable to login. Another session has been established with this account. Please verify you do not already have an open session.";

	/**
	 * @return String
	 */
	public String getLoginFailedAlreadyLoggedInMessage() {
		return LOGIN_FAILED_USER_ALREADY_LOGGED_IN;
	}

	/**
	 * 
	 */
	private final String LOGIN_FAILED_TEMP_PASSWORD_EXPIRED = "Unable to login. Your temporary password has expired. Please contact HelpDesk to renew your password.";

	/**
	 * @return String
	 */
	public String getLoginFailedTempPasswordExpiredMessage() {
		return LOGIN_FAILED_TEMP_PASSWORD_EXPIRED;
	}

	// TODO is this such a smart policy?
	// This makes denial of service attacks on accounts too easy.
	// Perhaps make them answer a security question?
	/**
	 * 
	 */
	private final String SECCOND_ATTEMPT_FAILED = "Failed 2nd attempt. Next attempt will lock the account.";

	/**
	 * @return String
	 */
	public String getSecondAttemptFailedMessage() {
		return SECCOND_ATTEMPT_FAILED;
	}

	/**
	 * 
	 */
	private final String ACCOUNT_REVOKED = "Your account has been revoked. Please contact the Helpdesk.";

	/**
	 * @return String
	 */
	public String getAccountRevokedMessage() {
		return ACCOUNT_REVOKED;
	}

	/**
	 * 
	 */
	private final String ACCOUNT_LOCKED2 = "Your account has been locked. Please contact the Helpdesk.";

	/**
	 * @return String
	 */
	public String getAccountLocked2Message() {
		return ACCOUNT_LOCKED2;
	}

	/**
	 * @param code
	 * @return String
	 */
	public String getUnknownCodeMessage(int code) {
		return "Unknown Code " + code;

	}

	/**
	 * 
	 */
	private final String SERVER_SIDE_VALIDATION = "Server Side Validation";

	/**
	 * @return String
	 */
	public String getServerSideValidationMessage() {
		return SERVER_SIDE_VALIDATION;
	}

	/**
	 * 
	 */
	private final String OID_IN_USE = "OID is already in use.";

	/**
	 * @return String
	 */
	public String getOIDInUseMessage() {
		return OID_IN_USE;
	}

	/**
	 * @param s
	 * @return String
	 */
	public String getCodeListAlreadyExistsMessage(String s) {
		return "Value Set " + s + " already exists.";
	}

	/**
	 * @param code
	 * @return String
	 */
	public String getUnknownErrorMessage(int code) {
		return "Unknown Code " + code;
	}

	/**
	 * 
	 */
	private final String EMAIL_ALREADY_EXISTS = "E-mail Address already exists.";

	/**
	 * @return String
	 */
	public String getEmailAlreadyExistsMessage() {
		return EMAIL_ALREADY_EXISTS;
	}

	/**
	 * 
	 */
	private final String DUPLICATE_CODES_MSG = "All code(s) were identified as duplicates to code(s) already in the value set and were ignored upon import.";

	/**
	 * @return String
	 */
	public String getDuplicateCodesMessage() {
		return DUPLICATE_CODES_MSG;
	}

	/**
	 * 
	 */
	private final String INVALID_TEMPLATE = "Import failed. Invalid template.";

	/**
	 * @return String
	 */
	public String getInvalidTemplateMessage() {
		return INVALID_TEMPLATE;
	}

	/**
	 * 
	 */
	private final String SYSTEM_ERROR = "Import failed due to system error. Please try again.";

	/**
	 * @return String
	 */
	public String getSystemErrorMessage() {
		return SYSTEM_ERROR;
	}

	/**
	 * 
	 */
	private final String EMPTY_FILE_ERROR = "Import failed. File is empty.";

	/**
	 * @return String
	 */
	public String getEmptyFileError() {
		return EMPTY_FILE_ERROR;
	}

	/**
	 * 
	 */
	private final String DUPLICATE_ERROR = "Import failed. One or more duplicate codes exist in file. Please remove then try again.";

	/**
	 * @return String
	 */
	public String getDuplicateErrorMessage() {
		return DUPLICATE_ERROR;
	}

	/**
	 * 
	 */
	private final String SECURITY_QUESTION_HEADER = "The answer(s) provided to the security question(s) do not match the following rules:";

	/**
	 * @return String
	 */
	public String getSecurityQuestionHeeaderMessage() {
		return SECURITY_QUESTION_HEADER;
	}

	/**
	 * 
	 */
	private final String NOT_UNIQUE_QUESTIONS = "You cannot use the same security question more than once.";

	/**
	 * @return String
	 */
	public String getNotUniqueQuestions() {
		return NOT_UNIQUE_QUESTIONS;
	}

	/**
	 * @param index
	 * @return String
	 */
	public String getSecurityAnswerTooShortMessage(int index) {
		return "Security Answer " + index + " is too short.";
	}

	/**
	 * 
	 */
	private final String MUST_NOT_CONTAIN_RUNS = "Passwords must not contain three consecutive identical characters.";

	/**
	 * @return String
	 */
	public String getMustNotContainRunsMessage() {
		return MUST_NOT_CONTAIN_RUNS;
	}

	/**
	 * 
	 */
	private final String MUST_NOT_CONTAIN_DICTIONAY_WORD = "Passwords must not consist of a single dictionary word with letters, numbers and symbols.";

	/**
	 * @return String
	 */
	public String getMustNotContainDictionaryWordMessage() {
		return MUST_NOT_CONTAIN_DICTIONAY_WORD;
	}

	/**
	 * 
	 */
	private final String MUST_NOT_CONTAIN_LOGIN_ID = "Passwords must not contain your User ID.";

	/**
	 * @return String
	 */
	public String getMustNotContainLoginIdMessage() {
		return MUST_NOT_CONTAIN_LOGIN_ID;
	}

	/**
	 * 
	 */
	private final String MUST_CONTAIN_SPECIAL = "Must contain a special character.";

	/**
	 * @return String
	 */
	public String getMustContainSpecialMessage() {
		return MUST_CONTAIN_SPECIAL;
	}

	/**
	 * 
	 */
	private final String MUST_CONTAIN_NUMBER = "Must contain a number.";

	/**
	 * @return String
	 */
	public String getMustContainNumberMessage() {
		return MUST_CONTAIN_NUMBER;
	}

	/**
	 * 
	 */
	private final String MUST_CONTAIN_LOWER = "Must contain a lowercase letter.";

	/**
	 * @return String
	 */
	public String getMustContainLowerMessage() {
		return MUST_CONTAIN_LOWER;
	}

	/**
	 * 
	 */
	private final String MUST_CONTAIN_UPPER = "Must contain an uppercase letter.";

	/**
	 * @return String
	 */
	public String getMustContainUpperMessage() {
		return MUST_CONTAIN_UPPER;
	}

	/**
	 * 
	 */
	private final String PASSWORD_WRONG_LENGTH = "Must be between 8 and 16 characters long.";

	/**
	 * @return String
	 */
	public String getPasswordWrongLengthMessage() {
		return PASSWORD_WRONG_LENGTH;
	}

	/**
	 * 
	 */
	private final String PASSWORD_MISMATCH = "Your new password and confirm password do not match.";

	/**
	 * @return String
	 */
	public String getPasswordMismatchMessage() {
		return PASSWORD_MISMATCH;
	}

	/**
	 * 
	 */
	private final String DOESNT_FOLLOW_RULES = "The new password you entered does not match the following rules:";

	/**
	 * @return String
	 */
	public String getDoesntFollowRulesMessage() {
		return DOESNT_FOLLOW_RULES;
	}

	/**
	 * 
	 */
	private final String UNKNOWN_FAIL = "Unknown failure reason";

	/**
	 * @return String
	 */
	public String getUnknownFailMessage() {
		return UNKNOWN_FAIL;
	}

	/**
	 * 
	 */
	private final String USER_NOT_FOUND_MSG = "User ID is required.";

	/**
	 * @return String
	 */
	public String getUserNotFoundMessage() {
		return USER_NOT_FOUND_MSG;
	}

	/**
	 * 
	 */
	private final String EMAIL_NOT_FOUND_MSG = "Email Address is required.";

	/**
	 * @return String
	 */
	public String getEmailNotFoundMessage() {
		return EMAIL_NOT_FOUND_MSG;
	}

	/**
	 * 
	 */
	private final String ACCOUNT_LOCKED = "Your account has been locked.Please Contact Helpdesk.";

	/**
	 * @return String
	 */
	public String getAccountLockedMessage() {
		return ACCOUNT_LOCKED;
	}

	/**
	 * 
	 */
	private final String ACCOUNT_TERMINATION = "Your account has been Terminated.Please Contact Helpdesk for more information.";

	/**
	 * @return String
	 */
	public String getAccountTermination() {
		return ACCOUNT_TERMINATION;
	}

	/**
	 * 
	 */
	private final String SECURITY_Q_MISMATCH = "The answer for the security question did not match.";

	/**
	 * @return String
	 */
	public String getSecurityQMismatchMessage() {
		return SECURITY_Q_MISMATCH;
	}

	/**
	 * 
	 */
	private final String SECURITY_NOT_ANSWERED = "Your security questions have not been answered.  You cannot continue. Please contact the Helpdesk";

	/**
	 * @return String
	 */
	public String getSecurityNotAnsweredMessage() {
		return SECURITY_NOT_ANSWERED;
	}

	/**
	 * 
	 */
	private final String EMAIL_MISMATCH = "Email did not match with the User ID.  Please Contact the Administrator.";

	/**
	 * @return String
	 */
	public String getEmailMismatchMessage() {
		return EMAIL_MISMATCH;
	}

	/**
	 * 
	 */
	private final String PHONE_10_DIGIT = "Phone Number is required to be 10 digits.";

	/**
	 * @return String
	 */
	public String getPhoneTenDigitMessage() {
		return PHONE_10_DIGIT;
	}

	/**
	 * 
	 */
	private final String EXCEL_FILE_TYPE = "Please select a file with an Excel file type (.xls or .xlsx)";

	/**
	 * @return String
	 */
	public String getExcelFileTypeMessage() {
		return EXCEL_FILE_TYPE;
	}

	/**
	 * 
	 */
	private final String FILE_NOT_SELECTED = "Please select a file.";

	/**
	 * @return String
	 */
	public String getFileNotSelectedMessage() {
		return FILE_NOT_SELECTED;
	}

	/**
	 * 
	 */
	private final String CODE_VERSION_REQUIRED = "Code System Version is required.";

	/**
	 * @return String
	 */
	public String getCodeVersionRequiredMessage() {
		return CODE_VERSION_REQUIRED;
	}

	/**
	 * 
	 */
	private final String CODE_SYSTEM_REQUIRED = "Code System is required.";

	/**
	 * @return String
	 */
	public String getCodeSystemRequiredMessage() {
		return CODE_SYSTEM_REQUIRED;
	}

	/**
	 * 
	 */
	private final String CATEGORY_REQUIRED = "Category is required.";

	/**
	 * @return String
	 */
	public String getCategoryRequiredMessage() {
		return CATEGORY_REQUIRED;
	}

	/**
	 * 
	 */
	private final String STEWARD_REQUIRED = "Steward is required.";

	/**
	 * @return String
	 */
	public String getStewardRequiredMessage() {
		return STEWARD_REQUIRED;
	}

	/**
	 * 
	 */
	private final String DESCRIPTOR_REQUIRED = "Descriptor is required.";

	/**
	 * @return String
	 */
	public String getDescriptorRequiredMessage() {
		return DESCRIPTOR_REQUIRED;
	}

	/**
	 * 
	 */
	private final String CODE_REQUIRED = "Code is required.";

	/**
	 * @return String
	 */
	public String getCodeRequiredMessage() {
		return CODE_REQUIRED;
	}

	/**
	 * 
	 */
	private final String GROUPING_REQUIRED = "A Grouping is required.";

	/**
	 * @return String
	 */
	public String getGroupingRequiredMessage() {
		return GROUPING_REQUIRED;
	}

	/**
	 * 
	 */
	private final String RATIONALE_REQUIRED = "Rationale is required.";

	/**
	 * @return String
	 */
	public String getRationaleRequiredMessage() {
		return RATIONALE_REQUIRED;
	}

	/**
	 * 
	 */
	private final String DESCRIPTION_REQUIRED = "Description is required.";

	/**
	 * @return String
	 */
	public String getDescriptionRequiredMeassage() {
		return DESCRIPTION_REQUIRED;
	}

	/**
	 * 
	 */
	private final String MEASURE_NAME_REQUIRED = "Measure Name is required.";

	/**
	 * @return String
	 */
	public String getMeasureNameRequiredMessage() {
		return MEASURE_NAME_REQUIRED;
	}

	/**
	 * 
	 */
	private final String ABV_NAME_REQUIRED = "Abbreviated Name is required.";

	/**
	 * @return String
	 */
	public String getAbvNameRequiredMessage() {
		return ABV_NAME_REQUIRED;
	}

	/**
	 * 
	 */
	private final String GROUPED_NAME_REQUIRED = "Grouped Value Set Name is required.";

	/**
	 * @return String
	 */
	public String getGroupedNameRequiredMessage() {
		return GROUPED_NAME_REQUIRED;
	}

	/**
	 * 
	 */
	private final String NAME_REQUIRED = "Value Set Name is required.";

	public String getNameRequiredMessage() {
		return NAME_REQUIRED;
	}

	/**
	 * 
	 */
	public static final String ONE_AND = "A Clause should start with only one AND.";

	/**
	 * @return String
	 */
	public String getOneAndMessage() {
		return ONE_AND;
	}

	/**
	 * 
	 */
	public static final String NO_MEASURES = "No Measures returned. Please search again.";

	/**
	 * @return String
	 */
	public String getNoMeasuresMessage() {
		return NO_MEASURES;
	}

	/**
	 * 
	 */
	public static final String NO_CODE_LISTS = "No Value Sets returned. Please search again.";

	/**
	 * @return String
	 */
	public String getNoCodeListsMessage() {
		return NO_CODE_LISTS;
	}

	/**
	 * 
	 */
	public static final String ROOT_OID_TOO_LONG = "Root OID cannot exceed 50 characters.";

	/**
	 * @return String
	 */
	public String getRootOIDTooLongMessage() {
		return ROOT_OID_TOO_LONG;
	}

	/**
	 * 
	 */
	public static final String OID_TOO_LONG = "OID cannot exceed 50 characters.";

	/**
	 * @return String
	 */
	public String getOIDTooLongMessage() {
		return OID_TOO_LONG;
	}

	/**
	 * 
	 */
	public static final String ROOT_OID_REQUIRED = "Root OID is required.";

	/**
	 * @return String
	 */
	public String getRootOIDRequiredMessage() {
		return ROOT_OID_REQUIRED;
	}

	/**
	 * 
	 */
	public static final String OID_REQUIRED = "OID is required.";

	/**
	 * @return String
	 */
	public String getOIDRequiredMessage() {
		return OID_REQUIRED;
	}

	/**
	 * 
	 */
	public static final String ORG_REQUIRED = "Organization is required.";

	/**
	 * @return String
	 */
	public String getOrgRequiredMessage() {
		return ORG_REQUIRED;
	}

	/**
	 * 
	 */
	public static final String PHONE_REQUIRED = "Phone Number is required.";

	/**
	 * @return String
	 */
	public String getPhoneRequiredMessage() {
		return PHONE_REQUIRED;
	}

	/**
	 * 
	 */
	public static final String LOGIN_ID_REQUIRED = "User ID is required.";

	/**
	 * @return String
	 */
	public String getLoginIDRequiredMessage() {
		return LOGIN_ID_REQUIRED;
	}

	/**
	 * 
	 */
	public static final String LOGIN_USER_REQUIRED = "User Name is required.";

	/**
	 * @return String
	 */
	public String getLoginUserRequiredMessage() {
		return LOGIN_USER_REQUIRED;
	}

	/**
	 * 
	 */
	public static final String LAST_NAME_REQUIRED = "Last Name is required.";

	/**
	 * @return String
	 */
	public String getLastNameRequiredMessage() {
		return LAST_NAME_REQUIRED;
	}

	/**
	 * 
	 */
	public static final String FIRST_NAME_MIN = "First Name must be at least two characters.";

	/**
	 * @return String
	 */
	public String getFirstMinMessage() {
		return FIRST_NAME_MIN;
	}

	/**
	 * 
	 */
	public static final String FIRST_NAME_REQUIRED = "First Name is required.";

	/**
	 * @return String
	 */
	public String getFirstNameRequiredMessage() {
		return FIRST_NAME_REQUIRED;
	}

	/**
	 * 
	 */
	public static final String ALERT_LOADING_MESSAGE = "Please wait until loading is complete.";

	/**
	 * @return String
	 */
	public String getAlertLoadingMessage() {
		return ALERT_LOADING_MESSAGE;
	}

	/**
	 * 
	 */
	public static final String TEMP_EMAIL_SENT = "Temporary Password E-mail has been sent.";

	/**
	 * @return String
	 */
	public String getTempEmailSentMessage() {
		return TEMP_EMAIL_SENT;
	}

	/**
	 * 
	 */
	public static final String CHANGES_SAVED = "Changes are successfully saved.";

	/**
	 * @return String
	 */
	public String getChangesSavedMessage() {
		return CHANGES_SAVED;
	}

	/**
	 * 
	 */
	public static final String CODE_LIST_ADDED = "Code has been successfully added to the value set.";

	/**
	 * @return String
	 */
	public String getCodeListAddedMessage() {
		return CODE_LIST_ADDED;
	}

	/**
	 * 
	 */
	public static final String CODE_LIST_ADDED_GROUP = "Value Set has been successfully added to the grouped value set.";

	/**
	 * @return String
	 */
	public String getCodeListAddedGroupMessage() {
		return CODE_LIST_ADDED_GROUP;
	}

	/**
	 * 
	 */
	public static final String IMPORT_SUCCESS = "Import Successful.";

	/**
	 * @return String
	 */
	public String getImportSuccessMessage() {
		return IMPORT_SUCCESS;
	}

	// public static final String x = "";
	// ASK STAN
	/**
	 * @param codes
	 * @return String
	 */
	public String getImportSuccessMessage(int codes) {
		String msg = "";
		if (codes == 1) {
			msg = "Import Successful.  1 code was identified as duplicate to code(s) already in the value set and was ignored upon import.";
		} else {
			msg = "Import Successful.  "
					+ codes
					+ " codes were identified as duplicates to code(s) already in the value set and were ignored upon import.";
		}
		return msg;
	}

	/**
	 * @param codeListName
	 * @param dataType
	 * @return String
	 */
	public String getQDMSuccessMessage(String codeListName, String dataType) {
		return " The QDM Element " + codeListName + ": " + dataType
				+ " has been added successfully.";
	}

	/**
	 * @param codeListName
	 * @param dataType
	 * @param occurrenceMessage
	 * @return String
	 */
	public String getQDMOcurrenceSuccessMessage(String codeListName,
			String dataType, String occurrenceMessage) {
		return " The QDM Element " + occurrenceMessage + " of " + codeListName
				+ ": " + dataType + " has been added successfully.";
	}

	/**
	 * 
	 */
	public static final String GROUPING_SAVED = "Grouping has been saved.";

	/**
	 * @return String
	 */
	public String getGroupingSavedMessage() {
		return GROUPING_SAVED;
	}

	/**
	 * 
	 */
	public static final String SUPP_DATA_SAVED = "Supplemental data elements have been saved.";

	/**
	 * @return String
	 */
	public String getSuppDataSavedMessage() {
		return SUPP_DATA_SAVED;
	}

	/**
	 * 
	 */
	public static final String PASSWORD_CHANGED = "Your password has been changed.";

	public String getPasswordSavedMessage() {
		return PASSWORD_CHANGED;
	}

	/**
	 * 
	 */
	public static final String SEC_QUESTIONS_UPDATED = "Your security questions have been updated.";

	/**
	 * @return String
	 */
	public String getSecurityQuestionsUpdatedMessage() {
		return SEC_QUESTIONS_UPDATED;
	}

	/**
	 * 
	 */
	public static final String PERSONAL_INFO_UPDATED = "Your personal information has been updated.";

	/**
	 * @return String
	 */
	public String getPersonalInfoUpdatedMessage() {
		return PERSONAL_INFO_UPDATED;
	}

	// US - 421
	/**
	 * 
	 */
	public static final String s_ERR_MEASURE_SCORE_REQUIRED = "Measure Scoring is required.";
	/**
	 * 
	 */
	public static final String s_ERR_RETRIEVE_SCORING_CHOICES = "Problem while retrieving measure scoring choices.";

	// US 62
	/**
	 * 
	 */
	public static final String s_ERR_RETRIEVE_UNITS = "Problem while retrieving units.";

	// US 602
	/**
	 * 
	 */
	public static final String s_ERR_RETRIEVE_STATUS = "Problem while retrieving status.";

	// US 171
	/**
	 * 
	 */
	public static final String s_ERR_RETRIEVE_OPERATOR = "Problem while retrieving operator.";
	// US - 502

	/**
	 * 
	 */
	private final String VALUE_SET_DATE_REQUIRED = "Value Set Package Date is required.";

	/**
	 * @return String
	 */
	public String getValueSetDateRequiredMessage() {
		return VALUE_SET_DATE_REQUIRED;
	}

	/**
	 * 
	 */
	private final String VALUE_SET_DATE_INVALID = "Value Set Package Date is not a valid date.";

	/**
	 * @return String
	 */
	public String getValueSetDateInvalidMessage() {
		return VALUE_SET_DATE_INVALID;
	}

	/**
	 * 
	 */
	public static final int DRAFT = 0;
	/**
	 * 
	 */
	public static final int COMPLETE = 1;

	/**
	 * 
	 */
	public static final String VALUE_SET_DRAFT_SAVED = "Value Set successfully saved as a draft.";
	/**
	 * 
	 */
	public static final String VALUE_SET_COMPLETE_SAVED = "Value Set successfully saved as complete.";

	/**
	 * @param mode
	 * @return String
	 */
	public String getValueSetChangedSavedMessage(int mode) {
		if (mode == DRAFT) {
			return VALUE_SET_DRAFT_SAVED;
		} else if (mode == COMPLETE) {
			return VALUE_SET_COMPLETE_SAVED;
		} else {
			return "";
		}
	}

	/**
	 * 
	 */
	public static final String GROUPED_VALUE_SET_DRAFT_SAVED = "Grouped Value Set successfully saved as a draft.";
	/**
	 * 
	 */
	public static final String GROUPED_VALUE_SET_COMPLETE_SAVED = "Grouped Value Set successfully saved as complete.";

	/**
	 * @param mode
	 * @return String
	 */
	public String getGroupedValueSetChangedSavedMessage(int mode) {
		if (mode == DRAFT) {
			return GROUPED_VALUE_SET_DRAFT_SAVED;
		} else if (mode == COMPLETE) {
			return GROUPED_VALUE_SET_COMPLETE_SAVED;
		} else {
			return "";
		}
	}

	/**
	 * 
	 */
	private final String INVALID_LAST_MODIFIED_DATE = "Invalid Last Modified Date.";

	/**
	 * @return String
	 */
	public String getInvalidLastModifiedDateMessage() {
		return INVALID_LAST_MODIFIED_DATE;
	}

	/**
	 * 
	 */
	private final String LAST_MODIFIED_DATE_NOT_UNIQUE = "The Last Modified date and time entered is already is use for this value set.";

	/**
	 * @return String
	 */
	public String getLastModifiedDateNotUniqueMessage() {
		return LAST_MODIFIED_DATE_NOT_UNIQUE;
	}

	/**
	 * 
	 */
	private final String GENERIC_ERROR_MESSAGE = "The Measure Authoring Tool was unable to process the request. Please try again. If the problem persists please contact the Help Desk.";

	/**
	 * @return String
	 */
	public String getGenericErrorMessage() {
		return GENERIC_ERROR_MESSAGE;
	}

	/**
	 * 
	 */
	private final String PASSWORD_REQUIRED_ERROR_MESSAGE = "Existing password is required to confirm changes.";

	/**
	 * @return String
	 */
	public String getPasswordRequiredErrorMessage() {
		return PASSWORD_REQUIRED_ERROR_MESSAGE;
	}

	/**
	 * 
	 */
	private final String PASSWORD_MISMATCH_ERROR_MESSAGE = "Incorrect password supplied.Try again";

	/**
	 * @return String
	 */
	public String getPasswordMismatchErrorMessage() {
		return PASSWORD_MISMATCH_ERROR_MESSAGE;
	}

	/**
	 * 
	 */
	private final String ALL_PASSWORD_FIELDS_REQUIRED = "All password fields are required.";

	/**
	 * @return String
	 */
	public String getAllPasswordFieldsRequired() {
		return ALL_PASSWORD_FIELDS_REQUIRED;
	}

	/**
	 * 
	 */
	private final String MEASURE_SELECTION_ERROR = "Please select at least one measure";

	/**
	 * @return String
	 */
	public String getMeasureSelectionError() {
		return MEASURE_SELECTION_ERROR;
	}

	/**
	 * 
	 */
	private final String TRANSFER_CHECKBOX_ERROR = "Please select at least one Value Set to transfer ownership.";

	/**
	 * @return String
	 */
	public String getTransferCheckBoxError() {
		return TRANSFER_CHECKBOX_ERROR;
	}

	/**
	 * 
	 */
	private final String TRANSFER_CHECKBOX_ERROR_MEASURE = "Please select at least one Measure to transfer ownership.";

	/**
	 * @return String
	 */
	public String getTransferCheckBoxErrorMeasure() {
		return TRANSFER_CHECKBOX_ERROR_MEASURE;
	}

	/**
	 * 
	 */
	private final String TRANSFER_OWNERSHIP_SUCCESS = "Ownership successfully transferred to ";

	/**
	 * @return String
	 */
	public String getTransferOwnershipSuccess() {
		return TRANSFER_OWNERSHIP_SUCCESS;
	}


	/**
	 * 
	 */
	private final String SAVE_ERROR_MSG = "You have unsaved changes that will be discarded if you continue. "
			+ "Do you want to continue without saving?";

	/**
	 * @return String the sAVE_ERROR_MSG
	 */
	public String getSaveErrorMsg() {
		return SAVE_ERROR_MSG;
	}

	/**
	 * 
	 */
	private final String RELATIONALOP_TWO_CHILD_MESSAGE = "Package Failed. Measure logic is incomplete.LHS and RHS are required for Timings and Relationships.";

	/**
	 * @return String
	 */
	public String getRelationalOpTwoChildMessage() {
		return RELATIONALOP_TWO_CHILD_MESSAGE;
	}

	/**
	 * 
	 */
	private final String MEASURE_DELETION_INVALID_PWD = "The entered password is invalid. Please try again.";

	/**
	 * @return String
	 */
	public String getMeasureDeletionInvalidPwd() {
		return MEASURE_DELETION_INVALID_PWD;
	}

	/**
	 * 
	 */
	private final String MEASURE_DELETION_SUCCESS_MSG = "Measure successfully deleted.";

	/**
	 * @return String
	 */
	public String getMeasureDeletionSuccessMgs() {
		return MEASURE_DELETION_SUCCESS_MSG;
	}

	/**
	 * 
	 */
	private final String DUPLICATE_APPLIED_QDM = "Value set with selected datatype already exists in applied elements.";

	/**
	 * @return String
	 */
	public String getDuplicateAppliedQDMMsg() {
		return DUPLICATE_APPLIED_QDM;
	}

	/**
	 * 
	 */
	private final String SUCCESSFUL_MODIFY_APPLIED_QDM = "Selected QDM element has been modified successfully";

	/**
	 * @return String
	 */
	public String getSuccessfulModifyQDMMsg() {
		return SUCCESSFUL_MODIFY_APPLIED_QDM;

	}

	/**
	 * 
	 */
	private final String USER_REQUIRED_ERROR_MSG = "Please select a user to transfer ownership.";

	/**
	 * @return String
	 */
	public String getUserRequiredErrorMessage() {
		return USER_REQUIRED_ERROR_MSG;

	}

	/**
	 * 
	 */
	private final String MEASURE_NOTES_REQUIRED_MESSAGE = "Text required in Title and Description fields.";

	/**
	 * @return String String
	 */
	public String getMEASURE_NOTES_REQUIRED_MESSAGE() {
		return MEASURE_NOTES_REQUIRED_MESSAGE;
	}

	/**
	 * 
	 */
	private final String MEASURE_NOTES_DELETE_SUCCESS_MESSAGE = "The measure note deleted successfully.";

	/**
	 * @return String
	 */
	public String getMEASURE_NOTES_DELETE_SUCCESS_MESSAGE() {
		return MEASURE_NOTES_DELETE_SUCCESS_MESSAGE;
	}

	/**
	 * 
	 */
	private final String MEASURE_NOTES_SAVE_SUCCESS_MESSAGE = "The measure note saved successfully.";

	/**
	 * @return String
	 */
	public String getMEASURE_NOTES_SAVE_SUCCESS_MESSAGE() {
		return MEASURE_NOTES_SAVE_SUCCESS_MESSAGE;
	}

	/**
	 * 
	 */
	private final String UMLS_SUCCESSFULL_LOGIN = "Successfully logged into UMLS";

	/**
	 * @return String
	 */
	public String getUMLS_SUCCESSFULL_LOGIN() {
		return UMLS_SUCCESSFULL_LOGIN;
	}

	/**
	 * 
	 */
	private final String UML_LOGIN_UNAVAILABLE = "Unable to verify your UMLS credentials. Please contact the MAT Help Desk or try again.";

	/**
	 * @return String
	 */
	public String getUML_LOGIN_UNAVAILABLE() {
		return UML_LOGIN_UNAVAILABLE;
	}

	/**
	 * 
	 */
	private final String UML_LOGIN_FAILED = "Login failed. Please sign in again.";

	/**
	 * @return String
	 */
	public String getUML_LOGIN_FAILED() {
		return UML_LOGIN_FAILED;
	}

	/**
	 * 
	 */
	private final String UMLS_NOT_LOGGEDIN = "You are not logged in to UMLS. Please access the UMLS Account tab to continue.";

	/**
	 * @return String
	 */
	public String getUMLS_NOT_LOGGEDIN() {
		return UMLS_NOT_LOGGEDIN;
	}

	/**
	 * 
	 */
	private final String UMLS_OID_REQUIRED = "Please enter an OID.";

	/**
	 * @return String
	 */
	public String getUMLS_OID_REQUIRED() {
		return UMLS_OID_REQUIRED;
	}

	/**
	 * 
	 */
	private final String VSAC_RETRIEVE_FAILED = "Unable to retrieve from VSAC. Please check the data and try again.";

	/**
	 * @return String
	 */
	public String getVSAC_RETRIEVE_FAILED() {
		return VSAC_RETRIEVE_FAILED;
	}

	/**
	 * 
	 */
	private final String VSAC_UPDATE_SUCCESSFULL = "Successfully updated applied QDM list with VSAC data.";

	/**
	 * @return String
	 */
	public String getVSAC_UPDATE_SUCCESSFULL() {
		return VSAC_UPDATE_SUCCESSFULL;
	}

	/**
	 * 
	 */
	private final String DELETE_MEASURE_WARNING_MESSAGE = "Deleting a draft or version of a measure will"
			+ "permanently remove the designated measure draft or "
			+ "version from  the Measure Authoring Tool. Deleted measures cannot <br> be recovered.";

	/**
	 * @return String the dELETE_MEASURE_WARNING_MESSAGE
	 */
	public String getDELETE_MEASURE_WARNING_MESSAGE() {
		return DELETE_MEASURE_WARNING_MESSAGE;
	}

	/**
	 * @return String the mEASURE_PACKAGE_UMLS_NOT_LOGGED_IN
	 */
	public String getMEASURE_PACKAGE_UMLS_NOT_LOGGED_IN() {
		return MEASURE_PACKAGE_UMLS_NOT_LOGGED_IN;
	}

	/**
	 *
	 */
	private final String MEASURE_PACKAGE_UMLS_NOT_LOGGED_IN = "Measure packaged successfully. "
			+ "Value set data is not included in the measure package as you are not logged into UMLS.";

	/**
	 * 
	 */
	private final String VALIDATION_MSG_ELEMENT_WITHOUT_VSAC = "Please enter name and select a datatype associated with it.";

	/**
	 * @return String
	 */
	public String getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC() {
		return VALIDATION_MSG_ELEMENT_WITHOUT_VSAC;
	}

	/**
	 * 
	 */
	private final String VALIDATION_MSG_DATA_TYPE_VSAC = "Please select datatype from drop down list.";

	/**
	 * @return String
	 */
	public String getVALIDATION_MSG_DATA_TYPE_VSAC() {
		return VALIDATION_MSG_DATA_TYPE_VSAC;
	}

	/**
	 * @return String the cLAUSE_WORK_SPACE_VALIDATION_ERROR
	 */
	public String getCLAUSE_WORK_SPACE_VALIDATION_ERROR() {
		return CLAUSE_WORK_SPACE_VALIDATION_ERROR;
	}

	/**
	 * 
	 */
	private final String CLAUSE_WORK_SPACE_VALIDATION_ERROR = "Measure logic is incomplete."
			+ "LHS and RHS are required for Timings and Relationships.";

	/**
	 * 
	 */
	private final String CLAUSE_WORK_SPACE_VALIDATION_SUCCESS = "Measure logic validation successful.";

	/**
	 * @return String the cLAUSE_WORK_SPACE_VALIDATION_SUCCESS
	 */
	public String getCLAUSE_WORK_SPACE_VALIDATION_SUCCESS() {
		return CLAUSE_WORK_SPACE_VALIDATION_SUCCESS;
	}

	/**
	 * 
	 */
	private final String MODIFY_QDM_SELECT_ATLEAST_ONE = "Please select atleast one applied QDM to modify.";

	/**
	 * @return String the mODIFY_QDM_SELECT_ATLEAST_ONE
	 */
	public String getMODIFY_QDM_SELECT_ATLEAST_ONE() {
		return MODIFY_QDM_SELECT_ATLEAST_ONE;
	}

	/**
	 * 
	 */
	private final String MODIFY_QDM_ATTRIBUTE_VALIDATION = "A value set with an Attribute category must be used for this data element.";

	/**
	 * @return String the mODIFY_QDM_ATTRIBUTE_VALIDATION
	 * 
	 */
	public String getMODIFY_QDM_ATTRIBUTE_VALIDATION() {
		return MODIFY_QDM_ATTRIBUTE_VALIDATION;
	}

	/**
	 * 
	 */
	private final String MODIFY_QDM_NON_ATTRIBUTE_VALIDATION = "A value set with a non-Attribute category must be used for this data element.";

	/**
	 * @return String the mODIFY_QDM_NON_ATTRIBUTE_VALIDATION
	 */
	public String getMODIFY_QDM_NON_ATTRIBUTE_VALIDATION() {
		return MODIFY_QDM_NON_ATTRIBUTE_VALIDATION;
	}
}
