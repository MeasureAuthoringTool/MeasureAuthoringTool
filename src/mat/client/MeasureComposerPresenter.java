package mat.client;

import mat.client.clause.QDMPresenter;
import mat.client.clause.clauseworkspace.presenter.ClauseWorkspacePresenter;
import mat.client.event.MATClickHandler;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.MeasureNotesPresenter;
import mat.client.measure.MeasureNotesView;
import mat.client.measure.metadata.AddEditAuthorsView;
import mat.client.measure.metadata.AddEditMeasureTypeView;
import mat.client.measure.metadata.MetaDataPresenter;
import mat.client.measure.metadata.MetaDataView;
import mat.client.measure.metadata.events.ContinueToMeasurePackageEvent;
import mat.client.measurepackage.MeasurePackagePresenter;
import mat.client.measurepackage.MeasurePackageView;
import mat.client.shared.ContentWithHeadingWidget;
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

public class MeasureComposerPresenter implements MatPresenter, Enableable {
	//private MatClausePresenter clauseWorkspace = new MatClausePresenter();
	private QDMPresenter qdmPresenter;
	private Widget measurePackageWidget;
	private SimplePanel emptyWidget = new SimplePanel();
	private MetaDataPresenter metaDataPresenter ;
	private MeasurePackagePresenter measurePackagePresenter ;
	private MatTabLayoutPanel measureComposerTabLayout;
	private ContentWithHeadingWidget measureComposerContent = new ContentWithHeadingWidget();
	private PreviousContinueButtonBar buttonBar = new PreviousContinueButtonBar();
    private static SimplePanel subSkipContentHolder = new SimplePanel();  
    private String measureComposerTab;
    private ClauseWorkspacePresenter clauseWorkspacePresenter = new ClauseWorkspacePresenter();
    private MeasureNotesPresenter measureNotesPresenter = new MeasureNotesPresenter(new MeasureNotesView());
    
	public MeasureComposerPresenter() {
		
		metaDataPresenter = (MetaDataPresenter) buildMeasureMetaDataPresenter();
		measurePackagePresenter = (MeasurePackagePresenter) buildMeasurePackageWidget();
		qdmPresenter = (QDMPresenter) buildQDMPresenter();
		measureComposerTabLayout = new MatTabLayoutPanel(true);
		measureComposerTabLayout.setId("measureComposerTabLayout");
		measureComposerTabLayout.addPresenter(metaDataPresenter,"Measure Details");	
		measureComposerTabLayout.addPresenter(qdmPresenter,"QDM Elements");
		//measureComposerTabLayout.addPresenter(clauseWorkspace,"Old CW");//name changed 
		measureComposerTabLayout.addPresenter(clauseWorkspacePresenter, "Clause Workspace");
		measureComposerTabLayout.addPresenter(buildMeasurePackageWidget(), "Measure Packager");
		measureComposerTabLayout.addPresenter(measureNotesPresenter, "Measure Notes");
		
		measureComposerTabLayout.setHeight("98%");
		//measureComposerTabLayout.selectTab(metaDataPresenter.getWidget());
		
		measureComposerTab = ConstantMessages.MEASURE_COMPOSER_TAB;
		MatContext.get().tabRegistry.put(measureComposerTab,measureComposerTabLayout);
		MatContext.get().enableRegistry.put(measureComposerTab,this);
		measureComposerTabLayout.addSelectionHandler(new SelectionHandler<Integer>(){
			public void onSelection(final SelectionEvent event) {
				int index = ((SelectionEvent<Integer>) event).getSelectedItem();
				// suppressing token dup
				String newToken = measureComposerTab + index;
				if(!History.getToken().equals(newToken)){
					MeasureSelectedEvent mse = MatContext.get().getCurrentMeasureInfo();
					String msg = " [measure] "+mse.getMeasureName()+" [version] "+mse.getMeasureVersion();
					String mid = mse.getMeasureId();
					MatContext.get().recordTransactionEvent(mid, null, "MEASURE_TAB_EVENT", newToken+msg, ConstantMessages.DB_LOG);
					History.newItem(newToken, false);
				}
			}});
		
		measureComposerContent.setContent(emptyWidget);
		measureComposerContent.setFooter(buttonBar);
		
		buttonBar.getPreviousButton().addClickHandler(new MATClickHandler() {
			
			@Override
			public void onEvent(GwtEvent arg0) {
				int selectedIndex = measureComposerTabLayout.getSelectedIndex();
				if(selectedIndex != 0){
					measureComposerTabLayout.selectPreviousTab();
				}else{
					//US 385. Display the first tab which is value set search.
					metaDataPresenter.displayDetail();
					buttonBar.subState = selectedIndex;
					beforeDisplay();
				}
			}

			@Override
			protected boolean doAlert() {
				return true;
			}
			
		});
		
		buttonBar.getContinueButton().addClickHandler(new MATClickHandler() {

			@Override
			protected void onEvent(GwtEvent event) {
				measureComposerTabLayout.selectNextTab();
			}

			@Override
			protected boolean doAlert() {
				return true;
			}
			
		});
		
		measureComposerTabLayout.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {				
				int index = ((SelectionEvent<Integer>)event).getSelectedItem(); 
				buttonBar.setPreviousButtonVisible(measureComposerTabLayout.hasPreviousTab());
				buttonBar.setContinueButtonVisible(measureComposerTabLayout.hasNextTab());
				buttonBar.state = measureComposerTabLayout.getSelectedIndex();
				buttonBar.setPageNamesOnState();
			}
		});
		
		MatContext.get().getEventBus().addHandler(ContinueToMeasurePackageEvent.TYPE, new ContinueToMeasurePackageEvent.Handler() {
			@Override
			public void onContinueToMeasurePackage(ContinueToMeasurePackageEvent event) {
				measureComposerTabLayout.selectTab(measurePackagePresenter);
				buttonBar.state = measureComposerTabLayout.getSelectedIndex();
				buttonBar.setPageNamesOnState();
			}
		});
	}
	
	@Override
	public void beforeDisplay() {		
		String currentMeasureId = MatContext.get().getCurrentMeasureId();		
		if(currentMeasureId != null && !"".equals(currentMeasureId)) {
			if(MatContext.get().isCurrentMeasureEditable()){
			    MatContext.get().getMeasureLockService().setMeasureLock();
			}
			String heading = MatContext.get().getCurrentMeasureName()+" ";
			String version = MatContext.get().getCurrentMeasureVersion();
			//when a measure is initaly created we need to explictly create the heading
			if(!version.startsWith("Draft") && !version.startsWith("v")){
				version = "Draft based on v"+version;
				//version = MeasureUtility.getVersionText(version, true);
			}
			
			heading = heading+version;
			measureComposerContent.setHeading(heading,"MeasureComposer");
			FlowPanel fp = new FlowPanel();
			subSkipContentHolder.clear();
			subSkipContentHolder.add(SkipListBuilder.buildSubSkipList("CodeList"));
			fp.add(subSkipContentHolder);
			fp.add(measureComposerTabLayout);
			measureComposerContent.setContent(fp);
			MatContext.get().setVisible(buttonBar,true);
			MatContext.get().getZoomFactorService().resetFactorArr();
			measureComposerTabLayout.selectTab(metaDataPresenter);//This for some reason not calling the metaDataPresenter.beforeDisplay. So explicitly calling that
			metaDataPresenter.beforeDisplay();
		}
		else {
			measureComposerContent.setHeading("No Measure Selected","MeasureComposer");
			measureComposerContent.setContent(emptyWidget);
			MatContext.get().setVisible(buttonBar,false);
		}
		Mat.focusSkipLists("MeasureComposer");
		buttonBar.state = measureComposerTabLayout.getSelectedIndex();
		buttonBar.setPageNamesOnState();
	}

	@Override
	public void beforeClosingDisplay() {
		if(MatContext.get().isMeasureDeleted()){
			MatContext.get().getCurrentMeasureInfo().setMeasureId("");
			MatContext.get().setMeasureDeleted(false);
		}
		MatContext.get().getMeasureLockService().releaseMeasureLock();
		Command waitForUnlock = new Command(){
			public void execute() {
			  if(!MatContext.get().getMeasureLockService().isResettingLock()){
	 			  measureComposerTabLayout.close();
	 			  measureComposerTabLayout.updateHeaderSelection(0);
	 			  measureComposerTabLayout.setSelectedIndex(0);
	 			  buttonBar.state = measureComposerTabLayout.getSelectedIndex();
	 			  buttonBar.setPageNamesOnState();
	 		  }else{
	 			  DeferredCommand.addCommand(this);
	 		  }
	 	   }
	 	};
	 	if(MatContext.get().getMeasureLockService().isResettingLock()){
	 		waitForUnlock.execute();
	 		 //This is done to reset measure composure tab to show "No Measure Selected" as when measure is deleted,it should not show Any sub tabs under MeasureComposure.
		  	if(MatContext.get().getCurrentMeasureInfo()!=null){
		  		MatContext.get().getCurrentMeasureInfo().setMeasureId("");
		  	}
	 	}
	 	else{
	 	    measureComposerTabLayout.close();
	 	    measureComposerTabLayout.updateHeaderSelection(0);
	 	    measureComposerTabLayout.setSelectedIndex(0);
	 	    buttonBar.state = measureComposerTabLayout.getSelectedIndex();
		  	buttonBar.setPageNamesOnState();
		  //This is done to reset measure composure tab to show "No Measure Selected" as when measure is deleted,it should not show Any sub tabs under MeasureComposure.
		  	if(MatContext.get().getCurrentMeasureInfo()!=null){
		  		MatContext.get().getCurrentMeasureInfo().setMeasureId("");
		  	}
	 	}
		
	}

	@Override
	public Widget getWidget() {
		return measureComposerContent;
	}

	
	private MatPresenter buildMeasurePackageWidget() {
		MeasurePackageView mpv = new MeasurePackageView();
		MeasurePackagePresenter mpp = new MeasurePackagePresenter(mpv);
		measurePackageWidget = mpp.getWidget();
		return mpp;
	}
	private MatPresenter buildMeasureMetaDataPresenter(){
		MetaDataView mdV = new MetaDataView();
		AddEditAuthorsView aeaV = new AddEditAuthorsView();		
		AddEditMeasureTypeView aemtV = new AddEditMeasureTypeView();
		MetaDataPresenter mdP = new MetaDataPresenter(mdV,aeaV,aemtV,buttonBar,MatContext.get().getListBoxCodeProvider());
		return mdP;
	}
	
	private QDMPresenter buildQDMPresenter(){
		QDMPresenter qdmP = new QDMPresenter();
		return qdmP;
		
	}
	
	
	/*public MatClausePresenter getClauseWorkspace() {
		return clauseWorkspace;
	}
	public void setClauseWorkspace(MatClausePresenter clauseWorkspace) {
		this.clauseWorkspace = clauseWorkspace;
	}*/
	
	class EnterKeyDownHandler implements KeyDownHandler {
		private int i = 0;
		public EnterKeyDownHandler(int index){
			i = index;
		}
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				measureComposerTabLayout.selectTab(i);
			}
		}
	}
	
	public static void setSubSkipEmbeddedLink(String name){
		Widget w = SkipListBuilder.buildSubSkipList(name);
		subSkipContentHolder.clear();
		subSkipContentHolder.add(w);
	}

	/**
	 * implementing Enableable interface
	 * set enablement for navigation links and measure composer tabs
	 * consider setting enablement for each presenter and for skip links
	 */
	@Override
	public void setEnabled(boolean enabled) {
		buttonBar.setEnabled(enabled);
		measureComposerTabLayout.setEnabled(enabled);
	}

	/**
	 * @return the metaDataPresenter
	 */
	public MetaDataPresenter getMetaDataPresenter() {
		return metaDataPresenter;
	}

	/**
	 * @param metaDataPresenter the metaDataPresenter to set
	 */
	public void setMetaDataPresenter(MetaDataPresenter metaDataPresenter) {
		this.metaDataPresenter = metaDataPresenter;
	}

	/**
	 * @return the measureComposerTabLayout
	 */
	public MatTabLayoutPanel getMeasureComposerTabLayout() {
		return measureComposerTabLayout;
	}

	/**
	 * @param measureComposerTabLayout the measureComposerTabLayout to set
	 */
	public void setMeasureComposerTabLayout(
			MatTabLayoutPanel measureComposerTabLayout) {
		this.measureComposerTabLayout = measureComposerTabLayout;
	}
	
}
