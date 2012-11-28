package mat.reportmodel;

import java.util.HashSet;
import java.util.Set;

public class Category {
	private String id;
	private String description;
	private String abbreviation;
	
	private Set<DataType> dataTypes = new HashSet<DataType>();
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<DataType> getDataTypes() {
		return dataTypes;
	}
	public void setDataTypes(Set<DataType> dataTypes) {
		this.dataTypes = dataTypes;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
}
