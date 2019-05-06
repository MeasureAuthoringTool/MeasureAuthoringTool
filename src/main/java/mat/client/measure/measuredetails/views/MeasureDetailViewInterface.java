package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;

public interface MeasureDetailViewInterface {
	public Widget getFirstElement();
	public Widget getWidget();
	public void buildDetailView();
	public void clear();
	public boolean hasUnsavedChanges();
	public void setReadOnly(boolean readOnly);
	public ConfirmationDialogBox getSaveConfirmation();
	public void resetForm();
	public MeasureDetailsComponentModel getMeasureDetailsComponentModel();
	void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model);
	public TextArea getTextEditor();
	void setObserver(MeasureDetailsComponentObserver observer);
	MeasureDetailsComponentObserver getObserver();
}
