package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.DefinitionView;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.shared.measure.measuredetails.models.DefinitionModel;

public class DefinitionObserver implements MeasureDetailsComponentObserver {
	private DefinitionView view;

	public DefinitionObserver() {

	}
	
	public DefinitionObserver(DefinitionView view) {
		this.view = view;
	}
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (DefinitionView) view; 
	}
	
	private DefinitionModel updateFromView() {
		final DefinitionModel model = (DefinitionModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
