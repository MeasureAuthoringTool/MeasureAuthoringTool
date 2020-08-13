package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.measuredetails.observers.RationaleObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.RationaleModel;

public class RationaleView implements MeasureDetailViewInterface {
	private FlowPanel mainPanel = new FlowPanel();
	
	private MeasureDetailsTextEditor measureDetailsTextEditor;
	private RationaleModel model;
	private RationaleModel originalModel;
	private RationaleObserver observer;
	
	public RationaleView() {
		
	}
	
	public RationaleView(RationaleModel model) {
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
		measureDetailsTextEditor.setTitle("Rationale Editor");
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
		this.model = (RationaleModel) model; 	
		this.originalModel = this.model;
		buildModel(this.originalModel);
	}

	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
		this.observer = (RationaleObserver) observer; 
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return observer;
	}
	
	private void buildModel(RationaleModel model) {
		this.model = new RationaleModel(model);
	}
	
	private void addEventHandlers() {
		measureDetailsTextEditor.getTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());		
	}

	@Override
	public Widget getFirstElement() {
		return null;
	}

}
