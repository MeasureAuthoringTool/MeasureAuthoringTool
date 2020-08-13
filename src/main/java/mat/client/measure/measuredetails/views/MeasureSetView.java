package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.measuredetails.observers.MeasureSetObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.MeasureSetModel;

public class MeasureSetView implements MeasureDetailViewInterface {
	private final FlowPanel mainPanel = new FlowPanel();
	private MeasureDetailsTextEditor measureDetailsTextEditor;
	private MeasureSetModel model;
	private MeasureSetModel originalModel;
	private MeasureSetObserver observer;
	
	public MeasureSetView() {
		
	}
	
	public MeasureSetView(MeasureSetModel model) {
		this.originalModel = model; 
		this.model = new MeasureSetModel(this.originalModel);
		buildDetailView();
	}
	
	@Override
	public Widget getWidget() {
		return mainPanel;
	}
	
	@Override
	public boolean hasUnsavedChanges() {
		return false;
	}

	@Override
	public void buildDetailView() {
		measureDetailsTextEditor = new MeasureDetailsTextEditor();
		mainPanel.add(measureDetailsTextEditor);
		measureDetailsTextEditor.setTitle("Measure Set Editor");
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
		return measureDetailsTextEditor.getTextEditor();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
		this.originalModel = (MeasureSetModel) model;
		this.model = new MeasureSetModel(this.originalModel);
	}

	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
		this.observer = (MeasureSetObserver) observer; 
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return observer;
	}

	private void addEventHandlers() {
		measureDetailsTextEditor.getTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());		
	}

	@Override
	public Widget getFirstElement() {
		return null;
	}
}
