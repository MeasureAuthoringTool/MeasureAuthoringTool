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
@Table(name = "RECENT_CQL_ACTIVITY_LOG")
public class RecentCQLActivityLog {
	private String id;
	private Timestamp time;
	private String userId;
	private String cqlId;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="CQL_ID")
	public String getCqlId() {
		return cqlId;
	}

	public void setCqlId(String cqlId) {
		this.cqlId = cqlId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", nullable = false, length = 19)
	public Date getTime() {
		return (Date)time;
	}

	public void setTime(Date created) {
		this.time = new Timestamp(created.getTime());
	}

	@Column(name="USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
}
