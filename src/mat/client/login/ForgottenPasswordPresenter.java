package mat.client.login;

import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import mat.client.event.PasswordEmailSentEvent;
import mat.client.event.ReturnToLoginEvent;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.shared.ForgottenPasswordResult;

/**
 * The Class ForgottenPasswordPresenter.
 */
public class ForgottenPasswordPresenter {

	/**
	 * The Interface Display.
	 */
	public static interface Display {
		
		/**
		 * Gets the login id.
		 * 
		 * @return the login id
		 */
		public TextBox getLoginId();
		
		/**
		 * Gets the security question.
		 * 
		 * @return the security question
		 */
		public String getSecurityQuestion();
		
		/**
		 * Gets the security answer.
		 * 
		 * @return the security answer
		 */
		public String getSecurityAnswer();
		
		/**
		 * Gets the submit.
		 * 
		 * @return the submit
		 */
		public HasClickHandlers getSubmit();
		
		/**
		 * Gets the reset.
		 * 
		 * @return the reset
		 */
		public HasClickHandlers getReset();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public MessageAlert getErrorMessageDisplay();
		
		/**
		 * Sets the security question answer enabled.
		 * 
		 * @param show
		 *            the new security question answer enabled
		 */
		public void setSecurityQuestionAnswerEnabled(boolean show);
		
		/**
		 * Adds the security question options.
		 * 
		 * @param text
		 *            the text
		 */
		public void addSecurityQuestionOptions(String text);
		
		/**
		 * Sets the focus.
		 * 
		 * @param focus
		 *            the new focus
		 */
		public void setFocus(boolean focus);
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
	}

	/** The display. */
	private final Display display;
	
	/** The invalid user counter. */
	private int invalidUserCounter = 0;
	
	/**
	 * Instantiates a new forgotten password presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
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
					display.getErrorMessageDisplay().clearAlert();
					if(null == display.getLoginId().getValue() || display.getLoginId().getValue().trim().isEmpty()){
						display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getLoginIDRequiredMessage());
						return;
					}
					display.getLoginId().setEnabled(false);
					loadSecurityQuestionForUserId(display.getLoginId().getText());		
				}
				else{
					if(invalidUserCounter >= 3){
						//String message = convertMessage(ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED);
						//display.getErrorMessageDisplay().setMessage(message);
						//return;
					}
					requestForgottenPassword(); 
				} 
				
			}
		});
		
		display.setSecurityQuestionAnswerEnabled(false);
	}
	
	/**
	 * Reset.
	 */
	private void reset() {
		display.getLoginId().setEnabled(true);
		display.getLoginId().setValue("");
		display.setSecurityQuestionAnswerEnabled(false);
		display.getErrorMessageDisplay().clearAlert();
		display.getLoginId().setFocus(true); 
		invalidUserCounter = 0;
		ForgottenPasswordView.isUserIdSubmit = true;
	}
	
	/**
	 * Load security question for user id.
	 * 
	 * @param userid
	 *            the userid
	 */
	private void loadSecurityQuestionForUserId(final String userid) {
		
		MatContext.get().getLoginService().isLockedUser(userid, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {}

			@Override
			public void onSuccess(Boolean isLocked) {
				//if(!isLocked){
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
				//} else{
				//	display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getAccountLockedMessage());
				//}
				
			}
		
		});
		
		
	}
	
	/**
	 * Request forgotten password.
	 */
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
						//invalidUserCounter = result.getCounter();
						//MatContext.get().recordUserEvent(display.getLoginId().getValue(), "Forgot Password", "", false);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
					}
				});
	}

	/**
	 * Convert message.
	 * 
	 * @param id
	 *            the id
	 * @return the string
	 */
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
	
	/**
	 * Go.
	 * 
	 * @param container
	 *            the container
	 */
	public void go(HasWidgets container) {
		reset();
		container.add(display.asWidget());
		display.getLoginId().setFocus(true);
	}
}
