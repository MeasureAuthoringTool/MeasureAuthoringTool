package mat.client.clause;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.MatPresenter;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.VSACProfile;

// TODO: Auto-generated Javadoc
/**
 * The Class VSACProfileSelectionPresenter.
 */
public class VSACProfileSelectionPresenter implements MatPresenter {

	/**
	 * The Interface SearchDisplay.
	 */
	interface SearchDisplay {

		/**
		 * As widget.
		 *
		 * @return the widget
		 */
		Widget asWidget();

		/**
		 * Gets the VSAC profile input.
		 *
		 * @return the VSAC profile input
		 */
		HasValueChangeHandlers<Boolean> getVSACProfileInput();

		/**
		 * Gets the VSAC profile list box.
		 *
		 * @return the VSAC profile list box
		 */
		ListBoxMVP getVSACProfileListBox();


		/**
		 * Sets the vsac profile selection list.
		 *
		 * @param vsacProfileSelectionList the new vsac profile selection list
		 */
		void setVsacProfileSelectionList(List<String> vsacProfileSelectionList);
		
		/**
		 * Sets the vsac profile list box.
		 */
		void setVSACProfileListBox();

		/**
		 * Gets the error message display.
		 *
		 * @return the error message display
		 */
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		void resetVSACValueSetWidget();
	}

	/** The panel. */
	SimplePanel panel = new SimplePanel();

	/** The search display. */
	SearchDisplay searchDisplay;
	
	/** The vsacapi service async. */
	VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get()
			.getVsacapiServiceAsync();

	/**
	 * Instantiates a new VSAC profile selection presenter.
	 *
	 * @param srchDisplay the srch display
	 */
	public VSACProfileSelectionPresenter(SearchDisplay srchDisplay) {
		searchDisplay = srchDisplay;
		addAllHandlers();
	}
	
	/**
	 * Adds the all handlers.
	 */
	private void addAllHandlers(){
		searchDisplay.getVSACProfileInput().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue().toString().equals("true")) {
					if (!MatContext.get().isUMLSLoggedIn()) { //UMLS Login Validation
						searchDisplay.getErrorMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
						return;
					}
					searchDisplay.getVSACProfileListBox().setEnabled(true);
					vsacapiServiceAsync.getAllProfileList(new AsyncCallback<VsacApiResult>() {
						
						@Override
						public void onSuccess(VsacApiResult result) {
							if(result.getVsacProfileResp()!=null){
								searchDisplay.setVsacProfileSelectionList(getProfileList(result.getVsacProfileResp()));
							}
							searchDisplay.setVSACProfileListBox();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
						}
					});
				} else if(event.getValue().toString().equals("false")){
					searchDisplay.getErrorMessageDisplay().clear();
					searchDisplay.getVSACProfileListBox().setEnabled(false);
					searchDisplay.setVSACProfileListBox();
				}
				
			}
		});
		
	}
	
	/**
	 * Gets the profile list.
	 *
	 * @param list the list
	 * @return the profile list
	 */
	private List<String> getProfileList(List<VSACProfile> list){
		List<String> profileList = new ArrayList<String>();
		for(int i=0; i<list.size(); i++){
			profileList.add(list.get(i).getName());
		}
		return profileList;
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		displaySearch();
	}

	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
		return panel;
	}

	/**
	 * Display search.
	 */
	public void displaySearch(){
		searchDisplay.getErrorMessageDisplay().clear();
		searchDisplay.resetVSACValueSetWidget();
	}
	
	

}
