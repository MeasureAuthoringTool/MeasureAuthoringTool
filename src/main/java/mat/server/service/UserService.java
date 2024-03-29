package mat.server.service;

import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.service.SaveUpdateUserResult;
import mat.client.login.service.SecurityQuestionOptions;
import mat.model.Organization;
import mat.model.User;
import mat.server.model.MatUserDetails;
import mat.shared.ForgottenLoginIDResult;
import mat.shared.ForgottenPasswordResult;

import java.util.HashMap;
import java.util.List;

/**
 * The Interface UserService.
 */
public interface UserService {

    /**
     * Search for users by name.
     *
     * @param orgId the org id
     * @return the list
     */
    List<User> searchForUsersByName(String orgId);

    /**
     * Gets the by id.
     *
     * @param id the id
     * @return the by id
     */
    User getById(String id);

    /**
     * Save new.
     *
     * @param user the user
     * @throws UserIDNotUnique the user id not unique
     */
    void saveNew(User user);

    /**
     * Save existing.
     *
     * @param user the user
     * @throws UserIDNotUnique
     */
    void saveExisting(User user);

    /**
     * Save update user.
     *
     * @param model the model
     * @return the save update user result
     */
    SaveUpdateUserResult saveUpdateUser(ManageUsersDetailModel model);

    /**
     * Request forgotten password.
     *
     * @param email              the email
     * @param securityQuestion   the security question
     * @param securityAnswer     the security answer
     * @param invalidUserCounter the invalid user counter
     * @return the forgotten password result
     */
    ForgottenPasswordResult requestForgottenPassword(String email,
                                                     String securityQuestion, String securityAnswer, int invalidUserCounter);

    /**
     * Request activate.
     *
     * @param userid the userid
     */
    void activate(String userid);

    /**
     * Sets the user password.
     *
     * @param user              the user
     * @param clearTextPassword the clear text password
     * @param isTemporary       the is temporary
     */
    void setUserPassword(User user, String clearTextPassword, boolean isTemporary);

    /**
     * Gets the password hash.
     *
     * @param userId   the user id
     * @param password the password
     * @return the password hash
     */
    String getPasswordHash(String userId, String password);

    /**
     * Checks if is admin for user.
     *
     * @param admin the admin
     * @param user  the user
     * @return true, if is admin for user
     */
    boolean isAdminForUser(User admin, User user);

    /**
     * Generate random password.
     *
     * @return the string
     */
    String generateRandomPassword();

    /**
     * Delete user.
     *
     * @param userid the userid
     */
    void deleteUser(String userid);

    /**
     * Gets the security question options.
     *
     * @param email the email
     * @return the security question options
     */
    SecurityQuestionOptions getSecurityQuestionOptions(String email);

    //US212

    /**
     * Sets the user sign in date.
     *
     * @param userid the new user sign in date
     */
    void setUserSignInDate(String userid);

    /**
     * Sets the user sign out date.
     *
     * @param userid the new user sign out date
     */
    void setUserSignOutDate(String userid);

    /**
     * Gets the footer ur ls.
     *
     * @return the footer ur ls
     */
    List<String> getFooterURLs();

    /**
     * Search non admin users.
     *
     * @param string the string
     * @param i      the i
     * @param j      the j
     * @return the list
     */
    List<User> searchNonAdminUsers(String string, int i, int j);

    /**
     * Find by email id.
     *
     * @param emailId the email id
     * @return the user
     */
    User findByEmailID(String emailId);

    /**
     * Update on sign out.
     *
     * @param userId       the user id
     * @param email        the email
     * @param activityType the activity type
     * @return the string
     */
    String updateOnSignOut(String userId, String email, String activityType);

    /**
     * Request forgotten login id.
     *
     * @param email the email
     * @return the forgotten login id result
     */
    ForgottenLoginIDResult requestForgottenLoginID(String email);

    /**
     * Gets the security question options for email.
     *
     * @param email the email
     * @return the security question options for email
     */
    SecurityQuestionOptions getSecurityQuestionOptionsForEmail(String email);

    /**
     * Gets the security question.
     *
     * @param userid the userid
     * @return the security question
     */
    String getSecurityQuestion(String userid);

    /**
     * Gets the all non admin active users.
     *
     * @return the all non admin active users
     */
    List<User> getAllNonAdminActiveUsers();

    /**
     * Checks if is locked user.
     *
     * @param loginId the login id
     * @return true, if is locked user
     */
    boolean isLockedUser(String loginId);

    /**
     * Checks if the user is locked out of the MAT
     *
     * @param harpId User's HARP ID.
     * @return true, if the user was locked (likely due to inactivity).
     */
    boolean isHarpUserLockedRevoked(String harpId);

    /**
     * Search for non terminated users.
     *
     * @return the list
     */
    List<User> searchForNonTerminatedUsers();

    /**
     * Search for used organizations.
     *
     * @return the hash map
     */
    HashMap<String, Organization> searchForUsedOrganizations();

    /**
     * Adds the by update user password history.
     *
     * @param user       the user
     * @param isValidPwd the is valid pwd
     */
    void addByUpdateUserPasswordHistory(User user, boolean isValidPwd);

    List<User> getAllUsers();

    List<User> searchForUsersWithActiveBonnie(String searchTerm);

    List<MatUserDetails> getAllActiveUserDetailsByHarpId(String harpId);

}
