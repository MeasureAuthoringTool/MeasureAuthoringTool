package org.ifmc.mat.reportmodel;

import java.sql.Timestamp;
import java.util.Date;

public class MeasureAuditLog {
	private String id;
	private String activityType;	
	private Timestamp time;
	private String userId;
	private Measure measure;
	private String additionalInfo;

	public Date getTime() {
		return (Date)time;
	}

	public void setTime(Date created) {
		this.time = new Timestamp(created.getTime());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
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

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}
