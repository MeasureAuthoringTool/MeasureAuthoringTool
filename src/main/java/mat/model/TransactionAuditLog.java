package mat.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="TRANSACTION_AUDIT_LOG")
public class TransactionAuditLog {
	private String id;
	private String primaryId;
	private String secondaryId;
	private String activityType;
	private String userId;
	private Timestamp time;
	private String additionalInfo;
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "PRIMARY_ID", length = 40)
	public String getPrimaryId() {
		return primaryId;
	}
	
	public void setPrimaryId(String primaryId) {
		this.primaryId = primaryId;
	}
	
	@Column(name = "SECONDARY_ID", length = 40)
	public String getSecondaryId() {
		return secondaryId;
	}
	
	public void setSecondaryId(String secondaryId) {
		this.secondaryId = secondaryId;
	}
	
	@Column(name = "ACTIVITY_TYPE", nullable = false, length = 40)
	public String getActivityType() {
		return activityType;
	}
	
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	
	@Column(name = "USER_ID", nullable = false, length = 40)
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", nullable = false, length = 19)
	public Date getTime() {
		return (Date)time;
	}
	
	public void setTime(Date created) {
		this.time = new Timestamp(created.getTime());
	}
	
	@Column(name = "ADDL_INFO", length = 2000)
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}
