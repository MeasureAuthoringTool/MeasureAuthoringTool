package mat.server.service.jobs;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import mat.dao.UserDAO;

public class CheckUserChangePasswordLimitTest extends IntegrationTestConfig {
	@Autowired UserDAO userDAO;
	@Autowired JavaMailSender mailSender;
	@Autowired SimpleMailMessage simpleMailMessage;
	@Autowired CheckUserChangePasswordLimit checkUserChangePasswordLimit;
	
	@Before
	public void setup() {
		checkUserChangePasswordLimit.setUserDAO(userDAO);
		checkUserChangePasswordLimit.setMailSender(mailSender);
		checkUserChangePasswordLimit.setSimpleMailMessage(simpleMailMessage);
	}
	
	@Test
	public void testWarningEmail() {
		checkUserChangePasswordLimit.CheckUserPasswordLimitDays();
	}
}
