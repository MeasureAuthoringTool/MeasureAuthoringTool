package mat.DTO;


import mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * DTO for operator object.
 */
public class OperatorDTO implements IsSerializable, HasListBox {
	
	/** The id. */
	private String id;
	
	/** The operator. */
	private String operator;
	
	/** The operator type. */
	private String operatorType;
	
	/**
	 * Instantiates a new operator dto.
	 */
	public OperatorDTO(){
		
	}
	
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
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getValue()
	 */
	public String getValue() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getItem()
	 */
	public String getItem() {
		return operator;
	}
	
	/**
	 * Sets the item.
	 * 
	 * @param longName
	 *            the new item
	 */
	public void setItem(String longName) {
		this.operator = longName;
	}
	
	/**
	 * Gets the operator.
	 * 
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}
	
	/**
	 * Sets the operator.
	 * 
	 * @param operator
	 *            the new operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
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
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getSortOrder()
	 */
	@Override
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
}
