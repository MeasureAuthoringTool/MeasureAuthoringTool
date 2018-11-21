package mat.client.measure.measuredetails;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.extras.summernote.client.event.SummernoteKeyUpEvent;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.buttons.DeleteButton;
import mat.client.buttons.SaveButton;
import mat.client.measure.measuredetails.navigation.MeasureDetailsNavigation;
import mat.client.measure.measuredetails.views.ComponentDetailView;
import mat.client.measure.measuredetails.views.MeasureDetailsViewFactory;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.client.shared.editor.RichTextEditor;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsModel;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;

public class MeasureDetailsView {
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel mainContentPanel = new HorizontalPanel();
	private HorizontalPanel headingPanel = new HorizontalPanel();
	private HorizontalPanel saveButtonPanel = new HorizontalPanel();
	private VerticalPanel widgetComponentPanel = new VerticalPanel();
	private ErrorMessageAlert errorAlert = new ErrorMessageAlert();
	private MatDetailItem currentMeasureDetail;
	private ComponentDetailView componentDetailView;
	private boolean isMeasureEditable;
	private SaveButton saveButton = new SaveButton("Measure Details");
	private DeleteButton deleteMeasureButton = new DeleteButton("Measure Details", "Delete Measure");
	private MeasureDetailsModel measureDetailsComponent;
	private RichTextEditor currentRichTextEditor;
	
	
	public MeasureDetailsView(MeasureDetailsModel measureDetailsComponent, MeasureDetailsItems measureDetail, MeasureDetailsNavigation navigationPanel) {
		currentMeasureDetail = measureDetail;
		this.measureDetailsComponent = measureDetailsComponent;
		mainPanel.add(errorAlert);
		buildMeasureDetailsButtonPanel();

		mainContentPanel.add(navigationPanel.getWidget());
		mainContentPanel.setWidth("100%");
		widgetComponentPanel = buildDetailView(currentMeasureDetail);
		mainContentPanel.add(widgetComponentPanel);
		mainContentPanel.getElement().setId("measureDetailsView_ContentPanel");
		mainPanel.add(mainContentPanel);
		mainPanel.setStyleName("contentPanel");
	}

	private void buildHeading() {
		headingPanel.clear();
		HTML headingHTML = new HTML();
		headingHTML.setHTML("<h4><b>" + currentMeasureDetail.displayName() + "</b></h4>");
		headingHTML.getElement().setId("measureDetailsView_HeadingContent");
		headingHTML.setTitle(currentMeasureDetail.displayName());
		headingHTML.getElement().setTabIndex(0);
		headingPanel.add(headingHTML);
		headingPanel.getElement().setId("measureDetailsView_HeadingPanel");
		widgetComponentPanel.add(headingPanel);
		widgetComponentPanel.add(new SpacerWidget());
	}
	
	private void buildSavePanel(MatDetailItem currentMeasureDetail) {
		if(currentMeasureDetail != MeasureDetailsItems.POPULATIONS) {
			widgetComponentPanel.add(new SpacerWidget());
			saveButtonPanel.add(saveButton);
			saveButtonPanel.setWidth("100%");
			saveButton.setPull(Pull.RIGHT);
			saveButton.setMarginRight(30);
			widgetComponentPanel.add(saveButtonPanel);
		}
	}
	
	private void buildMeasureDetailsButtonPanel() {
		mainPanel.add(new SpacerWidget());
		HorizontalPanel panel = new HorizontalPanel();
		ButtonToolBar toolbar = new ButtonToolBar();	
		toolbar.add(deleteMeasureButton);
		panel.add(toolbar);
		mainPanel.add(panel);
		mainPanel.add(new SpacerWidget());
	}
	
	public VerticalPanel buildDetailView(MatDetailItem currentMeasureDetail) {
		this.currentMeasureDetail = currentMeasureDetail;
		widgetComponentPanel.clear();
		buildHeading();
		componentDetailView = MeasureDetailsViewFactory.get().getMeasureDetailComponentView(measureDetailsComponent, currentMeasureDetail);
		currentRichTextEditor = componentDetailView.getRichTextEditor();
		if(currentRichTextEditor != null) {
			currentRichTextEditor.addSummernoteKeyUpHandler(keyUpEvent -> handleRichTextTabOut(keyUpEvent));
		}
		widgetComponentPanel.add(componentDetailView.getWidget());
		widgetComponentPanel.setWidth("100%");
		widgetComponentPanel.setStyleName("marginLeft15px");
		widgetComponentPanel.getElement().setId("measureDetailsView_ComponentPanel");
		buildSavePanel(currentMeasureDetail);
		setReadOnly(isMeasureEditable);
		return widgetComponentPanel;
	}
	
	public void setFocusOnHeader() {
		DOM.getElementById("measureDetailsView_HeadingContent").focus();
	}
	
	private void handleRichTextTabOut(SummernoteKeyUpEvent keyUpEvent) {
		if(keyUpEvent.getNativeEvent().getCtrlKey() && keyUpEvent.getNativeEvent().getShiftKey() && keyUpEvent.getNativeEvent().getKeyCode() == 9) {
            keyUpEvent.getNativeEvent().preventDefault();
            DOM.getElementById("measureDetailsView_HeadingContent").focus();
        }
        else if(keyUpEvent.getNativeEvent().getCtrlKey() && keyUpEvent.getNativeEvent().getKeyCode() == 9) {
            keyUpEvent.getNativeEvent().preventDefault();
            saveButton.setFocus(true);
        }
	}
	
	public VerticalPanel buildDetailView(MeasureDetailsModel measureDetailsComponent, MatDetailItem currentMeasureDetail, MeasureDetailsNavigation navigationPanel) {
		this.currentMeasureDetail = currentMeasureDetail;
		this.measureDetailsComponent = measureDetailsComponent;
		return buildDetailView(currentMeasureDetail);
	}
	
	public Widget getWidget() {
		return mainPanel;
	}
	
	public Button getDeleteMeasureButton() {
		return this.deleteMeasureButton;
	}
	
	public boolean isValid() {
		return componentDetailView.isComplete();
	}
	
	public MessageAlert getErrorMessageAlert() {
		return errorAlert;
	}

	public void setReadOnly(boolean isReadOnly) {
		boolean enabled = !isReadOnly;
		saveButton.setEnabled(enabled);
		deleteMeasureButton.setEnabled(enabled);
		componentDetailView.setReadOnly(isReadOnly);
		isMeasureEditable = isReadOnly;
	}
	
	public MeasureDetailState getState() {
		return componentDetailView.getState();
	}
	
	public ConfirmationDialogBox getSaveConfirmation() {
		return componentDetailView.getSaveConfirmation();
	}
	
	public void resetForm() {
		componentDetailView.resetForm();
	}
	
	public MeasureDetailsComponentModel getMeasureDetailsComponentModel() {
		return componentDetailView.getMeasureDetailsComponentModel();
	}
	public SaveButton getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(SaveButton saveButton) {
		this.saveButton = saveButton;
	}
}