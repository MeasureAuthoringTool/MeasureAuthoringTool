package mat.model;

import java.sql.Timestamp;
import java.util.Date;

import mat.model.clause.Measure;

/**
 * The Class AuditLog.
 */
public class AuditLog {
	
	/** The id. */
	private String id;
	
	/** The activity type. */
	private String activityType;
	
	/** The create date. */
	private Timestamp createDate;
	
	/** The created by. */
	private String createdBy;

	/** The update date. */
	private Timestamp updateDate;
	
	/** The updated by. */
	private String updatedBy;
	
	/** The measure. */
	private Measure measure;
	
	/** The qds. */
	private QualityDataSet qds;
	
	/** The list object. */
	private ListObject listObject;

	/**
	 * Gets the creates the date.
	 * 
	 * @return the creates the date
	 */
	public Date getCreateDate() {
		return (Date)createDate;
	}

	/**
	 * Sets the creates the date.
	 * 
	 * @param created
	 *            the new creates the date
	 */
	public void setCreateDate(Date created) {
		this.createDate = new Timestamp(created.getTime());
	}

	/**
	 * Gets the created by.
	 * 
	 * @return the created by
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the created by.
	 * 
	 * @param createdBy
	 *            the new created by
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the update date.
	 * 
	 * @return the update date
	 */
	public Date getUpdateDate() {
		return (Date)updateDate;
	}

	/**
	 * Sets the update date.
	 * 
	 * @param updated
	 *            the new update date
	 */
	public void setUpdateDate(Date updated) {
		if (updated != null)
			this.updateDate = new Timestamp(updated.getTime());
	}

	/**
	 * Gets the updated by.
	 * 
	 * @return the updated by
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * Sets the updated by.
	 * 
	 * @param updatedBy
	 *            the new updated by
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * Gets the measure.
	 * 
	 * @return the measure
	 */
	public Measure getMeasure() {
		return measure;
	}

	/**
	 * Sets the measure.
	 * 
	 * @param measure
	 *            the new measure
	 */
	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	

	/**
	 * Gets the qds.
	 * 
	 * @return the qds
	 */
	public QualityDataSet getQds() {
		return qds;
	}

	/**
	 * Sets the qds.
	 * 
	 * @param qds
	 *            the new qds
	 */
	public void setQds(QualityDataSet qds) {
		this.qds = qds;
	}

	/**
	 * Gets the activity type.
	 * 
	 * @return the activity type
	 */
	public String getActivityType() {
		return activityType;
	}

	/**
	 * Sets the activity type.
	 * 
	 * @param activityType
	 *            the new activity type
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * Gets the list object.
	 * 
	 * @return the list object
	 */
	public ListObject getListObject() {
		return listObject;
	}

	/**
	 * Sets the list object.
	 * 
	 * @param listObject
	 *            the new list object
	 */
	public void setListObject(ListObject listObject) {
		this.listObject = listObject;
	}
	
}
