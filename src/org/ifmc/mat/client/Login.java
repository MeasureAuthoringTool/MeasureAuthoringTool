package org.ifmc.mat.client;

import org.ifmc.mat.client.event.BackToLoginPageEvent;
import org.ifmc.mat.client.event.FirstLoginPageEvent;
import org.ifmc.mat.client.event.ForgottenPasswordEvent;
import org.ifmc.mat.client.event.LogoffEvent;
import org.ifmc.mat.client.event.PasswordEmailSentEvent;
import org.ifmc.mat.client.event.ReturnToLoginEvent;
import org.ifmc.mat.client.event.SuccessfulLoginEvent;
import org.ifmc.mat.client.event.TemporaryPasswordLoginEvent;
import org.ifmc.mat.client.login.FirstLoginPresenter;
import org.ifmc.mat.client.login.FirstLoginView;
import org.ifmc.mat.client.login.ForgottenPasswordPresenter;
import org.ifmc.mat.client.login.ForgottenPasswordView;
import org.ifmc.mat.client.login.LoginPresenter;
import org.ifmc.mat.client.login.LoginView;
import org.ifmc.mat.client.login.TempPwdLoginPresenter;
import org.ifmc.mat.client.login.TempPwdView;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.shared.ConstantMessages;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;

public class Login extends MainLayout implements EntryPoint {

	private Panel content;
	private LoginPresenter loginPresenter;
	private ForgottenPasswordPresenter forgottenPasswordPresenter;
	private FirstLoginPresenter securityQuestionsPresenter;
	private TempPwdLoginPresenter tempPwdLogingPresenter;

	protected void initEntryPoint() {
		MatContext.get().setCurrentModule(ConstantMessages.LOGIN_MODULE);
		showLoadingMessage();
		content = getContentPanel();
		initPresenters();
		loginPresenter.go(content);
		
		MatContext.get().getEventBus().addHandler(PasswordEmailSentEvent.TYPE, new PasswordEmailSentEvent.Handler() {
			
			public void onPasswordEmailSent(PasswordEmailSentEvent event) {
				content.clear();
				loginPresenter.go(content);
				loginPresenter.displayForgottenPasswordMessage();
			}
		});
		MatContext.get().getEventBus().addHandler(ForgottenPasswordEvent.TYPE, new ForgottenPasswordEvent.Handler() {
			
			public void onForgottenPassword(ForgottenPasswordEvent event) {
				content.clear();
				forgottenPasswordPresenter.go(content);
			}
		});
		
		MatContext.get().getEventBus().addHandler(SuccessfulLoginEvent.TYPE, new SuccessfulLoginEvent.Handler() {
			
			public void onSuccessfulLogin(SuccessfulLoginEvent event) {
//				MatContext.get().openNewHtmlPage("/Mat.html");
				MatContext.get().redirectToHtmlPage("/Mat.html");
			}
		});
		
		MatContext.get().getEventBus().addHandler(ReturnToLoginEvent.TYPE, new ReturnToLoginEvent.Handler() {
			
			public void onReturnToLogin(ReturnToLoginEvent event) {
				content.clear();
				loginPresenter.go(content);
			}
		});
		
		MatContext.get().getEventBus().addHandler(BackToLoginPageEvent.TYPE, new BackToLoginPageEvent.Handler() {
			
			public void onLoginFailure(BackToLoginPageEvent event) {
				MatContext.get().redirectToHtmlPage("/Login.html");
			}
		});
        
		MatContext.get().getEventBus().addHandler(FirstLoginPageEvent.TYPE, new FirstLoginPageEvent.Handler() {
			
			@Override
			public void onFirstLogin(FirstLoginPageEvent event) {
				content.clear();
				securityQuestionsPresenter.go(content);
			}
		});
		
		MatContext.get().getEventBus().addHandler(TemporaryPasswordLoginEvent.TYPE, new TemporaryPasswordLoginEvent.Handler() {

			@Override
			public void onTempPasswordLogin(TemporaryPasswordLoginEvent event) {
				content.clear();
				tempPwdLogingPresenter.go(content);
			}
			
			
		});
		
		//US 439. Call signout when logoff event fired. 
		MatContext.get().getEventBus().addHandler(LogoffEvent.TYPE, new LogoffEvent.Handler() {

			@Override
			public void onLogoff(LogoffEvent event) {
	    		 callSignOut();
			}
		});


	}
	
	//US 439.  Signing out and redirects to Login.html
	private void callSignOut(){
		 MatContext.get().getLoginService().signOut(new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable arg0) {
					redirectToLogin();
				}

				@Override
				public void onSuccess(Void arg0) {
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
		Timer t = new Timer() {
			@Override
			public void run() {
				MatContext.get().redirectToHtmlPage("/Login.html");
			}
		};
		t.schedule(1000);		
	}


	
	private void initPresenters() {
		LoginView usernamePasswordView = new LoginView();
		loginPresenter = new LoginPresenter(usernamePasswordView);
		
		FirstLoginView securityQuestionsView = new FirstLoginView();
		securityQuestionsPresenter = new FirstLoginPresenter(securityQuestionsView);
		
		ForgottenPasswordView forgottenPasswordView = new ForgottenPasswordView();
		forgottenPasswordPresenter = new ForgottenPasswordPresenter(forgottenPasswordView);
		
		TempPwdView temPwdview = new TempPwdView();
		tempPwdLogingPresenter = new TempPwdLoginPresenter(temPwdview);
		
	}
	
}
