package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.ClinicalRecommendationView;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.shared.measure.measuredetails.models.ClinicalRecommendationModel;

public class ClinicalRecommendationObserver implements MeasureDetailsComponentObserver{
	private ClinicalRecommendationView view;

	public ClinicalRecommendationObserver() {

	}

	public ClinicalRecommendationObserver(ClinicalRecommendationView view) {
		this.view = view;
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (ClinicalRecommendationView) view; 
	}
	
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	private ClinicalRecommendationModel updateFromView() {
		ClinicalRecommendationModel model = (ClinicalRecommendationModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
