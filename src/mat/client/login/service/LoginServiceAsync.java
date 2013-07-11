package mat.client.login.service;


import java.util.HashMap;
import java.util.List;

import mat.client.login.LoginModel;
import mat.model.SecurityQuestions;
import mat.model.UserSecurityQuestion;
import mat.shared.ForgottenLoginIDResult;
import mat.shared.ForgottenPasswordResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync extends AsynchronousService{

	void isValidUser(String username, String password,
			AsyncCallback<LoginModel> callback);
	void forgotPassword(String email, 
			String securityQuestion, String securityAnswer, int invalidUserCounter, 
			AsyncCallback<ForgottenPasswordResult> callback);
	void signOut(AsyncCallback<Void> callback);
	void changePasswordSecurityAnswers(LoginModel model,
			AsyncCallback<LoginResult> callback);
	void  changeTempPassword(String email, String password, AsyncCallback<LoginModel> callback);
	void getSecurityQuestionOptions(String userid, AsyncCallback<SecurityQuestionOptions> callback);
	void getSecurityQuestionsAnswers(String userid,
			AsyncCallback<List<UserSecurityQuestion>> callback);
	void getFooterURLs(AsyncCallback<List<String>> callback);
	void updateOnSignOut(String userId, String email, String activityType,
			AsyncCallback<String> callback);
	void getSecurityQuestionOptionsForEmail(String email,
			AsyncCallback<SecurityQuestionOptions> callback);
	void forgotLoginID(String email,
			AsyncCallback<ForgottenLoginIDResult> callback);
	void getSecurityQuestion(String userid, AsyncCallback<String> callback);
	void validatePassword(String userID, String enteredPassword,
			AsyncCallback<HashMap<String,String>> callback);
	void isLockedUser(String loginId, AsyncCallback<Boolean> callback);
	void getSecurityQuestions(AsyncCallback<List<SecurityQuestions>> callback);
	
}
