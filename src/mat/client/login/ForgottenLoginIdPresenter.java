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

/**
 * The Class ForgottenLoginIdPresenter.
 */
public class ForgottenLoginIdPresenter {
	
	/**
	 * The Interface Display.
	 */
	public static interface Display {
		
		/**
		 * Gets the email.
		 * 
		 * @return the email
		 */
		public HasValue<String> getEmail();
		
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
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
	}

	/** The display. */
	private final Display display;
	
	
	/**
	 * Instantiates a new forgotten login id presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
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
	
	/**
	 * Reset.
	 */
	private void reset() {
		display.getEmail().setValue("");
		display.getErrorMessageDisplay().clear();
	}
	
	/**
	 * Request forgotten login id.
	 */
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
			case ForgottenLoginIDResult.SECURITY_QUESTION_MISMATCH:
				message = MatContext.get().getMessageDelegate().getSecurityQMismatchMessage();
				break;
			case ForgottenLoginIDResult.EMAIL_NOT_FOUND_MSG:
				message = MatContext.get().getMessageDelegate().getEmailNotFoundMessage();
				break;
			case ForgottenLoginIDResult.USER_ALREADY_LOGGED_IN:
				message = MatContext.get().getMessageDelegate().getLoginFailedAlreadyLoggedInMessage();
				break;
			case ForgottenLoginIDResult.EMAIL_INVALID:
				message = "Invalid Email Address.";
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
	}
}
