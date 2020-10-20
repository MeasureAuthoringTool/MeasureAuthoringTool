package mat.server;

import mat.client.myAccount.MyAccountModel;
import mat.client.myAccount.SecurityQuestionsModel;
import mat.client.myAccount.service.MyAccountService;
import mat.client.myAccount.service.SaveMyAccountResult;
import mat.dao.UserDAO;
import mat.dao.UserSecurityQuestionDAO;
import mat.model.SecurityQuestions;
import mat.model.User;
import mat.model.UserPreference;
import mat.model.UserSecurityQuestion;
import mat.server.logging.LogFactory;
import mat.server.service.SecurityQuestionsService;
import mat.server.service.UserService;
import mat.shared.HashUtility;
import mat.shared.MyAccountModelValidator;
import mat.shared.PasswordVerifier;
import mat.shared.SecurityQuestionVerifier;
import mat.shared.StringUtility;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
@Service
public class MyAccountServiceImpl extends SpringRemoteServiceServlet implements MyAccountService {

    /**
     * The Constant logger.
     */
    private static final Log logger = LogFactory.getLog(MyAccountServiceImpl.class);
    @Autowired
    private UserSecurityQuestionDAO userSecurityQuestionDAO;
    @Autowired
    private UserService userService;


    /**
     * Extract model.
     *
     * @param user the user
     * @return the my account model
     */
    private MyAccountModel extractModel(User user) {
        MyAccountModel model = new MyAccountModel();
        model.setFirstName(user.getFirstName());
        model.setMiddleInitial(user.getMiddleInit());
        model.setLastName(user.getLastName());
        model.setEmailAddress(user.getEmailAddress());
        model.setPhoneNumber(user.getPhoneNumber());
        model.setTitle(user.getTitle());
        model.setOrganization(user.getOrganizationName());
        model.setOid(user.getOrgOID());
        model.setLoginId(user.getLoginId());
        boolean enableFreeTextEditor = user.getUserPreference() != null && user.getUserPreference().isFreeTextEditorEnabled();
        model.setEnableFreeTextEditor(enableFreeTextEditor);
        logger.debug("Model Object for User " + user.getLoginId() + " is updated and returned with Organisation ::: " + model.getOrganization());
        return model;
    }

    /**
     * Sets the model fields on user.
     *
     * @param user  the user
     * @param model the model
     */
    private void setModelFieldsOnUser(User user, MyAccountModel model) {
        user.setFirstName(model.getFirstName());
        user.setMiddleInit(model.getMiddleInitial());
        user.setLastName(model.getLastName());
        user.setEmailAddress(model.getEmailAddress());
        user.setPhoneNumber(model.getPhoneNumber());
        user.setTitle(model.getTitle());
        UserPreference userPreference = user.getUserPreference() == null ? new UserPreference() : user.getUserPreference();
        userPreference.setUser(user);
        userPreference.setFreeTextEditorEnabled(model.isEnableFreeTextEditor());
        user.setUserPreference(userPreference);
    }


    /**
     * Gets the security questions service.
     *
     * @return the security questions service
     */
    private SecurityQuestionsService getSecurityQuestionsService() {
        return context.getBean(SecurityQuestionsService.class);
    }


    /* (non-Javadoc)
     * @see mat.client.myAccount.service.MyAccountService#getMyAccount()
     */
    @Override
    public MyAccountModel getMyAccount() throws IllegalArgumentException {
        User user = userService.getById(LoggedInUserUtil.getLoggedInUser());
        logger.debug("Fetched User ....." + user.getLoginId());
        return extractModel(user);
    }

    /* (non-Javadoc)
     * @see mat.client.myAccount.service.MyAccountService#saveMyAccount(mat.client.myAccount.MyAccountModel)
     */
    @Override
    public SaveMyAccountResult saveMyAccount(MyAccountModel model) {
        SaveMyAccountResult result = new SaveMyAccountResult();
        MyAccountModelValidator validator = new MyAccountModelValidator();
        model.scrubForMarkUp();
        List<String> messages = validator.validate(model);
        UserDAO userDAO = context.getBean(UserDAO.class);
        if (messages.size() != 0) {
            logger.debug("Server-Side Validation for saveMyAccount Failed for User :: " + LoggedInUserUtil.getLoggedInUser());
            result.setSuccess(false);
            result.setFailureReason(SaveMyAccountResult.SERVER_SIDE_VALIDATION);
            result.setMessages(messages);
        } else {
            //If there is no validation error messages then proceed to save.
            //TODO Add database constraint for OID to be non-nullable
            User user = userService.getById(LoggedInUserUtil.getLoggedInUser());
            User exsistingUser = userDAO.findByEmail(model.getEmailAddress());
            if (exsistingUser != null && (!(exsistingUser.getId().equals(user.getId())))) {
                result.setSuccess(false);
                result.setFailureReason(SaveMyAccountResult.ID_NOT_UNIQUE);
            } else {
                setModelFieldsOnUser(user, model);

                userService.saveExisting(user);
                result.setSuccess(true);
            }


        }
        return result;
    }

    /* (non-Javadoc)
     * @see mat.client.myAccount.service.MyAccountService#getSecurityQuestions()
     */
    @Override
    public SecurityQuestionsModel getSecurityQuestions() {
        User user = userService.getById(LoggedInUserUtil.getLoggedInUser());
        System.out.println("User ID in MyAccountServiceImpl is:::" + user.getLoginId());
        List<UserSecurityQuestion> secQuestions = user.getUserSecurityQuestions();

        SecurityQuestionsModel model = new SecurityQuestionsModel();
        if (secQuestions.size() > 0) {
            model.setQuestion1(secQuestions.get(0).getSecurityQuestions().getQuestion());
            model.setQuestion1Answer(secQuestions.get(0).getSecurityAnswer());
        }
        if (secQuestions.size() > 1) {
            model.setQuestion2(secQuestions.get(1).getSecurityQuestions().getQuestion());
            model.setQuestion2Answer(secQuestions.get(1).getSecurityAnswer());
        }
        if (secQuestions.size() > 2) {
            model.setQuestion3(secQuestions.get(2).getSecurityQuestions().getQuestion());
            model.setQuestion3Answer(secQuestions.get(2).getSecurityAnswer());
        }
        model.scrubForMarkUp();
        return model;
    }

    /* (non-Javadoc)
     * @see mat.client.myAccount.service.MyAccountService#saveSecurityQuestions(mat.client.myAccount.SecurityQuestionsModel)
     */
    @Override
    public SaveMyAccountResult saveSecurityQuestions(SecurityQuestionsModel model) {
        logger.debug("Saving security questions");
        SaveMyAccountResult result = new SaveMyAccountResult();
        model.scrubForMarkUp();
        SecurityQuestionVerifier sverifier =
                new SecurityQuestionVerifier(model.getQuestion1(), model.getQuestion1Answer(),
                        model.getQuestion2(), model.getQuestion2Answer(),
                        model.getQuestion3(), model.getQuestion3Answer());
        if (!sverifier.isValid()) {
            logger.debug("Server Side Validation Failed in saveSecurityQuestions for User:" + LoggedInUserUtil.getLoggedInUser());
            result.setSuccess(false);
            result.setMessages(sverifier.getMessages());
            result.setFailureReason(SaveMyAccountResult.SERVER_SIDE_VALIDATION);
        } else {
            User user = userService.getById(LoggedInUserUtil.getLoggedInUser());
            List<UserSecurityQuestion> secQuestions = user.getUserSecurityQuestions();
            while (secQuestions.size() < 3) {
                UserSecurityQuestion newQuestion = new UserSecurityQuestion();
                newQuestion.setUserId(user.getId());
                secQuestions.add(newQuestion);
            }

            String newQuestion1 = model.getQuestion1();
            SecurityQuestions secQue1 = getSecurityQuestionsService().getSecurityQuestionObj(newQuestion1);
            secQuestions.get(0).setSecurityQuestionId(secQue1.getQuestionId());
            secQuestions.get(0).setSecurityQuestions(secQue1);

            String originalAnswer1 = secQuestions.get(0).getSecurityAnswer();
            if (StringUtility.isEmptyOrNull(originalAnswer1) || !originalAnswer1.equalsIgnoreCase(model.getQuestion1Answer())) {
                String salt1 = UUID.randomUUID().toString();
                secQuestions.get(0).setSalt(salt1);
                String answer1 = HashUtility.getSecurityQuestionHash(salt1, model.getQuestion1Answer());
                secQuestions.get(0).setSecurityAnswer(answer1);
                secQuestions.get(0).setRowId("0");
                userSecurityQuestionDAO.save(secQuestions.get(0));
            }

            String newQuestion2 = model.getQuestion2();
            SecurityQuestions secQue2 = getSecurityQuestionsService().getSecurityQuestionObj(newQuestion2);
            secQuestions.get(1).setSecurityQuestionId(secQue2.getQuestionId());
            secQuestions.get(1).setSecurityQuestions(secQue2);

            String originalAnswer2 = secQuestions.get(1).getSecurityAnswer();
            if (StringUtility.isEmptyOrNull(originalAnswer2) || !originalAnswer2.equalsIgnoreCase(model.getQuestion2Answer())) {
                String salt2 = UUID.randomUUID().toString();
                secQuestions.get(1).setSalt(salt2);
                String answer2 = HashUtility.getSecurityQuestionHash(salt2, model.getQuestion2Answer());
                secQuestions.get(1).setSecurityAnswer(answer2);
                secQuestions.get(1).setRowId("1");
                userSecurityQuestionDAO.save(secQuestions.get(1));
            }

            String newQuestion3 = model.getQuestion3();
            SecurityQuestions secQue3 = getSecurityQuestionsService().getSecurityQuestionObj(newQuestion3);
            secQuestions.get(2).setSecurityQuestionId(secQue3.getQuestionId());
            secQuestions.get(2).setSecurityQuestions(secQue3);

            String originalAnswer3 = secQuestions.get(2).getSecurityAnswer();
            if (StringUtility.isEmptyOrNull(originalAnswer3) || !originalAnswer3.equalsIgnoreCase(model.getQuestion3Answer())) {
                String salt3 = UUID.randomUUID().toString();
                secQuestions.get(2).setSalt(salt3);
                String answer3 = HashUtility.getSecurityQuestionHash(salt3, model.getQuestion3Answer());
                secQuestions.get(2).setSecurityAnswer(answer3);
                secQuestions.get(2).setRowId("2");
                userSecurityQuestionDAO.save(secQuestions.get(2));
            }

            user.setUserSecurityQuestions(secQuestions);
            userService.saveExisting(user);
            result.setSuccess(true);
        }
        return result;
    }


    /* (non-Javadoc)
     * @see mat.client.myAccount.service.MyAccountService#changePassword(java.lang.String)
     */
    @Override
    public SaveMyAccountResult changePassword(String password) {
        SaveMyAccountResult result = new SaveMyAccountResult();
        String markupRegExp = "<[^>]+>";
        String noMarkupTextPwd = password.trim().replaceAll(markupRegExp, "");
        password = noMarkupTextPwd;
        PasswordVerifier verifier = new PasswordVerifier(password, password);

        if (!verifier.isValid()) {
            logger.debug("Server Side Validation Failed in changePassword for User:" + LoggedInUserUtil.getLoggedInUser());
            result.setSuccess(false);
            result.setMessages(verifier.getMessages());
            result.setFailureReason(SaveMyAccountResult.SERVER_SIDE_VALIDATION);
        } else {
            User user = userService.getById(LoggedInUserUtil.getLoggedInUser());
            //to maintain user password History
            userService.addByUpdateUserPasswordHistory(user, false);
            userService.setUserPassword(user, password, false);
            userService.saveExisting(user);
            result.setSuccess(true);
        }
        return result;

    }

    // US212
    @Override
    public void setUserSignInDate(String userid) {
        userService.setUserSignInDate(userid);
    }


    @Override
    public void setUserSignOutDate(String userid) {
        userService.setUserSignOutDate(userid);
    }
}
