package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.MeasureDetailState;
import mat.client.measure.measuredetails.observers.DescriptionObserver;
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
	@Override
	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean isComplete() {
		return !this.descriptionModel.getFormattedText().isEmpty();
	}

	@Override
	public boolean hasUnsavedChanges() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void buildDetailView() {
		measureDetailsRichTextEditor = new MeasureDetailsRichTextEditor(mainPanel);
		measureDetailsRichTextEditor.getRichTextEditor().setTitle("Description Edit");
		measureDetailsRichTextEditor.getRichTextEditor().setCode(this.descriptionModel.getFormattedText());
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		measureDetailsRichTextEditor.setReadOnly(readOnly);
	}

	@Override
	public MeasureDetailState getState() {
		if(this.descriptionModel.getFormattedText().isEmpty()) {
			return MeasureDetailState.BLANK;
		} else {
			return MeasureDetailState.COMPLETE;
		}
		
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
	
	

	public DescriptionView(DescriptionModel descriptionModel) {
		this.originalDescriptionModel = descriptionModel;
		buildGeneralInformationModel(this.originalDescriptionModel);
		
		buildDetailView();
		
        addEventHandlers();
	}
	
	private void addEventHandlers() {
		measureDetailsRichTextEditor.getRichTextEditor().addSummernoteKeyUpHandler(event -> observer.handleDescriptionChanged());
	}
	
	private void buildGeneralInformationModel(DescriptionModel originalDescriptionModel) {
		this.descriptionModel = new DescriptionModel();
		descriptionModel.setFormattedText(originalDescriptionModel.getFormattedText());
	}

	@Override
	public RichTextEditor getRichTextEditor() {
		return measureDetailsRichTextEditor.getRichTextEditor();
	}
	
	public DescriptionModel getDescriptionModel() {
		return descriptionModel;
	}

	public void setDescriptionModel(DescriptionModel descriptionModel) {
		this.descriptionModel = descriptionModel;
	}

	public DescriptionObserver getObserver() {
		return observer;
	}

	public void setObserver(DescriptionObserver observer) {
		this.observer = observer;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
}
