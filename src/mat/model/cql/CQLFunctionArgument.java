package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLFunctionArgument implements IsSerializable {
	private String argumentName;
	private String argumentType;
	
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
}
