package mat.client.shared;

import mat.shared.ConstantMessages;

/**
 * Message store to prevent duplicated messages
 * final String fields and their getters
 * @author aschmidt
 *
 */
public class MessageDelegate {
	
	public String getMeasureSetRequiredMessage(){
		return "Measure Set is required.";
	}
	
	public String getSuplementalRequiredMessage(){
		return "Supplemental Data Elements are required.";
	}
	
	public String getMeasureObservationRequiredMessage(){
		return "Measure Observation is required.";
	}
	
	public String getMeasurePopulationRequiredMessage(){
		return "Measure Population is required.";
	}
	
	public String getDenominatorExceptionsRequiredMessage(){
		return "Denominator Exceptions is required.";
	}
	
	public String getNumeratorExclusionsRequiredMessage(){
		return"Numerator Exclusions is required.";
	}
	public String getNumeratorRequiredMessage(){
		return "Numerator is required.";
	}
	
	public String getDenominatorExclusionsRequiredMessage(){
		return "Denominator Exclusions is required.";
	}
	
	
	public String getDenominatorRequiredMessage(){
		return "Denominator is required.";
	}
	
	public String getInitialPatientPopRequiredMessage(){
		return "Initial Patient Population is required.";
	}
	
	public String getGuidenceRequiredMessage(){
		return "Guidance is required.";
	}
	public String getDefinitionRequiredMessage(){
		return "Definition is required.";
	}
	
	public String getReferencesRequiredMessage(){
		return "Reference(s) is required.";
	}
	
	public String getImporvementNotationRequiredMessage(){
		return "Improvement Notation is required.";
	}
	
	public String getClinicalRecomendRequiredMessage(){
		return "Clinical Recommendation Statement is required." ;
	}
	
	public String getRateAggregationRequiredMessage(){
		return "Rate Aggregation is required.";
	}
	
	public String getDisclaimerRequiredMessage(){
		return "Disclaimer is required.";
	}
	
	public String getEndorsedByRequiredMessage(){
		return "Endorsed By is required.";
	}
	
	public String getNQFNumberRequiredMessage(){
		return "NQF Number is required.";
	}
	
	
	public static String getMeasureSaveServerErrorMessage(int code){
		String pre = "Error saving: ";
		String s ="SPECIAL CODE "+code+".";

		if(code == ConstantMessages.ID_NOT_UNIQUE)
			s = "ID not unique.";
		if(code ==ConstantMessages.REACHED_MAXIMUM_VERSION)
			s= "maximum version reached.";
		if(code ==ConstantMessages.REACHED_MAXIMUM_MAJOR_VERSION)
			s = "maximum major version reached.";
		if(code ==ConstantMessages.REACHED_MAXIMUM_MINOR_VERSION)
			s = "maximum minor version reached.";
		if(code ==ConstantMessages.INVALID_VALUE_SET_DATE)
			s = "invalid value set date.";
		return pre + s;
	}

	public String getMeasurePhraseAlreadyExistsMessage(String name ){
		return name + " already exists.";
	}
	
	private final String PACKAGE_SUCCESS = "Measure packaged successfully. Please access the Measure Library to export the measure.";
	public String getPackageSuccessMessage(){
		return PACKAGE_SUCCESS;
	}
	
	private final String UNABLE_TO_PROCESS = "System error.  Unable to process information.";
	public String getUnableToProcessMessage(){
		return UNABLE_TO_PROCESS;
	}
	
	/*
	 * RATIO
	 */
	private final String RATIO_WRONG_NUM ="For a Ratio measure, a grouping must contain exactly one of each of the following: "
		+"Population, Denominator, and Numerator.";
	public String getRatioWrongNumMessage(){
		return RATIO_WRONG_NUM;
	}
	private final String RATIO_MAY_NOT_CONTAIN = "For a Ratio measure, a grouping may not contain a Denominator Exception, Measure Observation, or Measure Population.";
	public String getRatioMayNotContainMessage(){
		return RATIO_MAY_NOT_CONTAIN;
	}
	private final String RATIO_TOO_MANY = "For a Ratio measure, a grouping may not contain more than one of each of the following: Denominator Exclusion and Numerator Exclusion.";
	public String getRatioTooManyMessage(){
		return RATIO_TOO_MANY;
	}
	
	/*
	 * PROPORTION
	 */
	private final String PROPORTION_WRONG_NUM ="For a Proportion measure, a grouping must contain exactly one of each of the following: "
		+"Population and Denominator.";
	public String getProportionWrongNumMessage(){
		return PROPORTION_WRONG_NUM;
	}
	private final String PROPORTION_MAY_NOT_CONTAIN = "For a Proportion measure, a grouping may not contain a Numerator Exclusion, Measure Population, or Measure Observation.";
	public String getProportionMayNotContainMessage(){
		return PROPORTION_MAY_NOT_CONTAIN;
	}
	private final String PROPORTION_TOO_FEW = "For a Proportion measure, a grouping may not contain less than one Numerator.";
	public String getProportionTooFewMessage(){
		return PROPORTION_TOO_FEW;
	}
	private final String PROPORTION_TOO_MANY = "For a Proportion measure, a grouping may not contain more than one of each of the following: " +
			"Denominator Exclusions and Denominator Exceptions.";
	public String getProportionTooManyMessage(){
		return PROPORTION_TOO_MANY;
	}
	
	/*
	 * CONTINUOUS VARIABLE
	 */
	private final String CONTINUOUS_VARIABLE_WRONG_NUM ="For a Continuous Variable measure, a grouping must contain exactly one of each of the following: "
			+"Population, Measure Population, and Measure Observation.";
	public String getContinuousVariableWrongNumMessage(){
		return CONTINUOUS_VARIABLE_WRONG_NUM;
	}
	private final String CONTINUOUS_VARIABLE_MAY_NOT_CONTAIN = "For a Continuous Variable measure, a grouping may not contain any Numerator, Numerator Exclusions, Denominator, Denominator Exclusions, or Denominator Exceptions.";
	public String getContinuousVariableMayNotContainMessage(){
		return CONTINUOUS_VARIABLE_MAY_NOT_CONTAIN;
	}

	private final String PASSWORD_REQUIRED ="Password is required.";
	public String getPasswordRequiredMessage(){
		return PASSWORD_REQUIRED;
	}
	private final String SERVER_CALL_NULL = "Server call for login returned null.";
	public String getServerCallNullMessage(){
		return SERVER_CALL_NULL;
	}

	private final String PLEASE_SELECT_ITEM = "Please select an item from the list.";
	public String getPleaseSelectItemMessage(){
		return PLEASE_SELECT_ITEM;
	}

	public String getGroupedCodeListExistsMessage(String str){
		return "Grouped Value Set " + str + " already exists.";
	}

	private final String CODE_LIST_REQUIRED = "Value Set is required.";
	public String getCodeListRequiredMessage(){
		return CODE_LIST_REQUIRED;
	}

	public String getCodeListAlreadyExistsForGroupedMessage(String str){
		return "Value Set already exists for the " + str + " grouped value set.";
	}
	public String getCodeAlreadyExistsForMessage(String str){
		return "Code already exists for " + str + " value set.";
	}

	private final String ATTRIBUTES_NOT_ALLOWED = "This phrase cannot have attributes.";
	public String getAttributesNotAllowedMessage(){
		return ATTRIBUTES_NOT_ALLOWED;
	}

	public String getIdentityMessage(String msg){
		return msg;
	}

	private final String QUANTITY_NUM = "Quantity must be a number.";
	public String getQuantityNumMessage(){
		return QUANTITY_NUM;
	}
	
	private final String QUANTITY_INT = "Quantity must be an integer.";
	public String getQuantityIntMessage(){
		return QUANTITY_INT;
	}

	private final String NO_CONDITION = "If there is no Condition, there cannot be a second Phrase Element.";
	public String getNoConditionMessage(){
		return NO_CONDITION;
	}

	private final String NO_PHRASE_ELEMENTS = "No Phrase Elements have been created for this measure. In order to complete a Measure Phrase, at least one Phrase Element must be created and selected.";
	public String getNoPhraseElementsMessage(){
		return NO_PHRASE_ELEMENTS;
	}

	private final String NEED_SECOND = "Please select a second Phrase Element";
	public String getNeedSecondMessage(){
		return NEED_SECOND;
	}

	private final String NO_ELEMENTS = "No Phrase Elements have been created for this measure. In order to complete a Measure Phrase, at least one Phrase Element must be created and selected.";
	public String getNoElementsMessage(){
		return NO_ELEMENTS;
	}

	private final String ENTER_NAME = "Please enter a name for this simple expression";
	public String getEnterNameMessage(){
		return ENTER_NAME;
	}

	private final String LOGIN_FAILED = "Login failed. Please sign in again.";
	public String getLoginFailedMessage(){
		return LOGIN_FAILED;
	}
	
	private final String LOGIN_FAILED_USER_ALREADY_LOGGED_IN = "Unable to login. Another session has been established with this account. Please verify you do not already have an open session.";
	public String getLoginFailedAlreadyLoggedInMessage(){
		return LOGIN_FAILED_USER_ALREADY_LOGGED_IN;
	}

	//TODO is this such a smart policy?
	//This makes denial of service attacks on accounts too easy.
	//Perhaps make them answer a security question?
	private final String SECCOND_ATTEMPT_FAILED = "Failed 2nd attempt. Next attempt will lock the account.";
	 public String getSecondAttemptFailedMessage(){
		 return SECCOND_ATTEMPT_FAILED;
	 }

	private final String ACCOUNT_REVOKED = "Your account has been revoked. Please contact the Helpdesk.";
    public String getAccountRevokedMessage(){
    	return ACCOUNT_REVOKED;
    }


    private final String ACCOUNT_LOCKED2 = "Your account has been locked. Please contact the Helpdesk.";
	public String getAccountLocked2Message(){
		return ACCOUNT_LOCKED2;
	}

	private final String MULTIPLE_EXCLUSION = "The Package Grouping cannot contain more than one Denominator Exclusion clause.";
	public String getMultipleExclusionMessage(){
		return MULTIPLE_EXCLUSION;
	}

	private final String MULTIPLE_POP_CLAUSE = "The Package Grouping cannot contain more than one Population clause.";
	public String getMultiplePopClauseMessage(){
		return MULTIPLE_POP_CLAUSE;
	}

	private final String NO_POP_CLAUSE = "The Package Grouping does not contain a Population clause.";
	public String getNoPopClauseMessage(){
		return NO_POP_CLAUSE;
	}

	private final String MULTIPLE_DENOM_CLAUSE = "The Package Grouping cannot contain more than one Denominator clause.";
	public String getMultipleDenomClauseMessage(){
		return MULTIPLE_DENOM_CLAUSE;
	}

	private final String NO_DENOMINATOR_CLAUSE = "The Package Grouping does not contain a Denominator clause.";
	public String getNoDenominatorClauseMessage(){
		return NO_DENOMINATOR_CLAUSE;
	}
	private final String NO_NUMERATOR_CLAUSE = "The Package Grouping does not contain a Numerator clause.";
	public String getNoNumeratorClauseMessage(){
		return NO_NUMERATOR_CLAUSE;
	}

	public String getUnknownCodeMessage(int code){
		return "Unknown Code " + code;

	}

	private final String SERVER_SIDE_VALIDATION = "Server Side Validation";
	public String getServerSideValidationMessage(){
		return SERVER_SIDE_VALIDATION;
	}

	private final String OID_IN_USE = "OID is already in use.";
	public String getOIDInUseMessage(){
		return OID_IN_USE;
	}

	public String getCodeListAlreadyExistsMessage(String s){
		return "Value Set " + s + " already exists.";
	}

	public String getAlreadyExistsMessage(String s){
		return s + " already exists.";
	}

	public String getUnknownErrorMessage(int code){
		return "Unknown Code "+ code;
	}

	private final String EMAIL_ALREADY_EXISTS = "E-mail Address already exists.";
	public String getEmailAlreadyExistsMessage(){
		return EMAIL_ALREADY_EXISTS;
	}

	private final String DUPLICATE_CODES_MSG = "All code(s) were identified as duplicates to code(s) already in the value set and were ignored upon import.";
	public String getDuplicateCodesMessage(){
		return DUPLICATE_CODES_MSG;
	}


	private final String INVALID_TEMPLATE = "Import failed. Invalid template.";
	public String getInvalidTemplateMessage(){
		return INVALID_TEMPLATE;
	}

	private final String SYSTEM_ERROR = "Import failed due to system error. Please try again.";
	public String getSystemErrorMessage(){
		return SYSTEM_ERROR;
	}

	private final String EMPTY_FILE_ERROR = "Import failed. File is empty.";
	public String getEmptyFileError(){
		return EMPTY_FILE_ERROR;
	}

	private final String DUPLICATE_ERROR = "Import failed. One or more duplicate codes exist in file. Please remove then try again.";
	public String getDuplicateErrorMessage(){
		return DUPLICATE_ERROR;
	}

	private final String SECURITY_QUESTION_HEADER = "The answer(s) provided to the security question(s) do not match the following rules:";
	public String getSecurityQuestionHeeaderMessage(){
		return SECURITY_QUESTION_HEADER;
	}

	private final String NOT_UNIQUE_QUESTIONS = "You cannot use the same security question more than once.";
	public String getNotUniqueQuestions(){
		return NOT_UNIQUE_QUESTIONS;
	}

	public String getSecurityAnswerTooShortMessage(int index){
		return "Security Answer "+index+" is too short.";
	}

	private final String MUST_NOT_CONTAIN_RUNS = "Passwords must not contain three consecutive identical characters.";
	public String getMustNotContainRunsMessage(){
		return MUST_NOT_CONTAIN_RUNS;
	}

	private final String MUST_NOT_CONTAIN_DICTIONAY_WORD = "Passwords must not consist of a single dictionary word with letters, numbers and symbols.";
	public String getMustNotContainDictionaryWordMessage(){
		return MUST_NOT_CONTAIN_DICTIONAY_WORD;
	}
	private final String MUST_NOT_CONTAIN_LOGIN_ID = "Passwords must not contain your User ID.";
	public String getMustNotContainLoginIdMessage(){
		return MUST_NOT_CONTAIN_LOGIN_ID;
	}

	private final String MUST_CONTAIN_SPECIAL = "Must contain a special character.";
	public String getMustContainSpecialMessage(){
		return MUST_CONTAIN_SPECIAL;
	}

	private final String MUST_CONTAIN_NUMBER = "Must contain a number.";
	public String getMustContainNumberMessage(){
		return MUST_CONTAIN_NUMBER;
	}

	private final String MUST_CONTAIN_LOWER = "Must contain a lowercase letter.";
	public String getMustContainLowerMessage(){
		return MUST_CONTAIN_LOWER;
	}

	private final String MUST_CONTAIN_UPPER = "Must contain an uppercase letter.";
	public String getMustContainUpperMessage(){
		return MUST_CONTAIN_UPPER;
	}

	private final String PASSWORD_WRONG_LENGTH = "Must be between 8 and 16 characters long.";
	public String getPasswordWrongLengthMessage(){
		return PASSWORD_WRONG_LENGTH;
	}

	private final String PASSWORD_MISMATCH = "Your new password and confirm password do not match.";
	public String getPasswordMismatchMessage(){
		return PASSWORD_MISMATCH;
	}

	private final String DOESNT_FOLLOW_RULES = "The new password you entered does not match the following rules:";
	public String getDoesntFollowRulesMessage(){
		return DOESNT_FOLLOW_RULES;
	}

	private final String UNKNOWN_FAIL = "Unknown failure reason";
	public String getUnknownFailMessage(){
		return UNKNOWN_FAIL;
	}

	private final String USER_NOT_FOUND_MSG = "User ID is required.";
	public String getUserNotFoundMessage(){
		return USER_NOT_FOUND_MSG;
	}
	
	private final String EMAIL_NOT_FOUND_MSG = "Email Address is required.";
	public String getEmailNotFoundMessage(){
		return EMAIL_NOT_FOUND_MSG;
	}
	
	private final String ACCOUNT_LOCKED = "Your account has been locked.Please Contact Helpdesk.";
	
	
	public String getAccountLockedMessage(){
		return ACCOUNT_LOCKED;
	}
	
	private final String ACCOUNT_TERMINATION = "Your account has been Terminated.Please Contact Helpdesk for more information.";
	public String getAccountTermination(){
		return ACCOUNT_TERMINATION;
	}
	

	private final String SECURITY_Q_MISMATCH = "The answer for the security question did not match.";
	public String getSecurityQMismatchMessage(){
		return SECURITY_Q_MISMATCH;
	}


	private final String SECURITY_NOT_ANSWERED = "Your security questions have not been answered.  You cannot continue. Please contact the Helpdesk";
	public String getSecurityNotAnsweredMessage(){
		return SECURITY_NOT_ANSWERED;
	}

	private final String EMAIL_MISMATCH = "Email did not match with the User ID.  Please Contact the Administrator.";
	public String getEmailMismatchMessage(){
		return EMAIL_MISMATCH;
	}

	private final String PHONE_10_DIGIT = "Phone Number is required to be 10 digits.";
	public String getPhoneTenDigitMessage(){
		return PHONE_10_DIGIT;
	}

	private final String EXCEL_FILE_TYPE = "Please select a file with an Excel file type (.xls or .xlsx)";
	public String getExcelFileTypeMessage(){
		return EXCEL_FILE_TYPE;
	}
	private final String FILE_NOT_SELECTED = "Please select a file.";
	public String getFileNotSelectedMessage(){
		return FILE_NOT_SELECTED;
	}

	private final String CODE_VERSION_REQUIRED = "Code System Version is required.";
	public String getCodeVersionRequiredMessage(){
		return CODE_VERSION_REQUIRED;
	}

	private final String  CODE_SYSTEM_REQUIRED = "Code System is required.";
	public String getCodeSystemRequiredMessage(){
		return CODE_SYSTEM_REQUIRED;
	}

	private final String STATUS_REQUIRED = "Status is required.";
	public String getStatusRequiredMessage(){
		return STATUS_REQUIRED;
	}

	private final String CATEGORY_REQUIRED = "Category is required.";
	public String getCategoryRequiredMessage(){
		return CATEGORY_REQUIRED;
	}
	private final String STEWARD_REQUIRED = "Steward is required.";
	public String getStewardRequiredMessage(){
		return STEWARD_REQUIRED;
	}

	private final String DESCRIPTOR_REQUIRED = "Descriptor is required.";
	public String getDescriptorRequiredMessage(){
		return DESCRIPTOR_REQUIRED;
	}

	private final String CODE_REQUIRED = "Code is required.";
	public String getCodeRequiredMessage(){
		return CODE_REQUIRED;
	}
	private final String GROUPING_REQUIRED = "A Grouping is required.";
	public String getGroupingRequiredMessage(){
		return GROUPING_REQUIRED;
	}

	private final String RATIONALE_REQUIRED = "Rationale is required.";
	public String getRationaleRequiredMessage(){
		return RATIONALE_REQUIRED;
	}
	private final String STRAT_REQUIRED = "Stratification is required.";
	public String getStratRequiredMessage(){
		return STRAT_REQUIRED;
	}

	private final String DESCRIPTION_REQUIRED= "Description is required.";
	public String getDescriptionRequiredMeassage(){
		return DESCRIPTION_REQUIRED;
	}

	private final String MEASURE_STEWARD_REQUIRED = "Measure Steward is required.";
	public String getMeasureStewardRequiredMessage(){
		return MEASURE_STEWARD_REQUIRED;
	}

	private final String AUTHOR_REQUIRED = "Measure Developer is required.";
	public String getAuthorRequiredMessage(){
		return AUTHOR_REQUIRED;
	}

	private final String MEASURE_TYPE_REQUIRED = "Measure Type is required.";
	public String getMeasureTypeRequiredMessage(){
		return MEASURE_TYPE_REQUIRED;
	}
	private final String MEASURE_SCORE_REQUIRED = "Measure Scoring is required.";
	public String getMeasureScoreRequiredMessage(){
		return MEASURE_SCORE_REQUIRED;
	}

	private final String MEASURE_PERIOD_TO_DATE_INVALID ="Measurement Period To is not a valid date.";
	public String getMeasurePeriodToDateInvalidMessage(){
		return MEASURE_PERIOD_TO_DATE_INVALID;
	}

	private final String MEASURE_PERIOD_TO_REQUIRED ="Measurement Period To is required.";
	public String getMeasurePeriodToRequiredMessage(){
		return MEASURE_PERIOD_TO_REQUIRED;
	}

	private final String MEASURE_PERIOD_FROM_DATE_INVALID ="Measurement Period From is not a valid date.";
	public String getMeasurePeriodFromDateInvalidMessage(){
		return MEASURE_PERIOD_FROM_DATE_INVALID;
	}


	private final String MEASURE_PERIOD_FROM_REQUIRED = "Measurement Period From is required.";
	public String getMeasurePeriodFromRequiredMessage(){
		return MEASURE_PERIOD_FROM_REQUIRED;

	}

	private final String AVAIL_DATE_INVALID = "Available Date is not a valid date.";
	public String getAvailDateInvalidMessage(){
		return AVAIL_DATE_INVALID;
	}


	private final String AVAIL_DATE_REQUIRED = "Available Date is required.";
	public String getAvailDateRequiredMessage(){
		return AVAIL_DATE_REQUIRED;
	}

	private final String MEASURE_ID_REQUIRED ="Measure ID is required.";
	public String getMeasureIDRequiredMessage(){
		return MEASURE_ID_REQUIRED;
	}

	private final String MEASURE_STATUS_REQUIRED ="Measure Status is required.";
	public String getMeasureStatusRequiredMessage(){
		return MEASURE_STATUS_REQUIRED;
	}

	private final String VERSION_REQUIRED = "Version Number is required.";
	public String getVersionRequiredMessage(){
		return VERSION_REQUIRED;
	}

	private final String ABV_MEASURE_NAME_REQUIRED ="Abbreviated Measure Name is required.";
	public String getAbvMeasureNameRequired(){
		return ABV_MEASURE_NAME_REQUIRED;
	}


	private final String MEASURE_NAME_REQUIRED = "Measure Name is required.";
		public String getMeasureNameRequiredMessage(){
		return MEASURE_NAME_REQUIRED;
	}

	private final String PLEASE_SELECT_CLAUSE = "Please select a clause from the clause library in order to clone.";
	public String getPleaseSelectClauseMessage(){
		return PLEASE_SELECT_CLAUSE;
	}

	private final String ABV_NAME_REQUIRED = "Abbreviated Name is required.";
	public String getAbvNameRequiredMessage(){
		return ABV_NAME_REQUIRED;
	}

	private final String GROUPED_NAME_REQUIRED = "Grouped Value Set Name is required.";
	public String getGroupedNameRequiredMessage(){
		return GROUPED_NAME_REQUIRED;
	}
	
	private final String NAME_REQUIRED = "Value Set Name is required.";
	public String getNameRequiredMessage(){
		return NAME_REQUIRED;
	}


	public static final String ONE_AND = "A Clause should start with only one AND.";
	public String getOneAndMessage(){
		return ONE_AND;
	}

	public static final String NO_MEASURES = "No Measures returned. Please search again.";
	public String getNoMeasuresMessage(){
		return NO_MEASURES;
	}

	public static final String NO_CODE_LISTS = "No Value Sets returned. Please search again.";
	public String getNoCodeListsMessage(){
		return NO_CODE_LISTS;
	}

	public static final String ROOT_OID_TOO_LONG = "Root OID cannot exceed 50 characters.";
	public String getRootOIDTooLongMessage(){
		return ROOT_OID_TOO_LONG;
	}

	public static final String OID_TOO_LONG ="OID cannot exceed 50 characters.";
	public String getOIDTooLongMessage(){
		return OID_TOO_LONG;
	}

	public static final String ROOT_OID_REQUIRED = "Root OID is required.";
	public String getRootOIDRequiredMessage(){
		return ROOT_OID_REQUIRED;
	}

	public static final String OID_REQUIRED = "OID is required.";
	public String getOIDRequiredMessage(){
		return OID_REQUIRED;
	}

	public static final String ORG_REQUIRED = "Organization is required.";
	public String getOrgRequiredMessage(){
		return ORG_REQUIRED;
	}

	public static final String PHONE_REQUIRED = "Phone Number is required.";
	public String getPhoneRequiredMessage(){
		return PHONE_REQUIRED;
	}


	public static final String LOGIN_ID_REQUIRED = "User ID is required.";
	public String getLoginIDRequiredMessage(){
		return LOGIN_ID_REQUIRED;
	}
	
	public static final String LOGIN_USER_REQUIRED = "User Name is required.";
	public String getLoginUserRequiredMessage(){
		return LOGIN_USER_REQUIRED;
	}

	public static final String LAST_NAME_REQUIRED = "Last Name is required.";
	public String getLastNameRequiredMessage(){
		return LAST_NAME_REQUIRED;
	}


	public static final String FIRST_NAME_MIN = "First Name must be at least two characters.";
	public String getFirstMinMessage(){
		return FIRST_NAME_MIN;
	}


	public static final String FIRST_NAME_REQUIRED = "First Name is required.";
	public String getFirstNameRequiredMessage(){
		return FIRST_NAME_REQUIRED;
	}


	public static final String ALERT_LOADING_MESSAGE = "Please wait until loading is complete.";
	public String getAlertLoadingMessage(){
		return ALERT_LOADING_MESSAGE;
	}

	public static final String  TEMP_EMAIL_SENT = "Temporary Password E-mail has been sent.";
	public String getTempEmailSentMessage(){
		return TEMP_EMAIL_SENT;
	}

	public static final String CHANGES_SAVED = "Changes are successfully saved.";
	public String getChangesSavedMessage(){
		return CHANGES_SAVED;
	}

	public static final String CODE_LIST_ADDED = "Code has been successfully added to the value set.";
	public String getCodeListAddedMessage(){
		return CODE_LIST_ADDED;
	}


	public static final String CODE_LIST_ADDED_GROUP = "Value Set has been successfully added to the grouped value set.";
	public String getCodeListAddedGroupMessage(){
		return CODE_LIST_ADDED_GROUP;
	}
	public static final String IMPORT_SUCCESS = "Import Successful.";
	public String getImportSuccessMessage(){
		return IMPORT_SUCCESS;
	}

	//public static final String x = "";
	//ASK STAN
	public String getImportSuccessMessage(int codes){
		String msg = "";
		if(codes ==1){
			msg = "Import Successful.  1 code was identified as duplicate to code(s) already in the value set and was ignored upon import.";
		}
		else{
			msg = "Import Successful.  "+codes+" codes were identified as duplicates to code(s) already in the value set and were ignored upon import.";
		}
		return msg;
	}


	public String getQDMSuccessMessage(String codeListName, String dataType){
		return " The QDM Element "+codeListName+": "+dataType+" has been added successfully.";
	}

	public String getQDMOcurrenceSuccessMessage(String codeListName, String dataType,String occurrenceMessage){
		return " The QDM Element "+occurrenceMessage+ " of "+codeListName+": "+dataType+" has been added successfully.";
	}

	public static final String GROUPING_SAVED = "Grouping has been saved.";
	public String getGroupingSavedMessage(){
		return GROUPING_SAVED;
	}
	
	public static final String SUPP_DATA_SAVED = "Supplemental data elements have been saved.";
	public String getSuppDataSavedMessage(){
		return SUPP_DATA_SAVED;
	}


	public static final String PASSWORD_CHANGED = "Your password has been changed.";
	public String getPasswordSavedMessage(){
		return PASSWORD_CHANGED;
	}

	public static final String SEC_QUESTIONS_UPDATED = "Your security questions have been updated.";
	public String getSecurityQuestionsUpdatedMessage(){
		return SEC_QUESTIONS_UPDATED;
	}


	public static final String PERSONAL_INFO_UPDATED = "Your personal information has been updated.";
	public String getPersonalInfoUpdatedMessage(){
		return PERSONAL_INFO_UPDATED;
	}


	//US - 421
	public static final String s_ERR_MEASURE_SCORE_REQUIRED = "Measure Scoring is required.";
	public static final String s_ERR_RETRIEVE_SCORING_CHOICES = "Problem while retrieving measure scoring choices.";
	
	//US 62
	public static final String s_ERR_RETRIEVE_UNITS = "Problem while retrieving units.";
    
	//US 602
	public static final String s_ERR_RETRIEVE_STATUS = "Problem while retrieving status."; 
	
	
	//US 171
	public static final String s_ERR_RETRIEVE_OPERATOR = "Problem while retrieving operator.";
    //US - 502
	
	private final String VALUE_SET_DATE_REQUIRED = "Value Set Package Date is required.";
	public String getValueSetDateRequiredMessage(){
		return VALUE_SET_DATE_REQUIRED;
	}
	
	private final String VALUE_SET_DATE_INVALID = "Value Set Package Date is not a valid date.";
	public String getValueSetDateInvalidMessage(){
		return VALUE_SET_DATE_INVALID;
	}
 
	
	public static final int DRAFT = 0;
	public static final int COMPLETE = 1;
	
	public static final String VALUE_SET_DRAFT_SAVED = "Value Set successfully saved as a draft.";
	public static final String VALUE_SET_COMPLETE_SAVED = "Value Set successfully saved as complete.";
	public String getValueSetChangedSavedMessage(int mode){
		if(mode == DRAFT)
			return VALUE_SET_DRAFT_SAVED;
		else if(mode == COMPLETE)
			return VALUE_SET_COMPLETE_SAVED;
		else 
			return "";
	}
	
	public static final String GROUPED_VALUE_SET_DRAFT_SAVED = "Grouped Value Set successfully saved as a draft.";
	public static final String GROUPED_VALUE_SET_COMPLETE_SAVED = "Grouped Value Set successfully saved as complete.";
	public String getGroupedValueSetChangedSavedMessage(int mode){
		if(mode == DRAFT)
			return GROUPED_VALUE_SET_DRAFT_SAVED;
		else if(mode == COMPLETE)
			return GROUPED_VALUE_SET_COMPLETE_SAVED;
		else 
			return "";
	}

	private final String INVALID_LAST_MODIFIED_DATE = "Invalid Last Modified Date.";
	public String getInvalidLastModifiedDateMessage() {
		return INVALID_LAST_MODIFIED_DATE;
	}

	private final String LAST_MODIFIED_DATE_NOT_UNIQUE = "The Last Modified date and time entered is already is use for this value set.";
	public String getLastModifiedDateNotUniqueMessage() {
		return LAST_MODIFIED_DATE_NOT_UNIQUE;
	}
	
	private final String COPYRIGHT_REQUIRED= "Copyright is required.";
	public String getCopyrightRequiredMeassage(){
		return COPYRIGHT_REQUIRED;
	}
	
	private final String RISK_ADJUSTMENT_REQUIRED = "Risk Adjustment is required.";
	public String getRiskAdjustmentRequiredMessage(){
		return RISK_ADJUSTMENT_REQUIRED;
	}
	
	private final String TRANSMISSION_FORMAT_REQUIRED = "Transmission Format is required.";
	public String getTransmissionFormatRequiredMessage(){
		return TRANSMISSION_FORMAT_REQUIRED;
	}
	
	private final String SUPPLEMENTAL_DATA_REQUIRED = "Supplemental Data Elements are required.";
	public String getSupplementalDataRequiredMessage(){
		return SUPPLEMENTAL_DATA_REQUIRED;
	}

	private final String GENERIC_ERROR_MESSAGE = "The Measure Authoring Tool was unable to process the request. Please try again. If the problem persists please contact the Help Desk.";
	public String getGenericErrorMessage(){
		return GENERIC_ERROR_MESSAGE;
	}
	
	private final String PASSWORD_REQUIRED_ERROR_MESSAGE = "Existing password is required to confirm changes.";
	public String getPasswordRequiredErrorMessage(){
		return PASSWORD_REQUIRED_ERROR_MESSAGE;
	}
	
	private final String PASSWORD_MISMATCH_ERROR_MESSAGE = "Incorrect password supplied.Try again";
	public String getPasswordMismatchErrorMessage(){
		return PASSWORD_MISMATCH_ERROR_MESSAGE;
	}
	
	private final String ALL_PASSWORD_FIELDS_REQUIRED = "All password fields are required.";
	public String getAllPasswordFieldsRequired(){
		return ALL_PASSWORD_FIELDS_REQUIRED;
	}
	
	private final String MEASURE_SELECTION_ERROR = "Please select at least one measure";
	public String getMeasureSelectionError(){
		return MEASURE_SELECTION_ERROR;
	}
	
	
	
	private final String TRANSFER_CHECKBOX_ERROR = "Please select at least one Value Set to transfer ownership.";
	public String getTransferCheckBoxError(){
		return TRANSFER_CHECKBOX_ERROR;
	}
	
	private final String TRANSFER_CHECKBOX_ERROR_MEASURE = "Please select at least one Measure to transfer ownership.";
	public String getTransferCheckBoxErrorMeasure(){
		return TRANSFER_CHECKBOX_ERROR_MEASURE;
	}
	
	private final String TRANSFER_OWNERSHIP_SUCCESS = "Ownership successfully transferred to ";
	public String getTransferOwnershipSuccess(){
		return TRANSFER_OWNERSHIP_SUCCESS;
	}
	private final String UNSAVED_CHANGES_IN_CANVAS_CLONE="You have unsaved changes. Please save your changes and then Clone.";
	public String getUnsavedChangesinCanvasClone(){
		return UNSAVED_CHANGES_IN_CANVAS_CLONE;
	}
	
	private final String SAVE_ERROR_MSG = "You have unsaved changes that will be discarded if you continue. " +
	"Do you want to continue without saving?";
	/**
	 * @return the sAVE_ERROR_MSG
	 */
	public String getSaveErrorMsg() {
		return SAVE_ERROR_MSG;
	}
	
	private final String NO_QDM_SELECTED="Please select a QDM element.";
	public String getNoQdmSelectedMessage(){
		return NO_QDM_SELECTED;
	}
	
	private final String RELATIONALOP_TWO_CHILD_MESSAGE = "Package Failed. Measure logic contains one or more Incomplete Timings. LHS and RHS are required for all Timings.";
	public String getRelationalOpTwoChildMessage() {
		return RELATIONALOP_TWO_CHILD_MESSAGE;
	}
	
	private final String MEASURE_DELETION_INVALID_PWD="The entered password is invalid. Please try again.";
	public String getMeasureDeletionInvalidPwd(){
		return MEASURE_DELETION_INVALID_PWD;
	}
	
	private final String MEASURE_DELETION_SUCCESS_MSG="Measure successfully deleted.";
	public String getMeasureDeletionSuccessMgs(){
		return MEASURE_DELETION_SUCCESS_MSG;
	}
	
	private final String DUPLICATE_APPLIED_QDM="Selected QDM element is already available in the applied elements list";
	public String getDuplicateAppliedQDMMsg(){
		return DUPLICATE_APPLIED_QDM;
		
	}
	
	private final String SUCCESSFUL_MODIFY_APPLIED_QDM="Selected QDM element has been modified successfully";
	public String getSuccessfulModifyQDMMsg(){
		return SUCCESSFUL_MODIFY_APPLIED_QDM;
		
	}
	
	private final String USER_REQUIRED_ERROR_MSG="Please select a user to transfer ownership.";
	public String getUserRequiredErrorMessage(){
		return USER_REQUIRED_ERROR_MSG;
		
	}
	
	private final String VALUE_SET_NAME_DATATYPE_REQD = "Please enter Name and select a Datatype associated with it.";
	public String getVALUE_SET_NAME_DATATYPE_REQD() {
		return VALUE_SET_NAME_DATATYPE_REQD;
	}
	
	private final String MEASURE_NOTES_REQUIRED_MESSAGE ="Text required in Title and Description fields.";
	public String getMEASURE_NOTES_REQUIRED_MESSAGE() {
		return MEASURE_NOTES_REQUIRED_MESSAGE;
	}
	
	private final String MEASURE_NOTES_DELETE_SUCCESS_MESSAGE ="The measure note deleted successfully.";
	public String getMEASURE_NOTES_DELETE_SUCCESS_MESSAGE() {
		return MEASURE_NOTES_DELETE_SUCCESS_MESSAGE;
	}
	
	private final String MEASURE_NOTES_SAVE_SUCCESS_MESSAGE ="The measure note saved successfully.";
	public String getMEASURE_NOTES_SAVE_SUCCESS_MESSAGE() {
		return MEASURE_NOTES_SAVE_SUCCESS_MESSAGE;
	}

	private final String UMLS_SUCCESSFULL_LOGIN="Successfully logged into UMLS";
	public String getUMLS_SUCCESSFULL_LOGIN() {
		return UMLS_SUCCESSFULL_LOGIN;
	}
	
	private final String UML_LOGIN_UNAVAILABLE ="Unable to verify your UMLS credentials. Please contact the MAT Help Desk or try again.";
	public String getUML_LOGIN_UNAVAILABLE() {
		return UML_LOGIN_UNAVAILABLE;
	}
	
	private final String UML_LOGIN_FAILED="Login failed. Please sign in again.";
	public String getUML_LOGIN_FAILED(){
		return UML_LOGIN_FAILED;
	}
	
	private final String UMLS_NOT_LOGGEDIN ="You are not logged in to UMLS. Please access the UMLS Account tab to continue.";
	public String getUMLS_NOT_LOGGEDIN() {
		return UMLS_NOT_LOGGEDIN;
	}
	
	private final String UMLS_OID_REQUIRED ="Please enter an OID.";
	public String getUMLS_OID_REQUIRED() {
		return UMLS_OID_REQUIRED;
	}
	
	private final String VSAC_RETRIEVE_FAILED = "Unable to retrieve from VSAC. Please check the data and try again.";
	public String getVSAC_RETRIEVE_FAILED() {
		return VSAC_RETRIEVE_FAILED;
	}
	
	private final String VSAC_UPDATE_SUCCESSFULL="Successfully updated applied QDM list with VSAC data.";
	public String getVSAC_UPDATE_SUCCESSFULL() {
		return VSAC_UPDATE_SUCCESSFULL;
	}
	
	private final String DELETE_MEASURE_WARNING_MESSAGE ="Deleting a draft or version of a measure will permanently remove the designated measure draft or " +
			"version from  the Measure Authoring Tool. Deleted measures cannot <br> be recovered.";
	/**
	 * @return the dELETE_MEASURE_WARNING_MESSAGE
	 */
	public String getDELETE_MEASURE_WARNING_MESSAGE() {
		return DELETE_MEASURE_WARNING_MESSAGE;
	}

	/**
	 * @return the mEASURE_PACKAGE_UMLS_NOT_LOGGED_IN
	 */
	public String getMEASURE_PACKAGE_UMLS_NOT_LOGGED_IN() {
		return MEASURE_PACKAGE_UMLS_NOT_LOGGED_IN;
	}

	private final String MEASURE_PACKAGE_UMLS_NOT_LOGGED_IN = "Measure packaged successfully. "
			+ "Value set data is not included in the measure package as you are not logged into UMLS.";

	private final String VALIDATION_MSG_ELEMENT_WITHOUT_VSAC ="Please enter Value Set name and select a Datatype associated with it.";
	public String getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC() {
		return VALIDATION_MSG_ELEMENT_WITHOUT_VSAC;
	}
	
}


