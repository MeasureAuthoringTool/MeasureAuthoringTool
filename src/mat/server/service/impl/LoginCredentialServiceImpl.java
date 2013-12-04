package mat.server.service.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.sql.Timestamp;
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

public class LoginCredentialServiceImpl implements LoginCredentialService {

	private static final Log logger = LogFactory
			.getLog(LoginCredentialServiceImpl.class);
	private static LoginModel loginModel1;
	private static MatUserDetails userDetails1;
	private static Timestamp currentTimeStamp;

	@Autowired
	private HibernateUserDetailService hibernateUserService;

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityQuestionsService securityQuestionsService;

	@Autowired
	UserDAO userDAO;

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

	//user login validation
	@Override
	public LoginModel isValidUser(String userId, String password) {
		
		loginModel1=new LoginModel();
		Date currentDate=new Date();
		currentTimeStamp = new Timestamp(currentDate.getTime());
		loginModel1=isValidIdPass(userId,password);
		logger.info("loginModel.isLoginFailedEvent():"+loginModel1.isLoginFailedEvent());
		if (!loginModel1.isLoginFailedEvent()) {
			onSuccessLogin(userId);
		}
		return loginModel1;
	}
	
	
	//to check for password validity
	private LoginModel isValidIdPass(String userId, String password)
	{
		userDetails1 = (MatUserDetails) hibernateUserService
				.loadUserByUsername(userId);
			
		if (userDetails1 != null) {
			loginModel1=isLoginIsNull(userId,password);
         } else { // user not found
        	 loginModel1.setLoginFailedEvent(true);
        	 loginModel1.setErrorMessage(MatContext.get().getMessageDelegate()
					.getLoginFailedMessage());
		}
	return loginModel1;
	}
	
	//to check if login is null
	private LoginModel isLoginIsNull(String userId, String password){
		
		String hashPassword = userService.getPasswordHash(userDetails1
				.getUserPassword().getSalt(), password);
		if (userDetails1.getStatus().getId().equals("2")) {
			// REVOKED USER NO
			logger.info("User status is 2, revoked");
			loginModel1.setLoginFailedEvent(true);
			loginModel1.setErrorMessage(MatContext.get()
					.getMessageDelegate().getAccountRevokedMessage());
			loginModel1.setUserId(userId);
			// loginModel.setEmail(userDetails.getEmailAddress());
			loginModel1.setLoginId(userDetails1.getLoginId());
		} else if (hashPassword.equalsIgnoreCase(userDetails1
				.getUserPassword().getPassword())
				&& userDetails1.getUserPassword().getPasswordlockCounter() < 3
				&& userDetails1.getUserPassword().getForgotPwdlockCounter() < 3) {
			loginModel1=isValidLogin(userId,password);

			} else {
				loginModel1=incrPassLockCounter(userId,password);
			
			}// end of else

	return loginModel1;
	}
	
	//to check number of failed login attempts
	private LoginModel incrPassLockCounter(String userId, String password){
		 
			logger.debug("Authentication Exception, need to log the failed attempts and increment the lockCounter");
			loginModel1.setLoginFailedEvent(true);
			loginModel1.setUserId(userId);
			int currentPasswordlockCounter = userDetails1.getUserPassword()
					.getPasswordlockCounter();
			logger.info("CurrentPasswordLockCounter value:"
					+ currentPasswordlockCounter);
			if (userDetails1.getLockedOutDate() != null) {
				logger.debug("User locked out");
				loginModel1.setErrorMessage(MatContext.get()
						.getMessageDelegate().getAccountLocked2Message());
			}
			switch (currentPasswordlockCounter) {
			case 0:
				loginModel1=firstFailedLogin(userId,currentPasswordlockCounter);
				break;
			case 1:
				loginModel1=secondFailedLogin(userId, currentPasswordlockCounter);
				break;
			case 2:
				loginModel1=thirdFailedLogin(userId);
				break;
			default:
				// USER LOCKED OUT
				logger.info("USER LOCKED OUT :" + userId);
			}// end of switch
			hibernateUserService.saveUserDetails(userDetails1);

		 return loginModel1;
	 }
	
	   //first failed login attempt
	private LoginModel firstFailedLogin(String userId,int currentPasswordlockCounter){
			
			logger.debug("First failed login attempt");
			// FIRST FAILED LOGIN ATTEMPT
			loginModel1.setErrorMessage(MatContext.get()
					.getMessageDelegate().getLoginFailedMessage());
			userDetails1.getUserPassword().setPasswordlockCounter(
					currentPasswordlockCounter + 1);
			userDetails1.getUserPassword().setFirstFailedAttemptTime(
					currentTimeStamp);
			return loginModel1;
	 }
		
		//second failed login attempt
	private LoginModel secondFailedLogin(String userId,int currentPasswordlockCounter){
			
			logger.debug("Second failed login attempt");
			// SECOND FAILED LOGIN ATTEMPT
			Timestamp firstFailedAttemptTime = userDetails1
					.getUserPassword().getFirstFailedAttemptTime();
			long difference = currentTimeStamp.getTime()
					- firstFailedAttemptTime.getTime();
			long MinuteDifference = difference / (60 * 1000);
			if (MinuteDifference > 15) {
				loginModel1.setErrorMessage(MatContext.get()
						.getMessageDelegate().getLoginFailedMessage());
				logger.info("MinuteDifference:" + MinuteDifference);
				logger.info("Since the minuteDifference is greater than 15 minutes, update the failedAttemptTime");
				userDetails1.getUserPassword()
						.setFirstFailedAttemptTime(currentTimeStamp);
			} else {
				loginModel1.setErrorMessage(MatContext.get()
						.getMessageDelegate()
						.getSecondAttemptFailedMessage());
				userDetails1.getUserPassword().setPasswordlockCounter(
						currentPasswordlockCounter + 1);
			}
			return loginModel1;
		}
       
		//third failed login attempt
	private LoginModel thirdFailedLogin(String userId){
		
			// USER THIRD FAILED LOGIN ATTEMPT
			logger.info("USER THIRD FAILED LOGIN ATTEMPT :" + userId);
			Timestamp updatedFailedAttemptTime = userDetails1
					.getUserPassword().getFirstFailedAttemptTime();
			long timeDifference = currentTimeStamp.getTime()
					- updatedFailedAttemptTime.getTime();
			long minDifference = timeDifference / (60 * 1000);
			if (minDifference > 15) {
				logger.debug("MinuteDifference:" + minDifference);
				logger.debug("Since the minuteDifference is greater than 15 minutes, update the failedAttemptTime");
				loginModel1.setErrorMessage(MatContext.get()
						.getMessageDelegate().getLoginFailedMessage());
				userDetails1.getUserPassword()
						.setFirstFailedAttemptTime(currentTimeStamp);
				userDetails1.getUserPassword().setPasswordlockCounter(1);
			} else {
				loginModel1.setErrorMessage(MatContext.get()
						.getMessageDelegate()
						.getAccountLocked2Message());
				userDetails1.setLockedOutDate(currentTimeStamp);
				userDetails1.getUserPassword().setPasswordlockCounter(3);
				logger.debug("Locking user out");
			}
			return loginModel1;
		}
	 
	 //check to see if it is a valid login
	private LoginModel isValidLogin(String userId,String password){
		
		Date lastSignIn = userDetails1.getSignInDate();
		Date lastSignOut = userDetails1.getSignOutDate();

		boolean alreadySignedIn = MatContext.get().isAlreadySignedIn(
				lastSignOut, lastSignIn, currentTimeStamp);

		if (alreadySignedIn) {
			loginModel1=isAlreadySignedIn(userId);
			
		}else if(userDetails1.getUserPassword().isInitial()
				|| userDetails1.getUserPassword()
				.isTemporaryPassword()){
			
			loginModel1=tempPassExpiration(userId);
			
			}else {
				loginModel1=isPassMatched(userId);
		}
	return loginModel1;
	}
	 
	//check if already signed in
	private LoginModel isAlreadySignedIn(String userId){
		
		// USER ALREADY LOGGED IN
					logger.info("USER ALREADY LOGGED IN :" + userId);
					loginModel1.setErrorMessage(MatContext.get()
							.getMessageDelegate()
							.getLoginFailedAlreadyLoggedInMessage());
					loginModel1.setLoginFailedEvent(true);
		 return loginModel1;
	 }
	 
	 //to check the expiration of intial or temp password
	private LoginModel tempPassExpiration(String userId){
		
		//If this is a temporary or initial password, check for 5 day limit
		Date createDate = userDetails1.getUserPassword().getCreatedDate();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(createDate);
		calendar.roll(Calendar.DAY_OF_MONTH, 5);
		
		Calendar calendarForToday = GregorianCalendar.getInstance();
		if(calendarForToday.after(calendar)){
			logger.info("USER Temp or Initial Password has expired :" + userId);
			loginModel1.setErrorMessage(MatContext.get()
					.getMessageDelegate()
					.getLoginFailedTempPasswordExpiredMessage());
			loginModel1.setLoginFailedEvent(true);
		}else{
			loginModel1 = loginModelSetter(loginModel1, userDetails1);
			// userDetails.setSignInDate(currentTimeStamp);
			hibernateUserService.saveUserDetails(userDetails1);
			logger.info("Roles for " + userId + ": "
					+ userDetails1.getRoles().getDescription());
		}
		return loginModel1;
	}

	//check if password is matched
	private LoginModel isPassMatched(String userId){

		logger.debug("Password matched, not locked out");
		if (!userDetails1.getUserPassword().isInitial()
				&& !userDetails1.getUserPassword()
						.isTemporaryPassword()) {
			setAuthenticationToken(userDetails1);
		}
		loginModel1 = loginModelSetter(loginModel1, userDetails1);
		// userDetails.setSignInDate(currentTimeStamp);
		hibernateUserService.saveUserDetails(userDetails1);
		logger.info("Roles for " + userId + ": "
				+ userDetails1.getRoles().getDescription());
		return loginModel1;
	}
	
	//to check login failed attempts
	private void onSuccessLogin(String userId){
		
		logger.info(userDetails1.getLoginId() + " has logged in.");
		String s = "\nlogin_success\n";

		String chartReport = "CHARTREPORT";
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
		s += "/login_success\n" + chartReport;
		logger.info(s);
	}
	

	
	@Override
	public void signOut() {
		// as of US212 update to user sign out date
		// will be taken care of by invocation of
		// MatContext.stopUserLockUpdate
		/*
		 * if(userid != null) { Date currentDate = new Date(); Timestamp
		 * currentTimeStamp = new Timestamp(currentDate.getTime()); User user =
		 * userService.getById(userid);
		 * 
		 * if(user != null){ user.setSignOutDate(currentTimeStamp);
		 * userService.saveExisting(user); } }
		 */
		SecurityContextHolder.clearContext();
	}

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
		return loginModel;
	}

	@Override
	public boolean changePasswordSecurityAnswers(LoginModel model) {
		logger.info("First time login, changing password and security answers");
		logger.info("Changing password");
		boolean result = false;
		User user = userService.getById(model.getUserId());
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

	@Override
	public UserDetails loadUserByUsername(String userId) {
		// UserDAO userDAO = (UserDAO)context.getBean("userDAO");
		return userDAO.getUser(userId);
	}

}
