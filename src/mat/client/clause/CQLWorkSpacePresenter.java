package mat.client.clause;

import java.util.HashMap;
import java.util.List;

import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.MatPresenter;
import mat.client.shared.MatContext;
import mat.model.cql.CQLDefinition;
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
	
	/** The current selected clause. */
	String currentSelectedClause = null;
	
	/** The cql model. */
	CQLModel cqlModel = null;
	
	/** The measure id. */
	String measureId = null;
	
	
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
		 * Button addViewButton.
		 */
		Button addCQLViewButton = new Button();
		Object mainFlowPanel = null;

		
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
		 * @return the clicked menu
		 */
		String getClickedMenu();
		
		/**
		 * Gets the current selected clause.
		 *
		 * @return the current selected clause
		 */
		String getCurrentSelectedClause();
		
		/**
		 * Sets the clicked menu.
		 *
		 * @param clickedMenu the new clicked menu
		 */
		void setClickedMenu(String clickedMenu);
		
		/**
		 * Sets the current selected clause.
		 *
		 * @param currentSelectedClause the new current selected clause
		 */
		void setCurrentSelectedClause(String currentSelectedClause);
		
		/**
		 * Gets the patient radio.
		 *
		 * @return the patient radio
		 */
		InlineRadio getPatientRadio();
		
		/**
		 * Gets the population radio.
		 *
		 * @return the population radio
		 */
		InlineRadio getPopulationRadio();
		
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
		Button getSaveCQLGeneralInfoBtn();

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

		Alert getErrorMessageAlertGenInfo();

		Alert getSuccessMessageAlertDefinition();

		Alert getErrorMessageAlertDefinition();

		Alert getSuccessMessageAlertParameter();

		Alert getErrorMessageAlertParameter();

		void unsetActiveMenuItem(String menuClickedBefore);

		FlowPanel getMainFlowPanel();

		void setMainFlowPanel(FlowPanel mainFlowPanel);
		
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
		measureId = MatContext.get().getCurrentMeasureId();
		searchDisplay.getAddDefineButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addAndModifyDefintions();
			}
		});
		
		
		searchDisplay.getAddParameterButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				/*searchDisplay.setParameterIntoList();
				saveCQLData();*/
				addAndModifyParameters();
			}
			
			
		});
		
		
		searchDisplay.getSaveCQLGeneralInfoBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				saveAndModifyCQLGeneralInfo();
			}
		});
		
//		searchDisplay.getViewCQL().addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				searchDisplay.unsetActiveMenuItem(clickedMenu);
//				searchDisplay.getViewCQL().setActive(true);
//				clickedMenu = "view";
//				buildCQLFileView();
//			}
//		});
		
		
	}
	
	
	/**
	 * Save and modify cql general info.
	 */
	private void saveAndModifyCQLGeneralInfo(){
		
		String context = "";
		if(searchDisplay.getPatientRadio().getValue()){
			context = "Patient";
		} else {
			context = "Population";
		} 
		
		MatContext.get().getMeasureService().saveAndModifyCQLGeneralInfo(MatContext.get().getCurrentMeasureId(), context, new AsyncCallback<SaveUpdateCQLResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				searchDisplay.getSuccessMessageAlert().clear();
				if(result.isSuccess()){
					
					if(result.getCqlModel().getContext().equalsIgnoreCase("Patient")){
						searchDisplay.getPatientRadio().setValue(true);
					} else {
						searchDisplay.getPopulationRadio().setValue(true);
					}
					searchDisplay.getSuccessMessageAlert().setVisible(true);
					/*Icon checkIcon = new Icon(IconType.CHECK_CIRCLE);
					HTML successtext = new HTML("<h5>" + checkIcon + " <b>"+ MatContext.get().getMessageDelegate().getSUCCESSFUL_QDM_REMOVE_MSG() +"</b> </h5>");*/
					searchDisplay.getSuccessMessageAlert().setText(MatContext.get().getMessageDelegate().getSUCCESSFUL_SAVED_CQL_GEN_INFO());
				} 
				
			}
		});
	}
	
	
	
	
	
	/**
	 * Adds the and modify parameters.
	 */
	private void addAndModifyParameters() {
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
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						searchDisplay.setCurrentSelectedParamerterObjId(null);
						if (result.isSuccess()) {
							searchDisplay.setViewParameterList(result.getCqlModel().getCqlParameters());
							searchDisplay.clearAndAddParameterNamesToListBox();
							searchDisplay.updateParamMap();
						} else {
							Window.alert(" "+ result.getFailureReason());
						}
						searchDisplay.getParameterNameTxtArea().clear();
						searchDisplay.getParameterAceEditor().setText("");
						
					}
				});
			} else {
				MatContext.get().getMeasureService().saveAndModifyParameters(MatContext.get().getCurrentMeasureId(), null, parameter,
						searchDisplay.getViewParameterList(), new AsyncCallback<SaveUpdateCQLResult>() {
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if (result.isSuccess()) {
							searchDisplay.setViewParameterList(result.getCqlModel().getCqlParameters());
							searchDisplay.clearAndAddParameterNamesToListBox();
							searchDisplay.updateParamMap();
						} else {
							Window.alert(" " + result.getFailureReason());
						}
						searchDisplay.getParameterNameTxtArea().clear();
						searchDisplay.getParameterAceEditor().setText("");
						
					}
					
				});
			}
			
		} else {
			// show Error Message.
		}
		
	}
	
	/**
	 * This method is called to Add/Modify Definitions into Measure Xml.
	 * 
	 */
	private void addAndModifyDefintions() {
		
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
					}
					
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						//searchDisplay.getViewDefinitions().add(define);
						searchDisplay.setCurrentSelectedDefinitionObjId(null);
						if(result.isSuccess()){
							searchDisplay.setViewDefinitions(result.getCqlModel().getDefinitionList());
							searchDisplay.clearAndAddDefinitionNamesToListBox();
							searchDisplay.updateDefineMap();
						} else {
							Window.alert(" "+result.getFailureReason());
						}
						searchDisplay.getDefineNameTxtArea().clear();
						searchDisplay.getDefineAceEditor().setText("");;
					}
				});
				
			} else {
				
				MatContext.get().getMeasureService().saveAndModifyDefinitions(MatContext.get().getCurrentMeasureId(), null, define,
						searchDisplay.getViewDefinitions(), new AsyncCallback<SaveUpdateCQLResult>() {
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						if(result.isSuccess()){
							searchDisplay.setViewDefinitions(result.getCqlModel().getDefinitionList());
							searchDisplay.clearAndAddDefinitionNamesToListBox();
							searchDisplay.updateDefineMap();
						} else {
							Window.alert(" "+result.getFailureReason());
						}
						searchDisplay.getDefineNameTxtArea().clear();
						searchDisplay.getDefineAceEditor().setText("");;
						
					}
				});
				
			}
		} else {
			// show Error Display message
		}
		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		cqlModel = null;
		searchDisplay.setCurrentSelectedDefinitionObjId(null);
		searchDisplay.setCurrentSelectedParamerterObjId(null);
		
		searchDisplay.getParamCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getDefineCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.setClickedMenu("general");
		panel.clear();
		searchDisplay.getMainPanel().clear();
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		cqlModel = new CQLModel();
		getCQLData();
		searchDisplay.buildView();
		addHandler();
		panel.add(searchDisplay.getMainPanel());
	}
	
	/**
	 * Gets the CQL data.
	 *
	 * @return the CQL data
	 */
	private void getCQLFileData(){
		MatContext.get().getMeasureService().getCQLFileData(MatContext.get().getCurrentMeasureId(), new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuccess(String cqlString) {
				if(cqlString != null){
					searchDisplay.getCqlAceEditor().setText(cqlString);
				}
			}
		});
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
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if(result.getCqlModel() != null){
					
					if(!result.getCqlModel().getContext().isEmpty()){
						if(result.getCqlModel().getContext().equalsIgnoreCase("Patient")){
							searchDisplay.getPatientRadio().setValue(true);
						} else {
							searchDisplay.getPopulationRadio().setValue(true);
						}
					}
					
					if (result.getCqlModel().getDefinitionList() != null &&
							result.getCqlModel().getDefinitionList().size() >0) {
						searchDisplay.setViewDefinitions(result.getCqlModel().getDefinitionList());
						searchDisplay.clearAndAddDefinitionNamesToListBox();
						searchDisplay.updateDefineMap();
					}
					if (result.getCqlModel().getCqlParameters() != null && 
							result.getCqlModel().getCqlParameters().size() >0) {
						searchDisplay.setViewParameterList(result.getCqlModel().getCqlParameters());
						searchDisplay.clearAndAddParameterNamesToListBox();
						searchDisplay.updateParamMap();
					}
				}
				
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
	
	private void addHandler() {
		searchDisplay.getGeneralInformation().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.unsetActiveMenuItem(clickedMenu);
				searchDisplay.getGeneralInformation().setActive(true);
				clickedMenu = "general";
				searchDisplay.buildGeneralInformation();
				
			}
		});
		
		searchDisplay.getParameterLibrary().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.unsetActiveMenuItem(clickedMenu);
				searchDisplay.getParameterLibrary().setActive(true);
				clickedMenu = "param";
				searchDisplay.buildParameterLibraryView();
				
			}
		});
		searchDisplay.getDefinitionLibrary().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.unsetActiveMenuItem(clickedMenu);
				searchDisplay.getDefinitionLibrary().setActive(true);
				clickedMenu = "define";
				searchDisplay.buildDefinitionLibraryView();
				
			}
		});
		
		searchDisplay.getFunctionLibrary().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.unsetActiveMenuItem(clickedMenu);
				searchDisplay.getFunctionLibrary().setActive(true);
				clickedMenu = "func";
				searchDisplay.buildFunctionLibraryView();
			}
		});
		
		searchDisplay.getViewCQL().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.unsetActiveMenuItem(clickedMenu);
				searchDisplay.getViewCQL().setActive(true);
				clickedMenu = "view";
				buildCQLFileView();
			}
		});
	}
	
	
	public void buildCQLFileView(){
		searchDisplay.getMainFlowPanel().clear();
		
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();
		
		searchDisplay.getCqlAceEditor().setMode(AceEditorMode.CQL);
		searchDisplay.getCqlAceEditor().setTheme(AceEditorTheme.ECLIPSE);
		searchDisplay.getCqlAceEditor().getElement().getStyle().setFontSize(14, Unit.PX);
		searchDisplay.getCqlAceEditor().setSize("500px", "500px");
		searchDisplay.getCqlAceEditor().setAutocompleteEnabled(true);
		
		parameterVP.add(searchDisplay.getCqlAceEditor());
		searchDisplay.getMainFlowPanel().add(parameterVP);
				
		parameterFP.add(parameterVP);
		parameterFP.setStyleName("cqlRightContainer");

		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("700px");
		vp.setHeight("500px");
		parameterFP.setWidth("700px");
		parameterFP.setStyleName("marginLeft15px");
		vp.add(parameterFP);

		getCQLFileData();
		//addCqlEventHandkers();
		searchDisplay.getMainFlowPanel().add(vp);	
		
		
	}

	
}
