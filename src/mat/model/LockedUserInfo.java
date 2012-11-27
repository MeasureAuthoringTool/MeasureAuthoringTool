/**
 * 
 */
package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author vandavar
 * This class has got bare minimum user information  to display the locked user 
 * details. 
 * Need not need to use the entire User model since that involves transferring 
 * the security role and password model over the network from server to client.
 *
 */
public class LockedUserInfo  implements IsSerializable {
	private String userId;
	private String emailAddress;
	private String FirstName;
	private String LastName;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	

}
