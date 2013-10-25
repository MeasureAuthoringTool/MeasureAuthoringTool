package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class OperatorType.
 */
public class OperatorType implements IsSerializable{
	
	/** The id. */
	private String id;
	
	/** The operator type. */
	private String operatorType;
	
	
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
	 * Gets the operator type.
	 * 
	 * @return the operator type
	 */
	public String getOperatorType() {
		return operatorType;
	}
	
	/**
	 * Sets the operator type.
	 * 
	 * @param operatorType
	 *            the new operator type
	 */
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

}
