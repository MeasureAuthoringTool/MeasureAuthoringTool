package mat.client.populationworkspace;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import mat.client.Mat;
import mat.client.populationworkspace.model.PopulationDataModel;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.SkipListBuilder;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

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
	HTML caution = new HTML();
	HorizontalPanel headingPanel = new HorizontalPanel();

	HorizontalPanel cautionPanel = new HorizontalPanel();
	/** The cql left nav bar panel view. */
	private CQLPopulationLeftNavBarPanelView cqlLeftNavBarPanelView;

	private CQLViewPopulationsDisplay cqlViewPopulationsDisplay;

	private PopulationDataModel populationDataModel = null;
	private Document document = null;
	private CQLPopulationObserver cqlPopulationObserver;
	CQLStratificationDetailView cqlStratificationDetailView;
	CQLMeasureObservationDetailView cqlMeasureObservationDetailView;
	
	CQLPopulationDetail cqlPopulationDetailView;

	/**
	 * Instantiates a new CQL work space view.
	 */
	public CQLPopulationWorkSpaceView() {
		cqlLeftNavBarPanelView = new CQLPopulationLeftNavBarPanelView();
		cqlViewPopulationsDisplay = new CQLViewPopulationsDisplay();
	}
	
	public void setObserver(CQLPopulationObserver cqlPopulationObserver) {
		this.cqlPopulationObserver = cqlPopulationObserver;
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
		
		caution.addStyleName("leftAligned");
		caution.setStyleName("marginLeft");
		cautionPanel.getElement().setId("cautionPanel");
		
		mainFlowPanel.setWidth("700px");
		
		mainPanel.getElement().setId("CQLPopulationWorkspaceView.containerPanel");		
		mainPanel.add(headingPanel);
		mainPanel.add(cautionPanel);
		mainPanel.add(cqlLeftNavBarPanelView.getMessagePanel());
		mainPanel.add(mainFlowPanel);
		mainPanel.setStyleName("cqlRightContainer");
		
		resetMessageDisplay();

		mainHPPanel.addStyleName("cqlRightMessage");
		mainHPPanel.add(cqlLeftNavBarPanelView.buildMeasureLibCQLView(document));

		mainHPPanel.add(mainPanel);

		mainVPanel.add(mainHPPanel);
	}

	private void setCautionText(String displayName) {
		cautionPanel.clear();
		caution.setHTML(getCautionText(displayName));
		cautionPanel.add(caution);
	}

	private String getCautionText(String displayName) {
		return CQLWorkSpaceConstants.POPULATIONS.MEASURE_OBSERVATIONS.popName().equalsIgnoreCase(displayName) ? CQLWorkSpaceConstants.MEASURE_OBSERVATION_CAUTION_MSG : getPopulationOrStratificationCautionText(displayName)  ;
	}

	private String getPopulationOrStratificationCautionText(String displayName) {
		return CQLWorkSpaceConstants.POPULATIONS.STRATIFICATION.popName().equalsIgnoreCase(displayName) ? CQLWorkSpaceConstants.STRATIFICATION_CAUTION_MSG : CQLWorkSpaceConstants.GENERIC_CAUTION_MSG;
	}

	@Override
	public void displayMeasureObservations() {
		mainFlowPanel.clear();
		populationDataModel.loadPopulations(document);
		cqlMeasureObservationDetailView = new CQLMeasureObservationDetailView(populationDataModel, 
				CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS);
		
		cqlMeasureObservationDetailView.setObserver(cqlPopulationObserver);
		cqlMeasureObservationDetailView.displayPopulationDetail(mainFlowPanel);
		setHeadingBasedOnCurrentSection("Population Workspace > " + cqlMeasureObservationDetailView.getPopulationsObject().getDisplayName(), "headingPanel");
		setCautionText(cqlMeasureObservationDetailView.getPopulationsObject().getDisplayName());
	}
	
	@Override
	public void displayStratification() {
		mainFlowPanel.clear();
		mainFlowPanel.setHeight("100%");
		cqlStratificationDetailView  = new CQLStratificationDetailView();
		populationDataModel.loadPopulations(document);
		mainFlowPanel.add(cqlStratificationDetailView.buildView(populationDataModel));
		cqlStratificationDetailView.setObserver(cqlPopulationObserver);
		setHeadingBasedOnCurrentSection("Population Workspace > Stratification", "headingPanel");
		setCautionText("Stratification");
	}
	
	@Override
	public void displayPopulationDetailView(String populationType) {
		populationDataModel.loadPopulations(document);
		cqlPopulationDetailView = CQLPopulationDetailFactory.getCQLPopulationDetailView(populationDataModel, populationType);
		cqlPopulationDetailView.setObserver(cqlPopulationObserver);
		cqlPopulationDetailView.displayPopulationDetail(mainFlowPanel);
		setHeadingBasedOnCurrentSection("Population Workspace > " + cqlPopulationDetailView.getPopulationsObject().getDisplayName(), "headingPanel");
		setCautionText(cqlPopulationDetailView.getPopulationsObject().getDisplayName());
	}

	/**
	 * Reset All components to default state.
	 */
	@Override
	public void resetAll() {
		mainFlowPanel.clear();
		headingPanel.clear();
		cautionPanel.clear();
		cqlLeftNavBarPanelView.getRightHandNavPanel().clear();
		//cqlLeftNavBarPanelView.setIsPageDirty(false);

		this.document = null;
		this.populationDataModel = null;

		resetMessageDisplay();
	}

	@Override
	public void buildViewPopulations() {
		mainFlowPanel.clear();
		cautionPanel.clear();
		cqlViewPopulationsDisplay.getMeasureXmlAndBuildView();
		mainFlowPanel.add(cqlViewPopulationsDisplay.getMainPanel());
		mainFlowPanel.setHeight("100%");
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

	public CQLPopulationObserver getCqlPopulationObserver() {
		return cqlPopulationObserver;
	}
	@Override
	public CQLStratificationDetailView getCqlStratificationDetailView() {
		return cqlStratificationDetailView;
	}

	public void setCqlPopulationObserver(CQLPopulationObserver cqlPopulationObserver) {
		this.cqlPopulationObserver = cqlPopulationObserver;
	}
	@Override
	public void setCqlStratificationDetailView(CQLStratificationDetailView cqlStratificationDetailView) {
		this.cqlStratificationDetailView = cqlStratificationDetailView;
	}
	@Override
	public CQLMeasureObservationDetailView getCqlMeasureObservationDetailView() {
		return cqlMeasureObservationDetailView;
	}
	@Override
	public void setCqlMeasureObservationDetailView(CQLMeasureObservationDetailView cqlMeasureObservationDetailView ) {
		this.cqlMeasureObservationDetailView = cqlMeasureObservationDetailView;
	}
	public CQLPopulationDetail getCqlPopulationDetailView() {
		return cqlPopulationDetailView;
	}
	@Override
	public void setCqlPopulationDetailView(CQLPopulationDetail cqlPopulationDetailView ) {
		this.cqlPopulationDetailView = cqlPopulationDetailView;
	}

}
