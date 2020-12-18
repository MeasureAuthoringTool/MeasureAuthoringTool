package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import mat.client.measure.measuredetails.observers.InitialPopulationObserver;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.shared.measure.measuredetails.models.InitialPopulationModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

public class InitialPopulationView implements MeasureDetailViewInterface {

	private FlowPanel mainPanel = new FlowPanel();
	private MeasureDetailsTextEditor measureDetailsTextEditor;
	private InitialPopulationModel model;
	private InitialPopulationModel originalModel;
	private InitialPopulationObserver observer;

	public InitialPopulationView() {

	}
	
	public InitialPopulationView(InitialPopulationModel initialPopulationModel) {
		this.originalModel = initialPopulationModel;
		buildInitialPopulationModel(this.originalModel);
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
		measureDetailsTextEditor.setTitle("Initial Population Editor");
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
		return measureDetailsTextEditor.getTextEditor();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
	}
	
	public void setObserver(InitialPopulationObserver observer) {
		this.observer = observer; 
	}
		
	private void addEventHandlers() {
		measureDetailsTextEditor.getTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());
	}
	
	private void buildInitialPopulationModel(InitialPopulationModel initialPopulationModel) {
		this.model = new InitialPopulationModel(initialPopulationModel);
	}

	@Override
	public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
		this.model = (InitialPopulationModel) model; 	
		this.originalModel = this.model;
		buildInitialPopulationModel(this.originalModel);
	}

	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
		this.observer = (InitialPopulationObserver) observer; 
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return observer;
	}

	@Override
	public Widget getFirstElement() {
		return measureDetailsTextEditor.getTextEditor();
	}
}
