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
import org.gwtbootstrap3.client.ui.ListBox;
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
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
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
import mat.client.cqlworkspace.functions.CQLFunctionsView.Observer;
import mat.client.cqlworkspace.generalinformation.CQLGeneralInformationUtility;
import mat.client.cqlworkspace.includedlibrary.CQLIncludeLibraryView;
import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility;
import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetView;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.JSONAttributeModeUtility;
import mat.client.shared.JSONCQLTimingExpressionUtility;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.MessagePanel;
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
	private static String currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
	private static String nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
	private QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT.create(QDSAttributesService.class);
	private static CQLWorkSpaceView cqlWorkspaceView;
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
	private boolean isFormatable = true;
	private boolean isProgramReleaseBoxEnabled = true; 
	private boolean isRetrieveButtonEnabled = true; 
	private boolean isApplyButtonEnabled = false; 
	private boolean previousIsProgramReleaseBoxEnabled = true; 
	private boolean previousIsRetrieveButtonEnabled = true; 
	private boolean previousIsApplyButtonEnabled = false; 
	private MessagePanel messagePanel = new MessagePanel();
	public CQLWorkSpacePresenter(final CQLWorkSpaceView workspaceView) {
		cqlWorkspaceView = workspaceView;
		addEventHandlers();
		JSONCQLTimingExpressionUtility.getAllCQLTimingExpressionsList();
		JSONAttributeModeUtility.getAllAttrModeList();
		JSONAttributeModeUtility.getAllModeDetailsList();
	}
	
	private void buildInsertPopUp() {
		cqlWorkspaceView.resetMessageDisplay();
		InsertIntoAceEditorDialogBox.showListOfItemAvailableForInsertDialogBox(curAceEditor);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
	}

	/**
	 * Adds the event handlers.
	 */
	private void addEventHandlers() {

		ClickHandler cHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().detach();
				cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().detach();
				cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().detach();
			}
		};
		cqlWorkspaceView.getMainPanel().addDomHandler(cHandler, ClickEvent.getType());

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
		cqlWorkspaceView.getViewCQLView().getExportErrorFile().addClickHandler(new ClickHandler() {
			
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

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBoxNoButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						cqlWorkspaceView.resetMessageDisplay();
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
					}
				});

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBoxYesButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
							deleteDefinition();
							cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null
								&& cqlWorkspaceView.getCqlLeftNavBarPanelView()
										.getCurrentSelectedFunctionArgumentObjId() == null) {
							deleteFunction();
							cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (cqlWorkspaceView.getCqlLeftNavBarPanelView()
								.getCurrentSelectedFunctionArgumentObjId() != null) {
							deleteFunctionArgument();
							cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (cqlWorkspaceView.getCqlLeftNavBarPanelView()
								.getCurrentSelectedParamerterObjId() != null) {
							deleteParameter();
							cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (cqlWorkspaceView.getCqlLeftNavBarPanelView()
								.getCurrentSelectedIncLibraryObjId() != null) {
							deleteInclude();
							cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (cqlWorkspaceView.getCqlLeftNavBarPanelView()
								.getCurrentSelectedValueSetObjId() != null) {
							checkAndDeleteValueSet();
							cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						} else if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedCodesObjId() != null) {
							deleteCode();
							cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
						}
					}
				});
	}


	private void addWarningConfirmationHandlers() {

		messagePanel.getWarningConfirmationYesButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
				messagePanel.getWarningConfirmationMessageAlert().clearAlert();
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().isDoubleClick()) {
					clickEventOnListboxes();
				} else if (cqlWorkspaceView.getCqlLeftNavBarPanelView().isNavBarClick()) {
					changeSectionSelection();
				} else {
					clearViewIfDirtyNotSet();
				}
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
			}
		});

		messagePanel.getWarningConfirmationNoButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				messagePanel.getWarningConfirmationMessageAlert().clearAlert();
				// no was selected, don't move anywhere
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().isNavBarClick()) {
					unsetActiveMenuItem(nextSection);
				}
				if (currentSection.equals(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().setSelectedIndex(-1);
				} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().setSelectedIndex(-1);
				} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().setSelectedIndex(-1);
				}
			}
		});
	}


	private void addParameterSectionHandlers() {

		cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {

			@Override
			public void onShow(ShowEvent showEvent) {
				cqlWorkspaceView.resetMessageDisplay();
				showCompleteCQL(cqlWorkspaceView.getCQLParametersView().getViewCQLAceEditor());
			}
		});

		cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent hideEvent) {
				resetAceEditor(cqlWorkspaceView.getCQLParametersView().getViewCQLAceEditor());
				cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getInfoButtonGroup().getElement()
						.setAttribute("class", "btn-group");

			}
		});

		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getSaveButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
						if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
							addAndModifyParameters();
							// 508 change to parameter section
							cqlWorkspaceView.getCqlLeftNavBarPanelView()
									.setFocus(cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea());
						}
					}

				});

		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getEraseButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						cqlWorkspaceView.resetMessageDisplay();
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
						resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
						eraseParameter();
						// 508 change to parameter section
						cqlWorkspaceView.getCqlLeftNavBarPanelView()
								.setFocus(cqlWorkspaceView.getCQLParametersView().getParameterAceEditor());
					}
				});
		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_PARAMETER(
										cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().getValue()));
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();

					}

				});

		cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		// Parameter Add New Functionality
		cqlWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.resetMessageDisplay();
				resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					showUnsavedChangesWarning();
				} else {
					addNewParameter();
				}
				// 508 change to parameter section
				cqlWorkspaceView.getCqlLeftNavBarPanelView()
						.setFocus(cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea());
			}
		});

		cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				cqlWorkspaceView.resetMessageDisplay();
				cqlWorkspaceView.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.NONE);
				String comment = cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea().getText();
				if (validator.isCommentTooLongOrContainsInvalidText(comment)) {
					cqlWorkspaceView.getCQLParametersView().getParamCommentGroup()
							.setValidationState(ValidationState.ERROR);
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});
	}

	private void addNewParameter() {
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().getText() != null)) {
			cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().setText("");
		}
		if ((cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea() != null)) {
			cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText("");
		}

		if ((cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea() != null)) {
			cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea().setText("");
		}

		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			cqlWorkspaceView.getCQLParametersView()
					.setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
			cqlWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox().setText("");
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex() >= 0) {
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().setItemSelected(
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(), false);
		}

		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
	}


	private void addFunctionSectionHandlers() {

		cqlWorkspaceView.getCqlFunctionsView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {

			@Override
			public void onShow(ShowEvent showEvent) {
				cqlWorkspaceView.resetMessageDisplay();
				showCompleteCQL(cqlWorkspaceView.getCqlFunctionsView().getViewCQLAceEditor());
			}
		});

		cqlWorkspaceView.getCqlFunctionsView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent hideEvent) {
				resetAceEditor(cqlWorkspaceView.getCqlFunctionsView().getViewCQLAceEditor());
				cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement()
						.setAttribute("class", "btn-group");

			}
		});

		// Save functionality for Function section
		cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCqlFunctionsView().getPanelViewCQLCollapse());

				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addAndModifyFunction();
					// 508 changes for Functions Section
					cqlWorkspaceView.getCqlLeftNavBarPanelView()
							.setFocus(cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea());
				}

			}
		});

		// Erase functionality for Function Section
		cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.resetMessageDisplay();
				resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCqlFunctionsView().getPanelViewCQLCollapse());

				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				eraseFunction();
				// 508 changes for Functions Section
				cqlWorkspaceView.getCqlLeftNavBarPanelView()
						.setFocus(cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor());
			}
		});

		// Insert into Ace Editor functionality for Function Section
		cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getInsertButton()
				.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						buildInsertPopUp();
					}
				});

		// Delete functionality for Function Section
		cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCqlFunctionsView().getPanelViewCQLCollapse());
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
								.createMultiLineAlert(
										MatContext.get().getMessageDelegate().getDeleteConfirmationFunctionCQLWorkspace(
												cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea().getValue()));
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
					}
				});

		// Creating Arguments for Functions in Function Section
		cqlWorkspaceView.getCqlFunctionsView().getAddNewArgument().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
				resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCqlFunctionsView().getPanelViewCQLCollapse());
				cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement()
						.setAttribute("class", "btn-group");
				CQLFunctionArgument addNewFunctionArgument = new CQLFunctionArgument();
				AddFunctionArgumentDialogBox.showArgumentDialogBox(addNewFunctionArgument, false,cqlWorkspaceView.getCqlFunctionsView(), messagePanel,MatContext.get().getMeasureLockService().checkForEditPermission());
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				// 508 changes for function section
				cqlWorkspaceView.getCqlFunctionsView().getAddNewArgument().setFocus(true);
			}
		});

		cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		cqlWorkspaceView.getCqlFunctionsView().getFunctionCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		// Function Add New Functionality
		cqlWorkspaceView.getCqlFunctionsView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.resetMessageDisplay();
				resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCqlFunctionsView().getPanelViewCQLCollapse());
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					showUnsavedChangesWarning();
				} else {
					addNewFunction();
				}
				// 508 changes for Functions Section
				cqlWorkspaceView.getCqlLeftNavBarPanelView()
						.setFocus(cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea());
			}
		});

		cqlWorkspaceView.getCqlFunctionsView().getFunctionCommentTextArea().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				cqlWorkspaceView.resetMessageDisplay();
				cqlWorkspaceView.getCqlFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.NONE);
				String comment = cqlWorkspaceView.getCqlFunctionsView().getFunctionCommentTextArea().getText();
				if (validator.isCommentTooLongOrContainsInvalidText(comment)) {
					cqlWorkspaceView.getCqlFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});

		addFunctionSectionObserverHandlers();

	}

	private void addNewFunction() {
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList().clear();
		cqlWorkspaceView.getCqlFunctionsView().getFunctionArgNameMap().clear();
		cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
		cqlWorkspaceView.getCqlFunctionsView().createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>(),
				MatContext.get().getMeasureLockService().checkForEditPermission());
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().setText("");
		}
		if ((cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea() != null)) {
			cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea().setText("");
		}

		if ((cqlWorkspaceView.getCqlFunctionsView().getFunctionCommentTextArea() != null)) {
			cqlWorkspaceView.getCqlFunctionsView().getFunctionCommentTextArea().setText("");
		}
		cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox().setText("");
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText("");
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex() >= 0) {
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().setItemSelected(
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex(), false);
		}		
		cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn().setValue(true);
		cqlWorkspaceView.getCqlFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
		cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCqlFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
	}

	/**
	 * Adds the function section observer handlers.
	 */
	private void addFunctionSectionObserverHandlers() {

		cqlWorkspaceView.getCqlFunctionsView().setObserver(new Observer() {

			// Modify functionality for Function Arguments
			@Override
			public void onModifyClicked(CQLFunctionArgument result) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				cqlWorkspaceView.resetMessageDisplay();
				if (result.getArgumentType().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
					getAttributesForDataType(result);
				} else {
					AddFunctionArgumentDialogBox.showArgumentDialogBox(result, true, cqlWorkspaceView.getCqlFunctionsView(), messagePanel, MatContext.get().getMeasureLockService().checkForEditPermission());
				}

			}

			// Delete functionality for Function Arguments
			@Override
			public void onDeleteClicked(CQLFunctionArgument result, int index) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(result.getId());
				cqlWorkspaceView.getCqlLeftNavBarPanelView()
						.setCurrentSelectedFunctionArgumentName(result.getArgumentName());
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate()
								.getDELETE_CONFIRMATION_FUNCTION_ARGUMENT(result.getArgumentName()));
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
			}
		});

	}


	private void addDefinitionSectionHandlers() {

		cqlWorkspaceView.getCQlDefinitionsView().getPanelViewCQLCollapse().addShowHandler(new ShowHandler() {

			@Override
			public void onShow(ShowEvent showEvent) {
				cqlWorkspaceView.resetMessageDisplay();
				showCompleteCQL(cqlWorkspaceView.getCQlDefinitionsView().getViewCQLAceEditor());
			}
		});

		cqlWorkspaceView.getCQlDefinitionsView().getPanelViewCQLCollapse().addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent hideEvent) {
				cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getInfoButtonGroup().getElement()
						.setAttribute("class", "btn-group");
				resetAceEditor(cqlWorkspaceView.getCQlDefinitionsView().getViewCQLAceEditor());

			}

		});

		// Definition section Save functionality
		cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQlDefinitionsView().getPanelViewCQLCollapse());

				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addAndModifyDefintions();
					// 508 changes for Definitions Section
					cqlWorkspaceView.getCqlLeftNavBarPanelView()
							.setFocus(cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea());
				}
			}
		});

		// Definition section Insert into Ace Editor functionality
		cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getInsertButton()
				.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						buildInsertPopUp();
					}
				});

		// Definition section Erase functionality
		cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.resetMessageDisplay();
				resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQlDefinitionsView().getPanelViewCQLCollapse());

				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				eraseDefinition();
				// 508 changes for Definition Section
				cqlWorkspaceView.getCqlLeftNavBarPanelView()
						.setFocus(cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor());
			}
		});
		// Definition Delete Icon Functionality
		cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQlDefinitionsView().getPanelViewCQLCollapse());
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
								.createMultiLineAlert(MatContext.get().getMessageDelegate()
										.getDeleteConfirmationDefinitionCQLWorkspace(cqlWorkspaceView
												.getCQlDefinitionsView().getDefineNameTxtArea().getValue()));
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();

					}
				});

		cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		cqlWorkspaceView.getCQlDefinitionsView().getDefineCommentTextArea().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		// Definition Add New Functionality
		cqlWorkspaceView.getCQlDefinitionsView().getAddNewButtonBar().getaddNewButton()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						cqlWorkspaceView.resetMessageDisplay();
						resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCQlDefinitionsView().getPanelViewCQLCollapse());

						cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
						if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
							showUnsavedChangesWarning();
						} else {
							addNewDefinition();
						}
						// 508 changes for Definitions Section
						cqlWorkspaceView.getCqlLeftNavBarPanelView()
								.setFocus(cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea());
					}
				});

		cqlWorkspaceView.getCQlDefinitionsView().getDefineCommentTextArea().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				cqlWorkspaceView.resetMessageDisplay();
				cqlWorkspaceView.getCQlDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.NONE);
				String comment = cqlWorkspaceView.getCQlDefinitionsView().getDefineCommentTextArea().getText();
				if (validator.isCommentTooLongOrContainsInvalidText(comment)) {
					cqlWorkspaceView.getCQlDefinitionsView().getDefineCommentGroup()
							.setValidationState(ValidationState.ERROR);
					messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				}
			}
		});
	}

	private void addNewDefinition() {
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().getText() != null)) {
			cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().setText("");
		}
		if ((cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea() != null)) {
			cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().setText("");
		}

		if ((cqlWorkspaceView.getCQlDefinitionsView().getDefineCommentTextArea() != null)) {
			cqlWorkspaceView.getCQlDefinitionsView().getDefineCommentTextArea().setText("");
		}
		cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox().setText("");
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox().setText("");
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex() >= 0) {
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().setItemSelected(
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex(), false);
		}

		// Functionality to reset the disabled features for supplemental data
		// definitions when erased.
		cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().setEnabled(true);
		cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().setReadOnly(false);
		cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePATRadioBtn().setEnabled(true);
		cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setEnabled(true);
		cqlWorkspaceView.getCQlDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getSaveButton().setEnabled(true);
		cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getInsertButton().setEnabled(true);
		cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getTimingExpButton().setEnabled(true);
		cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
		cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);

	}

	/**
	 * Adds the list box event handler.
	 */
	private void addListBoxEventHandler() {
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);

					if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
						showUnsavedChangesWarning();
					} else {
						int selectedIndex = cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsListBox().getSelectedIndex();
						if (selectedIndex != -1) {
							final String selectedComponentObjectId = cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsListBox().getValue(selectedIndex);
							if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentMap().get(selectedComponentObjectId) != null) {
								cqlWorkspaceView.getComponentView().setPageInformation(cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentMap().get(selectedComponentObjectId));
							}
						}
						cqlWorkspaceView.resetMessageDisplay();
					}
				}
			}
		});

		// Double Click Handler Event for Parameter Name ListBox in Parameter Section
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox()
				.addDoubleClickHandler(new DoubleClickHandler() {
					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsLoading()) {
							event.stopPropagation();
						} else {
							showSearchBusyOnDoubleClick(true);
							cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
							cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
							resetAceEditor(cqlWorkspaceView.getCQLParametersView().getViewCQLAceEditor());
							resetViewCQLCollapsiblePanel(
									cqlWorkspaceView.getCQLParametersView().getPanelViewCQLCollapse());

							cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
							cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
							if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
								showSearchBusyOnDoubleClick(false);
								showUnsavedChangesWarning();
							} else {
								int selectedIndex = cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox()
										.getSelectedIndex();
								if (selectedIndex != -1) {
									final String selectedParamID = cqlWorkspaceView.getCqlLeftNavBarPanelView()
											.getParameterNameListBox().getValue(selectedIndex);
									cqlWorkspaceView.getCqlLeftNavBarPanelView()
											.setCurrentSelectedParamerterObjId(selectedParamID);
									if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterMap()
											.get(selectedParamID) != null) {
										cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setTitle("Delete");
										cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setEnabled(false);
										cqlWorkspaceView.getCQLParametersView().setWidgetReadOnly(false);

										cqlWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton()
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
														cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea()
																.setText(cqlWorkspaceView.getCqlLeftNavBarPanelView()
																		.getParameterMap().get(selectedParamID)
																		.getName());
														cqlWorkspaceView.getCQLParametersView().getParameterAceEditor()
																.setText(cqlWorkspaceView.getCqlLeftNavBarPanelView()
																		.getParameterMap().get(selectedParamID)
																		.getLogic());
														cqlWorkspaceView.getCQLParametersView()
																.getParameterCommentTextArea()
																.setText(cqlWorkspaceView.getCqlLeftNavBarPanelView()
																		.getParameterMap().get(selectedParamID)
																		.getCommentString());
														CQLParameter currentParameter = cqlWorkspaceView
																.getCqlLeftNavBarPanelView().getParameterMap()
																.get(selectedParamID);
														boolean isReadOnly = cqlWorkspaceView.getCqlLeftNavBarPanelView()
																.getParameterMap().get(selectedParamID).isReadOnly();

														if (MatContext.get().getMeasureLockService()
																.checkForEditPermission()) {
															cqlWorkspaceView.getCQLParametersView()
																	.setWidgetReadOnly(!isReadOnly);

														if (!currentParameter.isReadOnly()) {
																if (!result.getCqlErrors().isEmpty()
																		|| !result.getUsedCQLParameters().contains(
																				currentParameter.getName())) {
																	cqlWorkspaceView.getCQLParametersView()
																			.getParameterButtonBar().getDeleteButton()
																			.setEnabled(true);

																} else {
																	cqlWorkspaceView.getCQLParametersView()
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
															cqlWorkspaceView.getCQLParametersView().getParameterAceEditor()
																	.clearAnnotations();
															cqlWorkspaceView.getCQLParametersView().getParameterAceEditor()
																	.removeAllMarkers();
															for (CQLErrors error : errorList) {
																int startLine = error.getStartErrorInLine();
																int startColumn = error.getStartErrorAtOffset();
																cqlWorkspaceView.getCQLParametersView()
																		.getParameterAceEditor()
																		.addAnnotation(startLine, startColumn,
																				error.getErrorMessage(),
																				AceAnnotationType.ERROR);
															}
															cqlWorkspaceView.getCQLParametersView().getParameterAceEditor()
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

								cqlWorkspaceView.resetMessageDisplay();
							}
						}
						// 508 change to parameter section
						cqlWorkspaceView.getCqlLeftNavBarPanelView()
								.setFocus(cqlWorkspaceView.getCQLParametersView().getMainParamViewVerticalPanel());
					}
				});

		// Double Click Handler Event for Definition Name ListBox in Defintion Section
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox()
				.addDoubleClickHandler(new DoubleClickHandler() {
					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsLoading()) {
							event.stopPropagation();
						} else {

							showSearchBusyOnDoubleClick(true);

							cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
							cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().removeAllMarkers();
							resetAceEditor(cqlWorkspaceView.getCQlDefinitionsView().getViewCQLAceEditor());
							resetViewCQLCollapsiblePanel(
									cqlWorkspaceView.getCQlDefinitionsView().getPanelViewCQLCollapse());
							cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox().setText("");

							cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
							if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
								showSearchBusyOnDoubleClick(false);
								showUnsavedChangesWarning();
							} else {
								int selectedIndex = cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox()
										.getSelectedIndex();
								if (selectedIndex != -1) {
									final String selectedDefinitionID = cqlWorkspaceView.getCqlLeftNavBarPanelView()
											.getDefineNameListBox().getValue(selectedIndex);
									cqlWorkspaceView.getCqlLeftNavBarPanelView()
											.setCurrentSelectedDefinitionObjId(selectedDefinitionID);
									if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionMap()
											.get(selectedDefinitionID) != null) {

										// disable definitionName and fields for
										// Supplemental data definitions
										cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
												.setTitle("Delete");

										cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
												.setEnabled(false);
										cqlWorkspaceView.getCQlDefinitionsView().setWidgetReadOnly(false);
										cqlWorkspaceView.getCQlDefinitionsView().getAddNewButtonBar().getaddNewButton()
												.setEnabled(MatContext.get().getMeasureLockService()
														.checkForEditPermission());
										// load most recent used cql artifacts
										MatContext.get().getMeasureService().getUsedCQLArtifacts(
												MatContext.get().getCurrentMeasureId(),
												new AsyncCallback<GetUsedCQLArtifactsResult>() {

													@Override
													public void onFailure(Throwable caught) {
														showSearchBusyOnDoubleClick(false);
														cqlWorkspaceView.getCQlDefinitionsView().setWidgetReadOnly(
																MatContext.get().getMeasureLockService()
																		.checkForEditPermission());
														Window.alert(MatContext.get().getMessageDelegate()
																.getGenericErrorMessage());
													}

													@Override
													public void onSuccess(GetUsedCQLArtifactsResult result) {
														showSearchBusyOnDoubleClick(false);
														boolean isReadOnly = cqlWorkspaceView.getCqlLeftNavBarPanelView()
																.getDefinitionMap().get(selectedDefinitionID)
																.isSupplDataElement();
														CQLDefinition currentDefinition = cqlWorkspaceView
																.getCqlLeftNavBarPanelView().getDefinitionMap()
																.get(selectedDefinitionID);
														cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea()
																.setText(cqlWorkspaceView.getCqlLeftNavBarPanelView()
																		.getDefinitionMap().get(selectedDefinitionID)
																		.getName());
														cqlWorkspaceView.getCQlDefinitionsView().getDefineCommentTextArea()
																.setText(cqlWorkspaceView.getCqlLeftNavBarPanelView()
																		.getDefinitionMap().get(selectedDefinitionID)
																		.getCommentString());
														cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor()
																.setText(cqlWorkspaceView.getCqlLeftNavBarPanelView()
																		.getDefinitionMap().get(selectedDefinitionID)
																		.getLogic());
														if (MatContext.get().getMeasureLockService()
																.checkForEditPermission()) {
															cqlWorkspaceView.getCQlDefinitionsView()
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
																	cqlWorkspaceView.getCQlDefinitionsView()
																			.getDefineButtonBar().getDeleteButton()
																			.setEnabled(true);
																	cqlWorkspaceView.getCQlDefinitionsView()
																			.getContextDefinePATRadioBtn()
																			.setEnabled(true);
																	cqlWorkspaceView.getCQlDefinitionsView()
																			.getContextDefinePOPRadioBtn()
																			.setEnabled(true);
																} else {
																	cqlWorkspaceView.getCQlDefinitionsView()
																			.getDefineButtonBar().getDeleteButton()
																			.setEnabled(false);
																	cqlWorkspaceView.getCQlDefinitionsView()
																			.getContextDefinePATRadioBtn()
																			.setEnabled(false);
																	cqlWorkspaceView.getCQlDefinitionsView()
																			.getContextDefinePOPRadioBtn()
																			.setEnabled(false);
																}
															}
															
															if (cqlWorkspaceView.getCqlLeftNavBarPanelView()
																	.getDefinitionMap().get(selectedDefinitionID)
																	.getContext().equalsIgnoreCase("patient")) {
																cqlWorkspaceView.getCQlDefinitionsView()
																		.getContextDefinePATRadioBtn().setValue(true);
																cqlWorkspaceView.getCQlDefinitionsView()
																		.getContextDefinePOPRadioBtn().setValue(false);
															} else {
																cqlWorkspaceView.getCQlDefinitionsView()
																		.getContextDefinePOPRadioBtn().setValue(true);
																cqlWorkspaceView.getCQlDefinitionsView()
																		.getContextDefinePATRadioBtn().setValue(false);
															}

														}

														Map<String, List<CQLErrors>> expressionCQLErrorMap = result
																.getCqlErrorsPerExpression();
														if (expressionCQLErrorMap != null) {
															List<CQLErrors> errorList = expressionCQLErrorMap
																	.get(currentDefinition.getName());
															cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor()
																	.clearAnnotations();
															cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor()
																	.removeAllMarkers();
															for (CQLErrors error : errorList) {
																int startLine = error.getStartErrorInLine();
																int startColumn = error.getStartErrorAtOffset();
																cqlWorkspaceView.getCQlDefinitionsView()
																		.getDefineAceEditor().addAnnotation(startLine,
																				startColumn, error.getErrorMessage(),
																				AceAnnotationType.ERROR);
															}
															cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor()
																	.setAnnotations();
														}

														if (result.getCqlErrors().isEmpty()
																&& result.getExpressionReturnTypeMap() != null) {
															cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
																	.setText(result.getExpressionReturnTypeMap().get(
																			currentDefinition.getName()));
															cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
																	.setTitle("Return Type of CQL Expression is "
																			+ result.getExpressionReturnTypeMap()
																					.get(currentDefinition
																							.getName()));

														} else {
															cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
																	.setText("");
															cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
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

								cqlWorkspaceView.resetMessageDisplay();
							}
						}
						// 508 changes for Definitions Section
						cqlWorkspaceView.getCqlLeftNavBarPanelView()
								.setFocus(cqlWorkspaceView.getCQlDefinitionsView().getMainDefineViewVerticalPanel());
					}
				});

		// Double Click Handler Event for Function Name ListBox in Function Section
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
					showSearchBusyOnDoubleClick(true);
					cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
					cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();

					resetAceEditor(cqlWorkspaceView.getCqlFunctionsView().getViewCQLAceEditor());
					resetViewCQLCollapsiblePanel(cqlWorkspaceView.getCqlFunctionsView().getPanelViewCQLCollapse());

					cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox().setText("");
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
					if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
						showSearchBusyOnDoubleClick(false);
						showUnsavedChangesWarning();
					} else {
						int selectedIndex = cqlWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox()
								.getSelectedIndex();
						if (selectedIndex != -1) {
							final String selectedFunctionId = cqlWorkspaceView.getCqlLeftNavBarPanelView()
									.getFuncNameListBox().getValue(selectedIndex);
							cqlWorkspaceView.getCqlLeftNavBarPanelView()
									.setCurrentSelectedFunctionObjId(selectedFunctionId);
							if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap()
									.get(selectedFunctionId) != null) {
								cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton()
										.setEnabled(false);
								cqlWorkspaceView.getCqlFunctionsView().setWidgetReadOnly(false);
								cqlWorkspaceView.getCqlFunctionsView().getAddNewButtonBar().getaddNewButton()
										.setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
								// load most recent used cql artifacts
								MatContext.get().getMeasureService().getUsedCQLArtifacts(
										MatContext.get().getCurrentMeasureId(),
										new AsyncCallback<GetUsedCQLArtifactsResult>() {

											@Override
											public void onFailure(Throwable caught) {
												showSearchBusyOnDoubleClick(false);
												cqlWorkspaceView.getCqlFunctionsView().setWidgetReadOnly(MatContext.get()
														.getMeasureLockService().checkForEditPermission());
												Window.alert(
														MatContext.get().getMessageDelegate().getGenericErrorMessage());
											}

											@Override
											public void onSuccess(GetUsedCQLArtifactsResult result) {
												showSearchBusyOnDoubleClick(false);
												CQLFunctions currentFunction = cqlWorkspaceView.getCqlLeftNavBarPanelView()
														.getFunctionMap().get(selectedFunctionId);

												cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea()
														.setText(cqlWorkspaceView.getCqlLeftNavBarPanelView()
																.getFunctionMap().get(selectedFunctionId)
																.getName());
												cqlWorkspaceView.getCqlFunctionsView().getFunctionCommentTextArea()
														.setText(cqlWorkspaceView.getCqlLeftNavBarPanelView()
																.getFunctionMap().get(selectedFunctionId)
																.getCommentString());
												cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor()
														.setText(cqlWorkspaceView.getCqlLeftNavBarPanelView()
																.getFunctionMap().get(selectedFunctionId)
																.getLogic());
												if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap()
														.get(selectedFunctionId).getContext()
														.equalsIgnoreCase("patient")) {
													cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn()
															.setValue(true);
													cqlWorkspaceView.getCqlFunctionsView().getContextFuncPOPRadioBtn()
															.setValue(false);
												} else {
													cqlWorkspaceView.getCqlFunctionsView().getContextFuncPOPRadioBtn()
															.setValue(true);
													cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn()
															.setValue(false);
												}

												if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
													cqlWorkspaceView.getCqlFunctionsView().setWidgetReadOnly(true);
													
													if (!result.getCqlErrors().isEmpty()
															|| !result.getUsedCQLFunctions()
																	.contains(currentFunction.getName())) {
														cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar()
																.getDeleteButton().setEnabled(true);
														// MAT-8571 :Enable Function context radio buttons if its used
														// and CQL has error.
														cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn()
																.setEnabled(true);
														cqlWorkspaceView.getCqlFunctionsView().getContextFuncPOPRadioBtn()
																.setEnabled(true);
													} else {
														cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar()
																.getDeleteButton().setEnabled(false);
														cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn()
																.setEnabled(false);
														cqlWorkspaceView.getCqlFunctionsView().getContextFuncPOPRadioBtn()
																.setEnabled(false);
													}
												}

												Map<String, List<CQLErrors>> expressionCQLErrorMap = result
														.getCqlErrorsPerExpression();
												if (expressionCQLErrorMap != null) {
													List<CQLErrors> errorList = expressionCQLErrorMap
															.get(currentFunction.getName());
													cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor()
															.clearAnnotations();
													cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor()
															.removeAllMarkers();
													for (CQLErrors error : errorList) {
														int startLine = error.getStartErrorInLine();
														int startColumn = error.getStartErrorAtOffset();
														cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor()
																.addAnnotation(startLine, startColumn,
																		error.getErrorMessage(),
																		AceAnnotationType.ERROR);
													}
													cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor()
															.setAnnotations();
												}
												if (result.getCqlErrors().isEmpty()
														&& result.getExpressionReturnTypeMap() != null) {
													cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox()
															.setText(result.getExpressionReturnTypeMap()
																	.get(currentFunction.getName()));
													cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox()
															.setTitle("Return Type of CQL Expression is "
																	+ result.getExpressionReturnTypeMap()
																			.get(currentFunction.getName()));

												} else {
													cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox()
															.setText("");
													cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox()
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
						if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
							cqlWorkspaceView.getCqlFunctionsView().getFunctionArgNameMap().clear();
							CQLFunctions selectedFunction = cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap()
									.get(cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
							if (selectedFunction.getArgumentList() != null) {
								cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList().clear();
								cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList()
										.addAll(selectedFunction.getArgumentList());
							} else {
								cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList().clear();
							}
						}
						cqlWorkspaceView.resetMessageDisplay();
					}
					cqlWorkspaceView.getCqlFunctionsView().createAddArgumentViewForFunctions(
							cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList(),
							MatContext.get().getMeasureLockService().checkForEditPermission());

				}
				// 508 changes for Functions Section
				cqlWorkspaceView.getCqlLeftNavBarPanelView()
						.setFocus(cqlWorkspaceView.getCqlFunctionsView().getMainFunctionVerticalPanel());
			}
		});

		// Double Click Handler Event for Includes Name ListBox in the Includes Section
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox()
				.addDoubleClickHandler(new DoubleClickHandler() {
					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsLoading()) {
							event.stopPropagation();
						} else {
							cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
							cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);

							if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
								showUnsavedChangesWarning();
							} else {
								int selectedIndex = cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox()
										.getSelectedIndex();
								if (selectedIndex != -1) {
									final String selectedIncludeLibraryID = cqlWorkspaceView.getCqlLeftNavBarPanelView()
											.getIncludesNameListbox().getValue(selectedIndex);
									cqlWorkspaceView.getCqlLeftNavBarPanelView()
											.setCurrentSelectedIncLibraryObjId(selectedIncludeLibraryID);
									if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
											.get(selectedIncludeLibraryID) != null) {

										MatContext.get().getCQLLibraryService().findCQLLibraryByID(
												cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
														.get(selectedIncludeLibraryID).getCqlLibraryId(),
												new AsyncCallback<CQLLibraryDataSetObject>() {

													@Override
													public void onSuccess(CQLLibraryDataSetObject result) {
														if (result != null) {
															currentIncludeLibrarySetId = result.getCqlSetId();
															currentIncludeLibraryId = result.getId();
															cqlWorkspaceView.getIncludeView().buildIncludesReadOnlyView();

															cqlWorkspaceView.getIncludeView().getAliasNameTxtArea()
																	.setText(cqlWorkspaceView.getCqlLeftNavBarPanelView()
																			.getIncludeLibraryMap()
																			.get(selectedIncludeLibraryID)
																			.getAliasName());
															cqlWorkspaceView.getIncludeView().getViewCQLEditor()
																	.setText(result.getCqlText());
															cqlWorkspaceView.getIncludeView().getOwnerNameTextBox()
																	.setText(cqlWorkspaceView.getCqlLeftNavBarPanelView()
																			.getOwnerName(result));
															cqlWorkspaceView.getIncludeView().getCqlLibraryNameTextBox()
																	.setText(result.getCqlName());
															
															cqlWorkspaceView.getIncludeView().getSaveModifyButton()
																	.setEnabled(false);
															cqlWorkspaceView.getIncludeView().getSaveModifyButton()
																	.setEnabled(false);
															if (MatContext.get().getMeasureLockService()
																	.checkForEditPermission()) {
																cqlWorkspaceView.getIncludeView().setWidgetReadOnly(false);
																cqlWorkspaceView.getIncludeView().getDeleteButton()
																		.setEnabled(false);
																cqlWorkspaceView.getIncludeView().getSaveModifyButton()
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
																						CQLIncludeLibrary cqlIncludeLibrary = cqlWorkspaceView
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
																							cqlWorkspaceView
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
														messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
													}
												});

										cqlWorkspaceView.getIncludeView().setSelectedObject(
												cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
												.get(selectedIncludeLibraryID).getCqlLibraryId());
										cqlWorkspaceView.getIncludeView().setIncludedList(
												cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludedList(cqlWorkspaceView
														.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
										cqlWorkspaceView.getIncludeView().getSelectedObjectList().clear();
									}
								}
								cqlWorkspaceView.resetMessageDisplay();
							}
						}

					}
				});

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().addKeyPressHandler(event -> listBoxKeyPress(cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox(), event));

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().addKeyPressHandler(event -> listBoxKeyPress(cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox(), event));
		
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().addKeyPressHandler(event -> listBoxKeyPress(cqlWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox(), event));
		
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().addKeyPressHandler(event -> listBoxKeyPress(cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox(), event));

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsListBox().addKeyPressHandler(event -> listBoxKeyPress(cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsListBox(), event));
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
		cqlWorkspaceView.getCqlGeneralInformationView().getSaveButton().addClickHandler(event -> saveCQLGeneralInformation());
		cqlWorkspaceView.getCqlGeneralInformationView().getComments().addValueChangeHandler(event -> resetMessagesAndSetPageDirty(true));
	}
	
	private void resetMessagesAndSetPageDirty(boolean isPageDirty) {
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			cqlWorkspaceView.resetMessageDisplay();
			cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(isPageDirty);
		}
	}
	
	private void saveCQLGeneralInformation() {
		resetMessagesAndSetPageDirty(false);
		String comments = cqlWorkspaceView.getCqlGeneralInformationView().getComments().getText().trim();
		//TODO don't pass the message panel to a validator, check response and set message here
		boolean isvalid = CQLGeneralInformationUtility.validateGeneralInformationSection(cqlWorkspaceView.getCqlGeneralInformationView(), messagePanel, null, comments);
		if(isvalid) {
			saveCQLGeneralInformationAsync(comments);
		}
	}

	private void saveCQLGeneralInformationAsync(String comments) {
		MatContext.get().getMeasureService().saveAndModifyCQLGeneralInfo(MatContext.get().getCurrentMeasureId(), null, comments, 
				new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onFailure(Throwable caught) {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);
			}

			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if (result != null) {
					cqlLibraryComment = result.getCqlModel().getLibraryComment();
					cqlWorkspaceView.getCqlGeneralInformationView().getComments().setText(cqlLibraryComment);
					cqlWorkspaceView.getCqlGeneralInformationView().getComments().setCursorPos(0);
					messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getCurrentMeasureName() + " general information successfully updated.");
				}
				showSearchingBusy(false);
			}
		});
	}
	
	/**
	 * Adds the include CQL library handlers.
	 */
	private void addIncludeCQLLibraryHandlers() {
		cqlWorkspaceView.getIncludeView().getSaveModifyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				messagePanel.getErrorMessageAlert().clearAlert();
				messagePanel.getSuccessMessageAlert().clearAlert();
				final EditIncludedLibraryDialogBox editIncludedLibraryDialogBox = new EditIncludedLibraryDialogBox("Replace Library");
				editIncludedLibraryDialogBox.findAvailableLibraries(currentIncludeLibrarySetId, currentIncludeLibraryId, true);

				editIncludedLibraryDialogBox.getApplyButton().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						messagePanel.getErrorMessageAlert().clearAlert();
						messagePanel.getSuccessMessageAlert().clearAlert();
						if (editIncludedLibraryDialogBox.getSelectedList().size() > 0) {
							final CQLIncludeLibrary toBeModified = cqlWorkspaceView.getCqlLeftNavBarPanelView()
									.getIncludeLibraryMap()
									.get(cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId());

							final CQLLibraryDataSetObject dto = editIncludedLibraryDialogBox.getSelectedList().get(0);
							if (dto != null) {
								final CQLIncludeLibrary currentObject = new CQLIncludeLibrary(dto);

								MatContext.get().getMeasureService().saveIncludeLibrayInCQLLookUp(
										MatContext.get().getCurrentMeasureId(), toBeModified, currentObject,
										cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
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
														cqlWorkspaceView.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(
																		filterComponentMeasuresFromIncludedLibraries(result.getCqlModel().getCqlIncludeLibrarys()));
														cqlWorkspaceView.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
														MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
														MatContext.get().setIncludedValues(result);
														MatContext.get().setCQLModel(result.getCqlModel());

														editIncludedLibraryDialogBox.getDialogModal().hide();
														DomEvent.fireNativeEvent(Document.get().createDblClickEvent(
																cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(), 0, 0, 0, 0, false, false, false, false),
																cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox());
														String libraryNameWithVersion = result.getIncludeLibrary().getCqlLibraryName() + " v" + result.getIncludeLibrary().getVersion();
														messagePanel.getSuccessMessageAlert().createAlert(libraryNameWithVersion + " has been successfully saved as the alias " + result.getIncludeLibrary().getAliasName());
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
		cqlWorkspaceView.getIncludeView().getFocusPanel().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				// Search when enter is pressed.
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					cqlWorkspaceView.getIncludeView().getSearchButton().click();
				}
			}
		});

		// Search CQL Libraries Functionality for Includes Section
		cqlWorkspaceView.getIncludeView().getSearchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAllIncludeLibraryList(cqlWorkspaceView.getIncludeView().getSearchTextBox().getText().trim());
			}
		});

		// Save Functionality for Includes Section
		cqlWorkspaceView.getIncludeView().getIncludesButtonBar().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addIncludeLibraryInCQLLookUp();
					// 508 changes for Library Alias.
					cqlWorkspaceView.getCqlLeftNavBarPanelView()
							.setFocus(cqlWorkspaceView.getIncludeView().getAliasNameTxtArea());
				}
			}
		});

		// Erase Functionality for Includes Section
		cqlWorkspaceView.getIncludeView().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				cqlWorkspaceView.resetMessageDisplay();
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				clearAlias();

				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox()
						.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else {
					messagePanel.getWarningMessageAlert().clearAlert();
				}
				// 508 changes for Library Alias.
				cqlWorkspaceView.getCqlLeftNavBarPanelView()
						.setFocus(cqlWorkspaceView.getIncludeView().getAliasNameTxtArea());
			}
		});

		// Delete Functionality for Includes Section
		cqlWorkspaceView.getIncludeView().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_INCLUDE(
								cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().getValue()));
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
				// 508 changes for Library Alias.
				cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setFocus(true);
			}

		});

		// Close Functionality for Includes Section
		cqlWorkspaceView.getIncludeView().getCloseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentIncludeLibrarySetId = null;
				// Below lines are to clear search suggestion textbox and listbox
				// selection after erase.
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().setItemSelected(
							cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(),
							false);
				}
				cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText("");
				cqlWorkspaceView.buildIncludesView();
				SaveCQLLibraryResult cqlLibrarySearchModel = new SaveCQLLibraryResult();
				cqlLibrarySearchModel
						.setCqlLibraryDataSetObjects(cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryList());
				cqlWorkspaceView.getIncludeView().setIncludedList(cqlWorkspaceView.getCqlLeftNavBarPanelView()
						.getIncludedList(cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
				cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(cqlLibrarySearchModel,
						MatContext.get().getMeasureLockService().checkForEditPermission(), false);
				cqlWorkspaceView.getIncludeView()
						.setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());

				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox()
						.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
					messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else {
					messagePanel.getWarningMessageAlert().clearAlert();
				}
				// 508 changes for Library Alias.
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getIncludeView().getAliasNameTxtArea());
			}
		});

		cqlWorkspaceView.getIncludeView().setObserver(new CQLIncludeLibraryView.Observer() {

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
								cqlWorkspaceView.getIncludeView().getViewCQLEditor().setText(result.getCqlText());
							}
						});
			}
		});

	}

	/**
	 * Adds the include library in CQL look up.
	 */
	private void addIncludeLibraryInCQLLookUp() {
		cqlWorkspaceView.resetMessageDisplay();
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox()
				.getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
			messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
			return;
		} else {
			messagePanel.getWarningMessageAlert().clearAlert();
		}
		final String aliasName = cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().getText();

		if (!aliasName.isEmpty() && cqlWorkspaceView.getIncludeView().getSelectedObjectList().size() > 0) {
			// functionality to add Include Library
			CQLLibraryDataSetObject cqlLibraryDataSetObject = cqlWorkspaceView.getIncludeView().getSelectedObjectList().get(0);
			CQLIncludeLibrary incLibrary = new CQLIncludeLibrary();
			incLibrary.setAliasName(aliasName);
			incLibrary.setCqlLibraryId(cqlLibraryDataSetObject.getId());
			String versionValue = cqlLibraryDataSetObject.getVersion().replace("v", "") + "." + cqlLibraryDataSetObject.getRevisionNumber();
			incLibrary.setVersion(versionValue);
			incLibrary.setCqlLibraryName(cqlLibraryDataSetObject.getCqlName());
			incLibrary.setQdmVersion(cqlLibraryDataSetObject.getQdmVersion());
			incLibrary.setSetId(cqlLibraryDataSetObject.getCqlSetId());
			if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() == null) {
				showSearchingBusy(true);
				// this is just to add include library and not modify
				MatContext.get().getMeasureService().saveIncludeLibrayInCQLLookUp(
						MatContext.get().getCurrentMeasureId(), null, incLibrary,
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
						new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								showSearchingBusy(false);
								if (caught instanceof InvalidLibraryException) {
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
										cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										cqlWorkspaceView.getCqlLeftNavBarPanelView()
												.setViewIncludeLibrarys(filterComponentMeasuresFromIncludedLibraries(
														result.getCqlModel().getCqlIncludeLibrarys()));
										MatContext.get().setIncludes(
												getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
										cqlWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
										cqlWorkspaceView.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
										cqlWorkspaceView.getIncludeView().setIncludedList(
												cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludedList(cqlWorkspaceView
														.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
										messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getIncludeLibrarySuccessMessage(result.getIncludeLibrary().getAliasName()));
										clearAlias();
										MatContext.get().setIncludedValues(result);
										MatContext.get().setCQLModel(result.getCqlModel());

										if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
										} else {
											messagePanel.getWarningMessageAlert().clearAlert();
										}
									}
								}
								showSearchingBusy(false);
							}
						});
			}
		} else {
			cqlWorkspaceView.getIncludeView().getAliasNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSAVE_INCLUDE_LIBRARY_VALIATION_ERROR());
		}
	}

	/**
	 * Event Handlers for Context Radio Buttons.
	 */
	private void addEventHandlersOnContextRadioButtons() {
		cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePATRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
							cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);
						} else {
							cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setValue(true);
						}

					}
				});

		cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePOPRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePOPRadioBtn().getValue()) {
							cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePATRadioBtn().setValue(false);
						} else {
							cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
						}

					}
				});

		cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn().getValue()) {
							cqlWorkspaceView.getCqlFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
						} else {
							cqlWorkspaceView.getCqlFunctionsView().getContextFuncPOPRadioBtn().setValue(true);
						}
					}
				});

		cqlWorkspaceView.getCqlFunctionsView().getContextFuncPOPRadioBtn()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
						if (cqlWorkspaceView.getCqlFunctionsView().getContextFuncPOPRadioBtn().getValue()) {
							cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn().setValue(false);
						} else {
							cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn().setValue(true);
						}
					}
				});
	}

	/**
	 * Event Handlers for Ace Editors.
	 */
	private void addEventHandlerOnAceEditors() {
		cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().isReadOnly()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().isReadOnly()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().isReadOnly()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});
	}

	/**
	 * Method to trigger double Click on List Boxes based on section when user
	 * clicks Yes on Warning message (Dirty Check).
	 */
	private void clickEventOnListboxes() {

		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
		switch (currentSection) {
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().fireEvent(new DoubleClickEvent() {
			});
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().fireEvent(new DoubleClickEvent() {
			});
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().fireEvent(new DoubleClickEvent() {
			});
			break;
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().fireEvent(new DoubleClickEvent() {
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
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsTab().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_APPLIED_QDM):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_CODES):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(false);
			break;
		case (CQLWorkSpaceConstants.CQL_VIEW_MENU):
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewCQL().setActive(false);
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
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsCollapse().getElement()
							.setClassName("panel-collapse collapse in");
					break;
				case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
					currentSection = nextSection;
					includesEvent();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
							.setClassName("panel-collapse collapse in");
					break;
				case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
					currentSection = nextSection;
					functionEvent();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
							.setClassName("panel-collapse collapse in");
					break;
				case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
					currentSection = nextSection;
					parameterEvent();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
							.setClassName("panel-collapse collapse in");
					break;
				case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
					currentSection = nextSection;
					definitionEvent();
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
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
		cqlWorkspaceView.hideInformationDropDown();
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

		cqlWorkspaceView.getValueSetView().setObserver(new CQLAppliedValueSetView.Observer() {
			@Override
			public void onModifyClicked(CQLQualityDataSetDTO result) {
				if (!cqlWorkspaceView.getValueSetView().getIsLoading()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
					isModified = true;
					modifyValueSetDTO = result;
					String displayName = result.getName();
					// Substring at 60th character length.
					if (displayName.length() >= 60) {
						displayName = displayName.substring(0, 59);
					}
					HTML searchHeaderText = new HTML("<strong>Modify value set ( " + displayName + ")</strong>");
					cqlWorkspaceView.getValueSetView().getSearchHeader().clear();
					cqlWorkspaceView.getValueSetView().getSearchHeader().add(searchHeaderText);
					cqlWorkspaceView.getValueSetView().getMainPanel().getElement().focus();
					if (result.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
						isUserDefined = true;
					} else {
						isUserDefined = false;
					}

					onModifyValueSet(result, isUserDefined);
					// 508 Compliance for Value Sets section
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getValueSetView().getOIDInput());
				} else {
					// do nothing when loading.
				}

			}

			@Override
			public void onDeleteClicked(CQLQualityDataSetDTO result, final int index) {
				if (!cqlWorkspaceView.getValueSetView().getIsLoading()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
					if ((modifyValueSetDTO != null) && modifyValueSetDTO.getId().equalsIgnoreCase(result.getId())) {
						isModified = false;
					}
					String measureId = MatContext.get().getCurrentMeasureId();
					if ((measureId != null) && !measureId.equals("")) {
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(result.getId());
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate()
										.getDELETE_CONFIRMATION_VALUESET(result.getName()));
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
						// 508 Compliance for Value Sets section
						cqlWorkspaceView.getValueSetView().getOIDInput().setFocus(true);
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
							messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_QDM_REMOVE_MSG(result.getCqlQualityDataSetDTO().getName()));
							messagePanel.getSuccessMessageAlert().setVisible(true);
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
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setAvailableQDSAttributeList(result);
						AddFunctionArgumentDialogBox.showArgumentDialogBox(functionArg, true, cqlWorkspaceView.getCqlFunctionsView(), messagePanel, MatContext.get().getMeasureLockService().checkForEditPermission());
					}

				});
	}

	/**
	 * This method Clears alias view on Erase Button click when isPageDirty is not
	 * set.
	 */
	private void clearAlias() {
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((cqlWorkspaceView.getIncludeView().getAliasNameTxtArea() != null)) {
			cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText("");
		}
		if ((cqlWorkspaceView.getIncludeView().getViewCQLEditor().getText() != null)) {
			cqlWorkspaceView.getIncludeView().getViewCQLEditor().setText("");
		}
		// Below lines are to clear Library search text box.
		if ((cqlWorkspaceView.getIncludeView().getSearchTextBox().getText() != null)) {
			cqlWorkspaceView.getIncludeView().getSearchTextBox().setText("");
		}
		cqlWorkspaceView.getIncludeView().getSelectedObjectList().clear();
		cqlWorkspaceView.getIncludeView().setSelectedObject(null);
		cqlWorkspaceView.getIncludeView().setIncludedList(cqlWorkspaceView.getCqlLeftNavBarPanelView()
				.getIncludedList(cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
		unCheckAvailableLibraryCheckBox();

		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().setItemSelected(
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(), false);
		}

	}

	/**
	 * Un check available library check box.
	 */
	private void unCheckAvailableLibraryCheckBox() {
		List<CQLLibraryDataSetObject> availableLibraries = new ArrayList<CQLLibraryDataSetObject>();
		availableLibraries = cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryList();
		for (int i = 0; i < availableLibraries.size(); i++) {
			availableLibraries.get(i).setSelected(false);
		}
		
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryList());
		cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
				MatContext.get().getMeasureLockService().checkForEditPermission(), false);
	}

	/**
	 * Clears the ace editor for parameters. Functionality for the eraser icon in
	 * parameter section.
	 */
	private void eraseParameter() {

		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if (cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().getText() != null) {
			cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().setText("");
			cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}

	}

	/**
	 * Clears the ace editor for definitions. Functionality for the eraser icon in
	 * definition section.
	 */
	private void eraseDefinition() {

		cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
		if ((cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().getText() != null)) {
			cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().setText("");
			cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}
	}

	/**
	 * Clears the ace editor for functions. Functionality for the eraser icon in
	 * function section.
	 */
	private void eraseFunction() {

		cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if ((cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().setText("");
			cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}
	}

	/**
	 * Adds and modify function.
	 */
	protected void addAndModifyFunction() {
		cqlWorkspaceView.resetMessageDisplay();
		final String functionName = cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea().getText();
		String functionBody = cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().getText();
		String funcComment = cqlWorkspaceView.getCqlFunctionsView().getFunctionCommentTextArea().getText();
		String funcContext = "";
		if (cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn().getValue()) {
			funcContext = "Patient";
		} else {
			funcContext = "Population";
		}
		
		boolean isValidFunctionName = isValidExpressionName(functionName);
		if (isValidFunctionName) {
			if (validator.hasSpecialCharacter(functionName.trim())) {
				cqlWorkspaceView.getCqlFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
				cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
			} else if (validator.isCommentTooLongOrContainsInvalidText(funcComment)) {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				cqlWorkspaceView.getCqlFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
			} else {
				CQLFunctions function = new CQLFunctions();
				function.setLogic(functionBody);
				function.setName(functionName);
				function.setCommentString(funcComment);
				function.setArgumentList(cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList());
				function.setContext(funcContext);
				CQLFunctions toBeModifiedParamObj = null;

				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
					toBeModifiedParamObj = cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap()
							.get(cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getMeasureService().saveAndModifyFunctions(MatContext.get().getCurrentMeasureId(),
						toBeModifiedParamObj, function, cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewFunctions(),
						isFormatable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
								cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
								cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
								messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {

										cqlWorkspaceView.getCqlLeftNavBarPanelView()
												.setViewFunctions(result.getCqlModel().getCqlFunctions());
										MatContext.get()
												.setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
										cqlWorkspaceView.getCqlLeftNavBarPanelView()
												.setCurrentSelectedFunctionObjId(result.getFunction().getId());
										cqlWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
										cqlWorkspaceView.getCqlLeftNavBarPanelView().updateFunctionMap();
										messagePanel.getErrorMessageAlert().clearAlert();
										messagePanel.getSuccessMessageAlert().setVisible(true);
										cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea()
												.setText(result.getFunction().getName());
										cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor()
												.setText(result.getFunction().getLogic());
										cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor()
												.clearAnnotations();
										cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor()
												.removeAllMarkers();
										if (validateCQLArtifact(result)) {
											messagePanel.getSuccessMessageAlert().clearAlert();
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_MODIFY_WITH_ERRORS(functionName.trim()));
											cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox().setText("");
											cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox()
													.setTitle("Return Type of CQL Expression");

										} else if (!result.isDatatypeUsedCorrectly()) {
											messagePanel.getSuccessMessageAlert().clearAlert();
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWarningBadDataTypeCombination());
											if (result.isValidCQLWhileSavingExpression()) {
												cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox()
														.setText(result.getFunction().getReturnType());
												cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression is "
																+ result.getFunction().getReturnType());
											} else {
												cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox().setText("");
												cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression");
											}

										} else {
											messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_MODIFY(functionName.trim()));
											if (result.isValidCQLWhileSavingExpression()) {
												cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox()
														.setText(result.getFunction().getReturnType());
												cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression is "
																+ result.getFunction().getReturnType());
											} else {
												cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox().setText("");
												cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression");
											}
										}

										cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().setAnnotations();
										cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().redisplay();

									} else if (result.getFailureReason() == 1) { //TODO 1, 2, 3, aren't descriptive
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
										cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
									} else if (result.getFailureReason() == 2) {
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify.");
										cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
									} else if (result.getFailureReason() == 3) {
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
										cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
										if (result.getFunction() != null) {
											cqlWorkspaceView.getCqlFunctionsView().createAddArgumentViewForFunctions(
													result.getFunction().getArgumentList(),
													MatContext.get().getMeasureLockService().checkForEditPermission());
										}
									} else if (result.getFailureReason() == 6) {
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlFunctionArgumentNameError());
									} else if (result.getFailureReason() == 8) {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
									}

								}
								showSearchingBusy(false);
								// if there are errors, enable the function delete button
								if (!result.getCqlErrors().isEmpty()) {
									cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton()
											.setEnabled(true);
									cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn().setEnabled(true);
									cqlWorkspaceView.getCqlFunctionsView().getContextFuncPOPRadioBtn().setEnabled(true);
									cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox().setText("");

								}

						else {
									// if the saved function is in use, then disable the delete button
									if (result.getUsedCQLArtifacts().getUsedCQLFunctions().contains(functionName)) {
										cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton()
												.setEnabled(false);
										cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn()
												.setEnabled(false);
										cqlWorkspaceView.getCqlFunctionsView().getContextFuncPOPRadioBtn()
												.setEnabled(false);
									}

						else {
										cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton()
												.setEnabled(true);
										cqlWorkspaceView.getCqlFunctionsView().getContextFuncPATRadioBtn()
												.setEnabled(true);
										cqlWorkspaceView.getCqlFunctionsView().getContextFuncPOPRadioBtn()
												.setEnabled(true);
									}
								}
							}
						});

			}

		} else {
			cqlWorkspaceView.getCqlFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(functionName.isEmpty() 
					? MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_FUNCTION()
					: "Invalid Function name. " + MessageDelegate.DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
		}

	}

	/**
	 * Adds the and modify parameters.
	 */
	private void addAndModifyParameters() {
		cqlWorkspaceView.resetMessageDisplay();
		final String parameterName = cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().getText();
		String parameterLogic = cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().getText();
		String parameterComment = cqlWorkspaceView.getCQLParametersView().getParameterCommentTextArea().getText();
		
		boolean isValidParamaterName = isValidExpressionName(parameterName);
		if (isValidParamaterName) {
			if (validator.hasSpecialCharacter(parameterName.trim())) {
				cqlWorkspaceView.getCQLParametersView().getParamNameGroup().setValidationState(ValidationState.ERROR);
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
				cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());

			} else if (validator.isCommentTooLongOrContainsInvalidText(parameterComment)) {
				messagePanel.getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				cqlWorkspaceView.getCQLParametersView().getParamCommentGroup().setValidationState(ValidationState.ERROR);

			} else {
				CQLParameter parameter = new CQLParameter();
				parameter.setLogic(parameterLogic);
				parameter.setName(parameterName);
				parameter.setCommentString(parameterComment);
				CQLParameter toBeModifiedParamObj = null;

				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
					toBeModifiedParamObj = cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterMap()
							.get(cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getMeasureService().saveAndModifyParameters(MatContext.get().getCurrentMeasureId(),
						toBeModifiedParamObj, parameter,
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewParameterList(), isFormatable,
						new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
								messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										cqlWorkspaceView.getCqlLeftNavBarPanelView()
												.setViewParameterList(result.getCqlModel().getCqlParameters());
										MatContext.get().setParameters(
												getParamaterList(result.getCqlModel().getCqlParameters()));
										MatContext.get().setCQLModel(result.getCqlModel());
										cqlWorkspaceView.getCqlLeftNavBarPanelView()
												.setCurrentSelectedParamerterObjId(result.getParameter().getId());
										cqlWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
										cqlWorkspaceView.getCqlLeftNavBarPanelView().updateParamMap();
										messagePanel.getErrorMessageAlert().clearAlert();
										cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea()
												.setText(result.getParameter().getName());
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor()
												.setText(result.getParameter().getLogic());
										cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
										
										if (validateCQLArtifact(result)) {
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_PARAMETER_MODIFY_WITH_ERRORS(parameterName.trim()));
										} else if (!result.isDatatypeUsedCorrectly()) {
											messagePanel.getSuccessMessageAlert().clearAlert();
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWarningBadDataTypeCombination());
										} else {
											messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_PARAMETER_MODIFY(parameterName.trim()));
										}

										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().setAnnotations();
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().redisplay();

									} else if (result.getFailureReason() == 1) { //TODO 1, 2, 3, etc. aren't clear failure reasons
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
										cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
									} else if (result.getFailureReason() == 2) {
										messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify."); //TODO move to constant, extract common method
										cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
									} else if (result.getFailureReason() == 3) {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
										cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
									} else if (result.getFailureReason() == 8) {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
									}
								}
								showSearchingBusy(false);
								// if there are errors, enable the parameter delete button
								if (!result.getCqlErrors().isEmpty()) {
									cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton()
											.setEnabled(true);
								}

						else {
									// if the saved parameter is in use, then disable the delete button
									if (result.getUsedCQLArtifacts().getUsedCQLParameters().contains(parameterName)) {
										cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setEnabled(false);
									}

						else {
										cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setEnabled(true);
									}
								}
							}
						});

			}

		} else {
			cqlWorkspaceView.getCQLParametersView().getParamNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(parameterName.isEmpty() 
					? MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_PARAMETER()
					: "Invalid Parameter name. " + MessageDelegate.DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
		}

	}

	/**
	 * This method is called to Add/Modify Definitions into Measure Xml.
	 * 
	 */
	private void addAndModifyDefintions() {
		cqlWorkspaceView.resetMessageDisplay();
		final String definitionName = cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().getText();
		String definitionLogic = cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().getText();
		String definitionComment = cqlWorkspaceView.getCQlDefinitionsView().getDefineCommentTextArea().getText();
		String defineContext = "";
		if (cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}
		
		boolean isValidDefinitionName = isValidExpressionName(definitionName);
		if (isValidDefinitionName) {
			if (validator.hasSpecialCharacter(definitionName.trim())) {
				cqlWorkspaceView.getCQlDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
				cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
			} else if (validator.isCommentTooLongOrContainsInvalidText(definitionComment)) {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				cqlWorkspaceView.getCQlDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);

			} else {
				final CQLDefinition define = new CQLDefinition();
				define.setName(definitionName);
				define.setLogic(definitionLogic);
				define.setCommentString(definitionComment);
				define.setContext(defineContext);
				CQLDefinition toBeModifiedObj = null;

				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
					toBeModifiedObj = cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionMap()
							.get(cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getMeasureService().saveAndModifyDefinitions(MatContext.get().getCurrentMeasureId(),
						toBeModifiedObj, define, cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewDefinitions(),
						isFormatable, new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
								messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {

								if (result != null) {
									if (result.isSuccess()) {
										cqlWorkspaceView.getCqlLeftNavBarPanelView()
												.setViewDefinitions(result.getCqlModel().getDefinitionList());
										MatContext.get().setDefinitions(
												getDefinitionList(result.getCqlModel().getDefinitionList()));
										cqlWorkspaceView.getCqlLeftNavBarPanelView()
												.setCurrentSelectedDefinitionObjId(result.getDefinition().getId());
										cqlWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
										cqlWorkspaceView.getCqlLeftNavBarPanelView().updateDefineMap();
										messagePanel.getErrorMessageAlert().clearAlert();
										cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea()
												.setText(result.getDefinition().getName());
										cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor()
												.setText(result.getDefinition().getLogic());
										cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
										cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().removeAllMarkers();
										
										if (validateCQLArtifact(result)) {
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_DEFINITION_MODIFY_WITH_ERRORS(definitionName.trim()));
											cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox().setText("");
											cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
													.setTitle("Return Type of CQL Expression");
										} else if (!result.isDatatypeUsedCorrectly()) {
											messagePanel.getSuccessMessageAlert().clearAlert();
											messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWarningBadDataTypeCombination());
											if (result.isValidCQLWhileSavingExpression()) {
												cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
														.setText(result.getDefinition().getReturnType());
												cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression is "
																+ result.getDefinition().getReturnType());
											} else {
												cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
														.setText("");
												cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression");
											}

										} else {
											messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_DEFINITION_MODIFY(definitionName.trim()));
											if (result.isValidCQLWhileSavingExpression()) {
												cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
														.setText(result.getDefinition().getReturnType());
												cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression is "
																+ result.getDefinition().getReturnType());
											} else {
												cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
														.setText("");
												cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox()
														.setTitle("Return Type of CQL Expression");
											}
										}

										cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().setAnnotations();
										cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().redisplay();

									} else {
										if (result.getFailureReason() == 1) { //TODO failure reasons aren't clear
											messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
											cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
										} else if (result.getFailureReason() == 2) {
											messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify.");
											cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
										} else if (result.getFailureReason() == 3) {
											messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
											cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea()
													.setText(definitionName.trim());
										} else if (result.getFailureReason() == 8) {
											messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
										}
									}

								}
								showSearchingBusy(false);
								// if there are errors, enable the definition button
								if (!result.getCqlErrors().isEmpty()) {
									cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
											.setEnabled(true);
									cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePATRadioBtn()
											.setEnabled(true);
									cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePOPRadioBtn()
											.setEnabled(true);
									cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox().setText("");
								}

						else {
									// if the saved definition is in use, then disable the delete button
									if (result.getUsedCQLArtifacts().getUsedCQLDefinitions().contains(definitionName)) {
										cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
												.setEnabled(false);
										cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePATRadioBtn()
												.setEnabled(false);
										cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePOPRadioBtn()
												.setEnabled(false);
									}

						else {
										cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
												.setEnabled(true);
										cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePATRadioBtn()
												.setEnabled(true);
										cqlWorkspaceView.getCQlDefinitionsView().getContextDefinePOPRadioBtn()
												.setEnabled(true);
									}
								}
							}
						});

			}

		} else {
			cqlWorkspaceView.getCQlDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(definitionName.isEmpty() 
					? MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_DEFINITION()
					: "Invalid Definition name. " + MessageDelegate.DEFINED_KEYWORD_EXPRESION_ERROR_MESSAGE);
			cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
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
		cqlWorkspaceView.getCqlLeftNavBarPanelView().clearShotcutKeyList();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		cqlWorkspaceView.getCqlFunctionsView().getFunctionArgNameMap().clear();
		cqlWorkspaceView.getValueSetView().clearCellTableMainPanel();
		cqlWorkspaceView.getCodesView().clearCellTableMainPanel();
		cqlWorkspaceView.getIncludeView().getSearchTextBox().setText("");
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsCollapse().getElement().setClassName("panel-collapse collapse");
		cqlWorkspaceView.getComponentView().clearAceEditor();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
				.setClassName("panel-collapse collapse");
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
				.setClassName("panel-collapse collapse");
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
				.setClassName("panel-collapse collapse");
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
				.setClassName("panel-collapse collapse");
		if (cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList().size() > 0) {
			cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList().clear();
		}
		isModified = false;
		isCodeModified = false;
		setId = null;
		currentIncludeLibrarySetId = null;
		currentIncludeLibraryId = null;
		modifyValueSetDTO = null;
		curAceEditor = null;
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		messagePanel.clearAlerts();
		cqlWorkspaceView.resetAll();
		panel.clear();
		cqlWorkspaceView.getMainPanel().clear();
		MatContext.get().getValuesets().clear();
	}

	@Override
	public void beforeDisplay() {
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		cqlWorkspaceView.buildView(messagePanel);
		addLeftNavEventHandler();
		cqlWorkspaceView.resetMessageDisplay();
		panel.add(cqlWorkspaceView.asWidget());
		getCQLDataForLoad();
		getComponentMeasureData();
		if (cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList().size() > 0) {
			cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList().clear();
		}
		cqlWorkspaceView.getCqlLeftNavBarPanelView().resetActiveAnchorLists();
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
		messagePanel.clearAlerts();
		showSearchingBusy(true);
		
		MatContext.get().getCQLLibraryService().searchForIncludes(setId, searchText, true,
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
							cqlWorkspaceView.getCqlLeftNavBarPanelView()
									.setIncludeLibraryList(result.getCqlLibraryDataSetObjects());
							cqlWorkspaceView.buildIncludesView();
							cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
									MatContext.get().getMeasureLockService().checkForEditPermission(), false);

						} else {
							cqlWorkspaceView.buildIncludesView();
							cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
									MatContext.get().getMeasureLockService().checkForEditPermission(), false);
							if (!cqlWorkspaceView.getIncludeView().getSearchTextBox().getText().isEmpty()) {
								messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getNoIncludes());
							}
						}

						if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT) {
							messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
						} else {
							messagePanel.getWarningMessageAlert().clearAlert();
						}
						// 508 changes for Library Alias.
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getIncludeView().getAliasNameTxtArea());
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
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsTab().setVisible(false);
				}
				else {
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsTab().setVisible(true);
					cqlWorkspaceView.getCqlLeftNavBarPanelView().updateComponentInformation(results);
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
				String cqlLibraryName = cqlWorkspaceView.getCqlGeneralInformationView()
						.createCQLLibraryName(MatContext.get().getCurrentMeasureName());
				cqlWorkspaceView.getCqlGeneralInformationView().getLibraryNameValue().setText(cqlLibraryName);
				
				cqlLibraryComment = result.getCqlModel().getLibraryComment();
				String measureVersion = MatContext.get().getCurrentMeasureVersion();

				measureVersion = measureVersion.replaceAll("Draft ", "").trim();
				if (measureVersion.startsWith("v")) {
					measureVersion = measureVersion.substring(1);
				}
				cqlWorkspaceView.getCqlGeneralInformationView().setGeneralInfoOfLibrary(cqlLibraryName, measureVersion,
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
			cqlWorkspaceView.getCqlLeftNavBarPanelView().setAppliedQdmTableList(appliedValueSetTableList);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().setAppliedCodeTableList(appliedCodeTableList);
			if ((result.getCqlModel().getDefinitionList() != null)
					&& (result.getCqlModel().getDefinitionList().size() > 0)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setViewDefinitions(result.getCqlModel().getDefinitionList());
				cqlWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
				cqlWorkspaceView.getCqlLeftNavBarPanelView().updateDefineMap();
				MatContext.get().setDefinitions(getDefinitionList(result.getCqlModel().getDefinitionList()));
			} else {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineBadge().setText("00");
			}
			if ((result.getCqlModel().getCqlParameters() != null)
					&& (result.getCqlModel().getCqlParameters().size() > 0)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setViewParameterList(result.getCqlModel().getCqlParameters());
				cqlWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
				cqlWorkspaceView.getCqlLeftNavBarPanelView().updateParamMap();
				MatContext.get().setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
			} else {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getParamBadge().setText("00");
			}
			if ((result.getCqlModel().getCqlFunctions() != null)
					&& (result.getCqlModel().getCqlFunctions().size() > 0)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setViewFunctions(result.getCqlModel().getCqlFunctions());
				cqlWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
				cqlWorkspaceView.getCqlLeftNavBarPanelView().updateFunctionMap();
				MatContext.get().setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
			} else {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionBadge().setText("00");
			}
			if ((result.getCqlModel().getCqlIncludeLibrarys() != null)
					&& (result.getCqlModel().getCqlIncludeLibrarys().size() > 0)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(filterComponentMeasuresFromIncludedLibraries(result.getCqlModel().getCqlIncludeLibrarys()));
				cqlWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
				cqlWorkspaceView.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
				MatContext.get().setIncludedValues(result);

			} else {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesBadge().setText("00");
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap().clear();
			}

			boolean isValidQDMVersion = cqlWorkspaceView.getCqlLeftNavBarPanelView().checkForIncludedLibrariesQDMVersion(false); //false because it is a measures cql view
			if (!isValidQDMVersion) {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_QDM_VERSION_IN_INCLUDES());
			} else {
				messagePanel.getErrorMessageAlert().clearAlert();
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

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getGeneralInformation().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
				generalInfoEvent();
			}
		});

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
					showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					includesEvent();
				}

			}
		});
		
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsTab().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_COMPONENTS_MENU;
					showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					componentsEvent();
				}

			}
		});

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQDM().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				appliedQDMEvent();
				// 508 : Shift focus to search panel.
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getValueSetView().getOIDInput());
			}
		});

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
					showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					parameterEvent();
				}

			}
		});

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
					showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					definitionEvent();
				}
			}
		});

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
					showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					functionEvent();
				}
			}
		});

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewCQL().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.hideAceEditorAutoCompletePopUp();
				viewCqlEvent();
			}
		});

		cqlWorkspaceView.getCqlLeftNavBarPanelView().getCodesLibrary().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_CODES;
					showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					codesEvent();
					// 508 : Shift focus to code search panel. //TODO why are we using the left nav just to set focus on a text box?
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getCodesView().getCodeSearchInput());
				}
			}
		});

	}

	/**
	 * Build View for General info when General Info AnchorList item is clicked.
	 */
	private void generalInfoEvent() {
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			cqlWorkspaceView.buildGeneralInformation();
			boolean isValidQDMVersion = cqlWorkspaceView.getCqlLeftNavBarPanelView().checkForIncludedLibrariesQDMVersion(false); //false because it is a measures cql view
			if (!isValidQDMVersion) {
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_QDM_VERSION_IN_INCLUDES());
			} else {
				messagePanel.getErrorMessageAlert().clearAlert();
			}
			cqlWorkspaceView.getCqlGeneralInformationView().getComments().setText(cqlLibraryComment);
		}

		cqlWorkspaceView.setGeneralInfoHeading();
		cqlWorkspaceView.getCqlGeneralInformationView().getComments().setCursorPos(0);
	}

	/**
	 * Applied QDM event.
	 */
	private void appliedQDMEvent() {
		// server
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			cqlWorkspaceView.getValueSetView().getPasteButton()
					.setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
			buildAppliedQDMTable();
		}
		//On load of Value Sets page, set the Programs from VSAC 
		loadProgramsAndReleases();		
		cqlWorkspaceView.getValueSetView().setHeading("CQL Workspace > Value Sets", "subQDMAPPliedListContainerPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	private void loadProgramsAndReleases() {
		CQLAppliedValueSetUtility.loadProgramsAndReleases(cqlWorkspaceView.getValueSetView().getProgramListBox(), cqlWorkspaceView.getValueSetView().getReleaseListBox());
	}

	
	private void buildAppliedQDMTable() {
		cqlWorkspaceView.buildAppliedQDM();
		boolean isEditable = MatContext.get().getMeasureLockService().checkForEditPermission();

		// initialize the valuesets to be used, getUsedArtifacts() will update with the
		// proper value
		for (CQLQualityDataSetDTO valuset : cqlWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQdmTableList()) {
			valuset.setUsed(true);
		}

		cqlWorkspaceView.getValueSetView().buildAppliedValueSetCellTable(
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQdmTableList(), isEditable);
		cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
		cqlWorkspaceView.getValueSetView().setWidgetsReadOnly(isEditable);
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
							for (CQLQualityDataSetDTO cqlDTo : cqlWorkspaceView.getCqlLeftNavBarPanelView()
									.getAppliedQdmTableList()) {
								cqlDTo.setUsed(false);
							}
						} else { // otherwise, check if the valueset is in the used valusets list
							for (CQLQualityDataSetDTO cqlDTo : cqlWorkspaceView.getCqlLeftNavBarPanelView()
									.getAppliedQdmTableList()) {
								if (result.getUsedCQLValueSets().contains(cqlDTo.getName())) {
									cqlDTo.setUsed(true);
								} else {
									cqlDTo.setUsed(false);
								}
							}
						}

						if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQdmTableList().size() > 0) {
							cqlWorkspaceView.getValueSetView().getCelltable().redraw();
							cqlWorkspaceView.getValueSetView().getListDataProvider().refresh();
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
						} else { // otherwise, check if the valueset is in the used valusets list
							for (CQLCode cqlCode : appliedCodeTableList) {
								if (result.getUsedCQLcodes().contains(cqlCode.getDisplayName())) {
									cqlCode.setUsed(true);
								} else {
									cqlCode.setUsed(false);
								}
							}
						}

						if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getAppliedCodeTableList().size() > 0) {
							cqlWorkspaceView.getCodesView().getCelltable().redraw();
							cqlWorkspaceView.getCodesView().getListDataProvider().refresh();
						}
					}

				});
	}

	/**
	 * Build View for Parameter when Parameter AnchorList item is clicked.
	 */
	private void parameterEvent() {
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
		cqlWorkspaceView.buildParameterLibraryView();

		cqlWorkspaceView.getCQLParametersView()
				.setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
		cqlWorkspaceView.getCQLParametersView().getAddNewButtonBar().getaddNewButton()
				.setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());

		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = cqlWorkspaceView.getCQLParametersView().getParameterAceEditor();
		cqlWorkspaceView.getCQLParametersView().setHeading("CQL Workspace > Parameter", "mainParamViewVerticalPanel");
		Mat.focusSkipLists("MeasureComposer");
	}
	
	/**
	 * Build View for Components when components AnchorList item is clicked.
	 */
	private void componentsEvent() {
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsTab().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_COMPONENTS_MENU;
		cqlWorkspaceView.getMainFlowPanel().clear();
		cqlWorkspaceView.buildComponentsView();
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(new ArrayList<CQLLibraryDataSetObject>());
		cqlWorkspaceView.getCqlLeftNavBarPanelView().updateComponentSearchBox();
		Mat.focusSkipLists("MeasureComposer");
	}
	

	/**
	 * Build View for Includes when Includes AnchorList item is clicked.
	 */
	private void includesEvent() {
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
		cqlWorkspaceView.getMainFlowPanel().clear();
		cqlWorkspaceView.getIncludeView().setIncludedList(cqlWorkspaceView.getCqlLeftNavBarPanelView()
				.getIncludedList(cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
		cqlWorkspaceView.buildIncludesView();
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(new ArrayList<CQLLibraryDataSetObject>());
		cqlWorkspaceView.getIncludeView().buildIncludeLibraryCellTable(result,
				MatContext.get().getLibraryLockService().checkForEditPermission(), true);
		cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText("");
		cqlWorkspaceView.getIncludeView().getSearchTextBox().setText("");
		cqlWorkspaceView.getIncludeView()
				.setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
		cqlWorkspaceView.getIncludeView().setHeading("CQL Workspace > Includes", "IncludeSectionContainerPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * codes event.
	 */
	private void codesEvent() {
		cqlWorkspaceView.hideInformationDropDown();
		// server
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_CODES;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_CODES;
		
			cqlWorkspaceView.buildCodes();
			cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList,
					MatContext.get().getMeasureLockService().checkForEditPermission());
			cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
			cqlWorkspaceView.getCodesView()
					.setWidgetsReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());

			getUsedCodes();
			boolean isEditable = MatContext.get().getMeasureLockService().checkForEditPermission();
			cqlWorkspaceView.getCodesView().getPasteButton().setEnabled(isEditable);

		}
		cqlWorkspaceView.getCodesView().setHeading("CQL Workspace > Codes", "codesContainerPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * Build View for Definition when Definition AnchorList item is clicked.
	 */
	private void definitionEvent() {
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
		cqlWorkspaceView.buildDefinitionLibraryView();

		cqlWorkspaceView.getCQlDefinitionsView()
				.setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
	
		cqlWorkspaceView.getCQlDefinitionsView().getAddNewButtonBar().getaddNewButton()
				.setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
		cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor();
		cqlWorkspaceView.getCQlDefinitionsView().setHeading("CQL Workspace > Definition", "mainDefViewVerticalPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * Build View for Function when Funtion AnchorList item is clicked.
	 */
	private void functionEvent() {
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		unsetActiveMenuItem(currentSection);
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
		cqlWorkspaceView.buildFunctionLibraryView();
		cqlWorkspaceView.getCqlFunctionsView()
				.setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
		
		cqlWorkspaceView.getCqlFunctionsView().getAddNewButtonBar().getaddNewButton()
				.setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
		cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
		cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor();
		cqlWorkspaceView.getCqlFunctionsView().setHeading("CQL Workspace > Function", "mainFuncViewVerticalPanel");
		Mat.focusSkipLists("MeasureComposer");

	}

	/**
	 * Build View for View Cql when View Cql AnchorList item is clicked.
	 */
	private void viewCqlEvent() {
		cqlWorkspaceView.hideInformationDropDown();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		cqlWorkspaceView.getValueSetView().getCellTableMainPanel().clear();
		cqlWorkspaceView.getCodesView().getCellTableMainPanel().clear();
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewCQL().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			cqlWorkspaceView.buildCQLFileView(MatContext.get().getMeasureLockService().checkForEditPermission());
			buildCQLView();
		}
		cqlWorkspaceView.getViewCQLView().setHeading("CQL Workspace > View CQL", "cqlViewCQL_Id");
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
		if (!cqlWorkspaceView.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			cqlWorkspaceView.resetMessageDisplay();
			if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_GENERAL_MENU)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(false);
			} else if(menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_COMPONENTS_MENU)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsTab().setActive(false);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsListBox().setSelectedIndex(-1);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsCollapse().getElement().setClassName("panel-collapse collapse");
				cqlWorkspaceView.getComponentView().clearAceEditor();
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(false);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterNameListBox().setSelectedIndex(-1);
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getParamCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getParamCollapse().getElement()
							.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineNameListBox().setSelectedIndex(-1);
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefineCollapse().getElement()
							.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(false);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getFuncNameListBox().setSelectedIndex(-1);
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement()
							.setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEW_MENU)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewCQL().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_APPLIED_QDM)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_CODES)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_INCLUDES_MENU)) {
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(false);
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesNameListbox().setSelectedIndex(-1);
				if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement()
							.setClassName("panel-collapse collapse");
				}
			}
		}
	}

	/**
	 * Method to build View for Anchor List item View CQL.
	 */
	private void buildCQLView() {
		cqlWorkspaceView.getViewCQLView().getCqlAceEditor().setText("");
		showSearchingBusy(true);
		MatContext.get().getMeasureService().getMeasureCQLLibraryData(MatContext.get().getCurrentMeasureId(),
				new AsyncCallback<SaveUpdateCQLResult>() {
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							if ((result.getCqlString() != null) && !result.getCqlString().isEmpty()) {
								
								cqlWorkspaceView.getViewCQLView().getCqlAceEditor().clearAnnotations();
								cqlWorkspaceView.getViewCQLView().getCqlAceEditor().removeAllMarkers();
								cqlWorkspaceView.getViewCQLView().getCqlAceEditor().redisplay();
								messagePanel.clearAlerts();

								if (!result.getCqlErrors().isEmpty()) {
									messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVIEW_CQL_ERROR_MESSAGE());
									for (CQLErrors error : result.getCqlErrors()) {
										String errorMessage = new String();
										errorMessage = errorMessage.concat("Error in line : " + error.getErrorInLine()
												+ " at Offset :" + error.getErrorAtOffeset());
										int line = error.getErrorInLine();
										int column = error.getErrorAtOffeset();
										cqlWorkspaceView.getViewCQLView().getCqlAceEditor().addAnnotation(line - 1, column,
												error.getErrorMessage(), AceAnnotationType.ERROR);
									}
									cqlWorkspaceView.getViewCQLView().getCqlAceEditor().setText(result.getCqlString());
									cqlWorkspaceView.getViewCQLView().getCqlAceEditor().setAnnotations();
									cqlWorkspaceView.getViewCQLView().getCqlAceEditor().redisplay();
								} else if (!result.isDatatypeUsedCorrectly()) {
									messagePanel.clearAlerts();
									messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVIEW_CQL_ERROR_MESSAGE_BAD_VALUESET_DATATYPE());
									cqlWorkspaceView.getViewCQLView().getCqlAceEditor().setText(result.getCqlString());
								} else {
									messagePanel.getSuccessMessageAlert().setVisible(true);
									messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVIEW_CQL_NO_ERRORS_MESSAGE());
									cqlWorkspaceView.getViewCQLView().getCqlAceEditor().setText(result.getCqlString());
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
	public static CQLWorkSpaceView getSearchDisplay() {
		return cqlWorkspaceView;
	}

	private boolean validateCQLArtifact(SaveUpdateCQLResult result) {
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
	 * Delete definition.
	 */
	protected void deleteDefinition() {
		cqlWorkspaceView.resetMessageDisplay();
		final String definitionName = cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().getText();
		
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
			final CQLDefinition toBeModifiedObj = cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionMap()
					.get(cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
			if (toBeModifiedObj.isSupplDataElement()) {
				messagePanel.getSuccessMessageAlert().clearAlert();
				messagePanel.getErrorMessageAlert().createAlert("Unauthorized delete operation.");
				cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
			} else {
				showSearchingBusy(true);
				MatContext.get().getMeasureService().deleteDefinition(MatContext.get().getCurrentMeasureId(),
						toBeModifiedObj, cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewDefinitions(),
						new AsyncCallback<SaveUpdateCQLResult>() {

							@Override
							public void onFailure(Throwable caught) {
								cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
								messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if (result != null) {
									if (result.isSuccess()) {
										cqlWorkspaceView.getCqlLeftNavBarPanelView()
												.setViewDefinitions(result.getCqlModel().getDefinitionList());
										MatContext.get().setDefinitions(
												getDefinitionList(result.getCqlModel().getDefinitionList()));
										cqlWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
										cqlWorkspaceView.getCqlLeftNavBarPanelView().updateDefineMap();
										messagePanel.getErrorMessageAlert().clearAlert();
										messagePanel.getSuccessMessageAlert().setVisible(true);

										cqlWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox()
												.setText("");
										cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().setText("");
										cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().setText("");
										cqlWorkspaceView.getCqlLeftNavBarPanelView()
												.setCurrentSelectedDefinitionObjId(null);
										cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
										cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().removeAllMarkers();
										// Commenting below code as its taking away focus and that makes our application
										// not 508 compliant with other fields.
										// searchDisplay.getCQlDefinitionsView().getDefineAceEditor().redisplay();
										cqlWorkspaceView.getCQlDefinitionsView().getDefineAceEditor().setAnnotations();
										cqlWorkspaceView.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton()
												.setEnabled(false);
										messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSuccessfulDefinitionRemoveMessage(toBeModifiedObj.getName()));
										cqlWorkspaceView.getCQlDefinitionsView().getReturnTypeTextBox().setText("");

									} else if (result.getFailureReason() == 2) { //TODO 2 is not a clear failure reason
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify.");
										cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
									} else if (result.getFailureReason() == 4) {
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert("Unauthorized delete operation.");
										cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea()
												.setText(definitionName.trim());
									}
								}
								showSearchingBusy(false);
								// 508 changes for Definitions Section
								cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(
										cqlWorkspaceView.getCQlDefinitionsView().getMainDefineViewVerticalPanel());
							}
						});
			}

		} else {
			cqlWorkspaceView.resetMessageDisplay();
			messagePanel.getErrorMessageAlert().createAlert("Please select a definition to delete.");
			cqlWorkspaceView.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
		}
	}

	/**
	 * Delete function.
	 */
	protected void deleteFunction() {

		cqlWorkspaceView.resetMessageDisplay();
		final String functionName = cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea().getText();
		
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
			final CQLFunctions toBeModifiedFuncObj = cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionMap()
					.get(cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
			showSearchingBusy(true);
			MatContext.get().getMeasureService().deleteFunctions(MatContext.get().getCurrentMeasureId(),
					toBeModifiedFuncObj, cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewFunctions(),
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
									cqlWorkspaceView.getCqlLeftNavBarPanelView()
											.setViewFunctions(result.getCqlModel().getCqlFunctions());
									MatContext.get().setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
									cqlWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
									messagePanel.getErrorMessageAlert().clearAlert();
									cqlWorkspaceView.getCqlLeftNavBarPanelView().updateFunctionMap();
									cqlWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText("");
									messagePanel.getSuccessMessageAlert().setVisible(true);
									cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea().setText("");
									cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().setText("");
									cqlWorkspaceView.getCqlFunctionsView().getFunctionArgNameMap().clear();
									cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList().clear();
									cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
									cqlWorkspaceView.getCqlLeftNavBarPanelView()
											.setCurrentSelectedFunctionArgumentObjId(null);
									cqlWorkspaceView.getCqlLeftNavBarPanelView()
											.setCurrentSelectedFunctionArgumentName(null);
									cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
									cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
									cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
									// Commenting below code as its taking away focus and that makes our application
									// not 508 compliant with other fields.
									// searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().redisplay();
									cqlWorkspaceView.getCqlFunctionsView().getFunctionBodyAceEditor().setAnnotations();
									cqlWorkspaceView.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton()
											.setEnabled(false);
									messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSuccessfulFunctionRemoveMessage(toBeModifiedFuncObj.getName()));
									cqlWorkspaceView.getCqlFunctionsView().getReturnTypeTextBox().setText("");
									if (result.getFunction() != null) {
										cqlWorkspaceView.getCqlFunctionsView().createAddArgumentViewForFunctions(
												new ArrayList<CQLFunctionArgument>(),
												MatContext.get().getMeasureLockService().checkForEditPermission());
									}
								} else if (result.getFailureReason() == 2) { //TODO this failure reason is not clear
									messagePanel.getSuccessMessageAlert().clearAlert();
									messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify."); //TODO make duplicate strings constants
									cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea()
											.setText(functionName.trim());
								} else if (result.getFailureReason() == 4) {
									messagePanel.getSuccessMessageAlert().clearAlert();
									messagePanel.getErrorMessageAlert().createAlert("Unauthorized delete operation.");
									cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea()
											.setText(functionName.trim());
								}

							}
							showSearchingBusy(false);
							// 508 changes for Functions Section
							cqlWorkspaceView.getCqlLeftNavBarPanelView()
									.setFocus(cqlWorkspaceView.getCqlFunctionsView().getMainFunctionVerticalPanel());
						}
					});
		} else {
			cqlWorkspaceView.resetMessageDisplay();
			messagePanel.getErrorMessageAlert().createAlert("Please select a function to delete.");
			cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
		}
		
	}

	protected void deleteFunctionArgument() {

		String funcArgName = null;

		cqlWorkspaceView.resetMessageDisplay();
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		Iterator<CQLFunctionArgument> iterator = cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList()
				.iterator();
		cqlWorkspaceView.getCqlFunctionsView().getFunctionArgNameMap().remove(
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentName().toLowerCase());
		while (iterator.hasNext()) {
			CQLFunctionArgument cqlFunArgument = iterator.next();
			if (cqlFunArgument.getId()
					.equals(cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId())) {

				iterator.remove();
				cqlWorkspaceView.getCqlFunctionsView().createAddArgumentViewForFunctions(
						cqlWorkspaceView.getCqlFunctionsView().getFunctionArgumentList(),
						MatContext.get().getMeasureLockService().checkForEditPermission());
				funcArgName = cqlFunArgument.getArgumentName();
				break;
			}
		}

		// resetting name and id
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		// 508 changes for Functions Section
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getCqlFunctionsView().getFuncNameTxtArea());

		messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSuccessfulFunctionArgumentRemoveMessage(funcArgName));
	}

	/**
	 * Delete parameter.
	 */
	protected void deleteParameter() {

		cqlWorkspaceView.resetMessageDisplay();
		final String parameterName = cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().getText();
		
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
			final CQLParameter toBeModifiedParamObj = cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterMap()
					.get(cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());
			if (toBeModifiedParamObj.isReadOnly()) {
				messagePanel.getSuccessMessageAlert().clearAlert();
				messagePanel.getErrorMessageAlert().createAlert("Unauthorized delete operation.");
				cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
			} else {
				showSearchingBusy(true);
				MatContext.get().getMeasureService().deleteParameter(MatContext.get().getCurrentMeasureId(),
						toBeModifiedParamObj, cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewParameterList(),
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
										cqlWorkspaceView.getCqlLeftNavBarPanelView()
												.setViewParameterList((result.getCqlModel().getCqlParameters()));
										MatContext.get().setParameters(
												getParamaterList(result.getCqlModel().getCqlParameters()));
										cqlWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
										cqlWorkspaceView.getCqlLeftNavBarPanelView().updateParamMap();
										messagePanel.getErrorMessageAlert().clearAlert();
										messagePanel.getSuccessMessageAlert().setVisible(true);
										cqlWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox()
												.setText("");
										cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText("");
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().setText("");
										cqlWorkspaceView.getCqlLeftNavBarPanelView()
												.setCurrentSelectedParamerterObjId(null);
										cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().clearAnnotations();
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
										// Commenting below code as its taking away focus and that makes our application
										// not 508 compliant with other fields.
										// searchDisplay.getCQLParametersView().getParameterAceEditor().redisplay();
										cqlWorkspaceView.getCQLParametersView().getParameterAceEditor().setAnnotations();
										cqlWorkspaceView.getCQLParametersView().getParameterButtonBar().getDeleteButton()
												.setEnabled(false);
										messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSuccessfulParameterRemoveMessage(toBeModifiedParamObj.getName()));
									} else if (result.getFailureReason() == 2) { //TODO this isn't clear
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify."); //TODO make constants
										cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea()
												.setText(parameterName.trim());
									} else if (result.getFailureReason() == 4) { //TODO move some of this repeated code to a method
										messagePanel.getSuccessMessageAlert().clearAlert();
										messagePanel.getErrorMessageAlert().createAlert("Unauthorized delete operation.");
										cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea()
												.setText(parameterName.trim());
									}
								}
								showSearchingBusy(false);
								// 508 change to parameter section
								cqlWorkspaceView.getCqlLeftNavBarPanelView()
										.setFocus(cqlWorkspaceView.getCQLParametersView().getMainParamViewVerticalPanel());
							}
						});
			}
		} else {
			cqlWorkspaceView.resetMessageDisplay();
			messagePanel.getErrorMessageAlert().createAlert("Please select parameter to delete.");
			cqlWorkspaceView.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
		}
		
	}

	/**
	 * Delete include.
	 */
	protected void deleteInclude() {

		cqlWorkspaceView.resetMessageDisplay();
		final String aliasName = cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().getText();
		
		if (cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() != null) {
			final CQLIncludeLibrary toBeModifiedIncludeObj = cqlWorkspaceView.getCqlLeftNavBarPanelView()
					.getIncludeLibraryMap()
					.get(cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId());
			showSearchingBusy(true);
			MatContext.get().getMeasureService().deleteInclude(MatContext.get().getCurrentMeasureId(),
					toBeModifiedIncludeObj, cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
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
									cqlWorkspaceView.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(filterComponentMeasuresFromIncludedLibraries(result.getCqlModel().getCqlIncludeLibrarys()));
									MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
									MatContext.get().setIncludedValues(result);

									cqlWorkspaceView.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
									cqlWorkspaceView.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
									messagePanel.getErrorMessageAlert().clearAlert();
									messagePanel.getSuccessMessageAlert().setVisible(true);
									cqlWorkspaceView.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox()
											.setText("");
									cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText("");
									cqlWorkspaceView.getIncludeView().getCqlLibraryNameTextBox().setText("");
									cqlWorkspaceView.getIncludeView().getOwnerNameTextBox().setText("");
									cqlWorkspaceView.getIncludeView().getViewCQLEditor().setText("");
									cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
									cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsPageDirty(false);
									cqlWorkspaceView.getIncludeView().getViewCQLEditor().clearAnnotations();
									cqlWorkspaceView.getIncludeView().getViewCQLEditor().removeAllMarkers();
									cqlWorkspaceView.getIncludeView().getViewCQLEditor().setAnnotations();
									cqlWorkspaceView.getIncludeView().getDeleteButton().setEnabled(false);

									cqlWorkspaceView.getIncludeView().getCloseButton()
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
								} else if (result.getFailureReason() == 2) {
									messagePanel.getSuccessMessageAlert().clearAlert();
									messagePanel.getErrorMessageAlert().createAlert("Unable to find Node to modify.");
									cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
								}
							}
							showSearchingBusy(false);
						}
					});
		} else {
			cqlWorkspaceView.resetMessageDisplay();
			messagePanel.getErrorMessageAlert().createAlert("Please select an alias to delete.");
			cqlWorkspaceView.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
		}
	}

	private void deleteCode() {
		cqlWorkspaceView.resetMessageDisplay();
		showSearchingBusy(true);
		MatContext.get().getMeasureService().deleteCode(
				cqlWorkspaceView.getCqlLeftNavBarPanelView().getCurrentSelectedCodesObjId(),
				MatContext.get().getCurrentMeasureId(), new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onFailure(Throwable caught) {
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
						showSearchingBusy(false);
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						showSearchingBusy(false);
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
						if (result.isSuccess()) {
							messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_CODE_REMOVE_MSG(result.getCqlCode().getCodeOID()));
							cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
							appliedCodeTableList.clear();
							appliedCodeTableList.addAll(result.getCqlCodeList());
							cqlWorkspaceView.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
							cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList,
									MatContext.get().getMeasureLockService().checkForEditPermission());
							getAppliedValueSetList();
						} else {
							messagePanel.getErrorMessageAlert().createAlert("Unable to delete.");
						}
						// 508 : Shift focus to code search panel.
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getCodesView().getCodeSearchInput());
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
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(null);
						showSearchingBusy(false);
					}

					@Override
					public void onFailure(Throwable caught) {
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(null);
						showSearchingBusy(false);
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}
				});
	}

	private void copyValueSets() {
		cqlWorkspaceView.resetMessageDisplay();
		if (cqlWorkspaceView.getValueSetView().getQdmSelectedList() != null &&
				cqlWorkspaceView.getValueSetView().getQdmSelectedList().size() > 0) {
			mat.model.GlobalCopyPasteObject gbCopyPaste = new GlobalCopyPasteObject();
			gbCopyPaste.setCopiedValueSetList(cqlWorkspaceView.getValueSetView().getQdmSelectedList());
			MatContext.get().setGlobalCopyPaste(gbCopyPaste);
			messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().VALUE_SETS_COPIED_SUCCESSFULLY);
		} else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCOPY_QDM_SELECT_ATLEAST_ONE());
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

	/**
	 * paste Value Sets. This functionality is to paste all the Value sets elements
	 * that have been copied from any Measure and can be pasted to any measure.
	 */
	private void pasteValueSets() {
		showSearchingBusy(true);
		cqlWorkspaceView.resetMessageDisplay();
		GlobalCopyPasteObject gbCopyPaste = MatContext.get().getGlobalCopyPaste();
		if ((gbCopyPaste != null) && (gbCopyPaste.getCopiedValueSetList().size() > 0)) {
			List<CQLValueSetTransferObject> cqlValueSetTransferObjectsList = cqlWorkspaceView.getValueSetView()
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
									messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFULLY_VALUESET_PASTE());
								}
							}
						});
			} else {
				showSearchingBusy(false);
				messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFULLY_VALUESET_PASTE());
			}
			MatContext.get().getGlobalCopyPaste().getCopiedValueSetList().clear();
		} else {
			showSearchingBusy(false);
			messagePanel.getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWARNING_PASTING_IN_VALUESET());
		}

	}

	/**
	 * Adds the QDM Search Panel event Handlers.
	 */
	private void addValueSetSearchPanelHandlers() {

		cqlWorkspaceView.getValueSetView().getCopyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				copyValueSets();
			}
		});

		cqlWorkspaceView.getValueSetView().getSelectAllButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectAllValueSets();
			}
		});
		
		cqlWorkspaceView.getValueSetView().getPasteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					pasteValueSets();
				} else {
					event.preventDefault();
				}
			}
		});
		
		cqlWorkspaceView.getValueSetView().getReleaseListBox().addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				isRetrieveButtonEnabled = true;
				cqlWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);		
				previousIsApplyButtonEnabled = isApplyButtonEnabled;
				isApplyButtonEnabled = false;
				cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(isApplyButtonEnabled);
				alert508StateChanges();
			}
		});
		
		cqlWorkspaceView.getValueSetView().getProgramListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				isRetrieveButtonEnabled = true;
				cqlWorkspaceView.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);
				
				previousIsApplyButtonEnabled = isApplyButtonEnabled;
				isApplyButtonEnabled = false; 
				cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(isApplyButtonEnabled);
				
				CQLAppliedValueSetUtility.loadReleases(cqlWorkspaceView.getValueSetView().getReleaseListBox(), cqlWorkspaceView.getValueSetView().getProgramListBox());
				
				alert508StateChanges();
			}
		});

		/**
		 * this functionality is to clear the content on the ValueSet Search Panel.
		 */
		cqlWorkspaceView.getValueSetView().getCancelQDMButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cqlWorkspaceView.resetMessageDisplay();
				isModified = false;
				cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
				//508 compliance for Value Sets
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getValueSetView().getOIDInput());
				
				previousIsProgramReleaseBoxEnabled = isProgramReleaseBoxEnabled;
				isProgramReleaseBoxEnabled = true; 
				
				loadProgramsAndReleases(); 
				alert508StateChanges();
			}
		});

		cqlWorkspaceView.getValueSetView().getUpdateFromVSACButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					cqlWorkspaceView.resetMessageDisplay();
					updateVSACValueSets();
					// 508 compliance for Value Sets
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getValueSetView().getOIDInput());
				}
			}
		});

		/**
		 * this functionality is to retrieve the value set from VSAC with latest
		 * information which consists of Expansion Profile list and Version List.
		 */
		cqlWorkspaceView.getValueSetView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
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
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getValueSetView().getOIDInput());
					}
				}
			}
		});

		/**
		 * this handler is invoked when apply button is clicked on search Panel in Value
		 * Set tab and this is to add new value set or user Defined Value Set to the
		 * Applied Value Set list.
		 */
		cqlWorkspaceView.getValueSetView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					MatContext.get().clearDVIMessages();
					cqlWorkspaceView.resetMessageDisplay();

					if (isModified && (modifyValueSetDTO != null)) {
						modifyValueSetOrUserDefined(isUserDefined);
					} else {
						addNewValueSet(isUserDefined);
					}
					// 508 compliance for Value Sets
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getValueSetView().getOIDInput());
				}
			}
		});

		/**
		 * Adding value Change handler for UserDefined Input in Search Panel in QDM
		 * Elements Tab
		 * 
		 */
		cqlWorkspaceView.getValueSetView().getUserDefinedInput().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				cqlWorkspaceView.resetMessageDisplay();
				isUserDefined = cqlWorkspaceView.getValueSetView().validateUserDefinedInput();
			}
		});

		/**
		 * Adding value change handler for OID input in Search Panel in QDM elements Tab
		 */

		cqlWorkspaceView.getValueSetView().getOIDInput().addValueChangeHandler(event -> clearOID());
		
		cqlWorkspaceView.getValueSetView().getOIDInput().sinkBitlessEvent("input");
		
		cqlWorkspaceView.getValueSetView().getClearButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (!cqlWorkspaceView.getValueSetView().getIsLoading()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getValueSetView().clearSelectedCheckBoxes();
				}
			}
		});

		addValueSetObserverHandler();
	}
		
	private void clearOID() {

		previousIsRetrieveButtonEnabled = isRetrieveButtonEnabled;
		previousIsProgramReleaseBoxEnabled = isProgramReleaseBoxEnabled;
		
		cqlWorkspaceView.resetMessageDisplay();		
		isUserDefined = cqlWorkspaceView.getValueSetView().validateOIDInput();
		
		if (cqlWorkspaceView.getValueSetView().getOIDInput().getValue().length() <= 0 ) {
			isRetrieveButtonEnabled = true;
			isProgramReleaseBoxEnabled = true;
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
		
		helpTextBuilder.append(build508HelpString(previousIsProgramReleaseBoxEnabled, isProgramReleaseBoxEnabled, "Program and Release List Boxes"));
		helpTextBuilder.append(build508HelpString(previousIsRetrieveButtonEnabled, isRetrieveButtonEnabled, "Retrieve Button"));
		helpTextBuilder.append(build508HelpString(previousIsApplyButtonEnabled, isApplyButtonEnabled, "Apply Button"));
		
		cqlWorkspaceView.getValueSetView().getHelpBlock().setText(helpTextBuilder.toString());
	}
	
	private String build508HelpString(boolean previousState, boolean currentState, String elementName) {
		
		String helpString = "";
		if(currentState != previousState) {
			helpString = elementName.concat(" ").concat(Boolean.TRUE.equals(currentState) ? "enabled" : "disabled");
		}
		
		return helpString; 
	}

	private void addCodeSearchPanelHandlers() {
		cqlWorkspaceView.getCodesView().getCopyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				copyCodes();
			}
		});

		cqlWorkspaceView.getCodesView().getPasteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					pasteCodes();
				} else {
					event.preventDefault();
				}
			}
		});
		
		cqlWorkspaceView.getCodesView().getSelectAllButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectAllCodes();
			}
		});
		
		cqlWorkspaceView.getCodesView().getClearButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (!cqlWorkspaceView.getCodesView().getIsLoading()) {
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getCodesView().clearSelectedCheckBoxes();
				}
			}
		});
		cqlWorkspaceView.getCodesView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					cqlWorkspaceView.resetMessageDisplay();
					if (!isCodeModified)
						searchCQLCodesInVsac();
					// 508 Compliance for Codes section
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getCodesView().getCodeInput());
				}
			}
		});

		cqlWorkspaceView.getCodesView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					MatContext.get().clearDVIMessages();
					cqlWorkspaceView.resetMessageDisplay();
					if(isCodeModified && modifyCQLCode != null) {
						modifyCodes();
					} else if (null != cqlWorkspaceView.getCodesView().getCodeSearchInput().getValue() 
							&& !cqlWorkspaceView.getCodesView().getCodeSearchInput().getValue().isEmpty()) {
						addNewCodes();	
					}
					// 508 Compliance for Codes section
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getCodesView().getCodeInput());
				}
			}
		});

		cqlWorkspaceView.getCodesView().getCancelCodeButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					isCodeModified = false;
					cqlWorkspaceView.resetMessageDisplay();
					cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
					// 508 Compliance for Codes section
					cqlWorkspaceView.getCqlLeftNavBarPanelView()
							.setFocus(cqlWorkspaceView.getCodesView().getCodeSearchInput());
				}
			}
		});

		cqlWorkspaceView.getCodesView().setDelegator(new Delegator() {

			@Override
			public void onDeleteClicked(CQLCode result, int index) {
				if (!cqlWorkspaceView.getCodesView().getIsLoading()) {
					messagePanel.getSuccessMessageAlert().clearAlert();
					messagePanel.getErrorMessageAlert().clearAlert();
					if (result != null) {
						isCodeModified = false;
						modifyCQLCode = null;
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(result.getId());
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate()
										.getDELETE_CONFIRMATION_CODES(result.getCodeOID()));
						cqlWorkspaceView.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
						// 508 Compliance for Codes section
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getCodesView().getCodeInput());
					}
				} else {
					// table is loading, do nothing
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
					//508 Compliance for Value Sets section
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getCodesView().getCodeInput());
				} else {
					//do nothing when loading.
				}
			}
		});
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
		codesView.setCodeSystemOid(cqlCode.getCodeSystemOID());
		codesView.getCodeSystemVersionInput().setValue(cqlCode.getCodeSystemVersion());
		codesView.getIncludeCodeSystemVersionCheckBox().setValue(cqlCode.isIsCodeSystemVersionIncluded());
		codesView.getSaveButton().setEnabled(true);
	}
	
	private void copyCodes() {
		cqlWorkspaceView.resetMessageDisplay();
		if (cqlWorkspaceView.getCodesView().getCodesSelectedList() != null && cqlWorkspaceView.getCodesView().getCodesSelectedList().size() > 0) {
			mat.model.GlobalCopyPasteObject gbCopyPaste = new GlobalCopyPasteObject();
			gbCopyPaste.setCopiedCodeList(cqlWorkspaceView.getCodesView().getCodesSelectedList());
			MatContext.get().setGlobalCopyPaste(gbCopyPaste);
			messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().CODES_COPIED_SUCCESSFULLY);
		} else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCOPY_CODE_SELECT_ATLEAST_ONE());
		}
	}
	
	private void selectAllCodes() {
		cqlWorkspaceView.resetMessageDisplay();
		if(cqlWorkspaceView.getCodesView().getAllCodes() != null) {
			cqlWorkspaceView.getCodesView().selectAll();
			messagePanel.getSuccessMessageAlert().createAlert(CODES_SELECTED_SUCCESSFULLY);
		}
	}

	/**
	 * paste Value Sets. This functionality is to paste all the Value sets elements
	 * that have been copied from any Measure and can be pasted to any measure.
	 */
	private void pasteCodes() {
		cqlWorkspaceView.resetMessageDisplay();
		GlobalCopyPasteObject gbCopyPaste = MatContext.get().getGlobalCopyPaste();
		if ((gbCopyPaste != null) && (gbCopyPaste.getCopiedCodeList().size() > 0)) {
			List<CQLCode> codesToPaste = cqlWorkspaceView.getCodesView().setMatCodeList(gbCopyPaste.getCopiedCodeList(),
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
						messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().SUCCESSFULLY_PASTED_CODES_IN_MEASURE);
						cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						MatContext.get().getCQLModel().setCodeList(appliedCodeTableList);
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList,
								MatContext.get().getMeasureLockService().checkForEditPermission());
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

	/**
	 * modify codes
	 */
	private void modifyCodes() {
		String measureId = MatContext.get().getCurrentMeasureId();
		final String codeName = StringUtility.removeEscapedCharsFromString(cqlWorkspaceView.getCodesView().getCodeDescriptorInput().getValue());
		CQLCode refCode = buildCQLCodeFromCodesView(codeName);
		
		MatCodeTransferObject transferObject = cqlWorkspaceView.getCodesView().getCodeTransferObject(measureId, refCode);
		
		if (null != transferObject) {
			
			appliedCodeTableList.removeIf(code -> code.getDisplayName().equals(modifyCQLCode.getDisplayName()));
			
			if(!cqlWorkspaceView.getCodesView().checkCodeInAppliedCodeTableList(refCode.getDisplayName(), appliedCodeTableList)) {

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
						messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_CODE());
						cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getMeasureLockService().checkForEditPermission());
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						if (result.getCqlModel().getAllValueSetAndCodeList() != null) {
							setAppliedValueSetListInTable(result.getCqlModel().getAllValueSetAndCodeList());
						}
						//Temporary fix to update codes for insert Icon.
						getAppliedValueSetList();
						showSearchingBusy(false);
						cqlWorkspaceView.getCodesView().getSaveButton().setEnabled(false);
						isCodeModified = false;
						modifyCQLCode = null;
					}
				});
			} else {
				messagePanel.getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(refCode.getDisplayName()));
			}

		}
	}
	
	private void addNewCodes() {
		messagePanel.getSuccessMessageAlert().clearAlert();
		messagePanel.getErrorMessageAlert().clearAlert();
		String measureId = MatContext.get().getCurrentMeasureId();
		final String codeName = StringUtility.removeEscapedCharsFromString(cqlWorkspaceView.getCodesView().getCodeDescriptorInput().getValue());
		CQLCode refCode = buildCQLCodeFromCodesView(codeName);

		final String codeSystemName = refCode.getCodeSystemName();
		final String codeId = refCode.getCodeOID();

		MatCodeTransferObject transferObject = cqlWorkspaceView.getCodesView().getCodeTransferObject(measureId, refCode);

		if (null != transferObject) {
			showSearchingBusy(true);
			service.saveCQLCodestoMeasure(transferObject, new AsyncCallback<SaveUpdateCQLResult>() {

				@Override
				public void onSuccess(SaveUpdateCQLResult result) {
					showSearchingBusy(false);
					if (result.isSuccess()) {
						messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCodeSuccessMessage(cqlWorkspaceView.getCodesView().getCodeInput().getText()));
						cqlWorkspaceView.getCodesView().resetCQLCodesSearchPanel();
						appliedCodeTableList.clear();
						appliedCodeTableList.addAll(result.getCqlCodeList());
						cqlWorkspaceView.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
						cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList,
								MatContext.get().getMeasureLockService().checkForEditPermission());
						getAppliedValueSetList();
					} else {
						messagePanel.getSuccessMessageAlert().clearAlert();
						if (result.getFailureReason() == result.getDuplicateCode()) {
							messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().generateDuplicateErrorMessage(codeName));
							cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getMeasureLockService().checkForEditPermission());
						}

						else if (result.getFailureReason() == result.getBirthdateOrDeadError()) {
							messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getBirthdateOrDeadMessage(codeSystemName, codeId));
							cqlWorkspaceView.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getMeasureLockService().checkForEditPermission());
						}
					}
					// 508 : Shift focus to code search panel.
					cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getCodesView().getCodeSearchInput());
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
							messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVSAC_UPDATE_SUCCESSFULL());
							List<CQLQualityDataSetDTO> appliedListModel = new ArrayList<CQLQualityDataSetDTO>();
							for (CQLQualityDataSetDTO cqlQDMDTO : result.getUpdatedCQLQualityDataDTOLIst()) {
								if (!ConstantMessages.DEAD_OID.equals(cqlQDMDTO.getDataType())
										&& !ConstantMessages.BIRTHDATE_OID.equals(cqlQDMDTO.getDataType())
										&& (cqlQDMDTO.getType() == null)) {
									appliedListModel.add(cqlQDMDTO);
									// Update existing Table value set list
									for (CQLQualityDataSetDTO cqlQualityDataSetDTO : appliedValueSetTableList) {
										if (cqlQualityDataSetDTO.getId().equals(cqlQDMDTO.getId())) {
											cqlQualityDataSetDTO.setOriginalCodeListName(cqlQDMDTO.getOriginalCodeListName());
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
									for (CQLQualityDataSetDTO dataSetDTO : MatContext.get().getValueSetCodeQualityDataSetList()) {
										if (dataSetDTO.getId().equals(cqlQDMDTO.getId())) {
											dataSetDTO.setOriginalCodeListName(cqlQDMDTO.getOriginalCodeListName());
											dataSetDTO.setName(cqlQDMDTO.getName());
										}
									}
								}
							}
							cqlWorkspaceView.getValueSetView().buildAppliedValueSetCellTable(appliedListModel,
									MatContext.get().getMeasureLockService().checkForEditPermission());
							cqlWorkspaceView.getCqlLeftNavBarPanelView().setAppliedQdmTableList(appliedValueSetTableList);
							cqlWorkspaceView.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
						} else {
							messagePanel.getErrorMessageAlert().createAlert(cqlWorkspaceView.getValueSetView().convertMessage(result.getFailureReason()));
						}
						showSearchingBusy(false);
					}
				});
	}

	
	private void searchCQLCodesInVsac() {

		final String url = cqlWorkspaceView.getCodesView().getCodeSearchInput().getValue().trim();
		cqlWorkspaceView.getCodesView().getCodeSearchInput().setText(url);
		messagePanel.getSuccessMessageAlert().clearAlert();
		if (!MatContext.get().isUMLSLoggedIn()) {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			messagePanel.getErrorMessageAlert().setVisible(true);
			return;
		}

		// OID validation.
		if ((url == null) || url.trim().isEmpty()) {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_CODE_IDENTIFIER_REQUIRED());
			return;
		}

		// Code Identifier validation to check Identifier starts with "CODE:"
		if (validator.validateForCodeIdentifier(url)) {
			cqlWorkspaceView.getCodesView().getSaveButton().setEnabled(false);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_INVALID_CODE_IDENTIFIER());

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
					cqlWorkspaceView.getCodesView().getCodeDescriptorInput()
							.setValue(result.getDirectReferenceCode().getCodeDescriptor());
					cqlWorkspaceView.getCodesView().getCodeInput().setValue(result.getDirectReferenceCode().getCode());
					cqlWorkspaceView.getCodesView().getCodeSystemInput()
							.setValue(result.getDirectReferenceCode().getCodeSystemName());
					cqlWorkspaceView.getCodesView().getCodeSystemVersionInput()
							.setValue(result.getDirectReferenceCode().getCodeSystemVersion());
					cqlWorkspaceView.getCodesView().setCodeSystemOid(result.getDirectReferenceCode().getCodeSystemOid());
					messagePanel.getSuccessMessageAlert().createAlert("Code " + result.getDirectReferenceCode().getCode() + " successfully retrieved from VSAC.");
					cqlWorkspaceView.getCodesView().getSaveButton().setEnabled(true);
					
					CQLCode code = buildCQLCodeFromCodesView(StringUtility.removeEscapedCharsFromString(cqlWorkspaceView.getCodesView().getCodeDescriptorInput().getValue()));
					cqlWorkspaceView.getCodesView().setValidateCodeObject(code);
					
				} else {
					String message = cqlWorkspaceView.getCodesView().convertMessage(result.getFailureReason());
					messagePanel.getErrorMessageAlert().createAlert(message);
					messagePanel.getErrorMessageAlert().setVisible(true);
				}
				
				showSearchingBusy(false);
				// 508 : Shift focus to code search panel.
				cqlWorkspaceView.getCqlLeftNavBarPanelView().setFocus(cqlWorkspaceView.getCodesView().getCodeSearchInput());
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
		final String oid = cqlWorkspaceView.getValueSetView().getOIDInput().getValue();
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
					String valueSetName = "";
					
					List<MatValueSet> matValueSets = result.getVsacResponse();					
					if (matValueSets != null) {						
						currentMatValueSet = matValueSets.get(0);
						valueSetName = currentMatValueSet.getDisplayName();
					}
					
					cqlWorkspaceView.getValueSetView().getOIDInput().setTitle(oid);
					cqlWorkspaceView.getValueSetView().getUserDefinedInput().setValue(valueSetName);
					cqlWorkspaceView.getValueSetView().getUserDefinedInput().setTitle(valueSetName);

					cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(true);					
					boolean isVersionEnabled = isListValueNotSelected(cqlWorkspaceView.getValueSetView().getProgramListBox().getSelectedValue()) 
							&& isListValueNotSelected(cqlWorkspaceView.getValueSetView().getReleaseListBox().getSelectedValue());
					cqlWorkspaceView.getValueSetView().getHelpBlock().setColor("transparent");
					cqlWorkspaceView.getValueSetView().getHelpBlock().setText("Version selection is ".concat(Boolean.TRUE.equals(isVersionEnabled) ? "enabled" : "disabled"));
					
					
					showSearchingBusy(false);
					messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getValuesetSuccessfulReterivalMessage(matValueSets.get(0).getDisplayName()));
					messagePanel.getSuccessMessageAlert().setVisible(true);
				} else {
					String message = cqlWorkspaceView.getValueSetView().convertMessage(result.getFailureReason());
					messagePanel.getErrorMessageAlert().createAlert(message);
					messagePanel.getErrorMessageAlert().setVisible(true);
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
		final String suffix = cqlWorkspaceView.getValueSetView().getSuffixInput().getValue();
		final String codeListName = (originalCodeListName != null ? originalCodeListName : "")
				+ (!suffix.isEmpty() ? " (" + suffix + ")" : "");

		// Check if QDM name already exists in the list.
		if (!cqlWorkspaceView.getValueSetView().checkNameInValueSetList(codeListName, appliedValueSetTableList)) {
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
							cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
						}

						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							String message = "";
							if (result != null) {
								if (result.isSuccess()) {

									message = MatContext.get().getMessageDelegate()
											.getValuesetSuccessMessage(codeListName);
									MatContext.get().getEventBus().fireEvent(new QDSElementCreatedEvent(codeListName));
									cqlWorkspaceView.getValueSetView().resetCQLValuesetearchPanel();
									messagePanel.getSuccessMessageAlert().createAlert(message);
									previousIsProgramReleaseBoxEnabled = isProgramReleaseBoxEnabled;
									isProgramReleaseBoxEnabled = true;
									loadProgramsAndReleases(); 
									
									getAppliedValueSetList();
								} else {
									if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
										messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getName()));
									}
								}
							}
							getUsedValueSets();
							currentMatValueSet = null;
							cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
							showSearchingBusy(false);
						}
					});
		} else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(codeListName));
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
				if (!cqlWorkspaceView.getValueSetView().checkNameInValueSetList(userDefinedInput,
						appliedValueSetTableList)) {
					showSearchingBusy(true);
					MatContext.get().getMeasureService().saveCQLUserDefinedValuesettoMeasure(matValueSetTransferObject,
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

												String message = MatContext.get().getMessageDelegate()
														.getValuesetSuccessMessage(userDefinedInput);
												messagePanel.getSuccessMessageAlert().createAlert(message);
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

									getUsedValueSets();
									cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
									showSearchingBusy(false);
								}
							});

				} else {
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
			String originalName = cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText();
			String suffix = cqlWorkspaceView.getValueSetView().getSuffixInput().getValue();
			String displayName = (!originalName.isEmpty() ? originalName : "")
					+ (!suffix.isEmpty() ? " (" + suffix + ")" : "");
			if (modifyValueSetDTO.getVersion() == null) {
				modifyValueSetDTO.setVersion("");
			}
			
			String releaseValue = cqlWorkspaceView.getValueSetView().getReleaseListBox().getSelectedValue();
			if(releaseValue == null) {
				modifyValueSetDTO.setRelease("");
			} else if(!releaseValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
				modifyValueSetDTO.setRelease(releaseValue);
				modifyValueSetDTO.setVersion("");
			} else {
				modifyValueSetDTO.setRelease("");
			}
			
			String programValue = cqlWorkspaceView.getValueSetView().getProgramListBox().getSelectedValue();
			if(!programValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
				modifyValueSetDTO.setProgram(programValue);
			} else {
				modifyValueSetDTO.setProgram("");
			}
			
			modifyValueSetList(modifyValueSetDTO);
			if (!cqlWorkspaceView.getValueSetView().checkNameInValueSetList(displayName, appliedValueSetTableList)) {
				
				if (!cqlWorkspaceView.getValueSetView().getSuffixInput().getValue().isEmpty()) {
					modifyValueSetDTO.setSuffix(cqlWorkspaceView.getValueSetView().getSuffixInput().getValue());
					modifyValueSetDTO.setName(
							originalName + " (" + cqlWorkspaceView.getValueSetView().getSuffixInput().getValue() + ")");
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

			getUsedValueSets();
		} else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE());
		}
	}

	/**
	 * Modify QDM with out value set.
	 */
	private void modifyUserDefinedValueSet() {
		modifyValueSetDTO.setVersion("");
		if ((cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText().trim().length() > 0)) {
			String originalName = cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText();
			String suffix = cqlWorkspaceView.getValueSetView().getSuffixInput().getValue();
			String usrDefDisplayName = (!originalName.isEmpty() ? originalName : "")
					+ (!suffix.isEmpty() ? " (" + suffix + ")" : "");
			
			modifyValueSetList(modifyValueSetDTO);
			if (!cqlWorkspaceView.getValueSetView().checkNameInValueSetList(usrDefDisplayName, appliedValueSetTableList)) {
				
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
			} else {
				appliedValueSetTableList.add(modifyValueSetDTO);
				messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(usrDefDisplayName));
			}

			getUsedValueSets();
		} else {
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
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
		matValueSetTransferObject.setUserDefinedText(cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText());
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
						messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
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
								messagePanel.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
								getAppliedValueSetList();
							} else {
								if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
									messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getName()));
								} else if (result
										.getFailureReason() == SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION) {
									messagePanel.getErrorMessageAlert().createAlert("Invalid Input data.");
								}
							}
						}
						cqlWorkspaceView.getValueSetView().getSaveButton().setEnabled(false);
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

		String originalCodeListName = cqlWorkspaceView.getValueSetView().getUserDefinedInput().getValue();

		matValueSetTransferObject.setCqlQualityDataSetDTO(new CQLQualityDataSetDTO());
		matValueSetTransferObject.getCqlQualityDataSetDTO().setOriginalCodeListName(originalCodeListName);

		if (!cqlWorkspaceView.getValueSetView().getSuffixInput().getValue().isEmpty()) {
			matValueSetTransferObject.getCqlQualityDataSetDTO()
					.setSuffix(cqlWorkspaceView.getValueSetView().getSuffixInput().getValue());
			matValueSetTransferObject.getCqlQualityDataSetDTO().setName(
					originalCodeListName + " (" + cqlWorkspaceView.getValueSetView().getSuffixInput().getValue() + ")");
		} else {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setName(originalCodeListName);
		}
		
		// set it to empty string to begin with
		matValueSetTransferObject.getCqlQualityDataSetDTO().setRelease("");
		String releaseValue = cqlWorkspaceView.getValueSetView().getReleaseListBox().getSelectedValue(); 
		if(!releaseValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setRelease(releaseValue);
		}
		
		matValueSetTransferObject.getCqlQualityDataSetDTO().setProgram("");
		String programValue = cqlWorkspaceView.getValueSetView().getProgramListBox().getSelectedValue();
		if(!programValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setProgram(programValue);
		}

		
		CodeListSearchDTO codeListSearchDTO = new CodeListSearchDTO();
		codeListSearchDTO.setName(cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText());
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		matValueSetTransferObject.setMatValueSet(currentMatValueSet);
		matValueSetTransferObject.setMeasureId(measureID);
		matValueSetTransferObject.setUserDefinedText(cqlWorkspaceView.getValueSetView().getUserDefinedInput().getText());
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
				cqlWorkspaceView.getCqlGeneralInformationView().setWidgetReadOnlyForMeasure(!busy);
				break;
			case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
				cqlWorkspaceView.getIncludeView().setReadOnly(!busy);
				break;
			case (CQLWorkSpaceConstants.CQL_APPLIED_QDM):
				cqlWorkspaceView.getValueSetView().setReadOnly(!busy);
				break;
			case (CQLWorkSpaceConstants.CQL_CODES):
				cqlWorkspaceView.getCodesView().setReadOnly(!busy);
				break;
			case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
				cqlWorkspaceView.getCQLParametersView().setReadOnly(!busy);
				break;
			case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
				cqlWorkspaceView.getCQlDefinitionsView().setReadOnly(!busy);
				break;
			case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
				cqlWorkspaceView.getCqlFunctionsView().setReadOnly(!busy);
				break;
			}

		}
		cqlWorkspaceView.getCqlLeftNavBarPanelView().setIsLoading(busy);

	}

	private void showSearchBusyOnDoubleClick(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getGeneralInformation().setEnabled(!busy);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getCodesLibrary().setEnabled(!busy);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getAppliedQDM().setEnabled(!busy);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getParameterLibrary().setEnabled(!busy);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getDefinitionLibrary().setEnabled(!busy);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getFunctionLibrary().setEnabled(!busy);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getViewCQL().setEnabled(!busy);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getIncludesLibrary().setEnabled(!busy);
		cqlWorkspaceView.getCqlLeftNavBarPanelView().getComponentsTab().setEnabled(!busy);
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
		cqlWorkspaceView.getValueSetView().buildAppliedValueSetCellTable(appliedValueSetTableList,
				MatContext.get().getMeasureLockService().checkForEditPermission());
		cqlWorkspaceView.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
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
	
	//TODO move to common method
	private void showUnsavedChangesWarning() {
		messagePanel.clearAlerts();
		messagePanel.getWarningConfirmationMessageAlert().createAlert();
		messagePanel.getWarningConfirmationYesButton().setFocus(true);
	}
	
	public MessagePanel getMessagePanel() {
		return messagePanel;
	}
}