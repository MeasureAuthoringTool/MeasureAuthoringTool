package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.observers.ClinicalRecommendationObserver;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.shared.measure.measuredetails.models.ClinicalRecommendationModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;

public class ClinicalRecommendationView implements MeasureDetailViewInterface {
	private FlowPanel mainPanel = new FlowPanel();
	
	private MeasureDetailsTextEditor measureDetailsTextEditor;
	private ClinicalRecommendationModel model;
	private ClinicalRecommendationModel originalModel;
	private ClinicalRecommendationObserver observer;
	
	public ClinicalRecommendationView() {
		
	}
	
	public ClinicalRecommendationView(ClinicalRecommendationModel model) {
		this.originalModel = model; 
		buildModel(this.originalModel);
		buildDetailView();
	}
	
	@Override
	public Widget getWidget() {
		return mainPanel;
	}


	@Override
	public boolean hasUnsavedChanges() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void buildDetailView() {
		measureDetailsTextEditor = new MeasureDetailsTextEditor();
		mainPanel.add(measureDetailsTextEditor);
		measureDetailsTextEditor.setTitle("Clinical Recommendation Editor");
		measureDetailsTextEditor.setText(this.model.getEditorText());
		addEventHandlers();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.measureDetailsTextEditor.setReadOnly(readOnly);
	}

	@Override
	public ConfirmationDialogBox getSaveConfirmation() {
		return null;
	}

	@Override
	public void resetForm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MeasureDetailsComponentModel getMeasureDetailsComponentModel() {
		return this.model;
	}

	@Override
	public TextArea getTextEditor() {
		return this.measureDetailsTextEditor.getTextEditor();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
		this.model = (ClinicalRecommendationModel) model; 	
		this.originalModel = this.model;
		buildModel(this.originalModel);
	}

	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
		this.observer = (ClinicalRecommendationObserver) observer; 
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return observer;
	}
	
	private void buildModel(ClinicalRecommendationModel model) {
		this.model = new ClinicalRecommendationModel(model);
	}
	
	private void addEventHandlers() {
		measureDetailsTextEditor.getTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());		
	}

	@Override
	public Widget getFirstElement() {
		return null;
	}
}
