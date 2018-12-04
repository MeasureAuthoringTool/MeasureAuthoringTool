package mat.client.measure.measuredetails.views;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.observers.MeasurePopulationObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.editor.RichTextEditor;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.MeasurePopulationModel;

public class MeasurePopulationView implements MeasureDetailViewInterface {

	private FlowPanel mainPanel = new FlowPanel();
	private MeasureDetailsRichTextEditor measureDetailsRichTextEditor;
	private MeasurePopulationModel measurePopulationModel;
	private MeasurePopulationModel originalMeasurePopulationModel;
	private MeasurePopulationObserver observer;

	public MeasurePopulationView() {
		
	}

	public MeasurePopulationView(MeasurePopulationModel measurePopulationModel) {
		this.originalMeasurePopulationModel = measurePopulationModel;
		buildMeasurePopulationModel(this.originalMeasurePopulationModel);
		buildDetailView();
		addEventHandlers();
	}
	
	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return true;
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
		measureDetailsRichTextEditor = new MeasureDetailsRichTextEditor(mainPanel);
		measureDetailsRichTextEditor.getRichTextEditor().setTitle("Measure Population Edit Edit");
		measureDetailsRichTextEditor.getRichTextEditor().setEditorText(this.measurePopulationModel.getFormattedText());			
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		measureDetailsRichTextEditor.getRichTextEditor().setReadOnly(readOnly);	
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
		return this.measurePopulationModel;
	}

	@Override
	public RichTextEditor getRichTextEditor() {
		return this.measureDetailsRichTextEditor.getRichTextEditor();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
	public MeasurePopulationObserver getObserver() {
		return observer;
	}
	
	public void setObserver(MeasurePopulationObserver observer) {
		this.observer = observer; 
	}
	
	private void buildMeasurePopulationModel(MeasurePopulationModel measurePopulationModel) {
		this.measurePopulationModel = new MeasurePopulationModel(measurePopulationModel);		
	}
	
	public MeasurePopulationModel getMeasurePopulationModel() {
		return this.measurePopulationModel;
	}
	
	public void setMeasurePopulationModel(MeasurePopulationModel measurePopulationModel) {
		this.measurePopulationModel = measurePopulationModel;
	}
	
	private void addEventHandlers() {
		measureDetailsRichTextEditor.getRichTextEditor().addKeyUpHandler(event -> observer.handleDescriptionChanged());
	}
}
