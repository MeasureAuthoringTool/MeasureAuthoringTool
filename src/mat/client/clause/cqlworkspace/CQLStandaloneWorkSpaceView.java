package mat.client.clause.cqlworkspace;

import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.model.cql.CQLFunctionArgument;

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
	private CQLAppliedValueSetView valueSetView;
	
	/** The code view. */
	private CQLCodesView codesView;

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
	private CQLView cqlViewCQLView;
	
	/** The cql left nav bar panel view. */
	private CQLLeftNavBarPanelView cqlLeftNavBarPanelView;
	
	/** The clicked menu. */
	public String clickedMenu = "general";

	/** The clicked menu. */
	public String nextClickedMenu = "general";
	
	private HorizontalPanel lockedButtonHPanel = new HorizontalPanel();
	
	/**
	 * Instantiates a new CQL standalone work space view.
	 */
	public CQLStandaloneWorkSpaceView() {
		generalInformationView = new CQLGeneralInformationView();
		cqlParametersView = new CQLParametersView();
		cqlDefinitionsView = new CQlDefinitionsView();
		cqlFunctionsView = new CQLFunctionsView();
		codesView = new CQLCodesView();
		valueSetView = new CQLAppliedValueSetView();
		inclView = new CQLIncludeLibraryView();
		cqlViewCQLView = new CQLView();
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
		buildLockedButtonPanel();
		//mainPanel.add(lockedButtonVPanel);
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(cqlLeftNavBarPanelView.getMessagePanel());
		mainPanel.add(mainFlowPanel);

		resetMessageDisplay();

		mainHPPanel.addStyleName("cqlRightMessage");
		mainHPPanel.add(cqlLeftNavBarPanelView.buildMeasureLibCQLView());
		mainHPPanel.add(mainPanel);
		//mainVPanel.add(lockedButtonVPanel);
		mainVPanel.add(mainHPPanel);
        mainVPanel.add(valueSetView.getCellTableMainPanel());
        mainVPanel.add(codesView.getCellTableMainPanel());
		
	}
	
	
	@Override
	public void buildGeneralInformation() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(generalInformationView.getCQLView());

	}
	
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
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#buildCQLFileView()
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
		//508 Compliance for Parameter section
		getCqlLeftNavBarPanelView().setFocus(getCQLParametersView().getMainParamViewVerticalPanel());
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
		//508 Compliance for Definition section
		getCqlLeftNavBarPanelView().setFocus(getCQLDefinitionsView().getMainDefineViewVerticalPanel());
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
		mainFlowPanel.add(cqlFunctionsView.getView(MatContext.get().getLibraryLockService().checkForEditPermission()));
		//508 Compliance for Function section
		getCqlLeftNavBarPanelView().setFocus(getCQLFunctionsView().getMainFunctionVerticalPanel());
	}
	
	
	/**
	 * Unset each section selected object and clear Value sets CellTable Panel.
	 */
	public void unsetEachSectionSelectedObject() {
		cqlLeftNavBarPanelView.setCurrentSelectedDefinitionObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedParamerterObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedFunctionObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedFunctionArgumentObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedFunctionArgumentName(null);
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
	//	cqlLeftNavBarPanelView.getDeleteConfirmationMessgeAlert().clearAlert();
		hideAceEditorAutoCompletePopUp();
		hideInformationDropDown();
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
	 * @see mat.client.clause.cqlworkspace.CQLStandaloneWorkSpacePresenter.ViewDisplay#hideAceEditorAutoCompletePopUp()
	 */
	@Override
	public void hideAceEditorAutoCompletePopUp() {
		cqlDefinitionsView.hideAceEditorAutoCompletePopUp();
		cqlParametersView.hideAceEditorAutoCompletePopUp();
		cqlFunctionsView.hideAceEditorAutoCompletePopUp();
	}
	@Override
	public void hideInformationDropDown() {
		cqlDefinitionsView.getDefineButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
		cqlParametersView.getParameterButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
		cqlFunctionsView.getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
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
		//buildLockedButtonPanel();
		return mainVPanel;
	}

	private void buildLockedButtonPanel() {
		lockedButtonHPanel.clear();
		
		if(MatContext.get().getCurrentLibraryInfo().isEditable() && 
				MatContext.get().getCurrentLibraryInfo().isLocked()){
			Icon lockIcon = new Icon(IconType.LOCK);
			lockIcon.setSize(IconSize.LARGE);
			lockIcon.setColor("#daa520");
			lockIcon.setId("LockedIcon");
			lockIcon.setTitle("Locked by "+MatContext.get().getCurrentCQLLibraryLockedUserName());
			lockedButtonHPanel.add(lockIcon);
			String label= MatContext.get().getCurrentCQLLibraryLockedUserName();
			if(label.length() > 20) {
				label = label.substring(0, 19);
			}
			HTML html = new HTML("<p>Locked by " + label+"</p>");
			//html.getElement().setAttribute("style", "color: #0964A2; font-size: small; margin-left: 2px;");
			html.setStyleName("standAloneLockedLabel");
			html.getElement().setAttribute("id", "LockedBy");
			lockedButtonHPanel.add(html);
			lockedButtonHPanel.getElement().setAttribute("id", "StandAloneCQL_LockedButtonHPanel");
			//lockedButtonVPanel.getElement().setAttribute("style", "right: 150px; overflow: scroll; margin-top: -12px; position: absolute;");
			lockedButtonHPanel.setStyleName("standAloneLockedWidget");
			mainPanel.add(lockedButtonHPanel);
		} else {
			lockedButtonHPanel.removeStyleName("standAloneLockedWidget");
		}
		
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
	
	
   /* @Override
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
    @Override
    public CQLIncludeLibraryView getIncludeView() {
		return inclView;
	}
    
    /**
	 * Gets the alias name txt area.
	 *
	 * @return the alias name txt area
	 */
	@Override
	public TextBox getAliasNameTxtArea() {
		return getIncludeView().getAliasNameTxtArea();
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getViewCQLEditor()
	 */
	@Override
	public AceEditor getViewCQLEditor() {
		return getIncludeView().getViewCQLEditor();
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getOwnerNameTextBox()
	 */
	@Override
	public TextBox getOwnerNameTextBox() {
		return getIncludeView().getOwnerNameTextBox();
	}


	@Override
	public void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList) {
		cqlFunctionsView.createAddArgumentViewForFunctions(argumentList, MatContext.get().getLibraryLockService().checkForEditPermission());
	}
	
	@Override
	public CQLButtonToolBar getParameterButtonBar() {
		return cqlParametersView.getParameterButtonBar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getDefineButtonBar()
	 */
	@Override
	public CQLButtonToolBar getDefineButtonBar() {
		return cqlDefinitionsView.getDefineButtonBar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionButtonBar()
	 */
	@Override
	public CQLButtonToolBar getFunctionButtonBar() {
		return cqlFunctionsView.getFunctionButtonBar();
	}
	
	@Override
	public TextBox getDefineNameTxtArea() {
		return cqlDefinitionsView.getDefineNameTxtArea();
	}
	
	@Override
	public AceEditor getDefineAceEditor() {
		return cqlDefinitionsView.getDefineAceEditor();
	}
	
	@Override
	public InlineRadio getContextDefinePATRadioBtn() {
		return cqlDefinitionsView.getContextDefinePATRadioBtn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getContextPOPToggleSwitch()
	 */
	@Override
	public InlineRadio getContextDefinePOPRadioBtn() {
		return cqlDefinitionsView.getContextDefinePOPRadioBtn();
	}
	
	/**
	 * Gets the func name txt area.
	 *
	 * @return the func name txt area
	 */
	@Override
	public TextBox getFuncNameTxtArea() {
		return cqlFunctionsView.getFuncNameTxtArea();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionBodyAceEditor()
	 */
	@Override
	public AceEditor getFunctionBodyAceEditor() {
		return cqlFunctionsView.getFunctionBodyAceEditor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getContextFuncPATRadioBtn()
	 */
	@Override
	public InlineRadio getContextFuncPATRadioBtn() {
		return cqlFunctionsView.getContextFuncPATRadioBtn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getContextFuncPOPRadioBtn()
	 */
	@Override
	public InlineRadio getContextFuncPOPRadioBtn() {
		return cqlFunctionsView.getContextFuncPOPRadioBtn();
	}
	
	@Override
	public List<CQLFunctionArgument> getFunctionArgumentList() {
		return cqlFunctionsView.getFunctionArgumentList();
	}
	
	/**
	 * Gets the parameter name txt area.
	 *
	 * @return the parameter name txt area
	 */
	@Override
	public TextBox getParameterNameTxtArea() {
		return cqlParametersView.getParameterNameTxtArea();
	}
	
	/**
	 * Gets the parameter ace editor.
	 *
	 * @return the parameter ace editor
	 */
	@Override
	public AceEditor getParameterAceEditor() {
		return cqlParametersView.getParameterAceEditor();
	}

	@Override
	public Map<String, CQLFunctionArgument> getFunctionArgNameMap() {
		return cqlFunctionsView.getFunctionArgNameMap();
	}
	
	/**
	 * Creates the add argument view for functions.
	 *
	 * @param argumentList
	 *            the argument list
	 */
	@Override
	public void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList,boolean isEditable) {
		cqlFunctionsView.createAddArgumentViewForFunctions(argumentList,isEditable);
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

	
	@Override
	public CQLAppliedValueSetView getValueSetView() {
		return valueSetView;
	}


	@Override
	public CQLCodesView getCodesView() {
		return codesView;
	}

	@Override
	public HorizontalPanel getLockedButtonVPanel() {
		//lockedButtonVPanel.clear();
		//buildLockedButtonPanel();
		return lockedButtonHPanel;
	}


	public void setLockedButtonVPanel(HorizontalPanel lockedButtonVPanel) {
		this.lockedButtonHPanel = lockedButtonVPanel;
	}

}
