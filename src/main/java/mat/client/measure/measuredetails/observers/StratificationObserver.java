package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.StratificationView;
import mat.shared.measure.measuredetails.models.StratificationModel;

public class StratificationObserver implements MeasureDetailsComponentObserver{
	private StratificationView view;

	public StratificationObserver() {

	}

	public StratificationObserver(StratificationView view) {
		this.view = view;
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (StratificationView) view; 
	}
	
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	private StratificationModel updateFromView() {
		StratificationModel model = (StratificationModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}

}
