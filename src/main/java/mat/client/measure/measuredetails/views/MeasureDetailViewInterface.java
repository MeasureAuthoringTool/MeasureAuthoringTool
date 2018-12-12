package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.editor.RichTextEditor;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;

public interface MeasureDetailViewInterface {
	public Widget getWidget();
	public void buildDetailView();
	public void clear();
	public boolean isComplete();
	public boolean hasUnsavedChanges();
	public void setReadOnly(boolean readOnly);
	public ConfirmationDialogBox getSaveConfirmation();
	public void resetForm();
	public MeasureDetailsComponentModel getMeasureDetailsComponentModel();
	void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model);
	public RichTextEditor getRichTextEditor();
	void setObserver(MeasureDetailsComponentObserver observer);
	MeasureDetailsComponentObserver getObserver();
}
