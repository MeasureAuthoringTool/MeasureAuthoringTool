package mat.server.service.jobs;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@RunWith(SpringJUnit4ClassRunner.class)
@org.springframework.context.annotation.Configuration
@ContextConfiguration({
		"file:**/applicationContext-mail.xml", 
		"file:**/war/WEB-INF/mat-persistence.xml",
		"file:**/applicationContext-service.xml"})
@Transactional(transactionManager="txManager")
public class IntegrationTestConfig {
	
	@Test
	public void setup() {
		//Successful Spring Initialization
		assertTrue(true);
	}
	
	@Bean
	public CheckUserChangePasswordLimit checkUserChangePasswordLimit() {
		return new CheckUserChangePasswordLimit();
	}
	
	@Bean
	public OnetimeUserNotificationTask onetimeUserNotificationTask() {
		return new OnetimeUserNotificationTask();
	}
}
