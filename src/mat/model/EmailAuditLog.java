package mat.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The Class MeasureAuditLog.
 */
public class EmailAuditLog {
	
	/** The id. */
	private String id;
	
	/** The activity type. */
	private String activityType;	
	
	/** The time. */
	private Timestamp time;
	
	/** The user id. */
	private String loginId;
	
	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public Date getTime() {
		return (Date)time;
	}

	/**
	 * Sets the time.
	 * 
	 * @param created
	 *            the new time
	 */
	public void setTime(Date created) {
		this.time = new Timestamp(created.getTime());
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
	 * Gets the activity type.
	 * 
	 * @return the activity type
	 */
	public String getActivityType() {
		return activityType;
	}

	/**
	 * Sets the activity type.
	 * 
	 * @param activityType
	 *            the new activity type
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * Sets the user id.
	 * 
	 * @param userId
	 *            the new user id
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
}
