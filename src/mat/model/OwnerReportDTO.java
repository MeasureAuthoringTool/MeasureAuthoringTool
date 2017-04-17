package mat.model;

public class OwnerReportDTO {
	
	private String firstName; 
	
	private String lastName; 
	
	private String organization; 
	
	private String name; 
	
	private String id; 
	
	public OwnerReportDTO(String firstName, String lastName, String organization, String name, String id) {
		this.firstName = firstName; 
		this.lastName = lastName; 
		this.organization = organization; 
		this.name = name; 
		this.id = id; 
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
