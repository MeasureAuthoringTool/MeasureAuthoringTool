package mat.client.login;

import mat.client.ImageResources;
import mat.client.Mat;
import mat.client.shared.EmailAddressTextBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;

import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class LoginView.
 */
public class LoginView implements LoginPresenter.Display  {
	
	
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The welcome panel. */
	private Panel welcomePanel;
	
	/** The info message. */
	private HTML infoMessage = new HTML();
	
	/** The info message panel. */
	private Panel infoMessagePanel;
	
	/** The userid. */
	private TextBox userid;
	
	/** The password. */
	private TextBox password;
	
	/** One time password */
	private TextBox oneTimePassword;
	
	/** The submit button. */
	private Button submitButton;
	
	
	/** The forgot login id. */
	private Anchor forgotLoginId;
	
	/** The forgot password. */
	private Anchor forgotPassword;
	
	/** The main panel. */
	private VerticalPanel mainPanel = new VerticalPanel();
	
	/** The success. */
	Label success = new Label();
	
	/**
	 * Instantiates a new login view.
	 */
	public LoginView() {
		mainPanel.addStyleName("centered");
		
		
		welcomePanel = wrapInSpacer(new WelcomeLabelWidget());
		mainPanel.add(welcomePanel);
		
		Grid infoGrid = new Grid(2,2);
		FocusableImageButton focusableImageButton = new FocusableImageButton(ImageResources.INSTANCE.icon_success_sm(),"Success");
		infoGrid.setWidget(0, 0, focusableImageButton);
		Mat.removeInputBoxFromFocusPanel(focusableImageButton.getElement());
		success.setStyleName("loginInfoMessageHeader");
		infoGrid.setWidget(0, 1, success);
		infoGrid.setWidget(1, 1, infoMessage);
		SimplePanel infoMessage = new SimplePanel();
		infoMessage.add(infoGrid);
		infoMessage.setStyleName("loginInfoMessageContainer");
		infoMessagePanel = wrapInSpacer(infoMessage);
		mainPanel.add(infoMessagePanel);
		
		SimplePanel loginTitleHolder = new SimplePanel();
		Label loginTitlePanel = new Label("Please sign in");
		loginTitleHolder.add(loginTitlePanel);
		loginTitleHolder.setStylePrimaryName("loginBlueTitleHolder");
		loginTitlePanel.setStylePrimaryName("loginBlueTitle");
		mainPanel.add(loginTitleHolder);
		
		VerticalPanel loginPanel = new VerticalPanel();
		loginPanel.add(errorMessages);
		
		userid = new EmailAddressTextBox();
		userid.getElement().setAttribute("id", "UserID");
		//loginPanel.add(LabelBuilder.buildLabel(userid, "E-mail Address"));
		loginPanel.add(LabelBuilder.buildLabel(userid, "User ID"));
		loginPanel.add(userid);
		loginPanel.add(new SpacerWidget());
		
		password = new PasswordTextBox();
		loginPanel.add(LabelBuilder.buildLabel(password, "Password"));
		loginPanel.add(password);
		loginPanel.add(new SpacerWidget());
		
		oneTimePassword = new EmailAddressTextBox();
		oneTimePassword.getElement().setAttribute("id", "OneTimePassword");
		loginPanel.add(LabelBuilder.buildLabel(oneTimePassword, "Security Code"));
		loginPanel.add(oneTimePassword);
		loginPanel.add(new SpacerWidget());
		
		submitButton = new PrimaryButton("Sign In","primaryButton");
		loginPanel.add(submitButton);
		
		loginPanel.setStylePrimaryName("loginContentPanel");
		loginPanel.add(new SpacerWidget());
		
		HorizontalPanel hPanel = new HorizontalPanel();
		HTML desc = new HTML("Forgot your&nbsp;");
		HTML or = new HTML("&nbsp;or&nbsp;");
		forgotLoginId = new Anchor("User ID");
		forgotLoginId.setTitle("Forgot LoginId");
		forgotLoginId.getElement().setAttribute("alt", "User ID");
		forgotPassword = new Anchor("Password?");
		forgotPassword.setTitle("Forgot Password");
		forgotPassword.getElement().setAttribute("alt", "Password");
		hPanel.add(desc);
		hPanel.add(forgotLoginId);
		hPanel.add(or);
		hPanel.add(forgotPassword);
		loginPanel.add(hPanel);
		
		password.setWidth("200px");
		oneTimePassword.setWidth("200px");
		mainPanel.add(loginPanel);
		
	}
	
	/**
	 * Wrap in spacer.
	 * 
	 * @param w
	 *            the w
	 * @return the simple panel
	 */
	private SimplePanel wrapInSpacer(Widget w) {
		SimplePanel spacer = new SimplePanel();
		spacer.setStylePrimaryName("loginSpacer");
		spacer.add(w);
		return spacer;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#getSubmit()
	 */
	@Override
	public HasClickHandlers getSubmit() {
		return submitButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#getUserid()
	 */
	@Override
	public HasValue<String> getUserid() {
		return userid;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#getPassword()
	 */
	@Override
	public HasValue<String> getPassword() {
		return password;
	}
	
	@Override
	public HasValue<String> getOneTimePassword() {
		return oneTimePassword;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#getForgotPassword()
	 */
	@Override
	public HasClickHandlers getForgotPassword() {
		return forgotPassword;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#getInfoMessage()
	 */
	@Override
	public HasHTML getInfoMessage() {
		return infoMessage;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#setWelcomeVisible(boolean)
	 */
	@Override
	public void setWelcomeVisible(boolean value) {
		MatContext.get().setVisible(welcomePanel,value);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#setInfoMessageVisible(boolean)
	 */
	@Override
	public void setInfoMessageVisible(boolean value) {
		if(value){
			success.setText("Success");
		}else{
			success.setText("");
		}
		MatContext.get().setVisible(infoMessagePanel,value);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#getUseridField()
	 */
	@Override
	public HasKeyDownHandlers getUseridField() {
		return userid;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#getPasswordField()
	 */
	@Override
	public HasKeyDownHandlers getPasswordField() {
		return password;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#setInitialFocus()
	 */
	@Override
	public void setInitialFocus() {
		userid.setFocus(false);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.LoginPresenter.Display#getForgotLoginId()
	 */
	@Override
	public HasClickHandlers getForgotLoginId() {
		return forgotLoginId;
	}
	@Override
	public Button getSubmitButton() {
		return submitButton;
	}
}
