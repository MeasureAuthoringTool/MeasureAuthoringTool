package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class CQLFunctions implements CQLExpression, IsSerializable {
	
	private String aliasName;
	
	/** The id. */
	private String id;
	
	/** The function name. */
	private String functionName;
	
	/** The function logic. */
	private String functionLogic;
	
	/** The argument. */
	private List<CQLFunctionArgument> argument;
	
	/** The context. */
	private String context = "Patient";
	
	private String commentString = "";
	
	private String returnType;

	
	/**
	 * Gets the argument list.
	 *
	 * @return the argument list
	 */
	public List<CQLFunctionArgument> getArgumentList() {
		return argument;
	}
	
	/**
	 * Sets the argument list.
	 *
	 * @param argumentList the new argument list
	 */
	public void setArgumentList(List<CQLFunctionArgument> argumentList) {
		this.argument = argumentList;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return getFunctionName();
	}

	@Override
	public void setName(String name) {
		setFunctionName(name);
	}

	@Override
	public String getLogic() {
		return getFunctionLogic();
	}

	@Override
	public void setLogic(String logic) {
		setFunctionLogic(logic);
	}

	public String getFunctionName() {
		return functionName.trim();
	}

	public void setFunctionName(String name) {
		this.functionName = name.trim();
	}

	public String getFunctionLogic() {
		return functionLogic.trim();
	}

	public void setFunctionLogic(String logic) {
		this.functionLogic = logic.trim();
	}
	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public String getContext() {
		return context;
	}
	
	/**
	 * Sets the context.
	 *
	 * @param context the new context
	 */
	public void setContext(String context) {
		this.context = context;
	}

	public String getCommentString() {
		return commentString;
	}

	public void setCommentString(String commentString) {
		this.commentString = commentString;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
}
