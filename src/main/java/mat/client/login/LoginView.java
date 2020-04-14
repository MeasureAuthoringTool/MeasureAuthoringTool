package mat.client.login;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelFooter;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ColumnOffset;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.InputType;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.ChangePasswordWidget;

public class LoginView implements LoginPresenter.LoginViewDisplay  {
	private VerticalPanel mainPanel = new VerticalPanel();

	private Anchor signInWithHarp;
	private Anchor forgotLoginId;
	private Anchor forgotPassword;
	
	private Input userIdText = new Input(InputType.TEXT);
	private TextBox securityCodeInput = new TextBox();
	private Button submitButton = new Button("Sign In");
	private FormGroup passwordGroup = new FormGroup();
	private FormGroup userIdGroup = new FormGroup();
	private FormGroup authTokenGroup = new FormGroup();
	private FormGroup messageFormGrp = new FormGroup();
	private HelpBlock helpBlock = new HelpBlock();
	private Heading welcomeHeading = new Heading(HeadingSize.H4, "Welcome to the Measure Authoring Tool");
	
	private Panel successMessagePanel = new Panel();
	private PanelBody successMessageBody = new PanelBody();
	private ChangePasswordWidget changePasswordWidget = new ChangePasswordWidget();
	
	public LoginView() {
		Container loginFormContainer = new Container();
		
		successMessagePanel.setType(PanelType.SUCCESS);
		PanelHeader successPanelHeader = new PanelHeader();
		Icon checkIcon = new Icon(IconType.CHECK_CIRCLE);
		HTML headerText = new HTML("<h5>" + checkIcon + " <b>Success</b> </h5>");
		successPanelHeader.add(headerText);
		
		successMessagePanel.add(successPanelHeader);
		successMessagePanel.add(successMessageBody);
		successMessagePanel.setVisible(false);
		Row headingRow = new Row();
		Column headingCol = new Column(ColumnSize.SM_10);
		headingCol.setOffset(ColumnOffset.SM_3);
		headingCol.add(welcomeHeading);
		headingRow.add(headingCol);
		
		Row successMessagePanelRow = new Row();
		Column successMessageCol = new Column(ColumnSize.LG_8);
		successMessageCol.setOffset(ColumnOffset.MD_1);
		successMessageCol.add(successMessagePanel);
		successMessagePanelRow.add(successMessageCol);
		
		loginFormContainer.add(headingRow);
		loginFormContainer.add(successMessagePanelRow);
		
		Row mainRow = new Row();
		Column mainCol = new Column(ColumnSize.SM_10);
		mainCol.setOffset(ColumnOffset.SM_3);
		Panel loginPanel = new Panel();
		loginPanel.setWidth("300px");
		PanelHeader header = new PanelHeader();
		header.setStyleName("loginNewBlueTitleHolder");
		HTML loginText = new HTML("<strong>Please Sign In</strong>");
		header.add(loginText);
		PanelBody loginPanelBody = new PanelBody();
		
		Form loginForm = new Form();
		messageFormGrp.add(helpBlock);
		messageFormGrp.getElement().setAttribute("role", "alert");
		loginForm.add(messageFormGrp);
		
		FormLabel userIdLabel = new FormLabel();
		userIdLabel.setText("User ID");
		userIdLabel.setTitle("User ID");
		userIdLabel.setFor("inputUserId");
		userIdText.setWidth("250px");
		userIdText.setHeight("27px");
		userIdText.setId("inputUserId");
		userIdText.setPlaceholder("Enter User ID");
		userIdText.setTitle("Enter User ID Required");
		userIdGroup.add(userIdLabel);
		userIdGroup.add(userIdText);
		
		FormLabel passwordLabel = new FormLabel();
		passwordLabel.setText("Password");
		passwordLabel.setTitle("Password");
		passwordLabel.setFor("inputPwd");
		changePasswordWidget.getPassword().setId("inputPwd");
		changePasswordWidget.getPassword().setWidth("250px");
		changePasswordWidget.getPassword().setHeight("27px");
		changePasswordWidget.getPassword().setPlaceholder("Enter Password");
		changePasswordWidget.getPassword().setTitle("Enter Password Required");
		changePasswordWidget.getPassword().setValidateOnBlur(true);
		passwordGroup.add(passwordLabel);
		passwordGroup.add(changePasswordWidget.getPassword());
		
		FormLabel authLabel = new FormLabel();
		authLabel.setText("Security Code");
		authLabel.setTitle("Security Code");
		authLabel.setFor("inputAuthCode");
		securityCodeInput.setWidth("210px");
		securityCodeInput.setHeight("28.5px");
		securityCodeInput.setId("inputAuthCode");
		securityCodeInput.setPlaceholder("Enter Security Code ");
		securityCodeInput.setTitle("Enter Security Code Required");
		
		InputGroup securityInputGroup = new InputGroup();
		InputGroupAddon addon = new InputGroupAddon();
		addon.setIcon(IconType.KEY);
		securityInputGroup.add(addon);
		securityInputGroup.add(securityCodeInput);
		
		authTokenGroup.add(authLabel);
		authTokenGroup.add(securityInputGroup);
		
		FormGroup buttonFormGroup = new FormGroup();
		submitButton.setType(ButtonType.SUCCESS);
		submitButton.setTitle("Sign In");
		buttonFormGroup.add(submitButton);
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(userIdGroup);
		formFieldSet.add(passwordGroup);
		formFieldSet.add(authTokenGroup);
		formFieldSet.add(buttonFormGroup);
		loginForm.add(formFieldSet);
		loginPanelBody.add(loginForm);
		
		PanelFooter loginPanelFooter = new PanelFooter();
		HorizontalPanel hPanel = new HorizontalPanel();
		HTML desc = new HTML("Forgot your&nbsp;");
		HTML or = new HTML("&nbsp;or&nbsp;");
		forgotLoginId = new Anchor();
		forgotLoginId.setText("User ID");
		forgotLoginId.setTitle("Forgot LoginId");
		forgotLoginId.getElement().setAttribute("alt", "User ID");
		forgotPassword = new Anchor();
		forgotPassword.setText("Password?");
		forgotPassword.setTitle("Forgot Password");
		forgotPassword.getElement().setAttribute("alt", "Password");
		hPanel.add(desc);
		hPanel.add(forgotLoginId);
		hPanel.add(or);
		hPanel.add(forgotPassword);

		HorizontalPanel harpPanel = new HorizontalPanel();
		signInWithHarp = new Anchor();
		signInWithHarp.setText("Sign In with HARP");
		signInWithHarp.setTitle("Sign In with HARP");
		signInWithHarp.getElement().setAttribute("alt", "HARP Login");
		harpPanel.add(signInWithHarp);
		
		loginPanelFooter.add(hPanel);
		loginPanelFooter.add(harpPanel);
		
		loginPanel.add(header);
		loginPanel.add(loginPanelBody);
		loginPanel.add(loginPanelFooter);
		
		mainCol.add(loginPanel);
		mainRow.add(mainCol);
		loginFormContainer.add(mainRow);
		mainPanel.add(loginFormContainer);
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	
	@Override
	public Anchor getForgotLoginId() {
		return forgotLoginId;
	}
	
	@Override
	public void setForgotLoginId(Anchor forgotLoginId) {
		this.forgotLoginId = forgotLoginId;
	}
	
	@Override
	public Anchor getForgotPassword() {
		return forgotPassword;
	}

	@Override
	public void setForgotPassword(Anchor forgotPassword) {
		this.forgotPassword = forgotPassword;
	}

	@Override
	public Anchor getSignInWithHarp() {
		return signInWithHarp;
	}

	@Override
	public void setSignInWithHarp(Anchor signInWithHarp) {
		this.signInWithHarp = signInWithHarp;
	}
	
	@Override
	public Input getUserIdText() {
		return userIdText;
	}
	
	@Override
	public void setUserIdText(Input userIdText) {
		this.userIdText = userIdText;
	}
	
	@Override
	public Input getPasswordInput() {
		return changePasswordWidget.getPassword();
	}
	
	@Override
	public void setPasswordInput(Input passwordInput) {
		this.changePasswordWidget.setPassword(passwordInput);
	}
	
	@Override
	public Button getSubmitButton() {
		return submitButton;
	}
	
	@Override
	public TextBox getSecurityCodeInput() {
		return securityCodeInput;
	}
	@Override
	
	public FormGroup getPasswordGroup() {
		return passwordGroup;
	}
	@Override
	
	public FormGroup getAuthTokenGroup() {
		return authTokenGroup;
	}
	
	@Override
	public FormGroup getUserIdGroup() {
		return userIdGroup;
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
	public Heading getWelcomeHeading() {
		return welcomeHeading;
	}
	
	@Override
	public Panel getSuccessMessagePanel() {
		return successMessagePanel;
	}
	
	@Override
	public PanelBody getSuccessMessageBody() {
		return successMessageBody;
	}
	
	
	
}
