package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLFunctionArgument implements IsSerializable, Cloneable {
	private String id;
	private String argumentName;
	private String argumentType;
	private String otherType;
	private String qdmDataType;
	private String attributeName;
	private boolean isValid;
	
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public String getArgumentName() {
		return argumentName.trim();
	}
	public void setArgumentName(String argumentName) {
		this.argumentName = argumentName.trim();
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
	
	public String getReturnType() {
		if(this.qdmDataType != null) {
			return this.qdmDataType;
		} else if(this.otherType != null) {
			return this.otherType;
		} else {
			return this.argumentType;
		}
	}
	
	public CQLFunctionArgument clone() {
		CQLFunctionArgument argumentClone = new CQLFunctionArgument();
		argumentClone.setArgumentName(this.getArgumentName());
		argumentClone.setId(this.getId());
		argumentClone.setArgumentType(this.getArgumentType());
		argumentClone.setAttributeName(this.getAttributeName());
		argumentClone.setQdmDataType(this.getQdmDataType());
		argumentClone.setOtherType(this.getOtherType());
		return argumentClone;
	}
	
}
