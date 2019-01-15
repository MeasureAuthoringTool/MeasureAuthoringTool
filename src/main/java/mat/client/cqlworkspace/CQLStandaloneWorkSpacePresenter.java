package mat.client.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.CqlComposerPresenter;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.clause.event.QDSElementCreatedEvent;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.cqlworkspace.codes.CQLCodesView;
import mat.client.cqlworkspace.codes.CQLCodesView.Delegator;
import mat.client.cqlworkspace.functions.CQLFunctionsView.Observer;
import mat.client.cqlworkspace.generalinformation.CQLGeneralInformationUtility;
import mat.client.cqlworkspace.includedlibrary.CQLIncludeLibraryView;
import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility;
import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetView;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.expressionbuilder.modal.ExpressionBuilderModal;
import mat.client.measure.service.CQLLibraryServiceAsync;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.ValueSetNameInputValidator;
import mat.client.umls.service.VsacApiResult;
import mat.model.CQLValueSetTransferObject;
import mat.model.CodeListSearchDTO;
import mat.model.GlobalCopyPasteObject;
import mat.model.MatCodeTransferObject;
import mat.model.MatValueSet;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CQLError;
import mat.shared.CQLIdentifierObject;
import mat.shared.ConstantMessages;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.StringUtility;
import mat.shared.cql.error.InvalidLibraryException;

public class CQLStandaloneWorkSpacePresenter extends AbstractCQLWorkspacePresenter implements MatPresenter {
	private SimplePanel emptyWidget = new SimplePanel();
	private boolean isCQLWorkSpaceLoaded = false;
	private String cqlLibraryName;
	private static CQLWorkspaceView cqlWorkspaceView;
	private final CQLLibraryServiceAsync cqlService = MatContext.get().getCQLLibraryService();
	private static final String ERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR  = "Invalid Library Alias. Must be unique, start with an alpha-character or underscore followed by an alpha-numeric character(s) or underscore(s), and must not contain spaces.";

	
	public CQLStandaloneWorkSpacePresenter(final CQLWorkspaceView srchDisplay) {
		cqlWorkspaceView = srchDisplay;
		emptyWidget.add(new Label("No CQL Library Selected"));
		addEventHandlers();
	}

	private void addViewCQLEventHandlers() {
		cqlWorkspaceView.getViewCQLView().getExportErrorFile().addClickHandler(event -> viewCQLExportErrorFileClicked());
	}

	private void addEventHandlers() {
		MatContext.get().getEventBus().addHandler(CQLLibrarySelectedEvent.TYPE, new CQLLibrarySelectedEvent.Handler() {
			@Override
			public void onLibrarySelected(CQLLibrarySelectedEvent event) {
				isCQLWorkSpaceLoaded = false;
				if (event.getCqlLibraryId() != null) {
					isCQLWorkSpaceLoaded = true;
					logRecentActivity();
				} else {
					displayEmpty();
				}
			}
		});
		
		getDeleteConfirmationDialogBoxYesButton().addClickHandler(event -> deleteConfirmationDialogBoxYesClicked());
		getDeleteConfirmationDialogBoxNoButton().addClickHandler(event -> deleteConfirmationDialogBoxNoClicked());

		ClickHandler cHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().detach();
				cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().detach();
				cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().detach();
			}
		};
		cqlWorkspaceView.getMainPanel().addDomHandler(cHandler, ClickEvent.getType());

		addGeneralInfoEventHandlers();
		addValueSetEventHandlers();
		addCodeSearchPanelHandlers();
		addIncludeCQLLibraryHandlers();
		addParameterEventHandlers();
		addDefineEventHandlers();
		addFunctionEventHandlers();
		addEventHandlerOnAceEditors();
		addEventHandlersOnContextRadioButtons();
		addWarningAlertHandlers();
		addViewCQLEventHandlers();
	}

	private void onModifyValueSet(CQLQualityDataSetDTO result, boolean isUserDefined) {
		String oid = isUserDefined ? EMPTY_STRING : result.getOid();
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
		previousIsProgramListBoxEnabled = isProgramListBoxEnabled;
		CQLAppliedValueSetUtility.setProgramsAndReleases(result.getProgram(), result.getRelease(), cqlWorkspaceView.getValueSetView());
		isProgramListBoxEnabled = true;
	}

	private void onModifyCode(CQLCode cqlCode) {
		CQLCodesView codesView = cqlWorkspaceView.getCodesView();
		codesView.getCodeSearchInput().setEnabled(false);
		codesView.getRetrieveFromVSACButton().setEnabled(false);
		codesView.getCodeSearchInput().setValue(cqlCode.getCodeIdentifier());
		codesView.getSuffixTextBox().setValue(cqlCode.getSuffix());
		codesView.getCodeDescriptorInput().setValue(cqlCode.getName());
		codesView.getCodeInput().setValue(cqlCode.getCodeOID());
		codesView.getCodeSystemInput().setValue(cqlCode.getCodeSystemName());
		codesView.getCodeSystemVersionInput().setValue(cqlCode.getCodeSystemVersion());
		codesView.setCodeSystemOid(cqlCode.getCodeSystemOID());
		codesView.getIncludeCodeSystemVersionCheckBox().setValue(cqlCode.isIsCodeSystemVersionIncluded());
		codesView.getSaveButton().setEnabled(true);
	}


	private void deleteValueSet(String toBeDeletedValueSetId) {
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().deleteValueSet(toBeDeletedValueSetId, MatContext.get().getCurrentCQLLibraryId(), new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onFailure(final Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);
			}

			@Override
			public void onSuccess(final SaveUpdateCQLResult result) {
				if (result != null && result.getCqlErrors().isEmpty()) {
					modifyValueSetDTO = null;
					getAppliedValueSetList();
					messagePanel.getSuccessMessageAlert().createAlert(buildRemovedSuccessfullyMessage(VALUESET, result.getCqlQualityDataSetDTO().getName()));
					messagePanel.getSuccessMessageAlert().setVisible(true);
				}
				getUsedArtifacts();
				showSearchingBusy(false);
			}
		});
	}

	private void getAppliedValueSetList() {
		showSearchingBusy(true);
		String cqlLibraryId = MatContext.get().getCurrentCQLLibraryId();
		if ((cqlLibraryId != null) && !cqlLibraryId.equals(EMPTY_STRING)) {
			MatContext.get().getLibraryService().getCQLData(cqlLibraryId, new AsyncCallback<SaveUpdateCQLResult>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					showSearchingBusy(false);

				}

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					setAppliedValueSetListInTable(result.getCqlModel().getAllValueSetAndCodeList());
					showSearchingBusy(false);
				}
			});
		}

	}

	private void addParameterEventHandlers() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().addKeyPressHandler(event -> leftNavParameterNameListBoxKeyPressEvent(event));
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().addDoubleClickHandler(event -> leftNavParameterNameListBoxDoubleClickEvent(event));
		cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse().addShowHandler(event -> parameterViewShowEvent());
		cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse().addHideHandler(event -> parameterViewHideEvent());
		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getSaveButton().addClickHandler(event -> parameterViewSaveButtonClicked());
		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getEraseButton().addClickHandler(event -> parameterViewEraseButtonClicked());
		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().addClickHandler(event -> parameterViewDeleteButtonClicked());
		cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().addKeyUpHandler(event -> keyUpEvent());
		cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea().addKeyUpHandler(event -> keyUpEvent());
		cqlWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton().addClickHandler(event -> parameterViewAddNewClicked());
		cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea().addBlurHandler(event -> parameterCommentBlurEvent());
	}

	private void showCompleteCQL(final AceEditor aceEditor) {
		MatContext.get().getCQLLibraryService().getLibraryCQLFileData(MatContext.get().getCurrentCQLLibraryId(), new AsyncCallback<SaveUpdateCQLResult>() {
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				String formattedName = result.getCqlModel().getFormattedName();
				if (result.isSuccess()) {
					if ((result.getCqlString() != null) && !result.getCqlString().isEmpty()) {
						aceEditor.clearAnnotations();
						aceEditor.redisplay();
						
						if(result.getLibraryNameErrorsMap().get(formattedName) != null) {
							for (CQLError error : result.getLibraryNameErrorsMap().get(formattedName)) {
								int line = error.getErrorInLine();
								int column = error.getErrorAtOffeset();
								aceEditor.addAnnotation(line - 1, column, error.getErrorMessage(), AceAnnotationType.ERROR);
							}
						}
						

						if(result.getLibraryNameWarningsMap().get(formattedName) != null) {
							for (CQLError warning : result.getLibraryNameWarningsMap().get(formattedName)) {
								int line = warning.getErrorInLine(); 
								int column = warning.getErrorAtOffeset();
								aceEditor.addAnnotation(line - 1, column, warning.getErrorMessage(), AceAnnotationType.WARNING);
							}	
						}
												
						aceEditor.setText(result.getCqlString());
						aceEditor.setAnnotations();
						aceEditor.gotoLine(1);
						aceEditor.redisplay();
					}

				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
	}

	private void resetAceEditor(AceEditor aceEditor) {
		aceEditor.clearAnnotations();
		aceEditor.removeAllMarkers();
		aceEditor.setText(EMPTY_STRING);
	}

	public void resetViewCQLCollapsiblePanel(PanelCollapse panelCollapse) {
		panelCollapse.getElement().setClassName("panel-collapse collapse");
	}

	private void addNewParameter() {
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

		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			cqlWorkspaceView.getCQLParametersView()
			.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
			cqlWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}

		cqlWorkspaceView.getCQLLeftNavBarPanelView().getSearchSuggestParamTextBox().setText(EMPTY_STRING);
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex() >= 0) {
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().setItemSelected(
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(), false);
		}

		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
	}

	private void addDefineEventHandlers() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox().addKeyPressHandler(event -> cqlLeftNavBarDefineNameListBoxKeyPressed(event));
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox().addDoubleClickHandler(event -> cqlLeftNavBarDefineNameListBoxDoubleClickEvent(event));
		cqlWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse().addShowHandler(event -> cqlDefinitionsShowEvent());
		cqlWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse().addHideHandler(event -> cqlDefinitionsHideEvent());
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getInsertButton().addClickHandler(event -> buildInsertPopUp());
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getSaveButton().addClickHandler(event -> definitionSaveButtonClicked());
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getEraseButton().addClickHandler(event -> definitionEraseButtonClicked());
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().addClickHandler(event -> definitionDeleteButtonClicked());
		cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().addKeyUpHandler(event -> keyUpEvent());
		cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea().addKeyUpHandler(event -> keyUpEvent());
		cqlWorkspaceView.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().addClickHandler(event -> definitionsAddNewClicked());
		cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea().addBlurHandler(event -> definitionCommentBlurEvent());
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getExpressionBuilderButton().addClickHandler(event -> expresionBuilderButtonClicked());
	}
	
	private void expresionBuilderButtonClicked() {
		ExpressionBuilderModal modal = new ExpressionBuilderModal(cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor());
		modal.show();
	}

	private void addNewDefinition() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
		setIsPageDirty(false);
		if ((cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().getText() != null)) {
			cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setText(EMPTY_STRING);
		}
		if ((cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea() != null)) {
			cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setText(EMPTY_STRING);
		}

		if ((cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea()!= null)) {
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
		cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().setEnabled(true);
		cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setEnabled(true);
		cqlWorkspaceView.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getSaveButton().setEnabled(true);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getInsertButton().setEnabled(true);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getTimingExpButton().setEnabled(true);
		cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
		cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);
	}

	private void addFunctionEventHandlers() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getFuncNameListBox().addKeyPressHandler(event -> leftNavBarFuncNameListBoxKeyPressedEvent(event));
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getFuncNameListBox().addDoubleClickHandler(event -> leftNavBarFuncNameListBoxDoubleClickedEvent(event));
		cqlWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse().addShowHandler(event -> cqlFunctionsViewShowEvent());
		cqlWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse().addHideHandler(event -> cqlFunctionsViewHideEvent());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getInsertButton().addClickHandler(event -> functionsViewInsertButtonClicked());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getSaveButton().addClickHandler(event -> functionsViewSaveButtonClicked());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getEraseButton().addClickHandler(event -> functionsViewEraseButtonClicked());
		cqlWorkspaceView.getCQLFunctionsView().getAddNewArgument().addClickHandler(event -> functionsViewAddNewArgumentClicked());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().addClickHandler(event -> functionsViewDeleteButtonClicked());
		cqlWorkspaceView.getCQLFunctionsView().setObserver(new Observer() {
			@Override
			public void onModifyClicked(CQLFunctionArgument result) {
				setIsPageDirty(true);
				cqlWorkspaceView.resetMessageDisplay();
				if (result.getArgumentType().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
					getAttributesForDataType(result);
				} else {
					AddFunctionArgumentDialogBox.showArgumentDialogBox(result, true,cqlWorkspaceView.getCQLFunctionsView(), messagePanel,MatContext.get().getLibraryLockService().checkForEditPermission());
				}

			}

			@Override
			public void onDeleteClicked(CQLFunctionArgument result, int index) {
				cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(result.getId());
				cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(result.getArgumentName());
				deleteConfirmationDialogBox.getMessageAlert().createAlert(buildSelectedToDeleteWithConfirmationMessage(ARGUMENT, result.getArgumentName()));
				deleteConfirmationDialogBox.show();
				cqlWorkspaceView.getCQLFunctionsView().getMainFunctionVerticalPanel().setFocus(true);
			}
		});

		cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().addKeyUpHandler(event -> keyUpEvent());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().addKeyUpHandler(event -> keyUpEvent());
		cqlWorkspaceView.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().addClickHandler(event -> functionsViewAddNewClicked());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().addBlurHandler(event -> functionCommentBlurEvent());
	}

	private void addNewFunction() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		cqlWorkspaceView.getCQLFunctionsView().getFunctionArgumentList().clear();
		cqlWorkspaceView.getCQLFunctionsView().getFunctionArgNameMap().clear();
		cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
		((CQLStandaloneWorkSpaceView)cqlWorkspaceView).createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>());
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
		cqlWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(true);
		cqlWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
		cqlWorkspaceView.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
	}

	private void addWarningAlertHandlers() {
		messagePanel.getWarningConfirmationYesButton().addClickHandler(event -> warningConfirmationYesButtonClicked());
		messagePanel.getWarningConfirmationNoButton().addClickHandler(event -> warningConfirmationNoButtonClicked());
	}

	private void addGeneralInfoEventHandlers() {
		cqlWorkspaceView.getCqlGeneralInformationView().getSaveButton().addClickHandler(event -> saveCQLGeneralInfo());
		cqlWorkspaceView.getCqlGeneralInformationView().getLibraryNameValue().addKeyUpHandler(event -> resetMessagesAndSetPageDirty(true));
		cqlWorkspaceView.getCqlGeneralInformationView().getComments().addValueChangeHandler(event -> resetMessagesAndSetPageDirty(true));
	}

	private void saveCQLGeneralInfo() {
		resetMessagesAndSetPageDirty(false);
		String libraryName = cqlWorkspaceView.getCqlGeneralInformationView().getLibraryNameValue().getText().trim();
		String commentContent = cqlWorkspaceView.getCqlGeneralInformationView().getComments().getText().trim();

		boolean isValid = CQLGeneralInformationUtility.validateGeneralInformationSection(cqlWorkspaceView.getCqlGeneralInformationView(), messagePanel, libraryName, commentContent);
		if(isValid) {
			saveCQLGeneralInformation(libraryName, commentContent);
		}
	}

	private void addEventHandlerOnAceEditors() {
		cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().addKeyDownHandler(event -> definitionsAceEditorKeyDownEvent());
		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().addKeyDownHandler(event -> parameterAceEditorKeyDownEvent());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().addKeyDownHandler(event -> functionAceEditorKeyDownEvent());
	}
	
	private void addEventHandlersOnContextRadioButtons() {
		cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().addValueChangeHandler(event -> cqlDefinitionsViewContextDefinePATRadioBtnValueChangedEvent());
		cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn().addValueChangeHandler(event -> cqlDefinitionsViewContextDefinePOPRadioBtnValueChangedEvent());
		cqlWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().addValueChangeHandler(event -> cqlFunctionViewContextFuncPATRadioBtnValueChangedEvent());
		cqlWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn().addValueChangeHandler(event -> cqlFunctionViewContextFuncPOPRadioBtnValueChangedEvent());
	}

	private void addIncludeCQLLibraryHandlers() {
		cqlWorkspaceView.getIncludeView().getSaveModifyButton().addClickHandler(event -> includeViewSaveModifyClicked());
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().addKeyPressHandler(event -> cqlLeftNavParIncludesNameListBoxKeyPressedEvent(event));
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().addDoubleClickHandler(event -> cqlLeftNavBarIncludesNameListBoxDoubleClickEvent(event));
		cqlWorkspaceView.getIncludeView().getSearchButton().addClickHandler(event -> includeViewSearchButtonClicked());
		cqlWorkspaceView.getIncludeView().getFocusPanel().addKeyDownHandler(event -> includesViewFocusPanelKeyDownEvent(event));
		cqlWorkspaceView.getIncludeView().getIncludesButtonBar().getSaveButton().addClickHandler(event -> includesViewSaveButtonClicked());
		cqlWorkspaceView.getIncludeView().getDeleteButton().addClickHandler(event -> includesViewDeleteButtonClicked());
		cqlWorkspaceView.getIncludeView().getCloseButton().addClickHandler(event -> includesViewCloseButtonClicked());
		cqlWorkspaceView.getIncludeView().getEraseButton().addClickHandler(event -> includesViewEraseButtonClicked());
		cqlWorkspaceView.getIncludeView().setObserver(new CQLIncludeLibraryView.Observer() {

			@Override
			public void onCheckBoxClicked(CQLLibraryDataSetObject result) {
				MatContext.get().getCQLLibraryService().findCQLLibraryByID(result.getId(),
						new AsyncCallback<CQLLibraryDataSetObject>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}

					@Override
					public void onSuccess(CQLLibraryDataSetObject result) {
						cqlWorkspaceView.getIncludeView().getViewCQLEditor().setText(result.getCqlText());
					}
				});
			}
		});
	}

	protected void addAndModifyFunction() {
		cqlWorkspaceView.resetMessageDisplay();
		final String functionName = cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().getText();
		String functionBody = cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().getText();
		String functionComment = cqlWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().getText();
		String funcContext = EMPTY_STRING;
		if (cqlWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().getValue()) {
			funcContext = "Patient";
		} else {
			funcContext = "Population";
		}

		boolean isValidFunctionName = isValidExpressionName(functionName);
		if (isValidFunctionName) {
			if (validator.hasSpecialCharacter(functionName.trim())) {
				cqlWorkspaceView.getCQLFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
				messagePanel.getErrorMessageAlert().createAlert(ERROR_FUNCTION_NAME_NO_SPECIAL_CHAR);
				cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setText(functionName.trim());	
			} else if(validator.isCommentMoreThan250Characters(functionComment)) {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				cqlWorkspaceView.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
			} else if(validator.doesCommentContainInvalidCharacters(functionComment)){
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_COMMENT_CHARACTERS());
				cqlWorkspaceView.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
			} else {
				CQLFunctions function = new CQLFunctions();
				function.setLogic(functionBody);
				function.setName(functionName);
				function.setCommentString(functionComment);
				function.setArgumentList(cqlWorkspaceView.getCQLFunctionsView().getFunctionArgumentList());
				function.setContext(funcContext);
				CQLFunctions toBeModifiedParamObj = null;

				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
					toBeModifiedParamObj = cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionMap()
							.get(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().saveAndModifyFunctions(
						MatContext.get().getCurrentCQLLibraryId(), toBeModifiedParamObj, function,
						cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewFunctions(),
						isFormattable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
								cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
								cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
								messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										cqlWorkspaceView.getCQLLeftNavBarPanelView()
										.setViewFunctions(result.getCqlModel().getCqlFunctions());
										MatContext.get()
										.setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
										cqlWorkspaceView.getCQLLeftNavBarPanelView()
										.setCurrentSelectedFunctionObjId(result.getFunction().getId());
										cqlWorkspaceView.getCQLLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
										cqlWorkspaceView.getCQLLeftNavBarPanelView().updateFunctionMap();
										messagePanel.getErrorMessageAlert().clearAlert();
										messagePanel.getSuccessMessageAlert().setVisible(true);
										cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea()
										.setText(result.getFunction().getName());
										cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor()
										.setText(result.getFunction().getLogic());
										setIsPageDirty(false);
										cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor()
										.clearAnnotations();
										cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor()
										.removeAllMarkers();
										if (SharedCQLWorkspaceUtility.validateCQLArtifact(result, cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor(), messagePanel, FUNCTION, functionName.trim())) {
											cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(EMPTY_STRING);
											cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
										} else if (!result.isDatatypeUsedCorrectly()) {
											if(result.isValidCQLWhileSavingExpression()){
												cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(result.getFunction().getReturnType());
												cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is " + result.getFunction().getReturnType());
											} else {
												cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(EMPTY_STRING);
												cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}
										} else {
											if(result.isValidCQLWhileSavingExpression()){
												cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(result.getFunction().getReturnType());
												cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is " + result.getFunction().getReturnType());
											} else {
												cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(EMPTY_STRING);
												cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}
										}

										cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().setAnnotations();
										cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().redisplay();
									} else if (result.getFailureReason() == SaveUpdateCQLResult.NAME_NOT_UNIQUE) {
										displayErrorMessage(ERROR_DUPLICATE_IDENTIFIER_NAME, functionName, cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea());
									} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) {
										displayErrorMessage(UNABLE_TO_FIND_NODE_TO_MODIFY, functionName, cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea());
									} else if (result.getFailureReason() == SaveUpdateCQLResult.NO_SPECIAL_CHAR) {
										displayErrorMessage(ERROR_FUNCTION_NAME_NO_SPECIAL_CHAR, functionName, cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea());
										if (result.getFunction() != null) {
											((CQLStandaloneWorkSpaceView)cqlWorkspaceView).createAddArgumentViewForFunctions(
													result.getFunction().getArgumentList());
										}
									} else if (result.getFailureReason() == SaveUpdateCQLResult.FUNCTION_ARGUMENT_INVALID) {
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert(MessageDelegate.CQL_FUNCTION_ARGUMENT_NAME_ERROR);
									} else if (result.getFailureReason() == SaveUpdateCQLResult.COMMENT_INVALID) {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
									}

								}
								showSearchingBusy(false);
								if(!result.getCqlErrors().isEmpty()) {
									cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
								} else {
									if(result.getUsedCQLArtifacts().getUsedCQLFunctions().contains(functionName)) {
										cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
									} else {
										cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
									}
								}
							}
						});
			}
		} else {
			cqlWorkspaceView.getCQLFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(functionName.isEmpty() ? ERROR_SAVE_CQL_FUNCTION : "Invalid Function name. " + DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
		}
	}

	private void addAndModifyParameters() {
		cqlWorkspaceView.resetMessageDisplay();
		final String parameterName = cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().getText();
		String parameterLogic = cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().getText();
		String parameterComment = cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea().getText();

		boolean isValidParamaterName = isValidExpressionName(parameterName);
		if (isValidParamaterName) {
			if (validator.hasSpecialCharacter(parameterName.trim())) {
				cqlWorkspaceView.getCQLParametersView().getParamNameGroup().setValidationState(ValidationState.ERROR);
				messagePanel.getErrorMessageAlert().createAlert(ERROR_PARAMETER_NAME_NO_SPECIAL_CHAR);
				cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
			} else if(validator.isCommentMoreThan250Characters(parameterComment)){
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				cqlWorkspaceView.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.ERROR);
			} else if(validator.doesCommentContainInvalidCharacters(parameterComment)){
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_COMMENT_CHARACTERS());
				cqlWorkspaceView.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.ERROR);
			} else {
				CQLParameter parameter = new CQLParameter();
				parameter.setLogic(parameterLogic);
				parameter.setName(parameterName);
				parameter.setCommentString(parameterComment);
				CQLParameter toBeModifiedParamObj = null;

				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
					toBeModifiedParamObj = cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterMap().get(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedParamerterObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().saveAndModifyParameters(
						MatContext.get().getCurrentCQLLibraryId(), toBeModifiedParamObj, parameter,
						cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewParameterList(),
						isFormattable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
								messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										cqlWorkspaceView.getCQLLeftNavBarPanelView().setViewParameterList(result.getCqlModel().getCqlParameters());
										MatContext.get().setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
										MatContext.get().setCQLModel(result.getCqlModel());
										cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedParamerterObjId(result.getParameter().getId());
										cqlWorkspaceView.getCQLLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
										cqlWorkspaceView.getCQLLeftNavBarPanelView().updateParamMap();
										messagePanel.getErrorMessageAlert().clearAlert();
										cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(result.getParameter().getName());
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().setText(result.getParameter().getLogic());
										setIsPageDirty(false);
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
										SharedCQLWorkspaceUtility.validateCQLArtifact(result, cqlWorkspaceView.getCQLParametersView().getParameterAceEditor(), messagePanel, PARAMETER, parameterName.trim());
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().setAnnotations();
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().redisplay();
									} else if (result.getFailureReason() == SaveUpdateCQLResult.NAME_NOT_UNIQUE) {
										displayErrorMessage(ERROR_DUPLICATE_IDENTIFIER_NAME, parameterName, cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea());
									} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) {
										displayErrorMessage(UNABLE_TO_FIND_NODE_TO_MODIFY, parameterName, cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea());
									} else if (result.getFailureReason() == SaveUpdateCQLResult.NO_SPECIAL_CHAR) {
										displayErrorMessage(ERROR_PARAMETER_NAME_NO_SPECIAL_CHAR, parameterName, cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea());
									} else if (result.getFailureReason() == SaveUpdateCQLResult.COMMENT_INVALID) {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
									}
								}
								showSearchingBusy(false);
								if (!result.getCqlErrors().isEmpty()) {
									cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
								} else {
									if (result.getUsedCQLArtifacts().getUsedCQLParameters().contains(parameterName)) {
										cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
									} else {
										cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
									}
								}

							}
						});

			}

		} else {
			cqlWorkspaceView.getCQLParametersView().getParamNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(parameterName.isEmpty() 
					? ERROR_SAVE_CQL_PARAMETER
							: "Invalid Parameter name. " + DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
		}

	}

	private void addAndModifyDefintions() {

		cqlWorkspaceView.resetMessageDisplay();
		final String definitionName = cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().getText();
		String definitionLogic = cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().getText();
		String definitionComment = cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea().getText();
		String defineContext = EMPTY_STRING;
		if (cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}

		boolean isValidDefinitionName = isValidExpressionName(definitionName);
		if (isValidDefinitionName) {
			if (validator.hasSpecialCharacter(definitionName.trim())) {
				cqlWorkspaceView.getCQLDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
				messagePanel.getErrorMessageAlert().createAlert(ERROR_DEFINITION_NAME_NO_SPECIAL_CHAR);
				cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
			} else if(validator.isCommentMoreThan250Characters(definitionComment)){
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
			} else if(validator.doesCommentContainInvalidCharacters(definitionComment)){
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_COMMENT_CHARACTERS());
				cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
			} else {
				final CQLDefinition define = new CQLDefinition();
				define.setName(definitionName);
				define.setLogic(definitionLogic);
				define.setCommentString(definitionComment);
				define.setContext(defineContext);
				CQLDefinition toBeModifiedObj = null;

				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
					toBeModifiedObj = cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionMap()
							.get(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().saveAndModifyDefinitions(
						MatContext.get().getCurrentCQLLibraryId(), toBeModifiedObj, define,
						cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewDefinitions(),
						isFormattable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
								messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										cqlWorkspaceView.getCQLLeftNavBarPanelView().setViewDefinitions(result.getCqlModel().getDefinitionList());
										MatContext.get().setDefinitions(getDefinitionList(result.getCqlModel().getDefinitionList()));
										cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(result.getDefinition().getId());
										cqlWorkspaceView.getCQLLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
										cqlWorkspaceView.getCQLLeftNavBarPanelView().updateDefineMap();
										messagePanel.getErrorMessageAlert().clearAlert();
										cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setText(result.getDefinition().getName());
										cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setText(result.getDefinition().getLogic());
										setIsPageDirty(false);
										cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
										cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().removeAllMarkers();
										cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
										if (SharedCQLWorkspaceUtility.validateCQLArtifact(result, cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor(), messagePanel, DEFINITION, definitionName.trim())) {
											cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(EMPTY_STRING);
											cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
										} else if (!result.isDatatypeUsedCorrectly()) {
											if(result.isValidCQLWhileSavingExpression()){
												cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
												cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getDefinition().getReturnType());
											} else {
												cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(EMPTY_STRING);
												cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}

										} else {
											if(result.isValidCQLWhileSavingExpression()){
												cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
												cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getDefinition().getReturnType());
											} else {
												cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(EMPTY_STRING);
												cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");

											}
										}
										cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setAnnotations();
										cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().redisplay();
									} else {
										if (result.getFailureReason() == SaveUpdateCQLResult.NAME_NOT_UNIQUE) {
											displayErrorMessage(ERROR_DUPLICATE_IDENTIFIER_NAME, definitionName, cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea());
										} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) {
											displayErrorMessage(UNABLE_TO_FIND_NODE_TO_MODIFY, definitionName, cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea());
										} else if (result.getFailureReason() == SaveUpdateCQLResult.NO_SPECIAL_CHAR) {
											displayErrorMessage(ERROR_DEFINITION_NAME_NO_SPECIAL_CHAR, definitionName, cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea());
										} else if (result.getFailureReason() == SaveUpdateCQLResult.COMMENT_INVALID) {
											messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
										}
									}
								}
								showSearchingBusy(false);
								// if there are errors, enable the definition button
								if (!result.getCqlErrors().isEmpty()) {
									cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(true);
								} else {
									if (result.getUsedCQLArtifacts().getUsedCQLDefinitions().contains(definitionName)) {
										cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
									} else {
										cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(true);
									}
								}

							}
						});

			}

		} else {
			cqlWorkspaceView.getCQLDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(definitionName.isEmpty() ? ERROR_SAVE_CQL_DEFINITION : "Invalid Definition name. " + DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
		}

	}

	private void addIncludeLibraryInCQLLookUp() {
		cqlWorkspaceView.resetMessageDisplay();
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox()
				.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
			messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
			return;

		} else {
			messagePanel.getWarningMessageAlert().clearAlert();
		}
		final String aliasName = cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().getText();

		if (!aliasName.isEmpty() && cqlWorkspaceView.getIncludeView().getSelectedObjectList().size() > 0) {
			CQLLibraryDataSetObject cqlLibraryDataSetObject = cqlWorkspaceView.getIncludeView().getSelectedObjectList().get(0);

			if (validator.doesAliasNameFollowCQLAliasNamingConvention(aliasName.trim())) {
				CQLIncludeLibrary incLibrary = new CQLIncludeLibrary();
				incLibrary.setAliasName(aliasName);
				incLibrary.setCqlLibraryId(cqlLibraryDataSetObject.getId());
				String versionValue = cqlLibraryDataSetObject.getVersion().replace("v", EMPTY_STRING) + "." + cqlLibraryDataSetObject.getRevisionNumber();
				incLibrary.setVersion(versionValue);
				incLibrary.setCqlLibraryName(cqlLibraryDataSetObject.getCqlName());
				incLibrary.setQdmVersion(cqlLibraryDataSetObject.getQdmVersion());
				incLibrary.setSetId(cqlLibraryDataSetObject.getCqlSetId());
				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() == null) {
					// this is just to add include library and not modify
					MatContext.get().getCQLLibraryService().saveIncludeLibrayInCQLLookUp(
							MatContext.get().getCurrentCQLLibraryId(), null, incLibrary,
							cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewIncludeLibrarys(),
							new AsyncCallback<SaveUpdateCQLResult>() {

								@Override
								public void onFailure(Throwable caught) {
									showSearchingBusy(false);
									if(caught instanceof InvalidLibraryException) {
										messagePanel.getErrorMessageAlert().createAlert(caught.getMessage());
									} else {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
									}
								}

								@Override
								public void onSuccess(SaveUpdateCQLResult result) {
									if (result != null) {
										if (result.isSuccess()) {
											cqlWorkspaceView.resetMessageDisplay();
											setIsPageDirty(false);
											cqlWorkspaceView.getCQLLeftNavBarPanelView().setViewIncludeLibrarys(
													result.getCqlModel().getCqlIncludeLibrarys());
											MatContext.get().setIncludes(
													getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
											MatContext.get().setCQLModel(result.getCqlModel());
											cqlWorkspaceView.getCQLLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
											cqlWorkspaceView.getCQLLeftNavBarPanelView().udpateIncludeLibraryMap();
											cqlWorkspaceView.getIncludeView().setIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryMap()));
											messagePanel.getSuccessMessageAlert().createAlert(getIncludeLibrarySuccessMessage(result.getIncludeLibrary().getAliasName()));
											clearAlias();

											MatContext.get().setIncludedValues(result);
											if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
												messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
											} else {
												messagePanel.getWarningMessageAlert().clearAlert();
											}
										}
									}
								}
							});
				}

			} else {
				((CQLStandaloneWorkSpaceView)cqlWorkspaceView).getCqlIncludeLibraryView().getAliasNameGroup().setValidationState(ValidationState.ERROR);
				messagePanel.getErrorMessageAlert().createAlert(ERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR);
				((CQLStandaloneWorkSpaceView)cqlWorkspaceView).getAliasNameTxtArea().setText(aliasName.trim());
			}
		} else {
			((CQLStandaloneWorkSpaceView)cqlWorkspaceView).getCqlIncludeLibraryView().getAliasNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(SAVE_INCLUDE_LIBRARY_VALIATION_ERROR);
		}
	}

	private void eraseParameter() {
		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if(cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().getText() != null) {
			cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().setText(EMPTY_STRING);
			setIsPageDirty(true);
		}

	}

	private void eraseDefinition() {
		cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
		if ((cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().getText() != null)) {
			cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setText(EMPTY_STRING);
			setIsPageDirty(true);
		}
	}

	private void eraseFunction() {
		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if ((cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().setText(EMPTY_STRING);
			setIsPageDirty(true);
		}
	}

	private void buildInsertPopUp() {
		cqlWorkspaceView.resetMessageDisplay();
		InsertIntoAceEditorDialogBox.showListOfItemAvailableForInsertDialogBox(curAceEditor);
		setIsPageDirty(true);
	}

	protected void deleteDefinition() {
		cqlWorkspaceView.resetMessageDisplay();
		final String definitionName = cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().getText();
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
			final CQLDefinition toBeModifiedObj = cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionMap().get(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
			showSearchingBusy(true);
			MatContext.get().getCQLLibraryService().deleteDefinition(MatContext.get().getCurrentCQLLibraryId(), toBeModifiedObj, cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewDefinitions(), new AsyncCallback<SaveUpdateCQLResult>() {

				@Override
				public void onFailure(Throwable caught) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					showSearchingBusy(false);
				}

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					if (result != null) {
						if (result.isSuccess()) {
							cqlWorkspaceView.getCQLLeftNavBarPanelView().setViewDefinitions(result.getCqlModel().getDefinitionList());
							MatContext.get().setDefinitions(getDefinitionList(result.getCqlModel().getDefinitionList()));
							cqlWorkspaceView.getCQLLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
							cqlWorkspaceView.getCQLLeftNavBarPanelView().updateDefineMap();
							messagePanel.getErrorMessageAlert().clearAlert();
							messagePanel.getSuccessMessageAlert().setVisible(true);
							cqlWorkspaceView.getCQLLeftNavBarPanelView().getSearchSuggestDefineTextBox().setText(EMPTY_STRING);
							cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setText(EMPTY_STRING);
							cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setText(EMPTY_STRING);
							cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
							setIsPageDirty(false);
							cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
							cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().removeAllMarkers();
							cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setAnnotations();
							cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
							messagePanel.getSuccessMessageAlert().createAlert(buildRemovedSuccessfullyMessage(DEFINITION, toBeModifiedObj.getName()));
							cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(EMPTY_STRING);
						} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) {
							displayErrorMessage(UNABLE_TO_FIND_NODE_TO_MODIFY, definitionName, cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea());
						} else if (result.getFailureReason() == SaveUpdateCQLResult.SERVER_SIDE_VALIDATION) {
							displayErrorMessage(UNAUTHORIZED_DELETE_OPERATION, definitionName, cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea());
						}
					}
					showSearchingBusy(false);
					cqlWorkspaceView.getCQLDefinitionsView().getMainDefineViewVerticalPanel().setFocus(true);
				}
			});
		} else {
			cqlWorkspaceView.resetMessageDisplay();
			messagePanel.getErrorMessageAlert().createAlert(SELECT_DEFINITION_TO_DELETE);
			cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
		}
	}

	protected void deleteFunction() {
		cqlWorkspaceView.resetMessageDisplay();
		final String functionName = cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().getText();
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
			final CQLFunctions toBeModifiedFuncObj = cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionMap()
					.get(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
			showSearchingBusy(true);
			MatContext.get().getCQLLibraryService().deleteFunctions(MatContext.get().getCurrentCQLLibraryId(),
					toBeModifiedFuncObj, cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewFunctions(),
					new AsyncCallback<SaveUpdateCQLResult>() {

				@Override
				public void onFailure(Throwable caught) {
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					showSearchingBusy(false);
				}

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					if (result != null) {
						if (result.isSuccess()) {
							cqlWorkspaceView.getCQLLeftNavBarPanelView().setViewFunctions(result.getCqlModel().getCqlFunctions());
							MatContext.get().setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
							cqlWorkspaceView.getCQLLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
							cqlWorkspaceView.getCQLLeftNavBarPanelView().updateFunctionMap();
							messagePanel.getErrorMessageAlert().clearAlert();
							cqlWorkspaceView.getCQLFunctionsView().getFunctionArgNameMap().clear();
							cqlWorkspaceView.getCQLFunctionsView().getFunctionArgumentList().clear();
							cqlWorkspaceView.getCQLLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText(EMPTY_STRING);
							messagePanel.getSuccessMessageAlert().setVisible(true);
							cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setText(EMPTY_STRING);
							cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().setText(EMPTY_STRING);
							cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
							cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
							cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
							setIsPageDirty(false);
							cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
							cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();

							cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().setAnnotations();
							cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
							messagePanel.getSuccessMessageAlert().createAlert(buildRemovedSuccessfullyMessage(FUNCTION, toBeModifiedFuncObj.getName()));
							cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(EMPTY_STRING);

							if (result.getFunction() != null) {
								((CQLStandaloneWorkSpaceView)cqlWorkspaceView).createAddArgumentViewForFunctions(
										new ArrayList<CQLFunctionArgument>());
							}
						} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) {
							displayErrorMessage(UNABLE_TO_FIND_NODE_TO_MODIFY, functionName, cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea());
						} else if (result.getFailureReason() == SaveUpdateCQLResult.SERVER_SIDE_VALIDATION) {
							displayErrorMessage(UNAUTHORIZED_DELETE_OPERATION, functionName, cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea());
						}
					}
					showSearchingBusy(false);
					cqlWorkspaceView.getCQLFunctionsView().getMainFunctionVerticalPanel().setFocus(true);
				}
			});
		} else {
			cqlWorkspaceView.resetMessageDisplay();
			messagePanel.getErrorMessageAlert().createAlert(SELECT_FUNCTION_TO_DELETE);
			cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
		}
	}

	protected void deleteFunctionArgument(){
		String funcArgName = null;
		cqlWorkspaceView.resetMessageDisplay();
		setIsPageDirty(true);
		Iterator<CQLFunctionArgument> iterator = cqlWorkspaceView.getCQLFunctionsView().getFunctionArgumentList()
				.iterator();
		cqlWorkspaceView.getCQLFunctionsView().getFunctionArgNameMap().remove(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionArgumentName().toLowerCase());
		while (iterator.hasNext()) {
			CQLFunctionArgument cqlFunArgument = iterator.next();
			if (cqlFunArgument.getId().equals(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId())) {
				iterator.remove();
				cqlWorkspaceView.getCQLFunctionsView().createAddArgumentViewForFunctions(
						cqlWorkspaceView.getCQLFunctionsView().getFunctionArgumentList(),
						MatContext.get().getMeasureLockService().checkForEditPermission());
				funcArgName = cqlFunArgument.getArgumentName();
				break;
			}
		}

		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setFocus(true);

		messagePanel.getSuccessMessageAlert().createAlert(buildRemovedSuccessfullyMessage(ARGUMENT, funcArgName));
		setIsPageDirty(true);
	}


	protected void deleteParameter() {
		cqlWorkspaceView.resetMessageDisplay();
		final String parameterName = cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().getText();
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
			final CQLParameter toBeModifiedParamObj = cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterMap()
					.get(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedParamerterObjId());

			if(toBeModifiedParamObj.isReadOnly()){
				messagePanel.getSuccessMessageAlert().clearAlert();
				messagePanel.getErrorMessageAlert().createAlert("Unauthorized delete operation.");
				cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
			} else {
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().deleteParameter(MatContext.get().getCurrentCQLLibraryId(),
						toBeModifiedParamObj,
						cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewParameterList(),
						new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onFailure(Throwable caught) {
						messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result != null) {
							if (result.isSuccess()) {
								cqlWorkspaceView.getCQLLeftNavBarPanelView().setViewParameterList((result.getCqlModel().getCqlParameters()));
								MatContext.get().setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
								cqlWorkspaceView.getCQLLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
								cqlWorkspaceView.getCQLLeftNavBarPanelView().updateParamMap();
								messagePanel.getErrorMessageAlert().clearAlert();
								messagePanel.getSuccessMessageAlert().setVisible(true);
								cqlWorkspaceView.getCQLLeftNavBarPanelView().getSearchSuggestParamTextBox().setText(EMPTY_STRING);
								cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(EMPTY_STRING);
								cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().setText(EMPTY_STRING);
								cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
								setIsPageDirty(false);
								cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
								cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
								cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().setAnnotations();
								cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
								messagePanel.getSuccessMessageAlert().createAlert(buildRemovedSuccessfullyMessage(PARAMETER, toBeModifiedParamObj.getName()));  
							} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) {
								displayErrorMessage(UNABLE_TO_FIND_NODE_TO_MODIFY, parameterName, cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea());
							}else if (result.getFailureReason() == SaveUpdateCQLResult.SERVER_SIDE_VALIDATION) {
								displayErrorMessage(UNAUTHORIZED_DELETE_OPERATION, parameterName, cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea());
							}
						}
						showSearchingBusy(false);
						cqlWorkspaceView.getCQLParametersView().getMainParamViewVerticalPanel().setFocus(true);
					}
				});
			} 
		}else {
			cqlWorkspaceView.resetMessageDisplay();
			messagePanel.getErrorMessageAlert().createAlert(SELECT_PARAMETER_TO_DELETE);
			cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
		}
	}

	protected void deleteInclude() {
		cqlWorkspaceView.resetMessageDisplay();
		final String aliasName = cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().getText();
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() != null) {
			final CQLIncludeLibrary toBeModifiedIncludeObj = cqlWorkspaceView.getCQLLeftNavBarPanelView()
					.getIncludeLibraryMap()
					.get(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId());
			showSearchingBusy(true);
			MatContext.get().getCQLLibraryService().deleteInclude(MatContext.get().getCurrentCQLLibraryId(),
					toBeModifiedIncludeObj, 
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewIncludeLibrarys(),
					new AsyncCallback<SaveUpdateCQLResult>() {

				@Override
				public void onFailure(Throwable caught) {
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					showSearchingBusy(false);
				}

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					if (result.isSuccess()) {
						cqlWorkspaceView.getCQLLeftNavBarPanelView().setViewIncludeLibrarys(result.getCqlModel().getCqlIncludeLibrarys());
						MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
						MatContext.get().setIncludedValues(result);
						cqlWorkspaceView.getCQLLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
						cqlWorkspaceView.getCQLLeftNavBarPanelView().udpateIncludeLibraryMap();
						messagePanel.getErrorMessageAlert().clearAlert();
						messagePanel.getSuccessMessageAlert().setVisible(true);
						cqlWorkspaceView.getCQLLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText(EMPTY_STRING);
						cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText(EMPTY_STRING);
						cqlWorkspaceView.getIncludeView().getCqlLibraryNameTextBox().setText(EMPTY_STRING);
						cqlWorkspaceView.getIncludeView().getOwnerNameTextBox().setText(EMPTY_STRING);
						cqlWorkspaceView.getIncludeView().getViewCQLEditor().setText(EMPTY_STRING);
						cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
						setIsPageDirty(false);
						cqlWorkspaceView.getIncludeView().getViewCQLEditor().clearAnnotations();
						cqlWorkspaceView.getIncludeView().getViewCQLEditor().removeAllMarkers();
						cqlWorkspaceView.getIncludeView().getViewCQLEditor().setAnnotations();
						cqlWorkspaceView.getIncludeView().getDeleteButton().setEnabled(false);

						cqlWorkspaceView.getIncludeView().getCloseButton().fireEvent(new GwtEvent<ClickHandler>() {
							@Override
							public com.google.gwt.event.shared.GwtEvent.Type<ClickHandler> getAssociatedType() {
								return ClickEvent.getType();
							}

							@Override
							protected void dispatch(ClickHandler handler) {
								handler.onClick(null);
							}
						});
						messagePanel.getSuccessMessageAlert().createAlert(buildRemovedSuccessfullyMessage(LIBRARY, toBeModifiedIncludeObj.getAliasName()));
					} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) {
						displayErrorMessage(UNABLE_TO_FIND_NODE_TO_MODIFY, aliasName, cqlWorkspaceView.getIncludeView().getAliasNameTxtArea());
					}
					showSearchingBusy(false);
				}
			});
		} else {
			cqlWorkspaceView.resetMessageDisplay();
			messagePanel.getErrorMessageAlert().createAlert(SELECT_ALIAS_TO_DELETE);
			cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
		}
	}

	private void deleteCode(){
		cqlWorkspaceView.resetMessageDisplay();
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().deleteCode(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedCodesObjId(), MatContext.get().getCurrentCQLLibraryId(), new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onFailure(Throwable caught) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
				showSearchingBusy(false);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
				showSearchingBusy(false);
				if(result.isSuccess()){
					messagePanel.getSuccessMessageAlert().createAlert(buildRemovedSuccessfullyMessage(CODE, result.getCqlCode().getCodeOID()));
					cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
					appliedCodeTableList.clear();
					appliedCodeTableList.addAll(result.getCqlCodeList());
					MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);
					cqlWorkspaceView.getCQLLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
					cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService().checkForEditPermission());
					getAppliedValueSetList();
				} else {
					messagePanel.getErrorMessageAlert().createAlert("Unable to delete.");		
				}

				cqlWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
			}
		});
	}

	private void checkAndDeleteValueSet(){
		MatContext.get().getLibraryService().getCQLData(MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onSuccess(final SaveUpdateCQLResult result) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedValueSetObjId(null);
				appliedValueSetTableList.clear();
				if (result.getCqlModel().getAllValueSetAndCodeList() != null) {
					for (CQLQualityDataSetDTO dto : result.getCqlModel().getAllValueSetAndCodeList()) {
						if(dto.isSuppDataElement() || 
								dto.getOid().equals("419099009") || dto.getOid().equals("21112-8") 
								|| (dto.getType() !=null && dto.getType().equalsIgnoreCase("code")))
							continue;
						appliedValueSetTableList.add(dto);
					}

					if (appliedValueSetTableList.size() > 0) {
						Iterator<CQLQualityDataSetDTO> iterator = appliedValueSetTableList
								.iterator();
						while (iterator.hasNext()) {
							CQLQualityDataSetDTO dataSetDTO = iterator.next();
							if (dataSetDTO.getUuid() != null) {
								if (dataSetDTO.getUuid().equals(cqlWorkspaceView.getValueSetView()
										.getSelectedElementToRemove().getUuid())) {
									if (!dataSetDTO.isUsed()) {
										deleteValueSet(dataSetDTO.getId());
										iterator.remove();
									}
								}
							}
						}
					}
				}
				showSearchingBusy(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedValueSetObjId(null);
				showSearchingBusy(false);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
	}

	private void saveCQLGeneralInformation(String libraryName, String libraryComment) {

		String libraryId = MatContext.get().getCurrentCQLLibraryId();
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().saveAndModifyCQLGeneralInfo(libraryId, libraryName, libraryComment,
				new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onFailure(Throwable caught) {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);
			}

			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if (result != null) {
					cqlLibraryName = result.getCqlModel().getLibraryName().trim();
					cqlLibraryComment = result.getCqlModel().getLibraryComment();
					cqlWorkspaceView.getCqlGeneralInformationView().getLibraryNameValue().setText(cqlLibraryName);
					cqlWorkspaceView.getCqlGeneralInformationView().getComments().setText(result.getCqlModel().getLibraryComment());
					cqlWorkspaceView.getCqlGeneralInformationView().getComments().setCursorPos(0);
					messagePanel.getSuccessMessageAlert().createAlert(cqlLibraryName + " general information successfully updated");
					setIsPageDirty(false);
					MatContext.get().getCurrentLibraryInfo().setLibraryName(cqlLibraryName);
					CqlComposerPresenter.setContentHeading();
				}
				showSearchingBusy(false);
			}
		});

	}

	private void logRecentActivity() {
		MatContext.get().getCQLLibraryService().isLibraryAvailableAndLogRecentActivity(
				MatContext.get().getCurrentCQLLibraryId(), MatContext.get().getLoggedinUserId(),
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {}

					@Override
					public void onSuccess(Void result) {
						isCQLWorkSpaceLoaded = true;
						displayCQLView();
					}
				});

	}

	private void displayCQLView() {
		panel.clear();
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		cqlWorkspaceView.buildView(messagePanel);
		addLeftNavEventHandler();
		getCQLDataForLoad(); 
		cqlWorkspaceView.resetMessageDisplay();
		panel.add(cqlWorkspaceView.asWidget());
	}


	@Override
	public void beforeClosingDisplay() {
		currentIncludeLibrarySetId = null;
		currentIncludeLibraryId = null; 
		cqlWorkspaceView.getCQLLeftNavBarPanelView().clearShotcutKeyList();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		cqlWorkspaceView.getCQLFunctionsView().getFunctionArgNameMap().clear();
		cqlWorkspaceView.getValueSetView().clearCellTableMainPanel();
		cqlWorkspaceView.getCodesView().clearCellTableMainPanel();
		cqlWorkspaceView.getIncludeView().getSearchTextBox().setText(EMPTY_STRING);
		setIsPageDirty(false);
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesCollapse().getElement()
		.setClassName("panel-collapse collapse");
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getParamCollapse().getElement()
		.setClassName("panel-collapse collapse");
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineCollapse().getElement()
		.setClassName("panel-collapse collapse");
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionCollapse().getElement()
		.setClassName("panel-collapse collapse");
		if (cqlWorkspaceView.getCQLFunctionsView().getFunctionArgumentList().size() > 0) {
			cqlWorkspaceView.getCQLFunctionsView().getFunctionArgumentList().clear();
		}
		isModified = false;
		isCodeModified = false;
		setId = null;
		modifyValueSetDTO = null;
		curAceEditor = null;
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		messagePanel.clearAlerts();
		cqlWorkspaceView.resetAll();
		setIsPageDirty(false);
		panel.clear();
		cqlWorkspaceView.getMainPanel().clear();
		MatContext.get().getValuesets().clear();
	}

	@Override
	public void beforeDisplay() {
		if ((MatContext.get().getCurrentCQLLibraryId() == null)
				|| MatContext.get().getCurrentCQLLibraryId().isEmpty()) {
			displayEmpty();
		} else {
			panel.clear();
			((CQLStandaloneWorkSpaceView)cqlWorkspaceView).getLockedButtonVPanel();
			panel.add(cqlWorkspaceView.asWidget());
			if (!isCQLWorkSpaceLoaded) { 
				// this check is made so that when CQL is clicked from CQL library, its not called twice.
				displayCQLView();

			} else {
				isCQLWorkSpaceLoaded = false;
			}
		}
		CqlComposerPresenter.setSubSkipEmbeddedLink("CQLStandaloneWorkSpaceView.containerPanel");
		Mat.focusSkipLists("CqlComposer");

	}

	private void getCQLDataForLoad() {
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().getCQLDataForLoad(MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				handleCQLData(result);
				showSearchingBusy(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				showSearchingBusy(false);
			}
		});
	}


	private void handleCQLData(SaveUpdateCQLResult result) {
		if (result.isSuccess()) {
			if (result.getCqlModel() != null) {
				if (result.getSetId() != null) {
					setId = result.getSetId();
				}
				if (result.getCqlModel().getLibraryName() != null) {
					cqlLibraryName = cqlWorkspaceView.getCqlGeneralInformationView()
							.createCQLLibraryName(MatContext.get().getCurrentCQLLibraryeName());
					cqlLibraryComment = result.getCqlModel().getLibraryComment();
					String libraryVersion = MatContext.get().getCurrentCQLLibraryVersion();

					libraryVersion = libraryVersion.replaceAll("Draft ", EMPTY_STRING).trim();
					if (libraryVersion.startsWith("v")) {
						libraryVersion = libraryVersion.substring(1);
					}
					cqlWorkspaceView.getCqlGeneralInformationView().setGeneralInfoOfLibrary(cqlLibraryName, libraryVersion,
							result.getCqlModel().getQdmVersion(), "QDM", cqlLibraryComment);
				}

				List<CQLQualityDataSetDTO> appliedAllValueSetList = new ArrayList<>();
				List<CQLQualityDataSetDTO> appliedValueSetListInXML = result.getCqlModel().getAllValueSetAndCodeList();

				for (CQLQualityDataSetDTO dto : appliedValueSetListInXML) {
					if (dto.isSuppDataElement())
						continue;
					appliedAllValueSetList.add(dto);
				}
				MatContext.get().setCQLModel(result.getCqlModel());
				MatContext.get().setValuesets(appliedAllValueSetList);
				appliedValueSetTableList.clear();
				appliedCodeTableList.clear();
				for (CQLQualityDataSetDTO dto : result.getCqlModel().getValueSetList()) {
					if (dto.isSuppDataElement())
						continue;
					appliedValueSetTableList.add(dto);
				}
				cqlWorkspaceView.getCQLLeftNavBarPanelView().setAppliedQdmTableList(appliedValueSetTableList);
				cqlWorkspaceView.getCQLLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);

				if (result.getCqlModel().getCodeList() != null) {
					appliedCodeTableList.addAll(result.getCqlModel().getCodeList());
				}
				cqlWorkspaceView.getCQLLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
				cqlWorkspaceView.getCQLLeftNavBarPanelView().setAppliedCodeTableList(appliedCodeTableList);
				MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);

				if ((result.getCqlModel().getDefinitionList() != null)
						&& (result.getCqlModel().getDefinitionList().size() > 0)) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView()
					.setViewDefinitions(result.getCqlModel().getDefinitionList());
					cqlWorkspaceView.getCQLLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
					cqlWorkspaceView.getCQLLeftNavBarPanelView().updateDefineMap();
					MatContext.get().setDefinitions(getDefinitionList(result.getCqlModel().getDefinitionList()));
				} else {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineBadge().setText("00");
				}
				if ((result.getCqlModel().getCqlParameters() != null)
						&& (result.getCqlModel().getCqlParameters().size() > 0)) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView()
					.setViewParameterList(result.getCqlModel().getCqlParameters());
					cqlWorkspaceView.getCQLLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
					cqlWorkspaceView.getCQLLeftNavBarPanelView().updateParamMap();
					MatContext.get().setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
				} else {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getParamBadge().setText("00");
				}
				if ((result.getCqlModel().getCqlFunctions() != null)
						&& (result.getCqlModel().getCqlFunctions().size() > 0)) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().setViewFunctions(result.getCqlModel().getCqlFunctions());
					cqlWorkspaceView.getCQLLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
					cqlWorkspaceView.getCQLLeftNavBarPanelView().updateFunctionMap();
					MatContext.get().setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
				} else {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionBadge().setText("00");
				}
				if ((result.getCqlModel().getCqlIncludeLibrarys() != null)
						&& (result.getCqlModel().getCqlIncludeLibrarys().size() > 0)) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView()
					.setViewIncludeLibrarys(result.getCqlModel().getCqlIncludeLibrarys());
					cqlWorkspaceView.getCQLLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
					cqlWorkspaceView.getCQLLeftNavBarPanelView().udpateIncludeLibraryMap();
					MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
					MatContext.get().setIncludedValues(result);


				} else {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesBadge().setText("00");
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryMap().clear();
				}

				boolean isValidQDMVersion = cqlWorkspaceView.getCQLLeftNavBarPanelView()
						.checkForIncludedLibrariesQDMVersion(true); //true because its a standalone library
				if (!isValidQDMVersion) {
					messagePanel.getErrorMessageAlert().createAlert(INVALID_QDM_VERSION_IN_INCLUDES);
				} else {
					messagePanel.getErrorMessageAlert().clearAlert();
				}
			}
		}
	}

	private void addLeftNavEventHandler() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getGeneralInformation().addClickHandler(event -> generalInfoEvent());
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesLibrary().addClickHandler(event -> leftNavBarIncludesLibraryClicked(event));
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getCodesLibrary().addClickHandler(event -> leftNavBarCodesClicked(event));
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQDM().addClickHandler(event -> leftNavbarAppliedQDMClicked());
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterLibrary().addClickHandler(event -> leftNavBarParametersClicked(event));
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionLibrary().addClickHandler(event -> leftNavBarDefinitionsClicked(event));
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionLibrary().addClickHandler(event -> leftNavBarFunctionClicked(event));
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewCQL().addClickHandler(event -> leftNavBarViewCQLClicked());
	}

	private void getUsedArtifacts() {
		showSearchingBusy(true);
		MatContext.get().getLibraryService().getUsedCqlArtifacts(
				MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<GetUsedCQLArtifactsResult>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);

					}

					@Override
					public void onSuccess(GetUsedCQLArtifactsResult result) {
						showSearchingBusy(false);
						if(!result.getCqlErrors().isEmpty()) {
							for(CQLQualityDataSetDTO cqlDTo : cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQdmTableList()){
								cqlDTo.setUsed(false);
							}
						} else {
							for(CQLQualityDataSetDTO cqlDTo : cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQdmTableList()){
								if (result.getUsedCQLValueSets().contains(cqlDTo.getName())) {
									cqlDTo.setUsed(true);
								} else{
									cqlDTo.setUsed(false);
								}
							}
						}

						if(cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQdmTableList().size() > 0) {
							cqlWorkspaceView.getValueSetView().getCelltable().redraw();
							cqlWorkspaceView.getValueSetView().getListDataProvider().refresh();
						}

					}

				});

	}

	private void addValueSetEventHandlers() {
		cqlWorkspaceView.getValueSetView().getCancelQDMButton().addClickHandler(event -> valueSetViewCancelQDMButtonClicked());
		cqlWorkspaceView.getValueSetView().getUpdateFromVSACButton().addClickHandler(event -> valueSetViewUpdateFromVSACButtonClicked());
		cqlWorkspaceView.getValueSetView().getRetrieveFromVSACButton().addClickHandler(event -> valueSetViewRetrieveFromVSACClicked());
		cqlWorkspaceView.getValueSetView().getSaveButton().addClickHandler(event -> valueSetViewSaveButtonClicked());
		cqlWorkspaceView.getValueSetView().getUserDefinedInput().addValueChangeHandler(event -> valueSetViewUserDefinedInputChangedEvent());
		cqlWorkspaceView.getValueSetView().getClearButton().addClickHandler(event -> valueSetViewClearButtonClicked());
		cqlWorkspaceView.getValueSetView().getCopyButton().addClickHandler(event -> copyValueSets());
		cqlWorkspaceView.getValueSetView().getSelectAllButton().addClickHandler(event -> selectAllValueSets());
		cqlWorkspaceView.getValueSetView().getPasteButton().addClickHandler(event -> valueSetViewPasteButtonClicked(event));
		cqlWorkspaceView.getValueSetView().getReleaseListBox().addChangeHandler(event -> valueSetViewReleaseListBoxChangedEvent());
		cqlWorkspaceView.getValueSetView().getProgramListBox().addChangeHandler(event -> valueSetViewProgramListBoxChangedEvent());
		cqlWorkspaceView.getValueSetView().getOIDInput().addValueChangeHandler(event -> clearOID());
		cqlWorkspaceView.getValueSetView().getOIDInput().sinkBitlessEvent("input");
		cqlWorkspaceView.getValueSetView().setObserver(new CQLAppliedValueSetView.Observer() {
			@Override
			public void onModifyClicked(CQLQualityDataSetDTO result) {
				if(!cqlWorkspaceView.getValueSetView().getIsLoading()){
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
					isModified = true;
					modifyValueSetDTO = result;
					String displayName = result.getName();
					// Substring at 60th character length.
					if(displayName.length() >=60){
						displayName = displayName.substring(0, 59);
					}
					HTML searchHeaderText = new HTML("<strong>Modify value set ( "+displayName +")</strong>");
					cqlWorkspaceView.getValueSetView().getSearchHeader().clear();
					cqlWorkspaceView.getValueSetView().getSearchHeader().add(searchHeaderText);
					cqlWorkspaceView.getValueSetView().getMainPanel().getElement().focus();
					if(result.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)){
						isUserDefined = true;
					} else {
						isUserDefined = false;
					}

					onModifyValueSet(result, isUserDefined);
					cqlWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
				}
			}

			@Override
			public void onDeleteClicked(CQLQualityDataSetDTO result, final int index) {
				if(!cqlWorkspaceView.getValueSetView().getIsLoading()){
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
					if((modifyValueSetDTO!=null) && modifyValueSetDTO.getId().equalsIgnoreCase(result.getId())){
						isModified = false;
					}
					String libraryId = MatContext.get().getCurrentCQLLibraryId();
					if ((libraryId != null) && !libraryId.equals(EMPTY_STRING)) {
						cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedValueSetObjId(result.getId());
						deleteConfirmationDialogBox.getMessageAlert().createAlert(buildSelectedToDeleteWithConfirmationMessage(VALUESET, result.getName()));
						deleteConfirmationDialogBox.show();
						cqlWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
					}
				}
			}

		});
	}

	private void clearOID() {
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

	private void alert508StateChanges() {
		StringBuilder helpTextBuilder = new StringBuilder();
		helpTextBuilder.append(build508HelpString(previousIsProgramListBoxEnabled, isProgramListBoxEnabled, "Program and Release List Boxes"));
		helpTextBuilder.append(build508HelpString(previousIsRetrieveButtonEnabled, isRetrieveButtonEnabled, "Retrieve Button"));
		helpTextBuilder.append(build508HelpString(previousIsApplyButtonEnabled, isApplyButtonEnabled, "Apply Button"));

		cqlWorkspaceView.getValueSetView().getHelpBlock().setText(helpTextBuilder.toString());
	}

	private void copyValueSets() {
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

	private void selectAllValueSets() {
		cqlWorkspaceView.resetMessageDisplay();
		if(cqlWorkspaceView.getValueSetView().getAllValueSets() != null &&
				cqlWorkspaceView.getValueSetView().getAllValueSets().size() > 0){
			cqlWorkspaceView.getValueSetView().selectAll();
			messagePanel.getSuccessMessageAlert().createAlert(VALUE_SETS_SELECTED_SUCCESSFULLY);
		}
	}

	private void pasteValueSets() {
		cqlWorkspaceView.resetMessageDisplay();
		GlobalCopyPasteObject gbCopyPaste = MatContext.get().getGlobalCopyPaste();
		showSearchingBusy(true);
		if ((gbCopyPaste != null) && (gbCopyPaste.getCopiedValueSetList().size() > 0)) {
			List<CQLValueSetTransferObject> cqlValueSetTransferObjectsList = cqlWorkspaceView.getValueSetView()
					.setMatValueSetListForValueSets(gbCopyPaste.getCopiedValueSetList(), appliedValueSetTableList);
			if (cqlValueSetTransferObjectsList.size() > 0) {

				MatContext.get().getLibraryService().saveValueSetList(cqlValueSetTransferObjectsList,
						appliedValueSetTableList, MatContext.get().getCurrentCQLLibraryId(),
						new AsyncCallback<CQLQualityDataModelWrapper>() {

					@Override
					public void onFailure(Throwable caught) {
						showSearchingBusy(false);
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}

					@Override
					public void onSuccess(CQLQualityDataModelWrapper result) {
						showSearchingBusy(false);
						if (result != null && result.getQualityDataDTO() != null) {
							setAppliedValueSetListInTable(result.getQualityDataDTO());
							messagePanel.getSuccessMessageAlert().createAlert(SUCCESSFULLY_VALUESET_PASTE);
						}
					}
				});
			} else {
				showSearchingBusy(false);
				messagePanel.getSuccessMessageAlert().createAlert(SUCCESSFULLY_VALUESET_PASTE);
			}
			MatContext.get().getGlobalCopyPaste().getCopiedValueSetList().clear();
			
		} else {
			showSearchingBusy(false);
			messagePanel.getWarningMessageAlert().createAlert(WARNING_PASTING_IN_VALUESET);
		}	 
	}


	private void copyCodes() {
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

	private void selectAllCodes() {
		cqlWorkspaceView.resetMessageDisplay();
		if(cqlWorkspaceView.getCodesView().getAllCodes() != null) {
			cqlWorkspaceView.getCodesView().selectAll();
			messagePanel.getSuccessMessageAlert().createAlert(CODES_SELECTED_SUCCESSFULLY);
		}
	}

	private void pasteCodes() {
		cqlWorkspaceView.resetMessageDisplay();
		GlobalCopyPasteObject gbCopyPaste = MatContext.get().getGlobalCopyPaste();
		if( (gbCopyPaste != null) && (gbCopyPaste.getCopiedCodeList().size()>0) ){
			List<CQLCode> codesToPaste = cqlWorkspaceView.getCodesView().setMatCodeList(gbCopyPaste.getCopiedCodeList(), appliedCodeTableList);
			if(codesToPaste.size() > 0) {
				String cqlLibraryId = MatContext.get().getCurrentCQLLibraryId();
				showSearchingBusy(true);
				cqlService.saveCQLCodeListToCQLLibrary(codesToPaste, cqlLibraryId, new AsyncCallback<SaveUpdateCQLResult>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate()
								.getGenericErrorMessage());
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						messagePanel.getSuccessMessageAlert().createAlert(SUCCESSFULLY_PASTED_CODES_IN_MEASURE);
						cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);
						cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService().checkForEditPermission());
						cqlWorkspaceView.getCQLLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						if (result != null && result.getCqlModel().getAllValueSetAndCodeList() != null) {
							setAppliedValueSetListInTable(result.getCqlModel().getAllValueSetAndCodeList());
						}
						showSearchingBusy(false);
					}
				});
			} else {
				showSearchingBusy(false);
				messagePanel.getSuccessMessageAlert().createAlert(SUCCESSFULLY_PASTED_CODES_IN_MEASURE);
			}
			MatContext.get().getGlobalCopyPaste().getCopiedCodeList().clear();
		} else {
			showSearchingBusy(false);
			messagePanel.getWarningMessageAlert().createAlert(CLIPBOARD_DOES_NOT_CONTAIN_CODES);
		}
	}

	private void addCodeSearchPanelHandlers() {
		cqlWorkspaceView.getCodesView().getCopyButton().addClickHandler(event -> copyCodes());
		cqlWorkspaceView.getCodesView().getSelectAllButton().addClickHandler(event -> selectAllCodes());
		cqlWorkspaceView.getCodesView().getPasteButton().addClickHandler(event -> codesViewPastButtonClicked(event));
		cqlWorkspaceView.getCodesView().getClearButton().addClickHandler(event -> codesViewClearButtonClicked());
		cqlWorkspaceView.getCodesView().getRetrieveFromVSACButton().addClickHandler(event -> codesViewRetrieveFromVSACButtonClicked());
		cqlWorkspaceView.getCodesView().getSaveButton().addClickHandler(event -> codesViewSaveButtonClicked());
		cqlWorkspaceView.getCodesView().getCancelCodeButton().addClickHandler(event -> codesViewCancelButtonClicked());
		cqlWorkspaceView.getCodesView().setDelegator(new Delegator() {
			@Override
			public void onDeleteClicked(CQLCode result, int index) {
				if(!cqlWorkspaceView.getCodesView().getIsLoading()) {
					messagePanel.getSuccessMessageAlert().clearAlert();
					messagePanel.getErrorMessageAlert().clearAlert();
					if(result != null){
						cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedCodesObjId(result.getId());
						deleteConfirmationDialogBox.getMessageAlert().createAlert(buildSelectedToDeleteWithConfirmationMessage(CODE, result.getCodeOID()));
						deleteConfirmationDialogBox.show();
						cqlWorkspaceView.getCodesView().getCodeInput().setFocus(true);
					}
				} 
			}

			@Override
			public void onModifyClicked(CQLCode object) {
				if(!cqlWorkspaceView.getValueSetView().getIsLoading()){
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
					isCodeModified = true;
					modifyCQLCode = object;
					cqlWorkspaceView.getCodesView().setValidateCodeObject(modifyCQLCode);
					String displayName = object.getCodeOID();
					// Substring at 60th character length.
					if(displayName.length() >=60){
						displayName = displayName.substring(0, 59);
					}
					HTML searchHeaderText = new HTML("<strong>Modify Code ( "+displayName +")</strong>");
					cqlWorkspaceView.getCodesView().getSearchHeader().clear();
					cqlWorkspaceView.getCodesView().getSearchHeader().add(searchHeaderText);
					cqlWorkspaceView.getCodesView().getMainPanel().getElement().focus();

					onModifyCode(object);
					cqlWorkspaceView.getCodesView().getCodeInput().setFocus(true);
				}
			}
		});
	}

	private void modifyCodes() {
		String cqlLibraryId = MatContext.get().getCurrentCQLLibraryId();	
		String codeName = cqlWorkspaceView.getCodesView().getCodeDescriptorInput().getValue();
		codeName = StringUtility.removeEscapedCharsFromString(codeName);
		CQLCode refCode = buildCQLCodeFromCodesView(codeName);

		MatCodeTransferObject transferObject = cqlWorkspaceView.getCodesView().getCodeTransferObject(cqlLibraryId, refCode);
		if (null != transferObject) {
			appliedCodeTableList.removeIf(code -> code.getDisplayName().equals(modifyCQLCode.getDisplayName()));

			if(!cqlWorkspaceView.getCodesView().checkCodeInAppliedCodeTableList(refCode.getDisplayName(), appliedCodeTableList)) {
				showSearchingBusy(true);
				cqlService.modifyCQLCodeInCQLLibrary(modifyCQLCode, refCode, cqlLibraryId, new AsyncCallback<SaveUpdateCQLResult>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate()
								.getGenericErrorMessage());
						showSearchingBusy(false);
						appliedCodeTableList.add(modifyCQLCode);
					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						messagePanel.getSuccessMessageAlert().createAlert(SUCCESSFUL_MODIFY_APPLIED_CODE);
						cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);
						cqlWorkspaceView.getCQLLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						if (result.getCqlModel().getAllValueSetAndCodeList() != null) {
							setAppliedValueSetListInTable(result.getCqlModel().getAllValueSetAndCodeList());
						}
						cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService().checkForEditPermission());
						//Temporary fix to update codes for insert Icon.
						getAppliedValueSetList();
						showSearchingBusy(false);
						cqlWorkspaceView.getCodesView().getSaveButton().setEnabled(false);
						isCodeModified = false;
						modifyCQLCode = null;
					}
				});
			} else {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(refCode.getDisplayName()));
			}
		}
	}

	private void addNewCodes() {
		String cqlLibraryId = MatContext.get().getCurrentCQLLibraryId();
		final String codeName = StringUtility.removeEscapedCharsFromString(cqlWorkspaceView.getCodesView().getCodeDescriptorInput().getValue());
		CQLCode refCode = buildCQLCodeFromCodesView(codeName);
		final String codeSystemName = refCode.getCodeSystemName();
		final String codeId = refCode.getCodeOID();

		MatCodeTransferObject transferObject = cqlWorkspaceView.getCodesView().getCodeTransferObject(cqlLibraryId, refCode);

		if (null != transferObject) {
			showSearchingBusy(true);
			cqlService.saveCQLCodestoCQLLibrary(transferObject, new AsyncCallback<SaveUpdateCQLResult>() {

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {

					if(result.isSuccess()){
						messagePanel.getSuccessMessageAlert().createAlert(getCodeSuccessMessage(cqlWorkspaceView.getCodesView().getCodeInput().getText()));
						cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService().checkForEditPermission());
						cqlWorkspaceView.getCQLLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						getAppliedValueSetList();
					} else {
						messagePanel.getSuccessMessageAlert().clearAlert();
						if(result.getFailureReason()==result.getDuplicateCode()){
							messagePanel.getErrorMessageAlert().createAlert(generateDuplicateErrorMessage(codeName));
						}

						else if(result.getFailureReason() ==  result.getBirthdateOrDeadError()) {
							messagePanel.getErrorMessageAlert().createAlert(getBirthdateOrDeadMessage(codeSystemName, codeId));
							cqlWorkspaceView.getCodesView().buildCodesCellTable(
									appliedCodeTableList,
									MatContext.get().getMeasureLockService().checkForEditPermission());
						}
					}
					showSearchingBusy(false);
					cqlWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
					cqlWorkspaceView.getCodesView().getSaveButton().setEnabled(!result.isSuccess());
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					showSearchingBusy(false);
				}
			});
		}
	}

	private CQLCode buildCQLCodeFromCodesView(String codeName) {
		CQLCode refCode = new CQLCode();
		CQLCodesView codesView = cqlWorkspaceView.getCodesView();
		boolean isCodeSystemVersionIncluded = codesView.getIncludeCodeSystemVersionCheckBox().getValue();
		refCode.setCodeOID(codesView.getCodeInput().getValue());
		refCode.setName(codesView.getCodeDescriptorInput().getValue());
		refCode.setDisplayName(StringUtility.removeEscapedCharsFromString(codeName));
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

	private void updateVSACValueSets() {
		showSearchingBusy(true);
		String expansionId = null;

		cqlService.updateCQLVSACValueSets(MatContext.get().getCurrentCQLLibraryId(), expansionId, new AsyncCallback<VsacApiResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				showSearchingBusy(false);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());

			}

			@Override
			public void onSuccess(final VsacApiResult result) {
				if (result.isSuccess()) {
					messagePanel.getSuccessMessageAlert().createAlert(VSAC_UPDATE_SUCCESSFULL);
					List<CQLQualityDataSetDTO> appliedListModel = new ArrayList<>();
					for (CQLQualityDataSetDTO cqlQDMDTO : result.getUpdatedCQLQualityDataDTOLIst()) {
						if (!ConstantMessages.DEAD_OID.equals(cqlQDMDTO.getDataType()) && !ConstantMessages.BIRTHDATE_OID.equals(cqlQDMDTO.getDataType())
								&& (cqlQDMDTO.getType() == null))  {
							appliedListModel.add(cqlQDMDTO);

							for(CQLQualityDataSetDTO cqlQualityDataSetDTO : appliedValueSetTableList){
								if(cqlQualityDataSetDTO.getId().equals(cqlQDMDTO.getId())){
									cqlQualityDataSetDTO.setOriginalCodeListName(cqlQDMDTO.getOriginalCodeListName());
									cqlQualityDataSetDTO.setName(cqlQDMDTO.getName());
								}
							}

							for(CQLIdentifierObject cqlIdentifierObject : MatContext.get().getValuesets()){
								if(cqlIdentifierObject.getId().equals(cqlQDMDTO.getId())){
									cqlIdentifierObject.setIdentifier(cqlQDMDTO.getName());
								}
							}

							for(CQLQualityDataSetDTO dataSetDTO : MatContext.get().getValueSetCodeQualityDataSetList()){
								if(dataSetDTO.getId().equals(cqlQDMDTO.getId())){
									dataSetDTO.setOriginalCodeListName(cqlQDMDTO.getOriginalCodeListName());
									dataSetDTO.setName(cqlQDMDTO.getName());
								}
							}
						}
					}
					cqlWorkspaceView.getValueSetView().buildAppliedValueSetCellTable(appliedListModel, MatContext.get().getLibraryLockService().checkForEditPermission());
					cqlWorkspaceView.getCQLLeftNavBarPanelView().setAppliedQdmTableList(appliedValueSetTableList);
					cqlWorkspaceView.getCQLLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
				} else {
					messagePanel.getErrorMessageAlert().createAlert(cqlWorkspaceView.getValueSetView().convertMessage(result.getFailureReason()));
				}
				showSearchingBusy(false);
			}
		});
	}

	private void searchValueSetInVsac(String release, String expansionProfile) {
		showSearchingBusy(true);
		currentMatValueSet = null;
		final String oid = cqlWorkspaceView.getValueSetView().getOIDInput().getValue();
		if (!MatContext.get().isUMLSLoggedIn()) {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			messagePanel.getErrorMessageAlert().setVisible(true);
			showSearchingBusy(false);
			return;
		}

		if ((oid == null) || oid.trim().isEmpty()) {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED());
			messagePanel.getErrorMessageAlert().setVisible(true);
			showSearchingBusy(false);
			return;
		}

		vsacapiService.getMostRecentValueSetByOID(oid, release, expansionProfile, new AsyncCallback<VsacApiResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
				messagePanel.getErrorMessageAlert().setVisible(true);
				showSearchingBusy(false);
			}
			@Override
			public void onSuccess(final VsacApiResult result) {
				if (result.isSuccess()) {
					List<MatValueSet> matValueSets = result.getVsacResponse();
					if (matValueSets != null) {						
						currentMatValueSet = matValueSets.get(0);
					}
					cqlWorkspaceView.getValueSetView().getOIDInput().setTitle(oid);
					cqlWorkspaceView.getValueSetView().getUserDefinedInput()
					.setValue(currentMatValueSet.getDisplayName());
					cqlWorkspaceView.getValueSetView().getUserDefinedInput()
					.setTitle(currentMatValueSet.getDisplayName());

					cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(true);					
					messagePanel.getSuccessMessageAlert().createAlert(getValuesetSuccessfulReterivalMessage(matValueSets.get(0).getDisplayName()));
					messagePanel.getSuccessMessageAlert().setVisible(true);
				} else {
					String message = cqlWorkspaceView.getValueSetView().convertMessage(result.getFailureReason());
					messagePanel.getErrorMessageAlert().createAlert(message);
					messagePanel.getErrorMessageAlert().setVisible(true);
				}
				showSearchingBusy(false);
			}
		});
	}

	private void searchCQLCodesInVsac() {

		final String url = cqlWorkspaceView.getCodesView().getCodeSearchInput().getValue().trim();
		cqlWorkspaceView.getCodesView().getCodeSearchInput().setText(url);

		if (!MatContext.get().isUMLSLoggedIn()) {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			messagePanel.getErrorMessageAlert().setVisible(true);

			return;
		}

		if ((url == null) || url.trim().isEmpty()) {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_CODE_IDENTIFIER_REQUIRED());
			return;
		}

		if(validator.validateForCodeIdentifier(url)){
			cqlWorkspaceView.getCodesView().getSaveButton().setEnabled(false);
			messagePanel.getErrorMessageAlert().createAlert(UMLS_INVALID_CODE_IDENTIFIER);
			return;
		} else {
			retrieveCodeReferences(url);
		}

	}

	private void retrieveCodeReferences(String url){

		showSearchingBusy(true);

		vsacapiService.getDirectReferenceCode(url, new AsyncCallback<VsacApiResult>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());
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
					cqlWorkspaceView.getCodesView().getSaveButton().setEnabled(true);

					messagePanel.getSuccessMessageAlert().createAlert("Code "+result.getDirectReferenceCode().getCode()+" successfully retrieved from VSAC.");
					CQLCode code = buildCQLCodeFromCodesView(StringUtility.removeEscapedCharsFromString(cqlWorkspaceView.getCodesView().getCodeDescriptorInput().getValue()));
					cqlWorkspaceView.getCodesView().setValidateCodeObject(code);

				} else {
					String message = cqlWorkspaceView.getCodesView().convertMessage(result.getFailureReason());
					messagePanel.getErrorMessageAlert().createAlert(message);
					messagePanel.getErrorMessageAlert().setVisible(true);
				}

				showSearchingBusy(false);
				cqlWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
			}
		});
	}

	protected final void modifyValueSetOrUserDefined(final boolean isUserDefined) {
		if (!isUserDefined) { // Normal Available Value Set Flow
			modifyValueSet();
		} else { // Pseudo Value set Flow
			modifyUserDefinedValueSet();
		}
	}

	private void modifyValueSet() {
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
			getUsedArtifacts();
		} else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE());
		}
	}

	private void modifyValueSetList(CQLQualityDataSetDTO qualityDataSetDTO) {
		for (int i = 0; i < appliedValueSetTableList.size(); i++) {
			if (qualityDataSetDTO.getName().equals(appliedValueSetTableList.get(i).getName())) {
				appliedValueSetTableList.remove(i);
				break;

			}
		}
	}

	private void updateAppliedValueSetsList(final MatValueSet matValueSet, final CodeListSearchDTO codeListSearchDTO,
			final CQLQualityDataSetDTO qualityDataSetDTO) {		
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		matValueSetTransferObject.setCqlLibraryId(MatContext.get().getCurrentCQLLibraryId());
		matValueSetTransferObject.setMatValueSet(matValueSet);
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setCqlQualityDataSetDTO(qualityDataSetDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		matValueSetTransferObject.setUserDefinedText(cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText());
		matValueSetTransferObject.scrubForMarkUp();
		showSearchingBusy(true);
		MatContext.get().getLibraryService().modifyCQLValueSets(matValueSetTransferObject,
				new AsyncCallback<SaveUpdateCQLResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);
				isModified = false;
				modifyValueSetDTO = null;
				currentMatValueSet = null;
				cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
			}

			@Override
			public void onSuccess(final SaveUpdateCQLResult result) {
				if (result != null) {
					if (result.isSuccess()) {
						isModified = false;
						modifyValueSetDTO = null;
						currentMatValueSet = null;
						cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();

						messagePanel.getSuccessMessageAlert().createAlert(SUCCESSFUL_MODIFY_APPLIED_VALUESET);
						getAppliedValueSetList();
					} else {
						if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
							messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getName()));
						} else if (result.getFailureReason() == SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION) {
							messagePanel.getErrorMessageAlert().createAlert("Invalid Input data.");
						}
					}
				}
				cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
				showSearchingBusy(false);
			}
		});

	}

	private void addNewValueSet(final boolean isUserDefinedValueSet) {
		if (!isUserDefinedValueSet) {
			addVSACCQLValueset();
		} else {
			addUserDefinedValueSet();
		}
	}

	private void addVSACCQLValueset() {
		String libraryID = MatContext.get().getCurrentCQLLibraryId();
		CQLValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(libraryID);
		matValueSetTransferObject.scrubForMarkUp();
		String originalCodeListName = matValueSetTransferObject.getMatValueSet().getDisplayName(); 
		String suffix = cqlWorkspaceView.getValueSetView().getSuffixInput().getValue();
		final String codeListName = (originalCodeListName!=null ? originalCodeListName : EMPTY_STRING) + (!suffix.isEmpty() ? " (" + suffix + ")" : EMPTY_STRING);
		if (!cqlWorkspaceView.getValueSetView().checkNameInValueSetList(codeListName,appliedValueSetTableList)) {
			showSearchingBusy(true);
			MatContext.get().getLibraryService().saveCQLValueset(matValueSetTransferObject,
					new AsyncCallback<SaveUpdateCQLResult>() {

				@Override
				public void onFailure(Throwable caught) {
					showSearchingBusy(false);
					if (!appliedValueSetTableList.isEmpty()) {
						appliedValueSetTableList.clear();
					}
					currentMatValueSet = null;
					cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
				}

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					String message = EMPTY_STRING;
					if (result != null) {
						if (result.isSuccess()) {
							message = getValuesetSuccessMessage(codeListName);
							MatContext.get().getEventBus().fireEvent(new QDSElementCreatedEvent(codeListName));
							cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
							messagePanel.getSuccessMessageAlert().createAlert(message);
							previousIsProgramListBoxEnabled = isProgramListBoxEnabled;
							isProgramListBoxEnabled = true;
							loadProgramsAndReleases(); 
							getAppliedValueSetList();
						} else {
							if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
								messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getName()));
							}
						}
					}
					getUsedArtifacts();
					currentMatValueSet = null;
					cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);

					showSearchingBusy(false);
				}
			});
		}  else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(codeListName));
		}

	}

	private void addUserDefinedValueSet() {
		CQLValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(MatContext.get().getCurrentCQLLibraryId());
		matValueSetTransferObject.scrubForMarkUp();
		matValueSetTransferObject.setMatValueSet(null);
		if ((matValueSetTransferObject.getUserDefinedText().length() > 0)) {
			ValueSetNameInputValidator valueSetNameInputValidator = new ValueSetNameInputValidator();
			String message = valueSetNameInputValidator.validate(matValueSetTransferObject);
			if (message.isEmpty()) {
				final String userDefinedInput = matValueSetTransferObject.getCqlQualityDataSetDTO().getName(); 
				if (!cqlWorkspaceView.getValueSetView().checkNameInValueSetList(userDefinedInput,appliedValueSetTableList)) {
					showSearchingBusy(true);
					MatContext.get().getLibraryService().saveCQLUserDefinedValueset(matValueSetTransferObject,
							new AsyncCallback<SaveUpdateCQLResult>() {
						@Override
						public void onFailure(final Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							showSearchingBusy(false);
							cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
						}

						@Override
						public void onSuccess(final SaveUpdateCQLResult result) {
							if (result != null) {
								if (result.isSuccess()) {
									if (result.getXml() != null) {
										messagePanel.getSuccessMessageAlert().createAlert(getValuesetSuccessMessage(userDefinedInput));
										MatContext.get().setValuesets(result.getCqlAppliedQDMList());
										cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
										getAppliedValueSetList();
									}
								} else {
									if (result.getFailureReason() == SaveUpdateCQLResult.ALREADY_EXISTS) {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getName()));
									} else if (result.getFailureReason() == SaveUpdateCQLResult.SERVER_SIDE_VALIDATION) {
										messagePanel.getErrorMessageAlert().createAlert("Invalid input data.");
									}
								}
							}
							getUsedArtifacts();
							cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
							showSearchingBusy(false);
						}
					});

				}  else {
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(userDefinedInput));
				}
			} else {
				messagePanel.getErrorMessageAlert().createAlert(message);
			}

		} else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
		}

	}

	private CQLValueSetTransferObject createValueSetTransferObject(String libraryID) {
		if(currentMatValueSet == null) {
			currentMatValueSet = new MatValueSet();
		}
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		matValueSetTransferObject.setCqlLibraryId(libraryID);

		String originalCodeListName = cqlWorkspaceView.getValueSetView().getUserDefinedInput().getValue(); 

		matValueSetTransferObject.setCqlQualityDataSetDTO(new CQLQualityDataSetDTO());
		matValueSetTransferObject.getCqlQualityDataSetDTO().setOriginalCodeListName(originalCodeListName);

		if(!cqlWorkspaceView.getValueSetView().getSuffixInput().getValue().isEmpty()){
			matValueSetTransferObject.getCqlQualityDataSetDTO().setSuffix(cqlWorkspaceView.getValueSetView().getSuffixInput().getValue());
			matValueSetTransferObject.getCqlQualityDataSetDTO().setName(originalCodeListName+" ("+cqlWorkspaceView.getValueSetView().getSuffixInput().getValue()+")");
		} else {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setName(originalCodeListName);
		}

		matValueSetTransferObject.getCqlQualityDataSetDTO().setRelease(EMPTY_STRING);
		String releaseValue = cqlWorkspaceView.getValueSetView().getReleaseListBox().getSelectedValue(); 
		if(!releaseValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setRelease(releaseValue);
		}

		matValueSetTransferObject.getCqlQualityDataSetDTO().setProgram(EMPTY_STRING);
		String programValue = cqlWorkspaceView.getValueSetView().getProgramListBox().getSelectedValue();
		if(!programValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setProgram(programValue);
		}

		CodeListSearchDTO codeListSearchDTO = new CodeListSearchDTO();
		codeListSearchDTO.setName(cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText());
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		matValueSetTransferObject.setMatValueSet(currentMatValueSet);
		matValueSetTransferObject.setCqlLibraryId(libraryID);
		matValueSetTransferObject.setUserDefinedText(cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText());
		return matValueSetTransferObject;
	}

	private void modifyUserDefinedValueSet() {
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
			getUsedArtifacts();
		} else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
		}
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
			cqlWorkspaceView.buildGeneralInformation();
			boolean isValidQDMVersion = cqlWorkspaceView.getCQLLeftNavBarPanelView().checkForIncludedLibrariesQDMVersion(true); //true because its a standalone library
			if(!isValidQDMVersion){
				messagePanel.getErrorMessageAlert().createAlert(INVALID_QDM_VERSION_IN_INCLUDES);
			} else {
				messagePanel.getErrorMessageAlert().clearAlert();
			}
			cqlWorkspaceView.getCqlGeneralInformationView().getLibraryNameValue().setText(cqlLibraryName);
			cqlWorkspaceView.getCqlGeneralInformationView().getComments().setText(cqlLibraryComment);
		}
		cqlWorkspaceView.setGeneralInfoHeading();
		cqlWorkspaceView.getCqlGeneralInformationView().getComments().setCursorPos(0);
	}

	private void appliedQDMEvent() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.hideInformationDropDown();
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQDM().setActive(true);
			cqlWorkspaceView.getValueSetView().getPasteButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
			currentSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			buildAppliedQDMTable();
		}		

		loadProgramsAndReleases();		
		cqlWorkspaceView.getValueSetView().setHeading("CQL Library Workspace > Value Sets", "subQDMAPPliedListContainerPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	private void buildAppliedQDMTable() {
		cqlWorkspaceView.buildAppliedQDM();
		boolean isEditable = MatContext.get().getLibraryLockService().checkForEditPermission();

		for(CQLQualityDataSetDTO valuset : cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQdmTableList()) {
			valuset.setUsed(true);
		}

		cqlWorkspaceView.getValueSetView().buildAppliedValueSetCellTable(cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQdmTableList(), isEditable);
		cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
		cqlWorkspaceView.getValueSetView().setWidgetsReadOnly(isEditable);
		getUsedArtifacts();
	}

	private void codesEvent() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_CODES;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getCodesLibrary().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_CODES;
			cqlWorkspaceView.buildCodes();
			cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService().checkForEditPermission());
			cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
			cqlWorkspaceView.getCodesView().setWidgetsReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
			getUsedCodes();
			boolean isEnabled = MatContext.get().getLibraryLockService().checkForEditPermission();
			cqlWorkspaceView.getCodesView().getPasteButton().setEnabled(isEnabled);
		}
		cqlWorkspaceView.getCodesView().setHeading("CQL Library Workspace > Codes", "codesContainerPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	private void getUsedCodes() {
		showSearchingBusy(true);

		MatContext.get().getLibraryService().getUsedCqlArtifacts(
				MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<GetUsedCQLArtifactsResult>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(GetUsedCQLArtifactsResult result) {
						showSearchingBusy(false);
						if(!result.getCqlErrors().isEmpty()) {
							for(CQLCode cqlCode : appliedCodeTableList){
								cqlCode.setUsed(false);
							}
						} else {
							for(CQLCode cqlCode : appliedCodeTableList){
								if (result.getUsedCQLcodes().contains(cqlCode.getDisplayName())) {
									cqlCode.setUsed(true);
								} else{
									cqlCode.setUsed(false);
								}
							}
						}

						if(cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedCodeTableList().size() > 0) {
							cqlWorkspaceView.getCodesView().getCelltable().redraw();
							cqlWorkspaceView.getCodesView().getListDataProvider().refresh();
						}
					}

				});
	}

	private void parameterEvent() {
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
		cqlWorkspaceView.buildParameterLibraryView();
		cqlWorkspaceView.getCQLParametersView().setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		cqlWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = cqlWorkspaceView.getCQLParametersView().getParameterAceEditor();
		cqlWorkspaceView.getCQLParametersView().setHeading("CQL Library Workspace > Parameter", "mainParamViewVerticalPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	private void includesEvent() {
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
		cqlWorkspaceView.getMainFlowPanel().clear();
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getIncludeView().setIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView()
				.getIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryMap()));
		cqlWorkspaceView.buildIncludesView();
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(new ArrayList<CQLLibraryDataSetObject>());
		cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
				MatContext.get().getLibraryLockService().checkForEditPermission(), true);

		cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText(EMPTY_STRING);
		cqlWorkspaceView.getIncludeView().getSearchTextBox().setText(EMPTY_STRING);
		((CQLStandaloneWorkSpaceView)cqlWorkspaceView).getCqlIncludeLibraryView().setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		cqlWorkspaceView.getIncludeView().setHeading("CQL Library Workspace > Includes", "IncludeSectionContainerPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	private void definitionEvent() {
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
		cqlWorkspaceView.buildDefinitionLibraryView();
		cqlWorkspaceView.getCQLDefinitionsView().setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		cqlWorkspaceView.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor();
		cqlWorkspaceView.getCQLDefinitionsView().setHeading("CQL Library Workspace > Definition", "mainDefViewVerticalPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	private void functionEvent() {
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
		cqlWorkspaceView.buildFunctionLibraryView();
		cqlWorkspaceView.getCQLFunctionsView()
		.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());

		cqlWorkspaceView.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor();
		cqlWorkspaceView.getCQLFunctionsView().setHeading("CQL Library Workspace > Function", "mainFuncViewVerticalPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	private void viewCqlEvent() {
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
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewCQL().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			cqlWorkspaceView.buildCQLFileView(MatContext.get().getLibraryLockService().checkForEditPermission());
			buildCQLView();
		}
		cqlWorkspaceView.getViewCQLView().setHeading("CQL Library Workspace > View CQL", "cqlViewCQL_Id");
		Mat.focusSkipLists("CqlComposer");
	}

	private void buildCQLView() {
		cqlWorkspaceView.getViewCQLView().getCqlAceEditor().setText(EMPTY_STRING);
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().getCQLLibraryFileData(MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<SaveUpdateCQLResult>() {
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if (result.isSuccess()) {
					buildCQLViewSuccess(result);
				}
				showSearchingBusy(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);
			}
		});

	}
	
	private void buildCQLViewSuccess(SaveUpdateCQLResult result) {
		if ((result.getCqlString() != null) && !result.getCqlString().isEmpty()) {
			cqlWorkspaceView.getViewCQLView().getCqlAceEditor().clearAnnotations();
			cqlWorkspaceView.getViewCQLView().getCqlAceEditor().removeAllMarkers();
			cqlWorkspaceView.getViewCQLView().getCqlAceEditor().redisplay();
			cqlWorkspaceView.getViewCQLView().getCqlAceEditor().setText(result.getCqlString());
			
			messagePanel.clearAlerts();

			List<CQLError> errors = new ArrayList<>(); 
			List<CQLError> warnings = new ArrayList<>(); 

			addErrorsAndWarningsForParentLibrary(result, errors, warnings);
			SharedCQLWorkspaceUtility.displayMessagesForViewCQL(result, cqlWorkspaceView.getViewCQLView().getCqlAceEditor(), messagePanel);
			cqlWorkspaceView.getViewCQLView().getCqlAceEditor().setAnnotations();
			cqlWorkspaceView.getViewCQLView().getCqlAceEditor().redisplay();
		}
		
		showSearchingBusy(false);
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
	
	private void unsetActiveMenuItem(String menuClickedBefore) {
		if (!getIsPageDirty()) {
			cqlWorkspaceView.resetMessageDisplay();
			if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_GENERAL_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getGeneralInformation().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterLibrary().setActive(false);
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().setSelectedIndex(-1);
				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getParamCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getParamCollapse().getElement()
					.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox().setSelectedIndex(-1);
				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineCollapse().getElement()
					.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionLibrary().setActive(false);
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getFuncNameListBox().setSelectedIndex(-1);
				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionCollapse().getElement()
					.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEW_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewCQL().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_APPLIED_QDM)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getAppliedQDM().setActive(false);
			}else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_CODES)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getCodesLibrary().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_INCLUDES_MENU)) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesLibrary().setActive(false);
				cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().setSelectedIndex(-1);
				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesCollapse().getElement()
					.setClassName("panel-collapse collapse");
				}
			}
		}
	}

	private void getAllIncludeLibraryList(final String searchText) {
		messagePanel.getErrorMessageAlert().clearAlert();
		messagePanel.getSuccessMessageAlert().clearAlert();
		messagePanel.getWarningMessageAlert().clearAlert();
		showSearchingBusy(true);

		MatContext.get().getCQLLibraryService().searchForIncludes(setId, cqlLibraryName, searchText,
				new AsyncCallback<SaveCQLLibraryResult>() {

			@Override
			public void onFailure(Throwable caught) {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);
			}

			@Override
			public void onSuccess(SaveCQLLibraryResult result) {
				showSearchingBusy(false);
				if (result != null && result.getCqlLibraryDataSetObjects().size() > 0) {
					cqlWorkspaceView.getCQLLeftNavBarPanelView()
					.setIncludeLibraryList(result.getCqlLibraryDataSetObjects());
					cqlWorkspaceView.buildIncludesView();
					cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
							MatContext.get().getLibraryLockService().checkForEditPermission(), false);

				} else {
					cqlWorkspaceView.buildIncludesView();
					cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
							MatContext.get().getLibraryLockService().checkForEditPermission(), false);
					if (!cqlWorkspaceView.getIncludeView().getSearchTextBox().getText().isEmpty())
						messagePanel.getErrorMessageAlert().createAlert(NO_LIBRARIES_RETURNED);
				}

				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox()
						.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else {
					messagePanel.getWarningMessageAlert().clearAlert();
				}
				cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
			}
		});

	}

	private void showSearchingBusy(final boolean busy) {
		showSearchBusyOnDoubleClick(busy);
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			switch(currentSection.toLowerCase()) {
			case(CQLWorkSpaceConstants.CQL_GENERAL_MENU): 
				cqlWorkspaceView.getCqlGeneralInformationView().setWidgetReadOnlyForCQLLibrary(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
				cqlWorkspaceView.getIncludeView().setReadOnly(!busy);				
			break;
			case(CQLWorkSpaceConstants.CQL_APPLIED_QDM): 
				cqlWorkspaceView.getValueSetView().setReadOnly(!busy);				
			break;
			case(CQLWorkSpaceConstants.CQL_CODES): 
				cqlWorkspaceView.getCodesView().setReadOnly(!busy);				
			break;
			case(CQLWorkSpaceConstants.CQL_PARAMETER_MENU): 
				cqlWorkspaceView.getCQLParametersView().setReadOnly(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_DEFINE_MENU): 
				cqlWorkspaceView.getCQLDefinitionsView().setReadOnly(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_FUNCTION_MENU): 
				cqlWorkspaceView.getCQLFunctionsView().setReadOnly(!busy);
			break;															  
			}

		}
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsLoading(busy);
	}

	private void showSearchBusyOnDoubleClick(boolean busy) {
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
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewCQL().setEnabled(!busy);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesLibrary().setEnabled(!busy);
	}

	private void clearAlias() {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
		setIsPageDirty(false);
		if ((cqlWorkspaceView.getIncludeView().getAliasNameTxtArea() != null)) {
			cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText(EMPTY_STRING);
		}
		if ((cqlWorkspaceView.getIncludeView().getViewCQLEditor().getText() != null)) {
			cqlWorkspaceView.getIncludeView().getViewCQLEditor().setText(EMPTY_STRING);
		}
		// Below lines are to clear Library search text box.
		if ((cqlWorkspaceView.getIncludeView().getSearchTextBox().getText() != null)) {
			cqlWorkspaceView.getIncludeView().getSearchTextBox().setText(EMPTY_STRING);
		}
		cqlWorkspaceView.getIncludeView().getSelectedObjectList().clear();
		cqlWorkspaceView.getIncludeView().setSelectedObject(null);
		cqlWorkspaceView.getIncludeView().setIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView()
				.getIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryMap()));
		unCheckAvailableLibraryCheckBox();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText(EMPTY_STRING);
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().setItemSelected(
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(), false);
		}

	}

	private void unCheckAvailableLibraryCheckBox() {
		List<CQLLibraryDataSetObject> availableLibraries = new ArrayList<>();
		availableLibraries = cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryList();
		for (int i = 0; i < availableLibraries.size(); i++) {
			availableLibraries.get(i).setSelected(false);
		}
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryList());
		cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
				MatContext.get().getLibraryLockService().checkForEditPermission(), false);
	}

	private void clickEventOnListboxes() {
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

	private void changeSectionSelection() {
		cqlWorkspaceView.hideInformationDropDown();
		switch (currentSection) {
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
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewCQL().setActive(false);
		break;
		default:
			break;
		}

		switch (nextSection) {
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			currentSection = nextSection;
		includesEvent();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesCollapse().getElement()
		.setClassName("panel-collapse collapse in");
		break;
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			currentSection = nextSection;
		functionEvent();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionCollapse().getElement()
		.setClassName("panel-collapse collapse in");
		break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			currentSection = nextSection;
		parameterEvent();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getParamCollapse().getElement()
		.setClassName("panel-collapse collapse in");
		break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			currentSection = nextSection;
		definitionEvent();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineCollapse().getElement()
		.setClassName("panel-collapse collapse in");
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

	private void clearViewIfDirtyNotSet() {
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

	private void getAttributesForDataType(final CQLFunctionArgument functionArg) {
		attributeService.getAllAttributesByDataType(functionArg.getQdmDataType(),
				new AsyncCallback<List<QDSAttributes>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(List<QDSAttributes> result) {
				cqlWorkspaceView.getCQLLeftNavBarPanelView().setAvailableQDSAttributeList(result);
				AddFunctionArgumentDialogBox.showArgumentDialogBox(functionArg, true,cqlWorkspaceView.getCQLFunctionsView(), messagePanel,MatContext.get().getLibraryLockService().checkForEditPermission());
			}

		});
	}

	private List<CQLIdentifierObject> getDefinitionList(List<CQLDefinition> definitionList) {

		List<CQLIdentifierObject> defineList = new ArrayList<>();

		for (int i = 0; i < definitionList.size(); i++) {
			CQLIdentifierObject definition = new CQLIdentifierObject(null, definitionList.get(i).getName(),definitionList.get(i).getId());
			defineList.add(definition);
		}
		return defineList;
	}

	private List<CQLIdentifierObject> getParamaterList(List<CQLParameter> parameterList) {

		List<CQLIdentifierObject> paramList = new ArrayList<>();

		for (int i = 0; i < parameterList.size(); i++) {
			CQLIdentifierObject parameter = new CQLIdentifierObject(null, parameterList.get(i).getName(),parameterList.get(i).getId());
			paramList.add(parameter);
		}
		return paramList;
	}

	private List<CQLIdentifierObject> getFunctionList(List<CQLFunctions> functionList) {

		List<CQLIdentifierObject> funcList = new ArrayList<>();

		for (int i = 0; i < functionList.size(); i++) {
			CQLIdentifierObject function = new CQLIdentifierObject(null, functionList.get(i).getName(),functionList.get(i).getId());
			funcList.add(function);
		}
		return funcList;
	}


	private List<String> getIncludesList(List<CQLIncludeLibrary> includesList) {
		List<String> incLibList = new ArrayList<>();
		for (int i = 0; i < includesList.size(); i++) {
			incLibList.add(includesList.get(i).getAliasName());
		}
		return incLibList;
	}

	private void displayEmpty() {
		panel.clear();
		panel.add(emptyWidget);
	}

	@Override
	public Widget getWidget() {
		panel.setStyleName("contentPanel");
		return panel;
	}

	public static CQLWorkspaceView getSearchDisplay() {
		return cqlWorkspaceView;
	}

	private void setAppliedValueSetListInTable(List<CQLQualityDataSetDTO> valueSetList) {
		appliedValueSetTableList.clear();
		List<CQLQualityDataSetDTO> allValuesets = new ArrayList<>();
		for (CQLQualityDataSetDTO dto : valueSetList) {
			if (dto.isSuppDataElement())
				continue;
			allValuesets.add(dto);
		}
		MatContext.get().setValuesets(allValuesets);
		for (CQLQualityDataSetDTO valueset : allValuesets) {
			if((valueset.getOid().equals("419099009") || valueset.getOid().equals("21112-8") 
					|| (valueset.getType() !=null) && valueset.getType().equalsIgnoreCase("code"))){
				continue;
			}
			appliedValueSetTableList.add(valueset);
		}
		cqlWorkspaceView.getValueSetView().buildAppliedValueSetCellTable(appliedValueSetTableList, MatContext.get().getLibraryLockService().checkForEditPermission());
		cqlWorkspaceView.getCQLLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
	}

	private void resetMessagesAndSetPageDirty(boolean isPageDirty) {
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			cqlWorkspaceView.resetMessageDisplay();
			setIsPageDirty(isPageDirty);
		}
	}

	private void loadProgramsAndReleases() {
		CQLAppliedValueSetUtility.loadProgramsAndReleases(cqlWorkspaceView.getValueSetView().getProgramListBox(), cqlWorkspaceView.getValueSetView().getReleaseListBox());
	}

	private void deleteConfirmationDialogBoxYesClicked() {
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
			deleteDefinition();
			deleteConfirmationDialogBox.hide();
		} else if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null
				&& cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId() == null) {
			deleteFunction();
			deleteConfirmationDialogBox.hide();
		}else if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId() != null) {
			deleteFunctionArgument();
			deleteConfirmationDialogBox.hide();
		} else if (cqlWorkspaceView.getCQLLeftNavBarPanelView()
				.getCurrentSelectedParamerterObjId() != null) {
			deleteParameter();
			deleteConfirmationDialogBox.hide();
		} else if (cqlWorkspaceView.getCQLLeftNavBarPanelView()
				.getCurrentSelectedIncLibraryObjId() != null) {
			deleteInclude();
			deleteConfirmationDialogBox.hide();
		}  else if(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedValueSetObjId() != null){
			checkAndDeleteValueSet();
			deleteConfirmationDialogBox.hide();
		} else if(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedCodesObjId() != null){
			deleteCode();
			deleteConfirmationDialogBox.hide();
		}
	}

	private void deleteConfirmationDialogBoxNoClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		deleteConfirmationDialogBox.hide();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
	}

	private void leftNavParameterNameListBoxKeyPressEvent(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
			DomEvent.fireNativeEvent(
					Document.get().createDblClickEvent(cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(), 0, 0, 0, 0, false, false,false, false), cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox());
		}
	}

	private void leftNavParameterNameListBoxDoubleClickEvent(DoubleClickEvent event) {
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIsLoading()) {
			event.stopPropagation();
		} else {
			showSearchBusyOnDoubleClick(true);
			cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
			cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
			parameterViewHideEvent();
			resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());

			cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(true);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
			if (getIsPageDirty()) {
				showSearchBusyOnDoubleClick(false);
				showUnsavedChangesWarning();
			} else {
				int selectedIndex = cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex();
				if (selectedIndex != -1) {
					final String selectedParamID = cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterNameListBox().getValue(selectedIndex);
					cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedParamerterObjId(selectedParamID);
					if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterMap().get(selectedParamID) != null) {
						cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setTitle("Delete");
						cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
						cqlWorkspaceView.getCQLParametersView().setWidgetReadOnly(false);
						cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
						cqlWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
						MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(MatContext.get().getCurrentCQLLibraryId(),
								new AsyncCallback<GetUsedCQLArtifactsResult>() {

									@Override
									public void onFailure(Throwable caught) {
										showSearchBusyOnDoubleClick(false);
										Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
									}

									@Override
									public void onSuccess(GetUsedCQLArtifactsResult result) {
										parameterListBoxDoubleClickEventSuccess(selectedParamID, result);
									}

								});

					} else {
						showSearchBusyOnDoubleClick(false);
					}
				} else {
					showSearchBusyOnDoubleClick(false);
				}

				cqlWorkspaceView.resetMessageDisplay();

			}
		}
		cqlWorkspaceView.getCQLParametersView().getMainParamViewVerticalPanel().setFocus(true);
	}

	private void parameterListBoxDoubleClickEventSuccess(final String selectedParamID, GetUsedCQLArtifactsResult result) {
		showSearchBusyOnDoubleClick(false);
		cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterMap().get(selectedParamID).getName());
		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().setText(cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterMap().get(selectedParamID).getLogic());
		cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea().setText(cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterMap().get(selectedParamID).getCommentString());

		boolean isReadOnly = cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterMap().get(selectedParamID).isReadOnly();
		CQLParameter currentParameter = cqlWorkspaceView.getCQLLeftNavBarPanelView().getParameterMap().get(selectedParamID);
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			cqlWorkspaceView.getCQLParametersView().setWidgetReadOnly(!isReadOnly);
			if (!currentParameter.isReadOnly()) {
				if (!result.getCqlErrors().isEmpty() || !result.getUsedCQLParameters().contains(currentParameter.getName())) {
					cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
				} else{
					cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
				}
			}
		}

		SharedCQLWorkspaceUtility.setCQLWorkspaceExceptionAnnotations(currentParameter.getName(), result.getCqlErrorsPerExpression(), result.getCqlWarningsPerExpression(), curAceEditor);
		
	}
	
	private void parameterViewShowEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		showCompleteCQL(cqlWorkspaceView.getCQLParametersView().getViewCQLAceEditor());
	}

	private void parameterViewHideEvent() {
		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
		resetAceEditor(cqlWorkspaceView.getCQLParametersView().getViewCQLAceEditor());
	}

	private void parameterViewSaveButtonClicked() {
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			addAndModifyParameters();
			cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setFocus(true);
		}
	}

	private void parameterViewEraseButtonClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
		eraseParameter(); 
		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().focus();
	}

	private void parameterViewDeleteButtonClicked() {
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
		deleteConfirmationDialogBox.getMessageAlert().createAlert(buildSelectedToDeleteWithConfirmationMessage(PARAMETER, cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().getValue()));
		deleteConfirmationDialogBox.show();
	}

	private void keyUpEvent() {
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			cqlWorkspaceView.resetMessageDisplay();
			setIsPageDirty(true);
		}
	}

	private void parameterViewAddNewClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
		if (getIsPageDirty()) {
			showUnsavedChangesWarning();
		} else {
			addNewParameter();
		}
		cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setFocus(true);
	}

	private void parameterCommentBlurEvent() {
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

	private void cqlDefinitionsViewContextDefinePATRadioBtnValueChangedEvent() {
		setIsPageDirty(true);
		if (cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
			cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);
		} else {
			cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(true);
		}
	}

	private void cqlDefinitionsViewContextDefinePOPRadioBtnValueChangedEvent() {
		setIsPageDirty(true);
		if (cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn().getValue()) {
			cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(false);
		} else {
			cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
		}
	}

	private void cqlFunctionViewContextFuncPATRadioBtnValueChangedEvent() {
		setIsPageDirty(true);
		if (cqlWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().getValue()) {
			cqlWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
		} else {
			cqlWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(true);
		}
	}

	private void cqlFunctionViewContextFuncPOPRadioBtnValueChangedEvent() {
		setIsPageDirty(true);
		if (cqlWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn().getValue()) {
			cqlWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(false);
		} else {
			cqlWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(true);
		}
	}

	private void editIncludedLibraryDialogApplyButtonClicked(
			final EditIncludedLibraryDialogBox editIncludedLibraryDialogBox) {
		messagePanel.getErrorMessageAlert().clearAlert();
		messagePanel.getSuccessMessageAlert().clearAlert();
		if(editIncludedLibraryDialogBox.getSelectedList().size() > 0){
			final CQLIncludeLibrary toBeModified = cqlWorkspaceView.getCQLLeftNavBarPanelView()
					.getIncludeLibraryMap()
					.get(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId());

			final CQLLibraryDataSetObject dto = editIncludedLibraryDialogBox.getSelectedList().get(0);
			if (dto != null) {
				final CQLIncludeLibrary currentObject = new CQLIncludeLibrary(dto);

				MatContext.get().getCQLLibraryService().saveIncludeLibrayInCQLLookUp(
						MatContext.get().getCurrentCQLLibraryId(), toBeModified, currentObject,
						cqlWorkspaceView.getCQLLeftNavBarPanelView().getViewIncludeLibrarys(),
						new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable arg0) {

								editIncludedLibraryDialogBox.getDialogModal().hide();
								editIncludedLibraryDialogBox.getErrorMessageAlert().clearAlert();
								Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								editIncludedLibraryDialogBox.getErrorMessageAlert().clearAlert();
								CQLAppliedValueSetUtility.loadReleases(cqlWorkspaceView.getValueSetView().getReleaseListBox(), cqlWorkspaceView.getValueSetView().getProgramListBox());
								if (result != null) {
									if (result.isSuccess()) {
										cqlWorkspaceView.getCQLLeftNavBarPanelView().setViewIncludeLibrarys(
												result.getCqlModel().getCqlIncludeLibrarys());
										cqlWorkspaceView.getCQLLeftNavBarPanelView().udpateIncludeLibraryMap();
										MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
										MatContext.get().setIncludedValues(result);
										MatContext.get().setCQLModel(result.getCqlModel());
										editIncludedLibraryDialogBox.getDialogModal().hide();
										DomEvent.fireNativeEvent(
												Document.get()
												.createDblClickEvent(cqlWorkspaceView.getCQLLeftNavBarPanelView()
														.getIncludesNameListbox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
														false),
												cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox());
										String libraryNameWithVersion = result.getIncludeLibrary().getCqlLibraryName() + " v" +result.getIncludeLibrary().getVersion();
										messagePanel.getSuccessMessageAlert().createAlert(libraryNameWithVersion + " has been successfully saved as the alias " + result.getIncludeLibrary().getAliasName());
									}
								}

							}

						});

			}
		} else {
			editIncludedLibraryDialogBox.getErrorMessageAlert().clearAlert();
			editIncludedLibraryDialogBox.getErrorMessageAlert().createAlert(NO_LIBRARY_TO_REPLACE);
		}
	}

	private void includeViewSaveModifyClicked() {
		messagePanel.getErrorMessageAlert().clearAlert();
		messagePanel.getSuccessMessageAlert().clearAlert();
		final EditIncludedLibraryDialogBox editIncludedLibraryDialogBox = new EditIncludedLibraryDialogBox("Replace Library");
		editIncludedLibraryDialogBox.findAvailableLibraries(currentIncludeLibrarySetId, currentIncludeLibraryId);
		editIncludedLibraryDialogBox.getApplyButton().addClickHandler(event -> editIncludedLibraryDialogApplyButtonClicked(editIncludedLibraryDialogBox));
	}

	private void cqlLeftNavParIncludesNameListBoxKeyPressedEvent(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
			DomEvent.fireNativeEvent(Document.get().createDblClickEvent(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,false),cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox());
		}
	}

	private void cqlLeftNavBarIncludesNameListBoxDoubleClickEvent(DoubleClickEvent event) {
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIsLoading()) {
			event.stopPropagation();
		} else {
			cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(true);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);

			if (getIsPageDirty()) {
				showUnsavedChangesWarning();
			} else {
				int selectedIndex = cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex();
				if (selectedIndex != -1) {
					final String selectedIncludeLibraryID = cqlWorkspaceView.getCQLLeftNavBarPanelView()
							.getIncludesNameListbox().getValue(selectedIndex);
					cqlWorkspaceView.getCQLLeftNavBarPanelView()
					.setCurrentSelectedIncLibraryObjId(selectedIncludeLibraryID);
					if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryMap().get(selectedIncludeLibraryID) != null) {
						MatContext.get().getCQLLibraryService().findCQLLibraryByID(
								cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryMap()
								.get(selectedIncludeLibraryID).getCqlLibraryId(),
								new AsyncCallback<CQLLibraryDataSetObject>() {

									@Override
									public void onSuccess(CQLLibraryDataSetObject result) {
										if (result != null) {
											currentIncludeLibrarySetId = result.getCqlSetId();
											currentIncludeLibraryId = result.getId();
											cqlWorkspaceView.getIncludeView().buildIncludesReadOnlyView();

											cqlWorkspaceView.getIncludeView().getAliasNameTxtArea()
											.setText(cqlWorkspaceView.getCQLLeftNavBarPanelView()
													.getIncludeLibraryMap()
													.get(selectedIncludeLibraryID)
													.getAliasName());
											cqlWorkspaceView.getIncludeView().getViewCQLEditor()
											.setText(result.getCqlText());
											cqlWorkspaceView.getIncludeView().getOwnerNameTextBox()
											.setText(cqlWorkspaceView.getCQLLeftNavBarPanelView()
													.getOwnerName(result));
											cqlWorkspaceView.getIncludeView().getCqlLibraryNameTextBox()
											.setText(result.getCqlName());

											cqlWorkspaceView.getIncludeView().getSaveModifyButton().setEnabled(false);														
											cqlWorkspaceView.getIncludeView().getDeleteButton().setEnabled(false);

											cqlWorkspaceView.getIncludeView().getSaveModifyButton().setEnabled(false);
											if (MatContext.get().getLibraryLockService()
													.checkForEditPermission()) {
												cqlWorkspaceView.getIncludeView().setWidgetReadOnly(false);
												cqlWorkspaceView.getIncludeView().getSaveModifyButton().setEnabled(true);

												MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(
														MatContext.get().getCurrentCQLLibraryId(),
														new AsyncCallback<GetUsedCQLArtifactsResult>() {

															@Override
															public void onFailure(Throwable caught) {
																Window.alert(MatContext.get()
																		.getMessageDelegate()
																		.getGenericErrorMessage());
															}

															@Override
															public void onSuccess(
																	GetUsedCQLArtifactsResult result) {

																CQLIncludeLibrary cqlIncludeLibrary = cqlWorkspaceView
																		.getCQLLeftNavBarPanelView()
																		.getIncludeLibraryMap()
																		.get(selectedIncludeLibraryID);
																
																
																AceEditor editor = cqlWorkspaceView.getIncludeView().getViewCQLEditor(); 
																editor.clearAnnotations();
																editor.removeAllMarkers();
																
																String formattedName = cqlIncludeLibrary.getCqlLibraryName() + "-" + cqlIncludeLibrary.getVersion();												
																List<CQLError> errorsForLibrary = result.getLibraryNameErrorsMap().get(formattedName);
																List<CQLError> warningsForLibrary = result.getLibraryNameWarningsMap().get(formattedName);											
																SharedCQLWorkspaceUtility.createCQLWorkspaceAnnotations(errorsForLibrary, SharedCQLWorkspaceUtility.ERROR_PREFIX, AceAnnotationType.ERROR, editor);
																SharedCQLWorkspaceUtility.createCQLWorkspaceAnnotations(warningsForLibrary, SharedCQLWorkspaceUtility.WARNING_PREFIX, AceAnnotationType.WARNING, editor);
																editor.setAnnotations();

																if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLLibraries().contains(
																		cqlIncludeLibrary.getCqlLibraryName()+ "-"+ cqlIncludeLibrary.getVersion()+ "|"
																				+ cqlIncludeLibrary.getAliasName())) {
																	cqlWorkspaceView.getIncludeView().getDeleteButton().setEnabled(true);
																}
															}

														});
											}
										}
									}

									@Override
									public void onFailure(Throwable caught) {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
									}
								});

						cqlWorkspaceView.getIncludeView().setSelectedObject(
								cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryMap()
								.get(selectedIncludeLibraryID).getCqlLibraryId());
						cqlWorkspaceView.getIncludeView()
						.setIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView()
								.getIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView()
										.getIncludeLibraryMap()));
						cqlWorkspaceView.getIncludeView().getSelectedObjectList().clear();
					}
				}
				cqlWorkspaceView.resetMessageDisplay();

			}

		}
	}

	private void cqlDefinitionsShowEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		showCompleteCQL(cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor());
	}

	private void cqlDefinitionsHideEvent() {
		cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
		resetAceEditor(cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor());
	}

	private void definitionSaveButtonClicked() {
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse());
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			addAndModifyDefintions();
			cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
			cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
			cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setFocus(true);
		}
	}

	private void definitionEraseButtonClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
		cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse());
		eraseDefinition(); 
		cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().focus();
	}

	private void definitionDeleteButtonClicked() {
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse());
		deleteConfirmationDialogBox.getMessageAlert().createAlert(buildSelectedToDeleteMessage(DEFINITION, cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().getValue()));
		deleteConfirmationDialogBox.show();
	}

	private void definitionsAddNewClicked() {
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

	private void definitionCommentBlurEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.NONE);
		String comment = cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea().getText();
		if(validator.isCommentMoreThan250Characters(comment)){
			cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
		} else if(validator.doesCommentContainInvalidCharacters(comment)){
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_COMMENT_CHARACTERS());
			cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
		}
	}

	private void cqlLeftNavBarDefineNameListBoxKeyPressed(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
			DomEvent.fireNativeEvent(Document.get().createDblClickEvent(cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex(), 0, 0, 0, 0, false, false, false, false),cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox());
		}
	}

	private void cqlLeftNavBarDefineNameListBoxDoubleClickEvent(DoubleClickEvent event) {
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIsLoading()) {
			event.stopPropagation();
		} else {
			showSearchBusyOnDoubleClick(true);
			cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
			cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().removeAllMarkers();

			resetAceEditor(cqlWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor());
			resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse());
			cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(EMPTY_STRING);

			cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(true);
			if (getIsPageDirty()) {
				showSearchBusyOnDoubleClick(false);
				showUnsavedChangesWarning();
			} else {
				int selectedIndex = cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex();
				if (selectedIndex != -1) {
					final String selectedDefinitionID = cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefineNameListBox().getValue(selectedIndex);
					cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(selectedDefinitionID);

					if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID) != null) {
						cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setTitle("Delete");

						cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
						cqlWorkspaceView.getCQLDefinitionsView().setWidgetReadOnly(false);
						cqlWorkspaceView.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
						
						MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(MatContext.get().getCurrentCQLLibraryId(),
								new AsyncCallback<GetUsedCQLArtifactsResult>() {

									@Override
									public void onFailure(Throwable caught) {
										showSearchBusyOnDoubleClick(false);
										cqlWorkspaceView.getCQLDefinitionsView().setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
										Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
									}

									@Override
									public void onSuccess(GetUsedCQLArtifactsResult result) {
										definitionListBoxDoubleClickEventSuccess(selectedDefinitionID, result);
									}
								});

					} else {
						showSearchBusyOnDoubleClick(false);
					}
				}else {
					showSearchBusyOnDoubleClick(false);
				}

				cqlWorkspaceView.resetMessageDisplay();
			}
		}

		cqlWorkspaceView.getCQLDefinitionsView().getMainDefineViewVerticalPanel().setFocus(true);
	}

	private void definitionListBoxDoubleClickEventSuccess(final String selectedDefinitionID, GetUsedCQLArtifactsResult result) {
		showSearchBusyOnDoubleClick(false);
		boolean isReadOnly = cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).isSupplDataElement();

		CQLDefinition currentDefinition = cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID);

		cqlWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setText(cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getName());
		cqlWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setText(cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getLogic());
		cqlWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea().setText(cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getCommentString());

		boolean isNamePatient = cqlWorkspaceView.getCQLLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getContext().equalsIgnoreCase("patient");
		
		cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(isNamePatient);
		cqlWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(!isNamePatient);

		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			cqlWorkspaceView.getCQLDefinitionsView().setWidgetReadOnly(!isReadOnly);
			if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLDefinitions().contains(currentDefinition.getName())) {
				cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(true);
			} else {
				cqlWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
			}
			
		}

		SharedCQLWorkspaceUtility.setCQLWorkspaceExceptionAnnotations(currentDefinition.getName(), result.getCqlErrorsPerExpression(), result.getCqlWarningsPerExpression(), curAceEditor);

		if(result.getCqlErrors().isEmpty() && result.getExpressionReturnTypeMap() != null){
			cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getExpressionReturnTypeMap().get(currentDefinition.getName()));
			cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getExpressionReturnTypeMap().get(currentDefinition.getName()));

		} else {
			cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(EMPTY_STRING);
			cqlWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
		}
	}
	
	private void codesViewPastButtonClicked(ClickEvent event) {
		if(MatContext.get().getLibraryLockService().checkForEditPermission()) {
			pasteCodes();
		} else {
			event.preventDefault();
		}
	}

	private void codesViewClearButtonClicked() {
		if (!cqlWorkspaceView.getCodesView().getIsLoading()) {
			cqlWorkspaceView.resetMessageDisplay();
			cqlWorkspaceView.getCodesView().clearSelectedCheckBoxes();
		}
	}

	private void codesViewRetrieveFromVSACButtonClicked() {
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			cqlWorkspaceView.resetMessageDisplay();
			if (!isCodeModified) {
				searchCQLCodesInVsac();
			}
			cqlWorkspaceView.getCodesView().getCodeInput().setFocus(true);
		}
	}

	private void codesViewSaveButtonClicked() {
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			MatContext.get().clearDVIMessages();
			cqlWorkspaceView.resetMessageDisplay();
			if(isCodeModified && modifyCQLCode != null) {
				modifyCodes();
			} else if (null != cqlWorkspaceView.getCodesView().getCodeSearchInput().getValue() 
					&& !cqlWorkspaceView.getCodesView().getCodeSearchInput().getValue().isEmpty()) {
				addNewCodes();	
			}

			cqlWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
		}
	}

	private void codesViewCancelButtonClicked() {
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			isCodeModified = false;
			cqlWorkspaceView.resetMessageDisplay();
			cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
			cqlWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
		}
	}

	private void includeViewSearchButtonClicked() {
		getAllIncludeLibraryList(cqlWorkspaceView.getIncludeView().getSearchTextBox().getText().trim());
		cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
	}

	private void includesViewFocusPanelKeyDownEvent(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			cqlWorkspaceView.getIncludeView().getSearchButton().click();
		}
	}

	private void includesViewSaveButtonClicked() {
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			addIncludeLibraryInCQLLookUp();
			cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
		}
	}

	private void includesViewDeleteButtonClicked() {
		deleteConfirmationDialogBox.getMessageAlert().createAlert(buildSelectedToDeleteWithConfirmationMessage(LIBRARY, cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().getValue()));
		deleteConfirmationDialogBox.show();
		cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
	}

	private void includesViewCloseButtonClicked() {
		currentIncludeLibrarySetId =  null;
		cqlWorkspaceView.getCQLLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText(EMPTY_STRING);
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
			cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().setItemSelected(
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(),
					false);
		}
		cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText(EMPTY_STRING);
		cqlWorkspaceView.buildIncludesView();
		SaveCQLLibraryResult cqlLibrarySearchModel = new SaveCQLLibraryResult();
		cqlLibrarySearchModel
		.setCqlLibraryDataSetObjects(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryList());
		cqlWorkspaceView.getIncludeView().setIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView()
				.getIncludedList(cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludeLibraryMap()));
		cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(cqlLibrarySearchModel,
				MatContext.get().getLibraryLockService().checkForEditPermission(), false);
		cqlWorkspaceView.getIncludeView()
		.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIncludesNameListbox()
				.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
			messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
		} else {
			messagePanel.getWarningMessageAlert().clearAlert();
		}
		cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
	}

	private void includesViewEraseButtonClicked() {
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

	private void functionCommentBlurEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.NONE);
		String comment = cqlWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().getText();
		if(validator.isCommentMoreThan250Characters(comment)){
			cqlWorkspaceView.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
		} else if(validator.doesCommentContainInvalidCharacters(comment)){
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_COMMENT_CHARACTERS());
			cqlWorkspaceView.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
		}
	}

	private void leftNavBarFuncNameListBoxKeyPressedEvent(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
			DomEvent.fireNativeEvent(
					Document.get()
					.createDblClickEvent(cqlWorkspaceView.getCQLLeftNavBarPanelView()
							.getFuncNameListBox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
							false),
					cqlWorkspaceView.getCQLLeftNavBarPanelView().getFuncNameListBox());
		}
	}

	private void leftNavBarFuncNameListBoxDoubleClickedEvent(DoubleClickEvent event) {
		if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getIsLoading()) {
			event.stopPropagation();
		} else {
			showSearchBusyOnDoubleClick(true);
			cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
			cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
			cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(true);
			cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);

			resetAceEditor(cqlWorkspaceView.getCQLFunctionsView().getViewCQLAceEditor());
			resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());

			cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(EMPTY_STRING);


			if (getIsPageDirty()) {
				showSearchBusyOnDoubleClick(false);
				showUnsavedChangesWarning();
			} else {
				int selectedIndex = cqlWorkspaceView.getCQLLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex();
				if (selectedIndex != -1) {
					final String selectedFunctionId = cqlWorkspaceView.getCQLLeftNavBarPanelView().getFuncNameListBox().getValue(selectedIndex);
					cqlWorkspaceView.getCQLLeftNavBarPanelView().setCurrentSelectedFunctionObjId(selectedFunctionId);

					if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId) != null) {
						cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
						cqlWorkspaceView.getCQLFunctionsView().setWidgetReadOnly(false);
						cqlWorkspaceView.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
						// load most recent used cql artifacts
						MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(MatContext.get().getCurrentCQLLibraryId(),
								new AsyncCallback<GetUsedCQLArtifactsResult>() {

									@Override
									public void onFailure(Throwable caught) {
										showSearchingBusy(false);
										cqlWorkspaceView.getCQLFunctionsView().setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
										Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
									}

									@Override
									public void onSuccess(GetUsedCQLArtifactsResult result) {
										functionListBoxDoubleClickEventSuccess(selectedFunctionId, result);
									}

								});

					} else {
						showSearchBusyOnDoubleClick(false);
					}
				} else {
					showSearchBusyOnDoubleClick(false);
				}
				if (cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
					CQLFunctions selectedFunction = cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionMap().get(cqlWorkspaceView.getCQLLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
					if (selectedFunction.getArgumentList() != null) {
						cqlWorkspaceView.getCQLFunctionsView().getFunctionArgumentList().clear();
						cqlWorkspaceView.getCQLFunctionsView().getFunctionArgumentList().addAll(selectedFunction.getArgumentList());
					} else {
						cqlWorkspaceView.getCQLFunctionsView().getFunctionArgumentList().clear();
					}
				}
				cqlWorkspaceView.resetMessageDisplay();
			}
			cqlWorkspaceView.getCQLFunctionsView().createAddArgumentViewForFunctions(cqlWorkspaceView.getCQLFunctionsView().getFunctionArgumentList(),
					MatContext.get().getLibraryLockService().checkForEditPermission());

		}

		cqlWorkspaceView.getCQLFunctionsView().getMainFunctionVerticalPanel().setFocus(true);
	}
	
	private void functionListBoxDoubleClickEventSuccess(final String selectedFunctionId, GetUsedCQLArtifactsResult result) {
		showSearchBusyOnDoubleClick(false);
		cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setText(cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getName());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().setText(cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getLogic());
		cqlWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().setText(cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getCommentString());

		boolean isNamePatient = cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getContext().equalsIgnoreCase("patient");
		
		cqlWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(isNamePatient);
		cqlWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(!isNamePatient);

		CQLFunctions currentFunction = cqlWorkspaceView.getCQLLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId);
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			cqlWorkspaceView.getCQLFunctionsView().setWidgetReadOnly(true);

			if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLFunctions().contains(currentFunction.getName())) {
				cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
			} else {
				cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
			}
		}

		SharedCQLWorkspaceUtility.setCQLWorkspaceExceptionAnnotations(currentFunction.getName(), result.getCqlErrorsPerExpression(), 
				result.getCqlWarningsPerExpression(), cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor());
		
		if(result.getCqlErrors().isEmpty() && result.getExpressionReturnTypeMap() != null){
			cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(result.getExpressionReturnTypeMap().get(currentFunction.getName()));
			cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getExpressionReturnTypeMap().get(currentFunction.getName()));

		} else {
			cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(EMPTY_STRING);
			cqlWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
		}
	}

	private void cqlFunctionsViewShowEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		showCompleteCQL(cqlWorkspaceView.getCQLFunctionsView().getViewCQLAceEditor());
	}

	private void cqlFunctionsViewHideEvent() {
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
		resetAceEditor(cqlWorkspaceView.getCQLFunctionsView().getViewCQLAceEditor());
	}

	private void functionsViewInsertButtonClicked() {
		buildInsertPopUp();
		cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().focus();
	}

	private void functionsViewSaveButtonClicked() {
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			addAndModifyFunction();
			cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setFocus(true);
		}
	}

	private void functionsViewEraseButtonClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
		eraseFunction();
		cqlWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().focus();
	}

	private void functionsViewAddNewArgumentClicked() {
		cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
		cqlWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
		CQLFunctionArgument addNewFunctionArgument = new CQLFunctionArgument();
		AddFunctionArgumentDialogBox.showArgumentDialogBox(addNewFunctionArgument, false, cqlWorkspaceView.getCQLFunctionsView(), messagePanel,MatContext.get().getLibraryLockService().checkForEditPermission());
		setIsPageDirty(true);
	}

	private void functionsViewDeleteButtonClicked() {
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
		deleteConfirmationDialogBox.getMessageAlert().createAlert(buildSelectedToDeleteMessage(FUNCTION, cqlWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().getValue()));
		deleteConfirmationDialogBox.show();
	}

	private void functionsViewAddNewClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(false);
		resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
		if (getIsPageDirty()) {
			showUnsavedChangesWarning();
		} else {
			addNewFunction();
		}
		cqlWorkspaceView.getCQLFunctionsView().getMainFunctionVerticalPanel().setFocus(true);
	}

	private void leftNavBarIncludesLibraryClicked(ClickEvent event) {
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCQLLeftNavBarPanelView().setIsDoubleClick(false);
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
			showUnsavedChangesWarning();
			event.stopPropagation();
		} else {
			includesEvent();
		}
	}

	private void leftNavBarCodesClicked(ClickEvent event) {
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

	private void leftNavbarAppliedQDMClicked() {
		appliedQDMEvent();
		cqlWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
	}

	private void leftNavBarParametersClicked(ClickEvent event) {
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

	private void leftNavBarDefinitionsClicked(ClickEvent event) {
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

	private void leftNavBarFunctionClicked(ClickEvent event) {
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

	private void leftNavBarViewCQLClicked() {
		cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
		viewCqlEvent();
	}

	private void valueSetViewCancelQDMButtonClicked() {
		cqlWorkspaceView.resetMessageDisplay();
		isModified = false;
		cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
		cqlWorkspaceView.getValueSetView().getOIDInput().setFocus(true);

		previousIsProgramListBoxEnabled = isProgramListBoxEnabled;
		isProgramListBoxEnabled = true; 

		loadProgramsAndReleases(); 
		alert508StateChanges();
	}

	private void valueSetViewUpdateFromVSACButtonClicked() {
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			cqlWorkspaceView.resetMessageDisplay();
			updateVSACValueSets();
			cqlWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
		}
	}

	private void valueSetViewRetrieveFromVSACClicked() {
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
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
				cqlWorkspaceView.getValueSetView().getOIDInput().setFocus(true); 
			}
		}
	}

	private void valueSetViewSaveButtonClicked() {
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
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

	private void valueSetViewUserDefinedInputChangedEvent() {
		cqlWorkspaceView.resetMessageDisplay();
		isUserDefined = cqlWorkspaceView.getValueSetView().validateUserDefinedInput();
	}

	private void valueSetViewClearButtonClicked() {
		if(!cqlWorkspaceView.getValueSetView().getIsLoading()){
			cqlWorkspaceView.resetMessageDisplay();
			cqlWorkspaceView.getValueSetView().clearSelectedCheckBoxes();
		}
	}

	private void valueSetViewPasteButtonClicked(ClickEvent event) {
		if(MatContext.get().getLibraryLockService().checkForEditPermission()) {
			pasteValueSets();
		} else {
			event.preventDefault();
		}
	}

	private void valueSetViewReleaseListBoxChangedEvent() {
		isRetrieveButtonEnabled = true;
		cqlWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);				
		previousIsApplyButtonEnabled = isApplyButtonEnabled;
		isApplyButtonEnabled = false;
		cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(isApplyButtonEnabled);
		alert508StateChanges();
	}

	private void valueSetViewProgramListBoxChangedEvent() {
		isRetrieveButtonEnabled = true;
		cqlWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);

		previousIsApplyButtonEnabled = isApplyButtonEnabled;
		isApplyButtonEnabled = false; 
		cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(isApplyButtonEnabled);
		CQLAppliedValueSetUtility.loadReleases(cqlWorkspaceView.getValueSetView().getReleaseListBox(), cqlWorkspaceView.getValueSetView().getProgramListBox());
		alert508StateChanges();
	}

	private void viewCQLExportErrorFileClicked() {
		if(MatContext.get().getLibraryLockService().checkForEditPermission()) {
			String url = GWT.getModuleBaseURL() + "export?libraryid=" + MatContext.get().getCurrentCQLLibraryId() + "&format=errorFileStandAlone";
			Window.open(url + "&type=save", "_self", "");
		}
	}

	private void warningConfirmationYesButtonClicked() {
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

	private void warningConfirmationNoButtonClicked() {
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
}