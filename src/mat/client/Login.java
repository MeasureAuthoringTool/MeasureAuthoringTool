package mat.client;

import mat.client.event.BackToLoginPageEvent;
import mat.client.event.FirstLoginPageEvent;
import mat.client.event.ForgotLoginIDEmailSentEvent;
import mat.client.event.ForgotLoginIDEvent;
import mat.client.event.ForgottenPasswordEvent;
import mat.client.event.LogoffEvent;
import mat.client.event.PasswordEmailSentEvent;
import mat.client.event.ReturnToLoginEvent;
import mat.client.event.SuccessfulLoginEvent;
import mat.client.event.TemporaryPasswordLoginEvent;
import mat.client.login.FirstLoginPresenter;
import mat.client.login.FirstLoginView;
import mat.client.login.ForgottenLoginIdNewPresenter;
import mat.client.login.ForgottenLoginIdNewView;
import mat.client.login.ForgottenPasswordPresenter;
import mat.client.login.ForgottenPasswordView;
import mat.client.login.LoginNewPresenter;
import mat.client.login.LoginNewView;
import mat.client.login.TempPwdLoginPresenter;
import mat.client.login.TempPwdView;
import mat.client.shared.MatContext;
import mat.client.util.ClientConstants;
import mat.shared.ConstantMessages;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;

/**
 * The Class Login.
 */
public class Login extends MainLayout implements EntryPoint {
	
	/** The content. */
	private Panel content;
	
	/** The forgotten login id presenter. */
	//private ForgottenLoginIdPresenter forgottenLoginIdPresenter;
	private ForgottenLoginIdNewPresenter forgottenLoginIdNewPresenter;
	/** The forgotten pwd presenter. */
	private ForgottenPasswordPresenter forgottenPwdPresenter;
	
	/** The login presenter. */
	//private LoginPresenter loginPresenter;
	
	private LoginNewPresenter loginNewPresenter;
	
	/** The security questions presenter. */
	private FirstLoginPresenter securityQuestionsPresenter;
	
	/** The temp pwd loging presenter. */
	private TempPwdLoginPresenter tempPwdLogingPresenter;
	
	//US 439.  Signing out and redirects to Login.html
	/**
	 * Call sign out.
	 */
	private void callSignOut(){
		MatContext.get().setUMLSLoggedIn(false);
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
	
	/* (non-Javadoc)
	 * @see mat.client.MainLayout#initEntryPoint()
	 */
	@Override
	protected void initEntryPoint() {
		MatContext.get().setCurrentModule(ConstantMessages.LOGIN_MODULE);
		showLoadingMessage();
		content = getContentPanel();
		initPresenters();
		loginNewPresenter.go(content);
		//loginPresenter.go(content);
		MatContext.get().getEventBus().addHandler(PasswordEmailSentEvent.TYPE, new PasswordEmailSentEvent.Handler() {
			
			@Override
			public void onPasswordEmailSent(final PasswordEmailSentEvent event) {
				content.clear();
				/*loginPresenter.go(content);
				loginPresenter.displayForgottenPasswordMessage();
				 */
				loginNewPresenter.go(content);
				loginNewPresenter.displayForgottenPasswordMessage();
			}
		});
		
		MatContext.get().getEventBus().addHandler(ForgotLoginIDEmailSentEvent.TYPE, new ForgotLoginIDEmailSentEvent.Handler() {
			
			@Override
			public void onForgotLoginIdEmailSent(final ForgotLoginIDEmailSentEvent event) {
				content.clear();
				/*loginPresenter.go(content);
				loginPresenter.displayForgottenLoginIDMessage();
				 */loginNewPresenter.go(content);
				 loginNewPresenter.displayForgottenLoginIDMessage();
			}
		});
		MatContext.get().getEventBus().addHandler(ForgottenPasswordEvent.TYPE, new ForgottenPasswordEvent.Handler() {
			
			@Override
			public void onForgottenPassword(final ForgottenPasswordEvent event) {
				content.clear();
				forgottenPwdPresenter.go(content);
			}
		});
		
		// Forgot LoginId
		MatContext.get().getEventBus().addHandler(ForgotLoginIDEvent.TYPE, new ForgotLoginIDEvent.Handler() {
			
			@Override
			public void onForgottenLoginID(final ForgotLoginIDEvent event) {
				content.clear();
				//forgottenLoginIdPresenter.go(content);
				forgottenLoginIdNewPresenter.go(content);
			}
		});
		
		MatContext.get().getEventBus().addHandler(SuccessfulLoginEvent.TYPE, new SuccessfulLoginEvent.Handler() {
			
			@Override
			public void onSuccessfulLogin(final SuccessfulLoginEvent event) {
				//				MatContext.get().openNewHtmlPage("/Mat.html");
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_MAT);
			}
		});
		
		MatContext.get().getEventBus().addHandler(ReturnToLoginEvent.TYPE, new ReturnToLoginEvent.Handler() {
			
			@Override
			public void onReturnToLogin(final ReturnToLoginEvent event) {
				content.clear();
				//loginPresenter.go(content);
				loginNewPresenter.go(content);
			}
		});
		
		MatContext.get().getEventBus().addHandler(BackToLoginPageEvent.TYPE, new BackToLoginPageEvent.Handler() {
			
			@Override
			public void onLoginFailure(final BackToLoginPageEvent event) {
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
			}
		});
		
		MatContext.get().getEventBus().addHandler(FirstLoginPageEvent.TYPE, new FirstLoginPageEvent.Handler() {
			
			@Override
			public void onFirstLogin(final FirstLoginPageEvent event) {
				content.clear();
				securityQuestionsPresenter.go(content);
			}
		});
		
		MatContext.get().getEventBus().addHandler(TemporaryPasswordLoginEvent.TYPE, new TemporaryPasswordLoginEvent.Handler() {
			
			@Override
			public void onTempPasswordLogin(final TemporaryPasswordLoginEvent event) {
				content.clear();
				tempPwdLogingPresenter.go(content);
			}
			
			
		});
		
		//US 439. Call signout when logoff event fired.
		MatContext.get().getEventBus().addHandler(LogoffEvent.TYPE, new LogoffEvent.Handler() {
			
			@Override
			public void onLogoff(final LogoffEvent event) {
				callSignOut();
			}
		});
		
		
	}
	
	/**
	 * Inits the presenters.
	 */
	private void initPresenters() {
		/*final LoginView unamePasswordView = new LoginView();
		loginPresenter = new LoginPresenter(unamePasswordView);*/
		LoginNewView loginView = new LoginNewView();
		loginNewPresenter = new LoginNewPresenter(loginView);
		
		
		final FirstLoginView securityQuesView = new FirstLoginView();
		securityQuestionsPresenter = new FirstLoginPresenter(securityQuesView);
		
		final ForgottenPasswordView forgottenPwdView = new ForgottenPasswordView();
		forgottenPwdPresenter = new ForgottenPasswordPresenter(forgottenPwdView);
		
		/*final ForgottenLoginIdView forgottenLoginIdView = new ForgottenLoginIdView();
		forgottenLoginIdPresenter = new ForgottenLoginIdPresenter(forgottenLoginIdView);
		 */
		ForgottenLoginIdNewView forgottenLoginIdNewView = new ForgottenLoginIdNewView();
		forgottenLoginIdNewPresenter = new ForgottenLoginIdNewPresenter(forgottenLoginIdNewView);
		final TempPwdView temPwdview = new TempPwdView();
		tempPwdLogingPresenter = new TempPwdLoginPresenter(temPwdview);
		
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
	
}
