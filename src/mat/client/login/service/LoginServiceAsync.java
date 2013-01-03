package mat.client.login.service;


import java.util.List;

import mat.client.login.LoginModel;
import mat.model.UserSecurityQuestion;
import mat.shared.ForgottenPasswordResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync extends AsynchronousService{

	void isValidUser(String username, String password,
			AsyncCallback<LoginModel> callback);
	void forgotPassword(String email, 
			String securityQuestion, String securityAnswer,
			AsyncCallback<ForgottenPasswordResult> callback);
	void signOut(AsyncCallback<Void> callback);
	void changePasswordSecurityAnswers(LoginModel model,
			AsyncCallback<LoginResult> callback);
	void  changeTempPassword(String email, String password, AsyncCallback<LoginModel> callback);
	void getSecurityQuestionOptions(String userid, AsyncCallback<SecurityQuestionOptions> callback);
	void getSecurityQuestionsAnswers(String userid,
			AsyncCallback<List<UserSecurityQuestion>> callback);
	void getFooterURLs(AsyncCallback<List<String>> callback);
	
}
