package mat.client.measure.measuredetails.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.MeasureDetailState;
import mat.client.measure.measuredetails.components.MeasureDetailsComponentModel;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.util.RichTextEditor;

public class DisclaimerView implements ComponentDetailView {
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
		return null;
	}

	@Override
	public ConfirmationDialogBox getSaveConfirmation() {
		return null;
	}

	@Override
	public void resetForm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MeasureDetailsComponentModel getMeasureDetailsComponentModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RichTextEditor getRichTextEditor() {
		// TODO Auto-generated method stub
		return null;
	}

}
