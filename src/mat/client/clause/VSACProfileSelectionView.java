package mat.client.clause;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.VSACProfile;

// TODO: Auto-generated Javadoc
/**
 * The Class VSACProfileSelectionView.
 */
public class VSACProfileSelectionView implements
		VSACProfileSelectionPresenter.SearchDisplay, HasSelectionHandlers<Boolean> {
	
	/** The profile sel. */
	private CustomCheckBox profileSel = new CustomCheckBox("Select a Profile", "Use a default Expansion Profile ?", 1);
	
	/** The vsac profile list box. */
	private ListBoxMVP vsacProfileListBox = new ListBoxMVP();
	
	/** The container panel. */
	private SimplePanel containerPanel = new SimplePanel();
	
	/** The vsac profile selection list. */
	private List<String> vsacProfileSelectionList = new ArrayList<String>();
	
	/** The vsacapi service async. */
	VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get()
			.getVsacapiServiceAsync();
	
	/** The error message panel. */
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	
	private HandlerManager handlerManager = new HandlerManager(this);
	
	
	
	/**
	 * Instantiates a new VSAC profile selection view.
	 */
	public VSACProfileSelectionView(){
		VerticalPanel vp = new VerticalPanel();
		vp.setHeight("50px");
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizonalPanel");
		//hp.setStyleName("marginTop");
		hp.setWidth("420px");
		hp.add(profileSel);
		//profileSel.addValueChangeHandler(profileSelChangeHandler);
		hp.add(vsacProfileListBox);
		vsacProfileListBox.addItem("--Select--");
		hp.add(new SpacerWidget());
		hp.add(new SpacerWidget());
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.getElement().setId("mainPanel_HorizontalPanel");
		mainPanel.setWidth("100%");
		mainPanel.setHeight("200px");
		vp.add(errorMessagePanel);
		errorMessagePanel.getElement().setId("errorMessagePanel_ErrorMessageDisplay");
		vp.add(hp);
		vp.setWidth("100%");
		mainPanel.add(vp);
		//mainPanel.setStyleName("valueSetMarginTop");
		containerPanel.getElement().setId("subContainerPanel");
		containerPanel.setWidth("100%");
		containerPanel.add(mainPanel);
		containerPanel.setStyleName("qdsContentPanel");
		MatContext.get().setVSACProfileView(this);
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getVSACProfileInput()
	 */
	@Override
	public HasValueChangeHandlers<Boolean> getVSACProfileInput() {
		return profileSel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getVSACProfileListBox()
	 */
	@Override
	public ListBoxMVP getVSACProfileListBox() {
		return vsacProfileListBox;
	}
	
	/**
	 * Gets the vsac profile selection list.
	 *
	 * @return the vsacProfileSelectionList
	 */
	public List<String> getVsacProfileSelectionList() {
		return vsacProfileSelectionList;
	}

	/**
	 * Sets the vsac profile selection list.
	 *
	 * @param vsacProfileSelectionList the vsacProfileSelectionList to set
	 */
	@Override
	public void setVsacProfileSelectionList(List<String> vsacProfileSelectionList) {
		this.vsacProfileSelectionList = vsacProfileSelectionList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessagePanel;
	}
	
	/**
	 * Gets the profile list.
	 *
	 * @param list the list
	 * @return the profile list
	 */
//	private List<String> getProfileList(List<VSACProfile> list){
//		List<String> profileList = new ArrayList<String>();
//		for(int i=0; i<list.size(); i++){
//			profileList.add(list.get(i).getName());
//		}
//		return profileList;
//	}
//		
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#setVSACProfileListBox()
	 */
	@Override
	public void setVSACProfileListBox() {
		vsacProfileListBox.clear();
		vsacProfileListBox.addItem("--Select--");
		for(int i =0; i<vsacProfileSelectionList.size()&&vsacProfileSelectionList!=null;i++){
			vsacProfileListBox.addItem(vsacProfileSelectionList.get(i));
		}
		
	}
	
	
	/** The profile sel change handler. */
//	private  ValueChangeHandler<Boolean> profileSelChangeHandler = new ValueChangeHandler<Boolean>() {
//		@Override
//		public void onValueChange(ValueChangeEvent<Boolean> event) {
//			if(event.getValue().toString().equals(true)){
//				vsacProfileListBox.setEnabled(true);
//				vsacapiServiceAsync.getAllProfileList(new AsyncCallback<VsacApiResult>() {
//					@Override
//					public void onSuccess(VsacApiResult result) {
//						setVsacProfileSelectionList(getProfileList(result.getVsacProfileResp()));
//						setVSACProfileListBox();
//					}
//					
//					@Override
//					public void onFailure(Throwable caught) {
//						
//					}
//				});
//			} else if(event.getValue().toString().equals(true)){
//				vsacProfileListBox.setEnabled(false);
//			}
//		}
//	};


	@Override
	public void resetVSACValueSetWidget() {
		profileSel.setValue(false);
		vsacProfileListBox.clear();
		vsacProfileListBox.setEnabled(false);
		vsacProfileListBox.addItem("--Select--");
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
      handlerManager.fireEvent(event);
	}

	@Override
	public HandlerRegistration addSelectionHandler(
			SelectionHandler<Boolean> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}

}
