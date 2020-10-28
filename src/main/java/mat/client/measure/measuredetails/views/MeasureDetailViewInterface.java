package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;

public interface MeasureDetailViewInterface {
	Widget getFirstElement();
	Widget getWidget();
	void buildDetailView();
	void clear();
	boolean hasUnsavedChanges();
	void setReadOnly(boolean readOnly);
	ConfirmationDialogBox getSaveConfirmation();
	void resetForm();
	MeasureDetailsComponentModel getMeasureDetailsComponentModel();
	void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model);
	TextArea getTextEditor();

	void setObserver(MeasureDetailsComponentObserver observer);
	MeasureDetailsComponentObserver getObserver();
}
