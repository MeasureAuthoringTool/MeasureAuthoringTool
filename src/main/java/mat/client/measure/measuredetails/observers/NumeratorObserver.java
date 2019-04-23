package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.NumeratorView;
import mat.shared.measure.measuredetails.models.NumeratorModel;

public class NumeratorObserver implements MeasureDetailsComponentObserver {
	private NumeratorView view; 
	
	@Override
	public void handleValueChanged() {
		this.view.setMeasureDetailsComponentModel(updateFromView());
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (NumeratorView) view; 
	}
	
	private NumeratorModel updateFromView() {
		NumeratorModel model = (NumeratorModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
