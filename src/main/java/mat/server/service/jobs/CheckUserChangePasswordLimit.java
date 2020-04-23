package mat.server.service.jobs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import mat.dao.EmailAuditLogDAO;
import mat.dao.UserDAO;
import mat.model.EmailAuditLog;
import mat.model.User;
import mat.server.service.UserService;
import mat.server.util.ServerConstants;
import mat.shared.ConstantMessages;

/**
 * The Class CheckUserPasswordLimit.
 */
public class CheckUserChangePasswordLimit {

	/** The Constant logger. */
	private static final Log logger=LogFactory.getLog(CheckUserChangePasswordLimit.class);

	/** The user dao. */
	private UserDAO userDAO;

	private EmailAuditLogDAO emailAuditLogDAO;

	/** The mail sender. */
	private MailSender mailSender;

	/** The simple mail message. */
	private SimpleMailMessage simpleMailMessage;

	/** The warning day limit. */
	private int passwordwarningDayLimit;

	/** The expiry day limit. */
	private int passwordexpiryDayLimit;

	/** The warning mail template. */
	private String warningMailTemplate;

	/** The warning mail subject. */
	private String warningMailSubject;

	/** The expiry mail template. */
	private String expiryMailTemplate;

	/** The expiry mail subject. */
	private String expiryMailSubject;

	/** The Constant WARNING_EMAIL_FLAG. */
	private final static String WARNING_EMAIL_FLAG = "WARNING";

	/** The Constant EXPIRY_EMAIL_FLAG. */
	private final static String EXPIRY_EMAIL_FLAG = "EXPIRED";

	/** The user service. */
	private UserService userService;

	@Autowired private Configuration freemarkerConfiguration;

	/**
	 * Gets the expiry mail template.
	 *
	 * @return the expiry mail template
	 */
	public String getExpiryMailTemplate() {
		return expiryMailTemplate;
	}

	/**
	 * Sets the expiry mail template.
	 *
	 * @param expiryMailTemplate the new expiry mail template
	 */
	public void setExpiryMailTemplate(String expiryMailTemplate) {
		this.expiryMailTemplate = expiryMailTemplate;
	}

	/**
	 * Gets the expiry mail subject.
	 *
	 * @return the expiry mail subject
	 */
	public String getExpiryMailSubject() {
		return expiryMailSubject;
	}

	/**
	 * Sets the expiry mail subject.
	 *
	 * @param expiryMailSubject the new expiry mail subject
	 */
	public void setExpiryMailSubject(String expiryMailSubject) {
		this.expiryMailSubject = expiryMailSubject;
	}


	/**
	 * Gets the user dao.
	 *
	 * @return the user dao
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * Sets the user dao.
	 *
	 * @param userDAO the new user dao
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public EmailAuditLogDAO getEmailAuditLogDAO() {
		return emailAuditLogDAO;
	}

	public void setEmailAuditLogDAO(EmailAuditLogDAO emailAuditLogDAO) {
		this.emailAuditLogDAO = emailAuditLogDAO;
	}


	/**
	 * Gets the mail sender.
	 *
	 * @return the mail sender
	 */
	public MailSender getMailSender() {
		return mailSender;
	}

	/**
	 * Sets the mail sender.
	 *
	 * @param mailSender the new mail sender
	 */
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Gets the simple mail message.
	 *
	 * @return the simple mail message
	 */
	public SimpleMailMessage getSimpleMailMessage() {
		return simpleMailMessage;
	}

	/**
	 * Sets the simple mail message.
	 *
	 * @param simpleMailMessage the new simple mail message
	 */
	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}

	/**
	 * Gets the passwordwarning day limit.
	 *
	 * @return the passwordwarning day limit
	 */
	public int getPasswordwarningDayLimit() {
		return passwordwarningDayLimit;
	}

	/**
	 * Sets the passwordwarning day limit.
	 *
	 * @param passwordwarningDayLimit the new passwordwarning day limit
	 */
	public void setPasswordwarningDayLimit(int passwordwarningDayLimit) {
		this.passwordwarningDayLimit = passwordwarningDayLimit;
	}

	/**
	 * Gets the passwordexpiry day limit.
	 *
	 * @return the passwordexpiry day limit
	 */
	public int getPasswordexpiryDayLimit() {
		return passwordexpiryDayLimit;
	}

	/**
	 * Sets the passwordexpiry day limit.
	 *
	 * @param passwordexpiryDayLimit the new passwordexpiry day limit
	 */
	public void setPasswordexpiryDayLimit(int passwordexpiryDayLimit) {
		this.passwordexpiryDayLimit = passwordexpiryDayLimit;
	}

	/**
	 * Gets the warning mail template.
	 *
	 * @return the warning mail template
	 */
	public String getWarningMailTemplate() {
		return warningMailTemplate;
	}

	/**
	 * Sets the warning mail template.
	 *
	 * @param warningMailTemplate the new warning mail template
	 */
	public void setWarningMailTemplate(String warningMailTemplate) {
		this.warningMailTemplate = warningMailTemplate;
	}

	/**
	 * Gets the warning mail subject.
	 *
	 * @return the warning mail subject
	 */
	public String getWarningMailSubject() {
		return warningMailSubject;
	}

	/**
	 * Sets the warning mail subject.
	 *
	 * @param warningMailSubject the new warning mail subject
	 */
	public void setWarningMailSubject(String warningMailSubject) {
		this.warningMailSubject = warningMailSubject;
	}

	/**
	 * Gets the user service.
	 *
	 * @return the user service
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user service.
	 *
	 * @param userService the new user service
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Gets the warning email flag.
	 *
	 * @return the warning email flag
	 */
	public static String getWarningEmailFlag() {
		return WARNING_EMAIL_FLAG;
	}



	/**
	 * Gets the expiry email flag.
	 *
	 * @return the expiry email flag
	 */
	public static String getExpiryEmailFlag() {
		return EXPIRY_EMAIL_FLAG;
	}

	/**
	 * Method to Send
	 * 1.Warning Email for Warning Day Limit 35 days.
	 * 2.Password change screen for day limit >50 days
	 *
	 * @return void
	 */
	public void CheckUserPasswordLimitDays(){

		logger.info(" :: CheckUserPasswordLimitDays Method START :: ");

		CheckUserLoginPasswordDays(passwordwarningDayLimit,WARNING_EMAIL_FLAG);
		CheckUserLoginPasswordDays(passwordexpiryDayLimit,EXPIRY_EMAIL_FLAG);

		logger.info(" :: CheckUserPasswordLimitDays Method END :: ");

	}

	/**
	 * Check user login password days.
	 *
	 * @param noOfDaysPasswordLimit the no of days limit
	 * @param emailType the email type
	 */
	private void CheckUserLoginPasswordDays(final long noOfDaysPasswordLimit, final String emailType){

		logger.info(" :: checkUserLoginDays Method START :: for Sending " + emailType + " Type Email");
		//Get all the Users
				final List<User> users = userDAO.find();
				final List<User> emailUsers=checkUsersLastPassword(noOfDaysPasswordLimit,users);
				final Map<String, Object> model= new HashMap<String, Object>();
				final Map<String, String> content= new HashMap<String, String>();
				final String envirUrl = ServerConstants.getEnvURL();

				for(User user:emailUsers){

					//Send 45 days password limit email for all the users in the list.
					logger.info("Sending email to "+user.getFirstName());
					simpleMailMessage.setTo(user.getEmailAddress());

					//Creation of the model map can be its own method.
					content.put("User", "Measure Authoring Tool User");

					/**
					 * If the user is not a normal user then set the user role in the email
					 */
					String userRole = "";
					if(! (user.getSecurityRole().getId().trim().equals("3")) ){
						userRole = "("+user.getSecurityRole().getDescription()+")";
					}
					content.put("rolename",userRole);

                    content.put(ConstantMessages.HARPID, user.getHarpId());
                    content.put(ConstantMessages.USER_EMAIL, user.getEmailAddress());
                    content.put(ConstantMessages.URL, envirUrl);

					//5 days Expiry Date
				    if(passwordexpiryDayLimit==noOfDaysPasswordLimit) {
						final String expiryDate=getFormattedExpiryDate(new Date(),5-1);
						content.put("passwordExpiryDate",expiryDate );
					}

					model.put("content", content);
					String text = null;
					try {
						if(WARNING_EMAIL_FLAG.equals(emailType)){
							text = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(warningMailTemplate), model);
							simpleMailMessage.setText(text);
							simpleMailMessage.setSubject(warningMailSubject + ServerConstants.getEnvName());
						}else if (EXPIRY_EMAIL_FLAG.equals(emailType)){
							text = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(expiryMailTemplate), model);
							simpleMailMessage.setText(text);
							simpleMailMessage.setSubject(expiryMailSubject + ServerConstants.getEnvName());
						}
						mailSender.send(simpleMailMessage);
						EmailAuditLog emailAudit = new EmailAuditLog();
						emailAudit.setActivityType("Password " + emailType + " email sent.");
						emailAudit.setTimestamp(new Date());
						emailAudit.setLoginId(user.getLoginId());
						emailAuditLogDAO.save(emailAudit);
						content.clear();
						model.clear();
						logger.info("Email Sent to "+user.getFirstName());
					} catch (IOException | TemplateException e) {
						e.printStackTrace();
					}
				}

				logger.info(" :: CheckUserLoginPasswordDays Method END :: ");

	}

	/**
	 * Check users last password date.
	 *
	 * @param passwordDayLimit the passwor day limit
	 * @param users the users
	 * @return the list
	 */
	private List<User> checkUsersLastPassword(final long passwordDayLimit,final List<User> users){

		logger.info(" :: checkUsersLastPassword Method Start :: ");

		final List<User> returnUserList = new ArrayList<User>();
		final Date passwordDaysAgo=getPasswordNumberOfDaysAgo((int)passwordDayLimit);
	    logger.info(passwordDayLimit + "passwordDaysAgo:"+passwordDaysAgo);

	    for(User user:users){
			Date lastPasswordCreatedDate = user.getPassword().getCreatedDate();

			if(lastPasswordCreatedDate == null || !checkValidUser(user)){
				continue;
			}

			lastPasswordCreatedDate = DateUtils.truncate(lastPasswordCreatedDate, Calendar.DATE);
			logger.info("User:"+user.getFirstName()+"  :::: last Created Password Date :::::   " + lastPasswordCreatedDate);
			//for User password equals 35 days
			if(passwordwarningDayLimit==passwordDayLimit){

				if(lastPasswordCreatedDate.equals(passwordDaysAgo)) {
					logger.info("User:"+user.getEmailAddress()+" who's last password was "+ passwordDayLimit +" days ago.");
					returnUserList.add(user);
					}else{
					logger.info("User:"+user.getEmailAddress()+" who's last password was not "+ passwordDayLimit +" days ago.");
					}
				}

			    // for User Password Greater than 60days
			 else if(passwordexpiryDayLimit==passwordDayLimit){

				if(lastPasswordCreatedDate.before((passwordDaysAgo))
						|| lastPasswordCreatedDate.equals(passwordDaysAgo)){
					logger.info("User:"+user.getEmailAddress()+" who's last password was more than "+ passwordDayLimit +" days ago.");
					returnUserList.add(user);
					//to maintain Password history
					getUserService().addByUpdateUserPasswordHistory(user, true);
					user.getPassword().setTemporaryPassword(true);
					user.getPassword().setCreatedDate(DateUtils.truncate(new Date(),Calendar.DATE));
					getUserService().saveExisting(user);

					/*returnUserList.add(user);
					 * MatContext.get().getEventBus().fireEvent(new TemporaryPasswordLoginEvent());*/
					}else{
					logger.info("User:"+user.getEmailAddress()+" who's last password was not more than "+ passwordDayLimit +" days ago.");
					}
				}

	    }
		logger.info(" :: checkUsersLastPassword Method END :: ");

	return returnUserList;
	}


   /**
    * Gets the password number of days ago.
    *
    * @param noOfDaysPasswordLimit the no of day limit
    * @return the password number of days ago
    */
   private Date getPasswordNumberOfDaysAgo(final int noOfDaysPasswordLimit) {

		logger.info(" :: getPasswordNumberOfDaysAgo Method START :: ");

		Date numberOfDaysAgo;
		numberOfDaysAgo=DateUtils.truncate(new Date(),Calendar.DATE);
		numberOfDaysAgo=DateUtils.addDays(numberOfDaysAgo, noOfDaysPasswordLimit);

		logger.info(" :: getPasswordNumberOfDaysAgo Method END :: " + numberOfDaysAgo);
		return numberOfDaysAgo;
	}


   /**
    * Check valid user.
    *
    * @param user the user
    * @return true, if successful
    */
   private boolean checkValidUser(final User user) {
		logger.info(" :: checkValidUser Method START :: ");

		Boolean isValidUser = true;

		//final Date terminationDate = user.getTerminationDate();
		final Date signInDate = user.getSignInDate();
		System.out.println("signInDate :: "+ signInDate);
		if(signInDate == null || user.getStatus().getStatusId().equals("2")){
			isValidUser = false;
		}

		logger.info(user.getFirstName() + " :: checkValidUser Method END :: isValidUser ::: "+ isValidUser);

		return isValidUser;
   }

   /**
    * Gets the formatted expiry date.
    *
    * @param startDate the start date
    * @param willExpireIn the will expire in
    * @return the formatted expiry date
    */
   private String getFormattedExpiryDate(Date startDate,int willExpireIn){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DATE, willExpireIn);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEEE, MMMMM d, yyyy");
		String returnDateString  = simpleDateFormat.format(calendar.getTime());
		return returnDateString;
	}
}
