package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.CopyrightView;
import mat.shared.measure.measuredetails.models.CopyrightModel;

public class CopyrightObserver {
private CopyrightView copyrightView;
	
	public CopyrightObserver(CopyrightView copyrightView) {
		this.copyrightView = copyrightView;
	}
	
	public void handleDescriptionChanged() {
		copyrightView.setCopyrightModel(updateCopyrightModelFromView());
	}
	
	private CopyrightModel updateCopyrightModelFromView() {
		CopyrightModel copyrightModel = (CopyrightModel) copyrightView.getCopyrightModel();
		copyrightModel.setFormattedText(copyrightView.getRichTextEditor().getFormattedText());
		copyrightModel.setPlainText(copyrightView.getRichTextEditor().getPlainText());
		return copyrightModel;
	}
}
