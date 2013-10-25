package mat.client.shared;

import mat.shared.ConstantMessages;

/**
 * Message store to prevent duplicated messages final String fields and their
 * getters.
 * 
 * @author aschmidt
 */
public class MessageDelegate {

	/**
	 * Gets the measure save server error message.
	 * 
	 * @param code
	 *            the code
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

	/** The package success. */
	private final String PACKAGE_SUCCESS = "Measure packaged successfully. Please access the Measure Library to export the measure.";

	/**
	 * Gets the package success message.
	 * 
	 * @return String {@link String}
	 */
	public final String getPackageSuccessMessage() {
		return PACKAGE_SUCCESS;
	}

	/** The unable to process. */
	private final String UNABLE_TO_PROCESS = "System error.  Unable to process information.";

	/**
	 * Gets the unable to process message.
	 * 
	 * @return String
	 */
	public String getUnableToProcessMessage() {
		return UNABLE_TO_PROCESS;
	}

	/*
	 * RATIO
	 */
	/** The ratio wrong num. */
	private final String RATIO_WRONG_NUM = "For a Ratio measure, a grouping must contain exactly one of each of the following: "
			+ "Population, Denominator, and Numerator.";

	/**
	 * Gets the ratio wrong num message.
	 * 
	 * @return String
	 */
	public String getRatioWrongNumMessage() {
		return RATIO_WRONG_NUM;
	}

	/** The ratio may not contain. */
	private final String RATIO_MAY_NOT_CONTAIN = "For a Ratio measure, a grouping may not contain a Denominator Exception, Measure Observation, or Measure Population.";

	/**
	 * Gets the ratio may not contain message.
	 * 
	 * @return String
	 */
	public String getRatioMayNotContainMessage() {
		return RATIO_MAY_NOT_CONTAIN;
	}

	/** The ratio too many. */
	private final String RATIO_TOO_MANY = "For a Ratio measure, a grouping may not contain more than one of each of the following: Denominator Exclusion and Numerator Exclusion.";

	/**
	 * Gets the ratio too many message.
	 * 
	 * @return String
	 */
	public String getRatioTooManyMessage() {
		return RATIO_TOO_MANY;
	}

	/*
	 * PROPORTION
	 */
	/** The proportion wrong num. */
	private final String PROPORTION_WRONG_NUM = "For a Proportion measure, a grouping must contain exactly one of each of the following: "
			+ "Population and Denominator.";

	/**
	 * Gets the proportion wrong num message.
	 * 
	 * @return String
	 */
	public String getProportionWrongNumMessage() {
		return PROPORTION_WRONG_NUM;
	}

	/** The proportion may not contain. */
	private final String PROPORTION_MAY_NOT_CONTAIN = "For a Proportion measure, a grouping may not contain a Numerator Exclusion, Measure Population, or Measure Observation.";

	/**
	 * Gets the proportion may not contain message.
	 * 
	 * @return String
	 */
	public String getProportionMayNotContainMessage() {
		return PROPORTION_MAY_NOT_CONTAIN;
	}

	/** The proportion too few. */
	private final String PROPORTION_TOO_FEW = "For a Proportion measure, a grouping may not contain less than one Numerator.";

	/**
	 * Gets the proportion too few message.
	 * 
	 * @return String
	 */
	public String getProportionTooFewMessage() {
		return PROPORTION_TOO_FEW;
	}

	/** The proportion too many. */
	private final String PROPORTION_TOO_MANY = "For a Proportion measure, a grouping may not contain more than one of each of the following: "
			+ "Denominator Exclusions and Denominator Exceptions.";

	/**
	 * Gets the proportion too many message.
	 * 
	 * @return String
	 */
	public String getProportionTooManyMessage() {
		return PROPORTION_TOO_MANY;
	}

	/*
	 * CONTINUOUS VARIABLE
	 */
	/** The continuous variable wrong num. */
	private final String CONTINUOUS_VARIABLE_WRONG_NUM = "For a Continuous Variable measure, a grouping must contain exactly one of each of the following: "
			+ "Population, Measure Population, and Measure Observation.";

	/**
	 * Gets the continuous variable wrong num message.
	 * 
	 * @return String
	 */
	public String getContinuousVariableWrongNumMessage() {
		return CONTINUOUS_VARIABLE_WRONG_NUM;
	}

	/** The continuous variable may not contain. */
	private final String CONTINUOUS_VARIABLE_MAY_NOT_CONTAIN = "For a Continuous Variable measure, a grouping may not contain any Numerator, Numerator Exclusions, Denominator, Denominator Exclusions, or Denominator Exceptions.";

	/**
	 * Gets the continuous variable may not contain message.
	 * 
	 * @return String
	 */
	public String getContinuousVariableMayNotContainMessage() {
		return CONTINUOUS_VARIABLE_MAY_NOT_CONTAIN;
	}

	/** The password required. */
	private final String PASSWORD_REQUIRED = "Password is required.";

	/**
	 * Gets the password required message.
	 * 
	 * @return String
	 */
	public String getPasswordRequiredMessage() {
		return PASSWORD_REQUIRED;
	}

	/** The server call null. */
	private final String SERVER_CALL_NULL = "Server call for login returned null.";

	/**
	 * Gets the server call null message.
	 * 
	 * @return String
	 */
	/**
	 * @return String
	 */
	public String getServerCallNullMessage() {
		return SERVER_CALL_NULL;
	}

	/**
	 * Gets the grouped code list exists message.
	 * 
	 * @param str
	 *            the str
	 * @return String
	 */
	public String getGroupedCodeListExistsMessage(String str) {
		return "Grouped Value Set " + str + " already exists.";
	}

	/** The code list required. */
	private final String CODE_LIST_REQUIRED = "Value Set is required.";

	/**
	 * Gets the code list required message.
	 * 
	 * @return String
	 */
	public String getCodeListRequiredMessage() {
		return CODE_LIST_REQUIRED;
	}

	/**
	 * Gets the code list already exists for grouped message.
	 * 
	 * @param str
	 *            the str
	 * @return String
	 */
	public String getCodeListAlreadyExistsForGroupedMessage(String str) {
		return "Value Set already exists for the " + str
				+ " grouped value set.";
	}

	/**
	 * Gets the code already exists for message.
	 * 
	 * @param str
	 *            the str
	 * @return String
	 */
	public String getCodeAlreadyExistsForMessage(String str) {
		return "Code already exists for " + str + " value set.";
	}

	/**
	 * Gets the identity message.
	 * 
	 * @param msg
	 *            the msg
	 * @return String
	 */
	public String getIdentityMessage(String msg) {
		return msg;
	}

	/** The login failed. */
	private final String LOGIN_FAILED = "Login failed. Please sign in again.";

	/**
	 * Gets the login failed message.
	 * 
	 * @return String
	 */
	public String getLoginFailedMessage() {
		return LOGIN_FAILED;
	}

	/** The login failed user already logged in. */
	private final String LOGIN_FAILED_USER_ALREADY_LOGGED_IN = "Unable to login. Another session has been established with this account. Please verify you do not already have an open session.";

	/**
	 * Gets the login failed already logged in message.
	 * 
	 * @return String
	 */
	public String getLoginFailedAlreadyLoggedInMessage() {
		return LOGIN_FAILED_USER_ALREADY_LOGGED_IN;
	}

	/** The login failed temp password expired. */
	private final String LOGIN_FAILED_TEMP_PASSWORD_EXPIRED = "Unable to login. Your temporary password has expired. Please contact HelpDesk to renew your password.";

	/**
	 * Gets the login failed temp password expired message.
	 * 
	 * @return String
	 */
	public String getLoginFailedTempPasswordExpiredMessage() {
		return LOGIN_FAILED_TEMP_PASSWORD_EXPIRED;
	}

	// TODO is this such a smart policy?
	// This makes denial of service attacks on accounts too easy.
	// Perhaps make them answer a security question?
	/** The seccond attempt failed. */
	private final String SECCOND_ATTEMPT_FAILED = "Failed 2nd attempt. Next attempt will lock the account.";

	/**
	 * Gets the second attempt failed message.
	 * 
	 * @return String
	 */
	public String getSecondAttemptFailedMessage() {
		return SECCOND_ATTEMPT_FAILED;
	}

	/** The account revoked. */
	private final String ACCOUNT_REVOKED = "Your account has been revoked. Please contact the Helpdesk.";

	/**
	 * Gets the account revoked message.
	 * 
	 * @return String
	 */
	public String getAccountRevokedMessage() {
		return ACCOUNT_REVOKED;
	}

	/** The ACCOUN t_ locke d2. */
	private final String ACCOUNT_LOCKED2 = "Your account has been locked. Please contact the Helpdesk.";

	/**
	 * Gets the account locked2 message.
	 * 
	 * @return String
	 */
	public String getAccountLocked2Message() {
		return ACCOUNT_LOCKED2;
	}

	/**
	 * Gets the unknown code message.
	 * 
	 * @param code
	 *            the code
	 * @return String
	 */
	public String getUnknownCodeMessage(int code) {
		return "Unknown Code " + code;

	}

	/** The server side validation. */
	private final String SERVER_SIDE_VALIDATION = "Server Side Validation";

	/**
	 * Gets the server side validation message.
	 * 
	 * @return String
	 */
	public String getServerSideValidationMessage() {
		return SERVER_SIDE_VALIDATION;
	}

	/** The oid in use. */
	private final String OID_IN_USE = "OID is already in use.";

	/**
	 * Gets the oID in use message.
	 * 
	 * @return String
	 */
	public String getOIDInUseMessage() {
		return OID_IN_USE;
	}

	/**
	 * Gets the code list already exists message.
	 * 
	 * @param s
	 *            the s
	 * @return String
	 */
	public String getCodeListAlreadyExistsMessage(String s) {
		return "Value Set " + s + " already exists.";
	}

	/**
	 * Gets the unknown error message.
	 * 
	 * @param code
	 *            the code
	 * @return String
	 */
	public String getUnknownErrorMessage(int code) {
		return "Unknown Code " + code;
	}

	/** The email already exists. */
	private final String EMAIL_ALREADY_EXISTS = "E-mail Address already exists.";

	/**
	 * Gets the email already exists message.
	 * 
	 * @return String
	 */
	public String getEmailAlreadyExistsMessage() {
		return EMAIL_ALREADY_EXISTS;
	}

	/** The duplicate codes msg. */
	private final String DUPLICATE_CODES_MSG = "All code(s) were identified as duplicates to code(s) already in the value set and were ignored upon import.";

	/**
	 * Gets the duplicate codes message.
	 * 
	 * @return String
	 */
	public String getDuplicateCodesMessage() {
		return DUPLICATE_CODES_MSG;
	}

	/** The invalid template. */
	private final String INVALID_TEMPLATE = "Import failed. Invalid template.";

	/**
	 * Gets the invalid template message.
	 * 
	 * @return String
	 */
	public String getInvalidTemplateMessage() {
		return INVALID_TEMPLATE;
	}

	/** The system error. */
	private final String SYSTEM_ERROR = "Import failed due to system error. Please try again.";

	/**
	 * Gets the system error message.
	 * 
	 * @return String
	 */
	public String getSystemErrorMessage() {
		return SYSTEM_ERROR;
	}

	/** The empty file error. */
	private final String EMPTY_FILE_ERROR = "Import failed. File is empty.";

	/**
	 * Gets the empty file error.
	 * 
	 * @return String
	 */
	public String getEmptyFileError() {
		return EMPTY_FILE_ERROR;
	}

	/** The duplicate error. */
	private final String DUPLICATE_ERROR = "Import failed. One or more duplicate codes exist in file. Please remove then try again.";

	/**
	 * Gets the duplicate error message.
	 * 
	 * @return String
	 */
	public String getDuplicateErrorMessage() {
		return DUPLICATE_ERROR;
	}

	/** The security question header. */
	private final String SECURITY_QUESTION_HEADER = "The answer(s) provided to the security question(s) do not match the following rules:";

	/**
	 * Gets the security question heeader message.
	 * 
	 * @return String
	 */
	public String getSecurityQuestionHeeaderMessage() {
		return SECURITY_QUESTION_HEADER;
	}

	/** The not unique questions. */
	private final String NOT_UNIQUE_QUESTIONS = "You cannot use the same security question more than once.";

	/**
	 * Gets the not unique questions.
	 * 
	 * @return String
	 */
	public String getNotUniqueQuestions() {
		return NOT_UNIQUE_QUESTIONS;
	}

	/**
	 * Gets the security answer too short message.
	 * 
	 * @param index
	 *            the index
	 * @return String
	 */
	public String getSecurityAnswerTooShortMessage(int index) {
		return "Security Answer " + index + " is too short.";
	}

	/** The must not contain runs. */
	private final String MUST_NOT_CONTAIN_RUNS = "Passwords must not contain three consecutive identical characters.";

	/**
	 * Gets the must not contain runs message.
	 * 
	 * @return String
	 */
	public String getMustNotContainRunsMessage() {
		return MUST_NOT_CONTAIN_RUNS;
	}

	/** The must not contain dictionay word. */
	private final String MUST_NOT_CONTAIN_DICTIONAY_WORD = "Passwords must not consist of a single dictionary word with letters, numbers and symbols.";

	/**
	 * Gets the must not contain dictionary word message.
	 * 
	 * @return String
	 */
	public String getMustNotContainDictionaryWordMessage() {
		return MUST_NOT_CONTAIN_DICTIONAY_WORD;
	}

	/** The must not contain login id. */
	private final String MUST_NOT_CONTAIN_LOGIN_ID = "Passwords must not contain your User ID.";

	/**
	 * Gets the must not contain login id message.
	 * 
	 * @return String
	 */
	public String getMustNotContainLoginIdMessage() {
		return MUST_NOT_CONTAIN_LOGIN_ID;
	}

	/** The must contain special. */
	private final String MUST_CONTAIN_SPECIAL = "Must contain a special character.";

	/**
	 * Gets the must contain special message.
	 * 
	 * @return String
	 */
	public String getMustContainSpecialMessage() {
		return MUST_CONTAIN_SPECIAL;
	}

	/** The must contain number. */
	private final String MUST_CONTAIN_NUMBER = "Must contain a number.";

	/**
	 * Gets the must contain number message.
	 * 
	 * @return String
	 */
	public String getMustContainNumberMessage() {
		return MUST_CONTAIN_NUMBER;
	}

	/** The must contain lower. */
	private final String MUST_CONTAIN_LOWER = "Must contain a lowercase letter.";

	/**
	 * Gets the must contain lower message.
	 * 
	 * @return String
	 */
	public String getMustContainLowerMessage() {
		return MUST_CONTAIN_LOWER;
	}

	/** The must contain upper. */
	private final String MUST_CONTAIN_UPPER = "Must contain an uppercase letter.";

	/**
	 * Gets the must contain upper message.
	 * 
	 * @return String
	 */
	public String getMustContainUpperMessage() {
		return MUST_CONTAIN_UPPER;
	}

	/** The password wrong length. */
	private final String PASSWORD_WRONG_LENGTH = "Must be between 8 and 16 characters long.";

	/**
	 * Gets the password wrong length message.
	 * 
	 * @return String
	 */
	public String getPasswordWrongLengthMessage() {
		return PASSWORD_WRONG_LENGTH;
	}

	/** The password mismatch. */
	private final String PASSWORD_MISMATCH = "Your new password and confirm password do not match.";

	/**
	 * Gets the password mismatch message.
	 * 
	 * @return String
	 */
	public String getPasswordMismatchMessage() {
		return PASSWORD_MISMATCH;
	}

	/** The doesnt follow rules. */
	private final String DOESNT_FOLLOW_RULES = "The new password you entered does not match the following rules:";

	/**
	 * Gets the doesnt follow rules message.
	 * 
	 * @return String
	 */
	public String getDoesntFollowRulesMessage() {
		return DOESNT_FOLLOW_RULES;
	}

	/** The unknown fail. */
	private final String UNKNOWN_FAIL = "Unknown failure reason";

	/**
	 * Gets the unknown fail message.
	 * 
	 * @return String
	 */
	public String getUnknownFailMessage() {
		return UNKNOWN_FAIL;
	}

	/** The user not found msg. */
	private final String USER_NOT_FOUND_MSG = "User ID is required.";

	/**
	 * Gets the user not found message.
	 * 
	 * @return String
	 */
	public String getUserNotFoundMessage() {
		return USER_NOT_FOUND_MSG;
	}

	/** The email not found msg. */
	private final String EMAIL_NOT_FOUND_MSG = "Email Address is required.";

	/**
	 * Gets the email not found message.
	 * 
	 * @return String
	 */
	public String getEmailNotFoundMessage() {
		return EMAIL_NOT_FOUND_MSG;
	}

	/** The account locked. */
	private final String ACCOUNT_LOCKED = "Your account has been locked.Please Contact Helpdesk.";

	/**
	 * Gets the account locked message.
	 * 
	 * @return String
	 */
	public String getAccountLockedMessage() {
		return ACCOUNT_LOCKED;
	}

	/** The account termination. */
	private final String ACCOUNT_TERMINATION = "Your account has been Terminated.Please Contact Helpdesk for more information.";

	/**
	 * Gets the account termination.
	 * 
	 * @return String
	 */
	public String getAccountTermination() {
		return ACCOUNT_TERMINATION;
	}

	/** The security q mismatch. */
	private final String SECURITY_Q_MISMATCH = "The answer for the security question did not match.";

	/**
	 * Gets the security q mismatch message.
	 * 
	 * @return String
	 */
	public String getSecurityQMismatchMessage() {
		return SECURITY_Q_MISMATCH;
	}

	/** The security not answered. */
	private final String SECURITY_NOT_ANSWERED = "Your security questions have not been answered.  You cannot continue. Please contact the Helpdesk";

	/**
	 * Gets the security not answered message.
	 * 
	 * @return String
	 */
	public String getSecurityNotAnsweredMessage() {
		return SECURITY_NOT_ANSWERED;
	}

	/** The email mismatch. */
	private final String EMAIL_MISMATCH = "Email did not match with the User ID.  Please Contact the Administrator.";

	/**
	 * Gets the email mismatch message.
	 * 
	 * @return String
	 */
	public String getEmailMismatchMessage() {
		return EMAIL_MISMATCH;
	}

	/** The PHON e_10_ digit. */
	private final String PHONE_10_DIGIT = "Phone Number is required to be 10 digits.";

	/**
	 * Gets the phone ten digit message.
	 * 
	 * @return String
	 */
	public String getPhoneTenDigitMessage() {
		return PHONE_10_DIGIT;
	}

	/** The excel file type. */
	private final String EXCEL_FILE_TYPE = "Please select a file with an Excel file type (.xls or .xlsx)";

	/**
	 * Gets the excel file type message.
	 * 
	 * @return String
	 */
	public String getExcelFileTypeMessage() {
		return EXCEL_FILE_TYPE;
	}

	/** The file not selected. */
	private final String FILE_NOT_SELECTED = "Please select a file.";

	/**
	 * Gets the file not selected message.
	 * 
	 * @return String
	 */
	public String getFileNotSelectedMessage() {
		return FILE_NOT_SELECTED;
	}

	/** The code version required. */
	private final String CODE_VERSION_REQUIRED = "Code System Version is required.";

	/**
	 * Gets the code version required message.
	 * 
	 * @return String
	 */
	public String getCodeVersionRequiredMessage() {
		return CODE_VERSION_REQUIRED;
	}

	/** The code system required. */
	private final String CODE_SYSTEM_REQUIRED = "Code System is required.";

	/**
	 * Gets the code system required message.
	 * 
	 * @return String
	 */
	public String getCodeSystemRequiredMessage() {
		return CODE_SYSTEM_REQUIRED;
	}

	/** The category required. */
	private final String CATEGORY_REQUIRED = "Category is required.";

	/**
	 * Gets the category required message.
	 * 
	 * @return String
	 */
	public String getCategoryRequiredMessage() {
		return CATEGORY_REQUIRED;
	}

	/** The steward required. */
	private final String STEWARD_REQUIRED = "Steward is required.";

	/**
	 * Gets the steward required message.
	 * 
	 * @return String
	 */
	public String getStewardRequiredMessage() {
		return STEWARD_REQUIRED;
	}

	/** The descriptor required. */
	private final String DESCRIPTOR_REQUIRED = "Descriptor is required.";

	/**
	 * Gets the descriptor required message.
	 * 
	 * @return String
	 */
	public String getDescriptorRequiredMessage() {
		return DESCRIPTOR_REQUIRED;
	}

	/** The code required. */
	private final String CODE_REQUIRED = "Code is required.";

	/**
	 * Gets the code required message.
	 * 
	 * @return String
	 */
	public String getCodeRequiredMessage() {
		return CODE_REQUIRED;
	}

	/** The grouping required. */
	private final String GROUPING_REQUIRED = "A Grouping is required.";

	/**
	 * Gets the grouping required message.
	 * 
	 * @return String
	 */
	public String getGroupingRequiredMessage() {
		return GROUPING_REQUIRED;
	}

	/** The rationale required. */
	private final String RATIONALE_REQUIRED = "Rationale is required.";

	/**
	 * Gets the rationale required message.
	 * 
	 * @return String
	 */
	public String getRationaleRequiredMessage() {
		return RATIONALE_REQUIRED;
	}

	/** The description required. */
	private final String DESCRIPTION_REQUIRED = "Description is required.";

	/**
	 * Gets the description required meassage.
	 * 
	 * @return String
	 */
	public String getDescriptionRequiredMeassage() {
		return DESCRIPTION_REQUIRED;
	}

	/** The measure name required. */
	private final String MEASURE_NAME_REQUIRED = "Measure Name is required.";

	/**
	 * Gets the measure name required message.
	 * 
	 * @return String
	 */
	public String getMeasureNameRequiredMessage() {
		return MEASURE_NAME_REQUIRED;
	}

	/** The abv name required. */
	private final String ABV_NAME_REQUIRED = "Abbreviated Name is required.";

	/**
	 * Gets the abv name required message.
	 * 
	 * @return String
	 */
	public String getAbvNameRequiredMessage() {
		return ABV_NAME_REQUIRED;
	}

	/** The grouped name required. */
	private final String GROUPED_NAME_REQUIRED = "Grouped Value Set Name is required.";

	/**
	 * Gets the grouped name required message.
	 * 
	 * @return String
	 */
	public String getGroupedNameRequiredMessage() {
		return GROUPED_NAME_REQUIRED;
	}

	/** The name required. */
	private final String NAME_REQUIRED = "Value Set Name is required.";

	/**
	 * Gets the name required message.
	 * 
	 * @return the name required message
	 */
	public String getNameRequiredMessage() {
		return NAME_REQUIRED;
	}

	/** The Constant ONE_AND. */
	public static final String ONE_AND = "A Clause should start with only one AND.";

	/**
	 * Gets the one and message.
	 * 
	 * @return String
	 */
	public String getOneAndMessage() {
		return ONE_AND;
	}

	/** The Constant NO_MEASURES. */
	public static final String NO_MEASURES = "No Measures returned. Please search again.";

	/**
	 * Gets the no measures message.
	 * 
	 * @return String
	 */
	public String getNoMeasuresMessage() {
		return NO_MEASURES;
	}

	/** The Constant NO_CODE_LISTS. */
	public static final String NO_CODE_LISTS = "No Value Sets returned. Please search again.";

	/**
	 * Gets the no code lists message.
	 * 
	 * @return String
	 */
	public String getNoCodeListsMessage() {
		return NO_CODE_LISTS;
	}

	/** The Constant ROOT_OID_TOO_LONG. */
	public static final String ROOT_OID_TOO_LONG = "Root OID cannot exceed 50 characters.";

	/**
	 * Gets the root oid too long message.
	 * 
	 * @return String
	 */
	public String getRootOIDTooLongMessage() {
		return ROOT_OID_TOO_LONG;
	}

	/** The Constant OID_TOO_LONG. */
	public static final String OID_TOO_LONG = "OID cannot exceed 50 characters.";

	/**
	 * Gets the oID too long message.
	 * 
	 * @return String
	 */
	public String getOIDTooLongMessage() {
		return OID_TOO_LONG;
	}

	/** The Constant ROOT_OID_REQUIRED. */
	public static final String ROOT_OID_REQUIRED = "Root OID is required.";

	/**
	 * Gets the root oid required message.
	 * 
	 * @return String
	 */
	public String getRootOIDRequiredMessage() {
		return ROOT_OID_REQUIRED;
	}

	/** The Constant OID_REQUIRED. */
	public static final String OID_REQUIRED = "OID is required.";

	/**
	 * Gets the oID required message.
	 * 
	 * @return String
	 */
	public String getOIDRequiredMessage() {
		return OID_REQUIRED;
	}

	/** The Constant ORG_REQUIRED. */
	public static final String ORG_REQUIRED = "Organization is required.";

	/**
	 * Gets the org required message.
	 * 
	 * @return String
	 */
	public String getOrgRequiredMessage() {
		return ORG_REQUIRED;
	}

	/** The Constant PHONE_REQUIRED. */
	public static final String PHONE_REQUIRED = "Phone Number is required.";

	/**
	 * Gets the phone required message.
	 * 
	 * @return String
	 */
	public String getPhoneRequiredMessage() {
		return PHONE_REQUIRED;
	}

	/** The Constant LOGIN_ID_REQUIRED. */
	public static final String LOGIN_ID_REQUIRED = "User ID is required.";

	/**
	 * Gets the login id required message.
	 * 
	 * @return String
	 */
	public String getLoginIDRequiredMessage() {
		return LOGIN_ID_REQUIRED;
	}

	/** The Constant LOGIN_USER_REQUIRED. */
	public static final String LOGIN_USER_REQUIRED = "User Name is required.";

	/**
	 * Gets the login user required message.
	 * 
	 * @return String
	 */
	public String getLoginUserRequiredMessage() {
		return LOGIN_USER_REQUIRED;
	}

	/** The Constant LAST_NAME_REQUIRED. */
	public static final String LAST_NAME_REQUIRED = "Last Name is required.";

	/**
	 * Gets the last name required message.
	 * 
	 * @return String
	 */
	public String getLastNameRequiredMessage() {
		return LAST_NAME_REQUIRED;
	}

	/** The Constant FIRST_NAME_MIN. */
	public static final String FIRST_NAME_MIN = "First Name must be at least two characters.";

	/**
	 * Gets the first min message.
	 * 
	 * @return String
	 */
	public String getFirstMinMessage() {
		return FIRST_NAME_MIN;
	}

	/** The Constant FIRST_NAME_REQUIRED. */
	public static final String FIRST_NAME_REQUIRED = "First Name is required.";

	/**
	 * Gets the first name required message.
	 * 
	 * @return String
	 */
	public String getFirstNameRequiredMessage() {
		return FIRST_NAME_REQUIRED;
	}

	/** The Constant ALERT_LOADING_MESSAGE. */
	public static final String ALERT_LOADING_MESSAGE = "Please wait until loading is complete.";

	/**
	 * Gets the alert loading message.
	 * 
	 * @return String
	 */
	public String getAlertLoadingMessage() {
		return ALERT_LOADING_MESSAGE;
	}

	/** The Constant TEMP_EMAIL_SENT. */
	public static final String TEMP_EMAIL_SENT = "Temporary Password E-mail has been sent.";

	/**
	 * Gets the temp email sent message.
	 * 
	 * @return String
	 */
	public String getTempEmailSentMessage() {
		return TEMP_EMAIL_SENT;
	}

	/** The Constant CHANGES_SAVED. */
	public static final String CHANGES_SAVED = "Changes are successfully saved.";

	/**
	 * Gets the changes saved message.
	 * 
	 * @return String
	 */
	public String getChangesSavedMessage() {
		return CHANGES_SAVED;
	}

	/** The Constant CODE_LIST_ADDED. */
	public static final String CODE_LIST_ADDED = "Code has been successfully added to the value set.";

	/**
	 * Gets the code list added message.
	 * 
	 * @return String
	 */
	public String getCodeListAddedMessage() {
		return CODE_LIST_ADDED;
	}

	/** The Constant CODE_LIST_ADDED_GROUP. */
	public static final String CODE_LIST_ADDED_GROUP = "Value Set has been successfully added to the grouped value set.";

	/**
	 * Gets the code list added group message.
	 * 
	 * @return String
	 */
	public String getCodeListAddedGroupMessage() {
		return CODE_LIST_ADDED_GROUP;
	}

	/** The Constant IMPORT_SUCCESS. */
	public static final String IMPORT_SUCCESS = "Import Successful.";

	/**
	 * Gets the import success message.
	 * 
	 * @return String
	 */
	public String getImportSuccessMessage() {
		return IMPORT_SUCCESS;
	}

	// public static final String x = "";
	// ASK STAN
	/**
	 * Gets the import success message.
	 * 
	 * @param codes
	 *            the codes
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
	 * Gets the qDM success message.
	 * 
	 * @param codeListName
	 *            the code list name
	 * @param dataType
	 *            the data type
	 * @return String
	 */
	public String getQDMSuccessMessage(String codeListName, String dataType) {
		return " The QDM Element " + codeListName + ": " + dataType
				+ " has been added successfully.";
	}

	/**
	 * Gets the qDM ocurrence success message.
	 * 
	 * @param codeListName
	 *            the code list name
	 * @param dataType
	 *            the data type
	 * @param occurrenceMessage
	 *            the occurrence message
	 * @return String
	 */
	public String getQDMOcurrenceSuccessMessage(String codeListName,
			String dataType, String occurrenceMessage) {
		return " The QDM Element " + occurrenceMessage + " of " + codeListName
				+ ": " + dataType + " has been added successfully.";
	}

	/** The Constant GROUPING_SAVED. */
	public static final String GROUPING_SAVED = "Grouping has been saved.";

	/**
	 * Gets the grouping saved message.
	 * 
	 * @return String
	 */
	public String getGroupingSavedMessage() {
		return GROUPING_SAVED;
	}

	/** The Constant SUPP_DATA_SAVED. */
	public static final String SUPP_DATA_SAVED = "Supplemental data elements have been saved.";

	/**
	 * Gets the supp data saved message.
	 * 
	 * @return String
	 */
	public String getSuppDataSavedMessage() {
		return SUPP_DATA_SAVED;
	}

	/** The Constant PASSWORD_CHANGED. */
	public static final String PASSWORD_CHANGED = "Your password has been changed.";

	/**
	 * Gets the password saved message.
	 * 
	 * @return the password saved message
	 */
	public String getPasswordSavedMessage() {
		return PASSWORD_CHANGED;
	}

	/** The Constant SEC_QUESTIONS_UPDATED. */
	public static final String SEC_QUESTIONS_UPDATED = "Your security questions have been updated.";

	/**
	 * Gets the security questions updated message.
	 * 
	 * @return String
	 */
	public String getSecurityQuestionsUpdatedMessage() {
		return SEC_QUESTIONS_UPDATED;
	}

	/** The Constant PERSONAL_INFO_UPDATED. */
	public static final String PERSONAL_INFO_UPDATED = "Your personal information has been updated.";

	/**
	 * Gets the personal info updated message.
	 * 
	 * @return String
	 */
	public String getPersonalInfoUpdatedMessage() {
		return PERSONAL_INFO_UPDATED;
	}

	// US - 421
	/** The Constant s_ERR_MEASURE_SCORE_REQUIRED. */
	public static final String s_ERR_MEASURE_SCORE_REQUIRED = "Measure Scoring is required.";
	
	/** The Constant s_ERR_RETRIEVE_SCORING_CHOICES. */
	public static final String s_ERR_RETRIEVE_SCORING_CHOICES = "Problem while retrieving measure scoring choices.";

	// US 62
	/** The Constant s_ERR_RETRIEVE_UNITS. */
	public static final String s_ERR_RETRIEVE_UNITS = "Problem while retrieving units.";

	// US 602
	/** The Constant s_ERR_RETRIEVE_STATUS. */
	public static final String s_ERR_RETRIEVE_STATUS = "Problem while retrieving status.";

	// US 171
	/** The Constant s_ERR_RETRIEVE_OPERATOR. */
	public static final String s_ERR_RETRIEVE_OPERATOR = "Problem while retrieving operator.";
	// US - 502

	/** The value set date required. */
	private final String VALUE_SET_DATE_REQUIRED = "Value Set Package Date is required.";

	/**
	 * Gets the value set date required message.
	 * 
	 * @return String
	 */
	public String getValueSetDateRequiredMessage() {
		return VALUE_SET_DATE_REQUIRED;
	}

	/** The value set date invalid. */
	private final String VALUE_SET_DATE_INVALID = "Value Set Package Date is not a valid date.";

	/**
	 * Gets the value set date invalid message.
	 * 
	 * @return String
	 */
	public String getValueSetDateInvalidMessage() {
		return VALUE_SET_DATE_INVALID;
	}

	/** The Constant DRAFT. */
	public static final int DRAFT = 0;
	
	/** The Constant COMPLETE. */
	public static final int COMPLETE = 1;

	/** The Constant VALUE_SET_DRAFT_SAVED. */
	public static final String VALUE_SET_DRAFT_SAVED = "Value Set successfully saved as a draft.";
	
	/** The Constant VALUE_SET_COMPLETE_SAVED. */
	public static final String VALUE_SET_COMPLETE_SAVED = "Value Set successfully saved as complete.";

	/**
	 * Gets the value set changed saved message.
	 * 
	 * @param mode
	 *            the mode
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

	/** The Constant GROUPED_VALUE_SET_DRAFT_SAVED. */
	public static final String GROUPED_VALUE_SET_DRAFT_SAVED = "Grouped Value Set successfully saved as a draft.";
	
	/** The Constant GROUPED_VALUE_SET_COMPLETE_SAVED. */
	public static final String GROUPED_VALUE_SET_COMPLETE_SAVED = "Grouped Value Set successfully saved as complete.";

	/**
	 * Gets the grouped value set changed saved message.
	 * 
	 * @param mode
	 *            the mode
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

	/** The invalid last modified date. */
	private final String INVALID_LAST_MODIFIED_DATE = "Invalid Last Modified Date.";

	/**
	 * Gets the invalid last modified date message.
	 * 
	 * @return String
	 */
	public String getInvalidLastModifiedDateMessage() {
		return INVALID_LAST_MODIFIED_DATE;
	}

	/** The last modified date not unique. */
	private final String LAST_MODIFIED_DATE_NOT_UNIQUE = "The Last Modified date and time entered is already is use for this value set.";

	/**
	 * Gets the last modified date not unique message.
	 * 
	 * @return String
	 */
	public String getLastModifiedDateNotUniqueMessage() {
		return LAST_MODIFIED_DATE_NOT_UNIQUE;
	}

	/** The generic error message. */
	private final String GENERIC_ERROR_MESSAGE = "The Measure Authoring Tool was unable to process the request. Please try again. If the problem persists please contact the Help Desk.";

	/**
	 * Gets the generic error message.
	 * 
	 * @return String
	 */
	public String getGenericErrorMessage() {
		return GENERIC_ERROR_MESSAGE;
	}

	/** The password required error message. */
	private final String PASSWORD_REQUIRED_ERROR_MESSAGE = "Existing password is required to confirm changes.";

	/**
	 * Gets the password required error message.
	 * 
	 * @return String
	 */
	public String getPasswordRequiredErrorMessage() {
		return PASSWORD_REQUIRED_ERROR_MESSAGE;
	}

	/** The password mismatch error message. */
	private final String PASSWORD_MISMATCH_ERROR_MESSAGE = "Incorrect password supplied.Try again";

	/**
	 * Gets the password mismatch error message.
	 * 
	 * @return String
	 */
	public String getPasswordMismatchErrorMessage() {
		return PASSWORD_MISMATCH_ERROR_MESSAGE;
	}

	/** The all password fields required. */
	private final String ALL_PASSWORD_FIELDS_REQUIRED = "All password fields are required.";

	/**
	 * Gets the all password fields required.
	 * 
	 * @return String
	 */
	public String getAllPasswordFieldsRequired() {
		return ALL_PASSWORD_FIELDS_REQUIRED;
	}

	/** The measure selection error. */
	private final String MEASURE_SELECTION_ERROR = "Please select at least one measure";

	/**
	 * Gets the measure selection error.
	 * 
	 * @return String
	 */
	public String getMeasureSelectionError() {
		return MEASURE_SELECTION_ERROR;
	}

	/** The transfer checkbox error. */
	private final String TRANSFER_CHECKBOX_ERROR = "Please select at least one Value Set to transfer ownership.";

	/**
	 * Gets the transfer check box error.
	 * 
	 * @return String
	 */
	public String getTransferCheckBoxError() {
		return TRANSFER_CHECKBOX_ERROR;
	}

	/** The transfer checkbox error measure. */
	private final String TRANSFER_CHECKBOX_ERROR_MEASURE = "Please select at least one Measure to transfer ownership.";

	/**
	 * Gets the transfer check box error measure.
	 * 
	 * @return String
	 */
	public String getTransferCheckBoxErrorMeasure() {
		return TRANSFER_CHECKBOX_ERROR_MEASURE;
	}

	/** The transfer ownership success. */
	private final String TRANSFER_OWNERSHIP_SUCCESS = "Ownership successfully transferred to ";

	/**
	 * Gets the transfer ownership success.
	 * 
	 * @return String
	 */
	public String getTransferOwnershipSuccess() {
		return TRANSFER_OWNERSHIP_SUCCESS;
	}


	/** The save error msg. */
	private final String SAVE_ERROR_MSG = "You have unsaved changes that will be discarded if you continue. "
			+ "Do you want to continue without saving?";

	/**
	 * Gets the save error msg.
	 * 
	 * @return String the sAVE_ERROR_MSG
	 */
	public String getSaveErrorMsg() {
		return SAVE_ERROR_MSG;
	}

	/** The relationalop two child message. */
	private final String RELATIONALOP_TWO_CHILD_MESSAGE = "Package Failed. Measure logic is incomplete.LHS and RHS are required for Timings and Relationships.";

	/**
	 * Gets the relational op two child message.
	 * 
	 * @return String
	 */
	public String getRelationalOpTwoChildMessage() {
		return RELATIONALOP_TWO_CHILD_MESSAGE;
	}

	/** The measure deletion invalid pwd. */
	private final String MEASURE_DELETION_INVALID_PWD = "The entered password is invalid. Please try again.";

	/**
	 * Gets the measure deletion invalid pwd.
	 * 
	 * @return String
	 */
	public String getMeasureDeletionInvalidPwd() {
		return MEASURE_DELETION_INVALID_PWD;
	}

	/** The measure deletion success msg. */
	private final String MEASURE_DELETION_SUCCESS_MSG = "Measure successfully deleted.";

	/**
	 * Gets the measure deletion success mgs.
	 * 
	 * @return String
	 */
	public String getMeasureDeletionSuccessMgs() {
		return MEASURE_DELETION_SUCCESS_MSG;
	}

	/** The duplicate applied qdm. */
	private final String DUPLICATE_APPLIED_QDM = "Value set with selected datatype already exists in applied elements.";

	/**
	 * Gets the duplicate applied qdm msg.
	 * 
	 * @return String
	 */
	public String getDuplicateAppliedQDMMsg() {
		return DUPLICATE_APPLIED_QDM;
	}

	/** The successful modify applied qdm. */
	private final String SUCCESSFUL_MODIFY_APPLIED_QDM = "Selected QDM element has been modified successfully";

	/**
	 * Gets the successful modify qdm msg.
	 * 
	 * @return String
	 */
	public String getSuccessfulModifyQDMMsg() {
		return SUCCESSFUL_MODIFY_APPLIED_QDM;

	}

	/** The user required error msg. */
	private final String USER_REQUIRED_ERROR_MSG = "Please select a user to transfer ownership.";

	/**
	 * Gets the user required error message.
	 * 
	 * @return String
	 */
	public String getUserRequiredErrorMessage() {
		return USER_REQUIRED_ERROR_MSG;

	}

	/** The measure notes required message. */
	private final String MEASURE_NOTES_REQUIRED_MESSAGE = "Text required in Title and Description fields.";

	/**
	 * Gets the measure notes required message.
	 * 
	 * @return String String
	 */
	public String getMEASURE_NOTES_REQUIRED_MESSAGE() {
		return MEASURE_NOTES_REQUIRED_MESSAGE;
	}

	/** The measure notes delete success message. */
	private final String MEASURE_NOTES_DELETE_SUCCESS_MESSAGE = "The measure note deleted successfully.";

	/**
	 * Gets the measure notes delete success message.
	 * 
	 * @return String
	 */
	public String getMEASURE_NOTES_DELETE_SUCCESS_MESSAGE() {
		return MEASURE_NOTES_DELETE_SUCCESS_MESSAGE;
	}

	/** The measure notes save success message. */
	private final String MEASURE_NOTES_SAVE_SUCCESS_MESSAGE = "The measure note saved successfully.";

	/**
	 * Gets the measure notes save success message.
	 * 
	 * @return String
	 */
	public String getMEASURE_NOTES_SAVE_SUCCESS_MESSAGE() {
		return MEASURE_NOTES_SAVE_SUCCESS_MESSAGE;
	}

	/** The umls successfull login. */
	private final String UMLS_SUCCESSFULL_LOGIN = "Successfully logged into UMLS";

	/**
	 * Gets the umls successfull login.
	 * 
	 * @return String
	 */
	public String getUMLS_SUCCESSFULL_LOGIN() {
		return UMLS_SUCCESSFULL_LOGIN;
	}

	/** The uml login unavailable. */
	private final String UML_LOGIN_UNAVAILABLE = "Unable to verify your UMLS credentials. Please contact the MAT Help Desk or try again.";

	/**
	 * Gets the uml login unavailable.
	 * 
	 * @return String
	 */
	public String getUML_LOGIN_UNAVAILABLE() {
		return UML_LOGIN_UNAVAILABLE;
	}

	/** The uml login failed. */
	private final String UML_LOGIN_FAILED = "Login failed. Please sign in again.";

	/**
	 * Gets the uml login failed.
	 * 
	 * @return String
	 */
	public String getUML_LOGIN_FAILED() {
		return UML_LOGIN_FAILED;
	}

	/** The umls not loggedin. */
	private final String UMLS_NOT_LOGGEDIN = "You are not logged in to UMLS. Please access the UMLS Account tab to continue.";

	/**
	 * Gets the umls not loggedin.
	 * 
	 * @return String
	 */
	public String getUMLS_NOT_LOGGEDIN() {
		return UMLS_NOT_LOGGEDIN;
	}

	/** The umls oid required. */
	private final String UMLS_OID_REQUIRED = "Please enter an OID.";

	/**
	 * Gets the umls oid required.
	 * 
	 * @return String
	 */
	public String getUMLS_OID_REQUIRED() {
		return UMLS_OID_REQUIRED;
	}

	/** The vsac retrieve failed. */
	private final String VSAC_RETRIEVE_FAILED = "Unable to retrieve from VSAC. Please check the data and try again.";

	/**
	 * Gets the vsac retrieve failed.
	 * 
	 * @return String
	 */
	public String getVSAC_RETRIEVE_FAILED() {
		return VSAC_RETRIEVE_FAILED;
	}

	/** The vsac update successfull. */
	private final String VSAC_UPDATE_SUCCESSFULL = "Successfully updated applied QDM list with VSAC data.";

	/**
	 * Gets the vsac update successfull.
	 * 
	 * @return String
	 */
	public String getVSAC_UPDATE_SUCCESSFULL() {
		return VSAC_UPDATE_SUCCESSFULL;
	}

	/** The delete measure warning message. */
	private final String DELETE_MEASURE_WARNING_MESSAGE = "Deleting a draft or version of a measure will"
			+ "permanently remove the designated measure draft or "
			+ "version from  the Measure Authoring Tool. Deleted measures cannot <br> be recovered.";

	/**
	 * Gets the delete measure warning message.
	 * 
	 * @return String the dELETE_MEASURE_WARNING_MESSAGE
	 */
	public String getDELETE_MEASURE_WARNING_MESSAGE() {
		return DELETE_MEASURE_WARNING_MESSAGE;
	}

	/**
	 * Gets the measure package umls not logged in.
	 * 
	 * @return String the mEASURE_PACKAGE_UMLS_NOT_LOGGED_IN
	 */
	public String getMEASURE_PACKAGE_UMLS_NOT_LOGGED_IN() {
		return MEASURE_PACKAGE_UMLS_NOT_LOGGED_IN;
	}

	/** The measure package umls not logged in. */
	private final String MEASURE_PACKAGE_UMLS_NOT_LOGGED_IN = "Measure packaged successfully. "
			+ "Value set data is not included in the measure package as you are not logged into UMLS.";

	/** The validation msg element without vsac. */
	private final String VALIDATION_MSG_ELEMENT_WITHOUT_VSAC = "Please enter name and select a datatype associated with it.";

	/**
	 * Gets the validation msg element without vsac.
	 * 
	 * @return String
	 */
	public String getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC() {
		return VALIDATION_MSG_ELEMENT_WITHOUT_VSAC;
	}

	/** The validation msg data type vsac. */
	private final String VALIDATION_MSG_DATA_TYPE_VSAC = "Please select datatype from drop down list.";

	/**
	 * Gets the validation msg data type vsac.
	 * 
	 * @return String
	 */
	public String getVALIDATION_MSG_DATA_TYPE_VSAC() {
		return VALIDATION_MSG_DATA_TYPE_VSAC;
	}

	/**
	 * Gets the clause work space validation error.
	 * 
	 * @return String the cLAUSE_WORK_SPACE_VALIDATION_ERROR
	 */
	public String getCLAUSE_WORK_SPACE_VALIDATION_ERROR() {
		return CLAUSE_WORK_SPACE_VALIDATION_ERROR;
	}

	/** The clause work space validation error. */
	private final String CLAUSE_WORK_SPACE_VALIDATION_ERROR = "Measure logic is incomplete."
			+ "LHS and RHS are required for Timings and Relationships.";

	/** The clause work space validation success. */
	private final String CLAUSE_WORK_SPACE_VALIDATION_SUCCESS = "Measure logic validation successful.";

	/**
	 * Gets the clause work space validation success.
	 * 
	 * @return String the cLAUSE_WORK_SPACE_VALIDATION_SUCCESS
	 */
	public String getCLAUSE_WORK_SPACE_VALIDATION_SUCCESS() {
		return CLAUSE_WORK_SPACE_VALIDATION_SUCCESS;
	}

	/** The modify qdm select atleast one. */
	private final String MODIFY_QDM_SELECT_ATLEAST_ONE = "Please select atleast one applied QDM to modify.";

	/**
	 * Gets the modify qdm select atleast one.
	 * 
	 * @return String the mODIFY_QDM_SELECT_ATLEAST_ONE
	 */
	public String getMODIFY_QDM_SELECT_ATLEAST_ONE() {
		return MODIFY_QDM_SELECT_ATLEAST_ONE;
	}

	/** The modify qdm attribute validation. */
	private final String MODIFY_QDM_ATTRIBUTE_VALIDATION = "A value set with an Attribute category must be used for this data element.";

	/**
	 * Gets the modify qdm attribute validation.
	 * 
	 * @return String the mODIFY_QDM_ATTRIBUTE_VALIDATION
	 */
	public String getMODIFY_QDM_ATTRIBUTE_VALIDATION() {
		return MODIFY_QDM_ATTRIBUTE_VALIDATION;
	}

	/** The modify qdm non attribute validation. */
	private final String MODIFY_QDM_NON_ATTRIBUTE_VALIDATION = "A value set with a non-Attribute category must be used for this data element.";

	/**
	 * Gets the modify qdm non attribute validation.
	 * 
	 * @return String the mODIFY_QDM_NON_ATTRIBUTE_VALIDATION
	 */
	public String getMODIFY_QDM_NON_ATTRIBUTE_VALIDATION() {
		return MODIFY_QDM_NON_ATTRIBUTE_VALIDATION;
	}
}
