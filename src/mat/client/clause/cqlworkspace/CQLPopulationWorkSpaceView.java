package mat.client.clause.cqlworkspace;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;

import mat.client.Mat;
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

	private CQLViewPopulationsDisplay cqlViewPopulationsDisplay;

	private PopulationDataModel populationDataModel = null;
	private Document document = null;

	/**
	 * Instantiates a new CQL work space view.
	 */
	public CQLPopulationWorkSpaceView() {
		cqlLeftNavBarPanelView = new CQLPopulationLeftNavBarPanelView();
		cqlViewPopulationsDisplay = new CQLViewPopulationsDisplay();
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
		headingPanel.setStyleName("marginLeft");
		headingPanel.getElement().setId("headingPanel");
		
		mainFlowPanel.setWidth("700px");
		
		mainPanel.getElement().setId("CQLPopulationWorkspaceView.containerPanel");		
		mainPanel.add(headingPanel);
		mainPanel.add(cqlLeftNavBarPanelView.getMessagePanel());
		mainPanel.add(mainFlowPanel);
		mainPanel.setStyleName("cqlRightContainer");
		
		resetMessageDisplay();

		mainHPPanel.addStyleName("cqlRightMessage");
		mainHPPanel.add(cqlLeftNavBarPanelView.buildMeasureLibCQLView(document));

		mainHPPanel.add(mainPanel);

		mainVPanel.add(mainHPPanel);		
	}
	
	@Override
	public void displayInitialPopulations() {
		/** The function add new button. */
		showPopulation(this.populationDataModel.getInitialPopulationsObject());
	}
	@Override
	public void displayNumerators() {
		showPopulation(this.populationDataModel.getNumeratorsObject());
	}
	@Override
	public void displayDenominator() {
		showPopulation(this.populationDataModel.getDenominatorsObject());
	}
	@Override
	public void displayDenominatorExclusions() {
		showPopulation(this.populationDataModel.getDenominatorExclusionsObject());
	}
	@Override
	public void displayDenominatorExceptions() {
		showPopulation(this.populationDataModel.getDenominatorExceptionsObject());
	}
	@Override
	public void displayNumeratorExclusion() {
		showPopulation(this.populationDataModel.getNumeratorExclusionsObject());
	}
	@Override
	public void displayMeasurePopulations() {
		showPopulation(this.populationDataModel.getMeasurePopulationsObject());
	}
	@Override
	public void displayMeasurePopulationsExclusions() {
		showPopulation(this.populationDataModel.getMeasurePopulationsExclusionsObject());
	}
	@Override
	public void displayMeasureObservations() {

		mainFlowPanel.clear();
	}
	@Override
	public void displayStratification() {

		mainFlowPanel.clear();
	}

	private void showPopulation(PopulationsObject populationObject) {
		List<PopulationClauseObject> popClauses = populationObject.getPopulationClauseObjectList();
		mainFlowPanel.clear();		
		setHeadingBasedOnCurrentSection("Population Workspace > " + populationObject.getDisplayName(), "headingPanel");
		Grid populationGrid = new Grid(popClauses.size(), 4);
		populationGrid.addStyleName("borderSpacing");

		for (int i = 0; i < popClauses.size(); i++) {

			PopulationClauseObject populationClauseObject = popClauses.get(i);

			// set the name of the Initial Population clause.
			FormLabel nameLabel = new FormLabel();
			nameLabel.setText(populationClauseObject.getDisplayName());
			nameLabel.setTitle(populationClauseObject.getDisplayName());

			populationGrid.setWidget(i, 0, nameLabel);
			populationGrid.getCellFormatter().setWidth(i, 0, "230px");

			// Set a listbox with all definition names in it.
			ListBox definitionListBox = new ListBox();
			definitionListBox.setSize("180px", "28px");			
			definitionListBox.addItem("--Select Definition--", "");
			definitionListBox.setTitle("Select Definition List");
			definitionListBox.setId("definitionList_" + populationClauseObject.getDisplayName());

			for (String definitionName : this.populationDataModel.getDefinitionNameList()) {
				definitionListBox.addItem(definitionName, definitionName);
			}

			// select a definition name in the listbox
			for (int j = 0; j < definitionListBox.getItemCount(); j++) {
				String definitionName = definitionListBox.getItemText(j);
				if (definitionName.equals(populationClauseObject.getCqlDefinitionDisplayName())) {
					definitionListBox.setItemSelected(j, true);
					break;
				}
			}

			populationGrid.setWidget(i, 1, definitionListBox);

			// button for Delete
			Button deleteButton = new Button();
			deleteButton.setType(ButtonType.LINK);
			deleteButton.getElement().setId("deleteButton_" + populationClauseObject.getDisplayName());
			deleteButton.setTitle("Delete");
			deleteButton.setSize("50px", "30px");
			deleteButton.getElement().setAttribute("aria-label", "Delete");
			deleteButton.setIcon(IconType.TRASH);
			deleteButton.setIconSize(IconSize.LARGE);
			deleteButton.setColor("#0964A2");

			populationGrid.setWidget(i, 2, deleteButton);

			// button for View Human Readable
			Button viewHRButton = new Button();
			viewHRButton.setType(ButtonType.LINK);
			viewHRButton.getElement().setId("viewHRButton_" + populationClauseObject.getDisplayName());
			viewHRButton.setTitle("View Human Readable");
			viewHRButton.setSize("50px", "30px");
			viewHRButton.getElement().setAttribute("aria-label", "View Human Readable");
			viewHRButton.setIcon(IconType.BINOCULARS);
			viewHRButton.setIconSize(IconSize.LARGE);
			viewHRButton.setColor("black");

			populationGrid.setWidget(i, 3, viewHRButton);

		}

		ScrollPanel scrollPanel = new ScrollPanel(populationGrid);
		scrollPanel.setSize("700px", "250px");

		mainFlowPanel.add(new SpacerWidget());
		
		HorizontalPanel btnPanel = new HorizontalPanel();		
		btnPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		btnPanel.setStyleName("marginLeftButtons");		
		btnPanel.add(getAllButtons(populationObject.getDisplayName(), populationObject.getPopulationName()));
		
		mainFlowPanel.add(btnPanel);		
		mainFlowPanel.add(scrollPanel);
		mainFlowPanel.add(new SpacerWidget());
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

	@Override
	public void buildViewPopulations() {
		// resetAll();
		mainFlowPanel.clear();
		cqlViewPopulationsDisplay.getMeasureXmlAndBuildView();
		mainFlowPanel.add(cqlViewPopulationsDisplay.getMainPanel());
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
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setClickedMenu(java.
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
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#setClickedMenu(java.
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
	/*
	 * public void unsetEachSectionSelectedObject() {
	 * cqlLeftNavBarPanelView.setCurrentSelectedDefinitionObjId(null);
	 * cqlLeftNavBarPanelView.setCurrentSelectedParamerterObjId(null);
	 * cqlLeftNavBarPanelView.setCurrentSelectedFunctionObjId(null);
	 * cqlLeftNavBarPanelView.setCurrentSelectedFunctionArgumentObjId(null);
	 * cqlLeftNavBarPanelView.setCurrentSelectedFunctionArgumentName(null);
	 * cqlLeftNavBarPanelView.setCurrentSelectedIncLibraryObjId(null);
	 * 
	 * }
	 */

	/**
	 * Reset message display.
	 */
	@Override
	public void resetMessageDisplay() {
		cqlLeftNavBarPanelView.getWarningMessageAlert().clearAlert();
		cqlLeftNavBarPanelView.getSuccessMessageAlert().clearAlert();
		cqlLeftNavBarPanelView.getErrorMessageAlert().clearAlert();
		cqlLeftNavBarPanelView.getWarningConfirmationMessageAlert().clearAlert();
		if (cqlLeftNavBarPanelView.getGlobalWarningConfirmationMessageAlert() != null)
			cqlLeftNavBarPanelView.getGlobalWarningConfirmationMessageAlert().clearAlert();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getCqlLeftNavBarPanelView()
	 */
	@Override
	public CQLPopulationLeftNavBarPanelView getCqlLeftNavBarPanelView() {
		return cqlLeftNavBarPanelView;
	}

	@Override
	public void setHeadingBasedOnCurrentSection(String headingText, String panelId) {
		setHeading(headingText, panelId);
		Mat.focusSkipLists("MeasureComposer");
	}

	private void setHeading(String text, String linkName) {
		headingPanel.clear();
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr + "<h4><b>" + text + "</b></h4>");
		headingPanel.add(heading);
	}

	@Override
	public CQLViewPopulationsDisplay getCqlViewPopulationsDisplay() {
		return cqlViewPopulationsDisplay;
	}

	public void setCqlViewPopulationsDisplay(CQLViewPopulationsDisplay cqlViewPopulationsDisplay) {
		this.cqlViewPopulationsDisplay = cqlViewPopulationsDisplay;
	}
	
	private Button getNewButton(String displayName, String sectionName, String buttonName) {
		Button newBtn = new Button();
		newBtn.setType(ButtonType.LINK);
		newBtn.setText(buttonName);		
		if(buttonName.equals("Save")) {
			newBtn.setId("saveButton_" + sectionName);
			newBtn.setTitle("Click this button to save " + displayName);
			newBtn.setIcon(IconType.SAVE);
			newBtn.setPull(Pull.RIGHT);
		}else {			
			newBtn.setId("addNewButton_" + sectionName);
			newBtn.setTitle("Click this button to add a new " + displayName.substring(0, displayName.length()-1));
			newBtn.setIcon(IconType.PLUS);
			newBtn.setPull(Pull.LEFT);
		}				
		newBtn.setSize(ButtonSize.SMALL);
		
		return newBtn;
	}
	
	private ButtonGroup getAllButtons(String displayName, String sectionName) {
		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(getNewButton(displayName, sectionName, "Add New"));
		btnGroup.add(getNewButton(displayName, sectionName, "Save"));
		btnGroup.getElement().setAttribute("class", "btn-group");
		return btnGroup;
	}
}
