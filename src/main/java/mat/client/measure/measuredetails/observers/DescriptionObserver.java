package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.view.DescriptionView;
import mat.shared.measure.measuredetails.components.DescriptionModel;

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
		descriptionModel.setFormatedText(descriptionView.getRichTextEditor().getFormattedText());
		descriptionModel.setPlainText(descriptionView.getRichTextEditor().getPlainText());
		return descriptionModel;
	}
}
