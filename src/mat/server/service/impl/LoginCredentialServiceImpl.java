package mat.server.service.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import mat.client.login.LoginModel;
import mat.client.shared.MatContext;
import mat.model.User;
import mat.model.UserPassword;
import mat.model.UserSecurityQuestion;
import mat.server.PreventCachingFilter;
import mat.server.hibernate.HibernateUserDetailService;
import mat.server.model.MatUserDetails;
import mat.server.service.LoginCredentialService;
import mat.server.service.UserService;
import mat.shared.ForgottenPasswordResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

public class LoginCredentialServiceImpl implements LoginCredentialService {

	private static final Log logger = LogFactory.getLog(LoginCredentialServiceImpl.class);
	
	@Autowired
	private HibernateUserDetailService  hibernateUserService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public LoginModel isValidUser(String userId, String password) {
		
		LoginModel loginModel = new LoginModel();
		MatUserDetails userDetails =(MatUserDetails )hibernateUserService.loadUserByUsername(userId);
		Date currentDate = new Date();
		Timestamp currentTimeStamp = new Timestamp(currentDate.getTime());
		
		if(userDetails != null)
		{
			String hashPassword = userService.getPasswordHash(userDetails.getUserPassword().getSalt(), password);
		    
			if(userDetails.getStatus().getId().equals("2"))
			{
				//REVOKED USER NO
				logger.debug("User status is 2, revoked");
				 loginModel.setLoginFailedEvent(true);
				 loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getAccountRevokedMessage());
				 loginModel.setUserId(userId);
				 loginModel.setEmail(userDetails.getEmailAddress());
			}/*else if ((userDetails.getTerminationDate()!=null &&
					userDetails.getTerminationDate().after(userDetails.getActivationDate())))
			{
				logger.info("User Not Active from past 180 days, Terminated USER userDetails.getTerminationDate() ::::" + userDetails.getTerminationDate());
				logger.info("User Not Active from past 180 days, Terminated USER   :::: " + userDetails.getTerminationDate().after(userDetails.getActivationDate()));
				 loginModel.setLoginFailedEvent(true);
				 loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getAccountTermination());
				 loginModel.setUserId(userId);
				 loginModel.setEmail(userDetails.getEmailAddress());
				
			}*/else if(hashPassword.equalsIgnoreCase(userDetails.getUserPassword().getPassword())
					&& userDetails.getUserPassword().getPasswordlockCounter() < 3 && userDetails.getUserPassword().getForgotPwdlockCounter() < 3){
				
				Date lastSignIn = userDetails.getSignInDate();
				Date lastSignOut = userDetails.getSignOutDate();
				
				
				boolean alreadySignedIn = MatContext.get().isAlreadySignedIn(lastSignOut, lastSignIn, currentTimeStamp);
				
				if(alreadySignedIn){
					//USER ALREADY LOGGED IN
					logger.info("USER ALREADY LOGGED IN :" + userId);
					loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedAlreadyLoggedInMessage());
					loginModel.setLoginFailedEvent(true);
				}else{
					logger.debug("Password matched, not locked out");
					if(!userDetails.getUserPassword().isInitial() && !userDetails.getUserPassword().isTemporaryPassword()) {
						setAuthenticationToken(userDetails);
					}
					loginModel = loginModelSetter(loginModel, userDetails);
//					userDetails.setSignInDate(currentTimeStamp);
					hibernateUserService.saveUserDetails(userDetails);
					logger.info("Roles for " + userId + ": " + userDetails.getRoles().getDescription());
				}
			}else{
				 logger.debug("Authentication Exception, need to log the failed attempts and increment the lockCounter");
				 loginModel.setLoginFailedEvent(true);
				 loginModel.setUserId(userId);
				 int currentPasswordlockCounter = userDetails.getUserPassword().getPasswordlockCounter();
				 logger.info("CurrentPasswordLockCounter value:" +currentPasswordlockCounter);
				 if(userDetails.getLockedOutDate() != null){
					 logger.debug("User locked out");
					 loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getAccountLocked2Message());
				 }
				 switch(currentPasswordlockCounter){
				    case 0:
				    	logger.debug("First failed login attempt");
				    	//FIRST FAILED LOGIN ATTEMPT
				    	 loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
						 userDetails.getUserPassword().setPasswordlockCounter(currentPasswordlockCounter+1);
						 userDetails.getUserPassword().setFirstFailedAttemptTime(currentTimeStamp);
						 break;
					case 1:
						logger.debug("Second failed login attempt");
						//SECOND FAILED LOGIN ATTEMPT
				    	 Timestamp firstFailedAttemptTime = userDetails.getUserPassword().getFirstFailedAttemptTime();
				    	 long difference =  currentTimeStamp.getTime() - firstFailedAttemptTime.getTime();
						 long MinuteDifference = difference/(60*1000);
				    	 if(MinuteDifference > 15){
				    		 loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
							 logger.info("MinuteDifference:"+ MinuteDifference);
							 logger.info("Since the minuteDifference is greater than 15 minutes, update the failedAttemptTime");
							 userDetails.getUserPassword().setFirstFailedAttemptTime(currentTimeStamp);
						 }else{
							 loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getSecondAttemptFailedMessage());
							 userDetails.getUserPassword().setPasswordlockCounter(currentPasswordlockCounter+1);
						 }
						 break;
					case 2:
						//USER THIRD FAILED LOGIN ATTEMPT
						logger.info("USER THIRD FAILED LOGIN ATTEMPT :" + userId);
				    	Timestamp updatedFailedAttemptTime = userDetails.getUserPassword().getFirstFailedAttemptTime();
				    	long timeDifference =  currentTimeStamp.getTime() - updatedFailedAttemptTime.getTime();
				    	long minDifference = timeDifference/(60*1000);
						if(minDifference > 15){
							 logger.debug("MinuteDifference:"+ minDifference);
							 logger.debug("Since the minuteDifference is greater than 15 minutes, update the failedAttemptTime");
							 loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
							 userDetails.getUserPassword().setFirstFailedAttemptTime(currentTimeStamp);
							 userDetails.getUserPassword().setPasswordlockCounter(1);
						}else{
							 loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getAccountLocked2Message());
							 userDetails.setLockedOutDate(currentTimeStamp);
							 userDetails.getUserPassword().setPasswordlockCounter(3);
							 logger.debug("Locking user out");
					    }
						break;
					default:
					   //USER LOCKED OUT
					   logger.info("USER LOCKED OUT :" + userId);
				 }//end of switch
				 hibernateUserService.saveUserDetails(userDetails);
				
			 }//end of else
			 
	    }
		else { // user not found
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MatContext.get().getMessageDelegate().getLoginFailedMessage());
		}
		
		if(!loginModel.isLoginFailedEvent()){
			String s = "\nlogin_success\n";

			String chartReport ="CHARTREPORT";
			
			List<MemoryPoolMXBean> pbeans = ManagementFactory.getMemoryPoolMXBeans();
			for( MemoryPoolMXBean bean: pbeans){
				MemoryUsage mused = bean.getPeakUsage();
				for(String x :bean.getMemoryManagerNames()){
					s +="MemoryManager "+ x+ ":\n";
				}
				s += "poolInit:      \t"+mused.getInit() + " ";
				s += "poolPeak:      \t"+mused.getUsed() +" " ;
				s += "poolMax:       \t"+mused.getMax()+"\n\n";
			}

			MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
			//bean.setVerbose(true);

			MemoryUsage mu = bean.getNonHeapMemoryUsage();
			//[MemoryUsage]
			//\nNonHeap|init:<<val>>|committed:<<val>>|max:<<val>>|used:<<val>>
			//\nHeap|init:<<val>>|committed:<<val>>|max:<<val>>|used:<<val>>		
			
			s += "PermGen init:      \t"+mu.getInit()+"\n";
			s += "PermGen committed: \t"+mu.getCommitted()+" ";
			s += "PermGen Max:       \t"+mu.getMax()+" ";
			s += "PermGen Used:      \t"+mu.getUsed()+" ";
			chartReport +=" "+mu.getUsed();
//			ManagementFactory.
			
			mu =bean.getHeapMemoryUsage();
			// Heap|init:<<val>>|committed:<<val>>|max:<<val>>|used:<<val>>			
			s += "Heap init:      \t"+mu.getInit()+" ";
			s += "Heap committed: \t"+mu.getCommitted()+" ";
			s += "Heap Max:       \t"+mu.getMax()+" ";
			s += "Heap Used:      \t"+mu.getUsed()+"\n";

			chartReport +=" "+mu.getUsed();
			
			ThreadMXBean tBean = ManagementFactory.getThreadMXBean();
			s += "Threads running:   \t"+tBean.getThreadCount()+"\n";
			
			chartReport +=" "+tBean.getThreadCount();
			
			chartReport +=" "+System.currentTimeMillis();
			
			Log logger = LogFactory.getLog(PreventCachingFilter.class);
			chartReport +="\n";
			s+= "/login_success\n" + chartReport;
			logger.info(s);
		}
		return loginModel;
	}

	@Override
	public void signOut() {
		//as of US212 update to user sign out date 
		//will be taken care of by invocation of 
		//MatContext.stopUserLockUpdate
		/*if(userid != null) {
			Date currentDate = new Date();
			Timestamp currentTimeStamp = new Timestamp(currentDate.getTime());
			User user = userService.getById(userid);
			
			if(user != null){
				user.setSignOutDate(currentTimeStamp);
				userService.saveExisting(user);
			}
		}*/
		SecurityContextHolder.clearContext();
	}

	@Override
	public LoginModel changeTempPassword(String email, String changedpassword) {
		logger.info("Changing the temporary Password");
		LoginModel loginModel = new LoginModel();
		
		MatUserDetails userDetails = (MatUserDetails)hibernateUserService.loadUserByUsername(email);
		UserPassword userPassword = userDetails.getUserPassword();
		String hashPassword = userService.getPasswordHash(userPassword.getSalt(), changedpassword);
		Date currentDate = new Date();
		Timestamp currentTimeStamp = new Timestamp(currentDate.getTime());
		userPassword.setPassword(hashPassword);
		userPassword.setCreatedDate(currentTimeStamp);
		userPassword.setTemporaryPassword(false);
		hibernateUserService.saveUserDetails(userDetails);
		
		setAuthenticationToken(userDetails);
		loginModel = loginModelSetter(loginModel,userDetails);
		
		hibernateUserService.saveUserDetails(userDetails);
		
		logger.info("Roles for " + email + ": " + userDetails.getRoles().getDescription());
		return loginModel;
		
	}

	private void setAuthenticationToken(MatUserDetails userDetails){
		logger.debug("Setting authentication token");
		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getId(), userDetails.getUserPassword().getPassword(),userDetails.getAuthorities());

		//US 170. set additional details for history event
		((UsernamePasswordAuthenticationToken)auth).setDetails(userDetails);
		 SecurityContext sc = new SecurityContextImpl();
		sc.setAuthentication(auth);		
		SecurityContextHolder.setContext(sc);		
	}
	
	private LoginModel loginModelSetter(LoginModel loginmodel, MatUserDetails userDetails){
		LoginModel loginModel = loginmodel;
		loginModel.setRole(userDetails.getRoles());
		loginModel.setInitialPassword(userDetails.getUserPassword().isInitial());
		loginModel.setTemporaryPassword(userDetails.getUserPassword().isTemporaryPassword());
		loginModel.setUserId(userDetails.getId());
		loginModel.setEmail(userDetails.getEmailAddress());
		return loginModel;
	}

	@Override
	public ForgottenPasswordResult forgotPassword(String userid, String email,
			String securityQuestion, String securityAnswer) {
		return userService.requestForgottenPassword(email, securityQuestion, securityAnswer);
	}

	@Override
	public void changePasswordSecurityAnswers(LoginModel model) {
		logger.info("First time login, changing password and security answers");
		logger.info("Changing password");
		
		User user = userService.getById(model.getUserId());
		userService.setUserPassword(user, model.getPassword(),false);
		user.getPassword().setInitial(false);
		
		logger.info("Saving security questions");
		List<UserSecurityQuestion> secQuestions = user.getSecurityQuestions();
		while(secQuestions.size() < 3) {
			UserSecurityQuestion newQuestion = new UserSecurityQuestion();
			secQuestions.add(newQuestion);
		}
		
		secQuestions.get(0).setSecurityQuestion(model.getQuestion1());
		secQuestions.get(0).setSecurityAnswer(model.getQuestion1Answer());

		secQuestions.get(1).setSecurityQuestion(model.getQuestion2());
		secQuestions.get(1).setSecurityAnswer(model.getQuestion2Answer());

		secQuestions.get(2).setSecurityQuestion(model.getQuestion3()); 
		secQuestions.get(2).setSecurityAnswer(model.getQuestion3Answer());
		user.setSecurityQuestions(secQuestions);
		userService.saveExisting(user);
		
		MatUserDetails userDetails = (MatUserDetails)hibernateUserService.loadUserByUsername(user.getEmailAddress());
		setAuthenticationToken(userDetails);
		
	}
	
}
