package mat.client.measure;

import java.util.List;

import mat.client.shared.VerticalFlowPanel;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
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
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.util.FeatureFlagConstant;
import mat.model.clause.ModelTypeHelper;

public class AbstractNewMeasureView implements DetailDisplay {
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
    protected FormGroup cqlLibraryNameGroup = new FormGroup();
    protected FormGroup shortNameGroup = new FormGroup();
    protected FormGroup scoringGroup = new FormGroup();
    protected FormGroup patientBasedFormGrp = new FormGroup();
    protected HTML cautionMsgLibraryName = new HTML();
    protected HTML cautionMsgPlaceHolder = new HTML();
    protected HTML cautionPatientbasedMsgPlaceHolder = new HTML();
    EditConfirmationDialogBox confirmationDialogBox = new EditConfirmationDialogBox();

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
        messageFormGrp.setValidationState(ValidationState.NONE);
        getErrorMessageDisplay().clearAlert();
        warningMessageAlert.clearAlert();
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
        measureNameLabel.setTitle("Measure Name");
        measureNameLabel.setFor("MeasureNameTextArea");
        measureNameLabel.setShowRequiredIndicator(true);
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
        modelLabel.setTitle("Model");
        modelLabel.setFor("measureModel");
        modelLabel.setShowRequiredIndicator(true);
        modelLabel.setId("measureModel_Id");
        return modelLabel;
    }

    /**
     * Builds a vertical panel with model types wrapped in
     *
     * @return measureModelPanel
     */
    protected VerticalFlowPanel buildModelTypePanel() {
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

    protected void buildMeasureNameTextArea() {
        measureNameTextBox.setId("MeasureNameTextArea");
        measureNameTextBox.setTitle("Enter Measure Name Required.");
        measureNameTextBox.setWidth("400px");
        measureNameTextBox.setHeight("50px");
        measureNameTextBox.setMaxLength(500);
    }

    protected FormLabel buildCQLLibraryNameLabel() {
        FormLabel cqlLibraryNameLabel = new FormLabel();
        cqlLibraryNameLabel.setText("CQL Library Name");
        cqlLibraryNameLabel.setTitle("CQL Library Name");
        cqlLibraryNameLabel.setFor("CqlLibraryNameTextArea");
        cqlLibraryNameLabel.setShowRequiredIndicator(true);
        cqlLibraryNameLabel.setId("CqlLibraryNameTextArea_Id");
        return cqlLibraryNameLabel;
    }

    private void buildCQLLibraryTextArea() {
        cqlLibraryNameTextBox.setId("CqlLibraryNameTextArea");
        cqlLibraryNameTextBox.setTitle("Enter CQL Library Name Required.");
        cqlLibraryNameTextBox.setWidth("400px");
        cqlLibraryNameTextBox.setHeight("50px");
        cqlLibraryNameTextBox.setMaxLength(500);
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
        shortNameLabel.setTitle("eCQM Abbreviated Title");
        shortNameLabel.setFor("ShortNameTextBox");
        shortNameLabel.setShowRequiredIndicator(true);
        shortNameLabel.setId("ShortNameTextBox_Id");
        return shortNameLabel;
    }

    protected void buildShortNameTextBox() {
        eCQMAbbreviatedTitleTextBox.setId("ShortNameTextBox");
        eCQMAbbreviatedTitleTextBox.setTitle("Enter eCQM Abbreviated Title Required");
        eCQMAbbreviatedTitleTextBox.setWidth("18em");
        eCQMAbbreviatedTitleTextBox.setMaxLength(32);
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
        measureScoringListBox.getElement().setId("measScoringInput_ListBoxMVP");
        measureScoringListBox.setTitle("Measure Scoring Required.");
        measureScoringListBox.setStyleName("form-control");
        measureScoringListBox.setVisibleItemCount(1);
        measureScoringListBox.setWidth("18em");
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
        patientBasedLabel.setTitle("Patient-based Measure");
        patientBasedLabel.setFor("PatientBasedMeasureListBox");
        patientBasedLabel.setShowRequiredIndicator(true);
        patientBasedLabel.setId("PatientBasedMeasureListBox_Id");
        return patientBasedLabel;
    }

    protected void buildPatientBasedInput() {
        patientBasedListBox.getElement().setId("patientBasedMeasure_listbox");
        patientBasedListBox.setTitle("Patient Based Indicator Required.");
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
        formFieldSet.add(cqlLibraryNameGroup);
        formFieldSet.add(shortNameGroup);
        formFieldSet.add(scoringGroup);
        formFieldSet.add(patientBasedFormGrp);
        formFieldSet.add(buttonFormGroup);
        return formFieldSet;
    }
}
