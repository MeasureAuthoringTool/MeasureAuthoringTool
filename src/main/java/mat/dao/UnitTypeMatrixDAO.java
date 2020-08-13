package mat.dao;

import mat.dto.UnitMatrixDTO;
import mat.model.UnitMatrix;

import java.util.List;

/**
 * DAO interface for Unit, UnitType and Matrix tables operation.
 */
public interface UnitTypeMatrixDAO extends IDAO<UnitMatrix, String> {
	
	/**
	 * Gets the all unit matrix.
	 * 
	 * @return the all unit matrix
	 */
	public List<UnitMatrixDTO> getAllUnitMatrix();
}
