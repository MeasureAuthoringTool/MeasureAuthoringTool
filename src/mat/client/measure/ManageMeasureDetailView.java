package mat.client.measure;

import java.util.List;

import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
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

import mat.client.clause.cqlworkspace.EditConfirmationDialogBox;
import mat.client.codelist.HasListBox;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MeasureNameLabel;
import mat.client.shared.MessageAlert;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.TextAreaWithMaxLength;

public class ManageMeasureDetailView implements DetailDisplay{
	
	private SimplePanel mainPanel = new SimplePanel();
	
	private MeasureNameLabel measureNameLabel = new MeasureNameLabel();
	
	private TextAreaWithMaxLength name = new TextAreaWithMaxLength();
	
	private TextBox shortName = new TextBox();
	
	private ListBoxMVP  measScoringInput = new ListBoxMVP();
	
	private ListBoxMVP patientBasedInput = new ListBoxMVP();
	
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("measureDetail");
	
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	EditConfirmationDialogBox confirmationDialogBox = new EditConfirmationDialogBox();
	
	protected HTML instructions = new HTML("Enter a measure name and abbreviated name. Then continue to the Measure Composer.");

	private String cautionMsgStr = "<div style=\"padding-left:5px;\">WARNING: Changing the 'Measure Scoring' will have the following impacts:<br/>" +
								   "<img src='images/bullet.png'/> Populations in the Population Workspace that do not apply to the 'Measure Scoring' selected will be deleted.<br/>" +
                                   "<img src='images/bullet.png'/> Existing Groupings in the Measure Packager will be deleted.</div>";

    protected HTML cautionMsgPlaceHolder = new HTML();
    
    protected HTML requiredInstructions = new HTML("All fields are required.");
    
    protected Label patientBasedMeasure = new Label();
    
    Label invisibleRadioYes; 
    
    Label invisibleRadioNo;
    
    private FormGroup measureNameGroup = new FormGroup();
	
	private FormGroup shortNameGroup = new FormGroup();
	
	private FormGroup scoringGroup = new FormGroup();
	
	private FormGroup messageFormGrp = new FormGroup();
	
	private FormGroup patientBasedFormGrp = new FormGroup();
	
	private HelpBlock helpBlock = new HelpBlock();

	private String cautionPatientbasedMsgStr = "<div style=\"padding-left:5px;\">WARNING: Changing the Measure Scoring type will "
			+ "reset the Patient-based Measure to its default setting.<br/>";

	protected HTML cautionPatientbasedMsgPlaceHolder = new HTML();

	public ManageMeasureDetailView() {
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		mainPanel.getElement().setId("mainPanel_SimplePanel");

		FlowPanel fPanel = new FlowPanel();
		fPanel.getElement().setId("fPanel_FlowPanel");
		fPanel.setWidth("90%");	
		fPanel.setHeight("100%");
		fPanel.add(measureNameLabel);
		measureNameLabel.getElement().setId("measureNameLabel_MeasureNameLabel");
		fPanel.add(instructions);
		instructions.getElement().setId("instructions_HTML");
		fPanel.add(new SpacerWidget());
		fPanel.add(requiredInstructions);
		requiredInstructions.getElement().setId("requiredInstructions_HTML");
		fPanel.add(new SpacerWidget());
		fPanel.add(errorMessages);
		errorMessages.getElement().setId("errorMessages_ErrorMessageDisplay");
		
		Form createMeasureForm = new Form();
		FormLabel measureNameLabel = new FormLabel();
		measureNameLabel.setText("Name");
		measureNameLabel.setTitle("Name");
		measureNameLabel.setFor("NameTextArea");
		measureNameLabel.setShowRequiredIndicator(true);
		measureNameLabel.setId("NameTextArea_Id");
		name.setId("NameTextArea");
		
		name.setTitle("Enter Measure Name.");
		name.setWidth("400px");
		name.setHeight("50px");
		name.setMaxLength(500);
		measureNameGroup.add(measureNameLabel);
		measureNameGroup.add(name);
		
		FormLabel shortNameLabel = new FormLabel();
		shortNameLabel.setText("eCQM Abbreviated Title");
		shortNameLabel.setTitle("eCQM Abbreviated Title");
		shortNameLabel.setFor("ShortNameTextBox");
		shortNameLabel.setShowRequiredIndicator(true);
		shortNameLabel.setId("ShortNameTextBox_Id");
		shortName.setId("ShortNameTextBox");
		shortName.setTitle("Enter eCQM Abbreviated Title");;
		shortName.setWidth("18em");
		shortName.setMaxLength(32);
		shortNameGroup.add(shortNameLabel);
		shortNameGroup.add(shortName);
		
		FormLabel scoringLabel = new FormLabel();
		scoringLabel.setText("Measure Scoring");
		scoringLabel.setTitle("Measure Scoring");
		scoringLabel.setFor("MeasureScoringListBox");
		scoringLabel.setShowRequiredIndicator(true);
		scoringLabel.setId("MeasureScoringListBox");
		measScoringInput.getElement().setId("measScoringInput_ListBoxMVP");
		measScoringInput.setTitle("Measure Scoring.");
		measScoringInput.setStyleName("form-control");
		measScoringInput.setVisibleItemCount(1);
		measScoringInput.setWidth("18em");
		scoringGroup.add(scoringLabel);
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(measScoringInput);
		hp.add(new HTML("&nbsp;"));
		hp.add(cautionMsgPlaceHolder);
		cautionMsgPlaceHolder.getElement().setId("cautionMsgPlaceHolder_HTML");
		scoringGroup.add(hp);
		
		FormLabel patientBasedLabel = new FormLabel();
		patientBasedLabel.setText("Patient-based Measure");
		patientBasedLabel.setTitle("Patient-based Measure");
		patientBasedLabel.setFor("PatientBasedMeasureListBox");
		patientBasedLabel.setShowRequiredIndicator(true);
		patientBasedLabel.setId("PatientBasedMeasureListBox_Id");
		patientBasedInput.getElement().setId("patientBasedMeasure_listbox");
		patientBasedInput.setTitle("Patient Based Indicator.");
		patientBasedInput.setStyleName("form-control");
		patientBasedInput.setVisibleItemCount(1);
		patientBasedInput.setWidth("18em");
		patientBasedFormGrp.add(patientBasedLabel);
		HorizontalPanel msgPanel = new HorizontalPanel();
		msgPanel.add(patientBasedInput);
		msgPanel.add(new HTML("&nbsp;"));
		msgPanel.add(cautionPatientbasedMsgPlaceHolder);
		cautionPatientbasedMsgPlaceHolder.getElement().setId("cautionPatientbasedMsgPlaceHolder_HTML");
		patientBasedFormGrp.add(msgPanel);
		
		FormGroup buttonFormGroup = new FormGroup();
		buttonBar.getSaveButton().setText("Save and Continue");
		buttonBar.getSaveButton().setTitle("Save and Continue");
		buttonBar.getCancelButton().setTitle("Cancel");
		buttonBar.getElement().setId("buttonBar_SaveCancelButtonBar");
		buttonFormGroup.add(buttonBar);
		
		messageFormGrp.add(helpBlock);
		messageFormGrp.getElement().setAttribute("role", "alert");
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(measureNameGroup);
		formFieldSet.add(shortNameGroup);
		formFieldSet.add(scoringGroup);
		formFieldSet.add(patientBasedFormGrp);
		formFieldSet.add(buttonFormGroup);
		createMeasureForm.add(formFieldSet);
		createMeasureForm.add(messageFormGrp);
		fPanel.add(createMeasureForm);
		mainPanel.add(fPanel);
				
	}

	@Override
	public void setMeasureName(String name) {
		measureNameLabel.setMeasureName(name);
	}

	@Override
	public void showMeasureName(boolean show) {
		MatContext.get().setVisible(measureNameLabel,show);
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	@Override
	public HasValue<String> getName() {
		return name;
	}
	
	@Override
	public HasValue<String> getShortName() {
		return shortName;
	}		

	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public void clearFields() {
		name.setText("");
		shortName.setText("");
		measScoringInput.setSelectedIndex(0);//default to --Select-- value.
		helpBlock.setText("");
		messageFormGrp.setValidationState(ValidationState.NONE);
		
	}

	@Override
	public String getMeasScoringValue() {
		return measScoringInput.getItemText(measScoringInput.getSelectedIndex());
	}

	@Override
	public ListBoxMVP getMeasScoringChoice() {
		return measScoringInput;
	}

	@Override
	public void setScoringChoices(List<? extends HasListBox> texts) {
		setListBoxItems(measScoringInput, texts, MatContext.PLEASE_SELECT);
	}

	private void setListBoxItems(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption){
		listBox.clear();
		listBox.addItem(defaultOption,"");
		if(itemList != null){
			for(HasListBox listBoxContent : itemList){
				listBox.addItem(listBoxContent.getItem(), listBoxContent.getItem());
			}
		}
	}

	@Override
	public HasValue<String> getMeasureVersion() {
		return null;
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
	public ListBoxMVP getPatientBasedInput() {
		return patientBasedInput;
	}

	@Override
	public void setPatientBasedInput(ListBoxMVP patientBasedInput) {
		this.patientBasedInput = patientBasedInput;
	}

	@Override
	public HelpBlock getHelpBlock() {
		return helpBlock;
	}

	public void setHelpBlock(HelpBlock helpBlock) {
		this.helpBlock = helpBlock;
	}

	@Override
	public FormGroup getMessageFormGrp() {
		return messageFormGrp;
	}

	public EditConfirmationDialogBox getConfirmationDialogBox() {
		return confirmationDialogBox;
	}

}
