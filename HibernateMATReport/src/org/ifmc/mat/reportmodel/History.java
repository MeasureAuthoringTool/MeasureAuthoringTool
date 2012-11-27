/**
 * 
 */
package org.ifmc.mat.reportmodel;

/**
 * @author vandavar
 *
 */
public class History {
	private String userId;
	private String eventTitle;
	private String logTimeStamp;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	public String getLogTimeStamp() {
		return logTimeStamp;
	}
	public void setLogTimeStamp(String logTimeStamp) {
		this.logTimeStamp = logTimeStamp;
	}
}
