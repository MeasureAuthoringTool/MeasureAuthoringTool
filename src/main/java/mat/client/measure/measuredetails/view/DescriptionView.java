package mat.client.measure.measuredetails.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.MeasureDetailState;
import mat.client.shared.ConfirmationDialogBox;

public class DescriptionView implements ComponentDetailView {
	private FlowPanel mainPanel = new FlowPanel();
	@Override
	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
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

	@Override
	public void setReadOnly(boolean readOnly) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MeasureDetailState getState() {
		// TODO Auto-generated method stub
		return MeasureDetailState.BLANK;
	}

	@Override
	public ConfirmationDialogBox getSaveConfirmation() {
		return null;
	}

}
