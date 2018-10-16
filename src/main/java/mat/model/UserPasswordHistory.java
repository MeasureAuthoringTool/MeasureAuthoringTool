package mat.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;


// TODO: Auto-generated Javadoc
/**
 * The Class UserPasswordHistory.
 */
public class UserPasswordHistory implements IsSerializable, Serializable{


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2972696565028280765L;

	/** The id. */
	private String id;
	
	/** The user. */
	private User user;
	
	/** The password. */
	private String password;
	
	/** The salt. */
	private String salt;
	
	/** The created date. */
	private Date createdDate;
	
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
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the salt.
	 *
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * Sets the salt.
	 *
	 * @param salt the new salt
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets the user.
	 *
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
