package mat.client.login;

import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;

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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ForgottenPasswordView implements ForgottenPasswordPresenter.Display {
	private Panel mainPanel;
	private TextBox loginId;
	private Label securityQuestion;
	private TextBox securityAnswer;
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private VerticalPanel securityQuestionAnsPanel = new  VerticalPanel();
	Hidden securityAnswerHidden = new Hidden();
	public static boolean isUserIdSubmit = true;
	
	
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
		
		loginId = new TextBox();
		loginId.setTitle("Enter User ID");
		loginId.setEnabled(true);
		loginId.setWidth("170px");
		loginId.setHeight("15px");
		
		bluePanel.add(loginId);
		bluePanel.add(new SpacerWidget());
		
		bluePanel.add(securityQuestionAnsPanel);
		bluePanel.add(new SpacerWidget());
		
		buttonBar.getSaveButton().setText("Submit");
		bluePanel.add(buttonBar);
		
		mainPanel.add(bluePanel);
		
	}	


	@Override
	public String getSecurityQuestion() {
		return securityQuestion.getText();
	}

	@Override
	public String getSecurityAnswer() {
		return securityAnswerHidden.getValue();
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
	public void addSecurityQuestionOptions(String text) {
		securityQuestion.setText(text);
	}
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public void setFocus(boolean focus) {
		securityAnswer.setFocus(focus);
	}

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
			
			securityAnswer = new TextBox();
			securityAnswer.setTitle("Enter Security Question Answer");
			securityQuestionAnsPanel.add(LabelBuilder.buildLabel(securityAnswer, "Security Question Answer"));
			securityQuestionAnsPanel.add(securityAnswer);
			securityQuestionAnsPanel.add(securityAnswerHidden);
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
			
			securityAnswer.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					String ans = securityAnswer.getText();
					securityAnswerHidden.setValue(ans);
					String asterisks = "";
					for (int i = 0; i < ans.length(); i++) {
						asterisks += "*";
					}
					securityAnswer.setText(asterisks);
				}
			});
			
			securityAnswer.addFocusHandler(new FocusHandler() {
				@Override
				public void onFocus(FocusEvent event) {
					securityAnswer.setText("");
				}
			});
		}
	}

	@Override
	public TextBox getLoginId() {
		return loginId;
	}
	
}
