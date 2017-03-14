package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
//import org.gwtbootstrap3.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.cqlworkspace.CQLFunctionsView.Observer;
import mat.client.clause.event.QDSElementCreatedEvent;
import mat.client.codelist.HasListBox;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.JSONAttributeModeUtility;
import mat.client.shared.JSONCQLTimingExpressionUtility;
import mat.client.shared.MatContext;
import mat.client.shared.ValueSetNameInputValidator;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.CQLValueSetTransferObject;
import mat.model.CodeListSearchDTO;
import mat.model.MatValueSet;
import mat.model.VSACExpansionProfile;
import mat.model.VSACVersion;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CQLErrors;
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

	/** The is u ser defined. */
	private boolean isUserDefined = false;

	/** The exp profile to all qdm. */
	private String expProfileToAllValueSet = "";

	/** The is modfied. */
	private boolean isModified = false;

	/** The is expansion profile. */
	private boolean isExpansionProfile = false;

	/** The current mat value set. */
	private MatValueSet currentMatValueSet;
	
	/** The applied QDM list. */
	private List<CQLQualityDataSetDTO> appliedValueSetTableList = new ArrayList<CQLQualityDataSetDTO>();
	
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
		 * Gets the adds the parameter button.
		 *
		 * @return the adds the parameter button
		 */
		Button getAddParameterButton();

		
		/**
		 * Gets the parameter name txt area.
		 *
		 * @return the parameter name txt area
		 */
		TextBox getParameterNameTxtArea();
		
		/**
		 * Gets the delete define button.
		 *
		 * @return the delete define button
		 */
		Button getDeleteDefineButton();

		/**
		 * Gets the alias name txt area.
		 *
		 * @return the alias name txt area
		 */
		TextBox getAliasNameTxtArea();
		
		/**
		 * Gets the define name txt area.
		 *
		 * @return the define name txt area
		 */
		TextBox getDefineNameTxtArea();

		/**
		 * Gets the adds the define button.
		 *
		 * @return the adds the define button
		 */
		Button getAddDefineButton();

		/**
		 * Gets the main v panel.
		 *
		 * @return the main v panel
		 */
		Widget asWidget();

		/**
		 * Gets the parameter ace editor.
		 *
		 * @return the parameter ace editor
		 */
		AceEditor getParameterAceEditor();

		/**
		 * Gets the define ace editor.
		 *
		 * @return the define ace editor
		 */
		AceEditor getDefineAceEditor();

		
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
		 * Gets the cql ace editor.
		 *
		 * @return the cql ace editor
		 */
		AceEditor getCqlAceEditor();

		/**
		 * Builds the cql file view.
		 */
		void buildCQLFileView();

		/**
		 * Gets the func name txt area.
		 *
		 * @return the func name txt area
		 */
		TextBox getFuncNameTxtArea();

		/**
		 * Gets the save function button.
		 *
		 * @return the save function button
		 */
		Button getSaveFunctionButton();

		/**
		 * Gets the function body ace editor.
		 *
		 * @return the function body ace editor
		 */
		AceEditor getFunctionBodyAceEditor();

		/**
		 * Gets the erase define button.
		 *
		 * @return the erase define button
		 */
		Button getEraseDefineButton();

		/**
		 * Gets the delete parameter button.
		 *
		 * @return the delete parameter button
		 */
		Button getDeleteParameterButton();

		/**
		 * Gets the erase parameter button.
		 *
		 * @return the erase parameter button
		 */
		Button getEraseParameterButton();

		/**
		 * Gets the adds the new argument.
		 *
		 * @return the adds the new argument
		 */
		Button getAddNewArgument();

		/**
		 * Gets the context pat toggle switch.
		 *
		 * @return the context pat toggle switch
		 */
		InlineRadio getContextDefinePATRadioBtn();

		/**
		 * Gets the context pop toggle switch.
		 *
		 * @return the context pop toggle switch
		 */
		InlineRadio getContextDefinePOPRadioBtn();

		/**
		 * Gets the parameter button bar.
		 *
		 * @return the parameter button bar
		 */
		CQLButtonToolBar getParameterButtonBar();

		/**
		 * Gets the define button bar.
		 *
		 * @return the define button bar
		 */
		CQLButtonToolBar getDefineButtonBar();

		/**
		 * Gets the function button bar.
		 *
		 * @return the function button bar
		 */
		CQLButtonToolBar getFunctionButtonBar();

		/**
		 * Gets the function argument list.
		 *
		 * @return the function argument list
		 */
		List<CQLFunctionArgument> getFunctionArgumentList();

		/**
		 * Sets the function argument list.
		 *
		 * @param functionArgumentList
		 *            the new function argument list
		 */
		void setFunctionArgumentList(List<CQLFunctionArgument> functionArgumentList);

		/**
		 * Gets the erase function button.
		 *
		 * @return the erase function button
		 */
		Button getEraseFunctionButton();

		/**
		 * Creates the add argument view for functions.
		 *
		 * @param argumentList
		 *            the argument list
		 */
		void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList,boolean isEditable);


		/**
		 * Gets the context func pat radio btn.
		 *
		 * @return the context func pat radio btn
		 */
		InlineRadio getContextFuncPATRadioBtn();

		/**
		 * Gets the context func pop radio btn.
		 *
		 * @return the context func pop radio btn
		 */
		InlineRadio getContextFuncPOPRadioBtn();

		/**
		 * Gets the function arg name map.
		 *
		 * @return the function arg name map
		 */
		Map<String, CQLFunctionArgument> getFunctionArgNameMap();

		/**
		 * Sets the function arg name map.
		 *
		 * @param functionArgNameMap
		 *            the function arg name map
		 */
		void setFunctionArgNameMap(HashMap<String, CQLFunctionArgument> functionArgNameMap);


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
		void buildInfoPanel(Widget source);

		/**
		 * Gets the define info button.
		 *
		 * @return the define info button
		 */
		Button getDefineInfoButton();

		/**
		 * Gets the param info button.
		 *
		 * @return the param info button
		 */
		Button getParamInfoButton();

		/**
		 * Gets the func info button.
		 *
		 * @return the func info button
		 */
		Button getFuncInfoButton();

		/**
		 * Gets the define timing exp button.
		 *
		 * @return the define timing exp button
		 */
		Button getDefineTimingExpButton();

		/**
		 * Hide ace editor auto complete pop up.
		 */
		void hideAceEditorAutoCompletePopUp();

		/**
		 * Gets the func timing exp button.
		 *
		 * @return the func timing exp button
		 */
		Button getFuncTimingExpButton();


		/**
		 * Builds the applied QDM.
		 */
		void buildAppliedQDM();

		/**
		 * Gets the qdm view.
		 *
		 * @return the qdm view
		 */
		CQLAppliedValueSetView getQdmView();


		/**
		 * Gets the include view.
		 *
		 * @return the include view
		 */
		CQLIncludeLibraryView getIncludeView();

		/**
		 * Gets the main flow panel.
		 *
		 * @return the main flow panel
		 */
		FlowPanel getMainFlowPanel();

		/**
		 * Gets the view CQL editor.
		 *
		 * @return the view CQL editor
		 */
		AceEditor getViewCQLEditor();

		/**
		 * Gets the owner name text box.
		 *
		 * @return the owner name text box
		 */
		TextBox getOwnerNameTextBox();

		
		/**
		 * Gets the cql functions view.
		 *
		 * @return the cql functions view
		 */
		CQLFunctionsView getCqlFunctionsView();

		CQLLeftNavBarPanelView getCqlLeftNavBarPanelView();

		CQLParametersView getCQLParametersView();

		CQlDefinitionsView getCQlDefinitionsView();

		void resetMessageDisplay();

		CQLGeneralInformationView getCqlGeneralInformationView();

		void resetAll();

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
		addObserverHandler();
		JSONCQLTimingExpressionUtility.getAllCQLTimingExpressionsList();
		JSONAttributeModeUtility.getAllAttrModeList();
		JSONAttributeModeUtility.getAllModeDetailsList();
		MatContext.get().getAllAttributesList();
	}

	/**
	 * Build Insert Pop up.
	 */
	private void buildInsertPopUp() {
		searchDisplay.resetMessageDisplay();
		InsertIntoAceEditorDialogBox.showListOfItemAvailableForInsertDialogBox(searchDisplay, currentSection);
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
	}

	/**
	 * Builds the timing expression pop up.
	 */
	/*
	 * private void buildTimingExpressionPopUp() {
	 * searchDisplay.resetMessageDisplay();
	 * InsertTimingExpressionIntoAceEditor.showTimingExpressionDialogBox(
	 * searchDisplay, currentSection); searchDisplay.setIsPageDirty(true); }
	 */

	/**
	 * Adds the event handlers.
	 */
	private void addEventHandlers() {

		searchDisplay.getDefineButtonBar().getInsertButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				buildInsertPopUp();
			}
		});

		searchDisplay.getFunctionButtonBar().getInsertButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				buildInsertPopUp();
			}
		});
		searchDisplay.getAddDefineButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addAndModifyDefintions();
				}
			}
		});

		searchDisplay.getAddParameterButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addAndModifyParameters();
				}
			}

		});

		searchDisplay.getSaveFunctionButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addAndModifyFunction();
				}

			}
		});

		searchDisplay.getEraseDefineButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					clearDefinition();
				}
			}
		});

		searchDisplay.getEraseFunctionButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					clearFunction();
				}
			}
		});

		searchDisplay.getEraseParameterButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					clearParameter();
				}
			}
		});
		
		/*searchDisplay.getIncludeView().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.setIsDoubleClick(false);
				searchDisplay.setIsNavBarClick(false);
				if (searchDisplay.getIsPageDirty()) {
					searchDisplay.showUnsavedChangesWarning();
				} else {
					clearIncludeLibrary();
				}
			}
		});
*/
		
		searchDisplay.getIncludeView().getCloseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				// Below lines are to clear search suggestion textbox and listbox
				// selection after erase.
				searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestIncludeTextBox().setText("");
				if (searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex() >= 0) {
					searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox()
							.setItemSelected(searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getSelectedIndex(), false);
				}
				
				searchDisplay.buildIncludesView();
				SaveCQLLibraryResult cqlLibrarySearchModel = new SaveCQLLibraryResult();
				cqlLibrarySearchModel.setCqlLibraryDataSetObjects(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryList());
				searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
						.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
				searchDisplay.getIncludeView().buildIncludeLibraryCellTable(
						cqlLibrarySearchModel,MatContext.get().getMeasureLockService().checkForEditPermission());
				searchDisplay.getIncludeView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
				
				if(searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT){
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else{
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
				}
			}
		});


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

		searchDisplay.getAddNewArgument().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.hideAceEditorAutoCompletePopUp();
				CQLFunctionArgument addNewFunctionArgument = new CQLFunctionArgument();
				AddFunctionArgumentDialogBox.showArgumentDialogBox(addNewFunctionArgument, false, searchDisplay,MatContext.get().getMeasureLockService().checkForEditPermission());
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
			}
		});

		searchDisplay.getDefineInfoButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.buildInfoPanel((Widget) event.getSource());

			}
		});

		searchDisplay.getParamInfoButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.buildInfoPanel((Widget) event.getSource());

			}
		});

		searchDisplay.getFuncInfoButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.buildInfoPanel((Widget) event.getSource());

			}
		});
		ClickHandler cHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getDefineAceEditor().detach();
				searchDisplay.getParameterAceEditor().detach();
				searchDisplay.getFunctionBodyAceEditor().detach();
			}
		};
		searchDisplay.getMainPanel().addDomHandler(cHandler, ClickEvent.getType());

		searchDisplay.getParameterButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// load most recent used cql artifacts
				MatContext.get().getMeasureService().getUsedCQLArtifacts(MatContext.get().getCurrentMeasureId(),
						new AsyncCallback<GetUsedCQLArtifactsResult>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							}

							@Override
							public void onSuccess(GetUsedCQLArtifactsResult result) {
								String selectedParamName = searchDisplay.getParameterNameTxtArea().getText();
								if (!result.getUsedCQLParameters().contains(selectedParamName)) {
									searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show(
											MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_PARAMETER());
								}
							}

						});
			}

		});

		searchDisplay.getDefineButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// load most recent used cql artifacts
				MatContext.get().getMeasureService().getUsedCQLArtifacts(MatContext.get().getCurrentMeasureId(),
						new AsyncCallback<GetUsedCQLArtifactsResult>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							}

							@Override
							public void onSuccess(GetUsedCQLArtifactsResult result) {
								String selectedDefName = searchDisplay.getDefineNameTxtArea().getText();
								if (!result.getUsedCQLDefinitions().contains(selectedDefName)) {
									searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show(
											MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_DEFINITION());
								}
							}

						});
			}
		});

		searchDisplay.getFunctionButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// load most recent used cql artifacts
				MatContext.get().getMeasureService().getUsedCQLArtifacts(MatContext.get().getCurrentMeasureId(),
						new AsyncCallback<GetUsedCQLArtifactsResult>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							}

							@Override
							public void onSuccess(GetUsedCQLArtifactsResult result) {
								String selectedFuncName = searchDisplay.getFuncNameTxtArea().getText();
								if (!result.getUsedCQLFunctions().contains(selectedFuncName)) {
									searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show(
											MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_FUNCTION());
								}
							}

						});
			}

		});
		
		searchDisplay.getIncludeView().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// load most recent used cql artifacts
				MatContext.get().getMeasureService().getUsedCQLArtifacts(MatContext.get().getCurrentMeasureId(),
						new AsyncCallback<GetUsedCQLArtifactsResult>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							}

							@Override
							public void onSuccess(GetUsedCQLArtifactsResult result) {
								String selectedAliasName = searchDisplay.getIncludeView().getAliasNameTxtArea().getText();
								String selectedLibName = searchDisplay.getIncludeView().getCqlLibraryNameTextBox().getText();
								if (!result.getUsedCQLLibraries().contains(selectedLibName + "." + selectedAliasName)) {
									searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show(
											MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_INCLUDE());
								}
							}

						});
			}

		});

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
				} else if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
					deleteFunction();
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				} else if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
					deleteParameter();
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				} else if(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() != null){
					deleteInclude();
					searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().hide();
				}
			}
		});

		addEventHandlerOnAceEditors();
		addEventHandlersOnContextRadioButtons();
		addQDMELmentSearchPanelHandlers();
		addIncludeCQLLibraryHandlers();
		addListBoxEventHandler();
	}
	
	public void addListBoxEventHandler() {

		searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				searchDisplay.getCQLParametersView().getParameterAceEditor().clearAnnotations();
				searchDisplay.getCQLParametersView().getParameterAceEditor().removeAllMarkers();
				searchDisplay.getCQLParametersView().getParameterAceEditor().redisplay();
				System.out.println("In addParameterEventHandler on DoubleClick isPageDirty = " + searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()
						+ " selectedIndex = " + searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex());
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex();
					if (selectedIndex != -1) {
						final String selectedParamID = searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getValue(selectedIndex);
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(selectedParamID);
						if (searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID) != null) {
							searchDisplay.getCQLParametersView().getParameterNameTxtArea()
									.setText(searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).getParameterName());
							searchDisplay.getCQLParametersView().getParameterAceEditor().setText(searchDisplay.getCqlLeftNavBarPanelView()
									.getParameterMap().get(selectedParamID).getParameterLogic());
							System.out.println("In Parameter DoubleClickHandler, doing setText()");
							// disable parameterName and Logic fields for
							// Default Parameter
							boolean isReadOnly = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).isReadOnly();
							searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setTitle("Delete");

							if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
								searchDisplay.getCQLParametersView().setWidgetReadOnly(!isReadOnly);
								searchDisplay.getCQLParametersView().getParameterButtonBar().getEraseButton().setEnabled(true);
							}

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
											if (result.getUsedCQLParameters().contains(
													searchDisplay.getCqlLeftNavBarPanelView().getParameterMap().get(selectedParamID).getParameterName())) {
												searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(false);
											}
										}

									});
						}
					}

					searchDisplay.resetMessageDisplay();

				}

			}
		});
		
		
		searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				searchDisplay.getCQlDefinitionsView().getDefineAceEditor().clearAnnotations();
				searchDisplay.getCQlDefinitionsView().getDefineAceEditor().removeAllMarkers();
				searchDisplay.getCQlDefinitionsView().getDefineAceEditor().redisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex();
					if (selectedIndex != -1) {
						final String selectedDefinitionID = searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getValue(selectedIndex);
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(selectedDefinitionID);
						if (searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID) != null) {
							searchDisplay.getCQlDefinitionsView().getDefineNameTxtArea()
									.setText(searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getDefinitionName());
							searchDisplay.getCQlDefinitionsView().getDefineAceEditor()
									.setText(searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getDefinitionLogic());
							if (searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getContext().equalsIgnoreCase("patient")) {
								searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setValue(true);
								searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setValue(false);
							} else {
								searchDisplay.getCQlDefinitionsView().getContextDefinePOPRadioBtn().setValue(true);
								searchDisplay.getCQlDefinitionsView().getContextDefinePATRadioBtn().setValue(false);
							}
							// disable definitionName and fields for
							// Supplemental data definitions
							boolean isReadOnly = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).isSupplDataElement();
							searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setTitle("Delete");

							if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
								searchDisplay.getCQlDefinitionsView().setWidgetReadOnly(!isReadOnly);
								searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getEraseButton().setEnabled(true);
							}

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
											if (result.getUsedCQLDefinitions().contains(
													searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap().get(selectedDefinitionID).getDefinitionName())) {
												searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(false);

											}
										}

									});
						}
					}

					searchDisplay.resetMessageDisplay();
				}
			}
		});
		
		
		searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().clearAnnotations();
				searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().removeAllMarkers();
				searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().redisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(true);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);
				if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
					searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
				} else {
					int selectedIndex = searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex();
					if (selectedIndex != -1) {
						final String selectedFunctionId = searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getValue(selectedIndex);
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(selectedFunctionId);
						if (searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId) != null) {
							searchDisplay.getCqlFunctionsView().getFuncNameTxtArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getFunctionName());
							searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor().setText(searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getFunctionLogic());
							if (searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getContext().equalsIgnoreCase("patient")) {
								searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setValue(true);
								searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setValue(false);
							} else {
								searchDisplay.getCqlFunctionsView().getContextFuncPOPRadioBtn().setValue(true);
								searchDisplay.getCqlFunctionsView().getContextFuncPATRadioBtn().setValue(false);
							}

							if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
								searchDisplay.getCqlFunctionsView().setWidgetReadOnly(true);
								searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(true);
							}

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
											if (result.getUsedCQLFunctions().contains(
													searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap().get(selectedFunctionId).getFunctionName())) {
												searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(false);

											}
										}

									});
						}
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
				}
				searchDisplay.getCqlFunctionsView().createAddArgumentViewForFunctions(searchDisplay.getCqlFunctionsView().getFunctionArgumentList(),MatContext.get().getMeasureLockService().checkForEditPermission());
				searchDisplay.resetMessageDisplay();
			}
		});
		
		
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {

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
												searchDisplay.getIncludeView().buildIncludesReadOnlyView();
												
												searchDisplay.getIncludeView().getAliasNameTxtArea().setText(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
														.get(selectedIncludeLibraryID).getAliasName());
												searchDisplay.getIncludeView().getViewCQLEditor().setText(result.getCqlText());
												searchDisplay.getIncludeView().getOwnerNameTextBox().setText(
														searchDisplay.getCqlLeftNavBarPanelView().getOwnerName(result));
												searchDisplay.getIncludeView().getCqlLibraryNameTextBox()
														.setText(result.getCqlName());
												
												if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
													searchDisplay.getIncludeView().setWidgetReadOnly(false);
													searchDisplay.getIncludeView().getDeleteButton().setEnabled(true);
												}
												
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
																if (result.getUsedCQLLibraries().contains(
																		cqlIncludeLibrary.getCqlLibraryName() + "." + cqlIncludeLibrary.getAliasName())) {
																	searchDisplay.getIncludeView().getDeleteButton().setEnabled(false);
																}
															}

														});
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
		});
	}
	
	
	
	/**
	 * Adds the include CQL library handlers.
	 */
	private void addIncludeCQLLibraryHandlers() {
		
		searchDisplay.getIncludeView().getIncludesButtonBar().getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					addIncludeLibraryInCQLLookUp();
				}
			}
		});
		
		searchDisplay.getIncludeView().getSearchButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getAllIncludeLibraryList(searchDisplay.getIncludeView().getSearchTextBox().getText().trim());
			}
		});
		searchDisplay.getIncludeView().getFocusPanel().addKeyDownHandler(new KeyDownHandler(){
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				//Search when enter is pressed.
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
					searchDisplay.getIncludeView().getSearchButton().click();
				}
			}
		});
		
		searchDisplay.getIncludeView().getEraseButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
				searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(false);				
				clearAlias();	
				
				if(searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT){
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else{
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
				}
			}
		});
		
		
		searchDisplay.getIncludeView().setObserver(new CQLIncludeLibraryView.Observer() {
			
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
		
		/*searchDisplay.getIncludeView().getEraseButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.setIsDoubleClick(false);
				searchDisplay.setIsNavBarClick(false);
				if (searchDisplay.getIsPageDirty()) {
					searchDisplay.showUnsavedChangesWarning();
				} else {
					clearIncludeLibrary();
				}
			}
		});
*/
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
											if(searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT){
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
											} else{
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
											}
										}  else if (result.getFailureReason() == 1) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getAliasNameTxtArea().setText(aliasName.trim());
										} else if (result.getFailureReason() == 2) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Missing includes library tag.");
											searchDisplay.getAliasNameTxtArea().setText(aliasName.trim());
										}  else if(result.getFailureReason() == 3){
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getAliasNameTxtArea().setText(aliasName.trim());
										}
									}
									showSearchingBusy(false);
								}
							});
				}
			
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_INCLUDE_ALIAS_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getAliasNameTxtArea().setText(aliasName.trim());
			}
			
			
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
					MatContext.get().getMessageDelegate().getSAVE_INCLUDE_LIBRARY_VALIATION_ERROR());
		}
	}

	/**
	 * Event Handlers for Context Radio Buttons.
	 */
	private void addEventHandlersOnContextRadioButtons() {
		searchDisplay.getContextDefinePATRadioBtn().addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				if (searchDisplay.getContextDefinePATRadioBtn().getValue()) {
					searchDisplay.getContextDefinePOPRadioBtn().setValue(false);
				} else {
					searchDisplay.getContextDefinePOPRadioBtn().setValue(true);
				}

			}
		});

		searchDisplay.getContextDefinePOPRadioBtn().addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				if (searchDisplay.getContextDefinePOPRadioBtn().getValue()) {
					searchDisplay.getContextDefinePATRadioBtn().setValue(false);
				} else {
					searchDisplay.getContextDefinePATRadioBtn().setValue(true);
				}

			}
		});

		searchDisplay.getContextFuncPATRadioBtn().addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				if (searchDisplay.getContextFuncPATRadioBtn().getValue()) {
					searchDisplay.getContextFuncPOPRadioBtn().setValue(false);
				} else {
					searchDisplay.getContextFuncPOPRadioBtn().setValue(true);
				}
			}
		});

		searchDisplay.getContextFuncPOPRadioBtn().addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				if (searchDisplay.getContextFuncPOPRadioBtn().getValue()) {
					searchDisplay.getContextFuncPATRadioBtn().setValue(false);
				} else {
					searchDisplay.getContextFuncPATRadioBtn().setValue(true);
				}
			}
		});
	}

	/**
	 * Event Handlers for Ace Editors.
	 */
	private void addEventHandlerOnAceEditors() {
		searchDisplay.getDefineAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getDefineAceEditor().isReadOnly()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		searchDisplay.getParameterAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getParameterAceEditor().isReadOnly()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				}
			}
		});

		searchDisplay.getFunctionBodyAceEditor().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getFunctionBodyAceEditor().isReadOnly()) {
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
	 * Adds the observer handler.
	 */
	private void addObserverHandler() {
		searchDisplay.getCqlFunctionsView().setObserver( new Observer() {
			
			@Override
			public void onModifyClicked(CQLFunctionArgument result) {
				// TODO Auto-generated method stub
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				searchDisplay.resetMessageDisplay();
				if (result.getArgumentType().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
					getAttributesForDataType(result);
				} else {
					AddFunctionArgumentDialogBox.showArgumentDialogBox(result, true, searchDisplay,MatContext.get().getMeasureLockService().checkForEditPermission());
				}
				
			}
			
			@Override
			public void onDeleteClicked(CQLFunctionArgument result, int index) {
				searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
				Iterator<CQLFunctionArgument> iterator = searchDisplay.getFunctionArgumentList().iterator();
				searchDisplay.getFunctionArgNameMap().remove(result.getArgumentName().toLowerCase());
				while (iterator.hasNext()) {
					CQLFunctionArgument cqlFunArgument = iterator.next();
					if (cqlFunArgument.getId().equals(result.getId())) {

						iterator.remove();
						searchDisplay.createAddArgumentViewForFunctions(searchDisplay.getFunctionArgumentList(),MatContext.get().getMeasureLockService().checkForEditPermission());
						break;
					}
				}
				
			}
		});
		
		searchDisplay.getQdmView().setObserver(new CQLAppliedValueSetView.Observer() {
			@Override
			public void onModifyClicked(CQLQualityDataSetDTO result) {
				searchDisplay.resetMessageDisplay();
				resetCQLValuesetearchPanel();
				isModified = true;
				modifyValueSetDTO = result;
				String displayName = result.getCodeListName();
				HTML searchHeaderText = new HTML("<strong>Modify value set ( "+displayName +")</strong>");
				searchDisplay.getQdmView().getSearchHeader().clear();
				searchDisplay.getQdmView().getSearchHeader().add(searchHeaderText);
				searchDisplay.getQdmView().getMainPanel().getElement().focus();
				if(result.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)){
					isUserDefined = true;
				} else {
					isUserDefined = false;
				}
				
				onModifyValueSet(result, isUserDefined);

			}

			@Override
			public void onDeleteClicked(CQLQualityDataSetDTO result, final int index) {
				searchDisplay.resetMessageDisplay();
				resetCQLValuesetearchPanel();
				if((modifyValueSetDTO!=null) && modifyValueSetDTO.getId().equalsIgnoreCase(result.getId())){
					isModified = false;
				}
				String measureId = MatContext.get().getCurrentMeasureId();
				if ((measureId != null) && !measureId.equals("")) {
					showSearchingBusy(true);
					MatContext.get().getMeasureService().getCQLAppliedQDMFromMeasureXml(measureId, false,
							new AsyncCallback<CQLQualityDataModelWrapper>() {
						
						@Override
						public void onSuccess(final CQLQualityDataModelWrapper result) {
							appliedValueSetTableList.clear();
							if (result.getQualityDataDTO() != null) {
								for (CQLQualityDataSetDTO dto : result.getQualityDataDTO()) {
									if(dto.isSuppDataElement())
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
													.equals(searchDisplay.getQdmView()
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
							showSearchingBusy(false);
						}
						
						@Override
						public void onFailure(Throwable caught) {
							showSearchingBusy(false);
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
					});
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
							MatContext.get().getMessageDelegate().getSUCCESSFUL_QDM_REMOVE_MSG());
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);
				}
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
						AddFunctionArgumentDialogBox.showArgumentDialogBox(functionArg, true, searchDisplay,MatContext.get().getMeasureLockService().checkForEditPermission());

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
				result,MatContext.get().getMeasureLockService().checkForEditPermission());
	}

	/**
	 * This method Clears parameter view on Erase Button click when isPageDirty
	 * is not set.
	 */
	private void clearParameter() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		searchDisplay.getParameterAceEditor().clearAnnotations();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getParameterAceEditor().getText() != null)) {
			searchDisplay.getParameterAceEditor().setText("");
		}
		if ((searchDisplay.getParameterNameTxtArea() != null)) {
			searchDisplay.getParameterNameTxtArea().setText("");
		}

		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			searchDisplay.getCQLParametersView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
		}
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestParamTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox()
					.setItemSelected(searchDisplay.getCqlLeftNavBarPanelView().getParameterNameListBox().getSelectedIndex(), false);
		}

		searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
	}

	/**
	 * This method Clears Definition view on Erase Button click when isPageDirty
	 * is not set.
	 */
	private void clearDefinition() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		searchDisplay.getDefineAceEditor().clearAnnotations();
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getDefineAceEditor().getText() != null)) {
			searchDisplay.getDefineAceEditor().setText("");
		}
		if ((searchDisplay.getDefineNameTxtArea() != null)) {
			searchDisplay.getDefineNameTxtArea().setText("");
		}
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestDefineTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox()
					.setItemSelected(searchDisplay.getCqlLeftNavBarPanelView().getDefineNameListBox().getSelectedIndex(), false);
		}

		// Functionality to reset the disabled features for supplemental data
		// definitions when erased.
		searchDisplay.getDefineNameTxtArea().setEnabled(true);
		searchDisplay.getDefineAceEditor().setReadOnly(false);
		searchDisplay.getContextDefinePATRadioBtn().setEnabled(true);
		searchDisplay.getContextDefinePOPRadioBtn().setEnabled(true);
		searchDisplay.getDefineButtonBar().getSaveButton().setEnabled(true);
		searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getDefineButtonBar().getInsertButton().setEnabled(true);
		searchDisplay.getDefineButtonBar().getTimingExpButton().setEnabled(true);
		searchDisplay.getContextDefinePATRadioBtn().setValue(true);
		searchDisplay.getContextDefinePOPRadioBtn().setValue(false);
	}

	/**
	 * This method Clears Function view on Erase Button click when isPageDirty
	 * is not set.
	 */
	private void clearFunction() {
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		searchDisplay.getFunctionArgumentList().clear();
		searchDisplay.getFunctionArgNameMap().clear();
		searchDisplay.getFunctionBodyAceEditor().clearAnnotations();
		searchDisplay.createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>(),MatContext.get().getMeasureLockService().checkForEditPermission());
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		if ((searchDisplay.getFunctionBodyAceEditor().getText() != null)) {
			searchDisplay.getFunctionBodyAceEditor().setText("");
		}
		if ((searchDisplay.getFuncNameTxtArea() != null)) {
			searchDisplay.getFuncNameTxtArea().setText("");
		}
		// Below lines are to clear search suggestion textbox and listbox
		// selection after erase.
		searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText("");
		if (searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex() >= 0) {
			searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().setItemSelected(
					searchDisplay.getCqlLeftNavBarPanelView().getFuncNameListBox().getSelectedIndex(),
					false);
		}
		searchDisplay.getContextFuncPATRadioBtn().setValue(true);
		searchDisplay.getContextFuncPOPRadioBtn().setValue(false);
		searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
	}
	

	/**
	 * Adds and modify function.
	 */
	protected void addAndModifyFunction() {
		searchDisplay.resetMessageDisplay();
		final String functionName = searchDisplay.getFuncNameTxtArea().getText();
		String functionBody = searchDisplay.getFunctionBodyAceEditor().getText();
		String funcContext = "";
		if (searchDisplay.getContextFuncPATRadioBtn().getValue()) {
			funcContext = "Patient";
		} else {
			funcContext = "Population";
		}
		if (!functionName.isEmpty()) {
			if (!validator.validateForSpecialChar(functionName.trim())) {

				CQLFunctions function = new CQLFunctions();
				function.setFunctionLogic(functionBody);
				function.setFunctionName(functionName);
				function.setArgumentList(searchDisplay.getFunctionArgumentList());
				function.setContext(funcContext);
				CQLFunctions toBeModifiedParamObj = null;
				
				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
					toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
				}
				showSearchingBusy(true);
					MatContext.get().getMeasureService().saveAndModifyFunctions(MatContext.get().getCurrentMeasureId(),
							toBeModifiedParamObj, function, searchDisplay.getCqlLeftNavBarPanelView().getViewFunctions(),
							new AsyncCallback<SaveUpdateCQLResult>() {

								@Override
								public void onFailure(Throwable caught) {
									searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
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

											searchDisplay.getFuncNameTxtArea()
													.setText(result.getFunction().getFunctionName());
											searchDisplay.getFunctionBodyAceEditor()
													.setText(result.getFunction().getFunctionLogic());
											searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
											searchDisplay.getFunctionBodyAceEditor().clearAnnotations();
											searchDisplay.getFunctionBodyAceEditor().removeAllMarkers();
											searchDisplay.getFunctionBodyAceEditor().redisplay();
											searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(true);

											if (validateCQLArtifact(result, currentSection)) {
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getSUCESS_FUNCTION_MODIFY_WITH_ERRORS());

											} else {
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
														MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_MODIFY());
											}
											searchDisplay.getFunctionBodyAceEditor().setAnnotations();
											searchDisplay.getFunctionBodyAceEditor().redisplay();

										} else if (result.getFailureReason() == 1) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
											searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
										} else if (result.getFailureReason() == 2) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert("Unable to find Node to modify.");
											searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
										} else if (result.getFailureReason() == 3) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
											if (result.getFunction() != null) {
												searchDisplay.createAddArgumentViewForFunctions(
														result.getFunction().getArgumentList(),MatContext.get().getMeasureLockService().checkForEditPermission());
											}
										}

									}
									showSearchingBusy(false);
																	}
							});
				
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
			}

		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_FUNCTION());
			searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
		}

	}

	/**
	 * Adds the and modify parameters.
	 */
	private void addAndModifyParameters() {
		searchDisplay.resetMessageDisplay();
		final String parameterName = searchDisplay.getParameterNameTxtArea().getText();
		String parameterLogic = searchDisplay.getParameterAceEditor().getText();
		if (!parameterName.isEmpty()) {

			if (!validator.validateForSpecialChar(parameterName.trim())) {

				CQLParameter parameter = new CQLParameter();
				parameter.setParameterLogic(parameterLogic);
				parameter.setParameterName(parameterName);
				CQLParameter toBeModifiedParamObj = null;
				
				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
					toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());		
				} 
				showSearchingBusy(true);
				MatContext.get().getMeasureService().saveAndModifyParameters(MatContext.get().getCurrentMeasureId(),
							toBeModifiedParamObj, parameter, searchDisplay.getCqlLeftNavBarPanelView().getViewParameterList(),
							new AsyncCallback<SaveUpdateCQLResult>() {

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
											searchDisplay.getParameterNameTxtArea()
													.setText(result.getParameter().getParameterName());
											searchDisplay.getParameterAceEditor()
													.setText(result.getParameter().getParameterLogic());
											searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
											searchDisplay.getParameterAceEditor().clearAnnotations();
											searchDisplay.getParameterAceEditor().removeAllMarkers();
											searchDisplay.getParameterAceEditor().redisplay();
											searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(true);
											if (validateCQLArtifact(result, currentSection)) {
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getSUCESS_PARAMETER_MODIFY_WITH_ERRORS());

											} else {
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(
														MatContext.get().getMessageDelegate().getSUCESS_PARAMETER_MODIFY());

											}
											searchDisplay.getParameterAceEditor().setAnnotations();
											searchDisplay.getParameterAceEditor().redisplay();

										} else if (result.getFailureReason() == 1) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
											searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
										} else if (result.getFailureReason() == 2) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert("Unable to find Node to modify.");
											searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
										} else if (result.getFailureReason() == 3) {
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
										}
									}
									showSearchingBusy(false);
								}
							});
				

			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
			}

		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_PARAMETER());
			searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
		}

	}
	
	/**
	 * This method is called to Add/Modify Definitions into Measure Xml.
	 * 
	 */
	private void addAndModifyDefintions() {
		searchDisplay.resetMessageDisplay();
		final String definitionName = searchDisplay.getDefineNameTxtArea().getText();
		String definitionLogic = searchDisplay.getDefineAceEditor().getText();
		String defineContext = "";
		if (searchDisplay.getContextDefinePATRadioBtn().getValue()) {
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}
		if (!definitionName.isEmpty()) {

			if (!validator.validateForSpecialChar(definitionName.trim())) {

				final CQLDefinition define = new CQLDefinition();
				define.setDefinitionName(definitionName);
				define.setDefinitionLogic(definitionLogic);
				define.setContext(defineContext);
				CQLDefinition toBeModifiedObj = null;
				
				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
					toBeModifiedObj = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
				}
				showSearchingBusy(true);
				MatContext.get().getMeasureService().saveAndModifyDefinitions(
							MatContext.get().getCurrentMeasureId(), toBeModifiedObj, define,
							searchDisplay.getCqlLeftNavBarPanelView().getViewDefinitions(), new AsyncCallback<SaveUpdateCQLResult>() {

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
											searchDisplay.getDefineNameTxtArea()
													.setText(result.getDefinition().getDefinitionName());
											searchDisplay.getDefineAceEditor()
													.setText(result.getDefinition().getDefinitionLogic());
											searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
											searchDisplay.getDefineAceEditor().clearAnnotations();
											searchDisplay.getDefineAceEditor().removeAllMarkers();
											searchDisplay.getDefineAceEditor().redisplay();
											searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(true);
											if (validateCQLArtifact(result, currentSection)) {
												searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getSUCESS_DEFINITION_MODIFY_WITH_ERRORS());
											} else {
												searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getSUCESS_DEFINITION_MODIFY());
											}
											searchDisplay.getDefineAceEditor().setAnnotations();
											searchDisplay.getDefineAceEditor().redisplay();

										} else {
											if (result.getFailureReason() == 1) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
												searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
											} else if (result.getFailureReason() == 2) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
														.createAlert("Unable to find Node to modify.");
												searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
											} else if (result.getFailureReason() == 3) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get()
														.getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
												searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
											}
										}

									}
									showSearchingBusy(false);
																	}
							});
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
			}

		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_DEFINITION());
			searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
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
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
		searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
		searchDisplay.getFunctionArgNameMap().clear();
		searchDisplay.getIncludeView().getSearchTextBox().setText("");
		searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
		searchDisplay.resetMessageDisplay();
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getParamCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getDefineCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionCollapse().getElement().setClassName("panel-collapse collapse");
		if (searchDisplay.getFunctionArgumentList().size() > 0) {
			searchDisplay.getFunctionArgumentList().clear();
		}
		isModified = false;
		modifyValueSetDTO = null;
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		searchDisplay.getCqlLeftNavBarPanelView().getMessagePanel().clear();
		searchDisplay.resetAll();
		panel.clear();
		searchDisplay.getMainPanel().clear();
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
		MatContext.get().getAllCqlKeywordsAndQDMDatatypesForCQLWorkSpace();
		MatContext.get().getAllUnits();
		// getAppliedQDMList(true);
		//loadElementLookUpNode();
		if (searchDisplay.getFunctionArgumentList().size() > 0) {
			searchDisplay.getFunctionArgumentList().clear();
		}
		
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
		MatContext.get().getCQLLibraryService().searchForIncludes(searchText, new AsyncCallback<SaveCQLLibraryResult>() {

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
					searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,MatContext.get().getMeasureLockService().checkForEditPermission());
					
				} else {
					searchDisplay.buildIncludesView();
					searchDisplay.getIncludeView().buildIncludeLibraryCellTable(result,MatContext.get().getMeasureLockService().checkForEditPermission());
					if(!searchDisplay.getIncludeView().getSearchTextBox().getText().isEmpty())
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getNoIncludes());
				}
				
				
				if(searchDisplay.getCqlLeftNavBarPanelView().getIncludesNameListbox().getItemCount() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT){
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLimitWarningMessage());
				} else{
					searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clearAlert();
				}
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
								
								searchDisplay.getCqlGeneralInformationView().getLibraryVersionValue().setText(measureVersion);
								
								searchDisplay.getCqlGeneralInformationView().getUsingModelValue().setText("QDM");
								
								searchDisplay.getCqlGeneralInformationView().getModelVersionValue().setText("5.0.2");
							}
							

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
							for (CQLQualityDataSetDTO dto : result.getCqlModel().getValueSetList()) {
								if (dto.isSuppDataElement())
									continue;
								appliedValueSetTableList.add(dto);
							}
							searchDisplay.getCqlLeftNavBarPanelView().setAppliedQdmTableList(appliedValueSetTableList);

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
							} else {
								searchDisplay.getCqlLeftNavBarPanelView().getIncludesBadge().setText("00");
								searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap().clear();
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

				MatContext.get().getMeasureService().getCQLValusets(MatContext.get().getCurrentMeasureId(),
						new AsyncCallback<CQLQualityDataModelWrapper>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());

					}

					@Override
					public void onSuccess(CQLQualityDataModelWrapper result) {
						String ExpIdentifier = null;
						appliedValueSetTableList.clear();
						List<CQLQualityDataSetDTO> allValuesets = new ArrayList<CQLQualityDataSetDTO>();
						if(result != null){
							for (CQLQualityDataSetDTO dto : result.getQualityDataDTO()) {
								if (dto.isSuppDataElement())
									continue;
								allValuesets.add(dto);
								if(result.getVsacExpIdentifier() != null){
									if(!result.getVsacExpIdentifier().isEmpty() && !result.getVsacExpIdentifier().equalsIgnoreCase("")){
										ExpIdentifier = result.getVsacExpIdentifier();
									}
								}
							}
							searchDisplay.getCqlLeftNavBarPanelView().setAppliedQdmList(allValuesets);
							for(CQLQualityDataSetDTO valueset : allValuesets){
								//filtering out codes from valuesets list
								if (valueset.getOid().equals("419099009") || valueset.getOid().equals("21112-8"))
									continue;
		
								appliedValueSetTableList.add(valueset);		
							}

							searchDisplay.getCqlLeftNavBarPanelView().setAppliedQdmTableList(appliedValueSetTableList);
						}
						searchDisplay.hideAceEditorAutoCompletePopUp();
						appliedQDMEvent();

						//if UMLS is not logged in
						if (!MatContext.get().isUMLSLoggedIn()) {
							if(ExpIdentifier !=null){
								searchDisplay.getQdmView().getVSACExpansionProfileListBox().setEnabled(false);
								searchDisplay.getQdmView().getVSACExpansionProfileListBox().clear();
								searchDisplay.getQdmView().getVSACExpansionProfileListBox().addItem(ExpIdentifier);
								searchDisplay.getQdmView().getDefaultExpProfileSel().setValue(true);
								searchDisplay.getQdmView().getDefaultExpProfileSel().setEnabled(false);
								isExpansionProfile = true;
								expProfileToAllValueSet = ExpIdentifier;
							} else {
								expProfileToAllValueSet = "";
								isExpansionProfile = false;
							}
						} else {
							if(ExpIdentifier != null){
								isExpansionProfile = true;
								searchDisplay.getQdmView().getVSACExpansionProfileListBox().setEnabled(true);
								searchDisplay.getQdmView().setExpProfileList(MatContext.get()
										.getExpProfileList());
								searchDisplay.getQdmView().setDefaultExpansionProfileListBox();
								for(int j = 0; j < searchDisplay.getQdmView().getVSACExpansionProfileListBox().getItemCount(); j++){
									if(searchDisplay.getQdmView().getVSACExpansionProfileListBox().getItemText(j)
											.equalsIgnoreCase(ExpIdentifier)) {
										searchDisplay.getQdmView().getVSACExpansionProfileListBox().setItemSelected(j, true);
										searchDisplay.getQdmView().getVSACExpansionProfileListBox().setSelectedIndex(j);
										searchDisplay.getQdmView().getDefaultExpProfileSel().setValue(true);
										break;
									}
								}
							} else{
								isExpansionProfile = false;
							}
						}
					}
				});
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
	 * Build View for General info when General Info AnchorList item is clicked.
	 */
	private void generalInfoEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();

		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			searchDisplay.buildGeneralInformation();
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
			searchDisplay.buildAppliedQDM();
			boolean isEditable = MatContext.get().getMeasureLockService().checkForEditPermission();
			searchDisplay.getQdmView().buildAppliedValueSetCellTable(searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList(),
					isEditable);
				resetCQLValuesetearchPanel();
				searchDisplay.getQdmView().setWidgetsReadOnly(isEditable);
			
		}

	}

	/**
	 * Build View for Parameter when Parameter AnchorList item is clicked.
	 */
	private void parameterEvent() {
		unsetActiveMenuItem(currentSection);

		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
		searchDisplay.buildParameterLibraryView();

		searchDisplay.getCQLParametersView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());

		searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getParameterButtonBar().getDeleteButton().setTitle("Delete");
	}

	/**
	 * Build View for Includes when Includes AnchorList item is clicked.
	 */
	private void includesEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);

		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_INCLUDES_MENU;
		searchDisplay.getMainFlowPanel().clear();
		searchDisplay.getIncludeView().setIncludedList(searchDisplay.getCqlLeftNavBarPanelView()
				.getIncludedList(searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()));
		getAllIncludeLibraryList(searchDisplay.getIncludeView().getSearchTextBox().getText());
		searchDisplay.getIncludeView().getAliasNameTxtArea().setText("");
		searchDisplay.getIncludeView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
	}
	
	/**
	 * Build View for Definition when Definition AnchorList item is clicked.
	 */
	private void definitionEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);

		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
		searchDisplay.buildDefinitionLibraryView();
		
		searchDisplay.getCQlDefinitionsView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());

		searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getDefineButtonBar().getDeleteButton().setTitle("Delete");
	}

	/**
	 * Build View for Function when Funtion AnchorList item is clicked.
	 */
	private void functionEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		unsetActiveMenuItem(currentSection);
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
		searchDisplay.buildFunctionLibraryView();
		searchDisplay.getCqlFunctionsView().setWidgetReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());

		searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getFunctionButtonBar().getDeleteButton().setTitle("Delete");

	}

	/**
	 * Build View for View Cql when View Cql AnchorList item is clicked.
	 */
	private void viewCqlEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
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
		searchDisplay.getCqlAceEditor().setText("");
		MatContext.get().getMeasureService().getMeasureCQLFileData(MatContext.get().getCurrentMeasureId(),
				new AsyncCallback<SaveUpdateCQLResult>() {
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							if ((result.getCqlString() != null) && !result.getCqlString().isEmpty()) {
								//validateViewCQLFile(result.getCqlString());
								// searchDisplay.getCqlAceEditor().setText(result.getCqlString());
								
								searchDisplay.getCqlAceEditor().clearAnnotations();
								searchDisplay.getCqlAceEditor().removeAllMarkers();
								searchDisplay.getCqlAceEditor().redisplay();
								searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clear();
								searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().clear();
								searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationMessageAlert().clear();

								if (!result.getCqlErrors().isEmpty()) {
									searchDisplay.getCqlLeftNavBarPanelView().getWarningMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getVIEW_CQL_ERROR_MESSAGE());
									for (CQLErrors error : result.getCqlErrors()) {
										String errorMessage = new String();
										errorMessage = errorMessage.concat("Error in line : " + error.getErrorInLine() + " at Offset :"
												+ error.getErrorAtOffeset());
										int line = error.getErrorInLine();
										int column = error.getErrorAtOffeset();
										searchDisplay.getCqlAceEditor().addAnnotation(line - 1, column, error.getErrorMessage(),
												AceAnnotationType.WARNING);
									}
									searchDisplay.getCqlAceEditor().setText(result.getCqlString());
									searchDisplay.getCqlAceEditor().setAnnotations();
									searchDisplay.getCqlAceEditor().redisplay();
								} else {
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
											.createAlert(MatContext.get().getMessageDelegate().getVIEW_CQL_NO_ERRORS_MESSAGE());
									searchDisplay.getCqlAceEditor().setText(result.getCqlString());
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
	private List<String> getDefinitionList(List<CQLDefinition> definitionList) {

		List<String> defineList = new ArrayList<String>();

		for (int i = 0; i < definitionList.size(); i++) {
			defineList.add(definitionList.get(i).getDefinitionName());
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
	private List<String> getParamaterList(List<CQLParameter> parameterList) {

		List<String> paramList = new ArrayList<String>();

		for (int i = 0; i < parameterList.size(); i++) {
			paramList.add(parameterList.get(i).getParameterName());
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
	private List<String> getFunctionList(List<CQLFunctions> functionList) {

		List<String> funcList = new ArrayList<String>();

		for (int i = 0; i < functionList.size(); i++) {
			funcList.add(functionList.get(i).getFunctionName());
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
			final AceEditor editor = getAceEditorBasedOnCurrentSection(searchDisplay, currentSection);
			for (CQLErrors error : result.getCqlErrors()) {
				int startLine = error.getStartErrorInLine();
				int startColumn = error.getStartErrorAtOffset();
				editor.addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.WARNING);
				if (!isInvalid) {
					isInvalid = true;
				}
			}
		}

		return isInvalid;
	}

	/**
	 * Validate user defined input. In this functionality we are disabling all
	 * the fields in Search Panel except Name
	 * which are required to create new UserDefined QDM Element.
	 */
	private void validateUserDefinedInput() {
		if (searchDisplay.getQdmView().getUserDefinedInput().getValue().length() > 0) {
			isUserDefined = true;
			searchDisplay.getQdmView().getOIDInput().setEnabled(true);
			searchDisplay.getQdmView().getUserDefinedInput()
					.setTitle(searchDisplay.getQdmView().getUserDefinedInput().getValue());
			searchDisplay.getQdmView().getQDMExpProfileListBox().setEnabled(false);
			searchDisplay.getQdmView().getVersionListBox().setEnabled(false);

			searchDisplay.getQdmView().getRetrieveFromVSACButton().setEnabled(false);
			searchDisplay.getQdmView().getSaveButton().setEnabled(true);
		} else {
			isUserDefined = false;
			searchDisplay.getQdmView().getUserDefinedInput().setTitle("Enter Name");
			searchDisplay.getQdmView().getOIDInput().setEnabled(true);
			searchDisplay.getQdmView().getRetrieveFromVSACButton().setEnabled(true);
			searchDisplay.getQdmView().getSaveButton().setEnabled(false);
		}
	}

	/**
	 * Validate oid input. depending on the OID input we are disabling and
	 * enabling the fields in Search Panel
	 */
	private void validateOIDInput() {
		if (searchDisplay.getQdmView().getOIDInput().getValue().length() > 0) {
			isUserDefined = false;
			searchDisplay.getQdmView().getUserDefinedInput().setEnabled(false);
			searchDisplay.getQdmView().getSaveButton().setEnabled(false);
			searchDisplay.getQdmView().getRetrieveFromVSACButton().setEnabled(true);
		} else if (searchDisplay.getQdmView().getUserDefinedInput().getValue().length() > 0) {
			isUserDefined = true;
			searchDisplay.getQdmView().getQDMExpProfileListBox().clear();
			searchDisplay.getQdmView().getVersionListBox().clear();
			searchDisplay.getQdmView().getUserDefinedInput().setEnabled(true);
			searchDisplay.getQdmView().getSaveButton().setEnabled(true);

		} else {
			searchDisplay.getQdmView().getUserDefinedInput().setEnabled(true);
		}
	}

	/**
	 * Gets the ace editor based on current section.
	 *
	 * @param searchDisplay the search display
	 * @param currentSection the current section
	 * @return the ace editor based on current section
	 */
	private static AceEditor getAceEditorBasedOnCurrentSection(ViewDisplay searchDisplay, String currentSection) {
		AceEditor editor = null;
		switch (currentSection) {
		case CQLWorkSpaceConstants.CQL_DEFINE_MENU:
			editor = searchDisplay.getDefineAceEditor();
			break;
		case CQLWorkSpaceConstants.CQL_FUNCTION_MENU:
			editor = searchDisplay.getFunctionBodyAceEditor();
			break;
		case CQLWorkSpaceConstants.CQL_PARAMETER_MENU:
			editor = searchDisplay.getParameterAceEditor();
			break;
		default:
			/* editor = searchDisplay.getDefineAceEditor(); */
			break;
		}
		return editor;
	}

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
		final String definitionName = searchDisplay.getDefineNameTxtArea().getText();
		String definitionLogic = searchDisplay.getDefineAceEditor().getText();
		String defineContext = "";
		if (searchDisplay.getContextDefinePATRadioBtn().getValue()) {
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}
		if (!definitionName.isEmpty()) {

			if (!validator.validateForSpecialChar(definitionName.trim())) {

				final CQLDefinition define = new CQLDefinition();
				define.setDefinitionName(definitionName);
				define.setDefinitionLogic(definitionLogic);
				define.setContext(defineContext);

				if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId() != null) {
					CQLDefinition toBeModifiedObj = searchDisplay.getCqlLeftNavBarPanelView().getDefinitionMap()
							.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedDefinitionObjId());
					showSearchingBusy(true);
					MatContext.get().getMeasureService().deleteDefinition(MatContext.get().getCurrentMeasureId(),
							toBeModifiedObj, define, searchDisplay.getCqlLeftNavBarPanelView().getViewDefinitions(),
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
											searchDisplay.getDefineNameTxtArea().setText("");
											searchDisplay.getDefineAceEditor().setText("");
											searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedDefinitionObjId(null);
											searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
											searchDisplay.getDefineAceEditor().clearAnnotations();
											searchDisplay.getDefineAceEditor().removeAllMarkers();
											searchDisplay.getDefineAceEditor().redisplay();
											searchDisplay.getDefineAceEditor().setAnnotations();
											searchDisplay.getDefineAceEditor().redisplay();
											searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(false);
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
													.createAlert("This Definition has been deleted successfully.");

										} else if (result.getFailureReason() == 2) {
											searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
											searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
													.createAlert("Unable to find Node to modify.");
											searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
										}
									}
									showSearchingBusy(false);
								}
							});
				} else {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select a definition to delete.");
					searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
				}
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
			}

		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select a definition to delete.");
			searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
		}
	}

	/**
	 * Delete function.
	 */
	protected void deleteFunction() {

		searchDisplay.resetMessageDisplay();
		final String functionName = searchDisplay.getFuncNameTxtArea().getText();
		String functionBody = searchDisplay.getFunctionBodyAceEditor().getText();
		String funcContext = "";
		if (searchDisplay.getContextFuncPATRadioBtn().getValue()) {
			funcContext = "Patient";
		} else {
			funcContext = "Population";
		}
		if (!functionName.isEmpty()) {
			CQLFunctions function = new CQLFunctions();
			function.setFunctionLogic(functionBody);
			function.setFunctionName(functionName);
			function.setArgumentList(searchDisplay.getFunctionArgumentList());
			function.setContext(funcContext);
			if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId() != null) {
				CQLFunctions toBeModifiedFuncObj = searchDisplay.getCqlLeftNavBarPanelView().getFunctionMap()
						.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedFunctionObjId());
				showSearchingBusy(true);
				MatContext.get().getMeasureService().deleteFunctions(MatContext.get().getCurrentMeasureId(),
						toBeModifiedFuncObj, function, searchDisplay.getCqlLeftNavBarPanelView().getViewFunctions(),
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
										searchDisplay.getCqlLeftNavBarPanelView().updateFunctionMap();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().clearAlert();

										searchDisplay.getCqlLeftNavBarPanelView().getSearchSuggestFuncTextBox().setText("");
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);
										searchDisplay.getFuncNameTxtArea().setText("");
										searchDisplay.getFunctionBodyAceEditor().setText("");
										searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedFunctionObjId(null);
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getFunctionBodyAceEditor().clearAnnotations();
										searchDisplay.getFunctionBodyAceEditor().removeAllMarkers();
										searchDisplay.getFunctionBodyAceEditor().redisplay();
										searchDisplay.getFunctionBodyAceEditor().setAnnotations();
										searchDisplay.getFunctionBodyAceEditor().redisplay();
										searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.createAlert("This Function has been deleted successfully.");
									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Unable to find Node to modify.");
										searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
									}
									
									if (result.getFunction() != null) {
										searchDisplay
												.createAddArgumentViewForFunctions(result.getFunction().getArgumentList(),MatContext.get().getMeasureLockService().checkForEditPermission());
									}
								}
								showSearchingBusy(false);
							}
						});
			} else {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select a function to delete.");
				searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
			}
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select a function to delete.");
			searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
		}
	}

	/**
	 * Delete parameter.
	 */
	protected void deleteParameter() {

		searchDisplay.resetMessageDisplay();
		final String parameterName = searchDisplay.getParameterNameTxtArea().getText();
		String parameterBody = searchDisplay.getParameterAceEditor().getText();

		if (!parameterName.isEmpty()) {
			CQLParameter parameter = new CQLParameter();
			parameter.setParameterLogic(parameterBody);
			parameter.setParameterName(parameterName);
			if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId() != null) {
				CQLParameter toBeModifiedParamObj = searchDisplay.getCqlLeftNavBarPanelView().getParameterMap()
						.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedParamerterObjId());
				showSearchingBusy(true);
				MatContext.get().getMeasureService().deleteParameter(MatContext.get().getCurrentMeasureId(),
						toBeModifiedParamObj, parameter, searchDisplay.getCqlLeftNavBarPanelView().getViewParameterList(),
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
										searchDisplay.getParameterNameTxtArea().setText("");
										searchDisplay.getParameterAceEditor().setText("");
										searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedParamerterObjId(null);
										searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(false);
										searchDisplay.getParameterAceEditor().clearAnnotations();
										searchDisplay.getParameterAceEditor().removeAllMarkers();
										searchDisplay.getParameterAceEditor().redisplay();
										searchDisplay.getParameterAceEditor().setAnnotations();
										searchDisplay.getParameterAceEditor().redisplay();
										searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.createAlert("This Parameter has been deleted successfully.");
									} else if (result.getFailureReason() == 2) {
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().clearAlert();
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Unable to find Node to modify.");
										searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
									}
								}
								showSearchingBusy(false);
							}
						});
			} else {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select parameter to delete.");
				searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
			}
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select a parameter to delete.");
			searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
		}
	}
	
	protected void deleteInclude() {

		searchDisplay.resetMessageDisplay();
		final String aliasName = searchDisplay.getIncludeView().getAliasNameTxtArea().getText();
		String includeLibName = searchDisplay.getIncludeView().getCqlLibraryNameTextBox().getText();

		if (!aliasName.isEmpty()) {
			CQLIncludeLibrary cqlLibObject = new CQLIncludeLibrary();
			cqlLibObject.setCqlLibraryName(includeLibName);
			cqlLibObject.setAliasName(aliasName);
			if (searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId() != null) {
				CQLIncludeLibrary toBeModifiedIncludeObj = searchDisplay.getCqlLeftNavBarPanelView().getIncludeLibraryMap()
						.get(searchDisplay.getCqlLeftNavBarPanelView().getCurrentSelectedIncLibraryObjId());
				showSearchingBusy(true);
				MatContext.get().getMeasureService().deleteInclude(MatContext.get().getCurrentMeasureId(),
						toBeModifiedIncludeObj, cqlLibObject, searchDisplay.getCqlLeftNavBarPanelView().getViewIncludeLibrarys(),
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
										searchDisplay.getIncludeView().getViewCQLEditor().redisplay();
										searchDisplay.getIncludeView().getViewCQLEditor().setAnnotations();
										searchDisplay.getIncludeView().getViewCQLEditor().redisplay();
										searchDisplay.getIncludeView().getDeleteButton().setEnabled(false);
										searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
												.createAlert("This Included Library has been deleted successfully.");
										
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
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Please select an alias to delete.");
			searchDisplay.getIncludeView().getAliasNameTxtArea().setText(aliasName.trim());
		}
	}

	/**
	 * Load element look up node.
	 *//*
	private void loadElementLookUpNode() {

		MatContext.get().getMeasureService().getMeasureXmlForMeasure(MatContext.get().getCurrentMeasureId(),
				new AsyncCallback<MeasureXmlModel>() {

					@Override
					public void onSuccess(MeasureXmlModel result) {
						String xml = result != null ? result.getXml() : null;
						//setMeasureElementsMap(xml);

					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}*/

	/**
	 * Sets the qdm elements map. Also finds SubTree Node and corresponding Node
	 * Tree and add to SubTreeLookUpNode map. Also finds CQL dEfinitions and add
	 * to CQLDEfinitionsNode map.
	 *
	 * @param xml            the new qdm elements map
	 */
	/*private void setMeasureElementsMap(String xml) {

		CQLWorkSpaceConstants.elementLookUpName = new TreeMap<String, String>();
		CQLWorkSpaceConstants.elementLookUpNode = new TreeMap<String, Node>();
		CQLWorkSpaceConstants.elementLookUpDataTypeName = new TreeMap<String, String>();

		Document document = XMLParser.parse(xml);
		NodeList nodeList = document.getElementsByTagName("elementLookUp");
		setupElementLookupQDMNodes(nodeList);

		List<String> dataTypeList = new ArrayList<String>();
		dataTypeList.addAll(CQLWorkSpaceConstants.getElementLookUpDataTypeName().values());
		attributeService.getDatatypeList(dataTypeList, new AsyncCallback<Map<String, List<String>>>() {

			@Override
			public void onFailure(Throwable caught) {
				// Window.alert("I failed");

			}

			@Override
			public void onSuccess(Map<String, List<String>> datatypeMap) {
				CQLWorkSpaceConstants.setDatatypeMap(datatypeMap);
			}
		});
	}*/

	/**
	 * Sets the up element lookup QDM nodes.
	 *
	 * @param nodeList the new up element lookup QDM nodes
	 */
	public void setupElementLookupQDMNodes(NodeList nodeList) {
		if ((null != nodeList) && (nodeList.getLength() > 0)) {
			NodeList qdms = nodeList.item(0).getChildNodes();
			for (int i = 0; i < qdms.getLength(); i++) {
				if ("qdm".equals(qdms.item(i).getNodeName())) {
					NamedNodeMap namedNodeMap = qdms.item(i).getAttributes();
					String isSupplementData = namedNodeMap.getNamedItem("suppDataElement").getNodeValue();
					if (isSupplementData.equals("false")) { // filter
															// supplementDataElements
															// from
															// elementLookUp
						String name = namedNodeMap.getNamedItem("name").getNodeValue();
						// Prod Issue fixed : qdm name has trailing spaces which
						// is reterived frm VSAC.
						// So QDM attribute dialog box is throwing error in
						// FF.To fix that spaces are removed from start and end.
						name = name.trim();
						// name = name.replaceAll("^\\s+|\\s+$", "");
						String uuid = namedNodeMap.getNamedItem("uuid").getNodeValue();
						if (namedNodeMap.getNamedItem("instance") != null) {
							name = namedNodeMap.getNamedItem("instance").getNodeValue() + " of " + name;
						}

						if (namedNodeMap.getNamedItem("datatype") != null) {
							String dataType = namedNodeMap.getNamedItem("datatype").getNodeValue().trim();
							name = name + " : " + namedNodeMap.getNamedItem("datatype").getNodeValue();
							CQLWorkSpaceConstants.elementLookUpDataTypeName.put(uuid, dataType);
						}
						CQLWorkSpaceConstants.elementLookUpNode.put(name + "~" + uuid, qdms.item(i));
						CQLWorkSpaceConstants.elementLookUpName.put(uuid, name);
					}
				}
			}

		}
	}

	/**
	 * Adds the QDM Search Panel event Handlers.
	 */
	private void addQDMELmentSearchPanelHandlers() {
		addQDMElementExpProfileHandlers();

		/**
		 * this functionality is to clear the content on the QDM Element Search
		 * Panel.
		 */
		searchDisplay.getQdmView().getCancelQDMButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				isModified = false;
				resetCQLValuesetearchPanel();
			}
		});

		searchDisplay.getQdmView().getUpdateFromVSACButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					searchDisplay.resetMessageDisplay();
					updateVSACValueSets();	
				}
			}
		});
	
		/**
		 * this functionality is to retrieve the value set from VSAC with latest
		 * information which consists of Expansion Profile list and Version
		 * List.
		 */
		searchDisplay.getQdmView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					String version = null;
					String expansionProfile = null;
					searchValueSetInVsac(version, expansionProfile);
				}
			}
		});

		/**
		 * this handler is invoked when apply button is clicked on search Panel
		 * in QDM elements tab and this is to add new value set or user Defined
		 * QDM to the Applied QDM list.
		 */
		searchDisplay.getQdmView().getSaveButton().addClickHandler(new ClickHandler() {

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
				}
			}
		});

		/**
		 * Adding value Change handler for UserDefined Input in Search Panel in
		 * QDM Elements Tab
		 * 
		 */
		searchDisplay.getQdmView().getUserDefinedInput().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				searchDisplay.resetMessageDisplay();
				validateUserDefinedInput();
			}
		});

		/**
		 * Adding value change handler for OID input in Search Panel in QDM
		 * elements Tab
		 */

		searchDisplay.getQdmView().getOIDInput().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				searchDisplay.resetMessageDisplay();
				validateOIDInput();
			}
		});

		/**
		 * value change handler for Expansion Profile in Search Panel in QDM
		 * Elements Tab
		 */
		searchDisplay.getQdmView().getQDMExpProfileListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				searchDisplay.resetMessageDisplay();
				if (!searchDisplay.getQdmView()
						.getExpansionProfileValue(searchDisplay.getQdmView().getQDMExpProfileListBox())
						.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
					searchDisplay.getQdmView().getVersionListBox().setSelectedIndex(0);
				}
			}
		});

		/**
		 * value Change Handler for Version listBox in Search Panel
		 */
		searchDisplay.getQdmView().getVersionListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				searchDisplay.resetMessageDisplay();
				if (!searchDisplay.getQdmView().getVersionValue(searchDisplay.getQdmView().getVersionListBox())
						.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
					searchDisplay.getQdmView().getQDMExpProfileListBox().setSelectedIndex(0);
				}

			}
		});
	}
	
	/**
	 * Update vsac value sets.
	 */
	private void updateVSACValueSets() {
		
		String expansionId = null;
		if(expProfileToAllValueSet.isEmpty()){
			expansionId = null;
		} else {
			expansionId = expProfileToAllValueSet;
		}
		searchDisplay.getQdmView().showSearchingBusyOnQDM(true);
		vsacapiService.updateCQLVSACValueSets(MatContext.get().getCurrentMeasureId(), expansionId,
				new AsyncCallback<VsacApiResult>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				searchDisplay.getQdmView().showSearchingBusyOnQDM(false);
			}
			
			@Override
			public void onSuccess(final VsacApiResult result) {
				searchDisplay.getQdmView().showSearchingBusyOnQDM(false);
				if (result.isSuccess()) {
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getVSAC_UPDATE_SUCCESSFULL());
					List<CQLQualityDataSetDTO> appliedListModel = new ArrayList<CQLQualityDataSetDTO>();
					for (CQLQualityDataSetDTO cqlQDMDTO : result.getUpdatedCQLQualityDataDTOLIst()) {
						if (!ConstantMessages.EXPIRED_OID.equals(cqlQDMDTO
								.getDataType()) && !ConstantMessages.BIRTHDATE_OID.equals(cqlQDMDTO
										.getDataType()))  {
							appliedListModel.add(cqlQDMDTO);
						} 
					}
					searchDisplay.getQdmView().buildAppliedValueSetCellTable(appliedListModel, MatContext.get().getMeasureLockService().checkForEditPermission());
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(convertMessage(result.getFailureReason()));
				}
			}
		});
	}

	/**
	 * click Handlers for ExpansioN Profile Panel in new QDM Elements Tab.
	 */
	private void addQDMElementExpProfileHandlers() {
		searchDisplay.getQdmView().getApplyDefaultExpansionIdButton().addClickHandler(new ClickHandler() {

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
					int selectedindex = searchDisplay.getQdmView().getVSACExpansionProfileListBox().getSelectedIndex();
					String selectedValue =
							searchDisplay.getQdmView().getVSACExpansionProfileListBox().getValue(selectedindex);

					if(!selectedValue.equalsIgnoreCase("--Select--")){
						updateAllValueSetWithExpProfile(appliedValueSetTableList);
						isExpansionProfile = true;
						expProfileToAllValueSet = selectedValue;} 
					else if(!searchDisplay.getQdmView().getDefaultExpProfileSel().getValue()){ 
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

		searchDisplay.getQdmView().getDefaultExpProfileSel()
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
							searchDisplay.getQdmView().getVSACExpansionProfileListBox().setEnabled(true);
							searchDisplay.getQdmView().setExpProfileList(MatContext.get().getExpProfileList());
							searchDisplay.getQdmView().setDefaultExpansionProfileListBox();
						} else if (event.getValue().toString().equals("false")) {
							searchDisplay.getQdmView().getVSACExpansionProfileListBox().setEnabled(false);
							searchDisplay.getQdmView().setDefaultExpansionProfileListBox();
						}

					}
				});

	}
	
	/**
	 * Update all applied QDM Elements with default Expansion Profile.
	 *
	 * @param list the list
	 */
	private void updateAllValueSetWithExpProfile(List<CQLQualityDataSetDTO> list) {
		List<CQLQualityDataSetDTO> modifiedCqlValueSetList = new ArrayList<CQLQualityDataSetDTO>();
		for (CQLQualityDataSetDTO cqlQualityDataSetDTO : list) {
			if (!ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(cqlQualityDataSetDTO.getOid())) {
				cqlQualityDataSetDTO.setVersion("1.0");
				if (!expProfileToAllValueSet.isEmpty()) {
					cqlQualityDataSetDTO.setExpansionIdentifier(expProfileToAllValueSet);
				}
				if (searchDisplay.getQdmView().getDefaultExpProfileSel().getValue()) {
					modifiedCqlValueSetList.add(cqlQualityDataSetDTO);
				}
			}
		}
		//Updating all SDE
		updateAllSuppleDataElementsWithExpProfile(modifiedCqlValueSetList);
	}
	
	/**
	 * Update Expansion Profile in Default Four SDE's.
	 *
	 * @param modifiedCqlQDMList the modified cql QDM list
	 */
	protected void updateAllSuppleDataElementsWithExpProfile(final List<CQLQualityDataSetDTO> modifiedCqlQDMList) {
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
	}
	
	/**
	 * Update all in measure xml.
	 *
	 * @param modifiedCqlQDMList the modified cql QDM list
	 */
	private void updateAllInMeasureXml(List<CQLQualityDataSetDTO> modifiedCqlQDMList) {
		String measureId =  MatContext.get().getCurrentMeasureId();
		service.updateCQLMeasureXMLForExpansionProfile(modifiedCqlQDMList, measureId, expProfileToAllValueSet,
				new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				getAppliedValueSetList();
				if (!searchDisplay.getQdmView().getDefaultExpProfileSel().getValue()) {
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
	
	/**
	 * Search value set in vsac.
	 *
	 * @param version
	 *            the version
	 * @param expansionProfile
	 *            the expansion profile
	 */
	private void searchValueSetInVsac(String version, String expansionProfile) {

		final String oid = searchDisplay.getQdmView().getOIDInput().getValue();
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
		searchDisplay.getQdmView().showSearchingBusyOnQDM(true);
		expProfileToAllValueSet = getExpProfileValue();
		if (expProfileToAllValueSet.isEmpty()) {
			isExpansionProfile = false;
			expansionProfile = null;
		} else {
			isExpansionProfile = true;
			expansionProfile = expProfileToAllValueSet;
		}

		vsacapiService.getMostRecentValueSetByOID(oid, expansionProfile, new AsyncCallback<VsacApiResult>() {

			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
				searchDisplay.getQdmView().showSearchingBusyOnQDM(false);
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
					searchDisplay.getQdmView().getOIDInput().setTitle(oid);
					searchDisplay.getQdmView().getUserDefinedInput().setValue(matValueSets.get(0).getDisplayName());
					searchDisplay.getQdmView().getUserDefinedInput().setTitle(matValueSets.get(0).getDisplayName());
					searchDisplay.getQdmView().getQDMExpProfileListBox().setEnabled(true);
					searchDisplay.getQdmView().getVersionListBox().setEnabled(true);

					searchDisplay.getQdmView().getSaveButton().setEnabled(true);

					if (isExpansionProfile) {
						searchDisplay.getQdmView().getQDMExpProfileListBox().setEnabled(false);
						searchDisplay.getQdmView().getVersionListBox().setEnabled(false);
						searchDisplay.getQdmView().getQDMExpProfileListBox().clear();
						searchDisplay.getQdmView().getQDMExpProfileListBox().addItem(expProfileToAllValueSet,
								expProfileToAllValueSet);
					} else {
						searchDisplay.getQdmView().setQDMExpProfileListBox(
								getProfileList(MatContext.get().getVsacExpProfileList()));
						getVSACVersionListByOID(oid);
						searchDisplay.getQdmView().getQDMExpProfileListBox().setEnabled(true);
						searchDisplay.getQdmView().getVersionListBox().setEnabled(true);
					}
					searchDisplay.getQdmView().showSearchingBusyOnQDM(false);
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
							.createAlert(MatContext.get().getMessageDelegate().getVSAC_RETRIEVAL_SUCCESS());
					searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().setVisible(true);

				} else {
					String message = convertMessage(result.getFailureReason());
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(message);
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().setVisible(true);
					searchDisplay.getQdmView().showSearchingBusyOnQDM(false);
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
		final String codeListName = matValueSetTransferObject.getMatValueSet().getDisplayName();
		String expProfile = matValueSetTransferObject.getMatValueSet().getExpansionProfile();
		String version = matValueSetTransferObject.getMatValueSet().getVersion();
		expProfileToAllValueSet = getExpProfileValue();
		if(!expProfileToAllValueSet.equalsIgnoreCase("")){
			expProfile = expProfileToAllValueSet;
			matValueSetTransferObject.getMatValueSet().setExpansionProfile(expProfile);
		}
		if(expProfile == null){
			expProfile = "";
			matValueSetTransferObject.getMatValueSet().setExpansionProfile(expProfile);
		}
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
									resetCQLValuesetearchPanel();
									searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert(message);
									getAppliedValueSetList();
								} else {
									if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
										searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
												MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg());
									}
								}
							}
							showSearchingBusy(true);
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
				final String userDefinedInput = matValueSetTransferObject.getUserDefinedText();
				String expProfile = searchDisplay.getQdmView()
						.getExpansionProfileValue(searchDisplay.getQdmView().getQDMExpProfileListBox());
				String version = searchDisplay.getQdmView()
						.getVersionValue(searchDisplay.getQdmView().getVersionListBox());
				if (expProfile == null) {
					expProfile = "";
				}
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
												resetCQLValuesetearchPanel();
												getAppliedValueSetList();
											}
										} else {
											if (result.getFailureReason() == result.ALREADY_EXISTS) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
														MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg());
											} else if (result.getFailureReason() == result.SERVER_SIDE_VALIDATION) {
												searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert("Invalid input data.");
											}
										}
									}
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
			String expansionId;
			String version;
			String displayName = searchDisplay.getQdmView().getUserDefinedInput().getText();
			expansionId = searchDisplay.getQdmView().getExpansionProfileValue(searchDisplay.getQdmView().getQDMExpProfileListBox());
			version = searchDisplay.getQdmView().getVersionValue(searchDisplay.getQdmView().getVersionListBox());
			if(expansionId == null){
				expansionId = "";
			}
			if(version == null){
				version = "";
			}
			expProfileToAllValueSet = getExpProfileValue();
			if(modifyValueSetDTO.getExpansionIdentifier() == null){
				if(expProfileToAllValueSet.equalsIgnoreCase("")){
					modifyValueSetDTO.setExpansionIdentifier("");
				} else {
					modifyValueSetDTO.setExpansionIdentifier(expProfileToAllValueSet);
				}
			}
			if(modifyValueSetDTO.getVersion() == null){
				modifyValueSetDTO.setVersion("");
			}
			
			modifyValueSetList(modifyValueSetDTO);
			
			if(!CheckNameInValueSetList(displayName)){
				updateAppliedValueSetsList(modifyWithDTO, null, modifyValueSetDTO, false);
			}
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(MatContext.get().
					getMessageDelegate().getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE());
		}
	}
	
	
	private String getExpProfileValue() {
	int selectedindex =	searchDisplay.getQdmView().getVSACExpansionProfileListBox().getSelectedIndex();
	String result = searchDisplay.getQdmView().getVSACExpansionProfileListBox().getValue(selectedindex);
	if (!result.equalsIgnoreCase(MatContext.PLEASE_SELECT)){
		return result;
	}else{
		return "";
	}
	}

	/**
	 * Modify QDM with out value set.
	 */
	private void modifyUserDefinedValueSet() {
		modifyValueSetDTO.setExpansionIdentifier("");
		modifyValueSetDTO.setVersion("");
		if ((searchDisplay.getQdmView().getUserDefinedInput().getText().trim().length() > 0)) {
			final String usrDefDisplayName = searchDisplay.getQdmView().getUserDefinedInput().getText();
			String expProfile = searchDisplay.getQdmView().getExpansionProfileValue(searchDisplay.getQdmView().getQDMExpProfileListBox());
			String version = searchDisplay.getQdmView().getVersionValue(searchDisplay.getQdmView().getVersionListBox());
			if(expProfile == null){
				expProfile = "";
			}
			if(version == null){
				version = "";
			}
			
			modifyValueSetList(modifyValueSetDTO);
			if(!CheckNameInValueSetList(usrDefDisplayName)){
				CQLValueSetTransferObject object = new CQLValueSetTransferObject();
				object.setUserDefinedText(searchDisplay.getQdmView().getUserDefinedInput().getText());
				object.scrubForMarkUp();
				ValueSetNameInputValidator valueSetNameInputValidator = new ValueSetNameInputValidator();
				/*qdmInputValidator.validate(object);*/
				String message = valueSetNameInputValidator.validate(object);
				if (message.isEmpty()) {
				
					CodeListSearchDTO modifyWithDTO = new CodeListSearchDTO();
					modifyWithDTO.setName(searchDisplay.getQdmView().getUserDefinedInput().getText());
					updateAppliedValueSetsList(null, modifyWithDTO, modifyValueSetDTO, true);
				} else {
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(message);
				}
			}
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
		String version = searchDisplay.getQdmView().getVersionValue(searchDisplay.getQdmView().getVersionListBox());
		String expansionProfile = searchDisplay.getQdmView().getExpansionProfileValue(
				searchDisplay.getQdmView().getQDMExpProfileListBox());
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		matValueSetTransferObject.setMeasureId(MatContext.get().getCurrentMeasureId());
		matValueSetTransferObject.setMatValueSet(matValueSet);
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setCqlQualityDataSetDTO(qualityDataSetDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		int expIdselectedIndex = searchDisplay.getQdmView().getQDMExpProfileListBox().getSelectedIndex();
		int versionSelectionIndex = searchDisplay.getQdmView().getVersionListBox().getSelectedIndex();
		if((version != null) || (expansionProfile != null) ){
			if (!expansionProfile.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !expansionProfile.equalsIgnoreCase("")) {
				matValueSetTransferObject.setExpansionProfile(true);
				matValueSetTransferObject.setVersion(false);
				currentMatValueSet.setExpansionProfile(searchDisplay
						.getQdmView().getQDMExpProfileListBox().getValue(expIdselectedIndex));
				
			} else if (!version.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !version.equalsIgnoreCase("")){
				matValueSetTransferObject.setVersion(true);
				matValueSetTransferObject.setExpansionProfile(false);
				currentMatValueSet.setVersion(searchDisplay.getQdmView().getVersionListBox().getValue(versionSelectionIndex));
			}
		}
		
		if(!expProfileToAllValueSet.isEmpty() && !isUSerDefined){
			currentMatValueSet.setExpansionProfile(expProfileToAllValueSet);
			currentMatValueSet.setVersion("1.0");
			matValueSetTransferObject.setExpansionProfile(true);
			matValueSetTransferObject.setVersion(false);
		}
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
						resetCQLValuesetearchPanel();
						modifyValueSetDTO = result.getCqlQualityDataSetDTO();
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert()
						.createAlert(MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
						getAppliedValueSetList();
					} else{
						
						if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
										MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg());
						
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
						if (valueset.getOid().equals("419099009") || valueset.getOid().equals("21112-8")) 
							continue;
								
						appliedValueSetTableList.add(valueset);
					}
					
					
					searchDisplay.getQdmView().buildAppliedValueSetCellTable(appliedValueSetTableList, MatContext.get().getMeasureLockService()
							.checkForEditPermission());
					//if UMLS is not logged in
					if (!MatContext.get().isUMLSLoggedIn()) {
						if(result.getVsacExpIdentifier()!=null){
							searchDisplay.getQdmView().getVSACExpansionProfileListBox().setEnabled(false);
							searchDisplay.getQdmView().getVSACExpansionProfileListBox().clear();
							searchDisplay.getQdmView().getVSACExpansionProfileListBox().addItem(result.getVsacExpIdentifier());
							searchDisplay.getQdmView().getDefaultExpProfileSel().setValue(true);
							searchDisplay.getQdmView().getDefaultExpProfileSel().setEnabled(false);
							isExpansionProfile = true;
							expProfileToAllValueSet = result.getVsacExpIdentifier();
						} else {
							expProfileToAllValueSet = "";
							isExpansionProfile = false;
						}
					} else {
						if(result.getVsacExpIdentifier()!=null){
							searchDisplay.getQdmView().getVSACExpansionProfileListBox().setEnabled(true);
							searchDisplay.getQdmView().setExpProfileList(MatContext.get()
									.getExpProfileList());
							searchDisplay.getQdmView().setDefaultExpansionProfileListBox();
							for(int i = 0; i < searchDisplay.getQdmView().getVSACExpansionProfileListBox().getItemCount(); i++){
								if(searchDisplay.getQdmView().getVSACExpansionProfileListBox().getItemText(i)
										.equalsIgnoreCase(result.getVsacExpIdentifier())) {
									searchDisplay.getQdmView().getVSACExpansionProfileListBox().setSelectedIndex(i);
									break;
								}
							}
							searchDisplay.getQdmView().getDefaultExpProfileSel().setEnabled(true);
							searchDisplay.getQdmView().getDefaultExpProfileSel().setValue(true);
							
							expProfileToAllValueSet = result.getVsacExpIdentifier();
							isExpansionProfile = true;
						} else {
							searchDisplay.getQdmView().getDefaultExpProfileSel().setEnabled(true);
							expProfileToAllValueSet = "";
							isExpansionProfile = false;
						}
					}
					
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
		String version = searchDisplay.getQdmView().getVersionValue(searchDisplay.getQdmView().getVersionListBox());
		String expansionProfile = searchDisplay.getQdmView().getExpansionProfileValue(
				searchDisplay.getQdmView().getQDMExpProfileListBox());
		int expIdSelectionIndex = searchDisplay.getQdmView().getQDMExpProfileListBox().getSelectedIndex();
		int versionSelectionIndex = searchDisplay.getQdmView().getVersionListBox().getSelectedIndex();
		
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		matValueSetTransferObject.setMeasureId(measureID);
		CodeListSearchDTO codeListSearchDTO = new CodeListSearchDTO();
		codeListSearchDTO.setName(searchDisplay.getQdmView().getUserDefinedInput().getText());
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		if((version != null) || (expansionProfile != null) ){
			if (!expansionProfile.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !expansionProfile.equalsIgnoreCase("")) {
				matValueSetTransferObject.setExpansionProfile(true);
				matValueSetTransferObject.setVersion(false);
				currentMatValueSet.setExpansionProfile(searchDisplay
						.getQdmView().getQDMExpProfileListBox().getValue(expIdSelectionIndex));
				
			} else if (!version.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !version.equalsIgnoreCase("")){
				matValueSetTransferObject.setVersion(true);
				matValueSetTransferObject.setExpansionProfile(false);
				currentMatValueSet.setVersion(searchDisplay.getQdmView().getVersionListBox().getValue(versionSelectionIndex));
			}
		}
		
		
		if (!expProfileToAllValueSet.isEmpty() && !isUserDefined) {
			currentMatValueSet.setExpansionProfile(expProfileToAllValueSet);
			matValueSetTransferObject.setExpansionProfile(true);
			matValueSetTransferObject.setVersion(false);
		}
		matValueSetTransferObject.setMatValueSet(currentMatValueSet);
		matValueSetTransferObject.setMeasureId(measureID);
		matValueSetTransferObject.setUserDefinedText(searchDisplay.getQdmView().getUserDefinedInput().getText());
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
							.createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg());
					return true;
					
				}
			}
		}
		return false;
	}

	
	/**
	 * Convert message.
	 * 
	 * @param id
	 *            the id
	 * @return the string
	 */
	private String convertMessage(final int id) {
		String message;
		switch (id) {
		case VsacApiResult.UMLS_NOT_LOGGEDIN:
			message = MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN();
			break;
		case VsacApiResult.OID_REQUIRED:
			message = MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED();
			break;
		default:
			message = MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED();
		}
		return message;
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
					searchDisplay.getQdmView().setQDMVersionListBoxOptions(getVersionList(result.getVsacVersionResp()));
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
	 * Reset QDM search panel.
	 */
	private void resetCQLValuesetearchPanel() {
		HTML searchHeaderText = new HTML("<strong>Search</strong>");
		searchDisplay.getQdmView().getSearchHeader().clear();
		searchDisplay.getQdmView().getSearchHeader().add(searchHeaderText);
		
		searchDisplay.getQdmView().getOIDInput().setEnabled(true);
		searchDisplay.getQdmView().getOIDInput().setValue("");
		searchDisplay.getQdmView().getOIDInput().setTitle("Enter OID");
		
		searchDisplay.getQdmView().getUserDefinedInput().setEnabled(true);
		searchDisplay.getQdmView().getUserDefinedInput().setValue("");
		searchDisplay.getQdmView().getUserDefinedInput().setTitle("Enter Name");
		
		searchDisplay.getQdmView().getQDMExpProfileListBox().clear();
		searchDisplay.getQdmView().getVersionListBox().clear();
		
		searchDisplay.getQdmView().getQDMExpProfileListBox().setEnabled(false);
		searchDisplay.getQdmView().getVersionListBox().setEnabled(false);
		
		searchDisplay.getQdmView().getSaveButton().setEnabled(false);
		
		searchDisplay.getQdmView().getApplyDefaultExpansionIdButton().setEnabled(true);
		searchDisplay.getQdmView().getUpdateFromVSACButton().setEnabled(true);
	}
	

	/**
	 * On modify value set qdm.
	 *
	 * @param result the result
	 * @param isUserDefined the is user defined
	 */
	private void onModifyValueSet(CQLQualityDataSetDTO result, boolean isUserDefined){
		
		String oid = isUserDefined ? "" : result.getOid();
		searchDisplay.getQdmView().getOIDInput().setEnabled(true);
		
		searchDisplay.getQdmView().getOIDInput().setValue(oid);
		searchDisplay.getQdmView().getOIDInput().setTitle(oid);
		
		searchDisplay.getQdmView().getRetrieveFromVSACButton().setEnabled(!isUserDefined);
		
		searchDisplay.getQdmView().getUserDefinedInput().setEnabled(isUserDefined);
		searchDisplay.getQdmView().getUserDefinedInput().setValue(result.getCodeListName());
		searchDisplay.getQdmView().getUserDefinedInput().setTitle(result.getCodeListName());
		
		searchDisplay.getQdmView().getQDMExpProfileListBox().clear();
		if(result.getExpansionIdentifier() != null){
			if(!isUserDefined)
				searchDisplay.getQdmView().getQDMExpProfileListBox().addItem(result.getExpansionIdentifier(),result.getExpansionIdentifier());
		}
		searchDisplay.getQdmView().getQDMExpProfileListBox().setEnabled(false);
		
		searchDisplay.getQdmView().getVersionListBox().clear();
		searchDisplay.getQdmView().getVersionListBox().setEnabled(false);
		
		expProfileToAllValueSet = getExpProfileValue();
		
		if(!expProfileToAllValueSet.isEmpty()){
			searchDisplay.getQdmView().getQDMExpProfileListBox().clear();
			if(!isUserDefined)
				searchDisplay.getQdmView().getQDMExpProfileListBox().addItem(expProfileToAllValueSet,
						expProfileToAllValueSet);
		}
		
		searchDisplay.getQdmView().getSaveButton().setEnabled(isUserDefined);
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
	private List<? extends HasListBox> getProfileList(List<VSACExpansionProfile> list) {
		return list;
	}
	public void showSearchingBusy(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setEnabled(!busy);
		if(!currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_INCLUDES_MENU))
			searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setEnabled(!busy);
		if(MatContext.get().getLibraryLockService().checkForEditPermission()) {
			searchDisplay.getCqlGeneralInformationView().setWidgetReadOnly(!busy);
			searchDisplay.getIncludeView().getSaveButton().setEnabled(!busy);
			searchDisplay.getIncludeView().getEraseButton().setEnabled(!busy);
			
			searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getSaveButton().setEnabled(!busy);
			searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getEraseButton().setEnabled(!busy);
			searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getDeleteButton().setEnabled(!busy);
			searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getInsertButton().setEnabled(!busy);
			searchDisplay.getCQlDefinitionsView().getDefineButtonBar().getInfoButton().setEnabled(!busy);
			
			searchDisplay.getCQLParametersView().getParameterButtonBar().getSaveButton().setEnabled(!busy);
			searchDisplay.getCQLParametersView().getParameterButtonBar().getEraseButton().setEnabled(!busy);
			searchDisplay.getCQLParametersView().getParameterButtonBar().getDeleteButton().setEnabled(!busy);
			searchDisplay.getCQLParametersView().getParameterButtonBar().getInfoButton().setEnabled(!busy);
			
			searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getSaveButton().setEnabled(!busy);
			searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getEraseButton().setEnabled(!busy);
			searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getDeleteButton().setEnabled(!busy);
			searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getInsertButton().setEnabled(!busy);
			searchDisplay.getCqlFunctionsView().getFunctionButtonBar().getInfoButton().setEnabled(!busy);
			searchDisplay.getCqlFunctionsView().getAddNewArgument().setEnabled(!busy);
		}
		searchDisplay.getIncludeView().getSearchButton().setEnabled(!busy);
		
		
	}

}
