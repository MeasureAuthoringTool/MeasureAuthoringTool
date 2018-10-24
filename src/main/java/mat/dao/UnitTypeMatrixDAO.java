package mat.dao;

import java.util.List;

import mat.DTO.UnitMatrixDTO;
import mat.model.UnitTypeMatrix;

/**
 * DAO interface for Unit, UnitType and Matrix tables operation.
 */
public interface UnitTypeMatrixDAO extends IDAO<UnitTypeMatrix, String> {
	
	/**
	 * Gets the all unit matrix.
	 * 
	 * @return the all unit matrix
	 */
	public List<UnitMatrixDTO> getAllUnitMatrix();
}
