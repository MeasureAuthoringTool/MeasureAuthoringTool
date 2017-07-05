package mat.client.shared;

import mat.shared.ConstantMessages;

// TODO: Auto-generated Javadoc
/**
 * Message store to prevent duplicated messages final String fields and their
 * getters.
 * 
 * @author aschmidt
 */
public class MessageDelegate {
	
	
	
	/** The Constant WELCOME_MESSAGE. */
	public static final String WELCOME_MESSAGE = "You have successfully logged into the MAT.";
	
	/** The Constant ALERT_LOADING_MESSAGE. */
	public static final String ALERT_LOADING_MESSAGE = "Please wait until loading is complete.";
	
	/** The Constant MEASURE_DEVELOPER_ADDED_SUCCESSFULLY. */
	public static final String MEASURE_DEVELOPER_ADDED_SUCCESSFULLY= "Measure Developer updated successfully. Changes must be saved on the previous page.";
	
	/** The Constant CHANGES_SAVED. */
	public static final String CHANGES_SAVED = "Changes are successfully saved.";
	
	/** The Constant NO_MARKUP_ALLOWED. */
	public static final String NO_MARKUP_ALLOWED = "No markup text allowed in any text fields.";
	
	/** The Constant CODE_LIST_ADDED. */
	public static final String CODE_LIST_ADDED = "Code has been successfully added to the value set.";
	
	/** The Constant CODE_LIST_ADDED_GROUP. */
	public static final String CODE_LIST_ADDED_GROUP = "Value set has been successfully added to the grouped value set.";
	
	/** The Constant COMPLETE. */
	public static final int COMPLETE = 1;
	
	/** The Constant DRAFT. */
	public static final int DRAFT = 0;
	
	/** The Constant FIRST_NAME_MIN. */
	public static final String FIRST_NAME_MIN = "First Name must be at least two characters.";
	
	/** The Constant FIRST_NAME_REQUIRED. */
	public static final String FIRST_NAME_REQUIRED = "First Name is required.";
	
	/** The Constant GROUPED_VALUE_SET_COMPLETE_SAVED. */
	public static final String GROUPED_VALUE_SET_COMPLETE_SAVED = "Grouped value set successfully saved as complete.";
	
	/** The Constant GROUPED_VALUE_SET_DRAFT_SAVED. */
	public static final String GROUPED_VALUE_SET_DRAFT_SAVED = "Grouped value set successfully saved as a draft.";
	
	/** The Constant GROUPING_SAVED. */
	public static final String GROUPING_SAVED = "Grouping has been saved.";
	
	/** The Constant IMPORT_SUCCESS. */
	public static final String IMPORT_SUCCESS = "Import Successful.";
	
	/** The Constant LAST_NAME_REQUIRED. */
	public static final String LAST_NAME_REQUIRED = "Last Name is required.";
	
	/** The Constant LOGIN_ID_REQUIRED. */
	public static final String LOGIN_ID_REQUIRED = "User ID is required.";
	
	/** The Constant LIBRARY_NAME_REQUIRED. */
	public static final String LIBRARY_NAME_REQUIRED = "Library Name is required.";
	
	/** The Constant CQL_STAND_ALONE_LIBRARY_NAME_ERROR. */
	public static final String CQL_STAND_ALONE_LIBRARY_NAME_ERROR = "Invalid Library Name.Must start with an alpha-character or underscore followed by an alpha-numeric character(s) or underscore(s), and must not contain spaces.";
	
	/** The Constant CQL_FUNCTION_ARGUMENT_NAME_ERROR. */
	public static final String CQL_FUNCTION_ARGUMENT_NAME_ERROR = "Invalid argument name. Must start with an alpha-character or underscore followed by an alpha-numeric character(s) or underscore(s), and must not contain spaces.";
	/** The Constant EMAIL_ID_INCORRECT_FORMAT. */
	public static final String EMAIL_ID_INCORRECT_FORMAT = "Email Address has an incorrect format.";
	/** The Constant EMAIL_ID_REQUIRED. */
	public static final String EMAIL_ID_REQUIRED = "Email Address is required.";
	/** The Constant LOGIN_USER_REQUIRED. */
	public static final String LOGIN_USER_REQUIRED = "User Name is required.";
	
	/** The Constant NO_CODE_LISTS. */
	public static final String NO_CODE_LISTS = "No value sets returned. Please search again.";
	
	/** The Constant NO_MEASURES. */
	public static final String NO_MEASURES = "No measures returned. Please search again.";
	
	/** The Constant NO_MEASURES. */
	public static final String NO_LIBRARUES = "No libraries returned. Please search again.";
	
	/** The Constant WARNING_BAD_DATA_TYPE_COMBINATION. */
	public static final String WARNING_BAD_DATA_TYPE_COMBINATION ="Successfully saved with errors. You have an incorrect value set/code datatype combination.";
	
	/** The no includes. */
	public  final String NO_INCLUDES = "No libraries returned.Please search again.";
	
	
	/** The Constant OID_REQUIRED. */
	public static final String OID_REQUIRED = "OID is required.";
	
	/** The Constant OID_TOO_LONG. */
	public static final String OID_TOO_LONG = "OID cannot exceed 50 characters.";
	
	/** The Constant ONE_AND. */
	public static final String ONE_AND = "A clause should start with only one AND.";
	
	/** The Constant ORG_REQUIRED. */
	public static final String ORG_REQUIRED = "Organization is required.";
	
	/** The Constant PASSWORD_CHANGED. */
	public static final String PASSWORD_CHANGED = "Your password has been changed.";
	
	/** The Constant PERSONAL_INFO_UPDATED. */
	public static final String PERSONAL_INFO_UPDATED = "Your personal information has been updated.";
	
	/** The Constant PHONE_REQUIRED. */
	public static final String PHONE_REQUIRED = "Phone Number is required.";
	
	/** The Constant ROOT_OID_REQUIRED. */
	public static final String ROOT_OID_REQUIRED = "Root OID is required.";
	
	/** The Constant ROOT_OID_TOO_LONG. */
	public static final String ROOT_OID_TOO_LONG = "Root OID cannot exceed 50 characters.";
	
	// US - 421
	/** The Constant s_ERR_MEASURE_SCORE_REQUIRED. */
	public static final String s_ERR_MEASURE_SCORE_REQUIRED = "Measure Scoring is required.";
	
	/** The Constant CONTINOUS_VARIABLE_IS_NOT_PATIENT_BASED_ERROR. */
	public static final String CONTINOUS_VARIABLE_IS_NOT_PATIENT_BASED_ERROR = "Continous Variable measures must not be patient based.";
	
	// US 171
	/** The Constant s_ERR_RETRIEVE_OPERATOR. */
	public static final String s_ERR_RETRIEVE_OPERATOR = "Problem while retrieving operator.";
	// US - 502
	
	/** The Constant s_ERR_RETRIEVE_SCORING_CHOICES. */
	public static final String s_ERR_RETRIEVE_SCORING_CHOICES = "Problem while retrieving measure scoring choices.";
	
	// US 602
	/** The Constant s_ERR_RETRIEVE_STATUS. */
	public static final String s_ERR_RETRIEVE_STATUS = "Problem while retrieving status.";
	
	// US 62
	/** The Constant s_ERR_RETRIEVE_UNITS. */
	public static final String s_ERR_RETRIEVE_UNITS = "Problem while retrieving units.";
	
	/** The Constant SEC_QUESTIONS_UPDATED. */
	public static final String SEC_QUESTIONS_UPDATED = "Your security questions have been updated.";
	
	/** The Constant SUPP_DATA_SAVED. */
	public static final String SUPP_DATA_SAVED = "Supplemental Data Elements have been saved.";
	
	/** The Constant RISK_ADJ_SAVED. */
	public static final String RISK_ADJ_SAVED = "Risk Adjustment Variables have been saved.";
	
	/** The Constant TEMP_EMAIL_SENT. */
	public static final String TEMP_EMAIL_SENT = "Temporary Password E-mail has been sent.";
	
	/** The Constant VALUE_SET_COMPLETE_SAVED. */
	public static final String VALUE_SET_COMPLETE_SAVED = "Value set successfully saved as complete.";
	
	/** The Constant VALUE_SET_DRAFT_SAVED. */
	public static final String VALUE_SET_DRAFT_SAVED = "Value set successfully saved as a draft.";
	
	/** The Constant VSAC_VERSION_OR_EFFECTIVE_DATE_REQUIRED. */
	public static final String VSAC_VERSION_OR_EFFECTIVE_DATE_REQUIRED = "Value set version or effective date has been selected."
			+ " Please enter a date to continue.";
	/**
	 * Ratio Measure - Association Required for Numerator/Denominator in case of more than 1 IP.
	 */
	public static final String RATIO_NUM_DENO_ASSOCIATION_REQUIRED = "For Ratio measures, in the case of more than one Initial Population, "
			+ " Numerator and Denominator must contain one association.";
	
	/** The Constant RATIO_MEASURE_OBS_ASSOCIATION_REQUIRED. */
	public static final String RATIO_MEASURE_OBS_ASSOCIATION_REQUIRED = "For Ratio Measures, each Measure Observation requires an association be made to the Numerator or the Denominator.";

	/** The Constant ERROR_IN_SAVING_QDM_ELEMENTS. */
	private static final String ERROR_IN_SAVING_QDM_ELEMENTS = "Valuesets with different OIDs can not have the same valueset name.";

	/** The Constant CQL_LIBRARY_LIMIT_WARNING. */
	private static final String CQL_LIBRARY_LIMIT_WARNING = "You have reached the maximum number of selected CQL Libraries (10).";
	
	/** The vsac expansion profile selection. */
	public final String VSAC_EXPANSION_PROFILE_SELECTION = "Please Select VSAC Expansion Profile to Apply to value sets.";
	
	/** The vsac expansion profile selection. */
	public final String DEFAULT_EXPANSION_PROFILE_REMOVED = "Successfully removed VSAC Expansion Profile from value sets.";
	
	/** The vsac profile applied to qdm elements. */
	public final String VSAC_PROFILE_APPLIED_TO_QDM_ELEMENTS = "Successfully applied VSAC Expansion Profile to value sets.";
	
	/** The successful oid retreival from vsac. */
	public final String  SUCCESSFUL_OID_RETREIVAL_FROM_VSAC = "Value set successfully retrieved from VSAC.";
	
	/** The warning pasting in applied qdm elements. */
	public final String WARNING_PASTING_IN_APPLIED_QDM_ELEMENTS = "You are trying to paste QDM elements in this measure." +
			"If you want to continue say Yes or No to cancel.";
	
	/** The successfully pasted qdm elements in measure. */
	public final String SUCCESSFULLY_PASTED_QDM_ELEMENTS_IN_MEASURE = "Selected QDM elements have been pasted successfully.";
	
	/** The copy qdm select atleast one. */
	public final String COPY_QDM_SELECT_ATLEAST_ONE  = "Please select at least one applied QDM element to copy.";
	
	/** The successful qdm remove msg. */
	public final String SUCCESSFUL_QDM_REMOVE_MSG  = "Selected value set has been removed successfully.";
	
	/** The warning measure package creation generic. */
	public final String WARNING_MEASURE_PACKAGE_CREATION_GENERIC = "Unable to create measure package. " +
			"Please validate your measure logic in both Population Workspace and Clause Workspace.";
	
	/** The warning measure package creation risk adjustment. */
	public final String WARNING_MEASURE_PACKAGE_CREATION_RISK_ADJUSTMENT = "Unable to create measure package. " +
			"Please validate that Risk Adjustment Vairable clauses do not contain a datetimediff function or a QDM Variable.";
	
	/** The warning measure package creation strata. */
	public final String WARNING_MEASURE_PACKAGE_CREATION_STRATA = "Unable to create measure package. Stratum must contain at least one clause.";
	
	/** The successfully modified all oids. */
	public final String SUCCESSFULLY_MODIFIED_ALL_OIDS = "All QDM elements and/or attributes using the same value set OID have been modified " +
			"to the selected Version and/or Expansion Identifier.";
	
	/** The view cql error message. */
	public final String VIEW_CQL_ERROR_MESSAGE = "You are viewing CQL with few validation errors. Errors are marked with triangle sign on line number.";
	
	/** The view cql no errors message. */
	public final String VIEW_CQL_NO_ERRORS_MESSAGE ="You are viewing CQL with no validation errors.";
	
	/** The view cql error message bad valueset datatype. */
	public final String VIEW_CQL_ERROR_MESSAGE_BAD_VALUESET_DATATYPE ="CQL file with validation errors. You have an incorrect value set/code datatype combination.";
	
	/** The successful saved cql gen info. */
	public final String SUCCESSFUL_SAVED_CQL_GEN_INFO  = "Successfully saved CQL general information.";
	
	/** The successful saved cql definition. */
	public final String SUCCESSFUL_SAVED_CQL_DEFINITION  = "Successfully saved definition into CQL.";
	
	/** The successful saved cql functions. */
	public final String SUCCESSFUL_SAVED_CQL_FUNCTIONS  = "Successfully saved function into CQL.";
	
	/** The successful saved cql parameter. */
	public final String SUCCESSFUL_SAVED_CQL_PARAMETER  = "Successfully saved parameter into CQL.";
	
	/** The successful saved cql include library. */
	public final String SUCCESSFUL_SAVED_CQL_INCLUDE_LIBRARY  = "Library Insert Alias Name Here successfully Included.";
	
	/** The error save cql definition. */
	public final String ERROR_SAVE_CQL_DEFINITION  = "Please enter definition name.";
	
	/** The successful saved cql definition with errors. */
	public final String SUCCESSFUL_SAVED_CQL_DEFINITION_WITH_ERRORS  = "Successfully saved definition into CQL with errors.";
	
	/** The successful saved cql functions with errors. */
	public final String SUCCESSFUL_SAVED_CQL_FUNCTIONS_WITH_ERRORS  = "Successfully saved function into CQL  with errors.";
	
	/** The successful saved cql parameter with errors. */
	public final String SUCCESSFUL_SAVED_CQL_PARAMETER_WITH_ERRORS  = "Successfully saved parameter into CQL  with errors.";
	
	/** The error save cql parameter. */
	public final String ERROR_SAVE_CQL_PARAMETER  = "Please enter parameter name.";
	
	/** The error save cql function. */
	public final String ERROR_SAVE_CQL_FUNCTION  = "Please enter function name.";
	
	/** The error duplicate identifier name. */
	public final String ERROR_DUPLICATE_IDENTIFIER_NAME  = "Name already exists.";
	
	/** The sucess definition modify. */
	public final String SUCESS_DEFINITION_MODIFY  = "Successfully modified definition.";
	
	/** The sucess parameter modify. */
	public final String SUCESS_PARAMETER_MODIFY  = "Successfully modified parameter.";
	
	/** The sucess function modify. */
	public final String SUCESS_FUNCTION_MODIFY  = "Successfully modified function.";
	
	/** The sucess definition modify with errors. */
	public final String SUCESS_DEFINITION_MODIFY_WITH_ERRORS  = "Successfully modified definition with errors.";
	
	/** The sucess parameter modify with errors. */
	public final String SUCESS_PARAMETER_MODIFY_WITH_ERRORS  = "Successfully modified parameter with errors.";
	
	/** The sucess function modify with errors. */
	public final String SUCESS_FUNCTION_MODIFY_WITH_ERRORS  = "Successfully modified function with errors.";
	
	/** The error parameter name no special char. */
	public final String ERROR_PARAMETER_NAME_NO_SPECIAL_CHAR  = "Invalid Parameter name. " +
			"Duplicate name or use of restricted character(s).";
	
	/** The error definition name no special char. */
	public final String ERROR_DEFINITION_NAME_NO_SPECIAL_CHAR  = "Invalid Definition name. " +
			"Duplicate name or use of restricted character(s).";
	
	/** The error function name no special char. */
	public final String ERROR_FUNCTION_NAME_NO_SPECIAL_CHAR  = "Invalid Function and/or Argument name. " +
			"Duplicate name or use of restricted character(s).";
	
	/** The error include alias name no special char. */
	public final String ERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR  = "Invalid Library Alias. Must be unique, start with an alpha-character or underscore followed by an alpha-numeric character(s) or underscore(s), and must not contain spaces.";
	
	/** The delete warning message. */
	public final String DELETE_WARNING_MESSAGE = "You have selected to delete this expression. Do you want to permanently delete";
	
	/** The save include library valiation error. */
	public final String SAVE_INCLUDE_LIBRARY_VALIATION_ERROR = "Alias name and CQL Library selection are required.";
	
	/** The modify cql library name. */
	public final String MODIFY_CQL_LIBRARY_NAME = "Successfully modified CQL Library Name.";
	
	
	/** The Constant NO_USERS_RETURNED. */
	public static final String NO_USERS_RETURNED = "No Users returned. Please search again.";
	
	/** The Constant NO_VERSION_CREATED. */
	public static final String NO_VERSION_CREATED = "Unable to version. There are validation errors in CQL. Please correct and try again.";

	private final String MSG_TYPE_CHECK_VALIDATION_MEASURE_PACKAGE = "Unable to create measure package. There is an incorrect return type in the following grouping(s): ";
	
	private final String ERROR_VALIDATION_COMMENT_AREA = "Comment cannot exceed 250 characters. ";
	
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
	
	/** The abv name required. */
	private final String ABV_NAME_REQUIRED = "Abbreviated Name is required.";
	
	/** The account locked. */
	private final String ACCOUNT_LOCKED = "Please contact MAT Support. We are unable to complete your request at this time.";
	
	/** The ACCOUN t_ locke d2.
	private final String ACCOUNT_LOCKED2 = "Your account has been locked. Please contact Support.";
	 */
	/** The account revoked. */
	private final String ACCOUNT_REVOKED = "Your account has been revoked. Please contact Support.";
	
	/** The account termination. */
	private final String ACCOUNT_TERMINATION = "Your account has been terminated. Please contact Support for more information.";
	
	/** The all password fields required. */
	private final String ALL_PASSWORD_FIELDS_REQUIRED = "All password fields are required.";
	
	/** The category required. */
	private final String CATEGORY_REQUIRED = "Category is required.";
	
	/** The clause work space validation error. */
	private final String MEASURE_LOGIC_IS_INCOMPLETE = " Measure logic is incomplete.";
	
	/** The lhs rhs required. */
	private final String LHS_RHS_REQUIRED = " LHS and RHS are required for Timings, Relationships and Satisfies functions.";
	
	/** The atleast one child required. */
	private final String ATLEAST_ONE_CHILD_REQUIRED = " Functions must contain at least one child node.";
	
	/** The at least three children are required. */
	private final String AT_LEAST_TWO_CHILDREN_REQUIRED = "Union, Intersection, and Datetimediff must contain at least two or more child nodes.";
	
	/** The at least three children are required. */
	private final String AT_LEAST_THREE_CHILDREN_REQUIRED = "Satisfies All and Satisfies Any must contain at least three or more child nodes.";
	
	/** The clause work space validation success. */
	private final String CLAUSE_WORK_SPACE_VALIDATION_SUCCESS = "Measure logic validation successful.";
	
	/** The clause work space validation error. */
	private final String POPULATION_WORK_SPACE_VALIDATION_ERROR = "Logic must only contain clauses and logical operators.";
	
	/** The measure logic is incorrect. */
	private final String MEASURE_LOGIC_IS_INCORRECT = " Measure logic is incorrect.";
	/** The population work space measure observation validation error. */
	private final String POPULATION_WORK_SPACE_MEASURE_OBSERVATION_VALIDATION_ERROR = " Logic must only contain clauses.";
	
	/** The invalid character validation error. */
	private final String INVALID_CHARACTER_VALIDATION_ERROR =" Value set name cannot contain any of the following characters + * ? : - | ! \" %";
	
	/** The organization success message. */
	private final String ORGANIZATION_SUCCESS_MESSAGE = "Organization successfully added.";
	
	/** The user success message. */
	private final String USER_SUCCESS_MESSAGE = "User information saved successfully.";
	
	/** The clause work space validation success. */
	private final String POPULATION_WORK_SPACE_VALIDATION_SUCCESS = " Measure logic validation successful.";
	
	/** The code list required. */
	private final String CODE_LIST_REQUIRED = "Value set is required.";
	
	/** The code required. */
	private final String CODE_REQUIRED = "Code is required.";
	
	/** The clause empty. */
	private final String CLAUSE_EMPTY = "Clause must contain logic.";
	
	//MAT-8606 validations.	
	private final String PATIENT_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE = "For Patient-based Measures, all definitions directly added to populations must return a Boolean.";
	
	//MAT-8608 validations.	
	private final String EPISODE_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE = "For Episode-based Measures, all definitions directly added to populations must return a list of the same type.";
		
	//MAT-8622 Measure Observation and Patient-based Measure Indicator in Ratio scoring type.
	private final String EPISODE_BASED_RATIO_MEASURE_SAVE_GROUPING_VALIDATION_MESSAGE = "Measure Observations can only be added to a measure grouping in a Ratio measure, if the measure is Episode-based.";
			
	//MAT-8624 Single Argument Required for Measure Observation User-defined Function .
	private final String MEASURE_OBSERVATION_USER_DEFINED_FUNC_VALIDATION_MESSAGE = "Measure Observations added to a measure grouping may only contain a user-defined function that has exactly 1 argument in the argument list.";
	
	//MAT-8626 validations for Argument Type for Measure Observation Function Must Match Return Type of Associated Population.	
	private final String MEASURE_OBSERVATION_RETURN_SAME_TYPE_VALIDATION_MESSAGE = "Measure Observations added to a measure grouping must have an argument that returns the same type as the definition directly applied to the Measure Population associated with the Measure Observation.";
		
	//MAT-8627 validations for functions attached to Measure Observations.
	private final String MEASURE_OBSERVATION_USER_DEFINED_FUNC_REURN_TYPE_VALIDATION_MESSAGE = "Measure Observations added to a measure grouping must contain a user-defined function that returns an integer, a decimal, or a quantity.";
	
	/**
	 * Gets the population work space validation error.
	 *
	 * @return the population work space validation error
	 */
	public String getPOPULATION_WORK_SPACE_VALIDATION_ERROR() {
		return POPULATION_WORK_SPACE_VALIDATION_ERROR;
	}
	
	/**
	 * Gets the population work space validation success.
	 *
	 * @return the population work space validation success
	 */
	public String getPOPULATION_WORK_SPACE_VALIDATION_SUCCESS() {
		return POPULATION_WORK_SPACE_VALIDATION_SUCCESS;
	}
	
	/** The code system required. */
	private final String CODE_SYSTEM_REQUIRED = "Code System is required.";
	
	/** The code version required. */
	private final String CODE_VERSION_REQUIRED = "Code System Version is required.";
	
	/** The continuous variable may not contain. */
	private final String CONTINUOUS_VARIABLE_MAY_NOT_CONTAIN = "For a Continuous Variable measure, a grouping may not contain any Numerator, Numerator Exclusions, Denominator, Denominator Exclusions, or Denominator Exceptions.";
	
	/*
	 * CONTINUOUS VARIABLE
	 */
	/** The continuous variable wrong num. */
	private final String CONTINUOUS_VARIABLE_WRONG_NUM = "For a Continuous Variable measure, a grouping must contain exactly one of each of the following: "
			+ "Initial Population, Measure Population,and at least one Measure Observation.";
	
	/** The delete measure warning message. */
	private final String DELETE_MEASURE_WARNING_MESSAGE = "Deleting a draft or version of a measure will"
			+ "permanently remove the designated measure draft or "
			+ "version from  the Measure Authoring Tool. Deleted measures cannot <br> be recovered.";
	
	/** The description required. */
	private final String DESCRIPTION_REQUIRED = "Description is required.";
	
	/** The descriptor required. */
	private final String DESCRIPTOR_REQUIRED = "Descriptor is required.";
	
	/** The doesnt follow rules. */
	private final String DOESNT_FOLLOW_RULES = "The new password you entered does not match the following rules:";
	
	/** The duplicate applied qdm. */
	private final String DUPLICATE_APPLIED_VALUE_SET = "Value set name already exists.";
	
	/** The duplicate codes msg. */
	private final String DUPLICATE_CODES_MSG = "All code(s) were identified as duplicates to code(s) already in the value set and were ignored upon import.";
	
	/** The duplicate error. */
	private final String DUPLICATE_ERROR = "Import failed. One or more duplicate codes exist in file. Please remove then try again.";
	
	/** The email already exists. */
	private final String EMAIL_ALREADY_EXISTS = "E-mail Address already exists.";
	
	/** The email mismatch. */
	private final String EMAIL_MISMATCH = "Email did not match with the User ID.  Please Contact the Administrator.";
	
	/** The email not found msg. */
	private final String EMAIL_NOT_FOUND_MSG = "Email Address is required.";
	
	/** The empty file error. */
	private final String EMPTY_FILE_ERROR = "Import failed. File is empty.";
	
	/** The excel file type. */
	private final String EXCEL_FILE_TYPE = "Please select a file with an Excel file type (.xls or .xlsx)";
	
	/** The file not selected. */
	private final String FILE_NOT_SELECTED = "Please select a file.";
	
	/** The generic error message. */
	private final String GENERIC_ERROR_MESSAGE = "The Measure Authoring Tool was unable to process the request. Please try again. If the problem persists please contact the Help Desk.";
	
	/** The grouped name required. */
	private final String GROUPED_NAME_REQUIRED = "Grouped Value Set Name is required.";
	
	/** The grouping required. */
	private final String GROUPING_REQUIRED = "A Grouping is required.";
	
	/** The invalid last modified date. */
	private final String INVALID_LAST_MODIFIED_DATE = "Invalid Last Modified Date.";
	
	/** The invalid template. */
	private final String INVALID_TEMPLATE = "Import failed. Invalid template.";
	
	/** The last modified date not unique. */
	private final String LAST_MODIFIED_DATE_NOT_UNIQUE = "The Last Modified date and time entered is already is use for this value set.";
	
	/** The login failed. */
	private final String LOGIN_FAILED = "Invalid username and/or password and/or One Time Security code. MAT accounts are locked after three invalid login attempts.";
	
	/** The login failed temp password expired. */
	private final String LOGIN_FAILED_TEMP_PASSWORD_EXPIRED = "Unable to login. Your temporary password has expired. Please contact HelpDesk to renew your password.";
	
	/** The login failed user already logged in. */
	private final String LOGIN_FAILED_USER_ALREADY_LOGGED_IN = "Unable to login. Another session has been established with this account. Please verify you do not already have an open session.";
	
	/** The measure deletion invalid pwd. */
	private final String MEASURE_DELETION_INVALID_PWD = "The entered password is invalid. Please try again.";
	
	/** The measure deletion success msg. */
	private final String MEASURE_DELETION_SUCCESS_MSG = "Measure successfully deleted.";
	
	/** The measure name required. */
	private final String MEASURE_NAME_REQUIRED = "Measure Name is required.";
	
	/** The measure notes delete success message. */
	private final String MEASURE_NOTES_DELETE_SUCCESS_MESSAGE = "The measure note deleted successfully.";
	
	/** The measure notes required message. */
	private final String MEASURE_NOTES_REQUIRED_MESSAGE = "Text required in Title and Description fields.";
	
	/** The measure notes save success message. */
	private final String MEASURE_NOTES_SAVE_SUCCESS_MESSAGE = "The measure note saved successfully.";
	
	/** The measure package umls not logged in. */
	private final String MEASURE_PACKAGE_UMLS_NOT_LOGGED_IN = "Measure packaged successfully. "
			+ "Value set data is not included in the measure package as you are not logged into UMLS.";
	
	/** The measure package failed vsac timeout. */
	private final String MEASURE_PACKAGE_FAILED_VSAC_TIMEOUT = "Measure Package Failed. VSAC request timed out.Please contact Help Desk.";
	
	/** The Measure Period has invalid "TO" and/or "FROM" period dates. */
	public final String MEASURE_PERIOD_DATES_ERROR = "Please enter valid Measurement Period dates.";
	
	/** The measure selection error. */
	private final String MEASURE_SELECTION_ERROR = "Please select at least one measure";
	
	/** The modify qdm attribute validation. */
	private final String MODIFY_QDM_ATTRIBUTE_VALIDATION = "A value set with an Attribute category must be used for this data element.";
	
	/** The modify qdm non attribute validation. */
	private final String MODIFY_QDM_NON_ATTRIBUTE_VALIDATION = "A value set with a non-Attribute category must be used for this data element.";
	
	/** The modify qdm select atleast one. */
	private final String MODIFY_VALUE_SET_SELECT_ATLEAST_ONE = "Please select atleast one applied value set to modify.";
	
	/** The must contain lower. */
	private final String MUST_CONTAIN_LOWER = "Must contain a lowercase letter.";
	
	/** The must contain number. */
	private final String MUST_CONTAIN_NUMBER = "Must contain a number.";
	
	/** The must contain special. */
	private final String MUST_CONTAIN_SPECIAL = "Must contain a special character.";
	
	/** The must contain upper. */
	private final String MUST_CONTAIN_UPPER = "Must contain an uppercase letter.";
	
	/** The must not contain dictionay word. */
	private final String MUST_NOT_CONTAIN_DICTIONAY_WORD = "Passwords must not consist of a single dictionary word with letters, numbers and symbols.";
	
	/** The must not contain login id. */
	private final String MUST_NOT_CONTAIN_LOGIN_ID = "Passwords must not contain your User ID.";
	
	/** The must not contain runs. */
	private final String MUST_NOT_CONTAIN_RUNS = "Passwords must not contain three consecutive identical characters.";
	
	/** The name required. */
	private final String NAME_REQUIRED = "Value Set Name is required.";
	
	/** The not unique questions. */
	private final String NOT_UNIQUE_QUESTIONS = "You cannot use the same security question more than once.";
	
	/** The oid in use. */
	private final String OID_IN_USE = "OID is already in use.";
	
	/** The package success. */
	private final String PACKAGE_SUCCESS = "Measure packaged successfully. Please access the Measure Library to export the measure.";
	
	/** The package success with some OID's not retrived from VSAC. */
	private final String PACKAGE_SUCCESS_VSAC_OID_MISSING = "Measure packaged successfully. One or more OIDs could not be updated from VSAC.";
	
	/** The password mismatch. */
	private final String PASSWORD_MISMATCH = "Your new password and confirm password do not match.";
	
	/** The is not previous password. */
	private final String IS_NOT_CURRENT_PASSWORD="New password cannot be the same as the previous 6 passwords.";
	
	/** The is previous password cannot be reused. */
	private final String IS_NOT_PREVIOUS_PASSWORD="Previous 6 passwords cannot be reused. Try again.";
	
	/** The change old password. */
	private final String CHANGE_OLD_PASSWORD="Password needs to be at least one day old before you can change it. Try again.";
	
	/** The password mismatch error message. */
	private final String PASSWORD_MISMATCH_ERROR_MESSAGE = "Incorrect password supplied.Try again";
	
	/** The password required. */
	private final String PASSWORD_REQUIRED = "Password is required.";
	
	/** The password required error message. */
	private final String PASSWORD_REQUIRED_ERROR_MESSAGE = "Existing password is required to confirm changes.";
	
	/** The password wrong length. */
	private final String PASSWORD_WRONG_LENGTH = "Must be between 8 and 16 characters long.";
	
	/** Security-code/OneTimePassword required. */
	private final String SECURITYCODE_REQUIRED = "Security Code is required.";
	
	/** The PHON e_10_ digit. */
	private final String PHONE_10_DIGIT = "Phone Number is required to be 10 digits.";
	
	/** The proportion may not contain. */
	private final String PROPORTION_MAY_NOT_CONTAIN = "For a Proportion measure,a grouping may not contain a Numerator Exclusion,Measure Population,or Measure Observation.";
	
	/** The proportion too few. */
	private final String PROPORTION_TOO_FEW = "For a Proportion measure, a grouping may not contain less than one Numerator.";
	
	/** The proportion too many. */
	private final String PROPORTION_TOO_MANY = "For a Proportion measure, a grouping may not contain more than one of each of the following: "
			+ "Denominator Exclusions and Denominator Exceptions.";
	
	/*
	 * PROPORTION
	 */
	/** The proportion wrong num. */
	private final String PROPORTION_WRONG_NUM = "For a Proportion measure, a grouping must contain exactly one of each of the following: "
			+ "Initial Population, Denominator and Numerator.";
	
	/** The ratio may not contain. */
	private final String RATIO_MAY_NOT_CONTAIN = "For a Ratio measure,a grouping may not contain a Denominator Exception,or Measure Population.";
	
	/** The ratio too many. */
	private final String RATIO_TOO_MANY = "For a Ratio measure, a grouping may not contain more than one of each of the following: Denominator Exclusion and Numerator Exclusion.";
	
	/** The ratio too many populations. */
	private final String RATIO_TOO_MANY_POPULATIONS = "For a Ratio measure, a grouping may not contain more than two of the following: Initial Populations.";
	/*
	 * RATIO
	 */
	/** The ratio wrong num. */
	private final String RATIO_WRONG_NUM = "For a Ratio measure, a grouping must contain exactly one of each of the following: "
			+ "Denominator and Numerator.";
	
	/** The ratio too few populations. */
	private final String RATIO_TOO_FEW_POPULATIONS = "For a Ratio measure, a grouping must contain at least one Initial Population.";
	
	/** The invalid logic population work space. */
	private final String INVALID_LOGIC_CQL_WORK_SPACE = "Measure Logic is incomplete.Please validate your measure logic in CQL Workspace.";
	
	/** The invalidlogic clause work space. */
	private final String INVALIDLOGIC_CLAUSE_WORK_SPACE = "Clause logic is incomplete.Please validate your clause logic.";
	/**
	 * Gets the ratio too few populations.
	 *
	 * @return the rATIO_TOO_FEW_POPULATIONS
	 */
	public String getRATIO_TOO_FEW_POPULATIONS() {
		return RATIO_TOO_FEW_POPULATIONS;
	}
	
	/** The cohort wrong num. */
	private final String COHORT_WRONG_NUM = "For a Cohort measure, a grouping must contain exactly one Initial Population.";
	
	/** The stratification validation for grouping. */
	private final String STRATIFICATION_VALIDATION_FOR_GROUPING = " Measure Grouping cannot contain more than one Stratification.";
	
	/** The measure observation validation for grouping. */
	private final String MEASURE_OBS_VALIDATION_FOR_GROUPING = " A Ratio Measure may not contain more than 2 Measure Observations in a Measure grouping.";
	
	
	/** The rationale required. */
	private final String RATIONALE_REQUIRED = "Rationale is required.";
	
	/** The relationalop two child message. */
	private final String RELATIONALOP_TWO_CHILD_MESSAGE = "Package Failed. Measure logic is incomplete."
			+" LHS and RHS are required for Timings and Relationships.";
	
	/** The save error msg. */
	private final String SAVE_ERROR_MSG = "You have unsaved changes that will be discarded if you continue. "
			+ "Do you want to continue without saving?";
	
	// TODO is this such a smart policy?
	// This makes denial of service attacks on accounts too easy.
	// Perhaps make them answer a security question?
	/** The seccond attempt failed. */
	//private final String SECCOND_ATTEMPT_FAILED = "Failed 2nd attempt. Next attempt will lock the account.";
	
	/** The security not answered. */
	private final String SECURITY_NOT_ANSWERED = "Your security questions have not been answered.  You cannot continue. Please contact the Helpdesk";
	
	/** The security q mismatch. */
	private final String SECURITY_Q_MISMATCH = "The answer for the security question did not match.";
	
	/** The security question header. */
	private final String SECURITY_QUESTION_HEADER = "The answer(s) provided to the security question(s) do not match the following rules:";
	
	/** The server call null. */
	private final String SERVER_CALL_NULL = "Server call for login returned null.";
	
	/** The server side validation. */
	private final String SERVER_SIDE_VALIDATION = "Server Side Validation";
	
	/** The steward required. */
	private final String STEWARD_REQUIRED = "Steward is required.";
	
	/** The successful modify applied qdm. */
	private final String SUCCESSFUL_MODIFY_APPLIED_VALUESET = "Selected value set has been modified successfully.";
	
	/** The system error. */
	private final String SYSTEM_ERROR = "Import failed due to system error. Please try again.";
	
	/** The transfer checkbox error. */
	private final String TRANSFER_CHECKBOX_ERROR = "Please select at least one Value Set to transfer ownership.";
	
	/** The transfer checkbox error measure. */
	private final String TRANSFER_CHECKBOX_ERROR_MEASURE = "Please select at least one Measure to transfer ownership.";
	
	/** The transfer checkbox error measure. */
	private final String TRANSFER_CHECKBOX_ERROR_CQL_LIBRARY = "Please select at least one CQL Library to transfer ownership.";
	
	/** The transfer ownership success. */
	private final String TRANSFER_OWNERSHIP_SUCCESS = "Ownership successfully transferred to ";
	
	/** The uml login failed. */
	private final String UML_LOGIN_FAILED = "Login failed. Please sign in again.";
	
	/** The uml login unavailable. */
	private final String UML_LOGIN_UNAVAILABLE = "Unable to verify your UMLS credentials. Please contact the MAT Help Desk or try again.";
	
	/** The umls not loggedin. */
	private final String UMLS_NOT_LOGGEDIN = "You are not logged in to UMLS. Please access the UMLS Account tab to continue.";
	
	/** The umls oid required. */
	private final String UMLS_OID_REQUIRED = "Please enter an OID.";
	
	/** The umls oid required. */
	private final String UMLS_CODE_IDENTIFIER_REQUIRED = "Please enter a code identifier.";
	
	/** The umls invalid code identifier. */
	private final String UMLS_INVALID_CODE_IDENTIFIER = "Invalid code identifier. "
			+ "Please copy the complete URL for the code directly from VSAC and try again.";
	
	/** The umls successfull login. */
	private final String UMLS_SUCCESSFULL_LOGIN = "Successfully logged into UMLS";
	
	/** The unable to process. */
	private final String UNABLE_TO_PROCESS = "System error.  Unable to process information.";
	
	/** The unknown fail. */
	private final String UNKNOWN_FAIL = "Unknown failure reason";
	
	/** The user not found msg. */
	private final String USER_NOT_FOUND_MSG = "User ID is required.";
	
	/** The user required error msg. */
	private final String USER_REQUIRED_ERROR_MSG = "Please select a user to transfer ownership.";
	
	/** The validation msg data type vsac. */
	private final String VALIDATION_MSG_DATA_TYPE_VSAC = "Please select datatype from drop down list.";
	
	/** The validation msg element without vsac. */
	private final String VALIDATION_MSG_ELEMENT_WITHOUT_VSAC = "Please enter value set name.";
	
	/** The value set date invalid. */
	private final String VALUE_SET_DATE_INVALID = "Value Set Package Date is not a valid date.";
	
	/** The value set date required. */
	private final String VALUE_SET_DATE_REQUIRED = "Value Set Package Date is required.";
	
	/** The vsac retrieve failed. */
	private final String VSAC_RETRIEVE_FAILED = "Unable to retrieve from VSAC. Please check the data and try again.";
	
	/** The vsac update successfull. */
	private final String VSAC_UPDATE_SUCCESSFULL = "Successfully updated applied Value Set list with VSAC data.";
	
	
	/** The comment added successfully. */
	private final String COMMENT_ADDED_SUCCESSFULLY ="Comment Changes Added.";
	
	/** The comparison diloag box error display. */
	private final String COMPARISON_DILOAG_BOX_ERROR_DISPLAY="Please enter Quantity field.";
	
	/** The comparison diloag box error display. */
	private final String COMPARISON_DILOAG_BOX_UNIT_ERROR_DISPLAY="Please select Unit.";
	
	/** The component measures added successfully. */
	private final String COMPONENT_MEASURES_ADDED_SUCCESSFULLY="Component Measures updated successfully. Changes must be saved on the previous page.";
	
	/** The removed functions error message. */
	private final String POPULATION_WORKSPACE_DATETIMEDIFF_ERROR_MESSAGE = "Highlighted clause contains Datetimediff function.";
	
	/** The clause work space invalid nested clause. */
	private final String CLAUSE_WORK_SPACE_INVALID_NESTED_CLAUSE ="Invalid clause(s) used in logic.";
	
	/** The clause work space invalid nested clause. */
	private final String CLAUSE_WORK_SPACE_INVALID_ATTRIBUTE_IN_SAT_ALL_ANY ="Satisfies All and Satisfies Any LHS QDM element may not contain attributes.";
	
	/** The clause work space invalid nested clause. */
	private final String CLAUSE_WORK_SPACE_INVALID_NESTED_DEPTH_CLAUSE = "Clause Exceeds Maximum Number of Nested Logic Levels (10).";
	
	/** The clause work space invalid nested clause. */
	private final String CLAUSE_WORK_SPACE_INVALID_LOGICAL_OPERATOR = "Any logical operator under a top-level logical operator must contain at least one logical operator or clause. " +
			"Any terminal logical operator under a top-level logical operator must contain at least one clause.";
	
	/** The measure observation validation for functions. */
	private final String MEASURE_OBSERVATION_VALIDATION_FOR_FUNCTIONS = "Contents of the clause logic are not permitted for Measure Observations.";
	
	/** The only one child required. */
	private final String ONLY_ONE_CHILD_REQUIRED = " Functions must contain only one child node.";
	
	/** The invalid logic measure packager. */
	private final String INVALID_LOGIC_MEASURE_PACKAGER = "Populations or Measure Observations within a Measure Grouping must contain a valid Definition or Function.";
	
	/** The delete confirmation parameter. */
	private final String DELETE_CONFIRMATION_PARAMETER = "You have selected to delete this expression. Do you want to permanently delete this Parameter?";
	
	/** The delete confirmation definition. */
	private final String DELETE_CONFIRMATION_DEFINITION = "You have selected to delete this expression. Do you want to permanently delete this Definition?";
	
	/** The delete confirmation function. */
	private final String DELETE_CONFIRMATION_FUNCTION = "You have selected to delete this expression. Do you want to permanently delete this Function?";
	
	/** The delete confirmation include. */
	private final String DELETE_CONFIRMATION_INCLUDE = "You have selected to delete this expression. Do you want to permanently delete this Library Alias?";
	
	/** The error library version. */
	private final String ERROR_LIBRARY_VERSION ="Please select version type Major or Minor.";
	
	/** The error library draft. */
	private final String ERROR_LIBRARY_DRAFT = "Please select a Library Version to create a draft.";
	
	/** The error select attribute name. */
	private final String ERROR_SELECT_ATTRIBUTE_NAME = "Please select Attribute Name.";
	
	/** The error select item name. */
	private final String ERROR_SELECT_ITEM_NAME = "Please select Item Name.";
	
	/** The error select item type. */
	private final String ERROR_SELECT_ITEM_TYPE = "Please select Item Type.";
	
	/** The error select item name or data type. */
	private final String ERROR_SELECT_ITEM_NAME_OR_DATA_TYPE = "Please select Item Name or Datatype.";
	
	/** The error invalid code data type. */
	private final String ERROR_INVALID_CODE_DATA_TYPE = "Invalid code/datatype combination.";
	
	/** The error invalid quantity. */
	private final String ERROR_INVALID_QUANTITY = "Please Enter valid Quantity.";
	
	/** The error invalid date time quantity. */
	private final String ERROR_INVALID_DATE_TIME_QUANTITY = "You can not enter both DateTime and Quantity.";
	
	/** The error invalid mode details. */
	private final String ERROR_INVALID_MODE_DETAILS = "Please Select valid Mode Details.";
	
	/** The error select attribute to insert. */
	private final String ERROR_SELECT_ATTRIBUTE_TO_INSERT = "Please Select Attribute to insert into Editor";
	
	/** The error enter date time and quantity. */
	private final String ERROR_ENTER_DATE_TIME_AND_QUANTITY = "Please enter DateTime or Quantity/units.";
	
	/** The error invalid date time. */
	private final String ERROR_INVALID_DATE_TIME = "Please Enter a valid Date/Time.";
	
	private final String WARNING_DELETION_CQL_LIBRARY = "Deleting a draft of a library will permanently remove the designated "
			+ "library draft from the Measure Authoring Tool. Deleted libraries cannot be recovered.";
	
	/** The cql library deletion success msg. */
	private final String CQL_LIBRARY_DELETION_SUCCESS_MSG = "CQL Library successfully deleted.";
	
	/**
	 * Gets the only one child required.
	 *
	 * @return the only one child required
	 */
	public String getONLY_ONE_CHILD_REQUIRED() {
		return ONLY_ONE_CHILD_REQUIRED;
	}
	
	/**
	 * Gets the measure observation validation for functions.
	 *
	 * @return the measure observation validation for functions
	 */
	public String getMEASURE_OBSERVATION_VALIDATION_FOR_FUNCTIONS() {
		return MEASURE_OBSERVATION_VALIDATION_FOR_FUNCTIONS;
	}
	
	/**
	 * Gets the component measures added successfully.
	 *
	 * @return the component measures added successfully
	 */
	public String getCOMPONENT_MEASURES_ADDED_SUCCESSFULLY() {
		return COMPONENT_MEASURES_ADDED_SUCCESSFULLY;
	}
	
	/**
	 * Gets the abv name required message.
	 * 
	 * @return String
	 */
	public String getAbvNameRequiredMessage() {
		return ABV_NAME_REQUIRED;
	}
	
	/**
	 * Gets the account locked message.
	 * 
	 * @return String
	 */
	public String getAccountLockedMessage() {
		return ACCOUNT_LOCKED;
	}
	
	/**
	 * Gets the account revoked message.
	 * 
	 * @return String
	 */
	public String getAccountRevokedMessage() {
		return ACCOUNT_REVOKED;
	}
	
	/**
	 * Gets the account termination.
	 * 
	 * @return String
	 */
	public String getAccountTermination() {
		return ACCOUNT_TERMINATION;
	}
	
	/**
	 * Gets the alert loading message.
	 * 
	 * @return String
	 */
	public String getAlertLoadingMessage() {
		return ALERT_LOADING_MESSAGE;
	}
	
	/**
	 * Gets the markup not allowed message.
	 * 
	 * @return String
	 */
	public String getNoMarkupAllowedMessage() {
		return NO_MARKUP_ALLOWED;
	}
	
	/**
	 * Gets the all password fields required.
	 * 
	 * @return String
	 */
	public String getAllPasswordFieldsRequired() {
		return ALL_PASSWORD_FIELDS_REQUIRED;
	}
	
	/**
	 * Gets the category required message.
	 * 
	 * @return String
	 */
	public String getCategoryRequiredMessage() {
		return CATEGORY_REQUIRED;
	}
	
	/**
	 * Gets the changes saved message.
	 * 
	 * @return String
	 */
	public String getChangesSavedMessage() {
		return CHANGES_SAVED;
	}
	
	
	/**
	 * Gets the clause work space validation success.
	 * 
	 * @return String the cLAUSE_WORK_SPACE_VALIDATION_SUCCESS
	 */
	public String getCLAUSE_WORK_SPACE_VALIDATION_SUCCESS() {
		return CLAUSE_WORK_SPACE_VALIDATION_SUCCESS;
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
	 * Gets the code list added group message.
	 * 
	 * @return String
	 */
	public String getCodeListAddedGroupMessage() {
		return CODE_LIST_ADDED_GROUP;
	}
	
	/**
	 * Gets the code list added message.
	 * 
	 * @return String
	 */
	public String getCodeListAddedMessage() {
		return CODE_LIST_ADDED;
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
	 * Gets the code list required message.
	 * 
	 * @return String
	 */
	public String getCodeListRequiredMessage() {
		return CODE_LIST_REQUIRED;
	}
	
	/**
	 * Gets the code required message.
	 * 
	 * @return String
	 */
	public String getCodeRequiredMessage() {
		return CODE_REQUIRED;
	}
	
	/**
	 * Gets the code system required message.
	 * 
	 * @return String
	 */
	public String getCodeSystemRequiredMessage() {
		return CODE_SYSTEM_REQUIRED;
	}
	
	/**
	 * Gets the code version required message.
	 * 
	 * @return String
	 */
	public String getCodeVersionRequiredMessage() {
		return CODE_VERSION_REQUIRED;
	}
	
	/**
	 * Gets the continuous variable may not contain message.
	 * 
	 * @return String
	 */
	public String getContinuousVariableMayNotContainMessage() {
		return CONTINUOUS_VARIABLE_MAY_NOT_CONTAIN;
	}
	
	/**
	 * Gets the continuous variable wrong num message.
	 * 
	 * @return String
	 */
	public String getContinuousVariableWrongNumMessage() {
		return CONTINUOUS_VARIABLE_WRONG_NUM;
	}
	
	/**
	 * Gets the delete measure warning message.
	 * 
	 * @return String the dELETE_MEASURE_WARNING_MESSAGE
	 */
	public String getDELETE_MEASURE_WARNING_MESSAGE() {
		return DELETE_MEASURE_WARNING_MESSAGE;
	}
	
	/**
	 * Gets the description required meassage.
	 * 
	 * @return String
	 */
	public String getDescriptionRequiredMeassage() {
		return DESCRIPTION_REQUIRED;
	}
	
	/**
	 * Gets the descriptor required message.
	 * 
	 * @return String
	 */
	public String getDescriptorRequiredMessage() {
		return DESCRIPTOR_REQUIRED;
	}
	
	/**
	 * Gets the doesnt follow rules message.
	 * 
	 * @return String
	 */
	public String getDoesntFollowRulesMessage() {
		return DOESNT_FOLLOW_RULES;
	}
	
	/**
	 * Gets the duplicate applied qdm msg.
	 * 
	 * @return String
	 */
	public String getDuplicateAppliedValueSetMsg() {
		return DUPLICATE_APPLIED_VALUE_SET;
	}
	
	/**
	 * Gets the duplicate codes message.
	 * 
	 * @return String
	 */
	public String getDuplicateCodesMessage() {
		return DUPLICATE_CODES_MSG;
	}
	
	/**
	 * Gets the duplicate error message.
	 * 
	 * @return String
	 */
	public String getDuplicateErrorMessage() {
		return DUPLICATE_ERROR;
	}
	
	/**
	 * Gets the email already exists message.
	 * 
	 * @return String
	 */
	public String getEmailAlreadyExistsMessage() {
		return EMAIL_ALREADY_EXISTS;
	}
	
	/**
	 * Gets the email mismatch message.
	 * 
	 * @return String
	 */
	public String getEmailMismatchMessage() {
		return EMAIL_MISMATCH;
	}
	
	/**
	 * Gets the email not found message.
	 * 
	 * @return String
	 */
	public String getEmailNotFoundMessage() {
		return EMAIL_NOT_FOUND_MSG;
	}
	
	/**
	 * Gets the empty file error.
	 * 
	 * @return String
	 */
	public String getEmptyFileError() {
		return EMPTY_FILE_ERROR;
	}
	
	/**
	 * Gets the excel file type message.
	 * 
	 * @return String
	 */
	public String getExcelFileTypeMessage() {
		return EXCEL_FILE_TYPE;
	}
	
	/**
	 * Gets the file not selected message.
	 * 
	 * @return String
	 */
	public String getFileNotSelectedMessage() {
		return FILE_NOT_SELECTED;
	}
	
	/**
	 * Gets the first min message.
	 * 
	 * @return String
	 */
	public String getFirstMinMessage() {
		return FIRST_NAME_MIN;
	}
	
	/**
	 * Gets the first name required message.
	 * 
	 * @return String
	 */
	public String getFirstNameRequiredMessage() {
		return FIRST_NAME_REQUIRED;
	}
	
	/**
	 * Gets the generic error message.
	 * 
	 * @return String
	 */
	public String getGenericErrorMessage() {
		return GENERIC_ERROR_MESSAGE;
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
	
	/**
	 * Gets the grouped name required message.
	 * 
	 * @return String
	 */
	public String getGroupedNameRequiredMessage() {
		return GROUPED_NAME_REQUIRED;
	}
	
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
	
	/**
	 * Gets the grouping required message.
	 * 
	 * @return String
	 */
	public String getGroupingRequiredMessage() {
		return GROUPING_REQUIRED;
	}
	
	/**
	 * Gets the grouping saved message.
	 * 
	 * @return String
	 */
	public String getGroupingSavedMessage() {
		return GROUPING_SAVED;
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
	 * Gets the invalid last modified date message.
	 * 
	 * @return String
	 */
	public String getInvalidLastModifiedDateMessage() {
		return INVALID_LAST_MODIFIED_DATE;
	}
	
	/**
	 * Gets the invalid template message.
	 * 
	 * @return String
	 */
	public String getInvalidTemplateMessage() {
		return INVALID_TEMPLATE;
	}
	
	/**
	 * Gets the last modified date not unique message.
	 * 
	 * @return String
	 */
	public String getLastModifiedDateNotUniqueMessage() {
		return LAST_MODIFIED_DATE_NOT_UNIQUE;
	}
	
	/**
	 * Gets the last name required message.
	 * 
	 * @return String
	 */
	public String getLastNameRequiredMessage() {
		return LAST_NAME_REQUIRED;
	}
	
	/**
	 * Gets the login failed already logged in message.
	 * 
	 * @return String
	 */
	public String getLoginFailedAlreadyLoggedInMessage() {
		return LOGIN_FAILED_USER_ALREADY_LOGGED_IN;
	}
	
	/**
	 * Gets the login failed message.
	 * 
	 * @return String
	 */
	public String getLoginFailedMessage() {
		return LOGIN_FAILED;
	}
	
	/**
	 * Gets the login failed temp password expired message.
	 * 
	 * @return String
	 */
	public String getLoginFailedTempPasswordExpiredMessage() {
		return LOGIN_FAILED_TEMP_PASSWORD_EXPIRED;
	}
	
	/**
	 * Gets the login id required message.
	 * 
	 * @return String
	 */
	public String getLoginIDRequiredMessage() {
		return LOGIN_ID_REQUIRED;
	}
	
	/**
	 * Gets the email id incorrect format message.
	 * 
	 * @return String
	 */
	public String getEmailIdFormatIncorrectMessage() {
		return EMAIL_ID_INCORRECT_FORMAT;
	}
	
	/**
	 * Gets the login user required message.
	 * 
	 * @return String
	 */
	public String getLoginUserRequiredMessage() {
		return LOGIN_USER_REQUIRED;
	}
	
	/**
	 * Gets the measure notes delete success message.
	 * 
	 * @return String
	 */
	public String getMEASURE_NOTES_DELETE_SUCCESS_MESSAGE() {
		return MEASURE_NOTES_DELETE_SUCCESS_MESSAGE;
	}
	
	/**
	 * Gets the measure notes required message.
	 * 
	 * @return String String
	 */
	public String getMEASURE_NOTES_REQUIRED_MESSAGE() {
		return MEASURE_NOTES_REQUIRED_MESSAGE;
	}
	
	/**
	 * Gets the measure notes save success message.
	 * 
	 * @return String
	 */
	public String getMEASURE_NOTES_SAVE_SUCCESS_MESSAGE() {
		return MEASURE_NOTES_SAVE_SUCCESS_MESSAGE;
	}
	
	/**
	 * Gets the measure package umls not logged in.
	 * 
	 * @return String the mEASURE_PACKAGE_UMLS_NOT_LOGGED_IN
	 */
	public String getMEASURE_PACKAGE_UMLS_NOT_LOGGED_IN() {
		return MEASURE_PACKAGE_UMLS_NOT_LOGGED_IN;
	}
	
	/**
	 * Gets the measure package failed due to vsac timeout message.
	 * 
	 * @return String the MEASURE_PACKAGE_FAILED_VSAC_TIMEOUT
	 */
	public String getMEASURE_PACKAGE_VSAC_TIMEOUT() {
		return MEASURE_PACKAGE_FAILED_VSAC_TIMEOUT;
	}
	
	/**
	 * Gets the measure package failed due to invalid period dates message.
	 * 
	 * @return String the MEASURE_PERIOD_DATES_ERROR
	 */
	public String getMEASURE_PERIOD_DATES_ERROR() {
		return MEASURE_PERIOD_DATES_ERROR;
	}
	
	
	/**
	 * Gets the measure deletion invalid pwd.
	 * 
	 * @return String
	 */
	public String getMeasureDeletionInvalidPwd() {
		return MEASURE_DELETION_INVALID_PWD;
	}
	
	/**
	 * Gets the measure deletion success mgs.
	 * 
	 * @return String
	 */
	public String getMeasureDeletionSuccessMgs() {
		return MEASURE_DELETION_SUCCESS_MSG;
	}
	
	/**
	 * Gets the measure name required message.
	 * 
	 * @return String
	 */
	public String getMeasureNameRequiredMessage() {
		return MEASURE_NAME_REQUIRED;
	}
	
	/**
	 * Gets the measure selection error.
	 * 
	 * @return String
	 */
	public String getMeasureSelectionError() {
		return MEASURE_SELECTION_ERROR;
	}
	
	/**
	 * Gets the modify qdm attribute validation.
	 * 
	 * @return String the mODIFY_QDM_ATTRIBUTE_VALIDATION
	 */
	public String getMODIFY_QDM_ATTRIBUTE_VALIDATION() {
		return MODIFY_QDM_ATTRIBUTE_VALIDATION;
	}
	
	/**
	 * Gets the modify qdm non attribute validation.
	 * 
	 * @return String the mODIFY_QDM_NON_ATTRIBUTE_VALIDATION
	 */
	public String getMODIFY_QDM_NON_ATTRIBUTE_VALIDATION() {
		return MODIFY_QDM_NON_ATTRIBUTE_VALIDATION;
	}
	
	/**
	 * Gets the modify qdm select atleast one.
	 * 
	 * @return String the mODIFY_QDM_SELECT_ATLEAST_ONE
	 */
	public String getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE() {
		return MODIFY_VALUE_SET_SELECT_ATLEAST_ONE;
	}
	
	/**
	 * Gets the must contain lower message.
	 * 
	 * @return String
	 */
	public String getMustContainLowerMessage() {
		return MUST_CONTAIN_LOWER;
	}
	
	/**
	 * Gets the must contain number message.
	 * 
	 * @return String
	 */
	public String getMustContainNumberMessage() {
		return MUST_CONTAIN_NUMBER;
	}
	
	/**
	 * Gets the must contain special message.
	 * 
	 * @return String
	 */
	public String getMustContainSpecialMessage() {
		return MUST_CONTAIN_SPECIAL;
	}
	
	/**
	 * Gets the must contain upper message.
	 * 
	 * @return String
	 */
	public String getMustContainUpperMessage() {
		return MUST_CONTAIN_UPPER;
	}
	
	/**
	 * Gets the must not contain dictionary word message.
	 * 
	 * @return String
	 */
	public String getMustNotContainDictionaryWordMessage() {
		return MUST_NOT_CONTAIN_DICTIONAY_WORD;
	}
	
	/**
	 * Gets the must not contain login id message.
	 * 
	 * @return String
	 */
	public String getMustNotContainLoginIdMessage() {
		return MUST_NOT_CONTAIN_LOGIN_ID;
	}
	
	/**
	 * Gets the must not contain runs message.
	 * 
	 * @return String
	 */
	public String getMustNotContainRunsMessage() {
		return MUST_NOT_CONTAIN_RUNS;
	}
	
	/**
	 * Gets the name required message.
	 * 
	 * @return the name required message
	 */
	public String getNameRequiredMessage() {
		return NAME_REQUIRED;
	}
	
	/**
	 * Gets the no code lists message.
	 * 
	 * @return String
	 */
	public String getNoCodeListsMessage() {
		return NO_CODE_LISTS;
	}
	
	/**
	 * Gets the no measures message.
	 * 
	 * @return String
	 */
	public String getNoMeasuresMessage() {
		return NO_MEASURES;
	}
	
	/**
	 * Gets the not unique questions.
	 * 
	 * @return String
	 */
	public String getNotUniqueQuestions() {
		return NOT_UNIQUE_QUESTIONS;
	}
	
	/**
	 * Gets the oID in use message.
	 * 
	 * @return String
	 */
	public String getOIDInUseMessage() {
		return OID_IN_USE;
	}
	
	/**
	 * Gets the oID required message.
	 * 
	 * @return String
	 */
	public String getOIDRequiredMessage() {
		return OID_REQUIRED;
	}
	
	/**
	 * Gets the oID too long message.
	 * 
	 * @return String
	 */
	public String getOIDTooLongMessage() {
		return OID_TOO_LONG;
	}
	
	/**
	 * Gets the one and message.
	 * 
	 * @return String
	 */
	public String getOneAndMessage() {
		return ONE_AND;
	}
	
	/**
	 * Gets the org required message.
	 * 
	 * @return String
	 */
	public String getOrgRequiredMessage() {
		return ORG_REQUIRED;
	}
	
	/**
	 * Gets the package success message.
	 * 
	 * @return String {@link String}
	 */
	public final String getPackageSuccessMessage() {
		return PACKAGE_SUCCESS;
	}
	
	/**
	 * Gets the package success amber message.
	 *
	 * @return the package success amber message
	 */
	public final String getPackageSuccessAmberMessage() {
		return PACKAGE_SUCCESS_VSAC_OID_MISSING;
	}
	
	/**
	 * Gets the password mismatch error message.
	 * 
	 * @return String
	 */
	public String getPasswordMismatchErrorMessage() {
		return PASSWORD_MISMATCH_ERROR_MESSAGE;
	}
	
	/**
	 * Gets the password mismatch message.
	 * 
	 * @return String
	 */
	public String getPasswordMismatchMessage() {
		return PASSWORD_MISMATCH;
	}
	
	/**
	 * Gets the password required error message.
	 * 
	 * @return String
	 */
	public String getPasswordRequiredErrorMessage() {
		return PASSWORD_REQUIRED_ERROR_MESSAGE;
	}
	
	/**
	 * Gets the password required message.
	 * 
	 * @return String
	 */
	public String getPasswordRequiredMessage() {
		return PASSWORD_REQUIRED;
	}
	
	/**
	 * Gets the security code required message.
	 * 
	 * @return String
	 */
	public String getSecurityCodeRequiredMessage() {
		return SECURITYCODE_REQUIRED;
	}
	
	/**
	 * Gets the password saved message.
	 * 
	 * @return the password saved message
	 */
	public String getPasswordSavedMessage() {
		return PASSWORD_CHANGED;
	}
	
	
	/**
	 * Gets the password wrong length message.
	 * 
	 * @return String
	 */
	public String getPasswordWrongLengthMessage() {
		return PASSWORD_WRONG_LENGTH;
	}
	
	/**
	 * Gets the personal info updated message.
	 * 
	 * @return String
	 */
	public String getPersonalInfoUpdatedMessage() {
		return PERSONAL_INFO_UPDATED;
	}
	
	/**
	 * Gets the phone required message.
	 * 
	 * @return String
	 */
	public String getPhoneRequiredMessage() {
		return PHONE_REQUIRED;
	}
	
	/**
	 * Gets the phone ten digit message.
	 * 
	 * @return String
	 */
	public String getPhoneTenDigitMessage() {
		return PHONE_10_DIGIT;
	}
	
	/**
	 * Gets the proportion may not contain message.
	 * 
	 * @return String
	 */
	public String getProportionMayNotContainMessage() {
		return PROPORTION_MAY_NOT_CONTAIN;
	}
	
	/**
	 * Gets the proportion too few message.
	 * 
	 * @return String
	 */
	public String getProportionTooFewMessage() {
		return PROPORTION_TOO_FEW;
	}
	
	/**
	 * Gets the proportion too many message.
	 * 
	 * @return String
	 */
	public String getProportionTooManyMessage() {
		return PROPORTION_TOO_MANY;
	}
	
	/**
	 * Gets the proportion wrong num message.
	 * 
	 * @return String
	 */
	public String getProportionWrongNumMessage() {
		return PROPORTION_WRONG_NUM;
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
	 * Gets the ratio may not contain message.
	 * 
	 * @return String
	 */
	public String getRatioMayNotContainMessage() {
		return RATIO_MAY_NOT_CONTAIN;
	}
	
	/**
	 * Gets the rationale required message.
	 * 
	 * @return String
	 */
	public String getRationaleRequiredMessage() {
		return RATIONALE_REQUIRED;
	}
	
	/**
	 * Gets the ratio too many message.
	 * 
	 * @return String
	 */
	public String getRatioTooManyMessage() {
		return RATIO_TOO_MANY;
	}
	
	/**
	 * Gets the ratio wrong num message.
	 * 
	 * @return String
	 */
	public String getRatioWrongNumMessage() {
		return RATIO_WRONG_NUM;
	}
	
	/**
	 * Gets the relational op two child message.
	 * 
	 * @return String
	 */
	public String getRelationalOpTwoChildMessage() {
		return RELATIONALOP_TWO_CHILD_MESSAGE;
	}
	
	/**
	 * Gets the root oid required message.
	 * 
	 * @return String
	 */
	public String getRootOIDRequiredMessage() {
		return ROOT_OID_REQUIRED;
	}
	
	/**
	 * Gets the root oid too long message.
	 * 
	 * @return String
	 */
	public String getRootOIDTooLongMessage() {
		return ROOT_OID_TOO_LONG;
	}
	
	/**
	 * Gets the save error msg.
	 * 
	 * @return String the sAVE_ERROR_MSG
	 */
	public String getSaveErrorMsg() {
		return SAVE_ERROR_MSG;
	}
	
	/**
	 * Gets the second attempt failed message.
	 *
	 * @param index the index
	 * @return String
	 */
	/*	public String getSecondAttemptFailedMessage() {
		return SECCOND_ATTEMPT_FAILED;
	}
	 */
	
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
	
	/**
	 * Gets the security not answered message.
	 * 
	 * @return String
	 */
	public String getSecurityNotAnsweredMessage() {
		return SECURITY_NOT_ANSWERED;
	}
	
	/**
	 * Gets the security q mismatch message.
	 * 
	 * @return String
	 */
	public String getSecurityQMismatchMessage() {
		return SECURITY_Q_MISMATCH;
	}
	
	/**
	 * Gets the security question heeader message.
	 * 
	 * @return String
	 */
	public String getSecurityQuestionHeeaderMessage() {
		return SECURITY_QUESTION_HEADER;
	}
	
	/**
	 * Gets the security questions updated message.
	 * 
	 * @return String
	 */
	public String getSecurityQuestionsUpdatedMessage() {
		return SEC_QUESTIONS_UPDATED;
	}
	
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
	 * Gets the server side validation message.
	 * 
	 * @return String
	 */
	public String getServerSideValidationMessage() {
		return SERVER_SIDE_VALIDATION;
	}
	
	/**
	 * Gets the steward required message.
	 * 
	 * @return String
	 */
	public String getStewardRequiredMessage() {
		return STEWARD_REQUIRED;
	}
	
	/**
	 * Gets the successful modify qdm msg.
	 * 
	 * @return String
	 */
	public String getSUCCESSFUL_MODIFY_APPLIED_VALUESET() {
		return SUCCESSFUL_MODIFY_APPLIED_VALUESET;
		
	}
	
	/**
	 * Gets the supp data saved message.
	 * 
	 * @return String
	 */
	public String getSuppDataSavedMessage() {
		return SUPP_DATA_SAVED;
	}
	
	/**
	 * Gets the risk adj saved message.
	 *
	 * @return the risk adj saved message
	 */
	public String getRiskAdjSavedMessage() {
		return RISK_ADJ_SAVED;
	}
	/**
	 * Gets the system error message.
	 * 
	 * @return String
	 */
	public String getSystemErrorMessage() {
		return SYSTEM_ERROR;
	}
	
	/**
	 * Gets the temp email sent message.
	 * 
	 * @return String
	 */
	public String getTempEmailSentMessage() {
		return TEMP_EMAIL_SENT;
	}
	
	/**
	 * Gets the transfer check box error.
	 * 
	 * @return String
	 */
	public String getTransferCheckBoxError() {
		return TRANSFER_CHECKBOX_ERROR;
	}
	
	/**
	 * Gets the transfer check box error measure.
	 * 
	 * @return String
	 */
	public String getTransferCheckBoxErrorMeasure() {
		return TRANSFER_CHECKBOX_ERROR_MEASURE;
	}
	
	/**
	 * Gets the transfer ownership success.
	 * 
	 * @return String
	 */
	public String getTransferOwnershipSuccess() {
		return TRANSFER_OWNERSHIP_SUCCESS;
	}
	
	/**
	 * Gets the uml login failed.
	 * 
	 * @return String
	 */
	public String getUML_LOGIN_FAILED() {
		return UML_LOGIN_FAILED;
	}
	
	/**
	 * Gets the uml login unavailable.
	 * 
	 * @return String
	 */
	public String getUML_LOGIN_UNAVAILABLE() {
		return UML_LOGIN_UNAVAILABLE;
	}
	
	/**
	 * Gets the umls not loggedin.
	 * 
	 * @return String
	 */
	public String getUMLS_NOT_LOGGEDIN() {
		return UMLS_NOT_LOGGEDIN;
	}
	
	/**
	 * Gets the umls oid required.
	 * 
	 * @return String
	 */
	public String getUMLS_OID_REQUIRED() {
		return UMLS_OID_REQUIRED;
	}
	
	/**
	 * Gets the umls successfull login.
	 * 
	 * @return String
	 */
	public String getUMLS_SUCCESSFULL_LOGIN() {
		return UMLS_SUCCESSFULL_LOGIN;
	}
	
	/**
	 * Gets the unable to process message.
	 * 
	 * @return String
	 */
	public String getUnableToProcessMessage() {
		return UNABLE_TO_PROCESS;
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
	
	/**
	 * Gets the unknown fail message.
	 * 
	 * @return String
	 */
	public String getUnknownFailMessage() {
		return UNKNOWN_FAIL;
	}
	
	/**
	 * Gets the user not found message.
	 * 
	 * @return String
	 */
	public String getUserNotFoundMessage() {
		return USER_NOT_FOUND_MSG;
	}
	
	/**
	 * Gets the user required error message.
	 * 
	 * @return String
	 */
	public String getUserRequiredErrorMessage() {
		return USER_REQUIRED_ERROR_MSG;
		
	}
	
	/**
	 * Gets the validation msg data type vsac.
	 * 
	 * @return String
	 */
	public String getVALIDATION_MSG_DATA_TYPE_VSAC() {
		return VALIDATION_MSG_DATA_TYPE_VSAC;
	}
	
	/**
	 * Gets the validation msg element without vsac.
	 * 
	 * @return String
	 */
	public String getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC() {
		return VALIDATION_MSG_ELEMENT_WITHOUT_VSAC;
	}
	
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
	
	/**
	 * Gets the value set date invalid message.
	 * 
	 * @return String
	 */
	public String getValueSetDateInvalidMessage() {
		return VALUE_SET_DATE_INVALID;
	}
	
	/**
	 * Gets the value set date required message.
	 * 
	 * @return String
	 */
	public String getValueSetDateRequiredMessage() {
		return VALUE_SET_DATE_REQUIRED;
	}
	
	/**
	 * Gets the vsac retrieve failed.
	 * 
	 * @return String
	 */
	public String getVSAC_RETRIEVE_FAILED() {
		return VSAC_RETRIEVE_FAILED;
	}
	
	/**
	 * Gets the vsac update successfull.
	 * 
	 * @return String
	 */
	public String getVSAC_UPDATE_SUCCESSFULL() {
		return VSAC_UPDATE_SUCCESSFULL;
	}
	
	/**
	 * Gets the vsac version or effective date required.
	 *
	 * @return the vsac version or effective date required
	 */
	public String getVSAC_VERSION_OR_EFFECTIVE_DATE_REQUIRED() {
		return VSAC_VERSION_OR_EFFECTIVE_DATE_REQUIRED;
	}
	
	/**
	 * Gets the checks if is not previous password.
	 *
	 * @return the checks if is not previous password
	 */
	public String getIS_NOT_PREVIOUS_PASSWORD() {
		return IS_NOT_PREVIOUS_PASSWORD;
	}
	
	/**
	 * Gets the change old password.
	 *
	 * @return the change old password
	 */
	public String getCHANGE_OLD_PASSWORD() {
		return CHANGE_OLD_PASSWORD;
	}
	
	/**
	 * Gets the checks if is not current password.
	 *
	 * @return the checks if is not current password
	 */
	public String getIS_NOT_CURRENT_PASSWORD() {
		return IS_NOT_CURRENT_PASSWORD;
	}
	
	/**
	 * Gets the comment added successfully.
	 *
	 * @return the cOMMENT_ADDED_SUCCESSFULLY
	 */
	public String getCOMMENT_ADDED_SUCCESSFULLY() {
		return COMMENT_ADDED_SUCCESSFULLY;
	}
	
	/**
	 * Gets the cohort wrong num.
	 *
	 * @return the cOHORT_WRONG_NUM
	 */
	public String getCOHORT_WRONG_NUM() {
		return COHORT_WRONG_NUM;
	}
	
	/**
	 * Gets the comparison diloag box error display.
	 *
	 * @return the comparison diloag box error display
	 */
	public String getComparisonDiloagBoxErrorDisplay() {
		return COMPARISON_DILOAG_BOX_ERROR_DISPLAY;
	}
	
	/**
	 * Gets the ratio too many populations.
	 *
	 * @return the rATIO_TOO_MANY_POPULATIONS
	 */
	public String getRATIO_TOO_MANY_POPULATIONS() {
		return RATIO_TOO_MANY_POPULATIONS;
	}
	
	/**
	 * Gets the ratio num deno association required.
	 *
	 * @return the ratioNumDenoAssociationRequired
	 */
	public String getRatioNumDenoAssociationRequired() {
		return RATIO_NUM_DENO_ASSOCIATION_REQUIRED;
	}
	
	/**
	 * Gets the ratio measure observation association required.
	 *
	 * @return the ratioMeasureObsAssociationRequired
	 */
	public String getRatioMeasureObsAssociationRequired() {
		return RATIO_MEASURE_OBS_ASSOCIATION_REQUIRED;
	}
	
	/**
	 * Gets the measure logic is incomplete.
	 *
	 * @return the mEASURE_LOGIC_IS_INCOMPLETE
	 */
	public String getMEASURE_LOGIC_IS_INCOMPLETE() {
		return MEASURE_LOGIC_IS_INCOMPLETE;
	}
	
	/**
	 * Gets the lhs rhs required.
	 *
	 * @return the lHS_RHS_REQUIRED
	 */
	public String getLHS_RHS_REQUIRED() {
		return LHS_RHS_REQUIRED;
	}
	
	/**
	 * Gets the atleaste one child required.
	 *
	 * @return the aTLEASTE_ONE_CHILD_REQUIRED
	 */
	public String getATLEAST_ONE_CHILD_REQUIRED() {
		return ATLEAST_ONE_CHILD_REQUIRED;
	}
	
	/**
	 * Gets the at least two children required.
	 *
	 * @return the at LEAST_TWO_CHILDREN_REQUIRED
	 */
	public String getAT_LEAST_TWO_CHILDREN_REQUIRED() {
		return AT_LEAST_TWO_CHILDREN_REQUIRED;
	}
	
	
	/**
	 * Gets the at least three children required.
	 *
	 * @return the at LEAST_THREE_CHILDREN_REQUIRED
	 */
	public String getAT_LEAST_THREE_CHILDREN_REQUIRED() {
		return AT_LEAST_THREE_CHILDREN_REQUIRED;
	}
	
	
	/**
	 * Gets the population work space measure observation validation error.
	 *
	 * @return the pOPULATION_WORK_SPACE_MEASURE_OBSERVATION_VALIDATION_ERROR
	 */
	public String getPOPULATION_WORK_SPACE_MEASURE_OBSERVATION_VALIDATION_ERROR() {
		return POPULATION_WORK_SPACE_MEASURE_OBSERVATION_VALIDATION_ERROR;
	}
	
	
	/**
	 * Gets the invalid character validation error.
	 *
	 * @return the invalid character validation error
	 */
	public String getINVALID_CHARACTER_VALIDATION_ERROR() {
		return INVALID_CHARACTER_VALIDATION_ERROR;
	}
	
	/**
	 * Gets the measure developer added successfully.
	 *
	 * @return the measureDeveloperAddedSuccessfully
	 */
	public static String getMeasureDeveloperAddedSuccessfully() {
		return MEASURE_DEVELOPER_ADDED_SUCCESSFULLY;
	}
	
	/**
	 * Gets the measure logic is incorrect.
	 *
	 * @return the mEASURE_LOGIC_IS_INCORRECT
	 */
	public String getMEASURE_LOGIC_IS_INCORRECT() {
		return MEASURE_LOGIC_IS_INCORRECT;
	}
	
	/**
	 * Gets the stratification validation for grouping.
	 *
	 * @return the stratification validation for grouping
	 */
	public String getSTRATIFICATION_VALIDATION_FOR_GROUPING() {
		return STRATIFICATION_VALIDATION_FOR_GROUPING;
	}
	
	/**
	 * Gets the measure observation validation for grouping.
	 *
	 * @return the measure observation validation for grouping
	 */
	public String getMEASURE_OBS_VALIDATION_FOR_GROUPING() {
		return MEASURE_OBS_VALIDATION_FOR_GROUPING;
	}
	
	/**
	 * Gets the organization success message.
	 *
	 * @return the oRGANIZATION_SUCCESS_MESSAGE
	 */
	public String getORGANIZATION_SUCCESS_MESSAGE() {
		return ORGANIZATION_SUCCESS_MESSAGE;
	}
	
	/**
	 * Gets the user success message.
	 *
	 * @return the uSER_SUCCESS_MESSAGE
	 */
	public String getUSER_SUCCESS_MESSAGE() {
		return USER_SUCCESS_MESSAGE;
	}
	
	/**
	 * Gets the email id required.
	 *
	 * @return the emailIdRequired
	 */
	public String getEmailIdRequired() {
		return EMAIL_ID_REQUIRED;
	}
	
	/**
	 * Gets the population workspace datetimediff error message.
	 *
	 * @return the population workspace datetimediff error message
	 */
	public String getPOPULATION_WORKSPACE_DATETIMEDIFF_ERROR_MESSAGE() {
		return POPULATION_WORKSPACE_DATETIMEDIFF_ERROR_MESSAGE;
	}
	
	/**
	 * Gets the invalid logic population work space.
	 *
	 * @return the iNVALID_LOGIC_POPULATION_WORK_SPACE
	 */
	public String getINVALID_LOGIC_CQL_WORK_SPACE() {
		return INVALID_LOGIC_CQL_WORK_SPACE;
	}
	
	/**
	 * Gets the invalidlogic clause work space.
	 *
	 * @return the iNVALIDLOGIC_CLAUSE_WORK_SPACE
	 */
	public String getINVALIDLOGIC_CLAUSE_WORK_SPACE() {
		return INVALIDLOGIC_CLAUSE_WORK_SPACE;
	}
	
	/**
	 * Gets the clause work space invalid nested clause.
	 *
	 * @return the cLAUSE_WORK_SPACE_INVALID_NESTED_CLAUSE
	 */
	public String getCLAUSE_WORK_SPACE_INVALID_NESTED_CLAUSE() {
		return CLAUSE_WORK_SPACE_INVALID_NESTED_CLAUSE;
	}
	
	/**
	 * Gets the clause work space invalid nested depth clause.
	 *
	 * @return the cLAUSE_WORK_SPACE_INVALID_NESTED_DEPTH_CLAUSE
	 */
	public String getCLAUSE_WORK_SPACE_INVALID_NESTED_DEPTH_CLAUSE() {
		return CLAUSE_WORK_SPACE_INVALID_NESTED_DEPTH_CLAUSE;
	}
	
	/**
	 * Gets the welcome message.
	 *
	 * @return the welcome message
	 */
	public static String getWelcomeMessage() {
		return WELCOME_MESSAGE;
	}
	
	
	/**
	 * Gets the vsac expansion profile selection.
	 *
	 * @return the vsac expansion profile selection
	 */
	public String getVsacExpansionProfileSelection() {
		return VSAC_EXPANSION_PROFILE_SELECTION;
	}
	
	/**
	 * Gets the vsac profile applied to qdm elements.
	 *
	 *
	 * @return the vsac profile applied to qdm elements
	 */
	public String getVsacProfileAppliedToQdmElements() {
		return VSAC_PROFILE_APPLIED_TO_QDM_ELEMENTS;
	}
	
	/**
	 * Gets the vsac profile applied to qdm elements.
	 *
	 *
	 * @return the default expansion message
	 */
	public String getDefaultExpansionIdRemovedMessage() {
		return DEFAULT_EXPANSION_PROFILE_REMOVED;
	}
	
	/**
	 * Gets the vsac retrieval success.
	 *
	 * @return the vsac retrieval success
	 */
	public String getVSAC_RETRIEVAL_SUCCESS() {
		return SUCCESSFUL_OID_RETREIVAL_FROM_VSAC;
	}
	
	/**
	 * Gets the warning pasting in applied qdm elements.
	 *
	 * @return the warning pasting in applied qdm elements
	 */
	public String getWARNING_PASTING_IN_APPLIED_QDM_ELEMENTS() {
		return WARNING_PASTING_IN_APPLIED_QDM_ELEMENTS;
	}
	
	/**
	 * Gets the error saving in  qdm elements.
	 *
	 * @return the error saving in qdm elements
	 */
	public String getERROR_IN_SAVING_QDM_ELEMENTS() {
		return ERROR_IN_SAVING_QDM_ELEMENTS;
	}
	
	/**
	 * Gets the successfully pasted qdm elements in measure.
	 *
	 * @return the successfully pasted qdm elements in measure
	 */
	public String getSUCCESSFULLY_PASTED_QDM_ELEMENTS_IN_MEASURE() {
		return SUCCESSFULLY_PASTED_QDM_ELEMENTS_IN_MEASURE;
	}
	
	/**
	 * Gets the copy qdm select atleast one.
	 *
	 * @return the copy qdm select atleast one
	 */
	public String getCOPY_QDM_SELECT_ATLEAST_ONE() {
		return COPY_QDM_SELECT_ATLEAST_ONE;
	}
	
	
	/**
	 * Gets the successful qdm remove msg.
	 *
	 * @return the successful qdm remove msg
	 */
	public String getSUCCESSFUL_QDM_REMOVE_MSG() {
		return SUCCESSFUL_QDM_REMOVE_MSG;
	}
	
	/**
	 * Gets the warning measure package creation generic.
	 *
	 * @return the warning measure package creation generic
	 */
	public String getWARNING_MEASURE_PACKAGE_CREATION_GENERIC() {
		return WARNING_MEASURE_PACKAGE_CREATION_GENERIC;
	}
	
	/**
	 * Gets the warning measure package creation risk adjustment.
	 *
	 * @return the warning measure package creation risk adjustment
	 */
	public String getWARNING_MEASURE_PACKAGE_CREATION_RISK_ADJUSTMENT(){
		return WARNING_MEASURE_PACKAGE_CREATION_RISK_ADJUSTMENT;
	}
	
	/**
	 * Gets the warning measure package creation strata.
	 *
	 * @return the warning measure package creation strata
	 */
	public String getWARNING_MEASURE_PACKAGE_CREATION_STRATA() {
		return WARNING_MEASURE_PACKAGE_CREATION_STRATA;
	}
	
	/**
	 * Gets the clause work space invalid attribute in sat all any.
	 *
	 * @return the clause work space invalid attribute in sat all any
	 */
	public String getCLAUSE_WORK_SPACE_INVALID_ATTRIBUTE_IN_SAT_ALL_ANY() {
		return CLAUSE_WORK_SPACE_INVALID_ATTRIBUTE_IN_SAT_ALL_ANY;
	}
	
	/**
	 * Gets the clause work space invalid logical operator.
	 *
	 * @return the clause work space invalid logical operator
	 */
	public String getCLAUSE_WORK_SPACE_INVALID_LOGICAL_OPERATOR() {
		return CLAUSE_WORK_SPACE_INVALID_LOGICAL_OPERATOR;
	}
	
	/**
	 * Gets the clause empty.
	 *
	 * @return the cLAUSE_EMPTY
	 */
	public String getCLAUSE_EMPTY() {
		return CLAUSE_EMPTY;
	}
	
	/**
	 * Gets the successfully modified all oids.
	 *
	 * @return the successfully modified all oids
	 */
	public String getSUCCESSFULLY_MODIFIED_ALL_OIDS() {
		return SUCCESSFULLY_MODIFIED_ALL_OIDS;
	}
	
	/**
	 * Gets the comparison diloag box unit error display.
	 *
	 * @return the comparison diloag box unit error display
	 */
	public String getCOMPARISON_DILOAG_BOX_UNIT_ERROR_DISPLAY() {
		return COMPARISON_DILOAG_BOX_UNIT_ERROR_DISPLAY;
	}
	
	/**
	 * Gets the successful saved cql gen info.
	 *
	 * @return the successful saved cql gen info
	 */
	public String getSUCCESSFUL_SAVED_CQL_GEN_INFO() {
		return SUCCESSFUL_SAVED_CQL_GEN_INFO;
	}
	
	/**
	 * Gets the successful saved cql definition.
	 *
	 * @return the successful saved cql definition
	 */
	public String getSUCCESSFUL_SAVED_CQL_DEFINITION() {
		return SUCCESSFUL_SAVED_CQL_DEFINITION;
	}
	
	/**
	 * Gets the successful saved cql parameter.
	 *
	 * @return the successful saved cql parameter
	 */
	public String getSUCCESSFUL_SAVED_CQL_PARAMETER() {
		return SUCCESSFUL_SAVED_CQL_PARAMETER;
	}
	
	/**
	 * Gets the error save cql definition.
	 *
	 * @return the error save cql definition
	 */
	public String getERROR_SAVE_CQL_DEFINITION() {
		return ERROR_SAVE_CQL_DEFINITION;
	}
	
	/**
	 * Gets the error save cql parameter.
	 *
	 * @return the error save cql parameter
	 */
	public String getERROR_SAVE_CQL_PARAMETER() {
		return ERROR_SAVE_CQL_PARAMETER;
	}
	
	/**
	 * Gets the error duplicate identifier name.
	 *
	 * @return the error duplicate identifier name
	 */
	public String getERROR_DUPLICATE_IDENTIFIER_NAME() {
		return ERROR_DUPLICATE_IDENTIFIER_NAME;
	}
	
	/**
	 * Gets the sucess definition modify.
	 *
	 * @return the sucess definition modify
	 */
	public String getSUCESS_DEFINITION_MODIFY() {
		return SUCESS_DEFINITION_MODIFY;
	}
	
	/**
	 * Gets the sucess parameter modify.
	 *
	 * @return the sucess parameter modify
	 */
	public String getSUCESS_PARAMETER_MODIFY() {
		return SUCESS_PARAMETER_MODIFY;
	}
	
	/**
	 * Gets the successful saved cql functions.
	 *
	 * @return the successful saved cql functions
	 */
	public String getSUCCESSFUL_SAVED_CQL_FUNCTIONS() {
		return SUCCESSFUL_SAVED_CQL_FUNCTIONS;
	}
	
	/**
	 * Gets the error save cql function.
	 *
	 * @return the error save cql function
	 */
	public String getERROR_SAVE_CQL_FUNCTION() {
		return ERROR_SAVE_CQL_FUNCTION;
	}
	
	/**
	 * Gets the sucess function modify.
	 *
	 * @return the sucess function modify
	 */
	public String getSUCESS_FUNCTION_MODIFY() {
		return SUCESS_FUNCTION_MODIFY;
	}
	
	/**
	 * Gets the error parameter name no special char.
	 *
	 * @return the error parameter name no special char
	 */
	public String getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR() {
		return ERROR_PARAMETER_NAME_NO_SPECIAL_CHAR;
	}
	
	/**
	 * Gets the error definition name no special char.
	 *
	 * @return the error definition name no special char
	 */
	public String getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR() {
		return ERROR_DEFINITION_NAME_NO_SPECIAL_CHAR;
	}
	
	/**
	 * Gets the error function name no special char.
	 *
	 * @return the error function name no special char
	 */
	public String getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR() {
		return ERROR_FUNCTION_NAME_NO_SPECIAL_CHAR;
	}
	
	/**
	 * Gets the successful saved cql definition with errors.
	 *
	 * @return the successful saved cql definition with errors
	 */
	public String getSUCCESSFUL_SAVED_CQL_DEFINITION_WITH_ERRORS() {
		return SUCCESSFUL_SAVED_CQL_DEFINITION_WITH_ERRORS;
	}

	/**
	 * Gets the successful saved cql functions with errors.
	 *
	 * @return the successful saved cql functions with errors
	 */
	public String getSUCCESSFUL_SAVED_CQL_FUNCTIONS_WITH_ERRORS() {
		return SUCCESSFUL_SAVED_CQL_FUNCTIONS_WITH_ERRORS;
	}

	/**
	 * Gets the successful saved cql parameter with errors.
	 *
	 * @return the successful saved cql parameter with errors
	 */
	public String getSUCCESSFUL_SAVED_CQL_PARAMETER_WITH_ERRORS() {
		return SUCCESSFUL_SAVED_CQL_PARAMETER_WITH_ERRORS;
	}
	
	/**
	 * Gets the sucess definition modify with errors.
	 *
	 * @return the sucess definition modify with errors
	 */
	public String getSUCESS_DEFINITION_MODIFY_WITH_ERRORS() {
		return SUCESS_DEFINITION_MODIFY_WITH_ERRORS;
	}

	/**
	 * Gets the sucess parameter modify with errors.
	 *
	 * @return the sucess parameter modify with errors
	 */
	public String getSUCESS_PARAMETER_MODIFY_WITH_ERRORS() {
		return SUCESS_PARAMETER_MODIFY_WITH_ERRORS;
	}

	/**
	 * Gets the sucess function modify with errors.
	 *
	 * @return the sucess function modify with errors
	 */
	public String getSUCESS_FUNCTION_MODIFY_WITH_ERRORS() {
		return SUCESS_FUNCTION_MODIFY_WITH_ERRORS;
	}

	/**
	 * Gets the invalid logic measure packager.
	 *
	 * @return the invalid logic measure packager
	 */
	public String getINVALID_LOGIC_MEASURE_PACKAGER() {
		return INVALID_LOGIC_MEASURE_PACKAGER;
	}
	
	/**
	 * Gets the delete warning message.
	 *
	 * @return the delete warning message
	 */
	public String getDELETE_WARNING_MESSAGE() {
		return DELETE_WARNING_MESSAGE; 
	}

	/**
	 * Gets the view cql error message.
	 *
	 * @return the view cql error message
	 */
	public String getVIEW_CQL_ERROR_MESSAGE() {
		return VIEW_CQL_ERROR_MESSAGE;
	}

	/**
	 * Gets the view cql no errors message.
	 *
	 * @return the view cql no errors message
	 */
	public String getVIEW_CQL_NO_ERRORS_MESSAGE() {
		return VIEW_CQL_NO_ERRORS_MESSAGE;
	}

	/**
	 * Gets the delete confirmation parameter.
	 *
	 * @return the delete confirmation parameter
	 */
	public String getDELETE_CONFIRMATION_PARAMETER() {
		return DELETE_CONFIRMATION_PARAMETER;
	}

	/**
	 * Gets the delete confirmation definition.
	 *
	 * @return the delete confirmation definition
	 */
	public String getDELETE_CONFIRMATION_DEFINITION() {
		return DELETE_CONFIRMATION_DEFINITION;
	}

	/**
	 * Gets the delete confirmation function.
	 *
	 * @return the delete confirmation function
	 */
	public String getDELETE_CONFIRMATION_FUNCTION() {
		return DELETE_CONFIRMATION_FUNCTION;
	}
	
	/**
	 * Gets the delete confirmation include.
	 *
	 * @return the delete confirmation include
	 */
	public String getDELETE_CONFIRMATION_INCLUDE() {
		return DELETE_CONFIRMATION_INCLUDE;
	}

	/**
	 * Gets the valueset success message.
	 *
	 * @param codeListName the code list name
	 * @return the valueset success message
	 */
	public String getValuesetSuccessMessage(String codeListName) {
		return "Value set " + codeListName + " has been applied successfully.";
	}

	/**
	 * Gets the error include alias name no special char.
	 *
	 * @return the error include alias name no special char
	 */
	public String getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR() {
		return ERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR;
	}

	/**
	 * Gets the successful saved cql include library.
	 *
	 * @return the successful saved cql include library
	 */
	public String getSUCCESSFUL_SAVED_CQL_INCLUDE_LIBRARY() {
		return SUCCESSFUL_SAVED_CQL_INCLUDE_LIBRARY;
	}

	/**
	 * Gets the include library success message.
	 *
	 * @param aliasName the alias name
	 * @return the include library success message
	 */
	public String getIncludeLibrarySuccessMessage(String aliasName) {
		return " Library " + aliasName + " successfully included.";
	}

	/**
	 * Gets the save include library valiation error.
	 *
	 * @return the save include library valiation error
	 */
	public String getSAVE_INCLUDE_LIBRARY_VALIATION_ERROR() {
		return SAVE_INCLUDE_LIBRARY_VALIATION_ERROR;
	}

	/**
	 * Gets the no includes.
	 *
	 * @return the no includes
	 */
	public  String getNoIncludes() {
		return NO_INCLUDES;
	}

	/**
	 * Gets the library name required.
	 *
	 * @return the library name required
	 */
	public String getLibraryNameRequired() {
		return LIBRARY_NAME_REQUIRED;
	}

	/**
	 * Gets the cql stand alone library name error.
	 *
	 * @return the cql stand alone library name error
	 */
	public String getCqlStandAloneLibraryNameError() {
		return CQL_STAND_ALONE_LIBRARY_NAME_ERROR;
	}

	/**
	 * Gets the no librarues.
	 *
	 * @return the no librarues
	 */
	public String getNoLibrarues() {
		return NO_LIBRARUES;
	}

	/**
	 * Gets the error library version.
	 *
	 * @return the error library version
	 */
	public String getERROR_LIBRARY_VERSION() {
		return ERROR_LIBRARY_VERSION;
	}

	/**
	 * Gets the modify cql library name.
	 *
	 * @return the modify cql library name
	 */
	public String getMODIFY_CQL_LIBRARY_NAME() {
		return MODIFY_CQL_LIBRARY_NAME;
	}

	/**
	 * Gets the error library draft.
	 *
	 * @return the error library draft
	 */
	public String getERROR_LIBRARY_DRAFT() {
		return ERROR_LIBRARY_DRAFT;
	}

	/**
	 * Gets the cql limit warning message.
	 *
	 * @return the cql limit warning message
	 */
	public String getCqlLimitWarningMessage() {
		return CQL_LIBRARY_LIMIT_WARNING;
	}

	/**
	 * Gets the no users returned.
	 *
	 * @return the no users returned
	 */
	public static String getNoUsersReturned() {
		return NO_USERS_RETURNED;
	}

	/**
	 * Gets the no version created.
	 *
	 * @return the no version created
	 */
	public String getNoVersionCreated() {
		return NO_VERSION_CREATED;
	}

	/**
	 * Gets the warning bad data type combination.
	 *
	 * @return the warning bad data type combination
	 */
	public String getWarningBadDataTypeCombination() {
		return WARNING_BAD_DATA_TYPE_COMBINATION;
	}

	/**
	 * Gets the view cql error message bad valueset datatype.
	 *
	 * @return the view cql error message bad valueset datatype
	 */
	public String getVIEW_CQL_ERROR_MESSAGE_BAD_VALUESET_DATATYPE() {
		return VIEW_CQL_ERROR_MESSAGE_BAD_VALUESET_DATATYPE;
	}

	/**
	 * Gets the error select attribute name.
	 *
	 * @return the error select attribute name
	 */
	public String getERROR_SELECT_ATTRIBUTE_NAME() {
		return ERROR_SELECT_ATTRIBUTE_NAME;
	}

	/**
	 * Gets the error select item name.
	 *
	 * @return the error select item name
	 */
	public String getERROR_SELECT_ITEM_NAME() {
		return ERROR_SELECT_ITEM_NAME;
	}

	/**
	 * Gets the error select item type.
	 *
	 * @return the error select item type
	 */
	public String getERROR_SELECT_ITEM_TYPE() {
		return ERROR_SELECT_ITEM_TYPE;
	}

	/**
	 * Gets the error select item name or data type.
	 *
	 * @return the error select item name or data type
	 */
	public String getERROR_SELECT_ITEM_NAME_OR_DATA_TYPE() {
		return ERROR_SELECT_ITEM_NAME_OR_DATA_TYPE;
	}

	/**
	 * Gets the error invalid code data type.
	 *
	 * @return the error invalid code data type
	 */
	public String getERROR_INVALID_CODE_DATA_TYPE() {
		return ERROR_INVALID_CODE_DATA_TYPE;
	}

	/**
	 * Gets the error invalid quantity.
	 *
	 * @return the error invalid quantity
	 */
	public String getERROR_INVALID_QUANTITY() {
		return ERROR_INVALID_QUANTITY;
	}

	/**
	 * Gets the error invalid date time quantity.
	 *
	 * @return the error invalid date time quantity
	 */
	public String getERROR_INVALID_DATE_TIME_QUANTITY() {
		return ERROR_INVALID_DATE_TIME_QUANTITY;
	}

	/**
	 * Gets the error invalid mode details.
	 *
	 * @return the error invalid mode details
	 */
	public String getERROR_INVALID_MODE_DETAILS() {
		return ERROR_INVALID_MODE_DETAILS;
	}

	/**
	 * Gets the error select attribute to insert.
	 *
	 * @return the error select attribute to insert
	 */
	public String getERROR_SELECT_ATTRIBUTE_TO_INSERT() {
		return ERROR_SELECT_ATTRIBUTE_TO_INSERT;
	}

	/**
	 * Gets the error enter date time and quantity.
	 *
	 * @return the error enter date time and quantity
	 */
	public String getERROR_ENTER_DATE_TIME_AND_QUANTITY() {
		return ERROR_ENTER_DATE_TIME_AND_QUANTITY;
	}

	/**
	 * Gets the error invalid date time.
	 *
	 * @return the error invalid date time
	 */
	public String getERROR_INVALID_DATE_TIME() {
		return ERROR_INVALID_DATE_TIME;
	}

	/**
	 * Gets the cql function argument name error.
	 *
	 * @return the cql function argument name error
	 */
	public String getCqlFunctionArgumentNameError() {
		return CQL_FUNCTION_ARGUMENT_NAME_ERROR;
	}

	/**
	 * Gets the transfer checkbox error cql library.
	 *
	 * @return the transfer checkbox error cql library
	 */
	public String getTRANSFER_CHECKBOX_ERROR_CQL_LIBRARY() {
		return TRANSFER_CHECKBOX_ERROR_CQL_LIBRARY;
	}
	
	/**
	 * Gets the code success message.
	 *
	 * @param codeName the code name
	 * @return the code success message
	 */
	public String getCodeSuccessMessage(String codeName) {
		return "Code "+ codeName + " has been applied successfully.";
	}

	/**
	 * Gets the umls code identifier required.
	 *
	 * @return the umls code identifier required
	 */
	public String getUMLS_CODE_IDENTIFIER_REQUIRED() {
		return UMLS_CODE_IDENTIFIER_REQUIRED;
	}

	/**
	 * Gets the umls invalid code identifier.
	 *
	 * @return the umls invalid code identifier
	 */
	public String getUMLS_INVALID_CODE_IDENTIFIER() {
		return UMLS_INVALID_CODE_IDENTIFIER;
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

	/**
	 * @return the pATIENT_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE
	 */
	public String getPATIENT_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE() {
		return PATIENT_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE;
	}

	/**
	 * @return the ePISODE_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE
	 */
	public String getEPISODE_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE() {
		return EPISODE_BASED_DEFINITIONS_SAVE_GROUPING_VALIDATION_MESSAGE;
	}

	/**
	 * @return the ePISODE_BASED_RATIO_MEASURE_SAVE_GROUPING_VALIDATION_MESSAGE
	 */
	public String getEPISODE_BASED_RATIO_MEASURE_SAVE_GROUPING_VALIDATION_MESSAGE() {
		return EPISODE_BASED_RATIO_MEASURE_SAVE_GROUPING_VALIDATION_MESSAGE;
	}

	/**
	 * @return the mEASURE_OBSERVATION_USER_DEFINED_FUNC_VALIDATION_MESSAGE
	 */
	public String getMEASURE_OBSERVATION_USER_DEFINED_FUNC_VALIDATION_MESSAGE() {
		return MEASURE_OBSERVATION_USER_DEFINED_FUNC_VALIDATION_MESSAGE;
	}

	/**
	 * @return the mEASURE_OBSERVATION_RETURN_SAME_TYPE_VALIDATION_MESSAGE
	 */
	public String getMEASURE_OBSERVATION_RETURN_SAME_TYPE_VALIDATION_MESSAGE() {
		return MEASURE_OBSERVATION_RETURN_SAME_TYPE_VALIDATION_MESSAGE;
	}

	/**
	 * @return the mEASURE_OBSERVATION_USER_DEFINED_FUNC_REURN_TYPE_VALIDATION_MESSAGE
	 */
	public String getMEASURE_OBSERVATION_USER_DEFINED_FUNC_REURN_TYPE_VALIDATION_MESSAGE() {
		return MEASURE_OBSERVATION_USER_DEFINED_FUNC_REURN_TYPE_VALIDATION_MESSAGE;
	}

}
