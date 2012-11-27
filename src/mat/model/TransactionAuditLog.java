package mat.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author aschmidt
 *
 */
public class TransactionAuditLog {
	
	private String id;
	private String primaryId;
	private String secondaryId;
	private String activityType;
	private String userId;
	private Timestamp time;
	private String additionalInfo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrimaryId() {
		return primaryId;
	}
	public void setPrimaryId(String primaryId) {
		this.primaryId = primaryId;
	}
	public String getSecondaryId() {
		return secondaryId;
	}
	public void setSecondaryId(String secondaryId) {
		this.secondaryId = secondaryId;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getTime() {
		return (Date)time;
	}
	public void setTime(Date created) {
		this.time = new Timestamp(created.getTime());
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}
