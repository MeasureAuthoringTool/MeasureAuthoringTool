package mat.model;

import java.sql.Timestamp;
import java.util.Date;

import mat.model.clause.Clause;
import mat.model.clause.Measure;

public class AuditLog {
	private String id;
	private String activityType;
	
	private Timestamp createDate;
	private String createdBy;

	private Timestamp updateDate;
	private String updatedBy;
	
	private Measure measure;
	private Clause clause;
	private QualityDataSet qds;
	private ListObject listObject;

	public Date getCreateDate() {
		return (Date)createDate;
	}

	public void setCreateDate(Date created) {
		this.createDate = new Timestamp(created.getTime());
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getUpdateDate() {
		return (Date)updateDate;
	}

	public void setUpdateDate(Date updated) {
		if (updated != null)
			this.updateDate = new Timestamp(updated.getTime());
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	public Clause getClause() {
		return clause;
	}

	public void setClause(Clause clause) {
		this.clause = clause;
	}

	public QualityDataSet getQds() {
		return qds;
	}

	public void setQds(QualityDataSet qds) {
		this.qds = qds;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public ListObject getListObject() {
		return listObject;
	}

	public void setListObject(ListObject listObject) {
		this.listObject = listObject;
	}
	
}
