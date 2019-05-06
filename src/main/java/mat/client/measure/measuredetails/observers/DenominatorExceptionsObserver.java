package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.DenominatorExceptionsView;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.shared.measure.measuredetails.models.DenominatorExceptionsModel;

public class DenominatorExceptionsObserver implements MeasureDetailsComponentObserver {
	private DenominatorExceptionsView view; 
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (DenominatorExceptionsView) view; 		
	}
	
	private DenominatorExceptionsModel updateFromView() {
		DenominatorExceptionsModel model = (DenominatorExceptionsModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
