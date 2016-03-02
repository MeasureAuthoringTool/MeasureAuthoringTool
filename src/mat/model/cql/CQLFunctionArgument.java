package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLFunctionArgument implements IsSerializable {
	private String id;
	private String argumentName;
	private String argumentType;
	private String otherType;
	private String qdmDataType;
	private String attributeName;
	private boolean isValid;
	
	public String getArgumentName() {
		return argumentName;
	}
	public void setArgumentName(String argumentName) {
		this.argumentName = argumentName;
	}
	public String getArgumentType() {
		return argumentType;
	}
	public void setArgumentType(String argumentType) {
		this.argumentType = argumentType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getOtherType() {
		return otherType;
	}
	public void setOtherType(String otherType) {
		this.otherType = otherType;
	}
	public String getQdmDataType() {
		return qdmDataType;
	}
	public void setQdmDataType(String qdmDataType) {
		this.qdmDataType = qdmDataType;
	}
}
