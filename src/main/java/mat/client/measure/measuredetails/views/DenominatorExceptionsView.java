package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import mat.client.measure.measuredetails.observers.DenominatorExceptionsObserver;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.shared.measure.measuredetails.models.DenominatorExceptionsModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

public class DenominatorExceptionsView implements MeasureDetailViewInterface {
	private FlowPanel mainPanel = new FlowPanel();
	private MeasureDetailsTextEditor measureDetailsTextEditor;
	private DenominatorExceptionsModel model;
	private DenominatorExceptionsModel originalModel;
	private DenominatorExceptionsObserver observer;
	
	public DenominatorExceptionsView() {

	}
	
	public DenominatorExceptionsView(DenominatorExceptionsModel model) {
		this.originalModel = model; 
		buildModel(this.originalModel);
		buildDetailView();
	}

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
		measureDetailsTextEditor.setTitle("Denominator Exceptions Editor");
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
	public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
		this.model = (DenominatorExceptionsModel) model; 	
		this.originalModel = this.model;
		buildModel(this.originalModel);		
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
	public void setObserver(MeasureDetailsComponentObserver observer) {
		this.observer = (DenominatorExceptionsObserver) observer; 		
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return observer;
	}

	private void buildModel(DenominatorExceptionsModel model) {
		this.model = new DenominatorExceptionsModel(model);
	}
	
	private void addEventHandlers() {
		measureDetailsTextEditor.getTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());		
	}

	@Override
	public Widget getFirstElement() {
		return null;
	}
}
