package mat.DTO;


import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Data Transfer Object for Audit Log (Measure, CodeList etc.,)
 *
 */
public class AuditLogDTO implements IsSerializable{

	private String id;
	private String componentId;
	private String activityType;
	private String userId;
	private Date eventTs;
	private String additionlInfo;
	
	public AuditLogDTO(){
		
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
