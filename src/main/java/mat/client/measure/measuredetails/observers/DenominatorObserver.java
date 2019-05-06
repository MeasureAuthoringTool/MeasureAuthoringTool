package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.DenominatorView;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.shared.measure.measuredetails.models.DenominatorModel;

public class DenominatorObserver implements MeasureDetailsComponentObserver {
	private DenominatorView view; 

	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (DenominatorView) view; 
	}
	
	private DenominatorModel updateFromView() {
		DenominatorModel model = (DenominatorModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
