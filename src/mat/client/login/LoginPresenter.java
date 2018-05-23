package mat.client.login;

import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Login;
import mat.client.event.FirstLoginPageEvent;
import mat.client.event.ForgotLoginIDEvent;
import mat.client.event.ForgottenPasswordEvent;
import mat.client.event.SuccessfulLoginEvent;
import mat.client.event.TemporaryPasswordLoginEvent;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;



/**
 * The Class LoginPresenter.
 */
public class LoginPresenter {
	
	/**
	 * The Interface Display.
	 */
	public static interface Display {
		
		/**
		 * Gets the submit.
		 * 
		 * @return the submit
		 */
		public HasClickHandlers getSubmit();
		
		/**
		 * Gets the userid.
		 * 
		 * @return the userid
		 */
		public HasValue<String> getUserid();
		
		/**
		 * Gets the password.
		 * 
		 * @return the password
		 */
		public HasValue<String> getPassword();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the info message.
		 * 
		 * @return the info message
		 */
		public HasHTML getInfoMessage();
		
		/**
		 * Sets the welcome visible.
		 * 
		 * @param value
		 *            the new welcome visible
		 */
		public void setWelcomeVisible(boolean value);
		
		/**
		 * Sets the info message visible.
		 * 
		 * @param value
		 *            the new info message visible
		 */
		public void setInfoMessageVisible(boolean value);
		
		/**
		 * Gets the forgot password.
		 * 
		 * @return the forgot password
		 */
		public HasClickHandlers getForgotPassword();
		
		/**
		 * Gets the forgot login id.
		 * 
		 * @return the forgot login id
		 */
		public HasClickHandlers getForgotLoginId();
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Gets the userid field.
		 * 
		 * @return the userid field
		 */
		public HasKeyDownHandlers getUseridField();
		
		/**
		 * Gets the password field.
		 * 
		 * @return the password field
		 */
		public HasKeyDownHandlers getPasswordField();
		
		/**
		 * Sets the initial focus.
		 */
		public void setInitialFocus();
		
		HasValue<String> getOneTimePassword();
		
		Button getSubmitButton();
	}
	
	/** The display. */
	private final Display display;
	
	/** The login model. */
	private LoginModel loginModel;
	
	/** The submit on enter handler. */
	private KeyDownHandler submitOnEnterHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				submit();
			}
		}
	};
	
	/** The contextcallback. */
	private  final AsyncCallback<LoginModel> contextcallback = new AsyncCallback<LoginModel>(){
		
		@Override
		public void onFailure(Throwable cause) {
			cause.printStackTrace();
			//			display.getErrorMessageDisplay().setMessage(cause.getMessage());
			display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			display.getSubmitButton().setEnabled(true);
		}
		
		@Override
		public void onSuccess(LoginModel result) {
			loginModel = result;
			if(result != null) {
				String secRole = null;
				if(result.getRole() != null) {
					secRole = result.getRole().getDescription();
				}
				MatContext.get().setUserInfo(result.getUserId(), result.getEmail(), secRole,result.getLoginId());
				if(loginModel.isInitialPassword()){
					MatContext.get().getEventBus().fireEvent(new FirstLoginPageEvent());
				}else if(loginModel.isLoginFailedEvent()){
					display.getErrorMessageDisplay().setMessage(loginModel.getErrorMessage());
				}else if(loginModel.isTemporaryPassword()){
					MatContext.get().getEventBus().fireEvent(new TemporaryPasswordLoginEvent());
				}else{
					MatContext.get().getEventBus().fireEvent(new SuccessfulLoginEvent());
				}
			}
			else {
				display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getServerCallNullMessage());
			}
			display.getSubmitButton().setEnabled(true);
			
		}
		
		
	};
	
	/**
	 * Instantiates a new login presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
	public LoginPresenter(Display displayArg) {
		display = displayArg;
		loginModel = new LoginModel();
		display.getSubmit().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				submit();
			}
			
			
		});
		display.getForgotPassword().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				reset();
				MatContext.get().getEventBus().fireEvent(new ForgottenPasswordEvent());
			}
		});
		
		display.getForgotLoginId().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				reset();
				MatContext.get().getEventBus().fireEvent(new ForgotLoginIDEvent());
			}
		});
		display.getUseridField().addKeyDownHandler(submitOnEnterHandler);
		display.getPasswordField().addKeyDownHandler(submitOnEnterHandler);
	}
	
	/**
	 * Submit.
	 */
	private void submit() {
		display.getErrorMessageDisplay().clear();
		display.setInfoMessageVisible(false);
		
		display.getSubmitButton().setEnabled(false);
		if(display.getUserid().getValue().isEmpty()) {
			display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getLoginIDRequiredMessage());
			display.getSubmitButton().setEnabled(true);
		}else if(display.getPassword().getValue().isEmpty()) {
			display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getPasswordRequiredMessage());
			display.getSubmitButton().setEnabled(true);
		}else if(display.getOneTimePassword().getValue().isEmpty()) {
			display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getSecurityCodeRequiredMessage());
			display.getSubmitButton().setEnabled(true);
		}else{
			MatContext.get().isValidUser(display.getUserid().getValue(),display.getPassword().getValue(), display.getOneTimePassword().getValue(), contextcallback);
		}
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
		displayWelcomeMessage();
		display.setInitialFocus();
		Login.hideLoadingMessage();
		
	}
	
	/**
	 * Display welcome message.
	 */
	private void displayWelcomeMessage() {
		display.setWelcomeVisible(true);
		display.setInfoMessageVisible(false);
	}
	
	/**
	 * Display forgotten password message.
	 */
	public void displayForgottenPasswordMessage() {
		display.setWelcomeVisible(false);
		display.setInfoMessageVisible(true);
		display.getInfoMessage().setHTML("A temporary password has been sent to the e-mail address associated with the User Id you provided. Please<br> check your e-mail and continue to sign in.");
	}
	
	/**
	 * Display forgotten login id message.
	 */
	public void displayForgottenLoginIDMessage() {
		display.setWelcomeVisible(false);
		display.setInfoMessageVisible(true);
		display.getInfoMessage().setHTML("Measure Authoring Tool just sent your User ID to the e-mail address you provided. Please<br> check your e-mail and continue to sign in. ");
	}
	
	/**
	 * Reset.
	 */
	public void reset() {
		display.getErrorMessageDisplay().clear();
		display.setInfoMessageVisible(false);
		display.getPassword().setValue("");
		display.getUserid().setValue("");
		display.getOneTimePassword().setValue("");
	}
}
