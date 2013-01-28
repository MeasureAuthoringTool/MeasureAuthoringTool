package mat.client.measure.metadata;

import java.util.ArrayList;
import java.util.List;

import mat.client.codelist.HasListBox;
import mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.TextAreaWithMaxLength;
import mat.model.Author;
import mat.model.MeasureType;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MetaDataView implements MetaDataDetailDisplay{
	
	protected FlowPanel mainPanel = new FlowPanel();
	protected FocusPanel focusPanel = new FocusPanel();
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	protected Label nameInput = new Label();
	protected Label abbrInput = new Label();
	protected Label measScoringInput = new Label();
	//protected TextBox measureIdInput = new TextBox();
	protected Label finalizedDate = new Label();
	protected TextAreaWithMaxLength rationaleInput = new TextAreaWithMaxLength();
	
	protected Label versionInput = new Label();
	protected ListBoxMVP authorInput = new ListBoxMVP();
	protected SimplePanel emptyAuthorsPanel = new SimplePanel();
	protected ListBox authorListBox =new ListBox();
	protected ListBox measureTypeListBox = new ListBox();
	protected ListBoxMVP measureStewardInput = new ListBoxMVP(false);

	//US 413. Added panel and input box for Steward Other option.
	protected VerticalPanel emptyTextBoxHolder = new VerticalPanel();
	protected TextBox measureStewardOtherInput = new TextBox();	
	protected TextAreaWithMaxLength  descriptionInput = new TextAreaWithMaxLength ();	
	protected TextAreaWithMaxLength  copyrightInput = new TextAreaWithMaxLength ();	
	protected TextAreaWithMaxLength  disclaimerInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  stratificationInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  riskAdjustmentInput  = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  rateAggregationInput  = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  initialPatientPopInput  = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  denominatorInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  denominatorExclusionsInput  = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  numeratorInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  numeratorExclusionsInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  denominatorExceptionsInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  measurePopulationInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  measureObservationsInput = new TextAreaWithMaxLength ();
	
	
	
	
	protected ListBoxMVP objectStatusInput = new ListBoxMVP();
	protected ListBoxMVP measureTypeInput = new ListBoxMVP();
	protected SimplePanel emptyMeasureTypePanel = new SimplePanel();
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	protected DateBoxWithCalendar measurePeriodFromInput = new DateBoxWithCalendar();
	protected DateBoxWithCalendar measurePeriodToInput = new DateBoxWithCalendar();
	protected TextAreaWithMaxLength  supplementalDataInput  = new TextAreaWithMaxLength ();
	protected TextBox codeSystemVersionInput = new TextBox();
	protected TextBox NQFIDInput = new TextBox();
	protected TextAreaWithMaxLength setNameInput = new TextAreaWithMaxLength();
	protected TextBox eMeasureIdentifierInput = new TextBox();
	protected Label eMeasureIdentifier  = new Label();
	protected Label endorsedByNQF = new Label();
	private int counter = 0;
	
	
	protected RadioButton No = new RadioButton("NQFgroup","No");
	protected RadioButton Yes = new RadioButton("NQFgroup","Yes");
	
	
	protected TextAreaWithMaxLength  clinicalStmtInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  improvementNotationInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  referenceInput = createReferenceInput();
	protected TextAreaWithMaxLength  definitionsInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength guidanceInput = new TextAreaWithMaxLength();
	protected TextAreaWithMaxLength transmissionFormatInput = new TextAreaWithMaxLength();	
	private Button addEditMeasureType = new PrimaryButton("Add/Edit Measure Type");
	private Button addEditAuthors = new PrimaryButton("Add/Edit Measure Developer(s)");
	
	private Button AddRowButton = new PrimaryButton("Add Reference");
	private Button saveButton = new PrimaryButton("Save");
	private Button generateeMeasureIDButton = new Button("Generate Identifier");
	
	private ArrayList<TextAreaWithMaxLength> referenceArrayList = new ArrayList<TextAreaWithMaxLength>(); 
	
	private SimplePanel referencePlaceHolder = new SimplePanel();
	
	private final FlexTable referenceTable = new FlexTable();
	
	private ErrorMessageDisplay saveErrorDisplay = new ErrorMessageDisplay();
	
	
	public MetaDataView(){
		//referenceArrayList = new ArrayList<TextAreaWithMaxLength>();
	
		HorizontalPanel mainContent = new HorizontalPanel();
		mainPanel.setStylePrimaryName("searchResultsContainer");
		mainPanel.addStyleName("leftAligned");
		
		mainContent.add(buildLeftSideForm());
		mainPanel.add(saveErrorDisplay);
		mainPanel.add(mainContent);
		mainPanel.setStyleName("contentPanel");
		DOM.setElementAttribute(mainPanel.getElement(), "id", "MetaDataView.containerPanel");
		
		focusPanel.add(mainPanel);
		
		
	}
	
	
	private Widget buildLeftSideForm(){
		ChangeHandler changeHandler = new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				clearErrorMsg();
				
			}
		};
		KeyDownHandler keyDownHandler = new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				clearErrorMsg();
				
			}
		};
		
		ClickHandler clickHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearErrorMsg();
				
			}
		};
		
		authorListBox.setVisibleItemCount(5);
		authorListBox.addChangeHandler(changeHandler);		
		
		measureTypeListBox.setVisibleItemCount(5);
		measureTypeListBox.addChangeHandler(changeHandler);
		
		FlowPanel fPanel = new FlowPanel();
		
		fPanel.setStyleName("leftSideForm");

		  
		
		fPanel.add(new Label("All fields are required except where noted as optional."));
		fPanel.add(new SpacerWidget());

		fPanel.add(errorMessages);
		
        fPanel.add(LabelBuilder.buildLabel(nameInput, "eMeasure Title"));
		fPanel.add(nameInput);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(abbrInput, "eMeasure Abbreviated Title"));
		fPanel.add(abbrInput);
		fPanel.add(new SpacerWidget());
		
		HorizontalFlowPanel horizontalPanel = new HorizontalFlowPanel();
		horizontalPanel.add(LabelBuilder.buildLabel(eMeasureIdentifierInput, "eMeasure Identifier (Measure Authoring Tool)"));
		Widget optionLabelWidget = LabelBuilder.buildLabel(eMeasureIdentifierInput, " - Optional");
		optionLabelWidget.setStyleName("generate-emeasureid-button");
		horizontalPanel.add(optionLabelWidget);
		fPanel.add(horizontalPanel);
				
		fPanel.add(eMeasureIdentifierInput);
		fPanel.add(generateeMeasureIDButton);
		generateeMeasureIDButton.addClickHandler(clickHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(finalizedDate, "Finalized Date"));
		fPanel.add(finalizedDate);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(eMeasureIdentifier, "GUID"));
		fPanel.add(eMeasureIdentifier);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(versionInput, "eMeasure Version Number"));
		fPanel.add(versionInput);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(objectStatusInput, "Measure Status"));
		fPanel.add(objectStatusInput);
		objectStatusInput.addChangeHandler(changeHandler);
		fPanel.add(new SpacerWidget());
				
		fPanel.add(LabelBuilder.buildLabel(NQFIDInput, "NQF Number"));
		fPanel.add(NQFIDInput);
		NQFIDInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		HorizontalPanel measurePeriodPanel = new HorizontalPanel();
	    measurePeriodPanel.add(new Label("From"));
	    measurePeriodPanel.add(measurePeriodFromInput);
	    measurePeriodPanel.add(new Label("To"));
	    measurePeriodPanel.add(measurePeriodToInput);
	    measurePeriodFromInput.getDateBox().addKeyDownHandler(keyDownHandler);
	    measurePeriodToInput.getDateBox().addKeyDownHandler(keyDownHandler);
	    measurePeriodFromInput.getCalendar().addClickHandler(clickHandler);
	    measurePeriodToInput.getCalendar().addClickHandler(clickHandler);
		fPanel.add(LabelBuilder.buildLabel(measurePeriodFromInput, "Measurement Period"));
		fPanel.add(measurePeriodPanel);
		fPanel.add(new SpacerWidget());
		
		//US 413. Included for measure steward other option.
		VerticalPanel verStewardPanel = new VerticalPanel();		
		verStewardPanel.add(LabelBuilder.buildLabel(measureStewardInput, "Measure Steward"));
		verStewardPanel.add(measureStewardInput);
		verStewardPanel.add(new SpacerWidget());
		verStewardPanel.add(emptyTextBoxHolder);
		measureStewardInput.addChangeHandler(changeHandler);
		fPanel.add(verStewardPanel);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(authorListBox, "Measure Developer"));
		fPanel.add(emptyAuthorsPanel);
		fPanel.add(addEditAuthors);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(endorsedByNQF, "Endorsed By NQF"));
		fPanel.add(wrapRadioButton(No));
		fPanel.add(wrapRadioButton(Yes));
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(descriptionInput, "Description"));
		fPanel.add(descriptionInput);
		descriptionInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(copyrightInput, "Copyright"));
		fPanel.add(copyrightInput);
		copyrightInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		//Disclaimer
		fPanel.add(LabelBuilder.buildLabel(disclaimerInput, "Disclaimer"));
		fPanel.add(disclaimerInput);
		disclaimerInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		//US 421. Measure Scoring choice is now part of Measure creation process. So just display here. 
		fPanel.add(LabelBuilder.buildLabel(measScoringInput, "Measure Scoring"));
		fPanel.add(measScoringInput);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(measureTypeListBox, "Measure Type"));
		fPanel.add(emptyMeasureTypePanel);
		fPanel.add(addEditMeasureType);
		fPanel.add(new SpacerWidget());

		fPanel.add(LabelBuilder.buildLabel(stratificationInput , "Stratification"));
		fPanel.add(stratificationInput );
		stratificationInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(riskAdjustmentInput, "Risk Adjustment"));
		fPanel.add(riskAdjustmentInput);
		riskAdjustmentInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		 
		//Rate Aggregation riskAggregationInput
		fPanel.add(LabelBuilder.buildLabel(rateAggregationInput, "Rate Aggregation"));
		fPanel.add(rateAggregationInput);
		rateAggregationInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		
		fPanel.add(LabelBuilder.buildLabel(rationaleInput, "Rationale"));
		fPanel.add(rationaleInput);
		rationaleInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(clinicalStmtInput, "Clinical Recommendation Statement"));
		fPanel.add(clinicalStmtInput);
		clinicalStmtInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(improvementNotationInput, "Improvement Notation"));
		fPanel.add(improvementNotationInput);
		improvementNotationInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		referenceInput.setSize("500px", "100px");
	    referenceInput.setMaxLength(2000);
	    referenceInput.addKeyDownHandler(keyDownHandler);
	    buildReferenceTable(referenceInput);	   
	    referencePlaceHolder.add(referenceTable);
	    fPanel.add(LabelBuilder.buildLabel(referencePlaceHolder, "Reference(s)"));
		fPanel.add(referencePlaceHolder);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(definitionsInput, "Definition"));
		fPanel.add(definitionsInput);
		definitionsInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
	
		fPanel.add(LabelBuilder.buildLabel(guidanceInput, "Guidance"));
		fPanel.add(guidanceInput);
		guidanceInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(transmissionFormatInput, "Transmission Format"));
		fPanel.add(transmissionFormatInput);
		transmissionFormatInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		
		fPanel.add(LabelBuilder.buildLabel(initialPatientPopInput, "Initial Patient Population"));
		fPanel.add(initialPatientPopInput);
		initialPatientPopInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(denominatorInput, "Denominator"));
		fPanel.add(denominatorInput);
		denominatorInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(denominatorExclusionsInput, "Denominator Exclusions"));
		fPanel.add(denominatorExclusionsInput);
		denominatorExclusionsInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(numeratorInput, "Numerator"));
		fPanel.add(numeratorInput);
		numeratorInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(numeratorExclusionsInput, "Numerator Exclusions"));
		fPanel.add(numeratorExclusionsInput);
		numeratorExclusionsInput.addKeyDownHandler(keyDownHandler);
		
		fPanel.add(LabelBuilder.buildLabel(denominatorExceptionsInput, "Denominator Exceptions"));
		fPanel.add(denominatorExceptionsInput);
		denominatorExceptionsInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(measurePopulationInput, "Measure Population"));
		fPanel.add(measurePopulationInput);
		measurePopulationInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(measureObservationsInput, "Measure Observations"));
		fPanel.add(measureObservationsInput);
		measureObservationsInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(supplementalDataInput, "Supplemental Data Elements"));
		fPanel.add(supplementalDataInput);
		supplementalDataInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(setNameInput, "Measure Set"));
		fPanel.add(setNameInput);
		setNameInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		
	    AddRowButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addRow(referenceTable);
				clearErrorMsg();
			}
	       });
	   
	    
	 	fPanel.add(errorMessages);
		fPanel.add(successMessages);
		saveButton.setTitle("Save     Ctrl+Alt+s");
		fPanel.add(saveButton);
		successMessages.setMessage("");
		fPanel.add(new SpacerWidget());
		codeSystemVersionInput.setMaxLength(255);
		rationaleInput.setMaxLength(15000);
		NQFIDInput.setMaxLength(64);
		descriptionInput.setSize("500px","100px");
		descriptionInput.setMaxLength(15000);
		copyrightInput.setSize("500px","100px");
		copyrightInput.setMaxLength(15000);
		disclaimerInput.setSize("500px","100px");
		disclaimerInput.setMaxLength(15000);
		rationaleInput.setMaxLength(15000);
		rationaleInput.setSize("500px", "100px");
		stratificationInput.setSize("500px", "100px");
		stratificationInput.setMaxLength(15000);
		riskAdjustmentInput.setSize("500px","100px");
		riskAdjustmentInput.setMaxLength(15000);
		rateAggregationInput.setSize("500px","100px");
		rateAggregationInput.setMaxLength(15000);
		
		setNameInput.setSize("500px","50px");
		setNameInput.setMaxLength(155);
		
		measureStewardOtherInput.setMaxLength(200);
		measureStewardOtherInput.setWidth("415px");
		clinicalStmtInput.setSize("500px","100px");
		clinicalStmtInput.setMaxLength(15000);
		improvementNotationInput.setSize("500px", "100px");
		improvementNotationInput.setMaxLength(15000);
		stratificationInput.setSize("500px", "100px");
		definitionsInput.setMaxLength(15000);
		definitionsInput.setSize("500px", "100px");
		guidanceInput.setMaxLength(15000);
		guidanceInput.setSize("500px", "100px");
		transmissionFormatInput.setMaxLength(15000);
		transmissionFormatInput.setSize("500px", "100px");
		supplementalDataInput.setMaxLength(15000);
		supplementalDataInput.setSize("500px", "100px");
		
		initialPatientPopInput.setSize("500px", "100px");
		initialPatientPopInput.setMaxLength(15000);
		denominatorInput.setSize("500px", "100px");
		denominatorInput.setMaxLength(15000);
		denominatorExclusionsInput.setSize("500px", "100px");
		denominatorExclusionsInput.setMaxLength(15000);
		numeratorInput.setSize("500px", "100px");
		numeratorInput.setMaxLength(15000);
		numeratorExclusionsInput.setSize("500px", "100px");
		numeratorExclusionsInput.setMaxLength(15000);
		denominatorExceptionsInput.setSize("500px", "100px");
		denominatorExceptionsInput.setMaxLength(15000);
		measurePopulationInput.setSize("500px", "100px");
		measurePopulationInput.setMaxLength(15000);
		measureObservationsInput.setSize("500px", "100px");
		measureObservationsInput.setMaxLength(15000);
		eMeasureIdentifierInput.setReadOnly(true);
		eMeasureIdentifierInput.setMaxLength(6);
		generateeMeasureIDButton.setEnabled(true);
		String emeasureIdMSG = "Once an eMeasure Identifier (Measure Authoring Tool) has been generated it may not be modified or removed for any draft or version of a measure.";
		generateeMeasureIDButton.setTitle(emeasureIdMSG);
		eMeasureIdentifierInput.setTitle(emeasureIdMSG);
		return fPanel;	
	}
	
	private Widget wrapRadioButton(RadioButton w) {
		SimplePanel p = new SimplePanel();
		p.add(w);	
		w.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				clearErrorMsg();
			}
		});
		return p;
	}
	
	
	@Override
	public Widget asWidget() {
		return focusPanel;
		//return mainPanel;
	}

	@Override
	public Label getMeasureName() {
		return nameInput;
	}

	@Override
	public Label getShortName() {
		return abbrInput;
	}

	/* Returns the Measure Scoring choice value.
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureScoring()
	 */
	@Override
	public Label getMeasureScoring(){
		return measScoringInput;
	}
	

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}


	@Override
	public HasClickHandlers getEditAuthorsButton() {
		return addEditAuthors;
	}

	@Override
	public HasKeyDownHandlers getFocusPanel(){
		return focusPanel;
	}
	
	

	@Override
	public HasClickHandlers getEditMeasureTypeButton() {
		return addEditMeasureType;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}
	
	
	private void setListBoxOptions(ListBox input, List<? extends HasListBox> itemList,String defaultText) {
		input.clear();
		if(defaultText != null) {
			input.addItem(defaultText, "");
		}
		if(itemList != null){
			for(HasListBox listBoxContent : itemList){
				input.addItem(listBoxContent.getItem(),"" +listBoxContent.getValue());
			}
		}
	}
	@Override
	public void setMeasureStewardOptions(List<? extends HasListBox> itemList) {
		setListBoxOptions(measureStewardInput, itemList, "--Select--");
	}

	@Override
	public Label getVersionNumber() {
		return versionInput;
	}


	/*@Override
	public HasValue<String> getMeasureId() {
		return measureIdInput;
	}*/


	@Override
	public HasValue<String> getSupplementalData() {
		return supplementalDataInput;
	}
	
	@Override
	public HasValue<String> getSetName() {
		return setNameInput;
	}


	
	@Override
	public Label geteMeasureIdentifier() {
		return eMeasureIdentifier;
	}

	@Override
	public HasValue<String> getNqfId(){
		return NQFIDInput;
	}
	
	@Override
	public Label getFinalizedDate() {
		return finalizedDate;
	}


	@Override
	public String getMeasureType() {
		return measureTypeInput.getValue(measureTypeInput.getSelectedIndex());
	}


	@Override
	public ListBoxMVP getMeasureSteward() {
		return measureStewardInput;
	}

	//US 413
	/* Returns the Steward Other text box object
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureStewardOther()
	 */
	@Override
	public TextBox getMeasureStewardOther() {
		return measureStewardOtherInput;
	}


	@Override
	public HasValue<Boolean> getEndorsebyNQF() {
		return Yes;
	}


	@Override
	public String getAuthor() {
		return authorInput.getValue(authorInput.getSelectedIndex());
	}


	@Override
	public HasValue<String> getDescription() {
		return descriptionInput;
	}
	
	@Override
	public HasValue<String> getCopyright() {
		return copyrightInput;
	}

	@Override
	public HasValue<String> getDisclaimer() {
		return disclaimerInput;
	}


	@Override
	public HasValue<String> getInitialPatientPop() {
		return initialPatientPopInput;
	}
	
	@Override
	public HasValue<String> getDenominator() {
		return denominatorInput;
	}
	@Override
	public HasValue<String> getDenominatorExclusions() {
		return denominatorExclusionsInput;
	}
	@Override
	public HasValue<String> getNumerator() {
		return numeratorInput;
	}
	@Override
	public HasValue<String> getNumeratorExclusions() {
		return numeratorExclusionsInput;
	}
	@Override
	public HasValue<String> getDenominatorExceptions() {
		return denominatorExceptionsInput;
	}
	@Override
	public HasValue<String> getMeasurePopulation() {
		return measurePopulationInput;
	}
	
	@Override
	public HasValue<String> getMeasureObservations() {
		return measureObservationsInput;
	}
	
	@Override
	public HasValue<String> getClinicalRecommendation() {
		return clinicalStmtInput;
	}


	@Override
	public HasValue<String> getDefinitions() {
		return definitionsInput;
	}


	@Override
	public HasValue<String> getGuidance() {
		return guidanceInput;
	}
	
	@Override
	public HasValue<String> getTransmissionFormat() {
		return transmissionFormatInput;
	}


	@Override
	public HasValue<String> getRationale() {
		return rationaleInput;
	}


	@Override
	public HasValue<String> getImprovementNotation() {
		return improvementNotationInput;
	}


	@Override
	public HasValue<String> getStratification() {
		return stratificationInput;
	}
	
	@Override
	public HasValue<String> getRiskAdjustment() {
		return riskAdjustmentInput;
	}

	@Override
	public HasValue<String> getRateAggregation() {
		return rateAggregationInput;
	}
	

	@Override
	public HasValue<String> getReference() {
		return referenceInput;
	}


	@Override
	public String getMeasurementFromPeriod() {
		return measurePeriodFromInput.getValue();
	}


	@Override
	public String getMeasurementToPeriod() {
		return measurePeriodToInput.getValue();
	}


	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	
	@Override
	public void setAuthorsList(List<Author> authorList){
		emptyAuthorsPanel.clear();
		authorListBox.clear();
		for(Author author: authorList){
			authorListBox.addItem(author.getAuthorName());
		}
		emptyAuthorsPanel.add(authorListBox);
	}
	
	@Override
	public void setMeasureTypeList(List<MeasureType> measureType){
		emptyMeasureTypePanel.clear();
		measureTypeListBox.clear();
		for(MeasureType mt:measureType){
			measureTypeListBox.addItem(mt.getDescription());
		}
		emptyMeasureTypePanel.add(measureTypeListBox);
	}

	private TextAreaWithMaxLength createReferenceInput() {
		TextAreaWithMaxLength newReferenceBox = new TextAreaWithMaxLength();
		DOM.setElementAttribute(newReferenceBox.getElement(), "id", "Reference");
		newReferenceBox.setSize("500px", "100px");
		newReferenceBox.setMaxLength(2000);
		newReferenceBox.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				clearErrorMsg();
				
			}
		});
		return newReferenceBox;   
	}
	
	
	
	private void  addRow(FlexTable reference){
		   TextAreaWithMaxLength newReferenceBox = createReferenceInput();
		   ++counter;
		   String dynamicLabel = "Reference"+counter;
		   Widget newReferenceBoxLabel = LabelBuilder.buildInvisibleLabel(newReferenceBox, dynamicLabel);
		   HorizontalPanel hp = new HorizontalPanel();
		   hp.add(newReferenceBoxLabel);
		   hp.add(newReferenceBox);
		   Button newremoveButton = new PrimaryButton("Remove");
		   newremoveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearErrorMsg();
				Cell cell = referenceTable.getCellForEvent(event); 
                int clickedRowIndex = cell.getRowIndex();
				removeRow(referenceTable,clickedRowIndex);
				
			}
		   });
		   int numRows = referenceTable.getRowCount();
		   referenceTable.setWidget(numRows, 0, hp);
		   referenceTable.setWidget(numRows, 1,newremoveButton);
		   referenceTable.getFlexCellFormatter().setRowSpan(0, 1, numRows + 1);
		   referenceArrayList.add(newReferenceBox);
	}
	
	private void removeRow(FlexTable reference,int rowIndex){
		  int numRows = reference.getRowCount();
		  referenceArrayList.remove(referenceTable.getWidget(rowIndex, 0));
		  referenceTable.removeRow(rowIndex);
		  referenceTable.getFlexCellFormatter().setRowSpan(0, 1, numRows - 1);
		   
	}


	@Override
	public List<String> getReferenceValues() {
		ArrayList<String> referenceValues = new ArrayList<String>();
		for(TextAreaWithMaxLength referenceBox: referenceArrayList){
			referenceValues.add(referenceBox.getValue());
		}
		return referenceValues;
		
	}


	@Override
	public void setReferenceValues(List<String> values, boolean editable) {
		if(values != null && !values.isEmpty()){
			clearReferences();
			for(int i=0;i<values.size();i++){
				TextAreaWithMaxLength newReferenceBox = createReferenceInput();
				newReferenceBox.setValue(values.get(i));
				newReferenceBox.setEnabled(editable);
				if(i == 0){
					referenceTable.setWidget(0, 0,newReferenceBox);
				    referenceTable.setWidget(0, 1, new SimplePanel());
					referenceTable.setWidget(0, 2, AddRowButton);
				}else{
					referenceTable.setWidget(i, 0, newReferenceBox);
					if(editable) {
							Button newremoveButton = new PrimaryButton("Remove");
						newremoveButton.addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									Cell cell = referenceTable.getCellForEvent(event); 
					                int clickedRowIndex = cell.getRowIndex();
									removeRow(referenceTable,clickedRowIndex);
								}
							   });
						referenceTable.setWidget(i, 1, newremoveButton);
					}
				}
				referenceArrayList.add(newReferenceBox);
			}
			referencePlaceHolder.add(referenceTable);
		}else if(values.isEmpty()){
			clearReferences();
			TextAreaWithMaxLength newReferenceBox = createReferenceInput();
			referenceTable.setWidget(0, 0,newReferenceBox);
		    referenceTable.setWidget(0, 1, new SimplePanel());
			referenceTable.setWidget(0, 2, AddRowButton);
			referenceArrayList.add(newReferenceBox);
			referencePlaceHolder.add(referenceTable);
		}
	}

   private void buildReferenceTable(TextAreaWithMaxLength referenceInput){
	    clearReferences();
	    referenceTable.setWidget(0, 0,referenceInput);
	    referenceArrayList.add(referenceInput);
	    referenceTable.setWidget(0, 1, new SimplePanel());
	    referenceTable.setWidget(0, 2, AddRowButton);	    
   }
	
   
  private void clearReferences(){
	    referencePlaceHolder.clear();
		referenceTable.clear();
		referenceTable.removeAllRows();
		referenceArrayList.clear();
		FlexCellFormatter cellFormatter = referenceTable.getFlexCellFormatter();
		cellFormatter.setHorizontalAlignment(0, 1,HasHorizontalAlignment.ALIGN_LEFT);
  }


	@Override
	public DateBoxWithCalendar getMeasurementFromPeriodInputBox() {
		return measurePeriodFromInput;
	}


	@Override
	public DateBoxWithCalendar getMeasurementToPeriodInputBox() {
		return measurePeriodToInput;
	}

	//US 413
	/* Returns the text value of Measure Steward
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureStewardValue()
	 */
	@Override
	public String getMeasureStewardValue() {
		return measureStewardInput.getItemText(measureStewardInput.getSelectedIndex());
	}
	
	//US 413
	/* Returns the index value of Measure Steward listbox
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureStewardListBox()
	 */
	@Override
	public HasValue<String> getMeasureStewardListBox(){
		return measureStewardInput;
	}

	//US 413
	/* Returns the Measure Steward Other value.
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureStewardOtherValue()
	 */
	@Override
	public String getMeasureStewardOtherValue() {
		return measureStewardOtherInput.getValue();
	}
	
	@Override
	public HasValue<String> getEmeasureId(){
		// TODO Auto-generated method stub
		return eMeasureIdentifierInput;
	}
	
	@Override
	public void setGenerateEmeasureIdButtonEnabled(boolean b) {
		// TODO Auto-generated method stub
		generateeMeasureIDButton.setEnabled(b);
	}
	
	@Override
	public HasClickHandlers getGenerateEmeasureIdButton() {
		// TODO Auto-generated method stub
		return generateeMeasureIDButton;
	}
	//US 413
	/* Clears out the Steward Other panel and re-draw the Steward Other input components
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#showOtherTextBox()
	 */
	@Override
	public void showOtherTextBox() {
		clearOtherPanel();
		Widget otherSpecify = LabelBuilder.buildLabel(measureStewardOtherInput, "User Defined Steward");
		emptyTextBoxHolder.add(otherSpecify);		
		emptyTextBoxHolder.add(measureStewardOtherInput);		
	}

	//US 413
	/* Clears out the Steward Other panel by calling local method .
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#hideOtherTextBox()
	 */
	@Override
	public void hideOtherTextBox() {
		clearOtherPanel();		
	}
	
	//US 413
	/**
	 * Local method to clear out the Steward other panel.
	 */
	private void clearOtherPanel(){		
		measureStewardOtherInput.setValue(null);
		emptyTextBoxHolder.clear();
	}


	@Override
	public ListBoxMVP getMeasureStatus() {
		return objectStatusInput;
	}
	
	@Override
	public void setAddEditButtonsVisible(boolean b) {
		addEditAuthors.setEnabled(b);
		addEditMeasureType.setEnabled(b);
		measurePeriodFromInput.setEnableCSS(b);
		measurePeriodToInput.setEnableCSS(b);
		AddRowButton.setEnabled(b);
		
	}

	@Override
	public HasValue<Boolean> getNotEndorsebyNQF() {
		return No;
	}


	

	@Override
	public void setObjectStatusOptions(List<? extends HasListBox> texts) {
		setListBoxOptions(objectStatusInput, texts, MatContext.PLEASE_SELECT);
		
	}


	@Override
	public String getMeasureStatusValue() {
		return objectStatusInput.getItemText(objectStatusInput.getSelectedIndex());
	}


	@Override
	public void enableEndorseByRadioButtons(boolean b) {
		No.setEnabled(b);
		Yes.setEnabled(b);
	}


	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setSaveButtonVisible(boolean)
	 */
	@Override
	public void setSaveButtonEnabled(boolean b) {
		saveButton.setEnabled(b);
		
	}


	@Override
	public ErrorMessageDisplay getSaveErrorMsg() {
		// TODO Auto-generated method stub
		return saveErrorDisplay;
	}


	
	@Override
	public Button getSaveBtn() {
		return saveButton;
	}
	
	private void clearErrorMsg(){
			getSaveErrorMsg().clear();
	}
	
}
