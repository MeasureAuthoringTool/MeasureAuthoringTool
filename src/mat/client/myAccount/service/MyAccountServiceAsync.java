package mat.client.myAccount.service;

import mat.client.myAccount.MyAccountModel;
import mat.client.myAccount.SecurityQuestionsModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface MyAccountServiceAsync {
	void getMyAccount(AsyncCallback<MyAccountModel> callback)
			throws IllegalArgumentException;
	void saveMyAccount(MyAccountModel model, AsyncCallback<Void> callback);
	
	void getSecurityQuestions(AsyncCallback<SecurityQuestionsModel> callback);
	void saveSecurityQuestions(SecurityQuestionsModel model, AsyncCallback<Void> callback);
	
	void changePassword(String password, AsyncCallback<String> asyncCallback);
	public void setUserSignInDate(String userid, AsyncCallback<Void> callback);
	public void setUserSignOutDate(String userid, AsyncCallback<Void> asyncCallback);
}
