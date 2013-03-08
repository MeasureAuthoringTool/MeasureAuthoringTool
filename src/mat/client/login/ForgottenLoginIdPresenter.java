package mat.client.login;

import java.util.Collections;
import java.util.List;

import mat.client.event.ForgotLoginIDEmailSentEvent;
import mat.client.event.PasswordEmailSentEvent;
import mat.client.event.ReturnToLoginEvent;
import mat.client.login.service.SecurityQuestionOptions;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.NameValuePair;
import mat.shared.ForgottenLoginIDResult;
import mat.shared.ForgottenPasswordResult;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ForgottenLoginIdPresenter {

	public static interface Display {
		public HasValue<String> getEmail();
		public HasValue<String> getSecurityQuestion();
		public HasValue<String> getSecurityAnswer();
		
		public HasClickHandlers getSubmit();
		public HasClickHandlers getReset();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public void setSecurityQuestionAnswerEnabled(boolean enabled);
		public void addSecurityQuestionOptions(List<NameValuePair> texts);
		public void setFocus(boolean focus);
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
				requestForgottenLoginID();
			}
		});
		
		display.getEmail().addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				display.setFocus(true);
				display.setSecurityQuestionAnswerEnabled(false);
				display.getErrorMessageDisplay().clear();
				loadSecurityQuestionsForUserId(arg0.getValue());
			}
		});
		
		display.setSecurityQuestionAnswerEnabled(false);
	}
	
	private void reset() {
		display.getEmail().setValue("");
		display.getSecurityAnswer().setValue("");
		display.getErrorMessageDisplay().clear();
	}
	private void loadSecurityQuestionsForUserId(String email) {
		MatContext.get().getLoginService().getSecurityQuestionOptionsForEmail(email, new AsyncCallback<SecurityQuestionOptions>() {

			@Override
			public void onFailure(Throwable exc) {
				display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(SecurityQuestionOptions options) {
				if(options.isUserFound() && options.getSecurityQuestions().size() > 0) {
					display.setSecurityQuestionAnswerEnabled(true);
					display.setFocus(true);//This line is needed to focus the dropdown when the security Questions dynamically changed.
					List<NameValuePair> qs = options.getSecurityQuestions();
					Collections.sort(qs, new NameValuePair.Comparator());
					display.addSecurityQuestionOptions(qs);
				}
				else {
					display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getEmailNotFoundMessage());
				}
			}
			
		});
	}
	private void requestForgottenLoginID() {
		MatContext.get().getLoginService().forgotLoginID(display.getEmail().getValue(), display.getSecurityQuestion().getValue(),
				display.getSecurityAnswer().getValue(), new AsyncCallback<ForgottenLoginIDResult>(){

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
			default: message = MatContext.get().getMessageDelegate().getUnknownFailMessage();
		}
		return message;
	}
	public void go(HasWidgets container) {
		reset();
		MatContext.get().getSecurityQuestions(new AsyncCallback<List<NameValuePair>>() {
			public void onSuccess(List<NameValuePair> values) {
				display.addSecurityQuestionOptions(values);
			}
			public void onFailure(Throwable t) {
				//Window.alert(t.getMessage());
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
		
		container.add(display.asWidget());
	}
}
