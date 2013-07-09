package mat.server.service;

import mat.client.login.LoginModel;

import org.springframework.security.core.userdetails.UserDetails;

public interface LoginCredentialService {
	public LoginModel isValidUser(String userId,String password);
	public void signOut();
	public LoginModel changeTempPassword(String userid, String changedpassword);
	/*public ForgottenPasswordResult forgotPassword(String userid, String email, 
			String securityQuestion, String securityAnswer);*/
	public boolean changePasswordSecurityAnswers(LoginModel model);
	public UserDetails loadUserByUsername(String userId); 
		}
