package mat.server.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;

import mat.DTO.UserPreferenceDTO;
import mat.client.login.LoginModel;
import mat.client.shared.MatContext;
import mat.dao.UserDAO;
import mat.dao.UserSecurityQuestionDAO;
import mat.model.SecurityQuestions;
import mat.model.User;
import mat.model.UserPassword;
import mat.model.UserPreference;
import mat.model.UserSecurityQuestion;
import mat.server.hibernate.HibernateUserDetailService;
import mat.server.model.MatUserDetails;
import mat.server.service.LoginCredentialService;
import mat.server.service.SecurityQuestionsService;
import mat.server.service.UserService;
import mat.server.twofactorauth.TwoFactorValidationService;
import mat.shared.HashUtility;
import mat.shared.StringUtility;
import org.springframework.stereotype.Service;

/** The Class LoginCredentialServiceImpl. */
@Service
public class LoginCredentialServiceImpl implements LoginCredentialService {
	
	private static final Log logger = LogFactory.getLog(LoginCredentialServiceImpl.class);
	private static Timestamp currentTimeStamp;
	
	@Autowired private HibernateUserDetailService hibernateUserService;
	@Autowired private SecurityQuestionsService securityQuestionsService;
	@Autowired private UserDAO userDAO;
	@Autowired private UserService userService;
	@Autowired private TwoFactorValidationService matOtpValidatorService;
	@Autowired private UserSecurityQuestionDAO userSecurityQuestionDAO;
	
	/*
	 * {@inheritDoc}
	 */
	@Override
	public boolean changePasswordSecurityAnswers(LoginModel model) {
		logger.info("First time login, changing password and security answers");
		logger.info("Changing password");
		boolean result = false;
		User user = userService.getById(model.getUserId());
		
		//before setting new Password we need to store the old password in password History
		userService.addByUpdateUserPasswordHistory(user,false);
		userService.setUserPassword(user, model.getPassword(), false);
		user.getPassword().setInitial(false);
		logger.info("Saving security questions");
		List<UserSecurityQuestion> secQuestions = user.getUserSecurityQuestions();
		while (secQuestions.size() < 3) {
			UserSecurityQuestion newQuestion = new UserSecurityQuestion();
			newQuestion.setUserId(user.getId());
			secQuestions.add(newQuestion);
		}
		String newQuestion1 = model.getQuestion1();
		SecurityQuestions secQue1 = securityQuestionsService.getSecurityQuestionObj(newQuestion1);
		secQuestions.get(0).setSecurityQuestionId(secQue1.getQuestionId());
		secQuestions.get(0).setSecurityQuestions(secQue1);
		
		String originalAnswer1 = secQuestions.get(0).getSecurityAnswer();
		if(StringUtility.isEmptyOrNull(originalAnswer1) ||  (!StringUtility.isEmptyOrNull(originalAnswer1) && !originalAnswer1.equalsIgnoreCase(model.getQuestion1Answer()))) {
			String salt1 = UUID.randomUUID().toString();
			secQuestions.get(0).setSalt(salt1);
			String answer1 = HashUtility.getSecurityQuestionHash(salt1, model.getQuestion1Answer());
			secQuestions.get(0).setSecurityAnswer(answer1);
			secQuestions.get(0).setRowId("0");
			userSecurityQuestionDAO.save(secQuestions.get(0));
		}
		
		String newQuestion2 = model.getQuestion2();
		SecurityQuestions secQue2 = securityQuestionsService.getSecurityQuestionObj(newQuestion2);
		secQuestions.get(1).setSecurityQuestionId(secQue2.getQuestionId());
		secQuestions.get(1).setSecurityQuestions(secQue2);
		String originalAnswer2 = secQuestions.get(1).getSecurityAnswer();
		if(StringUtility.isEmptyOrNull(originalAnswer2) ||  (!StringUtility.isEmptyOrNull(originalAnswer2) && !originalAnswer2.equalsIgnoreCase(model.getQuestion2Answer()))) {
			String salt2 = UUID.randomUUID().toString();
			secQuestions.get(1).setSalt(salt2);
			String answer2 = HashUtility.getSecurityQuestionHash(salt2, model.getQuestion2Answer());
			secQuestions.get(1).setSecurityAnswer(answer2);
			secQuestions.get(1).setRowId("1");
			userSecurityQuestionDAO.save(secQuestions.get(1));
		}

		
		String newQuestion3 = model.getQuestion3();
		SecurityQuestions secQue3 = securityQuestionsService.getSecurityQuestionObj(newQuestion3);
		secQuestions.get(2).setSecurityQuestionId(secQue3.getQuestionId());
		secQuestions.get(2).setSecurityQuestions(secQue3);
		
		String originalAnswer3 = secQuestions.get(2).getSecurityAnswer();
		if(StringUtility.isEmptyOrNull(originalAnswer3) ||  (!StringUtility.isEmptyOrNull(originalAnswer3) && !originalAnswer3.equalsIgnoreCase(model.getQuestion3Answer()))) {
			String salt3 = UUID.randomUUID().toString();
			secQuestions.get(2).setSalt(salt3);
			String answer3 = HashUtility.getSecurityQuestionHash(salt3, model.getQuestion3Answer());
			secQuestions.get(2).setSecurityAnswer(answer3);
			secQuestions.get(2).setRowId("2");
			userSecurityQuestionDAO.save(secQuestions.get(2));
		}

		user.setUserSecurityQuestions(secQuestions);
		userService.saveExisting(user);
		MatUserDetails userDetails = (MatUserDetails) hibernateUserService.loadUserByUsername(user.getLoginId());
		if (userDetails != null) {
			setAuthenticationToken(userDetails);
			result = true;
		} else {
			model.setErrorMessage(MatContext.get().getMessageDelegate()
					.getGenericErrorMessage());
			result = false;
		}
		return result;
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public LoginModel changeTempPassword(String email, String changedpassword) {
		logger.info("Changing the temporary Password");
		logger.info("Changing the temporary Password for user " + email);
		LoginModel loginModel = new LoginModel();
		MatUserDetails userDetails = (MatUserDetails) hibernateUserService.loadUserByUsername(email);
		UserPassword userPassword = userDetails.getUserPassword();
		String hashPassword = userService.getPasswordHash(
				userPassword.getSalt(), changedpassword);
		Date currentDate = new Date();
		Timestamp currentTimeStamp = new Timestamp(currentDate.getTime());
		userPassword.setPassword(hashPassword);
		userPassword.setCreatedDate(currentTimeStamp);
		userPassword.setTemporaryPassword(false);
		hibernateUserService.saveUserDetails(userDetails);
		setAuthenticationToken(userDetails);
		loginModel = loginModelSetter(loginModel, userDetails);
		hibernateUserService.saveUserDetails(userDetails);
		logger.info("Roles for " + email + ": " + userDetails.getRoles().getDescription());
		return loginModel;
	}
	
	/**
	 * First failed login.
	 *
	 * @param userId the user id
	 * @param currentPasswordlockCounter the current password lock counter
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel firstFailedLogin(String userId, int currentPasswordlockCounter, LoginModel validateUserLoginModel, MatUserDetails validateUserMatUserDetails) {
		logger.debug("First failed login attempt");
		// FIRST FAILED LOGIN ATTEMPT
		validateUserLoginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
		validateUserMatUserDetails.getUserPassword().setPasswordlockCounter(currentPasswordlockCounter + 1);
		validateUserMatUserDetails.getUserPassword().setFirstFailedAttemptTime(currentTimeStamp);
		return validateUserLoginModel;
	}

	/**
	 * Increment password lock counter.
	 *
	 * @param userId the user id
	 * @param password the password
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel incrementPassLockCounter(String userId, String password, LoginModel validateUserLoginModel, MatUserDetails validateUserMatUserDetails) {
		logger.debug("Authentication Exception, need to log the failed attempts and increment the lockCounter");
		validateUserLoginModel.setLoginFailedEvent(true);
		int currentPasswordlockCounter = validateUserMatUserDetails.getUserPassword().getPasswordlockCounter();
		logger.info("CurrentPasswordLockCounter value:" + currentPasswordlockCounter);
		if (validateUserMatUserDetails.getLockedOutDate() != null) {
			logger.debug("User locked out");
			validateUserLoginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
		}
		switch (currentPasswordlockCounter) {
			case 0:
				validateUserLoginModel = firstFailedLogin(userId, currentPasswordlockCounter, validateUserLoginModel, validateUserMatUserDetails);
				break;
			case 1:
				validateUserLoginModel = secondFailedLogin(userId, currentPasswordlockCounter, validateUserLoginModel, validateUserMatUserDetails);
				break;
			case 2:
				validateUserLoginModel = thirdFailedLogin(userId,validateUserLoginModel, validateUserMatUserDetails);
				break;
			default:
				// USER LOCKED OUT
				logger.info("USER LOCKED OUT :" + userId);
		} // end of switch.
		hibernateUserService.saveUserDetails(validateUserMatUserDetails);
		return validateUserLoginModel;
	}

	/**
	 * Checks if is already signed in.
	 *
	 * @param userId the user id
	 * @param validateUserLoginModel the validate user login model
	 * @return the login model
	 */
	private LoginModel isAlreadySignedIn(String userId, LoginModel validateUserLoginModel) {
		// USER ALREADY LOGGED IN
		logger.info("USER ALREADY LOGGED IN :" + userId);
		validateUserLoginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedAlreadyLoggedInMessage());
		validateUserLoginModel.setLoginFailedEvent(true);
		return validateUserLoginModel;
	}

	/**
	 * Checks if is login is null.
	 *
	 * @param userId the user id
	 * @param password the password
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel isValidUserDetailsNotNull(String userId, String password, LoginModel validateUserLoginModel, MatUserDetails validateUserMatUserDetails) {
		String hashPassword = userService.getPasswordHash(validateUserMatUserDetails.getUserPassword().getSalt(), password);
		if (validateUserMatUserDetails.getStatus().getStatusId().equals("2")) {
			// REVOKED USER NO
			logger.info("User status is 2, revoked");
			validateUserLoginModel.setLoginFailedEvent(true);
			validateUserLoginModel.setErrorMessage(MatContext.get().getMessageDelegate().getAccountRevokedMessage());
			validateUserLoginModel.setUserId(userId);
			validateUserLoginModel.setLoginId(validateUserMatUserDetails.getLoginId());
		} else if (hashPassword.equalsIgnoreCase(validateUserMatUserDetails.getUserPassword().getPassword())
				&& (validateUserMatUserDetails.getUserPassword().getPasswordlockCounter() < 3)
				&& (validateUserMatUserDetails.getUserPassword().getForgotPwdlockCounter() < 3)) {
			validateUserLoginModel = isValidLogin(userId, password, validateUserLoginModel, validateUserMatUserDetails);
		} else {
			validateUserLoginModel = incrementPassLockCounter(userId, password, validateUserLoginModel, validateUserMatUserDetails);
		} // end of else
		return validateUserLoginModel;
	}

	/**
	 * Checks if is pass matched.
	 *
	 * @param userId the user id
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel isPasswordMatched(String userId, LoginModel validateUserLoginModel, MatUserDetails validateUserMatUserDetails) {
		logger.debug("Password matched, not locked out");
		if (!validateUserMatUserDetails.getUserPassword().isInitial()
				&& !validateUserMatUserDetails.getUserPassword().isTemporaryPassword()) {
			setAuthenticationToken(validateUserMatUserDetails);
		}
		validateUserLoginModel = loginModelSetter(validateUserLoginModel, validateUserMatUserDetails);
		hibernateUserService.saveUserDetails(validateUserMatUserDetails);
		logger.info("Roles for " + userId + ": " + validateUserMatUserDetails.getRoles().getDescription());
		return validateUserLoginModel;
	}

	/**
	 * Checks if is valid login.
	 *
	 * @param userId the user id
	 * @param password the password
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel isValidLogin(String userId, String password, LoginModel validateUserLoginModel, MatUserDetails validateUserMatUserDetails) {
		Date lastSignIn = validateUserMatUserDetails.getSignInDate();
		Date lastSignOut = validateUserMatUserDetails.getSignOutDate();
		boolean alreadySignedIn = MatContext.get().isAlreadySignedIn(lastSignOut, lastSignIn, currentTimeStamp);
		if (alreadySignedIn) {
			validateUserLoginModel = isAlreadySignedIn(userId,validateUserLoginModel);
		} else if (validateUserMatUserDetails.getUserPassword().isInitial()
				|| validateUserMatUserDetails.getUserPassword().isTemporaryPassword()) {
			validateUserLoginModel = temporaryPasswordExpiration(userId, validateUserLoginModel, validateUserMatUserDetails);
		} else {
			validateUserLoginModel = isPasswordMatched(userId, validateUserLoginModel, validateUserMatUserDetails);
		}
		return validateUserLoginModel;
	}
	
	/*
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValidPassword(String userId, String password) {
		logger.info("LoginCredentialServiceImpl: isValidPassword start :  ");
		MatUserDetails userDetails = (MatUserDetails) hibernateUserService.loadUserByUsername(userId);
		if (userDetails != null) {
			String hashPassword = userService.getPasswordHash(userDetails.getUserPassword().getSalt(), password);
			if (hashPassword.equalsIgnoreCase(userDetails.getUserPassword().getPassword())) {
				logger.info("LoginCredentialServiceImpl: isValidPassword end : password matched. ");
				return true;
			} else {
				logger.info("LoginCredentialServiceImpl: isValidPassword end : password mismatched. ");
				return false;
			}
		} else {
			logger.info("LoginCredentialServiceImpl: isValidPassword end : user detail null ");
			return false;
		}
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public LoginModel isValidUser(String userId, String password, String oneTimePassword,String sessionId) {
		LoginModel validateUserLoginModel = new LoginModel();
		MatUserDetails validateUserMatUserDetails = (MatUserDetails) hibernateUserService.loadUserByUsername(userId);
		if(validateUserMatUserDetails !=null) {
			validateUserMatUserDetails.setSessionId(sessionId);
		}
		Date currentDate = new Date();
		currentTimeStamp = new Timestamp(currentDate.getTime());
		validateUserLoginModel = isValidUserIdPassword(userId, password, validateUserLoginModel, validateUserMatUserDetails);
		logger.info("loginModel.isLoginFailedEvent() for userId/password matching:" + validateUserLoginModel.isLoginFailedEvent());
		if (!validateUserLoginModel.isLoginFailedEvent()) {
			validateUserLoginModel = isValid2FactorOTP(userId, oneTimePassword, validateUserLoginModel, validateUserMatUserDetails);
		}
		if (!validateUserLoginModel.isLoginFailedEvent()) {
			onSuccessLogin(userId, validateUserMatUserDetails);
		}
		return validateUserLoginModel;
	}
	
	private LoginModel isValid2FactorOTP(String userId, String oneTimePassword, LoginModel validateUserLoginModel, MatUserDetails validateUserMatUserDetails) {
		
		LoginModel loginModel = validateUserLoginModel;
		
		if(this.matOtpValidatorService != null){
			boolean isValidOTP = this.matOtpValidatorService.validateOTPForUser(userId, oneTimePassword); 
			if(!isValidOTP){
				loginModel.setLoginFailedEvent(true);
				loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
			}
		}else{
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
		}
		
		return loginModel;
	}

	/**
	 * Checks if is valid user id password.
	 *
	 * @param userId the user id
	 * @param password the password
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel isValidUserIdPassword(String userId, String password, LoginModel validateUserLoginModel, MatUserDetails validateUserMatUserDetails) {
		
		if (validateUserMatUserDetails != null) {
			validateUserLoginModel = isValidUserDetailsNotNull(userId, password, validateUserLoginModel, validateUserMatUserDetails);
		} else { // user not found
			validateUserLoginModel.setLoginFailedEvent(true);
			validateUserLoginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
		}
		return validateUserLoginModel;
	}
	
	/*
	 * {@inheritDoc}
	 */
	@Override
	public UserDetails loadUserByUsername(String userId) {
		return userDAO.getUser(userId);
	}
	
	/** Login model setter.
	 * @param loginmodel the login model
	 * @param userDetails the user details
	 * @return the login model */
	private LoginModel loginModelSetter(LoginModel loginmodel, MatUserDetails userDetails) {
		LoginModel loginModel = loginmodel;
		loginModel.setRole(userDetails.getRoles());
		loginModel.setInitialPassword(userDetails.getUserPassword().isInitial());
		loginModel.setTemporaryPassword(userDetails.getUserPassword().isTemporaryPassword());
		loginModel.setUserId(userDetails.getId());
		loginModel.setEmail(userDetails.getEmailAddress());
		loginModel.setLoginId(userDetails.getLoginId());
		loginModel.setFirstName(userDetails.getUsername());
		UserPreference userPreference = userDetails.getUserPreference();
		UserPreferenceDTO userPreferenceDTO = new UserPreferenceDTO();
		if(userPreference != null) {
			userPreferenceDTO.setFreeTextEditorEnabled(userPreference.isFreeTextEditorEnabled());
		}
		loginModel.setUserPreference(userPreferenceDTO);
		return loginModel;
	}

	/**
	 * On success login.
	 *
	 * @param userId the user id
	 * @param validateUserMatUserDetails the validate user mat user details
	 */
	private void onSuccessLogin(String userId,MatUserDetails validateUserMatUserDetails) {
		logger.info(validateUserMatUserDetails.getLoginId() + " has logged in.");
		String s = "\nlogin_success\n";
		logger.info(s);
	}
	
	/**
	 * Second failed login.
	 *
	 * @param userId the user id
	 * @param currentPasswordlockCounter the current passwordlock counter
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel secondFailedLogin(String userId, int currentPasswordlockCounter, LoginModel validateUserLoginModel, MatUserDetails validateUserMatUserDetails) {
		logger.debug("Second failed login attempt");
		// SECOND FAILED LOGIN ATTEMPT
		Timestamp firstFailedAttemptTime = validateUserMatUserDetails.getUserPassword().getFirstFailedAttemptTime();
		long difference = currentTimeStamp.getTime()
				- firstFailedAttemptTime.getTime();
		long minuteDifference = difference / (60 * 1000);
		if (minuteDifference > 15) {
			validateUserLoginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
			logger.info("MinuteDifference:" + minuteDifference);
			logger.info("Since the minuteDifference is greater than 15 minutes, update the failedAttemptTime");
			validateUserMatUserDetails.getUserPassword().setFirstFailedAttemptTime(currentTimeStamp);
		} else {
			validateUserLoginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
			validateUserMatUserDetails.getUserPassword().setPasswordlockCounter(currentPasswordlockCounter + 1);
		}
		return validateUserLoginModel;
	}
	
	/** Sets the authentication token.
	 * 
	 * @param userDetails the new authentication token */
	private void setAuthenticationToken(MatUserDetails userDetails) {
		logger.debug("Setting authentication token");
		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getId(), userDetails.getUserPassword().getPassword(), userDetails.getAuthorities());
		
		// US 170. set additional details for history event
		((UsernamePasswordAuthenticationToken) auth).setDetails(userDetails);
		SecurityContext sc = new SecurityContextImpl();
		sc.setAuthentication(auth);
		SecurityContextHolder.setContext(sc);
	}
	
	/*
	 * {@inheritDoc}
	 */
	@Override
	public void signOut() {
		SecurityContextHolder.clearContext();
	}

	/**
	 * Temp pass expiration.
	 *
	 * @param userId the user id
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel temporaryPasswordExpiration(String userId, LoginModel validateUserLoginModel, MatUserDetails validateUserMatUserDetails) {
		//If this is a temporary or initial password, check for 5 day limit
		Date createDate = validateUserMatUserDetails.getUserPassword().getCreatedDate();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(createDate);
		calendar.add(Calendar.DAY_OF_MONTH, 5);
		Calendar calendarForToday = GregorianCalendar.getInstance();
		if (calendarForToday.after(calendar)) {
			logger.info("USER Temp or Initial Password has expired :" + userId);
			validateUserLoginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedTempPasswordExpiredMessage());
			validateUserMatUserDetails.setLockedOutDate(currentTimeStamp);
			validateUserLoginModel.setLoginFailedEvent(true);
			hibernateUserService.saveUserDetails(validateUserMatUserDetails);
		} else {
			validateUserLoginModel = loginModelSetter(validateUserLoginModel, validateUserMatUserDetails);
			hibernateUserService.saveUserDetails(validateUserMatUserDetails);
			logger.info("Roles for " + userId + ": " + validateUserMatUserDetails.getRoles().getDescription());
		}
		return validateUserLoginModel;
	}
	
	/**
	 * Third failed login.
	 *
	 * @param userId the user id
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel thirdFailedLogin(String userId, LoginModel validateUserLoginModel, MatUserDetails validateUserMatUserDetails) {
		
		// USER THIRD FAILED LOGIN ATTEMPT
		logger.info("USER THIRD FAILED LOGIN ATTEMPT :" + userId);
		Timestamp updatedFailedAttemptTime = validateUserMatUserDetails.getUserPassword().getFirstFailedAttemptTime();
		long timeDifference = currentTimeStamp.getTime()
				- updatedFailedAttemptTime.getTime();
		long minDifference = timeDifference / (60 * 1000);
		if (minDifference > 15) {
			logger.debug("MinuteDifference:" + minDifference);
			logger.debug("Since the minuteDifference is greater than 15 minutes, update the failedAttemptTime");
			validateUserLoginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
			validateUserMatUserDetails.getUserPassword()
			.setFirstFailedAttemptTime(currentTimeStamp);
			validateUserMatUserDetails.getUserPassword().setPasswordlockCounter(1);
		} else {
			validateUserLoginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
			validateUserMatUserDetails.setLockedOutDate(currentTimeStamp);
			validateUserMatUserDetails.getUserPassword().setPasswordlockCounter(3);
			logger.debug("Locking user out");
		}
		return validateUserLoginModel;
	}

	public TwoFactorValidationService getTwoFactorValidationService() {
		return matOtpValidatorService;
	}

	public void setTwoFactorValidationService(TwoFactorValidationService twoFactorValidationService) {
		this.matOtpValidatorService = twoFactorValidationService;
	}
}