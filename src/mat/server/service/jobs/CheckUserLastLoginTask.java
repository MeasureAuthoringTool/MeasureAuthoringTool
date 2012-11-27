package mat.server.service.jobs;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import mat.dao.UserDAO;
import mat.model.Status;
import mat.model.User;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;



public class CheckUserLastLoginTask {
	private static final Log logger = LogFactory.getLog(CheckUserLastLoginTask.class);
	
	private UserDAO userDAO;
	
	private MailSender mailSender;
	private SimpleMailMessage simpleMailMessage;
	
	private int warningDayLimit;
	private int expiryDayLimit;
	
	private String warningMailTemplate;
	private String warningMailSubject;
	
	private String expiryMailTemplate;
	private String expiryMailSubject;
	
	private VelocityEngine velocityEngine;
	
	private final static String WARNING_EMAIL_FLAG = "WARNING";
	private final static String EXPIRY_EMAIL_FLAG = "EXPIRED";
	
	/**
	 * Method to Send 
	 * 1.Warning Email for Warning Day Limit -90 days.
	 * 2.Account Expiration Email for day limit -180 days and 
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
		
		for(User user:emailUsers){
			
			//Send email for all the users in the list.
			logger.info("Sending email to "+user.getFirstName());
			simpleMailMessage.setTo(user.getEmailAddress());
			
			//Creation of the model map can be its own method.
			content.put("firstname", user.getFirstName());
			content.put("lastname", user.getLastName());
			
			model.put("content", content);
			String text = null;
			
			if(WARNING_EMAIL_FLAG.equals(emailType)){
				text = VelocityEngineUtils.mergeTemplateIntoString(
			               velocityEngine, warningMailTemplate, model);
				simpleMailMessage.setText(text);
				simpleMailMessage.setSubject(warningMailSubject);
			}
			else if (EXPIRY_EMAIL_FLAG.equals(emailType)){
				text = VelocityEngineUtils.mergeTemplateIntoString(
			               velocityEngine, expiryMailTemplate, model);
				simpleMailMessage.setText(text);
				simpleMailMessage.setSubject(expiryMailSubject);
				
				//Update Termination Date for User.
				updateUserTerminationDate(user);
			}	
			mailSender.send(simpleMailMessage);
			content.clear();
			model.clear();
			logger.info("Email Sent to "+user.getFirstName());
		}
		logger.info(" :: checkUserLoginDays Method END :: ");
	}
	
	/**
	 * Method Find Sub List of Users from Users List with Sign_in_date = noOfDayLimit.  
	 *  
	 * @param noOfDayLimit type integer.
	 * @param users type List.
	 * 
	 * @return List.
	 */
	private List<User> checkLastLogin(final long dayLimit, final List<User> users){
		
		logger.info(" :: checkLastLogin Method Start :: ");
		
		final List<User> returnUserList = new ArrayList<User>();
		final Date daysAgo=getNumberOfDaysAgo((int)dayLimit);

		logger.info(dayLimit + "daysAgo:"+daysAgo);
		
		for(User user:users){
			Date lastSignInDate = user.getSignInDate();
			
			if(lastSignInDate == null || !checkValidUser(user)){
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
	 * Method to Update Termination Date of User with no activity done in last 180 days.
	 * 
	 * @param User
	 * @return void
	 * */
	
	private void updateUserTerminationDate(final User user){
		
		logger.info(" :: updateUserTerminationDate Method START :: ");
		
		user.setTerminationDate(new Date());
		Status status = new Status();
		status.setId("2");
		status.setDescription("User Terminated Using Scheduler");
		user.setStatus(status);
		userDAO.save(user);
		logger.info(" :: updateUserTerminationDate Method END :: ");
		
	}
	
	/**
	 * Method to check if the User has valid ACTIVATION DATE, TERMINATION DATE. 
	 * @param user.
	 * @return boolean.
	 */
	private boolean checkValidUser(final User user) {
		logger.info(" :: checkValidUser Method START :: ");
		
		Boolean isValidUser = true;
		
		//final Date terminationDate = user.getTerminationDate();
		final Date signInDate = user.getSignInDate();
		System.out.println("signInDate :: "+ signInDate);
		if(signInDate == null || user.getStatus().getId().equals("2")){
			isValidUser = false;
		}
		
		logger.info(user.getFirstName() + " :: checkValidUser Method END :: isValidUser ::: "+ isValidUser);
		
		return isValidUser;
		
	}

	/**
	 * Method to find date = noOfDayLimit ago. 
	 * @param noOfDayLimit Type Integer.
	 * @return Date.
	 */
	private Date getNumberOfDaysAgo(final int noOfDayLimit) {
		
		logger.info(" :: getNumberOfDaysAgo Method START :: ");
		
		Date numberOfDaysAgo;
		numberOfDaysAgo=DateUtils.truncate(new Date(),Calendar.DATE);
		numberOfDaysAgo=DateUtils.addDays(numberOfDaysAgo, noOfDayLimit);
		
		logger.info(" :: getNumberOfDaysAgo Method END :: " + numberOfDaysAgo);
		return numberOfDaysAgo;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(final UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @param mailSender the mailSender to set
	 */
	public void setMailSender(final MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * @return the mailSender
	 */
	public MailSender getMailSender() {
		return mailSender;
	}

	/**
	 * @param simpleMailMessage the simpleMailMessage to set
	 */
	public void setSimpleMailMessage(final SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}

	/**
	 * @return the simpleMailMessage
	 */
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

	/**
	 * @param warningMailTemplate the warningMailTemplate to set
	 */
	public void setWarningMailTemplate(final String warningMailTemplate) {
		this.warningMailTemplate = warningMailTemplate;
	}

	/**
	 * @param warningMailSubject the warningMailSubject to set
	 */
	public void setWarningMailSubject(final String warningMailSubject) {
		this.warningMailSubject = warningMailSubject;
	}

	/**
	 * @return the warningMailSubject
	 */
	public String getWarningMailSubject() {
		return warningMailSubject;
	}

	/**
	 * @return the warningMailTemplate
	 */
	public String getWarningMailTemplate() {
		return warningMailTemplate;
	}

	/**
	 * @param expiryMailTemplate the expiryMailTemplate to set
	 */
	public void setExpiryMailTemplate(final String expiryMailTemplate) {
		this.expiryMailTemplate = expiryMailTemplate;
	}

	/**
	 * @param expiryMailSubject the expiryMailSubject to set
	 */
	public void setExpiryMailSubject(final String expiryMailSubject) {
		this.expiryMailSubject = expiryMailSubject;
	}

	/**
	 * @return the expiryMailSubject
	 */
	public String getExpiryMailSubject() {
		return expiryMailSubject;
	}

	/**
	 * @return the expiryMailTemplate
	 */
	public String getExpiryMailTemplate() {
		return expiryMailTemplate;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(final VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

}
