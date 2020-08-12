package mat.server.service.jobs;

import mat.config.MatAppContextTest;
import mat.dao.MatFlagDAO;
import mat.dao.UserDAO;
import mat.model.MatFlag;
import mat.server.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

public class OnetimeUserNotificationTaskTest extends MatAppContextTest {
    @MockBean
    MatFlagDAO matFlagDAO;
    @MockBean
    UserDAO userDAO;
    @Autowired
    UserService userService;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    SimpleMailMessage simpleMailMessage;
    @Autowired
    OnetimeUserNotificationTask onetimeUserNotificationTask;

    @BeforeEach
    public void setup() {
        // Autowiring is not working due to a mix of xml/java config and missing annotations.
        onetimeUserNotificationTask.setUserDAO(userDAO);
        onetimeUserNotificationTask.setMatFlagDAO(matFlagDAO);
    }

    @Test
    public void verifySpringInitialization() {
        assertNotNull("OnetimeUserNotificationTask bean is not null", onetimeUserNotificationTask);
    }

    @Test
    public void testSendOnetimeUserNotificationEmails() {
        MatFlag matFlag = new MatFlag();
        matFlag.setFlag("0");

        Mockito.when(matFlagDAO.find()).thenReturn(Arrays.asList(matFlag));

        onetimeUserNotificationTask.sendOnetimeUserNotificationEmails();
    }
}
