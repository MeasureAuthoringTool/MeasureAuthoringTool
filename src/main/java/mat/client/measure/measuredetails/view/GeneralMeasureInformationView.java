package mat.client.measure.measuredetails.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.components.GeneralInformationComponent;

public class GeneralMeasureInformationView implements ComponentDetailView{
	private FlowPanel mainPanel = new FlowPanel();
	private GeneralInformationComponent generalinformationComponent;
	public GeneralMeasureInformationView(GeneralInformationComponent generalInformationComponent) {
		this.generalinformationComponent = generalInformationComponent;
	}
	
	//TODO compare model against old model
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public Widget getWidget() {
		return mainPanel;
	}

}
