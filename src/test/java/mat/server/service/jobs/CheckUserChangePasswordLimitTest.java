package mat.server.service.jobs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import mat.config.MatAppContextTest;
import mat.dao.UserDAO;

import static org.junit.Assert.assertNotNull;

public class CheckUserChangePasswordLimitTest extends MatAppContextTest {
    @MockBean
    UserDAO userDAO;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    SimpleMailMessage simpleMailMessage;
    @Autowired
    CheckUserChangePasswordLimit checkUserChangePasswordLimit;

    @BeforeEach
    public void before() {
        // Autowiring is not working here due to a mix of xml/java config and missing annotations.
        checkUserChangePasswordLimit.setUserDAO(userDAO);
    }

    @Test
    public void verifySpringInitialization() {
        assertNotNull("CheckUserChangePasswordLimit bean is not null", checkUserChangePasswordLimit);
    }

    @Test
    public void testWarningEmail() {
        checkUserChangePasswordLimit.checkUserPasswordLimitDays();
    }
}
