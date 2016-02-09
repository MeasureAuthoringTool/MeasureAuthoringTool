package mat.shared;

import java.util.List;

import mat.client.shared.GenericResult;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLParameter;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveUpdateCQLResult.
 */
public class SaveUpdateCQLResult extends GenericResult{
	
	
    /** The cql definition list. */
    List<CQLDefinition> cqlDefinitionList ;
	
	/** The definition. */
	CQLDefinition definition;
	
    /** The cql parameter list. */
    List<CQLParameter> cqlParameterList ;
	
    /** The parameter. */
    CQLParameter parameter;
    
    public static final int NAME_NOT_UNIQUE = 1;
    
	
	
	
	/**
	 * Gets the definition.
	 *
	 * @return the definition
	 */
	public CQLDefinition getDefinition() {
		return definition;
	}

	/**
	 * Sets the definition.
	 *
	 * @param definition the new definition
	 */
	public void setDefinition(CQLDefinition definition) {
		this.definition = definition;
	}

	/**
	 * Gets the cql definition list.
	 *
	 * @return the cql definition list
	 */
	public List<CQLDefinition> getCqlDefinitionList() {
		return cqlDefinitionList;
	}

	/**
	 * Sets the cql definition list.
	 *
	 * @param cqlDefinitionList the new cql definition list
	 */
	public void setCqlDefinitionList(List<CQLDefinition> cqlDefinitionList) {
		this.cqlDefinitionList = cqlDefinitionList;
	}

	/**
	 * Gets the cql parameter list.
	 *
	 * @return the cql parameter list
	 */
	public List<CQLParameter> getCqlParameterList() {
		return cqlParameterList;
	}

	/**
	 * Sets the cql parameter list.
	 *
	 * @param cqlParameterList the new cql parameter list
	 */
	public void setCqlParameterList(List<CQLParameter> cqlParameterList) {
		this.cqlParameterList = cqlParameterList;
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
