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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.CqlLibraryPresenter;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.cqlworkspace.EditConfirmationDialogBox;
import mat.client.measure.AbstractNewMeasureView;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.WarningConfirmationMessageAlert;

public class NewLibraryView implements CqlLibraryPresenter.DetailDisplay{
	

	private static final String CQL_LIBRARY_NAME = "CQL Library Name";
	private TextArea nameField = new TextArea();
	private ErrorMessageAlert errorMessage = new ErrorMessageAlert();
	private WarningConfirmationMessageAlert warningConfirmationAlert = new WarningConfirmationMessageAlert();
	private SuccessMessageAlert successMessage = new SuccessMessageAlert();
	protected HTML instructions = new HTML("Enter a CQL Library name. Then continue to the CQL Composer.");
	private SimplePanel mainDetailViewVP = new SimplePanel();
	private EditConfirmationDialogBox createNewConfirmationDialogBox = new EditConfirmationDialogBox();
	
	SaveContinueCancelButtonBar buttonToolBar = new SaveContinueCancelButtonBar("cqlDetail");
	
	public NewLibraryView(){
		resetAll();
		mainDetailViewVP.clear();
	}
	
	private void buildView(){
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

		FormGroup buttonGroup = new FormGroup();
		buttonGroup.add(buttonToolBar);
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(nameGroup);
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
	
	@Override
	public Widget asWidget(){
		buildView();
		return mainDetailViewVP;
	}

	@Override
	public HasValue<String> getName() {
		return nameField;
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
