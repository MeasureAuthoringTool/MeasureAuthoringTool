package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.MeasurePopulationExclusionsView;
import mat.shared.measure.measuredetails.models.MeasurePopulationExclusionsModel;

public class MeasurePopulationExclusionsObserver implements MeasureDetailsComponentObserver {
	private MeasurePopulationExclusionsView view;
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (MeasurePopulationExclusionsView) view; 
	}
	
	private MeasurePopulationExclusionsModel updateFromView() {
		MeasurePopulationExclusionsModel model = (MeasurePopulationExclusionsModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
