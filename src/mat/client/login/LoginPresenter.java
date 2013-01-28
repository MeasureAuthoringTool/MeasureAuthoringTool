package mat.client.login;

import mat.client.Login;
import mat.client.event.FirstLoginPageEvent;
import mat.client.event.ForgottenPasswordEvent;
import mat.client.event.SuccessfulLoginEvent;
import mat.client.event.TemporaryPasswordLoginEvent;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;

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



public class LoginPresenter {

	public static interface Display {
		public HasClickHandlers getSubmit();
		public HasValue<String> getUserid();
		public HasValue<String> getPassword();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public HasHTML getInfoMessage();
		public void setWelcomeVisible(boolean value);
		public void setInfoMessageVisible(boolean value);
		public HasClickHandlers getForgotPassword();
		public Widget asWidget();

		public HasKeyDownHandlers getUseridField();
		public HasKeyDownHandlers getPasswordField();
		public void setInitialFocus();
	}
	private final Display display;
	private LoginModel loginModel;

	private KeyDownHandler submitOnEnterHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				submit();
			}
		}
	};

	private  final AsyncCallback<LoginModel> contextcallback = new AsyncCallback<LoginModel>(){

		@Override
		public void onFailure(Throwable cause) {
			cause.printStackTrace();
//			display.getErrorMessageDisplay().setMessage(cause.getMessage());
			display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
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

		}


	};

	public LoginPresenter(Display displayArg) {
		this.display = displayArg;
		this.loginModel = new LoginModel();
		display.getSubmit().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				submit();
			}


		});
		display.getForgotPassword().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				reset();
				MatContext.get().getEventBus().fireEvent(new ForgottenPasswordEvent());
			}
		});
		display.getUseridField().addKeyDownHandler(submitOnEnterHandler);
		display.getPasswordField().addKeyDownHandler(submitOnEnterHandler);
	}

	private void submit() {
		display.getErrorMessageDisplay().clear();
		display.setInfoMessageVisible(false);
		if(display.getUserid().getValue().isEmpty()) {
			display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getLoginIDRequiredMessage());
		}else if(display.getPassword().getValue().isEmpty()) {
			display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getPasswordRequiredMessage());
		}else{
			MatContext.get().isValidUser(display.getUserid().getValue(),display.getPassword().getValue(), contextcallback);
		}
	}

	public void go(HasWidgets container) {
		reset();
		container.add(display.asWidget());
		displayWelcomeMessage();
		display.setInitialFocus();
		Login.hideLoadingMessage();

	}
	private void displayWelcomeMessage() {
		display.setWelcomeVisible(true);
		display.setInfoMessageVisible(false);
	}

	public void displayForgottenPasswordMessage() {
		display.setWelcomeVisible(false);
		display.setInfoMessageVisible(true);
		display.getInfoMessage().setHTML("A temporary password has been sent to the e-mail address associated with the User Id you provided. Please<br> check your e-mail and continue to sign in.");
	}

	public void reset() {
		display.getErrorMessageDisplay().clear();
		display.setInfoMessageVisible(false);
		display.getPassword().setValue("");
		display.getUserid().setValue("");
	}
}
