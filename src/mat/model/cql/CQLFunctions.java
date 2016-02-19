package mat.model.cql;

import java.util.List;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLFunctions implements IsSerializable {
	private String id;
	private String functionName;
	private String functionLogic;
	private List<CQLFunctionArgument> argumentList;
	private String context;
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getFunctionLogic() {
		return functionLogic;
	}
	public void setFunctionLogic(String functionLogic) {
		this.functionLogic = functionLogic;
	}
	public List<CQLFunctionArgument> getArgumentList() {
		return argumentList;
	}
	public void setArgumentList(List<CQLFunctionArgument> argumentList) {
		this.argumentList = argumentList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
}
