package mat.client.measure.measuredetails.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MeasureStewardView implements ComponentDetailView{
	private FlowPanel mainPanel = new FlowPanel();
	
	public MeasureStewardView() {
		mainPanel.add(new Label("Measure Steward / Developer"));
	}
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public Widget getWidget() {
		return mainPanel;
	}

}
