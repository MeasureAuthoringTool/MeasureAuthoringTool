package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.ImprovementNotationView;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.shared.measure.measuredetails.models.ImprovementNotationModel;

public class ImprovementNotationObserver implements MeasureDetailsComponentObserver {
	private ImprovementNotationView view;

	public ImprovementNotationObserver() {
		
	}
	
	public ImprovementNotationObserver(ImprovementNotationView descriptionView) {
		this.view = descriptionView;
	}
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	private ImprovementNotationModel updateFromView() {
		final ImprovementNotationModel improvementNotationModel = (ImprovementNotationModel) view.getMeasureDetailsComponentModel();
		improvementNotationModel.setEditorText(view.getTextEditor().getText());
		return improvementNotationModel;
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (ImprovementNotationView) view; 
	}
}
