package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.RiskAdjustmentView;
import mat.shared.measure.measuredetails.models.RiskAdjustmentModel;

public class RiskAdjustmentObserver implements MeasureDetailsComponentObserver{
	private RiskAdjustmentView view;

	public RiskAdjustmentObserver() {

	}

	public RiskAdjustmentObserver(RiskAdjustmentView view) {
		this.view = view;
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (RiskAdjustmentView) view; 
	}
	
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	private RiskAdjustmentModel updateFromView() {
		RiskAdjustmentModel model = (RiskAdjustmentModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
