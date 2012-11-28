package mat.reportmodel;

import java.sql.Timestamp;
import java.util.Date;



public class CodeListAuditLog {
	private String id;
	private String activityType;	
	private Timestamp time;
	private String userId;
	private ListObject codeList;
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

	public ListObject getCodeList() {
		return codeList;
	}

	public void setCodeList(ListObject codeList) {
		this.codeList = codeList;
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
