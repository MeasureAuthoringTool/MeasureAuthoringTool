package mat.server.service.jobs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Class to set Test configuration. This is the base configuration
 * provided that should be included to setup the required beans
 * in your actual Test classes. 
 * 
 * This wiring will help create Integration tests to execute
 * functionality using real data.
 */
@Configuration
@ImportResource({
		"file:**/war/WEB-INF/applicationContext-mail.xml", 
		"file:**/war/WEB-INF/mat-persistence.xml",
		"file:**/war/WEB-INF/applicationContext-service.xml"})
public class TestConfiguration {
	
	@Bean
	public CheckUserChangePasswordLimit checkUserChangePasswordLimit() {
		return new CheckUserChangePasswordLimit();
	}
	
	@Bean
	public OnetimeUserNotificationTask onetimeUserNotificationTask() {
		return new OnetimeUserNotificationTask();
	}
}
