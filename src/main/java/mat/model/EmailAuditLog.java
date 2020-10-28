package mat.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "EMAIL_AUDIT_LOG")
public class EmailAuditLog implements java.io.Serializable {

	private static final long serialVersionUID = -6523654611888748683L;
	private String id;
	private Date timestamp;
	private String loginId;
	private String activityType;

	public EmailAuditLog() {
	}

	public EmailAuditLog(String id, String loginId) {
		this.id = id;
		this.loginId = loginId;
	}

	public EmailAuditLog(String id, String loginId, String activityType) {
		this.id = id;
		this.loginId = loginId;
		this.activityType = activityType;
	}

	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", nullable = false, length = 19)
	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Column(name = "LOGIN_ID", nullable = false, length = 40)
	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@Column(name = "ACTIVITY_TYPE", length = 64)
	public String getActivityType() {
		return this.activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
}
