package mat.model.clause;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class QDSAttributes.
 */
public class QDSAttributes implements IsSerializable {
	
	/** The id. */
	private String id;
	
	/** The name. */
	private String name;
	
	/** The data type id. */
	private String dataTypeId;
	
	/** The q ds attribute type. */
	private String qDSAttributeType;
	
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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the data type id.
	 * 
	 * @return the data type id
	 */
	public String getDataTypeId() {
		return dataTypeId;
	}
	
	/**
	 * Sets the data type id.
	 * 
	 * @param dataTypeId
	 *            the new data type id
	 */
	public void setDataTypeId(String dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	
	/**
	 * Gets the q ds attribute type.
	 * 
	 * @return the q ds attribute type
	 */
	public String getqDSAttributeType() {
		return qDSAttributeType;
	}
	
	/**
	 * Sets the q ds attribute type.
	 * 
	 * @param qDSAttributeType
	 *            the new q ds attribute type
	 */
	public void setqDSAttributeType(String qDSAttributeType) {
		this.qDSAttributeType = qDSAttributeType;
	}
}
