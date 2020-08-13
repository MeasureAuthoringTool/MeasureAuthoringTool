package mat.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.thirdparty.guava.common.annotations.VisibleForTesting;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.clause.QDMAppliedSelectionPresenter;
import mat.client.clause.QDMAppliedSelectionView;
import mat.client.clause.clauseworkspace.presenter.ClauseWorkSpacePresenter;
import mat.client.cqlworkspace.CQLMeasureWorkSpacePresenter;
import mat.client.cqlworkspace.CQLMeasureWorkSpaceView;
import mat.client.event.MATClickHandler;
import mat.client.event.MeasureEditEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.measuredetails.MeasureDetailsPresenter;
import mat.client.measure.metadata.events.ContinueToMeasurePackageEvent;
import mat.client.measurepackage.MeasurePackagePresenter;
import mat.client.measurepackage.MeasurePackagerView;
import mat.client.populationworkspace.CQLPopulationWorkSpacePresenter;
import mat.client.populationworkspace.CQLPopulationWorkSpaceView;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.FocusableWidget;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.PreviousContinueButtonBar;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.util.FeatureFlagConstant;
import mat.shared.ConstantMessages;
import mat.shared.model.util.MeasureDetailsUtil;
import org.gwtbootstrap3.client.ui.Button;

import java.util.LinkedList;
import java.util.List;

public class MeasureComposerPresenter implements MatPresenter, MeasureHeading, Enableable, TabObserver {
    MatTabLayoutPanel targetTabLayout;
    MatPresenter targetPresenter;
    MatPresenter sourcePresenter;
    HandlerRegistration yesHandler;
    HandlerRegistration noHandler;
    private static FocusableWidget subSkipContentHolder;
    private List<MatPresenter> presenterList;
    private PreviousContinueButtonBar buttonBar = new PreviousContinueButtonBar();
    private ClauseWorkSpacePresenter clauseWorkSpacePresenter = new ClauseWorkSpacePresenter();
    private SimplePanel emptyWidget = new SimplePanel();
    private ContentWithHeadingWidget measureComposerContent = new ContentWithHeadingWidget();
    private String measureComposerTab;
    private MatTabLayoutPanel measureComposerTabLayout;
    @SuppressWarnings("unused")
    private MeasurePackagePresenter measurePackagePresenter;
    private MeasureDetailsPresenter measureDetailsPresenter;
    private CQLMeasureWorkSpaceView cqlWorkspaceView;
    private CQLMeasureWorkSpacePresenter cqlWorkspacePresenter;
    private static String MEASURE_COMPOSER = "MeasureComposer";
    private static String MEASURE_PACKAGER = "Measure Packager";


    class EnterKeyDownHandler implements KeyDownHandler {
        private int i = 0;

        public EnterKeyDownHandler(int index) {
            i = index;
        }

        @Override
        public void onKeyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                measureComposerTabLayout.selectTab(i);
            }
        }
    }

    public static void setSubSkipEmbeddedLink(String name) {
        if (subSkipContentHolder == null) {
            subSkipContentHolder = new FocusableWidget(SkipListBuilder.buildSkipList("Skip to Sub Content"));
        }
        Mat.removeInputBoxFromFocusPanel(subSkipContentHolder.getElement());
        Widget w = SkipListBuilder.buildSubSkipList(name);
        subSkipContentHolder.clear();
        subSkipContentHolder.add(w);
        subSkipContentHolder.setFocus(true);
    }

    @SuppressWarnings("unchecked")
    public MeasureComposerPresenter() {
        presenterList = new LinkedList<MatPresenter>();
        buttonBar.getElement().setId("buttonBar_PreviousContinueButtonBar");
        emptyWidget.getElement().setId("emptyWidget_SimplePanel");

        measurePackagePresenter = (MeasurePackagePresenter) buildMeasurePackageWidget(this);
        measureDetailsPresenter = (MeasureDetailsPresenter) buildMeasureDetailsPresenter(this);

        measureComposerTabLayout = new MatTabLayoutPanel(this);
        measureComposerTabLayout.getElement().setAttribute("id", "measureComposerTabLayout");
        presenterList.add(measureDetailsPresenter);
        measureComposerTabLayout.add(measureDetailsPresenter.getWidget(), "Measure Details", true);

        MatPresenter cqlWorkspacePresenter = buildCQLWorkSpaceTab();
        measureComposerTabLayout.add(cqlWorkspacePresenter.getWidget(), "CQL Workspace", true);
        presenterList.add(cqlWorkspacePresenter);
        MatPresenter cqlPopulationWorkspacePresenter = buildCQLPopulationWorkspaceTab();
        measureComposerTabLayout.add(cqlPopulationWorkspacePresenter.getWidget(), "Population Workspace", true);
        presenterList.add(cqlPopulationWorkspacePresenter);
        measureComposerTabLayout.add(measurePackagePresenter.getWidget(), "Measure Packager", true);
        presenterList.add(measurePackagePresenter);
        measureComposerTabLayout.add(clauseWorkSpacePresenter.getWidget(), "Clause Workspace", true);
        presenterList.add(clauseWorkSpacePresenter);
        MatPresenter appliedQDMPresenter = buildAppliedQDMPresenter();
        measureComposerTabLayout.add(appliedQDMPresenter.getWidget(), "QDM Elements", true);
        presenterList.add(appliedQDMPresenter);
        measureComposerTabLayout.setHeight("98%");
        measureComposerTab = ConstantMessages.MEASURE_COMPOSER_TAB;
        MatContext.get().tabRegistry.put(measureComposerTab, measureComposerTabLayout);
        MatContext.get().enableRegistry.put(measureComposerTab, this);
        measureComposerTabLayout.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            @SuppressWarnings("rawtypes")
            public void onSelection(final SelectionEvent event) {
                int index = ((SelectionEvent<Integer>) event).getSelectedItem();
                // suppressing token dup
                String newToken = measureComposerTab + index;
                if (!History.getToken().equals(newToken)) {
                    MeasureSelectedEvent mse = MatContext.get().getCurrentMeasureInfo();
                    String msg = " [measure] " + mse.getMeasureName() + " [version] " + mse.getMeasureVersion();
                    String mid = mse.getMeasureId();
                    MatContext.get().recordTransactionEvent(mid, null, "MEASURE_TAB_EVENT", newToken + msg, ConstantMessages.DB_LOG);
                    History.newItem(newToken, false);
                }
            }
        });

        measureComposerContent.setContent(emptyWidget);
        measureComposerContent.setFooter(buttonBar);

        buttonBar.getPreviousButton().addClickHandler(new MATClickHandler() {

            @Override
            protected boolean doAlert() {
                return true;
            }

            @SuppressWarnings("rawtypes")
            @Override
            public void onEvent(GwtEvent arg0) {
                int selectedIndex = measureComposerTabLayout.getSelectedIndex();
                if (selectedIndex != 0) {
                    measureComposerTabLayout.selectPreviousTab();
                } else {
                    buttonBar.subState = selectedIndex;
                    beforeDisplay();
                }
            }

        });

        buttonBar.getContinueButton().addClickHandler(new MATClickHandler() {
            @Override
            protected boolean doAlert() {
                return true;
            }

            @SuppressWarnings("rawtypes")
            @Override
            protected void onEvent(GwtEvent event) {
                measureComposerTabLayout.selectNextTab();
            }

        });

        measureComposerTabLayout.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                //int index = ((SelectionEvent<Integer>)event).getSelectedItem();
                buttonBar.state = measureComposerTabLayout.getSelectedIndex();
                buttonBar.setPageNamesOnState();
            }
        });

        MatContext.get().getEventBus().addHandler(ContinueToMeasurePackageEvent.TYPE, new ContinueToMeasurePackageEvent.Handler() {
            @Override
            public void onContinueToMeasurePackage(ContinueToMeasurePackageEvent event) {
                buttonBar.state = measureComposerTabLayout.getSelectedIndex();
                buttonBar.setPageNamesOnState();
            }
        });

        MatContext.get().getEventBus().addHandler(MeasureEditEvent.TYPE, event -> disableMeasurePackageTabForFhirMeasures(MatContext.get().getCurrentMeasureModel()));
    }

    public void disableMeasurePackageTabForFhirMeasures(String currentMeasureModel) {
        for(int i = 0; i < measureComposerTabLayout.getTabBar().getTabCount(); i++) {
            if(measureComposerTabLayout.getTabBar().getTabHTML(i).contains(MEASURE_PACKAGER)) {
                if(MeasureDetailsUtil.isPackageable(currentMeasureModel, MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR))) {
                    measureComposerTabLayout.getTabBar().setTabEnabled(i, false);
                } else {
                    measureComposerTabLayout.getTabBar().setTabEnabled(i, true);
                }
            }
        }
    }

    @Override
    public void beforeClosingDisplay() {
        if (MatContext.get().isMeasureDeleted()) {
            MatContext.get().getCurrentMeasureInfo().setMeasureId("");
            MatContext.get().setMeasureDeleted(false);
        }
        MatContext.get().getMeasureLockService().releaseMeasureLock();
        Command waitForUnlock = new Command() {
            @Override
            public void execute() {
                if (!MatContext.get().getMeasureLockService().isResettingLock()) {
                    notifyCurrentTabOfClosing();
                    measureComposerTabLayout.updateHeaderSelection(0);
                    measureComposerTabLayout.setSelectedIndex(0);
                    buttonBar.state = measureComposerTabLayout.getSelectedIndex();
                    buttonBar.setPageNamesOnState();
                } else {
                    Scheduler.get().scheduleDeferred(this);
                }
            }
        };
        if (MatContext.get().getMeasureLockService().isResettingLock()) {
            waitForUnlock.execute();
            //This is done to reset measure composure tab to show "No Measure Selected" as when measure is deleted,it should not show Any sub tabs under MeasureComposure.
            if (MatContext.get().getCurrentMeasureInfo() != null) {
                MatContext.get().getCurrentMeasureInfo().setMeasureId("");
            }
        } else {
            notifyCurrentTabOfClosing();
            measureComposerTabLayout.updateHeaderSelection(0);
            measureComposerTabLayout.setSelectedIndex(0);
            buttonBar.state = measureComposerTabLayout.getSelectedIndex();
            buttonBar.setPageNamesOnState();
            //This is done to reset measure composure tab to show "No Measure Selected" as when measure is deleted,it should not show Any sub tabs under MeasureComposure.
            if (MatContext.get().getCurrentMeasureInfo() != null) {
                MatContext.get().getCurrentMeasureInfo().setMeasureId("");
            }
        }

    }

    @Override
    public void beforeDisplay() {
        String currentMeasureId = MatContext.get().getCurrentMeasureId();
        String heading = buildMeasureHeading(currentMeasureId);
        if ((currentMeasureId != null) && !"".equals(currentMeasureId)) {
            if (MatContext.get().isCurrentMeasureEditable()) {
                MatContext.get().getMeasureLockService().setMeasureLock();
            }

            measureComposerContent.setHeading(heading, MEASURE_COMPOSER);
            FlowPanel fp = new FlowPanel();
            fp.getElement().setId("fp_FlowPanel");
            setSubSkipEmbeddedLink("MeasureDetailsView.deleteMeasureButton");
            fp.add(subSkipContentHolder);
            fp.add(measureComposerTabLayout);
            measureComposerContent.setContent(fp);
            MatContext.get().setVisible(buttonBar, true);
            measureComposerTabLayout.selectTab(presenterList.indexOf(measureDetailsPresenter));
            measureDetailsPresenter.beforeDisplay();
        } else {
            measureComposerContent.setHeading(heading, MEASURE_COMPOSER);
            measureComposerContent.setContent(emptyWidget);
            MatContext.get().setVisible(buttonBar, false);
        }
        Mat.focusSkipLists("MainContent");
        buttonBar.state = measureComposerTabLayout.getSelectedIndex();
        buttonBar.setPageNamesOnState();
    }

    @VisibleForTesting
    static String buildMeasureHeading(String currentMeasureId) {
        String heading = "";
        if ((currentMeasureId != null) && !"".equals(currentMeasureId)) {
            heading = MatContext.get().getCurrentMeasureName() + " ";
            String version = MatContext.get().getCurrentMeasureVersion();
            // when a measure is initially created we need to explicitly create the heading
            if (!version.startsWith("Draft") && !version.startsWith("v")) {
                version = "Draft based on v" + version;
            }
            String model = getHeadingMeasureModel();
            heading = heading + version + model;
        } else {
            heading = "No Measure Selected";
        }

        return heading;
    }

    private static String getHeadingMeasureModel() {
        String model = "";
        if (MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR)) {
            model = " (" + MeasureDetailsUtil.getModelTypeDisplayName(MatContext.get().getCurrentMeasureInfo().getMeasureModel()) + ")";
        }
        return model;
    }

    private MatPresenter buildMeasureDetailsPresenter(MeasureHeading measureHeading) {
        MeasureDetailsPresenter measureDetailsPresenter = new MeasureDetailsPresenter(measureHeading);
        return measureDetailsPresenter;
    }

    private MatPresenter buildMeasurePackageWidget(MeasureHeading measureHeading) {
        MeasurePackagerView measurePackagerView = new MeasurePackagerView();
        MeasurePackagePresenter measurePackagePresenter = new MeasurePackagePresenter(measurePackagerView, measureHeading);
        measurePackagePresenter.getWidget();
        return measurePackagePresenter;
    }

    private MatPresenter buildAppliedQDMPresenter() {
        QDMAppliedSelectionView vascProfileSelectionView = new QDMAppliedSelectionView();
        QDMAppliedSelectionPresenter vsacProfileSelectionPresenter =
                new QDMAppliedSelectionPresenter(vascProfileSelectionView);
        vsacProfileSelectionPresenter.getWidget();
        return vsacProfileSelectionPresenter;
    }

    private MatPresenter buildCQLWorkSpaceTab() {
        cqlWorkspaceView = new CQLMeasureWorkSpaceView();
        cqlWorkspacePresenter = new CQLMeasureWorkSpacePresenter(cqlWorkspaceView);
        cqlWorkspacePresenter.getWidget();
        return cqlWorkspacePresenter;
    }

    private MatPresenter buildCQLPopulationWorkspaceTab() {
        CQLPopulationWorkSpaceView cqlPopulationWorkspaceView = new CQLPopulationWorkSpaceView();
        CQLPopulationWorkSpacePresenter cqlPopulationPresenter =
                new CQLPopulationWorkSpacePresenter(cqlPopulationWorkspaceView);
        cqlPopulationPresenter.getWidget();
        return cqlPopulationPresenter;
    }

    @Override
    public Widget getWidget() {
        return measureComposerContent;
    }

    @Override
    public void setEnabled(boolean enabled) {
        buttonBar.setEnabled(enabled);
        measureComposerTabLayout.setEnabled(enabled);
    }

    @Override
    public boolean isValid() {
        MatContext.get().setErrorTabIndex(-1);
        MatContext.get().setErrorTab(false);
        int selectedIndex = measureComposerTabLayout.getSelectedIndex();
        if (presenterList.get(selectedIndex) instanceof MeasureDetailsPresenter) {
            MeasureDetailsPresenter measureDetailsPresenter = (MeasureDetailsPresenter) presenterList.get(selectedIndex);
            return !measureDetailsPresenter.isDirty();
        } else if (presenterList.get(selectedIndex) instanceof CQLMeasureWorkSpacePresenter) {
            CQLMeasureWorkSpacePresenter presenter = (CQLMeasureWorkSpacePresenter) presenterList.get(selectedIndex);
            return presenter.isCQLWorkspaceValid();
        } else if (presenterList.get(selectedIndex) instanceof CQLPopulationWorkSpacePresenter) {
            CQLPopulationWorkSpacePresenter presenter = (CQLPopulationWorkSpacePresenter) presenterList.get(selectedIndex);
            return presenter.isPopulationWorkSpaceValid();
        } else if (presenterList.get(selectedIndex) instanceof MeasurePackagePresenter) {
            MeasurePackagePresenter presenter = (MeasurePackagePresenter) presenterList.get(selectedIndex);
            return presenter.isMeasurePackageValid();
        }

        return true;
    }

    @Override
    public void updateOnBeforeSelection() {
        MatPresenter presenter = presenterList.get(measureComposerTabLayout.getSelectedIndex());
        if (presenter != null) {
            MatContext.get().setAriaHidden(presenter.getWidget(), "false");
            presenter.beforeDisplay();
        }
    }

    @Override
    public void showUnsavedChangesError() {
        int selectedIndex = measureComposerTabLayout.getSelectedIndex();
        WarningConfirmationMessageAlert saveErrorMessageAlert = null;
        String auditMessage = null;
        Button saveButton = null;
        if (presenterList.get(selectedIndex) instanceof MeasureDetailsPresenter) {
            MeasureDetailsPresenter measureDetailsPresenter = (MeasureDetailsPresenter) presenterList.get(selectedIndex);
            measureDetailsPresenter.getView().clearAlerts();
            measureDetailsPresenter.getMessagePanel().clearAlerts();
            saveErrorMessageAlert = measureDetailsPresenter.getMessagePanel().getGlobalWarningConfirmationMessageAlert();
        } else if (presenterList.get(selectedIndex) instanceof CQLMeasureWorkSpacePresenter) {
            CQLMeasureWorkSpacePresenter cqlWorkSpacePresenter = (CQLMeasureWorkSpacePresenter) presenterList.get(selectedIndex);
            cqlWorkSpacePresenter.getSearchDisplay().resetMessageDisplay();
            saveErrorMessageAlert = cqlWorkSpacePresenter.getMessagePanel().getGlobalWarningConfirmationMessageAlert();
            auditMessage = cqlWorkSpacePresenter.getSearchDisplay().getClickedMenu().toUpperCase() + "_TAB_YES_CLICKED";
        } else if (presenterList.get(selectedIndex) instanceof CQLPopulationWorkSpacePresenter) {
            CQLPopulationWorkSpacePresenter presenter = (CQLPopulationWorkSpacePresenter) presenterList.get(selectedIndex);
            saveErrorMessageAlert = presenter.getSearchDisplay().getCqlLeftNavBarPanelView().getGlobalWarningConfirmationMessageAlert();
            auditMessage = presenter.getSearchDisplay().getClickedMenu().toUpperCase() + "_TAB_YES_CLICKED";
        } else if (presenterList.get(selectedIndex) instanceof MeasurePackagePresenter) {
            MeasurePackagePresenter presenter = (MeasurePackagePresenter) presenterList.get(selectedIndex);
            presenter.getView().getSaveErrorMessageDisplayOnEdit().clearAlert();
            saveErrorMessageAlert = presenter.getView().getSaveErrorMessageDisplay();
            saveButton = presenter.getView().getPackageGroupingWidget().getSaveGrouping();
        }

        if (saveErrorMessageAlert != null) {
            showErrorMessageAlert(saveErrorMessageAlert);
        }

        handleClickEventsOnUnsavedChangesMsg(saveErrorMessageAlert, auditMessage, saveButton);
    }


    private void showErrorMessageAlert(WarningConfirmationMessageAlert errorMessageDisplay) {
        errorMessageDisplay.createAlert();
        errorMessageDisplay.getWarningConfirmationYesButton().setFocus(true);
    }

    public void notifyCurrentTabOfClosing() {
        MatPresenter oldPresenter = presenterList.get(measureComposerTabLayout.getSelectedIndex());
        if (oldPresenter != null) {
            MatContext.get().setAriaHidden(oldPresenter.getWidget(), "true");
            oldPresenter.beforeClosingDisplay();
        }
        if (sourcePresenter != null) {
            MatContext.get().setAriaHidden(sourcePresenter.getWidget(), "true");
            sourcePresenter.beforeClosingDisplay();
        }
    }

    private void handleClickEventsOnUnsavedChangesMsg(final WarningConfirmationMessageAlert saveErrorMessage, final String auditMessage, Button saveButton) {
        removeHandlers();
        yesHandler = saveErrorMessage.getWarningConfirmationYesButton().addClickHandler(event -> onYesButtonClicked(saveErrorMessage, auditMessage));
        noHandler = saveErrorMessage.getWarningConfirmationNoButton().addClickHandler(event -> onNoButtonClicked(saveErrorMessage, saveButton));
    }

    private void onNoButtonClicked(final WarningConfirmationMessageAlert saveErrorMessage, Button saveButton) {
        saveErrorMessage.clearAlert();
        if (saveButton != null) {
            saveButton.setFocus(true);
        }
        resetTabTargets();
    }

    private void onYesButtonClicked(final WarningConfirmationMessageAlert saveErrorMessage, final String auditMessage) {
        if (auditMessage != null) {
            MatContext.get().recordTransactionEvent(MatContext.get().getCurrentMeasureId(),
                    null, auditMessage, auditMessage, ConstantMessages.DB_LOG);
        }
        saveErrorMessage.clearAlert();
        notifyCurrentTabOfClosing();

        if (targetPresenter == null && targetTabLayout == null) {
            targetTabLayout = measureComposerTabLayout;
            targetPresenter = presenterList.get(measureComposerTabLayout.getTargetSelection());
        }

        targetTabLayout.setIndexFromTargetSelection();
        MatContext.get().setAriaHidden(targetPresenter.getWidget(), "false");
        targetPresenter.beforeDisplay();
        resetTabTargets();
    }

    private void removeHandlers() {
        if (yesHandler != null) {
            yesHandler.removeHandler();
        }
        if (noHandler != null) {
            noHandler.removeHandler();
        }
    }

    public void setTabTargets(MatTabLayoutPanel targetTabLayout, MatPresenter sourcePresenter, MatPresenter targetPresenter) {
        this.targetPresenter = targetPresenter;
        this.targetTabLayout = targetTabLayout;
        this.sourcePresenter = sourcePresenter;
    }

    private void resetTabTargets() {
        targetPresenter = null;
        targetTabLayout = null;
        sourcePresenter = null;
    }

    @Override
    public void updateMeasureHeading() {
        String heading = buildMeasureHeading(MatContext.get().getCurrentMeasureId());
        measureComposerContent.setHeading(heading, MEASURE_COMPOSER);
    }

    public CQLMeasureWorkSpacePresenter getCQLWorkspacePresenter() {
        return this.cqlWorkspacePresenter;
    }
}
