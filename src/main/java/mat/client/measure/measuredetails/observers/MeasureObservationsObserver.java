package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.MeasureObservationsView;
import mat.shared.measure.measuredetails.models.MeasureObservationsModel;

public class MeasureObservationsObserver implements MeasureDetailsComponentObserver {
	private MeasureObservationsView view; 

	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}

	private MeasureObservationsModel updateFromView() {
		MeasureObservationsModel model = (MeasureObservationsModel) view.getMeasureDetailsComponentModel();
		model.setFormattedText(view.getRichTextEditor().getFormattedText());
		model.setPlainText(view.getRichTextEditor().getPlainText());
		return model;
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (MeasureObservationsView) view; 
	}

}
