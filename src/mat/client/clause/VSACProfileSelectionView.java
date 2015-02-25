package mat.client.clause;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.VSACProfile;

public class VSACProfileSelectionView implements
		VSACProfileSelectionPresenter.SearchDisplay {
	
	private CustomCheckBox profileSel = new CustomCheckBox("Select a Profile", "Use a default Expansion Profile", 1);
	
	private ListBoxMVP vsacProfileListBox = new ListBoxMVP();
	
	private SimplePanel containerPanel = new SimplePanel();
	
	private List<String> vsacProfileSelectionList = new ArrayList<String>();
	
	VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get()
			.getVsacapiServiceAsync();
	
	
	public VSACProfileSelectionView(){
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("vp_VerticalPanel");
		hp.setWidth("400px");
		hp.add(profileSel);
		profileSel.addValueChangeHandler(profileSelChangeHandler);
		hp.add(vsacProfileListBox);
		vsacProfileListBox.addItem("--Select--");
		hp.add(new SpacerWidget());
		hp.add(new SpacerWidget());
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.getElement().setId("mainPanel_HorizontalPanel");
		mainPanel.setWidth("100%");
		mainPanel.add(hp);
		containerPanel.getElement().setId("subContainerPanel");
		containerPanel.setWidth("100%");
		containerPanel.add(mainPanel);
		containerPanel.setStyleName("qdsContentPanel");
		MatContext.get().setVSACProfileView(this);
	}

	@Override
	public Widget asWidget() {
		return containerPanel;
	}

	@Override
	public CustomCheckBox getVSACProfileInput() {
		return profileSel;
	}

	@Override
	public ListBoxMVP getVSACProfileListBox() {
		return vsacProfileListBox;
	}
	
	/**
	 * @return the vsacProfileSelectionList
	 */
	public List<String> getVsacProfileSelectionList() {
		return vsacProfileSelectionList;
	}

	/**
	 * @param vsacProfileSelectionList the vsacProfileSelectionList to set
	 */
	@Override
	public void setVsacProfileSelectionList(List<String> vsacProfileSelectionList) {
		this.vsacProfileSelectionList = vsacProfileSelectionList;
	}
	
	private List<String> getProfileList(List<VSACProfile> list){
		List<String> profileList = new ArrayList<String>();
		for(int i=0; i<list.size(); i++){
			profileList.add(list.get(i).getName());
		}
		return profileList;
	}
	
	@Override
	public void setVSACProfileListBox() {
		vsacProfileListBox.clear();
		vsacProfileListBox.addItem("--Select--");
		for(int i =0; i<vsacProfileSelectionList.size()&&vsacProfileSelectionList!=null;i++){
			vsacProfileListBox.addItem(vsacProfileSelectionList.get(i));
		}
		
	}

	private  ValueChangeHandler<Boolean> profileSelChangeHandler = new ValueChangeHandler<Boolean>() {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			if(event.getValue().toString().equals(true)){
				vsacProfileListBox.setEnabled(true);
				vsacapiServiceAsync.getAllProfileList(new AsyncCallback<VsacApiResult>() {
					@Override
					public void onSuccess(VsacApiResult result) {
						setVsacProfileSelectionList(getProfileList(result.getVsacProfileResp()));
						setVSACProfileListBox();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
			} else if(event.getValue().toString().equals(true)){
				vsacProfileListBox.setEnabled(false);
			}
		}
	};

}
