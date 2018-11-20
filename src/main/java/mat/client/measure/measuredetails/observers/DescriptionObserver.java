package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.components.DescriptionModel;
import mat.client.measure.measuredetails.view.DescriptionView;

public class DescriptionObserver {
	private DescriptionView descriptionView;
	
	public DescriptionObserver(DescriptionView descriptionView) {
		this.descriptionView = descriptionView;
	}
	
	public void handleDescriptionChanged() {
		descriptionView.setDescriptionModel(updateDescriptionModelFromView());
	}
	
	private DescriptionModel updateDescriptionModelFromView() {
		DescriptionModel descriptionModel = descriptionView.getDescriptionModel();
		descriptionModel.setFormatedText(descriptionView.getRichTextEditor().getFormatedText());
		descriptionModel.setPlanText(descriptionView.getRichTextEditor().getPlanText());
		return descriptionModel;
	}
}
