package mat.server.service;

import mat.client.login.LoginModel;
import mat.client.login.service.LoginResult;
import mat.shared.ForgottenPasswordResult;

public interface LoginCredentialService {
	public LoginModel isValidUser(String userId,String password);
	public void signOut();
	public LoginModel changeTempPassword(String userid, String changedpassword);
	public ForgottenPasswordResult forgotPassword(String userid, String email, 
			String securityQuestion, String securityAnswer);
	public boolean changePasswordSecurityAnswers(LoginModel model);
}
