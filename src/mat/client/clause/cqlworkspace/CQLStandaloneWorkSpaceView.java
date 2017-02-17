package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.SpacerWidget;

public class CQLStandaloneWorkSpaceView implements CQLStandaloneWorkSpacePresenter.ViewDisplay{
	
	/** The main horizontal panel. */
	HorizontalPanel mainHPPanel = new HorizontalPanel();

	/** The main horizontal panel. */
	private VerticalPanel mainPanel = new VerticalPanel();

	/** The main v panel. */
	private VerticalPanel mainVPanel = new VerticalPanel();

	/** The main flow panel. */
	private FlowPanel mainFlowPanel = new FlowPanel();
	
	/** The right hand nav panel. */
	private VerticalPanel rightHandNavPanel = new VerticalPanel();

	/** The message panel. */
	private HorizontalPanel messagePanel = new HorizontalPanel();
	
	
	/** The qdm view. */
	private CQLQDMAppliedView qdmView;

	/** The incl view. */
	private CQLIncludeLibraryView inclView;

	/** The general information view. */
	private CQLGeneralInformationView generalInformationView;
	
	/** The cql parameters view. */
	private CQLParametersView cqlParametersView;
	
	/** The cql definitions view. */
	private CQlDefinitionsView cqlDefinitionsView;
	
	/** The cql functions view. */
	private CQLFunctionsView cqlFunctionsView;
	
	/** The cql view CQL view. */
	private CQLViewCQLView cqlViewCQLView;
	
	/** The cql left nav bar panel view. */
	private CQLLeftNavBarPanelView cqlLeftNavBarPanelView;
	
	/** The clicked menu. */
	public String clickedMenu = "general";

	/** The clicked menu. */
	public String nextClickedMenu = "general";
	
	
	public CQLStandaloneWorkSpaceView() {
		generalInformationView = new CQLGeneralInformationView();
		cqlParametersView = new CQLParametersView();
		cqlDefinitionsView = new CQlDefinitionsView();
		cqlFunctionsView = new CQLFunctionsView();
		qdmView = new CQLQDMAppliedView();
		inclView = new CQLIncludeLibraryView();
		cqlViewCQLView = new CQLViewCQLView();
		cqlLeftNavBarPanelView = new CQLLeftNavBarPanelView(inclView, cqlParametersView, 
				cqlDefinitionsView, cqlFunctionsView, cqlViewCQLView);
		
		resetAll();
	}
	

	@Override
	public void buildView() {
		resetAll();
		cqlLeftNavBarPanelView.unsetEachSectionSelectedObject();
		
		mainFlowPanel.setWidth("700px");
		mainPanel.getElement().setId("CQLStandaloneWorkSpaceView.containerPanel");
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(cqlLeftNavBarPanelView.getMessagePanel());
		mainPanel.add(mainFlowPanel);

		cqlLeftNavBarPanelView.resetMessageDisplay();

		mainHPPanel.addStyleName("cqlRightMessage");
		mainHPPanel.add(cqlLeftNavBarPanelView.buildMeasureLibCQLView());
		mainHPPanel.add(mainPanel);
		
	}
	@Override
	public Widget asWidget(){
		mainPanel.clear();
		return mainPanel;
	}
	
	@Override
	public VerticalPanel getMainPanel() {
		return mainPanel;
	}

	@Override
	public VerticalPanel getMainVPanel() {
		return mainVPanel;
	}

	@Override
	public HorizontalPanel getMainHPanel() {
		return mainHPPanel;
	}

	@Override
	public FlowPanel getMainFlowPanel() {
		return mainFlowPanel;
	}
	
	private void resetAll() {
		mainFlowPanel.clear();
		cqlLeftNavBarPanelView.resetAll();
		cqlLeftNavBarPanelView.setIsPageDirty(false);
		cqlLeftNavBarPanelView.resetMessageDisplay();
	}


	@Override
	public String getClickedMenu() {
		return clickedMenu;
	}

    @Override
	public void setClickedMenu(String clickedMenu) {
		this.clickedMenu = clickedMenu;
	}

    @Override
	public String getNextClickedMenu() {
		return nextClickedMenu;
	}

    @Override
	public void setNextClickedMenu(String nextClickedMenu) {
		this.nextClickedMenu = nextClickedMenu;
	}

    @Override
	public CQLLeftNavBarPanelView getCqlLeftNavBarPanelView() {
		return cqlLeftNavBarPanelView;
	}

}
