package mat.client.measure.measuredetails.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class MeasureStewardView implements ComponentDetailView{
	private FlowPanel mainPanel = new FlowPanel();
	
	public MeasureStewardView() {
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void buildDetailView() {
		// TODO Auto-generated method stub
		
	}

}
