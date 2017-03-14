package mat.client.clause.cqlworkspace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
//import org.gwtbootstrap3.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;
import mat.model.cql.CQLFunctionArgument;

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

	/**
	 * Instantiates a new CQL work space view.
	 */
	public CQLWorkSpaceView() {
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
        mainVPanel.add(qdmView.getCellTableMainPanel());
        
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
		qdmView.resetVSACValueSetWidget();
		qdmView.setWidgetToDefault();
		resetMessageDisplay();
		
		unsetEachSectionSelectedObject();
		VerticalPanel appliedQDMTopPanel = new VerticalPanel();

		appliedQDMTopPanel.add(qdmView.asWidget());
		qdmView.buildCellTableWidget();
		VerticalPanel vp = new VerticalPanel();
		vp.setStyleName("cqlRightContainer");
		vp.setWidth("700px");
		appliedQDMTopPanel.setWidth("700px");
		appliedQDMTopPanel.setStyleName("marginLeft15px");
		vp.add(appliedQDMTopPanel);
		
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
	 * Creates the add argument view for functions.
	 *
	 * @param argumentList
	 *            the argument list
	 */
	@Override
	public void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList,boolean isEditable) {
		cqlFunctionsView.createAddArgumentViewForFunctions(argumentList,isEditable);
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
	 * @see mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#
	 * getParameterNameTxtArea()
	 */
	/**
	 * Gets the parameter name txt area.
	 *
	 * @return the parameter name txt area
	 */
	@Override
	public TextBox getParameterNameTxtArea() {
		return cqlParametersView.getParameterNameTxtArea();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDeleteDefineButton
	 * ()
	 */
	/**
	 * Gets the delete define button.
	 *
	 * @return the delete define button
	 */
	@Override
	public Button getDeleteDefineButton() {
		return cqlDefinitionsView.getDefineButtonBar().getDeleteButton();
	}

	/**
	 * Gets the delete parameter button.
	 *
	 * @return the delete define button
	 */
	@Override
	public Button getDeleteParameterButton() {
		return cqlParametersView.getParameterButtonBar().getDeleteButton();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefineNameTxtArea(
	 * )
	 */
	/**
	 * Gets the define name txt area.
	 *
	 * @return the define name txt area
	 */
	@Override
	public TextBox getDefineNameTxtArea() {
		return cqlDefinitionsView.getDefineNameTxtArea();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getAddDefineButton()
	 */
	/**
	 * Gets the adds the define button.
	 *
	 * @return the adds the define button
	 */
	@Override
	public Button getAddDefineButton() {
		return cqlDefinitionsView.getDefineButtonBar().getSaveButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getParameterAceEditor
	 * ()
	 */
	/**
	 * Gets the parameter ace editor.
	 *
	 * @return the parameter ace editor
	 */
	@Override
	public AceEditor getParameterAceEditor() {
		return cqlParametersView.getParameterAceEditor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.CQLWorkSpacePresenter.ViewDisplay#getDefineAceEditor()
	 */
	/**
	 * Gets the define ace editor.
	 *
	 * @return the define ace editor
	 */
	@Override
	public AceEditor getDefineAceEditor() {
		return cqlDefinitionsView.getDefineAceEditor();
	}

	/**
	 * Sets the define ace editor.
	 *
	 * @param defineAceEditor
	 *            the new define ace editor
	 */
	public void setDefineAceEditor(AceEditor defineAceEditor) {
		cqlDefinitionsView.setDefineAceEditor(defineAceEditor);
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
	 * Gets the cql ace editor.
	 *
	 * @return the cql ace editor
	 */
	@Override
	public AceEditor getCqlAceEditor() {
		return cqlViewCQLView.getCqlAceEditor();
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

	/**
	 * Sets the func name txt area.
	 *
	 * @param funcNameTxtArea
	 *            the new func name txt area
	 */
	public void setFuncNameTxtArea(MatTextBox funcNameTxtArea) {
		cqlFunctionsView.setFuncNameTxtArea(funcNameTxtArea);
	}
	

	/**
	 * get the erase define button.
	 *
	 * @return the erase define button
	 */
	@Override
	public Button getEraseDefineButton() {
		return cqlDefinitionsView.getDefineButtonBar().getEraseButton();
	}

	/**
	 * get the erase define button.
	 *
	 * @return the erase parameter button
	 */
	@Override
	public Button getEraseParameterButton() {
		return cqlParametersView.getParameterButtonBar().getEraseButton();
	}

	/**
	 * get the erase define button.
	 *
	 * @return the erase parameter button
	 */
	@Override
	public Button getEraseFunctionButton() {
		return cqlFunctionsView.getFunctionButtonBar().getEraseButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getSaveFunctionButton()
	 */
	@Override
	public Button getSaveFunctionButton() {
		return cqlFunctionsView.getFunctionButtonBar().getSaveButton();
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

	/**
	 * Sets the function body ace editor.
	 *
	 * @param functionBodyAceEditor
	 *            the new function body ace editor
	 */
	public void setFunctionBodyAceEditor(AceEditor functionBodyAceEditor) {
		cqlFunctionsView.setFunctionBodyAceEditor(functionBodyAceEditor);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getAddNewArgument()
	 */
	@Override
	public Button getAddNewArgument() {
		return cqlFunctionsView.getAddNewArgument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getContextPATToggleSwitch()
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getAddParameterButton()
	 */
	@Override
	public Button getAddParameterButton() {
		return cqlParametersView.getParameterButtonBar().getSaveButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getParameterButtonBar()
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionArgumentList()
	 */
	@Override
	public List<CQLFunctionArgument> getFunctionArgumentList() {
		return cqlFunctionsView.getFunctionArgumentList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setFunctionArgumentList(java.util.List)
	 */
	@Override
	public void setFunctionArgumentList(List<CQLFunctionArgument> functionArgumentList) {
		cqlFunctionsView.setFunctionArgumentList(functionArgumentList);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getFunctionArgNameMap()
	 */
	@Override
	public Map<String, CQLFunctionArgument> getFunctionArgNameMap() {
		return cqlFunctionsView.getFunctionArgNameMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * setFunctionArgNameMap(java.util.HashMap)
	 */
	@Override
	public void setFunctionArgNameMap(HashMap<String, CQLFunctionArgument> functionArgNameMap) {
		cqlFunctionsView.setFunctionArgNameMap(functionArgNameMap);
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
	@Override
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
	}
	

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
		if(cqlLeftNavBarPanelView.getGlobalWarningConfirmationMessageAlert() != null)
			cqlLeftNavBarPanelView.getGlobalWarningConfirmationMessageAlert().clearAlert();
		cqlLeftNavBarPanelView.getDeleteConfirmationMessgeAlert().clearAlert();
		hideAceEditorAutoCompletePopUp();

	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#
	 * getDefineInfoButton()
	 */
	@Override
	public Button getDefineInfoButton() {
		return cqlDefinitionsView.getDefineButtonBar().getInfoButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getParamInfoButton()
	 */
	@Override
	public Button getParamInfoButton() {
		return cqlParametersView.getParameterButtonBar().getInfoButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getFuncInfoButton()
	 */
	@Override
	public Button getFuncInfoButton() {
		return cqlFunctionsView.getFunctionButtonBar().getInfoButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getDefineTimingExpButton()
	 */
	@Override
	public Button getDefineTimingExpButton() {
		return getDefineButtonBar().getTimingExpButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getFuncTimingExpButton()
	 */
	@Override
	public Button getFuncTimingExpButton() {
		return getFunctionButtonBar().getTimingExpButton();
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
	public CQLAppliedValueSetView getQdmView() {
		return qdmView;
	}
	

	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getCqlFunctionsView()
	 */
	@Override
	public CQLFunctionsView getCqlFunctionsView() {
		return cqlFunctionsView;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay#getCqlLeftNavBarPanelView()
	 */
	@Override
	public CQLLeftNavBarPanelView getCqlLeftNavBarPanelView(){
		return cqlLeftNavBarPanelView;
	}
	
	@Override
	public CQLParametersView getCQLParametersView(){
		return cqlParametersView;
	}
	
	@Override
	public CQlDefinitionsView getCQlDefinitionsView(){
		return cqlDefinitionsView;
	}
	
	@Override
	public CQLGeneralInformationView getCqlGeneralInformationView(){
		return generalInformationView;
	}

}
