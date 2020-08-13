package mat.model;

import mat.model.clause.CQLLibrary;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="CQL_AUDIT_LOG")
public class CQLAuditLog {
	private String id;
	private String activityType;	
	private Timestamp time;
	private String userId;
	private CQLLibrary cqlLibrary;
	private String additionalInfo;

	public CQLAuditLog() {
		
	}

	public CQLAuditLog(CQLLibrary cqlLibrary, Date time) {
		this.cqlLibrary = cqlLibrary;
		this.time = new Timestamp(time.getTime());
	}

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
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", nullable = false, length = 19)
	public Date getTime() {
		return time;
	}

	public void setTime(Date created) {
		this.time = new Timestamp(created.getTime());
	}

	@Column(name="ACTIVITY_TYPE")
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

	@ManyToOne
	@JoinColumn(name="CQL_ID")
	public CQLLibrary getCqlLibrary() {
		return cqlLibrary;
	}

	public void setCqlLibrary(CQLLibrary cqlLibrary) {
		this.cqlLibrary = cqlLibrary;
	}


	@Column(name = "ADDL_INFO", length = 2000)
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}
