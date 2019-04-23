package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.observers.DescriptionObserver;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.editor.RichTextEditor;
import mat.shared.measure.measuredetails.models.DescriptionModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;

public class DescriptionView implements MeasureDetailViewInterface {
	private FlowPanel mainPanel = new FlowPanel();
	private MeasureDetailsRichTextEditor measureDetailsRichTextEditor;
	private DescriptionModel originalDescriptionModel;
	private DescriptionModel descriptionModel;
	private DescriptionObserver observer;
	
	public DescriptionView() {
		
	}
	
	public DescriptionView(DescriptionModel descriptionModel) {
		this.originalDescriptionModel = descriptionModel;
		buildGeneralInformationModel(this.originalDescriptionModel);
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
		measureDetailsRichTextEditor = new MeasureDetailsRichTextEditor(mainPanel);
		measureDetailsRichTextEditor.getRichTextEditor().setTitle("Description Editor");
		measureDetailsRichTextEditor.getRichTextEditor().setEditorText(this.descriptionModel.getFormattedText());
        addEventHandlers();
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
		return this.descriptionModel;
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
		this.descriptionModel = (DescriptionModel) model; 
		this.originalDescriptionModel = descriptionModel;
		buildGeneralInformationModel(this.originalDescriptionModel);
	}


	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
		this.observer = (DescriptionObserver) observer; 
	}
	
	@Override
	public DescriptionObserver getObserver() {
		return observer;
	}
	
	private void addEventHandlers() {
		measureDetailsRichTextEditor.getRichTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());
	}
	
	private void buildGeneralInformationModel(DescriptionModel originalDescriptionModel) {
		this.descriptionModel = new DescriptionModel(originalDescriptionModel);
	}

	@Override
	public Widget getFirstElement() {
		return measureDetailsRichTextEditor.getRichTextEditor();
	}
}
