package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OperatorType implements IsSerializable{
	private String id;
	private String operatorType;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOperatorType() {
		return operatorType;
	}
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

}
