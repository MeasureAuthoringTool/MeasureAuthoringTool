package mat.client.measure.measuredetails.view;

import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.MeasureDetailState;
import mat.client.measure.measuredetails.components.MeasureDetailsComponentModel;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.util.RichTextEditor;

public interface ComponentDetailView {
	public Widget getWidget();
	public void buildDetailView();
	public boolean isComplete();
	public boolean hasUnsavedChanges();
	public MeasureDetailState getState();
	public void setReadOnly(boolean readOnly);
	public ConfirmationDialogBox getSaveConfirmation();
	public void resetForm();
	public MeasureDetailsComponentModel getMeasureDetailsComponentModel();
	public RichTextEditor getRichTextEditor();
}
