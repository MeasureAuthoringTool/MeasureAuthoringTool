package mat.client.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.DTO.UserPreferenceDTO;
import mat.client.Mat;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.cqlworkspace.codes.CQLCodesView;
import mat.client.cqlworkspace.generalinformation.CQLGeneralInformationUtility;
import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility;
import mat.client.expressionbuilder.modal.ExpressionBuilderHomeModal;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.MessagePanel;
import mat.client.shared.ValueSetNameInputValidator;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.client.util.MatTextBox;
import mat.model.CQLValueSetTransferObject;
import mat.model.CodeListSearchDTO;
import mat.model.GlobalCopyPasteObject;
import mat.model.MatValueSet;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CQLError;
import mat.shared.CQLIdentifierObject;
import mat.shared.CQLModelValidator;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.StringUtility;

import static mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility.getOidFromUrl;
import static mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility.isFhirUrl;

public abstract class AbstractCQLWorkspacePresenter {

	public static final int CQL_LIBRARY_NAME_WARNING_LENGTH = 200;
	public static final String CQL_LIBRARY_NAME_WARNING_MESSAGE = "The CQL Library name exceeds 200 characters. Long CQL Library names may cause problems upon export with zip files and file storage.";

	protected static final String PATIENT = "Patient";
	protected static final String POPULATION = "Population";
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
	protected static final String VIEW_CQL_ERROR_MESSAGE = "You are viewing the CQL file with validation errors. Errors are marked with a red square on the line number.";
	protected static final String VIEW_CQL_ERROR_MESSAGE_COMPOSITE_AND_INCLUDED = "You are viewing the CQL file with validation errors. Errors in the main library are marked with a red square on the line number. To view errors in included libraries or component measures, navigate to those sections of the CQL Workspace.";
	protected static final String VIEW_CQL_ERROR_MESSAGE_COMPOSITE = "You are viewing the CQL file with validation errors. Errors in the main library are marked with a red square on the line number. To view errors in component measures, navigate to the Components section of the CQL Workspace.";
	protected static final String VIEW_CQL_ERROR_MESSAGE_INCLUDED = "You are viewing the CQL file with validation errors. Errors in the main library are marked with a red square on the line number. To view errors in included libraries, navigate to the Includes section of the CQL Workspace. ";
	protected static final String VIEW_CQL_ERROR_MESSAGE_INCLUDE_LIBS_ERRORS = "These included libraries have errors: ";
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
	protected static final String INCORRECT_VALUE_SET_CODE_DATATYPE_COMBINATION = "There is an incorrect value set/code datatype combination.";
	protected static final String ARGUMENT = "Argument";
	protected static final String LIBRARY = "Library";
	protected static final String PARAMETER = "Parameter";
	protected static final String FUNCTION = "Function";
	protected static final String DEFINITION = "Definition";
	protected static final String CODE = "Code";
	protected static final String VALUESET = "Value set";
	protected static final String PANEL_COLLAPSE_IN = "panel-collapse collapse in";
	protected static final String PANEL_COLLAPSE_COLLAPSE = "panel-collapse collapse";
	protected static final String INVALID_INPUT_DATA = "Invalid Input data.";
	protected static final String EMPTY_STRING = "";

	protected final Logger logger = Logger.getLogger("MAT");
	protected HelpBlock helpBlock = new HelpBlock();
	protected MessagePanel messagePanel = new MessagePanel();
	protected CQLWorkspaceView cqlWorkspaceView;
	protected SimplePanel panel = new SimplePanel();
	protected String setId = null;
	protected String currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
	protected String nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
	protected boolean isModified = false;
	protected boolean isLibraryNameExists = false;
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
	protected String cqlLibraryName;
	protected enum Color {
		RED, YELLOW, GREEN;
	}

	protected final VSACAPIServiceAsync vsacapiService = MatContext.get().getVsacapiServiceAsync();
	protected QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT.create(QDSAttributesService.class);
	protected DeleteConfirmationDialogBox deleteConfirmationDialogBox = new DeleteConfirmationDialogBox();

	protected abstract void componentsEvent();
	protected abstract void focusSkipLists();
	protected abstract void buildCQLView();
	protected abstract boolean hasEditPermissions();
	public abstract boolean isStandaloneCQLLibrary();
	protected abstract void exportErrorFile();
	protected abstract void setGeneralInformationViewEditable(boolean isEditable);
	protected abstract void addVSACCQLValueset();
	protected abstract void addUserDefinedValueSet();
	protected abstract void modifyCodes();
	protected abstract void addNewCodes();
	protected abstract void pasteCodes();
	protected abstract void deleteDefinition();
	protected abstract void deleteFunction();
	protected abstract void deleteFunctionArgument();
	protected abstract void deleteParameter();
	protected abstract void deleteInclude();
	protected abstract void checkAndDeleteValueSet();
	protected abstract void deleteCode();
	protected abstract void searchValueSetInVsac(String release, String expansionProfile);
	protected abstract void updateVSACValueSets();
	protected abstract void pasteValueSets();
	protected abstract void updateAppliedValueSetsList(MatValueSet matValueSet, CodeListSearchDTO codeListSearchDTO, CQLQualityDataSetDTO qualityDataSetDTO);
	protected abstract void addIncludeLibraryInCQLLookUp();
	protected abstract void showCompleteCQL(AceEditor aceEditor);
	protected abstract void addAndModifyParameters();
	protected abstract void addAndModifyFunction();
	protected abstract void addAndModifyDefintions();
	protected abstract void getAppliedValuesetAndCodeList();
	protected abstract String getCurrentModelType();

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

	public static boolean isValidExpressionName(String expressionName) {
		final String trimedExpression = expressionName.trim();
		return !trimedExpression.isEmpty() && !trimedExpression.equalsIgnoreCase(PATIENT)
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

	protected void buildInsertPopUp(String modelType) {
		cqlWorkspaceView.resetMessageDisplay();
		InsertIntoAceEditorDialogBox.showListOfItemAvailableForInsertDialogBox(curAceEditor, this, modelType);
	}

	protected void buildCQLViewSuccess(SaveUpdateCQLResult result) {
		if (result.getCqlString() != null && !result.getCqlString().isEmpty()) {
			cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor().clearAnnotations();
			cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor().removeAllMarkers();
			cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor().redisplay();
			cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor().setText(result.getCqlString());

			messagePanel.clearAlerts();

			List<CQLError> errors = new ArrayList<>();
			List<CQLError> warnings = new ArrayList<>();

			addErrorsAndWarningsForParentLibrary(result, errors, warnings);
			SharedCQLWorkspaceUtility.displayMessagesForViewCQL(result, cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor(), messagePanel);
			cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor().setAnnotations();
			cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor().redisplay();
		}
	}

	protected List<CQLIdentifierObject> getDefinitionList(List<CQLDefinition> definitionList) {
		List<CQLIdentifierObject> defineList = new ArrayList<>();

		for (CQLDefinition cqlDefinition: definitionList) {
			CQLIdentifierObject definition = new CQLIdentifierObject(null, cqlDefinition.getName(), cqlDefinition.getId());
			defineList.add(definition);
		}

		return defineList;
	}

	protected List<CQLIdentifierObject> getParameterList(List<CQLParameter> parameterList) {
		List<CQLIdentifierObject> paramList = new ArrayList<>();

		for (CQLParameter cqlParameter: parameterList) {
			CQLIdentifierObject parameter = new CQLIdentifierObject(null, cqlParameter.getName(), cqlParameter.getId());
			paramList.add(parameter);
		}

		return paramList;
	}

	protected List<CQLIdentifierObject> getFunctionList(List<CQLFunctions> functionList) {
		List<CQLIdentifierObject> funcList = new ArrayList<>();

		for (CQLFunctions cqlFunction: functionList) {
			CQLIdentifierObject function = new CQLIdentifierObject(null, cqlFunction.getName(), cqlFunction.getId());
			funcList.add(function);
		}

		return funcList;
	}

	protected List<String> getIncludesList(List<CQLIncludeLibrary> includesList) {
		List<String> incLibList = new ArrayList<>();

		for (CQLIncludeLibrary cqlIncludeLibrary: includesList) {
			incLibList.add(cqlIncludeLibrary.getAliasName());
		}

		return incLibList;
	}

	protected void valueSetViewRetrieveFromVSACClicked() {
		if (hasEditPermissions()) {
			cqlWorkspaceView.resetMessageDisplay();
			String release;
			String expansionProfile = null;

			release = cqlWorkspaceView.getValueSetView().getReleaseListBox().getSelectedValue();
			release = MatContext.PLEASE_SELECT.equals(release) ? null : release;

			String program = cqlWorkspaceView.getValueSetView().getProgramListBox().getSelectedValue();
			program = MatContext.PLEASE_SELECT.equals(program) ? null : program;

			if(null == release && null != program) {
				HashMap<String, String> pgmProfileMap = (HashMap<String, String>) MatContext.get().getProgramToLatestProfile();
				expansionProfile = pgmProfileMap.get(program);
			}
			if(release != null && program == null) {
				messagePanel.getErrorMessageAlert().createAlert(SharedCQLWorkspaceUtility.MUST_HAVE_PROGRAM_WITH_RELEASE);
			} else {
				searchValueSetInVsac(release, expansionProfile);
				// 508 compliance for Value Sets
				cqlWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
			}
		}
	}

	private void addErrorsAndWarningsForParentLibrary(SaveUpdateCQLResult result, List<CQLError> errors, List<CQLError> warnings) {
		String formattedName = result.getCqlModel().getFormattedName();
		if(result.getLibraryNameErrorsMap().get(formattedName) != null) {
			errors.addAll(result.getLibraryNameErrorsMap().get(formattedName));
		}

		if(result.getLibraryNameWarningsMap().get(formattedName) != null) {
			warnings.addAll(result.getLibraryNameWarningsMap().get(formattedName));
		}
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

	protected void searchCQLCodesInVsac() {
		final String url = cqlWorkspaceView.getCodesView().getCodeSearchInput().getValue().trim();
		cqlWorkspaceView.getCodesView().getCodeSearchInput().setText(url);
		messagePanel.getSuccessMessageAlert().clearAlert();
		if (!MatContext.get().isUMLSLoggedIn()) {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			messagePanel.getErrorMessageAlert().setVisible(true);
			return;
		}

		if ((url == null) || url.trim().isEmpty()) {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_CODE_IDENTIFIER_REQUIRED());
			return;
		}

		if (validator.validateForCodeIdentifier(url)) {
			cqlWorkspaceView.getCodesView().getApplyButton().setEnabled(false);
			messagePanel.getErrorMessageAlert().createAlert(UMLS_INVALID_CODE_IDENTIFIER);

			return;
		} else {
			retrieveCodeReferences(url);
		}

	}

	private void retrieveCodeReferences(String url) {
		showSearchingBusy(true);
		vsacapiService.getDirectReferenceCode(url, new AsyncCallback<VsacApiResult>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error in vsacapiService.getDirectReferenceCode. Error message: " + caught.getMessage(), caught);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);
			}

			@Override
			public void onSuccess(VsacApiResult result) {

				if (result.isSuccess()) {
					cqlWorkspaceView.getCodesView().getCodeDescriptorInput().setValue(result.getDirectReferenceCode().getCodeDescriptor());
					cqlWorkspaceView.getCodesView().getCodeInput().setValue(result.getDirectReferenceCode().getCode());
					cqlWorkspaceView.getCodesView().getCodeSystemInput().setValue(result.getDirectReferenceCode().getCodeSystemName());
					cqlWorkspaceView.getCodesView().getCodeSystemVersionInput().setValue(result.getDirectReferenceCode().getCodeSystemVersion());
					cqlWorkspaceView.getCodesView().setCodeSystemOid(result.getDirectReferenceCode().getCodeSystemOid());
					messagePanel.getSuccessMessageAlert().createAlert("Code " + result.getDirectReferenceCode().getCode() + " successfully retrieved from VSAC.");
					cqlWorkspaceView.getCodesView().getApplyButton().setEnabled(true);
					CQLCode code = buildCQLCodeFromCodesView(StringUtility.removeEscapedCharsFromString(cqlWorkspaceView.getCodesView().getCodeDescriptorInput().getValue()));
					cqlWorkspaceView.getCodesView().setValidateCodeObject(code);
				} else {
					String message = convertMessage(result.getFailureReason());
					messagePanel.getErrorMessageAlert().createAlert(message);
					messagePanel.getErrorMessageAlert().setVisible(true);
				}

				showSearchingBusy(false);
				cqlWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
			}
		});
	}

	protected void addCQLLibraryEditorViewHandlers() {
		cqlWorkspaceView.getCQLLibraryEditorView().getExportErrorFile().addClickHandler(event -> exportErrorFile());
		cqlWorkspaceView.getCQLLibraryEditorView().getInsertButton().addClickHandler(event -> buildInsertPopUp(getCurrentModelType()));
		cqlWorkspaceView.getCQLLibraryEditorView().getSaveButton().addClickHandler(event -> saveCQLFile());
	}

	protected abstract void saveCQLFile();

	protected void onSaveCQLFileSuccess(SaveUpdateCQLResult result) {
		messagePanel.clearAlerts();
		Color c = Color.GREEN;
		cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor().clearAnnotations();
		SharedCQLWorkspaceUtility.displayAnnotationForViewCQL(result, cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor());
		List<String> errorMessages = new ArrayList<>();

		if(!result.getLinterErrorMessages().isEmpty() || !result.getCqlErrors().isEmpty()) {
			errorMessages.add("The CQL file was saved with errors.");

			if(!result.getLinterErrorMessages().isEmpty()) {
				result.getLinterErrorMessages().forEach(e -> errorMessages.add(e));
			}

			c = Color.RED;
			SharedCQLWorkspaceUtility.displayAnnotationForViewCQL(result, cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor());
		}

		else {
			errorMessages.add("Changes to the CQL File have been successfully saved.");
		}

		if(!result.getLinterWarningMessages().isEmpty()) {
			errorMessages.addAll(result.getLinterWarningMessages());
			if(c != Color.RED) {
				c = Color.YELLOW;
			}
		}

		if(!result.isDatatypeUsedCorrectly()) {
			errorMessages.add(INCORRECT_VALUE_SET_CODE_DATATYPE_COMBINATION);
			c = Color.RED;
		}
		setSpecificErrorMessage(c, errorMessages);
	}

	private void setSpecificErrorMessage(Color color, List<String> errorMessages) {
		switch(color)
		{
			case GREEN:
				messagePanel.getSuccessMessageAlert().createAlert("Changes to the CQL File have been successfully saved.");
				break;
			case YELLOW:
				messagePanel.getWarningMessageAlert().createAlert(errorMessages);
				break;
			case RED:
				messagePanel.getErrorMessageAlert().createAlert(errorMessages);
				break;
		}
	}

    protected void onSaveCQLFileFailure(SaveUpdateCQLResult result) {
		logger.log(Level.INFO, "onSaveCQLFileFailure failureReason" + result.getFailureReason());
		SharedCQLWorkspaceUtility.displayAnnotationForViewCQL(result, cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor());
		if (result.getFailureReason() == SaveUpdateCQLResult.CUSTOM) {
			StringBuilder msg = new StringBuilder();
			result.getCqlErrors().forEach(e -> msg.append(msg.length() > 0 ? ", " : "").append(e.getErrorMessage()));
			messagePanel.getErrorMessageAlert().createAlert("The MAT was unable to save the changes. Errors: " + msg);
		}
        if (result.getFailureReason() == SaveUpdateCQLResult.SYNTAX_ERRORS) {
            messagePanel.getErrorMessageAlert().createAlert("The MAT was unable to save the changes. All items entered must be written in the correct CQL syntax. The line where MAT is no longer able to read the file is marked with a red square.");
        } else if (result.getFailureReason() == SaveUpdateCQLResult.DUPLICATE_CQL_KEYWORD) {
            messagePanel.getErrorMessageAlert().createAlert("The CQL file could not be saved. All identifiers must be unique and can not match any CQL keywords");
        }
    }

    protected void onModifyValueSet(CQLQualityDataSetDTO result, boolean isUserDefined) {
        String oid = isUserDefined ? EMPTY_STRING :
                isFhirUrl(result.getOid()) ? getOidFromUrl(result.getOid()) :
                        result.getOid();

        cqlWorkspaceView.getValueSetView().getOIDInput().setEnabled(true);
        cqlWorkspaceView.getValueSetView().getOIDInput().setValue(oid);
        cqlWorkspaceView.getValueSetView().getOIDInput().setTitle(oid);

		cqlWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(!isUserDefined);

		cqlWorkspaceView.getValueSetView().getUserDefinedInput().setEnabled(isUserDefined);
		cqlWorkspaceView.getValueSetView().getUserDefinedInput().setValue(result.getOriginalCodeListName());
		cqlWorkspaceView.getValueSetView().getUserDefinedInput().setTitle(result.getOriginalCodeListName());

		cqlWorkspaceView.getValueSetView().getSuffixInput().setEnabled(true);
		cqlWorkspaceView.getValueSetView().getSuffixInput().setValue(result.getSuffix());
		cqlWorkspaceView.getValueSetView().getSuffixInput().setTitle(result.getSuffix());

		setReleaseAndProgramFieldsOnEdit(result);
		cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(isUserDefined);
		alert508StateChanges();
	}


	private void setReleaseAndProgramFieldsOnEdit(CQLQualityDataSetDTO result) {
		previousIsProgramReleaseBoxEnabled = isProgramReleaseBoxEnabled;
		CQLAppliedValueSetUtility.setProgramsAndReleases(result.getProgram(), result.getRelease(), cqlWorkspaceView.getValueSetView());
		isProgramReleaseBoxEnabled = true;
	}


	protected CQLCode buildCQLCodeFromCodesView(String codeName) {
		CQLCode refCode = new CQLCode();
		CQLCodesView codesView = cqlWorkspaceView.getCodesView();
		boolean isCodeSystemVersionIncluded = codesView.getIncludeCodeSystemVersionCheckBox().getValue();
		refCode.setCodeOID(codesView.getCodeInput().getValue());
		refCode.setName(codesView.getCodeDescriptorInput().getValue());
		refCode.setCodeSystemName(codesView.getCodeSystemInput().getValue());
		refCode.setCodeSystemVersion(codesView.getCodeSystemVersionInput().getValue());
		refCode.setCodeIdentifier(codesView.getCodeSearchInput().getValue());
		refCode.setCodeSystemOID(codesView.getCodeSystemOid());
		refCode.setIsCodeSystemVersionIncluded(isCodeSystemVersionIncluded);

		if(!codesView.getSuffixTextBox().getValue().isEmpty()){
			refCode.setSuffix(codesView.getSuffixTextBox().getValue());
			refCode.setDisplayName(codeName+" ("+codesView.getSuffixTextBox().getValue()+")");
		} else {
			refCode.setDisplayName(codeName);
		}

		return refCode;
	}

	protected void alert508StateChanges() {
		StringBuilder helpTextBuilder = new StringBuilder();
		helpTextBuilder.append(build508HelpString(previousIsProgramListBoxEnabled, isProgramListBoxEnabled, "Program and Release List Boxes"));
		helpTextBuilder.append(build508HelpString(previousIsRetrieveButtonEnabled, isRetrieveButtonEnabled, "Retrieve Button"));
		helpTextBuilder.append(build508HelpString(previousIsApplyButtonEnabled, isApplyButtonEnabled, "Apply Button"));
		cqlWorkspaceView.getValueSetView().getHelpBlock().setText(helpTextBuilder.toString());
	}

	protected void clearOID() {
		previousIsRetrieveButtonEnabled = isRetrieveButtonEnabled;
		previousIsProgramListBoxEnabled = isProgramListBoxEnabled;
		cqlWorkspaceView.resetMessageDisplay();
		isUserDefined = cqlWorkspaceView.getValueSetView().validateOIDInput();
		if (cqlWorkspaceView.getValueSetView().getOIDInput().getValue().length() <= 0 ) {
			isRetrieveButtonEnabled = true;
			isProgramListBoxEnabled = true;
			cqlWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);
			loadProgramsAndReleases();
		} else {
			isRetrieveButtonEnabled = true;
			cqlWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);
		}

		alert508StateChanges();
	}

	protected void onModifyCode(CQLCode cqlCode) {
		CQLCodesView codesView = cqlWorkspaceView.getCodesView();
		codesView.getCodeSearchInput().setValue(cqlCode.getCodeIdentifier());
		codesView.getSuffixTextBox().setValue(cqlCode.getSuffix());
		codesView.getCodeDescriptorInput().setValue(cqlCode.getName());
		codesView.getCodeInput().setValue(cqlCode.getCodeOID());
		codesView.getCodeSystemInput().setValue(cqlCode.getCodeSystemName());
		codesView.getCodeSystemVersionInput().setValue(cqlCode.getCodeSystemVersion());
		codesView.setCodeSystemOid(cqlCode.getCodeSystemOID());
		codesView.getIncludeCodeSystemVersionCheckBox().setValue(cqlCode.isIsCodeSystemVersionIncluded());
		codesView.getApplyButton().setEnabled(false);
	}

	protected void copyCodes() {
		cqlWorkspaceView.resetMessageDisplay();
		if(cqlWorkspaceView.getCodesView().getCodesSelectedList() != null && cqlWorkspaceView.getCodesView().getCodesSelectedList().size() > 0){
			mat.model.GlobalCopyPasteObject gbCopyPaste = new GlobalCopyPasteObject();
			gbCopyPaste.setCopiedCodeList(cqlWorkspaceView.getCodesView().getCodesSelectedList());
			MatContext.get().setGlobalCopyPaste(gbCopyPaste);
			messagePanel.getSuccessMessageAlert().createAlert(CODES_COPIED_SUCCESSFULLY);
		} else {
			messagePanel.getErrorMessageAlert().createAlert(COPY_CODE_SELECT_ATLEAST_ONE);
		}
	}

	protected void selectAllCodes() {
		cqlWorkspaceView.resetMessageDisplay();
		if(cqlWorkspaceView.getCodesView().getAllCodes() != null) {
			cqlWorkspaceView.getCodesView().selectAll();
			messagePanel.getSuccessMessageAlert().createAlert(CODES_SELECTED_SUCCESSFULLY);
		}
	}

	protected void clearAlias() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
		setIsPageDirty(false);
		if ((cqlWorkspaceView.getIncludeView().getAliasNameTxtArea() != null)) {
			cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText(EMPTY_STRING);
		}
		if ((cqlWorkspaceView.getIncludeView().getViewCQLEditor().getText() != null)) {
			cqlWorkspaceView.getIncludeView().getViewCQLEditor().setText(EMPTY_STRING);
		}

		if ((cqlWorkspaceView.getIncludeView().getSearchTextBox().getText() != null)) {
			cqlWorkspaceView.getIncludeView().getSearchTextBox().setText(EMPTY_STRING);
		}
		cqlWorkspaceView.getIncludeView().getSelectedObjectList().clear();
		cqlWorkspaceView.getIncludeView().setSelectedObject(null);
		cqlWorkspaceView.getIncludeView().setIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryMap()));
		unCheckAvailableLibraryCheckBox();

		cqlWorkspaceView.getCQLLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText(EMPTY_STRING);
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().setItemSelected(
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(), false);
		}
	}

	protected void showSearchingBusy(final boolean busy) {
		showSearchBusyOnDoubleClick(busy);
		if (hasEditPermissions()) {
			switch(currentSection.toLowerCase()) {
			case(CQLWorkSpaceConstants.CQL_GENERAL_MENU):
				setGeneralInformationViewEditable(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
				cqlWorkspaceView.getIncludeView().setIsEditable(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_APPLIED_QDM):
				cqlWorkspaceView.getValueSetView().setIsEditable(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_CODES):
				cqlWorkspaceView.getCodesView().setIsEditable(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
				cqlWorkspaceView.getCQLParametersView().setIsEditable(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_DEFINE_MENU):
				cqlWorkspaceView.getCQLDefinitionsView().setIsEditable(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
				cqlWorkspaceView.getCQLFunctionsView().setIsEditable(!busy);
			break;
			}

		}
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsLoading(busy);
	}

	protected void codesViewSaveButtonClicked() {
		if (hasEditPermissions()) {
			MatContext.get().clearDVIMessages();
			cqlWorkspaceView.resetMessageDisplay();
			if(isCodeModified && modifyCQLCode != null) {
				modifyCodes();
			} else if (null != cqlWorkspaceView.getCodesView().getCodeSearchInput().getValue()
					&& !cqlWorkspaceView.getCodesView().getCodeSearchInput().getValue().isEmpty()) {
				addNewCodes();
			}
			cqlWorkspaceView.getCodesView().getCodeInput().setFocus(true);
		}
	}

	protected void codesViewRetrieveFromVSACButtonClicked() {
		if (hasEditPermissions()) {
			cqlWorkspaceView.resetMessageDisplay();
			searchCQLCodesInVsac();
			cqlWorkspaceView.getCodesView().getCodeInput().setFocus(true);
		}
	}

	protected void pasteCodesClicked(ClickEvent event) {
		if (hasEditPermissions()) {
			pasteCodes();
		} else {
			event.preventDefault();
		}
	}

	protected void codesViewClearButtonClicked() {
		if (!cqlWorkspaceView.getCodesView().getIsLoading()) {
			cqlWorkspaceView.resetMessageDisplay();
			cqlWorkspaceView.getCodesView().clearSelectedCheckBoxes();
		}
	}

	protected void addNewParameter() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		setIsPageDirty(false);
		if ((cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().getText() != null)) {
			cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().setText(EMPTY_STRING);
		}
		if ((cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea() != null)) {
			cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(EMPTY_STRING);
		}

		if ((cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea() != null)) {
			cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea().setText(EMPTY_STRING);
		}

		if (hasEditPermissions()) {
			cqlWorkspaceView.getCQLParametersView().setWidgetReadOnly(hasEditPermissions());
			cqlWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}

		cqlWorkspaceView.getCQLLeftNavBarPanelView().getSearchSuggestParamTextBox().setText(EMPTY_STRING);
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex() >= 0) {
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().setItemSelected(
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(), false);
		}

		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
	}

	protected void showSearchBusyOnDoubleClick(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getGeneralInformation().setEnabled(!busy);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getCodesLibrary().setEnabled(!busy);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQDM().setEnabled(!busy);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterLibrary().setEnabled(!busy);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionLibrary().setEnabled(!busy);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionLibrary().setEnabled(!busy);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getCQLLibraryEditorTab().setEnabled(!busy);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesLibrary().setEnabled(!busy);
	}

	protected void resetViewCQLCollapsiblePanel(PanelCollapse panelCollapse) {
		panelCollapse.getElement().setClassName(PANEL_COLLAPSE_COLLAPSE);
	}

	protected void copyValueSets() {
		cqlWorkspaceView.resetMessageDisplay();
		if(cqlWorkspaceView.getValueSetView().getQdmSelectedList() != null &&
				cqlWorkspaceView.getValueSetView().getQdmSelectedList().size() > 0){
			mat.model.GlobalCopyPasteObject gbCopyPaste = new GlobalCopyPasteObject();
			gbCopyPaste.setCopiedValueSetList(cqlWorkspaceView.getValueSetView().getQdmSelectedList());
			MatContext.get().setGlobalCopyPaste(gbCopyPaste);
			messagePanel.getSuccessMessageAlert().createAlert(VALUE_SETS_COPIED_SUCCESSFULLY);
		} else {
			messagePanel.getErrorMessageAlert().createAlert(COPY_QDM_SELECT_ATLEAST_ONE);
		}
	}

	protected void valueSetViewReleaseListBoxChanged() {
		isRetrieveButtonEnabled = true;
		cqlWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);
		previousIsApplyButtonEnabled = isApplyButtonEnabled;
		isApplyButtonEnabled = false;
		cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(isApplyButtonEnabled);
		alert508StateChanges();
	}

	protected void valueSetViewProgramListBoxChanged() {
		isRetrieveButtonEnabled = true;
		cqlWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);

		previousIsApplyButtonEnabled = isApplyButtonEnabled;
		isApplyButtonEnabled = false;
		cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(isApplyButtonEnabled);

		CQLAppliedValueSetUtility.loadReleases(cqlWorkspaceView.getValueSetView().getReleaseListBox(), cqlWorkspaceView.getValueSetView().getProgramListBox());

		alert508StateChanges();
	}

	protected void selectAllValueSets() {
		cqlWorkspaceView.resetMessageDisplay();
		if(cqlWorkspaceView.getValueSetView().getAllValueSets() != null &&
				cqlWorkspaceView.getValueSetView().getAllValueSets().size() > 0){
			cqlWorkspaceView.getValueSetView().selectAll();
			messagePanel.getSuccessMessageAlert().createAlert(VALUE_SETS_SELECTED_SUCCESSFULLY);
		}
	}

	protected void valueSetViewUpdateFromVSACClicked() {
		if (hasEditPermissions()) {
			cqlWorkspaceView.resetMessageDisplay();
			updateVSACValueSets();
			cqlWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
		}
	}

	protected void parameterAddNewClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		if (getIsPageDirty()) {
			showUnsavedChangesWarning();
		} else {
			addNewParameter();
		}

		cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setFocus(true);
	}

	protected void parameterCommentBlurEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.NONE);
		String comment = cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea().getText();
		if(validator.isCommentMoreThan250Characters(comment)){
			cqlWorkspaceView.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
		} else if(validator.doesCommentContainInvalidCharacters(comment)) {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_COMMENT_CHARACTERS());
			cqlWorkspaceView.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.ERROR);
		}
	}

	protected void createAddArgumentViewForFunctions() {
		cqlWorkspaceView.getCQLFunctionsView().createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>(), hasEditPermissions());
	}

	protected void valueSetViewSaveButtonClicked() {
		if (hasEditPermissions()) {
			MatContext.get().clearDVIMessages();
			cqlWorkspaceView.resetMessageDisplay();

			if (isModified && (modifyValueSetDTO != null)) {
				modifyValueSetOrUserDefined(isUserDefined);
			} else {
				addNewValueSet(isUserDefined);
			}
			cqlWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
		}
	}

	protected void valueSetViewPasteClicked(ClickEvent event) {
		if (hasEditPermissions()) {
			pasteValueSets();
		} else {
			event.preventDefault();
		}
	}

	protected void valueSetViewClearButtonClicked() {
		if(!cqlWorkspaceView.getValueSetView().getIsLoading()){
			cqlWorkspaceView.resetMessageDisplay();
			cqlWorkspaceView.getValueSetView().clearSelectedCheckBoxes();
		}
	}

	protected void valueSetViewUserDefinedInputChangedEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		isUserDefined = cqlWorkspaceView.getValueSetView().validateUserDefinedInput();
	}

	protected final void modifyValueSetOrUserDefined(final boolean isUserDefined) {
		if (!isUserDefined) { // Normal Available Value Set Flow
			modifyValueSet();
		} else {
			modifyUserDefinedValueSet();
		}
	}

	protected void modifyUserDefinedValueSet() {
		modifyValueSetDTO.setVersion(EMPTY_STRING);
		if ((cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText().trim().length() > 0)) {
			String originalName = cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText();
			String suffix = cqlWorkspaceView.getValueSetView().getSuffixInput().getValue();
			String usrDefDisplayName = (!originalName.isEmpty() ? originalName : EMPTY_STRING) + (!suffix.isEmpty() ? " (" + suffix + ")" : EMPTY_STRING);

			modifyValueSetList(modifyValueSetDTO);
			if (!cqlWorkspaceView.getValueSetView().checkNameInValueSetList(usrDefDisplayName,appliedValueSetTableList)) {

				CQLValueSetTransferObject object = new CQLValueSetTransferObject();
				object.setUserDefinedText(cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText());
				object.scrubForMarkUp();
				ValueSetNameInputValidator valueSetNameInputValidator = new ValueSetNameInputValidator();

				String message = valueSetNameInputValidator.validate(object);
				if (message.isEmpty()) {

					CodeListSearchDTO modifyWithDTO = new CodeListSearchDTO();
					modifyWithDTO.setName(usrDefDisplayName);
					modifyValueSetDTO.setOriginalCodeListName(originalName);
					modifyValueSetDTO.setSuffix(suffix);
					modifyValueSetDTO.setName(usrDefDisplayName);
					updateAppliedValueSetsList(null, modifyWithDTO, modifyValueSetDTO);
				} else {
					messagePanel.getErrorMessageAlert().createAlert(message);
				}
			}  else {
				appliedValueSetTableList.add(modifyValueSetDTO);
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(usrDefDisplayName));
			}
		} else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
		}
	}

	protected void modifyValueSetList(CQLQualityDataSetDTO qualityDataSetDTO) {
		for(CQLQualityDataSetDTO dataSetDTO: appliedValueSetTableList) {
			if (qualityDataSetDTO.getName().equals(dataSetDTO.getName())) {
				appliedValueSetTableList.remove(dataSetDTO);
				break;
			}
		}
	}

	protected void modifyValueSet() {
		MatValueSet modifyWithDTO = currentMatValueSet;
		if ((modifyValueSetDTO != null) && (modifyWithDTO != null)) {
			String originalName = cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText();
			String suffix = cqlWorkspaceView.getValueSetView().getSuffixInput().getValue();
			String displayName = (!originalName.isEmpty() ? originalName : EMPTY_STRING)  + (!suffix.isEmpty() ? " (" + suffix + ")" : EMPTY_STRING);

			if (modifyValueSetDTO.getVersion() == null) {
				modifyValueSetDTO.setVersion(EMPTY_STRING);
			}

			String releaseValue = cqlWorkspaceView.getValueSetView().getReleaseListBox().getSelectedValue();
			if(releaseValue == null) {
				modifyValueSetDTO.setRelease(EMPTY_STRING);
			} else if(!releaseValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
				modifyValueSetDTO.setRelease(releaseValue);
				modifyValueSetDTO.setVersion(EMPTY_STRING);
			} else {
				modifyValueSetDTO.setRelease(EMPTY_STRING);
			}

			String programValue = cqlWorkspaceView.getValueSetView().getProgramListBox().getSelectedValue();
			if(!programValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
				modifyValueSetDTO.setProgram(programValue);
			} else {
				modifyValueSetDTO.setProgram(EMPTY_STRING);
			}

			modifyValueSetList(modifyValueSetDTO);

			if (!cqlWorkspaceView.getValueSetView().checkNameInValueSetList(displayName,appliedValueSetTableList)) {

				if(!cqlWorkspaceView.getValueSetView().getSuffixInput().getValue().isEmpty()){
					modifyValueSetDTO.setSuffix(cqlWorkspaceView.getValueSetView().getSuffixInput().getValue());
					modifyValueSetDTO.setName(originalName+" ("+cqlWorkspaceView.getValueSetView().getSuffixInput().getValue()+")");
				} else {
					modifyValueSetDTO.setName(originalName);
					modifyValueSetDTO.setSuffix(null);
				}
				modifyValueSetDTO.setOriginalCodeListName(originalName);
				updateAppliedValueSetsList(modifyWithDTO, null, modifyValueSetDTO);
			} else {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(displayName));
				appliedValueSetTableList.add(modifyValueSetDTO);
			}
		} else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE());
		}
	}

	protected void leftNavBarCodesClicked(ClickEvent event) {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_CODES;
			showUnsavedChangesWarning();
			event.stopPropagation();
		} else {
			codesEvent();
			cqlWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
		}
	}

	protected void leftNavAppliedQDMClicked() {
		appliedQDMEvent();
		cqlWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
	}

	protected void leftNavParameterClickEvent(ClickEvent event) {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
			showUnsavedChangesWarning();
			event.stopPropagation();
		} else {
			parameterEvent();
		}
	}

	protected void addEventHandlerOnAceEditors() {
		cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().addKeyDownHandler(event -> definitionsAceEditorKeyDownEvent());
		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().addKeyDownHandler(event -> parameterAceEditorKeyDownEvent());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().addKeyDownHandler(event -> functionAceEditorKeyDownEvent());
		cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor().addKeyDownHandler(event -> viewCQLAceEditorKeyDownEvent());
	}

	private void definitionsAceEditorKeyDownEvent() {
		if (!cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().isReadOnly()) {
			cqlWorkspaceView.resetMessageDisplay();
			setIsPageDirty(true);
		}
	}

	private void parameterAceEditorKeyDownEvent() {
		if (!cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().isReadOnly()) {
			cqlWorkspaceView.resetMessageDisplay();
			setIsPageDirty(true);
		}
	}

	private void functionAceEditorKeyDownEvent() {
		if (!cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().isReadOnly()) {
			cqlWorkspaceView.resetMessageDisplay();
			setIsPageDirty(true);
		}
	}

	protected void keyUpEvent() {
		resetMessagesAndSetPageDirty(true);
	}

	protected void codesViewCancelButtonClicked() {
		if (hasEditPermissions()) {
			isCodeModified = false;
			cqlWorkspaceView.resetMessageDisplay();
			cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
			cqlWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
		}
	}

	protected void addNewValueSet(final boolean isUserDefinedValueSet) {
		if (!isUserDefinedValueSet) {
			addVSACCQLValueset();
		} else {
			addUserDefinedValueSet();
		}
	}

	protected void warningConfirmationNoButtonClicked() {
		messagePanel.getWarningConfirmationMessageAlert().clearAlert();
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().isNavBarClick()) {
			unsetActiveMenuItem(nextSection);
		}
		if (currentSection.equals(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getFuncNameListBox().setSelectedIndex(-1);
		} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().setSelectedIndex(-1);
		} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox().setSelectedIndex(-1);
		}
	}

	protected void addWarningConfirmationHandlers() {
		messagePanel.getWarningConfirmationYesButton().addClickHandler(event -> warningConfirmationYesButtonClicked());
		messagePanel.getWarningConfirmationNoButton().addClickHandler(event -> warningConfirmationNoButtonClicked());
	}

	protected void warningConfirmationYesButtonClicked() {
		setIsPageDirty(false);
		messagePanel.getWarningConfirmationMessageAlert().clearAlert();
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().isDoubleClick()) {
			clickEventOnListboxes();
		} else if (cqlWorkspaceView.getCQLLeftNavBarPanelView().isNavBarClick()) {
			changeSectionSelection();
		} else {
			clearViewIfDirtyNotSet();
		}
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
	}

	protected void expressionBuilderButtonClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		ExpressionBuilderHomeModal modal = new ExpressionBuilderHomeModal(this, new ExpressionBuilderModel(null));
		modal.show();
	}

	protected void deleteConfirmationNoClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		deleteConfirmationDialogBox.hide();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
	}

	protected void eraseDefinition() {
		cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
		if ((cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().getText() != null)) {
			cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().replace(EMPTY_STRING);
			setIsPageDirty(true);
		}
	}

	protected void eraseFunction() {
		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if ((cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().replace(EMPTY_STRING);
			setIsPageDirty(true);
		}
	}

	protected void eraseParameter() {
		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if (cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().getText() != null) {
			cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().replace(EMPTY_STRING);
			setIsPageDirty(true);
		}
	}

	protected void resetMessagesAndSetPageDirty(boolean isPageDirty) {
		if (hasEditPermissions()) {
			cqlWorkspaceView.resetMessageDisplay();
			setIsPageDirty(isPageDirty);
		}
	}

	protected void listBoxKeyPress(ListBox listBox, KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
			DomEvent.fireNativeEvent(Document.get().createDblClickEvent(listBox.getSelectedIndex(), 0, 0, 0, 0, false, false, false, false), listBox);
		}
	}

	protected void resetAceEditor(AceEditor aceEditor) {
		aceEditor.clearAnnotations();
		aceEditor.removeAllMarkers();
		aceEditor.setText(EMPTY_STRING);
	}

	protected void successfullyDeletedValueSet(final SaveUpdateCQLResult result) {
		if (result != null && result.getCqlErrors().isEmpty()) {
			modifyValueSetDTO = null;
			getAppliedValuesetAndCodeList();
			messagePanel.getSuccessMessageAlert().createAlert(buildRemovedSuccessfullyMessage(VALUESET, result.getCqlQualityDataSetDTO().getName()));
			messagePanel.getSuccessMessageAlert().setVisible(true);
		}
		showSearchingBusy(false);
	}

	protected void unCheckAvailableLibraryCheckBox() {
		List<CQLLibraryDataSetObject> availableLibraries = cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryList();
		for(CQLLibraryDataSetObject dataSetObject: availableLibraries) {
			dataSetObject.setSelected(false);
		}

		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryList());
		cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result, hasEditPermissions(), false);
	}

	protected void deleteConfirmationYesClicked() {
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
			deleteDefinition();
			deleteConfirmationDialogBox.hide();
		} else if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null
				&& cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId() == null) {
			deleteFunction();
			deleteConfirmationDialogBox.hide();
		} else if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId() != null) {
			deleteFunctionArgument();
			deleteConfirmationDialogBox.hide();
		} else if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
			deleteParameter();
			deleteConfirmationDialogBox.hide();
		} else if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() != null) {
			deleteInclude();
			deleteConfirmationDialogBox.hide();
		} else if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedValueSetObjId() != null) {
			checkAndDeleteValueSet();
			deleteConfirmationDialogBox.hide();
		} else if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedCodesObjId() != null) {
			deleteCode();
			deleteConfirmationDialogBox.hide();
		}
	}

	protected void clickEventOnListboxes() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		switch (currentSection) {
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getFuncNameListBox().fireEvent(new DoubleClickEvent() {
			});
		break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().fireEvent(new DoubleClickEvent() {
			});
		break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox().fireEvent(new DoubleClickEvent() {
			});
		break;
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().fireEvent(new DoubleClickEvent() {
			});
		break;
		default:
			break;
		}
	}

	protected void clearViewIfDirtyNotSet() {
		switch (currentSection) {
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			addNewFunction();
		break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			addNewParameter();
		break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			addNewDefinition();
		break;
		default:
			break;
		}
	}

	protected void addNewFunction() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		cqlWorkspaceView.getCQLFunctionsView().getFunctionArgumentList().clear();
		cqlWorkspaceView.getCQLFunctionsView().getFunctionArgNameMap().clear();
		cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
		createAddArgumentViewForFunctions();
		setIsPageDirty(false);
		if ((cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().setText(EMPTY_STRING);
		}
		if ((cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea() != null)) {
			cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setText(EMPTY_STRING);
		}

		if ((cqlWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea()!= null)) {
			cqlWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().setText(EMPTY_STRING);
		}
		cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(EMPTY_STRING);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText(EMPTY_STRING);
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex() >= 0) {
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getFuncNameListBox().setItemSelected(
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex(), false);
		}
		cqlWorkspaceView.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
	}

	protected void addNewDefinition() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
		setIsPageDirty(false);
		if ((cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().getText() != null)) {
			cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setText(EMPTY_STRING);
		}
		if ((cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea() != null)) {
			cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setText(EMPTY_STRING);
		}

		if ((cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea() != null)) {
			cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea().setText(EMPTY_STRING);
		}
		cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(EMPTY_STRING);

		cqlWorkspaceView.getCQLLeftNavBarPanelView().getSearchSuggestDefineTextBox().setText(EMPTY_STRING);
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex() >= 0) {
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox().setItemSelected(
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex(), false);
		}

		cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setEnabled(true);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setReadOnly(false);
		cqlWorkspaceView.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getSaveButton().setEnabled(true);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getInsertButton().setEnabled(true);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getTimingExpButton().setEnabled(true);

	}

	protected void changeSectionSelection() {
		cqlWorkspaceView.hideInformationDropDown();
		unsetCurrentSelection();
		setNextSelection();
	}

	protected void unsetCurrentSelection(){
		switch (currentSection) {
		case (CQLWorkSpaceConstants.CQL_COMPONENTS_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getComponentsTab().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_APPLIED_QDM):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQDM().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_CODES):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getCodesLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getGeneralInformation().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_VIEW_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getCQLLibraryEditorTab().setActive(false);
			break;
		default:
			break;
		}
	}

	protected String getWorkspaceTitle() {
		return "CQL Workspace";
	}

	protected void includesEvent() {
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
		cqlWorkspaceView.getMainFlowPanel().clear();
		cqlWorkspaceView.getIncludeView().setIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView()
				.getIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryMap()));
		cqlWorkspaceView.buildIncludesView();
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(new ArrayList<CQLLibraryDataSetObject>());
		cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
				hasEditPermissions(), true);

		cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText(EMPTY_STRING);
		cqlWorkspaceView.getIncludeView().getSearchTextBox().setText(EMPTY_STRING);
		cqlWorkspaceView.getIncludeView().setWidgetReadOnly(hasEditPermissions());
		cqlWorkspaceView.getIncludeView().setHeading(getWorkspaceTitle() + " > Includes", "IncludeSectionContainerPanel");
		focusSkipLists();
	}

	protected void viewCqlEvent() {
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getCQLLibraryEditorTab().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			UserPreferenceDTO userPreference = MatContext.get().getLoggedInUserPreference();
			cqlWorkspaceView.buildCQLFileView(hasEditPermissions() && userPreference.isFreeTextEditorEnabled(), hasEditPermissions());
			buildCQLView();
		}
		addEventHandlerOnAceEditors();
		curAceEditor = cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor();
		curAceEditor.setText("");
		cqlWorkspaceView.getCQLLibraryEditorView().setHeading(getWorkspaceTitle() + " > CQL Library Editor", "cqlViewCQL_Id");
		focusSkipLists();
	}

	protected void functionEvent() {
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
		cqlWorkspaceView.buildFunctionLibraryView();
		cqlWorkspaceView.getCQLFunctionsView().setWidgetReadOnly(hasEditPermissions());
		addEventHandlerOnAceEditors();
		cqlWorkspaceView.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(hasEditPermissions());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor();
		curAceEditor.setText("");
		cqlWorkspaceView.getCQLFunctionsView().setHeading(getWorkspaceTitle() + " > Function", "mainFuncViewVerticalPanel");
		focusSkipLists();
	}

	protected void parameterEvent() {
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
		cqlWorkspaceView.buildParameterLibraryView();
		cqlWorkspaceView.getCQLParametersView().setWidgetReadOnly(hasEditPermissions());
		addEventHandlerOnAceEditors();
		cqlWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(hasEditPermissions());
		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = cqlWorkspaceView.getCQLParametersView().getParameterAceEditor();
		curAceEditor.setText("");
		cqlWorkspaceView.getCQLParametersView().setHeading(getWorkspaceTitle() + " > Parameter", "mainParamViewVerticalPanel");
		focusSkipLists();
	}

	protected void definitionEvent() {
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
		cqlWorkspaceView.buildDefinitionLibraryView();
		cqlWorkspaceView.getCQLDefinitionsView().setWidgetReadOnly(hasEditPermissions());
		addEventHandlerOnAceEditors();
		cqlWorkspaceView.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(hasEditPermissions());
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor();
		curAceEditor.setText("");
		cqlWorkspaceView.getCQLDefinitionsView().setHeading(getWorkspaceTitle() + " > Definition", "mainDefViewVerticalPanel");
		focusSkipLists();
	}

	protected void appliedQDMEvent() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.hideInformationDropDown();
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQDM().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			cqlWorkspaceView.getValueSetView().getPasteButton().setEnabled(hasEditPermissions());
			buildAppliedQDMTable();
		}

		loadProgramsAndReleases();
		cqlWorkspaceView.getValueSetView().setHeading(getWorkspaceTitle() + " > Value Sets", "subQDMAPPliedListContainerPanel");
		focusSkipLists();
	}

	protected void addNewArgumentClicked() {
		cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
		CQLFunctionArgument addNewFunctionArgument = new CQLFunctionArgument();
		AddFunctionArgumentDialogBox.showArgumentDialogBox(addNewFunctionArgument, false,cqlWorkspaceView.getCQLFunctionsView(), messagePanel, hasEditPermissions(), getCurrentModelType());
		setIsPageDirty(true);
		cqlWorkspaceView.getCQLFunctionsView().getAddNewArgument().setFocus(true);
	}

	protected void shiftFocusToCodeSearchPanel(SaveUpdateCQLResult result) {
		cqlWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
		cqlWorkspaceView.getCodesView().getApplyButton().setEnabled(!result.isSuccess());
	}

	protected void parameterShowEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		showCompleteCQL(cqlWorkspaceView.getCQLParametersView().getViewCQLAceEditor());
	}

	protected void parameterHideEvent() {
		resetAceEditor(cqlWorkspaceView.getCQLParametersView().getViewCQLAceEditor());
		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
	}

	protected void parameterSaveClicked() {
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
		if (hasEditPermissions()) {
			addAndModifyParameters();
			cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setFocus(true);
		}
	}

	protected void parameterDeleteClicked() {
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
		deleteConfirmationDialogBox.getMessageAlert().createAlert(buildSelectedToDeleteWithConfirmationMessage(PARAMETER, cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().getValue()));
		deleteConfirmationDialogBox.show();
	}

	protected void parameterEraseClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
		eraseParameter();
		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().focus();
	}

	protected void functionShowEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		showCompleteCQL(cqlWorkspaceView.getCQLFunctionsView().getViewCQLAceEditor());
	}

	protected void functionHideEvent() {
		resetAceEditor(cqlWorkspaceView.getCQLFunctionsView().getViewCQLAceEditor());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
	}

	protected void functionSaveClicked() {
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());

		if (hasEditPermissions()) {
			addAndModifyFunction();
			cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setFocus(true);
		}
	}

	protected void functionEraseClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		eraseFunction();
		cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().focus();
	}

	protected void definitionHideEvent() {
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
		resetAceEditor(cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor());
	}

	protected void definitionShowEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		showCompleteCQL(cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor());
	}

	protected void definitionSaveClicked() {
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse());
		if (hasEditPermissions()) {
			addAndModifyDefintions();
			cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
			cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
			cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setFocus(true);
		}
	}

	protected void definitionEraseClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse());
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
		cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
		eraseDefinition();
		cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().focus();
	}

	protected void definitionAddNewClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
		cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();

		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse());
		if (getIsPageDirty()) {
			showUnsavedChangesWarning();
		} else {
			addNewDefinition();
		}
		cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setFocus(true);
	}

	protected void definitionCommentBlurEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.NONE);
		String comment = cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea().getText();
		if (validator.isCommentMoreThan250Characters(comment)) {
			cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
		} else if(validator.doesCommentContainInvalidCharacters(comment)){
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_COMMENT_CHARACTERS());
			cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
		}
	}

	protected void generalCommentBlurEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCqlGeneralInformationView().getCommentsGroup().setValidationState(ValidationState.NONE);
		String comment = cqlWorkspaceView.getCqlGeneralInformationView().getCommentsTextBox().getText();
		if (validator.isCommentMoreThan2500Characters(comment)) {
			cqlWorkspaceView.getCqlGeneralInformationView().getCommentsGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(CQLGeneralInformationUtility.COMMENT_LENGTH_ERROR);
		} else if(validator.doesCommentContainInvalidCharacters(comment)){
			cqlWorkspaceView.getCqlGeneralInformationView().getCommentsGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_COMMENT_CHARACTERS());
		}
	}

	protected void codesEvent() {
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_CODES;
			showUnsavedChangesWarning();
		} else {
			boolean isEditable = hasEditPermissions();
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getCodesLibrary().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_CODES;
			cqlWorkspaceView.buildCodes();
			cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, isEditable);
			cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
			cqlWorkspaceView.getCodesView().setWidgetsReadOnly(isEditable);
			cqlWorkspaceView.getCodesView().getPasteButton().setEnabled(isEditable);
		}
		cqlWorkspaceView.getCodesView().setHeading(getWorkspaceTitle() + " > Codes", "codesContainerPanel");
		focusSkipLists();
	}

	private void generalInfoEvent() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlWorkspaceView.hideInformationDropDown();
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getGeneralInformation().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			cqlWorkspaceView.buildGeneralInformation(hasEditPermissions());

			buildOrClearErrorPanel();

			cqlWorkspaceView.getCqlGeneralInformationView().getLibraryNameTextBox().setText(cqlLibraryName);
			cqlWorkspaceView.getCqlGeneralInformationView().getCommentsTextBox().setText(cqlLibraryComment);
		}
		cqlWorkspaceView.setGeneralInfoHeading();
		cqlWorkspaceView.getCqlGeneralInformationView().getCommentsTextBox().setCursorPos(0);
	}

	protected void buildOrClearErrorPanel() {
		if (hasEditPermissions()) {
			boolean isValidQDMVersion = cqlWorkspaceView.getCQLLeftNavBarPanelView().checkForIncludedLibrariesQDMVersion(isStandaloneCQLLibrary());

			List<String> errorMessageList = new ArrayList<>();
			if (isLibraryNameExists) {
				errorMessageList.add(MessageDelegate.DUPLICATE_LIBRARY_NAME);
			}
			if(!isValidQDMVersion){
				errorMessageList.add(INVALID_QDM_VERSION_IN_INCLUDES);
			}

			displayErrorsOrWarnings(errorMessageList);
		}
	}

	private void displayErrorsOrWarnings(List<String> errorMessageList) {
		if (errorMessageList.isEmpty()) {
			messagePanel.getErrorMessageAlert().clearAlert();
			checkAndDisplayLibraryNameWarning();
		} else {
			messagePanel.getErrorMessageAlert().createAlert(errorMessageList);
		}
	}

	private void checkAndDisplayLibraryNameWarning() {
		if (cqlLibraryName.length() > AbstractCQLWorkspacePresenter.CQL_LIBRARY_NAME_WARNING_LENGTH) {
			messagePanel.getWarningMessageAlert().createAlert(CQL_LIBRARY_NAME_WARNING_MESSAGE);
		}
	}

	protected void leftNavDefinitionClicked(ClickEvent event) {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
			showUnsavedChangesWarning();
			event.stopPropagation();
		} else {
			definitionEvent();
		}
	}

	protected void leftNavFunctionClicked(ClickEvent event) {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
			showUnsavedChangesWarning();
			event.stopPropagation();
		} else {
			functionEvent();
		}
	}

	protected void leftNavIncludesLibraryClicked(ClickEvent event) {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
			showUnsavedChangesWarning();
			event.stopPropagation();
		} else {
			includesEvent();
		}
	}

	protected void includeViewEraseButtonClicked() {
		setIsPageDirty(false);
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		clearAlias();
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
			messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
		} else {
			messagePanel.getWarningMessageAlert().clearAlert();
		}
		cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
	}

	protected void includeViewCloseButtonClicked() {
		currentIncludeLibrarySetId = null;
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText(EMPTY_STRING);
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().setItemSelected(
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(),
					false);
		}
		cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText(EMPTY_STRING);
		cqlWorkspaceView.buildIncludesView();
		SaveCQLLibraryResult cqlLibrarySearchModel = new SaveCQLLibraryResult();
		cqlLibrarySearchModel.setCqlLibraryDataSetObjects(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryList());
		cqlWorkspaceView.getIncludeView().setIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryMap()));
		cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(cqlLibrarySearchModel, hasEditPermissions(), false);
		cqlWorkspaceView.getIncludeView().setWidgetReadOnly(hasEditPermissions());

		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
			messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
		} else {
			messagePanel.getWarningMessageAlert().clearAlert();
		}
		cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
	}

	protected void addNewFunctionClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		if (getIsPageDirty()) {
			showUnsavedChangesWarning();
		} else {
			addNewFunction();
		}

		cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setFocus(true);
	}

	private void viewCQLAceEditorKeyDownEvent() {
		if (!cqlWorkspaceView.getCQLLibraryEditorView().getCqlAceEditor().isReadOnly()) {
			cqlWorkspaceView.resetMessageDisplay();
			setIsPageDirty(true);
		}
	}

	protected void functionCommentBlurEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.NONE);
		String comment = cqlWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().getText();
		if (validator.isCommentMoreThan250Characters(comment)) {
			cqlWorkspaceView.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
		} else if(validator.doesCommentContainInvalidCharacters(comment)){
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_COMMENT_CHARACTERS());
			cqlWorkspaceView.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
		}
	}

	protected void includesViewSaveClicked() {
		if (hasEditPermissions()) {
			addIncludeLibraryInCQLLookUp();
			cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
		}
	}

	protected void aliasNameChangeHandler() {
		setIsPageDirty(true);
	}

	protected void includeFocusPanelKeyDown(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			cqlWorkspaceView.getIncludeView().getSearchButton().click();
		}
	}

	protected void leftNavCQLLibraryEditorViewEvent() {
		cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
		viewCqlEvent();
	}

	protected void loadProgramsAndReleases() {
		CQLAppliedValueSetUtility.loadProgramsAndReleases(cqlWorkspaceView.getValueSetView().getProgramListBox(), cqlWorkspaceView.getValueSetView().getReleaseListBox());
	}

	protected void buildAppliedQDMTable() {
		cqlWorkspaceView.buildAppliedQDM();
		boolean isEditable = hasEditPermissions();
		for (CQLQualityDataSetDTO valuset : cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQdmTableList()) {
			valuset.setUsed(true);
		}
		cqlWorkspaceView.getValueSetView().buildAppliedValueSetCellTable(cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQdmTableList(), isEditable);
		cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
		cqlWorkspaceView.getValueSetView().setWidgetsReadOnly(isEditable);
	}

	public void setNextSelection() {
		switch (nextSection) {
		case (CQLWorkSpaceConstants.CQL_COMPONENTS_MENU):
			currentSection = nextSection;
			componentsEvent();
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getComponentsCollapse().getElement().setClassName(PANEL_COLLAPSE_IN);
			break;
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			currentSection = nextSection;
			includesEvent();
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesCollapse().getElement().setClassName(PANEL_COLLAPSE_IN);
			break;
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			currentSection = nextSection;
			functionEvent();
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionCollapse().getElement().setClassName(PANEL_COLLAPSE_IN);
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			currentSection = nextSection;
			parameterEvent();
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getParamCollapse().getElement().setClassName(PANEL_COLLAPSE_IN);
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			currentSection = nextSection;
			definitionEvent();
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineCollapse().getElement().setClassName(PANEL_COLLAPSE_IN);
			break;
		case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
			currentSection = nextSection;
			generalInfoEvent();
			break;
		case (CQLWorkSpaceConstants.CQL_VIEW_MENU):
			currentSection = nextSection;
			viewCqlEvent();
			break;
		case (CQLWorkSpaceConstants.CQL_APPLIED_QDM):
			currentSection = nextSection;
			appliedQDMEvent();
			break;
		case (CQLWorkSpaceConstants.CQL_CODES):
			currentSection = nextSection;
			codesEvent();
			break;
		default:
			break;
		}
	}

	protected void unsetActiveMenuItem(String menuClickedBefore) {
		if (!getIsPageDirty()) {
			cqlWorkspaceView.resetMessageDisplay();
			if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_GENERAL_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getGeneralInformation().setActive(false);
			} else if(menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_COMPONENTS_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getComponentsTab().setActive(false);
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getComponentsListBox().setSelectedIndex(-1);
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getComponentsCollapse().getElement().setClassName(PANEL_COLLAPSE_COLLAPSE);
				((CQLMeasureWorkSpaceView)cqlWorkspaceView).getComponentView().clearAceEditor();
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterLibrary().setActive(false);
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().setSelectedIndex(-1);
				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getParamCollapse().getElement().getClassName().equalsIgnoreCase(PANEL_COLLAPSE_IN)) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getParamCollapse().getElement().setClassName(PANEL_COLLAPSE_COLLAPSE);
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox().setSelectedIndex(-1);
				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineCollapse().getElement().getClassName().equalsIgnoreCase(PANEL_COLLAPSE_IN)) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineCollapse().getElement().setClassName(PANEL_COLLAPSE_COLLAPSE);
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionLibrary().setActive(false);
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getFuncNameListBox().setSelectedIndex(-1);
				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionCollapse().getElement().getClassName().equalsIgnoreCase(PANEL_COLLAPSE_IN)) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionCollapse().getElement().setClassName(PANEL_COLLAPSE_COLLAPSE);
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEW_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getCQLLibraryEditorTab().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_APPLIED_QDM)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQDM().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_CODES)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getCodesLibrary().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_INCLUDES_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesLibrary().setActive(false);
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().setSelectedIndex(-1);
				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesCollapse().getElement().getClassName().equalsIgnoreCase(PANEL_COLLAPSE_IN)) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesCollapse().getElement().setClassName(PANEL_COLLAPSE_COLLAPSE);
				}
			}
		}
	}

	protected void checkIfLibraryNameExistsAndLoadGeneralInfo() {
		if (hasEditPermissions() && StringUtility.isNotBlank(this.cqlLibraryName)) {
			MatContext.get().getMeasureService().checkIfLibraryNameExists(this.cqlLibraryName, this.setId, new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, "Error in measureService.checkIfLibraryNameExists. Error message: " + caught.getMessage(), caught);
					showSearchingBusy(false);
					Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}

				@Override
				public void onSuccess(Boolean result) {
					isLibraryNameExists = result;
					generalInfoEvent();
				}
			});

		} else {
			generalInfoEvent();
		}

	}

	protected void displayMsgAndResetDirtyPostSave(String name) {
		isLibraryNameExists = false;
		setIsPageDirty(false);
		String successMsg = name +  " general information successfully updated.";
		if (cqlLibraryName.length() > AbstractCQLWorkspacePresenter.CQL_LIBRARY_NAME_WARNING_LENGTH) {
			List<String> messageList = new ArrayList<>();
			messageList.add(successMsg);
			messageList.add(AbstractCQLWorkspacePresenter.CQL_LIBRARY_NAME_WARNING_MESSAGE);
			messagePanel.getWarningMessageAlert().createAlert(messageList);
		} else {
			messagePanel.getSuccessMessageAlert().createAlert(successMsg);
		}
	}

	protected String convertMessage(final int id) {
		String message;
		switch (id) {
		case VsacApiResult.UMLS_NOT_LOGGEDIN:
			message = MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN();
			break;
		case VsacApiResult.OID_REQUIRED:
			message = MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED();
			break;
		case VsacApiResult.CODE_URL_REQUIRED:
			message = MatContext.get().getMessageDelegate().getUMLS_CODE_IDENTIFIER_REQUIRED();
			break;
		case VsacApiResult.VSAC_REQUEST_TIMEOUT:
			message = MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_TIMEOUT();
			break;
		case VsacApiResult.VSAC_UNAUTHORIZED_ERROR:
			message = MessageDelegate.VSAC_UNAUTHORIZED_ERROR;
			Mat.hideUMLSActive(true);
			MatContext.get().setUMLSLoggedIn(false);
			break;
		default:
			message = MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED();
		}
		return message;
	}

}
