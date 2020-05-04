package mat.dto;


import com.google.gwt.user.client.rpc.IsSerializable;

import mat.client.codelist.HasListBox;

public class OperatorDTO implements IsSerializable, HasListBox {
	
	private String id;
	
	private String operator;
	
	private String operatorType;
	
	public OperatorDTO(){
		
	}
	
	public OperatorDTO(String id, String operator, String operatorType) {
		super();
		this.id = id;
		this.operator = operator;
		this.operatorType = operatorType;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getValue()
	 */
	@Override
	public String getValue() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getItem()
	 */
	@Override
	public String getItem() {
		return operator;
	}
	
	public void setItem(String longName) {
		this.operator = longName;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String getOperatorType() {
		return operatorType;
	}
	
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getSortOrder()
	 */
	@Override
	public int getSortOrder() {
		return 0;
	}
	
	
	
}
