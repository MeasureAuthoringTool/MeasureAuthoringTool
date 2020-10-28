package mat.config;

import lombok.extern.slf4j.Slf4j;
import mat.server.service.jobs.CheckUserChangePasswordLimit;
import mat.server.service.jobs.OnetimeUserNotificationTask;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Class to set Test configuration. This is the base configuration
 * provided that should be included to setup the required beans
 * in your actual Test classes.
 * <p>
 * This wiring will help create Integration tests to execute
 * functionality using real data.
 */
@Slf4j
@TestConfiguration
@ImportResource({
        "file:**/war/WEB-INF/applicationContext-mail.xml",
        "file:**/war/WEB-INF/applicationContext-service.xml"})
public class MatTestConfig {

    @MockBean
    private DataSource dataSource;

    @MockBean
    private HttpSession httpSession;

    @Primary
    @Bean
    public CheckUserChangePasswordLimit checkUserChangePasswordLimit() {
        return new CheckUserChangePasswordLimit();
    }

    @Primary
    @Bean
    public OnetimeUserNotificationTask onetimeUserNotificationTask() {
        return new OnetimeUserNotificationTask();
    }

    @Primary
    @Bean
    public SimpleMailMessage userLastLoginTemplateMessage() {
        return new SimpleMailMessage();
    }

    @PostConstruct
    public void init() {
        log.debug("Mute codacy compalin", dataSource);
        log.debug("Mute codacy compalin", httpSession);
    }
}
