package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.TransmissionFormatView;
import mat.shared.measure.measuredetails.models.TransmissionFormatModel;

public class TransmissionFormatObserver implements MeasureDetailsComponentObserver {
	private TransmissionFormatView view;

	public TransmissionFormatObserver() {

	}
	
	public TransmissionFormatObserver(TransmissionFormatView view) {
		this.view = view;
	}
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (TransmissionFormatView) view; 
	}
	
	private TransmissionFormatModel updateFromView() {
		final TransmissionFormatModel model = (TransmissionFormatModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
