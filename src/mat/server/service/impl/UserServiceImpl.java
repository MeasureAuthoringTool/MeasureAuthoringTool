package mat.server.service.impl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.service.SaveUpdateUserResult;
import mat.client.login.service.SecurityQuestionOptions;
import mat.client.shared.MatContext;
import mat.client.shared.NameValuePair;
import mat.dao.OrganizationDAO;
import mat.dao.SecurityRoleDAO;
import mat.dao.StatusDAO;
import mat.dao.TransactionAuditLogDAO;
import mat.dao.UserDAO;
import mat.dao.UserPasswordHistoryDAO;
import mat.model.Organization;
import mat.model.SecurityRole;
import mat.model.Status;
import mat.model.TransactionAuditLog;
import mat.model.User;
import mat.model.UserPassword;
import mat.model.UserPasswordHistory;
import mat.model.UserSecurityQuestion;
import mat.server.service.CodeListService;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

// TODO: Auto-generated Javadoc
/**
 * The Class UserServiceImpl.
 */
public class UserServiceImpl implements UserService {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(UserServiceImpl.class);
	
	/** The Constant ALPHABET. */
	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	
	/** The Constant NUMERIC. */
	private static final String NUMERIC = "1234567890";
	
	/** The Constant EMAIL_PATTERN. */
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	//private static Random ID = new Random(99999);
	/** The id. */
	private static Random ID = new Random(System.currentTimeMillis());
	
	//US 440
	/** The template util. */
	TemplateUtil templateUtil = TemplateUtil.getInstance();
	
	/** The mail sender. */
	@Autowired
	private JavaMailSender mailSender;
	
	/** The template message. */
	@Autowired
	private SimpleMailMessage templateMessage;
	
	/** The user dao. */
	@Autowired
	private UserDAO userDAO;
	
	/** The status dao. */
	@Autowired
	private StatusDAO statusDAO;
	
	/** The security role dao. */
	@Autowired
	private SecurityRoleDAO securityRoleDAO;
	
	/** The transaction audit log dao. */
	@Autowired
	private TransactionAuditLogDAO transactionAuditLogDAO;
	
	/** The organization dao. */
	@Autowired
	private OrganizationDAO organizationDAO;
	
	/** The code list service. */
	@Autowired
	private CodeListService codeListService;
	
	/** The accessibility url. */
	private String accessibilityUrl;
	
	/** The terms of use url. */
	private String termsOfUseUrl;
	
	/** The privacy policy use url. */
	private String privacyPolicyUseUrl;
	
	/** The user guide url. */
	private String userGuideUrl;
	
	/** The pawd history size. */
	private final int PASSWORD_HISTORY_SIZE = 5;
	
	/** The user password history dao. */
	@Autowired
	private UserPasswordHistoryDAO userPasswordHistoryDAO;
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#generateRandomPassword()
	 */
	@Override
	public String generateRandomPassword() {
		logger.info("In generateRandomPassword().....");
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
			while((c==pre1) || (c==pre2)){
				c = ALPHABET.charAt(r.nextInt(ALPHABET.length()));
			}
			
			pwdBuilder.append(c);
		}
		password = pwdBuilder.toString();
		
		return password;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#requestResetLockedPassword(java.lang.String)
	 */
	@Override
	public void requestResetLockedPassword(String userid) {
		logger.info("In requestResetLockedPassword(String userid).....");
		User user = userDAO.find(userid);
		String newPassword = generateRandomPassword();
		if(user.getPassword() == null) {
			UserPassword pwd = new UserPassword();
			user.setPassword(pwd);
		}
		
		//to maintain user password History
		if(user.getPassword()!=null) {
			addByUpdateUserPasswordHistory(user,false);
		}
		setUserPassword(user, newPassword, true);
		userDAO.save(user);
		notifyUserOfTemporaryPassword(user, newPassword);
	}
	
	/**
	 * Notify user of temporary password.
	 * 
	 * @param user
	 *            the user
	 * @param newPassword
	 *            the new password
	 */
	public void notifyUserOfTemporaryPassword(User user, String newPassword) {
		logger.info("In notifyUserOfTemporaryPassword(User user, String newPassword).....");
		SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
		msg.setSubject(ServerConstants.TEMP_PWD_SUBJECT + ServerConstants.getEnvName());
		msg.setTo(user.getEmailAddress());
		
		String expiryDateString = getFormattedExpiryDate(new Date(),5);
		
		//US 440. Re-factored to use template based framework
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(ConstantMessages.PASSWORD, newPassword);
		paramsMap.put(ConstantMessages.PASSWORD_EXPIRE_DATE, expiryDateString);
		paramsMap.put(ConstantMessages.URL, ServerConstants.getEnvURL());
		String text = templateUtil.mergeTemplate(ConstantMessages.TEMPLATE_TEMP_PASSWORD, paramsMap);
		System.out.println(text);
		msg.setText(text);
		logger.info("Sending email to " + user.getEmailAddress());
		try {
			mailSender.send(msg);
		}
		catch(MailException exc) {
			logger.error(exc);
		}
		
	}
	
	/**
	 * Gets the formatted expiry date.
	 * 
	 * @param startDate
	 *            the start date
	 * @param willExpireIn
	 *            the will expire in
	 * @return the formatted expiry date
	 */
	private String getFormattedExpiryDate(Date startDate,int willExpireIn){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DAY_OF_MONTH, willExpireIn);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEEE, MMMMM d, yyyy");
		String returnDateString  = simpleDateFormat.format(calendar.getTime());
		return returnDateString;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#setUserPassword(mat.model.User, java.lang.String, boolean)
	 */
	@Override
	public void setUserPassword(User user, String clearTextPassword, boolean isTemporary) {
		logger.info("In setUserPassword(User user, String clearTextPassword, boolean isTemporary)........");
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
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#requestForgottenPassword(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public ForgottenPasswordResult requestForgottenPassword(String loginId,
			String securityQuestion, String securityAnswer, int invalidUserCounter) {
		logger.info("In requestForgottenPassword(String loginId, String securityQuestion, String securityAnswer, int invalidUserCounter).......");
		ForgottenPasswordResult result = new ForgottenPasswordResult();
		result.setEmailSent(false);
		//logger.info(" requestForgottenPassword   Login Id ====" + loginId);
		User user = null;
		try {
			//user = userDAO.findByEmail(email);
			user = userDAO.findByLoginId(loginId);
		}
		catch(ObjectNotFoundException exc) {
			exc.printStackTrace();
		}
		
		if(user == null) {
			result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTION_MISMATCH);
		}
		else if(user.getSecurityQuestions().size() != 3) {
			result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTIONS_NOT_SET);
		}
		else if(user.getLockedOutDate() != null) {
			result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTION_MISMATCH);
		}
		else if(!securityQuestionMatch(user, securityQuestion, securityAnswer)) {
			result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTION_MISMATCH);
			int lockCounter = user.getPassword().getForgotPwdlockCounter() + 1;
			if(lockCounter == 3) {
				user.setLockedOutDate(new Date());
				notifyUserOfAccountLocked(user);
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
				
				//to maitain passwordHistory
				addByUpdateUserPasswordHistory(user,false);
				String newPassword = generateRandomPassword();
				setUserPassword(user, newPassword, true);
				result.setEmailSent(true);
				sendResetPassword(user.getEmailAddress(), newPassword);
				user.setLockedOutDate(null);
				user.getPassword().setForgotPwdlockCounter(0);
				userDAO.save(user);
			}
		}
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#requestForgottenLoginID(java.lang.String)
	 */
	@Override
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
		
		if((user == null) && inValidEmail) {
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
	
	/**
	 * Checks if is valid email.
	 * 
	 * @param emailAddress
	 *            the email address
	 * @return true, if is valid email
	 */
	private boolean isValidEmail(String emailAddress){
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(emailAddress);
		return matcher.matches();
		
	}
	
	/**
	 * Security question match.
	 * 
	 * @param user
	 *            the user
	 * @param securityQuestion
	 *            the security question
	 * @param securityAnswer
	 *            the security answer
	 * @return true, if successful
	 */
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
	
	
	/**
	 * Send reset password.
	 * 
	 * @param email
	 *            the email
	 * @param newPassword
	 *            the new password
	 */
	private void sendResetPassword(String email, String newPassword) {
		logger.info("In sendResetPassword(String email, String newPassword)........" +newPassword);
		SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
		msg.setSubject(ServerConstants.TEMP_PWD_SUBJECT + ServerConstants.getEnvName());
		msg.setTo(email);
		String expiryDateString = getFormattedExpiryDate(new Date(), 5);
		//US 440. Re-factored to use template based framework
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(ConstantMessages.PASSWORD_EXPIRE_DATE, expiryDateString);
		paramsMap.put(ConstantMessages.PASSWORD, newPassword);
		paramsMap.put(ConstantMessages.URL, ServerConstants.getEnvURL());
		String text = templateUtil.mergeTemplate(ConstantMessages.TEMPLATE_RESET_PASSWORD, paramsMap);
		msg.setText(text);
		logger.info("Sending email to " + email);
		try {
			mailSender.send(msg);
		}
		catch(MailException exc) {
			logger.error(exc);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#searchForUsersByName(java.lang.String)
	 */
	@Override
	public List<User> searchForUsersByName(String orgId) {
		if(orgId == null) {
			orgId = "";
		}
		return userDAO.searchForUsersByName(orgId);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#searchForUsedOrganizations()
	 */
	@Override
	public HashMap<String, Organization> searchForUsedOrganizations() {
		return userDAO.searchAllUsedOrganizations();
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#searchNonAdminUsers(java.lang.String, int, int)
	 */
	@Override
	public List<User> searchNonAdminUsers(String orgId, int startIndex, int numResults) {
		if(orgId == null) {
			orgId = "";
		}
		return userDAO.searchNonAdminUsers(orgId, startIndex - 1, numResults);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#findByEmailID(java.lang.String)
	 */
	@Override
	public User findByEmailID(String emailId) {
		return userDAO.findByEmail(emailId);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#countSearchResults(java.lang.String)
	 */
	@Override
	public int countSearchResults(String text) {
		return userDAO.countSearchResults(text);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#countSearchResultsNonAdmin(java.lang.String)
	 */
	@Override
	public int countSearchResultsNonAdmin(String text) {
		return userDAO.countSearchResultsNonAdmin(text);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#getById(java.lang.String)
	 */
	@Override
	public User getById(String id) {
		return userDAO.find(id);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#saveNew(mat.model.User)
	 */
	@Override
	public void saveNew(User user) {
		logger.info("In saveNew(User user)..........");
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
	
	/**
	 * Notify user of new account.
	 * 
	 * @param user
	 *            the user
	 */
	public void notifyUserOfNewAccount(User user) {
		logger.info("In notifyUserOfNewAccount(User user)..........");
		MimeMessage message = mailSender.createMimeMessage();
		try {
			message.setSubject(ServerConstants.NEW_ACCESS_SUBJECT + ServerConstants.getEnvName());
			BodyPart body = new MimeBodyPart();
			HashMap<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put(ConstantMessages.LOGINID, user.getLoginId());
			paramsMap.put(ConstantMessages.URL, ServerConstants.getEnvURL());
			String text = templateUtil.mergeTemplate(ConstantMessages.TEMPLATE_WELCOME, paramsMap);
			body.setContent(text, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(body);
			message.setFrom(new InternetAddress("NO-REPLY-Support@emeasuretool.org"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmailAddress()));
			message.setContent(multipart);
			
			mailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Notify user of forgotten login id.
	 * 
	 * @param user
	 *            the user
	 */
	public void notifyUserOfForgottenLoginId(User user) {
		SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
		msg.setSubject(ServerConstants.FORGOT_LOGINID_SUBJECT + ServerConstants.getEnvName());
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(ConstantMessages.LOGINID, user.getLoginId());
		paramsMap.put(ConstantMessages.URL, ServerConstants.getEnvURL());
		String text = templateUtil.mergeTemplate(ConstantMessages.TEMPLATE_FORGOT_LOGINID, paramsMap);
		msg.setTo(user.getEmailAddress());
		msg.setText(text);
		
		try {
			mailSender.send(msg);
		}
		catch(MailException exc) {
			logger.error(exc);
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#getPasswordHash(java.lang.String, java.lang.String)
	 */
	@Override
	public String getPasswordHash(String salt, String password) {
		String hashed = hash(salt + password);
		return hashed;
	}
	
	/**
	 * Hash.
	 * 
	 * @param s
	 *            the s
	 * @return the string
	 */
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
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#saveExisting(mat.model.User)
	 */
	@Override
	public void saveExisting(User user) {
		userDAO.save(user);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#isAdminForUser(mat.model.User, mat.model.User)
	 */
	@Override
	public boolean isAdminForUser(User admin, User user) {
		boolean isAdmin = admin.getSecurityRole().getId().equals("1");
		boolean isSelf = admin.getId().equals(user.getId());
		
		return isAdmin && !isSelf;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#deleteUser(java.lang.String)
	 */
	@Override
	public void deleteUser(String userid) {
		userDAO.delete(userid);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#getSecurityQuestionOptions(java.lang.String)
	 */
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
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#getSecurityQuestionOptionsForEmail(java.lang.String)
	 */
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
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#saveUpdateUser(mat.client.admin.ManageUsersDetailModel)
	 */
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
		if(model.isActive() && (user.getStatus()!= null) && !user.getStatus().getId().equals("1")) {
			reactivatingUser = true;
		}
		User exsitingUser = userDAO.findByEmail(model.getEmailAddress());
		if((exsitingUser != null) && (!(exsitingUser.getId().equals(user.getId()) ) )) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateUserResult.ID_NOT_UNIQUE);
		}
		else{
			setModelFieldsOnUser(model, user);
			
			
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
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#getFooterURLs()
	 */
	@Override
	public List<String> getFooterURLs(){
		List<String> footerUrls = new ArrayList<String>();
		footerUrls.add(accessibilityUrl);
		footerUrls.add(privacyPolicyUseUrl);
		footerUrls.add(termsOfUseUrl);
		footerUrls.add(userGuideUrl);
		return footerUrls;
	}
	
	/**
	 * Sets the model fields on user.
	 * 
	 * @param model
	 *            the model
	 * @param user
	 *            the user
	 */
	private void setModelFieldsOnUser(ManageUsersDetailModel model, User user) {
		user.setFirstName(model.getFirstName());
		user.setLastName(model.getLastName());
		user.setMiddleInit(model.getMiddleInitial());
		user.setTitle(model.getTitle());
		user.setEmailAddress(model.getEmailAddress());
		user.setPhoneNumber(model.getPhoneNumber());
		user.setStatus(getStatusObject(model.isActive()));
		user.setSecurityRole(getRole(model.getRole()));
		//user.setOrgOID(model.getOid());
		//user.setRootOID(model.getRootOid());
		//user.setOrganizationName(model.getOrganization());
		
		if(model.isActive()){
			Organization organization = organizationDAO.find(Long.parseLong(model.getOrganizationId()));
			user.setOrganization(organization);
		} else if(!model.isActive()){
			Organization organizationRevoked = organizationDAO.find(Long.parseLong("1"));// 1 org id in db has blank organization name and oid.
			user.setOrganization(organizationRevoked);
		}
		
		// if the user was activated, set term date to null and set the activation date
		if(model.isBeingActivated()) {
			user.setTerminationDate(null);
			user.setSignInDate(null);
			user.setSignOutDate(null);
			user.setActivationDate(new Date());
		}
		
		// if the user is being revoked/terminated, update the termination date
		else if(model.isBeingRevoked()) {
			user.setTerminationDate(new Date());
		}
	}
	
	/**
	 * Gets the status object.
	 * 
	 * @param isActive
	 *            the is active
	 * @return the status object
	 */
	private Status getStatusObject(boolean isActive) {
		String id = isActive ? "1" : "2";
		return statusDAO.find(id);
	}
	
	/**
	 * Gets the role.
	 * 
	 * @param value
	 *            the value
	 * @return the role
	 */
	private SecurityRole getRole(String value) {
		for(SecurityRole role : securityRoleDAO.find()) {
			if(role.getId().equals(value)) {
				return role;
			}
		}
		return null;
	}
	
	//US212
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#setUserSignInDate(java.lang.String)
	 */
	@Override
	public void setUserSignInDate(String userid) {
		userDAO.setUserSignInDate(userid);
	}
	//US212
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#setUserSignOutDate(java.lang.String)
	 */
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
	/**
	 * Generate unique login id.
	 * 
	 * @param firstName
	 *            the first name
	 * @param lastName
	 *            the last name
	 * @return the string
	 */
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
	
	/**
	 * Sets the accessibility url.
	 * 
	 * @param accessibilityUrl
	 *            the new accessibility url
	 */
	public void setAccessibilityUrl(String accessibilityUrl) {
		this.accessibilityUrl = accessibilityUrl;
	}
	
	
	
	/**
	 * Gets the accessibility url.
	 * 
	 * @return the accessibility url
	 */
	public String getAccessibilityUrl() {
		return accessibilityUrl;
	}
	
	
	
	/**
	 * Sets the terms of use url.
	 * 
	 * @param termsOfUseUrl
	 *            the new terms of use url
	 */
	public void setTermsOfUseUrl(String termsOfUseUrl) {
		this.termsOfUseUrl = termsOfUseUrl;
	}
	
	
	
	/**
	 * Gets the terms of use url.
	 * 
	 * @return the terms of use url
	 */
	public String getTermsOfUseUrl() {
		return termsOfUseUrl;
	}
	
	
	
	/**
	 * Sets the privacy policy use url.
	 * 
	 * @param privacyPolicyUseUrl
	 *            the new privacy policy use url
	 */
	public void setPrivacyPolicyUseUrl(String privacyPolicyUseUrl) {
		this.privacyPolicyUseUrl = privacyPolicyUseUrl;
	}
	
	
	
	/**
	 * Gets the privacy policy use url.
	 * 
	 * @return the privacy policy use url
	 */
	public String getPrivacyPolicyUseUrl() {
		return privacyPolicyUseUrl;
	}
	
	/**
	 * Sets the user guide url.
	 * 
	 * @param userGuideUrl
	 *            the new user guide url
	 */
	public void setUserGuideUrl(String userGuideUrl) {
		this.userGuideUrl = userGuideUrl;
	}
	
	/**
	 * Gets the user guide url.
	 * 
	 * @return the user guide url
	 */
	public String getUserGuideUrl() {
		return userGuideUrl;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#updateOnSignOut(java.lang.String, java.lang.String, java.lang.String)
	 */
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
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#getSecurityQuestion(java.lang.String)
	 */
	@Override
	public String getSecurityQuestion(String userLoginID) {
		return userDAO.getRandomSecurityQuestion(userLoginID);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#getAllNonAdminActiveUsers()
	 */
	@Override
	public List<User> getAllNonAdminActiveUsers(){
		return userDAO.getAllNonAdminActiveUsers();
	}
	
	@Override
	public List<User> getAllUsers(){
		return userDAO.getAllUsers();
	}
	
	/**
	 * Notify user of account locked.
	 * 
	 * @param user
	 *            the user
	 */
	public void notifyUserOfAccountLocked(User user) {
		SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
		msg.setSubject(ServerConstants.ACCOUNT_LOCKED_SUBJECT + ServerConstants.getEnvName());
		String text = ServerConstants.ACCOUNT_LOCKED_MESSAGE;
		msg.setTo(user.getEmailAddress());
		msg.setText(text);
		try {
			mailSender.send(msg);
		}
		catch(MailException exc) {
			logger.error(exc);
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#isLockedUser(java.lang.String)
	 */
	@Override
	public boolean isLockedUser(String loginId) {
		User user = userDAO.findByLoginId(loginId);
		if((user != null) && ((user.getLockedOutDate() != null) || (user.getPassword().getPasswordlockCounter() >= 3))){
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.UserService#searchForNonTerminatedUsers()
	 */
	@Override
	public List<User> searchForNonTerminatedUsers() {
		return userDAO.searchForNonTerminatedUser();
	}
	
	
	/**
	 *  temporary and initial sign in password should not be stored in password History
	 *  isValidPwd boolean is set for special case where current valid password becomes temporary
	 *  password when it exceeds 60 days limit and it Should be added to password history.
	 * *
	 *
	 * @param user the user
	 * @param isValidPwd the is valid pwd
	 */
	@Override
	public void addByUpdateUserPasswordHistory(User user, boolean isValidPwd){
		List<UserPasswordHistory> pwdHistoryList = userPasswordHistoryDAO.getPasswordHistory(user.getId());
		if(isValidPwd || !(user.getPassword().isInitial()
				|| user.getPassword().isTemporaryPassword())) {
			UserPasswordHistory passwordHistory = new UserPasswordHistory();
			passwordHistory.setUser(user);
			passwordHistory.setPassword(user.getPassword().getPassword());
			passwordHistory.setSalt(user.getPassword().getSalt());
			passwordHistory.setCreatedDate(user.getPassword().getCreatedDate());
			if(pwdHistoryList.size()<PASSWORD_HISTORY_SIZE){
				user.getPasswordHistory().add(passwordHistory);
			} else {
				userPasswordHistoryDAO.addByUpdateUserPasswordHistory(user);
			}
		}
		
	}
	
}
