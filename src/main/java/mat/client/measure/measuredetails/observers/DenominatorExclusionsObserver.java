package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.DenominatorExclusionsView;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.shared.measure.measuredetails.models.DenominatorExclusionsModel;

public class DenominatorExclusionsObserver implements MeasureDetailsComponentObserver {
	private DenominatorExclusionsView view; 
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (DenominatorExclusionsView) view; 
	}
	
	private DenominatorExclusionsModel updateFromView() {
		DenominatorExclusionsModel model = (DenominatorExclusionsModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
