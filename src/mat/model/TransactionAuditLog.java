package mat.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The Class TransactionAuditLog.
 * 
 * @author aschmidt
 */
public class TransactionAuditLog {
	
	/** The id. */
	private String id;
	
	/** The primary id. */
	private String primaryId;
	
	/** The secondary id. */
	private String secondaryId;
	
	/** The activity type. */
	private String activityType;
	
	/** The user id. */
	private String userId;
	
	/** The time. */
	private Timestamp time;
	
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
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the primary id.
	 * 
	 * @return the primary id
	 */
	public String getPrimaryId() {
		return primaryId;
	}
	
	/**
	 * Sets the primary id.
	 * 
	 * @param primaryId
	 *            the new primary id
	 */
	public void setPrimaryId(String primaryId) {
		this.primaryId = primaryId;
	}
	
	/**
	 * Gets the secondary id.
	 * 
	 * @return the secondary id
	 */
	public String getSecondaryId() {
		return secondaryId;
	}
	
	/**
	 * Sets the secondary id.
	 * 
	 * @param secondaryId
	 *            the new secondary id
	 */
	public void setSecondaryId(String secondaryId) {
		this.secondaryId = secondaryId;
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
	 * @param additionalInfo
	 *            the new additional info
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}
