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

import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.service.SaveUpdateUserResult;
import mat.client.login.service.SecurityQuestionOptions;
import mat.client.shared.MatContext;
import mat.client.shared.NameValuePair;
import mat.dao.SecurityRoleDAO;
import mat.dao.StatusDAO;
import mat.dao.UserDAO;
import mat.model.SecurityRole;
import mat.model.Status;
import mat.model.User;
import mat.model.UserPassword;
import mat.model.UserSecurityQuestion;
import mat.server.service.CodeListService;
import mat.server.service.UserIDNotUnique;
import mat.server.service.UserService;
import mat.server.util.TemplateUtil;
import mat.shared.ConstantMessages;
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
	private CodeListService codeListService;
	
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
	
	private void notifyUserOfTemporaryPassword(User user, String newPassword) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setSubject("MAT password reset request.");
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
			String securityQuestion, String securityAnswer) {
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
			result.setFailureReason(ForgottenPasswordResult.USER_NOT_FOUND);
		}
		else if(user.getSecurityQuestions().size() != 3) {
			result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTIONS_NOT_SET);
		}
		else if(user.getLockedOutDate() != null) {
			result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED);
		}
		else if(!securityQuestionMatch(user, securityQuestion, securityAnswer)) {
			result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTION_MISMATCH);
			int lockCounter = user.getPassword().getForgotPwdlockCounter() + 1;
			if(lockCounter == 3) {
				result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTIONS_LOCKED);
				user.setLockedOutDate(new Date());
			}
			user.getPassword().setForgotPwdlockCounter(lockCounter);
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
				userDAO.save(user);
			}
		}
		return result;
	}
	private boolean securityQuestionMatch(User user, 
			String securityQuestion, String securityAnswer) {
		for(UserSecurityQuestion usq : user.getSecurityQuestions()) {
			if(securityQuestion.equals(usq.getSecurityQuestion()) &&
					securityAnswer.equals(usq.getSecurityAnswer())) {
				return true;
			}
		}
		return false;
	}
		
		
	private void sendResetPassword(String email, String newPassword) {		
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setSubject("MAT password reset request.");
		msg.setTo(email);
		//US 440. Re-factored to use template based framework
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(ConstantMessages.PASSWORD, newPassword);
		String text = templateUtil.mergeTemplate(ConstantMessages.TEMPLATE_RESET_PASSWORD, paramsMap);
		msg.setText(text);

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
	public int countSearchResults(String text) {
		return userDAO.countSearchResults(text);
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
	
	private void notifyUserOfNewAccount(User user) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setSubject("MAT application access.");
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
		//User user = userDAO.findByEmail(email);
		User user = userDAO.findByLoginId(loginId);
		SecurityQuestionOptions options = new SecurityQuestionOptions();
		options.setSecurityQuestions(new ArrayList<NameValuePair>());
		if(user != null) {
			options.setUserFound(true);
			for(UserSecurityQuestion q : user.getSecurityQuestions()) {
				NameValuePair nvp = 
					new NameValuePair(q.getSecurityQuestion(), q.getSecurityQuestion());
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
	
}
