package mat.client.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.shared.event.HideEvent;
import org.gwtbootstrap3.client.shared.event.HideHandler;
import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import mat.shared.CQLErrors;
import mat.shared.CQLIdentifierObject;
import mat.shared.ConstantMessages;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.StringUtility;
import mat.shared.cql.error.InvalidLibraryException;

public class CQLStandaloneWorkSpacePresenter extends AbstractCQLWorkspacePresenter implements MatPresenter {
	private static CQLStandaloneWorkSpaceView cqlStandaloneWorkspaceView;
	private SimplePanel emptyWidget = new SimplePanel();
	private boolean isCQLWorkSpaceLoaded = false;
	private String cqlLibraryName;

	private final CQLLibraryServiceAsync cqlService = MatContext.get().getCQLLibraryService();



	public CQLStandaloneWorkSpacePresenter(final CQLStandaloneWorkSpaceView srchDisplay) {
		cqlStandaloneWorkspaceView = srchDisplay;
		emptyWidget.add(new Label("No CQL Library Selected"));
		addEventHandlers();
	}

	private void addViewCQLEventHandlers() {
		cqlStandaloneWorkspaceView.getViewCQLView().getExportErrorFile().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getLibraryLockService().checkForEditPermission()) {
					String url = GWT.getModuleBaseURL() + "export?libraryid=" + MatContext.get().getCurrentCQLLibraryId() + "&format=errorFileStandAlone";
					Window.open(url + "&type=save", "_self", "");
				}
			}
		});

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
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBoxYesButton()
		.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
					deleteDefinition();
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				} else if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null
						&& cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId() == null) {
					deleteFunction();
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				}else if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId() != null) {
					deleteFunctionArgument();
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				} else if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
						.getCurrentSelectedParamerterObjId() != null) {
					deleteParameter();
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				} else if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
						.getCurrentSelectedIncLibraryObjId() != null) {
					deleteInclude();
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				}  else if(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedValueSetObjId() != null){
					checkAndDeleteValueSet();
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				} else if(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedCodesObjId() != null){
					deleteCode();
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				}
			}
		});

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBoxNoButton()
		.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
			}
		});

		ClickHandler cHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().detach();
				cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().detach();
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().detach();
			}
		};
		cqlStandaloneWorkspaceView.getMainPanel().addDomHandler(cHandler, ClickEvent.getType());

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

	/**
	 * On modify value set qdm.
	 *
	 * @param result
	 *            the result
	 * @param isUserDefined
	 *            the is user defined
	 */
	private void onModifyValueSet(CQLQualityDataSetDTO result, boolean isUserDefined) {
		String oid = isUserDefined ? "" : result.getOid();
		cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().setEnabled(true);

		cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().setValue(oid);
		cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().setTitle(oid);

		cqlStandaloneWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(!isUserDefined);

		cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput().setEnabled(isUserDefined);
		cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput().setValue(result.getOriginalCodeListName());
		cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput().setTitle(result.getOriginalCodeListName());

		cqlStandaloneWorkspaceView.getValueSetView().getSuffixInput().setEnabled(true);
		cqlStandaloneWorkspaceView.getValueSetView().getSuffixInput().setValue(result.getSuffix());
		cqlStandaloneWorkspaceView.getValueSetView().getSuffixInput().setTitle(result.getSuffix());

		setReleaseAndProgramFieldsOnEdit(result);
		cqlStandaloneWorkspaceView.getValueSetView().getSaveButton().setEnabled(isUserDefined);
		alert508StateChanges();
	}

	private void setReleaseAndProgramFieldsOnEdit(CQLQualityDataSetDTO result) {
		previousIsProgramListBoxEnabled = isProgramListBoxEnabled;
		CQLAppliedValueSetUtility.setProgramsAndReleases(result.getProgram(), result.getRelease(), cqlStandaloneWorkspaceView.getValueSetView());
		isProgramListBoxEnabled = true;
	}


	private void onModifyCode(CQLCode cqlCode) {
		CQLCodesView codesView = cqlStandaloneWorkspaceView.getCodesView();
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


	/**
	 * Save library xml.
	 *
	 * @param toBeDeletedValueSetId
	 *            the to Be Deleted Value Set Id
	 * 
	 */
	private void deleteValueSet(String toBeDeletedValueSetId) {
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().deleteValueSet(toBeDeletedValueSetId,
				MatContext.get().getCurrentCQLLibraryId(), new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onFailure(final Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);
			}

			@Override
			public void onSuccess(final SaveUpdateCQLResult result) {
				if (result != null && result.getCqlErrors().isEmpty()) {
					modifyValueSetDTO = null;
					// The below call will update the Applied QDM drop
					// down list in insert popup.
					getAppliedValueSetList();
					messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_QDM_REMOVE_MSG(result.getCqlQualityDataSetDTO().getName()));
					messagePanel.getSuccessMessageAlert().setVisible(true);
				}
				getUsedArtifacts();
				showSearchingBusy(false);
			}
		});
	}

	/**
	 * Gets the applied QDM list.
	 *
	 * @return the applied QDM list
	 */
	private void getAppliedValueSetList() {
		showSearchingBusy(true);
		String cqlLibraryId = MatContext.get().getCurrentCQLLibraryId();
		if ((cqlLibraryId != null) && !cqlLibraryId.equals("")) {
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

	/**
	 * Adds the parameter event handlers.
	 */
	private void addParameterEventHandlers() {
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(
							Document.get()
							.createDblClickEvent(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
									.getParameterNameListBox().getSelectedIndex(), 0, 0, 0, 0, false, false,
									false, false),
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox());
				}

			}
		});

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox()
		.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {

				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
					showSearchBusyOnDoubleClick(true);
					cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
					cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
					cqlStandaloneWorkspaceView.getCQLParametersView().getParameterButtonBar().getInfoButtonGroup()
					.getElement().setAttribute("class", "btn-group");
					resetAceEditor(cqlStandaloneWorkspaceView.getCQLParametersView().getViewCQLAceEditor());
					resetViewCQLCollapsiblePanel(
							cqlStandaloneWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());

					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
					if (getIsPageDirty()) {
						showSearchBusyOnDoubleClick(false);
						showUnsavedChangesWarning();
					} else {
						int selectedIndex = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox()
								.getSelectedIndex();
						if (selectedIndex != -1) {
							final String selectedParamID = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
									.getParameterNameListBox().getValue(selectedIndex);
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
							.setCurrentSelectedParamerterObjId(selectedParamID);
							if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterMap()
									.get(selectedParamID) != null) {

								cqlStandaloneWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton()
								.setTitle("Delete");
								cqlStandaloneWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton()
								.setEnabled(false);
								cqlStandaloneWorkspaceView.getCQLParametersView().setWidgetReadOnly(false);
								cqlStandaloneWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton()
								.setEnabled(false);
								cqlStandaloneWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
								// load most recent used cql artifacts
								MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(
										MatContext.get().getCurrentCQLLibraryId(),
										new AsyncCallback<GetUsedCQLArtifactsResult>() {

											@Override
											public void onFailure(Throwable caught) {
												showSearchBusyOnDoubleClick(false);
												Window.alert(MatContext.get().getMessageDelegate()
														.getGenericErrorMessage());
											}

											@Override
											public void onSuccess(GetUsedCQLArtifactsResult result) {
												showSearchBusyOnDoubleClick(false);
												cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).getName());
												cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().setText(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).getLogic());
												cqlStandaloneWorkspaceView.getCQLParametersView().getParameterCommentTextArea().setText(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).getCommentString());

												boolean isReadOnly = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).isReadOnly();
												CQLParameter currentParameter = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID);
												if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
													cqlStandaloneWorkspaceView.getCQLParametersView().setWidgetReadOnly(!isReadOnly);
													if (!currentParameter.isReadOnly()) {
														// if there are cql errors or the parameter is not in use, enable the delete button
														if (!result.getCqlErrors().isEmpty() || !result.getUsedCQLParameters().contains(currentParameter.getName())) {
															cqlStandaloneWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
														} else{
															cqlStandaloneWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
														}
													}

												}
												Map<String, List<CQLErrors>> expressionCQLErrorMap = result.getCqlErrorsPerExpression();
												if (expressionCQLErrorMap != null) {
													List<CQLErrors> errorList = expressionCQLErrorMap.get(currentParameter.getName());
													cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
													cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
													for (CQLErrors error : errorList) {
														int startLine = error.getStartErrorInLine();
														int startColumn = error.getStartErrorAtOffset();
														cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().addAnnotation(startLine, startColumn,error.getErrorMessage(),AceAnnotationType.ERROR);
													}
													cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().setAnnotations();
												}

											}

										});

							} else {
								showSearchBusyOnDoubleClick(false);
							}
						} else {
							showSearchBusyOnDoubleClick(false);
						}

						cqlStandaloneWorkspaceView.resetMessageDisplay();

					}
				}
				// 508 change to parameter section
				cqlStandaloneWorkspaceView.getCQLParametersView().getMainParamViewVerticalPanel().setFocus(true);
			}
		});


		cqlStandaloneWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {

			@Override
			public void onShow(ShowEvent showEvent) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				showCompleteCQL(cqlStandaloneWorkspaceView.getCQLParametersView().getViewCQLAceEditor());
			}
		});

		cqlStandaloneWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent hideEvent) {
				cqlStandaloneWorkspaceView.getCQLParametersView().getParameterButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetAceEditor(cqlStandaloneWorkspaceView.getCQLParametersView().getViewCQLAceEditor());

			}
		});


		// Parameter Save Icon Functionality
		cqlStandaloneWorkspaceView.getCQLParametersView().getParameterButtonBar().getSaveButton()
		.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					addAndModifyParameters();
					//508 change to parameter section
					cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setFocus(true);
				}
			}

		});
		// Parameter Erase Icon Functionality
		cqlStandaloneWorkspaceView.getCQLParametersView().getParameterButtonBar().getEraseButton()
		.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
				eraseParameter(); 
				//508 change to parameter section
				cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().focus();
			}
		});
		// Parameter Delete Icon Functionality
		cqlStandaloneWorkspaceView.getParameterButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_PARAMETER(cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea().getValue()));
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();

			}

		});

		cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					setIsPageDirty(true);
				}
			}
		});

		cqlStandaloneWorkspaceView.getCQLParametersView().getParameterCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					setIsPageDirty(true);
				}
			}
		});


		// Parameter Add New Functionality
		cqlStandaloneWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
				if (getIsPageDirty()) {
					showUnsavedChangesWarning();
				} else {
					addNewParameter();
				}
				//508 change to parameter section
				cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setFocus(true);
			}
		});

		cqlStandaloneWorkspaceView.getCQLParametersView().getParameterCommentTextArea().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				cqlStandaloneWorkspaceView.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.NONE);
				String comment = cqlStandaloneWorkspaceView.getCQLParametersView().getParameterCommentTextArea().getText();
				if(validator.isCommentTooLongOrContainsInvalidText(comment)){
					cqlStandaloneWorkspaceView.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.ERROR);
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});
	}

	/**
	 * Get Complete CQL And add it to Collapsible Panel in read Only Mode.
	 */
	private void showCompleteCQL(final AceEditor aceEditor) {
		MatContext.get().getCQLLibraryService().getLibraryCQLFileData(MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<SaveUpdateCQLResult>() {
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if (result.isSuccess()) {
					if ((result.getCqlString() != null) && !result.getCqlString().isEmpty()) {

						aceEditor.clearAnnotations();
						aceEditor.redisplay();

						if (!result.getCqlErrors().isEmpty()) {

							for (CQLErrors error : result.getCqlErrors()) {
								String errorMessage = new String();
								errorMessage = errorMessage.concat("Error in line : " + error.getErrorInLine()
								+ " at Offset :" + error.getErrorAtOffeset());
								int line = error.getErrorInLine();
								int column = error.getErrorAtOffeset();
								aceEditor.addAnnotation(line - 1, column,
										error.getErrorMessage(), AceAnnotationType.ERROR);
							}
							aceEditor.setText(result.getCqlString());
							aceEditor.setAnnotations();
							aceEditor.gotoLine(1);
							aceEditor.redisplay();
						} else {

							aceEditor.setText(result.getCqlString());
							aceEditor.gotoLine(1);
						}
					}

				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
	}

	/**
	 * 
	 */
	private void resetAceEditor(AceEditor aceEditor) {
		aceEditor.clearAnnotations();
		aceEditor.removeAllMarkers();
		//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
		//aceEditor.redisplay();
		aceEditor.setText("");
	}

	/**
	 * @param panelCollapse 
	 * 
	 */
	public void resetViewCQLCollapsiblePanel(PanelCollapse panelCollapse) {
		panelCollapse.getElement().setClassName("panel-collapse collapse");
	}

	/**
	 * Adds the new parameter.
	 */
	private void addNewParameter() {

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		setIsPageDirty(false);
		if ((cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().getText() != null)) {
			cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().setText("");
		}
		if ((cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea() != null)) {
			cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText("");
		}

		if ((cqlStandaloneWorkspaceView.getCQLParametersView().getParameterCommentTextArea() != null)) {
			cqlStandaloneWorkspaceView.getCQLParametersView().getParameterCommentTextArea().setText("");
		}

		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			cqlStandaloneWorkspaceView.getCQLParametersView()
			.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
			cqlStandaloneWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox().setText("");
		if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex() >= 0) {
			cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().setItemSelected(
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(), false);
		}

		cqlStandaloneWorkspaceView.getParameterButtonBar().getDeleteButton().setEnabled(false);
	}

	/**
	 * Adds the define event handlers.
	 */
	private void addDefineEventHandlers() {

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(
							Document.get()
							.createDblClickEvent(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
									.getDefineNameListBox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
									false),
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox());
				}

			}
		});

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox()
		.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {

				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
					showSearchBusyOnDoubleClick(true);
					cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
					cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().removeAllMarkers();

					resetAceEditor(cqlStandaloneWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor());
					resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse());
					cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText("");

					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
					if (getIsPageDirty()) {
						showSearchBusyOnDoubleClick(false);
						showUnsavedChangesWarning();
					} else {
						int selectedIndex = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox()
								.getSelectedIndex();
						if (selectedIndex != -1) {
							final String selectedDefinitionID = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
									.getDefineNameListBox().getValue(selectedIndex);
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
							.setCurrentSelectedDefinitionObjId(selectedDefinitionID);

							if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionMap()
									.get(selectedDefinitionID) != null) {
								cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton()
								.setTitle("Delete");

								cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
								cqlStandaloneWorkspaceView.getCQLDefinitionsView().setWidgetReadOnly(false);
								cqlStandaloneWorkspaceView.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
								// load most recent used cql artifacts
								MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(
										MatContext.get().getCurrentCQLLibraryId(),
										new AsyncCallback<GetUsedCQLArtifactsResult>() {

											@Override
											public void onFailure(Throwable caught) {
												showSearchBusyOnDoubleClick(false);
												cqlStandaloneWorkspaceView.getCQLDefinitionsView().setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
												Window.alert(MatContext.get().getMessageDelegate()
														.getGenericErrorMessage());
											}

											@Override
											public void onSuccess(GetUsedCQLArtifactsResult result) {
												showSearchBusyOnDoubleClick(false);
												boolean isReadOnly = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
														.getDefinitionMap().get(selectedDefinitionID).isSupplDataElement();

												CQLDefinition currentDefinition = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID);

												cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea()
												.setText(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionMap()
														.get(selectedDefinitionID).getName());
												cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor()
												.setText(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionMap()
														.get(selectedDefinitionID).getLogic());
												cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea()
												.setText(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getCommentString());

												if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionMap()
														.get(selectedDefinitionID).getContext().equalsIgnoreCase("patient")) {
													cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn()
													.setValue(true);
													cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn()
													.setValue(false);
												} else {
													cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn()
													.setValue(true);
													cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn()
													.setValue(false);
												}

												if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
													cqlStandaloneWorkspaceView.getCQLDefinitionsView().setWidgetReadOnly(!isReadOnly);
													// if there are cql errors or the definition is not in use, enable the delete button
													if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLDefinitions().contains(currentDefinition.getName())) {
														cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(true);
													} else {
														cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
													}


												}

												Map<String,List<CQLErrors>> expressionCQLErrorMap = result.getCqlErrorsPerExpression();
												if(expressionCQLErrorMap != null){
													List<CQLErrors> errorList = expressionCQLErrorMap.get(currentDefinition.getName());
													cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
													cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().removeAllMarkers();
													for (CQLErrors error : errorList) {
														int startLine = error.getStartErrorInLine();
														int startColumn = error.getStartErrorAtOffset();
														cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.ERROR);
													}
													cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setAnnotations();
												}

												if(result.getCqlErrors().isEmpty() && result.getExpressionReturnTypeMap() != null){
													cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getExpressionReturnTypeMap()
															.get(currentDefinition.getName()));
													cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getExpressionReturnTypeMap()
													.get(currentDefinition.getName()) );

												} else {
													cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
													cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");

												}

											}

										});

							} else {
								showSearchBusyOnDoubleClick(false);
							}
						}else {
							showSearchBusyOnDoubleClick(false);
						}

						cqlStandaloneWorkspaceView.resetMessageDisplay();
					}
				}
				//508 changes for Definitions Section
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getMainDefineViewVerticalPanel().setFocus(true);
			}
		});

		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {

			@Override
			public void onShow(ShowEvent showEvent) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				showCompleteCQL(cqlStandaloneWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor());
			}
		});

		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent hideEvent) {
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetAceEditor(cqlStandaloneWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor());

			}

		});


		cqlStandaloneWorkspaceView.getDefineButtonBar().getInsertButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				buildInsertPopUp();
			}
		});
		// Definition Save Icon Functionality
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse());
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					addAndModifyDefintions();
					cqlStandaloneWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
					cqlStandaloneWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
					//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
					//searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().redisplay();
					//508 changes for Definitions Section
					cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setFocus(true);
				}
			}


		});
		// Definition Erase Icon Functionality
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
				//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
				//searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().redisplay();

				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse());
				eraseDefinition(); 
				//508 changes to Definition section
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().focus();
			}
		});

		// Definition Delete Icon Functionality
		cqlStandaloneWorkspaceView.getDefineButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse());
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDeleteConfirmationDefinitionCQLLibraryWorkspace(cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().getValue()));
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();

			}
		});

		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					setIsPageDirty(true);
				}
			}
		});

		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					setIsPageDirty(true);
				}
			}
		});


		// Definition Add New Functionality
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
				//searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().redisplay();

				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLDefinitionsView().getPanelViewCQLCollapse());
				if (getIsPageDirty()) {
					showUnsavedChangesWarning();
				} else {
					addNewDefinition();
				}
				//508 changes for Definitions Section
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setFocus(true);
			}
		});


		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.NONE);
				String comment = cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea().getText();
				if(validator.isCommentTooLongOrContainsInvalidText(comment)){
					cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});
	}

	/**
	 * Adds the new definition.
	 */
	private void addNewDefinition() {

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
		setIsPageDirty(false);
		if ((cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().getText() != null)) {
			cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setText("");
		}
		if ((cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea() != null)) {
			cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setText("");
		}

		if ((cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea()!= null)) {
			cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea().setText("");
		}
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox().setText("");
		if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex() >= 0) {
			cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().setItemSelected(
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex(), false);
		}

		// Functionality to reset the disabled features for supplemental data
		// definitions when erased.
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setEnabled(true);
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setReadOnly(false);
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().setEnabled(true);
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setEnabled(true);
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		cqlStandaloneWorkspaceView.getDefineButtonBar().getSaveButton().setEnabled(true);
		cqlStandaloneWorkspaceView.getDefineButtonBar().getDeleteButton().setEnabled(false);
		cqlStandaloneWorkspaceView.getDefineButtonBar().getInsertButton().setEnabled(true);
		cqlStandaloneWorkspaceView.getDefineButtonBar().getTimingExpButton().setEnabled(true);
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);


	}

	/**
	 * Adds the function event handlers.
	 */
	private void addFunctionEventHandlers() {
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(
							Document.get()
							.createDblClickEvent(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
									.getFuncNameListBox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
									false),
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox());
				}

			}
		});
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
					showSearchBusyOnDoubleClick(true);
					cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
					cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);

					resetAceEditor(cqlStandaloneWorkspaceView.getCQLFunctionsView().getViewCQLAceEditor());
					resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());

					cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText("");


					if (getIsPageDirty()) {
						showSearchBusyOnDoubleClick(false);
						showUnsavedChangesWarning();
					} else {
						int selectedIndex = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox()
								.getSelectedIndex();
						if (selectedIndex != -1) {
							final String selectedFunctionId = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
									.getFuncNameListBox().getValue(selectedIndex);
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
							.setCurrentSelectedFunctionObjId(selectedFunctionId);

							if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap()
									.get(selectedFunctionId) != null) {
								cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
								cqlStandaloneWorkspaceView.getCQLFunctionsView().setWidgetReadOnly(false);
								cqlStandaloneWorkspaceView.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
								// load most recent used cql artifacts
								MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(
										MatContext.get().getCurrentCQLLibraryId(),
										new AsyncCallback<GetUsedCQLArtifactsResult>() {

											@Override
											public void onFailure(Throwable caught) {
												showSearchingBusy(false);
												cqlStandaloneWorkspaceView.getCQLFunctionsView().setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
												Window.alert(
														MatContext.get().getMessageDelegate().getGenericErrorMessage());
											}

											@Override
											public void onSuccess(GetUsedCQLArtifactsResult result) {
												showSearchBusyOnDoubleClick(false);
												cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setText(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getName());
												cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().setText(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getLogic());
												cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().setText(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getCommentString());

												if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getContext().equalsIgnoreCase("patient")) {
													cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(true);
													cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
												} else {
													cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(true);
													cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(false);
												}
												CQLFunctions currentFunction = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId);
												if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
													cqlStandaloneWorkspaceView.getCQLFunctionsView().setWidgetReadOnly(true);

													if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLFunctions().contains(currentFunction.getName())) {
														cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
													} else {
														cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
													}
												}
												Map<String,List<CQLErrors>> expressionCQLErrorMap = result.getCqlErrorsPerExpression();
												if(expressionCQLErrorMap != null){
													List<CQLErrors> errorList = expressionCQLErrorMap.get(currentFunction.getName());
													cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
													cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
													for (CQLErrors error : errorList) {
														int startLine = error.getStartErrorInLine();
														int startColumn = error.getStartErrorAtOffset();
														cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.ERROR);
													}
													cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().setAnnotations();
												}
												if(result.getCqlErrors().isEmpty() && result.getExpressionReturnTypeMap() != null){
													cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(result.getExpressionReturnTypeMap()
															.get(currentFunction.getName()));
													cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getExpressionReturnTypeMap()
													.get(currentFunction.getName()));

												} else {
													cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText("");
													cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
												}
											}

										});

							} else {
								showSearchBusyOnDoubleClick(false);
							}
						} else {
							showSearchBusyOnDoubleClick(false);
						}
						if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
							CQLFunctions selectedFunction = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap()
									.get(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
							if (selectedFunction.getArgumentList() != null) {
								cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionArgumentList().clear();
								cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionArgumentList()
								.addAll(selectedFunction.getArgumentList());
							} else {
								cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionArgumentList().clear();
							}
						}
						cqlStandaloneWorkspaceView.resetMessageDisplay();
					}
					cqlStandaloneWorkspaceView.getCQLFunctionsView().createAddArgumentViewForFunctions(
							cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionArgumentList(),
							MatContext.get().getLibraryLockService().checkForEditPermission());

				}
				//508 changes for Functions Section
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getMainFunctionVerticalPanel().setFocus(true);
			}
		});

		cqlStandaloneWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {

			@Override
			public void onShow(ShowEvent showEvent) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				showCompleteCQL(cqlStandaloneWorkspaceView.getCQLFunctionsView().getViewCQLAceEditor());
			}
		});

		cqlStandaloneWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent hideEvent) {
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetAceEditor(cqlStandaloneWorkspaceView.getCQLFunctionsView().getViewCQLAceEditor());

			}
		});


		// Function Insert Icon Functionality
		cqlStandaloneWorkspaceView.getFunctionButtonBar().getInsertButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				buildInsertPopUp();
				//508 changes for Functions Section
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().focus();
			}
		});

		// Function Save Icon Functionality
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					addAndModifyFunction();
					//508 changes for Functions Section
					cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setFocus(true);
				}

			}
		});

		// Functions Erase Icon Functionality
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
				eraseFunction();
				//508 changes for Functions Section
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().focus();
			}
		});

		// Add Function Argument functionality
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getAddNewArgument().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.hideAceEditorAutoCompletePopUp();
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
				CQLFunctionArgument addNewFunctionArgument = new CQLFunctionArgument();
				AddFunctionArgumentDialogBox.showArgumentDialogBox(addNewFunctionArgument, false, cqlStandaloneWorkspaceView.getCQLFunctionsView(), messagePanel,MatContext.get().getLibraryLockService().checkForEditPermission());
				setIsPageDirty(true);
			}
		});
		// Function Delete Icon Functionality
		cqlStandaloneWorkspaceView.getFunctionButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDeleteConfirmationFunctionCQLLibraryWorkspace(cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().getValue()));
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();



			}

		});

		cqlStandaloneWorkspaceView.getCQLFunctionsView().setObserver(new Observer() {

			@Override
			public void onModifyClicked(CQLFunctionArgument result) {
				setIsPageDirty(true);
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				if (result.getArgumentType().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
					getAttributesForDataType(result);
				} else {
					AddFunctionArgumentDialogBox.showArgumentDialogBox(result, true,cqlStandaloneWorkspaceView.getCQLFunctionsView(), messagePanel,MatContext.get().getLibraryLockService().checkForEditPermission());
				}

			}

			@Override
			public void onDeleteClicked(CQLFunctionArgument result, int index) {
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				setIsPageDirty(true);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(result.getId());
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(result.getArgumentName());
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_FUNCTION_ARGUMENT(result.getArgumentName()));
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
				//508 changes for Functions Section
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getMainFunctionVerticalPanel().setFocus(true);
			}
		});

		cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					setIsPageDirty(true);
				}
			}
		});

		cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					setIsPageDirty(true);
				}
			}
		});


		// Function Add New Functionality
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				resetViewCQLCollapsiblePanel(cqlStandaloneWorkspaceView.getCQLFunctionsView().getPanelViewCQLCollapse());
				if (getIsPageDirty()) {
					showUnsavedChangesWarning();
				} else {
					addNewFunction();
				}
				//508 changes for Functions Section
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getMainFunctionVerticalPanel().setFocus(true);
			}
		});


		cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.NONE);
				String comment = cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().getText();
				if(validator.isCommentTooLongOrContainsInvalidText(comment)){
					cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});
	}

	/**
	 * Adds the new function.
	 */
	private void addNewFunction() {
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionArgumentList().clear();
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionArgNameMap().clear();
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
		cqlStandaloneWorkspaceView.createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>());
		setIsPageDirty(false);
		if ((cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().setText("");
		}
		if ((cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea() != null)) {
			cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setText("");
		}

		if ((cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea()!= null)) {
			cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().setText("");
		}
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText("");

		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText("");
		if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex() >= 0) {
			cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().setItemSelected(
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex(), false);
		}
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(true);
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		cqlStandaloneWorkspaceView.getFunctionButtonBar().getDeleteButton().setEnabled(false);
	}

	/**
	 * Adds the warning alert handlers.
	 */
	private void addWarningAlertHandlers() {

		messagePanel.getWarningConfirmationYesButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setIsPageDirty(false);
				messagePanel.getWarningConfirmationMessageAlert().clearAlert();
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().isDoubleClick()) {
					clickEventOnListboxes();
				} else if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().isNavBarClick()) {
					changeSectionSelection();
				} else {
					clearViewIfDirtyNotSet();
				}
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
			}
		});

		messagePanel.getWarningConfirmationNoButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				messagePanel.getWarningConfirmationMessageAlert().clearAlert();
				// no was selected, don't move anywhere
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().isNavBarClick()) {
					unsetActiveMenuItem(nextSection);
				}
				if (currentSection.equals(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().setSelectedIndex(-1);
				} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().setSelectedIndex(-1);
				} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().setSelectedIndex(-1);
				}
			}
		});

	}

	/**
	 * Adds the general info event handlers.
	 */
	private void addGeneralInfoEventHandlers() {
		cqlStandaloneWorkspaceView.getCqlGeneralInformationView().getSaveButton().addClickHandler(event -> saveCQLGeneralInfo());
		cqlStandaloneWorkspaceView.getCqlGeneralInformationView().getLibraryNameValue().addKeyUpHandler(event -> resetMessagesAndSetPageDirty(true));
		cqlStandaloneWorkspaceView.getCqlGeneralInformationView().getComments().addValueChangeHandler(event -> resetMessagesAndSetPageDirty(true));
	}

	private void resetMessagesAndSetPageDirty(boolean isPageDirty) {
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			cqlStandaloneWorkspaceView.resetMessageDisplay();
			setIsPageDirty(isPageDirty);
		}
	}

	private void saveCQLGeneralInfo() {
		resetMessagesAndSetPageDirty(false);

		String libraryName = cqlStandaloneWorkspaceView.getCqlGeneralInformationView().getLibraryNameValue().getText().trim();
		String commentContent = cqlStandaloneWorkspaceView.getCqlGeneralInformationView().getComments().getText().trim();

		boolean isValid = CQLGeneralInformationUtility.validateGeneralInformationSection(cqlStandaloneWorkspaceView.getCqlGeneralInformationView(), messagePanel, libraryName, commentContent);
		if(isValid) {
			saveCQLGeneralInformation(libraryName, commentContent);
		}
	}

	/**
	 * Event Handlers for Ace Editors.
	 */
	private void addEventHandlerOnAceEditors() {
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().isReadOnly()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					setIsPageDirty(true);
				}
			}
		});

		cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().isReadOnly()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					setIsPageDirty(true);
				}
			}
		});

		cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().isReadOnly()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					setIsPageDirty(true);
				}
			}
		});
	}

	/**
	 * Adds the event handlers on context radio buttons.
	 */
	private void addEventHandlersOnContextRadioButtons() {
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn()
		.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				setIsPageDirty(true);
				if (cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
					cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);
				} else {
					cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(true);
				}

			}
		});

		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn()
		.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				setIsPageDirty(true);
				if (cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePOPRadioBtn().getValue()) {
					cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(false);
				} else {
					cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
				}

			}
		});

		cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn()
		.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				setIsPageDirty(true);
				if (cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().getValue()) {
					cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
				} else {
					cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(true);
				}
			}
		});

		cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn()
		.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				setIsPageDirty(true);
				if (cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPOPRadioBtn().getValue()) {
					cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(false);
				} else {
					cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(true);
				}
			}
		});
	}


	/**
	 * Adds the include CQL library handlers.
	 */
	private void addIncludeCQLLibraryHandlers() {

		cqlStandaloneWorkspaceView.getIncludeView().getSaveModifyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				messagePanel.getErrorMessageAlert().clearAlert();
				messagePanel.getSuccessMessageAlert().clearAlert();
				final EditIncludedLibraryDialogBox editIncludedLibraryDialogBox = new EditIncludedLibraryDialogBox("Replace Library");
				editIncludedLibraryDialogBox.findAvailableLibraries(currentIncludeLibrarySetId, currentIncludeLibraryId, false);

				editIncludedLibraryDialogBox.getApplyButton().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						messagePanel.getErrorMessageAlert().clearAlert();
						messagePanel.getSuccessMessageAlert().clearAlert();
						if(editIncludedLibraryDialogBox.getSelectedList().size() > 0){
							final CQLIncludeLibrary toBeModified = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
									.getIncludeLibraryMap()
									.get(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId());

							final CQLLibraryDataSetObject dto = editIncludedLibraryDialogBox.getSelectedList().get(0);
							if (dto != null) {
								final CQLIncludeLibrary currentObject = new CQLIncludeLibrary(dto);

								MatContext.get().getCQLLibraryService().saveIncludeLibrayInCQLLookUp(
										MatContext.get().getCurrentCQLLibraryId(), toBeModified, currentObject,
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
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
												CQLAppliedValueSetUtility.loadReleases(cqlStandaloneWorkspaceView.getValueSetView().getReleaseListBox(), cqlStandaloneWorkspaceView.getValueSetView().getProgramListBox());
												if (result != null) {
													if (result.isSuccess()) {
														cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(
																result.getCqlModel().getCqlIncludeLibrarys());
														cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
														MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
														MatContext.get().setIncludedValues(result);
														MatContext.get().setCQLModel(result.getCqlModel());
														editIncludedLibraryDialogBox.getDialogModal().hide();
														DomEvent.fireNativeEvent(
																Document.get()
																.createDblClickEvent(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
																		.getIncludesNameListbox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
																		false),
																cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox());
														String libraryNameWithVersion = result.getIncludeLibrary().getCqlLibraryName() + " v" +result.getIncludeLibrary().getVersion();
														messagePanel.getSuccessMessageAlert().createAlert(libraryNameWithVersion + " has been successfully saved as the alias " + result.getIncludeLibrary().getAliasName());
													}
												}

											}

										});

							}
						} else {
							editIncludedLibraryDialogBox.getErrorMessageAlert().clearAlert();
							editIncludedLibraryDialogBox.getErrorMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getNO_LIBRARY_TO_REPLACE());
						}


					}
				});
			}
		});

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(
							Document.get()
							.createDblClickEvent(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
									.getIncludesNameListbox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
									false),
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox());
				}

			}
		});
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox()
		.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);

					if (getIsPageDirty()) {
						showUnsavedChangesWarning();
					} else {
						int selectedIndex = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex();
						if (selectedIndex != -1) {
							final String selectedIncludeLibraryID = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
									.getIncludesNameListbox().getValue(selectedIndex);
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
							.setCurrentSelectedIncLibraryObjId(selectedIncludeLibraryID);
							if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap().get(selectedIncludeLibraryID) != null) {
								MatContext.get().getCQLLibraryService().findCQLLibraryByID(
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
										.get(selectedIncludeLibraryID).getCqlLibraryId(),
										new AsyncCallback<CQLLibraryDataSetObject>() {

											@Override
											public void onSuccess(CQLLibraryDataSetObject result) {
												if (result != null) {
													currentIncludeLibrarySetId = result.getCqlSetId();
													currentIncludeLibraryId = result.getId();
													cqlStandaloneWorkspaceView.getIncludeView().buildIncludesReadOnlyView();

													cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea()
													.setText(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
															.getIncludeLibraryMap()
															.get(selectedIncludeLibraryID)
															.getAliasName());
													cqlStandaloneWorkspaceView.getIncludeView().getViewCQLEditor()
													.setText(result.getCqlText());
													cqlStandaloneWorkspaceView.getIncludeView().getOwnerNameTextBox()
													.setText(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
															.getOwnerName(result));
													cqlStandaloneWorkspaceView.getIncludeView().getCqlLibraryNameTextBox()
													.setText(result.getCqlName());

													cqlStandaloneWorkspaceView.getIncludeView().getSaveModifyButton().setEnabled(false);														
													cqlStandaloneWorkspaceView.getIncludeView().getDeleteButton().setEnabled(false);

													cqlStandaloneWorkspaceView.getIncludeView().getSaveModifyButton().setEnabled(false);
													if (MatContext.get().getLibraryLockService()
															.checkForEditPermission()) {
														cqlStandaloneWorkspaceView.getIncludeView().setWidgetReadOnly(false);
														cqlStandaloneWorkspaceView.getIncludeView().getSaveModifyButton().setEnabled(true);


														// load most recent
														// used
														// cql artifacts
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

																		CQLIncludeLibrary cqlIncludeLibrary = cqlStandaloneWorkspaceView
																				.getCqlLeftNavBarPanelView()
																				.getIncludeLibraryMap()
																				.get(selectedIncludeLibraryID);

																		// if there are errors or the library is not in use, enable the delete button
																		if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLLibraries().contains(
																				cqlIncludeLibrary.getCqlLibraryName()+ "-"+ cqlIncludeLibrary.getVersion()+ "|"
																						+ cqlIncludeLibrary.getAliasName())) {
																			cqlStandaloneWorkspaceView.getIncludeView().getDeleteButton().setEnabled(true);
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

								cqlStandaloneWorkspaceView.getIncludeView().setSelectedObject(
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
										.get(selectedIncludeLibraryID).getCqlLibraryId());
								cqlStandaloneWorkspaceView.getIncludeView()
								.setIncludedList(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
										.getIncludedList(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
												.getIncludeLibraryMap()));
								cqlStandaloneWorkspaceView.getIncludeView().getSelectedObjectList().clear();
							}
						}
						cqlStandaloneWorkspaceView.resetMessageDisplay();

					}

				}

			}
		});

		// Includes search Button Functionality
		cqlStandaloneWorkspaceView.getIncludeView().getSearchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAllIncludeLibraryList(cqlStandaloneWorkspaceView.getIncludeView().getSearchTextBox().getText().trim());
				// 508 changes for Library Alias.
				cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
			}
		});

		// Includes search Button Focus Functionality
		cqlStandaloneWorkspaceView.getIncludeView().getFocusPanel().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				// Search when enter is pressed.
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					cqlStandaloneWorkspaceView.getIncludeView().getSearchButton().click();
				}
			}
		});

		// Includes Save Functionality
		cqlStandaloneWorkspaceView.getIncludeView().getIncludesButtonBar().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					addIncludeLibraryInCQLLookUp();
					// 508 changes for Library Alias.
					cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
				}
			}
		});

		// Includes Delete Functionality
		cqlStandaloneWorkspaceView.getIncludeView().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_INCLUDE(cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().getValue()));
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
				// 508 changes for Library Alias.
				cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
			}

		});

		// Includes close Functionality
		cqlStandaloneWorkspaceView.getIncludeView().getCloseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				// Below lines are to clear search suggestion textbox and
				// listbox
				// selection after erase.
				currentIncludeLibrarySetId =  null;
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().setItemSelected(
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(),
							false);
				}
				cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().setText("");
				cqlStandaloneWorkspaceView.buildIncludesView();
				SaveCQLLibraryResult cqlLibrarySearchModel = new SaveCQLLibraryResult();
				cqlLibrarySearchModel
				.setCqlLibraryDataSetObjects(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryList());
				cqlStandaloneWorkspaceView.getIncludeView().setIncludedList(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
						.getIncludedList(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
				cqlStandaloneWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(cqlLibrarySearchModel,
						MatContext.get().getLibraryLockService().checkForEditPermission(), false);
				cqlStandaloneWorkspaceView.getIncludeView()
				.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox()
						.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else {
					messagePanel.getWarningMessageAlert().clearAlert();
				}
				// 508 changes for Library Alias.
				cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
			}
		});

		// Includes Erase Functionality
		cqlStandaloneWorkspaceView.getIncludeView().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				cqlStandaloneWorkspaceView.resetMessageDisplay();
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				clearAlias();
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else {
					messagePanel.getWarningMessageAlert().clearAlert();
				}
				// 508 changes for Library Alias.
				cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
			}
		});

		// Includes Celltable Observer for CheckBox
		cqlStandaloneWorkspaceView.getIncludeView().setObserver(new CQLIncludeLibraryView.Observer() {

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
						cqlStandaloneWorkspaceView.getIncludeView().getViewCQLEditor().setText(result.getCqlText());
					}
				});
			}
		});
	}

	/**
	 * Adds and modify function.
	 */
	protected void addAndModifyFunction() {
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		final String functionName = cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().getText();
		String functionBody = cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().getText();
		String functionComment = cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionCommentTextArea().getText();
		String funcContext = "";
		if (cqlStandaloneWorkspaceView.getCQLFunctionsView().getContextFuncPATRadioBtn().getValue()) {
			funcContext = "Patient";
		} else {
			funcContext = "Population";
		}

		boolean isValidFunctionName = isValidExpressionName(functionName);
		if (isValidFunctionName) {
			if (validator.hasSpecialCharacter(functionName.trim())) {
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setText(functionName.trim());	
			} else if(validator.isCommentTooLongOrContainsInvalidText(functionComment)){
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
			} else {
				CQLFunctions function = new CQLFunctions();
				function.setLogic(functionBody);
				function.setName(functionName);
				function.setCommentString(functionComment);
				function.setArgumentList(cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionArgumentList());
				function.setContext(funcContext);
				CQLFunctions toBeModifiedParamObj = null;

				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
					toBeModifiedParamObj = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap()
							.get(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().saveAndModifyFunctions(
						MatContext.get().getCurrentCQLLibraryId(), toBeModifiedParamObj, function,
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewFunctions(),
						isFormattable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
								cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
								cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
								messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
										.setViewFunctions(result.getCqlModel().getCqlFunctions());
										MatContext.get()
										.setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
										.setCurrentSelectedFunctionObjId(result.getFunction().getId());
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().updateFunctionMap();
										messagePanel.getErrorMessageAlert().clearAlert();
										messagePanel.getSuccessMessageAlert().setVisible(true);
										cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea()
										.setText(result.getFunction().getName());
										cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor()
										.setText(result.getFunction().getLogic());
										setIsPageDirty(false);
										cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor()
										.clearAnnotations();
										cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor()
										.removeAllMarkers();
										if (validateCQLArtifact(result, currentSection)) {
											messagePanel.getSuccessMessageAlert().clearAlert();
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_MODIFY_WITH_ERRORS(functionName.trim()));
											cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText("");
											cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
										} else if (!result.isDatatypeUsedCorrectly()) {
											messagePanel.getSuccessMessageAlert().clearAlert();
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWarningBadDataTypeCombination());
											if(result.isValidCQLWhileSavingExpression()){
												cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(result.getFunction().getReturnType());
												cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is " + result.getFunction().getReturnType());
											} else {
												cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText("");
												cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}
										} else {
											messagePanel.getWarningMessageAlert().clearAlert();
											messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_MODIFY(functionName.trim()));
											if(result.isValidCQLWhileSavingExpression()){
												cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText(result.getFunction().getReturnType());
												cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is " + result.getFunction().getReturnType());
											} else {
												cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText("");
												cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}
										}

										cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().setAnnotations();
										cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().redisplay();

									} else if (result.getFailureReason() == SaveUpdateCQLResult.NAME_NOT_UNIQUE) { //TODO
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
										cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
									} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) {
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify."); //TODO
										cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea()
										.setText(functionName.trim());
									} else if (result.getFailureReason() == SaveUpdateCQLResult.NO_SPECIAL_CHAR) {
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
										cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea()
										.setText(functionName.trim());
										if (result.getFunction() != null) {
											cqlStandaloneWorkspaceView.createAddArgumentViewForFunctions(
													result.getFunction().getArgumentList());
										}
									} else if (result.getFailureReason() == SaveUpdateCQLResult.FUNCTION_ARGUMENT_INVALID) {
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlFunctionArgumentNameError());
									} else if (result.getFailureReason() == SaveUpdateCQLResult.COMMENT_INVALID) {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
									}

								}
								showSearchingBusy(false);

								// if there are cql errors enable the delete button
								if(!result.getCqlErrors().isEmpty()) {
									cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
								}

								else {
									// if the function is in use, disable the delete button
									if(result.getUsedCQLArtifacts().getUsedCQLFunctions().contains(functionName)) {
										cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
									}

									else {
										cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
									}

								}


							}
						});

			}

		} else {
			cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(functionName.isEmpty() 
					? MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_FUNCTION()
							: "Invalid Function name. " + MessageDelegate.DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
		}

	}

	/**
	 * Adds the and modify parameters.
	 */
	private void addAndModifyParameters() {

		cqlStandaloneWorkspaceView.resetMessageDisplay();
		final String parameterName = cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea().getText();
		String parameterLogic = cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().getText();
		String parameterComment = cqlStandaloneWorkspaceView.getCQLParametersView().getParameterCommentTextArea().getText();

		boolean isValidParamaterName = isValidExpressionName(parameterName);
		if (isValidParamaterName) {
			if (validator.hasSpecialCharacter(parameterName.trim())) {
				cqlStandaloneWorkspaceView.getCQLParametersView().getParamNameGroup().setValidationState(ValidationState.ERROR);
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
				cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
			} else if(validator.isCommentTooLongOrContainsInvalidText(parameterComment)){
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				cqlStandaloneWorkspaceView.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.ERROR);
			} else {
				CQLParameter parameter = new CQLParameter();
				parameter.setLogic(parameterLogic);
				parameter.setName(parameterName);
				parameter.setCommentString(parameterComment);
				CQLParameter toBeModifiedParamObj = null;

				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
					toBeModifiedParamObj = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterMap()
							.get(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().saveAndModifyParameters(
						MatContext.get().getCurrentCQLLibraryId(), toBeModifiedParamObj, parameter,
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewParameterList(),
						isFormattable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
								messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
										.setViewParameterList(result.getCqlModel().getCqlParameters());
										MatContext.get().setParameters(
												getParamaterList(result.getCqlModel().getCqlParameters()));
										MatContext.get().setCQLModel(result.getCqlModel());
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
										.setCurrentSelectedParamerterObjId(result.getParameter().getId());
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().updateParamMap();
										messagePanel.getErrorMessageAlert().clearAlert();
										cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea()
										.setText(result.getParameter().getName());
										cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor()
										.setText(result.getParameter().getLogic());
										setIsPageDirty(false);
										cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
										cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getCQLParametersView().getParameterAceEditor().redisplay();

										if (validateCQLArtifact(result, currentSection)) {
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_PARAMETER_MODIFY_WITH_ERRORS(parameterName.trim()));
										} else if (!result.isDatatypeUsedCorrectly()) {
											messagePanel.getSuccessMessageAlert().clearAlert();
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWarningBadDataTypeCombination());
										} else {
											messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_PARAMETER_MODIFY(parameterName.trim()));
										}
										cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().setAnnotations();
										cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().redisplay();
									} else if (result.getFailureReason() == 1) {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
										cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
									} else if (result.getFailureReason() == 2) {
										messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify.");
										cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
									} else if (result.getFailureReason() == 3) {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
										cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
									} else if (result.getFailureReason() == 8) {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
									}
								}
								showSearchingBusy(false);

								// if there are errors, enable the parameter delete button
								if (!result.getCqlErrors().isEmpty()) {
									cqlStandaloneWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
								}

								else {
									// if the saved parameter is in use, then disable the delete button
									if (result.getUsedCQLArtifacts().getUsedCQLParameters().contains(parameterName)) {
										cqlStandaloneWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
									}

									else {
										cqlStandaloneWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
									}
								}

							}
						});

			}

		} else {
			cqlStandaloneWorkspaceView.getCQLParametersView().getParamNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(parameterName.isEmpty() 
					? MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_PARAMETER()
							: "Invalid Parameter name. " + MessageDelegate.DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			cqlStandaloneWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
		}

	}

	/**
	 * This method is called to Add/Modify Definitions into Library Xml.
	 * 
	 */
	private void addAndModifyDefintions() {

		cqlStandaloneWorkspaceView.resetMessageDisplay();
		final String definitionName = cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().getText();
		String definitionLogic = cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().getText();
		String definitionComment = cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineCommentTextArea().getText();
		String defineContext = "";
		if (cqlStandaloneWorkspaceView.getCQLDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}

		boolean isValidDefinitionName = isValidExpressionName(definitionName);
		if (isValidDefinitionName) {
			if (validator.hasSpecialCharacter(definitionName.trim())) {
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
			} else if(validator.isCommentTooLongOrContainsInvalidText(definitionComment)){
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
			} else {
				final CQLDefinition define = new CQLDefinition();
				define.setName(definitionName);
				define.setLogic(definitionLogic);
				define.setCommentString(definitionComment);
				define.setContext(defineContext);
				CQLDefinition toBeModifiedObj = null;

				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
					toBeModifiedObj = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionMap()
							.get(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().saveAndModifyDefinitions(
						MatContext.get().getCurrentCQLLibraryId(), toBeModifiedObj, define,
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewDefinitions(),
						isFormattable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
								messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
										.setViewDefinitions(result.getCqlModel().getDefinitionList());
										MatContext.get().setDefinitions(
												getDefinitionList(result.getCqlModel().getDefinitionList()));
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
										.setCurrentSelectedDefinitionObjId(result.getDefinition().getId());
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
										cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().updateDefineMap();
										messagePanel.getErrorMessageAlert().clearAlert();
										cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea()
										.setText(result.getDefinition().getName());
										cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor()
										.setText(result.getDefinition().getLogic());
										setIsPageDirty(false);
										cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
										cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getCQLDefinitionsView().getDefineAceEditor().redisplay();
										cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
										if (validateCQLArtifact(result, currentSection)) {
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_DEFINITION_MODIFY_WITH_ERRORS(definitionName.trim()));
											cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
											cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
										} else if (!result.isDatatypeUsedCorrectly()) {
											messagePanel.getSuccessMessageAlert().clearAlert();
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWarningBadDataTypeCombination());
											if(result.isValidCQLWhileSavingExpression()){
												cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
												cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getDefinition().getReturnType());
											} else {
												cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
												cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}

										} else {
											messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_DEFINITION_MODIFY(definitionName.trim()));
											if(result.isValidCQLWhileSavingExpression()){
												cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
												cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getDefinition().getReturnType());
											} else {
												cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
												cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");

											}
										}

										cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setAnnotations();
										cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().redisplay();

									} else {
										if (result.getFailureReason() == SaveUpdateCQLResult.NAME_NOT_UNIQUE) {
											messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
											cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea()
											.setText(definitionName.trim());
										} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) {//TODO
											messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify.");
											cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
										} else if (result.getFailureReason() == SaveUpdateCQLResult.NO_SPECIAL_CHAR) {
											messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
											cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea()
											.setText(definitionName.trim());
										} else if (result.getFailureReason() == SaveUpdateCQLResult.COMMENT_INVALID) {
											messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
										}
									}

								}
								showSearchingBusy(false);

								// if there are errors, enable the
								// definition button
								if (!result.getCqlErrors().isEmpty()) {
									cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton()
									.setEnabled(true);
								}

								else {
									// if the saved definition is in
									// use, then disable the delete
									// button
									if (result.getUsedCQLArtifacts().getUsedCQLDefinitions().contains(definitionName)) {
										cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
									}

									else {
										cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(true);
									}
								}

							}
						});

			}

		} else {
			cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(definitionName.isEmpty() 
					? MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_DEFINITION()
							: "Invalid Definition name. " + MessageDelegate.DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
		}

	}

	/**
	 * Adds the include library in CQL look up.
	 */
	private void addIncludeLibraryInCQLLookUp() {
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox()
				.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
			messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
			return;

		} else {
			messagePanel.getWarningMessageAlert().clearAlert();
		}
		final String aliasName = cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().getText();

		if (!aliasName.isEmpty() && cqlStandaloneWorkspaceView.getIncludeView().getSelectedObjectList().size() > 0) {
			// functioanlity to add Include Library
			CQLLibraryDataSetObject cqlLibraryDataSetObject = cqlStandaloneWorkspaceView.getIncludeView().getSelectedObjectList()
					.get(0);

			if (validator.doesAliasNameFollowCQLAliasNamingConvention(aliasName.trim())) {

				CQLIncludeLibrary incLibrary = new CQLIncludeLibrary();
				incLibrary.setAliasName(aliasName);
				incLibrary.setCqlLibraryId(cqlLibraryDataSetObject.getId());
				String versionValue = cqlLibraryDataSetObject.getVersion().replace("v", "") + "."
						+ cqlLibraryDataSetObject.getRevisionNumber();
				incLibrary.setVersion(versionValue);
				incLibrary.setCqlLibraryName(cqlLibraryDataSetObject.getCqlName());
				incLibrary.setQdmVersion(cqlLibraryDataSetObject.getQdmVersion());
				incLibrary.setSetId(cqlLibraryDataSetObject.getCqlSetId());
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() == null) {
					// this is just to add include library and not modify
					MatContext.get().getCQLLibraryService().saveIncludeLibrayInCQLLookUp(
							MatContext.get().getCurrentCQLLibraryId(), null, incLibrary,
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
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
											cqlStandaloneWorkspaceView.resetMessageDisplay();
											setIsPageDirty(false);
											cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(
													result.getCqlModel().getCqlIncludeLibrarys());
											MatContext.get().setIncludes(
													getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
											MatContext.get().setCQLModel(result.getCqlModel());
											cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
											cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
											cqlStandaloneWorkspaceView.getIncludeView()
											.setIncludedList(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
													.getIncludedList(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
															.getIncludeLibraryMap()));
											messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getIncludeLibrarySuccessMessage(result.getIncludeLibrary().getAliasName()));
											clearAlias();

											MatContext.get().setIncludedValues(result);
											if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox()
													.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
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
				cqlStandaloneWorkspaceView.getCqlIncludeLibraryView().getAliasNameGroup().setValidationState(ValidationState.ERROR);
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
				cqlStandaloneWorkspaceView.getAliasNameTxtArea().setText(aliasName.trim());
			}
		} else {
			cqlStandaloneWorkspaceView.getCqlIncludeLibraryView().getAliasNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSAVE_INCLUDE_LIBRARY_VALIATION_ERROR());
		}
	}

	/**
	 * Clears the ace editor for parameters. Functionality for the eraser icon in parameter section. 
	 */
	private void eraseParameter() {

		cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if(cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().getText() != null) {
			cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().setText("");
			setIsPageDirty(true);
		}

	}

	/**
	 * Clears the ace editor for definitions. Functionality for the eraser icon in definition section. 
	 */
	private void eraseDefinition() {

		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
		if ((cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().getText() != null)) {
			cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor().setText("");
			setIsPageDirty(true);
		}
	}

	/**
	 * Clears the ace editor for functions. Functionality for the eraser icon in function section. 
	 */
	private void eraseFunction() {

		cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if ((cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor().setText("");
			setIsPageDirty(true);
		}
	}

	/**
	 * Builds the insert pop up.
	 */
	/*
	 * Build Insert Pop up.
	 */
	private void buildInsertPopUp() {
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		InsertIntoAceEditorDialogBox.showListOfItemAvailableForInsertDialogBox(curAceEditor);
		setIsPageDirty(true);
	}

	/**
	 * Delete definition.
	 */
	protected void deleteDefinition() {
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		final String definitionName = cqlStandaloneWorkspaceView.getDefineNameTxtArea().getText();
		if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
			final CQLDefinition toBeModifiedObj = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionMap()
					.get(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
			showSearchingBusy(true);
			MatContext.get().getCQLLibraryService().deleteDefinition(MatContext.get().getCurrentCQLLibraryId(),
					toBeModifiedObj, cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewDefinitions(),
					new AsyncCallback<SaveUpdateCQLResult>() {

				@Override
				public void onFailure(Throwable caught) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					showSearchingBusy(false);
				}

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					if (result != null) {
						if (result.isSuccess()) {
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
							.setViewDefinitions(result.getCqlModel().getDefinitionList());
							MatContext.get().setDefinitions(
									getDefinitionList(result.getCqlModel().getDefinitionList()));
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
							.clearAndAddDefinitionNamesToListBox();
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().updateDefineMap();
							messagePanel.getErrorMessageAlert().clearAlert();
							messagePanel.getSuccessMessageAlert().setVisible(true);
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox()
							.setText("");
							cqlStandaloneWorkspaceView.getDefineNameTxtArea().setText("");
							cqlStandaloneWorkspaceView.getDefineAceEditor().setText("");
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
							.setCurrentSelectedDefinitionObjId(null);
							setIsPageDirty(false);
							cqlStandaloneWorkspaceView.getDefineAceEditor().clearAnnotations();
							cqlStandaloneWorkspaceView.getDefineAceEditor().removeAllMarkers();

							cqlStandaloneWorkspaceView.getDefineAceEditor().setAnnotations();
							cqlStandaloneWorkspaceView.getDefineButtonBar().getDeleteButton().setEnabled(false);
							messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSuccessfulDefinitionRemoveMessage(toBeModifiedObj.getName()));
							cqlStandaloneWorkspaceView.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
						} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) { //TODO
							messagePanel.getSuccessMessageAlert().clearAlert();
							messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify.");
							cqlStandaloneWorkspaceView.getDefineNameTxtArea().setText(definitionName.trim());
						} else if (result.getFailureReason() == SaveUpdateCQLResult.SERVER_SIDE_VALIDATION) {
							messagePanel.getSuccessMessageAlert().clearAlert();
							messagePanel.getErrorMessageAlert().createAlert("Unauthorized delete operation.");
							cqlStandaloneWorkspaceView.getDefineNameTxtArea().setText(definitionName.trim());
						}
					}
					showSearchingBusy(false);
					//508 changes for Definitions Section
					cqlStandaloneWorkspaceView.getCQLDefinitionsView().getMainDefineViewVerticalPanel().setFocus(true);
				}
			});
		} else {
			cqlStandaloneWorkspaceView.resetMessageDisplay();
			messagePanel.getErrorMessageAlert().createAlert("Please select a definition to delete.");
			cqlStandaloneWorkspaceView.getDefineNameTxtArea().setText(definitionName.trim());
		}
	}

	/**
	 * Delete function.
	 */
	protected void deleteFunction() {

		cqlStandaloneWorkspaceView.resetMessageDisplay();
		final String functionName = cqlStandaloneWorkspaceView.getFuncNameTxtArea().getText();
		if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
			final CQLFunctions toBeModifiedFuncObj = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap()
					.get(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
			showSearchingBusy(true);
			MatContext.get().getCQLLibraryService().deleteFunctions(MatContext.get().getCurrentCQLLibraryId(),
					toBeModifiedFuncObj, cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewFunctions(),
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
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
							.setViewFunctions(result.getCqlModel().getCqlFunctions());
							MatContext.get()
							.setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().updateFunctionMap();
							messagePanel.getErrorMessageAlert().clearAlert();
							cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionArgNameMap().clear();
							cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionArgumentList().clear();
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText("");
							messagePanel.getSuccessMessageAlert().setVisible(true);
							cqlStandaloneWorkspaceView.getFuncNameTxtArea().setText("");
							cqlStandaloneWorkspaceView.getFunctionBodyAceEditor().setText("");
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
							cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
							setIsPageDirty(false);
							cqlStandaloneWorkspaceView.getFunctionBodyAceEditor().clearAnnotations();
							cqlStandaloneWorkspaceView.getFunctionBodyAceEditor().removeAllMarkers();

							cqlStandaloneWorkspaceView.getFunctionBodyAceEditor().setAnnotations();
							cqlStandaloneWorkspaceView.getFunctionButtonBar().getDeleteButton().setEnabled(false);
							messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSuccessfulFunctionRemoveMessage(toBeModifiedFuncObj.getName()));
							cqlStandaloneWorkspaceView.getCQLFunctionsView().getReturnTypeTextBox().setText("");

							if (result.getFunction() != null) {
								cqlStandaloneWorkspaceView.createAddArgumentViewForFunctions(
										new ArrayList<CQLFunctionArgument>());
							}

						} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) { //TODO
							messagePanel.getSuccessMessageAlert().clearAlert();
							messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify.");
							cqlStandaloneWorkspaceView.getFuncNameTxtArea().setText(functionName.trim());
						} else if (result.getFailureReason() == SaveUpdateCQLResult.SERVER_SIDE_VALIDATION) {
							messagePanel.getSuccessMessageAlert().clearAlert();
							messagePanel.getErrorMessageAlert().createAlert("Unauthorized delete operation.");
							cqlStandaloneWorkspaceView.getFuncNameTxtArea().setText(functionName.trim());
						}
					}
					showSearchingBusy(false);
					//508 Compliance for Function section
					cqlStandaloneWorkspaceView.getCQLFunctionsView().getMainFunctionVerticalPanel().setFocus(true);
				}
			});
		} else {
			cqlStandaloneWorkspaceView.resetMessageDisplay();
			messagePanel.getErrorMessageAlert().createAlert("Please select a function to delete.");
			cqlStandaloneWorkspaceView.getFuncNameTxtArea().setText(functionName.trim());
		}
	}

	protected void deleteFunctionArgument(){

		String funcArgName = null;

		cqlStandaloneWorkspaceView.resetMessageDisplay();
		setIsPageDirty(true);
		Iterator<CQLFunctionArgument> iterator = cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionArgumentList()
				.iterator();
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionArgNameMap()
		.remove(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentName().toLowerCase());
		while (iterator.hasNext()) {
			CQLFunctionArgument cqlFunArgument = iterator.next();
			if (cqlFunArgument.getId().equals(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId())) {

				iterator.remove();
				cqlStandaloneWorkspaceView.getCQLFunctionsView().createAddArgumentViewForFunctions(
						cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionArgumentList(),
						MatContext.get().getMeasureLockService().checkForEditPermission());
				funcArgName = cqlFunArgument.getArgumentName();
				break;
			}
		}

		//resetting name and id
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);

		//508 Compliance for Function section
		cqlStandaloneWorkspaceView.getCQLFunctionsView().getFuncNameTxtArea().setFocus(true);

		messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSuccessfulFunctionArgumentRemoveMessage(funcArgName));

	}

	/**
	 * Delete parameter.
	 */
	protected void deleteParameter() {

		cqlStandaloneWorkspaceView.resetMessageDisplay();
		final String parameterName = cqlStandaloneWorkspaceView.getParameterNameTxtArea().getText();
		if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
			final CQLParameter toBeModifiedParamObj = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterMap()
					.get(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());

			if(toBeModifiedParamObj.isReadOnly()){
				messagePanel.getSuccessMessageAlert().clearAlert();
				messagePanel.getErrorMessageAlert().createAlert("Unauthorized delete operation.");
				cqlStandaloneWorkspaceView.getParameterNameTxtArea().setText(parameterName.trim());
			} else {
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().deleteParameter(MatContext.get().getCurrentCQLLibraryId(),
						toBeModifiedParamObj,
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewParameterList(),
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
								cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
								.setViewParameterList((result.getCqlModel().getCqlParameters()));
								MatContext.get().setParameters(
										getParamaterList(result.getCqlModel().getCqlParameters()));
								cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
								cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().updateParamMap();
								messagePanel.getErrorMessageAlert().clearAlert();
								messagePanel.getSuccessMessageAlert().setVisible(true);

								cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox().setText("");
								cqlStandaloneWorkspaceView.getParameterNameTxtArea().setText("");
								cqlStandaloneWorkspaceView.getParameterAceEditor().setText("");
								cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
								.setCurrentSelectedParamerterObjId(null);
								setIsPageDirty(false);
								cqlStandaloneWorkspaceView.getParameterAceEditor().clearAnnotations();
								cqlStandaloneWorkspaceView.getParameterAceEditor().removeAllMarkers();
								cqlStandaloneWorkspaceView.getParameterAceEditor().setAnnotations();
								cqlStandaloneWorkspaceView.getParameterButtonBar().getDeleteButton().setEnabled(false);
								messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSuccessfulParameterRemoveMessage(toBeModifiedParamObj.getName()));  
							} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) { //TODO make a constant
								messagePanel.getSuccessMessageAlert().clearAlert();
								messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify.");
								cqlStandaloneWorkspaceView.getParameterNameTxtArea().setText(parameterName.trim());
							}else if (result.getFailureReason() == SaveUpdateCQLResult.SERVER_SIDE_VALIDATION) {
								messagePanel.getSuccessMessageAlert().clearAlert();
								messagePanel.getErrorMessageAlert().createAlert("Unauthorized delete operation.");
								cqlStandaloneWorkspaceView.getParameterNameTxtArea().setText(parameterName.trim());
							}
						}
						showSearchingBusy(false);
						//508 Compliance for Function section
						cqlStandaloneWorkspaceView.getCQLParametersView().getMainParamViewVerticalPanel().setFocus(true);
					}
				});
			} 
		}else {
			cqlStandaloneWorkspaceView.resetMessageDisplay();
			messagePanel.getErrorMessageAlert().createAlert("Please select parameter to delete.");
			cqlStandaloneWorkspaceView.getParameterNameTxtArea().setText(parameterName.trim());
		}
	}

	/**
	 * Delete include.
	 */
	protected void deleteInclude() {

		cqlStandaloneWorkspaceView.resetMessageDisplay();
		final String aliasName = cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().getText();
		if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() != null) {
			final CQLIncludeLibrary toBeModifiedIncludeObj = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
					.getIncludeLibraryMap()
					.get(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId());
			showSearchingBusy(true);
			MatContext.get().getCQLLibraryService().deleteInclude(MatContext.get().getCurrentCQLLibraryId(),
					toBeModifiedIncludeObj, 
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
					new AsyncCallback<SaveUpdateCQLResult>() {

				@Override
				public void onFailure(Throwable caught) {
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					showSearchingBusy(false);
				}

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					if (result.isSuccess()) {
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(result.getCqlModel().getCqlIncludeLibrarys());
						MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));

						MatContext.get().setIncludedValues(result);


						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
						messagePanel.getErrorMessageAlert().clearAlert();
						messagePanel.getSuccessMessageAlert().setVisible(true);
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
						cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().setText("");
						cqlStandaloneWorkspaceView.getIncludeView().getCqlLibraryNameTextBox().setText("");
						cqlStandaloneWorkspaceView.getIncludeView().getOwnerNameTextBox().setText("");
						cqlStandaloneWorkspaceView.getIncludeView().getViewCQLEditor().setText("");
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
						setIsPageDirty(false);
						cqlStandaloneWorkspaceView.getIncludeView().getViewCQLEditor().clearAnnotations();
						cqlStandaloneWorkspaceView.getIncludeView().getViewCQLEditor().removeAllMarkers();
						cqlStandaloneWorkspaceView.getIncludeView().getViewCQLEditor().setAnnotations();
						cqlStandaloneWorkspaceView.getIncludeView().getDeleteButton().setEnabled(false);

						cqlStandaloneWorkspaceView.getIncludeView().getCloseButton()
						.fireEvent(new GwtEvent<ClickHandler>() {
							@Override
							public com.google.gwt.event.shared.GwtEvent.Type<ClickHandler> getAssociatedType() {
								return ClickEvent.getType();
							}

							@Override
							protected void dispatch(ClickHandler handler) {
								handler.onClick(null);
							}
						});

						messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSuccessfulIncludeRemoveMessage(toBeModifiedIncludeObj.getAliasName()));
					} else if (result.getFailureReason() == SaveUpdateCQLResult.NODE_NOT_FOUND) { //TODO
						messagePanel.getSuccessMessageAlert().clearAlert();
						messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify.");
						cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
					}
					showSearchingBusy(false);
				}
			});
		} else {
			cqlStandaloneWorkspaceView.resetMessageDisplay();
			messagePanel.getErrorMessageAlert().createAlert("Please select an alias to delete.");
			cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
		}
	}

	private void deleteCode(){
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().deleteCode(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedCodesObjId(), MatContext.get().getCurrentCQLLibraryId(), new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onFailure(Throwable caught) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
				showSearchingBusy(false);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
				showSearchingBusy(false);
				if(result.isSuccess()){
					messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_CODE_REMOVE_MSG(result.getCqlCode().getCodeOID()));
					cqlStandaloneWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
					appliedCodeTableList.clear();
					appliedCodeTableList.addAll(result.getCqlCodeList());
					MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
					//searchDisplay.buildCodes();
					cqlStandaloneWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService().checkForEditPermission());
					getAppliedValueSetList();
				} else {
					messagePanel.getErrorMessageAlert().createAlert("Unable to delete.");		
				}

				//508 : Shift focus to code search panel.
				cqlStandaloneWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
			}
		});
	}

	private void checkAndDeleteValueSet(){
		MatContext.get().getLibraryService().getCQLData(MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onSuccess(final SaveUpdateCQLResult result) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(null);
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
								if (dataSetDTO.getUuid().equals(cqlStandaloneWorkspaceView.getValueSetView()
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
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(null);
				showSearchingBusy(false);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
	}
	/**
	 * Save CQL general information.
	 */
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
					cqlStandaloneWorkspaceView.getCqlGeneralInformationView().getLibraryNameValue().setText(cqlLibraryName);
					cqlStandaloneWorkspaceView.getCqlGeneralInformationView().getComments().setText(result.getCqlModel().getLibraryComment());
					cqlStandaloneWorkspaceView.getCqlGeneralInformationView().getComments().setCursorPos(0);
					messagePanel.getSuccessMessageAlert().createAlert(cqlLibraryName + " general information successfully updated");
					setIsPageDirty(false);
					MatContext.get().getCurrentLibraryInfo().setLibraryName(cqlLibraryName);
					CqlComposerPresenter.setContentHeading();
				}
				showSearchingBusy(false);
			}
		});

	}

	/**
	 * Log recent activity.
	 */
	private void logRecentActivity() {
		MatContext.get().getCQLLibraryService().isLibraryAvailableAndLogRecentActivity(
				MatContext.get().getCurrentCQLLibraryId(), MatContext.get().getLoggedinUserId(),
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// Do Nothing
					}

					@Override
					public void onSuccess(Void result) {
						isCQLWorkSpaceLoaded = true;
						displayCQLView();
					}
				});

	}

	/**
	 * Display CQL view.
	 */
	private void displayCQLView() {
		panel.clear();
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		cqlStandaloneWorkspaceView.buildView(messagePanel);
		addLeftNavEventHandler();
		getCQLDataForLoad(); 
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		panel.add(cqlStandaloneWorkspaceView.asWidget());
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		currentIncludeLibrarySetId = null;
		currentIncludeLibraryId = null; 
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().clearShotcutKeyList();
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		cqlStandaloneWorkspaceView.getFunctionArgNameMap().clear();
		cqlStandaloneWorkspaceView.getValueSetView().clearCellTableMainPanel();
		cqlStandaloneWorkspaceView.getCodesView().clearCellTableMainPanel();
		cqlStandaloneWorkspaceView.getIncludeView().getSearchTextBox().setText("");
		setIsPageDirty(false);
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
		.setClassName("panel-collapse collapse");
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
		.setClassName("panel-collapse collapse");
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
		.setClassName("panel-collapse collapse");
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
		.setClassName("panel-collapse collapse");
		if (cqlStandaloneWorkspaceView.getFunctionArgumentList().size() > 0) {
			cqlStandaloneWorkspaceView.getFunctionArgumentList().clear();
		}
		isModified = false;
		isCodeModified = false;
		setId = null;
		modifyValueSetDTO = null;
		curAceEditor = null;
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		messagePanel.clearAlerts();
		cqlStandaloneWorkspaceView.resetAll();
		setIsPageDirty(false);
		panel.clear();
		cqlStandaloneWorkspaceView.getMainPanel().clear();
		MatContext.get().getValuesets().clear();
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		if ((MatContext.get().getCurrentCQLLibraryId() == null)
				|| MatContext.get().getCurrentCQLLibraryId().isEmpty()) {
			displayEmpty();
		} else {
			panel.clear();
			cqlStandaloneWorkspaceView.getLockedButtonVPanel();
			panel.add(cqlStandaloneWorkspaceView.asWidget());
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

	/**
	 * Gets the CQL data.
	 *
	 * @return the CQL data
	 */
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
					cqlLibraryName = cqlStandaloneWorkspaceView.getCqlGeneralInformationView()
							.createCQLLibraryName(MatContext.get().getCurrentCQLLibraryeName());
					cqlLibraryComment = result.getCqlModel().getLibraryComment();
					String libraryVersion = MatContext.get().getCurrentCQLLibraryVersion();

					libraryVersion = libraryVersion.replaceAll("Draft ", "").trim();
					if (libraryVersion.startsWith("v")) {
						libraryVersion = libraryVersion.substring(1);
					}
					cqlStandaloneWorkspaceView.getCqlGeneralInformationView().setGeneralInfoOfLibrary(cqlLibraryName, libraryVersion,
							result.getCqlModel().getQdmVersion(), "QDM", cqlLibraryComment);
				}

				List<CQLQualityDataSetDTO> appliedAllValueSetList = new ArrayList<CQLQualityDataSetDTO>();
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
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setAppliedQdmTableList(appliedValueSetTableList);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);

				if (result.getCqlModel().getCodeList() != null) {
					appliedCodeTableList.addAll(result.getCqlModel().getCodeList());
				}
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setAppliedCodeTableList(appliedCodeTableList);
				MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);

				if ((result.getCqlModel().getDefinitionList() != null)
						&& (result.getCqlModel().getDefinitionList().size() > 0)) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
					.setViewDefinitions(result.getCqlModel().getDefinitionList());
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().updateDefineMap();
					MatContext.get().setDefinitions(getDefinitionList(result.getCqlModel().getDefinitionList()));
				} else {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineBadge().setText("00");
				}
				if ((result.getCqlModel().getCqlParameters() != null)
						&& (result.getCqlModel().getCqlParameters().size() > 0)) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
					.setViewParameterList(result.getCqlModel().getCqlParameters());
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().updateParamMap();
					MatContext.get().setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
				} else {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParamBadge().setText("00");
				}
				if ((result.getCqlModel().getCqlFunctions() != null)
						&& (result.getCqlModel().getCqlFunctions().size() > 0)) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setViewFunctions(result.getCqlModel().getCqlFunctions());
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().updateFunctionMap();
					MatContext.get().setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
				} else {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionBadge().setText("00");
				}
				if ((result.getCqlModel().getCqlIncludeLibrarys() != null)
						&& (result.getCqlModel().getCqlIncludeLibrarys().size() > 0)) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
					.setViewIncludeLibrarys(result.getCqlModel().getCqlIncludeLibrarys());
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
					MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
					MatContext.get().setIncludedValues(result);


				} else {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesBadge().setText("00");
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap().clear();
				}

				boolean isValidQDMVersion = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
						.checkForIncludedLibrariesQDMVersion(true); //true because its a standalone library
				if (!isValidQDMVersion) {
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_QDM_VERSION_IN_INCLUDES());
				} else {
					messagePanel.getErrorMessageAlert().clearAlert();
				}
			}
		}
	}

	/**
	 * Adds the left nav event handler.
	 */
	private void addLeftNavEventHandler() {

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getGeneralInformation().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// searchDisplay.hideAceEditorAutoCompletePopUp();
				generalInfoEvent();
			}
		});

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				// searchDisplay.hideAceEditorAutoCompletePopUp();
				if (getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
					showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					includesEvent();
				}

			}
		});

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCodesLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				// searchDisplay.hideAceEditorAutoCompletePopUp();
				if (getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_CODES;
					showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					codesEvent();
					//508 : Shift focus to code search panel.
					cqlStandaloneWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
				}

			}

		});

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQDM().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				appliedQDMEvent();
				//508 : Shift focus to search panel.
				cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
			}
		});
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlStandaloneWorkspaceView.hideAceEditorAutoCompletePopUp();
				if (getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
					showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					parameterEvent();
				}

			}
		});

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlStandaloneWorkspaceView.hideAceEditorAutoCompletePopUp();
				if (getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
					showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					definitionEvent();
				}
			}
		});

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlStandaloneWorkspaceView.hideAceEditorAutoCompletePopUp();
				if (getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
					showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					functionEvent();
				}
			}
		});

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewCQL().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.hideAceEditorAutoCompletePopUp();
				viewCqlEvent();
			}
		});

	}
	/**
	 * Gets the used artifacts.
	 *
	 * @return the used artifacts
	 */
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
						// if there are errors, set the valuesets to not used.
						if(!result.getCqlErrors().isEmpty()) {
							for(CQLQualityDataSetDTO cqlDTo : cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQdmTableList()){
								cqlDTo.setUsed(false);
							}
						}

						// otherwise, check if the valueset is in the used valusets list
						else {
							for(CQLQualityDataSetDTO cqlDTo : cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQdmTableList()){
								if (result.getUsedCQLValueSets().contains(cqlDTo.getName())) {
									cqlDTo.setUsed(true);
								} else{
									cqlDTo.setUsed(false);
								}
							}
						}

						if(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQdmTableList().size() > 0) {
							cqlStandaloneWorkspaceView.getValueSetView().getCelltable().redraw();
							cqlStandaloneWorkspaceView.getValueSetView().getListDataProvider().refresh();
						}

					}

				});

	}
	/**
	 * Adds the QDM Search Panel event Handlers.
	 */
	private void addValueSetEventHandlers() {
		/**
		 * this functionality is to clear the content on the QDM Element Search
		 * Panel.
		 */
		cqlStandaloneWorkspaceView.getValueSetView().getCancelQDMButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				isModified = false;
				cqlStandaloneWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
				//508 compliance for Value Sets
				cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().setFocus(true);

				previousIsProgramListBoxEnabled = isProgramListBoxEnabled;
				isProgramListBoxEnabled = true; 

				loadProgramsAndReleases(); 
				alert508StateChanges();
			}
		});

		cqlStandaloneWorkspaceView.getValueSetView().getUpdateFromVSACButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					updateVSACValueSets();
					//508 compliance for Value Sets
					cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
				}
			}
		});

		/**
		 * this functionality is to retrieve the value set from VSAC with latest
		 * information which consists of Expansion Profile list and Version
		 * List.
		 */
		cqlStandaloneWorkspaceView.getValueSetView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();					
					String release;
					String expansionProfile = null;

					release = cqlStandaloneWorkspaceView.getValueSetView().getReleaseListBox().getSelectedValue();
					release = MatContext.PLEASE_SELECT.equals(release) ? null : release;

					String program = cqlStandaloneWorkspaceView.getValueSetView().getProgramListBox().getSelectedValue();
					program = MatContext.PLEASE_SELECT.equals(program) ? null : program;


					if(null == release && null != program) {
						HashMap<String, String> pgmProfileMap = (HashMap<String, String>) MatContext.get().getProgramToLatestProfile();
						expansionProfile = pgmProfileMap.get(program);
					}

					if(release != null && program == null) {
						messagePanel.getErrorMessageAlert().createAlert(SharedCQLWorkspaceUtility.MUST_HAVE_PROGRAM_WITH_RELEASE);
					} else {
						searchValueSetInVsac(release, expansionProfile);

						//508 compliance for Value Sets
						cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().setFocus(true); 
					}
				}
			}
		});

		/**
		 * this handler is invoked when apply button is clicked on search Panel
		 * in QDM elements tab and this is to add new value set or user Defined
		 * QDM to the Applied QDM list.
		 */
		cqlStandaloneWorkspaceView.getValueSetView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					MatContext.get().clearDVIMessages();
					cqlStandaloneWorkspaceView.resetMessageDisplay();

					if (isModified && (modifyValueSetDTO != null)) {
						modifyValueSetOrUserDefined(isUserDefined);
					} else {
						addNewValueSet(isUserDefined);
					}
					//508 compliance for Value Sets
					cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().setFocus(true); 
				}
			}
		});

		/**
		 * Adding value Change handler for UserDefined Input in Search Panel in
		 * QDM Elements Tab
		 * 
		 */
		cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				cqlStandaloneWorkspaceView.resetMessageDisplay();
				isUserDefined = cqlStandaloneWorkspaceView.getValueSetView().validateUserDefinedInput();
			}
		});

		/**
		 * Adding value change handler for OID input in Search Panel in QDM
		 * elements Tab
		 */
		cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().addValueChangeHandler(event -> clearOID());

		cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().sinkBitlessEvent("input");

		cqlStandaloneWorkspaceView.getValueSetView().setObserver(new CQLAppliedValueSetView.Observer() {
			@Override
			public void onModifyClicked(CQLQualityDataSetDTO result) {
				if(!cqlStandaloneWorkspaceView.getValueSetView().getIsLoading()){
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					cqlStandaloneWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
					isModified = true;
					modifyValueSetDTO = result;
					String displayName = result.getName();
					// Substring at 60th character length.
					if(displayName.length() >=60){
						displayName = displayName.substring(0, 59);
					}
					HTML searchHeaderText = new HTML("<strong>Modify value set ( "+displayName +")</strong>");
					cqlStandaloneWorkspaceView.getValueSetView().getSearchHeader().clear();
					cqlStandaloneWorkspaceView.getValueSetView().getSearchHeader().add(searchHeaderText);
					cqlStandaloneWorkspaceView.getValueSetView().getMainPanel().getElement().focus();
					if(result.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)){
						isUserDefined = true;
					} else {
						isUserDefined = false;
					}

					onModifyValueSet(result, isUserDefined);
					//508 Compliance for Value Sets section
					cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
				} else {
					//do nothing when loading.
				}
			}

			@Override
			public void onDeleteClicked(CQLQualityDataSetDTO result, final int index) {
				if(!cqlStandaloneWorkspaceView.getValueSetView().getIsLoading()){
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					cqlStandaloneWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
					if((modifyValueSetDTO!=null) && modifyValueSetDTO.getId().equalsIgnoreCase(result.getId())){
						isModified = false;
					}
					String libraryId = MatContext.get().getCurrentCQLLibraryId();
					if ((libraryId != null) && !libraryId.equals("")) {
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(result.getId());
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_VALUESET(result.getName()));
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
						//508 Compliance for Value Sets section
						cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
					}
				} else {
					//do nothing when loading.
				}
			}

		});

		cqlStandaloneWorkspaceView.getValueSetView().getClearButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if(!cqlStandaloneWorkspaceView.getValueSetView().getIsLoading()){
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					cqlStandaloneWorkspaceView.getValueSetView().clearSelectedCheckBoxes();
				}
			}
		});

		cqlStandaloneWorkspaceView.getValueSetView().getCopyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				copyValueSets();
			}
		});

		cqlStandaloneWorkspaceView.getValueSetView().getSelectAllButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectAllValueSets();
			}
		});

		cqlStandaloneWorkspaceView.getValueSetView().getPasteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getLibraryLockService().checkForEditPermission()) {
					pasteValueSets();
				} else {
					event.preventDefault();
				}
			}
		});

		cqlStandaloneWorkspaceView.getValueSetView().getReleaseListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				isRetrieveButtonEnabled = true;
				cqlStandaloneWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);				
				previousIsApplyButtonEnabled = isApplyButtonEnabled;
				isApplyButtonEnabled = false;
				cqlStandaloneWorkspaceView.getValueSetView().getSaveButton().setEnabled(isApplyButtonEnabled);
				alert508StateChanges();
			}
		});

		cqlStandaloneWorkspaceView.getValueSetView().getProgramListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				isRetrieveButtonEnabled = true;
				cqlStandaloneWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);

				previousIsApplyButtonEnabled = isApplyButtonEnabled;
				isApplyButtonEnabled = false; 
				cqlStandaloneWorkspaceView.getValueSetView().getSaveButton().setEnabled(isApplyButtonEnabled);

				CQLAppliedValueSetUtility.loadReleases(cqlStandaloneWorkspaceView.getValueSetView().getReleaseListBox(), cqlStandaloneWorkspaceView.getValueSetView().getProgramListBox());

				alert508StateChanges();
			}
		});
	}

	private void clearOID() {

		previousIsRetrieveButtonEnabled = isRetrieveButtonEnabled;
		previousIsProgramListBoxEnabled = isProgramListBoxEnabled;

		cqlStandaloneWorkspaceView.resetMessageDisplay();
		isUserDefined = cqlStandaloneWorkspaceView.getValueSetView().validateOIDInput();

		if (cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().getValue().length() <= 0 ) {
			isRetrieveButtonEnabled = true;
			isProgramListBoxEnabled = true;
			cqlStandaloneWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);
			loadProgramsAndReleases();
		} else {
			isRetrieveButtonEnabled = true;
			cqlStandaloneWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);
		}

		alert508StateChanges();
	}

	private void alert508StateChanges() {
		StringBuilder helpTextBuilder = new StringBuilder();

		helpTextBuilder.append(build508HelpString(previousIsProgramListBoxEnabled, isProgramListBoxEnabled, "Program and Release List Boxes"));
		helpTextBuilder.append(build508HelpString(previousIsRetrieveButtonEnabled, isRetrieveButtonEnabled, "Retrieve Button"));
		helpTextBuilder.append(build508HelpString(previousIsApplyButtonEnabled, isApplyButtonEnabled, "Apply Button"));

		cqlStandaloneWorkspaceView.getValueSetView().getHelpBlock().setText(helpTextBuilder.toString());
	}

	private String build508HelpString(boolean previousState, boolean currentState, String elementName) {

		String helpString = "";
		if(currentState != previousState) {
			helpString = elementName.concat(" ").concat(Boolean.TRUE.equals(currentState) ? "enabled" : "disabled");
		}

		return helpString; 
	}

	private void copyValueSets() {
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		if(cqlStandaloneWorkspaceView.getValueSetView().getQdmSelectedList() != null &&
				cqlStandaloneWorkspaceView.getValueSetView().getQdmSelectedList().size() > 0){
			mat.model.GlobalCopyPasteObject gbCopyPaste = new GlobalCopyPasteObject();
			gbCopyPaste.setCopiedValueSetList(cqlStandaloneWorkspaceView.getValueSetView().getQdmSelectedList());
			MatContext.get().setGlobalCopyPaste(gbCopyPaste);
			messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().VALUE_SETS_COPIED_SUCCESSFULLY);
		} else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCOPY_QDM_SELECT_ATLEAST_ONE());
		}
	}

	private void selectAllValueSets() {
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		if(cqlStandaloneWorkspaceView.getValueSetView().getAllValueSets() != null &&
				cqlStandaloneWorkspaceView.getValueSetView().getAllValueSets().size() > 0){
			cqlStandaloneWorkspaceView.getValueSetView().selectAll();
			messagePanel.getSuccessMessageAlert().createAlert(VALUE_SETS_SELECTED_SUCCESSFULLY);
		}
	}

	/**
	 * paste Value Sets. This functionality is to paste all the Value sets elements that have been copied
	 * from any Measure and can be pasted to any measure.
	 */
	private void pasteValueSets() {
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		GlobalCopyPasteObject gbCopyPaste = MatContext.get().getGlobalCopyPaste();
		showSearchingBusy(true);
		if ((gbCopyPaste != null) && (gbCopyPaste.getCopiedValueSetList().size() > 0)) {
			List<CQLValueSetTransferObject> cqlValueSetTransferObjectsList = cqlStandaloneWorkspaceView.getValueSetView()
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
							messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFULLY_VALUESET_PASTE());
						}
					}
				});
			} else {
				showSearchingBusy(false);
				messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFULLY_VALUESET_PASTE());
			}
			MatContext.get().getGlobalCopyPaste().getCopiedValueSetList().clear();
			;
		} else {
			showSearchingBusy(false);
			messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWARNING_PASTING_IN_VALUESET());
		}	 
	}


	private void copyCodes() {
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		if(cqlStandaloneWorkspaceView.getCodesView().getCodesSelectedList() != null && cqlStandaloneWorkspaceView.getCodesView().getCodesSelectedList().size() > 0){
			mat.model.GlobalCopyPasteObject gbCopyPaste = new GlobalCopyPasteObject();
			gbCopyPaste.setCopiedCodeList(cqlStandaloneWorkspaceView.getCodesView().getCodesSelectedList());
			MatContext.get().setGlobalCopyPaste(gbCopyPaste);
			messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().CODES_COPIED_SUCCESSFULLY);
		} else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCOPY_CODE_SELECT_ATLEAST_ONE());
		}
	}

	private void selectAllCodes() {
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		if(cqlStandaloneWorkspaceView.getCodesView().getAllCodes() != null) {
			cqlStandaloneWorkspaceView.getCodesView().selectAll();
			messagePanel.getSuccessMessageAlert().createAlert(CODES_SELECTED_SUCCESSFULLY);
		}
	}

	/**
	 * paste Value Sets. This functionality is to paste all the Value sets elements that have been copied
	 * from any Measure and can be pasted to any measure.
	 */
	private void pasteCodes() {
		cqlStandaloneWorkspaceView.resetMessageDisplay();
		GlobalCopyPasteObject gbCopyPaste = MatContext.get().getGlobalCopyPaste();
		if( (gbCopyPaste != null) && (gbCopyPaste.getCopiedCodeList().size()>0) ){
			List<CQLCode> codesToPaste = cqlStandaloneWorkspaceView.getCodesView().setMatCodeList(gbCopyPaste.getCopiedCodeList(), appliedCodeTableList);
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
						messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().SUCCESSFULLY_PASTED_CODES_IN_MEASURE);
						cqlStandaloneWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);
						cqlStandaloneWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService().checkForEditPermission());
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						if (result != null && result.getCqlModel().getAllValueSetAndCodeList() != null) {
							setAppliedValueSetListInTable(result.getCqlModel().getAllValueSetAndCodeList());
						}
						showSearchingBusy(false);
					}
				});
			} else {
				showSearchingBusy(false);
				messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().SUCCESSFULLY_PASTED_CODES_IN_MEASURE);
			}
			MatContext.get().getGlobalCopyPaste().getCopiedCodeList().clear();
		} else {
			showSearchingBusy(false);
			messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().CLIPBOARD_DOES_NOT_CONTAIN_CODES);
		}
	}

	private void addCodeSearchPanelHandlers() {
		cqlStandaloneWorkspaceView.getCodesView().getCopyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				copyCodes();
			}
		});

		cqlStandaloneWorkspaceView.getCodesView().getSelectAllButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectAllCodes();
			}
		});

		cqlStandaloneWorkspaceView.getCodesView().getPasteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getLibraryLockService().checkForEditPermission()) {
					pasteCodes();
				} else {
					event.preventDefault();
				}
			}
		});
		cqlStandaloneWorkspaceView.getCodesView().getClearButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (!cqlStandaloneWorkspaceView.getCodesView().getIsLoading()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					cqlStandaloneWorkspaceView.getCodesView().clearSelectedCheckBoxes();
				}
			}
		});

		cqlStandaloneWorkspaceView.getCodesView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					if (!isCodeModified)
						searchCQLCodesInVsac();
					//508 Compliance for Codes section
					cqlStandaloneWorkspaceView.getCodesView().getCodeInput().setFocus(true);
				}
			}
		});

		cqlStandaloneWorkspaceView.getCodesView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					MatContext.get().clearDVIMessages();
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					if(isCodeModified && modifyCQLCode != null) {
						modifyCodes();
					} else if (null != cqlStandaloneWorkspaceView.getCodesView().getCodeSearchInput().getValue() 
							&& !cqlStandaloneWorkspaceView.getCodesView().getCodeSearchInput().getValue().isEmpty()) {
						addNewCodes();	
					}
					//508 Compliance for Codes section
					cqlStandaloneWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
				}

			}
		});

		cqlStandaloneWorkspaceView.getCodesView().getCancelCodeButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					isCodeModified = false;
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					cqlStandaloneWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
					//508 Compliance for Codes section
					cqlStandaloneWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
				}
			}
		});
		cqlStandaloneWorkspaceView.getCodesView().setDelegator(new Delegator() {

			@Override
			public void onDeleteClicked(CQLCode result, int index) {
				if(!cqlStandaloneWorkspaceView.getCodesView().getIsLoading()) {
					messagePanel.getSuccessMessageAlert().clearAlert();
					messagePanel.getErrorMessageAlert().clearAlert();
					if(result != null){
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(result.getId());
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_CODES(result.getCodeOID()));
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
						//508 Compliance for Codes section
						cqlStandaloneWorkspaceView.getCodesView().getCodeInput().setFocus(true);
					}
				} else {
					// table is loading, do nothing
				}		

			}

			@Override
			public void onModifyClicked(CQLCode object) {
				if(!cqlStandaloneWorkspaceView.getValueSetView().getIsLoading()){
					cqlStandaloneWorkspaceView.resetMessageDisplay();
					cqlStandaloneWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
					isCodeModified = true;
					modifyCQLCode = object;
					cqlStandaloneWorkspaceView.getCodesView().setValidateCodeObject(modifyCQLCode);
					String displayName = object.getCodeOID();
					// Substring at 60th character length.
					if(displayName.length() >=60){
						displayName = displayName.substring(0, 59);
					}
					HTML searchHeaderText = new HTML("<strong>Modify Code ( "+displayName +")</strong>");
					cqlStandaloneWorkspaceView.getCodesView().getSearchHeader().clear();
					cqlStandaloneWorkspaceView.getCodesView().getSearchHeader().add(searchHeaderText);
					cqlStandaloneWorkspaceView.getCodesView().getMainPanel().getElement().focus();

					onModifyCode(object);
					//508 Compliance for Value Sets section
					cqlStandaloneWorkspaceView.getCodesView().getCodeInput().setFocus(true);
				} else {
					//do nothing when loading.
				}
			}
		});
	}


	/**
	 * modify codes
	 */
	private void modifyCodes() {
		String cqlLibraryId = MatContext.get().getCurrentCQLLibraryId();	
		String codeName = cqlStandaloneWorkspaceView.getCodesView().getCodeDescriptorInput().getValue();
		codeName = StringUtility.removeEscapedCharsFromString(codeName);
		CQLCode refCode = buildCQLCodeFromCodesView(codeName);

		MatCodeTransferObject transferObject = cqlStandaloneWorkspaceView.getCodesView().getCodeTransferObject(cqlLibraryId, refCode);

		if (null != transferObject) {

			appliedCodeTableList.removeIf(code -> code.getDisplayName().equals(modifyCQLCode.getDisplayName()));

			if(!cqlStandaloneWorkspaceView.getCodesView().checkCodeInAppliedCodeTableList(refCode.getDisplayName(), appliedCodeTableList)) {
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
						messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_CODE());
						cqlStandaloneWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						if (result.getCqlModel().getAllValueSetAndCodeList() != null) {
							setAppliedValueSetListInTable(result.getCqlModel().getAllValueSetAndCodeList());
						}
						cqlStandaloneWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService().checkForEditPermission());
						//Temporary fix to update codes for insert Icon.
						getAppliedValueSetList();
						showSearchingBusy(false);
						cqlStandaloneWorkspaceView.getCodesView().getSaveButton().setEnabled(false);
						isCodeModified = false;
						modifyCQLCode = null;
					}
				});
			} else {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(refCode.getDisplayName()));
			}
		}
	}

	/**
	 * Adds the new codes.
	 */
	private void addNewCodes() {
		String cqlLibraryId = MatContext.get().getCurrentCQLLibraryId();
		final String codeName = StringUtility.removeEscapedCharsFromString(cqlStandaloneWorkspaceView.getCodesView().getCodeDescriptorInput().getValue());
		CQLCode refCode = buildCQLCodeFromCodesView(codeName);
		final String codeSystemName = refCode.getCodeSystemName();
		final String codeId = refCode.getCodeOID();

		MatCodeTransferObject transferObject = cqlStandaloneWorkspaceView.getCodesView().getCodeTransferObject(cqlLibraryId, refCode);

		if (null != transferObject) {
			showSearchingBusy(true);
			cqlService.saveCQLCodestoCQLLibrary(transferObject, new AsyncCallback<SaveUpdateCQLResult>() {

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {

					if(result.isSuccess()){
						messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCodeSuccessMessage(cqlStandaloneWorkspaceView.getCodesView().getCodeInput().getText()));
						cqlStandaloneWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						cqlStandaloneWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService().checkForEditPermission());
						cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						//Temporary fix to update codes for insert Icon.
						getAppliedValueSetList();
					} else {
						messagePanel.getSuccessMessageAlert().clearAlert();
						if(result.getFailureReason()==result.getDuplicateCode()){
							messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().generateDuplicateErrorMessage(codeName));
						}

						else if(result.getFailureReason() ==  result.getBirthdateOrDeadError()) {
							messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getBirthdateOrDeadMessage(codeSystemName, codeId));
							cqlStandaloneWorkspaceView.getCodesView().buildCodesCellTable(
									appliedCodeTableList,
									MatContext.get().getMeasureLockService().checkForEditPermission());
						}
					}
					showSearchingBusy(false);
					//508 : Shift focus to code search panel.
					cqlStandaloneWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
					cqlStandaloneWorkspaceView.getCodesView().getSaveButton().setEnabled(!result.isSuccess());
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
					showSearchingBusy(false);

				}
			});
		}
	}

	private CQLCode buildCQLCodeFromCodesView(String codeName) {
		CQLCode refCode = new CQLCode();
		CQLCodesView codesView = cqlStandaloneWorkspaceView.getCodesView();
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

	/**
	 * Update vsac value sets.
	 */
	private void updateVSACValueSets() {
		showSearchingBusy(true);
		String expansionId = null;

		cqlService.updateCQLVSACValueSets(MatContext.get().getCurrentCQLLibraryId(), expansionId,
				new AsyncCallback<VsacApiResult>() {

			@Override
			public void onFailure(final Throwable caught) {
				showSearchingBusy(false);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());

			}

			@Override
			public void onSuccess(final VsacApiResult result) {

				if (result.isSuccess()) {
					messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVSAC_UPDATE_SUCCESSFULL());
					List<CQLQualityDataSetDTO> appliedListModel = new ArrayList<CQLQualityDataSetDTO>();
					for (CQLQualityDataSetDTO cqlQDMDTO : result.getUpdatedCQLQualityDataDTOLIst()) {
						if (!ConstantMessages.DEAD_OID.equals(cqlQDMDTO
								.getDataType()) && !ConstantMessages.BIRTHDATE_OID.equals(cqlQDMDTO
										.getDataType())
								&& (cqlQDMDTO.getType() == null))  {
							appliedListModel.add(cqlQDMDTO);
							//Update existing Table value set list
							for(CQLQualityDataSetDTO cqlQualityDataSetDTO : appliedValueSetTableList){
								if(cqlQualityDataSetDTO.getId().equals(cqlQDMDTO.getId())){
									cqlQualityDataSetDTO.setOriginalCodeListName(cqlQDMDTO.getOriginalCodeListName());
									cqlQualityDataSetDTO.setName(cqlQDMDTO.getName());
								}
							}
							// update existing Value set list for Insert Button and short cut keys
							for(CQLIdentifierObject cqlIdentifierObject : MatContext.get().getValuesets()){
								if(cqlIdentifierObject.getId().equals(cqlQDMDTO.getId())){
									cqlIdentifierObject.setIdentifier(cqlQDMDTO.getName());
								}
							}
							// Update value set list for Attribute builder.
							for(CQLQualityDataSetDTO dataSetDTO : MatContext.get().getValueSetCodeQualityDataSetList()){
								if(dataSetDTO.getId().equals(cqlQDMDTO.getId())){
									dataSetDTO.setOriginalCodeListName(cqlQDMDTO.getOriginalCodeListName());
									dataSetDTO.setName(cqlQDMDTO.getName());
								}
							}
						}
					}
					cqlStandaloneWorkspaceView.getValueSetView().buildAppliedValueSetCellTable(appliedListModel,
							MatContext.get().getLibraryLockService().checkForEditPermission());
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setAppliedQdmTableList(appliedValueSetTableList);
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);

				} else {
					messagePanel.getErrorMessageAlert().createAlert(cqlStandaloneWorkspaceView.getValueSetView().convertMessage(result.getFailureReason()));
				}
				showSearchingBusy(false);
			}
		});
	}

	/**
	 * Search value set in vsac.
	 *
	 * @param version
	 *            the version
	 * @param expansionProfile
	 *            the expansion profile
	 */
	private void searchValueSetInVsac(String release, String expansionProfile) {
		showSearchingBusy(true);
		currentMatValueSet = null;
		final String oid = cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().getValue();
		if (!MatContext.get().isUMLSLoggedIn()) {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			messagePanel.getErrorMessageAlert().setVisible(true);
			showSearchingBusy(false);
			return;
		}

		// OID validation.
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
				// to get the VSAC version list corresponding the OID
				if (result.isSuccess()) {
					List<MatValueSet> matValueSets = result.getVsacResponse();
					if (matValueSets != null) {						
						currentMatValueSet = matValueSets.get(0);
					}
					cqlStandaloneWorkspaceView.getValueSetView().getOIDInput().setTitle(oid);
					cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput()
					.setValue(currentMatValueSet.getDisplayName());
					cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput()
					.setTitle(currentMatValueSet.getDisplayName());

					cqlStandaloneWorkspaceView.getValueSetView().getSaveButton().setEnabled(true);					
					messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getValuesetSuccessfulReterivalMessage(matValueSets.get(0).getDisplayName()));
					messagePanel.getSuccessMessageAlert().setVisible(true);
				} else {
					String message = cqlStandaloneWorkspaceView.getValueSetView().convertMessage(result.getFailureReason());
					messagePanel.getErrorMessageAlert().createAlert(message);
					messagePanel.getErrorMessageAlert().setVisible(true);
				}
				showSearchingBusy(false);
			}
		});
	}


	/**
	 * Search CQL codes in vsac.
	 */
	private void searchCQLCodesInVsac() {

		final String url = cqlStandaloneWorkspaceView.getCodesView().getCodeSearchInput().getValue().trim();
		cqlStandaloneWorkspaceView.getCodesView().getCodeSearchInput().setText(url);

		if (!MatContext.get().isUMLSLoggedIn()) {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			messagePanel.getErrorMessageAlert().setVisible(true);

			return;
		}

		// Code Identifier validation.
		if ((url == null) || url.trim().isEmpty()) {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_CODE_IDENTIFIER_REQUIRED());
			return;
		}

		// Code Identifier validation to check Identifier starts with "CODE:"
		if(validator.validateForCodeIdentifier(url)){
			cqlStandaloneWorkspaceView.getCodesView().getSaveButton().setEnabled(false);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_INVALID_CODE_IDENTIFIER());
			return;
		} else {
			retrieveCodeReferences(url);
		}

	}


	/**
	 * Retrieve code references.
	 *
	 * @param url the url
	 */
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
					cqlStandaloneWorkspaceView.getCodesView().getCodeDescriptorInput().setValue(result.getDirectReferenceCode().getCodeDescriptor());
					cqlStandaloneWorkspaceView.getCodesView().getCodeInput().setValue(result.getDirectReferenceCode().getCode());
					cqlStandaloneWorkspaceView.getCodesView().getCodeSystemInput().setValue(result.getDirectReferenceCode().getCodeSystemName());
					cqlStandaloneWorkspaceView.getCodesView().getCodeSystemVersionInput().setValue(result.getDirectReferenceCode().getCodeSystemVersion());
					cqlStandaloneWorkspaceView.getCodesView().setCodeSystemOid(result.getDirectReferenceCode().getCodeSystemOid());
					cqlStandaloneWorkspaceView.getCodesView().getSaveButton().setEnabled(true);

					messagePanel.getSuccessMessageAlert().createAlert("Code "+result.getDirectReferenceCode().getCode()+" successfully retrieved from VSAC.");
					CQLCode code = buildCQLCodeFromCodesView(StringUtility.removeEscapedCharsFromString(cqlStandaloneWorkspaceView.getCodesView().getCodeDescriptorInput().getValue()));
					cqlStandaloneWorkspaceView.getCodesView().setValidateCodeObject(code);

				} else {
					String message = cqlStandaloneWorkspaceView.getCodesView().convertMessage(result.getFailureReason());
					messagePanel.getErrorMessageAlert().createAlert(message);
					messagePanel.getErrorMessageAlert().setVisible(true);
				}

				showSearchingBusy(false);
				//508 : Shift focus to code search panel.
				cqlStandaloneWorkspaceView.getCodesView().getCodeSearchInput().setFocus(true);
			}
		});
	}


	/**
	 * Modify QDM.
	 *
	 * @param isUserDefined
	 *            the is user defined
	 */
	protected final void modifyValueSetOrUserDefined(final boolean isUserDefined) {
		if (!isUserDefined) { // Normal Available Value Set Flow
			modifyValueSet();
		} else { // Pseudo Value set Flow
			modifyUserDefinedValueSet();
		}
	}

	/**
	 * Modify value set QDM.
	 */
	private void modifyValueSet() {
		// Normal Available QDM Flow
		MatValueSet modifyWithDTO = currentMatValueSet;
		if ((modifyValueSetDTO != null) && (modifyWithDTO != null)) {
			String originalName = cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput().getText();
			String suffix = cqlStandaloneWorkspaceView.getValueSetView().getSuffixInput().getValue();
			String displayName = (!originalName.isEmpty() ? originalName : "")  + (!suffix.isEmpty() ? " (" + suffix + ")" : "");

			if (modifyValueSetDTO.getVersion() == null) {
				modifyValueSetDTO.setVersion("");
			}

			String releaseValue = cqlStandaloneWorkspaceView.getValueSetView().getReleaseListBox().getSelectedValue();
			if(releaseValue == null) {
				modifyValueSetDTO.setRelease("");
			} else if(!releaseValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
				modifyValueSetDTO.setRelease(releaseValue);
				modifyValueSetDTO.setVersion("");
			} else {
				modifyValueSetDTO.setRelease("");
			}

			String programValue = cqlStandaloneWorkspaceView.getValueSetView().getProgramListBox().getSelectedValue();
			if(!programValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
				modifyValueSetDTO.setProgram(programValue);
			} else {
				modifyValueSetDTO.setProgram("");
			}

			modifyValueSetList(modifyValueSetDTO);


			if (!cqlStandaloneWorkspaceView.getValueSetView().checkNameInValueSetList(displayName,appliedValueSetTableList)) {

				if(!cqlStandaloneWorkspaceView.getValueSetView().getSuffixInput().getValue().isEmpty()){
					modifyValueSetDTO.setSuffix(cqlStandaloneWorkspaceView.getValueSetView().getSuffixInput().getValue());
					modifyValueSetDTO.setName(originalName+" ("+cqlStandaloneWorkspaceView.getValueSetView().getSuffixInput().getValue()+")");
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

	/**
	 * Modify QDM list.
	 *
	 * @param qualityDataSetDTO
	 *            the quality data set DTO
	 */
	private void modifyValueSetList(CQLQualityDataSetDTO qualityDataSetDTO) {
		for (int i = 0; i < appliedValueSetTableList.size(); i++) {
			if (qualityDataSetDTO.getName().equals(appliedValueSetTableList.get(i).getName())) {
				appliedValueSetTableList.remove(i);
				break;

			}
		}
	}

	/**
	 * Update applied Value Set list.
	 *
	 * @param matValueSet
	 *            the mat value set
	 * @param codeListSearchDTO
	 *            the code list search DTO
	 * @param qualityDataSetDTO
	 *            the quality data set DTO
	 * @param isUSerDefined
	 *            the is U ser defined
	 */
	private void updateAppliedValueSetsList(final MatValueSet matValueSet, final CodeListSearchDTO codeListSearchDTO,
			final CQLQualityDataSetDTO qualityDataSetDTO) {		
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		matValueSetTransferObject.setCqlLibraryId(MatContext.get().getCurrentCQLLibraryId());
		matValueSetTransferObject.setMatValueSet(matValueSet);
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setCqlQualityDataSetDTO(qualityDataSetDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		matValueSetTransferObject.setUserDefinedText(cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput().getText());
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
				cqlStandaloneWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
			}

			@Override
			public void onSuccess(final SaveUpdateCQLResult result) {
				if (result != null) {
					if (result.isSuccess()) {
						isModified = false;
						modifyValueSetDTO = null;
						currentMatValueSet = null;
						cqlStandaloneWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();

						messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
						getAppliedValueSetList();
					} else {
						if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
							messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getName()));
						} else if (result.getFailureReason() == SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION) {
							messagePanel.getErrorMessageAlert().createAlert("Invalid Input data.");
						}
					}
				}
				cqlStandaloneWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
				showSearchingBusy(false);
			}
		});

	}

	/**
	 * Adds the selected code listto library.
	 *
	 * @param isUserDefinedValueSet
	 *            the is user defined QDM
	 */
	private void addNewValueSet(final boolean isUserDefinedValueSet) {
		if (!isUserDefinedValueSet) {
			addVSACCQLValueset();
		} else {
			addUserDefinedValueSet();
		}
	}

	/**
	 * Adds the QDS with value set.
	 */
	private void addVSACCQLValueset() {

		String libraryID = MatContext.get().getCurrentCQLLibraryId();
		CQLValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(libraryID);
		matValueSetTransferObject.scrubForMarkUp();
		String originalCodeListName = matValueSetTransferObject.getMatValueSet().getDisplayName(); 
		String suffix = cqlStandaloneWorkspaceView.getValueSetView().getSuffixInput().getValue();
		final String codeListName = (originalCodeListName!=null ? originalCodeListName : "") + (!suffix.isEmpty() ? " (" + suffix + ")" : "");

		// Check if QDM name already exists in the list.
		if (!cqlStandaloneWorkspaceView.getValueSetView().checkNameInValueSetList(codeListName,appliedValueSetTableList)) {
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
					cqlStandaloneWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
				}

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					String message = "";
					if (result != null) {
						if (result.isSuccess()) {

							message = MatContext.get().getMessageDelegate()
									.getValuesetSuccessMessage(codeListName);
							MatContext.get().getEventBus().fireEvent(new QDSElementCreatedEvent(codeListName));
							cqlStandaloneWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
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
					cqlStandaloneWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);

					showSearchingBusy(false);
				}
			});
		}  else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(codeListName));
		}

	}

	/**
	 * Adds the QDS with out value set.
	 */
	private void addUserDefinedValueSet() {

		CQLValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(
				MatContext.get().getCurrentCQLLibraryId());
		matValueSetTransferObject.scrubForMarkUp();
		matValueSetTransferObject.setMatValueSet(null);
		if ((matValueSetTransferObject.getUserDefinedText().length() > 0)) {
			ValueSetNameInputValidator valueSetNameInputValidator = new ValueSetNameInputValidator();
			String message = valueSetNameInputValidator.validate(matValueSetTransferObject);
			if (message.isEmpty()) {
				final String userDefinedInput = matValueSetTransferObject.getCqlQualityDataSetDTO().getName(); 

				// Check if QDM name already exists in the list.
				if (!cqlStandaloneWorkspaceView.getValueSetView().checkNameInValueSetList(userDefinedInput,appliedValueSetTableList)) {
					showSearchingBusy(true);
					MatContext.get().getLibraryService().saveCQLUserDefinedValueset(matValueSetTransferObject,
							new AsyncCallback<SaveUpdateCQLResult>() {
						@Override
						public void onFailure(final Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							showSearchingBusy(false);
							cqlStandaloneWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
						}

						@Override
						public void onSuccess(final SaveUpdateCQLResult result) {
							if (result != null) {
								if (result.isSuccess()) {
									if (result.getXml() != null) {
										String message = MatContext.get().getMessageDelegate().getValuesetSuccessMessage(userDefinedInput);
										messagePanel.getSuccessMessageAlert().createAlert(message);
										MatContext.get().setValuesets(result.getCqlAppliedQDMList());
										cqlStandaloneWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
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
							cqlStandaloneWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
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

	/**
	 * Creates the value set transfer object.
	 *
	 * @param libraryID
	 *            the library ID
	 * @return the CQL value set transfer object
	 */
	private CQLValueSetTransferObject createValueSetTransferObject(String libraryID) {
		if(currentMatValueSet == null) {
			currentMatValueSet = new MatValueSet();
		}
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		matValueSetTransferObject.setCqlLibraryId(libraryID);

		String originalCodeListName = cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput().getValue(); 

		matValueSetTransferObject.setCqlQualityDataSetDTO(new CQLQualityDataSetDTO());
		matValueSetTransferObject.getCqlQualityDataSetDTO().setOriginalCodeListName(originalCodeListName);

		if(!cqlStandaloneWorkspaceView.getValueSetView().getSuffixInput().getValue().isEmpty()){
			matValueSetTransferObject.getCqlQualityDataSetDTO().setSuffix(cqlStandaloneWorkspaceView.getValueSetView().getSuffixInput().getValue());
			matValueSetTransferObject.getCqlQualityDataSetDTO().setName(originalCodeListName+" ("+cqlStandaloneWorkspaceView.getValueSetView().getSuffixInput().getValue()+")");
		} else {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setName(originalCodeListName);
		}

		// set it to empty string to begin with
		matValueSetTransferObject.getCqlQualityDataSetDTO().setRelease("");
		String releaseValue = cqlStandaloneWorkspaceView.getValueSetView().getReleaseListBox().getSelectedValue(); 
		if(!releaseValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setRelease(releaseValue);
		}

		matValueSetTransferObject.getCqlQualityDataSetDTO().setProgram("");
		String programValue = cqlStandaloneWorkspaceView.getValueSetView().getProgramListBox().getSelectedValue();
		if(!programValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setProgram(programValue);
		}

		CodeListSearchDTO codeListSearchDTO = new CodeListSearchDTO();
		codeListSearchDTO.setName(cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput().getText());
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		matValueSetTransferObject.setMatValueSet(currentMatValueSet);
		matValueSetTransferObject.setCqlLibraryId(libraryID);
		matValueSetTransferObject.setUserDefinedText(cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput().getText());
		return matValueSetTransferObject;
	}

	/**
	 * Modify QDM with out value set.
	 */
	private void modifyUserDefinedValueSet() {
		modifyValueSetDTO.setVersion("");
		if ((cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput().getText().trim().length() > 0)) {
			String originalName = cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput().getText();
			String suffix = cqlStandaloneWorkspaceView.getValueSetView().getSuffixInput().getValue();
			String usrDefDisplayName = (!originalName.isEmpty() ? originalName : "") + (!suffix.isEmpty() ? " (" + suffix + ")" : ""); 

			modifyValueSetList(modifyValueSetDTO);
			if (!cqlStandaloneWorkspaceView.getValueSetView().checkNameInValueSetList(usrDefDisplayName,appliedValueSetTableList)) {

				CQLValueSetTransferObject object = new CQLValueSetTransferObject();
				object.setUserDefinedText(cqlStandaloneWorkspaceView.getValueSetView().getUserDefinedInput().getText());
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


	/**
	 * Build View for General info when General Info AnchorList item is clicked.
	 */
	private void generalInfoEvent() {
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlStandaloneWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlStandaloneWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlStandaloneWorkspaceView.hideInformationDropDown();
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			cqlStandaloneWorkspaceView.buildGeneralInformation();
			boolean isValidQDMVersion = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().checkForIncludedLibrariesQDMVersion(true); //true because its a standalone library
			if(!isValidQDMVersion){
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_QDM_VERSION_IN_INCLUDES());
			} else {
				messagePanel.getErrorMessageAlert().clearAlert();
			}
			cqlStandaloneWorkspaceView.getCqlGeneralInformationView().getLibraryNameValue().setText(cqlLibraryName);
			cqlStandaloneWorkspaceView.getCqlGeneralInformationView().getComments().setText(cqlLibraryComment);
		}
		cqlStandaloneWorkspaceView.setGeneralInfoHeading();
		cqlStandaloneWorkspaceView.getCqlGeneralInformationView().getComments().setCursorPos(0);
	}

	/**
	 * Applied QDM event.
	 */
	private void appliedQDMEvent() {
		// server
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlStandaloneWorkspaceView.hideInformationDropDown();
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(true);
			cqlStandaloneWorkspaceView.getValueSetView().getPasteButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
			currentSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			buildAppliedQDMTable();
		}		
		//On load of Value Sets page, set the Programs from VSAC 
		loadProgramsAndReleases();		
		cqlStandaloneWorkspaceView.getValueSetView().setHeading("CQL Library Workspace > Value Sets", "subQDMAPPliedListContainerPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Builds the applied QDM table.
	 */
	private void buildAppliedQDMTable() {
		cqlStandaloneWorkspaceView.buildAppliedQDM();
		boolean isEditable = MatContext.get().getLibraryLockService().checkForEditPermission();

		// initialize the valuesets to be used, getUsedArtifacts() will update with the proper value
		for(CQLQualityDataSetDTO valuset : cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQdmTableList()) {
			valuset.setUsed(true);
		}

		cqlStandaloneWorkspaceView.getValueSetView().buildAppliedValueSetCellTable(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQdmTableList(), isEditable);
		cqlStandaloneWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
		cqlStandaloneWorkspaceView.getValueSetView().setWidgetsReadOnly(isEditable);
		getUsedArtifacts();
	}

	/**
	 * codes event.
	 */
	private void codesEvent() {
		// server
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_CODES;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_CODES;
			cqlStandaloneWorkspaceView.buildCodes();
			cqlStandaloneWorkspaceView.getCodesView().buildCodesCellTable(
					appliedCodeTableList,
					MatContext.get().getLibraryLockService().checkForEditPermission());
			cqlStandaloneWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
			cqlStandaloneWorkspaceView.getCodesView()
			.setWidgetsReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
			getUsedCodes();
			boolean isEnabled = MatContext.get().getLibraryLockService().checkForEditPermission();
			cqlStandaloneWorkspaceView.getCodesView().getPasteButton().setEnabled(isEnabled);
		}
		cqlStandaloneWorkspaceView.getCodesView().setHeading("CQL Library Workspace > Codes", "codesContainerPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Method to find used codes for delete button state.
	 */
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
						// if there are errors, set the codes to not used.
						if(!result.getCqlErrors().isEmpty()) {
							for(CQLCode cqlCode : appliedCodeTableList){
								cqlCode.setUsed(false);
							}
						}                           	
						// otherwise, check if the valueset is in the used valusets list
						else {
							for(CQLCode cqlCode : appliedCodeTableList){
								//
								if (result.getUsedCQLcodes().contains(cqlCode.getDisplayName())) {
									cqlCode.setUsed(true);
								} else{
									cqlCode.setUsed(false);
								}
							}
						}

						if(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getAppliedCodeTableList().size() > 0) {
							cqlStandaloneWorkspaceView.getCodesView().getCelltable().redraw();
							cqlStandaloneWorkspaceView.getCodesView().getListDataProvider().refresh();
						}
					}

				});
	}

	/**
	 * Build View for Parameter when Parameter AnchorList item is clicked.
	 */
	private void parameterEvent() {
		unsetActiveMenuItem(currentSection);
		cqlStandaloneWorkspaceView.hideInformationDropDown();
		cqlStandaloneWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlStandaloneWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
		cqlStandaloneWorkspaceView.buildParameterLibraryView();

		cqlStandaloneWorkspaceView.getCQLParametersView()
		.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());

		cqlStandaloneWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
		cqlStandaloneWorkspaceView.getParameterButtonBar().getDeleteButton().setEnabled(false);
		cqlStandaloneWorkspaceView.getParameterButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = cqlStandaloneWorkspaceView.getCQLParametersView().getParameterAceEditor();
		cqlStandaloneWorkspaceView.getCQLParametersView().setHeading("CQL Library Workspace > Parameter", "mainParamViewVerticalPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Build View for Includes when Includes AnchorList item is clicked.
	 */
	private void includesEvent() {
		unsetActiveMenuItem(currentSection);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlStandaloneWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlStandaloneWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
		cqlStandaloneWorkspaceView.getMainFlowPanel().clear();
		cqlStandaloneWorkspaceView.hideInformationDropDown();
		cqlStandaloneWorkspaceView.getIncludeView().setIncludedList(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
				.getIncludedList(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
		cqlStandaloneWorkspaceView.buildIncludesView();
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(new ArrayList<CQLLibraryDataSetObject>());
		cqlStandaloneWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
				MatContext.get().getLibraryLockService().checkForEditPermission(), true);

		cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().setText("");
		cqlStandaloneWorkspaceView.getIncludeView().getSearchTextBox().setText("");
		cqlStandaloneWorkspaceView.getCqlIncludeLibraryView()
		.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		cqlStandaloneWorkspaceView.getIncludeView().setHeading("CQL Library Workspace > Includes", "IncludeSectionContainerPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Build View for Definition when Definition AnchorList item is clicked.
	 */
	private void definitionEvent() {
		unsetActiveMenuItem(currentSection);
		cqlStandaloneWorkspaceView.hideInformationDropDown();
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlStandaloneWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlStandaloneWorkspaceView.getCodesView().getCellTableMainPanel().clear();

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
		cqlStandaloneWorkspaceView.buildDefinitionLibraryView();

		cqlStandaloneWorkspaceView.getCQLDefinitionsView()
		.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());

		cqlStandaloneWorkspaceView.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
		cqlStandaloneWorkspaceView.getDefineButtonBar().getDeleteButton().setEnabled(false);
		cqlStandaloneWorkspaceView.getDefineButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = cqlStandaloneWorkspaceView.getCQLDefinitionsView().getDefineAceEditor();
		cqlStandaloneWorkspaceView.getCQLDefinitionsView().setHeading("CQL Library Workspace > Definition", "mainDefViewVerticalPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Build View for Function when Funtion AnchorList item is clicked.
	 */
	private void functionEvent() {
		cqlStandaloneWorkspaceView.hideInformationDropDown();
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlStandaloneWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlStandaloneWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		unsetActiveMenuItem(currentSection);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
		cqlStandaloneWorkspaceView.buildFunctionLibraryView();
		cqlStandaloneWorkspaceView.getCQLFunctionsView()
		.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());

		cqlStandaloneWorkspaceView.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
		cqlStandaloneWorkspaceView.getFunctionButtonBar().getDeleteButton().setEnabled(false);
		cqlStandaloneWorkspaceView.getFunctionButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = cqlStandaloneWorkspaceView.getCQLFunctionsView().getFunctionBodyAceEditor();
		cqlStandaloneWorkspaceView.getCQLFunctionsView().setHeading("CQL Library Workspace > Function", "mainFuncViewVerticalPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Build View for View Cql when View Cql AnchorList item is clicked.
	 */
	private void viewCqlEvent() {
		cqlStandaloneWorkspaceView.hideInformationDropDown();
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlStandaloneWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlStandaloneWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		if (getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewCQL().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			cqlStandaloneWorkspaceView.buildCQLFileView(MatContext.get().getLibraryLockService().checkForEditPermission());
			buildCQLView();
		}
		cqlStandaloneWorkspaceView.getViewCQLView().setHeading("CQL Library Workspace > View CQL", "cqlViewCQL_Id");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Method to build View for Anchor List item View CQL.
	 */
	private void buildCQLView() {
		cqlStandaloneWorkspaceView.getCqlAceEditor().setText("");
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().getCQLLibraryFileData(MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<SaveUpdateCQLResult>() {
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if (result.isSuccess()) {
					if ((result.getCqlString() != null) && !result.getCqlString().isEmpty()) {

						cqlStandaloneWorkspaceView.getCqlAceEditor().clearAnnotations();
						cqlStandaloneWorkspaceView.getCqlAceEditor().removeAllMarkers();
						cqlStandaloneWorkspaceView.getCqlAceEditor().redisplay();
						messagePanel.getSuccessMessageAlert().clearAlert();
						messagePanel.getWarningMessageAlert().clearAlert();
						messagePanel.getWarningConfirmationMessageAlert().clearAlert();

						if (!result.getCqlErrors().isEmpty()) {
							messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVIEW_CQL_ERROR_MESSAGE());
							for (CQLErrors error : result.getCqlErrors()) {
								String errorMessage = new String();
								errorMessage = errorMessage.concat("Error in line : " + error.getErrorInLine()
								+ " at Offset :" + error.getErrorAtOffeset());
								int line = error.getErrorInLine();
								int column = error.getErrorAtOffeset();
								cqlStandaloneWorkspaceView.getCqlAceEditor().addAnnotation(line - 1, column,
										error.getErrorMessage(), AceAnnotationType.ERROR);
							}
							cqlStandaloneWorkspaceView.getCqlAceEditor().setText(result.getCqlString());
							cqlStandaloneWorkspaceView.getCqlAceEditor().setAnnotations();
							cqlStandaloneWorkspaceView.getCqlAceEditor().redisplay();
						} else if (!result.isDatatypeUsedCorrectly()) {
							messagePanel.getSuccessMessageAlert().clearAlert();
							messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVIEW_CQL_ERROR_MESSAGE_BAD_VALUESET_DATATYPE());
							cqlStandaloneWorkspaceView.getCqlAceEditor().setText(result.getCqlString());
						} else {
							messagePanel.getSuccessMessageAlert().setVisible(true);
							messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVIEW_CQL_NO_ERRORS_MESSAGE());
							cqlStandaloneWorkspaceView.getCqlAceEditor().setText(result.getCqlString());
						}
					}

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

	/**
	 * Unset active menu item.
	 *
	 * @param menuClickedBefore the menu clicked before
	 */
	private void unsetActiveMenuItem(String menuClickedBefore) {
		if (!getIsPageDirty()) {
			cqlStandaloneWorkspaceView.resetMessageDisplay();
			if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_GENERAL_MENU)) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(false);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().setSelectedIndex(-1);
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParamCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
					.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().setSelectedIndex(-1);
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
					.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(false);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().setSelectedIndex(-1);
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
					.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEW_MENU)) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewCQL().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_APPLIED_QDM)) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(false);
			}else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_CODES)) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_INCLUDES_MENU)) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(false);
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().setSelectedIndex(-1);
				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
					.setClassName("panel-collapse collapse");
				}
			}
		}
	}

	/**
	 * This method is called at beforeDisplay and get searchButton click on
	 * Include section and reterives CQL Versioned libraries eligible to be
	 * included into any parent cql library.
	 *
	 * @param searchText
	 *            the search text
	 * @return the all include library list
	 */
	private void getAllIncludeLibraryList(final String searchText) {
		messagePanel.getErrorMessageAlert().clearAlert();
		messagePanel.getSuccessMessageAlert().clearAlert();
		messagePanel.getWarningMessageAlert().clearAlert();
		showSearchingBusy(true);

		MatContext.get().getCQLLibraryService().searchForIncludes(setId, searchText, false,
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
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
					.setIncludeLibraryList(result.getCqlLibraryDataSetObjects());
					cqlStandaloneWorkspaceView.buildIncludesView();
					cqlStandaloneWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
							MatContext.get().getLibraryLockService().checkForEditPermission(), false);

				} else {
					cqlStandaloneWorkspaceView.buildIncludesView();
					cqlStandaloneWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
							MatContext.get().getLibraryLockService().checkForEditPermission(), false);
					if (!cqlStandaloneWorkspaceView.getIncludeView().getSearchTextBox().getText().isEmpty())
						messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getNoIncludes());
				}

				if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox()
						.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else {
					messagePanel.getWarningMessageAlert().clearAlert();
				}
				// 508 changes for Library Alias.
				cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
			}
		});

	}

	/**
	 * Show searching busy.
	 *
	 * @param busy the busy
	 */
	private void showSearchingBusy(final boolean busy) {
		showSearchBusyOnDoubleClick(busy);
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			switch(currentSection.toLowerCase()) {
			case(CQLWorkSpaceConstants.CQL_GENERAL_MENU): 
				cqlStandaloneWorkspaceView.getCqlGeneralInformationView().setWidgetReadOnlyForCQLLibrary(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
				cqlStandaloneWorkspaceView.getIncludeView().setReadOnly(!busy);				
			break;
			case(CQLWorkSpaceConstants.CQL_APPLIED_QDM): 
				cqlStandaloneWorkspaceView.getValueSetView().setReadOnly(!busy);				
			break;
			case(CQLWorkSpaceConstants.CQL_CODES): 
				cqlStandaloneWorkspaceView.getCodesView().setReadOnly(!busy);				
			break;
			case(CQLWorkSpaceConstants.CQL_PARAMETER_MENU): 
				cqlStandaloneWorkspaceView.getCQLParametersView().setReadOnly(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_DEFINE_MENU): 
				cqlStandaloneWorkspaceView.getCQLDefinitionsView().setReadOnly(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_FUNCTION_MENU): 
				cqlStandaloneWorkspaceView.getCQLFunctionsView().setReadOnly(!busy);
			break;															  
			}

		}
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsLoading(busy);
	}

	private void showSearchBusyOnDoubleClick(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getGeneralInformation().setEnabled(!busy);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCodesLibrary().setEnabled(!busy);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQDM().setEnabled(!busy);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterLibrary().setEnabled(!busy);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionLibrary().setEnabled(!busy);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionLibrary().setEnabled(!busy);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewCQL().setEnabled(!busy);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesLibrary().setEnabled(!busy);
	}

	/**
	 * This method Clears alias view on Erase Button click when isPageDirty is
	 * not set.
	 */
	private void clearAlias() {
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
		setIsPageDirty(false);
		if ((cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea() != null)) {
			cqlStandaloneWorkspaceView.getIncludeView().getAliasNameTxtArea().setText("");
		}
		if ((cqlStandaloneWorkspaceView.getIncludeView().getViewCQLEditor().getText() != null)) {
			cqlStandaloneWorkspaceView.getIncludeView().getViewCQLEditor().setText("");
		}
		// Below lines are to clear Library search text box.
		if ((cqlStandaloneWorkspaceView.getIncludeView().getSearchTextBox().getText() != null)) {
			cqlStandaloneWorkspaceView.getIncludeView().getSearchTextBox().setText("");
		}
		cqlStandaloneWorkspaceView.getIncludeView().getSelectedObjectList().clear();
		cqlStandaloneWorkspaceView.getIncludeView().setSelectedObject(null);
		cqlStandaloneWorkspaceView.getIncludeView().setIncludedList(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView()
				.getIncludedList(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
		unCheckAvailableLibraryCheckBox();

		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
		if (cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
			cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().setItemSelected(
					cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(), false);
		}

	}

	/**
	 * Un check available library check box.
	 */
	private void unCheckAvailableLibraryCheckBox() {
		List<CQLLibraryDataSetObject> availableLibraries = new ArrayList<CQLLibraryDataSetObject>();
		availableLibraries = cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryList();
		for (int i = 0; i < availableLibraries.size(); i++) {
			availableLibraries.get(i).setSelected(false);
		}
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryList());
		cqlStandaloneWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
				MatContext.get().getLibraryLockService().checkForEditPermission(), false);
	}

	/**
	 * Validate CQL artifact.
	 *
	 * @param result
	 *            the result
	 * @param currentSect
	 *            the current sect
	 * @return true, if successful
	 */
	private boolean validateCQLArtifact(SaveUpdateCQLResult result, String currentSect) {
		boolean isInvalid = false;
		if (!result.getCqlErrors().isEmpty()) {
			for (CQLErrors error : result.getCqlErrors()) {
				int startLine = error.getStartErrorInLine();
				int startColumn = error.getStartErrorAtOffset();
				curAceEditor.addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.ERROR);
				if (!isInvalid) {
					isInvalid = true;
				}
			}
		}

		return isInvalid;
	}
	/**
	 * Method to trigger double Click on List Boxes based on section when user
	 * clicks Yes on Warning message (Dirty Check).
	 */
	private void clickEventOnListboxes() {

		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
		switch (currentSection) {
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().fireEvent(new DoubleClickEvent() {
			});
		break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().fireEvent(new DoubleClickEvent() {
			});
		break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().fireEvent(new DoubleClickEvent() {
			});
		break;
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().fireEvent(new DoubleClickEvent() {
			});
		break;
		default:
			break;
		}

	}

	/**
	 * Method to Unset current Left Nav section and set next selected section
	 * when user clicks yes on warning message (Dirty Check).
	 */
	private void changeSectionSelection() {
		cqlStandaloneWorkspaceView.hideInformationDropDown();
		// Unset current selected section.
		switch (currentSection) {
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			unsetActiveMenuItem(currentSection);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_APPLIED_QDM):
			unsetActiveMenuItem(currentSection);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_CODES):
			unsetActiveMenuItem(currentSection);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			unsetActiveMenuItem(currentSection);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			unsetActiveMenuItem(currentSection);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			unsetActiveMenuItem(currentSection);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
			unsetActiveMenuItem(currentSection);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_VIEW_MENU):
			unsetActiveMenuItem(currentSection);
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getViewCQL().setActive(false);
		break;
		default:
			break;
		}
		// Set Next Selected Section.
		switch (nextSection) {
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			currentSection = nextSection;
		includesEvent();
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
		.setClassName("panel-collapse collapse in");
		break;
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			currentSection = nextSection;
		functionEvent();
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
		.setClassName("panel-collapse collapse in");
		break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			currentSection = nextSection;
		parameterEvent();
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
		.setClassName("panel-collapse collapse in");
		break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			currentSection = nextSection;
		definitionEvent();
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
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

	/**
	 * This method clears the view if isPageDirty flag is not set.
	 */
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

	/**
	 * Get Attributed for Selected Function Argument - QDM Data Type from db.
	 *
	 * @param functionArg
	 *            - CQLFunctionArgument.
	 * @return the attributes for data type
	 */
	private void getAttributesForDataType(final CQLFunctionArgument functionArg) {
		attributeService.getAllAttributesByDataType(functionArg.getQdmDataType(),
				new AsyncCallback<List<QDSAttributes>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(List<QDSAttributes> result) {
				cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().setAvailableQDSAttributeList(result);
				AddFunctionArgumentDialogBox.showArgumentDialogBox(functionArg, true,cqlStandaloneWorkspaceView.getCQLFunctionsView(), messagePanel,MatContext.get().getLibraryLockService().checkForEditPermission());
			}

		});
	}

	/**
	 * Gets the definition list.
	 *
	 * @param definitionList
	 *            the definition list
	 * @return the definition list
	 */
	private List<CQLIdentifierObject> getDefinitionList(List<CQLDefinition> definitionList) {

		List<CQLIdentifierObject> defineList = new ArrayList<CQLIdentifierObject>();

		for (int i = 0; i < definitionList.size(); i++) {
			CQLIdentifierObject definition = new CQLIdentifierObject(null, definitionList.get(i).getName(),definitionList.get(i).getId());
			defineList.add(definition);
		}
		return defineList;
	}

	/**
	 * Gets the paramater list.
	 *
	 * @param parameterList
	 *            the parameter list
	 * @return the paramater list
	 */
	private List<CQLIdentifierObject> getParamaterList(List<CQLParameter> parameterList) {

		List<CQLIdentifierObject> paramList = new ArrayList<CQLIdentifierObject>();

		for (int i = 0; i < parameterList.size(); i++) {
			CQLIdentifierObject parameter = new CQLIdentifierObject(null, parameterList.get(i).getName(),parameterList.get(i).getId());
			paramList.add(parameter);
		}
		return paramList;
	}

	/**
	 * Gets the function list.
	 *
	 * @param functionList
	 *            the function list
	 * @return the function list
	 */
	private List<CQLIdentifierObject> getFunctionList(List<CQLFunctions> functionList) {

		List<CQLIdentifierObject> funcList = new ArrayList<CQLIdentifierObject>();

		for (int i = 0; i < functionList.size(); i++) {
			CQLIdentifierObject function = new CQLIdentifierObject(null, functionList.get(i).getName(),functionList.get(i).getId());
			funcList.add(function);
		}
		return funcList;
	}

	/**
	 * Gets the includes list.
	 *
	 * @param includesList
	 *            the includes list
	 * @return the includes list
	 */
	private List<String> getIncludesList(List<CQLIncludeLibrary> includesList) {

		List<String> incLibList = new ArrayList<String>();

		for (int i = 0; i < includesList.size(); i++) {
			incLibList.add(includesList.get(i).getAliasName());
		}
		return incLibList;
	}

	/**
	 * Display empty.
	 */
	private void displayEmpty() {
		panel.clear();
		panel.add(emptyWidget);
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		panel.setStyleName("contentPanel");
		return panel;
	}

	/**
	 * returns the searchDisplay.
	 * 
	 * @return ViewDisplay.
	 */
	public static CQLStandaloneWorkSpaceView getSearchDisplay() {
		return cqlStandaloneWorkspaceView;
	}

	/**
	 * @param result
	 */
	private void setAppliedValueSetListInTable(List<CQLQualityDataSetDTO> valueSetList) {
		appliedValueSetTableList.clear();
		List<CQLQualityDataSetDTO> allValuesets = new ArrayList<CQLQualityDataSetDTO>();
		for (CQLQualityDataSetDTO dto : valueSetList) {
			if (dto.isSuppDataElement())
				continue;
			allValuesets.add(dto);
		}
		MatContext.get().setValuesets(allValuesets);
		for (CQLQualityDataSetDTO valueset : allValuesets) {
			// filtering out codes from valuesets list
			if((valueset.getOid().equals("419099009") || valueset.getOid().equals("21112-8") 
					|| (valueset.getType() !=null) && valueset.getType().equalsIgnoreCase("code"))){
				continue;
			}
			appliedValueSetTableList.add(valueset);
		}
		cqlStandaloneWorkspaceView.getValueSetView().buildAppliedValueSetCellTable(appliedValueSetTableList,
				MatContext.get().getLibraryLockService().checkForEditPermission());
		cqlStandaloneWorkspaceView.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
	}



	private void loadProgramsAndReleases() {
		CQLAppliedValueSetUtility.loadProgramsAndReleases(cqlStandaloneWorkspaceView.getValueSetView().getProgramListBox(), cqlStandaloneWorkspaceView.getValueSetView().getReleaseListBox());
	}
}