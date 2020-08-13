package mat.client.measure;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.MessagePanel;
import mat.client.shared.SpacerWidget;

public class ManageMeasureVersionView implements VersionDisplay {
	
	private ManageMeasureSearchModel.Result selectedMeasure;

	private SaveContinueCancelButtonBar buttonBar = new SaveContinueCancelButtonBar("measuVersion");

	private VerticalPanel cellTablePanel = new VerticalPanel();

	private MessagePanel messagePanel = new MessagePanel();
	
	private MessageAlert errorMessages = new ErrorMessageAlert();

	private FlowPanel mainPanel = new FlowPanel();

	private RadioButton majorRadio = new RadioButton("group", "Major");

	private RadioButton minorRadio = new RadioButton("group", "Minor");

	public ManageMeasureVersionView() {
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(messagePanel);
		messagePanel.getElement().setId("errorMessages_ErrorMessageDisplay");
		
		VerticalPanel radioPanel = new VerticalPanel();
		radioPanel.getElement().getStyle().setMarginLeft(5, Unit.PX);
		Label radioLabel = new Label("Select Version Type");
		radioLabel.setTitle("Select Version Type Required");
		radioLabel.getElement().setTabIndex(0);
		radioPanel.add(radioLabel);
		radioPanel.add(new SpacerWidget());
		radioPanel.add(majorRadio);
		majorRadio.getElement().setId("majorRadio_RadioButton");
		radioPanel.add(minorRadio);
		minorRadio.getElement().setId("minorRadio_RadioButton");
		mainPanel.add(cellTablePanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(radioPanel);
		mainPanel.add(new SpacerWidget());
		
		SimplePanel buttonPanel = new SimplePanel();
		buttonBar.getSaveButton().setText("Package and Version");
		buttonBar.getSaveButton().setTitle("Package and version");
		buttonBar.getCancelButton().setTitle("Cancel");
		buttonPanel.add(buttonBar);
		buttonPanel.setWidth("100%");
		mainPanel.add(buttonPanel);
		
	}
	
	public void buildHTML(){
		cellTablePanel.clear();
		if(selectedMeasure != null){
			String selectedItemName = selectedMeasure.getName();
			String selectedItemDraftText = selectedMeasure.getVersion();
			StringBuilder paragraph = new StringBuilder("<p>You are packaging and creating a version of <b>\""+ selectedItemName + " " + selectedItemDraftText +"\"</b>");
			paragraph.append("</p>");
			HTML paragraphHtml = new HTML(paragraph.toString());
			cellTablePanel.add(paragraphHtml);
		}
	}

	@Override
	public Widget asWidget() {
		buildHTML();
		return mainPanel;
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public RadioButton getMajorRadioButton() {
		return majorRadio;
	}

	@Override
	public RadioButton getMinorRadioButton() {
		return minorRadio;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	@Override
	public Result getSelectedMeasure() {
		return selectedMeasure;
	}
	@Override
	public void setSelectedMeasure(ManageMeasureSearchModel.Result selectedMeasure) {
		this.selectedMeasure = selectedMeasure;
	}
	
	@Override
	public MessagePanel getMessagePanel() {
		return messagePanel;
	}

	public void setMessagePanel(MessagePanel messagePanel) {
		this.messagePanel = messagePanel;
	}
}
