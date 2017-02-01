package mat.client;

import mat.client.clause.cqlworkspace.CQLWorkSpacePresenter;
import mat.client.clause.cqlworkspace.CQLWorkSpaceView;
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
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * The Class CqlComposerPresenter.
 */
@SuppressWarnings("deprecation")
public class CqlComposerPresenter implements MatPresenter, Enableable {
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
				cqlComposerTabLayout.selectTab(i);
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
	
	/** The empty widget. */
	private SimplePanel emptyWidget = new SimplePanel();
	
	/** The measure composer content. */
	private ContentWithHeadingWidget cqlComposerContent = new ContentWithHeadingWidget();
	
	/** The cql composer tab. */
	private String cqlComposerTab;
	
	/** The cql composer tab layout. */
	private MatTabLayoutPanel cqlComposerTabLayout;
	
	/**
	 * Instantiates a new measure composer presenter.
	 */
	@SuppressWarnings("unchecked")
	public CqlComposerPresenter() {
		emptyWidget.getElement().setId("emptyWidget_SimplePanel");
		
		cqlComposerTabLayout = new MatTabLayoutPanel(true);
		cqlComposerTabLayout.setId("cqlComposerTabLayout");
		cqlComposerTabLayout.addPresenter(buildCQLWorkSpaceTab(), "CQL Workspace");
		cqlComposerTabLayout.setHeight("98%");
		cqlComposerTab = ConstantMessages.CQL_COMPOSER_TAB;
		MatContext.get().tabRegistry.put(cqlComposerTab, cqlComposerTabLayout);
		MatContext.get().enableRegistry.put(cqlComposerTab, this);
		cqlComposerTabLayout.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			@SuppressWarnings("rawtypes")
			public void onSelection(final SelectionEvent event) {
				int index = ((SelectionEvent<Integer>) event).getSelectedItem();
				String newToken = cqlComposerTab + index;
				if (!History.getToken().equals(newToken)) {
					//TODO: create CqlLibrarySelectedEvent and get its info
					//CqlSelectedEvent cse = MatContext.get().getCurrentCqlInfo();
					//String msg = " [measure] " + cse.getLibraryName() + " [version] " + cse.getLibraryVersion();
					//String mid = cse.getLibraryId();
					//MatContext.get().recordTransactionEvent(mid, null, "CQL_LIBRARY_TAB_EVENT", newToken + msg, ConstantMessages.DB_LOG);
					
					History.newItem(newToken, false);
				}
			} });
		
		cqlComposerContent.setContent(emptyWidget);
		
		
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		//TODO: Write a code in MatContext to get currentLibraryID
		// currentLibraryId = MatContext.get().getCurrentLibraryId();
		
		//if ((currentLibraryId != null) && !"".equals(currentLibraryId)) {
			/*if (MatContext.get().isCurrentLibraryEditable()) {
				MatContext.get().getLibraryLockService().setLibraryLock();
			}*/
			//TODO: Get Library name and version from MatContext.
/*			String heading = MatContext.get().getCurrentLibraryName() + " ";
			String version = MatContext.get().getCurrentLibraryVersion();*/
			//when a library is initaly created we need to explictly create the heading
			/*if (!version.startsWith("Draft") && !version.startsWith("v")) {
				version = "Draft based on v" + version;
				version = LibraryUtility.getVersionText(version, true);
			}
			
			heading = heading + version;
			cqlComposerContent.setHeading(heading, "CqlComposer");*/
			FlowPanel fp = new FlowPanel();
			fp.getElement().setId("fp_FlowPanel");
			setSubSkipEmbeddedLink("MetaDataView.containerPanel");
			fp.add(subSkipContentHolder);
			fp.add(cqlComposerTabLayout);
			cqlComposerContent.setContent(fp);
			MatContext.get().setVisible(buttonBar, true);
			MatContext.get().getZoomFactorService().resetFactorArr();
	//	} else {
			//cqlComposerContent.setHeading("No Library Selected", "CqlComposer");
			//cqlComposerContent.setContent(emptyWidget);
			//MatContext.get().setVisible(buttonBar, false);
		//}
		Mat.focusSkipLists("MainContent");
		buttonBar.state = cqlComposerTabLayout.getSelectedIndex();
		buttonBar.setPageNamesOnState();
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
	public MatTabLayoutPanel getCqlComposerTabLayout() {
		return cqlComposerTabLayout;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return cqlComposerContent;
	}
	
	/**
	 * implementing Enableable interface
	 * set enablement for navigation links and cql composer tabs
	 * consider setting enablement for each presenter and for skip links.
	 *
	 * @param enabled the new enabled
	 */
	@Override
	public void setEnabled(boolean enabled) {
		cqlComposerTabLayout.setEnabled(enabled);
	}
	
	/**
	 * Sets the cql composer tab layout.
	 *
	 * @param cqlComposerTabLayout the cqlComposerTabLayout to set
	 */
	public void setCqlComposerTabLayout(
			MatTabLayoutPanel cqlComposerTabLayout) {
		this.cqlComposerTabLayout = cqlComposerTabLayout;
	}
	
}
