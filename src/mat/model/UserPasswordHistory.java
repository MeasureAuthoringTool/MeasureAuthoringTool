package mat.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;


// TODO: Auto-generated Javadoc
/**
 * The Class UserPasswordHistory.
 */
public class UserPasswordHistory implements IsSerializable, Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2972696565028280765L;

	/** The id. */
//	private String id;
	
	/** The user. */
	private String userId;
	
	/** The password. */
	private String password;
	
	/** The salt. */
	private String salt;
	
	/** The created date. */
	private Date createdDate;
	
	/** The row id. */
	private String rowId;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
//	public String getId() {
//		return id;
//	}
//
//	/**
//	 * Sets the id.
//	 *
//	 * @param id the new id
//	 */
//	public void setId(String id) {
//		this.id = id;
//	}

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
	 * Gets the user id.
	 *
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the row id.
	 *
	 * @return the rowId
	 */
	public String getRowId() {
		return rowId;
	}

	/**
	 * Sets the row id.
	 *
	 * @param rowId the rowId to set
	 */
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
}
