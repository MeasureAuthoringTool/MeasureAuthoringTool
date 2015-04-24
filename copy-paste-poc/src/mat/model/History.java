/**
 * 
 */
package mat.model;

/**
 * The Class History.
 * 
 * @author vandavar
 */
public class History {
	
	/** The user id. */
	private String userId;
	
	/** The event title. */
	private String eventTitle;
	
	/** The log time stamp. */
	private String logTimeStamp;
	
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
	 * Gets the event title.
	 * 
	 * @return the event title
	 */
	public String getEventTitle() {
		return eventTitle;
	}
	
	/**
	 * Sets the event title.
	 * 
	 * @param eventTitle
	 *            the new event title
	 */
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	
	/**
	 * Gets the log time stamp.
	 * 
	 * @return the log time stamp
	 */
	public String getLogTimeStamp() {
		return logTimeStamp;
	}
	
	/**
	 * Sets the log time stamp.
	 * 
	 * @param logTimeStamp
	 *            the new log time stamp
	 */
	public void setLogTimeStamp(String logTimeStamp) {
		this.logTimeStamp = logTimeStamp;
	}
}
