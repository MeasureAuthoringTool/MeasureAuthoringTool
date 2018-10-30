package mat.client.measure.measuredetails.view;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.Widget;

public class MeasurePopulationView implements ComponentDetailView {

	private FlowPanel mainPanel = new FlowPanel();

	public MeasurePopulationView() {

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
