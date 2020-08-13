package mat.dto;


import com.google.gwt.user.client.rpc.IsSerializable;

import java.sql.Timestamp;
import java.util.Date;

public class UserAuditLogDTO implements IsSerializable{

	
	/** The id. */
	private String id;

	/** The action type. */
	private String actionType;
	
	/** The activity type. */
	private String activityType;
	
	/** The time. */
	private Timestamp time;

	/** The user id. */
	private String userEmail;

	/** The measure. */
	private String userId;

	/** The additional info. */
	private String additionalInfo;
	

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
	 * Gets the action type.
	 *
	 * @return the action type
	 */
	public String getActionType() {
		return actionType;
	}

	/**
	 * Sets the action type.
	 *
	 * @param actionType the new action type
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
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
	 * @param activityType the new activity type
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * Sets the time.
	 *
	 * @param time the new time
	 */
	public void setTime(Date time) {
		this.time = new Timestamp(time.getTime());
	}

	/**
	 * Gets the additional info.
	 *
	 * @return the additional info
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	/**
	 * Sets the additional info.
	 *
	 * @param additionalInfo the new additional info
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserAuditLogDTO() {

	}

	public UserAuditLogDTO(String id, String actionType, String activityType, Date time, String userEmail,
			String userId, String additionalInfo) {
		super();
		this.id = id;
		this.actionType = actionType;
		this.activityType = activityType;
		this.time = new Timestamp(time.getTime());
		this.userEmail = userEmail;
		this.userId = userId;
		this.additionalInfo = additionalInfo;
	}
}
