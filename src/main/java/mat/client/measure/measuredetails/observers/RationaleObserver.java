package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.RationaleView;
import mat.shared.measure.measuredetails.models.RationaleModel;

public class RationaleObserver implements MeasureDetailsComponentObserver{
	private RationaleView view;

	public RationaleObserver() {

	}

	public RationaleObserver(RationaleView view) {
		this.view = view;
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (RationaleView) view; 
	}
	
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	private RationaleModel updateFromView() {
		RationaleModel model = (RationaleModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
