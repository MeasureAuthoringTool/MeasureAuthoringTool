package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.SupplementalDataElementsView;
import mat.shared.measure.measuredetails.models.SupplementalDataElementsModel;

public class SupplementalDataElementsObserver implements MeasureDetailsComponentObserver {
	private SupplementalDataElementsView view;

	public SupplementalDataElementsObserver() {

	}
	
	public SupplementalDataElementsObserver(SupplementalDataElementsView view) {
		this.view = view;
	}
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (SupplementalDataElementsView) view; 
	}
	
	private SupplementalDataElementsModel updateFromView() {
		final SupplementalDataElementsModel model = (SupplementalDataElementsModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
