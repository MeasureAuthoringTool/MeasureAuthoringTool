package mat.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The Class RecentMeasureActivityLog.
 */
public class RecentCQLActivityLog {

	/** The id. */
	private String id;
	
	/** The time. */
	private Timestamp time;
	
	/** The user id. */
	private String userId;
	
	/** The measure id. */
	private String cqlId;

	public String getCqlId() {
		return cqlId;
	}

	public void setCqlId(String cqlId) {
		this.cqlId = cqlId;
	}

	/**
	 * Gets the id.
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the time.
	 * @return the time
	 */
	public Date getTime() {
		return (Date)time;
	}

	
	/**
	 * Sets the time.
	 * @param created the new time
	 */
	public void setTime(Date created) {
		this.time = new Timestamp(created.getTime());
	}

	/**
	 * Gets the user id.
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 * @param userId the user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
}
