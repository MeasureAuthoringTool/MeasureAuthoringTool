package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.CqlLibraryPresenter;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
public class CQLLibraryDetailView implements CqlLibraryPresenter.DetailDisplay{
	
	private Button saveButton = new Button("Save and Continue");
	private Button cancelButton = new Button("Cancel");
	private TextArea nameField = new TextArea();
	private ErrorMessageAlert errorMessage = new ErrorMessageAlert();
	private SuccessMessageAlert successMessage = new SuccessMessageAlert();
	protected HTML instructions = new HTML("Enter a CQL Library name. Then continue to the CQL Composer.");
	private SimplePanel mainDetailViewVP = new SimplePanel();
	
	public CQLLibraryDetailView(){
		resetAll();
		mainDetailViewVP.clear();
	}
	
	private void buildView(){
		mainDetailViewVP.clear();
		resetAll();
		mainDetailViewVP.setStylePrimaryName("contentPanel");
		mainDetailViewVP.addStyleName("leftAligned");
		
		VerticalPanel contentPanel = new VerticalPanel();
		contentPanel.setWidth("90%");	
		contentPanel.setHeight("100%");
		
		
		FormGroup nameGroup = new FormGroup();
		FormLabel nameLabel = new FormLabel();
		nameLabel.setText("Name");
		nameLabel.setTitle("Name");
		nameLabel.setShowRequiredIndicator(true);
		nameLabel.setMarginTop(5);
		nameLabel.setId("cqlLibraryName_Label");
		
		nameField.setMaxLength(500);
		nameField.setWidth("400px");
		nameField.setHeight("50px");
		
		
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		saveButton.setType(ButtonType.PRIMARY);
		saveButton.setTitle("Save and Continue");
		cancelButton.setType(ButtonType.DANGER);
		cancelButton.setTitle("Cancel");
		buttonToolBar.add(saveButton);
		buttonToolBar.add(cancelButton);
		
		
		nameGroup.add(nameLabel);
		nameGroup.add(nameField);
		FormGroup buttonGroup = new FormGroup();
		buttonGroup.add(buttonToolBar);
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(nameGroup);
		formFieldSet.add(buttonGroup);
		Form detailForm = new Form();
		detailForm.add(formFieldSet);
		contentPanel.add(new SpacerWidget());
		contentPanel.add(new SpacerWidget());
		contentPanel.add(instructions);
		contentPanel.add(errorMessage);
		contentPanel.add(new SpacerWidget());
		contentPanel.add(new SpacerWidget());
		
		contentPanel.add(detailForm);
		contentPanel.add(new SpacerWidget());
		contentPanel.add(new SpacerWidget());
		
		
		mainDetailViewVP.add(contentPanel);
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
		return saveButton;
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return cancelButton;
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

	public void setErrorMessage(ErrorMessageAlert errorMessage) {
		this.errorMessage = errorMessage;
	}

	public SuccessMessageAlert getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(SuccessMessageAlert successMessage) {
		this.successMessage = successMessage;
	}
	
	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

}
