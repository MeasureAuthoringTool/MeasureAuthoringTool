package mat.server.service.jobs;

import mat.config.MatAppContextTest;
import mat.dao.EmailAuditLogDAO;
import mat.dao.UserDAO;
import mat.model.SecurityRole;
import mat.model.Status;
import mat.model.User;
import mat.model.UserPassword;
import mat.server.service.UserService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CheckUserChangePasswordLimitTest extends MatAppContextTest {
    @MockBean
    UserService userService;
    @MockBean
    JavaMailSender mailSender;
    @MockBean
    private UserDAO userDAO;
    @MockBean
    private EmailAuditLogDAO emailAuditLogDAO;
    @Autowired
    private SimpleMailMessage simpleMailMessage;
    @Autowired
    private CheckUserChangePasswordLimit checkUserChangePasswordLimit;
    @Value("${mat.password.warning.dayLimit}")
    private int passwordWarningDayLimit;
    @Value("${mat.password.expiry.dayLimit}")
    private int passwordExpiryDayLimit;

    @Test
    public void verifySpringInitialization() {
        assertNotNull(checkUserChangePasswordLimit, "CheckUserChangePasswordLimit bean is not null");
    }

    @Test
    public void testWarningEmailPastDayLimit() {
        String harpId = "Password Expiration HARP ID";
        String email = "Password Expiration EMAIL";

        User testUser = buildNormalUser();
        testUser.setHarpId(harpId);
        testUser.setEmailAddress(email);
        testUser.getPassword().setCreatedDate(DateUtils.addDays(new Date(), passwordWarningDayLimit));

        when(userDAO.find()).thenReturn(List.of(testUser));
        checkUserChangePasswordLimit.checkUserPasswordLimitDays();

        assertFalse(testUser.getPassword().isTemporaryPassword(), "Valid password was marked as temp.");
        assertNotNull(simpleMailMessage.getText());
        assertTrue(simpleMailMessage.getText().contains("It is time to change your Measure Authoring Tool password."));
        assertTrue(simpleMailMessage.getText().contains(harpId));
        assertTrue(simpleMailMessage.getText().contains(email));
        verify(emailAuditLogDAO, times(1)).save(any());
    }

    @Test
    public void testExpirationEmailPastDayLimit() {
        String harpId = "Password Expiration HARP ID";
        String email = "Password Expiration EMAIL";

        User testUser = buildNormalUser();
        testUser.setHarpId(harpId);
        testUser.setEmailAddress(email);
        testUser.getPassword().setCreatedDate(DateUtils.addDays(new Date(), passwordExpiryDayLimit));
        Date prevCreateDate = testUser.getPassword().getCreatedDate();

        when(userDAO.find()).thenReturn(List.of(testUser));
        checkUserChangePasswordLimit.checkUserPasswordLimitDays();

        assertTrue(testUser.getPassword().isTemporaryPassword(), "Expired password was not marked as temp.");
        assertTrue(testUser.getPassword().getCreatedDate().after(prevCreateDate));
        assertNotNull(simpleMailMessage.getText());
        assertTrue(simpleMailMessage.getText().contains("Your Measure Authoring Tool password is set to expire in 5 days."));
        assertTrue(simpleMailMessage.getText().contains(harpId));
        assertTrue(simpleMailMessage.getText().contains(email));
        verify(emailAuditLogDAO, times(1)).save(any());
    }

    @Test
    public void testExpirationEmailBeforeDayLimit() {
        User testUser = buildNormalUser();
        testUser.getPassword().setCreatedDate(DateUtils.addDays(new Date(), passwordExpiryDayLimit / 2));
        Date prevCreateDate = testUser.getPassword().getCreatedDate();

        when(userDAO.find()).thenReturn(List.of(testUser));
        checkUserChangePasswordLimit.checkUserPasswordLimitDays();

        assertFalse(testUser.getPassword().isTemporaryPassword(), "Valid password was marked as temp.");
        assertEquals(testUser.getPassword().getCreatedDate(), prevCreateDate);
        verify(emailAuditLogDAO, times(0)).save(any());
    }

    private User buildNormalUser() {
        User usr = new User();

        Status usrStatus = new Status();
        usrStatus.setStatusId("1"); //Active

        SecurityRole secRole = new SecurityRole();
        secRole.setId("3"); //Normal User

        usr.setSecurityRole(secRole);
        usr.setStatus(usrStatus);
        usr.setPassword(new UserPassword());
        usr.setId(UUID.randomUUID().toString());
        usr.setSignInDate(new Date());
        usr.setFirstName("Mark");
        usr.setLastName("Asread");
        usr.setEmailAddress("Mark.Asread@nowhere.meh");
        usr.setLoginId(UUID.randomUUID().toString());
        return usr;
    }
}
