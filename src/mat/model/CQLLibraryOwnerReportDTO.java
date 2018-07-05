package mat.model;

/**
 * The CQL Library Ownership Report Data Transfer Object
 */
public class CQLLibraryOwnerReportDTO extends OwnerReportDTO {
	
	private String type; 
	
	private String status; 
	
	private String versionNumber; 
	
	private String setId;

	public CQLLibraryOwnerReportDTO(String cqlLibraryName, String type, String status, String versionNumber,
			String id, String setId, String firstName, String lastName, String organization) {
		super(firstName, lastName, organization, cqlLibraryName, id);
		this.type = type;
		this.status = status;
		this.versionNumber = versionNumber;
		this.setId = setId;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getSetId() {
		return setId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}
}
