package mat.server.service.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.service.SaveUpdateUserResult;
import mat.client.login.service.SecurityQuestionOptions;
import mat.client.shared.MatContext;
import mat.client.shared.NameValuePair;
import mat.dao.OrganizationDAO;
import mat.dao.SecurityRoleDAO;
import mat.dao.StatusDAO;
import mat.dao.TransactionAuditLogDAO;
import mat.dao.UserDAO;
import mat.dao.UserPasswordHistoryDAO;
import mat.model.Organization;
import mat.model.SecurityRole;
import mat.model.Status;
import mat.model.TransactionAuditLog;
import mat.model.User;
import mat.model.UserBonnieAccessInfo;
import mat.model.UserPassword;
import mat.model.UserPasswordHistory;
import mat.model.UserSecurityQuestion;
import mat.server.logging.LogFactory;
import mat.server.model.MatUserDetails;
import mat.server.service.UserService;
import mat.server.util.ServerConstants;
import mat.shared.ConstantMessages;
import mat.shared.ForgottenLoginIDResult;
import mat.shared.ForgottenPasswordResult;
import mat.shared.HashUtility;
import mat.shared.PasswordVerifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private static final Log logger = LogFactory.getLog(UserServiceImpl.class);

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "1234567890";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Random ID = new Random(System.currentTimeMillis());
    private static final int PASSWORD_HISTORY_SIZE = 5;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SimpleMailMessage templateMessage;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private StatusDAO statusDAO;
    @Autowired
    private SecurityRoleDAO securityRoleDAO;
    @Autowired
    private TransactionAuditLogDAO transactionAuditLogDAO;
    @Autowired
    private OrganizationDAO organizationDAO;
    @Autowired
    private UserPasswordHistoryDAO userPasswordHistoryDAO;
    @Autowired
    private Configuration freemarkerConfiguration;
    @Value("${mat.from.emailAddress}")
    private String emailFromAddress;
    @Value("${mat.accessibilitypolicy.url}")
    private String accessibilityUrl;
    @Value("${mat.termsofuse.url}")
    private String termsOfUseUrl;
    @Value("${mat.privacypolicy.url}")
    private String privacyPolicyUseUrl;
    @Value("${mat.userguide.url}")
    private String userGuideUrl;
    @Value("${mat.support.emailAddress}")
    private String supportEmailAddress;

    @Override
    public String generateRandomPassword() {
        logger.debug("In generateRandomPassword().....");
        String password = null;
        Random r = new Random(System.currentTimeMillis());
        StringBuilder pwdBuilder = new StringBuilder();
        pwdBuilder.append(Character.toUpperCase(ALPHABET.charAt(r.nextInt(ALPHABET.length()))));
        pwdBuilder.append(NUMERIC.charAt(r.nextInt(NUMERIC.length())));
        char[] specialArr = PasswordVerifier.getAllowedSpecialChars();
        pwdBuilder.append(specialArr[r.nextInt(specialArr.length)]);
        for (int i = pwdBuilder.length(); i < PasswordVerifier.getMinLength(); i++) {

            // need to guarantee no 3 consecutive characters
            char pre1 = pwdBuilder.charAt(pwdBuilder.length() - 1);
            char pre2 = pwdBuilder.charAt(pwdBuilder.length() - 2);
            char c = ALPHABET.charAt(r.nextInt(ALPHABET.length()));
            while ((c == pre1) || (c == pre2)) {
                c = ALPHABET.charAt(r.nextInt(ALPHABET.length()));
            }

            pwdBuilder.append(c);
        }
        password = pwdBuilder.toString();

        return password;
    }

    @Override
    public void activate(String userid) {
        logger.debug("In activate(String userid).....");
        User user = userDAO.find(userid);
        String newPassword = generateRandomPassword();
        if (user.getPassword() == null) {
            UserPassword pwd = new UserPassword();
            user.setPassword(pwd);
        }

        //to maintain user password History
        if (user.getPassword() != null) {
            addByUpdateUserPasswordHistory(user, false);
        }
        setUserPassword(user, newPassword, true);
        userDAO.save(user);
        notifyUserUnlocked(user, newPassword);
    }

    /**
     * Notify user of temporary password.
     *
     * @param user        the user
     * @param newPassword the new password
     */
    public void notifyUserUnlocked(User user, String newPassword) {
        logger.debug("In notifyUserUnlocked(User user, String newPassword).....");
        SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
        msg.setSubject(ServerConstants.TEMP_PWD_SUBJECT + ServerConstants.getEnvName());
        msg.setTo(user.getEmailAddress());

        String expiryDateString = getFormattedExpiryDate(new Date(), 5);

        // US 440. Re-factored to use template based framework
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(ConstantMessages.PASSWORD, newPassword);
        paramsMap.put(ConstantMessages.PASSWORD_EXPIRE_DATE, expiryDateString);
        paramsMap.put(ConstantMessages.URL, ServerConstants.getEnvURL());
        paramsMap.put(ConstantMessages.HARPID, user.getHarpId());
        paramsMap.put(ConstantMessages.USER_EMAIL, user.getEmailAddress());
        paramsMap.put(ConstantMessages.SUPPORT_EMAIL, supportEmailAddress);

        logger.debug("Sending email to " + user.getEmailAddress());
        try {
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("mail/tempPasswordTemplate.ftl"), paramsMap);
            msg.setText(text);
            mailSender.send(msg);
        } catch (IOException | TemplateException exc) {
            logger.error(exc);
        }

    }

    /**
     * Gets the formatted expiry date.
     *
     * @param startDate    the start date
     * @param willExpireIn the will expire in
     * @return the formatted expiry date
     */
    private String getFormattedExpiryDate(Date startDate, int willExpireIn) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, willExpireIn);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEEE, MMMMM d, yyyy");
        String returnDateString = simpleDateFormat.format(calendar.getTime());
        return returnDateString;
    }

    @Override
    public void setUserPassword(User user, String clearTextPassword, boolean isTemporary) {
        logger.debug("In setUserPassword(User user, String clearTextPassword, boolean isTemporary)........");
        String salt = UUID.randomUUID().toString();
        user.getPassword().setSalt(salt);
        String password = getPasswordHash(salt, clearTextPassword);
        user.getPassword().setPassword(password);
        user.getPassword().setCreatedDate(new Date());
        user.getPassword().setTemporaryPassword(isTemporary);
        user.setLockedOutDate(null);
        user.getPassword().setForgotPwdlockCounter(0);
        user.getPassword().setPasswordlockCounter(0);
    }

    @Override
    public ForgottenPasswordResult requestForgottenPassword(String loginId,
                                                            String securityQuestion, String securityAnswer, int invalidUserCounter) {
        logger.debug("In requestForgottenPassword(String loginId, String securityQuestion, String securityAnswer, int invalidUserCounter).......");
        ForgottenPasswordResult result = new ForgottenPasswordResult();
        result.setEmailSent(false);
        // logger.debug(" requestForgottenPassword   Login Id ====" + loginId);
        User user = null;
        try {
            user = userDAO.findByLoginId(loginId);
        } catch (ObjectNotFoundException exc) {
            logger.error(exc);
        }

        if (user == null) {
            result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTION_MISMATCH);
        } else if (user.getUserSecurityQuestions().size() != 3) {
            result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTIONS_NOT_SET);
        } else if (user.getLockedOutDate() != null) {
            result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTION_MISMATCH);
        } else if (!securityQuestionMatch(user, securityQuestion, securityAnswer)) {
            result.setFailureReason(ForgottenPasswordResult.SECURITY_QUESTION_MISMATCH);
            int lockCounter = user.getPassword().getForgotPwdlockCounter() + 1;
            if (lockCounter == 3) {
                user.setLockedOutDate(new Date());
                notifyUserOfAccountLocked(user);
            }
            user.getPassword().setForgotPwdlockCounter(lockCounter);
            userDAO.save(user);
        } else {

            Date lastSignIn = user.getSignInDate();
            Date lastSignOut = user.getSignOutDate();
            Date current = new Date();

            boolean isAlreadySignedIn = MatContext.get().isAlreadySignedIn(lastSignOut, lastSignIn, current);

            if (isAlreadySignedIn) {
                result.setFailureReason(ForgottenPasswordResult.USER_ALREADY_LOGGED_IN);
            } else {

                //to maitain passwordHistory
                addByUpdateUserPasswordHistory(user, false);
                String newPassword = generateRandomPassword();
                setUserPassword(user, newPassword, true);
                result.setEmailSent(true);
                sendResetPassword(user.getEmailAddress(), newPassword);
                user.setLockedOutDate(null);
                user.getPassword().setForgotPwdlockCounter(0);
                userDAO.save(user);
            }
        }
        return result;
    }

    @Override
    public ForgottenLoginIDResult requestForgottenLoginID(String email) {
        ForgottenLoginIDResult result = new ForgottenLoginIDResult();
        result.setEmailSent(false);
        logger.debug(" requestForgottenLoginID   email ====" + email);
        User user = null;
        boolean inValidEmail = false;
        try {
            if (isValidEmail(email)) {
                inValidEmail = true;
                user = userDAO.findByEmail(email);
            }
        } catch (ObjectNotFoundException exc) {
            logger.debug(" requestForgottenLoginID   Exception " + exc.getMessage());
        }

        if ((user == null) && inValidEmail) {
            result.setFailureReason(ForgottenLoginIDResult.EMAIL_NOT_FOUND_MSG);
            logger.debug(" requestForgottenLoginID   user not found for email ::" + email);
        } else if (!inValidEmail) {
            result.setFailureReason(ForgottenLoginIDResult.EMAIL_INVALID);
            logger.debug(" requestForgottenLoginID   Invalid email ::" + email);
        } else {

            Date lastSignIn = user.getSignInDate();
            Date lastSignOut = user.getSignOutDate();
            Date current = new Date();

            boolean isAlreadySignedIn = MatContext.get().isAlreadySignedIn(lastSignOut, lastSignIn, current);

            if (isAlreadySignedIn) {
                result.setFailureReason(ForgottenLoginIDResult.USER_ALREADY_LOGGED_IN);
            } else {
                logger.debug(" requestForgottenLoginID   User ID Found and email sent successfully to email address ::" + email);
                result.setEmailSent(true);
                notifyUserOfForgottenLoginId(user);
            }
        }
        return result;
    }

    /**
     * Checks if is valid email.
     *
     * @param emailAddress the email address
     * @return true, if is valid email
     */
    private boolean isValidEmail(String emailAddress) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(emailAddress);
        return matcher.matches();

    }

    /**
     * Security question match.
     *
     * @param user             the user
     * @param securityQuestion the security question
     * @param securityAnswer   the security answer
     * @return true, if successful
     */
    private boolean securityQuestionMatch(User user, String securityQuestion, String securityAnswer) {

        if (StringUtils.isNotBlank(securityAnswer)) {
            Optional<UserSecurityQuestion> secQuestion = user.getUserSecurityQuestions().stream().filter(q -> securityQuestion.equalsIgnoreCase(q.getSecurityQuestions().getQuestion())).findFirst();
            if (secQuestion.isPresent()) {
                UserSecurityQuestion usq = secQuestion.get();
                String hashedSecurityAnswer = HashUtility.getSecurityQuestionHash(usq.getSalt(), securityAnswer);
                return hashedSecurityAnswer.equalsIgnoreCase(usq.getSecurityAnswer());
            }
        }
        return false;
    }


    /**
     * Send reset password.
     *
     * @param email       the email
     * @param newPassword the new password
     */
    private void sendResetPassword(String email, String newPassword) {
        logger.debug("In sendResetPassword(String email, String newPassword)........" + newPassword);
        SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
        msg.setSubject(ServerConstants.TEMP_PWD_SUBJECT + ServerConstants.getEnvName());
        msg.setTo(email);
        String expiryDateString = getFormattedExpiryDate(new Date(), 5);
        //US 440. Re-factored to use template based framework
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(ConstantMessages.PASSWORD_EXPIRE_DATE, expiryDateString);
        paramsMap.put(ConstantMessages.PASSWORD, newPassword);
        paramsMap.put(ConstantMessages.URL, ServerConstants.getEnvURL());
        paramsMap.put(ConstantMessages.SUPPORT_EMAIL, supportEmailAddress);
        try {
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("mail/resetPasswordTemplate.ftl"), paramsMap);
            msg.setText(text);
            logger.debug("Sending email to " + email);
            mailSender.send(msg);
        } catch (MailException | IOException | TemplateException exc) {
            logger.error(exc);
        }
    }

    @Override
    public List<User> searchForUsersByName(String orgId) {
        if (orgId == null) {
            orgId = "";
        }
        return userDAO.searchForUsersByName(orgId);
    }

    @Override
    public List<User> searchForUsersWithActiveBonnie(String searchTerm) {
        List<User> resultList = new ArrayList<>();
        if (searchTerm == null) {
            searchTerm = "";
        }
        List<User> userList = userDAO.searchForUsersByName(searchTerm);
        for (User user : userList) {
            UserBonnieAccessInfo userBonnieAccessInfo = user.getUserBonnieAccessInfo();
            if (userBonnieAccessInfo != null) {
                resultList.add(user);
            }
        }
        return resultList;
    }

    @Override
    public List<MatUserDetails> getAllActiveUserDetailsByHarpId(String harpId) {
        return userDAO.getAllUserDetailsByHarpId(harpId).stream()
                .filter(MatUserDetails::isNotLockedOrRevoked)
                .collect(Collectors.toList());
    }

    @Override
    public HashMap<String, Organization> searchForUsedOrganizations() {
        return userDAO.searchAllUsedOrganizations();
    }

    @Override
    public List<User> searchNonAdminUsers(String orgId, int startIndex, int numResults) {
        if (orgId == null) {
            orgId = "";
        }
        return userDAO.searchNonAdminUsers(orgId, startIndex - 1, numResults);
    }

    @Override
    public User findByEmailID(String emailId) {
        return userDAO.findByEmail(emailId);
    }

    @Override
    public User getById(String id) {
        return userDAO.find(id);
    }


    @Override
    public void saveNew(User user) {
        logger.debug("In saveNew(User user)..........");
        if (user.getPassword() == null) {
            UserPassword pwd = new UserPassword();
            user.setPassword(pwd);
        }

        String newPassword = generateRandomPassword();
        user.setActivationDate(new Date());
        setUserPassword(user, newPassword, false);
        user.getPassword().setInitial(true);
        user.setStatus(statusDAO.find("1"));

        String newLoginId = generateUniqueLoginId(user.getFirstName(), user.getLastName());
        boolean isUniqueLoginId = false;

        while (!isUniqueLoginId) {
            if (!userDAO.findUniqueLoginId(newLoginId)) {
                isUniqueLoginId = true;
                user.setLoginId(newLoginId);
            } else {
                newLoginId = generateUniqueLoginId(user.getFirstName(), user.getLastName());
            }
        }
        userDAO.save(user);
        notifyUserOfNewAccount(user);
    }

    /**
     * Notify user of new account.
     *
     * @param user the user
     */
    public void notifyUserOfNewAccount(User user) {
        logger.debug("In notifyUserOfNewAccount(User user)..........");
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setSubject(ServerConstants.NEW_ACCESS_SUBJECT + ServerConstants.getEnvName());
            BodyPart body = new MimeBodyPart();
            HashMap<String, Object> paramsMap = new HashMap<>();
            paramsMap.put(ConstantMessages.HARPID, user.getHarpId());
            paramsMap.put(ConstantMessages.USER_EMAIL, user.getEmailAddress());
            paramsMap.put(ConstantMessages.URL, ServerConstants.getEnvURL());
            paramsMap.put(ConstantMessages.SUPPORT_EMAIL, supportEmailAddress);
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("mail/welcomeTemplate.ftl"), paramsMap);
            body.setContent(text, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(body);
            message.setFrom(new InternetAddress(emailFromAddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmailAddress()));
            message.setContent(multipart);

            mailSender.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            logger.error(e);
        }

    }

    /**
     * Notify user of forgotten login id.
     *
     * @param user the user
     */
    private void notifyUserOfForgottenLoginId(User user) {
        SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
        msg.setSubject(ServerConstants.FORGOT_LOGINID_SUBJECT + ServerConstants.getEnvName());
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(ConstantMessages.LOGINID, user.getLoginId());
        paramsMap.put(ConstantMessages.URL, ServerConstants.getEnvURL());
        paramsMap.put(ConstantMessages.SUPPORT_EMAIL, supportEmailAddress);
        try {
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("mail/forgotLoginIDTemplate.ftl"), paramsMap);
            msg.setTo(user.getEmailAddress());
            msg.setText(text);
            mailSender.send(msg);
        } catch (MailException | IOException | TemplateException exc) {
            logger.error(exc);
        }
    }

    @Override
    public String getPasswordHash(String salt, String password) {
        String hashed = hash(salt + password);
        return hashed;
    }

    private String hash(String s) {
        try {
            if (s == null) {
                s = "";
            }
            MessageDigest m = MessageDigest.getInstance("SHA-256");
            m.update(s.getBytes(), 0, s.length());
            return new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void saveExisting(User user) {
        userDAO.save(user);
    }

    @Override
    public boolean isAdminForUser(User admin, User user) {
        boolean isAdmin = admin.getSecurityRole().getId().equals("1");
        boolean isSelf = admin.getId().equals(user.getId());

        return isAdmin && !isSelf;
    }

    @Override
    public void deleteUser(String userid) {
        userDAO.delete(userid);
    }

    @Override
    public SecurityQuestionOptions getSecurityQuestionOptions(String loginId) {
        User user = userDAO.findByLoginId(loginId);
        SecurityQuestionOptions options = new SecurityQuestionOptions();
        options.setSecurityQuestions(new ArrayList<NameValuePair>());
        if (user != null) {
            options.setUserFound(true);
            for (UserSecurityQuestion q : user.getUserSecurityQuestions()) {
                NameValuePair nvp = new NameValuePair(q.getSecurityQuestions().getQuestion(), q.getSecurityQuestions().getQuestion());
                options.getSecurityQuestions().add(nvp);
            }
        } else {
            options.setUserFound(false);
        }
        return options;
    }

    @Override
    public SecurityQuestionOptions getSecurityQuestionOptionsForEmail(String email) {
        User user = userDAO.findByEmail(email);
        SecurityQuestionOptions options = new SecurityQuestionOptions();
        options.setSecurityQuestions(new ArrayList<NameValuePair>());
        if (user != null) {
            options.setUserFound(true);
            for (UserSecurityQuestion q : user.getUserSecurityQuestions()) {
                NameValuePair nvp = new NameValuePair(q.getSecurityQuestions().getQuestion(), q.getSecurityQuestions().getQuestion());
                options.getSecurityQuestions().add(nvp);
            }
        } else {
            options.setUserFound(false);
        }
        return options;
    }

    @Override
    public SaveUpdateUserResult saveUpdateUser(ManageUsersDetailModel model) {
        SaveUpdateUserResult result = new SaveUpdateUserResult();
        User user;
        if (model.isExistingUser()) {
            user = getById(model.getKey());
        } else {
            user = new User();
        }

        boolean reactivatingUser = false;
        if (model.isActive() && (user.getStatus() != null) && !user.getStatus().getStatusId().equals(Status.STATUS_ACTIVE)) {
            reactivatingUser = true;
        }
        if (checkEmailNotUnique(model)) {
            result.setSuccess(false);
            result.setFailureReason(SaveUpdateUserResult.USER_EMAIL_NOT_UNIQUE);
        } else {
            saveValidatedUserFromModel(model, result, user, reactivatingUser);
        }
        return result;
    }

    private void saveValidatedUserFromModel(ManageUsersDetailModel model, SaveUpdateUserResult result, User user, boolean reactivatingUser) {
        setModelFieldsOnUser(model, user);
        if (model.isExistingUser()) {
            if (reactivatingUser) {
                activate(user.getId());
            }
            saveExisting(user);
        } else {
            saveNew(user);
        }
        result.setSuccess(true);
    }

    private boolean checkEmailNotUnique(ManageUsersDetailModel model) {
        User existing = userDAO.findByEmail(model.getEmailAddress());
        return existing != null && !(existing.getId().equals(model.getKey()));
    }


    @Override
    public List<String> getFooterURLs() {
        List<String> footerUrls = new ArrayList<>();
        footerUrls.add(accessibilityUrl);
        footerUrls.add(privacyPolicyUseUrl);
        footerUrls.add(termsOfUseUrl);
        footerUrls.add(userGuideUrl);
        return footerUrls;
    }

    private void setModelFieldsOnUser(ManageUsersDetailModel model, User user) {
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setMiddleInit(model.getMiddleInitial());
        user.setTitle(model.getTitle());
        user.setEmailAddress(model.getEmailAddress());
        user.setHarpId(model.getHarpId());
        user.setPhoneNumber(model.getPhoneNumber());
        user.setStatus(getStatusObject(model.isActive()));
        user.setSecurityRole(getRole(model.getRole()));
        user.setFhirFlag(model.isFhirAccessible());

        if (model.isActive()) {
            Organization organization = organizationDAO.find(Long.parseLong(model.getOrganizationId()));
            user.setOrganization(organization);
        } else if (!model.isActive()) {
            // 1 org id in db has blank organization name and oid.
            Organization organizationRevoked = organizationDAO.find(Long.parseLong("1"));
            user.setOrganization(organizationRevoked);
        }

        // if the user was activated, set term date to null and set the activation date
        if (model.isBeingActivated()) {
            user.setTerminationDate(null);
            user.setSignInDate(null);
            user.setSignOutDate(null);
            user.setActivationDate(new Date());
        } else if (model.isBeingRevoked()) {
            // if the user is being revoked/terminated, update the termination date
            user.setTerminationDate(new Date());
        }
    }

    /**
     * Gets the status object.
     *
     * @param isActive the is active
     * @return the status object
     */
    private Status getStatusObject(boolean isActive) {
        String id = isActive ? "1" : "2";
        return statusDAO.find(id);
    }

    /**
     * Gets the role.
     *
     * @param value the value
     * @return the role
     */
    private SecurityRole getRole(String value) {
        for (SecurityRole role : securityRoleDAO.find()) {
            if (role.getId().equals(value)) {
                return role;
            }
        }
        return null;
    }

    // US212
    @Override
    public void setUserSignInDate(String userid) {
        userDAO.setUserSignInDate(userid);
    }

    // US212
    @Override
    public void setUserSignOutDate(String userid) {
        userDAO.setUserSignOutDate(userid);
    }

    /*
     *
     * Algorithm to Generated Unique login ID for new User
     * First Name - 2 characters
     * Last Name - 6 characters
     * Four Digit - Random number
     *
     * */

    /**
     * Generate unique login id.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @return the string
     */
    private String generateUniqueLoginId(String firstName, String lastName) {
        StringBuilder generatedId = new StringBuilder();
        int firstNameLastIndex = 2;
        int lastNameLastIndex = 6;
        //Check if First Name length us less than 2 then set endIndex to length
        if (firstName.length() < 2) {
            firstNameLastIndex = lastName.length();
        }
        //Check if last Name length us less than 6 then set endIndex to length
        if (lastName.length() < 6) {
            lastNameLastIndex = lastName.length();
        }

        generatedId = generatedId.append(firstName.substring(0, firstNameLastIndex)).append(lastName.substring(0, lastNameLastIndex)).append((ID.nextInt(9000) + 1000));
        return generatedId.toString();
    }

    public void setAccessibilityUrl(String accessibilityUrl) {
        this.accessibilityUrl = accessibilityUrl;
    }

    public String getAccessibilityUrl() {
        return accessibilityUrl;
    }

    public void setTermsOfUseUrl(String termsOfUseUrl) {
        this.termsOfUseUrl = termsOfUseUrl;
    }

    public String getTermsOfUseUrl() {
        return termsOfUseUrl;
    }

    public void setPrivacyPolicyUseUrl(String privacyPolicyUseUrl) {
        this.privacyPolicyUseUrl = privacyPolicyUseUrl;
    }

    public String getPrivacyPolicyUseUrl() {
        return privacyPolicyUseUrl;
    }

    public void setUserGuideUrl(String userGuideUrl) {
        this.userGuideUrl = userGuideUrl;
    }

    public String getUserGuideUrl() {
        return userGuideUrl;
    }

    @Override
    public String updateOnSignOut(String userId, String email, String activityType) {
        Date signoutDate = new Date();
        TransactionAuditLog auditLog = new TransactionAuditLog();
        auditLog.setActivityType(activityType);
        auditLog.setUserId(userId);
        auditLog.setAdditionalInfo("[" + email + "]");
        auditLog.setTime(signoutDate);

        User user = userDAO.find(userId);
        user.setSignOutDate(signoutDate);
        user.setSessionId(null);
        try {
            userDAO.save(user);
            transactionAuditLogDAO.save(auditLog);
            logger.debug("SignOut Successful" + signoutDate.toString());
            return "SUCCESS";
        } catch (Exception e) {
            logger.debug("SignOut Unsuccessful " + "(" + signoutDate.toString() + ")" + e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public String getSecurityQuestion(String userLoginID) {
        return userDAO.getRandomSecurityQuestion(userLoginID);
    }

    @Override
    public List<User> getAllNonAdminActiveUsers() {
        return userDAO.getAllNonAdminActiveUsers();
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Notify user of account locked.
     *
     * @param user the user
     */
    private void notifyUserOfAccountLocked(User user) {
        SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
        msg.setSubject(ServerConstants.ACCOUNT_LOCKED_SUBJECT + ServerConstants.getEnvName());
        String text = ServerConstants.ACCOUNT_LOCKED_MESSAGE;
        msg.setTo(user.getEmailAddress());
        msg.setText(text);
        try {
            mailSender.send(msg);
        } catch (MailException exc) {
            logger.error(exc);
        }
    }

    @Override
    public boolean isLockedUser(String loginId) {
        User user = userDAO.findByLoginId(loginId);
        if ((user != null) && ((user.getLockedOutDate() != null) || (user.getPassword().getPasswordlockCounter() >= 3))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isHarpUserLockedRevoked(String harpId) {
        return userDAO.getAllUserDetailsByHarpId(harpId).stream().allMatch(MatUserDetails::isLockedOrRevoked);
    }

    @Override
    public List<User> searchForNonTerminatedUsers() {
        return userDAO.searchForNonTerminatedUser();
    }


    /**
     * temporary and initial sign in password should not be stored in password History
     * isValidPwd boolean is set for special case where current valid password becomes temporary
     * password when it exceeds 60 days limit and it Should be added to password history.
     * *
     *
     * @param user       the user
     * @param isValidPwd the is valid pwd
     */
    @Override
    public void addByUpdateUserPasswordHistory(User user, boolean isValidPwd) {
        if (isValidPwd || !(user.getPassword().isInitial()
                || user.getPassword().isTemporaryPassword())) {
            UserPasswordHistory passwordHistory = new UserPasswordHistory();
            passwordHistory.setUser(user);
            passwordHistory.setPassword(user.getPassword().getPassword());
            passwordHistory.setSalt(user.getPassword().getSalt());
            passwordHistory.setCreatedDate(user.getPassword().getCreatedDate());

            List<UserPasswordHistory> pwdHistoryList = userPasswordHistoryDAO.getPasswordHistory(user.getId());
            if (pwdHistoryList.size() < PASSWORD_HISTORY_SIZE) {
                userPasswordHistoryDAO.save(passwordHistory);
            } else {
                userPasswordHistoryDAO.addByUpdateUserPasswordHistory(user);
            }
        }

    }

}
