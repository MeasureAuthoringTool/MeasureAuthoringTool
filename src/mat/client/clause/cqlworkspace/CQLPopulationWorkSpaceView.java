package mat.client.clause.cqlworkspace;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
//import org.gwtbootstrap3.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;

import mat.client.Mat;
import mat.client.shared.CQLAddNewButton;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;

/**
 * The Class CQLPopulationWorkSpaceView.
 * 
 * 
 */

public class CQLPopulationWorkSpaceView implements CQLPopulationWorkSpacePresenter.ViewDisplay {

	/** The main horizontal panel. */
	HorizontalPanel mainHPPanel = new HorizontalPanel();

	/** The main horizontal panel. */
	private VerticalPanel mainPanel = new VerticalPanel();

	/** The main v panel. */
	private VerticalPanel mainVPanel = new VerticalPanel();

	/** The main flow panel. */
	private FlowPanel mainFlowPanel = new FlowPanel();
	
	/** The clicked menu. */
	public String clickedMenu = "general";

	/** The clicked menu. */
	public String nextClickedMenu = "general";

	/** The vp. */
	VerticalPanel vp = new VerticalPanel();
	
	HTML heading = new HTML();
	HorizontalPanel headingPanel = new HorizontalPanel();

	/** The cql left nav bar panel view. */
	private CQLPopulationLeftNavBarPanelView cqlLeftNavBarPanelView;
	
	private PopulationDataModel populationDataModel = null;
	private Document document = null;
	
	/**
	 * Instantiates a new CQL work space view.
	 */
	public CQLPopulationWorkSpaceView() {
		cqlLeftNavBarPanelView = new CQLPopulationLeftNavBarPanelView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLNewPresenter.ViewDisplay#buildView()
	 */
	/**
	 * Builds the view.
	 */
	@Override
	public void buildView(Document document, PopulationDataModel populationDataModel) {
		resetAll();
		
		this.document = document;
		this.populationDataModel = populationDataModel;
		
		heading.addStyleName("leftAligned");
		headingPanel.getElement().setId("headingPanel");
		mainFlowPanel.setWidth("700px");
		mainPanel.getElement().setId("CQLPopulationWorkspaceView.containerPanel");		
		/*mainPanel.add(new SpacerWidget());*/
		mainPanel.add(headingPanel);
		mainPanel.add(cqlLeftNavBarPanelView.getMessagePanel());
		mainPanel.add(mainFlowPanel);
		resetMessageDisplay();

		mainHPPanel.addStyleName("cqlRightMessage");
		mainHPPanel.add(cqlLeftNavBarPanelView.buildMeasureLibCQLView(document));
		
		mainHPPanel.add(mainPanel);
		
		mainVPanel.add(mainHPPanel);
		mainVPanel.setStyleName("cqlRightContainer");
	}
	
	public void displayInitialPopulations() {
		/** The function add new button. */
		
		PopulationsObject initPopulationObject = this.populationDataModel.getInitialPopulationsObject();
		List<PopulationClauseObject> initPopClauses = this.populationDataModel.getInitialPopulationsObject().getPopulationClauseObjectList();
		showPopulation(initPopClauses, initPopulationObject);				
	}
	
	public void displayNumerators() {
		
		List<PopulationClauseObject> numeratorsClauses = this.populationDataModel.getNumeratorsObject().getPopulationClauseObjectList();
		showPopulation(numeratorsClauses, this.populationDataModel.getNumeratorsObject());				
	}

	private void showPopulation(List<PopulationClauseObject> popClauses, PopulationsObject populationObject) {
		
		mainFlowPanel.clear();
		Grid populationGrid = new Grid(popClauses.size(), 4);
		
		for(int i=0;i<popClauses.size();i++) {
			
			PopulationClauseObject populationClauseObject = popClauses.get(i);
			
			//set the name of the Initial Population clause.
			FormLabel nameLabel = new FormLabel();
			nameLabel.setText(populationClauseObject.getDisplayName());
			nameLabel.setTitle(populationClauseObject.getDisplayName());
			nameLabel.setMarginTop(15.00);
			nameLabel.setMarginLeft(10.00);
			nameLabel.setMarginRight(10.00);
			
			populationGrid.setWidget(i, 0, nameLabel);
			
			//Set a listbox with all definition names in it.
			ListBox definitionListBox = new ListBox();
			definitionListBox.setWidth("50");			
			
			for(String definitionName : this.populationDataModel.getDefinitionNameList()) {
				definitionListBox.addItem(definitionName, definitionName);
			}
			
			//select a definition name in the listbox
			for(int j=0;j<definitionListBox.getItemCount();j++) {
				String definitionName = definitionListBox.getItemText(j);
				if(definitionName.equals(populationClauseObject.getCqlDefinitionDisplayName())){
					definitionListBox.setItemSelected(j, true);
					break;
				}
			}
			
			populationGrid.setWidget(i, 1, definitionListBox);
			
			//button for Delete
			Button deleteButton = new Button();
			deleteButton.setType(ButtonType.LINK);
			deleteButton.getElement().setId("deleteButton_"+populationClauseObject.getDisplayName());
			deleteButton.setTitle("Delete");
			//deleteButton.setText("Delete");
			deleteButton.setSize("70px", "30px");
			deleteButton.setMarginTop(15.00);
			deleteButton.setMarginLeft(10.00);
			deleteButton.getElement().setAttribute("aria-label", "Delete");
			deleteButton.setIcon(IconType.TRASH);
			deleteButton.setIconSize(IconSize.LARGE);
			deleteButton.setColor("#0964A2");
			
			populationGrid.setWidget(i, 2, deleteButton);
			
			//button for View Human Readable
			Button viewHRButton = new Button();
			viewHRButton.setType(ButtonType.LINK);
			viewHRButton.getElement().setId("viewHRButton_"+populationClauseObject.getDisplayName());
			viewHRButton.setTitle("View Human Readable");
			//viewHRButton.setText("View Human Readable");
			viewHRButton.setSize("70px", "30px");
			viewHRButton.setMarginTop(15.00);
			viewHRButton.setMarginLeft(10.00);
			viewHRButton.getElement().setAttribute("aria-label", "View Human Readable");
			viewHRButton.setIcon(IconType.BINOCULARS);
			viewHRButton.setIconSize(IconSize.LARGE);
			viewHRButton.setColor("black");
			
			populationGrid.setWidget(i, 3, viewHRButton);
			
		}
				
		ScrollPanel scrollPanel = new ScrollPanel(populationGrid);
		
		mainFlowPanel.add(new SpacerWidget());
		CQLAddNewButton addNewButtonBar = new CQLAddNewButton(populationObject.getPopulationName());
		addNewButtonBar.getaddNewButton().setPull(Pull.LEFT);
		mainFlowPanel.add(addNewButtonBar);
		mainFlowPanel.add(new SpacerWidget());
		mainFlowPanel.add(scrollPanel);
		mainFlowPanel.add(new SpacerWidget());
	}
	
	
	
	
	/**
	 * Reset All components to default state.
	 */
	@Override
	public void resetAll() {
		mainFlowPanel.clear();
		headingPanel.clear();
		cqlLeftNavBarPanelView.getRightHandNavPanel().clear();
		cqlLeftNavBarPanelView.setIsPageDirty(false);
		
		this.document = null;
		this.populationDataModel = null;
		
		resetMessageDisplay();
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getMainPanel()
	 */
	/**
	 * Gets the main panel.
	 *
	 * @return the main panel
	 */
	@Override
	public VerticalPanel getMainPanel() {
		return mainPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getMainVPanel()
	 */
	/**
	 * Gets the main h panel.
	 *
	 * @return the main h panel
	 */
	@Override
	public HorizontalPanel getMainHPanel() {
		return mainHPPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getMainVPanel()
	 */
	/**
	 * Gets the main v panel.
	 *
	 * @return the main v panel
	 */
	@Override
	public Widget asWidget() {
		return mainVPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getClickedMenu()
	 */
	/**
	 * Gets the clicked menu.
	 *
	 * @return the clicked menu
	 */
	@Override
	public String getClickedMenu() {
		return clickedMenu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setClickedMenu(java.
	 * lang.String)
	 */
	/**
	 * Sets the clicked menu.
	 *
	 * @param clickedMenu
	 *            the new clicked menu
	 */
	@Override
	public void setClickedMenu(String clickedMenu) {
		this.clickedMenu = clickedMenu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getClickedMenu()
	 */
	/**
	 * Gets the clicked menu.
	 *
	 * @return the clicked menu
	 */
	@Override
	public String getNextClickedMenu() {
		return nextClickedMenu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setClickedMenu(java.
	 * lang.String)
	 */
	/**
	 * Sets the clicked menu.
	 *
	 * @param nextClickedMenu
	 *            the new next clicked menu
	 */
	@Override
	public void setNextClickedMenu(String nextClickedMenu) {
		this.nextClickedMenu = nextClickedMenu;
	}

	/**
	 * Gets the main flow panel.
	 *
	 * @return the main flow panel
	 */
	@Override
	public FlowPanel getMainFlowPanel() {
		return mainFlowPanel;
	}

	/**
	 * Unset each section selected object and clear Value sets CellTable Panel.
	 */
	/*public void unsetEachSectionSelectedObject() {
		cqlLeftNavBarPanelView.setCurrentSelectedDefinitionObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedParamerterObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedFunctionObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedFunctionArgumentObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedFunctionArgumentName(null);
		cqlLeftNavBarPanelView.setCurrentSelectedIncLibraryObjId(null);
		
	}*/
	
	
	/**
	 * Reset message display.
	 */
	@Override
	public void resetMessageDisplay() {
		cqlLeftNavBarPanelView.getWarningMessageAlert().clearAlert();
		cqlLeftNavBarPanelView.getSuccessMessageAlert().clearAlert();
		cqlLeftNavBarPanelView.getErrorMessageAlert().clearAlert();
		cqlLeftNavBarPanelView.getWarningConfirmationMessageAlert().clearAlert();
		if(cqlLeftNavBarPanelView.getGlobalWarningConfirmationMessageAlert() != null)
			cqlLeftNavBarPanelView.getGlobalWarningConfirmationMessageAlert().clearAlert();
		
	}

	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getCqlLeftNavBarPanelView()
	 */
	@Override
	public CQLPopulationLeftNavBarPanelView getCqlLeftNavBarPanelView(){
		return cqlLeftNavBarPanelView;
	}

	@Override
	public void setHeadingBasedOnCurrentSection(String headingText, String panelId){
		setHeading(headingText, panelId);
		Mat.focusSkipLists("CqlPopulationView");
	}
	
	private void setHeading(String text,String linkName) {
		headingPanel.clear();
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
		headingPanel.add(heading);
	}
}
