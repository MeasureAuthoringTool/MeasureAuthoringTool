package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.shared.SpacerWidget;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLStandaloneWorkSpaceView.
 */
public class CQLStandaloneWorkSpaceView implements CQLStandaloneWorkSpacePresenter.ViewDisplay{
	
	/** The main horizontal panel. */
	HorizontalPanel mainHPPanel = new HorizontalPanel();

	/** The main horizontal panel. */
	private VerticalPanel mainPanel = new VerticalPanel();

	/** The main v panel. */
	private VerticalPanel mainVPanel = new VerticalPanel();

	/** The main flow panel. */
	private FlowPanel mainFlowPanel = new FlowPanel();
	
	/** The qdm view. */
	private CQLAppliedValueSetView qdmView;

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
	
	
	/**
	 * Instantiates a new CQL standalone work space view.
	 */
	public CQLStandaloneWorkSpaceView() {
		generalInformationView = new CQLGeneralInformationView();
		cqlParametersView = new CQLParametersView();
		cqlDefinitionsView = new CQlDefinitionsView();
		cqlFunctionsView = new CQLFunctionsView();
		qdmView = new CQLAppliedValueSetView();
		inclView = new CQLIncludeLibraryView();
		cqlViewCQLView = new CQLViewCQLView();
		cqlLeftNavBarPanelView = new CQLLeftNavBarPanelView();
		
		resetAll();
	}
	

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#buildView()
	 */
	@Override
	public void buildView() {
		resetAll();
		unsetEachSectionSelectedObject();
		
		buildGeneralInformation();
		mainFlowPanel.setWidth("700px");
		mainPanel.getElement().setId("CQLStandaloneWorkSpaceView.containerPanel");
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(cqlLeftNavBarPanelView.getMessagePanel());
		mainPanel.add(mainFlowPanel);

		resetMessageDisplay();

		mainHPPanel.addStyleName("cqlRightMessage");
		mainHPPanel.add(cqlLeftNavBarPanelView.buildMeasureLibCQLView());
		mainHPPanel.add(mainPanel);
		mainVPanel.add(mainHPPanel);
        mainVPanel.add(qdmView.getCellTableMainPanel());
		
	}
	
	
	@Override
	public void buildGeneralInformation() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(generalInformationView.getCQLView());

	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#buildCQLFileView()
	 */
	@Override
	public void buildCQLFileView() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(cqlViewCQLView.buildView());

	}
	
	
	/**
	 * Unset each section selected object and clear Value sets CellTable Panel.
	 */
	public void unsetEachSectionSelectedObject() {
		cqlLeftNavBarPanelView.setCurrentSelectedDefinitionObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedParamerterObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedFunctionObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedIncLibraryObjId(null);
		cqlFunctionsView.getFunctionArgNameMap().clear();
		if (cqlFunctionsView.getFunctionArgumentList().size() > 0) {
			cqlFunctionsView.getFunctionArgumentList().clear();
		}
		qdmView.clearCellTableMainPanel();
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
		cqlLeftNavBarPanelView.getGlobalWarningConfirmationMessageAlert().clearAlert();
		cqlLeftNavBarPanelView.getDeleteConfirmationMessgeAlert().clearAlert();
		hideAceEditorAutoCompletePopUp();

	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#hideAceEditorAutoCompletePopUp()
	 */
	@Override
	public void hideAceEditorAutoCompletePopUp() {
		cqlDefinitionsView.hideAceEditorAutoCompletePopUp();
		cqlParametersView.hideAceEditorAutoCompletePopUp();
		cqlFunctionsView.hideAceEditorAutoCompletePopUp();
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#getMainPanel()
	 */
	@Override
	public VerticalPanel getMainPanel() {
		return mainPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#getMainVPanel()
	 */
	@Override
	public Widget asWidget() {
		return mainVPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#getMainHPanel()
	 */
	@Override
	public HorizontalPanel getMainHPanel() {
		return mainHPPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#getMainFlowPanel()
	 */
	@Override
	public FlowPanel getMainFlowPanel() {
		return mainFlowPanel;
	}
	
	/**
	 * Reset all.
	 */
	private void resetAll() {
		mainFlowPanel.clear();
		cqlLeftNavBarPanelView.getRightHandNavPanel().clear();
		inclView.setAliasNameTxtArea("");
		System.out.println(" in resetAll doing setText");
		
		cqlLeftNavBarPanelView.getViewIncludeLibrarys().clear();
		cqlLeftNavBarPanelView.getViewParameterList().clear();
		cqlLeftNavBarPanelView.getViewDefinitions().clear();
		cqlLeftNavBarPanelView.getViewFunctions().clear();
		cqlLeftNavBarPanelView.getViewIncludeLibrarys().clear();

		if (cqlLeftNavBarPanelView.getIncludesCollapse() != null) {
			cqlLeftNavBarPanelView.getIncludesCollapse().clear();
		}
		if (cqlLeftNavBarPanelView.getParamCollapse() != null) {
			cqlLeftNavBarPanelView.getParamCollapse().clear();
		}
		if (cqlLeftNavBarPanelView.getDefineCollapse() != null) {
			cqlLeftNavBarPanelView.getDefineCollapse().clear();
		}
		if (cqlLeftNavBarPanelView.getFunctionCollapse() != null) {
			cqlLeftNavBarPanelView.getFunctionCollapse().clear();
		}
		cqlParametersView.resetAll();
		cqlDefinitionsView.resetAll();
		cqlFunctionsView.resetAll();
		cqlViewCQLView.resetAll();
		cqlLeftNavBarPanelView.setIsPageDirty(false);
		resetMessageDisplay();
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#getClickedMenu()
	 */
	@Override
	public String getClickedMenu() {
		return clickedMenu;
	}

    /* (non-Javadoc)
     * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#setClickedMenu(java.lang.String)
     */
    @Override
	public void setClickedMenu(String clickedMenu) {
		this.clickedMenu = clickedMenu;
	}

    /* (non-Javadoc)
     * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#getNextClickedMenu()
     */
    @Override
	public String getNextClickedMenu() {
		return nextClickedMenu;
	}

    /* (non-Javadoc)
     * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#setNextClickedMenu(java.lang.String)
     */
    @Override
	public void setNextClickedMenu(String nextClickedMenu) {
		this.nextClickedMenu = nextClickedMenu;
	}

    /* (non-Javadoc)
     * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#getCqlLeftNavBarPanelView()
     */
    @Override
	public CQLLeftNavBarPanelView getCqlLeftNavBarPanelView() {
		return cqlLeftNavBarPanelView;
	}
    
    /* (non-Javadoc)
     * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#getCQLParametersView()
     */
    @Override
    public CQLParametersView getCQLParametersView(){
    	return cqlParametersView; 
    }
    
    /* (non-Javadoc)
     * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#getCQLDefinitionsView()
     */
    @Override
    public CQlDefinitionsView getCQLDefinitionsView(){
    	return cqlDefinitionsView;
    }
    
    /* (non-Javadoc)
     * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#getCQLFunctionsView()
     */
    @Override
    public CQLFunctionsView getCQLFunctionsView(){
    	return cqlFunctionsView;
    }
    
    /* (non-Javadoc)
     * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#getCqlIncludeLibraryView()
     */
    @Override
    public CQLIncludeLibraryView getCqlIncludeLibraryView(){
    	return inclView;
    }
    
    @Override
	public AceEditor getCqlAceEditor() {
		return cqlViewCQLView.getCqlAceEditor();
	}
    
    @Override
    public CQLGeneralInformationView getCqlGeneralInformationView(){
    	return generalInformationView;
    }

}
