package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.NumeratorExclusionsView;
import mat.shared.measure.measuredetails.models.NumeratorExclusionsModel;

public class NumeratorExclusionsObserver implements MeasureDetailsComponentObserver {
	private NumeratorExclusionsView view; 
	
	
	@Override
	public void handleValueChanged() {
		this.view.setMeasureDetailsComponentModel(updateFromView());
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (NumeratorExclusionsView) view; 		
	}
	
	private NumeratorExclusionsModel updateFromView() {
		NumeratorExclusionsModel model = (NumeratorExclusionsModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
