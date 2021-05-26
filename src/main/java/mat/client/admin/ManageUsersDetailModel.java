package mat.client.admin;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.BaseModel;

/**
 * The Class ManageUsersDetailModel.
 */
public class ManageUsersDetailModel implements IsSerializable, BaseModel {

    /**
     * The user id.
     */
    private String userID;

    /**
     * The first name.
     */
    private String firstName;

    /**
     * The last name.
     */
    private String lastName;

    /**
     * The middle initial.
     */
    private String middleInitial;

    /**
     * The title.
     */
    private String title;

    /**
     * The email address.
     */
    private String emailAddress;

    private String harpId;

    /**
     * The login id.
     */
    private String loginId;

    /**
     * The phone number.
     */
    private String phoneNumber;

    /**
     * The organization.
     */
    private String organization;

    /**
     * The organization id.
     */
    private String organizationId;

    /**
     * The oid.
     */
    private String oid;

    /**
     * The role.
     */
    private String role;

    /**
     * The active.
     */
    private boolean active = true;

    /**
     * The is existing user.
     */
    private boolean isExistingUser;

    /**
     * The is locked.
     */
    private boolean isLocked;

    /**
     * The current user can unlock.
     */
    private boolean currentUserCanUnlock;

    /**
     * The current user can change account status.
     */
    private boolean currentUserCanChangeAccountStatus;

    /**
     * The password expiration msg.
     */
    private String passwordExpirationMsg;

    private String lastSuccessFullLoginDateTimeMessage;

    /**
     * The revoke date.
     */
    private String revokeDate;

    /**
     * The additional info.
     */
    private String additionalInfo;

    /**
     * Is the user being activated?
     */
    private boolean beingActivated = false;

    /**
     * Is the user being revoked?
     */
    private boolean beingRevoked = false;

    private boolean isFhirAccessible;

    public boolean isBeingActivated() {
        return beingActivated;
    }

    public void setBeingActivated(boolean beingActivated) {
        this.beingActivated = beingActivated;
    }

    public boolean isBeingRevoked() {
        return beingRevoked;
    }

    public void setBeingRevoked(boolean beingRevoked) {
        this.beingRevoked = beingRevoked;
    }

    /**
     * Gets the revoke date.
     *
     * @return the revoke date
     */
    public String getRevokeDate() {
        return revokeDate;
    }

    /**
     * Sets the revoke date.
     *
     * @param revokeDate the new revoke date
     */
    public void setRevokeDate(String revokeDate) {
        this.revokeDate = revokeDate;

    }

    /**
     * Checks if is current user can unlock.
     *
     * @return true, if is current user can unlock
     */
    public boolean isCurrentUserCanUnlock() {
        return currentUserCanUnlock;
    }

    /**
     * Sets the current user can unlock.
     *
     * @param currentUserCanUnlock the new current user can unlock
     */
    public void setCurrentUserCanUnlock(boolean currentUserCanUnlock) {
        this.currentUserCanUnlock = currentUserCanUnlock;
    }

    /**
     * Checks if is current user can change account status.
     *
     * @return true, if is current user can change account status
     */
    public boolean isCurrentUserCanChangeAccountStatus() {
        return currentUserCanChangeAccountStatus;
    }

    /**
     * Sets the current user can change account status.
     *
     * @param currentUserCanChangeRole the new current user can change account status
     */
    public void setCurrentUserCanChangeAccountStatus(boolean currentUserCanChangeRole) {
        currentUserCanChangeAccountStatus = currentUserCanChangeRole;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the user id.
     *
     * @param userID the new user id
     */
    public void setUserID(String userID) {
        this.userID = doTrim(userID);
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = doTrim(firstName);
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = doTrim(lastName);
    }

    /**
     * Gets the middle initial.
     *
     * @return the middle initial
     */
    public String getMiddleInitial() {
        return middleInitial;
    }

    /**
     * Sets the middle initial.
     *
     * @param middleInitial the new middle initial
     */
    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = doTrim(middleInitial);
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = doTrim(title);
    }

    /**
     * Gets the email address.
     *
     * @return the email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address.
     *
     * @param emailAddress the new email address
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = doTrim(emailAddress);
    }

    public String getHarpId() {
        return harpId;
    }

    public void setHarpId(String harpId) {
        this.harpId = harpId;
    }

    /**
     * Gets the phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number.
     *
     * @param phoneNumber the new phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = doTrim(phoneNumber);
    }

    /**
     * Gets the organization.
     *
     * @return the organization
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Sets the organization.
     *
     * @param organization the new organization
     */
    public void setOrganization(String organization) {
        this.organization = doTrim(organization);
    }

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active.
     *
     * @param active the new active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return userID;
    }

    /**
     * Checks if is existing user.
     *
     * @return true, if is existing user
     */
    public boolean isExistingUser() {
        return isExistingUser;
    }

    /**
     * Sets the existing user.
     *
     * @param isExistingUser the new existing user
     */
    public void setExistingUser(boolean isExistingUser) {
        this.isExistingUser = isExistingUser;
    }

    /**
     * Checks if is locked.
     *
     * @return true, if is locked
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Sets the locked.
     *
     * @param isLocked the new locked
     */
    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * Gets the oid.
     *
     * @return the oid
     */
    public String getOid() {
        return oid;
    }

    /**
     * Sets the oid.
     *
     * @param oid the new oid
     */
    public void setOid(String oid) {
        this.oid = doTrim(oid);
    }
	/*public String getRootOid() {
		return rootOid;
	}
	public void setRootOid(String rootOid) {
		this.rootOid = doTrim(rootOid);
	}*/

    /**
     * Gets the role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role.
     *
     * @param role the new role
     */
    public void setRole(String role) {
        this.role = doTrim(role);
    }

    /**
     * Do trim.
     *
     * @param str the str
     * @return the string
     */
    private String doTrim(String str) {
        if (str == null) {
            return str;
        } else {
            return str.trim();
        }
    }

    /**
     * Sets the login id.
     *
     * @param loginId the loginId to set
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * Gets the login id.
     *
     * @return the loginId
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * Gets the organization id.
     *
     * @return the organizationId
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * Sets the organization id.
     *
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * Gets the password expiration msg.
     *
     * @return the passwordExpirationDate
     */
    public String getPasswordExpirationMsg() {
        return passwordExpirationMsg;
    }

    /**
     * Sets the password expiration msg.
     *
     * @param passwordExpirationDate the passwordExpirationDate to set
     */
    public void setPasswordExpirationMsg(String passwordExpirationDate) {
        passwordExpirationMsg = passwordExpirationDate;
    }


    /**
     * Gets the additional info.
     *
     * @return the additional info
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * Sets the additional info.
     *
     * @param additionalInfo the new additional info
     */
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }


    /* (non-Javadoc)
     * @see mat.model.BaseModel#scrubForMarkUp()
     */
    @Override
    public void scrubForMarkUp() {
        String markupRegExp = "<[^>]+>";

        String noMarkupText = this.getFirstName().trim().replaceAll(markupRegExp, "");
        if (this.getFirstName().trim().length() > noMarkupText.length()) {
            this.setFirstName(noMarkupText);
        }
        noMarkupText = this.getLastName().trim().replaceAll(markupRegExp, "");
        if (this.getLastName().trim().length() > noMarkupText.length()) {
            this.setLastName(noMarkupText);
        }
        noMarkupText = this.getMiddleInitial().trim().replaceAll(markupRegExp, "");
        if (this.getMiddleInitial().trim().length() > noMarkupText.length()) {
            this.setMiddleInitial(noMarkupText);
        }
        noMarkupText = this.getTitle().trim().replaceAll(markupRegExp, "");
        if (this.getTitle().trim().length() > noMarkupText.length()) {
            this.setTitle(noMarkupText);
        }
        noMarkupText = this.getEmailAddress().trim().replaceAll(markupRegExp, "");
        if (this.getEmailAddress().trim().length() > noMarkupText.length()) {
            this.setEmailAddress(noMarkupText);
        }
        noMarkupText = this.getHarpId().trim().replaceAll(markupRegExp, "");
        if (this.getHarpId().trim().length() > noMarkupText.length()) {
            this.setHarpId(noMarkupText);
        }
        noMarkupText = this.getOid().trim().replaceAll(markupRegExp, "");
        if (this.getOid().trim().length() > noMarkupText.length()) {
            this.setOid(noMarkupText);
        }
        noMarkupText = this.getPhoneNumber().trim().replaceAll(markupRegExp, "");
        if (this.getPhoneNumber().trim().length() > noMarkupText.length()) {
            this.setPhoneNumber(noMarkupText);
        }
        noMarkupText = this.getAdditionalInfo().trim().replaceAll(markupRegExp, "");
        if (this.getAdditionalInfo().trim().length() > noMarkupText.length()) {
            this.setAdditionalInfo(noMarkupText);
        }
    }

    public String getLastSuccessFullLoginDateTimeMessage() {
        return lastSuccessFullLoginDateTimeMessage;
    }

    public void setLastSuccessFullLoginDateTimeMessage(String lastSuccessFullLoginDateTimeMessage) {
        this.lastSuccessFullLoginDateTimeMessage = lastSuccessFullLoginDateTimeMessage;
    }

    public boolean isFhirAccessible() {
        return isFhirAccessible;
    }

    public void setFhirAccessible(boolean fhirAccessible) {
        isFhirAccessible = fhirAccessible;
    }
}
