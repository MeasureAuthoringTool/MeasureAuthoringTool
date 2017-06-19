package mat.model.cql;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLFunctions.
 */
public class CQLFunctions implements IsSerializable {
	
	/** The id. */
	private String id;
	
	/** The function name. */
	private String functionName;
	
	/** The function logic. */
	private String functionLogic;
	
	/** The argument. */
	private List<CQLFunctionArgument> argument;
	
	/** The context. */
	private String context;
	
	private String commentString = "";
	
	/**
	 * Gets the function name.
	 *
	 * @return the function name
	 */
	public String getFunctionName() {
		return functionName.trim();
	}
	
	/**
	 * Sets the function name.
	 *
	 * @param functionName the new function name
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName.trim();
	}
	
	/**
	 * Gets the function logic.
	 *
	 * @return the function logic
	 */
	public String getFunctionLogic() {
		return functionLogic.trim();
	}
	
	/**
	 * Sets the function logic.
	 *
	 * @param functionLogic the new function logic
	 */
	public void setFunctionLogic(String functionLogic) {
		this.functionLogic = functionLogic.trim();
	}
	
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
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
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
}
