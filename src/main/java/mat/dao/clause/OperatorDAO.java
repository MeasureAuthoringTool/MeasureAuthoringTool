package mat.dao.clause;

import java.util.List;

import mat.dto.OperatorDTO;
import mat.dao.IDAO;
import mat.model.Operator;

/**
 * DAO interface for Operator.
 */
public interface OperatorDAO extends IDAO<Operator, String> {
	
	/**
	 * Gets the logical operators.
	 * 
	 * @return the logical operators
	 */
	public List<OperatorDTO> getLogicalOperators();
	
	/**
	 * Gets the rel timingperators.
	 * 
	 * @return the rel timingperators
	 */
	public List<OperatorDTO> getRelTimingperators();
	
	/**
	 * Gets the rel associations operators.
	 * 
	 * @return the rel associations operators
	 */
	public List<OperatorDTO> getRelAssociationsOperators();
	
	/**
	 * Gets the all operators.
	 * 
	 * @return the all operators
	 */
	public List<OperatorDTO> getAllOperators();
	
}
