package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.DescriptionView;
import mat.shared.measure.measuredetails.models.DescriptionModel;

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
		descriptionModel.setFormattedText(descriptionView.getRichTextEditor().getFormattedText());
		descriptionModel.setPlainText(descriptionView.getRichTextEditor().getPlainText());
		return descriptionModel;
	}
}
