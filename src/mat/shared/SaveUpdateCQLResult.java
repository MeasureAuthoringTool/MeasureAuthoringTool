package mat.shared;

import java.util.ArrayList;
import java.util.List;

import mat.client.shared.GenericResult;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveUpdateCQLResult.
 */
public class SaveUpdateCQLResult extends GenericResult{
	
	private CQLModel cqlModel;
	
	private String cqlString;
	
	private List<CQLErrors> cqlErrors = new ArrayList<CQLErrors>();
	
	/** The definition. */
	private CQLDefinition definition;
	
	/** The parameter. */
	private CQLParameter parameter;
	
	private CQLFunctions function;
	
	public static final int NAME_NOT_UNIQUE = 1;
	public static final int NODE_NOT_FOUND = 2;
	public static final int NO_SPECIAL_CHAR = 3;
	
	public String getCqlString() {
		return cqlString;
	}
	
	public void setCqlString(String cqlString) {
		this.cqlString = cqlString;
	}
	
	public List<CQLErrors> getCqlErrors() {
		return cqlErrors;
	}

	public void setCqlErrors(List<CQLErrors> cqlErrors) {
		this.cqlErrors = cqlErrors;
	}

	public CQLModel getCqlModel() {
		return cqlModel;
	}
	
	public void setCqlModel(CQLModel cqlModel) {
		this.cqlModel = cqlModel;
	}
	
	
	
	
	public CQLDefinition getDefinition() {
		return definition;
	}
	
	public void setDefinition(CQLDefinition definition) {
		this.definition = definition;
	}
	
	/**
	 * Gets the parameter.
	 *
	 * @return the parameter
	 */
	public CQLParameter getParameter() {
		return parameter;
	}
	
	/**
	 * Sets the parameter.
	 *
	 * @param parameter the new parameter
	 */
	public void setParameter(CQLParameter parameter) {
		this.parameter = parameter;
	}

	public CQLFunctions getFunction() {
		return function;
	}

	public void setFunction(CQLFunctions function) {
		this.function = function;
	}
}
