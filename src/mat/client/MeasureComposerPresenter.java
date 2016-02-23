package mat.client;

import mat.client.clause.QDMAppliedSelectionPresenter;
import mat.client.clause.QDMAppliedSelectionView;
import mat.client.clause.clauseworkspace.presenter.ClauseWorkSpacePresenter;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkspacePresenter;
import mat.client.clause.cqlworkspace.CQLWorkSpacePresenter;
import mat.client.clause.cqlworkspace.CQLWorkSpaceView;
import mat.client.event.MATClickHandler;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.MeasureNotesPresenter;
import mat.client.measure.MeasureNotesView;
import mat.client.measure.metadata.MetaDataPresenter;
import mat.client.measure.metadata.MetaDataView;
import mat.client.measure.metadata.events.ContinueToMeasurePackageEvent;
import mat.client.measurepackage.MeasurePackagePresenter;
import mat.client.measurepackage.MeasurePackagerView;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.FocusableWidget;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.PreviousContinueButtonBar;
import mat.client.shared.SkipListBuilder;
import mat.shared.ConstantMessages;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
// TODO: Auto-generated Javadoc
//MAT-4898
//import mat.client.measure.metadata.AddEditAuthorsView;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasureComposerPresenter.
 */
@SuppressWarnings("deprecation")
public class MeasureComposerPresenter implements MatPresenter, Enableable {
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
	
	/** The button bar. */
	private PreviousContinueButtonBar buttonBar = new PreviousContinueButtonBar();
	
	/**
	 * The Clause Workspace presenter.
	 */
	private ClauseWorkSpacePresenter clauseWorkSpacePresenter = new ClauseWorkSpacePresenter();
	/** The Population workspace presenter. */
	private PopulationWorkspacePresenter populationWorkspacePresenter = new PopulationWorkspacePresenter();
	
	/** The empty widget. */
	private SimplePanel emptyWidget = new SimplePanel();
	
	/** The measure composer content. */
	private ContentWithHeadingWidget measureComposerContent = new ContentWithHeadingWidget();
	
	/** The measure composer tab. */
	private String measureComposerTab;
	
	/** The measure composer tab layout. */
	private MatTabLayoutPanel measureComposerTabLayout;
	
	/** The measure notes presenter. */
	private MeasureNotesPresenter measureNotesPresenter = new MeasureNotesPresenter(new MeasureNotesView());
	
	/** The measure package presenter. */
	private MeasurePackagePresenter measurePackagePresenter;
	
	/** The meta data presenter. */
	private MetaDataPresenter metaDataPresenter;
	
	//private MatClausePresenter clauseWorkspace = new MatClausePresenter();
	
	
	/**
	 * Instantiates a new measure composer presenter.
	 */
	@SuppressWarnings("unchecked")
	public MeasureComposerPresenter() {
		buttonBar.getElement().setId("buttonBar_PreviousContinueButtonBar");
		emptyWidget.getElement().setId("emptyWidget_SimplePanel");
		
		metaDataPresenter = (MetaDataPresenter) buildMeasureMetaDataPresenter();
		//		measurePackagePresenter_old = (MeasurePackagePresenter_Old) buildOldMeasurePackageWidget(); // Commented to hide the Old measure Packager Tab menu
		measurePackagePresenter = (MeasurePackagePresenter) buildMeasurePackageWidget();
		
		measureComposerTabLayout = new MatTabLayoutPanel(true);
		measureComposerTabLayout.setId("measureComposerTabLayout");
		measureComposerTabLayout.addPresenter(metaDataPresenter, "Measure Details");
		//measureComposerTabLayout.addPresenter(qdmPresenter, "Old QDM Elements");
		measureComposerTabLayout.addPresenter(buildAppliedQDMPresenter(), "QDM Elements");
		measureComposerTabLayout.addPresenter(clauseWorkSpacePresenter, "Clause Workspace");
		measureComposerTabLayout.addPresenter(buildCQLWorkSpaceTab(), "CQL Workspace");
		measureComposerTabLayout.addPresenter(populationWorkspacePresenter, "Population Workspace");
		//		measureComposerTabLayout.addPresenter(buildOldMeasurePackageWidget(), "Old Measure Packager"); // Commented to hide the Old measure Packager Tab menu
		measureComposerTabLayout.addPresenter(buildMeasurePackageWidget(), "Measure Packager");
		measureComposerTabLayout.addPresenter(measureNotesPresenter, "Measure Notes");
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
				buttonBar.setPreviousButtonVisible(measureComposerTabLayout.hasPreviousTab());
				buttonBar.setContinueButtonVisible(measureComposerTabLayout.hasNextTab());
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
					measureComposerTabLayout.close();
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
			measureComposerTabLayout.close();
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
			MatContext.get().getZoomFactorService().resetFactorArr();
			measureComposerTabLayout.selectTab(metaDataPresenter); //This for some reason not calling the metaDataPresenter.beforeDisplay. So explicitly calling that
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
	 * Builds the Old measure package widget.
	 *
	 * @return the mat presenter
	 */
	
	//commented to hide the Old_measure Packager from Tab menu
	/*private MatPresenter buildOldMeasurePackageWidget() {
		MeasurePackageView mpv = new MeasurePackageView();
		MeasurePackagePresenter_Old mpp = new MeasurePackagePresenter_Old(mpv);
		mpp.getWidget();
		return mpp;
	}*/
	
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
	
	
	/*public MatClausePresenter getClauseWorkspace() {
		return clauseWorkspace;
	}
	public void setClauseWorkspace(MatClausePresenter clauseWorkspace) {
		this.clauseWorkspace = clauseWorkspace;
	}*/
	
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
	 * Gets the measure composer tab layout.
	 *
	 * @return the measureComposerTabLayout
	 */
	public MatTabLayoutPanel getMeasureComposerTabLayout() {
		return measureComposerTabLayout;
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
	 * Sets the measure composer tab layout.
	 *
	 * @param measureComposerTabLayout the measureComposerTabLayout to set
	 */
	public void setMeasureComposerTabLayout(
			MatTabLayoutPanel measureComposerTabLayout) {
		this.measureComposerTabLayout = measureComposerTabLayout;
	}
	
	/**
	 * Sets the meta data presenter.
	 *
	 * @param metaDataPresenter the metaDataPresenter to set
	 */
	public void setMetaDataPresenter(MetaDataPresenter metaDataPresenter) {
		this.metaDataPresenter = metaDataPresenter;
	}
	
}
