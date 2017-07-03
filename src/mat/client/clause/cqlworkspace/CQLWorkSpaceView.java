package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.HorizontalPanel;
//import org.gwtbootstrap3.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLWorkSpaceView.
 */
/**
 * @author jnarang
 *
 */
public class CQLWorkSpaceView implements CQLWorkSpacePresenter.ViewDisplay {

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

	/** The value set view. */
	private CQLAppliedValueSetView valueSetView;

	/** The incl view. */
	private CQLIncludeLibraryView inclView;
	
	/** The code view. */
	private CQLCodesView codesView;

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

	/**
	 * Instantiates a new CQL work space view.
	 */
	public CQLWorkSpaceView() {
		generalInformationView = new CQLGeneralInformationView();
		cqlParametersView = new CQLParametersView();
		cqlDefinitionsView = new CQlDefinitionsView();
		cqlFunctionsView = new CQLFunctionsView();
		codesView = new CQLCodesView();
		valueSetView = new CQLAppliedValueSetView();
		inclView = new CQLIncludeLibraryView();
		cqlViewCQLView = new CQLViewCQLView();
		cqlLeftNavBarPanelView = new CQLLeftNavBarPanelView();
		
		resetAll();
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
	public void buildView() {
		resetAll();
		unsetEachSectionSelectedObject();
	
		buildGeneralInformation();
		mainFlowPanel.setWidth("700px");
		mainPanel.getElement().setId("CQLWorkspaceView.containerPanel");
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(cqlLeftNavBarPanelView.getMessagePanel());
		mainPanel.add(mainFlowPanel);

		resetMessageDisplay();

		mainHPPanel.addStyleName("cqlRightMessage");
		mainHPPanel.add(cqlLeftNavBarPanelView.buildMeasureLibCQLView());
		mainHPPanel.add(mainPanel);
		
		mainVPanel.add(mainHPPanel);
        mainVPanel.add(valueSetView.getCellTableMainPanel());
        mainVPanel.add(codesView.getCellTableMainPanel());
        
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#buildCQLFileView()
	 */
	/**
	 * Builds the cql file view.
	 */
	@Override
	public void buildCQLFileView() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(cqlViewCQLView.buildView());

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * buildGeneralInformation()
	 */
	/**
	 * Builds the general information.
	 */
	@Override
	public void buildGeneralInformation() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(generalInformationView.getView());

	}

	

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#buildAppliedQDM()
	 */
	@Override
	public void buildAppliedQDM() {
		mainFlowPanel.clear();
		valueSetView.resetVSACValueSetWidget();
		valueSetView.setWidgetToDefault();
		resetMessageDisplay();
		
		unsetEachSectionSelectedObject();
		VerticalPanel appliedQDMTopPanel = new VerticalPanel();

		appliedQDMTopPanel.add(valueSetView.asWidget());
		valueSetView.buildCellTableWidget();
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("700px");
		appliedQDMTopPanel.setWidth("700px");
		appliedQDMTopPanel.setStyleName("marginLeft15px");
		vp.add(appliedQDMTopPanel);
		
		mainFlowPanel.add(vp);

	}

	@Override
	public void buildCodes() {
		mainFlowPanel.clear();
		codesView.resetVSACCodeWidget();
		codesView.setWidgetToDefault();
		resetMessageDisplay();
		
		unsetEachSectionSelectedObject();
		VerticalPanel codesTopPanel = new VerticalPanel();

		codesTopPanel.add(codesView.asWidget());
		codesView.buildCellTableWidget();
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("700px");
		codesTopPanel.setWidth("700px");
		codesTopPanel.setStyleName("marginLeft15px");
		vp.add(codesTopPanel);
		
		mainFlowPanel.add(vp);

	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#buildIncludesView()
	 */
	@Override
	public void buildIncludesView() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		resetMessageDisplay();
		VerticalPanel includesTopPanel = new VerticalPanel();
		inclView.resetToDefault();
		// building searchWidget for adding new aliasName
		inclView.buildAddNewAliasView();

		includesTopPanel.add(inclView.asWidget());

		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("700px");
		includesTopPanel.setWidth("700px");
		includesTopPanel.setStyleName("marginLeft15px");
		vp.add(includesTopPanel);
		mainFlowPanel.add(vp);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * buildParameterLibraryView()
	 */
	/**
	 * Builds the parameter library view.
	 */
	@Override
	public void buildParameterLibraryView() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(cqlParametersView.getView());

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * buildDefinitionLibraryView()
	 */
	/**
	 * Builds the definition library view.
	 */
	@Override
	public void buildDefinitionLibraryView() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(cqlDefinitionsView.getView());
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * buildFunctionLibraryView()
	 */
	/**
	 * Builds the function library view.
	 */
	@Override
	public void buildFunctionLibraryView() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(cqlFunctionsView.getView(MatContext.get().getMeasureLockService().checkForEditPermission()));
	}
	
	/**
	 * Reset All components to default state.
	 */
	@Override
	public void resetAll() {
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
		generalInformationView.resetAll();
		cqlParametersView.resetAll();
		cqlDefinitionsView.resetAll();
		cqlFunctionsView.resetAll();
		cqlViewCQLView.resetAll();
		cqlLeftNavBarPanelView.setIsPageDirty(false);
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

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#buildInfoPanel(com.google.gwt.user.client.ui.Widget)
	 */
	/*
	 * This Method is used to give information of Keyboard ShortcutKeys on
	 * AceEditor for Users.
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * buildInfoPanel(com.google.gwt.user.client.ui.Widget)
	 */
	/*@Override
	public void buildInfoPanel(Widget sourceWidget) {

		PopupPanel panel = new PopupPanel();
		panel.setAutoHideEnabled(true);
		panel.setPopupPosition(sourceWidget.getAbsoluteLeft() + 40, sourceWidget.getAbsoluteTop() + 20);
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		panel.setWidget(dialogContents);

		HTML html1 = new HTML("Ctrl-Alt-v  : Value Sets");
		HTML html2 = new HTML("Ctrl-Alt-y  : Datatypes");
		HTML html3 = new HTML("Ctrl-Alt-t  : Timings");
		HTML html4 = new HTML("Ctrl-Alt-f  : Functions");
		HTML html5 = new HTML("Ctrl-Alt-d  : Definitions");
		HTML html6 = new HTML("Ctrl-Alt-p  : Parameters");
		HTML html7 = new HTML("Ctrl-Alt-a  : Attributes");
		HTML html8 = new HTML("Ctrl-Space  : All");

		dialogContents.add(html1);
		dialogContents.add(html2);
		dialogContents.add(html3);
		dialogContents.add(html4);
		dialogContents.add(html5);
		dialogContents.add(html6);
		dialogContents.add(html7);
		dialogContents.add(html8);
		panel.show();
	}*/
	

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#hideAceEditorAutoCompletePopUp()
	 */
	@Override
	public void hideAceEditorAutoCompletePopUp() {
		cqlDefinitionsView.hideAceEditorAutoCompletePopUp();
		cqlParametersView.hideAceEditorAutoCompletePopUp();
		cqlFunctionsView.hideAceEditorAutoCompletePopUp();
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
		valueSetView.clearCellTableMainPanel();
		codesView.clearCellTableMainPanel();
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
		if(cqlLeftNavBarPanelView.getGlobalWarningConfirmationMessageAlert() != null)
			cqlLeftNavBarPanelView.getGlobalWarningConfirmationMessageAlert().clearAlert();
		//cqlLeftNavBarPanelView.getDeleteConfirmationMessgeAlert().clearAlert();
		hideAceEditorAutoCompletePopUp();
		resetFormGroups();
	}

	
	
	private void resetFormGroups() {
		cqlDefinitionsView.resetDefineFormGroup();
		cqlParametersView.resetParamFormGroup();
		cqlFunctionsView.resetFuncFormGroup();
		generalInformationView.resetFormGroup();
		inclView.resetFromGroup();
		
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getCqlLeftNavBarPanelView()
	 */
	@Override
	public CQLLeftNavBarPanelView getCqlLeftNavBarPanelView(){
		return cqlLeftNavBarPanelView;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getCqlGeneralInformationView()
	 */
	@Override
	public CQLGeneralInformationView getCqlGeneralInformationView(){
		return generalInformationView;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getIncludeView()
	 */
	@Override
	public CQLIncludeLibraryView getIncludeView() {
		return inclView;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getQdmView()
	 */
	@Override
	public CQLAppliedValueSetView getValueSetView() {
		return valueSetView;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getCQLParametersView()
	 */
	@Override
	public CQLParametersView getCQLParametersView(){
		return cqlParametersView;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getCQlDefinitionsView()
	 */
	@Override
	public CQlDefinitionsView getCQlDefinitionsView(){
		return cqlDefinitionsView;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getCqlFunctionsView()
	 */
	@Override
	public CQLFunctionsView getCqlFunctionsView() {
		return cqlFunctionsView;
	}
	
	/**
	 * Gets the view CQL view.
	 *
	 * @return the view CQL view
	 */
	@Override
	public CQLViewCQLView getViewCQLView(){
		return cqlViewCQLView;
	}

	@Override
	public CQLCodesView getCodesView() {
		return codesView;
	}
	
}
