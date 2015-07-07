package mat.server.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import mat.client.login.LoginModel;
import mat.client.shared.MatContext;
import mat.dao.UserDAO;
import mat.model.SecurityQuestions;
import mat.model.User;
import mat.model.UserPassword;
import mat.model.UserPasswordHistory;
import mat.model.UserSecurityQuestion;
import mat.server.hibernate.HibernateUserDetailService;
import mat.server.model.MatUserDetails;
import mat.server.service.LoginCredentialService;
import mat.server.service.SecurityQuestionsService;
import mat.server.service.UserService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;

// TODO: Auto-generated Javadoc
/** The Class LoginCredentialServiceImpl. */
public class LoginCredentialServiceImpl implements LoginCredentialService {
	
	/** The current time stamp. */
	private static Timestamp currentTimeStamp;
	/** The Constant logger. */
	private static final Log logger = LogFactory
			.getLog(LoginCredentialServiceImpl.class);
	/** The hibernate user service. */
	@Autowired
	private HibernateUserDetailService hibernateUserService;
	/** The security questions service. */
	@Autowired
	private SecurityQuestionsService securityQuestionsService;
	/** The user dao. */
	@Autowired
	private UserDAO userDAO;
	/** The user service. */
	@Autowired
	private UserService userService;
	
	/*
	 * (non-Javadoc)
	 * @see mat.server.service.LoginCredentialService#changePasswordSecurityAnswers(mat.client.login.LoginModel)
	 */
	@Override
	public boolean changePasswordSecurityAnswers(LoginModel model) {
		logger.info("First time login, changing password and security answers");
		logger.info("Changing password");
		boolean result = false;
		User user = userService.getById(model.getUserId());
		
		//before setting new Password we need to store the old password in password History
		List<UserPasswordHistory> pwdHisList = userService.getUpdatedPasswordHistoryList(user, false);
		user.getPasswordHistory().clear();
		user.getPasswordHistory().addAll(pwdHisList);
		//user.setPasswordHistory(pwdHisList);
		userService.setUserPassword(user, model.getPassword(), false);
		user.getPassword().setInitial(false);
		logger.info("Saving security questions");
		List<UserSecurityQuestion> secQuestions = user.getSecurityQuestions();
		while (secQuestions.size() < 3) {
			UserSecurityQuestion newQuestion = new UserSecurityQuestion();
			secQuestions.add(newQuestion);
		}
		String newQuestion1 = model.getQuestion1();
		SecurityQuestions secQue1 = securityQuestionsService
				.getSecurityQuestionObj(newQuestion1);
		secQuestions.get(0).setSecurityQuestionId(secQue1.getQuestionId());
		secQuestions.get(0).setSecurityQuestions(secQue1);
		secQuestions.get(0).setSecurityAnswer(model.getQuestion1Answer());
		String newQuestion2 = model.getQuestion2();
		SecurityQuestions secQue2 = securityQuestionsService
				.getSecurityQuestionObj(newQuestion2);
		secQuestions.get(1).setSecurityQuestionId(secQue2.getQuestionId());
		secQuestions.get(1).setSecurityQuestions(secQue2);
		secQuestions.get(1).setSecurityAnswer(model.getQuestion2Answer());
		String newQuestion3 = model.getQuestion3();
		SecurityQuestions secQue3 = securityQuestionsService
				.getSecurityQuestionObj(newQuestion3);
		secQuestions.get(2).setSecurityQuestionId(secQue3.getQuestionId());
		secQuestions.get(2).setSecurityQuestions(secQue3);
		secQuestions.get(2).setSecurityAnswer(model.getQuestion3Answer());
		user.setSecurityQuestions(secQuestions);
		userService.saveExisting(user);
		MatUserDetails userDetails = (MatUserDetails) hibernateUserService
				.loadUserByUsername(user.getLoginId());
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
	 * (non-Javadoc)
	 * @see mat.server.service.LoginCredentialService#changeTempPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public LoginModel changeTempPassword(String email, String changedpassword) {
		logger.info("Changing the temporary Password");
		logger.info("Changing the temporary Password for user " + email);
		LoginModel loginModel = new LoginModel();
		MatUserDetails userDetails = (MatUserDetails) hibernateUserService
				.loadUserByUsername(email);
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
		logger.info("Roles for " + email + ": "
				+ userDetails.getRoles().getDescription());
		return loginModel;
	}
	/* Checks for first login failed attempt
	 * */
	/**
	 * First failed login.
	 *
	 * @param userId the user id
	 * @param currentPasswordlockCounter the current password lock counter
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel firstFailedLogin(String userId, int currentPasswordlockCounter,LoginModel validateUserLoginModel
			,MatUserDetails validateUserMatUserDetails) {
		logger.debug("First failed login attempt");
		// FIRST FAILED LOGIN ATTEMPT
		validateUserLoginModel.setErrorMessage(MatContext.get()
				.getMessageDelegate().getLoginFailedMessage());
		validateUserMatUserDetails.getUserPassword().setPasswordlockCounter(
				currentPasswordlockCounter + 1);
		validateUserMatUserDetails.getUserPassword().setFirstFailedAttemptTime(
				currentTimeStamp);
		return validateUserLoginModel;
	}
	//to check the number of failed login attempts and increment the password lock counter
	/**
	 * Increment password lock counter.
	 *
	 * @param userId the user id
	 * @param password the password
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel incrementPassLockCounter(String userId, String password,LoginModel validateUserLoginModel
			,MatUserDetails validateUserMatUserDetails) {
		logger.debug("Authentication Exception, need to log the failed attempts and increment the lockCounter");
		validateUserLoginModel.setLoginFailedEvent(true);
		//validateUserLoginModel.setUserId(userId);
		int currentPasswordlockCounter = validateUserMatUserDetails.getUserPassword()
				.getPasswordlockCounter();
		logger.info("CurrentPasswordLockCounter value:"
				+ currentPasswordlockCounter);
		if (validateUserMatUserDetails.getLockedOutDate() != null) {
			logger.debug("User locked out");
			validateUserLoginModel.setErrorMessage(MatContext.get()
					.getMessageDelegate().getLoginFailedMessage());
		}
		switch (currentPasswordlockCounter) {
			case 0:
				validateUserLoginModel = firstFailedLogin(userId, currentPasswordlockCounter,validateUserLoginModel,
						validateUserMatUserDetails);
				break;
			case 1:
				validateUserLoginModel = secondFailedLogin(userId, currentPasswordlockCounter,validateUserLoginModel,
						validateUserMatUserDetails);
				break;
			case 2:
				validateUserLoginModel = thirdFailedLogin(userId,validateUserLoginModel,
						validateUserMatUserDetails);
				break;
			default:
				// USER LOCKED OUT
				logger.info("USER LOCKED OUT :" + userId);
		} // end of switch.
		hibernateUserService.saveUserDetails(validateUserMatUserDetails);
		return validateUserLoginModel;
	}
	//Invokes if the user is already signed in
	/**
	 * Checks if is already signed in.
	 *
	 * @param userId the user id
	 * @param validateUserLoginModel the validate user login model
	 * @return the login model
	 */
	private LoginModel isAlreadySignedIn(String userId,LoginModel validateUserLoginModel) {
		// USER ALREADY LOGGED IN
		logger.info("USER ALREADY LOGGED IN :" + userId);
		validateUserLoginModel.setErrorMessage(MatContext.get()
				.getMessageDelegate()
				.getLoginFailedAlreadyLoggedInMessage());
		validateUserLoginModel.setLoginFailedEvent(true);
		return validateUserLoginModel;
	}
	//checks if the validUserDetails is not null and invokes isValidLogin() and incrementPassLockCounter() methods
	/**
	 * Checks if is login is null.
	 *
	 * @param userId the user id
	 * @param password the password
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel isValidUserDetailsNotNull(String userId, String password,LoginModel validateUserLoginModel,
			MatUserDetails validateUserMatUserDetails) {
		String hashPassword = userService.getPasswordHash(validateUserMatUserDetails
				.getUserPassword().getSalt(), password);
		if (validateUserMatUserDetails.getStatus().getId().equals("2")) {
			// REVOKED USER NO
			logger.info("User status is 2, revoked");
			validateUserLoginModel.setLoginFailedEvent(true);
			validateUserLoginModel.setErrorMessage(MatContext.get()
					.getMessageDelegate().getAccountRevokedMessage());
			validateUserLoginModel.setUserId(userId);
			// loginModel.setEmail(userDetails.getEmailAddress());
			validateUserLoginModel.setLoginId(validateUserMatUserDetails.getLoginId());
		} else if (hashPassword.equalsIgnoreCase(validateUserMatUserDetails
				.getUserPassword().getPassword())
				&& (validateUserMatUserDetails.getUserPassword().getPasswordlockCounter() < 3)
				&& (validateUserMatUserDetails.getUserPassword().getForgotPwdlockCounter() < 3)) {
			validateUserLoginModel = isValidLogin(userId, password,validateUserLoginModel,
					validateUserMatUserDetails);
		} else {
			validateUserLoginModel = incrementPassLockCounter(userId, password,validateUserLoginModel,
					validateUserMatUserDetails);
		} // end of else
		return validateUserLoginModel;
	}
	//checks to see if password is matched and sets AuthenticationToken for the validateUserMatUserDetails
	/**
	 * Checks if is pass matched.
	 *
	 * @param userId the user id
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel isPasswordMatched(String userId,LoginModel validateUserLoginModel,
			MatUserDetails validateUserMatUserDetails) {
		logger.debug("Password matched, not locked out");
		if (!validateUserMatUserDetails.getUserPassword().isInitial()
				&& !validateUserMatUserDetails.getUserPassword()
				.isTemporaryPassword()) {
			setAuthenticationToken(validateUserMatUserDetails);
		}
		validateUserLoginModel = loginModelSetter(validateUserLoginModel, validateUserMatUserDetails);
		// userDetails.setSignInDate(currentTimeStamp);
		hibernateUserService.saveUserDetails(validateUserMatUserDetails);
		logger.info("Roles for " + userId + ": "
				+ validateUserMatUserDetails.getRoles().getDescription());
		return validateUserLoginModel;
	}
	//check to see if it is a valid login and invokes methods to validate pass and temp Password Expiration
	/**
	 * Checks if is valid login.
	 *
	 * @param userId the user id
	 * @param password the password
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel isValidLogin(String userId, String password,LoginModel validateUserLoginModel,
			MatUserDetails validateUserMatUserDetails) {
		Date lastSignIn = validateUserMatUserDetails.getSignInDate();
		Date lastSignOut = validateUserMatUserDetails.getSignOutDate();
		boolean alreadySignedIn = MatContext.get().isAlreadySignedIn(
				lastSignOut, lastSignIn, currentTimeStamp);
		if (alreadySignedIn) {
			validateUserLoginModel = isAlreadySignedIn(userId,validateUserLoginModel);
		} else if (validateUserMatUserDetails.getUserPassword().isInitial()
				|| validateUserMatUserDetails.getUserPassword()
				.isTemporaryPassword()) {
			validateUserLoginModel = temporaryPasswordExpiration(userId,validateUserLoginModel,
					validateUserMatUserDetails);
		} else {
			validateUserLoginModel = isPasswordMatched(userId,validateUserLoginModel,
					validateUserMatUserDetails);
		}
		return validateUserLoginModel;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.server.service.LoginCredentialService#isValidPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isValidPassword(String userId, String password) {
		logger.info("LoginCredentialServiceImpl: isValidPassword start :  ");
		MatUserDetails userDetails = (MatUserDetails) hibernateUserService
				.loadUserByUsername(userId);
		if (userDetails != null) {
			String hashPassword = userService.getPasswordHash(userDetails
					.getUserPassword().getSalt(), password);
			if (hashPassword.equalsIgnoreCase(userDetails.getUserPassword()
					.getPassword())) {
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
	//to check the user login validation
	/*
	 * (non-Javadoc)
	 * @see mat.server.service.LoginCredentialService#isValidUser(java.lang.String, java.lang.String)
	 */
	@Override
	public LoginModel isValidUser(String userId, String password) {
		LoginModel validateUserLoginModel = new LoginModel();
		MatUserDetails validateUserMatUserDetails = (MatUserDetails) hibernateUserService
				.loadUserByUsername(userId);
		Date currentDate = new Date();
		currentTimeStamp = new Timestamp(currentDate.getTime());
		validateUserLoginModel = isValidUserIdPassword(userId, password, validateUserLoginModel,
				validateUserMatUserDetails);
		logger.info("loginModel.isLoginFailedEvent():" + validateUserLoginModel.isLoginFailedEvent());
		if (!validateUserLoginModel.isLoginFailedEvent()) {
			onSuccessLogin(userId, validateUserMatUserDetails);
		}
		return validateUserLoginModel;
	}
	//to check for the user password validity
	/**
	 * Checks if is valid user id password.
	 *
	 * @param userId the user id
	 * @param password the password
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel isValidUserIdPassword(String userId, String password,LoginModel validateUserLoginModel,
			MatUserDetails validateUserMatUserDetails) {
		
		if (validateUserMatUserDetails != null) {
			validateUserLoginModel = isValidUserDetailsNotNull(userId, password,validateUserLoginModel,
					validateUserMatUserDetails);
		} else { // user not found
			validateUserLoginModel.setLoginFailedEvent(true);
			validateUserLoginModel.setErrorMessage(MatContext.get().getMessageDelegate()
					.getLoginFailedMessage());
		}
		return validateUserLoginModel;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.server.service.LoginCredentialService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String userId) {
		// UserDAO userDAO = (UserDAO)context.getBean("userDAO");
		return userDAO.getUser(userId);
	}
	/** Login model setter.
	 * @param loginmodel the login model
	 * @param userDetails the user details
	 * @return the login model */
	private LoginModel loginModelSetter(LoginModel loginmodel,
			MatUserDetails userDetails) {
		LoginModel loginModel = loginmodel;
		loginModel.setRole(userDetails.getRoles());
		loginModel
		.setInitialPassword(userDetails.getUserPassword().isInitial());
		loginModel.setTemporaryPassword(userDetails.getUserPassword()
				.isTemporaryPassword());
		loginModel.setUserId(userDetails.getId());
		loginModel.setEmail(userDetails.getEmailAddress());
		loginModel.setLoginId(userDetails.getLoginId());
		loginModel.setFirstName(userDetails.getUsername());
		return loginModel;
	}
	//invokes On Success User Login and displays ChartReport
	/**
	 * On success login.
	 *
	 * @param userId the user id
	 * @param validateUserMatUserDetails the validate user mat user details
	 */
	private void onSuccessLogin(String userId,MatUserDetails validateUserMatUserDetails) {
		logger.info(validateUserMatUserDetails.getLoginId() + " has logged in.");
		String s = "\nlogin_success\n";
		/*String chartReport = "CHARTREPORT";
		List<MemoryPoolMXBean> pbeans = ManagementFactory
				.getMemoryPoolMXBeans();
		for (MemoryPoolMXBean bean : pbeans) {
			MemoryUsage mused = bean.getPeakUsage();
			for (String x : bean.getMemoryManagerNames()) {
				s += "MemoryManager " + x + ":\n";
			}
			s += "poolInit:      \t" + mused.getInit() + " ";
			s += "poolPeak:      \t" + mused.getUsed() + " ";
			s += "poolMax:       \t" + mused.getMax() + "\n\n";
		}
		
		MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
		// bean.setVerbose(true);
		
		MemoryUsage mu = bean.getNonHeapMemoryUsage();
		// [MemoryUsage]
		// \nNonHeap|init:<<val>>|committed:<<val>>|max:<<val>>|used:<<val>>
		// \nHeap|init:<<val>>|committed:<<val>>|max:<<val>>|used:<<val>>
		
		s += "PermGen init:      \t" + mu.getInit() + "\n";
		s += "PermGen committed: \t" + mu.getCommitted() + " ";
		s += "PermGen Max:       \t" + mu.getMax() + " ";
		s += "PermGen Used:      \t" + mu.getUsed() + " ";
		chartReport += " " + mu.getUsed();
		// ManagementFactory.
		
		mu = bean.getHeapMemoryUsage();
		// Heap|init:<<val>>|committed:<<val>>|max:<<val>>|used:<<val>>
		s += "Heap init:      \t" + mu.getInit() + " ";
		s += "Heap committed: \t" + mu.getCommitted() + " ";
		s += "Heap Max:       \t" + mu.getMax() + " ";
		s += "Heap Used:      \t" + mu.getUsed() + "\n";
		
		chartReport += " " + mu.getUsed();
		
		ThreadMXBean tBean = ManagementFactory.getThreadMXBean();
		s += "Threads running:   \t" + tBean.getThreadCount() + "\n";
		chartReport += " " + tBean.getThreadCount();
		chartReport += " " + System.currentTimeMillis();
		
		// Log logger = LogFactory.getLog(PreventCachingFilter.class);
		chartReport += "\n";
		s += "/login_success\n" + chartReport;*/
		logger.info(s);
	}
	
	//Checks for Second login failed attempt
	/**
	 * Second failed login.
	 *
	 * @param userId the user id
	 * @param currentPasswordlockCounter the current passwordlock counter
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel secondFailedLogin(String userId, int currentPasswordlockCounter,LoginModel validateUserLoginModel,
			MatUserDetails validateUserMatUserDetails) {
		logger.debug("Second failed login attempt");
		// SECOND FAILED LOGIN ATTEMPT
		Timestamp firstFailedAttemptTime = validateUserMatUserDetails
				.getUserPassword().getFirstFailedAttemptTime();
		long difference = currentTimeStamp.getTime()
				- firstFailedAttemptTime.getTime();
		long minuteDifference = difference / (60 * 1000);
		if (minuteDifference > 15) {
			validateUserLoginModel.setErrorMessage(MatContext.get()
					.getMessageDelegate().getLoginFailedMessage());
			logger.info("MinuteDifference:" + minuteDifference);
			logger.info("Since the minuteDifference is greater than 15 minutes, update the failedAttemptTime");
			validateUserMatUserDetails.getUserPassword()
			.setFirstFailedAttemptTime(currentTimeStamp);
		} else {
			validateUserLoginModel.setErrorMessage(MatContext.get()
					.getMessageDelegate()
					.getLoginFailedMessage());
			validateUserMatUserDetails.getUserPassword().setPasswordlockCounter(
					currentPasswordlockCounter + 1);
		}
		return validateUserLoginModel;
	}
	
	/** Sets the authentication token.
	 * 
	 * @param userDetails the new authentication token */
	private void setAuthenticationToken(MatUserDetails userDetails) {
		logger.debug("Setting authentication token");
		Authentication auth = new UsernamePasswordAuthenticationToken(
				userDetails.getId(), userDetails.getUserPassword()
				.getPassword(), userDetails.getAuthorities());
		
		// US 170. set additional details for history event
		((UsernamePasswordAuthenticationToken) auth).setDetails(userDetails);
		SecurityContext sc = new SecurityContextImpl();
		sc.setAuthentication(auth);
		SecurityContextHolder.setContext(sc);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.server.service.LoginCredentialService#signOut()
	 */
	@Override
	public void signOut() {
		// as of US212 update to user sign out date
		// will be taken care of by invocation of
		// MatContext.stopUserLockUpdate
		/*
		 * if(userid != null) { Date currentDate = new Date(); Timestamp
		 * currentTimeStamp = new Timestamp(currentDate.getTime()); User user =
		 * userService.getById(userid);
		 * if(user != null){ user.setSignOutDate(currentTimeStamp);
		 * userService.saveExisting(user); } }
		 */
		SecurityContextHolder.clearContext();
	}
	//to check the 5 day limit for temporary password expiration
	/**
	 * Temp pass expiration.
	 *
	 * @param userId the user id
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel temporaryPasswordExpiration(String userId,LoginModel validateUserLoginModel,
			MatUserDetails validateUserMatUserDetails) {
		//If this is a temporary or initial password, check for 5 day limit
		Date createDate = validateUserMatUserDetails.getUserPassword().getCreatedDate();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(createDate);
		calendar.add(Calendar.DAY_OF_MONTH, 5);
		Calendar calendarForToday = GregorianCalendar.getInstance();
		if (calendarForToday.after(calendar)) {
			logger.info("USER Temp or Initial Password has expired :" + userId);
			validateUserLoginModel.setErrorMessage(MatContext.get()
					.getMessageDelegate()
					.getLoginFailedTempPasswordExpiredMessage());
			validateUserMatUserDetails.setLockedOutDate(currentTimeStamp);
			validateUserLoginModel.setLoginFailedEvent(true);
			hibernateUserService.saveUserDetails(validateUserMatUserDetails);
		} else {
			validateUserLoginModel = loginModelSetter(validateUserLoginModel, validateUserMatUserDetails);
			// userDetails.setSignInDate(currentTimeStamp);
			hibernateUserService.saveUserDetails(validateUserMatUserDetails);
			logger.info("Roles for " + userId + ": "
					+ validateUserMatUserDetails.getRoles().getDescription());
		}
		return validateUserLoginModel;
	}
	
	//Checks for Third login failed attempt and locks the user account.
	/**
	 * Third failed login.
	 *
	 * @param userId the user id
	 * @param validateUserLoginModel the validate user login model
	 * @param validateUserMatUserDetails the validate user mat user details
	 * @return the login model
	 */
	private LoginModel thirdFailedLogin(String userId,LoginModel validateUserLoginModel,
			MatUserDetails validateUserMatUserDetails) {
		
		// USER THIRD FAILED LOGIN ATTEMPT
		logger.info("USER THIRD FAILED LOGIN ATTEMPT :" + userId);
		Timestamp updatedFailedAttemptTime = validateUserMatUserDetails
				.getUserPassword().getFirstFailedAttemptTime();
		long timeDifference = currentTimeStamp.getTime()
				- updatedFailedAttemptTime.getTime();
		long minDifference = timeDifference / (60 * 1000);
		if (minDifference > 15) {
			logger.debug("MinuteDifference:" + minDifference);
			logger.debug("Since the minuteDifference is greater than 15 minutes, update the failedAttemptTime");
			validateUserLoginModel.setErrorMessage(MatContext.get()
					.getMessageDelegate().getLoginFailedMessage());
			validateUserMatUserDetails.getUserPassword()
			.setFirstFailedAttemptTime(currentTimeStamp);
			validateUserMatUserDetails.getUserPassword().setPasswordlockCounter(1);
		} else {
			validateUserLoginModel.setErrorMessage(MatContext.get()
					.getMessageDelegate()
					.getLoginFailedMessage());
			validateUserMatUserDetails.setLockedOutDate(currentTimeStamp);
			validateUserMatUserDetails.getUserPassword().setPasswordlockCounter(3);
			logger.debug("Locking user out");
		}
		return validateUserLoginModel;
	}
	
}