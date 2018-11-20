package mat.client.measure.measuredetails.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.MeasureDetailState;
import mat.client.measure.measuredetails.components.DescriptionModel;
import mat.client.measure.measuredetails.components.GeneralInformationModel;
import mat.client.measure.measuredetails.components.MeasureDetailsComponentModel;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.util.RichTextEditor;

public class DescriptionView implements ComponentDetailView {
	private FlowPanel mainPanel = new FlowPanel();
	private RichTextEditor richTextEditor = new RichTextEditor();
	private DescriptionModel originalDescriptionModel;
	private DescriptionModel descriptionModel;
	@Override
	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasUnsavedChanges() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void buildDetailView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		richTextEditor.setEnabled(!readOnly);
	}

	@Override
	public MeasureDetailState getState() {
		if(this.descriptionModel.getDescription().isEmpty()) {
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
		richTextEditor.setTitle("Description Edit");
		richTextEditor.setCode(descriptionModel.getDescription());
		Window.alert(descriptionModel.getDescription());
		HorizontalPanel textAreaPanel = new HorizontalPanel();
        textAreaPanel.add(richTextEditor);
        textAreaPanel.setWidth("95%");
        mainPanel.add(textAreaPanel);
	}
	
	private void buildGeneralInformationModel(DescriptionModel originalDescriptionModel) {
		this.descriptionModel = new DescriptionModel();
		descriptionModel.setDescription(originalDescriptionModel.getDescription());
	}

	@Override
	public RichTextEditor getRichTextEditor() {
		return richTextEditor;
	}
}
