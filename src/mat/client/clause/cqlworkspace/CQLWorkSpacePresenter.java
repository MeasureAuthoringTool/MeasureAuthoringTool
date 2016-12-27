package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.PanelCollapse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.MatPresenter;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.cqlworkspace.CQLWorkSpaceView.Observer;
import mat.client.codelist.HasListBox;
import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.DeleteConfirmationMessageAlert;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.JSONAttributeModeUtility;
import mat.client.shared.JSONCQLTimingExpressionUtility;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.shared.WarningMessageAlert;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.MatValueSet;
import mat.model.VSACExpansionIdentifier;
import mat.model.VSACVersion;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CQLErrors;
import mat.shared.CQLModelValidator;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLPresenterNavBarWithList.
 */
public class CQLWorkSpacePresenter implements MatPresenter {
	/** The Measurement Period OID . */
	private static final String MEASUREMENT_PERIOD_OID = "2.16.840.1.113883.3.67.1.101.1.53";
	
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
	
	CQLModelValidator validator = new CQLModelValidator();
	
	/** The vsacapi service. */
	private final VSACAPIServiceAsync vsacapiService = MatContext.get()
			.getVsacapiServiceAsync();
	
	/** The is u ser defined. */
	private boolean isUserDefined = false;
	
	/** The exp profile to all qdm. */
	private String expIdentifierToAllQDM = "";
	
	/** The is modfied. */
	private boolean isModified = false;
	
	/** The is expansion profile. */
	private boolean isExpansionIdentifier = false;
	
	/** The is all oi ds updated. */
	private boolean  isAllOIDsUpdated = false;
	
	/** The current mat value set. */
	private MatValueSet currentMatValueSet;
	/**
	 * The Interface ViewDisplay.
	 */
	public static interface ViewDisplay {
		
		/**
		 * Top Main panel of CQL Workspace Tab.
		 * @return HorizontalPanel
		 */
		VerticalPanel getMainPanel();
		
		/**
		 * Generates View for CQLWorkSpace tab.
		 */
		void buildView();
		
		/**
		 * Gets the definition library.
		 *
		 * @return the definition library
		 */
		AnchorListItem getDefinitionLibrary();
		
		/**
		 * Gets the general information.
		 *
		 * @return the general information
		 */
		AnchorListItem getGeneralInformation();
		
		/**
		 * Gets the parameter library.
		 *
		 * @return the parameter library
		 */
		AnchorListItem getParameterLibrary();
		
		/**
		 * Gets the function library.
		 *
		 * @return the function library
		 */
		AnchorListItem getFunctionLibrary();
		
		/**
		 * Builds the general information.
		 */
		void buildGeneralInformation();
		
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
		 * Gets the view cql.
		 *
		 * @return the view cql
		 */
		AnchorListItem getViewCQL();
		
		
		/**
		 * Update suggest oracle.
		 */
		void updateSuggestOracle();
		
		/**
		 * Clear and add parameter names to list box.
		 */
		void clearAndAddParameterNamesToListBox();
		
		/**
		 * Gets the adds the parameter button.
		 *
		 * @return the adds the parameter button
		 */
		Button getAddParameterButton();
		
		/**
		 * Gets the removes the parameter button.
		 *
		 * @return the removes the parameter button
		 */
		Button getRemoveParameterButton();
		
		/**
		 * Gets the parameter name txt area.
		 *
		 * @return the parameter name txt area
		 */
		TextBox getParameterNameTxtArea();
		
		
		/**
		 * Gets the view parameter list.
		 *
		 * @return the view parameter list
		 */
		List<CQLParameter> getViewParameterList();
		
		/**
		 * Gets the parameter map.
		 *
		 * @return the parameter map
		 */
		Map<String, CQLParameter> getParameterMap();
		
		/**
		 * Gets the parameter name map.
		 *
		 * @return the parameter name map
		 */
		Map<String, String> getParameterNameMap();
		
		
		/**
		 * Gets the parameter name list box.
		 *
		 * @return the parameter name list box
		 */
		ListBox getParameterNameListBox();
		
		/**
		 * Update suggest define oracle.
		 */
		void updateSuggestDefineOracle();
		
		/**
		 * Clear and add definition names to list box.
		 */
		void clearAndAddDefinitionNamesToListBox();
		
		/**
		 * Gets the define name map.
		 *
		 * @return the define name map
		 */
		Map<String, String> getDefineNameMap();
		
		/**
		 * Gets the definition map.
		 *
		 * @return the definition map
		 */
		Map<String, CQLDefinition> getDefinitionMap();
		
		/**
		 * Gets the define name list box.
		 *
		 * @return the define name list box
		 */
		ListBox getDefineNameListBox();
		
		/**
		 * Gets the delete define button.
		 *
		 * @return the delete define button
		 */
		Button getDeleteDefineButton();
		
		/**
		 * Gets the view definitions.
		 *
		 * @return the view definitions
		 */
		List<CQLDefinition> getViewDefinitions();
		
		/**
		 * Sets the view definitions.
		 *
		 * @param viewDefinitions the new view definitions
		 */
		void setViewDefinitions(List<CQLDefinition> viewDefinitions);
		
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
		VerticalPanel getMainVPanel();
		
		
		/**
		 * Gets the message panel.
		 *
		 * @return the message panel
		 */
		HorizontalPanel getMessagePanel();
		/**
		 * Gets the param badge.
		 *
		 * @return the param badge
		 */
		Badge getParamBadge();
		
		/**
		 * Gets the param collapse.
		 *
		 * @return the param collapse
		 */
		PanelCollapse getParamCollapse();
		
		/**
		 * Gets the define collapse.
		 *
		 * @return the define collapse
		 */
		PanelCollapse getDefineCollapse();
		
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
		 * Gets the define badge.
		 *
		 * @return the define badge
		 */
		Badge getDefineBadge();
		
		/**
		 * Gets the clicked menu.
		 *
		 * @return the clicked menuF
		 */
		String getClickedMenu();
		/**
		 * Sets the clicked menu.
		 *
		 * @param clickedMenu the new clicked menu
		 */
		void setClickedMenu(String clickedMenu);
		/**
		 * Gets the current selected definition obj id.
		 *
		 * @return the current selected definition obj id
		 */
		String getCurrentSelectedDefinitionObjId();
		
		/**
		 * Update define map.
		 */
		void updateDefineMap();
		
		/**
		 * Gets the cql ace editor.
		 *
		 * @return the cql ace editor
		 */
		AceEditor getCqlAceEditor();
		
		/**
		 * Sets the current selected definition obj id.
		 *
		 * @param currentSelectedDefinitionObjId the new current selected definition obj id
		 */
		void setCurrentSelectedDefinitionObjId(String currentSelectedDefinitionObjId);
		
		/**
		 * Gets the current selected paramerter obj id.
		 *
		 * @return the current selected paramerter obj id
		 */
		String getCurrentSelectedParamerterObjId();
		
		/**
		 * Sets the current selected paramerter obj id.
		 *
		 * @param currentSelectedParamerterObjId the new current selected paramerter obj id
		 */
		void setCurrentSelectedParamerterObjId(String currentSelectedParamerterObjId);
		
		/**
		 * Sets the view parameter list.
		 *
		 * @param viewParameterList the new view parameter list
		 */
		void setViewParameterList(List<CQLParameter> viewParameterList);
		
		/**
		 * Update param map.
		 */
		void updateParamMap();
		
		/**
		 * Gets the success message alert.
		 *
		 * @return the success message alert
		 */
		MessageAlert getSuccessMessageAlert();
		
		/**
		 * Sets the success message alert.
		 *
		 * @param successMessageAlert
		 *            the new success message alert
		 */
		void setSuccessMessageAlert(SuccessMessageAlert successMessageAlert);
		
		/**
		/**
		 * Gets the success message alert definition.
		 *
		 * @return the success message alert definition
		 */
		MessageAlert getErrorMessageAlert();
		
		/**
		 * Gets the error message alert definition.
		 *
		 * @param errorMessageAlert the new error message alert
		 * @return the error message alert definition
		 */
		void setErrorMessageAlert(ErrorMessageAlert errorMessageAlert);
		
		/**
		 * Gets the success message alert parameter.
		 *
		 * @return the success message alert parameter
		 */
		WarningConfirmationMessageAlert getWarningConfirmationMessageAlert();
		
		/**
		 * Sets the success message alert.
		 *
		 * @param warningMessageAlert the new warning confirmation message alert
		 */
		void setWarningConfirmationMessageAlert(WarningConfirmationMessageAlert warningMessageAlert);
		
		/**
		 * Builds the cql file view.
		 */
		void buildCQLFileView();
		
		/**
		 * Sets the checks if is page dirty.
		 *
		 * @param isPageDirty the new checks if is page dirty
		 */
		void setIsPageDirty(Boolean isPageDirty);
		
		/**
		 * Gets the checks if is page dirty.
		 *
		 * @return the checks if is page dirty
		 */
		Boolean getIsPageDirty();
		
		/**
		 * Sets the checks if is double click.
		 *
		 * @param isDoubleClick the new checks if is double click
		 */
		void setIsDoubleClick(Boolean isDoubleClick);
		
		/**
		 * Checks if is double click.
		 *
		 * @return the boolean
		 */
		Boolean isDoubleClick();
		
		/**
		 * Sets the checks if is nav bar click.
		 *
		 * @param isDoubleClick the new checks if is nav bar click
		 */
		void setIsNavBarClick(Boolean isDoubleClick);
		
		/**
		 * Checks if is nav bar click.
		 *
		 * @return the boolean
		 */
		Boolean isNavBarClick();
		
		
		/**
		 * Update suggest func oracle.
		 */
		void updateSuggestFuncOracle();
		
		/**
		 * Clear and add functions names to list box.
		 */
		void clearAndAddFunctionsNamesToListBox();
		
		/**
		 * Gets the function collapse.
		 *
		 * @return the function collapse
		 */
		PanelCollapse getFunctionCollapse();
		
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
		 * Update function map.
		 */
		void updateFunctionMap();
		
		/**
		 * Gets the current selected function obj id.
		 *
		 * @return the current selected function obj id
		 */
		String getCurrentSelectedFunctionObjId();
		
		/**
		 * Gets the function map.
		 *
		 * @return the function map
		 */
		Map<String, CQLFunctions> getFunctionMap();
		
		/**
		 * Gets the view functions.
		 *
		 * @return the view functions
		 */
		List<CQLFunctions> getViewFunctions();
		
		/**
		 * Sets the view functions.
		 *
		 * @param viewFunctions the new view functions
		 */
		void setViewFunctions(List<CQLFunctions> viewFunctions);
		
		/**
		 * Sets the current selected function obj id.
		 *
		 * @param currentSelectedFunctionObjId the new current selected function obj id
		 */
		void setCurrentSelectedFunctionObjId(String currentSelectedFunctionObjId);
		
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
		 * Gets the observer.
		 *
		 * @return the observer
		 */
		Observer getObserver();
		
		/**
		 * Sets the observer.
		 *
		 * @param observer the new observer
		 */
		void setObserver(Observer observer);
		
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
		 * @param functionArgumentList the new function argument list
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
		 * @param argumentList the argument list
		 */
		void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList);
		
		/**
		 * Gets the available qds attribute list.
		 *
		 * @return the available qds attribute list
		 */
		List<QDSAttributes> getAvailableQDSAttributeList();
		
		/**
		 * Sets the available qds attribute list.
		 *
		 * @param availableQDSAttributeList the new available qds attribute list
		 */
		void setAvailableQDSAttributeList(List<QDSAttributes> availableQDSAttributeList);
		
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
		 * @param functionArgNameMap the function arg name map
		 */
		void setFunctionArgNameMap(HashMap<String, CQLFunctionArgument> functionArgNameMap);
		
		/**
		 * Gets the func name list box.
		 *
		 * @return the func name list box
		 */
		org.gwtbootstrap3.client.ui.ListBox getFuncNameListBox();
		
		/**
		 * Gets the warning confirmation yes button.
		 *
		 * @return the warning confirmation yes button
		 */
		Button getWarningConfirmationYesButton();
		
		/**
		 * Gets the warning confirmation no button.
		 *
		 * @return the warning confirmation no button
		 */
		Button getWarningConfirmationNoButton();
		
		/**
		 * Gets the applied qdm list.
		 *
		 * @return the applied qdm list
		 */
		List<CQLQualityDataSetDTO> getAppliedQdmList();
		
		/**
		 * Sets the applied qdm list.
		 *
		 * @param appliedQdmList the new applied qdm list
		 */
		void setAppliedQdmList(List<CQLQualityDataSetDTO> appliedQdmList);
		
		/**
		 * Reset message display.
		 */
		void resetMessageDisplay();
		
		/**
		 * Gets the main h panel.
		 *
		 * @return the main h panel
		 */
		HorizontalPanel getMainHPanel();
		
		/**
		 * Gets the argument text area.
		 *
		 * @return the argument text area
		 */
		TextBox getArgumentTextArea();
		
		/**
		 * Sets the parameter widget read only.
		 *
		 * @param isEditable the new parameter widget read only
		 */
		void setParameterWidgetReadOnly(boolean isEditable);
		
		/**
		 * Show unsaved changes warning.
		 */
		void showUnsavedChangesWarning();
		
		/**
		 * Sets the next clicked menu.
		 *
		 * @param nextClickedMenu the new next clicked menu
		 */
		void setNextClickedMenu(String nextClickedMenu);
		
		/**
		 * Gets the next clicked menu.
		 *
		 * @return the next clicked menu
		 */
		Object getNextClickedMenu();
		
		/**
		 * Gets the global warning confirmation yes button.
		 *
		 * @return the global warning confirmation yes button
		 */
		Button getGlobalWarningConfirmationYesButton();
		
		/**
		 * Gets the global warning confirmation no button.
		 *
		 * @return the global warning confirmation no button
		 */
		Button getGlobalWarningConfirmationNoButton();
		
		/**
		 * Gets the global warning confirmation message alert.
		 *
		 * @return the global warning confirmation message alert
		 */
		WarningConfirmationMessageAlert getGlobalWarningConfirmationMessageAlert();
		
		/**
		 * Sets the global warning confirmation message alert.
		 *
		 * @param globalWarningMessageAlert the new global warning confirmation message alert
		 */
		void setGlobalWarningConfirmationMessageAlert(WarningConfirmationMessageAlert globalWarningMessageAlert);
		
		/**
		 * Show global unsaved changes warning.
		 */
		void showGlobalUnsavedChangesWarning();
		
		/**
		 * Gets the function badge.
		 *
		 * @return the function badge
		 */
		Badge getFunctionBadge();
		
		/**
		 * Builds the info panel.
		 *
		 * @param source the source
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

		SuggestBox getSearchSuggestFuncTextBox();

		SuggestBox getSearchSuggestDefineTextBox();

		SuggestBox getSearchSuggestTextBox();

		void setDefinitionWidgetReadOnly(boolean isEditable);

		DeleteConfirmationMessageAlert getDeleteConfirmationMessageAlert();

		void setDeleteConfirmationMessageAlert(DeleteConfirmationMessageAlert deleteConfirmationMessageAlert);

		void showDeleteConfirmationMessageAlert(String message);

		DeleteConfirmationDialogBox getDeleteConfirmationDialogBox(); 
		
		Button getDeleteConfirmationDialogBoxYesButton();
		
		Button getDeleteConfirmationDialogBoxNoButton(); 
		
		Button getDeleteConfirmationYesButton();

		Button getDeleteConfirmationNoButton();

		void setUsedCQLArtifacts(GetUsedCQLArtifactsResult results);

		AnchorListItem getAppliedQDM();

		void buildAppliedQDM();

		CQLQDMAppliedView getQdmView();

		void setWarningMessageAlert(WarningMessageAlert warningMessageAlert);

		MessageAlert getWarningMessageAlert();

		void setAppliedQdmTableList(List<CQLQualityDataSetDTO> appliedQdmTableList);

		List<CQLQualityDataSetDTO> getAppliedQdmTableList();

	
	}
	
	
	/**
	 * Instantiates a new CQL presenter nav bar with list.
	 *
	 * @param srchDisplay the srch display
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
		searchDisplay.setIsPageDirty(true);
	}
	
	/**
	 * Builds the timing expression pop up.
	 */
	/*private void buildTimingExpressionPopUp() {
		searchDisplay.resetMessageDisplay();
		InsertTimingExpressionIntoAceEditor.showTimingExpressionDialogBox(searchDisplay, currentSection);
		searchDisplay.setIsPageDirty(true);
	}*/
	
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
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					addAndModifyDefintions();
				}
			}
		});
		
		searchDisplay.getAddParameterButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					addAndModifyParameters();
				}
			}
			
		});
		
		searchDisplay.getSaveFunctionButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					addAndModifyFunction();
				}
				
			}
		});
		
		searchDisplay.getEraseDefineButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.setIsDoubleClick(false);
				searchDisplay.setIsNavBarClick(false);
				if (searchDisplay.getIsPageDirty()) {
					searchDisplay.showUnsavedChangesWarning();
				} else {
					clearDefinition();
				}
			}
		});
		
		searchDisplay.getEraseFunctionButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.setIsDoubleClick(false);
				searchDisplay.setIsNavBarClick(false);
				if (searchDisplay.getIsPageDirty()) {
					searchDisplay.showUnsavedChangesWarning();
				} else {
					clearFunction();
				}
			}
		});
		
		searchDisplay.getEraseParameterButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.setIsDoubleClick(false);
				searchDisplay.setIsNavBarClick(false);
				if (searchDisplay.getIsPageDirty()) {
					searchDisplay.showUnsavedChangesWarning();
				} else {
					clearParameter();
				}
			}
		});
		
		searchDisplay.getWarningConfirmationYesButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.setIsPageDirty(false);
				searchDisplay.getWarningConfirmationMessageAlert().clearAlert();
				if (searchDisplay.isDoubleClick()) {
					clickEventOnListboxes();
				} else if (searchDisplay.isNavBarClick()) {
					changeSectionSelection();
				} else {
					clearViewIfDirtyNotSet();
				}
				searchDisplay.setIsNavBarClick(false);
				searchDisplay.setIsDoubleClick(false);
			}
		});
		
		searchDisplay.getWarningConfirmationNoButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getWarningConfirmationMessageAlert().clearAlert();
				// no was selected, don't move anywhere
				if (searchDisplay.isNavBarClick()) {
					unsetActiveMenuItem(nextSection);
				}
				if (currentSection.equals(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
					searchDisplay.getFuncNameListBox().setSelectedIndex(-1);
				} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
					searchDisplay.getParameterNameListBox().setSelectedIndex(-1);
				} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
					searchDisplay.getDefineNameListBox().setSelectedIndex(-1);
				}
			}
		});
		
		searchDisplay.getAddNewArgument().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.hideAceEditorAutoCompletePopUp();
				CQLFunctionArgument addNewFunctionArgument = new CQLFunctionArgument();
				AddFunctionArgumentDialogBox.showArgumentDialogBox(addNewFunctionArgument , false, searchDisplay);
				searchDisplay.setIsPageDirty(true);
			}
		});
		
		
		searchDisplay.getDefineInfoButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.buildInfoPanel((Widget)event.getSource());
				
			}
		});
		
		
		searchDisplay.getParamInfoButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.buildInfoPanel((Widget)event.getSource());
				
			}
		});
		
		searchDisplay.getFuncInfoButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.buildInfoPanel((Widget)event.getSource());
				
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
				MatContext.get().getMeasureService().getUsedCQLArtifacts(MatContext.get().getCurrentMeasureId(), new AsyncCallback<GetUsedCQLArtifactsResult>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());								
					}

					@Override
					public void onSuccess(GetUsedCQLArtifactsResult result) {
						String selectedParamName = searchDisplay.getParameterNameTxtArea().getText();
						if (!result.getUsedCQLParameters().contains(selectedParamName)) {
							searchDisplay.getDeleteConfirmationDialogBox().show(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_PARAMETER());
						} 
					}
					
				});
			}
			
		}); 
		
		searchDisplay.getDefineButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// load most recent used cql artifacts
				MatContext.get().getMeasureService().getUsedCQLArtifacts(MatContext.get().getCurrentMeasureId(), new AsyncCallback<GetUsedCQLArtifactsResult>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());								
					}

					@Override
					public void onSuccess(GetUsedCQLArtifactsResult result) {
						String selectedDefName = searchDisplay.getDefineNameTxtArea().getText();
						if (!result.getUsedCQLDefinitions().contains(selectedDefName)) {
							searchDisplay.getDeleteConfirmationDialogBox().show(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_DEFINITION());
						} 
					}
					
				});
			}
		});
		
		searchDisplay.getFunctionButtonBar().getDeleteButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// load most recent used cql artifacts
				MatContext.get().getMeasureService().getUsedCQLArtifacts(MatContext.get().getCurrentMeasureId(), new AsyncCallback<GetUsedCQLArtifactsResult>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());								
					}

					@Override
					public void onSuccess(GetUsedCQLArtifactsResult result) {
						String selectedFuncName = searchDisplay.getFuncNameTxtArea().getText();
						if(!result.getUsedCQLFunctionss().contains(selectedFuncName)) {
							searchDisplay.getDeleteConfirmationDialogBox().show(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_FUNCTION());
						} 
					}
					
				});
			}
			
		});
		
		searchDisplay.getDeleteConfirmationDialogBoxNoButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getDeleteConfirmationDialogBox().hide();
			}
		}); 
		
		searchDisplay.getDeleteConfirmationDialogBoxYesButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchDisplay.getDefineNameTxtArea().getText() != null) {
					deleteDefinition();
					searchDisplay.getDeleteConfirmationDialogBox().hide(); 
				}
				
				if(searchDisplay.getFuncNameTxtArea().getText() != null) {
					deleteFunction();
					searchDisplay.getDeleteConfirmationDialogBox().hide(); 
				}
				
				if(searchDisplay.getParameterNameTxtArea().getText() != null) {
					deleteParameter();
					searchDisplay.getDeleteConfirmationDialogBox().hide(); 
				}
			}
		}); 
		
		
		addEventHandlerOnAceEditors();
		addEventHandlersOnContextRadioButtons();
		addQDMELmentSearchPanelHandlers();
	}
	
	/**
	 * Event Handlers for Context Radio Buttons.
	 */
	private void addEventHandlersOnContextRadioButtons() {
		searchDisplay.getContextDefinePATRadioBtn().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				searchDisplay.setIsPageDirty(true);
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
				searchDisplay.setIsPageDirty(true);
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
				searchDisplay.setIsPageDirty(true);
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
				searchDisplay.setIsPageDirty(true);
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
					searchDisplay.setIsPageDirty(true);
				}
			}
		});
		
		searchDisplay.getParameterAceEditor().addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getParameterAceEditor().isReadOnly()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.setIsPageDirty(true);
				}
			}
		});
		
		
		searchDisplay.getFunctionBodyAceEditor().addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!searchDisplay.getFunctionBodyAceEditor().isReadOnly()) {
					searchDisplay.resetMessageDisplay();
					searchDisplay.setIsPageDirty(true);
				}
			}
		});
	}
	
	/**
	 * Method to trigger double Click on List Boxes based on section when user
	 * clicks Yes on Warning message (Dirty Check).
	 */
	private void clickEventOnListboxes() {
		
		searchDisplay.setIsDoubleClick(false);
		searchDisplay.setIsNavBarClick(false);
		switch (currentSection) {
			case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
				searchDisplay.getFuncNameListBox().fireEvent(new DoubleClickEvent(){});
			break;
			case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
				searchDisplay.getParameterNameListBox().fireEvent(new DoubleClickEvent(){});
			break;
			case (CQLWorkSpaceConstants.CQL_DEFINE_MENU) :
				searchDisplay.getDefineNameListBox().fireEvent(new DoubleClickEvent(){});
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
			case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
				unsetActiveMenuItem(currentSection);
			searchDisplay.getFunctionLibrary().setActive(false);
			break;
			case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
				unsetActiveMenuItem(currentSection);
			searchDisplay.getParameterLibrary().setActive(false);
			break;
			case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
				unsetActiveMenuItem(currentSection);
			searchDisplay.getDefinitionLibrary().setActive(false);
			break;
			case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
				unsetActiveMenuItem(currentSection);
			searchDisplay.getGeneralInformation().setActive(false);
			break;
			case (CQLWorkSpaceConstants.CQL_VIEW_MENU):
				unsetActiveMenuItem(currentSection);
			searchDisplay.getViewCQL().setActive(false);
			break;
			default:
				break;
		}
		// Set Next Selected Section.
		switch (nextSection) {
			case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
				currentSection = nextSection;
			functionEvent();
			searchDisplay.getFunctionCollapse().getElement().setClassName("panel-collapse collapse in");
			break;
			case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
				currentSection = nextSection;
			parameterEvent();
			searchDisplay.getParamCollapse().getElement().setClassName("panel-collapse collapse in");
			break;
			case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
				currentSection = nextSection;
			definitionEvent();
			searchDisplay.getDefineCollapse().getElement().setClassName("panel-collapse collapse in");
			break;
			case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
				currentSection = nextSection;
			generalInfoEvent();
			break;
			case (CQLWorkSpaceConstants.CQL_VIEW_MENU):
				currentSection = nextSection;
			viewCqlEvent();
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
		searchDisplay.setObserver(new CQLWorkSpaceView.Observer() {
			@Override
			public void onModifyClicked(CQLFunctionArgument result) {
				searchDisplay.setIsPageDirty(true);
				searchDisplay.resetMessageDisplay();
				if (result.getArgumentType().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
					getAttributesForDataType(result);
				} else {
					AddFunctionArgumentDialogBox.showArgumentDialogBox(result, true , searchDisplay);
				}
				
			}
			@Override
			public void onDeleteClicked(CQLFunctionArgument result, int index) {
				searchDisplay.setIsPageDirty(true);
				Iterator<CQLFunctionArgument> iterator = searchDisplay.getFunctionArgumentList().iterator();
				searchDisplay.getFunctionArgNameMap().remove(result.getArgumentName().toLowerCase());
				while (iterator.hasNext()) {
					CQLFunctionArgument cqlFunArgument = iterator.next();
					if (cqlFunArgument.getId().equals(result.getId())) {
						
						iterator.remove();
						searchDisplay.createAddArgumentViewForFunctions(searchDisplay.getFunctionArgumentList());
						break;
					}
				}
			}
			
		});
	}
	
	/**
	 * Get Attributed for Selected Function Argument - QDM Data Type from db.
	 *
	 * @param functionArg - CQLFunctionArgument.
	 * @return the attributes for data type
	 */
	private void getAttributesForDataType(final CQLFunctionArgument functionArg){
		attributeService.getAllAttributesByDataType(functionArg.getQdmDataType(),
				new AsyncCallback<List<QDSAttributes>>() {
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				System.out
				.println("Error retrieving data type attributes. "
						+ caught.getMessage());
				
			}
			
			@Override
			public void onSuccess(List<QDSAttributes> result) {
				searchDisplay.setAvailableQDSAttributeList(result);
				AddFunctionArgumentDialogBox.showArgumentDialogBox(functionArg, true, searchDisplay);
				
			}
			
		});
	}
	
	/**
	 * This method Clears parameter view on Erase Button click when isPageDirty is not set.
	 */
	private void clearParameter() {
		searchDisplay.setCurrentSelectedParamerterObjId(null);
		searchDisplay.getParameterAceEditor().clearAnnotations();
		searchDisplay.setIsPageDirty(false);
		if ((searchDisplay.getParameterAceEditor().getText() != null)) {
			searchDisplay.getParameterAceEditor().setText("");
		}
		if ((searchDisplay.getParameterNameTxtArea() != null)) {
			searchDisplay.getParameterNameTxtArea().setText("");
		}
		
		if (MatContext.get().getMeasureLockService()
				.checkForEditPermission()) {
			searchDisplay.setParameterWidgetReadOnly(MatContext.get().getMeasureLockService()
					.checkForEditPermission());
		}
		//Below lines are to clear search suggestion textbox and listbox selection after erase.
		searchDisplay.getSearchSuggestTextBox().setText("");
		if(searchDisplay.getParameterNameListBox().getSelectedIndex() >= 0){
			searchDisplay.getParameterNameListBox().setItemSelected(searchDisplay.getParameterNameListBox().getSelectedIndex(), false);
		}	
		
		searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
	}
	
	/**
	 * This method Clears Definition view on Erase Button click when isPageDirty is not set.
	 */
	private void clearDefinition() {
		searchDisplay.setCurrentSelectedDefinitionObjId(null);
		searchDisplay.getDefineAceEditor().clearAnnotations();
		searchDisplay.setIsPageDirty(false);
		if ((searchDisplay.getDefineAceEditor().getText() != null)) {
			searchDisplay.getDefineAceEditor().setText("");
		}
		if ((searchDisplay.getDefineNameTxtArea() != null)) {
			searchDisplay.getDefineNameTxtArea().setText("");
		}
		//Below lines are to clear search suggestion textbox and listbox selection after erase.
		searchDisplay.getSearchSuggestDefineTextBox().setText("");
		if(searchDisplay.getDefineNameListBox().getSelectedIndex() >= 0){
			searchDisplay.getDefineNameListBox().setItemSelected(searchDisplay.getDefineNameListBox().getSelectedIndex(), false);
		}
		
		//Functionality to reset the disabled features for supplemental data definitions when erased.
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
	 * This method Clears Function view on Erase Button click when isPageDirty is not set.
	 */
	private void clearFunction() {
		searchDisplay.setCurrentSelectedFunctionObjId(null);
		searchDisplay.getFunctionArgumentList().clear();
		searchDisplay.getFunctionArgNameMap().clear();
		searchDisplay.getFunctionBodyAceEditor().clearAnnotations();
		searchDisplay.createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>());
		searchDisplay.setIsPageDirty(false);
		if ((searchDisplay.getFunctionBodyAceEditor().getText() != null)) {
			searchDisplay.getFunctionBodyAceEditor().setText("");
		}
		if ((searchDisplay.getFuncNameTxtArea() != null)) {
			searchDisplay.getFuncNameTxtArea().setText("");
		}
	//Below lines are to clear search suggestion textbox and listbox selection after erase.
		searchDisplay.getSearchSuggestFuncTextBox().setText("");
		if(searchDisplay.getFuncNameListBox().getSelectedIndex() >= 0){
			searchDisplay.getFuncNameListBox().setItemSelected(searchDisplay.getFuncNameListBox().getSelectedIndex(), false);
		}
		searchDisplay.getContextFuncPATRadioBtn().setValue(true);
		searchDisplay.getContextFuncPOPRadioBtn().setValue(false);
		searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
	}
	
	
	/**
	 * Adds  and modify function.
	 */
	protected void addAndModifyFunction() {
		searchDisplay.resetMessageDisplay();
		final String functionName = searchDisplay.getFuncNameTxtArea().getText();
		String functionBody = searchDisplay.getFunctionBodyAceEditor().getText();
		String funcContext = "";
		if(searchDisplay.getContextFuncPATRadioBtn().getValue()){
			funcContext = "Patient";
		} else {
			funcContext = "Population";
		}
		if (!functionName.isEmpty()) {
			if(!validator.validateForSpecialChar(functionName.trim())) {

				CQLFunctions function = new CQLFunctions();
				function.setFunctionLogic(functionBody);
				function.setFunctionName(functionName);
				function.setArgumentList(searchDisplay.getFunctionArgumentList());
				function.setContext(funcContext);
				if(searchDisplay.getCurrentSelectedFunctionObjId() != null){
					CQLFunctions toBeModifiedParamObj = searchDisplay.getFunctionMap().get(searchDisplay.getCurrentSelectedFunctionObjId());
					MatContext.get().getMeasureService().saveAndModifyFunctions(MatContext.get().getCurrentMeasureId(), toBeModifiedParamObj, function,
							searchDisplay.getViewFunctions(), new AsyncCallback<SaveUpdateCQLResult>() {
						
						@Override
						public void onFailure(Throwable caught) {
							searchDisplay.getErrorMessageAlert().createAlert(
									MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
						
						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							if (result.isSuccess()) {
								
									searchDisplay.setViewFunctions(result.getCqlModel().getCqlFunctions());
									MatContext.get().setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
									searchDisplay.clearAndAddFunctionsNamesToListBox();
									searchDisplay.updateFunctionMap();
									searchDisplay.getErrorMessageAlert().clearAlert();
									searchDisplay.getSuccessMessageAlert().setVisible(true);
									
									searchDisplay.getFuncNameTxtArea().setText(result.getFunction().getFunctionName());
									searchDisplay.getFunctionBodyAceEditor().setText(result.getFunction().getFunctionLogic());
									searchDisplay.setIsPageDirty(false);
									searchDisplay.getFunctionBodyAceEditor().clearAnnotations();
									searchDisplay.getFunctionBodyAceEditor().removeAllMarkers();
									searchDisplay.getFunctionBodyAceEditor().redisplay();
									searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(true);
									
									if(validateCQLArtifact(result, currentSection)){
										/*searchDisplay.getSuccessMessageAlert().add(getMsgPanel(IconType.CHECK_CIRCLE,
												MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_MODIFY_WITH_ERRORS()));
												*/
										searchDisplay.getSuccessMessageAlert().clearAlert();
										searchDisplay.getWarningMessageAlert().createAlert(MatContext.get()
												.getMessageDelegate().getSUCESS_FUNCTION_MODIFY_WITH_ERRORS());

										} else {
											searchDisplay.getSuccessMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getSUCESS_FUNCTION_MODIFY());
									}
									searchDisplay.getFunctionBodyAceEditor().setAnnotations();
									searchDisplay.getFunctionBodyAceEditor().redisplay();
							
							} else if (result.getFailureReason() == 1) {
								searchDisplay.getSuccessMessageAlert().clearAlert();
								searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
								searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
							} else if (result.getFailureReason() == 2) {
								searchDisplay.getSuccessMessageAlert().clearAlert();
								searchDisplay.getErrorMessageAlert()
								.createAlert("Unable to find Node to modify.");
								searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
							}  else if(result.getFailureReason() == 3) {
								searchDisplay.getSuccessMessageAlert().clearAlert();
								searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
								searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
								if(result.getFunction()!=null){
									searchDisplay.createAddArgumentViewForFunctions(
											result.getFunction().getArgumentList());
								}
							}
						}
					});
				} else {
					MatContext.get().getMeasureService().saveAndModifyFunctions(MatContext.get().getCurrentMeasureId(),
							null, function, searchDisplay.getViewFunctions(), new AsyncCallback<SaveUpdateCQLResult>() {
						
						@Override
						public void onFailure(Throwable caught) {
							searchDisplay.getErrorMessageAlert().createAlert(
									MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
						
						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							if (result.isSuccess()) {
								searchDisplay.setIsPageDirty(false);
								searchDisplay.setViewFunctions(result.getCqlModel().getCqlFunctions());
								MatContext.get().setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
								searchDisplay.clearAndAddFunctionsNamesToListBox();
								searchDisplay.updateFunctionMap();
								searchDisplay.getFuncNameTxtArea().setText(result.getFunction().getFunctionName());
								searchDisplay.getFunctionBodyAceEditor().setText(result.getFunction().getFunctionLogic());
								searchDisplay.setCurrentSelectedFunctionObjId(result.getFunction().getId());
								searchDisplay.getErrorMessageAlert().clearAlert();
								searchDisplay.getSuccessMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getSUCCESSFUL_SAVED_CQL_FUNCTIONS());
								searchDisplay.getFunctionBodyAceEditor().clearAnnotations();
								searchDisplay.getFunctionBodyAceEditor().removeAllMarkers();
								searchDisplay.getFunctionBodyAceEditor().redisplay();
								searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(true);
								if(validateCQLArtifact(result, currentSection)){
									searchDisplay.getSuccessMessageAlert().clearAlert();
									searchDisplay.getWarningMessageAlert().createAlert(MatContext.get()
											.getMessageDelegate().getSUCCESSFUL_SAVED_CQL_FUNCTIONS_WITH_ERRORS());
								} else {
									searchDisplay.getSuccessMessageAlert().createAlert(MatContext.get()
											.getMessageDelegate().getSUCCESSFUL_SAVED_CQL_FUNCTIONS());
								}
								searchDisplay.getFunctionBodyAceEditor().setAnnotations();
								searchDisplay.getFunctionBodyAceEditor().redisplay();
							} else if (result.getFailureReason() == 1) {
								searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
								searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
							} else if (result.getFailureReason() == 2) {
								searchDisplay.getErrorMessageAlert().createAlert("Missing Functions Tag.");
								searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
							}  else if(result.getFailureReason() == 3){
								searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
								searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
								if(result.getFunction()!=null){
									searchDisplay.createAddArgumentViewForFunctions(result.getFunction().getArgumentList());
								}
								
							}
						}
						
					});
				}
				
			} else {
				searchDisplay.getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_FUNCTION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
			}
			
			
		} else {
			searchDisplay.getErrorMessageAlert()
			.createAlert(MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_FUNCTION());
			searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
		}
		
	}
	
	
	/**
	 * Adds the and modify parameters.
	 */
	private void addAndModifyParameters() {
		searchDisplay.resetMessageDisplay();
		CQLParameter param = searchDisplay.getParameterMap().get(searchDisplay.getCurrentSelectedParamerterObjId());
		//to check if Default Parameter is Editable
		if((param != null) && param.isReadOnly()){
			return;
		}
		final String parameterName = searchDisplay.getParameterNameTxtArea().getText();
		String parameterLogic = searchDisplay.getParameterAceEditor().getText();
		if (!parameterName.isEmpty()) {
			
			if(!validator.validateForSpecialChar(parameterName.trim())) {
				
				CQLParameter parameter = new CQLParameter();
				parameter.setParameterLogic(parameterLogic);
				parameter.setParameterName(parameterName);
				if(searchDisplay.getCurrentSelectedParamerterObjId() != null){
					CQLParameter toBeModifiedParamObj = searchDisplay.getParameterMap().get(searchDisplay.getCurrentSelectedParamerterObjId());
					MatContext.get().getMeasureService().saveAndModifyParameters(MatContext.get().getCurrentMeasureId(), toBeModifiedParamObj, parameter,
							searchDisplay.getViewParameterList(), new AsyncCallback<SaveUpdateCQLResult>() {
						
						@Override
						public void onFailure(Throwable caught) {
							searchDisplay.getErrorMessageAlert().createAlert(
									MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
						
						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							//searchDisplay.setCurrentSelectedParamerterObjId(null);
							if (result.isSuccess()) {							
									searchDisplay.setViewParameterList(result.getCqlModel().getCqlParameters());
									MatContext.get().setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
									searchDisplay.clearAndAddParameterNamesToListBox();
									searchDisplay.updateParamMap();
									searchDisplay.getErrorMessageAlert().clearAlert();
									searchDisplay.getParameterNameTxtArea().setText(result.getParameter().getParameterName());
									searchDisplay.getParameterAceEditor().setText(result.getParameter().getParameterLogic());
									searchDisplay.setIsPageDirty(false);
									searchDisplay.getParameterAceEditor().clearAnnotations();
									searchDisplay.getParameterAceEditor().removeAllMarkers();
									searchDisplay.getParameterAceEditor().redisplay();
									searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(true);
									if(validateCQLArtifact(result, currentSection)){
										searchDisplay.getWarningMessageAlert().createAlert(
												MatContext.get().getMessageDelegate().getSUCESS_PARAMETER_MODIFY_WITH_ERRORS());
										
									} else {
										searchDisplay.getSuccessMessageAlert().createAlert(
												MatContext.get().getMessageDelegate().getSUCESS_PARAMETER_MODIFY());
										
									}
									searchDisplay.getParameterAceEditor().setAnnotations();
									searchDisplay.getParameterAceEditor().redisplay();
							
							} else if (result.getFailureReason() == 1) {
								searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
								searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
							} else if (result.getFailureReason() == 2) {
								searchDisplay.getErrorMessageAlert()
								.createAlert("Unable to find Node to modify.");
								searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
							}  else if(result.getFailureReason() == 3) {
								searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
								searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
							}
						}
					});
				} else {
					MatContext.get().getMeasureService().saveAndModifyParameters(MatContext.get().getCurrentMeasureId(),
							null, parameter, searchDisplay.getViewParameterList(),
							new AsyncCallback<SaveUpdateCQLResult>() {
						
						@Override
						public void onFailure(Throwable caught) {
							searchDisplay.getErrorMessageAlert().createAlert(
									MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
						
						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							if (result.isSuccess()) {
								searchDisplay.setViewParameterList(result.getCqlModel().getCqlParameters());
								MatContext.get().setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
								searchDisplay.clearAndAddParameterNamesToListBox();
								searchDisplay.updateParamMap();
								searchDisplay.getParameterNameTxtArea().setText(result.getParameter().getParameterName());
								searchDisplay.getParameterAceEditor().setText(result.getParameter().getParameterLogic());
								searchDisplay.setCurrentSelectedParamerterObjId(result.getParameter().getId());
								searchDisplay.getErrorMessageAlert().clearAlert();
								searchDisplay.setIsPageDirty(false);
								searchDisplay.getParameterAceEditor().clearAnnotations();
								searchDisplay.getParameterAceEditor().removeAllMarkers();
								searchDisplay.getParameterAceEditor().redisplay();
								searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(true);
								if(validateCQLArtifact(result, currentSection)){
									searchDisplay.getWarningMessageAlert().createAlert(MatContext.get()
											.getMessageDelegate().getSUCCESSFUL_SAVED_CQL_PARAMETER_WITH_ERRORS());
								} else {
									searchDisplay.getSuccessMessageAlert().createAlert(MatContext.get()
											.getMessageDelegate().getSUCCESSFUL_SAVED_CQL_PARAMETER());
								}
								searchDisplay.getParameterAceEditor().setAnnotations();
								searchDisplay.getParameterAceEditor().redisplay();
							} else if (result.getFailureReason() == 1) {
								searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
								searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
							} else if (result.getFailureReason() == 2) {
								searchDisplay.getErrorMessageAlert().createAlert("Missing Parameters Tag.");
								searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
							}  else if(result.getFailureReason() == 3) {
								searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
								searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
							}
						}
						
					});
				}
				
			} else {
				searchDisplay.getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_PARAMETER_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
			}
			
			
		} else {
			searchDisplay.getErrorMessageAlert()
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
		if(searchDisplay.getContextDefinePATRadioBtn().getValue()){
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}
		if (!definitionName.isEmpty()) {
			
			if(!validator.validateForSpecialChar(definitionName.trim())) {
				
				final CQLDefinition define = new CQLDefinition();
				define.setDefinitionName(definitionName);
				define.setDefinitionLogic(definitionLogic);
				define.setContext(defineContext);
				
				if(searchDisplay.getCurrentSelectedDefinitionObjId() != null){
					CQLDefinition toBeModifiedObj = searchDisplay.getDefinitionMap()
							.get(searchDisplay.getCurrentSelectedDefinitionObjId());
					
					MatContext.get().getMeasureService().saveAndModifyDefinitions(
							MatContext.get().getCurrentMeasureId(), toBeModifiedObj, define,
							searchDisplay.getViewDefinitions(), new AsyncCallback<SaveUpdateCQLResult>() {
								
								@Override
								public void onFailure(Throwable caught) {
									searchDisplay.setCurrentSelectedDefinitionObjId(null);
									searchDisplay.getErrorMessageAlert().createAlert(
											MatContext.get().getMessageDelegate().getGenericErrorMessage());
								}
								
								@Override
								public void onSuccess(SaveUpdateCQLResult result) {
									//	searchDisplay.setCurrentSelectedDefinitionObjId(null);
									if(result.isSuccess()){		
											searchDisplay.setViewDefinitions(result.getCqlModel().getDefinitionList());
											MatContext.get().setDefinitions(getDefinitionList(result.getCqlModel().getDefinitionList()));
											searchDisplay.clearAndAddDefinitionNamesToListBox();
											searchDisplay.updateDefineMap();
											searchDisplay.getErrorMessageAlert().clearAlert();
											searchDisplay.getDefineNameTxtArea()
											.setText(result.getDefinition().getDefinitionName());
											searchDisplay.getDefineAceEditor().setText(result.getDefinition().getDefinitionLogic());
											searchDisplay.setIsPageDirty(false);
											searchDisplay.getDefineAceEditor().clearAnnotations();
											searchDisplay.getDefineAceEditor().removeAllMarkers();
											searchDisplay.getDefineAceEditor().redisplay();
											searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(true);
											if(validateCQLArtifact(result, currentSection)){
												searchDisplay.getWarningMessageAlert().createAlert(
														MatContext.get().getMessageDelegate().getSUCESS_DEFINITION_MODIFY_WITH_ERRORS());
											} else {
												searchDisplay.getSuccessMessageAlert().createAlert(
														MatContext.get().getMessageDelegate().getSUCESS_DEFINITION_MODIFY());
											}
											searchDisplay.getDefineAceEditor().setAnnotations();
											searchDisplay.getDefineAceEditor().redisplay();
										
									} else {
										if(result.getFailureReason() ==1){
											searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
											searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
										} else if(result.getFailureReason() == 2){
											searchDisplay.getErrorMessageAlert()
											.createAlert("Unable to find Node to modify.");
											searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
										} else if(result.getFailureReason() == 3) {
											searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
													.getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
											searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
										}
									}
									/*searchDisplay.getDefineNameTxtArea().clear();
							searchDisplay.getDefineAceEditor().setText("");;*/
								}
							});
					
				} else {
					
					MatContext.get().getMeasureService().saveAndModifyDefinitions(MatContext.get().getCurrentMeasureId(), null, define,
							searchDisplay.getViewDefinitions(), new AsyncCallback<SaveUpdateCQLResult>() {
						
						@Override
						public void onFailure(Throwable caught) {
							searchDisplay.getErrorMessageAlert().createAlert(
									MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
						
						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							if (result.isSuccess()) {
								
								searchDisplay.setViewDefinitions(result.getCqlModel().getDefinitionList());
								MatContext.get().setDefinitions(getDefinitionList(result.getCqlModel().getDefinitionList()));
								searchDisplay.clearAndAddDefinitionNamesToListBox();
								searchDisplay.updateDefineMap();
								searchDisplay.setCurrentSelectedDefinitionObjId(result.getDefinition().getId());
								searchDisplay.getErrorMessageAlert().clearAlert();
								searchDisplay.getDefineNameTxtArea().setText(result.getDefinition().getDefinitionName());
								searchDisplay.getDefineAceEditor().setText(result.getDefinition().getDefinitionLogic());
								searchDisplay.setIsPageDirty(false);
								searchDisplay.getDefineAceEditor().clearAnnotations();
								searchDisplay.getDefineAceEditor().removeAllMarkers();
								searchDisplay.getDefineAceEditor().redisplay();
								searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(true);
								if(validateCQLArtifact(result, currentSection)){
									searchDisplay.getWarningMessageAlert().createAlert(MatContext.get()
											.getMessageDelegate().getSUCCESSFUL_SAVED_CQL_DEFINITION_WITH_ERRORS());
								} else {
									searchDisplay.getSuccessMessageAlert().createAlert(MatContext.get()
											.getMessageDelegate().getSUCCESSFUL_SAVED_CQL_DEFINITION());
								}
								
								searchDisplay.getDefineAceEditor().setAnnotations();
								searchDisplay.getDefineAceEditor().redisplay();
							} else {
								
								if (result.getFailureReason() == 1) {
									searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
											.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
									searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
								} else if(result.getFailureReason() == 2){
									searchDisplay.getErrorMessageAlert()
									.createAlert("Missing Definitions Tag.");
									searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
								} else if(result.getFailureReason() == 3) {
									searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
											.getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
									searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
								}
							}
							/*searchDisplay.getDefineNameTxtArea().clear();
							searchDisplay.getDefineAceEditor().setText("");;*/
							
						}
					});
					
				}
			} else {
				searchDisplay.getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
			}
			
			
			
		} else {
			searchDisplay.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_DEFINITION());
			searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		searchDisplay.setCurrentSelectedDefinitionObjId(null);
		searchDisplay.setCurrentSelectedParamerterObjId(null);
		searchDisplay.setCurrentSelectedFunctionObjId(null);
		searchDisplay.getFunctionArgNameMap().clear();
		searchDisplay.setIsPageDirty(false);
		searchDisplay.resetMessageDisplay();
		searchDisplay.getParamCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getDefineCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getFunctionCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getViewCQL().getElement().setClassName("panel-collapse collapse");
		if (searchDisplay.getFunctionArgumentList().size() > 0) {
			searchDisplay.getFunctionArgumentList().clear();
		}
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		searchDisplay.getMessagePanel().clear();
		panel.clear();
		searchDisplay.getMainPanel().clear();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		getCQLData();
		searchDisplay.buildView();
		addLeftNavEventHandler();
		searchDisplay.resetMessageDisplay();
		MatContext.get().getAllCqlKeywordsAndQDMDatatypesForCQLWorkSpace();
		MatContext.get().getAllUnits();
		//getAppliedQDMList(true);
		loadElementLookUpNode();
		if (searchDisplay.getFunctionArgumentList().size() > 0) {
			searchDisplay.getFunctionArgumentList().clear();
		}
		panel.add(searchDisplay.getMainHPanel());
	}
	
	
	/**
	 * Sets the definition widget read only.
	 *
	 * @param isEditable the new definition widget read only
	 */
	private void setDefinitionWidgetReadOnly(boolean isEditable) {
		
		searchDisplay.getDefineNameTxtArea().setEnabled(isEditable);
		searchDisplay.getDefineAceEditor().setReadOnly(!isEditable);
		searchDisplay.getDefineButtonBar().setEnabled(isEditable);
		searchDisplay.getContextDefinePATRadioBtn().setEnabled(isEditable);
		searchDisplay.getContextDefinePOPRadioBtn().setEnabled(isEditable);
		searchDisplay.getDefineButtonBar().getDeleteButton().setTitle("Delete");
	}
	
	
	/**
	 * Sets the function widget read only.
	 *
	 * @param isEditable the new function widget read only
	 */
	private void setFunctionWidgetReadOnly(boolean isEditable) {
		
		searchDisplay.getFuncNameTxtArea().setEnabled(isEditable);
		searchDisplay.getFunctionBodyAceEditor().setReadOnly(!isEditable);
		searchDisplay.getFunctionButtonBar().setEnabled(isEditable);
		searchDisplay.getAddNewArgument().setEnabled(isEditable);
		searchDisplay.getContextFuncPATRadioBtn().setEnabled(isEditable);
		searchDisplay.getContextFuncPOPRadioBtn().setEnabled(isEditable);
		searchDisplay.getFunctionButtonBar().getDeleteButton().setTitle("Delete");
		
	}
	
	
	
	/**
	 * Gets the CQL data.
	 *
	 * @return the CQL data
	 */
	private void getCQLData() {
		MatContext.get().getMeasureService().getCQLData(MatContext.get()
				.getCurrentMeasureId(), new AsyncCallback<SaveUpdateCQLResult>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				
			}
			
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if (result.getCqlModel() != null) {
					
					List<CQLQualityDataSetDTO> appliedAllValueSetList = new ArrayList<CQLQualityDataSetDTO>();
					List<CQLQualityDataSetDTO> appliedValueSetTableList = new ArrayList<CQLQualityDataSetDTO>();
					List<CQLQualityDataSetDTO> appliedValueSetListInXML = result.getCqlModel().getAllValueSetList();
					for (CQLQualityDataSetDTO dto : appliedValueSetListInXML) {
						if(dto.isSuppDataElement())
							continue;
						if (!dto.getOid().equalsIgnoreCase(MEASUREMENT_PERIOD_OID)) {
							appliedAllValueSetList.add(dto);
						} else {
							System.out.print("Skipping Measurement Period from QDM List.");
						}
					}
					searchDisplay.setAppliedQdmList(appliedAllValueSetList);
					
					
					for (CQLQualityDataSetDTO dto : result.getCqlModel().getValueSetList()) {
						if(dto.isSuppDataElement())
							continue;
						if (!dto.getOid().equalsIgnoreCase(MEASUREMENT_PERIOD_OID)) {
							appliedValueSetTableList.add(dto);
						} else {
							System.out.print("Skipping Measurement Period from QDM List.");
						}
					}
					searchDisplay.setAppliedQdmTableList(appliedValueSetTableList);
					
					if ((result.getCqlModel().getDefinitionList() != null) &&
							(result.getCqlModel().getDefinitionList().size() > 0)) {
						searchDisplay.setViewDefinitions(result.getCqlModel().getDefinitionList());
						searchDisplay.clearAndAddDefinitionNamesToListBox();
						searchDisplay.updateDefineMap();
						MatContext.get().setDefinitions(getDefinitionList(result.getCqlModel().getDefinitionList()));
					} else {
						searchDisplay.getDefineBadge().setText("00");
					}
					if ((result.getCqlModel().getCqlParameters() != null) &&
							(result.getCqlModel().getCqlParameters().size() > 0)) {
						searchDisplay.setViewParameterList(result.getCqlModel().getCqlParameters());
						searchDisplay.clearAndAddParameterNamesToListBox();
						searchDisplay.updateParamMap();
						MatContext.get().setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
					} else {
						searchDisplay.getParamBadge().setText("00");
					}
					if ((result.getCqlModel().getCqlFunctions() != null) &&
							(result.getCqlModel().getCqlFunctions().size() > 0)) {
						searchDisplay.setViewFunctions(result.getCqlModel().getCqlFunctions());
						searchDisplay.clearAndAddFunctionsNamesToListBox();
						searchDisplay.updateFunctionMap();
						MatContext.get().setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
					} else {
						searchDisplay.getFunctionBadge().setText("00");
					} 
					
					
				}
				
			}
		});
	}
	
	/**
	 * Adding handlers for Anchor Items.
	 */
	private void addLeftNavEventHandler() {
		
		searchDisplay.getGeneralInformation().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.hideAceEditorAutoCompletePopUp();
				generalInfoEvent();
			}
		});
		
		searchDisplay.getAppliedQDM().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.hideAceEditorAutoCompletePopUp();
				appliedQDMEvent();
			}
		});
		
		
		searchDisplay.getParameterLibrary().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.setIsNavBarClick(true);
				searchDisplay.setIsDoubleClick(false);
				searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
					searchDisplay.showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					parameterEvent();
				}
				
			}
		});
		
		searchDisplay.getDefinitionLibrary().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.setIsNavBarClick(true);
				searchDisplay.setIsDoubleClick(false);
				searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
					searchDisplay.showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					definitionEvent();
				}
			}
		});
		
		searchDisplay.getFunctionLibrary().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.setIsNavBarClick(true);
				searchDisplay.setIsDoubleClick(false);
				searchDisplay.hideAceEditorAutoCompletePopUp();
				if (searchDisplay.getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
					searchDisplay.showUnsavedChangesWarning();
					event.stopPropagation();
				} else {
					functionEvent();
				}
			}
		});
		
		searchDisplay.getViewCQL().addClickHandler(new ClickHandler() {
			
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
		searchDisplay.setIsNavBarClick(true);
		searchDisplay.setIsDoubleClick(false);
		if (searchDisplay.getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			searchDisplay.showUnsavedChangesWarning();
			
		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getGeneralInformation().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
			searchDisplay.buildGeneralInformation();
		}
		
	}
	
	private void appliedQDMEvent() {
		searchDisplay.setIsNavBarClick(true);
		searchDisplay.setIsDoubleClick(false);
		if (searchDisplay.getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			searchDisplay.showUnsavedChangesWarning();
			
		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getAppliedQDM().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_APPLIED_QDM;
			searchDisplay.buildAppliedQDM();
			searchDisplay.getQdmView().buildAppliedQDMCellTable(searchDisplay.getAppliedQdmTableList(), MatContext.get().getMeasureLockService()
					.checkForEditPermission());
			searchDisplay.getQdmView().setWidgetsReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
		}
		
	}
	/**
	 * Build View for Parameter when Parameter AnchorList item is clicked.
	 */
	private void parameterEvent() {
		unsetActiveMenuItem(currentSection);
		
		
		searchDisplay.getParameterLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
		searchDisplay.buildParameterLibraryView();
		
		searchDisplay.setParameterWidgetReadOnly(MatContext.get().getMeasureLockService()
				.checkForEditPermission());
		
		searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getParameterButtonBar().getDeleteButton().setTitle("Delete");
	}
	/**
	 * Build View for Definition when Definition AnchorList item is clicked.
	 */
	private void definitionEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.setIsNavBarClick(true);
		searchDisplay.setIsDoubleClick(false);
		
		searchDisplay.getDefinitionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
		searchDisplay.buildDefinitionLibraryView();
		setDefinitionWidgetReadOnly(MatContext.get().getMeasureLockService()
				.checkForEditPermission());
		
		searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getDefineButtonBar().getDeleteButton().setTitle("Delete");
	}
	/**
	 * Build View for Function when Funtion AnchorList item is clicked.
	 */
	private void functionEvent() {
		searchDisplay.setIsNavBarClick(true);
		searchDisplay.setIsDoubleClick(false);
		unsetActiveMenuItem(currentSection);
		searchDisplay.getFunctionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
		searchDisplay.buildFunctionLibraryView();
		setFunctionWidgetReadOnly(MatContext.get().getMeasureLockService()
				.checkForEditPermission());
		
		searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
		searchDisplay.getFunctionButtonBar().getDeleteButton().setTitle("Delete");

	}
	/**
	 * Build View for View Cql when View Cql AnchorList item is clicked.
	 */
	private void viewCqlEvent() {
		searchDisplay.setIsNavBarClick(true);
		searchDisplay.setIsDoubleClick(false);
		if (searchDisplay.getIsPageDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			searchDisplay.showUnsavedChangesWarning();
		} else {
			unsetActiveMenuItem(currentSection);
			searchDisplay.getViewCQL().setActive(true);
			currentSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
			searchDisplay.buildCQLFileView();
			buildCQLView();
		}
		
	}
	
	/**
	 * Method to unset Anchor List Item selection for previous selection and set for new selections.
	 *
	 * @param menuClickedBefore the menu clicked before
	 */
	private void unsetActiveMenuItem(String menuClickedBefore) {
		if (!searchDisplay.getIsPageDirty()) {
			searchDisplay.resetMessageDisplay();
			if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_GENERAL_MENU)) {
				searchDisplay.getGeneralInformation().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
				searchDisplay.getParameterLibrary().setActive(false);
				searchDisplay.getParameterNameListBox().setSelectedIndex(-1);
				if (searchDisplay.getParamCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getParamCollapse().getElement().setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
				searchDisplay.getDefinitionLibrary().setActive(false);
				searchDisplay.getDefineNameListBox().setSelectedIndex(-1);
				if (searchDisplay.getDefineCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getDefineCollapse().getElement().setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)) {
				searchDisplay.getFunctionLibrary().setActive(false);
				searchDisplay.getFuncNameListBox().setSelectedIndex(-1);
				if (searchDisplay.getFunctionCollapse().getElement().getClassName()
						.equalsIgnoreCase("panel-collapse collapse in")) {
					searchDisplay.getFunctionCollapse().getElement().setClassName("panel-collapse collapse");
				}
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEW_MENU)) {
				searchDisplay.getViewCQL().setActive(false);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_APPLIED_QDM)) {
				searchDisplay.getAppliedQDM().setActive(false);
			}
		}
	}
	/**
	 * Method to build View for Anchor List item View CQL.
	 */
	private void buildCQLView() {
		searchDisplay.getCqlAceEditor().setText("");
		MatContext.get().getMeasureService().getCQLFileData(MatContext.get()
				.getCurrentMeasureId(), new AsyncCallback<SaveUpdateCQLResult>() {
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if (result.isSuccess()) {
					if ((result.getCqlString() != null)
							&& !result.getCqlString().isEmpty()) {
						validateViewCQLFile(result.getCqlString());
						//searchDisplay.getCqlAceEditor().setText(result.getCqlString());
					}
					
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
		
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
	 * Gets the msg panel.
	 *
	 * @param iconType the icon type
	 * @param message the message
	 * @return the msg panel
	 *//*
	private HTML getMsgPanel(IconType iconType, String message) {
		Icon checkIcon = new Icon(iconType);
		HTML msgHtml = new HTML(checkIcon + " <b>" + message + "</b>");
		return msgHtml;
	}
	*/
	
		
	/**
	 * Gets the definition list.
	 *
	 * @param definitionList the definition list
	 * @return the definition list
	 */
	private List<String> getDefinitionList(
			List<CQLDefinition> definitionList) {
		
		List<String> defineList = new ArrayList<String>();
		
		for(int i=0; i<definitionList.size(); i++){
			defineList.add(definitionList.get(i).getDefinitionName());
		}
		return defineList;
	}
	
	/**
	 * Gets the paramater list.
	 *
	 * @param parameterList the parameter list
	 * @return the paramater list
	 */
	private List<String> getParamaterList(
			List<CQLParameter> parameterList) {
		
		List<String> paramList = new ArrayList<String>();
		
		for(int i=0; i<parameterList.size(); i++){
			paramList.add(parameterList.get(i).getParameterName());
		}
		return paramList;
	}
	
	/**
	 * Gets the function list.
	 *
	 * @param functionList the function list
	 * @return the function list
	 */
	private List<String> getFunctionList(
			List<CQLFunctions> functionList) {
		
		List<String> funcList = new ArrayList<String>();
		
		for(int i=0; i<functionList.size(); i++){
			funcList.add(functionList.get(i).getFunctionName());
		}
		return funcList;
	}
	
	
	
	/**
	 * returns the searchDisplay.
	 * @return ViewDisplay.
	 */
	public ViewDisplay getSearchDisplay() {
		return searchDisplay;
	}
	
	/**
	 * Validate CQL file on View CQL and show warning or success message accordingly.
	 * @param cqlText
	 */
	private void validateViewCQLFile(final String cqlText){
		MatContext.get().getMeasureService().parseCQLStringForError(cqlText, new AsyncCallback<SaveUpdateCQLResult>(){

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageAlert().createAlert(
						MatContext.get().getMessageDelegate().getGenericErrorMessage());
				searchDisplay.getCqlAceEditor().setText(cqlText);
			}

			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				searchDisplay.getCqlAceEditor().clearAnnotations();
				searchDisplay.getCqlAceEditor().removeAllMarkers();
				searchDisplay.getCqlAceEditor().redisplay();
				searchDisplay.getSuccessMessageAlert().clear();
				searchDisplay.getWarningMessageAlert().clear();
				searchDisplay.getWarningConfirmationMessageAlert().clear();
				
				if(!result.getCqlErrors().isEmpty()){
					searchDisplay.getWarningConfirmationMessageAlert().setVisible(true);
					searchDisplay.getWarningConfirmationMessageAlert().setText(MatContext.get().getMessageDelegate().getVIEW_CQL_ERROR_MESSAGE());
					for(CQLErrors error : result.getCqlErrors()){
							String errorMessage = new String();
							errorMessage = errorMessage.concat("Error in line : "+ error.getErrorInLine() + " at Offset :" + error.getErrorAtOffeset());
							int line = error.getErrorInLine();
							int column = error.getErrorAtOffeset();
							searchDisplay.getCqlAceEditor().addAnnotation(line - 1, column, error.getErrorMessage(), AceAnnotationType.WARNING);
					}
					searchDisplay.getCqlAceEditor().setText(cqlText);
					searchDisplay.getCqlAceEditor().setAnnotations();
					searchDisplay.getCqlAceEditor().redisplay();
				} else {
					searchDisplay.getSuccessMessageAlert().setVisible(true);
					searchDisplay.getSuccessMessageAlert().setText(MatContext.get().getMessageDelegate().getVIEW_CQL_NO_ERRORS_MESSAGE());
					searchDisplay.getCqlAceEditor().setText(cqlText);
				}
				
			}
			
		});

	}
	
	private boolean validateCQLArtifact(SaveUpdateCQLResult result ,String currentSect){
		boolean isInvalid = false;
		if(!result.getCqlErrors().isEmpty()){
			final AceEditor editor = getAceEditorBasedOnCurrentSection(searchDisplay, currentSection);
			for(CQLErrors error : result.getCqlErrors()){
				int startLine = error.getStartErrorInLine(); 
				int startColumn = error.getStartErrorAtOffset(); 
				editor.addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.WARNING);
				if(!isInvalid) {
					isInvalid = true;
				}
			}
		}
		
		return isInvalid;
	}
	
	/**
	 * Validate user defined input.
	 * In this functionality we are disabling all the fields in Search Panel
	 * except Name and DataType Selection List Box which are required to create new
	 * UserDefined QDM Element.
	 */
	private void validateUserDefinedInput(){
		if(searchDisplay.getQdmView().getUserDefinedInput().getValue().length()>0){
			isUserDefined = true;
			searchDisplay.getQdmView().getOIDInput().setEnabled(true);
			searchDisplay.getQdmView().getUserDefinedInput().setTitle(searchDisplay.getQdmView().getUserDefinedInput().getValue());
			searchDisplay.getQdmView().getQDMExpIdentifierListBox().setEnabled(false);
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
	 * Validate oid input.
	 * depending on the OID input we are disabling and
	 * enabling the fields in Search Panel
	 */
	private void validateOIDInput(){
		if (searchDisplay.getQdmView().getOIDInput().getValue().length()>0) {
			isUserDefined = false;
			searchDisplay.getQdmView().getUserDefinedInput().setEnabled(false);
			searchDisplay.getQdmView().getSaveButton().setEnabled(false);
			searchDisplay.getQdmView().getRetrieveFromVSACButton().setEnabled(true);
		} else if(searchDisplay.getQdmView().getUserDefinedInput().getValue().length()>0){
			isUserDefined = true;
			searchDisplay.getQdmView().getQDMExpIdentifierListBox().clear();
			searchDisplay.getQdmView().getVersionListBox().clear();
			searchDisplay.getQdmView().getUserDefinedInput().setEnabled(true);
			searchDisplay.getQdmView().getSaveButton().setEnabled(true);
			
		} else {
			searchDisplay.getQdmView().getUserDefinedInput().setEnabled(true);
		}
	}
	
	private static AceEditor getAceEditorBasedOnCurrentSection(ViewDisplay searchDisplay, String currentSection) {
		AceEditor editor = null;
		switch(currentSection) {
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
				/*editor = searchDisplay.getDefineAceEditor();*/
				break;
		}
		return editor;
	}
	
	/*private void removeMarkers(AceEditor aceEditor, int row){
		
	}*/
	
	protected void deleteDefinition() {
		searchDisplay.resetMessageDisplay();
		final String definitionName = searchDisplay.getDefineNameTxtArea().getText();
		String definitionLogic = searchDisplay.getDefineAceEditor().getText();
		String defineContext = "";
		if(searchDisplay.getContextDefinePATRadioBtn().getValue()){
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}
		if (!definitionName.isEmpty()) {
			
			if(!validator.validateForSpecialChar(definitionName.trim())) {
				
				final CQLDefinition define = new CQLDefinition();
				define.setDefinitionName(definitionName);
				define.setDefinitionLogic(definitionLogic);
				define.setContext(defineContext);
				
				if(searchDisplay.getCurrentSelectedDefinitionObjId() != null){
					CQLDefinition toBeModifiedObj = searchDisplay.getDefinitionMap()
							.get(searchDisplay.getCurrentSelectedDefinitionObjId());
					
					MatContext.get().getMeasureService().deleteDefinition(MatContext.get().getCurrentMeasureId(), toBeModifiedObj, define,
							searchDisplay.getViewDefinitions(), new AsyncCallback<SaveUpdateCQLResult>() {
								
								@Override
								public void onFailure(Throwable caught) {
									searchDisplay.setCurrentSelectedDefinitionObjId(null);
									searchDisplay.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
								}
								
								@Override
								public void onSuccess(SaveUpdateCQLResult result) {
									if (result.isSuccess()) {			
											searchDisplay.setViewDefinitions(result.getCqlModel().getDefinitionList()); 
											MatContext.get().setDefinitions(getDefinitionList(result.getCqlModel().getDefinitionList()));
											searchDisplay.clearAndAddDefinitionNamesToListBox();
											searchDisplay.updateDefineMap();
											searchDisplay.getErrorMessageAlert().clearAlert();
											searchDisplay.getSuccessMessageAlert().setVisible(true);
											
											searchDisplay.getSearchSuggestDefineTextBox().setText("");
											searchDisplay.getDefineNameTxtArea().setText("");
											searchDisplay.getDefineAceEditor().setText("");
											searchDisplay.setCurrentSelectedDefinitionObjId(null);
											searchDisplay.setIsPageDirty(false);
											searchDisplay.getDefineAceEditor().clearAnnotations();
											searchDisplay.getDefineAceEditor().removeAllMarkers();
											searchDisplay.getDefineAceEditor().redisplay();
											searchDisplay.getDefineAceEditor().setAnnotations();
											searchDisplay.getDefineAceEditor().redisplay();
											searchDisplay.getDefineButtonBar().getDeleteButton().setEnabled(false);
											searchDisplay.getSuccessMessageAlert().createAlert("This Definition has been deleted successfully.");
									
									} else if (result.getFailureReason() == 2) {
										searchDisplay.getSuccessMessageAlert().clearAlert();
										searchDisplay.getErrorMessageAlert().createAlert("Unable to find Node to modify.");
										searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
									} 								
								}
							});
				} else {
					searchDisplay.resetMessageDisplay();
					searchDisplay.getErrorMessageAlert().createAlert("Please select a definition to delete.");
					searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
				}
			} else {
				searchDisplay.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getERROR_DEFINITION_NAME_NO_SPECIAL_CHAR());
				searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
			}
			
			
			
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getErrorMessageAlert().createAlert("Please select a definition to delete.");
			searchDisplay.getDefineNameTxtArea().setText(definitionName.trim());
		}
	}
	
	/**
	 * Delete function
	 */
	protected void deleteFunction() {

		searchDisplay.resetMessageDisplay();
		final String functionName = searchDisplay.getFuncNameTxtArea().getText();
		String functionBody = searchDisplay.getFunctionBodyAceEditor().getText();
		String funcContext = "";
		if(searchDisplay.getContextFuncPATRadioBtn().getValue()){
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
			if(searchDisplay.getCurrentSelectedFunctionObjId() != null){
				CQLFunctions toBeModifiedFuncObj = searchDisplay.getFunctionMap().get(searchDisplay.getCurrentSelectedFunctionObjId());
				MatContext.get().getMeasureService().deleteFunctions(MatContext.get().getCurrentMeasureId(), toBeModifiedFuncObj, function,
						searchDisplay.getViewFunctions(), new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							searchDisplay.setViewFunctions(result.getCqlModel().getCqlFunctions());
							MatContext.get().setFuncs(getFunctionList(result.getCqlModel().getCqlFunctions()));
							searchDisplay.clearAndAddFunctionsNamesToListBox();
							searchDisplay.updateFunctionMap();
							searchDisplay.getErrorMessageAlert().clearAlert();
							
							searchDisplay.getSearchSuggestFuncTextBox().setText("");
							searchDisplay.getSuccessMessageAlert().setVisible(true);
							searchDisplay.getFuncNameTxtArea().setText("");
							searchDisplay.getFunctionBodyAceEditor().setText("");
							searchDisplay.setCurrentSelectedFunctionObjId(null);
							searchDisplay.setIsPageDirty(false);
							searchDisplay.getFunctionBodyAceEditor().clearAnnotations();
							searchDisplay.getFunctionBodyAceEditor().removeAllMarkers();
							searchDisplay.getFunctionBodyAceEditor().redisplay();
							searchDisplay.getFunctionBodyAceEditor().setAnnotations();
							searchDisplay.getFunctionBodyAceEditor().redisplay();
							searchDisplay.getFunctionButtonBar().getDeleteButton().setEnabled(false);
							searchDisplay.getSuccessMessageAlert().createAlert("This Function has been deleted successfully.");
						} else if (result.getFailureReason() == 2) {
							searchDisplay.getSuccessMessageAlert().clearAlert();
							searchDisplay.getErrorMessageAlert()
							.createAlert("Unable to find Node to modify.");
							searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
						}  if(result.getFunction()!=null){
							searchDisplay.createAddArgumentViewForFunctions(result.getFunction().getArgumentList());
						}
					}
				});
			} else {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getErrorMessageAlert().createAlert("Please select a function to delete.");
				searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
			}
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getErrorMessageAlert().createAlert("Please select a function to delete.");
			searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
		}
	}
	
	protected void deleteParameter() {

		searchDisplay.resetMessageDisplay();
		final String parameterName = searchDisplay.getParameterNameTxtArea().getText();
		String parameterBody = searchDisplay.getParameterAceEditor().getText();

		if (!parameterName.isEmpty()) {
			CQLParameter parameter = new CQLParameter();
			parameter.setParameterLogic(parameterBody);
			parameter.setParameterName(parameterName);
			if(searchDisplay.getCurrentSelectedParamerterObjId() != null){
				CQLParameter toBeModifiedParamObj = searchDisplay.getParameterMap().get(searchDisplay.getCurrentSelectedParamerterObjId());
				MatContext.get().getMeasureService().deleteParameter(MatContext.get().getCurrentMeasureId(), toBeModifiedParamObj, parameter,
						searchDisplay.getViewParameterList(), new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							searchDisplay.setViewParameterList((result.getCqlModel().getCqlParameters()));
							MatContext.get().setParameters(getParamaterList(result.getCqlModel().getCqlParameters()));
							searchDisplay.clearAndAddParameterNamesToListBox();
							searchDisplay.updateParamMap();
							searchDisplay.getErrorMessageAlert().clearAlert();
							searchDisplay.getSuccessMessageAlert().setVisible(true);
							
							searchDisplay.getSearchSuggestTextBox().setText("");
							searchDisplay.getParameterNameTxtArea().setText("");
							searchDisplay.getParameterAceEditor().setText("");
							searchDisplay.setCurrentSelectedParamerterObjId(null);
							searchDisplay.setIsPageDirty(false);
							searchDisplay.getParameterAceEditor().clearAnnotations();
							searchDisplay.getParameterAceEditor().removeAllMarkers();
							searchDisplay.getParameterAceEditor().redisplay();
							searchDisplay.getParameterAceEditor().setAnnotations();
							searchDisplay.getParameterAceEditor().redisplay();
							searchDisplay.getParameterButtonBar().getDeleteButton().setEnabled(false);
							searchDisplay.getSuccessMessageAlert().createAlert("This Parameter has been deleted successfully.");
						} else if (result.getFailureReason() == 2) {
							searchDisplay.getSuccessMessageAlert().clearAlert();
							searchDisplay.getErrorMessageAlert().createAlert("Unable to find Node to modify.");
							searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());
						}  
					}
				});
			} else {
				searchDisplay.resetMessageDisplay();
				searchDisplay.getErrorMessageAlert().createAlert("Please select parameter to delete.");
				searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());		
			}
		} else {
			searchDisplay.resetMessageDisplay();
			searchDisplay.getErrorMessageAlert().createAlert("Please select a parameter to delete.");
			searchDisplay.getParameterNameTxtArea().setText(parameterName.trim());		
		}
	}
	
	private void loadElementLookUpNode(){
		
		MatContext.get().getMeasureService().getMeasureXmlForMeasure(
				MatContext.get().getCurrentMeasureId(), new AsyncCallback<MeasureXmlModel>() {
			
			@Override
			public void onSuccess(MeasureXmlModel result) {
				String xml = result != null ? result.getXml() : null;
				setMeasureElementsMap(xml);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		} );
	}
	
	
	/**
	 * Sets the qdm elements map. 
	 * Also finds SubTree Node and corresponding Node Tree and add to SubTreeLookUpNode map.
	 * Also finds CQL dEfinitions and add to CQLDEfinitionsNode map.
	 * @param xml            the new qdm elements map
	 * @param measureId the measure id
	 */
	private void setMeasureElementsMap(String xml) {
		
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
				//Window.alert("I failed");
				
			}
			@Override
			public void onSuccess(Map<String, List<String>> datatypeMap) {
				CQLWorkSpaceConstants.setDatatypeMap(datatypeMap);
			}
		});
	}
	
	public void setupElementLookupQDMNodes(NodeList nodeList) {
		if ((null != nodeList) && (nodeList.getLength() > 0)) {
			NodeList qdms = nodeList.item(0).getChildNodes();
			for (int i = 0; i < qdms.getLength(); i++) {
				if ("qdm".equals(qdms.item(i).getNodeName())) {
					NamedNodeMap namedNodeMap = qdms.item(i).getAttributes();
					String isSupplementData = namedNodeMap.getNamedItem("suppDataElement").getNodeValue();
					if (isSupplementData.equals("false")) { //filter supplementDataElements from elementLookUp
						String name = namedNodeMap.getNamedItem("name").getNodeValue();
						// Prod Issue fixed : qdm name has trailing spaces which is reterived frm VSAC.
						//So QDM attribute dialog box is throwing error in FF.To fix that spaces are removed from start and end.
						name = name.trim();
						//name = name.replaceAll("^\\s+|\\s+$", "");
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
		addQDMElementExpIdentifierHandlers();
		
		/**
		 * this functionality is to clear the content on the QDM Element Search Panel.
		 * */
		searchDisplay.getQdmView().getCancelQDMButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getQdmView().resetQDSMsgPanel();
				isModified = false;
				searchDisplay.getQdmView().getSearchHeader().setText("Search");
				searchDisplay.getQdmView().getOIDInput().setEnabled(true);
				searchDisplay.getQdmView().getOIDInput().setValue("");
				searchDisplay.getQdmView().getOIDInput().setTitle("Enter OID");
				searchDisplay.getQdmView().getUserDefinedInput().setEnabled(true);
				searchDisplay.getQdmView().getUserDefinedInput().setTitle("Enter Name");
				searchDisplay.getQdmView().getUserDefinedInput().setValue("");
				searchDisplay.getQdmView().getQDMExpIdentifierListBox().clear();
				searchDisplay.getQdmView().getVersionListBox().clear();
				
				searchDisplay.getQdmView().getQDMExpIdentifierListBox().setEnabled(false);
				searchDisplay.getQdmView().getVersionListBox().setEnabled(false);
			
				searchDisplay.getQdmView().getSaveButton().setEnabled(false);
				
			}
		});
		
		/**
		 * this functionality is to retrieve the value set from VSAC with latest information which
		 * consists of Expansion Identifier list and Version List.
		 * */
		searchDisplay.getQdmView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					searchDisplay.getQdmView().resetQDSMsgPanel();
					String version = null;
					String expansionProfile = null;
					searchValueSetInVsac(version, expansionProfile);
				}
			}
		});
		
		
		/**
		 * this handler is invoked when apply button is clicked on search Panel in
		 * QDM elements tab and this is to add new value set or user Defined QDM to the
		 * Applied QDM list.
		 * */
		searchDisplay.getQdmView().getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					MatContext.get().clearDVIMessages();
					searchDisplay.getQdmView().resetQDSMsgPanel();
					/*if(isModified && (modifyValueSetDTO != null)){
							modifyQDM(isUserDefined);
						
					} else {
							addSelectedCodeListtoMeasure(isUserDefined);
					}*/

				}
			}
		});
		
		
		/**
		 * Adding value Change handler for UserDefined Input in Search Panel in QDM Elements Tab
		 * 
		 * */
		searchDisplay.getQdmView().getUserDefinedInput().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				searchDisplay.getQdmView().resetQDSMsgPanel();
				validateUserDefinedInput();
			}
		});
		
		/**
		 *  Adding value change handler for OID input in Search Panel in QDM elements Tab
		 */
		
		searchDisplay.getQdmView().getOIDInput().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				searchDisplay.getQdmView().resetQDSMsgPanel();
				validateOIDInput();
			}
		});
		
		
		
		/**
		 * value change handler for Expansion Identifier in Search Panel in QDM Elements Tab
		 * */
		searchDisplay.getQdmView().getQDMExpIdentifierListBox().addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				searchDisplay.getQdmView().resetQDSMsgPanel();
				if(!searchDisplay.getQdmView().getExpansionIdentifierValue(
						searchDisplay.getQdmView().getQDMExpIdentifierListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
					searchDisplay.getQdmView().getVersionListBox().setSelectedIndex(0);
				}	
			}
		});
		
		/**
		 * value Change Handler for Version listBox in Search Panel
		 * */
		searchDisplay.getQdmView().getVersionListBox().addChangeHandler(new ChangeHandler() {
			
			
			@Override
			public void onChange(ChangeEvent event) {
				searchDisplay.getQdmView().resetQDSMsgPanel();
				if(!searchDisplay.getQdmView().getVersionValue(
						searchDisplay.getQdmView().getVersionListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
					searchDisplay.getQdmView().getQDMExpIdentifierListBox().setSelectedIndex(0);
				}
				
			}
		});
	}
	
	
	/**
	 * click Handlers for ExpansioN Identifier Panel in new QDM Elements Tab.
	 */
	private void addQDMElementExpIdentifierHandlers() {
		searchDisplay.getQdmView().getApplyDefaultExpansionIdButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					// code for adding profile to List to applied QDM
					searchDisplay.getQdmView().resetQDSMsgPanel();
					if (!MatContext.get().isUMLSLoggedIn()) { // UMLS
						// Login
						// Validation
						searchDisplay.getQdmView().getErrorMessageDisplay().setMessage(MatContext.get()
								.getMessageDelegate().getUMLS_NOT_LOGGEDIN());
						return ;
					}
					searchDisplay.getQdmView().getSearchHeader().setText("Search");
					//String selectedValue = searchDisplay.getQdmView().getExpValue(searchDisplay.getQdmView().getVSACExpansionIdentifierListBox());
					/*if(!selectedValue.equalsIgnoreCase("--Select--")){
						expIdentifierToAllQDM = selectedValue;
						//updateAllQDMsWithExpProfile(appliedQDMList);
					} else if(!searchDisplay.getQdmView().getDefaultExpIdentifierSel().getValue()){
						expIdentifierToAllQDM = "";
						//updateAllQDMsWithExpProfile(appliedQDMList);
					} else {
						searchDisplay.getQdmView().getErrorMessageDisplay().setMessage(MatContext.get()
								.getMessageDelegate().getVsacExpansionIdentifierSelection());
					}*/
				}
			}
		});
		
		searchDisplay.getQdmView().getDefaultExpIdentifierSel().addValueChangeHandler(
				new ValueChangeHandler<Boolean>() {
					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						if (event.getValue().toString().equals("true")) {
							if (!MatContext.get().isUMLSLoggedIn()) { // UMLS
								// Login
								// Validation
								searchDisplay.getQdmView()
								.getErrorMessageDisplay()
								.setMessage(
										MatContext.get()
										.getMessageDelegate()
										.getUMLS_NOT_LOGGEDIN());
								return;
							}
							searchDisplay.getQdmView().getVSACExpansionIdentifierListBox().setEnabled(
									true);
							searchDisplay.getQdmView().setExpIdentifierList(MatContext.get()
									.getExpIdentifierList());
							searchDisplay.getQdmView().setDefaultExpansionIdentifierListBox();
						} else if (event.getValue().toString().equals("false")) {
							searchDisplay.getQdmView().getVSACExpansionIdentifierListBox().setEnabled(
									false);
							searchDisplay.getQdmView().setDefaultExpansionIdentifierListBox();
						}
						
					}
				});
		
		
	}
	
	/**
	 * Search value set in vsac.
	 *
	 * @param version            the version
	 * @param expansionProfile the expansion profile
	 */
	private void searchValueSetInVsac(String version, String expansionProfile) {
		
		final String oid = searchDisplay.getQdmView().getOIDInput().getValue();
		if (!MatContext.get().isUMLSLoggedIn()) {
			searchDisplay.getQdmView().getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate()
					.getUMLS_NOT_LOGGEDIN());
			return;
		}
		
		// OID validation.
		if ((oid == null) || oid.trim().isEmpty()) {
			searchDisplay.getQdmView().getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate()
					.getUMLS_OID_REQUIRED());
			return;
		}
		//showSearchingBusy(true);
		
		if(expIdentifierToAllQDM.isEmpty()){
			expansionProfile = null;
		} else {
			expansionProfile = expIdentifierToAllQDM;
		}
		
		vsacapiService.getMostRecentValueSetByOID(oid,
				expansionProfile, new AsyncCallback<VsacApiResult>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getQdmView().getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate()
						.getVSAC_RETRIEVE_FAILED());
				//showSearchingBusy(false);
			}
			
			/**
			 * On success.
			 * 
			 * @param result
			 *            the result
			 */
			@Override
			public void onSuccess(final VsacApiResult result) {
				//to get the VSAC version list corresponding the OID
				if(result.isSuccess()){
					List<MatValueSet> matValueSets = result.getVsacResponse();
					if (matValueSets != null) {
						MatValueSet matValueSet = matValueSets.get(0);
						currentMatValueSet = matValueSet;
					}
					searchDisplay.getQdmView().getOIDInput().setTitle(oid);
					searchDisplay.getQdmView().getUserDefinedInput().setValue(matValueSets.get(0).getDisplayName());
					searchDisplay.getQdmView().getUserDefinedInput().setTitle(matValueSets.get(0).getDisplayName());
					searchDisplay.getQdmView().getQDMExpIdentifierListBox().setEnabled(true);
					searchDisplay.getQdmView().getVersionListBox().setEnabled(true);
					
					
					searchDisplay.getQdmView().getSaveButton().setEnabled(true);
					
					if(isExpansionIdentifier){
						searchDisplay.getQdmView().getQDMExpIdentifierListBox().setEnabled(false);
						searchDisplay.getQdmView().getVersionListBox().setEnabled(false);
						searchDisplay.getQdmView().getQDMExpIdentifierListBox().clear();
						searchDisplay.getQdmView().getQDMExpIdentifierListBox().addItem(expIdentifierToAllQDM,
								expIdentifierToAllQDM);
					} else {
						searchDisplay.getQdmView().setQDMExpIdentifierListBox(getProfileList(
								MatContext.get().getVsacExpIdentifierList()));
						getVSACVersionListByOID(oid);
						searchDisplay.getQdmView().getQDMExpIdentifierListBox().setEnabled(true);
						searchDisplay.getQdmView().getVersionListBox().setEnabled(true);
					}
					//showSearchingBusy(false);
					searchDisplay.getQdmView().getSuccessMessageDisplay().setMessage(MatContext.get()
							.getMessageDelegate().getVSAC_RETRIEVAL_SUCCESS());
					
				} else {
					String message = convertMessage(result.getFailureReason());
					searchDisplay.getQdmView().getErrorMessageDisplay().setMessage(message);
					//showSearchingBusy(false);
				}
			}
		});
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
				message = MatContext.get().getMessageDelegate()
				.getUMLS_NOT_LOGGEDIN();
				break;
			case VsacApiResult.OID_REQUIRED:
				message = MatContext.get().getMessageDelegate()
				.getUMLS_OID_REQUIRED();
				break;
			default:
				message = MatContext.get().getMessageDelegate()
				.getVSAC_RETRIEVE_FAILED();
		}
		return message;
	}
	/**
	 * Gets the VSAC version list by oid.
	 * if the default Expansion Identifier is present then
	 * we are not making this VSAC call.
	 * 
	 * @param oid
	 *            the oid
	 * @return the VSAC version list by oid
	 */
	private void getVSACVersionListByOID(String oid) {
		vsacapiService.getAllVersionListByOID(oid,
				new AsyncCallback<VsacApiResult>() {
			
			@Override
			public void onSuccess(VsacApiResult result) {
				
				if (result.getVsacVersionResp() != null) {
					searchDisplay.getQdmView().setQDMVersionListBoxOptions(getVersionList(result
							.getVsacVersionResp()));
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getQdmView().getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate()
						.getVSAC_RETRIEVE_FAILED());
				//showSearchingBusy(false);
			}
		});
		
	}
	/**
	 * Gets the version list.
	 *
	 * @param list the list
	 * @return the version list
	 */
	private List<? extends HasListBox> getVersionList(List<VSACVersion> list) {
		return list;
	}
	
	/**
	 * Gets the profile list.
	 *
	 * @param list the list
	 * @return the profile list
	 */
	private List<? extends HasListBox> getProfileList(List<VSACExpansionIdentifier> list){
		return list;
	}
}
