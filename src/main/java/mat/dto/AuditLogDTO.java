package mat.dto;


import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AuditLogDTO implements IsSerializable{

	private String id;

	private String componentId;
	
	private String activityType;
	
	private String userId;
	
	private Date eventTs;
	
	private String additionlInfo;
	
	public AuditLogDTO(){
		
	}

	public AuditLogDTO(String id, String activityType, String userId, Date eventTs, String additionlInfo) {
		super();
		this.id = id;
		this.activityType = activityType;
		this.userId = userId;
		this.eventTs = eventTs;
		this.additionlInfo = additionlInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
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

	public Date getEventTs() {
		return eventTs;
	}

	public void setEventTs(Date eventTs) {
		this.eventTs = eventTs;
	}
	
	public String getAdditionlInfo() {
		return additionlInfo;
	}

	public void setAdditionlInfo(String additionlInfo) {
		this.additionlInfo = additionlInfo;
	}	
}
