package mat.DTO;


import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Data Transfer Object for Audit Log (Measure, CodeList etc.,)
 *
 */
public class AuditLogDTO implements IsSerializable{

	/** The id. */
	private String id;
	
	/** The component id. */
	private String componentId;
	
	/** The activity type. */
	private String activityType;
	
	/** The user id. */
	private String userId;
	
	/** The event ts. */
	private Date eventTs;
	
	/** The additionl info. */
	private String additionlInfo;
	
	/**
	 * Instantiates a new audit log dto.
	 */
	public AuditLogDTO(){
		
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
	 * Gets the component id.
	 * 
	 * @return the component id
	 */
	public String getComponentId() {
		return componentId;
	}

	/**
	 * Sets the component id.
	 * 
	 * @param componentId
	 *            the new component id
	 */
	public void setComponentId(String componentId) {
		this.componentId = componentId;
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
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 * 
	 * @param userId
	 *            the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the event ts.
	 * 
	 * @return the event ts
	 */
	public Date getEventTs() {
		return eventTs;
	}

	/**
	 * Sets the event ts.
	 * 
	 * @param eventTs
	 *            the new event ts
	 */
	public void setEventTs(Date eventTs) {
		this.eventTs = eventTs;
	}
	
	/**
	 * Gets the additionl info.
	 * 
	 * @return the additionl info
	 */
	public String getAdditionlInfo() {
		return additionlInfo;
	}

	/**
	 * Sets the additionl info.
	 * 
	 * @param additionlInfo
	 *            the new additionl info
	 */
	public void setAdditionlInfo(String additionlInfo) {
		this.additionlInfo = additionlInfo;
	}	
}
