package mat.client.measure.metadata;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import mat.client.ImageResources;
import mat.client.clause.QDSAppliedListModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.PrimaryButton;
import mat.client.shared.RadioButtonCell;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.TextAreaWithMaxLength;
import mat.client.util.CellTableUtility;
import mat.model.Author;
import mat.model.MeasureSteward;
import mat.model.MeasureType;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.dom.client.Element;
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
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

// TODO: Auto-generated Javadoc
/**
 * The Class MetaDataView.
 */
public class MetaDataView implements MetaDataDetailDisplay{
	
	/** The main panel. */
	protected FlowPanel mainPanel = new FlowPanel();
	
	/** The focus panel. */
	protected FocusPanel focusPanel = new FocusPanel();
	
	/** The cell table panel. */
	protected FlowPanel cellTablePanel=new  FlowPanel();
	
	/** The success messages. */
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
	/** The abbr input. */
	protected Label abbrInput = new Label();
	
	/** The patient based input */
	protected Label patientBasedInput = new Label(); 
	
	/** The meas scoring input. */
	protected Label measScoringInput = new Label();
	
	/** The finalized date. */
	protected Label finalizedDate = new Label();
	
	/** The rationale input. */
	protected TextAreaWithMaxLength rationaleInput = new TextAreaWithMaxLength();
	
	/** The version input. */
	protected Label versionInput = new Label();
	
	/** The author input. */
	protected ListBoxMVP authorInput = new ListBoxMVP();
	
	/** The empty authors panel. */
	protected SimplePanel emptyAuthorsPanel = new SimplePanel();
	
	/** The author list box. */
	protected ListBox authorListBox =new ListBox();
	
	/** The measure type list box. */
	protected ListBox measureTypeListBox = new ListBox();
	
	/** The measure steward input. */
	//protected ListBoxMVP measureStewardInput = new ListBoxMVP(false);
	
	//US 413. Added panel and input box for Steward Other option.
	/** The empty text box holder. */
	protected VerticalPanel emptyTextBoxHolder = new VerticalPanel();
	
	/** The qdm item count list v panel. */
	protected VerticalPanel qdmItemCountListVPanel = new VerticalPanel();
	
	/** The component measures list v panel. */
	protected VerticalPanel componentMeasuresListVPanel = new VerticalPanel();
	
	/** The qdm item count list s panel. */
	protected ScrollPanel qdmItemCountListSPanel = new ScrollPanel();
	
	/** The component measures list s panel. */
	protected ScrollPanel componentMeasuresListSPanel = new ScrollPanel();
	
	/** The measure type s panel. */
	protected ScrollPanel measureTypeSPanel = new ScrollPanel();
	
	/** The author s panel. */
	protected ScrollPanel authorSPanel = new ScrollPanel();
	
	/** The steward s panel. */
	protected ScrollPanel stewardSPanel = new ScrollPanel();
	
	/** The measure steward other input. */
	protected TextBox measureStewardOtherInput = new TextBox();
	
	/** The description input. */
	protected TextAreaWithMaxLength  descriptionInput = new TextAreaWithMaxLength ();
	
	/** The copyright input. */
	protected TextAreaWithMaxLength  copyrightInput = new TextAreaWithMaxLength ();
	
	/** The disclaimer input. */
	protected TextAreaWithMaxLength  disclaimerInput = new TextAreaWithMaxLength ();
	
	/** The stratification input. */
	protected TextAreaWithMaxLength  stratificationInput = new TextAreaWithMaxLength ();
	
	/** The risk adjustment input. */
	protected TextAreaWithMaxLength  riskAdjustmentInput  = new TextAreaWithMaxLength ();
	
	/** The rate aggregation input. */
	protected TextAreaWithMaxLength  rateAggregationInput  = new TextAreaWithMaxLength ();
	
	/** The initial patient pop input. */
	protected TextAreaWithMaxLength  initialPopInput  = new TextAreaWithMaxLength ();
	
	/** The denominator input. */
	protected TextAreaWithMaxLength  denominatorInput = new TextAreaWithMaxLength ();
	
	/** The denominator exclusions input. */
	protected TextAreaWithMaxLength  denominatorExclusionsInput  = new TextAreaWithMaxLength ();
	
	/** The numerator input. */
	protected TextAreaWithMaxLength  numeratorInput = new TextAreaWithMaxLength ();
	
	/** The numerator exclusions input. */
	protected TextAreaWithMaxLength  numeratorExclusionsInput = new TextAreaWithMaxLength ();
	
	/** The denominator exceptions input. */
	protected TextAreaWithMaxLength  denominatorExceptionsInput = new TextAreaWithMaxLength ();
	
	/** The measure population input. */
	protected TextAreaWithMaxLength  measurePopulationInput = new TextAreaWithMaxLength ();
	
	/** The measure population exclusions input. */
	protected TextAreaWithMaxLength  measurePopulationExclusionsInput = new TextAreaWithMaxLength ();
	
	/** The measure observations input. */
	protected TextAreaWithMaxLength  measureObservationsInput = new TextAreaWithMaxLength ();
	
	/** The measure type input. */
	protected ListBoxMVP measureTypeInput = new ListBoxMVP();
	
	/** The empty measure type panel. */
	protected SimplePanel emptyMeasureTypePanel = new SimplePanel();
	
	/** The error messages. */
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The measure period from input. */
	protected DateBoxWithCalendar measurePeriodFromInput = new DateBoxWithCalendar();
	
	/** The measure period to input. */
	protected DateBoxWithCalendar measurePeriodToInput = new DateBoxWithCalendar();
	
	/** The supplemental data input. */
	protected TextAreaWithMaxLength  supplementalDataInput  = new TextAreaWithMaxLength ();
	
	/** The code system version input. */
	protected TextBox codeSystemVersionInput = new TextBox();
	
	/** The NQFID input. */
	protected TextBox NQFIDInput = new TextBox();
		
	/** The set name input. */
	protected TextAreaWithMaxLength setNameInput = new TextAreaWithMaxLength();
	
	/** The e measure identifier input. */
	protected TextBox eMeasureIdentifierInput = new TextBox();
	
	/** The e measure identifier. */
	protected Label eMeasureIdentifier  = new Label();
	
	/** The endorsed by nqf. */
	protected Label endorsedByNQF = new Label();
	
	/** The item label. */
	protected Label itemLabel = new Label();
	
	/** The component measures label. */
	protected Label componentMeasuresLabel = new Label();
	
	/** The counter. */
	private int counter = 0;
	
	/** The No. */
	protected RadioButton No = new RadioButton("NQFgroup","No");
	
	/** The Yes. */
	protected RadioButton Yes = new RadioButton("NQFgroup","Yes");
	
	/** The clinical stmt input. */
	protected TextAreaWithMaxLength  clinicalStmtInput = new TextAreaWithMaxLength ();
	
	/** The improvement notation input. */
	protected TextAreaWithMaxLength  improvementNotationInput = new TextAreaWithMaxLength ();
	
	/** The reference input. */
	protected TextAreaWithMaxLength  referenceInput = createReferenceInput();
	
	/** The definitions input. */
	protected TextAreaWithMaxLength  definitionsInput = new TextAreaWithMaxLength ();
	
	/** The guidance input. */
	protected TextAreaWithMaxLength guidanceInput = new TextAreaWithMaxLength();
	
	/** The transmission format input. */
	protected TextAreaWithMaxLength transmissionFormatInput = new TextAreaWithMaxLength();
	
	/** The add edit measure type. */
	private Button addEditMeasureType = new PrimaryButton("Add/Edit Measure Type","primaryMetaDataButton");
	
	/** The add edit cmponent measures. */
	private Button addEditCmponentMeasures = new PrimaryButton("Add/Edit Component Measures","primaryMetaDataButton");
	
	/** The Add row button. */
	private Button AddRowButton = new PrimaryButton("Add Reference","primaryGreyLeftButton");
	
	/** The save button. */
	private Button saveButton = new PrimaryButton("Save","primaryButton");
	
	/** The generatee measure id button. */
	private Button generateeMeasureIDButton = new SecondaryButton("Generate Identifier");
	
	/** The delete measure. */
	private Button deleteMeasure = new SecondaryButton("Delete Measure");
	
	/** The reference array list. */
	private ArrayList<TextAreaWithMaxLength> referenceArrayList = new ArrayList<TextAreaWithMaxLength>();
	
	/** The reference place holder. */
	private SimplePanel referencePlaceHolder = new SimplePanel();
	
	/** The reference table. */
	private final FlexTable referenceTable = new FlexTable();
	
	
	/** The save error display. */
	private ErrorMessageDisplay saveErrorDisplay = new ErrorMessageDisplay();
	
	/** The selection model. */
	private MultiSelectionModel<QualityDataSetDTO> selectionModel;
	
	/** The measure selection model. */
	private MultiSelectionModel<ManageMeasureSearchModel.Result> measureSelectionModel;
	
	/** The cell table. */
	private CellTable<QualityDataSetDTO> cellTable;
	
	/** The horz panel. */
	private HorizontalPanel horzPanel = new HorizontalPanel();
	
	/** The horz component measure panel. */
	private HorizontalPanel horzComponentMeasurePanel = new HorizontalPanel();
	
	/** The qdm selected list v panel. */
	VerticalPanel qdmSelectedListVPanel=new VerticalPanel();
	
	/** The component measures selected list v panel. */
	VerticalPanel componentMeasuresSelectedListVPanel = new VerticalPanel();
	
	/** The component measures selected list v panel. */
	HorizontalPanel topHPanel = new HorizontalPanel();
	
	/** The component measures selected list v panel. */
	HorizontalPanel bottomHPanel = new HorizontalPanel();
	
	/** The qdm selected list s panel. */
	ScrollPanel qdmSelectedListSPanel=new ScrollPanel();
	
	/** The vertical panel. */
	VerticalPanel vPanel=new VerticalPanel();
	
	/** The qdm selected list. */
	private  List<QualityDataSetDTO> qdmSelectedList;
	
	/** The search string. */
	private TextBox searchString = new TextBox();
	
	/** The component measure selected list. */
	private List<ManageMeasureSearchModel.Result> componentMeasureSelectedList;
	
	/** The element. */
	private  Element element;
	
	/** The component measure cell table. */
	private CellTable<ManageMeasureSearchModel.Result> componentMeasureCellTable;
	
	/** The measure type cell table. */
	private CellTable<MeasureType> measureTypeCellTable;
	
	/** The author cell table. */
	private CellTable<Author> authorCellTable;
	
	/** The steward cell table. */
	private CellTable<MeasureSteward> stewardCellTable;
	
	/** The selected measure list. */
	private List<ManageMeasureSearchModel.Result> selectedMeasureList;
	
	/** The component measures list panel. */
	VerticalPanel componentMeasuresListPanel = new VerticalPanel();
	
	/** The measures list selection model. */
	private MultiSelectionModel<ManageMeasureSearchModel.Result> measuresListSelectionModel;
	
	/** The measure type selection model. */
	private MultiSelectionModel<MeasureType> measureTypeSelectioModel;
	
	/** The author selection model. */
	private MultiSelectionModel<Author> authorSelectionModel;
	
	/** The steward selection model. */
	private SingleSelectionModel<MeasureSteward> stewardSelectionModel;
	
	// private MatButtonCell searchButton = new MatButtonCell("click to Search Measures","customSearchButton");
	
	/** The search button. */
	private PrimaryButton searchButton = new PrimaryButton("Go");
	
	/** The dialog box. */
	private static DialogBox dialogBox = new DialogBox(true,true);
	
	/** The measure type selected list. */
	private List<MeasureType> measureTypeSelectedList = new ArrayList<MeasureType>();
	
	/** The authors selected list. */
	private List<Author> authorsSelectedList = new ArrayList<Author>();
	
	/** The steward id. */
	private String stewardId;
	
	/** The steward value. */
	private String stewardValue;
	
	/** The calender year. */
//	private CustomCheckBox calenderYear = new CustomCheckBox("Calendar Year", "Calendar Year", true);
	private CheckBox calenderYear = new CheckBox();
	
	//private ToggleSwitch  calenderYear = new ToggleSwitch();
	
	private String measureScoringType ;
	/**
	 * Instantiates a new meta data view.
	 */
	public MetaDataView(){
		addClickHandlers();
		searchString.setHeight("20px");
		VerticalPanel mainContent = new VerticalPanel();
		mainContent.getElement().setId("mainContent_VerticalPanel");
		mainPanel.setStylePrimaryName("searchResultsContainer");
		mainPanel.addStyleName("leftAligned");
		mainPanel.getElement().setId("mainPanel_FlowPanel01");
		/*buildForm();*/
		topHPanel.setHeight("25%");
		bottomHPanel.setHeight("75%");
		mainContent.add(topHPanel);
		mainContent.add(bottomHPanel);
		
		mainPanel.add(saveErrorDisplay);
		mainPanel.add(mainContent);
		mainPanel.setStyleName("contentPanel");
		DOM.setElementAttribute(mainPanel.getElement(), "id", "MetaDataView.containerPanel");
		focusPanel.add(mainPanel);
		focusPanel.getElement().setId("focusPanel_FocusPanel01");
	}
	
	
	/**
	 * Builds the left side form.
	 * 
	 * @return the widget
	 */
	@Override
	public void buildForm() {
		topHPanel.clear();
		bottomHPanel.clear();
		authorListBox.setVisibleItemCount(5);
		//authorListBox.addChangeHandler(changeHandler);
		authorListBox.getElement().setId("authorListBox_ListBox");
		
		measureTypeListBox.setVisibleItemCount(5);
		//measureTypeListBox.addChangeHandler(changeHandler);
		measureTypeListBox.getElement().setId("measureTypeListBox_ListBox");
		
		
		/** The panel for the top left side of screen */
		VerticalPanel topLeftSidePanel = new VerticalPanel();
		
		/** The panel for the top right side of screen */
		VerticalPanel topRightSidePanel = new VerticalPanel();
		
		
		topHPanel.getElement().setId("mainPanel_HPanelTop");
		bottomHPanel.getElement().setId("mainPanel_HPanelBottom");
		topRightSidePanel.getElement().setId("mainPanel_VTopRight");
		topLeftSidePanel.getElement().setId("mainPanel_VTopLeft");
		FlowPanel fPanel = new FlowPanel();
		topHPanel.add(topLeftSidePanel);
		topHPanel.add(topRightSidePanel);
		
		bottomHPanel.add(fPanel);
		
		fPanel.setStyleName("leftSideForm");
		fPanel.getElement().setId("fPanel_FlowPanelLeft");
		fPanel.add(new SpacerWidget());
		fPanel.add(errorMessages);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setAttribute("id", "emeasureTitlePanel");
		hp.add(new SpacerWidget());
		hp.add(new SpacerWidget());
		hp.add(deleteMeasure);
		deleteMeasure.getElement().setId("deleteMeasure_Button");
		hp.setStylePrimaryName("floatRightButtonPanel");
		topRightSidePanel.add(new SpacerWidget());
		topRightSidePanel.add(new SpacerWidget());
		topRightSidePanel.add(hp);
		topRightSidePanel.add(new SpacerWidget());
		topRightSidePanel.add(new SpacerWidget());
		topRightSidePanel.add(new SpacerWidget());
		
		// create a panel to display read-only data in disclosrePanel
		topRightSidePanel.setSize("300px", "100px");
		topRightSidePanel.setStyleName("rightSideForm");
		
		// create a panel to display the general info
		VerticalPanel generalPanel = new VerticalPanel();
		generalPanel.setSize("270px", "232px");
		generalPanel.setStyleName("measureDetailRightPanel");
		generalPanel.getElement().setId("generalPanel");
		
		// General Information Label
		Label generalLabel = new Label("General Information");
		generalLabel.setStyleName("measurementPeriodHeader");
		generalLabel.getElement().setId("generalInfoHeader_Label");
		generalLabel.getElement().setAttribute("tabIndex", "0");
		generalPanel.add(generalLabel);
		
		//US 421. Measure Scoring choice is now part of Measure creation process. So just display here.
		Label measScoringInputLabel = (Label) LabelBuilder.buildLabel(measScoringInput, "Measure Scoring");
		measScoringInputLabel.getElement().setId("MeasScoringLabel");
		measScoringInputLabel.setStyleName("marginLeft20pxBold");
		measScoringInputLabel.setTitle(measScoringInputLabel.getText());
		generalPanel.add(measScoringInputLabel);
		
		measScoringInput.setStyleName("marginLeft20px");
		measScoringInput.getElement().setId("MeasScoringValue");
		generalPanel.add(measScoringInput);
		generalPanel.add(new SpacerWidget());
		
		// MAT-8616 Add patient based measure field to Measure Details > General Information Section
		Label patientBasedLabel = (Label) LabelBuilder.buildLabel(patientBasedInput, "Patient-based Measure");
		patientBasedLabel.setStyleName("marginLeft20pxBold");
		patientBasedLabel.setTitle(patientBasedLabel.getText());
		generalPanel.add(patientBasedLabel);
		
		patientBasedInput.getElement().setId("abbrInput");
		patientBasedInput.setStyleName("marginLeft20px");
		generalPanel.add(patientBasedInput);
		generalPanel.add(new SpacerWidget());
		
		Label abbrInputLabel =  (Label) LabelBuilder.buildLabel(abbrInput, "eCQM Abbreviated Title");
		abbrInputLabel.setStyleName("marginLeft20pxBold");
		abbrInputLabel.setTitle(abbrInputLabel.getText());
		generalPanel.add(abbrInputLabel);
		
		abbrInput.getElement().setId("abbrInput");
		abbrInput.setStyleName("marginLeft20px");
		generalPanel.add(abbrInput);
		generalPanel.add(new SpacerWidget());
		
		Label finalizedDateLabel = (Label) LabelBuilder.buildLabel(finalizedDate, "Finalized Date");
		finalizedDateLabel.setStyleName("marginLeft20pxBold");
		finalizedDateLabel.setTitle(finalizedDateLabel.getText());
		generalPanel.add(finalizedDateLabel);
		
		finalizedDate.getElement().setId("finalizedDate");
		finalizedDate.setStyleName("marginLeft20px");
		generalPanel.add(finalizedDate);
		generalPanel.add(new SpacerWidget());
		
		Label eMeasureIdentifierLabel = (Label) LabelBuilder.buildLabel(eMeasureIdentifier, "GUID");
		eMeasureIdentifierLabel.setStyleName("marginLeft20pxBold");
		eMeasureIdentifierLabel.setTitle(eMeasureIdentifierLabel.getText());
		generalPanel.add(eMeasureIdentifierLabel);
		
		eMeasureIdentifier.getElement().setId("eMeasureIdentifier");
		eMeasureIdentifier.setStyleName("marginLeft20px");
		generalPanel.add(eMeasureIdentifier);
		generalPanel.add(new SpacerWidget());
		
		Label versionInputLabel = (Label) LabelBuilder.buildLabel(versionInput, "eCQM Version Number");
		versionInputLabel.setStyleName("marginLeft20pxBold");
		versionInputLabel.setTitle(versionInputLabel.getText());
		generalPanel.add(versionInputLabel);
		
		versionInput.getElement().setId("versionInput");
		versionInput.setStyleName("marginLeft20px");
		generalPanel.add(versionInput);
		
		//measurementPeriod Header
		topRightSidePanel.add(new SpacerWidget());
		topRightSidePanel.add(new SpacerWidget());
		topRightSidePanel.add(new SpacerWidget());
		topRightSidePanel.add(generalPanel);
		
		fPanel.add(new SpacerWidget());
		
		HorizontalFlowPanel horizontalPanel = new HorizontalFlowPanel();
		horizontalPanel.getElement().setId("horizontalPanel_HorizontalFlowPanelLeft");
		Label eMeasureIdentifierInputLabel = (Label) LabelBuilder.buildLabel(eMeasureIdentifierInput, "eCQM Identifier (Measure Authoring Tool)");
		eMeasureIdentifierInputLabel.setStyleName("bold");
		topLeftSidePanel.add(new SpacerWidget());
		topLeftSidePanel.add(eMeasureIdentifierInputLabel);
		//Widget optionLabelWidget = LabelBuilder.buildLabel(eMeasureIdentifierInput, " - Optional");
		//optionLabelWidget.setStyleName("generate-emeasureid-button");
		eMeasureIdentifierInput.getElement().setId("eMeasureIdentifierInput_TextBox");
		//horizontalPanel.add(optionLabelWidget);
		
		horizontalPanel.add(eMeasureIdentifierInput);
		horizontalPanel.add(generateeMeasureIDButton);
		generateeMeasureIDButton.getElement().setId("generateeMeasureIDButton_Button");
		//generateeMeasureIDButton.addClickHandler(clickHandler);
		generateeMeasureIDButton.getElement().setId("generateeMeasureIDButton_Button");
		topLeftSidePanel.add(horizontalPanel);
		topLeftSidePanel.add(new SpacerWidget());
		
		// MAT 2995 : Commented Measure Status Field from Measure Detail View.
		/*fPanel.add(LabelBuilder.buildLabel(objectStatusInput, "Measure Status"));
		fPanel.add(objectStatusInput);
		objectStatusInput.addChangeHandler(changeHandler);
		fPanel.add(new SpacerWidget());*/
		
		Label nQFIDInputLabel = (Label) LabelBuilder.buildLabel(NQFIDInput, "NQF Number");
		nQFIDInputLabel.setStyleName("bold");
		topLeftSidePanel.add(nQFIDInputLabel);
		topLeftSidePanel.add(new SpacerWidget());
		topLeftSidePanel.add(NQFIDInput);
		NQFIDInput.getElement().setId("NQFIDInput_TextBox");
		//NQFIDInput.addKeyDownHandler(keyDownHandler);
		topLeftSidePanel.add(new SpacerWidget());
	
		VerticalPanel measurementPeriodPanel = new VerticalPanel();
		measurementPeriodPanel.getElement().setId("measurementPeriod_VerticalPanel");
		measurementPeriodPanel.setStyleName("valueSetSearchPanel");
		measurementPeriodPanel.setSize("505px", "100px");
		//measurementPeriod Header
		Label measurePeriodFromInputLabel = new Label("Measurement Period");
		measurePeriodFromInputLabel.setStyleName("measurementPeriodHeader");
		measurePeriodFromInputLabel.getElement().setId("measurementPeriodHeader_Label");
		measurePeriodFromInputLabel.getElement().setAttribute("tabIndex", "0");
		measurementPeriodPanel.add(measurePeriodFromInputLabel);
		//measurementPeriodPanel.add(new SpacerWidget());
		HorizontalPanel calenderYearDatePanel = new HorizontalPanel();
		calenderYearDatePanel.getElement().setId("calenderYear_HorizontalPanel");
		calenderYearDatePanel.add(calenderYear);
		Label calenderLabel = new Label("Calendar Year");
		calenderLabel.setStyleName("qdmLabel");
		Label calenderYearLabel = new Label("(January 1,20XX through December 31,20XX)");
		calenderYearLabel.setStyleName("qdmLabel");
		calenderYearDatePanel.add(calenderLabel);
		calenderYearDatePanel.add(calenderYearLabel);
		calenderYear.getElement().setId("calenderYear_CustomCheckBox");
		calenderYear.addValueChangeHandler(calenderYearChangeHandler);
		calenderYearDatePanel.addStyleName("marginTop");
		HorizontalPanel measurePeriodPanel = new HorizontalPanel();
		measurePeriodPanel.getElement().setId("measurePeriodPanel_HorizontalPanel");
		Label fromLabel = new Label("From");
		fromLabel.addStyleName("firstLabel");
		measurePeriodPanel.add(fromLabel);
		measurePeriodFromInput.getDateBox().getElement().setAttribute("id", "measurePeriodFromInput");
		measurePeriodFromInput.getDateBox().setWidth("127px");
		measurePeriodPanel.add(measurePeriodFromInput);
		measurePeriodFromInput.getElement().setId("measurePeriodFromInput_DateBoxWithCalendar");
		Label toLabel = new Label("To");
		toLabel.addStyleName("secondLabel");
		measurePeriodPanel.add(toLabel);
		measurePeriodToInput.getDateBox().setWidth("127px");
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
		topLeftSidePanel.add(measurementPeriodPanel);
		topLeftSidePanel.add(new SpacerWidget());
		
		Label stewardTableLabel = (Label) LabelBuilder.buildLabel(stewardCellTable, "Measure Steward List");
		stewardTableLabel.setStyleName("measureDetailTableHeader");
		topLeftSidePanel.add(stewardTableLabel);
		topLeftSidePanel.add(stewardSPanel);
		
		Label authorTableLabel = (Label) LabelBuilder.buildLabel(authorCellTable, "Measure Developer List");
		authorTableLabel.setStyleName("measureDetailTableHeader");
		fPanel.add(authorTableLabel);
		fPanel.add(authorSPanel);
		fPanel.add(new SpacerWidget());
		
		Label endorsedByNQFLabel = (Label) LabelBuilder.buildLabel(endorsedByNQF, "Endorsed By NQF");
		endorsedByNQFLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(endorsedByNQFLabel);
		endorsedByNQF.getElement().setId("endorsedByNQF_Label");
		fPanel.add(wrapRadioButton(No));
		No.getElement().setId("No_RadioButton");
		fPanel.add(wrapRadioButton(Yes));
		Yes.getElement().setId("Yes_RadioButton");
		fPanel.add(new SpacerWidget());
		
		Label descriptionInputLabel = (Label) LabelBuilder.buildLabel(descriptionInput, "Description");
		descriptionInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(descriptionInputLabel);
		fPanel.add(descriptionInput);
		descriptionInput.getElement().setId("descriptionInput_TextAreaWithMaxLength");
		//descriptionInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		Label copyrightInputLabel = (Label) LabelBuilder.buildLabel(copyrightInput, "Copyright");
		copyrightInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(copyrightInputLabel);
		fPanel.add(copyrightInput);
		copyrightInput.getElement().setId("copyrightInput_TextAreaWithMaxLength");
		//copyrightInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		//Disclaimer
		Label disclaimerInputLabel = (Label) LabelBuilder.buildLabel(disclaimerInput, "Disclaimer");
		disclaimerInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(disclaimerInputLabel);
		fPanel.add(disclaimerInput);
		disclaimerInput.getElement().setId("disclaimerInput_TextAreaWithMaxLength");
		//disclaimerInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		Label measureTypeTableLabel = (Label) LabelBuilder.buildLabel(measureTypeCellTable, "Measure Type List");
		measureTypeTableLabel.setStyleName("measureDetailTableHeader");
		fPanel.add(measureTypeTableLabel);
		fPanel.add(measureTypeSPanel);
		//fPanel.add(addEditMeasureType);
		fPanel.add(new SpacerWidget());
	
		Label CompMeasureTableLabel = (Label) LabelBuilder.buildLabel(componentMeasureCellTable, " Component Measures List");
		CompMeasureTableLabel.setStyleName("measureDetailTableHeader");
		fPanel.add(CompMeasureTableLabel);
		fPanel.add(horzComponentMeasurePanel);
		fPanel.add(new SpacerWidget());
		fPanel.add(addEditCmponentMeasures);
		addEditCmponentMeasures.getElement().setId("addEditCmponentMeasures_Button");
		fPanel.add(new SpacerWidget());
		
		Label stratificationInputLabel = (Label) LabelBuilder.buildLabel(stratificationInput , "Stratification");
		stratificationInputLabel.setStyleName("measureDetailLabelStyle");
		
		stratificationInput.getElement().setId("stratificationInput_TextAreaWithMaxLength");
		//stratificationInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(stratificationInputLabel);
		fPanel.add(stratificationInput);
		fPanel.add(new SpacerWidget());
		
		Label riskAdjInputLabel = (Label) LabelBuilder.buildLabel(riskAdjustmentInput, "Risk Adjustment");
		riskAdjInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(riskAdjInputLabel);
		fPanel.add(riskAdjustmentInput);
		riskAdjustmentInput.getElement().setId("riskAdjustmentInput_TextAreaWithMaxLength");
		//riskAdjustmentInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		//Rate Aggregation riskAggregationInput
		Label riskAggInputLabel = (Label) LabelBuilder.buildLabel(rateAggregationInput, "Rate Aggregation");
		riskAggInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(riskAggInputLabel);
		fPanel.add(rateAggregationInput);
		rateAggregationInput.getElement().setId("rateAggregationInput_TextAreaWithMaxLength");
		//rateAggregationInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		Label rationaleInputLabel = (Label) LabelBuilder.buildLabel(rationaleInput, "Rationale");
		rationaleInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(rationaleInputLabel);
		fPanel.add(rationaleInput);
		rationaleInput.getElement().setId("rationaleInput_TextAreaWithMaxLength");
		//rationaleInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		Label clinicalStmtInputLabel = (Label)LabelBuilder.buildLabel(clinicalStmtInput, "Clinical Recommendation Statement");
		clinicalStmtInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(clinicalStmtInputLabel);
		fPanel.add(clinicalStmtInput);
		clinicalStmtInput.getElement().setId("clinicalStmtInput_TextAreaWithMaxLength");
		//clinicalStmtInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		Label improvementNotationInputLabel = (Label)LabelBuilder.buildLabel(improvementNotationInput, "Improvement Notation");
		improvementNotationInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(improvementNotationInputLabel);
		fPanel.add(improvementNotationInput);
		improvementNotationInput.getElement().setId("improvementNotationInput_TextAreaWithMaxLength");
		//improvementNotationInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		referenceInput.setSize("500px", "100px");
		referenceInput.setMaxLength(2000);
		//referenceInput.addKeyDownHandler(keyDownHandler);
		buildReferenceTable(referenceInput);
		referencePlaceHolder.add(referenceTable);
		Label referencePlaceHolderInputLabel = (Label)LabelBuilder.buildLabel(referencePlaceHolder, "Reference(s)");
		referencePlaceHolderInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(referencePlaceHolderInputLabel);
		fPanel.add(referencePlaceHolder);
		referencePlaceHolder.getElement().setId("referencePlaceHolder_SimplePanel");
		//fPanel.add(new SpacerWidget());
		
		Label definitionsInputLabel = (Label) LabelBuilder.buildLabel(definitionsInput, "Definition");
		definitionsInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(definitionsInputLabel);
		fPanel.add(definitionsInput);
		definitionsInput.getElement().setId("definitionsInput_TextAreaWithMaxLength");
		//definitionsInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		Label guidanceInputLabel = (Label) LabelBuilder.buildLabel(guidanceInput, "Guidance");
		guidanceInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(guidanceInputLabel);
		fPanel.add(guidanceInput);
		guidanceInput.getElement().setId("guidanceInput_TextAreaWithMaxLength");
		//guidanceInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		Label transmissionFormatInputLabel = (Label) LabelBuilder.buildLabel(transmissionFormatInput, "Transmission Format");
		transmissionFormatInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(transmissionFormatInputLabel);
		fPanel.add(transmissionFormatInput);
		transmissionFormatInput.getElement().setId("transmissionFormatInput_TextAreaWithMaxLength");
		//transmissionFormatInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		Label initialPopInputLabel = (Label) LabelBuilder.buildLabel(initialPopInput, "Initial Population");
		initialPopInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(initialPopInputLabel);
		fPanel.add(initialPopInput);
		initialPopInput.getElement().setId("initialPopInput_TextAreaWithMaxLength");
		//initialPopInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		if ((measureScoringType != null) && (measureScoringType.equalsIgnoreCase("Ratio")
				|| measureScoringType.equalsIgnoreCase("Proportion"))) {
			Label denoInputLabel = (Label) LabelBuilder.buildLabel(denominatorInput, "Denominator");
			denoInputLabel.setStyleName("measureDetailLabelStyle");
			fPanel.add(denoInputLabel);
			//fPanel.add(new SpacerWidget());
			fPanel.add(denominatorInput);
			denominatorInput.getElement().setId("denominatorInput_TextAreaWithMaxLength");
			//	denominatorInput.addKeyDownHandler(keyDownHandler);
			fPanel.add(new SpacerWidget());
			
			Label denoExclInputLabel = (Label) LabelBuilder.buildLabel(denominatorExclusionsInput, "Denominator Exclusions");
			denoExclInputLabel.setStyleName("measureDetailLabelStyle");
			fPanel.add(denoExclInputLabel);
			//fPanel.add(new SpacerWidget());
			fPanel.add(denominatorExclusionsInput);
			denominatorExclusionsInput.getElement().setId("denominatorExclusionsInput_TextAreaWithMaxLength");
			//	denominatorExclusionsInput.addKeyDownHandler(keyDownHandler);
			fPanel.add(new SpacerWidget());
			
			Label numInputLabel = (Label) LabelBuilder.buildLabel(numeratorInput, "Numerator");
			numInputLabel.setStyleName("measureDetailLabelStyle");
			fPanel.add(numInputLabel);
			//fPanel.add(new SpacerWidget());
			fPanel.add(numeratorInput);
			numeratorInput.getElement().setId("numeratorInput_TextAreaWithMaxLength");
			//numeratorInput.addKeyDownHandler(keyDownHandler);
			fPanel.add(new SpacerWidget());
			
			Label numExclInputLabel = (Label) LabelBuilder.buildLabel(numeratorExclusionsInput, "Numerator Exclusions");
			numExclInputLabel.setStyleName("measureDetailLabelStyle");
			fPanel.add(numExclInputLabel);
			//	fPanel.add(new SpacerWidget());
			fPanel.add(numeratorExclusionsInput);
			numeratorExclusionsInput.getElement().setId("numeratorExclusionsInput_TextAreaWithMaxLength");
			//numeratorExclusionsInput.addKeyDownHandler(keyDownHandler);
			fPanel.add(new SpacerWidget());
			
		}
		
		
		if ((measureScoringType != null) && ((measureScoringType.equalsIgnoreCase("Proportion")))) {
			
			Label denoExcepInputLabel = (Label) LabelBuilder.buildLabel(denominatorExceptionsInput, "Denominator Exceptions");
			denoExcepInputLabel.setStyleName("measureDetailLabelStyle");
			fPanel.add(denoExcepInputLabel);
			//
			fPanel.add(denominatorExceptionsInput);
			denominatorExceptionsInput.getElement().setId("denominatorExceptionsInput_TextAreaWithMaxLength");
			//denominatorExceptionsInput.addKeyDownHandler(keyDownHandler);
			fPanel.add(new SpacerWidget());
		}
		if((measureScoringType != null) && measureScoringType.equalsIgnoreCase("Continuous Variable")){
			Label measurePopInputLabel = (Label) LabelBuilder.buildLabel(measurePopulationInput, "Measure Population");
			measurePopInputLabel.setStyleName("measureDetailLabelStyle");
			fPanel.add(measurePopInputLabel);
			//fPanel.add(new SpacerWidget());
			fPanel.add(measurePopulationInput);
			measurePopulationInput.getElement().setId("measurePopulationInput_TextAreaWithMaxLength");
			//measurePopulationInput.addKeyDownHandler(keyDownHandler);
			fPanel.add(new SpacerWidget());
			
			Label measurePopExclInputLabel = (Label) LabelBuilder.buildLabel(measurePopulationExclusionsInput, "Measure Population Exclusions");
			measurePopExclInputLabel.setStyleName("measureDetailLabelStyle");
			fPanel.add(measurePopExclInputLabel);
			//	fPanel.add(new SpacerWidget());
			fPanel.add(measurePopulationExclusionsInput);
			measurePopulationExclusionsInput.getElement().setId("MmeasurePopulationExclusionsInput_TextAreaWithMaxLength");
			//measurePopulationExclusionsInput.addKeyDownHandler(keyDownHandler);
			fPanel.add(new SpacerWidget());
		}
		
		if((measureScoringType != null) &&(measureScoringType.equalsIgnoreCase("Continuous Variable")
				|| measureScoringType.equalsIgnoreCase("Ratio"))){
			Label measureObInputLabel = (Label) LabelBuilder.buildLabel(measureObservationsInput, "Measure Observations");
			measureObInputLabel.setStyleName("measureDetailLabelStyle");
			fPanel.add(measureObInputLabel);
			//	fPanel.add(new SpacerWidget());
			fPanel.add(measureObservationsInput);
			measureObservationsInput.getElement().setId("measureObservationsInput_TextAreaWithMaxLength");
			//measureObservationsInput.addKeyDownHandler(keyDownHandler);
			fPanel.add(new SpacerWidget());
		}
		Label supplementdalDataInputLabel = (Label) LabelBuilder.buildLabel(supplementalDataInput, "Supplemental Data Elements");
		supplementdalDataInputLabel.setStyleName("measureDetailLabelStyle");
		fPanel.add(supplementdalDataInputLabel);
		//fPanel.add(new SpacerWidget());
		fPanel.add(supplementalDataInput);
		supplementalDataInput.getElement().setId("supplementalDataInput_TextAreaWithMaxLength");
		//supplementalDataInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		Label measureSetNameLable = (Label) LabelBuilder.buildLabel(setNameInput, "Measure Set");
		measureSetNameLable.setStyleName("measureDetailLabelStyle");
		fPanel.add(measureSetNameLable);
		//fPanel.add(new SpacerWidget());
		fPanel.add(setNameInput);
		setNameInput.getElement().setId("setNameInput_TextAreaWithMaxLength");
		//setNameInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		
		AddRowButton.getElement().setId("AddRowButton_Button");
		fPanel.add(errorMessages);
		fPanel.add(successMessages);
		saveButton.setTitle("Save");
		saveButton.getElement().setId("saveButton_Button");
		fPanel.add(saveButton);
		successMessages.setMessage("");
		fPanel.add(new SpacerWidget());
		codeSystemVersionInput.setMaxLength(255);
		rationaleInput.setMaxLength(15000);
		NQFIDInput.setMaxLength(64);
		descriptionInput.setSize("500px", "100px");
		descriptionInput.setMaxLength(15000);
		copyrightInput.setSize("500px", "100px");
		copyrightInput.setMaxLength(15000);
		disclaimerInput.setSize("500px", "100px");
		disclaimerInput.setMaxLength(15000);
		rationaleInput.setMaxLength(15000);
		rationaleInput.setSize("500px", "100px");
		stratificationInput.setSize("500px", "100px");
		stratificationInput.setMaxLength(15000);
		riskAdjustmentInput.setSize("500px", "100px");
		riskAdjustmentInput.setMaxLength(15000);
		rateAggregationInput.setSize("500px", "100px");
		rateAggregationInput.setMaxLength(15000);
		
		setNameInput.setSize("500px", "50px");
		setNameInput.setMaxLength(155);
		
		//measureStewardOtherInput.setMaxLength(200);
		//measureStewardOtherInput.setWidth("415px");
		clinicalStmtInput.setSize("500px", "100px");
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
		
		
		initialPopInput.setSize("500px", "100px");
		initialPopInput.setMaxLength(15000);
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
		measurePopulationExclusionsInput.setSize("500px","100px");
		measurePopulationExclusionsInput.setMaxLength(15000);
		measureObservationsInput.setSize("500px", "100px");
		measureObservationsInput.setMaxLength(15000);
		eMeasureIdentifierInput.setReadOnly(true);
		eMeasureIdentifierInput.setMaxLength(6);
		generateeMeasureIDButton.setEnabled(true);
		String emeasureIdMSG = "Once an eCQM Identifier (Measure Authoring Tool) has been generated it may not be modified or removed for any draft or version of a measure.";
		generateeMeasureIDButton.setTitle(emeasureIdMSG);
		eMeasureIdentifierInput.setTitle(emeasureIdMSG);
		
	}
	
	//TODO by Ravi
	/**
	 * Adds the column to table.
	 *
	 * @param cellTable the cell table
	 * @param isEditable the is editable
	 * @return the cell table
	 */
	private CellTable<QualityDataSetDTO> addColumnToTable(CellTable<QualityDataSetDTO> cellTable, boolean isEditable) {
		Label itemCountHeader = new Label("ItemCount List");
		itemCountHeader.getElement().setId("itemCountHeader_Label");
		itemCountHeader.setStyleName("invisibleTableCaption");
		com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
		itemCountHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(itemCountHeader.getElement());
		MatCheckBoxCell qdmCheckBox = new MatCheckBoxCell(false, true, !isEditable);
		Column<QualityDataSetDTO, Boolean> chkBoxColumn = new  Column<QualityDataSetDTO, Boolean>(qdmCheckBox) {
			
			@Override
			public Boolean getValue(QualityDataSetDTO object) {
				boolean isSelected = false;
				if (qdmSelectedList.size() > 0) {
					for (int i = 0; i < qdmSelectedList.size(); i++) {
						if (qdmSelectedList.get(i).getUuid().equalsIgnoreCase(object.getUuid())) {
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
		
		chkBoxColumn.setFieldUpdater(new FieldUpdater<QualityDataSetDTO, Boolean>() {
			
			@Override
			public void update(int index, QualityDataSetDTO object, Boolean value) {
				selectionModel.setSelected(object, value);
				if (value) {
					qdmSelectedList.add(object);
				} else {
					for (int i = 0; i < qdmSelectedList.size(); i++) {
						if (qdmSelectedList.get(i).getUuid().equalsIgnoreCase(object.getUuid())) {
							qdmSelectedList.remove(i);
							break;
						}
					}
					
				}
				itemLabel.setText("Selected Items: " + qdmSelectedList.size());
				element.setAttribute("aria-role", "panel");
				element.setAttribute("aria-labelledby", "selectedItemsSummary");
				element.setAttribute("aria-live", "assertive");
				element.setAttribute("aria-atomic", "true");
				element.setAttribute("aria-relevant", "all");
				element.setAttribute("role", "alert");
			}
		});
		cellTable.addColumn(chkBoxColumn, SafeHtmlUtils.fromSafeConstant("<span title='Select'>" + "Select"
				+ "</span>"));
		
		Column<QualityDataSetDTO, SafeHtml> codeListName = new Column<QualityDataSetDTO, SafeHtml>(new SafeHtmlCell()) {
			
			@Override
			public SafeHtml getValue(QualityDataSetDTO object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String value;
				String QDMDetails = "";
				
				if (object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
					QDMDetails = "(User defined)";
				}  else {
					String version = object.getVersion();
					String effectiveDate = object.getEffectiveDate();
					
					if (effectiveDate != null) {
						
						QDMDetails = "(OID: " + object.getOid() + ", Effective Date: " + effectiveDate + ")";
					}  else if (!version.equals("1.0") && !version.equals("1")) {
						
						QDMDetails = "(OID: " + object.getOid() + ", Version: " + version + ")";
					} else {
						
						QDMDetails = "(OID: " + object.getOid() + ")";
					}
				}
				
				if ((object.getOccurrenceText() != null) && !object.getOccurrenceText().equals("")) {
					value = object.getOccurrenceText() + " of " + object.getCodeListName();
					sb.appendHtmlConstant("<span title=\"" + QDMDetails + " \" tabIndex=\"0\" >" + value + " </span>");
					
				} else {
					value = object.getCodeListName();
					sb.appendHtmlConstant("<span title=\"" + QDMDetails + " \" tabIndex=\"0\">" + value + " </span>");
				}
				
				return sb.toSafeHtml();
			}
		};
		
		cellTable.addColumn(codeListName, SafeHtmlUtils.fromSafeConstant("<span title='Name'>" + "Name"
				+ "</span>"));
		
		Column<QualityDataSetDTO, SafeHtml> vsacDataType = new Column<QualityDataSetDTO, SafeHtml>(new SafeHtmlCell()) {
			
			@Override
			public SafeHtml getValue(QualityDataSetDTO object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant("<span title=\"" + object.getDataType() + " \" tabIndex=\"0\" >"
						+ object.getDataType() + " </span>");
				
				return sb.toSafeHtml();
			}
		};
		
		cellTable.addColumn(vsacDataType, SafeHtmlUtils.fromSafeConstant("<span title='Datatype'>" + "Datatype"
				+ "</span>"));
		
		cellTable.setWidth("100%");
		cellTable.setColumnWidth(0, 5, Unit.PCT);
		cellTable.setColumnWidth(1, 10, Unit.PCT);
		cellTable.setColumnWidth(2, 10, Unit.PCT);
		
		return cellTable;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#buildCellTable
	 * (mat.client.clause.QDSAppliedListModel, boolean)
	 */
	@Override
	public void buildCellTable(QDSAppliedListModel appliedListModel, boolean isEditable) {
		horzPanel.clear();
		qdmItemCountListVPanel.clear();
		qdmItemCountListSPanel.clear();
		qdmSelectedListVPanel.clear();
		vPanel.clear();
		qdmItemCountListSPanel.setStyleName("cellTablePanel");
		if ((appliedListModel.getAppliedQDMs() != null) && (appliedListModel.getAppliedQDMs().size() > 0)) {
			cellTable = new CellTable<QualityDataSetDTO>();
			selectionModel = new MultiSelectionModel<QualityDataSetDTO>();
			cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			ListDataProvider<QualityDataSetDTO> sortProvider = new ListDataProvider<QualityDataSetDTO>();
			cellTable.setSelectionModel(selectionModel);
			if ((qdmSelectedList!=null) && (qdmSelectedList.size()>0)) {
				updateQDMSelectedList(appliedListModel.getAppliedQDMs());
				List<QualityDataSetDTO> selectedQDMList = new ArrayList<QualityDataSetDTO>();
				selectedQDMList.addAll(swapQdmElements(appliedListModel.getAppliedQDMs()));
				cellTable.setRowData(selectedQDMList);
				cellTable.setRowCount(selectedQDMList.size(), true);
				sortProvider.refresh();
				sortProvider.getList().addAll(selectedQDMList);
			} else {
				cellTable.setRowData(appliedListModel.getAppliedQDMs());
				cellTable.setRowCount(appliedListModel.getAppliedQDMs().size(), true);
				sortProvider.refresh();
				sortProvider.getList().addAll(appliedListModel.getAppliedQDMs());
			}
			cellTable.redraw();
			sortProvider.refresh();
			sortProvider.getList().addAll(appliedListModel.getAppliedQDMs());
			ListHandler<QualityDataSetDTO> sortHandler = new ListHandler<QualityDataSetDTO>(sortProvider.getList());
			cellTable.addColumnSortHandler(sortHandler);
			cellTable = addColumnToTable(cellTable, isEditable);
			//			updateQDMSelectedList(appliedListModel.getAppliedQDMs());
			sortProvider.addDataDisplay(cellTable);
			Label invisibleLabel = (Label) LabelBuilder
					.buildInvisibleLabel(
							"appliedQDMTableSummary",
							"In the Following Applied QDM Elements table a checkBoxCell is positioned to "
									+ "the left of the table with a select Column header followed by "
									+ "QDM name in second column and Datatype in third column. "
									+ "The Applied QDM elements are listed alphabetically in a table.");
			Label label = (Label) LabelBuilder
					.buildInvisibleLabel("selectedItemsSummary", "Selected Items: " + qdmSelectedList.size());
			cellTable.getElement().setAttribute("id", "AppliedQDMTable");
			cellTable.getElement().setAttribute("aria-describedby", "appliedQDMTableSummary");
			qdmItemCountListSPanel.setSize("500px", "150px");
			qdmItemCountListSPanel.add(invisibleLabel);
			qdmItemCountListSPanel.setWidget(cellTable);
			qdmItemCountListVPanel.add(label);
			qdmItemCountListVPanel.add(qdmItemCountListSPanel);
			horzPanel.add(qdmItemCountListVPanel);
			vPanel.setWidth("10px");
			horzPanel.add(vPanel);
			SimplePanel sPanel = new SimplePanel();
			sPanel.setHeight("75px");
			qdmSelectedListVPanel.add(sPanel);
			itemLabel.setText("Selected Items: " + qdmSelectedList.size());
			qdmSelectedListVPanel.add(itemLabel);
			element = qdmSelectedListVPanel.getElement();
			horzPanel.add(qdmSelectedListVPanel);
			
		} else {
			HTML desc = new HTML("<p> No Applied QDM Elements.</p>");
			qdmItemCountListSPanel.setSize("500px", "60px");
			qdmItemCountListSPanel.setWidget(desc);
			qdmItemCountListVPanel.add(qdmItemCountListSPanel);
			horzPanel.add(qdmItemCountListVPanel);
		}
	}
	
	/**
	 * Update qdm selected list.
	 *
	 * @param selectedList the selected list
	 */
	public void updateQDMSelectedList(List<QualityDataSetDTO> selectedList) {
		if (qdmSelectedList.size() != 0) {
			for (int i = 0; i < qdmSelectedList.size(); i++) {
				for (int j = 0; j < selectedList.size(); j++) {
					if (qdmSelectedList.get(i).getUuid().
							equalsIgnoreCase(selectedList.get(j).getUuid())) {
						qdmSelectedList.set(i, selectedList.get(j));
						break;
					}
				}
			}
		}
		
	}
	
	/**
	 * Swap qdm elements.
	 * @param qdmList the qdm list
	 * @return the list
	 */
	private  List<QualityDataSetDTO> swapQdmElements(List<QualityDataSetDTO> qdmList) {
		List<QualityDataSetDTO> qdmselectedList = new ArrayList<QualityDataSetDTO>();
		qdmselectedList.addAll(qdmSelectedList);
		for (int i = 0; i < qdmList.size(); i++) {
			if (!qdmSelectedList.contains(qdmList.get(i))) {
				qdmselectedList.add(qdmList.get(i));
			}
			
		}
		
		return qdmselectedList;
	}
	
	
	/**
	 * Update component measures selected list.
	 *
	 * @param measuresSelectedList the measures selected list
	 */
	public void updateComponentMeasuresSelectedList(List<ManageMeasureSearchModel.Result> measuresSelectedList) {
		if (componentMeasureSelectedList.size() != 0) {
			for (int i = 0; i < componentMeasureSelectedList.size(); i++) {
				for (int j = 0; j < measuresSelectedList.size(); j++) {
					if (componentMeasureSelectedList.get(i).getId().
							equalsIgnoreCase(measuresSelectedList.get(j).getId())) {
						componentMeasureSelectedList.set(i, measuresSelectedList.get(j));
						break;
					}
				}
			}
		}
		
	}
	
	
	
	/**
	 * Update measure type selected list.
	 *
	 * @param measureTypeList the measure type list
	 */
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
	
	/**
	 * Update measure developers selected list.
	 *
	 * @param measureDeveloperList the measure developer list
	 */
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
	
	
	
	
	/**
	 * Swap measure type list.
	 *
	 * @param authorsList the authors list
	 * @return the list
	 */
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
	
	
	/**
	 * Swap measure steward list.
	 *
	 * @param measureStewardList the measure steward list
	 * @return the list
	 */
	private  List<MeasureSteward> swapMeasureStewardList(List<MeasureSteward> measureStewardList) {
		List<MeasureSteward> stewardSelectedList = new ArrayList<MeasureSteward>();
		for(int i = 0; i<measureStewardList.size(); i++){
			if(measureStewardList.get(i).getId().equals(stewardId)){
				stewardSelectedList.add(measureStewardList.get(i));
				break;
			}
		}
		for (int j = 0; j < measureStewardList.size(); j++) {
			if (!stewardSelectedList.contains(measureStewardList.get(j))) {
				stewardSelectedList.add(measureStewardList.get(j));
			}
		}
		
		return stewardSelectedList;
	}
	
	/**
	 * Adds the measures column to table.
	 *
	 * @param editable the editable
	 * @return the cell table
	 */
	private CellTable<ManageMeasureSearchModel.Result> addMeasuresColumnToTable(
			boolean editable) {
		Label measureSearchHeader = new Label("Component Measures List");
		measureSearchHeader.getElement().setId("measureSearchHeader_Label");
		measureSearchHeader.setStyleName("invisibleTableCaption");
		com.google.gwt.dom.client.TableElement elem = componentMeasureCellTable
				.getElement().cast();
		measureSearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());
		measureSelectionModel = new MultiSelectionModel<ManageMeasureSearchModel.Result>();
		componentMeasureCellTable.setSelectionModel(measureSelectionModel);
		MatCheckBoxCell chbxCell = new MatCheckBoxCell(false, true, !editable);
		
		Column<ManageMeasureSearchModel.Result, Boolean> selectColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(
				chbxCell) {
			
			@Override
			public Boolean getValue(Result object) {
				boolean isSelected = false;
				if ((componentMeasureSelectedList != null)
						&& (componentMeasureSelectedList.size() > 0)) {
					for (int i = 0; i < componentMeasureSelectedList.size(); i++) {
						if (componentMeasureSelectedList.get(i).getId()
								.equalsIgnoreCase(object.getId())) {
							isSelected = true;
							// measureSelectionModel.setSelected(object,
							// isSelected);
							break;
						}
					}
				} else {
					isSelected = false;
				}
				return isSelected;
			}
		};
		
		selectColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, Boolean>() {
			
			@Override
			public void update(int index, Result object, Boolean value) {
				measureSelectionModel.setSelected(object, value);
				if (value) {
					componentMeasureSelectedList.add(object);
				} else {
					for (int i = 0; i < componentMeasureSelectedList
							.size(); i++) {
						if (componentMeasureSelectedList.get(i).getId()
								.equalsIgnoreCase(object.getId())) {
							componentMeasureSelectedList.remove(i);
							break;
						}
					}
				}
				componentMeasuresLabel.setText("Selected Items: "
						+ componentMeasureSelectedList.size());
			}
		});
		
		componentMeasureCellTable.addColumn(selectColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Select'>"
						+ "Select" + "</span>"));
		
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
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> finalizedDateColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new SafeHtmlCell()) {
			
			@Override
			public SafeHtml getValue(Result object) {
				return CellTableUtility
						.getColumnToolTip(convertTimestampToString(object
								.getFinalizedDate()));
			}
		};
		
		componentMeasureCellTable.addColumn(finalizedDateColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Finalized Date'>"
						+ "Finalized Date" + "</span>"));
		
		return componentMeasureCellTable;
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#buildComponentMeasuresSelectedList(java.util.List)
	 */
	@Override
	public void buildComponentMeasuresSelectedList(List<ManageMeasureSearchModel.Result> result, boolean editable){
		horzComponentMeasurePanel.clear();
		componentMeasuresListSPanel.clear();
		componentMeasuresSelectedListVPanel.clear();
		componentMeasuresListSPanel.setStyleName("cellTablePanel");
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
			updateComponentMeasuresSelectedList(selectedMeasureList);
			sortProvider.addDataDisplay(componentMeasureCellTable);
			componentMeasureCellTable.setWidth("100%");
			Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("componentMeasureListSummary",
					"In the following Measure List table,Select is given in first Column, Measure Name is given in Second column,"
							+ " Version in Third column, Finalized Date in fouth column.");
			Label label = (Label)LabelBuilder
					.buildInvisibleLabel("selectedComponentMeasuresSummary","Selected Items: "+ componentMeasureSelectedList.size());
			componentMeasureCellTable.getElement().setAttribute("id", "ComponentMeasuresListCellTable");
			componentMeasureCellTable.getElement().setAttribute("aria-describedby", "componentMeasureListSummary");
			componentMeasuresListSPanel.setSize("500px", "150px");
			componentMeasuresListSPanel.add(invisibleLabel);
			componentMeasuresListSPanel.setWidget(componentMeasureCellTable);
			componentMeasuresListVPanel.add(componentMeasuresListSPanel);
			horzComponentMeasurePanel.add(componentMeasuresListVPanel);
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setWidth("10px");
			horzComponentMeasurePanel.add(vPanel);
			SimplePanel sPanel = new SimplePanel();
			sPanel.setHeight("75px");
			componentMeasuresSelectedListVPanel.add(sPanel);
			componentMeasuresLabel.setText("Selected Items: " + componentMeasureSelectedList.size());
			componentMeasuresSelectedListVPanel.add(componentMeasuresLabel);
			horzComponentMeasurePanel.add(componentMeasuresSelectedListVPanel);
			
		} else {
			HTML desc = new HTML("<p> No Component Measures Selected.</p>");
			componentMeasuresListSPanel.setSize("500px", "60px");
			componentMeasuresListSPanel.setWidget(desc);
			componentMeasuresListVPanel.add(componentMeasuresListSPanel);
			horzComponentMeasurePanel.add(componentMeasuresListVPanel);
			horzComponentMeasurePanel.setWidth("99%");
		}
		
	}
	
	
	/**
	 * Adds the measure type column to table.
	 *
	 * @param editable the editable
	 */
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
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#buildMeasureTypeCellTable(java.util.List, boolean)
	 */
	@Override
	public void buildMeasureTypeCellTable(List<MeasureType> measureTypeDTOList, boolean editable){
		//horzMeasureTypePanel.clear();
		measureTypeSPanel.clear();
		//measureTypeSelectedListVPanel.clear();
		measureTypeSPanel.setStyleName("cellTablePanel");
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
			measureTypeSPanel.setSize("500px", "150px");
			measureTypeSPanel.add(invisibleLabel);
			measureTypeSPanel.setWidget(measureTypeCellTable);
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#buildAuthorCellTable(java.util.List, boolean)
	 */
	@Override
	public void buildAuthorCellTable(List<Author> currentAuthorsList, boolean editable) {
		authorSPanel.clear();
		authorSPanel.setStyleName("cellTablePanel");
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
				"In the following Measure Type List table,Select is given in first Column and Author is given in Second column");
		authorSPanel.getElement().setAttribute("id", "AuthorListCellTable");
		authorSPanel.getElement().setAttribute("aria-describedby", "authorListSummary");
		authorSPanel.setSize("500px", "150px");
		authorSPanel.add(invisibleLabel);
		authorSPanel.setWidget(authorCellTable);
	}
	
	/**
	 * Adds the author column to table.
	 *
	 * @param editable the editable
	 */
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
	
	
	/**
	 * Adds the steward column to table.
	 *
	 * @param editable the editable
	 */
	private void addStewardColumnToTable(boolean editable) {
		Label measureSearchHeader = new Label("Measure Steward List");
		measureSearchHeader.getElement().setId("measureDeveloperHeader_Label");
		measureSearchHeader.setStyleName("invisibleTableCaption");
		com.google.gwt.dom.client.TableElement elem = stewardCellTable.getElement().cast();
		measureSearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());
		stewardSelectionModel = new SingleSelectionModel<MeasureSteward>();
		stewardCellTable.setSelectionModel(stewardSelectionModel);
		RadioButtonCell stewardRadioButton = new RadioButtonCell(false, true, editable);
		Column<MeasureSteward, Boolean> selectColumn = new Column<MeasureSteward, Boolean>(
				stewardRadioButton) {
			
			@Override
			public Boolean getValue(MeasureSteward object) {
				boolean isSelected = false;
				
				if ((stewardId != null)) {
					if (stewardId.equalsIgnoreCase(object.getId())) {
						setStewardValue(object.getOrgName());
						isSelected = true;
					}
				} else {
					isSelected = false;
				}
				return isSelected;
				
			}
		};
		
		selectColumn.setFieldUpdater(new FieldUpdater<MeasureSteward, Boolean>() {
			
			@Override
			public void update(int index, MeasureSteward object, Boolean value) {
				stewardSelectionModel.setSelected(object, value);
				if (value) {
					setStewardId(object.getId());
					setStewardValue(object.getOrgName());
				}
				
			}
		});
		stewardCellTable.addColumn(selectColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Select'>"
						+ "Select" + "</span>"));
		
		Column<MeasureSteward, SafeHtml> measureNameColumn = new Column<MeasureSteward, SafeHtml>(
				new SafeHtmlCell()) {
			
			@Override
			public SafeHtml getValue(MeasureSteward object) {
				return CellTableUtility.getColumnToolTip(object.getOrgName(), object.getOrgOid());
			}
		};
		
		stewardCellTable.addColumn(measureNameColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Measure Steward'>"
						+ "Measure Steward" + "</span>"));
		
		
		
		
	}
	
	/**
	 * Update loading state.
	 *
	 * @param cellTable the cell table
	 */
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
	/**
	 * Adds the click handlers.
	 */
	public void addClickHandlers(){
		searchString.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchString.setText("");
				
			}
		});
	}
	
	
	/**
	 * Wrap radio button.
	 * 
	 * @param w
	 *            the w
	 * @return the widget
	 */
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
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getSearchButton()
	 */
	@Override
	public PrimaryButton getSearchButton(){
		return searchButton;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseMetaDataDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		
		return focusPanel;
		//return mainPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#asComponentMeasuresWidget()
	 */
	@Override
	public Widget asComponentMeasuresWidget() {
		return horzComponentMeasurePanel;
		//return mainPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getShortName()
	 */
	@Override
	public Label getShortName() {
		return abbrInput;
	}
	
	/* Returns the Measure Scoring choice value.
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureScoring()
	 */
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureScoring()
	 */
	@Override
	public Label getMeasureScoring(){
		return measScoringInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getSearchString()
	 */
	@Override
	public HasValue<String> getSearchString(){
		return searchString;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseMetaDataDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getFocusPanel()
	 */
	@Override
	public HasKeyDownHandlers getFocusPanel(){
		return focusPanel;
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getEditMeasureTypeButton()
	 */
	@Override
	public HasClickHandlers getEditMeasureTypeButton() {
		return addEditMeasureType;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getAddEditCmponentMeasures()
	 */
	@Override
	public HasClickHandlers getAddEditComponentMeasures() {
		return addEditCmponentMeasures;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}
	
	
	/**
	 * Sets the list box options.
	 *
	 * @return the version number
	 */
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getVersionNumber()
	 */
	@Override
	public Label getVersionNumber() {
		return versionInput;
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getSupplementalData()
	 */
	@Override
	public HasValue<String> getSupplementalData() {
		return supplementalDataInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getSetName()
	 */
	@Override
	public HasValue<String> getSetName() {
		return setNameInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasurePopulationExclusions()
	 */
	@Override
	public HasValue<String> getMeasurePopulationExclusions() {
		return measurePopulationExclusionsInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#geteMeasureIdentifier()
	 */
	@Override
	public Label geteMeasureIdentifier() {
		return eMeasureIdentifier;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getNqfId()
	 */
	@Override
	public HasValue<String> getNqfId() {
		return NQFIDInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getFinalizedDate()
	 */
	@Override
	public Label getFinalizedDate() {
		return finalizedDate;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureType()
	 */
	@Override
	public String getMeasureType() {
		return measureTypeInput.getValue(measureTypeInput.getSelectedIndex());
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getEndorsebyNQF()
	 */
	@Override
	public HasValue<Boolean> getEndorsebyNQF() {
		return Yes;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getAuthor()
	 */
	@Override
	public String getAuthor() {
		return authorInput.getValue(authorInput.getSelectedIndex());
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getDescription()
	 */
	@Override
	public HasValue<String> getDescription() {
		return descriptionInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getCopyright()
	 */
	@Override
	public HasValue<String> getCopyright() {
		return copyrightInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getDisclaimer()
	 */
	@Override
	public HasValue<String> getDisclaimer() {
		return disclaimerInput;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getInitialPatientPop()
	 */
	@Override
	public HasValue<String> getInitialPop() {
		return initialPopInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getDenominator()
	 */
	@Override
	public HasValue<String> getDenominator() {
		return denominatorInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getDenominatorExclusions()
	 */
	@Override
	public HasValue<String> getDenominatorExclusions() {
		return denominatorExclusionsInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getNumerator()
	 */
	@Override
	public HasValue<String> getNumerator() {
		return numeratorInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getNumeratorExclusions()
	 */
	@Override
	public HasValue<String> getNumeratorExclusions() {
		return numeratorExclusionsInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getDenominatorExceptions()
	 */
	@Override
	public HasValue<String> getDenominatorExceptions() {
		return denominatorExceptionsInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasurePopulation()
	 */
	@Override
	public HasValue<String> getMeasurePopulation() {
		return measurePopulationInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureObservations()
	 */
	@Override
	public HasValue<String> getMeasureObservations() {
		return measureObservationsInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getClinicalRecommendation()
	 */
	@Override
	public HasValue<String> getClinicalRecommendation() {
		return clinicalStmtInput;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getDefinitions()
	 */
	@Override
	public HasValue<String> getDefinitions() {
		return definitionsInput;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getGuidance()
	 */
	@Override
	public HasValue<String> getGuidance() {
		return guidanceInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getTransmissionFormat()
	 */
	@Override
	public HasValue<String> getTransmissionFormat() {
		return transmissionFormatInput;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getRationale()
	 */
	@Override
	public HasValue<String> getRationale() {
		return rationaleInput;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getImprovementNotation()
	 */
	@Override
	public HasValue<String> getImprovementNotation() {
		return improvementNotationInput;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getStratification()
	 */
	@Override
	public HasValue<String> getStratification() {
		return stratificationInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getRiskAdjustment()
	 */
	@Override
	public HasValue<String> getRiskAdjustment() {
		return riskAdjustmentInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getRateAggregation()
	 */
	@Override
	public HasValue<String> getRateAggregation() {
		return rateAggregationInput;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getReference()
	 */
	@Override
	public HasValue<String> getReference() {
		return referenceInput;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasurementFromPeriod()
	 */
	@Override
	public String getMeasurementFromPeriod() {
		return measurePeriodFromInput.getValue();
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasurementToPeriod()
	 */
	@Override
	public String getMeasurementToPeriod() {
		return measurePeriodToInput.getValue();
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseMetaDataDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	/**
	 * Creates the reference input.
	 * 
	 * @return the text area with max length
	 */
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
	
	
	
	/**
	 * Adds the row.
	 * 
	 * @param reference
	 *            the reference
	 */
	@Override
	public void  addRow(FlexTable reference) {
		TextAreaWithMaxLength newReferenceBox = createReferenceInput();
		++counter;
		String dynamicLabel = "Reference" + counter;
		Widget newReferenceBoxLabel = LabelBuilder.buildInvisibleLabel(newReferenceBox, dynamicLabel);
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(newReferenceBoxLabel);
		hp.add(newReferenceBox);
		newReferenceBox.getElement().setId(dynamicLabel+"_TextAreaWithMaxLength");
		Button newremoveButton = new PrimaryButton("Remove", "primaryGreyLeftButton");
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
		referenceTable.setWidget(numRows, 0, hp);
		//referenceTable.setWidget(numRows, 1, new SimplePanel());
		referenceTable.setWidget(numRows, 1, newremoveButton);
		newremoveButton.getElement().setId("newremoveButton"+numRows+"_Button");
		referenceTable.getFlexCellFormatter().setRowSpan(0, 1, numRows + 1);
		referenceArrayList.add(newReferenceBox);
	}
	
	/**
	 * Removes the row.
	 * 
	 * @param reference
	 *            the reference
	 * @param rowIndex
	 *            the row index
	 */
	private void removeRow(FlexTable reference, int rowIndex) {
		int numRows = reference.getRowCount();
		if (referenceTable.getWidget(rowIndex, 0) instanceof HorizontalPanel) {
			HorizontalPanel horizontalPanel =  (HorizontalPanel) referenceTable.getWidget(rowIndex, 0);
			TextAreaWithMaxLength areaWithMaxLength = (TextAreaWithMaxLength) horizontalPanel.getWidget(1);
			referenceArrayList.remove(areaWithMaxLength);
		} else if (referenceTable.getWidget(rowIndex, 0) instanceof TextAreaWithMaxLength) {
			referenceArrayList.remove(referenceTable.getWidget(rowIndex, 0));
		}
		referenceTable.removeRow(rowIndex);
		//referenceTable.getFlexCellFormatter().setRowSpan(0, 1, numRows - 1);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getReferenceValues()
	 */
	@Override
	public List<String> getReferenceValues() {
		ArrayList<String> referenceValues = new ArrayList<String>();
		for (TextAreaWithMaxLength referenceBox: referenceArrayList) {
			referenceValues.add(referenceBox.getValue());
		}
		return referenceValues;
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setReferenceValues(java.util.List, boolean)
	 */
	@Override
	public void setReferenceValues(List<String> values, boolean editable) {
		if ((values != null) && !values.isEmpty()) {
			clearReferences();
			for (int i = 0; i < values.size(); i++) {
				TextAreaWithMaxLength newReferenceBox = createReferenceInput();
				newReferenceBox.setValue(values.get(i));
				newReferenceBox.setEnabled(editable);
				if (i == 0) {
					referenceTable.setWidget(0, 0, newReferenceBox);
					referenceTable.setWidget(0, 1, new SimplePanel());
					referenceTable.setWidget(0, 2, AddRowButton);
				} else {
					referenceTable.setWidget(i, 0, newReferenceBox);
					if (editable) {
						Button newRemoveButton = new PrimaryButton("Remove", "primaryGreyLeftButton");
						newRemoveButton.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								com.google.gwt.user.client.ui.HTMLTable.Cell cell = referenceTable.getCellForEvent(event);
								int clickedRowIndex = cell.getRowIndex();
								removeRow(referenceTable, clickedRowIndex);
							}
						});
						referenceTable.setWidget(i, 1, new SimplePanel());
						referenceTable.setWidget(i, 2, newRemoveButton);
					}
				}
				referenceArrayList.add(newReferenceBox);
			}
			referencePlaceHolder.add(referenceTable);
		} else if (values.isEmpty()) {
			clearReferences();
			TextAreaWithMaxLength newReferenceBox = createReferenceInput();
			referenceTable.setWidget(0, 0, newReferenceBox);
			referenceTable.setWidget(0, 1, new SimplePanel());
			referenceTable.setWidget(0, 2, AddRowButton);
			referenceArrayList.add(newReferenceBox);
			referencePlaceHolder.add(referenceTable);
		}
	}
	
	/**
	 * Builds the reference table.
	 * 
	 * @param referenceInput
	 *            the reference input
	 */
	private void buildReferenceTable(TextAreaWithMaxLength referenceInput) {
		clearReferences();
		referenceTable.setWidget(0, 0, referenceInput);
		referenceArrayList.add(referenceInput);
		referenceTable.setWidget(0, 1, new SimplePanel());
		referenceTable.setWidget(0, 2, AddRowButton);
	}
	
	
	/**
	 * Clear references.
	 */
	private void clearReferences() {
		referencePlaceHolder.clear();
		referenceTable.clear();
		referenceTable.removeAllRows();
		referenceArrayList.clear();
		FlexCellFormatter cellFormatter = referenceTable.getFlexCellFormatter();
		cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasurementFromPeriodInputBox()
	 */
	@Override
	public DateBoxWithCalendar getMeasurementFromPeriodInputBox() {
		return measurePeriodFromInput;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasurementToPeriodInputBox()
	 */
	@Override
	public DateBoxWithCalendar getMeasurementToPeriodInputBox() {
		return measurePeriodToInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getEmeasureId()
	 */
	@Override
	public HasValue<String> getEmeasureId(){
		// TODO Auto-generated method stub
		return eMeasureIdentifierInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setGenerateEmeasureIdButtonEnabled(boolean)
	 */
	@Override
	public void setGenerateEmeasureIdButtonEnabled(boolean b) {
		// TODO Auto-generated method stub
		generateeMeasureIDButton.setEnabled(b);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getGenerateEmeasureIdButton()
	 */
	@Override
	public HasClickHandlers getGenerateEmeasureIdButton() {
		// TODO Auto-generated method stub
		return generateeMeasureIDButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getDeleteMeasure()
	 */
	@Override
	public Button getDeleteMeasure() {
		return deleteMeasure;
	}
	
	/**
	 * Local method to clear out the Steward other panel.
	 */
	private void clearOtherPanel() {
		measureStewardOtherInput.setValue(null);
		emptyTextBoxHolder.clear();
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureStatus()
	 */
	/*@Override
	public ListBoxMVP getMeasureStatus() {
		return objectStatusInput;
	}*/
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setAddEditButtonsVisible(boolean)
	 */
	@Override
	public void setAddEditButtonsVisible(boolean b) {
		addEditMeasureType.setEnabled(b);
		addEditCmponentMeasures.setEnabled(b);
		measurePeriodFromInput.setEnableCSS(b);
		measurePeriodToInput.setEnableCSS(b);
		AddRowButton.setEnabled(b);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setMeasurementPeriodButtonsVisible(boolean)
	 */
	@Override
	public void setMeasurementPeriodButtonsVisible(boolean b){
		measurePeriodFromInput.getDateBox().setEnabled(b);
		measurePeriodToInput.getDateBox().setEnabled(b);
		measurePeriodFromInput.setEnableCSS(b);
		measurePeriodToInput.setEnableCSS(b);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getNotEndorsebyNQF()
	 */
	@Override
	public HasValue<Boolean> getNotEndorsebyNQF() {
		return No;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseMetaDataDisplay#setObjectStatusOptions(java.util.List)
	 */
	/*@Override
	public void setObjectStatusOptions(List<? extends HasListBox> texts) {
		setListBoxOptions(objectStatusInput, texts, MatContext.PLEASE_SELECT);
		
	}*/
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureStatusValue()
	 */
	//@Override
	/*public String getMeasureStatusValue() {
		return objectStatusInput.getItemText(objectStatusInput.getSelectedIndex());
	}*/
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#enableEndorseByRadioButtons(boolean)
	 */
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
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseMetaDataDisplay#getSaveErrorMsg()
	 */
	@Override
	public ErrorMessageDisplay getSaveErrorMsg() {
		// TODO Auto-generated method stub
		return saveErrorDisplay;
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getSaveBtn()
	 */
	@Override
	public Button getSaveBtn() {
		return saveButton;
	}
	
	/**
	 * Clear error msg.
	 */
	private void clearErrorMsg() {
		getSaveErrorMsg().clear();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setQdmSelectedList(java.util.List)
	 */
	@Override
	public void setQdmSelectedList(List<QualityDataSetDTO> qdmSelectedList) {
		this.qdmSelectedList = qdmSelectedList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getQdmSelectedList()
	 */
	@Override
	public List<QualityDataSetDTO> getQdmSelectedList() {
		return qdmSelectedList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getComponentMeasureSelectedList()
	 */
	@Override
	public List<ManageMeasureSearchModel.Result> getComponentMeasureSelectedList() {
		return componentMeasureSelectedList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setComponentMeasureSelectedList(java.util.List)
	 */
	@Override
	public void setComponentMeasureSelectedList(
			List<ManageMeasureSearchModel.Result> componentMeasureSelectedList) {
		this.componentMeasureSelectedList = componentMeasureSelectedList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureTypeSelectedList()
	 */
	@Override
	public List<MeasureType> getMeasureTypeSelectedList() {
		return measureTypeSelectedList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setMeasureTypeSelectedList(java.util.List)
	 */
	@Override
	public void setMeasureTypeSelectedList(List<MeasureType> measureTypeSelectedList) {
		this.measureTypeSelectedList = measureTypeSelectedList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getDialogBox()
	 */
	@Override
	public DialogBox getDialogBox() {
		return dialogBox;
	}
	
	/**
	 * Convert timestamp to string.
	 *
	 * @param ts the ts
	 * @return the string
	 */
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
	
	
	/**
	 * Gets the authors selected list.
	 *
	 * @return the authorsSelectedList
	 */
	@Override
	public List<Author> getAuthorsSelectedList() {
		return authorsSelectedList;
	}
	
	
	/**
	 * Sets the authors selected list.
	 *
	 * @param authorsSelectedList the authorsSelectedList to set
	 */
	@Override
	public void setAuthorsSelectedList(List<Author> authorsSelectedList) {
		this.authorsSelectedList = authorsSelectedList;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#buildStewardCellTable(java.util.List, boolean)
	 */
	@Override
	public void buildStewardCellTable(List<MeasureSteward> currentStewardList,
			boolean editable) {
		
		stewardSPanel.clear();
		stewardSPanel.setStyleName("cellTablePanel");
		stewardCellTable = new CellTable<MeasureSteward>();
		stewardCellTable
		.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<MeasureSteward> sortProvider = new ListDataProvider<MeasureSteward>();
		if(stewardId!=null) {
			List<MeasureSteward> measureStewardSelectedList = new ArrayList<MeasureSteward>();
			measureStewardSelectedList.addAll(swapMeasureStewardList(currentStewardList));
			stewardCellTable.setRowData(measureStewardSelectedList);
			stewardCellTable.setRowCount(measureStewardSelectedList.size(), true);
			sortProvider.refresh();
			sortProvider.getList().addAll(measureStewardSelectedList);
		} else {
			stewardCellTable.setRowData(currentStewardList);
			stewardCellTable.setRowCount(currentStewardList.size(), true);
			sortProvider.refresh();
			sortProvider.getList().addAll(currentStewardList);
		}
		
		addStewardColumnToTable(editable);
		sortProvider.addDataDisplay(stewardCellTable);
		stewardCellTable.setWidth("100%");
		Label invisibleLabel = (Label) LabelBuilder
				.buildInvisibleLabel(
						"stewardListSummary",
						"In the following Steward List table, Select is given in first Column and Steward is given in Second column");
		stewardSPanel.getElement().setAttribute("id", "StewardListCellTable");
		stewardSPanel.getElement().setAttribute("aria-describedby",
				"stewardListSummary");
		stewardSPanel.setSize("500px", "150px");
		stewardSPanel.add(invisibleLabel);
		stewardSPanel.setWidget(stewardCellTable);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setStewardId(java.lang.String)
	 */
	@Override
	public void setStewardId(String id){
		stewardId = id;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getStewardId()
	 */
	@Override
	public String getStewardId() {
		return stewardId;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getStewardValue()
	 */
	@Override
	public String getStewardValue() {
		return stewardValue;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setStewardValue(java.lang.String)
	 */
	@Override
	public void setStewardValue(String stewardValue){
		this.stewardValue = stewardValue;
	}
	
	/** The calender year change handler. */
	private  ValueChangeHandler<Boolean> calenderYearChangeHandler = new ValueChangeHandler<Boolean>() {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			measurePeriodFromInput.setValue("");
			measurePeriodToInput.setValue("");
			errorMessages.clear();
			if (calenderYear.getValue().equals(Boolean.FALSE)) {
				measurePeriodFromInput.setEnabled(true);
				measurePeriodToInput.setEnabled(true);
			} else {
				measurePeriodFromInput.setEnabled(false);
				measurePeriodToInput.setEnabled(false);
			}
		}
	};
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getCalenderYear()
	 */
	@Override
	public CheckBox getCalenderYear() {
		return calenderYear;
	}
	
	
	/**
	 * @return the measureScoringType
	 */
	public String getMeasureScoringType() {
		return measureScoringType;
	}
	
	
	/**
	 * @param measureScoringType the measureScoringType to set
	 */
	@Override
	public void setMeasureScoringType(String measureScoringType) {
		this.measureScoringType = measureScoringType;
	}
	
	
	/**
	 * @return the authorListBox
	 */
	@Override
	public ListBox getAuthorListBox() {
		return authorListBox;
	}
	
	
	
	
	
	/**
	 * @return the measureTypeListBox
	 */
	@Override
	public ListBox getMeasureTypeListBox() {
		return measureTypeListBox;
	}
	
	
	
	
	
	/**
	 * @return the generateeMeasureIDButton
	 */
	@Override
	public Button getGenerateeMeasureIDButton() {
		return generateeMeasureIDButton;
	}
	
	
	
	
	
	/**
	 * @return the abbrInput
	 */
	@Override
	public Label getAbbrInput() {
		return abbrInput;
	}
	
	/**
	 * @return the patient based input field
	 */
	@Override
	public Label getPatientBasedInput() {
		return patientBasedInput;
	}
	
	
	
	
	/**
	 * @return the measScoringInput
	 */
	@Override
	public Label getMeasScoringInput() {
		return measScoringInput;
	}
	
	
	
	
	
	/**
	 * @return the rationaleInput
	 */
	@Override
	public TextAreaWithMaxLength getRationaleInput() {
		return rationaleInput;
	}
	
	
	
	
	
	/**
	 * @return the versionInput
	 */
	@Override
	public Label getVersionInput() {
		return versionInput;
	}
	
	
	
	
	
	/**
	 * @return the authorInput
	 */
	@Override
	public ListBoxMVP getAuthorInput() {
		return authorInput;
	}
	
	
	
	
	
	/**
	 * @return the measureStewardOtherInput
	 */
	@Override
	public TextBox getMeasureStewardOtherInput() {
		return measureStewardOtherInput;
	}
	
	
	
	
	
	/**
	 * @return the descriptionInput
	 */
	@Override
	public TextAreaWithMaxLength getDescriptionInput() {
		return descriptionInput;
	}
	
	
	
	
	
	/**
	 * @return the copyrightInput
	 */
	@Override
	public TextAreaWithMaxLength getCopyrightInput() {
		return copyrightInput;
	}
	
	
	
	
	
	/**
	 * @return the disclaimerInput
	 */
	@Override
	public TextAreaWithMaxLength getDisclaimerInput() {
		return disclaimerInput;
	}
	
	
	
	
	
	/**
	 * @return the riskAdjustmentInput
	 */
	@Override
	public TextAreaWithMaxLength getRiskAdjustmentInput() {
		return riskAdjustmentInput;
	}
	
	
	
	
	
	/**
	 * @return the rateAggregationInput
	 */
	@Override
	public TextAreaWithMaxLength getRateAggregationInput() {
		return rateAggregationInput;
	}
	
	
	
	
	
	/**
	 * @return the initialPopInput
	 */
	@Override
	public TextAreaWithMaxLength getInitialPopInput() {
		return initialPopInput;
	}
	
	
	
	
	
	/**
	 * @return the denominatorInput
	 */
	@Override
	public TextAreaWithMaxLength getDenominatorInput() {
		return denominatorInput;
	}
	
	
	
	
	
	/**
	 * @return the denominatorExclusionsInput
	 */
	@Override
	public TextAreaWithMaxLength getDenominatorExclusionsInput() {
		return denominatorExclusionsInput;
	}
	
	
	
	
	
	/**
	 * @return the numeratorInput
	 */
	@Override
	public TextAreaWithMaxLength getNumeratorInput() {
		return numeratorInput;
	}
	
	
	
	
	
	/**
	 * @return the numeratorExclusionsInput
	 */
	@Override
	public TextAreaWithMaxLength getNumeratorExclusionsInput() {
		return numeratorExclusionsInput;
	}
	
	
	
	
	
	/**
	 * @return the denominatorExceptionsInput
	 */
	@Override
	public TextAreaWithMaxLength getDenominatorExceptionsInput() {
		return denominatorExceptionsInput;
	}
	
	
	
	
	
	/**
	 * @return the measurePopulationInput
	 */
	@Override
	public TextAreaWithMaxLength getMeasurePopulationInput() {
		return measurePopulationInput;
	}
	
	
	
	
	
	/**
	 * @return the measurePopulationExclusionsInput
	 */
	@Override
	public TextAreaWithMaxLength getMeasurePopulationExclusionsInput() {
		return measurePopulationExclusionsInput;
	}
	
	
	
	
	
	/**
	 * @return the measureObservationsInput
	 */
	@Override
	public TextAreaWithMaxLength getMeasureObservationsInput() {
		return measureObservationsInput;
	}
	
	
	
	
	
	/**
	 * @return the measureTypeInput
	 */
	@Override
	public ListBoxMVP getMeasureTypeInput() {
		return measureTypeInput;
	}
	
	
	
	
	
	/**
	 * @return the measurePeriodFromInput
	 */
	@Override
	public DateBoxWithCalendar getMeasurePeriodFromInput() {
		return measurePeriodFromInput;
	}
	
	
	
	
	
	/**
	 * @return the measurePeriodToInput
	 */
	@Override
	public DateBoxWithCalendar getMeasurePeriodToInput() {
		return measurePeriodToInput;
	}
	
	
	
	
	
	/**
	 * @return the supplementalDataInput
	 */
	@Override
	public TextAreaWithMaxLength getSupplementalDataInput() {
		return supplementalDataInput;
	}
	
	
	
	
	
	/**
	 * @return the codeSystemVersionInput
	 */
	@Override
	public TextBox getCodeSystemVersionInput() {
		return codeSystemVersionInput;
	}
	
	
	
	
	
	/**
	 * @return the nQFIDInput
	 */
	@Override
	public TextBox getNQFIDInput() {
		return NQFIDInput;
	}
	
	
	
	
	
	/**
	 * @return the setNameInput
	 */
	@Override
	public TextAreaWithMaxLength getSetNameInput() {
		return setNameInput;
	}
	
	
	
	
	
	/**
	 * @return the clinicalStmtInput
	 */
	@Override
	public TextAreaWithMaxLength getClinicalStmtInput() {
		return clinicalStmtInput;
	}
	
	
	
	
	
	/**
	 * @return the improvementNotationInput
	 */
	@Override
	public TextAreaWithMaxLength getImprovementNotationInput() {
		return improvementNotationInput;
	}
	
	
	
	
	
	/**
	 * @return the referenceInput
	 */
	@Override
	public TextAreaWithMaxLength getReferenceInput() {
		return referenceInput;
	}
	
	
	
	
	
	/**
	 * @return the definitionsInput
	 */
	@Override
	public TextAreaWithMaxLength getDefinitionsInput() {
		return definitionsInput;
	}
	
	
	
	
	
	/**
	 * @return the transmissionFormatInput
	 */
	@Override
	public TextAreaWithMaxLength getTransmissionFormatInput() {
		return transmissionFormatInput;
	}
	
	
	
	
	
	/**
	 * @return the stratificationInput
	 */
	@Override
	public TextAreaWithMaxLength getStratificationInput() {
		return stratificationInput;
	}
	
	
	
	
	
	/**
	 * @return the guidanceInput
	 */
	@Override
	public TextAreaWithMaxLength getGuidanceInput() {
		return guidanceInput;
	}
	
	
	/**
	 * @return the addRowButton
	 */
	@Override
	public Button getAddRowButton() {
		return AddRowButton;
	}
	
	/**
	 * @return the referenceTable
	 */
	@Override
	public FlexTable getReferenceTable() {
		return referenceTable;
	}
	
}
