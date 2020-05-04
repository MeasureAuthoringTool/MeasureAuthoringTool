package mat.server;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import mat.client.login.LoginModel;
import mat.client.login.service.LoginResult;
import mat.client.login.service.LoginService;
import mat.client.login.service.SecurityQuestionOptions;
import mat.client.shared.MatContext;
import mat.client.shared.MatException;
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
import mat.shared.ConstantMessages;
import mat.shared.ForgottenLoginIDResult;
import mat.shared.ForgottenPasswordResult;
import mat.shared.HarpConstants;
import mat.shared.HashUtility;
import mat.shared.PasswordVerifier;
import mat.shared.SecurityQuestionVerifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * The Class LoginServiceImpl.
 */
@SuppressWarnings("serial")
@Service
public class LoginServiceImpl extends SpringRemoteServiceServlet implements LoginService {
	
	private static final Log logger = LogFactory.getLog(LoginServiceImpl.class);
	
	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FAILURE";
	static int AUDIT_LOG_USER_ID_LENGTH = 40;
	
	@Autowired private UserDAO userDAO;
	@Autowired private UserPasswordHistoryDAO userPasswordHistoryDAO;
	@Autowired private UserService userService;	
	@Autowired private LoginCredentialService loginCredentialService;
	@Autowired private TransactionAuditService auditService;
	@Autowired private SecurityQuestionsService securityQuestionsService;
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public SecurityQuestionOptions getSecurityQuestionOptions(String userid) {
		return userService.getSecurityQuestionOptions(userid);
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public String getSecurityQuestion(String userid) {
		return userService.getSecurityQuestion(userid);
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public SecurityQuestionOptions getSecurityQuestionOptionsForEmail(String email) {
		return userService.getSecurityQuestionOptionsForEmail(email);
	}
	
	/* (non-Javadoc)
	 * {@inheritDoc}
	 */
	@Override
	public LoginModel isValidUser(String userId, String password, String oneTimePassword) {
		// Code to create New session ID everytime user log's in. Story Ref - MAT1222
		HttpSession session = getThreadLocalRequest().getSession(false);
		if ((session != null) && !session.isNew()) {
			session.invalidate();
		}
		session = getThreadLocalRequest().getSession(true);
		LoginModel loginModel = loginCredentialService.isValidUser(userId, password, oneTimePassword,session.getId());
		return loginModel;
	}

	@Override
	public LoginModel initSession(Map<String, String> harpUserInfo) throws MatException {
		logger.info("initSession::harpId::" + harpUserInfo.get(HarpConstants.HARP_ID));
		HttpSession session = getThreadLocalRequest().getSession();
		if (userService.isHarpUserLockedRevoked(harpUserInfo.get(HarpConstants.HARP_ID))) {
			throw new MatException("MAT_ACCOUNT_REVOKED_LOCKED");
		}
		return loginCredentialService.initSession(harpUserInfo, session.getId());
	}

    @Override
    public Boolean checkForAssociatedHarpId(String harpId) throws MatException {
	    try {
            return userDAO.findAssociatedHarpId(harpId);
        } catch (Exception e) {
            throw new MatException("Unable to verify if user has associated Harp Id");
        }

    }

    @Override
    public String getSecurityQuestionToVerifyHarpUser(String loginId, String password) throws MatException {
	    try {
            if(isValidPassword(loginId, password)) {
                return userDAO.getRandomSecurityQuestion(loginId);
            } else {
                throw new MatException("Invalid User");
            }
        } catch (Exception e) {
            throw new MatException("Unable to retrieve a security question to verify user");
        }
    }

    @Override
    public boolean verifyHarpUser(String securityQuestion, String securityAnswer, String loginId, Map<String, String> harpUserInfo) throws MatException {
	    User user = userDAO.findByLoginId(loginId);
        if (StringUtils.isNotBlank(securityAnswer)) {
            for (UserSecurityQuestion q : user.getUserSecurityQuestions()) {
                if (q.getSecurityQuestions().getQuestion().equalsIgnoreCase(securityQuestion)) {
                    if(HashUtility.getSecurityQuestionHash(q.getSalt(), securityAnswer).equalsIgnoreCase(q.getSecurityAnswer())) {
                        saveHarpUserInfo(harpUserInfo, loginId);
						if (userService.isHarpUserLockedRevoked(harpUserInfo.get(HarpConstants.HARP_ID))) {
							throw new MatException("MAT_ACCOUNT_REVOKED_LOCKED");
						}
                        return true;
                    }
                }
            }
        }
        return false;
    }

	@Override
	public boolean isValidPassword(String loginId, String password) {
        return loginCredentialService.isValidPassword(loginId, password);
	}

	private void saveHarpUserInfo(Map<String, String> harpUserInfo, String loginId) throws MatException {
        logger.info("User Verified, updating user information of::harpId::" + harpUserInfo.get(HarpConstants.HARP_ID));
        HttpSession session = getThreadLocalRequest().getSession();
        loginCredentialService.saveHarpUserInfo(harpUserInfo, loginId, session.getId());
    }

	@Override
	public ForgottenPasswordResult forgotPassword(String loginId, String securityQuestion, String securityAnswer) {
		
		// don't pass invalidUserCounter to server anymore
		ForgottenPasswordResult forgottenPasswordResult = userService.requestForgottenPassword(loginId, securityQuestion, securityAnswer, 1);
		String ipAddress = getClientIpAddr(getThreadLocalRequest());
		logger.info("Login ID --- " + loginId);
		String truncatedLoginId = loginId;
		if (loginId.length() > AUDIT_LOG_USER_ID_LENGTH) {
			truncatedLoginId = loginId.substring(0, AUDIT_LOG_USER_ID_LENGTH);
		}
		if (forgottenPasswordResult.getFailureReason() > 0) {
			logger.info("Forgot Password Failed ====> CLient IPAddress :: " + ipAddress);
			auditService.recordTransactionEvent(UUID.randomUUID().toString(), null, "FORGOT_PASSWORD_EVENT", 
					truncatedLoginId, "[IP: " + ipAddress + " ]" + "Forgot Password Failed for " + loginId, ConstantMessages.DB_LOG);
		} else {
			logger.info("Forgot Password Success ====> CLient IPAddress :: " + ipAddress);
			auditService.recordTransactionEvent(UUID.randomUUID().toString(), null, "FORGOT_PASSWORD_EVENT", 
					truncatedLoginId, "[IP: " + ipAddress + " ]" + "Forgot Password Success for" + loginId, ConstantMessages.DB_LOG);
		}
		return forgottenPasswordResult;
		
	}
	
	/* (non-Javadoc)
	 * {@inheritDoc}
	 */
	@Override
	public ForgottenLoginIDResult forgotLoginID(String email) {
		ForgottenLoginIDResult forgottenLoginIDResult = userService.requestForgottenLoginID(email);
		if (!forgottenLoginIDResult.isEmailSent()) {
			String ipAddress = getClientIpAddr(getThreadLocalRequest());
			logger.info("CLient IPAddress :: " + ipAddress);
			String message = null;
			String truncatedEmail = email;
			if (email.length() > AUDIT_LOG_USER_ID_LENGTH) {
				truncatedEmail = email.substring(0, AUDIT_LOG_USER_ID_LENGTH);
			}
			if (forgottenLoginIDResult.getFailureReason() == 5) {
				logger.info(" User ID Found and but user already logged in : IP Address Location :" + ipAddress);
				message = MatContext.get().getMessageDelegate().getLoginFailedAlreadyLoggedInMessage();
				// this is to show success message on client side.
				forgottenLoginIDResult.setEmailSent(true);
				// Failure reason un-set : burp suite showing different values
				// in response for invalid user. It should be same for valid and
				// invalid user.
				forgottenLoginIDResult.setFailureReason(0);
				// Illegal activity is logged in Transaction Audit table with IP
				// Address of client requesting for User Id.
				auditService.recordTransactionEvent(UUID.randomUUID().toString(), null, "FORGOT_USER_EVENT", 
						truncatedEmail, "[IP: " + ipAddress + " ]" + "[EMAIL Entered: " + email + " ]" + message, ConstantMessages.DB_LOG);
			} else if (forgottenLoginIDResult.getFailureReason() == 4) {
				message = MatContext.get().getMessageDelegate().getEmailNotFoundMessage();
				logger.info(" User ID : " + email + " Not found in User Table IP Address Location :" + ipAddress);
				// this is to show success message on client side.
				forgottenLoginIDResult.setEmailSent(true);
				// Failure reason un-set : burp suite showing different values
				// in response for invalid user. It should be same for valid and
				// invalid user.
				forgottenLoginIDResult.setFailureReason(0);
				// Illegal activity is logged in Transaction Audit table with IP
				// Address of client requesting for User Id.
				auditService.recordTransactionEvent(UUID.randomUUID().toString(), null, "FORGOT_USER_EVENT", 
						truncatedEmail, "[IP: " + ipAddress + " ]" + "[EMAIL Entered: " + email + " ]" + message, ConstantMessages.DB_LOG);
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
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public void signOut() {
		loginCredentialService.signOut();
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public LoginResult changePasswordSecurityAnswers(LoginModel model) {
		LoginModel loginModel = model;
		model.scrubForMarkUp();
		LoginResult result = new LoginResult();
		logger.info("LoggedInUserUtil.getLoggedInLoginId() ::::" + LoggedInUserUtil.getLoggedInLoginId());
		logger.info("loginModel.getPassword()() ::::" + loginModel.getPassword());
		String markupRegExp = "<[^>]+>";
		String noMarkupTextPwd = loginModel.getPassword().trim().replaceAll(markupRegExp, "");
		loginModel.setPassword(noMarkupTextPwd);
		PasswordVerifier verifier = new PasswordVerifier(loginModel.getPassword(), loginModel.getPassword());
		
		if (verifier.isValid()) {
			SecurityQuestionVerifier sverifier = new SecurityQuestionVerifier(
					loginModel.getQuestion1(), loginModel.getQuestion1Answer(),
					loginModel.getQuestion2(), loginModel.getQuestion2Answer(),
					loginModel.getQuestion3(), loginModel.getQuestion3Answer());
			
			if (sverifier.isValid()) {
				boolean isSuccessful = loginCredentialService.changePasswordSecurityAnswers(loginModel);
				result.setSuccess(isSuccessful);
			} else {
				logger.info("Server Side Validation Failed in changePasswordSecurityAnswers for User:" + LoggedInUserUtil.getLoggedInUser());
				result.setSuccess(false);
				result.setMessages(sverifier.getMessages());
				result.setFailureReason(LoginResult.SERVER_SIDE_VALIDATION_SECURITY_QUESTIONS);
			}
			
		} else {
			logger.info("Server Side Validation Failed in changePasswordSecurityAnswers for User:" + LoggedInUserUtil.getLoggedInUser());
			result.setSuccess(false);
			result.setMessages(verifier.getMessages());
			result.setFailureReason(LoginResult.SERVER_SIDE_VALIDATION_PASSWORD);
			
		}
		return result;
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public LoginModel changeTempPassword(String email, String changedpassword) {
		return loginCredentialService.changeTempPassword(email, changedpassword);
	}
	
	/* (non-Javadoc)
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getFooterURLs() {
		List<String> footerURLs = userService.getFooterURLs();
		return footerURLs;
	}
	
	/* (non-Javadoc)
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, String> validatePassword(String userID, String enteredPassword) {
		String ifMatched = FAILURE;
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if ((enteredPassword == null) || enteredPassword.equals("")) {
			resultMap.put("message", MatContext.get().getMessageDelegate().getPasswordRequiredErrorMessage());
		} else {
			MatUserDetails userDetails = (MatUserDetails) userDAO.getUser(userID);
			if (userDetails != null) {
				String hashPassword = userService.getPasswordHash(userDetails.getUserPassword().getSalt(), enteredPassword);
				if (hashPassword.equalsIgnoreCase(userDetails.getUserPassword().getPassword())) {
					ifMatched = SUCCESS;
				} else {
					int currentPasswordlockCounter = userDetails.getUserPassword().getPasswordlockCounter();
					logger.info("CurrentPasswordLockCounter value:" + currentPasswordlockCounter);
					if (currentPasswordlockCounter == 2) {
						String resultStr = updateOnSignOut(userDetails.getId(), userDetails.getEmailAddress(), "SIGN_OUT_EVENT");
						if (resultStr.equals(SUCCESS)) {
							Date currentDate = new Date();
							Timestamp currentTimeStamp = new Timestamp(currentDate.getTime());
							userDetails.setSignOutDate(currentTimeStamp);
							userDetails.setSessionId(null);
							userDetails.getUserPassword()
							.setPasswordlockCounter(0);
							userDAO.saveUserDetails(userDetails);
							resultMap.put("message", "REDIRECT");
							logger.info("Locking user out with LOGIN ID ::" + userDetails.getLoginId() + " :: USER ID ::" + userDetails.getId());
						}
					} else {
						resultMap.put("message", MatContext.get().getMessageDelegate().getPasswordMismatchErrorMessage());
						userDetails.getUserPassword().setPasswordlockCounter(currentPasswordlockCounter + 1);
						userDAO.saveUserDetails(userDetails);
					}
					
				}
			}
		}
		resultMap.put("result", ifMatched);
		return resultMap;
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, String> validateNewPassword(String userID,String newPassword) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		MatUserDetails userDetails = (MatUserDetails) userDAO.getUser(userID);
		String ifMatched = FAILURE;
		
		if (userDetails != null) {
			String hashPassword = userService.getPasswordHash(userDetails .getUserPassword().getSalt(), newPassword);
			
			if (hashPassword.equalsIgnoreCase(userDetails.getUserPassword().getPassword())) {
				ifMatched = SUCCESS;
			}

			if(ifMatched.equals(FAILURE)){
				List<UserPasswordHistory> passwordHistory = userPasswordHistoryDAO.getPasswordHistory(userDetails.getId());
				for(int i=0; i<passwordHistory.size(); i++){
					hashPassword = userService.getPasswordHash(passwordHistory.get(i).getSalt(), newPassword);
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
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, String> validatePasswordCreationDate(String userID) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
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
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public List<UserSecurityQuestion> getSecurityQuestionsAnswers(String userID) {
		User user = userService.getById(userID);
		List<UserSecurityQuestion> secQuestions = new ArrayList<UserSecurityQuestion>(user.getUserSecurityQuestions());
		logger.info("secQuestions Length " + secQuestions.size());
		return secQuestions;
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public String updateOnSignOut(String userId, String emailId, String activityType) {
		UMLSSessionTicket.remove(getThreadLocalRequest().getSession().getId());
		String resultStr = userService.updateOnSignOut(userId, emailId, activityType);
		SecurityContextHolder.clearContext();
		getThreadLocalRequest().getSession().invalidate();
		logger.info("User Session Invalidated at :::: " + new Date());
		logger.info("In UserServiceImpl Signout Update " + resultStr);
		return resultStr;
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLockedUser(String loginId) {
		return userService.isLockedUser(loginId);
	}

	@Override
	public boolean isHarpUserLocked(String harpId) {
		return userService.isHarpUserLockedRevoked(harpId);
	}

	/* 
	 * {@inheritDoc}
	 */
	@Override
	public List<SecurityQuestions> getSecurityQuestions() {
		logger.info("Found...." + securityQuestionsService);
		return securityQuestionsService.getSecurityQuestions();
	}
}
