package mat.server.service.impl;

import com.google.gwt.dev.util.collect.HashMap;
import mat.client.shared.MatRuntimeException;
import mat.model.SecurityRole;
import mat.server.LoggedInUserUtil;
import mat.server.hibernate.HibernateUserDetailService;
import mat.server.model.MatUserDetails;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggedInUserUtil.class, SecurityContextHolder.class})
public class LoginCredentialServiceImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private LoginCredentialServiceImpl loginCredentialService;
    @Mock
    private HibernateUserDetailService hibernateUserService;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(LoggedInUserUtil.class);
        PowerMockito.mockStatic(SecurityContextHolder.class);
    }

    @Test
    public void testSwitchUserPassed() {
        String userId = "1";
        String otherUserId = "newuserid1";
        String sessionId = "";
        String harpId1 = "harpId1";

        SecurityRole roles = new SecurityRole();
        roles.setId(SecurityRole.ADMIN_ROLE_ID);
        roles.setDescription(SecurityRole.ADMIN_ROLE);

        Map<String, String> harpUserInfo = new HashMap<>();

        MatUserDetails userDetails = new MatUserDetails();
        userDetails.setId(userId);
        userDetails.setHarpId(harpId1);
        userDetails.setRoles(roles);

        MatUserDetails otherUserDetails = new MatUserDetails();
        otherUserDetails.setId(otherUserId);
        otherUserDetails.setHarpId(harpId1);
        otherUserDetails.setRoles(roles);

        PreAuthenticatedAuthenticationToken token =
                new PreAuthenticatedAuthenticationToken(userDetails.getId(), "accessToken", userDetails.getAuthorities());
        token.setDetails(userDetails);

        PowerMockito.when(LoggedInUserUtil.getToken()).thenReturn(token);

        Mockito.when(hibernateUserService.loadUserByHarpId(harpId1)).thenReturn(userDetails);

        Mockito.when(hibernateUserService.loadUserById(otherUserId)).thenReturn(otherUserDetails);

        loginCredentialService.switchUser(harpUserInfo, otherUserId, sessionId);

        PowerMockito.verifyStatic(SecurityContextHolder.class, Mockito.times(1));
        SecurityContextHolder.setContext(Mockito.any(SecurityContext.class));
        // mute codacy false positive
        assertNotNull(loginCredentialService);
    }


    @Test
    public void testSwitchUserDifferentHarpId() {
        thrown.expect(MatRuntimeException.class);
        thrown.expectMessage("SWITCH_USER_HAS_OTHER_HARP_ID");

        String userId = "1";
        String otherUserId = "newuserid1";
        String sessionId = "";
        String harpId1 = "harpId1";

        SecurityRole roles = new SecurityRole();
        roles.setId(SecurityRole.ADMIN_ROLE_ID);
        roles.setDescription(SecurityRole.ADMIN_ROLE);

        Map<String, String> harpUserInfo = new HashMap<>();

        MatUserDetails userDetails = new MatUserDetails();
        userDetails.setId(userId);
        userDetails.setHarpId(harpId1);
        userDetails.setRoles(roles);

        MatUserDetails otherUserDetails = new MatUserDetails();
        otherUserDetails.setId(otherUserId);
        otherUserDetails.setHarpId("differentHarpId");
        otherUserDetails.setRoles(roles);

        PreAuthenticatedAuthenticationToken token =
                new PreAuthenticatedAuthenticationToken(userDetails.getId(), "accessToken", userDetails.getAuthorities());
        token.setDetails(userDetails);

        PowerMockito.when(LoggedInUserUtil.getToken()).thenReturn(token);

        Mockito.when(hibernateUserService.loadUserByHarpId(harpId1)).thenReturn(userDetails);

        Mockito.when(hibernateUserService.loadUserById(otherUserId)).thenReturn(otherUserDetails);

        loginCredentialService.switchUser(harpUserInfo, otherUserId, sessionId);
        // mute codacy false positive
        assertNotNull(loginCredentialService);
    }

}
