package org.ifmc.mat.client.login;

import org.ifmc.mat.client.ImageResources;
import org.ifmc.mat.client.shared.EmailAddressTextBox;
import org.ifmc.mat.client.shared.ErrorMessageDisplay;
import org.ifmc.mat.client.shared.ErrorMessageDisplayInterface;
import org.ifmc.mat.client.shared.FocusableImageButton;
import org.ifmc.mat.client.shared.LabelBuilder;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.client.shared.PrimaryButton;
import org.ifmc.mat.client.shared.SpacerWidget;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
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

public class LoginView implements LoginPresenter.Display  {


	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private Panel welcomePanel;
	private HTML infoMessage = new HTML();
	private Panel infoMessagePanel;
	private TextBox userid;
	private TextBox password;
	private Button submitButton;
	private Anchor forgotPassword;
	private VerticalPanel mainPanel = new VerticalPanel();
	
	public LoginView() {
		mainPanel.addStyleName("centered");
		
		
		welcomePanel = wrapInSpacer(new WelcomeLabelWidget());
		mainPanel.add(welcomePanel);
		
		Grid infoGrid = new Grid(2,2);
		infoGrid.setWidget(0, 0, new FocusableImageButton(ImageResources.INSTANCE.icon_success_sm(),"Success"));
		Label success = new Label("Success");
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
		loginPanel.add(LabelBuilder.buildLabel(userid, "E-mail Address"));
		loginPanel.add(userid);
		loginPanel.add(new SpacerWidget());
		
		password = new PasswordTextBox();
		loginPanel.add(LabelBuilder.buildLabel(password, "Password"));
		loginPanel.add(password);
		loginPanel.add(new SpacerWidget());
		
		submitButton = new PrimaryButton("Sign In");
		loginPanel.add(submitButton);
		
		loginPanel.setStylePrimaryName("loginContentPanel");
		loginPanel.add(new SpacerWidget());
		
		HorizontalPanel hPanel = new HorizontalPanel();
		forgotPassword = new Anchor("Forgot Password?");
		forgotPassword.setTitle("Forgot Password");
		forgotPassword.getElement().setAttribute("alt", "Forgot Password");
		FocusableImageButton questionMark = new FocusableImageButton(ImageResources.INSTANCE.help(),"help");
		hPanel.add(questionMark);
		hPanel.add(forgotPassword);
		loginPanel.add(hPanel);
		
		password.setWidth("200px");
		
		mainPanel.add(loginPanel);
		
	}

	private SimplePanel wrapInSpacer(Widget w) {
		SimplePanel spacer = new SimplePanel();
		spacer.setStylePrimaryName("loginSpacer");
		spacer.add(w);
		return spacer;
	}

	@Override
	public HasClickHandlers getSubmit() {
		return submitButton;
	}

	@Override
	public HasValue<String> getUserid() {
		return userid;
	}

	@Override
	public HasValue<String> getPassword() {
		return password;
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public HasClickHandlers getForgotPassword() {
		return forgotPassword;
	}

	@Override
	public HasHTML getInfoMessage() {
		return infoMessage;
	}

	@Override
	public void setWelcomeVisible(boolean value) {
		MatContext.get().setVisible(welcomePanel,value);
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	@Override
	public void setInfoMessageVisible(boolean value) {
		MatContext.get().setVisible(infoMessagePanel,value);
	}

	@Override
	public HasKeyDownHandlers getUseridField() {
		return userid;
	}

	@Override
	public HasKeyDownHandlers getPasswordField() {
		return password;
	}

	@Override
	public void setInitialFocus() {
		userid.setFocus(false);
	}
	
}
