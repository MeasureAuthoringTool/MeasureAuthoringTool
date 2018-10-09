package mat.server.service.jobs;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import mat.config.TestContextConfiguration;
import mat.dao.UserDAO;
import mat.server.service.UserService;

public class OnetimeUserNotificationTaskTest extends TestContextConfiguration {
	@Autowired UserDAO userDAO;
	@Autowired UserService userService;
	@Autowired JavaMailSender mailSender;
	@Autowired SimpleMailMessage simpleMailMessage;
	@Autowired OnetimeUserNotificationTask onetimeUserNotificationTask;
	
	@Before
	public void setup() {
		onetimeUserNotificationTask.setUserDAO(userDAO);
		onetimeUserNotificationTask.setUserService(userService);
	}
	
	@Test
	public void verifySpringInitialization() {
		assertNotNull("OnetimeUserNotificationTask bean is not null", onetimeUserNotificationTask);
	}
	
	@Ignore("We don't want to send emails to all users. "
			+ "Comment the ignore and add logic for specific user when testing")
	@Test
	public void testSendOnetimeUserNotificationEmails() {
		onetimeUserNotificationTask.sendOnetimeUserNotificationEmails();
	}
}
