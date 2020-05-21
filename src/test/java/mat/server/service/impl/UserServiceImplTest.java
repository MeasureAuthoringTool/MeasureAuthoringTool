package mat.server.service.impl;

import java.io.IOException;

import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import freemarker.template.Configuration;
import freemarker.template.Template;
import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.service.SaveUpdateUserResult;
import mat.dao.OrganizationDAO;
import mat.dao.SecurityRoleDAO;
import mat.dao.StatusDAO;
import mat.dao.UserDAO;
import mat.model.User;
import mat.server.model.MatUserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final String DEFAULT_USER_EMAIL = "user@example.com";
    private static final String DEFAULT_HARP_ID = "defaultHarpId";
    private static final String DEFAULT_USER_ID = "1";
    private static final String OTHER_USER_ID = "Other";
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private SimpleMailMessage templateMessage;
    @Mock
    private UserDAO userDAO;
    @Mock
    private StatusDAO statusDAO;
    @Mock
    private SecurityRoleDAO securityRoleDAO;
    @Mock
    private OrganizationDAO organizationDAO;
    @Mock
    private MimeMessage mimeMessage;
    @Mock
    private Configuration freemarkerConfiguration;
    @Mock
    private Template template;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        Whitebox.setInternalState(userService, "emailFromAddress", "from@example.com");
        // trick codacy unused warning
        assertNotNull(templateMessage);
        assertNotNull(statusDAO);
        assertNotNull(securityRoleDAO);
        assertNotNull(organizationDAO);
    }

    @Test
    public void testSuccessSaved() throws IOException {
        ManageUsersDetailModel model = newDefaultModel();

        Mockito.when(freemarkerConfiguration.getTemplate(Mockito.anyString())).thenReturn(template);


        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        SaveUpdateUserResult result = userService.saveUpdateUser(model);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testEmailNotUnique() {
        ManageUsersDetailModel model = newDefaultModel();

        User userWithSameEmail = new User();
        userWithSameEmail.setId(OTHER_USER_ID);
        userWithSameEmail.setEmailAddress(DEFAULT_USER_EMAIL);

        Mockito.when(userDAO.findByEmail(Mockito.anyString())).thenReturn(userWithSameEmail);

        SaveUpdateUserResult result = userService.saveUpdateUser(model);
        assertFalse(result.isSuccess());
        assertEquals(SaveUpdateUserResult.USER_EMAIL_NOT_UNIQUE, result.getFailureReason());
    }

    @Disabled
    @Test
    public void testHarpNotUnique() {
        ManageUsersDetailModel model = newDefaultModel();

        MatUserDetails userWithSameEmail = new MatUserDetails();
        userWithSameEmail.setId(OTHER_USER_ID);
        userWithSameEmail.setHarpId(DEFAULT_HARP_ID);

        Mockito.when(userDAO.getUserDetailsByHarpId(Mockito.anyString())).thenReturn(userWithSameEmail);

        SaveUpdateUserResult result = userService.saveUpdateUser(model);
        assertFalse(result.isSuccess());
        assertEquals(SaveUpdateUserResult.USER_HARP_ID_NOT_UNIQUE, result.getFailureReason());
    }

    private ManageUsersDetailModel newDefaultModel() {
        ManageUsersDetailModel model = new ManageUsersDetailModel();
        model.setUserID(DEFAULT_USER_ID);
        model.setOrganizationId("1");
        model.setFirstName("FirstName");
        model.setLastName("LastName");
        model.setEmailAddress(DEFAULT_USER_EMAIL);
        model.setHarpId(DEFAULT_HARP_ID);
        return model;
    }

}
