package mat.client.login;

import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Input;

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

import mat.client.shared.ChangePasswordWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.NameValuePair;
import mat.client.shared.PasswordRules;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SecurityQuestionAnswerWidget;
import mat.client.shared.SecurityQuestionsDisplay;
import mat.client.shared.SpacerWidget;


/**
 * The Class FirstLoginView.
 */
public class FirstLoginView implements SecurityQuestionsDisplay {
	
	/** The main panel. */
	private VerticalPanel mainPanel;
	
	/** The security questions widget. */
	private SecurityQuestionAnswerWidget securityQuestionsWidget = 
			new SecurityQuestionAnswerWidget();
	
	/** The change password widget. */
	private ChangePasswordWidget changePasswordWidget = 
		new ChangePasswordWidget();
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("firstLogin");
	
	/** The pwd error messages. */
	private MessageAlert pwdErrorMessages = new ErrorMessageAlert();
	
	/** The sec error messages. */
	private MessageAlert secErrorMessages = new ErrorMessageAlert();
	
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
		Form formPassword = new Form();
		FieldSet fieldSet = new FieldSet();
		fieldSet.add(changePasswordWidget.getPasswordGroup());
		fieldSet.add(changePasswordWidget.getConfirmPasswordGroup());
		formPassword.add(fieldSet);
		hPanel.add(formPassword);
		
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
		
		Form formSecurityQuestionAnswer = new Form();
		FieldSet fieldSetQnA = new FieldSet();
		fieldSetQnA.add(securityQuestionsWidget.getRulesGroup());
		fieldSetQnA.add(securityQuestionsWidget.getQuestionAns1FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getAns1FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getQuestionAns2FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getAns2FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getQuestionAns3FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getAns3FormGroup());
		formSecurityQuestionAnswer.add(fieldSetQnA);

		bluePanel.add(formSecurityQuestionAnswer);
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
	public MessageAlert getPasswordErrorMessageDisplay() {
		return pwdErrorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getSecurityErrorMessageDisplay()
	 */
	@Override
	public MessageAlert getSecurityErrorMessageDisplay() {
		return secErrorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getPassword()
	 */
	@Override
	public Input getPassword() {
		return changePasswordWidget.getPassword();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getConfirmPassword()
	 */
	@Override
	public Input getConfirmPassword() {
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
	public SecurityQuestionAnswerWidget getSecurityQuestionsWidget() {
		return securityQuestionsWidget;
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getAnswerText1()
	 */
	@Override
	public String getAnswerText1() {
		return securityQuestionsWidget.getAnswer1().getValue();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getAnswerText2()
	 */
	@Override
	public String getAnswerText2() {
		return securityQuestionsWidget.getAnswer2().getValue();
		}

	/* (non-Javadoc)
	 * @see mat.client.login.FirstLoginPresenter.Display#getAnswerText3()
	 */
	@Override
	public String getAnswerText3() {
		return securityQuestionsWidget.getAnswer3().getValue();
	}

	@Override
	public MessageAlert getSuccessMessageDisplay() {
		return null;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return null;
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return null;
	}

	@Override
	public Map<String, String> getSecurityAnswers() {
		return null;
	}
	
}
