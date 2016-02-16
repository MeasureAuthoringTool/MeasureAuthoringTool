package mat.shared;

import mat.client.shared.GenericResult;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveUpdateCQLResult.
 */
public class SaveUpdateCQLResult extends GenericResult{
	
	private CQLModel cqlModel;
	
	private String cqlString;
	
	/** The definition. */
	private CQLDefinition definition;
	
	/** The parameter. */
	private CQLParameter parameter;
	
	public static final int NAME_NOT_UNIQUE = 1;
	public static final int NODE_NOT_FOUND = 2;
	
	public String getCqlString() {
		return cqlString;
	}
	
	public void setCqlString(String cqlString) {
		this.cqlString = cqlString;
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
}
