package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.shared.event.HideEvent;
import org.gwtbootstrap3.client.shared.event.HideHandler;
import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
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
//import org.gwtbootstrap3.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.cqlworkspace.CQLCodesView.Delegator;
import mat.client.clause.cqlworkspace.CQLFunctionsView.Observer;
import mat.client.clause.event.QDSElementCreatedEvent;
import mat.client.codelist.HasListBox;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.JSONAttributeModeUtility;
import mat.client.shared.JSONCQLTimingExpressionUtility;
import mat.client.shared.MatContext;
import mat.client.shared.ValueSetNameInputValidator;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.CQLValueSetTransferObject;
import mat.model.CodeListSearchDTO;
import mat.model.MatCodeTransferObject;
import mat.model.MatValueSet;
import mat.model.VSACVersion;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeWrapper;
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

// TODO: Auto-generated Javadoc
/**
 * The Class CQLPresenterNavBarWithList.
 */
public class CQLWorkSpacePresenter implements MatPresenter {
	
	/** The panel. */
	private SimplePanel panel = new SimplePanel();

	/** The clicked menu. */
	private String currentSection = "general";
	/** The next clicked menu. */
	private String nextSection = "general";

	/** QDSAttributesServiceAsync instance. */
	private QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);
	/** The search display. */
	private ViewDisplay searchDisplay;

	/** The service. */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	/** The modify value set dto. */
	private CQLQualityDataSetDTO modifyValueSetDTO;

	/** The validator. */
	CQLModelValidator validator = new CQLModelValidator();

	/** The vsacapi service. */
	private final VSACAPIServiceAsync vsacapiService = MatContext.get().getVsacapiServiceAsync();

	/** The is user defined. */
	private boolean isUserDefined = false;

	/** The exp profile to all qdm. */
//	private String expProfileToAllValueSet = "";
	
	/** The setId */
	private String setId = null;
	
	private String currentIncludeLibrarySetId = null;
	
	private String currentIncludeLibraryId = null; 

	/** The is modfied. */
	private boolean isModified = false;

	/** The is expansion profile. */
	//private boolean isExpansionProfile = false;

	/** The current mat value set. */
	private MatValueSet currentMatValueSet;
	
	/** The applied QDM list. */
	private List<CQLQualityDataSetDTO> appliedValueSetTableList = new ArrayList<CQLQualityDataSetDTO>();
	
	private List<CQLCode> appliedCodeTableList = new ArrayList<CQLCode>();
	
	private AceEditor curAceEditor;
	
	/**
	 * Flag for if parameters, definitions, or functions should be formatted.
	 * For now this flag will always be set to true. 
	 */
	private boolean isFormatable = true;
	
	/**
	 * The Interface ViewDisplay.
	 */
	public static interface ViewDisplay {

		/**
		 * Top Main panel of CQL Workspace Tab.
		 * 
		 * @return HorizontalPanel
		 */
		VerticalPanel getMainPanel();

		/**
		 * Generates View for CQLWorkSpace tab.
		 */
		void buildView();

		/**
		 * Builds the general information.
		 */
		void buildGeneralInformation();

		/**
		 * Builds the includes library view.
		 */
		void buildIncludesView();
		
		/**
		 * Builds the applied QDM.
		 */
		void buildAppliedQDM();
		
		/**
		 * Builds the parameter library view.
		 */
		void buildParameterLibraryView();

		/**
		 * Builds the definition library view.
		 */
		void buildDefinitionLibraryView();

		/**
		 * Builds the function library view.
		 */
		void buildFunctionLibraryView();
		
		/**
		 * Builds the cql file view.
		 */
		void buildCQLFileView();

		/**
		 * Gets the main v panel.
		 *
		 * @return the main v panel
		 */
		Widget asWidget();

		/**
		 * Gets the clicked menu.
		 *
		 * @return the clicked menuF
		 */
		String getClickedMenu();

		/**
		 * Sets the clicked menu.
		 *
		 * @param clickedMenu
		 *            the new clicked menu
		 */
		void setClickedMenu(String clickedMenu);
		
		/**
		 * Gets the main h panel.
		 *
		 * @return the main h panel
		 */
		HorizontalPanel getMainHPanel();

		/**
		 * Sets the next clicked menu.
		 *
		 * @param nextClickedMenu
		 *            the new next clicked menu
		 */
		void setNextClickedMenu(String nextClickedMenu);

		/**
		 * Gets the next clicked menu.
		 *
		 * @return the next clicked menu
		 */
		Object getNextClickedMenu();

		/**
		 * Builds the info panel.
		 *
		 * @param source
		 *            the source
		 */
		//void buildInfoPanel(Widget source);

		/**
		 * Hide ace editor auto complete pop up.
		 */
		void hideAceEditorAutoCompletePopUp();

		/**
		 * Gets the main flow panel.
		 *
		 * @return the main flow panel
		 */
		FlowPanel getMainFlowPanel();

		/**
		 * Gets the cql left nav bar panel view.
		 *
		 * @return the cql left nav bar panel view
		 */
		CQLLeftNavBarPanelView getCqlLeftNavBarPanelView();

		/**
		 * Gets the cql general information view.
		 *
		 * @return the cql general information view
		 */
		CQLGeneralInformationView getCqlGeneralInformationView();
		
		/**
		 * Gets the include view.
		 *
		 * @return the include view
		 */
		CQLIncludeLibraryView getIncludeView();
		
		/**
		 * Gets the qdm view.
		 *
		 * @return the qdm view
		 */
		CQLAppliedValueSetView getValueSetView();

		
		/**
		 * Gets the CQL parameters view.
		 *
		 * @return the CQL parameters view
		 */
		CQLParametersView getCQLParametersView();

		/**
		 * Gets the c ql definitions view.
		 *
		 * @return the c ql definitions view
		 */
		CQlDefinitionsView getCQlDefinitionsView();
		
		/**
		 * Gets the cql functions view.
		 *
		 * @return the cql functions view
		 */
		CQLFunctionsView getCqlFunctionsView();

		/**
		 * Gets the view CQL view.
		 *
		 * @return the view CQL view
		 */
		CQLView getViewCQLView();
		
		/**
		 * Reset message display.
		 */
		void resetMessageDisplay();
		
		/**
		 * Reset all.
		 */
		void resetAll();
		
		CQLCodesView getCodesView();

		void buildCodes();

		void hideInformationDropDown();

	}

	/**
	 * Instantiates a new CQL presenter nav bar with list.
	 *
	 * @param srchDisplay
	 *            the srch display
	 */
	public CQLWorkSpacePresenter(final ViewDisplay srchDisplay) {
		searchDisplay = srchDisplay;
		addEventHandlers();
		JSONCQLTimingExpressionUtility.getAllCQLTimingExpressionsList();
		JSONAttributeModeUtility.getAllAttrModeList();
		JSONAttributeModeUtility.getAllModeDetailsList();
		
	}

	/**
	 * Build Insert Pop up.
	 */
	private void buildInsertPopUp() {
		searchDisplay.resetMessageDisplay();
		InsertIntoAceEditorDialogBox.showListOfItemAvailableForInsertDialogBox(searchDisplay
				.getCqlLeftNavBarPanelView(), curAceEditor);
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
		addIncludeCQLLibraryHandlers();
		addValueSetSearchPanelHandlers();
		addCodeSearchPanelHandlers();
		addParameterSectionHandlers();
		addDefinitionSectionHandlers();
		addFunctionSectionHandlers();
		addListBoxEventHandler();
	}

	/**
	 * Adds the delete confirmation handlers.
	 */
	private void addDeleteConfirmationHandlers() {
		
		searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBoxNoButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBoxYesButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
					deleteDefinition();
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				} else if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null
						&& searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId() == null) {
					deleteFunction();
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				} else if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId() != null) {
					deleteFunctionArgument();
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				}else if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
					deleteParameter();
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				} else if(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() != null){
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
	}

	/**
	 * Adds the warning confirmation handlers.
	 */
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
	
	/**
	 * Adds the parameter section handlers.
	 */
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
				searchDisplay.getCQLParametersView().getParameterButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");

			}
		});		
		
		
		
		searchDisplay.getCQLParametersView().getParameterButtonBar().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addAndModifyParameters();
					//508 change to parameter section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLParametersView().getParameterNameTxtArea());
				}
			}

		});

		searchDisplay.getCQLParametersView().getParameterButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

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
		/*searchDisplay.getCQLParametersView().getParameterButtonBar().getInfoButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().buildInfoPanel((Widget) event.getSource());

			}
		});*/

		searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

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
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
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
			searchDisplay.getCQLParametersView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
				searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox()
					.setItemSelected(searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(), false);
		}

		searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
	}	
	
	/**
	 * Adds the function section handlers.
	 */
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
				searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");

			}
		});

		
		

		//Save functionality for Function section
		searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse());
				
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addAndModifyFunction();
					//508 changes for Functions Section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCqlFunctionsView().getFuncNameTxtArea());
				}

			}
		});

		//Erase functionality for Function Section
		searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				resetViewCQLCollapsiblePanel(searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse());
				
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				eraseFunction();
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor());
			}
		});

		//Insert into Ace Editor functionality for Function Section
		searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getInsertButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				buildInsertPopUp();
			}
		});

		//Info Icon functionality for Function Section
		/*searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getInfoButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().buildInfoPanel((Widget) event.getSource());

			}
		});*/

		//Delete functionality for Function Section
		searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse());
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createMultiLineAlert(MatContext.get().getMessageDelegate().getDeleteConfirmationFunctionCQLWorkspace(searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().getValue()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
			}
		});

		//Creating Arguments for Functions in Function Section
		searchDisplay.getCqlFunctionsView().getAddNewArgument().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.hideAceEditorAutoCompletePopUp();
				resetViewCQLCollapsiblePanel(searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse());
				searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				CQLFunctionArgument addNewFunctionArgument = new CQLFunctionArgument();
				AddFunctionArgumentDialogBox.showArgumentDialogBox(addNewFunctionArgument, false, 
						searchDisplay.getCqlFunctionsView(), searchDisplay.getCqlLeftNavBarPanelView(), 
						MatContext.get().getMeasureLockService().checkForEditPermission());
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				//508 changes for function section
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
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCqlFunctionsView().getFuncNameTxtArea());
			}
		});
		
		searchDisplay.getCqlFunctionsView().getFunctionCommentTextArea().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.NONE);
				String comment = searchDisplay.getCqlFunctionsView().getFunctionCommentTextArea().getText();
				if(validator.validateForCommentTextArea(comment)){
					searchDisplay.getCqlFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
							MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
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
		searchDisplay.getCqlFunctionsView().createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>(),MatContext.get().getMeasureLockService().checkForEditPermission());
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().setText("");
		}
		if ((searchDisplay.getCqlFunctionsView().getFuncNameTxtArea() != null)) {
			searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText("");
		}
		
		if ((searchDisplay.getCqlFunctionsView().getFunctionCommentTextArea()!= null)) {
			searchDisplay.getCqlFunctionsView().getFunctionCommentTextArea().setText("");
		}
		searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex(),
					false);
		}
		searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setEnabled(true);
		searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setEnabled(true);
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

			//Modify functionality for Function Arguments
			@Override
			public void onModifyClicked(CQLFunctionArgument result) {
				// TODO Auto-generated method stub
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				searchDisplay.resetMessageDisplay();
				if (result.getArgumentType().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
					getAttributesForDataType(result);
				} else {
					AddFunctionArgumentDialogBox.showArgumentDialogBox(result, true, searchDisplay.getCqlFunctionsView(), searchDisplay.getCqlLeftNavBarPanelView(),
							MatContext.get().getMeasureLockService().checkForEditPermission());
				}

			}

			//Delete functionality for Function Arguments
			@Override
			public void onDeleteClicked(CQLFunctionArgument result, int index) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(result.getId());
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(result.getArgumentName());
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_FUNCTION_ARGUMENT(result.getArgumentName()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
			}
		});
			
	}

	/**
	 * Adds the definition section handlers.
	 */
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
				searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
				resetAceEditor(searchDisplay.getCQlDefinitionsView().getViewCQLAceEditor());

			}

		});		
		
		
		
	    //Definition section Save functionality
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetViewCQLCollapsiblePanel(searchDisplay.getCQlDefinitionsView().getPanelViewCQLCollapse());
				
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addAndModifyDefintions();
					//508 changes for Definitions Section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea());
				}
			}
		});
		
		//Definition section Insert into Ace Editor functionality
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getInsertButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				buildInsertPopUp();
			}
		});

		//Definition section Erase functionality
		searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				resetViewCQLCollapsiblePanel(searchDisplay.getCQlDefinitionsView().getPanelViewCQLCollapse());
				
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				eraseDefinition();
				//508 changes for Definition Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQlDefinitionsView().getDefineAceEditor());
			}
		});
		
		//Definition section Information Functionality
		/*searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getInfoButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().buildInfoPanel((Widget) event.getSource());
				
			}	
		});*/
		
		// Definition Delete Icon Functionality
				searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						resetViewCQLCollapsiblePanel(searchDisplay.getCQlDefinitionsView().getPanelViewCQLCollapse());
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createMultiLineAlert(MatContext.get().getMessageDelegate().getDeleteConfirmationDefinitionCQLWorkspace(searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().getValue()));
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
				searchDisplay.getCQlDefinitionsView().getAddNewButtonBar().getaddNewButton().addClickHandler(new ClickHandler() {

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
						//508 changes for Definitions Section
						searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea());
					}
				});		
				
				searchDisplay.getCQlDefinitionsView().getDefineCommentTextArea().addBlurHandler(new BlurHandler() {
					
					@Override
					public void onBlur(BlurEvent event) {
						searchDisplay.resetMessageDisplay();
						searchDisplay.getCQlDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.NONE);
						String comment = searchDisplay.getCQlDefinitionsView().getDefineCommentTextArea().getText();
						if(validator.validateForCommentTextArea(comment)){
							searchDisplay.getCQlDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
									MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
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
		
		if ((searchDisplay.getCQlDefinitionsView().getDefineCommentTextArea()!= null)) {
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

		//Double Click Handler Event for Parameter Name ListBox in Parameter Section
		searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox()
				.addDoubleClickHandler(new DoubleClickHandler() {
					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
							event.stopPropagation();
						} else {
							showSearchingBusy(true);
							searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
							searchDisplay.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
							resetAceEditor(searchDisplay.getCQLParametersView().getViewCQLAceEditor());
							resetViewCQLCollapsiblePanel(
									searchDisplay.getCQLParametersView().getPanelViewCQLCollapse());
						
							searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
							searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
							if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
								showSearchingBusy(false);
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
										// load most recent used cql artifacts
										MatContext.get().getMeasureService().getUsedCQLArtifacts(
												MatContext.get().getCurrentMeasureId(),
												new AsyncCallback<GetUsedCQLArtifactsResult>() {

													@Override
													public void onFailure(Throwable caught) {
														showSearchingBusy(false);
														Window.alert(MatContext.get().getMessageDelegate()
																.getGenericErrorMessage());
													}

													@Override
													public void onSuccess(GetUsedCQLArtifactsResult result) {
														showSearchingBusy(false);
														searchDisplay.getCQLParametersView().getParameterNameTxtArea()
																.setText(searchDisplay.getCqlLeftNavBarPanelView()
																		.getParameterMap().get(selectedParamID)
																		.getParameterName());
														searchDisplay.getCQLParametersView().getParameterAceEditor()
																.setText(searchDisplay.getCqlLeftNavBarPanelView()
																		.getParameterMap().get(selectedParamID)
																		.getParameterLogic());
														searchDisplay.getCQLParametersView()
																.getParameterCommentTextArea()
																.setText(searchDisplay.getCqlLeftNavBarPanelView()
																		.getParameterMap().get(selectedParamID)
																		.getCommentString());
														boolean isReadOnly = searchDisplay.getCqlLeftNavBarPanelView()
																.getParameterMap().get(selectedParamID).isReadOnly();
														if (MatContext.get().getMeasureLockService()
																.checkForEditPermission()) {
															searchDisplay.getCQLParametersView()
																	.setWidgetReadOnly(!isReadOnly);
															// Checks if Draft
															if (MatContext.get().getCurrentMeasureVersion()
																	.toLowerCase()
																	.contains(CQLWorkSpaceConstants.CQL_DRAFT
																			.toLowerCase())) {
																searchDisplay.getCQLParametersView()
																		.getAddNewButtonBar().getaddNewButton()
																		.setEnabled(true);
															}
														}

														CQLParameter currentParameter = searchDisplay
																.getCqlLeftNavBarPanelView().getParameterMap()
																.get(selectedParamID);

														Map<String, List<CQLErrors>> expressionCQLErrorMap = result
																.getCqlErrorsPerExpression();
														if (expressionCQLErrorMap != null) {
															List<CQLErrors> errorList = expressionCQLErrorMap
																	.get(currentParameter.getParameterName());
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

														// if it is not a
														// default parameter,
														// check if the delete
														// button needs to be
														// enabled
														if (!currentParameter.isReadOnly()) {

															// enable delete
															// button if there
															// are errors are
															// the parameter is
															// not in use.
															if (!result.getCqlErrors().isEmpty()
																	|| !result.getUsedCQLParameters().contains(
																			currentParameter.getParameterName())) {
																searchDisplay.getCQLParametersView()
																		.getParameterButtonBar().getDeleteButton()
																		.setEnabled(true);

															}
														}
													}

												});

									} else {
										showSearchingBusy(false);
									}
								} else {
									showSearchingBusy(false);
								}

								searchDisplay.resetMessageDisplay();
							}
						}
						// 508 change to parameter section
						searchDisplay.getCqlLeftNavBarPanelView()
								.setFocus(searchDisplay.getCQLParametersView().getMainParamViewVerticalPanel());
					}
				});
		
		//Double Click Handler Event for Definition Name ListBox in Defintion Section
		searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
					
					showSearchingBusy(true);
					
					searchDisplay.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
					searchDisplay.getCQlDefinitionsView().getDefineAceEditor().removeAllMarkers();
					resetAceEditor(searchDisplay.getCQlDefinitionsView().getViewCQLAceEditor());
					resetViewCQLCollapsiblePanel(searchDisplay.getCQlDefinitionsView().getPanelViewCQLCollapse());
					searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText("");


					searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
					if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
						showSearchingBusy(false);
						searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
					} else {
						int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex();
						if (selectedIndex != -1) {
							final String selectedDefinitionID = searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getValue(selectedIndex);
							searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(selectedDefinitionID);
							if (searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID) != null) {
								

								/*searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText(searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().
									get(selectedDefinitionID).getReturnType());*/
								// disable definitionName and fields for
								// Supplemental data definitions
								searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setTitle("Delete");
								
								searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
								searchDisplay.getCQlDefinitionsView().setWidgetReadOnly(false);
								// load most recent used cql artifacts
								MatContext.get().getMeasureService().getUsedCQLArtifacts(
										MatContext.get().getCurrentMeasureId(),
										new AsyncCallback<GetUsedCQLArtifactsResult>() {

											@Override
											public void onFailure(Throwable caught) {
												showSearchingBusy(false);
												searchDisplay.getCQlDefinitionsView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
												Window.alert(
														MatContext.get().getMessageDelegate().getGenericErrorMessage());
											}

											@Override
											public void onSuccess(GetUsedCQLArtifactsResult result) {
												showSearchingBusy(false);
												
												boolean isReadOnly = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).isSupplDataElement();
												CQLDefinition currentDefinition = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID);
												
												if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
													searchDisplay.getCQlDefinitionsView().setWidgetReadOnly(!isReadOnly);
													//Checks if Draft
													if(MatContext.get().getCurrentMeasureVersion().toLowerCase().contains(CQLWorkSpaceConstants.CQL_DRAFT.toLowerCase())){
														searchDisplay.getCQlDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
													}
													searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setEnabled(false);
													searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setEnabled(false);
												}

												
												searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea()
												.setText(searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getDefinitionName());
												searchDisplay.getCQlDefinitionsView().getDefineCommentTextArea()
												.setText(searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getCommentString());
												searchDisplay.getCQlDefinitionsView().getDefineAceEditor()
												.setText(searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getDefinitionLogic());
												if (searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getContext().equalsIgnoreCase("patient")) {
													searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
													searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);
												} else {
													searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setValue(true);
													searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setValue(false);
												}
												
												
												
												Map<String,List<CQLErrors>> expressionCQLErrorMap = result.getCqlErrorsPerExpression();
												if(expressionCQLErrorMap != null){
													List<CQLErrors> errorList = expressionCQLErrorMap.get(currentDefinition.getDefinitionName());
													searchDisplay.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
													searchDisplay.getCQlDefinitionsView().getDefineAceEditor().removeAllMarkers();
													for (CQLErrors error : errorList) {
														int startLine = error.getStartErrorInLine();
														int startColumn = error.getStartErrorAtOffset();
														searchDisplay.getCQlDefinitionsView().getDefineAceEditor().addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.ERROR);
													}
													searchDisplay.getCQlDefinitionsView().getDefineAceEditor().setAnnotations();
												}
												
												if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
													// if the current definition is not a default definition, check if we need to enable the delete buttons
													if(!currentDefinition.isSupplDataElement()) {

														// if there are errors or the definition is not used, enable the context radio buttons and delete button
														if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLDefinitions().contains(currentDefinition.getDefinitionName())) {
															searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(true);
															searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setEnabled(true);
															searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setEnabled(true);
														}
													}	
												}


												if(result.getCqlErrors().isEmpty() && result.getExpressionReturnTypeMap() != null){
													searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText(result.getExpressionReturnTypeMap()
															.get(currentDefinition.getDefinitionName()));
													searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+ result.getExpressionReturnTypeMap()
													.get(currentDefinition.getDefinitionName()));

												} else {
													searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText("");
													searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
												}
											}

										});

							} else{
								showSearchingBusy(false);
							}
						} else{
							showSearchingBusy(false);
						}

						searchDisplay.resetMessageDisplay();
					}
				}
				//508 changes for Definitions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQlDefinitionsView().getMainDefineViewVerticalPanel());
			}
		});
		
		//Double Click Handler Event for Function Name ListBox in Function Section
		searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsLoading()) {
					event.stopPropagation();
				} else {
				showSearchingBusy(true);	
				searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
				searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
				
				resetAceEditor(searchDisplay.getCqlFunctionsView().getViewCQLAceEditor());
				resetViewCQLCollapsiblePanel(searchDisplay.getCqlFunctionsView().getPanelViewCQLCollapse());
				
				searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					showSearchingBusy(false);
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex();
					if (selectedIndex != -1) {
						final String selectedFunctionId = searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getValue(selectedIndex);
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(selectedFunctionId);
						if (searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId) != null) {
							searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
							searchDisplay.getCqlFunctionsView().setWidgetReadOnly(false);
							// load most recent used cql artifacts
							MatContext.get().getMeasureService().getUsedCQLArtifacts(
									MatContext.get().getCurrentMeasureId(),
									new AsyncCallback<GetUsedCQLArtifactsResult>() {

										@Override
										public void onFailure(Throwable caught) {
											showSearchingBusy(false);
											searchDisplay.getCqlFunctionsView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
											Window.alert(
													MatContext.get().getMessageDelegate().getGenericErrorMessage());
										}

										@Override
										public void onSuccess(GetUsedCQLArtifactsResult result) {
											showSearchingBusy(false);
											if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
												searchDisplay.getCqlFunctionsView().setWidgetReadOnly(true);
												//Checks if Draft
												if(MatContext.get().getCurrentMeasureVersion().toLowerCase().contains(CQLWorkSpaceConstants.CQL_DRAFT.toLowerCase())){
													searchDisplay.getCqlFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
												}
												
												searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setEnabled(false);
												searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setEnabled(false);
											}
											
											searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getFunctionName());
											searchDisplay.getCqlFunctionsView().getFunctionCommentTextArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getCommentString());
											searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().setText(searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getFunctionLogic());
											if (searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getContext().equalsIgnoreCase("patient")) {
												searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setValue(true);
												searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
											} else {
												searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setValue(true);
												searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setValue(false);
											}
											CQLFunctions currentFunction = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId);
											Map<String,List<CQLErrors>> expressionCQLErrorMap = result.getCqlErrorsPerExpression();
											if(expressionCQLErrorMap != null){
												List<CQLErrors> errorList = expressionCQLErrorMap.get(currentFunction.getFunctionName());
												searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
												searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
												for (CQLErrors error : errorList) {
													int startLine = error.getStartErrorInLine();
													int startColumn = error.getStartErrorAtOffset();
													searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.ERROR);
												}
												searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().setAnnotations();
											}
											
											
											if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
												// if there are errors or the function is not in use, enable the context radio buttons and delete button
												if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLFunctions().contains(currentFunction.getFunctionName())) {
													searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
													//MAT-8571 :Enable Function context radio buttons if its used and CQL has error.
													searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setEnabled(true);
													searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setEnabled(true);
												}	
											}
											
											
											
											if(result.getCqlErrors().isEmpty() && result.getExpressionReturnTypeMap() != null){
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText(result.getExpressionReturnTypeMap()
														.get(currentFunction.getFunctionName()));
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+result.getExpressionReturnTypeMap()
														.get(currentFunction.getFunctionName()));
									
											} else {
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
							
											}
										}

									});
							
						} else {
							showSearchingBusy(false);
						}
					} else {
						showSearchingBusy(false);
					}
					if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
						CQLFunctions selectedFunction = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
								.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
						if (selectedFunction.getArgumentList() != null) {
							searchDisplay.getCqlFunctionsView().getFunctionArgumentList().clear();
							searchDisplay.getCqlFunctionsView().getFunctionArgumentList().addAll(selectedFunction.getArgumentList());
						} else {
							searchDisplay.getCqlFunctionsView().getFunctionArgumentList().clear();
						}
					}
					searchDisplay.resetMessageDisplay();
				}
				searchDisplay.getCqlFunctionsView().createAddArgumentViewForFunctions(searchDisplay.getCqlFunctionsView().getFunctionArgumentList(),MatContext.get().getMeasureLockService().checkForEditPermission());
				
			}
				//508 changes for Functions Section
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCqlFunctionsView().getMainFunctionVerticalPanel());
			}
		});
		
		//Double Click Handler Event for Includes Name ListBox in the Includes Section
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().addDoubleClickHandler(new DoubleClickHandler() {
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
					int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex();
					if (selectedIndex != -1) {
						final String selectedIncludeLibraryID = searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getValue(selectedIndex);
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(selectedIncludeLibraryID);
						if (searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap().get(selectedIncludeLibraryID) != null) {
							
								MatContext.get().getCQLLibraryService().findCQLLibraryByID(
									searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap().get(selectedIncludeLibraryID).getCqlLibraryId(),
									new AsyncCallback<CQLLibraryDataSetObject>() {

										@Override
										public void onSuccess(CQLLibraryDataSetObject result) {
											if (result != null) {
												currentIncludeLibrarySetId = result.getCqlSetId();
												currentIncludeLibraryId = result.getId();
												searchDisplay.getIncludeView().buildIncludesReadOnlyView();
												
												searchDisplay.getIncludeView().getAliasNameTxtArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
														.get(selectedIncludeLibraryID).getAliasName());
												searchDisplay.getIncludeView().getViewCQLEditor().setText(result.getCqlText());
												searchDisplay.getIncludeView().getOwnerNameTextBox().setText(
														searchDisplay.getCqlLeftNavBarPanelView().getOwnerName(result));
												searchDisplay.getIncludeView().getCqlLibraryNameTextBox()
														.setText(result.getCqlName());
											//	searchDisplay.getIncludeView().addAvailableItems(new ArrayList<String>());
												
												if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
													searchDisplay.getIncludeView().setWidgetReadOnly(false);
													searchDisplay.getIncludeView().getSaveModifyButton().setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
												
													searchDisplay.getIncludeView().getDeleteButton().setEnabled(false);
													// load most recent used cql artifacts
													MatContext.get().getMeasureService().getUsedCQLArtifacts(
															MatContext.get().getCurrentMeasureId(),
															new AsyncCallback<GetUsedCQLArtifactsResult>() {

																@Override
																public void onFailure(Throwable caught) {
																	Window.alert(
																			MatContext.get().getMessageDelegate().getGenericErrorMessage());
																}

																@Override
																public void onSuccess(GetUsedCQLArtifactsResult result) {
																	CQLIncludeLibrary cqlIncludeLibrary = searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap().get(selectedIncludeLibraryID);
														
																	// if the cql file has errors or the library is not in use, enable the includes delete button
																	if(!result.getCqlErrors().isEmpty() || !result.getUsedCQLLibraries().contains(
																			cqlIncludeLibrary.getCqlLibraryName() + "-" + cqlIncludeLibrary.getVersion() + "|" + cqlIncludeLibrary.getAliasName())) {
																		searchDisplay.getIncludeView().getDeleteButton().setEnabled(true);
																	}
																}

															});
												}
												
												
											}
										}

										@Override
										public void onFailure(Throwable caught) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
													MatContext.get().getMessageDelegate().getGenericErrorMessage());
										}
									});

							searchDisplay.getIncludeView().setSelectedObject(
									searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap().get(selectedIncludeLibraryID).getCqlLibraryId());
							searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
							searchDisplay.getIncludeView().getSelectedObjectList().clear();
						}
					}
					searchDisplay.resetMessageDisplay();
				}
				}

			}
		});
		
		
		searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(Document.get().createDblClickEvent(searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(),
									0, 0, 0, 0, false, false, false, false), searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox());
				}
				
			}
		});
		
		
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
						if(editIncludedLibraryDialogBox.getSelectedList().size() > 0){
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
												Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
											}

											@Override
											public void onSuccess(SaveUpdateCQLResult result) {
												editIncludedLibraryDialogBox.getErrorMessageAlert().clearAlert();
												if (result != null) {
													currentIncludeLibraryId = dto.getId();
													if (result.isSuccess()) {
														searchDisplay.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(
																result.getCqlModel().getCqlIncludeLibrarys());
														searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
														MatContext.get().setIncludes(getIncludesList(
																result.getCqlModel().getCqlIncludeLibrarys()));
														MatContext.get().setIncludedValueSetNames(
																result.getCqlModel().getIncludedValueSetNames());
														MatContext.get().setIncludedCodeNames(
																result.getCqlModel().getIncludedCodeNames());
														// MatContext.get().getIncludedValueSetNames().addAll(result.getCqlModel().getIncludedCodeNames());
														MatContext.get().setIncludedParamNames(
																result.getCqlModel().getIncludedParamNames());
														MatContext.get().setIncludedDefNames(
																result.getCqlModel().getIncludedDefNames());
														MatContext.get().setIncludedFuncNames(
																result.getCqlModel().getIncludedFuncNames());
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
		
		//KeyDownHandler for Includes FocusPanel
		searchDisplay.getIncludeView().getFocusPanel().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				// Search when enter is pressed.
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					searchDisplay.getIncludeView().getSearchButton().click();
				}
			}
		});
		
		//Search CQL Libraries Functionality for Includes Section
		searchDisplay.getIncludeView().getSearchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAllIncludeLibraryList(searchDisplay.getIncludeView().getSearchTextBox().getText().trim());
			}
		});
				
		//Save Functionality for Includes Section
		searchDisplay.getIncludeView().getIncludesButtonBar().getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addIncludeLibraryInCQLLookUp();
					// 508 changes for Library Alias.
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
				}
			}
		});
		
		//Erase Functionality for Includes Section
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
		
		//Delete Functionality for Includes Section
		searchDisplay.getIncludeView().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {	
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_INCLUDE(searchDisplay.getIncludeView().getAliasNameTxtArea().getValue()));
				searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
				// 508 changes for Library Alias.
				searchDisplay.getIncludeView().getAliasNameTxtArea().setFocus(true);
			}

		});
		
		//Close Functionality for Includes Section
		searchDisplay.getIncludeView().getCloseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentIncludeLibrarySetId =  null;
				// Below lines are to clear search suggestion textbox and listbox
				// selection after erase.
				searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
				if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
					searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
							.setItemSelected(searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(), false);
				}
				searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
				searchDisplay.buildIncludesView();
				SaveCQLLibraryResult cqlLibrarySearchModel = new SaveCQLLibraryResult();
				cqlLibrarySearchModel.setCqlLibraryDataSetObjects(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryList());
				searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
						.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
				searchDisplay.getIncludeView().buildIncludeLibraryCellTable(
						cqlLibrarySearchModel,MatContext.get().getMeasureLockService().checkForEditPermission(), false);
				searchDisplay.getIncludeView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
				
				if(searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT){
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else{
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
				}
				// 508 changes for Library Alias.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
			}
		});
		
		
		searchDisplay.getIncludeView().setObserver(new CQLIncludeLibraryView.Observer() {
			
			//CheckBox Observer functionality for Includes Section
			@Override
			public void onCheckBoxClicked(CQLLibraryDataSetObject result) {
				MatContext.get().getCQLLibraryService().findCQLLibraryByID(result.getId(), new AsyncCallback<CQLLibraryDataSetObject>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
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
		if(searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT){
			searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
			return;
		} else{
			searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
		}
		final String aliasName = searchDisplay.getIncludeView().getAliasNameTxtArea().getText();
		
		if (!aliasName.isEmpty() && searchDisplay.getIncludeView().getSelectedObjectList().size()>0) {
			//functioanlity to add Include Library
			CQLLibraryDataSetObject cqlLibraryDataSetObject = searchDisplay.getIncludeView().getSelectedObjectList().get(0);
			
			if (validator.validateForAliasNameSpecialChar(aliasName.trim())) {
				
				CQLIncludeLibrary incLibrary = new CQLIncludeLibrary();
				incLibrary.setAliasName(aliasName);
				incLibrary.setCqlLibraryId(cqlLibraryDataSetObject.getId());
				String versionValue = cqlLibraryDataSetObject.getVersion().replace("v", "")+"."+cqlLibraryDataSetObject.getRevisionNumber();
				incLibrary.setVersion(versionValue);
				incLibrary.setCqlLibraryName(cqlLibraryDataSetObject.getCqlName());
				incLibrary.setQdmVersion(cqlLibraryDataSetObject.getQdmVersion());
				incLibrary.setSetId(cqlLibraryDataSetObject.getCqlSetId());
				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() == null) {
					showSearchingBusy(true);
					//this is just to add include library and not modify
					MatContext.get().getMeasureService().saveIncludeLibrayInCQLLookUp(MatContext.get().getCurrentMeasureId(), 
							null, incLibrary, searchDisplay.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(), new AsyncCallback<SaveUpdateCQLResult>() {

								@Override
								public void onFailure(Throwable caught) {
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getGenericErrorMessage());
									showSearchingBusy(false);
									
								}

								@Override
								public void onSuccess(SaveUpdateCQLResult result) {
									if(result != null){
										if (result.isSuccess()) {
											searchDisplay.resetMessageDisplay();
											searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
											searchDisplay.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(result.getCqlModel().getCqlIncludeLibrarys());
											MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
											searchDisplay.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
											searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
											searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
													.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getIncludeLibrarySuccessMessage(result.getIncludeLibrary().getAliasName()));
											clearAlias();
											MatContext.get().setIncludedValueSetNames(result.getCqlModel().getIncludedValueSetNames());
											MatContext.get().setIncludedCodeNames(result.getCqlModel().getIncludedCodeNames());
											//MatContext.get().getIncludedValueSetNames().addAll(result.getCqlModel().getIncludedCodeNames());
											MatContext.get().setIncludedParamNames(result.getCqlModel().getIncludedParamNames());
											MatContext.get().setIncludedDefNames(result.getCqlModel().getIncludedDefNames());
											MatContext.get().setIncludedFuncNames(result.getCqlModel().getIncludedFuncNames());
											if(searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT){
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
											} else{
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
											}
										}  else if (result.getFailureReason() == 1) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
										} else if (result.getFailureReason() == 2) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Missing includes library tag.");
											searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
										}  else if(result.getFailureReason() == 3){
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
										}
									}
									showSearchingBusy(false);
								}
							});
				}
			
			} else {
				searchDisplay.getIncludeView().getAliasNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
			}
			
			
		} else {
			searchDisplay.getIncludeView().getAliasNameGroup().setValidationState(ValidationState.ERROR);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
					MatContext.get().getMessageDelegate().getSAVE_INCLUDE_LIBRARY_VALIATION_ERROR());
		}
	}

	/**
	 * Event Handlers for Context Radio Buttons.
	 */
	private void addEventHandlersOnContextRadioButtons() {
		searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().addValueChangeHandler(new ValueChangeHandler<Boolean>() {

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

		searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().addValueChangeHandler(new ValueChangeHandler<Boolean>() {

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

		searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().addValueChangeHandler(new ValueChangeHandler<Boolean>() {

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

		searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().addValueChangeHandler(new ValueChangeHandler<Boolean>() {

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
			searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement().setClassName("panel-collapse collapse in");
			break;
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			currentSection = nextSection;
			functionEvent();
			searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement().setClassName("panel-collapse collapse in");
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			currentSection = nextSection;
			parameterEvent();
			searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement().setClassName("panel-collapse collapse in");
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			currentSection = nextSection;
			definitionEvent();
			searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement().setClassName("panel-collapse collapse in");
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
			clearFunction();
			break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			clearParameter();
			break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			clearDefinition();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Adds the Value Set observer handler.
	 */
	private void addValuSetObserverHandler() {
		
		searchDisplay.getValueSetView().setObserver(new CQLAppliedValueSetView.Observer() {
			@Override
			public void onModifyClicked(CQLQualityDataSetDTO result) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
				isModified = true;
				modifyValueSetDTO = result;
				String displayName = result.getCodeListName();
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
			}

			@Override
			public void onDeleteClicked(CQLQualityDataSetDTO result, final int index) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
				if((modifyValueSetDTO!=null) && modifyValueSetDTO.getId().equalsIgnoreCase(result.getId())){
					isModified = false;
				}
				String measureId = MatContext.get().getCurrentMeasureId();
				if ((measureId != null) && !measureId.equals("")) {
					searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(result.getId());
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_VALUESET(result.getCodeListName()));
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
					//508 Compliance for Value Sets section
					searchDisplay.getValueSetView().getOIDInput().setFocus(true);
				}
			}
			
			
		});

	}

	/**
	 * Save measure xml.
	 *
	 * @param toBeDeletedValueSetId the to Be Deleted Value Set Id
	
	 */
	private void deleteValueSet(String toBeDeletedValueSetId) {
		showSearchingBusy(true);
		MatContext.get().getMeasureService().deleteValueSet(toBeDeletedValueSetId,  MatContext.get()
				.getCurrentMeasureId(),  new AsyncCallback<SaveUpdateCQLResult>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());
				showSearchingBusy(false);
			}
			
			@Override
			public void onSuccess(final SaveUpdateCQLResult result) {
				if(result != null && result.getCqlErrors().isEmpty()){
					modifyValueSetDTO = null;
					// This code snapet is not required.Commenting it out till it is regressed and fully tested.
					/*searchDisplay.getQdmView().getCelltable().setVisibleRangeAndClearData(searchDisplay
							.getQdmView().getCelltable().getVisibleRange(), true);
					searchDisplay.getQdmView().getListDataProvider().getList().remove(indexOf);
					if(searchDisplay.getQdmView().getListDataProvider().getList().size()>0){
						searchDisplay.getQdmView().getListDataProvider().refresh();
						searchDisplay.getQdmView().getPager().setPageStart(searchDisplay.getQdmView().getCelltable().getVisibleRange().getStart(),
								searchDisplay.getQdmView().getListDataProvider().getList().size());
					} else {
						searchDisplay.getQdmView().buildAppliedValueSetCellTable(list, isModified);
					}*/
					//searchDisplay.getCqlLeftNavBarPanelView().setAppliedQdmTableList(list);
					//The below call will update the Applied QDM drop down list in insert popup.
					getAppliedValueSetList();
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
							MatContext.get().getMessageDelegate().getSUCCESSFUL_QDM_REMOVE_MSG(result.getCqlQualityDataSetDTO().getCodeListName()));
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
						caught.printStackTrace();
						System.out.println("Error retrieving data type attributes. " + caught.getMessage());

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
	 * This method Clears alias view on Erase Button click when isPageDirty
	 * is not set.
	 */
	private void clearAlias() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getIncludeView().getAliasNameTxtArea() != null)) {
			searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
		}
		if((searchDisplay.getIncludeView().getViewCQLEditor().getText() != null)){
			searchDisplay.getIncludeView().getViewCQLEditor().setText("");
		}
		//Below lines are to clear Library search text box.
		if((searchDisplay.getIncludeView().getSearchTextBox().getText() != null)){
			searchDisplay.getIncludeView().getSearchTextBox().setText("");
		}
		searchDisplay.getIncludeView().getSelectedObjectList().clear();
		searchDisplay.getIncludeView().setSelectedObject(null);
		searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
		unCheckAvailableLibraryCheckBox();
		
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
					.setItemSelected(searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(), false);
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
		/*searchDisplay.getIncludeView().buildIncludeLibraryCellTable(availableLibraries, 
				MatContext.get().getMeasureLockService().checkForEditPermission());*/
		SaveCQLLibraryResult result = new SaveCQLLibraryResult();
		result.setCqlLibraryDataSetObjects(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryList());
		searchDisplay.getIncludeView().buildIncludeLibraryCellTable(
				result,MatContext.get().getMeasureLockService().checkForEditPermission(), false);
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
		
		searchDisplay.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
		if ((searchDisplay.getCQlDefinitionsView().getDefineAceEditor().getText() != null)) {
			searchDisplay.getCQlDefinitionsView().getDefineAceEditor().setText("");
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}
	}
	
	/**
	 * Clears the ace editor for functions. Functionality for the eraser icon in function section. 
	 */
	private void eraseFunction() {
		
		searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
		if ((searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().getText() != null)) {
			searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().setText("");
			searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		}
	}

	/**
	 * This method Clears parameter view on Erase Button click when isPageDirty
	 * is not set.
	 */
	private void clearParameter() {
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
			searchDisplay.getCQLParametersView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
				searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox()
					.setItemSelected(searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(), false);
		}

		searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
	}

	/**
	 * This method Clears Definition view on Erase Button click when isPageDirty
	 * is not set.
	 */
	private void clearDefinition() {
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
			searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox()
					.setItemSelected(searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex(), false);
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
	 * This method Clears Function view on Erase Button click when isPageDirty
	 * is not set.
	 */
	private void clearFunction() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		searchDisplay.getCqlFunctionsView().getFunctionArgumentList().clear();
		searchDisplay.getCqlFunctionsView().getFunctionArgNameMap().clear();
		searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
		searchDisplay.getCqlFunctionsView().createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>(),MatContext.get().getMeasureLockService().checkForEditPermission());
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
					searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex(),
					false);
		}
		searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setValue(true);
		searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
		searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getCqlFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
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
		if (!functionName.isEmpty()) {
			if (validator.validateForSpecialChar(functionName.trim())) {
				searchDisplay.getCqlFunctionsView().getFuncNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
			} else if(validator.validateForCommentTextArea(funcComment)){
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				searchDisplay.getCqlFunctionsView().getFuncCommentGroup().setValidationState(ValidationState.ERROR);
			}else {
				CQLFunctions function = new CQLFunctions();
				function.setFunctionLogic(functionBody);
				function.setFunctionName(functionName);
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
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getGenericErrorMessage());
									showSearchingBusy(false);
								}

								@Override
								public void onSuccess(SaveUpdateCQLResult result) {
									if(result != null ){
										if (result.isSuccess()) {

											searchDisplay.getCqlLeftNavBarPanelView().setViewFunctions(result.getCqlModel().getCqlFunctions());
											MatContext.get()
													.setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
											searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(result.getFunction().getId());
											searchDisplay.getCqlLeftNavBarPanelView().clearAndAddFunctionsNamesToListBox();
											searchDisplay.getCqlLeftNavBarPanelView().updateFunctionMap();
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);

											searchDisplay.getCqlFunctionsView().getFuncNameTxtArea()
													.setText(result.getFunction().getFunctionName());
											searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor()
													.setText(result.getFunction().getFunctionLogic());
											searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
											searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
											searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
											//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
											//searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().redisplay();

											if (validateCQLArtifact(result, currentSection)) {
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getSUCESS_FUNCTION_MODIFY_WITH_ERRORS(functionName.trim()));
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
												.setText("");
												searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");

											} else if (!result.isDatatypeUsedCorrectly()) {
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWarningBadDataTypeCombination());
												if(result.isValidCQLWhileSavingExpression()){
													searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText(result.getFunction().getReturnType());
													searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+ result.getFunction().getReturnType());
												} else {
													searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");
													searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
												}
												
											}else {
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
														MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_MODIFY(functionName.trim()));
												if(result.isValidCQLWhileSavingExpression()){
													searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText(result.getFunction().getReturnType());
													searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+ result.getFunction().getReturnType());
												} else {
													searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");
													searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
												}
											}
											
											
											
											searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().setAnnotations();
											searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().redisplay();

										} else if (result.getFailureReason() == 1) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
											searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
										} else if (result.getFailureReason() == 2) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert("Unable to find Node to modify.");
											searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
										} else if (result.getFailureReason() == 3) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
											if (result.getFunction() != null) {
												searchDisplay.getCqlFunctionsView().createAddArgumentViewForFunctions(
														result.getFunction().getArgumentList(),MatContext.get().getMeasureLockService().checkForEditPermission());
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
									// if there are errors, enable the function delete button
									if(!result.getCqlErrors().isEmpty()) {
										searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
										searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setEnabled(true);
										searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setEnabled(true);
										searchDisplay.getCqlFunctionsView().getReturnTypeTextBox()
										.setText("");
										
									}
									
									else {
										// if the saved function is in use, then disable the delete button
										if (result.getUsedCQLArtifacts().getUsedCQLFunctions().contains(functionName)) {
											searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
											searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setEnabled(false);
											searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setEnabled(false);
										}
										
										else {
											searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
											searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setEnabled(true);
											searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setEnabled(true);
										}
									}
								}
							});
				

			}

		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_FUNCTION());
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
		if (!parameterName.isEmpty()) {

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
				parameter.setParameterLogic(parameterLogic);
				parameter.setParameterName(parameterName);
				parameter.setCommentString(parameterComment);
				CQLParameter toBeModifiedParamObj = null;
				
				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
					toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());		
				} 
				showSearchingBusy(true);
				MatContext.get().getMeasureService().saveAndModifyParameters(MatContext.get().getCurrentMeasureId(),
							toBeModifiedParamObj, parameter, searchDisplay.getCqlLeftNavBarPanelView().getViewParameterList(),
							isFormatable, new AsyncCallback<SaveUpdateCQLResult>() {

								@Override
								public void onFailure(Throwable caught) {
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getGenericErrorMessage());
									showSearchingBusy(false);
								}

								@Override
								public void onSuccess(SaveUpdateCQLResult result) {
									if(result != null){
										if (result.isSuccess()) {
											searchDisplay.getCqlLeftNavBarPanelView().setViewParameterList(result.getCqlModel().getCqlParameters());
											MatContext.get().setParameters(
													getParamaterList(result.getCqlModel().getCqlParameters()));
											searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(result.getParameter().getId());
											searchDisplay.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
											searchDisplay.getCqlLeftNavBarPanelView().updateParamMap();
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
											searchDisplay.getCQLParametersView().getParameterNameTxtArea()
													.setText(result.getParameter().getParameterName());
											searchDisplay.getCQLParametersView().getParameterAceEditor()
													.setText(result.getParameter().getParameterLogic());
											searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
											searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
											searchDisplay.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
											//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
											//searchDisplay.getCQLParametersView().getParameterAceEditor().redisplay();
											if (validateCQLArtifact(result, currentSection)) {
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getSUCESS_PARAMETER_MODIFY_WITH_ERRORS(parameterName.trim()));

											} else if (!result.isDatatypeUsedCorrectly()) {
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWarningBadDataTypeCombination());
											} else {
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
														MatContext.get().getMessageDelegate().getSUCESS_PARAMETER_MODIFY(parameterName.trim()));
											}
											
											
											
											
											searchDisplay.getCQLParametersView().getParameterAceEditor().setAnnotations();
											searchDisplay.getCQLParametersView().getParameterAceEditor().redisplay();

										} else if (result.getFailureReason() == 1) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
											searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
										} else if (result.getFailureReason() == 2) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert("Unable to find Node to modify.");
											searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
										} else if (result.getFailureReason() == 3) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
										} else if (result.getFailureReason() == 8) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
										}
									}
									showSearchingBusy(false);
									// if there are errors, enable the parameter delete button
									if(!result.getCqlErrors().isEmpty()) {
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
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_PARAMETER());
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
		if (!definitionName.isEmpty()) {

			if (validator.validateForSpecialChar(definitionName.trim())) {
				searchDisplay.getCQlDefinitionsView().getDefineNameGroup().setValidationState(ValidationState.ERROR);
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
			} else if (validator.validateForCommentTextArea(definitionComment)){
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
				searchDisplay.getCQlDefinitionsView().getDefineCommentGroup().setValidationState(ValidationState.ERROR);
				
			} else {
				final CQLDefinition define = new CQLDefinition();
				define.setDefinitionName(definitionName);
				define.setDefinitionLogic(definitionLogic);
				define.setCommentString(definitionComment);
				define.setContext(defineContext);
				CQLDefinition toBeModifiedObj = null;
				
				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
					toBeModifiedObj = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getMeasureService().saveAndModifyDefinitions(
							MatContext.get().getCurrentMeasureId(), toBeModifiedObj, define,
							searchDisplay.getCqlLeftNavBarPanelView().getViewDefinitions(), 
							isFormatable, new AsyncCallback<SaveUpdateCQLResult>() {

								@Override
								public void onFailure(Throwable caught) {
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getGenericErrorMessage());
									showSearchingBusy(false);
								}

								@Override
								public void onSuccess(SaveUpdateCQLResult result) {
									
									if(result != null){
										if (result.isSuccess()) {
											searchDisplay.getCqlLeftNavBarPanelView().setViewDefinitions(result.getCqlModel().getDefinitionList());
											MatContext.get().setDefinitions(
													getDefinitionList(result.getCqlModel().getDefinitionList()));
											searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(result.getDefinition().getId());
											searchDisplay.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
											searchDisplay.getCqlLeftNavBarPanelView().updateDefineMap();
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
											searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea()
													.setText(result.getDefinition().getDefinitionName());
											searchDisplay.getCQlDefinitionsView().getDefineAceEditor()
													.setText(result.getDefinition().getDefinitionLogic());
											searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
											searchDisplay.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
											searchDisplay.getCQlDefinitionsView().getDefineAceEditor().removeAllMarkers();
											//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
											//searchDisplay.getCQlDefinitionsView().getDefineAceEditor().redisplay();
											
											if (validateCQLArtifact(result, currentSection)) {
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getSUCESS_DEFINITION_MODIFY_WITH_ERRORS(definitionName.trim()));
												searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText("");
												searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
											}  else if (!result.isDatatypeUsedCorrectly()) {
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWarningBadDataTypeCombination());
												if(result.isValidCQLWhileSavingExpression()){
													searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
													searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+ result.getDefinition().getReturnType());
												} else {
													searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText("");
													searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
												}
												
											} else {
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getSUCESS_DEFINITION_MODIFY(definitionName.trim()));
												if(result.isValidCQLWhileSavingExpression()){
													searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText(result.getDefinition().getReturnType());
													searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression is "+ result.getDefinition().getReturnType());
												} else {
													searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText("");
													searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setTitle("Return Type of CQL Expression");
												}
											}
											
											searchDisplay.getCQlDefinitionsView().getDefineAceEditor().setAnnotations();
											searchDisplay.getCQlDefinitionsView().getDefineAceEditor().redisplay();

										} else {
											if (result.getFailureReason() == 1) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
												searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
											} else if (result.getFailureReason() == 2) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
														.createAlert("Unable to find Node to modify.");
												searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
											} else if (result.getFailureReason() == 3) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
												searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
											} else if (result.getFailureReason() == 8) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getERROR_VALIDATION_COMMENT_AREA());
											}
										}

									}
									showSearchingBusy(false);
									// if there are errors, enable the definition button
									if(!result.getCqlErrors().isEmpty()) {
										searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(true);
										searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setEnabled(true);
										searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setEnabled(true);
										searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText("");
									}
									
									else {
										// if the saved definition is in use, then disable the delete button
										if (result.getUsedCQLArtifacts().getUsedCQLDefinitions().contains(definitionName)) {
											searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
											searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setEnabled(false);
											searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setEnabled(false);
										}
										
										else {
											searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(true);
											searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setEnabled(true);
											searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setEnabled(true);
										}
									}
																	}
							});

			}

		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_DEFINITION());
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
		searchDisplay.getCqlGeneralInformationView().clearAllGeneralInfoOfLibrary();
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
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement().setClassName("panel-collapse collapse");
		if (searchDisplay.getCqlFunctionsView().getFunctionArgumentList().size() > 0) {
			searchDisplay.getCqlFunctionsView().getFunctionArgumentList().clear();
		}
		isModified = false;
		//isExpansionProfile = false;
	//	expProfileToAllValueSet = "";
		setId= null;
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
		
		//getAllIncludeLibraryList("");
		searchDisplay.buildView();
		addLeftNavEventHandler();
		searchDisplay.resetMessageDisplay();
		panel.add(searchDisplay.asWidget());
		getCQLData();
		// getAppliedQDMList(true);
		//loadElementLookUpNode();
		if (searchDisplay.getCqlFunctionsView().getFunctionArgumentList().size() > 0) {
			searchDisplay.getCqlFunctionsView().getFunctionArgumentList().clear();
		}
		MeasureComposerPresenter.setSubSkipEmbeddedLink("CQLWorkspaceView.containerPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * This method is called at beforeDisplay and get searchButton click on Include section
	 * and reterives CQL Versioned libraries eligible to be included into any parent cql library.
	 *
	 * @param searchText the search text
	 * @return the all include library list
	 */
	private void getAllIncludeLibraryList(final String searchText) {
		searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
		searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
		searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
		showSearchingBusy(true);
		//int startIndex = 1;
		//int pageSize = Integer.MAX_VALUE;
		
		MatContext.get().getCQLLibraryService().searchForIncludes(setId,searchText, true,
				new AsyncCallback<SaveCQLLibraryResult>() {

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);
			}

			@Override
			public void onSuccess(SaveCQLLibraryResult result) {
				showSearchingBusy(false);
				if(result != null && result.getCqlLibraryDataSetObjects().size() > 0){
					searchDisplay.getCqlLeftNavBarPanelView().setIncludeLibraryList(result.getCqlLibraryDataSetObjects());
					searchDisplay.buildIncludesView();
					searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,MatContext.get().getMeasureLockService().checkForEditPermission(), false);
					
				} else {
					searchDisplay.buildIncludesView();
					searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,MatContext.get().getMeasureLockService().checkForEditPermission(), false);
					if(!searchDisplay.getIncludeView().getSearchTextBox().getText().isEmpty())
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getNoIncludes());
				}
				
				
				if(searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT){
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else{
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
				}
				// 508 changes for Library Alias.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getIncludeView().getAliasNameTxtArea());
			}
		});
		
	}

	/**
	 * Gets the CQL data.
	 *
	 * @return the CQL data
	 */
	private void getCQLData() {
		showSearchingBusy(true);
		MatContext.get().getMeasureService().getMeasureCQLData(MatContext.get().getCurrentMeasureId(),
				new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						showSearchingBusy(false);

					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result!= null && result.getCqlModel() != null) {
							
							if(result.getSetId() != null){
								setId= result.getSetId();
							}
							if(result.getCqlModel().getLibraryName()!=null){
						//	if(result.getCqlModel().getLibrary()!=null){
								String cqlLibraryName = searchDisplay.getCqlGeneralInformationView()
										.createCQLLibraryName(MatContext.get().getCurrentMeasureName());
								searchDisplay.getCqlGeneralInformationView().getLibraryNameValue()
								.setText(cqlLibraryName);
								
								String measureVersion = MatContext.get().getCurrentMeasureVersion();
								
								measureVersion = measureVersion.replaceAll("Draft ", "").trim();
								if(measureVersion.startsWith("v")){
									measureVersion = measureVersion.substring(1);
								}
								searchDisplay.getCqlGeneralInformationView().setGeneralInfoOfLibrary(cqlLibraryName,measureVersion, result.getCqlModel().getQdmVersion(),"QDM");
								
							}
							
								/*if(result.getExpIdentifier() !=null){
									isExpansionProfile = true;
									expProfileToAllValueSet = result.getExpIdentifier();
								} else {
									expProfileToAllValueSet = "";
									isExpansionProfile = false;
								}*/

							List<CQLQualityDataSetDTO> appliedAllValueSetList = new ArrayList<CQLQualityDataSetDTO>();
							List<CQLQualityDataSetDTO> appliedValueSetListInXML = result.getCqlModel()
									.getAllValueSetList();
							
							for (CQLQualityDataSetDTO dto : appliedValueSetListInXML) {
								if (dto.isSuppDataElement())
									continue;
								appliedAllValueSetList.add(dto);
							}
							
							MatContext.get().setValuesets(appliedAllValueSetList);
							searchDisplay.getCqlLeftNavBarPanelView().setAppliedQdmList(appliedAllValueSetList);
							appliedValueSetTableList.clear();
							appliedCodeTableList.clear();
							for (CQLQualityDataSetDTO dto : result.getCqlModel().getValueSetList()) {
								if (dto.isSuppDataElement())
									continue;
								appliedValueSetTableList.add(dto);
							}
							if(result.getCqlModel().getCodeList()!=null){
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
								MatContext.get()
										.setDefinitions(getDefinitionList(result.getCqlModel().getDefinitionList()));
							} else {
								searchDisplay.getCqlLeftNavBarPanelView().getDefineBadge().setText("00");
							}
							if ((result.getCqlModel().getCqlParameters() != null)
									&& (result.getCqlModel().getCqlParameters().size() > 0)) {
								searchDisplay.getCqlLeftNavBarPanelView().setViewParameterList(result.getCqlModel().getCqlParameters());
								searchDisplay.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
								searchDisplay.getCqlLeftNavBarPanelView().updateParamMap();
								MatContext.get()
										.setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
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
								searchDisplay.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(result.getCqlModel().getCqlIncludeLibrarys());
								searchDisplay.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
								searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
								MatContext.get()
										.setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
								MatContext.get().setIncludedValueSetNames(result.getCqlModel().getIncludedValueSetNames());
								MatContext.get().setIncludedCodeNames(result.getCqlModel().getIncludedCodeNames());
								//MatContext.get().getIncludedValueSetNames().addAll(result.getCqlModel().getIncludedCodeNames());
								MatContext.get().setIncludedParamNames(result.getCqlModel().getIncludedParamNames());
								MatContext.get().setIncludedDefNames(result.getCqlModel().getIncludedDefNames());
								MatContext.get().setIncludedFuncNames(result.getCqlModel().getIncludedFuncNames());
								
							} else {
								searchDisplay.getCqlLeftNavBarPanelView().getIncludesBadge().setText("00");
								searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap().clear();
							}
							
							boolean isValidQDMVersion = searchDisplay.getCqlLeftNavBarPanelView().checkForIncludedLibrariesQDMVersion();
							if(!isValidQDMVersion){
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_QDM_VERSION_IN_INCLUDES());
							} else {
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
							}

						} else {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
						showSearchingBusy(false);

					}
				});
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
			boolean isValidQDMVersion = searchDisplay.getCqlLeftNavBarPanelView().checkForIncludedLibrariesQDMVersion();
			if(!isValidQDMVersion){
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_QDM_VERSION_IN_INCLUDES());
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
			}
		}

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
			buildAppliedQDMTable();
		}

		searchDisplay.getValueSetView().setHeading("CQL Workspace > Value Sets", "subQDMAPPliedListContainerPanel");
		Mat.focusSkipLists("MeasureComposer");
	}
	
	private void buildAppliedQDMTable() {
		searchDisplay.buildAppliedQDM();
      //  setExpansionProfilePanelValues();
		boolean isEditable = MatContext.get().getMeasureLockService().checkForEditPermission();
		
		// initialize the valuesets to be used, getUsedArtifacts() will update with the proper value
		for(CQLQualityDataSetDTO valuset : searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList()) {
			valuset.setUsed(true);
		}
		
		searchDisplay.getValueSetView().buildAppliedValueSetCellTable(searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList(), isEditable);
		searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
		searchDisplay.getValueSetView().setWidgetsReadOnly(isEditable);
		getUsedValueSets();
	}

	/*private void setExpansionProfilePanelValues() {
		//if UMLS is not logged in
		if (!MatContext.get().isUMLSLoggedIn()) {
			if(expProfileToAllValueSet !=null && !expProfileToAllValueSet.equalsIgnoreCase("")){
				searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setEnabled(false);
				searchDisplay.getValueSetView().getVSACExpansionProfileListBox().clear();
				searchDisplay.getValueSetView().getVSACExpansionProfileListBox().addItem(expProfileToAllValueSet);
				searchDisplay.getValueSetView().getDefaultExpProfileSel().setValue(true);
				searchDisplay.getValueSetView().getDefaultExpProfileSel().setEnabled(false);
				isExpansionProfile = true;
			} else {
				isExpansionProfile = false;
			}
		} else {
			if(expProfileToAllValueSet != null && !expProfileToAllValueSet.equalsIgnoreCase("")){
				isExpansionProfile = true;
				searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setEnabled(true);
				searchDisplay.getValueSetView().setExpProfileList(MatContext.get()
						.getExpProfileList());
				searchDisplay.getValueSetView().setDefaultExpansionProfileListBox();
				for(int j = 0; j < searchDisplay.getValueSetView().getVSACExpansionProfileListBox().getItemCount(); j++){
					if(searchDisplay.getValueSetView().getVSACExpansionProfileListBox().getItemText(j)
							.equalsIgnoreCase(expProfileToAllValueSet)) {
						searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setItemSelected(j, true);
						searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setSelectedIndex(j);
						searchDisplay.getValueSetView().getDefaultExpProfileSel().setValue(true);
						break;
					}
				}
			} else{
				isExpansionProfile = false;
			}
		}

	}*/

	private void getUsedValueSets() {
		searchDisplay.getValueSetView().showSearchingBusyOnQDM(true);
		MatContext.get().getMeasureService().getUsedCQLArtifacts(
                     MatContext.get().getCurrentMeasureId(),
                     new AsyncCallback<GetUsedCQLArtifactsResult>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                   Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                                   searchDisplay.getValueSetView().showSearchingBusyOnQDM(false);
                            }

                            @Override
                            public void onSuccess(GetUsedCQLArtifactsResult result) {
                            	searchDisplay.getValueSetView().showSearchingBusyOnQDM(false);
                            	// if there are errors, set the valuesets to not used.
                            	if(!result.getCqlErrors().isEmpty()) {
                            		for(CQLQualityDataSetDTO cqlDTo : searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList()){
                                      cqlDTo.setUsed(false);
                            		}
                            	}
                            	
                            	// otherwise, check if the valueset is in the used valusets list
                            	else {
                                   for(CQLQualityDataSetDTO cqlDTo : searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList()){
                                          if (result.getUsedCQLValueSets().contains(cqlDTo.getCodeListName())) {
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
	 * Call this to update used status for delete button.
	 */
	private void getUsedCodes() {
		searchDisplay.getCodesView().showSearchingBusyOnCodes(true);
		MatContext.get().getMeasureService().getUsedCQLArtifacts(
		        MatContext.get().getCurrentMeasureId(),
		        new AsyncCallback<GetUsedCQLArtifactsResult>() {

		               @Override
		               public void onFailure(Throwable caught) {
		                      Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
		                      searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
		               }

		               @Override
		               public void onSuccess(GetUsedCQLArtifactsResult result) {
		            	searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
		               	// if there are errors, set the codes to not used.
		               	if(!result.getCqlErrors().isEmpty()) {
		               		for(CQLCode cqlCode : appliedCodeTableList){
		                         cqlCode.setUsed(false);
		               		}
		               	}
		               	
		               	// otherwise, check if the valueset is in the used valusets list
		               	else {
		                      for(CQLCode cqlCode : appliedCodeTableList){
		                             //if (result.getUsedCQLcodes().contains(cqlCode.getCodeName())) {
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

		searchDisplay.getCQLParametersView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
		//Checks if Draft
		if(MatContext.get().getCurrentMeasureVersion().toLowerCase().contains(CQLWorkSpaceConstants.CQL_DRAFT.toLowerCase())){
			searchDisplay.getCQLParametersView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
		searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setTitle("Delete");
		curAceEditor = searchDisplay.getCQLParametersView().getParameterAceEditor();
		searchDisplay.getCQLParametersView().setHeading("CQL Workspace > Parameter", "mainParamViewVerticalPanel");
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
		//getAllIncludeLibraryList(searchDisplay.getIncludeView().getSearchTextBox().getText());
		searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
		searchDisplay.getIncludeView().getSearchTextBox().setText("");
		searchDisplay.getIncludeView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
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
		//	searchDisplay.getCodesView().showSearchingBusyOnCodes(true);
			searchDisplay.buildCodes();
			searchDisplay.getCodesView().buildCodesCellTable(
					appliedCodeTableList,
					MatContext.get().getMeasureLockService().checkForEditPermission());
			searchDisplay.getCodesView().resetCQLCodesSearchPanel();
			searchDisplay.getCodesView()
					.setWidgetsReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
			
			getUsedCodes();
			
			
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
		
		searchDisplay.getCQlDefinitionsView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
		//Checks if Draft
		if(MatContext.get().getCurrentMeasureVersion().toLowerCase().contains(CQLWorkSpaceConstants.CQL_DRAFT.toLowerCase())){
			searchDisplay.getCQlDefinitionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
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
		searchDisplay.getCqlFunctionsView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
		//Checks if Draft
		if(MatContext.get().getCurrentMeasureVersion().toLowerCase().contains(CQLWorkSpaceConstants.CQL_DRAFT.toLowerCase())){
			searchDisplay.getCqlFunctionsView().getAddNewButtonBar().getaddNewButton().setEnabled(true);
		}
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
			searchDisplay.buildCQLFileView();
			buildCQLView();
		}

	}

	/**
	 * Method to unset Anchor List Item selection for previous selection and set
	 * for new selections.
	 *
	 * @param menuClickedBefore
	 *            the menu clicked before
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
					searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement().setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
				searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().setSelectedIndex(-1);
				if (searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement().setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
				searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(false);
				searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().setSelectedIndex(-1);
				if (searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement().setClassName("panel-collapse collapse");
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
					searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement().setClassName("panel-collapse collapse");
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
								//validateViewCQLFile(result.getCqlString());
								// searchDisplay.getCqlAceEditor().setText(result.getCqlString());
								
								searchDisplay.getViewCQLView().getCqlAceEditor().clearAnnotations();
								searchDisplay.getViewCQLView().getCqlAceEditor().removeAllMarkers();
								searchDisplay.getViewCQLView().getCqlAceEditor().redisplay();
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
								searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
								searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationMessageAlert().clearAlert();

								if (!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getVIEW_CQL_ERROR_MESSAGE());
									for (CQLErrors error : result.getCqlErrors()) {
										String errorMessage = new String();
										errorMessage = errorMessage.concat("Error in line : " + error.getErrorInLine() + " at Offset :"
												+ error.getErrorAtOffeset());
										int line = error.getErrorInLine();
										int column = error.getErrorAtOffeset();
										searchDisplay.getViewCQLView().getCqlAceEditor().addAnnotation(line - 1, column, error.getErrorMessage(),
												AceAnnotationType.ERROR);
									}
									searchDisplay.getViewCQLView().getCqlAceEditor().setText(result.getCqlString());
									searchDisplay.getViewCQLView().getCqlAceEditor().setAnnotations();
									searchDisplay.getViewCQLView().getCqlAceEditor().redisplay();
								}  else if (!result.isDatatypeUsedCorrectly()) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVIEW_CQL_ERROR_MESSAGE_BAD_VALUESET_DATATYPE());
									searchDisplay.getViewCQLView().getCqlAceEditor().setText(result.getCqlString());
								} else {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
											.createAlert(MatContext.get().getMessageDelegate().getVIEW_CQL_NO_ERRORS_MESSAGE());
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
	 * Gets the msg panel.
	 *
	 * @param definitionList the definition list
	 * @return the msg panel
	 *//*
		 * private HTML getMsgPanel(IconType iconType, String message) { Icon
		 * checkIcon = new Icon(iconType); HTML msgHtml = new HTML(checkIcon +
		 * " <b>" + message + "</b>"); return msgHtml; }
		 */

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
			CQLIdentifierObject definition = new CQLIdentifierObject(null, definitionList.get(i).getDefinitionName());
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
			CQLIdentifierObject parameter = new CQLIdentifierObject(null, parameterList.get(i).getParameterName());
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
			CQLIdentifierObject function = new CQLIdentifierObject(null, functionList.get(i).getFunctionName());
			funcList.add(function);
		}
		return funcList;
	}
	
	/**
	 * Gets the includes list.
	 *
	 * @param includesList the includes list
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
	public ViewDisplay getSearchDisplay() {
		return searchDisplay;
	}

	/**
	 * Validate CQL artifact.
	 *
	 * @param result the result
	 * @param currentSect the current sect
	 * @return true, if successful
	 */
	private boolean validateCQLArtifact(SaveUpdateCQLResult result, String currentSect) {
		boolean isInvalid = false;
		if (!result.getCqlErrors().isEmpty()) {
			//final AceEditor editor = getAceEditorBasedOnCurrentSection(searchDisplay, currentSection);
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
	 * Gets the ace editor based on current section.
	 *
	 * @param searchDisplay the search display
	 * @param currentSection the current section
	 * @return the ace editor based on current section
	 */
	/*private static AceEditor getAceEditorBasedOnCurrentSection(ViewDisplay searchDisplay, String currentSection) {
		AceEditor editor = null;
		switch (currentSection) {
		case CQLWorkSpaceConstants.CQL_DEFINE_MENU:
			editor = searchDisplay.getCQlDefinitionsView().getDefineAceEditor();
			break;
		case CQLWorkSpaceConstants.CQL_FUNCTION_MENU:
			editor = searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor();
			break;
		case CQLWorkSpaceConstants.CQL_PARAMETER_MENU:
			editor = searchDisplay.getCQLParametersView().getParameterAceEditor();
			break;
		default:
			 editor = searchDisplay.getDefineAceEditor(); 
			break;
		}
		return editor;
	}*/

	/*
	 * private void removeMarkers(AceEditor aceEditor, int row){
	 * 
	 * }
	 */

	/**
	 * Delete definition.
	 */
	protected void deleteDefinition() {
		searchDisplay.resetMessageDisplay();
		final String definitionName = searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().getText();
		/*String definitionLogic = searchDisplay.getCQlDefinitionsView().getDefineAceEditor().getText();
		String defineContext = "";
		if (searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().getValue()) {
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}
		if (!definitionName.isEmpty()) {

			if (!validator.validateForSpecialChar(definitionName.trim())) {

				final CQLDefinition define = new CQLDefinition();
				define.setDefinitionName(definitionName);
				define.setDefinitionLogic(definitionLogic);
				define.setContext(defineContext);*/

		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
			final CQLDefinition toBeModifiedObj = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
					.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
			if(toBeModifiedObj.isSupplDataElement()) {
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
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
										MatContext.get().getMessageDelegate().getGenericErrorMessage());
								showSearchingBusy(false);
							}

							@Override
							public void onSuccess(SaveUpdateCQLResult result) {
								if(result != null){
									if (result.isSuccess()) {
										searchDisplay.getCqlLeftNavBarPanelView().setViewDefinitions(result.getCqlModel().getDefinitionList());
										MatContext.get().setDefinitions(
												getDefinitionList(result.getCqlModel().getDefinitionList()));
										searchDisplay.getCqlLeftNavBarPanelView().clearAndAddDefinitionNamesToListBox();
										searchDisplay.getCqlLeftNavBarPanelView().updateDefineMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);

										searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox().setText("");
										searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText("");
										searchDisplay.getCQlDefinitionsView().getDefineAceEditor().setText("");
										searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
										searchDisplay.getCQlDefinitionsView().getDefineAceEditor().removeAllMarkers();
										//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
										//searchDisplay.getCQlDefinitionsView().getDefineAceEditor().redisplay();
										searchDisplay.getCQlDefinitionsView().getDefineAceEditor().setAnnotations();
										searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.createAlert(MatContext.get().getMessageDelegate().getSuccessfulDefinitionRemoveMessage(toBeModifiedObj.getDefinitionName()));
										searchDisplay.getCQlDefinitionsView().getReturnTypeTextBox().setText("");
										
									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unable to find Node to modify.");
										searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
									} else if (result.getFailureReason() == 4) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
												.createAlert("Unauthorized delete operation.");
										searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
									}
								}
								showSearchingBusy(false);
								//508 changes for Definitions Section
								searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQlDefinitionsView().getMainDefineViewVerticalPanel());
							}
						});
			}
			
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select a definition to delete.");
			searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
		}
			/*} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
			}

		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select a definition to delete.");
			searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea().setText(definitionName.trim());
		}*/
	}

	/**
	 * Delete function.
	 */
	protected void deleteFunction() {

		searchDisplay.resetMessageDisplay();
		final String functionName = searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().getText();
		/*String functionBody = searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().getText();
		String funcContext = "";
		if (searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().getValue()) {
			funcContext = "Patient";
		} else {
			funcContext = "Population";
		}
		if (!functionName.isEmpty()) {
			CQLFunctions function = new CQLFunctions();
			function.setFunctionLogic(functionBody);
			function.setFunctionName(functionName);
			function.setArgumentList(searchDisplay.getCqlFunctionsView().getFunctionArgumentList());
			function.setContext(funcContext);*/
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
							if(result != null){
								if (result.isSuccess()) {
									searchDisplay.getCqlLeftNavBarPanelView().setViewFunctions(result.getCqlModel().getCqlFunctions());
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
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
									searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
									searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
									searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
									//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
									//searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().redisplay();
									searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().setAnnotations();
									searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
											.createAlert(MatContext.get().getMessageDelegate().getSuccessfulFunctionRemoveMessage(toBeModifiedFuncObj.getFunctionName()));
									searchDisplay.getCqlFunctionsView().getReturnTypeTextBox().setText("");
									if (result.getFunction() != null) {
										searchDisplay.getCqlFunctionsView().createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>(), 
												MatContext.get().getMeasureLockService().checkForEditPermission());
									}
								} else if (result.getFailureReason() == 2) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Unable to find Node to modify.");
									searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
								} else if (result.getFailureReason() == 4) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Unauthorized delete operation.");
									searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
								}
								
							}
							showSearchingBusy(false);
							//508 changes for Functions Section
							searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCqlFunctionsView().getMainFunctionVerticalPanel());
						}
					});
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select a function to delete.");
			searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
		}
		/*} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select a function to delete.");
			searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(functionName.trim());
		}*/
	}

	protected void deleteFunctionArgument(){
		
		String funcArgName = null;
		
		searchDisplay.resetMessageDisplay();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
		Iterator<CQLFunctionArgument> iterator = searchDisplay.getCqlFunctionsView().getFunctionArgumentList()
				.iterator();
		searchDisplay.getCqlFunctionsView().getFunctionArgNameMap()
		.remove(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentName().toLowerCase());
		while (iterator.hasNext()) {
			CQLFunctionArgument cqlFunArgument = iterator.next();
			if (cqlFunArgument.getId().equals(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionArgumentObjId())) {

				iterator.remove();
				searchDisplay.getCqlFunctionsView().createAddArgumentViewForFunctions(
						searchDisplay.getCqlFunctionsView().getFunctionArgumentList(),
						MatContext.get().getMeasureLockService().checkForEditPermission());
				funcArgName = cqlFunArgument.getArgumentName();
				break;
			}
		}

		//resetting name and id
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentName(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionArgumentObjId(null);
		//508 changes for Functions Section
		searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCqlFunctionsView().getFuncNameTxtArea());
		
		searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
		.createAlert(MatContext.get().getMessageDelegate().getSuccessfulFunctionArgumentRemoveMessage(funcArgName)); 
	}
	
	/**
	 * Delete parameter.
	 */
	protected void deleteParameter() {

		searchDisplay.resetMessageDisplay();
		final String parameterName = searchDisplay.getCQLParametersView().getParameterNameTxtArea().getText();
		/*String parameterBody = searchDisplay.getCQLParametersView().getParameterAceEditor().getText();

		if (!parameterName.isEmpty()) {
			CQLParameter parameter = new CQLParameter();
			parameter.setParameterLogic(parameterBody);
			parameter.setParameterName(parameterName);*/
		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
			final CQLParameter toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap()
					.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());
			if(toBeModifiedParamObj.isReadOnly()){
				searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Unauthorized delete operation.");
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
							if(result != null){
								if (result.isSuccess()) {
									searchDisplay.getCqlLeftNavBarPanelView().setViewParameterList((result.getCqlModel().getCqlParameters()));
									MatContext.get()
											.setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
									searchDisplay.getCqlLeftNavBarPanelView().clearAndAddParameterNamesToListBox();
									searchDisplay.getCqlLeftNavBarPanelView().updateParamMap();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);

									searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox().setText("");
									searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText("");
									searchDisplay.getCQLParametersView().getParameterAceEditor().setText("");
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
									searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
									searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
									searchDisplay.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
									//Commenting below code as its taking away focus and that makes our application not 508 compliant with other fields.
									//searchDisplay.getCQLParametersView().getParameterAceEditor().redisplay();
									searchDisplay.getCQLParametersView().getParameterAceEditor().setAnnotations();
									searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
											.createAlert(MatContext.get().getMessageDelegate().getSuccessfulParameterRemoveMessage(toBeModifiedParamObj.getParameterName()));
								} else if (result.getFailureReason() == 2) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Unable to find Node to modify.");
									searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
								} else if (result.getFailureReason() == 4) {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Unauthorized delete operation.");
									searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
								}
							}
							showSearchingBusy(false);
							//508 change to parameter section
							searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCQLParametersView().getMainParamViewVerticalPanel());
						}
					});
		}
		}else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select parameter to delete.");
			searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
		}
		/*} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select a parameter to delete.");
			searchDisplay.getCQLParametersView().getParameterNameTxtArea().setText(parameterName.trim());
		}*/
	}
	
	/**
	 * Delete include.
	 */
	protected void deleteInclude() {

		searchDisplay.resetMessageDisplay();
		final String aliasName = searchDisplay.getIncludeView().getAliasNameTxtArea().getText();
		/*String includeLibName = searchDisplay.getIncludeView().getCqlLibraryNameTextBox().getText();

		if (!aliasName.isEmpty()) {
			CQLIncludeLibrary cqlLibObject = new CQLIncludeLibrary();
			cqlLibObject.setCqlLibraryName(includeLibName);
			cqlLibObject.setAliasName(aliasName);*/
		if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() != null) {
			final CQLIncludeLibrary toBeModifiedIncludeObj = searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
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
							if(result != null){
								if (result.isSuccess()) {
									searchDisplay.getCqlLeftNavBarPanelView().setViewIncludeLibrarys(result.getCqlModel().getCqlIncludeLibrarys());
									MatContext.get().setIncludes(getIncludesList(result.getCqlModel().getCqlIncludeLibrarys()));
									MatContext.get().setIncludedValueSetNames(result.getCqlModel().getIncludedValueSetNames());
									MatContext.get().setIncludedCodeNames(result.getCqlModel().getIncludedCodeNames());
									//MatContext.get().getIncludedValueSetNames().addAll(result.getCqlModel().getIncludedCodeNames());
									MatContext.get().setIncludedParamNames(result.getCqlModel().getIncludedParamNames());
									MatContext.get().setIncludedDefNames(result.getCqlModel().getIncludedDefNames());
									MatContext.get().setIncludedFuncNames(result.getCqlModel().getIncludedFuncNames());
									searchDisplay.getCqlLeftNavBarPanelView().clearAndAddAliasNamesToListBox();
									searchDisplay.getCqlLeftNavBarPanelView().udpateIncludeLibraryMap();
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);

									searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
									searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
									searchDisplay.getIncludeView().getCqlLibraryNameTextBox().setText("");
									searchDisplay.getIncludeView().getOwnerNameTextBox().setText("");
									searchDisplay.getIncludeView().getViewCQLEditor().setText("");
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedIncLibraryObjId(null);
									searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
									searchDisplay.getIncludeView().getViewCQLEditor().clearAnnotations();
									searchDisplay.getIncludeView().getViewCQLEditor().removeAllMarkers();
									//searchDisplay.getIncludeView().getViewCQLEditor().redisplay();
									searchDisplay.getIncludeView().getViewCQLEditor().setAnnotations();
									searchDisplay.getIncludeView().getDeleteButton().setEnabled(false);
									
									searchDisplay.getIncludeView().getCloseButton().fireEvent(new GwtEvent<ClickHandler>() {
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
									searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Unable to find Node to modify.");
									searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
								}
							}
							showSearchingBusy(false);
						}
					});
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select an alias to delete.");
			searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
		}
		/*} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select an alias to delete.");
			searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
		}*/
	}

	
	private void deleteCode(){
		searchDisplay.getCodesView().showSearchingBusyOnCodes(true);
		MatContext.get().getMeasureService().deleteCode(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedCodesObjId(), MatContext.get().getCurrentMeasureId(), new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
				searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
				searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(null);
				if(result.isSuccess()){
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_CODE_REMOVE_MSG(result.getCqlCode().getCodeOID()));
					searchDisplay.getCodesView().resetCQLCodesSearchPanel();
					appliedCodeTableList.clear();
					appliedCodeTableList.addAll(result.getCqlCodeList());
					searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
					//searchDisplay.buildCodes();
					System.out.println("in delete codes...Used codes:"+result.getUsedCQLArtifacts().getUsedCQLcodes());
					searchDisplay.getCodesView().buildCodesCellTable(
							appliedCodeTableList,
							MatContext.get().getMeasureLockService().checkForEditPermission());
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
		MatContext.get().getMeasureService().getCQLAppliedQDMFromMeasureXml(MatContext.get().getCurrentMeasureId(), false,
				new AsyncCallback<CQLQualityDataModelWrapper>() {
			
			@Override
			public void onSuccess(final CQLQualityDataModelWrapper result) {
				appliedValueSetTableList.clear();
				if (result.getQualityDataDTO() != null) {
					for (CQLQualityDataSetDTO dto : result.getQualityDataDTO()) {
						if(dto.isSuppDataElement() || 
								dto.getOid().equals("419099009") || dto.getOid().equals("21112-8") 
										|| (dto.getType() !=null && dto.getType().equalsIgnoreCase("code")))
							continue;
						appliedValueSetTableList.add(dto);
					}
					
					
					if (appliedValueSetTableList.size() > 0) {
						Iterator<CQLQualityDataSetDTO> iterator = appliedValueSetTableList.iterator();
						while (iterator.hasNext()) {
							CQLQualityDataSetDTO dataSetDTO = iterator
									.next();
							if(dataSetDTO
									.getUuid() != null){
								if (dataSetDTO
										.getUuid()
										.equals(searchDisplay.getValueSetView()
												.getSelectedElementToRemove()
												.getUuid())) {
									if(!dataSetDTO.isUsed()){
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
	
	/**
	 * Adds the QDM Search Panel event Handlers.
	 */
	private void addValueSetSearchPanelHandlers() {

		/**
		 * this functionality is to clear the content on the ValueSet Search
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
			}
		});

		searchDisplay.getValueSetView().getUpdateFromVSACButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
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
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					String version = null;
					String expansionProfile = null;
					searchValueSetInVsac(version, expansionProfile);
					//508 compliance for Value Sets
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				}
			}
		});

		/**
		 * this handler is invoked when apply button is clicked on search Panel
		 * in Value Set tab and this is to add new value set or user Defined
		 * Value Set to the Applied Value Set list.
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
				isUserDefined = searchDisplay.getValueSetView().validateUserDefinedInput(isUserDefined);
			}
		});

		/**
		 * Adding value change handler for OID input in Search Panel in QDM
		 * elements Tab
		 */

		searchDisplay.getValueSetView().getOIDInput().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				searchDisplay.resetMessageDisplay();
				isUserDefined = searchDisplay.getValueSetView().validateOIDInput(isUserDefined);
			}
		});

		/**
		 * value change handler for Expansion Profile in Search Panel in QDM
		 * Elements Tab
		 */
		/*searchDisplay.getValueSetView().getQDMExpProfileListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				searchDisplay.resetMessageDisplay();
				if (!searchDisplay.getValueSetView()
						.getExpansionProfileValue(searchDisplay.getValueSetView().getQDMExpProfileListBox())
						.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
					searchDisplay.getValueSetView().getVersionListBox().setSelectedIndex(0);
				}
			}
		});*/

		/**
		 * value Change Handler for Version listBox in Search Panel
		 */
		searchDisplay.getValueSetView().getVersionListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				searchDisplay.resetMessageDisplay();
				/*if (!searchDisplay.getValueSetView().getVersionValue(searchDisplay.getValueSetView().getVersionListBox())
						.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
					searchDisplay.getValueSetView().getQDMExpProfileListBox().setSelectedIndex(0);
				}*/

			}
		});
		
		//addValueSetExpProfileHandlers();
		addValuSetObserverHandler();
		
	}
	
	
	private void addCodeSearchPanelHandlers() {
		
		
		searchDisplay.getCodesView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					searchCQLCodesInVsac();
					//508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeInput());
				}
			}
		});
		
		searchDisplay.getCodesView().getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					MatContext.get().clearDVIMessages();
					addNewCodes();
					searchDisplay.resetMessageDisplay();
					//508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeInput());
				}
				
			}
		});
		
		searchDisplay.getCodesView().getCancelCodeButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
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
				searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
				if(result != null){
					searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedCodesObjId(result.getId());
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_CODES(result.getCodeOID()));
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
					//508 Compliance for Codes section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeInput());
				}
				
			}
		});
	}
	
	

	private void addNewCodes() {
		searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
		searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();
		String measureId = MatContext.get().getCurrentMeasureId();
		MatCodeTransferObject transferObject = new MatCodeTransferObject();
		CQLCode refCode = new CQLCode();
		final String codeName = searchDisplay.getCodesView().getCodeDescriptorInput().getValue();
		
		refCode.setCodeOID(searchDisplay.getCodesView().getCodeInput().getValue());
		refCode.setCodeName(codeName);
		
		refCode.setCodeSystemName(searchDisplay.getCodesView().getCodeSystemInput().getValue());
		refCode.setCodeSystemVersion(searchDisplay.getCodesView().getCodeSystemVersionInput().getValue());
		refCode.setCodeIdentifier(searchDisplay.getCodesView().getCodeSearchInput().getValue());
		//refCode.setDisplayName(searchDisplay.getCodesView().getCodeDescriptorInput().getValue());
		refCode.setCodeSystemOID(searchDisplay.getCodesView().getCodeSystemOid());
		
		if(!searchDisplay.getCodesView().getSuffixTextBox().getValue().isEmpty()){
			refCode.setSuffix(searchDisplay.getCodesView().getSuffixTextBox().getValue());
			refCode.setDisplayName(codeName+" ("+searchDisplay.getCodesView().getSuffixTextBox().getValue()+")");
		} else {
			refCode.setDisplayName(codeName);
		}
		
		transferObject.setCqlCode(refCode);
		transferObject.setId(measureId);
		searchDisplay.getCodesView().showSearchingBusyOnCodes(true);
		service.saveCQLCodestoMeasure(transferObject, new AsyncCallback<SaveUpdateCQLResult>() {
			
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
				if(result.isSuccess()){
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().
							getMessageDelegate().getCodeSuccessMessage(searchDisplay.getCodesView().getCodeInput().getText()));
					searchDisplay.getCodesView().resetCQLCodesSearchPanel();
					appliedCodeTableList.clear();
					appliedCodeTableList .addAll(result.getCqlCodeList());
					searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
					System.out.println("in addNewCodes...Used codes:"+result.getUsedCQLArtifacts().getUsedCQLcodes());
					searchDisplay.getCodesView().buildCodesCellTable(
							appliedCodeTableList,
							MatContext.get().getMeasureLockService().checkForEditPermission());
					//Temporary fix to update codes for insert Icon.
					getAppliedValueSetList();
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
					if(result.getFailureReason()==result.getDuplicateCode()){
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().generateDuplicateErrorMessage(codeName));
						searchDisplay.getCodesView().buildCodesCellTable(
								appliedCodeTableList,
								MatContext.get().getMeasureLockService().checkForEditPermission());
					}
					
					else if(result.getFailureReason() ==  result.getBirthdateOrDeadError()) {
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getBirthdateOrDeadMessage());
						searchDisplay.getCodesView().buildCodesCellTable(
								appliedCodeTableList,
								MatContext.get().getMeasureLockService().checkForEditPermission());
					}
				}
				//getUsedCodes();
				
				//508 : Shift focus to code search panel.
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getCodesView().getCodeSearchInput());
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());
				searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
				
			}
		});
	}
	
	/*List<CQLCode> cloneCQLCodeList(List<CQLCode> list){
		List<CQLCode> newList = new ArrayList<CQLCode>();
		
		for(CQLCode code : list){
			newList.add((CQLCode) code.clonez());
		}
		
		return newList;
	}*/
	
	/**
	 * Update vsac value sets.
	 */
	private void updateVSACValueSets() {
		
		/*String expansionId = null;
		if(expProfileToAllValueSet.isEmpty()){
			expansionId = null;
		} else {
			expansionId = expProfileToAllValueSet;
		}*/
		searchDisplay.getValueSetView().showSearchingBusyOnQDM(true);
		service.updateCQLVSACValueSets(MatContext.get().getCurrentMeasureId(), null,
				new AsyncCallback<VsacApiResult>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				searchDisplay.getValueSetView().showSearchingBusyOnQDM(false);
			}
			
			@Override
			public void onSuccess(final VsacApiResult result) {
				searchDisplay.getValueSetView().showSearchingBusyOnQDM(false);
				if (result.isSuccess()) {
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVSAC_UPDATE_SUCCESSFULL());
					List<CQLQualityDataSetDTO> appliedListModel = new ArrayList<CQLQualityDataSetDTO>();
					for (CQLQualityDataSetDTO cqlQDMDTO : result.getUpdatedCQLQualityDataDTOLIst()) {
						if (!ConstantMessages.EXPIRED_OID.equals(cqlQDMDTO
								.getDataType()) && !ConstantMessages.BIRTHDATE_OID.equals(cqlQDMDTO
										.getDataType())
								&& (cqlQDMDTO.getType() == null))  {
							appliedListModel.add(cqlQDMDTO);
						} 
					}
					searchDisplay.getValueSetView().buildAppliedValueSetCellTable(appliedListModel, MatContext.get().getMeasureLockService().checkForEditPermission());
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(searchDisplay.getValueSetView().convertMessage(result.getFailureReason()));
				}
			}
		});
	}

	/**
	 * click Handlers for ExpansioN Profile Panel in new QDM Elements Tab.
	 */
	/*private void addValueSetExpProfileHandlers() {
		searchDisplay.getValueSetView().getApplyDefaultExpansionIdButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					// code for adding profile to List to applied QDM
					searchDisplay.resetMessageDisplay();
					if (!MatContext.get().isUMLSLoggedIn()) { // UMLS
						// Login
						// Validation
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
								.createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
						return;
					}
					int selectedindex = searchDisplay.getValueSetView().getVSACExpansionProfileListBox().getSelectedIndex();
					String selectedValue =
							searchDisplay.getValueSetView().getVSACExpansionProfileListBox().getValue(selectedindex);

					if(!selectedValue.equalsIgnoreCase("--Select--")){
						updateAllValueSetWithExpProfile(appliedValueSetTableList);
						isExpansionProfile = true;
						expProfileToAllValueSet = selectedValue;} 
					else if(!searchDisplay.getValueSetView().getDefaultExpProfileSel().getValue()){ 
						updateAllValueSetWithExpProfile(appliedValueSetTableList); 
						isExpansionProfile = false;
						expProfileToAllValueSet = "";} else {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
							.createAlert(MatContext.get()
									.getMessageDelegate().getVsacExpansionProfileSelection
									());}
				}
			}
		});

		searchDisplay.getValueSetView().getDefaultExpProfileSel()
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						if (event.getValue().toString().equals("true")) {
							if (!MatContext.get().isUMLSLoggedIn()) { // UMLS
								// Login
								// Validation
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
										.createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
								searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
								return;
							}
							searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setEnabled(true);
							searchDisplay.getValueSetView().setExpProfileList(MatContext.get().getExpProfileList());
							searchDisplay.getValueSetView().setDefaultExpansionProfileListBox();
						} else if (event.getValue().toString().equals("false")) {
							searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setEnabled(false);
							searchDisplay.getValueSetView().setDefaultExpansionProfileListBox();
						}

					}
				});

	}*/
	
	/**
	 * Update all applied QDM Elements with default Expansion Profile.
	 *
	 * @param list the list
	 */
	/*private void updateAllValueSetWithExpProfile(List<CQLQualityDataSetDTO> list) {
		List<CQLQualityDataSetDTO> modifiedCqlValueSetList = new ArrayList<CQLQualityDataSetDTO>();
		for (CQLQualityDataSetDTO cqlQualityDataSetDTO : list) {
			if (!ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(cqlQualityDataSetDTO.getOid())) {
				cqlQualityDataSetDTO.setVersion("1.0");
				if (!expProfileToAllValueSet.isEmpty()) {
					cqlQualityDataSetDTO.setExpansionIdentifier(expProfileToAllValueSet);
				}
				if (searchDisplay.getValueSetView().getDefaultExpProfileSel().getValue()) {
					modifiedCqlValueSetList.add(cqlQualityDataSetDTO);
				}
			}
		}
		//Updating all SDE
		updateAllSuppleDataElementsWithExpProfile(modifiedCqlValueSetList);
	}*/
	
	/**
	 * Update Expansion Profile in Default Four SDE's.
	 *
	 * @param modifiedCqlQDMList the modified cql QDM list
	 */
	/*protected void updateAllSuppleDataElementsWithExpProfile(final List<CQLQualityDataSetDTO> modifiedCqlQDMList) {
		String measureId =  MatContext.get().getCurrentMeasureId();
		service.getDefaultCQLSDEFromMeasureXml(measureId, new AsyncCallback<CQLQualityDataModelWrapper>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
			@Override
			public void onSuccess(CQLQualityDataModelWrapper result) {
				for (CQLQualityDataSetDTO cqlQualityDataSetDTO : result.getQualityDataDTO()) {
					if (!ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(cqlQualityDataSetDTO.getOid())) {
						cqlQualityDataSetDTO.setVersion("1.0");
						cqlQualityDataSetDTO.setExpansionIdentifier(expProfileToAllValueSet);
						modifiedCqlQDMList.add(cqlQualityDataSetDTO);
					}
				}
				updateAllInMeasureXml(modifiedCqlQDMList);
			}
		});
	}*/
	
	/**
	 * Update all in measure xml.
	 *
	 * @param modifiedCqlQDMList the modified cql QDM list
	 */
	/*private void updateAllInMeasureXml(List<CQLQualityDataSetDTO> modifiedCqlQDMList) {
		String measureId =  MatContext.get().getCurrentMeasureId();
		service.updateCQLMeasureXMLForExpansionProfile(modifiedCqlQDMList, measureId, null,
				new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				getAppliedValueSetList();
				if (!searchDisplay.getValueSetView().getDefaultExpProfileSel().getValue()) {
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get()
							.getMessageDelegate().getDefaultExpansionIdRemovedMessage());
					
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get()
							.getMessageDelegate().getVsacProfileAppliedToQdmElements());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				
			}
		});
	}
	*/
	private void searchCQLCodesInVsac() {

		final String url = searchDisplay.getCodesView().getCodeSearchInput().getValue().trim();
		searchDisplay.getCodesView().getCodeSearchInput().setText(url);
		searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
		if (!MatContext.get().isUMLSLoggedIn()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);

			return;
		}
		
		// OID validation.
		if ((url == null) || url.trim().isEmpty()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_CODE_IDENTIFIER_REQUIRED());
			
			return;
		}
		
		//Code Identifier validation to check Identifier starts with "CODE:"
		if(validator.validateForCodeIdentifier(url)){
			searchDisplay.getCodesView().getSaveButton().setEnabled(false);
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_INVALID_CODE_IDENTIFIER());

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
				searchDisplay.getCodesView().showSearchingBusyOnCodes(false);
				
			}

			@Override
			public void onSuccess(VsacApiResult result) {
				
				if (result.isSuccess()) {
					showSearchingBusy(false);
					searchDisplay.getCodesView().getCodeDescriptorInput().setValue(result.getDirectReferenceCode().getCodeDescriptor());
					searchDisplay.getCodesView().getCodeInput().setValue(result.getDirectReferenceCode().getCode());
					searchDisplay.getCodesView().getCodeSystemInput().setValue(result.getDirectReferenceCode().getCodeSystemName());
					searchDisplay.getCodesView().getCodeSystemVersionInput().setValue(result.getDirectReferenceCode().getCodeSystemVersion());
					searchDisplay.getCodesView().setCodeSystemOid(result.getDirectReferenceCode().getCodeSystemOid());
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert("Code "+result.getDirectReferenceCode().getCode()+" successfully retrieved from VSAC.");
					
			 } else if (result.getFailureReason() == 5) { 
				 searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_INVALID_CODE_IDENTIFIER());
				 
			 } else {
				 searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
			 }
				
				showSearchingBusy(false);
				//508 : Shift focus to code search panel.
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
	private void searchValueSetInVsac(String version, String expansionProfile) {

		final String oid = searchDisplay.getValueSetView().getOIDInput().getValue();
		if (!MatContext.get().isUMLSLoggedIn()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);

			return;
		}
		
		// OID validation.
		if ((oid == null) || oid.trim().isEmpty()) {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED());
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
			return;
		}
		showSearchingBusy(true);
		/*expProfileToAllValueSet = getExpProfileValue();
		if (expProfileToAllValueSet.isEmpty()) {
			isExpansionProfile = false;
			expansionProfile = null;
		} else {
			isExpansionProfile = true;
			expansionProfile = expProfileToAllValueSet;
		}*/

		vsacapiService.getMostRecentValueSetByOID(oid, null, new AsyncCallback<VsacApiResult>() {

			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
				showSearchingBusy(false);
			}

			/**
			 * On success.
			 * 
			 * @param result
			 *            the result
			 */
			@Override
			public void onSuccess(final VsacApiResult result) {
				// to get the VSAC version list corresponding the OID
				if (result.isSuccess()) {
					List<MatValueSet> matValueSets = result.getVsacResponse();
					if (matValueSets != null) {
						MatValueSet matValueSet = matValueSets.get(0);
						currentMatValueSet = matValueSet;
					}
					String valueSetName = matValueSets.get(0).getDisplayName();
					searchDisplay.getValueSetView().getOIDInput().setTitle(oid);
					searchDisplay.getValueSetView().getUserDefinedInput().setValue(valueSetName);
					searchDisplay.getValueSetView().getUserDefinedInput().setTitle(valueSetName);
				//	searchDisplay.getValueSetView().getQDMExpProfileListBox().setEnabled(true);
					searchDisplay.getValueSetView().getVersionListBox().setEnabled(true);

					searchDisplay.getValueSetView().getSaveButton().setEnabled(true);

					/*if (isExpansionProfile) {
						searchDisplay.getValueSetView().getQDMExpProfileListBox().setEnabled(false);
						searchDisplay.getValueSetView().getVersionListBox().setEnabled(false);
						searchDisplay.getValueSetView().getQDMExpProfileListBox().clear();
						searchDisplay.getValueSetView().getQDMExpProfileListBox().addItem(expProfileToAllValueSet,
								expProfileToAllValueSet);
					} else {*/
						/*searchDisplay.getValueSetView().setQDMExpProfileListBox(
								getProfileList(MatContext.get().getVsacExpProfileList()));*/
						getVSACVersionListByOID(oid);
						//searchDisplay.getValueSetView().getQDMExpProfileListBox().setEnabled(true);
						searchDisplay.getValueSetView().getVersionListBox().setEnabled(true);
					//}
						showSearchingBusy(false);
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getValuesetSuccessfulReterivalMessage(matValueSets.get(0).getDisplayName()));
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
		final String codeListName = (originalCodeListName!=null ? originalCodeListName : "") + (!suffix.isEmpty() ? " (" + suffix + ")" : "");
		
		//String expProfile = matValueSetTransferObject.getMatValueSet().getExpansionProfile();
		String version = matValueSetTransferObject.getMatValueSet().getVersion();
		/*expProfileToAllValueSet = getExpProfileValue();
		if(!expProfileToAllValueSet.equalsIgnoreCase("")){
			expProfile = expProfileToAllValueSet;
			matValueSetTransferObject.getMatValueSet().setExpansionProfile(expProfile);
		}
		if(expProfile == null){
			expProfile = "";
			matValueSetTransferObject.getMatValueSet().setExpansionProfile(expProfile);
		}*/
		if (version == null) {
			version = "";
		}
		// Check if QDM name already exists in the list.
		if (!CheckNameInValueSetList(codeListName)) {
			showSearchingBusy(true);
			MatContext.get().getMeasureService().saveCQLValuesettoMeasure(matValueSetTransferObject,
					new AsyncCallback<SaveUpdateCQLResult>() {

						@Override
						public void onFailure(Throwable caught) {
							showSearchingBusy(false);
							if (appliedValueSetTableList.size() > 0) {
								appliedValueSetTableList.removeAll(appliedValueSetTableList);
							}
						}

						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							String message = "";
							if(result != null){
								if (result.isSuccess()) {
									
									message = MatContext.get().getMessageDelegate().getValuesetSuccessMessage(codeListName);
									MatContext.get().getEventBus().fireEvent(new QDSElementCreatedEvent(codeListName));
									searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(message);
									getAppliedValueSetList();
								} else {
									if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
												MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getCodeListName()));
									}
								}
							}
							getUsedValueSets();
							showSearchingBusy(false);
						}
					});
		}

	}
	
	
	/**
	 * Adds the selected code listto measure.
	 *
	 * @param isUserDefinedValueSet the is user defined QDM
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

		if ((matValueSetTransferObject.getUserDefinedText().length() > 0)) {
			ValueSetNameInputValidator valueSetNameInputValidator = new ValueSetNameInputValidator();
			String message = valueSetNameInputValidator.validate(matValueSetTransferObject);
			if (message.isEmpty()) {
				final String userDefinedInput = matValueSetTransferObject.getCqlQualityDataSetDTO().getCodeListName(); 
				/*String expProfile = searchDisplay.getValueSetView()
						.getExpansionProfileValue(searchDisplay.getValueSetView().getQDMExpProfileListBox());*/
				String version = searchDisplay.getValueSetView()
						.getVersionValue(searchDisplay.getValueSetView().getVersionListBox());
				/*if (expProfile == null) {
					expProfile = "";
				}*/
				if (version == null) {
					version = "";
				}
				// Check if QDM name already exists in the list.
				if (!CheckNameInValueSetList(userDefinedInput)) {
					showSearchingBusy(true);
					MatContext.get().getMeasureService().saveCQLUserDefinedValuesettoMeasure(matValueSetTransferObject,
							new AsyncCallback<SaveUpdateCQLResult>() {
								@Override
								public void onFailure(final Throwable caught) {
									Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
									showSearchingBusy(false);
								}

								@SuppressWarnings("static-access")
								@Override
								public void onSuccess(final SaveUpdateCQLResult result) {
									if(result != null){
										if (result.isSuccess()) {
											if (result.getXml() != null) {
												
												String message = MatContext.get().getMessageDelegate()
														.getValuesetSuccessMessage(userDefinedInput);
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(message);
												MatContext.get().setValuesets(result.getCqlAppliedQDMList());
												searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
												getAppliedValueSetList();
											}
										} else {
											if (result.getFailureReason() == result.ALREADY_EXISTS) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
														MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getCodeListName()));
											} else if (result.getFailureReason() == result.SERVER_SIDE_VALIDATION) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Invalid input data.");
											}
										}
									}
									
									getUsedValueSets();
									showSearchingBusy(false);
								}
							});

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
	 * @param isUserDefined the is user defined
	 */
	protected final void modifyValueSetOrUserDefined(final boolean isUserDefined) {
		if (!isUserDefined) { //Normal Available Value Set Flow
			modifyValueSet();
		} else { //Pseudo Value set Flow
			modifyUserDefinedValueSet();
		}
	}

	
	/**
	 * Modify value set QDM.
	 */
	private void modifyValueSet() {
		//Normal Available QDM Flow
		MatValueSet modifyWithDTO = currentMatValueSet;
		if ((modifyValueSetDTO != null) && (modifyWithDTO != null)) {
			//String expansionId;
			String version;
			String originalName = searchDisplay.getValueSetView().getUserDefinedInput().getText();
			String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
			String displayName = (!originalName.isEmpty() ? originalName : "")  + (!suffix.isEmpty() ? " (" + suffix + ")" : "");
			//expansionId = searchDisplay.getValueSetView().getExpansionProfileValue(searchDisplay.getValueSetView().getQDMExpProfileListBox());
			version = searchDisplay.getValueSetView().getVersionValue(searchDisplay.getValueSetView().getVersionListBox());
			/*if(expansionId == null){
				expansionId = "";
			}*/
			if(version == null){
				version = "";
			}
			//expProfileToAllValueSet = getExpProfileValue();
			/*if(modifyValueSetDTO.getExpansionIdentifier() == null){
				if(expProfileToAllValueSet.equalsIgnoreCase("")){
					modifyValueSetDTO.setExpansionIdentifier("");
				} else {
					modifyValueSetDTO.setExpansionIdentifier(expProfileToAllValueSet);
				}
			}*/
			if(modifyValueSetDTO.getVersion() == null){
				modifyValueSetDTO.setVersion("");
			}
			
			modifyValueSetList(modifyValueSetDTO);
			
			if(!CheckNameInValueSetList(displayName)){
				modifyValueSetDTO.setCodeListName(displayName);
				modifyValueSetDTO.setSuffix(suffix);
				modifyValueSetDTO.setOriginalCodeListName(originalName);
				modifyWithDTO.setDisplayName(displayName);
				updateAppliedValueSetsList(modifyWithDTO, null, modifyValueSetDTO, false);
			}
			
			getUsedValueSets();
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().
					getMessageDelegate().getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE());
		}
	}
	
	
	/**
	 * Gets the exp profile value.
	 *
	 * @return the exp profile value
	 */
	/*private String getExpProfileValue() {
	int selectedindex =	searchDisplay.getValueSetView().getVSACExpansionProfileListBox().getSelectedIndex();
	String result = searchDisplay.getValueSetView().getVSACExpansionProfileListBox().getValue(selectedindex);
	if (!result.equalsIgnoreCase(MatContext.PLEASE_SELECT)){
		return result;
	}else{
		return "";
	}
	}*/

	/**
	 * Modify QDM with out value set.
	 */
	private void modifyUserDefinedValueSet() {
	//	modifyValueSetDTO.setExpansionIdentifier("");
		modifyValueSetDTO.setVersion("");
		if ((searchDisplay.getValueSetView().getUserDefinedInput().getText().trim().length() > 0)) {
			String originalName = searchDisplay.getValueSetView().getUserDefinedInput().getText();
			String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
			String usrDefDisplayName = (!originalName.isEmpty() ? originalName : "") + (!suffix.isEmpty() ? " (" + suffix + ")" : ""); 
			//String expProfile = searchDisplay.getValueSetView().getExpansionProfileValue(searchDisplay.getValueSetView().getQDMExpProfileListBox());
			String version = searchDisplay.getValueSetView().getVersionValue(searchDisplay.getValueSetView().getVersionListBox());
			/*if(expProfile == null){
				expProfile = "";
			}*/
			if(version == null){
				version = "";
			}
			
			modifyValueSetList(modifyValueSetDTO);
			if(!CheckNameInValueSetList(usrDefDisplayName)){
				CQLValueSetTransferObject object = new CQLValueSetTransferObject();
				object.setUserDefinedText(searchDisplay.getValueSetView().getUserDefinedInput().getText());
				object.scrubForMarkUp();
				ValueSetNameInputValidator valueSetNameInputValidator = new ValueSetNameInputValidator();
				/*qdmInputValidator.validate(object);*/
				String message = valueSetNameInputValidator.validate(object);
				if (message.isEmpty()) {
				
					CodeListSearchDTO modifyWithDTO = new CodeListSearchDTO();
					modifyWithDTO.setName(usrDefDisplayName);
					modifyValueSetDTO.setOriginalCodeListName(originalName);
					modifyValueSetDTO.setSuffix(suffix);
					modifyValueSetDTO.setCodeListName(usrDefDisplayName);
					updateAppliedValueSetsList(null, modifyWithDTO, modifyValueSetDTO, true);
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(message);
				}
			}
			
			getUsedValueSets();
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
					MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
		}
	}
	
	
	/**
	 * Update applied Value Set list.
	 *
	 * @param matValueSet the mat value set
	 * @param codeListSearchDTO the code list search DTO
	 * @param qualityDataSetDTO the quality data set DTO
	 * @param isUSerDefined the is U ser defined
	 */
	private void updateAppliedValueSetsList(final MatValueSet matValueSet , final CodeListSearchDTO codeListSearchDTO ,
			final CQLQualityDataSetDTO qualityDataSetDTO, final boolean isUSerDefined) {
		
		//modifyQDMList(qualityDataSetDTO);
		String version = searchDisplay.getValueSetView().getVersionValue(searchDisplay.getValueSetView().getVersionListBox());
		/*String expansionProfile = searchDisplay.getValueSetView().getExpansionProfileValue(
				searchDisplay.getValueSetView().getQDMExpProfileListBox());*/
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		matValueSetTransferObject.setMeasureId(MatContext.get().getCurrentMeasureId());
		matValueSetTransferObject.setMatValueSet(matValueSet);
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setCqlQualityDataSetDTO(qualityDataSetDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		//int expIdselectedIndex = searchDisplay.getValueSetView().getQDMExpProfileListBox().getSelectedIndex();
		int versionSelectionIndex = searchDisplay.getValueSetView().getVersionListBox().getSelectedIndex();
		if((version != null) /*|| (expansionProfile != null)*/ ){
			/*if (!expansionProfile.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !expansionProfile.equalsIgnoreCase("")) {
				matValueSetTransferObject.setExpansionProfile(true);
				matValueSetTransferObject.setVersion(false);
				currentMatValueSet.setExpansionProfile(searchDisplay
						.getValueSetView().getQDMExpProfileListBox().getValue(expIdselectedIndex));
				
			} else*/ if (!version.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !version.equalsIgnoreCase("")){
				matValueSetTransferObject.setVersion(true);
				//matValueSetTransferObject.setExpansionProfile(false);
				currentMatValueSet.setVersion(searchDisplay.getValueSetView().getVersionListBox().getValue(versionSelectionIndex));
			}
		}
		
		/*if( !isUSerDefined){
			//currentMatValueSet.setExpansionProfile(expProfileToAllValueSet);
			currentMatValueSet.setVersion("1.0");
			matValueSetTransferObject.setExpansionProfile(true);
			matValueSetTransferObject.setVersion(false);
		}*/
		matValueSetTransferObject.scrubForMarkUp();
		showSearchingBusy(true);
		MatContext.get().getMeasureService().updateCQLValuesetsToMeasure(matValueSetTransferObject,
				new AsyncCallback<SaveUpdateCQLResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
							MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);
		
			}
			@Override
			public void onSuccess(final SaveUpdateCQLResult result) {
				if(result != null){
					if(result.isSuccess()){
						isModified = false;
						searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
						modifyValueSetDTO = result.getCqlQualityDataSetDTO();
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
						getAppliedValueSetList();
					} else{
						
						if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
										MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCqlQualityDataSetDTO().getCodeListName()));
						
						} else if (result.getFailureReason() == SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION) {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Invalid Input data.");
						}
					}
				}
				showSearchingBusy(false);
			}
		});
		
	}
	
	
	
	/**
	 * Modify QDM list.
	 *
	 * @param qualityDataSetDTO the quality data set DTO
	 */
	private void modifyValueSetList(CQLQualityDataSetDTO qualityDataSetDTO) {
		for (int i = 0; i < appliedValueSetTableList.size(); i++) {
			if (qualityDataSetDTO.getCodeListName().equals(appliedValueSetTableList.get(i).getCodeListName())) {
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
			MatContext.get().getMeasureService().getCQLValusets(measureId, new AsyncCallback<CQLQualityDataModelWrapper>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
					
				}

				@Override
				public void onSuccess(CQLQualityDataModelWrapper result) {

					appliedValueSetTableList.clear();
					
					List<CQLQualityDataSetDTO> allValuesets = new ArrayList<CQLQualityDataSetDTO>();				
					
					for (CQLQualityDataSetDTO dto : result.getQualityDataDTO()) {
						if(dto.isSuppDataElement())
							continue;
						allValuesets.add(dto);
					}
					
					searchDisplay.getCqlLeftNavBarPanelView().setAppliedQdmList(allValuesets);
					MatContext.get().setValuesets(allValuesets);
					for(CQLQualityDataSetDTO valueset : allValuesets){
						//filtering out codes from valuesets list
						//if ()
						if((valueset.getOid().equals("419099009") || valueset.getOid().equals("21112-8") 
								|| (valueset.getType() !=null) && valueset.getType().equalsIgnoreCase("code"))){
							continue;
						}
							
								
						appliedValueSetTableList.add(valueset);
					}
					
					
					searchDisplay.getValueSetView().buildAppliedValueSetCellTable(appliedValueSetTableList, MatContext.get().getMeasureLockService()
							.checkForEditPermission());
					searchDisplay.getCqlLeftNavBarPanelView().updateValueSetMap(appliedValueSetTableList);
					//if UMLS is not logged in
					/*if (!MatContext.get().isUMLSLoggedIn()) {
						if(result.getVsacExpIdentifier()!=null){
							searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setEnabled(false);
							searchDisplay.getValueSetView().getVSACExpansionProfileListBox().clear();
							searchDisplay.getValueSetView().getVSACExpansionProfileListBox().addItem(result.getVsacExpIdentifier());
							searchDisplay.getValueSetView().getDefaultExpProfileSel().setValue(true);
							searchDisplay.getValueSetView().getDefaultExpProfileSel().setEnabled(false);
							isExpansionProfile = true;
							expProfileToAllValueSet = result.getVsacExpIdentifier();
						} else {
							expProfileToAllValueSet = "";
							isExpansionProfile = false;
						}
					} else {
						if(result.getVsacExpIdentifier()!=null){
							searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setEnabled(true);
							searchDisplay.getValueSetView().setExpProfileList(MatContext.get()
									.getExpProfileList());
							searchDisplay.getValueSetView().setDefaultExpansionProfileListBox();
							for(int i = 0; i < searchDisplay.getValueSetView().getVSACExpansionProfileListBox().getItemCount(); i++){
								if(searchDisplay.getValueSetView().getVSACExpansionProfileListBox().getItemText(i)
										.equalsIgnoreCase(result.getVsacExpIdentifier())) {
									searchDisplay.getValueSetView().getVSACExpansionProfileListBox().setSelectedIndex(i);
									break;
								}
							}
							searchDisplay.getValueSetView().getDefaultExpProfileSel().setEnabled(true);
							searchDisplay.getValueSetView().getDefaultExpProfileSel().setValue(true);
							
							expProfileToAllValueSet = result.getVsacExpIdentifier();
							isExpansionProfile = true;
						} else {
							searchDisplay.getValueSetView().getDefaultExpProfileSel().setEnabled(true);
							expProfileToAllValueSet = "";
							isExpansionProfile = false;
						}
					}*/
					
				}
			});
		}
		
	}
	
	
	private void getAppliedCodeList() {
		String measureId = MatContext.get().getCurrentMeasureId();
		if ((measureId != null) && !measureId.equals("")) {
			
			service.getCQLCodes(measureId, new AsyncCallback<CQLCodeWrapper>() {
				
				@Override
				public void onSuccess(CQLCodeWrapper result) {
					appliedCodeTableList.clear();
					appliedCodeTableList.addAll(result.getCqlCodeList());
					searchDisplay.getCqlLeftNavBarPanelView().setCodeBadgeValue(appliedCodeTableList);
					searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList, MatContext.get().getMeasureLockService()
							.checkForEditPermission());
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
					
				}
			});
		}
	}
	
	
	
	/**
	 * Creates the value set transfer object.
	 *
	 * @param measureID the measure ID
	 * @return the CQL value set transfer object
	 */
	private CQLValueSetTransferObject createValueSetTransferObject(String measureID) {
		String version = searchDisplay.getValueSetView().getVersionValue(searchDisplay.getValueSetView().getVersionListBox());
		/*String expansionProfile = searchDisplay.getValueSetView().getExpansionProfileValue(
				searchDisplay.getValueSetView().getQDMExpProfileListBox());
		int expIdSelectionIndex = searchDisplay.getValueSetView().getQDMExpProfileListBox().getSelectedIndex();*/
		int versionSelectionIndex = searchDisplay.getValueSetView().getVersionListBox().getSelectedIndex();
		
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		matValueSetTransferObject.setMeasureId(measureID);
		
		String originalCodeListName = searchDisplay.getValueSetView().getUserDefinedInput().getValue(); 
		String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
		String codeListName = (!originalCodeListName.isEmpty() ? originalCodeListName : "") + (!suffix.isEmpty() ? " (" + suffix +")" : "");
		
		matValueSetTransferObject.setCqlQualityDataSetDTO(new CQLQualityDataSetDTO());
		matValueSetTransferObject.getCqlQualityDataSetDTO().setOriginalCodeListName(originalCodeListName);
		matValueSetTransferObject.getCqlQualityDataSetDTO().setSuffix(suffix);
		matValueSetTransferObject.getCqlQualityDataSetDTO().setCodeListName(codeListName);
		
		CodeListSearchDTO codeListSearchDTO = new CodeListSearchDTO();
		codeListSearchDTO.setName(searchDisplay.getValueSetView().getUserDefinedInput().getText());
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		if((version != null) /*|| (expansionProfile != null)*/ ){
			/*if (!expansionProfile.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !expansionProfile.equalsIgnoreCase("")) {
				matValueSetTransferObject.setExpansionProfile(true);
				matValueSetTransferObject.setVersion(false);
				currentMatValueSet.setExpansionProfile(searchDisplay
						.getValueSetView().getQDMExpProfileListBox().getValue(expIdSelectionIndex));
				
			} else*/ if (!version.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !version.equalsIgnoreCase("")){
				matValueSetTransferObject.setVersion(true);
				//matValueSetTransferObject.setExpansionProfile(false);
				currentMatValueSet.setVersion(searchDisplay.getValueSetView().getVersionListBox().getValue(versionSelectionIndex));
			}
		}
		
		
		/*if (!expProfileToAllValueSet.isEmpty() && !isUserDefined) {
			currentMatValueSet.setExpansionProfile(expProfileToAllValueSet);
			matValueSetTransferObject.setExpansionProfile(true);
			matValueSetTransferObject.setVersion(false);
		}*/
		matValueSetTransferObject.setMatValueSet(currentMatValueSet);
		matValueSetTransferObject.setMeasureId(measureID);
		matValueSetTransferObject.setUserDefinedText(searchDisplay.getValueSetView().getUserDefinedInput().getText());
		return matValueSetTransferObject;
	}
	
	/**
	 * Check name in QDM list.
	 *
	 * @param userDefinedInput the user defined input
	 * @return true, if successful
	 */
	private boolean CheckNameInValueSetList(String userDefinedInput) {
		if (appliedValueSetTableList.size() > 0) {
			Iterator<CQLQualityDataSetDTO> iterator = appliedValueSetTableList.iterator();
			while (iterator.hasNext()) {
				CQLQualityDataSetDTO dataSetDTO = iterator.next();
				if (dataSetDTO.getCodeListName().equalsIgnoreCase(userDefinedInput)) {
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(userDefinedInput));
					return true;
					
				}
			}
		}
		return false;
	}

	
	/**
	 * Gets the VSAC version list by oid. if the default Expansion Profile is
	 * present then we are not making this VSAC call.
	 * 
	 * @param oid
	 *            the oid
	 * @return the VSAC version list by oid
	 */
	private void getVSACVersionListByOID(String oid) {
		vsacapiService.getAllVersionListByOID(oid, new AsyncCallback<VsacApiResult>() {

			@Override
			public void onSuccess(VsacApiResult result) {

				if (result.getVsacVersionResp() != null) {
					searchDisplay.getValueSetView().setQDMVersionListBoxOptions(getVersionList(result.getVsacVersionResp()));
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
				// showSearchingBusy(false);
			}
		});

	}
	
	/**
	 * On modify value set qdm.
	 *
	 * @param result the result
	 * @param isUserDefined the is user defined
	 */
	private void onModifyValueSet(CQLQualityDataSetDTO result, boolean isUserDefined){
		
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
		
		//searchDisplay.getValueSetView().getQDMExpProfileListBox().clear();
		/*if(result.getExpansionIdentifier() != null){
			if(!isUserDefined)
				searchDisplay.getValueSetView().getQDMExpProfileListBox().addItem(result.getExpansionIdentifier(),result.getExpansionIdentifier());
		}*/
		//searchDisplay.getValueSetView().getQDMExpProfileListBox().setEnabled(false);
		
		searchDisplay.getValueSetView().getVersionListBox().clear();
		searchDisplay.getValueSetView().getVersionListBox().setEnabled(false);
		
		/*expProfileToAllValueSet = getExpProfileValue();
		
		if(!expProfileToAllValueSet.isEmpty()){
			searchDisplay.getValueSetView().getQDMExpProfileListBox().clear();
			if(!isUserDefined)
				searchDisplay.getValueSetView().getQDMExpProfileListBox().addItem(expProfileToAllValueSet,
						expProfileToAllValueSet);
		}*/
		
		searchDisplay.getValueSetView().getSaveButton().setEnabled(isUserDefined);
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
										aceEditor.addAnnotation(line - 1, column,
												error.getErrorMessage(), AceAnnotationType.ERROR);
									}
									aceEditor.setText(result.getCqlString());
									//searchDisplay.getCqlAceEditor().lockEditor(0, 0);
									aceEditor.setAnnotations();
									aceEditor.gotoLine(1);
									aceEditor.redisplay();
								} else {
									
									aceEditor.setText(result.getCqlString());
									aceEditor.gotoLine(1);
								}
							}

						}
						//showSearchingBusy(false);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						//showSearchingBusy(false);
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

	
	
	/*public void showSearchingBusy(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setEnabled(!busy);
				
		
	}*/
	/**
	 * Gets the version list.
	 *
	 * @param list
	 *            the list
	 * @return the version list
	 */
	private List<? extends HasListBox> getVersionList(List<VSACVersion> list) {
		return list;
	}

	/**
	 * Gets the profile list.
	 *
	 * @param list
	 *            the list
	 * @return the profile list
	 */
	/*private List<? extends HasListBox> getProfileList(List<VSACExpansionProfile> list) {
		return list;
	}*/
	
	/**
	 * Show searching busy.
	 *
	 * @param busy the busy
	 */
	public void showSearchingBusy(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setEnabled(!busy);
		if(!currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_INCLUDES_MENU))
			searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setEnabled(!busy);
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			searchDisplay.getCqlGeneralInformationView().setWidgetReadOnly(false);
			searchDisplay.getIncludeView().getSaveButton().setEnabled(!busy);
			searchDisplay.getIncludeView().getEraseButton().setEnabled(!busy);
			
			searchDisplay.getCQlDefinitionsView().getAddNewButtonBar().setEnabled(!busy);
			searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getSaveButton().setEnabled(!busy);
			searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getEraseButton().setEnabled(!busy);
			searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(!busy);
			searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getInsertButton().setEnabled(!busy);
			searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getInfoButton().setEnabled(!busy);
			
			searchDisplay.getCQLParametersView().getAddNewButtonBar().setEnabled(!busy);
			searchDisplay.getCQLParametersView().getParameterButtonBar().getSaveButton().setEnabled(!busy);
			searchDisplay.getCQLParametersView().getParameterButtonBar().getEraseButton().setEnabled(!busy);
			searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(!busy);
			searchDisplay.getCQLParametersView().getParameterButtonBar().getInfoButton().setEnabled(!busy);
			
			searchDisplay.getCqlFunctionsView().getAddNewButtonBar().setEnabled(!busy);
			searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getSaveButton().setEnabled(!busy);
			searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getEraseButton().setEnabled(!busy);
			searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(!busy);
			searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getInsertButton().setEnabled(!busy);
			searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getInfoButton().setEnabled(!busy);
			searchDisplay.getCqlFunctionsView().getAddNewArgument().setEnabled(!busy);
			
			searchDisplay.getValueSetView().getSaveButton().setEnabled(!busy);
			searchDisplay.getValueSetView().getCancelQDMButton().setEnabled(!busy);
			searchDisplay.getValueSetView().getUpdateFromVSACButton().setEnabled(!busy);
			searchDisplay.getValueSetView().getRetrieveFromVSACButton().setEnabled(!busy);
			searchDisplay.getValueSetView().buildAppliedValueSetCellTable(appliedValueSetTableList, !busy);
			
			searchDisplay.getCodesView().getSaveButton().setEnabled(!busy);
			searchDisplay.getCodesView().getCancelCodeButton().setEnabled(!busy);
			searchDisplay.getCodesView().getRetrieveFromVSACButton().setEnabled(!busy);
			searchDisplay.getCodesView().buildCodesCellTable(appliedCodeTableList, !busy);
		}
		searchDisplay.getIncludeView().getSearchButton().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().setIsLoading(busy);
		
	}

}
