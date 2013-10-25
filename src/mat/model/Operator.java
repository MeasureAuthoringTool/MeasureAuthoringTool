package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class Operator.
 */
public class Operator implements IsSerializable{
	
	/** The id. */
	private String id;
	
	/** The long name. */
	private String longName;
	
	/** The short name. */
	private String shortName;
	
	/** The operator type. */
	private OperatorType operatorType;
	
	
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
	 * Gets the long name.
	 * 
	 * @return the long name
	 */
	public String getLongName() {
		return longName;
	}
	
	/**
	 * Sets the long name.
	 * 
	 * @param longName
	 *            the new long name
	 */
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	/**
	 * Gets the short name.
	 * 
	 * @return the short name
	 */
	public String getShortName() {
		return shortName;
	}
	
	/**
	 * Sets the short name.
	 * 
	 * @param shortName
	 *            the new short name
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	/**
	 * Gets the operator type.
	 * 
	 * @return the operator type
	 */
	public OperatorType getOperatorType() {
		return operatorType;
	}
	
	/**
	 * Sets the operator type.
	 * 
	 * @param operatorType
	 *            the new operator type
	 */
	public void setOperatorType(OperatorType operatorType) {
		this.operatorType = operatorType;
	}
	
}
