package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.observers.DisclaimerObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.editor.RichTextEditor;
import mat.shared.measure.measuredetails.models.DisclaimerModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;

public class DisclaimerView implements MeasureDetailViewInterface {
	private FlowPanel mainPanel = new FlowPanel();
	private MeasureDetailsRichTextEditor measureDetailsRichTextEditor; 
	private DisclaimerModel disclaimerModel; 
	private DisclaimerModel originalDisclaimerModel; 
	private DisclaimerObserver disclaimerObserver; 
	
	public DisclaimerView(DisclaimerModel disclaimerModel) {
		this.originalDisclaimerModel = disclaimerModel;
		buildDisclaimerModel(this.originalDisclaimerModel);
		buildDetailView();
		addEventHandlers();
	}

	@Override
	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean isComplete() {
		return !this.disclaimerModel.getFormattedText().isEmpty();
	}

	@Override
	public boolean hasUnsavedChanges() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void buildDetailView() {
		measureDetailsRichTextEditor = new MeasureDetailsRichTextEditor(mainPanel);
		measureDetailsRichTextEditor.getRichTextEditor().setTitle("Disclaimer Edit");
		measureDetailsRichTextEditor.getRichTextEditor().setEditorText(this.disclaimerModel.getFormattedText());
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		measureDetailsRichTextEditor.setReadOnly(readOnly);
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
		return this.disclaimerModel;
	}

	@Override
	public RichTextEditor getRichTextEditor() {
		return this.measureDetailsRichTextEditor.getRichTextEditor();
	}

	@Override
	public void clear() {
		this.measureDetailsRichTextEditor.getRichTextEditor().setEditorText("");;		
	}
	
	public DisclaimerModel getDisclaimerModel() {
		return disclaimerModel;
	}

	public void setDisclaimerModel(DisclaimerModel disclaimerModel) {
		this.disclaimerModel = disclaimerModel;
	}

	public DisclaimerObserver getObserver() {
		return disclaimerObserver;
	}

	public void setObserver(DisclaimerObserver disclaimerObserver) {
		this.disclaimerObserver = disclaimerObserver;
	}
	
	private void buildDisclaimerModel(DisclaimerModel model) {
		this.disclaimerModel = new DisclaimerModel(model);
	}
	
	private void addEventHandlers() {
		measureDetailsRichTextEditor.getRichTextEditor().addKeyUpHandler(event -> disclaimerObserver.handleDescriptionChanged());
	}
}
