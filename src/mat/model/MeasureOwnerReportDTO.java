package mat.model;


public class MeasureOwnerReportDTO {
	String firstName;
	String lastName;
	String organizationName;
	String measureDescription;
	int cmsNumber;
	String nqfId;
	String guid;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getMeasureDescription() {
		return measureDescription;
	}
	public void setMeasureDescription(String measureDescription) {
		this.measureDescription = measureDescription;
	}
	public int getCmsNumber() {
		return cmsNumber;
	}
	public void setCmsNumber(int cmsNumber) {
		this.cmsNumber = cmsNumber;
	}
	public String getNqfId() {
		return nqfId;
	}
	public void setNqfId(String nqfId) {
		this.nqfId = nqfId;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
}
