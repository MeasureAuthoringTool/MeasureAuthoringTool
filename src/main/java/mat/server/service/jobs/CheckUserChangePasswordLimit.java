package mat.server.service.jobs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import mat.dao.EmailAuditLogDAO;
import mat.dao.UserDAO;
import mat.model.EmailAuditLog;
import mat.model.User;
import mat.server.service.UserService;
import mat.server.util.ServerConstants;
import mat.shared.ConstantMessages;

/**
 * The Class CheckUserPasswordLimit.
 */
@Service
public class CheckUserChangePasswordLimit {

    private static final Log logger = LogFactory.getLog(CheckUserChangePasswordLimit.class);

    private static final String WARNING_EMAIL_FLAG = "WARNING";
    private static final String EXPIRY_EMAIL_FLAG = "EXPIRED";

    @Autowired
    private UserService userService;

    @Autowired
    private Configuration freemarkerConfiguration;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EmailAuditLogDAO emailAuditLogDAO;

    @Autowired
    private MailSender mailSender;

    @Qualifier("userLastLoginTemplateMessage")
    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Value("${mat.password.warning.dayLimit}")
    private int passwordwarningDayLimit;

    @Value("${mat.password.expiry.dayLimit}")
    private int passwordexpiryDayLimit;

    @Value("${mat.password.warning.email.template}")
    private String warningMailTemplate;

    @Value("${mat.password.warning.email.subject}")
    private String warningMailSubject;

    @Value("${mat.password.expiry.email.template}")
    private String expiryMailTemplate;

    @Value("${mat.password.expiry.email.subject}")
    private String expiryMailSubject;
    @Value("${mat.support.emailAddress}")
    private String supportEmailAddress;


    /**
     * Method to Send
     * 1.Warning Email for Warning Day Limit 45 days.
     * 2.Password change screen for day limit >60 days
     *
     * @return void
     */
    @Scheduled(cron = "${mat.checkUserPasswordLimitDays.cron}")
    public void checkUserPasswordLimitDays() {

        logger.info(" :: CheckUserPasswordLimitDays Method START :: ");

        checkUserLoginPasswordDays(passwordwarningDayLimit, WARNING_EMAIL_FLAG);
        checkUserLoginPasswordDays(passwordexpiryDayLimit, EXPIRY_EMAIL_FLAG);

        logger.info(" :: CheckUserPasswordLimitDays Method END :: ");
    }

    /**
     * Check user login password days.
     *
     * @param noOfDaysPasswordLimit the no of days limit
     * @param emailType             the email type
     */
    private void checkUserLoginPasswordDays(final long noOfDaysPasswordLimit, final String emailType) {

        logger.info(" :: checkUserLoginDays Method START :: for Sending " + emailType + " Type Email");
        // Get all the Users
        final List<User> users = userDAO.find();
        final List<User> emailUsers = checkUsersLastPassword(noOfDaysPasswordLimit, users);
        final Map<String, Object> model = new HashMap<>();
        final Map<String, String> content = new HashMap<>();
        final String envirUrl = ServerConstants.getEnvURL();

        for (User user : emailUsers) {

            // Send 45 days password limit email for all the users in the list.
            logger.info("Sending email to " + user.getFirstName());
            simpleMailMessage.setTo(user.getEmailAddress());

            // Creation of the model map can be its own method.
            content.put("User", "Measure Authoring Tool User");

            /*
             * If the user is not a normal user then set the user role in the email
             */
            String userRole = "";
            if (!(user.getSecurityRole().getId().trim().equals("3"))) {
                userRole = "(" + user.getSecurityRole().getDescription() + ")";
            }
            content.put("rolename", userRole);

            content.put(ConstantMessages.LOGINID, user.getLoginId());
            content.put(ConstantMessages.URL, envirUrl);

            //5 days Expiry Date
            if (passwordexpiryDayLimit == noOfDaysPasswordLimit) {
                final String expiryDate = getFormattedExpiryDate(new Date(), 5 - 1);
                content.put("passwordExpiryDate", expiryDate);
            }

            model.put("content", content);
            model.put(ConstantMessages.SUPPORT_EMAIL, supportEmailAddress);
            String text = null;
            try {
                if (WARNING_EMAIL_FLAG.equals(emailType)) {
                    text = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(warningMailTemplate), model);
                    simpleMailMessage.setText(text);
                    simpleMailMessage.setSubject(warningMailSubject + ServerConstants.getEnvName());
                } else if (EXPIRY_EMAIL_FLAG.equals(emailType)) {
                    text = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(expiryMailTemplate), model);
                    simpleMailMessage.setText(text);
                    simpleMailMessage.setSubject(expiryMailSubject + ServerConstants.getEnvName());
                }
                mailSender.send(simpleMailMessage);
                EmailAuditLog emailAudit = new EmailAuditLog();
                emailAudit.setActivityType("Password " + emailType + " email sent.");
                emailAudit.setTimestamp(new Date());
                emailAudit.setLoginId(user.getLoginId());
                emailAuditLogDAO.save(emailAudit);
                content.clear();
                model.clear();
                logger.info("Email Sent to " + user.getFirstName());
            } catch (IOException | TemplateException e) {
                logger.error(e);
            }
        }

        logger.info(" :: CheckUserLoginPasswordDays Method END :: ");

    }

    /**
     * Check users last password date.
     *
     * @param passwordDayLimit the passwor day limit
     * @param users            the users
     * @return the list
     */
    private List<User> checkUsersLastPassword(final long passwordDayLimit, final List<User> users) {

        logger.info(" :: checkUsersLastPassword Method Start :: ");

        final List<User> returnUserList = new ArrayList<User>();
        final Date passwordDaysAgo = getPasswordNumberOfDaysAgo((int) passwordDayLimit);
        logger.info(passwordDayLimit + "passwordDaysAgo:" + passwordDaysAgo);

        for (User user : users) {
            Date lastPasswordCreatedDate = user.getPassword().getCreatedDate();

            if (lastPasswordCreatedDate == null || !checkValidUser(user)) {
                continue;
            }

            lastPasswordCreatedDate = DateUtils.truncate(lastPasswordCreatedDate, Calendar.DATE);
            logger.info("User:" + user.getFirstName() + "  :::: last Created Password Date :::::   " + lastPasswordCreatedDate);
            //for User password equals 45 days
            if (passwordwarningDayLimit == passwordDayLimit) {

                if (lastPasswordCreatedDate.equals(passwordDaysAgo)) {
                    logger.info("User:" + user.getEmailAddress() + " who's last password was " + passwordDayLimit + " days ago.");
                    returnUserList.add(user);
                } else {
                    logger.info("User:" + user.getEmailAddress() + " who's last password was not " + passwordDayLimit + " days ago.");
                }
            }

            // for User Password Greater than 60days
            else if (passwordexpiryDayLimit == passwordDayLimit) {

                if (lastPasswordCreatedDate.before((passwordDaysAgo))
                        || lastPasswordCreatedDate.equals(passwordDaysAgo)) {
                    logger.info("User:" + user.getEmailAddress() + " who's last password was more than " + passwordDayLimit + " days ago.");
                    returnUserList.add(user);
                    // to maintain Password history
                    getUserService().addByUpdateUserPasswordHistory(user, true);
                    user.getPassword().setTemporaryPassword(true);
                    user.getPassword().setCreatedDate(DateUtils.truncate(new Date(), Calendar.DATE));
                    getUserService().saveExisting(user);

                    /*returnUserList.add(user);
                     * MatContext.get().getEventBus().fireEvent(new TemporaryPasswordLoginEvent());*/
                } else {
                    logger.info("User:" + user.getEmailAddress() + " who's last password was not more than " + passwordDayLimit + " days ago.");
                }
            }

        }
        logger.info(" :: checkUsersLastPassword Method END :: ");

        return returnUserList;
    }


    /**
     * Gets the password number of days ago.
     *
     * @param noOfDaysPasswordLimit the no of day limit
     * @return the password number of days ago
     */
    private Date getPasswordNumberOfDaysAgo(final int noOfDaysPasswordLimit) {

        logger.info(" :: getPasswordNumberOfDaysAgo Method START :: ");

        Date numberOfDaysAgo;
        numberOfDaysAgo = DateUtils.truncate(new Date(), Calendar.DATE);
        numberOfDaysAgo = DateUtils.addDays(numberOfDaysAgo, noOfDaysPasswordLimit);

        logger.info(" :: getPasswordNumberOfDaysAgo Method END :: " + numberOfDaysAgo);
        return numberOfDaysAgo;
    }


    /**
     * Check valid user.
     *
     * @param user the user
     * @return true, if successful
     */
    private boolean checkValidUser(final User user) {
        logger.info(" :: checkValidUser Method START :: ");

        Boolean isValidUser = true;

        // final Date terminationDate = user.getTerminationDate();
        final Date signInDate = user.getSignInDate();
        logger.info("signInDate :: " + signInDate);
        if (signInDate == null || user.getStatus().getStatusId().equals("2")) {
            isValidUser = false;
        }

        logger.info(user.getFirstName() + " :: checkValidUser Method END :: isValidUser ::: " + isValidUser);

        return isValidUser;
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
        calendar.add(Calendar.DATE, willExpireIn);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEEE, MMMMM d, yyyy");
        String returnDateString = simpleDateFormat.format(calendar.getTime());
        return returnDateString;
    }

    public String getExpiryMailTemplate() {
        return expiryMailTemplate;
    }

    public void setExpiryMailTemplate(String expiryMailTemplate) {
        this.expiryMailTemplate = expiryMailTemplate;
    }

    public String getExpiryMailSubject() {
        return expiryMailSubject;
    }

    public void setExpiryMailSubject(String expiryMailSubject) {
        this.expiryMailSubject = expiryMailSubject;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public EmailAuditLogDAO getEmailAuditLogDAO() {
        return emailAuditLogDAO;
    }

    public void setEmailAuditLogDAO(EmailAuditLogDAO emailAuditLogDAO) {
        this.emailAuditLogDAO = emailAuditLogDAO;
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public SimpleMailMessage getSimpleMailMessage() {
        return simpleMailMessage;
    }

    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }

    public int getPasswordwarningDayLimit() {
        return passwordwarningDayLimit;
    }

    public void setPasswordwarningDayLimit(int passwordwarningDayLimit) {
        this.passwordwarningDayLimit = passwordwarningDayLimit;
    }

    public int getPasswordexpiryDayLimit() {
        return passwordexpiryDayLimit;
    }

    public void setPasswordexpiryDayLimit(int passwordexpiryDayLimit) {
        this.passwordexpiryDayLimit = passwordexpiryDayLimit;
    }

    public String getWarningMailTemplate() {
        return warningMailTemplate;
    }

    public void setWarningMailTemplate(String warningMailTemplate) {
        this.warningMailTemplate = warningMailTemplate;
    }

    public String getWarningMailSubject() {
        return warningMailSubject;
    }

    public void setWarningMailSubject(String warningMailSubject) {
        this.warningMailSubject = warningMailSubject;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public static String getWarningEmailFlag() {
        return WARNING_EMAIL_FLAG;
    }

    public static String getExpiryEmailFlag() {
        return EXPIRY_EMAIL_FLAG;
    }

}
