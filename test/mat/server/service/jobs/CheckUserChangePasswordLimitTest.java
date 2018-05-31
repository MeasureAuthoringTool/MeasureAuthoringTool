package mat.server.service.jobs;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import mat.dao.UserDAO;

public class CheckUserChangePasswordLimitTest extends TestContextConfiguration {
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
	public void verifySpringInitialization() {
		assertNotNull("CheckUserChangePasswordLimit bean is not null", checkUserChangePasswordLimit);
	}
	
	@Ignore("We don't want to send emails to all users. "
			+ "Comment the ignore and add logic for specific user when testing")
	@Test
	public void testWarningEmail() {
		checkUserChangePasswordLimit.CheckUserPasswordLimitDays();
	}
}
