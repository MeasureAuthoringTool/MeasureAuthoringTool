package org.ifmc.mat.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ifmc.mat.model.clause.MeasureShare;


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
	private String organizationName;
	private String orgOID;
	private String rootOID;
	private UserPassword password;
	private AuditLog auditLog;
	private Status status;
	private SecurityRole securityRole;	
	private List<UserSecurityQuestion> securityQuestions = new ArrayList<UserSecurityQuestion>();
	private Set<MeasureShare> measureShares = new HashSet<MeasureShare>();
	private Set<MeasureShare> ownedMeasureShares = new HashSet<MeasureShare>();
	

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
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getOrgOID() {
		return orgOID;
	}
	public void setOrgOID(String orgOID) {
		this.orgOID = orgOID;
	}
	public String getRootOID() {
		return rootOID;
	}
	public void setRootOID(String rootOID) {
		this.rootOID = rootOID;
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
	public List<UserSecurityQuestion> getSecurityQuestions() {
		return securityQuestions;
	}
	public void setSecurityQuestions(List<UserSecurityQuestion> securityQuestions) {
		this.securityQuestions = securityQuestions;
	}			
	public Date getSignInDate() {
		return signInDate;
	}
	public void setSignInDate(Date signInDate) {
		if (signInDate != null)
			this.signInDate = new Timestamp(signInDate.getTime());
	}
	public Date getSignOutDate() {
		return signOutDate;
	}
	public void setSignOutDate(Date signOutDate) {
		if (signOutDate != null)
			this.signOutDate = new Timestamp(signOutDate.getTime());
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

}
