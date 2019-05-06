package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.MeasurePopulationView;
import mat.shared.measure.measuredetails.models.MeasurePopulationModel;

public class MeasurePopulationObserver implements MeasureDetailsComponentObserver {
	private MeasurePopulationView view;
	
	public MeasurePopulationObserver() {
		
	}
	
	public MeasurePopulationObserver(MeasurePopulationView view) {
		this.view = view;
	}
	
	@Override 
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (MeasurePopulationView) view; 
	}
	
	private MeasurePopulationModel updateFromView() {
		MeasurePopulationModel model = (MeasurePopulationModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
