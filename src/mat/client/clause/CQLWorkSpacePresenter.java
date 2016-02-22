package mat.client.clause;

import java.util.HashMap;
import java.util.List;
import mat.client.MatPresenter;
import mat.client.shared.MatContext;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLParameter;
import mat.shared.SaveUpdateCQLResult;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
		 * Gets the context toggle switch.
		 *
		 * @return the context toggle switch
		 */
		//ToggleSwitch getContextToggleSwitch();
		
		/**
		 * Builds the cql file view.
		 */
		void buildCQLFileView();
		
		//ToggleSwitch getContextPOPToggleSwitch();
		
		//ToggleSwitch getContextPATToggleSwitch();
		
		/**
		 * Gets the context pat button.
		 *
		 * @return the context pat button
		 */
		Button getContextPatButton();
		
		/**
		 * Gets the context pop button.
		 *
		 * @return the context pop button
		 */
		Button getContextPopButton();
		
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
		
		Button getSaveFunctionButton();
		
		AceEditor getFunctionBodyAceEditor();
		
		void updateFunctionMap();
		
		String getCurrentSelectedFunctionObjId();
		
		HashMap<String, CQLFunctions> getFunctionMap();
		
		List<CQLFunctions> getViewFunctions();
		
		void setViewFunctions(List<CQLFunctions> viewFunctions);
		
		Alert getSuccessMessageAlertFunction();
		
		Alert getErrorMessageAlertFunction();
		
		void setCurrentSelectedFunctionObjId(String currentSelectedFunctionObjId);
		
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
		
		
		/*searchDisplay.getSaveCQLGeneralInfoBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				saveAndModifyCQLGeneralInfo();
			}
		});*/
		
		searchDisplay.getSaveFunctionButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				addAndModifyFunction();
			}
		});
		
		searchDisplay.getDefineNameTxtArea().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				resetMessageDisplay();
				
			}
		});
		
		
		searchDisplay.getParameterNameTxtArea().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				resetMessageDisplay();
				
			}
		});
		
		/*searchDisplay.getContextToggleSwitch().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				
				resetMessageDisplay();
			}
		});*/
		
		
		searchDisplay.getDefineAceEditor().getSelection().addSelectionListener(new AceSelectionListener() {
			
			@Override
			public void onChangeSelection(AceSelection selection) {
				resetMessageDisplay();
				
			}
		});
		
		
		searchDisplay.getParameterAceEditor().getSelection().addSelectionListener(new AceSelectionListener() {
			
			@Override
			public void onChangeSelection(AceSelection selection) {
				resetMessageDisplay();
				
			}
		});
		
		searchDisplay.getContextPatButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getContextPatButton().setEnabled(false);
				searchDisplay.getContextPatButton().setType(ButtonType.SUCCESS);
				searchDisplay.getContextPopButton().setEnabled(true);
				searchDisplay.getContextPopButton().setType(ButtonType.PRIMARY);
			}
		});
		
		
		searchDisplay.getContextPopButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getContextPatButton().setEnabled(true);
				searchDisplay.getContextPatButton().setType(ButtonType.PRIMARY);
				searchDisplay.getContextPopButton().setEnabled(false);
				searchDisplay.getContextPopButton().setType(ButtonType.SUCCESS);
			}
		});
		
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
		if (!definitionName.isEmpty() && !definitionLogic.isEmpty()) {
			
			final CQLDefinition define = new CQLDefinition();
			define.setDefinitionName(definitionName);
			define.setDefinitionLogic(definitionLogic);
			
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
		searchDisplay.setClickedMenu("general");
		panel.clear();
		searchDisplay.getMainPanel().clear();
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		getCQLData();
		searchDisplay.buildView();
		addHandler();
		setWidgetsReadOnly(MatContext.get().getMeasureLockService()
				.checkForEditPermission());
		panel.add(searchDisplay.getMainPanel());
	}
	
	
	/**
	 * Sets the widgets read only if the Measure is Locked
	 * and ReadOnly.
	 *
	 * @param editable the new widgets read only
	 */
	private void setWidgetsReadOnly(boolean editable) {
		
		/*searchDisplay.getContextToggleSwitch().setEnabled(true);
		searchDisplay.getSaveCQLGeneralInfoBtn().setEnabled(editable);
		 */
		searchDisplay.getParameterNameTxtArea().setEnabled(editable);
		searchDisplay.getParameterAceEditor().setReadOnly(!editable);
		searchDisplay.getAddParameterButton().setEnabled(editable);
		
		searchDisplay.getDefineNameTxtArea().setEnabled(editable);
		searchDisplay.getDefineAceEditor().setReadOnly(!editable);
		searchDisplay.getAddDefineButton().setEnabled(editable);
		
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
				
			}
		});
		searchDisplay.getDefinitionLibrary().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				unsetActiveMenuItem(clickedMenu);
				searchDisplay.getDefinitionLibrary().setActive(true);
				clickedMenu = "define";
				searchDisplay.buildDefinitionLibraryView();
				
			}
		});
		
		searchDisplay.getFunctionLibrary().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				unsetActiveMenuItem(clickedMenu);
				searchDisplay.getFunctionLibrary().setActive(true);
				clickedMenu = "func";
				searchDisplay.buildFunctionLibraryView();
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
