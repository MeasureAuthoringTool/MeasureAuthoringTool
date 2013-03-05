package mat.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.login.LoginModel;
import mat.client.login.service.LoginResult;
import mat.client.login.service.LoginService;
import mat.client.login.service.SecurityQuestionOptions;
import mat.client.myAccount.SecurityQuestionsModel;
import mat.client.shared.MatContext;
import mat.client.util.ClientConstants;
import mat.model.User;
import mat.model.UserSecurityQuestion;
import mat.server.service.LoginCredentialService;
import mat.server.service.UserService;
import mat.server.util.dictionary.CheckDictionaryWordInPassword;
import mat.shared.ForgottenLoginIDResult;
import mat.shared.ForgottenPasswordResult;
import mat.shared.PasswordVerifier;
import mat.shared.SecurityQuestionVerifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;

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
	public SecurityQuestionOptions getSecurityQuestionOptionsForEmail(String email) {
		UserService userService = (UserService)context.getBean("userService");
		return  userService.getSecurityQuestionOptionsForEmail(email);
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
	public ForgottenLoginIDResult forgotLoginID(String email,
			String securityQuestion, String securityAnswer) {
		UserService userService = (UserService)context.getBean("userService");
		return userService.requestForgottenLoginID(email, securityQuestion, securityAnswer);
	}
	@Override
	public void signOut() {
		 getLoginCredentialService().signOut();
	}

	@Override
	public LoginResult changePasswordSecurityAnswers(LoginModel model) {
		LoginModel loginModel = model;
		LoginResult result = new LoginResult();
		logger.info("LoggedInUserUtil.getLoggedInLoginId() ::::"+ LoggedInUserUtil.getLoggedInLoginId());
		logger.info("loginModel.getPassword()() ::::"+ loginModel.getPassword());
		
		PasswordVerifier verifier  = new PasswordVerifier(loginModel.getLoginId(), 
												loginModel.getPassword(), loginModel.getPassword());
		
		if(verifier.isValid()){
			SecurityQuestionVerifier sverifier = new SecurityQuestionVerifier(loginModel.getQuestion1(),
					loginModel.getQuestion1Answer(),
					loginModel.getQuestion2(),
					loginModel.getQuestion2Answer(),
					loginModel.getQuestion3(),
					loginModel.getQuestion3Answer());

			if(sverifier.isValid()){
				String resultMessage = callCheckDictionaryWordInPassword(loginModel.getPassword());
				if(resultMessage.equalsIgnoreCase("SUCCESS")){
					boolean isSuccessful = getLoginCredentialService().changePasswordSecurityAnswers(loginModel);
					if(isSuccessful){
						result.setSuccess(true);
					}else{
						result.setSuccess(false);
					}
				}else{
					logger.info("Server Side Validation Failed in changePasswordSecurityAnswers for User:"+LoggedInUserUtil.getLoggedInUser() );
					result.setSuccess(false);
					result.setFailureReason(LoginResult.DICTIONARY_EXCEPTION);
				}
			}else{
				logger.info("Server Side Validation Failed in changePasswordSecurityAnswers for User:"+LoggedInUserUtil.getLoggedInUser() );
				result.setSuccess(false);
				result.setMessages(sverifier.getMessages());
				result.setFailureReason(LoginResult.SERVER_SIDE_VALIDATION_SECURITY_QUESTIONS);
			}
			
		}
		else{
			logger.info("Server Side Validation Failed in changePasswordSecurityAnswers for User:"+LoggedInUserUtil.getLoggedInUser() );
			result.setSuccess(false);
			result.setMessages(verifier.getMessages());
			result.setFailureReason(LoginResult.SERVER_SIDE_VALIDATION_PASSWORD);
			
		}
		return result;
	}

	@Override
	public LoginModel changeTempPassword(String email, String changedpassword) {
		
		LoginModel loginModel = new LoginModel();
		
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
	
	@Override
	public List<String> getFooterURLs(){
		UserService userService = (UserService)context.getBean("userService");
		List<String> footerURLs = userService.getFooterURLs();
		return footerURLs;
	}
	
	private String callCheckDictionaryWordInPassword(String changedpassword){
		String returnMessage = "FAILURE";
		try {
			boolean result = CheckDictionaryWordInPassword.containsDictionaryWords(changedpassword);
			if(result)
				returnMessage = "SUCCESS";
				
		} catch (FileNotFoundException e) {
			returnMessage="EXCEPTION";
			e.printStackTrace();
			
		} catch (IOException e) {
			returnMessage="EXCEPTION";
			e.printStackTrace();
		}
		return returnMessage;
		
		
	}
	
	public List<UserSecurityQuestion> getSecurityQuestionsAnswers(String userID) {
		UserService userService = (UserService)context.getBean("userService");
		User user = userService.getById(userID);
		List<UserSecurityQuestion> secQuestions = new ArrayList<UserSecurityQuestion>(user.getSecurityQuestions()); 
		logger.info("secQuestions Length "+ secQuestions.size());
		return secQuestions;
	}
	
	public String updateOnSignOut(String userId, String emailId, String activityType){
		UserService userService = (UserService)context.getBean("userService");
		String resultStr = userService.updateOnSignOut(userId, emailId, activityType);
		SecurityContextHolder.clearContext();
		logger.info("In UserServiceImpl Signout Update " + resultStr);
		return resultStr;
	}

}



