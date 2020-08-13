package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.measuredetails.observers.NumeratorObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.NumeratorModel;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

public class NumeratorView implements MeasureDetailViewInterface {
	private FlowPanel mainPanel = new FlowPanel();
	private MeasureDetailsTextEditor measureDetailsTextEditor;
	private NumeratorModel model;
	private NumeratorModel originalModel;
	private NumeratorObserver observer;
	
	public NumeratorView() {

	}
	
	public NumeratorView(NumeratorModel model) {
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
		measureDetailsTextEditor.setTitle("Numerator Editor");
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
		this.model = (NumeratorModel) model; 	
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
		this.observer = (NumeratorObserver) observer; 
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return observer;
	}
	
	private void buildModel(NumeratorModel model) {
		this.model = new NumeratorModel(model);
	}
	
	private void addEventHandlers() {
		measureDetailsTextEditor.getTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());		
	}

	@Override
	public Widget getFirstElement() {
		return null;
	}
}
