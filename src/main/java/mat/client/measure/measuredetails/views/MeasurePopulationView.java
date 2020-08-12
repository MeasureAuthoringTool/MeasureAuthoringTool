package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.measuredetails.observers.MeasurePopulationObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.MeasurePopulationModel;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

public class MeasurePopulationView implements MeasureDetailViewInterface {

	private FlowPanel mainPanel = new FlowPanel();
	private MeasureDetailsTextEditor measureDetailsTextEditor;
	private MeasurePopulationModel model;
	private MeasurePopulationModel originalModel;
	private MeasurePopulationObserver observer;

	public MeasurePopulationView() {
		
	}

	public MeasurePopulationView(MeasurePopulationModel measurePopulationModel) {
		this.originalModel = measurePopulationModel;
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
		measureDetailsTextEditor.setTitle("Measure Population Editor");
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
		// TODO Auto-generated method stub
	}

	@Override
	public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
		this.model = (MeasurePopulationModel) model; 
		this.originalModel = this.model;
		buildModel(this.originalModel);		
	}

	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
		this.observer = (MeasurePopulationObserver) observer; 
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return observer;
	}
	
	private void buildModel(MeasurePopulationModel measurePopulationModel) {
		this.model = new MeasurePopulationModel(measurePopulationModel);		
	}
	
	private void addEventHandlers() {
		measureDetailsTextEditor.getTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());
	}

	@Override
	public Widget getFirstElement() {
		return null;
	}
}
