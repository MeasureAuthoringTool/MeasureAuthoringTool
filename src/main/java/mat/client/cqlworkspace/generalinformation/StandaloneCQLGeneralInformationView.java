package mat.client.cqlworkspace.generalinformation;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import mat.client.buttons.SaveToolBarButton;
import mat.client.cqlworkspace.AbstractCQLWorkspacePresenter;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MessagePanel;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;
import mat.model.MeasureSteward;
import mat.shared.CQLModelValidator;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.List;
import java.util.logging.Logger;

public class StandaloneCQLGeneralInformationView implements CQLGeneralInformationView {
    private static final Logger logger = Logger.getLogger("StandaloneCQLGeneralInformationView");

    protected static final String STYLE = "style";
    protected static final String PIXEL_150 = "150px";
    protected static final String FONT_SIZE_90_MARGIN_LEFT_15PX = "font-size:90%;margin-left:15px;";
    protected static final String MARGIN_STYLE = "margin-left:15px;margin-bottom:-15px;width:250px;height:32px;";

    protected static final String COMMENTS_MAX_LENGTH = "2500";
    protected static final int CQL_LIBRARY_NAME_MAX_LENGTH = 500;
    protected HorizontalPanel generalInfoMainHPanel = new HorizontalPanel();
    protected MatTextBox libraryNameTextBox = new MatTextBox();
    protected MatTextBox libraryVersionTextBox = new MatTextBox();
    protected MatTextBox usingModelTextBox = new MatTextBox();
    protected MatTextBox modelVersionTextBox = new MatTextBox();
    protected SaveToolBarButton saveButton = new SaveToolBarButton("GeneralInfo");
    protected FormGroup libraryNameGroup = new FormGroup();
    protected FormGroup usingModelGroup = new FormGroup();
    protected FormGroup libraryVersionGroup = new FormGroup();
    protected FormGroup modelVersionGroup = new FormGroup();
    protected FormGroup descriptionGroup = new FormGroup();
    protected FormGroup stewardGroup = new FormGroup();
    protected FormGroup experimentalGroup = new FormGroup();
    protected FormGroup commentsGroup = new FormGroup();
    protected TextArea descriptionTextArea = new TextArea();
    protected ListBoxMVP stewardListBox = new ListBoxMVP();
    protected CheckBox experimentalCheckbox = new CheckBox();
    protected HTML heading = new HTML();

    protected TextArea comments = new TextArea();
    protected InAppHelp inAppHelp = new InAppHelp("");

    private String stewardId;
    private String stewardValue;


    public StandaloneCQLGeneralInformationView() {
    }

    private void cleanUp() {
        generalInfoMainHPanel.clear();
        libraryNameGroup.clear();
        usingModelGroup.clear();
        libraryVersionGroup.clear();
        modelVersionGroup.clear();
        descriptionGroup.clear();
        stewardGroup.clear();
        experimentalGroup.clear();
        commentsGroup.clear();
    }

    private void build() {
        if (MatContext.get().isCurrentCQLLibraryModelTypeFhir()) {
            buildForFhir();
        } else {
            buildForQDM();
            ;
        }
    }

    private void buildForQDM() {
        cleanUp();
        heading.addStyleName("leftAligned");
        VerticalPanel generalInfoTopPanel = new VerticalPanel();

        FormLabel libraryNameLabel = new FormLabel();
        libraryNameLabel.setText("CQL Library Name");
        libraryNameLabel.setTitle("CQL Library Name");
        libraryNameLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
        libraryNameLabel.setWidth(PIXEL_150);
        libraryNameLabel.setId("libraryNameLabel_Label");
        libraryNameLabel.setFor("libraryNameValue_TextBox");

        libraryNameTextBox.getElement().setAttribute(STYLE, MARGIN_STYLE);
        libraryNameTextBox.getElement().setId("libraryNameValue_TextBox");
        libraryNameTextBox.setTitle("Required");
        libraryNameTextBox.setMaxLength(CQL_LIBRARY_NAME_MAX_LENGTH);

        libraryNameGroup.add(libraryNameLabel);
        libraryNameGroup.add(libraryNameTextBox);

        FormLabel libraryVersionLabel = new FormLabel();
        libraryVersionLabel.setText("CQL Library Version");
        libraryVersionLabel.setTitle("CQL Library Version");
        libraryVersionLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
        libraryVersionLabel.setWidth(PIXEL_150);
        libraryVersionLabel.setId("libraryVersionLabel_Label");
        libraryVersionLabel.setFor("libraryVersionValue_TextBox");

        libraryVersionTextBox.getElement().setAttribute(STYLE, MARGIN_STYLE);
        libraryVersionTextBox.getElement().setId("libraryVersionValue_TextBox");
        libraryVersionTextBox.setReadOnly(true);

        libraryVersionGroup.add(libraryVersionLabel);
        libraryVersionGroup.add(libraryVersionTextBox);

        FormLabel usingModeLabel = new FormLabel();
        usingModeLabel.setText("Using Model");
        usingModeLabel.setTitle("Using Model");
        usingModeLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
        usingModeLabel.setId("usingModeLabel_Label");
        usingModeLabel.setWidth(PIXEL_150);
        usingModeLabel.setFor("usingModelValue_TextBox");

        usingModelTextBox.getElement().setAttribute(STYLE, MARGIN_STYLE);
        usingModelTextBox.getElement().setId("usingModelValue_TextBox");
        usingModelTextBox.setReadOnly(true);

        usingModelGroup.add(usingModeLabel);
        usingModelGroup.add(usingModelTextBox);

        FormLabel modelVersionLabel = new FormLabel();
        modelVersionLabel.setText("Model Version");
        modelVersionLabel.setTitle("Model Version");
        modelVersionLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
        modelVersionLabel.getElement().setId("modelVersionLabel_Label");
        modelVersionLabel.setWidth(PIXEL_150);
        modelVersionLabel.setFor("modelVersionValue_TextBox");

        modelVersionTextBox.getElement().setAttribute(STYLE, "margin-left:15px;width:250px;height:32px;");
        modelVersionTextBox.getElement().setId("modelVersionValue_TextBox");
        modelVersionTextBox.setReadOnly(true);

        modelVersionGroup.add(modelVersionLabel);
        modelVersionGroup.add(modelVersionTextBox);

        heading.getElement().setTabIndex(0);

        generalInfoTopPanel.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));

        generalInfoTopPanel.add(new SpacerWidget());
        generalInfoTopPanel.add(new SpacerWidget());

        generalInfoTopPanel.add(libraryNameGroup);
        generalInfoTopPanel.add(new SpacerWidget());

        generalInfoTopPanel.add(libraryVersionGroup);
        generalInfoTopPanel.add(new SpacerWidget());

        generalInfoTopPanel.add(usingModelGroup);
        generalInfoTopPanel.add(new SpacerWidget());

        generalInfoTopPanel.add(modelVersionGroup);
        generalInfoTopPanel.add(new SpacerWidget());

        generalInfoTopPanel.setStyleName("marginLeft15px");
        generalInfoMainHPanel.add(generalInfoTopPanel);

        buildButtonLayoutPanelForQDM();
    }

    private void buildForFhir() {
        cleanUp();
        heading.addStyleName("leftAligned");
        VerticalPanel generalInfoTopPanel = new VerticalPanel();

        FormLabel libraryNameLabel = new FormLabel();
        libraryNameLabel.setText("CQL Library Name");
        libraryNameLabel.setTitle("CQL Library Name");
        libraryNameLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
        libraryNameLabel.setWidth(PIXEL_150);
        libraryNameLabel.setId("libraryNameLabel_Label");
        libraryNameLabel.setFor("libraryNameValue_TextBox");

        libraryNameTextBox.getElement().setAttribute(STYLE, MARGIN_STYLE);
        libraryNameTextBox.getElement().setId("libraryNameValue_TextBox");
        libraryNameTextBox.setTitle("Required");
        libraryNameTextBox.setMaxLength(CQL_LIBRARY_NAME_MAX_LENGTH);

        libraryNameGroup.add(libraryNameLabel);
        libraryNameGroup.add(libraryNameTextBox);

        FormLabel libraryVersionLabel = new FormLabel();
        libraryVersionLabel.setText("CQL Library Version");
        libraryVersionLabel.setTitle("CQL Library Version");
        libraryVersionLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
        libraryVersionLabel.setWidth("150px");
        libraryVersionLabel.setId("libraryVersionLabel_Label");
        libraryVersionLabel.setFor("libraryVersionValue_TextBox");

        libraryVersionTextBox.getElement().setAttribute(STYLE, MARGIN_STYLE);
        libraryVersionTextBox.getElement().setId("libraryVersionValue_TextBox");
        libraryVersionTextBox.setWidth("150px");
        libraryVersionTextBox.setReadOnly(true);

        libraryVersionGroup.add(libraryVersionLabel);
        libraryVersionGroup.add(libraryVersionTextBox);

        FormLabel usingModeLabel = new FormLabel();
        usingModeLabel.setText("Using Model");
        usingModeLabel.setTitle("Using Model");
        usingModeLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
        usingModeLabel.setId("usingModeLabel_Label");
        usingModeLabel.getElement().setAttribute("width","150px");
        usingModeLabel.setFor("usingModelValue_TextBox");

        usingModelTextBox.getElement().setAttribute(STYLE, "margin-left:15px;margin-bottom:-15px;height:32px;");
        usingModelTextBox.getElement().setId("usingModelValue_TextBox");
        usingModelTextBox.getElement().setAttribute("width","150px");
        usingModelTextBox.setReadOnly(true);

        usingModelGroup.add(usingModeLabel);
        usingModelGroup.add(usingModelTextBox);

        FormLabel modelVersionLabel = new FormLabel();
        modelVersionLabel.setText("Model Version");
        modelVersionLabel.setTitle("Model Version");
        modelVersionLabel.getElement().setAttribute(STYLE, "font-size:90%;margin-left:30px;");
        modelVersionLabel.getElement().setId("modelVersionLabel_Label");
        modelVersionLabel.getElement().setAttribute("width","150px");
        modelVersionLabel.setFor("modelVersionValue_TextBox");

        modelVersionTextBox.getElement().setAttribute(STYLE, "margin-left:30px;margin-bottom:-15px;height:32px;");
        modelVersionTextBox.getElement().setId("modelVersionValue_TextBox");
        modelVersionTextBox.getElement().setAttribute("width","150px");
        modelVersionTextBox.setReadOnly(true);

        modelVersionGroup.add(modelVersionLabel);
        modelVersionGroup.add(modelVersionTextBox);

        modelVersionGroup.add(modelVersionLabel);
        modelVersionGroup.add(modelVersionTextBox);

        FormLabel descriptionLabel = new FormLabel();
        descriptionLabel.setText("CQL Library Description");
        descriptionLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
        descriptionLabel.getElement().setId("descriptionLabel");
        descriptionLabel.setWidth("250px");
        descriptionLabel.getElement().setAttribute("for", "descriptionTextArea");
        descriptionTextArea.getElement().setAttribute(STYLE, "margin-left:15px;width:300px;");
        descriptionTextArea.getElement().setAttribute("id", "descriptionTextArea");
        descriptionTextArea.setReadOnly(false);
        descriptionTextArea.getElement().setAttribute("maxlength", COMMENTS_MAX_LENGTH);
        descriptionTextArea.setHeight("220px");
        descriptionGroup.add(descriptionLabel);
        descriptionGroup.add(descriptionTextArea);

        FormLabel commentsLabel = new FormLabel();
        commentsLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
        commentsLabel.setId("commentsLabel");
        commentsLabel.setFor("commentsContent");
        commentsLabel.setText("Comments");
        comments.getElement().setAttribute(STYLE, "margin-left:15px;width:250px;");
        comments.getElement().setAttribute("maxlength", COMMENTS_MAX_LENGTH);
        comments.getElement().setAttribute("id", "commentsContent");
        comments.setHeight("220px");
        comments.setWidth("300px");
        commentsGroup.add(commentsLabel);
        commentsGroup.add(comments);

        FormLabel stewardLabel = new FormLabel();
        stewardLabel.setText("Publisher");
        stewardLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
        stewardLabel.setWidth("250px");
        stewardLabel.setId("publisherLabel");
        stewardLabel.getElement().setAttribute("for", "publisherListBoxMVP");
        stewardListBox.setTitle("Measure Publisher List");
        stewardListBox.getElement().setAttribute(STYLE, "margin-left:15px;width:250px;height:32px;");
        stewardListBox.getElement().setId("publisherListBoxMVP");
        stewardListBox.setEnabled(true);
        stewardGroup.add(stewardLabel);
        stewardGroup.add(stewardListBox);

        FormLabel experimentalLabel = new FormLabel();
        experimentalLabel.setText("Experimental");
        experimentalLabel.getElement().setAttribute(STYLE, "font-size:90%;");
        experimentalLabel.getElement().setId("experimentalLabel");
        experimentalLabel.setWidth("150px");
        experimentalLabel.getElement().setAttribute("for", "experimentalCheckbox");
        experimentalCheckbox.getElement().setAttribute(STYLE, "margin-left:15px;width:50px;height:32px;");
        experimentalCheckbox.getElement().setId("experimentalCheckbox");
        experimentalGroup.add(experimentalCheckbox);
        experimentalGroup.add(experimentalLabel);

        heading.getElement().setTabIndex(0);

        generalInfoTopPanel.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));

        generalInfoTopPanel.add(new SpacerWidget());
        generalInfoTopPanel.add(new SpacerWidget());

        HorizontalPanel panel1 = new HorizontalPanel();
        panel1.add(libraryNameGroup);
        panel1.add(new SpacerWidget());
        panel1.add(libraryVersionGroup);
        generalInfoTopPanel.add(panel1);

        generalInfoTopPanel.add(new SpacerWidget());

        HorizontalPanel panel2 = new HorizontalPanel();
        panel2.add(descriptionGroup);
        panel2.add(new SpacerWidget());
        panel2.add(commentsGroup);
        generalInfoTopPanel.add(panel2);

        generalInfoTopPanel.add(new SpacerWidget());

        HorizontalPanel panel3 = new HorizontalPanel();
        panel3.add(usingModelGroup);
        panel3.add(new SpacerWidget());
        panel3.add(modelVersionGroup);
        generalInfoTopPanel.add(panel3);

        generalInfoTopPanel.add(new SpacerWidget());

        HorizontalPanel panel4 = new HorizontalPanel();
        panel4.add(stewardGroup);
        generalInfoTopPanel.add(panel4);

        generalInfoTopPanel.add(new SpacerWidget());

        HorizontalPanel panel5 = new HorizontalPanel();
        panel5.add(experimentalGroup);
        generalInfoTopPanel.add(panel5);

        generalInfoTopPanel.setStyleName("marginLeft15px");
        generalInfoMainHPanel.add(generalInfoTopPanel);

        buildButtonLayoutPanelForFhir();
    }

    public void buildButtonLayoutPanelForQDM() {
        generalInfoMainHPanel.setStyleName("cqlRightContainer");
        generalInfoMainHPanel.getElement().setId("generalInfoMainHPanel_HPanel");
        generalInfoMainHPanel.setWidth("715px");

        VerticalPanel generalInfoVPanel = new VerticalPanel();
        generalInfoVPanel.add(saveButton);

        FormLabel commentsLabel = new FormLabel();
        commentsLabel.setId("commentsLabel");
        commentsLabel.setFor("commentsContent");
        commentsLabel.setText("Comments");
        comments.getElement().setAttribute("maxlength", COMMENTS_MAX_LENGTH);
        comments.getElement().setAttribute("id", "commentsContent");
        comments.setHeight("220px");
        comments.setWidth("250px");
        commentsGroup.add(commentsLabel);
        commentsGroup.add(comments);
        commentsGroup.setStylePrimaryName("floatLeft");
        generalInfoVPanel.add(new SpacerWidget());
        generalInfoVPanel.add(new SpacerWidget());
        generalInfoVPanel.add(commentsGroup);
        generalInfoVPanel.add(new SpacerWidget());
        generalInfoMainHPanel.add(generalInfoVPanel);
    }

    public void buildButtonLayoutPanelForFhir() {
        generalInfoMainHPanel.setStyleName("cqlRightContainer");
        generalInfoMainHPanel.getElement().setId("generalInfoMainHPanel_HPanel");
        generalInfoMainHPanel.setWidth("715px");

        VerticalPanel generalInfoVPanel = new VerticalPanel();
        generalInfoVPanel.add(saveButton);

        generalInfoMainHPanel.add(generalInfoVPanel);
    }

    /**
     * This method will take a String and remove all non-alphabet/non-numeric characters
     * except underscore ("_") characters.
     *
     * @param originalString the original string
     * @return cleanedString
     */
    public String createCQLLibraryName(String originalString) {
        originalString = originalString.replaceAll(" ", "");
        String cleanedString = "";

        for (int i = 0; i < originalString.length(); i++) {
            char c = originalString.charAt(i);
            int intc = (int) c;
            if (c == '_' || (intc >= 48 && intc <= 57) || (intc >= 65 && intc <= 90) || (intc >= 97 && intc <= 122)) {

                if (!(cleanedString.isEmpty() && Character.isDigit(c))) {
                    cleanedString = cleanedString + "" + c;
                }

            }

        }

        return cleanedString;
    }

    public HorizontalPanel getView(boolean isEditable) {
        build();
        setIsEditable(isEditable);
        return generalInfoMainHPanel;
    }

    public MatTextBox getLibraryNameTextBox() {
        return libraryNameTextBox;
    }

    public MatTextBox getLibraryVersionTextBox() {
        return libraryVersionTextBox;
    }

    public MatTextBox getUsingModelTextBox() {
        return usingModelTextBox;
    }

    public MatTextBox getModelVersionTextBox() {
        return modelVersionTextBox;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setIsEditable(boolean isEditable) {
        getLibraryNameTextBox().setEnabled(isEditable);
        getSaveButton().setEnabled(isEditable);
        getCommentsTextBox().setEnabled(isEditable);
        getStewardListBox().setEnabled(isEditable);
        getDescriptionTextArea().setEnabled(isEditable);
        getExperimentalCheckbox().setEnabled(isEditable);
    }

    public void resetAll() {
        getLibraryNameTextBox().setText("");
        getLibraryVersionTextBox().setText("");
        getUsingModelTextBox().setText("");
        getModelVersionTextBox().setText("");
        getCommentsTextBox().setText("");
    }

    public void resetFormGroup() {
        getLibraryNameGroup().setValidationState(ValidationState.NONE);
        getCommentsGroup().setValidationState(ValidationState.NONE);
    }

    public FormGroup getLibraryNameGroup() {
        return libraryNameGroup;
    }

    public FormGroup getUsingModelGroup() {
        return usingModelGroup;
    }

    public FormGroup getLibraryVersionGroup() {
        return libraryVersionGroup;
    }

    public FormGroup getModelVersionGroup() {
        return modelVersionGroup;
    }

    public void setGeneralInfoOfLibrary(String libraryName,
                                        String version,
                                        String versionOfModel,
                                        String modelUsed,
                                        String comments,
                                        String description,
                                        List<MeasureSteward> allStewardList,
                                        String stewardId,
                                        boolean isExperimental) {
        logger.info("setGeneralInfoOfLibrary(" + libraryName + "," +
                version + "," +
                versionOfModel + "," +
                modelUsed + "," +
                comments + "," +
                allStewardList + "," +
                stewardId + "," +
                isExperimental + ")");
        getLibraryVersionTextBox().setText(defaultString(version));
        getUsingModelTextBox().setText(modelUsed);
        getModelVersionTextBox().setText(defaultString(versionOfModel));
        getLibraryNameTextBox().setText(defaultString(libraryName));
        getCommentsTextBox().setText(defaultString(comments));
        getCommentsTextBox().setCursorPos(0);
        getDescriptionTextArea().setText(defaultString(description));
        getDescriptionTextArea().setCursorPos(0);
        setOptionsInStewardList(allStewardList);
        getStewardListBox().setValue(defaultString(stewardId));
        getExperimentalCheckbox().setValue(isExperimental);
    }

    private String defaultString(String s) {
        return s == null ? "" : s;
    }

    public void setHeading(String text, String linkName) {
        String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
        heading.setHTML(linkStr + "<h4><b>" + text + "</b></h4>");
    }

    public TextArea getCommentsTextBox() {
        return comments;
    }

    public FormGroup getCommentsGroup() {
        return this.commentsGroup;
    }

    public InAppHelp getInAppHelp() {
        return inAppHelp;
    }


    public FormGroup getDescriptionGroup() {
        return descriptionGroup;
    }

    public FormGroup getStewardGroup() {
        return stewardGroup;
    }

    public FormGroup getExperimentalGroup() {
        return experimentalGroup;
    }

    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public ListBoxMVP getStewardListBox() {
        return stewardListBox;
    }

    public CheckBox getExperimentalCheckbox() {
        return experimentalCheckbox;
    }

    public boolean validateGeneralInformationSection(MessagePanel messagePanel,
                                                     String libraryName,
                                                     String commentBoxContent,
                                                     String description,
                                                     String stewardId,
                                                     boolean isExperimental) {
        CQLModelValidator validator = new CQLModelValidator();
        boolean isFhir = MatContext.get().isCurrentModelTypeFhir();
        logger.info("libraryName:" + libraryName +
                " commentBoxContent:" +commentBoxContent +
                " description:" + description +
                " stewardId:" + stewardId +
                " isExperimental:" + isExperimental +
                " isFhir:" + isFhir);

        if (libraryName != null && libraryName.isEmpty()) {
            getLibraryNameGroup().setValidationState(ValidationState.ERROR);
            messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getLibraryNameRequired());
            return false;
        }

        if (libraryName != null && !isFhir && !validator.isValidQDMName(libraryName)) {
            getLibraryNameGroup().setValidationState(ValidationState.ERROR);
            messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getQDMCqlLibyNameError());
            return false;
        }

        if (libraryName != null && isFhir && !validator.isValidFhirCqlName(libraryName)) {
            getLibraryNameGroup().setValidationState(ValidationState.ERROR);
            messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getFhirCqlLibyNameError());
            return false;
        }

        if (validator.isLibraryNameMoreThan500Characters(libraryName)) {
            getLibraryNameGroup().setValidationState(ValidationState.ERROR);
            messagePanel.getErrorMessageAlert().createAlert(LIBRARY_LENGTH_ERROR);
            return false;
        }

        if (!AbstractCQLWorkspacePresenter.isValidExpressionName(libraryName)) {
            getLibraryNameGroup().setValidationState(ValidationState.ERROR);
            messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getLibraryNameIsCqlKeywordError());
            return false;
        }

        if (validator.isCommentMoreThan2500Characters(commentBoxContent)) {
            getCommentsGroup().setValidationState(ValidationState.ERROR);
            messagePanel.getErrorMessageAlert().createAlert(COMMENT_LENGTH_ERROR);
            return false;
        }

        if (validator.doesCommentContainInvalidCharacters(commentBoxContent)) {
            getCommentsGroup().setValidationState(ValidationState.ERROR);
            messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_COMMENT_CHARACTERS());
            return false;
        }

        if (isFhir && (description == null || description.trim().equals(""))) {
            getDescriptionGroup().setValidationState(ValidationState.ERROR);
            messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLibraryDescriptionRequired());
            return false;
        }

        if (isFhir && (stewardId == null || stewardId.trim().equals(""))) {
            getStewardGroup().setValidationState(ValidationState.ERROR);
            messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlLibraryPublisherRequired());
            return false;
        }

        return true;
    }

    private void setOptionsInStewardList(List<MeasureSteward> allStewardList) {
        int i = 1;
        getStewardListBox().clear();
        getStewardListBox().addItem("--Select--");
        getStewardListBox().setSelectedIndex(i);
        for (final MeasureSteward m : allStewardList) {
            getStewardListBox().insertItem(m.getOrgName(), m.getId(), m.getOrgOid());
            if (getStewardId() != null && m.getId().equals(getStewardId())) {
                getStewardListBox().setSelectedIndex(i);
            }
            i = i + 1;
        }
    }

    public String getStewardId() {
        return stewardId;
    }

    public void setStewardId(String stewardId) {
        this.stewardId = stewardId;
    }

    public String getStewardValue() {
        return stewardValue;
    }

    public void setStewardValue(String stewardValue) {
        this.stewardValue = stewardValue;
    }
}
