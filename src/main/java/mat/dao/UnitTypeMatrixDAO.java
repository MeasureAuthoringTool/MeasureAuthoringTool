package mat.dao;

import java.util.List;

import mat.dto.UnitMatrixDTO;
import mat.model.UnitMatrix;

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
