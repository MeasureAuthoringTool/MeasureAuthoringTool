package mat.client.measure.measuredetails.view;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.Widget;

public class NumeratorView implements ComponentDetailView {

	private FlowPanel mainPanel = new FlowPanel();

	public NumeratorView() {

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
