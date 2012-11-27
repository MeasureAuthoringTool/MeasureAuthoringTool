package org.ifmc.mat.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ifmc.mat.client.login.LoginModel;
import org.ifmc.mat.client.login.service.LoginService;
import org.ifmc.mat.client.login.service.SecurityQuestionOptions;
import org.ifmc.mat.server.service.LoginCredentialService;
import org.ifmc.mat.server.service.UserService;
import org.ifmc.mat.shared.ForgottenPasswordResult;

@SuppressWarnings("serial")
public class LoginServiceImpl extends SpringRemoteServiceServlet implements LoginService{
	private static final Log logger = LogFactory.getLog(LoginServiceImpl.class);
	
	
	private LoginCredentialService getLoginCredentialService(){
		return (LoginCredentialService)context.getBean("loginService");
	}
	
	

	@Override
	public SecurityQuestionOptions getSecurityQuestionOptions(String userid) {
		UserService userService = (UserService)context.getBean("userService");
		return  userService.getSecurityQuestionOptions(userid);
	}



	@Override
	public LoginModel  isValidUser(String userId, String password) {
		LoginModel loginModel = getLoginCredentialService().isValidUser(userId, password);
		return loginModel;
	}
	
	public ForgottenPasswordResult forgotPassword(String email, 
			String securityQuestion, String securityAnswer) {

		UserService userService = (UserService)context.getBean("userService");
		return userService.requestForgottenPassword(email, securityQuestion, securityAnswer);
	}

	@Override
	public void signOut() {
		 getLoginCredentialService().signOut();
	}

	@Override
	public void changePasswordSecurityAnswers(LoginModel model) {
		getLoginCredentialService().changePasswordSecurityAnswers(model);
	}

	@Override
	public LoginModel changeTempPassword(String email, String changedpassword) {
		return getLoginCredentialService().changeTempPassword(email, changedpassword);
	}

	
}



