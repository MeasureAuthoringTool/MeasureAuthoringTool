package mat.client.cqlworkspace;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.HelpBlock;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.MatContext;
import mat.client.shared.MessagePanel;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.util.MatTextBox;
import mat.model.MatValueSet;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CQLModelValidator;
import mat.shared.StringUtility;

public abstract class AbstractCQLWorkspacePresenter {
	protected static final String CODES_SELECTED_SUCCESSFULLY = "All codes successfully selected.";
	protected static final String VALUE_SETS_SELECTED_SUCCESSFULLY = "All value sets successfully selected.";
	protected static final String UNABLE_TO_FIND_NODE_TO_MODIFY = "Unable to find Node to modify.";
	protected static final String UNAUTHORIZED_DELETE_OPERATION = "Unauthorized delete operation.";
	protected static final String SELECT_DEFINITION_TO_DELETE = "Please select a definition to delete.";
	protected static final String SELECT_FUNCTION_TO_DELETE = "Please select a function to delete.";
	protected static final String SELECT_PARAMETER_TO_DELETE = "Please select parameter to delete.";
	protected static final String SELECT_ALIAS_TO_DELETE = "Please select an alias to delete.";
	protected static final String RETURN_TYPE_OF_CQL_EXPRESSION = "Return Type of CQL Expression";
	protected static final String ERROR_SAVE_CQL_FUNCTION  = "Please enter function name.";
	protected static final String ERROR_SAVE_CQL_DEFINITION  = "Please enter definition name.";
	protected static final String ERROR_DUPLICATE_IDENTIFIER_NAME  = "Name already exists.";
	protected static final String NO_LIBRARIES_RETURNED = "No libraries returned.Please search again.";
	protected static final String WARNING_PASTING_IN_VALUESET = "The clipboard does not contain any value sets to be pasted at this time.";
	protected static final String SUCCESSFULLY_VALUESET_PASTE = "Selected value sets have been pasted successfully.";
	protected static final String SUCCESSFULLY_PASTED_CODES_IN_MEASURE = "Selected Codes have been pasted successfully.";
	protected static final String CLIPBOARD_DOES_NOT_CONTAIN_CODES = "The clipboard does not contain any codes to be pasted at this time.";
	protected static final String VALUE_SETS_COPIED_SUCCESSFULLY = "Value Sets successfully copied.";
	protected static final String CODES_COPIED_SUCCESSFULLY = "Codes successfully copied.";
	protected static final String COPY_QDM_SELECT_ATLEAST_ONE  = "Please select at least one Value Set to copy.";
	protected static final String COPY_CODE_SELECT_ATLEAST_ONE  = "Please select at least one Code to copy.";
	protected static final String VIEW_CQL_WARNING_MESSAGE = "You are viewing CQL with validation warnings. Warnings are marked with a yellow triangle on the line number.";
	protected static final String VIEW_CQL_ERROR_MESSAGE = "You are viewing CQL with validation errors. Errors are marked with a red square on the line number.";
	protected static final String VIEW_CQL_NO_ERRORS_MESSAGE ="You are viewing CQL with no validation errors.";
	protected static final String VIEW_CQL_ERROR_MESSAGE_BAD_VALUESET_DATATYPE ="CQL file with validation errors. You have an incorrect value set/code datatype combination.";
	protected static final String ERROR_SAVE_CQL_PARAMETER  = "Please enter parameter name.";
	protected static final String DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE = "Name can not be an exact match to a defined CQL keyword.";
	protected static final String ERROR_PARAMETER_NAME_NO_SPECIAL_CHAR  = "Invalid Parameter name. Duplicate name or use of restricted character(s).";
	protected static final String ERROR_DEFINITION_NAME_NO_SPECIAL_CHAR  = "Invalid Definition name. Duplicate name or use of restricted character(s).";
	protected static final String ERROR_FUNCTION_NAME_NO_SPECIAL_CHAR  = "Invalid Function and/or Argument name. Duplicate name or use of restricted character(s).";
	protected static final String SAVE_INCLUDE_LIBRARY_VALIATION_ERROR = "Alias name and CQL Library selection are required.";
	protected static final String UMLS_INVALID_CODE_IDENTIFIER = "Invalid code identifier. Please copy the complete URL for the code directly from VSAC and try again.";
	protected static final String INVALID_QDM_VERSION_IN_INCLUDES ="The current QDM version and the QDM version of one or more of the included libraries are not the same. Please navigate to the Includes section to replace or remove the conflicting libraries.";
	protected static final String NO_LIBRARY_TO_REPLACE ="Please select a library to replace.";
	protected static final String VSAC_UPDATE_SUCCESSFULL = "Successfully updated applied Value Set list with VSAC data.";
	protected static final String SUCCESSFUL_MODIFY_APPLIED_VALUESET = "Selected value set has been modified successfully.";
	protected static final String SUCCESSFUL_MODIFY_APPLIED_CODE = "Selected code has been modified successfully.";
	protected static final String ARGUMENT = "Argument";
	protected static final String LIBRARY = "Library";
	protected static final String PARAMETER = "Parameter";
	protected static final String FUNCTION = "Function";
	protected static final String DEFINITION = "Definition";
	protected static final String CODE = "Code";
	protected static final String VALUESET = "Value set";
	
	protected static final String EMPTY_STRING = "";
	protected HelpBlock helpBlock = new HelpBlock();
	protected MessagePanel messagePanel = new MessagePanel();
	protected SimplePanel panel = new SimplePanel();
	protected String setId = null;
	protected String currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
	protected String nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
	protected boolean isModified = false;
	protected AceEditor curAceEditor;
	protected CQLModelValidator validator = new CQLModelValidator();
	protected CQLCode modifyCQLCode;
	protected boolean isCodeModified = false;
	protected boolean isUserDefined = false;
	protected String currentIncludeLibrarySetId = null;
	protected String currentIncludeLibraryId = null;
	protected boolean previousIsProgramReleaseBoxEnabled = true; 
	protected boolean previousIsProgramListBoxEnabled = true; 
	protected boolean previousIsRetrieveButtonEnabled = true; 
	protected boolean previousIsApplyButtonEnabled = false; 
	protected boolean isProgramReleaseBoxEnabled = true; 
	protected boolean isRetrieveButtonEnabled = true; 
	protected boolean isApplyButtonEnabled = false; 
	protected boolean isProgramListBoxEnabled = true; 
	protected String cqlLibraryComment;
	protected boolean isFormattable = true;
	protected static Boolean isPageDirty = false;
	protected List<CQLQualityDataSetDTO> appliedValueSetTableList = new ArrayList<CQLQualityDataSetDTO>();
	protected List<CQLCode> appliedCodeTableList = new ArrayList<CQLCode>();
	protected CQLQualityDataSetDTO modifyValueSetDTO;
	protected MatValueSet currentMatValueSet= null;
	
	protected final VSACAPIServiceAsync vsacapiService = MatContext.get().getVsacapiServiceAsync();
	protected QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT.create(QDSAttributesService.class);
	protected DeleteConfirmationDialogBox deleteConfirmationDialogBox = new DeleteConfirmationDialogBox();
	
	protected HelpBlock buildHelpBlock() {
		helpBlock = new HelpBlock();
		helpBlock.setText("");
		helpBlock.setColor("transparent");
		helpBlock.setHeight("0px");
		helpBlock.setPaddingTop(0.0);
		helpBlock.setPaddingBottom(0.0);
		helpBlock.setMarginBottom(0.0);
		helpBlock.setMarginTop(0.0);
		return helpBlock;
	}
	
	protected void showUnsavedChangesWarning() {
		messagePanel.clearAlerts();
		messagePanel.getWarningConfirmationMessageAlert().createAlert();
		messagePanel.getWarningConfirmationYesButton().setFocus(true);
	}
	
	public void setHelpBlockText(String message) {
		helpBlock.setText(message);
		helpBlock.getElement().setAttribute("role", "alert");
		helpBlock.getElement().focus();
	}
	
	public void setIsPageDirty(Boolean isPageDirty) {
		AbstractCQLWorkspacePresenter.isPageDirty = isPageDirty;
	}
	
	public Boolean getIsPageDirty() {
		return isPageDirty;
	}	
	
	protected boolean isValidExpressionName(String expressionName) {
		final String trimedExpression = expressionName.trim();
		return !trimedExpression.isEmpty() && !trimedExpression.equalsIgnoreCase("Patient") && !trimedExpression.equalsIgnoreCase("Population")
				&& MatContext.get().getCqlConstantContainer() != null 
				&& MatContext.get().getCqlConstantContainer().getCqlKeywordList() != null
				&& MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlKeywordsList() != null
				&& !MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlKeywordsList().stream().anyMatch(definedKeyWord -> definedKeyWord.equalsIgnoreCase(trimedExpression));
	}
	
	public MessagePanel getMessagePanel() {
		return messagePanel;
	}
	
	public HelpBlock getHelpBlock () {
		return helpBlock;
	}

	public DeleteConfirmationDialogBox getDeleteConfirmationDialogBox() {
		return deleteConfirmationDialogBox;
	}

	public void setDeleteConfirmationDialogBox(DeleteConfirmationDialogBox deleteConfirmationDialogBox) {
		this.deleteConfirmationDialogBox = deleteConfirmationDialogBox;
	}
	
	public Button getDeleteConfirmationDialogBoxYesButton() {
		return deleteConfirmationDialogBox.getYesButton();
	}

	public Button getDeleteConfirmationDialogBoxNoButton() {
		return deleteConfirmationDialogBox.getNoButton();
	}
	
	protected void displayErrorMessage(final String errorMessage, final String message, MatTextBox textBox) {
		messagePanel.getSuccessMessageAlert().clearAlert();
		messagePanel.getErrorMessageAlert().createAlert(errorMessage);
		textBox.setText(message.trim());
	}
	
	protected String build508HelpString(boolean previousState, boolean currentState, String elementName) {
		String helpString = EMPTY_STRING;
		if(currentState != previousState) {
			helpString = elementName.concat(" ").concat(Boolean.TRUE.equals(currentState) ? "enabled" : "disabled");
		}
		
		return helpString; 
	}
	
	protected String getCodeSuccessMessage(String codeName) {
		return "Code " + codeName + " has been successfully applied.";
	}
	
	protected String getIncludeLibrarySuccessMessage(String aliasName) {
		return " Library " + aliasName + " successfully included.";
	}
	
	protected String getBirthdateOrDeadMessage(String codeSystemName, String codeId) {
		return "The " + codeSystemName + " Code " + codeId  + " is a default code already provided. Multiple instances of this code are not allowed.";
	}

	protected String generateDuplicateErrorMessage(String name){
		return StringUtility.trimTextToSixtyChars(name) + " already exists. Please add a unique suffix.";
	}
	
	protected String getValuesetSuccessMessage(String codeListName) {
		return "Value set " + StringUtility.trimTextToSixtyChars(codeListName) + " has been successfully applied.";
	}
	
	protected String getValuesetSuccessfulReterivalMessage(String codeListName) {
		return "Value set " + StringUtility.trimTextToSixtyChars(codeListName) + " successfully retrieved from VSAC.";
	}
	
	protected String buildRemovedSuccessfullyMessage(String type, String name) {
		return type + " " + StringUtility.trimTextToSixtyChars(name) + " has been removed successfully.";
	}
	
	protected String buildSuccessfullySavedMessage(String type, String name) {
		return type + " " + StringUtility.trimTextToSixtyChars(name) + " successfully saved.";
	}
	
	protected String buildSuccessfullySavedWithErrors(String type, String name) {
		return type + " " + StringUtility.trimTextToSixtyChars(name) +" successfully saved with errors.";
	}
	
	protected String buildSelectedToDeleteMessage(String type, String name) {
		return "You have selected to delete " + type.toLowerCase() + " " + StringUtility.trimTextToSixtyChars(name) + ".";
	}
	
	protected String buildSelectedToDeleteWithConfirmationMessage(String type, String name) {
		String confirmationType = type;
		switch(type) {
		case LIBRARY:
			confirmationType = "library alias";
		case CODE:
			break;
		default:
			confirmationType = type.toLowerCase();
			break;
		}
		return "You have selected to delete " + type.toLowerCase() + " " + StringUtility.trimTextToSixtyChars(name) + ". Please confirm that you want to remove this " + confirmationType + ".";
	}
	
	public abstract CQLWorkspaceView getCQLWorkspaceView();
}
