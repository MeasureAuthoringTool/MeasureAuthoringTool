package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.CopyrightView;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.shared.measure.measuredetails.models.CopyrightModel;

public class CopyrightObserver implements MeasureDetailsComponentObserver {
	private CopyrightView view;

	public CopyrightObserver() {

	}

	public CopyrightObserver(CopyrightView view) {
		this.view = view;
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		this.view = (CopyrightView) view; 
	}
	
	
	@Override
	public void handleValueChanged() {
		view.setMeasureDetailsComponentModel(updateFromView());
	}
	
	private CopyrightModel updateFromView() {
		CopyrightModel model = (CopyrightModel) view.getMeasureDetailsComponentModel();
		model.setEditorText(view.getTextEditor().getText());
		return model;
	}
}
