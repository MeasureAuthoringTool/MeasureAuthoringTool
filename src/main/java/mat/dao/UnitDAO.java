package mat.dao;

import java.util.List;

import mat.dto.UnitDTO;
import mat.model.Unit;

/**
 * DAO interface for Unit, UnitType and Matrix tables operation.
 */
public interface UnitDAO extends IDAO<Unit, String> {
	
	/**
	 * Gets the all units.
	 * 
	 * @return the all units
	 */
	public List<UnitDTO> getAllUnits();
}
