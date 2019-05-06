package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.DisclaimerView;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.shared.measure.measuredetails.models.DisclaimerModel;

public class DisclaimerObserver implements MeasureDetailsComponentObserver {
	private DisclaimerView view;

	public DisclaimerObserver() {

	}
	
	public DisclaimerObserver(DisclaimerView view) {
		this.view = view;
	}
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (DisclaimerView) view; 
	}
	
	private DisclaimerModel updateFromView() {
		DisclaimerModel model = (DisclaimerModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
