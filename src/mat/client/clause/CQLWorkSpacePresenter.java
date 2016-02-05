package mat.client.clause;

import java.util.HashMap;
import java.util.List;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.TextArea;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.MatPresenter;
import mat.client.shared.MatContext;
import mat.model.cql.CQLDefinitionModelObject;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameterModelObject;

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
	CQLModel cqlModel = new CQLModel();
	
	
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
		 * Builds the view cql view.
		 */
		void buildViewCQLView();

		/**
		 * Update suggest oracle.
		 */
		void updateSuggestOracle();

		/**
		 * Clear and add parameter names to list box.
		 */
		void addParameterNamesToListBox();

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
		List<CQLParameterModelObject> getViewParameterList();

		/**
		 * Gets the parameter map.
		 *
		 * @return the parameter map
		 */
		HashMap<String, CQLParameterModelObject> getParameterMap();

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
		void addDefinitionNamesToListBox();

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
		HashMap<String, CQLDefinitionModelObject> getDefinitionMap();

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
		List<CQLDefinitionModelObject> getViewDefinitions();

		/**
		 * Sets the view definitions.
		 *
		 * @param viewDefinitions the new view definitions
		 */
		void setViewDefinitions(List<CQLDefinitionModelObject> viewDefinitions);

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
		 * Builds the cql view.
		 */
		void buildCQLView();

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

		void setParameterIntoList(String parameterName, String parameterLogic);

		void setDefinitionIntoList(String definitionName, String definitionLogic);
		
		void setCQLValuesInLists(CQLModel cqlModel);
		
		void unsetActiveMenuItem(String menuClickedBefore);

		AceEditor getCqlAceEditor();

		void setCqlAceEditor(AceEditor cqlAceEditor);
		
		void clearNameTxtAreas();
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
		
		
		
		searchDisplay.getViewCQL().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.unsetActiveMenuItem(clickedMenu);
				searchDisplay.getViewCQL().setActive(true);
				clickedMenu = "view";
				searchDisplay.getCqlAceEditor().setText(getCqlString());
				searchDisplay.buildViewCQLView();
			}
		});
		
		
		searchDisplay.getAddDefineButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String definitionName = searchDisplay.getDefineNameTxtArea().getText();
				String definitionLogic = searchDisplay.getDefineAceEditor().getText();
				searchDisplay.getDefineNameTxtArea().clear();
				searchDisplay.getDefineAceEditor().setText("");;
				searchDisplay.setDefinitionIntoList(definitionName,definitionLogic);
				saveCQLData();
			}
		});
		
		
		searchDisplay.getAddParameterButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String parameterName = searchDisplay.getParameterNameTxtArea().getText();
				String parameterLogic = searchDisplay.getParameterAceEditor().getText();
				searchDisplay.getParameterNameTxtArea().clear();
				searchDisplay.getParameterAceEditor().setText("");;
				searchDisplay.setParameterIntoList(parameterName, parameterLogic);
				saveCQLData();
			}
		});
		
		
	}


	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		searchDisplay.getParamCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.getDefineCollapse().getElement().setClassName("panel-collapse collapse");
		searchDisplay.setClickedMenu("general");
		searchDisplay.setCurrentSelectedClause(null);
		panel.clear();
		searchDisplay.getMainPanel().clear();

	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		searchDisplay.buildView();
		searchDisplay.clearNameTxtAreas();
		getCQLData();
		panel.add(searchDisplay.getMainPanel());

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
	 * Sets the cql data.
	 */
	private void setCQLData() {
		
		cqlModel.setCqlBuilder(getCqlString());
		cqlModel.setCqlParameters(searchDisplay.getViewParameterList());
		cqlModel.setDefinitionList(searchDisplay.getViewDefinitions());
		cqlModel.setMeasureId(MatContext.get().getCurrentMeasureId());
	}
	
	
	/**
	 * Gets the cql string.
	 *
	 * @return the cql string
	 */
	private String getCqlString(){
		
		StringBuilder cqlStr = new StringBuilder();
		//library Name
		cqlStr = cqlStr.append("library " + MatContext.get().getCurrentMeasureName().replaceAll(" ", "") + " version '2'");
		cqlStr = cqlStr.append("\n\n");
		//Using
		cqlStr = cqlStr.append("using QDM");
		cqlStr = cqlStr.append("\n\n");
		
		//parameter
		List<CQLParameterModelObject> paramList = searchDisplay.getViewParameterList();
		for(int i=0; i <paramList.size(); i++){
			
			cqlStr = cqlStr.append("parameter "+ paramList.get(i).getIdentifier() +"\t"+
					paramList.get(i).getTypeSpecifier());
			cqlStr = cqlStr.append("\n\n");			
		}
		
		//context
		String contextStr ="";
		if(searchDisplay.getPopulationRadio().getValue()){
			contextStr = "Population";
		} else {
			contextStr = "Patient";
		}
		
		cqlStr = cqlStr.append("context "+ contextStr +"\n\n");
		
				
		//define
		List<CQLDefinitionModelObject> defineList = searchDisplay.getViewDefinitions();
		for(int i=0; i <defineList.size(); i++){
			
			cqlStr = cqlStr.append("define "+ defineList.get(i).getIdentifier()+":\n");
			cqlStr = cqlStr.append(defineList.get(i).getExpression());
			cqlStr = cqlStr.append("\n\n");			
		}
		
		return cqlStr.toString();
	}
	
	
	/**
	 * Save cql data.
	 */
	public void saveCQLData(){
		setCQLData();
		MatContext.get().getMeasureService().saveCQLData(cqlModel, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
		
	/**
	 * Get CQL data.
	 */
	public void getCQLData(){
		
		//get the CQL file from the database
		MatContext.get().getMeasureService().getCQLData(MatContext.get().getCurrentMeasureId(), new AsyncCallback<CQLModel>() {
			
		@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub					
			}

			@Override
			public void onSuccess(CQLModel result) {
				//System.out.println("In CQLPresenterNavBarWithList getCqlData onSuccess (result).");
				//result.printCQL();
				if (result != null) {
					cqlModel = result;
					searchDisplay.setCQLValuesInLists(cqlModel);
				}

			}

		});	
		
		}

}
