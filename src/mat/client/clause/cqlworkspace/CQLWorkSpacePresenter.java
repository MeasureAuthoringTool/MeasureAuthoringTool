package mat.client.clause.cqlworkspace;

import java.util.HashMap;
import java.util.List;

import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.toggleswitch.client.ui.ToggleSwitch;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceSelection;
import edu.ycp.cs.dh.acegwt.client.ace.AceSelectionListener;
import mat.client.MatPresenter;
import mat.client.clause.cqlworkspace.CQLWorkSpaceView.Observer;
import mat.client.shared.CQLSaveDeleteEraseButtonBar;
import mat.client.shared.MatContext;
import mat.client.shared.WarningMessageAlert;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLModel;
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
	String clickedMenu = "general";
	
	/** The view. */
	String view ="";
	/**
	 * The Interface ViewDisplay.
	 */
	interface ViewDisplay {
		
		/**
		 * Top Main panel of CQL Workspace Tab.
		 * @return HorizontalPanel
		 */
		HorizontalPanel getMainPanel();
		
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
		 * Gets the parameter txt area.
		 *
		 * @return the parameter txt area
		 */
		AceEditor getParameterTxtArea();
		
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
		void setCurrentSelectedDefinitionObjId(
				String currentSelectedDefinitionObjId);
		
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
		 * Gets the save cql general info btn.
		 *
		 * @return the save cql general info btn
		 */
		//Button getSaveCQLGeneralInfoBtn();
		
		/**
		 * Gets the success message alert.
		 *
		 * @return the success message alert
		 */
		Alert getSuccessMessageAlert();
		
		/**
		 * Sets the success message alert.
		 *
		 * @param successMessageAlert the new success message alert
		 */
		void setSuccessMessageAlert(Alert successMessageAlert);
		
		/**
		 * Gets the error message alert gen info.
		 *
		 * @return the error message alert gen info
		 */
		Alert getErrorMessageAlertGenInfo();
		
		/**
		 * Gets the success message alert definition.
		 *
		 * @return the success message alert definition
		 */
		Alert getSuccessMessageAlertDefinition();
		
		/**
		 * Gets the error message alert definition.
		 *
		 * @return the error message alert definition
		 */
		Alert getErrorMessageAlertDefinition();
		
		/**
		 * Gets the success message alert parameter.
		 *
		 * @return the success message alert parameter
		 */
		Alert getSuccessMessageAlertParameter();
		
		/**
		 * Gets the error message alert parameter.
		 *
		 * @return the error message alert parameter
		 */
		Alert getErrorMessageAlertParameter();
		
		/**
		 * Builds the cql file view.
		 */
		void buildCQLFileView();
		
		/**
		 * Sets the checks if is parameter dirty.
		 *
		 * @param isParameterDirty the new checks if is parameter dirty
		 */
		void setIsParameterDirty(Boolean isParameterDirty);
		
		/**
		 * Gets the checks if is parameter dirty.
		 *
		 * @return the checks if is parameter dirty
		 */
		Boolean getIsParameterDirty();
		
		/**
		 * Sets the checks if is definition dirty.
		 *
		 * @param isDefinitionDirty the new checks if is definition dirty
		 */
		void setIsDefinitionDirty(Boolean isDefinitionDirty);
		
		/**
		 * Gets the checks if is definition dirty.
		 *
		 * @return the checks if is definition dirty
		 */
		Boolean getIsDefinitionDirty();
		
		/**
		 * Gets the warning message alert parameter.
		 *
		 * @return the warning message alert parameter
		 */
		WarningMessageAlert getWarningMessageAlertParameter();
		
		/**
		 * Gets the warning message alert definition.
		 *
		 * @return the warning message alert definition
		 */
		WarningMessageAlert getWarningMessageAlertDefinition();
		
		/**
		 * Gets the clear parameter yes button.
		 *
		 * @return the clear parameter yes button
		 */
		Button getClearParameterYesButton();
		
		/**
		 * Gets the clear parameter no button.
		 *
		 * @return the clear parameter no button
		 */
		Button getClearParameterNoButton();
		
		/**
		 * Gets the clear definition yes button.
		 *
		 * @return the clear definition yes button
		 */
		Button getClearDefinitionYesButton();
		
		/**
		 * Gets the clear definition no button.
		 *
		 * @return the clear definition no button
		 */
		Button getClearDefinitionNoButton();
		
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
		 * Gets the success message alert function.
		 *
		 * @return the success message alert function
		 */
		Alert getSuccessMessageAlertFunction();
		
		/**
		 * Gets the error message alert function.
		 *
		 * @return the error message alert function
		 */
		Alert getErrorMessageAlertFunction();
		
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
		ToggleSwitch getContextPATToggleSwitch();
		
		/**
		 * Gets the context pop toggle switch.
		 *
		 * @return the context pop toggle switch
		 */
		ToggleSwitch getContextPOPToggleSwitch();
		
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
		
		List<CQLFunctionArgument> getFunctionArgumentList();
		
		void setFunctionArgumentList(List<CQLFunctionArgument> functionArgumentList);
		
	}
	
	/** The search display. */
	ViewDisplay searchDisplay;
	
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
	 */
	private void addEventHandlers() {
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
		
		searchDisplay.getDefineNameTxtArea().addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				resetMessageDisplay();
				searchDisplay.setIsDefinitionDirty(true);
			}
		});
		
		
		searchDisplay.getParameterNameTxtArea().addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				resetMessageDisplay();
				searchDisplay.setIsParameterDirty(true);
			}
		});
		
		
		searchDisplay.getDefineAceEditor().getSelection().addSelectionListener(new AceSelectionListener() {
			
			@Override
			public void onChangeSelection(AceSelection selection) {
				resetMessageDisplay();
				searchDisplay.setIsDefinitionDirty(true);
				
			}
		});
		
		
		searchDisplay.getParameterAceEditor().getSelection().addSelectionListener(new AceSelectionListener() {
			
			@Override
			public void onChangeSelection(AceSelection selection) {
				resetMessageDisplay();
				searchDisplay.setIsParameterDirty(true);
			}
		});
		
		searchDisplay.getEraseDefineButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (searchDisplay.getIsDefinitionDirty()) {
					//searchDisplay.getWarningMessageAlertDefinition().clear();
					//searchDisplay.getWarningMessageAlertDefinition().add(getMsgPanel(IconType.WARNING, MatContext.get().getMessageDelegate().getSaveErrorMsg()));
					searchDisplay.getWarningMessageAlertDefinition().turnOnWarningAlert();
				} else {
					clearDefinition();
				}
			}
		});
		
		searchDisplay.getEraseParameterButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (searchDisplay.getIsParameterDirty()) {
					//searchDisplay.getWarningMessageAlertParameter().clear();
					//searchDisplay.getWarningMessageAlertParameter().add(getMsgPanel(IconType.WARNING, MatContext.get().getMessageDelegate().getSaveErrorMsg()));
					searchDisplay.getWarningMessageAlertParameter().turnOnWarningAlert();
				} else {
					clearParameter();
				}
			}
		});
		
		searchDisplay.getClearParameterYesButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearParameter();
				searchDisplay.setIsParameterDirty(false);
				//searchDisplay.getWarningMessageAlertParameter().clear();
				searchDisplay.getWarningMessageAlertParameter().turnOffWarningAlert();
			}
		});
		
		searchDisplay.getClearParameterNoButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//searchDisplay.getWarningMessageAlertParameter().clear();
				searchDisplay.getWarningMessageAlertParameter().turnOffWarningAlert();
			}
		});
		
		searchDisplay.getClearDefinitionYesButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearDefinition();
				searchDisplay.setIsDefinitionDirty(false);
				//searchDisplay.getWarningMessageAlertDefinition().clear();
				searchDisplay.getWarningMessageAlertDefinition().turnOffWarningAlert();
			}
		});
		
		searchDisplay.getClearDefinitionNoButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//searchDisplay.getWarningMessageAlertDefinition().clear();
				searchDisplay.getWarningMessageAlertDefinition().turnOffWarningAlert();
			}
		});
		
		
		searchDisplay.getAddNewArgument().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				CQLModel currentModel = new CQLModel();
				currentModel.setCqlParameters(searchDisplay.getViewParameterList());
				currentModel.setDefinitionList(searchDisplay.getViewDefinitions());
				CQLFunctionArgument addNewFunctionArgument = new CQLFunctionArgument();
				AddFunctionArgumentDialogBox.showArgumentDialogBox(currentModel, addNewFunctionArgument , false);
			}
		});
		
		
		searchDisplay.getContextPATToggleSwitch().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				
				if(searchDisplay.getContextPATToggleSwitch().getValue()){
					searchDisplay.getContextPOPToggleSwitch().setValue(false);
				} else {
					searchDisplay.getContextPOPToggleSwitch().setValue(true);
				}
				
			}
		});
		
		searchDisplay.getContextPOPToggleSwitch().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(searchDisplay.getContextPOPToggleSwitch().getValue()){
					searchDisplay.getContextPATToggleSwitch().setValue(false);
				} else {
					searchDisplay.getContextPATToggleSwitch().setValue(true);
				}
				
			}
		});
		
	}
	
	/**
	 * Adds the observer handler.
	 */
	private void addObserverHandler() {
		searchDisplay.setObserver(new CQLWorkSpaceView.Observer() {
			
			@Override
			public void onModifyClicked(CQLFunctionArgument result) {
				CQLModel currentModel = new CQLModel();
				currentModel.setCqlParameters(searchDisplay.getViewParameterList());
				currentModel.setDefinitionList(searchDisplay.getViewDefinitions());
				
				AddFunctionArgumentDialogBox.showArgumentDialogBox(currentModel,result,true);
				
			}
			
			@Override
			public void onDeleteClicked(CQLFunctionArgument result, int index) {
				Window.alert("Delete Clicked for argument name : "+ result.getArgumentName());
				
			}
			
		});
	}
	
	/**
	 * Clear parameter.
	 */
	private void clearParameter() {
		searchDisplay.setCurrentSelectedParamerterObjId(null);
		if ((searchDisplay.getParameterAceEditor().getText()!= null) ||
				(searchDisplay.getParameterNameTxtArea() != null)	) {
			searchDisplay.getParameterNameTxtArea().clear();
			searchDisplay.getParameterAceEditor().setText("");
		}
	}
	
	/**
	 * Clear definition.
	 */
	private void clearDefinition() {
		searchDisplay.setCurrentSelectedDefinitionObjId(null);
		if ((searchDisplay.getDefineAceEditor().getText()!= null) ||
				(searchDisplay.getDefineNameTxtArea() != null)	) {
			searchDisplay.getDefineAceEditor().setText("");
			searchDisplay.getDefineNameTxtArea().clear();
		}
	}
	
	
	/**
	 * Save and modify cql general info.
	 */
	/*private void saveAndModifyCQLGeneralInfo(){
		resetMessageDisplay();
		String context = "";
		if(searchDisplay.getContextToggleSwitch().getValue()){
			context = "Patient";
		} else {
			context = "Population";
		}
		
		MatContext.get().getMeasureService().saveAndModifyCQLGeneralInfo(MatContext.get().getCurrentMeasureId(), context, new AsyncCallback<SaveUpdateCQLResult>() {
			
			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageAlertGenInfo().setVisible(true);
				searchDisplay.getErrorMessageAlertGenInfo().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
						MatContext.get().getMessageDelegate().getGenericErrorMessage()));
			}
			
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				searchDisplay.getSuccessMessageAlert().clear();
				if(result.isSuccess()){
					
					if(result.getCqlModel().getContext().equalsIgnoreCase("Patient")){
						searchDisplay.getContextToggleSwitch().setValue(true);
					} else {
						searchDisplay.getContextToggleSwitch().setValue(false);
					}
					searchDisplay.getSuccessMessageAlert().setVisible(true);
					searchDisplay.getSuccessMessageAlert().add(getMsgPanel(IconType.CHECK_CIRCLE,
							MatContext.get().getMessageDelegate().getSUCCESSFUL_SAVED_CQL_GEN_INFO()));
				}
				
			}
		});
	}*/
	
	
	
	
	
	protected void addAndModifyFunction() {
		resetMessageDisplay();
		String functionName = searchDisplay.getFuncNameTxtArea().getText();
		String functionBody = searchDisplay.getFunctionBodyAceEditor().getText();
		if (!functionName.isEmpty() && !functionBody.isEmpty()) {
			CQLFunctions function = new CQLFunctions();
			function.setFunctionLogic(functionBody);
			function.setFunctionName(functionName);
			if(searchDisplay.getCurrentSelectedFunctionObjId() != null){
				CQLFunctions toBeModifiedParamObj = searchDisplay.getFunctionMap().get(searchDisplay.getCurrentSelectedFunctionObjId());
				MatContext.get().getMeasureService().saveAndModifyFunctions(MatContext.get().getCurrentMeasureId(), toBeModifiedParamObj, function,
						searchDisplay.getViewFunctions(), new AsyncCallback<SaveUpdateCQLResult>() {
					
					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getErrorMessageAlertFunction().setVisible(true);
						searchDisplay.getErrorMessageAlertFunction().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
								MatContext.get().getMessageDelegate().getGenericErrorMessage()));
						
					}
					
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						//searchDisplay.setCurrentSelectedParamerterObjId(null);
						if (result.isSuccess()) {
							searchDisplay.setViewFunctions(result.getCqlModel().getCqlFunctions());
							searchDisplay.clearAndAddFunctionsNamesToListBox();
							searchDisplay.updateFunctionMap();
							searchDisplay.getSuccessMessageAlertFunction().setVisible(true);
							searchDisplay.getSuccessMessageAlertFunction().add(getMsgPanel(IconType.CHECK_CIRCLE,
									MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_MODIFY()));
							searchDisplay.getFuncNameTxtArea().setText(result.getFunction().getFunctionName());
							
						} else {
							if (result.getFailureReason() == 1) {
								searchDisplay.getErrorMessageAlertFunction().setVisible(true);
								searchDisplay.getErrorMessageAlertFunction().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
										MatContext.get().getMessageDelegate().getERROR_DUPLICATE_FUNCTION_NAME()));
							} else {
								if (result.getFailureReason() == 2) {
									searchDisplay.getErrorMessageAlertFunction().setVisible(true);
									searchDisplay.getErrorMessageAlertFunction().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
											"Unable to find Node to modify."));
								}
							}
						}
						
						
					}
				});
			} else {
				MatContext.get().getMeasureService().saveAndModifyFunctions(MatContext.get().getCurrentMeasureId(), null, function,
						searchDisplay.getViewFunctions(), new AsyncCallback<SaveUpdateCQLResult>() {
					
					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getErrorMessageAlertFunction().setVisible(true);
						searchDisplay.getErrorMessageAlertFunction().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
								MatContext.get().getMessageDelegate().getGenericErrorMessage()));
						
					}
					
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							searchDisplay.setViewFunctions(result.getCqlModel().getCqlFunctions());
							searchDisplay.clearAndAddFunctionsNamesToListBox();
							searchDisplay.updateFunctionMap();
							searchDisplay.getFuncNameTxtArea().setText(result.getFunction().getFunctionName());
							
							searchDisplay.getSuccessMessageAlertFunction().setVisible(true);
							searchDisplay.getSuccessMessageAlertFunction().add(getMsgPanel(IconType.CHECK_CIRCLE,
									MatContext.get().getMessageDelegate().getSUCCESSFUL_SAVED_CQL_FUNCTIONS()));
							
						} else {
							if (result.getFailureReason() == 1) {
								searchDisplay.getErrorMessageAlertFunction().setVisible(true);
								searchDisplay.getErrorMessageAlertFunction().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
										MatContext.get().getMessageDelegate().getERROR_DUPLICATE_FUNCTION_NAME()));
							} else {
								if (result.getFailureReason() == 2) {
									searchDisplay.getErrorMessageAlertFunction().setVisible(true);
									searchDisplay.getErrorMessageAlertFunction().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
											"Missing Functions Tag."));
								}
							}
						}
						
					}
					
				});
			}
			
		} else {
			searchDisplay.getErrorMessageAlertFunction().setVisible(true);
			searchDisplay.getErrorMessageAlertFunction().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
					MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_FUNCTION()));
		}
		
	}
	
	
	/**
	 * Adds the and modify parameters.
	 */
	private void addAndModifyParameters() {
		resetMessageDisplay();
		String parameterName = searchDisplay.getParameterNameTxtArea().getText();
		String parameterLogic = searchDisplay.getParameterAceEditor().getText();
		if (!parameterName.isEmpty() && !parameterLogic.isEmpty()) {
			CQLParameter parameter = new CQLParameter();
			parameter.setParameterLogic(parameterLogic);
			parameter.setParameterName(parameterName);
			if(searchDisplay.getCurrentSelectedParamerterObjId() != null){
				CQLParameter toBeModifiedParamObj = searchDisplay.getParameterMap().get(searchDisplay.getCurrentSelectedParamerterObjId());
				MatContext.get().getMeasureService().saveAndModifyParameters(MatContext.get().getCurrentMeasureId(), toBeModifiedParamObj, parameter,
						searchDisplay.getViewParameterList(), new AsyncCallback<SaveUpdateCQLResult>() {
					
					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getErrorMessageAlertParameter().setVisible(true);
						searchDisplay.getErrorMessageAlertParameter().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
								MatContext.get().getMessageDelegate().getGenericErrorMessage()));
						
					}
					
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						//searchDisplay.setCurrentSelectedParamerterObjId(null);
						if (result.isSuccess()) {
							searchDisplay.setViewParameterList(result.getCqlModel().getCqlParameters());
							searchDisplay.clearAndAddParameterNamesToListBox();
							searchDisplay.updateParamMap();
							searchDisplay.getSuccessMessageAlertParameter().setVisible(true);
							searchDisplay.getSuccessMessageAlertParameter().add(getMsgPanel(IconType.CHECK_CIRCLE,
									MatContext.get().getMessageDelegate().getSUCESS_PARAMETER_MODIFY()));
							searchDisplay.getParameterNameTxtArea().setText(result.getParameter().getParameterName());
							searchDisplay.setIsParameterDirty(false);
						} else {
							if (result.getFailureReason() == 1) {
								searchDisplay.getErrorMessageAlertParameter().setVisible(true);
								searchDisplay.getErrorMessageAlertParameter().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
										MatContext.get().getMessageDelegate().getERROR_DUPLICATE_PARAMETER_NAME()));
							} else {
								if (result.getFailureReason() == 2) {
									searchDisplay.getErrorMessageAlertParameter().setVisible(true);
									searchDisplay.getErrorMessageAlertParameter().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
											"Unable to find Node to modify."));
								}
							}
						}
						
						
					}
				});
			} else {
				MatContext.get().getMeasureService().saveAndModifyParameters(MatContext.get().getCurrentMeasureId(), null, parameter,
						searchDisplay.getViewParameterList(), new AsyncCallback<SaveUpdateCQLResult>() {
					
					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getErrorMessageAlertParameter().setVisible(true);
						searchDisplay.getErrorMessageAlertParameter().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
								MatContext.get().getMessageDelegate().getGenericErrorMessage()));
						
					}
					
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							searchDisplay.setViewParameterList(result.getCqlModel().getCqlParameters());
							searchDisplay.clearAndAddParameterNamesToListBox();
							searchDisplay.updateParamMap();
							searchDisplay.getParameterNameTxtArea().setText(result.getParameter().getParameterName());
							
							searchDisplay.getSuccessMessageAlertParameter().setVisible(true);
							searchDisplay.getSuccessMessageAlertParameter().add(getMsgPanel(IconType.CHECK_CIRCLE,
									MatContext.get().getMessageDelegate().getSUCCESSFUL_SAVED_CQL_PARAMETER()));
							searchDisplay.setIsParameterDirty(false);
						} else {
							if (result.getFailureReason() == 1) {
								searchDisplay.getErrorMessageAlertParameter().setVisible(true);
								searchDisplay.getErrorMessageAlertParameter().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
										MatContext.get().getMessageDelegate().getERROR_DUPLICATE_PARAMETER_NAME()));
							} else {
								if (result.getFailureReason() == 2) {
									searchDisplay.getErrorMessageAlertParameter().setVisible(true);
									searchDisplay.getErrorMessageAlertParameter().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
											"Missing Parameters Tag."));
								}
							}
						}
						
					}
					
				});
			}
			
		} else {
			searchDisplay.getErrorMessageAlertParameter().setVisible(true);
			searchDisplay.getErrorMessageAlertParameter().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
					MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_PARAMETER()));
		}
		
	}
	
	/**
	 * This method is called to Add/Modify Definitions into Measure Xml.
	 * 
	 */
	private void addAndModifyDefintions() {
		resetMessageDisplay();
		String definitionName = searchDisplay.getDefineNameTxtArea().getText();
		String definitionLogic = searchDisplay.getDefineAceEditor().getText();
		String defineContext = "";
		if(searchDisplay.getContextPATToggleSwitch().getValue()){
			defineContext = "Patient";
		} else {
			defineContext = "Population";
		}
		if (!definitionName.isEmpty() && !definitionLogic.isEmpty()) {
			
			final CQLDefinition define = new CQLDefinition();
			define.setDefinitionName(definitionName);
			define.setDefinitionLogic(definitionLogic);
			define.setContext(defineContext);
			
			if(searchDisplay.getCurrentSelectedDefinitionObjId() != null){
				CQLDefinition toBeModifiedObj = searchDisplay.getDefinitionMap()
						.get(searchDisplay.getCurrentSelectedDefinitionObjId());
				
				MatContext.get().getMeasureService().saveAndModifyDefinitions(MatContext.get().getCurrentMeasureId(),
						toBeModifiedObj, define,searchDisplay.getViewDefinitions(), new AsyncCallback<SaveUpdateCQLResult>() {
					
					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.setCurrentSelectedDefinitionObjId(null);
						searchDisplay.getErrorMessageAlertParameter().setVisible(true);
						searchDisplay.getErrorMessageAlertParameter().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
								MatContext.get().getMessageDelegate().getGenericErrorMessage()));
					}
					
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						//	searchDisplay.setCurrentSelectedDefinitionObjId(null);
						if(result.isSuccess()){
							
							searchDisplay.setViewDefinitions(result.getCqlModel().getDefinitionList());
							searchDisplay.clearAndAddDefinitionNamesToListBox();
							searchDisplay.updateDefineMap();
							searchDisplay.getSuccessMessageAlertDefinition().setVisible(true);
							searchDisplay.getSuccessMessageAlertDefinition().add(getMsgPanel(IconType.CHECK_CIRCLE,
									MatContext.get().getMessageDelegate().getSUCESS_DEFINITION_MODIFY()));
							searchDisplay.getDefineNameTxtArea().setText(result.getDefinition().getDefinitionName());
							searchDisplay.setIsDefinitionDirty(false);
						} else {
							if(result.getFailureReason() ==1){
								searchDisplay.getErrorMessageAlertDefinition().setVisible(true);
								searchDisplay.getErrorMessageAlertDefinition().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
										MatContext.get().getMessageDelegate().getERROR_DUPLICATE_DEFINITION_NAME()));
							} else if(result.getFailureReason() == 2){
								searchDisplay.getErrorMessageAlertDefinition().setVisible(true);
								searchDisplay.getErrorMessageAlertDefinition().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
										"Unable to find Node to modify."));
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
						searchDisplay.getErrorMessageAlertParameter().setVisible(true);
						searchDisplay.getErrorMessageAlertParameter().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
								MatContext.get().getMessageDelegate().getGenericErrorMessage()));
						
					}
					
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							searchDisplay.setViewDefinitions(result.getCqlModel().getDefinitionList());
							searchDisplay.clearAndAddDefinitionNamesToListBox();
							searchDisplay.updateDefineMap();
							searchDisplay.getSuccessMessageAlertDefinition().setVisible(true);
							searchDisplay.getSuccessMessageAlertDefinition().add(getMsgPanel(IconType.CHECK_CIRCLE,
									MatContext.get().getMessageDelegate().getSUCCESSFUL_SAVED_CQL_DEFINITION()));
							searchDisplay.getDefineNameTxtArea().setText(result.getDefinition().getDefinitionName());
							searchDisplay.setIsDefinitionDirty(false);
						} else {
							
							if (result.getFailureReason() == 1) {
								searchDisplay.getErrorMessageAlertDefinition().setVisible(true);
								searchDisplay.getErrorMessageAlertDefinition().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
										MatContext.get().getMessageDelegate().getERROR_DUPLICATE_DEFINITION_NAME()));
							} else if(result.getFailureReason() == 2){
								searchDisplay.getErrorMessageAlertDefinition().setVisible(true);
								searchDisplay.getErrorMessageAlertDefinition().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
										"Missing Definitions Tag."));
							}
						}
						/*searchDisplay.getDefineNameTxtArea().clear();
						searchDisplay.getDefineAceEditor().setText("");;*/
						
					}
				});
				
			}
		} else {
			searchDisplay.getErrorMessageAlertDefinition().setVisible(true);
			searchDisplay.getErrorMessageAlertDefinition().add(getMsgPanel(IconType.EXCLAMATION_CIRCLE,
					MatContext.get().getMessageDelegate().getERROR_SAVE_CQL_DEFINITION()));
		}
		
	}
	
	
	
	/**
	 * Reset message display.
	 */
	private void resetMessageDisplay() {
		
		searchDisplay.getSuccessMessageAlert().clear();
		searchDisplay.getSuccessMessageAlert().setVisible(false);
		
		searchDisplay.getErrorMessageAlertGenInfo().clear();
		searchDisplay.getErrorMessageAlertGenInfo().setVisible(false);
		
		searchDisplay.getSuccessMessageAlertDefinition().clear();
		searchDisplay.getSuccessMessageAlertDefinition().setVisible(false);
		
		searchDisplay.getSuccessMessageAlertParameter().clear();
		searchDisplay.getSuccessMessageAlertParameter().setVisible(false);
		
		searchDisplay.getErrorMessageAlertDefinition().clear();
		searchDisplay.getErrorMessageAlertDefinition().setVisible(false);
		
		searchDisplay.getErrorMessageAlertParameter().clear();
		searchDisplay.getErrorMessageAlertParameter().setVisible(false);
		
		//searchDisplay.getWarningMessageAlertParameter().clear();
		searchDisplay.getWarningMessageAlertParameter().turnOffWarningAlert();
		
		//searchDisplay.getWarningMessageAlertDefinition().clear();
		searchDisplay.getWarningMessageAlertDefinition().turnOffWarningAlert();
		
		searchDisplay.getSuccessMessageAlertFunction().clear();
		searchDisplay.getSuccessMessageAlertFunction().setVisible(false);
		
		searchDisplay.getErrorMessageAlertFunction().clear();
		searchDisplay.getErrorMessageAlertFunction().setVisible(false);
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		searchDisplay.setCurrentSelectedDefinitionObjId(null);
		searchDisplay.setCurrentSelectedParamerterObjId(null);
		searchDisplay.setCurrentSelectedFunctionObjId(null);
		searchDisplay.getParamCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getDefineCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getFunctionCollapse().getElement().setClassName("panel-collapse collapse");
		clickedMenu = "general";
		panel.clear();
		searchDisplay.getMainPanel().clear();
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		clickedMenu = "general";
		getCQLData();
		searchDisplay.buildView();
		addHandler();
		panel.add(searchDisplay.getMainPanel());
	}
	
	
	/**
	 * Sets the parameter widget read only.
	 *
	 * @param isEditable the new parameter widget read only
	 */
	private void setParameterWidgetReadOnly(boolean isEditable){
		
		searchDisplay.getParameterNameTxtArea().setEnabled(isEditable);
		searchDisplay.getParameterAceEditor().setReadOnly(!isEditable);
		searchDisplay.getParameterButtonBar().setEnabled(isEditable);
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
		searchDisplay.getContextPATToggleSwitch().setEnabled(isEditable);
		searchDisplay.getContextPOPToggleSwitch().setEnabled(isEditable);
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
	private void addHandler() {
		searchDisplay.getGeneralInformation().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				unsetActiveMenuItem(clickedMenu);
				searchDisplay.getGeneralInformation().setActive(true);
				clickedMenu = "general";
				searchDisplay.buildGeneralInformation();
				
			}
		});
		
		searchDisplay.getParameterLibrary().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				unsetActiveMenuItem(clickedMenu);
				searchDisplay.getParameterLibrary().setActive(true);
				clickedMenu = "param";
				searchDisplay.buildParameterLibraryView();
				setParameterWidgetReadOnly(MatContext.get().getMeasureLockService()
						.checkForEditPermission());
				
			}
		});
		searchDisplay.getDefinitionLibrary().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				unsetActiveMenuItem(clickedMenu);
				searchDisplay.getDefinitionLibrary().setActive(true);
				clickedMenu = "define";
				searchDisplay.buildDefinitionLibraryView();
				boolean editable = MatContext.get().getMeasureLockService()
						.checkForEditPermission();
				
				searchDisplay.getContextPATToggleSwitch().setEnabled(editable);
				searchDisplay.getContextPOPToggleSwitch().setEnabled(editable);
				setDefinitionWidgetReadOnly(MatContext.get().getMeasureLockService()
						.checkForEditPermission());
				
			}
		});
		
		searchDisplay.getFunctionLibrary().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				unsetActiveMenuItem(clickedMenu);
				searchDisplay.getFunctionLibrary().setActive(true);
				clickedMenu = "func";
				searchDisplay.buildFunctionLibraryView();
				setFunctionWidgetReadOnly(MatContext.get().getMeasureLockService()
						.checkForEditPermission());
			}
		});
		
		searchDisplay.getViewCQL().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				unsetActiveMenuItem(clickedMenu);
				searchDisplay.getViewCQL().setActive(true);
				clickedMenu = "view";
				searchDisplay.buildCQLFileView();
				buildCQLView();
			}
			
		});
		
	}
	
	/**
	 * Method to unset Anchor List Item selection for previous selection and set for new selections.
	 *
	 * @param menuClickedBefore the menu clicked before
	 */
	private void unsetActiveMenuItem(String menuClickedBefore) {
		
		if(menuClickedBefore.equalsIgnoreCase("general")){
			searchDisplay.getGeneralInformation().setActive(false);
		} else if(menuClickedBefore.equalsIgnoreCase("param")){
			searchDisplay.getParameterLibrary().setActive(false);
			if(searchDisplay.getParamCollapse().getElement().getClassName().equalsIgnoreCase("panel-collapse collapse in")){
				searchDisplay.getParamCollapse().getElement().setClassName("panel-collapse collapse");
			}
		} else if(menuClickedBefore.equalsIgnoreCase("define")){
			searchDisplay.getDefinitionLibrary().setActive(false);
			if(searchDisplay.getDefineCollapse().getElement().getClassName().equalsIgnoreCase("panel-collapse collapse in")){
				searchDisplay.getDefineCollapse().getElement().setClassName("panel-collapse collapse");
			}
		} else if(menuClickedBefore.equalsIgnoreCase("func")){
			searchDisplay.getFunctionLibrary().setActive(false);
			if(searchDisplay.getFunctionCollapse().getElement().getClassName().equalsIgnoreCase("panel-collapse collapse in")){
				searchDisplay.getFunctionCollapse().getElement().setClassName("panel-collapse collapse");
			}
		} else if(menuClickedBefore.equalsIgnoreCase("view")){
			searchDisplay.getViewCQL().setActive(false);
		}
	}
	
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
		// TODO Auto-generated method stub
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
}
