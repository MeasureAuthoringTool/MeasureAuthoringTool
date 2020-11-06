package mat.client.cqlworkspace;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.Mat;
import mat.client.buttons.DefinitionFunctionButtonToolBar;
import mat.client.cqlworkspace.codes.CQLCodesView;
import mat.client.cqlworkspace.components.CQLComponentLibraryView;
import mat.client.cqlworkspace.definitions.CQLDefinitionsView;
import mat.client.cqlworkspace.functions.CQLFunctionsView;
import mat.client.cqlworkspace.generalinformation.StandaloneCQLGeneralInformationView;
import mat.client.cqlworkspace.includedlibrary.CQLIncludeLibraryView;
import mat.client.cqlworkspace.parameters.CQLParametersView;
import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetView;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.MatContext;
import mat.client.shared.MessagePanel;
import mat.client.shared.SpacerWidget;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLFunctionArgument;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import java.util.List;
import java.util.Map;

public class CQLStandaloneWorkSpaceView implements CQLWorkspaceView {
    HorizontalPanel mainHorizontalPanel = new HorizontalPanel();
    private VerticalPanel mainPanel = new VerticalPanel();
    private VerticalPanel mainVerticalPanel = new VerticalPanel();
    private FlowPanel mainFlowPanel = new FlowPanel();
    private CQLAppliedValueSetView valueSetView;
    private CQLCodesView codesView;
    private CQLIncludeLibraryView cqlIncludeLibraryView;
    private StandaloneCQLGeneralInformationView generalInformationView;
    private CQLParametersView cqlParametersView;
    private CQLDefinitionsView cqlDefinitionsView;
    private CQLFunctionsView cqlFunctionsView;
    private CQLLibraryEditorView cqlViewCQLView;
    private CQLLeftNavBarPanelView cqlLeftNavBarPanelView;
    public String clickedMenu = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
    public String nextClickedMenu = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
    private HorizontalPanel lockedButtonHPanel = new HorizontalPanel();
    private HelpBlock helpBlock = new HelpBlock();
    private MessagePanel messagePanel;

    public CQLStandaloneWorkSpaceView() {
        generalInformationView = new StandaloneCQLGeneralInformationView();
        cqlParametersView = new CQLParametersView();
        cqlDefinitionsView = new CQLDefinitionsView(() -> ModelTypeHelper.FHIR.equalsIgnoreCase(MatContext.get().getCurrentCQLLibraryModelType()));
        cqlFunctionsView = new CQLFunctionsView();
        codesView = new CQLCodesView();
        valueSetView = new CQLAppliedValueSetView();
        cqlIncludeLibraryView = new CQLIncludeLibraryView();
        cqlViewCQLView = new CQLLibraryEditorView();
        cqlLeftNavBarPanelView = new CQLLeftNavBarPanelView();
        resetAll();
    }

    public void buildView(MessagePanel messagePanel, HelpBlock helpBlock, boolean isEditable) {
        resetAll();
        unsetEachSectionSelectedObject();
        this.messagePanel = messagePanel;
        this.messagePanel.getElement().getStyle().setProperty("marginLeft", "5px");
        this.helpBlock = helpBlock;
        buildGeneralInformation(isEditable);
        mainFlowPanel.setWidth("700px");
        mainPanel.getElement().setId("CQLStandaloneWorkSpaceView.containerPanel");
        buildLockedButtonPanel();
        mainPanel.add(new SpacerWidget());

        mainPanel.add(messagePanel);
        mainPanel.add(helpBlock);
        mainPanel.add(mainFlowPanel);

        resetMessageDisplay();

        mainHorizontalPanel.addStyleName("cqlRightMessage");
        mainHorizontalPanel.add(cqlLeftNavBarPanelView.buildMeasureLibCQLView());
        cqlLeftNavBarPanelView.getComponentsTab().setVisible(false);
        mainHorizontalPanel.add(mainPanel);
        mainVerticalPanel.add(mainHorizontalPanel);
        mainVerticalPanel.add(valueSetView.getCellTableMainPanel());
        mainVerticalPanel.add(codesView.getCellTableMainPanel());

    }


    public void buildGeneralInformation(boolean isEditable) {
        unsetEachSectionSelectedObject();
        mainFlowPanel.clear();
        setGeneralInfoHeading();
        mainFlowPanel.add(generalInformationView.getView(isEditable));
    }

    public void buildIncludesView() {
        unsetEachSectionSelectedObject();
        mainFlowPanel.clear();
        resetMessageDisplay();
        VerticalPanel includesTopPanel = new VerticalPanel();
        cqlIncludeLibraryView.resetToDefault();
        cqlIncludeLibraryView.buildAddNewAliasView();

        includesTopPanel.add(cqlIncludeLibraryView.asWidget());

        VerticalPanel vp = new VerticalPanel();
        vp.setStyleName("cqlRightContainer");
        vp.setWidth("700px");
        includesTopPanel.setWidth("700px");
        includesTopPanel.setStyleName("marginLeft15px");
        vp.add(includesTopPanel);
        mainFlowPanel.add(vp);
    }


    public void buildCQLFileView(boolean isEditorEditable, boolean isPageEditable) {
        unsetEachSectionSelectedObject();
        mainFlowPanel.clear();
        mainFlowPanel.add(cqlViewCQLView.buildView(isEditorEditable, isPageEditable));

    }


    public void buildParameterLibraryView() {
        unsetEachSectionSelectedObject();
        mainFlowPanel.clear();
        mainFlowPanel.add(cqlParametersView.getView());
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
        mainFlowPanel.add(cqlFunctionsView.getView(MatContext.get().getLibraryLockService().checkForEditPermission()));
        //508 Compliance for Function section
        getCQLFunctionsView().getMainFunctionVerticalPanel().setFocus(true);
    }

    public void unsetEachSectionSelectedObject() {
        cqlLeftNavBarPanelView.resetSelectedObjects();
        cqlFunctionsView.getFunctionArgNameMap().clear();
        if (cqlFunctionsView.getFunctionArgumentList().size() > 0) {
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
        cqlIncludeLibraryView.resetFromGroup();
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


    public VerticalPanel getMainPanel() {
        return mainPanel;
    }

    public Widget asWidget() {
        return mainVerticalPanel;
    }

    private void buildLockedButtonPanel() {
        lockedButtonHPanel.clear();
        if (MatContext.get().getCurrentLibraryInfo().isEditable() &&
                MatContext.get().getCurrentLibraryInfo().isLocked()) {
            Icon lockIcon = new Icon(IconType.LOCK);
            lockIcon.setSize(IconSize.LARGE);
            lockIcon.setColor("#daa520");
            lockIcon.setId("LockedIcon");
            lockIcon.setTitle("Locked by " + MatContext.get().getCurrentCQLLibraryLockedUserName());
            lockedButtonHPanel.add(lockIcon);
            String label = MatContext.get().getCurrentCQLLibraryLockedUserName();
            if (label.length() > 20) {
                label = label.substring(0, 19);
            }
            HTML html = new HTML("<p>Locked by " + label + "</p>");
            html.setStyleName("standAloneLockedLabel");
            html.getElement().setAttribute("id", "LockedBy");
            lockedButtonHPanel.add(html);
            lockedButtonHPanel.getElement().setAttribute("id", "StandAloneCQL_LockedButtonHPanel");
            lockedButtonHPanel.setStyleName("standAloneLockedWidget");
            mainPanel.add(lockedButtonHPanel);
        } else {
            lockedButtonHPanel.removeStyleName("standAloneLockedWidget");
        }

    }


    public HorizontalPanel getMainHPanel() {
        return mainHorizontalPanel;
    }


    public FlowPanel getMainFlowPanel() {
        return mainFlowPanel;
    }

    public void resetAll() {
        mainFlowPanel.clear();
        cqlLeftNavBarPanelView.reset();
        cqlIncludeLibraryView.setAliasNameTxtArea("");
        System.out.println(" in resetAll doing setText");

        generalInformationView.resetAll();
        cqlParametersView.resetAll();
        cqlDefinitionsView.resetAll();
        cqlFunctionsView.resetAll();
        cqlViewCQLView.resetAll();
        resetMessageDisplay();
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

    public CQLParametersView getCQLParametersView() {
        return cqlParametersView;
    }

    public CQLDefinitionsView getCQLDefinitionsView() {
        return cqlDefinitionsView;
    }

    public CQLFunctionsView getCQLFunctionsView() {
        return cqlFunctionsView;
    }

    public CQLIncludeLibraryView getCqlIncludeLibraryView() {
        return cqlIncludeLibraryView;
    }

    public StandaloneCQLGeneralInformationView getCqlGeneralInformationView() {
        return generalInformationView;
    }

    public CQLIncludeLibraryView getIncludeView() {
        return cqlIncludeLibraryView;
    }


    public TextBox getAliasNameTxtArea() {
        return getIncludeView().getAliasNameTxtArea();
    }


    public AceEditor getViewCQLEditor() {
        return getIncludeView().getViewCQLEditor();
    }

    public TextBox getOwnerNameTextBox() {
        return getIncludeView().getOwnerNameTextBox();
    }


    public void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList) {
        cqlFunctionsView.createAddArgumentViewForFunctions(argumentList, MatContext.get().getLibraryLockService().checkForEditPermission());
    }

    public DefinitionFunctionButtonToolBar getParameterButtonBar() {
        return cqlParametersView.getParameterButtonBar();
    }

    public DefinitionFunctionButtonToolBar getFunctionButtonBar() {
        return cqlFunctionsView.getFunctionButtonBar();
    }

    public AceEditor getFunctionBodyAceEditor() {
        return cqlFunctionsView.getFunctionBodyAceEditor();
    }

    public Map<String, CQLFunctionArgument> getFunctionArgNameMap() {
        return cqlFunctionsView.getFunctionArgNameMap();
    }


    public void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList, boolean isEditable) {
        cqlFunctionsView.createAddArgumentViewForFunctions(argumentList, isEditable);
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


    public CQLAppliedValueSetView getValueSetView() {
        return valueSetView;
    }


    public CQLCodesView getCodesView() {
        return codesView;
    }

    public HorizontalPanel getLockedButtonVPanel() {
        return lockedButtonHPanel;
    }


    public void setLockedButtonVPanel(HorizontalPanel lockedButtonVPanel) {
        this.lockedButtonHPanel = lockedButtonVPanel;
    }

    public CQLLibraryEditorView getCQLLibraryEditorView() {
        return cqlViewCQLView;
    }

    public void setGeneralInfoHeading() {
        getCqlGeneralInformationView().setHeading("CQL Library Workspace > General Information", "generalInfoMainHPanel_HPanel");
        Mat.focusSkipLists("CqlComposer");
    }

    public HelpBlock getHelpBlock() {
        return helpBlock;
    }

    public void setHelpBlock(HelpBlock helpBlock) {
        this.helpBlock = helpBlock;
    }

    @Override
    public CQLLeftNavBarPanelView getCQLLeftNavBarPanelView() {
        return cqlLeftNavBarPanelView;
    }

    @Override
    public CQLComponentLibraryView getComponentView() {
        return null;
    }
}