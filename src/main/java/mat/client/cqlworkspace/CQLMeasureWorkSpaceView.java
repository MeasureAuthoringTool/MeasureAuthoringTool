package mat.client.cqlworkspace;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.Mat;
import mat.client.cqlworkspace.codes.CQLCodesView;
import mat.client.cqlworkspace.components.CQLComponentLibraryPresenter;
import mat.client.cqlworkspace.components.CQLComponentLibraryView;
import mat.client.cqlworkspace.definitions.CQLDefinitionsView;
import mat.client.cqlworkspace.functions.CQLFunctionsView;
import mat.client.cqlworkspace.generalinformation.MeasureCQLGeneralInformationView;
import mat.client.cqlworkspace.includedlibrary.CQLIncludeLibraryView;
import mat.client.cqlworkspace.parameters.CQLParametersView;
import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetView;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.MatContext;
import mat.client.shared.MessagePanel;
import mat.client.shared.SpacerWidget;
import mat.model.clause.ModelTypeHelper;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

public class CQLMeasureWorkSpaceView implements CQLWorkspaceView {
    HorizontalPanel mainHorizontalPanel = new HorizontalPanel();
    private VerticalPanel mainPanel = new VerticalPanel();
    private VerticalPanel mainVerticalPanel = new VerticalPanel();
    private FlowPanel mainFlowPanel = new FlowPanel();
    public String clickedMenu = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
    public String nextClickedMenu = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
    VerticalPanel vp = new VerticalPanel();
    private CQLAppliedValueSetView valueSetView;
    private CQLIncludeLibraryView inclView;
    private CQLCodesView codesView;
    private CQLParametersView cqlParametersView;
    private CQLDefinitionsView cqlDefinitionsView;
    private CQLFunctionsView cqlFunctionsView;
    private CQLLibraryEditorView cqlViewCQLView;
    private CQLLeftNavBarPanelView cqlLeftNavBarPanelView;
    private CQLComponentLibraryPresenter componentPresenter;
    private MeasureCQLGeneralInformationView generalInformationView;
    private HelpBlock helpBlock = new HelpBlock();
    MessagePanel messagePanel;

    public CQLMeasureWorkSpaceView() {
        generalInformationView = new MeasureCQLGeneralInformationView();
        componentPresenter = new CQLComponentLibraryPresenter();
        cqlParametersView = new CQLParametersView();
        cqlDefinitionsView = new CQLDefinitionsView(() -> ModelTypeHelper.FHIR.equalsIgnoreCase(MatContext.get().getCurrentMeasureModel()));
        cqlFunctionsView = new CQLFunctionsView();
        codesView = new CQLCodesView();
        valueSetView = new CQLAppliedValueSetView();
        inclView = new CQLIncludeLibraryView();
        cqlViewCQLView = new CQLLibraryEditorView();
        cqlLeftNavBarPanelView = new CQLLeftNavBarPanelView();

        resetAll();
    }


    public void buildView(MessagePanel messagePanel, HelpBlock helpBlock, boolean isEditable) {
        this.messagePanel = messagePanel;
        this.messagePanel.getElement().getStyle().setProperty("marginLeft", "5px");
        this.helpBlock = helpBlock;
        resetAll();
        unsetEachSectionSelectedObject();

        buildGeneralInformation(isEditable);
        mainFlowPanel.setWidth("700px");
        mainPanel.getElement().setId("CQLWorkspaceView.containerPanel");
        mainPanel.add(new SpacerWidget());

        mainPanel.add(messagePanel);
        mainPanel.add(helpBlock);
        mainPanel.add(mainFlowPanel);

        resetMessageDisplay();

        mainHorizontalPanel.addStyleName("cqlRightMessage");
        mainHorizontalPanel.add(cqlLeftNavBarPanelView.buildMeasureLibCQLView());
        mainHorizontalPanel.add(mainPanel);

        mainVerticalPanel.add(mainHorizontalPanel);
        mainVerticalPanel.add(valueSetView.getCellTableMainPanel());
        mainVerticalPanel.add(codesView.getCellTableMainPanel());

    }

    public void buildCQLFileView(boolean isEditorEditable, boolean isPageEditable) {
        unsetEachSectionSelectedObject();
        mainFlowPanel.clear();
        mainFlowPanel.add(cqlViewCQLView.buildView(isEditorEditable, isPageEditable));

    }

    public void buildGeneralInformation(boolean isEditable) {
        unsetEachSectionSelectedObject();
        mainFlowPanel.clear();
        setGeneralInfoHeading();
        mainFlowPanel.add(generalInformationView.getView(isEditable));

    }

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

    public void buildComponentsView() {
        unsetEachSectionSelectedObject();
        mainFlowPanel.clear();
        resetMessageDisplay();
        VerticalPanel componentsTopPanel = new VerticalPanel();
        componentPresenter.getView().reset();
        componentsTopPanel.add(componentPresenter.getView().asWidget());

        VerticalPanel vp = new VerticalPanel();
        vp.setStyleName("cqlRightContainer");
        vp.setWidth("700px");
        componentsTopPanel.setWidth("700px");
        componentsTopPanel.setStyleName("marginLeft15px");
        vp.add(componentsTopPanel);
        mainFlowPanel.add(vp);
    }


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

    public void buildParameterLibraryView() {
        unsetEachSectionSelectedObject();
        mainFlowPanel.clear();
        mainFlowPanel.add(cqlParametersView.getView());
        //508 Compliance for Parameter section
        getCQLParametersView().getMainParamViewVerticalPanel().setFocus(true);
    }

    public void buildDefinitionLibraryView() {
        unsetEachSectionSelectedObject();
        mainFlowPanel.clear();
        mainFlowPanel.add(cqlDefinitionsView.getView());
        //508 Compliance for Definition section
        getCQLDefinitionsView().getMainDefineViewVerticalPanel().setFocus(true);
    }


    public void buildFunctionLibraryView() {
        unsetEachSectionSelectedObject();
        mainFlowPanel.clear();
        mainFlowPanel.add(cqlFunctionsView.getView(MatContext.get().getMeasureLockService().checkForEditPermission()));
        //508 Compliance for Function section
        getCQLFunctionsView().getMainFunctionVerticalPanel().setFocus(true);
    }

    public void resetAll() {
        mainFlowPanel.clear();
        inclView.setAliasNameTxtArea("");
        cqlLeftNavBarPanelView.reset();
        generalInformationView.resetAll();
        cqlParametersView.resetAll();
        cqlDefinitionsView.resetAll();
        cqlFunctionsView.resetAll();
        cqlViewCQLView.resetAll();
        resetMessageDisplay();
    }


    public VerticalPanel getMainPanel() {
        return mainPanel;
    }

    public HorizontalPanel getMainHPanel() {
        return mainHorizontalPanel;
    }

    public Widget asWidget() {
        return mainVerticalPanel;
    }

    public String getClickedMenu() {
        return clickedMenu;
    }

    public void setClickedMenu(String clickedMenu) {
        this.clickedMenu = clickedMenu;
    }

    public String getNextClickedMenu() {
        return nextClickedMenu;
    }

    public void setNextClickedMenu(String nextClickedMenu) {
        this.nextClickedMenu = nextClickedMenu;
    }

    public FlowPanel getMainFlowPanel() {
        return mainFlowPanel;
    }

    public void hideAceEditorAutoCompletePopUp() {
        cqlDefinitionsView.hideAceEditorAutoCompletePopUp();
        cqlParametersView.hideAceEditorAutoCompletePopUp();
        cqlFunctionsView.hideAceEditorAutoCompletePopUp();
    }

    public void hideInformationDropDown() {
        cqlDefinitionsView.getDefineButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
        cqlParametersView.getParameterButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
        cqlFunctionsView.getFunctionButtonBar().getInfoButtonGroup().getElement().setAttribute("class", "btn-group");
    }


    public void unsetEachSectionSelectedObject() {
        cqlLeftNavBarPanelView.resetSelectedObjects();
        cqlFunctionsView.getFunctionArgNameMap().clear();
        if (!cqlFunctionsView.getFunctionArgumentList().isEmpty()) {
            cqlFunctionsView.getFunctionArgumentList().clear();
        }
        valueSetView.clearCellTableMainPanel();
        codesView.clearCellTableMainPanel();
    }

    public void resetMessageDisplay() {
        if (messagePanel != null) {
            messagePanel.clearAlerts();
        }
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

    public void resetAllSections() {

    }

    public MeasureCQLGeneralInformationView getCqlGeneralInformationView() {
        return generalInformationView;
    }

    public CQLIncludeLibraryView getIncludeView() {
        return inclView;
    }

    public CQLAppliedValueSetView getValueSetView() {
        return valueSetView;
    }

    public CQLParametersView getCQLParametersView() {
        return cqlParametersView;
    }

    public CQLLibraryEditorView getCQLLibraryEditorView() {
        return cqlViewCQLView;
    }

    public CQLCodesView getCodesView() {
        return codesView;
    }

    public void setGeneralInfoHeading() {
        getCqlGeneralInformationView().setHeading("CQL Workspace > General Information", "generalInfoMainHPanel_HPanel");
        Mat.focusSkipLists("MeasureComposer");
    }

    public HelpBlock getHelpBlock() {
        return helpBlock;
    }

    public void setHelpBlock(HelpBlock helpBlock) {
        this.helpBlock = helpBlock;
    }


    public CQLComponentLibraryView getComponentView() {
        return componentPresenter.getView();
    }


    @Override
    public AceEditor getViewCQLEditor() {
        return getIncludeView().getViewCQLEditor();
    }


    @Override
    public CQLLeftNavBarPanelView getCQLLeftNavBarPanelView() {
        return cqlLeftNavBarPanelView;
    }


    @Override
    public CQLDefinitionsView getCQLDefinitionsView() {
        return cqlDefinitionsView;
    }


    @Override
    public CQLFunctionsView getCQLFunctionsView() {
        return cqlFunctionsView;
    }

    public MeasureCQLGeneralInformationView getGeneralInformationView() {
        return generalInformationView;
    }

    public void setGeneralInformationView(MeasureCQLGeneralInformationView generalInformationView) {
        this.generalInformationView = generalInformationView;
    }
}