package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.shared.event.HideEvent;
import org.gwtbootstrap3.client.shared.event.HideHandler;
import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.CqlComposerPresenter;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.buttons.DefinitionFunctionButtonToolBar;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.cqlworkspace.CQLCodesView.Delegator;
import mat.client.clause.cqlworkspace.CQLFunctionsView.Observer;
import mat.client.clause.event.QDSElementCreatedEvent;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.measure.service.CQLLibraryServiceAsync;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.ValueSetNameInputValidator;
import mat.client.umls.service.VSACAPIServiceAsync;
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
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.StringUtility;

public class CQLStandaloneWorkSpacePresenter implements MatPresenter {
	
	private static final String CODES_SELECTED_SUCCESSFULLY = "All codes successfully selected.";
	
	private static final String VALUE_SETS_SELECTED_SUCCESSFULLY = "All value sets successfully selected.";

	private SimplePanel panel = new SimplePanel();

	private static ViewDisplay searchDisplay;

	private SimplePanel emptyWidget = new SimplePanel();

	private boolean isCQLWorkSpaceLoaded = false;

	private String currentSection = "general";

	private String nextSection = "general";

	private List<CQLQualityDataSetDTO> appliedValueSetTableList = new ArrayList<CQLQualityDataSetDTO>();

	CQLModelValidator validator = new CQLModelValidator();

	private String cqlLibraryName;
	
	private String cqlLibraryComment;

	private String setId = null;
	
	private String currentIncludeLibrarySetId = null;
	
	private String currentIncludeLibraryId = null; 

	private boolean isModified = false;
	
	private boolean isCodeModified = false;
	
	private boolean isUserDefined = false;

	private CQLQualityDataSetDTO modifyValueSetDTO;
	
	private CQLCode modifyCQLCode;

	private QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);

	private AceEditor curAceEditor;

	private final VSACAPIServiceAsync vsacapiService = MatContext.get().getVsacapiServiceAsync();

	private final CQLLibraryServiceAsync cqlService = MatContext.get().getCQLLibraryService();

	private MatValueSet currentMatValueSet= null;
	
	private List<CQLCode> appliedCodeTableList = new ArrayList<CQLCode>();
	
	/**
	 * Flag for if parameters, definitions, or functions should be formatted.
	 * For now this flag will always be set to true. 
	 */
	private boolean isFormatable = true;
	
	private boolean areProgramAndReleaseListBoxesEnabled = true; 
	private boolean isRetrieveButtonEnabled = true; 
	private boolean isApplyButtonEnabled = false; 
	
	private boolean previousIsProgramAndReleaseListBoxesEnabled = true; 
	private boolean previousIsRetrieveButtonEnabled = true; 
	private boolean previousIsApplyButtonEnabled = false; 

	public static interface ViewDisplay {

		VerticalPanel getMainPanel();

		Widget asWidget();

		HorizontalPanel getMainHPanel();

		FlowPanel getMainFlowPanel();

		void buildView();

		String getClickedMenu();

		void setClickedMenu(String clickedMenu);

		String getNextClickedMenu();

		void setNextClickedMenu(String nextClickedMenu);

		CQLLeftNavBarPanelView getCqlLeftNavBarPanelView();

		void resetMessageDisplay();

		void hideAceEditorAutoCompletePopUp();

		CQLParametersView getCQLParametersView();

		CQLDefinitionsView getCQLDefinitionsView();

		CQLFunctionsView getCQLFunctionsView();

		CQLIncludeLibraryView getCqlIncludeLibraryView();

		void buildCQLFileView(boolean isEditable);

		AceEditor getCqlAceEditor();

		void buildGeneralInformation();

		CQLGeneralInformationView getCqlGeneralInformationView();

		CQLIncludeLibraryView getIncludeView();

		TextBox getAliasNameTxtArea();

		AceEditor getViewCQLEditor();

		TextBox getOwnerNameTextBox();

		void buildIncludesView();

		void resetAll();

		void buildParameterLibraryView();

		void buildDefinitionLibraryView();

		void buildFunctionLibraryView();

		void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList);

		DefinitionFunctionButtonToolBar getParameterButtonBar();

		DefinitionFunctionButtonToolBar getDefineButtonBar();

		DefinitionFunctionButtonToolBar getFunctionButtonBar();

		TextBox getDefineNameTxtArea();

		AceEditor getDefineAceEditor();

		InlineRadio getContextDefinePATRadioBtn();

		InlineRadio getContextDefinePOPRadioBtn();

		TextBox getFuncNameTxtArea();

		AceEditor getFunctionBodyAceEditor();

		InlineRadio getContextFuncPATRadioBtn();

		InlineRadio getContextFuncPOPRadioBtn();

		List<CQLFunctionArgument> getFunctionArgumentList();

		TextBox getParameterNameTxtArea();

		AceEditor getParameterAceEditor();

		Map<String, CQLFunctionArgument> getFunctionArgNameMap();

		void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList, boolean isEditable);

		CQLAppliedValueSetView getValueSetView();

		CQLCodesView getCodesView();

		void buildAppliedQDM();

		void buildCodes();

		HorizontalPanel getLockedButtonVPanel();

		void hideInformationDropDown();

		CQLView getViewCQLView();

		void setGeneralInfoHeading();
		
		HelpBlock getHelpBlock();
	}

	public CQLStandaloneWorkSpacePresenter(final ViewDisplay srchDisplay) {
		searchDisplay = srchDisplay;
		emptyWidget.add(new Label("No CQL Library Selected"));
		addEventHandlers();
	}

	private void addViewCQLEventHandlers() {
		searchDisplay.getViewCQLView().getExportErrorFile().addClickHandler(new ClickHandler() {
			
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
		searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBoxYesButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
							deleteDefinition();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null
								&& searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId() == null) {
							deleteFunction();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						}else if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId() != null) {
							deleteFunctionArgument();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (searchDisplay.getCqlLeftNavBarPanelView()
								.getCurrentSelectedParamerterObjId() != null) {
							deleteParameter();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (searchDisplay.getCqlLeftNavBarPanelView()
								.getCurrentSelectedIncLibraryObjId() != null) {
							deleteInclude();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						}  else if(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedValueSetObjId() != null){
							checkAndDeleteValueSet();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedCodesObjId() != null){
							deleteCode();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						}
					}
				});

		searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBoxNoButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						searchDisplay.resetMessageDisplay();
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
					}
				});

		ClickHandler cHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCQLDefinitionsView().getDefineAceEditor().detach();
				searchDisplay.getCQLParametersView().getParameterAceEditor().detach();
				searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().detach();
			}
		};
		searchDisplay.getMainPanel().addDomHandler(cHandler, ClickEvent.getType());

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
		searchDisplay.getValueSetView().getOIDInput().setEnabled(true);

		searchDisplay.getValueSetView().getOIDInput().setValue(oid);
		searchDisplay.getValueSetView().getOIDInput().setTitle(oid);

		searchDisplay.getValueSetView().getRetrieveFromVSACButton().setEnabled(!isUserDefined);

		searchDisplay.getValueSetView().getUserDefinedInput().setEnabled(isUserDefined);
		searchDisplay.getValueSetView().getUserDefinedInput().setValue(result.getOriginalCodeListName());
		searchDisplay.getValueSetView().getUserDefinedInput().setTitle(result.getOriginalCodeListName());

		searchDisplay.getValueSetView().getSuffixInput().setEnabled(true);
		searchDisplay.getValueSetView().getSuffixInput().setValue(result.getSuffix());
		searchDisplay.getValueSetView().getSuffixInput().setTitle(result.getSuffix());
		
		setReleaseAndProgramFieldsOnEdit(result);
		searchDisplay.getValueSetView().getSaveButton().setEnabled(isUserDefined);
		alert508StateChanges();
	}
	
	private void setReleaseAndProgramFieldsOnEdit(CQLQualityDataSetDTO result) {
		previousIsProgramAndReleaseListBoxesEnabled = areProgramAndReleaseListBoxesEnabled;
		
		loadProgramsAndReleases();
		
		areProgramAndReleaseListBoxesEnabled = true;
				
		searchDisplay.getValueSetView().setProgramAndReleaseBoxesEnabled(areProgramAndReleaseListBoxesEnabled);
	}
	
	
	private void onModifyCode(CQLCode cqlCode) {
		CQLCodesView codesView = searchDisplay.getCodesView();
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
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_QDM_REMOVE_MSG(result.getCqlQualityDataSetDTO().getName()));
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);
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
		searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(
							Document.get()
									.createDblClickEvent(searchDisplay.getCqlLeftNavBarPanelView()
											.getParameterNameListBox().getSelectedIndex(), 0, 0, 0, 0, false, false,
											false, false),
							searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox());
				}

			}
		});
		
		searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox()
				.addDoubleClickHandler(new DoubleClickHandler() {
					@Override
					public void onDoubleClick(DoubleClickEvent event) {

						if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
							event.stopPropagation();
						} else {
							showSearchBusyOnDoubleClick(true);
							searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
							searchDisplay.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
							searchDisplay.getCQLParametersView().getParameterButtonBar().getInfoButtonGroup()
									.getElement().setAttribute("class", "btn-group");
							resetAceEditor(searchDisplay.getCQLParametersView().getViewCQLAceEditor());
							resetViewCQLCollapsiblePanel(
									searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());

							searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
							searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
							if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
								showSearchBusyOnDoubleClick(false);
								searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
							} else {
								int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox()
										.getSelectedIndex();
								if (selectedIndex != -1) {
									final String selectedParamID = searchDisplay.getCqlLeftNavBarPanelView()
											.getParameterNameListBox().getValue(selectedIndex);
									searchDisplay.getCqlLeftNavBarPanelView()
											.setCurrentSelectedParamerterObjId(selectedParamID);
									if (searchDisplay.getCqlLeftNavBarPanelView().getParameterMap()
											.get(selectedParamID) != null) {

										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setTitle("Delete");
										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setEnabled(false);
										searchDisplay.getCQLParametersView().setWidgetReadOnly(false);
										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setEnabled(false);
										searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
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
														searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).getName());
														searchDisplay.getCQLParametersView().getParameterAceEditor().setText(searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).getLogic());
														searchDisplay.getCQLParametersView().getParameterCommentTextArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).getCommentString());

														boolean isReadOnly = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).isReadOnly();
														CQLParameter currentParameter = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID);
														if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
															searchDisplay.getCQLParametersView().setWidgetReadOnly(!isReadOnly);
															if (!currentParameter.isReadOnly()) {
																// if there are cql errors or the parameter is not in use, enable the delete button
																if (!result.getCqlErrors().isEmpty() || !result.getUsedCQLParameters().contains(currentParameter.getName())) {
																	searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
																} else{
																	searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
																}
															}
															
														}
														Map<String, List<CQLErrors>> expressionCQLErrorMap = result.getCqlErrorsPerExpression();
														if (expressionCQLErrorMap != null) {
															List<CQLErrors> errorList = expressionCQLErrorMap.get(currentParameter.getName());
															searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
															searchDisplay.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
															for (CQLErrors error : errorList) {
																int startLine = error.getStartErrorInLine();
																int startColumn = error.getStartErrorAtOffset();
																searchDisplay.getCQLParametersView().getParameterAceEditor().addAnnotation(startLine, startColumn,error.getErrorMessage(),AceAnnotationType.ERROR);
															}
															searchDisplay.getCQLParametersView().getParameterAceEditor().setAnnotations();
														}

													}

												});

									} else {
										showSearchBusyOnDoubleClick(false);
									}
								} else {
									showSearchBusyOnDoubleClick(false);
								}

								searchDisplay.resetMessageDisplay();

							}
						}
						// 508 change to parameter section
						searchDisplay.getCqlLeftNavBarPanelView()
								.setFocus(searchDisplay.getCQLParametersView().getMainParamViewVerticalPanel());
					}
				});
		
		
		searchDisplay.getCQLParametersView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {
			
			@Override
			public void onShow(ShowEvent showEvent) {
				searchDisplay.resetMessageDisplay();
				showCompleteCQL(searchDisplay.getCQLParametersView().getViewCQLAceEditor());
			}
		});
		
		searchDisplay.getCQLParametersView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {
			
			@Override
			public void onHide(HideEvent hideEvent) {
				searchDisplay.getCQLParametersView().getParameterButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetAceEditor(searchDisplay.getCQLParametersView().getViewCQLAceEditor());
				
			}
		});
		
		
		// Parameter Save Icon Functionality
		searchDisplay.getCQLParametersView().getParameterButtonBar().getSaveButton()
		.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					addAndModifyParameters();
					//508 change to parameter section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLParametersView().getParameterNameTxtArea());
				}
			}

		});
		// Parameter Erase Icon Functionality
		searchDisplay.getCQLParametersView().getParameterButtonBar().getEraseButton()
		.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
				eraseParameter(); 
				//508 change to parameter section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLParametersView().getParameterAceEditor());
			}
		});
		// Parameter Delete Icon Functionality
		searchDisplay.getParameterButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_PARAMETER(searchDisplay.getCQLParametersView().getParameterNameTxtArea().getValue()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();

			}

		});

		searchDisplay.getCQLParametersView().getParameterNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
		
		searchDisplay.getCQLParametersView().getParameterCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});


		// Parameter Add New Functionality
		searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					addNewParameter();
				}
				//508 change to parameter section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLParametersView().getParameterNameTxtArea());
			}
		});
		
		searchDisplay.getCQLParametersView().getParameterCommentTextArea().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.NONE);
				String comment = searchDisplay.getCQLParametersView().getParameterCommentTextArea().getText();
				if(validator.validateForCommentTextArea(comment)){
					searchDisplay.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.ERROR);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
							MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
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

		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getCQLParametersView().getParameterAceEditor().getText() != null)) {
			searchDisplay.getCQLParametersView().getParameterAceEditor().setText("");
		}
		if ((searchDisplay.getCQLParametersView().getParameterNameTxtArea() != null)) {
			searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText("");
		}
		
		if ((searchDisplay.getCQLParametersView().getParameterCommentTextArea() != null)) {
			searchDisplay.getCQLParametersView().getParameterCommentTextArea().setText("");
		}

		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			searchDisplay.getCQLParametersView()
					.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
				searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(), false);
		}

		searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
	}
	
	/**
	 * Adds the define event handlers.
	 */
	private void addDefineEventHandlers() {
		
		searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(
							Document.get()
									.createDblClickEvent(searchDisplay.getCqlLeftNavBarPanelView()
											.getDefineNameListBox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
											false),
							searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox());
				}

			}
		});
		
		searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox()
		.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
					showSearchBusyOnDoubleClick(true);
					searchDisplay.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
					searchDisplay.getCQLDefinitionsView().getDefineAceEditor().removeAllMarkers();
					
					resetAceEditor(searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor());
					resetViewCQLCollapsiblePanel(searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse());
					searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
					
					searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
					if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
						showSearchBusyOnDoubleClick(false);
						searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					} else {
						int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox()
								.getSelectedIndex();
						if (selectedIndex != -1) {
							final String selectedDefinitionID = searchDisplay.getCqlLeftNavBarPanelView()
									.getDefineNameListBox().getValue(selectedIndex);
							searchDisplay.getCqlLeftNavBarPanelView()
							.setCurrentSelectedDefinitionObjId(selectedDefinitionID);
							
							if (searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
									.get(selectedDefinitionID) != null) {
								searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton()
								.setTitle("Delete");
								
								searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
								searchDisplay.getCQLDefinitionsView().setWidgetReadOnly(false);
								searchDisplay.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
								// load most recent used cql artifacts
								MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(
										MatContext.get().getCurrentCQLLibraryId(),
										new AsyncCallback<GetUsedCQLArtifactsResult>() {

											@Override
											public void onFailure(Throwable caught) {
												showSearchBusyOnDoubleClick(false);
												searchDisplay.getCQLDefinitionsView().setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
												Window.alert(MatContext.get().getMessageDelegate()
														.getGenericErrorMessage());
											}

											@Override
											public void onSuccess(GetUsedCQLArtifactsResult result) {
												showSearchBusyOnDoubleClick(false);
												boolean isReadOnly = searchDisplay.getCqlLeftNavBarPanelView()
														.getDefinitionMap().get(selectedDefinitionID).isSupplDataElement();
												
												CQLDefinition currentDefinition = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID);

												searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea()
												.setText(searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
														.get(selectedDefinitionID).getName());
												searchDisplay.getCQLDefinitionsView().getDefineAceEditor()
												.setText(searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
														.get(selectedDefinitionID).getLogic());
												searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea()
												.setText(searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getCommentString());
												
												if (searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
														.get(selectedDefinitionID).getContext().equalsIgnoreCase("patient")) {
													searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn()
													.setValue(true);
													searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn()
													.setValue(false);
												} else {
													searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn()
													.setValue(true);
													searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn()
													.setValue(false);
												}
												
												if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
													searchDisplay.getCQLDefinitionsView().setWidgetReadOnly(!isReadOnly);
													// if there are cql errors or the definition is not in use, enable the delete button
													if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLDefinitions().contains(currentDefinition.getName())) {
														searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(true);
													} else {
														searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
													}
													
													
												}
												
												Map<String,List<CQLErrors>> expressionCQLErrorMap = result.getCqlErrorsPerExpression();
												if(expressionCQLErrorMap != null){
													List<CQLErrors> errorList = expressionCQLErrorMap.get(currentDefinition.getName());
													searchDisplay.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
													searchDisplay.getCQLDefinitionsView().getDefineAceEditor().removeAllMarkers();
													for (CQLErrors error : errorList) {
														int startLine = error.getStartErrorInLine();
														int startColumn = error.getStartErrorAtOffset();
														searchDisplay.getCQLDefinitionsView().getDefineAceEditor().addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.ERROR);
													}
													searchDisplay.getCQLDefinitionsView().getDefineAceEditor().setAnnotations();
												}
												
												if(result.getCqlErrors().isEmpty() && result.getExpressionReturnTypeMap() != null){
													searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getExpressionReturnTypeMap()
															.get(currentDefinition.getName()));
													searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getExpressionReturnTypeMap()
															.get(currentDefinition.getName()) );
										
												} else {
													searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
													searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
													
												}
												
											}

										});
								
							} else {
								showSearchBusyOnDoubleClick(false);
							}
						}else {
							showSearchBusyOnDoubleClick(false);
						}

						searchDisplay.resetMessageDisplay();
					}
				}
				//508 changes for Definitions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLDefinitionsView().getMainDefineViewVerticalPanel());
			}
		});
		
		searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {
			
			@Override
			public void onShow(ShowEvent showEvent) {
				searchDisplay.resetMessageDisplay();
				showCompleteCQL(searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor());
			}
		});
		
		searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {
			
			@Override
			public void onHide(HideEvent hideEvent) {
				searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetAceEditor(searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor());
				
			}
			
		});
		
		
		searchDisplay.getDefineButtonBar().getInsertButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				buildInsertPopUp();
			}
		});
		// Definition Save Icon Functionality
		searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse());
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					addAndModifyDefintions();
					searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
					searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
					//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
					//searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().redisplay();
					//508 changes for Definitions Section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea());
				}
			}

			
		});
		// Definition Erase Icon Functionality
		searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
				searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
				//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
				//searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().redisplay();
				
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse());
				eraseDefinition(); 
				//508 changes to Definition section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLDefinitionsView().getDefineAceEditor());
			}
		});
		
		// Definition Delete Icon Functionality
		searchDisplay.getDefineButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse());
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDeleteConfirmationDefinitionCQLLibraryWorkspace(searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().getValue()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();

			}
		});

		searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
		
		searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		
		// Definition Add New Functionality
		searchDisplay.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().clearAnnotations();
				searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().removeAllMarkers();
				//searchDisplay.getCQLDefinitionsView().getViewCQLAceEditor().redisplay();
				
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLDefinitionsView().getPanelViewCQLCollapse());
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					addNewDefinition();
				}
				//508 changes for Definitions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea());
			}
		});
		
		
		searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.NONE);
				String comment = searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea().getText();
				if(validator.validateForCommentTextArea(comment)){
					searchDisplay.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
							MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});
	}
	
	/**
	 * Adds the new definition.
	 */
	private void addNewDefinition() {

		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		searchDisplay.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getCQLDefinitionsView().getDefineAceEditor().getText() != null)) {
			searchDisplay.getCQLDefinitionsView().getDefineAceEditor().setText("");
		}
		if ((searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea() != null)) {
			searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().setText("");
		}
		
		if ((searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea()!= null)) {
			searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea().setText("");
		}
		searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex(), false);
		}

		// Functionality to reset the disabled features for supplemental data
		// definitions when erased.
		searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().setEnabled(true);
		searchDisplay.getCQLDefinitionsView().getDefineAceEditor().setReadOnly(false);
		searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().setEnabled(true);
		searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setEnabled(true);
		searchDisplay.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		searchDisplay.getDefineButtonBar().getSaveButton().setEnabled(true);
		searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getDefineButtonBar().getInsertButton().setEnabled(true);
		searchDisplay.getDefineButtonBar().getTimingExpButton().setEnabled(true);
		searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
		searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);
	
		
	}
	
	/**
	 * Adds the function event handlers.
	 */
	private void addFunctionEventHandlers() {
		searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(
							Document.get()
									.createDblClickEvent(searchDisplay.getCqlLeftNavBarPanelView()
											.getFuncNameListBox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
											false),
							searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox());
				}

			}
		});
		searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
					showSearchBusyOnDoubleClick(true);
					searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
					searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
					searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
					searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
					
					resetAceEditor(searchDisplay.getCQLFunctionsView().getViewCQLAceEditor());
					resetViewCQLCollapsiblePanel(searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse());
					
					searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
					
					
					if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
						showSearchBusyOnDoubleClick(false);
						searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					} else {
						int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox()
								.getSelectedIndex();
						if (selectedIndex != -1) {
							final String selectedFunctionId = searchDisplay.getCqlLeftNavBarPanelView()
									.getFuncNameListBox().getValue(selectedIndex);
							searchDisplay.getCqlLeftNavBarPanelView()
									.setCurrentSelectedFunctionObjId(selectedFunctionId);
							
							if (searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
									.get(selectedFunctionId) != null) {
								searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
								searchDisplay.getCQLFunctionsView().setWidgetReadOnly(false);
								searchDisplay.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
								// load most recent used cql artifacts
								MatContext.get().getCQLLibraryService().getUsedCqlArtifacts(
										MatContext.get().getCurrentCQLLibraryId(),
										new AsyncCallback<GetUsedCQLArtifactsResult>() {

											@Override
											public void onFailure(Throwable caught) {
												showSearchingBusy(false);
												searchDisplay.getCQLFunctionsView().setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
												Window.alert(
														MatContext.get().getMessageDelegate().getGenericErrorMessage());
											}

											@Override
											public void onSuccess(GetUsedCQLArtifactsResult result) {
												showSearchBusyOnDoubleClick(false);
												searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getName());
												searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().setText(searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getLogic());
												searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getCommentString());

												if (searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getContext().equalsIgnoreCase("patient")) {
													searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(true);
													searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
												} else {
													searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(true);
													searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(false);
												}
												CQLFunctions currentFunction = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId);
												if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
													searchDisplay.getCQLFunctionsView().setWidgetReadOnly(true);
													
													if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLFunctions().contains(currentFunction.getName())) {
														searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
													} else {
														searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
													}
												}
												Map<String,List<CQLErrors>> expressionCQLErrorMap = result.getCqlErrorsPerExpression();
												if(expressionCQLErrorMap != null){
													List<CQLErrors> errorList = expressionCQLErrorMap.get(currentFunction.getName());
													searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
													searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
													for (CQLErrors error : errorList) {
														int startLine = error.getStartErrorInLine();
														int startColumn = error.getStartErrorAtOffset();
														searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.ERROR);
													}
													searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().setAnnotations();
												}
												if(result.getCqlErrors().isEmpty() && result.getExpressionReturnTypeMap() != null){
													searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText(result.getExpressionReturnTypeMap()
															.get(currentFunction.getName()));
													searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getExpressionReturnTypeMap()
															.get(currentFunction.getName()));
										
												} else {
													searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
													searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
												}
											}

										});
								
							} else {
								showSearchBusyOnDoubleClick(false);
							}
						} else {
							showSearchBusyOnDoubleClick(false);
						}
						if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
							CQLFunctions selectedFunction = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
									.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
							if (selectedFunction.getArgumentList() != null) {
								searchDisplay.getCQLFunctionsView().getFunctionArgumentList().clear();
								searchDisplay.getCQLFunctionsView().getFunctionArgumentList()
										.addAll(selectedFunction.getArgumentList());
							} else {
								searchDisplay.getCQLFunctionsView().getFunctionArgumentList().clear();
							}
						}
						searchDisplay.resetMessageDisplay();
					}
					searchDisplay.getCQLFunctionsView().createAddArgumentViewForFunctions(
							searchDisplay.getCQLFunctionsView().getFunctionArgumentList(),
							MatContext.get().getLibraryLockService().checkForEditPermission());
					
				}
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getMainFunctionVerticalPanel());
			}
		});
		
		searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {

			@Override
			public void onShow(ShowEvent showEvent) {
				searchDisplay.resetMessageDisplay();
				showCompleteCQL(searchDisplay.getCQLFunctionsView().getViewCQLAceEditor());
			}
		});

		searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent hideEvent) {
				searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetAceEditor(searchDisplay.getCQLFunctionsView().getViewCQLAceEditor());

			}
		});
		
		
		// Function Insert Icon Functionality
		searchDisplay.getFunctionButtonBar().getInsertButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				buildInsertPopUp();
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor());
			}
		});

		// Function Save Icon Functionality
		searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse());
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					addAndModifyFunction();
					//508 changes for Functions Section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getFuncNameTxtArea());
				}

			}
		});

		// Functions Erase Icon Functionality
		searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse());
				eraseFunction();
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor());
			}
		});

		// Add Function Argument functionality
		searchDisplay.getCQLFunctionsView().getAddNewArgument().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.hideAceEditorAutoCompletePopUp();
				searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse());
				CQLFunctionArgument addNewFunctionArgument = new CQLFunctionArgument();
				AddFunctionArgumentDialogBox.showArgumentDialogBox(addNewFunctionArgument, false,
						searchDisplay.getCQLFunctionsView(), searchDisplay.getCqlLeftNavBarPanelView(),
						MatContext.get().getLibraryLockService().checkForEditPermission());
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
			}
		});
		// Function Delete Icon Functionality
		searchDisplay.getFunctionButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse());
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDeleteConfirmationFunctionCQLLibraryWorkspace(searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().getValue()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
				
				

			}

		});

		searchDisplay.getCQLFunctionsView().setObserver(new Observer() {

			@Override
			public void onModifyClicked(CQLFunctionArgument result) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				searchDisplay.resetMessageDisplay();
				if (result.getArgumentType().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
					getAttributesForDataType(result);
				} else {
					AddFunctionArgumentDialogBox.showArgumentDialogBox(result, true,
							searchDisplay.getCQLFunctionsView(), searchDisplay.getCqlLeftNavBarPanelView(),
							MatContext.get().getLibraryLockService().checkForEditPermission());
				}

			}

			@Override
			public void onDeleteClicked(CQLFunctionArgument result, int index) {
				searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(result.getId());
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(result.getArgumentName());
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_FUNCTION_ARGUMENT(result.getArgumentName()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getMainFunctionVerticalPanel());
			}
		});

		searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
		
		searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
		

		// Function Add New Functionality
		searchDisplay.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLFunctionsView().getPanelViewCQLCollapse());
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					addNewFunction();
				}
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getMainFunctionVerticalPanel());
			}
		});
		
		
		searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.NONE);
				String comment = searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().getText();
				if(validator.validateForCommentTextArea(comment)){
					searchDisplay.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
							MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});
	}
	
	/**
	 * Adds the new function.
	 */
	private void addNewFunction() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		searchDisplay.getCQLFunctionsView().getFunctionArgumentList().clear();
		searchDisplay.getCQLFunctionsView().getFunctionArgNameMap().clear();
		searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
		searchDisplay.createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>());
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().setText("");
		}
		if ((searchDisplay.getCQLFunctionsView().getFuncNameTxtArea() != null)) {
			searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().setText("");
		}
		
		if ((searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea()!= null)) {
			searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().setText("");
		}
		searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
		
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex(), false);
		}
		searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(true);
		searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
		searchDisplay.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
	}
	
	/**
	 * Adds the warning alert handlers.
	 */
	private void addWarningAlertHandlers() {

		searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationYesButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
				searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationMessageAlert().clearAlert();
				if (searchDisplay.getCqlLeftNavBarPanelView().isDoubleClick()) {
					clickEventOnListboxes();
				} else if (searchDisplay.getCqlLeftNavBarPanelView().isNavBarClick()) {
					changeSectionSelection();
				} else {
					clearViewIfDirtyNotSet();
				}
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationNoButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationMessageAlert().clearAlert();
				// no was selected, don't move anywhere
				if (searchDisplay.getCqlLeftNavBarPanelView().isNavBarClick()) {
					unsetActiveMenuItem(nextSection);
				}
				if (currentSection.equals(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
					searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().setSelectedIndex(-1);
				} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
					searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().setSelectedIndex(-1);
				} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
					searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().setSelectedIndex(-1);
				}
			}
		});

	}

	/**
	 * Adds the general info event handlers.
	 */
	private void addGeneralInfoEventHandlers() {
		searchDisplay.getCqlGeneralInformationView().getSaveButton().addClickHandler(event -> saveCQLGeneralInfo());
		searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().addKeyUpHandler(event -> resetMessagesAndSetPageDirty(true));
		searchDisplay.getCqlGeneralInformationView().getComments().addValueChangeHandler(event -> resetMessagesAndSetPageDirty(true));
	}
	
	private void resetMessagesAndSetPageDirty(boolean isPageDirty) {
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(isPageDirty);
		}
	}
	
	private void saveCQLGeneralInfo() {
		
		resetMessagesAndSetPageDirty(false);
		
		String libraryName = searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().getText().trim();
		if (libraryName.isEmpty()) {
			searchDisplay.getCqlGeneralInformationView().getLibNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getLibraryNameRequired());
		} else {

			if (validator.validateForAliasNameSpecialChar(libraryName)) {
				saveCQLGeneralInformation();
			} else {
				searchDisplay.getCqlGeneralInformationView().getLibNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getCqlStandAloneLibraryNameError());
			}

		}
	
	}
	
	/**
	 * Event Handlers for Ace Editors.
	 */
	private void addEventHandlerOnAceEditors() {
		searchDisplay.getCQLDefinitionsView().getDefineAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getCQLDefinitionsView().getDefineAceEditor().isReadOnly()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		searchDisplay.getCQLParametersView().getParameterAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getCQLParametersView().getParameterAceEditor().isReadOnly()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().isReadOnly()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
	}

	/**
	 * Adds the event handlers on context radio buttons.
	 */
	private void addEventHandlersOnContextRadioButtons() {
		searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
							searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);
						} else {
							searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn().setValue(true);
						}

					}
				});

		searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (searchDisplay.getCQLDefinitionsView().getContextDefinePOPRadioBtn().getValue()) {
							searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(false);
						} else {
							searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
						}

					}
				});

		searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().getValue()) {
							searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
						} else {
							searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().setValue(true);
						}
					}
				});

		searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (searchDisplay.getCQLFunctionsView().getContextFuncPOPRadioBtn().getValue()) {
							searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(false);
						} else {
							searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().setValue(true);
						}
					}
				});
	}

	
	/**
	 * Adds the include CQL library handlers.
	 */
	private void addIncludeCQLLibraryHandlers() {
		
		searchDisplay.getIncludeView().getSaveModifyButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
				searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
				final EditIncludedLibraryDialogBox editIncludedLibraryDialogBox = new EditIncludedLibraryDialogBox("Replace Library");
				editIncludedLibraryDialogBox.findAvailableLibraries(currentIncludeLibrarySetId, currentIncludeLibraryId, false);
				
				editIncludedLibraryDialogBox.getApplyButton().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
						if(editIncludedLibraryDialogBox.getSelectedList().size() > 0){
							final CQLIncludeLibrary toBeModified = searchDisplay.getCqlLeftNavBarPanelView()
									.getIncludeLibraryMap()
									.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId());
							
							final CQLLibraryDataSetObject dto = editIncludedLibraryDialogBox.getSelectedList().get(0);
							if (dto != null) {
								final CQLIncludeLibrary currentObject = new CQLIncludeLibrary(dto);
								
								MatContext.get().getCQLLibraryService().saveIncludeLibrayInCQLLookUp(
										MatContext.get().getCurrentCQLLibraryId(), toBeModified, currentObject,
										searchDisplay.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
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
												if (result != null) {
													if (result.isSuccess()) {
														searchDisplay.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(
																result.getCqlModel().getCqlIncludeLibrarys());
														searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
														MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
														MatContext.get().setIncludedValues(result);
														MatContext.get().setCQLModel(result.getCqlModel());
														editIncludedLibraryDialogBox.getDialogModal().hide();
														DomEvent.fireNativeEvent(
																Document.get()
																		.createDblClickEvent(searchDisplay.getCqlLeftNavBarPanelView()
																				.getIncludesNameListbox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
																				false),
																searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox());
														String libraryNameWithVersion = result.getIncludeLibrary().getCqlLibraryName() + " v" +result.getIncludeLibrary().getVersion();
														searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
																.createAlert(libraryNameWithVersion + " has been successfully saved as the alias " + result.getIncludeLibrary().getAliasName());
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
		
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(
							Document.get()
									.createDblClickEvent(searchDisplay.getCqlLeftNavBarPanelView()
											.getIncludesNameListbox().getSelectedIndex(), 0, 0, 0, 0, false, false, false,
											false),
							searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox());
				}

			}
		});
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
				.addDoubleClickHandler(new DoubleClickHandler() {
					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
							event.stopPropagation();
						} else {
							searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
							searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);

							if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
								searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
							} else {
								int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
										.getSelectedIndex();
								if (selectedIndex != -1) {
									final String selectedIncludeLibraryID = searchDisplay.getCqlLeftNavBarPanelView()
											.getIncludesNameListbox().getValue(selectedIndex);
									searchDisplay.getCqlLeftNavBarPanelView()
											.setCurrentSelectedIncLibraryObjId(selectedIncludeLibraryID);
									if (searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
											.get(selectedIncludeLibraryID) != null) {

										MatContext.get().getCQLLibraryService().findCQLLibraryByID(
												searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
														.get(selectedIncludeLibraryID).getCqlLibraryId(),
												new AsyncCallback<CQLLibraryDataSetObject>() {

													@Override
													public void onSuccess(CQLLibraryDataSetObject result) {
														if (result != null) {
															currentIncludeLibrarySetId = result.getCqlSetId();
															currentIncludeLibraryId = result.getId();
															searchDisplay.getIncludeView().buildIncludesReadOnlyView();

															searchDisplay.getIncludeView().getAliasNameTxtArea()
																	.setText(searchDisplay.getCqlLeftNavBarPanelView()
																			.getIncludeLibraryMap()
																			.get(selectedIncludeLibraryID)
																			.getAliasName());
															searchDisplay.getIncludeView().getViewCQLEditor()
																	.setText(result.getCqlText());
															searchDisplay.getIncludeView().getOwnerNameTextBox()
																	.setText(searchDisplay.getCqlLeftNavBarPanelView()
																			.getOwnerName(result));
															searchDisplay.getIncludeView().getCqlLibraryNameTextBox()
																	.setText(result.getCqlName());
																
															searchDisplay.getIncludeView().getSaveModifyButton().setEnabled(false);														
															searchDisplay.getIncludeView().getDeleteButton().setEnabled(false);

															searchDisplay.getIncludeView().getSaveModifyButton().setEnabled(false);
															if (MatContext.get().getLibraryLockService()
																	.checkForEditPermission()) {
																searchDisplay.getIncludeView().setWidgetReadOnly(false);
																searchDisplay.getIncludeView().getSaveModifyButton().setEnabled(true);

																
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

																				CQLIncludeLibrary cqlIncludeLibrary = searchDisplay
																						.getCqlLeftNavBarPanelView()
																						.getIncludeLibraryMap()
																						.get(selectedIncludeLibraryID);
																				
																				// if there are errors or the library is not in use, enable the delete button
																				if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLLibraries().contains(
																						cqlIncludeLibrary.getCqlLibraryName()+ "-"+ cqlIncludeLibrary.getVersion()+ "|"
																								+ cqlIncludeLibrary.getAliasName())) {
																					searchDisplay.getIncludeView().getDeleteButton().setEnabled(true);
																				}
																			}

																		});
															}
														}
													}

													@Override
													public void onFailure(Throwable caught) {
														searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
																.createAlert(MatContext.get().getMessageDelegate()
																		.getGenericErrorMessage());
													}
												});

										searchDisplay.getIncludeView().setSelectedObject(
												searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
														.get(selectedIncludeLibraryID).getCqlLibraryId());
										searchDisplay.getIncludeView()
												.setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
														.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
																.getIncludeLibraryMap()));
										searchDisplay.getIncludeView().getSelectedObjectList().clear();
									}
								}
								searchDisplay.resetMessageDisplay();

							}

						}

					}
				});

		// Includes search Button Functionality
		searchDisplay.getIncludeView().getSearchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAllIncludeLibraryList(searchDisplay.getIncludeView().getSearchTextBox().getText().trim());
				// 508 changes for Library Alias.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
			}
		});

		// Includes search Button Focus Functionality
		searchDisplay.getIncludeView().getFocusPanel().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				// Search when enter is pressed.
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					searchDisplay.getIncludeView().getSearchButton().click();
				}
			}
		});

		// Includes Save Functionality
		searchDisplay.getIncludeView().getIncludesButtonBar().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					addIncludeLibraryInCQLLookUp();
					// 508 changes for Library Alias.
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
				}
			}
		});

		// Includes Delete Functionality
		searchDisplay.getIncludeView().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_INCLUDE(searchDisplay.getIncludeView().getAliasNameTxtArea().getValue()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
				// 508 changes for Library Alias.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
			}

		});

		// Includes close Functionality
		searchDisplay.getIncludeView().getCloseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				// Below lines are to clear search suggestion textbox and
				// listbox
				// selection after erase.
				currentIncludeLibrarySetId =  null;
				searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
				if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
					searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().setItemSelected(
							searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(),
							false);
				}
				searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
				searchDisplay.buildIncludesView();
				SaveCQLLibraryResult cqlLibrarySearchModel = new SaveCQLLibraryResult();
				cqlLibrarySearchModel
						.setCqlLibraryDataSetObjects(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryList());
				searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
						.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
				searchDisplay.getIncludeView().buildIncludeLibraryCellTable(cqlLibrarySearchModel,
						MatContext.get().getLibraryLockService().checkForEditPermission(), false);
				searchDisplay.getIncludeView()
						.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
				if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
						.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
				}
				// 508 changes for Library Alias.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
			}
		});

		// Includes Erase Functionality
		searchDisplay.getIncludeView().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				clearAlias();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
						.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
				}
				// 508 changes for Library Alias.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
			}
		});

		// Includes Celltable Observer for CheckBox
		searchDisplay.getIncludeView().setObserver(new CQLIncludeLibraryView.Observer() {

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
								searchDisplay.getIncludeView().getViewCQLEditor().setText(result.getCqlText());
							}
						});
			}
		});
	}

	/**
	 * Adds and modify function.
	 */
	protected void addAndModifyFunction() {
		searchDisplay.resetMessageDisplay();
		final String functionName = searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().getText();
		String functionBody = searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().getText();
		String functionComment = searchDisplay.getCQLFunctionsView().getFunctionCommentTextArea().getText();
		String funcContext = "";
		if (searchDisplay.getCQLFunctionsView().getContextFuncPATRadioBtn().getValue()) {
			funcContext = "Patient";
		} else {
			funcContext = "Population";
		}
		
		boolean isValidFunctionName = isValidExpressionName(functionName);
		if (isValidFunctionName) {
			if (validator.validateForSpecialChar(functionName.trim())) {
				searchDisplay.getCQLFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
			
				
			} else if(validator.validateForCommentTextArea(functionComment)){
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				searchDisplay.getCQLFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
			} else {
				CQLFunctions function = new CQLFunctions();
				function.setLogic(functionBody);
				function.setName(functionName);
				function.setCommentString(functionComment);
				function.setArgumentList(searchDisplay.getCQLFunctionsView().getFunctionArgumentList());
				function.setContext(funcContext);
				CQLFunctions toBeModifiedParamObj = null;

				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
					toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().saveAndModifyFunctions(
						MatContext.get().getCurrentCQLLibraryId(), toBeModifiedParamObj, function,
						searchDisplay.getCqlLeftNavBarPanelView().getViewFunctions(),
						isFormatable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {

										searchDisplay.getCqlLeftNavBarPanelView()
												.setViewFunctions(result.getCqlModel().getCqlFunctions());
										MatContext.get()
												.setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
										searchDisplay.getCqlLeftNavBarPanelView()
												.setCurrentSelectedFunctionObjId(result.getFunction().getId());
										searchDisplay.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().updateFunctionMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.setVisible(true);

										searchDisplay.getCQLFunctionsView().getFuncNameTxtArea()
												.setText(result.getFunction().getName());
										searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor()
												.setText(result.getFunction().getLogic());
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor()
												.clearAnnotations();
										searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor()
												.removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().redisplay();

										if (validateCQLArtifact(result, currentSection)) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_FUNCTION_MODIFY_WITH_ERRORS(functionName.trim()));
											searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
											searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");

										} else if (!result.isDatatypeUsedCorrectly()) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getWarningBadDataTypeCombination());
											if(result.isValidCQLWhileSavingExpression()){
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText(result.getFunction().getReturnType());
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is " + result.getFunction().getReturnType());
											} else {
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}
										} else {
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_FUNCTION_MODIFY(functionName.trim()));
											if(result.isValidCQLWhileSavingExpression()){
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText(result.getFunction().getReturnType());
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is " + result.getFunction().getReturnType());
											} else {
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
												searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}
										}

										searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().setAnnotations();
										searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().redisplay();

									} else if (result.getFailureReason() == 1) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getERROR_DUPLICATE_IDENTIFIER_NAME());
										searchDisplay.getCQLFunctionsView().getFuncNameTxtArea()
												.setText(functionName.trim());
									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unable to find Node to modify.");
										searchDisplay.getCQLFunctionsView().getFuncNameTxtArea()
												.setText(functionName.trim());
									} else if (result.getFailureReason() == 3) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
										searchDisplay.getCQLFunctionsView().getFuncNameTxtArea()
												.setText(functionName.trim());
										if (result.getFunction() != null) {
											searchDisplay.createAddArgumentViewForFunctions(
													result.getFunction().getArgumentList());
										}
									} else if (result.getFailureReason() == 6) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
												.getMessageDelegate().getCqlFunctionArgumentNameError());
									} else if (result.getFailureReason() == 8) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
												.getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
									}

								}
								showSearchingBusy(false);
								
								// if there are cql errors enable the delete button
								if(!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
								}
								
								else {
									// if the function is in use, disable the delete button
									if(result.getUsedCQLArtifacts().getUsedCQLFunctions().contains(functionName)) {
										searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
									}
									
									else {
										searchDisplay.getCQLFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
									}
									
								}
								
								
							}
						});

			}

		} else {
			searchDisplay.getCQLFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(functionName.isEmpty() 
					? MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_FUNCTION()
					: "Invalid Function name. " + MessageDelegate.DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			searchDisplay.getCQLFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
		}

	}

	/**
	 * Adds the and modify parameters.
	 */
	private void addAndModifyParameters() {

		searchDisplay.resetMessageDisplay();
		final String parameterName = searchDisplay.getCQLParametersView().getParameterNameTxtArea().getText();
		String parameterLogic = searchDisplay.getCQLParametersView().getParameterAceEditor().getText();
		String parameterComment = searchDisplay.getCQLParametersView().getParameterCommentTextArea().getText();
		
		boolean isValidParamaterName = isValidExpressionName(parameterName);
		if (isValidParamaterName) {
			if (validator.validateForSpecialChar(parameterName.trim())) {
				searchDisplay.getCQLParametersView().getParamNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
			} else if(validator.validateForCommentTextArea(parameterComment)){
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				searchDisplay.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.ERROR);
				
			} else {
				CQLParameter parameter = new CQLParameter();
				parameter.setLogic(parameterLogic);
				parameter.setName(parameterName);
				parameter.setCommentString(parameterComment);
				CQLParameter toBeModifiedParamObj = null;

				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
					toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().saveAndModifyParameters(
						MatContext.get().getCurrentCQLLibraryId(), toBeModifiedParamObj, parameter,
						searchDisplay.getCqlLeftNavBarPanelView().getViewParameterList(),
						isFormatable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										searchDisplay.getCqlLeftNavBarPanelView()
												.setViewParameterList(result.getCqlModel().getCqlParameters());
										MatContext.get().setParameters(
												getParamaterList(result.getCqlModel().getCqlParameters()));
										MatContext.get().setCQLModel(result.getCqlModel());
										searchDisplay.getCqlLeftNavBarPanelView()
												.setCurrentSelectedParamerterObjId(result.getParameter().getId());
										searchDisplay.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().updateParamMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
										searchDisplay.getCQLParametersView().getParameterNameTxtArea()
												.setText(result.getParameter().getName());
										searchDisplay.getCQLParametersView().getParameterAceEditor()
												.setText(result.getParameter().getLogic());
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
										searchDisplay.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getCQLParametersView().getParameterAceEditor().redisplay();

										if (validateCQLArtifact(result, currentSection)) {
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_PARAMETER_MODIFY_WITH_ERRORS(parameterName.trim()));

										} else if (!result.isDatatypeUsedCorrectly()) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getWarningBadDataTypeCombination());
										} else {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_PARAMETER_MODIFY(parameterName.trim()));
										}

										

										searchDisplay.getCQLParametersView().getParameterAceEditor().setAnnotations();
										searchDisplay.getCQLParametersView().getParameterAceEditor().redisplay();

									} else if (result.getFailureReason() == 1) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getERROR_DUPLICATE_IDENTIFIER_NAME());
										searchDisplay.getCQLParametersView().getParameterNameTxtArea()
												.setText(parameterName.trim());
									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unable to find Node to modify.");
										searchDisplay.getCQLParametersView().getParameterNameTxtArea()
												.setText(parameterName.trim());
									} else if (result.getFailureReason() == 3) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
										searchDisplay.getCQLParametersView().getParameterNameTxtArea()
												.setText(parameterName.trim());
									} else if (result.getFailureReason() == 8) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
												.getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
									}
								}
								showSearchingBusy(false);
								
								// if there are errors, enable the parameter delete button
								if (!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
								}

								else {
									// if the saved parameter is in use, then disable the delete button
									if (result.getUsedCQLArtifacts().getUsedCQLParameters().contains(parameterName)) {
										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
									}

									else {
										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(true);
									}
								}

							}
						});

			}

		} else {
			searchDisplay.getCQLParametersView().getParamNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(parameterName.isEmpty() 
						? MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_PARAMETER()
						: "Invalid Parameter name. " + MessageDelegate.DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
		}

	}

	/**
	 * This method is called to Add/Modify Definitions into Library Xml.
	 * 
	 */
	private void addAndModifyDefintions() {

		searchDisplay.resetMessageDisplay();
		final String definitionName = searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().getText();
		String definitionLogic = searchDisplay.getCQLDefinitionsView().getDefineAceEditor().getText();
		String definitionComment = searchDisplay.getCQLDefinitionsView().getDefineCommentTextArea().getText();
		String defineContext = "";
		if (searchDisplay.getCQLDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}
		
		boolean isValidDefinitionName = isValidExpressionName(definitionName);
		if (isValidDefinitionName) {
			if (validator.validateForSpecialChar(definitionName.trim())) {

				searchDisplay.getCQLDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
			} else if(validator.validateForCommentTextArea(definitionComment)){
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				searchDisplay.getCQLDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
		
			} else {
				final CQLDefinition define = new CQLDefinition();
				define.setName(definitionName);
				define.setLogic(definitionLogic);
				define.setCommentString(definitionComment);
				define.setContext(defineContext);
				CQLDefinition toBeModifiedObj = null;

				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
					toBeModifiedObj = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().saveAndModifyDefinitions(
						MatContext.get().getCurrentCQLLibraryId(), toBeModifiedObj, define,
						searchDisplay.getCqlLeftNavBarPanelView().getViewDefinitions(),
						isFormatable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										searchDisplay.getCqlLeftNavBarPanelView()
												.setViewDefinitions(result.getCqlModel().getDefinitionList());
										MatContext.get().setDefinitions(
												getDefinitionList(result.getCqlModel().getDefinitionList()));
										searchDisplay.getCqlLeftNavBarPanelView()
												.setCurrentSelectedDefinitionObjId(result.getDefinition().getId());
										searchDisplay.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().updateDefineMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
										searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea()
												.setText(result.getDefinition().getName());
										searchDisplay.getCQLDefinitionsView().getDefineAceEditor()
												.setText(result.getDefinition().getLogic());
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
										searchDisplay.getCQLDefinitionsView().getDefineAceEditor().removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getCQLDefinitionsView().getDefineAceEditor().redisplay();
										searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
										if (validateCQLArtifact(result, currentSection)) {
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_DEFINITION_MODIFY_WITH_ERRORS(definitionName.trim()));
											searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
											searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
										} else if (!result.isDatatypeUsedCorrectly()) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getWarningBadDataTypeCombination());
											if(result.isValidCQLWhileSavingExpression()){
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getDefinition().getReturnType());
											} else {
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}
											
										} else {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_DEFINITION_MODIFY(definitionName.trim()));
											if(result.isValidCQLWhileSavingExpression()){
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getDefinition().getReturnType());
											} else {
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");
												searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
												
											}
										}

										searchDisplay.getCQLDefinitionsView().getDefineAceEditor().setAnnotations();
										searchDisplay.getCQLDefinitionsView().getDefineAceEditor().redisplay();

									} else {
										if (result.getFailureReason() == 1) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getERROR_DUPLICATE_IDENTIFIER_NAME());
											searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea()
													.setText(definitionName.trim());
										} else if (result.getFailureReason() == 2) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert("Unable to find Node to modify.");
											searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea()
													.setText(definitionName.trim());
										} else if (result.getFailureReason() == 3) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea()
													.setText(definitionName.trim());
										} else if (result.getFailureReason() == 8) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
										}
									}

								}
								showSearchingBusy(false);
								
								// if there are errors, enable the
								// definition button
								if (!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton()
											.setEnabled(true);
								}
				
								else {
									// if the saved definition is in
									// use, then disable the delete
									// button
									if (result.getUsedCQLArtifacts().getUsedCQLDefinitions().contains(definitionName)) {
										searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
									}
			
									else {
										searchDisplay.getCQLDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(true);
									}
								}

							}
						});

			}

		} else {
			searchDisplay.getCQLDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(definitionName.isEmpty() 
					? MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_DEFINITION()
					: "Invalid Definition name. " + MessageDelegate.DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			searchDisplay.getCQLDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
		}

	}

	/**
	 * Adds the include library in CQL look up.
	 */
	private void addIncludeLibraryInCQLLookUp() {
		searchDisplay.resetMessageDisplay();
		if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
				.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
			searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
			return;

		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
		}
		final String aliasName = searchDisplay.getIncludeView().getAliasNameTxtArea().getText();

		if (!aliasName.isEmpty() && searchDisplay.getIncludeView().getSelectedObjectList().size() > 0) {
			// functioanlity to add Include Library
			CQLLibraryDataSetObject cqlLibraryDataSetObject = searchDisplay.getIncludeView().getSelectedObjectList()
					.get(0);

			if (validator.validateForAliasNameSpecialChar(aliasName.trim())) {

				CQLIncludeLibrary incLibrary = new CQLIncludeLibrary();
				incLibrary.setAliasName(aliasName);
				incLibrary.setCqlLibraryId(cqlLibraryDataSetObject.getId());
				String versionValue = cqlLibraryDataSetObject.getVersion().replace("v", "") + "."
						+ cqlLibraryDataSetObject.getRevisionNumber();
				incLibrary.setVersion(versionValue);
				incLibrary.setCqlLibraryName(cqlLibraryDataSetObject.getCqlName());
				incLibrary.setQdmVersion(cqlLibraryDataSetObject.getQdmVersion());
				incLibrary.setSetId(cqlLibraryDataSetObject.getCqlSetId());
				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() == null) {
					// this is just to add include library and not modify
					MatContext.get().getCQLLibraryService().saveIncludeLibrayInCQLLookUp(
							MatContext.get().getCurrentCQLLibraryId(), null, incLibrary,
							searchDisplay.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
							new AsyncCallback<SaveUpdateCQLResult>() {

								@Override
								public void onFailure(Throwable caught) {
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getGenericErrorMessage());

								}

								@Override
								public void onSuccess(SaveUpdateCQLResult result) {
									if (result != null) {
										if (result.isSuccess()) {
											searchDisplay.resetMessageDisplay();
											searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
											searchDisplay.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(
													result.getCqlModel().getCqlIncludeLibrarys());
											MatContext.get().setIncludes(
													getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
											MatContext.get().setCQLModel(result.getCqlModel());
											searchDisplay.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
											searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
											searchDisplay.getIncludeView()
													.setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
															.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
																	.getIncludeLibraryMap()));
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getIncludeLibrarySuccessMessage(
																	result.getIncludeLibrary().getAliasName()));
											clearAlias();

											MatContext.get().setIncludedValues(result);
											if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
													.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
														.createAlert(MatContext.get().getMessageDelegate()
																.getCqlLimitWarningMessage());
											} else {
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
														.clearAlert();
											}

										} else if (result.getFailureReason() == 1) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getAliasNameTxtArea().setText(aliasName.trim());
										} else if (result.getFailureReason() == 2) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert("Missing includes library tag.");
											searchDisplay.getAliasNameTxtArea().setText(aliasName.trim());
										} else if (result.getFailureReason() == 3) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getAliasNameTxtArea().setText(aliasName.trim());
										}
									}
								}
							});
				}

			} else {
				searchDisplay.getCqlIncludeLibraryView().getAliasNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
						MatContext.get().getMessageDelegate().getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getAliasNameTxtArea().setText(aliasName.trim());
			}

		} else {
			searchDisplay.getCqlIncludeLibraryView().getAliasNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getSAVE_INCLUDE_LIBRARY_VALIATION_ERROR());
		}
	}
	
	/**
	 * Clears the ace editor for parameters. Functionality for the eraser icon in parameter section. 
	 */
	private void eraseParameter() {
		
		searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if(searchDisplay.getCQLParametersView().getParameterAceEditor().getText() != null) {
			searchDisplay.getCQLParametersView().getParameterAceEditor().setText("");
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}
		
	}
	
	/**
	 * Clears the ace editor for definitions. Functionality for the eraser icon in definition section. 
	 */
	private void eraseDefinition() {
		
		searchDisplay.getCQLDefinitionsView().getDefineAceEditor().clearAnnotations();
		if ((searchDisplay.getCQLDefinitionsView().getDefineAceEditor().getText() != null)) {
			searchDisplay.getCQLDefinitionsView().getDefineAceEditor().setText("");
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}
	}
	
	/**
	 * Clears the ace editor for functions. Functionality for the eraser icon in function section. 
	 */
	private void eraseFunction() {
		
		searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if ((searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor().setText("");
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}
	}

	/**
	 * Builds the insert pop up.
	 */
	/*
	 * Build Insert Pop up.
	 */
	private void buildInsertPopUp() {
		searchDisplay.resetMessageDisplay();
		InsertIntoAceEditorDialogBox
				.showListOfItemAvailableForInsertDialogBox(searchDisplay.getCqlLeftNavBarPanelView(), curAceEditor);
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
	}

	/**
	 * Delete definition.
	 */
	protected void deleteDefinition() {
		searchDisplay.resetMessageDisplay();
		final String definitionName = searchDisplay.getDefineNameTxtArea().getText();
		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
				final CQLDefinition toBeModifiedObj = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
						.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().deleteDefinition(MatContext.get().getCurrentCQLLibraryId(),
						toBeModifiedObj, searchDisplay.getCqlLeftNavBarPanelView().getViewDefinitions(),
						new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
										MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										searchDisplay.getCqlLeftNavBarPanelView()
												.setViewDefinitions(result.getCqlModel().getDefinitionList());
										MatContext.get().setDefinitions(
												getDefinitionList(result.getCqlModel().getDefinitionList()));
										searchDisplay.getCqlLeftNavBarPanelView()
												.clearAndAddDefinitionNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().updateDefineMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.setVisible(true);

										searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox()
												.setText("");
										searchDisplay.getDefineNameTxtArea().setText("");
										searchDisplay.getDefineAceEditor().setText("");
										searchDisplay.getCqlLeftNavBarPanelView()
												.setCurrentSelectedDefinitionObjId(null);
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getDefineAceEditor().clearAnnotations();
										searchDisplay.getDefineAceEditor().removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getDefineAceEditor().redisplay();
										searchDisplay.getDefineAceEditor().setAnnotations();
										searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(false);
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate().getSuccessfulDefinitionRemoveMessage(toBeModifiedObj.getName()));
										searchDisplay.getCQLDefinitionsView().getReturnTypeTextBox().setText("");

									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unable to find Node to modify.");
										searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
									} else if (result.getFailureReason() == 4) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unauthorized delete operation.");
										searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
									}
								}
								showSearchingBusy(false);
								//508 changes for Definitions Section
								searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLDefinitionsView().getMainDefineViewVerticalPanel());
							}
						});
			} else {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert("Please select a definition to delete.");
				searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
			}
	}

	/**
	 * Delete function.
	 */
	protected void deleteFunction() {

		searchDisplay.resetMessageDisplay();
		final String functionName = searchDisplay.getFuncNameTxtArea().getText();
		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
			final CQLFunctions toBeModifiedFuncObj = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
					.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
			showSearchingBusy(true);
			MatContext.get().getCQLLibraryService().deleteFunctions(MatContext.get().getCurrentCQLLibraryId(),
					toBeModifiedFuncObj, searchDisplay.getCqlLeftNavBarPanelView().getViewFunctions(),
					new AsyncCallback<SaveUpdateCQLResult>() {

						@Override
						public void onFailure(Throwable caught) {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							showSearchingBusy(false);
						}

						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							if (result != null) {
								if (result.isSuccess()) {
									searchDisplay.getCqlLeftNavBarPanelView()
											.setViewFunctions(result.getCqlModel().getCqlFunctions());
									MatContext.get()
											.setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
									searchDisplay.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
									searchDisplay.getCqlLeftNavBarPanelView().updateFunctionMap();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
									searchDisplay.getCQLFunctionsView().getFunctionArgNameMap().clear();
									searchDisplay.getCQLFunctionsView().getFunctionArgumentList().clear();
									searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox()
											.setText("");
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
											.setVisible(true);
									searchDisplay.getFuncNameTxtArea().setText("");
									searchDisplay.getFunctionBodyAceEditor().setText("");
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
									searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
									searchDisplay.getFunctionBodyAceEditor().clearAnnotations();
									searchDisplay.getFunctionBodyAceEditor().removeAllMarkers();
									//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
									//searchDisplay.getFunctionBodyAceEditor().redisplay();
									searchDisplay.getFunctionBodyAceEditor().setAnnotations();
									searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
											.createAlert(MatContext.get().getMessageDelegate().getSuccessfulFunctionRemoveMessage(toBeModifiedFuncObj.getName()));
									
									
									
									searchDisplay.getCQLFunctionsView().getReturnTypeTextBox().setText("");
									
									if (result.getFunction() != null) {
										searchDisplay.createAddArgumentViewForFunctions(
												new ArrayList<CQLFunctionArgument>());
									}
									
								} else if (result.getFailureReason() == 2) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
											.createAlert("Unable to find Node to modify.");
									searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
								} else if (result.getFailureReason() == 4) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
											.createAlert("Unauthorized delete operation.");
									searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
								}

								
							}
							showSearchingBusy(false);
							//508 Compliance for Function section
							searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getMainFunctionVerticalPanel());
						}
					});
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select a function to delete.");
			searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
		}
	}
	
	protected void deleteFunctionArgument(){
		
		String funcArgName = null;
		
		searchDisplay.resetMessageDisplay();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		Iterator<CQLFunctionArgument> iterator = searchDisplay.getCQLFunctionsView().getFunctionArgumentList()
				.iterator();
		searchDisplay.getCQLFunctionsView().getFunctionArgNameMap()
		.remove(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentName().toLowerCase());
		while (iterator.hasNext()) {
			CQLFunctionArgument cqlFunArgument = iterator.next();
			if (cqlFunArgument.getId().equals(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId())) {

				iterator.remove();
				searchDisplay.getCQLFunctionsView().createAddArgumentViewForFunctions(
						searchDisplay.getCQLFunctionsView().getFunctionArgumentList(),
						MatContext.get().getMeasureLockService().checkForEditPermission());
				funcArgName = cqlFunArgument.getArgumentName();
				break;
			}
		}

		//resetting name and id
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		
		//508 Compliance for Function section
		searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLFunctionsView().getFuncNameTxtArea());
		
		searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
		.createAlert(MatContext.get().getMessageDelegate().getSuccessfulFunctionArgumentRemoveMessage(funcArgName));

	}

	/**
	 * Delete parameter.
	 */
	protected void deleteParameter() {

		searchDisplay.resetMessageDisplay();
		final String parameterName = searchDisplay.getParameterNameTxtArea().getText();
		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
			final CQLParameter toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap()
					.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());
			
			if(toBeModifiedParamObj.isReadOnly()){
				searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert("Unauthorized delete operation.");
				searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
				
			} else {
				showSearchingBusy(true);
				MatContext.get().getCQLLibraryService().deleteParameter(MatContext.get().getCurrentCQLLibraryId(),
						toBeModifiedParamObj,
						searchDisplay.getCqlLeftNavBarPanelView().getViewParameterList(),
						new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										searchDisplay.getCqlLeftNavBarPanelView()
												.setViewParameterList((result.getCqlModel().getCqlParameters()));
										MatContext.get().setParameters(
												getParamaterList(result.getCqlModel().getCqlParameters()));
										searchDisplay.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().updateParamMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.setVisible(true);

										searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox()
												.setText("");
										searchDisplay.getParameterNameTxtArea().setText("");
										searchDisplay.getParameterAceEditor().setText("");
										searchDisplay.getCqlLeftNavBarPanelView()
												.setCurrentSelectedParamerterObjId(null);
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getParameterAceEditor().clearAnnotations();
										searchDisplay.getParameterAceEditor().removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getParameterAceEditor().redisplay();
										searchDisplay.getParameterAceEditor().setAnnotations();
										searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate().getSuccessfulParameterRemoveMessage(toBeModifiedParamObj.getName()));  
									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unable to find Node to modify.");
										searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
									}else if (result.getFailureReason() == 4) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unauthorized delete operation.");
										searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
									}
								}
								showSearchingBusy(false);
								//508 Compliance for Function section
								searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLParametersView().getMainParamViewVerticalPanel());
							}
						});
			} 
		}else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select parameter to delete.");
			searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
		}
	}

	/**
	 * Delete include.
	 */
	protected void deleteInclude() {

		searchDisplay.resetMessageDisplay();
		final String aliasName = searchDisplay.getIncludeView().getAliasNameTxtArea().getText();
		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() != null) {
			final CQLIncludeLibrary toBeModifiedIncludeObj = searchDisplay.getCqlLeftNavBarPanelView()
					.getIncludeLibraryMap()
					.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId());
			showSearchingBusy(true);
			MatContext.get().getCQLLibraryService().deleteInclude(MatContext.get().getCurrentCQLLibraryId(),
					toBeModifiedIncludeObj, 
					searchDisplay.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
					new AsyncCallback<SaveUpdateCQLResult>() {

						@Override
						public void onFailure(Throwable caught) {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							showSearchingBusy(false);
						}

						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							if (result.isSuccess()) {
								searchDisplay.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(result.getCqlModel().getCqlIncludeLibrarys());
								MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
								
								MatContext.get().setIncludedValues(result);


								searchDisplay.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
								searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);

								searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox()
										.setText("");
								searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
								searchDisplay.getIncludeView().getCqlLibraryNameTextBox().setText("");
								searchDisplay.getIncludeView().getOwnerNameTextBox().setText("");
								searchDisplay.getIncludeView().getViewCQLEditor().setText("");
								searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
								searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
								searchDisplay.getIncludeView().getViewCQLEditor().clearAnnotations();
								searchDisplay.getIncludeView().getViewCQLEditor().removeAllMarkers();
								searchDisplay.getIncludeView().getViewCQLEditor().setAnnotations();
								searchDisplay.getIncludeView().getDeleteButton().setEnabled(false);

								searchDisplay.getIncludeView().getCloseButton()
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
								
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getSuccessfulIncludeRemoveMessage(toBeModifiedIncludeObj.getAliasName()));
							} else if (result.getFailureReason() == 2) {
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert("Unable to find Node to modify.");
								searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
							}
							showSearchingBusy(false);
						}
					});
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select an alias to delete.");
			searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
		}
	}

	private void deleteCode(){
		searchDisplay.resetMessageDisplay();
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().deleteCode(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedCodesObjId(), MatContext.get().getCurrentCQLLibraryId(), new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
				showSearchingBusy(false);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
				showSearchingBusy(false);
				if(result.isSuccess()){
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_CODE_REMOVE_MSG(result.getCqlCode().getCodeOID()));
					searchDisplay.getCodesView().resetCQLCodesSearchPanel();
					appliedCodeTableList.clear();
					appliedCodeTableList.addAll(result.getCqlCodeList());
					MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);
					searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
					//searchDisplay.buildCodes();
					searchDisplay.getCodesView().buildCodesCellTable(
							appliedCodeTableList,
							MatContext.get().getLibraryLockService().checkForEditPermission());
					//Temporary fix to update codes for insert Icon.
					getAppliedValueSetList();
				} else {
					
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Unable to delete.");
					
				}
				
				//508 : Shift focus to code search panel.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
			}
		});
	}
	
	private void checkAndDeleteValueSet(){
		MatContext.get().getLibraryService().getCQLData(MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onSuccess(final SaveUpdateCQLResult result) {
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(null);
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
										if (dataSetDTO.getUuid().equals(searchDisplay.getValueSetView()
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
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(null);
						showSearchingBusy(false);
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}
				});
	}
	/**
	 * Save CQL general information.
	 */
	private void saveCQLGeneralInformation() {

		String libraryId = MatContext.get().getCurrentCQLLibraryId();
		String libraryValue = searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().getText().trim();
		String libraryComment = searchDisplay.getCqlGeneralInformationView().getComments().getText().trim();
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().saveAndModifyCQLGeneralInfo(libraryId, libraryValue, libraryComment,
				new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result != null) {
							cqlLibraryName = result.getCqlModel().getLibraryName().trim();
							cqlLibraryComment = result.getCqlModel().getLibraryComment();
							searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().setText(cqlLibraryName);
							searchDisplay.getCqlGeneralInformationView().getComments().setText(result.getCqlModel().getLibraryComment());
							searchDisplay.getCqlGeneralInformationView().getComments().setCursorPos(0);
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
									.createAlert(cqlLibraryName + " general information successfully updated");
							searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
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
		searchDisplay.buildView();
		addLeftNavEventHandler();
		getCQLDataForLoad(); 
		searchDisplay.resetMessageDisplay();
		panel.add(searchDisplay.asWidget());
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		currentIncludeLibrarySetId = null;
		currentIncludeLibraryId = null; 
		searchDisplay.getCqlLeftNavBarPanelView().clearShotcutKeyList();
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		searchDisplay.getFunctionArgNameMap().clear();
		searchDisplay.getValueSetView().clearCellTableMainPanel();
		searchDisplay.getCodesView().clearCellTableMainPanel();
		searchDisplay.getIncludeView().getSearchTextBox().setText("");
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		searchDisplay.resetMessageDisplay();
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
				.setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
				.setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
				.setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
				.setClassName("panel-collapse collapse");
		if (searchDisplay.getFunctionArgumentList().size() > 0) {
			searchDisplay.getFunctionArgumentList().clear();
		}
		isModified = false;
		isCodeModified = false;
		setId = null;
		modifyValueSetDTO = null;
		curAceEditor = null;
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		searchDisplay.getCqlLeftNavBarPanelView().getMessagePanel().clear();
		searchDisplay.resetAll();
		panel.clear();
		searchDisplay.getMainPanel().clear();
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
			searchDisplay.getLockedButtonVPanel();
			panel.add(searchDisplay.asWidget());
			if (!isCQLWorkSpaceLoaded) { 
				// this check is made so that when CQL is clicked from CQL library, its not called twice.
				displayCQLView();

			} else {
				isCQLWorkSpaceLoaded = false;
			}
		}
		
		//Load VSAC Programs and Releases
		getProgramsAndReleases();
		
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
					cqlLibraryName = searchDisplay.getCqlGeneralInformationView()
							.createCQLLibraryName(MatContext.get().getCurrentCQLLibraryeName());
					cqlLibraryComment = result.getCqlModel().getLibraryComment();
					String libraryVersion = MatContext.get().getCurrentCQLLibraryVersion();

					libraryVersion = libraryVersion.replaceAll("Draft ", "").trim();
					if (libraryVersion.startsWith("v")) {
						libraryVersion = libraryVersion.substring(1);
					}
					searchDisplay.getCqlGeneralInformationView().setGeneralInfoOfLibrary(cqlLibraryName, libraryVersion,
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
				searchDisplay.getCqlLeftNavBarPanelView().setAppliedQdmTableList(appliedValueSetTableList);
				searchDisplay.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);

				if (result.getCqlModel().getCodeList() != null) {
					appliedCodeTableList.addAll(result.getCqlModel().getCodeList());
				}
				searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
				searchDisplay.getCqlLeftNavBarPanelView().setAppliedCodeTableList(appliedCodeTableList);
				MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);

				if ((result.getCqlModel().getDefinitionList() != null)
						&& (result.getCqlModel().getDefinitionList().size() > 0)) {
					searchDisplay.getCqlLeftNavBarPanelView()
							.setViewDefinitions(result.getCqlModel().getDefinitionList());
					searchDisplay.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
					searchDisplay.getCqlLeftNavBarPanelView().updateDefineMap();
					MatContext.get().setDefinitions(getDefinitionList(result.getCqlModel().getDefinitionList()));
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getDefineBadge().setText("00");
				}
				if ((result.getCqlModel().getCqlParameters() != null)
						&& (result.getCqlModel().getCqlParameters().size() > 0)) {
					searchDisplay.getCqlLeftNavBarPanelView()
							.setViewParameterList(result.getCqlModel().getCqlParameters());
					searchDisplay.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
					searchDisplay.getCqlLeftNavBarPanelView().updateParamMap();
					MatContext.get().setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getParamBadge().setText("00");
				}
				if ((result.getCqlModel().getCqlFunctions() != null)
						&& (result.getCqlModel().getCqlFunctions().size() > 0)) {
					searchDisplay.getCqlLeftNavBarPanelView().setViewFunctions(result.getCqlModel().getCqlFunctions());
					searchDisplay.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
					searchDisplay.getCqlLeftNavBarPanelView().updateFunctionMap();
					MatContext.get().setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getFunctionBadge().setText("00");
				}
				if ((result.getCqlModel().getCqlIncludeLibrarys() != null)
						&& (result.getCqlModel().getCqlIncludeLibrarys().size() > 0)) {
					searchDisplay.getCqlLeftNavBarPanelView()
							.setViewIncludeLibrarys(result.getCqlModel().getCqlIncludeLibrarys());
					searchDisplay.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
					searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
					MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
					MatContext.get().setIncludedValues(result);


				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getIncludesBadge().setText("00");
					searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap().clear();
				}

				boolean isValidQDMVersion = searchDisplay.getCqlLeftNavBarPanelView()
						.checkForIncludedLibrariesQDMVersion();
				if (!isValidQDMVersion) {
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getINVALID_QDM_VERSION_IN_INCLUDES());
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
				}
			}
		}
	}

	/**
	 * Adds the left nav event handler.
	 */
	private void addLeftNavEventHandler() {

		searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// searchDisplay.hideAceEditorAutoCompletePopUp();
				generalInfoEvent();
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				// searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					includesEvent();
				}

			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				// searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_CODES;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					codesEvent();
					//508 : Shift focus to code search panel.
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
				}

			}
			
		});
		
		searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				appliedQDMEvent();
				//508 : Shift focus to search panel.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
			}
		});
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					parameterEvent();
				}

			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					definitionEvent();
				}
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					functionEvent();
				}
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.hideAceEditorAutoCompletePopUp();
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
                            		for(CQLQualityDataSetDTO cqlDTo : searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList()){
                                      cqlDTo.setUsed(false);
                            		}
                            	}
                            	
                            	// otherwise, check if the valueset is in the used valusets list
                            	else {
                                   for(CQLQualityDataSetDTO cqlDTo : searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList()){
                                          if (result.getUsedCQLValueSets().contains(cqlDTo.getName())) {
                                              cqlDTo.setUsed(true);
                                          } else{
                                        	  cqlDTo.setUsed(false);
                                          }
                                   }
                            	}
                            		
                            	if(searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList().size() > 0) {
                                	searchDisplay.getValueSetView().getCelltable().redraw();
                                	searchDisplay.getValueSetView().getListDataProvider().refresh();
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
		searchDisplay.getValueSetView().getCancelQDMButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				isModified = false;
				searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
				//508 compliance for Value Sets
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				
				previousIsProgramAndReleaseListBoxesEnabled = areProgramAndReleaseListBoxesEnabled;
				areProgramAndReleaseListBoxesEnabled = true; 
				
				loadProgramsAndReleases(); 
				alert508StateChanges();
			}
		});

		searchDisplay.getValueSetView().getUpdateFromVSACButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					updateVSACValueSets();
					//508 compliance for Value Sets
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				}
			}
		});

		/**
		 * this functionality is to retrieve the value set from VSAC with latest
		 * information which consists of Expansion Profile list and Version
		 * List.
		 */
		searchDisplay.getValueSetView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();					
					String expansionProfile = searchDisplay.getValueSetView().getReleaseListBox().getSelectedValue();
					expansionProfile = MatContext.PLEASE_SELECT.equals(expansionProfile) ? null : expansionProfile;
					searchValueSetInVsac(expansionProfile);
					//508 compliance for Value Sets
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput()); 
				}
			}
		});

		/**
		 * this handler is invoked when apply button is clicked on search Panel
		 * in QDM elements tab and this is to add new value set or user Defined
		 * QDM to the Applied QDM list.
		 */
		searchDisplay.getValueSetView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					MatContext.get().clearDVIMessages();
					searchDisplay.resetMessageDisplay();

					if (isModified && (modifyValueSetDTO != null)) {
						modifyValueSetOrUserDefined(isUserDefined);
					} else {
						addNewValueSet(isUserDefined);
					}
					//508 compliance for Value Sets
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput()); 
				}
			}
		});

		/**
		 * Adding value Change handler for UserDefined Input in Search Panel in
		 * QDM Elements Tab
		 * 
		 */
		searchDisplay.getValueSetView().getUserDefinedInput().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				searchDisplay.resetMessageDisplay();
				isUserDefined = searchDisplay.getValueSetView().validateUserDefinedInput();
			}
		});

		/**
		 * Adding value change handler for OID input in Search Panel in QDM
		 * elements Tab
		 */
		searchDisplay.getValueSetView().getOIDInput().addValueChangeHandler(event -> clearOID());
		
		searchDisplay.getValueSetView().getOIDInput().sinkBitlessEvent("input");
		
		searchDisplay.getValueSetView().setObserver(new CQLAppliedValueSetView.Observer() {
			@Override
			public void onModifyClicked(CQLQualityDataSetDTO result) {
				if(!searchDisplay.getValueSetView().getIsLoading()){
					searchDisplay.resetMessageDisplay();
					searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
					isModified = true;
					modifyValueSetDTO = result;
					String displayName = result.getName();
					// Substring at 60th character length.
					if(displayName.length() >=60){
						displayName = displayName.substring(0, 59);
					}
					HTML searchHeaderText = new HTML("<strong>Modify value set ( "+displayName +")</strong>");
					searchDisplay.getValueSetView().getSearchHeader().clear();
					searchDisplay.getValueSetView().getSearchHeader().add(searchHeaderText);
					searchDisplay.getValueSetView().getMainPanel().getElement().focus();
					if(result.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)){
						isUserDefined = true;
					} else {
						isUserDefined = false;
					}
					
					onModifyValueSet(result, isUserDefined);
					//508 Compliance for Value Sets section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				} else {
					//do nothing when loading.
				}
			}

			@Override
			public void onDeleteClicked(CQLQualityDataSetDTO result, final int index) {
				if(!searchDisplay.getValueSetView().getIsLoading()){
					searchDisplay.resetMessageDisplay();
					searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
					if((modifyValueSetDTO!=null) && modifyValueSetDTO.getId().equalsIgnoreCase(result.getId())){
						isModified = false;
					}
					String libraryId = MatContext.get().getCurrentCQLLibraryId();
					if ((libraryId != null) && !libraryId.equals("")) {
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(result.getId());
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_VALUESET(result.getName()));
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
						//508 Compliance for Value Sets section
						searchDisplay.getValueSetView().getOIDInput().setFocus(true);
					}
				} else {
					//do nothing when loading.
				}
			}

		});
		
		searchDisplay.getValueSetView().getClearButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				if(!searchDisplay.getValueSetView().getIsLoading()){
					searchDisplay.resetMessageDisplay();
					searchDisplay.getValueSetView().clearSelectedCheckBoxes();
				}
			}
		});
		
		searchDisplay.getValueSetView().getCopyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				copyValueSets();
			}
		});
		
		searchDisplay.getValueSetView().getSelectAllButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectAllValueSets();
			}
		});

		searchDisplay.getValueSetView().getPasteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getLibraryLockService().checkForEditPermission()) {
					pasteValueSets();
				} else {
					event.preventDefault();
				}
			}
		});
		
		searchDisplay.getValueSetView().getReleaseListBox().addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				isRetrieveButtonEnabled = true;
				searchDisplay.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);				
				previousIsApplyButtonEnabled = isApplyButtonEnabled;
				isApplyButtonEnabled = false;
				searchDisplay.getValueSetView().getSaveButton().setEnabled(isApplyButtonEnabled);
				alert508StateChanges();
			}
		});
		
		searchDisplay.getValueSetView().getProgramListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				isRetrieveButtonEnabled = true;
				searchDisplay.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);
				
				previousIsApplyButtonEnabled = isApplyButtonEnabled;
				isApplyButtonEnabled = false; 
				searchDisplay.getValueSetView().getSaveButton().setEnabled(isApplyButtonEnabled);
				
				searchDisplay.getValueSetView().loadReleases();
				
				alert508StateChanges();
			}
		});
	}
	
	private void clearOID() {
		
		previousIsRetrieveButtonEnabled = isRetrieveButtonEnabled;
		previousIsProgramAndReleaseListBoxesEnabled = areProgramAndReleaseListBoxesEnabled;

		searchDisplay.resetMessageDisplay();
		isUserDefined = searchDisplay.getValueSetView().validateOIDInput();
		
		if (searchDisplay.getValueSetView().getOIDInput().getValue().length() <= 0 ) {
			isRetrieveButtonEnabled = true;
			areProgramAndReleaseListBoxesEnabled = true;
			searchDisplay.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);
			loadProgramsAndReleases();
		} else {
			isRetrieveButtonEnabled = true;
			searchDisplay.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);
		}

		alert508StateChanges();
	}
	
	private void alert508StateChanges() {
		StringBuilder helpTextBuilder = new StringBuilder();
		
		helpTextBuilder.append(build508HelpString(previousIsProgramAndReleaseListBoxesEnabled, areProgramAndReleaseListBoxesEnabled, "Program and Release List Boxes"));
		helpTextBuilder.append(build508HelpString(previousIsRetrieveButtonEnabled, isRetrieveButtonEnabled, "Retrieve Button"));
		helpTextBuilder.append(build508HelpString(previousIsApplyButtonEnabled, isApplyButtonEnabled, "Apply Button"));
		
		searchDisplay.getValueSetView().getHelpBlock().setText(helpTextBuilder.toString());
	}
	
	private String build508HelpString(boolean previousState, boolean currentState, String elementName) {
		
		String helpString = "";
		if(currentState != previousState) {
			helpString = elementName.concat(" ").concat(Boolean.TRUE.equals(currentState) ? "enabled" : "disabled");
		}
		
		return helpString; 
	}
	
	private void copyValueSets() {
		searchDisplay.resetMessageDisplay();
		if(searchDisplay.getValueSetView().getQdmSelectedList() != null &&
				searchDisplay.getValueSetView().getQdmSelectedList().size() > 0){
			mat.model.GlobalCopyPasteObject gbCopyPaste = new GlobalCopyPasteObject();
			gbCopyPaste.setCopiedValueSetList(searchDisplay.getValueSetView().getQdmSelectedList());
			MatContext.get().setGlobalCopyPaste(gbCopyPaste);
			searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().VALUE_SETS_COPIED_SUCCESSFULLY);
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
					MatContext.get().getMessageDelegate().getCOPY_QDM_SELECT_ATLEAST_ONE());
		}
	}
	
	private void selectAllValueSets() {
		searchDisplay.resetMessageDisplay();
		if(searchDisplay.getValueSetView().getAllValueSets() != null &&
				searchDisplay.getValueSetView().getAllValueSets().size() > 0){
			searchDisplay.getValueSetView().selectAll();
			searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(VALUE_SETS_SELECTED_SUCCESSFULLY);
			
		}
	}
	
	/**
	 * paste Value Sets. This functionality is to paste all the Value sets elements that have been copied
	 * from any Measure and can be pasted to any measure.
	 */
	private void pasteValueSets() {
		searchDisplay.resetMessageDisplay();
		GlobalCopyPasteObject gbCopyPaste = MatContext.get().getGlobalCopyPaste();
		showSearchingBusy(true);
		if ((gbCopyPaste != null) && (gbCopyPaste.getCopiedValueSetList().size() > 0)) {
			List<CQLValueSetTransferObject> cqlValueSetTransferObjectsList = searchDisplay.getValueSetView()
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
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
											.createAlert(MatContext.get().getMessageDelegate().getSUCCESSFULLY_VALUESET_PASTE());
								}
							}
						});
			} else {
				showSearchingBusy(false);
				searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFULLY_VALUESET_PASTE());
			}
			MatContext.get().getGlobalCopyPaste().getCopiedValueSetList().clear();
			;
		} else {
			showSearchingBusy(false);
			searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWARNING_PASTING_IN_VALUESET());
		}
		 
	}
	
	
	private void copyCodes() {
		searchDisplay.resetMessageDisplay();
		if(searchDisplay.getCodesView().getCodesSelectedList() != null && searchDisplay.getCodesView().getCodesSelectedList().size() > 0){
			mat.model.GlobalCopyPasteObject gbCopyPaste = new GlobalCopyPasteObject();
			gbCopyPaste.setCopiedCodeList(searchDisplay.getCodesView().getCodesSelectedList());
			MatContext.get().setGlobalCopyPaste(gbCopyPaste);
			searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().CODES_COPIED_SUCCESSFULLY);
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
					MatContext.get().getMessageDelegate().getCOPY_CODE_SELECT_ATLEAST_ONE());
		}
	}
	
	private void selectAllCodes() {
		searchDisplay.resetMessageDisplay();
		if(searchDisplay.getCodesView().getAllCodes() != null) {
			searchDisplay.getCodesView().selectAll();
			searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(CODES_SELECTED_SUCCESSFULLY);
		}
	}
	
	/**
	 * paste Value Sets. This functionality is to paste all the Value sets elements that have been copied
	 * from any Measure and can be pasted to any measure.
	 */
	private void pasteCodes() {
		searchDisplay.resetMessageDisplay();
		GlobalCopyPasteObject gbCopyPaste = MatContext.get().getGlobalCopyPaste();
		if( (gbCopyPaste != null) && (gbCopyPaste.getCopiedCodeList().size()>0) ){
			List<CQLCode> codesToPaste = searchDisplay.getCodesView().setMatCodeList(gbCopyPaste.getCopiedCodeList(), appliedCodeTableList);
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
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().
								getMessageDelegate().SUCCESSFULLY_PASTED_CODES_IN_MEASURE);
						searchDisplay.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);
						searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService().checkForEditPermission());
						searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						if (result != null && result.getCqlModel().getAllValueSetAndCodeList() != null) {
							setAppliedValueSetListInTable(result.getCqlModel().getAllValueSetAndCodeList());
						}
						showSearchingBusy(false);
					}
				});
			} else {
					showSearchingBusy(false);
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().SUCCESSFULLY_PASTED_CODES_IN_MEASURE);
			}
			MatContext.get().getGlobalCopyPaste().getCopiedCodeList().clear();
		} else {
			showSearchingBusy(false);
			searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().
					getMessageDelegate().CLIPBOARD_DOES_NOT_CONTAIN_CODES);
		}
	}

private void addCodeSearchPanelHandlers() {
		searchDisplay.getCodesView().getCopyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				copyCodes();
			}
		});
		
		searchDisplay.getCodesView().getSelectAllButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectAllCodes();
			}
		});

		searchDisplay.getCodesView().getPasteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getLibraryLockService().checkForEditPermission()) {
					pasteCodes();
				} else {
					event.preventDefault();
				}
			}
		});
		searchDisplay.getCodesView().getClearButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (!searchDisplay.getCodesView().getIsLoading()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCodesView().clearSelectedCheckBoxes();
				}
			}
		});
	
		searchDisplay.getCodesView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					if (!isCodeModified)
						searchCQLCodesInVsac();
					//508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeInput());
				}
			}
		});
		
		searchDisplay.getCodesView().getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					MatContext.get().clearDVIMessages();
					searchDisplay.resetMessageDisplay();
					if(isCodeModified && modifyCQLCode != null) {
						modifyCodes();
					} else if (null != searchDisplay.getCodesView().getCodeSearchInput().getValue() 
							&& !searchDisplay.getCodesView().getCodeSearchInput().getValue().isEmpty()) {
						addNewCodes();	
					}
					//508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
				}
				
			}
		});
		
		searchDisplay.getCodesView().getCancelCodeButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					isCodeModified = false;
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCodesView().resetCQLCodesSearchPanel();
					//508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
				}
			}
		});
		searchDisplay.getCodesView().setDelegator(new Delegator() {
			
			@Override
			public void onDeleteClicked(CQLCode result, int index) {
				if(!searchDisplay.getCodesView().getIsLoading()) {
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
					if(result != null){
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(result.getId());
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_CODES(result.getCodeOID()));
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
						//508 Compliance for Codes section
						searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeInput());
					}
				} else {
					// table is loading, do nothing
				}		
				
			}

			@Override
			public void onModifyClicked(CQLCode object) {
				if(!searchDisplay.getValueSetView().getIsLoading()){
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCodesView().resetCQLCodesSearchPanel();
					isCodeModified = true;
					modifyCQLCode = object;
					searchDisplay.getCodesView().setValidateCodeObject(modifyCQLCode);
					String displayName = object.getCodeOID();
					// Substring at 60th character length.
					if(displayName.length() >=60){
						displayName = displayName.substring(0, 59);
					}
					HTML searchHeaderText = new HTML("<strong>Modify Code ( "+displayName +")</strong>");
					searchDisplay.getCodesView().getSearchHeader().clear();
					searchDisplay.getCodesView().getSearchHeader().add(searchHeaderText);
					searchDisplay.getCodesView().getMainPanel().getElement().focus();
					
					onModifyCode(object);
					//508 Compliance for Value Sets section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeInput());
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
		String codeName = searchDisplay.getCodesView().getCodeDescriptorInput().getValue();
		codeName = StringUtility.removeEscapedCharsFromString(codeName);
		CQLCode refCode = buildCQLCodeFromCodesView(codeName);
	
		MatCodeTransferObject transferObject = searchDisplay.getCodesView().getCodeTransferObject(cqlLibraryId, refCode);
	
		if (null != transferObject) {
	
			appliedCodeTableList.removeIf(code -> code.getDisplayName().equals(modifyCQLCode.getDisplayName()));
	
			if(!searchDisplay.getCodesView().checkCodeInAppliedCodeTableList(refCode.getDisplayName(), appliedCodeTableList)) {
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
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().
								getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_CODE());
						searchDisplay.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);
						searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						if (result.getCqlModel().getAllValueSetAndCodeList() != null) {
							setAppliedValueSetListInTable(result.getCqlModel().getAllValueSetAndCodeList());
						}
						searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService().checkForEditPermission());
						//Temporary fix to update codes for insert Icon.
						getAppliedValueSetList();
						showSearchingBusy(false);
						searchDisplay.getCodesView().getSaveButton().setEnabled(false);
						isCodeModified = false;
						modifyCQLCode = null;
					}
				});
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(refCode.getDisplayName()));
			}
		}
	}

	/**
	 * Adds the new codes.
	 */
	private void addNewCodes() {
		String cqlLibraryId = MatContext.get().getCurrentCQLLibraryId();
		final String codeName = StringUtility.removeEscapedCharsFromString(searchDisplay.getCodesView().getCodeDescriptorInput().getValue());
		CQLCode refCode = buildCQLCodeFromCodesView(codeName);
		final String codeSystemName = refCode.getCodeSystemName();
		final String codeId = refCode.getCodeOID();

		MatCodeTransferObject transferObject = searchDisplay.getCodesView().getCodeTransferObject(cqlLibraryId, refCode);
		
		if (null != transferObject) {
			showSearchingBusy(true);
			cqlService.saveCQLCodestoCQLLibrary(transferObject, new AsyncCallback<SaveUpdateCQLResult>() {
				
				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					
					if(result.isSuccess()){
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().
								getMessageDelegate().getCodeSuccessMessage(searchDisplay.getCodesView().getCodeInput().getText()));
						searchDisplay.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getLibraryLockService()
								.checkForEditPermission());
						searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						//Temporary fix to update codes for insert Icon.
						getAppliedValueSetList();
					} else {
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
						if(result.getFailureReason()==result.getDuplicateCode()){
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().generateDuplicateErrorMessage(codeName));
						}
						
						else if(result.getFailureReason() ==  result.getBirthdateOrDeadError()) {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate()
									.getBirthdateOrDeadMessage(codeSystemName, codeId));
							
							searchDisplay.getCodesView().buildCodesCellTable(
									appliedCodeTableList,
									MatContext.get().getMeasureLockService().checkForEditPermission());
						}
					}
					showSearchingBusy(false);
					//508 : Shift focus to code search panel.
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
					searchDisplay.getCodesView().getSaveButton().setEnabled(!result.isSuccess());
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
		CQLCodesView codesView = searchDisplay.getCodesView();
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
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getVSAC_UPDATE_SUCCESSFULL());
						
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
							searchDisplay.getValueSetView().buildAppliedValueSetCellTable(appliedListModel,
									MatContext.get().getLibraryLockService().checkForEditPermission());
							searchDisplay.getCqlLeftNavBarPanelView().setAppliedQdmTableList(appliedValueSetTableList);
							searchDisplay.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
							
						} else {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
									searchDisplay.getValueSetView().convertMessage(result.getFailureReason()));
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
	private void searchValueSetInVsac(String expansionProfile) {
		showSearchingBusy(true);
		currentMatValueSet = null;
		final String oid = searchDisplay.getValueSetView().getOIDInput().getValue();
		if (!MatContext.get().isUMLSLoggedIn()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
			showSearchingBusy(false);
			return;
		}

		// OID validation.
		if ((oid == null) || oid.trim().isEmpty()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED());
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
			showSearchingBusy(false);
			return;
		}
		
		vsacapiService.getMostRecentValueSetByOID(oid, expansionProfile, new AsyncCallback<VsacApiResult>() {

			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
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
					searchDisplay.getValueSetView().getOIDInput().setTitle(oid);
					searchDisplay.getValueSetView().getUserDefinedInput()
							.setValue(currentMatValueSet.getDisplayName());
					searchDisplay.getValueSetView().getUserDefinedInput()
							.setTitle(currentMatValueSet.getDisplayName());

					searchDisplay.getValueSetView().getSaveButton().setEnabled(true);					
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getValuesetSuccessfulReterivalMessage(matValueSets.get(0).getDisplayName()));
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);

				} else {
					String message = searchDisplay.getValueSetView().convertMessage(result.getFailureReason());
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(message);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
				}
				showSearchingBusy(false);
			}
		});
	}
	
	
	/**
	 * Search CQL codes in vsac.
	 */
	private void searchCQLCodesInVsac() {

		final String url = searchDisplay.getCodesView().getCodeSearchInput().getValue().trim();
		searchDisplay.getCodesView().getCodeSearchInput().setText(url);
		
		if (!MatContext.get().isUMLSLoggedIn()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);

			return;
		}
		
		// Code Identifier validation.
		if ((url == null) || url.trim().isEmpty()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_CODE_IDENTIFIER_REQUIRED());
			
			return;
		}
		
		// Code Identifier validation to check Identifier starts with "CODE:"
		if(validator.validateForCodeIdentifier(url)){
			searchDisplay.getCodesView().getSaveButton().setEnabled(false);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
					MatContext.get().getMessageDelegate().getUMLS_INVALID_CODE_IDENTIFIER());
			
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
					searchDisplay.getCodesView().getCodeDescriptorInput().setValue(result.getDirectReferenceCode().getCodeDescriptor());
					searchDisplay.getCodesView().getCodeInput().setValue(result.getDirectReferenceCode().getCode());
					searchDisplay.getCodesView().getCodeSystemInput().setValue(result.getDirectReferenceCode().getCodeSystemName());
					searchDisplay.getCodesView().getCodeSystemVersionInput().setValue(result.getDirectReferenceCode().getCodeSystemVersion());
					searchDisplay.getCodesView().setCodeSystemOid(result.getDirectReferenceCode().getCodeSystemOid());
					searchDisplay.getCodesView().getSaveButton().setEnabled(true);
					
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert("Code "+result.getDirectReferenceCode().getCode()+" successfully retrieved from VSAC.");
					
					CQLCode code = buildCQLCodeFromCodesView(StringUtility.removeEscapedCharsFromString(searchDisplay.getCodesView().getCodeDescriptorInput().getValue()));
					searchDisplay.getCodesView().setValidateCodeObject(code);
					
				} else {
					String message = searchDisplay.getCodesView().convertMessage(result.getFailureReason());
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(message);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
				}
				
				showSearchingBusy(false);
				//508 : Shift focus to code search panel.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
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
			String originalName = searchDisplay.getValueSetView().getUserDefinedInput().getText();
			String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
			String displayName = (!originalName.isEmpty() ? originalName : "")  + (!suffix.isEmpty() ? " (" + suffix + ")" : "");

			if (modifyValueSetDTO.getVersion() == null) {
				modifyValueSetDTO.setVersion("");
			}
			
			String releaseValue = searchDisplay.getValueSetView().getReleaseListBox().getSelectedValue();
			if(!releaseValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
				modifyValueSetDTO.setRelease(releaseValue);
				modifyValueSetDTO.setVersion("");
			} else {
				modifyValueSetDTO.setRelease("");
			}
			
			String programValue = searchDisplay.getValueSetView().getProgramListBox().getSelectedValue();
			modifyValueSetDTO.setProgram(programValue);
			modifyValueSetList(modifyValueSetDTO);
			
			
			if (!searchDisplay.getValueSetView().checkNameInValueSetList(displayName,appliedValueSetTableList)) {

				if(!searchDisplay.getValueSetView().getSuffixInput().getValue().isEmpty()){
					modifyValueSetDTO.setSuffix(searchDisplay.getValueSetView().getSuffixInput().getValue());
					modifyValueSetDTO.setName(originalName+" ("+searchDisplay.getValueSetView().getSuffixInput().getValue()+")");
				} else {
					modifyValueSetDTO.setName(originalName);
					modifyValueSetDTO.setSuffix(null);
				}
				modifyValueSetDTO.setOriginalCodeListName(originalName);
				updateAppliedValueSetsList(modifyWithDTO, null, modifyValueSetDTO);
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(displayName));
				appliedValueSetTableList.add(modifyValueSetDTO);
			}
			getUsedArtifacts();
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE());
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
		matValueSetTransferObject.setUserDefinedText(searchDisplay.getValueSetView().getUserDefinedInput().getText());
		matValueSetTransferObject.scrubForMarkUp();
		showSearchingBusy(true);
		MatContext.get().getLibraryService().modifyCQLValueSets(matValueSetTransferObject,
				new AsyncCallback<SaveUpdateCQLResult>() {
					@Override
					public void onFailure(final Throwable caught) {
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
						isModified = false;
						modifyValueSetDTO = null;
						currentMatValueSet = null;
						searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
					}

					@Override
					public void onSuccess(final SaveUpdateCQLResult result) {
						if (result != null) {
							if (result.isSuccess()) {
								isModified = false;
								modifyValueSetDTO = null;
								currentMatValueSet = null;
								searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
								
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
										MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
								getAppliedValueSetList();
							} else {

								if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getName()));

								} else if (result
										.getFailureReason() == SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION) {
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
											.createAlert("Invalid Input data.");
								}
							}
						}
						searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
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
		String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
		final String codeListName = (originalCodeListName!=null ? originalCodeListName : "") + (!suffix.isEmpty() ? " (" + suffix + ")" : "");

		// Check if QDM name already exists in the list.
		if (!searchDisplay.getValueSetView().checkNameInValueSetList(codeListName,appliedValueSetTableList)) {
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
							searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
						}

						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							String message = "";
							if (result != null) {
								if (result.isSuccess()) {

									message = MatContext.get().getMessageDelegate()
											.getValuesetSuccessMessage(codeListName);
									MatContext.get().getEventBus().fireEvent(new QDSElementCreatedEvent(codeListName));
									searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
											.createAlert(message);
									
									previousIsProgramAndReleaseListBoxesEnabled = areProgramAndReleaseListBoxesEnabled;
									areProgramAndReleaseListBoxesEnabled = true;
									searchDisplay.getValueSetView().loadProgramsAndReleases();
									loadProgramsAndReleases(); 
									getAppliedValueSetList();
								} else {
									if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
												MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getName()));
									}
								}
							}
							getUsedArtifacts();
							currentMatValueSet = null;
							searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
						
							showSearchingBusy(false);
						}
					});
		}  else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
			.createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(codeListName));
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
				if (!searchDisplay.getValueSetView().checkNameInValueSetList(userDefinedInput,appliedValueSetTableList)) {
					showSearchingBusy(true);
					MatContext.get().getLibraryService().saveCQLUserDefinedValueset(matValueSetTransferObject,
							new AsyncCallback<SaveUpdateCQLResult>() {
								@Override
								public void onFailure(final Throwable caught) {
									Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
									showSearchingBusy(false);
									searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
								}

								@Override
								public void onSuccess(final SaveUpdateCQLResult result) {
									if (result != null) {
										if (result.isSuccess()) {
											if (result.getXml() != null) {

												String message = MatContext.get().getMessageDelegate()
														.getValuesetSuccessMessage(userDefinedInput);
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
														.createAlert(message);
												MatContext.get().setValuesets(result.getCqlAppliedQDMList());
												searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
												getAppliedValueSetList();
											}
										} else {
											if (result.getFailureReason() == SaveUpdateCQLResult.ALREADY_EXISTS) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
														.createAlert(MatContext.get().getMessageDelegate()
																.getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getName()));
											} else if (result.getFailureReason() == SaveUpdateCQLResult.SERVER_SIDE_VALIDATION) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
														.createAlert("Invalid input data.");
											}
										}
									}
									getUsedArtifacts();
									searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
									showSearchingBusy(false);
								}
							});

				}  else {
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(userDefinedInput));
				}
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(message);
			}

		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
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
		
		String originalCodeListName = searchDisplay.getValueSetView().getUserDefinedInput().getValue(); 
		
		matValueSetTransferObject.setCqlQualityDataSetDTO(new CQLQualityDataSetDTO());
		matValueSetTransferObject.getCqlQualityDataSetDTO().setOriginalCodeListName(originalCodeListName);
		
		if(!searchDisplay.getValueSetView().getSuffixInput().getValue().isEmpty()){
			matValueSetTransferObject.getCqlQualityDataSetDTO().setSuffix(searchDisplay.getValueSetView().getSuffixInput().getValue());
			matValueSetTransferObject.getCqlQualityDataSetDTO().setName(originalCodeListName+" ("+searchDisplay.getValueSetView().getSuffixInput().getValue()+")");
		} else {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setName(originalCodeListName);
		}
		
		// set it to empty string to begin with
		matValueSetTransferObject.getCqlQualityDataSetDTO().setRelease("");
		String releaseValue = searchDisplay.getValueSetView().getReleaseListBox().getSelectedValue(); 
		if(!releaseValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setRelease(releaseValue);
		}
		
		String programValue = searchDisplay.getValueSetView().getProgramListBox().getSelectedValue();
		matValueSetTransferObject.getCqlQualityDataSetDTO().setProgram(programValue);
		
		CodeListSearchDTO codeListSearchDTO = new CodeListSearchDTO();
		codeListSearchDTO.setName(searchDisplay.getValueSetView().getUserDefinedInput().getText());
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		matValueSetTransferObject.setMatValueSet(currentMatValueSet);
		matValueSetTransferObject.setCqlLibraryId(libraryID);
		matValueSetTransferObject.setUserDefinedText(searchDisplay.getValueSetView().getUserDefinedInput().getText());
		return matValueSetTransferObject;
	}

	/**
	 * Modify QDM with out value set.
	 */
	private void modifyUserDefinedValueSet() {
		modifyValueSetDTO.setVersion("");
		if ((searchDisplay.getValueSetView().getUserDefinedInput().getText().trim().length() > 0)) {
			String originalName = searchDisplay.getValueSetView().getUserDefinedInput().getText();
			String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
			String usrDefDisplayName = (!originalName.isEmpty() ? originalName : "") + (!suffix.isEmpty() ? " (" + suffix + ")" : ""); 

			modifyValueSetList(modifyValueSetDTO);
			if (!searchDisplay.getValueSetView().checkNameInValueSetList(usrDefDisplayName,appliedValueSetTableList)) {

				CQLValueSetTransferObject object = new CQLValueSetTransferObject();
				object.setUserDefinedText(searchDisplay.getValueSetView().getUserDefinedInput().getText());
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
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(message);
				}
			}  else {
				appliedValueSetTableList.add(modifyValueSetDTO);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(usrDefDisplayName));
			}
			getUsedArtifacts();
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
		}
	}

	
	/**
	 * Build View for General info when General Info AnchorList item is clicked.
	 */
	private void generalInfoEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		searchDisplay.hideInformationDropDown();
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();

		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			searchDisplay.buildGeneralInformation();
			boolean isValidQDMVersion = searchDisplay.getCqlLeftNavBarPanelView().checkForIncludedLibrariesQDMVersion();
			if(!isValidQDMVersion){
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_QDM_VERSION_IN_INCLUDES());
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
			}
			searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().setText(cqlLibraryName);
			searchDisplay.getCqlGeneralInformationView().getComments().setText(cqlLibraryComment);
		}
		searchDisplay.setGeneralInfoHeading();
		searchDisplay.getCqlGeneralInformationView().getComments().setCursorPos(0);
	}

	/**
	 * Applied QDM event.
	 */
	private void appliedQDMEvent() {
		// server
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.hideInformationDropDown();
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();

		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(true);
			searchDisplay.getValueSetView().getPasteButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
			currentSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			buildAppliedQDMTable();
		}		
		//On load of Value Sets page, set the Programs from VSAC 
		loadProgramsAndReleases();		
		searchDisplay.getValueSetView().setHeading("CQL Library Workspace > Value Sets", "subQDMAPPliedListContainerPanel");
		Mat.focusSkipLists("CqlComposer");
	}
	
	/**
	 * Builds the applied QDM table.
	 */
	private void buildAppliedQDMTable() {
		searchDisplay.buildAppliedQDM();
		boolean isEditable = MatContext.get().getLibraryLockService().checkForEditPermission();
		
		// initialize the valuesets to be used, getUsedArtifacts() will update with the proper value
		for(CQLQualityDataSetDTO valuset : searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList()) {
			valuset.setUsed(true);
		}
		
		searchDisplay.getValueSetView().buildAppliedValueSetCellTable(searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList(), isEditable);
		searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
		searchDisplay.getValueSetView().setWidgetsReadOnly(isEditable);
		getUsedArtifacts();
	}
	
	/**
	 * codes event.
	 */
	private void codesEvent() {
		// server
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_CODES;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();

		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_CODES;
			searchDisplay.buildCodes();
			searchDisplay.getCodesView().buildCodesCellTable(
					appliedCodeTableList,
					MatContext.get().getLibraryLockService().checkForEditPermission());
			searchDisplay.getCodesView().resetCQLCodesSearchPanel();
			searchDisplay.getCodesView()
					.setWidgetsReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
			getUsedCodes();
			boolean isEnabled = MatContext.get().getLibraryLockService().checkForEditPermission();
			searchDisplay.getCodesView().getPasteButton().setEnabled(isEnabled);
		}
		searchDisplay.getCodesView().setHeading("CQL Library Workspace > Codes", "codesContainerPanel");
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
		               	       
		               	if(searchDisplay.getCqlLeftNavBarPanelView().getAppliedCodeTableList().size() > 0) {
		                   	searchDisplay.getCodesView().getCelltable().redraw();
		                   	searchDisplay.getCodesView().getListDataProvider().refresh();
		               	}
		               }
		               
		        });
	}

	/**
	 * Build View for Parameter when Parameter AnchorList item is clicked.
	 */
	private void parameterEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.hideInformationDropDown();
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
		searchDisplay.buildParameterLibraryView();

		searchDisplay.getCQLParametersView()
				.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		
		searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
		searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getParameterButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = searchDisplay.getCQLParametersView().getParameterAceEditor();
		searchDisplay.getCQLParametersView().setHeading("CQL Library Workspace > Parameter", "mainParamViewVerticalPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Build View for Includes when Includes AnchorList item is clicked.
	 */
	private void includesEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
		searchDisplay.getMainFlowPanel().clear();
		searchDisplay.hideInformationDropDown();
		searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
				.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
		searchDisplay.buildIncludesView();
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(new ArrayList<CQLLibraryDataSetObject>());
		searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,
				MatContext.get().getLibraryLockService().checkForEditPermission(), true);
		
		searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
		searchDisplay.getIncludeView().getSearchTextBox().setText("");
		searchDisplay.getCqlIncludeLibraryView()
				.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		searchDisplay.getIncludeView().setHeading("CQL Library Workspace > Includes", "IncludeSectionContainerPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Build View for Definition when Definition AnchorList item is clicked.
	 */
	private void definitionEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.hideInformationDropDown();
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();

		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
		searchDisplay.buildDefinitionLibraryView();

		searchDisplay.getCQLDefinitionsView()
				.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
	
		searchDisplay.getCQLDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
		searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getDefineButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = searchDisplay.getCQLDefinitionsView().getDefineAceEditor();
		searchDisplay.getCQLDefinitionsView().setHeading("CQL Library Workspace > Definition", "mainDefViewVerticalPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Build View for Function when Funtion AnchorList item is clicked.
	 */
	private void functionEvent() {
		searchDisplay.hideInformationDropDown();
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		unsetActiveMenuItem(currentSection);
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
		searchDisplay.buildFunctionLibraryView();
		searchDisplay.getCQLFunctionsView()
				.setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		
		searchDisplay.getCQLFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(MatContext.get().getLibraryLockService().checkForEditPermission());
		searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getFunctionButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = searchDisplay.getCQLFunctionsView().getFunctionBodyAceEditor();
		searchDisplay.getCQLFunctionsView().setHeading("CQL Library Workspace > Function", "mainFuncViewVerticalPanel");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Build View for View Cql when View Cql AnchorList item is clicked.
	 */
	private void viewCqlEvent() {
		searchDisplay.hideInformationDropDown();
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			searchDisplay.buildCQLFileView(MatContext.get().getLibraryLockService().checkForEditPermission());
			buildCQLView();
		}
		searchDisplay.getViewCQLView().setHeading("CQL Library Workspace > View CQL", "cqlViewCQL_Id");
		Mat.focusSkipLists("CqlComposer");
	}

	/**
	 * Method to build View for Anchor List item View CQL.
	 */
	private void buildCQLView() {
		searchDisplay.getCqlAceEditor().setText("");
		showSearchingBusy(true);
		MatContext.get().getCQLLibraryService().getCQLLibraryFileData(MatContext.get().getCurrentCQLLibraryId(),
				new AsyncCallback<SaveUpdateCQLResult>() {
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							if ((result.getCqlString() != null) && !result.getCqlString().isEmpty()) {
								
								searchDisplay.getCqlAceEditor().clearAnnotations();
								searchDisplay.getCqlAceEditor().removeAllMarkers();
								searchDisplay.getCqlAceEditor().redisplay();
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
								searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
								searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationMessageAlert().clearAlert();

								if (!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getVIEW_CQL_ERROR_MESSAGE());
									for (CQLErrors error : result.getCqlErrors()) {
										String errorMessage = new String();
										errorMessage = errorMessage.concat("Error in line : " + error.getErrorInLine()
												+ " at Offset :" + error.getErrorAtOffeset());
										int line = error.getErrorInLine();
										int column = error.getErrorAtOffeset();
										searchDisplay.getCqlAceEditor().addAnnotation(line - 1, column,
												error.getErrorMessage(), AceAnnotationType.ERROR);
									}
									searchDisplay.getCqlAceEditor().setText(result.getCqlString());
									searchDisplay.getCqlAceEditor().setAnnotations();
									searchDisplay.getCqlAceEditor().redisplay();
								} else if (!result.isDatatypeUsedCorrectly()) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
											.createAlert(MatContext.get().getMessageDelegate()
													.getVIEW_CQL_ERROR_MESSAGE_BAD_VALUESET_DATATYPE());
									searchDisplay.getCqlAceEditor().setText(result.getCqlString());
								} else {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getVIEW_CQL_NO_ERRORS_MESSAGE());
									searchDisplay.getCqlAceEditor().setText(result.getCqlString());
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
		if (!searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			searchDisplay.resetMessageDisplay();
			if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_GENERAL_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(false);
				searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().setSelectedIndex(-1);
				if (searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
							.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
				searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().setSelectedIndex(-1);
				if (searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
							.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(false);
				searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().setSelectedIndex(-1);
				if (searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
							.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEW_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_APPLIED_QDM)) {
				searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(false);
			}else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_CODES)) {
				searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_INCLUDES_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(false);
				searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().setSelectedIndex(-1);
				if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
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
		searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
		searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
		searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
		showSearchingBusy(true);

		MatContext.get().getCQLLibraryService().searchForIncludes(setId, searchText, false,
				new AsyncCallback<SaveCQLLibraryResult>() {

					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(SaveCQLLibraryResult result) {
						showSearchingBusy(false);
						if (result != null && result.getCqlLibraryDataSetObjects().size() > 0) {
							searchDisplay.getCqlLeftNavBarPanelView()
									.setIncludeLibraryList(result.getCqlLibraryDataSetObjects());
							searchDisplay.buildIncludesView();
							searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,
									MatContext.get().getLibraryLockService().checkForEditPermission(), false);

						} else {
							searchDisplay.buildIncludesView();
							searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,
									MatContext.get().getLibraryLockService().checkForEditPermission(), false);
							if (!searchDisplay.getIncludeView().getSearchTextBox().getText().isEmpty())
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert(MatContext.get().getMessageDelegate().getNoIncludes());
						}

						if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
								.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
							searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
						} else {
							searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
						}
						// 508 changes for Library Alias.
						searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
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
				searchDisplay.getCqlGeneralInformationView().setWidgetReadOnlyForCQLLibrary(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
				searchDisplay.getIncludeView().setReadOnly(!busy);				
			break;
			case(CQLWorkSpaceConstants.CQL_APPLIED_QDM): 
				searchDisplay.getValueSetView().setReadOnly(!busy);				
			break;
			case(CQLWorkSpaceConstants.CQL_CODES): 
				searchDisplay.getCodesView().setReadOnly(!busy);				
			break;
			case(CQLWorkSpaceConstants.CQL_PARAMETER_MENU): 
				searchDisplay.getCQLParametersView().setReadOnly(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_DEFINE_MENU): 
				searchDisplay.getCQLDefinitionsView().setReadOnly(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_FUNCTION_MENU): 
				searchDisplay.getCQLFunctionsView().setReadOnly(!busy);
			break;															  
			}
			
		}
		searchDisplay.getCqlLeftNavBarPanelView().setIsLoading(busy);
	}
	
	private void showSearchBusyOnDoubleClick(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setEnabled(!busy);
	}

	/**
	 * This method Clears alias view on Erase Button click when isPageDirty is
	 * not set.
	 */
	private void clearAlias() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getIncludeView().getAliasNameTxtArea() != null)) {
			searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
		}
		if ((searchDisplay.getIncludeView().getViewCQLEditor().getText() != null)) {
			searchDisplay.getIncludeView().getViewCQLEditor().setText("");
		}
		// Below lines are to clear Library search text box.
		if ((searchDisplay.getIncludeView().getSearchTextBox().getText() != null)) {
			searchDisplay.getIncludeView().getSearchTextBox().setText("");
		}
		searchDisplay.getIncludeView().getSelectedObjectList().clear();
		searchDisplay.getIncludeView().setSelectedObject(null);
		searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
				.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
		unCheckAvailableLibraryCheckBox();

		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(), false);
		}

	}

	/**
	 * Un check available library check box.
	 */
	private void unCheckAvailableLibraryCheckBox() {
		List<CQLLibraryDataSetObject> availableLibraries = new ArrayList<CQLLibraryDataSetObject>();
		availableLibraries = searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryList();
		for (int i = 0; i < availableLibraries.size(); i++) {
			availableLibraries.get(i).setSelected(false);
		}
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryList());
		searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,
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

		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
		switch (currentSection) {
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().fireEvent(new DoubleClickEvent() {
			});
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().fireEvent(new DoubleClickEvent() {
			});
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().fireEvent(new DoubleClickEvent() {
			});
			break;
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().fireEvent(new DoubleClickEvent() {
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
		searchDisplay.hideInformationDropDown();
		// Unset current selected section.
		switch (currentSection) {
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_APPLIED_QDM):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_CODES):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_VIEW_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setActive(false);
			break;
		default:
			break;
		}
		// Set Next Selected Section.
		switch (nextSection) {
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			currentSection = nextSection;
			includesEvent();
			searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
					.setClassName("panel-collapse collapse in");
			break;
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			currentSection = nextSection;
			functionEvent();
			searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
					.setClassName("panel-collapse collapse in");
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			currentSection = nextSection;
			parameterEvent();
			searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
					.setClassName("panel-collapse collapse in");
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			currentSection = nextSection;
			definitionEvent();
			searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
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
						searchDisplay.getCqlLeftNavBarPanelView().setAvailableQDSAttributeList(result);
						AddFunctionArgumentDialogBox.showArgumentDialogBox(functionArg, true,
								searchDisplay.getCQLFunctionsView(), searchDisplay.getCqlLeftNavBarPanelView(),
								MatContext.get().getLibraryLockService().checkForEditPermission());

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
	public static ViewDisplay getSearchDisplay() {
		return searchDisplay;
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
		searchDisplay.getValueSetView().buildAppliedValueSetCellTable(appliedValueSetTableList,
				MatContext.get().getLibraryLockService().checkForEditPermission());
		searchDisplay.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
	}

	/**
	 * MAT-8977. 
	 * Get the program and releases from VSAC using REST calls and set it in the MatContext 
	 * the first time the value sets page is loaded.
	 * If the values have been loaded previously, no calls are made.
	 */
	private void getProgramsAndReleases() {

		HashMap<String, List<String>> pgmRelMap = (HashMap<String, List<String>>) MatContext.get().getProgramToReleases();

		if (pgmRelMap == null || pgmRelMap.isEmpty()) {
			MatContext.get().getProgramsAndReleasesFromVSAC();	
		}				
	}
	
	

	private void loadProgramsAndReleases() {
		searchDisplay.getValueSetView().loadProgramsAndReleases();
	}

	private boolean isValidExpressionName(String expressionName) {
		final String trimedExpression = expressionName.trim();
		return !trimedExpression.isEmpty() && !trimedExpression.equalsIgnoreCase("Patient") && !trimedExpression.equalsIgnoreCase("Population")
				&& MatContext.get().getCqlConstantContainer() != null 
				&& MatContext.get().getCqlConstantContainer().getCqlKeywordList() != null
				&& MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlKeywordsList() != null
				&& !MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlKeywordsList().stream().anyMatch(definedKeyWord -> definedKeyWord.equalsIgnoreCase(trimedExpression));
	}
}