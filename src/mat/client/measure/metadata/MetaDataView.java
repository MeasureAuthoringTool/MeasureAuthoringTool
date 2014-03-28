package mat.client.measure.metadata;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.ImageResources;
import mat.client.clause.QDSAppliedListModel;
import mat.client.codelist.HasListBox;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay;
import mat.client.shared.CustomButton;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.TextAreaWithMaxLength;
import mat.client.util.CellTableUtility;
import mat.model.Author;
import mat.model.MeasureType;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	
	/** The name input. */
	protected Label nameInput = new Label();
	
	/** The abbr input. */
	protected Label abbrInput = new Label();
	
	/** The meas scoring input. */
	protected Label measScoringInput = new Label();
	//protected TextBox measureIdInput = new TextBox();
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
	protected ListBoxMVP measureStewardInput = new ListBoxMVP(false);
	
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
	
	
	/** The object status input. */
	protected ListBoxMVP objectStatusInput = new ListBoxMVP();
	
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
	
	/** The add edit authors. */
	private Button addEditAuthors = new PrimaryButton("Add/Edit Measure Developer(s)","primaryMetaDataButton");
	
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
	
	/** The cell table. */
	private CellTable<QualityDataSetDTO> cellTable;
	
	 /** The table. */
 	//private CellTable<QualityDataSetDTO> table;
	
	/** The horz panel. */
	private HorizontalPanel horzPanel = new HorizontalPanel();
	
	/** The horz component measure panel. */
	private HorizontalPanel horzComponentMeasurePanel = new HorizontalPanel();
	
	/** The qdm selected list v panel. */
	VerticalPanel qdmSelectedListVPanel=new VerticalPanel();
	
	/** The component measures selected list v panel. */
	VerticalPanel componentMeasuresSelectedListVPanel = new VerticalPanel();
	
	/** The qdm selected list s panel. */
	ScrollPanel qdmSelectedListSPanel=new ScrollPanel();
	
	/** The v panel. */
	VerticalPanel vPanel=new VerticalPanel();
	
	/** The qdm selected list. */
	private  List<QualityDataSetDTO> qdmSelectedList;
	
	/** The search string. */
	private TextBox searchString = new TextBox();
	
	/** The component measure selected list. */
	private List<ManageMeasureSearchModel.Result> componentMeasureSelectedList;
	
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

	/** The element. */
    private  Element element;
    
    /** The component measure cell table. */
    private CellTable<ManageMeasureSearchModel.Result> componentMeasureCellTable; 
    
    /** The selected measure list. */
    private List<ManageMeasureSearchModel.Result> selectedMeasureList;
    
    /** The component measures list panel. */
    VerticalPanel componentMeasuresListPanel = new VerticalPanel();
    
    /** The measures list selection model. */
    private MultiSelectionModel<ManageMeasureSearchModel.Result> measuresListSelectionModel;
    
   // private MatButtonCell searchButton = new MatButtonCell("click to Search Measures","customSearchButton");
	
    /** The zoom search button. */
   private CustomButton zoomSearchButton = (CustomButton) getImage("Search",
			ImageResources.INSTANCE.searchZoom(), "Search" , "MeasureSearchButton");
    
    /** The search button. */
    private PrimaryButton searchButton = new PrimaryButton("Go");

	/**
	 * Instantiates a new meta data view.
	 */
	public MetaDataView(){
		addClickHandlers();
		searchString.setHeight("20px");
		//referenceArrayList = new ArrayList<TextAreaWithMaxLength>();
		HorizontalPanel mainContent = new HorizontalPanel();
		mainContent.getElement().setId("mainContent_HorizontalPanel");
		mainPanel.setStylePrimaryName("searchResultsContainer");
		mainPanel.addStyleName("leftAligned");
		mainPanel.getElement().setId("mainPanel_FlowPanel01");
		mainContent.add(buildLeftSideForm());
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
	private Widget buildLeftSideForm() {
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
		authorListBox.getElement().setId("authorListBox_ListBox");
		
		measureTypeListBox.setVisibleItemCount(5);
		measureTypeListBox.addChangeHandler(changeHandler);
		measureTypeListBox.getElement().setId("measureTypeListBox_ListBox");
		
		FlowPanel fPanel = new FlowPanel();
		
		fPanel.setStyleName("leftSideForm");
		fPanel.getElement().setId("fPanel_FlowPanelLeft");
		
		
		
		fPanel.add(new Label("All fields are required except where noted as optional."));
		fPanel.add(new SpacerWidget());
		
		fPanel.add(errorMessages);
		
		
		fPanel.add(LabelBuilder.buildLabel(nameInput, "eMeasure Title"));
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setAttribute("id", "emeasureTitlePanel");
		hp.add(nameInput);
		hp.add(deleteMeasure);
		fPanel.add(hp);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(abbrInput, "eMeasure Abbreviated Title"));
		fPanel.add(abbrInput);
		fPanel.add(new SpacerWidget());
		
		HorizontalFlowPanel horizontalPanel = new HorizontalFlowPanel();
		horizontalPanel.getElement().setId("horizontalPanel_HorizontalFlowPanelLeft");
		horizontalPanel.add(LabelBuilder.buildLabel(eMeasureIdentifierInput, "eMeasure Identifier (Measure Authoring Tool)"));
		Widget optionLabelWidget = LabelBuilder.buildLabel(eMeasureIdentifierInput, " - Optional");
		optionLabelWidget.setStyleName("generate-emeasureid-button");
		horizontalPanel.add(optionLabelWidget);
		fPanel.add(horizontalPanel);
		
		fPanel.add(eMeasureIdentifierInput);
		fPanel.add(generateeMeasureIDButton);
		generateeMeasureIDButton.addClickHandler(clickHandler);
		generateeMeasureIDButton.getElement().setId("generateeMeasureIDButton_Button");
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
		// MAT 2995 : Commented Measure Status Field from Measure Detail View.
		/*fPanel.add(LabelBuilder.buildLabel(objectStatusInput, "Measure Status"));
		fPanel.add(objectStatusInput);
		objectStatusInput.addChangeHandler(changeHandler);
		fPanel.add(new SpacerWidget());*/
		
		fPanel.add(LabelBuilder.buildLabel(NQFIDInput, "NQF Number"));
		fPanel.add(NQFIDInput);
		NQFIDInput.addKeyDownHandler(keyDownHandler);
		fPanel.add(new SpacerWidget());
		
		HorizontalPanel measurePeriodPanel = new HorizontalPanel();
		measurePeriodPanel.getElement().setId("measurePeriodPanel_HorizontalPanel");
		Label fromLabel = new Label("From");
		fromLabel.addStyleName("firstLabel");
		measurePeriodPanel.add(fromLabel);
		measurePeriodPanel.add(measurePeriodFromInput);
		Label toLabel = new Label("To");
		toLabel.addStyleName("secondLabel");
		measurePeriodPanel.add(toLabel);
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
		verStewardPanel.getElement().setId("verStewardPanel_verStewardPanel");
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
		
		fPanel.add(LabelBuilder.buildLabel(cellTable, " Items Counted - Optional"));
		fPanel.add(horzPanel);
		fPanel.add(new SpacerWidget());
		
		
		//fPanel.add(LabelBuilder.buildLabel(componentMeasureCellTable, " Component Measures Counted - Optional"));
		fPanel.add(horzComponentMeasurePanel);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(stratificationInput , "Stratification"));
		fPanel.add(stratificationInput);
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
		//fPanel.add(new SpacerWidget());
		
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
		
		
		fPanel.add(LabelBuilder.buildLabel(initialPopInput, "Initial Population"));
		fPanel.add(initialPopInput);
		initialPopInput.addKeyDownHandler(keyDownHandler);
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
		
		fPanel.add(LabelBuilder.buildLabel(measurePopulationExclusionsInput, "Measure Population Exclusions"));
		fPanel.add(measurePopulationExclusionsInput);
		measurePopulationExclusionsInput.addKeyDownHandler(keyDownHandler);
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
		saveButton.setTitle("Save");
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
		
		measureStewardOtherInput.setMaxLength(200);
		measureStewardOtherInput.setWidth("415px");
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
		String emeasureIdMSG = "Once an eMeasure Identifier (Measure Authoring Tool) has been generated it may not be modified or removed for any draft or version of a measure.";
		generateeMeasureIDButton.setTitle(emeasureIdMSG);
		eMeasureIdentifierInput.setTitle(emeasureIdMSG);
		return fPanel;
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
		itemCountHeader.setStyleName("recentSearchHeader");
		com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
		itemCountHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(itemCountHeader.getElement());
		MatCheckBoxCell qdmCheckBox = new MatCheckBoxCell(false, true, !isEditable);
			Column<QualityDataSetDTO, Boolean> chkBoxColumn = new  Column<QualityDataSetDTO, Boolean>(qdmCheckBox) {

				@Override
				public Boolean getValue(QualityDataSetDTO object) {
					if (qdmSelectedList.size() > 0) {
					for (int i = 0; i < qdmSelectedList.size(); i++) {
						if (qdmSelectedList.get(i).getUuid().equalsIgnoreCase(object.getUuid())) {
							object.setUsedMD(true);
							break;
						}
					}
				} else {
					object.setUsedMD(false);
					}
					return object.isUsedMD();
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
			
			cellTable.addColumn(vsacDataType, SafeHtmlUtils.fromSafeConstant("<span title='Data Type'>" + "Data Type"
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
			cellTable.setRowData(appliedListModel.getAppliedQDMs());
			cellTable.setRowCount(appliedListModel.getAppliedQDMs().size());
			cellTable.redraw();
			sortProvider.refresh();
			sortProvider.getList().addAll(appliedListModel.getAppliedQDMs());
			ListHandler<QualityDataSetDTO> sortHandler = new ListHandler<QualityDataSetDTO>(sortProvider.getList());
			cellTable.addColumnSortHandler(sortHandler);
			cellTable = addColumnToTable(cellTable, isEditable);
			updateQDMSelectedList(appliedListModel.getAppliedQDMs());
			sortProvider.addDataDisplay(cellTable);
			Label invisibleLabel = (Label) LabelBuilder
					.buildInvisibleLabel(
							"appliedQDMTableSummary",
							"In the Following Applied QDM Elements table a checkBoxCell is positioned to "
									+ "the left of the table with a select Column header followed by "
									+ "QDM name in second column and Datatype in third column. " 
									+ "The Applied QDM elements are listed alphabetically in a table.");
			Label label = (Label)LabelBuilder
					.buildInvisibleLabel("selectedItemsSummary","Selected Items: " + qdmSelectedList.size());
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
			
		} else{
			HTML desc = new HTML("<p> No Applied QDM Elements.</p>");
			qdmItemCountListSPanel.setSize("200px", "50px");
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
					if (qdmSelectedList.get(i).getUuid().equalsIgnoreCase(selectedList.get(j).getUuid())) {
						qdmSelectedList.set(i, selectedList.get(j));
						break;
					}
				}
			}
		}
		
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
					if (componentMeasureSelectedList.get(i).getId().equalsIgnoreCase(measuresSelectedList.get(j).getId())) {
						componentMeasureSelectedList.set(i, measuresSelectedList.get(j));
						break;
					}
				}
			}
		}
		
	}
	
	
	/**
	 * Adds the measures column to table.
	 *
	 * @param isEditable the is editable
	 * @return the cell table
	 */
	private CellTable<ManageMeasureSearchModel.Result> addMeasuresColumnToTable(boolean isEditable){
		Label measureSearchHeader = new Label("Component Measures List");
		measureSearchHeader.getElement().setId("measureSearchHeader_Label");
		measureSearchHeader.setStyleName("recentSearchHeader");
		com.google.gwt.dom.client.TableElement elem = componentMeasureCellTable.getElement().cast();
		measureSearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());
		MatCheckBoxCell measuresListCheckBox = new MatCheckBoxCell(false, true, !isEditable);
		Column<ManageMeasureSearchModel.Result, Boolean> selectColumn = 
				new Column<ManageMeasureSearchModel.Result, Boolean>(measuresListCheckBox){
			@Override
			public Boolean getValue(ManageMeasureSearchModel.Result object) {
				boolean isSelected = false;
				if (componentMeasureSelectedList.size() > 0) {
					for (int i = 0; i < componentMeasureSelectedList.size(); i++) {
						if (componentMeasureSelectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
							isSelected = true;
							break;
						}
					}
				} else {
					isSelected = false;
					}
					return isSelected;
			}};
			
			selectColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, Boolean>() {
				
				@Override
				public void update(int index, Result object, Boolean value) {
					System.out.println("Componenet Measures selectionModel:");
					measuresListSelectionModel.setSelected(object, value);
					if (value) {
						componentMeasureSelectedList.add(object);
					} else {
						for (int i = 0; i < componentMeasureSelectedList.size(); i++) {
							if (componentMeasureSelectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
								componentMeasureSelectedList.remove(i);
								break;
							}
						}
	                
					}
					componentMeasuresLabel.setText("Selected Items: " + componentMeasureSelectedList.size());
					
				}
			});
		
			componentMeasureCellTable.addColumn(selectColumn, SafeHtmlUtils.fromSafeConstant("<span title='Select'>"
					+ "Select" + "</span>"));
			
			Column<ManageMeasureSearchModel.Result, SafeHtml> measureNameColumn = 
					new Column<ManageMeasureSearchModel.Result, SafeHtml>(new SafeHtmlCell()){

						@Override
						public SafeHtml getValue(Result object) {
							return CellTableUtility.getColumnToolTip(object.getName());
						}};
						
						
		  componentMeasureCellTable.addColumn(measureNameColumn, SafeHtmlUtils.fromSafeConstant("<span title='Measure Name'>"
					+ "Measure Name" + "</span>"));
		  
		  
		  Column<ManageMeasureSearchModel.Result, SafeHtml> versionColumn = 
					new Column<ManageMeasureSearchModel.Result, SafeHtml>(new SafeHtmlCell()){

						@Override
						public SafeHtml getValue(Result object) {
							return CellTableUtility.getColumnToolTip(object.getVersion());
						}};
						
						
		  componentMeasureCellTable.addColumn(versionColumn, SafeHtmlUtils.fromSafeConstant("<span title='Version'>"
					+ "Version" + "</span>"));
		  
		  Column<ManageMeasureSearchModel.Result, SafeHtml> finalizedDateColumn = 
					new Column<ManageMeasureSearchModel.Result, SafeHtml>(new SafeHtmlCell()){

						@Override
						public SafeHtml getValue(Result object) {
							return CellTableUtility.getColumnToolTip(convertTimestampToString(object.getFinalizedDate()));
						}};
						
						
		  componentMeasureCellTable.addColumn(finalizedDateColumn, SafeHtmlUtils.fromSafeConstant("<span title='Finalized Date'>"
					+ "Finalized Date" + "</span>"));
			
		
		return componentMeasureCellTable;
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#buildComponentMeasuresCellTable(mat.client.measure.ManageMeasureSearchModel, boolean)
	 */
	@Override
	public void buildComponentMeasuresCellTable(ManageMeasureSearchModel result, boolean isEditable){
		horzComponentMeasurePanel.clear(); 
		componentMeasuresListSPanel.clear();
		componentMeasuresListVPanel.clear();
		componentMeasuresSelectedListVPanel.clear();
		HorizontalPanel searchButtonPanel = new HorizontalPanel();
		VerticalPanel vPanelHolder = new VerticalPanel();
		HorizontalPanel horizontalHolder= new HorizontalPanel();
		componentMeasuresListSPanel.setStyleName("cellTablePanel");
		horizontalHolder.setStyleName("floatRight");
		searchButton.getElement().setId("componentMeasureSearch_Button");
		searchString.getElement().setId("measureSearchInput_TextBox");
		searchString.setText("search...");
		componentMeasureCellTable = new CellTable<ManageMeasureSearchModel.Result>();
		componentMeasureCellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<ManageMeasureSearchModel.Result> sortProvider = 
				new ListDataProvider<ManageMeasureSearchModel.Result>();
		selectedMeasureList = new ArrayList<ManageMeasureSearchModel.Result>();
		measuresListSelectionModel = new MultiSelectionModel<ManageMeasureSearchModel.Result>();
		componentMeasureCellTable.setSelectionModel(measuresListSelectionModel);
		selectedMeasureList.addAll(result.getData());
		componentMeasureCellTable.setRowData(selectedMeasureList);
		componentMeasureCellTable.setPageSize(5);
		componentMeasureCellTable.redraw();
		componentMeasureCellTable.setRowCount(selectedMeasureList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(result.getData());
		componentMeasureCellTable = addMeasuresColumnToTable(isEditable);
		updateComponentMeasuresSelectedList(selectedMeasureList);
		sortProvider.addDataDisplay(componentMeasureCellTable);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
		spager.setPageStart(0);
		spager.setDisplay(componentMeasureCellTable);
		spager.setPageSize(5);
		componentMeasureCellTable.setWidth("100%");
		componentMeasuresListSPanel.setSize("500px", "150px");
		searchButtonPanel.setWidth("500px");
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("componentMeasureListSummary",
				"In the following Measure List table, Measure Name is given in first column,"
						+ " Version in second column, Finalized Date in third column,"
						+ "History in fourth column, Edit in fifth column, Share in sixth column"
						+ "Clone in seventh column and Export in eight column.");
		Label label = (Label)LabelBuilder
				.buildInvisibleLabel("selectedComponentMeasuresSummary","Selected Items: "+ componentMeasureSelectedList.size());
		componentMeasureCellTable.getElement().setAttribute("id", "ComponentMeasuresListCellTable");
		componentMeasureCellTable.getElement().setAttribute("aria-describedby", "componentMeasureListSummary");
		vPanelHolder.add(invisibleLabel);
		vPanelHolder.add(componentMeasureCellTable);
		vPanelHolder.add(new SpacerWidget());
		vPanelHolder.add(spager);
		componentMeasuresListSPanel.add(vPanelHolder);
		searchButtonPanel.add(LabelBuilder.buildLabel(componentMeasureCellTable, " Component Measures Counted - Optional"));
		horizontalHolder.add(searchString);
		horizontalHolder.add(searchButton);
		searchButtonPanel.add(horizontalHolder);
		componentMeasuresListVPanel.add(searchButtonPanel);
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
		
	}
	
	/**
	 * Gets the image.
	 *
	 * @param action the action
	 * @param url the url
	 * @param key the key
	 * @param id the id
	 * @return the image
	 */
	private Widget getImage(String action, ImageResource url, String key , String id) {
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonTextMeasureLibrary");
		image.setTitle(action);
		image.setResource(url, action);
		image.getElement().setAttribute("id", id);
		return image;
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
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureName()
	 */
	@Override
	public Label getMeasureName() {
		return nameInput;
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
	
	
	//public HasClickHandlers getSearchButton(){
		//return searchButton;
	//}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseMetaDataDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getEditAuthorsButton()
	 */
	@Override
	public HasClickHandlers getEditAuthorsButton() {
		return addEditAuthors;
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
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}
	
	
	/**
	 * Sets the list box options.
	 * 
	 * @param input
	 *            the input
	 * @param itemList
	 *            the item list
	 * @param defaultText
	 *            the default text
	 */
	private void setListBoxOptions(ListBox input, List<? extends HasListBox> itemList, String defaultText) {
		input.clear();
		if (defaultText != null) {
			input.addItem(defaultText, "");
		}
		if (itemList != null) {
			for (HasListBox listBoxContent : itemList) {
				input.addItem(listBoxContent.getItem(), "" + listBoxContent.getValue());
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseMetaDataDisplay#setMeasureStewardOptions(java.util.List)
	 */
	@Override
	public void setMeasureStewardOptions(List<? extends HasListBox> itemList) {
		setListBoxOptions(measureStewardInput, itemList, "--Select--");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getVersionNumber()
	 */
	@Override
	public Label getVersionNumber() {
		return versionInput;
	}
	
	
	/*@Override
	public HasValue<String> getMeasureId() {
		return measureIdInput;
	}*/
	
	
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
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureSteward()
	 */
	@Override
	public ListBoxMVP getMeasureSteward() {
		return measureStewardInput;
	}
	
	//US 413
	/* Returns the Steward Other text box object
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureStewardOther()
	 */
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureStewardOther()
	 */
	@Override
	public TextBox getMeasureStewardOther() {
		return measureStewardOtherInput;
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
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setAuthorsList(java.util.List)
	 */
	@Override
	public void setAuthorsList(List<Author> authorList) {
		emptyAuthorsPanel.clear();
		authorListBox.clear();
		for (Author author: authorList) {
			authorListBox.addItem(author.getAuthorName());
		}
		emptyAuthorsPanel.add(authorListBox);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setMeasureTypeList(java.util.List)
	 */
	@Override
	public void setMeasureTypeList(List<MeasureType> measureType) {
		emptyMeasureTypePanel.clear();
		measureTypeListBox.clear();
		for (MeasureType mt: measureType) {
			measureTypeListBox.addItem(mt.getDescription());
		}
		emptyMeasureTypePanel.add(measureTypeListBox);
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
	private void  addRow(FlexTable reference) {
		TextAreaWithMaxLength newReferenceBox = createReferenceInput();
		++counter;
		String dynamicLabel = "Reference" + counter;
		Widget newReferenceBoxLabel = LabelBuilder.buildInvisibleLabel(newReferenceBox, dynamicLabel);
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(newReferenceBoxLabel);
		hp.add(newReferenceBox);
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
	
	//US 413
	/* Returns the text value of Measure Steward
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureStewardValue()
	 */
	/* (non-Javadoc)
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
	/* (non-Javadoc)
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
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureStewardOtherValue()
	 */
	@Override
	public String getMeasureStewardOtherValue() {
		return measureStewardOtherInput.getValue();
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
	
	
	//US 413
	/* Clears out the Steward Other panel and re-draw the Steward Other input components
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#showOtherTextBox()
	 */
	/* (non-Javadoc)
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
	/* (non-Javadoc)
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
	private void clearOtherPanel() {
		measureStewardOtherInput.setValue(null);
		emptyTextBoxHolder.clear();
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureStatus()
	 */
	@Override
	public ListBoxMVP getMeasureStatus() {
		return objectStatusInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setAddEditButtonsVisible(boolean)
	 */
	@Override
	public void setAddEditButtonsVisible(boolean b) {
		addEditAuthors.setEnabled(b);
		addEditMeasureType.setEnabled(b);
		measurePeriodFromInput.setEnableCSS(b);
		measurePeriodToInput.setEnableCSS(b);
		AddRowButton.setEnabled(b);
		
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
	@Override
	public void setObjectStatusOptions(List<? extends HasListBox> texts) {
		setListBoxOptions(objectStatusInput, texts, MatContext.PLEASE_SELECT);
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#getMeasureStatusValue()
	 */
	@Override
	public String getMeasureStatusValue() {
		return objectStatusInput.getItemText(objectStatusInput.getSelectedIndex());
	}
	
	
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


	/*@Override
	public void buildCellList(QDSAppliedListModel appliedListModel) {
		initializeQDMCellListContent(appliedListModel);
	}*/


	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.MetaDataDetailDisplay#setAppliedQDMList(java.util.ArrayList)
	 */
	@Override
	public void setAppliedQDMList(ArrayList<QualityDataSetDTO> appliedQDMList) {
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
 
	
}
