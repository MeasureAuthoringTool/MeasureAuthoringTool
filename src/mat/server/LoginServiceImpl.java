package mat.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import mat.dao.UserDAO;
import mat.dao.UserPasswordHistoryDAO;
import mat.model.SecurityQuestions;
import mat.model.User;
import mat.model.UserPasswordHistory;
import mat.model.UserSecurityQuestion;
import mat.server.model.MatUserDetails;
import mat.server.service.LoginCredentialService;
import mat.server.service.SecurityQuestionsService;
import mat.server.service.TransactionAuditService;
import mat.server.service.UserService;
import mat.server.util.UMLSSessionTicket;
import mat.server.util.dictionary.CheckDictionaryWordInPassword;
import mat.shared.ConstantMessages;
import mat.shared.ForgottenLoginIDResult;
import mat.shared.ForgottenPasswordResult;
import mat.shared.PasswordVerifier;
import mat.shared.SecurityQuestionVerifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;


/**
 * The Class LoginServiceImpl.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends SpringRemoteServiceServlet implements
LoginService {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(LoginServiceImpl.class);
	
	/** The Constant SUCCESS. */
	private static final String SUCCESS = "SUCCESS";
	
	/** The Constant FAILURE. */
	private static final String FAILURE = "FAILURE";
	
	/** The length of the CREATE_USER field in the audit_log db table */
	static int AUDIT_LOG_USER_ID_LENGTH = 40;
	/**
	 * Gets the login credential service.
	 * 
	 * @return the login credential service
	 */
	private LoginCredentialService getLoginCredentialService() {
		return (LoginCredentialService) context.getBean("loginService");
	}
	
	/** The user dao. */
	@Autowired
	private UserDAO userDAO;
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#getSecurityQuestionOptions(java.lang.String)
	 */
	@Override
	public SecurityQuestionOptions getSecurityQuestionOptions(String userid) {
		UserService userService = (UserService) context.getBean("userService");
		return userService.getSecurityQuestionOptions(userid);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#getSecurityQuestion(java.lang.String)
	 */
	@Override
	public String getSecurityQuestion(String userid) {
		UserService userService = (UserService) context.getBean("userService");
		return userService.getSecurityQuestion(userid);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#getSecurityQuestionOptionsForEmail(java.lang.String)
	 */
	@Override
	public SecurityQuestionOptions getSecurityQuestionOptionsForEmail(
			String email) {
		UserService userService = (UserService) context.getBean("userService");
		return userService.getSecurityQuestionOptionsForEmail(email);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#isValidUser(java.lang.String, java.lang.String)
	 */
	@Override
	public LoginModel isValidUser(String userId, String password) {
		// Code to create New session ID everytime user log's in. Story Ref -
		// MAT1222
		HttpSession session = getThreadLocalRequest().getSession(false);
		if ((session != null) && !session.isNew()) {
			session.invalidate();
		}
		session = getThreadLocalRequest().getSession(true);
		LoginModel loginModel = getLoginCredentialService().isValidUser(userId,
				password);
		return loginModel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#isValidPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isValidPassword(String userId, String password) {
		
		Boolean isValid = getLoginCredentialService().isValidPassword(userId,
				password);
		
		return isValid;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#forgotPassword(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public ForgottenPasswordResult forgotPassword(String loginId,
			String securityQuestion, String securityAnswer,
			int invalidUserCounter) {
		
		UserService userService = (UserService) context.getBean("userService");
		// don't pass invalidUserCounter to server anymore
		ForgottenPasswordResult forgottenPasswordResult = userService
				.requestForgottenPassword(loginId, securityQuestion,
						securityAnswer, 1);
		String ipAddress = getClientIpAddr(getThreadLocalRequest());
		TransactionAuditService auditService = (TransactionAuditService) context
				.getBean("transactionAuditService");
		logger.info("Login ID --- " + loginId);
		String truncatedLoginId = loginId;
		if (loginId.length() > AUDIT_LOG_USER_ID_LENGTH) {
			truncatedLoginId = loginId.substring(0, AUDIT_LOG_USER_ID_LENGTH);
		}
		if (forgottenPasswordResult.getFailureReason() > 0) {
			logger.info("Forgot Password Failed ====> CLient IPAddress :: "
					+ ipAddress);
			auditService.recordTransactionEvent(UUID.randomUUID().toString(),
					null, "FORGOT_PASSWORD_EVENT", truncatedLoginId, "[IP: " + ipAddress
					+ " ]" + "Forgot Password Failed for " + loginId,
					ConstantMessages.DB_LOG);
		} else {
			logger.info("Forgot Password Success ====> CLient IPAddress :: "
					+ ipAddress);
			auditService.recordTransactionEvent(UUID.randomUUID().toString(),
					null, "FORGOT_PASSWORD_EVENT", truncatedLoginId, "[IP: " + ipAddress
					+ " ]" + "Forgot Password Success for" + loginId,
					ConstantMessages.DB_LOG);
		}
		return forgottenPasswordResult;
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#forgotLoginID(java.lang.String)
	 */
	@Override
	public ForgottenLoginIDResult forgotLoginID(String email) {
		UserService userService = (UserService) context.getBean("userService");
		ForgottenLoginIDResult forgottenLoginIDResult = userService
				.requestForgottenLoginID(email);
		if (!forgottenLoginIDResult.isEmailSent()) {
			String ipAddress = getClientIpAddr(getThreadLocalRequest());
			logger.info("CLient IPAddress :: " + ipAddress);
			String message = null;
			String truncatedEmail = email;
			if (email.length() > AUDIT_LOG_USER_ID_LENGTH) {
				truncatedEmail = email.substring(0, AUDIT_LOG_USER_ID_LENGTH);
			}
			TransactionAuditService auditService = (TransactionAuditService) context
					.getBean("transactionAuditService");
			if (forgottenLoginIDResult.getFailureReason() == 5) {
				logger.info(" User ID Found and but user already logged in : IP Address Location :"
						+ ipAddress);
				message = MatContext.get().getMessageDelegate()
						.getLoginFailedAlreadyLoggedInMessage();
				// this is to show success message on client side.
				forgottenLoginIDResult.setEmailSent(true);
				// Failure reason un-set : burp suite showing different values
				// in response for invalid user. It should be same for valid and
				// invalid user.
				forgottenLoginIDResult.setFailureReason(0);
				// Illegal activity is logged in Transaction Audit table with IP
				// Address of client requesting for User Id.
				auditService.recordTransactionEvent(UUID.randomUUID()
						.toString(), null, "FORGOT_USER_EVENT", truncatedEmail, "[IP: "
								+ ipAddress + " ]" + "[EMAIL Entered: " + email + " ]"
								+ message, ConstantMessages.DB_LOG);
			} else if (forgottenLoginIDResult.getFailureReason() == 4) {
				message = MatContext.get().getMessageDelegate()
						.getEmailNotFoundMessage();
				logger.info(" User ID : " + email
						+ " Not found in User Table IP Address Location :"
						+ ipAddress);
				// this is to show success message on client side.
				forgottenLoginIDResult.setEmailSent(true);
				// Failure reason un-set : burp suite showing different values
				// in response for invalid user. It should be same for valid and
				// invalid user.
				forgottenLoginIDResult.setFailureReason(0);
				// Illegal activity is logged in Transaction Audit table with IP
				// Address of client requesting for User Id.
				auditService.recordTransactionEvent(UUID.randomUUID()
						.toString(), null, "FORGOT_USER_EVENT", truncatedEmail, "[IP: "
								+ ipAddress + " ]" + "[EMAIL Entered: " + email + " ]"
								+ message, ConstantMessages.DB_LOG);
			}
			
		}
		return forgottenLoginIDResult;
	}
	
	/**
	 * Method to find IP address of Client *.
	 * 
	 * @param request
	 *            the request
	 * @return the client ip addr
	 */
	private String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#signOut()
	 */
	@Override
	public void signOut() {
		getLoginCredentialService().signOut();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#changePasswordSecurityAnswers(mat.client.login.LoginModel)
	 */
	@Override
	public LoginResult changePasswordSecurityAnswers(LoginModel model) {
		LoginModel loginModel = model;
		model.scrubForMarkUp();
		LoginResult result = new LoginResult();
		logger.info("LoggedInUserUtil.getLoggedInLoginId() ::::"
				+ LoggedInUserUtil.getLoggedInLoginId());
		logger.info("loginModel.getPassword()() ::::"
				+ loginModel.getPassword());
		String markupRegExp = "<[^>]+>";
		String noMarkupTextPwd = loginModel.getPassword().trim().replaceAll(markupRegExp, "");
		loginModel.setPassword(noMarkupTextPwd);
		PasswordVerifier verifier = new PasswordVerifier(
				loginModel.getLoginId(), loginModel.getPassword(),
				loginModel.getPassword());
		
		if (verifier.isValid()) {
			SecurityQuestionVerifier sverifier = new SecurityQuestionVerifier(
					loginModel.getQuestion1(), loginModel.getQuestion1Answer(),
					loginModel.getQuestion2(), loginModel.getQuestion2Answer(),
					loginModel.getQuestion3(), loginModel.getQuestion3Answer());
			
			if (sverifier.isValid()) {
				String resultMessage = callCheckDictionaryWordInPassword(loginModel
						.getPassword());
				if (resultMessage.equalsIgnoreCase("SUCCESS")) {
					boolean isSuccessful = getLoginCredentialService()
							.changePasswordSecurityAnswers(loginModel);
					if (isSuccessful) {
						result.setSuccess(true);
					} else {
						result.setSuccess(false);
					}
				} else {
					logger.info("Server Side Validation Failed in changePasswordSecurityAnswers for User:"
							+ LoggedInUserUtil.getLoggedInUser());
					result.setSuccess(false);
					result.setFailureReason(LoginResult.DICTIONARY_EXCEPTION);
				}
			} else {
				logger.info("Server Side Validation Failed in changePasswordSecurityAnswers for User:"
						+ LoggedInUserUtil.getLoggedInUser());
				result.setSuccess(false);
				result.setMessages(sverifier.getMessages());
				result.setFailureReason(LoginResult.SERVER_SIDE_VALIDATION_SECURITY_QUESTIONS);
			}
			
		} else {
			logger.info("Server Side Validation Failed in changePasswordSecurityAnswers for User:"
					+ LoggedInUserUtil.getLoggedInUser());
			result.setSuccess(false);
			result.setMessages(verifier.getMessages());
			result.setFailureReason(LoginResult.SERVER_SIDE_VALIDATION_PASSWORD);
			
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#changeTempPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public LoginModel changeTempPassword(String email, String changedpassword) {
		
		LoginModel loginModel = new LoginModel();
		
		String resultMessage = callCheckDictionaryWordInPassword(changedpassword);
		
		if (resultMessage.equalsIgnoreCase("EXCEPTION")) {
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MatContext.get().getMessageDelegate()
					.getGenericErrorMessage());
		} else if (resultMessage.equalsIgnoreCase("SUCCESS")) {
			loginModel = getLoginCredentialService().changeTempPassword(email,
					changedpassword);
		} else {
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MatContext.get().getMessageDelegate()
					.getMustNotContainDictionaryWordMessage());
		}
		
		return loginModel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#getFooterURLs()
	 */
	@Override
	public List<String> getFooterURLs() {
		UserService userService = (UserService) context.getBean("userService");
		List<String> footerURLs = userService.getFooterURLs();
		return footerURLs;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#validatePassword(java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String, String> validatePassword(String userID,
			String enteredPassword) {
		String ifMatched = FAILURE;
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if ((enteredPassword == null) || enteredPassword.equals("")) {
			resultMap.put("message", MatContext.get().getMessageDelegate()
					.getPasswordRequiredErrorMessage());
		} else {
			UserDAO userDAO = (UserDAO) context.getBean("userDAO");
			MatUserDetails userDetails = (MatUserDetails) userDAO
					.getUser(userID);
			if (userDetails != null) {
				UserService userService = (UserService) context
						.getBean("userService");
				String hashPassword = userService.getPasswordHash(userDetails
						.getUserPassword().getSalt(), enteredPassword);
				if (hashPassword.equalsIgnoreCase(userDetails.getUserPassword()
						.getPassword())) {
					ifMatched = SUCCESS;
				} else {
					int currentPasswordlockCounter = userDetails
							.getUserPassword().getPasswordlockCounter();
					logger.info("CurrentPasswordLockCounter value:"
							+ currentPasswordlockCounter);
					if (currentPasswordlockCounter == 2) {
						// Force the user to log out of the system
						// MatContext.get().handleSignOut("SIGN_OUT_EVENT",
						// true);
						String resultStr = updateOnSignOut(userDetails.getId(),
								userDetails.getEmailAddress(), "SIGN_OUT_EVENT");
						if (resultStr.equals(SUCCESS)) {
							Date currentDate = new Date();
							Timestamp currentTimeStamp = new Timestamp(
									currentDate.getTime());
							userDetails.setSignOutDate(currentTimeStamp);
							userDetails.getUserPassword()
							.setPasswordlockCounter(0);
							userDAO.saveUserDetails(userDetails);
							resultMap.put("message", "REDIRECT");
							logger.info("Locking user out with LOGIN ID ::"
									+ userDetails.getLoginId()
									+ " :: USER ID ::" + userDetails.getId());
						}
					} else {
						resultMap.put("message", MatContext.get()
								.getMessageDelegate()
								.getPasswordMismatchErrorMessage());
						userDetails.getUserPassword().setPasswordlockCounter(
								currentPasswordlockCounter + 1);
						userDAO.saveUserDetails(userDetails);
					}
					
				}
			}
		}
		resultMap.put("result", ifMatched);
		return resultMap;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#validateNewPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String, String> validateNewPassword(String userID,String newPassword) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		UserDAO userDAO = (UserDAO) context.getBean("userDAO");
		UserPasswordHistoryDAO userPasswordHistoryDAO = (UserPasswordHistoryDAO) context.getBean("userPasswordHistoryDAO");
		MatUserDetails userDetails = (MatUserDetails) userDAO.getUser(userID);
		String ifMatched = FAILURE;
		
		if (userDetails != null) {
			
			UserService userService = (UserService) context
					.getBean("userService");
			String hashPassword = userService.getPasswordHash(userDetails
					.getUserPassword().getSalt(), newPassword);
			
			if (hashPassword.equalsIgnoreCase(userDetails.getUserPassword()
					.getPassword())) {
				ifMatched = SUCCESS;
			}

			List<UserPasswordHistory> passwordHistory = userPasswordHistoryDAO.getPasswordHistory(userDetails.getId());
			
			if(ifMatched.equals(FAILURE)){
				for(int i=0; i<passwordHistory.size(); i++){
					hashPassword = userService.getPasswordHash(passwordHistory.get(i).getSalt(),
							newPassword);
					if (hashPassword.equalsIgnoreCase(passwordHistory.get(i).getPassword())) {
						ifMatched = SUCCESS;
						break;
					}
				}
			}
		}
		
		resultMap.put("result", ifMatched);
		return resultMap;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#validatePasswordCreationDate(java.lang.String)
	 */
	@Override
	public HashMap<String, String> validatePasswordCreationDate(String userID) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		UserDAO userDAO = (UserDAO) context.getBean("userDAO");
		MatUserDetails userDetails = (MatUserDetails) userDAO.getUser(userID);
		String ifMatched = FAILURE;
		Calendar calender = GregorianCalendar.getInstance();
		SimpleDateFormat currentDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = currentDateFormat.format(calender.getTime());
		String passwordCreationDate=currentDateFormat.format(userDetails.getUserPassword().getCreatedDate());
		if (userDetails != null) {
			if(currentDate.equals(passwordCreationDate)){
				ifMatched=SUCCESS;
			}
		}
		resultMap.put("result", ifMatched);
		return resultMap;
	}
	
	/**
	 * Redirect to html page.
	 * 
	 * @param html
	 *            the html
	 */
	public void redirectToHtmlPage(String html) {
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		String path = Window.Location.getPath();
		path = path.substring(0, path.lastIndexOf('/'));
		path += html;
		urlBuilder.setPath(path);
		Window.Location.replace(urlBuilder.buildString());
	}
	
	/**
	 * Call check dictionary word in password.
	 * 
	 * @param changedpassword
	 *            the changedpassword
	 * @return the string
	 */
	private String callCheckDictionaryWordInPassword(String changedpassword) {
		String returnMessage = FAILURE;
		try {
			boolean result = CheckDictionaryWordInPassword
					.containsDictionaryWords(changedpassword);
			if (result) {
				returnMessage = SUCCESS;
			}
			
		} catch (FileNotFoundException e) {
			returnMessage = "EXCEPTION";
			e.printStackTrace();
			
		} catch (IOException e) {
			returnMessage = "EXCEPTION";
			e.printStackTrace();
		}
		return returnMessage;
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#getSecurityQuestionsAnswers(java.lang.String)
	 */
	@Override
	public List<UserSecurityQuestion> getSecurityQuestionsAnswers(String userID) {
		UserService userService = (UserService) context.getBean("userService");
		User user = userService.getById(userID);
		List<UserSecurityQuestion> secQuestions = new ArrayList<UserSecurityQuestion>(
				user.getSecurityQuestions());
		logger.info("secQuestions Length " + secQuestions.size());
		return secQuestions;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#updateOnSignOut(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String updateOnSignOut(String userId, String emailId,
			String activityType) {
		UserService userService = (UserService) context.getBean("userService");
		UMLSSessionTicket.remove(getThreadLocalRequest().getSession().getId());
		String resultStr = userService.updateOnSignOut(userId, emailId,
				activityType);
		SecurityContextHolder.clearContext();
		getThreadLocalRequest().getSession().invalidate();
		logger.info("User Session Invalidated at :::: " + new Date());
		logger.info("In UserServiceImpl Signout Update " + resultStr);
		return resultStr;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#isLockedUser(java.lang.String)
	 */
	@Override
	public boolean isLockedUser(String loginId) {
		UserService userService = (UserService) context.getBean("userService");
		return userService.isLockedUser(loginId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.login.service.LoginService#getSecurityQuestions()
	 */
	@Override
	public List<SecurityQuestions> getSecurityQuestions() {
		logger.info("Loading....");
		SecurityQuestionsService securityQuestionsService = (SecurityQuestionsService) context
				.getBean("securityQuestionsService");
		logger.info("Found...." + context.getBean("securityQuestionsService"));
		return securityQuestionsService.getSecurityQuestions();
	}
	
}
