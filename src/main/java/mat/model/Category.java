package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CATEGORY")
public class Category {
	private String id;
	private String description;
	private String abbreviation;
	private Set<DataType> dataTypes = new HashSet<DataType>();
	private Set<CodeSystem> codeSystems = new HashSet<CodeSystem>();
	
	@Id
	@Column(name = "CATEGORY_ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "DESCRIPTION", nullable = false, length = 50)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "ABBREVIATION", nullable = false, length = 50)
	public String getAbbreviation() {
		return abbreviation;
	}
	
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	public Set<DataType> getDataTypes() {
		return dataTypes;
	}
	
	public void setDataTypes(Set<DataType> dataTypes) {
		this.dataTypes = dataTypes;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	public Set<CodeSystem> getCodeSystems() {
		return codeSystems;
	}
	
	public void setCodeSystems(Set<CodeSystem> codeSystems) {
		this.codeSystems = codeSystems;
	}
}
