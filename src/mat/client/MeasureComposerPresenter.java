package mat.client;

import java.util.LinkedList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.clause.QDMAppliedSelectionPresenter;
import mat.client.clause.QDMAppliedSelectionView;
import mat.client.clause.clauseworkspace.presenter.ClauseWorkSpacePresenter;
import mat.client.cqlworkspace.CQLWorkSpacePresenter;
import mat.client.cqlworkspace.CQLWorkSpaceView;
import mat.client.event.MATClickHandler;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.metadata.MetaDataPresenter;
import mat.client.measure.metadata.MetaDataView;
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
import mat.shared.ConstantMessages;

/**
 * The Class MeasureComposerPresenter.
 */
public class MeasureComposerPresenter implements MatPresenter, Enableable, TabObserver {
	/**
	 * The Class EnterKeyDownHandler.
	 */
	class EnterKeyDownHandler implements KeyDownHandler {
		
		/** The i. */
		private int i = 0;
		
		/**
		 * Instantiates a new enter key down handler.
		 *
		 * @param index the index
		 */
		public EnterKeyDownHandler(int index) {
			i = index;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.event.dom.client.KeyDownHandler#onKeyDown(com.google.gwt.event.dom.client.KeyDownEvent)
		 */
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				measureComposerTabLayout.selectTab(i);
			}
		}
	}
	
	MatTabLayoutPanel targetTabLayout;
	MatPresenter targetPresenter;
	MatPresenter sourcePresenter;
	HandlerRegistration yesHandler;
	HandlerRegistration noHandler;
	
	/** The sub skip content holder. */
	private static FocusableWidget subSkipContentHolder;
	
	/**
	 * Sets the sub skip embedded link.
	 *
	 * @param name the new sub skip embedded link
	 */
	public static void setSubSkipEmbeddedLink(String name) {
		if(subSkipContentHolder == null) {
			subSkipContentHolder = new FocusableWidget(SkipListBuilder.buildSkipList("Skip to Sub Content"));
		}
		Mat.removeInputBoxFromFocusPanel(subSkipContentHolder.getElement());
		Widget w = SkipListBuilder.buildSubSkipList(name);
		subSkipContentHolder.clear();
		subSkipContentHolder.add(w);
		subSkipContentHolder.setFocus(true);
	}
	
	private List<MatPresenter> presenterList;
	
	/** The button bar. */
	private PreviousContinueButtonBar buttonBar = new PreviousContinueButtonBar();
	
	/**
	 * The Clause Workspace presenter.
	 */
	private ClauseWorkSpacePresenter clauseWorkSpacePresenter = new ClauseWorkSpacePresenter();

	/** The empty widget. */
	private SimplePanel emptyWidget = new SimplePanel();
	
	/** The measure composer content. */
	private ContentWithHeadingWidget measureComposerContent = new ContentWithHeadingWidget();
	
	/** The measure composer tab. */
	private String measureComposerTab;
	
	/** The measure composer tab layout. */
	private MatTabLayoutPanel measureComposerTabLayout;
		
	/** The measure package presenter. */
	@SuppressWarnings("unused")
	private MeasurePackagePresenter measurePackagePresenter;
	
	/** The meta data presenter. */
	private MetaDataPresenter metaDataPresenter;
	
	
	/**
	 * Instantiates a new measure composer presenter.
	 */
	@SuppressWarnings("unchecked")
	public MeasureComposerPresenter() {
		presenterList = new LinkedList<MatPresenter>();
		buttonBar.getElement().setId("buttonBar_PreviousContinueButtonBar");
		emptyWidget.getElement().setId("emptyWidget_SimplePanel");
		
		metaDataPresenter = (MetaDataPresenter) buildMeasureMetaDataPresenter();
		measurePackagePresenter = (MeasurePackagePresenter) buildMeasurePackageWidget();
		
		measureComposerTabLayout = new MatTabLayoutPanel(this);
		measureComposerTabLayout.getElement().setAttribute("id", "measureComposerTabLayout");
		measureComposerTabLayout.add(metaDataPresenter.getWidget(), "Measure Details", true);
		presenterList.add(metaDataPresenter);
		MatPresenter cqlWorkspacePresenter = buildCQLWorkSpaceTab();
		measureComposerTabLayout.add(cqlWorkspacePresenter.getWidget(), "CQL Workspace", true);
		presenterList.add(cqlWorkspacePresenter);
		MatPresenter cqlPopulationWorkspacePresenter = buildCQLPopulationWorkspaceTab();
		measureComposerTabLayout.add(cqlPopulationWorkspacePresenter.getWidget(), "Population Workspace", true);
		presenterList.add(cqlPopulationWorkspacePresenter);
		MatPresenter measurePackagePresenter = buildMeasurePackageWidget();
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
			} });
		
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
					//US 385. Display the first tab which is value set search.
					metaDataPresenter.displayDetail();
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
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
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
					DeferredCommand.addCommand(this);
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
	
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		String currentMeasureId = MatContext.get().getCurrentMeasureId();
		if ((currentMeasureId != null) && !"".equals(currentMeasureId)) {
			if (MatContext.get().isCurrentMeasureEditable()) {
				MatContext.get().getMeasureLockService().setMeasureLock();
			}
			String heading = MatContext.get().getCurrentMeasureName() + " ";
			String version = MatContext.get().getCurrentMeasureVersion();
			//when a measure is initaly created we need to explictly create the heading
			if (!version.startsWith("Draft") && !version.startsWith("v")) {
				version = "Draft based on v" + version;
				//version = MeasureUtility.getVersionText(version, true);
			}
			
			heading = heading + version;
			measureComposerContent.setHeading(heading, "MeasureComposer");
			FlowPanel fp = new FlowPanel();
			fp.getElement().setId("fp_FlowPanel");
			setSubSkipEmbeddedLink("MetaDataView.containerPanel");
			fp.add(subSkipContentHolder);
			fp.add(measureComposerTabLayout);
			measureComposerContent.setContent(fp);
			MatContext.get().setVisible(buttonBar, true);
			measureComposerTabLayout.selectTab(presenterList.indexOf(metaDataPresenter));
			metaDataPresenter.beforeDisplay();
		} else {
			measureComposerContent.setHeading("No Measure Selected", "MeasureComposer");
			measureComposerContent.setContent(emptyWidget);
			MatContext.get().setVisible(buttonBar, false);
		}
		Mat.focusSkipLists("MainContent");
		buttonBar.state = measureComposerTabLayout.getSelectedIndex();
		buttonBar.setPageNamesOnState();
	}
	
	/**
	 * Builds the measure meta data presenter.
	 *
	 * @return the mat presenter
	 */
	private MatPresenter buildMeasureMetaDataPresenter() {
		MetaDataView mdV = new MetaDataView();
		//MAT-4898
		//AddEditAuthorsView aeaV = new AddEditAuthorsView();
		//AddEditMeasureTypeView aemtV = new AddEditMeasureTypeView();
		//		AddEditComponentMeasuresView aecmV = new AddEditComponentMeasuresView();
		MetaDataPresenter mdP = new MetaDataPresenter(mdV,buttonBar, MatContext.get().getListBoxCodeProvider());
		return mdP;
	}
	
	/**
	 * Builds the  measure package widget.
	 *
	 * @return the mat presenter
	 */
	private MatPresenter buildMeasurePackageWidget() {
		MeasurePackagerView measurePackagerView = new MeasurePackagerView();
		MeasurePackagePresenter measurePackagePresenter = new MeasurePackagePresenter(measurePackagerView);
		measurePackagePresenter.getWidget();
		return measurePackagePresenter;
	}
	

	
	/**
	 * Builds the qdm presenter.
	 *
	 * @return the qDM presenter
	 */
	private MatPresenter buildAppliedQDMPresenter(){
		QDMAppliedSelectionView vascProfileSelectionView = new QDMAppliedSelectionView();
		QDMAppliedSelectionPresenter vsacProfileSelectionPresenter =
				new QDMAppliedSelectionPresenter(vascProfileSelectionView);
		vsacProfileSelectionPresenter.getWidget();
		return vsacProfileSelectionPresenter;
	}
	
	
	/**
	 * Builds the cql work space tab.
	 *
	 * @return the mat presenter
	 */
	private MatPresenter buildCQLWorkSpaceTab(){
		CQLWorkSpaceView cqlView = new CQLWorkSpaceView();
		CQLWorkSpacePresenter cqlPresenter =
				new CQLWorkSpacePresenter(cqlView);
		cqlPresenter.getWidget();
		return cqlPresenter;
	}
	
	/**
	* Builds the cql work space tab.
	*
	* @return the mat presenter
	*/
	private MatPresenter buildCQLPopulationWorkspaceTab() {		
		CQLPopulationWorkSpaceView cqlPopulationWorkspaceView = new CQLPopulationWorkSpaceView();
		CQLPopulationWorkSpacePresenter cqlPopulationPresenter =
				new CQLPopulationWorkSpacePresenter(cqlPopulationWorkspaceView);
		cqlPopulationPresenter.getWidget();
		return cqlPopulationPresenter;
	}
	
	/**
	 * Gets the meta data presenter.
	 *
	 * @return the metaDataPresenter
	 */
	public MetaDataPresenter getMetaDataPresenter() {
		return metaDataPresenter;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return measureComposerContent;
	}
	
	/**
	 * implementing Enableable interface
	 * set enablement for navigation links and measure composer tabs
	 * consider setting enablement for each presenter and for skip links.
	 *
	 * @param enabled the new enabled
	 */
	@Override
	public void setEnabled(boolean enabled) {
		buttonBar.setEnabled(enabled);
		measureComposerTabLayout.setEnabled(enabled);
	}

	
	/**
	 * Sets the meta data presenter.
	 *
	 * @param metaDataPresenter the metaDataPresenter to set
	 */
	public void setMetaDataPresenter(MetaDataPresenter metaDataPresenter) {
		this.metaDataPresenter = metaDataPresenter;
	}

	@Override
	public boolean isValid() {
		MatContext.get().setErrorTabIndex(-1);
		MatContext.get().setErrorTab(false);
		int selectedIndex = measureComposerTabLayout.getSelectedIndex();
		if(presenterList.get(selectedIndex) instanceof MetaDataPresenter) {
			MetaDataPresenter presenter = (MetaDataPresenter) presenterList.get(selectedIndex);
			return presenter.isMeasureDetailsValid();
		} else if(presenterList.get(selectedIndex) instanceof CQLWorkSpacePresenter){
			CQLWorkSpacePresenter presenter = (CQLWorkSpacePresenter) presenterList.get(selectedIndex);
			return presenter.isCQLWorkspaceValid();
		} else if(presenterList.get(selectedIndex) instanceof CQLPopulationWorkSpacePresenter) {
			CQLPopulationWorkSpacePresenter presenter = (CQLPopulationWorkSpacePresenter) presenterList.get(selectedIndex);
			return presenter.isPopulationWorkSpaceValid();
		} else if(presenterList.get(selectedIndex) instanceof MeasurePackagePresenter) {
			MeasurePackagePresenter presenter = (MeasurePackagePresenter) presenterList.get(selectedIndex);
			return presenter.isMeasurePackageValid();
		}
		
		return true;
	}

	@Override
	public void updateOnBeforeSelection() {
		MatPresenter presenter = presenterList.get(measureComposerTabLayout.getSelectedIndex());
		if (presenter != null) {
			MatContext.get().setAriaHidden(presenter.getWidget(),  "false");
			presenter.beforeDisplay();
		}
	}
	
	@Override
	public void showUnsavedChangesError() {
		int selectedIndex = measureComposerTabLayout.getSelectedIndex();
		WarningConfirmationMessageAlert saveErrorMessageAlert = null;
		String auditMessage = null;
		Button saveButton = null;
		if(presenterList.get(selectedIndex) instanceof MetaDataPresenter) {
			saveErrorMessageAlert = metaDataPresenter.getMetaDataDisplay().getSaveErrorMsg();
			metaDataPresenter.getMetaDataDisplay().getBottomErrorMessage().clearAlert();
			metaDataPresenter.getMetaDataDisplay().getTopErrorMessage().clearAlert();
			metaDataPresenter.getMetaDataDisplay().getBottomSuccessMessage().clearAlert();
			metaDataPresenter.getMetaDataDisplay().getTopSuccessMessage().clearAlert();
			saveButton = metaDataPresenter.getMetaDataDisplay().getBottomSaveButton();
		} else if(presenterList.get(selectedIndex) instanceof CQLWorkSpacePresenter){
			CQLWorkSpacePresenter cqlWorkSpacePresenter = (CQLWorkSpacePresenter) presenterList.get(selectedIndex);
			CQLWorkSpacePresenter.getSearchDisplay().resetMessageDisplay();
			saveErrorMessageAlert = cqlWorkSpacePresenter.getMessagePanel().getGlobalWarningConfirmationMessageAlert();
			auditMessage = CQLWorkSpacePresenter.getSearchDisplay().getClickedMenu().toUpperCase() + "_TAB_YES_CLICKED";
		} else if(presenterList.get(selectedIndex) instanceof CQLPopulationWorkSpacePresenter) {
			CQLPopulationWorkSpacePresenter presenter = (CQLPopulationWorkSpacePresenter) presenterList.get(selectedIndex);
			saveErrorMessageAlert = presenter.getSearchDisplay().getCqlLeftNavBarPanelView().getGlobalWarningConfirmationMessageAlert();
			auditMessage = presenter.getSearchDisplay().getClickedMenu().toUpperCase() + "_TAB_YES_CLICKED";
		} else if(presenterList.get(selectedIndex) instanceof MeasurePackagePresenter) {
			MeasurePackagePresenter presenter = (MeasurePackagePresenter) presenterList.get(selectedIndex);
			presenter.getView().getSaveErrorMessageDisplayOnEdit().clearAlert();
			saveErrorMessageAlert = presenter.getView().getSaveErrorMessageDisplay();
			saveButton = presenter.getView().getPackageGroupingWidget().getSaveGrouping();
		}
		
		if(saveErrorMessageAlert != null) {
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
		if(sourcePresenter != null) {
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
		if(saveButton != null) {
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
		
		if(targetPresenter == null && targetTabLayout == null) {
			targetTabLayout = measureComposerTabLayout;
			targetPresenter = presenterList.get(measureComposerTabLayout.getTargetSelection());
		}
		
		targetTabLayout.setIndexFromTargetSelection();
		MatContext.get().setAriaHidden(targetPresenter.getWidget(),  "false");
		targetPresenter.beforeDisplay();
		resetTabTargets();
	}
	
	private void removeHandlers() {
		if(yesHandler!=null) {
			yesHandler.removeHandler();
		}
		if(noHandler!=null) {	
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
}
