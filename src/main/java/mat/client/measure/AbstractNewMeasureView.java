package mat.client.measure;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.*;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.codelist.HasListBox;
import mat.client.cqlworkspace.EditConfirmationDialogBox;
import mat.client.shared.*;
import mat.client.util.FeatureFlagConstant;
import mat.client.validator.ErrorHandler;
import mat.model.clause.ModelTypeHelper;
import mat.shared.validator.measure.CommonMeasureValidator;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.List;

public class AbstractNewMeasureView implements DetailDisplay {
    private static final String LIBRARY_NAME_REQUIRED = "A CQL Library name is required.";
    public static final String LIBRARY_LENGTH_ERROR = "CQL Library Name cannot exceed 500 characters.";

    protected SimplePanel mainPanel = new SimplePanel();
    protected MeasureNameLabel measureNameLabel = new MeasureNameLabel();
    protected TextArea measureNameTextBox = new TextArea();
    protected TextArea cqlLibraryNameTextBox = new TextArea();
    protected RadioButton fhirModel = new RadioButton("measureModel", "FHIR");
    protected RadioButton qdmModel = new RadioButton("measureModel", "QDM");
    protected TextBox eCQMAbbreviatedTitleTextBox = new TextBox();
    protected MessageAlert errorMessages = new ErrorMessageAlert();
    protected WarningConfirmationMessageAlert warningMessageAlert = new WarningConfirmationMessageAlert();
    protected ListBoxMVP measureScoringListBox = new ListBoxMVP();
    protected ListBoxMVP patientBasedListBox = new ListBoxMVP();
    protected SaveContinueCancelButtonBar buttonBar = new SaveContinueCancelButtonBar("measureDetail");
    protected HelpBlock helpBlock = new HelpBlock();
    protected FormGroup messageFormGrp = new FormGroup();
    protected FormGroup measureNameGroup = new FormGroup();
    protected FormGroup measureModelGroup = new FormGroup();
    protected FormGroup nameAndIdOptionGroup = new FormGroup();
    protected FormGroup cqlLibraryNameGroup = new FormGroup();
    protected FormGroup shortNameGroup = new FormGroup();
    protected FormGroup scoringGroup = new FormGroup();
    protected FormGroup patientBasedFormGrp = new FormGroup();
    protected HTML cautionMsgLibraryName = new HTML();
    protected HTML cautionMsgPlaceHolder = new HTML();
    protected HTML cautionPatientbasedMsgPlaceHolder = new HTML();
    EditConfirmationDialogBox confirmationDialogBox = new EditConfirmationDialogBox();
    private ErrorHandler errorHandler = new ErrorHandler();

    protected CheckBox generateCmsIdCheckbox = new CheckBox();
    protected CheckBox matchLibraryNameToCmsIdCheckbox = new CheckBox();

    public static final String CAUTION_LIBRARY_NAME_MSG_STR = "<div style=\"padding-left:5px;\">WARNING: Long CQL Library names may cause problems upon export with zip files and file storage. "
            + "Please keep CQL Library names concise.<br/>";

    protected String cautionPatientbasedMsgStr = "<div style=\"padding-left:5px;\">WARNING: Changing the Measure Scoring type will "
            + "reset the Patient-based Measure to its default setting.<br/>";

    protected String cautionMsgStr = "<div style=\"padding-left:5px;\">WARNING: Changing the 'Measure Scoring' will have the following impacts:<br/>" +
            "<img src='images/bullet.png'/> Populations in the Population Workspace that do not apply to the 'Measure Scoring' selected will be deleted.<br/>" +
            "<img src='images/bullet.png'/> Existing Groupings in the Measure Packager will be deleted.</div>";
    protected HTML requiredInstructions = new HTML("All fields are required.");
    protected Label patientBasedMeasure = new Label();

    @Override
    public Widget asWidget() {
        return mainPanel;
    }

    @Override
    public WarningConfirmationMessageAlert getWarningConfirmationMessageAlert() {
        return warningMessageAlert;
    }

    @Override
    public MessageAlert getErrorMessageDisplay() {
        return errorMessages;
    }

    @Override
    public void clearFields() {
        measureNameTextBox.setText("");
        eCQMAbbreviatedTitleTextBox.setText("");
        cqlLibraryNameTextBox.setText("");
        measureScoringListBox.setSelectedIndex(0);//default to --Select-- value.
        helpBlock.setText("");
        generateCmsIdCheckbox.setValue(false);
        messageFormGrp.setValidationState(ValidationState.NONE);
        getErrorMessageDisplay().clearAlert();
        warningMessageAlert.clearAlert();
        errorHandler.clearErrors();
    }

    @Override
    public HasClickHandlers getCancelButton() {
        return buttonBar.getCancelButton();
    }

    @Override
    public ListBoxMVP getMeasureScoringListBox() {
        return measureScoringListBox;
    }

    @Override
    public String getMeasureScoringValue() {
        return measureScoringListBox.getItemText(measureScoringListBox.getSelectedIndex());
    }

    @Override
    public String getMeasureModelType() {
        return fhirModel.getValue() ? ModelTypeHelper.FHIR : ModelTypeHelper.QDM;
    }

    @Override
    public void setMeasureModelType(String type) {
        if (ModelTypeHelper.FHIR.equalsIgnoreCase(type)) {
            // set FHIR model
            fhirModel.setEnabled(true);
            fhirModel.setValue(true);
            //set QDM model off
            qdmModel.setValue(false);
            qdmModel.setEnabled(false);
        } else {
            // set QDM model
            qdmModel.setEnabled(true);
            qdmModel.setValue(true);
            // set FHIR model off
            fhirModel.setValue(false);
            fhirModel.setEnabled(false);
        }
    }

    public void allowAllMeasureModelTypes() {
        fhirModel.setEnabled(true);
        qdmModel.setEnabled(true);
        fhirModel.setValue(true);
    }

    @Override
    public HasValue<String> getMeasureVersion() {
        return null;
    }

    @Override
    public HasValue<String> getMeasureNameTextBox() {
        return measureNameTextBox;
    }

    @Override
    public HasValue<String> getCQLLibraryNameTextBox() {
        return cqlLibraryNameTextBox;
    }

    @Override
    public HasClickHandlers getSaveButton() {
        return buttonBar.getSaveButton();
    }

    @Override
    public HasValue<String> getECQMAbbreviatedTitleTextBox() {
        return eCQMAbbreviatedTitleTextBox;
    }

    @Override
    public void setMeasureName(String name) {
        measureNameLabel.setMeasureName(name);
    }

    @Override
    public void setScoringChoices(List<? extends HasListBox> texts) {
        MatContext.get().setListBoxItems(measureScoringListBox, texts, MatContext.PLEASE_SELECT);
    }

    @Override
    public void showCautionMsg(boolean show) {
        if (show) {
            cautionMsgPlaceHolder.setHTML(cautionMsgStr);
            cautionPatientbasedMsgPlaceHolder.setHTML(cautionPatientbasedMsgStr);
        } else {
            cautionMsgPlaceHolder.setHTML("");
            cautionPatientbasedMsgPlaceHolder.setHTML("");
        }
    }

    @Override
    public void showMeasureName(boolean show) {
        MatContext.get().setVisible(measureNameLabel, show);
    }


    @Override
    public ListBoxMVP getPatientBasedListBox() {
        return patientBasedListBox;
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
    public EditConfirmationDialogBox getConfirmationDialogBox() {
        return confirmationDialogBox;
    }

    @Override
    public CheckBox getGenerateCmsIdCheckbox() {
        return generateCmsIdCheckbox;
    }

    @Override
    public CheckBox getMatchLibraryNameToCmsIdCheckbox() {
        return matchLibraryNameToCmsIdCheckbox;
    }

    @Override
    public RadioButton getFhirModel() {
        return fhirModel;
    }

    @Override
    public RadioButton getQdmModel() {
        return qdmModel;
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

        fPanel.add(new SpacerWidget());
        fPanel.add(requiredInstructions);
        requiredInstructions.getElement().setId("requiredInstructions_HTML");
        fPanel.add(new SpacerWidget());
        fPanel.add(errorMessages);
        fPanel.add(warningMessageAlert);
        errorMessages.getElement().setId("errorMessages_ErrorMessageDisplay");

        return fPanel;
    }

    protected FormLabel buildMeasureNameLabel() {
        FormLabel measureNameLabel = new FormLabel();
        measureNameLabel.setText("Measure Name");
        measureNameLabel.setShowRequiredIndicator(true);
        measureNameLabel.setTitle("Measure Name Required");
        measureNameLabel.setFor("MeasureNameTextArea");
        measureNameLabel.setId("MeasureNameTextArea_Id");
        return measureNameLabel;
    }

    /**
     * Create a label for measure model radio buttons
     *
     * @return modelLabel -> Model label Widget
     */
    protected FormLabel buildModelTypeLabel() {
        FormLabel modelLabel = new FormLabel();
        modelLabel.setText("Model");
        modelLabel.setShowRequiredIndicator(true);
        modelLabel.setTitle("Model Required");
        modelLabel.setFor("measureModel");
        modelLabel.setId("measureModel_Id");
        return modelLabel;
    }

    /**
     * Builds a vertical panel with model types wrapped in
     *
     * @return measureModelPanel
     */
    protected VerticalFlowPanel buildModelTypePanel() {
        ((Element) fhirModel.getElement().getChild(0)).setAttribute("aria-label","Model FHIR");
        ((Element) qdmModel.getElement().getChild(0)).setAttribute("aria-label","Model QDM");

        VerticalFlowPanel measureModelPanel = new VerticalFlowPanel();
        measureModelGroup.add(buildModelTypeLabel());
        //new model creation defaulted to FHIR
        if (MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR)) {
            fhirModel.setValue(true);
        } else {
            fhirModel.setEnabled(false);
            qdmModel.setValue(true);
        }
        measureModelPanel.add(fhirModel);
        measureModelPanel.add(qdmModel);
        return measureModelPanel;
    }

    protected VerticalFlowPanel buildCompositeModelTypePanel() {
        VerticalFlowPanel measureModelPanel = new VerticalFlowPanel();
        measureModelGroup.add(buildModelTypeLabel());
        fhirModel.setEnabled(false);
        qdmModel.setValue(true);
        measureModelPanel.add(fhirModel);
        measureModelPanel.add(qdmModel);
        return measureModelPanel;
    }

    /**
     * Add measure model type radios to create measure view iff 'MAT_ON_FHIR' flag is on
     */
    protected void addMeasureModelType() {
        if (MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR)) {
            measureModelGroup.add(buildModelTypePanel());
        }
    }

    protected void addCompositeMeasureModelType() {
        measureModelGroup.add(buildCompositeModelTypePanel());
    }

    protected void addGenerateCmsIdCheckbox() {
        generateCmsIdCheckbox.setText("Automatically generate a CMS ID on Save.");
        generateCmsIdCheckbox.setTitle("Click to generate a CMS ID on Save.");
        generateCmsIdCheckbox.setId("generateCmsId_CheckBox");
        ((Element) generateCmsIdCheckbox.getElement().getChild(0)).setAttribute("for", "generateCmsId_Input");
        ((Element) generateCmsIdCheckbox.getElement().getChild(0).getChild(0)).setAttribute("id", "generateCmsId_Input");
        nameAndIdOptionGroup.add(generateCmsIdCheckbox);
    }

    protected void addCompositeGenerateCmsIdCheckbox() {
        generateCmsIdCheckbox.setText("Automatically generate an eCQM ID on Save.");
        generateCmsIdCheckbox.setTitle("Click to generate an eCQM ID on Save.");
        generateCmsIdCheckbox.setId("generateCmsId_CheckBox");
        ((Element) generateCmsIdCheckbox.getElement().getChild(0)).setAttribute("for", "generateCmsId_Input");
        ((Element) generateCmsIdCheckbox.getElement().getChild(0).getChild(0)).setAttribute("id", "generateCmsId_Input");
        nameAndIdOptionGroup.add(generateCmsIdCheckbox);
    }

    protected void addMatchLibraryNameToCmsIdCheckbox(){
        matchLibraryNameToCmsIdCheckbox.setText("Match CQL Library Name to Generated ID.");
        matchLibraryNameToCmsIdCheckbox.setTitle("Click to match CQL Library Name to Generated ID.");
        matchLibraryNameToCmsIdCheckbox.setId("matchLibName_CheckBox");
        matchLibraryNameToCmsIdCheckbox.setEnabled(false);
        ((Element) matchLibraryNameToCmsIdCheckbox.getElement().getChild(0)).setAttribute("for", "matchLibName_Input");
        ((Element) matchLibraryNameToCmsIdCheckbox.getElement().getChild(0).getChild(0)).setAttribute("id", "matchLibName_Input");
        nameAndIdOptionGroup.add(matchLibraryNameToCmsIdCheckbox);
    }

    protected void buildMeasureNameTextArea() {
        measureNameTextBox.setId("MeasureNameTextArea");
        measureNameTextBox.setTitle("Enter Measure Name Required.");
        measureNameTextBox.setWidth("400px");
        measureNameTextBox.setHeight("50px");
        measureNameTextBox.setMaxLength(500);
        measureNameTextBox.addBlurHandler(errorHandler.buildBlurHandler(measureNameTextBox,
                (s) -> {
                    String result = null;
                    if (fhirModel.getValue()) {
                        result = getFirst(CommonMeasureValidator.validateFhirMeasureName(s));
                    }
                    if (result == null) {
                        result = getFirst(CommonMeasureValidator.validateMeasureName(s));
                    }
                    return result;
                }));
    }

    protected String getFirst(List<String> list) {
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    protected FormLabel buildCQLLibraryNameLabel() {
        FormLabel cqlLibraryNameLabel = new FormLabel();
        cqlLibraryNameLabel.setText("CQL Library Name");
        cqlLibraryNameLabel.setShowRequiredIndicator(true);
        cqlLibraryNameLabel.setTitle("CQL Library Name Required");
        cqlLibraryNameLabel.setFor("CqlLibraryNameTextArea");
        cqlLibraryNameLabel.setId("CqlLibraryNameTextArea_Id");
        return cqlLibraryNameLabel;
    }

    private void buildCQLLibraryTextArea() {
        cqlLibraryNameTextBox.setId("CqlLibraryNameTextArea");
        cqlLibraryNameTextBox.setTitle("Enter CQL Library Name Required.");
        cqlLibraryNameTextBox.setWidth("400px");
        cqlLibraryNameTextBox.setHeight("50px");
        cqlLibraryNameTextBox.setMaxLength(500);
        cqlLibraryNameTextBox.addBlurHandler(errorHandler.buildBlurHandler(cqlLibraryNameTextBox,
                (s) -> {
                    String result = null;
                    if (fhirModel.getValue()) {
                        result = getFirst(CommonMeasureValidator.validateFhirLibraryName(s));
                    } else {
                        result = getFirst(CommonMeasureValidator.validateQDMName(s));
                    }
                    return result;
                }));
    }

    protected HorizontalPanel buildCQLLibraryNamePanel() {
        HorizontalPanel cqlLibraryNamePanel = new HorizontalPanel();
        cqlLibraryNameGroup.add(buildCQLLibraryNameLabel());

        buildCQLLibraryTextArea();
        cqlLibraryNamePanel.add(cqlLibraryNameTextBox);
        cqlLibraryNamePanel.add(new HTML("&nbsp;"));
        cautionMsgLibraryName.setHTML(CAUTION_LIBRARY_NAME_MSG_STR);
        cqlLibraryNamePanel.add(cautionMsgLibraryName);
        return cqlLibraryNamePanel;
    }

    protected FormLabel buildShortNameLabel() {
        FormLabel shortNameLabel = new FormLabel();
        shortNameLabel.setText("eCQM Abbreviated Title");
        shortNameLabel.setShowRequiredIndicator(true);
        shortNameLabel.setTitle("eCQM Abbreviated Title");
        shortNameLabel.setFor("ShortNameTextBox");
        shortNameLabel.setId("ShortNameTextBox_Id");
        return shortNameLabel;
    }

    protected void buildShortNameTextBox() {
        eCQMAbbreviatedTitleTextBox.setId("ShortNameTextBox");
        eCQMAbbreviatedTitleTextBox.setTitle("Enter eCQM Abbreviated Title Required");
        eCQMAbbreviatedTitleTextBox.setWidth("18em");
        eCQMAbbreviatedTitleTextBox.setMaxLength(32);
        eCQMAbbreviatedTitleTextBox.addBlurHandler(errorHandler.buildBlurHandler(eCQMAbbreviatedTitleTextBox,
                s -> getFirst(CommonMeasureValidator.validateECQMAbbreviation(s))));
    }

    protected FormLabel buildScoringLabel() {
        FormLabel scoringLabel = new FormLabel();
        scoringLabel.setText("Measure Scoring");
        scoringLabel.setShowRequiredIndicator(true);
        scoringLabel.setTitle("Measure Scoring Required");
        scoringLabel.setFor("MeasureScoringListBox");
        scoringLabel.setId("MeasureScoringListBox");
        return scoringLabel;
    }

    protected void buildMeasureScoringInput() {
        measureScoringListBox.getElement().setId("measScoringInput_ListBoxMVP");
        measureScoringListBox.setTitle("Measure Scoring Required.");
        measureScoringListBox.setStyleName("form-control");
        measureScoringListBox.setVisibleItemCount(1);
        measureScoringListBox.setWidth("18em");
        measureScoringListBox.addBlurHandler(errorHandler.buildRequiredBlurHandler(measureScoringListBox));
    }

    protected HorizontalPanel buildScoringPanel() {
        HorizontalPanel scoringPanel = new HorizontalPanel();
        scoringPanel.add(measureScoringListBox);
        scoringPanel.add(new HTML("&nbsp;"));
        scoringPanel.add(cautionMsgPlaceHolder);
        return scoringPanel;
    }

    protected FormLabel buildPatientBasedLabel() {
        FormLabel patientBasedLabel = new FormLabel();
        patientBasedLabel.setText("Patient-based Measure");
        patientBasedLabel.setShowRequiredIndicator(true);
        patientBasedLabel.setTitle("Patient-based Measure Required");
        patientBasedLabel.setFor("PatientBasedMeasureListBox");
        patientBasedLabel.setId("PatientBasedMeasureListBox_Id");
        return patientBasedLabel;
    }

    protected void buildPatientBasedInput() {
        patientBasedListBox.getElement().setId("patientBasedMeasure_listbox");
        patientBasedListBox.setTitle("Patient Based Measure required");
        patientBasedListBox.setStyleName("form-control");
        patientBasedListBox.setVisibleItemCount(1);
        patientBasedListBox.setWidth("18em");
    }

    protected HorizontalPanel buildPatientBasedPanel() {
        HorizontalPanel patientBasedPanel = new HorizontalPanel();
        patientBasedPanel.add(patientBasedListBox);
        patientBasedPanel.add(new HTML("&nbsp;"));
        patientBasedPanel.add(cautionPatientbasedMsgPlaceHolder);
        return patientBasedPanel;
    }

    protected FieldSet buildFormFieldSet() {
        FormGroup buttonFormGroup = new FormGroup();
        buttonFormGroup.add(buttonBar);
        FieldSet formFieldSet = new FieldSet();
        formFieldSet.add(measureNameGroup);
        formFieldSet.add(measureModelGroup);
        formFieldSet.add(nameAndIdOptionGroup);
        formFieldSet.add(cqlLibraryNameGroup);
        formFieldSet.add(shortNameGroup);
        formFieldSet.add(scoringGroup);
        formFieldSet.add(patientBasedFormGrp);
        formFieldSet.add(buttonFormGroup);
        return formFieldSet;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }
}
