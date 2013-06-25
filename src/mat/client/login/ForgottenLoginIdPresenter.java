package mat.client.login;

import mat.client.event.ForgotLoginIDEmailSentEvent;
import mat.client.event.ReturnToLoginEvent;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.shared.ForgottenLoginIDResult;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ForgottenLoginIdPresenter {
	
	public static interface Display {
		public HasValue<String> getEmail();
		
		public HasClickHandlers getSubmit();
		public HasClickHandlers getReset();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public Widget asWidget();
	}

	private final Display display;
	
	
	public ForgottenLoginIdPresenter(Display displayArg) {
		this.display = displayArg;
		
		display.getReset().addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				reset();
				MatContext.get().getEventBus().fireEvent(new ReturnToLoginEvent());	
			}
		});
		
		display.getSubmit().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				display.getErrorMessageDisplay().clear();
				requestForgottenLoginID();
			}
		});
	}
	
	private void reset() {
		display.getEmail().setValue("");
		display.getErrorMessageDisplay().clear();
	}
	
	private void requestForgottenLoginID() {
		MatContext.get().getLoginService().forgotLoginID(display.getEmail().getValue(), new AsyncCallback<ForgottenLoginIDResult>(){
					@Override
					public void onFailure(Throwable caught) {
						display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
						
					}

					@Override
					public void onSuccess(ForgottenLoginIDResult result) {
						if(result.isEmailSent()) {
							MatContext.get().getEventBus().fireEvent(new ForgotLoginIDEmailSentEvent());	
						}
						else {
							String message = convertMessage(result.getFailureReason());
							display.getErrorMessageDisplay().setMessage(message);
						}
						
					}});
		}
	private String convertMessage(int id) {
		String message;
		switch(id) {
			case ForgottenLoginIDResult.SECURITY_QUESTIONS_NOT_SET:
				message = MatContext.get().getMessageDelegate().getSecurityNotAnsweredMessage();
				break;
			case ForgottenLoginIDResult.SECURITY_QUESTION_MISMATCH:
				message = MatContext.get().getMessageDelegate().getSecurityQMismatchMessage();
				break;
			case ForgottenLoginIDResult.SECURITY_QUESTIONS_LOCKED:
				message = MatContext.get().getMessageDelegate().getAccountLockedMessage();
				break;
			case ForgottenLoginIDResult.EMAIL_NOT_FOUND_MSG:
				message = MatContext.get().getMessageDelegate().getEmailNotFoundMessage();
				break;
			case ForgottenLoginIDResult.USER_ALREADY_LOGGED_IN:
				message = MatContext.get().getMessageDelegate().getLoginFailedAlreadyLoggedInMessage();
				break;
			case ForgottenLoginIDResult.SECURITY_QUESTIONS_LOCKED_SECOND_ATTEMPT:
				message = MatContext.get().getMessageDelegate().getSecondAttemptFailedMessage();
				break;
			case ForgottenLoginIDResult.EMAIL_INVALID:
				message = "Invalid Email Address.";
				break;
			default: message = MatContext.get().getMessageDelegate().getUnknownFailMessage();
		}
		return message;
	}
	
	
	
	public void go(HasWidgets container) {
		reset();
		container.add(display.asWidget());
	}
}
