package mat.model;


public class MeasureOwnerReportDTO extends OwnerReportDTO {
	int cmsNumber;
	String nqfId;
	public MeasureOwnerReportDTO(String firstName, String lastName, String organization, String measureDescription, 
			int cmsNumber, String nqfId, String guid) {
		super(firstName, lastName, organization, measureDescription, guid); 
		this.cmsNumber = cmsNumber; 
		this.nqfId = nqfId; 
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
}
