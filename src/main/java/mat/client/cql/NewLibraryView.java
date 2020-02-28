package mat.client.cql;

import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.TextArea;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.CqlLibraryPresenter;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.cqlworkspace.EditConfirmationDialogBox;
import mat.client.measure.AbstractNewMeasureView;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.util.FeatureFlagConstant;

import static mat.model.clause.ModelTypeHelper.FHIR;
import static mat.model.clause.ModelTypeHelper.QDM;

public class NewLibraryView implements CqlLibraryPresenter.DetailDisplay {


    private static final String CQL_LIBRARY_NAME = "CQL Library Name";
    private TextArea nameField = new TextArea();
    private ErrorMessageAlert errorMessage = new ErrorMessageAlert();
    private WarningConfirmationMessageAlert warningConfirmationAlert = new WarningConfirmationMessageAlert();
    private SuccessMessageAlert successMessage = new SuccessMessageAlert();
    protected HTML instructions = new HTML("Enter a CQL Library name. Then continue to the CQL Composer.");
    private SimplePanel mainDetailViewVP = new SimplePanel();
    private EditConfirmationDialogBox createNewConfirmationDialogBox = new EditConfirmationDialogBox();
    private RadioButton fhirModel = new RadioButton("measureModel", "FHIR");
    private RadioButton qdmModel = new RadioButton("measureModel", "QDM");
    FormLabel modelLabel = new FormLabel();
    private FormGroup libraryModelGroup = new FormGroup();

    SaveContinueCancelButtonBar buttonToolBar = new SaveContinueCancelButtonBar("cqlDetail");

    public NewLibraryView() {
        resetAll();
        mainDetailViewVP.clear();
    }

    private void buildView() {
        mainDetailViewVP.clear();
        resetAll();
        mainDetailViewVP.setStylePrimaryName("contentPanel");
        mainDetailViewVP.addStyleName("leftAligned");

        FormLabel nameLabel = new FormLabel();
        nameLabel.setText(CQL_LIBRARY_NAME);
        nameLabel.setTitle(CQL_LIBRARY_NAME);
        nameLabel.setShowRequiredIndicator(true);
        nameLabel.setMarginTop(5);
        nameLabel.setId("cqlLibraryName_Label");

        nameField.setId("CQLLibraryName_Input");
        nameField.setTitle("Enter CQL Library Name Required");
        nameField.setMaxLength(500);
        nameField.setWidth("400px");
        nameField.setHeight("50px");

        FormGroup nameGroup = new FormGroup();
        nameGroup.add(nameLabel);
        nameGroup.add(buildCQLLibraryNamePanel());

        addLibraryModelType();

        FormGroup buttonGroup = new FormGroup();
        buttonGroup.add(buttonToolBar);

        FieldSet formFieldSet = new FieldSet();
        formFieldSet.add(nameGroup);
        formFieldSet.add(libraryModelGroup);
        formFieldSet.add(buttonGroup);

        Form detailForm = new Form();
        detailForm.add(formFieldSet);

        VerticalPanel contentPanel = new VerticalPanel();
        contentPanel.setWidth("90%");
        contentPanel.setHeight("100%");
        contentPanel.add(new SpacerWidget());
        contentPanel.add(new SpacerWidget());
        contentPanel.add(instructions);
        contentPanel.add(errorMessage);
        contentPanel.add(warningConfirmationAlert);
        contentPanel.add(new SpacerWidget());
        contentPanel.add(new SpacerWidget());
        contentPanel.add(detailForm);
        contentPanel.add(new SpacerWidget());
        contentPanel.add(new SpacerWidget());


        mainDetailViewVP.add(contentPanel);
    }

    private HorizontalPanel buildCQLLibraryNamePanel() {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(nameField);
        horizontalPanel.add(new HTML(AbstractNewMeasureView.CAUTION_LIBRARY_NAME_MSG_STR));
        return horizontalPanel;
    }

    /**
     * Create a label for library model radio buttons
     *
     * @return modelLabel -> Model label Widget
     */
    private FormLabel buildModelTypeLabel() {
        modelLabel.setText("Model");
        modelLabel.setTitle("Model");
        modelLabel.setFor("libraryModel");
        modelLabel.setShowRequiredIndicator(true);
        modelLabel.setId("libraryModel_Id");
        return modelLabel;
    }

    /**
     * Builds a vertical panel with model types wrapped in
     *
     * @return mlibraryModelPanel
     */
    private VerticalPanel buildModelTypePanel() {
        VerticalPanel libraryModelPanel = new VerticalPanel();
        libraryModelGroup.add(buildModelTypeLabel());
        //new model creation defaulted to FHIR
        fhirModel.setValue(true);
        qdmModel.setEnabled(false);
        libraryModelPanel.add(fhirModel);
        libraryModelPanel.add(qdmModel);
        return libraryModelPanel;
    }

    /**
     * Add measure model type radios to create measure view iff 'MAT_ON_FHIR' flag is on
     */
    private void addLibraryModelType() {
        if (MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR)) {
            VerticalPanel modelTypePanel = buildModelTypePanel();
            libraryModelGroup.add(modelTypePanel);
        }
    }

    @Override
    public Widget asWidget() {
        buildView();
        return mainDetailViewVP;
    }

    @Override
    public HasValue<String> getName() {
        return nameField;
    }

    @Override
    public String getLibraryModelType() {
        return fhirModel.getValue() ? FHIR : QDM;
    }

    @Override
    public void setLibraryModelType(String type, boolean isDraft) {
        if (FHIR.equals(type)) {
            //set FHIR model
            fhirModel.setEnabled(true);
            fhirModel.setValue(true);
            //set QDM model off
            qdmModel.setValue(false);
            if (isDraft) {
                qdmModel.setEnabled(false);
            } else {
                qdmModel.setEnabled(true);
            }
        } else {
            //set QDM model
            qdmModel.setEnabled(true);
            qdmModel.setValue(true);
            //set FHIR model off
            fhirModel.setValue(false);
            if (isDraft) {
                fhirModel.setEnabled(false);
            } else {
                fhirModel.setEnabled(true);
            }
        }
    }

    @Override
    public HasClickHandlers getSaveButton() {
        return buttonToolBar.getSaveButton();
    }

    @Override
    public HasClickHandlers getCancelButton() {
        return buttonToolBar.getCancelButton();
    }

    @Override
    public void resetAll() {
        successMessage.clearAlert();
        errorMessage.clearAlert();
        nameField.setText("");
    }

    @Override
    public TextArea getNameField() {
        return nameField;
    }

    public void setNameField(TextArea nameField) {
        this.nameField = nameField;
    }

    @Override
    public ErrorMessageAlert getErrorMessage() {
        return errorMessage;
    }

    @Override
    public WarningConfirmationMessageAlert getWarningConfirmationAlert() {
        return warningConfirmationAlert;
    }

    public void setErrorMessage(ErrorMessageAlert errorMessage) {
        this.errorMessage = errorMessage;
    }

    public SuccessMessageAlert getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(SuccessMessageAlert successMessage) {
        this.successMessage = successMessage;
    }

    public EditConfirmationDialogBox getCreateNewConfirmationDialogBox() {
        return createNewConfirmationDialogBox;
    }

}
