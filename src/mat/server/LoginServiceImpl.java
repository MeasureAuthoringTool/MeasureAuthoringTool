package mat.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mat.client.login.LoginModel;
import mat.client.login.service.LoginResult;
import mat.client.login.service.LoginService;
import mat.client.login.service.SecurityQuestionOptions;
import mat.client.shared.MatContext;
import mat.client.util.ClientConstants;
import mat.dao.UserDAO;
import mat.model.User;
import mat.model.UserSecurityQuestion;
import mat.server.model.MatUserDetails;
import mat.server.service.LoginCredentialService;
import mat.server.service.TransactionAuditService;
import mat.server.service.UserService;
import mat.server.util.dictionary.CheckDictionaryWordInPassword;
import mat.shared.ConstantMessages;
import mat.shared.ForgottenLoginIDResult;
import mat.shared.ForgottenPasswordResult;
import mat.shared.PasswordVerifier;
import mat.shared.SecurityQuestionVerifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

@SuppressWarnings("serial")
public class LoginServiceImpl extends SpringRemoteServiceServlet implements LoginService{
	private static final Log logger = LogFactory.getLog(LoginServiceImpl.class);
	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FAILURE";
	
	private LoginCredentialService getLoginCredentialService(){
		return (LoginCredentialService)context.getBean("loginService");
	}
	
	

	@Override
	public SecurityQuestionOptions getSecurityQuestionOptions(String userid) {
		UserService userService = (UserService)context.getBean("userService");
		return  userService.getSecurityQuestionOptions(userid);
	}
	
	@Override
	public String getSecurityQuestion(String userid) {
		UserService userService = (UserService)context.getBean("userService");
		return userService.getSecurityQuestion(userid);
	}

	@Override
	public SecurityQuestionOptions getSecurityQuestionOptionsForEmail(String email) {
		UserService userService = (UserService)context.getBean("userService");
		return  userService.getSecurityQuestionOptionsForEmail(email);
	}


	@Override
	public LoginModel  isValidUser(String userId, String password) {
		//Code to create New session ID everytime user log's in. Story Ref - MAT1222
		HttpSession session = getThreadLocalRequest().getSession(false);
		if (session!=null && !session.isNew()) {
		    session.invalidate();
		}
		session = getThreadLocalRequest().getSession(true);
		LoginModel loginModel = getLoginCredentialService().isValidUser(userId, password);
		return loginModel;
	}
	
	public ForgottenPasswordResult forgotPassword(String loginId, 
		String securityQuestion, String securityAnswer) {

		UserService userService = (UserService)context.getBean("userService");
		return userService.requestForgottenPassword(loginId, securityQuestion, securityAnswer);
	}

	@Override
	public ForgottenLoginIDResult forgotLoginID(String email) {
		UserService userService = (UserService)context.getBean("userService");
		ForgottenLoginIDResult forgottenLoginIDResult = userService.requestForgottenLoginID(email);
		if(!forgottenLoginIDResult.isEmailSent()){
			String ipAddress = getClientIpAddr(getThreadLocalRequest());
			logger.info("CLient IPAddress :: " + ipAddress);
			String message=null;
			TransactionAuditService auditService = (TransactionAuditService)context.getBean("transactionAuditService");
			if(forgottenLoginIDResult.getFailureReason()==5){
				message = MatContext.get().getMessageDelegate().getLoginFailedAlreadyLoggedInMessage();
				//this is to show success message on client side.
				forgottenLoginIDResult.setEmailSent(true);
				//Illegal activity is logged in Transaction Audit table with IP Address of client requesting for User Id.
				auditService.recordTransactionEvent(UUID.randomUUID().toString(), null, "FORGOT_USER_EVENT", email, "[IP: "+ipAddress+" ]"+"[EMAIL Entered: "+email+" ]" +message, ConstantMessages.DB_LOG);
			}else if (forgottenLoginIDResult.getFailureReason()==4){
				message = MatContext.get().getMessageDelegate().getEmailNotFoundMessage();
				//this is to show success message on client side.
				forgottenLoginIDResult.setEmailSent(true);
				//Illegal activity is logged in Transaction Audit table with IP Address of client requesting for User Id.
				auditService.recordTransactionEvent(UUID.randomUUID().toString(), null, "FORGOT_USER_EVENT", email, "[IP: "+ipAddress+" ]"+"[EMAIL Entered: "+email+" ]" +message, ConstantMessages.DB_LOG);
			}
			
		}
		return forgottenLoginIDResult;
	}
	
	
	
	/** Method to find IP address of Client **/
	private String getClientIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
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
	
	@Override
	public HashMap<String,String> validatePassword(String userID,String enteredPassword){
		String ifMatched = FAILURE;
		HashMap<String,String> resultMap = new HashMap<String,String>(); 
		if(enteredPassword.equals(null)|| enteredPassword.equals("")){
			resultMap.put("message",MatContext.get().getMessageDelegate().getPasswordRequiredErrorMessage());
		}else{
				UserDAO userDAO = (UserDAO)context.getBean("userDAO");
				MatUserDetails userDetails =(MatUserDetails )userDAO.getUser(userID);
				if(userDetails != null)
				{
					UserService userService = (UserService)context.getBean("userService");
					String hashPassword = userService.getPasswordHash(userDetails.getUserPassword().getSalt(), enteredPassword);
					if(hashPassword.equalsIgnoreCase(userDetails.getUserPassword().getPassword())){
						ifMatched= SUCCESS;
					}else{
						int currentPasswordlockCounter = userDetails.getUserPassword().getPasswordlockCounter();
						logger.info("CurrentPasswordLockCounter value:" +currentPasswordlockCounter);
						if(currentPasswordlockCounter == 2){
							//Force the user to log out of the system
							 //MatContext.get().handleSignOut("SIGN_OUT_EVENT", true);
							 String resultStr = updateOnSignOut(userDetails.getId(), userDetails.getEmailAddress(),"SIGN_OUT_EVENT" );
							 if(resultStr.equals(SUCCESS)){
								 Date currentDate = new Date();
								 Timestamp currentTimeStamp = new Timestamp(currentDate.getTime());
								 userDetails.setSignOutDate(currentTimeStamp);
								 userDetails.getUserPassword().setPasswordlockCounter(0);
								 userDAO.saveUserDetails(userDetails);
								 resultMap.put("message","REDIRECT");
								 logger.info("Locking user out with LOGIN ID ::" + userDetails.getLoginId() + " :: USER ID ::" + userDetails.getId());
							 }	 
						}else{
							resultMap.put("message",MatContext.get().getMessageDelegate().getPasswordMismatchErrorMessage());
							userDetails.getUserPassword().setPasswordlockCounter(currentPasswordlockCounter + 1);
							userDAO.saveUserDetails(userDetails);
						}
						 
					}
				}
		}	
		resultMap.put("result",ifMatched);
		return resultMap;	
	}
	
	public void redirectToHtmlPage(String html) {
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		String path = Window.Location.getPath();
		path=path.substring(0, path.lastIndexOf('/'));
		path += html;
		urlBuilder.setPath(path);
		Window.Location.replace(urlBuilder.buildString());
	}
	
	
	private String callCheckDictionaryWordInPassword(String changedpassword){
		String returnMessage = FAILURE;
		try {
			boolean result = CheckDictionaryWordInPassword.containsDictionaryWords(changedpassword);
			if(result)
				returnMessage = SUCCESS;
				
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
		getThreadLocalRequest().getSession().invalidate();
		logger.info("User Session Invalidated at :::: " +new Date());
		logger.info("In UserServiceImpl Signout Update " + resultStr);
		return resultStr;
	}

	
    
}



