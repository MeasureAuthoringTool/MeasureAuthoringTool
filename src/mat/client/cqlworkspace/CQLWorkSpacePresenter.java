package mat.client.cqlworkspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.gwtbootstrap3.client.shared.event.HideEvent;
import org.gwtbootstrap3.client.shared.event.HideHandler;
import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.ListBox;
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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.event.QDSElementCreatedEvent;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.cqlworkspace.codes.CQLCodesView;
import mat.client.cqlworkspace.codes.CQLCodesView.Delegator;
import mat.client.cqlworkspace.components.CQLComponentLibraryView;
import mat.client.cqlworkspace.definitions.CQLDefinitionsView;
import mat.client.cqlworkspace.functions.CQLFunctionsView;
import mat.client.cqlworkspace.functions.CQLFunctionsView.Observer;
import mat.client.cqlworkspace.generalinformation.CQLGeneralInformationView;
import mat.client.cqlworkspace.includedlibrary.CQLIncludeLibraryView;
import mat.client.cqlworkspace.parameters.CQLParametersView;
import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility;
import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetView;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.JSONAttributeModeUtility;
import mat.client.shared.JSONCQLTimingExpressionUtility;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.ValueSetNameInputValidator;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.CQLValueSetTransferObject;
import mat.model.CodeListSearchDTO;
import mat.model.ComponentMeasureTabObject;
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
import mat.shared.cql.error.InvalidLibraryException;

public class CQLWorkSpacePresenter implements MatPresenter {
	
	private static final String CODES_SELECTED_SUCCESSFULLY = "All codes successfully selected.";
	
	private static final String VALUE_SETS_SELECTED_SUCCESSFULLY = "All value sets successfully selected.";

	private SimplePanel panel = new SimplePanel();

	private static String currentSection = "general";

	private static String nextSection = "general";

	private QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);

	private static ViewDisplay searchDisplay;

	private MeasureServiceAsync service = MatContext.get().getMeasureService();

	private CQLQualityDataSetDTO modifyValueSetDTO;

	private CQLCode modifyCQLCode;

	CQLModelValidator validator = new CQLModelValidator();

	private final VSACAPIServiceAsync vsacapiService = MatContext.get().getVsacapiServiceAsync();

	private boolean isUserDefined = false;

	private String setId = null;

	private String currentIncludeLibrarySetId = null;

	private String currentIncludeLibraryId = null;

	private String cqlLibraryComment;

	private boolean isModified = false;
	private boolean isCodeModified = false;

	private MatValueSet currentMatValueSet = null;

	private List<CQLQualityDataSetDTO> appliedValueSetTableList = new ArrayList<CQLQualityDataSetDTO>();

	private List<CQLCode> appliedCodeTableList = new ArrayList<CQLCode>();

	private AceEditor curAceEditor;

	/**
	 * Flag for if parameters, definitions, or functions should be formatted. For
	 * now this flag will always be set to true.
	 */
	private boolean isFormatable = true;
	
	
	private boolean isProgramReleaseBoxEnabled = true; 
	private boolean isRetrieveButtonEnabled = true; 
	private boolean isApplyButtonEnabled = false; 
	
	private boolean previousIsProgramReleaseBoxEnabled = true; 
	private boolean previousIsRetrieveButtonEnabled = true; 
	private boolean previousIsApplyButtonEnabled = false; 

	public static interface ViewDisplay {

		VerticalPanel getMainPanel();

		void buildView();

		void buildGeneralInformation();

		void buildIncludesView();

		void buildAppliedQDM();

		void buildParameterLibraryView();

		void buildDefinitionLibraryView();

		void buildFunctionLibraryView();

		void buildCQLFileView(boolean isEditable);

		Widget asWidget();

		String getClickedMenu();

		void setClickedMenu(String clickedMenu);

		HorizontalPanel getMainHPanel();

		void setNextClickedMenu(String nextClickedMenu);

		Object getNextClickedMenu();

		void hideAceEditorAutoCompletePopUp();

		FlowPanel getMainFlowPanel();

		CQLLeftNavBarPanelView getCqlLeftNavBarPanelView();

		CQLGeneralInformationView getCqlGeneralInformationView();
		
		CQLComponentLibraryView getComponentView();

		CQLIncludeLibraryView getIncludeView();

		CQLAppliedValueSetView getValueSetView();

		CQLParametersView getCQLParametersView();

		CQLDefinitionsView getCQlDefinitionsView();

		CQLFunctionsView getCqlFunctionsView();

		CQLView getViewCQLView();

		void resetMessageDisplay();

		void resetAll();

		CQLCodesView getCodesView();

		void buildCodes();

		void hideInformationDropDown();

		void setGeneralInfoHeading();
		
		HelpBlock getHelpBlock();

		void buildComponentsView();

	}

	public CQLWorkSpacePresenter(final ViewDisplay srchDisplay) {
		searchDisplay = srchDisplay;
		addEventHandlers();
		JSONCQLTimingExpressionUtility.getAllCQLTimingExpressionsList();
		JSONAttributeModeUtility.getAllAttrModeList();
		JSONAttributeModeUtility.getAllModeDetailsList();
	}
	
	private void buildInsertPopUp() {
		searchDisplay.resetMessageDisplay();
		InsertIntoAceEditorDialogBox
				.showListOfItemAvailableForInsertDialogBox(searchDisplay.getCqlLeftNavBarPanelView(), curAceEditor);
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
	}

	/**
	 * Adds the event handlers.
	 */
	private void addEventHandlers() {

		ClickHandler cHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCQLParametersView().getParameterAceEditor().detach();
				searchDisplay.getCQlDefinitionsView().getDefineAceEditor().detach();
				searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().detach();
			}
		};
		searchDisplay.getMainPanel().addDomHandler(cHandler, ClickEvent.getType());

		addWarningConfirmationHandlers();
		addDeleteConfirmationHandlers();
		addEventHandlerOnAceEditors();
		addEventHandlersOnContextRadioButtons();
		addGeneralInfoEventHandlers();
		addIncludeCQLLibraryHandlers();
		addValueSetSearchPanelHandlers();
		addCodeSearchPanelHandlers();
		addParameterSectionHandlers();
		addDefinitionSectionHandlers();
		addFunctionSectionHandlers();
		addViewCQLEventHandlers();
		addListBoxEventHandler();
	}
	

	private void addViewCQLEventHandlers() {
		searchDisplay.getViewCQLView().getExportErrorFile().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
					String url = GWT.getModuleBaseURL() + "export?id=" + MatContext.get().getCurrentMeasureId() + "&format=errorFileMeasure";
					Window.open(url + "&type=save", "_self", "");
				}
			}
		});
		
	}

	/**
	 * Adds the delete confirmation handlers.
	 */
	private void addDeleteConfirmationHandlers() {

		searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBoxNoButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						searchDisplay.resetMessageDisplay();
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
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
								&& searchDisplay.getCqlLeftNavBarPanelView()
										.getCurrentSelectedFunctionArgumentObjId() == null) {
							deleteFunction();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (searchDisplay.getCqlLeftNavBarPanelView()
								.getCurrentSelectedFunctionArgumentObjId() != null) {
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
						} else if (searchDisplay.getCqlLeftNavBarPanelView()
								.getCurrentSelectedValueSetObjId() != null) {
							checkAndDeleteValueSet();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedCodesObjId() != null) {
							deleteCode();
							searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						}
					}
				});
	}


	private void addWarningConfirmationHandlers() {

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


	private void addParameterSectionHandlers() {

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
				resetAceEditor(searchDisplay.getCQLParametersView().getViewCQLAceEditor());
				searchDisplay.getCQLParametersView().getParameterButtonBar().getInfoButtonGroup().getElement()
						.setAttribute("class", "btn-group");

			}
		});

		searchDisplay.getCQLParametersView().getParameterButtonBar().getSaveButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
						if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
							addAndModifyParameters();
							// 508 change to parameter section
							searchDisplay.getCqlLeftNavBarPanelView()
									.setFocus(searchDisplay.getCQLParametersView().getParameterNameTxtArea());
						}
					}

				});

		searchDisplay.getCQLParametersView().getParameterButtonBar().getEraseButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						searchDisplay.resetMessageDisplay();
						searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
						searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
						resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
						eraseParameter();
						// 508 change to parameter section
						searchDisplay.getCqlLeftNavBarPanelView()
								.setFocus(searchDisplay.getCQLParametersView().getParameterAceEditor());
					}
				});
		searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_PARAMETER(
										searchDisplay.getCQLParametersView().getParameterNameTxtArea().getValue()));
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();

					}

				});

		searchDisplay.getCQLParametersView().getParameterNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		searchDisplay.getCQLParametersView().getParameterCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
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
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					addNewParameter();
				}
				// 508 change to parameter section
				searchDisplay.getCqlLeftNavBarPanelView()
						.setFocus(searchDisplay.getCQLParametersView().getParameterNameTxtArea());
			}
		});

		searchDisplay.getCQLParametersView().getParameterCommentTextArea().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.NONE);
				String comment = searchDisplay.getCQLParametersView().getParameterCommentTextArea().getText();
				if (validator.validateForCommentTextArea(comment)) {
					searchDisplay.getCQLParametersView().getParamCommentGroup()
							.setValidationState(ValidationState.ERROR);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});
	}

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

		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			searchDisplay.getCQLParametersView()
					.setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
			searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(), false);
		}

		searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
	}


	private void addFunctionSectionHandlers() {

		searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {

			@Override
			public void onShow(ShowEvent showEvent) {
				searchDisplay.resetMessageDisplay();
				showCompleteCQL(searchDisplay.getCqlFunctionsView().getViewCQLAceEditor());
			}
		});

		searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent hideEvent) {
				resetAceEditor(searchDisplay.getCqlFunctionsView().getViewCQLAceEditor());
				searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement()
						.setAttribute("class", "btn-group");

			}
		});

		// Save functionality for Function section
		searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse());

				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addAndModifyFunction();
					// 508 changes for Functions Section
					searchDisplay.getCqlLeftNavBarPanelView()
							.setFocus(searchDisplay.getCqlFunctionsView().getFuncNameTxtArea());
				}

			}
		});

		// Erase functionality for Function Section
		searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				resetViewCQLCollapsiblePanel(searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse());

				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				eraseFunction();
				// 508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView()
						.setFocus(searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor());
			}
		});

		// Insert into Ace Editor functionality for Function Section
		searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getInsertButton()
				.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						buildInsertPopUp();
					}
				});

		// Delete functionality for Function Section
		searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						resetViewCQLCollapsiblePanel(searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse());
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
								.createMultiLineAlert(
										MatContext.get().getMessageDelegate().getDeleteConfirmationFunctionCQLWorkspace(
												searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().getValue()));
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
					}
				});

		// Creating Arguments for Functions in Function Section
		searchDisplay.getCqlFunctionsView().getAddNewArgument().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.hideAceEditorAutoCompletePopUp();
				resetViewCQLCollapsiblePanel(searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse());
				searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement()
						.setAttribute("class", "btn-group");
				CQLFunctionArgument addNewFunctionArgument = new CQLFunctionArgument();
				AddFunctionArgumentDialogBox.showArgumentDialogBox(addNewFunctionArgument, false,
						searchDisplay.getCqlFunctionsView(), searchDisplay.getCqlLeftNavBarPanelView(),
						MatContext.get().getMeasureLockService().checkForEditPermission());
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				// 508 changes for function section
				searchDisplay.getCqlFunctionsView().getAddNewArgument().setFocus(true);
			}
		});

		searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		searchDisplay.getCqlFunctionsView().getFunctionCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		// Function Add New Functionality
		searchDisplay.getCqlFunctionsView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				resetViewCQLCollapsiblePanel(searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse());
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					addNewFunction();
				}
				// 508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView()
						.setFocus(searchDisplay.getCqlFunctionsView().getFuncNameTxtArea());
			}
		});

		searchDisplay.getCqlFunctionsView().getFunctionCommentTextArea().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.NONE);
				String comment = searchDisplay.getCqlFunctionsView().getFunctionCommentTextArea().getText();
				if (validator.validateForCommentTextArea(comment)) {
					searchDisplay.getCqlFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});

		addFunctionSectionObserverHandlers();

	}

	private void addNewFunction() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		searchDisplay.getCqlFunctionsView().getFunctionArgumentList().clear();
		searchDisplay.getCqlFunctionsView().getFunctionArgNameMap().clear();
		searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
		searchDisplay.getCqlFunctionsView().createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>(),
				MatContext.get().getMeasureLockService().checkForEditPermission());
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().setText("");
		}
		if ((searchDisplay.getCqlFunctionsView().getFuncNameTxtArea() != null)) {
			searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText("");
		}

		if ((searchDisplay.getCqlFunctionsView().getFunctionCommentTextArea() != null)) {
			searchDisplay.getCqlFunctionsView().getFunctionCommentTextArea().setText("");
		}
		searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex(), false);
		}		
		searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setValue(true);
		searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
		searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getCqlFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
	}

	/**
	 * Adds the function section observer handlers.
	 */
	private void addFunctionSectionObserverHandlers() {

		searchDisplay.getCqlFunctionsView().setObserver(new Observer() {

			// Modify functionality for Function Arguments
			@Override
			public void onModifyClicked(CQLFunctionArgument result) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				searchDisplay.resetMessageDisplay();
				if (result.getArgumentType().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
					getAttributesForDataType(result);
				} else {
					AddFunctionArgumentDialogBox.showArgumentDialogBox(result, true,
							searchDisplay.getCqlFunctionsView(), searchDisplay.getCqlLeftNavBarPanelView(),
							MatContext.get().getMeasureLockService().checkForEditPermission());
				}

			}

			// Delete functionality for Function Arguments
			@Override
			public void onDeleteClicked(CQLFunctionArgument result, int index) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(result.getId());
				searchDisplay.getCqlLeftNavBarPanelView()
						.setCurrentSelectedFunctionArgumentName(result.getArgumentName());
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate()
								.getDELETE_CONFIRMATION_FUNCTION_ARGUMENT(result.getArgumentName()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
			}
		});

	}


	private void addDefinitionSectionHandlers() {

		searchDisplay.getCQlDefinitionsView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {

			@Override
			public void onShow(ShowEvent showEvent) {
				searchDisplay.resetMessageDisplay();
				showCompleteCQL(searchDisplay.getCQlDefinitionsView().getViewCQLAceEditor());
			}
		});

		searchDisplay.getCQlDefinitionsView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent hideEvent) {
				searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getInfoButtonGroup().getElement()
						.setAttribute("class", "btn-group");
				resetAceEditor(searchDisplay.getCQlDefinitionsView().getViewCQLAceEditor());

			}

		});

		// Definition section Save functionality
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQlDefinitionsView().getPanelViewCQLCollapse());

				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addAndModifyDefintions();
					// 508 changes for Definitions Section
					searchDisplay.getCqlLeftNavBarPanelView()
							.setFocus(searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea());
				}
			}
		});

		// Definition section Insert into Ace Editor functionality
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getInsertButton()
				.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						buildInsertPopUp();
					}
				});

		// Definition section Erase functionality
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				resetViewCQLCollapsiblePanel(searchDisplay.getCQlDefinitionsView().getPanelViewCQLCollapse());

				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				eraseDefinition();
				// 508 changes for Definition Section
				searchDisplay.getCqlLeftNavBarPanelView()
						.setFocus(searchDisplay.getCQlDefinitionsView().getDefineAceEditor());
			}
		});
		// Definition Delete Icon Functionality
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						resetViewCQLCollapsiblePanel(searchDisplay.getCQlDefinitionsView().getPanelViewCQLCollapse());
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
								.createMultiLineAlert(MatContext.get().getMessageDelegate()
										.getDeleteConfirmationDefinitionCQLWorkspace(searchDisplay
												.getCQlDefinitionsView().getDefineNameTxtArea().getValue()));
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();

					}
				});

		searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		searchDisplay.getCQlDefinitionsView().getDefineCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		// Definition Add New Functionality
		searchDisplay.getCQlDefinitionsView().getAddNewButtonBar().getaddNewButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						searchDisplay.resetMessageDisplay();
						resetViewCQLCollapsiblePanel(searchDisplay.getCQlDefinitionsView().getPanelViewCQLCollapse());

						searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
						searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
						if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
							searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
						} else {
							addNewDefinition();
						}
						// 508 changes for Definitions Section
						searchDisplay.getCqlLeftNavBarPanelView()
								.setFocus(searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea());
					}
				});

		searchDisplay.getCQlDefinitionsView().getDefineCommentTextArea().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCQlDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.NONE);
				String comment = searchDisplay.getCQlDefinitionsView().getDefineCommentTextArea().getText();
				if (validator.validateForCommentTextArea(comment)) {
					searchDisplay.getCQlDefinitionsView().getDefineCommentGroup()
							.setValidationState(ValidationState.ERROR);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});
	}

	private void addNewDefinition() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		searchDisplay.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getCQlDefinitionsView().getDefineAceEditor().getText() != null)) {
			searchDisplay.getCQlDefinitionsView().getDefineAceEditor().setText("");
		}
		if ((searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea() != null)) {
			searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText("");
		}

		if ((searchDisplay.getCQlDefinitionsView().getDefineCommentTextArea() != null)) {
			searchDisplay.getCQlDefinitionsView().getDefineCommentTextArea().setText("");
		}
		searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText("");
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex(), false);
		}

		// Functionality to reset the disabled features for supplemental data
		// definitions when erased.
		searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setEnabled(true);
		searchDisplay.getCQlDefinitionsView().getDefineAceEditor().setReadOnly(false);
		searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setEnabled(true);
		searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setEnabled(true);
		searchDisplay.getCQlDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getSaveButton().setEnabled(true);
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getInsertButton().setEnabled(true);
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getTimingExpButton().setEnabled(true);
		searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
		searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);

	}

	/**
	 * Adds the list box event handler.
	 */
	private void addListBoxEventHandler() {
		searchDisplay.getCqlLeftNavBarPanelView().getComponentsListBox().addDoubleClickHandler(new DoubleClickHandler() {
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
						int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getComponentsListBox().getSelectedIndex();
						if (selectedIndex != -1) {
							final String selectedComponentObjectId = searchDisplay.getCqlLeftNavBarPanelView().getComponentsListBox().getValue(selectedIndex);
							if (searchDisplay.getCqlLeftNavBarPanelView().getComponentMap().get(selectedComponentObjectId) != null) {
								searchDisplay.getComponentView().setPageInformation(searchDisplay.getCqlLeftNavBarPanelView().getComponentMap().get(selectedComponentObjectId));
							}
						}
						searchDisplay.resetMessageDisplay();
					}
				}
			}
		});

		// Double Click Handler Event for Parameter Name ListBox in Parameter Section
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

										searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton()
												.setEnabled(MatContext.get().getMeasureLockService()
														.checkForEditPermission());

										// load most recent used cql artifacts
										MatContext.get().getMeasureService().getUsedCQLArtifacts(
												MatContext.get().getCurrentMeasureId(),
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
														searchDisplay.getCQLParametersView().getParameterNameTxtArea()
																.setText(searchDisplay.getCqlLeftNavBarPanelView()
																		.getParameterMap().get(selectedParamID)
																		.getName());
														searchDisplay.getCQLParametersView().getParameterAceEditor()
																.setText(searchDisplay.getCqlLeftNavBarPanelView()
																		.getParameterMap().get(selectedParamID)
																		.getLogic());
														searchDisplay.getCQLParametersView()
																.getParameterCommentTextArea()
																.setText(searchDisplay.getCqlLeftNavBarPanelView()
																		.getParameterMap().get(selectedParamID)
																		.getCommentString());
														CQLParameter currentParameter = searchDisplay
																.getCqlLeftNavBarPanelView().getParameterMap()
																.get(selectedParamID);
														boolean isReadOnly = searchDisplay.getCqlLeftNavBarPanelView()
																.getParameterMap().get(selectedParamID).isReadOnly();

														if (MatContext.get().getMeasureLockService()
																.checkForEditPermission()) {
															searchDisplay.getCQLParametersView()
																	.setWidgetReadOnly(!isReadOnly);

														if (!currentParameter.isReadOnly()) {
																if (!result.getCqlErrors().isEmpty()
																		|| !result.getUsedCQLParameters().contains(
																				currentParameter.getName())) {
																	searchDisplay.getCQLParametersView()
																			.getParameterButtonBar().getDeleteButton()
																			.setEnabled(true);

																} else {
																	searchDisplay.getCQLParametersView()
																			.getParameterButtonBar().getDeleteButton()
																			.setEnabled(false);
																}
															}
														}
														Map<String, List<CQLErrors>> expressionCQLErrorMap = result
																.getCqlErrorsPerExpression();
														if (expressionCQLErrorMap != null) {
															List<CQLErrors> errorList = expressionCQLErrorMap
																	.get(currentParameter.getName());
															searchDisplay.getCQLParametersView().getParameterAceEditor()
																	.clearAnnotations();
															searchDisplay.getCQLParametersView().getParameterAceEditor()
																	.removeAllMarkers();
															for (CQLErrors error : errorList) {
																int startLine = error.getStartErrorInLine();
																int startColumn = error.getStartErrorAtOffset();
																searchDisplay.getCQLParametersView()
																		.getParameterAceEditor()
																		.addAnnotation(startLine, startColumn,
																				error.getErrorMessage(),
																				AceAnnotationType.ERROR);
															}
															searchDisplay.getCQLParametersView().getParameterAceEditor()
																	.setAnnotations();
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

		// Double Click Handler Event for Definition Name ListBox in Defintion Section
		searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox()
				.addDoubleClickHandler(new DoubleClickHandler() {
					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
							event.stopPropagation();
						} else {

							showSearchBusyOnDoubleClick(true);

							searchDisplay.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
							searchDisplay.getCQlDefinitionsView().getDefineAceEditor().removeAllMarkers();
							resetAceEditor(searchDisplay.getCQlDefinitionsView().getViewCQLAceEditor());
							resetViewCQLCollapsiblePanel(
									searchDisplay.getCQlDefinitionsView().getPanelViewCQLCollapse());
							searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText("");

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

										// disable definitionName and fields for
										// Supplemental data definitions
										searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
												.setTitle("Delete");

										searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
												.setEnabled(false);
										searchDisplay.getCQlDefinitionsView().setWidgetReadOnly(false);
										searchDisplay.getCQlDefinitionsView().getAddNewButtonBar().getaddNewButton()
												.setEnabled(MatContext.get().getMeasureLockService()
														.checkForEditPermission());
										// load most recent used cql artifacts
										MatContext.get().getMeasureService().getUsedCQLArtifacts(
												MatContext.get().getCurrentMeasureId(),
												new AsyncCallback<GetUsedCQLArtifactsResult>() {

													@Override
													public void onFailure(Throwable caught) {
														showSearchBusyOnDoubleClick(false);
														searchDisplay.getCQlDefinitionsView().setWidgetReadOnly(
																MatContext.get().getMeasureLockService()
																		.checkForEditPermission());
														Window.alert(MatContext.get().getMessageDelegate()
																.getGenericErrorMessage());
													}

													@Override
													public void onSuccess(GetUsedCQLArtifactsResult result) {
														showSearchBusyOnDoubleClick(false);
														boolean isReadOnly = searchDisplay.getCqlLeftNavBarPanelView()
																.getDefinitionMap().get(selectedDefinitionID)
																.isSupplDataElement();
														CQLDefinition currentDefinition = searchDisplay
																.getCqlLeftNavBarPanelView().getDefinitionMap()
																.get(selectedDefinitionID);
														searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea()
																.setText(searchDisplay.getCqlLeftNavBarPanelView()
																		.getDefinitionMap().get(selectedDefinitionID)
																		.getName());
														searchDisplay.getCQlDefinitionsView().getDefineCommentTextArea()
																.setText(searchDisplay.getCqlLeftNavBarPanelView()
																		.getDefinitionMap().get(selectedDefinitionID)
																		.getCommentString());
														searchDisplay.getCQlDefinitionsView().getDefineAceEditor()
																.setText(searchDisplay.getCqlLeftNavBarPanelView()
																		.getDefinitionMap().get(selectedDefinitionID)
																		.getLogic());
														if (MatContext.get().getMeasureLockService()
																.checkForEditPermission()) {
															searchDisplay.getCQlDefinitionsView()
																	.setWidgetReadOnly(!isReadOnly);

															// if the current definition is not a default definition,
															// check if we need to enable the delete buttons
															if (!currentDefinition.isSupplDataElement()) {

																// if there are errors or the definition is not used,
																// enable the context radio buttons and delete button
																if (!result.getCqlErrors().isEmpty()
																		|| !result.getUsedCQLDefinitions()
																				.contains(currentDefinition
																						.getName())) {
																	searchDisplay.getCQlDefinitionsView()
																			.getDefineButtonBar().getDeleteButton()
																			.setEnabled(true);
																	searchDisplay.getCQlDefinitionsView()
																			.getContextDefinePATRadioBtn()
																			.setEnabled(true);
																	searchDisplay.getCQlDefinitionsView()
																			.getContextDefinePOPRadioBtn()
																			.setEnabled(true);
																} else {
																	searchDisplay.getCQlDefinitionsView()
																			.getDefineButtonBar().getDeleteButton()
																			.setEnabled(false);
																	searchDisplay.getCQlDefinitionsView()
																			.getContextDefinePATRadioBtn()
																			.setEnabled(false);
																	searchDisplay.getCQlDefinitionsView()
																			.getContextDefinePOPRadioBtn()
																			.setEnabled(false);
																}
															}
															
															if (searchDisplay.getCqlLeftNavBarPanelView()
																	.getDefinitionMap().get(selectedDefinitionID)
																	.getContext().equalsIgnoreCase("patient")) {
																searchDisplay.getCQlDefinitionsView()
																		.getContextDefinePATRadioBtn().setValue(true);
																searchDisplay.getCQlDefinitionsView()
																		.getContextDefinePOPRadioBtn().setValue(false);
															} else {
																searchDisplay.getCQlDefinitionsView()
																		.getContextDefinePOPRadioBtn().setValue(true);
																searchDisplay.getCQlDefinitionsView()
																		.getContextDefinePATRadioBtn().setValue(false);
															}

														}

														Map<String, List<CQLErrors>> expressionCQLErrorMap = result
																.getCqlErrorsPerExpression();
														if (expressionCQLErrorMap != null) {
															List<CQLErrors> errorList = expressionCQLErrorMap
																	.get(currentDefinition.getName());
															searchDisplay.getCQlDefinitionsView().getDefineAceEditor()
																	.clearAnnotations();
															searchDisplay.getCQlDefinitionsView().getDefineAceEditor()
																	.removeAllMarkers();
															for (CQLErrors error : errorList) {
																int startLine = error.getStartErrorInLine();
																int startColumn = error.getStartErrorAtOffset();
																searchDisplay.getCQlDefinitionsView()
																		.getDefineAceEditor().addAnnotation(startLine,
																				startColumn, error.getErrorMessage(),
																				AceAnnotationType.ERROR);
															}
															searchDisplay.getCQlDefinitionsView().getDefineAceEditor()
																	.setAnnotations();
														}

														if (result.getCqlErrors().isEmpty()
																&& result.getExpressionReturnTypeMap() != null) {
															searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
																	.setText(result.getExpressionReturnTypeMap().get(
																			currentDefinition.getName()));
															searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
																	.setTitle("Return Type of CQL Expression is "
																			+ result.getExpressionReturnTypeMap()
																					.get(currentDefinition
																							.getName()));

														} else {
															searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
																	.setText("");
															searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
																	.setTitle("Return Type of CQL Expression");
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
						// 508 changes for Definitions Section
						searchDisplay.getCqlLeftNavBarPanelView()
								.setFocus(searchDisplay.getCQlDefinitionsView().getMainDefineViewVerticalPanel());
					}
				});

		// Double Click Handler Event for Function Name ListBox in Function Section
		searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
					showSearchBusyOnDoubleClick(true);
					searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
					searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();

					resetAceEditor(searchDisplay.getCqlFunctionsView().getViewCQLAceEditor());
					resetViewCQLCollapsiblePanel(searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse());

					searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");
					searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
					searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
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
								searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton()
										.setEnabled(false);
								searchDisplay.getCqlFunctionsView().setWidgetReadOnly(false);
								searchDisplay.getCqlFunctionsView().getAddNewButtonBar().getaddNewButton()
										.setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
								// load most recent used cql artifacts
								MatContext.get().getMeasureService().getUsedCQLArtifacts(
										MatContext.get().getCurrentMeasureId(),
										new AsyncCallback<GetUsedCQLArtifactsResult>() {

											@Override
											public void onFailure(Throwable caught) {
												showSearchBusyOnDoubleClick(false);
												searchDisplay.getCqlFunctionsView().setWidgetReadOnly(MatContext.get()
														.getMeasureLockService().checkForEditPermission());
												Window.alert(
														MatContext.get().getMessageDelegate().getGenericErrorMessage());
											}

											@Override
											public void onSuccess(GetUsedCQLArtifactsResult result) {
												showSearchBusyOnDoubleClick(false);
												CQLFunctions currentFunction = searchDisplay.getCqlLeftNavBarPanelView()
														.getFunctionMap().get(selectedFunctionId);

												searchDisplay.getCqlFunctionsView().getFuncNameTxtArea()
														.setText(searchDisplay.getCqlLeftNavBarPanelView()
																.getFunctionMap().get(selectedFunctionId)
																.getName());
												searchDisplay.getCqlFunctionsView().getFunctionCommentTextArea()
														.setText(searchDisplay.getCqlLeftNavBarPanelView()
																.getFunctionMap().get(selectedFunctionId)
																.getCommentString());
												searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor()
														.setText(searchDisplay.getCqlLeftNavBarPanelView()
																.getFunctionMap().get(selectedFunctionId)
																.getLogic());
												if (searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
														.get(selectedFunctionId).getContext()
														.equalsIgnoreCase("patient")) {
													searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn()
															.setValue(true);
													searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn()
															.setValue(false);
												} else {
													searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn()
															.setValue(true);
													searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn()
															.setValue(false);
												}

												if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
													searchDisplay.getCqlFunctionsView().setWidgetReadOnly(true);
													
													if (!result.getCqlErrors().isEmpty()
															|| !result.getUsedCQLFunctions()
																	.contains(currentFunction.getName())) {
														searchDisplay.getCqlFunctionsView().getFunctionButtonBar()
																.getDeleteButton().setEnabled(true);
														// MAT-8571 :Enable Function context radio buttons if its used
														// and CQL has error.
														searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn()
																.setEnabled(true);
														searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn()
																.setEnabled(true);
													} else {
														searchDisplay.getCqlFunctionsView().getFunctionButtonBar()
																.getDeleteButton().setEnabled(false);
														searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn()
																.setEnabled(false);
														searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn()
																.setEnabled(false);
													}
												}

												Map<String, List<CQLErrors>> expressionCQLErrorMap = result
														.getCqlErrorsPerExpression();
												if (expressionCQLErrorMap != null) {
													List<CQLErrors> errorList = expressionCQLErrorMap
															.get(currentFunction.getName());
													searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor()
															.clearAnnotations();
													searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor()
															.removeAllMarkers();
													for (CQLErrors error : errorList) {
														int startLine = error.getStartErrorInLine();
														int startColumn = error.getStartErrorAtOffset();
														searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor()
																.addAnnotation(startLine, startColumn,
																		error.getErrorMessage(),
																		AceAnnotationType.ERROR);
													}
													searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor()
															.setAnnotations();
												}
												if (result.getCqlErrors().isEmpty()
														&& result.getExpressionReturnTypeMap() != null) {
													searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
															.setText(result.getExpressionReturnTypeMap()
																	.get(currentFunction.getName()));
													searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
															.setTitle("Return Type of CQL Expression is "
																	+ result.getExpressionReturnTypeMap()
																			.get(currentFunction.getName()));

												} else {
													searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
															.setText("");
													searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
															.setTitle("Return Type of CQL Expression");

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
							searchDisplay.getCqlFunctionsView().getFunctionArgNameMap().clear();
							CQLFunctions selectedFunction = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
									.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
							if (selectedFunction.getArgumentList() != null) {
								searchDisplay.getCqlFunctionsView().getFunctionArgumentList().clear();
								searchDisplay.getCqlFunctionsView().getFunctionArgumentList()
										.addAll(selectedFunction.getArgumentList());
							} else {
								searchDisplay.getCqlFunctionsView().getFunctionArgumentList().clear();
							}
						}
						searchDisplay.resetMessageDisplay();
					}
					searchDisplay.getCqlFunctionsView().createAddArgumentViewForFunctions(
							searchDisplay.getCqlFunctionsView().getFunctionArgumentList(),
							MatContext.get().getMeasureLockService().checkForEditPermission());

				}
				// 508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView()
						.setFocus(searchDisplay.getCqlFunctionsView().getMainFunctionVerticalPanel());
			}
		});

		// Double Click Handler Event for Includes Name ListBox in the Includes Section
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
															
															searchDisplay.getIncludeView().getSaveModifyButton()
																	.setEnabled(false);
															searchDisplay.getIncludeView().getSaveModifyButton()
																	.setEnabled(false);
															if (MatContext.get().getMeasureLockService()
																	.checkForEditPermission()) {
																searchDisplay.getIncludeView().setWidgetReadOnly(false);
																searchDisplay.getIncludeView().getDeleteButton()
																		.setEnabled(false);
																searchDisplay.getIncludeView().getSaveModifyButton()
																		.setEnabled(true);

																// load most recent used cql artifacts
																MatContext.get().getMeasureService()
																		.getUsedCQLArtifacts(
																				MatContext.get().getCurrentMeasureId(),
																				new AsyncCallback<GetUsedCQLArtifactsResult>() {

																					@Override
																					public void onFailure(
																							Throwable caught) {
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

																						// if the cql file has errors or
																						// the library is not in use,
																						// enable the includes delete
																						// button
																						if (!result.getCqlErrors()
																								.isEmpty()
																								|| !result
																										.getUsedCQLLibraries()
																										.contains(
																												cqlIncludeLibrary
																														.getCqlLibraryName()
																														+ "-"
																														+ cqlIncludeLibrary
																																.getVersion()
																														+ "|"
																														+ cqlIncludeLibrary
																																.getAliasName())) {
																							searchDisplay
																									.getIncludeView()
																									.getDeleteButton()
																									.setEnabled(true);
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
										searchDisplay.getIncludeView().setIncludedList(
												searchDisplay.getCqlLeftNavBarPanelView().getIncludedList(searchDisplay
														.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
										searchDisplay.getIncludeView().getSelectedObjectList().clear();
									}
								}
								searchDisplay.resetMessageDisplay();
							}
						}

					}
				});

		searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().addKeyPressHandler(event -> listBoxKeyPress(searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox(), event));

		searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().addKeyPressHandler(event -> listBoxKeyPress(searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox(), event));
		
		searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().addKeyPressHandler(event -> listBoxKeyPress(searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox(), event));
		
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().addKeyPressHandler(event -> listBoxKeyPress(searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox(), event));

		searchDisplay.getCqlLeftNavBarPanelView().getComponentsListBox().addKeyPressHandler(event -> listBoxKeyPress(searchDisplay.getCqlLeftNavBarPanelView().getComponentsListBox(), event));
	}

	private void listBoxKeyPress(ListBox listBox, KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
			DomEvent.fireNativeEvent(
					Document.get()
							.createDblClickEvent(listBox
									.getSelectedIndex(), 0, 0, 0, 0, false, false, false, false),
							listBox);
		}

	}

	/**
	 * Adds the general info event handlers.
	 */
	private void addGeneralInfoEventHandlers() {
		searchDisplay.getCqlGeneralInformationView().getSaveButton().addClickHandler(event -> saveCQLGeneralInformation());
		searchDisplay.getCqlGeneralInformationView().getComments().addValueChangeHandler(event -> resetMessagesAndSetPageDirty(true));
	}
	
	private void resetMessagesAndSetPageDirty(boolean isPageDirty) {
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(isPageDirty);
		}
	}
	
	private void saveCQLGeneralInformation() {
		resetMessagesAndSetPageDirty(false);
		String comments = searchDisplay.getCqlGeneralInformationView().getComments().getText().trim();

		MatContext.get().getMeasureService().saveAndModifyCQLGeneralInfo(MatContext.get().getCurrentMeasureId(), null, comments, 
				new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
						MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);
			}

			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if (result != null) {
					cqlLibraryComment = result.getCqlModel().getLibraryComment();
					searchDisplay.getCqlGeneralInformationView().getComments().setText(cqlLibraryComment);
					searchDisplay.getCqlGeneralInformationView().getComments().setCursorPos(0);
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
							MatContext.get().getCurrentMeasureName() + " general information successfully updated.");
				}
				showSearchingBusy(false);
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
				editIncludedLibraryDialogBox.findAvailableLibraries(currentIncludeLibrarySetId, currentIncludeLibraryId, true);

				editIncludedLibraryDialogBox.getApplyButton().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
						if (editIncludedLibraryDialogBox.getSelectedList().size() > 0) {
							final CQLIncludeLibrary toBeModified = searchDisplay.getCqlLeftNavBarPanelView()
									.getIncludeLibraryMap()
									.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId());

							final CQLLibraryDataSetObject dto = editIncludedLibraryDialogBox.getSelectedList().get(0);
							if (dto != null) {
								final CQLIncludeLibrary currentObject = new CQLIncludeLibrary(dto);

								MatContext.get().getMeasureService().saveIncludeLibrayInCQLLookUp(
										MatContext.get().getCurrentMeasureId(), toBeModified, currentObject,
										searchDisplay.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
										new AsyncCallback<SaveUpdateCQLResult>() {

											@Override
											public void onFailure(Throwable arg0) {
												editIncludedLibraryDialogBox.getDialogModal().hide();
												editIncludedLibraryDialogBox.getErrorMessageAlert().clearAlert();
												Window.alert(
														MatContext.get().getMessageDelegate().getGenericErrorMessage());
											}

											@Override
											public void onSuccess(SaveUpdateCQLResult result) {
												editIncludedLibraryDialogBox.getErrorMessageAlert().clearAlert();
												if (result != null) {
													currentIncludeLibraryId = dto.getId();
													if (result.isSuccess()) {
														searchDisplay.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(
																		filterComponentMeasuresFromIncludedLibraries(result.getCqlModel().getCqlIncludeLibrarys()));
														searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
														MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
														MatContext.get().setIncludedValues(result);
														MatContext.get().setCQLModel(result.getCqlModel());

														editIncludedLibraryDialogBox.getDialogModal().hide();
														DomEvent.fireNativeEvent(Document.get().createDblClickEvent(
																searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(), 0, 0, 0, 0, false, false, false, false),
																searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox());
														String libraryNameWithVersion = result.getIncludeLibrary().getCqlLibraryName() + " v" + result.getIncludeLibrary().getVersion();
														searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
																.createAlert(libraryNameWithVersion + " has been successfully saved as the alias " + result.getIncludeLibrary().getAliasName());
													}
												}

											}

										});

							}
						} else {
							editIncludedLibraryDialogBox.getErrorMessageAlert().clearAlert();
							editIncludedLibraryDialogBox.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getNO_LIBRARY_TO_REPLACE());
						}

					}
				});
			}
		});

		// KeyDownHandler for Includes FocusPanel
		searchDisplay.getIncludeView().getFocusPanel().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				// Search when enter is pressed.
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					searchDisplay.getIncludeView().getSearchButton().click();
				}
			}
		});

		// Search CQL Libraries Functionality for Includes Section
		searchDisplay.getIncludeView().getSearchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAllIncludeLibraryList(searchDisplay.getIncludeView().getSearchTextBox().getText().trim());
			}
		});

		// Save Functionality for Includes Section
		searchDisplay.getIncludeView().getIncludesButtonBar().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addIncludeLibraryInCQLLookUp();
					// 508 changes for Library Alias.
					searchDisplay.getCqlLeftNavBarPanelView()
							.setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
				}
			}
		});

		// Erase Functionality for Includes Section
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
				searchDisplay.getCqlLeftNavBarPanelView()
						.setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
			}
		});

		// Delete Functionality for Includes Section
		searchDisplay.getIncludeView().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_INCLUDE(
								searchDisplay.getIncludeView().getAliasNameTxtArea().getValue()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
				// 508 changes for Library Alias.
				searchDisplay.getIncludeView().getAliasNameTxtArea().setFocus(true);
			}

		});

		// Close Functionality for Includes Section
		searchDisplay.getIncludeView().getCloseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentIncludeLibrarySetId = null;
				// Below lines are to clear search suggestion textbox and listbox
				// selection after erase.
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
						MatContext.get().getMeasureLockService().checkForEditPermission(), false);
				searchDisplay.getIncludeView()
						.setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());

				if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
						.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
				}
				// 508 changes for Library Alias.
				searchDisplay.getCqlLeftNavBarPanelView()
						.setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
			}
		});

		searchDisplay.getIncludeView().setObserver(new CQLIncludeLibraryView.Observer() {

			// CheckBox Observer functionality for Includes Section
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
			// functionality to add Include Library
			CQLLibraryDataSetObject cqlLibraryDataSetObject = searchDisplay.getIncludeView().getSelectedObjectList().get(0);
			CQLIncludeLibrary incLibrary = new CQLIncludeLibrary();
			incLibrary.setAliasName(aliasName);
			incLibrary.setCqlLibraryId(cqlLibraryDataSetObject.getId());
			String versionValue = cqlLibraryDataSetObject.getVersion().replace("v", "") + "." + cqlLibraryDataSetObject.getRevisionNumber();
			incLibrary.setVersion(versionValue);
			incLibrary.setCqlLibraryName(cqlLibraryDataSetObject.getCqlName());
			incLibrary.setQdmVersion(cqlLibraryDataSetObject.getQdmVersion());
			incLibrary.setSetId(cqlLibraryDataSetObject.getCqlSetId());
			if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() == null) {
				showSearchingBusy(true);
				// this is just to add include library and not modify
				MatContext.get().getMeasureService().saveIncludeLibrayInCQLLookUp(
						MatContext.get().getCurrentMeasureId(), null, incLibrary,
						searchDisplay.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
						new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								showSearchingBusy(false);

								if (caught instanceof InvalidLibraryException) {
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
											.createAlert(caught.getMessage());
								} else {
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getGenericErrorMessage());
								}
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										searchDisplay.resetMessageDisplay();
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getCqlLeftNavBarPanelView()
												.setViewIncludeLibrarys(filterComponentMeasuresFromIncludedLibraries(
														result.getCqlModel().getCqlIncludeLibrarys()));
										MatContext.get().setIncludes(
												getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
										searchDisplay.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
										searchDisplay.getIncludeView().setIncludedList(
												searchDisplay.getCqlLeftNavBarPanelView().getIncludedList(searchDisplay
														.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
												MatContext.get().getMessageDelegate().getIncludeLibrarySuccessMessage(
														result.getIncludeLibrary().getAliasName()));
										clearAlias();
										MatContext.get().setIncludedValues(result);
										MatContext.get().setCQLModel(result.getCqlModel());

										if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
										} else {
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.clearAlert();
										}
									}
								}
								showSearchingBusy(false);
							}
						});
			}
		} else {
			searchDisplay.getIncludeView().getAliasNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getSAVE_INCLUDE_LIBRARY_VALIATION_ERROR());
		}
	}

	/**
	 * Event Handlers for Context Radio Buttons.
	 */
	private void addEventHandlersOnContextRadioButtons() {
		searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
							searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);
						} else {
							searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setValue(true);
						}

					}
				});

		searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().getValue()) {
							searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setValue(false);
						} else {
							searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
						}

					}
				});

		searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().getValue()) {
							searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
						} else {
							searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setValue(true);
						}
					}
				});

		searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().getValue()) {
							searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setValue(false);
						} else {
							searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setValue(true);
						}
					}
				});
	}

	/**
	 * Event Handlers for Ace Editors.
	 */
	private void addEventHandlerOnAceEditors() {
		searchDisplay.getCQlDefinitionsView().getDefineAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getCQlDefinitionsView().getDefineAceEditor().isReadOnly()) {
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

		searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().isReadOnly()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
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
	
	private static void unsetCurrentSelection(){
		switch (currentSection) {
		case (CQLWorkSpaceConstants.CQL_COMPONENTS_MENU):
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getComponentsTab().setActive(false);
			break;
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
	}
	
	public void setNextSelection() {
		// Set Next Selected Section.
				switch (nextSection) {
				case (CQLWorkSpaceConstants.CQL_COMPONENTS_MENU):
					currentSection = nextSection;
					componentsEvent();
					searchDisplay.getCqlLeftNavBarPanelView().getComponentsCollapse().getElement()
							.setClassName("panel-collapse collapse in");
					break;
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
	 * Method to Unset current Left Nav section and set next selected section when
	 * user clicks yes on warning message (Dirty Check).
	 */
	private void changeSectionSelection() {
		searchDisplay.hideInformationDropDown();
		unsetCurrentSelection();
		setNextSelection();
		
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
	 * Adds the Value Set observer handler.
	 */
	private void addValueSetObserverHandler() {

		searchDisplay.getValueSetView().setObserver(new CQLAppliedValueSetView.Observer() {
			@Override
			public void onModifyClicked(CQLQualityDataSetDTO result) {
				if (!searchDisplay.getValueSetView().getIsLoading()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
					isModified = true;
					modifyValueSetDTO = result;
					String displayName = result.getName();
					// Substring at 60th character length.
					if (displayName.length() >= 60) {
						displayName = displayName.substring(0, 59);
					}
					HTML searchHeaderText = new HTML("<strong>Modify value set ( " + displayName + ")</strong>");
					searchDisplay.getValueSetView().getSearchHeader().clear();
					searchDisplay.getValueSetView().getSearchHeader().add(searchHeaderText);
					searchDisplay.getValueSetView().getMainPanel().getElement().focus();
					if (result.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
						isUserDefined = true;
					} else {
						isUserDefined = false;
					}

					onModifyValueSet(result, isUserDefined);
					// 508 Compliance for Value Sets section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				} else {
					// do nothing when loading.
				}

			}

			@Override
			public void onDeleteClicked(CQLQualityDataSetDTO result, final int index) {
				if (!searchDisplay.getValueSetView().getIsLoading()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
					if ((modifyValueSetDTO != null) && modifyValueSetDTO.getId().equalsIgnoreCase(result.getId())) {
						isModified = false;
					}
					String measureId = MatContext.get().getCurrentMeasureId();
					if ((measureId != null) && !measureId.equals("")) {
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(result.getId());
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate()
										.getDELETE_CONFIRMATION_VALUESET(result.getName()));
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
						// 508 Compliance for Value Sets section
						searchDisplay.getValueSetView().getOIDInput().setFocus(true);
					}
				} else {
					// do nothing when loading.
				}

			}

		});

	}

	/**
	 * Save measure xml.
	 *
	 * @param toBeDeletedValueSetId
	 *            the to Be Deleted Value Set Id
	 * 
	 */
	private void deleteValueSet(String toBeDeletedValueSetId) {
		showSearchingBusy(true);
		MatContext.get().getMeasureService().deleteValueSet(toBeDeletedValueSetId,
				MatContext.get().getCurrentMeasureId(), new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onFailure(final Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(final SaveUpdateCQLResult result) {
						if (result != null && result.getCqlErrors().isEmpty()) {
							modifyValueSetDTO = null;
							// The below call will update the Applied QDM drop down list in insert popup.
							getAppliedValueSetList();
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_QDM_REMOVE_MSG(
											result.getCqlQualityDataSetDTO().getName()));
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);
						}
						getUsedValueSets();
						showSearchingBusy(false);
					}
				});
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
								searchDisplay.getCqlFunctionsView(), searchDisplay.getCqlLeftNavBarPanelView(),
								MatContext.get().getMeasureLockService().checkForEditPermission());

					}

				});
	}

	/**
	 * This method Clears alias view on Erase Button click when isPageDirty is not
	 * set.
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
				MatContext.get().getMeasureLockService().checkForEditPermission(), false);
	}

	/**
	 * Clears the ace editor for parameters. Functionality for the eraser icon in
	 * parameter section.
	 */
	private void eraseParameter() {

		searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if (searchDisplay.getCQLParametersView().getParameterAceEditor().getText() != null) {
			searchDisplay.getCQLParametersView().getParameterAceEditor().setText("");
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}

	}

	/**
	 * Clears the ace editor for definitions. Functionality for the eraser icon in
	 * definition section.
	 */
	private void eraseDefinition() {

		searchDisplay.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
		if ((searchDisplay.getCQlDefinitionsView().getDefineAceEditor().getText() != null)) {
			searchDisplay.getCQlDefinitionsView().getDefineAceEditor().setText("");
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}
	}

	/**
	 * Clears the ace editor for functions. Functionality for the eraser icon in
	 * function section.
	 */
	private void eraseFunction() {

		searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if ((searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().setText("");
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}
	}

	/**
	 * Adds and modify function.
	 */
	protected void addAndModifyFunction() {
		searchDisplay.resetMessageDisplay();
		final String functionName = searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().getText();
		String functionBody = searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().getText();
		String funcComment = searchDisplay.getCqlFunctionsView().getFunctionCommentTextArea().getText();
		String funcContext = "";
		if (searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().getValue()) {
			funcContext = "Patient";
		} else {
			funcContext = "Population";
		}
		
		boolean isValidFunctionName = isValidExpressionName(functionName);
		if (isValidFunctionName) {
			if (validator.hasSpecialCharacter(functionName.trim())) {
				searchDisplay.getCqlFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
			} else if (validator.validateForCommentTextArea(funcComment)) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				searchDisplay.getCqlFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
			} else {
				CQLFunctions function = new CQLFunctions();
				function.setLogic(functionBody);
				function.setName(functionName);
				function.setCommentString(funcComment);
				function.setArgumentList(searchDisplay.getCqlFunctionsView().getFunctionArgumentList());
				function.setContext(funcContext);
				CQLFunctions toBeModifiedParamObj = null;

				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
					toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getMeasureService().saveAndModifyFunctions(MatContext.get().getCurrentMeasureId(),
						toBeModifiedParamObj, function, searchDisplay.getCqlLeftNavBarPanelView().getViewFunctions(),
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

										searchDisplay.getCqlFunctionsView().getFuncNameTxtArea()
												.setText(result.getFunction().getName());
										searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor()
												.setText(result.getFunction().getLogic());
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor()
												.clearAnnotations();
										searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor()
												.removeAllMarkers();
										if (validateCQLArtifact(result, currentSection)) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_FUNCTION_MODIFY_WITH_ERRORS(
																	functionName.trim()));
											searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");
											searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
													.setTitle("Return Type of CQL Expression");

										} else if (!result.isDatatypeUsedCorrectly()) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getWarningBadDataTypeCombination());
											if (result.isValidCQLWhileSavingExpression()) {
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
														.setText(result.getFunction().getReturnType());
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression is "
																+ result.getFunction().getReturnType());
											} else {
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression");
											}

										} else {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_FUNCTION_MODIFY(functionName.trim()));
											if (result.isValidCQLWhileSavingExpression()) {
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
														.setText(result.getFunction().getReturnType());
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression is "
																+ result.getFunction().getReturnType());
											} else {
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression");
											}
										}

										searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().setAnnotations();
										searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().redisplay();

									} else if (result.getFailureReason() == 1) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getERROR_DUPLICATE_IDENTIFIER_NAME());
										searchDisplay.getCqlFunctionsView().getFuncNameTxtArea()
												.setText(functionName.trim());
									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unable to find Node to modify.");
										searchDisplay.getCqlFunctionsView().getFuncNameTxtArea()
												.setText(functionName.trim());
									} else if (result.getFailureReason() == 3) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
										searchDisplay.getCqlFunctionsView().getFuncNameTxtArea()
												.setText(functionName.trim());
										if (result.getFunction() != null) {
											searchDisplay.getCqlFunctionsView().createAddArgumentViewForFunctions(
													result.getFunction().getArgumentList(),
													MatContext.get().getMeasureLockService().checkForEditPermission());
										}
									} else if (result.getFailureReason() == 6) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getCqlFunctionArgumentNameError());
									} else if (result.getFailureReason() == 8) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getERROR_VALIDATION_COMMENT_AREA());
									}

								}
								showSearchingBusy(false);
								// if there are errors, enable the function delete button
								if (!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton()
											.setEnabled(true);
									searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setEnabled(true);
									searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setEnabled(true);
									searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");

								}

						else {
									// if the saved function is in use, then disable the delete button
									if (result.getUsedCQLArtifacts().getUsedCQLFunctions().contains(functionName)) {
										searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton()
												.setEnabled(false);
										searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn()
												.setEnabled(false);
										searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn()
												.setEnabled(false);
									}

						else {
										searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton()
												.setEnabled(true);
										searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn()
												.setEnabled(true);
										searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn()
												.setEnabled(true);
									}
								}
							}
						});

			}

		} else {
			searchDisplay.getCqlFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(functionName.isEmpty() 
					? MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_FUNCTION()
					: "Invalid Function name. " + MessageDelegate.DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
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
			if (validator.hasSpecialCharacter(parameterName.trim())) {
				searchDisplay.getCQLParametersView().getParamNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());

			} else if (validator.validateForCommentTextArea(parameterComment)) {
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
				MatContext.get().getMeasureService().saveAndModifyParameters(MatContext.get().getCurrentMeasureId(),
						toBeModifiedParamObj, parameter,
						searchDisplay.getCqlLeftNavBarPanelView().getViewParameterList(), isFormatable,
						new AsyncCallback<SaveUpdateCQLResult>() {

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
										
										if (validateCQLArtifact(result, currentSection)) {
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_PARAMETER_MODIFY_WITH_ERRORS(
																	parameterName.trim()));

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
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getERROR_VALIDATION_COMMENT_AREA());
									}
								}
								showSearchingBusy(false);
								// if there are errors, enable the parameter delete button
								if (!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton()
											.setEnabled(true);
								}

						else {
									// if the saved parameter is in use, then disable the delete button
									if (result.getUsedCQLArtifacts().getUsedCQLParameters().contains(parameterName)) {
										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setEnabled(false);
									}

						else {
										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setEnabled(true);
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
	 * This method is called to Add/Modify Definitions into Measure Xml.
	 * 
	 */
	private void addAndModifyDefintions() {
		searchDisplay.resetMessageDisplay();
		final String definitionName = searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().getText();
		String definitionLogic = searchDisplay.getCQlDefinitionsView().getDefineAceEditor().getText();
		String definitionComment = searchDisplay.getCQlDefinitionsView().getDefineCommentTextArea().getText();
		String defineContext = "";
		if (searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}
		
		boolean isValidDefinitionName = isValidExpressionName(definitionName);
		if (isValidDefinitionName) {
			if (validator.hasSpecialCharacter(definitionName.trim())) {
				searchDisplay.getCQlDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
			} else if (validator.validateForCommentTextArea(definitionComment)) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				searchDisplay.getCQlDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);

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
				MatContext.get().getMeasureService().saveAndModifyDefinitions(MatContext.get().getCurrentMeasureId(),
						toBeModifiedObj, define, searchDisplay.getCqlLeftNavBarPanelView().getViewDefinitions(),
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
										searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea()
												.setText(result.getDefinition().getName());
										searchDisplay.getCQlDefinitionsView().getDefineAceEditor()
												.setText(result.getDefinition().getLogic());
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
										searchDisplay.getCQlDefinitionsView().getDefineAceEditor().removeAllMarkers();
										
										if (validateCQLArtifact(result, currentSection)) {
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_DEFINITION_MODIFY_WITH_ERRORS(
																	definitionName.trim()));
											searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText("");
											searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
													.setTitle("Return Type of CQL Expression");
										} else if (!result.isDatatypeUsedCorrectly()) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getWarningBadDataTypeCombination());
											if (result.isValidCQLWhileSavingExpression()) {
												searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
														.setText(result.getDefinition().getReturnType());
												searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression is "
																+ result.getDefinition().getReturnType());
											} else {
												searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
														.setText("");
												searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression");
											}

										} else {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getSUCESS_DEFINITION_MODIFY(definitionName.trim()));
											if (result.isValidCQLWhileSavingExpression()) {
												searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
														.setText(result.getDefinition().getReturnType());
												searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression is "
																+ result.getDefinition().getReturnType());
											} else {
												searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
														.setText("");
												searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression");
											}
										}

										searchDisplay.getCQlDefinitionsView().getDefineAceEditor().setAnnotations();
										searchDisplay.getCQlDefinitionsView().getDefineAceEditor().redisplay();

									} else {
										if (result.getFailureReason() == 1) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getERROR_DUPLICATE_IDENTIFIER_NAME());
											searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea()
													.setText(definitionName.trim());
										} else if (result.getFailureReason() == 2) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert("Unable to find Node to modify.");
											searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea()
													.setText(definitionName.trim());
										} else if (result.getFailureReason() == 3) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea()
													.setText(definitionName.trim());
										} else if (result.getFailureReason() == 8) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert(MatContext.get().getMessageDelegate()
															.getERROR_VALIDATION_COMMENT_AREA());
										}
									}

								}
								showSearchingBusy(false);
								// if there are errors, enable the definition button
								if (!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
											.setEnabled(true);
									searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn()
											.setEnabled(true);
									searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn()
											.setEnabled(true);
									searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText("");
								}

						else {
									// if the saved definition is in use, then disable the delete button
									if (result.getUsedCQLArtifacts().getUsedCQLDefinitions().contains(definitionName)) {
										searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
												.setEnabled(false);
										searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn()
												.setEnabled(false);
										searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn()
												.setEnabled(false);
									}

						else {
										searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
												.setEnabled(true);
										searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn()
												.setEnabled(true);
										searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn()
												.setEnabled(true);
									}
								}
							}
						});

			}

		} else {
			searchDisplay.getCQlDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(definitionName.isEmpty() 
					? MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_DEFINITION()
					: "Invalid Definition name. " + MessageDelegate.DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
		}

	}

	/**
	 * Before closing display.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		searchDisplay.getCqlLeftNavBarPanelView().clearShotcutKeyList();
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		searchDisplay.getCqlFunctionsView().getFunctionArgNameMap().clear();
		searchDisplay.getValueSetView().clearCellTableMainPanel();
		searchDisplay.getCodesView().clearCellTableMainPanel();
		searchDisplay.getIncludeView().getSearchTextBox().setText("");
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		searchDisplay.resetMessageDisplay();
		searchDisplay.getCqlLeftNavBarPanelView().getComponentsCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getComponentView().clearAceEditor();
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
				.setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
				.setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
				.setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
				.setClassName("panel-collapse collapse");
		if (searchDisplay.getCqlFunctionsView().getFunctionArgumentList().size() > 0) {
			searchDisplay.getCqlFunctionsView().getFunctionArgumentList().clear();
		}
		isModified = false;
		isCodeModified = false;
		setId = null;
		currentIncludeLibrarySetId = null;
		currentIncludeLibraryId = null;
		modifyValueSetDTO = null;
		curAceEditor = null;
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		searchDisplay.getCqlLeftNavBarPanelView().getMessagePanel().clear();
		searchDisplay.resetAll();
		panel.clear();
		searchDisplay.getMainPanel().clear();
		MatContext.get().getValuesets().clear();
	}

	/**
	 * Before display.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		searchDisplay.buildView();
		addLeftNavEventHandler();
		searchDisplay.resetMessageDisplay();
		panel.add(searchDisplay.asWidget());
		getCQLDataForLoad();
		getComponentMeasureData();
		if (searchDisplay.getCqlFunctionsView().getFunctionArgumentList().size() > 0) {
			searchDisplay.getCqlFunctionsView().getFunctionArgumentList().clear();
		}
		unsetAllSections();
		MeasureComposerPresenter.setSubSkipEmbeddedLink("CQLWorkspaceView.containerPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * This method is called at beforeDisplay and get searchButton click on Include
	 * section and reterives CQL Versioned libraries eligible to be included into
	 * any parent cql library.
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
		
		MatContext.get().getCQLLibraryService().searchForIncludes(setId, searchText, true,
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
									MatContext.get().getMeasureLockService().checkForEditPermission(), false);

						} else {
							searchDisplay.buildIncludesView();
							searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,
									MatContext.get().getMeasureLockService().checkForEditPermission(), false);
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
						searchDisplay.getCqlLeftNavBarPanelView()
								.setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
					}
				});

	}

	private void getCQLDataForLoad() {
		showSearchingBusy(true);
		MatContext.get().getMeasureService().getMeasureCQLDataForLoad(MatContext.get().getCurrentMeasureId(),
				new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);

					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						handleCQLData(result);
						showSearchingBusy(false);
					}
				});		
	}
	
	private void getComponentMeasureData() {
		MatContext.get().getMeasureService().getCQLLibraryInformationForComponentMeasure(MatContext.get().getCurrentMeasureId(), new AsyncCallback<List<ComponentMeasureTabObject>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(List<ComponentMeasureTabObject> results) {
				
				if(results.size() == 0) {
					searchDisplay.getCqlLeftNavBarPanelView().getComponentsTab().setVisible(false);
				}
				else {
					searchDisplay.getCqlLeftNavBarPanelView().getComponentsTab().setVisible(true);
					searchDisplay.getCqlLeftNavBarPanelView().updateComponentInformation(results);
				}
			}
		});
	}

	private void handleCQLData(SaveUpdateCQLResult result) {
		if (result != null && result.getCqlModel() != null) {

			if (result.getSetId() != null) {
				setId = result.getSetId();
			}
			if (result.getCqlModel().getLibraryName() != null) {
				String cqlLibraryName = searchDisplay.getCqlGeneralInformationView()
						.createCQLLibraryName(MatContext.get().getCurrentMeasureName());
				searchDisplay.getCqlGeneralInformationView().getLibraryNameValue().setText(cqlLibraryName);
				
				cqlLibraryComment = result.getCqlModel().getLibraryComment();
				String measureVersion = MatContext.get().getCurrentMeasureVersion();

				measureVersion = measureVersion.replaceAll("Draft ", "").trim();
				if (measureVersion.startsWith("v")) {
					measureVersion = measureVersion.substring(1);
				}
				searchDisplay.getCqlGeneralInformationView().setGeneralInfoOfLibrary(cqlLibraryName, measureVersion,
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
			if (result.getCqlModel().getCodeList() != null) {
				appliedCodeTableList.addAll(result.getCqlModel().getCodeList());
			}
			searchDisplay.getCqlLeftNavBarPanelView().setAppliedQdmTableList(appliedValueSetTableList);
			searchDisplay.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
			searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
			searchDisplay.getCqlLeftNavBarPanelView().setAppliedCodeTableList(appliedCodeTableList);
			if ((result.getCqlModel().getDefinitionList() != null)
					&& (result.getCqlModel().getDefinitionList().size() > 0)) {
				searchDisplay.getCqlLeftNavBarPanelView().setViewDefinitions(result.getCqlModel().getDefinitionList());
				searchDisplay.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
				searchDisplay.getCqlLeftNavBarPanelView().updateDefineMap();
				MatContext.get().setDefinitions(getDefinitionList(result.getCqlModel().getDefinitionList()));
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getDefineBadge().setText("00");
			}
			if ((result.getCqlModel().getCqlParameters() != null)
					&& (result.getCqlModel().getCqlParameters().size() > 0)) {
				searchDisplay.getCqlLeftNavBarPanelView().setViewParameterList(result.getCqlModel().getCqlParameters());
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
				searchDisplay.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(filterComponentMeasuresFromIncludedLibraries(result.getCqlModel().getCqlIncludeLibrarys()));
				searchDisplay.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
				searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
				MatContext.get().setIncludedValues(result);

			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getIncludesBadge().setText("00");
				searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap().clear();
			}

			boolean isValidQDMVersion = searchDisplay.getCqlLeftNavBarPanelView().checkForIncludedLibrariesQDMVersion(false); //false because it is a measures cql view
			if (!isValidQDMVersion) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getINVALID_QDM_VERSION_IN_INCLUDES());
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
			}

		} else {
			Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
		}
	}
	
	private List<CQLIncludeLibrary> filterComponentMeasuresFromIncludedLibraries(List<CQLIncludeLibrary> cqlIncludeLibraryList) {
		return Optional.ofNullable(cqlIncludeLibraryList).orElseGet(Collections::emptyList).stream().
				filter(lib -> lib.getIsComponent() == null || !"true".equals(lib.getIsComponent())).collect(Collectors.toList());
	}

	/**
	 * Adding handlers for Anchor Items.
	 */
	private void addLeftNavEventHandler() {

		searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.hideAceEditorAutoCompletePopUp();
				generalInfoEvent();
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					includesEvent();
				}

			}
		});
		
		searchDisplay.getCqlLeftNavBarPanelView().getComponentsTab().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_COMPONENTS_MENU;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					componentsEvent();
				}

			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				appliedQDMEvent();
				// 508 : Shift focus to search panel.
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

		searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_CODES;
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					codesEvent();
					// 508 : Shift focus to code search panel.
					searchDisplay.getCqlLeftNavBarPanelView()
							.setFocus(searchDisplay.getCodesView().getCodeSearchInput());
				}
			}
		});

	}

	/**
	 * Build View for General info when General Info AnchorList item is clicked.
	 */
	private void generalInfoEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();

		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			searchDisplay.buildGeneralInformation();
			boolean isValidQDMVersion = searchDisplay.getCqlLeftNavBarPanelView().checkForIncludedLibrariesQDMVersion(false); //false because it is a measures cql view
			if (!isValidQDMVersion) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getINVALID_QDM_VERSION_IN_INCLUDES());
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
			}
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
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();

		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			searchDisplay.getValueSetView().getPasteButton()
					.setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
			buildAppliedQDMTable();
		}
		//On load of Value Sets page, set the Programs from VSAC 
		loadProgramsAndReleases();		
		searchDisplay.getValueSetView().setHeading("CQL Workspace > Value Sets", "subQDMAPPliedListContainerPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	private void loadProgramsAndReleases() {
		CQLAppliedValueSetUtility.loadProgramsAndReleases(searchDisplay.getValueSetView().getProgramListBox(), searchDisplay.getValueSetView().getReleaseListBox());
	}

	
	private void buildAppliedQDMTable() {
		searchDisplay.buildAppliedQDM();
		boolean isEditable = MatContext.get().getMeasureLockService().checkForEditPermission();

		// initialize the valuesets to be used, getUsedArtifacts() will update with the
		// proper value
		for (CQLQualityDataSetDTO valuset : searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList()) {
			valuset.setUsed(true);
		}

		searchDisplay.getValueSetView().buildAppliedValueSetCellTable(
				searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList(), isEditable);
		searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
		searchDisplay.getValueSetView().setWidgetsReadOnly(isEditable);
		getUsedValueSets();
	}

	private void getUsedValueSets() {
		showSearchingBusy(true);
		MatContext.get().getMeasureService().getUsedCQLArtifacts(MatContext.get().getCurrentMeasureId(),
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
						if (!result.getCqlErrors().isEmpty()) {
							for (CQLQualityDataSetDTO cqlDTo : searchDisplay.getCqlLeftNavBarPanelView()
									.getAppliedQdmTableList()) {
								cqlDTo.setUsed(false);
							}
						}

						// otherwise, check if the valueset is in the used valusets list
						else {
							for (CQLQualityDataSetDTO cqlDTo : searchDisplay.getCqlLeftNavBarPanelView()
									.getAppliedQdmTableList()) {
								if (result.getUsedCQLValueSets().contains(cqlDTo.getName())) {
									cqlDTo.setUsed(true);
								} else {
									cqlDTo.setUsed(false);
								}
							}
						}

						if (searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList().size() > 0) {
							searchDisplay.getValueSetView().getCelltable().redraw();
							searchDisplay.getValueSetView().getListDataProvider().refresh();
						}
					}

				});

	}

	/**
	 * Call this to update used status for delete button.
	 */
	private void getUsedCodes() {
		showSearchingBusy(true);
		MatContext.get().getMeasureService().getUsedCQLArtifacts(MatContext.get().getCurrentMeasureId(),
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
						if (!result.getCqlErrors().isEmpty()) {
							for (CQLCode cqlCode : appliedCodeTableList) {
								cqlCode.setUsed(false);
							}
						}

						// otherwise, check if the valueset is in the used valusets list
						else {
							for (CQLCode cqlCode : appliedCodeTableList) {
								if (result.getUsedCQLcodes().contains(cqlCode.getDisplayName())) {
									cqlCode.setUsed(true);
								} else {
									cqlCode.setUsed(false);
								}
							}
						}

						if (searchDisplay.getCqlLeftNavBarPanelView().getAppliedCodeTableList().size() > 0) {
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
				.setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
		searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton()
				.setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());

		searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = searchDisplay.getCQLParametersView().getParameterAceEditor();
		searchDisplay.getCQLParametersView().setHeading("CQL Workspace > Parameter", "mainParamViewVerticalPanel");
		Mat.focusSkipLists("MeasureComposer");
	}
	
	/**
	 * Build View for Components when components AnchorList item is clicked.
	 */
	private void componentsEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.hideInformationDropDown();
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		searchDisplay.getCqlLeftNavBarPanelView().getComponentsTab().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_COMPONENTS_MENU;
		searchDisplay.getMainFlowPanel().clear();
		searchDisplay.buildComponentsView();
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(new ArrayList<CQLLibraryDataSetObject>());
		searchDisplay.getCqlLeftNavBarPanelView().updateComponentSearchBox();
		Mat.focusSkipLists("MeasureComposer");
	}
	

	/**
	 * Build View for Includes when Includes AnchorList item is clicked.
	 */
	private void includesEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.hideInformationDropDown();
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
		searchDisplay.getMainFlowPanel().clear();
		searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
				.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
		searchDisplay.buildIncludesView();
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(new ArrayList<CQLLibraryDataSetObject>());
		searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,
				MatContext.get().getLibraryLockService().checkForEditPermission(), true);
		searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
		searchDisplay.getIncludeView().getSearchTextBox().setText("");
		searchDisplay.getIncludeView()
				.setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
		searchDisplay.getIncludeView().setHeading("CQL Workspace > Includes", "IncludeSectionContainerPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * codes event.
	 */
	private void codesEvent() {
		searchDisplay.hideInformationDropDown();
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
			searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList,
					MatContext.get().getMeasureLockService().checkForEditPermission());
			searchDisplay.getCodesView().resetCQLCodesSearchPanel();
			searchDisplay.getCodesView()
					.setWidgetsReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());

			getUsedCodes();
			boolean isEditable = MatContext.get().getMeasureLockService().checkForEditPermission();
			searchDisplay.getCodesView().getPasteButton().setEnabled(isEditable);

		}
		searchDisplay.getCodesView().setHeading("CQL Workspace > Codes", "codesContainerPanel");
		Mat.focusSkipLists("MeasureComposer");
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

		searchDisplay.getCQlDefinitionsView()
				.setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
	
		searchDisplay.getCQlDefinitionsView().getAddNewButtonBar().getaddNewButton()
				.setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = searchDisplay.getCQlDefinitionsView().getDefineAceEditor();
		searchDisplay.getCQlDefinitionsView().setHeading("CQL Workspace > Definition", "mainDefViewVerticalPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * Build View for Function when Funtion AnchorList item is clicked.
	 */
	private void functionEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		searchDisplay.getValueSetView().getCellTableMainPanel().clear();
		searchDisplay.getCodesView().getCellTableMainPanel().clear();
		unsetActiveMenuItem(currentSection);
		searchDisplay.hideInformationDropDown();
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
		searchDisplay.buildFunctionLibraryView();
		searchDisplay.getCqlFunctionsView()
				.setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
		
		searchDisplay.getCqlFunctionsView().getAddNewButtonBar().getaddNewButton()
				.setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
		searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor();
		searchDisplay.getCqlFunctionsView().setHeading("CQL Workspace > Function", "mainFuncViewVerticalPanel");
		Mat.focusSkipLists("MeasureComposer");

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
			searchDisplay.buildCQLFileView(MatContext.get().getMeasureLockService().checkForEditPermission());
			buildCQLView();
		}
		searchDisplay.getViewCQLView().setHeading("CQL Workspace > View CQL", "cqlViewCQL_Id");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * Method to unset Anchor List Item selection for previous selection and set for
	 * new selections.
	 *
	 * @param menuClickedBefore
	 *            the menu clicked before
	 */
	private static void unsetActiveMenuItem(String menuClickedBefore) {
		if (!searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			searchDisplay.resetMessageDisplay();
			if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_GENERAL_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(false);
			} else if(menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_COMPONENTS_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getComponentsTab().setActive(false);
				searchDisplay.getCqlLeftNavBarPanelView().getComponentsListBox().setSelectedIndex(-1);
				searchDisplay.getCqlLeftNavBarPanelView().getComponentsCollapse().getElement().setClassName("panel-collapse collapse");
				searchDisplay.getComponentView().clearAceEditor();
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
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_CODES)) {
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
	 * Method to build View for Anchor List item View CQL.
	 */
	private void buildCQLView() {
		searchDisplay.getViewCQLView().getCqlAceEditor().setText("");
		showSearchingBusy(true);
		MatContext.get().getMeasureService().getMeasureCQLLibraryData(MatContext.get().getCurrentMeasureId(),
				new AsyncCallback<SaveUpdateCQLResult>() {
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							if ((result.getCqlString() != null) && !result.getCqlString().isEmpty()) {
								
								searchDisplay.getViewCQLView().getCqlAceEditor().clearAnnotations();
								searchDisplay.getViewCQLView().getCqlAceEditor().removeAllMarkers();
								searchDisplay.getViewCQLView().getCqlAceEditor().redisplay();
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
								searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
								searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationMessageAlert()
										.clearAlert();

								if (!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getVIEW_CQL_ERROR_MESSAGE());
									for (CQLErrors error : result.getCqlErrors()) {
										String errorMessage = new String();
										errorMessage = errorMessage.concat("Error in line : " + error.getErrorInLine()
												+ " at Offset :" + error.getErrorAtOffeset());
										int line = error.getErrorInLine();
										int column = error.getErrorAtOffeset();
										searchDisplay.getViewCQLView().getCqlAceEditor().addAnnotation(line - 1, column,
												error.getErrorMessage(), AceAnnotationType.ERROR);
									}
									searchDisplay.getViewCQLView().getCqlAceEditor().setText(result.getCqlString());
									searchDisplay.getViewCQLView().getCqlAceEditor().setAnnotations();
									searchDisplay.getViewCQLView().getCqlAceEditor().redisplay();
								} else if (!result.isDatatypeUsedCorrectly()) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
											.createAlert(MatContext.get().getMessageDelegate()
													.getVIEW_CQL_ERROR_MESSAGE_BAD_VALUESET_DATATYPE());
									searchDisplay.getViewCQLView().getCqlAceEditor().setText(result.getCqlString());
								} else {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getVIEW_CQL_NO_ERRORS_MESSAGE());
									searchDisplay.getViewCQLView().getCqlAceEditor().setText(result.getCqlString());
								}
							}
							showSearchingBusy(false);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
					}
				});

	}

	/**
	 * Gets the widget.
	 *
	 * @return the widget
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		panel.setStyleName("contentPanel");
		return panel;
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
			CQLIdentifierObject definition = new CQLIdentifierObject(null, definitionList.get(i).getName(),
					definitionList.get(i).getId());
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
			CQLIdentifierObject parameter = new CQLIdentifierObject(null, parameterList.get(i).getName(),
					parameterList.get(i).getId());
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
			CQLIdentifierObject function = new CQLIdentifierObject(null, functionList.get(i).getName(),
					functionList.get(i).getId());
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
	 * returns the searchDisplay.
	 * 
	 * @return ViewDisplay.
	 */
	public static ViewDisplay getSearchDisplay() {
		return searchDisplay;
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
			// final AceEditor editor = getAceEditorBasedOnCurrentSection(searchDisplay,
			// currentSection);
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
	 * Delete definition.
	 */
	protected void deleteDefinition() {
		searchDisplay.resetMessageDisplay();
		final String definitionName = searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().getText();
		
		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
			final CQLDefinition toBeModifiedObj = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
					.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
			if (toBeModifiedObj.isSupplDataElement()) {
				searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert("Unauthorized delete operation.");
				searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
			} else {
				showSearchingBusy(true);
				MatContext.get().getMeasureService().deleteDefinition(MatContext.get().getCurrentMeasureId(),
						toBeModifiedObj, searchDisplay.getCqlLeftNavBarPanelView().getViewDefinitions(),
						new AsyncCallback<SaveUpdateCQLResult>() {

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
										searchDisplay.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().updateDefineMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.setVisible(true);

										searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox()
												.setText("");
										searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText("");
										searchDisplay.getCQlDefinitionsView().getDefineAceEditor().setText("");
										searchDisplay.getCqlLeftNavBarPanelView()
												.setCurrentSelectedDefinitionObjId(null);
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
										searchDisplay.getCQlDefinitionsView().getDefineAceEditor().removeAllMarkers();
										// Commenting below code as its taking away focus and that makes our application
										// not 508 compliant with other fields.
										// searchDisplay.getCQlDefinitionsView().getDefineAceEditor().redisplay();
										searchDisplay.getCQlDefinitionsView().getDefineAceEditor().setAnnotations();
										searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
												.setEnabled(false);
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getSuccessfulDefinitionRemoveMessage(
																toBeModifiedObj.getName()));
										searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText("");

									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unable to find Node to modify.");
										searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea()
												.setText(definitionName.trim());
									} else if (result.getFailureReason() == 4) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unauthorized delete operation.");
										searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea()
												.setText(definitionName.trim());
									}
								}
								showSearchingBusy(false);
								// 508 changes for Definitions Section
								searchDisplay.getCqlLeftNavBarPanelView().setFocus(
										searchDisplay.getCQlDefinitionsView().getMainDefineViewVerticalPanel());
							}
						});
			}

		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select a definition to delete.");
			searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
		}
	}

	/**
	 * Delete function.
	 */
	protected void deleteFunction() {

		searchDisplay.resetMessageDisplay();
		final String functionName = searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().getText();
		
		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
			final CQLFunctions toBeModifiedFuncObj = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
					.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
			showSearchingBusy(true);
			MatContext.get().getMeasureService().deleteFunctions(MatContext.get().getCurrentMeasureId(),
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
									MatContext.get().setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
									searchDisplay.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().updateFunctionMap();
									searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText("");
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);
									searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText("");
									searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().setText("");
									searchDisplay.getCqlFunctionsView().getFunctionArgNameMap().clear();
									searchDisplay.getCqlFunctionsView().getFunctionArgumentList().clear();
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
									searchDisplay.getCqlLeftNavBarPanelView()
											.setCurrentSelectedFunctionArgumentObjId(null);
									searchDisplay.getCqlLeftNavBarPanelView()
											.setCurrentSelectedFunctionArgumentName(null);
									searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
									searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
									searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
									// Commenting below code as its taking away focus and that makes our application
									// not 508 compliant with other fields.
									// searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().redisplay();
									searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().setAnnotations();
									searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton()
											.setEnabled(false);
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getSuccessfulFunctionRemoveMessage(
													toBeModifiedFuncObj.getName()));
									searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");
									if (result.getFunction() != null) {
										searchDisplay.getCqlFunctionsView().createAddArgumentViewForFunctions(
												new ArrayList<CQLFunctionArgument>(),
												MatContext.get().getMeasureLockService().checkForEditPermission());
									}
								} else if (result.getFailureReason() == 2) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
											.createAlert("Unable to find Node to modify.");
									searchDisplay.getCqlFunctionsView().getFuncNameTxtArea()
											.setText(functionName.trim());
								} else if (result.getFailureReason() == 4) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
											.createAlert("Unauthorized delete operation.");
									searchDisplay.getCqlFunctionsView().getFuncNameTxtArea()
											.setText(functionName.trim());
								}

							}
							showSearchingBusy(false);
							// 508 changes for Functions Section
							searchDisplay.getCqlLeftNavBarPanelView()
									.setFocus(searchDisplay.getCqlFunctionsView().getMainFunctionVerticalPanel());
						}
					});
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select a function to delete.");
			searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
		}
		
	}

	protected void deleteFunctionArgument() {

		String funcArgName = null;

		searchDisplay.resetMessageDisplay();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		Iterator<CQLFunctionArgument> iterator = searchDisplay.getCqlFunctionsView().getFunctionArgumentList()
				.iterator();
		searchDisplay.getCqlFunctionsView().getFunctionArgNameMap().remove(
				searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentName().toLowerCase());
		while (iterator.hasNext()) {
			CQLFunctionArgument cqlFunArgument = iterator.next();
			if (cqlFunArgument.getId()
					.equals(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId())) {

				iterator.remove();
				searchDisplay.getCqlFunctionsView().createAddArgumentViewForFunctions(
						searchDisplay.getCqlFunctionsView().getFunctionArgumentList(),
						MatContext.get().getMeasureLockService().checkForEditPermission());
				funcArgName = cqlFunArgument.getArgumentName();
				break;
			}
		}

		// resetting name and id
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		// 508 changes for Functions Section
		searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCqlFunctionsView().getFuncNameTxtArea());

		searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
				MatContext.get().getMessageDelegate().getSuccessfulFunctionArgumentRemoveMessage(funcArgName));
	}

	/**
	 * Delete parameter.
	 */
	protected void deleteParameter() {

		searchDisplay.resetMessageDisplay();
		final String parameterName = searchDisplay.getCQLParametersView().getParameterNameTxtArea().getText();
		
		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
			final CQLParameter toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap()
					.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());
			if (toBeModifiedParamObj.isReadOnly()) {
				searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert("Unauthorized delete operation.");
				searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
			} else {
				showSearchingBusy(true);
				MatContext.get().getMeasureService().deleteParameter(MatContext.get().getCurrentMeasureId(),
						toBeModifiedParamObj, searchDisplay.getCqlLeftNavBarPanelView().getViewParameterList(),
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
										searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText("");
										searchDisplay.getCQLParametersView().getParameterAceEditor().setText("");
										searchDisplay.getCqlLeftNavBarPanelView()
												.setCurrentSelectedParamerterObjId(null);
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
										searchDisplay.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
										// Commenting below code as its taking away focus and that makes our application
										// not 508 compliant with other fields.
										// searchDisplay.getCQLParametersView().getParameterAceEditor().redisplay();
										searchDisplay.getCQLParametersView().getParameterAceEditor().setAnnotations();
										searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setEnabled(false);
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate()
														.getSuccessfulParameterRemoveMessage(
																toBeModifiedParamObj.getName()));
									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unable to find Node to modify.");
										searchDisplay.getCQLParametersView().getParameterNameTxtArea()
												.setText(parameterName.trim());
									} else if (result.getFailureReason() == 4) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unauthorized delete operation.");
										searchDisplay.getCQLParametersView().getParameterNameTxtArea()
												.setText(parameterName.trim());
									}
								}
								showSearchingBusy(false);
								// 508 change to parameter section
								searchDisplay.getCqlLeftNavBarPanelView()
										.setFocus(searchDisplay.getCQLParametersView().getMainParamViewVerticalPanel());
							}
						});
			}
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert("Please select parameter to delete.");
			searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
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
			MatContext.get().getMeasureService().deleteInclude(MatContext.get().getCurrentMeasureId(),
					toBeModifiedIncludeObj, searchDisplay.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
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
									searchDisplay.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(filterComponentMeasuresFromIncludedLibraries(result.getCqlModel().getCqlIncludeLibrarys()));
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

									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getSuccessfulIncludeRemoveMessage(
													toBeModifiedIncludeObj.getAliasName()));
								} else if (result.getFailureReason() == 2) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
											.createAlert("Unable to find Node to modify.");
									searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
								}
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

	private void deleteCode() {
		searchDisplay.resetMessageDisplay();
		showSearchingBusy(true);
		MatContext.get().getMeasureService().deleteCode(
				searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedCodesObjId(),
				MatContext.get().getCurrentMeasureId(), new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
						showSearchingBusy(false);
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						showSearchingBusy(false);
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
						if (result.isSuccess()) {
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate()
											.getSUCCESSFUL_CODE_REMOVE_MSG(result.getCqlCode().getCodeOID()));
							searchDisplay.getCodesView().resetCQLCodesSearchPanel();
							appliedCodeTableList.clear();
							appliedCodeTableList.addAll(result.getCqlCodeList());
							searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
							searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList,
									MatContext.get().getMeasureLockService().checkForEditPermission());
							// Temporary fix to update codes for insert Icon.
							getAppliedValueSetList();
						} else {

							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
									.createAlert("Unable to delete.");

						}
						// 508 : Shift focus to code search panel.
						searchDisplay.getCqlLeftNavBarPanelView()
								.setFocus(searchDisplay.getCodesView().getCodeSearchInput());
					}
				});
	}

	private void checkAndDeleteValueSet() {
		MatContext.get().getMeasureService().getCQLAppliedQDMFromMeasureXml(MatContext.get().getCurrentMeasureId(),
				false, new AsyncCallback<CQLQualityDataModelWrapper>() {

					@Override
					public void onSuccess(final CQLQualityDataModelWrapper result) {
						appliedValueSetTableList.clear();
						if (result.getQualityDataDTO() != null) {
							for (CQLQualityDataSetDTO dto : result.getQualityDataDTO()) {
								if (dto.isSuppDataElement() || dto.getOid().equals("419099009")
										|| dto.getOid().equals("21112-8")
										|| (dto.getType() != null && dto.getType().equalsIgnoreCase("code")))
									continue;
								appliedValueSetTableList.add(dto);
							}

							if (appliedValueSetTableList.size() > 0) {
								Iterator<CQLQualityDataSetDTO> iterator = appliedValueSetTableList.iterator();
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
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(null);
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

	private void copyValueSets() {
		searchDisplay.resetMessageDisplay();
		if (searchDisplay.getValueSetView().getQdmSelectedList() != null &&
				searchDisplay.getValueSetView().getQdmSelectedList().size() > 0) {
			mat.model.GlobalCopyPasteObject gbCopyPaste = new GlobalCopyPasteObject();
			gbCopyPaste.setCopiedValueSetList(searchDisplay.getValueSetView().getQdmSelectedList());
			MatContext.get().setGlobalCopyPaste(gbCopyPaste);
			searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().VALUE_SETS_COPIED_SUCCESSFULLY);
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getCOPY_QDM_SELECT_ATLEAST_ONE());
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
	 * paste Value Sets. This functionality is to paste all the Value sets elements
	 * that have been copied from any Measure and can be pasted to any measure.
	 */
	private void pasteValueSets() {
		showSearchingBusy(true);
		searchDisplay.resetMessageDisplay();
		GlobalCopyPasteObject gbCopyPaste = MatContext.get().getGlobalCopyPaste();
		if ((gbCopyPaste != null) && (gbCopyPaste.getCopiedValueSetList().size() > 0)) {
			List<CQLValueSetTransferObject> cqlValueSetTransferObjectsList = searchDisplay.getValueSetView()
					.setMatValueSetListForValueSets(gbCopyPaste.getCopiedValueSetList(), appliedValueSetTableList);
			if (cqlValueSetTransferObjectsList.size() > 0) {
				MatContext.get().getMeasureService().saveValueSetList(cqlValueSetTransferObjectsList,
						appliedValueSetTableList, MatContext.get().getCurrentMeasureId(),
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
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getSUCCESSFULLY_VALUESET_PASTE());
								}
							}
						});
			} else {
				showSearchingBusy(false);
				searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getSUCCESSFULLY_VALUESET_PASTE());
			}
			MatContext.get().getGlobalCopyPaste().getCopiedValueSetList().clear();
		} else {
			showSearchingBusy(false);
			searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getWARNING_PASTING_IN_VALUESET());
		}

	}

	/**
	 * Adds the QDM Search Panel event Handlers.
	 */
	private void addValueSetSearchPanelHandlers() {

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
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
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
				
				CQLAppliedValueSetUtility.loadReleases(searchDisplay.getValueSetView().getReleaseListBox(), searchDisplay.getValueSetView().getProgramListBox());
				
				alert508StateChanges();
			}
		});

		/**
		 * this functionality is to clear the content on the ValueSet Search Panel.
		 */
		searchDisplay.getValueSetView().getCancelQDMButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				isModified = false;
				searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
				//508 compliance for Value Sets
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				
				previousIsProgramReleaseBoxEnabled = isProgramReleaseBoxEnabled;
				isProgramReleaseBoxEnabled = true; 
				
				loadProgramsAndReleases(); 
				alert508StateChanges();
			}
		});

		searchDisplay.getValueSetView().getUpdateFromVSACButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					updateVSACValueSets();
					// 508 compliance for Value Sets
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				}
			}
		});

		/**
		 * this functionality is to retrieve the value set from VSAC with latest
		 * information which consists of Expansion Profile list and Version List.
		 */
		searchDisplay.getValueSetView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();					
					String release;
					String expansionProfile = null;
					
					release = searchDisplay.getValueSetView().getReleaseListBox().getSelectedValue();
					release = MatContext.PLEASE_SELECT.equals(release) ? null : release;
					
					String program = searchDisplay.getValueSetView().getProgramListBox().getSelectedValue();
					program = MatContext.PLEASE_SELECT.equals(program) ? null : program;
					
					if(null == release && null != program) {
						HashMap<String, String> pgmProfileMap = (HashMap<String, String>) MatContext.get().getProgramToLatestProfile();
						expansionProfile = pgmProfileMap.get(program);
					}
					if(release != null && program == null) {
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(SharedCQLWorkspaceUtility.MUST_HAVE_PROGRAM_WITH_RELEASE);
					} else {
						searchValueSetInVsac(release, expansionProfile);
						// 508 compliance for Value Sets
						searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
					}
				}
			}
		});

		/**
		 * this handler is invoked when apply button is clicked on search Panel in Value
		 * Set tab and this is to add new value set or user Defined Value Set to the
		 * Applied Value Set list.
		 */
		searchDisplay.getValueSetView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					MatContext.get().clearDVIMessages();
					searchDisplay.resetMessageDisplay();

					if (isModified && (modifyValueSetDTO != null)) {
						modifyValueSetOrUserDefined(isUserDefined);
					} else {
						addNewValueSet(isUserDefined);
					}
					// 508 compliance for Value Sets
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				}
			}
		});

		/**
		 * Adding value Change handler for UserDefined Input in Search Panel in QDM
		 * Elements Tab
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
		 * Adding value change handler for OID input in Search Panel in QDM elements Tab
		 */

		searchDisplay.getValueSetView().getOIDInput().addValueChangeHandler(event -> clearOID());
		
		searchDisplay.getValueSetView().getOIDInput().sinkBitlessEvent("input");
		
		searchDisplay.getValueSetView().getClearButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (!searchDisplay.getValueSetView().getIsLoading()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getValueSetView().clearSelectedCheckBoxes();
				}
			}
		});

		addValueSetObserverHandler();
	}
		
	private void clearOID() {

		previousIsRetrieveButtonEnabled = isRetrieveButtonEnabled;
		previousIsProgramReleaseBoxEnabled = isProgramReleaseBoxEnabled;
		
		searchDisplay.resetMessageDisplay();		
		isUserDefined = searchDisplay.getValueSetView().validateOIDInput();
		
		if (searchDisplay.getValueSetView().getOIDInput().getValue().length() <= 0 ) {
			isRetrieveButtonEnabled = true;
			isProgramReleaseBoxEnabled = true;
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
		
		helpTextBuilder.append(build508HelpString(previousIsProgramReleaseBoxEnabled, isProgramReleaseBoxEnabled, "Program and Release List Boxes"));
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

	private void addCodeSearchPanelHandlers() {
		searchDisplay.getCodesView().getCopyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				copyCodes();
			}
		});

		searchDisplay.getCodesView().getPasteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					pasteCodes();
				} else {
					event.preventDefault();
				}
			}
		});
		
		searchDisplay.getCodesView().getSelectAllButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectAllCodes();
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
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					if (!isCodeModified)
						searchCQLCodesInVsac();
					// 508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeInput());
				}
			}
		});

		searchDisplay.getCodesView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					MatContext.get().clearDVIMessages();
					searchDisplay.resetMessageDisplay();
					if(isCodeModified && modifyCQLCode != null) {
						modifyCodes();
					} else if (null != searchDisplay.getCodesView().getCodeSearchInput().getValue() 
							&& !searchDisplay.getCodesView().getCodeSearchInput().getValue().isEmpty()) {
						addNewCodes();	
					}
					// 508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeInput());
				}
			}
		});

		searchDisplay.getCodesView().getCancelCodeButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					isCodeModified = false;
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCodesView().resetCQLCodesSearchPanel();
					// 508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView()
							.setFocus(searchDisplay.getCodesView().getCodeSearchInput());
				}
			}
		});

		searchDisplay.getCodesView().setDelegator(new Delegator() {

			@Override
			public void onDeleteClicked(CQLCode result, int index) {
				if (!searchDisplay.getCodesView().getIsLoading()) {
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
					if (result != null) {
						isCodeModified = false;
						modifyCQLCode = null;
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(result.getId());
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate()
										.getDELETE_CONFIRMATION_CODES(result.getCodeOID()));
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
						// 508 Compliance for Codes section
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

	private void onModifyCode(CQLCode cqlCode) {
		CQLCodesView codesView = searchDisplay.getCodesView();
		codesView.getCodeSearchInput().setEnabled(false);
		codesView.getRetrieveFromVSACButton().setEnabled(false);
		codesView.getCodeSearchInput().setValue(cqlCode.getCodeIdentifier());
		codesView.getSuffixTextBox().setValue(cqlCode.getSuffix());
		codesView.getCodeDescriptorInput().setValue(cqlCode.getName());
		codesView.getCodeInput().setValue(cqlCode.getCodeOID());
		codesView.getCodeSystemInput().setValue(cqlCode.getCodeSystemName());
		codesView.setCodeSystemOid(cqlCode.getCodeSystemOID());
		codesView.getCodeSystemVersionInput().setValue(cqlCode.getCodeSystemVersion());
		codesView.getIncludeCodeSystemVersionCheckBox().setValue(cqlCode.isIsCodeSystemVersionIncluded());
		codesView.getSaveButton().setEnabled(true);
	}
	
	private void copyCodes() {
		searchDisplay.resetMessageDisplay();
		if (searchDisplay.getCodesView().getCodesSelectedList() != null && searchDisplay.getCodesView().getCodesSelectedList().size() > 0) {
			mat.model.GlobalCopyPasteObject gbCopyPaste = new GlobalCopyPasteObject();
			gbCopyPaste.setCopiedCodeList(searchDisplay.getCodesView().getCodesSelectedList());
			MatContext.get().setGlobalCopyPaste(gbCopyPaste);
			searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().CODES_COPIED_SUCCESSFULLY);
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getCOPY_CODE_SELECT_ATLEAST_ONE());
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
	 * paste Value Sets. This functionality is to paste all the Value sets elements
	 * that have been copied from any Measure and can be pasted to any measure.
	 */
	private void pasteCodes() {
		searchDisplay.resetMessageDisplay();
		GlobalCopyPasteObject gbCopyPaste = MatContext.get().getGlobalCopyPaste();
		if ((gbCopyPaste != null) && (gbCopyPaste.getCopiedCodeList().size() > 0)) {
			List<CQLCode> codesToPaste = searchDisplay.getCodesView().setMatCodeList(gbCopyPaste.getCopiedCodeList(),
					appliedCodeTableList);
			if (codesToPaste.size() > 0) {
				String measureId = MatContext.get().getCurrentMeasureId();
				showSearchingBusy(true);
				service.saveCQLCodeListToMeasure(codesToPaste, measureId, new AsyncCallback<SaveUpdateCQLResult>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
								MatContext.get().getMessageDelegate().SUCCESSFULLY_PASTED_CODES_IN_MEASURE);
						searchDisplay.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);
						searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList,
								MatContext.get().getMeasureLockService().checkForEditPermission());
						if (result != null && result.getCqlModel().getAllValueSetAndCodeList() != null) {
							setAppliedValueSetListInTable(result.getCqlModel().getAllValueSetAndCodeList());
						}
						showSearchingBusy(false);
					}
				});
			} else {
				showSearchingBusy(false);
				searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().SUCCESSFULLY_PASTED_CODES_IN_MEASURE);
			}
			MatContext.get().getGlobalCopyPaste().getCopiedCodeList().clear();
		} else {
			showSearchingBusy(false);
			searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().CLIPBOARD_DOES_NOT_CONTAIN_CODES);
		}
	}

	/**
	 * modify codes
	 */
	private void modifyCodes() {
		String measureId = MatContext.get().getCurrentMeasureId();
		final String codeName = StringUtility.removeEscapedCharsFromString(searchDisplay.getCodesView().getCodeDescriptorInput().getValue());
		CQLCode refCode = buildCQLCodeFromCodesView(codeName);
		
		MatCodeTransferObject transferObject = searchDisplay.getCodesView().getCodeTransferObject(measureId, refCode);
		
		if (null != transferObject) {
			
			appliedCodeTableList.removeIf(code -> code.getDisplayName().equals(modifyCQLCode.getDisplayName()));
			
			if(!searchDisplay.getCodesView().checkCodeInAppliedCodeTableList(refCode.getDisplayName(), appliedCodeTableList)) {

				showSearchingBusy(true);
				service.modifyCQLCodeInMeasure(modifyCQLCode, refCode, measureId, new AsyncCallback<SaveUpdateCQLResult>() {
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
						searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getMeasureLockService().checkForEditPermission());
						searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						if (result.getCqlModel().getAllValueSetAndCodeList() != null) {
							setAppliedValueSetListInTable(result.getCqlModel().getAllValueSetAndCodeList());
						}
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
	
	private void addNewCodes() {
		searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
		searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
		String measureId = MatContext.get().getCurrentMeasureId();
		final String codeName = StringUtility.removeEscapedCharsFromString(searchDisplay.getCodesView().getCodeDescriptorInput().getValue());
		CQLCode refCode = buildCQLCodeFromCodesView(codeName);

		final String codeSystemName = refCode.getCodeSystemName();
		final String codeId = refCode.getCodeOID();

		MatCodeTransferObject transferObject = searchDisplay.getCodesView().getCodeTransferObject(measureId, refCode);

		if (null != transferObject) {
			showSearchingBusy(true);
			service.saveCQLCodestoMeasure(transferObject, new AsyncCallback<SaveUpdateCQLResult>() {

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					showSearchingBusy(false);
					if (result.isSuccess()) {
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate()
										.getCodeSuccessMessage(searchDisplay.getCodesView().getCodeInput().getText()));
						searchDisplay.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList,
								MatContext.get().getMeasureLockService().checkForEditPermission());
						// Temporary fix to update codes for insert Icon.
						getAppliedValueSetList();
					} else {
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
						if (result.getFailureReason() == result.getDuplicateCode()) {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
									MatContext.get().getMessageDelegate().generateDuplicateErrorMessage(codeName));
							searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList,
									MatContext.get().getMeasureLockService().checkForEditPermission());
						}

						else if (result.getFailureReason() == result.getBirthdateOrDeadError()) {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext
									.get().getMessageDelegate().getBirthdateOrDeadMessage(codeSystemName, codeId));

							searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList,
									MatContext.get().getMeasureLockService().checkForEditPermission());
						}
					}
					// 508 : Shift focus to code search panel.
					searchDisplay.getCqlLeftNavBarPanelView()
							.setFocus(searchDisplay.getCodesView().getCodeSearchInput());
					searchDisplay.getCodesView().getSaveButton().setEnabled(!result.isSuccess());

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
		CQLCodesView codesView = searchDisplay.getCodesView();
		boolean isCodeSystemVersionIncluded = codesView.getIncludeCodeSystemVersionCheckBox().getValue();
		refCode.setCodeOID(codesView.getCodeInput().getValue());
		refCode.setName(StringUtility.removeEscapedCharsFromString(codesView.getCodeDescriptorInput().getValue()));
		refCode.setCodeSystemName(codesView.getCodeSystemInput().getValue());
		refCode.setCodeSystemVersion(codesView.getCodeSystemVersionInput().getValue());
		refCode.setCodeIdentifier(codesView.getCodeSearchInput().getValue());
		refCode.setCodeSystemOID(codesView.getCodeSystemOid());
		refCode.setIsCodeSystemVersionIncluded(isCodeSystemVersionIncluded);
		
		if (!codesView.getSuffixTextBox().getValue().isEmpty()) {
			refCode.setSuffix(codesView.getSuffixTextBox().getValue());
			refCode.setDisplayName(codeName + " (" + codesView.getSuffixTextBox().getValue() + ")");
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
		service.updateCQLVSACValueSets(MatContext.get().getCurrentMeasureId(), null,
				new AsyncCallback<VsacApiResult>() {

					@Override
					public void onFailure(final Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(final VsacApiResult result) {

						if (result.isSuccess()) {
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
									.createAlert(MatContext.get().getMessageDelegate().getVSAC_UPDATE_SUCCESSFULL());
							List<CQLQualityDataSetDTO> appliedListModel = new ArrayList<CQLQualityDataSetDTO>();
							for (CQLQualityDataSetDTO cqlQDMDTO : result.getUpdatedCQLQualityDataDTOLIst()) {
								if (!ConstantMessages.DEAD_OID.equals(cqlQDMDTO.getDataType())
										&& !ConstantMessages.BIRTHDATE_OID.equals(cqlQDMDTO.getDataType())
										&& (cqlQDMDTO.getType() == null)) {
									appliedListModel.add(cqlQDMDTO);
									// Update existing Table value set list
									for (CQLQualityDataSetDTO cqlQualityDataSetDTO : appliedValueSetTableList) {
										if (cqlQualityDataSetDTO.getId().equals(cqlQDMDTO.getId())) {
											cqlQualityDataSetDTO
													.setOriginalCodeListName(cqlQDMDTO.getOriginalCodeListName());
											cqlQualityDataSetDTO.setName(cqlQDMDTO.getName());
										}
									}
									// update existing Value set list for Insert Button and short cut keys
									for (CQLIdentifierObject cqlIdentifierObject : MatContext.get().getValuesets()) {
										if (cqlIdentifierObject.getId().equals(cqlQDMDTO.getId())) {
											cqlIdentifierObject.setIdentifier(cqlQDMDTO.getName());
										}
									}
									// Update value set list for Attribute builder.
									for (CQLQualityDataSetDTO dataSetDTO : MatContext.get()
											.getValueSetCodeQualityDataSetList()) {
										if (dataSetDTO.getId().equals(cqlQDMDTO.getId())) {
											dataSetDTO.setOriginalCodeListName(cqlQDMDTO.getOriginalCodeListName());
											dataSetDTO.setName(cqlQDMDTO.getName());
										}
									}
								}
							}
							searchDisplay.getValueSetView().buildAppliedValueSetCellTable(appliedListModel,
									MatContext.get().getMeasureLockService().checkForEditPermission());
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

	
	private void searchCQLCodesInVsac() {

		final String url = searchDisplay.getCodesView().getCodeSearchInput().getValue().trim();
		searchDisplay.getCodesView().getCodeSearchInput().setText(url);
		searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
		if (!MatContext.get().isUMLSLoggedIn()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);

			return;
		}

		// OID validation.
		if ((url == null) || url.trim().isEmpty()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getUMLS_CODE_IDENTIFIER_REQUIRED());

			return;
		}

		// Code Identifier validation to check Identifier starts with "CODE:"
		if (validator.validateForCodeIdentifier(url)) {
			searchDisplay.getCodesView().getSaveButton().setEnabled(false);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getUMLS_INVALID_CODE_IDENTIFIER());

			return;
		} else {
			retrieveCodeReferences(url);
		}

	}

	/**
	 * Retrieve code references.
	 *
	 * @param url
	 *            the url
	 */
	private void retrieveCodeReferences(String url) {

		showSearchingBusy(true);

		vsacapiService.getDirectReferenceCode(url, new AsyncCallback<VsacApiResult>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);

			}

			@Override
			public void onSuccess(VsacApiResult result) {

				if (result.isSuccess()) {
					searchDisplay.getCodesView().getCodeDescriptorInput()
							.setValue(result.getDirectReferenceCode().getCodeDescriptor());
					searchDisplay.getCodesView().getCodeInput().setValue(result.getDirectReferenceCode().getCode());
					searchDisplay.getCodesView().getCodeSystemInput()
							.setValue(result.getDirectReferenceCode().getCodeSystemName());
					searchDisplay.getCodesView().getCodeSystemVersionInput()
							.setValue(result.getDirectReferenceCode().getCodeSystemVersion());
					searchDisplay.getCodesView().setCodeSystemOid(result.getDirectReferenceCode().getCodeSystemOid());
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
							"Code " + result.getDirectReferenceCode().getCode() + " successfully retrieved from VSAC.");
					searchDisplay.getCodesView().getSaveButton().setEnabled(true);
					
					CQLCode code = buildCQLCodeFromCodesView(StringUtility.removeEscapedCharsFromString(searchDisplay.getCodesView().getCodeDescriptorInput().getValue()));
					searchDisplay.getCodesView().setValidateCodeObject(code);
					
				} else {
					String message = searchDisplay.getCodesView().convertMessage(result.getFailureReason());
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(message);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
				}
				
				showSearchingBusy(false);
				// 508 : Shift focus to code search panel.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
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
		currentMatValueSet = null;
		showSearchingBusy(true);
		final String oid = searchDisplay.getValueSetView().getOIDInput().getValue();
		if (!MatContext.get().isUMLSLoggedIn()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
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

		vsacapiService.getMostRecentValueSetByOID(oid, release, expansionProfile, new AsyncCallback<VsacApiResult>() {

			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);				
				showSearchingBusy(false);
			}

			@Override
			public void onSuccess(final VsacApiResult result) {
				// to get the VSAC version list corresponding the OID
				if (result.isSuccess()) {
					String valueSetName = "";
					
					List<MatValueSet> matValueSets = result.getVsacResponse();					
					if (matValueSets != null) {						
						currentMatValueSet = matValueSets.get(0);
						valueSetName = currentMatValueSet.getDisplayName();
					}
					
					searchDisplay.getValueSetView().getOIDInput().setTitle(oid);
					searchDisplay.getValueSetView().getUserDefinedInput().setValue(valueSetName);
					searchDisplay.getValueSetView().getUserDefinedInput().setTitle(valueSetName);

					searchDisplay.getValueSetView().getSaveButton().setEnabled(true);					
					boolean isVersionEnabled = isListValueNotSelected(searchDisplay.getValueSetView().getProgramListBox().getSelectedValue()) 
							&& isListValueNotSelected(searchDisplay.getValueSetView().getReleaseListBox().getSelectedValue());
					searchDisplay.getValueSetView().getHelpBlock().setColor("transparent");
					searchDisplay.getValueSetView().getHelpBlock().setText("Version selection is ".concat(Boolean.TRUE.equals(isVersionEnabled) ? "enabled" : "disabled"));
					
					
					showSearchingBusy(false);
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate()
									.getValuesetSuccessfulReterivalMessage(matValueSets.get(0).getDisplayName()));
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);

				} else {
					String message = searchDisplay.getValueSetView().convertMessage(result.getFailureReason());
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(message);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
					showSearchingBusy(false);
				}
			}
		});
	}

	/**
	 * Adds the QDS with value set.
	 */
	private void addVSACCQLValueset() {

		String measureID = MatContext.get().getCurrentMeasureId();
		CQLValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(measureID);
		matValueSetTransferObject.scrubForMarkUp();
		final String originalCodeListName = matValueSetTransferObject.getMatValueSet().getDisplayName();
		final String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
		final String codeListName = (originalCodeListName != null ? originalCodeListName : "")
				+ (!suffix.isEmpty() ? " (" + suffix + ")" : "");

		// Check if QDM name already exists in the list.
		if (!searchDisplay.getValueSetView().checkNameInValueSetList(codeListName, appliedValueSetTableList)) {
			showSearchingBusy(true);
			MatContext.get().getMeasureService().saveCQLValuesettoMeasure(matValueSetTransferObject,
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
									previousIsProgramReleaseBoxEnabled = isProgramReleaseBoxEnabled;
									isProgramReleaseBoxEnabled = true;
									loadProgramsAndReleases(); 
									
									getAppliedValueSetList();
								} else {
									if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
												MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(
														result.getCqlQualityDataSetDTO().getName()));
									}
								}
							}
							getUsedValueSets();
							currentMatValueSet = null;
							searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
							showSearchingBusy(false);
						}
					});
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(codeListName));
		}

	}

	/**
	 * Adds the selected code listto measure.
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
	 * Adds the QDS with out value set.
	 */
	private void addUserDefinedValueSet() {

		CQLValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(
				MatContext.get().getCurrentMeasureId());
		matValueSetTransferObject.scrubForMarkUp();
		matValueSetTransferObject.setMatValueSet(null);
		if ((matValueSetTransferObject.getUserDefinedText().length() > 0)) {
			ValueSetNameInputValidator valueSetNameInputValidator = new ValueSetNameInputValidator();
			String message = valueSetNameInputValidator.validate(matValueSetTransferObject);
			if (message.isEmpty()) {
				final String userDefinedInput = matValueSetTransferObject.getCqlQualityDataSetDTO().getName();
				// Check if QDM name already exists in the list.
				if (!searchDisplay.getValueSetView().checkNameInValueSetList(userDefinedInput,
						appliedValueSetTableList)) {
					showSearchingBusy(true);
					MatContext.get().getMeasureService().saveCQLUserDefinedValuesettoMeasure(matValueSetTransferObject,
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
																.getDuplicateAppliedValueSetMsg(result
																		.getCqlQualityDataSetDTO().getName()));
											} else if (result.getFailureReason() == SaveUpdateCQLResult.SERVER_SIDE_VALIDATION) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
														.createAlert("Invalid input data.");
											}
										}
									}

									getUsedValueSets();
									searchDisplay.getValueSetView().getSaveButton().setEnabled(false);
									showSearchingBusy(false);
								}
							});

				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
							MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(userDefinedInput));
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
			String displayName = (!originalName.isEmpty() ? originalName : "")
					+ (!suffix.isEmpty() ? " (" + suffix + ")" : "");
			if (modifyValueSetDTO.getVersion() == null) {
				modifyValueSetDTO.setVersion("");
			}
			
			String releaseValue = searchDisplay.getValueSetView().getReleaseListBox().getSelectedValue();
			if(releaseValue == null) {
				modifyValueSetDTO.setRelease("");
			} else if(!releaseValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
				modifyValueSetDTO.setRelease(releaseValue);
				modifyValueSetDTO.setVersion("");
			} else {
				modifyValueSetDTO.setRelease("");
			}
			
			String programValue = searchDisplay.getValueSetView().getProgramListBox().getSelectedValue();
			if(!programValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
				modifyValueSetDTO.setProgram(programValue);
			} else {
				modifyValueSetDTO.setProgram("");
			}
			
			modifyValueSetList(modifyValueSetDTO);
			if (!searchDisplay.getValueSetView().checkNameInValueSetList(displayName, appliedValueSetTableList)) {
				
				if (!searchDisplay.getValueSetView().getSuffixInput().getValue().isEmpty()) {
					modifyValueSetDTO.setSuffix(searchDisplay.getValueSetView().getSuffixInput().getValue());
					modifyValueSetDTO.setName(
							originalName + " (" + searchDisplay.getValueSetView().getSuffixInput().getValue() + ")");
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

			getUsedValueSets();
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE());
		}
	}

	/**
	 * Modify QDM with out value set.
	 */
	private void modifyUserDefinedValueSet() {
		modifyValueSetDTO.setVersion("");
		if ((searchDisplay.getValueSetView().getUserDefinedInput().getText().trim().length() > 0)) {
			String originalName = searchDisplay.getValueSetView().getUserDefinedInput().getText();
			String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
			String usrDefDisplayName = (!originalName.isEmpty() ? originalName : "")
					+ (!suffix.isEmpty() ? " (" + suffix + ")" : "");
			
			modifyValueSetList(modifyValueSetDTO);
			if (!searchDisplay.getValueSetView().checkNameInValueSetList(usrDefDisplayName, appliedValueSetTableList)) {
				
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
			} else {
				appliedValueSetTableList.add(modifyValueSetDTO);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
						MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(usrDefDisplayName));
			}

			getUsedValueSets();
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
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
		matValueSetTransferObject.setMeasureId(MatContext.get().getCurrentMeasureId());
		matValueSetTransferObject.setUserDefinedText(searchDisplay.getValueSetView().getUserDefinedInput().getText());
		matValueSetTransferObject.setMatValueSet(matValueSet);
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setCqlQualityDataSetDTO(qualityDataSetDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);

		matValueSetTransferObject.scrubForMarkUp();
		showSearchingBusy(true);
		MatContext.get().getMeasureService().updateCQLValuesetsToMeasure(matValueSetTransferObject,
				new AsyncCallback<SaveUpdateCQLResult>() {
					@Override
					public void onFailure(final Throwable caught) {
						showSearchingBusy(false);
						isModified = false;
						modifyValueSetDTO = null;
						currentMatValueSet = null;
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
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
											MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(
													result.getCqlQualityDataSetDTO().getName()));

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
	 * Gets the applied QDM list.
	 *
	 * @return the applied QDM list
	 */
	private void getAppliedValueSetList() {
		String measureId = MatContext.get().getCurrentMeasureId();
		if ((measureId != null) && !measureId.equals("")) {
			MatContext.get().getMeasureService().getCQLValusets(measureId,
					new AsyncCallback<CQLQualityDataModelWrapper>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());

						}

						@Override
						public void onSuccess(CQLQualityDataModelWrapper result) {
							setAppliedValueSetListInTable(result.getQualityDataDTO());
						}
					});
		}

	}

	/**
	 * Creates the value set transfer object.
	 *
	 * @param measureID
	 *            the measure ID
	 * @return the CQL value set transfer object
	 */
	private CQLValueSetTransferObject createValueSetTransferObject(String measureID) {
		if (currentMatValueSet == null) {
			currentMatValueSet = new MatValueSet();
		}

		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		matValueSetTransferObject.setMeasureId(measureID);

		String originalCodeListName = searchDisplay.getValueSetView().getUserDefinedInput().getValue();

		matValueSetTransferObject.setCqlQualityDataSetDTO(new CQLQualityDataSetDTO());
		matValueSetTransferObject.getCqlQualityDataSetDTO().setOriginalCodeListName(originalCodeListName);

		if (!searchDisplay.getValueSetView().getSuffixInput().getValue().isEmpty()) {
			matValueSetTransferObject.getCqlQualityDataSetDTO()
					.setSuffix(searchDisplay.getValueSetView().getSuffixInput().getValue());
			matValueSetTransferObject.getCqlQualityDataSetDTO().setName(
					originalCodeListName + " (" + searchDisplay.getValueSetView().getSuffixInput().getValue() + ")");
		} else {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setName(originalCodeListName);
		}
		
		// set it to empty string to begin with
		matValueSetTransferObject.getCqlQualityDataSetDTO().setRelease("");
		String releaseValue = searchDisplay.getValueSetView().getReleaseListBox().getSelectedValue(); 
		if(!releaseValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setRelease(releaseValue);
		}
		
		matValueSetTransferObject.getCqlQualityDataSetDTO().setProgram("");
		String programValue = searchDisplay.getValueSetView().getProgramListBox().getSelectedValue();
		if(!programValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setProgram(programValue);
		}

		
		CodeListSearchDTO codeListSearchDTO = new CodeListSearchDTO();
		codeListSearchDTO.setName(searchDisplay.getValueSetView().getUserDefinedInput().getText());
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		matValueSetTransferObject.setMatValueSet(currentMatValueSet);
		matValueSetTransferObject.setMeasureId(measureID);
		matValueSetTransferObject.setUserDefinedText(searchDisplay.getValueSetView().getUserDefinedInput().getText());
		return matValueSetTransferObject;
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
		previousIsProgramReleaseBoxEnabled = isProgramReleaseBoxEnabled;
		CQLAppliedValueSetUtility.setProgramsAndReleases(result.getProgram(), result.getRelease(), searchDisplay.getValueSetView());
		isProgramReleaseBoxEnabled = true;
	}

	/**
	 * Get CQL from Server and display in Collpsible Panel in read Only Mode.
	 */
	private void showCompleteCQL(final AceEditor aceEditor) {
		MatContext.get().getMeasureService().getMeasureCQLFileData(MatContext.get().getCurrentMeasureId(),
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
										aceEditor.addAnnotation(line - 1, column, error.getErrorMessage(),
												AceAnnotationType.ERROR);
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
		// Commenting below code as its taking away focus and that makes our application
		// not 508 compliant with other fields.
		// aceEditor.redisplay();
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
	 * Show searching busy.
	 *
	 * @param busy
	 *            the busy
	 */
	private void showSearchingBusy(final boolean busy) {
		showSearchBusyOnDoubleClick(busy);
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			switch (currentSection.toLowerCase()) {
			case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
				// This needs to be set to false to make the CQL Name un-editable under Measure.
				searchDisplay.getCqlGeneralInformationView().setWidgetReadOnlyForMeasure(!busy);
				break;
			case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
				searchDisplay.getIncludeView().setReadOnly(!busy);
				break;
			case (CQLWorkSpaceConstants.CQL_APPLIED_QDM):
				searchDisplay.getValueSetView().setReadOnly(!busy);
				break;
			case (CQLWorkSpaceConstants.CQL_CODES):
				searchDisplay.getCodesView().setReadOnly(!busy);
				break;
			case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
				searchDisplay.getCQLParametersView().setReadOnly(!busy);
				break;
			case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
				searchDisplay.getCQlDefinitionsView().setReadOnly(!busy);
				break;
			case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
				searchDisplay.getCqlFunctionsView().setReadOnly(!busy);
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
		searchDisplay.getCqlLeftNavBarPanelView().getComponentsTab().setEnabled(!busy);
	}

	/**
	 * This method sets the value sets to value set list for table and for short cut
	 * key.Also builds the table.
	 * 
	 * @param result
	 *            - {@link CQLQualityDataModelWrapper}
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
			if ((valueset.getOid().equals("419099009") || valueset.getOid().equals("21112-8")
					|| (valueset.getType() != null) && valueset.getType().equalsIgnoreCase("code"))) {
				continue;
			}
			appliedValueSetTableList.add(valueset);
		}
		searchDisplay.getValueSetView().buildAppliedValueSetCellTable(appliedValueSetTableList,
				MatContext.get().getMeasureLockService().checkForEditPermission());
		searchDisplay.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
	}
	
	private static final boolean isListValueNotSelected(String selectedValueFromList) {
		return MatContext.PLEASE_SELECT.equals(selectedValueFromList) || selectedValueFromList == null || selectedValueFromList.isEmpty();
	}

	public boolean isCQLWorkspaceValid() {
		return !(getSearchDisplay().getCqlLeftNavBarPanelView().getIsPageDirty());
	}

	private boolean isValidExpressionName(String expressionName) {
		final String trimedExpression = expressionName.trim();
		return !trimedExpression.isEmpty() && !trimedExpression.equalsIgnoreCase("Patient") && !trimedExpression.equalsIgnoreCase("Population")
				&& MatContext.get().getCqlConstantContainer() != null 
				&& MatContext.get().getCqlConstantContainer().getCqlKeywordList() != null
				&& MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlKeywordsList() != null
				&& !MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlKeywordsList().stream().anyMatch(definedKeyWord -> definedKeyWord.equalsIgnoreCase(trimedExpression));
	}
	
	//unsets all sections except general info section because that is the default section
	public void unsetAllSections() {
		searchDisplay.getCqlLeftNavBarPanelView().getComponentsTab().setActive(false);
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(false);
		searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(false);
		searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(false);
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(false);
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(false);
		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
		searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setActive(false);
	}

}