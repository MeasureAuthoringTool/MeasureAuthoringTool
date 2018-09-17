package mat.client.login;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ColumnOffset;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.InputType;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.buttons.CancelButton;
import mat.client.shared.RequiredIndicator;

/**
 * The Class ForgottenLoginIdView.
 */
public class ForgottenLoginIdView  implements ForgottenLoginIdPresenter.Display{
	private VerticalPanel mainPanel = new VerticalPanel();
	
	private Input emailAddressText = new Input(InputType.TEXT);
	private FormGroup emailAddressGroup = new FormGroup();
	
	
	/** The error messages. */
	private FormGroup messageFormGrp = new FormGroup();
	private HelpBlock helpBlock = new HelpBlock();
	private Button submitButton = new Button("Submit");
	private Button resetButton = new CancelButton("ForgottenLoginId");
	/**
	 * Instantiates a new forgotten login id view.
	 */
	public ForgottenLoginIdView() {
		Container loginFormContianer = new Container();
		Row mainRow = new Row();
		Column mainCol = new Column(ColumnSize.LG_12);
		mainCol.setOffset(ColumnOffset.LG_3);
		//Login Panel.
		Panel loginPanel = new Panel();
		loginPanel.setWidth("450px");
		//Login Panel Header.
		PanelHeader header = new PanelHeader();
		header.setStyleName("loginNewBlueTitleHolder");
		HTML loginText = new HTML("<strong>Request your User ID</strong>");
		header.add(loginText);
		//Login Panel Body.
		PanelBody loginPanelBody = new PanelBody();
		
		Form loginForm = new Form();
		FormLabel emailAddressLabel = new FormLabel();
		emailAddressLabel.setText("Email Address");
		emailAddressLabel.setTitle("Enter Email Address Required");
		emailAddressLabel.setFor("inputEmailAddress");
		emailAddressText.setWidth("250px");
		emailAddressText.setHeight("27px");
		emailAddressText.setId("inputUserId");
		emailAddressText.setPlaceholder("Enter Email Address");
		emailAddressText.setTitle("Enter Email Address");
		Label instructions = new Label("Enter the E-mail Address associated with your MAT account:");
		instructions.setStylePrimaryName("loginForgotInstructions");
		FormGroup instructionGroup = new FormGroup();
		instructionGroup.add(instructions);
		
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.getElement().setId("hPanel_HorizontalPanel");
		HTML required = new HTML(RequiredIndicator.get());
		hPanel.add(emailAddressLabel);
		hPanel.add(required);
		
		emailAddressGroup.add(hPanel);
		emailAddressGroup.add(emailAddressText);
		
		//add to form
		messageFormGrp.add(helpBlock);
		messageFormGrp.getElement().setAttribute("role", "alert");
		loginForm.add(messageFormGrp);
		
		FormGroup buttonFormGroup = new FormGroup();
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		submitButton.setType(ButtonType.PRIMARY);
		submitButton.setTitle("Submit");

		buttonToolBar.add(submitButton);
		buttonToolBar.add(resetButton);
		
		buttonFormGroup.add(buttonToolBar);
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(instructionGroup);
		formFieldSet.add(emailAddressGroup);
		formFieldSet.add(buttonFormGroup);
		loginForm.add(formFieldSet);
		loginPanelBody.add(loginForm);
		
		
		loginPanel.add(header);
		loginPanel.add(loginPanelBody);
		
		mainCol.add(loginPanel);
		mainRow.add(mainCol);
		loginFormContianer.add(mainRow);
		mainPanel.add(loginFormContianer);
	}
	
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	@Override
	public Input getEmailAddressText() {
		return emailAddressText;
	}
	@Override
	public void setEmailAddressText(Input emailAddressText) {
		this.emailAddressText = emailAddressText;
	}
	@Override
	public FormGroup getEmailAddressGroup() {
		return emailAddressGroup;
	}
	@Override
	public FormGroup getMessageFormGrp() {
		return messageFormGrp;
	}
	@Override
	public HelpBlock getHelpBlock() {
		return helpBlock;
	}
	@Override
	public Button getSubmitButton() {
		return submitButton;
	}
	@Override
	public
	Button getResetButton() {
		return resetButton;
	}
	
	
	
	
	
}
