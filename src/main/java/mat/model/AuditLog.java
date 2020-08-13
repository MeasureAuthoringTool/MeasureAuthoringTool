package mat.model;

import mat.model.clause.Measure;
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
@Table(name="AUDIT_LOG")
public class AuditLog {
	private String id;
	private String activityType;
	private Timestamp createDate;
	private String createdBy;
	private Timestamp updateDate;
	private String updatedBy;
	private Measure measure;
	private QualityDataSet qds;
	private ListObject listObject;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", nullable = false, length = 19)
	public Date getCreateDate() {
		return (Date)createDate;
	}

	public void setCreateDate(Date created) {
		this.createDate = new Timestamp(created.getTime());
	}

	@Column(name="CREATE_USER")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name="AUDIT_LOG_ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE", length = 19)
	public Date getUpdateDate() {
		return (Date)updateDate;
	}

	public void setUpdateDate(Date updated) {
		if (updated != null)
			this.updateDate = new Timestamp(updated.getTime());
	}

	@Column(name = "UPDATE_USER")
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@ManyToOne
	@JoinColumn(name="MEASURE_ID")
	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	@ManyToOne
	@JoinColumn(name="QDM_ID")
	public QualityDataSet getQds() {
		return qds;
	}

	public void setQds(QualityDataSet qds) {
		this.qds = qds;
	}

	@Column(name="ACTIVITY_TYPE")
	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	@ManyToOne
	@JoinColumn(name="LIST_OBJECT_ID")
	public ListObject getListObject() {
		return listObject;
	}

	public void setListObject(ListObject listObject) {
		this.listObject = listObject;
	}
	
}
