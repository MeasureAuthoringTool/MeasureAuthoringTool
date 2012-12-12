package mat.client.login;

import java.util.List;

import mat.client.shared.EmailAddressTextBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.NameValuePair;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ForgottenPasswordView implements ForgottenPasswordPresenter.Display {
	private Panel mainPanel;
	private TextBox email;
	private TextBox loginId;
	private ListBoxMVP securityQuestion;
	private TextBox securityAnswer;
	private SaveCancelButtonBar buttonBar;
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	public ForgottenPasswordView() {
		mainPanel = new VerticalPanel();
		mainPanel.addStyleName("centered");
		
		
		SimplePanel titleHolder = new SimplePanel();
		Label titlePanel = new Label("Request New Password");
		titleHolder.add(titlePanel);
		titleHolder.setStylePrimaryName("loginBlueTitleHolder");
		titleHolder.setWidth("100%");
		titlePanel.setStylePrimaryName("loginBlueTitle");
		mainPanel.add(titleHolder);
		
		VerticalPanel bluePanel = new VerticalPanel();
		bluePanel.setStylePrimaryName("loginContentPanel");
		
		Label instructions = new Label("To request a new password, please provide the following information:");
		instructions.setStylePrimaryName("loginForgotInstructions");
		bluePanel.add(instructions);
		bluePanel.add(new SpacerWidget());
		
		bluePanel.add(errorMessages);
		
		loginId = new EmailAddressTextBox();
		bluePanel.add(LabelBuilder.buildLabel(loginId, "User Id"));
		bluePanel.add(loginId);
		bluePanel.add(new SpacerWidget());
		
		securityQuestion = new ListBoxMVP();
		bluePanel.add(LabelBuilder.buildLabel(securityQuestion, "Security Question"));
		bluePanel.add(securityQuestion);
		bluePanel.add(new SpacerWidget());
		
		securityAnswer = new TextBox();
		bluePanel.add(LabelBuilder.buildLabel(securityAnswer, "Security Question Answer"));
		bluePanel.add(securityAnswer);
		bluePanel.add(new SpacerWidget());
		
		buttonBar = new SaveCancelButtonBar();
		buttonBar.getSaveButton().setText("Submit");
		bluePanel.add(buttonBar);
		
		mainPanel.add(bluePanel);
		
	}	

	/*@Override
	public HasValue<String> getEmail() {
		return email;
	}*/

	@Override
	public HasValue<String> getSecurityQuestion() {
		return securityQuestion;
	}

	@Override
	public HasValue<String> getSecurityAnswer() {
		return securityAnswer;
	}

	@Override
	public HasClickHandlers getSubmit() {
		return buttonBar.getSaveButton();
	}

	@Override
	public HasClickHandlers getReset() {
		return buttonBar.getCancelButton();
	}


	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public void addSecurityQuestionOptions(List<NameValuePair> texts) {
		securityQuestion.setDropdownOptions(texts);
	}
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public void setSecurityQuestionAnswerEnabled(boolean enabled) {
		securityQuestion.setEnabled(enabled);
		securityAnswer.setEnabled(enabled);
	}

	@Override
	public void setFocus(boolean focus) {
		securityQuestion.setFocus(focus);
	}

	@Override
	public HasValue<String> getLoginId() {
		return loginId;
	}
	
}
