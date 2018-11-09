package mat.client.measure.measuredetails.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.components.GeneralInformationModel;

public class GeneralMeasureInformationView implements ComponentDetailView{
	private FlowPanel mainPanel = new FlowPanel();
	private GeneralInformationModel generalInformationModel;
	public GeneralMeasureInformationView(GeneralInformationModel generalInformationModel) {
		this.generalInformationModel = generalInformationModel;
		GWT.log(generalInformationModel.toString());
		buildDetailView();
	}



	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean hasUnsavedChanges() {
		//TODO compare model against old model for dirty check
		return false;
	}



	@Override
	public void buildDetailView() {
		//TODO
		mainPanel.clear();
		//mainPanel.add(w);
	}

}
