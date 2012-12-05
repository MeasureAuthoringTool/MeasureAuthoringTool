package mat.server;

import java.io.FileNotFoundException;
import java.io.IOException;

import mat.client.login.LoginModel;
import mat.client.login.service.LoginService;
import mat.client.login.service.SecurityQuestionOptions;
import mat.client.shared.MatContext;
import mat.server.service.LoginCredentialService;
import mat.server.service.UserService;
import mat.server.util.dictionary.CheckDictionaryWordInPassword;
import mat.shared.ForgottenPasswordResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("serial")
public class LoginServiceImpl extends SpringRemoteServiceServlet implements LoginService{
	private static final Log logger = LogFactory.getLog(LoginServiceImpl.class);
	
	
	private LoginCredentialService getLoginCredentialService(){
		return (LoginCredentialService)context.getBean("loginService");
	}
	
	

	@Override
	public SecurityQuestionOptions getSecurityQuestionOptions(String userid) {
		UserService userService = (UserService)context.getBean("userService");
		return  userService.getSecurityQuestionOptions(userid);
	}



	@Override
	public LoginModel  isValidUser(String userId, String password) {
		LoginModel loginModel = getLoginCredentialService().isValidUser(userId, password);
		return loginModel;
	}
	
	public ForgottenPasswordResult forgotPassword(String email, 
			String securityQuestion, String securityAnswer) {

		UserService userService = (UserService)context.getBean("userService");
		return userService.requestForgottenPassword(email, securityQuestion, securityAnswer);
	}

	@Override
	public void signOut() {
		 getLoginCredentialService().signOut();
	}

	@Override
	public Boolean changePasswordSecurityAnswers(LoginModel model) {
		boolean result =false;
		LoginModel loginModel = model;
		try {
			result = CheckDictionaryWordInPassword.containsDictionaryWords(loginModel.getPassword());
		} catch (FileNotFoundException e) {
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			e.printStackTrace();
		} catch (IOException e) {
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			e.printStackTrace();
		}
		if(result){
			getLoginCredentialService().changePasswordSecurityAnswers(model);
			result=true;
		}else{
			result=false;
		}
		return result;
		
	}

	@Override
	public LoginModel changeTempPassword(String email, String changedpassword) {
		boolean result = false;
		LoginModel loginModel = new LoginModel();
		try {
			result = CheckDictionaryWordInPassword.containsDictionaryWords(changedpassword);
		} catch (FileNotFoundException e) {
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			e.printStackTrace();
		} catch (IOException e) {
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			e.printStackTrace();
		}
		if(result){
			loginModel= getLoginCredentialService().changeTempPassword(email, changedpassword);
		}else{
			
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getMustNotContainDictionaryWordMessage());
		}
		return loginModel;
	}

}



