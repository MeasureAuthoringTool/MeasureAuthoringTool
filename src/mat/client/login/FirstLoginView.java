package mat.client.login;

import java.util.List;

import mat.client.shared.ChangePasswordWidget;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.NameValuePair;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SecurityQuestionWithMaskedAnswerWidget;
import mat.client.shared.SpacerWidget;
import mat.client.shared.PasswordRules;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * The Class FirstLoginView.
 */
public class FirstLoginView implements FirstLoginPresenter.Display {
	
	/** The main panel. */
	private VerticalPanel mainPanel;
	
	/** The security questions widget. */
	private SecurityQuestionWithMaskedAnswerWidget securityQuestionsWidget = 
			new SecurityQuestionWithMaskedAnswerWidget();
	
	/** The change password widget. */
	private ChangePasswordWidget changePasswordWidget = 
		new ChangePasswordWidget();
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("firstLogin");
	
	/** The pwd error messages. */
	private ErrorMessageDisplay pwdErrorMessages = new ErrorMessageDisplay();
	
	/** The sec error messages. */
	private ErrorMessageDisplay secErrorMessages = new ErrorMessageDisplay();
	
	/** The welcome panel. */
	private Panel welcomePanel;
	
	/**
	 * Instantiates a new first login view.
	 */
	public FirstLoginView() {
		mainPanel = new VerticalPanel();
		mainPanel.addStyleName("centered");
		
		welcomePanel = wrapInSpacer(new WelcomeLabelWidget());
		mainPanel.add(welcomePanel);
		
		SimplePanel titleHolder = new SimplePanel();
		Label titlePanel = new Label("Initial Sign In");
		FocusPanel focusTitlePanel = new FocusPanel(titlePanel);
		focusTitlePanel.setTitle("Initial Sign In");
		titleHolder.add(focusTitlePanel);
		titleHolder.setStylePrimaryName("loginBlueTitleHolder");
		titleHolder.setWidth("100%");
		titlePanel.setStylePrimaryName("loginBlueTitle");
		mainPanel.add(titleHolder);
		
		VerticalPanel bluePanel = new VerticalPanel();
		bluePanel.setStylePrimaryName("loginContentPanel");
		bluePanel.setWidth("100%");
		
		Label required = new Label("All fields are required");
		bluePanel.add(required);
		bluePanel.add(new SpacerWidget());
		
		bluePanel.add(pwdErrorMessages);
		
		
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.getElement().setId("hPanel_HorizontalPanel");
		bluePanel.add(buildInstructions("Change Password"));
		hPanel.add(changePasswordWidget);
		
		PasswordRules rules = new PasswordRules();
		rules.addStyleName("leftAligned_small_text");
		rules.addStyleName("myAccountPasswordRules");
		hPanel.add(rules);
		bluePanel.add(hPanel);
		bluePanel.add(new SpacerWidget());
		bluePanel.add(new SpacerWidget());
		
		bluePanel.add(secErrorMessages);
		FocusPanel securityInstructionsFocusPanel = new FocusPanel(buildInstructions("Security Questions &amp; Answers"));
		securityInstructionsFocusPanel.setTitle("Security Questions and Answers.");
		bluePanel.add(securityInstructionsFocusPanel);
		bluePanel.add(securityQuestionsWidget);
		buttonBar.getSaveButton().setText("Submit");
		bluePanel.add(buttonBar);
		
		mainPanel.add(bluePanel);
	}
	
	/**
	 * Builds the instructions.
	 * 
	 * @param msg
	 *            the msg
	 * @return the html
	 */
	private HTML buildInstructions(String msg) {
		HTML instructions = new HTML(msg);
		instructions.setStylePrimaryName("loginForgotInstructions");
		return instructions;
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
	 * @see mat.client.login.FirstLoginPresenter.Display#getSubmit()
	 */
	@Override
	public HasClickHandlers getSubmit() {
		return buttonBar.getSaveButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getReset()
	 */
	@Override
	public HasClickHandlers getReset() {
		return buttonBar.getCancelButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getQuestion1Answer()
	 */
	@Override
	public HasValue<String> getQuestion1Answer() {
		return securityQuestionsWidget.getAnswer1();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getQuestion2Answer()
	 */
	@Override
	public HasValue<String> getQuestion2Answer() {
		return securityQuestionsWidget.getAnswer2();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getQuestion3Answer()
	 */
	@Override
	public HasValue<String> getQuestion3Answer() {
		return securityQuestionsWidget.getAnswer3();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getQuestion1Text()
	 */
	@Override
	public HasValue<String> getQuestion1Text() {
		return securityQuestionsWidget.getSecurityQuestion1();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getQuestion2Text()
	 */
	@Override
	public HasValue<String> getQuestion2Text() {
		return securityQuestionsWidget.getSecurityQuestion2();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getQuestion3Text()
	 */
	@Override
	public HasValue<String> getQuestion3Text() {
		return securityQuestionsWidget.getSecurityQuestion3();
	}


	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getPasswordErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getPasswordErrorMessageDisplay() {
		return pwdErrorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getSecurityErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getSecurityErrorMessageDisplay() {
		return secErrorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getPassword()
	 */
	@Override
	public HasValue<String> getPassword() {
		return changePasswordWidget.getPassword();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getConfirmPassword()
	 */
	@Override
	public HasValue<String> getConfirmPassword() {
		return changePasswordWidget.getConfirmPassword();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#addSecurityQuestionTexts(java.util.List)
	 */
	@Override
	public void addSecurityQuestionTexts(List<NameValuePair> texts) {
		securityQuestionsWidget.getSecurityQuestion1().setDropdownOptions(texts);
		securityQuestionsWidget.getSecurityQuestion2().setDropdownOptions(texts);
		securityQuestionsWidget.getSecurityQuestion3().setDropdownOptions(texts);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getSecurityQuestionsWidget()
	 */
	public SecurityQuestionWithMaskedAnswerWidget getSecurityQuestionsWidget() {
		return securityQuestionsWidget;
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getAnswerText1()
	 */
	@Override
	public String getAnswerText1() {
		return securityQuestionsWidget.getAnswerText1();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getAnswerText2()
	 */
	@Override
	public String getAnswerText2() {
		return securityQuestionsWidget.getAnswerText2();
		}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getAnswerText3()
	 */
	@Override
	public String getAnswerText3() {
		return securityQuestionsWidget.getAnswerText3();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#setAnswerText1(java.lang.String)
	 */
	@Override
	public void setAnswerText1(String answerText1) {
		securityQuestionsWidget.setAnswerText1(answerText1);
		
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#setAnswerText2(java.lang.String)
	 */
	@Override
	public void setAnswerText2(String answerText2) {
		securityQuestionsWidget.setAnswerText2(answerText2);
		
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#setAnswerText3(java.lang.String)
	 */
	@Override
	public void setAnswerText3(String answerText3) {
		securityQuestionsWidget.setAnswerText3(answerText3);
		
	}	
}
