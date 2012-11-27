package org.ifmc.mat.client.login;

import org.ifmc.mat.client.shared.ChangePasswordWidget;
import org.ifmc.mat.client.shared.ErrorMessageDisplay;
import org.ifmc.mat.client.shared.ErrorMessageDisplayInterface;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.client.shared.PrimaryButton;
import org.ifmc.mat.client.shared.SpacerWidget;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TempPwdView implements TempPwdLoginPresenter.Display {

	private Panel welcomePanel;
	private HTML infoMessage = new HTML();
	private Panel infoMessagePanel;
	private ChangePasswordWidget changePasswordWidget = 
		new ChangePasswordWidget();
	private Button submitButton;
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private VerticalPanel mainPanel = new VerticalPanel();
	public TempPwdView() {
		mainPanel.addStyleName("centered");
		welcomePanel = wrapInSpacer(new WelcomeLabelWidget());
		mainPanel.add(welcomePanel);
		
		Grid infoGrid = new Grid(2,2);
		infoGrid.setWidget(0, 0, new Image("images/icon_success_sm.gif"));
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
		HTML changePasswordlabel = new HTML("<b>Change Password</b>");
		changePasswordlabel.addStyleName("addPadding");
		loginPanel.add(changePasswordlabel);
		loginPanel.add(new SpacerWidget());
		
		loginPanel.add(changePasswordWidget);
		loginPanel.add(new SpacerWidget());
		
		submitButton = new PrimaryButton("Sign In");
		loginPanel.add(submitButton);
		
		loginPanel.setStylePrimaryName("loginContentPanel");
		mainPanel.add(loginPanel);
	}


	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	
	private SimplePanel wrapInSpacer(Widget w) {
		SimplePanel spacer = new SimplePanel();
		spacer.setStylePrimaryName("loginSpacer");
		spacer.add(w);
		return spacer;
	}


	@Override
	public HasValue<String> getPassword() {
		return changePasswordWidget.getPassword();
	}


	@Override
	public HasValue<String> getConfirmPassword() {
		return changePasswordWidget.getConfirmPassword();
	}


	@Override
	public HasClickHandlers getSubmit() {
		return submitButton;
	}


	@Override
	public void setWelcomeVisible(boolean value) {
		MatContext.get().setVisible(welcomePanel,value);
	}


	@Override
	public void setInfoMessageVisible(boolean value) {
		MatContext.get().setVisible(infoMessagePanel,value);
	}


	@Override
	public HasKeyDownHandlers getNewPasswordField() {
		return changePasswordWidget.getPassword();
	}


	@Override
	public HasKeyDownHandlers getConfirmPasswordField() {
		return changePasswordWidget.getConfirmPassword();
	}	
}
