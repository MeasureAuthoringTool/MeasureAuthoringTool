package mat.model;

import java.util.HashSet;
import java.util.Set;

/**
 * The Class Category.
 */
public class Category {
	
	/** The id. */
	private String id;
	
	/** The description. */
	private String description;
	
	/** The abbreviation. */
	private String abbreviation;
	
	/** The data types. */
	private Set<DataType> dataTypes = new HashSet<DataType>();
	
	/** The code systems. */
	private Set<CodeSystem> codeSystems = new HashSet<CodeSystem>();
	
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
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the data types.
	 * 
	 * @return the data types
	 */
	public Set<DataType> getDataTypes() {
		return dataTypes;
	}
	
	/**
	 * Sets the data types.
	 * 
	 * @param dataTypes
	 *            the new data types
	 */
	public void setDataTypes(Set<DataType> dataTypes) {
		this.dataTypes = dataTypes;
	}
	
	/**
	 * Gets the code systems.
	 * 
	 * @return the code systems
	 */
	public Set<CodeSystem> getCodeSystems() {
		return codeSystems;
	}
	
	/**
	 * Sets the code systems.
	 * 
	 * @param codeSystems
	 *            the new code systems
	 */
	public void setCodeSystems(Set<CodeSystem> codeSystems) {
		this.codeSystems = codeSystems;
	}
	
	/**
	 * Gets the abbreviation.
	 * 
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}
	
	/**
	 * Sets the abbreviation.
	 * 
	 * @param abbreviation
	 *            the new abbreviation
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
}
