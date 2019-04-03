package mat.client.login;

import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import mat.client.event.PasswordEmailSentEvent;
import mat.client.event.ReturnToLoginEvent;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.shared.ForgottenPasswordResult;

public class ForgottenPasswordPresenter {

	public static interface Display {

		public TextBox getLoginId();

		public String getSecurityQuestion();

		public String getSecurityAnswer();

		public HasClickHandlers getSubmit();

		public HasClickHandlers getReset();

		public MessageAlert getErrorMessageDisplay();

		public void setSecurityQuestionAnswerEnabled(boolean show);

		public void addSecurityQuestionOptions(String text);

		public void setFocus(boolean focus);

		public Widget asWidget();
	}

	private final Display display;

	public ForgottenPasswordPresenter(Display displayArg) {
		this.display = displayArg;

		display.getReset().addClickHandler(event -> onCancel());

		display.getSubmit().addClickHandler(event -> onForgotSubmit()); 

		display.setSecurityQuestionAnswerEnabled(false);
	}

	private void onForgotSubmit() {
		if(ForgottenPasswordView.isUserIdSubmit){
			display.getErrorMessageDisplay().clearAlert();
			if(null == display.getLoginId().getValue() || display.getLoginId().getValue().trim().isEmpty()){
				display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getLoginIDRequiredMessage());
				return;
			}
			display.getLoginId().setEnabled(false);
			loadSecurityQuestionForUserId(display.getLoginId().getText());		
		}
		else{
			forgotSecurityAnswer();
		}
	}

	private void forgotSecurityAnswer() {
		String answer = display.getSecurityAnswer();
		if (answer == null || answer.isEmpty()) {
			display.getErrorMessageDisplay().createAlert("Security Question Answer is required.");
		} else {
			requestForgottenPassword(); 
		}
	}

	private void onCancel() {
		reset();
		MatContext.get().getEventBus().fireEvent(new ReturnToLoginEvent());	
	}

	private void reset() {
		display.getLoginId().setEnabled(true);
		display.getLoginId().setValue("");
		display.setSecurityQuestionAnswerEnabled(false);
		display.getErrorMessageDisplay().clearAlert();
		display.getLoginId().setFocus(true); 
		ForgottenPasswordView.isUserIdSubmit = true;
	}

	private void loadSecurityQuestionForUserId(final String userid) {

		MatContext.get().getLoginService().isLockedUser(userid, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {}

			@Override
			public void onSuccess(Boolean isLocked) {
				MatContext.get().getLoginService().getSecurityQuestion(userid, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable exc) {
						display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}

					@Override
					public void onSuccess(String question) {							
						display.setSecurityQuestionAnswerEnabled(true);
						display.addSecurityQuestionOptions(question);
					}
				});

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
				}else{
					String message = convertMessage(result.getFailureReason());
					display.getErrorMessageDisplay().createAlert(message);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
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
		case ForgottenPasswordResult.USER_NOT_FOUND:
			message = MatContext.get().getMessageDelegate().getUserNotFoundMessage();
			break;
		case ForgottenPasswordResult.USER_ALREADY_LOGGED_IN:
			message = MatContext.get().getMessageDelegate().getLoginFailedAlreadyLoggedInMessage();
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
