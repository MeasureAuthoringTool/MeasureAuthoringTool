package mat.client.myAccount.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.client.myAccount.MyAccountModel;
import mat.client.myAccount.SecurityQuestionsModel;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("myAccount")
public interface MyAccountService extends RemoteService {
	
	/**
	 * Gets the my account.
	 * 
	 * @return the my account
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
	MyAccountModel getMyAccount() throws IllegalArgumentException;
	
	/**
	 * Save my account.
	 * 
	 * @param model
	 *            the model
	 * @return the save my account result
	 */
	SaveMyAccountResult saveMyAccount(MyAccountModel model);
	
	/**
	 * Gets the security questions.
	 * 
	 * @return the security questions
	 */
	SecurityQuestionsModel getSecurityQuestions();
	
	/**
	 * Save security questions.
	 * 
	 * @param model
	 *            the model
	 * @return the save my account result
	 */
	SaveMyAccountResult saveSecurityQuestions(SecurityQuestionsModel model);
	
	/**
	 * Change password.
	 * 
	 * @param password
	 *            the password
	 * @return the save my account result
	 */
	SaveMyAccountResult changePassword(String password);
	//US212
	/**
	 * Sets the user sign in date.
	 * 
	 * @param userid
	 *            the new user sign in date
	 */
	public void setUserSignInDate(String userid);
	
	/**
	 * Sets the user sign out date.
	 * 
	 * @param userid
	 *            the new user sign out date
	 */
	public void setUserSignOutDate(String userid);
	
}
