package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.InitialPopulationView;
import mat.shared.measure.measuredetails.models.InitialPopulationModel;

public class InitialPopulationObserver {
	private InitialPopulationView initialPopulationView;
	
	public InitialPopulationObserver(InitialPopulationView initialPopulationView) {
		this.initialPopulationView = initialPopulationView;
	}
	
	public void handleDescriptionChanged() {
		initialPopulationView.setInitialPopulationModel(updateDescriptionModelFromView());
	}
	
	private InitialPopulationModel updateDescriptionModelFromView() {
		InitialPopulationModel initialPopulationModel = (InitialPopulationModel) initialPopulationView.getMeasureDetailsComponentModel();
		initialPopulationModel.setFormattedText(initialPopulationView.getRichTextEditor().getFormattedText());
		initialPopulationModel.setPlainText(initialPopulationView.getRichTextEditor().getPlainText());
		return initialPopulationModel;
	}
}
