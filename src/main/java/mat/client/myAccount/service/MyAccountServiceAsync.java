package mat.client.myAccount.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.client.myAccount.MyAccountModel;
import mat.client.myAccount.SecurityQuestionsModel;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface MyAccountServiceAsync {
	
	/**
	 * Gets the my account.
	 * 
	 * @param callback
	 *            the callback
	 * @return the my account
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
	void getMyAccount(AsyncCallback<MyAccountModel> callback)
			throws IllegalArgumentException;
	
	/**
	 * Save my account.
	 * 
	 * @param model
	 *            the model
	 * @param callback
	 *            the callback
	 */
	void saveMyAccount(MyAccountModel model, AsyncCallback<SaveMyAccountResult> callback);
	
	/**
	 * Gets the security questions.
	 * 
	 * @param callback
	 *            the callback
	 * @return the security questions
	 */
	void getSecurityQuestions(AsyncCallback<SecurityQuestionsModel> callback);
	
	/**
	 * Save security questions.
	 * 
	 * @param model
	 *            the model
	 * @param callback
	 *            the callback
	 */
	void saveSecurityQuestions(SecurityQuestionsModel model,
			AsyncCallback<SaveMyAccountResult> callback);
	
	/**
	 * Sets the user sign in date.
	 * 
	 * @param userid
	 *            the userid
	 * @param callback
	 *            the callback
	 */
	public void setUserSignInDate(String userid, AsyncCallback<Void> callback);
	
	/**
	 * Sets the user sign out date.
	 * 
	 * @param userid
	 *            the userid
	 * @param asyncCallback
	 *            the async callback
	 */
	public void setUserSignOutDate(String userid, AsyncCallback<Void> asyncCallback);
	
	/**
	 * Change password.
	 * 
	 * @param password
	 *            the password
	 * @param asyncCallback
	 *            the async callback
	 */
	void changePassword(String password,
			AsyncCallback<SaveMyAccountResult> asyncCallback);
}
