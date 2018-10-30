package mat.client.measure.metadata;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import mat.client.ImageResources;
import mat.client.buttons.SaveDeleteMeasureDetailsButtonBarBuilder;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MessageAlert;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.TextAreaWithMaxLength;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.util.CellTableUtility;
import mat.model.Author;
import mat.model.MeasureSteward;
import mat.model.MeasureType;
import mat.model.QualityDataSetDTO;
import mat.shared.MatConstants;

/**
 * The Class MetaDataView.
 */
public class MetaDataView implements MetaDataDetailDisplay{
	private ListBoxMVP stewardListBox = new ListBoxMVP();
	private ListBoxMVP endorsedByListBox = new ListBoxMVP();
	protected FlowPanel mainPanel = new FlowPanel();
	protected FocusPanel focusPanel = new FocusPanel();
	protected FlowPanel cellTablePanel=new  FlowPanel();
	private MessageAlert bottomSuccessMessage = new SuccessMessageAlert();
	private MessageAlert topSuccessMessage = new SuccessMessageAlert();
	protected TextBox abbrInput = new TextBox();
	protected TextBox patientBasedInput = new TextBox(); 
	protected TextBox measScoringInput = new TextBox();
	protected TextBox finalizedDate = new TextBox();
	protected TextAreaWithMaxLength rationaleInput = new TextAreaWithMaxLength();
	protected TextBox versionInput = new TextBox();
	protected ListBoxMVP authorInput = new ListBoxMVP();
	protected SimplePanel emptyAuthorsPanel = new SimplePanel();
	protected ListBox authorListBox =new ListBox();
	protected ListBox measureTypeListBox = new ListBox();
	protected VerticalPanel emptyTextBoxHolder = new VerticalPanel();
	protected VerticalPanel qdmItemCountListVPanel = new VerticalPanel();
	protected ScrollPanel componentMeasuresListSPanel = new ScrollPanel();
	protected ScrollPanel measureTypeSPanel = new ScrollPanel();
	protected ScrollPanel authorSPanel = new ScrollPanel();
	protected ScrollPanel stewardSPanel = new ScrollPanel();
	protected TextBox measureStewardOtherInput = new TextBox();
	protected TextAreaWithMaxLength  descriptionInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  copyrightInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  disclaimerInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  stratificationInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  riskAdjustmentInput  = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  rateAggregationInput  = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  initialPopInput  = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  denominatorInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  denominatorExclusionsInput  = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  numeratorInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  numeratorExclusionsInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  denominatorExceptionsInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  measurePopulationInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  measurePopulationExclusionsInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  measureObservationsInput = new TextAreaWithMaxLength ();
	protected ListBoxMVP measureTypeInput = new ListBoxMVP();
	protected SimplePanel emptyMeasureTypePanel = new SimplePanel();
	protected MessageAlert bottomErrorMessage = new ErrorMessageAlert();
	protected MessageAlert topErrorMessage = new ErrorMessageAlert();
	protected DateBoxWithCalendar measurePeriodFromInput = new DateBoxWithCalendar();
	protected DateBoxWithCalendar measurePeriodToInput = new DateBoxWithCalendar();
	protected TextAreaWithMaxLength  supplementalDataInput  = new TextAreaWithMaxLength ();
	protected TextBox codeSystemVersionInput = new TextBox();
	protected TextBox nQFIDInput = new TextBox();
	protected TextAreaWithMaxLength setNameInput = new TextAreaWithMaxLength();
	protected TextBox eMeasureIdentifierInput = new TextBox();
	protected TextBox eMeasureIdentifier  = new TextBox();
	protected FormLabel endorsedByNQF = new FormLabel();
	protected FormLabel componentMeasuresLabel = new FormLabel();
	protected TextAreaWithMaxLength  clinicalStmtInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  improvementNotationInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength  referenceInput = createReferenceInput();
	protected TextAreaWithMaxLength  definitionsInput = new TextAreaWithMaxLength ();
	protected TextAreaWithMaxLength guidanceInput = new TextAreaWithMaxLength();
	protected TextAreaWithMaxLength transmissionFormatInput = new TextAreaWithMaxLength();
	private Button AddRowButton = new Button("Add Reference");
	private Button generateeMeasureIDButton = new Button("Generate Identifier");
	private ArrayList<TextAreaWithMaxLength> referenceArrayList = new ArrayList<TextAreaWithMaxLength>();
	private SimplePanel referencePlaceHolder = new SimplePanel();
	private final FlexTable referenceTable = new FlexTable();
	private WarningConfirmationMessageAlert saveErrorDisplay = new WarningConfirmationMessageAlert();
	private MultiSelectionModel<ManageMeasureSearchModel.Result> measureSelectionModel;
	VerticalPanel qdmSelectedListVPanel=new VerticalPanel();
	private  List<QualityDataSetDTO> qdmSelectedList;
	private TextBox searchString = new TextBox();
	private List<ManageMeasureSearchModel.Result> componentMeasureSelectedList;
	private CellTable<ManageMeasureSearchModel.Result> componentMeasureCellTable;
	private CellTable<MeasureType> measureTypeCellTable;
	private CellTable<Author> authorCellTable;
	private List<ManageMeasureSearchModel.Result> selectedMeasureList;
	VerticalPanel componentMeasuresListPanel = new VerticalPanel();
	private MultiSelectionModel<ManageMeasureSearchModel.Result> measuresListSelectionModel;
	private MultiSelectionModel<MeasureType> measureTypeSelectioModel;
	private MultiSelectionModel<Author> authorSelectionModel;
	private PrimaryButton searchButton = new PrimaryButton("Go");
	private static DialogBox dialogBox = new DialogBox(true,true);
	private List<MeasureType> measureTypeSelectedList = new ArrayList<MeasureType>();
	private List<Author> authorsSelectedList = new ArrayList<Author>();
	private String stewardId;
	private String stewardValue;
	private CheckBox calenderYear = new CheckBox();	
	private String measureScoringType ;
	private boolean isPatientBasedMeasure;
	private FormGroup messageFormGrp = new FormGroup();
	private HelpBlock helpBlock = new HelpBlock();
	private SaveDeleteMeasureDetailsButtonBarBuilder buttonBarTop = 
			new SaveDeleteMeasureDetailsButtonBarBuilder("MeasureDetailsTop", "saveButton_Button1", "deleteMeasure_Button1");
	private SaveDeleteMeasureDetailsButtonBarBuilder buttonBarBottom = 
			new SaveDeleteMeasureDetailsButtonBarBuilder("MeasureDetailsBottom", "saveButton_Button" ,"deleteMeasure_Button");
	private VerticalPanel componentMeasurePanel;
	
	private static final String COMPOSITE = "COMPOSITE";
	
	public MetaDataView(){
		generateeMeasureIDButton.setType(ButtonType.PRIMARY);
		generateeMeasureIDButton.setMarginLeft(14.00);
		
		AddRowButton.setType(ButtonType.LINK);
		AddRowButton.setIcon(IconType.PLUS);
		AddRowButton.setTitle("Add More Reference");
		bottomSuccessMessage.setWidth("750px");
		bottomErrorMessage.setWidth("750px");
		topSuccessMessage.setWidth("900px");
		topErrorMessage.setWidth("900px");
		saveErrorDisplay.setWidth("900px");
		
		addClickHandlers();
		searchString.setHeight("20px");
		
		messageFormGrp.add(helpBlock);
		messageFormGrp.getElement().setAttribute("role", "alert");
		helpBlock.setColor("transparent");
		
		saveErrorDisplay.clearAlert();
		mainPanel.setStyleName("contentPanel");
		mainPanel.getElement().setAttribute("id", "MetaDataView.containerPanel");
		focusPanel.add(mainPanel);
		focusPanel.getElement().setId("focusPanel_FocusPanel01");
	}

	@Override
	public void buildForm() {
		mainPanel.clear();
		mainPanel.add(messageFormGrp);
		mainPanel.add(saveErrorDisplay);
		mainPanel.add(topSuccessMessage);
		mainPanel.add(topErrorMessage);
		saveErrorDisplay.clearAlert();
		stewardSPanel.clear();
		authorListBox.setVisibleItemCount(5);
		resetEndorsedByListBox();
		authorListBox.getElement().setId("authorListBox_ListBox");
		
		measureTypeListBox.setVisibleItemCount(5);
		measureTypeListBox.getElement().setId("measureTypeListBox_ListBox");
		
		VerticalPanel fPanel = new VerticalPanel();
		fPanel.setStyleName("leftSideForm");
		fPanel.getElement().setId("fPanel_FlowPanelLeft");
		fPanel.add(new SpacerWidget());
		fPanel.add(bottomErrorMessage);
		
		PanelGroup metadataPanelGroup = buildMeasureMetadeta();
		
		fPanel.add(metadataPanelGroup);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(buttonBarTop.getButtonToolBar());
		fPanel.add(new SpacerWidget());
		
		PanelGroup panelGroup = new PanelGroup();
		panelGroup.setId("panelGroup2");
		Panel panel = new Panel(PanelType.DEFAULT);
		PanelHeader header = new PanelHeader();
		header.setText("Add/Edit Measure Details");
		panel.add(header);
		PanelBody panelBody = new PanelBody();
		
		VerticalPanel moreMeasureDetailsVP = new VerticalPanel();
		panelBody.add(moreMeasureDetailsVP);
		panel.add(panelBody);
		panelGroup.add(panel);
		panel.setWidth("900px");
		HorizontalFlowPanel horizontalPanel = new HorizontalFlowPanel();
		// eMeasureId and generate button
		buildEmeasureIdComponent(moreMeasureDetailsVP, horizontalPanel);
		moreMeasureDetailsVP.add(horizontalPanel);
		
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		HorizontalPanel nqfNumberEndorsmentPanel = new HorizontalPanel();
		// NQF Endorsed By and NQF number Text box
		buildNQFEndorsedByAndNumberTextBoxComponent(nqfNumberEndorsmentPanel);
	
		moreMeasureDetailsVP.add(nqfNumberEndorsmentPanel);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		VerticalPanel measurementPeriodPanel = new VerticalPanel();
		// Measurement Period Component
		createMeasurementPeriodWidget(measurementPeriodPanel);
		
		moreMeasureDetailsVP.add(measurementPeriodPanel);
		moreMeasureDetailsVP.add(new SpacerWidget());
		// Steward List Component
		buildStewardListComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		//Author Table
		buildAuthorTableComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		
		buildDescriptionInputComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		buildCopyWriteComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		//Disclaimer
		buildDisclaimerComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		// Measure Type Table
		buildMeasureTypeTableComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		buildStratificationInputComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		buildRiskAdjustmentInputComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		//Rate Aggregation riskAggregationInput
		buildRateAggregationInputComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		buildRationaleInputComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		buildClinicalStatementInputComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		buildImprovementNotationInputComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		buildReferencesComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		buildDefinitionComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		buildGuidanceComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		buildTransmissionFormatInputComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		buildInitialPopulationInputComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		if ((measureScoringType != null) && (measureScoringType.equalsIgnoreCase("Ratio")
				|| measureScoringType.equalsIgnoreCase("Proportion"))) {
			buildDenominatorInputComponent(moreMeasureDetailsVP);
			moreMeasureDetailsVP.add(new SpacerWidget());
			
			buildDenominatorExclusionInputComponent(moreMeasureDetailsVP);
			moreMeasureDetailsVP.add(new SpacerWidget());
			
			buildNumneratorInputComponent(moreMeasureDetailsVP);
			moreMeasureDetailsVP.add(new SpacerWidget());
			
			buildNumeratorExclusionInputComponent(moreMeasureDetailsVP);
			moreMeasureDetailsVP.add(new SpacerWidget());
		}
		
		if ((measureScoringType != null) && ((measureScoringType.equalsIgnoreCase("Proportion")))) {
			buildDenominatorExceptionInputComponent(moreMeasureDetailsVP);
			moreMeasureDetailsVP.add(new SpacerWidget());
		}
		if((measureScoringType != null) && measureScoringType.equalsIgnoreCase("Continuous Variable")){
			buildMeasurePopulationInputComponent(moreMeasureDetailsVP);
			moreMeasureDetailsVP.add(new SpacerWidget());
			
			buildMeasurePopExclusionInputComponent(moreMeasureDetailsVP);
			moreMeasureDetailsVP.add(new SpacerWidget());
		}
		
		if((measureScoringType != null) &&(measureScoringType.equalsIgnoreCase("Continuous Variable")
				|| (measureScoringType.equalsIgnoreCase("Ratio") && !isPatientBasedMeasure))){
			buildMeasureObservationInputComponent(moreMeasureDetailsVP);
			moreMeasureDetailsVP.add(new SpacerWidget());
		}

		buildSupplementalDataElementInputComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		buildMeasureSetInputComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		
		setMaxWidthAndSize();
		
		moreMeasureDetailsVP.add(bottomErrorMessage);
		moreMeasureDetailsVP.add(bottomSuccessMessage);
		
		bottomSuccessMessage.clearAlert();
		moreMeasureDetailsVP.add(new SpacerWidget());
		buttonBarBottom.getButtonToolBar().getElement().getStyle().setPaddingBottom(10, Unit.PX);
		fPanel.add(panelGroup);
		fPanel.add(buttonBarBottom.getButtonToolBar());
		mainPanel.add(fPanel);
	}

	private void buildMeasureSetInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel measureSetNameLable = new FormLabel();
		measureSetNameLable.setText("Measure Set");
		measureSetNameLable.setId("measureSetNameLable");
		measureSetNameLable.setFor("setNameInput_TextAreaWithMaxLength");
		measureSetNameLable.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(measureSetNameLable);
		moreMeasureDetailsVP.add(setNameInput);
		setNameInput.setPlaceholder("Enter Measure Set");
		setNameInput.setTitle("Enter Measure Set");
		setNameInput.getElement().setId("setNameInput_TextAreaWithMaxLength");
	}

	private void buildSupplementalDataElementInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel supplementdalDataInputLabel = new FormLabel();
		supplementdalDataInputLabel.setText("Supplemental Data Elements");
		supplementdalDataInputLabel.setId("supplementdalDataInputLabel");
		supplementdalDataInputLabel.setFor("supplementalDataInput_TextAreaWithMaxLength");
		supplementdalDataInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(supplementdalDataInputLabel);
		moreMeasureDetailsVP.add(supplementalDataInput);
		supplementalDataInput.setPlaceholder("Enter Supplemental Data Elements");
		supplementalDataInput.setTitle("Enter Supplemental Data Elements");
		supplementalDataInput.setId("supplementalDataInput_TextAreaWithMaxLength");
	}

	private void buildMeasureObservationInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel measureObInputLabel = new FormLabel();
		measureObInputLabel.setText("Measure Observations");
		measureObInputLabel.setId("measureObInputLabel");
		measureObInputLabel.setFor("measureObservationsInput_TextAreaWithMaxLength");
		measureObInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(measureObInputLabel);
		moreMeasureDetailsVP.add(measureObservationsInput);
		measureObservationsInput.setPlaceholder("Enter Measure Observations");
		measureObservationsInput.setTitle("Enter Measure Observations");
		measureObservationsInput.setId("measureObservationsInput_TextAreaWithMaxLength");
	}

	private void buildMeasurePopExclusionInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel measurePopExclInputLabel = new FormLabel();
		measurePopExclInputLabel.setText("Measure Population Exclusions");
		measurePopExclInputLabel.setId("measurePopExclInputLabel");
		measurePopExclInputLabel.setFor("MeasurePopulationExclusionsInput_TextAreaWithMaxLength");
		measurePopExclInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(measurePopExclInputLabel);
		moreMeasureDetailsVP.add(measurePopulationExclusionsInput);
		measurePopulationExclusionsInput.setPlaceholder("Enter Measure Population Exclusions");
		measurePopulationExclusionsInput.setTitle("Enter Measure Population Exclusions");
		measurePopulationExclusionsInput.setId("MeasurePopulationExclusionsInput_TextAreaWithMaxLength");
	}

	private void buildMeasurePopulationInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel measurePopInputLabel = new FormLabel();
		measurePopInputLabel.setText("Measure Population");
		measurePopInputLabel.setId("measurePopInputLabel");
		measurePopInputLabel.setFor("measurePopulationInput_TextAreaWithMaxLength");
		measurePopInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(measurePopInputLabel);
		moreMeasureDetailsVP.add(measurePopulationInput);
		measurePopulationInput.setPlaceholder("Enter Measure Population");
		measurePopulationInput.setTitle("Enter Measure Population");
		measurePopulationInput.setId("measurePopulationInput_TextAreaWithMaxLength");
	}

	private void buildDenominatorExceptionInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel denoExcepInputLabel = new FormLabel();
		denoExcepInputLabel.setText("Denominator Exceptions");
		denoExcepInputLabel.setId("denoExcepInputLabel");
		denoExcepInputLabel.setFor("denominatorExceptionsInput_TextAreaWithMaxLength");
		denoExcepInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(denoExcepInputLabel);
		moreMeasureDetailsVP.add(denominatorExceptionsInput);
		denominatorExceptionsInput.setPlaceholder("Enter Denominator Exceptions");
		denominatorExceptionsInput.setTitle("Enter Denominator Exceptions");
		denominatorExceptionsInput.setId("denominatorExceptionsInput_TextAreaWithMaxLength");
	}

	private void buildNumeratorExclusionInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel numExclInputLabel = new FormLabel();
		numExclInputLabel.setText("Numerator Exclusions");
		numExclInputLabel.setId("numExclInputLabel");
		numExclInputLabel.setFor("numeratorExclusionsInput_TextAreaWithMaxLength");
		numExclInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(numExclInputLabel);
		moreMeasureDetailsVP.add(numeratorExclusionsInput);
		numeratorExclusionsInput.setPlaceholder("Enter Numerator Exclusions");
		numeratorExclusionsInput.setTitle("Enter Numerator Exclusions");
		numeratorExclusionsInput.setId("numeratorExclusionsInput_TextAreaWithMaxLength");
	}

	private void buildNumneratorInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel numInputLabel = new FormLabel();
		numInputLabel.setText("Numerator");
		numInputLabel.setId("numInputLabel");
		numInputLabel.setFor("numeratorInput_TextAreaWithMaxLength");
		numInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(numInputLabel);
		moreMeasureDetailsVP.add(numeratorInput);
		numeratorInput.setPlaceholder("Enter Numerator");
		numeratorInput.setTitle("Enter Numerator");
		numeratorInput.setId("numeratorInput_TextAreaWithMaxLength");
	}

	private void buildDenominatorExclusionInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel denoExclInputLabel = new FormLabel();
		denoExclInputLabel.setText("Denominator Exclusions");
		denoExclInputLabel.setId("denoExclInputLabel");
		denoExclInputLabel.setFor("denominatorExclusionsInput_TextAreaWithMaxLength");
		denoExclInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(denoExclInputLabel);
		moreMeasureDetailsVP.add(denominatorExclusionsInput);
		denominatorExclusionsInput.setPlaceholder("Enter Denominator Exclusions");
		denominatorExclusionsInput.setTitle("Enter Denominator Exclusions");
		denominatorExclusionsInput.setId("denominatorExclusionsInput_TextAreaWithMaxLength");
	}

	private void buildDenominatorInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel denoInputLabel = new FormLabel();
		denoInputLabel.setText("Denominator");
		denoInputLabel.setId("denoInputLabel");
		denoInputLabel.setFor("denominatorInput_TextAreaWithMaxLength");
		denoInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(denoInputLabel);
		moreMeasureDetailsVP.add(denominatorInput);
		denominatorInput.setPlaceholder("Enter Denominator");
		denominatorInput.setTitle("Enter Denominator");
		denominatorInput.setId("denominatorInput_TextAreaWithMaxLength");
	}

	private void buildInitialPopulationInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel initialPopInputLabel = new FormLabel();
		initialPopInputLabel.setText("Initial Population");
		initialPopInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(initialPopInputLabel);
		moreMeasureDetailsVP.add(initialPopInput);
		initialPopInputLabel.setId("initialPopInputLabel");
		initialPopInputLabel.setFor("initialPopInput_TextAreaWithMaxLength");
		initialPopInput.setPlaceholder("Enter Initial Population");
		initialPopInput.setTitle("Enter Initial Population");
		initialPopInput.setId("initialPopInput_TextAreaWithMaxLength");
	}

	private void buildTransmissionFormatInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel transmissionFormatInputLabel = new FormLabel();
		transmissionFormatInputLabel.setText("Transmission Format");
		transmissionFormatInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(transmissionFormatInputLabel);
		moreMeasureDetailsVP.add(transmissionFormatInput);
		transmissionFormatInputLabel.setId("transmissionFormatInputLabel");
		transmissionFormatInputLabel.setFor("transmissionFormatInput_TextAreaWithMaxLength");
		transmissionFormatInput.setPlaceholder("Enter Transmission Format");
		transmissionFormatInput.setTitle("Enter Transmission Format");
		transmissionFormatInput.setId("transmissionFormatInput_TextAreaWithMaxLength");
	}

	private void buildGuidanceComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel guidanceInputLabel = new FormLabel();
		guidanceInputLabel.setText("Guidance");
		guidanceInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(guidanceInputLabel);
		moreMeasureDetailsVP.add(guidanceInput);
		guidanceInputLabel.setId("guidanceInputLabel");
		guidanceInputLabel.setFor("guidanceInput_TextAreaWithMaxLength");
		guidanceInput.setPlaceholder("Enter Guidance");
		guidanceInput.setTitle("Enter Guidance");
		guidanceInput.setId("guidanceInput_TextAreaWithMaxLength");
	}

	private void buildDefinitionComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel definitionsInputLabel = new FormLabel();
		definitionsInputLabel.setText("Definition");
		definitionsInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(definitionsInputLabel);
		moreMeasureDetailsVP.add(definitionsInput);
		
		definitionsInputLabel.setId("definitionsInputLabel");
		definitionsInputLabel.setFor("definitionsInput_TextAreaWithMaxLength");
		definitionsInput.setPlaceholder("Enter Definition");
		definitionsInput.setTitle("Enter Definition");
		definitionsInput.getElement().setId("definitionsInput_TextAreaWithMaxLength");
	}

	private void buildReferencesComponent(VerticalPanel moreMeasureDetailsVP) {
		AddRowButton.getElement().setId("AddRowButton_Button");
		referenceInput.setSize("750px", "60px");
		referenceInput.setMaxLength(2000);
		referenceInput.setTitle("Reference");
		referenceInput.setPlaceholder("Enter Reference");
		
		buildReferenceTable(referenceInput);
		referencePlaceHolder.add(referenceTable);
		FormLabel referencePlaceHolderInputLabel = new FormLabel();
		referencePlaceHolderInputLabel.setText("Reference(s)");
		referencePlaceHolderInputLabel.setStyleName("measureDetailLabelStyle");
		referencePlaceHolderInputLabel.setId("referencePlaceHolderInputLabel");
		referencePlaceHolderInputLabel.setFor("referencePlaceHolder_SimplePanel");
		referencePlaceHolder.getElement().setId("referencePlaceHolder_SimplePanel");
		moreMeasureDetailsVP.add(referencePlaceHolderInputLabel);
		moreMeasureDetailsVP.add(referencePlaceHolder);
	}

	private void buildImprovementNotationInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel improvementNotationInputLabel = new FormLabel();
		improvementNotationInputLabel.setText("Improvement Notation");
		
		improvementNotationInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(improvementNotationInputLabel);
		moreMeasureDetailsVP.add(improvementNotationInput);
		
		improvementNotationInputLabel.setId("improvementNotationInputLabel");
		improvementNotationInputLabel.setFor("improvementNotationInput_TextAreaWithMaxLength");
		improvementNotationInput.setPlaceholder("Enter Improvement Notation");
		improvementNotationInput.setTitle("Enter Improvement Notation");
		improvementNotationInput.setId("improvementNotationInput_TextAreaWithMaxLength");
	}

	private void buildClinicalStatementInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel clinicalStmtInputLabel = new FormLabel();
		clinicalStmtInputLabel.setText("Clinical Recommendation Statement");
		clinicalStmtInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(clinicalStmtInputLabel);
		moreMeasureDetailsVP.add(clinicalStmtInput);
		
		clinicalStmtInputLabel.setId("clinicalStmtInputLabel");
		clinicalStmtInputLabel.setFor("clinicalStmtInput_TextAreaWithMaxLength");
		clinicalStmtInput.setPlaceholder("Enter Clinical Recommendation Statement");
		clinicalStmtInput.setTitle("Enter Clinical Recommendation Statement");
		clinicalStmtInput.setId("clinicalStmtInput_TextAreaWithMaxLength");
	}

	private void buildRationaleInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel rationaleInputLabel = new FormLabel();
		rationaleInputLabel.setText("Rationale");
		rationaleInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(rationaleInputLabel);
		moreMeasureDetailsVP.add(rationaleInput);
		
		rationaleInputLabel.setId("rationaleInputLabel");
		rationaleInputLabel.setFor("rationaleInput_TextAreaWithMaxLength");
		rationaleInput.setPlaceholder("Enter Rationale");
		rationaleInput.setTitle("Enter Rationale");
		rationaleInput.setId("rationaleInput_TextAreaWithMaxLength");
	}

	private void buildRateAggregationInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel riskAggInputLabel = new FormLabel();
		riskAggInputLabel.setText("Rate Aggregation");
		riskAggInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(riskAggInputLabel);
		moreMeasureDetailsVP.add(rateAggregationInput);
		riskAggInputLabel.setId("riskAggInputLabel");
		riskAggInputLabel.setFor("rateAggregationInput_TextAreaWithMaxLength");
		rateAggregationInput.setPlaceholder("Enter Rate Aggregation");
		rateAggregationInput.setTitle("Enter Rate Aggregation");
		rateAggregationInput.setId("rateAggregationInput_TextAreaWithMaxLength");
	}

	private void buildRiskAdjustmentInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel riskAdjInputLabel = new FormLabel();
		riskAdjInputLabel.setText("Risk Adjustment");
		riskAdjInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(riskAdjInputLabel);
		moreMeasureDetailsVP.add(riskAdjustmentInput);
		
		riskAdjInputLabel.setId("riskAdjInputLabel");
		riskAdjInputLabel.setFor("riskAdjustmentInput_TextAreaWithMaxLength");
		riskAdjustmentInput.setPlaceholder("Enter Risk Adjustment");
		riskAdjustmentInput.setTitle("Enter Risk Adjustment");
		riskAdjustmentInput.setId("riskAdjustmentInput_TextAreaWithMaxLength");
	}

	private void buildStratificationInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel stratificationInputLabel = new FormLabel();
		stratificationInputLabel.setText("Stratification");
		stratificationInputLabel.setStyleName("measureDetailLabelStyle");
		stratificationInputLabel.setId("stratificationInputLabel");
		stratificationInputLabel.setFor("stratificationInput_TextAreaWithMaxLength");
		stratificationInput.setPlaceholder("Enter Stratification");
		stratificationInput.setTitle("Enter Stratification");
		stratificationInput.setId("stratificationInput_TextAreaWithMaxLength");
		moreMeasureDetailsVP.add(stratificationInputLabel);
		moreMeasureDetailsVP.add(stratificationInput);
	}

	private void buildComponentMeasureTableComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel CompMeasureTableLabel = new FormLabel();
		CompMeasureTableLabel.setText("Component Measures List");
		CompMeasureTableLabel.setTitle("Component Measures List");
		CompMeasureTableLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(CompMeasureTableLabel);
		moreMeasureDetailsVP.add(componentMeasuresListSPanel);
	}

	private void buildMeasureTypeTableComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel measureTypeTableLabel = new FormLabel();
		measureTypeTableLabel.setText("Measure Type List");
		measureTypeTableLabel.setTitle("Measure Type List");
		measureTypeTableLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(measureTypeTableLabel);
		moreMeasureDetailsVP.add(measureTypeSPanel);
	}

	private void buildDisclaimerComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel disclaimerInputLabel = new FormLabel();
		disclaimerInputLabel.setText("Disclaimer");
		disclaimerInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(disclaimerInputLabel);
		moreMeasureDetailsVP.add(disclaimerInput);
		
		disclaimerInputLabel.setId("disclaimerInputLabel");
		disclaimerInputLabel.setFor("disclaimerInput_TextAreaWithMaxLength");
		disclaimerInput.setPlaceholder("Enter Disclaimer");
		disclaimerInput.setTitle("Enter Disclaimer");
		disclaimerInput.setId("disclaimerInput_TextAreaWithMaxLength");
	}

	private void buildCopyWriteComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel copyrightInputLabel = new FormLabel();
		copyrightInputLabel.setText("Copyright");
		copyrightInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(copyrightInputLabel);
		moreMeasureDetailsVP.add(copyrightInput);
		copyrightInputLabel.setId("copyrightInputLabel");
		copyrightInputLabel.setFor("copyrightInput_TextAreaWithMaxLength");
		copyrightInput.setPlaceholder("Enter Copyright");
		copyrightInput.setTitle("Enter Copyright");
		copyrightInput.setId("copyrightInput_TextAreaWithMaxLength");
	}

	private void buildDescriptionInputComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel descriptionInputLabel = new FormLabel();
		descriptionInputLabel.setText("Description");
		descriptionInputLabel.setTitle("Description");
		descriptionInputLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(descriptionInputLabel);
		moreMeasureDetailsVP.add(descriptionInput);
		descriptionInputLabel.setId("descriptionInputLabel");
		descriptionInputLabel.setFor("descriptionInput_TextAreaWithMaxLength");
		descriptionInput.setPlaceholder("Enter Description");
		descriptionInput.setTitle("Enter Description");
		descriptionInput.setId("descriptionInput_TextAreaWithMaxLength");
	}

	private void buildAuthorTableComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel authorTableLabel = new FormLabel();
		authorTableLabel.setText("Measure Developer List");
		authorTableLabel.setStyleName("measureDetailLabelStyle");
		moreMeasureDetailsVP.add(authorTableLabel);
		moreMeasureDetailsVP.add(authorSPanel);
	}

	private void buildStewardListComponent(VerticalPanel moreMeasureDetailsVP) {
		FormLabel stewardTableLabel = new FormLabel();
		stewardTableLabel.setText("Measure Steward List");
		stewardTableLabel.setStyleName("measureDetailLabelStyle");
		stewardTableLabel.setId("stewardTableLabel");
		stewardTableLabel.setFor("stewardListBox");
		stewardListBox.setId("stewardListBox");
		stewardListBox.setTitle("Measure Steward List");
		moreMeasureDetailsVP.add(stewardTableLabel);
		stewardListBox.setWidth("750px");
		stewardSPanel.add(stewardListBox);
		moreMeasureDetailsVP.add(stewardSPanel);
	}

	private void buildNQFEndorsedByAndNumberTextBoxComponent(HorizontalPanel nqfNumberEndorsmentPanel) {
		VerticalPanel nqfNumberLeftVP = new VerticalPanel();
		VerticalPanel nqfNumberRightVP = new VerticalPanel();
		
		FormLabel nQFIDInputLabel = new FormLabel();
		nQFIDInputLabel.setText("NQF Number");
		nqfNumberRightVP.add(nQFIDInputLabel);
		nqfNumberRightVP.add(new SpacerWidget());
		nqfNumberRightVP.add(nQFIDInput);
		
		nQFIDInputLabel.setId("nQFIDInputLabel");
		nQFIDInputLabel.setFor("NQFIDInput_TextBox");
		nQFIDInput.setId("NQFIDInput_TextBox");

		nQFIDInput.setPlaceholder(MatConstants.ENTER_NQF_NUMBER);
		nQFIDInput.setTitle(MatConstants.ENTER_NQF_NUMBER);
		nQFIDInput.getElement().setAttribute("style", "width:150px;margin-top:-10px;");
		
		FormLabel endorsedByNQFLabel = new FormLabel();
		endorsedByNQFLabel.setText("Endorsed By NQF");
		nqfNumberLeftVP.add(endorsedByNQFLabel);
		endorsedByListBox.setWidth("150px");
		endorsedByListBox.setId("endorsedByNQFListBox");
		nqfNumberLeftVP.add(endorsedByListBox);
		nqfNumberRightVP.getElement().setAttribute("style", "padding-left:10px;");
		nqfNumberEndorsmentPanel.add(nqfNumberLeftVP);
		nqfNumberEndorsmentPanel.add(nqfNumberRightVP);
	}

	private void buildEmeasureIdComponent(VerticalPanel moreMeasureDetailsVP, HorizontalFlowPanel horizontalPanel) {
		horizontalPanel.getElement().setId("horizontalPanel_HorizontalFlowPanelLeft");
		FormLabel eMeasureIdentifierInputLabel = new FormLabel();
		eMeasureIdentifierInputLabel.setStyleName("bold");
		eMeasureIdentifierInputLabel.setText( "eCQM Identifier (Measure Authoring Tool)");
		moreMeasureDetailsVP.add(new SpacerWidget());
		moreMeasureDetailsVP.add(eMeasureIdentifierInputLabel);
		eMeasureIdentifierInputLabel.setId("eMeasureIdentifierInputLabel");
		eMeasureIdentifierInputLabel.setFor("eMeasureIdentifierInput_TextBox");
		eMeasureIdentifierInput.setId("eMeasureIdentifierInput_TextBox");
		eMeasureIdentifierInput.setTitle("Generated Identifier");
		eMeasureIdentifierInput.setWidth("150px");
		
		horizontalPanel.add(eMeasureIdentifierInput);
		horizontalPanel.add(generateeMeasureIDButton);
		generateeMeasureIDButton.getElement().setId("generateeMeasureIDButton_Button");
		generateeMeasureIDButton.getElement().setId("generateeMeasureIDButton_Button");
		eMeasureIdentifierInput.setReadOnly(true);
		generateeMeasureIDButton.setEnabled(true);
		String emeasureIdMSG = "Once an eCQM Identifier (Measure Authoring Tool) has been generated it may not be modified or removed for any draft or version of a measure.";
		generateeMeasureIDButton.setTitle(emeasureIdMSG);
		eMeasureIdentifierInput.setTitle(emeasureIdMSG);
	}

	private PanelGroup buildMeasureMetadeta() {
		VerticalPanel wrapperPanel = new VerticalPanel();
		HorizontalPanel generalMainPanel = new HorizontalPanel();
		VerticalPanel generalLeftPanel = new VerticalPanel();
		
		VerticalPanel generalRightPanel = new VerticalPanel();
		
		FormLabel measScoringInputLabel = new FormLabel();
		measScoringInputLabel.setText("Measure Scoring");
		
		measScoringInputLabel.setId("MeasScoringLabel");
		measScoringInputLabel.setFor("MeasScoringValue");
		measScoringInputLabel.setTitle(measScoringInputLabel.getText());
		generalLeftPanel.add(measScoringInputLabel);
		
		measScoringInput.setReadOnly(true);
		measScoringInput.setEnabled(false);
		measScoringInput.getElement().setId("MeasScoringValue");
		measScoringInput.setWidth("300px");
		generalLeftPanel.add(measScoringInput);
		generalLeftPanel.add(new SpacerWidget());
		
		FormLabel patientBasedLabel = new FormLabel();
		patientBasedLabel.setText("Patient-based Measure");
		patientBasedLabel.setTitle(patientBasedLabel.getText());
		generalLeftPanel.add(patientBasedLabel);
		patientBasedLabel.setId("patientBasedInput");
		patientBasedLabel.setFor("patientBasedMeasure");
		patientBasedInput.setId("patientBasedMeasure");
		patientBasedInput.setReadOnly(true);
		patientBasedInput.setEnabled(false);
		patientBasedInput.setWidth("300px");
		generalLeftPanel.add(patientBasedInput);
		generalLeftPanel.add(new SpacerWidget());
		
		FormLabel abbrInputLabel =  new FormLabel();
		abbrInputLabel.setText("eCQM Abbreviated Title");
		abbrInputLabel.setTitle(abbrInputLabel.getText());
		abbrInputLabel.setId("eCQMAbbrTitleLbl");
		abbrInputLabel.setFor("abbrInput");
		generalLeftPanel.add(abbrInputLabel);
		abbrInput.setReadOnly(true);
		abbrInput.setEnabled(false);
		abbrInput.setWidth("300px");
		abbrInput.setId("abbrInput");
		generalLeftPanel.add(abbrInput);
		generalLeftPanel.add(new SpacerWidget());
		
		FormLabel finalizedDateLabel = new FormLabel();
		finalizedDateLabel.setText("Finalized Date");
		finalizedDateLabel.setTitle(finalizedDateLabel.getText());
		generalRightPanel.add(finalizedDateLabel);
		finalizedDateLabel.setId("finalizedDateLabel");
		finalizedDateLabel.setFor("finalizedDate");
		finalizedDate.setId("finalizedDate");
		finalizedDate.setReadOnly(true);
		finalizedDate.setEnabled(false);
		finalizedDate.setWidth("300px");
		generalRightPanel.add(finalizedDate);
		generalRightPanel.add(new SpacerWidget());
		
		FormLabel eMeasureIdentifierLabel = new FormLabel();
		eMeasureIdentifierLabel.setText("GUID");
		eMeasureIdentifierLabel.setTitle(eMeasureIdentifierLabel.getText());
		generalRightPanel.add(eMeasureIdentifierLabel);
		eMeasureIdentifierLabel.setId("eMeasureIdentifierLabel");
		eMeasureIdentifierLabel.setFor("eMeasureIdentifier");
		eMeasureIdentifier.setId("eMeasureIdentifier");
		eMeasureIdentifier.setReadOnly(true);
		eMeasureIdentifier.setEnabled(false);
		eMeasureIdentifier.setWidth("300px");
		generalRightPanel.add(eMeasureIdentifier);
		generalRightPanel.add(new SpacerWidget());
		
		FormLabel versionInputLabel = new FormLabel();
		versionInputLabel.setText("eCQM Version Number");
		versionInputLabel.setTitle(versionInputLabel.getText());
		generalRightPanel.add(versionInputLabel);
		versionInputLabel.setId("versionInputLabel");
		versionInputLabel.setFor("versionInput");
		versionInput.setReadOnly(true);
		versionInput.setEnabled(false);
		versionInput.setWidth("300px");
		versionInput.setId("versionInput");
		generalRightPanel.add(versionInput);
		generalRightPanel.getElement().setAttribute("style", "margin-left:15px;");
		generalMainPanel.add(generalLeftPanel);
		generalMainPanel.add(generalRightPanel);
		
		componentMeasurePanel = new VerticalPanel();
		buildComponentMeasureTableComponent(componentMeasurePanel);
		wrapperPanel.add(generalMainPanel);
		wrapperPanel.add(new SpacerWidget());
		wrapperPanel.add(componentMeasurePanel);
		
		PanelCollapse panelCollapse = new PanelCollapse();
		Anchor viewMetadataAnchor = new Anchor();

		viewMetadataAnchor.setDataToggle(Toggle.COLLAPSE);
		viewMetadataAnchor.setDataParent("#panelGroup");
		viewMetadataAnchor.setHref("#panelCollapse");
		viewMetadataAnchor.setText("Click to view General Measure Information");
		viewMetadataAnchor.setTitle("Click to view General Measure Information");
		viewMetadataAnchor.setColor("White");
		viewMetadataAnchor.setPaddingRight(35);

		PanelGroup panelGroup = new PanelGroup();
		panelGroup.setId("panelGroup");
		Panel panel = new Panel(PanelType.PRIMARY);
		PanelHeader header = new PanelHeader();

		header.add(viewMetadataAnchor);

		panelCollapse.setId("panelCollapse");
		PanelBody body = new PanelBody();

		body.add(wrapperPanel);
		panelCollapse.add(body);

		panel.add(header);
		panel.add(panelCollapse);

		panelGroup.add(panel);
	
		return panelGroup;
	}

	private void createMeasurementPeriodWidget(VerticalPanel measurementPeriodPanel) {
		measurementPeriodPanel.getElement().setId("measurementPeriod_VerticalPanel");
		measurementPeriodPanel.setStyleName("valueSetSearchPanel");
		measurementPeriodPanel.setSize("505px", "100px");
		FormLabel measurePeriodFromInputLabel = new FormLabel();
		measurePeriodFromInputLabel.setText("Measurement Period");
		measurePeriodFromInputLabel.setStyleName("measureDetailTableHeader");
		measurePeriodFromInputLabel.getElement().setId("measurementPeriodHeader_Label");
		measurePeriodFromInputLabel.getElement().setAttribute("tabIndex", "0");
		measurementPeriodPanel.add(measurePeriodFromInputLabel);

		HorizontalPanel calenderYearDatePanel = new HorizontalPanel();
		calenderYearDatePanel.getElement().setId("calenderYear_HorizontalPanel");
		calenderYearDatePanel.add(calenderYear);
		FormLabel calenderLabel = new FormLabel();
		calenderLabel.setText("Calendar Year");
		calenderLabel.setStyleName("qdmLabel");
		FormLabel calenderYearLabel = new FormLabel();
		calenderYearLabel.setText("(January 1,20XX through December 31,20XX)");
		calenderYearLabel.setStyleName("qdmLabel");
		calenderYearDatePanel.add(calenderLabel);
		calenderYearDatePanel.add(calenderYearLabel);
		calenderYear.getElement().setId("calenderYear_CustomCheckBox");
		calenderYear.addValueChangeHandler(calenderYearChangeHandler);
		calenderYearDatePanel.addStyleName("marginTop");
		
		HorizontalPanel measurePeriodPanel = new HorizontalPanel();
		measurePeriodPanel.getElement().setId("measurePeriodPanel_HorizontalPanel");
		FormLabel fromLabel = new FormLabel();
		fromLabel.setText("From");
		fromLabel.setTitle("From");
		fromLabel.setMarginTop(5.00);
		fromLabel.addStyleName("firstLabel");
		measurePeriodPanel.add(fromLabel);
		measurePeriodFromInput.getDateBox().getElement().setAttribute("id", "measurePeriodFromInput");
		measurePeriodFromInput.getDateBox().setWidth("127px");
		measurePeriodFromInput.getCalendar().setTitle("Click to select From date.");
		measurePeriodPanel.add(measurePeriodFromInput);
		measurePeriodFromInput.getElement().setId("measurePeriodFromInput_DateBoxWithCalendar");
		FormLabel toLabel = new FormLabel();
		toLabel.setText("To");
		toLabel.setTitle("To");
		toLabel.setMarginTop(5.00);
		toLabel.addStyleName("secondLabel");
		measurePeriodPanel.add(toLabel);
		measurePeriodToInput.getDateBox().setWidth("127px");
		measurePeriodToInput.getCalendar().setTitle("Click to select To date.");
		measurePeriodToInput.getDateBox().getElement().setAttribute("id", "measurePeriodToInput");
		measurePeriodPanel.add(measurePeriodToInput);
		measurePeriodToInput.getElement().setId("measurePeriodToInput_DateBoxWithCalendar");
		
		Grid queryGrid = new Grid(3, 1);
		queryGrid.setWidget(0, 0, calenderYearDatePanel);
		queryGrid.setWidget(1, 0, new SpacerWidget());
		queryGrid.setWidget(2, 0, measurePeriodPanel);
		queryGrid.setStyleName("secondLabel");
		measurementPeriodPanel.add(queryGrid);
		queryGrid.getElement().setId("queryGrid_Grid");
	}

	private void setMaxWidthAndSize() {
		codeSystemVersionInput.setMaxLength(255);
		rationaleInput.setMaxLength(15000);
		nQFIDInput.setMaxLength(64);
		descriptionInput.setSize("750px", "60px");
		descriptionInput.setMaxLength(15000);
		copyrightInput.setSize("750px", "60px");
		copyrightInput.setMaxLength(15000);
		disclaimerInput.setSize("750px", "60px");
		disclaimerInput.setMaxLength(15000);
		rationaleInput.setMaxLength(15000);
		rationaleInput.setSize("750px", "60px");
		stratificationInput.setSize("750px", "60px");
		stratificationInput.setMaxLength(15000);
		riskAdjustmentInput.setSize("750px", "60px");
		riskAdjustmentInput.setMaxLength(15000);
		rateAggregationInput.setSize("750px", "60px");
		rateAggregationInput.setMaxLength(15000);
		
		setNameInput.setSize("750px", "50px");
		setNameInput.setMaxLength(155);
		
		clinicalStmtInput.setSize("750px", "60px");
		clinicalStmtInput.setMaxLength(15000);
		improvementNotationInput.setSize("750px", "60px");
		improvementNotationInput.setMaxLength(15000);
		stratificationInput.setSize("750px", "60px");
		definitionsInput.setMaxLength(15000);
		definitionsInput.setSize("750px", "60px");
		guidanceInput.setMaxLength(15000);
		guidanceInput.setSize("750px", "60px");
		transmissionFormatInput.setMaxLength(15000);
		transmissionFormatInput.setSize("750px", "60px");
		supplementalDataInput.setMaxLength(15000);
		supplementalDataInput.setSize("750px", "60px");
		
		initialPopInput.setSize("750px", "60px");
		initialPopInput.setMaxLength(15000);
		denominatorInput.setSize("750px", "60px");
		denominatorInput.setMaxLength(15000);
		denominatorExclusionsInput.setSize("750px", "60px");
		denominatorExclusionsInput.setMaxLength(15000);
		numeratorInput.setSize("750px", "60px");
		numeratorInput.setMaxLength(15000);
		numeratorExclusionsInput.setSize("750px", "60px");
		numeratorExclusionsInput.setMaxLength(15000);
		denominatorExceptionsInput.setSize("750px", "60px");
		denominatorExceptionsInput.setMaxLength(15000);
		measurePopulationInput.setSize("750px", "60px");
		measurePopulationInput.setMaxLength(15000);
		measurePopulationExclusionsInput.setSize("750px","60px");
		measurePopulationExclusionsInput.setMaxLength(15000);
		measureObservationsInput.setSize("750px", "60px");
		measureObservationsInput.setMaxLength(15000);
		
		eMeasureIdentifierInput.setMaxLength(6);
	}	

	public void updateMeasureTypeSelectedList(List<MeasureType> measureTypeList) {
		if (measureTypeSelectedList.size() != 0) {
			for (int i = 0; i < measureTypeSelectedList.size(); i++) {
				for (int j = 0; j < measureTypeList.size(); j++) {
					if (measureTypeSelectedList.get(i).getDescription().
							equalsIgnoreCase(measureTypeList.get(j).getDescription())) {
						measureTypeSelectedList.set(i, measureTypeList.get(j));
						break;
					}
				}
			}
		}
	}
	

	public void updateMeasureDevelopersSelectedList(List<Author> measureDeveloperList) {
		if (authorsSelectedList.size() != 0) {
			for (int i = 0; i < authorsSelectedList.size(); i++) {
				for (int j = 0; j < measureDeveloperList.size(); j++) {
					if (authorsSelectedList.get(i).getId().
							equalsIgnoreCase(measureDeveloperList.get(j).getId())) {
						authorsSelectedList.set(i, measureDeveloperList.get(j));
						break;
					}
				}
			}
		}
		
	}

	private  List<Author> swapMeasureDevelopersList(List<Author> authorsList) {
		List<Author> authorsListSelected = new ArrayList<Author>();
		authorsListSelected.addAll(authorsSelectedList);
		for (int i = 0; i < authorsList.size(); i++) {
			if (!authorsSelectedList.contains(authorsList.get(i))) {
				authorsListSelected.add(authorsList.get(i));
			}
		}
		
		return authorsListSelected;
	}
	

	private CellTable<ManageMeasureSearchModel.Result> addMeasuresColumnToTable(boolean editable) {
		Label measureSearchHeader = new Label();
		measureSearchHeader.setText("Component Measures List");
		measureSearchHeader.getElement().setId("measureSearchHeader_Label");
		measureSearchHeader.setStyleName("invisibleTableCaption");
		com.google.gwt.dom.client.TableElement elem = componentMeasureCellTable
				.getElement().cast();
		measureSearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());
		measureSelectionModel = new MultiSelectionModel<ManageMeasureSearchModel.Result>();
		componentMeasureCellTable.setSelectionModel(measureSelectionModel);

		
		Column<ManageMeasureSearchModel.Result, SafeHtml> measureNameColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				return CellTableUtility.getColumnToolTip(object.getName());
			}
		};
		
		componentMeasureCellTable.addColumn(measureNameColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Measure Name'>"
						+ "Measure Name" + "</span>"));
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> versionColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				return CellTableUtility.getColumnToolTip(object.getVersion());
			}
		};
		
		componentMeasureCellTable.addColumn(versionColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Version'>"
						+ "Version" + "</span>"));
		
		return componentMeasureCellTable;
	}

	@Override
	public void buildComponentMeasuresSelectedList(List<ManageMeasureSearchModel.Result> result, boolean editable){
		componentMeasuresListSPanel.clear();
		componentMeasuresListSPanel.setStyleName("cellTablePanelMeasureDetails");
		if(result.size()>0){
			componentMeasureCellTable = new CellTable<ManageMeasureSearchModel.Result>();
			componentMeasureCellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			ListDataProvider<ManageMeasureSearchModel.Result> sortProvider =
					new ListDataProvider<ManageMeasureSearchModel.Result>();
			selectedMeasureList = new ArrayList<ManageMeasureSearchModel.Result>();
			measuresListSelectionModel = new MultiSelectionModel<ManageMeasureSearchModel.Result>();
			componentMeasureCellTable.setSelectionModel(measuresListSelectionModel);
			selectedMeasureList.addAll(result);
			componentMeasureCellTable.setRowData(selectedMeasureList);
			componentMeasureCellTable.setRowCount(selectedMeasureList.size(), true);
			sortProvider.refresh();
			sortProvider.getList().addAll(result);
			updateLoadingState(componentMeasureCellTable);
			componentMeasureSelectedList = result;
			componentMeasureCellTable = addMeasuresColumnToTable(editable);
			sortProvider.addDataDisplay(componentMeasureCellTable);
			componentMeasureCellTable.setWidth("100%");
			Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("componentMeasureListSummary",
					"In the following Component Measure List table, Measure Name is given in first column,"
							+ " Version in second column.");
			componentMeasureCellTable.getElement().setAttribute("id", "ComponentMeasuresListCellTable");
			componentMeasureCellTable.getElement().setAttribute("aria-describedby", "componentMeasureListSummary");
			componentMeasureCellTable.setWidth("99%");
			VerticalPanel vp = new VerticalPanel();
			vp.add(invisibleLabel);
			vp.add(componentMeasureCellTable);
			vp.setSize("100%", "150px");
			componentMeasuresListSPanel.setWidget(vp);
			componentMeasurePanel.setVisible(true);
		} else {
			componentMeasurePanel.setVisible(false);
		}
	}
	

	private void addMeasureTypeColumnToTable(boolean editable) {
		Label measureSearchHeader = new Label("Measure Type List");
		measureSearchHeader.getElement().setId("measureTypeHeader_Label");
		measureSearchHeader.setStyleName("invisibleTableCaption");
		com.google.gwt.dom.client.TableElement elem = measureTypeCellTable
				.getElement().cast();
		measureSearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());
		measureTypeSelectioModel = new MultiSelectionModel<MeasureType>();
		measureTypeCellTable.setSelectionModel(measureTypeSelectioModel);
		MatCheckBoxCell chbxCell = new MatCheckBoxCell(false, true, !editable);
		
		Column<MeasureType, Boolean> selectColumn = new Column<MeasureType, Boolean>(
				chbxCell) {
			@Override
			public Boolean getValue(MeasureType object) {
				boolean isSelected = false;
				if (measureTypeSelectedList.size() > 0) {
					for (int i = 0; i < measureTypeSelectedList.size(); i++) {
						if (measureTypeSelectedList.get(i).getDescription()
								.equalsIgnoreCase(object.getDescription())) {
							isSelected = true;
							break;
						}
					}
				} else {
					isSelected = false;
				}
				return isSelected;
			}
		};
		
		selectColumn.setFieldUpdater(new FieldUpdater<MeasureType, Boolean>() {
			@Override
			public void update(int index, MeasureType object, Boolean value) {
				measureTypeSelectioModel.setSelected(object, value);
				if (value) {
					measureTypeSelectedList.add(object);
				} else {
					for (int i = 0; i < measureTypeSelectedList.size(); i++) {
						if (measureTypeSelectedList.get(i).getDescription()
								.equalsIgnoreCase(object.getDescription())) {
							measureTypeSelectedList.remove(i);
							break;
						}
					}
				}
			}
		});
		
		measureTypeCellTable.addColumn(selectColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Select'>"
						+ "Select" + "</span>"));
		
		Column<MeasureType, SafeHtml> measureNameColumn = new Column<MeasureType, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(MeasureType object) {
				return CellTableUtility.getColumnToolTip(object
						.getDescription());
			}
		};
		
		measureTypeCellTable.addColumn(measureNameColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Measure Type'>"
						+ "Measure Type" + "</span>"));
	}

	@Override
	public void buildMeasureTypeCellTable(List<MeasureType> measureTypeDTOList, boolean editable){
		measureTypeSPanel.clear();
		measureTypeSPanel.setStyleName("cellTablePanelMeasureDetails");
		
		Collections.sort(measureTypeDTOList, new MeasureType.MeasureTypeComparator());
		
		measureTypeDTOList.removeIf(m -> m.getAbbrName().equals(COMPOSITE));
		
		if (measureTypeDTOList.size() > 0) {
			measureTypeCellTable = new CellTable<MeasureType>();
			measureTypeCellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			ListDataProvider<MeasureType> sortProvider = new ListDataProvider<MeasureType>();
			measureTypeCellTable.setRowData(measureTypeDTOList);
			measureTypeCellTable.setRowCount(measureTypeDTOList.size(), true);
			sortProvider.refresh();
			sortProvider.getList().addAll(measureTypeDTOList);
			addMeasureTypeColumnToTable(editable);
			updateMeasureTypeSelectedList(measureTypeDTOList);
			sortProvider.addDataDisplay(measureTypeCellTable);
			measureTypeCellTable.setWidth("100%");
			Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("measureTypeListSummary",
					"In the following Measure Type List table,Select is given in first Column and Measure Type is given in Second column");
			measureTypeCellTable.getElement().setAttribute("id", "MeasureTypeListCellTable");
			measureTypeCellTable.getElement().setAttribute("aria-describedby", "measureTypeListSummary");
			
			VerticalPanel vp = new VerticalPanel();
			vp.add(invisibleLabel);
			vp.add(measureTypeCellTable);
			vp.setSize("750px", "150px");
			measureTypeSPanel.setWidget(vp);
		}
	}

	@Override
	public void buildAuthorCellTable(List<Author> currentAuthorsList, boolean editable) {
		authorSPanel.clear();
		authorSPanel.setStyleName("cellTablePanelMeasureDetails");
		authorCellTable = new CellTable<Author>();
		authorCellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<Author> sortProvider = new ListDataProvider<Author>();
		authorCellTable.setRowData(currentAuthorsList);
		if((authorsSelectedList!=null) && (authorsSelectedList.size()>0)){
			List<Author> selectauthorsList = new ArrayList<Author>();
			updateMeasureDevelopersSelectedList(currentAuthorsList);
			selectauthorsList.addAll(swapMeasureDevelopersList(currentAuthorsList));
			authorCellTable.setRowData(selectauthorsList);
			authorCellTable.setRowCount(selectauthorsList.size(), true);
			sortProvider.refresh();
			sortProvider.getList().addAll(selectauthorsList);
		} else {
			authorCellTable.setRowData(currentAuthorsList);
			authorCellTable.setRowCount(currentAuthorsList.size(), true);
			sortProvider.refresh();
			sortProvider.getList().addAll(currentAuthorsList);
		}
		authorCellTable.setRowCount(currentAuthorsList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(currentAuthorsList);
		addAuthorColumnToTable(editable);
		sortProvider.addDataDisplay(authorCellTable);
		authorCellTable.setWidth("100%");
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("authorListSummary",
				"In the following Measure Type List table,Select is given in first Column for selection and Author is given in Second column.");
		authorCellTable.getElement().setAttribute("id", "AuthorListCellTable");
		authorCellTable.getElement().setAttribute("aria-describedby", "authorListSummary");
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(invisibleLabel);
		vp.add(authorCellTable);
		vp.setSize("750px", "150px");
		authorSPanel.setWidget(vp);
	}

	private void addAuthorColumnToTable(boolean editable) {
		Label measureSearchHeader = new Label("Measure Developer List");
		measureSearchHeader.getElement().setId("measureDeveloperHeader_Label");
		measureSearchHeader.setStyleName("invisibleTableCaption");
		com.google.gwt.dom.client.TableElement elem = authorCellTable.getElement().cast();
		measureSearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());
		authorSelectionModel = new MultiSelectionModel<Author>();
		authorCellTable.setSelectionModel(authorSelectionModel);
		MatCheckBoxCell chbxCell = new MatCheckBoxCell(false, true, !editable);
		Column<Author, Boolean> selectColumn = new Column<Author, Boolean>(
				chbxCell) {
			@Override
			public Boolean getValue(Author object) {
				boolean isSelected = false;
				if ((authorsSelectedList != null) && (authorsSelectedList.size() > 0)) {
					for (int i = 0; i < authorsSelectedList.size(); i++) {
						if (authorsSelectedList.get(i).getId()
								.equalsIgnoreCase(object.getId())) {
							isSelected = true;
							break;
						}
					}
				} else {
					isSelected = false;
				}
				return isSelected;
				
			}
		};
		
		selectColumn.setFieldUpdater(new FieldUpdater<Author, Boolean>() {
			@Override
			public void update(int index, Author object, Boolean value) {
				authorSelectionModel.setSelected(object, value);
				if (value) {
					authorsSelectedList.add(object);
				} else {
					for (int i = 0; i < authorsSelectedList.size(); i++) {
						if (authorsSelectedList.get(i).getId()
								.equalsIgnoreCase(object.getId())) {
							authorsSelectedList.remove(i);
							break;
						}
					}
					
				}
				
			}
		});
		
		authorCellTable.addColumn(selectColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Select'>"
						+ "Select" + "</span>"));
		
		Column<Author, SafeHtml> measureNameColumn = new Column<Author, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Author object) {
				return CellTableUtility.getColumnToolTip(object.getAuthorName(), object.getOrgId());
			}
		};
		
		authorCellTable.addColumn(measureNameColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Measure Developers Name'>"
						+ "Measure Developer" + "</span>"));
	}
	

	private void updateLoadingState(CellTable<ManageMeasureSearchModel.Result> cellTable) {
		int cacheSize = cellTable.getVisibleItemCount();
		int curPageSize = cellTable.getPageSize();
		if (cacheSize >= curPageSize) {
			cellTable.setLoadingIndicator(new Image(ImageResources.INSTANCE.g_lock()));
		} else if (cacheSize == 0) {
			cellTable.setLoadingIndicator(new Image(ImageResources.INSTANCE.g_lock()));
		} else {
			cellTable.setLoadingIndicator(new Image(ImageResources.INSTANCE.g_lock()));
		}
	}

	public void addClickHandlers(){
		
		searchString.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchString.setText("");
			}
		});
	}
	

	@Override
	public PrimaryButton getSearchButton(){
		return searchButton;
	}
	

	@Override
	public Widget asWidget() {
		return focusPanel;
	}
	

	@Override
	public Widget asComponentMeasuresWidget() {
		return componentMeasuresListSPanel;
	}
	

	@Override
	public TextBox getShortName() {
		return abbrInput;
	}
	

	@Override
	public TextBox getMeasureScoring(){
		return measScoringInput;
	}
	

	@Override
	public HasValue<String> getSearchString(){
		return searchString;
	}
	

	@Override
	public MessageAlert getBottomErrorMessage() {
		return bottomErrorMessage;
	}
	
	@Override
	public MessageAlert getTopErrorMessage() {
		return topErrorMessage;
	}

	@Override
	public HasKeyDownHandlers getFocusPanel(){
		return focusPanel;
	}

	@Override
	public HasClickHandlers getSaveButtonHasClickHandlers() {
		return buttonBarBottom.getSaveButton();
	}

	@Override
	public TextBox getVersionNumber() {
		return versionInput;
	}

	@Override
	public HasValue<String> getSupplementalData() {
		return supplementalDataInput;
	}

	@Override
	public HasValue<String> getSetName() {
		return setNameInput;
	}

	@Override
	public HasValue<String> getMeasurePopulationExclusions() {
		return measurePopulationExclusionsInput;
	}

	@Override
	public TextBox geteMeasureIdentifier() {
		return eMeasureIdentifier;
	}

	@Override
	public HasValue<String> getNqfId() {
		return nQFIDInput;
	}
	
	@Override
	public TextBox getFinalizedDate() {
		return finalizedDate;
	}
	
	@Override
	public String getMeasureType() {
		return measureTypeInput.getValue(measureTypeInput.getSelectedIndex());
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
	public HasValue<String> getInitialPop() {
		return initialPopInput;
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
	public MessageAlert getBottomSuccessMessage() {
		return bottomSuccessMessage;
	}
	
	@Override
	public MessageAlert getTopSuccessMessage() {
		return topSuccessMessage;
	}

	private TextAreaWithMaxLength createReferenceInput() {
		TextAreaWithMaxLength newReferenceBox = new TextAreaWithMaxLength();
		newReferenceBox.getElement().setAttribute("id", "Reference");
		newReferenceBox.setSize("750px", "60px");
		newReferenceBox.setMaxLength(2000);
		newReferenceBox.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				clearErrorMsg();
			}
		});
		return newReferenceBox;
	}

	@Override
	public void  addRow(FlexTable reference) {
		TextAreaWithMaxLength newReferenceBox = createReferenceInput();
		newReferenceBox.setPlaceholder("Enter Reference");
		newReferenceBox.setTitle("Enter Reference");
		String dynamicLabel = "Reference" + referenceTable.getRowCount()+1;
		newReferenceBox.getElement().setId(dynamicLabel+"_TextAreaWithMaxLength");
		Button newremoveButton = new Button("Remove");
		newremoveButton.setId(dynamicLabel+"_RemoveButton");
		newremoveButton.setType(ButtonType.LINK);
		newremoveButton.setIcon(IconType.MINUS);
		newremoveButton.setTitle("Remove Reference");
		
		newremoveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearErrorMsg();
				com.google.gwt.user.client.ui.HTMLTable.Cell cell = referenceTable.getCellForEvent(event);
				int clickedRowIndex = cell.getRowIndex();
				removeRow(referenceTable, clickedRowIndex);
			}
		});
		int numRows = referenceTable.getRowCount();
		referenceTable.setWidget(numRows, 0, newReferenceBox);
		referenceTable.setWidget(numRows, 1, newremoveButton);
		newremoveButton.getElement().setId("newremoveButton"+numRows+"_Button");
		referenceArrayList.add(newReferenceBox);
	}

	private void removeRow(FlexTable reference, int rowIndex) {
		if (referenceTable.getWidget(rowIndex, 0) instanceof HorizontalPanel) {
			HorizontalPanel horizontalPanel =  (HorizontalPanel) referenceTable.getWidget(rowIndex, 0);
			TextAreaWithMaxLength areaWithMaxLength = (TextAreaWithMaxLength) horizontalPanel.getWidget(1);
			referenceArrayList.remove(areaWithMaxLength);
		} else if (referenceTable.getWidget(rowIndex, 0) instanceof TextAreaWithMaxLength) {
			referenceArrayList.remove(referenceTable.getWidget(rowIndex, 0));
		}
		referenceTable.removeRow(rowIndex);
	}

	@Override
	public List<String> getReferenceValues() {
		ArrayList<String> referenceValues = new ArrayList<String>();
		for (TextAreaWithMaxLength referenceBox: referenceArrayList) {
			referenceValues.add(referenceBox.getValue());
		}
		return referenceValues;
		
	}

	@Override
	public void setReferenceValues(List<String> values, boolean editable) {
		if ((values != null) && !values.isEmpty()) {
			clearReferences();
			for (int i = 0; i < values.size(); i++) {
				TextAreaWithMaxLength newReferenceBox = createReferenceInput();
				newReferenceBox.setValue(values.get(i));
				newReferenceBox.setEnabled(editable);
				newReferenceBox.setTitle("Reference");
				newReferenceBox.setPlaceholder("Enter Reference");
				if (i == 0) {
					referenceTable.setWidget(0, 0, newReferenceBox);
					VerticalPanel vp = new VerticalPanel();
					vp.getElement().setId("vp_1");
					vp.add(AddRowButton);
					
					referenceTable.setWidget(0, 1, vp);
				} else {
					referenceTable.setWidget(i, 0, newReferenceBox);
					if (editable) {
						Button newRemoveButton = new Button("Remove");
						newRemoveButton.setId("RemoveButton_"+i);
						newRemoveButton.setType(ButtonType.LINK);
						newRemoveButton.setIcon(IconType.MINUS);
						newRemoveButton.setTitle("Remove Reference");

						newRemoveButton.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								com.google.gwt.user.client.ui.HTMLTable.Cell cell = referenceTable.getCellForEvent(event);
								int clickedRowIndex = cell.getRowIndex();
								removeRow(referenceTable, clickedRowIndex);
							}
						});
						referenceTable.setWidget(i, 1, newRemoveButton);
					}
				}
				referenceArrayList.add(newReferenceBox);
			}
			referencePlaceHolder.add(referenceTable);
		} else if (values.isEmpty()) {
			clearReferences();
			TextAreaWithMaxLength newReferenceBox = createReferenceInput();
			newReferenceBox.setTitle("Enter Reference");
			newReferenceBox.setPlaceholder("Enter Reference");
			VerticalPanel vp = new VerticalPanel();
			vp.getElement().setId("vp_2");
			vp.add(AddRowButton);

			referenceTable.setWidget(0, 0, newReferenceBox);
			referenceTable.setWidget(0, 1, vp);
			referenceArrayList.add(newReferenceBox);
			referencePlaceHolder.add(referenceTable);
		}
	}

	private void buildReferenceTable(TextAreaWithMaxLength referenceInput) {
		clearReferences();
		referenceInput.setPlaceholder("Enter Reference");
		referenceInput.setTitle("Enter Reference");
		referenceTable.setWidget(0, 0, referenceInput);
		referenceArrayList.add(referenceInput);
		VerticalPanel vp = new VerticalPanel();
		vp.getElement().setId("vp_3");
		vp.add(AddRowButton);

		referenceTable.setWidget(0, 1, vp);
	}

	private void clearReferences() {
		referencePlaceHolder.clear();
		referenceTable.clear();
		referenceTable.removeAllRows();
		referenceArrayList.clear();
		FlexCellFormatter cellFormatter = referenceTable.getFlexCellFormatter();
		cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
	}

	@Override
	public DateBoxWithCalendar getMeasurementFromPeriodInputBox() {
		return measurePeriodFromInput;
	}

	@Override
	public DateBoxWithCalendar getMeasurementToPeriodInputBox() {
		return measurePeriodToInput;
	}
	
	@Override
	public HasValue<String> getEmeasureId(){
		return eMeasureIdentifierInput;
	}

	@Override
	public void setGenerateEmeasureIdButtonEnabled(boolean b) {
		generateeMeasureIDButton.setEnabled(b);
	}

	@Override
	public HasClickHandlers getGenerateEmeasureIdButton() {
		return generateeMeasureIDButton;
	}

	@Override
	public Button getBottomDeleteMeasureButton() {
		return buttonBarBottom.getDeleteButton();
	}

	@Override
	public void setAddEditButtonsVisible(boolean b) {
		measurePeriodFromInput.setEnableCSS(b);
		measurePeriodToInput.setEnableCSS(b);
		AddRowButton.setEnabled(b);
		
	}

	@Override
	public void setMeasurementPeriodButtonsVisible(boolean b){
		measurePeriodFromInput.getDateBox().setEnabled(b);
		measurePeriodToInput.getDateBox().setEnabled(b);
		measurePeriodFromInput.setEnableCSS(b);
		measurePeriodToInput.setEnableCSS(b);
	}
	
	private void resetEndorsedByListBox() {
		endorsedByListBox.clear();
		endorsedByListBox.insertItem("No", "false","No");
		endorsedByListBox.insertItem("Yes", "true","Yes");
		endorsedByListBox.setSelectedIndex(0);
		endorsedByListBox.setTitle("Endorsed By NQF List");
	}

	@Override
	public void setSaveButtonEnabled(boolean b) {
		buttonBarTop.getSaveButton().setEnabled(b);
		buttonBarBottom.getSaveButton().setEnabled(b);
	}

	@Override
	public WarningConfirmationMessageAlert getSaveErrorMsg() {
		return saveErrorDisplay;
	}
	
	@Override
	public Button getBottomSaveButton() {
		return buttonBarBottom.getSaveButton();
	}
	
	private void clearErrorMsg() {
		getSaveErrorMsg().clearAlert();
	}
	
	@Override
	public void setQdmSelectedList(List<QualityDataSetDTO> qdmSelectedList) {
		this.qdmSelectedList = qdmSelectedList;
	}
	
	@Override
	public List<QualityDataSetDTO> getQdmSelectedList() {
		return qdmSelectedList;
	}
	
	@Override
	public List<ManageMeasureSearchModel.Result> getComponentMeasureSelectedList() {
		return componentMeasureSelectedList;
	}
	
	@Override
	public void setComponentMeasureSelectedList(List<ManageMeasureSearchModel.Result> componentMeasureSelectedList) {
		this.componentMeasureSelectedList = componentMeasureSelectedList;
	}
	
	@Override
	public List<MeasureType> getMeasureTypeSelectedList() {
		return measureTypeSelectedList;
	}

	@Override
	public void setMeasureTypeSelectedList(List<MeasureType> measureTypeSelectedList) {
		this.measureTypeSelectedList = measureTypeSelectedList;
	}
	
	@Override
	public DialogBox getDialogBox() {
		return dialogBox;
	}
	
	private String convertTimestampToString(Timestamp ts) {
		String tsStr;
		if (ts == null) {
			tsStr = "";
		} else {
			int hours = ts.getHours();
			String ap = hours < 12 ? "AM" : "PM";
			int modhours = hours % 12;
			String mins = ts.getMinutes() + "";
			if (mins.length() == 1) {
				mins = "0" + mins;
			}
			String hoursStr = modhours == 0 ? "12" : modhours+"";
			tsStr = (ts.getMonth() + 1) + "/" + ts.getDate() + "/" + (ts.getYear() + 1900) + " "
					+ hoursStr + ":" + mins + " "+ap;
		}
		return tsStr;
	}
	
	@Override
	public List<Author> getAuthorsSelectedList() {
		return authorsSelectedList;
	}
	
	@Override
	public void setAuthorsSelectedList(List<Author> authorsSelectedList) {
		this.authorsSelectedList = authorsSelectedList;
	}
	
	@Override
	public void setStewardId(String id){
		stewardId = id;
	}
	
	@Override
	public String getStewardId() {
		return stewardId;
	}
	
	@Override
	public String getStewardValue() {
		return stewardValue;
	}
	
	@Override
	public void setStewardValue(String stewardValue){
		this.stewardValue = stewardValue;
	}
	
	private  ValueChangeHandler<Boolean> calenderYearChangeHandler = new ValueChangeHandler<Boolean>() {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			measurePeriodFromInput.setValue("");
			measurePeriodToInput.setValue("");
			bottomErrorMessage.clearAlert();
			topErrorMessage.clearAlert();
			if (calenderYear.getValue().equals(Boolean.FALSE)) {
				measurePeriodFromInput.setEnabled(true);
				measurePeriodToInput.setEnabled(true);
			} else {
				measurePeriodFromInput.setEnabled(false);
				measurePeriodToInput.setEnabled(false);
			}
		}
	};
	
	@Override
	public CheckBox getCalenderYear() {
		return calenderYear;
	}
	
	public String getMeasureScoringType() {
		return measureScoringType;
	}
	
	@Override
	public void setMeasureScoringType(String measureScoringType) {
		this.measureScoringType = measureScoringType;
	}
	
	@Override
	public ListBox getAuthorListBox() {
		return authorListBox;
	}
	
	@Override
	public ListBox getMeasureTypeListBox() {
		return measureTypeListBox;
	}
	
	@Override
	public Button getGenerateeMeasureIDButton() {
		return generateeMeasureIDButton;
	}
	
	@Override
	public TextBox getAbbrInput() {
		return abbrInput;
	}
	
	@Override
	public TextBox getPatientBasedInput() {
		return patientBasedInput;
	}
	
	@Override
	public TextBox getMeasScoringInput() {
		return measScoringInput;
	}
	
	@Override
	public TextAreaWithMaxLength getRationaleInput() {
		return rationaleInput;
	}
	
	@Override
	public TextBox getVersionInput() {
		return versionInput;
	}
	
	@Override
	public ListBoxMVP getAuthorInput() {
		return authorInput;
	}
	
	@Override
	public TextBox getMeasureStewardOtherInput() {
		return measureStewardOtherInput;
	}
	
	@Override
	public TextAreaWithMaxLength getDescriptionInput() {
		return descriptionInput;
	}
	
	@Override
	public TextAreaWithMaxLength getCopyrightInput() {
		return copyrightInput;
	}
	
	@Override
	public TextAreaWithMaxLength getDisclaimerInput() {
		return disclaimerInput;
	}
	
	@Override
	public TextAreaWithMaxLength getRiskAdjustmentInput() {
		return riskAdjustmentInput;
	}
	
	@Override
	public TextAreaWithMaxLength getRateAggregationInput() {
		return rateAggregationInput;
	}
	
	@Override
	public TextAreaWithMaxLength getInitialPopInput() {
		return initialPopInput;
	}
	
	@Override
	public TextAreaWithMaxLength getDenominatorInput() {
		return denominatorInput;
	}
	
	@Override
	public TextAreaWithMaxLength getDenominatorExclusionsInput() {
		return denominatorExclusionsInput;
	}
	
	@Override
	public TextAreaWithMaxLength getNumeratorInput() {
		return numeratorInput;
	}
	
	@Override
	public TextAreaWithMaxLength getNumeratorExclusionsInput() {
		return numeratorExclusionsInput;
	}
	
	@Override
	public TextAreaWithMaxLength getDenominatorExceptionsInput() {
		return denominatorExceptionsInput;
	}
	
	@Override
	public TextAreaWithMaxLength getMeasurePopulationInput() {
		return measurePopulationInput;
	}
	
	@Override
	public TextAreaWithMaxLength getMeasurePopulationExclusionsInput() {
		return measurePopulationExclusionsInput;
	}

	@Override
	public TextAreaWithMaxLength getMeasureObservationsInput() {
		return measureObservationsInput;
	}
	
	@Override
	public ListBoxMVP getMeasureTypeInput() {
		return measureTypeInput;
	}
	
	@Override
	public DateBoxWithCalendar getMeasurePeriodFromInput() {
		return measurePeriodFromInput;
	}
	
	@Override
	public DateBoxWithCalendar getMeasurePeriodToInput() {
		return measurePeriodToInput;
	}
	
	@Override
	public TextAreaWithMaxLength getSupplementalDataInput() {
		return supplementalDataInput;
	}
	
	@Override
	public TextBox getCodeSystemVersionInput() {
		return codeSystemVersionInput;
	}

	@Override
	public TextBox getNQFIDInput() {
		return nQFIDInput;
	}
	
	@Override
	public TextAreaWithMaxLength getSetNameInput() {
		return setNameInput;
	}
	
	@Override
	public TextAreaWithMaxLength getClinicalStmtInput() {
		return clinicalStmtInput;
	}
	
	@Override
	public TextAreaWithMaxLength getImprovementNotationInput() {
		return improvementNotationInput;
	}
	
	@Override
	public TextAreaWithMaxLength getReferenceInput() {
		return referenceInput;
	}
	
	@Override
	public TextAreaWithMaxLength getDefinitionsInput() {
		return definitionsInput;
	}
	
	@Override
	public TextAreaWithMaxLength getTransmissionFormatInput() {
		return transmissionFormatInput;
	}
	
	@Override
	public TextAreaWithMaxLength getStratificationInput() {
		return stratificationInput;
	}
	
	@Override
	public void setOptionsInStewardList(List<MeasureSteward> allStewardList, boolean editable) {
		int i=1;
		getStewardListBox().clear();
		getStewardListBox().addItem("--Select--");
		getStewardListBox().setSelectedIndex(i);
		for(MeasureSteward m : allStewardList){
			getStewardListBox().insertItem(m.getOrgName(), m.getId(), m.getOrgOid());
			if(getStewardId() != null){
				if(m.getId().equals(getStewardId())){
					getStewardListBox().setSelectedIndex(i);
				}
			}
			i= i+1;
		}
	}
	
	@Override
	public TextAreaWithMaxLength getGuidanceInput() {
		return guidanceInput;
	}
	
	@Override
	public Button getAddRowButton() {
		return AddRowButton;
	}
	
	@Override
	public FlexTable getReferenceTable() {
		return referenceTable;
	}

	@Override
	public ListBoxMVP getStewardListBox() {
		return stewardListBox;
	}

	public void setStewardListBox(ListBoxMVP stewardListBox) {
		this.stewardListBox = stewardListBox;
	}

	@Override
	public ListBoxMVP getEndorsedByListBox() {
		return endorsedByListBox;
	}

	public void setEndorsedByListBox(ListBoxMVP endorsedByListBox) {
		this.endorsedByListBox = endorsedByListBox;
	}

	@Override
	public Button getTopSaveButton() {
		return buttonBarTop.getSaveButton();
	}

	@Override
	public Button getTopDeleteMeasureButton() {
		return buttonBarTop.getDeleteButton();
	}

	public boolean isPatientBasedMeasure() {
		return isPatientBasedMeasure;
	}
	
	@Override
	public void setPatientBasedMeasure(boolean isPatientBasedMeasure) {
		this.isPatientBasedMeasure = isPatientBasedMeasure;
	}

	@Override
	public HelpBlock getHelpBlock() {
		return helpBlock;
	}
}
