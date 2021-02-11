package mat.client.measure.measuredetails;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import mat.client.buttons.CancelButton;
import mat.client.buttons.DeleteButton;
import mat.client.buttons.SaveButton;
import mat.client.measure.measuredetails.navigation.MeasureDetailsNavigation;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.MeasureDetailsViewFactory;
import mat.client.measure.measuredetails.views.ReferencesView;
import mat.client.shared.*;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsModel;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import java.util.List;

public class MeasureDetailsView {
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel mainContentPanel = new HorizontalPanel();
	private Panel headingPanel = new VerticalPanel();
	private HorizontalPanel saveButtonPanel = new HorizontalPanel();
	private VerticalPanel widgetComponentPanel = new VerticalPanel();
	private ErrorMessageAlert errorAlert = new ErrorMessageAlert();
	private MatDetailItem currentMeasureDetail;
	private MeasureDetailViewInterface componentDetailView;
	private boolean isMeasureEditable;
	private SaveButton saveButton = new SaveButton("Measure Details");
	private CancelButton cancelButton = new CancelButton("MeasureDetails");
	private DeleteButton deleteMeasureButton = new DeleteButton("Measure Details", "Delete Measure");
	private Button viewHumanReadableButton;
	private MeasureDetailsModel measureDetailsModel;
	private MessagePanel messagePanel;
	private MeasureDetailsObserver measureDetailsObserver;
	private HTML headingHTML;
	
	public MeasureDetailsView(MeasureDetailsModel measureDetailsModel, MeasureDetailsItems measureDetail, MeasureDetailsNavigation navigationPanel, MeasureDetailsObserver measureDetailsObserver) {
		currentMeasureDetail = measureDetail;
		this.measureDetailsModel = measureDetailsModel;
		
		HorizontalPanel errorPanel = new HorizontalPanel();
		errorPanel.add(errorAlert);
		mainPanel.add(errorPanel);
		buildMeasureDetailsButtonPanel();

		mainContentPanel.add(navigationPanel.getWidget());
		mainContentPanel.setWidth("850px");
		widgetComponentPanel = buildDetailView(currentMeasureDetail, measureDetailsObserver);
		mainContentPanel.add(widgetComponentPanel);
		mainContentPanel.getElement().setId("measureDetailsView_ContentPanel");
		mainPanel.add(mainContentPanel);
		mainPanel.setStyleName("contentPanel");
	}

	private void buildHeading() {
		headingPanel.clear();

		headingHTML = new HTML();
		headingHTML.setHTML("<h4><b>" + currentMeasureDetail.displayName() + "</b></h4>");
		headingHTML.getElement().setId("measureDetailsView_HeadingContent");
		headingHTML.setTitle(currentMeasureDetail.displayName());
		headingHTML.getElement().setTabIndex(-1);

		headingPanel.add(headingHTML);
		headingPanel.getElement().setId("measureDetailsView_HeadingPanel");

		if (componentDetailView.hasAllRequiredFields()) {
			headingPanel.add(new Label("All fields are required."));
		} else if (componentDetailView.hasSomeRequiredFields()) {
			headingPanel.add(new Label("* Indicates a required field."));
		}

		widgetComponentPanel.add(headingPanel);
		messagePanel = new MessagePanel();
		messagePanel.setWidth("625px");
		widgetComponentPanel.add(messagePanel);
		widgetComponentPanel.add(new SpacerWidget());
	}
	
	private void buildSavePanel(MatDetailItem currentMeasureDetail) {
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		if(currentMeasureDetail == MeasureDetailsItems.REFERENCES) {
			buttonToolBar.add(cancelButton);
		}
		if(currentMeasureDetail != MeasureDetailsItems.POPULATIONS && currentMeasureDetail != MeasureDetailsItems.COMPONENT_MEASURES) {
			widgetComponentPanel.add(new SpacerWidget());
			saveButtonPanel.clear();
			saveButtonPanel.setWidth("625px");
			saveButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			saveButton.setPull(Pull.RIGHT);
			buttonToolBar.add(saveButton);
			
			saveButtonPanel.add(buttonToolBar);
			widgetComponentPanel.add(saveButtonPanel);
		}
	}

	private void buildMeasureDetailsButtonPanel() {
		deleteMeasureButton.getElement().setId("MeasureDetailsView.deleteMeasureButton");
		mainPanel.add(new SpacerWidget());
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("100%");
		ButtonToolBar toolbar = new ButtonToolBar();
		toolbar.add(deleteMeasureButton);
		viewHumanReadableButton = buildHumanReadableButton();
		toolbar.add(viewHumanReadableButton);
		panel.add(toolbar);
		mainPanel.add(panel);
		mainPanel.add(new SpacerWidget());
	}
	
	private Button buildHumanReadableButton() {
		viewHumanReadableButton = new Button("View Human Readable");
		viewHumanReadableButton.setTitle("View Human Readable");
		viewHumanReadableButton.getElement().setAttribute("id", "view_human_readable_button");
		viewHumanReadableButton.setType(ButtonType.PRIMARY);
		viewHumanReadableButton.setPull(Pull.RIGHT);
		viewHumanReadableButton.addClickHandler(event -> generateHumanReadableForMeasureDetails());
		return viewHumanReadableButton;
	}

	private void generateHumanReadableForMeasureDetails() {
		messagePanel.clearAlerts();
		MatContext.get().getMeasureService().getHumanReadableForMeasureDetails(MatContext.get().getCurrentMeasureId(), MatContext.get().getCurrentMeasureModel(), new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				showHumanReadableDialogBox(result, measureDetailsModel.getGeneralInformationModel().getMeasureName());
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
	}

	public static native void showHumanReadableDialogBox(String result, String measureName) /*-{
		var dummyURL = window.location.protocol + "//"
			+ window.location.hostname + ":" + window.location.port + "/"
			+ "Mat.html";
		var humanReadableWindow = window.open(dummyURL, "",
			"width=1200,height=700,scrollbars=yes,resizable=yes");

		if (humanReadableWindow && humanReadableWindow.top) {
			humanReadableWindow.document.write(result);
			humanReadableWindow.document.title = measureName;
		}
	}-*/;

	public VerticalPanel buildDetailView(MatDetailItem currentMeasureDetail, MeasureDetailsObserver measureDetailsObserver) {
		this.currentMeasureDetail = currentMeasureDetail;
		widgetComponentPanel.clear();
		componentDetailView = MeasureDetailsViewFactory.get().getMeasureDetailComponentView(measureDetailsModel, currentMeasureDetail, this.measureDetailsObserver, messagePanel);

		buildHeading();
		

		widgetComponentPanel.add(componentDetailView.getWidget());
		widgetComponentPanel.setWidth("100%");
		widgetComponentPanel.setStyleName("marginLeft15px");
		widgetComponentPanel.getElement().setId("measureDetailsView_ComponentPanel");
		buildSavePanel(currentMeasureDetail);
		setReadOnly(isMeasureEditable);
		return widgetComponentPanel;
	}
	
	public void setFocusOnFirstElement() {
		if(componentDetailView.getFirstElement() != null) {
			componentDetailView.getFirstElement().getElement().focus();
		}
	}
	
	public VerticalPanel buildDetailView(MeasureDetailsModel measureDetailsModel, MatDetailItem currentMeasureDetail, MeasureDetailsNavigation navigationPanel, MeasureDetailsObserver measureDetailsObserver) {
		this.currentMeasureDetail = currentMeasureDetail;
		this.measureDetailsModel = measureDetailsModel;
		return buildDetailView(currentMeasureDetail, measureDetailsObserver);
	}
	
	public Widget getWidget() {
		return mainPanel;
	}
	
	public Button getDeleteMeasureButton() {
		return this.deleteMeasureButton;
	}
	
	public MessageAlert getErrorMessageAlert() {
		return errorAlert;
	}

	public void setReadOnly(boolean isReadOnly) {
		boolean enabled = !isReadOnly;
		saveButton.setEnabled(enabled);
		cancelButton.setEnabled(enabled);
		deleteMeasureButton.setEnabled(enabled);
		componentDetailView.setReadOnly(isReadOnly);
		isMeasureEditable = isReadOnly;
	}
	
	public void clear() {
		componentDetailView.clear();
		messagePanel.clearAlerts();
		errorAlert.clearAlert();
	}
	
	public void clearAlerts() {
		messagePanel.clearAlerts();
		if(currentMeasureDetail == MeasureDetailsItems.REFERENCES) {
			ReferencesView referencesView = (ReferencesView) getComponentDetailView();
			referencesView.hideDirtyCheck();
		}
	}
	
	public ConfirmationDialogBox getSaveConfirmation() {
		return componentDetailView.getSaveConfirmation();
	}
	
	public void resetForm() {
		messagePanel.clearAlerts();
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
	
	public CancelButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(CancelButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public void displayErrorMessage(List<String> messages) {
		messagePanel.clearAlerts();
		messagePanel.getErrorMessageAlert().createAlert(messages);
	}
	
	public void displayErrorMessage(String message) {
		messagePanel.clearAlerts();
		messagePanel.getErrorMessageAlert().createAlert(message);
	}
	
	public void displaySuccessMessage(String message) {
		messagePanel.clearAlerts();
		messagePanel.getSuccessMessageAlert().createAlert(message);
	}
	
	public void displayWarning(String message) {
		messagePanel.clearAlerts();
		messagePanel.getWarningMessageAlert().createAlert(message);
	}
	
	public void displayDirtyCheck() {
		if(currentMeasureDetail == MeasureDetailsItems.REFERENCES) {
			ReferencesView referencesView = (ReferencesView) getComponentDetailView();
			referencesView.hideDirtyCheck();
		}
		messagePanel.clearAlerts();
		messagePanel.getWarningConfirmationMessageAlert().createWarningAlert();
	}
	
	public MatDetailItem getCurrentMeasureDetail() {
		return currentMeasureDetail;
	}

	public void setCurrentMeasureDetail(MatDetailItem currentMeasureDetail) {
		this.currentMeasureDetail = currentMeasureDetail;
	}	
	
	public MessagePanel getMessagePanel() {
		return messagePanel;
	}
	
	public MeasureDetailViewInterface getComponentDetailView() {
		return componentDetailView;
	}

	public MeasureDetailsObserver getMeasureDetailsObserver() {
		return measureDetailsObserver;
	}

	public void setMeasureDetailsObserver(MeasureDetailsObserver measureDetailsObserver) {
		this.measureDetailsObserver = measureDetailsObserver;
	}
}