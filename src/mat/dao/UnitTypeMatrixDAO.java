package mat.dao;

import java.util.List;

import mat.DTO.UnitMatrixDTO;
import mat.model.UnitMatrix;

/**
 * DAO interface for Unit, UnitType and Matrix tables operation
 *
 */
public interface UnitTypeMatrixDAO extends IDAO<UnitMatrix, String> {
	public List<UnitMatrixDTO> getAllUnitMatrix();
}
