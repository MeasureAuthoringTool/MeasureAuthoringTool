package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder.In;

import mat.client.MatPresenter;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.cqlworkspace.CQLWorkSpaceView.Observer;
import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.DeleteConfirmationMessageAlert;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.JSONCQLTimingExpressionUtility;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLParameter;
import mat.shared.CQLErrors;
import mat.shared.CQLModelValidator;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceMarkerType;
import edu.ycp.cs.dh.acegwt.client.ace.AceRange;

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
		List<QualityDataSetDTO> getAppliedQdmList();
		
		/**
		 * Sets the applied qdm list.
		 *
		 * @param appliedQdmList the new applied qdm list
		 */
		void setAppliedQdmList(List<QualityDataSetDTO> appliedQdmList);
		
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

		Button getDeleteConfirmationYesButton();

		Button getDeleteConfirmationNoButton();

		void setUsedCQLArtifacts(GetUsedCQLArtifactsResult results);
		
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
	private void buildTimingExpressionPopUp() {
		searchDisplay.resetMessageDisplay();
		InsertTimingExpressionIntoAceEditor.showTimingExpressionDialogBox(searchDisplay, currentSection);
		searchDisplay.setIsPageDirty(true);
	}
	
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
		
		searchDisplay.getDefineTimingExpButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				buildTimingExpressionPopUp();
			}
		});
		
		
		searchDisplay.getFuncTimingExpButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				buildTimingExpressionPopUp();
			}
		});
		
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
							searchDisplay.showDeleteConfirmationMessageAlert("You have selected to delete this expression. Do you want to permanently delete this Parameter?");	
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
							searchDisplay.showDeleteConfirmationMessageAlert("You have selected to delete this expression. Do you want to permanently delete this Definition?");	
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
							searchDisplay.showDeleteConfirmationMessageAlert("You have selected to delete this expression. Do you want to permanently delete this Function?");	
						} 
					}
					
				});
			}
			
		});
		
		searchDisplay.getDeleteConfirmationNoButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
			}
		}); 
		
		searchDisplay.getDeleteConfirmationYesButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchDisplay.getDefineNameTxtArea().getText() != null) {
					deleteDefinition();
				}
				
				if(searchDisplay.getFuncNameTxtArea().getText() != null) {
					deleteFunction();
				}
				
				if(searchDisplay.getParameterNameTxtArea().getText() != null) {
					deleteParameter();
				}
			}
		}); 
		
		
		addEventHandlerOnAceEditors();
		addEventHandlersOnContextRadioButtons();
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
										searchDisplay.getSuccessMessageAlert().createAlert(MatContext.get()
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
									searchDisplay.getSuccessMessageAlert().createAlert(MatContext.get()
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
										searchDisplay.getSuccessMessageAlert().createAlert(
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
									searchDisplay.getSuccessMessageAlert().createAlert(MatContext.get()
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
												searchDisplay.getSuccessMessageAlert().createAlert(
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
									searchDisplay.getSuccessMessageAlert().createAlert(MatContext.get()
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
		getAppliedQDMList(true);
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
			}
		}
	}
	/**
	 * Method to build View for Anchor List item View CQL.
	 */
	private void buildCQLView() {
		
		MatContext.get().getMeasureService().getCQLFileData(MatContext.get()
				.getCurrentMeasureId(), new AsyncCallback<SaveUpdateCQLResult>() {
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if (result.isSuccess()) {
					if ((result.getCqlString() != null)
							&& !result.getCqlString().isEmpty()) {
						searchDisplay.getCqlAceEditor().setText(result.getCqlString());
					}
					//validateViewCQLFile();
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
	 */
	private HTML getMsgPanel(IconType iconType, String message) {
		Icon checkIcon = new Icon(iconType);
		HTML msgHtml = new HTML(checkIcon + " <b>" + message + "</b>");
		return msgHtml;
	}
	
	/**
	 * Get All Applied QDM List from Measure XML.
	 *
	 * @param checkForSupplementData - boolean.
	 * @return the applied qdm list
	 */
	private void getAppliedQDMList(boolean checkForSupplementData) {
		String measureId = MatContext.get().getCurrentMeasureId();
		if ((measureId != null) && !measureId.equals("")) {
			MatContext.get().getMeasureService().getAppliedQDMFromMeasureXml(measureId,
					checkForSupplementData,
					new AsyncCallback<QualityDataModelWrapper>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Unable to Reterive QDM List");
				}
				@Override
				public void onSuccess(QualityDataModelWrapper result) {
					List<QualityDataSetDTO> appliedQDMList = new ArrayList<QualityDataSetDTO>();
					for (QualityDataSetDTO dto : result.getQualityDataDTO()) {
						if (!dto.getOid().equalsIgnoreCase(MEASUREMENT_PERIOD_OID)) {
							appliedQDMList.add(dto);
						} else {
							System.out.print("Skipping Measurement Period from QDM List.");
						}
					}
					searchDisplay.setAppliedQdmList(appliedQDMList);
				}
			});
		}
	}
	
	
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
	
	private void validateViewCQLFile(){
		MatContext.get().getMeasureService().parseCQLForErrors(MatContext.get().getCurrentMeasureId(), new AsyncCallback<SaveUpdateCQLResult>(){

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageAlert().createAlert(
						MatContext.get().getMessageDelegate().getGenericErrorMessage());							
			}

			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				
				/*if(!result.getCqlErrors().isEmpty()){
					StringBuilder strBuilder = new StringBuilder("Validation failed with following errors.");
					for(int i=0; i<result.getCqlErrors().size(); i++) {
						strBuilder.append("\n\n");
						strBuilder.append(result.getCqlErrors().get(i));	
					}
					searchDisplay.getErrorMessageAlert()
					.createAlert(strBuilder.toString());
					
					//searchDisplay.getCqlAceEditor().addMarker(AceRange.create(1, 1, 1, 1), "yellowColor", AceMarkerType.FULL_LINE, true);
				}*/
				searchDisplay.getCqlAceEditor().clearAnnotations();
				searchDisplay.getCqlAceEditor().removeAllMarkers();
				searchDisplay.getCqlAceEditor().redisplay();
				if(!result.getCqlErrors().isEmpty()){
					/*searchDisplay.getErrorMessageAlert()
					.createAlert("Validation Failure Check Text in Editor.");*/
					//searchDisplay.getDefineAceEditor().addMarker(AceRange.create(1, 1, 1, 1), "yellowColor", AceMarkerType.TEXT, true);
					for(CQLErrors error : result.getCqlErrors()){
						//if(error.getErrorInLine() >= result.getCqlModel().getLines()){
							String errorMessage = new String();
							errorMessage = errorMessage.concat("Error in line : "+ error.getErrorInLine() + " at Offset :" + error.getErrorAtOffeset());
							HTML html = new HTML("<h6>" + errorMessage + "</h6>");
							//alert.add(html);
							int line = error.getErrorInLine();
							//line = line - result.getCqlModel().getLines() + 1;
							int column = error.getErrorAtOffeset();
							System.out.println("line: " + line + "  column: " + column);
							searchDisplay.getCqlAceEditor().addAnnotation(line - 1, column, error.getErrorMessage(), AceAnnotationType.WARNING);
							//searchDisplay.getDefineAceEditor().setAnnotations();
							// try SCREEN_LINE, FULL_LINE, TEXT          underline_squiggly
						//	int id = searchDisplay.getDefineAceEditor().addMarker(AceRange.create(line - 1, 1, line-1, 15), "underline", AceMarkerType.FULL_LINE, false);
							int id = searchDisplay.getCqlAceEditor().addMarker(AceRange.create(line - 1, column, line - 1, column + 50), "underline", AceMarkerType.FULL_LINE, false);
						//}
					}
					
					searchDisplay.getCqlAceEditor().setAnnotations();
					searchDisplay.getCqlAceEditor().redisplay();
					
				}
				
			}
			
		});

	}
	
	private boolean validateCQLArtifact(SaveUpdateCQLResult result ,String currentSect){
		boolean isInvalid = false;
		if(!result.getCqlErrors().isEmpty()){
			final AceEditor editor = getAceEditorBasedOnCurrentSection(searchDisplay, currentSection);
			System.out.println("In Presenter Errors");
			
			for(CQLErrors error : result.getCqlErrors()){
				
//				String errorMessage = "Error in line : "+ error.getErrorInLine() + " at Offset :" + error.getErrorAtOffeset();
//				HTML html = new HTML("<h6>" + errorMessage + "</h6>"); 

				int startLine = error.getStartErrorInLine(); 
				int startColumn = error.getStartErrorAtOffset(); 
				int endLine = error.getEndErrorInLine(); 
				int endColumn = error.getEndErrorAtOffset(); 
				
				
				editor.addAnnotation(startLine, startColumn, error.getErrorMessage(), AceAnnotationType.WARNING);
				int id = editor.addMarker(AceRange.create(startLine, startColumn, endLine, endColumn), "underline", AceMarkerType.FULL_LINE, false);
				if(!isInvalid) {
					isInvalid = true;
				}
			}
		}
		
		return isInvalid;
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
	
}
