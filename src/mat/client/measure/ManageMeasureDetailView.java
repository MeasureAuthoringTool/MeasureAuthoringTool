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

// TODO: Auto-generated Javadoc
/**
 * The Class ManageMeasureDetailView.
 */
public class ManageMeasureDetailView 
	implements ManageMeasurePresenter.DetailDisplay{
	
	/** The main panel. */
	private SimplePanel mainPanel = new SimplePanel();
	
	/** The measure name label. */
	private MeasureNameLabel measureNameLabel = new MeasureNameLabel();
	
/*	*//** The name label. *//*
	private String nameLabel = "Name";
	
	*//** The short name label. *//*
	private String shortNameLabel = "Abbreviated Name";
	
	//US 421. Measure scoring choice is now part of measure creation process.
	*//** The scoring label. *//*
	private String scoringLabel = "Measure Scoring";
	*/
	/** The name. */
	private TextAreaWithMaxLength name = new TextAreaWithMaxLength();
	
	/** The short name. */
	private TextBox shortName = new TextBox();
	
	//US 421. Measure scoring choice is now part of measure creation process.	
	/** The meas scoring input. */
	private ListBoxMVP  measScoringInput = new ListBoxMVP();
	
	/** The patient based input. */
	private ListBoxMVP patientBasedInput = new ListBoxMVP();
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("measureDetail");
	
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/**  The edit confirmation box. */
	EditConfirmationDialogBox confirmationDialogBox = new EditConfirmationDialogBox();
	
	/** The yes patient based radio button. *//*
	private RadioButton radioBtnYes;
	
	*//** The no patient based radio button. *//*
	private RadioButton radioBtnNo;*/
	
	/** The instructions. */
	protected HTML instructions = new HTML("Enter a measure name and abbreviated name. Then continue to the Measure Composer.");
	//User Story # 1391 Sprint 11
	/** The caution msg str. */
	private String cautionMsgStr = "<div style=\"padding-left:5px;\">WARNING: Changing the 'Measure Scoring' will have the following impacts:<br/>" +
								   "<img src='images/bullet.png'/> Populations in the Population Workspace that do not apply to the 'Measure Scoring' selected will be deleted.<br/>" +
                                   "<img src='images/bullet.png'/> Existing Groupings in the Measure Packager will be deleted.</div>";
	
    /** The caution msg place holder. */
    protected HTML cautionMsgPlaceHolder = new HTML();
    
    /** The required instructions. */
    protected HTML requiredInstructions = new HTML("All fields are required.");
    
    /** The patient based measure. */
    protected Label patientBasedMeasure = new Label();
    
    /** The invisible radio yes. */
    Label invisibleRadioYes; 
    
    /** The invisible radio no. */
    Label invisibleRadioNo;
    
    /** The measure name group. */
    private FormGroup measureNameGroup = new FormGroup();
	
	/** The short name group. */
	private FormGroup shortNameGroup = new FormGroup();
	
	/** The scoring group. */
	private FormGroup scoringGroup = new FormGroup();
	
	/** The message form grp. */
	private FormGroup messageFormGrp = new FormGroup();
	
	/** The patient based form grp. */
	private FormGroup patientBasedFormGrp = new FormGroup();
	
	/** The help block. */
	private HelpBlock helpBlock = new HelpBlock();

	/** The caution patientbased msg str. */
	private String cautionPatientbasedMsgStr = "<div style=\"padding-left:5px;\">WARNING: Changing the Measure Scoring type will "
			+ "reset the Patient-based Measure to its default setting.<br/>";

	/** The caution patientbased msg place holder. */
	protected HTML cautionPatientbasedMsgPlaceHolder = new HTML();
	
	/**
	 * Instantiates a new manage measure detail view.
	 */
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
		
		/*messageFormGrp.add(helpBlock);
		messageFormGrp.getElement().setAttribute("role", "alert");
		createMeasureForm.add(messageFormGrp);*/
		
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

	/*@Override
	public FormGroup getMeasureNameGroup() {
		return measureNameGroup;
	}

	@Override	
	public FormGroup getShortNameGroup() {
		return shortNameGroup;
	}

	@Override
	public FormGroup getScoringGroup() {
		return scoringGroup;
	}*/

	/*@Override
	public FormGroup getMessageFormGrp() {
		return messageFormGrp;
	}*/

	/*@Override
	public FormGroup getPatientBasedFormGrp() {
		return patientBasedFormGrp;
	}*/

	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#setMeasureName(java.lang.String)
	 */
	@Override
	public void setMeasureName(String name) {
		measureNameLabel.setMeasureName(name);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#showMeasureName(boolean)
	 */
	@Override
	public void showMeasureName(boolean show) {
		MatContext.get().setVisible(measureNameLabel,show);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}



	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getName()
	 */
	@Override
	public HasValue<String> getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getShortName()
	 */
	@Override
	public HasValue<String> getShortName() {
		return shortName;
	}		

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#getErrorMessageDisplay()
	 */
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#clearFields()
	 */
	@Override
	public void clearFields() {
		name.setText("");
		shortName.setText("");
		measScoringInput.setSelectedIndex(0);//default to --Select-- value.
		helpBlock.setText("");
		messageFormGrp.setValidationState(ValidationState.NONE);
		
	}
	
	/* Returns the text value of Measure Scoring choice.
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getMeasScoringValue()
	 */
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getMeasScoringValue()
	 */
	@Override
	public String getMeasScoringValue() {
		return measScoringInput.getItemText(measScoringInput.getSelectedIndex());
	}
	
	/* Returns the Measure Scoring ListBox object.
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getMeasScoringChoice()
	 */
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getMeasScoringChoice()
	 */
	@Override
	public ListBoxMVP getMeasScoringChoice() {
		return measScoringInput;
	}
	
	//US 421
	/* Set the value list for Measure Scoring choice list box.
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#setScoringChoices(java.util.List)
	 */
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#setScoringChoices(java.util.List)
	 */
	@Override
	public void setScoringChoices(List<? extends HasListBox> texts) {
		setListBoxItems(measScoringInput, texts, MatContext.PLEASE_SELECT);
	}

	/**
	 * Populates the list box with collection values.
	 * 
	 * @param listBox
	 *            the list box
	 * @param itemList
	 *            the item list
	 * @param defaultOption
	 *            the default option
	 */
	private void setListBoxItems(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption){
		listBox.clear();
		listBox.addItem(defaultOption,"");
		if(itemList != null){
			for(HasListBox listBoxContent : itemList){
				listBox.addItem(listBoxContent.getItem(), listBoxContent.getItem());
			}
		}
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getMeasureVersion()
	 */
	@Override
	public HasValue<String> getMeasureVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	//US 195
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#showCautionMsg(boolean)
	 */
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


	/**
	 * Gets the yes patient based radio button.
	 *
	 * @return the invisible radio alert yes
	 */
	/*@Override
	public RadioButton getPatientBasedYesRadioButton() {
		return radioBtnYes;
	}


	*//**
	 * Gets the no patient based radio button
	 *//*
	@Override
	public RadioButton getPatientBasedNoRadioButton() {
		return radioBtnNo;
	}*/
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getPatientBasedInput()
	 */
	@Override
	public ListBoxMVP getPatientBasedInput() {
		return patientBasedInput;
	}


	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#setPatientBasedInput(com.google.gwt.user.client.ui.ListBox)
	 */
	@Override
	public void setPatientBasedInput(ListBoxMVP patientBasedInput) {
		this.patientBasedInput = patientBasedInput;
	}

	/* (non-Javadoc)
	 *  * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getHelpBlock()
	 */
	@Override
	public HelpBlock getHelpBlock() {
		return helpBlock;
	}

	/**
	 * Sets the help block.
	 *
	 * @param helpBlock the new help block
	 */
	public void setHelpBlock(HelpBlock helpBlock) {
		this.helpBlock = helpBlock;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getMessageFormGrp()
	 */
	@Override
	public FormGroup getMessageFormGrp() {
		return messageFormGrp;
	}

	/**
	 * @return the createNewConfirmationDialogBox
	 */
	public EditConfirmationDialogBox getConfirmationDialogBox() {
		return confirmationDialogBox;
	}

}
