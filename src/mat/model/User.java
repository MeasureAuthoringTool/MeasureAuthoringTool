package mat.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mat.model.clause.MeasureShare;
import mat.model.cql.CQLLibraryShare;


public class User  {
	
	private String id;
	
	private String firstName;
	
	private String middleInit;
	
	private String lastName;
	
	private String emailAddress;
	
	private String phoneNumber;
	
	private String title;
	
	private Date activationDate;
	
	private Date terminationDate;
	
	private Timestamp signInDate;
	
	private Timestamp signOutDate;
	
	private Timestamp lockedOutDate;
	
	private Organization organization;

	private UserPassword password;
	
	private AuditLog auditLog;
	
	private Status status;
	
	private SecurityRole securityRole;	
	
	private UserBonnieAccessInfo userBonnieAccessInfo;
	
	private List<UserSecurityQuestion> userSecurityQuestions = new ArrayList<UserSecurityQuestion>();
	
	private Set<MeasureShare> measureShares = new HashSet<MeasureShare>();
	
	private Set<MeasureShare> ownedMeasureShares = new HashSet<MeasureShare>();
	
	private Set<CQLLibraryShare> cqlLibraryShares = new HashSet<CQLLibraryShare>();
	
	private Set<CQLLibraryShare> ownedCQLLibraryShares = new HashSet<CQLLibraryShare>();
	
	private String loginId;
	
	private String sessionId;
	
	private Set<UserPasswordHistory> passwordHistory = new  HashSet<UserPasswordHistory>();
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getMiddleInit() {
		return middleInit;
	}
	

	public void setMiddleInit(String middleInit) {
		this.middleInit = middleInit;
	}
	

	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public Date getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}

	public String getOrganizationName() {
		String orgName = "";
		if(this.organization != null){
			orgName = this.organization.getOrganizationName();
		}
		return orgName;
	}

	public String getOrgOID() {
		String orgOID = "";
		if(this.organization != null){
			orgOID = this.organization.getOrganizationOID();
		}
		return orgOID;
	}

	
	public String getOrganizationId() {
		String orgId = "";
		if(this.organization != null){
			orgId = String.valueOf(this.organization.getId());
		}
		return orgId;
	}

	public String getRootOID() {
		return "";
	}

	public UserPassword getPassword() {
		return password;
	}

	public void setPassword(UserPassword password) {
		if(password != null && password.getUser() == null) {
			password.setUser(this);
		}
		this.password = password;
	}
	

	public AuditLog getAuditLog() {
		if(auditLog == null) {
			auditLog = new AuditLog();
		}
		return auditLog;
	}

	public void setAuditLog(AuditLog auditLog) {
		this.auditLog = auditLog;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	

	public SecurityRole getSecurityRole() {
		return securityRole;
	}

	public void setSecurityRole(SecurityRole securityRole) {
		this.securityRole = securityRole;
	}

	public List<UserSecurityQuestion> getUserSecurityQuestions() {
		return userSecurityQuestions;
	}

	public void setUserSecurityQuestions(List<UserSecurityQuestion> securityQuestions) {
		this.userSecurityQuestions = securityQuestions;
	}			

	public Date getSignInDate() {
		return signInDate;
	}
	

	public void setSignInDate(Date signInDate) {
		if (signInDate != null) {
			this.signInDate = new Timestamp(signInDate.getTime());
		} else {
			this.signInDate = null;
		}
	}

	public Date getSignOutDate() {
		return signOutDate;
	}
	
	public void setSignOutDate(Date signOutDate) {
		if (signOutDate != null) {
			this.signOutDate = new Timestamp(signOutDate.getTime());
		} else {
			this.signOutDate = null;
		}
	}

	public Date getLockedOutDate() {
		return lockedOutDate;
	}

	public void setLockedOutDate(Date lockedOutDate) {
		if (lockedOutDate != null) {
			this.lockedOutDate = new Timestamp(lockedOutDate.getTime());
		}
		else {
			this.lockedOutDate = null;
		}
	}
	public UserBonnieAccessInfo getUserBonnieAccessInfo() {
		return userBonnieAccessInfo;
	}
	
	public void setUserBonnieAccessInfo(UserBonnieAccessInfo userBonnieAccessInfo) {
		this.userBonnieAccessInfo = userBonnieAccessInfo;
	}
	public Set<MeasureShare> getMeasureShares() {
		return measureShares;
	}
	

	public void setMeasureShares(Set<MeasureShare> measureShares) {
		this.measureShares = measureShares;
	}

	public Set<MeasureShare> getOwnedMeasureShares() {
		return ownedMeasureShares;
	}

	public void setOwnedMeasureShares(Set<MeasureShare> ownedMeasureShares) {
		this.ownedMeasureShares = ownedMeasureShares;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginId() {
		return loginId;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Set<UserPasswordHistory> getPasswordHistory() {
		return passwordHistory;
	}

	public void setPasswordHistory(Set<UserPasswordHistory> passwordHistory) {
		this.passwordHistory = passwordHistory;
	}

	public Set<CQLLibraryShare> getCqlLibraryShares() {
		return cqlLibraryShares;
	}

	public void setCqlLibraryShares(Set<CQLLibraryShare> cqlLibraryShares) {
		this.cqlLibraryShares = cqlLibraryShares;
	}

	public Set<CQLLibraryShare> getOwnedCQLLibraryShares() {
		return ownedCQLLibraryShares;
	}

	public void setOwnedCQLLibraryShares(Set<CQLLibraryShare> ownedCQLLibraryShares) {
		this.ownedCQLLibraryShares = ownedCQLLibraryShares;
	}

}
