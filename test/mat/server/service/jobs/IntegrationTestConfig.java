package mat.server.service.jobs;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * To run this Integration Test Config please follow these steps :
 *  
 * Comment the 'propertyPlaceholderConfigurer' bean definition
 * in the mat-persistence.xml (this is needed because the bean
 * with same name in applicationContext-service.xml get overridden
 * causing issue with properties not getting loaded for configuration
 * 
 * Uncomment the '2FA_AUTH_CLASS' property in MAT.properties file
 * 
 * Comment the 'vsacapi' bean definition. Somehow we keep on 
 * getting the 'groovy/lang/GroovyObject' noClassDefFoundError
 * even though Groovy is bundled within the VSAC jar
 */
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
