package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;

public interface MeasureDetailsComponentObserver {
	void handleValueChanged();
	void setView(MeasureDetailViewInterface view);
}
