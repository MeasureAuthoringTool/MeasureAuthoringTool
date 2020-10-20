package mat.server;

import mat.client.login.LoginModel;
import mat.client.login.service.LoginService;
import mat.client.shared.MatException;
import mat.dao.UserDAO;
import mat.model.User;
import mat.model.UserSecurityQuestion;
import mat.server.logging.LogFactory;
import mat.server.service.LoginCredentialService;
import mat.server.service.UserService;
import mat.server.util.UMLSSessionTicket;
import mat.shared.HarpConstants;
import mat.shared.HashUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * The Class LoginServiceImpl.
 */
@SuppressWarnings("serial")
@Service
public class LoginServiceImpl extends SpringRemoteServiceServlet implements LoginService {

    private static final Log logger = LogFactory.getLog(LoginServiceImpl.class);

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginCredentialService loginCredentialService;

    @Override
    public LoginModel initSession(Map<String, String> harpUserInfo) throws MatException {
        logger.debug("initSession::harpId::" + harpUserInfo.get(HarpConstants.HARP_ID));
        HttpSession session = getThreadLocalRequest().getSession();
        if (userService.isHarpUserLockedRevoked(harpUserInfo.get(HarpConstants.HARP_ID))) {
            throw new MatException("MAT_ACCOUNT_REVOKED_LOCKED");
        }
        return loginCredentialService.initSession(harpUserInfo, session.getId());
    }

    @Override
    public Boolean checkForAssociatedHarpId(String harpId) throws MatException {
        try {
            return userDAO.findAssociatedHarpId(harpId);
        } catch (Exception e) {
            throw new MatException("Unable to verify if user has associated Harp Id");
        }
    }

    @Override
    public String getSecurityQuestionToVerifyHarpUser(String loginId, String password) throws MatException {
        try {
            if (isValidPassword(loginId, password)) {
                return userDAO.getRandomSecurityQuestion(loginId);
            } else {
                throw new MatException("Invalid User");
            }
        } catch (Exception e) {
            throw new MatException("Unable to retrieve a security question to verify user");
        }
    }

    @Override
    public boolean verifyHarpUser(String securityQuestion, String securityAnswer, String loginId, Map<String, String> harpUserInfo) throws MatException {
        User user = userDAO.findByLoginId(loginId);
        if (StringUtils.isNotBlank(securityAnswer)) {
            for (UserSecurityQuestion q : user.getUserSecurityQuestions()) {
                if (q.getSecurityQuestions().getQuestion().equalsIgnoreCase(securityQuestion)) {
                    if (HashUtility.getSecurityQuestionHash(q.getSalt(), securityAnswer).equalsIgnoreCase(q.getSecurityAnswer())) {
                        saveHarpUserInfo(harpUserInfo, loginId);
                        if (userService.isHarpUserLockedRevoked(harpUserInfo.get(HarpConstants.HARP_ID))) {
                            throw new MatException("MAT_ACCOUNT_REVOKED_LOCKED");
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isValidPassword(String loginId, String password) {
        return loginCredentialService.isValidPassword(loginId, password);
    }

    @Override
    public void switchRole(String newRole) {
        loginCredentialService.switchRole(newRole);
    }

    @Override
    public void switchUser(Map<String, String> harpUserInfo, String newUserId) {
        String sessionId = getThreadLocalRequest().getSession().getId();
        logger.debug("LoginnService::switchUser HARP_ID: " + harpUserInfo.get(HarpConstants.HARP_ID) + " Session ID: " + sessionId + " New User ID: " + newUserId);
        loginCredentialService.switchUser(harpUserInfo, newUserId, sessionId);
    }

    private void saveHarpUserInfo(Map<String, String> harpUserInfo, String loginId) throws MatException {
        logger.debug("User Verified, updating user information of::harpId::" + harpUserInfo.get(HarpConstants.HARP_ID));
        HttpSession session = getThreadLocalRequest().getSession();
        loginCredentialService.saveHarpUserInfo(harpUserInfo, loginId, session.getId());
    }

    @Override
    public List<String> getFooterURLs() {
        List<String> footerURLs = userService.getFooterURLs();
        return footerURLs;
    }

    @Override
    public String updateOnSignOut(String userId, String emailId, String activityType) {
        UMLSSessionTicket.remove(getThreadLocalRequest().getSession().getId());
        String resultStr = userService.updateOnSignOut(userId, emailId, activityType);
        SecurityContextHolder.clearContext();
        getThreadLocalRequest().getSession().invalidate();
        logger.debug("User Session Invalidated at :::: " + new Date());
        logger.debug("In UserServiceImpl Signout Update " + resultStr);
        return resultStr;
    }

}
