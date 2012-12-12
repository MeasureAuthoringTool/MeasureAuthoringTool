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
	
	public ForgottenPasswordResult forgotPassword(String loginId, 
			String securityQuestion, String securityAnswer) {

		UserService userService = (UserService)context.getBean("userService");
		return userService.requestForgottenPassword(loginId, securityQuestion, securityAnswer);
	}

	@Override
	public void signOut() {
		 getLoginCredentialService().signOut();
	}

	@Override
	public String changePasswordSecurityAnswers(LoginModel model) {
		//boolean result =false;
		LoginModel loginModel = model;
		/*try {
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
		}*/
		
		String resultMessage = callCheckDictionaryWordInPassword(loginModel.getPassword());
		if(resultMessage.equalsIgnoreCase("SUCCESS")){
			getLoginCredentialService().changePasswordSecurityAnswers(model);
		}
		return resultMessage;
		
	}

	@Override
	public LoginModel changeTempPassword(String email, String changedpassword) {
		//boolean result = false;
		LoginModel loginModel = new LoginModel();
		/*try {
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
		}*/
		String resultMessage = callCheckDictionaryWordInPassword(changedpassword);
		
		if(resultMessage.equalsIgnoreCase("EXCEPTION")){
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
		}else if(resultMessage.equalsIgnoreCase("SUCCESS")){
			loginModel= getLoginCredentialService().changeTempPassword(email, changedpassword);
		}else{
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getMustNotContainDictionaryWordMessage());
		}
		
		return loginModel;
	}
	
	private String callCheckDictionaryWordInPassword(String changedpassword){
		String returnMessage = "FAILURE";
		try {
			boolean result = CheckDictionaryWordInPassword.containsDictionaryWords(changedpassword);
			if(result)
				returnMessage = "SUCCESS";
				
		} catch (FileNotFoundException e) {
			returnMessage="EXCEPTION";
			//loginModel.setLoginFailedEvent(true);
			//loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			e.printStackTrace();
			
		} catch (IOException e) {
			returnMessage="EXCEPTION";
			//loginModel.setLoginFailedEvent(true);
			//loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			e.printStackTrace();
		}
		return returnMessage;
		
		
	}

}



