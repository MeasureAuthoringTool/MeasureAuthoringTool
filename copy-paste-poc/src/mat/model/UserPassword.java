package mat.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The Class UserPassword.
 */
public class UserPassword {
	
	/** The id. */
	private String id;
	
	/** The user. */
	private User user;
	
	/** The passwordlock counter. */
	private int passwordlockCounter;
	
	/** The first failed attempt time. */
	private Timestamp firstFailedAttemptTime;
	
	/** The forgot pwdlock counter. */
	private int forgotPwdlockCounter;
	
	/** The password. */
	private String password;
	
	/** The salt. */
	private String salt;
	
	/** The initial. */
	private boolean initial;
	
	/** The temporary password. */
	private boolean temporaryPassword;
	
	/** The created date. */
	private Date createdDate;
	
	/**
	 * Instantiates a new user password.
	 */
	public UserPassword() {
		
	}
	
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
	 * @param user
	 *            the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Gets the passwordlock counter.
	 * 
	 * @return the passwordlock counter
	 */
	public int getPasswordlockCounter() {
		return passwordlockCounter;
	}
	
	/**
	 * Sets the passwordlock counter.
	 * 
	 * @param passwordlockCounter
	 *            the new passwordlock counter
	 */
	public void setPasswordlockCounter(int passwordlockCounter) {
		this.passwordlockCounter = passwordlockCounter;
	}
	
	/**
	 * Gets the forgot pwdlock counter.
	 * 
	 * @return the forgot pwdlock counter
	 */
	public int getForgotPwdlockCounter() {
		return forgotPwdlockCounter;
	}
	
	/**
	 * Sets the forgot pwdlock counter.
	 * 
	 * @param forgotPwdlockCounter
	 *            the new forgot pwdlock counter
	 */
	public void setForgotPwdlockCounter(int forgotPwdlockCounter) {
		this.forgotPwdlockCounter = forgotPwdlockCounter;
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
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Checks if is initial.
	 * 
	 * @return true, if is initial
	 */
	public boolean isInitial() {
		return initial;
	}
	
	/**
	 * Sets the initial.
	 * 
	 * @param initial
	 *            the new initial
	 */
	public void setInitial(boolean initial) {
		this.initial = initial;
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
	 * @param createdDate
	 *            the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	/**
	 * Sets the first failed attempt time.
	 * 
	 * @param firstFailedAttemptTime
	 *            the new first failed attempt time
	 */
	public void setFirstFailedAttemptTime(Timestamp firstFailedAttemptTime) {
		this.firstFailedAttemptTime = firstFailedAttemptTime;
	}
	
	/**
	 * Gets the first failed attempt time.
	 * 
	 * @return the first failed attempt time
	 */
	public Timestamp getFirstFailedAttemptTime() {
		return firstFailedAttemptTime;
	}
	
	/**
	 * Sets the temporary password.
	 * 
	 * @param temporaryPassword
	 *            the new temporary password
	 */
	public void setTemporaryPassword(boolean temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}
	
	/**
	 * Checks if is temporary password.
	 * 
	 * @return true, if is temporary password
	 */
	public boolean isTemporaryPassword() {
		return temporaryPassword;
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
	 * @param salt
	 *            the new salt
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
}