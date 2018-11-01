package mat.client.measure.measuredetails.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ComponentMeasuresView implements ComponentDetailView{
	private FlowPanel mainPanel = new FlowPanel();
	
	public ComponentMeasuresView() {
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
