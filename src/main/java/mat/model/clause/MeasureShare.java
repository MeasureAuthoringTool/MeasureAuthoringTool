package mat.model.clause;

import mat.model.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MEASURE_SHARE")
public class MeasureShare {
	
	private String id;
	
	private Measure measure;
	
	private ShareLevel shareLevel;
	
	private User owner;
	
	private User shareUser;
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "MEASURE_SHARE_ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEASURE_ID", nullable = false)
	public Measure getMeasure() {
		return measure;
	}
	
	public void setMeasure(Measure measure) {
		this.measure = measure;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHARE_LEVEL_ID", nullable = false)
	public ShareLevel getShareLevel() {
		return shareLevel;
	}
	
	public void setShareLevel(ShareLevel shareLevel) {
		this.shareLevel = shareLevel;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEASURE_OWNER_USER_ID", nullable = false)
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHARE_USER_ID", nullable = false)
	public User getShareUser() {
		return shareUser;
	}
	
	public void setShareUser(User shareUser) {
		this.shareUser = shareUser;
	}
}
