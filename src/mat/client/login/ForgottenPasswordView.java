package mat.client.login;

import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MessageAlert;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;

import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.InputType;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ForgottenPasswordView.
 */
public class ForgottenPasswordView implements ForgottenPasswordPresenter.Display {
	
	/** The main panel. */
	private Panel mainPanel;
	
	/** The login id. */
	private TextBox loginId = new TextBox();
	
	/** The security question. */
	private Label securityQuestion;
	
	/** The security answer. */
	private Input securityAnswer;
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("forgotPwd");
	
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The security question ans panel. */
	private VerticalPanel securityQuestionAnsPanel = new  VerticalPanel();
	
	/** The security answer hidden. */
	//Hidden securityAnswerHidden = new Hidden();
	
	/** The is user id submit. */
	public static boolean isUserIdSubmit = true;
	
	
	/**
	 * Instantiates a new forgotten password view.
	 */
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
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.getElement().setId("horizontalPanel_HorizontalPanel");
		Label userIdLabel = (Label) LabelBuilder.buildLabel(loginId, "User ID");
		horizontalPanel.add(userIdLabel);
		HTML required = new HTML(RequiredIndicator.get());
		horizontalPanel.add(required);
		bluePanel.add(horizontalPanel);
		bluePanel.add(new SpacerWidget());
		
		/*loginId = new TextBox();*/
		loginId.setTitle("Enter User ID");
		loginId.setPlaceholder("Enter User ID");
		loginId.setEnabled(true);
		loginId.setWidth("170px");
		//loginId.setHeight("15px");
		
		bluePanel.add(loginId);
		bluePanel.add(new SpacerWidget());
		
		bluePanel.add(securityQuestionAnsPanel);
		bluePanel.add(new SpacerWidget());
		
		buttonBar.getSaveButton().setText("Submit");
		bluePanel.add(buttonBar);
		
		mainPanel.add(bluePanel);
		
	}	


	/* (non-Javadoc)
	 * @see mat.client.login.ForgottenPasswordPresenter.Display#getSecurityQuestion()
	 */
	@Override
	public String getSecurityQuestion() {
		return securityQuestion.getText();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.ForgottenPasswordPresenter.Display#getSecurityAnswer()
	 */
	@Override
	public String getSecurityAnswer() {
		return securityAnswer.getValue();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.ForgottenPasswordPresenter.Display#getSubmit()
	 */
	@Override
	public HasClickHandlers getSubmit() {
		return buttonBar.getSaveButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.login.ForgottenPasswordPresenter.Display#getReset()
	 */
	@Override
	public HasClickHandlers getReset() {
		return buttonBar.getCancelButton();
	}


	/* (non-Javadoc)
	 * @see mat.client.login.ForgottenPasswordPresenter.Display#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.login.ForgottenPasswordPresenter.Display#addSecurityQuestionOptions(java.lang.String)
	 */
	@Override
	public void addSecurityQuestionOptions(String text) {
		securityQuestion.setText(text);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.ForgottenPasswordPresenter.Display#getErrorMessageDisplay()
	 */
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}

	/* (non-Javadoc)
	 * @see mat.client.login.ForgottenPasswordPresenter.Display#setFocus(boolean)
	 */
	@Override
	public void setFocus(boolean focus) {
		securityAnswer.setFocus(focus);
	}

	/* (non-Javadoc)
	 * @see mat.client.login.ForgottenPasswordPresenter.Display#setSecurityQuestionAnswerEnabled(boolean)
	 */
	@Override
	public void setSecurityQuestionAnswerEnabled(boolean enable) {
		securityQuestionAnsPanel.clear();
		if(enable){
			isUserIdSubmit = false;
			securityQuestion = new Label();
			Label label = (Label)LabelBuilder.buildLabel(securityQuestion, "Security Question:");
			securityQuestionAnsPanel.add(label);
			securityQuestionAnsPanel.add(securityQuestion);
			securityQuestionAnsPanel.add(new SpacerWidget());
			
			securityAnswer = new Input(InputType.PASSWORD);
			securityAnswer.setTitle("Enter Security Question Answer");
			securityQuestionAnsPanel.add(LabelBuilder.buildLabel(securityAnswer, "Security Question Answer"));
			securityQuestionAnsPanel.add(securityAnswer);
			//securityQuestionAnsPanel.add(securityAnswerHidden);
			securityQuestionAnsPanel.add(new SpacerWidget());
			
			
			Element element1 = securityAnswer.getElement();
		    element1.setAttribute("aria-role", "command");
			element1.setAttribute("aria-labelledby", "LiveRegion");
			element1.setAttribute("aria-live", "assertive");
			element1.setAttribute("aria-atomic", "true");
			element1.setAttribute("aria-relevant", "all");
			element1.setAttribute("role", "alert");						
			
			Element element = securityQuestion.getElement();
		    element.setAttribute("aria-role", "command");
			element.setAttribute("aria-labelledby", "LiveRegion");
			element.setAttribute("aria-live", "assertive");
			element.setAttribute("aria-atomic", "true");
			element.setAttribute("aria-relevant", "all");
			element.setAttribute("role", "alert");
			
			setFocus(true);
			
//			securityAnswer.addBlurHandler(new BlurHandler() {
//				@Override
//				public void onBlur(BlurEvent event) {
//					String ans = securityAnswer.getText();
//					securityAnswerHidden.setValue(ans);
//					String asterisks = "";
//					for (int i = 0; i < ans.length(); i++) {
//						asterisks += "*";
//					}
//					securityAnswer.setText(asterisks);
//				}
//			});
			
			securityAnswer.addFocusHandler(new FocusHandler() {
				@Override
				public void onFocus(FocusEvent event) {
					securityAnswer.setText("");
				}
			});
		}
	}

	/* (non-Javadoc)
	 * @see mat.client.login.ForgottenPasswordPresenter.Display#getLoginId()
	 */
	@Override
	public TextBox getLoginId() {
		return loginId;
	}
	
}
