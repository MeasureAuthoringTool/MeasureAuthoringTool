package mat.server.service.impl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.service.SaveUpdateUserResult;
import mat.client.login.service.SecurityQuestionOptions;
import mat.client.shared.MatContext;
import mat.client.shared.NameValuePair;
import mat.dao.SecurityRoleDAO;
import mat.dao.StatusDAO;
import mat.dao.TransactionAuditLogDAO;
import mat.dao.UserDAO;
import mat.model.SecurityRole;
import mat.model.Status;
import mat.model.TransactionAuditLog;
import mat.model.User;
import mat.model.UserPassword;
import mat.model.UserSecurityQuestion;
import mat.server.service.CodeListService;
import mat.server.service.UserIDNotUnique;
import mat.server.service.UserService;
import mat.server.util.ServerConstants;
import mat.server.util.TemplateUtil;
import mat.shared.ConstantMessages;
import mat.shared.ForgottenLoginIDResult;
import mat.shared.ForgottenPasswordResult;
import mat.shared.PasswordVerifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class UserServiceImpl implements UserService {
	private static final Log logger = LogFactory.getLog(UserServiceImpl.class);
	
	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUMERIC = "1234567890";
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	//private static Random ID = new Random(99999);
	private static Random ID = new Random(System.currentTimeMillis());
	
	//US 440
	TemplateUtil templateUtil = TemplateUtil.getInstance();
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private SimpleMailMessage templateMessage;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private StatusDAO statusDAO;
	
	@Autowired
	private SecurityRoleDAO securityRoleDAO;
	
	@Autowired
	private TransactionAuditLogDAO transactionAuditLogDAO;
	
	@Autowired
	private CodeListService codeListService;

	private String accessibilityUrl;

	private String termsOfUseUrl;

	private String privacyPolicyUseUrl;
	
	private String userGuideUrl;
	
	 public String generateRandomPassword() {
		String password = null;
		Random r = new Random(System.currentTimeMillis());
		StringBuilder pwdBuilder = new StringBuilder();
		pwdBuilder.append(Character.toUpperCase(ALPHABET.charAt(r.nextInt(ALPHABET.length()))));
		pwdBuilder.append(NUMERIC.charAt(r.nextInt(NUMERIC.length())));
		char[] specialArr = PasswordVerifier.getAllowedSpecialChars();
		pwdBuilder.append(specialArr[r.nextInt(specialArr.length)]);
		for(int i = pwdBuilder.length(); i < PasswordVerifier.getMinLength(); i++) {
			
			// need to guarantee no 3 consecutive characters
			char pre1 = pwdBuilder.charAt(pwdBuilder.length()-1);
			char pre2 = pwdBuilder.charAt(pwdBuilder.length()-2);
			char c = ALPHABET.charAt(r.nextInt(ALPHABET.length()));
			while(c==pre1 || c==pre2){
				c = ALPHABET.charAt(r.nextInt(ALPHABET.length()));
			}
			
			pwdBuilder.append(c);
		}
		password = pwdBuilder.toString();
		
		return password;
	}
	
	public void requestResetLockedPassword(String userid) {
		User user = userDAO.find(userid);
		String newPassword = generateRandomPassword();
		if(user.getPassword() == null) {
			UserPassword pwd = new UserPassword();
			user.setPassword(pwd);
		}
		setUserPassword(user, newPassword, true);
		userDAO.save(user);
		notifyUserOfTemporaryPassword(user, newPassword);
	}
	
	public void notifyUserOfTemporaryPassword(User user, String newPassword) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setSubject(ServerConstants.TEMP_PWD_SUBJECT);
		msg.setTo(user.getEmailAddress());

		//US 440. Re-factored to use template based framework
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(ConstantMessages.PASSWORD, newPassword);
		String text = templateUtil.mergeTemplate(ConstantMessages.TEMPLATE_TEMP_PASSWORD, paramsMap);
		msg.setText(text);
		logger.info("Sending email to " + user.getEmailAddress());
		try {
			this.mailSender.send(msg);
		}
		catch(MailException exc) {
			logger.error(exc);
		}
		
	}
	 
	public void setUserPassword(User user, String clearTextPassword, boolean isTemporary) {
		String salt = UUID.randomUUID().toString();
		user.getPassword().setSalt(salt);
		String password = getPasswordHash(salt, clearTextPassword);
		user.getPassword().setPassword(password);
		user.getPassword().setCreatedDate(new Date());
		user.getPassword().setTemporaryPassword(isTemporary);
		user.setLockedOutDate(null);
		user.getPassword().setForgotPwdlockCounter(0);
		user.getPassword().setPasswordlockCounter(0);
	}
	
	
	
	public ForgottenPasswordResult requestForgottenPassword(String loginId, 
			String securityQuestion, String securityAnswer, int invalidUserCounter) {
		ForgottenPasswordResult result = new ForgottenPasswordResult();
		result.setEmailSent(false);
		//logger.info(" requestForgottenPassword   Login Id ====" + loginId);
		User user = null;
		try {
			//user = userDAO.findByEmail(email);
			user = userDAO.findByLoginId(loginId);
		}
		catch(ObjectNotFoundException exc) { }
		
		if(user == null) {
			invalidUserCounter += 1;
			if(invalidUserCounter == 1){
				result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTION_MISMATCH);
			}else if(invalidUserCounter == 2){
				result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED_SECOND_ATTEMPT);
			}else if(invalidUserCounter == 3){
				result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED);
			}
			result.setCounter(invalidUserCounter);
//			result.setFailureReason(ForgottenPasswordResult.USER_NOT_FOUND);
		}
		else if(user.getSecurityQuestions().size() != 3) {
			result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTIONS_NOT_SET);
			result.setCounter(user.getPassword().getForgotPwdlockCounter());
		}
		else if(user.getLockedOutDate() != null) {
			result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED);
			result.setCounter(user.getPassword().getForgotPwdlockCounter());
		}
		else if(!securityQuestionMatch(user, securityQuestion, securityAnswer)) {
			result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTION_MISMATCH);
			int lockCounter = user.getPassword().getForgotPwdlockCounter() + 1;
			if(lockCounter == 2) {
				result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED_SECOND_ATTEMPT);
			}
			if(lockCounter == 3) {
				result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED);
				user.setLockedOutDate(new Date());
				notifyUserOfAccountLocked(user);
			}
			user.getPassword().setForgotPwdlockCounter(lockCounter);
			result.setCounter(user.getPassword().getForgotPwdlockCounter());
			userDAO.save(user);
		}
		else {	
			
			Date lastSignIn = user.getSignInDate();
			Date lastSignOut = user.getSignOutDate();
			Date current = new Date();
			
			boolean isAlreadySignedIn = MatContext.get().isAlreadySignedIn(lastSignOut, lastSignIn, current);
			
			if(isAlreadySignedIn){
				result.setFailureReason(ForgottenPasswordResult.USER_ALREADY_LOGGED_IN);
			}else{
				String newPassword = generateRandomPassword();
				setUserPassword(user, newPassword, true);
				result.setEmailSent(true);
				sendResetPassword(user.getEmailAddress(), newPassword);
				user.setLockedOutDate(null);
				user.getPassword().setForgotPwdlockCounter(0);
				result.setCounter(0);
				userDAO.save(user);
			}
		}
		return result;
	}
	
	private void sendAccountLockedMail(String email) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setSubject(ServerConstants.TEMP_PWD_SUBJECT);
		msg.setTo(email);
		//US 440. Re-factored to use template based framework
//		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
//		paramsMap.put(ConstantMessages.PASSWORD, newPassword);
//		String text = templateUtil.mergeTemplate(ConstantMessages.TEMPLATE_RESET_PASSWORD, paramsMap);
		msg.setText("hello");
//		System.out.println("newPassword ==============="+newPassword);
		logger.info("Sending email to " + email);
		try {
			this.mailSender.send(msg);
		}
		catch(MailException exc) {
			logger.error(exc);
		}
	}

	public ForgottenLoginIDResult requestForgottenLoginID(String email){
		ForgottenLoginIDResult result = new ForgottenLoginIDResult();
		result.setEmailSent(false);
		logger.info(" requestForgottenLoginID   email ====" + email);
		User user = null;
		boolean inValidEmail=false;
		try {
			if(isValidEmail(email)){
				inValidEmail=true;
				user = userDAO.findByEmail(email);
			}
		}
		catch(ObjectNotFoundException exc) { logger.info(" requestForgottenLoginID   Exception " + exc.getMessage());}
		
		if(user == null && inValidEmail) {
			result.setFailureReason(ForgottenLoginIDResult.EMAIL_NOT_FOUND_MSG);
			logger.info(" requestForgottenLoginID   user not found for email ::" +email );
		}else if(!inValidEmail){
			result.setFailureReason(ForgottenLoginIDResult.EMAIL_INVALID);
			logger.info(" requestForgottenLoginID   Invalid email ::" +email );
		}
		else {	
			
			Date lastSignIn = user.getSignInDate();
			Date lastSignOut = user.getSignOutDate();
			Date current = new Date();
			
			boolean isAlreadySignedIn = MatContext.get().isAlreadySignedIn(lastSignOut, lastSignIn, current);
			
			if(isAlreadySignedIn){
				result.setFailureReason(ForgottenLoginIDResult.USER_ALREADY_LOGGED_IN);
			}else{
				logger.info(" requestForgottenLoginID   User ID Found and email sent successfully to email address ::" +email );
				result.setEmailSent(true);
				notifyUserOfForgottenLoginId(user);
			}
		}
		return result;
	}
	
	private boolean isValidEmail(String emailAddress){
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(emailAddress);
		return matcher.matches();
	
	}
	
	private boolean securityQuestionMatch(User user, 
			String securityQuestion, String securityAnswer) {
		for(UserSecurityQuestion usq : user.getSecurityQuestions()) {
			if(securityQuestion.equalsIgnoreCase(usq.getSecurityQuestions().getQuestion()) &&
					securityAnswer.equalsIgnoreCase(usq.getSecurityAnswer())) {
				return true;
			}
		}
		return false;
	}
		
		
	private void sendResetPassword(String email, String newPassword) {		
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setSubject(ServerConstants.TEMP_PWD_SUBJECT);
		msg.setTo(email);
		//US 440. Re-factored to use template based framework
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(ConstantMessages.PASSWORD, newPassword);
		String text = templateUtil.mergeTemplate(ConstantMessages.TEMPLATE_RESET_PASSWORD, paramsMap);
		msg.setText(text);
		System.out.println("newPassword ==============="+newPassword);
		logger.info("Sending email to " + email);
		try {
			this.mailSender.send(msg);
		}
		catch(MailException exc) {
			logger.error(exc);
		}
	}
	
	
	@Override
	public List<User> searchForUsersByName(String orgId, int startIndex, int numResults) {
		if(orgId == null) {
			orgId = "";
		}
		return userDAO.searchForUsersByName(orgId, startIndex - 1, numResults);
	}
	
	@Override
	public List<User> searchNonAdminUsers(String orgId, int startIndex, int numResults) {
		if(orgId == null) {
			orgId = "";
		}
		return userDAO.searchNonAdminUsers(orgId, startIndex - 1, numResults);
	}
	
	@Override
	public User findByEmailID(String emailId) {
		return userDAO.findByEmail(emailId);
	}
	
	@Override
	public int countSearchResults(String text) {
		return userDAO.countSearchResults(text);
	}
	
	@Override
	public int countSearchResultsNonAdmin(String text) {
		return userDAO.countSearchResultsNonAdmin(text);
	}
	
	@Override
	public User getById(String id) {
		return userDAO.find(id);
	}

	
	@Override
	public void saveNew(User user) throws UserIDNotUnique {
		if(userDAO.userExists(user.getEmailAddress())) {
			throw new UserIDNotUnique();
		}
		if(user.getPassword() == null) {
			UserPassword pwd = new UserPassword();
			user.setPassword(pwd);
		}
		
		String newPassword = generateRandomPassword();
		user.setActivationDate(new Date());
		setUserPassword(user, newPassword, false);
		user.getPassword().setInitial(true);
		user.setStatus(statusDAO.find("1"));
		
		String newLoginId = generateUniqueLoginId(user.getFirstName(), user.getLastName());
		boolean isUniqueLoginId = false;
		
		while(!isUniqueLoginId){
			if(!userDAO.findUniqueLoginId(newLoginId)){
				isUniqueLoginId = true;
				user.setLoginId(newLoginId);
			}
			else{
				newLoginId = generateUniqueLoginId(user.getFirstName(), user.getLastName());
			}
		}
		userDAO.save(user);
		notifyUserOfNewAccount(user);
		notifyUserOfTemporaryPassword(user, newPassword);
	}
	
	public void notifyUserOfNewAccount(User user) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setSubject(ServerConstants.NEW_ACCESS_SUBJECT);
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(ConstantMessages.LOGINID, user.getLoginId());
		String text = templateUtil.mergeTemplate(ConstantMessages.TEMPLATE_WELCOME, paramsMap);
		
		msg.setTo(user.getEmailAddress());
		msg.setText(text);

		try {
			this.mailSender.send(msg);
		}
		catch(MailException exc) {
			logger.error(exc);
		}
	}
	
	public void notifyUserOfForgottenLoginId(User user) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setSubject(ServerConstants.FORGOT_LOGINID_SUBJECT);
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(ConstantMessages.LOGINID, user.getLoginId());
		String text = templateUtil.mergeTemplate(ConstantMessages.TEMPLATE_FORGOT_LOGINID, paramsMap);
		msg.setTo(user.getEmailAddress());
		msg.setText(text);

		try {
			this.mailSender.send(msg);
		}
		catch(MailException exc) {
			logger.error(exc);
		}
	}
	
	public String getPasswordHash(String salt, String password) {
		String hashed = hash(salt + password);
		return hashed;
	}
	private String hash(String s) {
		try {
			if(s == null) {
				s = "";
			}
			//MessageDigest m=MessageDigest.getInstance("MD5");
			MessageDigest m=MessageDigest.getInstance("SHA-256");
			m.update(s.getBytes(),0,s.length());
			return new BigInteger(1,m.digest()).toString(16);
		}
		catch(NoSuchAlgorithmException exc) {
			throw new RuntimeException(exc);
		}
	}
	
	@Override
	public void saveExisting(User user) {
		userDAO.save(user);
	}
	
	@Override
	public boolean isAdminForUser(User admin, User user) {
		boolean isAdmin = admin.getSecurityRole().getId().equals("1");
		boolean isSelf = admin.getId().equals(user.getId());

		return isAdmin && !isSelf;
	}

	@Override
	public void deleteUser(String userid) {
		userDAO.delete(userid);
	}

	@Override 
	public SecurityQuestionOptions getSecurityQuestionOptions(String loginId) {
		User user = userDAO.findByLoginId(loginId);
		SecurityQuestionOptions options = new SecurityQuestionOptions();
		options.setSecurityQuestions(new ArrayList<NameValuePair>());
		if(user != null) {
			options.setUserFound(true);
			for(UserSecurityQuestion q : user.getSecurityQuestions()) {
				NameValuePair nvp = 
					new NameValuePair(q.getSecurityQuestions().getQuestion(), q.getSecurityQuestions().getQuestion());
				options.getSecurityQuestions().add(nvp);
			}
		}
		else {
			options.setUserFound(false);
		}
		return options;
	}
	
	@Override 
	public SecurityQuestionOptions getSecurityQuestionOptionsForEmail(String email) {
		User user = userDAO.findByEmail(email);
		SecurityQuestionOptions options = new SecurityQuestionOptions();
		options.setSecurityQuestions(new ArrayList<NameValuePair>());
		if(user != null) {
			options.setUserFound(true);
			for(UserSecurityQuestion q : user.getSecurityQuestions()) {
				NameValuePair nvp = 
					new NameValuePair(q.getSecurityQuestions().getQuestion(), q.getSecurityQuestions().getQuestion());
				options.getSecurityQuestions().add(nvp);
			}
		}
		else {
			options.setUserFound(false);
		}
		return options;
	}

	@Override
	public SaveUpdateUserResult saveUpdateUser(ManageUsersDetailModel model) {
		SaveUpdateUserResult result = new SaveUpdateUserResult();
		User user = null;
		if(model.isExistingUser()) {
			user = getById(model.getKey());
		}
		else {
			user = new User();
		}
		
		boolean reactivatingUser = false;
		if(model.isActive() && user.getStatus()!= null && !user.getStatus().getId().equals("1")) {
			reactivatingUser = true;
		}
		
		setModelFieldsOnUser(model, user);
		
		try {
			if(model.isExistingUser()) {
				if(reactivatingUser) {
					requestResetLockedPassword(user.getId());
				}
				saveExisting(user);
			}
			else {
				saveNew(user);
				//codeListService.saveDefaultCodeList(user);

			}
		result.setSuccess(true);
		}
		catch(UserIDNotUnique exc) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateUserResult.ID_NOT_UNIQUE);
		}/*catch(CodeListNotUniqueException exp){
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.NOT_UNIQUE);	
		}*/

		return result;
	}
	
	@Override
	public List<String> getFooterURLs(){
		List<String> footerUrls = new ArrayList<String>();
		footerUrls.add(accessibilityUrl);
		footerUrls.add(privacyPolicyUseUrl);
		footerUrls.add(termsOfUseUrl);
		footerUrls.add(userGuideUrl);
		return footerUrls;
	}
	
	private void setModelFieldsOnUser(ManageUsersDetailModel model, User user) {
		user.setFirstName(model.getFirstName());
		user.setLastName(model.getLastName());
		user.setMiddleInit(model.getMiddleInitial());
		user.setTitle(model.getTitle());
		user.setEmailAddress(model.getEmailAddress());
		user.setPhoneNumber(model.getPhoneNumber());
		user.setStatus(getStatusObject(model.isActive()));
		user.setSecurityRole(getRole(model.getRole()));
		user.setOrgOID(model.getOid());
		user.setRootOID(model.getRootOid());
		user.setOrganizationName(model.getOrganization());
		
		if(model.isActive() && user.getActivationDate() == null) {
			user.setTerminationDate(null);
			user.setActivationDate(new Date());
		}
		else if(!model.isActive() && 
				(user.getTerminationDate() == null || 
						user.getTerminationDate().before(user.getActivationDate()))) {
			user.setTerminationDate(new Date());
		}
	}
	private Status getStatusObject(boolean isActive) {
		String id = isActive ? "1" : "2";
		return statusDAO.find(id);
	}
	
	private SecurityRole getRole(String value) {
		for(SecurityRole role : securityRoleDAO.find()) {
			if(role.getId().equals(value)) {
				return role;
			}
		}
		return null;
	}

	//US212
	@Override
	public void setUserSignInDate(String userid) {		
		userDAO.setUserSignInDate(userid);
	}
	//US212
	@Override
	public void setUserSignOutDate(String userid) {		
		userDAO.setUserSignOutDate(userid);
	}
	/*
	 * 
	 * Algorithm to Generated Unique login ID for new User
	 * First Name - 2 characters
	 * Last Name - 6 characters
	 * Four Digit - Random number
	 * 
	 * */
	private String generateUniqueLoginId(String firstName, String lastName){
		StringBuilder generatedId = new StringBuilder();
		int firstNameLastIndex = 2;
		int lastNameLastIndex = 6;
		//Check if First Name length us less than 2 then set endIndex to length
		if(firstName.length()< 2){
			firstNameLastIndex = lastName.length();
		}
		//Check if last Name length us less than 6 then set endIndex to length
		if(lastName.length()< 6){
			lastNameLastIndex = lastName.length();
		}
		
		generatedId = generatedId.append(firstName.substring(0,firstNameLastIndex)).append(lastName.substring(0, lastNameLastIndex)).append((ID.nextInt(9000) + 1000));
		return generatedId.toString();
	}
	
	public void setAccessibilityUrl(String accessibilityUrl) {
		this.accessibilityUrl = accessibilityUrl;
	}



	public String getAccessibilityUrl() {
		return accessibilityUrl;
	}



	public void setTermsOfUseUrl(String termsOfUseUrl) {
		this.termsOfUseUrl = termsOfUseUrl;
	}



	public String getTermsOfUseUrl() {
		return termsOfUseUrl;
	}



	public void setPrivacyPolicyUseUrl(String privacyPolicyUseUrl) {
		this.privacyPolicyUseUrl = privacyPolicyUseUrl;
	}



	public String getPrivacyPolicyUseUrl() {
		return privacyPolicyUseUrl;
	}

	public void setUserGuideUrl(String userGuideUrl) {
		this.userGuideUrl = userGuideUrl;
	}

	public String getUserGuideUrl() {
		return userGuideUrl;
	}

	@Override
	public String updateOnSignOut(String userId, String email, String activityType) {
		TransactionAuditLog auditLog = new TransactionAuditLog();
		auditLog.setActivityType(activityType);
		auditLog.setUserId(userId);
		auditLog.setAdditionalInfo("["+email+"]");
		
		Date signoutDate = new Date(); 
		User user = userDAO.find(userId);
		user.setSignOutDate(signoutDate);	
		try{
			userDAO.save(user);
			transactionAuditLogDAO.save(auditLog);
			logger.info("SignOut Successful" + signoutDate.toString());
			return "SUCCESS";
		}catch (Exception e) {
			logger.info("SignOut Unsuccessful "+ "("+ signoutDate.toString() +")"+ e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public String getSecurityQuestion(String userid) {
		return userDAO.getRandomSecurityQuestion(userid);
	}
	@Override
	public List<User> getAllNonAdminActiveUsers(){
		return this.userDAO.getAllNonAdminActiveUsers();
	}
	
	public void notifyUserOfAccountLocked(User user) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setSubject(ServerConstants.ACCOUNT_LOCKED_SUBJECT);
		String text = ServerConstants.ACCOUNT_LOCKED_MESSAGE;
		msg.setTo(user.getEmailAddress());
		msg.setText(text);
		try {
			this.mailSender.send(msg);
		}
		catch(MailException exc) {
			logger.error(exc);
		}
	}

	@Override
	public boolean isLockedUser(String loginId) {
		User user = userDAO.findByLoginId(loginId);
		if(user != null && (user.getLockedOutDate() != null || user.getPassword().getPasswordlockCounter() >= 3)){
			return true;
		}
		return false;
	}
		

}
