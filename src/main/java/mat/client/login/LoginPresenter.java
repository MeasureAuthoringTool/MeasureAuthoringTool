package mat.client.login;

import com.google.gwt.user.client.Window;
import mat.client.Login;
import mat.client.event.FirstLoginPageEvent;
import mat.client.event.ForgotLoginIDEvent;
import mat.client.event.ForgottenPasswordEvent;
import mat.client.event.SuccessfulLoginEvent;
import mat.client.event.TemporaryPasswordLoginEvent;
import mat.client.shared.MatContext;

import mat.client.util.ClientConstants;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class LoginPresenter {
	
	private LoginView view;
	private LoginModel loginModel;
	
	interface LoginViewDisplay {
		
		Widget asWidget();
		
		Anchor getSignInWithHarp();

		void setSignInWithHarp(Anchor signInWithHarp);
		
		Anchor getForgotLoginId();
		
		void setForgotLoginId(Anchor forgotLoginId);
		
		Anchor getForgotPassword();
		
		void setForgotPassword(Anchor forgotPassword);
		
		Input getUserIdText();
		
		void setUserIdText(Input userIdText);
		
		Input getPasswordInput();
		
		void setPasswordInput(Input passwordInput);
		
		Button getSubmitButton();
		
		TextBox getSecurityCodeInput();
		
		FormGroup getPasswordGroup();
		
		FormGroup getAuthTokenGroup();
		
		FormGroup getUserIdGroup();
		
		FormGroup getMessageFormGrp();
		
		HelpBlock getHelpBlock();
		
		
		
		Heading getWelcomeHeading();
		
		
		
		Panel getSuccessMessagePanel();
		
		
		
		PanelBody getSuccessMessageBody();
		
	}
	
	private KeyDownHandler submitOnEnterHandler = event -> {
		if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
			submit();
		}
	};
	
	public LoginPresenter(LoginView loginView) {
		view = loginView;
		
		loginModel = new LoginModel();

		view.getSignInWithHarp().addClickHandler(clickEvent -> {
			Window.Location.replace("HarpLogin.html");
		});

		view.getSubmitButton().addClickHandler(event -> submit());

		view.getForgotPassword().addClickHandler(event -> {
			reset();
			MatContext.get().getEventBus().fireEvent(new ForgottenPasswordEvent());
		});
		
		view.getForgotLoginId().addClickHandler(event -> {
			reset();
			MatContext.get().getEventBus().fireEvent(new ForgotLoginIDEvent());
		});
		view.getUserIdText().addKeyDownHandler(submitOnEnterHandler);
		view.getPasswordInput().addKeyDownHandler(submitOnEnterHandler);
		view.getSecurityCodeInput().addKeyDownHandler(submitOnEnterHandler);
	}
	
	public void go(HasWidgets container) {
		reset();
		container.add(view.asWidget());
		view.getSuccessMessagePanel().setVisible(false);
		view.getWelcomeHeading().setVisible(true);
		Login.hideLoadingMessage();
		
	}
	
	public void reset() {
		view.getHelpBlock().setText("");
		view.getUserIdText().setText("");
		view.getPasswordInput().setText("");
		view.getSecurityCodeInput().setText("");
		view.getUserIdGroup().setValidationState(ValidationState.NONE);
		view.getPasswordGroup().setValidationState(ValidationState.NONE);
		view.getAuthTokenGroup().setValidationState(ValidationState.NONE);
	}

	private void submit() {
		view.getSuccessMessagePanel().setVisible(false);
		view.getHelpBlock().setText("");
		view.getSubmitButton().setEnabled(false);
		
		if (view.getUserIdText().getText().isEmpty()) {
			view.getUserIdGroup().setValidationState(ValidationState.ERROR);
			view.getHelpBlock().setIconType(IconType.EXCLAMATION_CIRCLE);
			view.getHelpBlock().setText(MatContext.get().getMessageDelegate().getLoginIDRequiredMessage());
			view.getMessageFormGrp().setValidationState(ValidationState.ERROR);
			view.getSubmitButton().setEnabled(true);
		} else if (view.getPasswordInput().getText().isEmpty()) {
			view.getHelpBlock().setIconType(IconType.EXCLAMATION_CIRCLE);
			view.getHelpBlock().setText(MatContext.get().getMessageDelegate().getPasswordRequiredMessage());
			view.getMessageFormGrp().setValidationState(ValidationState.ERROR);
			view.getUserIdGroup().setValidationState(ValidationState.SUCCESS);
			view.getPasswordGroup().setValidationState(ValidationState.ERROR);
			view.getSubmitButton().setEnabled(true);
		} else if (view.getSecurityCodeInput().getText().isEmpty()) {
			view.getHelpBlock().setIconType(IconType.EXCLAMATION_CIRCLE);
			view.getHelpBlock().setText(MatContext.get().getMessageDelegate().getSecurityCodeRequiredMessage());
			view.getMessageFormGrp().setValidationState(ValidationState.ERROR);
			view.getUserIdGroup().setValidationState(ValidationState.SUCCESS);
			view.getPasswordGroup().setValidationState(ValidationState.SUCCESS);
			view.getAuthTokenGroup().setValidationState(ValidationState.ERROR);
			view.getSubmitButton().setEnabled(true);
		} else {
			view.getUserIdGroup().setValidationState(ValidationState.SUCCESS);
			view.getPasswordGroup().setValidationState(ValidationState.SUCCESS);
			view.getAuthTokenGroup().setValidationState(ValidationState.SUCCESS);

			MatContext.get().isValidUser(view.getUserIdText().getText(), view.getPasswordInput().getText(),
					view.getSecurityCodeInput().getText(), contextcallback);
		}
	}
	private  final AsyncCallback<LoginModel> contextcallback = new AsyncCallback<LoginModel>(){
		
		@Override
		public void onFailure(Throwable cause) {
			cause.printStackTrace();
			view.getHelpBlock().setText(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			view.getUserIdGroup().setValidationState(ValidationState.NONE);
			view.getPasswordGroup().setValidationState(ValidationState.NONE);
			view.getAuthTokenGroup().setValidationState(ValidationState.NONE);
			view.getSubmitButton().setEnabled(true);
		}
		
		@Override
		public void onSuccess(LoginModel result) {
			view.getUserIdGroup().setValidationState(ValidationState.NONE);
			view.getPasswordGroup().setValidationState(ValidationState.NONE);
			view.getAuthTokenGroup().setValidationState(ValidationState.NONE);
			loginModel = result;
			if (loginModel != null) {				
				if (loginModel.isLoginFailedEvent()) {
					view.getHelpBlock().setIconType(IconType.EXCLAMATION_CIRCLE);
					view.getMessageFormGrp().setValidationState(ValidationState.ERROR);
					view.getHelpBlock().setText(loginModel.getErrorMessage());
				} else {
					String secRole = null;
					if (loginModel.getRole() != null) {
						secRole = loginModel.getRole().getDescription();
					}
					MatContext.get().setUserInfo(loginModel.getUserId(), loginModel.getEmail(), secRole, loginModel.getLoginId(), loginModel.getUserPreference());
					if (loginModel.isInitialPassword()) {
						MatContext.get().getEventBus().fireEvent(new FirstLoginPageEvent());
					} else if (loginModel.isTemporaryPassword()) {
						MatContext.get().getEventBus().fireEvent(new TemporaryPasswordLoginEvent());
					} else {
						MatContext.get().getEventBus().fireEvent(new SuccessfulLoginEvent());
					}
				}
			} else {
				view.getHelpBlock().setText(MatContext.get().getMessageDelegate().getServerCallNullMessage());
			}
			view.getSubmitButton().setEnabled(true);
		}
	};
	
	public void displayForgottenPasswordMessage() {
		view.getWelcomeHeading().setVisible(false);
		HTML html = new HTML("A temporary password has been sent to the e-mail address associated with the User Id you provided. Please check your e-mail and continue to sign in.");
		view.getSuccessMessageBody().clear();
		view.getSuccessMessageBody().add(html);
		view.getSuccessMessagePanel().setVisible(true);
	}
	
	/**
	 * Display forgotten login id message.
	 */
	public void displayForgottenLoginIDMessage() {
		view.getWelcomeHeading().setVisible(false);
		HTML html = new HTML("Measure Authoring Tool just sent your User ID to the e-mail address you provided. Please check your e-mail and continue to sign in. ");
		view.getSuccessMessageBody().clear();
		view.getSuccessMessageBody().add(html);
		view.getSuccessMessagePanel().setVisible(true);
	}
}
