package org.ifmc.mat.client.login.service;


import org.ifmc.mat.client.login.LoginModel;
import org.ifmc.mat.shared.ForgottenPasswordResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync extends AsynchronousService{

	void isValidUser(String username, String password,
			AsyncCallback<LoginModel> callback);
	void forgotPassword(String email, 
			String securityQuestion, String securityAnswer,
			AsyncCallback<ForgottenPasswordResult> callback);
	void signOut(AsyncCallback<Void> callback);
	void changePasswordSecurityAnswers(LoginModel model, AsyncCallback<Void> callback);
	void  changeTempPassword(String email, String password, AsyncCallback<LoginModel> callback);
	void getSecurityQuestionOptions(String userid, AsyncCallback<SecurityQuestionOptions> callback);
}
