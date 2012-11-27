package org.ifmc.mat.client.login.service;


import org.ifmc.mat.client.login.LoginModel;
import org.ifmc.mat.shared.ForgottenPasswordResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService {
	
	public LoginModel  isValidUser(String username, String password); 
	public ForgottenPasswordResult forgotPassword(String email, 
			String securityQuestion, String securityAnswer);
	public void changePasswordSecurityAnswers(LoginModel model);
	public void signOut();
	public LoginModel changeTempPassword(String email, String password);
	
	public SecurityQuestionOptions getSecurityQuestionOptions(String userid);
	
}
