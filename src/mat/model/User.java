package mat.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mat.model.clause.MeasureShare;


/**
 * The Class User.
 */
public class User  {
	
	/** The id. */
	private String id;
	
	/** The first name. */
	private String firstName;
	
	/** The middle init. */
	private String middleInit;
	
	/** The last name. */
	private String lastName;
	
	/** The email address. */
	private String emailAddress;
	
	/** The phone number. */
	private String phoneNumber;
	
	/** The title. */
	private String title;
	
	/** The activation date. */
	private Date activationDate;
	
	/** The termination date. */
	private Date terminationDate;
	
	/** The sign in date. */
	private Timestamp signInDate;
	
	/** The sign out date. */
	private Timestamp signOutDate;
	
	/** The locked out date. */
	private Timestamp lockedOutDate;
	
	/** The organization. */
	private Organization organization;
	
	/** The organization name. */
	//private String organizationName;
	
	/** The org oid. */
	//private String orgOID;
	//private String rootOID;
	/** The password. */
	private UserPassword password;
	
	/** The audit log. */
	private AuditLog auditLog;
	
	/** The status. */
	private Status status;
	
	/** The security role. */
	private SecurityRole securityRole;	
	
	/** The security questions. */
	private List<UserSecurityQuestion> securityQuestions = new ArrayList<UserSecurityQuestion>();
	
	/** The measure shares. */
	private Set<MeasureShare> measureShares = new HashSet<MeasureShare>();
	
	/** The owned measure shares. */
	private Set<MeasureShare> ownedMeasureShares = new HashSet<MeasureShare>();
	
	/** The login id. */
	private String loginId;
	
	private Set<UserPasswordHistory> passwordHistory = new  HashSet<UserPasswordHistory>();

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @param firstName
	 *            the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Gets the middle init.
	 * 
	 * @return the middle init
	 */
	public String getMiddleInit() {
		return middleInit;
	}
	
	/**
	 * Sets the middle init.
	 * 
	 * @param middleInit
	 *            the new middle init
	 */
	public void setMiddleInit(String middleInit) {
		this.middleInit = middleInit;
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
	 * @param lastName
	 *            the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @param emailAddress
	 *            the new email address
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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
	 * @param phoneNumber
	 *            the new phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Gets the activation date.
	 * 
	 * @return the activation date
	 */
	public Date getActivationDate() {
		return activationDate;
	}
	
	/**
	 * Sets the activation date.
	 * 
	 * @param activationDate
	 *            the new activation date
	 */
	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}
	
	/**
	 * Gets the termination date.
	 * 
	 * @return the termination date
	 */
	public Date getTerminationDate() {
		return terminationDate;
	}
	
	/**
	 * Sets the termination date.
	 * 
	 * @param terminationDate
	 *            the new termination date
	 */
	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}
	
	/**
	 * Gets the organization name.
	 * 
	 * @return the organization name
	 */
	public String getOrganizationName() {
		String orgName = "";
		if(this.organization != null){
			orgName = this.organization.getOrganizationName();
		}
		return orgName;
	}
	
	/** Sets the organization name.
	 * 
	 * @param organizationName
	 *            the new organization name
	 */
	/*public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}*/
	
	/**
	 * Gets the org oid.
	 * 
	 * @return the org oid
	 */
	public String getOrgOID() {
		String orgOID = "";
		if(this.organization != null){
			orgOID = this.organization.getOrganizationOID();
		}
		return orgOID;
	}
	
	/** Sets the org oid.
	 * 
	 * @return the organization id */
	/*public void setOrgOID(String orgOID) {
		this.orgOID = orgOID;
	}*/
	
	
	public String getOrganizationId() {
		String orgId = "";
		if(this.organization != null){
			orgId = String.valueOf(this.organization.getId());
		}
		return orgId;
	}
	
	/**
	 * Removed Root OID from User. But for legacy code purpose, getRootOID() method just returns blank string.
	 * @return Blank String
	 */
	public String getRootOID() {
		return "";
	}
	
	/*public void setRootOID(String rootOID) {
		this.rootOID = rootOID;
	}*/
	
	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public UserPassword getPassword() {
		return password;
	}
	
	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(UserPassword password) {
		if(password != null && password.getUser() == null) {
			password.setUser(this);
		}
		this.password = password;
	}
	
	/**
	 * Gets the audit log.
	 * 
	 * @return the audit log
	 */
	public AuditLog getAuditLog() {
		if(auditLog == null) {
			auditLog = new AuditLog();
		}
		return auditLog;
	}
	
	/**
	 * Sets the audit log.
	 * 
	 * @param auditLog
	 *            the new audit log
	 */
	public void setAuditLog(AuditLog auditLog) {
		this.auditLog = auditLog;
	}
	
	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/**
	 * Gets the security role.
	 * 
	 * @return the security role
	 */
	public SecurityRole getSecurityRole() {
		return securityRole;
	}
	
	/**
	 * Sets the security role.
	 * 
	 * @param securityRole
	 *            the new security role
	 */
	public void setSecurityRole(SecurityRole securityRole) {
		this.securityRole = securityRole;
	}
	
	/**
	 * Gets the security questions.
	 * 
	 * @return the security questions
	 */
	public List<UserSecurityQuestion> getSecurityQuestions() {
		return securityQuestions;
	}
	
	/**
	 * Sets the security questions.
	 * 
	 * @param securityQuestions
	 *            the new security questions
	 */
	public void setSecurityQuestions(List<UserSecurityQuestion> securityQuestions) {
		this.securityQuestions = securityQuestions;
	}			
	
	/**
	 * Gets the sign in date.
	 * 
	 * @return the sign in date
	 */
	public Date getSignInDate() {
		return signInDate;
	}
	
	/**
	 * Sets the sign in date.
	 * 
	 * @param signInDate
	 *            the new sign in date
	 */
	public void setSignInDate(Date signInDate) {
		if (signInDate != null)
			this.signInDate = new Timestamp(signInDate.getTime());
	}
	
	/**
	 * Gets the sign out date.
	 * 
	 * @return the sign out date
	 */
	public Date getSignOutDate() {
		return signOutDate;
	}
	
	/**
	 * Sets the sign out date.
	 * 
	 * @param signOutDate
	 *            the new sign out date
	 */
	public void setSignOutDate(Date signOutDate) {
		if (signOutDate != null)
			this.signOutDate = new Timestamp(signOutDate.getTime());
	}
	
	/**
	 * Gets the locked out date.
	 * 
	 * @return the locked out date
	 */
	public Date getLockedOutDate() {
		return lockedOutDate;
	}
	
	/**
	 * Sets the locked out date.
	 * 
	 * @param lockedOutDate
	 *            the new locked out date
	 */
	public void setLockedOutDate(Date lockedOutDate) {
		if (lockedOutDate != null) {
			this.lockedOutDate = new Timestamp(lockedOutDate.getTime());
		}
		else {
			this.lockedOutDate = null;
		}
	}
	
	/**
	 * Gets the measure shares.
	 * 
	 * @return the measure shares
	 */
	public Set<MeasureShare> getMeasureShares() {
		return measureShares;
	}
	
	/**
	 * Sets the measure shares.
	 * 
	 * @param measureShares
	 *            the new measure shares
	 */
	public void setMeasureShares(Set<MeasureShare> measureShares) {
		this.measureShares = measureShares;
	}
	
	/**
	 * Gets the owned measure shares.
	 * 
	 * @return the owned measure shares
	 */
	public Set<MeasureShare> getOwnedMeasureShares() {
		return ownedMeasureShares;
	}
	
	/**
	 * Sets the owned measure shares.
	 * 
	 * @param ownedMeasureShares
	 *            the new owned measure shares
	 */
	public void setOwnedMeasureShares(Set<MeasureShare> ownedMeasureShares) {
		this.ownedMeasureShares = ownedMeasureShares;
	}
	
	/**
	 * Sets the login id.
	 * 
	 * @param loginId
	 *            the loginId to set
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
	
	/** Gets the organization.
	 * 
	 * @return the organization */
	public Organization getOrganization() {
		return organization;
	}

	/** Sets the organization.
	 * 
	 * @param organization the new organization */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return the passwordHistory
	 */
	public Set<UserPasswordHistory> getPasswordHistory() {
		return passwordHistory;
	}

	/**
	 * @param passwordHistory the passwordHistory to set
	 */
	public void setPasswordHistory(Set<UserPasswordHistory> passwordHistory) {
		this.passwordHistory = passwordHistory;
	}

}
