package mat.server;

import mat.server.model.MatUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Iterator;

/**
 * The Class LoggedInUserUtil.
 */
public class LoggedInUserUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggedInUserUtil.class);

    private static String loggedInUser;

    private static String loggedInUserEmail;

    private static String loggedInLoginId;

    private static String loggedInUserName;
    private static String loggedInUserHarpId;

    /**
     * Gets the token.
     *
     * @return the token
     */
    public static PreAuthenticatedAuthenticationToken getToken() {
        //re-factored to support Anonymous user US 439
        PreAuthenticatedAuthenticationToken token = null;
        if (SecurityContextHolder.getContext() != null) {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                try {
                    if (SecurityContextHolder.getContext().getAuthentication() instanceof PreAuthenticatedAuthenticationToken) {
                        token = (PreAuthenticatedAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                    }
                } catch (Exception exc) {
                    LOGGER.error("getToken",exc);
                }
            }
        }
        return token;
    }

    public static void setLoggedInUser(String u) {
        loggedInUser = u;
    }

    public static String getLoggedInUser() {
        if (loggedInUser != null) {
            return loggedInUser;
        } else {
            PreAuthenticatedAuthenticationToken token = getToken();
            String userName = null;
            if (token != null) {
                userName = token.getName();
            }
            return userName;
        }
    }

    public static void setLoggedInLoginId(String loginid) {
        loggedInLoginId = loginid;
    }

    public static String getLoggedInLoginId() {
        if (loggedInLoginId != null) {
            return loggedInLoginId;
        } else {
            PreAuthenticatedAuthenticationToken token = getToken();
            String loginId = null;
            if (token != null) {
                loginId = ((MatUserDetails) token.getDetails()).getLoginId();
            }
            return loginId;
        }
    }

    public static void setLoggedInUserEmail(String e) {
        loggedInUserEmail = e;
    }

    public static String getLoggedInUserEmailAddress() {
        if (loggedInUserEmail != null) {
            return loggedInUserEmail;
        }
        PreAuthenticatedAuthenticationToken token = getToken();
        String emailAddress = null;
        if (token != null) {
            emailAddress = ((MatUserDetails) token.getDetails()).getEmailAddress();

        }
        return emailAddress;
    }

    public static String getLoggedInUserHarpId() {
        if (loggedInUserHarpId != null) {
            return loggedInUserHarpId;
        }
        PreAuthenticatedAuthenticationToken token = getToken();
        String harpId = null;
        if (token != null) {
            harpId = ((MatUserDetails) token.getDetails()).getHarpId();

        }
        return harpId;
    }

    public static String getLoggedInUserRole() {
        String role = null;
        PreAuthenticatedAuthenticationToken token = getToken();
        if (token != null) {
            Iterator<GrantedAuthority> iter = token.getAuthorities().iterator();
            if (iter.hasNext()) {
                role = iter.next().getAuthority();
            }
        }
        return role;
    }

    public static void setLoggedInUserName(String e) {
        loggedInUserName = e;
    }

    public static String getLoggedUserName() {
        if (loggedInUserName != null) {
            return loggedInUserName;
        } else {
            PreAuthenticatedAuthenticationToken token = getToken();
            String firstName = null;
            String lastName = null;
            if (token != null) {
                firstName = ((MatUserDetails) token.getDetails()).getUsername();
                lastName = ((MatUserDetails) token.getDetails()).getUserLastName();
            }
            return firstName + " " + lastName;
        }
    }

    public static String getLoggedInUserId() {
        String result = null;
        PreAuthenticatedAuthenticationToken token = getToken();
        if (token != null && token.getDetails() instanceof MatUserDetails) {
            result = ((MatUserDetails) token.getDetails()).getId();
        }
        return result;
    }
}
