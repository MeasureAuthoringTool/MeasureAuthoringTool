package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.shared.MatContext;
import mat.shared.SaveUpdateCQLResult;

public class CQLStandaloneWorkSpacePresenter implements MatPresenter{

	/** The panel. */
	private SimplePanel panel = new SimplePanel();
	
	/** The search display. */
	private ViewDisplay searchDisplay;
	
	/** The empty widget. */
	private SimplePanel emptyWidget = new SimplePanel();
	
	/** The is measure details loaded. */
	private boolean isCQLWorkSpaceLoaded = false;
	
	
	
	/**
	 * The Interface ViewDisplay.
	 */
	public static interface ViewDisplay{


		/**
		 * Top Main panel of CQL Workspace Tab.
		 * 
		 * @return HorizontalPanel
		 */
		VerticalPanel getMainPanel();
		
		/**
		 * Gets the main v panel.
		 *
		 * @return the main v panel
		 */
		VerticalPanel getMainVPanel();
		
		/**
		 * Gets the main h panel.
		 *
		 * @return the main h panel
		 */
		HorizontalPanel getMainHPanel();
		
		/**
		 * Gets the main flow panel.
		 *
		 * @return the main flow panel
		 */
		FlowPanel getMainFlowPanel();

		/**
		 * Generates View for CQLWorkSpace tab.
		 */
		void buildView();

		Widget asWidget();

	}
	
	/**
	 * Instantiates a new CQL presenter
	 *
	 */
	public CQLStandaloneWorkSpacePresenter(final ViewDisplay srchDisplay) {
		searchDisplay = srchDisplay;
		emptyWidget.add(new Label("No CQL Librart Selected"));
		addEventHandlers();
	}
	
	private void addEventHandlers() {
		MatContext.get().getEventBus().addHandler(CQLLibrarySelectedEvent.TYPE, new CQLLibrarySelectedEvent.Handler() {

			@Override
			public void onLibrarySelected(CQLLibrarySelectedEvent event) {
				isCQLWorkSpaceLoaded = false;
				if (event.getCqlLibraryId() != null) {
					isCQLWorkSpaceLoaded = true;
					//getMeasureDetail();
					logRecentActivity();
				} else {
					displayEmpty();
				}
				
				
				//beforeDisplay();
			}

			
			
		});
		
	}

	private void logRecentActivity() {
		MatContext.get().getCQLLibraryService().isLibraryAvailableAndLogRecentActivity(MatContext.get().getCurrentCQLLibraryId(), 
				MatContext.get().getLoggedinUserId(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Void result) {
					//	getCQLData();
						displayCQLView();
						
					}
				});
		
	}
	
	private void displayCQLView(){
		panel.clear();
		searchDisplay.buildView();
		panel.add(searchDisplay.asWidget());
	}
	
	
	
	@Override
	public void beforeClosingDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeDisplay() {
		if ((MatContext.get().getCurrentCQLLibraryId() == null)
				|| MatContext.get().getCurrentCQLLibraryId().equals("")) {
			displayEmpty();
		} else {
			panel.clear();
			panel.add(searchDisplay.asWidget());
			if (!isCQLWorkSpaceLoaded) { // this check is made so that when CQL is clicked from CQL library, its not called twice.
				//getCQLData();
				isCQLWorkSpaceLoaded = true;
			} else {
				isCQLWorkSpaceLoaded = false;
			}
		}
		
		MeasureComposerPresenter.setSubSkipEmbeddedLink("CQLStandaloneWorkSpaceView.containerPanel");
		Mat.focusSkipLists("CqlComposer");
		
	}
	
	private void getCQLData(){
		
		MatContext.get().getCQLLibraryService().getCQLData(MatContext.get().getCurrentCQLLibraryId(),  new AsyncCallback<SaveUpdateCQLResult>() {
			
			@Override
			public void onSuccess(SaveUpdateCQLResult result) {
				if(result.isSuccess()){
					if(result.getCqlModel() != null){
						System.out.println("I got the model");
					}
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * Display empty.
	 */
	private void displayEmpty() {
		panel.clear();
		panel.add(emptyWidget);
	}

	@Override
	public Widget getWidget() {
		panel.setStyleName("contentPanel");
		return panel;
	}

}
