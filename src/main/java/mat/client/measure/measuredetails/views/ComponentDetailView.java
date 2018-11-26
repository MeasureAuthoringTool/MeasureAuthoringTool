package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.MeasureDetailState;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.editor.RichTextEditor;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;

public interface ComponentDetailView {
	public Widget getWidget();
	public void buildDetailView();
	public void clear();
	public boolean isComplete();
	public boolean hasUnsavedChanges();
	public MeasureDetailState getState();
	public void setReadOnly(boolean readOnly);
	public ConfirmationDialogBox getSaveConfirmation();
	public void resetForm();
	public MeasureDetailsComponentModel getMeasureDetailsComponentModel();
	public RichTextEditor getRichTextEditor();
}
