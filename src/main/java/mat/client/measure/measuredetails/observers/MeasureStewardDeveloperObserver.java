package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.MeasureStewardView;
import mat.shared.measure.measuredetails.models.MeasureStewardDeveloperModel;

public class MeasureStewardDeveloperObserver implements MeasureDetailsComponentObserver {
	
	private MeasureStewardView view;
	
	public MeasureStewardDeveloperObserver() {

	}

	public MeasureStewardDeveloperObserver(MeasureStewardView view) {
		this.view = view;
	}

	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (MeasureStewardView) view;
	}

	private MeasureStewardDeveloperModel updateFromView() {
		final MeasureStewardDeveloperModel model = (MeasureStewardDeveloperModel) view.getMeasureDetailsComponentModel();
		final int index = view.getStewardListBox().getSelectedIndex();
		if (index != 0) {
			model.setStewardId(view.getStewardListBox().getValue(index));
			model.setStewardValue(view.getStewardListBox().getItemText(index));
		} else {
			model.setStewardId(null);
			model.setStewardValue(null);
		}
		return model;
	}
}
