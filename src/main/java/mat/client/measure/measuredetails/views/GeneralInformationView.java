package mat.client.measure.measuredetails.views;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.*;
import mat.client.codelist.HasListBox;
import mat.client.measure.measuredetails.observers.GeneralInformationObserver;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.*;
import mat.client.validator.ErrorHandler;
import mat.dto.CompositeMeasureScoreDTO;
import mat.model.clause.ModelTypeHelper;
import mat.shared.CompositeMethodScoringConstant;
import mat.shared.MatConstants;
import mat.shared.StringUtility;
import mat.shared.measure.measuredetails.models.GeneralInformationModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.validator.measure.CommonMeasureValidator;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneralInformationView implements MeasureDetailViewInterface {
    private static final String EMPTY_STRING = "";
    private static final String TEXT_BOX_WIDTH = "300px";
    private static final int ECQM_ABBR_MAX_LENGTH = 32;
    private static final int MEASURE_NAME_MAX_LENGTH = 500;
    private FlowPanel mainPanel = new FlowPanel();

    private GeneralInformationModel generalInformationModel;
    private GeneralInformationModel originalModel;
    private TextBox abbrInput = new TextBox();
    private TextBox measureNameInput = new TextBox();
    private TextBox guidTextBox = new TextBox();
    private TextBox finalizedDateTextBox = new TextBox();
    private TextBox eCQMVersionNumberTextBox = new TextBox();
    private boolean isCompositeMeasure = false;
    private ListBoxMVP measureScoringInput = new ListBoxMVP();
    private ListBoxMVP populationBasisInput = new ListBoxMVP();
    private ListBoxMVP compositeScoringMethodInput = new ListBoxMVP();
    private ListBoxMVP patientBasedInput = new ListBoxMVP();
    private ListBoxMVP endorsedByListBox = new ListBoxMVP();
    private GeneralInformationObserver observer;
    List<CompositeMeasureScoreDTO> compositeChoices;
    private HelpBlock helpBlock = new HelpBlock();
    private FormGroup messageFormGrp = new FormGroup();
    private Button generateEMeasureIDButton = new Button("Generate Identifier");
    private TextBox eMeasureIdentifierInput = new TextBox();
    private TextBox nQFIDInput = new TextBox();
    private CustomCheckBox calendarYear = new CustomCheckBox("Click to select custom measurement period", "Click to select custom measurement period", false);
    protected DateBoxWithCalendar measurePeriodFromInput = new DateBoxWithCalendar();
    protected DateBoxWithCalendar measurePeriodToInput = new DateBoxWithCalendar();
    protected CheckBox experimentalCheckbox = new CheckBox();
    private FormLabel measureNameLabel;
    private ErrorHandler errorHandler = new ErrorHandler();
    private static final Logger logger = Logger.getLogger(GeneralInformationView.class.getSimpleName());

    public GeneralInformationView(boolean isComposite, GeneralInformationModel originalGeneralInformationModel) {
        originalModel = originalGeneralInformationModel;
        buildGeneralInformationModel(originalGeneralInformationModel);
        this.isCompositeMeasure = isComposite;
        compositeChoices = MatContext.get().buildCompositeScoringChoiceList();
        buildDetailView();
    }

    private void buildGeneralInformationModel(GeneralInformationModel originalGeneralInformationModel) {
        this.generalInformationModel = new GeneralInformationModel(originalGeneralInformationModel);
    }

    public Widget getWidget() {
        return mainPanel;
    }

    @Override
    public boolean hasUnsavedChanges() {
        return !originalModel.equals(generalInformationModel);
    }

    @Override
    public void buildDetailView() {
        boolean isFhir = ModelTypeHelper.isFhir(MatContext.get().getCurrentMeasureModel());
        mainPanel.clear();
        HorizontalPanel detailPanel = new HorizontalPanel();
        Form measureDetailForm = new Form();
        helpBlock.setHeight("0px");
        helpBlock.setWidth("0px");
        helpBlock.setColor("transparent");
        messageFormGrp.add(helpBlock);
        messageFormGrp.getElement().setAttribute("role", "alert");
        measureDetailForm.add(messageFormGrp);
        detailPanel.add(measureDetailForm);
        Grid panelGrid = new Grid(8, 2);

        panelGrid.setWidget(0, 0, buildMeasureNamePanel());
        panelGrid.setWidget(0, 1, buildAbbreviationPanel());
        panelGrid.setWidget(1, 0, buildMeasureScoringPanel());
        panelGrid.setWidget(1, 1, buildPatientBasedPanel());
        panelGrid.setWidget(2, 0, buildFinalizedDate());
        panelGrid.setWidget(2, 1, buildGUIDPanel());
        if (isFhir) {
            panelGrid.setWidget(3, 0, buildPopulationBasis());
            panelGrid.setWidget(3, 1, buildExperimentalPanel());
        }
        panelGrid.setWidget(4, 0, buildeCQMVersionPanel());
        panelGrid.setWidget(5, 0, buldeCQMIdentifierPanel());
        panelGrid.setWidget(6, 0, buildNQFNumberPanel());
        panelGrid.setWidget(7, 0, buildMeasurementPeriodPanel());
        hackTheColspan(panelGrid);

        if (isCompositeMeasure) {
            panelGrid.insertRow(1);
            VerticalPanel compositeScoringPanel = buildCompositeScoringPanel();
            panelGrid.setWidget(1, 0, compositeScoringPanel);
            VerticalPanel blankPanel = buildBlankPanel();
            panelGrid.setWidget(1, 1, blankPanel);
        }

        detailPanel.add(panelGrid);
        mainPanel.add(detailPanel);
        buildDropDowns();
        addEventHandlers();

    }

    private void hackTheColspan(Grid panelGrid) {
        panelGrid.getCellFormatter().getElement(7, 1).removeFromParent();
        panelGrid.getCellFormatter().getElement(7, 0).setAttribute("colspan", "2");
    }

    private VerticalPanel buildMeasurementPeriodPanel() {
        VerticalPanel measurementPeriodPanel = new VerticalPanel();
        measurementPeriodPanel.getElement().setAttribute("verticalAlign", "middle");
        measurementPeriodPanel.add(new SpacerWidget());
        measurementPeriodPanel.getElement().setId("measurementPeriod_VerticalPanel");
        measurementPeriodPanel.setSize("505px", "100px");
        FormLabel measurePeriodFromInputLabel = new FormLabel();
        measurePeriodFromInputLabel.setText("Measurement Period");
        measurePeriodFromInputLabel.getElement().setId("measurementPeriodHeader_Label");
        measurementPeriodPanel.add(measurePeriodFromInputLabel);

        HorizontalPanel calendarYearDatePanel = new HorizontalPanel();
        calendarYearDatePanel.getElement().setId("calendarYear_HorizontalPanel");
        calendarYearDatePanel.add(calendarYear);
        FormLabel calendarLabel = new FormLabel();
        if (ModelTypeHelper.isFhir(MatContext.get().getCurrentMeasureModel())) {
            calendarLabel.setText("Next Calendar Year");
        } else {
            calendarLabel.setText("Calendar Year (January 1, 20XX through December 31, 20XX)");
        }
        calendarLabel.getElement().getStyle().setPaddingTop(5.0, Unit.PX);
        calendarYearDatePanel.getElement().setAttribute("verticalAlign", "middle");
        calendarYearDatePanel.add(calendarLabel);
        calendarYear.getElement().setId("calendarYear_CustomCheckBox");


        HorizontalPanel fromPanel = new HorizontalPanel();
        fromPanel.getElement().setId("measurePeriodPanel_HorizontalPanel");
        FormLabel fromLabel = new FormLabel();
        fromLabel.setText("From");
        fromLabel.setTitle("From");
        fromLabel.setMarginTop(5.00);
        fromLabel.addStyleName("firstLabel");
        fromPanel.add(fromLabel);
        measurePeriodFromInput.getDateBox().getElement().setAttribute("id", "measurePeriodFromInput");
        measurePeriodFromInput.getDateBox().setWidth("127px");
        measurePeriodFromInput.getCalendar().setTitle("Click to select From date.");
        fromPanel.add(measurePeriodFromInput);
        measurePeriodFromInput.getElement().setId("measurePeriodFromInput_DateBoxWithCalendar");
        measurePeriodFromInput.getDateBox().addBlurHandler(errorHandler.buildBlurHandler(
                measurePeriodFromInput.getDateBox(),
                fromPanel.getElement(),
                (s) -> {
                    try {
                        com.google.gwt.i18n.shared.DateTimeFormat dateFormat = com.google.gwt.i18n.shared.DateTimeFormat.getFormat("MM/dd/yyyy");
                        dateFormat.parseStrict(generalInformationModel.getMeasureFromPeriod());
                    } catch (Exception e) {
                        return "Not MM/DD/YYYY format.";
                    }
                    return null;
                }));

        HorizontalPanel toPanel = new HorizontalPanel();
        FormLabel toLabel = new FormLabel();
        toLabel.setText("To");
        toLabel.setTitle("To");
        toLabel.setMarginTop(5.00);
        toLabel.addStyleName("secondLabel");
        toPanel.add(toLabel);
        measurePeriodToInput.getDateBox().setWidth("127px");
        measurePeriodToInput.getCalendar().setTitle("Click to select To date.");
        measurePeriodToInput.getDateBox().getElement().setAttribute("id", "measurePeriodToInput");
        toPanel.add(measurePeriodToInput);
        measurePeriodToInput.getElement().setId("measurePeriodToInput_DateBoxWithCalendar");
        measurePeriodToInput.getDateBox().addBlurHandler(errorHandler.buildBlurHandler(
                measurePeriodToInput.getDateBox(),
                toPanel.getElement(),
                (s) -> {
                    try {
                        com.google.gwt.i18n.shared.DateTimeFormat dateFormat = com.google.gwt.i18n.shared.DateTimeFormat.getFormat("MM/dd/yyyy");
                        Date toDate = dateFormat.parseStrict(generalInformationModel.getMeasureToPeriod());
                        if (generalInformationModel.getMeasureFromPeriod() != null) {
                            Date fromDate = dateFormat.parseStrict(generalInformationModel.getMeasureFromPeriod());
                            if (fromDate.after(toDate)) {
                                return "To date must be after From date.";
                            }
                        }

                    } catch (Exception e) {
                        return "Not MM/DD/YYYY format.";
                    }
                    return null;
                }));

        HorizontalPanel measurePeriodPanel = new HorizontalPanel();
        measurePeriodPanel.add(fromPanel);
        measurePeriodPanel.add(toPanel);

        Grid queryGrid = new Grid(3, 1);
        queryGrid.setWidget(0, 0, calendarYearDatePanel);
        queryGrid.setWidget(1, 0, new SpacerWidget());
        queryGrid.setWidget(2, 0, measurePeriodPanel);
        measurementPeriodPanel.add(queryGrid);
        queryGrid.getElement().setId("queryGrid_Grid");

        if (generalInformationModel.isCalendarYear()) {
            calendarYear.setTitle("Click to select custom measurement period");
        } else {
            calendarYear.setTitle("Click to select calendar year measurement period");
        }
        calendarYear.setValue(generalInformationModel.isCalendarYear());
        if (generalInformationModel.isCalendarYear() && ModelTypeHelper.isFhir(MatContext.get().getCurrentMeasureModel())) {
            String year = DateTimeFormat.getFormat("d-M-yyyy").format(new Date()).split("-")[2];
            int nextCalenderYear = Integer.parseInt(year) + 1;
            generalInformationModel.setMeasureFromPeriod("01/01/" + nextCalenderYear);
            generalInformationModel.setMeasureToPeriod("12/31/" + nextCalenderYear);
        }
        measurePeriodFromInput.setValue(generalInformationModel.getMeasureFromPeriod());
        measurePeriodToInput.setValue(generalInformationModel.getMeasureToPeriod());
        return measurementPeriodPanel;
    }

    private VerticalPanel buildNQFNumberPanel() {
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.getElement().addClassName("generalInformationPanel");
        HorizontalPanel nqfNumberEndorsmentPanel = new HorizontalPanel();
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
        nQFIDInput.getElement().setAttribute("style", "width:150px;margin-top:-10px;");
        if (generalInformationModel.getNqfId() != null) {
            nQFIDInput.setText(generalInformationModel.getNqfId());
            nQFIDInput.setTitle(generalInformationModel.getNqfId());
        } else {
            nQFIDInput.setPlaceholder(MatConstants.ENTER_NQF_NUMBER);
            nQFIDInput.setTitle(MatConstants.ENTER_NQF_NUMBER);
        }

        FormLabel endorsedByNQFLabel = new FormLabel();
        endorsedByNQFLabel.setText("Endorsed By NQF");
        nqfNumberLeftVP.add(endorsedByNQFLabel);
        endorsedByListBox.setWidth("150px");
        endorsedByListBox.setId("endorsedByNQFListBox");
        nqfNumberLeftVP.add(endorsedByListBox);
        nqfNumberRightVP.getElement().setAttribute("style", "padding-left:10px;");
        nqfNumberEndorsmentPanel.add(nqfNumberLeftVP);
        nqfNumberEndorsmentPanel.add(nqfNumberRightVP);
        verticalPanel.add(nqfNumberEndorsmentPanel);
        resetEndorsedByListBox();

        boolean endorsedByNQF = generalInformationModel.getEndorseByNQF() != null ? generalInformationModel.getEndorseByNQF() : false;
        if (endorsedByNQF) {
            endorsedByListBox.setSelectedIndex(endorsedByNQF ? 1 : 0);
        }

        setNQFTitle(endorsedByNQF);
        return verticalPanel;
    }

    public void setNQFTitle(boolean endorsedByNQF) {
        if (!endorsedByNQF) {
            helpBlock.setText("You have chosen no; the NQF number field has been cleared. It now reads as Not Applicable and is disabled.");
            nQFIDInput.setPlaceholder(MatConstants.NOT_APPLICABLE);
            nQFIDInput.setTitle(MatConstants.NOT_APPLICABLE);
            nQFIDInput.setText("");
            nQFIDInput.setReadOnly(true);
            nQFIDInput.setEnabled(false);
            nQFIDInput.setTabIndex(-1);
        } else {
            helpBlock.setText("You have chosen yes, the NQF number field is now enabled and required.");
            if (!StringUtility.isEmptyOrNull(nQFIDInput.getText())) {
                String placeHolderText = nQFIDInput.getText();
                nQFIDInput.setPlaceholder(placeHolderText);
                nQFIDInput.setTitle(placeHolderText);
            } else {
                nQFIDInput.setPlaceholder(MatConstants.ENTER_NQF_NUMBER);
                nQFIDInput.setTitle(MatConstants.ENTER_NQF_NUMBER);
            }
            nQFIDInput.setReadOnly(false);
            nQFIDInput.setEnabled(true);
            nQFIDInput.setTabIndex(0);
        }
    }

    private VerticalPanel buldeCQMIdentifierPanel() {
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.getElement().addClassName("generalInformationPanel");
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.getElement().setId("horizontalPanel_HorizontalFlowPanelLeft");
        FormLabel eMeasureIdentifierInputLabel = new FormLabel();
        eMeasureIdentifierInputLabel.setStyleName("bold");
        eMeasureIdentifierInputLabel.setText("eCQM Identifier (Measure Authoring Tool)");
        eMeasureIdentifierInputLabel.setId("eMeasureIdentifierInputLabel");
        eMeasureIdentifierInputLabel.setFor("eMeasureIdentifierInput_TextBox");
        verticalPanel.add(new SpacerWidget());
        verticalPanel.add(eMeasureIdentifierInputLabel);
        eMeasureIdentifierInput.setId("eMeasureIdentifierInput_TextBox");
        eMeasureIdentifierInput.setTitle("Generated Identifier");
        eMeasureIdentifierInput.setWidth("150px");

        if (generalInformationModel.geteMeasureId() != 0) {
            eMeasureIdentifierInput.setText(String.valueOf(generalInformationModel.geteMeasureId()));
            eMeasureIdentifierInput.setValue(String.valueOf(generalInformationModel.geteMeasureId()));
            generateEMeasureIDButton.setEnabled(false);
        }

        horizontalPanel.add(eMeasureIdentifierInput);
        horizontalPanel.add(generateEMeasureIDButton);
        horizontalPanel.add(eMeasureIdentifierInput);
        horizontalPanel.add(generateEMeasureIDButton);
        generateEMeasureIDButton.setType(ButtonType.PRIMARY);
        generateEMeasureIDButton.getElement().getStyle().setProperty("marginLeft", "5px");
        generateEMeasureIDButton.getElement().setId("generateeMeasureIDButton_Button");
        eMeasureIdentifierInput.setReadOnly(true);
        eMeasureIdentifierInput.setEnabled(false);
        eMeasureIdentifierInput.setTabIndex(-1);
        generateEMeasureIDButton.setEnabled(true);
        String emeasureIdMSG = "Once an eCQM Identifier (Measure Authoring Tool) has been generated it may not be modified or removed for any draft or version of a measure.";
        generateEMeasureIDButton.setTitle(emeasureIdMSG);
        eMeasureIdentifierInput.setTitle(emeasureIdMSG);
        verticalPanel.add(horizontalPanel);
        return verticalPanel;
    }

    private void buildDropDowns() {
        setCompositeScoringChoices(compositeChoices);
        if (isCompositeMeasure) {
            setCompositeScoringSelectedValue(generalInformationModel.getCompositeScoringMethod());
        } else {
            MatContext.get().getListBoxCodeProvider().getScoringList(new AsyncCallback<List<? extends HasListBox>>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(MessageDelegate.s_ERR_RETRIEVE_SCORING_CHOICES);
                }

                @Override
                public void onSuccess(List<? extends HasListBox> result) {
                    setScoringChoices(result);
                    measureScoringInput.setValueMetadata(generalInformationModel.getScoringMethod());
                    setPatientBasedInputOptions(MatContext.get().getPatientBasedIndicatorOptions(generalInformationModel.getScoringMethod()));
                    setPatientbasedIndicator();
                }
            });
            //setting up population basis values retrieved from Spreadsheet (Displayed only for FHIR)
            setPopulationBasisList(MatContext.get().getCqlConstantContainer().getPopulationBasisValidValues());
            populationBasisInput.setValueMetadata(generalInformationModel.getPopulationBasis());

        }
    }

    private void setPatientbasedIndicator() {
        patientBasedInput.setSelectedIndex(generalInformationModel.isPatientBased() ? 1 : 0);
    }

    private void addEventHandlers() {
        getGenerateEMeasureIDButton().addClickHandler(event -> observer.generateAndSaveNewEmeasureid());
        getMeasureScoringInput().addChangeHandler(event -> observer.handleMeasureScoringChanged());
        getCompositeScoringMethodInput().addChangeHandler(event -> observer.handleCompositeScoringChanged());
        getPatientBasedInput().addChangeHandler(event -> observer.handleInputChanged());
        getECQMAbbrInput().addChangeHandler(event -> observer.handleInputChanged());
        getMeasureNameInput().addChangeHandler(event -> observer.handleInputChanged());
        getEndorsedByListBox().addChangeHandler(event -> observer.handleEndorsedByNQFChanged());
        getnQFIDInput().addChangeHandler(event -> observer.handleInputChanged());
        getCalenderYear().addClickHandler(event -> observer.handleCalendarYearChanged());
        getMeasurePeriodFromInput().addValueChangeHandler(event -> observer.handleInputChanged());
        getMeasurePeriodToInput().addValueChangeHandler(event -> observer.handleInputChanged());
        getExperimentalCheckbox().addValueChangeHandler(event -> observer.handleInputChanged());
        getPopulationBasisInput().addValueChangeHandler(event -> observer.handleInputChanged());

        getMeasureScoringInput().addBlurHandler(errorHandler.buildBlurHandler(
                getMeasureScoringInput(),
                s -> getFirst(CommonMeasureValidator.validateMeasureScore(s))));
    }

    private VerticalPanel buildBlankPanel() {
        VerticalPanel blankPanel = new VerticalPanel();
        blankPanel.getElement().addClassName("generalInformationPanel");
        blankPanel.add(new SpacerWidget());
        return blankPanel;
    }

    private VerticalPanel buildFinalizedDate() {
        VerticalPanel panel = new VerticalPanel();
        panel.getElement().addClassName("generalInformationPanel");
        FormLabel finalizedDateLabel = new FormLabel();
        finalizedDateLabel.setText("Finalized Date");
        finalizedDateLabel.setTitle(finalizedDateLabel.getText());
        panel.add(finalizedDateLabel);
        finalizedDateLabel.setId("finalizedDateLabel");
        finalizedDateLabel.setFor("finalizedDate");
        finalizedDateTextBox.setId("finalizedDate");
        finalizedDateTextBox.setReadOnly(true);
        finalizedDateTextBox.setEnabled(false);
        finalizedDateTextBox.setWidth(TEXT_BOX_WIDTH);
        panel.add(finalizedDateTextBox);
        panel.add(new SpacerWidget());

        finalizedDateTextBox.setText(generalInformationModel.getFinalizedDate());
        return panel;
    }

    private VerticalPanel buildeCQMVersionPanel() {
        VerticalPanel panel = new VerticalPanel();
        panel.getElement().addClassName("generalInformationPanel");

        FormLabel eCQMVersionNumberLabel = new FormLabel();
        eCQMVersionNumberLabel.setText("eCQM Version Number");
        eCQMVersionNumberLabel.setTitle(eCQMVersionNumberLabel.getText());
        eCQMVersionNumberLabel.setId("versionInputLabel");
        eCQMVersionNumberLabel.setFor("versionInput");
        eCQMVersionNumberTextBox.setReadOnly(true);
        eCQMVersionNumberTextBox.setEnabled(false);
        eCQMVersionNumberTextBox.setWidth(TEXT_BOX_WIDTH);
        eCQMVersionNumberTextBox.setId("versionInput");

        eCQMVersionNumberTextBox.setText(generalInformationModel.geteCQMVersionNumber());

        panel.add(eCQMVersionNumberLabel);
        panel.add(eCQMVersionNumberTextBox);
        panel.add(new SpacerWidget());

        return panel;
    }

    private VerticalPanel buildExperimentalPanel() {
        VerticalPanel panel = new VerticalPanel();
        panel.getElement().addClassName("generalInformationPanel");
        FormLabel experimentalLabel = new FormLabel();
        experimentalLabel.setText("Experimental");
        experimentalLabel.setTitle("Experimental");
        experimentalLabel.setId("experimentalCB");
        experimentalLabel.setFor("versionInput");
        experimentalCheckbox.setEnabled(false);
        experimentalCheckbox.getElement().setAttribute("id", "experimentalCB");
        experimentalCheckbox.setValue(generalInformationModel.isExperimental());
        experimentalCheckbox.getElement().setAttribute("aria-label","Experimental Checkbox");

        panel.add(experimentalLabel);
        panel.add(experimentalCheckbox);
        panel.add(new SpacerWidget());
        return panel;
    }

    private VerticalPanel buildGUIDPanel() {
        VerticalPanel panel = new VerticalPanel();
        panel.getElement().addClassName("generalInformationPanel");
        FormLabel guidLabel = new FormLabel();
        guidLabel.setText("GUID");
        guidLabel.setTitle(guidLabel.getText());
        guidLabel.setId("guidLabel");
        guidLabel.setFor("guidLabel");
        guidTextBox.setId("guidLabel");
        guidTextBox.setReadOnly(true);
        guidTextBox.setEnabled(false);
        guidTextBox.setWidth(TEXT_BOX_WIDTH);
        panel.add(guidLabel);
        panel.add(guidTextBox);
        panel.add(new SpacerWidget());

        guidTextBox.setText(generalInformationModel.getGuid());
        return panel;
    }

    private String getFirst(List<String> list) {
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    private VerticalPanel buildMeasureNamePanel() {
        VerticalPanel measureNamePanel = new VerticalPanel();
        measureNamePanel.getElement().addClassName("generalInformationPanel");
        measureNameLabel = buildMeasureNameLabel();
        measureNameInput.setId("measureNameInput");
        measureNameInput.setText(generalInformationModel.getMeasureName());
        measureNameInput.setWidth(TEXT_BOX_WIDTH);
        measureNameInput.setMaxLength(MEASURE_NAME_MAX_LENGTH);
        if (MatContext.get().isCurrentMeasureModelFhir()) {
            measureNameInput.addBlurHandler(errorHandler.buildBlurHandler(measureNameInput, (s) -> getFirst(CommonMeasureValidator.validateFhirMeasureName(s))));
        }
        measureNameInput.addBlurHandler(errorHandler.buildBlurHandler(measureNameInput,(s) -> getFirst(CommonMeasureValidator.validateMeasureName(s))));
        measureNamePanel.add(measureNameLabel);
        measureNamePanel.add(measureNameInput);
        return measureNamePanel;
    }

    private VerticalPanel buildAbbreviationPanel() {
        VerticalPanel abbreviationPanel = new VerticalPanel();
        abbreviationPanel.getElement().addClassName("generalInformationPanel");
        FormLabel abbrInputLabel = buildAbbreviationLabel();
        abbreviationPanel.add(abbrInputLabel);
        abbrInput.setId("abbrInput");
        abbrInput.setText(generalInformationModel.geteCQMAbbreviatedTitle());
        abbrInput.setWidth(TEXT_BOX_WIDTH);
        abbrInput.setMaxLength(ECQM_ABBR_MAX_LENGTH);
        abbrInput.addBlurHandler(errorHandler.buildBlurHandler(abbrInput, s-> getFirst(CommonMeasureValidator.validateECQMAbbreviation(s))));

        abbreviationPanel.add(abbrInput);
        return abbreviationPanel;
    }

    protected VerticalPanel buildPatientBasedPanel() {
        VerticalPanel patientBasedPanel = new VerticalPanel();
        patientBasedPanel.getElement().addClassName("generalInformationPanel");
        FormLabel patientBasedLabel = buildPatientBasedLabel();
        buildPatientBasedInput();
        patientBasedPanel.add(patientBasedLabel);
        patientBasedPanel.add(patientBasedInput);
        return patientBasedPanel;
    }

    private FormLabel buildPatientBasedLabel() {
        FormLabel patientBasedLabel = new FormLabel();
        patientBasedLabel.setText("Patient-based Measure");
        patientBasedLabel.setTitle(patientBasedLabel.getText());
        patientBasedLabel.setId("patientBasedLabel");
        patientBasedLabel.setFor("patientBasedMeasure_listbox");
        return patientBasedLabel;
    }

    protected void buildPatientBasedInput() {
        patientBasedInput.getElement().setId("patientBasedMeasure_listbox");
        patientBasedInput.setTitle("Patient Based Indicator Required.");
        patientBasedInput.setStyleName("form-control");
        patientBasedInput.setVisibleItemCount(1);
        patientBasedInput.setWidth("18em");
        resetPatientBasedInput();
    }

    public void resetPatientBasedInput() {
        setPatientBasedInputOptions(MatContext.get().getPatientBasedIndicatorOptions(null));
        patientBasedInput.setSelectedIndex(1);
    }

    private VerticalPanel buildPopulationBasis() {
        VerticalPanel populationBasisPanel = new VerticalPanel();
        populationBasisPanel.getElement().addClassName("generalInformationPanel");

        FormLabel populationBasisLabel = new FormLabel();
        populationBasisLabel.setText("Population Basis (*)");
        populationBasisLabel.setTitle(populationBasisLabel.getText());
        populationBasisLabel.setId("populationBasisLabel");
        populationBasisLabel.setFor("populationBasisInput_ListBoxMVP");

        populationBasisInput.getElement().setId("measScoringInput_ListBoxMVP");
        populationBasisInput.setTitle("Population Basis Required.");
        populationBasisInput.setStyleName("form-control");
        populationBasisInput.setVisibleItemCount(1);
        populationBasisInput.setWidth("18em");
        populationBasisInput.addBlurHandler(errorHandler.buildRequiredBlurHandler(populationBasisInput));

        populationBasisPanel.add(populationBasisLabel);
        populationBasisPanel.add(populationBasisInput);
        return populationBasisPanel;
    }

    private VerticalPanel buildMeasureScoringPanel() {
        VerticalPanel measureSCoringPanel = new VerticalPanel();
        measureSCoringPanel.getElement().addClassName("generalInformationPanel");
        FormLabel measureScoringLabel = buildMeasureScoringLabel();
        buildMeasureScoringInput();

        measureSCoringPanel.add(measureScoringLabel);
        measureSCoringPanel.add(measureScoringInput);
        return measureSCoringPanel;
    }


    private FormLabel buildMeasureScoringLabel() {
        FormLabel measureScoringLabel = new FormLabel();
        measureScoringLabel.setText("Measure Scoring (*)");
        measureScoringLabel.setTitle(measureScoringLabel.getText());
        measureScoringLabel.setId("measureScoringLabel");
        measureScoringLabel.setFor("measScoringInput_ListBoxMVP");
        return measureScoringLabel;
    }

    private void buildMeasureScoringInput() {
        measureScoringInput.getElement().setId("measScoringInput_ListBoxMVP");
        measureScoringInput.setTitle("Measure Scoring Required.");
        measureScoringInput.setStyleName("form-control");
        measureScoringInput.setVisibleItemCount(1);
        measureScoringInput.setWidth("18em");
    }


    private VerticalPanel buildCompositeScoringPanel() {
        VerticalPanel compositeScoringPanel = new VerticalPanel();
        compositeScoringPanel.getElement().addClassName("generalInformationPanel");
        FormLabel compositeScoringLabel = new FormLabel();
        compositeScoringLabel.setText("Composite Scoring Method");
        compositeScoringLabel.setTitle(compositeScoringLabel.getText());
        compositeScoringLabel.setId("compositeScoringMethodLabel");
        compositeScoringLabel.setFor("compositeScoringMethodInput");
        compositeScoringPanel.add(compositeScoringLabel);
        compositeScoringPanel.add(compositeScoringMethodInput);
        compositeScoringMethodInput.setWidth("18em");
        return compositeScoringPanel;
    }

    private FormLabel buildAbbreviationLabel() {
        FormLabel abbrInputLabel = new FormLabel();
        abbrInputLabel.setText("eCQM Abbreviated Title (*)");
        abbrInputLabel.setTitle(abbrInputLabel.getText());
        abbrInputLabel.setId("eCQMAbbrTitleLabel");
        abbrInputLabel.setFor("abbrInput");
        return abbrInputLabel;
    }

    private FormLabel buildMeasureNameLabel() {
        FormLabel measureNameLabel = new FormLabel();
        measureNameLabel.setText("Measure Name  (*)");
        measureNameLabel.setTitle(measureNameLabel.getText());
        measureNameLabel.setId("measureNameLabel");
        measureNameLabel.setFor("measureNameInput");
        return measureNameLabel;
    }


    @Override
    public void setReadOnly(boolean readOnly) {
        abbrInput.setReadOnly(readOnly);
        abbrInput.setEnabled(!readOnly);
        measureNameInput.setReadOnly(readOnly);
        measureNameInput.setEnabled(!readOnly);
        compositeScoringMethodInput.setEnabled(!readOnly);
        measureScoringInput.setEnabled(!readOnly);
        populationBasisInput.setEnabled(!readOnly);
        patientBasedInput.setEnabled(!readOnly);
        endorsedByListBox.setEnabled(!readOnly);
        experimentalCheckbox.setEnabled(!readOnly);
        setNQFIdInputReadOnly(readOnly);
        setGenerateEMeasureButtonReadOnly(readOnly);
        setMeasurementPeriodReadOnly(readOnly);
    }

    private void setMeasurementPeriodReadOnly(boolean readOnly) {
        calendarYear.setEnabled(!readOnly);
        boolean isMeasurePeriodEnabled = (!readOnly && !generalInformationModel.isCalendarYear());
        measurePeriodFromInput.setEnabled(isMeasurePeriodEnabled);
        measurePeriodToInput.setEnabled(isMeasurePeriodEnabled);
    }

    private void setNQFIdInputReadOnly(boolean readOnly) {
        if (generalInformationModel.getEndorseByNQF() != null && generalInformationModel.getEndorseByNQF()) {
            nQFIDInput.setReadOnly(readOnly);
            nQFIDInput.setEnabled(!readOnly);
        }
    }

    private void setGenerateEMeasureButtonReadOnly(boolean readOnly) {
        if (generalInformationModel.geteMeasureId() != 0) {
            generateEMeasureIDButton.setEnabled(false);
        } else {
            generateEMeasureIDButton.setEnabled(!readOnly);
        }
    }

    public HelpBlock getHelpBlock() {
        return helpBlock;
    }

    public void setHelpBlock(HelpBlock helpBlock) {
        this.helpBlock = helpBlock;
    }

    public TextBox getECQMAbbrInput() {
        return abbrInput;
    }

    public void setECQMAbbrInput(TextBox abbrInput) {
        this.abbrInput = abbrInput;
    }

    public TextBox getMeasureNameInput() {
        return measureNameInput;
    }

    public void setMeasureNameInput(TextBox measureNameInput) {
        this.measureNameInput = measureNameInput;
    }

    public ListBoxMVP getMeasureScoringInput() {
        return measureScoringInput;
    }

    public void setMeasureScoringInput(ListBoxMVP measureScoringInput) {
        this.measureScoringInput = measureScoringInput;
    }

    public ListBoxMVP getPopulationBasisInput() {
        return populationBasisInput;
    }

    public void setPopulationBasisInput(ListBoxMVP populationBasisInput) {
        this.populationBasisInput = populationBasisInput;
    }

    public ListBoxMVP getCompositeScoringMethodInput() {
        return compositeScoringMethodInput;
    }

    public void setCompositeScoringMethodInput(ListBoxMVP compositeScoringMethodInput) {
        this.compositeScoringMethodInput = compositeScoringMethodInput;
    }

    public ListBoxMVP getPatientBasedInput() {
        return patientBasedInput;
    }

    public void setPatientBasedInput(ListBoxMVP patientBasedInput) {
        this.patientBasedInput = patientBasedInput;
    }

    public void setScoringChoices(List<? extends HasListBox> texts) {
        MatContext.get().setListBoxItems(measureScoringInput, texts, MatContext.PLEASE_SELECT);
    }

    public void setPopulationBasisList(List<String> populationBasisList) {
        MatContext.get().setPopulationBasisList(populationBasisInput, populationBasisList, MatContext.PLEASE_SELECT);
        logger.log(Level.INFO, "Successfully retrieved population Basis valid values");
    }

    public void setCompositeScoringChoices(List<? extends HasListBox> texts) {
        MatContext.get().setListBoxItems(compositeScoringMethodInput, texts, MatContext.PLEASE_SELECT);
    }

    public String getCompositeScoringValue() {
        return compositeScoringMethodInput.getItemText(compositeScoringMethodInput.getSelectedIndex());
    }

    public String getMeasureScoringValue() {
        return measureScoringInput.getItemText(measureScoringInput.getSelectedIndex());
    }

    public void setCompositeScoringSelectedValue(String compositeScoringMethod) {
        if (CompositeMethodScoringConstant.ALL_OR_NOTHING.equals(compositeScoringMethod)) {
            getCompositeScoringMethodInput().setSelectedIndex(1);
        } else if (CompositeMethodScoringConstant.OPPORTUNITY.equals(compositeScoringMethod)) {
            getCompositeScoringMethodInput().setSelectedIndex(2);
        } else if (CompositeMethodScoringConstant.PATIENT_LEVEL_LINEAR.equals(compositeScoringMethod)) {
            getCompositeScoringMethodInput().setSelectedIndex(3);
        } else {
            getCompositeScoringMethodInput().setSelectedIndex(0);
        }

        compositeScoringMethod = StringUtility.isEmptyOrNull(compositeScoringMethod) ? MatContext.PLEASE_SELECT : compositeScoringMethod;
        setScoringChoices(MatContext.get().getSelectionMap().get(compositeScoringMethod));
        measureScoringInput.setValueMetadata(generalInformationModel.getScoringMethod());
        setPatientBasedInputOptions(MatContext.get().getPatientBasedIndicatorOptions(generalInformationModel.getScoringMethod()));
        setPatientbasedIndicator();
    }

    @Override
    public ConfirmationDialogBox getSaveConfirmation() {
        if (hasUnsavedChanges()) {
            return observer.getSaveConfirmation(originalModel, generalInformationModel);
        }
        return null;
    }

    public GeneralInformationModel getGeneralInformationModel() {
        return generalInformationModel;
    }

    public void setGeneralInformationModel(GeneralInformationModel generalInformationModel) {
        this.generalInformationModel = generalInformationModel;
    }

    @Override
    public void resetForm() {
        helpBlock.setText(EMPTY_STRING);
        buildGeneralInformationModel(originalModel);
        buildDetailView();
    }

    @Override
    public MeasureDetailsComponentModel getMeasureDetailsComponentModel() {
        return generalInformationModel;
    }

    @Override
    public TextArea getTextEditor() {
        return null;
    }

    @Override
    public void clear() {
        helpBlock.setText(EMPTY_STRING);
        abbrInput.setText(EMPTY_STRING);
        eCQMVersionNumberTextBox.setText(EMPTY_STRING);
        finalizedDateTextBox.setText(EMPTY_STRING);
        guidTextBox.setText(EMPTY_STRING);
        measureNameInput.setText(EMPTY_STRING);
        measureScoringInput.clear();
        populationBasisInput.clear();
        compositeScoringMethodInput.clear();
        patientBasedInput.clear();
        experimentalCheckbox.setValue(false);
    }

    public void setPatientBasedInputOptions(List<String> patientBasedIndicatorOptions) {
        patientBasedInput.clear();
        for (String option : patientBasedIndicatorOptions) {
            patientBasedInput.addItem(option, option);
        }
    }

    public Button getGenerateEMeasureIDButton() {
        return generateEMeasureIDButton;
    }

    public void setGenerateEMeasureIDButton(Button generateEMeasureIDButton) {
        this.generateEMeasureIDButton = generateEMeasureIDButton;
    }

    public TextBox geteMeasureIdentifierInput() {
        return eMeasureIdentifierInput;
    }

    public void seteMeasureIdentifierInput(TextBox eMeasureIdentifierInput) {
        this.eMeasureIdentifierInput = eMeasureIdentifierInput;
    }

    @Override
    public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
    }

    @Override
    public void setObserver(MeasureDetailsComponentObserver observer) {
        this.observer = (GeneralInformationObserver) observer;
    }

    @Override
    public MeasureDetailsComponentObserver getObserver() {
        return observer;
    }

    public TextBox getnQFIDInput() {
        return nQFIDInput;
    }

    public void setnQFIDInput(TextBox nQFIDInput) {
        this.nQFIDInput = nQFIDInput;
    }

    private void resetEndorsedByListBox() {
        endorsedByListBox.clear();
        endorsedByListBox.insertItem("No", "false", "No");
        endorsedByListBox.insertItem("Yes", "true", "Yes");
        endorsedByListBox.setSelectedIndex(0);
        endorsedByListBox.setTitle("Endorsed By NQF List");
    }

    public ListBoxMVP getEndorsedByListBox() {
        return endorsedByListBox;
    }

    public void setEndorsedByListBox(ListBoxMVP endorsedByListBox) {
        this.endorsedByListBox = endorsedByListBox;
    }

    public CustomCheckBox getCalenderYear() {
        return calendarYear;
    }

    public void setCalenderYear(CustomCheckBox calenderYear) {
        this.calendarYear = calenderYear;
    }

    public DateBoxWithCalendar getMeasurePeriodFromInput() {
        return measurePeriodFromInput;
    }

    public void setMeasurePeriodFromInput(DateBoxWithCalendar measurePeriodFromInput) {
        this.measurePeriodFromInput = measurePeriodFromInput;
    }

    public DateBoxWithCalendar getMeasurePeriodToInput() {
        return measurePeriodToInput;
    }

    public void setMeasurePeriodToInput(DateBoxWithCalendar measurePeriodToInput) {
        this.measurePeriodToInput = measurePeriodToInput;
    }

    public void updateEmeasureId(int eMeasureId) {
        originalModel.seteMeasureId(eMeasureId);
        generalInformationModel.seteMeasureId(eMeasureId);
    }

    @Override
    public Widget getFirstElement() {
        return measureNameInput.asWidget();
    }

    public CheckBox getExperimentalCheckbox() {
        return experimentalCheckbox;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public List<String> preSave() {
        return getErrorHandler().validate();
    }
}
