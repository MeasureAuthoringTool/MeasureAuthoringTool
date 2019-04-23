package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.RateAggregationView;
import mat.shared.measure.measuredetails.models.RateAggregationModel;

public class RateAggregationObserver implements MeasureDetailsComponentObserver{
	private RateAggregationView view;

	public RateAggregationObserver() {

	}

	public RateAggregationObserver(RateAggregationView view) {
		this.view = view;
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (RateAggregationView) view; 
	}
	
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	private RateAggregationModel updateFromView() {
		RateAggregationModel model = (RateAggregationModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
