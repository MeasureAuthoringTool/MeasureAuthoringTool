package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.DescriptionView;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.shared.measure.measuredetails.models.DescriptionModel;

public class DescriptionObserver implements MeasureDetailsComponentObserver {
	private DescriptionView view;
	
	public DescriptionObserver() {
		
	}
	
	public DescriptionObserver(DescriptionView descriptionView) {
		this.view = descriptionView;
	}
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateDescriptionModelFromView());
	}
	
	private DescriptionModel updateDescriptionModelFromView() {
		DescriptionModel descriptionModel = (DescriptionModel) view.getMeasureDetailsComponentModel();
		descriptionModel.setEditorText(view.getTextEditor().getText());
		return descriptionModel;
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (DescriptionView) view; 
	}
}
