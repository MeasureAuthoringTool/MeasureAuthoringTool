package mat.config;

import javax.sql.DataSource;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import mat.server.service.jobs.CheckUserChangePasswordLimit;
import mat.server.service.jobs.OnetimeUserNotificationTask;

/**
 * Class to set Test configuration. This is the base configuration
 * provided that should be included to setup the required beans
 * in your actual Test classes.
 * <p>
 * This wiring will help create Integration tests to execute
 * functionality using real data.
 */
@TestConfiguration
@ImportResource({
        "file:**/war/WEB-INF/applicationContext-mail.xml",
        "file:**/war/WEB-INF/applicationContext-service.xml"})
public class MatTestConfig {

    @MockBean
    DataSource dataSource;

    @Bean
    public CheckUserChangePasswordLimit checkUserChangePasswordLimit() {
        return new CheckUserChangePasswordLimit();
    }

    @Bean
    public OnetimeUserNotificationTask onetimeUserNotificationTask() {
        return new OnetimeUserNotificationTask();
    }

}
