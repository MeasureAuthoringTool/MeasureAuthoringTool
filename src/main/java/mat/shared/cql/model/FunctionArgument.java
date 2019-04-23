package mat.shared.cql.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FunctionArgument implements IsSerializable {

	private String name;
	private String returnType;
	
	public FunctionArgument() {
		this.setName("");
		this.setReturnType("");
	}
	
	public FunctionArgument(String name, String type) {
		this.setName(name);
		this.setReturnType(type);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
}
