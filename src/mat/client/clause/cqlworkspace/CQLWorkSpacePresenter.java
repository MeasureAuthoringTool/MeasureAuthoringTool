package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorCallback;
import edu.ycp.cs.dh.acegwt.client.ace.AceSelection;
import edu.ycp.cs.dh.acegwt.client.ace.AceSelectionListener;
import mat.client.MatPresenter;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.cqlworkspace.CQLWorkSpaceView.CustomTextAreaWithNoWhiteSpaces;
import mat.client.clause.cqlworkspace.CQLWorkSpaceView.Observer;
import mat.client.shared.CQLSaveDeleteEraseButtonBar;
import mat.client.shared.ErrorMessageAlert;
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
import mat.shared.SaveUpdateCQLResult;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLPresenterNavBarWithList.
 */
public class CQLWorkSpacePresenter implements MatPresenter{
	
	/** The panel. */
	SimplePanel panel = new SimplePanel();
	
	/** The clicked menu. */
	String currentSection = "general";
	String nextSection = "general";
	String currentParamSelection = "";
	String previousParamSelection = "";
	
	QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);
	
	/** The view. */
	String view ="";
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
		TextArea getParameterNameTxtArea();
		
		
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
		HashMap<String, CQLParameter> getParameterMap();
		
		/**
		 * Gets the parameter name map.
		 *
		 * @return the parameter name map
		 */
		HashMap<String, String> getParameterNameMap();
		
		
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
		HashMap<String, String> getDefineNameMap();
		
		/**
		 * Gets the definition map.
		 *
		 * @return the definition map
		 */
		HashMap<String, CQLDefinition> getDefinitionMap();
		
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
		TextArea getDefineNameTxtArea();
		
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
		 * @param successMessageAlert
		 *            the new success message alert
		 */
		void setWarningConfirmationMessageAlert(WarningConfirmationMessageAlert warningMessageAlert);
		
		/**
		 * Builds the cql file view.
		 */
		void buildCQLFileView();
		
		/**
		 * Sets the checks if is page dirty.
		 *
		 * @param isParameterDirty the new checks if is page dirty
		 */
		void setIsPageDirty(Boolean isPageDirty);
		
		Boolean getIsPageDirty();
		
		void setIsDoubleClick(Boolean isDoubleClick);
		
		Boolean isDoubleClick();
		
		void setIsNavBarClick(Boolean isDoubleClick);
		
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
		TextArea getFuncNameTxtArea();
		
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
		HashMap<String, CQLFunctions> getFunctionMap();
		
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
		CQLSaveDeleteEraseButtonBar getParameterButtonBar();
		
		/**
		 * Gets the define button bar.
		 *
		 * @return the define button bar
		 */
		CQLSaveDeleteEraseButtonBar getDefineButtonBar();
		
		/**
		 * Gets the function button bar.
		 *
		 * @return the function button bar
		 */
		CQLSaveDeleteEraseButtonBar getFunctionButtonBar();
		
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
		
		Button getEraseFunctionButton();
		
		
		/**
		 * Creates the add argument view for functions.
		 *
		 * @param argumentList the argument list
		 */
		void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList);
		
		List<QDSAttributes> getAvailableQDSAttributeList();
		
		void setAvailableQDSAttributeList(List<QDSAttributes> availableQDSAttributeList);
		
		InlineRadio getContextFuncPATRadioBtn();
		
		InlineRadio getContextFuncPOPRadioBtn();
		
		boolean validateForSpecialChar(String identifierName);
		
		Map<String, CQLFunctionArgument> getFunctionArgNameMap();
		
		
		
		void setFunctionArgNameMap(HashMap<String, CQLFunctionArgument> functionArgNameMap);
		
		org.gwtbootstrap3.client.ui.ListBox getFuncNameListBox();
		
		Button getWarningConfirmationYesButton();
		
		Button getWarningConfirmationNoButton();
		
		List<QualityDataSetDTO> getAppliedQdmList();
		
		void setAppliedQdmList(List<QualityDataSetDTO> appliedQdmList);
		
		void resetMessageDisplay();
		
		HorizontalPanel getMainHPanel();
		
		CustomTextAreaWithNoWhiteSpaces getArgumentTextArea();
		
		void setParameterWidgetReadOnly(boolean isEditable);
		
		void showUnsavedChangesWarning();
		
		void setNextClickedMenu(String nextClickedMenu);
		
		Object getNextClickedMenu();
		
		Button getGlobalWarningConfirmationYesButton();
		
		Button getGlobalWarningConfirmationNoButton();
		
		WarningConfirmationMessageAlert getGlobalWarningConfirmationMessageAlert();
		
		void setGlobalWarningConfirmationMessageAlert(WarningConfirmationMessageAlert globalWarningMessageAlert);
		
		void showGlobalUnsavedChangesWarning();
		
	}
	
	/** The search display. */
	ViewDisplay searchDisplay;

	private EventHandler handler;
	
	/**
	 * Instantiates a new CQL presenter nav bar with list.
	 *
	 * @param srchDisplay the srch display
	 */
	public CQLWorkSpacePresenter(ViewDisplay srchDisplay) {
		searchDisplay = srchDisplay;
		addEventHandlers();
		addObserverHandler();
	}
	
	/**
	 * Adds the event handlers.
	 * @param type 
	 */
	private void addEventHandlers() {
		
		searchDisplay.getDefineButtonBar().getInsertButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				InsertIntoAceEditorDialogBox.showListOfParametersDialogBox(searchDisplay);
				searchDisplay.setIsPageDirty(true);
			}
		});
		
		
		searchDisplay.getAddDefineButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addAndModifyDefintions();
			}
		});
		
		searchDisplay.getAddParameterButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addAndModifyParameters();
			}
			
		});
		
		searchDisplay.getSaveFunctionButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addAndModifyFunction();
			}
		});
		
		searchDisplay.getDefineAceEditor().addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(!searchDisplay.getDefineAceEditor().getReadOnly()){
					//System.out.println("In Define Ace Editor addKeyDownHandler, setting dirtyflag = true");
					searchDisplay.resetMessageDisplay();
					searchDisplay.setIsPageDirty(true);
				}
			}
		});
		
		searchDisplay.getParameterAceEditor().addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(!searchDisplay.getParameterAceEditor().getReadOnly()){
					//System.out.println("In Define Ace Editor addKeyDownHandler, setting dirtyflag = true");
					searchDisplay.resetMessageDisplay();
					searchDisplay.setIsPageDirty(true);
				}
			}
		});
	
		
	searchDisplay.getFunctionBodyAceEditor().addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(!searchDisplay.getFunctionBodyAceEditor().getReadOnly()){
					searchDisplay.resetMessageDisplay();
					searchDisplay.setIsPageDirty(true);
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
				System.out.println("In changeListboxSelection setting dirtyFlag to false and clearing Alert");
	
				searchDisplay.setIsPageDirty(false);
				searchDisplay.getWarningConfirmationMessageAlert().clearAlert();
				if (searchDisplay.isDoubleClick()) {
					changeListboxSelection();
				} else if (searchDisplay.isNavBarClick()) {
					changeSectionSelection();
				} else {
					clearSelection();
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
					//unsetActiveMenuItem(nextClickedMenu);
				} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)) {
					searchDisplay.getParameterNameListBox().setSelectedIndex(-1);
					//unsetActiveMenuItem(nextClickedMenu);
				} else if (currentSection.equals(CQLWorkSpaceConstants.CQL_DEFINE_MENU)) {
					searchDisplay.getDefineNameListBox().setSelectedIndex(-1);
					//unsetActiveMenuItem(nextClickedMenu);
				}
			}
		});
		
		searchDisplay.getAddNewArgument().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				CQLFunctionArgument addNewFunctionArgument = new CQLFunctionArgument();
				AddFunctionArgumentDialogBox.showArgumentDialogBox( addNewFunctionArgument , false, searchDisplay);
				searchDisplay.setIsPageDirty(true);
			}
		});
		
		
		searchDisplay.getContextDefinePATRadioBtn().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				searchDisplay.setIsPageDirty(true);
				if(searchDisplay.getContextDefinePATRadioBtn().getValue()){
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
				if(searchDisplay.getContextDefinePOPRadioBtn().getValue()){
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
				if(searchDisplay.getContextFuncPATRadioBtn().getValue()){
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
				if(searchDisplay.getContextFuncPOPRadioBtn().getValue()){
					searchDisplay.getContextFuncPATRadioBtn().setValue(false);
				} else {
					searchDisplay.getContextFuncPATRadioBtn().setValue(true);
				}
			}
		});
	}
	
	public void changeListboxSelection () {
		
		searchDisplay.setIsDoubleClick(false);
		searchDisplay.setIsNavBarClick(false);
		switch (currentSection) { 
			case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
				searchDisplay.getFuncNameListBox().fireEvent(new DoubleClickEvent(){});
			case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
				searchDisplay.getParameterNameListBox().fireEvent(new DoubleClickEvent(){});
			case (CQLWorkSpaceConstants.CQL_DEFINE_MENU) : 
				searchDisplay.getDefineNameListBox().fireEvent(new DoubleClickEvent(){});
		}

	}
	
	public void changeSectionSelection () {
		switch (currentSection) {
			case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
				searchDisplay.getFunctionLibrary().fireEvent(new ClickEvent(){});
			case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
				searchDisplay.getParameterLibrary().fireEvent(new ClickEvent(){});
			case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
				searchDisplay.getDefinitionLibrary().fireEvent(new ClickEvent(){});
			case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
				searchDisplay.getGeneralInformation().fireEvent(new ClickEvent(){});
			case (CQLWorkSpaceConstants.CQL_VIEW_MENU): 
				searchDisplay.getViewCQL().fireEvent(new ClickEvent(){});
		}
		switch (nextSection) {
			case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
				searchDisplay.getFunctionLibrary().fireEvent(new ClickEvent(){});
			case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
				searchDisplay.getParameterLibrary().fireEvent(new ClickEvent(){});
			case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
				searchDisplay.getDefinitionLibrary().fireEvent(new ClickEvent(){});
			case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
				searchDisplay.getGeneralInformation().fireEvent(new ClickEvent(){});
			case (CQLWorkSpaceConstants.CQL_VIEW_MENU): 
				searchDisplay.getViewCQL().fireEvent(new ClickEvent(){});
		}
		//setActiveMenuItem(nextClickedMenu);                                                                
	}
	
	public void clearSelection() {
		
		switch (currentSection) {
			case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
				clearFunction();
			case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
				clearParameter();
			case (CQLWorkSpaceConstants.CQL_DEFINE_MENU): 
				clearDefinition();
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
					AddFunctionArgumentDialogBox.showArgumentDialogBox(result,true,searchDisplay);
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
				AddFunctionArgumentDialogBox.showArgumentDialogBox(functionArg,true,searchDisplay);
				
			}
			
		});
	}
	
	/**
	 * Clear parameter.
	 */
	private void clearParameter() {
		searchDisplay.setCurrentSelectedParamerterObjId(null);
		searchDisplay.setIsPageDirty(false);
		if ((searchDisplay.getParameterAceEditor().getText() != null)) {
			searchDisplay.getParameterAceEditor().setText("");
			System.out.println("In clearParameter setting text.");
		}
		if ((searchDisplay.getParameterNameTxtArea() != null)) {
			searchDisplay.getParameterNameTxtArea().clear();
		}
		
		if(MatContext.get().getMeasureLockService()
				.checkForEditPermission()){
			searchDisplay.setParameterWidgetReadOnly(MatContext.get().getMeasureLockService()
					.checkForEditPermission());
		}
	}
	
	/**
	 * Clear definition.
	 */
	private void clearDefinition() {
		searchDisplay.setCurrentSelectedDefinitionObjId(null);
		searchDisplay.setIsPageDirty(false);
		if ((searchDisplay.getDefineAceEditor().getText() != null)) {
			searchDisplay.getDefineAceEditor().setText("");
		}
		if ((searchDisplay.getDefineNameTxtArea() != null)) {
			searchDisplay.getDefineNameTxtArea().clear();
		}
		searchDisplay.getContextDefinePATRadioBtn().setValue(true);
		searchDisplay.getContextDefinePOPRadioBtn().setValue(false);
	}
	
	/**
	 * Clear function.
	 */
	private void clearFunction() {
		searchDisplay.setCurrentSelectedFunctionObjId(null);
		searchDisplay.getFunctionArgumentList().clear();
		searchDisplay.getFunctionArgNameMap().clear();
		searchDisplay.createAddArgumentViewForFunctions(new ArrayList<CQLFunctionArgument>());
		searchDisplay.setIsPageDirty(false);
		if ((searchDisplay.getFunctionBodyAceEditor().getText()!= null)) {
			searchDisplay.getFunctionBodyAceEditor().setText("");
		}
		if ((searchDisplay.getFuncNameTxtArea() != null)) {
			searchDisplay.getFuncNameTxtArea().clear();
		}
		searchDisplay.getContextFuncPATRadioBtn().setValue(true);
		searchDisplay.getContextFuncPOPRadioBtn().setValue(false);
	}
	
	
	/**
	 * Adds the and modify function.
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
			
			if(!searchDisplay.validateForSpecialChar(functionName.trim())) {
				
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
								searchDisplay.clearAndAddFunctionsNamesToListBox();
								searchDisplay.updateFunctionMap();
								searchDisplay.getSuccessMessageAlert().setVisible(true);
								searchDisplay.getSuccessMessageAlert().add(getMsgPanel(IconType.CHECK_CIRCLE,
										MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_MODIFY()));
								searchDisplay.getFuncNameTxtArea().setText(result.getFunction().getFunctionName());
								searchDisplay.setIsPageDirty(false);
							} else if (result.getFailureReason() == 1) {
								searchDisplay.getErrorMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getERROR_DUPLICATE_IDENTIFIER_NAME());
								searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
							} else if (result.getFailureReason() == 2) {
								searchDisplay.getErrorMessageAlert()
								.createAlert("Unable to find Node to modify.");
								searchDisplay.getFuncNameTxtArea().setText(functionName.trim());
							}  else if(result.getFailureReason() == 3) {
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
								searchDisplay.clearAndAddFunctionsNamesToListBox();
								searchDisplay.updateFunctionMap();
								searchDisplay.getFuncNameTxtArea().setText(result.getFunction().getFunctionName());
								searchDisplay.setCurrentSelectedFunctionObjId(result.getFunction().getId());
								searchDisplay.getSuccessMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getSUCCESSFUL_SAVED_CQL_FUNCTIONS());
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
		final String parameterName = searchDisplay.getParameterNameTxtArea().getText();
		String parameterLogic = searchDisplay.getParameterAceEditor().getText();
		if (!parameterName.isEmpty()) {
			
			if(!searchDisplay.validateForSpecialChar(parameterName.trim())) {
				
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
								searchDisplay.clearAndAddParameterNamesToListBox();
								searchDisplay.updateParamMap();
								searchDisplay.getSuccessMessageAlert().createAlert(
										MatContext.get().getMessageDelegate().getSUCESS_PARAMETER_MODIFY());
								searchDisplay.getParameterNameTxtArea()
								.setText(result.getParameter().getParameterName());
								searchDisplay.setIsPageDirty(false);
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
								searchDisplay.clearAndAddParameterNamesToListBox();
								searchDisplay.updateParamMap();
								searchDisplay.getParameterNameTxtArea()
								.setText(result.getParameter().getParameterName());
								searchDisplay.setCurrentSelectedParamerterObjId(result.getParameter().getId());
								searchDisplay.getSuccessMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getSUCCESSFUL_SAVED_CQL_PARAMETER());
								searchDisplay.setIsPageDirty(false);
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
			
			if(!searchDisplay.validateForSpecialChar(definitionName.trim())) {
				
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
										searchDisplay.clearAndAddDefinitionNamesToListBox();
										searchDisplay.updateDefineMap();
										searchDisplay.getSuccessMessageAlert().createAlert(
												MatContext.get().getMessageDelegate().getSUCESS_DEFINITION_MODIFY());
										searchDisplay.getDefineNameTxtArea()
										.setText(result.getDefinition().getDefinitionName());
										searchDisplay.setIsPageDirty(false);
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
								searchDisplay.clearAndAddDefinitionNamesToListBox();
								searchDisplay.updateDefineMap();
								searchDisplay.setCurrentSelectedDefinitionObjId(result.getDefinition().getId());
								searchDisplay.getSuccessMessageAlert().createAlert(MatContext.get()
										.getMessageDelegate().getSUCCESSFUL_SAVED_CQL_DEFINITION());
								searchDisplay.getDefineNameTxtArea()
								.setText(result.getDefinition().getDefinitionName());
								searchDisplay.setIsPageDirty(false);
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
			searchDisplay.getErrorMessageAlert()
			.createAlert(MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_DEFINITION());
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
		if(searchDisplay.getFunctionArgumentList().size() >0){
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
		if(searchDisplay.getFunctionArgumentList().size() >0){
			searchDisplay.getFunctionArgumentList().clear();
		}
		panel.add(searchDisplay.getMainHPanel());
	}
	
	
	/**
	 * Sets the definition widget read only.
	 *
	 * @param isEditable the new definition widget read only
	 */
	private void setDefinitionWidgetReadOnly(boolean isEditable){
		
		searchDisplay.getDefineNameTxtArea().setEnabled(isEditable);
		searchDisplay.getDefineAceEditor().setReadOnly(!isEditable);
		searchDisplay.getDefineButtonBar().setEnabled(isEditable);
		searchDisplay.getContextDefinePATRadioBtn().setEnabled(isEditable);
		searchDisplay.getContextDefinePOPRadioBtn().setEnabled(isEditable);
	}
	
	
	/**
	 * Sets the function widget read only.
	 *
	 * @param isEditable the new function widget read only
	 */
	private void setFunctionWidgetReadOnly(boolean isEditable){
		
		searchDisplay.getFuncNameTxtArea().setEnabled(isEditable);
		searchDisplay.getFunctionBodyAceEditor().setReadOnly(!isEditable);
		searchDisplay.getFunctionButtonBar().setEnabled(isEditable);
		searchDisplay.getAddNewArgument().setEnabled(isEditable);
		searchDisplay.getContextFuncPATRadioBtn().setEnabled(isEditable);
		searchDisplay.getContextFuncPOPRadioBtn().setEnabled(isEditable);
		
	}
	
	
	
	/**
	 * Gets the CQL data.
	 *
	 * @return the CQL data
	 */
	private void getCQLData(){
		MatContext.get().getMeasureService().getCQLData(MatContext.get().getCurrentMeasureId(), new AsyncCallback<SaveUpdateCQLResult>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				
			}
			
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if(result.getCqlModel() != null){
					
					/*if(!result.getCqlModel().getContext().isEmpty()){
						if(result.getCqlModel().getContext().equalsIgnoreCase("Patient")){
							searchDisplay.getContextToggleSwitch().setValue(true);
						} else {
							searchDisplay.getContextToggleSwitch().setValue(false);
						}
					}*/
					
					if ((result.getCqlModel().getDefinitionList() != null) &&
							(result.getCqlModel().getDefinitionList().size() >0)) {
						searchDisplay.setViewDefinitions(result.getCqlModel().getDefinitionList());
						searchDisplay.clearAndAddDefinitionNamesToListBox();
						searchDisplay.updateDefineMap();
					}
					if ((result.getCqlModel().getCqlParameters() != null) &&
							(result.getCqlModel().getCqlParameters().size() >0)) {
						searchDisplay.setViewParameterList(result.getCqlModel().getCqlParameters());
						searchDisplay.clearAndAddParameterNamesToListBox();
						searchDisplay.updateParamMap();
					}
					if ((result.getCqlModel().getCqlFunctions() != null) &&
							(result.getCqlModel().getCqlFunctions().size() >0)) {
						searchDisplay.setViewFunctions(result.getCqlModel().getCqlFunctions());
						searchDisplay.clearAndAddFunctionsNamesToListBox();
						searchDisplay.updateFunctionMap();
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
				searchDisplay.setIsNavBarClick(true);
				searchDisplay.setIsDoubleClick(false);
				if (searchDisplay.getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
					searchDisplay.showUnsavedChangesWarning();
				} else {
					generalInfoEvent();
				}
			}
		});
		
		
		searchDisplay.getParameterLibrary().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.setIsNavBarClick(true);
				searchDisplay.setIsDoubleClick(false);
				if (searchDisplay.getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
					searchDisplay.showUnsavedChangesWarning();
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
				if (searchDisplay.getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
					searchDisplay.showUnsavedChangesWarning();
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
				if (searchDisplay.getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
					searchDisplay.showUnsavedChangesWarning();
				} else {
					functionEvent();
				}
			}
		});
		
		searchDisplay.getViewCQL().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.setIsNavBarClick(true);
				searchDisplay.setIsDoubleClick(false);
				if (searchDisplay.getIsPageDirty()) {
					nextSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
					searchDisplay.showUnsavedChangesWarning();
				} else {
					viewCqlEvent();
				}
			}
		});
		
	}
	
	
	private void generalInfoEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.getGeneralInformation().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
		searchDisplay.buildGeneralInformation();
	}
	
	private void parameterEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.getParameterLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_PARAMETER_MENU;
		searchDisplay.buildParameterLibraryView();
		searchDisplay.setParameterWidgetReadOnly(MatContext.get().getMeasureLockService()
				.checkForEditPermission());
		
	}
	
	private void definitionEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.getDefinitionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_DEFINE_MENU;
		searchDisplay.buildDefinitionLibraryView();
		setDefinitionWidgetReadOnly(MatContext.get().getMeasureLockService()
				.checkForEditPermission());
	}
	
	private void functionEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.getFunctionLibrary().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_FUNCTION_MENU;
		searchDisplay.buildFunctionLibraryView();
		setFunctionWidgetReadOnly(MatContext.get().getMeasureLockService()
				.checkForEditPermission());
	}
	
	private void viewCqlEvent() {
		unsetActiveMenuItem(currentSection);
		searchDisplay.getViewCQL().setActive(true);
		currentSection = CQLWorkSpaceConstants.CQL_VIEW_MENU;
		searchDisplay.buildCQLFileView();
		buildCQLView();
		
	}
	
	/**
	 * Method to unset Anchor List Item selection for previous selection and set for new selections.
	 *
	 * @param menuClickedBefore the menu clicked before
	 */
	private void unsetActiveMenuItem(String menuClickedBefore) {
		searchDisplay.resetMessageDisplay();
		if(menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_GENERAL_MENU)){
			searchDisplay.getGeneralInformation().setActive(false);
		} else if(menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)){
			searchDisplay.getParameterLibrary().setActive(false);
			searchDisplay.getParameterNameListBox().setSelectedIndex(-1);
			if(searchDisplay.getParamCollapse().getElement().getClassName().equalsIgnoreCase("panel-collapse collapse in")){
				searchDisplay.getParamCollapse().getElement().setClassName("panel-collapse collapse");
			}
		} else if(menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DEFINE_MENU)){
			searchDisplay.getDefinitionLibrary().setActive(false);
			searchDisplay.getDefineNameListBox().setSelectedIndex(-1);
			if(searchDisplay.getDefineCollapse().getElement().getClassName().equalsIgnoreCase("panel-collapse collapse in")){
				searchDisplay.getDefineCollapse().getElement().setClassName("panel-collapse collapse");
			}
		} else if(menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)){
			searchDisplay.getFunctionLibrary().setActive(false);
			searchDisplay.getFuncNameListBox().setSelectedIndex(-1);
			if(searchDisplay.getFunctionCollapse().getElement().getClassName().equalsIgnoreCase("panel-collapse collapse in")){
				searchDisplay.getFunctionCollapse().getElement().setClassName("panel-collapse collapse");
			}
		} else if(menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEW_MENU)){
			searchDisplay.getViewCQL().setActive(false);
		}
	}
	
//	/**
//	 * Method to unset Anchor List Item selection for previous selection and set for new selections.
//	 *
//	 * @param menuClickedBefore the menu clicked before
//	 */
//	private void setActiveMenuItem(String menuClicked) {
//		searchDisplay.resetMessageDisplay();
//		if(menuClicked.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_GENERAL_MENU)){
//			searchDisplay.getGeneralInformation().setActive(true);
//		} else if(menuClicked.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_PARAMETER_MENU)){
//			searchDisplay.getParameterLibrary().setActive(true);
//			searchDisplay.getParameterNameListBox().setSelectedIndex(-1);
//			searchDisplay.getParamCollapse().getElement().setClassName("panel-collapse collapse in");
//		} else if(menuClicked.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DEFINE_MENU)){
//			searchDisplay.getDefinitionLibrary().setActive(true);
//			searchDisplay.getDefineNameListBox().setSelectedIndex(-1);
//			searchDisplay.getDefineCollapse().getElement().setClassName("panel-collapse collapse in");
//		} else if(menuClicked.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FUNCTION_MENU)){
//			searchDisplay.getFunctionLibrary().setActive(true);
//			searchDisplay.getFuncNameListBox().setSelectedIndex(-1);
//			searchDisplay.getFunctionCollapse().getElement().setClassName("panel-collapse collapse in");
//		} else if(menuClicked.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEW_MENU)){
//			searchDisplay.getViewCQL().setActive(true);
//		}
//	}
	

	
	/**
	 * Method to build View for Anchor List item View CQL.
	 */
	private void buildCQLView() {
		
		MatContext.get().getMeasureService().getCQLFileData(MatContext.get().getCurrentMeasureId(), new AsyncCallback<SaveUpdateCQLResult>() {
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if(result.isSuccess()){
					if((result.getCqlString() != null) && !result.getCqlString().isEmpty()){
						searchDisplay.getCqlAceEditor().setText(result.getCqlString());
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
	 */
	private HTML getMsgPanel(IconType iconType, String message) {
		Icon checkIcon = new Icon(iconType);
		HTML msgHtml = new HTML(checkIcon + " <b>"+ message +"</b>");
		return msgHtml;
	}
	
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
					searchDisplay.setAppliedQdmList(result.getQualityDataDTO());
				}
				
			});
		}
	}
	
	
	public ViewDisplay getSearchDisplay() {
		
		return searchDisplay;
	}
	
}
