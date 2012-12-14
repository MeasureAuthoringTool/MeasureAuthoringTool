package mat.client;

import mat.client.event.BackToLoginPageEvent;
import mat.client.event.ChangePaswwordSecurityQnsLoginPageEvent;
import mat.client.event.ForgottenPasswordEvent;
import mat.client.event.LogoffEvent;
import mat.client.event.PasswordEmailSentEvent;
import mat.client.event.ReturnToLoginEvent;
import mat.client.event.SuccessfulLoginEvent;

import mat.client.login.ChangePaswwordSecurityQnsLoginPresenter;
import mat.client.login.ChangePaswwordSecurityQnsLoginView;

import mat.client.login.ForgottenPasswordPresenter;
import mat.client.login.ForgottenPasswordView;
import mat.client.login.LoginPresenter;
import mat.client.login.LoginView;
//import mat.client.login.TempPwdLoginPresenter;
//import mat.client.login.TempPwdView;
import mat.client.shared.MatContext;
import mat.client.util.ClientConstants;
import mat.shared.ConstantMessages;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;

public class Login extends MainLayout implements EntryPoint {

	private Panel content;
	private LoginPresenter loginPresenter;
	private ForgottenPasswordPresenter forgottenPwdPresenter;
	private ChangePaswwordSecurityQnsLoginPresenter securityQuestionsPresenter;
	//private TempPwdLoginPresenter tempPwdLogingPresenter;

	protected void initEntryPoint() {
		MatContext.get().setCurrentModule(ConstantMessages.LOGIN_MODULE);
		showLoadingMessage();
		content = getContentPanel();
		initPresenters();
		loginPresenter.go(content);
		
		MatContext.get().getEventBus().addHandler(PasswordEmailSentEvent.TYPE, new PasswordEmailSentEvent.Handler() {
			
			public void onPasswordEmailSent(final PasswordEmailSentEvent event) {
				content.clear();
				loginPresenter.go(content);
				loginPresenter.displayForgottenPasswordMessage();
			}
		});
		MatContext.get().getEventBus().addHandler(ForgottenPasswordEvent.TYPE, new ForgottenPasswordEvent.Handler() {
			
			public void onForgottenPassword(final ForgottenPasswordEvent event) {
				content.clear();
				forgottenPwdPresenter.go(content);
			}
		});
		
		MatContext.get().getEventBus().addHandler(SuccessfulLoginEvent.TYPE, new SuccessfulLoginEvent.Handler() {
			
			public void onSuccessfulLogin(final SuccessfulLoginEvent event) {
//				MatContext.get().openNewHtmlPage("/Mat.html");
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_MAT);
			}
		});
		
		MatContext.get().getEventBus().addHandler(ReturnToLoginEvent.TYPE, new ReturnToLoginEvent.Handler() {
			
			public void onReturnToLogin(final ReturnToLoginEvent event) {
				content.clear();
				loginPresenter.go(content);
			}
		});
		
		MatContext.get().getEventBus().addHandler(BackToLoginPageEvent.TYPE, new BackToLoginPageEvent.Handler() {
			
			public void onLoginFailure(final BackToLoginPageEvent event) {
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
			}
		});
        
		MatContext.get().getEventBus().addHandler(ChangePaswwordSecurityQnsLoginPageEvent.TYPE, new ChangePaswwordSecurityQnsLoginPageEvent.Handler() {
			
			@Override
			public void onFirstLogin(final ChangePaswwordSecurityQnsLoginPageEvent event) {
				content.clear();
				securityQuestionsPresenter.go(content);
			}
		});
		
		/*MatContext.get().getEventBus().addHandler(TemporaryPasswordLoginEvent.TYPE, new TemporaryPasswordLoginEvent.Handler() {

			@Override
			public void onTempPasswordLogin(final TemporaryPasswordLoginEvent event) {
				content.clear();
				tempPwdLogingPresenter.go(content);
			}
			
			
		});*/
		
		//US 439. Call signout when logoff event fired. 
		MatContext.get().getEventBus().addHandler(LogoffEvent.TYPE, new LogoffEvent.Handler() {

			@Override
			public void onLogoff(final LogoffEvent event) {
	    		 callSignOut();
			}
		});


	}
	
	//US 439.  Signing out and redirects to Login.html
	private void callSignOut(){
		 MatContext.get().getLoginService().signOut(new AsyncCallback<Void>() {

				@Override
				public void onFailure(final Throwable arg0) {
					redirectToLogin();
				}

				@Override
				public void onSuccess(final Void arg0) {
					redirectToLogin();
				}
			});
	}
	
	/**
	 * Redirects to the Login.html
	 */
	private void redirectToLogin() {
		/*
		 * Added a timer to have a delay before redirect since 
		 * this was causing the firefox javascript exception.
		 */
		final Timer timer = new Timer() {
			@Override
			public void run() {
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
			}
		};
		timer.schedule(1000);		
	}


	
	private void initPresenters() {
		final LoginView unamePasswordView = new LoginView();
		loginPresenter = new LoginPresenter(unamePasswordView);
		
		final ChangePaswwordSecurityQnsLoginView securityQuesView = new ChangePaswwordSecurityQnsLoginView();
		securityQuestionsPresenter = new ChangePaswwordSecurityQnsLoginPresenter(securityQuesView);
		
		final ForgottenPasswordView forgottenPwdView = new ForgottenPasswordView();
		forgottenPwdPresenter = new ForgottenPasswordPresenter(forgottenPwdView);
		
		//final TempPwdView temPwdview = new TempPwdView();
		//tempPwdLogingPresenter = new TempPwdLoginPresenter(temPwdview);
		
	}
	
}
