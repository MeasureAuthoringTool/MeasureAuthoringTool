package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;

public class CQLWorkSpaceView implements CQLWorkSpacePresenter.ViewDisplay {

	HorizontalPanel mainHPPanel = new HorizontalPanel();

	private VerticalPanel mainPanel = new VerticalPanel();

	private VerticalPanel mainVPanel = new VerticalPanel();

	private FlowPanel mainFlowPanel = new FlowPanel();

	public String clickedMenu = "general";

	public String nextClickedMenu = "general";

	VerticalPanel vp = new VerticalPanel();

	private CQLAppliedValueSetView valueSetView;

	private CQLIncludeLibraryView inclView;

	private CQLCodesView codesView;

	private CQLGeneralInformationView generalInformationView;
	
	private CQLParametersView cqlParametersView;
	
	private CQlDefinitionsView cqlDefinitionsView;
	
	private CQLFunctionsView cqlFunctionsView;
	
	private CQLView cqlViewCQLView;
	
	private CQLLeftNavBarPanelView cqlLeftNavBarPanelView;
	
	private HelpBlock helpBlock = new HelpBlock();

	public CQLWorkSpaceView() {
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

	@Override
	public void buildCQLFileView(boolean isEditable) {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(cqlViewCQLView.buildView(isEditable));

	}


	@Override
	public void buildGeneralInformation() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		setGeneralInfoHeading();
		mainFlowPanel.add(generalInformationView.getView());

	}


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

	@Override
	public void buildParameterLibraryView() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(cqlParametersView.getView());
		//508 Compliance for Parameter section
		getCqlLeftNavBarPanelView().setFocus(getCQLParametersView().getMainParamViewVerticalPanel());
	}

	@Override
	public void buildDefinitionLibraryView() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(cqlDefinitionsView.getView());
		//508 Compliance for Definition section
		getCqlLeftNavBarPanelView().setFocus(getCQlDefinitionsView().getMainDefineViewVerticalPanel());
	}


	@Override
	public void buildFunctionLibraryView() {
		unsetEachSectionSelectedObject();
		mainFlowPanel.clear();
		mainFlowPanel.add(cqlFunctionsView.getView(MatContext.get().getMeasureLockService().checkForEditPermission()));
		//508 Compliance for Function section
		getCqlLeftNavBarPanelView().setFocus(getCqlFunctionsView().getMainFunctionVerticalPanel());
	}

	@Override
	public void resetAll() {
		mainFlowPanel.clear();
		cqlLeftNavBarPanelView.getRightHandNavPanel().clear();
		inclView.setAliasNameTxtArea("");
		
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
	

	@Override
	public VerticalPanel getMainPanel() {
		return mainPanel;
	}

	@Override
	public HorizontalPanel getMainHPanel() {
		return mainHPPanel;
	}

	@Override
	public Widget asWidget() {
		return mainVPanel;
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
	public FlowPanel getMainFlowPanel() {
		return mainFlowPanel;
	}

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
	

	public void unsetEachSectionSelectedObject() {
		cqlLeftNavBarPanelView.setCurrentSelectedDefinitionObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedParamerterObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedFunctionObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedFunctionArgumentObjId(null);
		cqlLeftNavBarPanelView.setCurrentSelectedFunctionArgumentName(null);
		cqlLeftNavBarPanelView.setCurrentSelectedIncLibraryObjId(null);
		cqlFunctionsView.getFunctionArgNameMap().clear();
		if (!cqlFunctionsView.getFunctionArgumentList().isEmpty()) {
			cqlFunctionsView.getFunctionArgumentList().clear();
		}
		valueSetView.clearCellTableMainPanel();
		codesView.clearCellTableMainPanel();
	}
	
	@Override
	public void resetMessageDisplay() {
		cqlLeftNavBarPanelView.getWarningMessageAlert().clearAlert();
		cqlLeftNavBarPanelView.getSuccessMessageAlert().clearAlert();
		cqlLeftNavBarPanelView.getErrorMessageAlert().clearAlert();
		cqlLeftNavBarPanelView.getWarningConfirmationMessageAlert().clearAlert();
		if(cqlLeftNavBarPanelView.getGlobalWarningConfirmationMessageAlert() != null)
			cqlLeftNavBarPanelView.getGlobalWarningConfirmationMessageAlert().clearAlert();
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

	@Override
	public CQLLeftNavBarPanelView getCqlLeftNavBarPanelView(){
		return cqlLeftNavBarPanelView;
	}

	@Override
	public CQLGeneralInformationView getCqlGeneralInformationView(){
		return generalInformationView;
	}

	@Override
	public CQLIncludeLibraryView getIncludeView() {
		return inclView;
	}

	@Override
	public CQLAppliedValueSetView getValueSetView() {
		return valueSetView;
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
	public CQLFunctionsView getCqlFunctionsView() {
		return cqlFunctionsView;
	}

	@Override
	public CQLView getViewCQLView(){
		return cqlViewCQLView;
	}

	@Override
	public CQLCodesView getCodesView() {
		return codesView;
	}
	
	@Override
	public void setGeneralInfoHeading(){
		getCqlGeneralInformationView().setHeading("CQL Workspace > General Information", "generalInfoMainHPanel_HPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	@Override
	public HelpBlock getHelpBlock() {
		return helpBlock;
	}

	public void setHelpBlock(HelpBlock helpBlock) {
		this.helpBlock = helpBlock;
	}
	
}
