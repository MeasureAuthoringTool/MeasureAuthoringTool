package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasurePopulationView;
import mat.shared.measure.measuredetails.models.MeasurePopulationModel;

public class MeasurePopulationObserver {
	private MeasurePopulationView measurePopulationView;
	
	public MeasurePopulationObserver(MeasurePopulationView measurePopulationView) {
		this.measurePopulationView = measurePopulationView;
	}
	
	public void handleDescriptionChanged() {
		measurePopulationView.setMeasurePopulationModel(updateDescriptionModelFromView());
	}
	
	private MeasurePopulationModel updateDescriptionModelFromView() {
		MeasurePopulationModel measurePopulationModel = (MeasurePopulationModel) measurePopulationView.getMeasureDetailsComponentModel();
		measurePopulationModel.setFormattedText(measurePopulationView.getRichTextEditor().getFormattedText());
		measurePopulationModel.setPlainText(measurePopulationView.getRichTextEditor().getPlainText());
		return measurePopulationModel;
	}
}
