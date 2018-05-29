package mat.server.service.jobs;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import mat.dao.UserDAO;
import mat.server.service.UserService;

public class OnetimeUserNotificationTaskTest extends IntegrationTestConfig {
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
	public void testSendOnetimeUserNotificationEmails() {
		onetimeUserNotificationTask.sendOnetimeUserNotificationEmails();
	}
}
