package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.observers.CopyrightObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.editor.RichTextEditor;
import mat.shared.measure.measuredetails.models.CopyrightModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;

public class CopyrightView implements MeasureDetailViewInterface {
	private FlowPanel mainPanel = new FlowPanel();
	private MeasureDetailsRichTextEditor measureDetailsRichTextEditor; 
	private CopyrightModel originalCopyrightModel; 
	private CopyrightModel copyrightModel; 
	private CopyrightObserver copyrightObserver; 
	
	public CopyrightView(CopyrightModel copyrightModel) {
		this.originalCopyrightModel = copyrightModel;
		buildCopyrightModel(this.originalCopyrightModel);
		buildDetailView();
		addEventHandlers();
	}
	
	@Override
	public boolean isComplete() {
		return !this.copyrightModel.getFormattedText().isEmpty();
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
		measureDetailsRichTextEditor.getRichTextEditor().setTitle("Copyright Edit");
		measureDetailsRichTextEditor.getRichTextEditor().setEditorText(this.copyrightModel.getFormattedText());	
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
		return this.copyrightModel;
	}
	
	public CopyrightModel getCopyrightModel() {
		return copyrightModel;
	}

	public void setCopyrightModel(CopyrightModel copyrightModel) {
		this.copyrightModel = copyrightModel;
	}

	@Override
	public RichTextEditor getRichTextEditor() {
		return this.measureDetailsRichTextEditor.getRichTextEditor();
	}

	@Override
	public void clear() {
		this.measureDetailsRichTextEditor.getRichTextEditor().setEditorText("");		
	}
	
	public CopyrightObserver getObserver() {
		return copyrightObserver;
	}

	public void setObserver(CopyrightObserver copyrightObserver) {
		this.copyrightObserver = copyrightObserver;
	}
	
	private void buildCopyrightModel(CopyrightModel originalCopyrightModel) {
		this.copyrightModel = new CopyrightModel(originalCopyrightModel);
	}

	private void addEventHandlers() {
		measureDetailsRichTextEditor.getRichTextEditor().addKeyUpHandler(event -> copyrightObserver.handleDescriptionChanged());
	}
}
