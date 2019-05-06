package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.InitialPopulationView;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.shared.measure.measuredetails.models.InitialPopulationModel;

public class InitialPopulationObserver implements MeasureDetailsComponentObserver {
	private InitialPopulationView view;
	
	public InitialPopulationObserver() {
		
	}
	
	public InitialPopulationObserver(InitialPopulationView initialPopulationView) {
		this.view = initialPopulationView;
	}
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (InitialPopulationView) view; 
	}
	
	private InitialPopulationModel updateFromView() {
		InitialPopulationModel model = (InitialPopulationModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
