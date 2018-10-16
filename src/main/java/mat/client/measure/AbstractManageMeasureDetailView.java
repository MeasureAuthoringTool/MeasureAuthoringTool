package mat.client.measure;

import java.util.List;

import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.codelist.HasListBox;
import mat.client.cqlworkspace.EditConfirmationDialogBox;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MeasureNameLabel;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;
import mat.client.shared.TextAreaWithMaxLength;

public class AbstractManageMeasureDetailView implements DetailDisplay{
	protected SimplePanel mainPanel = new SimplePanel();
	protected MeasureNameLabel measureNameLabel = new MeasureNameLabel();
	protected TextAreaWithMaxLength name = new TextAreaWithMaxLength();
	protected TextBox shortName = new TextBox();
	protected MessageAlert errorMessages = new ErrorMessageAlert();
	protected ListBoxMVP  measureScoringInput = new ListBoxMVP();
	protected ListBoxMVP patientBasedInput = new ListBoxMVP();
	protected SaveContinueCancelButtonBar buttonBar = new SaveContinueCancelButtonBar("measureDetail");
	protected HelpBlock helpBlock = new HelpBlock();
	protected FormGroup messageFormGrp = new FormGroup();
    protected FormGroup measureNameGroup = new FormGroup();	
    protected FormGroup shortNameGroup = new FormGroup();
    protected FormGroup scoringGroup = new FormGroup();
    protected FormGroup patientBasedFormGrp = new FormGroup();
    protected HTML cautionMsgPlaceHolder = new HTML();
	protected HTML cautionPatientbasedMsgPlaceHolder = new HTML();
	EditConfirmationDialogBox confirmationDialogBox = new EditConfirmationDialogBox();
	protected String cautionPatientbasedMsgStr = "<div style=\"padding-left:5px;\">WARNING: Changing the Measure Scoring type will "
			+ "reset the Patient-based Measure to its default setting.<br/>";
    
	protected String cautionMsgStr = "<div style=\"padding-left:5px;\">WARNING: Changing the 'Measure Scoring' will have the following impacts:<br/>" +
			   "<img src='images/bullet.png'/> Populations in the Population Workspace that do not apply to the 'Measure Scoring' selected will be deleted.<br/>" +
            "<img src='images/bullet.png'/> Existing Groupings in the Measure Packager will be deleted.</div>";
	protected HTML instructions = new HTML("Enter a measure name and abbreviated name. Then continue to the Measure Composer.");
    protected HTML requiredInstructions = new HTML("All fields are required.");
	protected Label patientBasedMeasure = new Label();
	
	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public void clearFields() {
		name.setText("");
		shortName.setText("");
		measureScoringInput.setSelectedIndex(0);//default to --Select-- value.
		helpBlock.setText("");
		messageFormGrp.setValidationState(ValidationState.NONE);
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	@Override
	public ListBoxMVP getMeasScoringChoice() {
		return measureScoringInput;
	}

	@Override
	public String getMeasScoringValue() {
		return measureScoringInput.getItemText(measureScoringInput.getSelectedIndex());
	}

	@Override
	public HasValue<String> getMeasureVersion() {
		return null;
	}

	@Override
	public HasValue<String> getName() {
		return name;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	@Override
	public HasValue<String> getShortName() {
		return shortName;
	}

	@Override
	public void setMeasureName(String name) {
		measureNameLabel.setMeasureName(name);
	}

	@Override
	public void setScoringChoices(List<? extends HasListBox> texts) {
		setListBoxItems(measureScoringInput, texts, MatContext.PLEASE_SELECT);
	}

	@Override
	public void showCautionMsg(boolean show) {
		if(show){
			cautionMsgPlaceHolder.setHTML(cautionMsgStr);
			cautionPatientbasedMsgPlaceHolder.setHTML(cautionPatientbasedMsgStr);
		}else{
			cautionMsgPlaceHolder.setHTML("");
			cautionPatientbasedMsgPlaceHolder.setHTML("");
		}
	}

	@Override
	public void showMeasureName(boolean show) {
		MatContext.get().setVisible(measureNameLabel,show);	
	}

	@Override
	public ListBoxMVP getPatientBasedInput() {
		return patientBasedInput;
	}

	@Override
	public HelpBlock getHelpBlock() {
		return helpBlock;
	}

	@Override
	public FormGroup getMessageFormGrp() {
		return messageFormGrp;
	}

	@Override
	public void setPatientBasedInput(ListBoxMVP patientBasedInput) {
		this.patientBasedInput = patientBasedInput;
	}

	@Override
	public EditConfirmationDialogBox getConfirmationDialogBox() {
		return confirmationDialogBox;
	}

	protected void setListBoxItems(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption){
		listBox.clear();
		listBox.addItem(defaultOption,"");
		if(itemList != null){
			for(HasListBox listBoxContent : itemList){
				listBox.addItem(listBoxContent.getItem(), listBoxContent.getItem());
			}
		}
	}
	
	protected void buildMainPanel() {
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		mainPanel.getElement().setId("mainPanel_SimplePanel");
	}
	
	protected FlowPanel buildFlowPanel() {
		FlowPanel fPanel = new FlowPanel();
		fPanel.getElement().setId("fPanel_FlowPanel");
		fPanel.setWidth("90%");	
		fPanel.setHeight("100%");
		fPanel.add(measureNameLabel);
		
		fPanel.add(instructions);
		instructions.getElement().setId("instructions_HTML");
		fPanel.add(new SpacerWidget());
		fPanel.add(requiredInstructions);
		requiredInstructions.getElement().setId("requiredInstructions_HTML");
		fPanel.add(new SpacerWidget());
		fPanel.add(errorMessages);
		errorMessages.getElement().setId("errorMessages_ErrorMessageDisplay");
		
		return fPanel;
	}
	
	protected FormLabel buildMeasureNameLabel() {
		FormLabel measureNameLabel = new FormLabel();
		measureNameLabel.setText("Name");
		measureNameLabel.setTitle("Name");
		measureNameLabel.setFor("NameTextArea");
		measureNameLabel.setShowRequiredIndicator(true);
		measureNameLabel.setId("NameTextArea_Id");
		return measureNameLabel;
	}
	
	protected void buildNameTextArea() {
		name.setId("NameTextArea");
		name.setTitle("Enter Measure Name Required.");
		name.setWidth("400px");
		name.setHeight("50px");
		name.setMaxLength(500);
	}
	
	protected FormLabel buildShortNameLabel() {
		FormLabel shortNameLabel = new FormLabel();
		shortNameLabel.setText("eCQM Abbreviated Title");
		shortNameLabel.setTitle("eCQM Abbreviated Title");
		shortNameLabel.setFor("ShortNameTextBox");
		shortNameLabel.setShowRequiredIndicator(true);
		shortNameLabel.setId("ShortNameTextBox_Id");
		return shortNameLabel;
	}
	
	protected void buildShortNameTextBox() {
		shortName.setId("ShortNameTextBox");
		shortName.setTitle("Enter eCQM Abbreviated Title Required");
		shortName.setWidth("18em");
		shortName.setMaxLength(32);	
	}
	
	protected FormLabel buildScoringLabel() {
		FormLabel scoringLabel = new FormLabel();
		scoringLabel.setText("Measure Scoring");
		scoringLabel.setTitle("Measure Scoring");
		scoringLabel.setFor("MeasureScoringListBox");
		scoringLabel.setShowRequiredIndicator(true);
		scoringLabel.setId("MeasureScoringListBox");
		return scoringLabel;
	}
	
	protected void buildMeasureScoringInput() {
		measureScoringInput.getElement().setId("measScoringInput_ListBoxMVP");
		measureScoringInput.setTitle("Measure Scoring Required.");
		measureScoringInput.setStyleName("form-control");
		measureScoringInput.setVisibleItemCount(1);
		measureScoringInput.setWidth("18em");
	}
	
	protected HorizontalPanel buildScoringPanel() {
		HorizontalPanel scoringPanel = new HorizontalPanel();
		scoringPanel.add(measureScoringInput);
		scoringPanel.add(new HTML("&nbsp;"));
		scoringPanel.add(cautionMsgPlaceHolder);
		return scoringPanel;
	}
	
	protected FormLabel buildPatientBasedLabel() {
		FormLabel patientBasedLabel = new FormLabel();
		patientBasedLabel.setText("Patient-based Measure");
		patientBasedLabel.setTitle("Patient-based Measure");
		patientBasedLabel.setFor("PatientBasedMeasureListBox");
		patientBasedLabel.setShowRequiredIndicator(true);
		patientBasedLabel.setId("PatientBasedMeasureListBox_Id");
		return patientBasedLabel;
	}
	
	protected void buildPatientBasedInput() {
		patientBasedInput.getElement().setId("patientBasedMeasure_listbox");
		patientBasedInput.setTitle("Patient Based Indicator Required.");
		patientBasedInput.setStyleName("form-control");
		patientBasedInput.setVisibleItemCount(1);
		patientBasedInput.setWidth("18em");
	}
	
	protected HorizontalPanel buildPatientBasedPanel() {
		 HorizontalPanel patientBasedPanel = new HorizontalPanel();
		 patientBasedPanel.add(patientBasedInput);
		 patientBasedPanel.add(new HTML("&nbsp;"));
		 patientBasedPanel.add(cautionPatientbasedMsgPlaceHolder);
		 return patientBasedPanel;
	}
	
	protected FieldSet buildFormFieldSet() {
		FormGroup buttonFormGroup = new FormGroup();
		buttonFormGroup.add(buttonBar);
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(measureNameGroup);
		formFieldSet.add(shortNameGroup);
		formFieldSet.add(scoringGroup);
		formFieldSet.add(patientBasedFormGrp);
		formFieldSet.add(buttonFormGroup);
		return formFieldSet;
	}
}
