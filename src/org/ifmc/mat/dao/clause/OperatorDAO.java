package org.ifmc.mat.dao.clause;

import java.util.List;

import org.ifmc.mat.DTO.OperatorDTO;
import org.ifmc.mat.dao.IDAO;
import org.ifmc.mat.model.Operator;

/**
 * DAO interface for Operator
 *
 */
public interface OperatorDAO extends IDAO<Operator, String> {
	public List<OperatorDTO> getLogicalOperators();
	public List<OperatorDTO> getRelTimingperators();
	public List<OperatorDTO> getRelAssociationsOperators();
	
}
