package mat.client.myAccount.service;

import mat.client.myAccount.MyAccountModel;
import mat.client.myAccount.SecurityQuestionsModel;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("myAccount")
public interface MyAccountService extends RemoteService {
	MyAccountModel getMyAccount() throws IllegalArgumentException;
	void saveMyAccount(MyAccountModel model);
	
	SecurityQuestionsModel getSecurityQuestions();
	void saveSecurityQuestions(SecurityQuestionsModel model);
	
	String changePassword(String password);
	//US212
	public void setUserSignInDate(String userid);
	public void setUserSignOutDate(String userid);
	
}
