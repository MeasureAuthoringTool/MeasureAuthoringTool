package mat.model;

/**
 * The CQL Library Ownership Report Data Transfer Object
 */
public class CQLLibraryOwnerReportDTO {
	
	private String cqlLibraryName; 
	
	private String type; 
	
	private String status; 
	
	private String versionNumber; 
	
	private String id; 
	
	private String setId;
	
	private String firstName; 
	
	private String lastName; 
	
	private String organization;

	public CQLLibraryOwnerReportDTO(String cqlLibraryName, String type, String status, String versionNumber,
			String id, String setId, String firstName, String lastName, String organization) {
		super();
		this.cqlLibraryName = cqlLibraryName;
		this.type = type;
		this.status = status;
		this.versionNumber = versionNumber;
		this.id = id; 
		this.setId = setId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.organization = organization;
	}

	public String getCqlLibraryName() {
		return cqlLibraryName;
	}

	public void setCqlLibraryName(String cqlLibraryName) {
		this.cqlLibraryName = cqlLibraryName;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	} 
	
	public String getSetId() {
		return setId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}

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

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}
}
