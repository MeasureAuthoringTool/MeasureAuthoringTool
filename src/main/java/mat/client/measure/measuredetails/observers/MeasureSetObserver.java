package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.MeasureSetView;
import mat.shared.measure.measuredetails.models.MeasureSetModel;

public class MeasureSetObserver implements MeasureDetailsComponentObserver {
	private MeasureSetView view;

	public MeasureSetObserver() {

	}
	
	public MeasureSetObserver(MeasureSetView view) {
		this.view = view;
	}
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (MeasureSetView) view; 
	}
	
	private MeasureSetModel updateFromView() {
		final MeasureSetModel model = (MeasureSetModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
