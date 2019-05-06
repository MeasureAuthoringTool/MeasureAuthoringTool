package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.GuidanceView;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.shared.measure.measuredetails.models.GuidanceModel;

public class GuidanceObserver implements MeasureDetailsComponentObserver {
	private GuidanceView view;

	public GuidanceObserver() {

	}
	
	public GuidanceObserver(GuidanceView view) {
		this.view = view;
	}
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (GuidanceView) view; 
	}
	
	private GuidanceModel updateFromView() {
		final GuidanceModel model = (GuidanceModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
