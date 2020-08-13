package mat.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "USER_AUDIT_LOG")
public class UserAuditLog {
	private String id;
	private String actionType;
	private String activityType;
	private Timestamp time;
	private String userEmail;
	private User user;
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

	@Column(name = "ACTION_TYPE", nullable = false, length = 32)
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Column(name = "ACTIVITY_TYPE", nullable = false, length = 40)
	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", nullable = false, length = 19)
	public Date getTime() {
		return (Date)time;
	}

	public void setTime(Date time) {
		this.time = new Timestamp(time.getTime());
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "USER_ID", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "ADDL_INFO", length = 2000)
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	@Column(name = "USER_EMAIL", nullable = false, length = 40)
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

}
