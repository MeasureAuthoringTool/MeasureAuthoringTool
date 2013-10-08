package mat.client.admin;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ManageUsersDetailModel implements IsSerializable{
	private String userID;
	private String firstName;
	private String lastName;
	private String middleInitial;
	private String title;
	private String emailAddress;
	private String loginId;
	private String phoneNumber;
	private String organization;
	private String oid;
	//private String rootOid;
	private String role;
	
	private boolean active = true;
	private boolean isExistingUser;
	private boolean isLocked;
	
	private boolean currentUserCanUnlock;
	private boolean currentUserCanChangeAccountStatus;
	
	public boolean isCurrentUserCanUnlock() {
		return currentUserCanUnlock;
	}
	public void setCurrentUserCanUnlock(boolean currentUserCanUnlock) {
		this.currentUserCanUnlock = currentUserCanUnlock;
	}
	public boolean isCurrentUserCanChangeAccountStatus() {
		return currentUserCanChangeAccountStatus;
	}
	public void setCurrentUserCanChangeAccountStatus(boolean currentUserCanChangeRole) {
		this.currentUserCanChangeAccountStatus = currentUserCanChangeRole;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = doTrim(userID);
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = doTrim(firstName);
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = doTrim(lastName);
	}
	public String getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = doTrim(middleInitial);
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = doTrim(title);
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = doTrim(emailAddress);
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = doTrim(phoneNumber);
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = doTrim(organization);
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getKey() {
		return userID;
	}
	public boolean isExistingUser() {
		return isExistingUser;
	}
	public void setExistingUser(boolean isExistingUser) {
		this.isExistingUser = isExistingUser;
	}
	public boolean isLocked() {
		return isLocked;
	}
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = doTrim(oid);
	}
	/*public String getRootOid() {
		return rootOid;
	}
	public void setRootOid(String rootOid) {
		this.rootOid = doTrim(rootOid);
	}*/
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = doTrim(role);
	}
	
	private String doTrim(String str){
		if(str == null)
			return str;
		else
			return str.trim();
	}
	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}
	
}
