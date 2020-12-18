package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import mat.client.measure.measuredetails.observers.DisclaimerObserver;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.shared.measure.measuredetails.models.DisclaimerModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;

public class DisclaimerView implements MeasureDetailViewInterface {
	private FlowPanel mainPanel = new FlowPanel();
	private MeasureDetailsTextEditor measureDetailsTextEditor; 
	private DisclaimerModel model; 
	private DisclaimerModel originalModel; 
	private DisclaimerObserver observer; 
	
	public DisclaimerView() {
		
	}
	
	public DisclaimerView(DisclaimerModel disclaimerModel) {
		this.originalModel = disclaimerModel;
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
		measureDetailsTextEditor.setTitle("Disclaimer Editor");
		measureDetailsTextEditor.setText(this.model.getEditorText());
		addEventHandlers();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		measureDetailsTextEditor.setReadOnly(readOnly);
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
		this.measureDetailsTextEditor.setText("");;		
	}
	
	@Override
	public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
		this.model = (DisclaimerModel) model; 
		this.originalModel = this.model;
		buildModel(this.originalModel);		
	}

	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
		this.observer = (DisclaimerObserver) observer; 
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return observer;
	}
	
	private void buildModel(DisclaimerModel model) {
		this.model = new DisclaimerModel(model);
	}
	
	private void addEventHandlers() {
		measureDetailsTextEditor.getTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());
	}

	@Override
	public Widget getFirstElement() {
		return measureDetailsTextEditor.getTextEditor();
	}
}
