package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.measuredetails.observers.TransmissionFormatObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.editor.RichTextEditor;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.TransmissionFormatModel;

public class TransmissionFormatView implements MeasureDetailViewInterface {
	private final FlowPanel mainPanel = new FlowPanel();
	private MeasureDetailsRichTextEditor measureDetailsRichTextEditor;
	private TransmissionFormatModel model;
	private TransmissionFormatModel originalModel;
	private TransmissionFormatObserver observer;
	
	public TransmissionFormatView() {
		
	}
	
	public TransmissionFormatView(TransmissionFormatModel model) {
		this.originalModel = model; 
		this.model = new TransmissionFormatModel(this.originalModel);
		buildDetailView();
	}
	
	@Override
	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public boolean hasUnsavedChanges() {
		return false;
	}

	@Override
	public void buildDetailView() {
		measureDetailsRichTextEditor = new MeasureDetailsRichTextEditor(mainPanel);
		measureDetailsRichTextEditor.getRichTextEditor().setTitle("Transmission Format Editor");
		measureDetailsRichTextEditor.getRichTextEditor().setEditorText(this.model.getFormattedText());	
		addEventHandlers();		
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.measureDetailsRichTextEditor.setReadOnly(readOnly);
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
	public RichTextEditor getRichTextEditor() {
		return measureDetailsRichTextEditor.getRichTextEditor();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
		this.originalModel = (TransmissionFormatModel) model;
		this.model = new TransmissionFormatModel(this.originalModel);
	}

	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
		this.observer = (TransmissionFormatObserver) observer; 
	}

	private void addEventHandlers() {
		measureDetailsRichTextEditor.getRichTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());		
	}
}
