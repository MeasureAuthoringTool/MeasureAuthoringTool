package mat.client.login;

import mat.client.event.PasswordEmailSentEvent;
import mat.client.event.ReturnToLoginEvent;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.shared.ForgottenPasswordResult;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ForgottenPasswordPresenter {

	public static interface Display {
		public TextBox getLoginId();
		public String getSecurityQuestion();
		public String getSecurityAnswer();
		public HasClickHandlers getSubmit();
		public HasClickHandlers getReset();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public void setSecurityQuestionAnswerEnabled(boolean show);
		public void addSecurityQuestionOptions(String text);
		public void setFocus(boolean focus);
		public Widget asWidget();
	}

	private final Display display;
	
	private int invalidUserCounter = 0;
	
	public ForgottenPasswordPresenter(Display displayArg) {
		this.display = displayArg;
		
		display.getReset().addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				reset();
				MatContext.get().getEventBus().fireEvent(new ReturnToLoginEvent());	
			}
		});
		
		display.getSubmit().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(ForgottenPasswordView.isUserIdSubmit){
					display.getErrorMessageDisplay().clear();
					if(null == display.getLoginId().getValue() || display.getLoginId().getValue().trim().isEmpty()){
						display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getLoginIDRequiredMessage());
						return;
					}
					display.getLoginId().setEnabled(false);
					loadSecurityQuestionForUserId(display.getLoginId().getText());		
				}else{
					if(invalidUserCounter >= 3){
						String message = convertMessage(ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED);
						display.getErrorMessageDisplay().setMessage(message);
						return;
					}
					requestForgottenPassword();
				}
				
			}
		});
		
		display.setSecurityQuestionAnswerEnabled(false);
	}
	
	private void reset() {
		display.getLoginId().setEnabled(true);
		display.getLoginId().setValue("");
		display.setSecurityQuestionAnswerEnabled(false);
		display.getErrorMessageDisplay().clear();
		display.getLoginId().setFocus(true);
		invalidUserCounter = 0;
		ForgottenPasswordView.isUserIdSubmit = true;
	}
	
	private void loadSecurityQuestionForUserId(String userid) {
		MatContext.get().getLoginService().getSecurityQuestion(userid, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable exc) {
				display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(String question) {
				if(null == question){
					String[] questions = MatContext.get().questions;
					int i = (int) (Math.random() * questions.length);
					question = questions[i];
				}
				display.setSecurityQuestionAnswerEnabled(true);
				display.addSecurityQuestionOptions(question);
			}
		});
	}
	
	private void requestForgottenPassword() {
		MatContext.get().getLoginService().forgotPassword(display.getLoginId().getValue(), 
				display.getSecurityQuestion(), 
				display.getSecurityAnswer(), 
				new AsyncCallback<ForgottenPasswordResult>() {
					
					@Override
					public void onSuccess(ForgottenPasswordResult result) {
						if(result.isEmailSent()) {
							MatContext.get().getEventBus().fireEvent(new PasswordEmailSentEvent());	
						}else if(ForgottenPasswordResult.USER_NOT_FOUND == result.getFailureReason()){
							invalidUserCounter += 1;
							String message = "";
							if(invalidUserCounter == 2){
								message = convertMessage(ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED_SECOND_ATTEMPT);
							}else if(invalidUserCounter >= 3){
								message = convertMessage(ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED);
							}else{
								message = convertMessage(ForgottenPasswordResult.SECURITY_QUESTION_MISMATCH);
							}
							display.getErrorMessageDisplay().setMessage(message);
							
						}else{
							String message = convertMessage(result.getFailureReason());
							display.getErrorMessageDisplay().setMessage(message);
						}
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
					}
				});
	}
	private String convertMessage(int id) {
		String message;
		switch(id) {
			case ForgottenPasswordResult.EMAIL_MISMATCH:
				message = MatContext.get().getMessageDelegate().getEmailMismatchMessage();
				break;
			case ForgottenPasswordResult.SECURITY_QUESTIONS_NOT_SET:
				message = MatContext.get().getMessageDelegate().getSecurityNotAnsweredMessage();
				break;
			case ForgottenPasswordResult.SECURITY_QUESTION_MISMATCH:
				message = MatContext.get().getMessageDelegate().getSecurityQMismatchMessage();
				break;
			case ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED:
				message = MatContext.get().getMessageDelegate().getAccountLockedMessage();
				break;
			case ForgottenPasswordResult.USER_NOT_FOUND:
				message = MatContext.get().getMessageDelegate().getUserNotFoundMessage();
				break;
			case ForgottenPasswordResult.USER_ALREADY_LOGGED_IN:
				message = MatContext.get().getMessageDelegate().getLoginFailedAlreadyLoggedInMessage();
				break;
			case ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED_SECOND_ATTEMPT:
				message = MatContext.get().getMessageDelegate().getSecondAttemptFailedMessage();
				break;
			default: message = MatContext.get().getMessageDelegate().getUnknownFailMessage();
		}
		return message;
	}
	public void go(HasWidgets container) {
		reset();
		container.add(display.asWidget());
		display.getLoginId().setFocus(true);
	}
}
