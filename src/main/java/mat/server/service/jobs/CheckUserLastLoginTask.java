package mat.server.service.jobs;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import mat.model.Status;
import mat.model.User;
import mat.server.util.ServerConstants;
import mat.shared.ConstantMessages;



/**
 * The Class CheckUserLastLoginTask.
 */
public class CheckUserLastLoginTask {

	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CheckUserLastLoginTask.class);

	/** The user dao. */
	private UserDAO userDAO;

	/** The mail sender. */
	private MailSender mailSender;

	/** The simple mail message. */
	private SimpleMailMessage simpleMailMessage;

	/** The warning day limit. */
	private int warningDayLimit;

	/** The expiry day limit. */
	private int expiryDayLimit;

	/** The warning mail template. */
	private String warningMailTemplate;

	/** The warning mail subject. */
	private String warningMailSubject;

	/** The expiry mail template. */
	private String expiryMailTemplate;

	/** The expiry mail subject. */
	private String expiryMailSubject;

	private EmailAuditLogDAO emailAuditLogDAO;

	/** The Constant WARNING_EMAIL_FLAG. */
	private final static String WARNING_EMAIL_FLAG = "WARNING";

	/** The Constant EXPIRY_EMAIL_FLAG. */
	private final static String EXPIRY_EMAIL_FLAG = "EXPIRED";

	@Autowired private Configuration freemarkerConfiguration;

	/**
	 * Method to Send
	 * 1.Warning Email for Warning Day Limit -30 days.
	 * 2.Account Expiration Email for day limit -60 days and
	 *  then marked user termination date to disable logging into the system.
	 *
	 * @return void
	 */
	public void checkUserLastLogin(){
		logger.info(" :: checkUserLastLogin Method START :: ");

		checkUserLoginDays(warningDayLimit,WARNING_EMAIL_FLAG);
		checkUserLoginDays(expiryDayLimit,EXPIRY_EMAIL_FLAG);

		logger.info(" :: checkUserLastLogin Method END :: ");
	}


	/**
	 * Method Find List of Users with Sign_in_date = noOfDayLimit and send email based on emailType using velocityEngineUtils.
	 *
	 * @param noOfDayLimit type integer.
	 * @param emailType type String.
	 *
	 * @return void
	 */
	private void checkUserLoginDays(final long noOfDayLimit, final String emailType) {

		logger.info(" :: checkUserLoginDays Method START :: for Sending " + emailType + " Type Email");

		//Get all the Users
		final List<User> users = userDAO.find();
		final List<User> emailUsers = checkLastLogin(noOfDayLimit, users);

		final Map<String, Object> model= new HashMap<String, Object>();
		final Map<String, String> content= new HashMap<String, String>();
		final String envirUrl = ServerConstants.getEnvURL();

		for(User user:emailUsers){

			//Send email for all the users in the list.
			logger.info("Sending email to "+user.getFirstName());
			simpleMailMessage.setTo(user.getEmailAddress());

			//Creation of the model map can be its own method.
			content.put("firstname", user.getFirstName());
			content.put("lastname", user.getLastName());

			/**
			 * If the user is not a normal user then set the user role in the email
			 */
			String userRole = "";
			if(! (user.getSecurityRole().getId().trim().equals("3")) ){
				userRole = "("+user.getSecurityRole().getDescription()+")";
			}
			content.put("rolename",userRole);

            content.put(ConstantMessages.HARPID, user.getHarpId());
            content.put(ConstantMessages.URL, envirUrl);
            content.put(ConstantMessages.USER_EMAIL, user.getEmailAddress());

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

					//Update Termination Date for User.
					updateUserTerminationDate(user);
				}
			} catch (IOException | TemplateException e) {
				e.printStackTrace();
			}

			mailSender.send(simpleMailMessage);
			EmailAuditLog emailAudit = new EmailAuditLog();
			emailAudit.setActivityType("User Account Termination " + emailType + " email sent.");
			emailAudit.setTimestamp(new Date());
			emailAudit.setLoginId(user.getLoginId());
			emailAuditLogDAO.save(emailAudit);


			content.clear();
			model.clear();
			logger.info("Email Sent to "+user.getFirstName());
		}
		logger.info(" :: checkUserLoginDays Method END :: ");
	}

	/**
	 * Method Find Sub List of Users from Users List with Sign_in_date =
	 * noOfDayLimit.
	 *
	 * @param dayLimit
	 *            the day limit
	 * @param users
	 *            type List.
	 * @return List.
	 */
	private List<User> checkLastLogin(final long dayLimit, final List<User> users){

		logger.info(" :: checkLastLogin Method Start :: ");

		final List<User> returnUserList = new ArrayList<User>();
		final Date daysAgo=getNumberOfDaysAgo((int)dayLimit);
		logger.info(dayLimit + "daysAgo:" + daysAgo);

		for(User user:users){
			Date lastSignInDate = user.getSignInDate();

			if (!checkValidUser(user)) {
				continue;
			}

			// MAT-6582:  If a user has never signed in, look at activation date
			if (lastSignInDate == null) {
				Date activationDate = user.getActivationDate();
				activationDate = DateUtils.truncate(activationDate, Calendar.DATE);
				logger.info("User:"+user.getFirstName()+"  :::: activationDate :::::   " + activationDate);
				if(activationDate.equals(daysAgo)) {
					logger.info("User:"+user.getEmailAddress()+" who has never logged in and was activated over "+ dayLimit +" days ago.");
					returnUserList.add(user);
				}else{
					logger.info("User:"+user.getEmailAddress()+" who has never logged in and was activated "+ dayLimit +" days ago.");
				}
				continue;
			}

			lastSignInDate = DateUtils.truncate(lastSignInDate, Calendar.DATE);
			logger.info("User:"+user.getFirstName()+"  :::: lastSignInDate :::::   " + lastSignInDate);
			if(lastSignInDate.equals(daysAgo)) {
				logger.info("User:"+user.getEmailAddress()+" who last logged "+ dayLimit +" days ago.");
				returnUserList.add(user);
			}else{
				logger.info("User:"+user.getEmailAddress()+" who was not last logged "+ dayLimit +" days ago.");
			}
		}
		logger.info(" :: checkLastLogin Method END :: ");
		return returnUserList;
	}

	/**
	 * Method to Update Termination Date of User with no activity done in last
	 * 180 days.
	 *
	 * @param user
	 *            the user
	 * @return void
	 */

	private void updateUserTerminationDate(final User user){

		logger.info(" :: updateUserTerminationDate Method START :: ");

		user.setTerminationDate(new Date());
		Status status = new Status();
		status.setStatusId("2");
		status.setDescription("User Terminated Using Scheduler");
		user.setStatus(status);
		userDAO.save(user);
		logger.info(" :: updateUserTerminationDate Method END :: ");

	}

	/**
	 * Method to check if the User has valid ACTIVATION DATE, TERMINATION DATE.
	 *
	 * @param user
	 *            the user
	 * @return boolean.
	 */
	private boolean checkValidUser(final User user) {
		logger.info(" :: checkValidUser Method START :: ");

		Boolean isValidUser = true;

		if(user.getStatus().getStatusId().equals("2")){
			isValidUser = false;
		}

		logger.info(user.getFirstName() + " :: checkValidUser Method END :: isValidUser ::: "+ isValidUser);

		return isValidUser;

	}

    /**
     * Method to find date = noOfDayLimit ago.
     *
     * @param noOfDayLimit Type Integer.
     * @return Date.
     */
    private Date getNumberOfDaysAgo(final int noOfDayLimit) {

        logger.info(" :: getNumberOfDaysAgo Method START :: ");

        Date numberOfDaysAgo;
        numberOfDaysAgo = DateUtils.truncate(new Date(), Calendar.DATE);
        numberOfDaysAgo = DateUtils.addDays(numberOfDaysAgo, noOfDayLimit);

        logger.info(" :: getNumberOfDaysAgo Method END :: " + numberOfDaysAgo);
        return numberOfDaysAgo;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setMailSender(final MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setSimpleMailMessage(final SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }

    public SimpleMailMessage getSimpleMailMessage() {
        return simpleMailMessage;
    }

    public int getWarningDayLimit() {
        return warningDayLimit;
    }

    public void setWarningDayLimit(final int warningDayLimit) {
        this.warningDayLimit = warningDayLimit;
    }

    public int getExpiryDayLimit() {
        return expiryDayLimit;
    }

    public void setExpiryDayLimit(final int expiryDayLimit) {
        this.expiryDayLimit = expiryDayLimit;
    }

    public void setWarningMailTemplate(final String warningMailTemplate) {
        this.warningMailTemplate = warningMailTemplate;
    }

    public void setWarningMailSubject(final String warningMailSubject) {
        this.warningMailSubject = warningMailSubject;
    }

    public String getWarningMailSubject() {
        return warningMailSubject;
    }

    public String getWarningMailTemplate() {
        return warningMailTemplate;
    }

    public void setExpiryMailTemplate(final String expiryMailTemplate) {
        this.expiryMailTemplate = expiryMailTemplate;
    }

    public void setExpiryMailSubject(final String expiryMailSubject) {
        this.expiryMailSubject = expiryMailSubject;
    }

    public String getExpiryMailSubject() {
        return expiryMailSubject;
    }

    public String getExpiryMailTemplate() {
        return expiryMailTemplate;
    }

    public EmailAuditLogDAO getEmailAuditLogDAO() {
        return emailAuditLogDAO;
    }


    public void setEmailAuditLogDAO(EmailAuditLogDAO emailAuditLogDAO) {
        this.emailAuditLogDAO = emailAuditLogDAO;
    }

}
