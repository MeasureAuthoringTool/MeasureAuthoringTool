package mat.client.cqlworkspace.generalinformation;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import mat.client.buttons.SaveToolBarButton;
import mat.client.cqlworkspace.AbstractCQLWorkspacePresenter;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.shared.MatContext;
import mat.client.shared.MessagePanel;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;
import mat.client.validator.ErrorHandler;
import mat.shared.CQLModelValidator;
import mat.shared.validator.measure.CommonMeasureValidator;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.List;

public class MeasureCQLGeneralInformationView implements CQLGeneralInformationView {

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
    protected FormGroup commentsGroup = new FormGroup();
    protected HTML heading = new HTML();

    protected static final String STYLE = "style";
    protected static final String PIXEL_150 = "150px";
    protected static final String FONT_SIZE_90_MARGIN_LEFT_15PX = "font-size:90%;margin-left:15px;";
    protected static final String MARGIN_STYLE = "margin-left:15px;margin-bottom:-15px;width:250px;height:32px;";

    protected static final String COMMENTS_MAX_LENGTH = "2500";
    protected static final int CQL_LIBRARY_NAME_MAX_LENGTH = 500;
    protected TextArea comments = new TextArea();
    protected InAppHelp inAppHelp = new InAppHelp("");
    private ErrorHandler errorHandler = new ErrorHandler();


    public MeasureCQLGeneralInformationView() {
        generalInfoMainHPanel.clear();
    }

    private void buildView() {

        libraryNameGroup.clear();
        usingModelGroup.clear();
        libraryVersionGroup.clear();
        modelVersionGroup.clear();
        commentsGroup.clear();
        heading.addStyleName("leftAligned");
        VerticalPanel generalInfoTopPanel = new VerticalPanel();

        FormLabel libraryNameLabel = new FormLabel();
        libraryNameLabel.setText("CQL Library Name (*)");
        libraryNameLabel.setTitle("CQL Library Name Required");
        libraryNameLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
        libraryNameLabel.setWidth(PIXEL_150);
        libraryNameLabel.setId("libraryNameLabel_Label");
        libraryNameLabel.setFor("libraryNameValue_TextBox");

        libraryNameTextBox.getElement().setAttribute(STYLE, MARGIN_STYLE);
        libraryNameTextBox.getElement().setId("libraryNameValue_TextBox");
        libraryNameTextBox.setTitle("Required");
        libraryNameTextBox.setMaxLength(CQL_LIBRARY_NAME_MAX_LENGTH);
        if (MatContext.get().isCurrentMeasureModelFhir()) {
            libraryNameTextBox.addBlurHandler(errorHandler.buildBlurHandler(libraryNameTextBox, libraryNameGroup,
                    (s) -> getFirst(CommonMeasureValidator.validateFhirLibraryName(s))));
        } else {
            libraryNameTextBox.addBlurHandler(errorHandler.buildBlurHandler(libraryNameTextBox, libraryNameGroup,
                    (s) -> getFirst(CommonMeasureValidator.validateQDMName(s))));
        }

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
        libraryVersionTextBox.setEnabled(false);
        libraryVersionTextBox.setTabIndex(-1);

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
        usingModelTextBox.setEnabled(false);
        usingModelTextBox.setTabIndex(-1);

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
        modelVersionTextBox.setEnabled(false);
        modelVersionTextBox.setTabIndex(-1);


        modelVersionGroup.add(modelVersionLabel);
        modelVersionGroup.add(modelVersionTextBox);

        heading.getElement().setTabIndex(-1);

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

        generalInfoMainHPanel.setStyleName("cqlRightContainer");
        generalInfoMainHPanel.getElement().setId("generalInfoMainHPanel_HPanel");
        generalInfoMainHPanel.setWidth("715px");
        generalInfoTopPanel.setStyleName("marginLeft15px");
        generalInfoMainHPanel.add(generalInfoTopPanel);
    }

    public void buildButtonLayoutPanel() {

        VerticalPanel generalInfoVPanel = new VerticalPanel();
        generalInfoVPanel.add(saveButton);

        FormLabel commentsLabel = new FormLabel();
        commentsLabel.setId("commentsLabel");
        commentsLabel.setFor("commentsContent");
        commentsLabel.setText("Comments");
        commentsLabel.setTitle("Comments");
        comments.getElement().setAttribute("maxlength", COMMENTS_MAX_LENGTH);
        comments.getElement().setAttribute("id", "commentsContent");
        comments.setTitle("Comments");
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
        generalInfoMainHPanel.clear();
        buildView();
        buildButtonLayoutPanel();
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

    public void setGeneralInfoOfLibrary(String libraryName, String version, String versionOfModel, String modelUsed,
                                        String comments) {
        getLibraryVersionTextBox().setText(version);
        getUsingModelTextBox().setText(modelUsed);
        getModelVersionTextBox().setText(versionOfModel);
        getLibraryNameTextBox().setText(libraryName);
        getCommentsTextBox().setText(comments);
        getCommentsTextBox().setCursorPos(0);
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

    public boolean validateGeneralInformationSection(MessagePanel messagePanel, String libraryName, String commentBoxContent) {
        CQLModelValidator validator = new CQLModelValidator();
        boolean isFhir = MatContext.get().isCurrentModelTypeFhir();

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
        return true;
    }

    private String getFirst(List<String> list) {
        return list != null && list.size() > 0 ? list.get(0) : null;
    }
}
