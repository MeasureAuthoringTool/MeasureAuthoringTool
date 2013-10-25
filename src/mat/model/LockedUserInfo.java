/**
 * 
 */
package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class LockedUserInfo.
 * 
 * @author vandavar This class has got bare minimum user information to display
 *         the locked user details. Need not need to use the entire User model
 *         since that involves transferring the security role and password model
 *         over the network from server to client.
 */
public class LockedUserInfo  implements IsSerializable {
	
	/** The user id. */
	private String userId;
	
	/** The email address. */
	private String emailAddress;
	
	/** The First name. */
	private String FirstName;
	
	/** The Last name. */
	private String LastName;
	
	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * Sets the user id.
	 * 
	 * @param userId
	 *            the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * Gets the first name.
	 * 
	 * @return the first name
	 */
	public String getFirstName() {
		return FirstName;
	}
	
	/**
	 * Sets the first name.
	 * 
	 * @param firstName
	 *            the new first name
	 */
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	
	/**
	 * Gets the last name.
	 * 
	 * @return the last name
	 */
	public String getLastName() {
		return LastName;
	}
	
	/**
	 * Sets the last name.
	 * 
	 * @param lastName
	 *            the new last name
	 */
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	

}
