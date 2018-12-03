package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.DisclaimerView;
import mat.shared.measure.measuredetails.models.DisclaimerModel;

public class DisclaimerObserver {
	
	private DisclaimerView disclaimerView;

	public DisclaimerObserver(DisclaimerView disclaimerView) {
		this.disclaimerView = disclaimerView;
	}
	
	public void handleDescriptionChanged() {
		disclaimerView.setDisclaimerModel(updateCopyrightModelFromView());
	}
	
	private DisclaimerModel updateCopyrightModelFromView() {
		DisclaimerModel disclaimerModel = (DisclaimerModel) disclaimerView.getDisclaimerModel();
		disclaimerModel.setFormattedText(disclaimerView.getRichTextEditor().getFormattedText());
		disclaimerModel.setPlainText(disclaimerView.getRichTextEditor().getPlainText());
		return disclaimerModel;
	}
}
