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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CheckUserLastLoginTaskTest extends MatAppContextTest {
    @MockBean
    protected UserService userService;
    @MockBean
    protected JavaMailSender mailSender;
    @MockBean
    private UserDAO userDAO;
    @MockBean
    private EmailAuditLogDAO emailAuditLogDAO;
    @Autowired
    private SimpleMailMessage simpleMailMessage;
    @Autowired
    private CheckUserLastLoginTask checkUserLastLoginTask;
    @Value("${mat.warning.dayLimit}")
    private int warningDayLimit;
    @Value("${mat.expiry.dayLimit}")
    private int expiryDayLimit;

    @Test
    public void verifySpringInitialization() {
        assertNotNull(checkUserLastLoginTask, "checkUserLastLoginTask bean is not null");
    }

    @Test
    public void testWarningEmail() {
        String harpId = "Inactivity Warning HARP ID";
        String email = "Inactivity Warning EMAIL";
        User testUser = buildNormalUser();
        testUser.setSignInDate(DateUtils.addDays(new Date(), warningDayLimit));
        testUser.setHarpId(harpId);
        testUser.setEmailAddress(email);

        when(userDAO.find()).thenReturn(List.of(testUser));
        checkUserLastLoginTask.checkUserLastLogin();

        assertEquals("1", testUser.getStatus().getStatusId());
        assertNotNull(simpleMailMessage.getText());
        assertTrue(simpleMailMessage.getText().contains("You have not signed into the Measure Authoring Tool in 30 days."));
        assertTrue(simpleMailMessage.getText().contains(harpId));
        assertTrue(simpleMailMessage.getText().contains(email));
        verify(emailAuditLogDAO, times(1)).save(any());
    }

    @Test
    public void testDeDupWarningEmail() {
        String userId = "Inactivity Warning Email User";
        User testUser = buildNormalUser();
        testUser.setSignInDate(DateUtils.addDays(new Date(), warningDayLimit - 5));
        testUser.setLoginId(userId);

        when(userDAO.find()).thenReturn(List.of(testUser));
        checkUserLastLoginTask.checkUserLastLogin();

        assertEquals("1", testUser.getStatus().getStatusId());
        verify(mailSender,times(0)).send(any(SimpleMailMessage.class));
        verify(emailAuditLogDAO, times(0)).save(any());
    }

    @Test
    public void testExpirationEmailInactiveUser() {
        String harpId = "Inactivity Warning HARP ID";
        String email = "Inactivity Warning EMAIL";
        User testUser = buildNormalUser();
        testUser.setHarpId(harpId);
        testUser.setEmailAddress(email);
        testUser.setSignInDate(DateUtils.addDays(new Date(), expiryDayLimit));

        when(userDAO.find()).thenReturn(List.of(testUser));
        checkUserLastLoginTask.checkUserLastLogin();

        assertEquals("2", testUser.getStatus().getStatusId());
        assertNotNull(simpleMailMessage.getText());
        assertTrue(simpleMailMessage.getText().contains("Your account has been inactive for 60 days and therefore, has been disabled."));
        assertTrue(simpleMailMessage.getText().contains(harpId));
        assertTrue(simpleMailMessage.getText().contains(email));
        verify(emailAuditLogDAO, times(1)).save(any());
        verify(userDAO, times(1)).save(any());
    }

    @Test
    public void testPastExpirationEmailInactiveUser() {
        String harpId = "Inactivity Warning HARP ID";
        String email = "Inactivity Warning EMAIL";

        User testUser = buildNormalUser();
        testUser.setHarpId(harpId);
        testUser.setEmailAddress(email);
        testUser.setSignInDate(DateUtils.addDays(new Date(), expiryDayLimit - 5));

        when(userDAO.find()).thenReturn(List.of(testUser));
        checkUserLastLoginTask.checkUserLastLogin();

        assertEquals("2", testUser.getStatus().getStatusId());
        assertNotNull(simpleMailMessage.getText());
        assertTrue(simpleMailMessage.getText().contains("Your account has been inactive for 60 days and therefore, has been disabled."));
        assertTrue(simpleMailMessage.getText().contains(email));
        assertTrue(simpleMailMessage.getText().contains(harpId));
        verify(emailAuditLogDAO, times(1)).save(any());
        verify(userDAO, times(1)).save(any());
    }

    @Test
    public void testExpirationEmailActiveUser() {
        User testUser = buildNormalUser();
        testUser.setSignInDate(DateUtils.addDays(new Date(), expiryDayLimit / 4));

        when(userDAO.find()).thenReturn(List.of(testUser));
        checkUserLastLoginTask.checkUserLastLogin();

        assertEquals("1", testUser.getStatus().getStatusId());
        verify(emailAuditLogDAO, times(0)).save(any());
        verify(userDAO, times(0)).save(any());
    }

    private User buildNormalUser() {
        User usr = new User();

        Status usrStatus = new Status();
        usrStatus.setStatusId("1"); //Active

        UserPassword usrPass = new UserPassword();
        usrPass.setCreatedDate(new Date());
        usrPass.setTemporaryPassword(false);

        SecurityRole secRole = new SecurityRole();
        secRole.setId("3"); //Normal User

        usr.setSecurityRole(secRole);
        usr.setStatus(usrStatus);
        usr.setPassword(usrPass);
        usr.setId(UUID.randomUUID().toString());
        usr.setSignInDate(new Date());
        usr.setFirstName("Test");
        usr.setLastName("User");
        usr.setEmailAddress("test.user@nowhere.meh");
        usr.setLoginId(UUID.randomUUID().toString());
        return usr;
    }
}
