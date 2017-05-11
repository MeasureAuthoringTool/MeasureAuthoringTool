package mat.client.measure;

import java.util.List;


import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.codelist.HasListBox;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
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
	
	/** The name label. */
	private String nameLabel = "Name";
	
	/** The short name label. */
	private String shortNameLabel = "Abbreviated Name";
	
	//US 421. Measure scoring choice is now part of measure creation process.
	/** The scoring label. */
	private String scoringLabel = "Measure Scoring";
	
	/** The name. */
	private TextAreaWithMaxLength name = new TextAreaWithMaxLength();
	
	/** The short name. */
	private TextBox shortName = new TextBox();
	
	//US 421. Measure scoring choice is now part of measure creation process.	
	/** The meas scoring input. */
	private ListBoxMVP  measScoringInput = new ListBoxMVP();
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("measureDetail");
	
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/**
	 * The yes patient based radio button
	 */
	private RadioButton radioBtnYes;
	
	/**
	 * The no patient based radio button
	 */
	private RadioButton radioBtnNo;
	
	/** The instructions. */
	protected HTML instructions = new HTML("Enter a measure name and abbreviated name. Then continue to the Measure Composer.");
	//User Story # 1391 Sprint 11
	/** The caution msg str. */
	private String cautionMsgStr = "<div style=\"padding-left:5px;\">WARNING: Changing the 'Measure Scoring' will have the following impacts:<br/>" +
								   "<img src='images/bullet.png'/> Populations in the Clause Workspace that do not apply to the 'Measure Scoring' selected will be deleted.<br/>" +
                                   "<img src='images/bullet.png'/> Existing Groupings in the Measure Packager will be deleted.</div>";
	
    /** The caution msg place holder. */
    protected HTML cautionMsgPlaceHolder = new HTML();
    
    /** The required instructions. */
    protected HTML requiredInstructions = new HTML("All fields are required.");
    
    /** The patient based measure. */
    protected Label patientBasedMeasure = new Label();
    
    Label invisibleRadioYes; 
    
    Label invisibleRadioNo;
	
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
		
		FlowPanel leftPanel = new FlowPanel();
		leftPanel.getElement().setId("leftPanel_FlowPanel");
		fPanel.add(leftPanel);
		
		leftPanel.add(LabelBuilder.buildLabel(name, nameLabel));
		leftPanel.add(name);
		name.getElement().setId("name_TextAreaWithMaxLength");
		leftPanel.add(new SpacerWidget());
		
		
		leftPanel.add(LabelBuilder.buildLabel(shortName, shortNameLabel));
		leftPanel.add(shortName);
		shortName.getElement().setId("shortName_TextBox");
		leftPanel.add(new SpacerWidget());
		//US 195 Adding Static Caution Message
		HorizontalPanel hp = new HorizontalPanel();
		leftPanel.getElement().setId("hp_HorizontalPanel");
		hp.add(measScoringInput);
		measScoringInput.getElement().setId("measScoringInput_ListBoxMVP");
		hp.add(new HTML("&nbsp;"));
		hp.add(cautionMsgPlaceHolder);
		cautionMsgPlaceHolder.getElement().setId("cautionMsgPlaceHolder_HTML");
		
		//US 421. Measure scoring choice is now part of measure creation process.
		leftPanel.add(LabelBuilder.buildLabel(measScoringInput, scoringLabel));
		measScoringInput.getElement().setId("measScoringInput_ListBoxMVP");
		leftPanel.add(hp);
		leftPanel.add(new SpacerWidget());
			
		leftPanel.add(buildPatientBasedMeasurePanel());

		SimplePanel buttonPanel = new SimplePanel();
		buttonPanel.getElement().setId("buttonPanel_SimplePanel");
		buttonBar.getSaveButton().setText("Save and Continue");
		buttonBar.getSaveButton().setTitle("Save and Continue");
		buttonBar.getCancelButton().setTitle("Cancel");
		buttonBar.getElement().setId("buttonBar_SaveCancelButtonBar");
		buttonPanel.add(buttonBar);
		buttonPanel.setWidth("100%");
		fPanel.add(buttonPanel);
		mainPanel.add(fPanel);
		
		shortName.setMaxLength(32);
		shortName.setWidth("192px");
		name.setMaxLength(500);
		name.setWidth("400px");
	}

	
	/**
	 * Builds the patient based measure panel.
	 *
	 * @return the vertical panel
	 */
	public VerticalPanel buildPatientBasedMeasurePanel(){
		VerticalPanel patientBasedMeasurePanel = new VerticalPanel();
		patientBasedMeasurePanel.getElement().setId("patientBasedMeasure_VerticalPanel");
		HorizontalPanel radionButtonPanel = new HorizontalPanel();
		radionButtonPanel.getElement().setId("patientBasedMeasure_HorizontalPanel");
		Label patientBasedMeasureLabel = (Label) LabelBuilder.buildLabel(patientBasedMeasure, "Patient Based Measure");
		patientBasedMeasurePanel.add(patientBasedMeasureLabel);
		
		invisibleRadioYes = (Label) LabelBuilder.buildInvisibleLabel("PatientBasedIndicatorchangedtoYes", "PatientBasedIndicatorchangedtoYes.");
		invisibleRadioNo = (Label) LabelBuilder.buildInvisibleLabel("PatientBasedIndicatorchangedtoNo", "PatientBasedIndicatorchangedtoNo.");
		
		patientBasedMeasure.getElement().setId("patientBasedMeasure_Label");
		radioBtnYes = new RadioButton("patientBasedGroup", "Yes");
		radioBtnNo = new RadioButton("patientBasedGroup", "No");
		radioBtnYes.getElement().setId("RadioBtn_Yes");
		radioBtnNo.getElement().setId("RadioBtn_No");
		
		radioBtnYes.getElement().setAttribute("aria-describedby", "PatientBasedIndicatorchangedtoYes");
		radioBtnNo.getElement().setAttribute("aria-describedby", "PatientBasedIndicatorchangedtoNo");
		
		// Check 'No' by default.
		radioBtnNo.setValue(true);
		radionButtonPanel.add(radioBtnNo);
		radionButtonPanel.add(radioBtnYes);
		radionButtonPanel.setStyleName("myAccountButtonLayout");
		patientBasedMeasurePanel.add(radionButtonPanel);
		patientBasedMeasurePanel.add(invisibleRadioNo);
		patientBasedMeasurePanel.add(invisibleRadioYes);
		patientBasedMeasurePanel.add(new SpacerWidget());
		return patientBasedMeasurePanel;
	}
	
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
				listBox.addItem(listBoxContent.getItem(),"" +listBoxContent.getValue());
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
		}else{
			cautionMsgPlaceHolder.setHTML("");
		}
	}


	/**
	 * Gets the yes patient based radio button
	 */
	@Override
	public RadioButton getPatientBasedYesRadioButton() {
		return radioBtnYes;
	}


	/**
	 * Gets the no patient based radio button
	 */
	@Override
	public RadioButton getPatientBasedNoRadioButton() {
		// TODO Auto-generated method stub
		return radioBtnNo;
	}
	
	@Override
	public Label getInvisibleRadioAlertYes() {
		return invisibleRadioYes;
	}
	
	@Override
	public Label getInvisibleRadioAlertNo() {
		return invisibleRadioNo;
	}
}
