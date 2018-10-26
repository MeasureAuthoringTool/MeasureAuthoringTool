package mat.client.measure.measuredetails.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class DescriptionView implements ComponentDetailView {
	private FlowPanel mainPanel = new FlowPanel();
	@Override
	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

}
