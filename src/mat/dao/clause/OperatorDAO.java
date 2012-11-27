package mat.dao.clause;

import java.util.List;

import mat.DTO.OperatorDTO;
import mat.dao.IDAO;
import mat.model.Operator;

/**
 * DAO interface for Operator
 *
 */
public interface OperatorDAO extends IDAO<Operator, String> {
	public List<OperatorDTO> getLogicalOperators();
	public List<OperatorDTO> getRelTimingperators();
	public List<OperatorDTO> getRelAssociationsOperators();
	
}
