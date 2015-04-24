package mat.client.myAccount;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class MyAccountModel.
 */
public class MyAccountModel implements IsSerializable {
	
	/** The first name. */
	private String firstName;
	
	/** The middle initial. */
	private String middleInitial;
	
	/** The last name. */
	private String lastName;
	
	/** The title. */
	private String title;
	
	/** The email address. */
	private String emailAddress;
	
	/** The phone number. */
	private String phoneNumber;
	
	/** The organisation. */
	private String organisation;
	
	/** The oid. */
	private String oid;
	//private String rootoid;
	/** The login id. */
	private String loginId;
	
	/**
	 * Gets the organisation.
	 * 
	 * @return the organisation
	 */
	public String getOrganisation() {
		return organisation;
	}
	
	/**
	 * Sets the organisation.
	 * 
	 * @param organisation
	 *            the new organisation
	 */
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
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
	 * @param oid
	 *            the new oid
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}
	/*public String getRootoid() {
		return rootoid;
	}
	public void setRootoid(String rootoid) {
		this.rootoid = rootoid;
	}*/
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
	 * @param middleInitial
	 *            the new middle initial
	 */
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
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
}
